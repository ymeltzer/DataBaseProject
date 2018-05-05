import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnID;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.Condition;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SelectQuery;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Selecter{
        private Table table;
        private SelectQuery selectQuery;
        private boolean isDistinct;
        private Condition whereCondition;
        private ArrayList<SelectQuery.FunctionInstance> functionInstanceArrayList;
        private SelectQuery.OrderBy[] orderBys;
        private ColumnID[] selectedColumnNames;
        private ArrayList<ColumnID> selectedColumnNamesList;
        private ArrayList<Row> tableToReturn;

    public Selecter(SelectQuery selectQuery, Table table){
        this.table = table;//new cloned table of selected table
        this.selectQuery = selectQuery;
        this.isDistinct = selectQuery.isDistinct();
        this.whereCondition = selectQuery.getWhereCondition();
        this.functionInstanceArrayList = selectQuery.getFunctions();
        this.orderBys = selectQuery.getOrderBys();
        this.selectedColumnNames = selectQuery.getSelectedColumnNames();
        this.tableToReturn = new ArrayList<Row>();
        this.selectedColumnNamesList = new ArrayList<ColumnID>(Arrays.asList(selectQuery.getSelectedColumnNames()));

    }

    /**
     * this method is the driver method to call on a selector to have it run the select query
     * @return
     */
    public Table doSelect(){

       if(whereCondition!=null) {//is there a where condition?
           runConditionsOnTable();
       }

       if(selectedColumnNames.length == 1){// which column should we act on?

           if(selectedColumnNames[0].getColumnName().equals("*")){//act on the whole table if there's a "*"

               if(functionInstanceArrayList.size()!=0){ //checks the functions we must apply to columns
                  this.table = doFunctions(this.table);

                   if(isDistinct){
                       this.table = getDistinctRows(table);
                   }
               }
               if(functionInstanceArrayList.size()==0){
                   if(isDistinct){
                        this.table = getDistinctRows(table);

                   }
               }


           }

           if(!selectedColumnNames[0].getColumnName().equals("*")){//act on specified columns and remove irrelevant columns and column descriptions
                    this.table = removeColumns(this.table, selectedColumnNamesList);//removes
                    if(isDistinct){
                        this.table = getDistinctRows(this.table);
                    }
                    if(functionInstanceArrayList.size()!=0){
                        this.table = doFunctions(this.table);
                        if(isDistinct){
                            this.table =getDistinctRows(this.table);
                        }
                    }

                }

       }
       if(selectedColumnNames.length > 1){//if we are given columns
           this.table = removeColumns(this.table, selectedColumnNamesList);
           if(isDistinct){
               this.table = getDistinctRows(this.table);

           }
           if(this.orderBys.length != 0){
               this.table = new OrderBy(this.selectQuery, this.table).runOrderby();
           }
           if(functionInstanceArrayList.size()!=0){
               this.table = doFunctions(this.table);
               if(isDistinct){
                   this.table =getDistinctRows(this.table);
               }
           }
       }





        return this.table;
    }

    private ArrayList<Row> cloneTable(Table table){
        ArrayList<Row> arr = new ArrayList<Row>();
        for(Row row : table.getTable()){
            Row row1 = new Row();
            for(Object c : row.getTheCells()){
                Cell cell = (Cell) c;
                row1.add(new Cell(cell.getValue()));
            }
            arr.add(row1);
        }
        return arr;
    }

    /**
     * I go through the cloned table and as an initial step to select, I remove rows that dont meet the where condition
     * @return
     */
     public void runConditionsOnTable(){

        for(int i = 0; i< this.table.getTable().size(); i++){

            Row r = new WhereCondition(this.whereCondition, this.table, this.table.getTable().get(i)).implementCondition(this.table.getTable().get(i), this.whereCondition);
            if(r==null){
                this.table.getTable().remove(this.table.getTable().get(i));//remove non matching row
                i--;
            }
        }
        if(this.table.getTable().size()==0){
            throw new IllegalArgumentException("'Where Condition' doesn't match any rows in this table");
        }
    }

    /**
     * removes specified columns values and Column Description from selected cloned table
     * @param table
     * @param namesList
     */
    public Table removeColumns(Table table, ArrayList<ColumnID> namesList){



        for(int i = 0; i < table.getColumnNames().size(); i++) {//goes through our selected table
            boolean bool = false;
            for(ColumnID columnID: namesList) {//relevant columns

                if(columnID.getColumnName().equals(table.getColumnNames().get(i).getColumnName())){
                    bool = true;
                }

            }
            if(!bool){
                removeEntireColumn(table.getColumnNames().get(i), table);//removes specified columndescription and column from specified table
                i--;

            }
        }
        for(ColumnID id: namesList){
            boolean bool=false;
            for(ColumnDescription c : table.getColumnNames()){//check against the original table if all the column id's were hit
                if(id.getColumnName().compareToIgnoreCase(c.getColumnName())==0){
                    bool = true;
                }
            }
            if(!bool){
                throw new IllegalArgumentException(id.getColumnName()+ " is not a column in " + this.table.getTableName());
            }
        }

        return table;
    }



    /**
     * this method takes a table and applies the functions to the specified columns
     * it also changes the columns name appropriately ie from 'GPA" to AVG(GPA)
     * it also checks if we are doing a Function(Distinct column)
     * @param table
     * @return
     */
  private Table doFunctions(Table table){

      for (SelectQuery.FunctionInstance f : functionInstanceArrayList) {
          boolean bool =false;

        for(ColumnDescription c : table.getColumnNames()) {
            if (c.getColumnName().compareToIgnoreCase(f.column.getColumnName()) == 0) {
                if(f.isDistinct){
                    table = getDistinctRows(table);
                }

                switch (f.function) {
                    case AVG:
                        Double average = getAverage(c, table);
                        changeColumnValues(table, c, average, f);
                        bool=true;
                        continue;

                    case MAX:
                        Object obj = getMax(c, table);
                        changeColumnValues(table, c, obj, f);
                        bool=true;
                        continue;

                    case MIN:
                        obj = getMin(c, table);
                        changeColumnValues(table, c, obj, f);
                        bool=true;
                        continue;

                    case SUM:
                        obj = getSum(c, table);
                        changeColumnValues(table, c, obj, f);
                        bool=true;
                        continue;
                    case COUNT:
                        obj = getCount(c, table);
                        changeColumnValues(table, c, obj, f);
                        bool=true;
                        continue;
                }

            }



        }
          if(!bool) {
              throw new IllegalArgumentException("The function(" + f.function.toString() + ") you supplied is Not Supported");
          }

      }

        return table;
  }

    /**
     * get average of numbers supplied
     * @param c
     * @return
     */
  private double getAverage(ColumnDescription c, Table table){
      int indexInRow = table.getColumnIndex(c.getColumnName());
      double sum = 0.0;
      int dividend =0;

      for(Row r : table.getTable()) {
          if (r.getCell(indexInRow).getValue() != null) {
              if (r.getCell(indexInRow).getValue() instanceof Integer) {
                  sum += (Integer) r.getCell(indexInRow).getValue();
              }
              if (r.getCell(indexInRow).getValue() instanceof Double) {
                  sum += (Double) r.getCell(indexInRow).getValue();
              }
              dividend++;
          }
      }

      return  sum/dividend;
  }

    /**
     * returns object in double form of the maximum value in a column
     *
     * @param c
     * @param table
     * @return
     */
  private Object getMax(ColumnDescription c, Table table) throws IllegalArgumentException {
      ColumnDescription.DataType type = c.getColumnType();
      switch (type) {
          case INT:
              int indexInRow = table.getColumnIndex(c.getColumnName());
              Integer max = (Integer) table.getTable().get(0).getCell(indexInRow).getValue();
              for (Row r : table.getTable()) {
                  if (r.getCell(indexInRow).getValue() != null) {
                      Integer temp = (Integer) r.getCell(indexInRow).getValue();
                      if (max < temp) {
                          max = temp;
                      }
                  }
              }
              return max;
          case BOOLEAN:
              throw new IllegalArgumentException("Cant apply Max function to a boolean column");
          case DECIMAL:
              indexInRow = table.getColumnIndex(c.getColumnName());
              Double max1 = (Double) table.getTable().get(0).getCell(indexInRow).getValue();
              for (Row r : table.getTable()) {
                  if (r.getCell(indexInRow).getValue() != null) {
                      Double temp = (Double) r.getCell(indexInRow).getValue();
                      if (max1 < temp) {
                          max1 = temp;
                      }
                  }
              }
              return max1;
          case VARCHAR:
              indexInRow = table.getColumnIndex(c.getColumnName());
              Cell maxCell = table.getTable().get(0).getCell(indexInRow);
              for (Row r : table.getTable()) {
                  if (r.getCell(indexInRow).getValue() != null) {
                      String temp = (String) r.getCell(indexInRow).getValue();

                          if (maxCell.compareTo(temp) < 0) {//if true then max cell is before temp in the alphabet
                              maxCell.setValue(temp);
                          }


                  }
              }
              return maxCell.getValue();

      }
      throw new IllegalArgumentException(type +" is not a supported datatype");
  }
  private Object getMin(ColumnDescription c, Table table){
      ColumnDescription.DataType type = c.getColumnType();
      switch (type) {
          case INT:
              int indexInRow = table.getColumnIndex(c.getColumnName());
              Integer min = (Integer) table.getTable().get(0).getCell(indexInRow).getValue();
              for (Row r : table.getTable()) {
                  if (r.getCell(indexInRow).getValue() != null) {
                      Integer temp = (Integer) r.getCell(indexInRow).getValue();
                      if (min > temp) {
                          min = temp;
                      }
                  }
              }
              return min;
          case BOOLEAN:
              throw new IllegalArgumentException("Cant apply Max function to a boolean column");
          case DECIMAL:
              indexInRow = table.getColumnIndex(c.getColumnName());
              Double min1 = (Double) table.getTable().get(0).getCell(indexInRow).getValue();
              for (Row r : table.getTable()) {
                  if (r.getCell(indexInRow).getValue() != null) {
                      Double temp = (Double) r.getCell(indexInRow).getValue();
                      if (min1 > temp) {
                          min1 = temp;
                      }
                  }
              }
              return min1;
          case VARCHAR:
              indexInRow = table.getColumnIndex(c.getColumnName());
              Cell minCell = table.getTable().get(0).getCell(indexInRow);
              for (Row r : table.getTable()) {
                  if (r.getCell(indexInRow).getValue() != null) {
                      String temp = (String) r.getCell(indexInRow).getValue();

                          if (minCell.compareTo(temp) > 0) {//if true min cell is after temp in the alphabet
                              minCell.setValue(temp);
                          }

                  }
              }
              return minCell.getValue();

      }
      throw new IllegalArgumentException(type +" is not a supported datatype");
  }
  private Object getSum(ColumnDescription c, Table table){
      int indexInRow = table.getColumnIndex(c.getColumnName());
      Double sum = 0.0;
      for(Row r : table.getTable()) {
          if(r.getCell(indexInRow).getValue() != null){
              Double temp = (Double) r.getCell(indexInRow).getValue();
              sum = sum + temp;
          }
      }
      return sum;
  }
  private Object getCount(ColumnDescription c, Table table){
      int indexInRow = table.getColumnIndex(c.getColumnName());
      int count = 0;
      for(Row r : table.getTable()) {
          if(r.getCell(indexInRow).getValue() != null){
              count++;
          }
      }
      return count;
  }




    /**
     * changes all column values in our cloned table  to the functions result
     * @param t
     * @param c
     * @param valueToAdd
     */
  private void changeColumnValues(Table t, ColumnDescription c, Object valueToAdd, SelectQuery.FunctionInstance f){

      int indexInRow = table.getColumnIndex(c.getColumnName());
      for(Row r: t.getTable()){
          r.getTheCells().get(indexInRow).setValue(valueToAdd);
      }

      t.getColNameTypes().add(changeColumnDescriptionName(c,f));
  }

    /**
     * uses imprompto Column description that I created to hold new column name and data type
     * @param c
     * @return
     */
    private ColNameType changeColumnDescriptionName(ColumnDescription c, SelectQuery.FunctionInstance f){
        String newColumnName = f.function.toString()+"("+f.column.getColumnName()+")";
        ColNameType colNameType = new ColNameType(newColumnName, c.getColumnType());
        colNameType.setNotNull(c.isNotNull());
        colNameType.setUnique(c.isUnique());


        return colNameType;
    }

    /**
     * I get distinct columns according to standard sql practice
     * I only remove a non distinct value(and its row) from a column if the entire row (for selected columns) is not unique
     *
     * @param table
     * @return
     */
    private Table getDistinctRows(Table table){

        for(int i=0; i < table.getTable().size()-1; i++){
            for(int j = i+1; j < table.getTable().size(); j++){
                boolean bool = false;
               Row r1 = table.getTable().get(i);
               Row r2 = table.getTable().get(j);
                if(!areRowsDifferent(r1,r2)) {// compare two rows
                    table.getTable().remove(r2);
                    j--;//we decrement because table size has been altered(conversely could have used an iterator, but I couldn't get it to work)


                }


            }

        }
        return table;

    }
    private boolean areRowsDifferent(Row r1, Row r2){
      boolean bool=false;

        for(int i = 0; i < r1.getTheCells().size(); i++){
            Cell c1 = r1.getTheCells().get(i);
            Object o = r2.getTheCells().get(i).getValue();
            Cell c2 = r2.getTheCells().get(i);
            if(!c1.equals(c2)){
                bool=true;
            }
        }
        return bool;
    }

    /**
     * removes entire column and column description
     * @param c
     * @param t
     */
    public void removeEntireColumn(ColumnDescription c, Table t){
        int i = t.getColumnIndex(c.getColumnName());
        for(Row r: t.getTable()){
            r.removeCell(i);

        }
        t.getColumnNames().remove(i);

    }
    public void removeColumnDescription(String columnName, Table t){
        for (ColumnDescription c : t.getColumnNames()){
            if(c.getColumnName().equals(columnName)){
                t.getColumnNames().remove(c);
                break;
            }
        }
        throw new IllegalArgumentException(columnName +" does not exist in this table");
    }
}




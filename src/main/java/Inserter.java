import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnValuePair;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.InsertQuery;

import java.util.ArrayList;
import java.util.Arrays;



public class Inserter{

    private ArrayList<ColumnValuePair> colValArrayList;

    private Table table;

    public Inserter(InsertQuery insertQuery, Table table) {
        this.colValArrayList = new ArrayList<ColumnValuePair>(Arrays.asList(insertQuery.getColumnValuePairs()));
        this.table = table;

    }

    /**
     * this constructor is being used by my update query class
     * @param arr
     * @param table
     */
    public Inserter(ArrayList<ColumnValuePair> arr, Table table){
        this.colValArrayList = arr;
        this.table = table;
    }

    public Row insertRow() {
        Row row = new Row(); // row to hold values to be added

        for (ColumnDescription c : table.getColumnNames()) {
            boolean bool=false;

            for (ColumnValuePair col : this.colValArrayList){

                if (col.getColumnID().getColumnName().compareToIgnoreCase(c.getColumnName())==0) {//do column Names match?

                    if(CompareValToColumnType(col.getValue(), c)){//check if value matches column type
                        if(c.isUnique() || this.table.getPrimaryKeyColumn().getColumnName().compareToIgnoreCase(c.getColumnName())==0){//if primary key column it must also be unique
                            if(isValueUniqueInColumn(c,col.getValue())){

                                Cell cell = new Cell(castValue(col.getValue(), c));

                                    row.add(cell);
                                    bool=true;
                                    break;

                            }
                            if(!isValueUniqueInColumn(c,col.getValue())){
                                throw new IllegalArgumentException("This value = " + col.getValue()+ " occurs already in unique column");
                            }
                        }
                        Cell cell = new Cell(castValue(col.getValue(), c));//if column isnt unique
                            row.add(cell);
                            bool=true;
                            break;

                    }

                }


            }
            if(bool){
                continue;
            }
            if(c.isNotNull()){
                throw new IllegalArgumentException("Column = "+c.getColumnName()+ " cannot be left null");
            }

            if(c.getHasDefault()){//adds default value if there is one
                Cell cell= new Cell(castValue(c.getDefaultValue(), c));
                row.add(cell);
                continue;
            }
            if(!c.getHasDefault()){
                row.add(new Cell());
            }

        }
        for(ColumnValuePair c: colValArrayList){//I am checking if any column value pair was not inserted therby implying bad input due to misspelled column name or such
           boolean wasEveryColumnValPairUsed= false;
            for(ColumnDescription col: table.getColumnNames()){
                if(c.getColumnID().getColumnName().compareToIgnoreCase(col.getColumnName())==0){
                   wasEveryColumnValPairUsed =true;
                }
            }
            if(!wasEveryColumnValPairUsed){
                throw new IllegalArgumentException(c.getColumnID().getColumnName()+ " is not a column in table" + this.table.getTableName());
            }
        }
        //add the value to an existing btree
        addValueToTree(row);
        return row;

    }

    private boolean CompareValToColumnType(String s, ColumnDescription c) {

        //comparing column types to the values being added
        Object obj = castValue(s, c);



        if (c.getColumnType().name().equals("BOOLEAN")) {
            if(obj instanceof Boolean) {
                return true;
            }
            return false;
        }
        if (c.getColumnType().name().equals("INT")) {
            if (obj instanceof Integer) {
                return true;
            }
            throw new IllegalArgumentException("The Value does not match the column's data type");
        }
        if (c.getColumnType().name().equals("DECIMAL")) {
            if (obj instanceof Double) {
                return true;
            }
            throw new IllegalArgumentException("The Value does not match the column's data type");
        }
        if (c.getColumnType().name().equals("VARCHAR")) {//in this method we will also check the varchars length
            if (obj instanceof String) {
                return true;
            }

            throw new IllegalArgumentException("The Value does not match the column's data type,");
        }
        throw new IllegalArgumentException("The value "+ s + "  is not an acceptable value for this table ");
    }

    /**
     * casts value and cuts varchar to length and checks decimal
     * @param toBeParsed
     * @param c
     * @return
     */

    private Object castValue(String toBeParsed, ColumnDescription c){



       switch(c.getColumnType()){
           case INT:
               Object object = Integer.parseInt(toBeParsed);
                return object;
           case BOOLEAN:
               object = toBeParsed.toLowerCase();
               if (object.equals("true")) {

                   return true;
               }
               if (object.equals("false")) {

                   return false;
               } else
                   throw new IllegalArgumentException("Cell with value" + toBeParsed + " does not contain a proper value");

           case DECIMAL:

               int x = toBeParsed.length();
               int indexOfDot = toBeParsed.indexOf(".");

               String untilDot = toBeParsed.substring(0, indexOfDot);

               String afterDot = toBeParsed.substring(indexOfDot+1, x);

               if (c.getWholeNumberLength() > 0 && ( (untilDot.length()) > (c.getWholeNumberLength()))) {
                   throw new IllegalArgumentException(toBeParsed + " is bad value. Before decimal val should be length="+c.getWholeNumberLength()+"!="+untilDot.length());
               }
               if (c.getFractionLength() > 0 && ( (afterDot.length()) > (c.getFractionLength()))) {

                   toBeParsed = toBeParsed.substring(0,indexOfDot+c.getFractionLength()+1);

               }

           return  Double.parseDouble(toBeParsed);

           case VARCHAR:
                toBeParsed = toBeParsed.trim();//I didn't remove the single quotes
                if(c.getVarCharLength() > 0 && toBeParsed.length() > c.getVarCharLength()){
                    throw new IllegalArgumentException("The varchar you are inserting is longer then its Constraint");
                }

                return toBeParsed;
       }
       throw new IllegalArgumentException("the string passed in isn't a proper datatype");
    }




    /**
     * returns boolean whether the this value is already found in the column
     * @param c
     * @param s
     * @return
     */
    private boolean isValueUniqueInColumn(ColumnDescription c, String s){

            for(Cell cell : getColumn(c)){

                if(cell.compareTo(castValue(s, c))==0){
                    return false;
                }

            }
            return true;

    }

    public ArrayList<Cell> getColumn(ColumnDescription c){
      ArrayList<Cell> arr = new ArrayList<Cell>();
      int index = this.table.getColumnIndex(c.getColumnName());

      for(Row r : this.table.getTable()){
          arr.add(r.getCell(index));
      }
      return arr;
    }

    /**
     * if a btree exists for a value's column we add the value to the tree
     * @param r
     */
    public void addValueToTree(Row r){
       for(ColumnValuePair col : colValArrayList) {
           String columnName = col.getColumnID().getColumnName();

           if ((this.table.getBTreeByName(columnName) != null) && (col.getValue()!=null)) {
               Object key = castValue(col.getValue(), this.table.getColumnNames().get(this.table.getColumnIndex(columnName)));
               this.table.getBTreeByName(columnName).put((Comparable)key , r);

           }
       }
    }




}





import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.CreateTableQuery;
import net.sf.jsqlparser.schema.Column;

import java.util.*;


public class Table {
    private String tableName;
    private ArrayList<ColumnDescription> columnNames;
    private ColumnDescription primaryKeyColumn;
    private ArrayList<Row> theTable;
    private ArrayList<ColumnDescription.DataType> columnDataTypes = new ArrayList<ColumnDescription.DataType>();
    private ArrayList<ColNameType> colNameTypes;
    private ArrayList<BTree> bTrees;


    public Table(CreateTableQuery query){
        this.tableName = query.getTableName();
        this.columnNames = new ArrayList<ColumnDescription>(Arrays.asList(query.getColumnDescriptions()));
        this.primaryKeyColumn = query.getPrimaryKeyColumn();
        this.theTable = new ArrayList<Row>();
        for(ColumnDescription c : columnNames){
            columnDataTypes.add(c.getColumnType());

        }
        this.bTrees = new ArrayList<>();//new btree container
        addPrimaryKeyIndex();//indexes primary key

    }


    /**
     * I create another Table constructor as an additional cloning system for parameter of select condition
     * @param anotherTable
     */
    public Table(Table anotherTable){
        this.tableName = anotherTable.getTableName();

        this.columnNames = (ArrayList<ColumnDescription>) anotherTable.getColumnNames().clone();
        this.primaryKeyColumn = anotherTable.getPrimaryKeyColumn();
        this.theTable = anotherTable.cloneTable();
        for(ColumnDescription c : this.columnNames){
            columnDataTypes.add(c.getColumnType());
        }
        this.colNameTypes = new ArrayList<ColNameType>();
        this.bTrees = anotherTable.getBTrees();
    }

    public void setColNameTypes(ArrayList<ColNameType> arr){
        this.colNameTypes = arr;
    }
    public ArrayList<ColNameType> getColNameTypes(){
        return this.colNameTypes;
    }

    public String getTableName(){
        return this.tableName;
    }

    public ArrayList<ColumnDescription> getColumnNames(){
        return this.columnNames;
    }
    public ArrayList<Row> getTable(){
        return this.theTable;

    }

    public void addRow(Row row){
        this.theTable.add(row);
    }
    public ColumnDescription getPrimaryKeyColumn(){
        return this.primaryKeyColumn;
    }

    public ArrayList<ColumnDescription.DataType> getDataTypes() {
        return this.columnDataTypes;

    }

     public ArrayList<Cell> getColumn(String columnName){
        ArrayList<Cell> list = new ArrayList<Cell>();
        int index =0;
        for(Row row : getTable()){
            Cell cell = new Cell(row.getTheCells().get(getColumnIndex(columnName)).getValue());
            list.add(cell);
        }

        return list;
    }

    public BTree getBTreeByName(String nameToGet){
        for(BTree b: this.bTrees){
            if(b.getName().compareToIgnoreCase(nameToGet)==0) {
                return b;
            }
        }
        return null;
    }
    public ArrayList<BTree> getBTrees(){
        return this.bTrees;
    }



   public int getColumnIndex(String columnName){
       int counter = 0;
       for(ColumnDescription c :this.columnNames){
           if(c.getColumnName().compareToIgnoreCase(columnName)==0){
               return counter;
           }
           counter++;
       }
       return counter;
   }
    public ColumnDescription.DataType getDataTypeForIndex(int i){
       return this.columnDataTypes.get(i);
    }
    public ArrayList<Cell> getColumn(ColumnDescription c){
        ArrayList<Cell> arr = new ArrayList<Cell>();
        int index = getColumnIndex(c.getColumnName());

        for(Row r : getTable()){
            Cell cell = r.getCell(index);
            arr.add(cell);
        }
        return arr;
    }


    public void printTable(){
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("Table Name = " + this.tableName);
        System.out.println("Primary Key column = " + this.primaryKeyColumn.getColumnName());
        System.out.println(" ");

        System.out.println("-      -      -      -      -      -      -      -      -      -      -      -      -      -      -      -      -      -");
        for (ColumnDescription c : this.columnNames) {
            System.out.printf("%-20s", c.getColumnName());
        }
        System.out.println();
        System.out.println("-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -");
        int i = 0;

        for (Row row : this.theTable) {
            for (Cell cell : row.getTheCells()) {
                if (cell.getValue() != null) {
                    System.out.printf("%-20s", cell.getValue().toString());
                    continue;
                }
                System.out.printf("%-20s", "null");
            }
            System.out.println();
        }
        System.out.println("-----------------------------------------------------------------------------------------");


    }

    /**
     * deep clones a table and return an array list of its values
     * deep clones down to cell value
     * @return
     */
    public ArrayList<Row> cloneTable(){
        ArrayList<Row> arr = new ArrayList<Row>();
        for(Row row : this.theTable){
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
     * gets index of a row within a table
     * used for Updating a row
     */
    int getRowIndex(Row r){
        if(contains(r)){
            return (theTable.indexOf(r));
        }
        return -1;
    }

    private boolean contains(Row r2){
        for(Row r1: this.theTable){
            if(!areRowsDifferent(r1,r2)){
                return true;
            }
        }
        return false;
    }
    private boolean areRowsDifferent(Row r1, Row r2){
        boolean bool=false;
        for(Cell c: r1.getTheCells()){

                if (c.compareTo(r2.getCell(r1.getCellIndex(c)).getValue()) < (0)) {
                    bool = true;
                }

        }
        return bool;
    }


   public ColumnDescription getColumnDescription(String columnName)throws IllegalArgumentException{
        for(ColumnDescription c : getColumnNames()){
            if(c.getColumnName().compareToIgnoreCase(columnName)==0){
                return c;
            }
        }
        throw new IllegalArgumentException("Column: "+ columnName +" Does not exist exist in table "+ this.tableName);
   }


    public ColumnDescription getColumnDescription(int index){
        return columnNames.get(index);
    }
    public ColumnDescription.DataType getColumnDataTape(int index){
        return columnDataTypes.get(index);
    }

    public boolean isColumnInTable(String columnName){
        boolean bool =false;
        for(ColumnDescription c : this.columnNames){
            if(c.getColumnName().compareToIgnoreCase(columnName)==0){
                bool=true;
            }
        }
        return bool;
    }

    /**
     * returns the alternative column description i created for select functions
     * @param columnName
     * @return
     */
    public int getColumnIndexForFunctions(String columnName){
        int counter = 0;
        for(ColNameType c :this.colNameTypes){
            if(c.getColumnName().compareToIgnoreCase(columnName)==0){
                return counter;
            }
            counter++;
        }
        return counter;
    }

    /**
     * this method is specifically for the select tests in my junit testing.
     * I remove columns in order to compare original column to result set return values
     * @param columnName
     */
    public void removeColumnTest(String columnName){
        int index = getColumnIndex(columnName);
        for(Row r: this.theTable){
            r.removeCell(index);
        }
        columnNames.remove(index);

    }
    public void addPrimaryKeyIndex(){
       // int index = getColumnIndex(primaryKeyColumn.getColumnName());
        ColumnDescription.DataType type = primaryKeyColumn.getColumnType();

        switch(type){
            case INT:
                BTree<Integer, Row> btree = new BTree<Integer, Row>(primaryKeyColumn.getColumnName());
                this.bTrees.add(btree);
                break;
            case BOOLEAN:
                throw new IllegalArgumentException("Primary Key column cannot be a Boolean dataType");
            case DECIMAL:
                BTree<Double, Row> btree1 = new BTree<Double, Row>(primaryKeyColumn.getColumnName());
                this.bTrees.add(btree1);
                break;
            case VARCHAR:
                BTree<String, Row> btree2 = new BTree<String, Row>(primaryKeyColumn.getColumnName());
                this.bTrees.add(btree2);
                break;

        }

    }









    @Override
    public String toString() {
        return "Table{" +
                ", columnNames=" + columnNames +
                ", theTable=" + theTable +
                '}';
    }
}





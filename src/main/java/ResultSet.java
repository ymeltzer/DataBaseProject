import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;
import net.sf.jsqlparser.statement.alter.AlterExpression;

import java.util.ArrayList;

public class ResultSet {


    private ArrayList<ColumnDescription> columnDescriptions;
    private ArrayList<ColNameType> columnDescriptionsWithAlteredNames;//used when we alter column Name ie. AVG(GPA). has same getters as ColumnDescription
    private ArrayList<Row> resultingRows;
    private boolean wasQuerySuccessful;
    private String exceptionMessage;
    private Table theTable;


    public ResultSet(boolean bool){
        this.wasQuerySuccessful = bool;
        resultingRows = new ArrayList<Row>();
        Row row = new Row();
        if(bool){
            row.add(new Cell(true));
            resultingRows.add(row);
        }
        if(!bool){//in case the query didnt work for some reason I return false and return the error message
            row.add(new Cell(false));
        }
        resultingRows.add(row);
    }
    public ResultSet(boolean bool, Exception e){
        this.wasQuerySuccessful = bool;
        this.resultingRows = new ArrayList<Row>();
        Row row = new Row();
        if(bool){
            row.add(new Cell(true));
            this.resultingRows.add(row);
        }
        if(!bool){//in case the query didnt work for some reason I return false and return the error message
            row.add(new Cell(false));
            row.add(new Cell(e.getMessage()));
            this.exceptionMessage = e.getMessage();
        }
        this.resultingRows.add(row);
    }

    public ResultSet(Table table){//for SelectQuery and create table
        this.resultingRows = table.getTable();
        this.theTable = table;
        if(table.getColNameTypes()==null|| table.getColNameTypes().size()==0){
            this.columnDescriptions = table.getColumnNames();
        }
        if(table.getColNameTypes()==null||table.getColNameTypes().size()!=0){
            this.columnDescriptionsWithAlteredNames = table.getColNameTypes();
        }
        this.wasQuerySuccessful= true;
    }


    public ArrayList<Row> getResultingRows(){
       return this.resultingRows;
    }
    public Table getTheTable(){
        return this.theTable;
    }
    public ArrayList<ColumnDescription> getColumnDescriptions() {
        return this.columnDescriptions;
    }
    public boolean getWasQuerySuccessful(){
        return this.wasQuerySuccessful;
    }

    /**
     * I still need to remove quotes for varchar
     */

    public void printResultSet(){
        System.out.println("ResultSet of your query returns = ");
        if(wasQuerySuccessful){
            System.out.println("true");
        }
        if(!wasQuerySuccessful){
            System.out.println("false");
                System.out.println("Error = " + exceptionMessage);

        }
    }
    public void printResultSetSelect(){
        System.out.println("-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  ");
        System.out.println("ResultSet of your query returns = ");
        if(this.wasQuerySuccessful) {
            if(columnDescriptionsWithAlteredNames == null) {

                    for (ColumnDescription c : columnDescriptions) {
                        System.out.printf("%-20s", c.getColumnName());
                    }

            }
            if(columnDescriptionsWithAlteredNames!=null) {
                for (ColNameType c : columnDescriptionsWithAlteredNames) {
                    System.out.printf("%-20s", c.getColumnName());
                }
            }
            System.out.println();

            for (Row row : resultingRows) {

                for (Cell cell : row.getTheCells()) {
                    if (cell.getValue() != null) {
                        System.out.printf("%-20s",  cell.getValue().toString());
                    }
                    if (cell.getValue() == null) {
                        System.out.printf("%-20s", "null" );
                    }
                }
                System.out.println();
            }
        }
        else{
            System.out.println("false");
            System.out.println(this.exceptionMessage);
        }
        System.out.println("-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  ");
    }
}

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.*;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.statement.create.table.CreateTable;


import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

/**
 * DataBase class
 * class that takes original string query and uses parser to figure
  the command and what to do with it
 */
public class DataBase{



    private ArrayList<Table> dataBase;

    public DataBase(){
        this.dataBase = new ArrayList<Table>();
    }

    public static void main(String[] args) throws JSQLParserException{
/**
        SQLParser parser = new SQLParser();
        SelectQuery result = (SelectQuery)parser.parse("SELECT first, last, id FROM students,teachers,schools;");
        //result = (SelectQuery)parser.parse("SELECT column1, column2 FROM table1 WHERE column3='some value' AND (column4='some value2' OR column4='some other value');");
        //result = (SelectQuery)parser.parse("SELECT * FROM YCStudent, RIESTStudent WHERE YCStudent.BannerID = RIETS.BannerID;");
        //result = (SelectQuery)parser.parse("select * from students;");
        //result = (SelectQuery)parser.parse("select * from students WHERE CurrentStudent=TRUE;");
        //result = (SelectQuery)parser.parse("select distinct first, last, id from students;");
        //result = (SelectQuery)parser.parse("select first, last, id from students where id=1234;");
        //result = (SelectQuery)parser.parse("select first, last, id from students where id=1234 AND first='moshe';");
        result = (SelectQuery)parser.parse("SELECT * FROM YCStudent ORDER BY GPA ASC, BannerID DESC;");
        SelectQuery.OrderBy[] orderbys =  result.getOrderBys();
        result = (SelectQuery)parser.parse("SELECT AVG(GPA) FROM STUDENTS;");
        //result = (SelectQuery)parser.parse("SELECT COUNT(DISTINCT GPA) FROM STUDENTS;");
*/

        DataBase dataBase = new DataBase();
        String query = "CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " SSNum int UNIQUE,"
                + " Class varchar(255),"
                + " FirstName varchar(255),"
                + " LastName varchar(255) NOT NULL,"
                + " GPA decimal(1,2) DEFAULT 0.00,"
                + " CurrentStudent boolean DEFAULT true,"
                + " PRIMARY KEY (BannerID)"
                + ");";
        dataBase.execute(query);
      dataBase.printDataBase();

        ResultSet resultSet1= dataBase.execute("INSERT INTO ycstudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Yudi','Meltzer',3.0,800092345,'Freshman');");
        ResultSet resultSet2= dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Yosef','Epstein',3.42,800002345,'Freshman');");
        ResultSet resultSet3= dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Yudi','Meltzer',3.0,800012745);");
        ResultSet resultSet4= dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Gav','Sturm',3.5,800012845);");
        ResultSet resultSet5= dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Leah','Meltzer',3.0,800012945);");
        ResultSet resultSet6= dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Dad','Meltzer',3.0,800012245);");
        ResultSet resultSet7= dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Fraydy','Meltzer',3.4,800012249);");
        dataBase.printDataBase();
        ResultSet resultSet12 = dataBase.execute("UPDATE YCStudent SET GPA=3.0,Class='Super Senior' WHERE BannerID=800012245;");
        dataBase.printDataBase();


/**



        DataBase dataBase = new DataBase();
        SQLParser parser = new SQLParser();
        UpdateQuery result = (UpdateQuery)parser.parse("UPDATE YCStudent SET GPA=3.0,Class='Super Senior' WHERE BannerID=800012345;");
        result = (UpdateQuery)parser.parse("UPDATE YCStudent SET GPA=3.0,Class='Super Senior';");
        System.out.println();
 //SQLParser parser = new SQLParser();
       String query = "CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " SSNum int UNIQUE,"
                + " Class varchar(255),"
                + " FirstName varchar(255),"
                + " LastName varchar(255) NOT NULL,"
                + " GPA decimal(1,2) DEFAULT 0.00,"
                + " CurrentStudent boolean DEFAULT true,"
                + " PRIMARY KEY (BannerID)"
                + ");";

       //CreateTableQuery result = (CreateTableQuery) parser.parse(query);

      // System.out.print("");

        dataBase.execute(query);
        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Ploni','Almoni',4.0,800012345,'Super Senior');");
        //dataBase.execute("DELETE From YCStudent;");
        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Yudi','Meltzer',3.4,800092345,'Freshman');");
        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Yosef','Epstein',3.42,800002345,'Freshman');");
        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Aaron','Shakib',3.0,800012745);");
        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Gav','Sturm',3.2,800012845);");
        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Leah','Meltzer',3.4,800012945);");
        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Dad','Meltzer',3.2,800012245);");

    */

      /*  for(Object t: dataBase.getDataBase()){
            Table table =(Table) t;
            dataBase.printTable(table);
        }
       dataBase.execute("DELETE FROM YCStudent WHERE CurrentStudent=true AND GPA < 3.0;");
        System.out.println();
        for(Object t: dataBase.getDataBase()){
            Table table =(Table) t;
            dataBase.printTable(table);
        }

*/

        // InsertQuery result = (InsertQuery)parser.parse("INSERT INTO YCStudent (FirstName, LastName, GPA, Class, BannerID) VALUES ('Ploni','Almoni',4.0, 'Senior',800012345);");
        //System.out.println();
       //dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Ploni','Almoni',4.0,800012345);");

       // dataBase.execute("DELETE FROM YCStudent WHERE Class='Super Senior' AND GPA > 3.0;");
       // System.out.println();


       //SelectQuery result = (SelectQuery)parser.parse("select * from YCStudents;");
       //DeleteQuery c = (DeleteQuery)parser.parse("DELETE FROM YCStudent;");
        //DeleteQuery result = (DeleteQuery)parser.parse("DELETE FROM YCStudent WHERE Class='Super Senior' AND GPA < 3.0;");
        //NOTE: for some strange reason JSQLParser can't handle the "*" in "DELETE * FROM" - wierd!!
        //result = (DeleteQuery)parser.parse("DELETE FROM YCStudent;");
   //     Condition condition = (Condition) result.getWhereCondition().getRightOperand();
       // System.out.println(condition.getLeftOperand().toString());
       // query = "CREATE INDEX SSNum_Index on YCStudent (SSNum);";
      //  CreateIndexQuery result9 = (CreateIndexQuery)parser.parse(query);
       // System.out.println();


    }



    private SQLQuery parseQuery(String query) throws JSQLParserException {//uses sql parser to turn string to object command

        SQLParser sqlParser = new SQLParser();
        SQLQuery sqlQuery = sqlParser.parse(query);
        return sqlQuery;
    }

    /**
     *
     * @param query
     * @return
     * @throws JSQLParserException
     * executes Sql command
     *
     */
    public ResultSet execute(String query) throws JSQLParserException {//takes object and deciphers the type of command object it is
        SQLQuery sqlQuery = parseQuery(query);
        try {

            if (sqlQuery instanceof CreateTableQuery) {
                CreateTableQuery createTableQuery = (CreateTableQuery) sqlQuery;
                this.dataBase.add(new Table(createTableQuery));
                return new ResultSet(true);
            }
        }
        catch(Exception exception){
            return new ResultSet(false, exception);
        }

        if(sqlQuery instanceof InsertQuery){
            try {
                InsertQuery insertQuery = (InsertQuery) sqlQuery;

                Inserter inserter = new Inserter(insertQuery, getTableByName(insertQuery.getTableName()));
                Row row = inserter.insertRow();
                getTableByName(insertQuery.getTableName()).addRow(row);
                return new ResultSet(true);
            }
            catch(Exception exception){
                return new ResultSet(false, exception);
            }

        }

        if(sqlQuery instanceof DeleteQuery){
                try {
                    DeleteQuery deleteQuery = (DeleteQuery) sqlQuery;

                    Deleter deleter = new Deleter(deleteQuery, getTableByName(deleteQuery.getTableName()), this);
                    deleter.deleteRow();
                    return new ResultSet(true);
                }
                catch(Exception exception){
                    return new ResultSet(false, exception);
                }


        }
        if(sqlQuery instanceof SelectQuery){
              try {
                    SelectQuery selectQuery = (SelectQuery) sqlQuery;
                    Table table = new Table(getTableByName(selectQuery.getFromTableNames()[0]));

                    table = new Selecter(selectQuery, table).doSelect();
                    return new ResultSet(table);
                }
                catch(Exception exception) {
                return new ResultSet(false, exception);
            }


        }

        if(sqlQuery instanceof CreateIndexQuery){
           CreateIndexQuery createIndexQuery = (CreateIndexQuery) sqlQuery;
        }


        if(sqlQuery instanceof UpdateQuery) {
            try {
                UpdateQuery updateQuery = (UpdateQuery) sqlQuery;
                boolean bool = new Updater(updateQuery, getTableByName(updateQuery.getTableName())).doUpdate();
                return new ResultSet(bool);
            }
            catch (Exception exception) {
                return new ResultSet(false, exception);
            }
        }

        return new ResultSet(false);

    }

    public ArrayList<Table> getDataBase() {
        return dataBase;
    }





    /*
     * this method creates a new  row of values in the proper table
     * ensuring values are in proper columns
     * I may add this method to the table class
     * @param insertQuery
     */

    //returns a specific table from the database given a name
    public Table getTableByName(String tableName){
        for(Table t : this.dataBase){
            if(t.getTableName().compareToIgnoreCase(tableName)==0){
                return t;
            }
        }
        throw new IllegalArgumentException("Table " + tableName + " Doesn't Exist");
    }





    /**
     * deletes entire table if there is no where condition
     * deltes specified rows if there is a where condition
     * @param deleteQuery
     */
    /**
    private void doDeleteQuery(DeleteQuery deleteQuery){

       Table theTable = getTableByName(deleteQuery.getTableName());
        if(deleteQuery.getWhereCondition()==null){
            this.dataBase.remove(dataBase.indexOf(getTableByName(deleteQuery.getTableName())));
        }
        if(deleteQuery.getWhereCondition()!=null){

            WhereCondition whereCondition = new WhereCondition(deleteQuery.getWhereCondition(), theTable);
            ArrayList<Row> arrayList = whereCondition.checkCondition();
            for(Row r : arrayList){
                for(Row row : getTableByName(deleteQuery.getTableName()).getTable()){

                    if(row.equals(r)){
                        getTableByName(deleteQuery.getTableName()).getTable().remove(row);
                    }
                }
            }



        }

    }
 private void removeRow(Table table, Row r){
        int counter =0;
        for(Row row : table.getTable()){
            boolean bool = true;

            // stuck on comparing cells
            for(Cell c : row.getTheCells()){
                int index = row.getCellIndex(c);//get of cell from first row

                Cell cell2 =r.getTheCells().get(counter);//get second cell using
               if(c.compareTo(cell2)!=0 ){
                   continue;
               }

                counter++;
            }

        }

    }
     */
    /**
    V castCell(String cellVal, ColumnDescription columnDescription){

        Object returnVal = null;

        switch(columnDescription.getColumnType()){

            case VARCHAR:
                returnVal = cellVal;
                break;
            case DECIMAL:
                returnVal = Float.parseFloat(cellVal);
                break;
            case BOOLEAN:
                if(cellVal.equals("true")){
                    returnVal = true;
                }
                if(cellVal.equals("false")){
                    returnVal = false;
                }
                break;
            case INT:
                returnVal = Integer.parseInt(cellVal);
        }
        return (V) cellVal;
    }
     */
    public void printDataBase(){
        for(Table table: this.dataBase) {
            System.out.println("-----------------------------------------------------------------------------------------");
            System.out.println("Table Name = " + table.getTableName());
            System.out.println("Primary Key column = " + table.getPrimaryKeyColumn());
            System.out.println(" ");

            System.out.println("-      -      -      -      -      -      -      -      -      -      -      -      -      -      -      -      -      -");
            for (ColumnDescription c : table.getColumnNames()) {
                System.out.printf("%-20s", c.getColumnName());
            }
            System.out.println();
            System.out.println("-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -");
            int i = 0;

            for (Row row : table.getTable()) {
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
    }

}










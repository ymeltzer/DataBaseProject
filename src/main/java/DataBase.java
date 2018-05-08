import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.*;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.statement.create.table.CreateTable;


import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This is an SQL DATABASE
 * Note* any method left longer than 30 lines was done so to increase readability and I have made notes where I have done so(it is usually for a switch statement)
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
/**

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
     // dataBase.printDataBase();

        ResultSet resultSet1= dataBase.execute("INSERT INTO ycstudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Yudi','Meltzer',3.0,800092345,'Freshman');");
        ResultSet resultSet2= dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Yosef','Epstein',3.42,800002345,'Freshman');");
        ResultSet resultSet3= dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Yudi','Sturm',3.0,800012745);");
        ResultSet resultSet4= dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Gav','Sturm',3.5,800012845);");
        ResultSet resultSet5= dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Leah','Jacobs',3.0,800012945);");
        ResultSet resultSet6= dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Dad','Meltzer',3.0,800012245);");
        ResultSet resultSet7= dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Fraydy','Meltzer',3.4,800012249);");
        dataBase.printDataBase();



       ResultSet resultSet13 = dataBase.execute("SELECT * FROM YCStudent ORDER BY GPA DESC, LastName ASC;");
  //     dataBase.printDataBase();
       resultSet13.printResultSetSelect();
*/
/**
        SQLParser parser = new SQLParser();
        String s = "CREATE INDEX SSNum_Index on YCStudent (SSNum);";
        CreateIndexQuery result = (CreateIndexQuery) parser.parse(s);
        System.out.println();
*/


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
     * I have left this method larger then 30 lines because to break up the code would make it more confusing for the reader
     *
     *
     */
    public ResultSet execute(String query) throws JSQLParserException {//takes object and deciphers the type of command object it is
        SQLQuery sqlQuery = parseQuery(query);
       try {
            if (sqlQuery instanceof CreateTableQuery) {
                CreateTableQuery createTableQuery = (CreateTableQuery) sqlQuery;
                this.dataBase.add(new Table(createTableQuery));
                return new ResultSet(getTableByName(createTableQuery.getTableName()));
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
            try {
                CreateIndexQuery createIndexQuery = (CreateIndexQuery) sqlQuery;
                Table table = getTableByName(createIndexQuery.getTableName());
                int index = table.getColumnIndex(createIndexQuery.getColumnName());
                Indexer indexer = new Indexer(table, createIndexQuery);
                table.getBTrees().add(indexer.doIndex());
                return new ResultSet(true);
                }
                catch(Exception exception){
                return new ResultSet(false, exception);
                }
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
     * This is a really nice print method I wrote to print out the tables in the database
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










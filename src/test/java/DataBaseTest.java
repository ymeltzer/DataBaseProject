import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SQLParser;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SelectQuery;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.schema.Database;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.Assert.*;

public class DataBaseTest {
    DataBase dataBase1 = new DataBase();
    public void createTwoTables() throws JSQLParserException {
        String query = "CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " Class varchar(255),"
                + " FirstName varchar(255),"
                + " LastName varchar(255) NOT NULL,"
                + " GPA decimal(1,2) DEFAULT 0.00,"
                + " PRIMARY KEY (BannerID)"
                + ");";

        dataBase1.execute(query);

        String query1 = "CREATE TABLE Family"
                + "("
                + " Age int,"
                + " NumberInFamily int,"
                + " FirstName varchar(255),"
                + " LastName varchar(255) NOT NULL,"
                + " Size decimal(1,2) DEFAULT 0.00,"
                + " PRIMARY KEY (NumberInFamily)"
                + ");";
        dataBase1.execute(query1);

    }
    public void addRows() throws JSQLParserException {
        dataBase1.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Yudi','Meltzer',3.4,800092345,'Freshman');");
        dataBase1.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Yosef','Epstein',3.42,800002345,'Freshman');");
        dataBase1.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Aaron','Shakib',3.0,800012745);");
        dataBase1.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Gav','Sturm',3.2,800012845);");
        dataBase1.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Leah','Meltzer',3.4,800012945);");
        dataBase1.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Dad','Meltzer',3.2,800012245);");
        dataBase1.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Michael','Freund',2.2,800010245, 'Freshman');");
        dataBase1.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Leah','Mckinsey',3.7,800012249,'Freshman');");
        dataBase1.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Hannah','Newman',1.7,800012045,'Freshman');");
        dataBase1.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Hannah','Urban',1.0,800017775,'Freshman');");

        dataBase1.execute("INSERT INTO Family (FirstName, LastName, Age, Size, NumberInFamily) VALUES ('Yudi','Meltzer',22,5.10,4);");
        dataBase1.execute("INSERT INTO Family (FirstName, LastName, Age, Size, NumberInFamily) VALUES ('Yaisef','Meltzer',24,5.8,3);");
        dataBase1.execute("INSERT INTO Family (FirstName, LastName, Age, Size, NumberInFamily) VALUES ('Chana','Meltzer',27,5.5,1);");
        dataBase1.execute("INSERT INTO Family (FirstName, LastName, Age, Size, NumberInFamily) VALUES ('Rivka','Meltzer',20,5.9,6);");
        dataBase1.execute("INSERT INTO Family (FirstName, LastName, Age, Size, NumberInFamily) VALUES ('Leah','Meltzer',25,5.4,2);");
        dataBase1.execute("INSERT INTO Family (FirstName, LastName, Age, Size, NumberInFamily) VALUES ('Dad','Meltzer',53,5.10,11);");
        dataBase1.execute("INSERT INTO Family (FirstName, LastName, Age, Size, NumberInFamily) VALUES ('Mom','Meltzer',53,5.7,22);");
        dataBase1.execute("INSERT INTO Family (FirstName, LastName, Age, Size, NumberInFamily) VALUES ('Fraydy','Meltzer',17,5.5,7);");
        dataBase1.execute("INSERT INTO Family (FirstName, LastName, Age, Size, NumberInFamily) VALUES ('Avrumi','Meltzer',13,6.2,8);");
        dataBase1.execute("INSERT INTO Family (FirstName, LastName, Age, Size, NumberInFamily) VALUES ('Sarah','Meltzer',11,5.1,9);");

    }
    /**
     * I create a table and test that a line was inserted
     * @throws JSQLParserException
     */
    @Test
    public void createTableAndInsert() throws JSQLParserException {

        DataBase dataBase = new DataBase();
        //Create table query
        String query = "CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " Class varchar(255),"
                + " FirstName varchar(255),"
                + " LastName varchar(255) NOT NULL,"
                + " GPA decimal(1,2) DEFAULT 0.00,"
                + " PRIMARY KEY (BannerID)"
                + ");";

         dataBase.execute(query);
         dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Yudi','Meltzer',3.4,800092345,'Freshman');");
        // dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Yosef','Epstein',3.42,800002345,'Freshman');");
        // dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Aaron','Shakib',3.0,800012745);");
         //dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Gav','Sturm',3.2,800012845);");
         //dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Leah','Meltzer',3.4,800012945);");
         //dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Dad','Meltzer',3.2,800012245);");


        Row row = dataBase.getTableByName("YCStudent").getTable().get(0);
        int index1 =dataBase.getTableByName("YCStudent").getColumnIndex("LastName");
        int index2 =dataBase.getTableByName("YCStudent").getColumnIndex("BannerID");
        int index3 =dataBase.getTableByName("YCStudent").getColumnIndex("FirstName");
        int index4 =dataBase.getTableByName("YCStudent").getColumnIndex("GPA");
        int index5 =dataBase.getTableByName("YCStudent").getColumnIndex("Class");
        assertTrue(dataBase.getTableByName("YCStudent").getTable().size()==1);
        assertTrue(row.getCell(index3).getValue().toString().equals("'Yudi'"));
        assertTrue(row.getCell(index1).getValue().toString().equals("'Meltzer'"));
        assertTrue(row.getCell(index4).getValue().toString().equals("3.4"));
        assertTrue(row.getCell(index2).getValue().toString().equals("800092345"));
        assertTrue(row.getCell(index5).getValue().toString().equals("'Freshman'"));
    }

    /**
     * I delete a row from a table after I insert a row
     * @throws JSQLParserException
     */
    @Test
    public void delete()throws JSQLParserException{
        DataBase dataBase = new DataBase();
        //Create table query
        String query = "CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " Class varchar(255),"
                + " FirstName varchar(255),"
                + " LastName varchar(255) NOT NULL,"
                + " GPA decimal(1,2) DEFAULT 0.00,"
                + " PRIMARY KEY (BannerID)"
                + ");";
        dataBase.execute(query);
        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Yudi','Meltzer',3.4,800092345,'Freshman');");
        dataBase.execute("DELETE FROM YCStudent WHERE LastName='Meltzer' AND GPA > 3.0;");
        assertTrue(dataBase.getTableByName("YCStudent").getTable().size()==0);
    }

    /**
     * for this method I print the tabe after all the ros are deleted to show that the rows have been deleted
     * @throws JSQLParserException
     */
    @Test
    public void deleteEntireTable() throws JSQLParserException {
        createTwoTables();
        addRows();
        dataBase1.execute("DELETE FROM YCStudent");
        assertTrue(dataBase1.getTableByName("YCStudent").getTable().size()==0);
        dataBase1.getTableByName("YCStudent").printTable();


    }

    /**
     * test delete using where condition
     * @throws JSQLParserException
     */
    @Test
    public void deleteWHERE() throws JSQLParserException {
        createTwoTables();
        addRows();

        dataBase1.execute("Delete FROM YCStudent WHERE LastName='Meltzer';");
        int index = dataBase1.getTableByName("YCStudent").getColumnIndex("LastName");
        boolean bool = false;
        for(Row r: dataBase1.getTableByName("YCStudent").getTable()){
            if(r.getTheCells().get(index).getValue().equals("'Meltzer'")){
                bool=true;
            }
        }
        assert(!bool);
        assertTrue(dataBase1.getTableByName("YCStudent").getTable().size()==7);

    }

    /**
     * I update a row using the where condition after it has been inserted in a table.
     * @throws JSQLParserException
     */
    @Test
    public void Update() throws JSQLParserException {
        DataBase dataBase = new DataBase();
        //Create table query
        String query = "CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " Class varchar(255),"
                + " FirstName varchar(255),"
                + " LastName varchar(255) NOT NULL,"
                + " GPA decimal(1,2) DEFAULT 0.00,"
                + " PRIMARY KEY (BannerID)"
                + ");";

        dataBase.execute(query);
        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Yudi','Meltzer',3.4,800092345,'Freshman');");
        dataBase.execute("UPDATE YCStudent SET GPA=3.6,Class='Freshman' WHERE BannerID=800092345;");


        Row row = dataBase.getTableByName("YCStudent").getTable().get(0);

        int index2 =dataBase.getTableByName("YCStudent").getColumnIndex("BannerID");
        int index4 =dataBase.getTableByName("YCStudent").getColumnIndex("GPA");
        int index5 =dataBase.getTableByName("YCStudent").getColumnIndex("Class");
        assertTrue(dataBase.getTableByName("YCStudent").getTable().size()==1);
        assertTrue(row.getCell(index4).getValue().toString().equals("3.6"));
        assertTrue(row.getCell(index2).getValue().toString().equals("800092345"));
        assertTrue(row.getCell(index5).getValue().toString().equals("'Freshman'"));
    }
    @Test
    public void update2() throws JSQLParserException {
        DataBase dataBase = new DataBase();
        //Create table query
        String query = "CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " Class varchar(255),"
                + " FirstName varchar(255),"
                + " LastName varchar(255) NOT NULL,"
                + " GPA decimal(1,2) DEFAULT 0.00,"
                + " PRIMARY KEY (BannerID)"
                + ");";

        dataBase.execute(query);
        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Yudi','Meltzer',3.4,800092345,'Freshman');");
        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Yosef','Epstein',3.42,800002345,'Freshman');");
        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Aaron','Shakib',3.0,800012745);");
        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Gav','Sturm',3.2,800012845);");
        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Leah','Meltzer',3.4,800012945);");
        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Dad','Meltzer',3.2,800012245);");
        dataBase.execute("UPDATE YCStudent SET GPA=2.1,Class='Senior';");
        int index4 =dataBase.getTableByName("YCStudent").getColumnIndex("GPA");
        int index5 =dataBase.getTableByName("YCStudent").getColumnIndex("Class");
        for(Row r: dataBase.getTableByName("YCStudent").getTable()){
            assertTrue(r.getCell(index4).getValue().toString().equals("2.1"));
            assertTrue(r.getCell(index5).getValue().toString().equals("'Senior'"));
        }
    }

    /**
     * test wrong table name by update query
     * @throws Exception
     */
    @Test
    public void update3() throws JSQLParserException {
        createTableAndInsert();
        addRows();
        ResultSet rs = dataBase1.execute("UPDATE Schools SET GPA=2.1,Class='Senior';");
        assertTrue(!rs.getWasQuerySuccessful());


    }

    /**
     * test wrong column name in where condition
     * @throws JSQLParserException
     */

    @Test
    public void update4() throws JSQLParserException {
        createTwoTables();
        addRows();
        ResultSet rs = dataBase1.execute("UPDATE YCStudent SET GPA=2.1,Class='Senior' WHERE Banner=800012245;");
        assertTrue(!rs.getWasQuerySuccessful());
    }

    /**
     * checks the where condition and ensures that the returned result set contains desired values.
     * once I have shown the where condition works I will no longer use it in other Select Query tests
     * @throws JSQLParserException
     */
    @Test
    public void whereCondition() throws JSQLParserException {
        createTwoTables();
        addRows();
        ResultSet rs = dataBase1.execute("SELECT * FROM YCStudent WHERE GPA > 3.4;");
        int index = dataBase1.getTableByName("YCStudent").getColumnIndex("GPA");
        for(Row r: rs.getResultingRows()){
            assertTrue(r.getCell(index).compareTo(3.4)>0);
        }

    }

    /**
     * in the next few methods I test various select methods
     * @throws JSQLParserException
     */
    @Test
    public void select1()throws JSQLParserException{
        createTwoTables();
        addRows();
        ResultSet rs = dataBase1.execute("select * from YCStudent;");

        assertTrue(dataBase1.getTableByName("YCStudent").toString().equals(rs.getTheTable().toString()));
    }

    /**
     * Here I show that I can select specific columns so in the further tests I shan't select specific columns
     * @throws JSQLParserException
     */
    @Test
    public void select2()throws JSQLParserException{

        createTwoTables();
        addRows();
        ResultSet rs = dataBase1.execute("select LastName, FirstName from YCStudent;");
        ArrayList<Cell> arr = dataBase1.getTableByName("YCStudent").getColumn("LastName");
        ArrayList<Cell> arr1 = rs.getTheTable().getColumn("LastName");
        for(int i = 0; i< arr.size(); i++){
          assertTrue(arr.get(i).getValue().toString().equals(arr1.get(i).getValue().toString()));
      }
        ArrayList<Cell> arr3 = dataBase1.getTableByName("YCStudent").getColumn("FirstName");
        ArrayList<Cell> arr4 = rs.getTheTable().getColumn("FirstName");
        for(int i = 0; i< arr.size(); i++){
            assertTrue(arr4.get(i).getValue().toString().equals(arr3.get(i).getValue().toString()));
        }
    }

    /**
     * tests that non distinct rows are removed from table
     * @throws JSQLParserException
     */
    @Test
    public void select3DISTINCT()throws JSQLParserException{

        String query = "CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " Class varchar(255),"
                + " FirstName varchar(255),"
                + " LastName varchar(255) NOT NULL,"
                + " GPA decimal(1,2) DEFAULT 0.00,"
                + " PRIMARY KEY (BannerID)"
                + ");";

        dataBase1.execute(query);
        dataBase1.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Yudi','Meltzer',3.4,800092345,'Freshman');");
        dataBase1.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Yudi','Meltzer',3.4,800092755,'Freshman');");



       ResultSet rs = dataBase1.execute("select distinct LastName, FirstName from YCStudent;");
        assertTrue(rs.getTheTable().getTable().size()==1);

        for(Row r: rs.getTheTable().getTable()){
            assertTrue(r.getTheCells().get(rs.getTheTable().getColumnIndex("LastName")).getValue().equals("'Meltzer'"));
        }
        for(Row r: rs.getTheTable().getTable()){
            assertTrue(r.getTheCells().get(rs.getTheTable().getColumnIndex("FirstName")).getValue().equals("'Yudi'"));
        }

    }

    /**
     * this method tests all the select functions
     * I print the result sets of the select queries for easy understanding
     * and then I do unit tests as well
     * @throws JSQLParserException
     */
    @Test
    public void selectFunctions1()throws JSQLParserException{
        String query = "CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " Class varchar(255),"
                + " FirstName varchar(255),"
                + " LastName varchar(255) NOT NULL,"
                + " GPA decimal(1,2) DEFAULT 0.00,"
                + " PRIMARY KEY (BannerID)"
                + ");";

        dataBase1.execute(query);
        dataBase1.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Yudi','Meltzer',3.4,800092345,'Freshman');");
        dataBase1.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Jasen','Meltzer',3.5,800092349,'Freshman');");
        ResultSet rs = dataBase1.execute("SELECT COUNT(DISTINCT GPA) FROM YCSTUDENT;");
        System.out.println("Query = " + "SELECT COUNT(DISTINCT GPA) FROM YCSTUDENT;");
        rs.printResultSetSelect();

        ResultSet rs1 = dataBase1.execute("SELECT AVG(GPA) FROM YCSTUDENT;");
        System.out.println("Query = " + "SELECT COUNT(DISTINCT GPA) FROM YCSTUDENT;");
        rs1.printResultSetSelect();

        ResultSet rs2 = dataBase1.execute("SELECT SUM(GPA) FROM YCSTUDENT;");
        System.out.println("Query = " + "SELECT SUM(GPA) FROM YCSTUDENT;");
        rs2.printResultSetSelect();

        ResultSet rs3 = dataBase1.execute("SELECT MAX(GPA) FROM YCSTUDENT;");
        System.out.println("Query = " + "SELECT MAX(GPA) FROM YCSTUDENT;");
        rs3.printResultSetSelect();

        ResultSet rs4 = dataBase1.execute("SELECT MIN(GPA) FROM YCSTUDENT;");
        System.out.println("Query = " + "SELECT MIN(GPA) FROM YCSTUDENT;");
        rs4.printResultSetSelect();


        for(Row r: rs.getTheTable().getTable()){
            assertTrue(r.getTheCells().get(rs.getTheTable().getColumnIndexForFunctions("COUNT(GPA)")).getValue().toString().equals("2"));
        }
        for(Row r: rs2.getTheTable().getTable()){
            assertTrue(r.getTheCells().get(rs2.getTheTable().getColumnIndexForFunctions("SUM(GPA)")).getValue().toString().equals("6.9"));
        }
        for(Row r: rs1.getTheTable().getTable()){
            assertTrue(r.getTheCells().get(rs1.getTheTable().getColumnIndexForFunctions("AVG(GPA)")).getValue().toString().equals("3.45"));
        }
        for(Row r: rs3.getTheTable().getTable()){
            assertTrue(r.getTheCells().get(rs3.getTheTable().getColumnIndexForFunctions("MAX(GPA)")).getValue().toString().equals("3.5"));
        }
        for(Row r: rs4.getTheTable().getTable()){
            assertTrue(r.getTheCells().get(rs4.getTheTable().getColumnIndexForFunctions("MIN(GPA)")).getValue().toString().equals("3.4"));
        }


    }

    /**
     * checks that a btree is created and that all the required values have been added
     * @throws JSQLParserException
     */
    @Test
    public void createIndexTest() throws JSQLParserException {
        String query = "CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " Class varchar(255),"
                + " FirstName varchar(255),"
                + " LastName varchar(255) NOT NULL,"
                + " GPA decimal(1,2) DEFAULT 0.00,"
                + " PRIMARY KEY (BannerID)"
                + ");";

        dataBase1.execute(query);
        dataBase1.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Yudi','Meltzer',3.4,800092345,'Freshman');");
        dataBase1.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Jasen','Meltzer',3.5,800092349,'Freshman');");

        dataBase1.execute("CREATE INDEX GPA_Index on YCStudent (GPA);");
        assertTrue(dataBase1.getTableByName("YCStudent").getBTrees().size()==2);
        ArrayList<BTree.Entry> entries = dataBase1.getTableByName("YCStudent").getBTreeByName("GPA").getOrderedEntries();
        assertTrue(entries.get(0).getKey().equals(3.4));
        assertTrue(entries.get(1).getKey().equals(3.5));

    }
    /*
   I test my btree method for getting all values whos key is less than given key
    */
    @Test
    public void btreeGetMethods1() throws JSQLParserException {
       createTwoTables();
       addRows();
        //I should now have two indexes; gpa, and Primary key column
        dataBase1.execute("CREATE INDEX LastName_Index on YCStudent (GPA);");
        BTree<String, Row> st = dataBase1.getTableByName("YCStudent").getBTreeByName("GPA");

        HashSet<Row> entries = dataBase1.getTableByName("YCStudent").getBTreeByName("GPA").getLessThan(3.5);
        int index = dataBase1.getTableByName("YCStudent").getColumnIndex("GPA");
        //I use this index to receive the GPA column from my hashset
        for(Row r : entries){
            assertTrue(r.getCell(index).compareTo(3.5)<0);//assert that each returned value from the btree is less than 3.5 as desired
        }

    }
    /*
   I test my btree method for getting all values whos key is greater than given key
    */
    @Test
    public void btreeGetMethods2() throws JSQLParserException {
      createTwoTables();
      addRows();
        //I should now have two indexes; gpa, and Primary key column
        dataBase1.execute("CREATE INDEX LastName_Index on YCStudent (GPA);");
        BTree<String, Row> st = dataBase1.getTableByName("YCStudent").getBTreeByName("GPA");

        HashSet<Row> entries = dataBase1.getTableByName("YCStudent").getBTreeByName("GPA").getGreaterThan(3.0);
        int index = dataBase1.getTableByName("YCStudent").getColumnIndex("GPA");
        //I use this index to receive the GPA column from my hashset
        for(Row r : entries){
            assertTrue(r.getCell(index).compareTo(3.0)>0);//assert that each returned value from the btree is greater than 3.0 as desired
        }

    }
    /*
   I test my btree method for getting all values who's key is greater than or equal to given key
    */
    @Test
    public void btreeGetMethods3() throws JSQLParserException {
       createTwoTables();
       addRows();
        //I should now have two indexes; gpa, and Primary key column
        dataBase1.execute("CREATE INDEX LastName_Index on YCStudent (GPA);");
        BTree<String, Row> st = dataBase1.getTableByName("YCStudent").getBTreeByName("GPA");

        HashSet<Row> entries = dataBase1.getTableByName("YCStudent").getBTreeByName("GPA").getGreaterThanEquals(3.42);
        int index = dataBase1.getTableByName("YCStudent").getColumnIndex("GPA");
        //I use this index to receive the GPA column from my hashset
        for(Row r : entries){
            assertTrue(r.getCell(index).compareTo(3.42)>=0);//assert that each returned value from the btree is greater than 3.0 as desired
        }

    }
    /*
   I test my btree method for getting all values whos less than or equal to given key
    */
    @Test
    public void btreeGetMethods4() throws JSQLParserException {
        createTwoTables();
        addRows();
        //I should now have two indexes; gpa, and Primary key column
        dataBase1.execute("CREATE INDEX LastName_Index on YCStudent (GPA);");
        BTree<String, Row> st = dataBase1.getTableByName("YCStudent").getBTreeByName("GPA");

        HashSet<Row> entries = dataBase1.getTableByName("YCStudent").getBTreeByName("GPA").getLessThanEquals(3.5);
        int index = dataBase1.getTableByName("YCStudent").getColumnIndex("GPA");
        //I use this index to receive the GPA column from my hashset
        for(Row r : entries){
            assertTrue(r.getCell(index).compareTo(3.5)<=0);//assert that each returned value from the btree is greater than 3.0 as desired
        }

    }
    /*
    I test my btree method for getting all values whos key is not equal to given key
     */
    @Test
    public void btreeGetMethods5() throws JSQLParserException {
        createTwoTables();
        addRows();
        //I should now have two indexes; gpa, and Primary key column
        dataBase1.execute("CREATE INDEX LastName_Index on YCStudent (GPA);");
        BTree<String, Row> st = dataBase1.getTableByName("YCStudent").getBTreeByName("GPA");

        HashSet<Row> entries = dataBase1.getTableByName("YCStudent").getBTreeByName("GPA").getNotEquals(3.0);
        int index = dataBase1.getTableByName("YCStudent").getColumnIndex("GPA");
        //I use this index to receive the GPA column from my hashset
        for(Row r : entries){
            assertTrue(r.getCell(index).compareTo(3.0)!=0);//assert that each returned value from the btree is greater than 3.0 as desired
        }

    }
    /*
    I test my btree method for getting all values with given key
     */
    @Test
    public void btreeGetMethods6() throws JSQLParserException {
        createTwoTables();
        addRows();
        //I should now have two indexes; gpa, and Primary key column
        dataBase1.execute("CREATE INDEX LastName_Index on YCStudent (GPA);");
        BTree<String, Row> st = dataBase1.getTableByName("YCStudent").getBTreeByName("GPA");

        HashSet<Row> entries = new HashSet<>();
        entries.addAll(dataBase1.getTableByName("YCStudent").getBTreeByName("GPA").get(3.0));
        int index = dataBase1.getTableByName("YCStudent").getColumnIndex("GPA");
        //I use this index to receive the GPA column from my hashset
        for(Row r : entries){
            assertTrue(r.getCell(index).compareTo(3.0)==0);//assert that each returned value from the btree is greater than 3.0 as desired
        }

    }


}
public class DBTest {
    public static void main(String[] args){
        try {
            System.out.println("SQLDataBase written by Yudi Meltzer.");
            System.out.println("This DataBase demo demonstrates numerous command options and their results");
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            DataBase dataBase = new DataBase();
            //Create table query
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

            ResultSet resultSet = dataBase.execute(query);


            System.out.println("Query = " + query);
            resultSet.printResultSetSelect();//returns empty result set with column names
            dataBase.printDataBase();

            //insert query
            ResultSet resultSet1 = dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Yudi','Meltzer',3.4,800092345,'Freshman');");
            ResultSet resultSet2 = dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Yosef','Epstein',3.42,800002345,'Freshman');");
            ResultSet resultSet3 =dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Aaron','Shakib',3.0,800012745);");
            ResultSet resultSet4 =dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Gav','Sturm',3.2,800012845);");
            ResultSet resultSet5 =dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Leah','Meltzer',3.4,800012945);");
            ResultSet resultSet6 =dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Dad','Meltzer',3.2,800012245);");

            System.out.println("Query = " + "INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Yudi','Meltzer',3.4,800092345,'Freshman');");
            resultSet1.printResultSet();
            System.out.println("Query = " + "INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Yosef','Epstein',3.42,800002345,'Freshman');");
            resultSet2.printResultSet();
            System.out.println("Query = " + "INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Aaron','Shakib',3.0,800012745);");
            resultSet3.printResultSet();
            System.out.println("Query = " + "INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Gav','Sturm',3.2,800012845);");
            resultSet4.printResultSet();
            System.out.println("Query = " + "INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Leah','Meltzer',3.4,800012945);");
            resultSet5.printResultSet();
            System.out.println("Query = " + "INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Dad','Meltzer',3.2,800012245);");
            resultSet6.printResultSet();
            dataBase.printDataBase();



            ResultSet resultSet8 = dataBase.execute("DELETE FROM YCStudent WHERE LastName='Meltzer' AND GPA > 3.0;");
            System.out.println("Query = " + "DELETE FROM YCStudent WHERE LastName='Meltzer' AND GPA > 3.0;");
            resultSet8.printResultSet();
            ResultSet resultSet9 = dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Dad','Meltzer',3.2,800012245);");
            System.out.println("Query = " + "INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Dad','Meltzer',3.2,800012245);");
            resultSet9.printResultSet();
            ResultSet resultSet10 = dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Yudi','Meltzer',3.4,800092345,'Freshman');");
            System.out.println("Query = " + "INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Yudi','Meltzer',3.4,800092345,'Freshman');");
            resultSet10.printResultSet();
            ResultSet resultSet11 = dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Leah','Meltzer',3.4,800012945);");
            System.out.println("Query = " + "INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Leah','Meltzer',3.4,800012945);");
            resultSet11.printResultSet();
            dataBase.printDataBase();

            ResultSet resultSet12 = dataBase.execute("UPDATE YCStudent SET GPA=3.0,Class='Super Senior' WHERE BannerID=800012245;");
            System.out.println("Query = " + "UPDATE YCStudent SET GPA=3.0,Class='Super Senior' WHERE BannerID=800012245;");
            resultSet12.printResultSet();
            dataBase.printDataBase();


            ResultSet resultSet13 = dataBase.execute("SELECT FirstName, GPA FROM YCStudent WHERE LastName='Meltzer' AND (GPA>3.0 OR GPA<2.0);");
            System.out.println("Query = " + "SELECT FirstName, GPA FROM YCStudent WHERE LastName='Meltzer' AND (GPA>3.0 OR GPA<2.0);");
            resultSet13.printResultSetSelect();


            ResultSet resultSet14 = dataBase.execute("SELECT * FROM YCStudent WHERE BannerID = 800092345;");
            System.out.println("Query = " + "SELECT * FROM YCStudent, YCStudent WHERE BannerID = 800092345;");
            resultSet14.printResultSetSelect();


            ResultSet resultSet15 = dataBase.execute("select * from YCstudent;");
            System.out.println("Query = " + "select * from YCstudent;");
            resultSet15.printResultSetSelect();

            ResultSet resultSet16 = dataBase.execute("select * from YCstudent WHERE CurrentStudent=TRUE;");
            System.out.println("Query = " + "select * from YCstudent WHERE CurrentStudent=TRUE;");
            resultSet16.printResultSetSelect();

            ResultSet resultSet17 = dataBase.execute("select distinct LastName, FirstName, BannerID from YCstudent;");
            System.out.println("Query = " + "select distinct LastName, FirstName, BannerID from YCstudent;");
            resultSet17.printResultSetSelect();

            ResultSet resultSet18 = dataBase.execute("SELECT * FROM YCStudent ORDER BY GPA ASC, Credits DESC;");
            System.out.println("Query = " + "SELECT * FROM YCStudent ORDER BY GPA ASC, BannerID DESC;");
            resultSet18.printResultSetSelect();

            ResultSet resultSet19 = dataBase.execute("SELECT * FROM YCStudent ORDER BY LastName ASC, GPA DESC;");
            System.out.println("Query = " + "SELECT * FROM YCStudent ORDER BY LastName ASC, GPA DESC;");
            resultSet19.printResultSetSelect();

            ResultSet resultSet20 = dataBase.execute("SELECT COUNT(DISTINCT GPA) FROM YCSTUDENT;");
            System.out.println("Query = " + "SELECT COUNT(DISTINCT GPA) FROM YCSTUDENT;");
            resultSet20.printResultSetSelect();

            ResultSet resultSet21 = dataBase.execute("SELECT AVG(GPA) FROM YCSTUDENT;");
            System.out.println("Query = " + "SELECT AVG(GPA) FROM YCSTUDENT;");
            resultSet21.printResultSetSelect();

            ResultSet resultSet22 = dataBase.execute("SELECT SUM(GPA) FROM YCSTUDENT;");
            System.out.println("Query = " + "SELECT SUM(GPA) FROM YCSTUDENT;");
            resultSet22.printResultSetSelect();

            ResultSet resultSet23 = dataBase.execute("SELECT MAX(GPA) FROM YCSTUDENT;");
            System.out.println("Query = " + "SELECT MAX(GPA) FROM YCSTUDENT;");
            resultSet23.printResultSetSelect();

            ResultSet resultSet24 = dataBase.execute("SELECT MIN(GPA) FROM YCSTUDENT;");
            System.out.println("Query = " + "SELECT MIN(GPA) FROM YCSTUDENT;");
            resultSet24.printResultSetSelect();

            ResultSet resultSet25 = dataBase.execute("SELECT MIN(GPA), MAX(LastName) FROM YCSTUDENT;");
            System.out.println("Query = " + "SELECT MIN(GPA), MAX(LastName) FROM YCSTUDENT;");
            resultSet25.printResultSetSelect();

            ResultSet resultSet26 = dataBase.execute("UPDATE YCStudent SET GPA=2.0,Class='Super Senior';");
            System.out.println("Query = " + "UPDATE YCStudent SET GPA=2.0,Class='Super Senior';");
            resultSet26.printResultSet();
            dataBase.printDataBase();

            ResultSet resultSet27 = dataBase.execute("CREATE INDEX GPA_Index on YCStudent (GPA);");
            System.out.println("Query = " + "CREATE INDEX LastName_Index on YCStudent (GPA);");
            resultSet27.printResultSet();
            System.out.println();
            ResultSet resultSet28 = dataBase.execute("CREATE INDEX LastName_Index on YCStudent (LastName);");
            System.out.println("Query = " + "CREATE INDEX LastName_Index on YCStudent (LastName);");
            resultSet28.printResultSet();
            System.out.println();
            System.out.println("Table is now Indexed by :");
            System.out.println("-   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   ");
            int counter = 0;
            for(BTree b : dataBase.getTableByName("YCStudent").getBTrees()){
                counter++;
                System.out.println(counter+ ". " + b.getName());
            }
            System.out.println("-   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   ");






        }
        catch(Exception e){

        }
    }

}

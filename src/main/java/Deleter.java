import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.Condition;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.DeleteQuery;
import jdk.nashorn.internal.objects.annotations.Where;

import java.util.ArrayList;
import java.util.Iterator;

public class Deleter {
    private Table table;
    private Condition condition;
    private DataBase dataBase;
    private DeleteQuery deleteQuery;

    public Deleter(DeleteQuery deleteQuery, Table table, DataBase dataBase) {
        this.table = table;
        this.condition = deleteQuery.getWhereCondition();
        this.dataBase = dataBase;
        this.deleteQuery = deleteQuery;
    }

    /**
     * this method drives the deleting of a row. it checks relavent conditions first and then delets rows
     */

    public void deleteRow() {

        if (this.condition == null) {

            for (int i = 0; i < this.dataBase.getTableByName(table.getTableName()).getTable().size(); i++) {
                //remove relevant row keys from tree

                removeRowFromTree(this.table.getTable().get(i));
                this.dataBase.getTableByName(table.getTableName()).getTable().remove(i);
                i--;
            }
        }
        if (this.condition != null) {
            boolean bool = false;
            for (int i = 0; i < this.dataBase.getTableByName(table.getTableName()).getTable().size(); i++) {

                WhereCondition whereCondition = new WhereCondition(this.deleteQuery.getWhereCondition(), this.table, this.dataBase.getTableByName(table.getTableName()).getTable().get(i));

                    Row row = whereCondition.implementCondition(this.dataBase.getTableByName(table.getTableName()).getTable().get(i), this.condition);

                    if (row != null) {
                        //remove relevant row keys from tree

                        removeRowFromTree(this.table.getTable().get(i));
                        this.dataBase.getTableByName(table.getTableName()).getTable().remove(i);
                        i--;
                        bool = true;
                    }

            }
            if (!bool) {
                throw new IllegalArgumentException("The Where clause you entered: " + this.condition.toString() + ", does not match any rows in table: " + this.table.getTableName());
            }
        }
    }

    /**
     * method which calls on btree delete
     * we effectively remove a row from the container of values held by the key
     * we do this in all the trees that contain this row
     * @param r
     */
    public void removeRowFromTree(Row r){
        for(int i = 0; i < r.getTheCells().size(); i++){

            Cell c = r.getCell(i);
            String columnName = table.getColumnNames().get(i).getColumnName();
            ColumnDescription.DataType type = this.table.getDataTypeForIndex(this.table.getColumnIndex(columnName));

            switch(type){
                case VARCHAR:
                    if(this.table.getBTreeByName(columnName)!=null) {
                        this.table.getBTreeByName(columnName).delete((String)c.getValue(), r);
                        break;

                    }
                case DECIMAL:
                    if(this.table.getBTreeByName(columnName)!=null) {
                        this.table.getBTreeByName(columnName).delete((Double)c.getValue(), r);
                        break;
                    }
                case INT:
                    if(this.table.getBTreeByName(columnName)!=null) {
                        this.table.getBTreeByName(columnName).delete((Integer)c.getValue(), r);
                        break;

                    }
            }
        }

    }



}


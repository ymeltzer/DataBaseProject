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


    public void deleteRow() {

        if (this.condition == null) {

            for (int i = 0; i < this.dataBase.getTableByName(table.getTableName()).getTable().size(); i++) {
                this.dataBase.getTableByName(table.getTableName()).getTable().remove(i);
                i--;

            }
            // Iterator<Row> rowIterator = table.getTable().iterator();
            //Row r = rowIterator.next();
            // for(Row r : this.dataBase.getTableByName(table.getTableName()).getTable()){

            //  this.dataBase.getTableByName(table.getTableName()).getTable().remove(r);
            //rowIterator.remove();
            // }
        }
        if (this.condition != null) {
            boolean bool = false;
            for (int i = 0; i < this.dataBase.getTableByName(table.getTableName()).getTable().size(); i++) {

                WhereCondition whereCondition = new WhereCondition(this.deleteQuery.getWhereCondition(), this.table, this.dataBase.getTableByName(table.getTableName()).getTable().get(i));
                try {
                    Row row = whereCondition.implementCondition(this.dataBase.getTableByName(table.getTableName()).getTable().get(i), this.condition);

                    if (row != null) {
                        this.dataBase.getTableByName(table.getTableName()).getTable().remove(i);
                        i--;
                        bool = true;
                    }
                }
                catch(NullPointerException e){
                    continue;
                }

            }
            if (!bool) {
                throw new IllegalArgumentException("The Where clause you entered: " + this.condition.toString() + ", does not match any rows in table: " + this.table.getTableName());
            }


        }
    }
}


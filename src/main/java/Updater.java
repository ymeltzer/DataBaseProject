import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnValuePair;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.Condition;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.UpdateQuery;
import net.sf.jsqlparser.schema.Column;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

public class Updater {
    private String tableName;
    private Condition whereCondition;
    private UpdateQuery updateQuery;
    private ArrayList<ColumnValuePair> columnValuePairs;
    private Table table;


    public Updater(UpdateQuery updateQuery, Table table) {
        this.updateQuery = updateQuery;
        this.columnValuePairs = new ArrayList<ColumnValuePair>(Arrays.asList(updateQuery.getColumnValuePairs()));
        this.tableName = updateQuery.getTableName();
        this.whereCondition = updateQuery.getWhereCondition();
        this.table = table;

    }

    /**
     * this method drives the update
     * it checks the to see if there is a where condition
     * @return
     */
    public boolean doUpdate() {
        if(updateQuery.getWhereCondition() != null){
                return doUpdateWithWhereCondition();

            }

            if (updateQuery.getWhereCondition() == null){

                return doUpdateWithoutWhereCondition();

                }
                return false;
    }





    /**
     * this method takes a row which has the values to update in the proper indexes and add them to the original row from the table
     * @param originalValues
     * @param withMissingValues
     * @return
     */
    private Row addMissingRowValuesToRow(Row originalValues, Row withMissingValues){

        for(int i=0; i < withMissingValues.getTheCells().size();i++ ){

                originalValues.getTheCells().get(i).setValue(withMissingValues.getTheCells().get(i).getValue());

        }
        return originalValues;
    }

    /**
     * this method takes a table and runs the condition on each row .
     * if the row passes inspection it gets added to an arraylist
     * @param c
     * @param table
     * @return
     */

    /**
     * this method checks to see if the current value from the given column value pair matches the required data type
     * of the column it is being inserted into
     * @param s
     * @param c
     * @return
     */
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
     * this method casts the string values from the given column value pairs into the intended data type of the given column
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
     * this method is used for columns that require a unique value for each row.
     * it checks if the value is unique in the value is unique in the given column
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

    /**
     * this method returns a desired column in arraylist form
     * @param c
     * @return
     */

    public ArrayList<Cell> getColumn(ColumnDescription c){
        ArrayList<Cell> arr = new ArrayList<Cell>();
        int index = this.table.getColumnIndex(c.getColumnName());

        for(Row r : this.table.getTable()){
            arr.add(r.getCell(index));
        }
        return arr;
    }

    /**
     * this method drives the update if there is a where condition
     * @return
     */
    private boolean doUpdateWithWhereCondition(){
        for(Row r : this.table.getTable()){
            if(new WhereCondition(updateQuery.getWhereCondition(), this.table, r).implementCondition(r,updateQuery.getWhereCondition())!=null){
                for(ColumnValuePair col: this.columnValuePairs){
                    boolean bool = false;
                    for(ColumnDescription c: this.table.getColumnNames()){

                        if(c.getColumnName().compareToIgnoreCase(col.getColumnID().getColumnName())==0){
                            if(CompareValToColumnType(col.getValue(), c)){
                                if(c.isUnique() || this.table.getPrimaryKeyColumn().getColumnName().compareToIgnoreCase(c.getColumnName())==0) {//if primary key column it must also be unique
                                    if (isValueUniqueInColumn(c, col.getValue())) {

                                        r.getTheCells().get(this.table.getColumnIndex(c.getColumnName())).setValue(castValue(col.getValue(), c));
                                        bool=true;
                                        break;

                                    }
                                    if(!isValueUniqueInColumn(c,col.getValue())){
                                        throw new IllegalArgumentException("This value = " + col.getValue()+ " occurs already in unique column");
                                    }
                                }
                                r.getTheCells().get(this.table.getColumnIndex(c.getColumnName())).setValue(castValue(col.getValue(), c));
                                bool=true;
                                break;
                            }
                        }
                    }
                    if(bool){
                        continue;
                    }
                    throw new IllegalArgumentException(col.getColumnID().getColumnName()+ " is not a valid column in this table= " + this.table.getTableName());
                }
            }
        }
        return true;
    }

    /**
     * this method drives an update on an entire table if there is no where condition
     * @return
     */
    private boolean doUpdateWithoutWhereCondition(){
        for(Row r: this.table.getTable()){
            for(ColumnValuePair col: this.columnValuePairs){
                boolean bool = false;
                for(ColumnDescription c: this.table.getColumnNames()){

                    if(c.getColumnName().compareToIgnoreCase(col.getColumnID().getColumnName())==0){
                        if(CompareValToColumnType(col.getValue(), c)){
                            if(c.isUnique() || this.table.getPrimaryKeyColumn().getColumnName().compareToIgnoreCase(c.getColumnName())==0) {//if primary key column it must also be unique
                                if (isValueUniqueInColumn(c, col.getValue())) {

                                    r.getTheCells().get(this.table.getColumnIndex(c.getColumnName())).setValue(castValue(col.getValue(), c));
                                    bool=true;
                                    break;

                                }
                                if(!isValueUniqueInColumn(c,col.getValue())){
                                    throw new IllegalArgumentException("This value = " + col.getValue()+ " occurs already in unique column");
                                }
                            }
                            r.getTheCells().get(this.table.getColumnIndex(c.getColumnName())).setValue(castValue(col.getValue(), c));
                            bool=true;
                            break;
                        }
                    }
                }
                if(bool){
                    continue;
                }
                throw new IllegalArgumentException(col.getColumnID().getColumnName()+ " is not a valid column in this table= " + this.table.getTableName());
            }
        }
        return true;
    }
}









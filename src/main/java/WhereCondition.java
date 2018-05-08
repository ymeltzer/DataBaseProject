import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnID;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SQLQuery;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.Condition;

import java.util.ArrayList;
import java.util.HashSet;


public class WhereCondition{

    private ArrayList<Row> rowsMeetConditions;
    private Condition condition;
    private Table table;
    private Row row;

    public WhereCondition(Condition condition, Table table, Row row){

        this.rowsMeetConditions = new ArrayList<Row>();
        this.table = table;
        this.condition = condition;
        this.row = row;
    }





    /**this is a recursive method that recursively goes through a where clause starting from "and",
     * then breaking it into "or", and then checking individual statements of equality
     * I allowed this method to be more than 30 lines because Breaking it up would make it confusing for the reader
     * each case of the switch statement is like a new method and I believe is very readable
     * @param row
     * @param c
     * @return Row
     */
    public Row implementCondition(Row row, Condition c){

        switch(c.getOperator()){

            case AND:
                if(c.getRightOperand() instanceof Condition && c.getLeftOperand() instanceof Condition){
                   Row row1 = implementCondition(row, (Condition) c.getRightOperand());
                   Row row2 = implementCondition(row, (Condition) c.getLeftOperand());

                   if(row1!= null && row2!=null){
                       return row;
                   }
                }
                break;

            case OR:
                if(c.getRightOperand() instanceof Condition || c.getLeftOperand() instanceof Condition){
                    Row row1 = implementCondition(row, (Condition) c.getRightOperand());
                    Row row2 = implementCondition(row, (Condition) c.getLeftOperand());
                    if(row1!= null || row2!=null){
                        return row;
                    }
                }
                break;

            case NOT_EQUALS:
                ColumnID id = (ColumnID) c.getLeftOperand();//turn left operand into column id
                if(!this.table.isColumnInTable(id.getColumnName())){
                    throw new IllegalArgumentException(id.getColumnName()+ " is not a column in " + this.table.getTableName());
                }
                int index = table.getColumnIndex(id.getColumnName());
                Object rightOperand = castCellsVal( (String)c.getRightOperand(), table.getColumnNames().get(index));
                    if (row.getCell(index).compareTo(rightOperand) != 0 && row.getCell(index).getValue() != null) {
                        return row;
                    }
                break;

            case LESS_THAN:
                id = (ColumnID) c.getLeftOperand();
                index = table.getColumnIndex(id.getColumnName());
                rightOperand = castCellsVal((String) c.getRightOperand(), table.getColumnNames().get(index));
                    if (row.getCell(index).compareTo(rightOperand) < 0 && row.getCell(index).getValue() != null) {
                        return row;
                    }
                break;

            case EQUALS:
                id = (ColumnID) c.getLeftOperand();
                if(!this.table.isColumnInTable(id.getColumnName())){
                    throw new IllegalArgumentException(id.getColumnName()+ " is not a column in " + this.table.getTableName());
                }
                index = table.getColumnIndex(id.getColumnName());
                rightOperand = castCellsVal((String) c.getRightOperand(), table.getColumnNames().get(index));
                    if (row.getCell(index).compareTo(rightOperand) == 0 && row.getCell(index).getValue() != null) {
                        return row;
                    }
                break;

            case LESS_THAN_OR_EQUALS:
                id = (ColumnID) c.getLeftOperand();
                if(!this.table.isColumnInTable(id.getColumnName())){
                    throw new IllegalArgumentException(id.getColumnName()+ " is not a column in " + this.table.getTableName());
                }
                index = table.getColumnIndex(id.getColumnName());
                rightOperand = castCellsVal((String) c.getRightOperand(), table.getColumnNames().get(index));
                    if (row.getCell(index).compareTo(rightOperand) <= 0 && row.getCell(index).getValue() != null) {
                        return row;
                        }
                break;

            case GREATER_THAN:
                id = (ColumnID) c.getLeftOperand();
                if(!this.table.isColumnInTable(id.getColumnName())){
                    throw new IllegalArgumentException(id.getColumnName()+ " is not a column in " + this.table.getTableName());
                }
                index = table.getColumnIndex(id.getColumnName());
                rightOperand = castCellsVal((String) c.getRightOperand(), table.getColumnNames().get(index));
                    if (row.getCell(index).compareTo(rightOperand) > 0 && row.getCell(index).getValue() != null) {
                        return row;
                    }
                break;

            case GREATER_THAN_OR_EQUALS:

                id = (ColumnID) c.getLeftOperand();
                if(!this.table.isColumnInTable(id.getColumnName())){
                    throw new IllegalArgumentException(id.getColumnName()+ " is not a column in " + this.table.getTableName());
                }
                index = table.getColumnIndex(id.getColumnName());
                rightOperand = castCellsVal((String) c.getRightOperand(), table.getColumnNames().get(index));
                if(row.getCell(index).compareTo(rightOperand) >= 0 && row.getCell(index).getValue()!=null){
                    return row;
                }
                break;
        }
        return null;
    }

    /**
     * this is my main method for Breaking up a where clause when all the columns in the select statement are indexed
     * I add all possible rows from the Btree into a hashset to ensure no duplicates
     * I allowed this method to be more than 30 lines because I believed breaking it up would make it confusing for the reader
     * each case of the switch statement is like a new method and I hope is very readable
     * @param meetsConditionsSet
     * @param c
     * @return
     */

    public HashSet<Row> implementIndexedCondition(HashSet<Row> meetsConditionsSet, Condition c){

        switch(c.getOperator()){

            case AND:
                if(c.getRightOperand() instanceof Condition && c.getLeftOperand() instanceof Condition){
                    //left condition of AND
                    HashSet<Row> withDesiredRows1 = implementIndexedCondition(meetsConditionsSet, (Condition) c.getRightOperand());
                    //right condition of AND
                    HashSet<Row> withDesiredRows2 = implementIndexedCondition(meetsConditionsSet, (Condition) c.getLeftOperand());
                    //check for the rows that are in both containers (and)
                    for(Row r: withDesiredRows1){
                        if(withDesiredRows2.contains(r)){
                            meetsConditionsSet.add(r);
                        }
                    }
                        return meetsConditionsSet;
                }

            case OR:
                if(c.getRightOperand() instanceof Condition || c.getLeftOperand() instanceof Condition){
                    //left condition OR
                    meetsConditionsSet.addAll(implementIndexedCondition(meetsConditionsSet, (Condition) c.getRightOperand()));
                    //right condition Or
                    meetsConditionsSet.addAll(implementIndexedCondition(meetsConditionsSet, (Condition) c.getLeftOperand()));
                        return meetsConditionsSet;
                }

            case NOT_EQUALS:
                ColumnID id = (ColumnID) c.getLeftOperand();//turn left operand into column id
                if(!this.table.isColumnInTable(id.getColumnName())){
                    throw new IllegalArgumentException(id.getColumnName()+ " is not a column in " + this.table.getTableName());
                }
                int index = table.getColumnIndex(id.getColumnName());
                Object rightOperand = castCellsVal( (String)c.getRightOperand(), table.getColumnNames().get(index));
                meetsConditionsSet.addAll(this.table.getBTreeByName(id.getColumnName()).getNotEquals((Comparable) rightOperand));
                return meetsConditionsSet;

            case LESS_THAN:
                id = (ColumnID) c.getLeftOperand();
                if(!this.table.isColumnInTable(id.getColumnName())){
                    throw new IllegalArgumentException(id.getColumnName()+ " is not a column in " + this.table.getTableName());
                }
                index = table.getColumnIndex(id.getColumnName());
                rightOperand = castCellsVal((String) c.getRightOperand(), table.getColumnNames().get(index));
                meetsConditionsSet.addAll(this.table.getBTreeByName(id.getColumnName()).getLessThan((Comparable) rightOperand));
                    return meetsConditionsSet;

            case EQUALS:
                id = (ColumnID) c.getLeftOperand();
                if(!this.table.isColumnInTable(id.getColumnName())){
                    throw new IllegalArgumentException(id.getColumnName()+ " is not a column in " + this.table.getTableName());
                }
                index = table.getColumnIndex(id.getColumnName());
                rightOperand = castCellsVal((String) c.getRightOperand(), table.getColumnNames().get(index));
                meetsConditionsSet.addAll(this.table.getBTreeByName(id.getColumnName()).get((Comparable) rightOperand));
                return meetsConditionsSet;

            case LESS_THAN_OR_EQUALS:
                id = (ColumnID) c.getLeftOperand();
                if(!this.table.isColumnInTable(id.getColumnName())){
                    throw new IllegalArgumentException(id.getColumnName()+ " is not a column in " + this.table.getTableName());
                }
                index = table.getColumnIndex(id.getColumnName());
                rightOperand = castCellsVal((String) c.getRightOperand(), table.getColumnNames().get(index));
                meetsConditionsSet.addAll(this.table.getBTreeByName(id.getColumnName()).getLessThanEquals((Comparable) rightOperand));
                    return meetsConditionsSet;

            case GREATER_THAN:
                id = (ColumnID) c.getLeftOperand();
                if(!this.table.isColumnInTable(id.getColumnName())){
                    throw new IllegalArgumentException(id.getColumnName()+ " is not a column in " + this.table.getTableName());
                }
                index = table.getColumnIndex(id.getColumnName());
                rightOperand = castCellsVal((String) c.getRightOperand(), table.getColumnNames().get(index));
                meetsConditionsSet.addAll(this.table.getBTreeByName(id.getColumnName()).getGreaterThan(((Comparable) rightOperand)));
                return meetsConditionsSet;

            case GREATER_THAN_OR_EQUALS:
                id = (ColumnID) c.getLeftOperand();
                if(!this.table.isColumnInTable(id.getColumnName())){
                    throw new IllegalArgumentException(id.getColumnName()+ " is not a column in " + this.table.getTableName());
                }
                index = table.getColumnIndex(id.getColumnName());
                rightOperand = castCellsVal((String) c.getRightOperand(), table.getColumnNames().get(index));
                meetsConditionsSet.addAll(this.table.getBTreeByName(id.getColumnName()).getGreaterThanEquals(((Comparable) rightOperand)));
                return meetsConditionsSet;
        }
        return meetsConditionsSet;
    }

    /**
     * In this method I cast the values of the Where Condition to appropriate types so that they can be compared to my object-cell-values
     * @param toBeParsed
     * @param columnDescription
     * @return
     */

    private Object castCellsVal(String toBeParsed, ColumnDescription columnDescription){


            switch (columnDescription.getColumnType()) {

                case INT:
                    return Integer.parseInt(toBeParsed);

                case BOOLEAN:
                    Object returnVal = toBeParsed.toLowerCase();
                    if (returnVal.equals("true")) {
                       return true;
                    }
                    if (returnVal.equals("false")) {
                        return false;
                    } else
                        throw new IllegalArgumentException("Cell with value" + toBeParsed + " does not contain a proper value");

                case DECIMAL:
                    return Double.parseDouble(toBeParsed);

                case VARCHAR:
                   return  toBeParsed;

            }



        return null;
    }





}

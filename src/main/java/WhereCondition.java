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

    /**
     * goes through the rows in the specified table and checks the condition against them to find matches
     * @return
     */




    /**this is a recursive method that recursively goes through a where clause starting from "and",
     * then breaking it into "or", and then checking individual statements of equality
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
                   break;
                }
            case OR:
                if(c.getRightOperand() instanceof Condition || c.getLeftOperand() instanceof Condition){
                    Row row1 = implementCondition(row, (Condition) c.getRightOperand());
                    Row row2 = implementCondition(row, (Condition) c.getLeftOperand());

                    if(row1!= null || row2!=null){
                        return row;
                    }
                    break;
                }


            case NOT_EQUALS:
                ColumnID id = (ColumnID) c.getLeftOperand();//turn left operand into column id
                if(!this.table.isColumnInTable(id.getColumnName())){
                    throw new IllegalArgumentException(id.getColumnName()+ " is not a column in " + this.table.getTableName());
                }
                String leftOperandVal = c.getLeftOperand().toString();

                int index = table.getColumnIndex(id.getColumnName());
                Object rightOperand = castCellsVal( (String)c.getRightOperand(), table.getColumnNames().get(index));

                    if (row.getCell(index).compareTo(rightOperand) != 0 && row.getCell(index).getValue() != null) {
                        return row;
                    }

                break;
            case LESS_THAN:
                id = (ColumnID) c.getLeftOperand();
                leftOperandVal = c.getLeftOperand().toString();
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
                leftOperandVal = c.getLeftOperand().toString();
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
                leftOperandVal = c.getLeftOperand().toString();
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
                leftOperandVal = c.getLeftOperand().toString();
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
                leftOperandVal = c.getLeftOperand().toString();
                index = table.getColumnIndex(id.getColumnName());
                rightOperand = castCellsVal((String) c.getRightOperand(), table.getColumnNames().get(index));
                if(row.getCell(index).compareTo(rightOperand) >= 0 && row.getCell(index).getValue()!=null){
                    return row;
                }
                break;

        }
        return null;
    }

    public HashSet<Row> implementIndexedCondition(HashSet<Row> meetsCondition, Condition c){

        switch(c.getOperator()){

            case AND:
                if(c.getRightOperand() instanceof Condition && c.getLeftOperand() instanceof Condition){

                    HashSet<Row> temp1 = implementIndexedCondition(meetsCondition, (Condition) c.getRightOperand());
                    HashSet<Row> temp2 =implementIndexedCondition(meetsCondition, (Condition) c.getLeftOperand());

                    for(Row r: temp1){
                        if(temp2.contains(r)){
                            meetsCondition.add(r);
                        }
                    }

                        return meetsCondition;

                }
            case OR:
                if(c.getRightOperand() instanceof Condition || c.getLeftOperand() instanceof Condition){
                    meetsCondition.addAll(implementIndexedCondition(meetsCondition, (Condition) c.getRightOperand()));
                    meetsCondition.addAll(implementIndexedCondition(meetsCondition, (Condition) c.getLeftOperand()));


                        return meetsCondition;

                }


            case NOT_EQUALS:
                ColumnID id = (ColumnID) c.getLeftOperand();//turn left operand into column id
                if(!this.table.isColumnInTable(id.getColumnName())){
                    throw new IllegalArgumentException(id.getColumnName()+ " is not a column in " + this.table.getTableName());
                }
                String leftOperandVal = c.getLeftOperand().toString();

                int index = table.getColumnIndex(id.getColumnName());
                Object rightOperand = castCellsVal( (String)c.getRightOperand(), table.getColumnNames().get(index));


                meetsCondition.addAll(this.table.getBTreeByName(id.getColumnName()).getNotEquals((Comparable) rightOperand));
                return meetsCondition;

            case LESS_THAN:
                id = (ColumnID) c.getLeftOperand();
                if(!this.table.isColumnInTable(id.getColumnName())){
                    throw new IllegalArgumentException(id.getColumnName()+ " is not a column in " + this.table.getTableName());
                }

                index = table.getColumnIndex(id.getColumnName());
                rightOperand = castCellsVal((String) c.getRightOperand(), table.getColumnNames().get(index));

                meetsCondition.addAll(this.table.getBTreeByName(id.getColumnName()).getLessThan((Comparable) rightOperand));
                    return meetsCondition;


            case EQUALS:
                id = (ColumnID) c.getLeftOperand();
                if(!this.table.isColumnInTable(id.getColumnName())){
                    throw new IllegalArgumentException(id.getColumnName()+ " is not a column in " + this.table.getTableName());
                }

                index = table.getColumnIndex(id.getColumnName());
                rightOperand = castCellsVal((String) c.getRightOperand(), table.getColumnNames().get(index));

                meetsCondition.addAll(this.table.getBTreeByName(id.getColumnName()).get((Comparable) rightOperand));
                return meetsCondition;



            case LESS_THAN_OR_EQUALS:

                id = (ColumnID) c.getLeftOperand();
                if(!this.table.isColumnInTable(id.getColumnName())){
                    throw new IllegalArgumentException(id.getColumnName()+ " is not a column in " + this.table.getTableName());
                }

                index = table.getColumnIndex(id.getColumnName());
                rightOperand = castCellsVal((String) c.getRightOperand(), table.getColumnNames().get(index));
                meetsCondition.addAll(this.table.getBTreeByName(id.getColumnName()).getLessThanEquals((Comparable) rightOperand));


                    return meetsCondition;


            case GREATER_THAN:
                id = (ColumnID) c.getLeftOperand();
                if(!this.table.isColumnInTable(id.getColumnName())){
                    throw new IllegalArgumentException(id.getColumnName()+ " is not a column in " + this.table.getTableName());
                }

                index = table.getColumnIndex(id.getColumnName());
                rightOperand = castCellsVal((String) c.getRightOperand(), table.getColumnNames().get(index));

                meetsCondition.addAll(this.table.getBTreeByName(id.getColumnName()).getGreaterThan(((Comparable) rightOperand)));

                return meetsCondition;

            case GREATER_THAN_OR_EQUALS:

                id = (ColumnID) c.getLeftOperand();
                if(!this.table.isColumnInTable(id.getColumnName())){
                    throw new IllegalArgumentException(id.getColumnName()+ " is not a column in " + this.table.getTableName());
                }

                index = table.getColumnIndex(id.getColumnName());
                rightOperand = castCellsVal((String) c.getRightOperand(), table.getColumnNames().get(index));
                meetsCondition.addAll(this.table.getBTreeByName(id.getColumnName()).getGreaterThanEquals(((Comparable) rightOperand)));


                return meetsCondition;
        }
        return meetsCondition;
    }

    private Object castCellsVal(String toBeParsed, ColumnDescription columnDescription){

        Object returnVal = null;



            switch (columnDescription.getColumnType()) {

                case INT:
                    return returnVal = Integer.parseInt(toBeParsed);

                case BOOLEAN:
                    returnVal = toBeParsed.toLowerCase();
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
                   return returnVal = toBeParsed;

            }



        return null;
    }





}

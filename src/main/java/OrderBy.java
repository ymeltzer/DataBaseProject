import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SelectQuery;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class OrderBy {
        private Table table;
        private SelectQuery selectQuery;
        private SelectQuery.OrderBy[] orderbys;
        private ArrayList<SelectQuery.OrderBy> orderBysList;

    public OrderBy(SelectQuery selectQuery, Table t){
            this.table = t;
            this.selectQuery = selectQuery;
            this.orderbys = selectQuery.getOrderBys();
            this.orderBysList = new ArrayList<SelectQuery.OrderBy>(Arrays.asList(selectQuery.getOrderBys()));
    }

    public Table runOrderby(){

        SelectQuery.OrderBy mainOrderBY = orderBysList.get(0);

            for(ColumnDescription masterColumn : this.table.getColumnNames()){

                if(mainOrderBY.getColumnID().getColumnName().compareToIgnoreCase(masterColumn.getColumnName())==0){

                    if(mainOrderBY.isAscending()){
                       this.table = orderByFirstTime(this.table, this.table.getColumnIndex(masterColumn.getColumnName()));
                    }
                    if(mainOrderBY.isDescending()){
                        this.table = orderByFirsTimeDescending(this.table, this.table.getColumnIndex(masterColumn.getColumnName()));
                    }

                }

            }

        return this.table;
    }

    private Table orderByFirstTime(Table t, int indexToOrder){

        for(int i = 0; i < table.getTable().size()-1; i++){
            for(int j = i + 1; j < table.getTable().size(); j++){
                Cell cell1 = t.getTable().get(i).getTheCells().get(indexToOrder);
                Object valueOfCell2 = t.getTable().get(j).getTheCells().get(indexToOrder).getValue();

                    if (cell1.compareTo(valueOfCell2) > 0) {

                        this.table = swapOrder(i, j);
                    }


                    if (cell1.compareTo(valueOfCell2) == 0) {

                        this.table = recursiveOrderBY(i, j, 1);
                    }



            }
        }


        return t;
    }
    private Table orderByFirsTimeDescending(Table t, int indexToOrder){

        for(int i = 0; i < table.getTable().size(); i++){
            for(int j = i + 1; j < table.getTable().size(); j++){
                Cell cell1 = t.getTable().get(i).getTheCells().get(indexToOrder);
                Object valueOfCell2 = t.getTable().get(j).getTheCells().get(indexToOrder).getValue();

                    if (cell1.compareTo(valueOfCell2) < 0) {

                        this.table = swapOrder(i, j);
                    }


                if(cell1.compareTo(valueOfCell2) == 0){

                    this.table = recursiveOrderBY(i,j,1);
                }



            }
        }


        return t;
    }
    private Table recursiveOrderBY(int row1Index, int row2Index, int orderByIndex){
        int columnIndex = 0;
        if(this.orderBysList.size()!=orderByIndex){

            for (ColumnDescription c : this.table.getColumnNames()) {
                if (orderBysList.get(orderByIndex).getColumnID().getColumnName().compareToIgnoreCase(c.getColumnName()) == 0) {
                    columnIndex = this.table.getColumnIndex(c.getColumnName());
                }
            }
            if (orderBysList.get(orderByIndex).isAscending()) {

                if (this.table.getTable().get(row1Index).getTheCells().get(columnIndex).compareTo(this.table.getTable().get(row2Index).getTheCells().get(columnIndex).getValue()) > 0) {
                    swapOrder(row1Index, row2Index);
                }


                if (this.table.getTable().get(row1Index).getTheCells().get(columnIndex).compareTo(this.table.getTable().get(row2Index).getTheCells().get(columnIndex).getValue()) == 0) {

                    this.table = recursiveOrderBY(row1Index, row2Index, orderByIndex + 1);
                }

            }
            if (orderBysList.get(orderByIndex).isDescending()) {

                if (this.table.getTable().get(row1Index).getTheCells().get(columnIndex).compareTo(this.table.getTable().get(row2Index).getTheCells().get(columnIndex).getValue()) < 0) {
                    swapOrder(row1Index, row2Index);
                }


                if (this.table.getTable().get(row1Index).getTheCells().get(columnIndex).compareTo(this.table.getTable().get(row2Index).getTheCells().get(columnIndex).getValue()) == 0) {
                    this.table = recursiveOrderBY(row1Index, row2Index, orderByIndex + 1);
                }

            }
        }
        return this.table;


    }
    private Table swapOrder(int row1Index, int row2Index){
        Row r1 = this.table.getTable().get(row1Index);
        Row r2 = this.table.getTable().get(row2Index);
        this.table.getTable().remove( row2Index);
        this.table.getTable().remove( row1Index);
        if(this.table.getTable().size()==row1Index){
            this.table.getTable().add(r2);
        }
        else {
            this.table.getTable().add(row1Index, r2);
        }
        if(this.table.getTable().size() == row2Index){
            this.table.getTable().add(r1);
        }
        else {
            this.table.getTable().add(row2Index, r1);
        }
        return this.table;

    }



    private int getOrderByListIndex(SelectQuery.OrderBy orderBy){
        int index =0;
        for(SelectQuery.OrderBy s : orderBysList){
            if(s.getColumnID().getColumnName().equals(orderBy.getColumnID().getColumnName())){
                return index;
            }
            index++;
        }
        return -1;
    }
}

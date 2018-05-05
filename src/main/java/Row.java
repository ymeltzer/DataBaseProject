import java.util.ArrayList;


public class Row {
    private ArrayList<Cell> theCells;

    public Row(){
        this.theCells = new ArrayList<Cell>();
    }

    public ArrayList<Cell> getRow(){
        return this.theCells;
    }

    public ArrayList<Cell> getTheCells() {
        return this.theCells;
    }
    public void add(Cell c){
        this.theCells.add(c);
    }

    public Cell getCell(int index){
       return this.theCells.get(index);
    }
    public void addCell(int index,Cell cell){
            this.theCells.add(index,cell);
    }
    public Cell getCellInColumn(int columnIndex){
        return this.theCells.get(columnIndex);
    }
    public void removeCell(int index){
        this.theCells.remove(index);
    }

    public int getCellIndex(Cell cell){
        int  counter =0;
        for(Cell c: this.getTheCells()){
            if(cell.equals(c)){
                return counter;
            }
        }
        throw new IllegalArgumentException("Cell " + cell.toString() + (" is not in this row"));
    }
    public boolean contains(Cell cell){
        for(Cell c: this.getTheCells()){
            if(cell.equals(c)){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Row{" +
                "theCells=" + theCells +
                '}';
    }
}

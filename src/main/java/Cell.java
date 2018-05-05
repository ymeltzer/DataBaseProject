public class Cell implements Comparable<Object>{

    private Object value;

    public Cell(Object O) {
        this.value = O;

    }
    public Cell(){
        this.value=null;
    }

    public Object getValue() {
        return value;
    }
    public void setValue(Object O){
        this.value = O;
    }




    /**
     * this method is used in conjunction with the Where clause
     * it overides comparable class which is the only way to compare to objects;
     * first checks if they are strings then checks if they are double then checks if they
     * cell.compareTo(Object)
     * @param o
     * @return int
     */
    @Override
    public int compareTo(Object o){
try {
    if (value instanceof String) {
        String thisValue = (String) value;
        String anotherValue = (String) o;

        return (thisValue.toUpperCase().compareTo(anotherValue.toUpperCase()));

    } else if (value instanceof Boolean) {
        String thisValue = String.valueOf(value);
        String anotherValue = String.valueOf(o);

        return (thisValue.toUpperCase().compareTo(anotherValue.toUpperCase()));
    } else if (value instanceof Double) {
        Double thisValue = (Double) value;
        Double anotherValue = (Double) o;

        if (thisValue > anotherValue) {
            return 1;
        } else if (thisValue < anotherValue) {
            return -1;
        }
        if (thisValue.equals(anotherValue))
            return 0;

    } else if (value instanceof Integer) {
        Integer thisValue = (Integer) value;
        Integer anotherValue = (Integer) o;

        if (thisValue > anotherValue) {
            return 1;
        } else if (thisValue < anotherValue) {
            return -1;
        }
        return 0;


    }
}
catch(NullPointerException e){

}

        throw new IllegalArgumentException("you have inserted a non compatible type to compare- " + this.value.toString() +" or " + o.toString());

    }

    @Override
    public boolean equals(Object that){

        if (this == that){

            return true;

        }

        if (that == null){

            return false;
        }

        if (getClass() != that.getClass()){

            return false;
        }

        Cell otherCell = (Cell) that;

        return this.getValue().equals(otherCell.getValue());
    }




    @Override
    public String toString(){
        return getClass() + "Value: " + this.value;
    }


}

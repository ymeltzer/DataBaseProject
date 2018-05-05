import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;

public class ColNameType {
    private String columnName;
    private ColumnDescription.DataType dataType;
    private boolean isNotNull;
    private boolean isUnique;

    public ColNameType(String name, ColumnDescription.DataType dataType){
        this.columnName =name;
        this.dataType=dataType;
        this.isNotNull = false;
        this.isUnique =false;
    }
    public String getColumnName(){
        return this.columnName;
    }

    public ColumnDescription.DataType getDataType() {
        return dataType;
    }

    public void setDataType(ColumnDescription.DataType dataType) {
        this.dataType = dataType;
    }

    public void setColumnNameName(String name) {
        this.columnName = name;
    }

    public void setNotNull(boolean notNull) {
        isNotNull = notNull;
    }
    public void setUnique(boolean unique){
        this.isUnique = unique;
    }
}

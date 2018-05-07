import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.CreateIndexQuery;

public class Indexer {
    private Table table;
    private int columnIndex;
    private ColumnDescription c;
    private ColumnDescription.DataType type;
    private String columnName;
    private String indexName;
    public Indexer(Table table, CreateIndexQuery createIndexQuery){
        this.table = table;
        this.columnIndex = table.getColumnIndex(createIndexQuery.getColumnName());
        ColumnDescription col = table.getColumnNames().get(columnIndex);
        this.c=col;
        this.type=c.getColumnType();
        this.columnName = createIndexQuery.getColumnName();
        this.indexName = createIndexQuery.getIndexName();
    }

    /**
     * this method simply creates and fills a btree with an existing column;
     * @return
     */
    public BTree doIndex(){
        if(table.getBTreeByName(this.columnName)!=null){
            throw new IllegalArgumentException("An index for " + this.columnName+ " already exists");

        }
        switch(this.type){
            case VARCHAR:
                BTree<String,Row> bTree = new BTree<String, Row>(this.columnName);
                fillTree(bTree);

                return bTree;

            case DECIMAL:
                BTree<Double ,Row> bTree1 = new BTree<Double, Row>(this.columnName);
                fillTree(bTree1);
                return bTree1;

            case BOOLEAN:
                throw new IllegalArgumentException("Cant index a boolean column");
            case INT:
                BTree<Integer ,Row> bTree2 = new BTree<Integer, Row>(this.columnName);
                fillTree(bTree2);
                return bTree2;
        }

            throw new IllegalArgumentException("Type =" + type.toString() + " is not supported by the Database");
    }
    private void fillTree(BTree tree){

        for(Row r: table.getTable()){
            if(r.getCell(this.columnIndex).getValue()!=null) {
                tree.put((Comparable) r.getCell(this.columnIndex).getValue(), r);
            }

        }
    }

}

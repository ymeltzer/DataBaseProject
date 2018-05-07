import com.sun.org.apache.xpath.internal.SourceTree;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SelectQuery;
import net.sf.jsqlparser.JSQLParserException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/******************************************************************************
 *  Compilation:  javac BTree.java
 *  Execution:    java BTree
 *  Dependencies: StdOut.java
 *
 *  B-tree.
 *
 *  Limitations
 *  -----------
 *   -  Assumes M is even and M >= 4
 *   -  should b be an array of children or list (it would help with
 *      casting to make it a list)
 *
 ******************************************************************************/



/**
 * taken from
 * https://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/BTree.java.html
 * edited by Judah Diament and Yudi Meltzer
 *  The {@code BTree} class represents an ordered symbol table of generic
 *  key-value pairs.
 *  It supports the <em>put</em>, <em>get</em>, <em>contains</em>,
 *  <em>size</em>, and <em>is-empty</em> methods.
 *  A symbol table implements the <em>associative array</em> abstraction:
 *  when associating a value with a key that is already in the symbol table,
 *  the convention is to replace the old value with the new value.
 *  Unlike {@link java.util.Map}, this class uses the convention that
 *  values cannot be {@code null}—setting the
 *  value associated with a key to {@code null} is equivalent to deleting the key
 *  from the symbol table.
 *  <p>
 *  This implementation uses a B-tree. It requires that
 *  the key type implements the {@code Comparable} interface and calls the
 *  {@code compareTo()} and method to compare two keys. It does not call either
 *  {@code equals()} or {@code hashCode()}.
 *  The <em>get</em>, <em>put</em>, and <em>contains</em> operations
 *  each make log<sub><em>m</em></sub>(<em>n</em>) probes in the worst case,
 *  where <em>n</em> is the number of key-value pairs
 *  and <em>m</em> is the branching factor.
 *  The <em>size</em>, and <em>is-empty</em> operations take constant time.
 *  Construction takes constant time.
 *  <p>
 *  For additional documentation, see
 *  <a href="https://algs4.cs.princeton.edu/62btree">Section 6.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */





public class BTree<Key extends Comparable<Key>, Value>
{
    //max children per B-tree node = MAX-1 (must be an even number and greater than 2)
    private static final int MAX = 4;
    private Node root; //root of the B-tree
    private Node leftMostExternalNode;
    private int height; //height of the B-tree
    private int n; //number of key-value pairs in the B-tree
    private String name;



    //B-tree node data type
    private static final class Node
    {
        private int entryCount; // number of entries
        private Entry[] entries = new Entry[BTree.MAX]; // the array of children
        private Node next;
        private Node previous;

        // create a node with k entries
        private Node(int k)
        {
            this.entryCount = k;
        }

        private void setNext(Node next)
        {
            this.next = next;
        }
        private Node getNext()
        {
            return this.next;
        }
        private void setPrevious(Node previous)
        {
            this.previous = previous;
        }
        private Node getPrevious()
        {
            return this.previous;
        }

        private Entry[] getEntries()
        {
            return Arrays.copyOf(this.entries, this.entryCount);
        }
    }

    //internal nodes: only use key and child
    //external nodes: only use key and value
    public static class Entry
    {
        private Comparable key;
        private Node child;
        private ArrayList<Row> val;

        public Entry(Comparable key, Object val, Node child)
        {
            if(val instanceof ArrayList){
                this.val = (ArrayList<Row>) val;
            }
            else{
                this.val = new ArrayList<>();
                this.val.add((Row)val);
            }
            this.key = key;
            this.child = child;
        }
        public Object getValue()
        {
            return this.val;
        }
        public Comparable getKey()
        {
            return this.key;
        }


    }

    /**
     * Initializes an empty B-tree.
     */
    public BTree(String name)
    {
        this.root = new Node(0);
        this.leftMostExternalNode = this.root;
        this.name = name;
    }


    /**
     * Returns true if this symbol table is empty.
     *
     * @return {@code true} if this symbol table is empty; {@code false}
     *         otherwise
     */
    public boolean isEmpty()
    {
        return this.size() == 0;
    }
    public String getName(){
        return this.name;
    }

    /**
     * @return the number of key-value pairs in this symbol table
     */
    public int size()
    {
        return this.n;
    }

    /**
     * @return the height of this B-tree
     */
    public int height()
    {
        return this.height;
    }

    /**
     * returns a list of all the entries in the Btree, ordered by key
     * @return
     */
    public ArrayList<Entry> getOrderedEntries()
    {
        Node current = this.leftMostExternalNode;
        ArrayList<Entry> entries = new ArrayList<Entry>();
        while(current != null)
        {
            for(Entry e : current.getEntries())
            {
                if(e.val != null)
                {
                    entries.add(e);
                }
            }
            current = current.getNext();
        }
        return entries;
    }

    public Entry getMinEntry()
    {
        Node current = this.leftMostExternalNode;
        while(current != null)
        {
            for(Entry e : current.getEntries())
            {
                if(e.val != null)
                {
                    return e;
                }
            }
        }
        return null;
    }

    public Entry getMaxEntry()
    {
        ArrayList<Entry> entries = this.getOrderedEntries();
        return entries.get(entries.size()-1);
    }

    /**
     * Returns the value associated with the given key.
     *
     * @param key the key
     * @return the value associated with the given key if the key is in the
     *         symbol table and {@code null} if the key is not in the symbol
     *         table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public ArrayList<Row> get(Key key)
    {
        if (key == null)
        {
            throw new IllegalArgumentException("argument to get() is null");
        }
        Entry entry = this.get(this.root, key, this.height);
        if(entry != null)
        {
            return entry.val;
        }
        return null;
    }

    private Entry get(Node currentNode, Key key, int height)
    {

        Entry[] entries = currentNode.entries;

        //current node is external (i.e. height == 0)
        if (height == 0)
        {
            for (int j = 0; j < currentNode.entryCount; j++)
            {
                if(isEqual(key, entries[j].key))
                {
                    //found desired key. Return its value
                    return entries[j];
                }
            }
            //didn't find the key
            return null;
        }

        //current node is internal (height > 0)
        else
        {
            for (int j = 0; j < currentNode.entryCount; j++)
            {
                //if (we are at the last key in this node OR the key we
                //are looking for is less than the next key, i.e. the
                //desired key must be in the subtree below the current entry),
                //then recurse into the current entry’s child
                if (j + 1 == currentNode.entryCount || less(key ,entries[j + 1].key))
                {
                    return this.get(entries[j].child, key, height - 1);
                }
            }
            //didn't find the key
            return null;
        }
    }

    /**
     * returns all values not equals to the key
     * @param key
     * @return
     */

    public HashSet<Row> getNotEquals(Key key)
    {
        HashSet rows= new HashSet<Row>();
        if (key == null)
        {
            throw new IllegalArgumentException("argument to get() is null");
        }

        ArrayList<Entry> entries = getOrderedEntries();
        for(Entry e : entries){
            if(!isEqual(key, e.key)){
                rows.addAll(e.val);
            }
        }
        return rows;
    }

    /**
     * getting all keys less than or equal to given key
     * @param key
     * @return
     */
    public HashSet<Row> getLessThanEquals(Key key)
    {
        HashSet rows= new HashSet<Row>();
        if (key == null)
        {
            throw new IllegalArgumentException("argument to get() is null");
        }

        ArrayList<Entry> entries = getOrderedEntries();
        for(Entry e : entries){
            if(isEqual(key, e.key)||greater(key,e.key)){
                rows.addAll(e.val);
            }
        }
        return rows;
    }
    public HashSet<Row> getGreaterThanEquals(Key key)
    {
        HashSet rows= new HashSet<Row>();
        if (key == null)
        {
            throw new IllegalArgumentException("argument to get() is null");
        }

        ArrayList<Entry> entries = getOrderedEntries();
        for(Entry e : entries){
            if(isEqual(key, e.key)||less(key,e.key)){
                rows.addAll(e.val);
            }
        }
        return rows;
    }
    /**
     * I first get the ordered btree and then check for keys greater than our key
     * @param key
     * @return
     */
    public HashSet<Row> getGreaterThan(Key key)
    {
        HashSet rows= new HashSet<Row>();
        if (key == null)
        {
            throw new IllegalArgumentException("argument to get() is null");
        }

        ArrayList<Entry> entries = getOrderedEntries();
        for(Entry e : entries){
            if(less(key, e.key)){
                rows.addAll(e.val);
            }
        }
        return rows;
    }

    public HashSet<Row> getLessThan(Key key) {
        HashSet rows = new HashSet<Row>();
        if (key == null)
        {
            throw new IllegalArgumentException("argument to get() is null");
        }

        ArrayList<Entry> entries = getOrderedEntries();
        for(Entry e : entries){
            if(greater(key, e.key)){
                rows.addAll(e.val);
            }
        }
        return rows;
    }

/**
 //I tried to traverse the btree through recursion but was having trouble so I used the getOrderedTree method instead
    private HashSet<Row> getGreaterThan(Node currentNode, Key key, int height, HashSet<Row> rows)
    {

        Entry[] entries = currentNode.entries;

        //current node is external (i.e. height == 0)
        if (height == 0)
        {
            for (int j = 0; j < currentNode.entryCount; j++)
            {
                if(less(key, entries[j].key))
                {
                    //found desired key. Return its value
                    rows.addAll(entries[j].val);
                }
            }
            //didn't find the key
            return rows;
        }

        //current node is internal (height > 0)
        else
        {
            for (int j = 0; j < currentNode.entryCount; j++)
            {
                //if (we are at the last key in this node OR the key we
                //are looking for is less than the next key, i.e. the
                //desired key must be in the subtree below the current entry),
                //then recurse into the current entry’s child


                if (j + 1 == currentNode.entryCount|| less(key, entries[j+1].key))
                {
                    return getGreaterThan(entries[j].child, key, height - 1,rows);
                }


            }

            return rows;
        }
    }
 */


    /**
     *
     * @param key
     */
    public void delete(Key key, Row value)
    {
        try {
            ArrayList<Row> arr = get(key);

            if (arr.contains(value)) {
                arr.remove(value);
            }
        }
        catch(Exception e){
            //doesn't matter if a null value is being deleted
            }
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old
     * value with the new value if the key is already in the symbol table. If
     * the value is {@code null}, this effectively deletes the key from the
     * symbol table.
     *
     * @param key the key
     * @param val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(Key key, Value val)
    {
        if (key == null)
        {
            throw new IllegalArgumentException("argument key to put() is null");
        }
        //if the key already exists in the b-tree, simply replace the value
        Entry alreadyThere = this.get(this.root, key, this.height);
        if(alreadyThere != null)
        {

            alreadyThere.val.add((Row)val);
            return;
        }

        Node newNode = this.put(this.root, key, val, this.height);
        this.n++;
        if (newNode == null)
        {
            return;
        }

        //split the root:
        //Create a new node to be the root.
        //Set the old root to be new root's first entry.
        //Set the node returned from the call to put to be new root's second entry
        Node newRoot = new Node(2);
        newRoot.entries[0] = new Entry(this.root.entries[0].key, null, this.root);
        newRoot.entries[1] = new Entry(newNode.entries[0].key, null, newNode);
        this.root = newRoot;
        //a split at the root always increases the tree height by 1
        this.height++;
    }

    /**
     *
     * @param currentNode
     * @param key
     * @param val
     * @param height
     * @return null if no new node was created (i.e. just added a new Entry into an existing node). If a new node was created due to the need to split, returns the new node
     */
    private Node put(Node currentNode, Key key, Value val, int height)
    {
        int j;
        Entry newEntry = new Entry(key, val, null);

        //external node
        if (height == 0)
        {
            //find index in currentNode’s entry[] to insert new entry
            for (j = 0; j < currentNode.entryCount; j++)
            {
                if (less(key, currentNode.entries[j].key))
                {
                    break;
                }
            }
        }

        // internal node
        else
        {
            //find index in node entry array to insert the new entry
            for (j = 0; j < currentNode.entryCount; j++)
            {
                //if (we are at the last key in this node OR the key we
                //are looking for is less than the next key, i.e. the
                //desired key must be added to the subtree below the current entry),
                //then do a recursive call to put on the current entry’s child
                if ((j + 1 == currentNode.entryCount) || less(key, currentNode.entries[j + 1].key))
                {
                    //increment j (j++) after the call so that a new entry created by a split
                    //will be inserted in the next slot
                    Node newNode = this.put(currentNode.entries[j++].child, key, val, height - 1);
                    if (newNode == null)
                    {
                        return null;
                    }
                    //if the call to put returned a node, it means I need to add a new entry to
                    //the current node
                    newEntry.key = newNode.entries[0].key;
                    newEntry.val = null;
                    newEntry.child = newNode;
                    break;
                }
            }
        }
        //shift entries over one place to make room for new entry
        for (int i = currentNode.entryCount; i > j; i--)
        {
            currentNode.entries[i] = currentNode.entries[i - 1];
        }
        //add new entry
        currentNode.entries[j] = newEntry;
        currentNode.entryCount++;
        if (currentNode.entryCount < BTree.MAX)
        {
            //no structural changes needed in the tree
            //so just return null
            return null;
        }
        else
        {
            //will have to create new entry in the parent due
            //to the split, so return the new node, which is
            //the node for which the new entry will be created
            return this.split(currentNode, height);
        }
    }

    /**
     * split node in half
     * @param currentNode
     * @return new node
     */
    private Node split(Node currentNode, int height)
    {
        Node newNode = new Node(BTree.MAX / 2);
        //by changing currentNode.entryCount, we will treat any value
        //at index higher than the new currentNode.entryCount as if
        //it doesn't exist
        currentNode.entryCount = BTree.MAX / 2;
        //copy top half of h into t
        for (int j = 0; j < BTree.MAX / 2; j++)
        {
            newNode.entries[j] = currentNode.entries[BTree.MAX / 2 + j];
        }
        //external node
        if (height == 0)
        {
            newNode.setNext(currentNode.getNext());
            newNode.setPrevious(currentNode);
            currentNode.setNext(newNode);
        }
        return newNode;
    }

    // comparison functions - make Comparable instead of Key to avoid casts
    private static boolean less(Comparable k1, Comparable k2)
    {
        if(k1==null&&k2==null){
            return false;
        }
        if(k1==null&& k2!=null){
            return true;
        }
        if(k1!=null&& k2==null){
            return false;
        }

        if(k1 instanceof String){
            return  ((String) k1).compareToIgnoreCase((String) k2)< 0;
        }
        if(k1 instanceof Integer){
            return  ((Integer) k1).compareTo((Integer) k2)< 0;
        }
        if(k1 instanceof Double){
            return  ((Double) k1).compareTo((Double) k2)< 0;
        }

        return (k1.compareTo(k2) < 0);
    }

    private static boolean isEqual(Comparable k1, Comparable k2)
    {

        if(k1==null&&k2==null){
            return true;
        }
        if(k1==null&& k2!=null){
            return false;
        }
        if(k1!=null&& k2==null){
            return false;
        }
        if(k1 instanceof String){

            return ((String) k1).compareToIgnoreCase((String) k2)==0;
        }
        if(k1 instanceof Integer){
            return  ((Integer) k1).compareTo((Integer) k2)== 0;
        }
        if(k1 instanceof Double){
            return  ((Double) k1).compareTo((Double) k2)==0;
        }
        return (k1.compareTo(k2) == 0);
    }
    private static boolean greater(Comparable k1, Comparable k2)
    {

        if(k1==null&&k2==null){
            return false;
        }
        if(k1==null&& k2!=null){
            return false;
        }
        if(k1!=null&& k2==null){
            return true;
        }
        if(k1 instanceof String){
            return ((String) k1).compareToIgnoreCase((String) k2)>0;
        }
        if(k1 instanceof Integer){
            return  ((Integer) k1).compareTo((Integer) k2)> 0;
        }
        if(k1 instanceof Double){
            return  ((Double) k1).compareTo((Double) k2)> 0;
        }
        return (k1.compareTo(k2)>0);
    }

    public static void main(String[] args) throws JSQLParserException {

        DataBase dataBase = new DataBase();
        String query = "CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " SSNum int UNIQUE,"
                + " Class varchar(255),"
                + " FirstName varchar(255),"
                + " LastName varchar(255) NOT NULL,"
                + " GPA decimal(1,2) DEFAULT 0.00,"
                + " CurrentStudent boolean DEFAULT true,"
                + " PRIMARY KEY (BannerID)"
                + ");";
        dataBase.execute(query);
        dataBase.execute("INSERT INTO ycstudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Yudi','Meltzer',4.0,800092345,'Freshman');");
        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, Class) VALUES ('Yosef','Epstein',3.42,800002345,'Freshman');");
        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Yudi','Math',2.0,800012745);");
        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Gav','Sturm',3.5,800012845);");
        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Leah','Dent',1.0,800012945);");
        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Dad','Sir',1.3,800012245);");
        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Fraydy','Caroline',2.9,80002222);");
        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Leah','Katz',3.5,80001111);");
        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Dad','Diament',3.5,800012098);");
        dataBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Fraydy','leff',2.1,800000000);");
        dataBase.execute("CREATE INDEX LastName_Index on YCStudent (GPA);");
       // int index = dataBase.getTableByName("YCStudent").getColumnIndex("LastName");
        /*
        dataBase.execute("CREATE INDEX LastName_Index on YCStudent (BannerID);");
        //BTree<String, Row> st = new BTree<String, Row>("LastName");
       // for(Row r: dataBase.getTableByName("YCStudent").getTable()){
         //   st.put((String)r.getCell(index).getValue(),r);
        //}
        */
       BTree<String, Row> st = dataBase.getTableByName("YCStudent").getBTreeByName("GPA");

       ArrayList<Entry> entries = dataBase.getTableByName("YCStudent").getBTreeByName("GPA").getOrderedEntries();
        System.out.println("Key-value pairs, sorted by key:");
       // entries = st.getOrderedEntries();
        for(Entry e : entries)
        {
            System.out.println("key = " + e.getKey() + "; value = " + e.getValue());
        }
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();


       ResultSet rs = dataBase.execute("Select * FROM YCStudent WHERE GPA=3.5;");
      rs.printResultSetSelect();
       System.out.println();
      //  dataBase.printDataBase();
      //  st.delete("'caroline'",dataBase.getTableByName("YCStudent").getTable().get(6));
        System.out.println("Key-value pairs, sorted by key:");
       ArrayList<Entry> entries1 = st.getOrderedEntries();
        for(Entry e :  entries1)
        {
            System.out.println("key = " + e.getKey() + "; value = " + e.getValue());
        }


    }
}



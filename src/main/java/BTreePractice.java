import java.util.ArrayList;


    public class BTreePractice{
        private Node root;       // root of the B-tree
        private int height;      // height of the B-tree
        private int n;           // number of key-value pairs in the B-tree
        private String name;
        private static final int M = 4;

        public static void main(String[] args){
            DataBase dataBase = new DataBase();

        }

        public BTreePractice(String name) {

            this.height = 0;
            this.name = name;
            this.n=0;
            this.root =new Node();
        }





        private static class Node{
            private Entry[] entries;
            private int entryCount;

            public Node() {
                this.entries = new Entry[M];
                this.entryCount=0;
            }
            public Node(int m) {
                this.entries = new Entry[m];
                this.entryCount=0;
            }


        }
        private static class Entry implements Comparable<Object>{
            private Cell key;
            private ArrayList<Row> value;
            private Node child;

            public Entry(Cell key, Row value, Node child){
                this.key =key;
                this.value = new ArrayList<Row>();
                this.value.add(value);
                this.child = child;
            }


            @Override
            public int compareTo(Object o) {
                try {
                    if (key.getValue() instanceof String) {
                        String thisValue = (String) key.getValue();
                        String anotherValue = (String) o;

                        return (thisValue.toUpperCase().compareTo(anotherValue.toUpperCase()));

                    } else if (key.getValue() instanceof Boolean) {
                        String thisValue = String.valueOf(key);
                        String anotherValue = String.valueOf(o);

                        return (thisValue.toUpperCase().compareTo(anotherValue.toUpperCase()));
                    } else if (key.getValue() instanceof Double) {
                        Double thisValue = (Double) key.getValue();
                        Double anotherValue = (Double) o;

                        if (thisValue > anotherValue) {
                            return 1;
                        } else if (thisValue < anotherValue) {
                            return -1;
                        }
                        if (thisValue.equals(anotherValue))
                            return 0;

                    } else if (key.getValue() instanceof Integer) {
                        Integer thisValue = (Integer) key.getValue();
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
                    return 0;
                }
        }
        public String getName(){
            return this.name;
        }
        private Node put(Node currentNode, Cell key, Row val, int height){
            int j;
            Entry newEntry = new Entry(key, val, null);
            //external node
            if(this.height==0){
                for(j=0; j < currentNode.entryCount; j++){
                    System.out.println(key.getValue().toString());
                    System.out.println("comparing to");
                    System.out.println(currentNode.entries[j].key.getValue().toString());
                    System.out.println();
                    if(key.compareTo(currentNode.entries[j].key.getValue())< 0){
                        break;
                    }

                }
            }
            else{
                //find index in node entry array to insert the new entry
                for(j=0; j< currentNode.entryCount; j++){
                    //if (we are at the last key in this node OR the key we
                    //are looking for is less than the next key, i.e. the
                    //desired key must be added to the subtree below the current entry),
                    //then do a recursive call to put on the current entry’s child
                    if((j+1==currentNode.entryCount)|| key.compareTo(currentNode.entries[j+1].key)<0){
                        //increment j (j++) after the call so that a new entry created by a split
                        //will be inserted in the next slot
                        Node newNode = this.put(currentNode.entries[j++].child, key, val, height-1);
                        if(newNode == null){
                            return null;
                        }
                        //if the call to put returned a node, it means I need to add a new entry to
                        //the current node
                        newEntry.key= newNode.entries[0].key;
                        newEntry.child = newNode;
                        break;

                    }
                }
            }
            //shift entries over one place to make room for new entry
            for(int i = currentNode.entryCount; i<j; i--){
                currentNode.entries[i] = currentNode.entries[i-1];
            }
            //add new entry
            currentNode.entries[j] = newEntry;
            currentNode.entryCount++;
            if(currentNode.entryCount < M){
                //no structural changes needed in the tree
                //so just return null
                return null;
            }
            else{
                //will have to create new entry in the parent due
                //to the split, so return the new node, which is
                //the node for which the new entry will be created
                return this.split(currentNode);
            }
        }
        private Node split(Node currentNode){
            Node newNode = new Node(M/2);
            //by changing currentNode.entryCount, we will treat any value
            //at index higher than the new currentNode.entryCount as if
            //it doesn't exist
            currentNode.entryCount = M/2;
            //copy half currentNode into the newNode
            for(int j =0; j < M/2; j++){
                newNode.entries[j] =currentNode.entries[M/2+j];
            }
            return newNode;

        }
        public void put(Cell key, Row val){
            Node newNode = this.put(this.root, key, val, this.height);
            this.n++;
            if(newNode == null){
                return;
            }
            //split the root
            //create a new node to be the root
            //set the node returned from the call to put to be new root's second entry
            Node newRoot = new Node(2);
            newRoot.entries[0]= new Entry(this.root.entries[0].key,null,this.root);
            newRoot.entries[1] = new Entry(newNode.entries[0].key, null, newNode);
            this.root = newRoot;
            //a split at the root always increases the tree height by 1
            this.height++;
        }

        public ArrayList<Row> get(Cell key){
            return this.get(this.root, key, this.height);
        }
        private ArrayList<Row> get(Node currentNode, Cell key, int height){
            Entry[] entries = currentNode.entries;
            //current node is external (i.e. height == 0)
            if(height==0){
                for(int j=0; j<currentNode.entryCount;j++){
                    if(key.compareTo(entries[j].key.getValue())==0){
                        //found desired key
                        return entries[j].value;
                    }
                }
                //if didn't find the key
                return null;
            }
            //current node is internal
            else{
                for(int j = 0; j<currentNode.entryCount; j++){
                    //if (we are at the last key in this node OR the key we
                    //are looking for is less than the next key, i.e. the
                    //desired key must be in the subtree below the current entry),
                    //then recurse into the current entry’s child
                    if( j+1 ==currentNode.entryCount || key.compareTo(entries[j+1].key.getValue())<0){
                        return  this.get(entries[j].child, key, height-1);
                    }
                }
            }

            return null;
        }

        public ArrayList<Row> getGreaterThan(Cell key){
            ArrayList<Row> results = new ArrayList<Row>();
            return this.get(this.root, key, this.height);
        }
        private ArrayList<Row> getGreaterThan(Node currentNode, Cell key, int height, ArrayList<Row> results){

            Entry[] entries = currentNode.entries;
            //current node is external (i.e. height == 0)
            if(height==0){
                for(int j=0; j<currentNode.entryCount;j++){
                    if(key.compareTo(entries[j].key.getValue())< 0){
                        //found desired key
                        results.addAll(entries[j].value);
                        return getGreaterThan(currentNode.entries[j].child, key, height -1, results);
                    }

                }

                return results;
            }
            //current node is internal
            else{
                for(int j = 0; j<currentNode.entryCount; j++){

                    if( j+1 ==currentNode.entryCount || key.compareTo(entries[j+1].key.getValue())>=0){
                        return this.getGreaterThan(entries[j].child, key, height-1, results);
                    }
                }
            }

            return results;
        }

        public ArrayList<Row> getLessThan(Cell key){
            ArrayList<Row> results = new ArrayList<Row>();
            return this.get(this.root, key, this.height);
        }
        private ArrayList<Row> getLessThan(Node currentNode, Cell key, int height, ArrayList<Row> results){

            Entry[] entries = currentNode.entries;
            //current node is external (i.e. height == 0)
            if(height==0){
                for(int j=0; j<currentNode.entryCount;j++){
                    if(key.compareTo(entries[j].key.getValue())>0){
                        //found desired key
                        results.addAll(entries[j].value);
                        return getLessThan(currentNode.entries[j].child, key, height -1, results);
                    }

                }

                return results;
            }
            //current node is internal
            else{
                for(int j = 0; j<currentNode.entryCount; j++){

                    if( j+1 == currentNode.entryCount || key.compareTo(entries[j+1].key.getValue())<=0){
                        return this.getLessThan(entries[j].child, key, height-1, results);
                    }
                }
            }

            return results;
        }




    }



















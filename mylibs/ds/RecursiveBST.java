package mylibs.ds;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdIn;

public class RecursiveBST<Key extends Comparable<Key>, Value>
{
    private class Node
    {
        Key key;
        Value value;
        Node left;
        Node right;
        int N;
    }

    private class KeysIterable implements Iterable<Key>
    {
        // TODO: implement

        private class KeysIterator implements Iterator<Key>
        {
            public KeysIterator() {}
            public boolean hasNext() {return false;}
            public Key next() {return null;}
        }

        public KeysIterable() {}
        public KeysIterable(Key lo, Key hi) {}
        public KeysIterator iterator() {return new KeysIterator();}
    }

    private Node root;

    public RecursiveBST() {root = null;}

    // Searches for key and replaces its value. If not found, inserts key and value.
    public void put(Key key, Value value) {root = put(root, key, value);}

    // Private recursive function to insert a key-value pair.
    private Node put(Node node, Key key, Value value)
    {
        if(node == null)
        {
            Node ret = new Node();
            ret.key = key;
            ret.value = value;
            ret.N = 1;
            return ret;
        }
        else if(key.compareTo(node.key) < 0)
        {
            Node ret = put(node.left, key, value);
            node.left = ret;
            updateNodeN(node);
            return node;
        }
        else if(key.compareTo(node.key) > 0)
        {
            Node ret = put(node.right, key, value);
            node.right = ret;
            updateNodeN(node);
            return node;
        }
        else
        {
            node.key = key;
            node.value = value;
            return node;
        }
    }

    private void updateNodeN(Node node)
    {
        if(node == null) return;
        int ret = 0;
        if(node.left != null) ret += node.left.N;
        if(node.right != null) ret += node.right.N;
        node.N = ret + 1;
    }

    // Return the value associated with key.
    public Value get(Key key)
    {
        Node node = get(root, key);
        if(node == null) return null;
        else return node.value;
    }

    // Return the Node associated with key within the binary search tree rooted
    // at node.
    private Node get(Node node, Key key)
    {
        if(node == null) return null;
        if(key.compareTo(node.key) < 0) return get(node.left, key);
        else if(key.compareTo(node.key) > 0) return get(node.right, key);
        else return node;
    }

    // Remove the node that is associated with key.
    public void delete(Key key) {root = delete(root, key);}

    // Delete the node associated with key within the binary search tree rooted
    // at node.
    private Node delete(Node node, Key key)
    {
        if(node == null) return null;
        else if(key.compareTo(node.key) < 0)
        {
            Node ret = delete(node.left, key);
            node.left = ret;
            updateNodeN(node);
            return node;
        }
        else if(key.compareTo(node.key) > 0)
        {
            Node ret = delete(node.right, key);
            node.right = ret;
            updateNodeN(node);
            return node;
        }
        else
        {
            // Node with key found. Store it in a temporary variable.
            // node is the Node to delete.
            // Find the ceiling of this node within its binary tree.
            Node successor = ceiling(node);

            if(successor != null)
            {
                // The successor should not have a node on its left.
                // If it did, it would not be the ceiling of node.
                // Set the successor's left to be the node left of node to delete.
                successor.right = deleteMin(node.right);
                successor.left = node.left;
                node.left = null;
                node.right = null;
            }
            else
            {
                successor = node.left;
            }

            // Update the counts of successor.
            updateNodeN(successor);

            // Return the successor so that it can be properly linked to the
            // node above node to delete.
            return successor;
        }
    }

    // Checks if the binary search tree contains a node associated with key.
    public boolean contains(Key key)
    {
        if(contains(root, key) != null) return true;
        else return false;
    }

    // Returns the Node associated with key that is within the binary search
    // tree rooted at node.
    private Node contains(Node node, Key key)
    {
        if(node == null) return null;
        else if(key.compareTo(node.key) < 0) return contains(node.left, key);
        else if(key.compareTo(node.key) > 0) return contains(node.right, key);
        else return node;
    }

    // Check if the binary search tree is null.
    public boolean isEmpty() {return root == null;}

    // Return the total number of nodes.
    public int size()
    {
        if(root == null) return 0;
        else return root.N;
    }

    // Return the smallest Key.
    public Key min()
    {
        Node ret = min(root);
        if(ret == null) return null;
        else return ret.key;
    }

    // Return the node containing the smallest key in the binary tree rooted at
    // node.
    private Node min(Node node)
    {
        if(node == null) return null;
        if(node.left != null) return min(node.left);
        else return node;
    }

    // Return the largest key.
    public Key max()
    {
        Node ret = max(root);
        if(ret == null) return null;
        else return ret.key;
    }

    // Return the Node that is associated with the largest key within the binary
    // search tree rooted at node.
    public Node max(Node node)
    {
        if(node == null) return null;
        if(node.right != null) return max(node.right);
        else return node;
    }

    // Return the largest key that is less than key.
    public Key floor(Key key)
    {
        Node node = floor(root, key);
        if(node == null) return null;
        else return node.key;
    }

    // Return the node associated with the largest key that is less than node.key
    // within the binary search tree rooted at node.
    private Node floor(Node node, Key key)
    {
        if(node == null) return null;
        if(key.compareTo(node.key) < 0)
        {
            Node ret = floor(node.left, key);
            if(ret == null) return null;
            else return ret;
        }
        else if(key.compareTo(node.key) > 0)
        {
            Node ret = floor(node.right, key);
            if(ret == null) return node;
            else return ret;
        }
        else
        {
            Node ret = floor(node.left, key);
            if(ret == null) return null;
            else return ret;
        }
    }

    // Return the smallest key that is greater than key.
    public Key ceiling(Key key)
    {
        // Find the node that is associated with key.
        if(key == null) return key;
        Node node = contains(root, key);
        Node ret = ceiling(node);
        if(ret == null) return null;
        else return ret.key;
    }

    // Return the node containing the smallest key greater than node.key within
    // the binary search tree rooted at node.
    private Node ceiling(Node node)
    {
        if(node == null) return null;
        else if(node.right == null) return null;
        else return min(node.right);
    }

    // Return the number of keys that are less than key.
    // Keys have to be unique.
    public int rank(Key key) {return rank(root, key);}

    // Returns the number of keys less than key within the binary search tree
    // at node.
    private int rank(Node node, Key key)
    {
        if(node == null) return 0;

        // key < current node's key.
        if(key.compareTo(node.key) < 0)
            // Go down the left subtree until the node's key is greater than key.
            return rank(node.left, key);
        // key > current node's key.
        else if(key.compareTo(node.key) > 0)
        {
            int ret;
            // No keys less than current node's key.
            if(node.left == null) ret = 0;

            // Count the number of keys less than current node's key.
            else ret = node.left.N + 1;

            // Then attempt to go down the right subtree to count all keys less
            // than key.

            // No keys larger than current node's key.
            if(node.right == null) return ret;

            // Count all keys less than key in the right subtree, add it to the
            // number of keys in the left subtree and current node, then return.
            else return ret + rank(node.right, key);
        }
        else
        {
            // Curren't node's key is equal to key. Return the number of keys in
            // the left subtree if any.
            if(node.left == null) return 0;
            else return node.left.N;
        }
    }

    // Return the key that has k keys less than it.
    public Key select(int k)
    {
        Node node = select(root, k);
        if(node == null) return null;
        else return node.key;
    }

    // Return the Node that has k keys less than it within the binary search
    // rooted at node.
    private Node select(Node node, int k)
    {
        if(k < 0) return null;
        else if(node == null) return null;
        int cntleft = 0, cntright = 0;
        if(node.left != null) cntleft = node.left.N;
        if(node.right != null) cntright = node.right.N;
        if(node.left != null)
        {
            if(node.left.N > k)
            {
                return select(node.left, k);
            }
            else if(node.left.N < k)
            {
                return select(node.right, k - node.left.N - 1);
            }
            else
            {
                return node;
            }
        }
        else if(k == 0) return node;
        else if(node.right != null)
        {
            if(node.right.N > k)
            {
                return select(node.right, k - 1);
            }
            else return null;
        }
        else return null;
    }

    // Remove the node with the smallest key.
    public void deleteMin() {root = deleteMin(root);}

    // Remove the node with the smallest key within the subtree rooted at node.
    private Node deleteMin(Node node)
    {
        if(node == null) return null;
        Node ret = deleteMin(node.left);
        if(ret == null)
        {
            // Current node's left link is null. Current node is the minimum.
            return node.right;
        }
        else
        {
            // ret is the node at node to delete's right link.
            // All keys in the ret subtree are less than node.key.
            node.left = ret;
            updateNodeN(node);
            return node;
        }
    }

    // Remove the pair with the greatest key.
    public void deleteMax() {root = deleteMax(root);}

    // Remove the node with the largest key within the subtree rooted at node.
    private Node deleteMax(Node node)
    {
        if(node == null)  return null;
        Node ret = deleteMax(node.right);
        if(ret == null)
        {
            return node.left;
        }
        else
        {
            node.right = ret;
            updateNodeN(node);
            return node;
        }
    }

    public int size(Key from, Key to)
    {
        // TODO: implement
        return 0;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        if(root != null)
        {
            toString(root, sb);
            if(sb.length() > 0)
                sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }

    private void toString(Node node, StringBuilder sb)
    {
        if(node.left != null) toString(node.left, sb);
        sb.append("(" + node.key + ", " + node.value + "), ");
        if(node.right != null) toString(node.right, sb);
    }

    public Iterable<Key> keys() {return new KeysIterable();}
    public Iterable<Key> keys(Key key1, Key key2) {return new KeysIterable(key1,key2);}

    public static void main(String[] args)
    {
        boolean test = false;
        if(args.length > 0)
        {
            for(String arg : args) {if(arg.equals("-test")) test = true;}
        }

        if(test)
        {
            RecursiveBST<String, Integer> st =
                new RecursiveBST <String, Integer>();
            System.out.println("Testing all operations on empty symbol table:");
            System.out.println("    Contents: " + st.toString());
            System.out.println("    isEmpty(): " + st.isEmpty());
            System.out.println("    size(): " + st.size());
            System.out.println("    size(C, G): " + st.size("C", "G"));
            System.out.println("    contains(E): " + st.contains("E"));
            System.out.println("    get(E): " + st.get("E"));
            System.out.println("    min(): " + st.min());
            System.out.println("    max(): " + st.max());
            System.out.println("    floor(E)(): " + st.floor("E"));
            System.out.println("    ceiling(E)(): " + st.ceiling("E"));
            System.out.println("    rank(E): " + st.rank("E"));
            System.out.println("    select(5): " + st.select(5));
            st.delete("E"); System.out.println("    delete(E): " + st.toString());
            st.deleteMin(); System.out.println("    deleteMin(): " + st.toString());
            st.deleteMax(); System.out.println("    deleteMax(): " + st.toString());
            System.out.println("    keys():");
            for(String str : st.keys()) System.out.println("        " + str);
            System.out.println("    keys(C, P):");
            for(String str : st.keys("C", "P")) System.out.println("        " + str);
            System.out.println("    keys(D, P):");
            for(String str : st.keys("D", "P")) System.out.println("        " + str);
            System.out.println("    keys(C, Q):");
            for(String str : st.keys("C", "Q")) System.out.println("        " + str);
            System.out.println("    keys(D, Q):");
            for(String str : st.keys("D", "Q")) System.out.println("        " +
            str);
            System.out.println("");

            System.out.println("Testing all operations with 1 element:");
            st.put("G", 3); System.out.println("    put(G, 3): " + st.toString());
            System.out.println("    isEmpty(): " + st.isEmpty());
            System.out.println("    size(): " + st.size());
            System.out.println("    size(B, G): " + st.size("B", "G"));
            System.out.println("    size(B, X): " + st.size("B", "X"));
            System.out.println("    size(C, D): " + st.size("C", "D"));
            System.out.println("    size(W, Z): " + st.size("W", "Z"));
            System.out.println("    contains(G): " + st.contains("G"));
            System.out.println("    contains(W): " + st.contains("W"));
            System.out.println("    get(G): " + st.get("G"));
            System.out.println("    get(W): " + st.get("W"));
            System.out.println("    min(): " + st.min());
            System.out.println("    max(): " + st.max());
            System.out.println("    floor(A): " + st.floor("A"));
            System.out.println("    floor(G): " + st.floor("G"));
            System.out.println("    floor(W): " + st.floor("w"));
            System.out.println("    ceiling(A): " + st.ceiling("A"));
            System.out.println("    ceiling(G): " + st.ceiling("G"));
            System.out.println("    ceiling(W): " + st.ceiling("w"));
            System.out.println("    rank(G): " + st.rank("G"));
            System.out.println("    rank(W): " + st.rank("w"));
            System.out.println("    select(0): " + st.select(0));
            System.out.println("    select(3): " + st.select(3));
            System.out.println("    keys():");
            for(String str : st.keys()) System.out.println("        " + str);
            System.out.println("    keys(C, P):");
            for(String str : st.keys("C", "P")) System.out.println("        " + str);
            System.out.println("    keys(D, P):");
            for(String str : st.keys("D", "P")) System.out.println("        " + str);
            System.out.println("    keys(C, Q):");
            for(String str : st.keys("C", "Q")) System.out.println("        " + str);
            System.out.println("    keys(D, Q):");
            for(String str : st.keys("D", "Q")) System.out.println("        " + str);
            st.put("A", 3); System.out.println("    put(A, 3): " + st.toString());
            st.delete("A"); System.out.println("    delete(A): " + st.toString());
            st.put("B", 2); System.out.println("    put(B, 2): " + st.toString());
            st.put("C", 7); System.out.println("    put(C, 7): " + st.toString());
            st.deleteMin(); System.out.println("    deleteMin(): " + st.toString());
            st.deleteMax(); System.out.println("    deleteMax(): " + st.toString());
            st.deleteMin(); System.out.println("    deleteMin(): " + st.toString());
            st.put("Z", 23); System.out.println("    put(Z, 23): " + st.toString());
            st.deleteMax(); System.out.println("    deleteMax(): " + st.toString());
            System.out.println("");

            System.out.println("Populating symbol table:");
            st.put("B", 3); System.out.println("    put(B, 1), size() = " + st.size() + ":  " + st.toString());
            st.put("W", 3); System.out.println("    put(W, 2), size() = " + st.size() + ":  " + st.toString());
            st.put("O", 3); System.out.println("    put(O, 3), size() = " + st.size() + ":  " + st.toString());
            st.put("P", 4); System.out.println("    put(P, 4), size() = " + st.size() + ":  " + st.toString());
            st.put("F", 5); System.out.println("    put(F, 5), size() = " + st.size() + ":  " + st.toString());
            st.put("R", 6); System.out.println("    put(R, 6), size() = " + st.size() + ":  " + st.toString());
            st.put("C", 7); System.out.println("    put(C, 7), size() = " + st.size() + ":  " + st.toString());
            System.out.println("");

            System.out.println("Testing iterators:");
            System.out.println("    Contents: " + st.toString());
            System.out.println("    keys():");
            for(String str : st.keys()) System.out.println("        " + str);
            System.out.println("    keys(C, P):");
            for(String str : st.keys("C", "P")) System.out.println("        " + str);
            System.out.println("    keys(D, P):");
            for(String str : st.keys("D", "P")) System.out.println("        " + str);
            System.out.println("    keys(C, Q):");
            for(String str : st.keys("C", "Q")) System.out.println("        " + str);
            System.out.println("    keys(D, Q):");
            for(String str : st.keys("D", "Q")) System.out.println("        " + str);
            System.out.println("");
            
            System.out.println("Testing with multiple elements:");
            System.out.println("    Contents: " + st.toString());
            System.out.println("    isEmpty(): " + st.isEmpty());
            System.out.println("    size(): " + st.size());
            System.out.println("    size(C, P): " + st.size("C", "P"));
            System.out.println("    size(D, P): " + st.size("D", "P"));
            System.out.println("    size(D, Q): " + st.size("D", "Q"));
            System.out.println("    size(A, C): " + st.size("A", "C"));
            System.out.println("    contains(C): " + st.contains("C"));
            System.out.println("    contains(D): " + st.contains("D"));
            System.out.println("    get(C): " + st.get("C"));
            System.out.println("    get(D): " + st.get("D"));
            System.out.println("    min(): " + st.min());
            System.out.println("    max(): " + st.max());
            System.out.println("    floor(C): " + st.floor("C"));
            System.out.println("    floor(E): " + st.floor("E"));
            System.out.println("    ceiling(C): " + st.ceiling("C"));
            System.out.println("    ceiling(E): " + st.ceiling("E"));
            System.out.println("    rank(A): " + st.rank("A"));
            System.out.println("    rank(F): " + st.rank("F"));
            System.out.println("    rank(W): " + st.rank("W"));
            System.out.println("    rank(Z): " + st.rank("Z"));
            System.out.println("    select(4): " + st.select(4));
            System.out.println("    select(20): " + st.select(20));
            System.out.println("    select(-1): " + st.select(-1));
            st.delete("B"); System.out.println("    delete(B), size() = " + st.size() + ", " + st.toString());
            st.delete("W"); System.out.println("    delete(W), size() = " + st.size() + ", " + st.toString());
            st.delete("G"); System.out.println("    delete(G), size() = " + st.size() + ", " + st.toString());
            st.deleteMin(); System.out.println("    deleteMin(), size() = " + st.size() + ", " + st.toString());
            st.deleteMax(); System.out.println("    deleteMax(), size() = " + st.size() + ", " + st.toString());
            st.deleteMin(); System.out.println("    deleteMin(), size() = " + st.size() + ", " + st.toString());
            st.deleteMax(); System.out.println("    deleteMax(), size() = " + st.size() + ", " + st.toString());
            st.deleteMin(); System.out.println("    deleteMin(), size() = " + st.size() + ", " + st.toString());
            st.delete("X"); System.out.println("    delete(X), size() = " + st.size() + ", " + st.toString());
            st.deleteMin(); System.out.println("    deleteMin(), size() = " + st.size() + ", " + st.toString());
            st.deleteMax(); System.out.println("    deleteMax(), size() = " + st.size() + ", " + st.toString());
        }
        else
        {
            /*
            RecursiveBST<String, Integer> st =
                new RecursiveBST<String, Integer>();
            // sample input is SEARCHEXAMPLE
            System.out.println("Symbol table empty? " + st.isEmpty());
            System.out.println("Testing put() operation:");
            int cnt = 0;
            while(!StdIn.isEmpty())
            {
                String key = StdIn.readString();
                st.put(key, cnt);
                cnt++;
            }
            System.out.println("    Contents" + st.toString());
            System.out.println("Symbol table empty? " + st.isEmpty());
            System.out.println("");

            System.out.println("Testing get() operation:");
            System.out.println("    Key X has value " + st.get("X"));
            System.out.println("    Key Z has value " + st.get("Z"));
            System.out.println("");

            System.out.println("Testing delete() operation");
            int sz = st.size();
            st.delete("X");
            st.delete("M");
            System.out.println(st.toString());
            System.out.println("    Number of elements decreased by: " + (sz - st.size()));
            System.out.println("");

            System.out.println("Testing contains() operation");
            System.out.println("    Contains X? " + st.contains("X"));
            System.out.println("    Contains R? " + st.contains("R"));
            System.out.println("");

            System.out.println("Testing keys iterator:");
            for(String str : st.keys())
            {
                System.out.println("    " + str);
            }
            System.out.println("");
            }
            */
        }
    }
}

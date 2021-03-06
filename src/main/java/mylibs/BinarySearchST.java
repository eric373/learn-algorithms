package mylibs;
import java.util.Iterator;
import java.io.FileNotFoundException;
import mylibs.Util;

// ordered symbol table implementation
public class BinarySearchST<Key extends Comparable<Key>, Value>
{
    // NOTE: always maintain the invariant that the smallest key is at keys[0]
    // and the largest key is at keys[N - 1]
    private Key[] keys;
    private Value[] values;
    private int N;

    private class KeysIterable implements Iterable<Key>
    {
        private int idxStart;
        private int idxEnd;

        private class KeysIterator implements Iterator<Key>
        {
            private int idx;

            public KeysIterator() {idx = idxStart;}
            public boolean hasNext() {return idxStart <= idx && idx < idxEnd;}
            public Key next() {return keys[idx++];}
        }

        public KeysIterable()
        {
            this.idxStart = 0;
            this.idxEnd = N;
        }

        public KeysIterable(Key key1, Key key2)
        {
            this.idxStart = rank(key1);
            this.idxEnd = rank(key2);
            if(isKeyIn(this.idxEnd, key2))
            	this.idxEnd++;
        }

        public KeysIterator iterator()
        {return new KeysIterator();}
    }

    public BinarySearchST()
    {
        keys = (Key[]) new Comparable[1];
        values = (Value[]) new Object[1];
        N = 0;
    }

    // Helper function to check if idx is not null
    // and if keys[idx] == key
    private boolean isKeyIn(int idx, Key key)
    {return keys[idx] != null && keys[idx].compareTo(key) == 0;}

    // Searches for key and replaces its value. If not found, inserts key and value.
    public void put(Key key, Value value)
    {
    	// get the index to insert this key into
        int idx = rank(key);

        // if there is a key that was previously inserted in idx
        if(!isKeyIn(idx, key))
        {
        	// move all elements one index to the right
            int cnt = N - 1;
            while(cnt >= idx)
            {
                keys[cnt + 1] = keys[cnt];
                values[cnt + 1] = values[cnt];
                cnt--;
            }
            
            // idx should now be free, insert
            keys[idx] = key;
            values[idx] = value;
            N++;
            if(N >= keys.length)
                resize(2 * keys.length);
        }
        else
        {
            keys[idx] = key;
            values[idx] = value;
        }
    }

    public Value get(Key key)
    {
        int idx = rank(key);
        if(isKeyIn(idx, key))
            return values[idx];
        else return null;
    }

    // Eager delete implementation.
    public void delete(Key key)
    {
        int idx = rank(key);
        if(isKeyIn(idx, key))
        {
            // delete key-value pair at index
            keys[idx] = null;
            values[idx] = null;

            // move all elements to fill empty position
            int cnt = idx;
            while(cnt < N - 1)
            {
                keys[cnt] = keys[cnt + 1];
                values[cnt] = values[cnt + 1];
                cnt++;
            }

            // avoid loitering
            keys[N - 1] = null;
            values[N - 1] = null;
            N--;
            if(N <= (int)(keys.length / 4))
                resize(keys.length / 2);
        }
    }

    public boolean contains(Key key)
    {
        int idx = rank(key);
        if(isKeyIn(idx, key)) return true;
        else return false;
    }

    private void resize(int sz)
    {
        if(sz == 0)
        {
            Key[] keystemp = (Key[]) new Comparable[1];
            Value[] valuestemp = (Value[]) new Object[1];
            keys = keystemp;
            values = valuestemp;
        }
        else
        {
            Key[] keystemp = (Key[]) new Comparable[sz];
            Value[] valuestemp = (Value[]) new Object[sz];
            for(int cnt = 0; cnt < N; cnt++)
            {
                keystemp[cnt] = keys[cnt];
                valuestemp[cnt] = values[cnt];
            }
            keys = keystemp;
            values = valuestemp;
        }
    }

    public boolean isEmpty() {return N == 0;}
    public int size() {return N;}

    public Key min() {return keys[0];}
    public Key max()
    {
        if(N > 0) return keys[N - 1];
        else return null;
    }

    public Key floor(Key key)
    {
        int idx = rank(key);
        Key ret = null;
        if(idx > 0) ret = keys[idx - 1];
        return ret;
    }

    public Key ceiling(Key key)
    {
        int idx = rank(key);

        boolean found = isKeyIn(idx, key);

        if(found && idx < N - 1) return keys[idx + 1];
        else if(!found && idx <= N - 1) return keys[idx];
        else return null;
    }

    // Return the number of keys that are less than key.
    // Keys have to be unique.
    public int rank(Key key)
    {
        int a = 0;
        int b = N - 1;
        int mid;

        // key is in between smallest and largest key
        while(a <= b)
        {
            mid = (a + b) / 2;
            int res = key.compareTo(keys[mid]);
            if(res < 0)
                b = mid - 1;
            else if(res > 0)
                a = mid + 1;
            else
                return mid;
        }

        // After this loop, key is not found.
        // This means b < a.
        // If keys[0] < key and keys[N - 1] > key, keys[b] < key and keys[a] > key.
        // Therefore, a = rank(key).
        // If a == 0, then key < keys[0].
        // If a > N - 1, then key > keys[N - 1].
        return a;
    }

    // Return the key that has k keys less than it.
    public Key select(int k)
    {
        if(k >= 0 && k < N) return keys[k];
        else return null;
    }

    // Remove the pair with the smallest key.
    public void deleteMin()
    {
        if(N > 0)
        {
            keys[0] = null;
            values[0] = null;
            
            int idx = 0;
            while(idx < N - 1)
            {
                keys[idx] = keys[idx + 1];
                values[idx] = values[idx + 1];
                idx++;
            }
            N--;

            if(N <= (keys.length / 4))
                resize(2 * keys.length);
        }
    }

    // Remove the pair with the greatest key.
    public void deleteMax()
    {
        if(N > 0)
        {
            keys[N - 1] = null;
            values[N - 1] = null;
            N--;
            if(N <= (keys.length / 4))
                resize(2 * keys.length);
        }
    }

    public int size(Key from, Key to)
    {
        int idxTo = rank(to);
        if(isKeyIn(idxTo, to)) idxTo++;
        return idxTo - rank(from);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        if(N > 0)
        {
            int idx = 0;
            int cnt = 0;
            while(cnt < N)
            {
                sb.append("(" + keys[idx] + ", " + values[idx] + "), ");
                cnt++;
                idx++;
            }
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }

    private String toStringIterator()
    {
    	StringBuilder sb = new StringBuilder();
        for(Key key : keys())
        	if(key != null)
        		sb.append(key.toString() + ", ");
        if(sb.length() > 0) sb.setLength(sb.length() - 2);
    	return sb.toString();
    }

    private String toStringIterator(Key key1, Key key2)
    {
        StringBuilder sb = new StringBuilder();
        for(Key key : keys(key1, key2))
        	if(key != null)
        		sb.append(key.toString() + ", ");
        if(sb.length() > 0) sb.setLength(sb.length() - 2);
        return sb.toString();
    }

    public Iterable<Key> keys() {return new KeysIterable();}
    public Iterable<Key> keys(Key key1, Key key2) {return new KeysIterable(key1, key2);}

    public static void main(String[] args) throws FileNotFoundException
    {
        boolean test = false;
        if(args.length > 0)
        {
            for(String arg : args) {if(arg.equals("-test")) test = true;}
        }

        if(test)
        {
            BinarySearchST<String, Integer> st =
                new BinarySearchST <String, Integer>();

            String pf = "fail";
            System.out.println("Testing all operations on empty symbol table:");
            System.out.println("Contents: " + st.toString());
            pf = "fail"; if(st.isEmpty()) pf = "pass"; System.out.println("    " + pf + " - isEmpty(): " + st.isEmpty());
            pf = "fail"; if(st.size() == 0) pf = "pass"; System.out.println("    " + pf + " - size(): " + st.size());
            pf = "fail"; if(st.size("C", "G") == 0) pf = "pass"; System.out.println("    " + pf + " - size(C, G): " + st.size("C", "G"));
            pf = "fail"; if(st.contains("E") == false) pf = "pass"; System.out.println("    " + pf + " - contains(E): " + st.contains("E"));
            pf = "fail"; if(st.get("E") == null) pf = "pass"; System.out.println("    " + pf + " - get(E): " + st.get("E"));
            pf = "fail"; if(st.min() == null) pf = "pass"; System.out.println("    " + pf + " - min(): " + st.min());
            pf = "fail"; if(st.max() == null) pf = "pass"; System.out.println("    " + pf + " - max(): " + st.max());
            pf = "fail"; if(st.floor("E") == null) pf = "pass"; System.out.println("    " + pf + " - floor(E): " + st.floor("E"));
            pf = "fail"; if(st.ceiling("E") == null) pf = "pass"; System.out.println("    " + pf + " - ceiling(E): " + st.ceiling("E"));
            pf = "fail"; if(st.rank("E") == 0) pf = "pass"; System.out.println("    " + pf + " - rank(E): " + st.rank("E"));
            pf = "fail"; if(st.select(5) == null) pf = "pass"; System.out.println("    " + pf + " - select(5): " + st.select(5));
            pf = "fail"; st.delete("E"); if(st.toString().isEmpty()) pf = "pass"; System.out.println("    " + pf + " - delete(E): " + st.toString());
            pf = "fail"; st.deleteMin(); if(st.toString().isEmpty()) pf = "pass"; System.out.println("    " + pf + " - deleteMin(): " + st.toString());
            pf = "fail"; st.deleteMax(); if(st.toString().isEmpty()) pf = "pass"; System.out.println("    " + pf + " - deleteMax(): " + st.toString());
            pf = "fail"; if(st.toStringIterator().isEmpty()) pf = "pass"; System.out.println("    " + pf + " - keys(): " + st.toStringIterator());
            pf = "fail"; if(st.toStringIterator("C", "P").isEmpty()) pf = "pass"; System.out.println("    " + pf + " - keys(C, P): " + st.toStringIterator("C", "P"));
            pf = "fail"; if(st.toStringIterator("D", "P").isEmpty()) pf = "pass"; System.out.println("    " + pf + " - keys(D, P): " + st.toStringIterator("D", "P"));
            pf = "fail"; if(st.toStringIterator("C", "Q").isEmpty()) pf = "pass"; System.out.println("    " + pf + " - keys(C, Q): " + st.toStringIterator("C", "Q"));
            pf = "fail"; if(st.toStringIterator("D", "Q").isEmpty()) pf = "pass"; System.out.println("    " + pf + " - keys(D, Q): " + st.toStringIterator("D", "Q"));
            System.out.println("");

            System.out.println("Testing all operations with 1 element:");
            pf = "fail"; st.put("G", 3); if(st.toString().equals(st.toString())) pf = "pass"; System.out.println("    " + pf + " - put(G, 3): " + st.toString());
            pf = "fail"; if(!st.isEmpty()) pf = "pass"; System.out.println("    " + pf + " - isEmpty(): " + st.isEmpty());
            pf = "fail"; if(st.size() == 1) pf = "pass"; System.out.println("    " + pf + " - size(): " + st.size());
            pf = "fail"; if(st.size("B", "G") == 1) pf = "pass"; System.out.println("    " + pf + " - size(B, G): " + st.size("B", "G"));
            pf = "fail"; if(st.size("B", "X") == 1) pf = "pass"; System.out.println("    " + pf + " - size(B, X): " + st.size("B", "X"));
            pf = "fail"; if(st.size("C", "D") == 0) pf = "pass"; System.out.println("    " + pf + " - size(C, D): " + st.size("C", "D"));
            pf = "fail"; if(st.size("W", "Z") == 0) pf = "pass"; System.out.println("    " + pf + " - size(W, Z): " + st.size("W", "Z"));
            pf = "fail"; if(st.contains("G")) pf = "pass"; System.out.println("    " + pf + " - contains(G): " + st.contains("G"));
            pf = "fail"; if(!st.contains("W")) pf = "pass"; System.out.println("    " + pf + " - contains(W): " + st.contains("W"));
            pf = "fail"; if(st.get("G") == 3) pf = "pass"; System.out.println("    " + pf + " - get(G): " + st.get("G"));
            pf = "fail"; if(st.get("W") == null) pf = "pass"; System.out.println("    " + pf + " - get(W): " + st.get("W"));
            pf = "fail"; if(st.min().equals("G")) pf = "pass"; System.out.println("    " + pf + " - min(): " + st.min());
            pf = "fail"; if(st.max().equals("G")) pf = "pass"; System.out.println("    " + pf + " - max(): " + st.max());
            pf = "fail"; if(st.floor("A") == null) pf = "pass"; System.out.println("    " + pf + " - floor(A): " + st.floor("A"));
            pf = "fail"; if(st.floor("G") == null) pf = "pass"; System.out.println("    " + pf + " - floor(G): " + st.floor("G"));
            pf = "fail"; if(st.floor("W").equals("G")) pf = "pass"; System.out.println("    " + pf + " - floor(W): " + st.floor("w"));
            pf = "fail"; if(st.ceiling("A").equals("G")) pf = "pass"; System.out.println("    " + pf + " - ceiling(A): " + st.ceiling("A"));
            pf = "fail"; if(st.ceiling("G") == null) pf = "pass"; System.out.println("    " + pf + " - ceiling(G): " + st.ceiling("G"));
            pf = "fail"; if(st.ceiling("W") == null) pf = "pass"; System.out.println("    " + pf + " - ceiling(W): " + st.ceiling("w"));
            pf = "fail"; if(st.rank("G") == 0) pf = "pass"; System.out.println("    " + pf + " - rank(G): " + st.rank("G"));
            pf = "fail"; if(st.rank("W") == 1) pf = "pass"; System.out.println("    " + pf + " - rank(W): " + st.rank("w"));
            pf = "fail"; if(st.select(0).equals("G")) pf = "pass"; System.out.println("    " + pf + " - select(0): " + st.select(0));
            pf = "fail"; if(st.select(3) == null) pf = "pass"; System.out.println("    " + pf + " - select(3): " + st.select(3));
            pf = "fail"; if(st.toStringIterator().equals("G")) pf = "pass"; System.out.println("    " + pf + " - keys(): " + st.toStringIterator());
            pf = "fail"; if(st.toStringIterator("C", "P").equals("G")) pf = "pass"; System.out.println("    " + pf + " - st.keys(C, P): " + st.toStringIterator());
            pf = "fail"; if(st.toStringIterator("D", "P").equals("G")) pf = "pass"; System.out.println("    " + pf + " - st.keys(D, P): " + st.toStringIterator());
            pf = "fail"; if(st.toStringIterator("C", "Q").equals("G")) pf = "pass"; System.out.println("    " + pf + " - st.keys(C, Q): " + st.toStringIterator());
            pf = "fail"; if(st.toStringIterator("D", "Q").equals("G")) pf = "pass"; System.out.println("    " + pf + " - st.keys(D, Q): " + st.toStringIterator());
            pf = "fail"; st.put("A", 3); if(st.toString().equals("(A, 3), (G, 3)")) pf = "pass"; System.out.println("    " + pf + " - put(A, 3): " + st.toString());
            pf = "fail"; st.delete("A"); if(st.toString().equals("(G, 3)")) pf = "pass"; System.out.println("    " + pf + " - delete(A): " + st.toString());
            pf = "fail"; st.put("B", 2); if(st.toString().equals("(B, 2), (G, 3)")) pf = "pass"; System.out.println("    " + pf + " - put(B, 2): " + st.toString());
            pf = "fail"; st.put("C", 7); if(st.toString().equals("(B, 2), (C, 7), (G, 3)")) pf = "pass"; System.out.println("    " + pf + " - put(C, 7): " + st.toString());
            pf = "fail"; st.deleteMin(); if(st.toString().equals("(C, 7), (G, 3)")) pf = "pass"; System.out.println("    " + pf + " - deleteMin(): " + st.toString());
            pf = "fail"; st.deleteMax(); if(st.toString().equals("(C, 7)")) pf = "pass"; System.out.println("    " + pf + " - deleteMax(): " + st.toString());
            pf = "fail"; st.deleteMin(); if(st.toString().equals("")) pf = "pass"; System.out.println("    " + pf + " - deleteMin(): " + st.toString());
            pf = "fail"; st.put("Z", 23); if(st.toString().equals("(Z, 23)")) pf = "pass"; System.out.println("    " + pf + " - put(Z, 23): " + st.toString());
            pf = "fail"; st.deleteMax(); if(st.toString().equals("")) pf = "pass"; System.out.println("    " + pf + " - deleteMax(): " + st.toString());
            System.out.println("");

            System.out.println("Populating symbol table:");
            pf = "fail"; st.put("B", 3); if(st.toString().equals("(B, 3)")) pf = "pass"; System.out.println("    " + pf + " - put(B, 1), size() = " + st.size() + ":  " + st.toString());
            pf = "fail"; st.put("W", 3); if(st.toString().equals("(B, 3), (W, 3)")) pf = "pass"; System.out.println("    " + pf + " - put(W, 2), size() = " + st.size() + ":  " + st.toString());
            pf = "fail"; st.put("O", 3); if(st.toString().equals("(B, 3), (O, 3), (W, 3)")) pf = "pass"; System.out.println("    " + pf + " - put(O, 3), size() = " + st.size() + ":  " + st.toString());
            pf = "fail"; st.put("P", 4); if(st.toString().equals("(B, 3), (O, 3), (P, 4), (W, 3)")) pf = "pass"; System.out.println("    " + pf + " - put(P, 4), size() = " + st.size() + ":  " + st.toString());
            pf = "fail"; st.put("F", 5); if(st.toString().equals("(B, 3), (F, 5), (O, 3), (P, 4), (W, 3)")) pf = "pass"; System.out.println("    " + pf + " - put(F, 5), size() = " + st.size() + ":  " + st.toString());
            pf = "fail"; st.put("R", 6); if(st.toString().equals("(B, 3), (F, 5), (O, 3), (P, 4), (R, 6), (W, 3)")) pf = "pass"; System.out.println("    " + pf + " - put(R, 6), size() = " + st.size() + ":  " + st.toString());
            pf = "fail"; st.put("C", 7); if(st.toString().equals("(B, 3), (C, 7), (F, 5), (O, 3), (P, 4), (R, 6), (W, 3)")) pf = "pass"; System.out.println("    " + pf + " - put(C, 7), size() = " + st.size() + ":  " + st.toString());
            System.out.println("");

            System.out.println("Testing iterators:");
            System.out.println("Contents: " + st.toString());

            pf = "fail"; if(st.toStringIterator().equals("B, C, F, O, P, R, W")) pf = "pass"; System.out.println("    " + pf + " - keys(): " + st.toStringIterator());
            pf = "fail"; if(st.toStringIterator("C", "P").equals("C, F, O, P")) pf = "pass"; System.out.println("    " + pf + " - keys(C, P): " + st.toStringIterator("C", "P"));
            pf = "fail"; if(st.toStringIterator("D", "P").equals("F, O, P")) pf = "pass"; System.out.println("    " + pf + " - keys(D, P): " + st.toStringIterator("D", "P"));
            pf = "fail"; if(st.toStringIterator("C", "Q").equals("C, F, O, P")) pf = "pass"; System.out.println("    " + pf + " - keys(C, Q): " + st.toStringIterator("C", "Q"));
            pf = "fail"; if(st.toStringIterator("D", "Q").equals("F, O, P")) pf = "pass"; System.out.println("    " + pf + " - keys(D, Q): " + st.toStringIterator("D", "Q"));
            System.out.println("");

            System.out.println("Testing with multiple elements:");
            System.out.println("Contents: " + st.toString());
            pf = "fail"; if(!st.isEmpty()) pf = "pass"; System.out.println("    " + pf + " - isEmpty(): " + st.isEmpty());
            pf = "fail"; if(st.size() == 7) pf = "pass"; System.out.println("    " + pf + " - size(): " + st.size());
            pf = "fail"; if(st.size("C", "P") == 4) pf = "pass"; System.out.println("    " + pf + " - size(C, P): " + st.size("C", "P"));
            pf = "fail"; if(st.size("D", "P") == 3) pf = "pass"; System.out.println("    " + pf + " - size(D, P): " + st.size("D", "P"));
            pf = "fail"; if(st.size("D", "Q") == 3) pf = "pass"; System.out.println("    " + pf + " - size(D, Q): " + st.size("D", "Q"));
            pf = "fail"; if(st.size("A", "C") == 2) pf = "pass"; System.out.println("    " + pf + " - size(A, C): " + st.size("A", "C"));
            pf = "fail"; if(st.contains("C")) pf = "pass"; System.out.println("    " + pf + " - contains(C): " + st.contains("C"));
            pf = "fail"; if(!st.contains("D")) pf = "pass"; System.out.println("    " + pf + " - contains(D): " + st.contains("D"));
            pf = "fail"; if(st.get("C") == 7) pf = "pass"; System.out.println("    " + pf + " - get(C): " + st.get("C"));
            pf = "fail"; if(st.get("D") == null) pf = "pass"; System.out.println("    " + pf + " - get(D): " + st.get("D"));
            pf = "fail"; if(st.min().equals("B")) pf = "pass"; System.out.println("    " + pf + " - min(): " + st.min());
            pf = "fail"; if(st.max().equals("W")) pf = "pass"; System.out.println("    " + pf + " - max(): " + st.max());
            pf = "fail"; if(st.floor("C").equals("B")) pf = "pass"; System.out.println("    " + pf + " - floor(C): " + st.floor("C"));
            pf = "fail"; if(st.floor("E").equals("C")) pf = "pass"; System.out.println("    " + pf + " - floor(E): " + st.floor("E"));
            pf = "fail"; if(st.ceiling("C").equals("F")) pf = "pass"; System.out.println("    " + pf + " - ceiling(C): " + st.ceiling("C"));
            pf = "fail"; if(st.ceiling("E").equals("F")) pf = "pass"; System.out.println("    " + pf + " - ceiling(E): " + st.ceiling("E"));
            pf = "fail"; if(st.rank("A") == 0) pf = "pass"; System.out.println("    " + pf + " - rank(A): " + st.rank("A"));
            pf = "fail"; if(st.rank("F") == 2) pf = "pass"; System.out.println("    " + pf + " - rank(F): " + st.rank("F"));
            pf = "fail"; if(st.rank("W") == 6) pf = "pass"; System.out.println("    " + pf + " - rank(W): " + st.rank("W"));
            pf = "fail"; if(st.rank("Z") == 7) pf = "pass"; System.out.println("    " + pf + " - rank(Z): " + st.rank("Z"));
            pf = "fail"; if(st.select(4).equals("P")) pf = "pass"; System.out.println("    " + pf + " - select(4): " + st.select(4));
            pf = "fail"; if(st.select(20) == null) pf = "pass"; System.out.println("    " + pf + " - select(20): " + st.select(20));
            pf = "fail"; if(st.select(-1) == null) pf = "pass"; System.out.println("    " + pf + " - select(-1): " + st.select(-1));
            pf = "fail"; st.delete("B"); if(st.toString().equals("(C, 7), (F, 5), (O, 3), (P, 4), (R, 6), (W, 3)")) pf = "pass"; System.out.println("    " + pf + " - delete(B), size() = " + st.size() + ", " + st.toString());
            pf = "fail"; st.delete("W"); if(st.toString().equals("(C, 7), (F, 5), (O, 3), (P, 4), (R, 6)")) pf = "pass"; System.out.println("    " + pf + " - delete(W), size() = " + st.size() + ", " + st.toString());
            pf = "fail"; st.delete("G"); if(st.toString().equals("(C, 7), (F, 5), (O, 3), (P, 4), (R, 6)")) pf = "pass"; System.out.println("    " + pf + " - delete(G), size() = " + st.size() + ", " + st.toString());
            pf = "fail"; st.deleteMin(); if(st.toString().equals("(F, 5), (O, 3), (P, 4), (R, 6)")) pf = "pass"; System.out.println("    " + pf + " - deleteMin(), size() = " + st.size() + ", " + st.toString());
            pf = "fail"; st.deleteMax(); if(st.toString().equals("(F, 5), (O, 3), (P, 4)")) pf = "pass"; System.out.println("    " + pf + " - deleteMax(), size() = " + st.size() + ", " + st.toString());
            pf = "fail"; st.deleteMin(); if(st.toString().equals("(O, 3), (P, 4)")) pf = "pass"; System.out.println("    " + pf + " - deleteMin(), size() = " + st.size() + ", " + st.toString());
            pf = "fail"; st.deleteMax(); if(st.toString().equals("(O, 3)")) pf = "pass"; System.out.println("    " + pf + " - deleteMax(), size() = " + st.size() + ", " + st.toString());
            pf = "fail"; st.deleteMin(); if(st.toString().equals("")) pf = "pass"; System.out.println("    " + pf + " - deleteMin(), size() = " + st.size() + ", " + st.toString());
            pf = "fail"; st.delete("X"); if(st.toString().equals("")) pf = "pass"; System.out.println("    " + pf + " - delete(X), size() = " + st.size() + ", " + st.toString());
            pf = "fail"; st.deleteMin(); if(st.toString().equals("")) pf = "pass"; System.out.println("    " + pf + " - deleteMin(), size() = " + st.size() + ", " + st.toString());
            pf = "fail"; st.deleteMax(); if(st.toString().equals("")) pf = "pass"; System.out.println("    " + pf + " - deleteMax(), size() = " + st.size() + ", " + st.toString());
        }
        else
        {
            BinarySearchST<String, Integer> st =
                new BinarySearchST<String, Integer>();
            // sample input is SEARCHEXAMPLE
            System.out.println("Symbol table empty? " + st.isEmpty());
            System.out.println("Testing put() operation:");
            String[] a = Util.fromFile(args[0]);
            int cnt = 0;
            while(cnt < a.length)
            {
                String key = a[cnt];
                st.put(key, cnt);
                cnt++;
            }
            System.out.println("    Contents: " + st.toString());
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
            System.out.println("    Contents: " + st.toString());
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
        }
}

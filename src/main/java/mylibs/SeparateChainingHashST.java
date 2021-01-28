package mylibs;
import java.util.Iterator;
import java.util.LinkedList;
import mylibs.SymbolTable;
import mylibs.LinkedListSequentialSearchST;
import java.lang.reflect.Array;
import mylibs.Pair;
import java.io.FileNotFoundException;
import mylibs.Util;

public class SeparateChainingHashST<Key extends Comparable<Key>, Value> //implements SymbolTable<Key, Value>
{
    // ----------------------------------------
    // Private members
    // ----------------------------------------
    private int N, baseM, M, lgM;
    private int[] primes;
    LinkedListSequentialSearchST<Key, Value>[] a;

    // ----------------------------------------
    // Private classes
    // ----------------------------------------
    // Iteration over every key
    private class KeysIterable implements Iterable<Key>
    {
        private class KeysIterator implements Iterator<Key>
        {
            private Iterator<Key> it;
            private int idx;

            public KeysIterator()
            {
                idx = nextList(0);
                if(idx >= 0) it = a[idx].keys().iterator();
            }
            public boolean hasNext()
            {
                if(it == null) return false;
                else if(!it.hasNext())
                {
                    idx = nextList(++idx);
                    if(idx >= 0)
                    {
                        it = a[idx].keys().iterator();
                        return true;
                    }
                    else return false;
                }
                else return true;
            }

            public Key next() {return it.next();}
            public void remove() {}
        }
        public KeysIterable() {}
        public KeysIterator iterator() {return new KeysIterator();}
    }

    // Iteration over every key-value pair
    private class EntriesIterable implements Iterable<Pair<Key, Value>>
    {
        private class EntriesIterator implements Iterator<Pair<Key, Value>>
        {
            private Iterator<Pair<Key, Value>> it;
            private int idx;

            // Constructor
            public EntriesIterator()
            {
                idx = nextList(0);
                if(idx >= 0) it = a[idx].entries().iterator();
            }
            public boolean hasNext()
            {
                boolean ret = false;
                if(it != null )
                {
                    if(!it.hasNext())
                    {
                        idx = nextList(++idx);
                        if(idx > 0)
                        {
                            it = a[idx].entries().iterator();
                            ret = true;
                        }
                    }
                    else ret = true;
                }
                return ret;
            }

            public Pair<Key, Value> next() {return it.next();}
            public void remove() {}
        }
        public void EntriesIterable () {}
        public Iterator<Pair<Key, Value>> iterator() {return new EntriesIterator();}
    }

    // ----------------------------------------
    // Private Functions
    // ----------------------------------------
    // Modular hashing function
    private int hash(Key key)
    {
        // Get modulo by a prime larger than M first then
        // get modulo by M.
        // This is to deal with the fact that M will be a multiple
        // of 2 when array resizing is used.
        int ret = key.hashCode() & 0x7fffffff;
        if(lgM < 26) ret = ret % primes[lgM + 5];
        return ret % M;
    }

    // Get the index of the next list that is not empty starting from, and
    // including curr
    private int nextList(int curr)
    {
        while(curr < M)
        {
            if(a[curr] != null && !a[curr].isEmpty()) break;
            else curr++;
        }
        if(curr >= M) return -1;
        else return curr;
    }

    // Array resizing
    private void resize(int sz)
    {
    	if(sz < baseM)
    	{
    		lgM = 5;
    		sz = baseM;
    	}
    	else if(sz == M / 2) lgM--;
    	else if(sz == M * 2) lgM++;

    	if(M != sz)
    	{
	    	M = sz;
	        LinkedListSequentialSearchST<Key, Value>[] temp = a;
	        a = (LinkedListSequentialSearchST<Key, Value>[])
	            Array.newInstance(LinkedListSequentialSearchST.class, M);
	        int i = 0;
	        N = 0;
	        while(i < temp.length)
	        {
	            if(temp[i] != null)
	            {
	                for(Pair<Key, Value> pair : temp[i].entries())
	                    this.put(pair.key, pair.val);
	            }
	            i++;
	        }
    	}
    }

    // ----------------------------------------
    // Constructor
    // ----------------------------------------
    public SeparateChainingHashST()
    {
        N = 0;
        baseM = 37;
        M = baseM;
        lgM = 5;
        primes = new int[]{0, 0, 0, 0, 0, 31, 61, 127, 251, 509, 1021, 2039, 4093, 8191, 16381, 32749, 65521, 131071, 262139, 524287, 1048573, 2097143, 4194301, 8388593, 16777213, 33554393, 67108859, 134217689, 268435399, 536870909, 1073741789, 2147483647};
        a = (LinkedListSequentialSearchST<Key,Value>[])
            Array.newInstance(LinkedListSequentialSearchST.class, M);
    }

    // ----------------------------------------
    // Primary operations
    // ----------------------------------------
    // Insert operation
    public void put(Key key, Value val)
    {
        int i = hash(key);
        if(a[i] == null) a[i] = new LinkedListSequentialSearchST<Key, Value>();
        int oldsz = a[i].size();
        a[i].put(key, val);
        if(oldsz != a[i].size()) N++;
        if(N / M > 8) resize(2 * M);
    }

    // Get operation
    public Value get(Key key)
    {
        int i = hash(key);
        if(a[i] == null) return null;
        return a[i].get(key);
    }

    // Delete operation
    public void delete(Key key)
    {
        int i = hash(key);
        if(a[i] != null)
        {
            int oldsz = a[i].size();
            a[i].delete(key);
            if(a[i].size() != oldsz) this.N--;
            if(a[i].size() == 0) a[i] = null;
            if(N / M < 2) resize(M / 2);
        }
    }

    // Check if a certain key exists
    public boolean contains(Key key)
    {
        int i = hash(key);
        return a[i] != null && a[i].contains(key);
    }

    // ----------------------------------------
    // Convenience
    // ----------------------------------------
    // Check if any elements are present
    public boolean isEmpty() {return N == 0;};

    // Get number of elements
    public int size() {return N;}

    // Return String representation
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for(Pair<Key, Value> pair : this.entries())
        {
            sb.append("(" + pair.key + ", " + pair.val + "), ");
        }
        if(sb.length() > 0) sb.setLength(sb.length() - 2);
        return sb.toString();
    }

    // iterables
    public KeysIterable keys() {return new KeysIterable();}
    public EntriesIterable entries() {return new EntriesIterable();}

    public static void main(String[] args) throws FileNotFoundException
    {
        boolean test = false;
        boolean resizeTest = false;
        if(args.length > 0)
        {
            for(String arg : args)
            {
                if(arg.equals("-test")) test = true;
                if(arg.equals("-resizeTest")) resizeTest = true;
            }
        }

        if(test)
        {
            Integer ret;
            boolean retBool;
            String retStr;
            Integer sz;
            SeparateChainingHashST<Integer, String> st = new SeparateChainingHashST<Integer, String>();
            String pf = "fail";
            System.out.println("Testing all operations on empty symbol table:");
            System.out.println("Contents: " + st.toString());
            pf = "fail"; retBool = st.isEmpty(); if(retBool) pf = "pass"; System.out.println("    " + pf + " - isEmpty(): " + retBool);
            pf = "fail"; ret = st.size(); if(ret == 0) pf = "pass"; System.out.println("    " + pf + " - size(): " + ret);
            pf = "fail"; retBool = st.contains(30); if(!retBool) pf = "pass"; System.out.println("    " + pf + " - contains(30): " + retBool);
            pf = "fail"; retStr = st.get(30); if(retStr == null) pf = "pass"; System.out.println("    " + pf + " - get(30): " + retStr);
            pf = "fail"; if(st.toString().isEmpty()) pf = "pass"; System.out.println("    " + pf + " - keys(): " + st.toString());
            System.out.println("");

            System.out.println("Testing all operations with 1 element:");
            pf = "fail"; st.put(30, "A"); retStr = st.toString(); if(retStr.equals("(30, A)")) pf = "pass"; System.out.println("    " + pf + " - put(30, A), size() = " + st.size() + ": " + retStr);
            pf = "fail"; retBool = st.isEmpty(); if(!retBool) pf = "pass"; System.out.println("    " + pf + " - isEmpty(): " + retBool);
            pf = "fail"; ret = st.size(); if(ret == 1) pf = "pass"; System.out.println("    " + pf + " - size(): " + st.size());
            pf = "fail"; retBool = st.contains(30); if(retBool) pf = "pass"; System.out.println("    " + pf + " - contains(30): " + retBool);
            pf = "fail"; retBool = st.contains(5); if(!retBool) pf = "pass"; System.out.println("    " + pf + " - contains(5): " + retBool);
            pf = "fail"; retStr = st.get(30); if(retStr == "A") pf = "pass"; System.out.println("    " + pf + " - get(30): " + retStr);
            pf = "fail"; retStr = st.get(50); if(retStr == null) pf = "pass"; System.out.println("    " + pf + " - get(50): " + retStr);
            pf = "fail"; st.put(10, "B"); retStr = st.toString(); if(retStr.equals("(10, B), (30, A)")) pf = "pass"; System.out.println("    " + pf + " - put(10, B), size() =  " + st.size() + ": " + retStr);
            pf = "fail"; st.delete(10); retStr = st.toString(); if(retStr.equals("(30, A)")) pf = "pass"; System.out.println("    " + pf + " - delete(10), size() = " + st.size() + ": " + retStr);
            System.out.println("");

            System.out.println("Populating symbol table:");
            pf = "fail"; st.put(30, "A"); retStr = st.toString(); if(retStr.equals("(30, A)")) pf = "pass"; System.out.println("    " + pf + " - put(30, A), size() = " + st.size() + ": " + retStr);
            pf = "fail"; st.put(45, "B"); retStr = st.toString(); if(retStr.equals("(45, B), (30, A)")) pf = "pass"; System.out.println("    " + pf + " - put(45, B), size() = " + st.size() + ": " + retStr);
            pf = "fail"; st.put(10, "C"); retStr = st.toString(); if(retStr.equals("(45, B), (10, C), (30, A)")) pf = "pass"; System.out.println("    " + pf + " - put(10, C), size() = " + st.size() + ": " + retStr);
            pf = "fail"; st.put(15, "D"); retStr = st.toString(); if(retStr.equals("(45, B), (10, C), (15, D), (30, A)")) pf = "pass"; System.out.println("    " + pf + " - put(15, D), size() = " + st.size() + ": " + retStr);
            pf = "fail"; st.put(25, "E"); retStr = st.toString(); if(retStr.equals("(45, B), (10, C), (15, D), (25, E), (30, A)")) pf = "pass"; System.out.println("    " + pf + " - put(25, E), size() = " + st.size() + ": " + retStr);
            pf = "fail"; st.put(35, "F"); retStr = st.toString(); if(retStr.equals("(45, B), (10, C), (15, D), (25, E), (30, A), (35, F)")) pf = "pass"; System.out.println("    " + pf + " - put(35, F), size() = " + st.size() + ": " + retStr);
            pf = "fail"; st.put(60, "G"); retStr = st.toString(); if(retStr.equals("(45, B), (10, C), (15, D), (60, G), (25, E), (30, A), (35, F)")) pf = "pass"; System.out.println("    " + pf + " - put(60, G), size() = " + st.size() + ": " + retStr);
            pf = "fail"; st.put(5, "H"); retStr = st.toString(); if(retStr.equals("(5, H), (45, B), (10, C), (15, D), (60, G), (25, E), (30, A), (35, F)")) pf = "pass"; System.out.println("    " + pf + " - put(5, H), size() = " + st.size() + ": " + retStr);
            pf = "fail"; st.put(13, "I"); retStr = st.toString(); if(retStr.equals("(5, H), (45, B), (10, C), (13, I), (15, D), (60, G), (25, E), (30, A), (35, F)")) pf = "pass"; System.out.println("    " + pf + " - put(13, I), size() = " + st.size() + ": " + retStr);
            pf = "fail"; st.put(3, "J"); retStr = st.toString(); if(retStr.equals("(3, J), (5, H), (45, B), (10, C), (13, I), (15, D), (60, G), (25, E), (30, A), (35, F)")) pf = "pass"; System.out.println("    " + pf + " - put(3, J), size() = " + st.size() + ": " + retStr);
            pf = "fail"; st.put(11, "K"); retStr = st.toString(); if(retStr.equals("(3, J), (5, H), (45, B), (10, C), (11, K), (13, I), (15, D), (60, G), (25, E), (30, A), (35, F)")) pf = "pass"; System.out.println("    " + pf + " - put(11, K), size() = " + st.size() + ": " + retStr);
            pf = "fail"; st.put(20, "L"); retStr = st.toString(); if(retStr.equals("(3, J), (5, H), (45, B), (10, C), (11, K), (13, I), (15, D), (20, L), (60, G), (25, E), (30, A), (35, F)")) pf = "pass"; System.out.println("    " + pf + " - put(20, L), size() = " + st.size() + ": " + retStr);
            pf = "fail"; st.put(27, "M"); retStr = st.toString(); if(retStr.equals("(3, J), (5, H), (45, B), (10, C), (11, K), (13, I), (15, D), (20, L), (60, G), (25, E), (27, M), (30, A), (35, F)")) pf = "pass"; System.out.println("    " + pf + " - put(27, M), size() = " + st.size() + ": " + retStr);
            pf = "fail"; st.put(31, "N"); retStr = st.toString(); if(retStr.equals("(3, J), (5, H), (45, B), (10, C), (11, K), (13, I), (15, D), (20, L), (60, G), (25, E), (27, M), (30, A), (31, N), (35, F)")) pf = "pass"; System.out.println("    " + pf + " - put(31, N), size() = " + st.size() + ": " + retStr);
            System.out.println("");

            System.out.println("Testing iterators:");
            StringBuilder sb = new StringBuilder();
            for(Integer i : st.keys())
                sb.append(i + ", ");
            if(sb.length() > 0) sb.setLength(sb.length() - 2);
            pf = "fail"; retStr = sb.toString(); if(retStr.equals("3, 5, 45, 10, 11, 13, 15, 20, 60, 25, 27, 30, 31, 35")) pf = "pass"; System.out.println("    " + pf + " - st.keys(): " + sb.toString());
            System.out.println("");
 
            System.out.println("Testing with multiple elements:");
            System.out.println("Contents: " + st.toString());
            pf = "fail"; retBool = st.isEmpty(); if(!retBool) pf = "pass"; System.out.println("    " + pf + " - isEmpty(): " + retBool);
            pf = "fail"; ret = st.size(); if(ret == 14) pf = "pass"; System.out.println("    " + pf + " - size(): " + ret);
            pf = "fail"; retBool = st.contains(30); if(retBool) pf = "pass"; System.out.println("    " + pf + " - contains(30): " + retBool);
            pf = "fail"; retBool = st.contains(32); if(!retBool) pf = "pass"; System.out.println("    " + pf + " - contains(32): " + retBool);
            pf = "fail"; retStr = st.get(30); if(retStr.equals("A")) pf = "pass"; System.out.println("    " + pf + " - get(30): " + retStr);
            pf = "fail"; retStr = st.get(31); if(retStr.equals("N")) pf = "pass"; System.out.println("    " + pf + " - get(31): " + retStr);
            pf = "fail"; retStr = st.get(20); if(retStr.equals("L")) pf = "pass"; System.out.println("    " + pf + " - get(20): " + retStr);
            pf = "fail"; sz = st.size(); ret = 35; st.delete(ret); retBool = st.contains(ret); if(!retBool && st.size() + 1 == sz) pf = "pass"; System.out.println("    " + pf + " - delete(" + ret + "): " + st.toString());
            pf = "fail"; sz = st.size(); ret = 10; st.delete(ret); retBool = st.contains(ret); if(!retBool && st.size() + 1 == sz) pf = "pass"; System.out.println("    " + pf + " - delete(" + ret + "): " + st.toString());
            pf = "fail"; sz = st.size(); ret = 20; st.delete(ret); retBool = st.contains(ret); if(!retBool && st.size() + 1 == sz) pf = "pass"; System.out.println("    " + pf + " - delete(" + ret + "): " + st.toString());
            pf = "fail"; sz = st.size(); ret = 5; st.delete(ret); retBool = st.contains(ret); if(!retBool && st.size() + 1 == sz) pf = "pass"; System.out.println("    " + pf + " - delete(" + ret + "): " + st.toString());
            pf = "fail"; sz = st.size(); ret = 15; st.delete(ret); retBool = st.contains(ret); if(!retBool && st.size() + 1 == sz) pf = "pass"; System.out.println("    " + pf + " - delete(" + ret + "): " + st.toString());
            pf = "fail"; sz = st.size(); ret = 31; st.delete(ret); retBool = st.contains(ret); if(!retBool && st.size() + 1 == sz) pf = "pass"; System.out.println("    " + pf + " - delete(" + ret + "): " + st.toString());
            pf = "fail"; sz = st.size(); ret = 25; st.delete(ret); retBool = st.contains(ret); if(!retBool && st.size() + 1 == sz) pf = "pass"; System.out.println("    " + pf + " - delete(" + ret + "): " + st.toString());
            pf = "fail"; sz = st.size(); ret = 30; st.delete(ret); retBool = st.contains(ret); if(!retBool && st.size() + 1 == sz) pf = "pass"; System.out.println("    " + pf + " - delete(" + ret + "): " + st.toString());
            pf = "fail"; sz = st.size(); ret = 11; st.delete(ret); retBool = st.contains(ret); if(!retBool && st.size() + 1 == sz) pf = "pass"; System.out.println("    " + pf + " - delete(" + ret + "): " + st.toString());
        }
        else if(resizeTest)
        {
            System.out.println("Testing Array Resizing:");
            SeparateChainingHashST<String, Integer> st = new SeparateChainingHashST<String, Integer>();
            int cnt = 0;
            String[] a = Util.fromFile(args[1], "\\w+");
            while(cnt < a.length)
            {
                String key = a[cnt];
                st.put(key, cnt);
                cnt++;
            }
            String pf;
            pf = "fail"; if(st.size() == 10674) pf = "pass"; System.out.println("    " + pf + " - Number of keys added: " + st.size());

            LinkedList<String> ls = new LinkedList<String>();
            for(String key : st.keys()) ls.add(key);
            for(String key : ls) st.delete(key);
            pf = "fail"; if(st.size() == 0) pf = "pass"; System.out.println("    " + pf + " - Number of keys after deletion: " + st.size());
        }
        else
        {
            SeparateChainingHashST<String, Integer> st = new SeparateChainingHashST<String, Integer>();
            System.out.println("Symbol table empty? " + st.isEmpty());
            System.out.println("Testing put() operation:");
            String[] a = Util.fromFile(args[0], "\\w+");
            int cnt = 0;
            while(cnt < a.length)
            {
                String key = a[cnt];
                st.put(key, cnt);
                cnt++;
            }
            System.out.println("    st.size(): " + st.size());

            System.out.println("    First 5 elements: ");
            cnt = 0;
            for(Pair<String, Integer> pair : st.entries())
            {
                System.out.println(String.format("        %s: %d", pair.key, pair.val));
                if(++cnt >= 5) break;
            }
            System.out.println("Symbol table empty? " + st.isEmpty());
            System.out.println("");

            System.out.println("Testing get() operation:");
            System.out.println("    indebted has value " + st.get("indebted"));
            System.out.println("    mood has value " + st.get("mood"));
            System.out.println("");

            System.out.println("Testing delete() operation");
            int sz = st.size();
            st.delete("indebted");
            st.delete("mood");
            System.out.println("    Number of elements decreased by: " + (sz - st.size()));
            System.out.println("");

            System.out.println("Testing contains() operation");
            System.out.println("    Contains indebted? " + st.contains("indebted"));
            System.out.println("    Contains mood? " + st.contains("mood"));
            System.out.println("");

            System.out.println("Testing keys iterator:");
            cnt = 0;
            for(String str : st.keys())
            {
                System.out.println("    " + str);
                if(++cnt >= 5) break;
            }
            System.out.println("");           
        }
    }

}
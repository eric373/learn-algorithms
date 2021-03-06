import java.util.Arrays;
import edu.princeton.cs.algs4.In;

public class BinarySearchSmallestIndex
{
    private int[] array;

    public BinarySearchSmallestIndex(int[] a)
    {
        array = a;
        Arrays.sort(array);
    }

    public int search(int t, int[] a)
    {
        array = a;
        Arrays.sort(array);
        return search(t);
    }

    public int search(int t)
    {
        int lo = 0;
        int hi = array.length - 1;
        int mid = (int)((lo + hi)/2);
        while(lo <= hi)
        {
            /*System.out.printf("%-5d %-5d %-5d %-5d %-5d %-5d\n"
                ,lo, mid, hi, array[lo], array[mid], array[hi]);
            */
            if(t < array[mid]) hi = mid - 1;
            else if(t > array[mid]) lo = mid + 1;
            else if(t == array[mid] && mid > 0 && t == array[mid - 1])
            {
                hi = mid - 1;
            }
            else
            {
                return mid;
            }
            mid = (int)((lo + hi)/2);
        }
        return -1;
    }

    public static void main(String[] args)    
    {
        int[] a = In.readInts(args[0]);
        BinarySearchSmallestIndex bs = new BinarySearchSmallestIndex(a);
        for(int n = 1; n < args.length; n++)
        {
            int t = Integer.parseInt(args[n]);
            int ret = bs.search(t, a);
            if(ret >= 0)
                System.out.printf("%-5d found     ret = %-5d\n", t, ret);
            else
                System.out.printf("%-5d missing   ret = %-5d\n", t, ret);
        }
    }
}
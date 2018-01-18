package chap2;

/**
 * Author: baojianfeng
 * Date: 2017-12-30
 */
public class BinarySearch<T extends Comparable<? super T>> {

    public int binarySearch(T[] arr, T value) {
        int low = 0;
        int high = arr.length - 1;

        // use center + 1 and center -1, means center is not needed in the next iteration
        while (low <= high) {
            int center = (low + high) / 2;
            if (arr[center].compareTo(value) < 0)
                low = center + 1;
            else if (arr[center].compareTo(value) > 0)
                high = center - 1;
            else
                return center;
        }

        return -1; // not exist;
    }
}

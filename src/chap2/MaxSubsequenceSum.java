package chap2;

/**
 * Author: baojianfeng
 * Date: 2017-12-29
 * Origin: CS5343 chapter 2 P28 PPT
 */
public class MaxSubsequenceSum {

    /**
     * this solution is O(N^3), brute force
     * @param arr int arr
     * @return the max sum
     */
    public int n3MaxSubSum(int[] arr) {
        int sum = Integer.MIN_VALUE;
        int arrLength = arr.length;

        for (int i = 0; i < arrLength; i++) {
            for (int j = i; j < arrLength; j++) {
                int curSum = 0;

                for (int k = i; k <= j; k++) {
                    curSum += arr[k];
                }

                if (curSum > sum)
                    sum = curSum;
            }
        }

        return sum;
    }

    /**
     * this solution is O(N^2)
     * @param arr int arr
     * @return the max sum
     */
    public int n2MaxSubSum(int[] arr) {
        int sum = Integer.MIN_VALUE;
        int arrLength = arr.length;
        int curSum = 0;

        for (int i = 0; i < arrLength; i++) {
            for (int j = i; j < arrLength; j++) {
                curSum += arr[j];

                if (curSum > sum)
                    sum = curSum;
            }

            curSum = 0;
        }

        return sum;
    }

    /**
     * this solution is O(nlogn),
     * it is a little different from the solution in the book, it puts negative sum into consideration.
     * @param arr int arr
     * @return the max sum
     */
    public static int nlognMaxSubSum(int[] arr) {
        return nlognMaxSum(arr, 0, arr.length - 1);
    }

    private static int nlognMaxSum(int[] arr, int left, int right) {
        if (left == right) // base case, different from the case in the book
            return arr[left];

        int center = (left + right) / 2;
        // max left sum & max right sum
        int maxLeftSum = nlognMaxSum(arr, left, center);
        int maxRightSum = nlognMaxSum(arr, center + 1, right);

        // max left boarder sum, start from center to left
        int maxLeftBoarderSum = Integer.MIN_VALUE;
        int curLeftSum = 0;
        for (int i = center; i >= left; i--) {
            curLeftSum += arr[i];

            if (curLeftSum > maxLeftBoarderSum)
                maxLeftBoarderSum = curLeftSum;
        }

        // max right boarder sum, start from center + 1 to right
        int maxRightBoarderSum = Integer.MIN_VALUE;
        int curRightSum = 0;
        for (int i = center + 1; i <= right; i++) {
            curRightSum += arr[i];

            if (curRightSum > maxRightBoarderSum)
                maxRightBoarderSum = curRightSum;
        }

        return max3(maxLeftSum, maxRightSum, maxLeftBoarderSum + maxRightBoarderSum);
    }

    /**
     * find the largest value in three integers
     * @param v1 value 1
     * @param v2 value 2
     * @param v3 value 3
     * @return max value
     */
    private static int max3(int v1, int v2, int v3) {
        int max = Integer.MIN_VALUE;

        if (v1 > max)
            max = v1;
        if (v2 > max)
            max = v2;
        if (v3 > max)
            max = v3;

        return max;
    }

    /**
     * this solution is O(n)
     * it is a little different from the solution in the book, since it also put the negative sum situation into consideration.
     * @param arr int arr
     * @return the max sum
     */
    public int nMaxSubSum(int[] arr) {
        int sum = Integer.MIN_VALUE; // this is different from the book, in the book sum = 0 as the initial value
        int curSum = 0;

        for (int i = 0; i < arr.length; i++) {
            curSum += arr[i];

            if (curSum > sum)
                sum = curSum;

            // when current sum goes negative, start a new sum
            if (curSum < 0)
                curSum = 0;
        }

        return sum;
    }

    public static void main(String[] args) {
        int[] arr = new int[]{13, -12, 22, 17, -13, 10, 1, 16};

        System.out.println("max sum is: " + nlognMaxSubSum(arr));
    }
}

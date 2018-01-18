package chap2;

/**
 * Author: baojianfeng
 * Date: 2017-12-30
 * Usage: compute the exponential value
 */
public class PowFunction {

    public static long pow(long x, long n) {
        if (n == 0) // only for when n is initially 0
            return 1;
        else if (n == 1) // base case
            return x;
        else if (n % 2 == 0)
            return pow(x * x, n / 2);
        else
            return pow(x * x, n / 2) * x;
    }

    public static void main(String[] args) {
        System.out.println("exponential value is: " + pow(2, 12));
    }
}

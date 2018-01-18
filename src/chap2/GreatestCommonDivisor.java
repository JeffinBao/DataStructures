package chap2;

/**
 * Author: baojianfeng
 * Date: 2017-12-30
 * Origin: P50 chap2 PPT
 */
public class GreatestCommonDivisor {

    /**
     * find the greatest common divisor, a must be greater than or equal to b
     * @param a a
     * @param b b
     * @return the greatest common divisor
     */
    public static long gcd(long a, long b) {
        while (b != 0) {
            long remainder = a % b;
            a = b;
            b = remainder;
        }

        return a;
    }
}

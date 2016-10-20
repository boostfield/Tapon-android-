package com.caoyang.tapon.Utils;

import java.util.Random;

/**
 * 随机数的产生
 */
public class RandomUtils {


    public static int nextInt(int lower, int upper) {
        Random ra = new Random();
        return (lower + ra.nextInt(upper - lower + 1));
    }

    public static long nextLong(int lower, int upper) {
        Random ra = new Random();
        int n = lower + ra.nextInt(upper - lower);
        long m = ra.nextLong();
        return n + m;
    }

    public static float nextFloat(int lower, int upper) {
        Random ra = new Random();
        int n = lower + ra.nextInt(upper - lower);
        float m = ra.nextFloat();
        return n + m;
    }

    public static double nextDouble(int lower, int upper) {
        Random ra = new Random();
        int n = lower + ra.nextInt(upper - lower);
        double m = ra.nextDouble();
        return n + m;
    }
}

package com.fantasy.sylvanas.core;

import org.junit.Test;

/**
 * Created by jiaji on 2017/8/24.
 */
public class CountBoltTest {
    static int a[] = new int[31270];
    static int s[] = new int[31270];


    static void init() {
        int i;
        a[1] = 1;
        s[1] = 1;
        for (i = 2; i < 31270; i++) {
            a[i] = a[i - 1] + (int) Math.log10((double) i) + 1;
            s[i] = s[i - 1] + a[i];
        }
    }

    static int Get(int n) {
        int i = 1;
        int length = 0;
        while (s[i] < n) i++;
        int pos = n - s[i - 1];
        return pos;
    }
    @Test
    public void test(){
        init();
        System.out.println(Get(3));
    }


    public static void main() {
        init();
        System.out.println(Get(10));
    }
}
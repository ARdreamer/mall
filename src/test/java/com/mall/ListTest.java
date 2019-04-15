package com.mall;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListTest {
    public static void main(String[] args) {
        List<Integer> a = new ArrayList();
        int[] c = new int[10];
        for (int i = 0; i < 10; i++) {
            a.add(i);
            c[i] = i;
        }
//        Object[] b = Arrays.copyOf(a.toArray(), 100);
//        for (int i = 0; i < b.length; i++) {
//            System.out.println(b[i]);
//        }

        int[] d = c.clone();
        for (int i = 0; i < d.length; i++) {
            System.out.println(d[i]);
        }
//        int[] d = Arrays.copyOf(c, 10);
        d[5] = 100;
        System.out.println(c[5]);
    }
}

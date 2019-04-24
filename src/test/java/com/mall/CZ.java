package com.mall;

import java.util.List;

public class CZ extends Parent {
    float id(float i) {
        System.out.println("111");
        return i;
    }

    double id(double i, float b) {
        return i;
    }

    double id(float b, double i) {
        return i;
    }

    //    void id(int i) {
//
//    }


    int id(int i) {
        return i;
    }
}

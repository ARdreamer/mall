package com.mall.util;

import java.math.BigDecimal;

public class BigDecimalUtil {
    private BigDecimalUtil() {

    }

    public static BigDecimal add(double x, double y) {
        BigDecimal b1 = new BigDecimal(Double.toString(x));
        BigDecimal b2 = new BigDecimal(Double.toString(y));
        return b1.add(b2);
    }

    public static BigDecimal sub(double x, double y) {
        BigDecimal b1 = new BigDecimal(Double.toString(x));
        BigDecimal b2 = new BigDecimal(Double.toString(y));
        return b1.subtract(b2);
    }

    public static BigDecimal mul(double x, double y) {
        BigDecimal b1 = new BigDecimal(Double.toString(x));
        BigDecimal b2 = new BigDecimal(Double.toString(y));
        return b1.multiply(b2);
    }

    public static BigDecimal div(double x, double y) {
        BigDecimal b1 = new BigDecimal(Double.toString(x));
        BigDecimal b2 = new BigDecimal(Double.toString(y));
        return b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP);//四舍五入

    }
}

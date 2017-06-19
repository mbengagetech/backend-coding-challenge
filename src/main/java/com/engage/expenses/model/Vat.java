package com.engage.expenses.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Vat {

    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;
    private static int scale = 2;
    private static BigDecimal vatPercentage = BigDecimal.valueOf(20);
    private static BigDecimal hundred = BigDecimal.valueOf(100);

    public static BigDecimal vatFromTotal(BigDecimal total) {
        return total.setScale(scale, ROUNDING)
                .multiply(vatPercentage)
                .divide(hundred.add(vatPercentage), ROUNDING);
    }

}

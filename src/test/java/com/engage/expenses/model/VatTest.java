package com.engage.expenses.model;

import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class VatTest {

    @Test
    public void shouldCalculateVatFromTotal() {
        assertThat(Vat.vatFromTotal(BigDecimal.valueOf(12))).isEqualByComparingTo("2");
        assertThat(Vat.vatFromTotal(BigDecimal.valueOf(120))).isEqualByComparingTo("20");

        assertThat(Vat.vatFromTotal(BigDecimal.valueOf(10))).isEqualByComparingTo("1.67");
        assertThat(Vat.vatFromTotal(BigDecimal.valueOf(20))).isEqualByComparingTo("3.33");
    }

}
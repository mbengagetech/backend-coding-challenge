package com.engage.expenses.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

import static java.util.stream.Collectors.toList;

public enum Currency {

    GBP, AUD, BGN, BRL, CAD, CHF, CNY, CZK, DKK, HKD, HRK, HUF, IDR, ILS, INR, JPY,
    KRW, MXN, MYR, NOK, NZD, PHP, PLN, RON, RUB, SEK, SGD, THB, TRY, USD, ZAR, EUR;

    public static boolean isSupported(String currency) {
        return Arrays.stream(Currency.values())
                .map(Enum::toString).collect(toList())
                .contains(StringUtils.upperCase(currency));
    }
}

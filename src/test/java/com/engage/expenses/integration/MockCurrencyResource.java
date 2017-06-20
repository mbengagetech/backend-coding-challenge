package com.engage.expenses.integration;

import com.engage.expenses.model.Currency;
import com.engage.expenses.resource.CurrencyResource;
import io.vavr.control.Either;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class MockCurrencyResource implements CurrencyResource {

    @Override
    public Either<String, BigDecimal> convertCurrency(LocalDate date, BigDecimal amount, Currency startingCurrency, Currency targetCurrency) {
        BigDecimal euroRate = BigDecimal.valueOf(1.1426);
        return Either.right(amount.divide(euroRate, RoundingMode.HALF_UP));
    }
}

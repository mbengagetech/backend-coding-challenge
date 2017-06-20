package com.engage.expenses.service;

import com.engage.expenses.model.Currency;
import com.engage.expenses.resource.CurrencyResource;
import io.vavr.control.Either;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CurrencyResourceMock implements CurrencyResource {

    private boolean called = false;

    @Override
    public Either<String, BigDecimal> convertCurrency(LocalDate date, BigDecimal amount, Currency startingCurrency, Currency targetCurrency) {
        called = true;
        return Either.right(amount);
    }

    public boolean hasBeenCalled() {
        return called;
    }
}

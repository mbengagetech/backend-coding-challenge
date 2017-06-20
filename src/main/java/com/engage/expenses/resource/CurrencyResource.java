package com.engage.expenses.resource;

import com.engage.expenses.model.Currency;
import io.vavr.control.Either;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CurrencyResource {

    Either<String, BigDecimal> convertCurrency(LocalDate date, BigDecimal amount, Currency startingCurrency, Currency targetCurrency);

}

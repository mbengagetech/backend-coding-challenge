package com.engage.expenses.resource;

import com.engage.expenses.model.Currency;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class FixerCurrencyResource implements CurrencyResource {

    private static final String BASE_URL = "http://api.fixer.io/";
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    @Override
    public Either<String, BigDecimal> convertCurrency(LocalDate date, BigDecimal amount, Currency startingCurrency, Currency targetCurrency) {
        String dateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        JsonNode result;
        try {
            result = Unirest.get(BASE_URL + dateString).queryString("base", targetCurrency).asJson().getBody();
        } catch (UnirestException e) {
            log.error(e.getMessage(), e);
            return Either.left("Failed to convert amount, please retry later or use GBP");
        }

        if (!result.getObject().getJSONObject("rates").has(startingCurrency.toString())) {
            return Either.left("Currency " + startingCurrency + " not supported");
        } else {
            BigDecimal rate = result.getObject().getJSONObject("rates").getBigDecimal(startingCurrency.toString());
            return Either.right(amount.setScale(10, ROUNDING).divide(rate, ROUNDING));
        }
    }
}

package com.engage.expenses.model;

import com.engage.expenses.resource.CurrencyResource;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Validation;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.engage.expenses.model.Expense.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ExpenseTest {

    @Test
    public void shouldBeValid() {
        Expense expense = new Expense(LocalDate.now(), BigDecimal.valueOf(2), "reason", DEFAULT_CURRENCY);
        Validation<Seq<String>, Expense> validated = expense.validate();
        assertThat(validated.isValid()).isTrue();
    }

    @Test
    public void shouldBeInvalidWhenMandatoryFieldsAreNull() {
        Expense expense = new Expense(null, null, null, null);
        Validation<Seq<String>, Expense> validated = expense.validate();
        assertThat(validated.isInvalid()).isTrue();
        assertThat(validated.getError().asJava())
                .containsExactlyInAnyOrder(DATE_EMPTY, AMOUNT_EMPTY, REASON_EMPTY, CURRENCY_EMPTY);
    }

    @Test
    public void shouldBeInvalidWhenDateIsInTheFuture() {
        Expense expense = new Expense(LocalDate.now().plusDays(1), BigDecimal.valueOf(2), "reason", DEFAULT_CURRENCY);
        Validation<Seq<String>, Expense> validated = expense.validate();
        assertThat(validated.isInvalid()).isTrue();
        assertThat(validated.getError().asJava())
                .containsExactlyInAnyOrder(DATE_IN_FUTURE);
    }

    @Test
    public void shouldBeInvalidWhenAmountIsZero() {
        Expense expense = new Expense(LocalDate.now(), BigDecimal.ZERO, "reason", DEFAULT_CURRENCY);
        Validation<Seq<String>, Expense> validated = expense.validate();
        assertThat(validated.isInvalid()).isTrue();
        assertThat(validated.getError().asJava())
                .containsExactlyInAnyOrder(AMOUNT_ZERO_OR_NEGATIVE);
    }

    @Test
    public void shouldBeInvalidWhenAmountIsNegative() {
        Expense expense = new Expense(LocalDate.now(), BigDecimal.valueOf(-1), "reason", DEFAULT_CURRENCY);
        Validation<Seq<String>, Expense> validated = expense.validate();
        assertThat(validated.isInvalid()).isTrue();
        assertThat(validated.getError().asJava())
                .containsExactlyInAnyOrder(AMOUNT_ZERO_OR_NEGATIVE);
    }

    @Test
    public void shouldKeepTheAmountTheSameIfCurrencyIsAlreadyTheDefaultOne() {
        BigDecimal initialAmount = BigDecimal.TEN;
        Expense expense = Expense.builder().amount(initialAmount).currency(DEFAULT_CURRENCY).build();
        CurrencyResource currencyResourceAdding1 = (date, amount, startingCurrency, DEFAULT_CURRENCY) -> Either.right(amount.add(BigDecimal.ONE));
        expense.convertToDefaultCurrency(currencyResourceAdding1);
        assertThat(expense.getAmount()).isEqualByComparingTo(initialAmount);
    }

    @Test
    public void shouldConvertTheAmountToTheDefaultCurrency() {
        BigDecimal initialAmount = BigDecimal.TEN;
        Expense expense = Expense.builder().amount(initialAmount).currency(Currency.EUR).build();
        CurrencyResource currencyResourceAdding1 = (date, amount, startingCurrency, DEFAULT_CURRENCY) -> Either.right(amount.add(BigDecimal.ONE));
        expense.convertToDefaultCurrency(currencyResourceAdding1);
        assertThat(expense.getAmount()).isEqualByComparingTo(initialAmount.add(BigDecimal.ONE));
    }

    @Test
    public void shouldKeepTheAmountTheSameIfCurrencyConversionFails() {
        BigDecimal initialAmount = BigDecimal.TEN;
        Expense expense = Expense.builder().amount(initialAmount).currency(Currency.EUR).build();
        CurrencyResource failingCurrencyResource = (date, amount, startingCurrency, DEFAULT_CURRENCY) -> Either.left("error");
        expense.convertToDefaultCurrency(failingCurrencyResource);
        assertThat(expense.getAmount()).isEqualByComparingTo(initialAmount);
    }
}
package com.engage.expenses.model;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.engage.expenses.model.Expense.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ExpenseTest {

    @Test
    public void shouldBeValid() {
        Expense expense = new Expense(LocalDate.now(), BigDecimal.valueOf(2), "reason");
        Validation<Seq<String>, Expense> validated = expense.validate();
        assertThat(validated.isValid()).isTrue();
    }

    @Test
    public void shouldBeInvalidWhenMandatoryFieldsAreNull() {
        Expense expense = new Expense(null, null, null);
        Validation<Seq<String>, Expense> validated = expense.validate();
        assertThat(validated.isInvalid()).isTrue();
        assertThat(validated.getError().asJava())
                .containsExactlyInAnyOrder(DATE_EMPTY, AMOUNT_EMPTY, REASON_EMPTY);
    }

    @Test
    public void shouldBeInvalidWhenDateIsInTheFuture() {
        Expense expense = new Expense(LocalDate.now().plusDays(1), BigDecimal.valueOf(2), "reason");
        Validation<Seq<String>, Expense> validated = expense.validate();
        assertThat(validated.isInvalid()).isTrue();
        assertThat(validated.getError().asJava())
                .containsExactlyInAnyOrder(DATE_IN_FUTURE);
    }

    @Test
    public void shouldBeInvalidWhenAmountIsZero() {
        Expense expense = new Expense(LocalDate.now(), BigDecimal.ZERO, "reason");
        Validation<Seq<String>, Expense> validated = expense.validate();
        assertThat(validated.isInvalid()).isTrue();
        assertThat(validated.getError().asJava())
                .containsExactlyInAnyOrder(AMOUNT_ZERO_OR_NEGATIVE);
    }

    @Test
    public void shouldBeInvalidWhenAmountIsNegative() {
        Expense expense = new Expense(LocalDate.now(), BigDecimal.valueOf(-1), "reason");
        Validation<Seq<String>, Expense> validated = expense.validate();
        assertThat(validated.isInvalid()).isTrue();
        assertThat(validated.getError().asJava())
                .containsExactlyInAnyOrder(AMOUNT_ZERO_OR_NEGATIVE);
    }

}
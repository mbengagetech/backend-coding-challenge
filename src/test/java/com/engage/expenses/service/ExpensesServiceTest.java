package com.engage.expenses.service;

import com.engage.expenses.model.Expense;
import com.engage.shared.exception.ValidationException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ExpensesServiceTest {

    private ExpensesService service;
    private ExpensesRepositoryMock repositoryMock = new ExpensesRepositoryMock();
    private CurrencyResourceMock currencyResourceMock = new CurrencyResourceMock();

    @Before
    public void setUp() {
        service = new ExpensesService(repositoryMock, currencyResourceMock);
    }

    @Test
    public void shouldSaveExpenseWhenValid() {
        Expense validExpense = new Expense(LocalDate.now(), BigDecimal.TEN, "reason", Expense.DEFAULT_CURRENCY);
        Expense expense = service.addExpense(validExpense);

        assertThat(repositoryMock.hasSaved(expense)).isTrue();
    }

    @Test
    public void shouldNotCallCurrencyConverterAndNotSaveExpenseWhenInvalid() {
        Expense invalidExpense = new Expense(null, null, "", null);

        assertThatExceptionOfType(ValidationException.class).isThrownBy(() -> service.addExpense(invalidExpense));
        assertThat(currencyResourceMock.hasBeenCalled()).isFalse();
        assertThat(repositoryMock.hasBeenCalled()).isFalse();
    }
}
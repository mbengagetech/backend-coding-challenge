package com.engage.expenses.service;

import com.engage.expenses.model.Expense;
import com.engage.expenses.repository.ExpensesRepository;
import com.engage.expenses.resource.CurrencyResource;
import com.engage.shared.exception.ValidationException;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Validation;

public class ExpensesService {

    private ExpensesRepository repository;
    private CurrencyResource currencyResource;

    public ExpensesService(ExpensesRepository repository, CurrencyResource currencyResource) {
        this.repository = repository;
        this.currencyResource = currencyResource;
    }

    public Expense addExpense(Expense newExpense) {
        Validation<Seq<String>, Expense> validatedExpense = newExpense.validate();
        Either<Seq<String>, Expense> expenseInDefaultCurrency = validatedExpense.toEither().flatMap(exp -> exp.convertToDefaultCurrency(currencyResource));

        Either<Seq<String>, Expense> savedExpense = expenseInDefaultCurrency.map(expense -> {
            repository.saveExpense(expense);
            return expense;
        });

        return savedExpense.getOrElseThrow(ValidationException::new);
    }
}

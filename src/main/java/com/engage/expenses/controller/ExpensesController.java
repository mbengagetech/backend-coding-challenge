package com.engage.expenses.controller;

import com.engage.expenses.api.Path;
import com.engage.expenses.model.Expense;
import com.engage.expenses.repository.ExpensesRepository;
import com.engage.expenses.resource.CurrencyResource;
import com.engage.shared.JsonMapper;
import com.engage.shared.exception.ValidationException;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Validation;

import static com.engage.expenses.Main.APPLICATION_JSON;
import static spark.Spark.get;
import static spark.Spark.post;

public class ExpensesController {

    public ExpensesController(JsonMapper jsonMapper, ExpensesRepository repository, CurrencyResource currencyResource) {

        get(Path.EXPENSES, APPLICATION_JSON, (req, res) -> repository.findAll(), jsonMapper::toJson);

        post(Path.EXPENSES, APPLICATION_JSON, (req, res) -> {
            Expense expenseDto = jsonMapper.fromJson(req.body(), Expense.class);
            Validation<Seq<String>, Expense> validatedExpense = expenseDto.validate();
            Either<Seq<String>, Expense> expenseInGBP = validatedExpense.toEither().flatMap(exp -> exp.convertToDefaultCurrency(currencyResource));

            Either<Seq<String>, Expense> result = expenseInGBP.map(expense -> {
                repository.saveExpense(expense);
                return expense;
            });

            if (result.isRight()) {
                return result.get();
            } else {
                throw new ValidationException(result.getLeft());
            }
        }, jsonMapper::toJson);
    }


}

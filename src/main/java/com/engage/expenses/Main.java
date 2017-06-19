package com.engage.expenses;

import com.engage.expenses.controller.ExpensesController;
import com.engage.expenses.repository.ExpensesRepository;
import com.engage.shared.JsonMapper;
import com.engage.shared.exception.ValidationException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import static spark.Spark.*;

public class Main {

    public static final String APPLICATION_JSON = "application/json";
    public static final int PORT = 8080;
    private static JsonMapper jsonMapper;

    public static void main(String[] args) {
        jsonMapper = new JsonMapper();

        externalStaticFileLocation("static");
        port(PORT);

        new Main(
                new ExpensesController(
                        jsonMapper,
                        new ExpensesRepository()
                ));
    }

    public Main(ExpensesController expensesController) {
        after((req, res) -> res.type("application/json"));

        exception(ValidationException.class, (exception, req, res) -> {
            res.status(400);
            res.type(APPLICATION_JSON);
            res.body(jsonMapper.toJson(exception.errors()));
        });
        exception(InvalidFormatException.class,  (exception, req, res) -> {
            res.status(400);
            res.type(APPLICATION_JSON);
            res.body(jsonMapper.toJson(exception.getMessage()));
        });
    }
}

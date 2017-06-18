package com.engage.expenses;

import com.engage.expenses.controller.ExpensesController;
import com.engage.expenses.repository.ExpensesRepository;
import com.engage.shared.JsonMapper;

import static spark.Spark.*;

public class Main {

    public static final String APPLICATION_JSON = "application/json";
    public static final int PORT = 8080;

    public static void main(String[] args) {
        JsonMapper jsonMapper = new JsonMapper();

        externalStaticFileLocation("static");
        port(PORT);

        new Main(
                new ExpensesController(
                        jsonMapper,
                        new ExpensesRepository()
                ));
    }

    public Main(ExpensesController expensesController) {
        after((req, res) -> {
            res.type("application/json");
        });
    }
}

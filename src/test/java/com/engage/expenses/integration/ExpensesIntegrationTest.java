package com.engage.expenses.integration;

import com.engage.expenses.Main;
import com.engage.expenses.api.Path;
import com.engage.expenses.controller.ExpensesController;
import com.engage.expenses.model.Expense;
import com.engage.expenses.repository.ExpensesRepository;
import com.engage.shared.Hibernate;
import com.engage.shared.JsonMapper;
import com.google.common.collect.ImmutableMap;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.*;
import spark.Spark;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.engage.expenses.Main.PORT;
import static org.assertj.core.api.Assertions.assertThat;
import static spark.Spark.port;

public class ExpensesIntegrationTest {

    private static JsonMapper jsonMapper;

    @BeforeClass
    public static void setUpTests() {
        jsonMapper = new JsonMapper();
        port(PORT);

        new Main(
                new ExpensesController(
                        jsonMapper,
                        new ExpensesRepository()
                )
        );
    }

    @AfterClass
    public static void tearDownTests() throws IOException {
        Spark.stop();
        Unirest.shutdown();
    }

    @Before
    public void setUp() {
        Hibernate.runNativeQuery("select setval('expenses_id_seq', 1)");
    }

    @After
    public void tearDown() {
        Hibernate.runNativeQuery("truncate expenses");
    }

    @Test
    public void shouldGetExpenses() throws UnirestException {

        Hibernate.runWithSession(session -> {
            session.persist(
                    new Expense(LocalDate.of(2017, 6, 18),
                            BigDecimal.valueOf(12), "Why not?"));
            session.flush();
            session.clear();
        });

        HttpResponse<JsonNode> res = Unirest.get("http://localhost:" + PORT + Path.EXPENSES).asJson();

        assertThat(res.getStatus()).isEqualTo(200);
        assertThat(res.getBody().isArray()).isTrue();
        JSONArray expensesJson = res.getBody().getArray();
        assertThat(expensesJson.length()).isEqualTo(1);

        JSONObject expected = new JSONObject(ImmutableMap.builder()
                .put("id", 2)
                .put("date", "18/6/2017")
                .put("amount", 12.0)
                .put("vat", 2.0)
                .put("reason", "Why not?").build());

        JSONObject actual = expensesJson.getJSONObject(0);

        assertThat(actual.similar(expected)).isTrue();
    }

    @Test
    public void shouldPostExpense() throws UnirestException {
        JSONObject requestBody = new JSONObject(ImmutableMap.builder()
                .put("date", "18/06/2017")
                .put("amount", "10.03")
                .put("reason", "reason")
                .build());
        HttpResponse<JsonNode> res = Unirest.post("http://localhost:" + PORT + Path.EXPENSES)
                .body(requestBody.toString()).asJson();

        assertThat(res.getStatus()).isEqualTo(200);
        JSONObject expenseJson = res.getBody().getObject();

        String expectedJson = jsonMapper.toJson(new Expense(2L, LocalDate.now(), BigDecimal.valueOf(8.2), "Reason"));
        assertThat(expenseJson.similar(new JSONObject(expectedJson)));

        List<Expense> expenses = Hibernate.fetchWithSession(session ->
                session.createQuery("from Expense where id = 2").getResultList());

        Expense expected = new Expense(2L, LocalDate.now(), BigDecimal.valueOf(8.2), "Reason");
        assertThat(expenses).containsExactlyInAnyOrder(expected);
    }

}

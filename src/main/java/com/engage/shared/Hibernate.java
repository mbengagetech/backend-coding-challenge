package com.engage.shared;

import com.engage.expenses.model.Expense;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.PostgreSQL95Dialect;

import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class Hibernate {

    private static final SessionFactory sessionFactory = createSessionFactory();

    private static SessionFactory createSessionFactory() {
        Configuration configuration = configuration();
        String url = configuration.getProperty(AvailableSettings.URL);
        configuration.setProperty(AvailableSettings.URL, url);
        configuration.setProperty(AvailableSettings.DIALECT, PostgreSQL95Dialect.class.getName());
        configuration.setProperty(AvailableSettings.USE_QUERY_CACHE, "false");
        configuration.setProperty(AvailableSettings.SHOW_SQL, "false");
        configuration.setProperty(AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        configuration.addAnnotatedClass(Expense.class);
        StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        return configuration.buildSessionFactory(serviceRegistryBuilder.build());
    }

    private static Configuration configuration() {
        Configuration configuration = new Configuration();
        configuration.configure("/hibernate-local.cfg.xml");
        return configuration;
    }

    public static void runWithSession(Consumer<Session> workInTransaction) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            workInTransaction.accept(session);
            transaction.commit();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public static <T> T fetchWithSession(Function<Session, T> workInTransaction) {
        try (Session session = sessionFactory.openSession()) {
            T result = workInTransaction.apply(session);
            return result;
        }
    }

    public static void runNativeQuery(String query) {
        runWithSession(session -> session.createNativeQuery(query).executeUpdate());
    }
}

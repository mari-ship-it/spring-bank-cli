package sirenko.mar.bank.config;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.*;
import sirenko.mar.bank.entity.Account;
import sirenko.mar.bank.entity.User;

import java.util.Scanner;

@Configuration
@ComponentScan("sirenko.mar.bank")
@PropertySource("classpath:application.properties")
public class HibernateConfiguration {

    @Bean
    public SessionFactory sessionFactory() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();

        configuration
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Account.class)
                .addPackage("sirenko.mar.bank")
                .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
                .setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/postgres")
                .setProperty("hibernate.connection.username", "postgres")
                .setProperty("hibernate.connection.password", "123")
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.hbm2ddl.auto", "update");

        return configuration.buildSessionFactory();
    }

    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);

    }
}

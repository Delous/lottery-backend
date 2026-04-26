package ru.onexteam.lottery.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

public class DbConfig {

    private static final Properties properties = new Properties();

    static {
        try (var input = DbConfig.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {

            if (input == null) {
                throw new RuntimeException("Файл application.properties не найден");
            }

            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить настройки подключения к БД", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.username"),
                properties.getProperty("db.password")
        );
    }

    public static void initialize() {
        var statements = List.of(
                """
                create table if not exists users (
                    id bigserial primary key,
                    email varchar(255) not null unique,
                    password varchar(255) not null,
                    role varchar(32) not null,
                    created_at timestamp not null default current_timestamp
                )
                """,
                """
                create table if not exists draws (
                    id bigserial primary key,
                    title varchar(255) not null,
                    status varchar(32) not null,
                    created_at timestamp not null default current_timestamp,
                    finished_at timestamp null
                )
                """,
                """
                create table if not exists draw_results (
                    id bigserial primary key,
                    draw_id bigint not null unique references draws(id) on delete cascade,
                    winning_combination varchar(64) not null,
                    created_at timestamp not null default current_timestamp
                )
                """,
                """
                create table if not exists tickets (
                    id bigserial primary key,
                    user_id bigint not null references users(id) on delete cascade,
                    draw_id bigint not null references draws(id) on delete cascade,
                    combination varchar(64) not null,
                    status varchar(32) not null,
                    created_at timestamp not null default current_timestamp
                )
                """,
                "create index if not exists idx_draws_status on draws(status)",
                "create index if not exists idx_tickets_draw_id on tickets(draw_id)",
                "create index if not exists idx_tickets_user_id on tickets(user_id)"
        );

        try (var connection = getConnection();
             var statement = connection.createStatement()) {
            for (var sql : statements) {
                statement.execute(sql);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось инициализировать схему базы данных", e);
        }
    }
}

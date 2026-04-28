package ru.onexteam.lottery.repository;

import ru.onexteam.lottery.config.DbConfig;
import ru.onexteam.lottery.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class UserRepository extends BaseRepository {

    private static final String INSERT_SQL = "insert into users (email, password, role) values (?, ?, ?)";
    private static final String FIND_BY_ID_SQL = "select id, email, password, role from users where id = ?";
    private static final String FIND_BY_EMAIL_SQL = "select id, email, password, role from users where email = ?";

    public void save(User user) {
        try (var connection = DbConfig.getConnection();
             var statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.email);
            statement.setString(2, user.password);
            statement.setString(3, user.role);
            statement.executeUpdate();

            try (var generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.id = generatedKeys.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось сохранить пользователя", e);
        }
    }

    public Optional<User> findById(Long id) {
        try (var connection = DbConfig.getConnection();
             var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            statement.setLong(1, id);
            return findOne(statement, this::mapRow);
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось найти пользователя по id", e);
        }
    }

    public Optional<User> findByEmail(String email) {
        try (var connection = DbConfig.getConnection();
             var statement = connection.prepareStatement(FIND_BY_EMAIL_SQL)) {

            statement.setString(1, email);
            return findOne(statement, this::mapRow);
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось найти пользователя по email", e);
        }
    }

    private User mapRow(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getLong("id"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getString("role")
        );
    }
}

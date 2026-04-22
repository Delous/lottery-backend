package ru.onexteam.lottery.repository;

import ru.onexteam.lottery.config.DbConfig;
import ru.onexteam.lottery.model.User;

import java.sql.*;
import java.util.Optional;

public class UserRepository {

    public void save(User user) {
        String sql = "insert into users (email, password, role) values (?, ?, ?)";

        try (Connection connection = DbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.email);
            statement.setString(2, user.password);
            statement.setString(3, user.role);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save user", e);
        }
    }

    public Optional<User> findByEmail(String email) {
        String sql = "select id, email, password, role from users where email = ?";

        try (Connection connection = DbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.id = rs.getLong("id");
                    user.email = rs.getString("email");
                    user.password = rs.getString("password");
                    user.role = rs.getString("role");

                    return Optional.of(user);
                }
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user by email", e);
        }
    }
}
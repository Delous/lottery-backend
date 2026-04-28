package ru.onexteam.lottery.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class BaseRepository {

    @FunctionalInterface
    protected interface RowMapper<T> {
        T map(ResultSet resultSet) throws SQLException;
    }

    protected <T> Optional<T> findOne(PreparedStatement statement, RowMapper<T> mapper) throws SQLException {
        try (var resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return Optional.of(mapper.map(resultSet));
            }

            return Optional.empty();
        }
    }

    protected <T> List<T> findMany(PreparedStatement statement, RowMapper<T> mapper) throws SQLException {
        var result = new ArrayList<T>();

        try (var resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                result.add(mapper.map(resultSet));
            }
        }

        return result;
    }
}

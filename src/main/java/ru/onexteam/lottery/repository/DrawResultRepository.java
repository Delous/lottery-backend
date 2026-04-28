package ru.onexteam.lottery.repository;

import ru.onexteam.lottery.config.DbConfig;
import ru.onexteam.lottery.model.DrawResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class DrawResultRepository extends BaseRepository {

    private static final String INSERT_SQL = "insert into draw_results (draw_id, winning_combination) values (?, ?)";
    private static final String FIND_BY_DRAW_ID_SQL = "select id, draw_id, winning_combination from draw_results where draw_id = ?";

    public DrawResult save(DrawResult drawResult) {
        try (var connection = DbConfig.getConnection();
             var statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, drawResult.drawId);
            statement.setString(2, drawResult.winningCombination);
            statement.executeUpdate();

            try (var generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    drawResult.id = generatedKeys.getLong(1);
                }
            }

            return drawResult;
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось сохранить результат тиража", e);
        }
    }

    public Optional<DrawResult> findByDrawId(Long drawId) {
        try (var connection = DbConfig.getConnection();
             var statement = connection.prepareStatement(FIND_BY_DRAW_ID_SQL)) {

            statement.setLong(1, drawId);
            return findOne(statement, this::mapRow);
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось найти результат тиража по id тиража", e);
        }
    }

    private DrawResult mapRow(ResultSet resultSet) throws SQLException {
        return new DrawResult(
                resultSet.getLong("id"),
                resultSet.getLong("draw_id"),
                resultSet.getString("winning_combination")
        );
    }
}

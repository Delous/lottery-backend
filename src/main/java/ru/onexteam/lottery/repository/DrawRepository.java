package ru.onexteam.lottery.repository;

import ru.onexteam.lottery.config.DbConfig;
import ru.onexteam.lottery.model.Draw;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class DrawRepository extends BaseRepository {

    private static final String INSERT_SQL = "insert into draws (title, status) values (?, ?)";
    private static final String FIND_BY_ID_SQL = "select id, title, status from draws where id = ?";
    private static final String FIND_BY_STATUS_SQL = "select id, title, status from draws where status = ? order by id desc";
    private static final String UPDATE_SQL = """
            update draws
            set title = ?,
                status = ?,
                finished_at = case when ? = 'FINISHED' then current_timestamp else finished_at end
            where id = ?
            """;

    public Draw save(Draw draw) {
        try (var connection = DbConfig.getConnection();
             var statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, draw.title);
            statement.setString(2, draw.status);
            statement.executeUpdate();

            try (var generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    draw.id = generatedKeys.getLong(1);
                }
            }

            return draw;
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось сохранить тираж", e);
        }
    }

    public Optional<Draw> findById(Long id) {
        try (var connection = DbConfig.getConnection();
             var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            statement.setLong(1, id);
            return findOne(statement, this::mapRow);
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось найти тираж по id", e);
        }
    }

    public List<Draw> findByStatus(String status) {
        try (var connection = DbConfig.getConnection();
             var statement = connection.prepareStatement(FIND_BY_STATUS_SQL)) {
            statement.setString(1, status);
            return findMany(statement, this::mapRow);
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось загрузить тиражи по статусу", e);
        }
    }

    public void update(Draw draw) {
        try (var connection = DbConfig.getConnection();
             var statement = connection.prepareStatement(UPDATE_SQL)) {

            statement.setString(1, draw.title);
            statement.setString(2, draw.status);
            statement.setString(3, draw.status);
            statement.setLong(4, draw.id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось обновить тираж", e);
        }
    }

    private Draw mapRow(ResultSet resultSet) throws SQLException {
        return new Draw(
                resultSet.getLong("id"),
                resultSet.getString("title"),
                resultSet.getString("status")
        );
    }
}

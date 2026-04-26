package ru.onexteam.lottery.repository;

import ru.onexteam.lottery.config.DbConfig;
import ru.onexteam.lottery.model.Ticket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class TicketRepository extends BaseRepository {

    private static final String INSERT_SQL = "insert into tickets (user_id, draw_id, combination, status) values (?, ?, ?, ?)";
    private static final String FIND_BY_ID_SQL = "select id, user_id, draw_id, combination, status from tickets where id = ?";
    private static final String FIND_BY_STATUS_SQL = "select id, user_id, draw_id, combination, status from tickets where draw_id = ? order by id desc";
    private static final String UPDATE_SQL = """
            update tickets
            set user_id = ?,
                draw_id = ?,
                combination = ?,
                status = ?
            where id = ?
            """;

    public Ticket save(Ticket ticket) {
        try (var connection = DbConfig.getConnection();
             var statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, ticket.userId);
            statement.setLong(2, ticket.drawId);
            statement.setString(3, ticket.combination);
            statement.setString(4, ticket.status);
            statement.executeUpdate();

            try (var generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ticket.id = generatedKeys.getLong(1);
                }
            }

            return ticket;
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось сохранить билет", e);
        }
    }

    public void update(Ticket ticket) {
        try (var connection = DbConfig.getConnection();
             var statement = connection.prepareStatement(UPDATE_SQL)) {

            statement.setLong(1, ticket.userId);
            statement.setLong(2, ticket.drawId);
            statement.setString(3, ticket.combination);
            statement.setString(4, ticket.status);
            statement.setLong(5, ticket.id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось обновить билет", e);
        }
    }

    public Optional<Ticket> findById(Long id) {
        try (var connection = DbConfig.getConnection();
             var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            statement.setLong(1, id);
            return findOne(statement, this::mapRow);
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось найти билет по id", e);
        }
    }

    public List<Ticket> findByStatus(Long drawId) {
        try (var connection = DbConfig.getConnection();
             var statement = connection.prepareStatement(FIND_BY_STATUS_SQL)) {

            statement.setLong(1, drawId);
            return findMany(statement, this::mapRow);
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось найти билеты по id тиража", e);
        }
    }

    private Ticket mapRow(ResultSet resultSet) throws SQLException {
        return new Ticket(
                resultSet.getLong("id"),
                resultSet.getLong("user_id"),
                resultSet.getLong("draw_id"),
                resultSet.getString("combination"),
                resultSet.getString("status")
        );
    }
}

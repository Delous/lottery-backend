package ru.onexteam.lottery.repository;

import ru.onexteam.lottery.config.DbConfig;
import ru.onexteam.lottery.model.Ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class TicketRepository extends BaseRepository {

    private static final String INSERT_SQL = "insert into tickets (user_id, draw_id, combination, status) values (?, ?, ?, ?)";
    private static final String FIND_BY_ID_SQL = "select id, user_id, draw_id, combination, status from tickets where id = ?";
    private static final String FIND_BY_USER_ID_SQL = "select id, user_id, draw_id, combination, status from tickets where user_id = ? order by id desc";
    private static final String FIND_BY_DRAW_ID_SQL = "select id, user_id, draw_id, combination, status from tickets where draw_id = ? order by id desc";
    private static final String FIND_BY_DRAW_ID_AND_USER_ID_SQL = """
            select id, user_id, draw_id, combination, status
            from tickets
            where draw_id = ? and user_id = ?
            order by id desc
            """;
    private static final String UPDATE_STATUSES_FOR_DRAW_SQL = """
            update tickets
            set status = case
                when combination = ? then 'WIN'
                else 'LOSE'
            end
            where draw_id = ?
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

    public Optional<Ticket> findById(Long id) {
        try (var connection = DbConfig.getConnection();
             var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            statement.setLong(1, id);
            return findOne(statement, this::mapRow);
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось найти билет по id", e);
        }
    }

    public List<Ticket> findByUserId(Long userId) {
        try (var connection = DbConfig.getConnection();
             var statement = connection.prepareStatement(FIND_BY_USER_ID_SQL)) {

            statement.setLong(1, userId);
            return findMany(statement, this::mapRow);
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось найти билеты пользователя", e);
        }
    }

    public List<Ticket> findByDrawId(Long drawId) {
        try (var connection = DbConfig.getConnection();
             var statement = connection.prepareStatement(FIND_BY_DRAW_ID_SQL)) {

            statement.setLong(1, drawId);
            return findMany(statement, this::mapRow);
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось найти билеты по id тиража", e);
        }
    }

    public List<Ticket> findByDrawIdAndUserId(Long drawId, Long userId) {
        try (var connection = DbConfig.getConnection();
             var statement = connection.prepareStatement(FIND_BY_DRAW_ID_AND_USER_ID_SQL)) {

            statement.setLong(1, drawId);
            statement.setLong(2, userId);
            return findMany(statement, this::mapRow);
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось найти билеты пользователя в этом тираже", e);
        }
    }

    public void updateStatusesForDraw(Long drawId, String winningCombination) {
        try (var connection = DbConfig.getConnection();
             var statement = connection.prepareStatement(UPDATE_STATUSES_FOR_DRAW_SQL)) {

            statement.setString(1, winningCombination);
            statement.setLong(2, drawId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось обновить статусы билетов для тиража", e);
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

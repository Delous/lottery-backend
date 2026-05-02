package ru.onexteam.lottery.service;

import ru.onexteam.lottery.model.Draw;
import ru.onexteam.lottery.model.Ticket;
import ru.onexteam.lottery.repository.DrawRepository;
import ru.onexteam.lottery.repository.TicketRepository;

import java.util.List;
import java.util.Optional;

public class TicketService {

    private final TicketRepository ticketRepository = new TicketRepository();
    private final DrawRepository drawRepository = new DrawRepository();

    public Ticket createTicket(Long userId, Long drawId) {
        if (userId == null) {
            throw new IllegalStateException("Требуется авторизованный пользователь");
        }

        Optional<Draw> drawOptional = drawRepository.findById(drawId);

        if (drawOptional.isEmpty()) {
            throw new IllegalArgumentException("Тираж не найден");
        }

        Draw draw = drawOptional.get();
        if (!"ACTIVE".equals(draw.status)) {
            throw new IllegalStateException("Тираж уже завершен");
        }

        Ticket ticket = new Ticket();
        ticket.userId = userId;
        ticket.drawId = drawId;
        ticket.combination = LotteryService.combinationToString(LotteryService.generateCombination());
        ticket.status = "PENDING";

        ticketRepository.save(ticket);
        return ticket;
    }

    public List<Ticket> getUserTickets(Long userId) {
        return ticketRepository.findByUserId(userId);
    }

    public Ticket getUserTicket(Long userId, Long ticketId) {
        return ticketRepository.findByUserIdAndId(userId, ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Билет не найден"));
    }

    public void checkAllTickets(Long drawId, List<Integer> winningNumbers) {
        List<Ticket> tickets = ticketRepository.findByDrawId(drawId);

        for (Ticket ticket : tickets) {
            List<Integer> ticketNumbers = LotteryService.combinationFromString(ticket.combination);
            ticket.status = ticketNumbers.equals(winningNumbers) ? "WIN" : "LOSE";
            ticketRepository.update(ticket);
        }
    }

    public Ticket getTicketResult(Long userId, Long ticketId) {
        Ticket ticket = getUserTicket(userId, ticketId);

        if ("PENDING".equals(ticket.status)) {
            throw new IllegalStateException("Результат билета пока недоступен");
        }

        return ticket;
    }
}

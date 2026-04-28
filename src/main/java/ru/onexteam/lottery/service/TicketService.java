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

    //создание билета
    public Ticket createTicket(Long userId, Long drawId, String combination) {

        //Получаем тираж по id-проверяем что он существует и ещё активен
        Optional<Draw> drawOptional = drawRepository.findById(drawId);

        if (drawOptional.isEmpty()) {
            throw new IllegalArgumentException("Тираж не найден");
        }
        Draw draw = drawOptional.get(); // достаём Draw из Optional

        if (!"ACTIVE".equals(draw.status)) {
            throw new IllegalStateException("Тираж уже завершён, билеты не продаются");
        }

        //Нормализуем комбинацию перед сохранением - парсим и сортируем
        List<Integer> numbers = LotteryService.combinationFromString(combination);
        String normalizedCombination = LotteryService.combinationToString(numbers);

        Ticket ticket = new Ticket();
        ticket.userId = userId;
        ticket.drawId = drawId;
        ticket.combination = combination;
        ticket.status = "PENDING";

        //Сохраняем новый билет с комбинацией пользователя и статусом "PENDING"
        ticketRepository.save(ticket);
        return ticket;
    }

    //проверка всех билетов после розыгрыша тиража
    public void chekAllTickets(Long drawId, List<Integer>  winningNumbers) {

        //Получаем все билеты этого тиража
        List<Ticket> tickets = ticketRepository.findByStatus(drawId);

        for (Ticket ticket : tickets) {
            List<Integer> ticketNumbers = LotteryService.combinationFromString(ticket.combination);

            if (ticketNumbers.equals(winningNumbers)) {
                ticket.status = "WIN";
            } else {
                ticket.status = "LOSE";
            }

            //Обновляем статус каждого билета "WIN" или "LOSE"
            ticketRepository.update(ticket);
        }
    }

    //получить результат конкретного билета
    public Ticket getTicketResult(Long ticketId) {

        //Получаем билет по id чтобы пользователь мог узнать свой результат
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);
        if (ticket.isEmpty()) {
            throw new IllegalArgumentException("Билет не найден");
        }

        return ticket.get(); // достаём Ticket из Optional
    }
}
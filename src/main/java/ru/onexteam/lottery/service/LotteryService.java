package ru.onexteam.lottery.service;

import ru.onexteam.lottery.model.Draw;
import ru.onexteam.lottery.model.DrawResult;
import ru.onexteam.lottery.repository.DrawResultRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.Arrays;

public class LotteryService {

    private final DrawService drawService = new DrawService();
    private final TicketService ticketService = new TicketService();
    private final DrawResultRepository drawResultRepository = new DrawResultRepository();

    public DrawResult runDraw(Long drawId) {

        //1. Находим тираж
        Draw draw = drawService.getDrawById(drawId);
        if (draw == null) {
            throw new IllegalArgumentException("Тираж не найден");
        }
        if (!"ACTIVE".equals(draw.status)) {
            throw new IllegalStateException("Розыгрыш этого тиража уже проведен");
        }

        //2. Генерация выигрышной комбинации
        List<Integer> winningNumbers = generateCombination();

        //В БД и модель сохраняем строкой
        String winningCombination = combinationToString(winningNumbers);

        //3. Завершение тиража
        drawService.finishDraw(draw, winningCombination);

        //4. Проверка билетов, WIN/LOSE
        ticketService.chekAllTickets(drawId, winningNumbers);

        //5. Сохраняем + возвращаем результат
        DrawResult result = new DrawResult();
        result.drawId = drawId;
        result.winningCombination = winningCombination;

        drawResultRepository.save(result);
        return result;
    }

    //Генерация 6 уникальных чисел от 1 до 49
    private List<Integer> generateCombination() {
        List<Integer> numbers = new ArrayList<>();
        Random random = new Random();

        while (numbers.size() < 6) {
            int num = random.nextInt(49) + 1;
            if (!numbers.contains(num)) {
                numbers.add(num);
            }
        }
        Collections.sort(numbers);
        return numbers;
    }

    //Список - строка для БД: [5, 12, 23] → "5,12,23"
    static String combinationToString(List<Integer> combination) {
        return combination.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    //Строка - список для логики: "5,12,23" → [5, 12, 23]
    static List<Integer> combinationFromString(String combination) {
        return Arrays.stream(combination.split(","))
                .map(Integer::parseInt)
                .sorted() // числовая сортировка
                .collect(Collectors.toList());
    }
}

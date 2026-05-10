package ru.onexteam.lottery.service;

import ru.onexteam.lottery.model.Draw;
import ru.onexteam.lottery.model.DrawResult;
import ru.onexteam.lottery.repository.DrawResultRepository;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LotteryService {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final DrawService drawService = new DrawService();
    private final TicketService ticketService = new TicketService();
    private final DrawResultRepository drawResultRepository = new DrawResultRepository();

    public DrawResult runDraw(Long drawId) {
        Optional<Draw> drawOptional = drawService.getDrawById(drawId);
        if (drawOptional.isEmpty()) {
            throw new IllegalArgumentException("Тираж не найден");
        }

        Draw draw = drawOptional.get();
        if (!"ACTIVE".equals(draw.status)) {
            throw new IllegalStateException("Тираж уже завершен");
        }

        List<Integer> winningNumbers = generateCombination();
        DrawResult result = new DrawResult();
        result.drawId = drawId;
        result.winningCombination = combinationToString(winningNumbers);

        drawService.finishDraw(draw);
        ticketService.checkAllTickets(drawId, winningNumbers);
        drawResultRepository.save(result);

        return result;
    }

    public static List<Integer> generateCombination() {
        List<Integer> numbers = new ArrayList<>();

        while (numbers.size() < 6) {
            int number = RANDOM.nextInt(49) + 1;
            if (!numbers.contains(number)) {
                numbers.add(number);
            }
        }

        Collections.sort(numbers);
        return numbers;
    }

    public static String combinationToString(List<Integer> combination) {
        return combination.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    public static List<Integer> combinationFromString(String combination) {
        return Arrays.stream(combination.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .sorted()
                .collect(Collectors.toList());
    }
}

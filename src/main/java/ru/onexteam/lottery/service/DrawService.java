package ru.onexteam.lottery.service;

import ru.onexteam.lottery.model.Draw;
import ru.onexteam.lottery.repository.DrawRepository;

import java.util.List;

public class DrawService {

    private final DrawRepository drawRepository = new DrawRepository();

    //создание нового тиража
    public Draw createDraw(String title){
        Draw draw = new Draw();
        draw.title = title;
        draw.status = "ACTIVE";
        draw.winningCombination = null;

        drawRepository.save(draw);
        return draw;

    }

    //получить все активные тиражи
    public List<Draw> getActiveDraws() {
        return drawRepository.findByStatus("ACTIVE");
    }

    //завершить тираж
    public void finishDraw(Draw draw, String winningCombination) {
        draw.status = "FINISHED";
        draw.winningCombination = winningCombination;
        drawRepository.update(draw);
    }

    //поиск по id
    public Draw getDrawById(Long drawId) {
        return drawRepository.findById(drawId);

    }
}
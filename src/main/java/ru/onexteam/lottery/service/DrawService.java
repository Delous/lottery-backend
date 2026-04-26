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

        //Сохраняем новый тираж с названием и статусом "ACTIVE"
        //после сохранения drawRepository должен записать
        //сгенерированный id обратно в объект draw
        drawRepository.save(draw);
        return draw;

    }

    //получить все активные тиражи
    public List<Draw> getActiveDraws() {

        //Получаем все тиражи у которых статус "ACTIVE"
        return drawRepository.findByStatus("ACTIVE");
    }

    //завершить тираж
    public void finishDraw(Draw draw, String winningCombination) {
        draw.status = "FINISHED";
        draw.winningCombination = winningCombination;

        //Обновляем тираж - записываем выигрышную комбинацию и меняем статус на "FINISHED"
        drawRepository.update(draw);
    }

    //поиск по id
    public Draw getDrawById(Long drawId) {
        //Получаем тираж по его id, чтобы проверить существует ли он
        return drawRepository.findById(drawId);

    }
}
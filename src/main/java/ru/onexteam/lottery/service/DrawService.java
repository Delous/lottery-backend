package ru.onexteam.lottery.service;

import ru.onexteam.lottery.model.Draw;
import ru.onexteam.lottery.model.DrawResult;
import ru.onexteam.lottery.repository.DrawRepository;
import ru.onexteam.lottery.repository.DrawResultRepository;

import java.util.List;
import java.util.Optional;

public class DrawService {

    private final DrawRepository drawRepository = new DrawRepository();
    private final DrawResultRepository drawResultRepository = new DrawResultRepository();

    public Draw createDraw(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Требуется название тиража");
        }

        Draw draw = new Draw();
        draw.title = title.trim();
        draw.status = "ACTIVE";

        drawRepository.save(draw);
        return draw;
    }

    public List<Draw> getActiveDraws() {
        return drawRepository.findByStatus("ACTIVE");
    }

    public void finishDraw(Draw draw) {
        draw.status = "FINISHED";
        drawRepository.update(draw);
    }

    public Optional<Draw> getDrawById(Long drawId) {
        return drawRepository.findById(drawId);
    }

    public Optional<DrawResult> getDrawResult(Long drawId) {
        return drawResultRepository.findByDrawId(drawId);
    }
}

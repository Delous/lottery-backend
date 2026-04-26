package ru.onexteam.lottery.model;

public class DrawResult {
    public Long id;
    public Long drawId;
    public String winningCombination;

    public DrawResult() {
    }

    public DrawResult(Long id, Long drawId, String winningCombination) {
        this.id = id;
        this.drawId = drawId;
        this.winningCombination = winningCombination;
    }
}
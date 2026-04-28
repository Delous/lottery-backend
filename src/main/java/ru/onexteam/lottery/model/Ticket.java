package ru.onexteam.lottery.model;

public class Ticket {
    public Long id;
    public Long userId;
    public Long drawId;
    public String combination;
    public String status; // WIN / LOSE

    public Ticket() {
    }

    public Ticket(Long id, Long userId, Long drawId, String combination, String status) {
        this.id = id;
        this.userId = userId;
        this.drawId = drawId;
        this.combination = combination;
        this.status = status;
    }
}
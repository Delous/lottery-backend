package ru.onexteam.lottery.model;

public class Ticket {
    public Long id;
    public Long userId;
    public Long drawId;
    public String combination;
    public String status; // WIN / LOSE
}
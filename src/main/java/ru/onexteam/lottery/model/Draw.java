package ru.onexteam.lottery.model;

public class Draw {
    public Long id;
    public String title;
    public String status; // ACTIVE / FINISHED

    public Draw() {
    }

    public Draw(Long id, String title, String status) {
        this.id = id;
        this.title = title;
        this.status = status;
    }
}
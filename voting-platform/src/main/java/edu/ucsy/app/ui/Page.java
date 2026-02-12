package edu.ucsy.app.ui;

public enum Page {
    Home, ActivePoll, History;

    public String getFxml() {
        return "%s.fxml".formatted(name());
    }
}

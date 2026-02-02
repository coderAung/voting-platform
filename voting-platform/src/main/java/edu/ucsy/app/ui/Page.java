package edu.ucsy.app.ui;

public enum Page {
    Home, ActivePoll, History;

    String getFxml() {
        return "%s.fxml".formatted(name());
    }
}

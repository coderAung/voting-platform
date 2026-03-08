package edu.ucsy.app.ui;

public enum Page {
    Home, ActivePoll, History, PollState;

    public String getFxml() {
        return "/edu/ucsy/app/ui/controller/%s.fxml".formatted(name());
    }
}

package edu.ucsy.app.ui.controller;

import edu.ucsy.app.rmi.dto.output.PollInfo;
import edu.ucsy.app.ui.Page;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ScrollPane;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MasterLayout {

    @Getter
    @Setter
    private static PollInfo currentPoll;

    @FXML private StackPane contentArea;
    private final ApplicationContext springContext;

    @FXML
    public void initialize() {
        showPage(Home.class, Page.Home);
    }

    @FXML public void showHome() { showPage(Home.class, Page.Home); }
    @FXML public void showHistory() { showPage(History.class, Page.History); }

    public <T> void showPage(Class<T> clz, Page page) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    clz.getResource(page.getFxml())
            );
            loader.setControllerFactory(springContext::getBean);
            Parent view = loader.load();
            if (view instanceof ScrollPane) {
                contentArea.getChildren().setAll(view);
            } else {
                ScrollPane scrollPane = new ScrollPane(view);
                scrollPane.setFitToWidth(true);
                scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
                contentArea.getChildren().setAll(scrollPane);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Parent ensureScrolling(Parent view) {
        if (view instanceof ScrollPane) {
            return view;
        }
        ScrollPane scrollPane = new ScrollPane(view);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return scrollPane;
    }

}
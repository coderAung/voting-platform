package edu.ucsy.app.ui.controller;

import edu.ucsy.app.rmi.dto.output.PollInfo;
import edu.ucsy.app.server.entities.Poll;
import edu.ucsy.app.server.service.PollManagementService;
import edu.ucsy.app.ui.DateTimeUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class History {

    private final PollManagementService pollManagementService;

    @FXML private VBox historyContainer;

    @FXML
    public void initialize() {
        refresh();
    }

    private void refresh() {
        historyContainer.getChildren().clear();
        pollManagementService.getAll().stream().map(PollInfo::new).forEach(poll -> {
            historyContainer.getChildren().add(createHistoryCard(poll));
        });
    }

    private Node createHistoryCard(PollInfo pollInfo) {
        var box = new VBox();
        box.setSpacing(12);
        box.getStyleClass().add("poll-card");

        var hbox = new HBox();
        hbox.setSpacing(4);
        var labelBox = new VBox();
        var title = new Label(pollInfo.title());
        title.getStyleClass().add("poll-title");

        var info = new Label("Ended: %s . Total %s ~ (%s)".formatted(DateTimeUtils.formatDate(pollInfo.endTime()), pollInfo.total(), pollInfo.isOwner() ? "Hosted" : "Joined"));
        info.getStyleClass().add("poll-meta");

        labelBox.getChildren().add(title);
        labelBox.getChildren().add(info);
        labelBox.setSpacing(4);
        HBox.setHgrow(labelBox, Priority.ALWAYS);

        var actionBox = new VBox();
        var status = new Label(pollInfo.status().name());
        status.getStyleClass().add(pollInfo.status().getCss());
        actionBox.setSpacing(8);
        actionBox.getChildren().add(status);

        if(!pollInfo.status().equals(Poll.Status.Active)) {
            var deleteBtn = new Button("Delete");
            deleteBtn.getStyleClass().add("delete-btn");
            deleteBtn.setOnAction(ev -> delete(pollInfo.id()));
            actionBox.getChildren().add(deleteBtn);
        }

        hbox.getChildren().add(labelBox);
        hbox.getChildren().add(actionBox);

        var vbox = new VBox();
        vbox.setSpacing(8);

        pollInfo.options().forEach(o -> {
            var optionBox = new HBox();
            optionBox.getStyleClass().add("option-row");
            if(pollInfo.total() > 0 && pollInfo.winner().id().equals(o.id())) {
                optionBox.getStyleClass().add("winner");
            }
            optionBox.getChildren().add(new Label(o.title()));
            var region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);
            optionBox.getChildren().add(region);
            optionBox.getChildren().add(new Label("%s votes".formatted(o.votes())));

            vbox.getChildren().add(optionBox);
        });

        box.getChildren().addAll(hbox, vbox);

        return box;
    }

    private void delete(UUID id) {
        pollManagementService.delete(id);
        refresh();
    }
}
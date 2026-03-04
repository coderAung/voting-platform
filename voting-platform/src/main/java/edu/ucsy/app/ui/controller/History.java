package edu.ucsy.app.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class History {

    @FXML private TableView<?> historyTable;

    @FXML
    public void initialize() {
        // table setup will go here later
    }

    private void delete() {
        // your delete logic here
    }
}
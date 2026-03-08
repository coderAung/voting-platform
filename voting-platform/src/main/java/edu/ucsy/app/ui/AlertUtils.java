package edu.ucsy.app.ui;

import javafx.scene.control.Alert;

public class AlertUtils {
    public static void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}

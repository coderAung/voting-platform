module edu.ucsy.app.votingplatform {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens edu.ucsy.app to javafx.fxml;
    opens edu.ucsy.app.ui.controller to javafx.fxml;

    exports edu.ucsy.app;
}
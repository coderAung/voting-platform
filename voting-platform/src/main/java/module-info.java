module edu.ucsy.app.votingplatform {
    requires javafx.controls;
    requires javafx.fxml;


    opens edu.ucsy.app to javafx.fxml;
    exports edu.ucsy.app;
}
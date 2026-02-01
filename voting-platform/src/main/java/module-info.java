module edu.ucsy.app.votingplatform {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires spring.core;
    requires spring.beans;
    requires spring.context;

    opens edu.ucsy.app.ui to javafx.fxml;
    opens edu.ucsy.app to spring.core, spring.beans, spring.context,
            javafx.fxml;
    opens edu.ucsy.app.handler to spring.core, spring.beans, spring.context;
    opens edu.ucsy.app.server.repo to spring.core, spring.beans, spring.context;
    opens edu.ucsy.app.server.service to spring.core, spring.beans, spring.context;
    opens edu.ucsy.app.server.entities to spring.core, spring.beans, spring.context;
    opens edu.ucsy.app.ui.controller to spring.core, spring.beans, spring.context,
            javafx.fxml, javafx.controls;

    exports edu.ucsy.app;

    exports edu.ucsy.app.server.repo;
    exports edu.ucsy.app.server.service;
    exports edu.ucsy.app.server.entities;

    exports edu.ucsy.app.ui;
    exports edu.ucsy.app.ui.controller;

    exports edu.ucsy.app.handler;
    exports edu.ucsy.app.handler.event;

    exports edu.ucsy.app.rmi;
    exports edu.ucsy.app.rmi.dto;
}
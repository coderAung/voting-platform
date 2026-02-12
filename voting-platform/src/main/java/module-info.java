module edu.ucsy.app.votingplatform {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires spring.core;
    requires spring.beans;
    requires spring.context;
    requires java.rmi;
    requires static lombok;
    requires jakarta.persistence;
    requires spring.data.jpa;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires org.hibernate.orm.core;
    requires spring.tx;

    opens edu.ucsy.app.ui to javafx.fxml;
    opens edu.ucsy.app to spring.core, spring.beans, spring.context,
            javafx.fxml;
    opens edu.ucsy.app.handler to spring.core, spring.beans, spring.context;
    opens edu.ucsy.app.config to spring.core, spring.beans, spring.context;

    opens edu.ucsy.app.server to spring.core, spring.beans, spring.context;
    opens edu.ucsy.app.server.repo to spring.core, spring.beans, spring.context;
    opens edu.ucsy.app.server.service to spring.core, spring.beans, spring.context;
    opens edu.ucsy.app.server.service.impl to spring.core, spring.beans, spring.context;
    opens edu.ucsy.app.server.entities to spring.core, spring.beans, spring.context,
            spring.data.jpa, jakarta.persistence, org.hibernate.orm.core;
    opens edu.ucsy.app.server.entities.pk to spring.core, spring.beans, spring.context,
            org.hibernate.orm.core;
    opens edu.ucsy.app.ui.controller to spring.core, spring.beans, spring.context,
            javafx.fxml, javafx.controls;

    exports edu.ucsy.app;
    exports edu.ucsy.app.handler;

    exports edu.ucsy.app.server.repo;
    exports edu.ucsy.app.server.service;
    exports edu.ucsy.app.server.entities;
    exports edu.ucsy.app.server.entities.pk;

    exports edu.ucsy.app.ui;
    exports edu.ucsy.app.ui.controller;

    exports edu.ucsy.app.rmi;
    exports edu.ucsy.app.rmi.dto;
    exports edu.ucsy.app.rmi.event;
    exports edu.ucsy.app.rmi.dto.output;
}
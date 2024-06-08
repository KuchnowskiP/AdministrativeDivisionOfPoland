module pl.edu.pwr.database.administrativedivisionofpoland {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.annotation;
    requires java.net.http;
    requires AdministrativeDivisionOfPoland.Backend.Contract.main;
    requires com.fasterxml.jackson.databind;


    opens pl.edu.pwr.database.administrativedivisionofpoland to javafx.fxml;
    exports pl.edu.pwr.database.administrativedivisionofpoland;
    exports pl.edu.pwr.database.administrativedivisionofpoland.controllers;
    opens pl.edu.pwr.database.administrativedivisionofpoland.controllers to javafx.fxml;
    exports pl.edu.pwr.database.administrativedivisionofpoland.data;
    opens pl.edu.pwr.database.administrativedivisionofpoland.data to javafx.fxml;
    exports pl.edu.pwr.database.administrativedivisionofpoland.data.services;
    opens pl.edu.pwr.database.administrativedivisionofpoland.data.services to javafx.fxml;
    exports pl.edu.pwr.database.administrativedivisionofpoland.handlers;
    opens pl.edu.pwr.database.administrativedivisionofpoland.handlers to javafx.fxml;
    exports pl.edu.pwr.database.administrativedivisionofpoland.data.services.api;
    opens pl.edu.pwr.database.administrativedivisionofpoland.data.services.api to javafx.fxml;
    exports pl.edu.pwr.database.administrativedivisionofpoland.authentication;
    exports pl.edu.pwr.database.administrativedivisionofpoland.builders.controllers;
    opens pl.edu.pwr.database.administrativedivisionofpoland.builders.controllers to javafx.fxml;
    exports pl.edu.pwr.database.administrativedivisionofpoland.builders.data.services;
    opens pl.edu.pwr.database.administrativedivisionofpoland.builders.data.services to javafx.fxml;
    exports pl.edu.pwr.database.administrativedivisionofpoland.data.managers;
    opens pl.edu.pwr.database.administrativedivisionofpoland.data.managers to javafx.fxml;
    exports pl.edu.pwr.database.administrativedivisionofpoland.builders.data.managers;
    opens pl.edu.pwr.database.administrativedivisionofpoland.builders.data.managers to javafx.fxml;
    exports pl.edu.pwr.database.administrativedivisionofpoland.builders.data.fetchers;
    opens pl.edu.pwr.database.administrativedivisionofpoland.builders.data.fetchers to javafx.fxml;
}
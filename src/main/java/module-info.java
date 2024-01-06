module pl.edu.pwr.database.administrativedivisionofpoland {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.annotation;
    requires java.net.http;
    requires AdministrativeDivisionOfPoland.Backend.Contract.main;
    requires com.fasterxml.jackson.databind;


    opens pl.edu.pwr.database.administrativedivisionofpoland to javafx.fxml;
    exports pl.edu.pwr.database.administrativedivisionofpoland;
}
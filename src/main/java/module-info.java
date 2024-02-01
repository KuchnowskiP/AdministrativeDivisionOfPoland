module pl.edu.pwr.database.administrativedivisionofpoland {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.annotation;
    requires java.net.http;
    requires AdministrativeDivisionOfPoland.Backend.Contract.main;
    requires com.fasterxml.jackson.databind;


    opens pl.edu.pwr.database.administrativedivisionofpoland to javafx.fxml;
    exports pl.edu.pwr.database.administrativedivisionofpoland;
    exports pl.edu.pwr.database.administrativedivisionofpoland.Controllers;
    opens pl.edu.pwr.database.administrativedivisionofpoland.Controllers to javafx.fxml;
    exports pl.edu.pwr.database.administrativedivisionofpoland.Data;
    opens pl.edu.pwr.database.administrativedivisionofpoland.Data to javafx.fxml;
    exports pl.edu.pwr.database.administrativedivisionofpoland.Data.Services;
    opens pl.edu.pwr.database.administrativedivisionofpoland.Data.Services to javafx.fxml;
    exports pl.edu.pwr.database.administrativedivisionofpoland.Handlers;
    opens pl.edu.pwr.database.administrativedivisionofpoland.Handlers to javafx.fxml;
}
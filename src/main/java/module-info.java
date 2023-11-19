module pl.edu.pwr.database.administrativedivisionofpoland {
    requires javafx.controls;
    requires javafx.fxml;


    opens pl.edu.pwr.database.administrativedivisionofpoland to javafx.fxml;
    exports pl.edu.pwr.database.administrativedivisionofpoland;
}
package pl.edu.pwr.database.administrativedivisionofpoland.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import pl.edu.pwr.database.administrativedivisionofpoland.UserData;

import java.net.URL;
import java.util.ResourceBundle;

public class Confirmation implements Initializable{

    public Button confirmationButton;
    public Label promptLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        promptLabel.setText("Czy na pewno chcesz " + UserData.prompt);
    }

    public void onConfirmButtonClick(ActionEvent actionEvent) {
        UserData.confirmed = true;
        Stage stage = (Stage) confirmationButton.getScene().getWindow();
        stage.close();
    }

    public void onDeclineButtonClick(ActionEvent actionEvent) {
        UserData.confirmed = false;
        Stage stage = (Stage) confirmationButton.getScene().getWindow();
        stage.close();
    }



}

package pl.edu.pwr.database.administrativedivisionofpoland.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import pl.edu.pwr.database.administrativedivisionofpoland.UserInput;

import java.net.URL;
import java.util.ResourceBundle;

public class ConfirmationPopupController implements Initializable{

    public Button confirmationButton;
    public Label promptLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        promptLabel.setText("Czy na pewno chcesz " + UserInput.prompt);
    }

    public void onConfirmButtonClick(ActionEvent ignoredActionEvent) {
        UserInput.confirmed = true;
        Stage stage = (Stage) confirmationButton.getScene().getWindow();
        stage.close();
    }

    public void onDeclineButtonClick(ActionEvent ignoredAactionEvent) {
        UserInput.confirmed = false;
        Stage stage = (Stage) confirmationButton.getScene().getWindow();
        stage.close();
    }
}

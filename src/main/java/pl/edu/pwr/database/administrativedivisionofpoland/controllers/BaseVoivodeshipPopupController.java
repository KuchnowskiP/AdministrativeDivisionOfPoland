package pl.edu.pwr.database.administrativedivisionofpoland.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import pl.edu.pwr.database.administrativedivisionofpoland.UserInput;
import pl.edu.pwr.database.administrativedivisionofpoland.data.api.IDataSender;
import pl.edu.pwr.database.administrativedivisionofpoland.data.api.IResultFetcher;

public abstract class BaseVoivodeshipPopupController extends BaseUnitPopupController {
    @FXML
    protected TextField licensePlateDifferentiatorTextField;
    @FXML
    protected TextField voivodeshipNameTextField;
    @FXML
    protected Label returningLabel;

    public BaseVoivodeshipPopupController(IResultFetcher resultFetcher, IDataSender requestSender) {
        super(resultFetcher, requestSender);
    }

    public boolean isInputIncorrect() {
        if (licensePlateDifferentiatorTextField.getText().length() > 1) {
            returningLabel.setText("Wyróżnik musi się składać z jednej litery");
            returningLabel.setVisible(true);
            UserInput.confirmed = false;
            return true;
        }

        if (voivodeshipNameTextField.getText().trim().isEmpty()) {
            returningLabel.setText("Nazwa jest wymagana");
            returningLabel.setVisible(true);
            UserInput.confirmed = false;
            return true;
        }
        returningLabel.setVisible(false);
        return false;
    }
}

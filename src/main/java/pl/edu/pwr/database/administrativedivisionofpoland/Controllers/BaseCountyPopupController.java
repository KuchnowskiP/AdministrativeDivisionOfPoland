package pl.edu.pwr.database.administrativedivisionofpoland.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import pl.edu.pwr.database.administrativedivisionofpoland.UserInput;

public abstract class BaseCountyPopupController extends BaseUnitPopupController {
    @FXML
    protected TextField countyNameTextField;
    @FXML
    protected TextField licensePlateDifferentiatorTextField;
    @FXML
    protected Label returningLabel;

    public boolean isInputIncorrect() {
        if (licensePlateDifferentiatorTextField.getText().length() > 2) {
            returningLabel.setText("Wyróżnik musi się składać z maksymalnie dwóch litery");
            returningLabel.setVisible(true);
            UserInput.confirmed = false;
            return true;
        }
        if (countyNameTextField.getText().trim().isEmpty()) {
            returningLabel.setText("Nazwa jest wymagana");
            returningLabel.setVisible(true);
            UserInput.confirmed = false;
            return true;
        }
        returningLabel.setVisible(false);
        return false;
    }

}

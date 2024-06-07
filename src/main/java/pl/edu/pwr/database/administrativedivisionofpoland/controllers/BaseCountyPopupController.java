package pl.edu.pwr.database.administrativedivisionofpoland.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.VoivodeshipDto;
import pl.edu.pwr.database.administrativedivisionofpoland.UserInput;
import pl.edu.pwr.database.administrativedivisionofpoland.data.api.IDataSender;
import pl.edu.pwr.database.administrativedivisionofpoland.data.api.IResultFetcher;

import java.util.Objects;

public abstract class BaseCountyPopupController extends BaseUnitPopupController {
    @FXML
    protected TextField countyNameTextField;
    @FXML
    protected TextField licensePlateDifferentiatorTextField;
    @FXML
    protected Label returningLabel;
    @FXML
    protected ChoiceBox voivodeshipChoiceBox;
    protected PageResult<VoivodeshipDto> requestVoivodeships;
    protected VoivodeshipDto selectedVoivodeship = new VoivodeshipDto(-1, "", "", "");

    public BaseCountyPopupController(IResultFetcher resultFetcher, IDataSender requestSender) {
        super(resultFetcher, requestSender);
    }

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

    public void setChoiceBoxes() throws Exception {
        requestVoivodeships = resultFetcher.getVoivodeships(1, Integer.MAX_VALUE);
        voivodeshipChoiceBox.getItems().add("-");
        voivodeshipChoiceBox.setValue("-");
        for (int i = 0; i < requestVoivodeships.items.size(); i++) {
            voivodeshipChoiceBox.getItems().add(requestVoivodeships.getItems().get(i).getName());
        }
    }

    public void initializeChoiceBoxesListeners() {
        voivodeshipChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue != null) {
                    System.out.println(newValue);
                    if (!Objects.equals(String.valueOf(newValue), "-")) {
                        selectedVoivodeship = requestVoivodeships.getItems().stream()
                                .filter(voivodeshipDto -> newValue.equals(voivodeshipDto.getName())).findAny().get();
                    } else {
                        selectedVoivodeship.setId(-1);
                    }
                }
            }
        });
    }

}

package pl.edu.pwr.database.administrativedivisionofpoland.Controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.County.CountyRequest;
import pl.edu.pwr.contract.Dtos.CountyDto;
import pl.edu.pwr.contract.Dtos.VoivodeshipDto;
import pl.edu.pwr.contract.OfficeAdres.OfficeAddressRequest;
import pl.edu.pwr.database.administrativedivisionofpoland.Data.DataSender;
import pl.edu.pwr.database.administrativedivisionofpoland.Data.Services.VoivodeshipService;
import pl.edu.pwr.database.administrativedivisionofpoland.UserInput;
import pl.edu.pwr.database.administrativedivisionofpoland.Utils.Utils;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddCountyPopupController extends BaseCountyPopupController implements Initializable {
    @FXML
    private CheckBox cityRightsCheckBox;
    @FXML
    private TextField localityTextField;
    @FXML
    private TextField postLocalityTextField;
    @FXML
    private TextField streetTextField;
    @FXML
    private TextField numberOfBuildingTextField;
    @FXML
    private TextField apartmentNumberTextField;
    @FXML
    private TabPane addressSelectionTabPane;
    @FXML
    private ChoiceBox voivodeshipChoiceBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTabPaneListeners();
        try {
            setChoiceBoxes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        initializeChoiceBoxesListeners();
    }

    PageResult<VoivodeshipDto> requestVoivodeships;
    VoivodeshipDto selectedVoivodeship = new VoivodeshipDto(-1,"","","");

    public void setChoiceBoxes() throws Exception {
        requestVoivodeships = Utils.getVoivodeshipResult(voivodeshipChoiceBox);
    }

    private void initializeChoiceBoxesListeners(){
        voivodeshipChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue)  {
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

    public void onConfirmButtonClick(ActionEvent actionEvent) throws Exception {
        if(isInputIncorrect()){
            return;
        }

        UserInput.prompt = "\ndodać powiat o nazwie \"" + countyNameTextField.getText() + "\"?";
        UserInput.getConfirmation();

        if(UserInput.confirmed) {
            CountyRequest countyRequest = new CountyRequest();
            countyRequest.setName(countyNameTextField.getText().trim());
            countyRequest.setVoivodeshipId(selectedVoivodeship.getId());
            countyRequest.setLicensePlateDifferentiator(licensePlateDifferentiatorTextField.getText());
            countyRequest.setIsCityWithCountyRights(cityRightsCheckBox.isSelected());
            String newTeryt = requestResultsReceiver.newCountyTeryt(selectedVoivodeship.getId(), cityRightsCheckBox.isSelected() ? 1 : 0);
            if(newTeryt == null){
                int newTerytInt = Integer.parseInt(selectedVoivodeship.getTerytCode());
                newTerytInt += 1000;
                newTerytInt += cityRightsCheckBox.isSelected() ? 1 : 0;
                newTeryt = String.format("%07d",newTerytInt);
            }
            countyRequest.setTerytCode(newTeryt);
            if(addressSelectionTabPane.getSelectionModel().isSelected(1)){
                OfficeAddressRequest newAddress = new OfficeAddressRequest();
                newAddress.setLocality(postLocalityTextField.getText().trim());
                newAddress.setStreet(streetTextField.getText().trim());
                newAddress.setPostalCode(postalCodeTextField.getText().trim());
                newAddress.setNumberOfBuilding(numberOfBuildingTextField.getText().trim());
                newAddress.setApartmentNumber(apartmentNumberTextField.getText().trim());
                HttpResponse<String> responseWithIdAsABody = requestSender.addAddress(newAddress);

                countyRequest.setLocality(localityTextField.getText().trim());

                countyRequest.setRegisteredOfficeAddressesId(Integer.parseInt(responseWithIdAsABody.body()));
            }else{
                countyRequest.setLocality(place);
                countyRequest.setRegisteredOfficeAddressesId(addressID);
            }

            if(requestSender.addCounty(countyRequest)){
                returningLabel.setVisible(true);
                returningLabel.setText("Pomyślnie dodano nowy powiat!");
            }else{
                returningLabel.setVisible(true);
                returningLabel.setText("Nie udało się dodać powiatu! Wprowadzono niepoprawne dane lub powiat" +
                        " o podanych atrybutach już istnieje.");
            }
            UserInput.confirmed = false;
        }else {
            returningLabel.setVisible(true);
            returningLabel.setText("Nie dodano powiatu, użytkownik przerwał operację!");
        }
    }
}
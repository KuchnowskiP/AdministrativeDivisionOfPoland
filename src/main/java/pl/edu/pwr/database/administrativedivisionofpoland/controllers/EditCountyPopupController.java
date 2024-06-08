package pl.edu.pwr.database.administrativedivisionofpoland.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import pl.edu.pwr.contract.County.CountyRequest;
import pl.edu.pwr.contract.Dtos.CountyDto;
import pl.edu.pwr.contract.OfficeAdres.OfficeAddressRequest;
import pl.edu.pwr.database.administrativedivisionofpoland.UserInput;
import pl.edu.pwr.database.administrativedivisionofpoland.data.IDataSender;
import pl.edu.pwr.database.administrativedivisionofpoland.data.IResultReceiver;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.ResourceBundle;

public class EditCountyPopupController extends BaseCountyPopupController implements Initializable {
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

    public EditCountyPopupController(IResultReceiver resultFetcher, IDataSender requestSender) {
        super(resultFetcher, requestSender);
    }

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

    public void onConfirmButtonClick(ActionEvent ignoredActionEvent) throws Exception {
        if(isInputIncorrect()){
            return;
        }

        UserInput.prompt = "\npotwierdzić dane powiatu o nazwie \"" + countyNameTextField.getText() + "\"?";
        UserInput.getConfirmation();

        CountyDto countyDto = (CountyDto) UserInput.unit;

        if(UserInput.confirmed) {
            CountyRequest countyRequest = new CountyRequest();
            countyRequest.setName(countyNameTextField.getText().trim());
            countyRequest.setVoivodeshipId(selectedVoivodeship.getId());
            countyRequest.setLicensePlateDifferentiator(licensePlateDifferentiatorTextField.getText());
            countyRequest.setIsCityWithCountyRights(cityRightsCheckBox.isSelected());
            if(Objects.equals(countyDto.getVoivodeshipId(), selectedVoivodeship.getId()) && selectedVoivodeship.getId() != null){
                countyRequest.setTerytCode(countyDto.getTerytCode());
            }else {
                String newTeryt = resultFetcher.newCountyTeryt(selectedVoivodeship.getId(), cityRightsCheckBox.isSelected() ? 1 : 0);
                if (newTeryt == null) {
                    int newTerytInt = Integer.parseInt(selectedVoivodeship.getTerytCode());
                    newTerytInt += 1000;
                    newTerytInt += cityRightsCheckBox.isSelected() ? 1 : 0;
                    newTeryt = String.format("%07d", newTerytInt);
                }
                countyRequest.setTerytCode(newTeryt);
            }
            if(addressSelectionTabPane.getSelectionModel().isSelected(1)){
                OfficeAddressRequest newAddress = new OfficeAddressRequest();
                newAddress.setLocality(postLocalityTextField.getText().trim());
                newAddress.setStreet(streetTextField.getText().trim());
                newAddress.setPostalCode(postalCodeTextField.getText().trim());
                newAddress.setNumberOfBuilding(numberOfBuildingTextField.getText().trim());
                newAddress.setApartmentNumber(apartmentNumberTextField.getText().trim());
                HttpResponse<String> responseWithIdAsABody = requestSender.addOfficeAddress(newAddress);

                countyRequest.setLocality(localityTextField.getText().trim());

                countyRequest.setRegisteredOfficeAddressesId(Integer.parseInt(responseWithIdAsABody.body()));
            }else{
                countyRequest.setLocality(place);
                countyRequest.setRegisteredOfficeAddressesId(addressID);
            }
            if(requestSender.editCounty(countyDto.getId(),countyRequest)){
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

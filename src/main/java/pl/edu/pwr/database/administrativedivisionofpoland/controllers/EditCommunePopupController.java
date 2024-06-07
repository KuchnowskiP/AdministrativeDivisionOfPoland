package pl.edu.pwr.database.administrativedivisionofpoland.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import pl.edu.pwr.contract.Commune.CommuneRequest;
import pl.edu.pwr.contract.Dtos.CommuneDto;
import pl.edu.pwr.contract.Dtos.CountyDto;
import pl.edu.pwr.contract.OfficeAdres.OfficeAddressRequest;
import pl.edu.pwr.database.administrativedivisionofpoland.UserInput;
import pl.edu.pwr.database.administrativedivisionofpoland.data.api.IDataSender;
import pl.edu.pwr.database.administrativedivisionofpoland.data.api.IResultFetcher;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;

public class EditCommunePopupController extends BaseCommunePopupController implements Initializable {
    @FXML
    private TextField populationTextField;
    @FXML
    private TextField areaTextField;
    @FXML
    private TextField communeNameTextField;
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
    private Label returningLabel;

    public EditCommunePopupController(IResultFetcher resultFetcher, IDataSender requestSender) {
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
        initializeVoivodeshipChoiceBoxListener();
        initializeCountyChoiceBoxListener();
    }


    CountyDto selectedCounty = new CountyDto(-1,-1,"","",false,"","");

    public void onConfirmButtonClick(ActionEvent ignoredActionEvent) throws Exception {
        if (communeNameTextField.getText().trim().isEmpty()) {
            returningLabel.setText("Nazwa jest wymagana");
            returningLabel.setVisible(true);
            UserInput.confirmed = false;
            return;
        } else {
            returningLabel.setVisible(false);
        }

        UserInput.prompt = "\ndodać gminę o nazwie \"" + communeNameTextField.getText() + "\"?";
        UserInput.getConfirmation();

        if(UserInput.confirmed) {

            CommuneRequest communeRequest = new CommuneRequest();

            String rodzajGminy = communeTypeChoiceBox.getSelectionModel().selectedItemProperty().getValue().toString();
            switch (rodzajGminy) {
                case "gmina miejska": {
                    System.out.print("setting ");
                    communeRequest.setCommuneTypeId(1);
                    System.out.println(communeRequest.getCommuneTypeId());
                    break;
                }
                case "gmina wiejska": {
                    System.out.print("setting ");
                    communeRequest.setCommuneTypeId(2);
                    System.out.println(communeRequest.getCommuneTypeId());
                    break;
                }
                case "gmina miejsko-wiejska": {
                    System.out.print("setting ");
                    communeRequest.setCommuneTypeId(3);
                    System.out.println(communeRequest.getCommuneTypeId());
                    break;
                }
            }

            communeRequest.setName(communeNameTextField.getText().trim());
            communeRequest.setCountyId(selectedCounty.getId());
            communeRequest.setArea(Double.valueOf(areaTextField.getText().trim()));
            communeRequest.setPopulation(Integer.valueOf(populationTextField.getText().trim()));
            String newTeryt = resultFetcher.newCommuneTeryt(selectedCounty.getId(), communeRequest.getCommuneTypeId());
            if (newTeryt == null) {
                int newTerytInt = Integer.parseInt(selectedCounty.getTerytCode());
                newTerytInt += 10;
                newTerytInt /= 10;
                newTerytInt *= 10;
                newTerytInt += communeRequest.getCommuneTypeId();
                newTeryt = String.format("%07d", newTerytInt);
            }
            communeRequest.setTerytCode(newTeryt);
            if (addressSelectionTabPane.getSelectionModel().isSelected(1)) {
                OfficeAddressRequest newAddress = new OfficeAddressRequest();
                newAddress.setLocality(postLocalityTextField.getText().trim());
                newAddress.setStreet(streetTextField.getText().trim());
                newAddress.setPostalCode(postalCodeTextField.getText().trim());
                newAddress.setNumberOfBuilding(numberOfBuildingTextField.getText().trim());
                newAddress.setApartmentNumber(apartmentNumberTextField.getText().trim());
                HttpResponse<String> responseWithIdAsABody = requestSender.addAddress(newAddress);

                communeRequest.setLocality(localityTextField.getText().trim());

                communeRequest.setRegisteredOfficeAddressesId(Integer.parseInt(responseWithIdAsABody.body()));
            } else {
                communeRequest.setLocality(place);
                communeRequest.setRegisteredOfficeAddressesId(addressID);
            }
            CommuneDto communeDto = (CommuneDto) UserInput.unit;
            if(requestSender.editCommune(communeDto.getId(), communeRequest)){
                returningLabel.setVisible(true);
                returningLabel.setText("Pomyślnie edytowano gminę!");
            }else{
                returningLabel.setVisible(true);
                returningLabel.setText("Nie udało się edytować gminy! Wprowadzono niepoprawne dane lub gmina" +
                        " o podanych atrybutach już istnieje.");
            }
            UserInput.confirmed = false;
        }else {
            returningLabel.setVisible(true);
            returningLabel.setText("Nie edytowano gminy, użytkownik przerwał operację!");
        }
    }
}

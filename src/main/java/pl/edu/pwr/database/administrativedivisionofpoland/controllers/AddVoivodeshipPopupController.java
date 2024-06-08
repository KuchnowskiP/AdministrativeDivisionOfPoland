package pl.edu.pwr.database.administrativedivisionofpoland.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import pl.edu.pwr.contract.OfficeAdres.OfficeAddressRequest;
import pl.edu.pwr.contract.Voivodeship.VoivodeshipRequest;
import pl.edu.pwr.database.administrativedivisionofpoland.UserInput;
import pl.edu.pwr.database.administrativedivisionofpoland.data.IDataSender;
import pl.edu.pwr.database.administrativedivisionofpoland.data.IResultReceiver;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;


public class AddVoivodeshipPopupController extends BaseVoivodeshipPopupController implements Initializable{
    @FXML
    private TextField postLocalityTextField;
    @FXML
    private TextField localityTextField;
    @FXML
    private TextField streetTextField;
    @FXML
    private TextField numberOfBuildingTextField;
    @FXML
    private TextField apartmentNumberTextField;

    public AddVoivodeshipPopupController(IResultReceiver resultFetcher, IDataSender requestSender) {
        super(resultFetcher, requestSender);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTabPaneListeners();
    }

    public void onConfirmButtonClick(ActionEvent ignoredActionEvent) throws Exception {
        if(isInputIncorrect()){
            return;
        }

        UserInput.prompt = "\ndodać województwo o nazwie \"" + voivodeshipNameTextField.getText() + "\"?";
        UserInput.getConfirmation();

        if(UserInput.confirmed) {
            VoivodeshipRequest voivodeshipRequest = new VoivodeshipRequest();
            voivodeshipRequest.setName(voivodeshipNameTextField.getText().trim());
            voivodeshipRequest.setLicensePlateDifferentiator(licensePlateDifferentiatorTextField.getText());
            voivodeshipRequest.setTerytCode(resultFetcher.newVoivodeshipTeryt());

            if (addressSelectionTabPane.getSelectionModel().isSelected(1)) {
                OfficeAddressRequest newAddress = new OfficeAddressRequest();
                newAddress.setLocality(postLocalityTextField.getText().trim());
                newAddress.setStreet(streetTextField.getText().trim());
                newAddress.setPostalCode(postalCodeTextField.getText().trim());
                newAddress.setNumberOfBuilding(numberOfBuildingTextField.getText().trim());
                newAddress.setApartmentNumber(apartmentNumberTextField.getText().trim());
                HttpResponse<String> responseWithIdAsABody = requestSender.addOfficeAddress(newAddress);

                voivodeshipRequest.setLocalityFirst(localityTextField.getText().trim());
                voivodeshipRequest.setIsSeatOfCouncilFirst(true);
                voivodeshipRequest.setIsSeatOfVoivodeFirst(true);
                voivodeshipRequest.setRegisteredOfficeAddressesIdFirst(Integer.parseInt(responseWithIdAsABody.body()));

            } else {
                voivodeshipRequest.setLocalityFirst(place);
                voivodeshipRequest.setIsSeatOfCouncilFirst(true);
                voivodeshipRequest.setIsSeatOfVoivodeFirst(true);
                voivodeshipRequest.setRegisteredOfficeAddressesIdFirst(addressID);
            }

            if(requestSender.addVoivodeship(voivodeshipRequest)){
                returningLabel.setVisible(true);
                returningLabel.setText("Pomyślnie dodano nowe województwo!");
            }else{
                returningLabel.setVisible(true);
                returningLabel.setText("Nie udało się dodać województwa! Wprowadzono niepoprawne dane lub województwo" +
                        " o podanych atrybutach już istnieje.");
            }
            UserInput.confirmed = false;
        }else {
            returningLabel.setVisible(true);
            returningLabel.setText("Nie dodano województwa, użytkownik przerwał operację!");
        }
    }
}

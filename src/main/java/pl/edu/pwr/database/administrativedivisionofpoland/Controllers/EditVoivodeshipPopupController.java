package pl.edu.pwr.database.administrativedivisionofpoland.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import pl.edu.pwr.contract.Dtos.VoivodeshipDto;
import pl.edu.pwr.contract.OfficeAdres.OfficeAddressRequest;
import pl.edu.pwr.contract.Voivodeship.VoivodeshipRequest;
import pl.edu.pwr.database.administrativedivisionofpoland.Data.DataSender;
import pl.edu.pwr.database.administrativedivisionofpoland.UserInput;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;

public class EditVoivodeshipPopupController extends BaseVoivodeshipPopupController implements Initializable {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTabPaneListeners();
    }

    public void onConfirmButtonClick(ActionEvent actionEvent) throws Exception {
        if(isInputIncorrect()){
            return;
        }

        UserInput.prompt = "\npotwierdzić dane województwa o nazwie \"" + voivodeshipNameTextField.getText() + "\"?";
        UserInput.getConfirmation();

        if(UserInput.confirmed) {
            VoivodeshipRequest voivodeshipRequest = new VoivodeshipRequest();
            voivodeshipRequest.setName(voivodeshipNameTextField.getText().trim());
            voivodeshipRequest.setLicensePlateDifferentiator(licensePlateDifferentiatorTextField.getText());
            VoivodeshipDto voivodeshipDto = (VoivodeshipDto) UserInput.unit;
            voivodeshipRequest.setTerytCode(voivodeshipDto.getTerytCode());

            if (addressSelectionTabPane.getSelectionModel().isSelected(1)) {
                OfficeAddressRequest newAddress = new OfficeAddressRequest();
                newAddress.setLocality(postLocalityTextField.getText().trim());
                newAddress.setStreet(streetTextField.getText().trim());
                newAddress.setPostalCode(postalCodeTextField.getText().trim());
                newAddress.setNumberOfBuilding(numberOfBuildingTextField.getText().trim());
                newAddress.setApartmentNumber(apartmentNumberTextField.getText().trim());
                HttpResponse<String> responseWithIdAsABody = requestSender.addAddress(newAddress);

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


            if(requestSender.editVoivodeship(voivodeshipDto.getId(),voivodeshipRequest)){
                returningLabel.setVisible(true);
                returningLabel.setText("Pomyślnie edytowano województwo!");
            }else{
                returningLabel.setVisible(true);
                returningLabel.setText("Nie udało się edytować województwa! Wprowadzono niepoprawne dane lub województwo" +
                        " o podanych atrybutach już istnieje.");
            }
            UserInput.confirmed = false;
        }else {
            returningLabel.setVisible(true);
            returningLabel.setText("Nie edytowano województwa, użytkownik przerwał operację!");
        }
    }
}

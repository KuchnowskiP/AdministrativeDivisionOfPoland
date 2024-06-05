package pl.edu.pwr.database.administrativedivisionofpoland.Controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Commune.CommuneRequest;
import pl.edu.pwr.contract.Dtos.CommuneDto;
import pl.edu.pwr.contract.Dtos.CountyDto;
import pl.edu.pwr.contract.Dtos.VoivodeshipDto;
import pl.edu.pwr.contract.OfficeAdres.OfficeAddressRequest;
import pl.edu.pwr.database.administrativedivisionofpoland.Data.Services.CountyService;
import pl.edu.pwr.database.administrativedivisionofpoland.Data.DataSender;
import pl.edu.pwr.database.administrativedivisionofpoland.Data.Services.VoivodeshipService;
import pl.edu.pwr.database.administrativedivisionofpoland.UserInput;
import pl.edu.pwr.database.administrativedivisionofpoland.Utils.Utils;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.ResourceBundle;

public class EditCommunePopupController extends BaseUnitPopupController implements Initializable {
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
    private ChoiceBox communeTypeChoiceBox;
    @FXML
    private ChoiceBox countyChoiceBox;
    @FXML
    private ChoiceBox voivodeshipChoiceBox;
    @FXML
    private Label returningLabel;

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
    PageResult<CountyDto> requestCounties;
    VoivodeshipDto selectedVoivodeship = new VoivodeshipDto(-1,"","","");
    CountyDto selectedCounty = new CountyDto(-1,-1,"","",false,"","");

    public void setChoiceBoxes() throws Exception {
        requestVoivodeships = Utils.getVoivodeshipResult(voivodeshipChoiceBox);
        communeTypeChoiceBox.getItems().addAll(new Object[]{"gmina miejska", "gmina wiejska", "gmina miejsko-wiejska"});
    }
    CountyService countyService = new CountyService();
    private void initializeChoiceBoxesListeners(){
        voivodeshipChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue)  {
                if (newValue != null) {
                    System.out.println(newValue);
                    if (!Objects.equals(String.valueOf(newValue), "-")) {
                        countyChoiceBox.getItems().clear();
                        countyChoiceBox.setDisable(false);
                        countyChoiceBox.getItems().add("-");
                        countyChoiceBox.setValue("-");
                        selectedVoivodeship = requestVoivodeships.getItems().stream()
                                .filter(voivodeshipDto -> newValue.equals(voivodeshipDto.getName())).findAny().get();
                        try {
                            requestCounties = countyService.getDto(selectedVoivodeship.getId(), 1, Integer.MAX_VALUE);
                            for (int i = 0; i < requestCounties.getItems().size(); i++) {
                                countyChoiceBox.getItems().add(requestCounties.getItems().get(i).getName());
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        selectedVoivodeship.setId(-1);
                        countyChoiceBox.setValue("-");
                        countyChoiceBox.setDisable(true);
                    }
                }
            }
        });
        countyChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue != null) {
                    if (!Objects.equals(String.valueOf(newValue), "-")) {
                        selectedCounty = requestCounties.getItems().stream()
                                .filter(voivodeshipDto -> newValue.equals(voivodeshipDto.getName())).findAny().get();
                    }

                    if (Objects.equals(String.valueOf(newValue), "-")) {
                        selectedCounty.setId(-1);
                    }
                }
            }
        });
    }

    public void onConfirmButtonClick(ActionEvent actionEvent) throws Exception {
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
            String newTeryt = requestResultsReceiver.newCommuneTeryt(selectedCounty.getId(), communeRequest.getCommuneTypeId());
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

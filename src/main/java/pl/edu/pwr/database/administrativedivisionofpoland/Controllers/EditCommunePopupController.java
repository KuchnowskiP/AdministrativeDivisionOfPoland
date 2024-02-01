package pl.edu.pwr.database.administrativedivisionofpoland.Controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Commune.CommuneRequest;
import pl.edu.pwr.contract.Dtos.CommuneDto;
import pl.edu.pwr.contract.Dtos.CountyDto;
import pl.edu.pwr.contract.Dtos.OfficeAddressDto;
import pl.edu.pwr.contract.Dtos.VoivodeshipDto;
import pl.edu.pwr.contract.OfficeAdres.OfficeAddressRequest;
import pl.edu.pwr.database.administrativedivisionofpoland.Data.Services.CountyDataService;
import pl.edu.pwr.database.administrativedivisionofpoland.Data.DataReceiver;
import pl.edu.pwr.database.administrativedivisionofpoland.Data.DataSender;
import pl.edu.pwr.database.administrativedivisionofpoland.Data.Services.VoivodeshipDataService;
import pl.edu.pwr.database.administrativedivisionofpoland.UserData;
import pl.edu.pwr.database.administrativedivisionofpoland.Utils.Utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class EditCommunePopupController implements Initializable {
    public Button closeButton;
    @FXML
    private TextField populationTextField;
    @FXML
    private TextField areaTextField;
    @FXML
    private TextField communeNameTextField;
    @FXML
    private TextField localityTextField;
    @FXML
    private TextField postalCodeTextField;
    @FXML
    private TextField postLocalityTextField;
    @FXML
    private TextField streetTextField;
    @FXML
    private TextField numberOfBuildingTextField;
    @FXML
    private TextField apartmentNumberTextField;
    @FXML
    private Button confirmButton;
    @FXML
    private TabPane addressSelectionTabPane;
    @FXML
    private ChoiceBox communeTypeChoiceBox;
    @FXML
    private ChoiceBox countyChoiceBox;
    @FXML
    private ChoiceBox voivodeshipChoiceBox;
    @FXML
    private TableView existingAddressesTableView;
    @FXML
    private CheckBox chooseExistingAddressCheckBox;
    @FXML
    private CheckBox addNewAddressCheckBox;
    @FXML
    private Label returningLabel;
    @FXML
    private TextField voivodeshipNameTextField;
    @FXML
    private TextField licensePlateDifferentiatorTextField;
    DataSender requestSender = new DataSender();
    DataReceiver requestResultsReceiver = new DataReceiver();
    Integer addressID;
    String place;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTabListeners();
        try {
            setChoiceBoxes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        initializeChoiceBoxesListeners();
    }

    VoivodeshipDataService voivodeshipDataService = new VoivodeshipDataService();
    PageResult<VoivodeshipDto> requestVoivodeships;
    PageResult<CountyDto> requestCounties;
    VoivodeshipDto selectedVoivodeship = new VoivodeshipDto(-1,"","","");
    CountyDto selectedCounty = new CountyDto(-1,-1,"","",false,"","");

    public void setChoiceBoxes() throws Exception {
        requestVoivodeships = Utils.getVoivodeshipResult(voivodeshipChoiceBox);
        communeTypeChoiceBox.getItems().addAll(new Object[]{"gmina miejska", "gmina wiejska", "gmina miejsko-wiejska"});
    }
    CountyDataService countyDataService = new CountyDataService();
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
                            requestCounties = countyDataService.get(selectedVoivodeship.getId(), 1, Integer.MAX_VALUE);
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

    private void initializeTabListeners() {
        addressSelectionTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            confirmButton.setDisable(true);
            if(Objects.equals(newValue.getId(), "1")){
                System.out.println("choosing");
                try {
                    PageResult<OfficeAddressDto> requestResult = requestResultsReceiver.getAddresses(1,Integer.MAX_VALUE);
                    Platform.runLater(() -> {
                        setColumnsInMainTable(OfficeAddressDto.class);
                        existingAddressesTableView.getItems().clear();
                        for(Object o : requestResult.items){
                            existingAddressesTableView.getItems().add(o);
                        }
                        existingAddressesTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
                            @Override
                            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                                if(newValue != null){
                                    try {
                                        confirmButton.setDisable(false);
                                        addressID = (Integer) newValue.getClass().getField("id").get(newValue);
                                        place = newValue.getClass().getField("locality").get(newValue).toString();
                                    } catch (IllegalAccessException | NoSuchFieldException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        });
                        existingAddressesTableView.setVisible(true);
                    });
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if(Objects.equals(newValue.getId(),"2")){
                System.out.println("adding new");
                postalCodeTextField.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                        if(!newValue.matches("[0-9]{2}-[0-9]{3}")){
                            confirmButton.setDisable(true);
                        }else{
                            confirmButton.setDisable(false);
                        }
                    }
                });
            }
        });
        addressSelectionTabPane.getSelectionModel().select(1);
        addressSelectionTabPane.getSelectionModel().select(0);
    }
    public void setColumnsInMainTable(Class<?> passedClass){
        List<TableColumn<?, ?>> columnsToAdd = new ArrayList<>();
        Field[] fields = passedClass.getFields();
        for(Field f : fields){
            columnsToAdd.add(new TableColumn<>(f.getName()));
        }

        int iterator = 0;
        for(TableColumn t : columnsToAdd){
            t.setCellValueFactory(new PropertyValueFactory<>(fields[iterator].getName()));
            iterator++;
        }
        Platform.runLater(() -> {
            existingAddressesTableView.getColumns().clear();
            existingAddressesTableView.getColumns().addAll(columnsToAdd);
        });

    }

    public void onConfirmButtonClick(ActionEvent actionEvent) throws Exception {
        if (communeNameTextField.getText().trim().isEmpty()) {
            returningLabel.setText("Nazwa jest wymagana");
            returningLabel.setVisible(true);
            UserData.confirmed = false;
            return;
        } else {
            returningLabel.setVisible(false);
        }

        UserData.prompt = "\ndodać gminę o nazwie \"" + communeNameTextField.getText() + "\"?";
        UserData.getConfirmation();

        if(UserData.confirmed) {

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
            CommuneDto communeDto = (CommuneDto) UserData.unit;
            if(requestSender.editCommune(communeDto.getId(), communeRequest)){
                returningLabel.setVisible(true);
                returningLabel.setText("Pomyślnie edytowano gminę!");
            }else{
                returningLabel.setVisible(true);
                returningLabel.setText("Nie udało się edytować gminy! Wprowadzono niepoprawne dane lub gmina" +
                        " o podanych atrybutach już istnieje.");
            }
            UserData.confirmed = false;
        }else {
            returningLabel.setVisible(true);
            returningLabel.setText("Nie edytowano gminy, użytkownik przerwał operację!");
        }
    }

    public void onCloseButtonClick(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}

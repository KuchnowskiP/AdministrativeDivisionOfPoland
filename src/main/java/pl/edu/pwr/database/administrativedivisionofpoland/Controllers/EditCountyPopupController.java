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
import pl.edu.pwr.contract.County.CountyRequest;
import pl.edu.pwr.contract.Dtos.CountyDto;
import pl.edu.pwr.contract.Dtos.OfficeAddressDto;
import pl.edu.pwr.contract.Dtos.VoivodeshipDto;
import pl.edu.pwr.contract.OfficeAdres.OfficeAddressRequest;
import pl.edu.pwr.database.administrativedivisionofpoland.Services.Data.DataReceiver;
import pl.edu.pwr.database.administrativedivisionofpoland.Services.Data.DataSender;
import pl.edu.pwr.database.administrativedivisionofpoland.Services.Data.DataService;
import pl.edu.pwr.database.administrativedivisionofpoland.UserData;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class EditCountyPopupController implements Initializable {
    public TextField countyNameTextField;
    public Button closeButton;
    @FXML
    private CheckBox cityRightsCheckBox;
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
    private ChoiceBox voivodeshipChoiceBox;
    @FXML
    private TableView existingAddressesTableView;

    @FXML
    private Label returningLabel;
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

    DataService dataService = new DataService();
    PageResult<VoivodeshipDto> requestVoivodeships;
    VoivodeshipDto selectedVoivodeship = new VoivodeshipDto(-1,"","","");
    CountyDto selectedCounty = new CountyDto(-1,-1,"","",false,"","");

    public void setChoiceBoxes() throws Exception {
        requestVoivodeships = dataService.getVoivodeships(1, Integer.MAX_VALUE);
        voivodeshipChoiceBox.getItems().add("-");
        voivodeshipChoiceBox.setValue("-");
        for(int i = 0; i < requestVoivodeships.items.size(); i++){
            voivodeshipChoiceBox.getItems().add(requestVoivodeships.getItems().get(i).getName());
        }
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
        if (licensePlateDifferentiatorTextField.getText().length() > 2) {
            returningLabel.setText("Wyróżnik musi się składać z maksymalnie dwóch litery");
            returningLabel.setVisible(true);
            UserData.confirmed = false;
            return;
        } else {
            returningLabel.setVisible(false);
        }

        if (countyNameTextField.getText().trim().isEmpty()) {
            returningLabel.setText("Nazwa jest wymagana");
            returningLabel.setVisible(true);
            UserData.confirmed = false;
            return;
        } else {
            returningLabel.setVisible(false);
        }

        UserData.prompt = "\npotwierdzić dane powiatu o nazwie \"" + countyNameTextField.getText() + "\"?";
        UserData.getConfirmation();

        CountyDto countyDto = (CountyDto) UserData.unit;

        if(UserData.confirmed) {
            CountyRequest countyRequest = new CountyRequest();
            countyRequest.setName(countyNameTextField.getText().trim());
            countyRequest.setVoivodeshipId(selectedVoivodeship.getId());
            countyRequest.setLicensePlateDifferentiator(licensePlateDifferentiatorTextField.getText());
            countyRequest.setIsCityWithCountyRights(cityRightsCheckBox.isSelected());
            if(Objects.equals(countyDto.getVoivodeshipId(), selectedVoivodeship.getId()) && selectedVoivodeship.getId() != null){
                countyRequest.setTerytCode(countyDto.getTerytCode());
            }else {
                String newTeryt = requestSender.newCountyTeryt(selectedVoivodeship.getId(), cityRightsCheckBox.isSelected() ? 1 : 0);
                if (newTeryt == null) {
                    int newTerytInt = Integer.parseInt(selectedVoivodeship.getTerytCode());
                    newTerytInt += 1000;
                    newTeryt += cityRightsCheckBox.isSelected() ? 1 : 0;
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
                HttpResponse<String> responseWithIdAsABody = requestSender.addAddress(newAddress);

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
            UserData.confirmed = false;
        }else {
            returningLabel.setVisible(true);
            returningLabel.setText("Nie dodano powiatu, użytkownik przerwał operację!");
        }
    }

    public void onCloseButtonClick(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}

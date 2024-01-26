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
import pl.edu.pwr.contract.Dtos.OfficeAddressDto;
import pl.edu.pwr.contract.OfficeAdres.OfficeAddressRequest;
import pl.edu.pwr.contract.Voivodeship.VoivodeshipRequest;
import pl.edu.pwr.database.administrativedivisionofpoland.UserData;
import pl.edu.pwr.database.administrativedivisionofpoland.Services.Data.DataReceiver;
import pl.edu.pwr.database.administrativedivisionofpoland.Services.Data.DataSender;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;


public class AddVoivodeshipPopupController implements Initializable{
    public Button closeButton;
    public Label nameLabel;
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
    @FXML
    private Button confirmButton;
    @FXML
    private TabPane addressSelectionTabPane;
    @FXML
    private TextField postalCodeTextField;
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
        if (licensePlateDifferentiatorTextField.getText().length() > 1) {
            returningLabel.setText("Wyróżnik musi się składać z jednej litery");
            returningLabel.setVisible(true);
            UserData.confirmed = false;
            return;
        } else {
            returningLabel.setVisible(false);
        }

        if (voivodeshipNameTextField.getText().trim().isEmpty()) {
            returningLabel.setText("Nazwa jest wymagana");
            returningLabel.setVisible(true);
            UserData.confirmed = false;
            return;
        } else {
            returningLabel.setVisible(false);
        }

        UserData.prompt = "\ndodać województwo o nazwie \"" + voivodeshipNameTextField.getText() + "\"?";
        UserData.getConfirmation();

        if(UserData.confirmed) {
            VoivodeshipRequest voivodeshipRequest = new VoivodeshipRequest();
            voivodeshipRequest.setName(voivodeshipNameTextField.getText().trim());
            voivodeshipRequest.setLicensePlateDifferentiator(licensePlateDifferentiatorTextField.getText());
            voivodeshipRequest.setTerytCode(requestSender.newVoivodeshipTeryt());

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

            if(requestSender.addVoivodeship(voivodeshipRequest)){
                returningLabel.setVisible(true);
                returningLabel.setText("Pomyślnie dodano nowe województwo!");
            }else{
                returningLabel.setVisible(true);
                returningLabel.setText("Nie udało się dodać województwa! Wprowadzono niepoprawne dane lub województwo" +
                        " o podanych atrybutach już istnieje.");
            }
            UserData.confirmed = false;
        }else {
            returningLabel.setVisible(true);
            returningLabel.setText("Nie dodano województwa, użytkownik przerwał operację!");
        }
    }

    public void onCloseButtonClick(ActionEvent actionEvent) throws IOException, InterruptedException {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}

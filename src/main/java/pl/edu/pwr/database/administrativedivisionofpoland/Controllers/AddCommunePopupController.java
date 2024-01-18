package pl.edu.pwr.database.administrativedivisionofpoland.Controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.CommuneDto;
import pl.edu.pwr.contract.Dtos.CountyDto;
import pl.edu.pwr.contract.Dtos.OfficeAddressDto;
import pl.edu.pwr.contract.Dtos.VoivodeshipDto;
import pl.edu.pwr.contract.Voivodeship.VoivodeshipRequest;
import pl.edu.pwr.database.administrativedivisionofpoland.Request;
import pl.edu.pwr.database.administrativedivisionofpoland.RequestResultsReceiver;
import pl.edu.pwr.database.administrativedivisionofpoland.RequestSender;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddCommunePopupController implements Initializable {
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
    private ComboBox communeTypeChoiceBox;
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
    RequestSender requestSender = new RequestSender();
    RequestResultsReceiver requestResultsReceiver = new RequestResultsReceiver();
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

    Request request = new Request();
    PageResult<VoivodeshipDto> requestVoivodeships;
    PageResult<CountyDto> requestCounties;
    VoivodeshipDto selectedVoivodeship = new VoivodeshipDto(-1,"","","");
    CountyDto selectedCounty = new CountyDto(-1,-1,"","",false,"","");

    public void setChoiceBoxes() throws Exception {
        requestVoivodeships = request.getVoivodeships(1, Integer.MAX_VALUE);
        voivodeshipChoiceBox.getItems().add("-");
        voivodeshipChoiceBox.setValue("-");
        for(int i = 0; i < requestVoivodeships.items.size(); i++){
            voivodeshipChoiceBox.getItems().add(requestVoivodeships.getItems().get(i).getName());
        }
        communeTypeChoiceBox.getItems().addAll(new Object[]{"gmina miejska", "gmina wiejska", "gmina miejsko-wiejska"});
    }

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
                            requestCounties = request.getCounties(selectedVoivodeship.getId(), 1, Integer.MAX_VALUE);
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

    public void onConfirmButtonClick(ActionEvent actionEvent) throws IOException, InterruptedException, IllegalAccessException {


//        AddVoivodeshipRequest addVoivodeshipRequest = new AddVoivodeshipRequest();
//        addVoivodeshipRequest.setName(voivodeshipNameTextField.getText().trim());
//        addVoivodeshipRequest.setLicensePlateDifferentiator(licensePlateDifferentiatorTextField.getText());
//        addVoivodeshipRequest.setTERYTCode("0000001");
//        if(addNewAddressCheckBox.isSelected()){
//            addVoivodeshipRequest.setLocalityFirst("Kraków");
//            addVoivodeshipRequest.setIsSeatOfCouncilFirst(true);
//            addVoivodeshipRequest.setIsSeatOfVoivodeFirst(true);
//            addVoivodeshipRequest.setRegisteredOfficeAddressesIdFirst(999);
//        }else{
//            addVoivodeshipRequest.setLocalityFirst(place);
//            addVoivodeshipRequest.setIsSeatOfCouncilFirst(true);
//            addVoivodeshipRequest.setIsSeatOfVoivodeFirst(true);
//            addVoivodeshipRequest.setRegisteredOfficeAddressesIdFirst(addressID);
//        }

        RequestSender.createCommune();
    }

    public void onCancelButtonClick(ActionEvent actionEvent) {
//        Stage stage = new Stage();
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Wybierz sb byczku");
//        fileChooser.showOpenDialog(stage);
    }
}
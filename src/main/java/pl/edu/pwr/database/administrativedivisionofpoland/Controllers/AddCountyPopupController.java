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
import pl.edu.pwr.contract.Dtos.OfficeAddressDto;
import pl.edu.pwr.contract.Voivodeship.VoivodeshipRequest;
import pl.edu.pwr.database.administrativedivisionofpoland.RequestResultsReceiver;
import pl.edu.pwr.database.administrativedivisionofpoland.RequestSender;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddCountyPopupController implements Initializable {
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
        initializeCheckBoxesListeners();
    }

    private void initializeCheckBoxesListeners() {
        chooseExistingAddressCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    addNewAddressCheckBox.setSelected(false);
                    try {
                        PageResult<OfficeAddressDto> requestResult = requestResultsReceiver.getAddresses(1, Integer.MAX_VALUE);
                        Platform.runLater(() -> {
                            setColumnsInMainTable(OfficeAddressDto.class);
                            existingAddressesTableView.getItems().clear();
                            for (Object o : requestResult.items) {
                                existingAddressesTableView.getItems().add(o);
                            }
                            existingAddressesTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
                                @Override
                                public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                                    if (newValue != null) {
                                        try {
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
                } else{
                    existingAddressesTableView.setVisible(false);
                }
            }
        });
        addNewAddressCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    chooseExistingAddressCheckBox.setSelected(false);
                    existingAddressesTableView.setVisible(false);
                }
            }
        });
    }

    public void setColumnsInMainTable(Class<?> passedClass) {
        List<TableColumn<?, ?>> columnsToAdd = new ArrayList<>();
        Field[] fields = passedClass.getFields();
        for (Field f : fields) {
            columnsToAdd.add(new TableColumn<>(f.getName()));
        }

        int iterator = 0;
        for (TableColumn t : columnsToAdd) {
            t.setCellValueFactory(new PropertyValueFactory<>(fields[iterator].getName()));
            iterator++;
        }
        Platform.runLater(() -> {
            existingAddressesTableView.getColumns().clear();
            existingAddressesTableView.getColumns().addAll(columnsToAdd);
        });

    }

    public void onConfirmButtonClick(ActionEvent actionEvent) throws IOException, InterruptedException, IllegalAccessException {
        if (licensePlateDifferentiatorTextField.getText().length() > 2) {
            returningLabel.setText("Wyróżnik musi się składać z jednej litery");
            returningLabel.setVisible(true);
        } else {
            returningLabel.setVisible(false);
        }
//        AddVoivodeshipRequest addVoivodeshipRequest = new AddVoivodeshipRequest();
//        addVoivodeshipRequest.setName(voivodeshipNameTextField.getText().trim());
//        addVoivodeshipRequest.setLicensePlateDifferentiator(licensePlateDifferentiatorTextField.getText());
//        addVoivodeshipRequest.setTERYTCode("0000001");
//        if (addNewAddressCheckBox.isSelected()) {
//            addVoivodeshipRequest.setLocalityFirst("Kraków");
//            addVoivodeshipRequest.setIsSeatOfCouncilFirst(true);
//            addVoivodeshipRequest.setIsSeatOfVoivodeFirst(true);
//            addVoivodeshipRequest.setRegisteredOfficeAddressesIdFirst(999);
//        } else {
//            addVoivodeshipRequest.setLocalityFirst(place);
//            addVoivodeshipRequest.setIsSeatOfCouncilFirst(true);
//            addVoivodeshipRequest.setIsSeatOfVoivodeFirst(true);
//            addVoivodeshipRequest.setRegisteredOfficeAddressesIdFirst(addressID);
//        }

        RequestSender.createCounty();
    }

    public void onCancelButtonClick(ActionEvent actionEvent) {
//        Stage stage = new Stage();
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Wybierz sb byczku");
//        fileChooser.showOpenDialog(stage);
    }
}
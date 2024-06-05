package pl.edu.pwr.database.administrativedivisionofpoland.Controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.OfficeAddressDto;
import pl.edu.pwr.database.administrativedivisionofpoland.Data.DataReceiver;
import pl.edu.pwr.database.administrativedivisionofpoland.Data.DataSender;
import pl.edu.pwr.database.administrativedivisionofpoland.Data.IDataSender;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class BaseUnitPopupController {
    @FXML
    protected TabPane addressSelectionTabPane;
    @FXML
    protected Button confirmButton;
    @FXML
    protected TableView existingAddressesTableView;
    @FXML
    protected Button closeButton;
    @FXML
    protected TextField postalCodeTextField;
    protected Integer addressID;
    protected String place;
    DataReceiver requestResultsReceiver = new DataReceiver();
    IDataSender requestSender = new DataSender();

    public void initializeTabPaneListeners(){
        addressSelectionTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            confirmButton.setDisable(true);
            if(Objects.equals(newValue.getId(), "1")){
                System.out.println("choosing");
                try {
                    PageResult<OfficeAddressDto> requestResult = requestResultsReceiver.getAddresses(1,Integer.MAX_VALUE);
                    Platform.runLater(() -> {
                        setColumnsInMainTable(OfficeAddressDto.class, existingAddressesTableView);
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

    public void setColumnsInMainTable(Class<?> passedClass, TableView passedTableView){
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
            passedTableView.getColumns().clear();
            passedTableView.getColumns().addAll(columnsToAdd);
        });

    }

    public void onCloseButtonClick(ActionEvent actionEvent) throws IOException, InterruptedException {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}

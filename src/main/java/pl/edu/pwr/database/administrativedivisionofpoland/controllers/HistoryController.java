package pl.edu.pwr.database.administrativedivisionofpoland.controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.History.CommuneHistoryDto;
import pl.edu.pwr.contract.History.CountyHistoryDto;
import pl.edu.pwr.contract.History.VoivodeshipHistoryDto;
import pl.edu.pwr.database.administrativedivisionofpoland.data.api.IResultFetcher;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HistoryController implements Initializable {
    @FXML private TabPane viewUnitsTabPane;
    @FXML private TableView<VoivodeshipHistoryDto> voivodeshipsTable = new TableView<>();
    @FXML private TableView<CountyHistoryDto> countiesTable = new TableView<>();
    @FXML private TableView<CommuneHistoryDto> communesTable = new TableView<>();
    TableView[] tables;
    int activeTable = 0;
    Class<?>[] units = new Class[]{VoivodeshipHistoryDto.class, CountyHistoryDto.class, CommuneHistoryDto.class};
    ChangeListener<?>[] currentlyActiveTableListeners;
    Thread tableUpdater = new Thread();
    IResultFetcher resultFetcher;

    public HistoryController(IResultFetcher resultFetcher) {
        this.resultFetcher = resultFetcher;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out),
                true, StandardCharsets.UTF_8)); //needed due to polish diacritics
        try {
            setInitialView();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        initializeStructures();
        TabPaneListenerInitializer(viewUnitsTabPane);
    }
    public void setInitialView() {
        changeView(); //setting content of table
    }
    public void changeView(){
        tableUpdater.interrupt();
        Runnable updateTable = this::changeItemsInMainTable;
        tableUpdater = new Thread(updateTable);
        tableUpdater.start();
    }
    public void initializeStructures(){
        tables = new TableView<?>[]{voivodeshipsTable, countiesTable, communesTable};
        currentlyActiveTableListeners = new ChangeListener[2];
    }
    public void setColumnsInMainTable(Class<?> passedClass){
        List<TableColumn<?, ?>> columnsToAdd = new ArrayList<>();
        Field[] fields = passedClass.getFields();
        for(Field f : fields){
            columnsToAdd.add(new TableColumn<>(f.getName()));
        }

        int iterator = 0;
        for(TableColumn<?,?> t : columnsToAdd){
            t.setCellValueFactory(new PropertyValueFactory<>(fields[iterator].getName()));
            iterator++;
        }
        Platform.runLater(() -> {
            tables[activeTable].getColumns().clear();
            tables[activeTable].getColumns().addAll(columnsToAdd);
        });
    }
    public void changeItemsInMainTable(){
        try {
            PageResult<?> requestResult = resultFetcher.getHistoryResult(activeTable);
            setColumnsInMainTable(units[activeTable]);
            Platform.runLater(() -> {
                tables[activeTable].getItems().clear();
                for(Object o : requestResult.items){
                    tables[activeTable].getItems().add(o);
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void TabPaneListenerInitializer(TabPane TabPane) {
        TabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(oldValue.getId() + " -> " + newValue.getId());
            activeTable = Integer.parseInt(newValue.getId());
            changeView();
        });
    }

    public void onRefreshButtonClick(ActionEvent ignoredActionEvent) {
        changeView();
    }
}

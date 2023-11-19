package pl.edu.pwr.database.administrativedivisionofpoland;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;

public class MainController implements Initializable {
    @FXML
    TableView<AdministrativeUnit> voivodeshipsTable = new TableView<AdministrativeUnit>();
    TableView<AdministrativeUnit> countiesTable = new TableView<AdministrativeUnit>();
    TableView<AdministrativeUnit> communesTable = new TableView<AdministrativeUnit>();
    TableView<AdministrativeUnit>[] tables;
    @FXML
    TableColumn<AdministrativeUnit, String> nameColumn;
    @FXML
    TableColumn<AdministrativeUnit, Integer> populationColumn;
    String path = "\\src\\main\\resources\\pl\\edu\\pwr\\database\\administrativedivisionofpoland\\";
    String[] unitsTree = new String[3];
    int unitsTreeIndex = 0;
    int activeTable = 0;

    public void changeItemsInMainList(String filename){
        nameColumn = new TableColumn<AdministrativeUnit, String>("Nazwa");
        nameColumn.setCellValueFactory(new PropertyValueFactory<AdministrativeUnit, String>("name"));
        populationColumn = new TableColumn<AdministrativeUnit, Integer>("Liczba ludności");
        populationColumn.setCellValueFactory(new PropertyValueFactory<AdministrativeUnit, Integer>("population"));
        tables[activeTable].setEditable(true);
        try {
            File myFile = new File(System.getProperty("user.dir") + path + filename);
            Scanner myScanner = new Scanner(myFile, StandardCharsets.UTF_8);
            tables[activeTable].getColumns().clear();
            tables[activeTable].getColumns().addAll(nameColumn, populationColumn);
            tables[activeTable].getItems().clear();
            String line;
            while(myScanner.hasNextLine()){
                line = myScanner.nextLine();
                tables[activeTable].getItems().add(new AdministrativeUnit(line, new Random().nextInt(21000)));
            }
            myScanner.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tables = new TableView[]{voivodeshipsTable, countiesTable, communesTable};
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));

        changeItemsInMainList("voivodeships.txt");
        unitsTree[0] = "voivodeships";

        voivodeshipsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AdministrativeUnit>() {
            @Override
            public void changed(ObservableValue<? extends AdministrativeUnit> observable, AdministrativeUnit oldValue, AdministrativeUnit newValue) {
                if(newValue != null) {
                    if(unitsTreeIndex < 2) {
                        unitsTreeIndex++;
                    }
                    unitsTree[unitsTreeIndex] = newValue.getName();
                    System.out.println("Selected item: " + unitsTree[unitsTreeIndex]);
                    Platform.runLater(() -> {
                        changeItemsInMainList(unitsTree[unitsTreeIndex] + ".txt");
                    });
                }
            }
        });

    }

    public void onBackButtonClick(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            if(unitsTreeIndex > 0) {
                unitsTreeIndex--;
            }
            System.out.println("Got back to: " + unitsTree[unitsTreeIndex]);
            changeItemsInMainList(unitsTree[unitsTreeIndex] + ".txt");
        });
    }
}
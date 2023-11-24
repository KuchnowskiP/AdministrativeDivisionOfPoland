package pl.edu.pwr.database.administrativedivisionofpoland;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;

public class MainController implements Initializable {
    public ChoiceBox voivodeshipReportChoiceBox;
    public ChoiceBox countyReportChoiceBox;
    public ChoiceBox communeReportChoiceBox;
    @FXML
    private TableView countiesTableManage;
    @FXML
    private TableView communesTableManage;
    @FXML
    private TableView voivodeshipsTableManage;
    @FXML
    private TabPane manageUnitsTabPane;
    @FXML
    private Tab communesTab;
    @FXML
    private Tab countiesTab;
    @FXML
    private Tab voivodeshipsTab;
    @FXML
    private TabPane manageTabPane;
    @FXML
    private Label loginFeedbackLabel;
    @FXML
    private TabPane viewUnitsTabPane;
    @FXML
    private TextField loginTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private TabPane mainTabPane;
    @FXML
    private Tab manageTab;
    @FXML
    private Tab viewTab;
    @FXML
    private TableView<AdministrativeUnit> voivodeshipsTable = new TableView<AdministrativeUnit>();
    @FXML
    private TableView<AdministrativeUnit> countiesTable = new TableView<AdministrativeUnit>();
    @FXML
    private TableView<AdministrativeUnit> communesTable = new TableView<AdministrativeUnit>();
    private TableView<AdministrativeUnit>[][] tables;
    @FXML
    TableColumn<AdministrativeUnit, String> nameColumn;
    @FXML
    TableColumn<AdministrativeUnit, Integer> populationColumn;
    String path = "\\src\\main\\resources\\pl\\edu\\pwr\\database\\administrativedivisionofpoland\\";
    String[] unitsTree = new String[3];
    String[] tabChangeStarters = new String[3];
    int[] unitsTreeIndexes = new int[2];
    int[] activeTables = new int[]{0,0};
    int maxDepth = 2;

    ChangeListener<AdministrativeUnit>[] currentlyActiveTableListeners;

    public void changeListener(int oldTab, int newTab, int viewOrManage){
        boolean changed = false;
        if(oldTab != -1){
            System.out.println("zmiana");
            tables[viewOrManage][oldTab].getSelectionModel().selectedItemProperty().removeListener(currentlyActiveTableListeners[viewOrManage]);
            changed = true;
            unitsTree[0] = tabChangeStarters[newTab];
        }
        unitsTreeIndexes[viewOrManage] = 0;
        boolean finalChanged = changed;
        if(finalChanged){
            unitsTreeIndexes[viewOrManage] = 0;
            activeTables[viewOrManage] = newTab;
            Platform.runLater(() -> {
                changeItemsInMainList(tabChangeStarters[newTab], viewOrManage);
            });
        }
            currentlyActiveTableListeners[viewOrManage] = new ChangeListener<AdministrativeUnit>() {
                @Override
                public void changed(ObservableValue<? extends AdministrativeUnit> observable, AdministrativeUnit oldValue, AdministrativeUnit newValue) {
                    if(newValue != null) {
                        if(unitsTreeIndexes[viewOrManage] < (maxDepth - newTab)) {
                            unitsTreeIndexes[viewOrManage]++;
                            unitsTree[unitsTreeIndexes[viewOrManage]] = newValue.getName();
                            System.out.println(unitsTree[unitsTreeIndexes[viewOrManage]]);
                            System.out.println("Selected item: " + unitsTree[unitsTreeIndexes[viewOrManage]]);
                            Platform.runLater(() -> {
                                changeItemsInMainList(unitsTree[unitsTreeIndexes[viewOrManage]], viewOrManage);
                            });
                        }
                    }
                }
            };
        tables[viewOrManage][newTab].getSelectionModel().selectedItemProperty().addListener(currentlyActiveTableListeners[viewOrManage]);
    }

    public void setColumns(String filename, int viewOrManage){
        nameColumn = new TableColumn<AdministrativeUnit, String>("Nazwa");
        nameColumn.setCellValueFactory(new PropertyValueFactory<AdministrativeUnit, String>("name"));
        populationColumn = new TableColumn<AdministrativeUnit, Integer>("Przykładowe dane");
        populationColumn.setCellValueFactory(new PropertyValueFactory<AdministrativeUnit, Integer>("population"));
        tables[viewOrManage][activeTables[viewOrManage]].getColumns().clear();
        if(unitsTreeIndexes[viewOrManage] == 1 && activeTables[viewOrManage] == 0){
            TableColumn master = new TableColumn<>("Powiaty w " + "'" + filename + "'");
            master.getColumns().addAll(nameColumn,populationColumn);
            tables[viewOrManage][activeTables[viewOrManage]].getColumns().add(master);
        }else if(unitsTreeIndexes[viewOrManage] == 2 && activeTables[viewOrManage] == 0 || unitsTreeIndexes[viewOrManage] == 1 && activeTables[viewOrManage] == 1){
            TableColumn master = new TableColumn<>("Gminy w " + "'" + filename + "'");
            master.getColumns().addAll(nameColumn,populationColumn);
            tables[viewOrManage][activeTables[viewOrManage]].getColumns().add(master);
        }else{
            tables[viewOrManage][activeTables[viewOrManage]].getColumns().addAll(nameColumn, populationColumn);
        }
    }

    public void changeItemsInMainList(String filename, int viewOrManage){
        try {
            File myFile = new File(System.getProperty("user.dir") + path + filename + ".txt");
            Scanner myScanner = new Scanner(myFile, StandardCharsets.UTF_8);
            setColumns(filename, viewOrManage);
            tables[viewOrManage][activeTables[viewOrManage]].getItems().clear();
            String line;
            while(myScanner.hasNextLine()){
                line = myScanner.nextLine();
                tables[viewOrManage][activeTables[viewOrManage]].getItems().add(new AdministrativeUnit(line, new Random().nextInt(21000)));
            }
            myScanner.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        passwordTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent key) {
                if(key.getCode().equals(KeyCode.ENTER)){
                    onLoginButtonClick(new ActionEvent());
                }
            }
        });
        mainTabPane.getTabs().remove(manageTab);
        tables = new TableView[][]{{voivodeshipsTable, countiesTable, communesTable},{voivodeshipsTableManage, countiesTableManage, communesTableManage}};
        tabChangeStarters = new String[]{"voivodeships","counties","communes"};
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));

        currentlyActiveTableListeners = new ChangeListener[2];
        currentlyActiveTableListeners[0] = new ChangeListener<AdministrativeUnit>() {
            @Override
            public void changed(ObservableValue<? extends AdministrativeUnit> observable, AdministrativeUnit oldValue, AdministrativeUnit newValue) {

            }
        };
        currentlyActiveTableListeners[1] = new ChangeListener<AdministrativeUnit>() {
            @Override
            public void changed(ObservableValue<? extends AdministrativeUnit> observable, AdministrativeUnit oldValue, AdministrativeUnit newValue) {

            }
        };
        changeItemsInMainList("voivodeships",0);
        unitsTree[0] = tabChangeStarters[0];
        changeListener(-1,0,0);
        TabPaneListenerInitializer(viewUnitsTabPane, 0);


        try {
            voivodeshipReportChoiceBox.getItems().add("-");
            voivodeshipReportChoiceBox.setValue("-");
            voivodeshipReportChoiceBox.getItems().addAll(Files.readAllLines(Path.of(System.getProperty("user.dir") + path + "voivodeships.txt")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        voivodeshipReportChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                System.out.println(newValue);
                if(Objects.equals(String.valueOf(newValue), "dolnośląskie")){
                    try {
                        countyReportChoiceBox.setDisable(false);
                        countyReportChoiceBox.getItems().add("-");
                        countyReportChoiceBox.setValue("-");
                        countyReportChoiceBox.getItems().addAll(Files.readAllLines(Path.of(System.getProperty("user.dir") + path + "dolnośląskie.txt")));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                if(Objects.equals(String.valueOf(newValue), "-")){
                    countyReportChoiceBox.setValue("-");
                    countyReportChoiceBox.getItems().clear();
                    countyReportChoiceBox.setDisable(true);
                }
            }
        });

        countyReportChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if(Objects.equals(String.valueOf(newValue), "wrocławski")){
                    try {
                        communeReportChoiceBox.setDisable(false);
                        communeReportChoiceBox.getItems().add("-");
                        communeReportChoiceBox.setValue("-");
                        communeReportChoiceBox.getItems().addAll(Files.readAllLines(Path.of(System.getProperty("user.dir") + path + "wrocławski.txt")));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                if(Objects.equals(String.valueOf(newValue), "-")){
                    communeReportChoiceBox.getItems().clear();
                    communeReportChoiceBox.setDisable(true);
                }
            }
        });
    }

    public void onBackButtonClick(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            if(unitsTreeIndexes[0] > 0) {
                unitsTreeIndexes[0]--;
            }
            System.out.println("Wrócono do: " + unitsTree[unitsTreeIndexes[0]]);
            changeItemsInMainList(unitsTree[unitsTreeIndexes[0]],0);
        });
    }

    public void onLoginButtonClick(ActionEvent actionEvent) {
        System.out.println("przycisk wciśnięty");
        String login = loginTextField.getText();
        String password = passwordTextField.getText();
        if(login.equals("admin") && password.equals("admin")){

            TabPaneListenerInitializer(manageUnitsTabPane, 1);
            voivodeshipsTableManage.setEditable(true);
            countiesTableManage.setEditable(true);
            communesTable.setEditable(true);

            changeListener(-1, 0, 1);

            Platform.runLater(() -> {
                loginFeedbackLabel.setText("Zalogowano jako " + loginTextField.getText());
                loginFeedbackLabel.setVisible(true);
                mainTabPane.getTabs().add(manageTab);
            });
        }else{
            loginFeedbackLabel.setText("Błędne dane logowania!");
            loginFeedbackLabel.setVisible(true);
        }
    }

    private void TabPaneListenerInitializer(TabPane TabPane, int viewOrManage) {
        TabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                System.out.println(oldValue.getId() + " -> " + newValue.getId());
                changeListener(Integer.parseInt(oldValue.getId()), Integer.parseInt(newValue.getId()), viewOrManage);
            }
        });
    }

    public void onManageBackButtonClick(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            if(unitsTreeIndexes[1] > 0) {
                unitsTreeIndexes[1]--;
            }
            System.out.println("Wrócono do: " + unitsTree[unitsTreeIndexes[1]]);
            changeItemsInMainList(unitsTree[unitsTreeIndexes[1]],1);
        });
    }
}
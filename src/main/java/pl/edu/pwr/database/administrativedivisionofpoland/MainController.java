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
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;

public class MainController implements Initializable {
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
    private TableView<AdministrativeUnit>[] tables;
    @FXML
    TableColumn<AdministrativeUnit, String> nameColumn;
    @FXML
    TableColumn<AdministrativeUnit, Integer> populationColumn;
    String path = "\\src\\main\\resources\\pl\\edu\\pwr\\database\\administrativedivisionofpoland\\";
    String[] unitsTree = new String[3];
    String[] tabChangeStarters = new String[3];
    int unitsTreeIndex;
    int activeTable = 0;
    int maxDepth = 2;

    ChangeListener<AdministrativeUnit> currentlyActiveListener;

    public void changeListener(int oldTab, int newTab){
        boolean changed = false;
        if(oldTab != -1){
            System.out.println("zmiana");
            tables[oldTab].getSelectionModel().selectedItemProperty().removeListener(currentlyActiveListener);
            changed = true;

            unitsTree[0] = tabChangeStarters[newTab];

        }
        unitsTreeIndex = 0;
        boolean finalChanged = changed;
        if(finalChanged){
            unitsTreeIndex = 0;
            activeTable = newTab;
            Platform.runLater(() -> {
                changeItemsInMainList(tabChangeStarters[newTab]);
            });
        }
            currentlyActiveListener = new ChangeListener<AdministrativeUnit>() {
                @Override
                public void changed(ObservableValue<? extends AdministrativeUnit> observable, AdministrativeUnit oldValue, AdministrativeUnit newValue) {
                    if(newValue != null) {
                        if(unitsTreeIndex < (maxDepth - newTab)) {
                            unitsTreeIndex++;
                            unitsTree[unitsTreeIndex] = newValue.getName();
                            System.out.println(unitsTree[unitsTreeIndex]);
                            System.out.println("Selected item: " + unitsTree[unitsTreeIndex]);
                            Platform.runLater(() -> {
                                changeItemsInMainList(unitsTree[unitsTreeIndex]);
                            });
                        }
                    }
                }
            };
        tables[newTab].getSelectionModel().selectedItemProperty().addListener(currentlyActiveListener);
    }

    public void setColumns(String filename){
        nameColumn = new TableColumn<AdministrativeUnit, String>("Nazwa");
        nameColumn.setCellValueFactory(new PropertyValueFactory<AdministrativeUnit, String>("name"));
        populationColumn = new TableColumn<AdministrativeUnit, Integer>("Przykładowe dane");
        populationColumn.setCellValueFactory(new PropertyValueFactory<AdministrativeUnit, Integer>("population"));
        tables[activeTable].getColumns().clear();
        if(unitsTreeIndex == 1 && activeTable == 0){
            TableColumn master = new TableColumn<>("Powiaty w " + "'" + filename + "'");
            master.getColumns().addAll(nameColumn,populationColumn);
            tables[activeTable].getColumns().add(master);
        }else if(unitsTreeIndex == 2 && activeTable == 0 || unitsTreeIndex == 1 && activeTable == 1){
            TableColumn master = new TableColumn<>("Gminy w " + "'" + filename + "'");
            master.getColumns().addAll(nameColumn,populationColumn);
            tables[activeTable].getColumns().add(master);
        }else{
            tables[activeTable].getColumns().addAll(nameColumn, populationColumn);
        }
    }

    public void changeItemsInMainList(String filename){
        try {
            File myFile = new File(System.getProperty("user.dir") + path + filename + ".txt");
            Scanner myScanner = new Scanner(myFile, StandardCharsets.UTF_8);
            setColumns(filename);
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
        viewUnitsTabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                System.out.println(oldValue.getId() + " -> " + newValue.getId());
                changeListener(Integer.parseInt(oldValue.getId()), Integer.parseInt(newValue.getId()));
            }
        });

        passwordTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent key) {
                if(key.getCode().equals(KeyCode.ENTER)){
                    onLoginButtonClick(new ActionEvent());
                }
            }
        });

        mainTabPane.getTabs().remove(manageTab);
        tables = new TableView[]{voivodeshipsTable, countiesTable, communesTable};
        tabChangeStarters = new String[]{"voivodeships","counties","communes"};
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));

        changeItemsInMainList("voivodeships");
        unitsTree[0] = tabChangeStarters[0];

        changeListener(-1,0);

    }

    public void onBackButtonClick(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            if(unitsTreeIndex > 0) {
                unitsTreeIndex--;
            }
            System.out.println("Wrócono do: " + unitsTree[unitsTreeIndex]);
            changeItemsInMainList(unitsTree[unitsTreeIndex]);
        });
    }

    public void onLoginButtonClick(ActionEvent actionEvent) {
        System.out.println("przycisk wciśnięty");
        String login = loginTextField.getText();
        String password = passwordTextField.getText();
        if(login.equals("admin") && password.equals("admin")){
            Platform.runLater(() -> {
                loginFeedbackLabel.setText("Zalogowano jako " + loginTextField.getText());
                loginFeedbackLabel.setVisible(true);
            });

            Platform.runLater(() -> {
                mainTabPane.getTabs().add(manageTab);
            });
        }else{
            loginFeedbackLabel.setText("Błędne dane logowania!");
            loginFeedbackLabel.setVisible(true);
        }
    }
}
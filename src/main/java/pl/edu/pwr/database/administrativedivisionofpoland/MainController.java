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
import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.CommuneDto;
import pl.edu.pwr.contract.Dtos.CountyDto;
import pl.edu.pwr.contract.Dtos.VoivodeshipDto;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

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
    private TableView voivodeshipsTable = new TableView<>();
    @FXML
    private TableView countiesTable = new TableView<>();
    @FXML
    private TableView communesTable = new TableView<>();
    private TableView[][] tables;
    String path = "\\src\\main\\resources\\pl\\edu\\pwr\\database\\administrativedivisionofpoland\\";
    Object[] unitsTree = new Object[]{-1,-1,-1};
    int[] unitsTreeIndexes = new int[2];
    int[] activeTables = new int[]{0,0};
    int maxDepth = 2;
    String masterName;
    Request request = new Request();
    Class<?>[] units = new Class[]{VoivodeshipDto.class, CountyDto.class, CommuneDto.class};
    ChangeListener<?>[] currentlyActiveTableListeners;

    public void changeListener(int oldTab, int newTab, int viewOrManage){  //nasłuchiwacz zmiany zakładki np z województw na powiaty
        boolean changed = false;
        if(oldTab != -1){
            System.out.println("zmiana");
            tables[viewOrManage][oldTab].getSelectionModel().selectedItemProperty().removeListener(currentlyActiveTableListeners[viewOrManage]);
            changed = true;
        }
        unitsTreeIndexes[viewOrManage] = 0;
        boolean finalChanged = changed;
        if(finalChanged){
            unitsTreeIndexes[viewOrManage] = 0;
            activeTables[viewOrManage] = newTab;
            Platform.runLater(() -> {
                changeItemsInMainList(unitsTree[unitsTreeIndexes[viewOrManage]], viewOrManage);
            });
        }
            currentlyActiveTableListeners[viewOrManage] = new ChangeListener<>() {
                @Override
                public void changed(ObservableValue<?> observableValue, Object oldValue, Object newValue) {
                    if(newValue != null) {
                        if(unitsTreeIndexes[viewOrManage] < (maxDepth - newTab)) {
                            unitsTreeIndexes[viewOrManage]++;
                            try {
                                unitsTree[unitsTreeIndexes[viewOrManage]] = newValue.getClass().getField("id").get(newValue);
                                masterName = newValue.getClass().getField("name").get(newValue).toString();
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            } catch (NoSuchFieldException e) {
                                throw new RuntimeException(e);
                            }
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

    public void setColumns(Class<?> passedClass, int viewOrManage){
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

        tables[viewOrManage][activeTables[viewOrManage]].getColumns().clear();
        if(unitsTreeIndexes[viewOrManage] == 1 && activeTables[viewOrManage] == 0){
            TableColumn master = new TableColumn<>("Powiaty w " + "'" + masterName  + "'");
            master.getColumns().addAll(columnsToAdd);
            tables[viewOrManage][activeTables[viewOrManage]].getColumns().add(master);
        }else if(unitsTreeIndexes[viewOrManage] == 2 && activeTables[viewOrManage] == 0 || unitsTreeIndexes[viewOrManage] == 1 && activeTables[viewOrManage] == 1){
            TableColumn master = new TableColumn<>("Gminy w " + "'" + masterName +  "'");
            master.getColumns().addAll(columnsToAdd);
            tables[viewOrManage][activeTables[viewOrManage]].getColumns().add(master);
        }else{
            tables[viewOrManage][activeTables[viewOrManage]].getColumns().addAll(columnsToAdd);
        }
    }

    public void changeItemsInMainList(Object id, int viewOrManage){
        try {
            PageResult<?> requestResult = new PageResult<>();
            switch (activeTables[viewOrManage]){
                case 0:{
                    switch (unitsTreeIndexes[viewOrManage]){
                        case 1:{
                            requestResult = request.getCounties(unitsTree[unitsTreeIndexes[viewOrManage]],1,Integer.MAX_VALUE);
                            break;
                        }
                        case 2:{
                            requestResult = request.getCommunes(unitsTree[unitsTreeIndexes[viewOrManage]], 1,Integer.MAX_VALUE);
                            break;
                        }
                        default:{
                            requestResult = request.getVoivodeships(1, Integer.MAX_VALUE);
                            break;
                        }
                    }
                    break;
                }
                case 1:{

                    if(unitsTreeIndexes[viewOrManage] == 1){
                        requestResult = request.getCommunes(unitsTree[unitsTreeIndexes[viewOrManage]], 1,Integer.MAX_VALUE);
                    }
                    else{
                        requestResult = request.getCounties(unitsTree[unitsTreeIndexes[viewOrManage]],1,Integer.MAX_VALUE);
                    }
                    break;
                }
                case 2:{
                    requestResult = request.getCommunes(-1, 1,Integer.MAX_VALUE);
                    break;
                }
            }
            setColumns(units[activeTables[viewOrManage] + unitsTreeIndexes[viewOrManage]], viewOrManage);
            tables[viewOrManage][activeTables[viewOrManage]].getItems().clear();
            String line;
            for(Object o : requestResult.items){
                tables[viewOrManage][activeTables[viewOrManage]].getItems().add(o);
                TableView a = new TableView<>();
            }
        } catch (Exception e) {
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
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));

        currentlyActiveTableListeners = new ChangeListener[2];
        changeItemsInMainList(-1,0);
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
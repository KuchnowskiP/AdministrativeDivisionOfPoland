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
import pl.edu.pwr.contract.Dtos.*;
import pl.edu.pwr.contract.Reports.AddReportRequest;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public ChoiceBox<String> voivodeshipReportChoiceBox;
    public ChoiceBox<String> countyReportChoiceBox;
    public ChoiceBox<String> communeReportChoiceBox;
    @FXML
    private CheckBox registeredOfficesCheckBox;
    @FXML
    private TextField topicTextField;
    @FXML
    private TextArea reportContentTextArea;
    @FXML
    private TableView<ReportDto> reportsTableManage;
    @FXML
    private TableView<CountyDto> countiesTableManage;
    @FXML
    private TableView<CommuneDto> communesTableManage;
    @FXML
    private TableView<VoivodeshipDto> voivodeshipsTableManage;
    @FXML
    private TabPane manageUnitsTabPane;
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
    private TableView<VoivodeshipDto> voivodeshipsTable = new TableView<>();
    @FXML
    private TableView<CountyDto> countiesTable = new TableView<>();
    @FXML
    private TableView<CommuneDto> communesTable = new TableView<>();
    private TableView[][] tables;
    Object[] unitsTree = new Object[]{-1,-1,-1};
    int[] unitsTreeIndexes = new int[2];
    int[] activeTables = new int[]{0,0};
    int maxDepth = 2;
    String[] masterName = new String[3];
    Request request = new Request();
    Class<?>[] units = new Class[]{VoivodeshipDto.class, CountyDto.class, CommuneDto.class, ReportDto.class,
            VoivodeshipAddressData.class, CountyAddressData.class, CommuneAddressData.class};
    ChangeListener<?>[] currentlyActiveTableListeners;
    int addressessAreChecked = 0;

    Thread tableUpdater = new Thread();

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
            tableUpdater.interrupt();
            Runnable updateTable = () -> {
                changeItemsInMainList(unitsTree[unitsTreeIndexes[viewOrManage]], viewOrManage);
            };
            tableUpdater = new Thread(updateTable);
            tableUpdater.start();
        }
            currentlyActiveTableListeners[viewOrManage] = (ChangeListener<Object>) (observableValue, oldValue, newValue) -> {
                if(newValue != null) {
                    if(unitsTreeIndexes[viewOrManage] < (maxDepth - newTab)) {
                        unitsTreeIndexes[viewOrManage]++;
                        try {
                            unitsTree[unitsTreeIndexes[viewOrManage]] = newValue.getClass().getField("id").get(newValue);
                            masterName[unitsTreeIndexes[viewOrManage]] = newValue.getClass().getField("name").get(newValue).toString();
                        } catch (IllegalAccessException | NoSuchFieldException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println(unitsTree[unitsTreeIndexes[viewOrManage]]);
                        System.out.println("Selected item: " + unitsTree[unitsTreeIndexes[viewOrManage]]);
                        tableUpdater.interrupt();
                        Runnable updateTable = () -> {
                            changeItemsInMainList(unitsTree[unitsTreeIndexes[viewOrManage]], viewOrManage);
                        };
                        tableUpdater = new Thread(updateTable);
                        tableUpdater.start();
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
            TableColumn master = new TableColumn<>("Powiaty w " + "'" + masterName[unitsTreeIndexes[viewOrManage]]  + "'");
            Platform.runLater(() -> {
                master.getColumns().addAll(columnsToAdd);
                tables[viewOrManage][activeTables[viewOrManage]].getColumns().add(master);
            });
        }else if(unitsTreeIndexes[viewOrManage] == 2 && activeTables[viewOrManage] == 0 || unitsTreeIndexes[viewOrManage] == 1 && activeTables[viewOrManage] == 1){
            TableColumn master = new TableColumn<>("Gminy w " + "'" + masterName[unitsTreeIndexes[viewOrManage]] +  "'");
            Platform.runLater(() -> {
                master.getColumns().addAll(columnsToAdd);
                tables[viewOrManage][activeTables[viewOrManage]].getColumns().add(master);
            });

        }else{
            Platform.runLater(() -> tables[viewOrManage][activeTables[viewOrManage]].getColumns().addAll(columnsToAdd));
        }
    }

    public void changeItemsInMainList(Object id, int viewOrManage){
        try {
            PageResult<?> requestResult = new PageResult<>();
            switch (activeTables[viewOrManage]){
                case 0:{
                    switch (unitsTreeIndexes[viewOrManage]){
                        case 1:{
                            if(addressessAreChecked == 4){
                                requestResult = request.getCountiesWithAddresses(unitsTree[unitsTreeIndexes[viewOrManage]],1,Integer.MAX_VALUE);
                            }else {
                                requestResult = request.getCounties(unitsTree[unitsTreeIndexes[viewOrManage]], 1, Integer.MAX_VALUE);
                            }
                            break;
                        }
                        case 2:{
                            if(addressessAreChecked == 4){
                                requestResult = request.getCommunesWithAddresses(unitsTree[unitsTreeIndexes[viewOrManage]],1,Integer.MAX_VALUE);
                            }else {
                                requestResult = request.getCommunes(unitsTree[unitsTreeIndexes[viewOrManage]], 1, Integer.MAX_VALUE);
                            }
                            break;
                        }
                        default:{
                            if(addressessAreChecked == 4){
                                requestResult = request.getVoivodeshipsWithAddresses(1,Integer.MAX_VALUE);
                            }else {
                                requestResult = request.getVoivodeships(1, Integer.MAX_VALUE);
                            }
                            break;
                        }
                    }
                    break;
                }
                case 1:{

                    if(unitsTreeIndexes[viewOrManage] == 1){
                        if(addressessAreChecked == 4){
                            requestResult = request.getCommunesWithAddresses(unitsTree[unitsTreeIndexes[viewOrManage]],1,Integer.MAX_VALUE);
                        }else {
                            requestResult = request.getCommunes(unitsTree[unitsTreeIndexes[viewOrManage]], 1, Integer.MAX_VALUE);
                        }
                    }
                    else{
                        if(addressessAreChecked == 4){
                            requestResult = request.getCountiesWithAddresses(unitsTree[unitsTreeIndexes[viewOrManage]],1,Integer.MAX_VALUE);
                        }else {
                            requestResult = request.getCounties(unitsTree[unitsTreeIndexes[viewOrManage]], 1, Integer.MAX_VALUE);
                        }
                    }
                    break;
                }
                case 2:{
                    if(addressessAreChecked == 4){
                        requestResult = request.getCommunesWithAddresses(unitsTree[unitsTreeIndexes[viewOrManage]],1,Integer.MAX_VALUE);
                    }else {
                        requestResult = request.getCommunes(unitsTree[unitsTreeIndexes[viewOrManage]], 1, Integer.MAX_VALUE);
                    }
                    break;
                }
                case 3:{
                    requestResult = request.getReports(1,Integer.MAX_VALUE);
                    break;
                }
            }

            setColumns(units[activeTables[viewOrManage] + unitsTreeIndexes[viewOrManage] + addressessAreChecked], viewOrManage);
            PageResult<?> finalRequestResult = requestResult;
            Platform.runLater(() -> {
                tables[viewOrManage][activeTables[viewOrManage]].getItems().clear();
                for(Object o : finalRequestResult.items){
                    tables[viewOrManage][activeTables[viewOrManage]].getItems().add(o);
                }
            });

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
        tables = new TableView[][]{{voivodeshipsTable, countiesTable, communesTable},{voivodeshipsTableManage, countiesTableManage, communesTableManage, reportsTableManage}};
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));

        currentlyActiveTableListeners = new ChangeListener[2];
        tableUpdater.interrupt();
        Runnable updateTable = () -> {
            changeItemsInMainList(-1, 0);
        };
        tableUpdater = new Thread(updateTable);
        tableUpdater.start();

        changeListener(-1,0,0);
        TabPaneListenerInitializer(viewUnitsTabPane, 0);

        try {
            choiceBoxListeners();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    PageResult<VoivodeshipDto> requestVoivodeships;
    PageResult<CountyDto> requestCounties;
    PageResult<CommuneDto> requestCommunes;
    VoivodeshipDto reportSelectedVoivodeship = new VoivodeshipDto(-1,"","","");
    CountyDto reportSelectedCounty = new CountyDto(-1,-1,"","",false,"","");
    CommuneDto reportSelectedCommune = new CommuneDto(-1,-1,"","",-1,-1.0,"","");
    public void choiceBoxListeners() throws Exception {
        requestVoivodeships = request.getVoivodeships(1, Integer.MAX_VALUE);
        voivodeshipReportChoiceBox.getItems().add("-");
        voivodeshipReportChoiceBox.setValue("-");
        for(int i = 0; i < requestVoivodeships.items.size(); i++){
            voivodeshipReportChoiceBox.getItems().add(requestVoivodeships.getItems().get(i).getName());
        }

        voivodeshipReportChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue)  {
                System.out.println(newValue);
                if(!Objects.equals(String.valueOf(newValue), "-")){
                    countyReportChoiceBox.getItems().clear();
                    countyReportChoiceBox.setDisable(false);
                    countyReportChoiceBox.getItems().add("-");
                    countyReportChoiceBox.setValue("-");
                    reportSelectedVoivodeship = requestVoivodeships.getItems().stream()
                            .filter(voivodeshipDto -> newValue.equals(voivodeshipDto.getName())).findAny().get();
                    try {
                        requestCounties = request.getCounties(reportSelectedVoivodeship.getId(),1,Integer.MAX_VALUE);
                        for(int i = 0; i < requestCounties.getItems().size(); i++){
                            countyReportChoiceBox.getItems().add(requestCounties.getItems().get(i).getName());
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                else{
                    reportSelectedVoivodeship.setId(-1);
                    countyReportChoiceBox.setValue("-");
                    countyReportChoiceBox.setDisable(true);
                }
            }
        });

        countyReportChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if(!Objects.equals(String.valueOf(newValue), "-")){
                    communeReportChoiceBox.getItems().clear();
                    communeReportChoiceBox.setDisable(false);
                    communeReportChoiceBox.getItems().add("-");
                    communeReportChoiceBox.setValue("-");
                    reportSelectedCounty = requestCounties.getItems().stream()
                            .filter(voivodeshipDto -> newValue.equals(voivodeshipDto.getName())).findAny().get();

                    try {
                        requestCommunes = request.getCommunes(reportSelectedCounty.getId(),1,Integer.MAX_VALUE);
                        for(int i = 0; i < requestCommunes.getItems().size(); i++){
                            communeReportChoiceBox.getItems().add(requestCommunes.getItems().get(i).getName() + " (" + requestCommunes.getItems().get(i).getCommuneType() + ")");
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                if(Objects.equals(String.valueOf(newValue), "-")){
                    reportSelectedCounty.setId(-1);
                    communeReportChoiceBox.getItems().clear();
                    communeReportChoiceBox.setDisable(true);
                }
            }
        });

        communeReportChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if(!Objects.equals(String.valueOf(newValue),"-")){
                    String[] name = newValue.toString().split("\\(");
                    reportSelectedCommune = requestCommunes.getItems().stream()
                            .filter(communeDto -> name[0].trim().equals(communeDto.getName())).findAny().get();
                }else{
                    reportSelectedCommune.setId(-1);
                }
            }
        });
    }
    public void onBackButtonClick(ActionEvent ignoredActionEvent) {

        if(unitsTreeIndexes[0] > 0) {
            unitsTreeIndexes[0]--;
        }
        System.out.println("Wrócono do: " + unitsTree[unitsTreeIndexes[0]]);

        tableUpdater.interrupt();
        Runnable updateTable = () -> {
            changeItemsInMainList(unitsTree[unitsTreeIndexes[0]], 0);
        };
        tableUpdater = new Thread(updateTable);
        tableUpdater.start();

    }

    public void onLoginButtonClick(ActionEvent ignoredActionEvent) {
        System.out.println("przycisk wciśnięty");
        String login = loginTextField.getText();
        String password = passwordTextField.getText();
        if(login.equals("admin") && password.equals("admin")){

            TabPaneListenerInitializer(manageUnitsTabPane, 1);
            voivodeshipsTableManage.setEditable(true);
            countiesTableManage.setEditable(true);
            communesTable.setEditable(true);

            Platform.runLater(() -> {
                        loginFeedbackLabel.setText("Zalogowano jako " + loginTextField.getText());
                        loginFeedbackLabel.setVisible(true);
                        mainTabPane.getTabs().add(manageTab);
            });

            changeListener(-1, 0, 1);

            tableUpdater.interrupt();
            Runnable updateTable = () -> {
                changeItemsInMainList(-1,1);
            };
            tableUpdater = new Thread(updateTable);
            tableUpdater.start();

        }else{
            Platform.runLater(() -> {
                loginFeedbackLabel.setText("Błędne dane logowania!");
                loginFeedbackLabel.setVisible(true);
            });

        }
    }

    private void TabPaneListenerInitializer(TabPane TabPane, int viewOrManage) {
        TabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(oldValue.getId() + " -> " + newValue.getId());
            changeListener(Integer.parseInt(oldValue.getId()), Integer.parseInt(newValue.getId()), viewOrManage);
        });
    }

    public void onManageBackButtonClick(ActionEvent ignoredActionEvent) {

        if(unitsTreeIndexes[1] > 0) {
            unitsTreeIndexes[1]--;
        }
        System.out.println("Wrócono do: " + unitsTree[unitsTreeIndexes[1]]);

        tableUpdater.interrupt();
        Runnable updateTable = () -> {
            changeItemsInMainList(unitsTree[unitsTreeIndexes[1]],1);
        };
        tableUpdater = new Thread(updateTable);
        tableUpdater.start();
    }

    public void onSendButtonClick(ActionEvent ignoredActionEvent) {
        System.out.println("Click");
        AddReportRequest addReportRequest = new AddReportRequest();
        addReportRequest.setTopic(topicTextField.getText());
        addReportRequest.setContent(reportContentTextArea.getText());
        if (reportSelectedVoivodeship.getId() != -1) {
            addReportRequest.setVoivodeshipId(reportSelectedVoivodeship.getId());
        }
        if (reportSelectedCounty.getId() != -1) {
            addReportRequest.setCountyId(reportSelectedCounty.getId());
        }
        if (reportSelectedCommune.getId() != -1) {
            addReportRequest.setCommuneId(reportSelectedCommune.getId());
        }
        try {
            request.createReport(addReportRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void onRefreshButtonClick(ActionEvent ignoredActionEvent) {
        tableUpdater.interrupt();
        Runnable updateTable = () -> {
            changeItemsInMainList(unitsTree[unitsTreeIndexes[0]],0);
        };
        tableUpdater = new Thread(updateTable);
        tableUpdater.start();
    }

    public void onManageRefreshButtonClick(ActionEvent ignoredActionEvent) {
        tableUpdater.interrupt();
        Runnable updateTable = () -> {
            changeItemsInMainList(unitsTree[unitsTreeIndexes[1]],1);
        };
        tableUpdater = new Thread(updateTable);
        tableUpdater.start();
    }

    public void onCheckboxChange(ActionEvent ignoredActionEvent) {
        if(registeredOfficesCheckBox.isSelected()){
            addressessAreChecked = 4;
        }else{
            addressessAreChecked = 0;
        }
        tableUpdater.interrupt();
        Runnable updateTable = () -> {
            changeItemsInMainList(unitsTree[unitsTreeIndexes[0]],0);
        };
        tableUpdater = new Thread(updateTable);
        tableUpdater.start();
    }
}
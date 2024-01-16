package pl.edu.pwr.database.administrativedivisionofpoland.Controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.*;
import pl.edu.pwr.contract.Reports.AddReportRequest;
import pl.edu.pwr.database.administrativedivisionofpoland.Request;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
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
    public ImageView flagImage;
    public ImageView emblemImage;
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
    TableView[][] tables;
    Object[] unitsTree = new Object[]{-1,-1,-1};
    int[] unitsTreeIndexes = new int[2];
    int[] activeTables = new int[]{0,0};
    int maxDepth = 2;
    String[] masterName = new String[3];
    Request request = new Request();
    Class<?>[] units = new Class[]{VoivodeshipDto.class, CountyDto.class, CommuneDto.class, ReportDto.class,
            VoivodeshipAddressData.class, CountyAddressData.class, CommuneAddressData.class};
    ChangeListener<?>[] currentlyActiveTableListeners;
    int addressesAreChecked = 0;
    Thread tableUpdater = new Thread();
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
        initializeListeners();
    }
    public void setInitialView() throws Exception {
        mainTabPane.getTabs().remove(manageTab); //hiding managing tab. Will be open after singing in.
        changeView(-1,0); //setting content of table
        setReportTab();
    }
    public void setReportTab() throws Exception {
        requestVoivodeships = request.getVoivodeships(1, Integer.MAX_VALUE);
        voivodeshipReportChoiceBox.getItems().add("-");
        voivodeshipReportChoiceBox.setValue("-");
        for(int i = 0; i < requestVoivodeships.items.size(); i++){
            voivodeshipReportChoiceBox.getItems().add(requestVoivodeships.getItems().get(i).getName());
        }
    }
    public void changeView(Object id, int viewOrManage){
        tableUpdater.interrupt();
        Runnable updateTable = () -> {
            changeItemsInMainTable(id, viewOrManage);
        };
        tableUpdater = new Thread(updateTable);
        tableUpdater.start();
    }
    public void initializeStructures(){
        tables = new TableView<?>[][]{{voivodeshipsTable, countiesTable, communesTable},
                {voivodeshipsTableManage, countiesTableManage, communesTableManage, reportsTableManage}};
        currentlyActiveTableListeners = new ChangeListener[2];
    }
    public void initializeListeners(){
        changeTableListener(-1,0,0);
        TabPaneListenerInitializer(viewUnitsTabPane, 0);
        choiceBoxListeners();
        setRowsFactories();
        passwordTextField.setOnKeyPressed(key -> {
            if(key.getCode().equals(KeyCode.ENTER)){
                onLoginButtonClick(new ActionEvent());
            }
        });
    }
    public void setColumnsInMainTable(Class<?> passedClass, int viewOrManage){
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
        Platform.runLater(() -> tables[viewOrManage][activeTables[viewOrManage]].getColumns().clear());
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
    public void changeItemsInMainTable(Object id, int viewOrManage){
        try {
            PageResult<?> requestResult = new PageResult<>();
            switch (activeTables[viewOrManage]){
                case 0:{
                    switch (unitsTreeIndexes[viewOrManage]){
                        case 1:{
                            if(addressesAreChecked == 4){
                                requestResult = request.getCountiesWithAddresses(unitsTree[unitsTreeIndexes[viewOrManage]],1,Integer.MAX_VALUE);
                            }else {
                                requestResult = request.getCounties(unitsTree[unitsTreeIndexes[viewOrManage]], 1, Integer.MAX_VALUE);
                            }
                            break;
                        }
                        case 2:{
                            if(addressesAreChecked == 4){
                                requestResult = request.getCommunesWithAddresses(unitsTree[unitsTreeIndexes[viewOrManage]],1,Integer.MAX_VALUE);
                            }else {
                                requestResult = request.getCommunes(unitsTree[unitsTreeIndexes[viewOrManage]], 1, Integer.MAX_VALUE);
                            }
                            break;
                        }
                        default:{
                            if(addressesAreChecked == 4){
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
                        if(addressesAreChecked == 4){
                            requestResult = request.getCommunesWithAddresses(unitsTree[unitsTreeIndexes[viewOrManage]],1,Integer.MAX_VALUE);
                        }else {
                            requestResult = request.getCommunes(unitsTree[unitsTreeIndexes[viewOrManage]], 1, Integer.MAX_VALUE);
                        }
                    }
                    else{
                        if(addressesAreChecked == 4){
                            requestResult = request.getCountiesWithAddresses(unitsTree[unitsTreeIndexes[viewOrManage]],1,Integer.MAX_VALUE);
                        }else {
                            requestResult = request.getCounties(unitsTree[unitsTreeIndexes[viewOrManage]], 1, Integer.MAX_VALUE);
                        }
                    }
                    break;
                }
                case 2:{
                    if(addressesAreChecked == 4){
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

            setColumnsInMainTable(units[activeTables[viewOrManage] + unitsTreeIndexes[viewOrManage] + addressesAreChecked], viewOrManage);
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
    public void changeTableListener(int oldTab, int newTab, int viewOrManage){  //nasłuchiwacz zmiany zakładki np z województw na powiaty
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
            changeView(unitsTree[unitsTreeIndexes[viewOrManage]], viewOrManage);
        }
        currentlyActiveTableListeners[viewOrManage] = (ChangeListener<Object>) (observableValue, oldValue, newValue) -> {
            System.out.println(newValue);
//            if(newValue != null) {
//                try {
//                    setImages(newValue.getClass().getField("terytCode").get(newValue).toString());
//                } catch (URISyntaxException | IllegalAccessException | NoSuchFieldException e) {
//                    throw new RuntimeException(e);
//                }
//            }
        };

        tables[viewOrManage][newTab].getSelectionModel().selectedItemProperty().addListener(currentlyActiveTableListeners[viewOrManage]);

    }
    private void TabPaneListenerInitializer(TabPane TabPane, int viewOrManage) {
        TabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(oldValue.getId() + " -> " + newValue.getId());
            changeTableListener(Integer.parseInt(oldValue.getId()), Integer.parseInt(newValue.getId()), viewOrManage);
        });
    }
    PageResult<VoivodeshipDto> requestVoivodeships;
    PageResult<CountyDto> requestCounties;
    PageResult<CommuneDto> requestCommunes;
    VoivodeshipDto reportSelectedVoivodeship = new VoivodeshipDto(-1,"","","");
    CountyDto reportSelectedCounty = new CountyDto(-1,-1,"","",false,"","");
    CommuneDto reportSelectedCommune = new CommuneDto(-1,-1,"","",-1,-1.0,"","");
    public void choiceBoxListeners() {
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
    public void setRowsFactories(){
        for(int i = 0; i < tables.length; i++){
            for(int j = 0; j < tables[i].length - 1; j++){
                tables[i][j].setRowFactory(trf -> {
                    TableRow<?> row = new TableRow<>();
                    row.setOnMouseClicked(event -> {
                        if(!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() % 2 == 0){
                            System.out.println(row.getItem());
                            if(unitsTreeIndexes[0] < (maxDepth)) {
                                unitsTreeIndexes[0]++;
                                try {
                                    unitsTree[unitsTreeIndexes[0]] = row.getItem().getClass().getField("id").get(row.getItem());
                                    masterName[unitsTreeIndexes[0]] = row.getItem().getClass().getField("name").get(row.getItem()).toString();
                                } catch (IllegalAccessException | NoSuchFieldException e) {
                                    throw new RuntimeException(e);
                                }
                                System.out.println(unitsTree[unitsTreeIndexes[0]]);
                                System.out.println("Selected item: " + unitsTree[unitsTreeIndexes[0]]);
                                changeView(unitsTree[unitsTreeIndexes[0]],0);
                            }
                        }
                    });
                    return row;
                });
            }
        }
    }
    public void onBackButtonClick(ActionEvent ignoredActionEvent) {
        if(unitsTreeIndexes[0] > 0) {
            unitsTreeIndexes[0]--;
        }
        System.out.println("Wrócono do: " + unitsTree[unitsTreeIndexes[0]]);
        changeView(unitsTree[unitsTreeIndexes[0]],0);
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

            changeTableListener(-1, 0, 1);
            changeView(-1,1);
        }else{
            Platform.runLater(() -> {
                loginFeedbackLabel.setText("Błędne dane logowania!");
                loginFeedbackLabel.setVisible(true);
            });

        }
    }
    public void onManageBackButtonClick(ActionEvent ignoredActionEvent) {

        if(unitsTreeIndexes[1] > 0) {
            unitsTreeIndexes[1]--;
        }
        System.out.println("Wrócono do: " + unitsTree[unitsTreeIndexes[1]]);
        changeView(unitsTree[unitsTreeIndexes[1]],1);
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
        changeView(unitsTree[unitsTreeIndexes[0]],0);
    }

    public void onManageRefreshButtonClick(ActionEvent ignoredActionEvent) {
        changeView(unitsTree[unitsTreeIndexes[1]],1);
    }

    public void onCheckboxChange(ActionEvent ignoredActionEvent) {
        if(registeredOfficesCheckBox.isSelected()){
            addressesAreChecked = 4;
        }else{
            addressesAreChecked = 0;
        }
        changeView(unitsTree[unitsTreeIndexes[0]],0);
    }
    public void setImages(String terytCode) throws URISyntaxException {
        if(terytCode == null) terytCode = "0000000";
        String flagFileName = "src/main/resources/flags/" + terytCode + ".png";
        String emblemFileName = "src/main/resources/emblems/" + terytCode + ".png";
        flagImage.setImage(new Image(new File(flagFileName).toURI().toString()));
        emblemImage.setImage(new Image(new File(emblemFileName).toURI().toString()));
    }
}
package pl.edu.pwr.database.administrativedivisionofpoland.Controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.*;
import pl.edu.pwr.contract.Reports.AddReportRequest;
import pl.edu.pwr.database.administrativedivisionofpoland.Data.*;
import pl.edu.pwr.database.administrativedivisionofpoland.Data.Services.CommuneDataService;
import pl.edu.pwr.database.administrativedivisionofpoland.Data.Services.CountyDataService;
import pl.edu.pwr.database.administrativedivisionofpoland.Data.Services.VoivodeshipDataService;
import pl.edu.pwr.database.administrativedivisionofpoland.Handlers.EventsHandler;
import pl.edu.pwr.database.administrativedivisionofpoland.Handlers.UIHandler;
import pl.edu.pwr.database.administrativedivisionofpoland.Main;
import pl.edu.pwr.database.administrativedivisionofpoland.UserData;
import pl.edu.pwr.database.administrativedivisionofpoland.Utils.Utils;

import java.io.*;
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
    public Button voivodeshipTabEditUnitButton;
    public Button countyTabEditUnitButton;public Button communeTabEditUnitButton;
    @FXML public Button communeTabAddUnitButton;
    @FXML public Button countyTabAddUnitButton;
    @FXML public Button voivodeshipTabAddUnitButton;
    @FXML public Button showHistoricalDataButton;
    @FXML private CheckBox registeredOfficesCheckBox;
    @FXML private TextField topicTextField;
    @FXML private TextArea reportContentTextArea;
    @FXML private TableView<ReportDto> reportsTableManage;
    @FXML private TableView<CountyDto> countiesTableManage;
    @FXML private TableView<CommuneDto> communesTableManage;
    @FXML private TableView<VoivodeshipDto> voivodeshipsTableManage;
    @FXML private TabPane manageUnitsTabPane;
    @FXML private Label loginFeedbackLabel;
    @FXML private TabPane viewUnitsTabPane;
    @FXML private TextField loginTextField;
    @FXML private PasswordField passwordTextField;
    @FXML private TabPane mainTabPane;
    @FXML private Tab manageTab;
    @FXML private TableView<VoivodeshipDto> voivodeshipsTable = new TableView<>();
    @FXML private TableView<CountyDto> countiesTable = new TableView<>();
    @FXML private TableView<CommuneDto> communesTable = new TableView<>();
    TableView[][] tables;
    public Object[] unitsTree = new Object[]{-1,-1,-1};
    public int[] unitsTreeIndexes = new int[2];
    int[] activeTables = new int[]{0,0};
    int maxDepth = 2;
    String[] masterName = new String[3];
    DataReceiver requestResultsReceiver = new DataReceiver();
    DataSender requestSender = new DataSender();
    Class<?>[] units = new Class[]{VoivodeshipDto.class, CountyDto.class, CommuneDto.class, ReportDto.class,
            VoivodeshipAddressData.class, CountyAddressData.class, CommuneAddressData.class};
    ChangeListener<?>[] currentlyActiveTableListeners;
    int addressesAreChecked = 0;
    Thread tableUpdater = new Thread();
    public EventsHandler eventsHandler;
    UIHandler uiHandler;

    public MainController() {
        this.eventsHandler = new EventsHandler(this);
        this.uiHandler = new UIHandler(this);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        eventsHandler.setupButtons();
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out),
                true, StandardCharsets.UTF_8)); //needed due to polish diacritics

        initializeStructures();
        initializeUI();
        initializeListeners();
    }
    public void initializeUI(){
        try {
            setInitialView();
            setReportTab();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void setInitialView() throws Exception {
        mainTabPane.getTabs().remove(manageTab); //hiding managing tab. Will be open after singing in.
        changeView(-1,0); //setting content of table
    }
    public void setReportTab() throws Exception {
        requestVoivodeships = Utils.getVoivodeshipResult(voivodeshipReportChoiceBox);
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
        setEnterKeyPressedEvent();
    }
    public void setEnterKeyPressedEvent(){
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
            TableColumn<?,?> column = new TableColumn<>(f.getName());
            column.setCellValueFactory(new PropertyValueFactory<>(f.getName()));
            columnsToAdd.add(column);
        }

        ObservableList<TableColumn<?,?>> sortOrder = tables[viewOrManage][activeTables[viewOrManage]].getSortOrder();

        Platform.runLater(() -> {
            tables[viewOrManage][activeTables[viewOrManage]].getColumns().clear();

            if (unitsTreeIndexes[viewOrManage] == 1 && activeTables[viewOrManage] == 0) {
                TableColumn master = new TableColumn<>("Powiaty w " + "'" + masterName[unitsTreeIndexes[viewOrManage]] + "'");
                master.getColumns().addAll(columnsToAdd);
                tables[viewOrManage][activeTables[viewOrManage]].getColumns().add(master);
            } else if (unitsTreeIndexes[viewOrManage] == 2 && activeTables[viewOrManage] == 0 || unitsTreeIndexes[viewOrManage] == 1 && activeTables[viewOrManage] == 1) {
                TableColumn master = new TableColumn<>("Gminy w " + "'" + masterName[unitsTreeIndexes[viewOrManage]] + "'");
                master.getColumns().addAll(columnsToAdd);
                tables[viewOrManage][activeTables[viewOrManage]].getColumns().add(master);
            } else {
                tables[viewOrManage][activeTables[viewOrManage]].getColumns().addAll(columnsToAdd);
            }

            tables[viewOrManage][activeTables[viewOrManage]].getSortOrder().setAll(sortOrder);
        });

    }
    public void changeItemsInMainTable(Object id, int viewOrManage){
        try {
            PageResult<?> requestResult = requestResultsReceiver.getResult(activeTables[viewOrManage],
                    unitsTreeIndexes[viewOrManage], unitsTree[unitsTreeIndexes[viewOrManage]], addressesAreChecked);
            setColumnsInMainTable(units[activeTables[viewOrManage] + unitsTreeIndexes[viewOrManage]
                    + addressesAreChecked], viewOrManage);

            Platform.runLater(() -> {
                tables[viewOrManage][activeTables[viewOrManage]].getItems().clear();
                for(Object o : requestResult.items){
                    tables[viewOrManage][activeTables[viewOrManage]].getItems().add(o);
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public VoivodeshipDto voivodeshipForEditionOrDeletion  = new VoivodeshipDto(-1,"","","");
    public CountyDto countyForEditionOrDeletion = new CountyDto(-1,-1,"","",false,"","");
    public CommuneDto communeForEditionOrDeletion = new CommuneDto(-1,-1,"","",-1,-1.0,"","");
    PageResult<VoivodeshipDto> requestVoivodeships;
    PageResult<CountyDto> requestCounties;
    PageResult<CommuneDto> requestCommunes;
    public void changeTableListener(int oldTab, int newTab, int viewOrManage){  //nasłuchiwacz zmiany zakładki np z województw na powiaty
        boolean changed = false;
        if(oldTab != -1){
            System.out.println("zmiana");
            tables[viewOrManage][oldTab].getSelectionModel().selectedItemProperty().removeListener(currentlyActiveTableListeners[viewOrManage]);
            changed = true;
        }
        unitsTreeIndexes[viewOrManage] = 0;
        if(viewOrManage == 1) {
            uiHandler.setAddButton();
            uiHandler.setEditButton();
        };
        boolean finalChanged = changed;
        if(finalChanged){
            unitsTreeIndexes[viewOrManage] = 0;
            activeTables[viewOrManage] = newTab;
            changeView(unitsTree[unitsTreeIndexes[viewOrManage]], viewOrManage);
        }
        currentlyActiveTableListeners[viewOrManage] = (ChangeListener<Object>) (observableValue, oldValue, newValue) -> {
            if(viewOrManage == 1){
                if(((unitsTreeIndexes[1] == 2 && activeTables[1] == 0) || (unitsTreeIndexes[1] == 1 && activeTables[1] == 1) || activeTables[1] == 2) && newValue != null){
                    try {
                        communeForEditionOrDeletion.setId((Integer) newValue.getClass().getField("id").get(newValue));
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    communeForEditionOrDeletion.setId(-1);
                }
                if(((activeTables[1] == 0 && unitsTreeIndexes[1] == 1) || (activeTables[1] == 1 && unitsTreeIndexes[1] == 0)) && newValue != null){
                    try {
                        countyForEditionOrDeletion.setId((Integer) newValue.getClass().getField("id").get(newValue));
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    countyForEditionOrDeletion.setId(-1);
                }
                if(newValue != null && activeTables[1] == 0 && unitsTreeIndexes[1] == 0){
                    try {
                        voivodeshipForEditionOrDeletion.setId((Integer) newValue.getClass().getField("id").get(newValue));
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    voivodeshipForEditionOrDeletion.setId(-1);
                }
                System.out.println("voivodeship " + voivodeshipForEditionOrDeletion.getId());
                System.out.println("county " + countyForEditionOrDeletion.getId());
                System.out.println("commune " + communeForEditionOrDeletion.getId());
            }
        };

        tables[viewOrManage][newTab].getSelectionModel().selectedItemProperty().addListener(currentlyActiveTableListeners[viewOrManage]);

    }
    private void TabPaneListenerInitializer(TabPane TabPane, int viewOrManage) {
        TabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(oldValue.getId() + " -> " + newValue.getId());
            changeTableListener(Integer.parseInt(oldValue.getId()), Integer.parseInt(newValue.getId()), viewOrManage);
        });
    }
    VoivodeshipDataService voivodeshipDataService = new VoivodeshipDataService();
    CountyDataService countyDataService = new CountyDataService();
    CommuneDataService communeDataService = new CommuneDataService();
    VoivodeshipDto reportSelectedVoivodeship = new VoivodeshipDto(-1,"","","");
    CountyDto reportSelectedCounty = new CountyDto(-1,-1,"","",false,"","");
    CommuneDto reportSelectedCommune = new CommuneDto(-1,-1,"","",-1,-1.0,"","");
    public void choiceBoxListeners() {
        voivodeshipReportChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue){
                if (newValue != null) {
                    System.out.println(newValue);
                    if (!Objects.equals(String.valueOf(newValue), "-")) {
                        countyReportChoiceBox.getItems().clear();
                        countyReportChoiceBox.setDisable(false);
                        countyReportChoiceBox.getItems().add("-");
                        countyReportChoiceBox.setValue("-");
                        reportSelectedVoivodeship = requestVoivodeships.getItems().stream()
                                .filter(voivodeshipDto -> newValue.equals(voivodeshipDto.getName())).findAny().get();
                        try {
                            requestCounties = countyDataService.get(reportSelectedVoivodeship.getId(), 1, Integer.MAX_VALUE);
                            for (int i = 0; i < requestCounties.getItems().size(); i++) {
                                countyReportChoiceBox.getItems().add(requestCounties.getItems().get(i).getName());
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        reportSelectedVoivodeship.setId(-1);
                        countyReportChoiceBox.setValue("-");
                        countyReportChoiceBox.setDisable(true);
                    }
                }
            }
        });
        countyReportChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue != null) {
                    if (!Objects.equals(String.valueOf(newValue), "-")) {
                        communeReportChoiceBox.getItems().clear();
                        communeReportChoiceBox.setDisable(false);
                        communeReportChoiceBox.getItems().add("-");
                        communeReportChoiceBox.setValue("-");
                        reportSelectedCounty = requestCounties.getItems().stream()
                                .filter(countyDto -> newValue.equals(countyDto.getName())).findAny().get();

                        try {
                            requestCommunes = communeDataService.get(reportSelectedCounty.getId(), 1, Integer.MAX_VALUE);
                            for (int i = 0; i < requestCommunes.getItems().size(); i++) {
                                communeReportChoiceBox.getItems().add(requestCommunes.getItems().get(i).getName() + " (" + requestCommunes.getItems().get(i).getCommuneType() + ")");
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (Objects.equals(String.valueOf(newValue), "-")) {
                        reportSelectedCounty.setId(-1);
                        communeReportChoiceBox.getItems().clear();
                        communeReportChoiceBox.setDisable(true);
                    }
                }
            }
        });
        communeReportChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if(newValue != null) {
                    if (!Objects.equals(String.valueOf(newValue), "-")) {
                        String[] name = newValue.toString().split("\\(");
                        reportSelectedCommune = requestCommunes.getItems().stream()
                                .filter(communeDto -> name[0].trim().equals(communeDto.getName())).findAny().get();
                    } else {
                        reportSelectedCommune.setId(-1);
                    }
                }
            }
        });
    }
    public void setRowsFactories(){
        for(int i = 0; i < tables.length; i++){
            for(int j = 0; j < tables[i].length - (1+i); j++){
                int finalI = i;
                tables[i][j].setRowFactory(trf -> {
                    TableRow<?> row = new TableRow<>();
                    row.setOnMouseClicked(event -> {
                        if(!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() % 2 == 0){
                            System.out.println(row.getItem());
                            if(unitsTreeIndexes[finalI] < (maxDepth)) {
                                unitsTreeIndexes[finalI]++;
                                if(finalI == 1){
                                    uiHandler.setAddButton();
                                    uiHandler.setEditButton();
                                }
                                try {
                                    unitsTree[unitsTreeIndexes[finalI]] = row.getItem().getClass().getField("id").get(row.getItem());
                                    masterName[unitsTreeIndexes[finalI]] = row.getItem().getClass().getField("name").get(row.getItem()).toString();
                                } catch (IllegalAccessException | NoSuchFieldException e) {
                                    throw new RuntimeException(e);
                                }
                                System.out.println(unitsTree[unitsTreeIndexes[finalI]]);
                                System.out.println("Selected item: " + unitsTree[unitsTreeIndexes[finalI]]);
                                changeView(unitsTree[unitsTreeIndexes[finalI]], finalI);
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
            uiHandler.setAddButton();
            uiHandler.setEditButton();
        }

        System.out.println("Wrócono do: " + unitsTree[unitsTreeIndexes[1]]);
        changeView(unitsTree[unitsTreeIndexes[1]],1);
    }
    public void onSendButtonClick(ActionEvent ignoredActionEvent) throws Exception {
        System.out.println("Click");
        UserData.prompt ="\nwysłać zgłoszenie?";
        UserData.getConfirmation();
        if(UserData.confirmed) {
            AddReportRequest addReportRequest = new AddReportRequest();
            addReportRequest.setTopic(topicTextField.getText().trim());
            addReportRequest.setContent(reportContentTextArea.getText().trim());
            if (reportSelectedVoivodeship.getId() != -1) {
                addReportRequest.setVoivodeshipId(reportSelectedVoivodeship.getId());
            }
            if (reportSelectedCounty.getId() != -1) {
                addReportRequest.setCountyId(reportSelectedCounty.getId());
            }
            if (reportSelectedCommune.getId() != -1) {
                addReportRequest.setCommuneId(reportSelectedCommune.getId());
            }
            requestSender.addReport(addReportRequest);
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
    public void onShowHistoricalDataButtonClick(ActionEvent ignoredActionEvent) {
        showHistoricalDataButton.setDisable(true);
        System.out.println("Showing history");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("history-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root, 1280,720);
            scene.getStylesheets().addAll(Main.class.getResource("style.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Dane historyczne");
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon.png")));
            stage.getIcons().add(icon);
            stage.setOnCloseRequest(windowEvent -> showHistoricalDataButton.setDisable(false));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void onDeleteButtonClick(ActionEvent actionEvent) throws IOException, InterruptedException {
        if(voivodeshipForEditionOrDeletion.getId() != -1){
            UserData.prompt ="\nusunąć to województwo?";
        }else if(countyForEditionOrDeletion.getId() != -1){
            UserData.prompt ="\nusunąć ten powiat?";
        }else if(communeForEditionOrDeletion.getId() != -1){
            UserData.prompt ="\nusunąć tą gminę?";
        }else{
            return;
        }

        UserData.getConfirmation();
        if(UserData.confirmed) {
            if (voivodeshipForEditionOrDeletion.getId() != -1) {
                if (requestSender.deleteVoivodeship(voivodeshipForEditionOrDeletion.getId())) {
                    changeView(unitsTree[unitsTreeIndexes[1]], 1);
                }
            } else if (countyForEditionOrDeletion.getId() != -1) {
                if (requestSender.deleteCounty(countyForEditionOrDeletion.getId())) {
                    changeView(unitsTree[unitsTreeIndexes[1]], 1);
                }
            } else if (communeForEditionOrDeletion.getId() != -1) {
                if (requestSender.deleteCommune(communeForEditionOrDeletion.getId())) {
                    changeView(unitsTree[unitsTreeIndexes[1]], 1);
                }
            }
        }
    }

    public void onEditButtonClick(ActionEvent ignoredActionEvent) {
        System.out.println("Editing");
        changeView(unitsTree[unitsTreeIndexes[1]],1);
    }
}
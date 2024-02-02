package pl.edu.pwr.database.administrativedivisionofpoland.Controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.*;
import pl.edu.pwr.database.administrativedivisionofpoland.Authentication.AuthenticationService;
import pl.edu.pwr.database.administrativedivisionofpoland.Data.DataReceiver;
import pl.edu.pwr.database.administrativedivisionofpoland.Data.DataSender;
import pl.edu.pwr.database.administrativedivisionofpoland.Handlers.StageEventsHandler;
import pl.edu.pwr.database.administrativedivisionofpoland.Handlers.UIInteractionHandler;
import pl.edu.pwr.database.administrativedivisionofpoland.Main;
import pl.edu.pwr.database.administrativedivisionofpoland.UserData;

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
    public ImageView flagImage;
    public ImageView emblemImage;
    public Button voivodeshipTabEditUnitButton;
    public Button countyTabEditUnitButton;public Button communeTabEditUnitButton;
    @FXML public Button communeTabAddUnitButton;
    @FXML public Button countyTabAddUnitButton;
    @FXML public Button voivodeshipTabAddUnitButton;
    @FXML public Button showHistoricalDataButton;
    @FXML private Tab voivodeshipViewTab;
    @FXML private Label viewingLabel;
    @FXML private Label inVoivodeshipLabel;
    @FXML private ChoiceBox communeOrCountyChoiceBox;
    @FXML private Button reportProblemButton;
    @FXML private CheckBox registeredOfficesCheckBox;
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
    Class<?>[] units = new Class[]{VoivodeshipExtended.class, CountyExtended.class, CommuneDto.class, ReportDto.class,
            VoivodeshipAddressData.class, CountyAddressData.class, CommuneAddressData.class};
    ChangeListener<?>[] currentlyActiveTableListeners;
    int addressesAreChecked = 0;
    Thread tableUpdater = new Thread();
    public StageEventsHandler stageEventsHandler;
    UIInteractionHandler uiInteractionHandler;
    int showCommunesByVoivodeships = 0;

    public MainController() {
        this.stageEventsHandler = new StageEventsHandler(this);
        this.uiInteractionHandler = new UIInteractionHandler(this);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stageEventsHandler.setupButtons();
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out),
                true, StandardCharsets.UTF_8)); //needed due to polish diacritics

        initializeStructures();
        initializeUI();
        initializeListeners();
    }
    public void initializeUI(){
        try {
            setInitialView();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void setInitialView() throws Exception {
        mainTabPane.getTabs().remove(manageTab); //hiding managing tab. Will be open after singing in.
        changeView(0); //setting content of table
    }
    public void changeView(int viewOrManage){
        tableUpdater.interrupt();
        Runnable updateTable = () -> {
            changeItemsInMainTable(viewOrManage);
        };
        tableUpdater = new Thread(updateTable);
        tableUpdater.start();
    }
    public void initializeStructures(){
        tables = new TableView<?>[][]{{voivodeshipsTable, countiesTable, communesTable},
                {voivodeshipsTableManage, countiesTableManage, communesTableManage, reportsTableManage}};
        currentlyActiveTableListeners = new ChangeListener[2];
        communeOrCountyChoiceBox.getItems().addAll("powiaty", "gminy");
    }
    public void initializeListeners(){
        changeTableListener(-1,0,0);
        TabPaneListenerInitializer(viewUnitsTabPane, 0);
        setRowsFactories();
        setEnterKeyPressedEvent();
        setCommuneOrCountyChoiceBox();
    }
    public void setCommuneOrCountyChoiceBox(){
        communeOrCountyChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                if(newValue.toString().equals("gminy")){
                    showCommunesByVoivodeships = 1;
                    changeView(0);
                }else{
                    showCommunesByVoivodeships = 0;
                    changeView(0);
                }
            }
        });
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
                inVoivodeshipLabel.setText("w '" + masterName[unitsTreeIndexes[viewOrManage]] + "'");
                TableColumn master;
                if(showCommunesByVoivodeships == 0) {
                    master = new TableColumn<>("Powiaty w " + "'" + masterName[unitsTreeIndexes[viewOrManage]] + "'");
                }else {
                    master = new TableColumn<>("Gminy w " + "'" + masterName[unitsTreeIndexes[viewOrManage]] + "'");
                }
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
    public void changeItemsInMainTable(int viewOrManage){
        try {
            PageResult<?> requestResult;
            if(showCommunesByVoivodeships == 0) {
                requestResult = requestResultsReceiver.getResult(activeTables[viewOrManage],
                        unitsTreeIndexes[viewOrManage], unitsTree[unitsTreeIndexes[viewOrManage]], addressesAreChecked);
            }else {
                requestResult = requestResultsReceiver.communeByVoivodeshipId(unitsTree[unitsTreeIndexes[viewOrManage]], 1, Integer.MAX_VALUE);
            }
            setColumnsInMainTable(units[activeTables[viewOrManage] + unitsTreeIndexes[viewOrManage]
                    + addressesAreChecked + showCommunesByVoivodeships], viewOrManage);

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
    public void changeTableListener(int oldTab, int newTab, int viewOrManage){  //nasłuchiwacz zmiany zakładki np z województw na powiaty
        boolean changed = false;
        if(oldTab != -1){
            System.out.println("zmiana");
            tables[viewOrManage][oldTab].getSelectionModel().selectedItemProperty().removeListener(currentlyActiveTableListeners[viewOrManage]);
            changed = true;
        }
        unitsTreeIndexes[viewOrManage] = 0;
        if(viewOrManage == 1) {
            uiInteractionHandler.setAddButton();
            uiInteractionHandler.setEditButton();
        };
        boolean finalChanged = changed;
        if(finalChanged){
            unitsTreeIndexes[viewOrManage] = 0;
            activeTables[viewOrManage] = newTab;
            changeView(viewOrManage);
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
        manageTab.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if(manageTab.isSelected() && registeredOfficesCheckBox.isSelected()){
                    addressesAreChecked = 0;
                }else if(registeredOfficesCheckBox.isSelected()){
                    addressesAreChecked = 4;
                }

                if(manageTab.isSelected() && showCommunesByVoivodeships == 1){
                    showCommunesByVoivodeships = 0;
                }else if(communeOrCountyChoiceBox.getSelectionModel().isSelected(1)){
                    showCommunesByVoivodeships = 1;
                }
            }
        });
        TabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(oldValue.getId() + " -> " + newValue.getId());
            if(Objects.equals(newValue.getId(), "0")){
                viewingLabel.setVisible(false);
                communeOrCountyChoiceBox.getSelectionModel().select(0);
                communeOrCountyChoiceBox.setVisible(false);
                inVoivodeshipLabel.setVisible(false);
            }
            changeTableListener(Integer.parseInt(oldValue.getId()), Integer.parseInt(newValue.getId()), viewOrManage);
        });
    }
    public void setRowsFactories(){
        for(int i = 0; i < tables.length; i++){
            for(int j = 0; j < tables[i].length - (1+i); j++){
                int finalI = i;
                tables[i][j].setRowFactory(trf -> {
                    TableRow<?> row = new TableRow<>();
                    row.setOnMouseClicked(event -> {
                        if(!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() % 2 == 0 && showCommunesByVoivodeships != 1){
                            System.out.println(row.getItem());
                            if(unitsTreeIndexes[finalI] < (maxDepth)) {
                                unitsTreeIndexes[finalI]++;
                                if(unitsTreeIndexes[0] == 1 && !manageTab.isSelected() && voivodeshipViewTab.isSelected()) {
                                    viewingLabel.setVisible(true);
                                    communeOrCountyChoiceBox.getSelectionModel().select(0);
                                    communeOrCountyChoiceBox.setVisible(true);
                                    inVoivodeshipLabel.setVisible(true);
                                }
                                else{
                                    viewingLabel.setVisible(false);
                                    communeOrCountyChoiceBox.setVisible(false);
                                    inVoivodeshipLabel.setVisible(false);
                                }
                                if(finalI == 1){
                                    uiInteractionHandler.setAddButton();
                                    uiInteractionHandler.setEditButton();
                                }
                                try {
                                    unitsTree[unitsTreeIndexes[finalI]] = row.getItem().getClass().getField("id").get(row.getItem());
                                    masterName[unitsTreeIndexes[finalI]] = row.getItem().getClass().getField("name").get(row.getItem()).toString();
                                } catch (IllegalAccessException | NoSuchFieldException e) {
                                    throw new RuntimeException(e);
                                }
                                System.out.println(unitsTree[unitsTreeIndexes[finalI]]);
                                System.out.println("Selected item: " + unitsTree[unitsTreeIndexes[finalI]]);
                                changeView(finalI);
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
            if(unitsTreeIndexes[0] == 1) {
                viewingLabel.setVisible(true);
                communeOrCountyChoiceBox.getSelectionModel().select(0);
                communeOrCountyChoiceBox.setVisible(true);
                inVoivodeshipLabel.setVisible(true);
            }else {
                viewingLabel.setVisible(false);
                communeOrCountyChoiceBox.setVisible(false);
                inVoivodeshipLabel.setVisible(false);
            }
        }
        System.out.println("Wrócono do: " + unitsTree[unitsTreeIndexes[0]]);
        changeView(0);
    }
    AuthenticationService authenticationService = AuthenticationService.getInstance();
    boolean successfulLogin = false;
    boolean heIsBack = false;
    public void onLoginButtonClick(ActionEvent ignoredActionEvent) {
        System.out.println("przycisk wciśnięty");
        String login = loginTextField.getText();
        String password = passwordTextField.getText();



        Runnable authenticate = new Runnable() {
            @Override
            public void run() {
                if(authenticationService.authenticate(login,password)){
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
                    changeView(1);
                }else{
                    Platform.runLater(() -> {
                        loginFeedbackLabel.setText("Błędne dane logowania!");
                        loginFeedbackLabel.setVisible(true);
                    });

                }
            }
        };
        Thread authenticator = new Thread(authenticate);
        authenticator.start();

    }

    public void onManageBackButtonClick(ActionEvent ignoredActionEvent) {
        if(unitsTreeIndexes[1] > 0) {
            unitsTreeIndexes[1]--;
            if(unitsTreeIndexes[1] == 1) {

            }else {

            }
            uiInteractionHandler.setAddButton();
            uiInteractionHandler.setEditButton();
        }

        System.out.println("Wrócono do: " + unitsTree[unitsTreeIndexes[1]]);
        changeView(1);
    }
    public void onRefreshButtonClick(ActionEvent ignoredActionEvent) {
        changeView(0);
    }
    public void onManageRefreshButtonClick(ActionEvent ignoredActionEvent) {
        changeView(1);
    }
    public void onCheckboxChange(ActionEvent ignoredActionEvent) {
        if(registeredOfficesCheckBox.isSelected()){
            addressesAreChecked = 4;
        }else{
            addressesAreChecked = 0;
        }
        changeView(0);
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

    public void onDeleteButtonClick(ActionEvent ignoredActionEvent) throws Exception {
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
                    changeView(1);
                }
            } else if (countyForEditionOrDeletion.getId() != -1) {
                if (requestSender.deleteCounty(countyForEditionOrDeletion.getId())) {
                    changeView(1);
                }
            } else if (communeForEditionOrDeletion.getId() != -1) {
                if (requestSender.deleteCommune(communeForEditionOrDeletion.getId())) {
                    changeView(1);
                }
            }
        }
    }

    public void onEditButtonClick(ActionEvent ignoredActionEvent) {
        System.out.println("Editing");
        changeView(1);
    }

    public void onReportProblemButtonClick(ActionEvent ignoredActionEvent) {
        reportProblemButton.setDisable(true);
        System.out.println("Report problem");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("send-report-popup.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root, 800,600);
            scene.getStylesheets().addAll(Main.class.getResource("style.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Zgłoś problem");
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon.png")));
            stage.getIcons().add(icon);
            stage.setOnCloseRequest(windowEvent -> reportProblemButton.setDisable(false));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
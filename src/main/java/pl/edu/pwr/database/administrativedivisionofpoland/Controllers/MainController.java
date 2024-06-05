package pl.edu.pwr.database.administrativedivisionofpoland.Controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.*;
import pl.edu.pwr.database.administrativedivisionofpoland.Authentication.AuthenticationService;
import pl.edu.pwr.database.administrativedivisionofpoland.Authentication.IAuthenticationService;
import pl.edu.pwr.database.administrativedivisionofpoland.Data.DataReceiver;
import pl.edu.pwr.database.administrativedivisionofpoland.Data.DataSender;
import pl.edu.pwr.database.administrativedivisionofpoland.Handlers.NavigationHandler;
import pl.edu.pwr.database.administrativedivisionofpoland.Handlers.UIInteractionHandler;
import pl.edu.pwr.database.administrativedivisionofpoland.Initializers.*;
import pl.edu.pwr.database.administrativedivisionofpoland.Main;
import pl.edu.pwr.database.administrativedivisionofpoland.UserInput;

import java.io.*;
import java.lang.reflect.Field;
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
    public Label viewingLabelMan;
    public ChoiceBox<String> communeOrCountyChoiceBoxMan;
    public Label inVoivodeshipLabelMan;
    public Tab voivodeshipViewTabMan;
    @FXML private Tab voivodeshipViewTab;
    @FXML public Label viewingLabel;
    @FXML public Label inVoivodeshipLabel;
    @FXML public ChoiceBox<String> communeOrCountyChoiceBox;
    @FXML private Button reportProblemButton;
    @FXML public CheckBox registeredOfficesCheckBox;
    @FXML public TableView<ReportDto> reportsTableManage;
    @FXML public TableView<CountyDto> countiesTableManage;
    @FXML public TableView<CommuneDto> communesTableManage;
    @FXML public TableView<VoivodeshipDto> voivodeshipsTableManage;
    @FXML private TabPane manageUnitsTabPane;
    @FXML private Label loginFeedbackLabel;
    @FXML public TabPane viewUnitsTabPane;
    @FXML public TextField loginTextField;
    @FXML public PasswordField passwordTextField;
    @FXML public TabPane mainTabPane;
    @FXML public Tab manageTab;
    @FXML public TableView<VoivodeshipDto> voivodeshipsTable = new TableView<>();
    @FXML public TableView<CountyDto> countiesTable = new TableView<>();
    @FXML public TableView<CommuneDto> communesTable = new TableView<>();
    public TableView[][] tables;
    // Represents the hierarchy of administrative units: voivodeship, county, commune.
    // Each element stores the ID of the respective unit.
    public Object[] administrativeUnitHierarchyChain = new Object[]{-1,-1,-1};
    // Represents the current depth levels when navigating through TabPanes.
    public int[] tabPaneDepthLevels = new int[2];
    int[] activeTables = new int[]{0,0};
    int maxDepth = 2;
    String[] masterName = new String[3];
    DataReceiver requestResultsReceiver = new DataReceiver();
    DataSender requestSender = new DataSender();
    Class<?>[] units = new Class[]{VoivodeshipExtended.class, CountyExtended.class, CommuneDto.class, ReportDto.class,
            VoivodeshipAddressData.class, CountyAddressData.class, CommuneAddressData.class};
    public ChangeListener<?>[] currentlyActiveTableListeners;
    public int addressesAreChecked = 0;
    Thread tableUpdater = new Thread();
    public NavigationHandler navigationHandler;
    UIInteractionHandler uiInteractionHandler;
    public int showCommunesByVoivodeships = 0;
    IInitializer uiInitializer;
    IListenerInitializer listenerInitializer;
    IInitializer structureInitializer;

    public MainController() {
        this.navigationHandler = new NavigationHandler(this);
        this.uiInteractionHandler = new UIInteractionHandler(this);
        this.uiInitializer = new UIInitializer(this);
        this.listenerInitializer = new ListenerInitializer(this);
        this.structureInitializer = new StructureInitializer(this);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        navigationHandler.setupButtons();
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out),
                true, StandardCharsets.UTF_8)); //needed due to polish diacritics

        structureInitializer.initialize();
        uiInitializer.initialize();
        listenerInitializer.initialize();
    }

    public void changeView(int viewOrManage){
        tableUpdater.interrupt();
        Runnable updateTable = () -> {
            changeItemsInMainTable(viewOrManage);
        };
        tableUpdater = new Thread(updateTable);
        tableUpdater.start();
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

            if (tabPaneDepthLevels[viewOrManage] == 1 && activeTables[viewOrManage] == 0) {
                if(viewOrManage == 0){
                    inVoivodeshipLabel.setText("w '" + masterName[tabPaneDepthLevels[viewOrManage]] + "'");
                }else{
                    inVoivodeshipLabelMan.setText("w '" + masterName[tabPaneDepthLevels[viewOrManage]] + "'");
                }

                TableColumn master;
                if(showCommunesByVoivodeships == 0) {
                    master = new TableColumn<>("Powiaty w " + "'" + masterName[tabPaneDepthLevels[viewOrManage]] + "'");
                }else {
                    master = new TableColumn<>("Gminy w " + "'" + masterName[tabPaneDepthLevels[viewOrManage]] + "'");
                }
                master.getColumns().addAll(columnsToAdd);
                tables[viewOrManage][activeTables[viewOrManage]].getColumns().add(master);
            } else if (tabPaneDepthLevels[viewOrManage] == 2 && activeTables[viewOrManage] == 0 || tabPaneDepthLevels[viewOrManage] == 1 && activeTables[viewOrManage] == 1) {
                TableColumn master = new TableColumn<>("Gminy w " + "'" + masterName[tabPaneDepthLevels[viewOrManage]] + "'");
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
                        tabPaneDepthLevels[viewOrManage], administrativeUnitHierarchyChain[tabPaneDepthLevels[viewOrManage]], addressesAreChecked);
            }else {
                requestResult = requestResultsReceiver.communeByVoivodeshipId(administrativeUnitHierarchyChain[tabPaneDepthLevels[viewOrManage]], 1, Integer.MAX_VALUE);
            }
            setColumnsInMainTable(units[activeTables[viewOrManage] + tabPaneDepthLevels[viewOrManage]
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
        tabPaneDepthLevels[viewOrManage] = 0;
        if(viewOrManage == 1) {
            uiInteractionHandler.setAddButton();
            uiInteractionHandler.setEditButton();
        }
        boolean finalChanged = changed;
        if(finalChanged){
            tabPaneDepthLevels[viewOrManage] = 0;
            activeTables[viewOrManage] = newTab;
            changeView(viewOrManage);
        }
        currentlyActiveTableListeners[viewOrManage] = (ChangeListener<Object>) (observableValue, oldValue, newValue) -> {
            if(viewOrManage == 1){
                if(((tabPaneDepthLevels[1] == 2 && activeTables[1] == 0) || (tabPaneDepthLevels[1] == 1 && activeTables[1] == 1) || activeTables[1] == 2) && newValue != null){
                    try {
                        communeForEditionOrDeletion.setId((Integer) newValue.getClass().getField("id").get(newValue));
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    communeForEditionOrDeletion.setId(-1);
                }
                if(((activeTables[1] == 0 && tabPaneDepthLevels[1] == 1) || (activeTables[1] == 1 && tabPaneDepthLevels[1] == 0)) && newValue != null){
                    try {
                        countyForEditionOrDeletion.setId((Integer) newValue.getClass().getField("id").get(newValue));
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    countyForEditionOrDeletion.setId(-1);
                }
                if(newValue != null && activeTables[1] == 0 && tabPaneDepthLevels[1] == 0){
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

    public void increaseDepth(int finalI, TableRow<?> row) {
        if(tabPaneDepthLevels[finalI] < maxDepth){
            tabPaneDepthLevels[finalI]++;
            changeDepthDependentElements();
            try {
                administrativeUnitHierarchyChain[tabPaneDepthLevels[finalI]] = row.getItem().getClass().getField("id").get(row.getItem());
                masterName[tabPaneDepthLevels[finalI]] = row.getItem().getClass().getField("name").get(row.getItem()).toString();
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
            System.out.println(administrativeUnitHierarchyChain[tabPaneDepthLevels[finalI]]);
            System.out.println("Selected item: " + administrativeUnitHierarchyChain[tabPaneDepthLevels[finalI]]);
            changeView(finalI);
        }
    }
    private void changeDepthDependentElements() {
        if(manageTab.isSelected()){
            showOrHideCountyCommuneChoiceBoxInManageTab();
            uiInteractionHandler.setAddButton();
            uiInteractionHandler.setEditButton();
        }else{
            showOrHideCountyCommuneChoiceBoxInViewTab();
        }
    }
    private void showOrHideCountyCommuneChoiceBoxInManageTab() {
        if(tabPaneDepthLevels[1] == 1) {
            viewingLabelMan.setVisible(true);
            communeOrCountyChoiceBoxMan.getSelectionModel().select(0);
            communeOrCountyChoiceBoxMan.setVisible(true);
            inVoivodeshipLabelMan.setVisible(true);
        }else{
            viewingLabelMan.setVisible(false);
            communeOrCountyChoiceBoxMan.setVisible(false);
            inVoivodeshipLabelMan.setVisible(false);
        }
    }

    private void showOrHideCountyCommuneChoiceBoxInViewTab() {
        if(tabPaneDepthLevels[0] == 1) {
            viewingLabel.setVisible(true);
            communeOrCountyChoiceBox.getSelectionModel().select(0);
            communeOrCountyChoiceBox.setVisible(true);
            inVoivodeshipLabel.setVisible(true);
        }else{
            viewingLabel.setVisible(false);
            communeOrCountyChoiceBox.setVisible(false);
            inVoivodeshipLabel.setVisible(false);
        }
    }
    
    public void onBackButtonClick(ActionEvent ignoredActionEvent) {
        if(tabPaneDepthLevels[0] > 0) {
            tabPaneDepthLevels[0]--;
            showOrHideCountyCommuneChoiceBoxInViewTab();
        }
        System.out.println("Wrócono do: " + administrativeUnitHierarchyChain[tabPaneDepthLevels[0]]);
        changeView(0);
    }
    IAuthenticationService authenticationService = AuthenticationService.getInstance();

    public void onLoginButtonClick(ActionEvent ignoredActionEvent) {
        System.out.println("przycisk wciśnięty");
        String login = loginTextField.getText();
        String password = passwordTextField.getText();



        Runnable authenticate = () -> {
            if(authenticationService.authenticate(login,password)){
                listenerInitializer.setUpTabPaneListener(manageUnitsTabPane, 1);
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
        };
        Thread authenticator = new Thread(authenticate);
        authenticator.start();

    }

    public void onManageBackButtonClick(ActionEvent ignoredActionEvent) {
        if(tabPaneDepthLevels[1] > 0) {
            tabPaneDepthLevels[1]--;
            showOrHideCountyCommuneChoiceBoxInManageTab();
            uiInteractionHandler.setAddButton();
            uiInteractionHandler.setEditButton();
        }

        System.out.println("Wrócono do: " + administrativeUnitHierarchyChain[tabPaneDepthLevels[1]]);
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

    public void onDeleteButtonClick(ActionEvent ignoredActionEvent) throws Exception{
        deletionPrompt();
        if(UserInput.confirmed) {
            deleteSelectedUnit();
        }
    }
    
    private void deletionPrompt() throws Exception {
        if (voivodeshipForEditionOrDeletion.getId() != -1) {
            UserInput.prompt = "\nusunąć to województwo?";
        } else if (countyForEditionOrDeletion.getId() != -1) {
            UserInput.prompt = "\nusunąć ten powiat?";
        } else if (communeForEditionOrDeletion.getId() != -1) {
            UserInput.prompt = "\nusunąć tą gminę?";
        } else {
            return;
        }

        UserInput.getConfirmation();
    }
        
    public void deleteSelectedUnit() throws Exception {
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
            scene.getStylesheets().addAll(Objects.requireNonNull(Main.class.getResource("style.css")).toExternalForm());
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
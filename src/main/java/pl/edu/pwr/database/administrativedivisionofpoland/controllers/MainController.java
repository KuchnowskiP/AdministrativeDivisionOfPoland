package pl.edu.pwr.database.administrativedivisionofpoland.controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.*;
import pl.edu.pwr.database.administrativedivisionofpoland.authentication.IAuthenticationService;
import pl.edu.pwr.database.administrativedivisionofpoland.data.IDataSender;
import pl.edu.pwr.database.administrativedivisionofpoland.data.IResultReceiver;
import pl.edu.pwr.database.administrativedivisionofpoland.handlers.DeletionHandler;
import pl.edu.pwr.database.administrativedivisionofpoland.handlers.NavigationHandler;
import pl.edu.pwr.database.administrativedivisionofpoland.handlers.UIInteractionHandler;
import pl.edu.pwr.database.administrativedivisionofpoland.handlers.IDeletionHandler;
import pl.edu.pwr.database.administrativedivisionofpoland.initializers.ListenerInitializer;
import pl.edu.pwr.database.administrativedivisionofpoland.initializers.StructureInitializer;
import pl.edu.pwr.database.administrativedivisionofpoland.initializers.UIInitializer;
import pl.edu.pwr.database.administrativedivisionofpoland.initializers.IInitializer;
import pl.edu.pwr.database.administrativedivisionofpoland.initializers.IListenerInitializer;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    public ImageView flagImage;
    @FXML
    public ImageView emblemImage;
    @FXML
    public Button voivodeshipTabEditUnitButton;
    @FXML
    public Button countyTabEditUnitButton;
    @FXML
    public Button communeTabEditUnitButton;
    @FXML public Button communeTabAddUnitButton;
    @FXML public Button countyTabAddUnitButton;
    @FXML public Button voivodeshipTabAddUnitButton;
    @FXML public Button showHistoricalDataButton;
    @FXML
    public Label viewingLabelMan;
    @FXML
    public ChoiceBox<String> communeOrCountyChoiceBoxMan;
    @FXML
    public Label inVoivodeshipLabelMan;
    @FXML
    public Tab voivodeshipViewTabMan;
    @FXML private Tab voivodeshipViewTab;
    @FXML public Label viewingLabel;
    @FXML public Label inVoivodeshipLabel;
    @FXML public ChoiceBox<String> communeOrCountyChoiceBox;
    @FXML
    public Button reportProblemButton;
    @FXML public CheckBox registeredOfficesCheckBox;
    @FXML public TableView<ReportDto> reportsTableManage;
    @FXML public TableView<CountyDto> countiesTableManage;
    @FXML public TableView<CommuneDto> communesTableManage;
    @FXML public TableView<VoivodeshipDto> voivodeshipsTableManage;
    @FXML public TabPane manageUnitsTabPane;
    @FXML public Label loginFeedbackLabel;
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
    public int[] administrativeUnitHierarchyChain = new int[]{-1,-1,-1};
    // Represents the current depth levels when navigating through TabPanes.
    public int[] tabPaneDepthLevels = new int[2];
    int[] activeTables = new int[]{0,0};
    int maxDepth = 2;
    String[] masterName = new String[3];

    Class<?>[] units = new Class[]{VoivodeshipExtended.class, CountyExtended.class, CommuneDto.class, ReportDto.class,
            VoivodeshipAddressData.class, CountyAddressData.class, CommuneAddressData.class};
    public ChangeListener<?>[] currentlyActiveTableListeners;
    public int addressesAreChecked = 0;
    Thread tableUpdater = new Thread();
    public NavigationHandler navigationHandler;
    public UIInteractionHandler uiInteractionHandler;
    public int showCommunesByVoivodeships = 0;
    IInitializer uiInitializer;
    public IListenerInitializer listenerInitializer;
    IInitializer structureInitializer;
    public IAuthenticationService authenticationService;
    IDeletionHandler deletionHandler;

    IDataSender dataSender;
    IResultReceiver resultFetcher;

    public MainController (IAuthenticationService authenticationService,IDataSender dataSender, IResultReceiver resultFetcher) {
        this.uiInteractionHandler = new UIInteractionHandler(this);
        this.uiInitializer = new UIInitializer(this);
        this.listenerInitializer = new ListenerInitializer(this);
        this.structureInitializer = new StructureInitializer(this);
        this.dataSender = dataSender;
        this.resultFetcher = resultFetcher;
        this.authenticationService = authenticationService;
        this.navigationHandler = new NavigationHandler(this, this.resultFetcher, this.dataSender);
        this.deletionHandler = new DeletionHandler(this, this.dataSender);
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
                requestResult = resultFetcher.getResult(activeTables[viewOrManage],
                        tabPaneDepthLevels[viewOrManage], administrativeUnitHierarchyChain[tabPaneDepthLevels[viewOrManage]], addressesAreChecked);
            }else {
                requestResult = resultFetcher.communeByVoivodeshipId(administrativeUnitHierarchyChain[tabPaneDepthLevels[viewOrManage]], 1, Integer.MAX_VALUE);
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
                administrativeUnitHierarchyChain[tabPaneDepthLevels[finalI]] = (int) row.getItem().getClass().getField("id").get(row.getItem());
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
    public void showOrHideCountyCommuneChoiceBoxInManageTab() {
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

    public void showOrHideCountyCommuneChoiceBoxInViewTab() {
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
        navigationHandler.goBackInTabDepth(ignoredActionEvent);
    }
    public void onLoginButtonClick(ActionEvent ignoredActionEvent) {
        navigationHandler.onLoginButtonClick(ignoredActionEvent);
    }
    public void onManageBackButtonClick(ActionEvent ignoredActionEvent) {
        navigationHandler.goBackInManageTabDepth(ignoredActionEvent);
    }
    public void onRefreshButtonClick(ActionEvent ignoredActionEvent) {
        navigationHandler.refreshView(ignoredActionEvent);
    }
    public void onManageRefreshButtonClick(ActionEvent ignoredActionEvent) {
        navigationHandler.refreshManageView(ignoredActionEvent);
    }
    public void onCheckboxChange(ActionEvent ignoredActionEvent) {
        navigationHandler.displayUnitsWithAddresses(ignoredActionEvent);
    }

    public void onShowHistoricalDataButtonClick(ActionEvent ignoredActionEvent) {
        navigationHandler.openHistoricalDataWindow(ignoredActionEvent);
    }

    public void onDeleteButtonClick(ActionEvent ignoredActionEvent) throws Exception{
        deletionHandler.onDeleteButtonClick(ignoredActionEvent);
    }

    public void onReportProblemButtonClick(ActionEvent ignoredActionEvent) {
        navigationHandler.openSendReportProblemWindow(ignoredActionEvent);
    }
}
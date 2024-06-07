package pl.edu.pwr.database.administrativedivisionofpoland.initializers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableRow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import pl.edu.pwr.database.administrativedivisionofpoland.controllers.MainController;
import pl.edu.pwr.database.administrativedivisionofpoland.initializers.api.IInitializer;
import pl.edu.pwr.database.administrativedivisionofpoland.initializers.api.IListenerInitializer;

import java.util.Objects;

public class ListenerInitializer implements IInitializer, IListenerInitializer {
    MainController mainController;

    public ListenerInitializer(MainController mainController){
        this.mainController = mainController;
    }

    @Override
    public void initialize() {
        try {
            setListeners();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void setListeners() {
        mainController.changeTableListener(-1,0,0);
        setUpManageTabListener();
        setUpTabPaneListener(mainController.viewUnitsTabPane, 0);
        setRowsFactories();
        setEnterKeyPressedEvent();
        setCommuneOrCountyChoiceBox();
    }

    public void setUpManageTabListener() {
        mainController.manageTab.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if(mainController.manageTab.isSelected() && mainController.registeredOfficesCheckBox.isSelected()){
                    mainController.addressesAreChecked = 0;
                }else if(mainController.registeredOfficesCheckBox.isSelected()){
                    mainController.addressesAreChecked = 4;
                }

                if(mainController.manageTab.isSelected() && mainController.showCommunesByVoivodeships == 1){
                    mainController.showCommunesByVoivodeships = 0;
                }else if(mainController.communeOrCountyChoiceBox.getSelectionModel().isSelected(1)){
                    mainController.showCommunesByVoivodeships = 1;
                }
            }
        });
    }

    @Override
    public void setUpTabPaneListener(TabPane TabPane, int viewOrManage) {
        TabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(oldValue.getId() + " -> " + newValue.getId());
            if(Objects.equals(newValue.getId(), "0")){
                mainController.viewingLabel.setVisible(false);
                mainController.communeOrCountyChoiceBox.getSelectionModel().select(0);
                mainController.communeOrCountyChoiceBox.setVisible(false);
                mainController.inVoivodeshipLabel.setVisible(false);
            }
            mainController.changeTableListener(Integer.parseInt(oldValue.getId()), Integer.parseInt(newValue.getId()), viewOrManage);
        });
    }

    public void setEnterKeyPressedEvent(){
        mainController.loginTextField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                mainController.onLoginButtonClick(new ActionEvent());
            }
        });
        mainController.passwordTextField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                mainController.onLoginButtonClick(new ActionEvent());
            }
        });
    }

    public void setCommuneOrCountyChoiceBox(){
        mainController.communeOrCountyChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                if(newValue.toString().equals("gminy")){
                    mainController.showCommunesByVoivodeships = 1;
                    mainController.changeView(0);
                }else{
                    mainController.showCommunesByVoivodeships = 0;
                    mainController.changeView(0);
                }
            }
        });
        mainController.communeOrCountyChoiceBoxMan.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                if(newValue.toString().equals("gminy")){
                    mainController.showCommunesByVoivodeships = 1;
                    mainController.changeView(1);
                }else{
                    mainController.showCommunesByVoivodeships = 0;
                    mainController.changeView(1);
                }
            }
        });
    }

    public void setRowsFactories(){
        for(int i = 0; i < mainController.tables.length; i++){
            for(int j = 0; j < mainController.tables[i].length - (1+i); j++){
                int finalI = i;
                mainController.tables[i][j].setRowFactory(trf -> {
                    TableRow<?> row = new TableRow<>();
                    row.setOnMouseClicked(event -> {
                        if(!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() % 2 == 0 && mainController.showCommunesByVoivodeships != 1){
                            System.out.println(row.getItem());
                            mainController.increaseDepth(finalI, row);
                        }
                    });
                    return row;
                });
            }
        }
    }
}

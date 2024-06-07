package pl.edu.pwr.database.administrativedivisionofpoland.handlers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import pl.edu.pwr.database.administrativedivisionofpoland.controllers.MainController;

import java.io.IOException;

public class UIInteractionHandler {
    MainController mainController;
    public UIInteractionHandler(MainController mainController){
        this.mainController = mainController;
    }

    public void setAddButton(){
        if(mainController.tabPaneDepthLevels[1] == 0){
            mainController.voivodeshipTabAddUnitButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
                        mainController.navigationHandler.openAddVoivodeshipPopup(actionEvent);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            mainController.countyTabAddUnitButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
                        mainController.navigationHandler.openAddCountyPopup(actionEvent);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        if(mainController.tabPaneDepthLevels[1] == 1){
            mainController.voivodeshipTabAddUnitButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
                        mainController.navigationHandler.openAddCountyPopup(actionEvent);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            mainController.countyTabAddUnitButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
                        mainController.navigationHandler.openAddCommunePopup(actionEvent);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        if(mainController.tabPaneDepthLevels[1] == 2){
            mainController.voivodeshipTabAddUnitButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
                        mainController.navigationHandler.openAddCommunePopup(actionEvent);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    public void setEditButton() {
        if(mainController.tabPaneDepthLevels[1] == 0){
            mainController.voivodeshipTabEditUnitButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
                        mainController.navigationHandler.openEditVoivodeshipPopup(actionEvent);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            mainController.countyTabEditUnitButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
                        mainController.navigationHandler.openEditCountyPopup(actionEvent);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        if(mainController.tabPaneDepthLevels[1] == 1){
            mainController.voivodeshipTabEditUnitButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
                        mainController.navigationHandler.openEditCountyPopup(actionEvent);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            mainController.countyTabEditUnitButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
                        mainController.navigationHandler.openEditCommunePopup(actionEvent);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        if(mainController.tabPaneDepthLevels[1] == 2){
            mainController.voivodeshipTabEditUnitButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
                        mainController.navigationHandler.openEditCommunePopup(actionEvent);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }
}

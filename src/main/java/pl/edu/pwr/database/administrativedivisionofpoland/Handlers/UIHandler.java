package pl.edu.pwr.database.administrativedivisionofpoland.Handlers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import pl.edu.pwr.database.administrativedivisionofpoland.Controllers.MainController;

import java.io.IOException;

public class UIHandler {
    MainController mainController;
    public UIHandler(MainController mainController){
        this.mainController = mainController;
    }

    public void setAddButton(){
        if(mainController.unitsTreeIndexes[1] == 0){
            mainController.voivodeshipTabAddUnitButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
                        mainController.eventsHandler.onAddVoivodeshipButtonClick(actionEvent);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            mainController.countyTabAddUnitButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
                        mainController.eventsHandler.onAddCountyButtonClick(actionEvent);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        if(mainController.unitsTreeIndexes[1] == 1){
            mainController.voivodeshipTabAddUnitButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
                        mainController.eventsHandler.onAddCountyButtonClick(actionEvent);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            mainController.countyTabAddUnitButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
                        mainController.eventsHandler.onAddCommuneButtonClick(actionEvent);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        if(mainController.unitsTreeIndexes[1] == 2){
            mainController.voivodeshipTabAddUnitButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
                        mainController.eventsHandler.onAddCommuneButtonClick(actionEvent);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }
}

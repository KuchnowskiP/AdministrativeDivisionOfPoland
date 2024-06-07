package pl.edu.pwr.database.administrativedivisionofpoland.handlers;

import javafx.event.ActionEvent;
import pl.edu.pwr.database.administrativedivisionofpoland.UserInput;
import pl.edu.pwr.database.administrativedivisionofpoland.controllers.MainController;
import pl.edu.pwr.database.administrativedivisionofpoland.data.api.IDataSender;
import pl.edu.pwr.database.administrativedivisionofpoland.handlers.api.IDeletionHandler;

public class DeletionHandler implements IDeletionHandler {
    private final MainController mainController;
    IDataSender dataSender;

    public DeletionHandler(MainController mainController, IDataSender dataSender) {
        this.mainController = mainController;
        this.dataSender = dataSender;
    }

    @Override
    public void onDeleteButtonClick(ActionEvent ignoredActionEvent) throws Exception {
        deletionPrompt();
        if (UserInput.confirmed) {
            deleteSelectedUnit();
        }
    }

    private void deletionPrompt() throws Exception {
        if (mainController.voivodeshipForEditionOrDeletion.getId() != -1) {
            UserInput.prompt = "\nusunąć to województwo?";
        } else if (mainController.countyForEditionOrDeletion.getId() != -1) {
            UserInput.prompt = "\nusunąć ten powiat?";
        } else if (mainController.communeForEditionOrDeletion.getId() != -1) {
            UserInput.prompt = "\nusunąć tą gminę?";
        } else {
            return;
        }

        UserInput.getConfirmation();
    }

    public void deleteSelectedUnit() throws Exception {
        if (mainController.voivodeshipForEditionOrDeletion.getId() != -1) {
            if (dataSender.deleteVoivodeship(mainController.voivodeshipForEditionOrDeletion.getId())) {
                mainController.changeView(1);
            }
        } else if (mainController.countyForEditionOrDeletion.getId() != -1) {
            if (dataSender.deleteCounty(mainController.countyForEditionOrDeletion.getId())) {
                mainController.changeView(1);
            }
        } else if (mainController.communeForEditionOrDeletion.getId() != -1) {
            if (dataSender.deleteCommune(mainController.communeForEditionOrDeletion.getId())) {
                mainController.changeView(1);
            }
        }
    }
}

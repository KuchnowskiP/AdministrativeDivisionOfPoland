package pl.edu.pwr.database.administrativedivisionofpoland.initializers;

import pl.edu.pwr.database.administrativedivisionofpoland.controllers.MainController;

public class UIInitializer implements IInitializer {
    MainController mainController;

    public UIInitializer(MainController mainController){
        this.mainController = mainController;
    }

    @Override
    public void initialize(){
        try {
            setInitialView();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setInitialView() {
        mainController.mainTabPane.getTabs().remove(mainController.manageTab); //hiding managing tab. Will be open after singing in.
        mainController.changeView(0); //setting content of table
    }
}

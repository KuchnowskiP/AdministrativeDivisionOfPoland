package pl.edu.pwr.database.administrativedivisionofpoland.Initializers;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.TableView;
import pl.edu.pwr.database.administrativedivisionofpoland.Controllers.MainController;

public class StructureInitializer implements IInitializer{
    MainController mainController;

    public StructureInitializer(MainController mainController){
        this.mainController = mainController;
    }

    @Override
    public void initialize() {
        try {
            setInitialStructure();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setInitialStructure() throws Exception {
        mainController.tables = new TableView<?>[][]{
                {mainController.voivodeshipsTable, mainController.countiesTable, mainController.communesTable},
                {mainController.voivodeshipsTableManage,mainController. countiesTableManage,
                        mainController.communesTableManage, mainController.reportsTableManage}};
        mainController.currentlyActiveTableListeners = new ChangeListener[2];
        mainController.communeOrCountyChoiceBox.getItems().addAll("powiaty", "gminy");
        mainController.communeOrCountyChoiceBoxMan.getItems().addAll("powiaty", "gminy");
    }
}

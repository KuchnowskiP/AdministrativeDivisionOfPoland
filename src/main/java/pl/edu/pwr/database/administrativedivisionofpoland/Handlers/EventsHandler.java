package pl.edu.pwr.database.administrativedivisionofpoland.Handlers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pl.edu.pwr.database.administrativedivisionofpoland.Controllers.AddCommunePopupController;
import pl.edu.pwr.database.administrativedivisionofpoland.Controllers.AddCountyPopupController;
import pl.edu.pwr.database.administrativedivisionofpoland.Controllers.AddVoivodeshipPopupController;
import pl.edu.pwr.database.administrativedivisionofpoland.Controllers.MainController;
import pl.edu.pwr.database.administrativedivisionofpoland.UserData;

import java.io.IOException;
import java.util.Objects;

public class EventsHandler {
    MainController mainController;
    public EventsHandler(MainController mainController){
        this.mainController = mainController;
    }

    public void setupButtons(){
        mainController.voivodeshipTabAddUnitButton.setOnAction(actionEvent -> {
            try {
                onAddVoivodeshipButtonClick(actionEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        mainController.countyTabAddUnitButton.setOnAction(actionEvent -> {
            try {
                onAddCountyButtonClick(actionEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        mainController.communeTabAddUnitButton.setOnAction(actionEvent -> {
            try {
                onAddCommuneButtonClick(actionEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        mainController.voivodeshipTabEditUnitButton.setOnAction(actionEvent -> {
            try {
                onEditVoivodeshipButtonClick(actionEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        mainController.countyTabEditUnitButton.setOnAction(actionEvent -> {
            try {
                onEditCountyButtonClick(actionEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        mainController.communeTabEditUnitButton.setOnAction(actionEvent -> {
            try {
                onEditCommuneButtonClick(actionEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void onAddVoivodeshipButtonClick(ActionEvent ignoredActionEvent) throws IOException {
        System.out.println("Adding voivodeship");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AddVoivodeshipPopupController.class.getResource("add-voivodeship-popup.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root, 800,600);
            scene.getStylesheets().addAll(this.getClass().getResource(("pop-up-style.css")).toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Podaj dane nowego województwa");
            Image icon = new Image(Objects.requireNonNull(AddVoivodeshipPopupController.class.getResourceAsStream("icon.png")));
            stage.getIcons().add(icon);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            mainController.changeView(mainController.unitsTree[mainController.unitsTreeIndexes[1]],1);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void onAddCountyButtonClick(ActionEvent ignoredActionEvent) throws IOException {
        System.out.println("Adding county");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AddCountyPopupController.class.getResource("add-county-popup.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root, 800,600);
            scene.getStylesheets().addAll(this.getClass().getResource(("pop-up-style.css")).toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Podaj dane nowego powiatu");
            Image icon = new Image(Objects.requireNonNull(AddCountyPopupController.class.getResourceAsStream("icon.png")));
            stage.getIcons().add(icon);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void onAddCommuneButtonClick(ActionEvent actionEvent) throws IOException {
        System.out.println("Adding commune");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AddCommunePopupController.class.getResource("add-commune-popup.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root, 800,600);
            scene.getStylesheets().addAll(this.getClass().getResource(("pop-up-style.css")).toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Podaj dane nowej gminy");
            Image icon = new Image(Objects.requireNonNull(AddCommunePopupController.class.getResourceAsStream("icon.png")));
            stage.getIcons().add(icon);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void onEditVoivodeshipButtonClick(ActionEvent actionEvent) throws IOException{
        if(mainController.voivodeshipForEditionOrDeletion.getId() == -1){
            return;
        }
        System.out.println("Editing voivodeship");
        try {
            UserData.unit = mainController.voivodeshipForEditionOrDeletion;
            FXMLLoader fxmlLoader = new FXMLLoader(AddVoivodeshipPopupController.class.getResource("edit-voivodeship-popup.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root, 800,600);
            scene.getStylesheets().addAll(this.getClass().getResource(("pop-up-style.css")).toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Podaj dane województwa");
            Image icon = new Image(Objects.requireNonNull(AddVoivodeshipPopupController.class.getResourceAsStream("icon.png")));
            stage.getIcons().add(icon);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            mainController.changeView(mainController.unitsTree[mainController.unitsTreeIndexes[1]],1);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void onEditCountyButtonClick(ActionEvent actionEvent) throws IOException{
        if(mainController.countyForEditionOrDeletion.getId() == -1){
            return;
        }
        System.out.println("Editing county");
        try {
            UserData.unit = mainController.countyForEditionOrDeletion;
            FXMLLoader fxmlLoader = new FXMLLoader(AddCountyPopupController.class.getResource("edit-county-popup.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root, 800,600);
            scene.getStylesheets().addAll(this.getClass().getResource(("pop-up-style.css")).toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Podaj dane powiatu");
            Image icon = new Image(Objects.requireNonNull(AddCountyPopupController.class.getResourceAsStream("icon.png")));
            stage.getIcons().add(icon);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            mainController.changeView(mainController.unitsTree[mainController.unitsTreeIndexes[1]],1);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void onEditCommuneButtonClick(ActionEvent actionEvent) throws IOException{
        if(mainController.communeForEditionOrDeletion.getId() == -1){
            return;
        }
        System.out.println("Editing commune");
        try {
            UserData.unit = mainController.communeForEditionOrDeletion;
            FXMLLoader fxmlLoader = new FXMLLoader(AddCommunePopupController.class.getResource("edit-commune-popup.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root, 800,600);
            scene.getStylesheets().addAll(this.getClass().getResource(("pop-up-style.css")).toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Podaj dane gminy");
            Image icon = new Image(Objects.requireNonNull(AddCommunePopupController.class.getResourceAsStream("icon.png")));
            stage.getIcons().add(icon);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            mainController.changeView(mainController.unitsTree[mainController.unitsTreeIndexes[1]],1);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
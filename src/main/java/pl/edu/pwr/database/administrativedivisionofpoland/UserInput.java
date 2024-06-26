package pl.edu.pwr.database.administrativedivisionofpoland;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pl.edu.pwr.database.administrativedivisionofpoland.controllers.AddCommunePopupController;
import pl.edu.pwr.database.administrativedivisionofpoland.controllers.ConfirmationPopupController;

import java.io.IOException;
import java.util.Objects;

public class UserInput {
    public static boolean confirmed = false;
    public static String prompt = "blah";
    public static Object unit = -1;
    public static void getConfirmation() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AddCommunePopupController.class.getResource("confirmation.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root, 280,160);
        scene.getStylesheets().addAll(Objects.requireNonNull(ConfirmationPopupController.class.getResource(("confirmation-style.css"))).toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Potwierdź operację");
        Image icon = new Image(Objects.requireNonNull(AddCommunePopupController.class.getResourceAsStream("icon.png")));
        stage.getIcons().add(icon);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}

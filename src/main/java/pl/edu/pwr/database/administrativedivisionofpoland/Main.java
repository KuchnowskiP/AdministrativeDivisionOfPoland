package pl.edu.pwr.database.administrativedivisionofpoland;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pl.edu.pwr.database.administrativedivisionofpoland.authentication.AuthenticationService;
import pl.edu.pwr.database.administrativedivisionofpoland.authentication.IAuthenticationService;
import pl.edu.pwr.database.administrativedivisionofpoland.builders.MainControllerBuilder;
import pl.edu.pwr.database.administrativedivisionofpoland.controllers.MainController;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        IAuthenticationService authenticationService = AuthenticationService.getInstance();
        HttpClient httpClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().build();
        String serverAddress = Config.getProperty("server.address");
        String serverPort = Config.getProperty("server.port");

        MainController mainController = new MainControllerBuilder()
                .setAuthenticationService(authenticationService)
                .setHttpClient(httpClient)
                .setObjectMapper(objectMapper)
                .setServerAddress(serverAddress)
                .setServerPort(serverPort)
                .setVoivodeshipGetter()
                .setVoivodeshipExtendedGetter()
                .setVoivodeshipAddressGetter()
                .setVoivodeshipHistorian()
                .setVoivodeshipTerytProvider()
                .setCountyExtendedGetter()
                .setCountyAddressGetter()
                .setCountyHistorian()
                .setCountyTerytProvider()
                .setCountyByIdGetter()
                .setCommuneGetter()
                .setCommuneAddressGetter()
                .setCommuneHistorian()
                .setCommuneTerytProvider()
                .setCommuneByIdGetter()
                .setCommuneByGPGetter()
                .setReportGetter()
                .setAddressGetter()
                .setVoivodeshipCreator()
                .setCountyCreator()
                .setCommuneCreator()
                .setReportCreator()
                .setAddressCreator()
                .setVoivodeshipEditor()
                .setCountyEditor()
                .setCommuneEditor()
                .setVoivodeshipDeleter()
                .setCountyDeleter()
                .setCommuneDeleter()
                .setDataSender()
                .setResultFetcher()
                .build();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("controllers/main-view.fxml"));
        fxmlLoader.setControllerFactory(c -> mainController);
        Scene scene = new Scene(fxmlLoader.load(), 1280, 800);
        scene.getStylesheets().addAll(Objects.requireNonNull(this.getClass().getResource("style.css")).toExternalForm());
        stage.setTitle("System zarządzania danymi o podziale administracyjnym Polski");
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("controllers/icon.png")));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
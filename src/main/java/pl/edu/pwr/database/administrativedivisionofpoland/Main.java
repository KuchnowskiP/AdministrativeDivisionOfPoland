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
import pl.edu.pwr.database.administrativedivisionofpoland.builders.data.fetchers.*;
import pl.edu.pwr.database.administrativedivisionofpoland.builders.data.managers.DataCreationManagerBuilder;
import pl.edu.pwr.database.administrativedivisionofpoland.builders.data.managers.DataDeletionManagerBuilder;
import pl.edu.pwr.database.administrativedivisionofpoland.builders.data.managers.DataEditingManagerBuilder;
import pl.edu.pwr.database.administrativedivisionofpoland.builders.data.managers.DataManagerDirector;
import pl.edu.pwr.database.administrativedivisionofpoland.builders.data.services.UnitServiceDirector;
import pl.edu.pwr.database.administrativedivisionofpoland.builders.data.services.*;
import pl.edu.pwr.database.administrativedivisionofpoland.controllers.MainController;
import pl.edu.pwr.database.administrativedivisionofpoland.data.DataSender;
import pl.edu.pwr.database.administrativedivisionofpoland.data.IDataSender;
import pl.edu.pwr.database.administrativedivisionofpoland.data.IResultReceiver;
import pl.edu.pwr.database.administrativedivisionofpoland.data.ResultReceiver;
import pl.edu.pwr.database.administrativedivisionofpoland.data.fetchers.AddressDataFetcher;
import pl.edu.pwr.database.administrativedivisionofpoland.data.fetchers.CommuneDataFetcher;
import pl.edu.pwr.database.administrativedivisionofpoland.data.fetchers.CountyDataFetcher;
import pl.edu.pwr.database.administrativedivisionofpoland.data.fetchers.VoivodeshipDataFetcher;
import pl.edu.pwr.database.administrativedivisionofpoland.data.fetchers.ReportDataDataFetcher;
import pl.edu.pwr.database.administrativedivisionofpoland.data.managers.DataCreationManager;
import pl.edu.pwr.database.administrativedivisionofpoland.data.managers.DataDeletionManager;
import pl.edu.pwr.database.administrativedivisionofpoland.data.managers.DataEditingManager;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.*;

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

        UnitServiceDirector unitServiceDirector = new UnitServiceDirector(
                authenticationService, httpClient, objectMapper, serverAddress, serverPort
        );

        VoivodeshipServiceBuilder voivodeshipServiceBuilder = new VoivodeshipServiceBuilder();
        unitServiceDirector.constructAuthenticatableUnitService(voivodeshipServiceBuilder);
        VoivodeshipService voivodeshipService = voivodeshipServiceBuilder.getResult();

        CountyServiceBuilder countyServiceBuilder = new CountyServiceBuilder();
        unitServiceDirector.constructAuthenticatableUnitService(countyServiceBuilder);
        CountyService countyService = countyServiceBuilder.getResult();

        CommuneServiceBuilder communeServiceBuilder = new CommuneServiceBuilder();
        unitServiceDirector.constructAuthenticatableUnitService(communeServiceBuilder);
        CommuneService communeService = communeServiceBuilder.getResult();

        ReportServiceBuilder reportServiceBuilder = new ReportServiceBuilder();
        unitServiceDirector.constructUnitService(reportServiceBuilder);
        ReportService reportService = reportServiceBuilder.getResult();

        AddressServiceBuilder addressServiceBuilder = new AddressServiceBuilder();
        unitServiceDirector.constructUnitService(addressServiceBuilder);
        AddressService addressService = addressServiceBuilder.getResult();

        DataManagerDirector dataManagerDirector = new DataManagerDirector(
                voivodeshipService,
                countyService,
                communeService,
                reportService,
                addressService
        );
        DataCreationManagerBuilder dataCreationManagerBuilder = new DataCreationManagerBuilder();
        dataManagerDirector.constructCreationManager(dataCreationManagerBuilder);
        DataCreationManager dataCreationManager = dataCreationManagerBuilder.getResult();

        DataEditingManagerBuilder dataEditingManagerBuilder = new DataEditingManagerBuilder();
        dataManagerDirector.constructEditingManager(dataEditingManagerBuilder);
        DataEditingManager dataEditingManager = dataEditingManagerBuilder.getResult();

        DataDeletionManagerBuilder dataDeletionManagerBuilder = new DataDeletionManagerBuilder();
        dataManagerDirector.constructDeletionManager(dataDeletionManagerBuilder);
        DataDeletionManager dataDeletionManager = dataDeletionManagerBuilder.getResult();

        IDataSender dataSender = new DataSender(dataCreationManager, dataEditingManager, dataDeletionManager);

        DataFetcherDirector dataFetcherDirector = new DataFetcherDirector(
                voivodeshipService,
                countyService,
                communeService,
                addressService,
                reportService
        );

        VoivodeshipDataFetcherBuilder voivodeshipDataFetcherBuilder = new VoivodeshipDataFetcherBuilder();
        dataFetcherDirector.constructVoivodeshipDataFetcher(voivodeshipDataFetcherBuilder);
        VoivodeshipDataFetcher voivodeshipDataFetcher = voivodeshipDataFetcherBuilder.getResult();

        CountyDataFetcherBuilder countyDataFetcherBuilder = new CountyDataFetcherBuilder();
        dataFetcherDirector.constructCountyDataFetcher(countyDataFetcherBuilder);
        CountyDataFetcher countyDataFetcher = countyDataFetcherBuilder.getResult();

        CommuneDataFetcherBuilder communeDataFetcherBuilder = new CommuneDataFetcherBuilder();
        dataFetcherDirector.constructCommuneDataFetcher(communeDataFetcherBuilder);
        CommuneDataFetcher communeDataFetcher = communeDataFetcherBuilder.getResult();

        ReportDataFetcherBuilder reportDataFetcherBuilder = new ReportDataFetcherBuilder();
        dataFetcherDirector.constructReportDataFetcher(reportDataFetcherBuilder);
        ReportDataDataFetcher reportDataFetcher = reportDataFetcherBuilder.getResult();

        AddressDataFetcherBuilder addressDataFetcherBuilder = new AddressDataFetcherBuilder();
        dataFetcherDirector.constructOfficeAddressDataFetcher(addressDataFetcherBuilder);
        AddressDataFetcher addressDataFetcher = addressDataFetcherBuilder.getResult();

        IResultReceiver resultReceiver = new ResultReceiver(
                voivodeshipDataFetcher,
                countyDataFetcher,
                communeDataFetcher,
                reportDataFetcher,
                addressDataFetcher
        );

        MainController mainController = new MainController(authenticationService, dataSender, resultReceiver);

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
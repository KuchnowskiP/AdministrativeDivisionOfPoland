package pl.edu.pwr.database.administrativedivisionofpoland.builders;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.edu.pwr.contract.Commune.CommuneRequest;
import pl.edu.pwr.contract.County.CountyRequest;
import pl.edu.pwr.contract.Dtos.*;
import pl.edu.pwr.contract.History.CommuneHistoryDto;
import pl.edu.pwr.contract.History.CountyHistoryDto;
import pl.edu.pwr.contract.History.VoivodeshipHistoryDto;
import pl.edu.pwr.contract.OfficeAdres.OfficeAddressRequest;
import pl.edu.pwr.contract.Reports.AddReportRequest;
import pl.edu.pwr.contract.Voivodeship.VoivodeshipRequest;
import pl.edu.pwr.database.administrativedivisionofpoland.authentication.IAuthenticationService;
import pl.edu.pwr.database.administrativedivisionofpoland.controllers.MainController;
import pl.edu.pwr.database.administrativedivisionofpoland.data.DataSender;
import pl.edu.pwr.database.administrativedivisionofpoland.data.ResultFetcher;
import pl.edu.pwr.database.administrativedivisionofpoland.data.api.IDataSender;
import pl.edu.pwr.database.administrativedivisionofpoland.data.api.IResultFetcher;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.*;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.api.*;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public class MainControllerBuilder {
    private IAuthenticationService authenticationService;
    private HttpClient httpClient;
    private ObjectMapper objectMapper;
    private String serverAddress;
    private String serverPort;

    private Gettable<VoivodeshipDto> voivodeshipGetter;
    private GettableExtended<VoivodeshipExtended> voivodeshipExtendedGetter;
    private GettableAddress<VoivodeshipAddressData> voivodeshipAddressGetter;
    private Trackable<VoivodeshipHistoryDto> voivodeshipHistorian;
    private TerytProvider voivodeshipTerytProvider;

    private GettableExtended<CountyExtended> countyExtendedGetter;
    private GettableAddress<CountyAddressData> countyAddressGetter;
    private Trackable<CountyHistoryDto> countyHistorian;
    private TerytProvider countyTerytProvider;
    private GettableById<CountyDto> countyByIdGetter;

    private Gettable<CommuneDto> communeGetter;
    private GettableAddress<CommuneAddressData> communeAddressGetter;
    private Trackable<CommuneHistoryDto> communeHistorian;
    private TerytProvider communeTerytProvider;
    private GettableById<CommuneDto> communeByIdGetter;
    private GettableByGrandparentUnit<CommuneDto> communeByGPGetter;

    private Gettable<ReportDto> reportGetter;
    private Gettable<OfficeAddressDto> addressGetter;

    private Creatable<VoivodeshipRequest, Boolean> voivodeshipCreator;
    private Creatable<CountyRequest, Boolean> countyCreator;
    private Creatable<CommuneRequest, Boolean> communeCreator;
    private Creatable<AddReportRequest, HttpResponse<String>> reportCreator;
    private Creatable<OfficeAddressRequest, HttpResponse<String>> addressCreator;

    private Editable<VoivodeshipRequest> voivodeshipEditor;
    private Editable<CountyRequest> countyEditor;
    private Editable<CommuneRequest> communeEditor;

    private Deletable voivodeshipDeleter;
    private Deletable countyDeleter;
    private Deletable communeDeleter;

    private IDataSender dataSender;
    private IResultFetcher resultFetcher;

    private void checkRequiredUnitFields(){
        if(
            this.authenticationService == null ||
            this.httpClient == null ||
            this.objectMapper == null ||
            this.serverAddress == null ||
            this.serverPort == null
        )
            throw new IllegalStateException("Not all required fields are set");
    }

    private void checkRequiredFieldsWithoutAuthentication(){
        if(
            this.httpClient == null ||
            this.objectMapper == null ||
            this.serverAddress == null ||
            this.serverPort == null
        )
            throw new IllegalStateException("Not all required fields are set");
    }

    public MainControllerBuilder setAuthenticationService(IAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
        return this;
    }

    public MainControllerBuilder setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    public MainControllerBuilder setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        return this;
    }

    public MainControllerBuilder setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
        return this;
    }

    public MainControllerBuilder setServerPort(String serverPort) {
        this.serverPort = serverPort;
        return this;
    }

    public MainControllerBuilder setVoivodeshipGetter() {
        checkRequiredUnitFields();
        this.voivodeshipGetter = new VoivodeshipService(
                this.authenticationService,
                this.httpClient,
                this.objectMapper,
                this.serverAddress,
                this.serverPort
        );
        return this;
    }

    public MainControllerBuilder setVoivodeshipExtendedGetter() {
        checkRequiredUnitFields();
        this.voivodeshipExtendedGetter = new VoivodeshipService(
                this.authenticationService,
                this.httpClient,
                this.objectMapper,
                this.serverAddress,
                this.serverPort);
        return this;
    }

    public MainControllerBuilder setVoivodeshipAddressGetter() {
        checkRequiredUnitFields();
        this.voivodeshipAddressGetter = new VoivodeshipService(
                this.authenticationService,
                this.httpClient,
                this.objectMapper,
                this.serverAddress,
                this.serverPort
        );
        return this;
    }

    public MainControllerBuilder setVoivodeshipHistorian() {
        checkRequiredUnitFields();
        this.voivodeshipHistorian = new VoivodeshipService(
                this.authenticationService,
                this.httpClient,
                this.objectMapper,
                this.serverAddress,
                this.serverPort
        );
        return this;
    }

    public MainControllerBuilder setVoivodeshipTerytProvider() {
        checkRequiredUnitFields();
        this.voivodeshipTerytProvider = new VoivodeshipService(
                this.authenticationService,
                this.httpClient,
                this.objectMapper,
                this.serverAddress,
                this.serverPort
        );
        return this;
    }

    public MainControllerBuilder setCountyExtendedGetter() {
        checkRequiredUnitFields();
        this.countyExtendedGetter = new CountyService(
                this.authenticationService,
                this.httpClient,
                this.objectMapper,
                this.serverAddress,
                this.serverPort
        );
        return this;
    }

    public MainControllerBuilder setCountyAddressGetter() {
        checkRequiredUnitFields();
        this.countyAddressGetter = new CountyService(
                this.authenticationService,
                this.httpClient,
                this.objectMapper,
                this.serverAddress,
                this.serverPort
        );
        return this;
    }

    public MainControllerBuilder setCountyHistorian() {
        checkRequiredUnitFields();
        this.countyHistorian = new CountyService(
                this.authenticationService,
                this.httpClient,
                this.objectMapper,
                this.serverAddress,
                this.serverPort
        );
        return this;
    }

    public MainControllerBuilder setCountyTerytProvider() {
        checkRequiredUnitFields();
        this.countyTerytProvider = new CountyService(
                this.authenticationService,
                this.httpClient,
                this.objectMapper,
                this.serverAddress,
                this.serverPort
        );
        return this;
    }

    public MainControllerBuilder setCountyByIdGetter() {
        checkRequiredUnitFields();
        this.countyByIdGetter = new CountyService(
                this.authenticationService,
                this.httpClient, this.objectMapper,
                this.serverAddress,
                this.serverPort
        );
        return this;
    }

    public MainControllerBuilder setCommuneGetter() {
        checkRequiredUnitFields();
        this.communeGetter = new CommuneService(
                this.authenticationService,
                this.httpClient,
                this.objectMapper,
                this.serverAddress,
                this.serverPort
        );
        return this;
    }

    public MainControllerBuilder setCommuneAddressGetter() {
        checkRequiredUnitFields();
        this.communeAddressGetter = new CommuneService(
                this.authenticationService,
                this.httpClient,
                this.objectMapper,
                this.serverAddress,
                this.serverPort
        );
        return this;
    }

    public MainControllerBuilder setCommuneHistorian() {
        checkRequiredUnitFields();
        this.communeHistorian = new CommuneService(
                this.authenticationService,
                this.httpClient,
                this.objectMapper,
                this.serverAddress,
                this.serverPort
        );
        return this;
    }

    public MainControllerBuilder setCommuneTerytProvider() {
        checkRequiredUnitFields();
        this.communeTerytProvider = new CommuneService(
                this.authenticationService,
                this.httpClient,
                this.objectMapper,
                this.serverAddress,
                this.serverPort
        );
        return this;
    }

    public MainControllerBuilder setCommuneByIdGetter() {
        checkRequiredUnitFields();
        this.communeByIdGetter = new CommuneService(
                this.authenticationService,
                this.httpClient,
                this.objectMapper,
                this.serverAddress,
                this.serverPort
        );
        return this;
    }

    public MainControllerBuilder setCommuneByGPGetter() {
        checkRequiredUnitFields();
        this.communeByGPGetter = new CommuneService(
                this.authenticationService,
                this.httpClient,
                this.objectMapper,
                this.serverAddress,
                this.serverPort
        );
        return this;
    }

    public MainControllerBuilder setReportGetter() {
        checkRequiredUnitFields();
        this.reportGetter = new ReportService(
                this.httpClient,
                this.objectMapper,
                this.serverAddress,
                this.serverPort
        );
        return this;
    }

    public MainControllerBuilder setAddressGetter() {
        checkRequiredUnitFields();
        this.addressGetter = new AddressService(
                this.authenticationService,
                this.httpClient,
                this.objectMapper,
                this.serverAddress,
                this.serverPort
        );
        return this;
    }

    public MainControllerBuilder setVoivodeshipCreator() {
        checkRequiredFieldsWithoutAuthentication();
        this.voivodeshipCreator = new VoivodeshipService(
                this.authenticationService,
                this.httpClient,
                this.objectMapper,
                this.serverAddress,
                this.serverPort
        );
        return this;
    }

    public MainControllerBuilder setCountyCreator() {
        checkRequiredUnitFields();
        this.countyCreator = new CountyService(
                this.authenticationService,
                this.httpClient,
                this.objectMapper,
                this.serverAddress,
                this.serverPort
        );
        return this;
    }

    public MainControllerBuilder setCommuneCreator() {
        checkRequiredUnitFields();
        this.communeCreator = new CommuneService(
                this.authenticationService,
                this.httpClient,
                this.objectMapper,
                this.serverAddress,
                this.serverPort
        );
        return this;
    }

    public MainControllerBuilder setReportCreator() {
        checkRequiredFieldsWithoutAuthentication();
        this.reportCreator = new ReportService(
                this.httpClient,
                this.objectMapper,
                this.serverAddress,
                this.serverPort
        );
        return this;
    }

    public MainControllerBuilder setAddressCreator() {
        checkRequiredUnitFields();
        this.addressCreator = new AddressService(
                this.authenticationService,
                this.httpClient,
                this.objectMapper,
                this.serverAddress,
                this.serverPort
        );
        return this;
    }

    public MainControllerBuilder setVoivodeshipEditor() {
        checkRequiredFieldsWithoutAuthentication();
        this.voivodeshipEditor = new VoivodeshipService(
                this.authenticationService,
                this.httpClient,
                this.objectMapper,
                this.serverAddress,
                this.serverPort
        );
        return this;
    }

    public MainControllerBuilder setCountyEditor() {
        checkRequiredUnitFields();
        this.countyEditor = new CountyService(
                this.authenticationService,
                this.httpClient,
                this.objectMapper,
                this.serverAddress,
                this.serverPort
        );
        return this;
    }

    public MainControllerBuilder setCommuneEditor() {
        checkRequiredUnitFields();
        this.communeEditor = new CommuneService(
                this.authenticationService,
                this.httpClient,
                this.objectMapper,
                this.serverAddress,
                this.serverPort
        );
        return this;
    }

    public MainControllerBuilder setVoivodeshipDeleter() {
        checkRequiredUnitFields();
        this.voivodeshipDeleter = new VoivodeshipService(
                this.authenticationService,
                this.httpClient,
                this.objectMapper,
                this.serverAddress,
                this.serverPort
        );
        return this;
    }

    public MainControllerBuilder setCountyDeleter() {
        checkRequiredUnitFields();
        this.countyDeleter = new CountyService(
                this.authenticationService,
                this.httpClient,
                this.objectMapper,
                this.serverAddress,
                this.serverPort
        );
        return this;
    }

    public MainControllerBuilder setCommuneDeleter() {
        checkRequiredUnitFields();
        this.communeDeleter = new CommuneService(
                this.authenticationService,
                this.httpClient,
                this.objectMapper,
                this.serverAddress,
                this.serverPort
        );
        return this;
    }

    public MainControllerBuilder setDataSender() {
        if(
            this.voivodeshipCreator == null ||
            this.countyCreator == null ||
            this.communeCreator == null ||
            this.reportCreator == null ||
            this.addressCreator == null ||
            this.voivodeshipEditor == null ||
            this.countyEditor == null ||
            this.communeEditor == null ||
            this.voivodeshipDeleter == null ||
            this.countyDeleter == null ||
            this.communeDeleter == null
        )
            throw new IllegalStateException("Not all required fields are set");
        this.dataSender = new DataSender(
                this.voivodeshipCreator,
                this.countyCreator,
                this.communeCreator,
                this.reportCreator,
                this.addressCreator,
                this.voivodeshipEditor,
                this.countyEditor,
                this.communeEditor,
                this.voivodeshipDeleter,
                this.countyDeleter,
                this.communeDeleter
        );
        return this;
    }

    public MainControllerBuilder setResultFetcher() {
        if(
            this.voivodeshipGetter == null ||
            this.voivodeshipExtendedGetter == null ||
            this.voivodeshipAddressGetter == null ||
            this.voivodeshipHistorian == null ||
            this.voivodeshipTerytProvider == null ||
            this.countyExtendedGetter == null ||
            this.countyAddressGetter == null ||
            this.countyHistorian == null ||
            this.countyTerytProvider == null ||
            this.countyByIdGetter == null ||
            this.communeGetter == null ||
            this.communeAddressGetter == null ||
            this.communeHistorian == null ||
            this.communeTerytProvider == null ||
            this.communeByIdGetter == null ||
            this.communeByGPGetter == null ||
            this.reportGetter == null ||
            this.addressGetter == null
        )
            throw new IllegalStateException("Not all required fields are set");
        this.resultFetcher = new ResultFetcher(
                this.voivodeshipGetter,
                this.voivodeshipExtendedGetter,
                this.voivodeshipAddressGetter,
                this.voivodeshipHistorian,
                this.voivodeshipTerytProvider,
                this.countyExtendedGetter,
                this.countyAddressGetter,
                this.countyHistorian,
                this.countyTerytProvider,
                this.countyByIdGetter,
                this.communeGetter,
                this.communeAddressGetter,
                this.communeHistorian,
                this.communeTerytProvider,
                this.communeByIdGetter,
                this.communeByGPGetter,
                this.reportGetter,
                this.addressGetter
        );
        return this;
    }


    public MainController build() {
        return new MainController(
                authenticationService, httpClient, objectMapper, serverAddress, serverPort,
                voivodeshipGetter, voivodeshipExtendedGetter, voivodeshipAddressGetter, voivodeshipHistorian,
                voivodeshipTerytProvider, countyExtendedGetter, countyAddressGetter, countyHistorian,
                countyTerytProvider, countyByIdGetter, communeGetter, communeAddressGetter, communeHistorian,
                communeTerytProvider, communeByIdGetter, communeByGPGetter, reportGetter, addressGetter,
                voivodeshipCreator, countyCreator, communeCreator, reportCreator, addressCreator, voivodeshipEditor,
                countyEditor, communeEditor, voivodeshipDeleter, countyDeleter, communeDeleter, dataSender,
                resultFetcher
        );
    }
}

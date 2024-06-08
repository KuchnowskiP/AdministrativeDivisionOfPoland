package pl.edu.pwr.database.administrativedivisionofpoland.builders.controllers;

import pl.edu.pwr.database.administrativedivisionofpoland.authentication.IAuthenticationService;
import pl.edu.pwr.database.administrativedivisionofpoland.controllers.MainController;
import pl.edu.pwr.database.administrativedivisionofpoland.data.IDataSender;
import pl.edu.pwr.database.administrativedivisionofpoland.data.IResultReceiver;

public class MainControllerBuilder {

    private IAuthenticationService authenticationService;
    private IDataSender dataSender;
    private IResultReceiver resultReceiver;

    public void setAuthenticationService(IAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public void setDataSender(IDataSender dataSender) {
        this.dataSender = dataSender;
    }

    public void setResultReceiver(IResultReceiver resultReceiver) {
        this.resultReceiver = resultReceiver;
    }

    public MainController getResult() {
        return new MainController(authenticationService, dataSender, resultReceiver);
    }
}

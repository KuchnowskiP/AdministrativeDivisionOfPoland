package pl.edu.pwr.database.administrativedivisionofpoland.builders.data.services;

import pl.edu.pwr.database.administrativedivisionofpoland.data.services.CountyService;

public class CountyServiceBuilder extends AuthenticatableServiceBuilder {
    public CountyService getResult() {
        return new CountyService(authenticationService, httpClient, objectMapper, serverAddress, serverPort);
    }
}

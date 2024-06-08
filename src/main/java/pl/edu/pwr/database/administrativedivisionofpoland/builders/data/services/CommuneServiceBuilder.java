package pl.edu.pwr.database.administrativedivisionofpoland.builders.data.services;

import pl.edu.pwr.database.administrativedivisionofpoland.data.services.CommuneService;

public class CommuneServiceBuilder extends AuthenticatableServiceBuilder {
    public CommuneService getResult() {
        return new CommuneService(authenticationService, httpClient, objectMapper, serverAddress, serverPort);
    }
}

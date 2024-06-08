package pl.edu.pwr.database.administrativedivisionofpoland.builders.data.services;

import pl.edu.pwr.database.administrativedivisionofpoland.data.services.VoivodeshipService;

public class VoivodeshipServiceBuilder extends AuthenticatableServiceBuilder {
    public VoivodeshipService getResult() {
        return new VoivodeshipService(authenticationService, httpClient, objectMapper, serverAddress, serverPort);
    }
}

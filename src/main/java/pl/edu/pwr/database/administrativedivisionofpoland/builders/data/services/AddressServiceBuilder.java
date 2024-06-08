package pl.edu.pwr.database.administrativedivisionofpoland.builders.data.services;

import pl.edu.pwr.database.administrativedivisionofpoland.data.services.AddressService;

public class AddressServiceBuilder extends AuthenticatableServiceBuilder {
    public AddressService getResult() {
        return new AddressService(authenticationService, httpClient, objectMapper, serverAddress, serverPort);
    }
}

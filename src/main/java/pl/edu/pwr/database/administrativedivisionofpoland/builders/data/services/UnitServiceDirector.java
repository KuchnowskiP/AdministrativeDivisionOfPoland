package pl.edu.pwr.database.administrativedivisionofpoland.builders.data.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.edu.pwr.database.administrativedivisionofpoland.Config;
import pl.edu.pwr.database.administrativedivisionofpoland.authentication.AuthenticationService;
import pl.edu.pwr.database.administrativedivisionofpoland.authentication.IAuthenticationService;

import java.net.http.HttpClient;

public class UnitServiceDirector {
    private final IAuthenticationService authenticationService;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String serverAddress;
    private final String serverPort;

    public UnitServiceDirector(
            IAuthenticationService authenticationService,
            HttpClient httpClient,
            ObjectMapper objectMapper,
            String serverAddress,
            String serverPort
    ) {
        this.authenticationService = authenticationService;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void constructUnitService(ServiceBuilder builder) {
        builder.reset();
        builder.setHttpClient(this.httpClient);
        builder.setObjectMapper(this.objectMapper);
        builder.setServerAddress(this.serverAddress);
        builder.setServerPort(this.serverPort);
    }

    public void constructAuthenticatableUnitService(AuthenticatableServiceBuilder builder) {
        builder.reset();
        builder.setAuthenticationService(this.authenticationService);
        builder.setHttpClient(this.httpClient);
        builder.setObjectMapper(this.objectMapper);
        builder.setServerAddress(this.serverAddress);
        builder.setServerPort(this.serverPort);
    }
}

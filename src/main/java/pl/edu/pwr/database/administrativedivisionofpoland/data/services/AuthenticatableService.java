package pl.edu.pwr.database.administrativedivisionofpoland.data.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.edu.pwr.database.administrativedivisionofpoland.authentication.IAuthenticationService;

import java.net.http.HttpClient;

public abstract class AuthenticatableService extends Service{
    protected IAuthenticationService authenticationService;


    public AuthenticatableService(IAuthenticationService authenticationService,
                                  HttpClient httpClient,
                                  ObjectMapper objectMapper,
                                  String serverAddress,
                                  String serverPort) {
        super(httpClient, objectMapper, serverAddress, serverPort);
        this.authenticationService = authenticationService;
    }
}

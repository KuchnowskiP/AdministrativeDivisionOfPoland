package pl.edu.pwr.database.administrativedivisionofpoland.data.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.edu.pwr.database.administrativedivisionofpoland.authentication.IAuthenticationService;

import java.net.http.HttpClient;

public abstract class Service {
    protected HttpClient httpClient;
    protected IAuthenticationService authenticationService;
    protected String serverAddress;
    protected String serverPort;
    protected ObjectMapper objectMapper;

    public Service(IAuthenticationService authenticationService, HttpClient httpClient, ObjectMapper objectMapper
            , String serverAddress, String serverPort) {
        this.httpClient = httpClient;
        this.authenticationService = authenticationService;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.objectMapper = objectMapper;
    }
}

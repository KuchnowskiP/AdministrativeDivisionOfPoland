package pl.edu.pwr.database.administrativedivisionofpoland.data.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.edu.pwr.database.administrativedivisionofpoland.authentication.IAuthenticationService;

import java.net.http.HttpClient;

public abstract class Service {
    protected HttpClient httpClient;
    protected ObjectMapper objectMapper;
    protected String serverAddress;
    protected String serverPort;


    public Service(HttpClient httpClient, ObjectMapper objectMapper
            , String serverAddress, String serverPort) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;

    }
}

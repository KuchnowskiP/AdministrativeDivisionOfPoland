package pl.edu.pwr.database.administrativedivisionofpoland.builders.data.services;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpClient;

public abstract class ServiceBuilder {
    protected HttpClient httpClient;
    protected ObjectMapper objectMapper;
    protected String serverAddress;
    protected String serverPort;

    public void reset() {
        this.httpClient = null;
        this.objectMapper = null;
        this.serverAddress = null;
        this.serverPort = null;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void setObjectMapper(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    public void setServerAddress(String serverAddress){
        this.serverAddress = serverAddress;
    }

    public void setServerPort(String serverPort){
        this.serverPort = serverPort;
    }
}

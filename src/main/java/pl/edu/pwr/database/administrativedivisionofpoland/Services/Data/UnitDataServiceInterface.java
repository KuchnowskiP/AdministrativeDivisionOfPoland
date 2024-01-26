package pl.edu.pwr.database.administrativedivisionofpoland.Services.Data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.net.http.HttpClient;

public interface UnitDataServiceInterface<T> {
    HttpClient httpClient = HttpClient.newHttpClient();
    ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().build();

    boolean create(T unitRequest) throws IllegalAccessException, IOException, InterruptedException;

    boolean edit(int ID, T unitRequest) throws IllegalAccessException, IOException, InterruptedException;
    boolean delete(int ID) throws IOException, InterruptedException;
}

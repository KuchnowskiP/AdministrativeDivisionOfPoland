package pl.edu.pwr.database.administrativedivisionofpoland.Data.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import pl.edu.pwr.contract.Common.PageResult;

import java.io.IOException;
import java.net.http.HttpClient;

public interface UnitDataServiceInterface<T1,T2> {
    HttpClient httpClient = HttpClient.newHttpClient();
    ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().build();
    boolean create(T1 unitRequest) throws IllegalAccessException, IOException, InterruptedException;
    boolean edit(int ID, T1 unitRequest) throws IllegalAccessException, IOException, InterruptedException;
    boolean delete(int ID) throws IOException, InterruptedException;
    PageResult<T2> get(Object ID, int page, int size) throws IOException, InterruptedException;
}

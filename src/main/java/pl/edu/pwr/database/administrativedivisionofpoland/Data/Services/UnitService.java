package pl.edu.pwr.database.administrativedivisionofpoland.Data.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.database.administrativedivisionofpoland.Authentication.AuthenticationService;

import java.io.IOException;
import java.net.http.HttpClient;

public interface UnitService<T1,T2> {
    AuthenticationService authenticationService = AuthenticationService.getInstance();
    HttpClient httpClient = HttpClient.newHttpClient();
    ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().build();
    boolean create(T1 unitRequest) throws Exception;
    boolean edit(int ID, T1 unitRequest) throws Exception;
    boolean delete(int ID) throws Exception;
    PageResult<T2> get(Object ID, int page, int size) throws IOException, InterruptedException;
}

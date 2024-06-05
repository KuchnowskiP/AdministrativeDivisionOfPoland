package pl.edu.pwr.database.administrativedivisionofpoland.Data.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.OfficeAddressDto;
import pl.edu.pwr.contract.OfficeAdres.OfficeAddressRequest;
import pl.edu.pwr.contract.Reports.AddReportRequest;
import pl.edu.pwr.database.administrativedivisionofpoland.Authentication.AuthenticationService;
import pl.edu.pwr.database.administrativedivisionofpoland.Authentication.IAuthenticationService;
import pl.edu.pwr.database.administrativedivisionofpoland.Config;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class AddressService {
    HttpClient httpClient = HttpClient.newHttpClient();
    ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().build();

    IAuthenticationService authenticationService = AuthenticationService.getInstance();

    String serverAddress = Config.getProperty("server.address");
    String serverPort = Config.getProperty("server.port");


    public PageResult<OfficeAddressDto> getAllAddresses(int page, int size) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/address/all?page=" + page + "&size=" + size))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        PageResult<OfficeAddressDto> result = objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
        return result;
    }

    public HttpResponse<String> addOfficeAddress(OfficeAddressRequest addOfficeAddressRequest)
            throws Exception {
        Map.Entry<String, String> bearerToken = authenticationService.getCredentials();
        HashMap<String, Object> values = new HashMap<String, Object>();
        for(Field field : addOfficeAddressRequest.getClass().getFields()){
            Object o = new AddReportRequest();
            if(field.get(addOfficeAddressRequest) != null) {
                values.put(field.getName(), field.get(addOfficeAddressRequest).toString());
            }else{
                values.put(field.getName(), " ");
            }
        }

        String requestBody = objectMapper.writeValueAsString(values);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/address/add"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .header(bearerToken.getKey(), bearerToken.getValue())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }
}

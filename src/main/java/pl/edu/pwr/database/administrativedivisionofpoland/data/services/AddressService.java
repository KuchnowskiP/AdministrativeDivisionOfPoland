package pl.edu.pwr.database.administrativedivisionofpoland.data.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.OfficeAddressDto;
import pl.edu.pwr.contract.OfficeAdres.OfficeAddressRequest;
import pl.edu.pwr.database.administrativedivisionofpoland.authentication.IAuthenticationService;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.api.Creatable;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.api.Gettable;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class AddressService extends AuthenticatableService implements Creatable<OfficeAddressRequest, HttpResponse<String>>, Gettable<OfficeAddressDto> {

    public AddressService(
            IAuthenticationService authenticationService,
            HttpClient httpClient, ObjectMapper objectMapper,
            String serverAddress,
            String serverPort) {
        super(authenticationService, httpClient, objectMapper, serverAddress, serverPort);
    }

    @Override
    public PageResult<OfficeAddressDto> get(Object Id, int page, int size) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/address/all?page=" + page + "&size=" + size))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
    }

    @Override
    public HttpResponse<String> create(OfficeAddressRequest addOfficeAddressRequest)
            throws Exception {
        Map.Entry<String, String> bearerToken = authenticationService.getCredentials();
        HashMap<String, Object> values = new HashMap<>();
        for(Field field : addOfficeAddressRequest.getClass().getFields()){
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

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}

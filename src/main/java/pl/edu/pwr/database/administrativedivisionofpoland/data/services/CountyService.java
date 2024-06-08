package pl.edu.pwr.database.administrativedivisionofpoland.data.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.County.CountyRequest;
import pl.edu.pwr.contract.Dtos.CountyAddressData;
import pl.edu.pwr.contract.Dtos.CountyDto;
import pl.edu.pwr.contract.Dtos.CountyExtended;
import pl.edu.pwr.contract.History.CountyHistoryDto;
import pl.edu.pwr.database.administrativedivisionofpoland.authentication.IAuthenticationService;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.api.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CountyService extends AuthenticatableService implements Creatable<CountyRequest, Boolean>,
        Editable<CountyRequest>, Deletable, GettableExtended<CountyExtended>, Gettable<CountyDto>, TerytProvider,
        GettableAddress<CountyAddressData>, Trackable<CountyHistoryDto>, GettableById<CountyDto> {

    public CountyService(IAuthenticationService authenticationService, HttpClient httpClient,
                         ObjectMapper objectMapper, String serverAddress, String serverPort) {
        super(authenticationService, httpClient, objectMapper, serverAddress, serverPort);
    }

    @Override
    public Boolean create(CountyRequest countyRequest) throws Exception {
        Map.Entry<String, String> bearerToken = authenticationService.getCredentials();
        HashMap<String, Object> values = new HashMap<>();
        for (Field field : countyRequest.getClass().getFields()) {
            if (field.get(countyRequest) != null) {
                values.put(field.getName(), field.get(countyRequest).toString());
            } else {
                values.put(field.getName(), " ");
            }
        }

        String requestBody = objectMapper.writeValueAsString(values);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/county/add"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .header(bearerToken.getKey(), bearerToken.getValue())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200;
    }

    @Override
    public boolean edit(int ID, CountyRequest countyRequest) throws Exception {
        Map.Entry<String, String> bearerToken = authenticationService.getCredentials();
        HashMap<String, Object> values = new HashMap<>();
        for (Field field : countyRequest.getClass().getFields()) {
            if (field.get(countyRequest) != null) {
                values.put(field.getName(), field.get(countyRequest).toString());
            } else {
                values.put(field.getName(), " ");
            }
        }

        String requestBody = objectMapper.writeValueAsString(values);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/county/update/" + ID))
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .header(bearerToken.getKey(), bearerToken.getValue())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200;
    }

    @Override
    public boolean delete(int ID) throws Exception {
        Map.Entry<String, String> bearerToken = authenticationService.getCredentials();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/county/delete/" + ID))
                .DELETE()
                .header("Content-Type", "application/json")
                .header(bearerToken.getKey(), bearerToken.getValue())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 204;
    }

    @Override
    public PageResult<CountyExtended> getExtended(Object voivodeshipId, int page, int size) throws IOException, InterruptedException {
        HttpRequest request;
        if (voivodeshipId.equals(-1)) {
            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/county/extended/all?page=" + page + "&size=" + size))
                    .header("Content-Type", "application/json")
                    .build();
        } else {
            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/county/extended/byVoivodeship?voivodeshipId=" + voivodeshipId + "&page=" + page + "&size=" + size))
                    .header("Content-Type", "application/json")
                    .build();
        }

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
    }

    @Override
    public PageResult<CountyDto> get(Object voivodeshipId, int page, int size) throws IOException, InterruptedException {
        HttpRequest request;
        if (voivodeshipId.equals(-1)) {
            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/county/all?page=" + page + "&size=" + size))
                    .header("Content-Type", "application/json")
                    .build();
        } else {
            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/county/byVoivodeship?voivodeshipId=" + voivodeshipId + "&page=" + page + "&size=" + size))
                    .header("Content-Type", "application/json")
                    .build();
        }

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
    }

    @Override
    public PageResult<CountyAddressData> getAddresses(Object voivodeshipId, int page, int size) throws IOException, InterruptedException {
        HttpRequest request;
        if (voivodeshipId.equals(-1)) {
            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/county/address/all?page=" + page + "&size=" + size))
                    .header("Content-Type", "application/json")
                    .build();
        } else {
            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/county/address/byVoivodeship?voivodeshipId=" + voivodeshipId + "&page=" + page + "&size=" + size))
                    .header("Content-Type", "application/json")
                    .build();
        }

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
    }

    @Override
    public String getNewTeryt(String[] args) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/county/teryt?voivodeshipId=" + args[0]))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String result = null;
        if (!Objects.equals(response.body(), "")) {
            int newTerytInteger = Integer.parseInt(response.body()) + 1000;
            newTerytInteger /= 10;
            newTerytInteger *= 10;
            newTerytInteger += Integer.parseInt(args[1]);
            result = String.format("%07d", newTerytInteger);
        }
        return result;
    }

    @Override
    public PageResult<CountyHistoryDto> getHistory(int page, int size) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/history/counties?page=" + page + "&size=" + size))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        PageResult<CountyHistoryDto> result = objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
        System.out.println("powiaty: " + response.body());
        return result;
    }

    @Override
    public CountyDto getById(int ID) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/county/" + ID))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        CountyDto result = objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
        System.out.println(result);
        return result;
    }
}

package pl.edu.pwr.database.administrativedivisionofpoland.data.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Commune.CommuneRequest;
import pl.edu.pwr.contract.Dtos.CommuneAddressData;
import pl.edu.pwr.contract.Dtos.CommuneDto;
import pl.edu.pwr.contract.History.CommuneHistoryDto;
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

public class CommuneService extends Service implements Creatable<CommuneRequest, Boolean>,
        Editable<CommuneRequest>, Deletable, Gettable<CommuneDto>, TerytProvider, GettableAddress<CommuneAddressData>,
        Trackable<CommuneHistoryDto>, GettableById<CommuneDto>, GettableByGrandparentUnit<CommuneDto> {

    public CommuneService(IAuthenticationService authenticationService, HttpClient httpClient, ObjectMapper objectMapper,
                          String serverAddress, String serverPort) {
        super(authenticationService, httpClient, objectMapper, serverAddress, serverPort);
    }

    @Override
    public Boolean create(CommuneRequest communeRequest) throws Exception {
        Map.Entry<String, String> bearerToken = authenticationService.getCredentials();
        HashMap<String, Object> values = new HashMap<>();
        for(Field field : communeRequest.getClass().getFields()){
            if(field.get(communeRequest) != null) {
                values.put(field.getName(), field.get(communeRequest).toString());
            }else{
                values.put(field.getName(), " ");
            }
        }

        String requestBody = objectMapper.writeValueAsString(values);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/commune/add"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .header(bearerToken.getKey(), bearerToken.getValue())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200;
    }

    @Override
    public boolean edit(int ID, CommuneRequest communeRequest) throws Exception {
        Map.Entry<String, String> bearerToken = authenticationService.getCredentials();
        HashMap<String, Object> values = new HashMap<>();
        for(Field field : communeRequest.getClass().getFields()){
            if(field.get(communeRequest) != null) {
                values.put(field.getName(), field.get(communeRequest).toString());
            }else{
                values.put(field.getName(), " ");
            }
        }

        String requestBody = objectMapper.writeValueAsString(values);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/commune/update/" + ID))
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .header(bearerToken.getKey(),bearerToken.getValue())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200;
    }

    @Override
    public boolean delete(int ID) throws Exception {
        Map.Entry<String, String> bearerToken = authenticationService.getCredentials();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/commune/delete/" + ID))
                .DELETE()
                .header("Content-Type", "application/json")
                .header(bearerToken.getKey(), bearerToken.getValue())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 204;
    }

    @Override
    public PageResult<CommuneDto> get(Object countyID, int page, int size) throws IOException, InterruptedException {
        HttpRequest request;
        if (countyID.equals(-1)) {
            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/commune/all?page=" + page + "&size=" + size))
                    .header("Content-Type", "application/json")
                    .build();
        }else{
            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/commune/byCounty?countyId=" + countyID + "&page=" + page + "&size=" + size))
                    .header("Content-Type", "application/json")
                    .build();
        }

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
    }

    @Override
    public PageResult<CommuneAddressData> getAddresses(Object countyID, int page, int size) throws IOException, InterruptedException {
        HttpRequest request;
        if (countyID.equals(-1)) {
            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/commune/address/all?page=" + page + "&size=" + size))
                    .header("Content-Type", "application/json")
                    .build();
        }else{
            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/commune/address/byCounty?countyId=" + countyID + "&page=" + page + "&size=" + size))
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
                .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/commune/teryt?countyId=" + args[0]))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String result = null;
        if(!Objects.equals(response.body(), "")) {
            int newTerytInteger = Integer.parseInt(response.body()) + 10;
            newTerytInteger /= 10;
            newTerytInteger *= 10;
            newTerytInteger += Integer.parseInt(args[1]);
            result = String.format("%07d", newTerytInteger);
        }
        return result;
    }

    @Override
    public PageResult<CommuneHistoryDto> getHistory(int page, int size) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/history/communes?page=" + page + "&size=" + size))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        PageResult<CommuneHistoryDto> result = objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
        System.out.println("gminy: " + response.body());
        return result;
    }

    @Override
    public CommuneDto getById(int ID) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/commune/" + ID))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        CommuneDto result =  objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
        System.out.println(result);
        return result;
    }

    @Override
    public PageResult<CommuneDto> getByGrandparentUnitId(Object voivodeshipId, int page, int size) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/commune/byVoivodeship?page=" + page + "&size=" + size + "&voivodeshipId=" + voivodeshipId))
                .header("Content-Type", "application/json")
                .build();


    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(
            response.body(), new TypeReference<>() {
            });

    }
}

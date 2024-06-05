package pl.edu.pwr.database.administrativedivisionofpoland.Data.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.VoivodeshipAddressData;
import pl.edu.pwr.contract.Dtos.VoivodeshipDto;
import pl.edu.pwr.contract.Dtos.VoivodeshipExtended;
import pl.edu.pwr.contract.History.VoivodeshipHistoryDto;
import pl.edu.pwr.contract.Voivodeship.VoivodeshipRequest;
import pl.edu.pwr.database.administrativedivisionofpoland.Config;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class VoivodeshipService implements UnitService<VoivodeshipRequest, VoivodeshipExtended> {

    @Override
    public boolean create(VoivodeshipRequest voivodeshipRequest) throws Exception {
        HashMap<String, Object> values = new HashMap<String, Object>();
        for (Field field : voivodeshipRequest.getClass().getFields()) {
            if (field.get(voivodeshipRequest) != null) {
                values.put(field.getName(), field.get(voivodeshipRequest).toString());
            } else {
                values.put(field.getName(), " ");
            }
        }

        String requestBody = objectMapper.writeValueAsString(values);
        System.out.println(requestBody);
        Map.Entry<String,String> bearerToken = authenticationService.getCredentials();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/voivodeship/add"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .header(bearerToken.getKey(), bearerToken.getValue())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return response.statusCode() == 200;
    }

    @Override
    public boolean edit(int ID, VoivodeshipRequest voivodeshipRequest) throws Exception {
        HashMap<String, Object> values = new HashMap<String, Object>();
        for (Field field : voivodeshipRequest.getClass().getFields()) {
            if (field.get(voivodeshipRequest) != null) {
                values.put(field.getName(), field.get(voivodeshipRequest).toString());
            } else {
                values.put(field.getName(), " ");
            }
        }

        String requestBody = objectMapper.writeValueAsString(values);
        Map.Entry<String, String> bearerToken = authenticationService.getCredentials();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/voivodeship/update/" + ID))
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .header(bearerToken.getKey(), bearerToken.getValue())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200;
    }

    @Override
    public boolean delete(int ID) throws Exception {
        Map.Entry<String,String> bearerToken = authenticationService.getCredentials();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/voivodeship/delete/" + ID))
                .DELETE()
                .header("Content-Type", "application/json")
                .header(bearerToken.getKey(),bearerToken.getValue())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 204;
    }

    @Override
    public PageResult<VoivodeshipExtended> get(Object ID, int page, int size) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/voivodeship/extended/all?page=" + page + "&size=" + size))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        PageResult<VoivodeshipExtended> result = objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
        return result;
    }

    public PageResult<VoivodeshipAddressData> getVoivodeshipsWithAddresses(int page, int size) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/voivodeship/address/all?page=" + page + "&size=" + size))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        PageResult<VoivodeshipAddressData> result = objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
        return result;
    }
    @Override
    public String getNewTeryt(String[] args) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/voivodeship/teryt"))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        int newTerytInteger = Integer.parseInt(response.body()) + 200000;
        String result = String.format("%07d", newTerytInteger);
        return result;
    }

    public PageResult<VoivodeshipHistoryDto> getVoivodeshipsHistory(int page, int size) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/history/voivodeships?page=" + page + "&size=" + size))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        PageResult<VoivodeshipHistoryDto> result = objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
        System.out.println("wojewodztwa: " + response.body());
        return result;
    }

    public PageResult<VoivodeshipDto> getDto(Object id, int page, int size) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/voivodeship/all?page=" + page + "&size=" + size))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        PageResult<VoivodeshipDto> result = objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
        return result;
    }
}

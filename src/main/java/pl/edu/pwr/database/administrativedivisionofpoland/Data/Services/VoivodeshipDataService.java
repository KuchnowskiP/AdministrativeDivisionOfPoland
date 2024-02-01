package pl.edu.pwr.database.administrativedivisionofpoland.Data.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.CountyDto;
import pl.edu.pwr.contract.Dtos.VoivodeshipAddressData;
import pl.edu.pwr.contract.Dtos.VoivodeshipDto;
import pl.edu.pwr.contract.Dtos.VoivodeshipExtended;
import pl.edu.pwr.contract.History.VoivodeshipHistoryDto;
import pl.edu.pwr.contract.Voivodeship.VoivodeshipRequest;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class VoivodeshipDataService implements UnitDataServiceInterface<VoivodeshipRequest, VoivodeshipExtended> {
    @Override
    public boolean create(VoivodeshipRequest voivodeshipRequest) throws IllegalAccessException, IOException, InterruptedException {
        HashMap<String, Object> values = new HashMap<String, Object>();
        for (Field field : voivodeshipRequest.getClass().getFields()) {
            if (field.get(voivodeshipRequest) != null) {
                values.put(field.getName(), field.get(voivodeshipRequest).toString());
            } else {
                values.put(field.getName(), " ");
            }
        }

        String requestBody = objectMapper.writeValueAsString(values);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.196.2:8085/api/voivodeship/add"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200;
    }

    @Override
    public boolean edit(int ID, VoivodeshipRequest voivodeshipRequest) throws IllegalAccessException, IOException, InterruptedException {
        HashMap<String, Object> values = new HashMap<String, Object>();
        for (Field field : voivodeshipRequest.getClass().getFields()) {
            if (field.get(voivodeshipRequest) != null) {
                values.put(field.getName(), field.get(voivodeshipRequest).toString());
            } else {
                values.put(field.getName(), " ");
            }
        }

        String requestBody = objectMapper.writeValueAsString(values);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.196.2:8085/api/voivodeship/update/" + ID))
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200;
    }

    @Override
    public boolean delete(int ID) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.196.2:8085/api/voivodeship/delete/" + ID))
                .DELETE()
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 204;
    }

    @Override
    public PageResult<VoivodeshipExtended> get(Object ID, int page, int size) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.196.2:8085/api/voivodeship/extended/all?page=" + page + "&size=" + size))
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
                .uri(URI.create("http://192.168.196.2:8085/api/voivodeship/address/all?page=" + page + "&size=" + size))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        PageResult<VoivodeshipAddressData> result = objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
        return result;
    }

    public String getNewVoivodeshipTeryt() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.196.2:8085/api/voivodeship/teryt"))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        int newTerytInteger = Integer.parseInt(response.body()) + 200000;
        String result = String.format("%07d", newTerytInteger);
        return result;
    }

    public PageResult<VoivodeshipHistoryDto> getVoivodeshipsHistory(int page, int size) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.196.2:8085/api/history/voivodeships?page=" + page + "&size=" + size))
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
                .uri(URI.create("http://192.168.196.2:8085/api/voivodeship/all?page=" + page + "&size=" + size))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        PageResult<VoivodeshipDto> result = objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
        return result;
    }
}

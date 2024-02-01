package pl.edu.pwr.database.administrativedivisionofpoland.Data.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.County.CountyRequest;
import pl.edu.pwr.contract.Dtos.CountyAddressData;
import pl.edu.pwr.contract.Dtos.CountyDto;
import pl.edu.pwr.contract.Dtos.CountyExtended;
import pl.edu.pwr.contract.History.CountyHistoryDto;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Objects;

public class CountyDataService implements UnitDataServiceInterface<CountyRequest, CountyDto> {
    @Override
    public boolean create(CountyRequest countyRequest) throws IllegalAccessException, IOException, InterruptedException {
        HashMap<String, Object> values = new HashMap<String, Object>();
        for (Field field : countyRequest.getClass().getFields()) {
            if (field.get(countyRequest) != null) {
                values.put(field.getName(), field.get(countyRequest).toString());
            } else {
                values.put(field.getName(), " ");
            }
        }

        String requestBody = objectMapper.writeValueAsString(values);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.196.2:8085/api/county/add"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200;
    }

    @Override
    public boolean edit(int ID, CountyRequest countyRequest) throws IllegalAccessException, IOException, InterruptedException {
        HashMap<String, Object> values = new HashMap<String, Object>();
        for (Field field : countyRequest.getClass().getFields()) {
            if (field.get(countyRequest) != null) {
                values.put(field.getName(), field.get(countyRequest).toString());
            } else {
                values.put(field.getName(), " ");
            }
        }

        String requestBody = objectMapper.writeValueAsString(values);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.196.2:8085/api/county/update/" + ID))
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200;
    }

    @Override
    public boolean delete(int ID) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.196.2:8085/api/county/delete/" + ID))
                .DELETE()
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 204;
    }

    @Override
    public PageResult<CountyDto> get(Object voivodeshipId, int page, int size) throws IOException, InterruptedException {
        HttpRequest request;
        if (voivodeshipId.equals(-1)) {
            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://192.168.196.2:8085/api/county/all?page=" + page + "&size=" + size))
                    .header("Content-Type", "application/json")
                    .build();
        } else {
            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://192.168.196.2:8085/api/county/byVoivodeship?voivodeshipId=" + voivodeshipId + "&page=" + page + "&size=" + size))
                    .header("Content-Type", "application/json")
                    .build();
        }

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        PageResult<CountyDto> result = objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
        return result;
    }

    public PageResult<CountyAddressData> getCountiesWithAddresses(Object voivodeshipId, int page, int size) throws IOException, InterruptedException {
        HttpRequest request;
        if (voivodeshipId.equals(-1)) {
            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://192.168.196.2:8085/api/county/address/all?page=" + page + "&size=" + size))
                    .header("Content-Type", "application/json")
                    .build();
        } else {
            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://192.168.196.2:8085/api/county/address/byVoivodeship?voivodeshipId=" + voivodeshipId + "&page=" + page + "&size=" + size))
                    .header("Content-Type", "application/json")
                    .build();
        }

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        PageResult<CountyAddressData> result = objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
        return result;
    }

    public String getNewCountyTeryt(Integer id, int city) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.196.2:8085/api/county/teryt?voivodeshipId=" + id))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String result = null;
        if (!Objects.equals(response.body(), "")) {
            int newTerytInteger = Integer.parseInt(response.body()) + 1000;
            newTerytInteger /= 10;
            newTerytInteger *= 10;
            newTerytInteger += city;
            result = String.format("%07d", newTerytInteger);
        }
        return result;
    }

    public PageResult<CountyHistoryDto> getCountiesHistory(int page, int size) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.196.2:8085/api/history/counties?page=" + page + "&size=" + size))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        PageResult<CountyHistoryDto> result = objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
        System.out.println("powiaty: " + response.body());
        return result;
    }

    public CountyDto countyById(int ID) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.196.2:8085/api/county/" + ID))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        CountyDto result = objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
        System.out.println(result);
        return result;
    }

    public PageResult<CountyExtended> getCountiesExtended(int page, int size) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.196.2:8085/api/county/extended/all?page=" + page + "&size=" + size))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        PageResult<CountyExtended> result = objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
        return result;
    }

}

package pl.edu.pwr.database.administrativedivisionofpoland.Services.Data;

import com.fasterxml.jackson.core.type.TypeReference;
import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Commune.CommuneRequest;
import pl.edu.pwr.contract.Dtos.CommuneDto;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class CommuneDataService implements UnitDataServiceInterface<CommuneRequest, CommuneDto> {
    @Override
    public boolean create(CommuneRequest communeRequest) throws IllegalAccessException, IOException, InterruptedException {
        HashMap<String, Object> values = new HashMap<String, Object>();
        for(Field field : communeRequest.getClass().getFields()){
            if(field.get(communeRequest) != null) {
                values.put(field.getName(), field.get(communeRequest).toString());
            }else{
                values.put(field.getName(), " ");
            }
        }

        String requestBody = objectMapper.writeValueAsString(values);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.196.2:8085/api/commune/add"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200;
    }

    @Override
    public boolean edit(int ID, CommuneRequest communeRequest) throws IllegalAccessException, IOException, InterruptedException {
        HashMap<String, Object> values = new HashMap<String, Object>();
        for(Field field : communeRequest.getClass().getFields()){
            if(field.get(communeRequest) != null) {
                values.put(field.getName(), field.get(communeRequest).toString());
            }else{
                values.put(field.getName(), " ");
            }
        }

        String requestBody = objectMapper.writeValueAsString(values);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.196.2:8085/api/commune/update/" + ID))
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200;
    }

    @Override
    public boolean delete(int ID) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.196.2:8085/api/commune/delete/" + ID))
                .DELETE()
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 204;
    }

    @Override
    public PageResult<CommuneDto> get(Object countyID, int page, int size) throws IOException, InterruptedException {
        HttpRequest request;
        if (countyID.equals(-1)) {
            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://192.168.196.2:8085/api/commune/all?page=" + page + "&size=" + size))
                    .header("Content-Type", "application/json")
                    .build();
        }else{
            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://192.168.196.2:8085/api/commune/byCounty?countyId=" + countyID + "&page=" + page + "&size=" + size))
                    .header("Content-Type", "application/json")
                    .build();
        }

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        PageResult<CommuneDto> result = objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
        return result;
    }
}

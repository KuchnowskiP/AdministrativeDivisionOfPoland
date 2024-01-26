package pl.edu.pwr.database.administrativedivisionofpoland.Services.Data;

import com.fasterxml.jackson.core.JsonProcessingException;
import pl.edu.pwr.contract.Voivodeship.VoivodeshipRequest;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class VoivodeshipDataService implements UnitDataServiceInterface<VoivodeshipRequest> {
    @Override
    public boolean create(VoivodeshipRequest voivodeshipRequest) throws IllegalAccessException, IOException, InterruptedException {
        HashMap<String, Object> values = new HashMap<String, Object>();
        for(Field field : voivodeshipRequest.getClass().getFields()){
            if(field.get(voivodeshipRequest) != null) {
                values.put(field.getName(), field.get(voivodeshipRequest).toString());
            }else{
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
        for(Field field : voivodeshipRequest.getClass().getFields()){
            if(field.get(voivodeshipRequest) != null) {
                values.put(field.getName(), field.get(voivodeshipRequest).toString());
            }else{
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
        return response.statusCode() == 200;
    }
}

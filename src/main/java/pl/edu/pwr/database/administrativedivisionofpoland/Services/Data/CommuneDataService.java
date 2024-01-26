package pl.edu.pwr.database.administrativedivisionofpoland.Services.Data;

import pl.edu.pwr.contract.Commune.CommuneRequest;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class CommuneDataService implements UnitDataServiceInterface<CommuneRequest> {
    @Override
    public boolean create(CommuneRequest unitRequest) throws IllegalAccessException, IOException, InterruptedException {
        return false;
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
        return false;
    }
}

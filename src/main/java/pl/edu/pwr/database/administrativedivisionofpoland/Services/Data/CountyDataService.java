package pl.edu.pwr.database.administrativedivisionofpoland.Services.Data;

import pl.edu.pwr.contract.County.CountyRequest;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class CountyDataService implements UnitDataServiceInterface<CountyRequest> {
    @Override
    public boolean create(CountyRequest unitRequest) throws IllegalAccessException, IOException, InterruptedException {
        return false;
    }

    @Override
    public boolean edit(int ID, CountyRequest countyRequest) throws IllegalAccessException, IOException, InterruptedException {
        HashMap<String, Object> values = new HashMap<String, Object>();
        for(Field field : countyRequest.getClass().getFields()){
            if(field.get(countyRequest) != null) {
                values.put(field.getName(), field.get(countyRequest).toString());
            }else{
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
        return false;
    }
}

package pl.edu.pwr.database.administrativedivisionofpoland;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.VoivodeshipDto;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Request {
    private  final HttpClient httpClient = HttpClient.newHttpClient();
    private  final ObjectMapper objectMapper = new ObjectMapper();
    public  PageResult<VoivodeshipDto> getVoivodeships(int page, int size) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8085/api/voivodeship/all?page=" + page + "&size=" + size))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        PageResult<VoivodeshipDto> result = objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
        return result;
    }
}

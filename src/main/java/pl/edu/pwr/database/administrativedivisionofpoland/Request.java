package pl.edu.pwr.database.administrativedivisionofpoland;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.CommuneDto;
import pl.edu.pwr.contract.Dtos.CountyDto;
import pl.edu.pwr.contract.Dtos.VoivodeshipDto;

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
    public PageResult<CountyDto> getCounties(int voivodeshipId, int page, int size) throws Exception {
        HttpRequest request;
        if (voivodeshipId == -1) {
            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8085/api/county/all?page=" + page + "&size=" + size))
                    .header("Content-Type", "application/json")
                    .build();
        }else{
            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8085/api/county/byVoivodeship?voivodeshipId=" + voivodeshipId + "&page=" + page + "&size=" + size))
                    .header("Content-Type", "application/json")
                    .build();
        }

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        PageResult<CountyDto> result = objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
        return result;
    }

    public PageResult<CommuneDto> getCommunes(int countyID, int page, int size) throws Exception {
        HttpRequest request;
        if (countyID == -1) {
            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8085/api/commune/all?page=" + page + "&size=" + size))
                    .header("Content-Type", "application/json")
                    .build();
        }else{
            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8085/api/commune/byCounty?countyId=" + countyID + "&page=" + page + "&size=" + size))
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

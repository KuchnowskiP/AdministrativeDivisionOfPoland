package pl.edu.pwr.database.administrativedivisionofpoland;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.*;
import pl.edu.pwr.contract.Reports.AddReportRequest;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class Request {
    private  final HttpClient httpClient = HttpClient.newHttpClient();
    private  final ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().build();

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
    public PageResult<CountyDto> getCounties(Object voivodeshipId, int page, int size) throws Exception {
        HttpRequest request;
        if (voivodeshipId.equals(-1)) {
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
    public PageResult<CountyAddressData> getCountiesWithAddresses(Object voivodeshipId, int page, int size) throws IOException, InterruptedException {
        HttpRequest request;
        if (voivodeshipId.equals(-1)) {
            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8085/api/county/address/all?page=" + page + "&size=" + size))
                    .header("Content-Type", "application/json")
                    .build();
        }else{
            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8085/api/county/address/byVoivodeship?voivodeshipId=" + voivodeshipId + "&page=" + page + "&size=" + size))
                    .header("Content-Type", "application/json")
                    .build();
        }

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        PageResult<CountyAddressData> result = objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
        return result;
    }

    public PageResult<CommuneDto> getCommunes(Object countyID, int page, int size) throws Exception {
        HttpRequest request;
        if (countyID.equals(-1)) {
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

    public PageResult<CommuneAddressData> getCommunesWithAddresses(Object countyID, int page, int size) throws Exception {
        HttpRequest request;
        if (countyID.equals(-1)) {
            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8085/api/commune/address/all?page=" + page + "&size=" + size))
                    .header("Content-Type", "application/json")
                    .build();
        }else{
            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8085/api/commune/address/byCounty?countyId=" + countyID + "&page=" + page + "&size=" + size))
                    .header("Content-Type", "application/json")
                    .build();
        }

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        PageResult<CommuneAddressData> result = objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
        return result;
    }

    public void createReport(AddReportRequest addReportRequest) throws Exception {
        HashMap<String, Object> values = new HashMap<String, Object>();
        for(Field field : addReportRequest.getClass().getFields()){
            Object o = new AddReportRequest();
            if(field.get(addReportRequest) != null) {
                values.put(field.getName(), field.get(addReportRequest).toString());
            }else{
                values.put(field.getName(), " ");
            }
        }

        String requestBody = objectMapper.writeValueAsString(values);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8085/api/report/add"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public PageResult<ReportDto> getReports(int page, int size) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8085/api/report/all?page=" + page + "&size=" + size))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        PageResult<ReportDto> result = objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
        return result;
    }

    public PageResult<VoivodeshipAddressData> getVoivodeshipsWithAddresses(int page, int size) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8085/api/voivodeship/address/all?page=" + page + "&size=" + size))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        PageResult<VoivodeshipAddressData> result = objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
        return result;
    }

}

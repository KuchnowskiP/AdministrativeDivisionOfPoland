package pl.edu.pwr.database.administrativedivisionofpoland.Data.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.*;
import pl.edu.pwr.contract.Reports.AddReportRequest;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class ReportService {
    HttpClient httpClient = HttpClient.newHttpClient();
    ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().build();


    public  void createReport(AddReportRequest addReportRequest) throws Exception {
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
                .uri(URI.create("http://192.168.196.2:8085/api/report/add"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public PageResult<ReportDto> getReports(int page, int size) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.196.2:8085/api/report/all?page=" + page + "&size=" + size))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        PageResult<ReportDto> result = objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
        return result;
    }

}

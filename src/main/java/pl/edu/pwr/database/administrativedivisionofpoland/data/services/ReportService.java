package pl.edu.pwr.database.administrativedivisionofpoland.data.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.ReportDto;
import pl.edu.pwr.contract.Reports.AddReportRequest;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.api.Creatable;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.api.Gettable;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class ReportService extends Service implements Creatable<AddReportRequest, HttpResponse<String>>, Gettable<ReportDto> {


    public ReportService(HttpClient httpClient,
                         ObjectMapper objectMapper,
                         String serverAddress,
                         String serverPort) {
        super(httpClient, objectMapper, serverAddress, serverPort);
    }

    @Override
    public HttpResponse<String> create(AddReportRequest addReportRequest) throws Exception {
        HashMap<String, Object> values = new HashMap<>();
        for(Field field : addReportRequest.getClass().getFields()){
            if(field.get(addReportRequest) != null) {
                values.put(field.getName(), field.get(addReportRequest).toString());
            }else{
                values.put(field.getName(), " ");
            }
        }

        String requestBody = objectMapper.writeValueAsString(values);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/report/add"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public PageResult<ReportDto> get(Object Id, int page, int size) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + serverAddress + ":" + serverPort + "/api/report/all?page=" + page + "&size=" + size))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
    }

}

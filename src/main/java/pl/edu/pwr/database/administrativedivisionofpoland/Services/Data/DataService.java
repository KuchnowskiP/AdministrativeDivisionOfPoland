package pl.edu.pwr.database.administrativedivisionofpoland.Services.Data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Commune.CommuneRequest;
import pl.edu.pwr.contract.County.CountyRequest;
import pl.edu.pwr.contract.Dtos.*;
import pl.edu.pwr.contract.History.CommuneHistoryDto;
import pl.edu.pwr.contract.History.CountyHistoryDto;
import pl.edu.pwr.contract.History.VoivodeshipHistoryDto;
import pl.edu.pwr.contract.OfficeAdres.OfficeAddressRequest;
import pl.edu.pwr.contract.Reports.AddReportRequest;
import pl.edu.pwr.contract.Voivodeship.VoivodeshipRequest;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Objects;

public class DataService {
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().build();

    public static PageResult<VoivodeshipDto> getVoivodeships(int page, int size) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.196.2:8085/api/voivodeship/all?page=" + page + "&size=" + size))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        PageResult<VoivodeshipDto> result = objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
        return result;
    }
    public static PageResult<CountyDto> getCounties(Object voivodeshipId, int page, int size) throws Exception {
        HttpRequest request;
        if (voivodeshipId.equals(-1)) {
            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://192.168.196.2:8085/api/county/all?page=" + page + "&size=" + size))
                    .header("Content-Type", "application/json")
                    .build();
        }else{
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
    public static PageResult<CountyAddressData> getCountiesWithAddresses(Object voivodeshipId, int page, int size) throws IOException, InterruptedException {
        HttpRequest request;
        if (voivodeshipId.equals(-1)) {
            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://192.168.196.2:8085/api/county/address/all?page=" + page + "&size=" + size))
                    .header("Content-Type", "application/json")
                    .build();
        }else{
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

    public static PageResult<CommuneDto> getCommunes(Object countyID, int page, int size) throws Exception {
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

    public static PageResult<CommuneAddressData> getCommunesWithAddresses(Object countyID, int page, int size) throws Exception {
        HttpRequest request;
        if (countyID.equals(-1)) {
            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://192.168.196.2:8085/api/commune/address/all?page=" + page + "&size=" + size))
                    .header("Content-Type", "application/json")
                    .build();
        }else{
            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://192.168.196.2:8085/api/commune/address/byCounty?countyId=" + countyID + "&page=" + page + "&size=" + size))
                    .header("Content-Type", "application/json")
                    .build();
        }

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        PageResult<CommuneAddressData> result = objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
        return result;
    }

    public static void createReport(AddReportRequest addReportRequest) throws Exception {
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

    public static PageResult<ReportDto> getReports(int page, int size) throws Exception {

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

    public static PageResult<VoivodeshipAddressData> getVoivodeshipsWithAddresses(int page, int size) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.196.2:8085/api/voivodeship/address/all?page=" + page + "&size=" + size))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        PageResult<VoivodeshipAddressData> result = objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
        return result;
    }

    public static PageResult<OfficeAddressDto> getAllAddresses(int page, int size) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.196.2:8085/api/address/all?page=" + page + "&size=" + size))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        PageResult<OfficeAddressDto> result = objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
        return result;
    }

    public static HttpResponse<String> addOfficeAddress(OfficeAddressRequest addOfficeAddressRequest)
            throws IllegalAccessException, IOException, InterruptedException {
        HashMap<String, Object> values = new HashMap<String, Object>();
        for(Field field : addOfficeAddressRequest.getClass().getFields()){
            Object o = new AddReportRequest();
            if(field.get(addOfficeAddressRequest) != null) {
                values.put(field.getName(), field.get(addOfficeAddressRequest).toString());
            }else{
                values.put(field.getName(), " ");
            }
        }

        String requestBody = objectMapper.writeValueAsString(values);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.196.2:8085/api/address/add"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    public static String getNewVoivodeshipTeryt() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.196.2:8085/api/voivodeship/teryt"))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        int newTerytInteger = Integer.parseInt(response.body()) + 200000;
        String result = String.format("%07d", newTerytInteger);
        return result;
    }

    public static String getNewCountyTeryt(Integer id, int city) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.196.2:8085/api/county/teryt?voivodeshipId=" + id))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String result = null;
        if(!Objects.equals(response.body(), "")) {
            int newTerytInteger = Integer.parseInt(response.body()) + 1000;
            newTerytInteger /= 10;
            newTerytInteger *= 10;
            newTerytInteger += city;
            result = String.format("%07d", newTerytInteger);
        }
        return result;
    }

    public static String getNewCommuneTeryt(Integer id, int type) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.196.2:8085/api/commune/teryt?countyId=" + id))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String result = null;
        if(!Objects.equals(response.body(), "")) {
            int newTerytInteger = Integer.parseInt(response.body()) + 10;
            newTerytInteger /= 10;
            newTerytInteger *= 10;
            newTerytInteger += type;
            result = String.format("%07d", newTerytInteger);
        }
        return result;
    }

    public static PageResult<VoivodeshipHistoryDto> getVoivodeshipsHistory(int page, int size) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.196.2:8085/api/history/voivodeships?page=" + page + "&size=" + size))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        PageResult<VoivodeshipHistoryDto> result = objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
        System.out.println("wojewodztwa: " + response.body());
        return result;
    }

    public static PageResult<CountyHistoryDto> getCountiesHistory(int page, int size) throws IOException, InterruptedException {
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

    public static PageResult<CommuneHistoryDto> getCommunesHistory(int page, int size) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.196.2:8085/api/history/communes?page=" + page + "&size=" + size))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        PageResult<CommuneHistoryDto> result = objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
        System.out.println("gminy: " + response.body());
        return result;
    }

    protected static boolean createCounty(CountyRequest countyRequest) throws IllegalAccessException, IOException, InterruptedException {
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
                .uri(URI.create("http://192.168.196.2:8085/api/county/add"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200;
    }

    protected static boolean createCommune(CommuneRequest communeRequest) throws IOException, InterruptedException, IllegalAccessException {
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

    protected static boolean countyDeletion(int ID) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.196.2:8085/api/county/delete/" + ID))
                .DELETE()
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 204;
    }

    protected static boolean communeDeletion(int ID) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.196.2:8085/api/commune/delete/" + ID))
                .DELETE()
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 204;
    }

    public static CountyDto countyById(int ID) throws IOException, InterruptedException {
       HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.196.2:8085/api/county/" + ID))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        CountyDto result =  objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
        System.out.println(result);
        return result;
    }
    public static CommuneDto communeById(int ID) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.196.2:8085/api/commune/" + ID))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        CommuneDto result =  objectMapper.readValue(
                response.body(), new TypeReference<>() {
                });
        System.out.println(result);
        return result;
    }
}

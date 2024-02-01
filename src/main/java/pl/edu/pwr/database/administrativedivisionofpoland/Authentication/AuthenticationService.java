package pl.edu.pwr.database.administrativedivisionofpoland.Authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import pl.edu.pwr.contract.Auth.AuthenticationResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationService {
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().build();
    private static final String authenticationUrl = "http://192.168.196.2:8085/api/auth/authenticate";
    private String login;
    private String password;
    private String token;
    public static AuthenticationService instance = new AuthenticationService();

    public static AuthenticationService getInstance(){
        return instance;
    }

    public boolean authenticate(String login, String password) {
        this.login = login;
        this.password = password;

        String requestBody;
        HttpResponse<String> response;
        try {
            HashMap<String, String> body = new HashMap<>(
                    Map.ofEntries(
                            Map.entry("login", login),
                            Map.entry("password", password)
                    ));
            requestBody = objectMapper.writeValueAsString(body);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(authenticationUrl))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .header("Content-Type", "application/json")
                    .build();

            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException | InterruptedException e) {
            return false;
        }

        if (response.statusCode() != 200) {
            return false;
        }

        AuthenticationResponse result;
        try {
            result = objectMapper.readValue(
                    response.body(), new TypeReference<>() {
                    });
        } catch (JsonProcessingException e) {
            return false;
        }

        token = result.getToken();
        return true;
    }

    public Map.Entry<String, String> getBearerTokenHeader() throws Exception {
        if (token == null) {
            if (login == null || password == null) {
                throw new Exception("No login or password provided");
            }

            boolean authenticated = authenticate(login, password);
            if (!authenticated) {
                throw new Exception("Authentication failed");
            }
        }

        return Map.entry("Authorization", "Bearer " + token);
    }

}

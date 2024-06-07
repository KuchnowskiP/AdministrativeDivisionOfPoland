package pl.edu.pwr.database.administrativedivisionofpoland.authentication;

import java.util.Map;

public interface IAuthenticationService {
    boolean authenticate(String username, String password);
    Map.Entry<String, String> getCredentials() throws Exception;
}

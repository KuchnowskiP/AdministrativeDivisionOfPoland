package pl.edu.pwr.database.administrativedivisionofpoland.Authentication;

import java.util.Map;

public interface IAuthenticationService {
    boolean authenticate(String username, String password);
    Map.Entry<String, String> getCredentials() throws Exception;
}

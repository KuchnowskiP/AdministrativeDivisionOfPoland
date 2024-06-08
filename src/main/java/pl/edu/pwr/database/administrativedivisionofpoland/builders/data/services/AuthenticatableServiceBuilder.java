package pl.edu.pwr.database.administrativedivisionofpoland.builders.data.services;

import pl.edu.pwr.database.administrativedivisionofpoland.authentication.IAuthenticationService;

public abstract class AuthenticatableServiceBuilder extends ServiceBuilder {
    protected IAuthenticationService authenticationService;

    @Override
    public void reset() {
        super.reset();
        this.authenticationService = null;
    }

    public AuthenticatableServiceBuilder setAuthenticationService(IAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
        return this;
    }
}

package pl.edu.pwr.database.administrativedivisionofpoland.builders.data.services;

import pl.edu.pwr.database.administrativedivisionofpoland.data.services.ReportService;

public class ReportServiceBuilder extends ServiceBuilder {
    public ReportService getResult() {
        return new ReportService(httpClient, objectMapper, serverAddress, serverPort);
    }
}

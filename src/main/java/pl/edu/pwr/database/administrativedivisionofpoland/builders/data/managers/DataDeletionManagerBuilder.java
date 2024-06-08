package pl.edu.pwr.database.administrativedivisionofpoland.builders.data.managers;

import pl.edu.pwr.database.administrativedivisionofpoland.data.managers.DataDeletionManager;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.api.Deletable;

public class DataDeletionManagerBuilder {
    private Deletable voivodeshipService;
    private Deletable countyService;
    private Deletable communeService;

    public void setVoivodeshipService(Deletable voivodeshipService) {
        this.voivodeshipService = voivodeshipService;
    }

    public void setCountyService(Deletable countyService) {
        this.countyService = countyService;
    }

    public void setCommuneService(Deletable communeService) {
        this.communeService = communeService;
    }

    public DataDeletionManager getResult() {
        return new DataDeletionManager(voivodeshipService, countyService, communeService);
    }
}

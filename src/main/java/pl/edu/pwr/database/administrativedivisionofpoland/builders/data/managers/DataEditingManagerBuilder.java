package pl.edu.pwr.database.administrativedivisionofpoland.builders.data.managers;

import pl.edu.pwr.contract.Commune.CommuneRequest;
import pl.edu.pwr.contract.County.CountyRequest;
import pl.edu.pwr.contract.Voivodeship.VoivodeshipRequest;
import pl.edu.pwr.database.administrativedivisionofpoland.data.managers.DataEditingManager;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.api.Editable;

public class DataEditingManagerBuilder {
    private Editable<VoivodeshipRequest> voivodeshipService;
    private Editable<CountyRequest> countyService;
    private Editable<CommuneRequest> communeService;

    public void setVoivodeshipService(Editable<VoivodeshipRequest> voivodeshipService) {
        this.voivodeshipService = voivodeshipService;
    }

    public void setCountyService(Editable<CountyRequest> countyService) {
        this.countyService = countyService;
    }

    public void setCommuneService(Editable<CommuneRequest> communeService) {
        this.communeService = communeService;
    }

    public DataEditingManager getResult() {
        return new DataEditingManager(voivodeshipService, countyService, communeService);
    }
}

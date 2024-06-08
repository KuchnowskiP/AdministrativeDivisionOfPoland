package pl.edu.pwr.database.administrativedivisionofpoland.builders.data.managers;

import pl.edu.pwr.contract.Commune.CommuneRequest;
import pl.edu.pwr.contract.County.CountyRequest;
import pl.edu.pwr.contract.OfficeAdres.OfficeAddressRequest;
import pl.edu.pwr.contract.Reports.AddReportRequest;
import pl.edu.pwr.contract.Voivodeship.VoivodeshipRequest;
import pl.edu.pwr.database.administrativedivisionofpoland.data.managers.DataCreationManager;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.api.Creatable;

import java.net.http.HttpResponse;

public class DataCreationManagerBuilder {
    private Creatable<VoivodeshipRequest, Boolean> voivodeshipCreator;
    private Creatable<CountyRequest, Boolean> countyCreator;
    private Creatable<CommuneRequest, Boolean> communeCreator;
    private Creatable<AddReportRequest, HttpResponse<String>> reportCreator;
    private Creatable<OfficeAddressRequest, HttpResponse<String>> addressCreator;


    public void setVoivodeshipCreator(Creatable<VoivodeshipRequest, Boolean> voivodeshipCreator) {
        this.voivodeshipCreator = voivodeshipCreator;
    }

    public void setCountyCreator(Creatable<CountyRequest, Boolean> countyCreator) {
        this.countyCreator = countyCreator;
    }

    public void setCommuneCreator(Creatable<CommuneRequest, Boolean> communeCreator) {
        this.communeCreator = communeCreator;
    }

    public void setReportCreator(Creatable<AddReportRequest, HttpResponse<String>> reportCreator) {
        this.reportCreator = reportCreator;
    }

    public void setAddressCreator(Creatable<OfficeAddressRequest, HttpResponse<String>> addressCreator) {
        this.addressCreator = addressCreator;
    }

    public DataCreationManager getResult() {
        return new DataCreationManager(voivodeshipCreator, countyCreator, communeCreator, reportCreator, addressCreator);
    }
}

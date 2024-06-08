package pl.edu.pwr.database.administrativedivisionofpoland.builders.data.managers;

import pl.edu.pwr.contract.Commune.CommuneRequest;
import pl.edu.pwr.contract.County.CountyRequest;
import pl.edu.pwr.contract.OfficeAdres.OfficeAddressRequest;
import pl.edu.pwr.contract.Reports.AddReportRequest;
import pl.edu.pwr.contract.Voivodeship.VoivodeshipRequest;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.*;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.api.Creatable;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.api.Deletable;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.api.Editable;

import java.net.http.HttpResponse;

public class DataManagerDirector {
    private final Creatable<VoivodeshipRequest, Boolean> voivodeshipCreator;
    private final Creatable<CountyRequest, Boolean> countyCreator;
    private final Creatable<CommuneRequest, Boolean> communeCreator;
    private final Creatable<AddReportRequest, HttpResponse<String>> reportCreator;
    private final Creatable<OfficeAddressRequest, HttpResponse<String>> addressCreator;
    private final Editable<VoivodeshipRequest> voivodeshipEditor;
    private final Editable<CountyRequest> countyEditor;
    private final Editable<CommuneRequest> communeEditor;
    private final Deletable voivodeshipDeleter;
    private final Deletable countyDeleter;
    private final Deletable communeDeleter;

    public DataManagerDirector(VoivodeshipService voivodeshipService,
                               CountyService countyService,
                               CommuneService communeService,
                               ReportService reportService,
                               AddressService addressService) {
        this.voivodeshipCreator = voivodeshipService;
        this.countyCreator = countyService;
        this.communeCreator = communeService;
        this.reportCreator = reportService;
        this.addressCreator = addressService;
        this.voivodeshipEditor = voivodeshipService;
        this.countyEditor = countyService;
        this.communeEditor = communeService;
        this.voivodeshipDeleter = voivodeshipService;
        this.countyDeleter = countyService;
        this.communeDeleter = communeService;
    }

    public void constructCreationManager(DataCreationManagerBuilder builder) {
        builder.setVoivodeshipCreator(voivodeshipCreator);
        builder.setCountyCreator(countyCreator);
        builder.setCommuneCreator(communeCreator);
        builder.setReportCreator(reportCreator);
        builder.setAddressCreator(addressCreator);
    }

    public void constructEditingManager(DataEditingManagerBuilder builder) {
        builder.setVoivodeshipService(voivodeshipEditor);
        builder.setCountyService(countyEditor);
        builder.setCommuneService(communeEditor);
    }

    public void constructDeletionManager(DataDeletionManagerBuilder builder) {
        builder.setVoivodeshipService(voivodeshipDeleter);
        builder.setCountyService(countyDeleter);
        builder.setCommuneService(communeDeleter);
    }
}

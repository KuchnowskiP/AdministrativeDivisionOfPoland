package pl.edu.pwr.database.administrativedivisionofpoland.data.managers;

import pl.edu.pwr.contract.Commune.CommuneRequest;
import pl.edu.pwr.contract.County.CountyRequest;
import pl.edu.pwr.contract.OfficeAdres.OfficeAddressRequest;
import pl.edu.pwr.contract.Reports.AddReportRequest;
import pl.edu.pwr.contract.Voivodeship.VoivodeshipRequest;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.api.Creatable;

import java.net.http.HttpResponse;

public class DataCreationManager implements IDataCreationManager {
    private final Creatable<VoivodeshipRequest, Boolean> voivodeshipCreator;
    private final Creatable<CountyRequest, Boolean> countyCreator;
    private final Creatable<CommuneRequest, Boolean> communeCreator;
    private final Creatable<AddReportRequest, HttpResponse<String>> reportCreator;
    private final Creatable<OfficeAddressRequest, HttpResponse<String>> addressCreator;

    public DataCreationManager(
            Creatable<VoivodeshipRequest, Boolean> voivodeshipCreator,
            Creatable<CountyRequest, Boolean> countyCreator,
            Creatable<CommuneRequest, Boolean> communeCreator,
            Creatable<AddReportRequest, HttpResponse<String>> reportCreator,
            Creatable<OfficeAddressRequest, HttpResponse<String>> addressCreator
    ) {
        this.voivodeshipCreator = voivodeshipCreator;
        this.countyCreator = countyCreator;
        this.communeCreator = communeCreator;
        this.reportCreator = reportCreator;
        this.addressCreator = addressCreator;
    }

    @Override
    public boolean addVoivodeship(VoivodeshipRequest voivodeshipRequest) throws Exception {
        return voivodeshipCreator.create(voivodeshipRequest);
    }

    @Override
    public boolean addCounty(CountyRequest countyRequest) throws Exception {
        return countyCreator.create(countyRequest);
    }

    @Override
    public boolean addCommune(CommuneRequest communeRequest) throws Exception {
        return communeCreator.create(communeRequest);
    }

    @Override
    public HttpResponse<String> addReport(AddReportRequest addReportRequest) throws Exception {
        return reportCreator.create(addReportRequest);
    }

    @Override
    public HttpResponse<String> addOfficeAddress(OfficeAddressRequest officeAddressRequest) throws Exception {
        return addressCreator.create(officeAddressRequest);
    }
}

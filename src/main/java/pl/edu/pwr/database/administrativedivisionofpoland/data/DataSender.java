package pl.edu.pwr.database.administrativedivisionofpoland.data;

import pl.edu.pwr.contract.Commune.CommuneRequest;
import pl.edu.pwr.contract.County.CountyRequest;
import pl.edu.pwr.contract.OfficeAdres.OfficeAddressRequest;
import pl.edu.pwr.contract.Reports.AddReportRequest;
import pl.edu.pwr.contract.Voivodeship.VoivodeshipRequest;
import pl.edu.pwr.database.administrativedivisionofpoland.data.api.IDataSender;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.api.Creatable;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.api.Deletable;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.api.Editable;

import java.net.http.HttpResponse;

public class DataSender implements IDataSender {
    Creatable<VoivodeshipRequest, Boolean> voivodeshipCreator;
    Creatable<CountyRequest, Boolean> countyCreator;
    Creatable<CommuneRequest, Boolean> communeCreator;
    Creatable<AddReportRequest, HttpResponse<String>> reportCreator;
    Creatable<OfficeAddressRequest, HttpResponse<String>> addressCreator;
    Editable<VoivodeshipRequest> voivodeshipEditor;
    Editable<CountyRequest> countyEditor;
    Editable<CommuneRequest> communeEditor;
    Deletable voivodeshipDeleter;
    Deletable countyDeleter;
    Deletable communeDeleter;

    public DataSender(
            Creatable<VoivodeshipRequest, Boolean> voivodeshipCreator,
            Creatable<CountyRequest, Boolean> countyCreator,
            Creatable<CommuneRequest, Boolean> communeCreator,
            Creatable<AddReportRequest, HttpResponse<String>> reportCreator,
            Creatable<OfficeAddressRequest, HttpResponse<String>> addressCreator,
            Editable<VoivodeshipRequest> voivodeshipEditor,
            Editable<CountyRequest> countyEditor,
            Editable<CommuneRequest> communeEditor,
            Deletable voivodeshipDeleter,
            Deletable countyDeleter,
            Deletable communeDeleter) {
        this.voivodeshipCreator = voivodeshipCreator;
        this.countyCreator = countyCreator;
        this.communeCreator = communeCreator;
        this.reportCreator = reportCreator;
        this.addressCreator = addressCreator;
        this.voivodeshipEditor = voivodeshipEditor;
        this.countyEditor = countyEditor;
        this.communeEditor = communeEditor;
        this.voivodeshipDeleter = voivodeshipDeleter;
        this.countyDeleter = countyDeleter;
        this.communeDeleter = communeDeleter;
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
    public void addReport(AddReportRequest addReportRequest) throws Exception {
        reportCreator.create(addReportRequest);
    }
    @Override
    public HttpResponse<String> addAddress(OfficeAddressRequest officeAddressRequest) throws Exception {
        return addressCreator.create(officeAddressRequest);
    }
    @Override
    public boolean editVoivodeship(int unitID, VoivodeshipRequest voivodeshipRequest) throws Exception {
        return voivodeshipEditor.edit(unitID, voivodeshipRequest);
    }
    @Override
    public boolean editCounty(int unitID, CountyRequest countyRequest) throws Exception {
        return countyEditor.edit(unitID, countyRequest);
    }
    @Override
    public boolean editCommune(Integer id, CommuneRequest communeRequest) throws Exception {
        return communeEditor.edit(id, communeRequest);
    }
    @Override
    public boolean deleteVoivodeship(int ID) throws Exception {
        return voivodeshipDeleter.delete(ID);
    }
    @Override
    public boolean deleteCounty(int ID) throws Exception {
        return countyDeleter.delete(ID);
    }
    @Override
    public boolean deleteCommune(int ID) throws Exception {
        return communeDeleter.delete(ID);
    }
}

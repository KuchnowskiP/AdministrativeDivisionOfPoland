package pl.edu.pwr.database.administrativedivisionofpoland.Data;

import pl.edu.pwr.contract.Commune.CommuneRequest;
import pl.edu.pwr.contract.County.CountyRequest;
import pl.edu.pwr.contract.OfficeAdres.OfficeAddressRequest;
import pl.edu.pwr.contract.Reports.AddReportRequest;
import pl.edu.pwr.contract.Voivodeship.VoivodeshipRequest;
import pl.edu.pwr.database.administrativedivisionofpoland.Data.Services.*;

import java.io.IOException;
import java.net.http.HttpResponse;

public class DataSender {
    private final VoivodeshipDataService voivodeshipDataService = new VoivodeshipDataService();
    private final CountyDataService countyDataService = new CountyDataService();
    private final CommuneDataService communeDataService = new CommuneDataService();
    private final AddressDataService addressDataService = new AddressDataService();
    private final ReportDataService reportDataService = new ReportDataService();

    public boolean addCounty(CountyRequest countyRequest) throws Exception {
        return countyDataService.create(countyRequest);
    }

    public boolean addCommune(CommuneRequest communeRequest) throws Exception {
        return communeDataService.create(communeRequest);
    }

    public void addReport(AddReportRequest addReportRequest) throws Exception {
        reportDataService.createReport(addReportRequest);
    }
    public boolean addVoivodeship(VoivodeshipRequest voivodeshipRequest) throws Exception {
        return voivodeshipDataService.create(voivodeshipRequest);
    }

    public HttpResponse<String> addAddress(OfficeAddressRequest officeAddressRequest) throws Exception {
        return addressDataService.addOfficeAddress(officeAddressRequest);
    }
    public boolean editVoivodeship(int unitID, VoivodeshipRequest voivodeshipRequest) throws Exception {
        return voivodeshipDataService.edit(unitID, voivodeshipRequest);
    }
    public boolean editCounty(int unitID, CountyRequest countyRequest) throws Exception {
        return countyDataService.edit(unitID, countyRequest);
    }

    public boolean editCommune(Integer id, CommuneRequest communeRequest) throws Exception {
        return communeDataService.edit(id, communeRequest);
    }
    public boolean deleteVoivodeship(int ID) throws Exception {
        return voivodeshipDataService.delete(ID);
    }
    public boolean deleteCounty(int ID) throws Exception {
        return countyDataService.delete(ID);
    }
    public boolean deleteCommune(int ID) throws Exception {
        return communeDataService.delete(ID);
    }
}

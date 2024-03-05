package pl.edu.pwr.database.administrativedivisionofpoland.Data;

import pl.edu.pwr.contract.Commune.CommuneRequest;
import pl.edu.pwr.contract.County.CountyRequest;
import pl.edu.pwr.contract.OfficeAdres.OfficeAddressRequest;
import pl.edu.pwr.contract.Reports.AddReportRequest;
import pl.edu.pwr.contract.Voivodeship.VoivodeshipRequest;
import pl.edu.pwr.database.administrativedivisionofpoland.Data.Services.*;

import java.net.http.HttpResponse;

public class DataSender {
    private final VoivodeshipService voivodeshipDataService = new VoivodeshipService();
    private final CountyService countyService = new CountyService();
    private final CommuneService communeService = new CommuneService();
    private final AddressService addressService = new AddressService();
    private final ReportService reportService = new ReportService();

    public boolean addCounty(CountyRequest countyRequest) throws Exception {
        return countyService.create(countyRequest);
    }

    public boolean addCommune(CommuneRequest communeRequest) throws Exception {
        return communeService.create(communeRequest);
    }

    public void addReport(AddReportRequest addReportRequest) throws Exception {
        reportService.createReport(addReportRequest);
    }
    public boolean addVoivodeship(VoivodeshipRequest voivodeshipRequest) throws Exception {
        return voivodeshipDataService.create(voivodeshipRequest);
    }

    public HttpResponse<String> addAddress(OfficeAddressRequest officeAddressRequest) throws Exception {
        return addressService.addOfficeAddress(officeAddressRequest);
    }
    public boolean editVoivodeship(int unitID, VoivodeshipRequest voivodeshipRequest) throws Exception {
        return voivodeshipDataService.edit(unitID, voivodeshipRequest);
    }
    public boolean editCounty(int unitID, CountyRequest countyRequest) throws Exception {
        return countyService.edit(unitID, countyRequest);
    }

    public boolean editCommune(Integer id, CommuneRequest communeRequest) throws Exception {
        return communeService.edit(id, communeRequest);
    }
    public boolean deleteVoivodeship(int ID) throws Exception {
        return voivodeshipDataService.delete(ID);
    }
    public boolean deleteCounty(int ID) throws Exception {
        return countyService.delete(ID);
    }
    public boolean deleteCommune(int ID) throws Exception {
        return communeService.delete(ID);
    }
}

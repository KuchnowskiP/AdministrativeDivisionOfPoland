package pl.edu.pwr.database.administrativedivisionofpoland.Data;

import pl.edu.pwr.contract.Commune.CommuneRequest;
import pl.edu.pwr.contract.County.CountyRequest;
import pl.edu.pwr.contract.OfficeAdres.OfficeAddressRequest;
import pl.edu.pwr.contract.Reports.AddReportRequest;
import pl.edu.pwr.contract.Voivodeship.VoivodeshipRequest;
import pl.edu.pwr.database.administrativedivisionofpoland.Data.Services.*;

import java.net.http.HttpResponse;

public interface IDataSender {
    VoivodeshipService voivodeshipDataService = new VoivodeshipService();
    CountyService countyService = new CountyService();
    CommuneService communeService = new CommuneService();
    AddressService addressService = new AddressService();
    ReportService reportService = new ReportService();

    boolean addCounty(CountyRequest countyRequest) throws Exception;
    boolean addCommune(CommuneRequest communeRequest) throws Exception;
    void addReport(AddReportRequest addReportRequest) throws Exception;
    boolean addVoivodeship(VoivodeshipRequest voivodeshipRequest) throws Exception;
    HttpResponse<String> addAddress(OfficeAddressRequest officeAddressRequest) throws Exception;
    boolean editVoivodeship(int unitID, VoivodeshipRequest voivodeshipRequest) throws Exception;
    boolean editCounty(int unitID, CountyRequest countyRequest) throws Exception;
    boolean editCommune(Integer id, CommuneRequest communeRequest) throws Exception;
    boolean deleteVoivodeship(int ID) throws Exception;
    boolean deleteCounty(int ID) throws Exception;
    boolean deleteCommune(int ID) throws Exception;
}

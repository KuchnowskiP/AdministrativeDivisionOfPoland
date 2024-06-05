package pl.edu.pwr.database.administrativedivisionofpoland.Data;

import pl.edu.pwr.contract.Commune.CommuneRequest;
import pl.edu.pwr.contract.County.CountyRequest;
import pl.edu.pwr.contract.OfficeAdres.OfficeAddressRequest;
import pl.edu.pwr.contract.Reports.AddReportRequest;
import pl.edu.pwr.contract.Voivodeship.VoivodeshipRequest;
import pl.edu.pwr.database.administrativedivisionofpoland.Data.Services.*;

import java.net.http.HttpResponse;

public class DataSender implements IDataSender {
    @Override
    public boolean addCounty(CountyRequest countyRequest) throws Exception {
        return countyService.create(countyRequest);
    }
    @Override
    public boolean addCommune(CommuneRequest communeRequest) throws Exception {
        return communeService.create(communeRequest);
    }
    @Override
    public void addReport(AddReportRequest addReportRequest) throws Exception {
        reportService.createReport(addReportRequest);
    }
    @Override
    public boolean addVoivodeship(VoivodeshipRequest voivodeshipRequest) throws Exception {
        return voivodeshipDataService.create(voivodeshipRequest);
    }
    @Override
    public HttpResponse<String> addAddress(OfficeAddressRequest officeAddressRequest) throws Exception {
        return addressService.addOfficeAddress(officeAddressRequest);
    }
    @Override
    public boolean editVoivodeship(int unitID, VoivodeshipRequest voivodeshipRequest) throws Exception {
        return voivodeshipDataService.edit(unitID, voivodeshipRequest);
    }
    @Override
    public boolean editCounty(int unitID, CountyRequest countyRequest) throws Exception {
        return countyService.edit(unitID, countyRequest);
    }
    @Override
    public boolean editCommune(Integer id, CommuneRequest communeRequest) throws Exception {
        return communeService.edit(id, communeRequest);
    }
    @Override
    public boolean deleteVoivodeship(int ID) throws Exception {
        return voivodeshipDataService.delete(ID);
    }
    @Override
    public boolean deleteCounty(int ID) throws Exception {
        return countyService.delete(ID);
    }
    @Override
    public boolean deleteCommune(int ID) throws Exception {
        return communeService.delete(ID);
    }
}

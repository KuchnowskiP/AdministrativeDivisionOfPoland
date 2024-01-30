package pl.edu.pwr.database.administrativedivisionofpoland.Services.Data;

import pl.edu.pwr.contract.Commune.CommuneRequest;
import pl.edu.pwr.contract.County.CountyRequest;
import pl.edu.pwr.contract.OfficeAdres.OfficeAddressRequest;
import pl.edu.pwr.contract.Reports.AddReportRequest;
import pl.edu.pwr.contract.Voivodeship.VoivodeshipRequest;

import java.io.IOException;
import java.net.http.HttpResponse;

public class DataSender {
    VoivodeshipDataService voivodeshipDataService = new VoivodeshipDataService();
    CountyDataService countyDataService = new CountyDataService();
    CommuneDataService communeDataService = new CommuneDataService();
    public boolean addCounty(CountyRequest countyRequest) throws IOException, InterruptedException, IllegalAccessException {
        return countyDataService.create(countyRequest);
    }

    public boolean addCommune(CommuneRequest communeRequest) throws IOException, InterruptedException, IllegalAccessException {
        return communeDataService.create(communeRequest);
    }

    public void addReport(AddReportRequest addReportRequest) throws Exception {
        DataService.createReport(addReportRequest);
    }
    public boolean addVoivodeship(VoivodeshipRequest voivodeshipRequest) throws Exception {
        return voivodeshipDataService.create(voivodeshipRequest);
    }

    public HttpResponse<String> addAddress(OfficeAddressRequest officeAddressRequest) throws Exception {
        return DataService.addOfficeAddress(officeAddressRequest);
    }
    public boolean editVoivodeship(int unitID, VoivodeshipRequest voivodeshipRequest) throws IOException, InterruptedException, IllegalAccessException{
        return voivodeshipDataService.edit(unitID, voivodeshipRequest);
    }
    public boolean editCounty(int unitID, CountyRequest countyRequest) throws IOException, InterruptedException, IllegalAccessException {
        return countyDataService.edit(unitID, countyRequest);
    }

    public boolean editCommune(Integer id, CommuneRequest communeRequest) throws IOException, InterruptedException, IllegalAccessException {
        return communeDataService.edit(id, communeRequest);
    }

    public String newVoivodeshipTeryt() throws IOException, InterruptedException {
        return DataService.getNewVoivodeshipTeryt();
    }

    public String newCountyTeryt(Integer id, int city) throws IOException, InterruptedException {
        return DataService.getNewCountyTeryt(id, city);
    }

    public String newCommuneTeryt(Integer id, int type) throws IOException, InterruptedException {
        return DataService.getNewCommuneTeryt(id, type);
    }

    public boolean deleteVoivodeship(int ID) throws IOException, InterruptedException {
        return voivodeshipDataService.delete(ID);
    }
    public boolean deleteCounty(int ID) throws IOException, InterruptedException {
        return countyDataService.delete(ID);
    }
    public boolean deleteCommune(int ID) throws IOException, InterruptedException {
        return countyDataService.delete(ID);
    }
}

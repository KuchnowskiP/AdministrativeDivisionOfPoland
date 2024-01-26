package pl.edu.pwr.database.administrativedivisionofpoland.Services;

import pl.edu.pwr.contract.Commune.CommuneRequest;
import pl.edu.pwr.contract.County.CountyRequest;
import pl.edu.pwr.contract.OfficeAdres.OfficeAddressRequest;
import pl.edu.pwr.contract.Reports.AddReportRequest;
import pl.edu.pwr.contract.Voivodeship.VoivodeshipRequest;

import java.io.IOException;
import java.net.http.HttpResponse;

public class DataSender extends DataService {
    public static boolean addCounty(CountyRequest countyRequest) throws IOException, InterruptedException, IllegalAccessException {
        return DataService.createCounty(countyRequest);
    }

    public static boolean addCommune(CommuneRequest communeRequest) throws IOException, InterruptedException, IllegalAccessException {
        return DataService.createCommune(communeRequest);
    }

    public void addReport(AddReportRequest addReportRequest) throws Exception {
        DataService.createReport(addReportRequest);
    }
    public boolean addVoivodeship(VoivodeshipRequest voivodeshipRequest) throws Exception {
        return DataService.createVoivodeship(voivodeshipRequest);
    }

    public HttpResponse<String> addAddress(OfficeAddressRequest officeAddressRequest) throws Exception {
        return DataService.addOfficeAddress(officeAddressRequest);
    }
    public boolean editVoivodeship(int unitID, VoivodeshipRequest voivodeshipRequest) throws IOException, InterruptedException, IllegalAccessException{
        return DataService.editVoivodeshipData(unitID, voivodeshipRequest);
    }

    public String newVoivodeshipTeryt() throws IOException, InterruptedException {
        return DataService.getNewVoivodeshipTeryt();
    }

    public String newCountyTeryt(Integer id) throws IOException, InterruptedException {
        return DataService.getNewCountyTeryt(id);
    }

    public String newCommuneTeryt(Integer id, int type) throws IOException, InterruptedException {
        return DataService.getNewCommuneTeryt(id, type);
    }

    public boolean deleteVoivodeship(int ID) throws IOException, InterruptedException {
        return DataService.voivodeshipDeletion(ID);
    }
    public boolean deleteCounty(int ID) throws IOException, InterruptedException {
        return DataService.countyDeletion(ID);
    }
    public boolean deleteCommune(int ID) throws IOException, InterruptedException {
        return DataService.communeDeletion(ID);
    }
}

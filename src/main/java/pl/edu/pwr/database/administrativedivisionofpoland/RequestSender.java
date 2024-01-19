package pl.edu.pwr.database.administrativedivisionofpoland;

import pl.edu.pwr.contract.Commune.CommuneRequest;
import pl.edu.pwr.contract.County.CountyRequest;
import pl.edu.pwr.contract.OfficeAdres.OfficeAddressRequest;
import pl.edu.pwr.contract.Reports.AddReportRequest;
import pl.edu.pwr.contract.Voivodeship.VoivodeshipRequest;

import java.io.IOException;
import java.net.http.HttpResponse;

public class RequestSender extends Request {
    public static void createCounty(CountyRequest countyRequest) throws IOException, InterruptedException, IllegalAccessException {
        Request.createCounty(countyRequest);
    }

    public static void createCommune(CommuneRequest communeRequest) throws IOException, InterruptedException, IllegalAccessException {
        Request.createCommune(communeRequest);
    }

    public void addReport(AddReportRequest addReportRequest) throws Exception {
        Request.createReport(addReportRequest);
    }
    public void addVoivodeship(VoivodeshipRequest voivodeshipRequest) throws Exception {
        Request.createVoivodeship(voivodeshipRequest);
    }

    public HttpResponse<String> addAddress(OfficeAddressRequest officeAddressRequest) throws Exception {
        return Request.addOfficeAddress(officeAddressRequest);
    }

    public String newVoivodeshipTeryt() throws IOException, InterruptedException {
        return Request.getNewVoivodeshipTeryt();
    }

    public String newCountyTeryt(Integer id) throws IOException, InterruptedException {
        return Request.getNewCountyTeryt(id);
    }

    public String newCommuneTeryt(Integer id, int type) throws IOException, InterruptedException {
        return Request.getNewCommuneTeryt(id, type);
    }

    public boolean deleteVoivodeship(int ID) throws IOException, InterruptedException {
        return Request.voivodeshipDeletion(ID);
    }
    public boolean deleteCounty(int ID) throws IOException, InterruptedException {
        return Request.countyDeletion(ID);
    }
    public boolean deleteCommune(int ID) throws IOException, InterruptedException {
        return Request.communeDeletion(ID);
    }
}

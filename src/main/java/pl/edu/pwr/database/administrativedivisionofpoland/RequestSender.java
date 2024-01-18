package pl.edu.pwr.database.administrativedivisionofpoland;

import pl.edu.pwr.contract.OfficeAdres.OfficeAddressRequest;
import pl.edu.pwr.contract.Reports.AddReportRequest;
import pl.edu.pwr.contract.Voivodeship.VoivodeshipRequest;

import java.io.IOException;
import java.net.http.HttpResponse;

public class RequestSender extends Request {
    public static void createCounty() {
    }

    public static void createCommune() {
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

    public String getMaxTeryt() throws IOException, InterruptedException {
        return Request.getNewVoivodeshipTeryt();
    }
}
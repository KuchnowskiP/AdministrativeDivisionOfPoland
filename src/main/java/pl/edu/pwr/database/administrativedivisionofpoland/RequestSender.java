package pl.edu.pwr.database.administrativedivisionofpoland;

import pl.edu.pwr.contract.OfficeAdres.AddOfficeAddressRequest;
import pl.edu.pwr.contract.Reports.AddReportRequest;
import pl.edu.pwr.contract.Voivodeship.AddVoivodeshipRequest;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RequestSender extends Request {
    public static void createCounty() {
    }

    public static void createCommune() {
    }

    public void addReport(AddReportRequest addReportRequest) throws Exception {
        Request.createReport(addReportRequest);
    }
    public void addVoivodeship(AddVoivodeshipRequest addVoivodeshipRequest) throws Exception {
        Request.createVoivodeship(addVoivodeshipRequest);
    }

    public HttpResponse<String> addAddress(AddOfficeAddressRequest addOfficeAddressRequest) throws Exception {
        return Request.addOfficeAddress(addOfficeAddressRequest);
    }
}

package pl.edu.pwr.database.administrativedivisionofpoland;

import pl.edu.pwr.contract.Reports.AddReportRequest;
import pl.edu.pwr.contract.Voivodeship.AddVoivodeshipRequest;

public class RequestSender extends Request {
    public void addReport(AddReportRequest addReportRequest) throws Exception {
        Request.createReport(addReportRequest);
    }
    public void addVoivodeship(AddVoivodeshipRequest addVoivodeshipRequest) throws Exception {
        Request.createVoivodeship(addVoivodeshipRequest);
    }
}

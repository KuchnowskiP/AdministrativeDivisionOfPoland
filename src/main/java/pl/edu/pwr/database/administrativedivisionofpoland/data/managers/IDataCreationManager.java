package pl.edu.pwr.database.administrativedivisionofpoland.data.managers;

import pl.edu.pwr.contract.Commune.CommuneRequest;
import pl.edu.pwr.contract.County.CountyRequest;
import pl.edu.pwr.contract.OfficeAdres.OfficeAddressRequest;
import pl.edu.pwr.contract.Reports.AddReportRequest;
import pl.edu.pwr.contract.Voivodeship.VoivodeshipRequest;

import java.net.http.HttpResponse;

public interface IDataCreationManager {
    boolean addVoivodeship(VoivodeshipRequest voivodeshipRequest) throws Exception;
    boolean addCounty(CountyRequest countyRequest) throws Exception;
    boolean addCommune(CommuneRequest communeRequest) throws Exception;
    HttpResponse<String> addReport(AddReportRequest reportRequest) throws Exception;
    HttpResponse<String> addOfficeAddress(OfficeAddressRequest officeAddressRequest) throws Exception;
}

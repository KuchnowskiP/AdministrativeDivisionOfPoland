package pl.edu.pwr.database.administrativedivisionofpoland.data;

import pl.edu.pwr.contract.Commune.CommuneRequest;
import pl.edu.pwr.contract.County.CountyRequest;
import pl.edu.pwr.contract.OfficeAdres.OfficeAddressRequest;
import pl.edu.pwr.contract.Reports.AddReportRequest;
import pl.edu.pwr.contract.Voivodeship.VoivodeshipRequest;
import pl.edu.pwr.database.administrativedivisionofpoland.data.managers.IDataCreationManager;
import pl.edu.pwr.database.administrativedivisionofpoland.data.managers.IDataDeletionManager;
import pl.edu.pwr.database.administrativedivisionofpoland.data.managers.IDataEditingManager;

import java.net.http.HttpResponse;

public class DataSender implements IDataSender {
    private final IDataCreationManager dataCreationService;
    private final IDataEditingManager dataEditingService;
    private final IDataDeletionManager dataDeletionService;


    public DataSender(IDataCreationManager dataCreationService,
                      IDataEditingManager dataEditingService,
                      IDataDeletionManager dataDeletionService) {
        this.dataCreationService = dataCreationService;
        this.dataEditingService = dataEditingService;
        this.dataDeletionService = dataDeletionService;

    }

    @Override
    public boolean addVoivodeship(VoivodeshipRequest voivodeshipRequest) throws Exception {
        return dataCreationService.addVoivodeship(voivodeshipRequest);
    }
    @Override
    public boolean addCounty(CountyRequest countyRequest) throws Exception {
        return dataCreationService.addCounty(countyRequest);
    }
    @Override
    public boolean addCommune(CommuneRequest communeRequest) throws Exception {
        return dataCreationService.addCommune(communeRequest);
    }
    @Override
    public  HttpResponse<String> addReport(AddReportRequest addReportRequest) throws Exception {
        return dataCreationService.addReport(addReportRequest);
    }
    @Override
    public HttpResponse<String> addOfficeAddress(OfficeAddressRequest officeAddressRequest) throws Exception {
        return dataCreationService.addOfficeAddress(officeAddressRequest);
    }
    @Override
    public boolean editVoivodeship(int unitID, VoivodeshipRequest voivodeshipRequest) throws Exception {
        return dataEditingService.editVoivodeship(unitID, voivodeshipRequest);
    }
    @Override
    public boolean editCounty(int unitID, CountyRequest countyRequest) throws Exception {
        return dataEditingService.editCounty(unitID, countyRequest);
    }
    @Override
    public boolean editCommune(Integer unitID, CommuneRequest communeRequest) throws Exception {
        return dataEditingService.editCommune(unitID, communeRequest);
    }
    @Override
    public boolean deleteVoivodeship(int unitID) throws Exception {
        return dataDeletionService.deleteVoivodeship(unitID);
    }
    @Override
    public boolean deleteCounty(int unitID) throws Exception {
        return dataDeletionService.deleteCounty(unitID);
    }
    @Override
    public boolean deleteCommune(int unitID) throws Exception {
        return dataDeletionService.deleteCommune(unitID);
    }
}

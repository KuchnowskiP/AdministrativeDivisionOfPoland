package pl.edu.pwr.database.administrativedivisionofpoland.data.managers;

import pl.edu.pwr.contract.Commune.CommuneRequest;
import pl.edu.pwr.contract.County.CountyRequest;
import pl.edu.pwr.contract.Voivodeship.VoivodeshipRequest;

public interface IDataEditingManager {
    boolean editVoivodeship(int unitId, VoivodeshipRequest voivodeshipRequest) throws Exception;
    boolean editCounty(int unitId, CountyRequest countyRequest) throws Exception;
    boolean editCommune(int unitId, CommuneRequest communeRequest) throws Exception;
}

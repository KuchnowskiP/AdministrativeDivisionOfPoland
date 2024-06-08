package pl.edu.pwr.database.administrativedivisionofpoland.data.managers;

public interface IDataDeletionManager {
    boolean deleteVoivodeship(int unitId) throws Exception;
    boolean deleteCounty(int unitId) throws Exception;
    boolean deleteCommune(int unitId) throws Exception;
}

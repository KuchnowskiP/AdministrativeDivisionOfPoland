package pl.edu.pwr.database.administrativedivisionofpoland.data.managers;

import pl.edu.pwr.database.administrativedivisionofpoland.data.services.api.Deletable;

public class DataDeletionManager implements IDataDeletionManager {
    private final Deletable voivodeshipDeleter;
    private final Deletable countyDeleter;
    private final Deletable communeDeleter;

    public DataDeletionManager(Deletable voivodeshipDeleter, Deletable countyDeleter, Deletable communeDeleter) {
        this.voivodeshipDeleter = voivodeshipDeleter;
        this.countyDeleter = countyDeleter;
        this.communeDeleter = communeDeleter;
    }

    @Override
    public boolean deleteVoivodeship(int unitId) throws Exception {
        return voivodeshipDeleter.delete(unitId);
    }

    @Override
    public boolean deleteCounty(int unitId) throws Exception {
        return countyDeleter.delete(unitId);
    }

    @Override
    public boolean deleteCommune(int unitId) throws Exception {
        return communeDeleter.delete(unitId);
    }
}

package pl.edu.pwr.database.administrativedivisionofpoland.Services.Data;

import pl.edu.pwr.contract.Commune.CommuneRequest;

import java.io.IOException;

public class CommuneDataService implements UnitDataServiceInterface<CommuneRequest> {
    @Override
    public boolean create(CommuneRequest unitRequest) throws IllegalAccessException, IOException, InterruptedException {
        return false;
    }

    @Override
    public boolean edit(int ID, CommuneRequest unitRequest) throws IllegalAccessException, IOException, InterruptedException {
        return false;
    }

    @Override
    public boolean delete(int ID) throws IOException, InterruptedException {
        return false;
    }
}

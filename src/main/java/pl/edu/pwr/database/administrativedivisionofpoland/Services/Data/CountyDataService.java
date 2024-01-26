package pl.edu.pwr.database.administrativedivisionofpoland.Services.Data;

import pl.edu.pwr.contract.County.CountyRequest;

import java.io.IOException;

public class CountyDataService implements UnitDataServiceInterface<CountyRequest> {
    @Override
    public boolean create(CountyRequest unitRequest) throws IllegalAccessException, IOException, InterruptedException {
        return false;
    }

    @Override
    public boolean edit(int ID, CountyRequest unitRequest) throws IllegalAccessException, IOException, InterruptedException {
        return false;
    }

    @Override
    public boolean delete(int ID) throws IOException, InterruptedException {
        return false;
    }
}

package pl.edu.pwr.database.administrativedivisionofpoland.data.services.api;

import pl.edu.pwr.contract.Common.PageResult;

import java.io.IOException;

public interface GettableAddress<T> {
    PageResult<T> getAddresses(Object ID, int page, int size) throws IOException, InterruptedException;

}

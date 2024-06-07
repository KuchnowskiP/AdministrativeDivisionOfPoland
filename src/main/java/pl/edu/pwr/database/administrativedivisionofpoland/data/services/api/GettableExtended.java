package pl.edu.pwr.database.administrativedivisionofpoland.data.services.api;

import pl.edu.pwr.contract.Common.PageResult;

import java.io.IOException;

public interface GettableExtended<T> {
    PageResult<T> getExtended(Object ID, int page, int size) throws IOException, InterruptedException;
}

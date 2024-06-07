package pl.edu.pwr.database.administrativedivisionofpoland.data.services.api;

import pl.edu.pwr.contract.Common.PageResult;

import java.io.IOException;

public interface Trackable<T> {
    PageResult<T> getHistory(int page, int size) throws IOException, InterruptedException;
}

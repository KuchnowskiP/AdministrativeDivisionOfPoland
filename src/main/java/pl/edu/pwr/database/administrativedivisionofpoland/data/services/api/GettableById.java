package pl.edu.pwr.database.administrativedivisionofpoland.data.services.api;

import java.io.IOException;

public interface GettableById<T> {
    T getById(int ID) throws IOException, InterruptedException;
}

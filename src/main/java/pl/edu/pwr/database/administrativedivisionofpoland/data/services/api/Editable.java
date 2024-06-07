package pl.edu.pwr.database.administrativedivisionofpoland.data.services.api;

public interface Editable<T> {
    boolean edit(int unitId, T unitRequest) throws Exception;
}

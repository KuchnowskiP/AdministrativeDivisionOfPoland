package pl.edu.pwr.database.administrativedivisionofpoland.data.services.api;

public interface Creatable<T, R> {
    R create(T unitRequest) throws Exception;
}

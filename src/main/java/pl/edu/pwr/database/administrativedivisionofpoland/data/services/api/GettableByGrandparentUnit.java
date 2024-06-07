package pl.edu.pwr.database.administrativedivisionofpoland.data.services.api;

import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.CommuneDto;

import java.io.IOException;

public interface GettableByGrandparentUnit<T> {
    PageResult<CommuneDto> getByGrandparentUnitId(Object voivodeshipId, int page, int size) throws IOException, InterruptedException;
}

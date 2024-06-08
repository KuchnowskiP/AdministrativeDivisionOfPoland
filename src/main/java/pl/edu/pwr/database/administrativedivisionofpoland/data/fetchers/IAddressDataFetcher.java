package pl.edu.pwr.database.administrativedivisionofpoland.data.fetchers;

import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.OfficeAddressDto;

public interface IAddressDataFetcher {
    PageResult<OfficeAddressDto> getAddresses(int page, int pageSize);
}

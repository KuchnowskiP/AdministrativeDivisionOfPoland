package pl.edu.pwr.database.administrativedivisionofpoland.data.fetchers;

import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.CountyAddressData;
import pl.edu.pwr.contract.Dtos.CountyDto;
import pl.edu.pwr.contract.Dtos.CountyExtended;
import pl.edu.pwr.contract.History.CountyHistoryDto;

public interface ICountyDataFetcher {
    PageResult<CountyExtended> getCountiesExtended(int unitId , int page, int pageSize);
    PageResult<CountyHistoryDto> getCountyHistory(int page, int pageSize);
    PageResult<CountyAddressData> getCountyAddresses(int unitId, int page, int pageSize);
    String getNewCountyTeryt(int id, int city);
    CountyDto getCountyById(int unitId);
}

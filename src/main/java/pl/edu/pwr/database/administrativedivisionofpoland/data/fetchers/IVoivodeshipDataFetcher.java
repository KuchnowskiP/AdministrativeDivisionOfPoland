package pl.edu.pwr.database.administrativedivisionofpoland.data.fetchers;

import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.VoivodeshipAddressData;
import pl.edu.pwr.contract.Dtos.VoivodeshipDto;
import pl.edu.pwr.contract.Dtos.VoivodeshipExtended;
import pl.edu.pwr.contract.History.VoivodeshipHistoryDto;

public interface IVoivodeshipDataFetcher {
    PageResult<VoivodeshipDto> getVoivodeships(int page, int pageSize);
    PageResult<VoivodeshipExtended> getVoivodeshipsExtended(int page, int pageSize);
    PageResult<VoivodeshipHistoryDto> getVoivodeshipHistory(int page, int pageSize);
    PageResult<VoivodeshipAddressData> getVoivodeshipAddresses(int page, int pageSize);
    String getNewVoivodeshipTeryt(String[] args);
}

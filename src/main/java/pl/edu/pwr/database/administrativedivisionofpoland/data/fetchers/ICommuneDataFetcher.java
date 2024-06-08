package pl.edu.pwr.database.administrativedivisionofpoland.data.fetchers;

import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.CommuneAddressData;
import pl.edu.pwr.contract.Dtos.CommuneDto;
import pl.edu.pwr.contract.History.CommuneHistoryDto;

public interface ICommuneDataFetcher {
    PageResult<CommuneDto> getCommunes(int unitId ,int page, int pageSize);
    PageResult<CommuneAddressData> getCommuneAddresses(int unitId, int page, int pageSize);
    PageResult<CommuneHistoryDto> getCommuneHistory(int page, int pageSize);
    String getNewCommuneTeryt(int id, int city);
    PageResult<CommuneDto> getCommunesByVoivodeshipId(int voivodeshipId, int page, int pageSize);
    CommuneDto getCommuneById(int unitId);
}

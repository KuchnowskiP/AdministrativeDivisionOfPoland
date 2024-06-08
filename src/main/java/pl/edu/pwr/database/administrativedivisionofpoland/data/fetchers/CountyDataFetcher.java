package pl.edu.pwr.database.administrativedivisionofpoland.data.fetchers;

import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.CountyAddressData;
import pl.edu.pwr.contract.Dtos.CountyDto;
import pl.edu.pwr.contract.Dtos.CountyExtended;
import pl.edu.pwr.contract.History.CountyHistoryDto;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.api.*;

import java.io.IOException;

public class CountyDataFetcher implements ICountyDataFetcher {
    private final Gettable<CountyDto> countyGetter;
    private final GettableExtended<CountyExtended> countyExtendedGetter;
    private final GettableAddress<CountyAddressData> countyAddressGetter;
    private final GettableById<CountyDto> countyByIdGetter;
    private final Trackable<CountyHistoryDto> countyHistorian;
    private final TerytProvider countyTerytProvider;

    public CountyDataFetcher(
            Gettable<CountyDto> countyGetter,
            GettableExtended<CountyExtended> countyExtendedGetter,
            GettableAddress<CountyAddressData> countyAddressGetter,
            GettableById<CountyDto> countyByIdGetter,
            Trackable<CountyHistoryDto> countyHistorian,
            TerytProvider countyTerytProvider
    ) {
        this.countyGetter = countyGetter;
        this.countyExtendedGetter = countyExtendedGetter;
        this.countyAddressGetter = countyAddressGetter;
        this.countyByIdGetter = countyByIdGetter;
        this.countyHistorian = countyHistorian;
        this.countyTerytProvider = countyTerytProvider;
    }

    @Override
    public PageResult<CountyExtended> getCountiesExtended(int unitId, int page, int pageSize) {
        try {
            return countyExtendedGetter.getExtended(unitId, page, pageSize);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageResult<CountyHistoryDto> getCountyHistory(int page, int pageSize) {
        try {
            return countyHistorian.getHistory(page, pageSize);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageResult<CountyAddressData> getCountyAddresses(int unitId, int page, int pageSize) {
        try {
            return countyAddressGetter.getAddresses(unitId, page, pageSize);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getNewCountyTeryt(int id, int city) {
        try {
            return countyTerytProvider.getNewTeryt(new String[]{String.valueOf(id), String.valueOf(city)});
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CountyDto getCountyById(int unitId) {
        try {
            return countyByIdGetter.getById(unitId);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

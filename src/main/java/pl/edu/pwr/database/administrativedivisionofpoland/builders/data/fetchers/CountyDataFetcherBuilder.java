package pl.edu.pwr.database.administrativedivisionofpoland.builders.data.fetchers;

import pl.edu.pwr.contract.Dtos.CountyAddressData;
import pl.edu.pwr.contract.Dtos.CountyDto;
import pl.edu.pwr.contract.Dtos.CountyExtended;
import pl.edu.pwr.contract.History.CountyHistoryDto;
import pl.edu.pwr.database.administrativedivisionofpoland.data.fetchers.CountyDataFetcher;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.api.*;

public class CountyDataFetcherBuilder {
    private Gettable<CountyDto> countyGetter;
    private GettableExtended<CountyExtended> countyExtendedGetter;
    private GettableAddress<CountyAddressData> countyAddressGetter;
    private GettableById<CountyDto> countyByIdGetter;
    private Trackable<CountyHistoryDto> countyHistorian;
    private TerytProvider countyTerytProvider;

    public void setCountyGetter(Gettable<CountyDto> countyGetter) {
        this.countyGetter = countyGetter;
    }

    public void setCountyExtendedGetter(GettableExtended<CountyExtended> countyExtendedGetter) {
        this.countyExtendedGetter = countyExtendedGetter;
    }

    public void setCountyAddressGetter(GettableAddress<CountyAddressData> countyAddressGetter) {
        this.countyAddressGetter = countyAddressGetter;
    }

    public void setCountyByIdGetter(GettableById<CountyDto> countyByIdGetter) {
        this.countyByIdGetter = countyByIdGetter;
    }

    public void setCountyHistorian(Trackable<CountyHistoryDto> countyHistorian) {
        this.countyHistorian = countyHistorian;
    }

    public void setCountyTerytProvider(TerytProvider countyTerytProvider) {
        this.countyTerytProvider = countyTerytProvider;
    }

    public CountyDataFetcher getResult() {
        return new CountyDataFetcher(
                countyGetter,
                countyExtendedGetter,
                countyAddressGetter,
                countyByIdGetter,
                countyHistorian,
                countyTerytProvider
        );
    }
}

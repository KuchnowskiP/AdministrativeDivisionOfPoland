package pl.edu.pwr.database.administrativedivisionofpoland.builders.data.fetchers;

import pl.edu.pwr.contract.Dtos.CommuneAddressData;
import pl.edu.pwr.contract.Dtos.CommuneDto;
import pl.edu.pwr.contract.History.CommuneHistoryDto;
import pl.edu.pwr.database.administrativedivisionofpoland.data.fetchers.CommuneDataFetcher;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.api.*;

public class CommuneDataFetcherBuilder {
    private Gettable<CommuneDto> communeGetter;
    private GettableAddress<CommuneAddressData> communeAddressGetter;
    private GettableById<CommuneDto> communeByIdGetter;
    private GettableByGrandparentUnit<CommuneDto> communeByGPGetter;
    private Trackable<CommuneHistoryDto> communeHistorian;
    private TerytProvider communeTerytProvider;

    public void setCommuneGetter(Gettable<CommuneDto> communeGetter) {
        this.communeGetter = communeGetter;
    }

    public void setCommuneAddressGetter(GettableAddress<CommuneAddressData> communeAddressGetter) {
        this.communeAddressGetter = communeAddressGetter;
    }

    public void setCommuneByIdGetter(GettableById<CommuneDto> communeByIdGetter) {
        this.communeByIdGetter = communeByIdGetter;
    }

    public void setCommuneByGPGetter(GettableByGrandparentUnit<CommuneDto> communeByGPGetter) {
        this.communeByGPGetter = communeByGPGetter;
    }

    public void setCommuneHistorian(Trackable<CommuneHistoryDto> communeHistorian) {
        this.communeHistorian = communeHistorian;
    }

    public void setCommuneTerytProvider(TerytProvider communeTerytProvider) {
        this.communeTerytProvider = communeTerytProvider;
    }

    public CommuneDataFetcher getResult() {
        return new CommuneDataFetcher(
                communeGetter,
                communeAddressGetter,
                communeByIdGetter,
                communeByGPGetter,
                communeHistorian,
                communeTerytProvider
        );
    }
}

package pl.edu.pwr.database.administrativedivisionofpoland.builders.data.fetchers;

import pl.edu.pwr.contract.Dtos.VoivodeshipAddressData;
import pl.edu.pwr.contract.Dtos.VoivodeshipDto;
import pl.edu.pwr.contract.Dtos.VoivodeshipExtended;
import pl.edu.pwr.contract.History.VoivodeshipHistoryDto;
import pl.edu.pwr.database.administrativedivisionofpoland.data.fetchers.VoivodeshipDataFetcher;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.api.*;

public class VoivodeshipDataFetcherBuilder {
    private Gettable<VoivodeshipDto> voivodeshipGetter;
    private GettableExtended<VoivodeshipExtended> voivodeshipExtendedGetter;
    private GettableAddress<VoivodeshipAddressData> voivodeshipAddressGetter;
    private Trackable<VoivodeshipHistoryDto> voivodeshipHistorian;
    private TerytProvider voivodeshipTerytProvider;

    public void setVoivodeshipGetter(Gettable<VoivodeshipDto> voivodeshipGetter) {
        this.voivodeshipGetter = voivodeshipGetter;
    }

    public void setVoivodeshipExtendedGetter(GettableExtended<VoivodeshipExtended> voivodeshipExtendedGetter) {
        this.voivodeshipExtendedGetter = voivodeshipExtendedGetter;
    }

    public void setVoivodeshipAddressGetter(GettableAddress<VoivodeshipAddressData> voivodeshipAddressGetter) {
        this.voivodeshipAddressGetter = voivodeshipAddressGetter;
    }

    public void setVoivodeshipHistorian(Trackable<VoivodeshipHistoryDto> voivodeshipHistorian) {
        this.voivodeshipHistorian = voivodeshipHistorian;
    }

    public void setVoivodeshipTerytProvider(TerytProvider voivodeshipTerytProvider) {
        this.voivodeshipTerytProvider = voivodeshipTerytProvider;
    }

    public VoivodeshipDataFetcher getResult() {
        return new VoivodeshipDataFetcher(
                voivodeshipGetter,
                voivodeshipExtendedGetter,
                voivodeshipAddressGetter,
                voivodeshipHistorian,
                voivodeshipTerytProvider
        );
    }
}

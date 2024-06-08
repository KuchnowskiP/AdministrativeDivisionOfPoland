package pl.edu.pwr.database.administrativedivisionofpoland.data.fetchers;

import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.VoivodeshipAddressData;
import pl.edu.pwr.contract.Dtos.VoivodeshipDto;
import pl.edu.pwr.contract.Dtos.VoivodeshipExtended;
import pl.edu.pwr.contract.History.VoivodeshipHistoryDto;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.api.*;

import java.io.IOException;

public class VoivodeshipDataFetcher implements IVoivodeshipDataFetcher {
    private final Gettable<VoivodeshipDto> voivodeshipGetter;
    private final GettableExtended<VoivodeshipExtended> voivodeshipExtendedGetter;
    private final GettableAddress<VoivodeshipAddressData> voivodeshipAddressGetter;
    private final Trackable<VoivodeshipHistoryDto> voivodeshipHistorian;
    private final TerytProvider voivodeshipTerytProvider;

    public VoivodeshipDataFetcher(Gettable<VoivodeshipDto> voivodeshipGetter, GettableExtended<VoivodeshipExtended> voivodeshipExtendedGetter, GettableAddress<VoivodeshipAddressData> voivodeshipAddressGetter, Trackable<VoivodeshipHistoryDto> voivodeshipHistorian, TerytProvider voivodeshipTerytProvider) {
        this.voivodeshipGetter = voivodeshipGetter;
        this.voivodeshipExtendedGetter = voivodeshipExtendedGetter;
        this.voivodeshipAddressGetter = voivodeshipAddressGetter;
        this.voivodeshipHistorian = voivodeshipHistorian;
        this.voivodeshipTerytProvider = voivodeshipTerytProvider;
    }

    @Override
    public PageResult<VoivodeshipDto> getVoivodeships(int page, int pageSize) {
        try {
            return voivodeshipGetter.get(null, page, pageSize);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageResult<VoivodeshipExtended> getVoivodeshipsExtended(int page, int pageSize) {
        try {
            return voivodeshipExtendedGetter.getExtended(null, page, pageSize);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageResult<VoivodeshipHistoryDto> getVoivodeshipHistory(int page, int pageSize) {
        try {
            return voivodeshipHistorian.getHistory(page, pageSize);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageResult<VoivodeshipAddressData> getVoivodeshipAddresses(int page, int pageSize) {
        try {
            return voivodeshipAddressGetter.getAddresses(null, page, pageSize);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getNewVoivodeshipTeryt(String[] args) {
        try {
            return voivodeshipTerytProvider.getNewTeryt(args);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

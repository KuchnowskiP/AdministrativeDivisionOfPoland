package pl.edu.pwr.database.administrativedivisionofpoland.data.fetchers;

import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.CommuneAddressData;
import pl.edu.pwr.contract.Dtos.CommuneDto;
import pl.edu.pwr.contract.History.CommuneHistoryDto;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.api.*;

import java.io.IOException;

public class CommuneDataFetcher implements ICommuneDataFetcher {
    private final Gettable<CommuneDto> communeGetter;
    private final GettableAddress<CommuneAddressData> communeAddressGetter;
    private final GettableById<CommuneDto> communeByIdGetter;
    private final GettableByGrandparentUnit<CommuneDto> communeByGPGetter;
    private final Trackable<CommuneHistoryDto> communeHistorian;
    private final TerytProvider communeTerytProvider;

    public CommuneDataFetcher(
            Gettable<CommuneDto> communeGetter,
            GettableAddress<CommuneAddressData> communeAddressGetter,
            GettableById<CommuneDto> communeByIdGetter,
            GettableByGrandparentUnit<CommuneDto> communeByGPGetter,
            Trackable<CommuneHistoryDto> communeHistorian,
            TerytProvider communeTerytProvider
    ) {
        this.communeGetter = communeGetter;
        this.communeByIdGetter = communeByIdGetter;
        this.communeByGPGetter = communeByGPGetter;
        this.communeAddressGetter = communeAddressGetter;
        this.communeHistorian = communeHistorian;
        this.communeTerytProvider = communeTerytProvider;
    }

    @Override
    public PageResult<CommuneDto> getCommunes(int unitId, int page, int pageSize) {
        try {
            return communeGetter.get(unitId, page, pageSize);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageResult<CommuneAddressData> getCommuneAddresses(int unitId, int page, int pageSize) {
        try {
            return communeAddressGetter.getAddresses(unitId, page, pageSize);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageResult<CommuneHistoryDto> getCommuneHistory(int page, int pageSize) {
        try {
            return communeHistorian.getHistory(page, pageSize);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getNewCommuneTeryt(int id, int type) {
        try {
            return communeTerytProvider.getNewTeryt(new String[]{String.valueOf(id), String.valueOf(type)});
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageResult<CommuneDto> getCommunesByVoivodeshipId(int voivodeshipId, int page, int pageSize) {
        try {
            return communeByGPGetter.getByGrandparentUnitId(voivodeshipId, page, pageSize);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CommuneDto getCommuneById(int unitId) {
        try {
            return communeByIdGetter.getById(unitId);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

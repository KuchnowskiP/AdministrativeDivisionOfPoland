package pl.edu.pwr.database.administrativedivisionofpoland.builders.data.fetchers;

import pl.edu.pwr.contract.Dtos.*;
import pl.edu.pwr.contract.History.CommuneHistoryDto;
import pl.edu.pwr.contract.History.CountyHistoryDto;
import pl.edu.pwr.contract.History.VoivodeshipHistoryDto;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.CommuneService;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.CountyService;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.VoivodeshipService;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.api.*;

public class DataFetcherDirector {
    private final Gettable<VoivodeshipDto> voivodeshipGetter;
    private final GettableExtended<VoivodeshipExtended> voivodeshipExtendedGetter;
    private final GettableAddress<VoivodeshipAddressData> voivodeshipAddressGetter;
    private final Trackable<VoivodeshipHistoryDto> voivodeshipHistorian;
    private final TerytProvider voivodeshipTerytProvider;
    private final Gettable<CountyDto> countyGetter;
    private final GettableExtended<CountyExtended> countyExtendedGetter;
    private final GettableAddress<CountyAddressData> countyAddressGetter;
    private final GettableById<CountyDto> countyByIdGetter;
    private final Trackable<CountyHistoryDto> countyHistorian;
    private final TerytProvider countyTerytProvider;
    private final Gettable<CommuneDto> communeGetter;
    private final GettableAddress<CommuneAddressData> communeAddressGetter;
    private final GettableById<CommuneDto> communeByIdGetter;
    private final GettableByGrandparentUnit<CommuneDto> communeByGPGetter;
    private final Trackable<CommuneHistoryDto> communeHistorian;
    private final TerytProvider communeTerytProvider;
    private final Gettable<OfficeAddressDto> addressGetter;
    private final Gettable<ReportDto> reportGetter;

    public DataFetcherDirector(
            VoivodeshipService voivodeshipService,
            CountyService countyService,
            CommuneService communeService, Gettable<OfficeAddressDto> addressGetter, Gettable<ReportDto> reportGetter
    ){
        this.voivodeshipGetter = voivodeshipService;
        this.voivodeshipExtendedGetter = voivodeshipService;
        this.voivodeshipAddressGetter = voivodeshipService;
        this.voivodeshipHistorian = voivodeshipService;
        this.voivodeshipTerytProvider = voivodeshipService;
        this.countyGetter = countyService;
        this.countyExtendedGetter = countyService;
        this.countyAddressGetter = countyService;
        this.countyByIdGetter = countyService;
        this.countyHistorian = countyService;
        this.countyTerytProvider = countyService;
        this.communeGetter = communeService;
        this.communeAddressGetter = communeService;
        this.communeByIdGetter = communeService;
        this.communeByGPGetter = communeService;
        this.communeHistorian = communeService;
        this.communeTerytProvider = communeService;
        this.addressGetter = addressGetter;
        this.reportGetter = reportGetter;
    }

    public void constructVoivodeshipDataFetcher(VoivodeshipDataFetcherBuilder builder){
        builder.setVoivodeshipGetter(voivodeshipGetter);
        builder.setVoivodeshipExtendedGetter(voivodeshipExtendedGetter);
        builder.setVoivodeshipAddressGetter(voivodeshipAddressGetter);
        builder.setVoivodeshipHistorian(voivodeshipHistorian);
        builder.setVoivodeshipTerytProvider(voivodeshipTerytProvider);
    }

    public void constructCountyDataFetcher(CountyDataFetcherBuilder builder){
        builder.setCountyGetter(countyGetter);
        builder.setCountyExtendedGetter(countyExtendedGetter);
        builder.setCountyAddressGetter(countyAddressGetter);
        builder.setCountyByIdGetter(countyByIdGetter);
        builder.setCountyHistorian(countyHistorian);
        builder.setCountyTerytProvider(countyTerytProvider);
    }

    public void constructCommuneDataFetcher(CommuneDataFetcherBuilder builder){
        builder.setCommuneGetter(communeGetter);
        builder.setCommuneAddressGetter(communeAddressGetter);
        builder.setCommuneByIdGetter(communeByIdGetter);
        builder.setCommuneByGPGetter(communeByGPGetter);
        builder.setCommuneHistorian(communeHistorian);
        builder.setCommuneTerytProvider(communeTerytProvider);
    }

    public void constructReportDataFetcher(ReportDataFetcherBuilder builder){
        builder.setReportGetter(reportGetter);
    }

    public void constructOfficeAddressDataFetcher(AddressDataFetcherBuilder builder){
        builder.setAddressGetter(addressGetter);
    }
    
}

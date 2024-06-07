package pl.edu.pwr.database.administrativedivisionofpoland.data;

import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.*;
import pl.edu.pwr.contract.History.CommuneHistoryDto;
import pl.edu.pwr.contract.History.CountyHistoryDto;
import pl.edu.pwr.contract.History.VoivodeshipHistoryDto;
import pl.edu.pwr.database.administrativedivisionofpoland.data.api.IResultFetcher;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.api.*;

import java.io.IOException;


public class ResultFetcher implements IResultFetcher {
    Gettable<VoivodeshipDto> voivodeshipGetter;
    GettableExtended<VoivodeshipExtended> voivodeshipExtendedGetter;
    GettableAddress<VoivodeshipAddressData> voivodeshipAddressGetter;
    Trackable<VoivodeshipHistoryDto> voivodeshipHistorian;
    TerytProvider voivodeshipTerytProvider;

    GettableExtended<CountyExtended> countyExtendedGetter;
    GettableAddress<CountyAddressData> countyAddressGetter;
    Trackable<CountyHistoryDto> countyHistorian;
    TerytProvider countyTerytProvider;
    GettableById<CountyDto> countyByIdGetter;

    Gettable<CommuneDto> communeGetter;
    GettableAddress<CommuneAddressData> communeAddressGetter;
    Trackable<CommuneHistoryDto> communeHistorian;
    TerytProvider communeTerytProvider;
    GettableById<CommuneDto> communeByIdGetter;
    GettableByGrandparentUnit<CommuneDto> communeByGPGetter;

    Gettable<ReportDto> reportGetter;
    Gettable<OfficeAddressDto> addressGetter;

    public ResultFetcher(
            Gettable<VoivodeshipDto> voivodeshipGetter,
            GettableExtended<VoivodeshipExtended> voivodeshipExtendedGetter,
            GettableAddress<VoivodeshipAddressData> voivodeshipAddressGetter,
            Trackable<VoivodeshipHistoryDto> voivodeshipHistorian,
            TerytProvider voivodeshipTerytProvider,
            GettableExtended<CountyExtended> countyExtendedGetter,
            GettableAddress<CountyAddressData> countyAddressGetter,
            Trackable<CountyHistoryDto> countyHistorian,
            TerytProvider countyTerytProvider,
            GettableById<CountyDto> countyByIdGetter,
            Gettable<CommuneDto> communeGetter,
            GettableAddress<CommuneAddressData> communeAddressGetter,
            Trackable<CommuneHistoryDto> communeHistorian,
            TerytProvider communeTerytProvider,
            GettableById<CommuneDto> communeByIdGetter,
            GettableByGrandparentUnit<CommuneDto> communeByGPGetter,
            Gettable<ReportDto> reportGetter,
            Gettable<OfficeAddressDto> addressGetter
    ) {
        this.voivodeshipGetter = voivodeshipGetter;
        this.voivodeshipExtendedGetter = voivodeshipExtendedGetter;
        this.voivodeshipAddressGetter = voivodeshipAddressGetter;
        this.voivodeshipHistorian = voivodeshipHistorian;
        this.voivodeshipTerytProvider = voivodeshipTerytProvider;
        this.countyExtendedGetter = countyExtendedGetter;
        this.countyAddressGetter = countyAddressGetter;
        this.countyHistorian = countyHistorian;
        this.countyTerytProvider = countyTerytProvider;
        this.countyByIdGetter = countyByIdGetter;
        this.communeGetter = communeGetter;
        this.communeAddressGetter = communeAddressGetter;
        this.communeHistorian = communeHistorian;
        this.communeTerytProvider = communeTerytProvider;
        this.communeByIdGetter = communeByIdGetter;
        this.communeByGPGetter = communeByGPGetter;
        this.reportGetter = reportGetter;
        this.addressGetter = addressGetter;
    }

    @Override
    public PageResult<VoivodeshipDto> getVoivodeships(int page, int size) throws IOException, InterruptedException {
        return voivodeshipGetter.get(null, 1, Integer.MAX_VALUE);
    }

    @Override
    public PageResult<?> getResult(int table, int treeIndex, Object unit, int addressesAreChecked) throws Exception {
        PageResult<?> requestResult = null;
        switch (table){
            case 0:{
                switch (treeIndex){
                    case 1:{
                        if(addressesAreChecked == 4){
                            requestResult = countyAddressGetter.getAddresses(unit, 1, Integer.MAX_VALUE);
                        }else {
                            requestResult = countyExtendedGetter.getExtended(unit, 1, Integer.MAX_VALUE);
                        }
                        break;
                    }
                    case 2:{
                        if(addressesAreChecked == 4){
                            requestResult = communeAddressGetter.getAddresses(unit, 1, Integer.MAX_VALUE);
                        }else {
                            requestResult = communeGetter.get(unit, 1, Integer.MAX_VALUE);
                        }
                        break;
                    }
                    default:{
                        if(addressesAreChecked == 4){
                            requestResult = voivodeshipAddressGetter.getAddresses(null, 1, Integer.MAX_VALUE);
                        }else {
                            requestResult = voivodeshipExtendedGetter.getExtended(null, 1, Integer.MAX_VALUE);
                        }
                        break;
                    }
                }
                break;
            }
            case 1:{

                if(treeIndex == 1){
                    if(addressesAreChecked == 4){
                        requestResult = communeAddressGetter.getAddresses(unit, 1, Integer.MAX_VALUE);
                    }else {
                        requestResult = communeGetter.get(unit, 1, Integer.MAX_VALUE);
                    }
                }
                else{
                    if(addressesAreChecked == 4){
                        requestResult = countyAddressGetter.getAddresses(unit, 1, Integer.MAX_VALUE);
                    }else {
                        requestResult = countyExtendedGetter.getExtended(unit, 1, Integer.MAX_VALUE);
                    }
                }
                break;
            }
            case 2:{
                if(addressesAreChecked == 4){
                    requestResult = communeAddressGetter.getAddresses(unit, 1, Integer.MAX_VALUE);
                }else {
                    requestResult = communeGetter.get(unit, 1, Integer.MAX_VALUE);
                }
                break;
            }
            case 3:{
                requestResult = reportGetter.get(null, 1, Integer.MAX_VALUE);
                break;
            }
        }
        return requestResult;
    }

    @Override
    public PageResult<?> getHistoryResult(int table) throws IOException, InterruptedException {
        PageResult<?> requestResult = null;
        switch(table){
            case 0: {
                requestResult = voivodeshipHistorian.getHistory(1, Integer.MAX_VALUE);
                break;
            }
            case 1: {
                requestResult = countyHistorian.getHistory(1, Integer.MAX_VALUE);
                break;
            }
            case 2: {
                requestResult = communeHistorian.getHistory(1, Integer.MAX_VALUE);
                break;
            }
        }
        return requestResult;
    }

    public PageResult<OfficeAddressDto> getAddresses(int page, int size) throws IOException, InterruptedException {
        return addressGetter.get(null, page, size);
    }
    public CountyDto getCountyById(int ID) throws IOException, InterruptedException {
        return countyByIdGetter.getById(ID);
    }
    public CommuneDto getCommuneById(int ID) throws IOException, InterruptedException {
        return communeByIdGetter.getById(ID);
    }
    public PageResult<CommuneDto> communeByVoivodeshipId(Object ID, int page, int size) throws IOException, InterruptedException {
        return communeByGPGetter.getByGrandparentUnitId(ID, page, size);
    }
    public String newVoivodeshipTeryt() throws IOException, InterruptedException {
        return voivodeshipTerytProvider.getNewTeryt(null);
    }

    public String newCountyTeryt(Integer id, int city) throws IOException, InterruptedException {
        return countyTerytProvider.getNewTeryt(new String[]{id.toString(), String.valueOf(city)});
    }

    public String newCommuneTeryt(Integer id, int type) throws IOException, InterruptedException {
        return communeTerytProvider.getNewTeryt(new String[]{id.toString(), String.valueOf(type)});
    }
}

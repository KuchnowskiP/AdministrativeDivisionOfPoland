package pl.edu.pwr.database.administrativedivisionofpoland.data;

import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.*;
import pl.edu.pwr.database.administrativedivisionofpoland.data.fetchers.*;

import java.io.IOException;


public class ResultReceiver implements IResultReceiver {
    private final IVoivodeshipDataFetcher voivodeshipDataFetcher;
    private final ICountyDataFetcher countyDataFetcher;
    private final ICommuneDataFetcher communeDataFetcher;
    private final IReportDataFetcher reportDataFetcher;
    private final IAddressDataFetcher addressDataFetcher;

    public ResultReceiver(
            IVoivodeshipDataFetcher voivodeshipDataFetcher,
            ICountyDataFetcher countyDataFetcher,
            ICommuneDataFetcher communeDataFetcher,
            IReportDataFetcher reportDataFetcher,
            IAddressDataFetcher addressDataFetcher) {
        this.voivodeshipDataFetcher = voivodeshipDataFetcher;
        this.countyDataFetcher = countyDataFetcher;
        this.communeDataFetcher = communeDataFetcher;
        this.reportDataFetcher = reportDataFetcher;
        this.addressDataFetcher = addressDataFetcher;
    }


    @Override
    public PageResult<VoivodeshipDto> getVoivodeships(int page, int size) throws IOException, InterruptedException {
        return voivodeshipDataFetcher.getVoivodeships(page, size);
    }

    @Override
    public PageResult<?> getResult(int table, int treeIndex, int unit, int addressesAreChecked) throws Exception {
        PageResult<?> requestResult = null;
        switch (table){
            case 0:{
                switch (treeIndex){
                    case 1:{
                        if(addressesAreChecked == 4){
                            requestResult = countyDataFetcher.getCountyAddresses(unit, 1, Integer.MAX_VALUE);
                        }else {
                            requestResult = countyDataFetcher.getCountiesExtended(unit, 1, Integer.MAX_VALUE);
                        }
                        break;
                    }
                    case 2:{
                        if(addressesAreChecked == 4){
                            requestResult = communeDataFetcher.getCommuneAddresses(unit, 1, Integer.MAX_VALUE);
                        }else {
                            requestResult = communeDataFetcher.getCommunes(unit, 1, Integer.MAX_VALUE);
                        }
                        break;
                    }
                    default:{
                        if(addressesAreChecked == 4){
                            requestResult = voivodeshipDataFetcher.getVoivodeshipAddresses(1, Integer.MAX_VALUE);
                        }else {
                            requestResult = voivodeshipDataFetcher.getVoivodeshipsExtended(1, Integer.MAX_VALUE);
                        }
                        break;
                    }
                }
                break;
            }
            case 1:{

                if(treeIndex == 1){
                    if(addressesAreChecked == 4){
                        requestResult = communeDataFetcher.getCommuneAddresses(unit, 1, Integer.MAX_VALUE);
                    }else {
                        requestResult = communeDataFetcher.getCommunes(unit, 1, Integer.MAX_VALUE);
                    }
                }
                else{
                    if(addressesAreChecked == 4){
                        requestResult = countyDataFetcher.getCountyAddresses(unit, 1, Integer.MAX_VALUE);
                    }else {
                        requestResult = countyDataFetcher.getCountiesExtended(unit, 1, Integer.MAX_VALUE);
                    }
                }
                break;
            }
            case 2:{
                if(addressesAreChecked == 4){
                    requestResult = communeDataFetcher.getCommuneAddresses(unit, 1, Integer.MAX_VALUE);
                }else {
                    requestResult = communeDataFetcher.getCommunes(unit, 1, Integer.MAX_VALUE);
                }
                break;
            }
            case 3:{
                requestResult = reportDataFetcher.getReports(1, Integer.MAX_VALUE);
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
                requestResult = voivodeshipDataFetcher.getVoivodeshipHistory(1, Integer.MAX_VALUE);
                break;
            }
            case 1: {
                requestResult = countyDataFetcher.getCountyHistory(1, Integer.MAX_VALUE);
                break;
            }
            case 2: {
                requestResult = communeDataFetcher.getCommuneHistory(1, Integer.MAX_VALUE);
                break;
            }
        }
        return requestResult;
    }

    public PageResult<OfficeAddressDto> getAddresses(int page, int size) throws IOException, InterruptedException {
        return addressDataFetcher.getAddresses(page, size);
    }
    public CountyDto getCountyById(int unitId) throws IOException, InterruptedException {
        return countyDataFetcher.getCountyById(unitId);
    }
    public CommuneDto getCommuneById(int unitId) throws IOException, InterruptedException {
        return communeDataFetcher.getCommuneById(unitId);
    }
    public PageResult<CommuneDto> communeByVoivodeshipId(int unitId, int page, int size) throws IOException, InterruptedException {
        return communeDataFetcher.getCommunesByVoivodeshipId(unitId, page, size);
    }
    public String newVoivodeshipTeryt() throws IOException, InterruptedException {
        return voivodeshipDataFetcher.getNewVoivodeshipTeryt(new String[]{});
    }

    public String newCountyTeryt(int id, int city) throws IOException, InterruptedException {
        return countyDataFetcher.getNewCountyTeryt(id, city);
    }

    public String newCommuneTeryt(int id, int type) throws IOException, InterruptedException {
        return communeDataFetcher.getNewCommuneTeryt(id, type);
    }
}

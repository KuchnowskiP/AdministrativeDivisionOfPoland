package pl.edu.pwr.database.administrativedivisionofpoland.Data;

import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.CommuneDto;
import pl.edu.pwr.contract.Dtos.CountyDto;
import pl.edu.pwr.contract.Dtos.OfficeAddressDto;
import pl.edu.pwr.database.administrativedivisionofpoland.Data.Services.*;

import java.io.IOException;


public class DataReceiver {
    private final VoivodeshipDataService voivodeshipDataService = new VoivodeshipDataService();
    private final CountyDataService countyDataService = new CountyDataService();
    private final CommuneDataService communeDataService = new CommuneDataService();
    private final AddressDataService addressDataService = new AddressDataService();
    private final ReportDataService reportDataService = new ReportDataService();

    public PageResult<?> getResult(int table, int treeIndex, Object unit, int addressesAreChecked) throws Exception {
        PageResult<?> requestResult = null;
        switch (table){
            case 0:{
                switch (treeIndex){
                    case 1:{
                        if(addressesAreChecked == 4){
                            requestResult = countyDataService.getCountiesWithAddresses(unit,1,Integer.MAX_VALUE);
                        }else {
                            requestResult = countyDataService.get(unit, 1, Integer.MAX_VALUE);
                        }
                        break;
                    }
                    case 2:{
                        if(addressesAreChecked == 4){
                            requestResult = communeDataService.getCommunesWithAddresses(unit,1,Integer.MAX_VALUE);
                        }else {
                            requestResult = communeDataService.get(unit, 1, Integer.MAX_VALUE);
                        }
                        break;
                    }
                    default:{
                        if(addressesAreChecked == 4){
                            requestResult = voivodeshipDataService.getVoivodeshipsWithAddresses(1,Integer.MAX_VALUE);
                        }else {
                            requestResult = voivodeshipDataService.get(null,1, Integer.MAX_VALUE);
                        }
                        break;
                    }
                }
                break;
            }
            case 1:{

                if(treeIndex == 1){
                    if(addressesAreChecked == 4){
                        requestResult = communeDataService.getCommunesWithAddresses(unit,1,Integer.MAX_VALUE);
                    }else {
                        requestResult = communeDataService.get(unit, 1, Integer.MAX_VALUE);
                    }
                }
                else{
                    if(addressesAreChecked == 4){
                        requestResult = countyDataService.getCountiesWithAddresses(unit,1,Integer.MAX_VALUE);
                    }else {
                        requestResult = countyDataService.get(unit, 1, Integer.MAX_VALUE);
                    }
                }
                break;
            }
            case 2:{
                if(addressesAreChecked == 4){
                    requestResult = communeDataService.getCommunesWithAddresses(unit,1,Integer.MAX_VALUE);
                }else {
                    requestResult = communeDataService.get(unit, 1, Integer.MAX_VALUE);
                }
                break;
            }
            case 3:{
                requestResult = reportDataService.getReports(1,Integer.MAX_VALUE);
                break;
            }
        }
        return requestResult;
    }
    public PageResult<?> getHistoryResult(int table) throws IOException, InterruptedException {
        PageResult<?> requestResult = null;
        switch(table){
            case 0: {
                requestResult = voivodeshipDataService.getVoivodeshipsHistory(1,Integer.MAX_VALUE);
                break;
            }
            case 1: {
                requestResult = countyDataService.getCountiesHistory(1,Integer.MAX_VALUE);
                break;
            }
            case 2: {
                requestResult = communeDataService.getCommunesHistory(1,Integer.MAX_VALUE);
                break;
            }
        }
        return requestResult;
    }

    public PageResult<OfficeAddressDto> getAddresses(int page, int size) throws IOException, InterruptedException {
        return addressDataService.getAllAddresses(page,size);
    }
    public CountyDto getCountyById(int ID) throws IOException, InterruptedException {
        return countyDataService.countyById(ID);
    }
    public CommuneDto getCommuneById(int ID) throws IOException, InterruptedException {
        return communeDataService.communeById(ID);
    }
    public String newVoivodeshipTeryt() throws IOException, InterruptedException {
        return voivodeshipDataService.getNewVoivodeshipTeryt();
    }

    public String newCountyTeryt(Integer id, int city) throws IOException, InterruptedException {
        return countyDataService.getNewCountyTeryt(id, city);
    }

    public String newCommuneTeryt(Integer id, int type) throws IOException, InterruptedException {
        return communeDataService.getNewCommuneTeryt(id, type);
    }
}

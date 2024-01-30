package pl.edu.pwr.database.administrativedivisionofpoland.Services.Data;

import javafx.scene.chart.PieChart;
import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.CommuneDto;
import pl.edu.pwr.contract.Dtos.CountyDto;
import pl.edu.pwr.contract.Dtos.OfficeAddressDto;

import java.io.IOException;


public class DataReceiver extends DataService {
    VoivodeshipDataService voivodeshipDataService = new VoivodeshipDataService();
    CountyDataService countyDataService = new CountyDataService();
    CommuneDataService communeDataService = new CommuneDataService();
    public PageResult<?> getResult(int table, int treeIndex, Object unit, int addressesAreChecked) throws Exception {
        PageResult<?> requestResult = null;
        switch (table){
            case 0:{
                switch (treeIndex){
                    case 1:{
                        if(addressesAreChecked == 4){
                            requestResult = DataService.getCountiesWithAddresses(unit,1,Integer.MAX_VALUE);
                        }else {
                            requestResult = countyDataService.get(unit, 1, Integer.MAX_VALUE);
                        }
                        break;
                    }
                    case 2:{
                        if(addressesAreChecked == 4){
                            requestResult = DataService.getCommunesWithAddresses(unit,1,Integer.MAX_VALUE);
                        }else {
                            requestResult = communeDataService.get(unit, 1, Integer.MAX_VALUE);
                        }
                        break;
                    }
                    default:{
                        if(addressesAreChecked == 4){
                            requestResult = DataService.getVoivodeshipsWithAddresses(1,Integer.MAX_VALUE);
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
                        requestResult = DataService.getCommunesWithAddresses(unit,1,Integer.MAX_VALUE);
                    }else {
                        requestResult = communeDataService.get(unit, 1, Integer.MAX_VALUE);
                    }
                }
                else{
                    if(addressesAreChecked == 4){
                        requestResult = DataService.getCountiesWithAddresses(unit,1,Integer.MAX_VALUE);
                    }else {
                        requestResult = countyDataService.get(unit, 1, Integer.MAX_VALUE);
                    }
                }
                break;
            }
            case 2:{
                if(addressesAreChecked == 4){
                    requestResult = DataService.getCommunesWithAddresses(unit,1,Integer.MAX_VALUE);
                }else {
                    requestResult = communeDataService.get(unit, 1, Integer.MAX_VALUE);
                }
                break;
            }
            case 3:{
                requestResult = DataService.getReports(1,Integer.MAX_VALUE);
                break;
            }
        }
        return requestResult;
    }
    public PageResult<?> getHistoryResult(int table) throws IOException, InterruptedException {
        PageResult<?> requestResult = null;
        switch(table){
            case 0: {
                requestResult = DataService.getVoivodeshipsHistory(1,Integer.MAX_VALUE);
                break;
            }
            case 1: {
                requestResult = DataService.getCountiesHistory(1,Integer.MAX_VALUE);
                break;
            }
            case 2: {
                requestResult = DataService.getCommunesHistory(1,Integer.MAX_VALUE);
                break;
            }
        }
        return requestResult;
    }

    public PageResult<OfficeAddressDto> getAddresses(int page, int size) throws IOException, InterruptedException {
        return DataService.getAllAddresses(page,size);
    }
    public CountyDto getCountyById(int ID) throws IOException, InterruptedException {
        return DataService.countyById(ID);
    }
    public CommuneDto getCommuneById(int ID) throws IOException, InterruptedException {
        return DataService.communeById(ID);
    }
}

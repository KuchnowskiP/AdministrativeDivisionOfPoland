package pl.edu.pwr.database.administrativedivisionofpoland;

import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.OfficeAddressDto;

import java.io.IOException;


public class RequestResultsReceiver extends Request {
    public PageResult<?> getResult(int table, int treeIndex, Object unit, int addressesAreChecked) throws Exception {
        PageResult<?> requestResult = null;
        switch (table){
            case 0:{
                switch (treeIndex){
                    case 1:{
                        if(addressesAreChecked == 4){
                            requestResult = Request.getCountiesWithAddresses(unit,1,Integer.MAX_VALUE);
                        }else {
                            requestResult = Request.getCounties(unit, 1, Integer.MAX_VALUE);
                        }
                        break;
                    }
                    case 2:{
                        if(addressesAreChecked == 4){
                            requestResult = Request.getCommunesWithAddresses(unit,1,Integer.MAX_VALUE);
                        }else {
                            requestResult = Request.getCommunes(unit, 1, Integer.MAX_VALUE);
                        }
                        break;
                    }
                    default:{
                        if(addressesAreChecked == 4){
                            requestResult = Request.getVoivodeshipsWithAddresses(1,Integer.MAX_VALUE);
                        }else {
                            requestResult = Request.getVoivodeships(1, Integer.MAX_VALUE);
                        }
                        break;
                    }
                }
                break;
            }
            case 1:{

                if(treeIndex == 1){
                    if(addressesAreChecked == 4){
                        requestResult = Request.getCommunesWithAddresses(unit,1,Integer.MAX_VALUE);
                    }else {
                        requestResult = Request.getCommunes(unit, 1, Integer.MAX_VALUE);
                    }
                }
                else{
                    if(addressesAreChecked == 4){
                        requestResult = Request.getCountiesWithAddresses(unit,1,Integer.MAX_VALUE);
                    }else {
                        requestResult = Request.getCounties(unit, 1, Integer.MAX_VALUE);
                    }
                }
                break;
            }
            case 2:{
                if(addressesAreChecked == 4){
                    requestResult = Request.getCommunesWithAddresses(unit,1,Integer.MAX_VALUE);
                }else {
                    requestResult = Request.getCommunes(unit, 1, Integer.MAX_VALUE);
                }
                break;
            }
            case 3:{
                requestResult = Request.getReports(1,Integer.MAX_VALUE);
                break;
            }
        }
        return requestResult;
    }
    public PageResult<?> getHistoryResult(int table) throws IOException, InterruptedException {
        PageResult<?> requestResult = null;
        switch(table){
            case 0: {
                requestResult = Request.getVoivodeshipsHistory(1,Integer.MAX_VALUE);
                break;
            }
            case 1: {
                requestResult = Request.getCountiesHistory(1,Integer.MAX_VALUE);
                break;
            }
            case 2: {
                requestResult = Request.getCommunesHistory(1,Integer.MAX_VALUE);
                break;
            }
        }
        return requestResult;
    }

    public PageResult<OfficeAddressDto> getAddresses(int page, int size) throws IOException, InterruptedException {
        return Request.getAllAddresses(page,size);
    }
}

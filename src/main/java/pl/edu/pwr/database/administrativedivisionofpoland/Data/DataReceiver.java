package pl.edu.pwr.database.administrativedivisionofpoland.Data;

import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.CommuneDto;
import pl.edu.pwr.contract.Dtos.CountyDto;
import pl.edu.pwr.contract.Dtos.OfficeAddressDto;
import pl.edu.pwr.contract.Dtos.VoivodeshipDto;
import pl.edu.pwr.database.administrativedivisionofpoland.Data.Services.*;

import java.io.IOException;


public class DataReceiver {
    private final VoivodeshipService voivodeshipDataService = new VoivodeshipService();
    private final CountyService countyService = new CountyService();
    private final CommuneService communeService = new CommuneService();
    private final AddressService addressService = new AddressService();
    private final ReportService reportService = new ReportService();

    public PageResult<?> getResult(int table, int treeIndex, Object unit, int addressesAreChecked) throws Exception {
        PageResult<?> requestResult = null;
        switch (table){
            case 0:{
                switch (treeIndex){
                    case 1:{
                        if(addressesAreChecked == 4){
                            requestResult = countyService.getCountiesWithAddresses(unit,1,Integer.MAX_VALUE);
                        }else {
                            requestResult = countyService.get(unit, 1, Integer.MAX_VALUE);
                        }
                        break;
                    }
                    case 2:{
                        if(addressesAreChecked == 4){
                            requestResult = communeService.getCommunesWithAddresses(unit,1,Integer.MAX_VALUE);
                        }else {
                            requestResult = communeService.get(unit, 1, Integer.MAX_VALUE);
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
                        requestResult = communeService.getCommunesWithAddresses(unit,1,Integer.MAX_VALUE);
                    }else {
                        requestResult = communeService.get(unit, 1, Integer.MAX_VALUE);
                    }
                }
                else{
                    if(addressesAreChecked == 4){
                        requestResult = countyService.getCountiesWithAddresses(unit,1,Integer.MAX_VALUE);
                    }else {
                        requestResult = countyService.get(unit, 1, Integer.MAX_VALUE);
                    }
                }
                break;
            }
            case 2:{
                if(addressesAreChecked == 4){
                    requestResult = communeService.getCommunesWithAddresses(unit,1,Integer.MAX_VALUE);
                }else {
                    requestResult = communeService.get(unit, 1, Integer.MAX_VALUE);
                }
                break;
            }
            case 3:{
                requestResult = reportService.getReports(1,Integer.MAX_VALUE);
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
                requestResult = countyService.getCountiesHistory(1,Integer.MAX_VALUE);
                break;
            }
            case 2: {
                requestResult = communeService.getCommunesHistory(1,Integer.MAX_VALUE);
                break;
            }
        }
        return requestResult;
    }

    public PageResult<OfficeAddressDto> getAddresses(int page, int size) throws IOException, InterruptedException {
        return addressService.getAllAddresses(page,size);
    }
    public CountyDto getCountyById(int ID) throws IOException, InterruptedException {
        return countyService.countyById(ID);
    }
    public CommuneDto getCommuneById(int ID) throws IOException, InterruptedException {
        return communeService.communeById(ID);
    }
    public PageResult<CountyDto> getCountiesDto(Object ID, int page, int size) throws IOException, InterruptedException {
        return countyService.getDto(ID,page,size);
    }
    public PageResult<VoivodeshipDto> getVoivodeshipsDto(Object ID, int page, int size) throws IOException, InterruptedException {
        return voivodeshipDataService.getDto(ID,page,size);
    }
    public PageResult<CommuneDto> communeByVoivodeshipId(Object ID, int page, int size) throws IOException, InterruptedException {
        return communeService.communeByVoivodeshipId(ID, page, size);
    }
    public String newVoivodeshipTeryt() throws IOException, InterruptedException {
        return voivodeshipDataService.getNewTeryt(null);
    }

    public String newCountyTeryt(Integer id, int city) throws IOException, InterruptedException {
        return countyService.getNewTeryt(new String[]{id.toString(), String.valueOf(city)});
    }

    public String newCommuneTeryt(Integer id, int type) throws IOException, InterruptedException {
        return communeService.getNewTeryt(new String[]{id.toString(), String.valueOf(type)});
    }
}

package pl.edu.pwr.database.administrativedivisionofpoland.data.api;

import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.CommuneDto;
import pl.edu.pwr.contract.Dtos.CountyDto;
import pl.edu.pwr.contract.Dtos.OfficeAddressDto;
import pl.edu.pwr.contract.Dtos.VoivodeshipDto;

import java.io.IOException;

public interface IResultFetcher {
    PageResult<VoivodeshipDto> getVoivodeships(int page, int size) throws IOException, InterruptedException;

    PageResult<?> getResult(int table, int treeIndex, Object unit, int addressesAreChecked) throws Exception;

    PageResult<?> getHistoryResult(int table) throws IOException, InterruptedException;

    PageResult<OfficeAddressDto> getAddresses(int page, int size) throws IOException, InterruptedException;

    CountyDto getCountyById(int ID) throws IOException, InterruptedException;

    CommuneDto getCommuneById(int ID) throws IOException, InterruptedException;

    PageResult<CommuneDto> communeByVoivodeshipId(Object ID, int page, int size) throws IOException, InterruptedException;

    String newVoivodeshipTeryt() throws IOException, InterruptedException;

    String newCountyTeryt(Integer id, int city) throws IOException, InterruptedException;

    String newCommuneTeryt(Integer id, int type) throws IOException, InterruptedException;
}

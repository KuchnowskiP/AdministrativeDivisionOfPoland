package pl.edu.pwr.database.administrativedivisionofpoland.data;

import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.CommuneDto;
import pl.edu.pwr.contract.Dtos.CountyDto;
import pl.edu.pwr.contract.Dtos.OfficeAddressDto;
import pl.edu.pwr.contract.Dtos.VoivodeshipDto;

import java.io.IOException;

public interface IResultReceiver {
    PageResult<VoivodeshipDto> getVoivodeships(int page, int size) throws IOException, InterruptedException;

    PageResult<?> getResult(int table, int treeIndex, int unit, int addressesAreChecked) throws Exception;

    PageResult<?> getHistoryResult(int table) throws IOException, InterruptedException;

    PageResult<OfficeAddressDto> getAddresses(int page, int size) throws IOException, InterruptedException;

    CountyDto getCountyById(int ID) throws IOException, InterruptedException;

    CommuneDto getCommuneById(int ID) throws IOException, InterruptedException;

    PageResult<CommuneDto> communeByVoivodeshipId(int ID, int page, int size) throws IOException, InterruptedException;

    String newVoivodeshipTeryt() throws IOException, InterruptedException;

    String newCountyTeryt(int id, int city) throws IOException, InterruptedException;

    String newCommuneTeryt(int id, int type) throws IOException, InterruptedException;
}

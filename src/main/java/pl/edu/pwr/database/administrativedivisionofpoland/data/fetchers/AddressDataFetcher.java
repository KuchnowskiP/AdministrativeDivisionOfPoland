package pl.edu.pwr.database.administrativedivisionofpoland.data.fetchers;

import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.OfficeAddressDto;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.api.Gettable;

import java.io.IOException;

public class AddressDataFetcher implements IAddressDataFetcher {
    private final Gettable<OfficeAddressDto> addressGetter;

    public AddressDataFetcher(Gettable<OfficeAddressDto> addressGetter) {
        this.addressGetter = addressGetter;
    }

    @Override
    public PageResult<OfficeAddressDto> getAddresses(int page, int pageSize) {
        try {
            return addressGetter.get(null, page, pageSize);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

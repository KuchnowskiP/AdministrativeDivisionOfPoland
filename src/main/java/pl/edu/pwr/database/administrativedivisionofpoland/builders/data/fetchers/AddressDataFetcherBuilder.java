package pl.edu.pwr.database.administrativedivisionofpoland.builders.data.fetchers;

import pl.edu.pwr.contract.Dtos.OfficeAddressDto;
import pl.edu.pwr.database.administrativedivisionofpoland.data.fetchers.AddressDataFetcher;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.api.Gettable;

public class AddressDataFetcherBuilder {
    private Gettable<OfficeAddressDto> addressGetter;

    public void setAddressGetter(Gettable<OfficeAddressDto> addressGetter) {
        this.addressGetter = addressGetter;
    }

    public AddressDataFetcher getResult() {
        return new AddressDataFetcher(addressGetter);
    }
}

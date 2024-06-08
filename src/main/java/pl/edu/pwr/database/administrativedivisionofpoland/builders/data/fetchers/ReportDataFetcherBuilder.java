package pl.edu.pwr.database.administrativedivisionofpoland.builders.data.fetchers;

import pl.edu.pwr.contract.Dtos.ReportDto;
import pl.edu.pwr.database.administrativedivisionofpoland.data.fetchers.ReportDataDataFetcher;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.api.Gettable;

public class ReportDataFetcherBuilder {
    private Gettable<ReportDto> reportGetter;

    public void setReportGetter(Gettable<ReportDto> reportGetter) {
        this.reportGetter = reportGetter;
    }

    public ReportDataDataFetcher getResult() {
        return new ReportDataDataFetcher(reportGetter);
    }
}

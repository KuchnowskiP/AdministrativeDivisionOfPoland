package pl.edu.pwr.database.administrativedivisionofpoland.data.fetchers;

import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.ReportDto;
import pl.edu.pwr.database.administrativedivisionofpoland.data.services.api.Gettable;

public class ReportDataDataFetcher implements IReportDataFetcher {
    private final Gettable<ReportDto> reportGetter;

    public ReportDataDataFetcher(Gettable<ReportDto> reportGetter) {
        this.reportGetter = reportGetter;
    }

    @Override
    public PageResult<ReportDto> getReports(int page, int size) throws Exception {
        return reportGetter.get(null, page, size);
    }
}

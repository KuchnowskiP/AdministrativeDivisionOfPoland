package pl.edu.pwr.database.administrativedivisionofpoland.data.fetchers;

import pl.edu.pwr.contract.Common.PageResult;
import pl.edu.pwr.contract.Dtos.ReportDto;

public interface IReportDataFetcher {
    PageResult<ReportDto> getReports(int page, int size) throws Exception;
}

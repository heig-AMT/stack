package ch.heigvd.amt.stack.ui.web.filter;

import ch.heigvd.amt.stack.application.authentication.AuthenticationFacade;
import ch.heigvd.amt.stack.application.authentication.query.SessionQuery;
import ch.heigvd.amt.stack.application.statistics.StatisticsFacade;
import ch.heigvd.amt.stack.application.statistics.dto.UsageStatisticsDTO;
import ch.heigvd.amt.stack.application.statistics.query.UsageStatisticsQuery;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class ProvideStatisticsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        /* Our dependencies requires us to override this method. */
    }

    @Inject
    private StatisticsFacade statisticsFacade;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;

        UsageStatisticsDTO statistics = statisticsFacade.getUsageStatistics(new UsageStatisticsQuery());
        httpRequest.setAttribute("statistics", statistics);
        httpRequest.getRequestDispatcher("/index.jsp").forward(httpRequest, response);

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}

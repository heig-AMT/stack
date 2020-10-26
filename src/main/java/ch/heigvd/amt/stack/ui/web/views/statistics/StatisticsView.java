package ch.heigvd.amt.stack.ui.web.views.statistics;

import ch.heigvd.amt.stack.application.statistics.StatisticsFacade;
import ch.heigvd.amt.stack.application.statistics.dto.UsageStatisticsDTO;
import ch.heigvd.amt.stack.application.statistics.query.UsageStatisticsQuery;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "StatisticsView", urlPatterns = "/")
public class StatisticsView extends HttpServlet {

    @Inject
    private StatisticsFacade statisticsFacade;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UsageStatisticsDTO statistics = statisticsFacade.getUsageStatistics(new UsageStatisticsQuery());
        req.setAttribute("statistics", statistics);
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}

package ch.heigvd.amt.stack.ui.web.endpoints.ranking;

import ch.heigvd.amt.stack.application.GamificationFacade;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ChangePageEndpoint", urlPatterns = "changePage.do")
public class ChangePageEndpoint extends HttpServlet {

  @Inject
  GamificationFacade gamificationFacade;

  @Override
  protected void doPost(
      HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String newCategory = req.getParameter("category");
    int paramPage;
    System.out.println("Last page : "+gamificationFacade.getLastRankingPage());
    if (req.getParameter("pageBack") != null) {
      paramPage = gamificationFacade.getLastRankingPage() - 1;
    } else {
      paramPage = gamificationFacade.getLastRankingPage() + 1;
    }

    String path = getServletContext().getContextPath() + "/rankings?category=" + newCategory
        + "&page=" + paramPage;
    resp.sendRedirect(path);
  }
}

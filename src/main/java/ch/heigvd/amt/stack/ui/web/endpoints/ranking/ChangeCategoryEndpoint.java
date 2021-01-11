package ch.heigvd.amt.stack.ui.web.endpoints.ranking;

import ch.heigvd.amt.stack.application.GamificationFacade;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="ChangeCategoryEndpoint", urlPatterns = "/changeCategory.do")
public class ChangeCategoryEndpoint extends HttpServlet {
  @Inject
  private GamificationFacade gamificationFacade;

  @Override
  protected void doPost(
      HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String newCategory=req.getParameter("newCategory");
    String path = getServletContext().getContextPath() + "/rankings?category="+newCategory
        +"&page=null&size=null";
    resp.sendRedirect(path);
  }
}

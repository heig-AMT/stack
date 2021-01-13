package ch.heigvd.amt.stack.ui.web.endpoints.ranking;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="ChangeCategoryEndpoint", urlPatterns = "/changeCategory.do")
public class ChangeCategoryEndpoint extends HttpServlet {

  @Override
  protected void doPost(
      HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String newCategory=req.getParameter("newCategory").toLowerCase();
    String path = getServletContext().getContextPath() + "/rankings?category="+newCategory
        +"&page=null";
    resp.sendRedirect(path);
  }
}

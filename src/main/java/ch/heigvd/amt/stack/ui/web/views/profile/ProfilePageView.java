package ch.heigvd.amt.stack.ui.web.views.profile;

import ch.heigvd.amt.stack.application.GamificationFacade;
import ch.heigvd.amt.stack.application.badges.query.BadgeQuery;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ProfilePageView", urlPatterns = "/profile")
public class ProfilePageView extends HttpServlet {

  @Inject
  GamificationFacade gamificationFacade;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    var tag = req.getSession().getId();
    var query = BadgeQuery.builder().tag(tag).build();
    var badges = gamificationFacade.getUserBadges(query);
    req.setAttribute("badges", badges);
    req.getRequestDispatcher("WEB-INF/views/profile.jsp").forward(req, resp);
  }
}

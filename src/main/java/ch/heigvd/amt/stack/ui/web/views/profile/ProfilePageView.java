package ch.heigvd.amt.stack.ui.web.views.profile;

import ch.heigvd.amt.stack.application.BadgeFacade;
import ch.heigvd.amt.stack.application.badges.dto.BadgeListDTO;
import ch.heigvd.amt.stack.application.badges.query.BadgeQuery;
import ch.heigvd.amt.stack.infrastructure.persistence.remote.RemoteGamificationRepository;
import ch.heigvd.gamify.api.dto.Badge;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ProfilePageView", urlPatterns = "/profile")
public class ProfilePageView extends HttpServlet {

    @Inject
    BadgeFacade badgeFacade;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BadgeListDTO badges=badgeFacade.getUserBadges(
            BadgeQuery.builder().username(req.getSession().getId()).build()
        );
        req.setAttribute("badges", badges);
        req.getRequestDispatcher("WEB-INF/views/profile.jsp").forward(req, resp);
    }
}

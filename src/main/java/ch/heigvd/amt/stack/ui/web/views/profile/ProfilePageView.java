package ch.heigvd.amt.stack.ui.web.views.profile;

import ch.heigvd.amt.stack.application.badges.dto.BadgeListDTO;
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


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // We do not alter the page for the moment.
        req.setAttribute("badges", BadgeListDTO.builder()
            .badges(
                List.of(new Badge()
                    .name("badge1").title("badge1"))
            ).build());
        req.getRequestDispatcher("WEB-INF/views/profile.jsp").forward(req, resp);
    }
}

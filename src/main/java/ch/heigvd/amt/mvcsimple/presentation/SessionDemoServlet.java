package ch.heigvd.amt.mvcsimple.presentation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "SessionDemoServlet", urlPatterns = "/sessionDemo")
public class SessionDemoServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    Integer counter = (Integer)req.getSession().getAttribute("counter");
    if (counter == null) {
      counter = 1;
    } else {
      counter += 1;
    }
    req.getSession().setAttribute("counter", counter);

    byte[] heavyModel = new byte[1024 * 1024 * 5];
    req.getSession().setAttribute("heavy", heavyModel);

    PrintWriter out = resp.getWriter();
    out.println("Session id: " + req.getSession().getId());
    out.println("Counter: " + counter);
  }
}

package ch.heigvd.amt.mvcsimple.presentation;

import ch.heigvd.amt.mvcsimple.business.QuoteGenerator;
import ch.heigvd.amt.mvcsimple.model.Quote;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

/**
 * The QuoteServlet is the Controller in the pattern. It receives HTTP requests, decides that the
 * QuoteGenerator service can provide the model (a list of Quote objects). After invoking the
 * service and obtaining the model, it attaches the model to the request (as a parameter named 'quotes').
 * Finally, it finds the view capable of rendering the model (view.jsp) and delegates the end of the work
 * to this component (by calling forwarding the request).
 */
public class QuoteServlet extends javax.servlet.http.HttpServlet {

    private QuoteGenerator service; // we will see later how to replace this with dependency injection

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        service = new QuoteGenerator();
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        List<Quote> model = service.generateQuotes();
        request.setAttribute("quotes", model);
        request.getRequestDispatcher("/WEB-INF/pages/view.jsp").forward(request, response);
    }
}

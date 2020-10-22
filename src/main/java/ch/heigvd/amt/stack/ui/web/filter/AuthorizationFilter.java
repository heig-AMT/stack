package ch.heigvd.amt.stack.ui.web.filter;

import ch.heigvd.amt.stack.application.authentication.dto.ConnectedDTO;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class AuthorizationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        /* Our dependencies requires us to override this method. */
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;
        var httpResponse = (HttpServletResponse) response;

        // We need to "localize the URI in case we have a context path
        var localizedUri = httpRequest.getRequestURI().replace(httpRequest.getContextPath(), "");
        if (isPublicResource(localizedUri)) {
            chain.doFilter(request, response);
            return;
        }

        var connected = (ConnectedDTO) httpRequest.getAttribute("connected");

        if (!connected.isConnected()) {
            redirectToLogin(httpRequest, httpResponse);
            return;
        }

        chain.doFilter(request, response);
    }

    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String initialUrl = request.getRequestURI();
        String query = request.getQueryString();

        initialUrl += query == null ? "" : ("?" + query);

        request.getSession().setAttribute("redirectUrl", initialUrl);
        response.sendRedirect(request.getContextPath() + "/login");
    }

    private boolean isPublicResource(String uri) {
        List<String> allowedResources = List.of
                ("/assets"
                        , "/favicon.ico"
                        , "/login"
                        , "/register"
                        , "/questions"
                        , "/question"
                        , "/error404"
                );

        boolean onHomePage = uri.equals("/") || uri.equals("");

        return allowedResources.stream().anyMatch(uri::startsWith) || onHomePage;
    }
}

package ch.heigvd.amt.stack.ui.web.filter;

import ch.heigvd.amt.stack.application.authentication.AuthenticationFacade;
import ch.heigvd.amt.stack.application.authentication.query.SessionQuery;
import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(filterName = "AuthorizationFilter", urlPatterns = "/*")
public class AuthorizationFilter implements Filter {

    @Inject
    private AuthenticationFacade authenticationFacade;

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

        var isAuthenticatedQuery = SessionQuery.builder()
            .tag(httpRequest.getSession().getId())
            .build();
        var connectedDTO = authenticationFacade.connected(isAuthenticatedQuery);

        if (!connectedDTO.isConnected()) {
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
            ( "/assets"
            , "/favicon.ico"
            , "/login"
            , "/register"
            , "/questions"
            , "/error404"
            );

        boolean onHomePage = uri.equals("/") || uri.equals("");

        return allowedResources.stream().anyMatch(uri::startsWith) || onHomePage;
    }
}

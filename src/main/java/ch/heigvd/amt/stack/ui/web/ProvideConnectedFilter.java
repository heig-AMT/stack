package ch.heigvd.amt.stack.ui.web;

import ch.heigvd.amt.stack.application.ServiceRegistry;
import ch.heigvd.amt.stack.application.authentication.AuthenticationFacade;
import ch.heigvd.amt.stack.application.authentication.query.SessionQuery;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter("/*")
public class ProvideConnectedFilter implements Filter {

    public static final String AUTHENTICATION_CONNECTED = ProvideConnectedFilter.class.getName() + ".AUTHENTICATION_CONNECTED";

    private AuthenticationFacade authenticationFacade;

    @Override
    public void init(FilterConfig filterConfig) {
        this.authenticationFacade = ServiceRegistry.getInstance().getAuthenticationFacade();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        boolean connected = authenticationFacade.connected(SessionQuery.builder().tag(httpRequest.getSession().getId()).build())
                .isConnected();
        httpRequest.getSession().setAttribute(AUTHENTICATION_CONNECTED, connected);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}

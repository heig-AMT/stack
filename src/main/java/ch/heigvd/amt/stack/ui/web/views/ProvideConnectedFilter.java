package ch.heigvd.amt.stack.ui.web.views;

import ch.heigvd.amt.stack.application.ServiceRegistry;
import ch.heigvd.amt.stack.application.authentication.AuthenticationFacade;
import ch.heigvd.amt.stack.application.authentication.query.SessionQuery;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter("/*")
public class ProvideConnectedFilter implements Filter {

    private AuthenticationFacade authenticationFacade;

    @Override
    public void init(FilterConfig filterConfig) {
        this.authenticationFacade = ServiceRegistry.getInstance().getAuthenticationFacade();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;
        var query = SessionQuery.builder()
                .tag(httpRequest.getSession().getId())
                .build();
        httpRequest.setAttribute("connected", authenticationFacade.connected(query));
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}

package ch.heigvd.amt.stack.ui.web.views;

import ch.heigvd.amt.stack.application.AuthenticationFacade;
import ch.heigvd.amt.stack.application.authentication.query.SessionQuery;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class ProvideConnectedFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        /* Our dependencies requires us to override this method. */
    }

    @Inject
    private AuthenticationFacade authenticationFacade;

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

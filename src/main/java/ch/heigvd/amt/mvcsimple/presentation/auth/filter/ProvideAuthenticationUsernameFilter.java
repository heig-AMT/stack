package ch.heigvd.amt.mvcsimple.presentation.auth.filter;

import ch.heigvd.amt.mvcsimple.business.api.SessionRepository;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter("/*")
public class ProvideAuthenticationUsernameFilter implements Filter {

    public static final String AUTHENTICATION_USERNAME = ProvideAuthenticationUsernameFilter.class.getName() + ".AUTHENTICATION_USERNAME";

    @EJB
    private SessionRepository repository;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        httpRequest.getSession().setAttribute(AUTHENTICATION_USERNAME, repository.username(httpRequest.getSession()));
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}

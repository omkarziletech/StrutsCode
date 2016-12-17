package com.gp.cvst.logisoft.web;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

public class AuthorizationFilter implements Filter {

    FilterConfig config = null;
    ServletContext servletContext = null;

    public AuthorizationFilter() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        config = filterConfig;
        servletContext = config.getServletContext();
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();

        //checking whether the user loggied in.If not redirect to Login Page

        if (session.getAttribute("loginuser") == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/jsps/login.jsp");
        } else {
            chain.doFilter(request, response);
        }

    }

    public void destroy() {
    }
}

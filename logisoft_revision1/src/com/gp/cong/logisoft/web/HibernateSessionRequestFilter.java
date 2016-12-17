package com.gp.cong.logisoft.web;

import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.ProcessInfoDAO;
import com.gp.cong.logisoft.jobscheduler.JobScheduler;
import com.logiware.common.dao.OnlineUserDAO;
import java.io.IOException;
import java.util.Date;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.hibernate.StaleObjectStateException;
import org.hibernate.exception.JDBCConnectionException;

public class HibernateSessionRequestFilter extends BaseHibernateDAO implements Filter {

    private static final Logger log = Logger.getLogger(HibernateSessionRequestFilter.class);
    public static ServletContext servletContext;
    public static String basePath = null;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        servletRequest.setCharacterEncoding("UTF-8");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        /*set basepath  */
        if (null == basePath) {
            basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        }
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        try {
            try {
                getCurrentSession().beginTransaction();
            } catch (JDBCConnectionException jdbcce) {
                try {
                    this.initializeSessionFactory();
                    getCurrentSession().beginTransaction();
                } catch (Exception ex) {
                    throw ex;
                }
            }
            OnlineUserDAO onlineUserDAO = new OnlineUserDAO();
            if (loginUser == null
                    && !request.getServletPath().contains("login.jsp") && !request.getServletPath().contains("login.do")) {
                response.sendRedirect(request.getContextPath() + "/jsps/login.jsp");
            } else if (null != request.getServletPath()
                    && !request.getServletPath().contains("login.jsp") && !request.getServletPath().contains("login.do")
                    && null != loginUser && (null != loginUser.getUserId() || null != session.getId())
                    && !onlineUserDAO.isUserAlreadyLoggedOn(loginUser.getUserId(), session.getId())) {
                session.removeAttribute("loginuser");
                response.sendRedirect(request.getContextPath() + "/jsps/login.jsp");
            } else {
                chain.doFilter(servletRequest, servletResponse);
            }
            // Commit and cleanup
            if (getCurrentSession().getTransaction().isActive()) {
                getCurrentSession().getTransaction().commit();
            }
            getCurrentSession().close();
        } catch (StaleObjectStateException staleEx) {
            log.info("INSDIE CATCH ERROR STATE OBJECT STATE EXCEPTION", staleEx);
            // Rollback, close everything, possibly compensate for any permanent changes
            // during the conversation, and finally restart business conversation. Maybe
            // give the user of the application a chance to merge some of his work with
            // fresh data... what you do here depends on your applications design.
            throw staleEx;
        } catch (Throwable ex) {
            String user = "";
            if (null != loginUser) {
                user = loginUser.getLoginName();
            }
            log.info("Exception happened for user - " + user + " at " + new Date(), ex);
            try {
                if (getCurrentSession().getTransaction().isActive()) {
                    getCurrentSession().getTransaction().rollback();
                }
                if (null != loginUser) {
                    getCurrentSession().getTransaction().begin();
                    new ProcessInfoDAO().deleteProcessInfo(loginUser.getUserId(), "LCL FILE");
                    if (getCurrentSession().getTransaction().isActive()) {
                        getCurrentSession().getTransaction().commit();
                    }
                }
            } catch (Throwable rbEx) {
                log.info("Could not rollback transaction after exception! ", rbEx);
            }
            throw new ServletException(ex);
        } finally {
            if (null != getCurrentSession() && getCurrentSession().isOpen()) {
                getCurrentSession().close();
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            this.initializeSessionFactory();
            servletContext = filterConfig.getServletContext();
            JobScheduler.servletContext = filterConfig.getServletContext();
            new OnlineUserDAO().killAllUsers();
        } catch (Exception ex) {
            log.info("Exception happened at " + new Date(), ex);
        }
    }

    @Override
    public void destroy() {
    }
}

package com.logiware.listener;

import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.util.DBUtil;
import com.logiware.common.dao.OnlineUserDAO;
import java.util.Date;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.log4j.Logger;
import org.hibernate.Transaction;

/**
 * UnboundUserSession used to remove all the user related locked records when
 * his session got auto expire.
 *
 * @author Ram
 */
public class UnboundUserSession implements HttpSessionListener {

    private static final Logger log = Logger.getLogger(UnboundUserSession.class);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        final String LOGIN_USER = "loginuser";
        User user = null;
        if (session.getAttribute(LOGIN_USER) != null) {
            try {
                user = (User) session.getAttribute(LOGIN_USER);
                DBUtil dbUtil = new DBUtil();
                dbUtil.killTabProcessInfo(user.getUserId());
            } catch (Exception e) {
                log.info("Destroying session in UnboundUserSession failed on " + new Date(), e);
            }
        }
        if (null != user) {
            OnlineUserDAO onlineUserDAO = new OnlineUserDAO();
            Transaction tx = null;
            try {
                tx = onlineUserDAO.getCurrentSession().getTransaction();
                if(!tx.isActive()){
                    tx.begin();
                }
                onlineUserDAO.delete(user.getUserId());
                tx.commit();
            } catch (Exception e) {
                log.info("Killing user in UnboundUserSession failed on " + new Date(), e);
                if (null != tx && !tx.isActive()) {
                    tx.rollback();
                }
            }
        }
    }

}

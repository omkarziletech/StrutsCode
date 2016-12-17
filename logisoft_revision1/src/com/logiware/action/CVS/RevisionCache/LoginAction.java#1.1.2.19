package com.logiware.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.NotesDAO;
import com.gp.cong.logisoft.hibernate.dao.RoleDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.util.DBUtil;
import com.logiware.accounting.model.NotesModel;
import com.logiware.common.dao.OnlineUserDAO;
import com.logiware.common.domain.OnlineUser;
import com.logiware.form.LoginForm;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 *
 * @author Lakshmi Narayanan
 */
public class LoginAction extends DispatchAction {

    public ActionForward Login(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LoginForm loginForm = (LoginForm) form;
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUser(loginForm.getUserName(), loginForm.getPassword());
        HttpSession session = request.getSession();
        final String LOGIN_USER = "loginuser";
        if (user == null) {
            loginForm.setFailureMessage("UserName and Password does not match");
            loginForm.reset(mapping, request);
            return mapping.findForward("failure");
        } else {
            session.setAttribute("currentYear", DateUtils.formatDate(new Date(), "yyyy"));
            OnlineUserDAO onlineUserDAO = new OnlineUserDAO();
            OnlineUser onlineUser = onlineUserDAO.findByUserId(user.getUserId());
            if (!loginForm.isLoginAgain() && null != onlineUser) {
                loginForm.setWarningMessage("User is already logged on");
                return mapping.findForward("warning");
            } else {
                session.setAttribute("loginuser", user);
                session.setAttribute("roleDuty", new RoleDAO().getRoleDuty(user.getRole().getRoleId()));
                List<NotesModel> followupTasks = new NotesDAO().getFollowUpTasks(user, "todayDate");
                if (null != onlineUser) {
                    onlineUser.setLoggedOn(new Date());
                    onlineUser.setIpAddress(request.getRemoteAddr());
                    onlineUser.setSessionId(session.getId());
                } else {
                    onlineUser = new OnlineUser(user.getUserId(), new Date(), request.getRemoteAddr(), session.getId());
                    onlineUserDAO.save(onlineUser);
                }
                if (session.getAttribute(LOGIN_USER) != null) {
                    DBUtil dbUtil = new DBUtil();
                    dbUtil.deletelogout(user.getUserId());
                }
                session.setAttribute("followupFlag", CommonUtils.isNotEmpty(followupTasks));
                return mapping.findForward("success");
            }
        }
    }

    public ActionForward showLogin(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("success");
    }
}

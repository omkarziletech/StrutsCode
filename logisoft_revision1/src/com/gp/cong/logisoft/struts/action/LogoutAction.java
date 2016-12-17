package com.gp.cong.logisoft.struts.action;

import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.util.DBUtil;
import com.logiware.common.dao.OnlineUserDAO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * MyEclipse Struts Creation date: 12-12-2007
 *
 * XDoclet definition:
 *
 * @struts.action path="/logout" name="logoutForm" input="/admin/Logout.jsp"
 * scope="request" validate="true"
 */
public class LogoutAction extends Action {

    /**
     * Method execute
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        User user = null;
        if (session.getAttribute("loginuser") != null) {
            user = (User) session.getAttribute("loginuser");
            DBUtil dbUtil = new DBUtil();
            dbUtil.deletelogout(user.getUserId());
            session.removeAttribute("loginuser");
        }
        if (null != user) {
            new OnlineUserDAO().delete(user.getUserId());
        }
        session.invalidate();
        return mapping.findForward("login");
    }
}

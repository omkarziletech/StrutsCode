/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.gp.cong.logisoft.struts.form.TestUserForm;

/** 
 * MyEclipse Struts
 * Creation date: 06-13-2008
 * 
 * XDoclet definition:
 * @struts.action path="/testUser" name="testUserForm" input="/jsps/admin/testUser.jsp" scope="request" validate="true"
 */
public class TestUserAction extends Action {
    /*
     * Generated Methods
     */

    /**
     * Method execute
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        TestUserForm testUserForm = (TestUserForm) form;// TODO Auto-generated method stub
        String loginName = testUserForm.getLoginName();
        String password = testUserForm.getPassword();
        request.setAttribute("loginName", loginName);
        request.setAttribute("password", password);
        return mapping.findForward("testuser");
    }
}
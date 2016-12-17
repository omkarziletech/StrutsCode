package com.logiware.common.action;

import com.gp.cong.common.CommonUtils;
import com.logiware.common.dao.OnlineUserDAO;
import com.logiware.common.form.OnlineUserForm;
import com.logiware.common.reports.OnlineUserExcelCreator;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Lakshmi Narayanan
 */
public class OnlineUserAction extends BaseAction {

    private static final OnlineUserDAO onlineUserDAO = new OnlineUserDAO();

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        OnlineUserForm onlineUserForm = (OnlineUserForm) form;
        if (CommonUtils.isEqual(onlineUserForm.getSortBy(), "id") && CommonUtils.isEqual(onlineUserForm.getOrderBy(), "asc")) {
            onlineUserForm.setSortBy("loggedOn");
            onlineUserForm.setOrderBy("desc");
        }
        onlineUserDAO.search(onlineUserForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward exportToExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            String fileName = new OnlineUserExcelCreator().create();
            out.print(fileName);
        } catch (IOException e) {
            throw e;
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
        return null;
    }

    public ActionForward killUser(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        OnlineUserForm onlineUserForm = (OnlineUserForm) form;
        onlineUserDAO.delete(onlineUserForm.getId());
        return search(mapping, form, request, response);
    }
}

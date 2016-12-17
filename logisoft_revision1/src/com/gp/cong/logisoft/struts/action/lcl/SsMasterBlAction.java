/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSSMasterBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO;
import com.gp.cvst.logisoft.struts.form.lcl.SsMasterBlForm;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Mei
 */
public class SsMasterBlAction extends LogiwareDispatchAction {

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception, SQLException {
        SsMasterBlForm ssMasterBlForm = (SsMasterBlForm) form;
        User user = (User) request.getSession().getAttribute("loginuser");
        if (CommonUtils.isNotEmpty(request.getParameter("fromScreen")) && "EXP_VOYAGE".equalsIgnoreCase(request.getParameter("fromScreen"))) {
            ssMasterBlForm = (SsMasterBlForm) request.getSession().getAttribute("oldMasterDisputeForm");
            if (null != ssMasterBlForm) {
                request.setAttribute("ssMasterDisputeList", new LclSSMasterBlDAO().getSsMasterDisputeBlList("Disputed", "LCL SS MASTER BL", ssMasterBlForm));
            }
        } else {
            request.getSession().removeAttribute("oldMasterDisputeForm");
        }
        return mapping.findForward("success");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception, SQLException {
        SsMasterBlForm ssMasterBlForm = (SsMasterBlForm) form;
        User user = (User) request.getSession().getAttribute("loginuser");
        HttpSession session = request.getSession(true);
        session.setAttribute("oldMasterDisputeForm", ssMasterBlForm);
        request.setAttribute("ssMasterDisputeList", new LclSSMasterBlDAO().getSsMasterDisputeBlList("Disputed", "LCL SS MASTER BL", ssMasterBlForm));
        return mapping.findForward("success");
    }

    public ActionForward acknowledge(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = (User) request.getSession().getAttribute("loginuser");
        SsMasterBlForm ssMasterBlForm = (SsMasterBlForm) form;
        String ackComments = "ACKNOWLEDGED BY---->" + user.getLoginName() + "   ON----->" + DateUtils.formatDate(new Date(), "dd-MMM-yyyy hh:mm a");
        new DocumentStoreLogDAO().updateAck(ackComments, ssMasterBlForm.getDocumentMasterId());
        PrintWriter out = response.getWriter();
        out.print(ackComments);
        out.flush();
        out.close();
        return null;
    }
}

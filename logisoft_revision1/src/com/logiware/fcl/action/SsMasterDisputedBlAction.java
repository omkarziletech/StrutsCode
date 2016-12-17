package com.logiware.fcl.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.ItemDAO;
import com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO;
import com.logiware.fcl.dao.SsMasterDisputedBlDAO;
import com.logiware.fcl.dao.SearchDAO;
import com.logiware.fcl.form.SsMasterDisputedBlForm;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Lakshmi Narayanan
 */
public class SsMasterDisputedBlAction extends BaseAction {

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SsMasterDisputedBlForm ssMasterDisputedBlForm = (SsMasterDisputedBlForm) form;
        request.getSession().setAttribute("oldSsMasterDisputedBlForm", ssMasterDisputedBlForm);
        new SsMasterDisputedBlDAO().search(ssMasterDisputedBlForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward doSort(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SsMasterDisputedBlForm ssMasterDisputedBlForm = (SsMasterDisputedBlForm) form;
        SsMasterDisputedBlForm oldSsMasterDisputedBlForm = (SsMasterDisputedBlForm) request.getSession().getAttribute("oldSsMasterDisputedBlForm");
        oldSsMasterDisputedBlForm.setSortBy(ssMasterDisputedBlForm.getSortBy());
        oldSsMasterDisputedBlForm.setOrderBy(ssMasterDisputedBlForm.getOrderBy());
        new SsMasterDisputedBlDAO().search(oldSsMasterDisputedBlForm);
        request.setAttribute("ssMasterDisputedBlForm", oldSsMasterDisputedBlForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward reset(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getSession().removeAttribute("oldSsMasterDisputedBlForm");
        request.setAttribute("ssMasterDisputedBlForm", new SsMasterDisputedBlForm());
        return mapping.findForward(SUCCESS);
    }

    public ActionForward checkLocking(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SsMasterDisputedBlForm ssMasterDisputedBlForm = (SsMasterDisputedBlForm) form;
        HttpSession session = request.getSession(true);
        ItemDAO itemDAO = new ItemDAO();
        User user = (User) request.getSession().getAttribute("loginuser");
        String result = new SearchDAO().checkLocking(ssMasterDisputedBlForm.getFileNumber(), user.getUserId());
        PrintWriter out = response.getWriter();
        if (CommonUtils.isNotEmpty(result)) {
            out.print(result);
        } else {
            String itemDesc = "Quotes, Bookings, and BLs";
            String itemDesc1 = "SS MASTER DISPUTED BL";
            Integer itemId;
            out.print("available========" + itemDAO.getItemId(itemDesc));
            itemId = Integer.parseInt(itemDAO.getItemId(itemDesc1));
            itemId = itemDAO.getParentId(itemId);
            session.setAttribute("SSMasterItemId", itemId);
        }
        out.flush();
        out.close();
        return null;
    }

    public ActionForward acknowledge(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = (User) request.getSession().getAttribute("loginuser");
        SsMasterDisputedBlForm ssMasterDisputedBlForm = (SsMasterDisputedBlForm) form;
        String ackComments = "ACKNOWLEDGED BY---->" + user.getLoginName() + "   ON----->" + DateUtils.formatDate(new Date(), "dd-MMM-yyyy hh:mm a");
        new DocumentStoreLogDAO().updateAck(ackComments, ssMasterDisputedBlForm.getFileNumber());
        PrintWriter out = response.getWriter();
        out.print(ackComments);
        out.flush();
        out.close();
        return null;
    }

    public ActionForward goBack(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SsMasterDisputedBlForm ssMasterDisputedBlForm = (SsMasterDisputedBlForm) request.getSession().getAttribute("oldSsMasterDisputedBlForm");
        request.setAttribute("ssMasterDisputedBlForm", ssMasterDisputedBlForm);
        return mapping.findForward(SUCCESS);
    }
}

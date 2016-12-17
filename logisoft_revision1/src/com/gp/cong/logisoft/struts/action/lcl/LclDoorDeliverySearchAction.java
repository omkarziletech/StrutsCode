package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.lcl.dwr.LclSession;
import com.gp.cong.lcl.model.LclDoorDeliverySearchBean;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclDoorDeliverySearchDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.SearchDAO;
import com.gp.cong.logisoft.reports.DeliveryPoolExcelCreator;
import com.gp.cvst.logisoft.struts.form.lcl.lclDoorDeliverySearchForm;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author PALRAJ
 */
public class LclDoorDeliverySearchAction extends LogiwareDispatchAction {

    private static final String SEARCHCRITERIA = "searchCriteria";

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        lclDoorDeliverySearchForm lclDoorDeliverySearchForm = (lclDoorDeliverySearchForm) form;
        HttpSession session = request.getSession();
        LclSession lclSession = null != request.getSession().getAttribute("lclSession") ? (LclSession) request.getSession().getAttribute("lclSession") : new LclSession();
        List<LclDoorDeliverySearchBean> lclDoorDeliverySearchResult = new LclDoorDeliverySearchDAO().search(lclDoorDeliverySearchForm);
        lclSession.setSearchResult("true");
        if(session != null) {
            session.setAttribute("lclDoorDeliverySearchResult", lclDoorDeliverySearchResult);
        }
        request.setAttribute("lclDoorDeliverySearchResult", lclDoorDeliverySearchResult);
        session.setAttribute("oldLclDoorDeliverySearchForm", lclDoorDeliverySearchForm);
        return mapping.findForward(SEARCHCRITERIA);
    }

    public ActionForward checkLocking(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        lclDoorDeliverySearchForm lclDoorDeliverySearchForm = (lclDoorDeliverySearchForm) form;
        User loginUser = getCurrentUser(request);
        SearchDAO searchDAO = new SearchDAO();
        searchDAO.checkLockingStatus(lclDoorDeliverySearchForm.getFileNumber(), loginUser.getUserId(), response);
        return null;
    }

    public ActionForward goBackToSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        lclDoorDeliverySearchForm oldLclDoorDeliverySearchForm = (lclDoorDeliverySearchForm) session.getAttribute("oldLclDoorDeliverySearchForm");
        List<LclDoorDeliverySearchBean> lclDoorDeliverySearchResult = new LclDoorDeliverySearchDAO().search(oldLclDoorDeliverySearchForm);
        request.setAttribute("fileNumber", request.getParameter("fileNumber"));
        request.setAttribute("lclDoorDeliverySearchResult", lclDoorDeliverySearchResult);
        request.setAttribute("lclDoorDeliverySearchForm", oldLclDoorDeliverySearchForm);
        return mapping.findForward(SEARCHCRITERIA);
    }

    public ActionForward doSort(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        LclSession lclSession = null != request.getSession().getAttribute("lclSession") ? (LclSession) request.getSession().getAttribute("lclSession") : new LclSession();
        lclDoorDeliverySearchForm lclDoorDeliverySearchForm = (lclDoorDeliverySearchForm) form;
        lclDoorDeliverySearchForm.setSortBy(request.getParameter("sortBy"));
        lclDoorDeliverySearchForm.setOrderBy(request.getParameter("orderBy"));
        List<LclDoorDeliverySearchBean> lclDoorDeliverySearchResult = new LclDoorDeliverySearchDAO().search(lclDoorDeliverySearchForm);
        lclSession.setSearchResult("true");
        if(session != null) {
            session.setAttribute("lclDoorDeliverySearchResult", lclDoorDeliverySearchResult);
        }
        request.setAttribute("lclDoorDeliverySearchResult", lclDoorDeliverySearchResult);
        session.setAttribute("oldLclDoorDeliverySearchForm", lclDoorDeliverySearchForm);
        return mapping.findForward(SEARCHCRITERIA);
    }

    public ActionForward clearValues(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception{
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.removeAttribute("lclDoorDeliverySearchResult");
        }
        return mapping.findForward(SEARCHCRITERIA);
    }
    public ActionForward exportToExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        try {
            List<LclDoorDeliverySearchBean> lclDoorDeliverySearchResult = (List<LclDoorDeliverySearchBean>) request.getSession().getAttribute("lclDoorDeliverySearchResult");
            String fileName = new DeliveryPoolExcelCreator().create(lclDoorDeliverySearchResult);
            out.print(fileName);
        } catch (Exception e) {
            throw e;
        } finally {
            out.flush();
            out.close();
           
        }
         return null;
    }
}

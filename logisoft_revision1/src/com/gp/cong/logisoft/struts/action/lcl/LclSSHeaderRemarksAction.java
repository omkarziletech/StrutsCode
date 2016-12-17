/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsRemarksDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclSSHeaderRemarksForm;
import com.gp.cong.logisoft.domain.lcl.LclSsRemarks;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.gp.cong.logisoft.domain.User;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Owner
 */
public class LclSSHeaderRemarksAction extends LogiwareDispatchAction {

    public ActionForward addRemarks(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclSSHeaderRemarksForm lclSSHeaderRemarksForm = (LclSSHeaderRemarksForm) form;
        LclSsRemarksDAO lclSSRemarksDAO = new LclSsRemarksDAO();
        User user = getCurrentUser(request);
        List<LclSsRemarks> remarks = lclSSRemarksDAO.getHeaderRemarksByHeaderIdAndType(lclSSHeaderRemarksForm.getHeaderId(), "void", "");
        if (CommonUtils.isNotEmpty(remarks)) {
            request.setAttribute("setColor", "true");
        }
        lclSSRemarksDAO.insertLclSSRemarks(lclSSHeaderRemarksForm.getHeaderId(), "Manual Note", "", lclSSHeaderRemarksForm.getRemarks().toUpperCase(),
                lclSSHeaderRemarksForm.getFollowupDate(), user.getUserId());
        request.setAttribute("remarksList", lclSSRemarksDAO.getHeaderRemarksByHeaderIdAndType(lclSSHeaderRemarksForm.getHeaderId(), "", ""));
        return mapping.findForward("VoyageNotes");
    }

    public ActionForward displayNotes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclSSHeaderRemarksForm lclSSHeaderRemarksForm = (LclSSHeaderRemarksForm) form;
        if (CommonUtils.isNotEmpty(lclSSHeaderRemarksForm.getActions())) {
            LclSsRemarksDAO lclSSRemarksDAO = new LclSsRemarksDAO();
            List<LclSsRemarks> remarks = lclSSRemarksDAO.getHeaderRemarksByHeaderIdAndType(lclSSHeaderRemarksForm.getHeaderId(), "void", "");
            if (CommonUtils.isNotEmpty(remarks)) {
                request.setAttribute("setColor", "true");
            }
            if (lclSSHeaderRemarksForm.getActions().equalsIgnoreCase("2")) {
                request.setAttribute("remarksList", lclSSRemarksDAO.getHeaderRemarksByHeaderIdAndType(lclSSHeaderRemarksForm.getHeaderId(), "Manual Note", "followexists"));
            } else if (lclSSHeaderRemarksForm.getActions().equalsIgnoreCase("3")) {
                request.setAttribute("remarksList", lclSSRemarksDAO.getHeaderRemarksByHeaderIdAndType(lclSSHeaderRemarksForm.getHeaderId(), "Manual Note", "followpast"));
            } else if (lclSSHeaderRemarksForm.getActions().equalsIgnoreCase("4")) {
                request.setAttribute("remarksList", lclSSRemarksDAO.getHeaderRemarksByHeaderIdAndType(lclSSHeaderRemarksForm.getHeaderId(), "Manual Note", ""));
            } else if (lclSSHeaderRemarksForm.getActions().equalsIgnoreCase("5")) {
                request.setAttribute("remarksList", lclSSRemarksDAO.getHeaderRemarksByHeaderIdAndType(lclSSHeaderRemarksForm.getHeaderId(), "auto", ""));
            } else if (lclSSHeaderRemarksForm.getActions().equalsIgnoreCase("show All")) {
                request.setAttribute("remarksList", lclSSRemarksDAO.getHeaderRemarksByHeaderIdAndType(lclSSHeaderRemarksForm.getHeaderId(), "", ""));
            } else if (lclSSHeaderRemarksForm.getActions().equalsIgnoreCase("6")) {
                request.setAttribute("remarksList", lclSSRemarksDAO.getHeaderRemarksByHeaderIdAndType(lclSSHeaderRemarksForm.getHeaderId(), "event", ""));
            } else if (lclSSHeaderRemarksForm.getActions().equalsIgnoreCase("11")) {
                request.setAttribute("remarksList", lclSSRemarksDAO.getHeaderRemarksByHeaderIdAndType(lclSSHeaderRemarksForm.getHeaderId(), "void", ""));

            }
        }
        return mapping.findForward("VoyageNotes");
    }

    public ActionForward deleteNotes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclSSHeaderRemarksForm lclSSHeaderRemarksForm = (LclSSHeaderRemarksForm) form;
        LclSsRemarksDAO lclSSRemarksDAO = new LclSsRemarksDAO();
        if (CommonUtils.isNotEmpty(lclSSHeaderRemarksForm.getId())) {
            LclSsRemarks lclSsRemarks = lclSSRemarksDAO.findById(lclSSHeaderRemarksForm.getId());
            LclSsRemarks autoSsRemarks = new LclSsRemarks();
            //autoUnitSsRemarks.setLclSsHeader(lclSsRemarks.getLclSsHeader());
            autoSsRemarks.setLclSsHeader(lclSsRemarks.getLclSsHeader());
            autoSsRemarks.setType("auto");
            //autoUnitSsRemarks.setStatus("Deleted");
            autoSsRemarks.setRemarks("Deleted -> Manual Note : " + lclSsRemarks.getRemarks());
            //autoUnitSsRemarks.setFollowUpUserId(lclSsRemarks.getFollowUpUserId());
//	    autoUnitSsRemarks.setFollowupDateTime(lclUnitSsRemarks.getFollowupDateTime());
//	    autoUnitSsRemarks.setFollowupEmail(lclUnitSsRemarks.getFollowupEmail());
            autoSsRemarks.setEnteredBy(lclSsRemarks.getEnteredBy());
            autoSsRemarks.setEnteredDatetime(lclSsRemarks.getEnteredDatetime());
            autoSsRemarks.setModifiedDatetime(new Date());
            autoSsRemarks.setModifiedBy(getCurrentUser(request));
            lclSsRemarks.setStatus("Closed");
            lclSsRemarks.setType("void");
            lclSsRemarks.setModifiedDatetime(new Date());
            lclSsRemarks.setModifiedBy(getCurrentUser(request));
            lclSSRemarksDAO.save(autoSsRemarks);
            List<LclSsRemarks> remarks = lclSSRemarksDAO.getHeaderRemarksByHeaderIdAndType(lclSSHeaderRemarksForm.getHeaderId(), "void", "");
            if (CommonUtils.isNotEmpty(remarks)) {
                request.setAttribute("setColor", "true");
            }
        }
        request.setAttribute("remarksList", lclSSRemarksDAO.getHeaderRemarksByHeaderIdAndType(lclSSHeaderRemarksForm.getHeaderId(), "Manual Note", ""));
        return mapping.findForward("VoyageNotes");
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsRemarks;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsRemarksDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclUnitSsRemarksForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class LclUnitSsRemarksAction extends LogiwareDispatchAction {

    private LclUnitSsRemarksDAO lclUnitSsRemarksDAO = new LclUnitSsRemarksDAO();

    public ActionForward displayNotes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitSsRemarksForm lclUnitSsRemarksForm = (LclUnitSsRemarksForm) form;
        List<LclUnitSsRemarks> remarks =lclUnitSsRemarksDAO.getRemarksList(Long.parseLong(lclUnitSsRemarksForm.getHeaderId()),
                Long.parseLong(lclUnitSsRemarksForm.getUnitId()), "voidedNotes", "");
        if (CommonUtils.isNotEmpty(remarks)) {
            request.setAttribute("setColor", "true");
        }
        request.setAttribute("notesList", lclUnitSsRemarksDAO.getRemarksList(Long.parseLong(lclUnitSsRemarksForm.getHeaderId()),
                Long.parseLong(lclUnitSsRemarksForm.getUnitId()), lclUnitSsRemarksForm.getActions(), ""));
        request.setAttribute("lclUnitSsRemarksForm", lclUnitSsRemarksForm);
        return mapping.findForward("unitSsNotes");
    }

    public ActionForward saveNotes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitSsRemarksForm lclUnitSsRemarksForm = (LclUnitSsRemarksForm) form;
        User loginUser = getCurrentUser(request);
        lclUnitSsRemarksDAO.insertLclunitRemarks(Long.parseLong(lclUnitSsRemarksForm.getHeaderId()),
                Long.parseLong(lclUnitSsRemarksForm.getUnitId()), "manual", lclUnitSsRemarksForm.getRemarks().toUpperCase(),
                loginUser.getUserId());
        List<LclUnitSsRemarks> remarks = lclUnitSsRemarksDAO.getRemarksList(Long.parseLong(lclUnitSsRemarksForm.getHeaderId()),
                Long.parseLong(lclUnitSsRemarksForm.getUnitId()), "voidedNotes", "");
        if (CommonUtils.isNotEmpty(remarks)) {
            request.setAttribute("setColor", "true");
        }
        request.setAttribute("lclUnitSsRemarksForm", lclUnitSsRemarksForm);
        request.setAttribute("notesList", lclUnitSsRemarksDAO.getRemarksList(Long.parseLong(lclUnitSsRemarksForm.getHeaderId()),
                Long.parseLong(lclUnitSsRemarksForm.getUnitId()), lclUnitSsRemarksForm.getActions(), ""));
        return mapping.findForward("unitSsNotes");
    }

    public ActionForward deleteNotes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitSsRemarksForm lclUnitSsRemarksForm = (LclUnitSsRemarksForm) form;
        if (CommonUtils.isNotEmpty(lclUnitSsRemarksForm.getId())) {
            LclUnitSsRemarks lclUnitSsRemarks = lclUnitSsRemarksDAO.findById(lclUnitSsRemarksForm.getId());
            LclUnitSsRemarks autoUnitSsRemarks = new LclUnitSsRemarks();
            autoUnitSsRemarks.setLclUnit(lclUnitSsRemarks.getLclUnit());
            autoUnitSsRemarks.setLclSsHeader(lclUnitSsRemarks.getLclSsHeader());
            autoUnitSsRemarks.setType("auto");
            //autoUnitSsRemarks.setStatus("NULL");
            autoUnitSsRemarks.setRemarks("Deleted -> Manual Note : " + lclUnitSsRemarks.getRemarks());
            autoUnitSsRemarks.setFollowUpUserId(lclUnitSsRemarks.getFollowUpUserId());
//	    autoUnitSsRemarks.setFollowupDateTime(lclUnitSsRemarks.getFollowupDateTime());
//	    autoUnitSsRemarks.setFollowupEmail(lclUnitSsRemarks.getFollowupEmail());
            autoUnitSsRemarks.setEnteredBy(lclUnitSsRemarks.getEnteredBy());
            autoUnitSsRemarks.setEnteredDatetime(lclUnitSsRemarks.getEnteredDatetime());
            autoUnitSsRemarks.setModifiedDatetime(new Date());
            autoUnitSsRemarks.setModifiedby(getCurrentUser(request));
            lclUnitSsRemarks.setStatus("Closed");
            lclUnitSsRemarks.setType("void");
            lclUnitSsRemarks.setModifiedDatetime(new Date());
            lclUnitSsRemarks.setModifiedby(getCurrentUser(request));
            lclUnitSsRemarksDAO.save(autoUnitSsRemarks);
            List<LclUnitSsRemarks> remarks = lclUnitSsRemarksDAO.getRemarksList(Long.parseLong(lclUnitSsRemarksForm.getHeaderId()),
                    Long.parseLong(lclUnitSsRemarksForm.getUnitId()), "voidedNotes", "");
            if (CommonUtils.isNotEmpty(remarks)) {
                request.setAttribute("setColor", "true");
            }
        }
        request.setAttribute("notesList", lclUnitSsRemarksDAO.getRemarksList(Long.parseLong(lclUnitSsRemarksForm.getHeaderId()),
                Long.parseLong(lclUnitSsRemarksForm.getUnitId()), lclUnitSsRemarksForm.getActions(), ""));
        return mapping.findForward("unitSsNotes");
    }
}

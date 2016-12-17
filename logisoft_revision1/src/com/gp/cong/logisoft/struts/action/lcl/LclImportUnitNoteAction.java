/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnit;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsRemarks;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsRemarksDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclUnitSsRemarksForm;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Logiware
 */
public class LclImportUnitNoteAction extends LogiwareDispatchAction implements LclCommonConstant {

    private LclUnitSsRemarksDAO lclUnitSsRemarksDAO = new LclUnitSsRemarksDAO();

    public ActionForward displayNotes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitSsRemarksForm lclUnitSsRemarksForm = (LclUnitSsRemarksForm) form;
        if (lclUnitSsRemarksForm.getUnitId() != null && !lclUnitSsRemarksForm.getUnitId().equals("")) {
            if (lclUnitSsRemarksForm.getActions() != null && lclUnitSsRemarksForm.getActions().equalsIgnoreCase("1")) {
                request.setAttribute("notesList", lclUnitSsRemarksDAO.executeQuery("from LclUnitSsRemarks where lclSsHeader.id='" + lclUnitSsRemarksForm.getHeaderId() + "' and lclUnit.id=" + lclUnitSsRemarksForm.getUnitId() + " AND type = 'Void' "));
            } else if (lclUnitSsRemarksForm.getActions() != null && lclUnitSsRemarksForm.getActions().equalsIgnoreCase("2")) {
                request.setAttribute("notesList", lclUnitSsRemarksDAO.executeQuery("from LclUnitSsRemarks where lclSsHeader.id='" + lclUnitSsRemarksForm.getHeaderId() + "' and lclUnit.id=" + lclUnitSsRemarksForm.getUnitId() + " AND followupDateTime >= '" + DateUtils.formatDate(new Date(), "yyyy-MM-dd") + "' ORDER BY id DESC"));
            } else if (lclUnitSsRemarksForm.getActions() != null && lclUnitSsRemarksForm.getActions().equalsIgnoreCase("3")) {
                request.setAttribute("notesList", lclUnitSsRemarksDAO.executeQuery("from LclUnitSsRemarks where lclSsHeader.id='" + lclUnitSsRemarksForm.getHeaderId() + "' and lclUnit.id=" + lclUnitSsRemarksForm.getUnitId() + " AND followupDateTime <= '" + DateUtils.formatDate(new Date(), "yyyy-MM-dd") + "' ORDER BY id DESC"));
            } else if (lclUnitSsRemarksForm.getActions() != null && lclUnitSsRemarksForm.getActions().equalsIgnoreCase("4")) {
                request.setAttribute("notesList", lclUnitSsRemarksDAO.executeQuery("from LclUnitSsRemarks where lclSsHeader.id='" + lclUnitSsRemarksForm.getHeaderId() + "' and lclUnit.id='" + lclUnitSsRemarksForm.getUnitId() + "'  AND type = 'Manual' ORDER BY id DESC"));
            } else if (lclUnitSsRemarksForm.getActions() != null && lclUnitSsRemarksForm.getActions().equalsIgnoreCase("5")) {
                request.setAttribute("notesList", lclUnitSsRemarksDAO.executeQuery("from LclUnitSsRemarks where lclSsHeader.id='" + lclUnitSsRemarksForm.getHeaderId() + "' and lclUnit.id='" + lclUnitSsRemarksForm.getUnitId() + "' AND type= 'auto'   ORDER BY id DESC"));
            } else if (lclUnitSsRemarksForm.getActions() != null && lclUnitSsRemarksForm.getActions().equalsIgnoreCase("11")) {
                request.setAttribute("notesList", lclUnitSsRemarksDAO.executeQuery("from LclUnitSsRemarks where lclSsHeader.id='" + lclUnitSsRemarksForm.getHeaderId() + "' and lclUnit.id='" + lclUnitSsRemarksForm.getUnitId() + "' AND type like 'EDISTG%' ORDER BY id DESC"));
            } else if (lclUnitSsRemarksForm.getActions() != null && lclUnitSsRemarksForm.getActions().equalsIgnoreCase("8")) {
                request.setAttribute("notesList", lclUnitSsRemarksDAO.executeQuery("from LclUnitSsRemarks where lclSsHeader.id='" + lclUnitSsRemarksForm.getHeaderId() + "' and lclUnit.id='" + lclUnitSsRemarksForm.getUnitId() + "' AND type IN ('T','Unit_Tracking')  ORDER BY id DESC"));
            } else {
                request.setAttribute("notesList", lclUnitSsRemarksDAO.executeQuery("from LclUnitSsRemarks where lclSsHeader.id='" + lclUnitSsRemarksForm.getHeaderId() + "' and lclUnit.id='" + lclUnitSsRemarksForm.getUnitId() + "' AND (type like ('EDISTG%') OR type IN ('auto','Manual'))  ORDER BY id DESC"));
            }
        }
        request.setAttribute("lclUnitSsRemarksForm", lclUnitSsRemarksForm);
        return mapping.findForward("unitNotes");
    }

    public ActionForward addUnitNotes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitSsRemarksForm lclUnitSsRemarksForm = (LclUnitSsRemarksForm) form;
        if (lclUnitSsRemarksForm.getRemarks() != null) {
            Date d = new Date();
            User user = (User) request.getSession().getAttribute("loginuser");
            LclUnitDAO lclunitdao = new LclUnitDAO();
            LclSsHeaderDAO lclssheaderdao = new LclSsHeaderDAO();
            LclUnit lclunit = lclunitdao.findById(Long.parseLong(lclUnitSsRemarksForm.getUnitId()));
            LclSsHeader lclssheader = lclssheaderdao.findById(Long.parseLong(lclUnitSsRemarksForm.getHeaderId()));
            LclUnitSsRemarks lclUnitSsRemarks = new LclUnitSsRemarks();
            lclUnitSsRemarks.setLclUnit(lclunit);
            lclUnitSsRemarks.setLclSsHeader(lclssheader);
            lclUnitSsRemarks.setType(REMARKS_TYPE_MANUAL);
            lclUnitSsRemarks.setRemarks(lclUnitSsRemarksForm.getRemarks().toUpperCase());
            lclUnitSsRemarks.setEnteredBy(user);
            if (lclUnitSsRemarksForm.getFollowUpDate() != null && !lclUnitSsRemarksForm.getFollowUpDate().equals("")) {
                lclUnitSsRemarks.setFollowupDateTime(DateUtils.parseDate(lclUnitSsRemarksForm.getFollowUpDate(), "dd-MMM-yyyy"));
                lclUnitSsRemarks.setFollowUpUserId(user);
            }
            if ("on".equalsIgnoreCase(lclUnitSsRemarksForm.getUserEmail())) {
                lclUnitSsRemarks.setFollowupEmail(lclUnitSsRemarksForm.getFollowUpEmail() + "," + user.getEmail());
            } else {
                lclUnitSsRemarks.setFollowupEmail(lclUnitSsRemarksForm.getFollowUpEmail());
            }
            lclUnitSsRemarks.setEnteredDatetime(d);
            lclUnitSsRemarks.setModifiedDatetime(d);
            lclUnitSsRemarks.setModifiedby(user);
            lclUnitSsRemarksDAO.save(lclUnitSsRemarks);
        }
        request.setAttribute("lclUnitSsRemarksForm", lclUnitSsRemarksForm);
        request.setAttribute("notesList", lclUnitSsRemarksDAO.executeQuery("from LclUnitSsRemarks where lclSsHeader.id='" + lclUnitSsRemarksForm.getHeaderId() + "' and lclUnit.id='" + lclUnitSsRemarksForm.getUnitId() + "'AND type IN('Manual') ORDER BY id DESC"));
        lclUnitSsRemarksForm.setActions("show All");
        return mapping.findForward("unitNotes");
    }
	 
    public ActionForward voidNotes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitSsRemarksForm lclUnitSsRemarksForm = (LclUnitSsRemarksForm) form;
        if (CommonUtils.isNotEmpty(lclUnitSsRemarksForm.getId())) {
	     User user = (User) request.getSession().getAttribute("loginuser");
            LclUnitSsRemarks lclUnitSsRemarks = lclUnitSsRemarksDAO.findById(lclUnitSsRemarksForm.getId());
	    LclUnitSsRemarks  autoUnitSsRemarks=new LclUnitSsRemarks();
	    autoUnitSsRemarks.setLclUnit(lclUnitSsRemarks.getLclUnit());
	    autoUnitSsRemarks.setLclSsHeader(lclUnitSsRemarks.getLclSsHeader());
	    autoUnitSsRemarks.setType("auto");
	    autoUnitSsRemarks.setRemarks("Deleted -> Manual Note : "+lclUnitSsRemarks.getRemarks());
	    autoUnitSsRemarks.setFollowUpUserId(lclUnitSsRemarks.getFollowUpUserId());
//	    autoUnitSsRemarks.setFollowupDateTime(lclUnitSsRemarks.getFollowupDateTime());
//	    autoUnitSsRemarks.setFollowupEmail(lclUnitSsRemarks.getFollowupEmail());
	    autoUnitSsRemarks.setEnteredBy(user);
	    autoUnitSsRemarks.setEnteredDatetime(new Date());	   
	    autoUnitSsRemarks.setModifiedDatetime(new Date());
	    autoUnitSsRemarks.setModifiedby(user);
            lclUnitSsRemarks.setType("Void");
            lclUnitSsRemarksDAO.save(autoUnitSsRemarks);
        }
        request.setAttribute("lclUnitSsRemarksForm", lclUnitSsRemarksForm);
//	request.setAttribute("notesList", lclUnitSsRemarksDAO.executeQuery("from LclUnitSsRemarks where lclSsHeader.id='" + lclUnitSsRemarksForm.getHeaderId() + "' and lclUnit.id='" + lclUnitSsRemarksForm.getUnitId() + "'AND type IN('auto','Manual')  ORDER BY id DESC"));
	 return displayNotes(mapping, form, request, response);
    }
    public ActionForward updateUnitNotes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	LclUnitSsRemarksForm lclUnitSsRemarksForm = (LclUnitSsRemarksForm) form;
        if (lclUnitSsRemarksForm.getRemarks() != null) {
            Date d = new Date();
            User user = (User) request.getSession().getAttribute("loginuser");
	    LclUnitSsRemarks lclUnitSsRemarks=lclUnitSsRemarksDAO.findById(lclUnitSsRemarksForm.getId());
	    LclUnitSsRemarks autounitSsRemarks=new LclUnitSsRemarks();
            autounitSsRemarks.setLclUnit(lclUnitSsRemarks.getLclUnit());
            autounitSsRemarks.setLclSsHeader(lclUnitSsRemarks.getLclSsHeader());
            autounitSsRemarks.setType(REMARKS_TYPE_AUTO);
            autounitSsRemarks.setRemarks("Manual Note: "+lclUnitSsRemarks.getRemarks()+" -> "+lclUnitSsRemarksForm.getRemarks().toUpperCase());
            String oldRemarks = lclUnitSsRemarks.getRemarks();
            lclUnitSsRemarks.setRemarks(lclUnitSsRemarksForm.getRemarks().toUpperCase());
            autounitSsRemarks.setEnteredBy(user);
	    autounitSsRemarks.setEnteredDatetime(d);
            if (lclUnitSsRemarksForm.getFollowUpDate() != null && !lclUnitSsRemarksForm.getFollowUpDate().equals("")) {
                lclUnitSsRemarks.setFollowupDateTime(DateUtils.parseDate(lclUnitSsRemarksForm.getFollowUpDate(), "dd-MMM-yyyy"));
//                autounitSsRemarks.setFollowupDateTime(DateUtils.parseDate(lclUnitSsRemarksForm.getFollowUpDate(), "dd-MMM-yyyy"));
                lclUnitSsRemarks.setFollowUpUserId(user);
//                autounitSsRemarks.setFollowUpUserId(user);
            }
            if ("on".equalsIgnoreCase(lclUnitSsRemarksForm.getUserEmail())) {
                lclUnitSsRemarks.setFollowupEmail(lclUnitSsRemarksForm.getFollowUpEmail() + "," + user.getEmail());
                autounitSsRemarks.setFollowupEmail(lclUnitSsRemarksForm.getFollowUpEmail() + "," + user.getEmail());
            } else {
                lclUnitSsRemarks.setFollowupEmail(lclUnitSsRemarksForm.getFollowUpEmail());
                autounitSsRemarks.setFollowupEmail(lclUnitSsRemarksForm.getFollowUpEmail());
            }
            
            autounitSsRemarks.setModifiedDatetime(d);
            autounitSsRemarks.setModifiedby(user);
            lclUnitSsRemarks.setModifiedDatetime(d);
            lclUnitSsRemarks.setModifiedby(user);
            if(CommonUtils.isNotEqual(oldRemarks, lclUnitSsRemarksForm.getRemarks().toUpperCase())){                
            lclUnitSsRemarksDAO.save(autounitSsRemarks);
            }
        }
        request.setAttribute("lclUnitSsRemarksForm", lclUnitSsRemarksForm);
//	request.setAttribute("notesList", lclUnitSsRemarksDAO.executeQuery("from LclUnitSsRemarks where lclSsHeader.id='" + lclUnitSsRemarksForm.getHeaderId() + "' and lclUnit.id='" + lclUnitSsRemarksForm.getUnitId() + "'AND type IN('Manual') ORDER BY id DESC"));
        return displayNotes(mapping, form, request, response);
    }
}

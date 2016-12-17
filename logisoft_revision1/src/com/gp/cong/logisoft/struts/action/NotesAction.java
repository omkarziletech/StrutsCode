/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.action;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.notes.NotesBC;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.Notes;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.NotesDAO;
import com.gp.cong.logisoft.struts.form.NotesForm;
import com.gp.cong.logisoft.util.CommonFunctions;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * r
 * MyEclipse Struts
 * Creation date: 12-01-2007
 * Modified by Gayatri 1/6/2009..
 * XDoclet definition:
 * @struts.action path="/notes" name="notesForm" input="/jsps/admin/Notes.jsp" scope="request" validate="true"
 */
public class NotesAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	HttpSession session = ((HttpServletRequest) request).getSession();
	NotesForm notesForm = (NotesForm) form;
	String forward = null;
	String buttonValue = notesForm.getButtonValue();
	NotesBC notesBC = new NotesBC();
	String itemName = request.getParameter("itemName");
	if (NotesConstants.SHOW_ACCRUALS_NOTES.equals(notesForm.getActions())) {
	    request.setAttribute(NotesConstants.NOTES_LIST, notesBC.getAccrualNotes(notesForm.getAccrualsRefId(), notesForm.getInvoiceRefId(),
                    (null !=notesForm.getShipmentType() && ("LCLI".equalsIgnoreCase(notesForm.getShipmentType()) || "LCLE".equalsIgnoreCase(notesForm.getShipmentType()))) ? null:notesForm.getCostRefId()));
	    request.setAttribute("notesForm", notesForm);
	    request.setAttribute("itemName", notesForm.getItemName());
	    return mapping.findForward(NotesConstants.NOTES_PAGE);
	} else {
	    //		  --------------------if button value null search notes for particular module------------------------
	    if (buttonValue == null || buttonValue.equals("")) {
		if ("CHARGE CODE".equalsIgnoreCase(notesForm.getModuleId())) {
		    request.setAttribute(NotesConstants.NOTES_LIST, new NotesDAO().getChargeCodeNotes(notesForm.getModuleRefId()));
		}else if (NotesConstants.SHOW_VOID_NOTES.equals(notesForm.getActions())) {
		    request.setAttribute(NotesConstants.NOTES_LIST, notesBC.getVoidNotes(notesForm.getModuleId(), notesForm.getModuleRefId(), notesForm.getItemName()));
		} else if (NotesConstants.SHOW_PAST_FOLLOWUPDATE_NOTES.equals(notesForm.getActions())) {
		    request.setAttribute(NotesConstants.NOTES_LIST, notesBC.findPastFollowupDateNotes(notesForm.getModuleId(), notesForm.getModuleRefId(), notesForm.getItemName()));
		    notesForm.setShowVoidNotes(false);
		} else if (NotesConstants.SHOW_EXISTS_FOLLOWUPDATE_NOTES.equals(notesForm.getActions())) {
		    request.setAttribute(NotesConstants.NOTES_LIST, notesBC.findExistsFollowupDateNotes(notesForm.getModuleId(), notesForm.getModuleRefId(), notesForm.getItemName()));
		    notesForm.setShowVoidNotes(false);
		} else if (NotesConstants.SHOW_MANUAL_NOTES.equals(notesForm.getActions())) {
		    request.setAttribute(NotesConstants.NOTES_LIST, notesBC.getByNoteType(notesForm.getModuleId(), notesForm.getModuleRefId(), NotesConstants.NOTES_TYPE_MANUAL, notesForm.getItemName()));
		    notesForm.setShowVoidNotes(false);
		} else if (NotesConstants.SHOW_AUTO_NOTES.equals(notesForm.getActions())) {
		    request.setAttribute(NotesConstants.NOTES_LIST, notesBC.getByNoteType(notesForm.getModuleId(), notesForm.getModuleRefId(), NotesConstants.NOTES_TYPE_AUTO, notesForm.getItemName()));
		    notesForm.setShowVoidNotes(false);
		} else if (NotesConstants.SHOW_EVENT_NOTES.equals(notesForm.getActions())) {
		    request.setAttribute(NotesConstants.NOTES_LIST, notesBC.getByNoteType(notesForm.getModuleId(), notesForm.getModuleRefId(), NotesConstants.NOTES_TYPE_EVENT, notesForm.getItemName()));
		    notesForm.setShowVoidNotes(false);
		} else if (NotesConstants.SHOW_CORRECTION_EVENT_NOTES.equals(notesForm.getActions())) {
		    //getModuleId = correction
		    request.setAttribute(NotesConstants.NOTES_LIST, notesBC.getByNoteType(notesForm.getModuleId(), notesForm.getModuleRefId(), NotesConstants.NOTES_TYPE_EVENT, notesForm.getItemName()));
		    notesForm.setShowVoidNotes(false);
		} else if (NotesConstants.SHOW_DELETED_CORRECTION_EVENT_NOTES.equals(notesForm.getActions())) {
		    request.setAttribute(NotesConstants.NOTES_LIST, notesBC.getDeletedNotesForCorrections(notesForm.getModuleId(), notesForm.getModuleRefId(), "deleted"));
		    notesForm.setShowVoidNotes(false);
		} else if (NotesConstants.SHOW_DISPUTED_NOTES.equals(notesForm.getActions())) {
		    request.setAttribute(NotesConstants.NOTES_LIST, new NotesDAO().findByDisputedNotes(notesForm.getModuleId(), notesForm.getModuleRefId(), NotesConstants.DISPUTEDBLCODE));
		    notesForm.setShowVoidNotes(false);
		} else if (NotesConstants.SHOW_TRACKING_NOTES.equals(notesForm.getActions())) {
		    request.setAttribute(NotesConstants.NOTES_LIST, new NotesDAO().findByDisputedNotes(notesForm.getModuleId(), notesForm.getModuleRefId(), NotesConstants.TRACKINGCODE));
		    notesForm.setShowVoidNotes(false);
		} else {
		    request.setAttribute(NotesConstants.NOTES_LIST, notesBC.getNotes(notesForm.getModuleId(), notesForm.getModuleRefId(), notesForm.getItemName()));
		    notesForm.setShowVoidNotes(false);
		}
		if (CommonFunctions.isNotNull(request.getParameter(CommonConstants.ACKNOWLEDGE)) && CommonFunctions.isNotNull(itemName)
			&& NotesConstants.DISPUTEDBLCODE.equals(itemName)) {
		    request.setAttribute(CommonConstants.ACKNOWLEDGE, request.getParameter(CommonConstants.ACKNOWLEDGE));
		}
		if (notesForm.getModuleId() != null && notesForm.getModuleId().equalsIgnoreCase(NotesConstants.MODULE_ID_CORRECTION)) {
		    forward = "correction_history";
		}
		request.setAttribute("notesForm", notesForm);
		request.setAttribute("itemName", notesForm.getItemName());
		return mapping.findForward(NotesConstants.NOTES_PAGE);
	    }

	    //			--------------------buttonValue to save,delete Notes------------------------------------------------
	    if (buttonValue.equals(CommonConstants.SAVE_ACTION)) {
		User user = null;
		String moduleName = null;
		Notes notes = new Notes();
		if (session.getAttribute(NotesConstants.LOGIN_USER) != null) {
		    user = (User) session.getAttribute(NotesConstants.LOGIN_USER);
		}
		notesForm.setActions("showAll");
		notes = notesBC.saveNotes(notesForm, user.getLoginName());
		request.setAttribute("notes", notes);
		request.setAttribute(NotesConstants.NOTES_LIST, notesBC.getNotes(notes.getModuleId(), notesForm.getModuleRefId(), notesForm.getItemName()));
		notesForm.setShowVoidNotes(false);
		forward = NotesConstants.NOTES_PAGE;
	    } else if (buttonValue.equals(CommonConstants.DELETE_ACTION)) {
		notesBC.deleteNotes(notesForm);
		request.setAttribute(NotesConstants.NOTES_LIST, notesBC.getNotes(notesForm.getModuleId(), notesForm.getModuleRefId(), notesForm.getItemName()));
		notesForm.setShowVoidNotes(false);
		forward = NotesConstants.NOTES_PAGE;
	    } else if (NotesConstants.SET_AS_VOID.equals(buttonValue)) {
		Integer noteId = Integer.parseInt(notesForm.getNoteId());
		notesBC.setAsVoid(noteId);
		if (notesForm.getActions().equals("showAll")) {
		    request.setAttribute(NotesConstants.NOTES_LIST, notesBC.getNotes(notesForm.getModuleId(), notesForm.getModuleRefId(), notesForm.getItemName()));
		} else {
		    request.setAttribute(NotesConstants.NOTES_LIST, notesBC.getByNoteType(notesForm.getModuleId(), notesForm.getModuleRefId(), NotesConstants.NOTES_TYPE_MANUAL, notesForm.getItemName()));
		}
		notesForm.setShowVoidNotes(false);
		forward = NotesConstants.NOTES_PAGE;
	    } else if (NotesConstants.SET_AS_CLOSED.equals(buttonValue)) {
		Integer noteId = Integer.parseInt(notesForm.getNoteId());
		notesBC.setAsClosed(noteId);
		if (notesForm.getActions().equals("showAll")) {
		    request.setAttribute(NotesConstants.NOTES_LIST, notesBC.getNotes(notesForm.getModuleId(), notesForm.getModuleRefId(), notesForm.getItemName()));
		} else {
		    request.setAttribute(NotesConstants.NOTES_LIST, notesBC.getByNoteType(notesForm.getModuleId(), notesForm.getModuleRefId(), NotesConstants.NOTES_TYPE_MANUAL, notesForm.getItemName()));
		}
		notesForm.setShowVoidNotes(false);
		forward = NotesConstants.NOTES_PAGE;
	    } else if ("EditNotes".equalsIgnoreCase(buttonValue)) {
		if(CommonUtils.isNotEmpty(notesForm.getNoteId())){
		    Notes notes = new NotesDAO().findById(Integer.parseInt(notesForm.getNoteId()));
		    notesForm.setFollowupDate(null != notes.getFollowupDate() ? DateUtils.formatDate(notes.getFollowupDate(), "MM/dd/yyyy hh:mm") : "");
		    notesForm.setPrintOnReport(notes.isPrintOnReport());
		    notesForm.setNotes(notes.getNoteDesc());
		    notesForm.setActions("showAll");
		    request.setAttribute(NotesConstants.NOTES_LIST, notesBC.getNotes(notes.getModuleId(), notesForm.getModuleRefId(), notesForm.getItemName()));
		    notesForm.setShowVoidNotes(false);
		}
		forward = NotesConstants.NOTES_PAGE;
	    } else if ("Update".equalsIgnoreCase(buttonValue)) {
		User user = (User) session.getAttribute(NotesConstants.LOGIN_USER);
		Notes notes = new NotesDAO().findById(Integer.parseInt(notesForm.getNoteId()));
		notes.setFollowupDate(CommonUtils.isNotEmpty(notesForm.getFollowupDate()) ? DateUtils.parseDate(notesForm.getFollowupDate(), "MM/dd/yyyy hh:mm") : null);
		notes.setPrintOnReport(notesForm.isPrintOnReport());
		notes.setNoteDesc(notesForm.getNotes());
		notes.setUpdateDate(new Date());
		notes.setUpdatedBy(user.getLoginName());
		notesForm.setActions("showAll");
		request.setAttribute(NotesConstants.NOTES_LIST, notesBC.getNotes(notes.getModuleId(), notesForm.getModuleRefId(), notesForm.getItemName()));
		notesForm.setShowVoidNotes(false);
		forward = NotesConstants.NOTES_PAGE;
	    }
	    //To access disputed bl Notes and Acknowledge
	    if (CommonFunctions.isNotNull(request.getParameter(CommonConstants.ACKNOWLEDGE)) && CommonFunctions.isNotNull(itemName)
		    && NotesConstants.DISPUTEDBLCODE.equals(itemName)) {
		request.setAttribute(CommonConstants.ACKNOWLEDGE, request.getParameter(CommonConstants.ACKNOWLEDGE));
	    }
	    request.setAttribute("itemName", notesForm.getItemName());
	    request.setAttribute(NotesConstants.NOTES_FORM, notesForm);
	    return mapping.findForward(forward);
	}
    }
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.action;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.logisoft.bc.notes.NotesBC;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.Notes;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.logiware.form.EventForm;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.actions.DispatchAction;


public class LogiwareEventAction extends DispatchAction {

    public void registerEvent(ActionForm form, HttpServletRequest request, HttpServletResponse response)  throws Exception {
        EventForm eventForm = (EventForm) form;
        //Check for event
        //get user info
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginuser");
        if (null != user && null != eventForm.getEventCode() && !eventForm.getEventCode().trim().equals("")) {
            //log the event
            String eventDesc = CommonConstants.getEventMap().get(eventForm.getEventCode().trim());
            if (CommonFunctions.isNotNull(eventForm.getEventCode()) && !eventForm.getEventCode().equalsIgnoreCase(NotesConstants.CONVERTTOBOOKING)) {
                if (eventForm.getEventCode().equalsIgnoreCase(NotesConstants.MANIFESTCODE)
                        || eventForm.getEventCode().equalsIgnoreCase(NotesConstants.CONVERTTOBL)) {
                    if(CommonFunctions.isNotNull(eventForm.getEventDesc())){
                    	 eventDesc =(null!=eventDesc)?eventDesc+eventForm.getEventDesc():eventForm.getEventDesc();
                         eventDesc = (null != eventDesc) ? eventDesc.replace("-->", " ") : eventDesc;
                         eventDesc = (null != eventDesc) ? eventDesc.replace("<br>", " ") : eventDesc;
                    }
                    saveNotes(eventForm, user, eventDesc);
                    /*if (eventForm.getEventCode().equalsIgnoreCase(NotesConstants.MANIFESTCODE)) {
                        String realPath = this.getServlet().getServletContext().getRealPath("/");
                        new EventsBC().sendManifestEmail((FclBillLaddingForm) eventForm, user,realPath,request);
                    }*/
                } else if (eventForm.getEventCode().equalsIgnoreCase(NotesConstants.CORRECTIONNUMBER)) {
                    eventDesc += " " + eventForm.getEventDesc();
                    saveNotes(eventForm, user, eventDesc);
                }else {
                    saveNotes(eventForm, user, eventDesc);
                }
            } 
        }
    }

    public void saveNotes(EventForm eventForm, User user, String eventDesc) throws Exception {
        NotesBC notesBC = new NotesBC();
        Notes note = new Notes();
        note.setModuleId(eventForm.getModuleId());
        note.setModuleRefId(eventForm.getModuleRefId());
        note.setNoteType(NotesConstants.NOTES_TYPE_EVENT);
        note.setUpdateDate(new Date());
        note.setUpdatedBy(user.getLoginName());
        note.setItemName(eventForm.getEventCode());
        note.setNoteDesc(eventDesc);
      //  note.setNoteDesc(eventDesc + " on " + DateUtils.parseDateToString(new Date()) + " by " + user.getLoginName());
        notesBC.saveNotes(note);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclRemarksForm;
import com.gp.cvst.logisoft.struts.form.lcl.NcmNumberForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 *
 * @author Owner
 */
public class LclNotesAction extends DispatchAction {

    public ActionForward addLclNotes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclRemarksForm lclNotesForm = (LclRemarksForm) form;
        LclFileNumberDAO lclFileNumberDAO=new LclFileNumberDAO();
        return mapping.findForward("newLclNotes");
    }
}

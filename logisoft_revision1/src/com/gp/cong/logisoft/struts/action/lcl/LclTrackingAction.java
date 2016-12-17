/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.logisoft.domain.lcl.LclRemarks;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.gp.cvst.logisoft.struts.form.lcl.LclTrackingForm;
import java.util.List;
import org.apache.struts.actions.DispatchAction;

/**
 *
 * @author Owner
 */
public class LclTrackingAction extends DispatchAction {

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclTrackingForm lclTrackingForm = (LclTrackingForm) form;
        String[] type = {"T","Unit_Tracking"};
        List<LclRemarks> unitTrackList = new LclRemarksDAO().getAllRemarksByType(lclTrackingForm.getFileId(), type);
        request.setAttribute("remarksList", unitTrackList);
        request.setAttribute("lclTrackingForm", lclTrackingForm);
        return mapping.findForward("success");
    }
}

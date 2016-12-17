/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.Lcl3pRefNo;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cvst.logisoft.struts.form.lcl.HsCodeForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import com.gp.cong.logisoft.hibernate.dao.lcl.Lcl3pRefNoDAO;

/**
 *
 * @author Owner
 */
public class LclBookingWhseDetailsAction extends DispatchAction {

   public ActionForward addHsCode(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
       HsCodeForm hsCodeForm = (HsCodeForm)form;
       if (null != hsCodeForm.getLcl3pRefNo().getId() && hsCodeForm.getLcl3pRefNo().getId()!= 0) {
            Lcl3pRefNo lcl3pRefNo = new Lcl3pRefNoDAO().findById(hsCodeForm.getId());
            lcl3pRefNo.setReference(hsCodeForm.getLcl3pRefNo().getReference());
       }else{
       new Lcl3pRefNoDAO().save(hsCodeForm.getLcl3pRefNo());
       }
       hsCodeForm.setHsCode("");
       hsCodeForm.getLcl3pRefNo().setType(hsCodeForm.getRefType());
       request.setAttribute("allLclHsCodeList", new Lcl3pRefNoDAO().executeQuery("from Lcl3pRefNo where lclFileNumber.id= "+hsCodeForm.getLcl3pRefNo().getLclFileNumber().getId()+" AND type='HsCode'"));
       request.setAttribute("fileNumberId", hsCodeForm.getLcl3pRefNo().getLclFileNumber().getId());
       request.setAttribute("fileNumber", hsCodeForm.getLcl3pRefNo().getLclFileNumber().getFileNumber());
       return mapping.findForward("newHsCode");
    } 
    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
       HsCodeForm hsCodeForm = (HsCodeForm)form;
       request.setAttribute("allLclHsCodeList", new Lcl3pRefNoDAO().executeQuery("from Lcl3pRefNo where lclFileNumber.id= "+hsCodeForm.getLcl3pRefNo().getLclFileNumber().getId()+" AND type='HsCode'"));
       request.setAttribute("fileNumberId", hsCodeForm.getLcl3pRefNo().getLclFileNumber().getId());
       request.setAttribute("fileNumber", hsCodeForm.getLcl3pRefNo().getLclFileNumber().getFileNumber());
       return mapping.findForward("newHsCode");
    }
   public ActionForward closeNotes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HsCodeForm hsCodeForm = (HsCodeForm)form;
        Lcl3pRefNo lcl3pRefNo=new Lcl3pRefNoDAO().findById(hsCodeForm.getId());
        new Lcl3pRefNoDAO().delete(hsCodeForm.getId());
        request.setAttribute("allLclHsCodeList", new Lcl3pRefNoDAO().executeQuery("from Lcl3pRefNo where lclFileNumber.id= "+hsCodeForm.getLcl3pRefNo().getLclFileNumber().getId()+"  AND type='HsCode'"));
        request.setAttribute("fileNumberId", hsCodeForm.getLcl3pRefNo().getLclFileNumber().getId());
       request.setAttribute("fileNumber", hsCodeForm.getLcl3pRefNo().getLclFileNumber().getFileNumber());
        return mapping.findForward("newHsCode");
    }
}

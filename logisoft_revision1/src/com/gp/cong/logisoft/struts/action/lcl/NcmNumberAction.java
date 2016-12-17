/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.Lcl3pRefNo;
import com.gp.cong.logisoft.hibernate.dao.lcl.Lcl3pRefNoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
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
public class NcmNumberAction extends DispatchAction {

    public ActionForward addNcmNumber(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        NcmNumberForm ncmNumberForm = (NcmNumberForm) form;
        if (null != ncmNumberForm.getLcl3pRefNo().getId() && ncmNumberForm.getLcl3pRefNo().getId()!= 0) {
            Lcl3pRefNo lcl3pRefNo = new Lcl3pRefNoDAO().findById(ncmNumberForm.getId());
            lcl3pRefNo.setReference(ncmNumberForm.getLcl3pRefNo().getReference());
       }else{
        new Lcl3pRefNoDAO().save(ncmNumberForm.getLcl3pRefNo());}
         ncmNumberForm.setNcmNumaber("");
         ncmNumberForm.getLcl3pRefNo().setType(ncmNumberForm.getLcl3pRefNo().getType());
        request.setAttribute("allLclNcmNumberList", new Lcl3pRefNoDAO().executeQuery("from Lcl3pRefNo where lclFileNumber.id= "+ncmNumberForm.getLcl3pRefNo().getLclFileNumber().getId()+" AND type='NCM Number'"));
        request.setAttribute("fileNumberId", ncmNumberForm.getLcl3pRefNo().getLclFileNumber().getId());
        request.setAttribute("fileNumber", ncmNumberForm.getLcl3pRefNo().getLclFileNumber().getFileNumber());
        return mapping.findForward("newNcmNumber");
    }
     public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
       NcmNumberForm ncmNumberForm = (NcmNumberForm) form;
       request.setAttribute("allLclNcmNumberList", new Lcl3pRefNoDAO().executeQuery("from Lcl3pRefNo where lclFileNumber.id= "+ncmNumberForm.getLcl3pRefNo().getLclFileNumber().getId()+" AND type='NCM Number'"));
       request.setAttribute("fileNumberId", ncmNumberForm.getLcl3pRefNo().getLclFileNumber().getId());
       return mapping.findForward("newNcmNumber");
    }
    public ActionForward closeNotes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        NcmNumberForm ncmNumberForm = (NcmNumberForm) form;
        new Lcl3pRefNoDAO().delete(ncmNumberForm.getId());
        request.setAttribute("allLclNcmNumberList", new Lcl3pRefNoDAO().executeQuery("from Lcl3pRefNo where lclFileNumber.id= "+ncmNumberForm.getLcl3pRefNo().getLclFileNumber().getId()+" AND type='NCM Number'"));
        request.setAttribute("fileNumberId", ncmNumberForm.getLcl3pRefNo().getLclFileNumber().getId());
        return mapping.findForward("newNcmNumber");
    }
}

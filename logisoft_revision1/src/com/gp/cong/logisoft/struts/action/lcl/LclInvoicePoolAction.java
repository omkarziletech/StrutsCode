package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.beans.ImportsManifestBean;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclInvoicePoolDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclInvoicePoolForm;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class LclInvoicePoolAction extends LogiwareDispatchAction implements LclCommonConstant {

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclInvoicePoolForm lclInvoicePoolForm = (LclInvoicePoolForm) form;
        request.setAttribute("lclInvoicePoolForm", lclInvoicePoolForm);
        return mapping.findForward("success");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclInvoicePoolForm lclInvoicePoolForm = (LclInvoicePoolForm) form;
        LclInvoicePoolDAO lclInvoicePoolDAO = new LclInvoicePoolDAO();
        String fileType = "";
        if (LCL_IMPORT.equalsIgnoreCase(lclInvoicePoolForm.getModuleName())) {
            fileType = LCL_SHIPMENT_TYPE_IMPORT;
        } else {
            fileType = LCL_SHIPMENT_TYPE_EXPORT;
        }
        List<ImportsManifestBean> invoicePoolList = lclInvoicePoolDAO.getARInvoiceResult(fileType, lclInvoicePoolForm);
        request.setAttribute("invoicePoolList", invoicePoolList);
        request.setAttribute("lclInvoicePoolForm", lclInvoicePoolForm);
        return mapping.findForward("success");
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cvst.logisoft.domain.DocumentStoreLog;
import com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclImportPaymentForm;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author LogiwareInc
 */
public class LclScanViewDetailsAction extends LogiwareDispatchAction {

    public ActionForward displayScanDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclImportPaymentForm lclImportPaymentForm = (LclImportPaymentForm) form;
        DocumentStoreLogDAO documentStoreLogDAO = new DocumentStoreLogDAO();
        String appendValues=lclImportPaymentForm.getVendorNo()+"-"+lclImportPaymentForm.getInvoiceNo();
        List<DocumentStoreLog> documentList = documentStoreLogDAO.getDocumentStoreLogForViewAll(appendValues, "INVOICE");
        request.setAttribute("documentList", documentList);
        request.setAttribute("lclImportPaymentForm", lclImportPaymentForm);
        return mapping.findForward("sucess");
    }
}

package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.lcl.EculineEdiDao;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.lcl.bc.InvoiceUtils;
import com.gp.cong.logisoft.lcl.bc.BolUtils;
import com.gp.cvst.logisoft.struts.form.lcl.LclEculineEdiBlInfoForm;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Rajesh
 */
public class LclEculineEdiBlInfoAction extends LogiwareDispatchAction {

    public ActionForward openBol(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        BolUtils bolUtils = new BolUtils();
        bolUtils.bolDetails(request, id);
        String ecuId = new EculineEdiDao().getEcuId("id", id);
        request.setAttribute("ecuId", ecuId);
        request.setAttribute("readyToApproveFlag", request.getParameter("readyToApproveFlag"));
        return mapping.findForward("bol");
    }

    public ActionForward updateBol(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclEculineEdiBlInfoForm blInfoForm = (LclEculineEdiBlInfoForm) form;
        EculineEdiDao eculineEdiDao = new EculineEdiDao();
        BolUtils bolUtils = new BolUtils();
        User thisUser = getCurrentUser(request);
        bolUtils.updateBol(blInfoForm, request, thisUser);
        String ecuId = eculineEdiDao.getEcuId("id", blInfoForm.getId());
        bolUtils.voyageDetails(request, ecuId);
        return mapping.findForward("container");
    }

    public ActionForward approveBol(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclEculineEdiBlInfoForm blInfoForm = (LclEculineEdiBlInfoForm) form;
        EculineEdiDao eculineEdiDao = new EculineEdiDao();
        BolUtils bolUtils = new BolUtils();
        Map<String, String> values = new HashMap<String, String>();
        String ecuId = eculineEdiDao.getEcuId("id", blInfoForm.getId());
        User thisUser = getCurrentUser(request);
        bolUtils.updateBol(blInfoForm, request, thisUser);
        values.put("ecuId", ecuId);
        values.put("blId", blInfoForm.getId());
        bolUtils.approveBol(values, request, thisUser);
        bolUtils.bolDetails(request, blInfoForm.getId());
        request.setAttribute("ecuId", ecuId);
        return mapping.findForward("bol");
    }

    public ActionForward openInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String bol = request.getParameter("bol");
        String id = request.getParameter("id");
        String cntrId = request.getParameter("cntrId");
        String fileNumberId = request.getParameter("fileNumberId");
        InvoiceUtils invoiceUtils = new InvoiceUtils();
        invoiceUtils.invoiceDetails(bol, request, cntrId, null, fileNumberId);
        request.setAttribute("id", id);
        request.setAttribute("fileNumberId", fileNumberId);
        return mapping.findForward("invoice");
    }

    public ActionForward linkDRDisplay(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclEculineEdiBlInfoForm lclEculineEdiBlInfoForm = (LclEculineEdiBlInfoForm) form;
        request.setAttribute("lclEculineEdiBlInfoForm", lclEculineEdiBlInfoForm);
        return mapping.findForward("linkDrPopup");
    }

    public ActionForward savelinkDR(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclEculineEdiBlInfoForm lclEculineEdiBlInfoForm = (LclEculineEdiBlInfoForm) form;
        if (CommonUtils.isNotEmpty(lclEculineEdiBlInfoForm.getFileNumber())) {
            LclFileNumberDAO lclFilenumberDAO = new LclFileNumberDAO();
            BolUtils bolUtils = new BolUtils();
            bolUtils.checkBkgStatusSave(lclEculineEdiBlInfoForm, request, lclFilenumberDAO);
        }
        return mapping.findForward("linkDrPopup");
    }
}

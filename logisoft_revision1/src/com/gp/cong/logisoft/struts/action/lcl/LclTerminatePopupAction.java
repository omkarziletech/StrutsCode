package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.RoleDuty;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclConsolidateDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.RoleDutyDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclTerminatePopupForm;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class LclTerminatePopupAction extends LogiwareDispatchAction {

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclTerminatePopupForm lclTerminatePopupForm = (LclTerminatePopupForm) form;
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        request.setAttribute("fileId", lclTerminatePopupForm.getFileId());
        request.setAttribute("fileNumber", lclTerminatePopupForm.getFileNumber());
        RoleDuty roleDuty = new RoleDutyDAO().getRoleDetails(getCurrentUser(request).getRole().getRoleId());
        if (null!=roleDuty && roleDuty.isTerminateWithoutInvoice()) {
            request.setAttribute("arInvoiceFlag", lclCostChargeDAO.isChargeCodeValidate(lclTerminatePopupForm.getFileId(), "", "1"));
        }
        request.setAttribute("isConsolidate", new LclConsolidateDAO().isConsoildateFile(lclTerminatePopupForm.getFileId()));
        return mapping.findForward("dispalyTerminate");
    }
    public ActionForward displayBatchTerminate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        session.removeAttribute("batchTerminateFileList");
        return mapping.findForward("dispalyBatchTerminate");
    }
    public ActionForward addTermiateFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclTerminatePopupForm lclTerminatePopupForm = (LclTerminatePopupForm) form;
        HttpSession session = request.getSession();
        Map<String,String> fileMap = null != session.getAttribute("batchTerminateFileList")?  (Map<String,String>)session.getAttribute("batchTerminateFileList") : new HashMap<String, String>();
        if(CommonUtils.isNotEmpty(lclTerminatePopupForm.getFileNumber()) && CommonUtils.isNotEmpty(lclTerminatePopupForm.getFileId())){
            fileMap.put(lclTerminatePopupForm.getFileId(), lclTerminatePopupForm.getFileNumber());
        }
        session.setAttribute("batchTerminateFileList",fileMap);
        return mapping.findForward("batchTerminateFileList");
    }
    public ActionForward removeTermiateFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclTerminatePopupForm lclTerminatePopupForm = (LclTerminatePopupForm) form;
        HttpSession session = request.getSession();
        Map<String,String> fileMap = null != session.getAttribute("batchTerminateFileList")?  (Map<String,String>)session.getAttribute("batchTerminateFileList") : new HashMap<String, String>();
        if(CommonUtils.isNotEmpty(lclTerminatePopupForm.getFileId())){
            fileMap.remove(lclTerminatePopupForm.getFileId());
        }
        session.setAttribute("batchTerminateFileList",fileMap);
        return mapping.findForward("batchTerminateFileList");
    }
}

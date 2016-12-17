package com.gp.cvst.logisoft.struts.action;

import com.gp.cong.common.CommonUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.bc.accounting.APReconcilationBC;
import com.gp.cong.logisoft.bc.accounting.ReconcileConstants;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.struts.form.ReconcileForm;
import com.oreilly.servlet.ServletUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;

public class ReconcileAction extends DispatchAction {

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ReconcileForm reconcileForm = (ReconcileForm) form;
        reconcileForm.setReconcileTransactions(new APReconcilationBC().getReconcileTransactions(reconcileForm));
        return mapping.findForward("success");
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ReconcileForm reconcileForm = (ReconcileForm) form;
        new APReconcilationBC().saveReconcileInProcess(reconcileForm.getSelectedIds(), reconcileForm.getUnSelectedIds());
        reconcileForm.setReconcileTransactions(new APReconcilationBC().getReconcileTransactions(reconcileForm));
        return mapping.findForward("success");
    }

    public ActionForward reconcile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ReconcileForm reconcileForm = (ReconcileForm) form;
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        new APReconcilationBC().reconcile(reconcileForm, loginUser);
        reconcileForm.reset(mapping, request);
        return mapping.findForward("success");
    }

    public ActionForward downloadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ReconcileForm reconcileForm = (ReconcileForm) form;
        StringBuilder fileName = new StringBuilder(LoadLogisoftProperties.getProperty("reportLocation")).append("/GeneralLedger/");
        File file = new File(fileName.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        fileName.append("Reconcile.xls");
        new APReconcilationBC().downloadFile(reconcileForm,fileName.toString());
        response.addHeader("Content-Disposition", "attachment; filename=" + FilenameUtils.getName(fileName.toString()));
        response.setContentType("application/vnd.ms-excel" + ";charset=utf-8");
        ServletUtils.returnFile(fileName.toString(), response.getOutputStream());
        return null;
    }

    public ActionForward uploadCSVFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ReconcileForm reconcileForm = (ReconcileForm) form;
        String path = LoadLogisoftProperties.getProperty("reportLocation");
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        FormFile file = reconcileForm.getBankCSVFile();
        File outFileName = new File(path + "/" + file.getFileName());
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(outFileName));
        BufferedInputStream bufferedInputStream = new BufferedInputStream(file.getInputStream());
        byte[] buff = new byte[1048576];
        int bytesRead;
        while (-1 != (bytesRead = bufferedInputStream.read(buff, 0, buff.length))) {
            bufferedOutputStream.write(buff, 0, bytesRead);
        }
        new APReconcilationBC().uploadFile(outFileName.getName(), outFileName.getPath());
        if (bufferedInputStream != null) {
            bufferedInputStream.close();
        }
        if (bufferedOutputStream != null) {
            bufferedOutputStream.close();
        }
        request.setAttribute("reconcileForm", reconcileForm);
        return mapping.findForward("success");
    }

    public ActionForward refresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ReconcileForm reconcileForm = new ReconcileForm();
        request.setAttribute("reconcileForm", reconcileForm);
        return mapping.findForward("success");
    }
}

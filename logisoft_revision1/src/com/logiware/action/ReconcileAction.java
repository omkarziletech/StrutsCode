package com.logiware.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.User;
import com.logiware.accounting.form.SessionForm;
import com.logiware.form.ReconcileForm;
import com.logiware.hibernate.dao.ReconcileDAO;
import com.logiware.utils.GlBatchUtils;
import com.oreilly.servlet.ServletUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 *
 * @author Lakshmi Naryanan
 */
public class ReconcileAction extends DispatchAction {

    private static final String SUCCESS = "success";
    private static final String JOURNAL_ENTRY = "journalEntry";

    public ActionForward refresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("reconcileForm", new ReconcileForm());
        request.getSession().removeAttribute("oldReconcileForm");
        return mapping.findForward(SUCCESS);
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ReconcileForm reconcileForm = (ReconcileForm) form;
        new ReconcileDAO().search(reconcileForm);
        request.getSession().removeAttribute("oldReconcileForm");
        return mapping.findForward(SUCCESS);
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ReconcileForm reconcileForm = (ReconcileForm) form;
        new ReconcileDAO().save(reconcileForm);
        request.setAttribute("message", reconcileForm.getGlAccount() + " is saved in progress successfully");
        request.getSession().removeAttribute("oldReconcileForm");
        return mapping.findForward(SUCCESS);
    }

    public ActionForward reconcile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        ReconcileForm reconcileForm = (ReconcileForm) form;
        new ReconcileDAO().reconcile(reconcileForm, loginUser);
        request.setAttribute("message", reconcileForm.getGlAccount() + " reconciled successfully");
        request.getSession().removeAttribute("oldReconcileForm");
        return mapping.findForward(SUCCESS);
    }

    public ActionForward downloadReconcileFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ReconcileForm reconcileForm = (ReconcileForm) form;
        String fileName = reconcileForm.getFileName();
        if (CommonUtils.isNotEmpty(fileName)) {
            response.addHeader("Content-Disposition", "attachment; filename=" + FilenameUtils.getName(fileName));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            ServletUtils.returnFile(fileName, response.getOutputStream());
            return null;
        }
        return mapping.findForward(SUCCESS);
    }

    public ActionForward importTemplate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ReconcileForm reconcileForm = (ReconcileForm) form;
        new ReconcileDAO().importTemplate(reconcileForm);
        request.getSession().removeAttribute("oldReconcileForm");
        return mapping.findForward(SUCCESS);
    }

    public ActionForward downloadExceptionFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ReconcileForm reconcileForm = (ReconcileForm) form;
        String exceptionFileName = reconcileForm.getExceptionFileName();
        if (CommonUtils.isNotEmpty(exceptionFileName)) {
            response.addHeader("Content-Disposition", "attachment; filename=" + FilenameUtils.getName(exceptionFileName));
            response.setContentType("application/octet-stream;charset=utf-8");
            ServletUtils.returnFile(exceptionFileName, response.getOutputStream());
            return null;
        }
        return mapping.findForward(SUCCESS);
    }

    public ActionForward createJournalEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ReconcileForm reconcileForm = (ReconcileForm) form;
        SessionForm oldReconcileForm = new SessionForm();
        PropertyUtils.copyProperties(oldReconcileForm, reconcileForm);
        request.getSession().setAttribute("oldReconcileForm", oldReconcileForm);
        GlBatchUtils.createReconcileJournalEntry(reconcileForm, request);
        return mapping.findForward(JOURNAL_ENTRY);
    }

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return refresh(mapping, form, request, response);
    }
}

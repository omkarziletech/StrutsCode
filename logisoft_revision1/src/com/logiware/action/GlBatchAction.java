package com.logiware.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cvst.logisoft.domain.Batch;
import com.gp.cvst.logisoft.domain.FiscalYear;
import com.logiware.accounting.dao.FiscalPeriodDAO;
import com.logiware.accounting.form.FiscalPeriodForm;
import com.logiware.accounting.form.SessionForm;
import com.logiware.bean.ReconcileModel;
import com.logiware.form.GlBatchForm;
import com.logiware.form.ReconcileForm;
import com.logiware.hibernate.dao.GlBatchDAO;
import com.logiware.hibernate.dao.ReconcileDAO;
import com.logiware.utils.GlBatchUtils;
import com.logiware.utils.JournalEntryUploader;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public class GlBatchAction extends DispatchAction
        implements ConstantsInterface {

    private static final String GL_BATCH = "glBatch";
    private static final String JOURNAL_ENTRY = "journalEntry";
    private static final String RECONCILE = "reconcile";
    private static final String FISCAL_PERIOD = "fiscalPeriod";

    public ActionForward searchBatch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlBatchForm glBatchForm = (GlBatchForm) form;
        GlBatchDAO glBatchDAO = new GlBatchDAO();
        String conditions = glBatchDAO.buildQuery(glBatchForm);
        Integer totalPageSize = glBatchDAO.getTotal(conditions);
        int noOfPages = totalPageSize / glBatchForm.getCurrentPageSize();
        int remainSize = totalPageSize % glBatchForm.getCurrentPageSize();
        if (remainSize > 0) {
            noOfPages++;
        }
        int start = glBatchForm.getCurrentPageSize() * (glBatchForm.getPageNo() - 1);
        int end = glBatchForm.getCurrentPageSize();
        glBatchForm.setNoOfPages(Integer.valueOf(noOfPages));
        glBatchForm.setTotalPageSize(totalPageSize);
        List glBatches = glBatchDAO.search(conditions, glBatchForm.getSortBy(), glBatchForm.getOrderBy(), start, end);
        glBatchForm.setNoOfRecords(Integer.valueOf(glBatches.size()));
        glBatchForm.setGlBatches(glBatches);
        return mapping.findForward(GL_BATCH);
    }

    public ActionForward addBatch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlBatchForm glBatchForm = (GlBatchForm) form;
        GlBatchUtils.addBatch(glBatchForm, request);
        return mapping.findForward(JOURNAL_ENTRY);
    }

    public ActionForward editBatch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlBatchForm glBatchForm = (GlBatchForm) form;
        GlBatchUtils.editBatch(glBatchForm, request);
        return mapping.findForward(JOURNAL_ENTRY);
    }

    public ActionForward gotoBatch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlBatchForm glBatchForm = (GlBatchForm) form;
        GlBatchUtils.gotoBatch(glBatchForm, request);
        return mapping.findForward(JOURNAL_ENTRY);
    }

    public ActionForward gotoJournalEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlBatchForm glBatchForm = (GlBatchForm) form;
        GlBatchUtils.gotoJournalEntry(glBatchForm, request);
        return mapping.findForward(JOURNAL_ENTRY);
    }

    public ActionForward saveOrUpdateBatch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlBatchForm glBatchForm = (GlBatchForm) form;
        GlBatchUtils.saveOrUpdate(glBatchForm, request);
        return mapping.findForward(JOURNAL_ENTRY);
    }

    public ActionForward autoReverseBatch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlBatchForm glBatchForm = (GlBatchForm) form;
        GlBatchUtils.autoReverseBatch(glBatchForm, request);
        return mapping.findForward(JOURNAL_ENTRY);
    }

    public ActionForward reverseBatch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlBatchForm glBatchForm = (GlBatchForm) form;
        GlBatchUtils.reverseBatch(glBatchForm, request);
        return mapping.findForward(JOURNAL_ENTRY);
    }

    public ActionForward copyBatch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlBatchForm glBatchForm = (GlBatchForm) form;
        Batch batch = new GlBatchDAO().findById(Integer.valueOf(Integer.parseInt(glBatchForm.getGlBatch().getId())));
        if (CommonUtils.isEqualIgnoreCase(batch.getStatus(), "posted")) {
            GlBatchUtils.copyPostedBatch(glBatchForm, request);
        } else {
            GlBatchUtils.copyOpenBatch(glBatchForm, request);
        }
        return mapping.findForward(JOURNAL_ENTRY);
    }

    public ActionForward addJournalEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlBatchForm glBatchForm = (GlBatchForm) form;
        GlBatchUtils.saveOrUpdate(glBatchForm, request);
        GlBatchUtils.addJournalEntry(glBatchForm, request);
        return mapping.findForward(JOURNAL_ENTRY);
    }

    public ActionForward deleteJournalEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlBatchForm glBatchForm = (GlBatchForm) form;
        GlBatchUtils.deleteJournalEntry(glBatchForm, request);
        return mapping.findForward(JOURNAL_ENTRY);
    }

    public ActionForward postBatch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlBatchForm glBatchForm = (GlBatchForm) form;
        GlBatchUtils.postBatch(glBatchForm, request);
        glBatchForm.setBatchId("");
        return searchBatch(mapping, form, request, response);
    }

    public ActionForward voidBatch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlBatchForm glBatchForm = (GlBatchForm) form;
        GlBatchUtils.voidBatch(glBatchForm, request);
        return searchBatch(mapping, form, request, response);
    }

    public ActionForward importBatch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlBatchForm glBatchForm = (GlBatchForm) form;
        if (CommonUtils.isNotEmpty(glBatchForm.getJournalEntry().getId())) {
            GlBatchUtils.saveOrUpdate(glBatchForm, request);
        }
        new JournalEntryUploader().importBatch(glBatchForm, request);
        return mapping.findForward(JOURNAL_ENTRY);
    }

    public ActionForward importJournalEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlBatchForm glBatchForm = (GlBatchForm) form;
        if (CommonUtils.isNotEmpty(glBatchForm.getJournalEntry().getId())) {
            GlBatchUtils.saveOrUpdate(glBatchForm, request);
        }
        new JournalEntryUploader().importJournalEntry(glBatchForm, request);
        return mapping.findForward(JOURNAL_ENTRY);
    }

    public ActionForward printBatch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlBatchForm glBatchForm = (GlBatchForm) form;
        Integer batchId = Integer.valueOf(Integer.parseInt(glBatchForm.getGlBatch().getId()));
        String contextPath = getServlet().getServletContext().getRealPath("/");
        GlBatchUtils.printBatch(contextPath, batchId, response);
        return null;
    }

    public ActionForward exportBatch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlBatchForm glBatchForm = (GlBatchForm) form;
        Integer batchId = Integer.valueOf(Integer.parseInt(glBatchForm.getGlBatch().getId()));
        GlBatchUtils.exportBatch(batchId, response);
        return null;
    }

    public ActionForward printJournalEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlBatchForm glBatchForm = (GlBatchForm) form;
        String journalEntryId = glBatchForm.getJournalEntry().getId();
        String contextPath = getServlet().getServletContext().getRealPath("/");
        GlBatchUtils.printJournalEntry(contextPath, journalEntryId, response);
        return null;
    }

    public ActionForward exportJournalEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlBatchForm glBatchForm = (GlBatchForm) form;
        String journalEntryId = glBatchForm.getJournalEntry().getId();
        GlBatchUtils.exportJournalEntry(journalEntryId, response);
        return null;
    }

    public ActionForward printHistory(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlBatchForm glBatchForm = (GlBatchForm) form;
        String journalEntryId = glBatchForm.getJournalEntry().getId();
        String contextPath = getServlet().getServletContext().getRealPath("/");
        GlBatchUtils.printHistory(contextPath, journalEntryId, response);
        return null;
    }

    public ActionForward exportHistory(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlBatchForm glBatchForm = (GlBatchForm) form;
        String journalEntryId = glBatchForm.getJournalEntry().getId();
        GlBatchUtils.exportHistory(journalEntryId, response);
        return null;
    }

    public ActionForward reset(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("glBatchForm", new GlBatchForm());
        return mapping.findForward(GL_BATCH);
    }

    public ActionForward goBackToReconcile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionForm oldReconcileForm = (SessionForm) request.getSession().getAttribute("oldReconcileForm");
        ReconcileForm reconcileForm = new ReconcileForm();
        PropertyUtils.copyProperties(reconcileForm, oldReconcileForm);
        request.setAttribute("reconcileForm", reconcileForm);
        return mapping.findForward(RECONCILE);
    }

    public ActionForward postAndGoBackToReconcile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlBatchForm glBatchForm = (GlBatchForm) form;
        GlBatchUtils.saveAndPost(glBatchForm, request);
        String error = (String) request.getAttribute("error");
        if (CommonUtils.isNotEmpty(error) && error.startsWith("Cannot post Batch - ")) {
            return mapping.findForward(JOURNAL_ENTRY);
        } else {
            SessionForm oldReconcileForm = (SessionForm) request.getSession().getAttribute("oldReconcileForm");
            ReconcileForm reconcileForm = new ReconcileForm();
            PropertyUtils.copyProperties(reconcileForm, oldReconcileForm);
            String batchId = glBatchForm.getGlBatch().getId();
            String glAccount = reconcileForm.getGlAccount();
            String reconcilePeriod = DateUtils.formatDate(DateUtils.parseDate(reconcileForm.getReconcileDate(), "MM/dd/yyyy"), "yyyyMM");
            List<ReconcileModel> transactions = new ReconcileDAO().getJeTransactions(batchId, glAccount, reconcilePeriod);
            reconcileForm.setTransactions(transactions);
            oldReconcileForm.setTransactions(transactions);
            request.setAttribute("reconcileForm", reconcileForm);
            request.getSession().setAttribute("oldReconcileForm", oldReconcileForm);
            return mapping.findForward(RECONCILE);
        }
    }

    public ActionForward postAndCloseYear(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlBatchForm glBatchForm = (GlBatchForm) form;
        GlBatchUtils.saveAndPost(glBatchForm, request);
        String error = (String) request.getAttribute("error");
        if (CommonUtils.isNotEmpty(error) && error.startsWith("Cannot post Batch - ")) {
            return mapping.findForward(JOURNAL_ENTRY);
        } else {
            FiscalPeriodForm fpform = new FiscalPeriodForm();
            FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
            FiscalYear fiscalYear = fiscalPeriodDAO.getFiscalYear(glBatchForm.getYear());
            fiscalPeriodDAO.closeYear(fiscalYear);
            fpform.setFiscalYear(fiscalPeriodDAO.getFiscalYear(glBatchForm.getYear()));
            fpform.setBudgetSet(fiscalPeriodDAO.getBudgetSet(glBatchForm.getYear()));
            fpform.setFiscalPeriods(fiscalPeriodDAO.getAllFiscalPeriods(glBatchForm.getYear()));
            request.setAttribute("message", "Year - " + fpform.getFiscalYear().getYear() + " is closed successfully.");
            request.setAttribute("fiscalPeriodForm", fpform);
            return mapping.findForward(FISCAL_PERIOD);
        }
    }

    public ActionForward deleteAndGoBackToFiscalPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlBatchForm glBatchForm = (GlBatchForm) form;
        GlBatchUtils.voidBatch(glBatchForm, request);
        FiscalPeriodForm fpform = new FiscalPeriodForm();
        FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
        fpform.setAction(glBatchForm.getAction());
        fpform.setFiscalYear(fiscalPeriodDAO.getFiscalYear(glBatchForm.getYear()));
        fpform.setBudgetSet(fiscalPeriodDAO.getBudgetSet(glBatchForm.getYear()));
        fpform.setFiscalPeriods(fiscalPeriodDAO.getAllFiscalPeriods(glBatchForm.getYear()));
        request.setAttribute("fiscalPeriodForm", fpform);
        return mapping.findForward(FISCAL_PERIOD);
    }

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return searchBatch(mapping, form, request, response);
    }
}

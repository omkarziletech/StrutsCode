package com.logiware.dwr;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.BankDetails;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.hibernate.dao.BankDetailsDAO;
import com.gp.cvst.logisoft.hibernate.dao.FiscalPeriodDAO;
import com.logiware.common.form.FileUploadForm;
import com.logiware.hibernate.dao.AccountingLedgerDAO;
import com.logiware.hibernate.dao.GlBatchDAO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.struts.upload.FormFile;
import org.directwebremoting.WebContextFactory;

public class GeneralLedgerDwr {

    public String validateGlAccountForSubledgers(String subLedgerType, String startDate, String endDate)
            throws IOException, ServletException, Exception {
        List transactions = new AccountingLedgerDAO().getInvalidSubledgersForPosting(subLedgerType, startDate, endDate);
        if (CommonUtils.isNotEmpty(transactions)) {
            HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
            request.setAttribute("transactions", transactions);
            return WebContextFactory.get().forwardToString("/jsps/GeneralLedger/invalidGLAccountsForSubledger.jsp");
        }
        return "canPost";
    }

    public void showCopyBatchPopup(String batchId, HttpServletRequest request) throws ServletException, IOException, Exception {
        request.setAttribute("batchId", batchId);
        request.setAttribute("openBatchIds", new GlBatchDAO().getOpenBatches(Integer.parseInt(batchId)));
    }

    public void showReverseBatchPopup(String batchId, HttpServletRequest request) throws ServletException, IOException, Exception {
        request.setAttribute("batchId", batchId);
        request.setAttribute("openBatchIds", new GlBatchDAO().getOpenBatches(Integer.parseInt(batchId)));
    }

    public String getSubledgerDescription(Integer id) throws Exception {
        GenericCode subledgerCode = new GenericCodeDAO().findById(id);
        return subledgerCode.getCodedesc();
    }

    public void showDrillDownForLineItem(String lineItemId, HttpServletRequest request) throws ServletException, IOException {
        List drillDownDetials = new GlBatchDAO().getDrillDownDetailsForLineItem(lineItemId);
        request.setAttribute("drillDownDetials", drillDownDetials);
    }

    public boolean updateGLAccount(String glAccountNumber, Integer id) throws Exception {
        TransactionLedger ledger = new AccountingLedgerDAO().findById(id);
        ledger.setGlAccountNumber(glAccountNumber);
        return true;
    }

    public boolean isPeriodOpenForThisDate(String javaDate) throws Exception {
        String sqlDate = DateUtils.formatDate(DateUtils.parseDate(javaDate, "MM/dd/yyyy"), "yyyy-MM-dd");
        return new FiscalPeriodDAO().isPeriodOpenForThisDate(sqlDate);
    }

    public List<BankDetails> getBankAccounts(String bankName, HttpServletRequest request) throws Exception {
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        return new BankDetailsDAO().getBankAccounts(loginUser.getUserId(), bankName);
    }

    public Integer getStartingNumber(String bankName, String bankAccountNo) {
        return new BankDetailsDAO().getStartingNumber(bankName, bankAccountNo);
    }

    public String importBatchFile(FileUploadForm fileUploadForm) throws Exception {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            FormFile file = fileUploadForm.getFile();
            StringBuilder fileName = new StringBuilder();
            fileName.append(LoadLogisoftProperties.getProperty("reportLocation"));
            fileName.append("/Documents/JournalEntry/Import/");
            fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
            fileName.append(file.getFileName());
            File template = new File(fileName.toString());
            inputStream = file.getInputStream();
            outputStream = FileUtils.openOutputStream(template);
            IOUtils.copy(inputStream, outputStream);
            return fileName.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }

    public String importJournalEntryFile(FileUploadForm fileUploadForm) throws Exception {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            FormFile file = fileUploadForm.getFile();
            System.out.println("Filename === "+file.getFileName());
            StringBuilder fileName = new StringBuilder();
            fileName.append(LoadLogisoftProperties.getProperty("reportLocation"));
            fileName.append("/Documents/JournalEntry/Import/");
            fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
            fileName.append(file.getFileName());
            File template = new File(fileName.toString());
            inputStream = file.getInputStream();
            outputStream = FileUtils.openOutputStream(template);
            IOUtils.copy(inputStream, outputStream);
            return fileName.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }

    public String importReconcileTemplate(FileUploadForm fileUploadForm) throws Exception {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            FormFile file = fileUploadForm.getFile();
            StringBuilder fileName = new StringBuilder();
            fileName.append(LoadLogisoftProperties.getProperty("reportLocation"));
            fileName.append("/Documents/Reconcile/Import/");
            fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
            fileName.append(file.getFileName());
            File template = new File(fileName.toString());
            inputStream = file.getInputStream();
            outputStream = FileUtils.openOutputStream(template);
            IOUtils.copy(inputStream, outputStream);
            return fileName.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }
}

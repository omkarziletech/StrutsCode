package com.gp.cong.logisoft.bc.accounting;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.ExcelGenerator.ExportSubLedgerToExcel;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.reports.JournalEntryPdfCreator;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.beans.TransactionBean;
import com.gp.cvst.logisoft.domain.Batch;
import com.gp.cvst.logisoft.domain.FiscalPeriod;
import com.gp.cvst.logisoft.domain.JournalEntry;
import com.gp.cvst.logisoft.hibernate.dao.BatchDAO;
import com.gp.cvst.logisoft.hibernate.dao.FiscalPeriodDAO;
import com.gp.cvst.logisoft.hibernate.dao.JournalEntryDAO;
import com.gp.cvst.logisoft.struts.form.RecieptsLedgerForm;
import com.logiware.constants.SubledgerConstant;
import com.logiware.hibernate.dao.AccountingLedgerDAO;
import java.io.File;
import java.util.Date;
import java.util.List;

public class JournalEntryBC {

	BatchDAO batchDAO = new BatchDAO();
	EmailschedulerDAO emailschedulerDAO = new EmailschedulerDAO();

	public void createJournalEntryReport(String batchId, String fileName, String contextPath) throws Exception {
		Batch batch = batchDAO.findById(batchId);
		JournalEntryPdfCreator journalEntryPdfCreator = new JournalEntryPdfCreator();
		journalEntryPdfCreator.createReport(batch, fileName, contextPath);
	}

	public void save(EmailSchedulerVO emailSchedulerVO)  throws Exception {
		emailschedulerDAO.save(emailSchedulerVO);
	}

	public String exportToExcel(String journalEntryId)  throws Exception {
		JournalEntry journalEntry = new JournalEntryDAO().findById(journalEntryId);
		if (CommonUtils.isEqualIgnoreCase(journalEntry.getSubledgerClose(), CommonConstants.YES)) {
			RecieptsLedgerForm recieptsLedgerForm = new RecieptsLedgerForm();
			recieptsLedgerForm.setSubLedgerType(journalEntry.getSourceCode().getCode());
			List<FiscalPeriod> fiscalPeriods = new FiscalPeriodDAO().findByPeriodDis(journalEntry.getPeriod());
			recieptsLedgerForm.setPeriod(fiscalPeriods.get(0).getId().toString());
			//recieptsLedgerForm.setStartDate();
			//recieptsLedgerForm.setEndDate();
			if (CommonUtils.isEqualIgnoreCase(journalEntry.getSourceCode().getCode(), SubledgerConstant.PJ)) {
				recieptsLedgerForm.setSortBy(CommonConstants.SORT_BY_CHARGECODE);
			} else {
				recieptsLedgerForm.setSortBy(CommonConstants.SORT_BY_GL_ACCOUNT);
			}
			List<TransactionBean> subledgers = new AccountingLedgerDAO().getSubledgersForJournalEntry(journalEntryId);
                        String excelFilePath = LoadLogisoftProperties.getProperty("reportLocation") + "/Documents/JournalEntry/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd");
			File file = new File(excelFilePath);
			if (!file.exists()) {
				file.mkdirs();
			}
			excelFilePath += "/JournalEntrySubledgerHistory.xls";
			return new ExportSubLedgerToExcel().exportToExcel(excelFilePath, recieptsLedgerForm, subledgers);
		} else {
			return null;
		}
	}
}

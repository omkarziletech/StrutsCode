package com.gp.cong.logisoft.bc.accounting;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.logisoft.ExcelGenerator.ExportSubLedgerToExcel;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.reports.ReceiptsLedgerPdfCreator;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.AccountingConstants;
import com.gp.cvst.logisoft.beans.TransactionBean;
import com.gp.cvst.logisoft.domain.Batch;
import com.gp.cvst.logisoft.domain.FiscalPeriod;
import com.gp.cvst.logisoft.domain.JournalEntry;
import com.gp.cvst.logisoft.domain.LineItem;
import com.gp.cvst.logisoft.hibernate.dao.AccountDetailsDAO;
import com.gp.cvst.logisoft.hibernate.dao.BatchDAO;
import com.gp.cvst.logisoft.hibernate.dao.FiscalPeriodDAO;
import com.gp.cvst.logisoft.hibernate.dao.JournalEntryDAO;
import com.gp.cvst.logisoft.hibernate.dao.LineItemDAO;
import com.gp.cvst.logisoft.hibernate.dao.SubledgerAcctsDAO;
import com.gp.cvst.logisoft.hibernate.dao.SubledgerDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionLedgerDAO;
import com.gp.cvst.logisoft.struts.form.RecieptsLedgerForm;

/**
 * @author Pradeep Pamuru Created on 19/05/2009 In Application Receipts Ledger
 *         is Know as Sub ledger(In AR Module)
 * 
 */
public class ReceiptsLedgerBc {


    /**
     * @param period
     * @return
     */
    public String createBatchForSubledger(String period)throws Exception {
        BatchDAO bdao = new BatchDAO();
        String batchDesc = "Subledger Close for Period" + period;
        Batch batch = new Batch();
        batch.setBatchDesc(batchDesc);
        batch.setReadyToPost("yes");
        batch.setStatus("ready to post");
        batch.setTotalCredit(new Double("0.00"));
        batch.setTotalDebit(new Double("0.00"));
        batch = bdao.saveAndReturn(batch);
        return batch.getBatchId().toString();
    }

    /**
     * @param period
     * @param batchNo
     * @param jeInteger
     * @param subledgerCode
     * @return
     */
    public String createJournalEntry(String period, String batchNo,
            Integer jeInteger, String subledgerCode)throws Exception {
        NumberFormat number = new DecimalFormat("000");
        String jeSequenceNumber = number.format(jeInteger);
        String jeId = batchNo + "-" + jeSequenceNumber;
        JournalEntry je = new JournalEntry();
        JournalEntryDAO jeDAO = new JournalEntryDAO();
        je.setJournalEntryId(jeId);
        je.setBatchId(Integer.parseInt(batchNo));
        je.setMemo("Receipts");
        je.setCredit(0.0);
        je.setDebit(0.0);
        je.setPeriod(period);
        je.setJournalEntryDesc(subledgerCode + " SubLedger Close for Period "
                + period);
        je.setJeDate(new Date());
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        GenericCode genericCode = genericCodeDAO.findByCodeName(subledgerCode,
                33);
        je.setSourceCode(genericCode);
        je.setSourceCodeDesc(genericCode.getCodedesc());
        jeDAO.save(je);

        return jeId;
    }


    public LineItem setCreditAndDebitBeforeInsertLineItems(LineItem line,
            String amount, String code) {
        if (amount == null) {
            amount = "0.0";
        }
        if (amount.contains(",")) {
            amount = amount.replaceAll(",", "");
        }
        if (code.equals(AccountingConstants.CODE_01)) {// Debit
            line.setCredit(0d);
            line.setDebit(Double.parseDouble(amount));
        } else {// Credit
            line.setCredit(Double.parseDouble(amount));
            line.setDebit(0d);
        }
        return line;
    }

    public String generateLineId(String jeId, Integer lineSequenceNumber)throws Exception {
        String lineItemId = jeId;
        NumberFormat number = new DecimalFormat("000");
        /*
         * if (lineSequenceNumber >= 0 && lineSequenceNumber < 10) { lineItemId =
         * jeId + "-" + "00" + lineSequenceNumber; } else if (lineSequenceNumber >=
         * 10 && lineSequenceNumber < 100) { lineItemId = jeId + "-" + "0" +
         * lineSequenceNumber; } else { lineItemId = jeId + "-" +
         * lineSequenceNumber; }
         */
            lineItemId = lineItemId + "-" + number.format(lineSequenceNumber);
        return lineItemId;
    }

    public String generateReport(RecieptsLedgerForm recieptsLedgerForm, List<TransactionBean> subLedgerList, String realPath)throws Exception {
        ReceiptsLedgerPdfCreator receiptsLedgerPdfCreator = new ReceiptsLedgerPdfCreator();
        String reportFileName = LoadLogisoftProperties.getProperty("reportLocation");
        File file = new File(reportFileName);
        if (!file.exists()) {
            file.mkdir();
        }
        reportFileName = reportFileName + "/" + CommonConstants.SUB_LEDGER_REPORT + ".pdf";
        return receiptsLedgerPdfCreator.generateReport(recieptsLedgerForm, subLedgerList, realPath, reportFileName);
    }

    public String generateExcelSheet(RecieptsLedgerForm recieptsLedgerForm, List<TransactionBean> subLedgerList)throws Exception {
        ExportSubLedgerToExcel exportSubLedgerToExcel = new ExportSubLedgerToExcel();
        String excelFilePath = LoadLogisoftProperties.getProperty("reportLocation");
        File file = new File(excelFilePath);
        if (!file.exists()) {
            file.mkdir();
        }
        excelFilePath = excelFilePath + "/" + CommonConstants.SUB_LEDGER_REPORT + ".xls";
        return exportSubLedgerToExcel.exportToExcel(excelFilePath, recieptsLedgerForm, subLedgerList);
    }
}

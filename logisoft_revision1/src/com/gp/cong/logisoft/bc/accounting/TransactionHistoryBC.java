/**
 * 
 */
package com.gp.cong.logisoft.bc.accounting;

import com.gp.cong.logisoft.reports.TransactionHistoryPdfCreator;
import com.gp.cong.logisoft.reports.dto.TransactionHistoryReportDTO;

/**
 * @author Administrator
 *
 */
public class TransactionHistoryBC {
	 
	public void createTransactionHistoryReport(String outputFileName,String realPath,TransactionHistoryReportDTO transactionHistoryVO) throws Exception {
	TransactionHistoryPdfCreator transactionHistoryPdfCreator=new TransactionHistoryPdfCreator();
	transactionHistoryPdfCreator.createReport(transactionHistoryVO, outputFileName, realPath);
	}

}

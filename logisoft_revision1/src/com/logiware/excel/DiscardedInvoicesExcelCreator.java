package com.logiware.excel;

import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.bean.PaymentBean;
import java.io.File;
import java.util.List;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 *
 * @author Lakshmi Naryanan
 */
public class DiscardedInvoicesExcelCreator extends BaseExcelCreator {

    private void writeContents(List<PaymentBean> discardedInvoices) throws Exception {
	createRow();
	resetColumnIndex();
	createHeaderCell("Type", tableHeaderCellStyleCenterBold);
	createHeaderCell("Invoice", tableHeaderCellStyleCenterBold);
	createHeaderCell("Amount", tableHeaderCellStyleCenterBold);
	createHeaderCell("Comments", tableHeaderCellStyleCenterBold);
	int rowCount = 0;
	for (PaymentBean discardedInvoice : discardedInvoices) {
	    createRow();
	    resetColumnIndex();
	    CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
	    CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
	    createTextCell(discardedInvoice.getTransactionType(), textCellStyle);
	    createTextCell(discardedInvoice.getInvoiceOrBl(), textCellStyle);
	    createDoubleCell(discardedInvoice.getPaidAmount(), doubleCellStyle);
	    createTextCell(discardedInvoice.getComments(), textCellStyle);
	    rowCount++;
	}
	setColumnAutoSize();
    }

    public String createExcel(String customerNumber, List<PaymentBean> discardedInvoices)throws Exception {
	try {
	    StringBuilder fileNameBuilder = new StringBuilder(LoadLogisoftProperties.getProperty("reportLocation"));
	    fileNameBuilder.append("/ArBatch/Import/");
	    File file = new File(fileNameBuilder.toString());
	    if (!file.exists()) {
		file.mkdirs();
	    }
	    fileNameBuilder.append("Discarded_").append(customerNumber).append(".xlsx");
	    init(fileNameBuilder.toString(), customerNumber);
	    writeContents(discardedInvoices);
            writeIntoFile();
	    return fileNameBuilder.toString();
	} catch (Exception e) {
	    throw e;
	} finally {
	    exit();
	}
    }
}

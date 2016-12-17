package com.logiware.excel;

import com.gp.cong.common.DateUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.accounting.dao.ArReportsDAO;
import com.logiware.bean.ReportBean;
import java.io.File;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 *
 * @author Lakshmi Narayanan
 */
public class StatementConfigurationExcelCreator extends BaseExcelCreator{

    private void writeContent(boolean isCustomerStatement) throws Exception {
	List<ReportBean> accounts = new ArReportsDAO().getAccountsConfiguredForStatements(isCustomerStatement);
	createRow();
	resetColumnIndex();
	createHeaderCell("Customer Name", tableHeaderCellStyleCenterBold);
	createHeaderCell("Customer Number", tableHeaderCellStyleCenterBold);
	createHeaderCell("Address", tableHeaderCellStyleCenterBold);
	createHeaderCell("City", tableHeaderCellStyleCenterBold);
	createHeaderCell("State", tableHeaderCellStyleCenterBold);
	createHeaderCell("Zip Code", tableHeaderCellStyleCenterBold);
	createHeaderCell("Type", tableHeaderCellStyleCenterBold);
	createHeaderCell("Collector", tableHeaderCellStyleCenterBold);
	createHeaderCell("Statement Type", tableHeaderCellStyleCenterBold);
	createHeaderCell("Statement Frequency", tableHeaderCellStyleCenterBold);
	createHeaderCell("Exempt from Auto Hold", tableHeaderCellStyleCenterBold);
	createHeaderCell("HHG/PE/AUTOS CREDIT", tableHeaderCellStyleCenterBold);
	createHeaderCell("Credit Balance", tableHeaderCellStyleCenterBold);
	createHeaderCell("Credit Invoice", tableHeaderCellStyleCenterBold);
	createHeaderCell("Email Address", tableHeaderCellStyleCenterBold);
	createHeaderCell("Fax Number", tableHeaderCellStyleCenterBold);
	createHeaderCell("Phone Number", tableHeaderCellStyleCenterBold);
	createHeaderCell("Attempt", tableHeaderCellStyleCenterBold);
	createHeaderCell("Current", tableHeaderCellStyleRightBold);
	createHeaderCell("31-60 Days", tableHeaderCellStyleRightBold);
	createHeaderCell("61-90 Days", tableHeaderCellStyleRightBold);
	createHeaderCell("91+ Days", tableHeaderCellStyleRightBold);
	createHeaderCell("Total Outstanding", tableHeaderCellStyleRightBold);
	int rowCount = 0;
	for (ReportBean account : accounts) {
	    createRow();
	    resetColumnIndex();
	    CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
	    CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
	    createTextCell(account.getAccountName(), textCellStyle);
	    createTextCell(account.getAccountNumber(), textCellStyle);
	    createTextCell(account.getAddress(), textCellStyle);
	    createTextCell(account.getCity(), textCellStyle);
	    createTextCell(account.getState(), textCellStyle);
	    createTextCell(account.getZip(), textCellStyle);
	    createTextCell(account.getAccountType(), textCellStyle);
	    createTextCell(account.getCollector(), textCellStyle);
	    createTextCell(account.getStatementType(), textCellStyle);
	    createTextCell(account.getStatementFrequency(), textCellStyle);
	    createTextCell(account.getExemptCreditProcess(), textCellStyle);
	    createTextCell(account.getHhgPeAutosCredit(), textCellStyle);
	    createTextCell(account.getCreditBalance(), textCellStyle);
	    createTextCell(account.getCreditInvoice(), textCellStyle);
	    createTextCell(account.getEmail(), textCellStyle);
	    createTextCell(account.getFax(), textCellStyle);
	    createTextCell(account.getPhone(), textCellStyle);
	    createTextCell(account.getAttempt(), textCellStyle);
	    createDoubleCell(account.getAge30Days(), doubleCellStyle);
	    createDoubleCell(account.getAge60Days(), doubleCellStyle);
	    createDoubleCell(account.getAge90Days(), doubleCellStyle);
	    createDoubleCell(account.getAge91Days(), doubleCellStyle);
	    createDoubleCell(account.getAgeTotal(), doubleCellStyle);
	    rowCount++;
	}
	setColumnAutoSize();
    }

    public String create(boolean isCustomerStatement) throws Exception {
	try {
	    StringBuilder fileNameBuilder = new StringBuilder();
            if(isCustomerStatement){
                fileNameBuilder.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/CustomerStatement/");
                fileNameBuilder.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
            }else{
                fileNameBuilder.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/ExemptFromAutoHold/");
                fileNameBuilder.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
            }
	    File file = new File(fileNameBuilder.toString());
	    if (!file.exists()) {
		file.mkdirs();
	    }
            if(isCustomerStatement){
                fileNameBuilder.append("StatementConfiguration.xlsx");
                init(fileNameBuilder.toString(), "Statement Configuration");
            }else{
                fileNameBuilder.append("ExemptFromAutoHold.xlsx");
                init(fileNameBuilder.toString(), "Exempt From Auto Hold");
            }
	    writeContent(isCustomerStatement);
	    writeIntoFile();
	    return fileNameBuilder.toString();
	} catch (Exception e) {
	    throw e;
	} finally {
	    exit();
	}
    }
}

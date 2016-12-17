package com.logiware.common.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cvst.logisoft.domain.Transaction;
import com.logiware.accounting.dao.AccrualsDAO;
import com.logiware.accounting.dao.FiscalPeriodDAO;
import com.logiware.common.form.UploadForm;
import com.logiware.hibernate.dao.AccountingTransactionDAO;
import com.logiware.hibernate.dao.ArTransactionHistoryDAO;
import com.logiware.hibernate.domain.ArTransactionHistory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Lakshmi Narayanan
 */
public class UploadAction extends BaseAction {

    private static boolean isNotEmpty(Cell cell) {
        return (null != cell
                && ((cell.getCellType() == Cell.CELL_TYPE_STRING && CommonUtils.isNotEmpty(cell.getStringCellValue()))
                || (cell.getCellType() == Cell.CELL_TYPE_NUMERIC && CommonUtils.isNotEmpty((int) cell.getNumericCellValue()))));
    }

    public ActionForward uploadArInvoices(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UploadForm uploadForm = (UploadForm) form;
        User user = (User) request.getSession().getAttribute("loginuser");
        InputStream is = null;
        try {
            is = uploadForm.getFile().getInputStream();
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);
            AccrualsDAO accrualsDAO = new AccrualsDAO();
            AccountingTransactionDAO arDAO = new AccountingTransactionDAO();
            ArTransactionHistoryDAO historyDAO = new ArTransactionHistoryDAO();
            String customerNumber = "CMSINT0003";
            String customerName = new TradingPartnerDAO().getAccountName(customerNumber);
            for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (null != row
                        && row.getLastCellNum() >= 9
                        && isNotEmpty(row.getCell(6))
                        && (isNotEmpty(row.getCell(8)) || null != row.getCell(8).getDateCellValue())
                        && isNotEmpty(row.getCell(9))) {
                    String reference = null != row.getCell(2) ? row.getCell(2).getStringCellValue() : "";
                    String invoiceNumber;
                    if (row.getCell(6).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        int value = (int) row.getCell(6).getNumericCellValue();
                        invoiceNumber = String.valueOf(value);
                    } else {
                        invoiceNumber = row.getCell(6).getStringCellValue();
                    }
                    Date invoiceDate;
                    if (row.getCell(6).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        int value = (int) row.getCell(8).getNumericCellValue();
                        invoiceDate = DateUtils.parseDate(String.valueOf(value), "yyyyMMdd");
                    } else if (row.getCell(6).getCellType() == Cell.CELL_TYPE_STRING) {
                        invoiceDate = DateUtils.parseDate(row.getCell(8).getStringCellValue(), "yyyyMMdd");
                    } else {
                        invoiceDate = row.getCell(8).getDateCellValue();
                    }
                    Date postedDate = accrualsDAO.getPostedDate(invoiceDate);
                    Double invoiceAmount;
                    if (row.getCell(9).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        invoiceAmount = row.getCell(9).getNumericCellValue();
                    } else {
                        invoiceAmount = Double.parseDouble(row.getCell(9).getStringCellValue());
                    }
                    Transaction ar = arDAO.getArTransaction(customerNumber, null, invoiceNumber);
                    if (null == ar) {
                        ar = new Transaction();
                        ar.setCustNo(customerNumber);
                        ar.setCustName(customerName);
                        ar.setInvoiceNumber(invoiceNumber);
                        ar.setTransactionDate(invoiceDate);
                        ar.setPostedDate(postedDate);
                        ar.setTransactionAmt(invoiceAmount);
                        ar.setBalance(invoiceAmount);
                        ar.setBalanceInProcess(invoiceAmount);
                        ar.setTransactionType(ConstantsInterface.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
                        ar.setStatus(ConstantsInterface.STATUS_OPEN);
                        ar.setCreditHold(ConstantsInterface.NO);
                        ar.setCreatedOn(new Date());
                        ar.setCreatedBy(user.getUserId());
                    } else {
                        ar.setTransactionAmt(ar.getTransactionAmt() + invoiceAmount);
                        ar.setBalance(ar.getBalance() + invoiceAmount);
                        ar.setBalanceInProcess(ar.getBalanceInProcess() + invoiceAmount);
                        ar.setUpdatedOn(new Date());
                        ar.setUpdatedBy(user.getUserId());
                    }
                    if (CommonUtils.isNotEmpty(reference)) {
                        ar.setCustomerReferenceNo(reference);
                    }
                    arDAO.saveOrUpdate(ar);
                    ArTransactionHistory history = new ArTransactionHistory();
                    history.setCustomerNumber(ar.getCustNo());
                    history.setBlNumber(ar.getBillLaddingNo());
                    history.setInvoiceNumber(ar.getInvoiceNumber());
                    history.setInvoiceDate(ar.getTransactionDate());
                    history.setTransactionDate(invoiceDate);
                    history.setPostedDate(postedDate);
                    history.setTransactionAmount(invoiceAmount);
                    history.setCustomerReferenceNumber(ar.getCustomerReferenceNo());
                    history.setTransactionType("INV");
                    history.setCreatedDate(new Date());
                    history.setCreatedBy(user.getLoginName());
                    historyDAO.save(history);
                }
            }
            request.setAttribute("message", uploadForm.getFile().getFileName() + " is uploaded...");
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != is) {
                is.close();
            }
        }
        return mapping.findForward(SUCCESS);
    }

    public ActionForward uploadBudget(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UploadForm uploadForm = (UploadForm) form;
        InputStream is = null;
        try {
            FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
            is = uploadForm.getFile().getInputStream();
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);
            List<Double> amounts = new ArrayList<Double>();
            List<String> budgetSets = new ArrayList<String>();
            for (int rowIndex = 4; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (null != row
                        && row.getLastCellNum() >= 15
                        && isNotEmpty(row.getCell(0))
                        && isNotEmpty(row.getCell(1))
                        && isNotEmpty(row.getCell(2))) {
                    String account;
                    budgetSets.clear();
                    amounts.clear();
                    if (row.getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        int value = (int) row.getCell(0).getNumericCellValue();
                        account = String.valueOf(value);
                    } else {
                        account = row.getCell(0).getStringCellValue();
                    }
                    account = account.replaceAll("[^0-9]", "");
                    account = account.substring(0, 2) + "-" + account.substring(2, 6) + "-" + account.substring(6, 8);
                    Integer year;
                    if (row.getCell(1).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        year = (int) row.getCell(1).getNumericCellValue();
                    } else {
                        year = Integer.parseInt(row.getCell(1).getStringCellValue().replaceAll("[^0-9]", ""));
                    }
                    if (row.getCell(2).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        int value = (int) row.getCell(2).getNumericCellValue();
                        budgetSets.add(String.valueOf(value));
                    } else {
                        String value = row.getCell(2).getStringCellValue();
                        budgetSets.addAll(Arrays.asList(StringUtils.split(value.replaceAll("[^1-9,]", ""), ",")));
                    }
                    for (int colIndex = 4; colIndex <= 15; colIndex++) {
                        if (row.getCell(colIndex).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            amounts.add(row.getCell(colIndex).getNumericCellValue());
                        } else {
                            String value = row.getCell(colIndex).getStringCellValue();
                            amounts.add(Double.parseDouble(value.replace(",", "")));
                        }
                    }
                    fiscalPeriodDAO.saveBudget(account, year, budgetSets, amounts);
                }
            }
            request.setAttribute("message", uploadForm.getFile().getFileName() + " is uploaded...");
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != is) {
                is.close();
            }
        }
        return mapping.findForward(SUCCESS);
    }

}

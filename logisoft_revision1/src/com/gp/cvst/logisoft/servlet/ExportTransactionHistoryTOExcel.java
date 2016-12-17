package com.gp.cvst.logisoft.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.gp.cong.logisoft.reports.dto.TransactionHistoryReportDTO;
import com.gp.cong.logisoft.util.DBUtil;
import com.gp.cvst.logisoft.beans.ChartOfAccountBean;

public class ExportTransactionHistoryTOExcel extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int i = 4;
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + new Date() + "Batch.xls");

            WritableWorkbook writableWorkbook = Workbook.createWorkbook(response.getOutputStream());
            WritableSheet writableSheet = writableWorkbook.createSheet("Demo", 0);
            HttpSession session = request.getSession();
            String account = request.getParameter("account");
            String account1 = request.getParameter("account1");
            String desc = request.getParameter("desc");
            String datefrom = request.getParameter("datefrom");
            String dateto = request.getParameter("dateto");
            String sourcecode = request.getParameter("sourcecode");
            DBUtil dBUtil = new DBUtil();
            List<ChartOfAccountBean> itemdetails = null;
            if (session.getAttribute("itemdetails") != null) {
                itemdetails = (List) session.getAttribute("itemdetails");
            }
            TransactionHistoryReportDTO transactionHistoryDTO = new TransactionHistoryReportDTO(account, account1, desc, sourcecode, datefrom, dateto, itemdetails);
            if (transactionHistoryDTO != null) {
                writableSheet.addCell(new Label(0, 0, "Starting Account:-- " + transactionHistoryDTO.getStartAccount()));
                writableSheet.addCell(new Label(1, 0, "Description:-- " + transactionHistoryDTO.getDescription()));
                writableSheet.addCell(new Label(2, 0, "Test:-- "));
                writableSheet.addCell(new Label(3, 0, "Period Form:-- " + transactionHistoryDTO.getFromPeriod()));

                // SECOND ROW
                writableSheet.addCell(new Label(0, 1, "Ending Account:-- " + transactionHistoryDTO.getEndAccount()));
                writableSheet.addCell(new Label(1, 1, "Source Code:-- " + transactionHistoryDTO.getSourceCode()));
                writableSheet.addCell(new Label(3, 1, "Period To:-- " + transactionHistoryDTO.getToPeriod()));

                //Chart of AccountDetails
                writableSheet.addCell(new Label(0, 3, "Account"));
                writableSheet.addCell(new Label(1, 3, "Period"));
                writableSheet.addCell(new Label(2, 3, "Date"));
                writableSheet.addCell(new Label(3, 3, "Source Code"));
                writableSheet.addCell(new Label(4, 3, "Reference"));
                writableSheet.addCell(new Label(5, 3, "Description"));
                writableSheet.addCell(new Label(6, 3, "Debit"));
                writableSheet.addCell(new Label(7, 3, "Credit"));
                writableSheet.addCell(new Label(8, 3, "Period"));
                writableSheet.addCell(new Label(9, 3, "Balance"));
                if (null != transactionHistoryDTO.getTransactionList()) {
                    for (Iterator iterator = transactionHistoryDTO.getTransactionList().iterator(); iterator.hasNext();) {
                        double debitAmount = 0.0, creditAmount = 0.0, balance = 0.0;
                        String balaceAmount = null, period = null;
                        ChartOfAccountBean chartOfAccountBean = (ChartOfAccountBean) iterator.next();
                        writableSheet.addCell(new Label(0, i, chartOfAccountBean.getAcct()));
                        writableSheet.addCell(new Label(1, i, chartOfAccountBean.getPeriod()));
                        writableSheet.addCell(new Label(2, i, chartOfAccountBean.getDate()));
                        writableSheet.addCell(new Label(3, i, chartOfAccountBean.getSourcecode()));
                        writableSheet.addCell(new Label(4, i, chartOfAccountBean.getReference()));
                        writableSheet.addCell(new Label(5, i, chartOfAccountBean.getDescription()));
                        if (chartOfAccountBean.getDebit() != null && !chartOfAccountBean.getDebit().trim().equals("")) {
                            debitAmount = Double.parseDouble(dBUtil.removeComma(chartOfAccountBean.getDebit()));
                        }
                        WritableCellFormat writableCellFormatDebit = new WritableCellFormat(NumberFormats.FLOAT);
                        Number debitNumberValue = new Number(6, i, debitAmount, writableCellFormatDebit);
                        writableSheet.addCell(debitNumberValue);

                        if (chartOfAccountBean.getCredit() != null && !chartOfAccountBean.getCredit().trim().equals("")) {
                            creditAmount = Double.parseDouble(dBUtil.removeComma(chartOfAccountBean.getCredit()));
                        }
                        WritableCellFormat writableCellFormatCredit = new WritableCellFormat(NumberFormats.FLOAT);
                        Number creditNumberValue = new Number(7, i, creditAmount, writableCellFormatCredit);
                        writableSheet.addCell(creditNumberValue);

                        // for Balance 
                        if (chartOfAccountBean.getBalance() != null && !chartOfAccountBean.getBalance().trim().equals("")) {

                            String stringTokens[] = StringUtils.splitPreserveAllTokens(chartOfAccountBean.getBalance(), "=");
                            if (stringTokens != null && stringTokens.length > 1) {
                                period = stringTokens[0];
                                balaceAmount = stringTokens[1];
                                if (balaceAmount != null && !balaceAmount.equals("")) {
                                    balance = Double.parseDouble(dBUtil.removeComma(balaceAmount));
                                }
                            } else {
                                balance = Double.parseDouble(dBUtil.removeComma(chartOfAccountBean.getBalance()));
                            }

                        }
                        WritableCellFormat writableCellFormatBalance = new WritableCellFormat(NumberFormats.FLOAT);
                        writableSheet.addCell(new Label(8, i, period));
                        Number balanceNumberValue = new Number(9, i, balance, writableCellFormatBalance);
                        writableSheet.addCell(balanceNumberValue);
                        i++;
                    }
                }
                writableWorkbook.write();
                writableWorkbook.close();
            }
        } catch (Exception e) {
            throw new ServletException("Exception in Excel Sample Servlet", e);
        }

    }
}

package com.gp.cvst.logisoft.servlet;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.Number;

import com.gp.cong.logisoft.util.DBUtil;
import com.gp.cvst.logisoft.beans.BatchesBean;
import com.gp.cvst.logisoft.hibernate.dao.BatchDAO;
import com.gp.cvst.logisoft.reports.dto.BatchReportDTO;

import org.apache.log4j.Logger;

public class ExportBatchNumberToExcel extends HttpServlet {

    private static final Logger log = Logger.getLogger(ExportBatchNumberToExcel.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	Integer batchindex = null;
	List batchlist = new ArrayList();
	BatchDAO batchDAO = new BatchDAO();
	BatchesBean b1 = null;
	String BatchID = "";
	List batchJELI = null;
	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	DateFormat dateFormat1 = new SimpleDateFormat("hh:mm aaa");
	DBUtil dBUtil = new DBUtil();

	int i = 4;
	try {
	    response.setContentType("application/vnd.ms-excel");
	    response.setHeader("Content-Disposition",
		    "attachment; filename=" + new Date() + "Batch.xls");
	    HttpSession session = request.getSession();
	    WritableWorkbook writableWorkbook = Workbook.createWorkbook(response
		    .getOutputStream());
	    WritableSheet writableSheet = writableWorkbook.createSheet("Demo", 0);
	    BatchDAO batchdao = new BatchDAO();
	    if (request.getParameter("batchIndex") != null) {
		batchindex = new Integer(request.getParameter("batchIndex"));
	    }
	    List batchesList = null;
	    if (session.getAttribute("batchesList") != null) {
		batchesList = (List) session.getAttribute("batchesList");
	    }
	    if (!batchesList.isEmpty()) {
		b1 = (BatchesBean) batchesList.get(batchindex);
		BatchID = b1.getBatchno();
	    }
	    batchlist = (List) batchDAO.getbatchDetails(BatchID);
	    BatchesBean bdto = new BatchesBean();
	    if (!batchlist.isEmpty()) {
		bdto = (BatchesBean) batchlist.get(0);
		String bn = (String) bdto.getBatchno();
		String bdesc = (String) bdto.getDesc();
		String td = (String) bdto.getTotalDebit();
		String tc = (String) bdto.getTotalCredit();
		//Date and time
		writableSheet.addCell(new Label(0, 0, "DATE:-" + dateFormat.format(new Date())));
		writableSheet.addCell(new Label(1, 0, "TIME:-" + dateFormat1.format(new Date())));
		// Batch Details 
		writableSheet.addCell(new Label(0, 1, "BatchNo:-" + bn));
		writableSheet.addCell(new Label(1, 1, "Batch Desc" + bdesc));
		writableSheet.addCell(new Label(2, 1, "Total Debit" + tc));
		writableSheet.addCell(new Label(3, 1, "Total Credit" + td));
		//	JE details 
		writableSheet.addCell(new Label(0, 3, "JE ID"));
		writableSheet.addCell(new Label(1, 3, "JE Desc"));
		writableSheet.addCell(new Label(2, 3, "JE Period"));
		writableSheet.addCell(new Label(3, 3, "Account"));
		writableSheet.addCell(new Label(4, 3, "Acct Desc"));
		writableSheet.addCell(new Label(5, 3, "Debit"));
		writableSheet.addCell(new Label(6, 3, "Credit"));
		double debitAmount = 0.0, creditAmount = 0.0;
		batchJELI = batchdao.getJEandLineDetailsofBatch(BatchID);
		for (Iterator iter = batchJELI.iterator(); iter.hasNext();) {
		    BatchReportDTO batchReportDTO = (BatchReportDTO) iter.next();
		    writableSheet.addCell(new Label(0, i, batchReportDTO.getJeid()));
		    writableSheet.addCell(new Label(1, i, batchReportDTO.getJedesc()));
		    writableSheet.addCell(new Label(2, i, batchReportDTO.getJeperiod()));
		    writableSheet.addCell(new Label(3, i, batchReportDTO.getAcct()));
		    writableSheet.addCell(new Label(4, i, batchReportDTO.getAcctdesc()));
		    if (batchReportDTO.getDebit() != null && !batchReportDTO.getDebit().equals("")) {
			String removeCommaDebitAmount = batchReportDTO.getDebit();

			debitAmount = Double.parseDouble(dBUtil.removeComma(removeCommaDebitAmount));
		    }
		    WritableCellFormat writableCellFormatForDebit = new WritableCellFormat(NumberFormats.FLOAT);
		    Number debitNumberValue = new Number(5, i, debitAmount, writableCellFormatForDebit);
		    writableSheet.addCell(debitNumberValue);

		    if (batchReportDTO.getCredit() != null && !batchReportDTO.getCredit().equals("")) {
			String removeCommaCreditAmount = batchReportDTO.getCredit();
			creditAmount = Double.parseDouble(dBUtil.removeComma(removeCommaCreditAmount));
		    }
		    WritableCellFormat writableCellFormatForCredit = new WritableCellFormat(NumberFormats.FLOAT);
		    Number creditNumberValue = new Number(6, i, creditAmount, writableCellFormatForCredit);
		    writableSheet.addCell(creditNumberValue);
		    i++;
		}
		//	JE details generatring daynamically
	    }
	    // END BATCH CODE
	    writableWorkbook.write();
	    writableWorkbook.close();
	} catch (Exception e) {
	    log.info("Exception in Excel Sample Servlet " + new Date(),e);
	}
    }
}

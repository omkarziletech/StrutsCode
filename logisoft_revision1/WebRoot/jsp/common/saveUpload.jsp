<%-- 
    Document   : loadArInvoice
    Created on : Oct 24, 2013, 11:32:26 PM
    Author     : Lakshmi Narayanan
--%>
<%@page import="org.apache.poi.ss.usermodel.Cell"%>
<%@page import="com.gp.cong.common.DateUtils"%>
<%@page import="com.logiware.hibernate.domain.ArTransactionHistory"%>
<%@page import="com.logiware.hibernate.dao.ArTransactionHistoryDAO"%>
<%@page import="com.logiware.accounting.dao.AccrualsDAO"%>
<%@page import="com.gp.cong.logisoft.domain.User"%>
<%@page import="com.gp.cong.common.ConstantsInterface"%>
<%@page import="com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO"%>
<%@page import="com.logiware.hibernate.dao.AccountingTransactionDAO"%>
<%@page import="com.gp.cvst.logisoft.domain.Transaction"%>
<%@page import="java.util.Date"%>
<%@page import="com.gp.cong.common.CommonUtils"%>
<%@page import="org.apache.poi.ss.usermodel.Row"%>
<%@page import="org.apache.poi.ss.usermodel.Sheet"%>
<%@page import="org.apache.poi.ss.usermodel.WorkbookFactory"%>
<%@page import="org.apache.poi.ss.usermodel.Workbook"%>
<%@page import="java.io.InputStream"%>
<%@page import="org.apache.commons.io.FilenameUtils"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.FileUploadException"%>
<%@page import="java.util.List"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>File Upload</title>
	<link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
    </head>
    <body>
	<%!
	    public void uploadArInvoices(FileItem item, User user) throws Exception {
		InputStream is = null;
		try {
		    is = item.getInputStream();
		    //Create workbook from input stream
		    Workbook workbook = WorkbookFactory.create(is);
		    //Get first sheet
		    Sheet sheet = workbook.getSheetAt(0);
		    AccrualsDAO accrualsDAO = new AccrualsDAO();
		    AccountingTransactionDAO arDAO = new AccountingTransactionDAO();
		    ArTransactionHistoryDAO historyDAO = new ArTransactionHistoryDAO();
		    String customerNumber = "CMSINT0003";
		    String customerName = new TradingPartnerDAO().getAccountName(customerNumber);
		    for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			if (null != row && row.getLastCellNum() >= 9
				&& (null != row.getCell(6)
				&& ((row.getCell(6).getCellType() == Cell.CELL_TYPE_STRING && CommonUtils.isNotEmpty(row.getCell(6).getStringCellValue()))
				|| (row.getCell(6).getCellType() == Cell.CELL_TYPE_NUMERIC && CommonUtils.isNotEmpty((int) row.getCell(6).getNumericCellValue()))))
				&& (null != row.getCell(8)
				&& ((row.getCell(8).getCellType() == Cell.CELL_TYPE_STRING && CommonUtils.isNotEmpty(row.getCell(8).getStringCellValue()))
				|| (row.getCell(8).getCellType() == Cell.CELL_TYPE_NUMERIC && CommonUtils.isNotEmpty((int) row.getCell(8).getNumericCellValue()))
				|| null != row.getCell(8).getDateCellValue()))
				&& (null != row.getCell(9)
				&& ((row.getCell(9).getCellType() == Cell.CELL_TYPE_STRING && CommonUtils.isNotEmpty(row.getCell(9).getStringCellValue()))
				|| (row.getCell(9).getCellType() == Cell.CELL_TYPE_NUMERIC && CommonUtils.isNotEmpty(row.getCell(9).getNumericCellValue()))))) {
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
		} catch (Exception e) {
		    throw e;
		} finally {
		    if (null != is) {
			is.close();
		    }
		}
	    }
	%>
	<%
	    if (ServletFileUpload.isMultipartContent(request)) {
		try {
		    User user = (User) session.getAttribute("loginuser");
		    List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
		    for (FileItem item : items) {
			if (!item.isFormField()) {
			    String filename = FilenameUtils.getName(item.getName());
			    uploadArInvoices(item, user);
			    request.setAttribute("message", filename + " is uploaded...");
			}
		    }
		} catch (FileUploadException e) {
		    throw new ServletException(e);
		}
	    } else {
		throw new ServletException("Content type is not multipart/form-data");
	    }
	%>
	<div class="message width-100pc align-center" style="margin: 20px 0 0 0;">${message}</div>
    </body>
</html>
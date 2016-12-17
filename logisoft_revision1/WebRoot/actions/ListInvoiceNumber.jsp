<%@ page import="java.util.List"%>
<%@page import="com.gp.cvst.logisoft.hibernate.dao.ApInvoiceDAO"%>
<%
	String accountNumber = request.getParameter("vendorNumber");
	String invoiceNumber = request.getParameter("invoicenumber");
	if (null == invoiceNumber || invoiceNumber.isEmpty()) {
		return;
	}
	if (null == accountNumber || accountNumber.equals("null")) {
		accountNumber = "%";
	}
	StringBuffer stringBuffer = new StringBuffer();
	stringBuffer.append("<ul>");
	if (accountNumber != null) {
		ApInvoiceDAO invoiceDAO = new ApInvoiceDAO();
		List<String> invoiceList = invoiceDAO.getAllInvoiceNumberByStatusAndAccNo("Open", accountNumber, invoiceNumber);
		if (null != invoiceList) {
			for (String invoiceNo : invoiceList) {
				if(invoiceNo.contains("'")){
					stringBuffer.append("<li id=\"").append(invoiceNo).append("\"><b>").append(invoiceNo).append("</b></li>");
				}else{
					stringBuffer.append("<li id='").append(invoiceNo).append("'><b>").append(invoiceNo).append("</b></li>");
				}
			}
		}
	}
	stringBuffer.append("</ul>");
	out.println(stringBuffer.toString());

%>
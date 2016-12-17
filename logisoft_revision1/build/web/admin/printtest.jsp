<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@page import="java.io.File"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.nio.channels.FileChannel"%>
<%@page import="java.nio.ByteBuffer"%>
<%@page import="com.sun.pdfview.PDFFile"%>
<%@page import="com.sun.pdfview.PDFPrintPage"%>
<%@page import="java.awt.print.PrinterJob"%>
<%@page import="java.awt.print.PageFormat"%>
<%@page import="java.awt.print.Book"%>
<%@page import="javax.print.PrintService"%>
<%@page import="javax.print.PrintServiceLookup"%>
<%@page import="java.awt.print.Paper"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
 
<html> 
	<head>
		<title>JSP for PrintTest form</title>
	</head>
	<body>
		<html:form action="/logout" scope="request">
			<%
				// Create a PDFFile from a File reference  
				/*File f = new File("C:\\ArCustomerStatement\\ADSCAR0001.pdf");  
				FileInputStream fis = new FileInputStream(f);  
				FileChannel fc = fis.getChannel();  
				ByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());  
				PDFFile pdfFile = new PDFFile(bb); // Create PDF Print Page  
				PDFPrintPage pages = new PDFPrintPage(pdfFile);  
				 
				 // Create Print Job  
				 PrinterJob pjob = PrinterJob.getPrinterJob(); */
				 PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
				 PrintService printService = null;
				 for(PrintService service : services) {				 
				 	if("Auto HP LaserJet P2015 Series PCL 5e on CONG1 (Copy 2)".equals(service.getName())) {				 	
				 		printService = service;
				 		break;
				 	}
				 }
				 /*pjob.setPrintService(printService);
				 PageFormat pf = PrinterJob.getPrinterJob().defaultPage();  
				 Paper paper = pf.getPaper();
				 paper.setSize(80,100);
				 //pf.setPaper(paper);				
				 pjob.setJobName(f.getName());  
				 Book book = new Book();  
				 book.append(pages, pf, pdfFile.getNumPages());  
				 pjob.setPageable(book);  */
				 
				 // Send print job to default printer  
				 //pjob.print(); 				
			 %>
		</html:form>
	</body>
</html>


package com.gp.cvst.logisoft.reports.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;


import com.gp.cvst.logisoft.reports.dto.ReportDTO;
import java.util.Date;

import org.apache.log4j.Logger;

public class EmailServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(EmailServlet.class);

    /**
     * Constructor of the object.
     */
    public EmailServlet() {
	super();
    }

    /**
     * Destruction of the servlet. <br>
     */
    public void destroy() {
	super.destroy(); // Just puts "destroy" string in log
	// Put your code here
    }

    /**
     * The doGet method of the servlet. <br>
     *
     * This method is called when a form has its tag value method equals to get.
     *
     * @param request  the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException      if an error occurred
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	ReportDTO rdto = (ReportDTO) request.getSession().getAttribute(EconoHelper.REPORT_DTO_SESSION_KEY);
	HttpSession session = request.getSession();
	ServletOutputStream servletOutputStream = response.getOutputStream();
	String contextPath = request.getContextPath();
	InputStream reportStream = request.getSession().getServletContext()
		.getResourceAsStream(rdto.getCompiledReport());

	// fill the parameters for this report
	Map parameters = rdto.getParameters();
	ReportSource rs = null;
	String saveName = rdto.getFileName();
	if (rdto.isDataSourceRequired()) {
	    rs = rdto.getReportDataSource();
	}
	JasperPrint jasperPrint = null;
	if (rdto.isDataSourceRequired()) {

	    try {
		jasperPrint = JasperFillManager.fillReport(reportStream, parameters, rs);
	    } catch (JRException e) {
		// TODO Auto-generated catch block
		log.info("doGet failed on " + new Date(),e);
	    }

	} else {
	    try {
		jasperPrint = JasperFillManager.fillReport(reportStream, parameters, new JREmptyDataSource());
	    } catch (JRException e) {
		// TODO Auto-generated catch block
		log.info("doGet failed on " + new Date(),e);
	    }
	}
	if (rdto.isSaveFile()) {
	    // add the file comment so it can be viewed later by clicking on it

	    String url = "C:/Logiware/" + saveName + ".pdf";
	    try {
		File filetoprint = new File(url);

		File tempfile;
		tempfile = File.createTempFile("example", ".dat");
		tempfile.deleteOnExit();
		PrintStream tps;
		tps = new PrintStream(new FileOutputStream(tempfile, true));
		tps.println("data line 1");
		tps.println("data line 2");
		tps.close();
		FileOutputStream fos = new FileOutputStream(filetoprint);
		byte[] reportByteArray;
		try {
		    reportByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
		    fos.write(reportByteArray);
		} catch (JRException e) {
		    // TODO Auto-generated catch block
		    log.info("doGet failed on " + new Date(),e);
		}
		// write to file

		// flushes the file to the system
		fos.close();
	    } catch (RuntimeException e) {
		// TODO Auto-generated catch block
		log.info("doGet failed on " + new Date(),e);
	    }

	    response.sendRedirect(contextPath + "/jsps/fclQuotes/SearchQuotation.jsp");
	}

    }

    /**
     * The doPost method of the servlet. <br>
     *
     * This method is called when a form has its tag value method equals to post.
     *
     * @param request  the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException      if an error occurred
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {

	response.setContentType("text/html");

	// create a file in the system

    }

    /**
     * Initialization of the servlet. <br>
     *
     * @throws ServletException if an error occurs
     */
    public void init() throws ServletException {
	// Put your code here
    }
}

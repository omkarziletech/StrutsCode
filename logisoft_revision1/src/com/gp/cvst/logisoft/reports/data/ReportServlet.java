package com.gp.cvst.logisoft.reports.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.reports.dto.ReportDTO;
import java.util.Date;

import org.apache.log4j.Logger;

public class ReportServlet extends HttpServlet {
private static final Logger log = Logger.getLogger(ReportServlet.class);
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Create properties, get Session

        // Report DTO object is used to transfer information to this servlet from the view beans
        ReportDTO rdto = (ReportDTO) request.getSession().getAttribute(EconoHelper.REPORT_DTO_SESSION_KEY);
        HttpSession session = request.getSession();
        //Visit visit = (Visit)session.getAttribute("visit");
        // this is the stream which is going to respond to the browser
        ServletOutputStream servletOutputStream = response.getOutputStream();
        // get the compiled report
        InputStream reportStream = getServletConfig().getServletContext().getResourceAsStream(rdto.getCompiledReport());
        String saveName = rdto.getFileName();
        // fill the parameters for this report
        Map parameters = rdto.getParameters();
        // get the data source for this report
        ReportSource rs = null;
        if (rdto.isDataSourceRequired()) {
            rs = rdto.getReportDataSource();
        }

        try {

            //	JRExporter exporter;
            JasperPrint jasperPrint = null;
            // if datasource is required use a dataSource otherwise use an empty datasource
            if (rdto.isDataSourceRequired()) {
                try {
                    jasperPrint = JasperFillManager.fillReport(reportStream, parameters, rs);
                } catch (RuntimeException e) {
                    // TODO Auto-generated catch block
                    log.info("doGet failed on " + new Date(),e);
                }
            } else {
                jasperPrint = JasperFillManager.fillReport(reportStream, parameters, new JREmptyDataSource());
            }


            // check if the file should be saved into the file system
            if (rdto.isSaveFile()) {
                // add the file comment so it can be viewed later by clicking on it
                String url = "E:/Logiware/" + saveName + ".pdf";
                com.gp.cong.struts.LoadLogisoftProperties p = new LoadLogisoftProperties();
                if (p.getProperty("reportLocation") != null) {
                    url = p.getProperty("reportLocation") + saveName + ".pdf";
                }
                String quoteid = new String();

                // check if the dir to save the reports exists otherwise create it


                // end of adding file comment


                // create a file in the system
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




                    //boolean abc=filetoprint.createNewFile();
                    FileOutputStream fos = new FileOutputStream(filetoprint);
                    // creates a byte array so it can be written to the file system
                    byte[] reportByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
                    // write to file
                    fos.write(reportByteArray);
                    // flushes the file to the system
                    fos.close();
                } catch (RuntimeException e) {
                    // TODO Auto-generated catch block
                    log.info("doGet failed on " + new Date(),e);
                }
                String buttonValue = (String) session.getAttribute("buttonValue");

            }

            // create the report with the data and the parameters and put it to the response stream
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);


            // set the response type
            response.setContentType("application/pdf");
            // flush the response otherwise it won't do anything
            servletOutputStream.flush();
            // close the stream - make sure to close the stream everytime
            servletOutputStream.close();

        } catch (Exception e) {
            // display stack trace in the browser
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            response.setContentType("text/plain");
            response.getOutputStream().print(stringWriter.toString());
        } finally {
            // clear the session before closing the servlet
            this.clearSession(request.getSession());
        }
    }

    private void clearSession(HttpSession session) {
        session.setAttribute(EconoHelper.REPORT_DTO_SESSION_KEY, null);
    }
}

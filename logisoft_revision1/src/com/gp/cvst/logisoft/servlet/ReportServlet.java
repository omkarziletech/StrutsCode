package com.gp.cvst.logisoft.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;


import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



import com.gp.cong.logisoft.domain.LogisoftHelper;
import com.gp.cong.struts.LoadLogisoftProperties;

import com.gp.cvst.logisoft.reports.data.ReportSource;
import com.gp.cvst.logisoft.reports.dto.ReportDTO;
import com.gp.cvst.logisoft.web.Visit;


import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

public class ReportServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ReportDTO rdto = (ReportDTO) request.getSession().getAttribute(LogisoftHelper.REPORT_DTO_SESSION_KEY);
        HttpSession session = request.getSession();
        Visit visit = (Visit) session.getAttribute("visit");
        ServletOutputStream servletOutputStream = response.getOutputStream();
        InputStream reportStream = getServletConfig().getServletContext().getResourceAsStream(rdto.getCompiledReport());
        Map parameters = rdto.getParameters();
        String fileName = rdto.getFileName();
        ReportSource rs = null;
        if (rdto.isDataSourceRequired()) {
            rs = rdto.getReportDataSource();
        }

        try {
            JasperPrint jasperPrint = null;
            if (rdto.isDataSourceRequired()) {
                jasperPrint = JasperFillManager.fillReport(reportStream, parameters, rs);
            } else {
                jasperPrint = JasperFillManager.fillReport(reportStream, parameters, new JREmptyDataSource());
            }
            if (rdto.isSaveFile()) {
                String reportLocation = null;
                String folderPath = LoadLogisoftProperties.getProperty("reportLocation");
                File file = new File(folderPath);
                if (!file.exists()) {
                    file.mkdir();
                }
                if (LoadLogisoftProperties.getProperty("reportLocation") != null) {
                    reportLocation = LoadLogisoftProperties.getProperty("reportLocation") + "/" + fileName + ".pdf";
                }
                FileOutputStream fos = new FileOutputStream(reportLocation);
                byte[] reportByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
                fos.write(reportByteArray);
                fos.close();
            }

            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            response.setContentType("application/pdf");
            servletOutputStream.flush();
            servletOutputStream.close();
        } catch (Exception e) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            response.setContentType("text/plain");
            response.getOutputStream().print(stringWriter.toString());
        } finally {
            this.clearSession(request.getSession());
        }
    }

    /**
     *
     */
    private void clearSession(HttpSession session) {
        session.setAttribute(LogisoftHelper.REPORT_DTO_SESSION_KEY, null);
    }
}

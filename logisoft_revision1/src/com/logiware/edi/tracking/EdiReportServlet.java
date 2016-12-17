/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.edi.tracking;

import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.util.EdiUtil;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.edi.entity.Shipment;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import org.apache.log4j.Logger;


/**
 *
 * @author vellaisamy
 */
public class EdiReportServlet extends HttpServlet{
private static final Logger log = Logger.getLogger(EdiReportServlet.class);
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("application/pdf");
        ServletOutputStream sout = response.getOutputStream();
        String id = request.getParameter("id");
        HttpSession session = request.getSession();
        EdiTrackingSystem ediTrackingSystem = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        User user = (User)session.getAttribute("loginuser");
        try {
            EdiTrackingSystemDAO ediTrackingSystemDAO = new EdiTrackingSystemDAO();
            ediTrackingSystem = ediTrackingSystemDAO.findById(id);
            //CREATE FILE FROM THE XML DATA
            String location = LoadLogisoftProperties.getProperty("reportLocation");
            location = location+"/ediTracking";
            File xmlFile = new File(location);
            if(!xmlFile.exists()){
                xmlFile.mkdir();
            }
            File file = new File(location+"/"+user.getLoginName()+"_xml.xml");
            FileOutputStream outPutStream = new FileOutputStream(file);
            BufferedOutputStream bout = new BufferedOutputStream(outPutStream);
            bout.write(ediTrackingSystem.getXml());
            bout.flush();
            bout.close();
            Shipment shipment = new ParseInttraXML().parseXml(file.getAbsolutePath());
            //generate pdf report
            EdiTrackingPdfCreator pdfCreator = new EdiTrackingPdfCreator();
            String pdfFileName = location+"/"+user.getLoginName()+".pdf";
            if("K".equals(ediTrackingSystem.getEdiCompany())){
                KnShippingInstruction knShippingInstruction =(KnShippingInstruction) ediTrackingSystemDAO.getKnShippingInstruction(ediTrackingSystem.getBookingNo());
                shipment =new EdiUtil().formatKnShippingInstructionToShipment(shipment, knShippingInstruction);
            }
            pdfCreator.createReport(shipment, pdfFileName, ediTrackingSystem.getEdiCompany());
            bufferedInputStream = new BufferedInputStream(new FileInputStream(new File(pdfFileName)));
            bufferedOutputStream = new BufferedOutputStream(sout);
            byte[] buff = new byte[1048576];
            int bytesRead;
            while (-1 != (bytesRead = bufferedInputStream.read(buff, 0, buff.length))) {
                bufferedOutputStream.write(buff, 0, bytesRead);
            }
            bufferedOutputStream.flush();
        } catch (ParserConfigurationException ex) {
            log.info("processRequest failed on " + new Date(),ex);
        } catch (SAXException ex) {
            log.info("processRequest failed on " + new Date(),ex);
        } finally {
            sout.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            log.info("doGet failed on " + new Date(),ex);
        }
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            log.info("doPost failed on " + new Date(),ex);
        }
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}

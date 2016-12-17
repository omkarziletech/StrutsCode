/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.logiware.edi.tracking;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 *
 * @author Logiware
 */
public class EdiXmlServlet extends HttpServlet {
private static final Logger log = Logger.getLogger(EdiXmlServlet.class);
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException,Exception {
        response.setContentType("text/xml;charset=UTF-8");
        ServletOutputStream   sout = null;
        PrintWriter out = null;
        try {
            String id = request.getParameter("id");
            String messageType = request.getParameter("messageType");
            EdiTrackingSystem ediTrackingSystem = null;
            if("304".equalsIgnoreCase(messageType) || "997".equalsIgnoreCase(messageType)) {
                EdiTrackingSystemDAO ediTrackingSystemDAO = new EdiTrackingSystemDAO();
                ediTrackingSystem = ediTrackingSystemDAO.findById(id);
                if(null != ediTrackingSystem) {
                    if("success".equalsIgnoreCase(ediTrackingSystem.getStatus())) {
                        sout = response.getOutputStream();
                        if("997".equalsIgnoreCase(messageType)) {
                            sout.write(ediTrackingSystem.getXml997());
                            sout.flush();
                        }else {
                            sout.write(ediTrackingSystem.getXml());
                            sout.flush();
                        }
                    }else{
                        response.setContentType("text/html");
                        out = response.getWriter();
                        out.write(ediTrackingSystem.getLog());
                        out.flush();
                    }
                }
            }
        } finally { 
            if(null != sout) {
                sout.close();
            }
            if(null != out) {
                out.close();
            }
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

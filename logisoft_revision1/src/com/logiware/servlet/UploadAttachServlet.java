package com.logiware.servlet;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cvst.logisoft.domain.DocumentStoreLog;
import com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO;
import java.io.*;
import java.util.Date;
import java.util.zip.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.hibernate.Transaction;

import org.apache.log4j.Logger;

/**
 * This is the servlet which supports the drag and drop enabled file
 * This servlet has three major functions:
 *
 * 1. Serve a list of all the images which have been uploaded
 * 2. Serve individual images when requested.
 * 3. Handle the uploading of new images to the server.
 *
 * @author Ramasamy D
 */
public class UploadAttachServlet extends HttpServlet
{
private static final Logger log = Logger.getLogger(UploadAttachServlet.class);
    // This is the default content type for an HTML page.
    private static final String CONTENT_TYPE = "text/html";

    /**
     * This method handles PUT requests from the client.  PUT requests will come
     * from the applet portion of this application and are the way that images and
     * other files can be posted to the server.
     *
     * @param request  the HTTP request object
     * @param response the HTTP response object
     *
     * @exception ServletException
     * @exception IOException
     */
@Override
    public void doPut(HttpServletRequest request, HttpServletResponse response)
        throws ServletException,
            IOException
    {
        /*
         * The uploading applet will zip all the files together to create a
         * faster upload and to use just one server connection.
         */
        ZipInputStream in = new ZipInputStream(request.getInputStream());

        /*
         * This will write all the files to a directory on the server.
         */
        /*log.info("Session Object==>"+session);
        log.info("File Location==>"+session.getAttribute(CommonConstants.FILE_LOCATION));
        log.info("File Name==>"+session.getAttribute(CommonConstants.FILE_NAME));
        System.out.println("Document Object==>"+session.getAttribute(CommonConstants.DOCUMENT_STORE_LOG));*/
        log.info("File Location==>"+request.getHeader(CommonConstants.FILE_LOCATION));
        File dir = new File(request.getHeader(CommonConstants.FILE_LOCATION).toString());
        String fileName = null;
        String fileSize = null;
        if(!dir.exists()){
            dir.mkdirs();
        }

        try {
            try {
                while (true) {
                    ZipEntry entry = in.getNextEntry();
                    if (entry == null) {
                        break;
                    }
                    fileName = request.getHeader(CommonConstants.FILE_NAME).toString();
                    fileName = fileName.substring(0,fileName.lastIndexOf('.'));
                    String fileType = entry.getName().substring(entry.getName().lastIndexOf("."),entry.getName().length());
                    fileName = fileName+fileType;
                    File file = new File(dir,fileName);
                    FileOutputStream out = new FileOutputStream(file);
                    try {
                        int read;
                        byte[] buf = new byte[2024];

                        while ((read = in.read(buf)) > 0) {
                            out.write(buf, 0, read);
                        }
                        out.close();
                    } finally {
                    }
                    fileSize = CommonUtils.getFileSize(file);
                }
                DocumentStoreLogDAO documentStoreLogDAO = new DocumentStoreLogDAO();
                DocumentStoreLog documentStore = new DocumentStoreLog();
                documentStore.setFileLocation(request.getHeader(CommonConstants.FILE_LOCATION));
                documentStore.setComments(request.getHeader("comments"));
                documentStore.setDocumentID(request.getHeader("documnetId"));
                documentStore.setDocumentName(request.getHeader("documentName"));
                documentStore.setDocumentType(request.getHeader("documentType"));
                documentStore.setScreenName(request.getHeader("screenName"));
                documentStore.setStatus(request.getHeader("status"));
                documentStore.setOperation("Attach");
                documentStore.setDateOprDone(new Date());
                documentStore.setFileName(fileName);
                documentStore.setFileSize(fileSize);
                Transaction tx = documentStoreLogDAO.getSession().getTransaction();
                tx.begin();
                documentStoreLogDAO.save(documentStore);
                tx.commit();
            } catch (ZipException ze) {
                /*
                 * We want to catch each sip exception separately because
                 * there is a possibility that we can read more files from
                 * the archive even if one of them is corrupted.
                 */
                ze.printStackTrace();
                log.info("doPut failed on " + new Date(),ze);
            }
        } catch (Exception e) {
            log.info("doPut failed on " + new Date(),e);
        } finally {
            in.close();
        }

        /*
         * Now that we have finished uploading the files
         * we will send a reponse to the server indicating
         * our success.
         */

        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>UploadAttachServlet</title></head></html>");
        out.flush();
        out.close();
        response.setStatus(HttpServletResponse.SC_OK);
    }

}

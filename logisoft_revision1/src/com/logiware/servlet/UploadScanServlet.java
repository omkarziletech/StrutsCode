package com.logiware.servlet;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cvst.logisoft.domain.DocumentStoreLog;
import com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import java.io.*;
import java.util.Date;
import java.util.zip.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.hibernate.Transaction;

import org.apache.log4j.Logger;
/**
 * This is the servlet which supports the uploading enabled file
 * This servlet has three major functions:
 *
 * 1. Serve a list of all the images which have been uploaded
 * 2. Serve individual images when requested.
 * 3. Handle the uploading of new images to the server.
 *
 * @author Ramasamy D
 */
public class UploadScanServlet extends HttpServlet
{
    private static final Logger log = Logger.getLogger(UploadScanServlet.class);
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
    public void doPut(HttpServletRequest request, HttpServletResponse response)
        throws ServletException,
            IOException
    {
        /*
         * The Scan applet will zip all the files together to create a
         * faster upload and to use just one server connection.
         */
        ZipInputStream in = new ZipInputStream(request.getInputStream());

        /*
         * This will write all the files to a directory on the server.
         */
        File dir = new File(request.getHeader(CommonConstants.FILE_LOCATION));
        if(!dir.exists()){
            dir.mkdirs();
        }

        try {
            try {

                File file = new File(dir,request.getHeader(CommonConstants.FILE_NAME));
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();
                String fileSize = null;
                while (true) {
                    ZipEntry entry = in.getNextEntry();
                    if (entry == null) {
                        break;
                    }
                    File f = File.createTempFile("translate", entry.getName());
                    FileOutputStream out = new FileOutputStream(f);
                    FileInputStream inStream = null;
                    try {
                        int read;
                        byte[] buf = new byte[2024];

                        while ((read = in.read(buf)) > 0) {
                            out.write(buf, 0, read);
                        }
                        out.close();
                        inStream = new FileInputStream(f);
                        byte[] b = new byte[inStream.available()];
                        inStream.read(b);
                        com.lowagie.text.Image image1 = com.lowagie.text.Image.getInstance(b);
                        image1.scalePercent(30);
                        image1.setCompressionLevel(9);
                        document.add(image1);
                    } finally {
                    }
                }
                document.close();
                fileSize = CommonUtils.getFileSize(file);
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
                documentStore.setFileName(request.getHeader(CommonConstants.FILE_NAME));
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
                log.info("doPut failed for ",ze);
            }
        } catch (Exception e) {
            log.info("doPut failed for ",e);
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
        out.println("<html><head><title>ImageSrv</title></head></html>");
        out.flush();
        out.close();
        response.setStatus(HttpServletResponse.SC_OK);

    }

}

package com.gp.cong.logisoft.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gp.cong.struts.LoadLogisoftProperties;
import java.util.Date;

import org.apache.log4j.Logger;

public class PdfServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(PdfServlet.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	try {
	    ServletOutputStream sout = response.getOutputStream();
	    //	response.setContentType( "application/pdf" );  // MIME type for pdf doc
	    String fileName = request.getParameter("fileName");
	    File file = new File(fileName);
	    if (fileName == null) {
		return;
	    }
	    if (fileName.endsWith(".pdf")) {
		response.setContentType("application/pdf");
	    } else if (fileName.endsWith(".doc")) {
		response.setContentType("application/msword");
	    } else if (fileName.endsWith(".xls")) {
		response.setContentType("application/vnd.ms-excel");
		response.addHeader("Content-Disposition", "inline; filename=" + file.getName());
	    } else if (fileName.endsWith(".txt")) {
		response.setContentType("text/html");
	    } else if (fileName.endsWith(".jpeg") || fileName.endsWith(".jpg")) {
		response.setContentType("image/jpeg");
	    } else if (fileName.endsWith(".png")) {
		response.setContentType("image/png");
	    } else if (fileName.endsWith(".bmp")) {
		response.setContentType("image/bmp");
	    } else if (fileName.endsWith(".gif")) {
		response.setContentType("image/gif");
	    } else if (fileName.endsWith(".tif")) {
		response.setContentType("image/tif");
	    } else if (fileName.endsWith(".mht") || fileName.endsWith(".mhtml")) {
		response.setContentType("message/rfc822");
	    } else if (fileName.endsWith(".msg")) {
		response.setContentType("application/octet-stream");
	    }
	    LoadLogisoftProperties loadLogisoftProperties = new LoadLogisoftProperties();
	    String outputFileName = loadLogisoftProperties.getProperty("reportLocation");
	    outputFileName = outputFileName + "/" + fileName;
	    BufferedInputStream bufferedInputStream = null;
	    BufferedOutputStream bufferedOutputStream = null;
	    try {
		bufferedInputStream = new BufferedInputStream(new FileInputStream(new File(fileName)));
		bufferedOutputStream = new BufferedOutputStream(sout);
		byte[] buff = new byte[1048576];
		int bytesRead;
		while (-1 != (bytesRead = bufferedInputStream.read(buff, 0, buff.length))) {
		    bufferedOutputStream.write(buff, 0, bytesRead);
		}
		bufferedOutputStream.flush();
	    } catch (final MalformedURLException e) {
		log.info("MalformedURLException in doGet at PdfServlet on " + new Date(),e);
		throw e;
	    } catch (final IOException e) {
		log.info("IOException in doGet at PdfServlet on " + new Date(),e);
		throw e;
	    } finally {
		if (bufferedInputStream != null) {
		    bufferedInputStream.close();
		}
		if (bufferedOutputStream != null) {
		    bufferedOutputStream.close();
		}
	    }
	} catch (Exception ex) {
	    log.info("doGet Failed at PdfServlet on " + new Date(),ex);
	}
    }
}

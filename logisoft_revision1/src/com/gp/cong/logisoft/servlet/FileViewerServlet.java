package com.gp.cong.logisoft.servlet;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.PaymentMethod;
import com.logiware.hibernate.dao.AchProcessDAO;
import com.logiware.hibernate.dao.PaymentMethodDAO;
import com.logiware.hibernate.domain.AchProcessHistory;
import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.oreilly.servlet.ServletUtils;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.semanticdesktop.aperture.mime.identifier.MimeTypeIdentifier;
import org.semanticdesktop.aperture.mime.identifier.magic.MagicMimeTypeIdentifier;

import org.apache.log4j.Logger;

public class FileViewerServlet extends HttpServlet implements java.io.Serializable {

    private static final Logger log = Logger.getLogger(FileViewerServlet.class);
    private static final long serialVersionUID = 2559971804113520952L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
	try {
	    String domain = request.getParameter("domain");
	    String id = request.getParameter("id");
	    String sourceFileName = request.getParameter("fileName");

	    //this is for viewing all ACH and its encrypted files
	    if (CommonUtils.isNotEmpty(domain)
		    && CommonUtils.isNotEmpty(id)
		    && CommonUtils.isNotEmpty(sourceFileName)) {
		if (CommonUtils.isEqualIgnoreCase(domain, "AchProcessHistory")) {
		    AchProcessHistory processHistory = new AchProcessDAO().findAchProcessHistoryById(Integer.parseInt(id));
		    if (null != processHistory) {
			String extension = FilenameUtils.getExtension(sourceFileName);
			InputStream inputStream = null;
			OutputStream outputStream = response.getOutputStream();
			if (CommonUtils.isEqual(extension, "txt")) {
			    inputStream = processHistory.getAchFile().getBinaryStream();
			} else if (CommonUtils.isEqual(extension, "pgp")) {
			    inputStream = processHistory.getEncryptedFile().getBinaryStream();
			}
			if (null != inputStream && null != outputStream) {
			    response.addHeader("Content-Disposition", "inline; filename=" + sourceFileName);
			    response.setContentType("text/plain" + ";charset=utf-8");
			    byte[] buf = new byte[4096];
			    int bytesRead = inputStream.read(buf);
			    while (bytesRead != -1) {
				outputStream.write(buf, 0, bytesRead);
				bytesRead = inputStream.read(buf);
			    }
			    outputStream.flush();
			    outputStream.close();
			    inputStream.close();
			}
		    }
		} else if (CommonUtils.isEqualIgnoreCase(domain, "PaymentMethod")) {
		    PaymentMethod paymentMethod = new PaymentMethodDAO().findById(Integer.parseInt(id));
		    if (null != paymentMethod) {
			OutputStream outputStream = response.getOutputStream();
			InputStream inputStream = paymentMethod.getAchDocument().getBinaryStream();
			if (null != inputStream && null != outputStream) {
			    response.addHeader("Content-Disposition", "inline; filename=" + sourceFileName);
			    response.setContentType(paymentMethod.getAchDocumentContentType() + ";charset=utf-8");
			    byte[] buf = new byte[4096];
			    int bytesRead = inputStream.read(buf);
			    while (bytesRead != -1) {
				outputStream.write(buf, 0, bytesRead);
				bytesRead = inputStream.read(buf);
			    }
			    outputStream.flush();
			    outputStream.close();
			    inputStream.close();
			}
		    }
		}
	    } else {
		File sourceFile = new File(sourceFileName);
		if (!sourceFile.exists()) {
		    return;
		}
		this.sendFile(sourceFileName, this.getMimeType(sourceFile), response);
	    }
	} catch (Exception e) {
	    log.info("doPost failed on " + new Date(),e);
	}
    }

    private void sendFile(String outputFileName, String contentType, HttpServletResponse response) {
	// Set the response header
	// Set the filename, is used when the file will be saved (problem with mozilla)
	File outputFile = new File(outputFileName);
	if ("application/vnd.ms-excel".equalsIgnoreCase(contentType)
		|| "application/vnd.openxmlformats-officedocument.spreadsheetml".equalsIgnoreCase(contentType)) {
	    response.addHeader("Content-Disposition", "attachment; filename=" + outputFile.getName());
	} else {
	    response.addHeader("Content-Disposition", "inline; filename=" + outputFile.getName());
	}
	response.setContentType(contentType + ";charset=utf-8");
	try {
	    ServletUtils.returnFile(outputFileName, response.getOutputStream());
	} catch (Exception e) {
	    log.info("sendFile failed on " + new Date(),e);
	}
    }

    public String getMimeType(File file) {
	String mimeType = "application/octet-stream";
	try {
	    MimeTypeIdentifier identifier = new MagicMimeTypeIdentifier();
	    FileInputStream inputStream = new FileInputStream(file);
	    byte[] bytes = IOUtils.toByteArray(inputStream);
	    IOUtils.closeQuietly(inputStream);
	    mimeType = identifier.identify(bytes, file.getName(), null);
	    if (CommonUtils.isEmpty(mimeType)) {
		mimeType = "application/octet-stream";
	    }
	} catch (Exception e) {
	    log.info("sendFile failed on " + new Date(),e);
	}
	return mimeType;
    }
}

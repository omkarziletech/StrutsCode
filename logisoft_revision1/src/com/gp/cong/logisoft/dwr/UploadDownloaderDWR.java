package com.gp.cong.logisoft.dwr;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.io.FileTransfer;
import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.bc.scan.ScanBC;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.ScanDAO;
import com.gp.cong.logisoft.struts.form.ScanForm;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.beans.ChartOfAccountBean;
import com.gp.cvst.logisoft.bo.CChartCodeBO;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.hibernate.dao.AccountDetailsDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.struts.form.ChartOfAccountsForm;
import com.logiware.accounting.excel.ChartOfAccountsExcelCreator;
import com.logiware.common.form.FileUploadForm;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.struts.upload.FormFile;
import org.semanticdesktop.aperture.mime.identifier.MimeTypeIdentifier;
import org.semanticdesktop.aperture.mime.identifier.magic.MagicMimeTypeIdentifier;

public class UploadDownloaderDWR {

    LoadLogisoftProperties loadLogisoftProperties = new LoadLogisoftProperties();

    /**
     * This method is being used as Downloader for all kind of files using DWR
     * The file is downloaded using dwr.engine.openInDownLoad in client side
     *
     * @author Lakshmi Narayanan V
     * @param filePath
     * @return FileTransfer Object
     */
    public FileTransfer downloadFile(String filePath) throws Exception {
        File file = new File(filePath);
        if (filePath == null || !file.exists()) {
            return null;
        }
        String contentType = "";
        MimeTypeIdentifier identifier = new MagicMimeTypeIdentifier();
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = IOUtils.toByteArray(inputStream);
        IOUtils.closeQuietly(inputStream);
        contentType = identifier.identify(bytes, file.getName(), null);
        if (!CommonUtils.isNotEmpty(contentType)) {
            contentType = "application/octet-stream";
        }
        if (filePath.endsWith(".mht") || filePath.endsWith(".mhtml")) {
            contentType = "message/rfc822";
        }
        return new FileTransfer(file.getName(), contentType, bytes);
    }

    public String exportToExcelForChartOfAccounts(ChartOfAccountsForm chartOfAccountsForm, HttpServletRequest request) throws Exception {
        AccountDetailsDAO accountDetailsDAO = new AccountDetailsDAO();
        String acct = chartOfAccountsForm.getAcct();
        String acctStatus = chartOfAccountsForm.getAcctStatus();
        String acctType = chartOfAccountsForm.getAccttype();
        String acct1 = chartOfAccountsForm.getAcct1();
        String acctGroup = chartOfAccountsForm.getAcctGroup();
        String buttonValue = chartOfAccountsForm.getButtonValue();
        CChartCodeBO objGenericBO = new CChartCodeBO();
        List<ChartOfAccountBean> accountDetailsList = null;
        if ("showall".equals(buttonValue)) {
            accountDetailsList = objGenericBO.getGenericCodesInfo();
        } else {
            accountDetailsList = accountDetailsDAO.findForSearch(acct, acctStatus, acctType, acct1, acctGroup);
        }
        return new ChartOfAccountsExcelCreator(accountDetailsList).create();
    }

    /**
     * This method is being used as Uploader for Attach plugin using DWR
     *
     * @author Lakshmi Narayanan V
     * @param fileTransfer
     * @param scanForm
     * @return Success or Error
     */
    public String uploadFiles(FileTransfer fileTransfer, ScanForm scanForm) throws Exception {

        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        String path = LoadLogisoftProperties.getProperty("reportLocation");

        String outFileLocation = path + "/" + scanForm.getSystemRule() + "/" + scanForm.getScreenName() + "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd");
        BufferedOutputStream bufferedOutputStream = null;
        BufferedInputStream bufferedInputStream = null;
        File dir = new File(outFileLocation);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileType = fileTransfer.getFilename().substring(fileTransfer.getFilename().lastIndexOf("."));
        if (fileType.contains("eml")) {
            fileType = ".mht";
        }
        String fileName = loginUser.getLoginName();
        Calendar date = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_kkmmss");
        fileName = sdf.format(date.getTime()) + "_" + fileName + fileType;
        File outFile = new File(outFileLocation + "/" + fileName);
        bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(outFile));
        bufferedInputStream = new BufferedInputStream(fileTransfer.getInputStream());
        byte[] buff = new byte[1048576];
        int bytesRead;
        while (-1 != (bytesRead = bufferedInputStream.read(buff, 0, buff.length))) {
            bufferedOutputStream.write(buff, 0, bytesRead);
        }
        if (outFile.exists()) {
            if (bufferedInputStream != null) {
                bufferedInputStream.close();
            }
            if (bufferedOutputStream != null) {
                bufferedOutputStream.close();
            }
            scanForm.setFileSize(CommonUtils.getFileSize(outFile));
            scanForm.setFileLocation(outFileLocation + "/");
            scanForm.setFileName(fileName);
            scanForm.setOperationDate(new Date());
            scanForm.setOperation(CommonConstants.PAGE_ACTION_ATTACH);
            ScanBC scanBC = new ScanBC();
            if (CommonConstants.SS_MASTER_BL.equals(scanForm.getDocumentName())
                    && "Disputed".equalsIgnoreCase(scanForm.getSsMasterStatus())) {
                scanBC.registerApprovedorDisputedEvent(loginUser.getLoginName(), NotesConstants.DISPUTEDBLCODE, scanForm.getFileNumber());
                FclBl bl = new FclBlDAO().getFileNoObject(scanForm.getFileNo());
                if (null != bl) {
                    bl.setMaster("Yes");
                    new ScanDAO().updateMasterReceived(bl.getFileNo(), "Yes");
                }
                //Update received master
            }
            if (CommonConstants.SS_MASTER_BL.equals(scanForm.getDocumentName())
                    && "Approved".equalsIgnoreCase(scanForm.getSsMasterStatus())) {
                scanBC.registerApprovedorDisputedEvent(loginUser.getLoginName(), NotesConstants.APPROVEDBLCODE, scanForm.getFileNumber());
                FclBl bl = new FclBlDAO().getFileNoObject(scanForm.getFileNo());
                if (null != bl) {
                    bl.setMaster("Yes");
                    new ScanDAO().updateMasterReceived(bl.getFileNo(), "Yes");
                }
            }
            scanBC.setDocumentStoreLog(scanForm);

            File tempFile = new File(fileTransfer.getFilename());
            return tempFile.getName();
        } else {
            return "Error";
        }
    }

    /**
     * This is method is being used as Uploader for DeployScanApplet plugin
     * using DWR
     *
     * @author Lakshmi Narayanan V
     * @param file
     * @param path
     * @param fileName
     * @param operation
     * @return boolean
     */
    public boolean uploadScanFile(String file, String path, String fileName, String operation) throws Exception {
        boolean result = false;
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        HttpSession session = request.getSession();
        List<String> byteArrayList = null;
        if (session.getAttribute("SCANFILE") != null) {
            byteArrayList = (List<String>) session.getAttribute("SCANFILE");
            session.removeAttribute("SCANFILE");
        } else {
            byteArrayList = new ArrayList<String>();
        }

        if (operation.equals("end")) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file1 = new File(path + "/" + fileName);
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file1));
            document.open();
            for (String byteValue : byteArrayList) {
                com.lowagie.text.Image image1 = com.lowagie.text.Image.getInstance(Base64.decode(byteValue));
                image1.scalePercent(30);
                document.add(image1);
            }
            document.close();
            session.removeAttribute("SCANFILE");
            result = true;
        } else {
            byteArrayList.add(file);
            session.setAttribute("SCANFILE", byteArrayList);
            result = false;
        }
        file = null;
        byteArrayList = null;
        return result;
    }

    public String uploadImageDocument(FileUploadForm fileUploadForm) throws IOException, Exception {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            FormFile inputFile = fileUploadForm.getFile();
            StringBuilder fileName = new StringBuilder();
            fileName.append(LoadLogisoftProperties.getProperty("reportLocation"));
            fileName.append("/Vendor/W9/");
            fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
            fileName.append(fileUploadForm.getCustomerNumber());
            fileName.append("_").append(DateUtils.formatDate(new Date(), "yyyyMMdd_kkmmss"));
            fileName.append(".").append(FilenameUtils.getExtension(inputFile.getFileName()));
            File w9 = new File(fileName.toString());
            inputStream = inputFile.getInputStream();
            outputStream = FileUtils.openOutputStream(w9);
            IOUtils.copy(inputStream, outputStream);
            return fileName.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }

    /**
     * This method is used for show file viewer template using DWR
     *
     * @author Lakshmi Narayanan.V
     * @param fileName
     * @return FileViewerTemplate
     */
    public String showFileViewer(String fileName) throws Exception {
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        request.setAttribute("fileName", fileName);
        return WebContextFactory.get().forwardToString("/jsps/FileManagement/FileViewerTemplate.jsp");
    }
}

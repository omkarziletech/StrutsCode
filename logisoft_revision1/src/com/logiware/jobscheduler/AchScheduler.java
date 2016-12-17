package com.logiware.jobscheduler;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.beans.TransactionBean;
import com.gp.cvst.logisoft.domain.BankDetails;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.logiware.bean.AchProcessInfo;
import com.logiware.bean.AchSchedulerBean;
import com.logiware.hibernate.dao.AchProcessDAO;
import com.logiware.hibernate.dao.AchSetUpDAO;
import com.logiware.hibernate.domain.AchProcessHistory;
import com.logiware.hibernate.domain.AchSetUp;
import com.logiware.reports.AchFileCreator;
import com.logiware.security.FtpWrapper;
import com.logiware.utils.AchSetUpUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletContext;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPConnectionClosedException;

import org.apache.log4j.Logger;
import org.directwebremoting.WebContextFactory;

/**
 *
 * @author Lakshminarayanan
 */
public class AchScheduler extends BaseHibernateDAO implements Serializable, ConstantsInterface {

    private static final long serialVersionUID = -4065513956274100892L;
    private static final Logger log = Logger.getLogger(AchScheduler.class);

    public String processAchPayments(String schedulerId, boolean canSend) {
	ServletContext servletContext = WebContextFactory.get().getServletContext();
	try {
	    AchProcessInfo achProcessInfo = (AchProcessInfo) servletContext.getAttribute("achProcessInfo");
	    if (null != achProcessInfo) {
		return "Running";
	    } else {
		this.updateAchProcessInfo("started", "Process Started", 0, servletContext);
		if (CommonUtils.isEqualIgnoreCase(schedulerId, "new")) {
		    this.doNewAchProcess(servletContext, false);
		} else {
		    this.reRunAchProcess(schedulerId, servletContext, canSend);
		}
		this.updateAchProcessInfo("ended", "Process Ended", 100, servletContext);
		return "Ended";
	    }
	} catch (Exception e) {
	    log.info("processAchPayments failed on " + new Date(), e);
	    this.updateAchProcessInfo("failed", "Process Failed", 100, servletContext);
	    return "failed";
	}
    }

    public void doNewAchProcess(ServletContext servletContext, boolean canSend) throws Exception {
	AchSetUpDAO achSetUpDAO = new AchSetUpDAO();
	this.updateAchProcessInfo("gettingBankDetails", "Getting All Bank Accounts with ACH SetUp from DB", 5, servletContext);
	List<BankDetails> bankDetailsList = achSetUpDAO.getBankDetailsForAch();
	if (CommonUtils.isNotEmpty(bankDetailsList)) {
	    this.updateAchProcessInfo("processingEachBankAccount", "Processing Payments for each bank account", 7, servletContext);
	    int processTime = 90 / bankDetailsList.size();
	    int count = 1;
	    int startTime = 8;
	    boolean isNoAch = true;
	    for (BankDetails bankDetails : bankDetailsList) {
		List<TransactionBean> achPaymentsList = achSetUpDAO.getAchPayments(bankDetails.getBankName(), bankDetails.getBankAcctNo());
		if (CommonUtils.isNotEmpty(achPaymentsList)) {
		    isNoAch = false;
		    AchProcessHistory achProcessHistory = new AchProcessHistory();
		    achProcessHistory.setStartTime(new Date());
		    this.updateAchProcessInfo("processingEachBankAccountStarted", "Processing Payments for " + bankDetails.getBankName() + " == " + bankDetails.getBankAcctNo() + " Started", startTime, servletContext);
		    try {
			int fileCreationTime = startTime + processTime / 5;
			achProcessHistory = this.createAchFile(achPaymentsList, bankDetails, achProcessHistory, startTime, fileCreationTime, servletContext);
			if (CommonUtils.isNotEmpty(achProcessHistory.getAchFileName()) && CommonUtils.isNotEqual(achProcessHistory.getStatus(), "fileCreationFailed")) {
			    int encryptionTime = fileCreationTime + (processTime * 4 / 10);
			    achProcessHistory = this.encryptAchFile(bankDetails, achProcessHistory, fileCreationTime, encryptionTime, servletContext);
			    if (CommonUtils.isNotEmpty(achProcessHistory.getEncryptedFileName()) && CommonUtils.isNotEqual(achProcessHistory.getStatus(), "encryptionFailed")) {
				Double totalAmount = 0d;
				for (TransactionBean achPayments : achPaymentsList) {
				    totalAmount += Double.parseDouble(achPayments.getAmount().replaceAll(",", ""));
				}
				achProcessHistory.setAmount(totalAmount);
				if (canSend) {
				    int ftpTime = encryptionTime + (processTime * 4 / 10);
				    achProcessHistory = this.sendEncryptedAchFile(bankDetails, achProcessHistory, encryptionTime, ftpTime, servletContext);
				    if (CommonUtils.isEqualIgnoreCase(achProcessHistory.getStatus(), "ftpCompleted")) {
					this.doUpdate(bankDetails, achProcessHistory, ftpTime, servletContext);
				    }
				} else {
				    achProcessHistory.setStatus("Ready to send");
				}
			    }
			}
		    } catch (Exception e) {
			this.updateAchProcessInfo("processingEachBankAccountFailed", "Processing Payments for " + bankDetails.getBankName() + " == " + bankDetails.getBankAcctNo() + " Failed", count * processTime, servletContext);
			achProcessHistory.setStatus("ftpFailed");
			log.info("doNewAchProcess failed on " + new Date(), e);
		    }
		    this.updateAchProcessInfo("processingEachBankAccountEnded", "Processing Payments for " + bankDetails.getBankName() + " == " + bankDetails.getBankAcctNo() + " Ended", count * processTime, servletContext);
		    startTime = count * processTime + 1;
		    count++;
		    achProcessHistory.setBankDetails(bankDetails);
		    new AchProcessDAO().insertAchProcessHistory(achProcessHistory);
		}
	    }
	    if (isNoAch) {
		this.updateAchProcessInfo("noAchPayments", "No ACH Payments available ", 95, servletContext);
	    }
	} else {
	    this.updateAchProcessInfo("failed", "No Bank Account SetUp with ACH", 8, servletContext);
	}
    }

    private void reRunAchProcess(String schedulerId, ServletContext servletContext, boolean canSend) throws Exception {
	AchProcessHistory achProcessHistory = new AchProcessDAO().findAchProcessHistoryById(Integer.parseInt(schedulerId));
	BankDetails bankDetails = achProcessHistory.getBankDetails();
	this.updateAchProcessInfo("sending file", "Ftp uploading to " + bankDetails.getBankName() + " for " + bankDetails.getBankAcctNo() + " Started", 7, servletContext);
	AchSetUp achSetUp = bankDetails.getAchSetUp();
	if (null != achSetUp) {
	    if (canSend
		    && (CommonUtils.isEqualIgnoreCase(achProcessHistory.getStatus(), "Ready to send")
		    || CommonUtils.isEqualIgnoreCase(achProcessHistory.getStatus(), "Completed"))) {
		achProcessHistory = this.sendEncryptedAchFile(bankDetails, achProcessHistory, 5, 85, servletContext);
		if (CommonUtils.isEqualIgnoreCase(achProcessHistory.getStatus(), "ftpCompleted")) {
		    this.doUpdate(bankDetails, achProcessHistory, 85, servletContext);
		}
	    }
	}
	this.updateAchProcessInfo("sending file", "Ftp uploading to " + bankDetails.getBankName() + " for " + bankDetails.getBankAcctNo() + " Ended", 95, servletContext);
    }

    private AchProcessHistory createAchFile(List<TransactionBean> achPaymentsList, BankDetails bankDetails, AchProcessHistory achProcessHistory, int startTime, int fileCreationTime, ServletContext servletContext) throws Exception {
	this.updateAchProcessInfo("fileCreationStarted", "File Creation for " + bankDetails.getBankName() + " == " + bankDetails.getBankAcctNo() + " Started", startTime, servletContext);
	String achFileName = new AchFileCreator().createAchFile(achPaymentsList, bankDetails);
	if (CommonUtils.isNotEmpty(achFileName) && new File(achFileName).exists()) {
	    FileInputStream is = null;
	    try {
		is = new FileInputStream(new File(achFileName));
		Blob blob = new BaseHibernateDAO().getCurrentSession().getLobHelper().createBlob(IOUtils.toByteArray(is));
		achProcessHistory.setAchFile(blob);
	    } catch (Exception e) {
		throw e;
	    } finally {
		if (null != is) {
		    is.close();
		}
	    }
	    achProcessHistory.setAchFileName(achFileName);
	    this.updateAchProcessInfo("fileCreationEnded", "File Creation for " + bankDetails.getBankName() + " == " + bankDetails.getBankAcctNo() + " Ended", fileCreationTime, servletContext);
	    achProcessHistory.setStatus("fileCreationCompleted");
	} else {
	    this.updateAchProcessInfo("fileCreationFailed", "File Creation for " + bankDetails.getBankName() + " == " + bankDetails.getBankAcctNo() + " Failed", fileCreationTime, servletContext);
	    achProcessHistory.setStatus("fileCreationFailed");
	}
	return achProcessHistory;
    }

    private AchProcessHistory encryptAchFile(BankDetails bankDetails, AchProcessHistory achProcessHistory, int startTime, int encryptionTime, ServletContext servletContext) throws Exception {
	AchSetUp achSetUp = bankDetails.getAchSetUp();
	this.updateAchProcessInfo("fileEncryptionStarted", "File Encryption for " + bankDetails.getBankName() + " == " + bankDetails.getBankAcctNo() + " Started", startTime, servletContext);
	String encryptedFileName = LoadLogisoftProperties.getProperty("reportLocation") + "/Documents/Encrypt/" + bankDetails.getBankName()
                + "/" + bankDetails.getBankAcctNo() + "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
	File outputFile = new File(encryptedFileName);
	if (!outputFile.exists()) {
	    outputFile.mkdirs();
	}
	encryptedFileName += FilenameUtils.getBaseName(achProcessHistory.getAchFileName()) + ".pgp";
//	FileEncryptor.encrypt(achProcessHistory.getAchFileName(), achSetUp.getPublicKey().getBinaryStream(), encryptedFileName, true, true);
	File encryptedFile = new File(encryptedFileName);
	if (encryptedFile.exists()) {
	    FileInputStream is = null;
	    try {
		is = new FileInputStream(encryptedFile);
		Blob blob = new BaseHibernateDAO().getCurrentSession().getLobHelper().createBlob(IOUtils.toByteArray(is));
		achProcessHistory.setEncryptedFile(blob);
	    } catch (Exception e) {
		throw e;
	    } finally {
		if (null != is) {
		    is.close();
		}
	    }
	    achProcessHistory.setEncryptedFileName(encryptedFileName);
	    achProcessHistory.setStatus("encryptionCompleted");
	    int achBatchSequence = bankDetails.getAchSetUp().getBatchSequence();
	    new AchSetUpDAO().updateAchPayments(bankDetails.getBankName(), bankDetails.getBankAcctNo(), achBatchSequence, STATUS_READY_TO_SEND);
	    bankDetails.getAchSetUp().setBatchSequence(achBatchSequence + 1);
	    this.updateAchProcessInfo("fileEncryptionEnded", "File Encryption for " + bankDetails.getBankName() + " == " + bankDetails.getBankAcctNo() + " Ended", encryptionTime, servletContext);
	} else {
	    this.updateAchProcessInfo("fileEncryptionFailed", "File Encryption for " + bankDetails.getBankName() + " == " + bankDetails.getBankAcctNo() + " Failed", encryptionTime, servletContext);
	    achProcessHistory.setStatus("encryptionFailed");
	}
	return achProcessHistory;
    }

    private AchProcessHistory sendEncryptedAchFile(BankDetails bankDetails, AchProcessHistory achProcessHistory, int startTime, int ftpTime, ServletContext servletContext) throws Exception {
	this.updateAchProcessInfo("ftpUploadStarted", "Ftp Uploading for " + bankDetails.getBankName() + " == " + bankDetails.getBankAcctNo() + " Started", startTime, servletContext);
	try {
	    File encryptedFile = new File(achProcessHistory.getEncryptedFileName());
	    InputStream inputStream = new FileInputStream(encryptedFile);
	    this.ftpUpload(inputStream, achProcessHistory.getEncryptedFileName(), bankDetails, achProcessHistory.getAmount());
	    inputStream.close();
	    achProcessHistory.setStatus("ftpCompleted");
	    this.updateAchProcessInfo("fileUploadEnded", "Ftp Uploading for " + bankDetails.getBankName() + " == " + bankDetails.getBankAcctNo() + " Ended", ftpTime, servletContext);
	} catch (Exception e) {
	    log.info("AchProcessHistory failed on " + new Date(), e);
	    if (e instanceof UnknownHostException) {
		this.updateAchProcessInfo("ftpFailed", "Ftp Host is not found", ftpTime, servletContext);
	    } else if (e instanceof FTPConnectionClosedException) {
		this.updateAchProcessInfo("ftpFailed", "Ftp Connection Closed", ftpTime, servletContext);
	    } else if (e instanceof IOException) {
		this.updateAchProcessInfo("ftpFailed", "Encrypted File not found", ftpTime, servletContext);
	    } else {
		this.updateAchProcessInfo("ftpFailed", "Unknown Exception on ftp upload", ftpTime, servletContext);
	    }
	    achProcessHistory.setStatus("ftpFailed");
	}
	return achProcessHistory;
    }

    private AchProcessHistory doUpdate(BankDetails bankDetails, AchProcessHistory achProcessHistory, int startTime, ServletContext servletContext) throws Exception {
	this.updateAchProcessInfo("updatingPayments", "Payment Records getting updation for " + bankDetails.getBankName() + " == " + bankDetails.getBankAcctNo(), startTime, servletContext);
	int achBatchSequence = bankDetails.getAchSetUp().getBatchSequence() - 1;
	new AchSetUpDAO().updateAchPayments(bankDetails.getBankName(), bankDetails.getBankAcctNo(), achBatchSequence, STATUS_SENT);
	achProcessHistory.setStatus("Completed");
	achProcessHistory.setEndTime(new Date());
	return achProcessHistory;
    }

    private void ftpUpload(InputStream in, String encryptedFile, BankDetails bankDetails, Double totalAmount) throws FTPConnectionClosedException, UnknownHostException, IOException, Exception {
	AchSetUp achSetUp = bankDetails.getAchSetUp();
	FtpWrapper ftpWrapper = new FtpWrapper();
	InetAddress inetAddress = InetAddress.getByName(achSetUp.getFtpHost());
	String ftpHost = inetAddress.getHostAddress();
	if (ftpWrapper.connectAndLogin(ftpHost, achSetUp.getFtpUserName(), achSetUp.getFtpPassword())) {
	    ftpWrapper.setPassiveMode(true);
	    ftpWrapper.setAsciiFileType(true);
	    String serverFile = FilenameUtils.getName(encryptedFile);
	    ftpWrapper.uploadFile(in, serverFile);
	    ftpWrapper.disconnect();
	    String fromName = AchSetUpUtils.getProperty("achController");
	    String fromAddress = AchSetUpUtils.getProperty("achControllerEmail");
	    String subject = NumberUtils.formatNumber(totalAmount, "") + " USD";
	    SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
	    String companyName = systemRulesDAO.getSystemRules("CompanyName");
	    String acctNo = systemRulesDAO.getSystemRules("EFTAccountNo");
	    String textMessage = "Company : " + companyName + "\nAccount : " + acctNo + "\nFile total : " + subject;
	    String htmlMessage = "Company : " + companyName + "<br>Account : " + acctNo + "<br>File total : " + subject;
	    EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
	    emailSchedulerVO.setEmailData(bankDetails.getBankName(), bankDetails.getBankEmail(), fromName, fromAddress, fromAddress, "", subject, htmlMessage);
	    emailSchedulerVO.setEmailInfo("ACHFTPUPLOAD", null, CONTACT_MODE_EMAIL, 0, new Date(), "ACHFTPUPLOAD", "" + achSetUp.getBatchSequence(), null);
	    emailSchedulerVO.setTextMessage(textMessage);
	    emailSchedulerVO.setStatus(EMAIL_STATUS_PENDING);
	    new EmailschedulerDAO().saveEmail(emailSchedulerVO);
	}
    }

    public AchProcessInfo getAchProcessInfo() throws Exception {
	ServletContext servletContext = WebContextFactory.get().getServletContext();
	AchProcessInfo achProcessInfo = (AchProcessInfo) servletContext.getAttribute("achProcessInfo");
	if (null != achProcessInfo) {
	    if (CommonUtils.isEqual(achProcessInfo.getStatus(), "ended")
		    || CommonUtils.isEqual(achProcessInfo.getStatus(), "failed")
		    || CommonUtils.isEqual(achProcessInfo.getStatus(), "ftpFailed")) {
		servletContext.removeAttribute("achProcessInfo");
	    }
	    return achProcessInfo;
	}
	return null;
    }

    public void updateAchProcessInfo(String status, String message, Integer progressCount, ServletContext servletContext) {
	AchProcessInfo achProcessInfo = (AchProcessInfo) servletContext.getAttribute("achProcessInfo");
	if (null != achProcessInfo) {
	    achProcessInfo.setMessage(achProcessInfo.getMessage() + "<br>" + message);
	} else {
	    achProcessInfo = new AchProcessInfo();
	    achProcessInfo.setMessage(message);
	}
	achProcessInfo.setStatus(status);
	achProcessInfo.setProgressCount(progressCount);
	servletContext.setAttribute("achProcessInfo", achProcessInfo);
    }

    public List<AchSchedulerBean> getAchProcessHistory(String startTime, String endTime, String status) throws Exception {
	List<AchSchedulerBean> achProcessHistoryList = new ArrayList<AchSchedulerBean>();
	List<String> properties = new ArrayList<String>();
	List<String> operators = new ArrayList<String>();
	List<Object> values = new ArrayList<Object>();
	if (CommonUtils.isNotEmpty(startTime)) {
	    properties.add("startTime");
	    operators.add(">=");
	    values.add(DateUtils.parseDate(startTime, "dd-MMM-yyyy hh:mm:ss"));
	}
	if (CommonUtils.isNotEmpty(endTime)) {
	    properties.add("endTime");
	    operators.add("<=");
	    values.add(DateUtils.parseDate(endTime, "dd-MMM-yyyy hh:mm:ss"));
	}
	if (CommonUtils.isNotEmpty(status)) {
	    properties.add("status");
	    operators.add("like");
	    values.add(status);
	}
	List<AchProcessHistory> achList = new AchProcessDAO().findByProperties(properties, operators, values);
	for (AchProcessHistory achProcessHistory : achList) {
	    AchSchedulerBean achSchedulerBean = new AchSchedulerBean(achProcessHistory);
	    achProcessHistoryList.add(achSchedulerBean);
	}
	return achProcessHistoryList;
    }
}

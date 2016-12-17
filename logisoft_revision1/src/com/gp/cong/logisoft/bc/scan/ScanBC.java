package com.gp.cong.logisoft.bc.scan;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.notes.NotesBC;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.Notes;
import java.util.List;

import com.gp.cong.logisoft.domain.ScanConfig;
import com.gp.cong.logisoft.hibernate.dao.ScanDAO;
import com.gp.cong.logisoft.struts.form.ScanForm;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cvst.logisoft.domain.DocumentStoreLog;
import com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cvst.logisoft.util.DBUtil;
import java.util.Date;

public class ScanBC {

    ScanDAO scanDAO = new ScanDAO();
    SystemRulesDAO systemRuleDAO = new SystemRulesDAO();
    DocumentStoreLog documentStoreLog = new DocumentStoreLog();
    DocumentStoreLogDAO documentStoreLogDAO = new DocumentStoreLogDAO();

    public List<ScanConfig> findScanConfigByScreenName(String screenName, String documentId) throws Exception {
        return scanDAO.findScanConfigByScreenName(screenName, documentId);
    }

    public List<ScanConfig> deleteScan(String screenName, String documentId, String scanId) throws Exception {
        if (CommonFunctions.isNotNull(scanId)) {
            scanDAO.delete(scanDAO.findById(new Long(scanId)));
            return scanDAO.findScanConfigByScreenName(screenName, documentId);
        } else {
            return null;
        }

    }

    public String getSystemRulesByCode(String ruleCode) throws Exception {
        return systemRuleDAO.getSystemRulesByCode(ruleCode);
    }

    public List getScreenNameList() throws Exception {
        DBUtil dUtil = new DBUtil();
        return dUtil.scanScreenName(58);
    }

    public List getFilterScreenNameList() throws Exception {
        DBUtil dUtil = new DBUtil();
        return dUtil.filterScreenName(58);
    }

    public void setDocumentStoreLog(ScanForm scanForm) throws Exception {
        documentStoreLog.setScreenName(scanForm.getScreenName());
        documentStoreLog.setDocumentName(scanForm.getDocumentName());
        documentStoreLog.setDocumentID(CommonUtils.isNotEmpty(scanForm.getFileNo()) ? scanForm.getFileNo() : scanForm.getFileNumber());
//        documentStoreLog.setDocumentType(scanForm.getDocumentType());
        documentStoreLog.setFileLocation(scanForm.getFileLocation());
        documentStoreLog.setFileName(scanForm.getFileName());
        documentStoreLog.setOperation(scanForm.getOperation());
        documentStoreLog.setDateOprDone(scanForm.getOperationDate());
        if (CommonConstants.SS_MASTER_BL.equals(scanForm.getDocumentName())) {
            documentStoreLog.setStatus("Approved".equalsIgnoreCase(scanForm.getSsMasterStatus()) ? "Approved" : "Disputed");
        }
        documentStoreLog.setComments(CommonUtils.wrapText(scanForm.getComments(), 30));
        documentStoreLog.setFileSize(scanForm.getFileSize());
        documentStoreLogDAO.save(documentStoreLog);
    }

    public DocumentStoreLog getDocumentStoreLog(ScanForm scanForm) throws Exception {
        documentStoreLog.setScreenName(scanForm.getScreenName());
        documentStoreLog.setDocumentName(scanForm.getDocumentName());
        documentStoreLog.setDocumentID(scanForm.getFileNumber());
//        documentStoreLog.setDocumentType(scanForm.getDocumentType());
        documentStoreLog.setFileLocation(scanForm.getFileLocation());
        documentStoreLog.setFileName(scanForm.getFileName());
        documentStoreLog.setOperation(scanForm.getOperation());
        documentStoreLog.setDateOprDone(scanForm.getOperationDate());
        documentStoreLog.setComments(scanForm.getComments().toUpperCase());
        documentStoreLog.setStatus(scanForm.getSsMasterStatus());
        return documentStoreLog;
    }

    public void registerApprovedorDisputedEvent(String userName, String eventCode, String fileNumber) throws Exception {
        if (CommonFunctions.isNotNull(userName) && CommonFunctions.isNotNull(eventCode)) {
            String eventDesc = CommonConstants.getEventMap().get(eventCode);
            if (CommonFunctions.isNotNull(eventDesc)) {
                saveNotes(eventCode, userName, eventDesc, fileNumber);
            }
        }
    }

    public void saveNotes(String eventCode, String userName, String eventDesc, String fileNumber) throws Exception {
        NotesBC notesBC = new NotesBC();
        Notes note = new Notes();
        note.setModuleId("FILE");
        note.setModuleRefId(fileNumber);
        note.setNoteType(NotesConstants.NOTES_TYPE_EVENT);
        note.setUpdateDate(new Date());
        note.setUpdatedBy(userName);
        note.setItemName(eventCode);
        note.setNoteDesc("Event -> " + eventDesc + " on " + DateUtils.parseDateToString(new Date()) + " by " + userName);
        notesBC.saveNotes(note);
    }
}

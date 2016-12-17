package com.logiware.common.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.bc.scan.ScanBC;
import com.gp.cong.logisoft.domain.ScanConfig;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclSSMasterBl;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.ScanDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSSMasterBlDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsRemarksDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.DocumentStoreLog;
import com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.logiware.common.form.ScanForm;
import com.logiware.common.model.ResultModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Lucky
 */
public class ScanAction extends BaseAction {

    private static final String ADDOREDIT = "addOrEdit";
    private static final String DOCUMENTS = "documents";
    private static final String SCAN = "scan";
    private static final String DRAGANDDROP = "dragAndDrop";
    private static final String VIEWSCANORATTACH = "viewScanOrAttach";
    private static final String SHOWSCANORATTACH = "showScanOrAttach";

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ScanForm scanForm = (ScanForm) form;
        if (null == scanForm.getScreenName()) {
            scanForm.setScreenName("");
        }
        if (scanForm.getBookingType() != null) {
            if (scanForm.getBookingType().equals("T") && scanForm.getScreenName().equals("LCL EXPORTS DR")) {
                List<ResultModel> resultsForImports = new ScanDAO().getDocumentTypes("LCL IMPORTS DR", scanForm.getIgnoreDocumentName(), scanForm.getDocumentId());
                int listSize = resultsForImports.size();
                List<ResultModel> results1 = resultsForImports.subList(0, listSize % 2 == 0 ? listSize / 2 : listSize / 2 + 1);
                List<ResultModel> results2 = resultsForImports.subList(listSize % 2 == 0 ? listSize / 2 : listSize / 2 + 1, listSize);
                scanForm.setImportTransResults1(results1);
                scanForm.setImportTransResults2(results2);
                scanForm.setHiddenScreenName("LCL IMPORTS DR");

            }
        }
        List<ResultModel> results = new ScanDAO().getDocumentTypes(scanForm.getScreenName(), scanForm.getIgnoreDocumentName(), scanForm.getDocumentId());
        int listSize = results.size();
        List<ResultModel> results1 = results.subList(0, listSize % 2 == 0 ? listSize / 2 : listSize / 2 + 1);
        List<ResultModel> results2 = results.subList(listSize % 2 == 0 ? listSize / 2 : listSize / 2 + 1, listSize);
        scanForm.setResults1(results1);
        scanForm.setResults2(results2);
        request.getSession().removeAttribute("results3");
        request.getSession().removeAttribute("importTransResults3");
        return mapping.findForward(SUCCESS);
    }

    //Dr view SOP
    public ActionForward sopDocument(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ScanForm scanForm = (ScanForm) form;
        if (null == scanForm.getScreenName()) {
            scanForm.setScreenName("");
        }
        List<ResultModel> results3 = new ScanDAO().getSopDocument(scanForm.getScreenName(), scanForm.getDocumentName(), scanForm.getDocumentId());
        scanForm.setResults3(results3);
        request.getSession().setAttribute("results3", results3);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward addDocument(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward(ADDOREDIT);
    }

    public ActionForward editDocument(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ScanForm scanForm = (ScanForm) form;
        ScanDAO scanDAO = new ScanDAO();
        ScanConfig scanConfig = scanDAO.findById(scanForm.getScanConfig().getId());
        scanForm.setScanConfig(scanConfig);
        return mapping.findForward(ADDOREDIT);
    }

    public ActionForward saveDocument(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ScanForm scanForm = (ScanForm) form;
        ScanDAO scanDAO = new ScanDAO();
        ScanConfig scanConfig = scanForm.getScanConfig();
        if (scanDAO.isDocumentExists(scanConfig.getScreenName(), scanConfig.getDocumentName(), scanConfig.getId())) {
            request.setAttribute("error", "Document Name - " + scanConfig.getDocumentName() + " already exists for Screen Name - " + scanConfig.getScreenName());
        } else {
            if (CommonUtils.isNotEmpty(scanConfig.getId())) {
                ScanConfig persistence = scanDAO.findById(scanConfig.getId());
                PropertyUtils.copyProperties(persistence, scanConfig);
                scanDAO.update(persistence);
                request.setAttribute("message", scanConfig.getDocumentName() + " is updated successfully");
            } else {
                scanDAO.save(scanConfig);
                request.setAttribute("message", scanConfig.getDocumentName() + " is added successfully");
            }
        }
        return search(mapping, form, request, response);
    }

    public ActionForward removeDocument(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ScanForm scanForm = (ScanForm) form;
        ScanDAO scanDAO = new ScanDAO();
        ScanConfig scanConfig = scanDAO.findById(scanForm.getScanConfig().getId());
        request.setAttribute("message", scanConfig.getDocumentName() + " is deleted successfully");
        scanDAO.delete(scanConfig);
        return search(mapping, form, request, response);
    }

    public ActionForward showDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ScanForm scanForm = (ScanForm) form;
        if (CommonUtils.isNotEmpty(scanForm.getScreenName())) {
            List<ResultModel> results = new ScanDAO().getDocuments(scanForm.getScreenName(), scanForm.getDocumentName(), scanForm.getDocumentId());
            scanForm.setResults(results);
        }
        request.setAttribute("results3", request.getSession().getAttribute("results3"));
        return mapping.findForward(DOCUMENTS);
    }

    public ActionForward deleteDocument(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ScanForm scanForm = (ScanForm) form;
        DocumentStoreLogDAO documentDAO = new DocumentStoreLogDAO();
        DocumentStoreLog document = documentDAO.findById(scanForm.getId());
        File file = new File(document.getFileLocation(), document.getFileName());
        if (file.exists()) {
            file.delete();
        }
        request.setAttribute("message", document.getFileName() + " is deleted successfully");
        documentDAO.delete(document);
        return showDocuments(mapping, form, request, response);
    }

    public ActionForward scan(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ScanForm scanForm = (ScanForm) form;
        Date today = new Date();
        User user = (User) request.getSession().getAttribute("loginuser");
        DocumentStoreLog document = new DocumentStoreLog();
        if (scanForm.getScreenName().equalsIgnoreCase("LCL EXPORTS DR") && scanForm.getHiddenScreenName().equalsIgnoreCase("LCL IMPORTS DR")
                && scanForm.getBookingType().equals("T")) {
            document.setScreenName(scanForm.getHiddenScreenName());
        } else {
            document.setScreenName(scanForm.getScreenName());
        }
        document.setDocumentName(scanForm.getDocumentName());
        document.setDocumentID(scanForm.getDocumentId());
        document.setOperation(ConstantsInterface.PAGE_ACTION_SCAN);
        document.setDateOprDone(today);
        if (ConstantsInterface.SS_MASTER_BL.equals(scanForm.getDocumentName()) && !"LCL SS MASTER BL".equalsIgnoreCase(scanForm.getScreenName())) {
            document.setStatus(scanForm.getStatus());
            String noteCode = "Disputed".equalsIgnoreCase(scanForm.getStatus()) ? NotesConstants.DISPUTEDBLCODE : NotesConstants.APPROVEDBLCODE;
            Map<String, Object> values = new HashMap<String, Object>();
            if (CommonUtils.isNotEmpty(scanForm.getSsMasterBl())) {
                values.put("new_master_bl", scanForm.getSsMasterBl());
            }
            values.put("received_master", "Yes");
            values.put("update_by", user.getLoginName());
            values.put("updated_date", new Date());
            new BaseHibernateDAO().update("fcl_bl", "file_no", scanForm.getDocumentId(), values);
            new ScanBC().registerApprovedorDisputedEvent(user.getLoginName(), noteCode, scanForm.getDocumentId());
        } else if ("LCL SS MASTER BL".equalsIgnoreCase(scanForm.getScreenName())) {
            ssMasterBlDetailForLclExport(scanForm, document, user, request);
        }
        document.setComments(CommonUtils.wrapText(scanForm.getComment(), 30));
        request.getSession().setAttribute(ConstantsInterface.DOCUMENT_STORE_LOG, document);
        return mapping.findForward(SCAN);
    }

    public ActionForward dragAndDrop(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ScanForm scanForm = (ScanForm) form;
        Date today = new Date();
        User user = (User) request.getSession().getAttribute("loginuser");
        DocumentStoreLog document = new DocumentStoreLog();
        if (scanForm.getScreenName().equalsIgnoreCase("LCL EXPORTS DR") && scanForm.getHiddenScreenName().equalsIgnoreCase("LCL IMPORTS DR")
                && scanForm.getBookingType().equals("T")) {
            document.setScreenName(scanForm.getHiddenScreenName());
        } else {
            document.setScreenName(scanForm.getScreenName());
        }
        document.setDocumentName(scanForm.getDocumentName());
        document.setDocumentID(scanForm.getDocumentId());
        document.setOperation(ConstantsInterface.PAGE_ACTION_ATTACH);
        document.setDateOprDone(today);
        if (ConstantsInterface.SS_MASTER_BL.equals(scanForm.getDocumentName()) && !"LCL SS MASTER BL".equalsIgnoreCase(scanForm.getScreenName())) {
            document.setStatus(scanForm.getStatus());
            String noteCode = "Disputed".equalsIgnoreCase(scanForm.getStatus()) ? NotesConstants.DISPUTEDBLCODE : NotesConstants.APPROVEDBLCODE;
            Map<String, Object> values = new HashMap<String, Object>();
            if (CommonUtils.isNotEmpty(scanForm.getSsMasterBl())) {
                values.put("new_master_bl", scanForm.getSsMasterBl());
            }
            values.put("received_master", "Yes");
            values.put("update_by", user.getLoginName());
            values.put("updated_date", new Date());
            new BaseHibernateDAO().update("fcl_bl", "file_no", scanForm.getDocumentId(), values);
            new ScanBC().registerApprovedorDisputedEvent(user.getLoginName(), noteCode, scanForm.getDocumentId());
        } else if ("LCL SS MASTER BL".equalsIgnoreCase(scanForm.getScreenName())) {
            ssMasterBlDetailForLclExport(scanForm, document, user, request);
        }
        document.setComments(CommonUtils.wrapText(scanForm.getComment(), 30));
        request.setAttribute(ConstantsInterface.DOCUMENT_STORE_LOG, document);
        return mapping.findForward(DRAGANDDROP);
    }

    public ActionForward attach(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        OutputStream os = null;
        try {
            ScanForm scanForm = (ScanForm) form;
            Date today = new Date();
            User user = (User) request.getSession().getAttribute("loginuser");
            StringBuilder path = new StringBuilder();
            String companyName = new SystemRulesDAO().getSystemRulesByCode("CompanyName");
            path.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/");
            path.append(companyName).append("/");
            path.append(scanForm.getScreenName()).append("/");
            path.append(DateUtils.formatDate(today, "yyyy/MM/dd")).append("/");
            File dir = new File(path.toString());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String extension = FilenameUtils.getExtension(scanForm.getDocument().getFileName());
            StringBuilder fileName = new StringBuilder();
            fileName.append(DateUtils.formatDate(today, "yyyyMMdd_kkmmss"));
            fileName.append("_").append(user.getLoginName());
            fileName.append(".").append("email".equalsIgnoreCase(extension) ? "mht" : extension);
            File outFile = new File(dir, fileName.toString());
            os = new FileOutputStream(outFile);
            IOUtils.copyLarge(scanForm.getDocument().getInputStream(), os);
            DocumentStoreLog document = new DocumentStoreLog();
            if (scanForm.getScreenName().equalsIgnoreCase("LCL EXPORTS DR") && scanForm.getHiddenScreenName().equalsIgnoreCase("LCL IMPORTS DR")
                    && scanForm.getBookingType().equals("T")) {
                document.setScreenName(scanForm.getHiddenScreenName());
            } else {
                document.setScreenName(scanForm.getScreenName());
            }
            document.setDocumentName(scanForm.getDocumentName());
            document.setDocumentID(scanForm.getDocumentId());
            document.setFileLocation(path.toString());
            document.setFileName(fileName.toString());
            document.setOperation(ConstantsInterface.PAGE_ACTION_ATTACH);
            document.setDateOprDone(today);
            if (ConstantsInterface.SS_MASTER_BL.equals(scanForm.getDocumentName()) && !"LCL SS MASTER BL".equalsIgnoreCase(scanForm.getScreenName())) {
                document.setStatus(scanForm.getStatus());
                String noteCode = "Disputed".equalsIgnoreCase(scanForm.getStatus()) ? NotesConstants.DISPUTEDBLCODE : NotesConstants.APPROVEDBLCODE;
                Map<String, Object> values = new HashMap<String, Object>();
                if (CommonUtils.isNotEmpty(scanForm.getSsMasterBl())) {
                    values.put("new_master_bl", scanForm.getSsMasterBl());
                }
                values.put("received_master", "Yes");
                values.put("update_by", user.getLoginName());
                values.put("updated_date", new Date());
                new BaseHibernateDAO().update("fcl_bl", "file_no", scanForm.getDocumentId(), values);
                new ScanBC().registerApprovedorDisputedEvent(user.getLoginName(), noteCode, scanForm.getDocumentId());
            } else if ("LCL SS MASTER BL".equalsIgnoreCase(scanForm.getScreenName())) {
                ssMasterBlDetailForLclExport(scanForm, document, user, request);
            }
            document.setComments(CommonUtils.wrapText(scanForm.getComment(), 30));
            document.setFileSize(CommonUtils.getFileSize(outFile));
            new DocumentStoreLogDAO().save(document);
            request.setAttribute("message", scanForm.getDocument().getFileName() + " is uploaded successfully.");
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(os);
        }
        return search(mapping, form, request, response);
    }

    public ActionForward upload(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        OutputStream os = null;
        PrintWriter out = null;
        try {
            out = response.getWriter();
            ScanForm scanForm = (ScanForm) form;
            Date today = new Date();
            User user = (User) request.getSession().getAttribute("loginuser");
            StringBuilder path = new StringBuilder();
            String companyName = new SystemRulesDAO().getSystemRulesByCode("CompanyName");
            path.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/");
            path.append(companyName).append("/");
            path.append(scanForm.getScreenName()).append("/");
            path.append(DateUtils.formatDate(today, "yyyy/MM/dd")).append("/");
            File dir = new File(path.toString());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String extension = FilenameUtils.getExtension(scanForm.getDocument().getFileName());
            StringBuilder fileName = new StringBuilder();
            fileName.append(DateUtils.formatDate(today, "yyyyMMdd_kkmmss"));
            fileName.append("_").append(user.getLoginName());
            fileName.append(".").append("email".equalsIgnoreCase(extension) ? "mht" : extension);
            File outFile = new File(dir, fileName.toString());
            os = new FileOutputStream(outFile);
            IOUtils.copyLarge(scanForm.getDocument().getInputStream(), os);
            DocumentStoreLog document = new DocumentStoreLog();
            if (scanForm.getScreenName().equalsIgnoreCase("LCL EXPORTS DR") && scanForm.getHiddenScreenName().equalsIgnoreCase("LCL IMPORTS DR")
                    && scanForm.getBookingType().equals("T")) {
                document.setScreenName(scanForm.getHiddenScreenName());
            } else {
                document.setScreenName(scanForm.getScreenName());
            }
            document.setDocumentName(scanForm.getDocumentName());
            document.setDocumentID(scanForm.getDocumentId());
            document.setFileLocation(path.toString());
            document.setFileName(fileName.toString());
            document.setOperation(ConstantsInterface.PAGE_ACTION_ATTACH);
            document.setDateOprDone(today);
            if (ConstantsInterface.SS_MASTER_BL.equals(scanForm.getDocumentName()) && !"LCL SS MASTER BL".equalsIgnoreCase(scanForm.getScreenName())) {
                document.setStatus(scanForm.getStatus());
                String noteCode = "Disputed".equalsIgnoreCase(scanForm.getStatus()) ? NotesConstants.DISPUTEDBLCODE : NotesConstants.APPROVEDBLCODE;
                Map<String, Object> values = new HashMap<String, Object>();
                if (CommonUtils.isNotEmpty(scanForm.getSsMasterBl())) {
                    values.put("new_master_bl", scanForm.getSsMasterBl());
                }
                values.put("received_master", "Yes");
                values.put("update_by", user.getLoginName());
                values.put("updated_date", new Date());
                new BaseHibernateDAO().update("fcl_bl", "file_no", scanForm.getDocumentId(), values);
                new ScanBC().registerApprovedorDisputedEvent(user.getLoginName(), noteCode, scanForm.getDocumentId());
            } else if ("LCL SS MASTER BL".equalsIgnoreCase(scanForm.getScreenName())) {
                ssMasterBlDetailForLclExport(scanForm, document, user, request);
            }
            document.setComments(CommonUtils.wrapText(scanForm.getComment(), 30));
            document.setFileSize(CommonUtils.getFileSize(outFile));
            new DocumentStoreLogDAO().save(document);
            out.print("success");
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(out);
        }
        return null;
    }
    public ActionForward showDocumentsForTrans(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ScanForm scanForm = (ScanForm) form;
        if (CommonUtils.isNotEmpty(scanForm.getHiddenScreenName())) {
            List<ResultModel> results = new ScanDAO().getDocuments(scanForm.getHiddenScreenName(), scanForm.getDocumentName(), scanForm.getDocumentId());
            scanForm.setImportTransResults(results);
        }
        request.setAttribute("results3", request.getSession().getAttribute("results3"));
        return mapping.findForward(DOCUMENTS);
    }

    public ActionForward deleteDocumentForTrans(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ScanForm scanForm = (ScanForm) form;
        DocumentStoreLogDAO documentDAO = new DocumentStoreLogDAO();
        DocumentStoreLog document = documentDAO.findById(scanForm.getId());
        File file = new File(document.getFileLocation(), document.getFileName());
        if (file.exists()) {
            file.delete();
        }
        request.setAttribute("message", document.getFileName() + " is deleted successfully");
        documentDAO.delete(document);
        return showDocumentsForTrans(mapping, form, request, response);
    }
    public void ssMasterBlDetailForLclExport(ScanForm scanForm, DocumentStoreLog document, User user, HttpServletRequest request) throws Exception {
        document.setStatus(scanForm.getStatus());
        LclSSMasterBlDAO lclSSMasterBlDAO = new LclSSMasterBlDAO();
        document.setDateRecd(null);
        String ssMasterId = scanForm.getDocumentId().substring(scanForm.getDocumentId().lastIndexOf("-") + 1, scanForm.getDocumentId().length());
        LclSSMasterBl masterBl = lclSSMasterBlDAO.findById(Long.parseLong(ssMasterId));
        if (CommonUtils.isNotEmpty(scanForm.getSsMasterBl())) {
            masterBl.setMasterBl(scanForm.getSsMasterBl());
            lclSSMasterBlDAO.saveOrUpdate(masterBl);
        }
        String remarks = "Event -> " + scanForm.getStatus() + " Master Bl  on " + DateUtils.parseDateToString(new Date()) + " by " + user.getFirstName();
        new LclSsRemarksDAO().insertLclSSRemarks(masterBl.getLclSsHeader().getId(), "event", "", remarks, null, user.getUserId());
    }
    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return search(mapping, form, request, response);
    }
}

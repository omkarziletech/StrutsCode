package com.gp.cong.logisoft.struts.action;

import java.io.File;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.scan.ScanBC;
import com.gp.cong.logisoft.domain.ScanConfig;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.ScanDAO;
import com.gp.cong.logisoft.struts.form.ScanForm;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.util.DBUtil;
import java.util.List;

public class ScanAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String SCAN_LIST_PAGE = "scanListPage";
        final String SCAN_PAGE = "scanPage";
        final String ATTACH_PAGE = "attachPage";
        final String SCAN_FORM = "scanForm";
        ScanBC scanBC = new ScanBC();
        DBUtil dBUtil = new DBUtil();
        ScanForm scanForm = (ScanForm) form;
        HttpSession session = request.getSession();
        String masterBl = request.getParameter("master");
        scanForm.setFileNo(request.getParameter("fileNumber"));
        //Get Report Location
        String folderPath = LoadLogisoftProperties.getProperty("reportLocation");
        File file = new File(folderPath);
        if (!file.exists()) {
            file.mkdir();
        }
        String path = LoadLogisoftProperties.getProperty("reportLocation");
        String pageAction = null;
        if (null != scanForm) {
            pageAction = scanForm.getPageAction();
        }
        if (null != pageAction && (pageAction.equals(CommonConstants.PAGE_ACTION_SCAN) || pageAction.equals(CommonConstants.PAGE_ACTION_ATTACH))) {
            String fileName = "";
            //Get current Logged in User
            User loginUser = null;
            loginUser = (User) session.getAttribute("loginuser");
            fileName = loginUser.getLoginName();
            //Generate fileName
            scanForm.setOperationDate(new Date());
            fileName = DateUtils.formatDate(new Date(), "yyyyMMdd_kkmmss") + "_" + fileName + ".pdf";
            //Generate fileLocation
            String fileLocation = path + "/" + scanForm.getSystemRule() + "/" + scanForm.getScreenName() + "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
            //insert into document_store_log
            scanForm.setFileLocation(fileLocation);
            scanForm.setFileName(fileName);
            scanForm.setOperation(pageAction);
            if (CommonConstants.SS_MASTER_BL.equalsIgnoreCase(scanForm.getDocumentName())
                    && ("Disputed".equalsIgnoreCase(scanForm.getSsMasterStatus()) || "Approved".equalsIgnoreCase(scanForm.getSsMasterStatus()))) {
                //Update received master
                FclBl bl = new FclBlDAO().getOriginalBl(scanForm.getFileNo());
                if (null != bl) {
                    bl.setMaster("Yes");
                    new ScanDAO().updateMasterReceived(bl.getFileNo(), "Yes");
                }
            }
            //Set fileName and fileLocation in session
            session.setAttribute(CommonConstants.DOCUMENT_STORE_LOG, scanBC.getDocumentStoreLog(scanForm));
            session.setAttribute(CommonConstants.FILE_NAME, fileName);
            session.setAttribute(CommonConstants.FILE_LOCATION, fileLocation);
            request.setAttribute(CommonConstants.SCAN_LIST, scanBC.findScanConfigByScreenName(scanForm.getScreenName(), scanForm.getFileNumber()));
            if (pageAction.equals(CommonConstants.PAGE_ACTION_SCAN)) {
                return mapping.findForward(SCAN_PAGE);
            } else {
                return mapping.findForward(ATTACH_PAGE);
            }
        } else {
            String quoteBookingName = null;
            scanForm.setSystemRule(scanBC.getSystemRulesByCode("CompanyName"));
            request.setAttribute(CommonConstants.SCAN_SCREEN_NAME_LIST, scanBC.getScreenNameList());
            request.setAttribute(CommonConstants.FILTER_SCREEN_NAME_LIST, scanBC.getFilterScreenNameList());
            request.setAttribute(CommonConstants.SCAN_SCREEN_DOCUMENT_TYPE_LIST, dBUtil.scanDocumentType(61));
            List<ScanConfig> scanList = scanBC.findScanConfigByScreenName(scanForm.getScreenName(), scanForm.getFileNumber());
            quoteBookingName = request.getParameter("quoteBookingName");
            if (CommonUtils.isNotEmpty(scanList)) {
                if (quoteBookingName != null) {
                    if (quoteBookingName.equals("QUOTE") || quoteBookingName.equals("BOOKING")) {
                        ScanConfig scanConfig1 = null;
                        for (ScanConfig scanConfig : scanList) {
                            if (scanConfig.getDocumentName().equals("SS LINE MASTER BL")) {
                                scanConfig1 = scanConfig;
                                break;
                            }
                        }
                        if (scanConfig1 != null) {
                            scanList.remove(scanConfig1);
                        }
                    }
                }
                int listSize = scanList.size();
                List<ScanConfig> scanList1 = scanList.subList(0, listSize % 2 == 0 ? listSize / 2 : listSize / 2 + 1);
                List<ScanConfig> scanList2 = scanList.subList(listSize % 2 == 0 ? listSize / 2 : listSize / 2 + 1, listSize);
                request.setAttribute(CommonConstants.SCAN_SUB_LIST1, scanList1);
                request.setAttribute(CommonConstants.SCAN_SUB_LIST2, scanList2);
            }
        }
        request.setAttribute(SCAN_FORM, scanForm);
        request.setAttribute("masterBl", masterBl);
        return mapping.findForward(SCAN_LIST_PAGE);
    }
}

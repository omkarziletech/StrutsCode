/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.lcl.dwr;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.lcl.model.LclBookingCommodityModel;
import com.gp.cong.lcl.model.LclModel;
import com.gp.cong.lcl.webservices.dom.Carrier;
import com.gp.cong.logisoft.bc.fcl.SedFilingBC;
import com.gp.cong.logisoft.bc.tradingpartner.GeneralInformationBC;
import com.gp.cong.logisoft.beans.ChargesInfoBean;
import com.gp.cong.logisoft.beans.ImportsManifestBean;
import com.gp.cong.logisoft.beans.LclRatesInfoBean;
import com.gp.cong.logisoft.beans.LclRatesPrtChgBean;
import com.gp.cong.logisoft.beans.RoutingOptionsBean;
import com.gp.cong.logisoft.domain.CustomerAccounting;
import com.gp.cong.logisoft.domain.CustomerContact;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.GeneralInformation;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.LCLPortConfiguration;
import com.gp.cong.logisoft.domain.LogFileEdi;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.ProcessInfo;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.RoleDuty;
import com.gp.cong.logisoft.domain.SedFilings;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.Warehouse;
import com.gp.cong.logisoft.domain.lcl.CommodityType;
import com.gp.cong.logisoft.domain.lcl.Disposition;
import com.gp.cong.logisoft.domain.lcl.Lcl3pRefNo;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cong.logisoft.domain.lcl.LclBookingExport;
import com.gp.cong.logisoft.domain.lcl.LclBookingHsCode;
import com.gp.cong.logisoft.domain.lcl.LclBookingPad;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceDetail;
import com.gp.cong.logisoft.domain.lcl.LclConsolidate;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclOptions;
import com.gp.cong.logisoft.domain.lcl.LclQuote;
import com.gp.cong.logisoft.domain.lcl.LclQuoteAc;
import com.gp.cong.logisoft.domain.lcl.LclQuoteHsCode;
import com.gp.cong.logisoft.domain.lcl.LclQuotePad;
import com.gp.cong.logisoft.domain.lcl.LclQuotePiece;
import com.gp.cong.logisoft.domain.lcl.LclQuotePieceDetail;
import com.gp.cong.logisoft.domain.lcl.LclQuotePieceWhse;
import com.gp.cong.logisoft.domain.lcl.LclRemarks;
import com.gp.cong.logisoft.domain.lcl.LclSSMasterBl;
import com.gp.cong.logisoft.domain.lcl.LclSearchTemplate;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnit;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.LclUnitWhse;
import com.gp.cong.logisoft.domain.lcl.PackageType;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPiece;
import com.gp.cong.logisoft.hibernate.dao.AgencyInfoDAO;
import com.gp.cong.logisoft.hibernate.dao.CustomerAccountingDAO;
import com.gp.cong.logisoft.hibernate.dao.CustomerContactDAO;
import com.gp.cong.logisoft.hibernate.dao.EdiDAO;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.GeneralInformationDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.LCLPortConfigurationDAO;
import com.gp.cong.logisoft.hibernate.dao.NotesDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.ProcessInfoDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.SedFilingsDAO;
import com.gp.cong.logisoft.hibernate.dao.SedSchedulebDetailsDAO;
import com.gp.cong.logisoft.hibernate.dao.TerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.hibernate.dao.WarehouseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.CommodityDetailsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.DispositionDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingAcTransDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLImportRatesDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLQuoteDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLRatesDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.Lcl3pRefNoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBlAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingDispoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingHotCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingHsCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportAmsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPadDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPlanDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingSegregationDao;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclConsolidateDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclHazmatDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclInbondsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclOptionsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteHazmatDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteHsCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteImportDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuotePadDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuotePieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuotePieceWhseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSSMasterBlDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSearchTemplateDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsDetailDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDispoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsImportsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclWarehsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.PackageTypeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.QuoteCommodityDetailsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.RoleDutyDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLCorrectionChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLCorrectionDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LclBLPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.commodityTypeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.kn.BookingEnvelopeDao;
import com.gp.cong.logisoft.jobscheduler.EMailScheduler;
import com.gp.cong.logisoft.jobscheduler.MailPropertyReader;
import com.gp.cong.logisoft.lcl.bc.LclBookingUtils;
import com.gp.cong.logisoft.lcl.model.LclBookingPlanBean;
import com.gp.cong.logisoft.lcl.report.LclReportConstants;
import com.gp.cong.logisoft.lcl.xml.GtnexusTemplate;
import com.gp.cong.logisoft.lcl.xml.InttraTemplate;
import com.gp.cong.logisoft.reports.CTSBolPdfCreator;
import com.gp.cong.logisoft.struts.action.lcl.LclUnitsRates;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.beans.MailMessageVO;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclCommodityForm;
import com.logiware.accounting.dao.LclManifestDAO;
import com.logiware.common.dao.PropertyDAO;
import com.logiware.cts.Srdapp;
import com.logiware.cts.SrdappService;
import com.logiware.domestic.CreateBOLXml;
import com.logiware.domestic.DomesticBooking;
import com.logiware.domestic.DomesticBookingDAO;
import com.logiware.domestic.DomesticPurchaseOrder;
import com.logiware.domestic.DomesticPurchaseOrderDAO;
import com.logiware.edi.entity.PackageDetails;
import com.logiware.edi.entity.Party;
import com.logiware.edi.entity.Shipment;
import com.logiware.edi.tracking.EdiTrackingSystem;
import com.logiware.edi.tracking.EdiTrackingSystemDAO;
import com.logiware.hibernate.dao.ArRedInvoiceDAO;
import com.logiware.hibernate.domain.ArRedInvoice;
import com.logiware.lcl.dao.ExportNotificationDAO;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.directwebremoting.WebContextFactory;

/**
 *
 * @author lakshh
 */
public class LclDwr implements ConstantsInterface, LclCommonConstant {

    private static final Logger log = Logger.getLogger(LclDwr.class);

    public boolean showCarogReceivedButton(String fileNumberId) throws Exception {
        boolean result = false;
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            List<LclBookingPiece> commList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
            if (!commList.isEmpty()) {
                result = true;
            }
        }
        return result;
    }

    public boolean checkInsurance(String destination) throws Exception {//checking Insurance charges by destination page-lclExportBooing.js methodName->lockInsurance();
        LCLPortConfigurationDAO lCLPortConfigurationDAO = new LCLPortConfigurationDAO();
        Object lcp[] = lCLPortConfigurationDAO.lclPortConfiguration(destination);
        if (lcp[3] != null && lcp[3].toString().equalsIgnoreCase("N")) {
            return true;
        }
        return false;
    }

    public boolean checkLclCollectCharges(String destination) throws Exception {//checking collect charges by destination page->lclExportBooing.js methodName->calculateCAFCharge
        LCLPortConfigurationDAO lCLPortConfigurationDAO = new LCLPortConfigurationDAO();
        Object lcp[] = lCLPortConfigurationDAO.lclPortConfiguration(destination);
        if (lcp[2] != null && lcp[2].toString().equalsIgnoreCase("Y")) {
            return true;
        }
        return false;
    }

    public boolean checkTerminal(String unLocationCode, String rateType) throws Exception {
        RefTerminalDAO refTerminalDAO = new RefTerminalDAO();
        if (rateType.equalsIgnoreCase("R")) {
            rateType = "Y";
        }
        RefTerminal refTerminal = refTerminalDAO.getTerminalByUnLocation(unLocationCode, rateType);
        if (CommonFunctions.isNotNull(refTerminal)) {
            return true;
        }
        return false;
    }

    public void clearRemarksSession(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        lclSession.setOverRiddedRemarks(null);

    }

    public String[] checkImportVoyageTerminal(String unLocationCode, String rateType, HttpServletRequest request) throws Exception {
        RefTerminalDAO refTerminalDAO = new RefTerminalDAO();
        PortsDAO portsDAO = new PortsDAO();
        String[] data = new String[3];
        String trmnum = refTerminalDAO.getTrmnum(unLocationCode, rateType);
        data[0] = trmnum != null ? trmnum : "";
        String eciPortCode = portsDAO.getPortValue("eciportcode", unLocationCode);
        data[1] = eciPortCode != null && eciPortCode.length() == 3 ? eciPortCode.trim() : "";
        User user = (User) request.getSession().getAttribute("loginuser");
        RoleDuty roleDuty = new RoleDutyDAO().getRoleDetails(user.getRole().getRoleId());
        if (null != roleDuty && roleDuty.isImportsVoyagePod()) {
            data[2] = String.valueOf(roleDuty.isImportsVoyagePod());
        }
        return data;
    }

    public boolean isCargoReceived(String fileId) throws Exception {
        LclFileNumber lclFileNumber = new LclFileNumberDAO().getByProperty("id", Long.parseLong(fileId));
        if (lclFileNumber != null && lclFileNumber.getStatus() != null) {
            if (lclFileNumber.getStatus().equals("WV") || lclFileNumber.getStatus().equals("R") || lclFileNumber.getStatus().equals("P")) {
                return true;
            }
        }
        return false;
    }

    public String getLatestCorrectionStatus(String fileNumberId) throws Exception {
        String status = "";
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            LCLCorrectionDAO lclCorrectionDAO = new LCLCorrectionDAO();
            Object statusObj = lclCorrectionDAO.getFieldDescByFileId(Long.parseLong(fileNumberId), "status");
            if (statusObj != null) {
                status = statusObj.toString();
            }
        }
        return status;
    }

    public String[] getLCLSellVendorValues(String chargeCode) throws Exception {
        String data[] = new String[2];
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        List genericCodeList = genericCodeDAO.findByCode((Object) chargeCode);
        GenericCode genericCode = (GenericCode) genericCodeList.get(0);
        data[0] = genericCode.getField10();
        data[1] = genericCode.getField11();
        if (data[0] == null) {
            data[0] = "";
        }
        if (data[1] == null) {
            data[1] = "";
        }
        return data;
    }

    public boolean checkTemplateFlag(String templateName) throws Exception {
        boolean flag = false;
        List<LclSearchTemplate> list = new LclSearchTemplateDAO().getAllTemplate();
        for (LclSearchTemplate lct : list) {
            if (lct.getTemplateName().equals(templateName)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public boolean lclUnverifiedDetails(String fileId, String fileNo, String status, String cityName, String labelsCount, String currentLocId, HttpServletRequest request) throws Exception {
        User user = null;
        if (request.getSession().getAttribute("loginuser") != null) {
            user = (User) request.getSession().getAttribute("loginuser");
        }
        if (CommonUtils.isNotEmpty(labelsCount)) {
            LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
            lclRemarksDAO.insertLclRemarks(Long.parseLong(fileId), REMARKS_DR_AUTO_NOTES, labelsCount + " Labels Printed", user.getUserId());
        }
        updateStatus(fileId, status, cityName, String.valueOf(user.getUserId()), currentLocId, "", null, null);
        /*As per mantis#3409 - Dispo change from OBKG to ANY chnaged to OBKG to RCVD
         /*ExportNotificationDAO notificationDAO = new ExportNotificationDAO();
         NotificationModel bkgContactEmails = notificationDAO.getBookingContactCodeJ(fileId, "obkg_to_any");
         if (null == bkgContactEmails) {
         bkgContactEmails = notificationDAO.getBkgNewContact(Long.parseLong(fileId), "dispo");
         }
         if (null != bkgContactEmails) {
         notificationDAO.insertNotification(Long.parseLong(fileId), "Disposition", "Pending", bkgContactEmails, user.getUserId());
         }*/
        if (CommonUtils.isNotEmpty(labelsCount)) {
            new LclUtils().setMailTransactionsDetails("LclBooking", "Label Print", user, labelsCount, "Pending", new Date(), fileNo, Long.parseLong(fileId));
        }
        return true;
    }

    public boolean updateStatus(String fileId, String status, String cityName,
            String userId, String currentLocId, String dispoStatus, String originId, String destinationId) throws Exception {
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        Integer loginId = Integer.parseInt(userId);
        if (null != fileId && !fileId.equals("")) {
            StringBuilder remarks = new StringBuilder();
            Long fileNoId = Long.parseLong(fileId);
            String CityName = new LclRemarksDAO().getCityName(currentLocId);
            if (status != null) {
                String statusRemarks = "";
                if (status.equalsIgnoreCase("WV")) {
                    status = "W";
                    remarks.append("Cargo Received (Verified -- ").append(CityName).append(")");
                    statusRemarks = "Inventory Status->WAREHOUSE(Verified)";
                    cargoRecivedBookingDispo("RCVD", Long.parseLong(fileId), loginId, Integer.parseInt(currentLocId), originId, destinationId);
                    ExportNotificationDAO notificationDAO = new ExportNotificationDAO();
                    String bkgAcctNo = notificationDAO.getBkgAcctNo(Long.parseLong(fileId), true);
                    List<String> bkgAcctNoList = new ArrayList<String>(Arrays.asList(bkgAcctNo.split(",")));
                    String columnName = "RUNV".equalsIgnoreCase(dispoStatus) ? "runv_to_rcvd" : "obkg_to_any";
                    String bkgContactEmails = notificationDAO.getBookingContactCodeJ(fileId, columnName, bkgAcctNoList);
                    if (null != bkgContactEmails) {
                        notificationDAO.insertNotification(Long.parseLong(fileId), "Disposition", "Pending", bkgContactEmails, Integer.parseInt(userId));
                    }
                } else if (status.equalsIgnoreCase("WU")) {
                    status = "W";
                    statusRemarks = "Inventory Status->WAREHOUSE(Un-Verified)";
                    remarks.append("Cargo Received (Not Verified -- ").append(CityName).append(")");
                    cargoRecivedBookingDispo("RUNV", Long.parseLong(fileId), loginId, Integer.parseInt(currentLocId), originId, destinationId);
                } else if (status.equalsIgnoreCase("RF")) {
                    statusRemarks = "Inventory Status->Refused";
                    remarks.append("Cargo Received (Refused -- ").append(CityName).append(")");
                }
                lclRemarksDAO.insertLclRemarks(fileNoId, REMARKS_DR_AUTO_NOTES, statusRemarks, loginId);
                new LclFileNumberDAO().updateLclFileNumbersStatus(fileId, status);
            }
            lclRemarksDAO.insertLclRemarks(fileNoId, REMARKS_DR_AUTO_NOTES, remarks.toString(), loginId);
        }
        return true;
    }

    public void cargoRecivedBookingDispo(String status, Long fileId, Integer loginId, Integer currentLocId, String originId, String destinationId) throws Exception {
        LclBookingDispoDAO lclBookingDispoDAO = new LclBookingDispoDAO();
        Integer dispoId = disposDesc(status);
        if (null != dispoId) {
            if (status.equalsIgnoreCase("RCVD")) {
                Integer wareHouseId = null;
                if (CommonUtils.isNotEmpty(originId) && CommonUtils.isNotEmpty(destinationId)) {
                    UnLocation origin = new UnLocationDAO().getUnlocation(originId);
                    UnLocation destination = new UnLocationDAO().getUnlocation(destinationId);
                    if (null != origin && null != destination) {
                        wareHouseId = new WarehouseDAO().getLclWarehouseRouting(origin.getId(), destination.getId());
                        if (CommonUtils.isEmpty(wareHouseId)) {
                            wareHouseId = new WarehouseDAO().getLclDefaultWarehouseRouting(origin.getId());
                        }
                    }
                }
                if (CommonUtils.isEmpty(wareHouseId)) {
                    wareHouseId = new WarehouseDAO().getWarehouseId(new UnLocationDAO().findById(currentLocId) != null
                            ? new UnLocationDAO().findById(currentLocId).getUnLocationCode() : "", "W");
                }
                lclBookingDispoDAO.insertBookingDispoForRCVD(fileId, dispoId, null,
                        null, loginId, currentLocId, CommonUtils.isNotEmpty(wareHouseId) ? wareHouseId : null);
                new LclBookingPieceDAO().insertBookingPieceWhse(fileId, loginId, CommonUtils.isNotEmpty(wareHouseId) ? wareHouseId : null);
            } else {
                lclBookingDispoDAO.insertBookingDispo(fileId, dispoId, null, null, loginId, currentLocId);
            }
        }
    }

    public boolean lclRefusedDetails(String fileId, String status, String cityName,
            String comment, String userId, String unlocationCode) throws Exception {
        if (CommonUtils.isNotEmpty(comment)) {
            LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
            lclRemarksDAO.insertLclRemarks(Long.parseLong(fileId), "Refused", comment.toUpperCase(), Integer.parseInt(userId));
        }
        updateStatus(fileId, status, cityName, userId, null, "", null, null);
        Integer warehouseId = new WarehouseDAO().getWarehouseId(unlocationCode, "B");
        if (CommonUtils.isNotEmpty(warehouseId)) {
            LclBookingDispoDAO lclBookingDispoDAO = new LclBookingDispoDAO();
            Integer dispoId = disposDesc("RF");
            Integer dispositionId = lclBookingDispoDAO.currentDispoId(Long.parseLong(fileId));
            if (!CommonUtils.isEqual(dispoId, dispositionId)) {
                lclBookingDispoDAO.insertBookingDispo(Long.parseLong(fileId), dispoId, Integer.parseInt(userId));
            }
            new LclBookingPieceDAO().insertExportBookingPieceWhse(Long.parseLong(fileId), Integer.parseInt(userId), warehouseId);

        }
        return true;
    }

//    public boolean mailRefusedDetails(String fileId, String status) throws Exception {
//        LclRemarks lclRemarks = new LclRemarksDAO().executeUniqueQuery("from LclRemarks where lclFileNumber.id=" + new Long(fileId) + " AND type='cargo Refused' AND status IS NULL");
//        if (lclRemarks != null) {
//            HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
//            User user = (User) request.getSession().getAttribute("loginuser");
//            String fromName = user.getFirstName();
//            String fromAddress = user.getEmail();
//            String subject = "Cargo Received";
//            String remarks = lclRemarks.getRemarks();
//            String remark = remarks.toUpperCase();
//            EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
//            emailSchedulerVO.setFileLocation("");
//            emailSchedulerVO.setName("Cargo Received");
//            emailSchedulerVO.setSubject("Followup Event" + " " + subject + "--" + lclRemarks.getLclFileNumber().getFileNumber());
//            emailSchedulerVO.setType("Email");
//            emailSchedulerVO.setStatus("Pending");
//            emailSchedulerVO.setToAddress(lclRemarks.getFollowupEmail() + "," + fromAddress);
//            emailSchedulerVO.setFromName(fromName);
//            emailSchedulerVO.setFromAddress(fromAddress);
//            emailSchedulerVO.setEmailDate(new Date());
//            emailSchedulerVO.setTextMessage(remark);
//            emailSchedulerVO.setModuleName("Cargo Received");
//            emailSchedulerVO.setUserName(user.getLoginName());
//            emailSchedulerVO.setModuleId(lclRemarks.getLclFileNumber().getFileNumber());
//            emailSchedulerVO.setHtmlMessage(remark);
//            new EmailschedulerDAO().saveOrUpdate(emailSchedulerVO);
//            lclRemarks.setStatus("Closed");
//        }
//        return true;
//    }
//    public boolean mailUnverifiedDetails(String fileId, String status) throws Exception {
//        LclRemarks lclRemarks = new LclRemarksDAO().executeUniqueQuery("from LclRemarks where lclFileNumber.id=" + new Long(fileId) + " AND type='cargo Unverified' AND status IS NULL");
//        if (lclRemarks != null) {
//            HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
//            User user = (User) request.getSession().getAttribute("loginuser");
//            String fromName = user.getFirstName();
//            String fromAddress = user.getEmail();
//            String remarks = lclRemarks.getRemarks();
//            String remark = remarks.toUpperCase();
//            String subject = "Cargo Received";
//            EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
//            emailSchedulerVO.setFileLocation("");
//            emailSchedulerVO.setName("Cargo Received");
//            emailSchedulerVO.setSubject("Followup Event" + " " + subject + "--" + lclRemarks.getLclFileNumber().getFileNumber());
//            emailSchedulerVO.setType("Email");
//            emailSchedulerVO.setStatus("Pending");
//            emailSchedulerVO.setToAddress(lclRemarks.getFollowupEmail() + "," + fromAddress);
//            emailSchedulerVO.setFromName(fromName);
//            emailSchedulerVO.setFromAddress(fromAddress);
//            emailSchedulerVO.setEmailDate(new Date());
//            emailSchedulerVO.setTextMessage(remark);
//            emailSchedulerVO.setModuleName("Cargo Received");
//            emailSchedulerVO.setUserName(user.getLoginName());
//            emailSchedulerVO.setModuleId(lclRemarks.getLclFileNumber().getFileNumber());
//            emailSchedulerVO.setHtmlMessage(remark);
//            new EmailschedulerDAO().saveOrUpdate(emailSchedulerVO);
//            lclRemarks.setStatus("Closed");
//        }
//        return true;
//    }
    public boolean mailVerifiedDetails(String fileId, HttpServletRequest request) throws Exception {
        LclRemarks lclRemarks = null;
        if (null != fileId && !fileId.equals("")) {
            lclRemarks = new LclRemarksDAO().getLclRemarksByType(fileId, "Cargo Received-Verified");
        }
        if (null != lclRemarks) {
            User user = (User) request.getSession().getAttribute("loginuser");
            String fromName = user.getFirstName();
            String fromAddress = user.getEmail();
            String remarks = lclRemarks.getRemarks();
            String remark = remarks.toUpperCase();
            String subject = "Cargo Received";
            EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
            emailSchedulerVO.setFileLocation("");
            emailSchedulerVO.setName("Cargo Received");
            emailSchedulerVO.setSubject("Followup Event" + " " + subject + "--" + lclRemarks.getLclFileNumber().getFileNumber());
            emailSchedulerVO.setType("Email");
            emailSchedulerVO.setStatus("Pending");
            emailSchedulerVO.setToAddress(lclRemarks.getFollowupEmail());
            emailSchedulerVO.setFromName(fromName);
            emailSchedulerVO.setFromAddress(fromAddress);
            emailSchedulerVO.setEmailDate(new Date());
            emailSchedulerVO.setTextMessage(remark);
            emailSchedulerVO.setModuleName("Cargo Received");
            emailSchedulerVO.setUserName(user.getLoginName());
            emailSchedulerVO.setModuleId(lclRemarks.getLclFileNumber().getFileNumber());
            emailSchedulerVO.setHtmlMessage(remark);
            new EmailschedulerDAO().saveOrUpdate(emailSchedulerVO);
            lclRemarks.setStatus("Closed");
        }
        return true;
    }

    public String[] getDefaultAgentForLcl(String unlocationCode, String type) throws Exception {
        PortsDAO portsDAO = new PortsDAO();
        String agentInfo[] = portsDAO.getDefaultAgentForLcl(unlocationCode, type);
        return agentInfo;
    }

    public boolean checkForDisable(String accountNo) throws Exception {//remove
        TradingPartnerDAO partnerDAO = new TradingPartnerDAO();
        Object obj = partnerDAO.chekForDisable(accountNo);
        if (null != obj && String.valueOf(obj).equalsIgnoreCase("Y")) {
            return true;
        }
        return false;
    }

    public boolean deleteRemarks(String fileId, String type, String userId, String fileState) throws Exception {
        if (CommonUtils.isNotEmpty(fileId)) {
            LclRemarksDAO lclRemarksdao = new LclRemarksDAO();
            String osdRemarks = lclRemarksdao.deleteOsdRemarks(Long.parseLong(fileId), type);
            if (fileState.equalsIgnoreCase("B")) {
                new LclRemarksDAO().insertLclRemarks(Long.parseLong(fileId), REMARKS_DR_AUTO_NOTES, "DELETED -> OSD ->" + osdRemarks, Integer.parseInt(userId));
            } else {
                new LclRemarksDAO().insertLclRemarks(Long.parseLong(fileId), REMARKS_QT_AUTO_NOTES, "DELETED -> OSD ->" + osdRemarks, Integer.parseInt(userId));
            }
            lclRemarksdao.deleteRemarks(Long.parseLong(fileId), type);
            return true;
        }
        return false;
    }

    public String getConsFileList(String fileIdA) throws Exception {
        StringBuilder sb = new StringBuilder();
        if (CommonUtils.isNotEmpty(fileIdA)) {
            LclConsolidateDAO lclConsolidateDAO = new LclConsolidateDAO();
            List<Object> fileNumberList = lclConsolidateDAO.getAllFileIdsById(fileIdA);
            if (fileNumberList != null && fileNumberList.size() > 0) {
                for (int i = 0; i < fileNumberList.size(); i++) {
                    if (i != 0) {
                        sb.append(",");
                    }
                    sb.append(fileNumberList.get(i).toString());
                }
            }
        }
        return sb.toString();
    }

    public String showTPDetails(String header, String fileIdA, String city, String state, String zip, String acctName, String acctNo, String address, String fmc, String oti, String commodityNo, String retailCommodity) {
        StringBuilder sb = new StringBuilder();
        sb.append("<HTML><BODY>");
        sb.append("<table>");
        sb.append("<tr>");
        sb.append("<td colspan='2' align='left'>");
        sb.append("<FONT size='2' COLOR=#008000>");
        sb.append("<b>");
        sb.append(header);
        sb.append("</b>");
        sb.append("</FONT>");
        sb.append("</td>");
        sb.append("</tr>");
        if (commodityNo != null && commodityNo.equals("000000 COMMON COMMODITY")) {
            commodityNo = "";
        }
        tooltipLabelValue(sb, "ACCOUNT NAME:", acctName);
        tooltipLabelValue(sb, "ACCOUNT NO:", acctNo);
        tooltipLabelValue(sb, "ADDRESS:", address);
        tooltipLabelValue(sb, "CITY:", city);
        tooltipLabelValue(sb, "STATE:", state);
        tooltipLabelValue(sb, "ZIP:", zip);
        tooltipLabelValue(sb, "FMC:", fmc);
        tooltipLabelValue(sb, "OTI NUMBER:", oti);
        tooltipLabelValue(sb, "COLOAD COMMODITY:", commodityNo);
        tooltipLabelValue(sb, "RETAIL COMMODITY:", retailCommodity);
        sb.append("</table>");
        sb.append("</BODY></HTML>");
        return sb.toString();
    }

    public String showClientSectionNotes(String header, String fileId,
            String companyName, String city, String state, String zip, String acctNo,
            String address, String fax, String phone, String moduleName, String userId) throws Exception {
        if (CommonUtils.isNotEmpty(fileId) && !fileId.equals("0")) {
            Long fileNoId = Long.parseLong(fileId);
            LclBooking lclBooking = null;
            LclQuote lclQuote = null;
            String remarkType = "";
            if ("B".equalsIgnoreCase(moduleName)) {
                lclBooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", fileNoId);
                new LCLBookingDAO().getCurrentSession().evict(lclBooking);
                remarkType = REMARKS_DR_AUTO_NOTES;
            } else {
                remarkType = REMARKS_QT_AUTO_NOTES;
                lclQuote = new LCLQuoteDAO().getByProperty("lclFileNumber.id", fileNoId);
                new LCLQuoteDAO().getCurrentSession().evict(lclQuote);
            }
            StringBuilder remarks = new StringBuilder();
            String updateNote = "";
            String insertNote = "";
            LclContact lclContact = null;
            if (header.equalsIgnoreCase("Client")) {
                if (lclBooking != null) {
                    lclContact = lclBooking.getClientContact();
                } else if (lclQuote != null) {
                    lclContact = lclQuote.getClientContact();
                }
            } else if (header.equalsIgnoreCase("Shipper")) {
                if (lclBooking != null) {
                    lclContact = lclBooking.getShipContact();
                } else if (lclQuote != null) {
                    lclContact = lclQuote.getShipContact();
                }
            } else if (header.equalsIgnoreCase("Forwarder")) {
                if (lclBooking != null) {
                    lclContact = lclBooking.getFwdContact();
                } else if (lclQuote != null) {
                    lclContact = lclQuote.getFwdContact();
                }
            } else if (header.equalsIgnoreCase("Consignee")) {
                if (lclBooking != null) {
                    lclContact = lclBooking.getConsContact();
                } else if (lclQuote != null) {
                    lclContact = lclQuote.getConsContact();
                }
            }
            if (null != lclContact) {
                if (CommonUtils.isNotEmpty(lclContact.getCompanyName())) {
                    if ((!lclContact.getCompanyName().equalsIgnoreCase(companyName))
                            || (CommonUtils.isNotEmpty(lclContact.getFax1()) && !lclContact.getFax1().equalsIgnoreCase(fax))
                            || (CommonUtils.isNotEmpty(lclContact.getPhone1()) && !lclContact.getPhone1().equalsIgnoreCase(phone))
                            || (CommonUtils.isNotEmpty(lclContact.getAddress()) && !lclContact.getAddress().equalsIgnoreCase(address))
                            || (CommonUtils.isNotEmpty(lclContact.getCity()) && !lclContact.getCity().equalsIgnoreCase(city))
                            || (CommonUtils.isNotEmpty(lclContact.getState()) && !lclContact.getState().equalsIgnoreCase(state))
                            || (CommonUtils.isNotEmpty(lclContact.getZip()) && !lclContact.getZip().equalsIgnoreCase(zip))) {
                        updateNote = "UPDATED -> ";
                        remarks.append("UPDATED -> ");
                    }
                } else if (CommonUtils.isEmpty(lclContact.getCompanyName()) && !CommonUtils.isEmpty(companyName)) {
                    insertNote = "INSERTED -> ";
                    remarks.append("INSERTED -> ");
                }
                if (insertNote == "INSERTED -> ") {
                    remarks.append(header + "-> ");
                    if (CommonUtils.isEmpty(lclContact.getCompanyName()) && !CommonUtils.isEmpty(companyName)) {
                        remarks.append("Company Name -> " + companyName + " ");
                    }
                    if (!CommonUtils.isEmpty(acctNo)) {
                        remarks.append("Account No -> " + acctNo + " ");
                    }
                    if (!CommonUtils.isEmpty(fax)) {
                        remarks.append("Fax -> " + fax + " ");
                    }
                    if (!CommonUtils.isEmpty(phone)) {
                        remarks.append("Phone -> " + phone + " ");
                    }
                    if (!CommonUtils.isEmpty(address)) {
                        remarks.append("Address -> " + address + " ");
                    }
                    if (!CommonUtils.isEmpty(city)) {
                        remarks.append("City -> " + city + " ");
                    }
                    if (!CommonUtils.isEmpty(state)) {
                        remarks.append("State -> " + state + " ");
                    }
                    if (!CommonUtils.isEmpty(zip)) {
                        remarks.append("Zip -> " + zip + " ");
                    }
                }
                if (updateNote == "UPDATED -> ") {
                    remarks.append(header + "-> ");
                    if (!lclContact.getCompanyName().equalsIgnoreCase(companyName) || (!CommonUtils.isEmpty(lclContact.getCompanyName()) && CommonUtils.isEmpty(companyName))) {
                        remarks.append("Company Name -> " + lclContact.getCompanyName() + " to " + companyName + " ");
                    }
                    if (header.equalsIgnoreCase("Client")) {
                        if (lclBooking != null && lclBooking.getClientAcct() != null && lclBooking.getClientAcct().getAccountno() != null) {
                            if (!lclBooking.getClientAcct().getAccountno().equalsIgnoreCase(acctNo) || (!CommonUtils.isEmpty(lclBooking.getClientAcct().getAccountno()) && CommonUtils.isEmpty(acctNo))) {
                                remarks.append("Account No -> " + lclBooking.getClientAcct().getAccountno() + " to " + acctNo + " ");
                            }
                        } else if (lclQuote != null && lclQuote.getClientAcct() != null && lclQuote.getClientAcct().getAccountno() != null) {
                            if (!lclQuote.getClientAcct().getAccountno().equalsIgnoreCase(acctNo) || (!CommonUtils.isEmpty(lclQuote.getClientAcct().getAccountno()) && CommonUtils.isEmpty(acctNo))) {
                                remarks.append("Account No -> " + lclQuote.getClientAcct().getAccountno() + " to " + acctNo + " ");
                            }
                        }
                    } else if (header.equalsIgnoreCase("Shipper")) {
                        if (lclBooking != null && lclBooking.getShipAcct() != null && lclBooking.getShipAcct().getAccountno() != null) {
                            if (!lclBooking.getShipAcct().getAccountno().equalsIgnoreCase(acctNo) || (!CommonUtils.isEmpty(lclBooking.getShipAcct().getAccountno()) && CommonUtils.isEmpty(acctNo))) {
                                remarks.append("Account No -> " + lclBooking.getShipAcct().getAccountno() + " to " + acctNo + " ");
                            }
                        } else if (lclQuote != null && lclQuote.getShipAcct() != null && lclQuote.getShipAcct().getAccountno() != null) {
                            if (!lclQuote.getShipAcct().getAccountno().equalsIgnoreCase(acctNo) || (!CommonUtils.isEmpty(lclQuote.getShipAcct().getAccountno()) && CommonUtils.isEmpty(acctNo))) {
                                remarks.append("Account No -> " + lclQuote.getShipAcct().getAccountno() + " to " + acctNo + " ");
                            }
                        }
                    } else if (header.equalsIgnoreCase("Forwarder")) {
                        if (lclBooking != null && lclBooking.getFwdAcct() != null && lclBooking.getFwdAcct().getAccountno() != null) {
                            if (!lclBooking.getFwdAcct().getAccountno().equalsIgnoreCase(acctNo) || (!CommonUtils.isEmpty(lclBooking.getFwdAcct().getAccountno()) && CommonUtils.isEmpty(acctNo))) {
                                remarks.append("Account No -> " + lclBooking.getFwdAcct().getAccountno() + " to " + acctNo + " ");
                            }
                        } else if (lclQuote != null && lclQuote.getFwdAcct() != null && lclQuote.getFwdAcct().getAccountno() != null) {
                            if (!lclQuote.getFwdAcct().getAccountno().equalsIgnoreCase(acctNo) || (!CommonUtils.isEmpty(lclQuote.getFwdAcct().getAccountno()) && CommonUtils.isEmpty(acctNo))) {
                                remarks.append("Account No -> " + lclQuote.getFwdAcct().getAccountno() + " to " + acctNo + " ");
                            }
                        }
                    } else if (header.equalsIgnoreCase("Consignee")) {
                        if (lclBooking != null && lclBooking.getConsAcct() != null && lclBooking.getConsAcct().getAccountno() != null) {
                            if (!lclBooking.getConsAcct().getAccountno().equalsIgnoreCase(acctNo) || (!CommonUtils.isEmpty(lclBooking.getConsAcct().getAccountno()) && CommonUtils.isEmpty(acctNo))) {
                                remarks.append("Account No -> " + lclBooking.getConsAcct().getAccountno() + " to " + acctNo + " ");
                            }
                        } else if (lclQuote != null && lclQuote.getConsAcct() != null && lclQuote.getConsAcct().getAccountno() != null) {
                            if (!lclQuote.getConsAcct().getAccountno().equalsIgnoreCase(acctNo) || (!CommonUtils.isEmpty(lclQuote.getConsAcct().getAccountno()) && CommonUtils.isEmpty(acctNo))) {
                                remarks.append("Account No -> " + lclQuote.getConsAcct().getAccountno() + " to " + acctNo + " ");
                            }
                        }
                    }
                    if (!lclContact.getFax1().equalsIgnoreCase(fax) || (!CommonUtils.isEmpty(lclContact.getFax1()) && CommonUtils.isEmpty(fax))) {
                        remarks.append("Fax -> " + lclContact.getFax1() + " to " + fax + " ");
                    }
                    if (!lclContact.getPhone1().equalsIgnoreCase(phone) || (!CommonUtils.isEmpty(lclContact.getPhone1()) && CommonUtils.isEmpty(phone))) {
                        remarks.append("Phone -> " + lclContact.getPhone1() + " to " + phone + " ");
                    }
                    if (!lclContact.getAddress().equalsIgnoreCase(address) || (!CommonUtils.isEmpty(lclContact.getAddress()) && CommonUtils.isEmpty(address))) {
                        remarks.append("Address -> " + lclContact.getAddress() + " to " + address + " ");
                    }
                    if (!lclContact.getCity().equalsIgnoreCase(city) || (!CommonUtils.isEmpty(lclContact.getCity()) && CommonUtils.isEmpty(city))) {
                        remarks.append("City -> " + lclContact.getCity() + " to " + city + " ");
                    }
                    if (!lclContact.getState().equalsIgnoreCase(state) || (!CommonUtils.isEmpty(lclContact.getState()) && CommonUtils.isEmpty(state))) {
                        remarks.append("State -> " + lclContact.getState() + " to " + state + " ");
                    }
                    if (!lclContact.getZip().equalsIgnoreCase(zip) || (!CommonUtils.isEmpty(lclContact.getZip()) && CommonUtils.isEmpty(zip))) {
                        remarks.append("Zip -> " + lclContact.getZip() + " to " + zip + " ");
                    }
                }
                if (!CommonUtils.isEmpty(remarks)) {
                    new LclRemarksDAO().insertLclRemarks(fileNoId, remarkType, remarks.toString(), Integer.parseInt(userId));
                }
            }
        }
        return "";
    }

    public String getContactDetails(String header, String acctNo, String acctName, String moduleName) throws Exception {
        StringBuilder sb = new StringBuilder();
        List<CustomerContact> custContact = null;
        CustAddress custAddress = null;
        String Email = "";
        String Fax = "";
        String address = "";
        String firstName = "";
        String lastName = "";
        String phone = "";
        String phone1 = "";
        String fax1 = "";
        String city = "";
        String state = "";
        String zip = "";
        String collector = "";
        CustomerContactDAO custContactDAO = new CustomerContactDAO();
        GeneralInformationDAO generalInformationDao = new GeneralInformationDAO();
        String importColoadRetail = generalInformationDao.getImportRatingColoadRetail(acctNo);
        CustomerAccounting customerAccounting = new CustomerAccountingDAO().findByAccountNumber(acctNo);
        if (acctNo != null && !acctNo.equals("")) {
            custContact = custContactDAO.findByAccountNumber(acctNo);
            custAddress = new CustAddressDAO().findByAccountNo(acctNo);
            if (null != customerAccounting && null != customerAccounting.getArcode()) {
                collector = customerAccounting.getArcode().getLoginName();
            }
            if (custAddress != null && custAddress.getAddress1() != null && !custAddress.getAddress1().equals("")) {
                address = custAddress.getAddress1().toUpperCase();
                if (custAddress.getPhone() != null && !custAddress.getPhone().equals("")) {
                    phone1 = custAddress.getPhone();
                }
                if (custAddress.getFax() != null && !custAddress.getFax().equals("")) {
                    fax1 = custAddress.getFax();
                }
                if (custAddress.getCity1() != null && !custAddress.getCity1().equals("")) {
                    city = custAddress.getCity1().toUpperCase();
                }
                if (custAddress.getState() != null && !custAddress.getState().equals("")) {
                    state = custAddress.getState().toUpperCase();
                }
                if (custAddress.getZip() != null && !custAddress.getZip().equals("")) {
                    zip = custAddress.getZip();
                }
            }
        }
        sb.append("<HTML><BODY>");
        sb.append("<table>");
        sb.append("<tr>");
        sb.append("<td colspan='2' align='left'>");
        sb.append("<FONT size='2' COLOR=#008000>");
        sb.append("<b>");
        sb.append(header);
        sb.append("</b>");
        sb.append("</FONT>");
        sb.append("</td>");
        sb.append("</tr>");
        tooltipLabelValue(sb, "ACCOUNT NAME:", acctName);
        tooltipLabelValue(sb, "ACCOUNT NO:", acctNo);
        tooltipLabelValue(sb, "ADDRESS:", address);
        if (state != null && !state.equals("")) {
            tooltipLabelValue(sb, "CITY/STATE/ZIP:", city + " / " + state + " / " + zip);
        } else {
            tooltipLabelValue(sb, "CITY/STATE/ZIP:", city + " / " + zip);
        }
        tooltipLabelValue(sb, "PHONE:", phone1);

        if (moduleName.equalsIgnoreCase("Imports")) {
            tooltipLabelValue(sb, "FAX:", fax1);
        } else if (moduleName.equalsIgnoreCase("Exports")) {
            tooltipLabelValue(sb, "FAX:", fax1);
        } else {
            tooltipLabelValue(sb, "FAX:", fax1);
        }
        if (moduleName.equalsIgnoreCase("Imports") || CommonUtils.isEmpty(moduleName)) {
            tooltipLabelValueA(sb, "COLLECTOR:", collector, "IMPORTS RATING COLOAD/RETAIL:", importColoadRetail);
        }
        sb.append("<tr>");
        sb.append("<td colspan='2' align='left'>");
        sb.append("<FONT size='2' COLOR=#008000>");
        sb.append("<b>");
        sb.append("Contacts:");
        sb.append("</b>");
        sb.append("</FONT>");
        sb.append("</td>");
        sb.append("</tr>");
        sb.append("</table>");
        sb.append("<table class='display-table' width='100%'><tr><th>FIRST NAME</th><th>LAST NAME</th><th>EMAIL</th><th>FAX</th><th>PHONE</th><th>Code F</th></tr>");
        sb.append("<tbody>");
        if (custContact != null) {
            for (CustomerContact contact : custContact) {
                if (contact != null && contact.getCodef() != null && contact.getCodef().getCode() != null && !contact.getCodef().getCode().equals("")) {
                    firstName = contact.getFirstName();
                    lastName = contact.getLastName();
                    phone = contact.getPhone();
                    Email = contact.getEmail();
                    Fax = contact.getFax();
                    sb.append("<tr><td>");
                    sb.append(firstName);
                    sb.append("</td><td>");
                    sb.append(lastName);
                    sb.append("</td><td>");
                    sb.append(Email);
                    sb.append("</td><td>");
                    sb.append(Fax);
                    sb.append("</td><td>");
                    sb.append(phone);
                    sb.append("</td><td>");
                    sb.append(contact.getCodef().getCode());
                    sb.append("</td></tr>");
                }
            }
        }
        sb.append("</tbody>");
        sb.append("</table>");
        sb.append("</BODY></HTML>");
        return sb.toString();
    }

    private StringBuilder tooltipLabelValue(StringBuilder sb, String label, String value) {
        sb.append("<tr>");
        sb.append("<td align=right>");
        sb.append("<FONT COLOR=red>");
        sb.append(label);
        sb.append("</FONT>");
        sb.append("</td>");
        sb.append("<td>");
        sb.append(value);
        sb.append("</td>");
        sb.append("</tr>");
        return sb;
    }

    private StringBuilder tooltipLabelValueA(StringBuilder sb, String label, String value, String label1, String value1) {
        sb.append("<tr>");
        sb.append("<td align=right>");
        sb.append("<FONT COLOR=red>");
        sb.append(label);
        sb.append("</FONT>");
        sb.append("</td>");
        sb.append("<td>");
        sb.append(value);
        sb.append("</td>");
        sb.append("<td>");
        sb.append("&nbsp;&nbsp;&nbsp;");
        sb.append("</td>");
        sb.append("<td align=right>");
        sb.append("<FONT COLOR=red>");
        sb.append(label1);
        sb.append("</FONT>");
        sb.append("</td>");
        sb.append("<td>");
        sb.append(value1);
        sb.append("</td>");
        sb.append("</tr>");
        return sb;
    }

    public String showRates(String strIndex, HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        String br = "<br>";
        sb.append("<HTML><BODY><FONT COLOR=#008000>");
        sb.append("RATES:");
        sb.append("</FONT>");
        sb.append(br);
        if (CommonUtils.isNotEmpty(strIndex)) {
            HttpSession session = request.getSession();
            int index = Integer.parseInt(strIndex);
            LclSession lclSession = (LclSession) session.getAttribute("lclSession");
            if (lclSession != null && lclSession.getRoutingOptionsList() != null && lclSession.getRoutingOptionsList().size() > 0) {
                RoutingOptionsBean routingOptionsBean = lclSession.getRoutingOptionsList().get(index);
                if (routingOptionsBean.getQuoteRateList() != null && routingOptionsBean.getQuoteRateList().size() > 0) {
                    for (int i = 0; i < routingOptionsBean.getQuoteRateList().size(); i++) {
                        ChargesInfoBean cinfobean = routingOptionsBean.getQuoteRateList().get(i);
                        sb.append(cinfobean.getChargesDesc() + ":         " + NumberUtils.convertToTwoDecimal(cinfobean.getRate().doubleValue()));
                        sb.append(br);
                    }//end of for loop
                }// end of routingOptionsBean.getQuoteRateList() if condition
                if (routingOptionsBean.getCtsAmount() != null && !routingOptionsBean.getCtsAmount().trim().equals("")) {
                    sb.append("INLAND:         " + routingOptionsBean.getCtsAmount().substring(1, routingOptionsBean.getCtsAmount().length()));
                    sb.append(br);
                }

            }
        }
        sb.append("</BODY></HTML>");
        return sb.toString();
    }

    public String displayRates(String rates) {
        StringBuilder sb = new StringBuilder();
        String br = "<br>";
        sb.append("<HTML><BODY><FONT size='0' COLOR=#008000>");
        sb.append("RATES:");
        sb.append("</FONT>");
        sb.append(br);
        String ocnFrt = rates.substring(0, rates.indexOf('\n'));
        String ttRev = rates.substring(rates.indexOf('\n') + 1);
        if (CommonUtils.isNotEmpty(rates)) {
            sb.append("<FONT size='0'>");
            sb.append("<table>");
            sb.append("<tr><td>");
            sb.append("<FONT color=red>");
            sb.append("OCNFRT :");
            sb.append("</FONT>");
            sb.append("</td><td>");
            sb.append(ocnFrt);
            sb.append("</td></tr>");
            sb.append("<tr><td>");
            sb.append("<FONT color=red>");
            sb.append("TTREV :");
            sb.append("</FONT>");
            sb.append("</td><td>");
            sb.append(ttRev);
            sb.append("</td></tr>");
            sb.append("</table>");
            sb.append(br);
            sb.append("</FONT>");
        }
        sb.append("</BODY></HTML>");
        return sb.toString();
    }

    public String showCloseRemarks(String strHeaderId) throws Exception {
        StringBuilder sb = new StringBuilder();
        String br = "<br>";
        if (CommonUtils.isNotEmpty(strHeaderId)) {
            sb.append("<HTML><BODY><FONT COLOR=#008000>");
            sb.append("Closed By: ");
            Long headerId = Long.parseLong(strHeaderId);
            LclSsHeaderDAO lclssheaderdao = new LclSsHeaderDAO();
            LclSsHeader lclssheader = lclssheaderdao.findById(headerId);
            if (lclssheader.getClosedBy() != null) {
                sb.append(lclssheader.getClosedBy().getLoginName());
            }
            sb.append(br);
            sb.append("On: ");
            if (lclssheader.getClosedDatetime() != null) {
                sb.append(DateUtils.formatStringDateToAppFormat(lclssheader.getClosedDatetime()));
            }
            sb.append("</FONT>");
            sb.append(br);
            sb.append("</BODY></HTML>");
        }

        return sb.toString();
    }

    public String showAuditRemarks(String strHeaderId) throws Exception {
        StringBuilder sb = new StringBuilder();
        String br = "<br>";
        if (CommonUtils.isNotEmpty(strHeaderId)) {
            Long headerId = Long.parseLong(strHeaderId);
            LclSsHeaderDAO lclssheaderdao = new LclSsHeaderDAO();
            LclSsHeader lclssheader = lclssheaderdao.findById(headerId);
            sb.append("<HTML><BODY><FONT COLOR=#008000>");
            sb.append("Audited By: ");
            if (lclssheader.getAuditedBy() != null) {
                sb.append(lclssheader.getAuditedBy().getLoginName());
            }
            sb.append(br);
            sb.append("On: ");
            if (lclssheader.getAuditedDatetime() != null) {
                sb.append("On: ").append(DateUtils.formatStringDateToAppFormat(lclssheader.getAuditedDatetime()));
            }
            sb.append("</FONT>");
            sb.append(br);
            sb.append("</BODY></HTML>");
        }
        return sb.toString();
    }

    public String getPickupDisable() throws Exception {
        return LoadLogisoftProperties.getProperty("application.enableCTS");
    }

    public String[] getdeliverCargoDetails(String unlocCode) throws Exception {//filled by deliveryCargo details
        String deliveryWarehouse[] = new String[8];
        Object warehs[] = new LclWarehsDAO().LclDeliverCargo(unlocCode, "W");
        if (warehs != null) {
            if (warehs[0] != null && !warehs[0].equals("")) {
                deliveryWarehouse[0] = warehs[0].toString();
            }
            if (warehs[1] != null && !warehs[1].equals("")) {
                deliveryWarehouse[1] = warehs[1].toString();
            }
            if (warehs[2] != null && !warehs[2].equals("")) {
                deliveryWarehouse[2] = warehs[2].toString();
            }
            if (warehs[3] != null && !warehs[3].equals("")) {
                deliveryWarehouse[3] = warehs[3].toString();
            }
            if (warehs[4] != null && !warehs[4].equals("")) {
                deliveryWarehouse[4] = warehs[4].toString();
            }
            if (warehs[5] != null && !warehs[5].equals("")) {
                deliveryWarehouse[5] = warehs[5].toString();
            }
            if (warehs[6] != null && !warehs[6].equals("")) {
//                String phnumber = warehs[6].toString();
//                String pNoSpaceRemove = StringUtils.remove(phnumber, " ");
//                String ph1 = pNoSpaceRemove.substring(0, 3);
//                String ph2 = pNoSpaceRemove.substring(3, 6);
//                String ph3 = pNoSpaceRemove.substring(6);
                deliveryWarehouse[6] = warehs[6].toString();
            }
            if (warehs[7] != null && !warehs[7].equals("")) {
//                String faxNumber = warehs[7].toString();
//                String faxNoSpaceRemove = StringUtils.remove(faxNumber, " ");
//                String ph1 = faxNoSpaceRemove.substring(0, 3);
//                String ph2 = faxNoSpaceRemove.substring(3, 6);
//                String ph3 = faxNoSpaceRemove.substring(6);
                deliveryWarehouse[7] = warehs[7].toString();
            }
        }
        return deliveryWarehouse;
    }

    public String[] getdeliverCargoDetailsForWhse(String originUnlocationCode) throws Exception {
        String databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        LclWarehsDAO lclwarehsDAO = new LclWarehsDAO(databaseSchema);
        String strWarehouse[] = new String[6];
        Object warehs[] = lclwarehsDAO.LclDeliverCargo(originUnlocationCode, "W");
        if (warehs != null) {
            if (warehs[0] != null && !warehs[0].equals("")) {
                strWarehouse[0] = warehs[1].toString() + "-" + warehs[0].toString();
            }
            if (warehs[2] != null && !warehs[2].equals("")) {
                strWarehouse[1] = warehs[2].toString();
            }
            if (warehs[3] != null && !warehs[3].equals("")) {
                strWarehouse[2] = warehs[3].toString();
            }
            if (warehs[4] != null && !warehs[4].equals("")) {
                strWarehouse[3] = warehs[4].toString();
            }
            if (warehs[5] != null && !warehs[5].equals("")) {
                strWarehouse[4] = warehs[5].toString();
            }
            if (warehs[6] != null && !warehs[6].equals("")) {
                strWarehouse[5] = warehs[6].toString();
            }
        }
        return strWarehouse;
    }

//    public String getTerminal(String rateType) throws Exception {//remove
//        if (rateType.equalsIgnoreCase("R")) {
//            rateType = "Y";
//        }
//        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
//        User user = (User) request.getSession().getAttribute("loginuser");
//        String trmnum = user.getTerminal().getTrmnum();
//        String unLocationCode = "";
//        String terminal = "";
//        RefTerminalDAO refterminaldao = new RefTerminalDAO();
//        RefTerminal refterminal = refterminaldao.getByProperty("trmnum", trmnum);
//        if (refterminal != null && refterminal.getUnLocationCode1() != null && !refterminal.getUnLocationCode1().trim().equals("")) {
//            unLocationCode = refterminal.getUnLocationCode1();
//        }
//        refterminal = refterminaldao.getTerminalByUnLocation(unLocationCode, rateType);
//        //if(trmnum.equals(termNumber)){
//        if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
//            terminal = refterminal.getTerminalLocation() + "/" + refterminal.getTrmnum();
//        }
//        //}
//        return terminal;
//    }
    public String[] getCommodityCode(String commCode) throws Exception {//remove
        commodityTypeDAO commodityTypeDAO = new commodityTypeDAO();
        String commodityDetails[] = commodityTypeDAO.defaultExportCommodity(commCode);
        return commodityDetails;
    }

    public boolean getdefaultErtValues(String commCode, String origin, String destination) throws Exception {
        if (CommonUtils.isNotEmpty(commCode)) {
            CommodityType commodityType = new commodityTypeDAO().getCommodityCode(commCode);
            if (commodityType != null && commodityType.getDefaultErt()) {
                String databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
                LCLRatesDAO lclratesdao = new LCLRatesDAO(databaseSchema);
                List commList = lclratesdao.getOfcertCommodityList(commCode);
                if (commList != null && !commList.isEmpty()) {
                    PortsDAO portsDAO = new PortsDAO();
                    String originSchdNo = portsDAO.getShedulenumber(origin);
                    String destinationSchdNo = portsDAO.getShedulenumber(destination);
                    List list = lclratesdao.getOfcertList(originSchdNo, destinationSchdNo, commCode);
                    if (list != null && !list.isEmpty()) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public String displayPackageType(String pkgType) throws Exception {
        if (!pkgType.equals("") && pkgType != null) {
            PackageType packageType = new PackageTypeDAO().getByProperty("description", pkgType);
            if (packageType != null) {
                return packageType.getDescription() + "==" + packageType.getId();
            }
        }
        return null;
    }

    public GenericCode getCommodityCodeInfo(String agentNumber) throws Exception {
        if (agentNumber != null && !agentNumber.equals("")) {
            GeneralInformation generalInformation = new GeneralInformationDAO().getGeneralInformationByAccountNumber(agentNumber);
            if (generalInformation != null) {
                GenericCode genericCode = generalInformation.getImpCommodity();
                if (genericCode != null) {
                    return genericCode;
                }
            }
        }
        return null;
    }

    public String getCommodityInfo(String agentNumber) throws Exception {
        String commodity = "";
        if (agentNumber != null && !agentNumber.equals("")) {
            GeneralInformation generalInformation = new GeneralInformationDAO().getGeneralInformationByAccountNumber(agentNumber);
            if (generalInformation != null) {
                Integer impCommNo = generalInformation.getImpCommodity().getId();
                GenericCode genericCode = new GenericCodeDAO().findById(impCommNo);
                if (genericCode != null) {
                    String code = genericCode.getCode();
                    if (code != null && !code.equals("")) {
                        CommodityType commodityType = new commodityTypeDAO().getCommodityCode(code);
                        if (commodityType != null) {
                            String desc = commodityType.getDescEn();
                            Long id = commodityType.getId();
                            String commcode = commodityType.getCode();
                            commodity = desc + "/" + commcode + "-" + id;
                        }
                    }
                }
            }
        }
        return commodity;
    }

    public String doorOriginCity(String fileNumberId) throws Exception {
        LclQuotePad lclQuotePad = new LclQuotePadDAO().getLclQuotePadByFileNumber(Long.parseLong(fileNumberId));
        String doorCity = "";
        if (lclQuotePad != null) {
            doorCity = lclQuotePad.getPickupContact().getCity();
        }
        return doorCity;
    }

//    public boolean checkHaz(String fileId) throws Exception {
//        List<LclBookingPiece> lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileId));
//        LclHazmatDAO lclbookinghazmatdao = new LclHazmatDAO();
//        for (LclBookingPiece lclBookingPiece : lclBookingPiecesList) {
//            if (lclBookingPiece != null) {
//                if (lclbookinghazmatdao.findByFileAndCommodity(Long.parseLong(fileId), lclBookingPiece.getId()) != null) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
    public boolean checkHaz(String fileId) throws Exception {
        return new LclHazmatDAO().getHazStatus(fileId);
    }

    public boolean qtHotCodeValidate(String fileId) throws Exception {
        return new LclHazmatDAO().getQtHotCodeStatus(fileId);
    }

    public boolean qtHazmatCodeExit(String fileId) throws Exception {
        return new LclBookingHotCodeDAO().qtHazmatCodeExit(fileId);
    }

    public boolean checkHotCodes(String fileId) throws Exception {
        Lcl3pRefNoDAO lcl3pRefNoDAO = new Lcl3pRefNoDAO();
        lcl3pRefNoDAO.getLclHscCodeListByType(fileId, "HTC");
        List list = lcl3pRefNoDAO.executeSQLQuery("SELECT COUNT(3p.id) FROM lcl_3p_ref_no 3p JOIN genericcode_dup g ON codetypeid=57 AND 3p.reference=CONCAT(g.code,'/',g.codedesc) WHERE 3p.type='HTC' AND file_number_id=" + fileId + " AND field1='Y'");
        Object countResult = list.get(0);
        if (countResult.toString().equals("0")) {
            return true;
        }
        return false;
    }

    public boolean checkQuoteHazmat(String fileId) throws Exception {
        List<LclQuotePiece> lclQuotePieceList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileId));
        LclQuoteHazmatDAO lclQuoteHazmatDAO = new LclQuoteHazmatDAO();
        for (LclQuotePiece lclQuotePiece : lclQuotePieceList) {
            if (lclQuotePiece != null) {
                if (lclQuoteHazmatDAO.findByFileAndCommodity(Long.parseLong(fileId), lclQuotePiece.getId()) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkBookingVoyage(String moduleName, String id, String condition) throws Exception {
        if (!LclCommonConstant.LCL_IMPORT.equalsIgnoreCase(moduleName)) {
            if (condition.equalsIgnoreCase("goback")) {
                return false;
            }
            RoleDuty roleDuty = new RoleDutyDAO().getRoleDetails(Integer.parseInt(id));
            if (roleDuty != null && roleDuty.isByPassVoyage() == true) {
                return true;
            }
        }
        return false;
    }

    public boolean checkUnPost(String blEnterByroleId) throws Exception {
        return new RoleDutyDAO().getRoleDetails(BL_UN_POST, Integer.parseInt(blEnterByroleId));
    }

    public boolean isbookingTerminate(String id) throws Exception {
        return new RoleDutyDAO().getRoleDetails(BKG_IMP_TERMINATE, Integer.parseInt(id));
    }

    public String showEmailIdOutsource(String terminalId) throws Exception {
        String terminalEmail = new TerminalDAO().defaultTerminalEmail(terminalId);
        return terminalEmail;
    }
// destinationRemarks page lclExportBooking.js and methodName destinationRemarks()

    public String[] defaultDestinationRemarks(String fdUnlocationcode, String podUnlocationcode) throws Exception {
        LCLPortConfigurationDAO lclPortConfigurationDAO = new LCLPortConfigurationDAO();
        String remarks[] = lclPortConfigurationDAO.lclDefaultDestinationRemarks(fdUnlocationcode, podUnlocationcode);
        return remarks;
    }

    public String[] defaultDestinationImportRemarks(String podUnlocationcode) throws Exception {
        LCLPortConfigurationDAO lclPortConfigurationDAO = new LCLPortConfigurationDAO();
        String remarks[] = lclPortConfigurationDAO.lclDefaultDestinationImportRemarks(podUnlocationcode);
        return remarks;
    }

    public String checkBarrelRate(String origin, String destination, String rateType, String commodityNo) throws Exception {
        String flag = "false";
        rateType = "R".equalsIgnoreCase(rateType) ? "Y" : rateType;
        String databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        LCLRatesDAO lclratesdao = new LCLRatesDAO(databaseSchema);
        String pooTrmnum = new RefTerminalDAO().getTrmnum(origin, rateType);
        String fdEciPortCode = new PortsDAO().getPortValue(PORTS_ECIPORTCODE, destination);
        if (pooTrmnum != null && !"".equalsIgnoreCase(pooTrmnum) && fdEciPortCode != null && !"".equalsIgnoreCase(fdEciPortCode)) {
            String barrelRate = lclratesdao.findBarrelRate(pooTrmnum, fdEciPortCode, commodityNo);
            if (CommonUtils.isNotEmpty(barrelRate) && CommonUtils.isNotEmpty(Double.parseDouble(barrelRate))) {
                flag = "true";
            }
        }
        return flag;
    }

    public boolean lclBatchTermination(String status, String terminateOption, String comment, String userId, String consolidateTerm, String fileNumber) throws Exception {
        LclFileNumberDAO fileNumberDAO = new LclFileNumberDAO();
        Map<String, String> fileMap = new HashMap<>();
        String[] fileNos = fileNumber.split(",");
        for (String fileNo : fileNos) {
            if (CommonUtils.isNotEmpty(fileNo)) {
                Long fileId = fileNumberDAO.getFileForTerminate(fileNo.trim());
                if (CommonUtils.isNotEmpty(fileId)) {
                    fileMap.put(fileId.toString(), fileNo);
                }
            }
        }
        for (Map.Entry<String, String> entry : fileMap.entrySet()) {
            String key = entry.getKey();
            lclDomTermination(key, status, terminateOption, comment, userId, consolidateTerm);
        }
        return true;
    }

    public boolean lclDomTermination(String fileId, String status, String terminateOption, String comment, String userId, String consolidateTerm) throws Exception {//Terminate Bkg
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        if (CommonUtils.isNotEmpty(consolidateTerm) && consolidateTerm.equals("Y")) {
            if (CommonUtils.isNotEmpty(fileId)) {
                List<Long> consDescList = new LclConsolidateDAO().getUnTerminateConsolidatesFiles(Long.parseLong(fileId));
                if (CommonUtils.isNotEmpty(consDescList)) {
                    for (Long consoFiledId : consDescList) {
                        // fileId = fileId + consoFiledId.toString() ;
                        if (!new LclConsolidateDAO().isReleased(consoFiledId)) {
                            new LclFileNumberDAO().updateLclFileNumbersStatus(consoFiledId.toString(), status);
                            new LCLBookingDAO().updateTerminateDesc(consoFiledId.toString(), terminateOption);
                            if (CommonUtils.isNotEmpty(comment)) {
                                lclRemarksDAO.insertLclRemarks(consoFiledId, REMARKS_DR_AUTO_NOTES, comment, Integer.parseInt(userId));
                            }
                            if (null != status && status.equals("X")) {
                                lclRemarksDAO.insertLclRemarks(consoFiledId, "T", "Cargo Terminated -- " + terminateOption, Integer.parseInt(userId));
                            }
                        }
                    }
                }
                new LclFileNumberDAO().updateLclFileNumbersStatus(fileId, status);
                new LCLBookingDAO().updateTerminateDesc(fileId, terminateOption);
                if (CommonUtils.isNotEmpty(comment)) {
                    lclRemarksDAO.insertLclRemarks(Long.parseLong(fileId), REMARKS_DR_AUTO_NOTES, comment, Integer.parseInt(userId));
                }
                if (null != status && status.equals("X")) {
                    lclRemarksDAO.insertLclRemarks(Long.parseLong(fileId), "T", "Cargo Terminated -- " + terminateOption, Integer.parseInt(userId));

                }
            }
        } else {
            new LclFileNumberDAO().updateLclFileNumbersStatus(fileId, status);
            new LCLBookingDAO().updateTerminateDesc(fileId, terminateOption);
            if (CommonUtils.isNotEmpty(comment)) {
                lclRemarksDAO.insertLclRemarks(Long.parseLong(fileId), REMARKS_DR_AUTO_NOTES, comment, Integer.parseInt(userId));
            }
            if (null != status && status.equals("X")) {
                lclRemarksDAO.insertLclRemarks(Long.parseLong(fileId), "T", "Cargo Terminated -- " + terminateOption, Integer.parseInt(userId));

            }
        }
        return true;
    }

    public boolean lclSaveInvoiceComments(String fileId, String status, String comment, String invoiceComment, String userId) throws Exception {//Terminate Bkg
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        String invoiceComments = "Terminate Skip - No Invoice Comment-->" + invoiceComment;
        lclRemarksDAO.insertLclRemarks(Long.parseLong(fileId), REMARKS_DR_AUTO_NOTES, invoiceComments.toUpperCase(), Integer.parseInt(userId));
        return true;
    }

    public String lclUnTerminationStatus(String fileId, String loginId) throws Exception {//Restore Option for Bkg
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        lclRemarksDAO.insertLclRemarks(Long.parseLong(fileId), "T", "Cargo UnTerminated", Integer.parseInt(loginId));
        new LclFileNumberDAO().updatePreviousStatus(fileId);
        return "";
    }

    public String removePickupInfo(String fileNumberId) throws Exception {
        String flag = "false";
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            LclBookingPadDAO lclBookingPadDAO = new LclBookingPadDAO();
            LclManifestDAO lclManifestDAO = new LclManifestDAO();
            LclBookingPad lclBookingPad = lclBookingPadDAO.getLclBookingPadByFileNumber(Long.parseLong(fileNumberId));
            if (lclBookingPad != null) {
                try {
                    lclManifestDAO.deleteLclAccruals(lclBookingPad.getLclBookingAc().getId().intValue(), new LclFileNumberDAO().getFileNumberByFileId(fileNumberId));
                    lclBookingPadDAO.deleteBookingPadByFileId(Long.parseLong(fileNumberId));
                } catch (Exception e) {
                    log.info("Error on removePickupInfo in LclDwr", e);
                }
            }
        }
        return flag;
    }

    public String removeQuotePickupInfo(String fileNumberId) throws Exception {//delete
        String flag = "false";
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            LclQuotePadDAO lclQuotePadDAO = new LclQuotePadDAO();
            LclQuotePad lclQuotePad = lclQuotePadDAO.getLclQuotePadByFileNumber(Long.parseLong(fileNumberId));
            if (lclQuotePad != null) {
                lclQuotePadDAO.delete(lclQuotePad);
            }
        }
        return flag;
    }

    public String searchFiles(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        LclSession lclSession = null != request.getSession().getAttribute("lclSession") ? (LclSession) request.getSession().getAttribute("lclSession") : new LclSession();
        lclSession.setSearchResult("false");
        session.setAttribute("lclSession", lclSession);
        return "true";
    }

    public boolean checkingRates(String fileId) throws Exception {
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        String isRates = lclCostChargeDAO.isChargeCodeValidate(String.valueOf(fileId), "", "0");
        if ("true".equalsIgnoreCase(isRates)) {
            return true;
        }
        return false;
    }

    public boolean getQuoteWhseDetails(String fileNumberId) throws Exception {
        if (fileNumberId != null && !fileNumberId.equals("")) {
            LclQuotePad lclQuotePad = new LclQuotePadDAO().getLclQuotePadByFileNumber(Long.parseLong(fileNumberId));
            if (lclQuotePad != null && lclQuotePad.getDeliveryContact().getCompanyName() != null && !lclQuotePad.getDeliveryContact().getCompanyName().equals("")) {
                return true;
            }
        }
        return false;
    }

    public boolean commodity(String fileNumberId) throws Exception {
        List<LclBookingPiece> lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
        if (CommonUtils.isNotEmpty(lclBookingPiecesList)) {
            return true;
        }
        return false;
    }

    public String[] deleteRates(String fileNumberId, HttpServletRequest request) throws Exception {
        String[] data = new String[2];
        Double total = 0.00;
        try {
            LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
            LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
            User user = (User) request.getSession().getAttribute("loginuser");
            List<LclBookingAc> lclBookingAclist = lclCostChargeDAO.findByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
            if (CommonUtils.isNotEmpty(lclBookingAclist)) {
                for (LclBookingAc lclBookingAc : lclBookingAclist) {
                    if (lclBookingAc.getArglMapping().getChargeCode() != null && !lclBookingAc.isManualEntry()) {
                        String remarks = "DELETED -> Charge Code -> " + lclBookingAc.getArglMapping().getChargeCode()
                                + " Charge Amount -> " + lclBookingAc.getArAmount();
                        lclRemarksDAO.insertLclRemarks(Long.parseLong(fileNumberId), REMARKS_DR_AUTO_NOTES, remarks, user.getUserId());
                        lclCostChargeDAO.delete(lclBookingAc);
                    } else {
                        total = total + lclBookingAc.getArAmount().doubleValue();
                    }
                }
            }
        } catch (Exception e) {
        }
        data[0] = "true";
        data[1] = total.toString();
        return data;
    }

    public String[] deleteQuoteRates(String fileNumberId, String userId) throws Exception {
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        String[] data = new String[2];
        Double total = 0.00;
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
        int loginId = Integer.parseInt(userId);
        List<LclQuoteAc> lclQuoteAcList = lclQuoteAcDAO.getLclCostByFileNumberME(Long.parseLong(fileNumberId), true);
        try {
            if (CommonUtils.isNotEmpty(lclQuoteAcList)) {
                for (LclQuoteAc lclQuoteAc : lclQuoteAcList) {
                    if (lclQuoteAc.getArglMapping().getChargeCode() != null && !lclQuoteAc.isManualEntry()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("DELETED -> Code -> ").append(lclQuoteAc.getArglMapping().getChargeCode());
                        sb.append(" Charge Amount -> ").append(lclQuoteAc.getArAmount());
                        lclRemarksDAO.insertLclRemarks(Long.parseLong(fileNumberId), REMARKS_QT_AUTO_NOTES, sb.toString(), loginId);
                        lclCostChargeDAO.delete(lclQuoteAc);
                    } else {
                        total = total + lclQuoteAc.getArAmount().doubleValue();

                    }
                }
            }
        } catch (Exception e) {
        }
        data[0] = "true";
        data[1] = total.toString();
        return data;
    }

    public boolean deleteBlRates(String fileNumberId) throws Exception {
        new LclBlAcDAO().deleteManualAutoRates(Long.parseLong(fileNumberId));
        return true;
    }

    public boolean deleteQuoteRate(String fileNumberId) throws Exception {
        new LclQuoteAcDAO().deleteManualAutoRates(Long.parseLong(fileNumberId));
        return true;
    }

    public boolean checkingQuoteRates(String fileNumberId) throws Exception {
        Integer ratesCount = new LclQuoteAcDAO().getLclChargeCountbyFileNumber(Long.parseLong(fileNumberId), 0);
        if (null != ratesCount && ratesCount > 0) {
            return true;
        }
        return false;
    }

    public boolean cargoReleaseStatus(String fileId, HttpServletRequest request) throws Exception {
        LclFileNumberDAO lclFileNumberDao = new LclFileNumberDAO();
        LclFileNumber lclFileNumber = lclFileNumberDao.getByProperty("id", Long.parseLong(fileId));
        User user = (User) request.getSession().getAttribute("loginuser");
        lclFileNumber.setStatus("R");
        LclRemarks lclremarks = new LclRemarks();
        lclremarks.setLclFileNumber(lclFileNumber);
        lclremarks.setType("T");
        LclUtils lclutils = new LclUtils();
        lclremarks.setRemarks("Cargo Released for Export");
        lclutils.insertLCLRemarks(lclFileNumber.getId(), "Inventory Status->Released", REMARKS_DR_AUTO_NOTES, user);
        lclremarks.setEnteredBy(user);
        lclremarks.setEnteredDatetime(new Date());
        lclremarks.setModifiedBy(user);
        lclremarks.setModifiedDatetime(new Date());
        new LclRemarksDAO().save(lclremarks);
        lclFileNumberDao.saveOrUpdate(lclFileNumber);
        return true;
    }

    public boolean commodityNum(String commNum, String fileId, String commCode, String hazmat,
            String Barrel, String commId, HttpServletRequest request) throws Exception {
        if (null != fileId && Long.parseLong(fileId) > 0) {
            List<LclBookingPiece> lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileId));
            for (LclBookingPiece lclBookingPiece : lclBookingPiecesList) {
                if (null != lclBookingPiece.getCommodityType()
                        && null != lclBookingPiece.getCommodityType().getCode()
                        && !"".equals(lclBookingPiece.getCommodityType().getCode())
                        && commNum.equalsIgnoreCase(lclBookingPiece.getCommodityType().getCode())
                        && (((null != hazmat) && (Boolean.parseBoolean(hazmat) == lclBookingPiece.isHazmat()))
                        && ((null != Barrel) && (Boolean.parseBoolean(Barrel) == lclBookingPiece.isIsBarrel())))
                        && ("".equals(commId) || (Long.parseLong(commId) != lclBookingPiece.getId()))) {
                    return true;
                }
            }
        } else {
            HttpSession session = request.getSession();
            LclSession lclSession = (LclSession) session.getAttribute("lclSession");
            if (null != lclSession && null != lclSession.getCommodityList() && !lclSession.getCommodityList().isEmpty()) {
                List<LclBookingPiece> lclBookingPiecesList = lclSession.getCommodityList();
                for (LclBookingPiece lclBookingPiece : lclBookingPiecesList) {
                    if (null != lclBookingPiece.getCommNo() && null != commNum
                            && CommonUtils.isNotEmpty(lclBookingPiece.getCommNo()) && commNum.equalsIgnoreCase(lclBookingPiece.getCommNo())
                            && (((null != hazmat) && (Boolean.parseBoolean(hazmat) == lclBookingPiece.isHazmat()))
                            && ((null != Barrel) && (Boolean.parseBoolean(Barrel) == lclBookingPiece.isIsBarrel())))) {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public boolean commodityNumForBl(String commoNo, String fileId) throws Exception {
        List<LclBlPiece> lclBlPiecesList = new LclBLPieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileId));
        for (LclBlPiece lclBlPiece : lclBlPiecesList) {
            if (lclBlPiece.getCommodityType().getCode().equalsIgnoreCase(commoNo)) {
                return true;
            }
        }
        return false;
    }

    public boolean chargeCodeValidate(String chargeCode, String fileId) throws Exception {
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        String chargeFlag = lclCostChargeDAO.isChargeCodeValidate(fileId, chargeCode, "true");
        if ("true".equalsIgnoreCase(chargeFlag)) {
            return true;
        }
        return false;
    }

    public String[] chargeCostValidate(String chargeCode, String shipmentType) throws Exception {
        LclBookingUtils lclBookingUtils = new LclBookingUtils();
        String data[] = lclBookingUtils.validateForChargeandCost(chargeCode, shipmentType);
        return data;
    }

    public boolean chargeCodeValidateForQuote(String chargeCode, String editchargeCode, String fileId) throws Exception {
        LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
        String chargeFlag = lclQuoteAcDAO.isChargeCodeValidate(fileId, chargeCode, "true");
        if ("true".equalsIgnoreCase(chargeFlag)) {
            return true;//remove
        }
        return false;
    }

    public boolean chargeCodeValidateForBl(String chargeCode, String editchargeCode, String fileId) throws Exception {
        if (chargeCode.equalsIgnoreCase(editchargeCode)) {
            return false;
        } else {
            if (fileId != null && !fileId.equals("")) {
                return new LclBlAcDAO().isChargeCodeValidate(fileId, chargeCode, "");
            }
        }
        return false;
    }

    public boolean quoteCommodityNum(String commNum, String fileId, String commCode, String hazmat, String Barrel, String commId, HttpServletRequest request) throws Exception {
        if (null != fileId && Long.parseLong(fileId) > 0) {
            List<LclQuotePiece> lclQuotePiecesList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", new Long(fileId));
            for (LclQuotePiece lclQuotePiece : lclQuotePiecesList) {
                if (null != lclQuotePiece.getCommodityType()
                        && null != lclQuotePiece.getCommodityType().getCode()
                        && !"".equals(lclQuotePiece.getCommodityType().getCode())
                        && commNum.equalsIgnoreCase(lclQuotePiece.getCommodityType().getCode())
                        && (((null != hazmat) && (Boolean.parseBoolean(hazmat) == lclQuotePiece.isHazmat())) && ((null != Barrel) && (Boolean.parseBoolean(Barrel) == lclQuotePiece.isIsBarrel())))
                        && ("".equals(commId) || (Long.parseLong(commId) != lclQuotePiece.getId()))) {
                    return true;
                }
            }
        } else {
            HttpSession session = request.getSession();
            LclSession lclSession = (LclSession) session.getAttribute("lclSession");
            if (lclSession != null && lclSession.getQuoteCommodityList() != null && lclSession.getQuoteCommodityList().size() > 0) {
                List<LclQuotePiece> lclQuotePiecesList = lclSession.getQuoteCommodityList();
                for (LclQuotePiece lclQuotePiece : lclQuotePiecesList) {
                    if (null != lclQuotePiece.getCommodityType()
                            && null != lclQuotePiece.getCommodityType().getCode()
                            && !"".equals(lclQuotePiece.getCommodityType().getCode())
                            && commNum.equalsIgnoreCase(lclQuotePiece.getCommodityType().getCode())
                            && (((null != hazmat) && (Boolean.parseBoolean(hazmat) == lclQuotePiece.isHazmat()))
                            && ((null != Barrel) && (Boolean.parseBoolean(Barrel) == lclQuotePiece.isIsBarrel())))) {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public boolean deleteRate(String id, String fileId, String bookPieceCount, String bookVolumeImp, String bookWeigtImp, String bookedPieceCount,
            String bookedVolumeImperial, String bookedWeightImperial, String commCode, String commNo, String editbarrel, String barrel, String actPieceCount,
            String actVolumeImp, String actWeigtImp, String actualWeightImperial, String actualVolumeImperial, String actualPieceCount,
            HttpServletRequest request) throws Exception {
        boolean found = false;
        if (CommonUtils.isNotEmpty(fileId) && !fileId.equals("0")) {
            LclBooking lclBooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", Long.parseLong(fileId));
            String status = lclBooking.getLclFileNumber().getStatus();
            if (editbarrel.equalsIgnoreCase("true")) {
                editbarrel = "Y";
            } else {
                editbarrel = "N";
            }
            LclBookingPiece lclBookingPiece = null;
            HttpSession session = request.getSession();
            LclSession lclSession = (LclSession) session.getAttribute("lclSession");
            if ("R".equalsIgnoreCase(status) || "W".equalsIgnoreCase(status)) {
                if (bookedWeightImperial.equalsIgnoreCase(actualWeightImperial) && bookedVolumeImperial.equalsIgnoreCase(actualVolumeImperial)
                        && commCode.equalsIgnoreCase(commNo) && bookedPieceCount.equalsIgnoreCase(actualPieceCount)
                        && editbarrel.equalsIgnoreCase(barrel)) {
                    return true;
                } else {
                    if (id != null && !id.trim().equals("")) {
                        lclBookingPiece = new LclBookingPieceDAO().executeUniqueQuery("from LclBookingPiece where id=" + Integer.parseInt(id) + " AND lclFileNumber.id=" + Long.parseLong(fileId) + "");
                        if ((lclBookingPiece.getActualPieceCount() == null || lclBookingPiece.getActualPieceCount().intValue() == 0) && (actualPieceCount == null || actualPieceCount.trim().equals("") || actualPieceCount.trim().equals("0"))) {
                            found = false;
                        } else if (lclBookingPiece.getActualPieceCount() != null && !lclBookingPiece.getActualPieceCount().toString().equalsIgnoreCase(actualPieceCount)) {
                            found = false;
                        }
                        if ((lclBookingPiece.getActualVolumeImperial() == null || lclBookingPiece.getActualVolumeImperial().intValue() == 0) && (actualVolumeImperial == null || !actualVolumeImperial.trim().equals("") || !actualVolumeImperial.trim().equals("0"))) {
                            found = false;
                        } else if (!lclBookingPiece.getActualVolumeImperial().toString().equalsIgnoreCase(actualVolumeImperial)) {
                            found = false;
                        }
                        if ((lclBookingPiece.getActualWeightImperial() == null || lclBookingPiece.getActualWeightImperial().intValue() == 0) && (!actualWeightImperial.trim().equals("") || actualWeightImperial == null || !actualWeightImperial.trim().equals("0"))) {
                            found = false;
                        } else if (!lclBookingPiece.getActualWeightImperial().toString().equalsIgnoreCase(actualWeightImperial)) {
                            found = false;
                        }
                        if (!lclBookingPiece.getCommodityType().getCode().equalsIgnoreCase(commNo) && lclBookingPiece.isIsBarrel() != Boolean.parseBoolean(barrel)) {
                            found = false;
                        }
                    }
                }
            } else {
                if (bookVolumeImp.equalsIgnoreCase(bookedVolumeImperial) && bookWeigtImp.equalsIgnoreCase(bookedWeightImperial)
                        && commCode.equalsIgnoreCase(commNo) && bookPieceCount.equalsIgnoreCase(bookedPieceCount)
                        && editbarrel.equalsIgnoreCase(barrel)) {
                    return true;
                } else {
                    if (id != null && !id.trim().equals("")) {
                        lclBookingPiece = new LclBookingPieceDAO().executeUniqueQuery("from LclBookingPiece where id=" + Integer.parseInt(id) + " AND lclFileNumber.id=" + Long.parseLong(fileId) + "");
                        if ((lclBookingPiece.getBookedPieceCount() == null || lclBookingPiece.getBookedPieceCount().intValue() == 0) && (bookedPieceCount == null || bookedPieceCount.trim().equals("") || bookedPieceCount.trim().equals("0"))) {
                            found = false;
                        } else if (!lclBookingPiece.getBookedPieceCount().toString().equalsIgnoreCase(bookedPieceCount)) {
                            found = false;
                        }
                        if ((lclBookingPiece.getBookedVolumeImperial() == null || lclBookingPiece.getBookedVolumeImperial().intValue() == 0) && (bookedVolumeImperial == null || !bookedVolumeImperial.trim().equals("") || !bookedVolumeImperial.trim().equals("0"))) {
                            found = false;
                        } else if (!lclBookingPiece.getBookedVolumeImperial().toString().equalsIgnoreCase(bookedVolumeImperial)) {
                            found = false;
                        }
                        if ((lclBookingPiece.getBookedWeightImperial() == null || lclBookingPiece.getBookedWeightImperial().intValue() == 0) && (!bookedWeightImperial.trim().equals("") || bookedWeightImperial == null || !bookedWeightImperial.trim().equals("0"))) {
                            found = false;
                        } else if (!lclBookingPiece.getBookedWeightImperial().toString().equalsIgnoreCase(bookedWeightImperial)) {
                            found = false;
                        }
                        if (!lclBookingPiece.getCommodityType().getCode().equalsIgnoreCase(commNo)) {
                            found = false;
                        }
                        if (lclBookingPiece.isIsBarrel() != Boolean.parseBoolean(barrel)) {
                            found = false;
                        }
                    } else {
                        if (lclSession != null && lclSession.getCommodityList() != null && lclSession.getCommodityList().size() > 0) {
                            List lclBookingPiecesList = lclSession.getCommodityList();
                            for (int i = 0; i < lclBookingPiecesList.size(); i++) {
                                lclBookingPiece = (LclBookingPiece) lclBookingPiecesList.get(i);
                                if ((lclBookingPiece.getBookedPieceCount() == null || lclBookingPiece.getBookedPieceCount().intValue() == 0) && (bookedPieceCount == null || bookedPieceCount.trim().equals("") || bookedPieceCount.trim().equals("0"))) {
                                    found = false;
                                } else if (!lclBookingPiece.getBookedPieceCount().toString().equalsIgnoreCase(bookedPieceCount)) {
                                    found = false;
                                }
                                if ((lclBookingPiece.getBookedVolumeImperial() == null || lclBookingPiece.getBookedVolumeImperial().intValue() == 0) && (bookedVolumeImperial == null || !bookedVolumeImperial.trim().equals("") || !bookedVolumeImperial.trim().equals("0"))) {
                                    found = false;
                                } else if (!lclBookingPiece.getBookedVolumeImperial().toString().equalsIgnoreCase(bookedVolumeImperial)) {
                                    found = false;
                                }
                                if ((lclBookingPiece.getBookedWeightImperial() == null || lclBookingPiece.getBookedWeightImperial().intValue() == 0) && (!bookedWeightImperial.trim().equals("") || bookedWeightImperial == null || !bookedWeightImperial.trim().equals("0"))) {
                                    found = false;
                                } else if (!lclBookingPiece.getBookedWeightImperial().toString().equalsIgnoreCase(bookedWeightImperial)) {
                                    found = false;
                                }
                                if (!lclBookingPiece.getCommodityType().getCode().equalsIgnoreCase(commNo)) {
                                    found = false;
                                }
                                if (lclBookingPiece.isIsBarrel() != Boolean.parseBoolean(barrel)) {
                                    found = false;
                                }
                            }

                        }
                    }

                }
            }
        }
        return found;
    }

//    public boolean deleteBlRate(String id, String fileId, String bookPieceCount, String bookVolumeImp, String bookWeigtImp, String bookedPieceCount,
//            String bookedVolumeImperial, String bookedWeightImperial, String commCode, String commNo, String editbarrel, String barrel, String actPieceCount, String actVolumeImp, String actWeigtImp, String actualWeightImperial, String actualVolumeImperial, String actualPieceCount) throws Exception {
//        Long fileNo = Long.parseLong(fileId);
//        LclBl lclBl = new LCLBlDAO().executeUniqueQuery("from LclBl where lclFileNumber.id=" + fileNo + "");
//        String status = lclBl.getLclFileNumber().getStatus();
//        boolean found = false;
//        if (editbarrel.equalsIgnoreCase("false")) {
//            editbarrel = "N";
//            found = true;
//        } else {
//            editbarrel = "Y";
//            found = true;
//        }
//        LclBlPiece lclBlPiece = null;
//        List<LclBlAc> lclBlAcList = new LclBlAcDAO().executeQuery("from LclBlAc where lclFileNumber.id=" + fileNo + "");
//        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
//        HttpSession session = request.getSession();
//        LclSession lclSession = (LclSession) session.getAttribute("lclSession");
////        if (status.equalsIgnoreCase("WV")) {
////            if (bookedWeightImperial.equalsIgnoreCase(actualWeightImperial) && bookedVolumeImperial.equalsIgnoreCase(actualVolumeImperial) && commCode.equalsIgnoreCase(commNo) && bookedPieceCount.equalsIgnoreCase(actualPieceCount) && editbarrel.equalsIgnoreCase(barrel)) {
////                return true;
////            }
//        if (actWeigtImp.equalsIgnoreCase(actualWeightImperial) && actVolumeImp.equalsIgnoreCase(actualVolumeImperial) && commCode.equalsIgnoreCase(commNo) && actPieceCount.equalsIgnoreCase(actualPieceCount) && editbarrel.equalsIgnoreCase(barrel)) {
//            return true;
//        } else {
//            if (fileNo != null && fileNo > 0) {
//                if (id != null && !id.trim().equals("")) {
//                    lclBlPiece = new LclBLPieceDAO().executeUniqueQuery("from LclBlPiece where id=" + Integer.parseInt(id) + " AND lclFileNumber.id=" + fileNo + "");
////                        if ((lclBlPiece.getActualPieceCount() == null || lclBlPiece.getActualPieceCount().intValue() == 0) && (actualPieceCount == null || actualPieceCount.trim().equals("") || actualPieceCount.trim().equals("0"))) {
////                            found = false;
////                        } else if (lclBlPiece.getActualPieceCount() != null && !lclBlPiece.getActualPieceCount().toString().equalsIgnoreCase(actualPieceCount)) {
////                            found = false;
////                        }
//                    if ((lclBlPiece.getActualVolumeImperial() == null || lclBlPiece.getActualVolumeImperial().intValue() == 0) && (actualVolumeImperial == null || !actualVolumeImperial.trim().equals("") || !actualVolumeImperial.trim().equals("0"))) {
//                        found = false;
//                    } else if (!lclBlPiece.getActualVolumeImperial().toString().equalsIgnoreCase(actualVolumeImperial)) {
//                        found = false;
//                    }
//                    if ((lclBlPiece.getActualWeightImperial() == null || lclBlPiece.getActualWeightImperial().intValue() == 0) && (!actualWeightImperial.trim().equals("") || actualWeightImperial == null || !actualWeightImperial.trim().equals("0"))) {
//                        found = false;
//                    } else if (!lclBlPiece.getActualWeightImperial().toString().equalsIgnoreCase(actualWeightImperial)) {
//                        found = false;
//                    }
//                    if (!lclBlPiece.getCommodityType().getCode().equalsIgnoreCase(commNo) && lclBlPiece.isIsBarrel() != Boolean.parseBoolean(barrel)) {
//                        found = false;
//                    }
//                }
//            }
//        }
////        }
////        else {
////            if (bookVolumeImp.equalsIgnoreCase(bookedVolumeImperial) && bookWeigtImp.equalsIgnoreCase(bookedWeightImperial) && commCode.equalsIgnoreCase(commNo) && bookPieceCount.equalsIgnoreCase(bookedPieceCount) && editbarrel.equalsIgnoreCase(barrel)) {
////                return true;
////            } else {
////                if (fileNo != null && fileNo > 0) {
////                    if (id != null && !id.trim().equals("")) {
////                        lclBlPiece = new LclBLPieceDAO().executeUniqueQuery("from LclBlPiece where id=" + Integer.parseInt(id) + " AND lclFileNumber.id=" + fileNo + "");
////                        if ((lclBlPiece.getBookedPieceCount() == null || lclBlPiece.getBookedPieceCount().intValue() == 0) && (bookedPieceCount == null || bookedPieceCount.trim().equals("") || bookedPieceCount.trim().equals("0"))) {
////                            found = false;
////                        } else if (!lclBlPiece.getBookedPieceCount().toString().equalsIgnoreCase(bookedPieceCount)) {
////                            found = false;
////                        }
////                        if ((lclBlPiece.getBookedVolumeImperial() == null || lclBlPiece.getBookedVolumeImperial().intValue() == 0) && (bookedVolumeImperial == null || !bookedVolumeImperial.trim().equals("") || !bookedVolumeImperial.trim().equals("0"))) {
////                            found = false;
////                        } else if (!lclBlPiece.getBookedVolumeImperial().toString().equalsIgnoreCase(bookedVolumeImperial)) {
////                            found = false;
////                        }
////                        if ((lclBlPiece.getBookedWeightImperial() == null || lclBlPiece.getBookedWeightImperial().intValue() == 0) && (!bookedWeightImperial.trim().equals("") || bookedWeightImperial == null || !bookedWeightImperial.trim().equals("0"))) {
////                            found = false;
////                        } else if (!lclBlPiece.getBookedWeightImperial().toString().equalsIgnoreCase(bookedWeightImperial)) {
////                            found = false;
////                        }
////                        if (!lclBlPiece.getCommodityType().getCode().equalsIgnoreCase(commNo) && lclBlPiece.isIsBarrel() != Boolean.parseBoolean(barrel)) {
////                            found = false;
////                        }
////                    }
////                } else {
////                    if (lclSession != null && lclSession.getCommodityList() != null && lclSession.getCommodityList().size() > 0) {
////                        List lclBlPiecesList = lclSession.getCommodityList();
////                        for (int i = 0; i < lclBlPiecesList.size(); i++) {
////                            lclBlPiece = (LclBlPiece) lclBlPiecesList.get(i);
////                            if ((lclBlPiece.getBookedPieceCount() == null || lclBlPiece.getBookedPieceCount().intValue() == 0) && (bookedPieceCount == null || bookedPieceCount.trim().equals("") || bookedPieceCount.trim().equals("0"))) {
////                                found = false;
////                            } else if (!lclBlPiece.getBookedPieceCount().toString().equalsIgnoreCase(bookedPieceCount)) {
////                                found = false;
////                            }
////                            if ((lclBlPiece.getBookedVolumeImperial() == null || lclBlPiece.getBookedVolumeImperial().intValue() == 0) && (bookedVolumeImperial == null || !bookedVolumeImperial.trim().equals("") || !bookedVolumeImperial.trim().equals("0"))) {
////                                found = false;
////                            } else if (!lclBlPiece.getBookedVolumeImperial().toString().equalsIgnoreCase(bookedVolumeImperial)) {
////                                found = false;
////                            }
////                            if ((lclBlPiece.getBookedWeightImperial() == null || lclBlPiece.getBookedWeightImperial().intValue() == 0) && (!bookedWeightImperial.trim().equals("") || bookedWeightImperial == null || !bookedWeightImperial.trim().equals("0"))) {
////                                found = false;
////                            } else if (!lclBlPiece.getBookedWeightImperial().toString().equalsIgnoreCase(bookedWeightImperial)) {
////                                found = false;
////                            }
////                            if (!lclBlPiece.getCommodityType().getCode().equalsIgnoreCase(commNo) && lclBlPiece.isIsBarrel() != Boolean.parseBoolean(barrel)) {
////                                found = false;
////                            }
////                        }
////
////                    }
////                }
////
////            }
////        }
//        return found;
//
//    }
//    public boolean deletingQuoteRate(String id, String fileId, String bookPieceCount, String bookVolumeImp, String bookWeigtImp, String bookedPieceCount,
//            String bookedVolumeImperial, String bookedWeightImperial, String commCode, String commNo, String editbarrel, String barrel,
//            String actPieceCount, String actVolumeImp, String actWeigtImp, String actualWeightImperial, String actualVolumeImperial,
//            String actualPieceCount, String actHazmat, String hazmat) throws Exception {//remove
//        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
//        HttpSession session = request.getSession();
//        Long fileNo = Long.parseLong(fileId);
//        LclQuotePiece lclQuotePiece = null;
//        LclSession lclSession = (LclSession) session.getAttribute("lclSession");
//        editbarrel = "false".equalsIgnoreCase(editbarrel) ? "N" : "Y";
//        if ("Y".equalsIgnoreCase(hazmat) || "N".equalsIgnoreCase(hazmat)) {
//            hazmat = "Y".equalsIgnoreCase(hazmat) ? "true" : "false";
//        }
//        if ("Y".equalsIgnoreCase(actHazmat) || "N".equalsIgnoreCase(actHazmat)) {
//            actHazmat = "Y".equalsIgnoreCase(actHazmat) ? "true" : "false";
//        }
//        if (bookVolumeImp.equalsIgnoreCase(bookedVolumeImperial) && bookWeigtImp.equalsIgnoreCase(bookedWeightImperial)
//                && commCode.equalsIgnoreCase(commNo) && bookPieceCount.equalsIgnoreCase(bookedPieceCount) && editbarrel.equalsIgnoreCase(barrel)
//                && actHazmat.equalsIgnoreCase(hazmat)) {
//            return true;
//        } else {
//            if (fileNo > 0) {
//                if (id != null && !id.trim().equals("")) {
//                    lclQuotePiece = new LclQuotePieceDAO().findById(Long.parseLong(id));
//                    if ((lclQuotePiece.getBookedPieceCount() == null || lclQuotePiece.getBookedPieceCount().intValue() == 0)
//                            && (bookedPieceCount == null || bookedPieceCount.trim().equals("") || bookedPieceCount.trim().equals("0"))
//                            || (!lclQuotePiece.getBookedPieceCount().toString().equalsIgnoreCase(bookedPieceCount))) {
//                        return false;
//                    }
//                    if ((lclQuotePiece.getBookedVolumeImperial() == null || lclQuotePiece.getBookedVolumeImperial().intValue() == 0)
//                            && (bookedVolumeImperial == null || !bookedVolumeImperial.trim().equals("") || !bookedVolumeImperial.trim().equals("0"))
//                            || (!lclQuotePiece.getBookedVolumeImperial().toString().equalsIgnoreCase(bookedVolumeImperial))) {
//                        return false;
//                    }
//                    if ((lclQuotePiece.getBookedWeightImperial() == null || lclQuotePiece.getBookedWeightImperial().intValue() == 0)
//                            && (!bookedWeightImperial.trim().equals("") || !bookedWeightImperial.trim().equals("0"))
//                            || !lclQuotePiece.getBookedWeightImperial().toString().equalsIgnoreCase(bookedWeightImperial)) {
//                        return false;
//                    }
//                    if (!lclQuotePiece.getCommodityType().getCode().equalsIgnoreCase(commNo)
//                            || (lclQuotePiece.isIsBarrel() != Boolean.parseBoolean(barrel))
//                            || Boolean.parseBoolean(actHazmat) != Boolean.parseBoolean(hazmat)) {
//                        return false;
//                    }
//                } else {
//                    return false;
//                }
//            } else {
//                if (lclSession != null && lclSession.getCommodityList() != null && lclSession.getCommodityList().size() > 0) {
//                    List<LclBookingPiece> lclQuotePiecesList = lclSession.getCommodityList();
//                    for (LclBookingPiece lclBkgPiece : lclQuotePiecesList) {
//                        if ((lclBkgPiece.getBookedPieceCount() == null || lclBkgPiece.getBookedPieceCount().intValue() == 0)
//                                && (bookedPieceCount == null || bookedPieceCount.trim().equals("") || bookedPieceCount.trim().equals("0"))
//                                || (!lclBkgPiece.getBookedPieceCount().toString().equalsIgnoreCase(bookedPieceCount))) {
//                            return false;
//                        }
//                        if ((lclBkgPiece.getBookedVolumeImperial() == null || lclBkgPiece.getBookedVolumeImperial().intValue() == 0)
//                                && (bookedVolumeImperial == null || !bookedVolumeImperial.trim().equals("") || !bookedVolumeImperial.trim().equals("0"))
//                                || (!lclBkgPiece.getBookedVolumeImperial().toString().equalsIgnoreCase(bookedVolumeImperial))) {
//                            return false;
//                        }
//                        if ((lclBkgPiece.getBookedWeightImperial() == null || lclBkgPiece.getBookedWeightImperial().intValue() == 0)
//                                && (!bookedWeightImperial.trim().equals("") || !bookedWeightImperial.trim().equals("0"))
//                                || (!lclBkgPiece.getBookedWeightImperial().toString().equalsIgnoreCase(bookedWeightImperial))) {
//                            return false;
//                        }
//                        if (!lclBkgPiece.getCommodityType().getCode().equalsIgnoreCase(commNo)
//                                || (lclBkgPiece.isIsBarrel() != Boolean.parseBoolean(barrel))
//                                || (Boolean.parseBoolean(actHazmat) != Boolean.parseBoolean(hazmat))) {
//                            return false;
//                        }
//                    }
//                }
//            }
//        }
//        return true;
//    }
    public boolean checkRates(String fileNumberId) throws Exception {//deleted
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            List<LclBookingAc> lclBookingAcList = new LclCostChargeDAO()
                    .getAllLclChargesByChargeCodeME(Long.parseLong(fileNumberId), "", false);
            if (CommonUtils.isNotEmpty(lclBookingAcList)) {
                return lclBookingAcList.size() > 0;
            }
        }
        return false;
    }
//update adjustment charges and check caf charges by bkg page lclExportBooking.js and methodName modifyAdjustmentAmount

    public boolean updateAdjusmentAmt(String chargeId, String adjustmentAmount,
            String adjusmentComment, String fileNumberId, HttpServletRequest request) throws Exception {
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        User user = (User) request.getSession().getAttribute("loginuser");
        if (chargeId != null && adjustmentAmount != null) {
            LclBookingAc lclBookingAc = lclCostChargeDAO.findById(Long.parseLong(chargeId));
            lclBookingAc.setAdjustmentAmount(new BigDecimal(adjustmentAmount));
            lclBookingAc.setAdjustmentComments((adjusmentComment.length() > 100 ? adjusmentComment.substring(0, 99) : adjusmentComment).toUpperCase());
            lclBookingAc.setModifiedDatetime(new Date());
            lclBookingAc.setModifiedBy(user);
            lclCostChargeDAO.saveOrUpdate(lclBookingAc);
        }
        String validate = lclCostChargeDAO.isChargeCodeValidate(fileNumberId, "CAF", "false");
        if (validate.equalsIgnoreCase("true")) {
            return true;
        }
        return false;
    }

//update adjustment charges and check caf charges by bkg page exportQuote.js and methodName calculateAdjustmentAmount
    public boolean checkCAFQuoteRates(String chargeId, String adjustmentAmount, String adjusmentComment,
            String fileNumberId, HttpServletRequest request) throws Exception {
        LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
        User user = (User) request.getSession().getAttribute("loginuser");
        if (chargeId != null && adjustmentAmount != null) {
            LclQuoteAc lclQuoteAc = lclQuoteAcDAO.findById(Long.parseLong(chargeId));
            lclQuoteAc.setAdjustmentAmount(new BigDecimal(adjustmentAmount));
            lclQuoteAc.setAdjustmentComments(((adjusmentComment.length() > 100 ? adjusmentComment.substring(0, 99) : adjusmentComment)).toUpperCase());
            lclQuoteAc.setModifiedDatetime(new Date());
            lclQuoteAc.setModifiedBy(user);
            lclQuoteAcDAO.saveOrUpdate(lclQuoteAc);
        }
        String validate = lclQuoteAcDAO.isChargeCodeValidate(fileNumberId, "CAF", "false");
        if (validate.equalsIgnoreCase("true")) {
            return true;
        }
        return false;
    }

    public boolean checkQuoteRates(String fileId) throws Exception {
        String chargeFlag = new LclQuoteAcDAO().isChargeCodeValidate(fileId, "", "false");
        if ("true".equalsIgnoreCase(chargeFlag)) {
            return true;
        }
        return false;
    }

    public boolean deleteColoadRates(String fileNumberId, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        LclSession lclSession = (LclSession) session.getAttribute("lclSession");
        if (lclSession != null && lclSession.getQuoteCommodityList() != null && lclSession.getQuoteCommodityList().size() > 0) {
            lclSession.setQuoteCommodityList(null);
        }
        if (lclSession != null && lclSession.getQuoteAcList() != null && lclSession.getQuoteAcList().size() > 0) {
            lclSession.setQuoteAcList(null);
            return true;
        }
        return false;
    }

    public boolean checkDispPortExists(String unitId, String unitSSId) throws Exception {
        if (CommonUtils.isNotEmpty(unitId) && CommonUtils.isNotEmpty(unitSSId)) {
            LclUnitSsDispoDAO lclUnitSsDispoDAO = new LclUnitSsDispoDAO();
            LclUnitSsDAO lclUnitSsDAO = new LclUnitSsDAO();
            Character status = lclUnitSsDAO.getStatusById(unitSSId);
            if (lclUnitSsDispoDAO.getUnitDispoCountByDispDesc(unitId, "PORT", null) > 0 && status.toString().equalsIgnoreCase("M")) {
                return true;
            }
        }
        return false;
    }

    public boolean checkBlRates(String fileId) throws Exception {
        return new LclBlAcDAO().isChargeCodeValidate(fileId, "", "");
    }

    public boolean checkcommodityinCarrierRates(String fileId, HttpServletRequest request) throws Exception {
        List lclBookingPiecesList = null;
        if (fileId != null && !fileId.trim().equals("") && !fileId.trim().equals("0")) {
            lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileId));

        } else {
            HttpSession session = request.getSession();
            LclSession lclSession = (LclSession) session.getAttribute("lclSession");
            if (lclSession != null && lclSession.getCommodityList() != null && lclSession.getCommodityList().size() > 0) {
                lclBookingPiecesList = lclSession.getCommodityList();
            }
        }
        if (lclBookingPiecesList != null && lclBookingPiecesList.size() > 0) {
            return true;
        }
        return false;
    }

    public boolean checkCommodityList(String fileId) throws Exception {
        List lclBookingPiecesList = null;
        if (fileId != null && !fileId.trim().equals("") && !fileId.trim().equals("0")) {
            lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileId));
        }
        if (CommonUtils.isEmpty(lclBookingPiecesList)) {
            return true;
        }
        return false;
    }

    public String checkHsCode(String fileId, String destination, String portOfDestination) throws Exception {//Release Btn Click for exports in BKG Screen
        PortsDAO portsdao = new PortsDAO();
        Ports fd = portsdao.getByProperty("unLocationCode", destination);
        Ports pod = portsdao.getByProperty("unLocationCode", portOfDestination);
        if ((fd != null && fd.getHscode() != null && fd.getHscode().equalsIgnoreCase("Y")) || (pod != null && pod.getHscode() != null && pod.getHscode().equalsIgnoreCase("Y"))) {
            String hsc = new LclBookingHsCodeDAO().isHsCodeAvailable(Long.parseLong(fileId));
            if (!"true".equalsIgnoreCase(hsc)) {
                return "HSCODE";
            }
        }
        if ((fd != null && fd.getNcmno() != null && fd.getNcmno().equalsIgnoreCase("Y")) || (pod != null && pod.getNcmno() != null && pod.getNcmno().equalsIgnoreCase("Y"))) {
            String ncm = new Lcl3pRefNoDAO().isChecked3PRefByType(fileId, "NCM", "");
            if (!"true".equalsIgnoreCase(ncm)) {
                return "NCMCODE";
            }
        }
        return null;
    }

    public boolean checkTransMode(String headerId, String detailId, String transMode) throws Exception {
        LclSsDetailDAO lclssdetaildao = new LclSsDetailDAO();
        int count = lclssdetaildao.getTransModeCountByHeader(headerId, detailId, transMode);
        if (count >= 1) {
            return true;
        }
        return false;
    }

    public boolean checkcommodityinCarrierRatesForBl(String fileId, HttpServletRequest request) throws Exception {
        List lclBlPiecesList = null;
        if (fileId != null && !fileId.trim().equals("") && !fileId.trim().equals("0")) {
            lclBlPiecesList = new LclBLPieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileId));

        } else {
            HttpSession session = request.getSession();
            LclSession lclSession = (LclSession) session.getAttribute("lclSession");
            if (lclSession != null && lclSession.getCommodityList() != null && lclSession.getCommodityList().size() > 0) {
                lclBlPiecesList = lclSession.getCommodityList();
            }
        }
        if (lclBlPiecesList != null && lclBlPiecesList.size() > 0) {
            return true;
        }
        return false;
    }

    public String outsourceUserEmailaddress(String userId) throws Exception {
        UserDAO userDAO = new UserDAO();
        return userDAO.getOutSourceEmail(userId);
    }

    public boolean checkPickupRate(String fileId, HttpServletRequest request) throws Exception {
        List<LclBookingAc> lclBookingAcList = new LclCostChargeDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileId));
        if (CommonUtils.isNotEmpty(lclBookingAcList)) {
            for (LclBookingAc lclBookingAc : lclBookingAcList) {
                if (lclBookingAc.getArglMapping().getChargeCode().toUpperCase().equals("INLAND")) {
                    return true;
                }
            }
        }
        User user = (User) request.getSession().getAttribute("loginuser");
        LclBooking lclBooking = new LCLBookingDAO().findById(Long.parseLong(fileId));
        lclBooking.setPooPickup(false);
        lclBooking.setModifiedBy(user);
        lclBooking.setModifiedDatetime(new Date());
        new LCLBookingDAO().saveOrUpdate(lclBooking);
        return false;
    }

    public boolean checkPickupRateForQuote(String fileNumberId) throws Exception {//Check PickUp and door Delivery rates
        LclQuotePadDAO lclQuotePadDAO = new LclQuotePadDAO();
        LclQuotePad lclQuotePad = lclQuotePadDAO.getLclQuotePadByFileNumber(Long.parseLong(fileNumberId));
        if (lclQuotePad != null && lclQuotePad.getLclQuoteAc() != null) {
            return true;
        }
        return false;
    }

    public void deleteQuotePad(String fileNumberId) throws Exception {//Check PickUp and door Delivery
        LclQuotePadDAO lclQuotePadDAO = new LclQuotePadDAO();
        lclQuotePadDAO.deleteQuotePad(fileNumberId);
    }

    public boolean deletingPickUpRate(String fileId) throws Exception {
        new LclCostChargeDAO().deleteRatesByChargeCode(Long.parseLong(fileId), "0012");
        return true;
    }

    public boolean deletingPickUpRateForQuote(String fileNumberId) throws Exception {
        List<LclQuoteAc> lclQuoteAcList = new LclQuoteAcDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
        if (CommonUtils.isNotEmpty(lclQuoteAcList)) {
            for (LclQuoteAc lclQuoteAc : lclQuoteAcList) {
                if (lclQuoteAc.getArglMapping().getChargeCode().toUpperCase().equals("PICKUP")) {
                    new LclQuoteAcDAO().delete(lclQuoteAc);
                }
            }
        }
        return true;
    }

//    public String[] defaultpolPOD(String pooID, String fdID) throws Exception {
//        String results[] = new String[6];
//        if (pooID != null && fdID != null && !pooID.trim().equals("") && !fdID.trim().equals("")) {
//            int pooid = Integer.parseInt(pooID);
//            int fdid = Integer.parseInt(fdID);
//            LclBookingPlanDAO lclBookingPlanDAO = new LclBookingPlanDAO();
//            LclBookingPlanBean lclBookingPlanBean = lclBookingPlanDAO.getRelay(pooid, fdid, "");
//            if (lclBookingPlanBean != null) {
//                if (lclBookingPlanBean.getPol_id() != null) {
//                    results[0] = lclBookingPlanBean.getPol_id().toString();
//                }
//                if (lclBookingPlanBean.getPol_name() != null) {
//                    results[1] = lclBookingPlanBean.getPol_name().toString();
//                }
//                if (lclBookingPlanBean.getPol_code() != null) {
//                    results[2] = lclBookingPlanBean.getPol_code().toString();
//                }
//                if (lclBookingPlanBean.getPod_id() != null) {
//                    results[3] = lclBookingPlanBean.getPod_id().toString();
//                }
//                if (lclBookingPlanBean.getPod_name() != null) {
//                    results[4] = lclBookingPlanBean.getPod_name().toString();
//                }
//                if (lclBookingPlanBean.getPod_code() != null) {
//                    results[5] = lclBookingPlanBean.getPod_code().toString();
//                }
//            }
//        }
//        return results;
//    }
//destination service for Exports
    public boolean checkVendorOptional(String chargeCode) throws Exception {
        GenericCode genericcode = new GenericCodeDAO().getGenericCodeByCode(chargeCode);
        if (genericcode != null) {
            if (genericcode.getField11() != null && genericcode.getField11().equals("Y")) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public String[] checkUnitNumberExists(String headerId, String unitNumber) throws Exception {
        String data[] = new String[16];
        if (unitNumber != null && !unitNumber.trim().equals("")) {
            LclUnitDAO lclunitdao = new LclUnitDAO();
            Integer count = 0;
            if (CommonUtils.isNotEmpty(headerId)) {
                count = lclunitdao.getUnitNumberCountByHeader(headerId, unitNumber);
            } else {
                LclUnit lclunit = lclunitdao.getUnit(unitNumber);
                data[3] = lclunit != null ? lclunit.getUnitNo() : "";
            }
            data[0] = count.toString();
            data[1] = "";
            data[4] = "";
            List<Object[]> scheduleList = lclunitdao.getScheduleNoByUnitNo(unitNumber);
            if (scheduleList != null && scheduleList.size() > 0) {
                for (Object[] scheduleObject : scheduleList) {
                    if (scheduleObject[0] != null && !scheduleObject[0].toString().trim().equals("")) {
                        data[1] += scheduleObject[0].toString() + ",";
                    }
                    if (scheduleObject[1] != null && !scheduleObject[1].toString().trim().equals("")) {
                        data[2] = scheduleObject[1].toString();
                    }
                }
                if (data[1] != null && data[1].contains(",")) {
                    data[1] = data[1].substring(0, data[1].length() - 1);
                }
            }
            if (!"".equals(data[2]) && data[2] != null) {
                LclUnit lclunit;
                lclunit = lclunitdao.getUnit(unitNumber);
                data[5] = lclunit.getComments();
                data[6] = lclunit.getRemarks();
                data[7] = lclunit.getHazmatPermitted() ? "true" : "false";
                data[8] = lclunit.getRefrigerated() ? "true" : "false";
                data[9] = lclunit.getUnitType().getId().toString();
                data[10] = lclunit.getTareWeightImperial() == null ? "" : lclunit.getTareWeightImperial().toString();
                data[11] = lclunit.getTareWeightMetric() == null ? "" : lclunit.getTareWeightMetric().toString();
                LclUnitWhse warehouse = CommonUtils.isNotEmpty(lclunit.getLclUnitWhseList())
                        ? lclunit.getLclUnitWhseList().get(lclunit.getLclUnitWhseList().size() - 1) : null;
                if (null != warehouse && null == warehouse.getLclSsHeader()) {
                    data[12] = warehouse.getChassisNoIn() == null ? "" : warehouse.getChassisNoIn();
                    data[13] = warehouse.getSealNoOut() == null ? "" : warehouse.getSealNoOut();
                    data[14] = warehouse.getStuffedByUser() == null ? "" : warehouse.getStuffedByUser().getFirstName() + " " + warehouse.getStuffedByUser().getLastName();
                    data[15] = warehouse.getStuffedByUser() == null ? "" : warehouse.getStuffedByUser().getUserId().toString();
                }
            }
        }
        String scheduleNo = new LclUnitDAO().checkUnitNoExist(unitNumber);
        if (CommonUtils.isNotEmpty(scheduleNo)) {
            data[4] = scheduleNo;
        }
        return data;
    }

    public String[] getUnitAndWarehouse(String unitNumber) throws Exception {
        String data[] = new String[2];
        if (unitNumber != null && !unitNumber.trim().equals("")) {
            LclUnitDAO lclunitdao = new LclUnitDAO();
            LclUnit lclunit = lclunitdao.getUnit(unitNumber);
            data[0] = lclunit != null ? lclunit.getId().toString() : "";
            if (null != lclunit && lclunit.getLclUnitWhseList().size() > 0) {
                LclUnitWhse warehouse = lclunit.getLclUnitWhseList().get(lclunit.getLclUnitWhseList().size() - 1);
                if (null == warehouse.getLclSsHeader()) {
                    data[1] = new LclUnitDAO().getWareHouseNumber(lclunit.getId().toString());
                }
            }

            //  List lclunit.getLclUnitWhseList();
        }
        return data;
    }

    public String[] isUnitNumberExists(String etaDate, String unitNumber) throws Exception {
        LclUnitDAO lclUnitDAO = new LclUnitDAO();
        String[] unitDetails = new String[4];
        String unitValidate = lclUnitDAO.isUnitNoValidateByUnitSs(unitNumber);
        if ("true".equalsIgnoreCase(unitValidate)) {
            boolean flag = true, flag1 = true;
            unitDetails[2] = "true";
            StringBuilder voyageNo = new StringBuilder();
            String etaFormat = DateUtils.parseDateToMYSQLFormat(etaDate);
            etaFormat += " 00:00:00";
            List<LclModel> unitDetailsList = lclUnitDAO.isUnitNumberExistsByVoy(unitNumber, etaFormat);
            if (unitDetailsList != null && !unitDetailsList.isEmpty()) {
                for (LclModel unitDetail : unitDetailsList) {
                    if (unitDetail.getDateDiff() < 0) {
                        unitDetails[0] = unitDetail.getUnitId();
                        unitDetails[3] = unitDetail.getUnitTypeId();
                        flag = false;
                    } else {
                        flag1 = false;
                    }
                    voyageNo.append(unitDetail.getScheduleNo()).append(",");
                }
                if (!flag && flag1) {
                    unitDetails[2] = "false";
                }
            }
            if (CommonUtils.isNotEmpty(voyageNo.toString())) {
                unitDetails[1] = StringUtils.removeEnd(voyageNo.toString(), ",");
            }
        } else {
            if (CommonUtils.isNotEmpty(unitValidate)) {
                unitDetails[1] = "UnitNo";
                unitDetails[0] = unitValidate;
            }
        }

        return unitDetails;
    }
    //lclCommodity.js

    public boolean checkBookedActualValues(String fileId, String actualPieceCount, String actualVolumeImperial, String actualWeightImperial,
            String actualWeightMetric, String actualVolumeMetric, String bookedPieceCount, String bookedVolumeImperial, String bookedVolumeMetric, String bookedWeightImperial, String bookedWeightMetric, HttpServletRequest request) throws Exception {
        User user = (User) request.getSession().getAttribute("loginuser");
        UserDAO userdao = new UserDAO();
        User userfromdb = userdao.findById(user.getUserId());
        if (userfromdb.getDifflclBookedDimsActual().equals("1")) {
            LclBooking lclBooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", Long.parseLong(fileId));
            StringBuilder message = new StringBuilder();
            String br = "<br>";
            message.append("<HTML><BODY>");
            message.append(br);
            message.append("<table border=1><thead><tr><th></th><th>Actual</th><th>Booked</th></tr></thead>");
            if (actualWeightImperial != null && !actualWeightImperial.equals("") && bookedWeightImperial != null && !bookedWeightImperial.equals("")) {
                if (!actualWeightImperial.equals(bookedWeightImperial)) {
                    message.append("<tr><td>Weight Imperial</td><td>");
                    message.append(actualWeightImperial);
                    message.append("</td><td>");
                    message.append(bookedWeightImperial);
                    message.append("</td></tr>");

                }
            }
            if (actualVolumeImperial != null && !actualVolumeImperial.equals("") && bookedVolumeImperial != null && !bookedVolumeImperial.equals("")) {
                if (!actualVolumeImperial.equals(bookedVolumeImperial)) {
                    message.append("<tr><td>Volume Imperial</td><td>");
                    message.append(actualVolumeImperial);
                    message.append("</td><td>");
                    message.append(bookedVolumeImperial);
                    message.append("</td></tr>");
//                    message += "ActualVolumeImperial is " + actualVolumeImperial + " and BookedVolumeImperial is " + bookedVolumeImperial + "\n";
                }
            }
            if (actualWeightMetric != null && !actualWeightMetric.equals("") && bookedWeightMetric != null && !bookedWeightMetric.equals("")) {
                if (!actualWeightMetric.equals(bookedWeightMetric)) {
                    message.append("<tr><td>Weight Metric</td><td>");
                    message.append(actualWeightMetric);
                    message.append("</td><td>");
                    message.append(bookedWeightMetric);
                    message.append("</td></tr>");
                }
            }
            if (actualVolumeMetric != null && !actualVolumeMetric.equals("") && bookedVolumeMetric != null && !bookedVolumeMetric.equals("")) {
                if (!actualVolumeMetric.equals(bookedVolumeMetric)) {
                    message.append("<tr><td>Volume Metric</td><td>");
                    message.append(actualVolumeMetric);
                    message.append("</td><td>");
                    message.append(bookedVolumeMetric);
                    message.append("</td></tr>");
                }
            }
            if (bookedPieceCount != null && !bookedPieceCount.equals("") && actualPieceCount != null && !actualPieceCount.equals("")) {
                if (!bookedPieceCount.equals(actualPieceCount)) {
                    message.append("<tr><td>Piece Count</td><td>");
                    message.append(actualPieceCount);
                    message.append("</td><td>");
                    message.append(bookedPieceCount);
                    message.append("</td></tr>");
                }
            }
            message.append("</table></BODY></HTML>");
            if (message != null && !message.toString().trim().equals("")) {
                EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
                String fromName = lclBooking.getEnteredBy().getFirstName();
                emailSchedulerVO.setFileLocation("");
                emailSchedulerVO.setName("BookedActualValues");
                emailSchedulerVO.setSubject("Commodity Details For " + lclBooking.getLclFileNumber().getFileNumber());
                emailSchedulerVO.setType("Email");
                emailSchedulerVO.setStatus("Pending");
                emailSchedulerVO.setToAddress(lclBooking.getEnteredBy().getEmail());
                emailSchedulerVO.setFromName(fromName);
                emailSchedulerVO.setFromAddress(lclBooking.getEnteredBy().getEmail());
                emailSchedulerVO.setEmailDate(new Date());
                emailSchedulerVO.setTextMessage(message.toString());
                emailSchedulerVO.setModuleName("BookedActualValues");
                emailSchedulerVO.setUserName(lclBooking.getEnteredBy().getLoginName());
                emailSchedulerVO.setModuleId(lclBooking.getLclFileNumber().getFileNumber());
                emailSchedulerVO.setHtmlMessage(message.toString());
                new EmailschedulerDAO().save(emailSchedulerVO);
            }
        }
        return false;
    }

    public String getconsolidatedfiles(String fileIdA) throws Exception {
        String fileNumber = new String();
        if (CommonUtils.isNotEmpty(fileIdA)) {
            List<LclConsolidate> consDescList = new LclConsolidateDAO().getLclCostByChargeCode(Long.parseLong(fileIdA), "R");
            if (CommonUtils.isNotEmpty(consDescList)) {
                for (LclConsolidate lc : consDescList) {
                    fileNumber = fileNumber + lc.getLclFileNumberB().getFileNumber() + "<br>";
                }
            }
        }
        return fileNumber;
    }

    public String getStatus(String fileNumber) throws Exception {
        String status = "";
        if (CommonUtils.isNotEmpty(fileNumber)) {
            LclFileNumber lclFileNumber = new LclFileNumberDAO().getByProperty("fileNumber", fileNumber);
            if (lclFileNumber != null) {
                status = lclFileNumber.getState();
            }
        }
        return status;
    }

    public boolean setCarrier(String index, String pickupCharge, String scac, String pickupCost, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        LclSession lclSession = (LclSession) session.getAttribute("lclSession");
        List<Carrier> carrierCostList = lclSession.getCarrierCostList();
        String pickUpCost = "";
        if (carrierCostList != null && !carrierCostList.isEmpty()) {
            for (int j = 0; j < carrierCostList.size(); j++) {
                Carrier carrier = carrierCostList.get(j);
                if (carrier != null && carrier.getScac() != null && carrier.getScac().trim().equalsIgnoreCase(scac)
                        && carrier.getFinalcharge() != null && !carrier.getFinalcharge().trim().equals("")) {
                    pickUpCost = carrier.getFinalcharge();
                    break;
                }
            }
        }
        if (index != null && !index.trim().equals("")) {
            List<RoutingOptionsBean> routingOptionsList = lclSession.getRoutingOptionsList();
            if (routingOptionsList != null && routingOptionsList.size() > 0) {
                RoutingOptionsBean routingOptionsBean = routingOptionsList.get(Integer.parseInt(index));
                routingOptionsBean.setCtsAmount("$" + pickupCharge);
                routingOptionsBean.setPickupCost(new BigDecimal(pickUpCost));
                routingOptionsBean.setScac("(" + scac + ")");
                routingOptionsList.set(Integer.parseInt(index), routingOptionsBean);
            }
            lclSession.setRoutingOptionsList(routingOptionsList);
        }
        session.setAttribute("lclSession", lclSession);
        return true;
    }

    public boolean verifyCargoReceived(String fileId, String bookedVolumeImperial, String bookedWeightImperial, String actualVolumeImperial, String actualWeightImperial, HttpServletRequest request) throws Exception {
        if (fileId != null && !fileId.equals("") && !fileId.equals("0")) {
            LclFileNumber lclFileNumber = new LclFileNumberDAO().findById(Long.parseLong(fileId));
            if ((lclFileNumber != null && lclFileNumber.getStatus() != null) && (lclFileNumber.getStatus().equalsIgnoreCase("WV") || lclFileNumber.getStatus().equalsIgnoreCase("R"))) {
                if ((bookedVolumeImperial != null && bookedWeightImperial != null) && (actualVolumeImperial != null && actualWeightImperial != null)) {
                    StringBuilder differentValues = new StringBuilder();
                    if (!(bookedVolumeImperial).equals(actualVolumeImperial)) {
                        differentValues.append("Cube ").append("and");
                    }
                    if (!(bookedWeightImperial).equals(actualWeightImperial)) {
                        differentValues.append(" Weight").append("and");
                    }
                    if (differentValues.toString() != null && !"".equals(differentValues.toString())) {
                        LclRemarks lclremarks = new LclRemarks();
                        lclremarks.setLclFileNumber(lclFileNumber);
                        lclremarks.setType(REMARKS_DR_AUTO_NOTES);
                        lclremarks.setRemarks("Charges will be Deleted Because Actual " + StringUtils.substringBeforeLast(differentValues.toString(), "and") + " are Different than Booked");
                        User user = (User) request.getSession().getAttribute("loginuser");
                        lclremarks.setEnteredBy(user);
                        lclremarks.setEnteredDatetime(new Date());
                        lclremarks.setModifiedBy(user);
                        lclremarks.setModifiedDatetime(new Date());
                        new LclRemarksDAO().save(lclremarks);
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean isConsolidted(String fileId) throws Exception {
        return new LclConsolidateDAO().isConsolidated(Long.parseLong(fileId));
    }

    public String verifyArinvoice(String fileId) throws Exception {
        String status = "";
        if (fileId != null && !fileId.equals("")) {
            List<ArRedInvoice> arRedInvoice = new ArRedInvoiceDAO().findByProperty("fileNo", fileId);
            if (CommonUtils.isNotEmpty(arRedInvoice)) {
                for (ArRedInvoice invoice : arRedInvoice) {
                    if (null != invoice.getStatus() && !"AR".equalsIgnoreCase(invoice.getStatus())) {
                        status = "unPosted";
                    } else {
                        status = !"unPosted".equalsIgnoreCase(status) ? "posted" : "unPosted";
                    }
                }
            }
        }
        return status;
    }

    public final String sendEDI(final String ssMasterId, HttpServletRequest request) throws Exception {
        String errorMsg = "";
        try {
            if (CommonUtils.isNotEmpty(ssMasterId)) {
                LclSSMasterBl ssMasterBl = new LclSSMasterBlDAO().findById(Long.parseLong(ssMasterId));
                LclSsDetail ssDetail = ssMasterBl.getLclSsHeader().getVesselSsDetail();
                if (null != ssDetail) {
                    HttpSession session = request.getSession();
                    User user = (User) session.getAttribute("loginuser");
                    if ("I".equals(ssDetail.getSpAcctNo().getGeneralInfo().getShippingCode())) {
                        errorMsg = new InttraTemplate().createXML(ssMasterBl, ssDetail, user);
                    } else if ("G".equals(ssDetail.getSpAcctNo().getGeneralInfo().getShippingCode())) {
                        errorMsg = new GtnexusTemplate().createXML(ssMasterBl, ssDetail, user);
                    }
                }
            }
        } catch (Exception ex) {
            errorMsg = "Error Occured";
            ex.printStackTrace();
        }
        return errorMsg;
    }

    public String createFlatFile(String trnref, String fileId, String doorOrigin, HttpServletRequest request) throws Exception {
        SedFilings sedFilings = new SedFilingsDAO().findByTrnref(trnref);
        Long fileNoId = Long.parseLong(fileId);
        LclBooking lclBooking = new LCLBookingDAO().findById(fileNoId);
        String fileLocation = new SedFilingBC().createFlatFileForLcl(sedFilings, doorOrigin, lclBooking);
        List<String> contents = FileUtils.readLines(new File(fileLocation));
        if (CommonUtils.isNotEmpty(contents)) {
            if ("N".equals(sedFilings.getStatus()) || "C".equals(sedFilings.getStatus()) || "E".equals(sedFilings.getStatus())) {
                sedFilings.setStatus("S");
            }
            new SedFilingsDAO().saveOrUpdate(sedFilings);
            HttpSession session = request.getSession();
            User loginUser = (User) session.getAttribute("loginuser");
            new LclRemarksDAO().insertLclRemarks(fileNoId, "AES", "AES is submitted for Transaction REF# : " + trnref, loginUser.getUserId());
            return "Success";
        }
        return "";
    }

    public boolean checkSchedBAvailability(String trnref) throws Exception {
        List schedList = new SedSchedulebDetailsDAO().findByTrnref(trnref);
        if (schedList.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean validDate(String date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        sdf.setLenient(false);
        try {
            Date date1 = sdf.parse(date);
        } catch (Exception pe) {
            return false;
        }
        return true;

    }

    public String displayDimsDetails(String id, String fileNumber, String commodityNo, HttpServletRequest request) throws Exception {
        StringBuilder sb = new StringBuilder();
        HttpSession session = request.getSession();
        LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        List detailList = null;
        if (id != null && !id.equals("")
                && fileNumber != null && !fileNumber.equals("") && !fileNumber.equals("0")) {
            detailList = new CommodityDetailsDAO().findByProperty("lclBookingPiece.id", Long.parseLong(id));
        } else if (CommonUtils.isNotEmpty(lclSession.getCommodityList())) {
            for (int i = 0; i < lclSession.getCommodityList().size(); i++) {
                LclBookingPiece lclBookingPiece = lclSession.getCommodityList().get(i);
                if (lclBookingPiece.getCommodityType().getCode().equals(commodityNo)) {
                    detailList = lclBookingPiece.getLclBookingPieceDetailList();
                    break;
                }
            }
        } else if (CommonUtils.isNotEmpty(lclSession.getBookingDetailList())) {
            detailList = lclSession.getBookingDetailList();
        }
        showDimsDetails(sb, detailList);
        return sb.toString();
    }

    public String[] displayQuoteDimsDetails(String id, String fileNumber, String commodityNo, HttpServletRequest request) throws Exception {
        String[] result = new String[2];
        StringBuilder sb = new StringBuilder();
        List detailList = new LinkedList();
        if (id != null && !id.equals("") && fileNumber != null && !fileNumber.equals("") && !fileNumber.equals("0")) {
            detailList = new QuoteCommodityDetailsDAO().findByProperty("quotePiece.id", Long.parseLong(id));
        } else {
            HttpSession session = request.getSession();
            LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
            detailList = null != lclSession.getQuoteDetailList() ? lclSession.getQuoteDetailList() : new LinkedList();
            if (lclSession != null && lclSession.getQuoteCommodityList() != null) {
                for (int i = 0; i < lclSession.getQuoteCommodityList().size(); i++) {
                    LclQuotePiece lclQuotePiece = lclSession.getQuoteCommodityList().get(i);
                    if (lclQuotePiece.getCommNo().equals(commodityNo)) {
                        detailList = lclQuotePiece.getLclQuotePieceDetailList();
                        break;
                    }
                }
            }
        }
        showQuoteDimsDetails(sb, detailList);
        result[0] = sb.toString();
        result[1] = CommonUtils.isNotEmpty(detailList) ? "true" : "false";
        return result;
    }

    private StringBuilder showDimsDetails(StringBuilder sb, List detailList) {
        sb.append("<HTML><BODY>");
        sb.append("<table width=\"100%\" style=\"border:2px solid black;\">");
        sb.append("<tr bgcolor='#FFFF00'>");
        sb.append("<td colspan='10' style=\"font-weight:bold;font-family:Calibri;font-size: 11px;text-align: center; color=#008000\">");
        sb.append("DIMS DETAILS");
        sb.append("</td>");
        sb.append("</tr>");
        if (CommonUtils.isNotEmpty(detailList)) {
            LclBookingPieceDetail detail = (LclBookingPieceDetail) detailList.get(0);
            sb.append("<tr  bgcolor='#9BC2E6' style=\"border:2px solid black;font-weight:bold;font-family:Calibri;font-size: 11px;text-align: center;\"><td  colspan='2' style=\"border-right:2px solid black;\">LENGTH</td><td  colspan='2' style=\"border-right:2px solid black;\">WIDTH</td><td  colspan='2' style=\"border-right:2px solid black;\">HEIGHT</td><td  colspan='2' style=\"border-right:2px solid black;\">WEIGHT PER PIECE</td><td style=\"border-right:2px solid black;\">PIECES</td><td>WAREHOUSE</td></tr>");
            sb.append("<tr style=\"border:2px solid black;font-weight:bold;font-family:Calibri;font-size: 11px;text-align: center;\"><b><td bgcolor='#99F27A' style=\"border-right:1px solid #dcdcdc;\">(IN)</td><td  bgcolor='#FFE699' style=\"border-right:2px solid black;\">(CM)</td><td bgcolor='#99F27A'  style=\"border-right:2px solid #dcdcdc;\">(IN)</td><td  bgcolor='#FFE699' style=\"border-right:2px solid black;\">(CM)</td><td bgcolor='#99F27A'>(IN)</td><td  bgcolor='#FFE699' style=\"border-right:2px solid black;\">(CM)</td><td bgcolor='#99F27A'  style=\"border-right:2px solid #dcdcdc;\">(KGS)</td><td  bgcolor='#FFE699' style=\"border-right:2px solid black;\">(LBS)</td><td  bgcolor='#99F27A' style=\"border-right:2px solid black;\"></td><td  bgcolor='#FFE699' style=\"border-right:2px solid black;\"></td></b></tr>");
            for (int i = 0; i < detailList.size(); i++) {
                detail = (LclBookingPieceDetail) detailList.get(i);
                if ("M".equalsIgnoreCase(detail.getActualUom())) {
                    sb.append("<tr style=\"font-family:Calibri;font-size: 11px;text-align: center;border-bottom:1px solid #dcdcdc;\"><td bgcolor='#99F27A' style=\"border-right:1px solid #dcdcdc;\">");
                    cbmcftConversion(sb, detail.getActualUom(), detail.getActualLength().toString());
                    sb.append("</td><td bgcolor='#FFE699' style=\"border-right:2px solid black;\">");
                    sb.append(detail.getActualLength());
                    sb.append("</td><td bgcolor='#99F27A' style=\"border-right:1px solid #dcdcdc;\">");
                    cbmcftConversion(sb, detail.getActualUom(), detail.getActualWidth().toString());
                    sb.append("</td><td bgcolor='#FFE699' style=\"border-right:2px solid black;\">");
                    sb.append(detail.getActualWidth());
                    sb.append("</td><td bgcolor='#99F27A' style=\"border-right:1px solid #dcdcdc;\">");
                    cbmcftConversion(sb, detail.getActualUom(), detail.getActualHeight().toString());
                    sb.append("</td><td bgcolor='#FFE699' style=\"border-right:2px solid black;\">");
                    sb.append(detail.getActualHeight());
                    sb.append("</td><td bgcolor='#99F27A' style=\"border-right:1px solid #dcdcdc;\">");
                } else {
                    sb.append("<tr style=\"font-family:Calibri;font-size: 11px;text-align: center;border-bottom:1px solid #dcdcdc;\"><td bgcolor='#99F27A' style=\"border-right:1px solid #dcdcdc;\">");
                    sb.append(detail.getActualLength());
                    sb.append("</td><td bgcolor='#FFE699' style=\"border-right:2px solid black;\">");
                    cbmcftConversion(sb, detail.getActualUom(), detail.getActualLength().toString());
                    sb.append("</td><td bgcolor='#99F27A' style=\"border-right:1px solid #dcdcdc;\">");
                    sb.append(detail.getActualWidth());
                    sb.append("</td><td bgcolor='#FFE699' style=\"border-right:2px solid black;\">");
                    cbmcftConversion(sb, detail.getActualUom(), detail.getActualWidth().toString());
                    sb.append("</td><td bgcolor='#99F27A' style=\"border-right:1px solid #dcdcdc;\">");
                    sb.append(detail.getActualHeight());
                    sb.append("</td><td bgcolor='#FFE699' style=\"border-right:2px solid black;\">");
                    cbmcftConversion(sb, detail.getActualUom(), detail.getActualHeight().toString());
                    sb.append("</td><td bgcolor='#99F27A' style=\"border-right:1px solid #dcdcdc;\">");
                }
                if (detail.getActualWeight() != null && !detail.getActualWeight().equals("")) {
                    sb.append(detail.getActualWeight());
                } else {
                    sb.append("");
                }
                sb.append("</td><td bgcolor='#FFE699' style=\"border-right:2px solid black;\">");
                if (detail.getActualWeight() != null && !detail.getActualWeight().equals("")) {
                    cbmcftConversion(sb, detail.getActualUom(), detail.getActualWeight().toString());
                } else {
                    sb.append("");
                }
                sb.append("</td><td bgcolor='#99F27A' style=\"border-right:2px solid black;\">");
                sb.append(detail.getPieceCount());
                sb.append("</td><td bgcolor='#FFE699' style=\"border-right:2px solid black;\">");
                if (detail.getStowedLocation() != null && !detail.getStowedLocation().equals("")) {
                    sb.append(detail.getStowedLocation());
                } else {
                    sb.append("");
                }
                sb.append("</td></tr>");
            }
        }
        //sb.append("</table>");
        sb.append("</table>");
        sb.append("</BODY></HTML>");
        return sb;
    }

    private StringBuilder cbmcftConversion(StringBuilder sb, String label, String value) {
        Double result = 0.0d;
        Double val = Double.parseDouble(value);
        if (label.equals("I")) {
            result = val * 2.54;
        }
        if (label.equals("M")) {
            result = val * 0.3937;
        }
        sb.append(String.format("%.2f", result));
        return sb;
    }

    private StringBuilder showQuoteDimsDetails(StringBuilder sb, List detailList) {
        sb.append("<HTML><BODY>");
        sb.append("<table width=\"100%\" style=\"border:2px solid black;\">");
        sb.append("<tr bgcolor='#FFFF00'>");
        sb.append("<td colspan='10' style=\"font-weight:bold;font-family:Calibri;font-size: 11px;text-align: center; color=#008000\">");
        sb.append("DIMS DETAILS");
        sb.append("</td>");
        sb.append("</tr>");
        if (CommonUtils.isNotEmpty(detailList)) {
            LclQuotePieceDetail detail = (LclQuotePieceDetail) detailList.get(0);
            sb.append("<tr  bgcolor='#9BC2E6' style=\"border:2px solid black;font-weight:bold;font-family:Calibri;font-size: 11px;text-align: center;\"><td  colspan='2' style=\"border-right:2px solid black;\">LENGTH</td><td  colspan='2' style=\"border-right:2px solid black;\">WIDTH</td><td  colspan='2' style=\"border-right:2px solid black;\">HEIGHT</td><td  colspan='2' style=\"border-right:2px solid black;\">WEIGHT PER PIECE</td><td style=\"border-right:2px solid black;\">PIECES</td><td>WAREHOUSE</td></tr>");
            sb.append("<tr style=\"border:2px solid black;font-weight:bold;font-family:Calibri;font-size: 11px;text-align: center;\"><b><td bgcolor='#99F27A' style=\"border-right:1px solid #dcdcdc;\">(IN)</td><td  bgcolor='#FFE699' style=\"border-right:2px solid black;\">(CM)</td><td bgcolor='#99F27A'  style=\"border-right:2px solid #dcdcdc;\">(IN)</td><td  bgcolor='#FFE699' style=\"border-right:2px solid black;\">(CM)</td><td bgcolor='#99F27A'>(IN)</td><td  bgcolor='#FFE699' style=\"border-right:2px solid black;\">(CM)</td><td bgcolor='#99F27A'  style=\"border-right:2px solid #dcdcdc;\">(KGS)</td><td  bgcolor='#FFE699' style=\"border-right:2px solid black;\">(LBS)</td><td  bgcolor='#99F27A' style=\"border-right:2px solid black;\"></td><td  bgcolor='#FFE699' style=\"border-right:2px solid black;\"></td></b></tr>");
            for (int i = 0; i < detailList.size(); i++) {
                detail = (LclQuotePieceDetail) detailList.get(i);
                if ("M".equalsIgnoreCase(detail.getActualUom())) {
                    sb.append("<tr style=\"font-family:Calibri;font-size: 11px;text-align: center;border-bottom:1px solid #dcdcdc;\"><td bgcolor='#99F27A' style=\"border-right:1px solid #dcdcdc;\">");
                    cbmcftConversion(sb, detail.getActualUom(), detail.getActualLength().toString());
                    sb.append("</td><td bgcolor='#FFE699' style=\"border-right:2px solid black;\">");
                    sb.append(detail.getActualLength());
                    sb.append("</td><td bgcolor='#99F27A' style=\"border-right:1px solid #dcdcdc;\">");
                    cbmcftConversion(sb, detail.getActualUom(), detail.getActualWidth().toString());
                    sb.append("</td><td bgcolor='#FFE699' style=\"border-right:2px solid black;\">");
                    sb.append(detail.getActualWidth());
                    sb.append("</td><td bgcolor='#99F27A' style=\"border-right:1px solid #dcdcdc;\">");
                    cbmcftConversion(sb, detail.getActualUom(), detail.getActualHeight().toString());
                    sb.append("</td><td bgcolor='#FFE699' style=\"border-right:2px solid black;\">");
                    sb.append(detail.getActualHeight());
                } else {
                    sb.append("<tr style=\"font-family:Calibri;font-size: 11px;text-align: center;border-bottom:1px solid #dcdcdc;\"><td bgcolor='#99F27A' style=\"border-right:1px solid #dcdcdc;\">");
                    sb.append(detail.getActualLength());
                    sb.append("</td><td bgcolor='#FFE699' style=\"border-right:2px solid black;\">");
                    cbmcftConversion(sb, detail.getActualUom(), detail.getActualLength().toString());
                    sb.append("</td><td bgcolor='#99F27A' style=\"border-right:1px solid #dcdcdc;\">");
                    sb.append(detail.getActualWidth());
                    sb.append("</td><td bgcolor='#FFE699' style=\"border-right:2px solid black;\">");
                    cbmcftConversion(sb, detail.getActualUom(), detail.getActualWidth().toString());
                    sb.append("</td><td bgcolor='#99F27A' style=\"border-right:1px solid #dcdcdc;\">");
                    sb.append(detail.getActualHeight());
                    sb.append("</td><td bgcolor='#FFE699' style=\"border-right:2px solid black;\">");
                    cbmcftConversion(sb, detail.getActualUom(), detail.getActualHeight().toString());
                }
                sb.append("</td><td bgcolor='#99F27A' style=\"border-right:1px solid #dcdcdc;\">");
                if (detail.getActualWeight() != null && !detail.getActualWeight().equals("")) {
                    sb.append(detail.getActualWeight());
                } else {
                    sb.append("");
                }
                sb.append("</td><td bgcolor='#FFE699' style=\"border-right:2px solid black;\">");
                if (detail.getActualWeight() != null && !detail.getActualWeight().equals("")) {
                    cbmcftConversion(sb, detail.getActualUom(), detail.getActualWeight().toString());
                } else {
                    sb.append("");
                }
                sb.append("</td><td bgcolor='#99F27A' style=\"border-right:2px solid black;\">");
                sb.append(detail.getPieceCount());
                sb.append("</td><td bgcolor='#FFE699' style=\"border-right:2px solid black;\">");
                if (detail.getStowedLocation() != null && !detail.getStowedLocation().equals("")) {
                    sb.append(detail.getStowedLocation());
                } else {
                    sb.append("");
                }
                sb.append("</td></tr>");
            }
        }
        //sb.append("</table>");
        sb.append("</table>");
        sb.append("</BODY></HTML>");
        return sb;
    }

    public void checkPickupCharges(String fileId, String chargeAmount) throws Exception {
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        LclBookingPadDAO lclBookingPadDAO = new LclBookingPadDAO();
        LclBookingAc lclBookingAc = lclCostChargeDAO.manaualChargeValidate(Long.parseLong(fileId), "PICKUP", false);
        if (lclBookingAc == null) {
            List<LclBookingAc> lclBookingAcList = lclCostChargeDAO.getAllLclCostByBluescreenChargeCodeME(Long.parseLong(fileId), "0012", true);
            LclBookingPad lclBookingPad = lclBookingPadDAO.getLclBookingPadByFileNumber(Long.parseLong(fileId));
            for (LclBookingAc lbac : lclBookingAcList) {
                if (lbac.getArAmount() != null && lclBookingPad.getLclBookingAc().getArAmount() != null && lbac.getArAmount().equals(lclBookingPad.getLclBookingAc().getArAmount())) {
                    lclBookingAc = lbac;
                    break;
                }
            }
        }
        LclBookingPad lclBookingPad = lclBookingPadDAO.getLclBookingPadByFileNumber(Long.parseLong(fileId));
        if (lclBookingPad == null) {
            lclBookingPad = new LclBookingPad();
        } else {
            if (CommonUtils.isNotEmpty(chargeAmount) && chargeAmount != null) {
                BigDecimal d = new BigDecimal(chargeAmount);
                lclBookingPad.getLclBookingAc().setArAmount(d);
                lclBookingPadDAO.saveOrUpdate(lclBookingPad);
                if (lclBookingAc != null) {
                    lclBookingAc.setRatePerUnit(lclBookingPad.getLclBookingAc().getArAmount());
                    lclBookingAc.setArAmount(d);
                    if (lclBookingPad.getLclBookingAc().getApAmount() != null) {
                        lclBookingAc.setApAmount(lclBookingPad.getLclBookingAc().getApAmount());
                    }
                    lclCostChargeDAO.saveOrUpdate(lclBookingAc);
                }
            }
        }
    }

    public String editurnyellow(String fileno) {
        String result = null;
        EdiDAO edidao = new EdiDAO();
        LogFileEdi edifile = edidao.getfilestatus(fileno);
        if (null != edifile && !edifile.equals("")) {
            String type = edifile.getMessageType();
            String status = edifile.getStatus();
            if (null != type && null != status) {
                if (type.equals("304") && status.equals("success")) {
                    result = "success";
                } else if (type.equals("997") && status.equals("success")) {
                    result = "fail";
                }
            }
        }
        return result;
    }

    public void checkQuotePickupCharges(String fileId, String chargeAmount) throws Exception {
        Long fileID = new Long(fileId);
        LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
        LclQuotePadDAO lclQuotePadDAO = new LclQuotePadDAO();
        LclQuoteAc lclQuoteAc = lclQuoteAcDAO.manaualChargeValidate(fileID, "PICKUP", false);
//        if (lclQuoteAc == null) {
//            List<LclQuoteAc> lclQuoteAcList = lclQuoteAcDAO.getAllLclCostByBluescreenChargeCodeME(fileID, "0012", true);
//            LclQuotePad lclQuotePad = lclQuotePadDAO.getLclQuotePadByFileNumber(fileID);
//            for (LclQuoteAc lbac : lclQuoteAcList) {
//                if (lbac.getArAmount() != null && lclQuotePad.getLclQuoteAc().getArAmount() != null && lbac.getArAmount().equals(lclQuotePad.getLclQuoteAc().getArAmount())) {
//                    lclQuoteAc = lbac;
//                    break;
//                }
//            }
//        }
//        LclQuotePad lclQuotePad = lclQuotePadDAO.getLclQuotePadByFileNumber(fileID);
//        if (lclQuotePad == null) {
//            lclQuotePad = new LclQuotePad();
//        } else {
//            if (CommonUtils.isNotEmpty(chargeAmount) && chargeAmount != null) {
//                BigDecimal d = new BigDecimal(chargeAmount);
//                lclQuotePad.getLclQuoteAc().setArAmount(d);
//                lclQuotePadDAO.saveOrUpdate(lclQuotePad);
//                if (lclQuoteAc != null) {
//                    lclQuoteAc.setRatePerUnit(lclQuotePad.getLclQuoteAc().getArAmount());
//                    lclQuoteAc.setArAmount(d);
//                    if (lclQuotePad.getLclQuoteAc().getApAmount() != null) {
//                        lclQuoteAc.setApAmount(lclQuotePad.getLclQuoteAc().getApAmount());
//                    }
//                    lclQuoteAcDAO.saveOrUpdate(lclQuoteAc);
//                }
//            }
//        }
    }

    public boolean getPortsHsCode(String unLocationCode, String fileId) throws Exception {//for Bkg screen in HSCode
        String ports = new PortsDAO().getPortValue(PORTS_HSCODE, unLocationCode);
        if (!"".equalsIgnoreCase(ports) && "D".equalsIgnoreCase(ports)) {
            String bkgFlag = new LclBookingHsCodeDAO().isCheckedBkgHsCode(Long.parseLong(fileId));
            if ("true".equalsIgnoreCase(bkgFlag)) {
                return true;
            }
        }
        return false;
    }

    public String checkEmptyForPortsHsCode(String unLocationCode, String fileId) throws Exception {//for Bkg Screen Imp & Exp in HSCode
        String bookingHsCode = "";
        String ports = new PortsDAO().getPortValue(PORTS_HSCODE, unLocationCode);
        if (!"".equalsIgnoreCase(ports) && "D".equalsIgnoreCase(ports)) {
            List<LclBookingHsCode> lclHsCodeList = new LclBookingHsCodeDAO().getHsCodeByList(Long.parseLong(fileId));
            if (CommonUtils.isNotEmpty(lclHsCodeList)) {
                for (LclBookingHsCode lclBookingHsCode : lclHsCodeList) {
                    bookingHsCode += lclBookingHsCode.getCodes() + "/" + lclBookingHsCode.getId() + "\n";
                    break;
                }
            }
        }
        return bookingHsCode;
    }

    public boolean getPortsHsCodeForQuote(String unLocationCode, String fileId) throws Exception {
        String ports = new PortsDAO().getPortValue(PORTS_HSCODE, unLocationCode);
        if (!"".equalsIgnoreCase(ports) && "D".equalsIgnoreCase(ports)) {
            String hsCodeFlag = new LclQuoteHsCodeDAO().isCheckedQteHsCode(Long.parseLong(fileId));
            if ("true".equalsIgnoreCase(hsCodeFlag)) {
                return true;
            }
        }
        return false;
    }

    public String checkEmptyForQuotePortsHsCode(String unLocationCode, String fileId) throws Exception {
        String quoteHsCode = "";
        String ports = new PortsDAO().getPortValue(PORTS_HSCODE, unLocationCode);
        if (!"".equalsIgnoreCase(ports) && "D".equalsIgnoreCase(ports)) {
            List<LclQuoteHsCode> lclHsCodeList = new LclQuoteHsCodeDAO().getHsCodeByList(Long.parseLong(fileId));
            if (CommonUtils.isNotEmpty(lclHsCodeList)) {
                for (LclQuoteHsCode lclQuoteHsCode : lclHsCodeList) {
                    quoteHsCode += lclQuoteHsCode.getCodes() + "/" + lclQuoteHsCode.getId() + "\n";
                }
            }
        }
        return quoteHsCode;
    }

    public boolean checkWhseLocationForQuote(String location, String bookingPieceId) throws Exception {
        if (CommonUtils.isNotEmpty(location) && CommonUtils.isNotEmpty(bookingPieceId)) {
            List<LclQuotePieceWhse> lclQuotePieceWhseList = new LclQuotePieceWhseDAO().findByProperty("lclQuotePiece.id", Long.parseLong(bookingPieceId));
            if (CommonUtils.isNotEmpty(lclQuotePieceWhseList)) {
                for (LclQuotePieceWhse lclPieceWhse : lclQuotePieceWhseList) {
                    if (lclPieceWhse.getWarehouse() != null) {
                        if (lclPieceWhse.getWarehouse().getWarehouseName().equals(location)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public String[] checkLinkedDrsCount(String unitSsId) throws Exception {
        String data[] = new String[2];
        LclUnitDAO lclUnitDAO = new LclUnitDAO();
        data[0] = lclUnitDAO.getInUnitCount(unitSsId).toString();
        if (Integer.parseInt(data[0]) > 0) {
            data[1] = lclUnitDAO.getRelsToInvoiceCount(unitSsId, 1).toString();
        }
        return data;
    }

    public String checkAgentsForInvoice(String unitSsId) throws Exception {
        LclUnitDAO lclUnitDAO = new LclUnitDAO();
        String agentNo = lclUnitDAO.getRelsToInvoiceAgentCount(unitSsId);
        return agentNo;
    }

    public Integer checkRelsInvoiceCount(String unitSsId) throws Exception {
        LclUnitDAO lclUnitDAO = new LclUnitDAO();
        return lclUnitDAO.getRelsToInvoiceCount(unitSsId, 1);
    }

    public String validateDrBillToParty(String unitSsId) throws Exception {
        String data = "";
        if (CommonUtils.isNotEmpty(unitSsId)) {
            LclUtils lclUtils = new LclUtils();
            data = lclUtils.getDrBillingTypeErrorMessage(Long.parseLong(unitSsId));
        }
        return data;
    }

    public String validateDrHasConsigneeOrNot(String unitSsId) throws Exception {
        return new LclUnitSsDAO().validateDrHasConsigneeOrNot(Long.parseLong(unitSsId));
    }

    public boolean checkCarrierCommodity(String fileNumberId) throws Exception {
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            List<LclBookingPiece> pieceList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
            if (CommonUtils.isNotEmpty(pieceList)) {
                for (LclBookingPiece piece : pieceList) {
                    String code = piece.getCommodityType().getCode().trim();
                    if (code.equals("032500")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public String getRegions(String origin, String Destination, String rateType) throws Exception {
        String terminal = "";
        String org = "";
        String dest = "";
        PortsDAO portsdao = new PortsDAO();
        RefTerminalDAO refterminaldao = new RefTerminalDAO();
        if (rateType.equals("R")) {
            rateType = "Y";
        }
        RefTerminal refterminal = refterminaldao.getTerminalByUnLocation(origin, rateType);
        if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
            org = refterminal.getTrmnum();
        }
        Ports ports = portsdao.getByProperty("unLocationCode", Destination);
        if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
            dest = ports.getEciportcode();
        }
        terminal += org + "-" + dest;
        return terminal;
    }

    public String getOrgDestValues(String fileNumberId) throws Exception {
        String origin = "";
        String destination = "";
        String org = "";
        String dest = "";
        String orgDestVal = "";
        String rateType = "";
        LclQuote lclQuote = new LCLQuoteDAO().findById(Long.parseLong(fileNumberId));
        PortsDAO portsdao = new PortsDAO();
        RefTerminalDAO refterminaldao = new RefTerminalDAO();
        if (lclQuote.getRateType().equals("R")) {
            rateType = "Y";
        } else {
            rateType = "C";
        }
        if (lclQuote.getPortOfOrigin() != null) {
            origin = lclQuote.getPortOfOrigin().getUnLocationCode();
        }
        if (lclQuote.getFinalDestination() != null) {
            destination = lclQuote.getFinalDestination().getUnLocationCode();
        }
        RefTerminal refterminal = refterminaldao.getTerminalByUnLocation(origin, rateType);
        if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
            org = refterminal.getTrmnum();
        }
        Ports ports = portsdao.getByProperty("unLocationCode", destination);
        if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
            dest = ports.getEciportcode();
        }
        orgDestVal += org + "-" + dest;
        return orgDestVal;
    }

    public boolean checkNotesCount(String acctNo, String moduleName, String type) throws Exception {
        NotesDAO notesDAO = new NotesDAO();
        if (moduleName.equalsIgnoreCase("Imports")) {
            if (type.equalsIgnoreCase("S")) {
                return notesDAO.isShipCustomerNotesForImports(acctNo);
            } else {
                return notesDAO.isCustomerNotesForImports(acctNo);
            }
        } else {
            return notesDAO.isCustomerNotes(acctNo);
        }
    }

    public Integer disposDesc(String filterByChanges) throws Exception {
        Integer id = null;
        Disposition disp = new Disposition();
        DispositionDAO dispdao = new DispositionDAO();
        if (filterByChanges.equalsIgnoreCase("lclExport")) {
            disp = dispdao.getByProperty("eliteCode", "ACTV");
        } else if (filterByChanges.equalsIgnoreCase("lclCfcl")) {
            disp = dispdao.getByProperty("eliteCode", "ACTV");
        } else if (filterByChanges.equalsIgnoreCase("lclDomestic")) {
            disp = dispdao.getByProperty("eliteCode", "LDIN");
        } else if (filterByChanges.equalsIgnoreCase("RCVD")) {
            disp = dispdao.getByProperty("eliteCode", "RCVD");
        } else if (filterByChanges.equalsIgnoreCase("RUNV")) {
            disp = dispdao.getByProperty("eliteCode", "RUNV");
        } else if (filterByChanges.equalsIgnoreCase("B") || filterByChanges.equalsIgnoreCase("RF")) {
            disp = dispdao.getByProperty("eliteCode", "OBKG");
        }
        if (disp != null) {
            id = disp.getId();
        }
        return id;
    }

    public Integer dispoOnLoadAndUnLoad(String filterByChanges) throws Exception {
        Integer id = null;
        Disposition disp = new Disposition();
        DispositionDAO dispdao = new DispositionDAO();
        if ("lclExport".equals(filterByChanges) || "lclCfcl".equals(filterByChanges)) {
            disp = dispdao.getByProperty("eliteCode", "INTX");
        }
        if ("lclDomestic".equals(filterByChanges)) {
            disp = dispdao.getByProperty("eliteCode", "INTR");
        }
        if (disp != null && disp.getId() != null) {
            id = disp.getId();
        }
        return id;
    }

    public String saveImportRelease(String fileId, String releaseName, String userId, String userName, String buttonValue, String moduleName) throws Exception {
        Date now = new Date();
        Date dateFormat = DateUtils.formatDateAndParseTo(now, "dd-MMM-yyyy hh:mm a");
        String dateTimeV = DateUtils.formatDate(dateFormat, "dd-MMM-yyyy hh:mm a");
        String[] dateTimeArray = dateTimeV.split(" ");
        User user = new UserDAO().findById(Integer.parseInt(userId));
        if (moduleName != null && !"".equals(moduleName) && LclCommonConstant.LCL_FILE_TYPE_QUOTE.equalsIgnoreCase(moduleName)) {
            LclQuoteImportDAO lclQuoteImportDAO = new LclQuoteImportDAO();
            return lclQuoteImportDAO.updateFreightRelease(fileId, releaseName, userName, buttonValue, dateTimeArray, dateFormat, user, lclQuoteImportDAO);
        } else {
            LclBookingImportDAO lclBookingImportDAO = new LclBookingImportDAO();
            new LCLBookingDAO().updateUserDetails(Long.parseLong(fileId),Integer.parseInt(userId));
            return lclBookingImportDAO.updateFreightRelease(fileId, releaseName, userName, buttonValue, dateTimeArray, dateFormat, user, lclBookingImportDAO);
        }
    }

    public String deleteImportRelease(String fileId, String releaseName, String userId, String moduleName) throws Exception {
        LclUtils lclUtils = new LclUtils();
        if (moduleName != null && !"".equals(moduleName) && LclCommonConstant.LCL_FILE_TYPE_QUOTE.equalsIgnoreCase(moduleName)) {
            LclQuoteImportDAO lclQuoteImportDAO = new LclQuoteImportDAO();
            lclQuoteImportDAO.deleteFreightRelease(fileId, releaseName, userId, lclQuoteImportDAO, lclUtils);
        } else {
            LclBookingImportDAO lclBookingImportDAO = new LclBookingImportDAO();
            lclBookingImportDAO.deleteFreightRelease(fileId, releaseName, userId, lclBookingImportDAO, lclUtils);
        }
        return "";
    }

    public String impwarehsDetails(String warehouseId) throws Exception {//remove
        StringBuilder details = new StringBuilder();
        WarehouseDAO warehouseDAO = new WarehouseDAO();
        Warehouse warehouse = warehouseDAO.findById(Integer.parseInt(warehouseId));
        if (CommonFunctions.isNotNull(warehouse)) {
            if (CommonFunctions.isNotNull(warehouse.getAddress())) {
                details.append(warehouse.getAddress()).append("\n");
            }
            if (CommonFunctions.isNotNull(warehouse.getCity())) {
                details.append(warehouse.getCity()).append(", ");
            }
            if (CommonFunctions.isNotNull(warehouse.getState())) {
                details.append(warehouse.getState()).append("  ");
            }
            if (CommonFunctions.isNotNull(warehouse.getZipCode())) {
                details.append(warehouse.getZipCode()).append("\n");
            }
            if (CommonFunctions.isNotNull(warehouse.getPhone())) {
                details.append("Phone:").append(warehouse.getPhone()).append("\n");
            }
            if (CommonFunctions.isNotNull(warehouse.getFax())) {
                details.append("Fax:").append(warehouse.getFax());
            }

        }
        return details.toString();
    }

//    public void setPost(String fileId) throws Exception {//remove
//        String st = null;
//        LclBl lclBl = new LCLBlDAO().findById(Long.parseLong(fileId));
//        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
//        User user = (User) request.getSession().getAttribute("loginuser");
//        lclBl.setPostedByUser(user.getUserId());
//        lclBl.setPostedDate(new Date());
//        new LCLBlDAO().saveOrUpdate(lclBl);
//    }
//    public void setUnPost(String fileId) throws Exception {//remove
//        String st = null;
//        LclBl lclBl = new LCLBlDAO().findById(Long.parseLong(fileId));
//        lclBl.setPostedDate(null);
//        lclBl.setPostedByUser(null);
//        new LCLBlDAO().saveOrUpdate(lclBl);
//    }
    public String updateLclOptions(String fileId, String key, String value, HttpServletRequest request) throws Exception {
        LclOptionsDAO optionsDAO = new LclOptionsDAO();
        User user = (User) request.getSession().getAttribute("loginuser");
        LclOptions options = optionsDAO.createInstance(Long.parseLong(fileId), value, user);
        options.setOptionKey(key);
        optionsDAO.saveOrUpdate(options);

//        if (value.equalsIgnoreCase("RECEIVE") || value.equalsIgnoreCase("MINI") || value.equalsIgnoreCase("PIER") || value.equalsIgnoreCase("ARRIVALDATE") || value.equalsIgnoreCase("HBLPIER")
//                || value.equalsIgnoreCase("HBLPOL") || value.equalsIgnoreCase ("DELIVERY")|| value.equalsIgnoreCase ("ROUTING")
//                ||value.equalsIgnoreCase ("EXPORT")) {
//            boolean isCheck = lclOptionsDAO.checkValueReceive(Long.parseLong(fileId), value);
//            if (isCheck == false) {
//                lclOptionsDAO.insertLclOptions(Long.parseLong(fileId), user.getUserId(), key, value);
//            }
//        }
//        lclOptionsDAO.updateLclOptions(Long.parseLong(fileId), user.getUserId(), key, value);
//        LclOptions lclOptions = lclOptionsDAO.getLclOptionsByValue(Long.parseLong(fileId), "FP");
//        LclOptions lclOptionsFreightPickup = lclOptionsDAO.getLclOptionsByValue(Long.parseLong(fileId), "FPFEILD");
//        if (lclOptions != null && lclOptions.getKey().equalsIgnoreCase("N") && lclOptionsFreightPickup != null && CommonUtils.isNotEmpty(lclOptionsFreightPickup.getKey())) {
//            lclOptionsDAO.updateLclOptions(Long.parseLong(fileId), user.getUserId(), "", "FPFEILD");
//        }
        return null;
    }

    public void getMemoEmailIds(String fileNumberId, String correctionId, String cnType,
            String acctNo, String newAcctNo, String differenceAmt, String viewMode,
            HttpServletRequest request) throws Exception {
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            // declare variables
            Long fileId = Long.parseLong(fileNumberId);
            String arEmail = "";
            String creditArEmail = "";
            String debitArEmail = "";
            Set<String> arCreditEmails = new TreeSet<String>();
            Set<String> arDebitEmails = new TreeSet<String>();
            List<String> debitEmailList = new ArrayList<String>();
            List<String> creditEmailList = new ArrayList<String>();
            Set<String> creditEmails = new TreeSet<String>();
            Set<String> debitEmails = new TreeSet<String>();
            Set<String> creditContacts = new TreeSet<String>();
            Set<String> debitContacts = new TreeSet<String>();
            LCLCorrectionDAO lclCorrectionDAO = new LCLCorrectionDAO();
            if ("view".equalsIgnoreCase(viewMode)) {
                Object[] creditDebitObject = lclCorrectionDAO.getCreditDebitEMails(Long.parseLong(correctionId));
                if (creditDebitObject != null) {
                    if (null != creditDebitObject[0]) {
                        creditEmailList.addAll(Arrays.asList((creditDebitObject[0].toString().toLowerCase()).split(",")));
                    }
                    if (null != creditDebitObject[1]) {
                        debitEmailList.addAll(Arrays.asList((creditDebitObject[1].toString().toLowerCase()).split(",")));
                    }
                }
                creditEmails = new TreeSet<String>(creditEmailList);
                debitEmails = new TreeSet<String>(debitEmailList);
            }
            // Get previous corrections if notice number not empty
            if (CommonUtils.isNotEmpty(correctionId)) {
                boolean emailDebitAdded = false, emailCreditAdded = false;
                Object[] creditDebitObject = lclCorrectionDAO.getCreditDebitEMails(Long.parseLong(correctionId));
                if (creditDebitObject != null) {
                    if (null != creditDebitObject[0] && !creditDebitObject[0].toString().trim().equals("")) {
                        creditEmailList.addAll(Arrays.asList((creditDebitObject[0].toString().toLowerCase()).split(",")));
                        emailCreditAdded = true;
                    }
                    if (null != creditDebitObject[1] && !creditDebitObject[1].toString().trim().equals("")) {
                        debitEmailList.addAll(Arrays.asList((creditDebitObject[1].toString().toLowerCase()).split(",")));
                        emailDebitAdded = true;
                    }
                }
                if ("A".equalsIgnoreCase(cnType)) {
                    newAcctNo = acctNo;
                }
                if (!emailDebitAdded || !emailCreditAdded) { // correction type A
                    LCLCorrectionChargeDAO lclCorrectionChargeDAO = new LCLCorrectionChargeDAO();
                    boolean isCredit = false, isDebit = false;
                    // get account number from recent posted correction
                    if (CommonUtils.isNotEmpty(correctionId)) {
                        List<Object> lclCorrectionChargeList = lclCorrectionChargeDAO.getCorrectionChargesSumForCreditDebit(correctionId);
                        for (Object lclCorrectionCharge : lclCorrectionChargeList) {
                            if (!isDebit || !isCredit) {
                                if (Double.parseDouble(lclCorrectionCharge.toString()) > 0d) {
                                    isDebit = true;
                                } else {
                                    isCredit = true;
                                }
                            }
                        }
                        // based on difference amount add into credit/debit memo Set
                        arEmail = getArEmail(acctNo);
                        if (isDebit) {
                            debitEmails.addAll(getEmailIdSet(acctNo));
                            if (null != arEmail && !"".equals(arEmail)) {
                                arDebitEmails.add(arEmail);
                                debitEmails.remove(arEmail);
                            }
                        }
                        if (isCredit) {
                            creditEmails.addAll(getEmailIdSet(acctNo));
                            if (null != arEmail && !"".equals(arEmail)) {
                                arCreditEmails.add(arEmail);
                                creditEmails.remove(arEmail);
                            }
                        }
                    }
                }
                creditArEmail = getArEmail(acctNo);
                debitArEmail = getArEmail(newAcctNo);
                creditContacts.addAll(getEmailIdSet(acctNo));
                debitContacts.addAll(getEmailIdSet(newAcctNo));
                if (CommonUtils.isNotEmpty(creditEmailList)) {
                    creditEmails = new TreeSet<String>(creditEmailList);
                }
                if (CommonUtils.isNotEmpty(debitEmailList)) {
                    debitEmails = new TreeSet<String>(debitEmailList);
                }
                if (CommonUtils.isNotEmpty(creditEmails)) {
                    creditContacts.removeAll(creditEmails);
                    if (CommonUtils.isNotEmpty(creditArEmail)) {
                        creditContacts.remove(creditArEmail.toLowerCase());
                        creditEmails.remove(creditArEmail.toLowerCase());
                        arCreditEmails.add(creditArEmail);
                    }
                }
                if (CommonUtils.isNotEmpty(debitEmails)) {
                    debitContacts.removeAll(debitEmails);
                    if (CommonUtils.isNotEmpty(debitArEmail)) {
                        debitContacts.remove(debitArEmail.toLowerCase());
                        debitEmails.remove(debitArEmail.toLowerCase());
                        arDebitEmails.add(debitArEmail);
                    }
                }
            } else if (CommonUtils.isNotEmpty(cnType) && !cnType.equalsIgnoreCase("A") && !cnType.equalsIgnoreCase("Y")) {
                Object latestAcctNo = lclCorrectionDAO.getFieldDescByFileId(fileId, "party_acct_no");
                if (latestAcctNo != null) {
                    acctNo = latestAcctNo.toString();
                }
                debitArEmail = getArEmail(newAcctNo);
                creditArEmail = getArEmail(acctNo);
                debitEmails.addAll(getEmailIdSet(newAcctNo));
                arDebitEmails.add(debitArEmail);
                if (null != debitArEmail) {
                    debitEmails.remove(debitArEmail.toLowerCase());
                }
                creditEmails.addAll(getEmailIdSet(acctNo));
                arCreditEmails.add(creditArEmail);
                if (null != creditArEmail) {
                    creditEmails.remove(creditArEmail.toLowerCase());
                }
            }
            request.setAttribute("debitEmails", debitEmails);
            request.setAttribute("arDebitEmails", arDebitEmails);
            request.setAttribute("creditEmails", creditEmails);
            request.setAttribute("arCreditEmails", arCreditEmails);
            request.setAttribute("creditContacts", creditContacts);
            request.setAttribute("debitContacts", debitContacts);
        }
//        WebContext webContext = WebContextFactory.get();
//        return webContext.forwardToString("/jsps/LCL/lclMemoEmailSection.jsp");
    }

    public String getArEmail(String acctNo) throws Exception {
        CustomerAccounting customerAccounting = new CustomerAccountingDAO().findByAccountNumber(acctNo);
        return customerAccounting.getAcctRecEmail();
    }

    public Set<String> getEmailIdSet(String acctNo) throws Exception {
        Set<String> emailIdSet = new TreeSet<String>();
        CustomerContactDAO customerContactDAO = new CustomerContactDAO();
        List<Object> contactsList = customerContactDAO.getAllEMailIdsByCodeJE1E3(acctNo);
        for (Object email : contactsList) {
            if (email != null) {
                emailIdSet.add(email.toString().toLowerCase());
            }
        }
        return emailIdSet;
    }

    public String getEmailIds(String acctNo) throws Exception {
        Set<String> emailIdSet = getEmailIdSet(acctNo);
        StringBuilder emailIds = new StringBuilder();
        for (String emailId : emailIdSet) {
            if (!"".equals(emailId)) {
                emailIds.append(emailId).append(",");
            }
        }
        return emailIds.toString();
    }

    public String getPolPodId(String pol, String pod) throws Exception {
        UnLocationDAO unLocPolDao = new UnLocationDAO();
        UnLocationDAO unLocPodDao = new UnLocationDAO();
        UnLocation unLocPol = unLocPolDao.getUnlocation(pol);
        UnLocation unLocPod = unLocPodDao.getUnlocation(pod);
        String unLocId = unLocPol.getId() + "," + unLocPod.getId();
        return unLocId;
    }

    public BigInteger getVoyageId(String pol, String pod, String scheduleNo) throws Exception {
        LclSsHeaderDAO lclSsHeaderDAO = new LclSsHeaderDAO();
        BigInteger headerId = lclSsHeaderDAO.getHeaderId(pol, pod, scheduleNo);
        if (null != headerId) {
            return headerId;
        }
        return null;
    }

    public Long getUnitDetails(Long headerId) throws Exception {
        List<LclUnitSs> lclUnitSs = null;
        LclUnitSsDAO lclUnitSsDAO = new LclUnitSsDAO();
        lclUnitSs = lclUnitSsDAO.findByProperty("lclSsHeader.id", headerId);
        if (!lclUnitSs.isEmpty()) {
            return lclUnitSs.get(0).getId();
        } else {
            return null;
        }
    }

    public String[] defaultImpagentComm(String agentNumber) throws Exception {
        commodityTypeDAO commodityTypeDAO = new commodityTypeDAO();
        String commodityDetails[] = commodityTypeDAO.defaultImportComm(agentNumber);
        return commodityDetails;
    }

    public boolean verifyRatesCombination(String orgTrm, String eciPort, String comCode) throws Exception {
        String databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        LCLRatesDAO lclratesdao = new LCLRatesDAO(databaseSchema);
        List<LclRatesInfoBean> ratesList = lclratesdao.findRates(orgTrm, eciPort, comCode);
        if (!ratesList.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean verifyPortChgRatesCombination(String orgTrm, String eciPort, String comCode, String chgCode) throws Exception {
        String databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        LCLRatesDAO lclratesdao = new LCLRatesDAO(databaseSchema);
        List<LclRatesPrtChgBean> ratesPrtChgList = lclratesdao.findPrtChgRateswithChgCode(orgTrm, eciPort, comCode, chgCode);
        if (!ratesPrtChgList.isEmpty()) {
            return true;
        }
        return false;
    }

    public String fillUom(String unlocationCode) throws Exception {
        Ports ports = new PortsDAO().getByProperty("unLocationCode", unlocationCode);
        if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
            return ports.getEngmet();
        }
        return "";
    }

    //update bookingcharges by released to inv by agent in imports and export for bundle charges page--lcl.js methodnamee->updateCharges
    public void updateBookingCharges(String id, String checkBoxValue, String index) throws Exception {
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        String flag = "0";
        if ("true".equalsIgnoreCase(checkBoxValue)) {
            flag = "1";
        }
        lclCostChargeDAO.updateCharges(id, flag, index);
    }
    //update QuoteCharges export for bundle charges page--exportQuote.js methodnamee->updateCharges

    public void updateQuoteCharges(String id, String checkBoxValue, String index) throws Exception {
        String flag = "0";
        if ("true".equalsIgnoreCase(checkBoxValue)) {
            flag = "1";
        }
        new LclQuoteAcDAO().updateCharges(id, flag, index);
    }

    public Integer unlinkDRFromBkgPieceUnit(String orgFileNumberId, String unitSsId, HttpServletRequest request) throws Exception {
        int count = 0;
        User user = (User) request.getSession().getAttribute("loginuser");
        LclUnitDAO lclUnitDAO = new LclUnitDAO();
        String segFileNumberIds = new LclBookingSegregationDao().getChildFileNumbers(orgFileNumberId);
        StringBuilder fileIds = new StringBuilder();
        String appendedFileIds = CommonUtils.isNotEmpty(segFileNumberIds) ? fileIds.append(orgFileNumberId).append(",").append(segFileNumberIds).toString() : orgFileNumberId;
        String bkgPieceId[] = lclUnitDAO.getBkgPieceUnitId(appendedFileIds);
        if (bkgPieceId != null) {
            String[] array = appendedFileIds.split(",");
            for (String fileId : array) {
                if (bkgPieceId[2] != null) {
                    new LclUtils().insertLCLRemarks(Long.parseLong(fileId), "UnLinked from a Unit# " + bkgPieceId[1].toString() + " of Voyage# " + bkgPieceId[2].toString() + "", "Unit_Tracking", user);
                }
                String fileNumberStatus = (String) new LclFileNumberDAO().getStatusByField("id", fileId);
                String realPath = request.getSession().getServletContext().getRealPath("/");
                if (CommonUtils.isNotEmpty(fileNumberStatus) && fileNumberStatus.equalsIgnoreCase("M")) {
                    new LclManifestDAO().getAllManifestImportsBookingsByUnitSS(Long.parseLong(unitSsId), null, null, user, false, realPath, true, fileId);
                }
            }
            if (bkgPieceId[0] != null) {
                count = lclUnitDAO.unlinkDrfromBkgUnit(bkgPieceId[0].toString());
            }
            new LclBookingSegregationDao().deleteSegregatedDrs(orgFileNumberId);
            new LclBookingImportAmsDAO().updateSegFileIdAsNull(orgFileNumberId, user.getUserId());
        }
        return count;
    }

    public String[] fillForeignPODImp(String fdId, String usaID) throws Exception {
        String podValues[] = new String[8];
        int pooid = Integer.parseInt(fdId);
        int fdid = Integer.parseInt(usaID);
        LclBookingPlanDAO lclBookingPlanDAO = new LclBookingPlanDAO();
        LclBookingPlanBean lclBookingPlanBean = lclBookingPlanDAO.getRelay(pooid, fdid, "N");
        if (lclBookingPlanBean != null) {
            if (lclBookingPlanBean.getPod_id() != null) {
                podValues[0] = lclBookingPlanBean.getPod_id().toString();
                podValues[1] = lclBookingPlanBean.getPod_name().toString();
                podValues[2] = lclBookingPlanBean.getPod_code().toString();
                UnLocation unlocation = new UnLocationDAO().findById(lclBookingPlanBean.getPod_id());
                if (unlocation != null && unlocation.getCountryId() != null) {
                    if (unlocation.getCountryId().getCodedesc().equals("UNITED STATES")) {
                        if (unlocation.getStateId() != null) {
                            podValues[3] = unlocation.getStateId().getCode();
                        }
                    } else {
                        podValues[3] = unlocation.getCountryId().getCodedesc();
                    }
                }
            }
            if (lclBookingPlanBean.getPol_id() != null) {
                podValues[4] = lclBookingPlanBean.getPol_id().toString();
                podValues[5] = lclBookingPlanBean.getPol_name().toString();
                podValues[6] = lclBookingPlanBean.getPol_code().toString();
                UnLocation unlocation = new UnLocationDAO().findById(lclBookingPlanBean.getPol_id());
                if (unlocation != null && unlocation.getCountryId() != null) {
                    if (unlocation.getCountryId().getCodedesc().equals("UNITED STATES")) {
                        if (unlocation.getStateId() != null) {
                            podValues[7] = unlocation.getStateId().getCode();
                        }
                    } else {
                        podValues[7] = unlocation.getCountryId().getCodedesc();
                    }
                }
            }
        }
        return podValues;
    }

    public String checkCurrentLocation(Long fileId) throws Exception {
        String polName = null;
        LclBookingDispoDAO lclBookingDispoDAO = new LclBookingDispoDAO();
        polName = lclBookingDispoDAO.currenLocationUnLocName(fileId);
        return polName;
    }

    public String checkBookingNofromEdi(String fileNumberId) throws Exception {
        String flag = "false";
        LclFileNumber lclFileNumber = new LclFileNumberDAO().findById(Long.parseLong(fileNumberId));
        String bookingNo = lclFileNumber.getFileNumber();
        EdiTrackingSystemDAO ediTrackingSystemDAO = new EdiTrackingSystemDAO();
        EdiTrackingSystem ediTrackingSystem = ediTrackingSystemDAO.findByBookingNo(bookingNo);
        if (ediTrackingSystem != null) {
            flag = "true";
        } else {
            flag = knOblTrackingSystem(this.getKNOblNo(fileNumberId, lclFileNumber.getFileNumber())) ? "true" : "false";
        }
        return flag;
    }

    public void getDatafromEdi(String fileNumberId, HttpServletRequest request) throws Exception {
        LclFileNumber lclFileNumber = new LclFileNumberDAO().findById(Long.parseLong(fileNumberId));
        String bookingNo = lclFileNumber.getFileNumber();
        EdiTrackingSystemDAO ediTrackingSystemDAO = new EdiTrackingSystemDAO();
        EdiTrackingSystem ediTrackingSystem = ediTrackingSystemDAO.findByBookingNo(bookingNo);
        if (null != ediTrackingSystem) {
            Shipment shipment = (Shipment) ediTrackingSystemDAO.findShipmentByEdiTrackingSystem(ediTrackingSystem, request);
            //***************COMMODITY**********************************************
            HashSet<PackageDetails> packSet = (HashSet<PackageDetails>) shipment.getPackageDetailsSet();
            PackageDetails packageDetails = null;
            Iterator<PackageDetails> i = packSet.iterator();
            if (i.hasNext()) {
                packageDetails = i.next();
            }
            //**************PARTY***************************************************
            HashSet<Party> partySet = (HashSet<Party>) shipment.getPartySet();
            Party party = null;
            Party shipperParty = null;
            Party forwarderParty = null;
            Party consigneeParty = null;
            Party notifyParty = null;
            Iterator<Party> j = partySet.iterator();
            while (j.hasNext()) {
                party = j.next();
                if (party.getRole() == null || party.getRole().equals("Forwarder")) {
                    forwarderParty = party;
                } else if (party.getRole() == null || party.getRole().equals("Shipper")) {
                    shipperParty = party;
                } else if (party.getRole() == null || party.getRole().equals("Consignee")) {
                    consigneeParty = party;
                } else if (party.getRole() == null || party.getRole().equals("MainNotifyParty")) {
                    notifyParty = party;
                }
            }
            //**********************************************************************
            String goodsDesc = null;
            String marksAndNo = null;
            String commodity = null;
            String forwarderName = null;
            String shipperName = null;
            String consigneeName = null;
            String notifyName = null;
            String blOriginCity = null;
            String portOfLoadCity = null;
            String portOfDischargeCity = null;
            String exportRefNo = null;
            String frtfwdRefNo = null;
            String shipperRefNo = null;
            if (null != packageDetails) {
                goodsDesc = packageDetails.getGoodsDesc();
                marksAndNo = packageDetails.getMarksAndNo();
                commodity = packageDetails.getCommodity();
            }
            if (null != forwarderParty) {
                forwarderName = forwarderParty.getName();
            }
            if (null != shipperParty) {
                shipperName = shipperParty.getName();
            }
            if (null != consigneeParty) {
                consigneeName = consigneeParty.getName();
            }
            if (null != notifyParty) {
                notifyName = notifyParty.getName();
            }
            if (shipment != null) {
                blOriginCity = shipment.getBlOrginCity();
                portOfLoadCity = shipment.getPortOLoadCity();
                portOfDischargeCity = shipment.getPortOfDischargeCity();
                exportRefNo = shipment.getExportRefNo();
                frtfwdRefNo = shipment.getFrtfwdRefNo();
                shipperRefNo = shipment.getShipperRefNo();
            }
            request.setAttribute("blOrigin", blOriginCity);
            request.setAttribute("portOfLoad", portOfLoadCity);
            request.setAttribute("placeOfDischarge", portOfDischargeCity);
            request.setAttribute("packSet", packSet);
            request.setAttribute("forwarderName", forwarderName);
            request.setAttribute("shipperName", shipperName);
            request.setAttribute("consigneeName", consigneeName);
            request.setAttribute("notifyName", notifyName);
            request.setAttribute("commodity", commodity);
            request.setAttribute("exportRefNo", exportRefNo);
            request.setAttribute("frtfwdRefNo", frtfwdRefNo);
            request.setAttribute("shipperRefNo", shipperRefNo);
            request.setAttribute("goodsDesc", goodsDesc);
            request.setAttribute("marksAndNo", marksAndNo);
            request.setAttribute("fileNumberId", fileNumberId);
//            WebContext webContext = WebContextFactory.get();
//            return webContext.forwardToString("/jsps/LCL/lclEdiData.jsp");
        } else {
            request.setAttribute("noData", true);
        }
    }

    public void convertToBlEdi(String fileNumberId, String[] lclEdiData, HttpServletRequest request) throws Exception {
        try {
            HttpSession session = request.getSession();
            LCLBlDAO lCLBlDAO = new LCLBlDAO();
            UnLocationDAO unLocationDAO = new UnLocationDAO();
            LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
            LclBLPieceDAO lclBLPieceDAO = new LclBLPieceDAO();
            EdiTrackingSystemDAO ediTrackingSystemDAO = new EdiTrackingSystemDAO();
            User user = (User) session.getAttribute("loginuser");
            LclFileNumber lclFileNumber = new LclFileNumberDAO().findById(Long.parseLong(fileNumberId));
            String bookingNo = lclFileNumber.getFileNumber();
            EdiTrackingSystem ediTrackingSystem = ediTrackingSystemDAO.findByBookingNo(bookingNo);
            if (null != ediTrackingSystem) {
                Shipment shipment = ediTrackingSystemDAO.findShipmentByEdiTrackingSystem(ediTrackingSystem, request);
                Long fileNumber = Long.valueOf(fileNumberId);
                LclBl lclBl = lCLBlDAO.getByProperty("lclFileNumber.id", fileNumber);
                //**************PARTY***********************************************
                HashSet<Party> partySet = (HashSet<Party>) shipment.getPartySet();
                Party party = null;
                Party shipperParty = null;
                Party forwarderParty = null;
                Party consigneeParty = null;
                Party notifyParty = null;
                Iterator<Party> j = partySet.iterator();
                while (j.hasNext()) {
                    party = j.next();
                    if (party.getRole() == null || party.getRole().equals("Forwarder")) {
                        forwarderParty = party;
                    } else if (party.getRole() == null || party.getRole().equals("Shipper")) {
                        shipperParty = party;
                    } else if (party.getRole() == null || party.getRole().equals("Consignee")) {
                        consigneeParty = party;
                    } else if (party.getRole() == null || party.getRole().equals("MainNotifyParty")) {
                        notifyParty = party;
                    }
                }
                //*****Populating data into BL screen after checking in popup of LclEdi Data
                //BL Origin
                if (null != lclEdiData[0] && !"".equals(lclEdiData[0]) && lclEdiData[0].equals("true")) {
                    String blOrigin = shipment.getBlOrigin();
                    String blOriginCity = shipment.getBlOrginCity();
                    if (blOrigin != null && !"".equals(blOrigin)) {
                        UnLocation unLocation = unLocationDAO.getUnlocation(blOrigin);
                        lclBl.setPortOfOrigin(unLocation);
                        lclBl.setPointOfOrigin(blOriginCity);
                    }
                }
                //Port Of Loading
                if (null != lclEdiData[1] && !"".equals(lclEdiData[1]) && lclEdiData[1].equals("true")) {
                    String portOfLoad = shipment.getPortOfLoad();
                    if (portOfLoad != null && !"".equals(portOfLoad)) {
                        UnLocation unLocation = unLocationDAO.getUnlocation(portOfLoad);
                        lclBl.setPortOfLoading(unLocation);
                    }
                }
                //Port Of Discharge
                if (null != lclEdiData[2] && !"".equals(lclEdiData[2]) && lclEdiData[2].equals("true")) {
                    String placeOfDischarge = shipment.getPlaceOfDischarge();
                    if (placeOfDischarge != null && !"".equals(placeOfDischarge)) {
                        UnLocation unLocation = unLocationDAO.getUnlocation(placeOfDischarge);
                        lclBl.setPortOfDestination(unLocation);
                    }
                }
                //Shipper
                if (null != lclEdiData[3] && !"".equals(lclEdiData[3]) && lclEdiData[3].equals("true")) {
                    LclContact lclContact = lclBl.getShipContact();
                    lclContact.setCompanyName(shipperParty.getName());
                    lclContact.setAddress(shipperParty.getAddress());
                }
                //Forwarder
                if (null != lclEdiData[4] && !"".equals(lclEdiData[4]) && lclEdiData[4].equals("true")) {
                    LclContact lclContact = lclBl.getFwdContact();
                    lclContact.setCompanyName(forwarderParty.getName());
                    lclContact.setAddress(forwarderParty.getAddress());
                }
                //Consignee
                if (null != lclEdiData[5] && !"".equals(lclEdiData[5]) && lclEdiData[5].equals("true")) {
                    LclContact lclContact = lclBl.getConsContact();
                    lclContact.setCompanyName(consigneeParty.getName());
                    lclContact.setAddress(consigneeParty.getAddress());
                }
                //Notify
                if (null != lclEdiData[6] && !"".equals(lclEdiData[6]) && lclEdiData[6].equals("true")) {
                    LclContact lclContact = lclBl.getNotyContact();
                    lclContact.setCompanyName(notifyParty.getName());
                    lclContact.setAddress(notifyParty.getAddress());
                }
                //Export Ref No
                if (null != lclEdiData[7] && !"".equals(lclEdiData[7]) && lclEdiData[7].equals("true")) {
                    String exportRefNo = shipment.getExportRefNo();
                    LclRemarks lclRemarks = lclRemarksDAO.getLclRemarksByType(fileNumberId, "Export Reference");
                    if (lclRemarks == null) {
                        lclRemarks = new LclRemarks();
                        lclRemarks.setLclFileNumber(lclFileNumber);
                        lclRemarks.setType("Export Reference");
                        lclRemarks.setRemarks(exportRefNo);
                        lclRemarks.setEnteredBy(user);
                        lclRemarks.setEnteredDatetime(new Date());
                        lclRemarks.setModifiedBy(user);
                        lclRemarks.setModifiedDatetime(new Date());
                        lclRemarksDAO.save(lclRemarks);
                    } else {
                        lclRemarks.setRemarks(exportRefNo);
                    }
                }
                //Forwarder Ref No
                if (null != lclEdiData[8] && !"".equals(lclEdiData[8]) && lclEdiData[8].equals("true")) {
                    String frtfwdRefNo = shipment.getFrtfwdRefNo();
                    lclBl.setFwdReference(frtfwdRefNo);
                }
                //Shipper Ref No
                if (null != lclEdiData[9] && !"".equals(lclEdiData[9]) && lclEdiData[9].equals("true")) {
                    String shipperRefNo = shipment.getShipperRefNo();
                    lclBl.setShipReference(shipperRefNo);
                }
                //Commodity
                if (null != lclEdiData[10] && !"".equals(lclEdiData[10]) && lclEdiData[10].equals("true")) {
                    // lclBLPieceDAO.deleteBlPieceByFileNumber(fileNumber);
                    HashSet<PackageDetails> packSet = (HashSet<PackageDetails>) shipment.getPackageDetailsSet();
                    PackageDetails packageDetails = null;
                    Iterator<PackageDetails> i = packSet.iterator();
                    LclBlPiece lclBlPiece = null;
                    List<LclBlPiece> lclCommodityList = lclBLPieceDAO.findByProperty("lclFileNumber.id", lclBl.getFileNumberId());
                    Iterator<LclBlPiece> k = lclCommodityList.iterator();
                    while (i.hasNext() && k.hasNext()) {
                        lclBlPiece = k.next();
                        packageDetails = i.next();
                        String commodity = packageDetails.getCommodity();
                        Integer noOfPackage = packageDetails.getNoOfPackage();
                        String packageTypeCode = packageDetails.getPackageTypeCode();
                        BigDecimal goodcrossVolume = packageDetails.getGoodcrossVolume();
                        BigDecimal goodgrossWeight = packageDetails.getGoodgrossWeight();
                        //gross volume metric
                        int goodcrossVolume1 = goodcrossVolume.intValue();
                        int goodgrossVolumeMetric = goodcrossVolume1 * 100;
                        lclBlPiece.setActualVolumeMetric(new BigDecimal(goodgrossVolumeMetric));
                        //gross volume imperical
                        int goodgrossVolumeImperical = (int) (35.315 * goodgrossVolumeMetric);
                        lclBlPiece.setActualVolumeImperial(new BigDecimal(goodgrossVolumeImperical));
                        //gross metric & imperical  weight
                        int goodgrossWeight1 = goodgrossWeight.intValue();
                        int goodgrossWeightImperical = (int) (goodgrossWeight1 * 2.204622);
                        lclBlPiece.setActualWeightMetric(goodgrossWeight);
                        lclBlPiece.setActualWeightImperial(new BigDecimal(goodgrossWeightImperical));
                        //booking & actual piece count
                        int bookedPieceCount = (lclBlPiece.getBookedPieceCount()).intValue();
                        if (bookedPieceCount > 0) {
                            lclBlPiece.setActualPieceCount(packageDetails.getNoOfPackage());
                        } else {
                            lclBlPiece.setBookedPieceCount(packageDetails.getNoOfPackage());
                            lclBlPiece.setActualPieceCount(packageDetails.getNoOfPackage());
                        }
                        lclBlPiece.setPieceDesc(packageDetails.getGoodsDesc());
                        lclBlPiece.setMarkNoDesc(packageDetails.getMarksAndNo());
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean checkDispositionStatus(String unitId, HttpServletRequest request) throws Exception {
        String disposition = new LclUnitSsDispoDAO().getDisposition(unitId);
        User user = (User) request.getSession().getAttribute("loginuser");
        RoleDuty roleDuty = new RoleDutyDAO().getRoleDetails(user.getRole().getRoleId());
        if (!disposition.equals("") && (LclCommonConstant.LCL_DISPOSITION_DATA.equalsIgnoreCase(disposition)
                || LclCommonConstant.LCL_DISPOSITION_WATR.equalsIgnoreCase(disposition)
                || ("PORT".equalsIgnoreCase(disposition) && roleDuty.isLinkDrAfterDispositionPort()))) {
            return true;
        }
        return false;
    }

    public String checkE1E3F1F3CountByAcctNo(String accountNo) throws Exception {
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        CustomerAccountingDAO customerAccountingDAO = new CustomerAccountingDAO();
        if (customerAccountingDAO.isMailFax(accountNo)) {
            return "";
        }
        return "-->AR Invoice Auto Notification Contact does NOT exist," + " For the Party "
                + tradingPartnerDAO.getTradingpatnerAccName(accountNo) + "(" + accountNo
                + ")<br><br> Please Enter Contacts in TP ";
    }

    public String getClauseDescription(String releaseClause) throws Exception {
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        GenericCode genObj = null;
        genObj = genericCodeDAO.findById(new Integer(releaseClause));
        String ClauseDescription = genObj.getCodedesc();
        return ClauseDescription;
    }
    //ImportUnit Level Auto Cost

    public void checkUnitAutoCostByImp(String originId, String fdId, String cfsWareHsId, String unitTypeId, String unitFlag, HttpServletRequest request) throws Exception {
        List<ImportsManifestBean> autoCostList = new LclUnitsRates().findAutoCosts(Integer.parseInt(originId), Integer.parseInt(fdId), Integer.parseInt(cfsWareHsId), Long.parseLong(unitTypeId));
        request.setAttribute("autoCostList", autoCostList);
        //unit flag is used to check some condition based on new voyage creation and autocost recalculated
        request.setAttribute("unitFlag", unitFlag);
        if (null == autoCostList || autoCostList.isEmpty()) {
            request.setAttribute("noData", true);
        }
    }
    //Import Unit Level Agent Charges

    public void getUnitLevelAgentCharge(String unitSsId, String unitAgentFlag, HttpServletRequest request) throws Exception {
        List<ImportsManifestBean> autoAgentList = new LclUnitSsDAO().getAgentInvoiceCharges(unitSsId);
        request.setAttribute("autoCostList", autoAgentList);
        //unit flag is used to check some condition based on new voyage creation and autocost recalculated
        request.setAttribute("unitFlag", unitAgentFlag);
        if (null == autoAgentList || autoAgentList.isEmpty()) {
            request.setAttribute("noData", true);
        }
    }

    public String[] createBolXML(String bookingId, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("loginuser");
        String[] result = new String[2];
        StringBuilder sb = new StringBuilder("");
        if (CommonUtils.isNotEmpty(bookingId)) {
            try {
                DomesticBooking domesticBooking = new DomesticBookingDAO().findById(Integer.parseInt(bookingId));
                List<DomesticPurchaseOrder> purchaseOrderList = new DomesticPurchaseOrderDAO().findByProperty("bookingId.id", Integer.parseInt(bookingId));
                if (CommonUtils.isEmpty(domesticBooking.getShipperName())) {
                    sb.append("Please Enter shipper details").append("<br>");
                }
                if (CommonUtils.isEmpty(domesticBooking.getConsigneeName())) {
                    sb.append("Please Enter consignee details").append("<br>");
                }
                if (CommonUtils.isEmpty(domesticBooking.getBilltoName())) {
                    sb.append("Please Enter bill to details").append("<br>");
                }
                for (DomesticPurchaseOrder domesticPurchaseOrder : purchaseOrderList) {
                    if (CommonUtils.isEmpty(domesticPurchaseOrder.getPurchaseOrderNo())) {
                        sb.append("Please Enter purchase order number").append("<br>");
                        break;
                    }
                }
                if (CommonUtils.isEmpty(sb.toString())) {
                    String fileLocation = LoadLogisoftProperties.getProperty("reportLocation") + "/DomesticBOL";
                    File dir = new File(fileLocation);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    String fileName = fileLocation + "/BOL_" + domesticBooking.getBookingNumber() + ".pdf";
                    String contextPath = WebContextFactory.get().getServletContext().getRealPath("/");
                    new CTSBolPdfCreator().createReport(domesticBooking, purchaseOrderList, fileName, contextPath);
                    MailPropertyReader mailPropertyReader = new MailPropertyReader();
                    Properties mailProperties = mailPropertyReader.getProperties();
                    EMailScheduler scheduler = new EMailScheduler();
                    StringBuilder htmlMessageBody = new StringBuilder();
                    htmlMessageBody.append("Dear ").append(user.getFirstName()).append(",<br>\n");
                    htmlMessageBody.append("The booking sent to BOL :").append(domesticBooking.getBookingNumber());
                    String toAddress = CommonUtils.isNotEmpty(user.getEmail()) ? user.getEmail() + "," + mailProperties.getProperty("mail.primaryfrieght.toaddress") : mailProperties.getProperty("mail.primaryfrieght.toaddress");
                    MailMessageVO mailMessageVO = new MailMessageVO("Customer", toAddress, mailProperties.getProperty("mail.primaryfrieght.from.name"), mailProperties.getProperty("mail.primaryfrieght.from"), null, null, "BOL Report", htmlMessageBody.toString());
                    scheduler.createHtmlEmail(null, fileName, mailMessageVO, null);
                    String pickUpDoc = new CreateBOLXml().createXml(domesticBooking, purchaseOrderList, user);
                    Srdapp cTSServicePort = new SrdappService().getSrdappCfc();
                    String str = cTSServicePort.bImport("420", pickUpDoc);
                    result[0] = str;
                    if (str.contains("<MessageType>")) {
                        result[1] = str.substring(str.indexOf("<MessageType>") + 13, str.indexOf("<MessageType>") + 14);
                        domesticBooking.setBolStatus(result[1]);
                        if (str.contains("<Message>")) {
                            domesticBooking.setStatusMessage(str.substring(str.indexOf("<Message>") + 9, str.indexOf("</Message>")));
                        }
                    }
                } else {
                    result[0] = sb.toString();
                }

            } catch (Exception e) {
                return null;
            }
        }
        return result;
    }

    public String[] getUnitCfsDetails(String unitId, String headerId) throws Exception {
        String[] cfsDetails = new LclUnitSsImportsDAO().getCfsWarehouse(unitId, headerId);
        return cfsDetails;
    }

    public String sendEmailForDoorPdf(String fileId, String fileNumber, HttpServletRequest request) throws Exception {
        User user = (User) request.getSession().getAttribute("loginuser");
        if (user.getEmail() != null && !user.getEmail().equals("")) {
            LclPrintUtil lclPrintUtil = new LclPrintUtil();
            String realPath = request.getSession().getServletContext().getRealPath("/");
            String reportLocation = LoadLogisoftProperties.getProperty("reportLocation");
            String fileLocation = lclPrintUtil.createImportBkgReport(reportLocation, fileId, fileNumber,
                    LclReportConstants.DOCUMENTLCLDELIVERYORDER, realPath, null, request);
            new LclUtils().sendMailWithoutPrintConfig(fileLocation, "LCLDeliveryOrder",
                    LclReportConstants.DOCUMENTLCLDELIVERYORDER + " For File#" + fileNumber, "Email", "Pending",
                    user.getEmail(), fileNumber, LclReportConstants.SCREENNAMELCLIMPORTBOOKINGREPORT,
                    "", "", user);
            return user.getEmail();
        } else {
            return "";
        }
    }

    public String getChargesCode(String glaMappingId) throws Exception {
        if (Integer.parseInt(glaMappingId) == 0) {
            return "select one";
        }
        GlMappingDAO glMappingDAO = new GlMappingDAO();
        GlMapping glMapping = null;
        glMapping = glMappingDAO.findById(new Integer(glaMappingId));
        String chargeCode = glMapping.getChargeCode();
        return chargeCode;
    }

    public String isColoadCommRates(String polUnCode, String podUnCode, String commNo, String billingType) throws Exception {
        LCLImportRatesDAO lclImportRatesDAO = new LCLImportRatesDAO();
        PortsDAO portsDAO = new PortsDAO();
        String polSchnum = portsDAO.getShedulenumber(polUnCode);
        String podSchnum = portsDAO.getShedulenumber(podUnCode);
        return lclImportRatesDAO.isRates(polSchnum, podSchnum, "1625", commNo, billingType);
    }

    public String[] createConsigneeAcctShipperAcct(String accountNo, String accountType, String loginName) throws Exception {
        String data[] = new String[2];
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        TradingPartner tradingPartner = tradingPartnerDAO.findById(accountNo);
        StringBuilder acctType = new StringBuilder();
        if (accountType.equalsIgnoreCase("C")) {
            String eciFwNo = new GeneralInformationBC().createEciConsigneeAccount();
            acctType = acctType.append(tradingPartner.getAcctType()).append(",C");
            tradingPartner.setAcctType(acctType.toString());
            tradingPartner.setECIFWNO(eciFwNo.toUpperCase());
        }
        if (accountType.equalsIgnoreCase("S")) {
            String eciAcctNo = new GeneralInformationBC().createEciAccount();
            acctType = acctType.append("S,").append(tradingPartner.getAcctType());
            tradingPartner.setAcctType(acctType.toString());
            tradingPartner.setEciAccountNo(eciAcctNo.toUpperCase());
        }
        tradingPartner.setUpdateBy(loginName);
        tradingPartner.setEnterDate(new Date());
        tradingPartnerDAO.updating(tradingPartner);
        data[0] = tradingPartner.getAccountName().toUpperCase();
        data[1] = tradingPartner.getAccountno().toUpperCase();
        return data;
    }

    public boolean checkCostStatus(String fileId) throws Exception {
        String costStatusFlag = "";
        String lclBookingAcIds = new LclCostChargeDAO().getConcatenetedBookingAcIds(Long.parseLong(fileId));
        if (CommonUtils.isNotEmpty(lclBookingAcIds)) {
            costStatusFlag = new LCLBookingAcTransDAO().getTransTypeCount(lclBookingAcIds);
        }
        return CommonUtils.isNotEmpty(costStatusFlag) && costStatusFlag.equalsIgnoreCase("true") ? true : false;
    }

    public String[] getWareHouseByTerminal(String unlocationCode) throws Exception {
        String[] data = new String[3];
        RefTerminal terminal = new RefTerminalDAO().getTerminalByUnLocation(unlocationCode, "Y");
        Warehouse wareHouse = new WarehouseDAO().getWareHouseBywarehsNo(terminal != null ? "W" + terminal.getTrmnum() : "");
        if (wareHouse != null) {
            data[0] = wareHouse.getWarehouseName();
            data[1] = wareHouse.getWarehouseNo();
            data[2] = wareHouse.getId().toString();
        }
        return data;
    }

    public String updateLclDetailStatus(String detailId, String status) throws Exception {
        LclSsDetail lclSsDetail = new LclSsDetailDAO().findById(Long.parseLong(detailId));
        if (lclSsDetail != null) {
            lclSsDetail.setDetailStatus(status);
            new LclSsDetailDAO().update(lclSsDetail);
        }
        return "";
    }

    public String isERTYorN(String coloadComm) throws Exception {
        return new LCLImportRatesDAO().isERTYorN(coloadComm);
    }
    public void getEculineAutoCost(String originId, String fdId, String cfsWareHsId, String unitTypeId, String voyageNo, String id, String cntrnNo, String screenFlag, HttpServletRequest request) throws Exception {
        List<ImportsManifestBean> autoCostList = new LclUnitsRates().findAutoCosts(Integer.parseInt(originId), Integer.parseInt(fdId), Integer.parseInt(cfsWareHsId), Long.parseLong(unitTypeId));
        request.setAttribute("autoCostList", autoCostList);
        request.setAttribute("voyNo", voyageNo);
        request.setAttribute("id", id);
        request.setAttribute("cntrnNo", cntrnNo);
        request.setAttribute("screenFlag", screenFlag);
        if (CommonUtils.isEmpty(autoCostList)) {
            request.setAttribute("noData", true);
        }
    }
//
//    public String getCommodityByAcctNo(String acctNo, String fieldName) {
//        return new GeneralInformationDAO().getCommodityByAcctNo(acctNo, fieldName);
//    }

    public boolean ertRatesChecking(String commCode, String origin, String destination) throws Exception {
        List originSchdNo = null;
        List destinationSchdNo = null;
        String orginCode = null, orginRegion = null, destinationCode = null, destinationRegion = null;
        if (CommonUtils.isNotEmpty(commCode)) {
            String databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
            LCLRatesDAO lclratesdao = new LCLRatesDAO(databaseSchema);
            List commList = lclratesdao.getOfcertCommodityList(commCode);
            if (commList != null && !commList.isEmpty()) {
                PortsDAO portsDAO = new PortsDAO();
                originSchdNo = portsDAO.getSheduleNo(origin);
                if (CommonUtils.isNotEmpty(originSchdNo)) {
                    Object[] orginRgion = (Object[]) originSchdNo.get(0);
                    if (orginRgion[0] != null && !orginRgion[0].equals("")) {
                        orginCode = orginRgion[0].toString();
                    }
                    if (orginRgion[1] != null && !orginRgion[1].equals("")) {
                        orginRegion = orginRgion[1].toString();
                    }
                }
                destinationSchdNo = portsDAO.getSheduleNo(destination);
                if (CommonUtils.isNotEmpty(destinationSchdNo)) {
                    Object[] destRegion = (Object[]) destinationSchdNo.get(0);
                    if (destRegion[0] != null && !destRegion[0].equals("")) {
                        destinationCode = destRegion[0].toString();
                    }
                    if (destRegion[1] != null && !destRegion[1].equals("")) {
                        destinationRegion = destRegion[1].toString();
                    }
                }
                List list = lclratesdao.getOfcert(orginCode, orginRegion, destinationCode, destinationRegion, commCode);
                if (list != null && !list.isEmpty()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    public boolean checkAccountTypeForEculine(String fileId, String accttype) throws Exception {
        boolean flag = false;
        if (!accttype.equalsIgnoreCase("")) {
            if (CommonUtils.isNotEmpty(new LCLBookingDAO().checkAcctNoAvailable(fileId, accttype))) {
                flag = true;
            } else {
                flag = false;
            }
        }
        return flag;
    }

    public String unitStuffingAvailable(String recordId, String moduleId, String action) throws Exception {
        ProcessInfo pInfo = new ProcessInfoDAO().getProcessInfo(recordId, moduleId, action);
        if (null != pInfo) {
            return "Cannot load at this time because " + new UserDAO().findById(pInfo.getUserid()).getLoginName() + " is currently using this process";
        } else {
            return "unlocked";
        }
    }

    public boolean getDispStatus(String headerId, String unitId) throws Exception {
        boolean dispoFlag = false;
        LclSsHeader lclssheader = null;
        Integer dispCount = 0;
        LclSsHeaderDAO lclSsHeaderDAO = new LclSsHeaderDAO();
        lclssheader = lclSsHeaderDAO.findById(Long.parseLong(headerId));
        dispCount = lclSsHeaderDAO.getDispCount(Long.parseLong(headerId), "AVAL");
        if (dispCount == 0 && (lclssheader.getClosedBy() == null && lclssheader.getAuditedBy() == null)) {
            dispoFlag = true;
        }
        return dispoFlag;
    }

    public String validateReleaseBySearch(String fileId, String fd, String pod, String hazmat, String hold, String flag, HttpServletRequest request) throws Exception {
        LCLBookingDAO bookingDAO = new LCLBookingDAO();
        String poaRestrication[] = bookingDAO.getHotCodePoa(fileId);
        return releaseValidate(fileId, pod, fd, "true".equalsIgnoreCase(poaRestrication[0]) ? "Y" : "N",
                poaRestrication[1], poaRestrication[2], "R", hazmat, hold, flag, request);
    }

    public String releaseValidate(String fileId, String fd, String pod,
            String aesByEci, String shipperAcct, String consAcct, String buttonValue, String hazmat, String hold, String flag, HttpServletRequest request) throws Exception {
        String txtMessage = "";
        int i = 1, j = 1;
        String hotCodeType = "HTC";
        String poaValues[] = null;
        String hotCodeValue[] = "INB,XXX".split(",");
        User user = (User) request.getSession().getAttribute("loginuser");
        LclBookingHotCodeDAO lclBookingHotCodeDAO = new LclBookingHotCodeDAO();
        List<Long> okg = new ArrayList<>();
        okg.add(Long.parseLong(fileId));
        String releaseText = buttonValue.equalsIgnoreCase("R") ? "Release" : "Pre-Release";
        String releaseLabel = buttonValue.equalsIgnoreCase("R") ? "Releas" : "Pre-Releas";
        if (releaseText.equalsIgnoreCase("Release")) {
            txtMessage = "<span style='color:Black'><H3 align=\"center\">Release requirements </H3></span>";
            String obkgStatus = new LclConsolidateDAO().getConsolitdateFileStatus(okg);
            if (obkgStatus != null) {
                txtMessage = "<span style='color:red'>" + txtMessage + i + "." + " Disposition should be RCVD or RUNV in order to release </span><br>";
                i += 1;
                j += 1;
            } else {
                txtMessage = "<span style='color:green'>" + txtMessage + i + "." + " Disposition should be RCVD or RUNV in order to release </span><br>";
                i += 1;
            }
        } else {
            txtMessage = "<span style='color:Black'><H3 align=\"center\">Pre-Release requirements </H3></span>";
        }
        String checkHsCode = ischeckBkgHsCode(fileId, fd, pod);
        String checkNcmCode = ischeckBkgNcmCode(fileId, fd, pod);
        LCLPortConfigurationDAO lclPortConfigurationDAO = new LCLPortConfigurationDAO();
        List HazValues = lclPortConfigurationDAO.getHazardousStatusYNR(fd, pod);
        Object[] Haz1 = (Object[]) HazValues.get(0);
        if (Haz1[0].equals("Y") || Haz1[0].equals("R")) {
            if (hazmat.equals("true")) {
                boolean hazmatval = new LclHazmatDAO().getHazStatus(fileId);
                if (hazmatval) {
                    txtMessage = "<span style='color:red'>" + txtMessage + i + "." + "Hazmat info will be required prior to " + releaseLabel + "ing this shipment </span><br>";
                    i += 1;
                    j += 1;
                } else {
                    txtMessage = "<span style='color:green'>" + txtMessage + i + "." + "Hazmat info will be required prior to " + releaseLabel + "ing this shipment </span><br>";
                    i += 1;
                }
            }
        }
        if ("Y".equalsIgnoreCase(aesByEci)) {
            if (!"".equals(shipperAcct) || !"".equals(consAcct)) {
                poaValues = new LCLBookingDAO().isValidatePoa(fileId, shipperAcct, consAcct);
            }
            boolean poaFlag = false;
            if ((null != poaValues && null != poaValues[0] && "Y".equalsIgnoreCase(poaValues[0]))
                    || (null != poaValues && null != poaValues[1] && "Y".equalsIgnoreCase(poaValues[1]))) {
                poaFlag = true;
            }
            if (!poaFlag) {
                txtMessage = "<span style='color:red'>" + txtMessage + i + "." + "Cannot " + releaseText + " due to POA restrictions </span><br>";
                i += 1;
                j += 1;
            } else {
                txtMessage = "<span style='color:green'>" + txtMessage + i + "." + "Cannot " + releaseText + " due to POA restrictions </span><br>";
                i += 1;
            }
        } else {
            txtMessage = "<span style='color:green'>" + txtMessage + i + "." + "Cannot " + releaseText + " due to POA restrictions </span><br>";
            i += 1;
        }
        boolean hotCodeINB = lclBookingHotCodeDAO.isHotCodeExistForThreeDigit(fileId, hotCodeType, hotCodeValue[0]);
        if (!hotCodeINB) {
            boolean isInbondExists = new LclInbondsDAO().isInbondExists(fileId);
            if (!isInbondExists) {
                txtMessage = "<span style='color:red'>" + txtMessage + i + "." + "Hot Code INB requires Inbond number to " + releaseText + "</span><br>";
                i += 1;
                j += 1;
            } else {
                txtMessage = "<span style='color:green'>" + txtMessage + i + "." + "Hot Code INB requires Inbond number to " + releaseText + "</span><br>";
                i += 1;
            }
        }
        boolean hotCodeXXX = lclBookingHotCodeDAO.isHotCodeExistForThreeDigit(fileId, hotCodeType, hotCodeValue[1]);
        if (!hotCodeXXX) {
            txtMessage = "<span style='color:red'>" + txtMessage + i + "." + "Dock Receipt cannot " + releaseText + "d: Hot Code XXX is active </span><br>";
            i += 1;
            j += 1;
        }
        PortsDAO portsdao = new PortsDAO();
        Ports fda = portsdao.getByProperty("unLocationCode", fd);
        if (fda.getHscode().equalsIgnoreCase("Y")) {
            if (checkHsCode != null && checkHsCode.equalsIgnoreCase("HSCODE")) {
                txtMessage = "<span style='color:red'>" + txtMessage + i + "." + "HS Code missing, cannot " + releaseText + "</span><br>";
                i += 1;
                j += 1;
            } else {
                txtMessage = "<span style='color:green'>" + txtMessage + i + "." + "HS Code missing, cannot " + releaseText + "</span><br>";
                i += 1;
            }
        }
        if (fda.getNcmno().equalsIgnoreCase("Y")) {
            if (checkNcmCode != null && checkNcmCode.equalsIgnoreCase("NCMCODE")) {
                txtMessage = "<span style='color:red'>" + txtMessage + i + "." + "NCM Number missing, cannot " + releaseText + "</span><br>";
                i += 1;
                j += 1;
            } else {
                txtMessage = "<span style='color:green'>" + txtMessage + i + "." + "NCM Number missing, cannot " + releaseText + "</span><br>";
                i += 1;
            }
        }
        Boolean releaseRoleDutyByAes = new RoleDutyDAO().getRoleDetails("aes_required_for_releasing_DRs", user.getRole().getRoleId());
        Boolean releaseRoleDutyByookedVoyage = new RoleDutyDAO().getRoleDetails("bkgvoyage_releaseDr", user.getRole().getRoleId());
        if (releaseRoleDutyByAes) {
            String isValidateAes = new Lcl3pRefNoDAO().isAesAvailable(Long.parseLong(fileId));
            if (!"true".equalsIgnoreCase(isValidateAes)) {
                txtMessage = "<span style='color:red'>" + txtMessage + i + "." + "AES required for " + releaseText + "</span><br>";
                i += 1;
                j += 1;
            } else {
                txtMessage = "<span style='color:green'>" + txtMessage + i + "." + "AES required for " + releaseText + "</span><br>";
                i += 1;
            }
        }
        if (releaseRoleDutyByookedVoyage) {
            if (flag.equalsIgnoreCase("true")) {
                txtMessage = "<span style='color:red'>" + txtMessage + i + "." + "Booked for Voyage is required to " + releaseText + "</span><br>";
                i += 1;
                j += 1;
            } else {
                txtMessage = "<span style='color:green'>" + txtMessage + i + "." + "Booked for Voyage is required to " + releaseText + "</span><br>";
                i += 1;

            }
        }
        if (!hold.equalsIgnoreCase("D")) {
            if (hold.equalsIgnoreCase("Y")) {
                txtMessage = "<span style='color:red'>" + txtMessage + i + "." + "DR is on Hold.Cannot " + releaseText + "</span><br>";
                i += 1;
                j += 1;
            } else {
                txtMessage = "<span style='color:green'>" + txtMessage + i + "." + "DR is on Hold.Cannot " + releaseText + "</span><br>";
                i += 1;
            }
        }
        if (((releaseText.equalsIgnoreCase("Release") && j != 1)) || ((!releaseText.equalsIgnoreCase("Release") && j != 1))) {
            return txtMessage;
        } else {
            return txtMessage = "";
        }
    }

    public String ischeckBkgHsCode(String fileId, String destination, String portOfDestination) throws Exception {//Release Btn Click for exports in BKG Screen
        PortsDAO portsdao = new PortsDAO();
        Ports fd = portsdao.getByProperty("unLocationCode", destination);
        Ports pod = portsdao.getByProperty("unLocationCode", portOfDestination);
        if ((fd != null && fd.getHscode() != null && fd.getHscode().equalsIgnoreCase("Y")) || (pod != null && pod.getHscode() != null && pod.getHscode().equalsIgnoreCase("Y"))) {
            String hsc = new LclBookingHsCodeDAO().isHsCodeAvailable(Long.parseLong(fileId));
            if (!"true".equalsIgnoreCase(hsc)) {
                return "HSCODE";
            }
        }
        return null;
    }

    public String ischeckBkgNcmCode(String fileId, String destination, String portOfDestination) throws Exception {//Release Btn Click for exports in BKG Screen
        PortsDAO portsdao = new PortsDAO();
        Ports fd = portsdao.getByProperty("unLocationCode", destination);
        Ports pod = portsdao.getByProperty("unLocationCode", portOfDestination);
        if ((fd != null && fd.getNcmno() != null && fd.getNcmno().equalsIgnoreCase("Y")) || (pod != null && pod.getNcmno() != null && pod.getNcmno().equalsIgnoreCase("Y"))) {
            String ncm = new Lcl3pRefNoDAO().isChecked3PRefByType(fileId, "NCM", "");
            if (!"true".equalsIgnoreCase(ncm)) {
                return "NCMCODE";
            }
        }
        return null;
    }

     public String releaseValidateConsolidateDR(String fileNumberId, String buttonValue, HttpServletRequest request) throws Exception {
        StringBuilder errorMessage = new StringBuilder("");
        String hotCodeType = "HTC";
        String poaValues[] = null;
        String hotCodeValue[] = "INB,XXX".split(",");
        User user = (User) request.getSession().getAttribute("loginuser");
        List<Long> fileList = new LclConsolidateDAO().getConsolidatesFiles(Long.parseLong(fileNumberId));
        List<Long> okg = new LclConsolidateDAO().getConsolidatesFiles(Long.parseLong(fileNumberId));
        String ConsStatus = new LclConsolidateDAO().getConsolitdateFileStatus(okg);
        boolean consolidateFlag = new LclConsolidateDAO().getConsolidateParentFileFlag(fileNumberId);
        for (Long fileId : fileList) {
            String txtMessage = "";
            try {
                LclFileNumber lclFileNumber = new LclFileNumberDAO().findById(fileId);
                LclBooking booking = lclFileNumber.getLclBooking();
                LclBookingExport bookingExport = lclFileNumber.getLclBookingExport();
                if (null != bookingExport && bookingExport.getReleasedDatetime() == null) {
                    String fd = null != booking.getFinalDestination() ? booking.getFinalDestination().getUnLocationCode() : "";
                    String pod = null != booking.getPortOfLoading() ? booking.getPortOfLoading().getUnLocationCode() : "";
                    String shipperAcct = null != booking.getShipAcct() ? booking.getShipAcct().getAccountno() : "";
                    String consAcct = null != booking.getConsAcct() ? booking.getConsAcct().getAccountno() : "";
                    String aesByEci = bookingExport.getAes() ? "Y" : "N";
                    String cfcl = bookingExport.isCfcl() ? "Y" : "N";
                    Boolean releaseRoleDutyByAes = new RoleDutyDAO().getRoleDetails("aes_required_for_releasing_DRs", user.getRole().getRoleId());
                    Boolean releaseRoleDutyByookedVoyage = new RoleDutyDAO().getRoleDetails("bkgvoyage_releaseDr", user.getRole().getRoleId());
                    boolean hazmat = new LclHazmatDAO().getHazStatus(fileId.toString());

                    LclBookingHotCodeDAO lclBookingHotCodeDAO = new LclBookingHotCodeDAO();
                    if ("R".equalsIgnoreCase(buttonValue)) {
                        if (ConsStatus != null) {
                            txtMessage = txtMessage + "<span class='red'> " + ConsStatus + "</span> Disposition should be RCVD or RUNV in order to release";
                            return txtMessage;
                        }
                    }
                    if ("Y".equalsIgnoreCase(booking.getHold())) {
                        txtMessage = txtMessage + "DR is on Hold,";
                    }
                    if (hazmat) {
                        txtMessage = txtMessage + "Missing Hazmat info,";
                    }
                    if ("Y".equalsIgnoreCase(aesByEci) && !consolidateFlag) {
                        if (!"".equals(shipperAcct) || !"".equals(consAcct)) {
                            poaValues = new LCLBookingDAO().isValidatePoa(fileId.toString(), shipperAcct, consAcct);
                        }
                        boolean poaFlag = false;
                        if ((null != poaValues && null != poaValues[0] && "Y".equalsIgnoreCase(poaValues[0]))
                                || (null != poaValues && null != poaValues[1] && "Y".equalsIgnoreCase(poaValues[1]))) {
                            poaFlag = true;
                        }
                        if (!poaFlag) {
                            txtMessage = txtMessage + "<span style='color:red'>POA</span> restrictions,";
                        }
                    }
                    if ("R".equalsIgnoreCase(buttonValue)) {
                        if (releaseRoleDutyByookedVoyage && "N".equals(cfcl) && (null == booking.getBookedSsHeaderId()
                                || (null != booking.getBookedSsHeaderId() && CommonUtils.isEmpty(booking.getBookedSsHeaderId().getScheduleNo())))) {
                            txtMessage = txtMessage + "Missing Booked for Voyage,";
                        }
                    }
                    boolean hotCodeINB = lclBookingHotCodeDAO.isHotCodeExistForThreeDigit(fileId.toString(), hotCodeType, hotCodeValue[0]);
                    if (!hotCodeINB) {
                        boolean isInbondExists = new LclInbondsDAO().isInbondExists(fileId.toString());
                        if (!isInbondExists) {
                            txtMessage = txtMessage + "Hot Code INB requires Inbond number,";
                        }
                    }
                    boolean hotCodeXXX = lclBookingHotCodeDAO.isHotCodeExistForThreeDigit(fileId.toString(), hotCodeType, hotCodeValue[1]);
                    if (!hotCodeXXX) {
                        txtMessage = txtMessage + "Hot Code <span style='color:red'>XXX</span> is active,";
                    }
                    String checkHsCode = checkHsCode(fileId.toString(), fd, pod);
                    if (checkHsCode != null && checkHsCode.equalsIgnoreCase("HSCODE")) {
                        txtMessage = txtMessage + "HS Code missing,";
                    }
                    if (checkHsCode != null && checkHsCode.equalsIgnoreCase("NCMCODE")) {
                        txtMessage = txtMessage + "NCM Number missing,";
                    }
                    if (releaseRoleDutyByAes) {
                        String isValidateAes = new Lcl3pRefNoDAO().isAesAvailable(fileId);
                        if (!"true".equalsIgnoreCase(isValidateAes)) {
                            txtMessage = txtMessage + "AES required,";
                        }
                    }
                    if (CommonUtils.isNotEmpty(txtMessage)) {
                        txtMessage = "<span class='red'>" + lclFileNumber.getFileNumber() + "</span> -" + txtMessage;
                        txtMessage = txtMessage.substring(0, txtMessage.length() - 1);
                        errorMessage.append(txtMessage).append("\n<br>");
                    }

                }
            } catch (Exception e) {
                //Do nothing
            }
        }
        return errorMessage.toString();
    }

    public String getAutoBillToCode(String fileId) throws Exception {
        LCLBlDAO lCLBlDAO = new LCLBlDAO();
        List<String> billToCode = lCLBlDAO.getBillToCode(Long.parseLong(fileId));
        String values = "";
        if (!billToCode.isEmpty()) {
            values = billToCode.toString().replace("[", "").replace("]", "");
        }
        return values;
    }

    public String checkInvoiceChargeAndCostMappingWithGLAccount(String arRedInvoiceId) throws Exception {
        LclUnitSsDAO lclUnitSsDAO = new LclUnitSsDAO();
        String chargeCode = "";
        List account = lclUnitSsDAO.getAccountForInvoiceChargeCodeFromGlAndTerminal((String) arRedInvoiceId);
        if (CommonUtils.isNotEmpty(account)) {
            for (Object obj1 : account) {
                Object[] obj = (Object[]) obj1;
                if (!lclUnitSsDAO.checkAccountExistInAccountDetailsForAccount((String) obj[0])) {
                    if (chargeCode.isEmpty()) {
                        chargeCode = (String) obj[1];
                    } else if (!chargeCode.contains((String) obj[1])) {
                        chargeCode = chargeCode + "," + (String) obj[1];
                    }
                }
            }
        }
        return chargeCode;
    }

    public String checkChargeAndCostMappingWithGL(String chargesCode, String terminalNo) throws Exception {
        LclUnitSsDAO lclUnitSsDAO = new LclUnitSsDAO();
        String chargeCode = "";
        List account = lclUnitSsDAO.getAccountForDistChargeCodeFromGlAndTerminal(chargesCode, terminalNo);
        if (CommonUtils.isNotEmpty(account)) {
            for (Object obj1 : account) {
                Object[] obj = (Object[]) obj1;
                if (!lclUnitSsDAO.checkAccountExistInAccountDetailsForAccount((String) obj[0])) {
                    if (obj[1] != null) {
                        chargeCode = (String) obj[1];
                    }
                }
            }
        }

        return chargeCode;
    }

    public String checkUnitLinkChargeAndCostMappingWithGL(String fileNumber) throws Exception {
        LclUnitSsDAO lclUnitSsDAO = new LclUnitSsDAO();
        String chargeCode = lclUnitSsDAO.getChargeCodeNotExistInGlForFileNumber(fileNumber, TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
        String unMappedChargeCode = "";
        List account = lclUnitSsDAO.getAccountForChargeCodeFromGlAndTerminal(fileNumber);
        if (CommonUtils.isNotEmpty(account)) {
            for (Object obj1 : account) {
                Object[] obj = (Object[]) obj1;
                if (!lclUnitSsDAO.checkAccountExistInAccountDetailsForAccount((String) obj[0])) {
                    if (chargeCode == null) {
                        chargeCode = (String) obj[1];
                    } else if (!chargeCode.contains((String) obj[1])) {
                        chargeCode = chargeCode + "," + (String) obj[1];
                    }
                }
            }
        }
        if (CommonUtils.isNotEmpty(chargeCode)) {
            unMappedChargeCode = unMappedChargeCode + " " + fileNumber + " -> " + chargeCode;
        }
        return unMappedChargeCode;
    }

    public void updateEconoOREculine(String bussinessUnit, String fileId, String userId, String remarkType) throws Exception {
        String remarks = "";
        String previousType = new LclFileNumberDAO().getBusinessUnit(fileId);
        if (!bussinessUnit.equalsIgnoreCase(previousType)) {
            String company = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
            company = company.equalsIgnoreCase("03") ? "Econo" : "OTI";
            remarks = !"ECU".equalsIgnoreCase(previousType) ? "Booking Changed " + company + " To Ecu WorldWide" : "Booking Changed Ecu WorldWide To " + company;
            new LclFileNumberDAO().updateEconoEculine(bussinessUnit, fileId);
            userId = !"".equalsIgnoreCase(userId) ? userId : new UserDAO().getUserInfo("system").getId().toString();
            new LclRemarksDAO().insertLclRemarks(Long.parseLong(fileId), remarkType, remarks, Integer.parseInt(userId));
        }
    }

    public String updateBusinessUnit(String fileId, String newBrand, String originAgentAcctNo, String userId, String oldBrand) throws Exception {
        String remarks = "";
        String newValue = "";
        String oldValue = "";
        if (CommonUtils.isNotEmpty(originAgentAcctNo)) {
            newBrand = new TradingPartnerDAO().getBusinessUnit(originAgentAcctNo);
        }
        if (CommonUtils.isNotEmpty(fileId)) {
            new LclFileNumberDAO().updateEconoEculine(newBrand, fileId);
            oldValue = oldBrand.equalsIgnoreCase("ECU") ? "Ecu Worldwide" : oldBrand.equalsIgnoreCase("ECI") ? "Econo" : oldBrand;
            newValue = newBrand.equalsIgnoreCase("ECU") ? "Ecu Worldwide" : newBrand.equalsIgnoreCase("ECI") ? "Econo" : newBrand;
            remarks = "(Brand Changed ->" + oldValue + " to " + newValue + ")";
            if (!oldBrand.equalsIgnoreCase(newBrand)) {
                new LclRemarksDAO().insertLclRemarks(Long.parseLong(fileId), REMARKS_TYPE_AUTO, remarks, Integer.parseInt(userId));
            }
        }
        return newBrand;
    }

    public void updateDefaultAgentFromBkg(String fileId, String agentAcctNo, String defaultRadio, String PodId, HttpServletRequest request) throws Exception {
        LclBl lclBl = new LCLBlDAO().findById(Long.parseLong(fileId));
        new LCLBlDAO().getCurrentSession().evict(lclBl);
        if (null != lclBl) {
            Date now = new Date();
            User thisUser = (User) request.getSession().getAttribute("loginuser");
            CustAddressDAO custaddress = new CustAddressDAO();
            lclBl.getLclFileNumber().getLclBooking().setAgentAcct(new TradingPartnerDAO().findById(agentAcctNo));//Freight Acct
            new LCLBookingDAO().update(lclBl.getLclFileNumber().getLclBooking());
            String[] pickupAcctNo = new AgencyInfoDAO().getAgentPickAcctNo(Integer.parseInt(PodId), agentAcctNo);
            if (CommonUtils.isNotEmpty(pickupAcctNo[0])) {
                CustAddress custAddress = custaddress.findPrimeContact(pickupAcctNo[0]);
                if (null != custAddress) {
                    LclContact agentContact = null != lclBl.getAgentContact() ? lclBl.getAgentContact() : new LclContact();
                    TradingPartner agentAcct = new TradingPartnerDAO().findById(pickupAcctNo[0]);
                    lclBl.setAgentAcct(agentAcct);
                    agentContact.setCompanyName(custAddress.getCoName());
                    agentContact.setAddress(custAddress.getAddress1());
                    agentContact.setCity(custAddress.getCity1());
                    agentContact.setState(custAddress.getState());
                    agentContact.setZip(custAddress.getZip());
                    agentContact.setPhone1(custAddress.getPhone());
                    agentContact.setEmail1(custAddress.getEmail1());
                    agentContact.setEmail2(custAddress.getEmail2());
                    agentContact.setFax1(custAddress.getFax());
                    agentContact.setEnteredDatetime(now);
                    agentContact.setModifiedDatetime(now);
                    agentContact.setModifiedBy(thisUser);
                    agentContact.setRemarks("blAgent");
                    if (null == agentContact.getLclFileNumber()) {
                        agentContact.setLclFileNumber(lclBl.getLclFileNumber());
                        agentContact.setEnteredBy(thisUser);
                        agentContact.setEnteredDatetime(now);
                    }
                    lclBl.setAgentContact(agentContact);
                    new LCLBlDAO().update(lclBl);// pickUp Acct
                }
            }
        }
    }

    public String[] fillTermstodoBlForPod(String pod, HttpServletRequest request) throws Exception {
        String[] terminalValues = new String[2];
        RefTerminalDAO refterminaldao = new RefTerminalDAO();
        RefTerminal refterminal = refterminaldao.getTerminalByUnLocation(pod, "F");
        if (refterminal != null) {
            terminalValues[0] = refterminal.getTerminalLocation();
            terminalValues[1] = refterminal.getTrmnum();
        } else {
            User user = (User) request.getSession().getAttribute("loginuser");
            if (user.getImportTerminal() != null) {
                terminalValues[0] = user.getImportTerminal().getTerminalLocation();
                terminalValues[1] = user.getImportTerminal().getTrmnum();
            }
        }
        return terminalValues;
    }

    public String checkCostMappingWithGL(String costCode, String terminalNo) throws Exception {
        LclUnitSsDAO lclUnitSsDAO = new LclUnitSsDAO();
        String chargeCode = "";
        List account = lclUnitSsDAO.getAccountForCostCodeFromGlAndTerminal(costCode, terminalNo);
        if (CommonUtils.isNotEmpty(account)) {
            for (Object obj1 : account) {
                Object[] obj = (Object[]) obj1;
                if (!lclUnitSsDAO.checkAccountExistInAccountDetailsForAccount((String) obj[0])) {
                    if (obj[1] != null) {
                        chargeCode = (String) obj[1];
                    }
                }
            }
        }

        return chargeCode;
    }
//getting autoIPI cost (Lcl imports)->charges.js methodName->getAutoIPICost

    public String[] getAutoIPICost(String fileNumberId, String impCfsId, String podUnCode,
            String fdUnCode, String transhipment, HttpServletRequest request) throws Exception {
        Long fileId = Long.parseLong(fileNumberId);
        Iterator ipiCost = null;
        String ipiCostArray[] = new String[3];
        PortsDAO portsDAO = new PortsDAO();
        User user = null;
        if (request.getSession().getAttribute("loginuser") != null) {
            user = (User) request.getSession().getAttribute("loginuser");
        }
        List<LclBookingPiece> lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", fileId);
        String podSchnum = portsDAO.getShedulenumber(podUnCode);
        String fdSchnum = portsDAO.getShedulenumber(fdUnCode);
        if (CommonUtils.isNotEqualIgnoreCase(podUnCode, fdUnCode) && null != impCfsId && !impCfsId.equals("")) {
            List<String> costeCodes = new ArrayList<String>();
            costeCodes.add("1607");
            ipiCost = new LCLImportRatesDAO().getLclImpIPICost(podSchnum, fdSchnum, impCfsId, costeCodes);
        }
        if (ipiCost != null && ipiCost.hasNext()) {
            List<LclBookingAc> calculatedIPICharge = new LCLImportChargeCalc().getNewipiCost(lclBookingPiecesList,
                    "A", null, user, ipiCost, transhipment);
            if (CommonUtils.isNotEmpty(calculatedIPICharge)) {
                LclBookingAc bookingAc = calculatedIPICharge.get(0);
                ipiCostArray[0] = bookingAc.getApglMapping().getChargeCode();
                ipiCostArray[1] = bookingAc.getApAmount().toString();
                ipiCostArray[2] = bookingAc.getApglMapping().getId().toString();
            }
        }
        return ipiCostArray;
    }

    public String checkPersonalEffects(String fileId) throws Exception {
        String result = "N";
        if (CommonUtils.isNotEmpty(fileId)) {
            List<LclBlPiece> lclBlPiecesList = new LclBLPieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileId));
            if (CommonUtils.isNotEmpty(lclBlPiecesList)) {
                for (LclBlPiece lclBlPiece : lclBlPiecesList) {
                    if (lclBlPiece.getPersonalEffects().equalsIgnoreCase("Y")) {
                        return "Y";
                    }
                }
            }
        }
        return result;
    }

    public String checkAndSavePersonalEffects(String fileId, HttpServletRequest request) throws Exception {
        String result = "N";
        if (CommonUtils.isNotEmpty(fileId)) {
            LclBlPiece lclBlPieceForSave = null;
            List<LclBlPiece> lclBlPiecesList = new LclBLPieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileId));
            if (CommonUtils.isNotEmpty(lclBlPiecesList)) {
                for (LclBlPiece lclBlPiece : lclBlPiecesList) {
                    lclBlPieceForSave = lclBlPiece;
                    if (lclBlPiece.getPersonalEffects().equalsIgnoreCase("Y")) {
                        return "Y";
                    }
                }
            }
            LCLBlDAO lclBlDAO = new LCLBlDAO();
            User user = null;
            if (request.getSession().getAttribute("loginuser") != null) {
                user = (User) request.getSession().getAttribute("loginuser");
            }
            lclBlDAO.updateBillToParty(lclBlPieceForSave.getLclFileNumber().getId(), "terms_type1", "", user.getUserId());
        }

        return result;
    }

    public void updateCommodityPersonalEffects(String fileId, HttpServletRequest request) throws Exception {
        if (CommonUtils.isNotEmpty(fileId)) {
            LclBookingPieceDAO lclBookingPieceDAO = new LclBookingPieceDAO();
            User user = null;
            if (request.getSession().getAttribute("loginuser") != null) {
                user = (User) request.getSession().getAttribute("loginuser");
            }
            lclBookingPieceDAO.updatePersonalEffects(new Long(fileId), "personal_effects", "Y", user.getUserId());
        }

    }

    public void saveTermsType(String fileId, String terms) throws Exception {
        LCLBlDAO lclbl = new LCLBlDAO();
        lclbl.setTerms(fileId, terms);

    }

    public String saveOnHoldNotes(String notes, String hold, String fileNumberId, String userId) throws Exception {
        Integer loginId = Integer.parseInt(userId);
        Long fileId = Long.parseLong(fileNumberId);
        Date now = new Date();
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        LclBooking lclBooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", fileId);
        lclBooking.setHold(hold);
        new LCLBookingDAO().saveOrUpdate(lclBooking);
        LclRemarks autoLclRemarks = new LclRemarks();
        User userfromdb = new UserDAO().findById(loginId);
        autoLclRemarks.setEnteredBy(userfromdb);
        autoLclRemarks.setModifiedBy(userfromdb);
        autoLclRemarks.setEnteredDatetime(now);
        autoLclRemarks.setModifiedDatetime(now);
        autoLclRemarks.setRemarks("Hold->" + hold + " ,Comments->" + notes);
        autoLclRemarks.setLclFileNumber(lclBooking.getLclFileNumber());
        autoLclRemarks.setType("OnHoldNotes");
        lclRemarksDAO.save(autoLclRemarks);
        return hold;
    }

    private boolean knOblTrackingSystem(String fileNumber) throws Exception {
        return new BookingEnvelopeDao().getKnOblFlag(fileNumber);
    }

    public String getKNOblNo(String fileNumberId, String fileNumber) throws Exception {
        String bkgBookingNo = "";
        if (new LCLBookingDAO().getBookingType(fileNumberId).equalsIgnoreCase("T")) {
            bkgBookingNo = "13-" + fileNumber;
        } else {
            bkgBookingNo = new RefTerminalDAO().getfileNumber(fileNumberId);
        }
        return bkgBookingNo;
    }

    public ArrayList<String> getLclEdiAndKnData(long fileNumberId, String fileNumber, HttpServletRequest request) throws Exception {
        String exportRefNo = "", marksAndNo = "", commodityDesc = "";
        EdiTrackingSystemDAO ediTrackingSystemDAO = new EdiTrackingSystemDAO();
        EdiTrackingSystem ediTrackingSystem = ediTrackingSystemDAO.findByBookingNo(fileNumber);
        List knOblTrackingSystem = new BookingEnvelopeDao().getKnOblSyatemTracking(this.getKNOblNo(String.valueOf(fileNumberId), fileNumber));
        ArrayList<String> lclEdiData = new ArrayList<>();
        if (null != ediTrackingSystem) {
            Shipment shipment = (Shipment) ediTrackingSystemDAO.findShipmentByEdiTrackingSystem(ediTrackingSystem, request);
            HashSet<PackageDetails> packSet = (HashSet<PackageDetails>) shipment.getPackageDetailsSet();
            PackageDetails packageDetails = null;
            Iterator<PackageDetails> i = packSet.iterator();
            if (i.hasNext()) {
                packageDetails = i.next();
            }
            if (null != shipment) {
                exportRefNo = null != shipment.getExportRefNo() ? shipment.getExportRefNo().toUpperCase() : "";
            }
            if (null != packageDetails) {
                marksAndNo = null != packageDetails.getMarksAndNo() ? packageDetails.getMarksAndNo().toUpperCase() : "";
                commodityDesc = null != packageDetails.getCommodity() ? packageDetails.getCommodity().toUpperCase() : "";
            }
        } else if (CommonUtils.isNotEmpty(knOblTrackingSystem)) {
            Object[] knTracking = (Object[]) knOblTrackingSystem.get(0);
            exportRefNo = (null != knTracking[0] && !knTracking[0].equals("")) ? knTracking[0].toString().toUpperCase() : "";
            marksAndNo = (null != knTracking[1] && !knTracking[1].equals("")) ? knTracking[1].toString().toUpperCase() : "";
            commodityDesc = (null != knTracking[2] && !knTracking[2].equals("")) ? knTracking[2].toString().toUpperCase() : "";
        }
        lclEdiData.add(exportRefNo);
        lclEdiData.add(marksAndNo);
        lclEdiData.add(commodityDesc);
        return lclEdiData;
    }

    public String checkChargeAndCostMappingWithGLForDistribute(String chargesCode, String terminalNo) throws Exception {
        LclUnitSsDAO lclUnitSsDAO = new LclUnitSsDAO();
        String chargeCode = "";
        List account = lclUnitSsDAO.getAccountForDistChargeCodeFromGlAndTerminal(chargesCode, terminalNo);
        if (CommonUtils.isNotEmpty(account)) {
            for (Object obj1 : account) {
                Object[] obj = (Object[]) obj1;
                chargeCode = (String) obj[1];
            }
        }

        return chargeCode;
    }

    public LclBookingCommodityModel getLclCommodity(String pieceId) throws Exception {
        Long id = null != pieceId ? Long.parseLong(pieceId) : 0;
        LclBookingPiece piece = new LclBookingPieceDAO().findById(id);
        LclBookingCommodityModel model = new LclBookingCommodityModel();
        model.setId(piece.getId());
        model.setHazmat(piece.isHazmat());
        model.setIsBarrel(piece.isIsBarrel());
        model.setMarkNoDesc(piece.getMarkNoDesc());
        model.setPieceDesc(piece.getPieceDesc());
        model.setBookedPieceCount(piece.getBookedPieceCount());
        model.setBookedWeightImperial(piece.getBookedWeightImperial());
        model.setBookedWeightMetric(piece.getBookedWeightMetric());
        model.setBookedVolumeImperial(piece.getBookedVolumeImperial());
        model.setBookedVolumeMetric(piece.getBookedVolumeMetric());
        model.setActualPieceCount(piece.getActualPieceCount());
        model.setActualWeightImperial(piece.getActualWeightImperial());
        model.setActualWeightMetric(piece.getActualWeightMetric());
        model.setActualVolumeImperial(piece.getActualVolumeImperial());
        model.setActualVolumeMetric(piece.getActualVolumeMetric());
        if (null != piece.getPackageType()) {
            model.setPackageTypeId(piece.getPackageType().getId());
            model.setDescription(piece.getPackageType().getDescription());
        }
        if (null != piece.getCommodityType()) {
            model.setCommodityId(piece.getCommodityType().getId());
            model.setCode(piece.getCommodityType().getCode());
            model.setDescEn(piece.getCommodityType().getDescEn());
        }
        return model;
    }

    public String insertLabelCountFromSearchScreen(String fileId, String fileNo,
            String labelsCount, HttpServletRequest request) throws Exception {
        User user = null;
        try {
            if (request.getSession().getAttribute("loginuser") != null) {
                user = (User) request.getSession().getAttribute("loginuser");
            }
            new LclUtils().setMailTransactionsDetails("LclBooking", "Label Print", user,
                    labelsCount, "Pending", new Date(), fileNo, Long.parseLong(fileId));
            return "Inserted Successfully ";
        } catch (Exception e) {
            return "Process Failed ";
        }
    }

    public String checkIntraForCarrier(String acctNo) throws Exception {
        String ediCode = new GeneralInformationDAO().getEdiCode(acctNo);
        return ediCode;

    }

    public String[] getHazardousStatus(String podUnlocationcode, String fdUnlocationcode) throws Exception {
        LCLPortConfigurationDAO lclPortConfigurationDAO = new LCLPortConfigurationDAO();
        List HazValues = lclPortConfigurationDAO.getHazardousStatusYNR(podUnlocationcode, fdUnlocationcode);
        Object[] Haz1 = (Object[]) HazValues.get(0);
        Object[] Haz2 = (Object[]) HazValues.get(1);
        String hazStatus[] = new String[2];
        if (Haz1[0].equals("N") || Haz2[0].equals("N")) {
            hazStatus[0] = "N";
            hazStatus[1] = Haz1[0].equals("N") ? (String) Haz1[1].toString() : (String) Haz2[1].toString();
        } else if (Haz1[0].equals("R") || Haz2[0].equals("R")) {
            hazStatus[0] = "R";
            if (Haz1[0].equals("R") && Haz2[0].equals("R")) {
                hazStatus[1] = Haz1[1].toString().equals(Haz2[1].toString()) ? (String) Haz1[1].toString() : (String) Haz1[1].toString() + "," + (String) Haz2[1].toString();
            } else {
                hazStatus[1] = Haz1[0].equals("R") ? (String) Haz1[1].toString() : (String) Haz2[1].toString();
            }
        } else {
            hazStatus[0] = "Y";
            hazStatus[1] = Haz1[0].equals("Y") ? (String) Haz1[1].toString() : (String) Haz2[1].toString();
        }
        return hazStatus;
    }

    public String fetchPOForCommodityDesc(String fileNumberId) throws Exception {
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            Lcl3pRefNoDAO lcl3pRefNoDAO = new Lcl3pRefNoDAO();
            return lcl3pRefNoDAO.getCustomerPoForCommodityDesc(fileNumberId);
        }
        return null;
    }

    public Integer fillAesItnValues(String fileId, String type) throws Exception {
        Lcl3pRefNoDAO lcl3pRefNoDAO = new Lcl3pRefNoDAO();
        Integer count = 0;
        String reference = new PropertyDAO().getProperty("AesException");
        boolean result = lcl3pRefNoDAO.isValidateAes(Long.parseLong(fileId));
        if (!result) {
            count = lcl3pRefNoDAO.insert3pRefNo(Long.parseLong(fileId), type, reference);
        }
        return count;
    }

    public String validateNoEeiAes(String fileNumberId, String status) throws Exception {
        StringBuilder messageBuilder = new StringBuilder();
        List<Long> fileList = new ArrayList<Long>();
        String message = "";
        Boolean checknoeei;
        if (status.equalsIgnoreCase("bl")) {
            fileList = new LclConsolidateDAO().getConsolidatesFiles(Long.parseLong(fileNumberId));
            fileList.add(Long.parseLong(fileNumberId));
        } else if (status.equalsIgnoreCase("booking")) {
            fileList.add(Long.parseLong(fileNumberId));
        }
        checknoeei = new Lcl3pRefNoDAO().isValidateNOEEI(fileList);
        if (checknoeei == true) {
            message = "AES EXCEPTION  <span style='color:red'>NOEEI 30.37 (A) LOW VALUE</span> Already EXISTS";
        }
        return messageBuilder.append(message).toString();
    }

    public Boolean noEeiAestoAdd(String fileNumberId, String status, HttpServletRequest request) throws Exception {
        Lcl3pRefNoDAO lcl3pRefNoDAO = new Lcl3pRefNoDAO();
        Date now = new Date();
        Lcl3pRefNo lcl3pRefNo = new Lcl3pRefNo();
        String remarks = "INSERTED-> NOEEI 30.37 (A)   LOW VALUE";
        User user = (User) request.getSession().getAttribute("loginuser");
        lcl3pRefNo.setReference("NOEEI 30.37 (A) LOW VALUE");
        lcl3pRefNo.setType("AES_EXCEPTION");
        lcl3pRefNo.setLclFileNumber(new LclFileNumber(Long.parseLong(fileNumberId)));
        lcl3pRefNo.setEnteredBy(user);
        lcl3pRefNo.setModifiedBy(user);
        lcl3pRefNo.setModifiedDatetime(now);
        lcl3pRefNo.setEnteredDatetime(now);
        lcl3pRefNoDAO.save(lcl3pRefNo);
        if (status.equalsIgnoreCase("bl")) {
            new LclRemarksDAO().insertLclRemarks(new LclFileNumber(Long.parseLong(fileNumberId)), REMARKS_BL_AUTO_NOTES, remarks, user);
        } else if (status.equalsIgnoreCase("booking")) {
            new LclRemarksDAO().insertLclRemarks(new LclFileNumber(Long.parseLong(fileNumberId)), REMARKS_DR_AUTO_NOTES, remarks, user);
        }
        return true;
    }

    public String preventtoAddNoEeiaes(String fileNumberId, String status) throws Exception {
        String message = "";
        List<Long> fileList = new ArrayList<Long>();
        if (status.equalsIgnoreCase("booking")) {
            message = new Lcl3pRefNoDAO().getAesType(Long.parseLong(fileNumberId));
        } else if (status.equalsIgnoreCase("bl")) {
            fileList = new LclConsolidateDAO().getConsolidatesFiles(Long.parseLong(fileNumberId));
            if (fileList.isEmpty()) {
                message = new Lcl3pRefNoDAO().getAesType(Long.parseLong(fileNumberId));
            }
        }
        return null != message ? message : "";
    }

    public String getUnreleasedConsolidatedDR(String fileNumberId, HttpServletRequest request) throws Exception {
        List<String> fileList = new LclConsolidateDAO().getReleasedConsolidatesFilesId(Long.parseLong(fileNumberId));
        String fileNumbers = "";
        for (String fileNumber : fileList) {
            if (CommonUtils.isNotEmpty(fileNumbers)) {
                fileNumbers = fileNumbers + "," + fileNumber;
            } else {
                fileNumbers = fileNumber;
            }
        }
        return fileNumbers;
    }

    public String getOptions(String fileId, String value) throws Exception {
        LclOptionsDAO optionsDAO = new LclOptionsDAO();
        String optionKey = optionsDAO.getOptionValue(Long.parseLong(fileId), value);
        return optionKey;
    }

    public Map<String, String> validateFilesToConsolidate(String fileId, String fileNumbers) throws Exception {
        LclBooking lclBooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", Long.parseLong(fileId));
        Integer pol5Digit = null != lclBooking.getPortOfLoading() ? lclBooking.getPortOfLoading().getId() : 0;
        Integer pod5Digit = null != lclBooking.getPortOfLoading() ? lclBooking.getPortOfDestination().getId() : 0;
        Integer fd5Digit = null != lclBooking.getPortOfLoading() ? lclBooking.getFinalDestination().getId() : 0;
        String fileNumberObj[] = fileNumbers.split(",");
        Map<String, String> resultList = new LclConsolidateDAO().validateFilesToConsolidate(Arrays.asList(fileNumberObj), pol5Digit, pod5Digit, fd5Digit);
        return resultList;
    }

    public String ignoreConsolidateFile(String errorFileNumbers, String fileNumbers) throws Exception {
        String fileNumberObj[] = fileNumbers.split(",");
        Set<String> consolidateFileNumberList = new HashSet<>();
        List consolidateOriginalList = new ArrayList<>();
        String fileNumberId = "";
        consolidateFileNumberList.addAll(Arrays.asList(fileNumberObj));
        for (String file : errorFileNumbers.split(",")) {
            consolidateFileNumberList.remove(file);
        }
        if (!consolidateFileNumberList.isEmpty()) {
            consolidateOriginalList.addAll(consolidateFileNumberList);
            fileNumberId = new LclFileNumberDAO().getFileNumberId(consolidateOriginalList);
        }
        return fileNumberId;
    }

    public String convertFileNumberId(String fileNumbers) throws Exception {
        String fileNumberObj[] = fileNumbers.split(",");
        List consolidateFileNumberList = Arrays.asList(fileNumberObj);
        String fileNumberId = new LclFileNumberDAO().getFileNumberId(consolidateFileNumberList);
        return fileNumberId;
    }

    public String getHsCode(String fileId) throws Exception {
        String countResult = "";
        Lcl3pRefNoDAO lcl3pRefNoDAO = new Lcl3pRefNoDAO();
        List hscode = lcl3pRefNoDAO.executeSQLQuery("SELECT codes FROM lcl_booking_hs_code WHERE file_number_id=" + fileId);
        if (CommonUtils.isNotEmpty(hscode)) {
            countResult = hscode.get(0).toString();
        }
        return countResult;
    }

    public void addDimsOverideRemarks(String fileId, String userId, String value) throws Exception {
        if (CommonUtils.isNotEmpty(fileId)) {
            new LclRemarksDAO().insertLclRemarks(Long.parseLong(fileId), REMARKS_DR_AUTO_NOTES, value, Integer.parseInt(userId));
        }
    }

    public String ischeckConvertBl(String fileId, String portOfDestination) throws Exception {
        PortsDAO portsdao = new PortsDAO();
        Ports fd = portsdao.getByProperty("unLocationCode", portOfDestination);
        if ((fd != null && fd.getHscode() != null && fd.getHscode().equalsIgnoreCase("Y"))) {
            return "HSCODE";
        }
        return null;
    }
    public String validateImpCostChargeStatus(String fileId, String fileNumber) throws Exception {
        TransactionDAO transactionDAO = new TransactionDAO();
        String txt = "";
            boolean validateCharge = transactionDAO.ValidateLclChargeStatus(fileNumber);
            if (validateCharge) {
                txt = "Some of the Charges are paid in Accounting.";
        }
        return txt;
    }
    public boolean validateRateStandard(String pod) throws Exception {
        if (CommonUtils.isNotEmpty(pod)){
        LCLPortConfigurationDAO lclPortConfigurationDAO = new LCLPortConfigurationDAO();
        List defaultRate = lclPortConfigurationDAO.getDefaultRateStatus(pod);
        Object[] Haz1 = (Object[]) defaultRate.get(0);
        if (Haz1[0].equals("M")) {
           return true;
        }
        }
        return false;
    }
}

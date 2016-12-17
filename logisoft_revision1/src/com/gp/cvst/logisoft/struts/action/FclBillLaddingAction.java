/*
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cvst.logisoft.struts.action;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.fcl.BookingFclBC;
import com.gp.cong.logisoft.bc.fcl.FclBlBC;
import com.gp.cong.logisoft.bc.fcl.FclBlConstants;
import com.gp.cong.logisoft.bc.fcl.FclBlUtil;
import com.gp.cong.logisoft.bc.fcl.ImportBc;
import com.gp.cong.logisoft.bc.fcl.ManifestBC;
import com.gp.cong.logisoft.bc.fcl.QuotationBC;
import com.gp.cong.logisoft.bc.fcl.QuotationConstants;
import com.gp.cong.logisoft.bc.notes.NotesBC;
import com.gp.cong.logisoft.bc.scheduler.ProcessInfoBC;
import com.gp.cong.logisoft.domain.SedFilings;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.LogFileEdiDAO;
import com.gp.cong.logisoft.hibernate.dao.NotesDAO;
import com.gp.cong.logisoft.hibernate.dao.SedFilingsDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.logisoft.util.DBUtil;
import com.gp.cong.logisoft.util.StringFormatter;
import com.gp.cvst.logisoft.beans.TransactionBean;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlCharges;
import com.gp.cvst.logisoft.domain.FclBlContainer;
import com.gp.cvst.logisoft.domain.FclBlCostCodes;
import com.gp.cvst.logisoft.domain.FileNumberForQuotaionBLBooking;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO;
import com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlChargesDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlContainerDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlCorrectionsDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlCostCodesDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.HazmatMaterialDAO;
import com.gp.cvst.logisoft.hibernate.dao.QuotationDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionLedgerDAO;
import com.gp.cvst.logisoft.struts.form.FclBillLaddingForm;
import com.logiware.accounting.dao.AccrualsDAO;
import com.logiware.accounting.model.ResultModel;
import com.logiware.action.EventAction;
import com.logiware.dwr.FclDwr;
import com.logiware.fcl.form.SessionForm;
import com.logiware.hibernate.dao.ArTransactionHistoryDAO;
import com.logiware.hibernate.domain.ArTransactionHistory;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

public class FclBillLaddingAction extends EventAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        synchronized (this) {
            FclBillLaddingForm fclBillLaddingform = (FclBillLaddingForm) form;

            super.registerEvent(form, request, response);
            HttpSession session = request.getSession(true);
            // TODO Auto-generated method stub
            String forwardName = "";
            String buttonValue = fclBillLaddingform.getButtonValue();
            String streamShipBL = fclBillLaddingform.getStreamShipBL();
            String houseBL = fclBillLaddingform.getHouseBL();
            String billingTerminal = fclBillLaddingform.getBillingTerminal();
            String oPrinting = fclBillLaddingform.getOPrinting();
            String nPrinting = fclBillLaddingform.getNPrinting();
            String blPrinting = fclBillLaddingform.getBLPrinting();
            String importsFreightRelease = fclBillLaddingform.getImportsFreightRelease();
            String originalBlRequired = fclBillLaddingform.getOriginalBlRequired();
            String fileType = fclBillLaddingform.getFileType();
            String originalBL = fclBillLaddingform.getOriginalBL();
            String hazmat = fclBillLaddingform.getHazmat();
            String destinationChargesPreCollect = fclBillLaddingform.getDestinationChargesPreCol();
            String billToCode = fclBillLaddingform.getBillToCode();
            String selectedTab = fclBillLaddingform.getSelectedTab();
            String fileNumber = fclBillLaddingform.getFileNo();
            String brand = fclBillLaddingform.getBrand();
            DBUtil dbUtil = new DBUtil();
            FclBlUtil fclBlUtil = new FclBlUtil();
            NotesBC notesBC = new NotesBC();
            DocumentStoreLogDAO documentStoreLogDAO = new DocumentStoreLogDAO();
            boolean importFlag;
            if (null != fileType && !fileType.equals("")) {
                importFlag = "I".equalsIgnoreCase(fileType);
            } else if (null != fclBillLaddingform.getBol() && !fclBillLaddingform.getBol().equals("")) {
                FclBl fcl = new FclBlDAO().findById(Integer.parseInt(fclBillLaddingform.getBol()));
                importFlag = "I".equalsIgnoreCase(fcl.getImportFlag());
            } else {
                importFlag = null != session.getAttribute(ImportBc.sessionName);
            }
            if (CommonFunctions.isNotNull(fileNumber)) {
                fileNumber = fileNumber.contains("-") ? fileNumber.substring(0, fileNumber.indexOf("-")) : fileNumber;
                String screenName = "I".equalsIgnoreCase(fileType) ? "IMPORT FILE" : "FCLFILE";
                request.setAttribute("TotalScan", documentStoreLogDAO.getNoOfFilesScannedOrAttached(fileNumber.substring(0, -1 != fileNumber.indexOf("-") ? fileNumber.indexOf("-") : fileNumber.length()), screenName, "", "Scan or Attach"));
                request.setAttribute("ManualNotes", new NotesDAO().isManualNotes(fileNumber.substring(0, -1 != fileNumber.indexOf("-") ? fileNumber.indexOf("-") : fileNumber.length())));
                request.setAttribute("MasterScan", documentStoreLogDAO.getNoOfFilesScannedOrAttached(fileNumber.substring(0, -1 != fileNumber.indexOf("-") ? fileNumber.indexOf("-") : fileNumber.length()), screenName, "SS LINE MASTER BL", "Scan or Attach"));
                request.setAttribute("MasterStatus", documentStoreLogDAO.getSsMasterStatus(fileNumber.substring(0, -1 != fileNumber.indexOf("-") ? fileNumber.indexOf("-") : fileNumber.length()), screenName, "SS LINE MASTER BL"));
                request.setAttribute("HouseBlStatus", documentStoreLogDAO.getHouseBlStatusOrOrginStatus(fileNumber.substring(0, -1 != fileNumber.indexOf("-") ? fileNumber.indexOf("-") : fileNumber.length()), screenName, "AGENT HOUSE BL"));
                request.setAttribute("OrginBlStatus", documentStoreLogDAO.getHouseBlStatusOrOrginStatus(fileNumber.substring(0, -1 != fileNumber.indexOf("-") ? fileNumber.indexOf("-") : fileNumber.length()), screenName, "ORIGIN INVOICE"));
                request.setAttribute("newBl", dbUtil.checkNewBl(fileNumber.substring(0, -1 != fileNumber.indexOf("-") ? fileNumber.indexOf("-") : fileNumber.length())));
                request.setAttribute("bulletRates", new QuotationDAO().isBulletRateByFileNo(fileNumber));
            }
            String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
            if ("02".equals(companyCode)) {
                request.setAttribute("companyCode", "OTIC");
            } else {
                request.setAttribute("companyCode", "ECCI");
            }
            if (buttonValue != null && buttonValue.equalsIgnoreCase("FEACalculation")) {
                request.setAttribute("showCostTab", "showCostTab");
            }
            String userName = "";
            User user = new User();
            if (session.getAttribute("loginuser") != null) {
                user = (User) session.getAttribute("loginuser");
                userName = user.getLoginName();
            }
            //---request from fclContainer jsp to editContainer-----
            if (null != request.getParameter("button")) {
                buttonValue = request.getParameter("button");
            }
            if (null != request.getParameter("bol")) {
                fclBillLaddingform.setBol(request.getParameter("bol"));
            } //---ends.
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            NumberFormat numberFormat = new DecimalFormat("###,###,##0.00");
            FclBlBC fclBlBC = new FclBlBC();
            TransactionDAO transactionDAO = new TransactionDAO();
            session.setAttribute("hazmat", hazmat);
            MessageResources messageResources = getResources(request);
            TransactionBean transactionBean = new TransactionBean();
            transactionBean.setStreamShipBL(streamShipBL);
            transactionBean.setHouseBL(houseBL);
            transactionBean.setBillingTerminal(billingTerminal);
            transactionBean.setDestinationChargesPreCol(destinationChargesPreCollect);
            transactionBean.setOPrinting(oPrinting);
            transactionBean.setOriginalBL(originalBL);
            transactionBean.setNPrinting(nPrinting);
            transactionBean.setBLPrinting(blPrinting);
            transactionBean.setImportsFreightRelease(importsFreightRelease);
            transactionBean.setInsurance(fclBillLaddingform.getInsurance());
            transactionBean.setBillToCode(billToCode);
            transactionBean.setOriginalBlRequired(originalBlRequired);
            transactionBean.setFileType(fileType);
            transactionBean.setSsBldestinationChargesPreCol(fclBillLaddingform.getSsBldestinationChargesPreCol());
            transactionBean.setDirectConsignment(fclBillLaddingform.getDirectConsignment());
            transactionBean.setRoutedAgentCheck(fclBillLaddingform.getRoutedAgentCheck());
            transactionBean.setBrand(fclBillLaddingform.getBrand());
            request.setAttribute(FclBlConstants.TRANSACTIONBEAN, transactionBean);
            request.setAttribute("hazmatidentity", request.getParameter("hazmatidentity"));
            session.setAttribute("companyMnemonicCode", new SystemRulesDAO().getSystemRulesByCode("CompanyNameMnemonic"));
            FclBlDAO fclblDAO = new FclBlDAO();
            // popup is getting update....
            //buttonValue.equals("getUpdatedBLInfo")getUpdatedContainerDetails
            if (buttonValue != null && buttonValue.equals("deleteBL")) {
                List fclBl = fclblDAO.findBolId(fclBillLaddingform.getBillofladding());
                if (fclBl.size() > 0) {
                    FclBl saveFclBl = (FclBl) fclBl.get(0);
                    fclblDAO.delete(saveFclBl);
                    setFileListAfterDeletingBl(session, saveFclBl);
                    fclBlBC.deleteFclBl(saveFclBl);
                    session.setAttribute("selectedFileNumber", saveFclBl.getFileNo());
                }
                session.setAttribute("screenName", "Bookings");
                forwardName = "closeSearch";
            }
            if (null != buttonValue) {
                if (buttonValue.equals("update") || buttonValue.equals("previousUpdate") || buttonValue.equals("manifest")
                        || buttonValue.equals("getUpdatedContainerDetails") || buttonValue.equals("getUpdatedBLInfo") || buttonValue.equals("deleteCostDetails") || buttonValue.equals("refreshManifestFlag")
                        || buttonValue.equals("deleteAesDetails") || "postAccrualsBeforeManifest".equalsIgnoreCase(buttonValue) || buttonValue.equals("deleteContainer")
                        || buttonValue.equals("updateContainerAndCharges")) {
                    FclBl saveFclBl = new FclBl();
                    boolean hasMasterBlChanged = false;
                    if (buttonValue.equals("deleteCostDetails")) {
                        fclBlBC.deleteCostDetails(fclBillLaddingform.getCostCodeId(), fclBillLaddingform.getBillofladding(), userName, fclBillLaddingform.getRatesNonRates(), request);
                    }
                    if (buttonValue.equals("refreshManifestFlag")) {
                        fclBlBC.updateManifestModifyFlag(fclBillLaddingform.getCostCodeId(), user);
                    }
                    if ("postAccrualsBeforeManifest".equals(buttonValue)) {
                        fclBlUtil.postAccrualsBeforeManifest(fclBillLaddingform.getBillofladding(), user);
                        request.setAttribute(FclBlConstants.REQUESTPAGE, fclBillLaddingform);
                    }
                    if (buttonValue.equals("deleteContainer")) {
                        FclBlContainerDAO fclBlContainerDAO = new FclBlContainerDAO();
                        if (CommonUtils.isNotEmpty(fclBillLaddingform.getTrailerNoId())) {
                            fclBlContainerDAO.deleteContainerDetails(Integer.parseInt(fclBillLaddingform.getTrailerNoId()));
                        }
                    }
                    List fclBlLits = fclblDAO.findBolId(fclBillLaddingform.getBillofladding());
                    List correctedfclBlList = fclblDAO.findLatestBolId(fclBillLaddingform.getBillofladding() + FclBlConstants.DELIMITER);
                    if (CommonFunctions.isNotNullOrNotEmpty(fclBlLits)) {
                        saveFclBl = (FclBl) fclBlLits.get(0);
                        saveFclBl.setUpdateBy(userName);
                        saveFclBl.setUpdatedDate(new Date());
                        if (null != fclBillLaddingform.getNewMasterBL() && !fclBillLaddingform.getNewMasterBL().equals(saveFclBl.getNewMasterBL())) {
                            hasMasterBlChanged = true;
                        }
                        if (buttonValue.equals("deleteCostDetails")) {
                            Integer costId = Integer.parseInt(fclBillLaddingform.getCostCodeId());
                            FclBlCostCodes cost = new FclBlCostCodesDAO().findById(costId);
                            for (Iterator iterator = saveFclBl.getFclblcostcodes().iterator(); iterator.hasNext();) {
                                FclBlCostCodes fclBlCostCodes = (FclBlCostCodes) iterator.next();
                                if ("OCNFRT".equalsIgnoreCase(cost.getCostCode())
                                        && "on".equalsIgnoreCase(cost.getReadOnlyFlag())
                                        && "R".equalsIgnoreCase(saveFclBl.getRatesNonRates())
                                        && CommonUtils.in(fclBlCostCodes.getCostCode(), "HAZFEE", "INTMDL", "BKRSUR", "INTFS", "INTRAMP", "NASLAN")
                                        && null == fclBlCostCodes.getBookingFlag()) {
                                    fclBlCostCodes.setDeleteFlag("yes");
                                    fclBlCostCodes.setProcessedStatus("");
                                } else if (CommonUtils.isEqual(fclBlCostCodes.getCodeId(), costId)) {
                                    fclBlCostCodes.setDeleteFlag("yes");
                                    fclBlCostCodes.setProcessedStatus("");
                                }
                            }
                        } else if ("postAccrualsBeforeManifest".equals(buttonValue)) {
                            for (Iterator iterator = saveFclBl.getFclblcostcodes().iterator(); iterator.hasNext();) {
                                FclBlCostCodes fclBlCostCodes = (FclBlCostCodes) iterator.next();
                                fclBlCostCodes.setTransactionType("AC");
                                fclBlCostCodes.setReadyToPost("M");
                                fclBlCostCodes.setManifestModifyFlag("yes");
                            }
                        }
                        fclBlBC.save(fclBillLaddingform, saveFclBl);
                        fclBlBC.updateFclBl(saveFclBl, userName, fclBillLaddingform);
                        if (buttonValue.equals("update") || buttonValue.equals("previousUpdate")) {
                            if (!"M".equalsIgnoreCase(saveFclBl.getReadyToPost()) && !"I".equalsIgnoreCase(saveFclBl.getImportFlag())) {
                                List list = new FclBlChargesDAO().findByPropertyAndBlNumber("chargeCode", "CAF", saveFclBl.getBol());
                                if ("P-Prepaid".equalsIgnoreCase(saveFclBl.getHouseBl())) {
                                    if (!list.isEmpty()) {
                                        for (Iterator it = list.iterator(); it.hasNext();) {
                                            FclBlCharges fclBlCharges = (FclBlCharges) it.next();
                                            saveFclBl.getFclcharge().remove(fclBlCharges);
                                        }
                                    }
                                } else if (list.isEmpty()) {
                                    fclBlUtil.recalculateCAF(saveFclBl.getBol(), saveFclBl.getFinalDestination());
                                }

                            }
                        }
                    } else {
                        saveFclBl.setUpdateBy(userName);
                        saveFclBl.setUpdatedDate(new Date());
                        fclBlBC.save(fclBillLaddingform, saveFclBl);
                    }
                    String realPath = this.getServlet().getServletContext().getRealPath("/");
                    fclBlBC.setRevisionFlagAndSendFreightPDF(fclBillLaddingform, request, saveFclBl, user, realPath, null);
                    //--Thie is to update corrected bl
                    if (CommonFunctions.isNotNullOrNotEmpty(correctedfclBlList)) {
                        fclBlBC.updateCorrectedBl(fclBillLaddingform, correctedfclBlList);
                    }
                    // need to cheack this code....

                    if (fclBillLaddingform.getAction() != null && fclBillLaddingform.getAction().equalsIgnoreCase("printAction")) {
                        request.setAttribute("printRequest", "true");
                    } else if (fclBillLaddingform.getAction() != null && fclBillLaddingform.getAction().equalsIgnoreCase("printBookingReport")) {
                        request.setAttribute("printBookingReport", "true");
                    } else if (fclBillLaddingform.getAction() != null && fclBillLaddingform.getAction().equalsIgnoreCase("manifest")) {
                        request.setAttribute("manifest", "true");
                        Set fclBlCostCodeSet = saveFclBl.getFclblcostcodes();
                        if ("C-Collect".equalsIgnoreCase(saveFclBl.getStreamShipBl())) {
                            if ("Yes".equalsIgnoreCase(fclBillLaddingform.getTransferCost())) {
                                for (Object o : fclBlCostCodeSet) {
                                    FclBlCostCodes costcodes = (FclBlCostCodes) o;
                                    if (!"DEFER".equals(costcodes.getCostCode()) && !"FFCOMM".equals(costcodes.getCostCode())
                                            && saveFclBl.getSslineNo().equals(costcodes.getAccNo())) {
                                        costcodes.setAccName(saveFclBl.getAgent());
                                        costcodes.setAccNo(saveFclBl.getAgentNo());
                                    }
                                }
                                notesBC.saveNotesWhileTransferCost(saveFclBl.getFileNo(), userName, "All costs in this file  transferred to " + saveFclBl.getAgent() + " from " + saveFclBl.getSslineName() + "");
                            } else {
                                notesBC.saveNotesWhileTransferCost(saveFclBl.getFileNo(), userName, "User chose NOT to transfer costs.");
                            }
                        }
                    } else if (fclBillLaddingform.getAction() != null && fclBillLaddingform.getAction().equalsIgnoreCase("confirmBoardPrintAction")) {
                        request.setAttribute("confirmBoardPrintRequest", "true");
                    } else if (fclBillLaddingform.getAction() != null && fclBillLaddingform.getAction().equalsIgnoreCase("sendEdi")) {
                        request.setAttribute("sendEdi", "true");
                    }
                    fclBlBC.SetRequestForFclChargesandCostCode(request, saveFclBl, buttonValue, messageResources, hasMasterBlChanged);
                    saveFclBl.setQuoteBy(fclBillLaddingform.getQuoteBy());

                    //---TO DISPLAY CONTACT CONFIG DETAILS FOR CODE C WITH E1 AND E2----
                    if (!"I".equalsIgnoreCase(saveFclBl.getImportFlag())) {
                        fclBlBC.getContactConfigDetailsForCodeC(saveFclBl, request);
                    }

                    Date date = null;
                    if (fclBillLaddingform.getQuoteDate() != null) {
                        date = DateUtils.parseToDateForMonthMMMandTime(fclBillLaddingform.getQuoteDate());
                        saveFclBl.setQuoteDate(date);
                    }
                    if (fclBillLaddingform.getBooking() != null) {
                        saveFclBl.setBookingNo(fclBillLaddingform.getBooking().toString());
                    }
                    if (fclBillLaddingform.getBookingBy() != null) {
                        saveFclBl.setBookingBy(fclBillLaddingform.getBookingBy());
                    }
                    Date date1 = null;
                    if (fclBillLaddingform.getBookingDate() != null) {
                        date1 = DateUtils.parseToDateForMonthMMMandTime(fclBillLaddingform.getBookingDate());
                        saveFclBl.setBookingDate(date1);
                    }
                    saveFclBl.setZip(fclBillLaddingform.getZip());
                    request.setAttribute(FclBlConstants.FCLBL, saveFclBl);

                    Quotation quotation = fclBlBC.getQuoteByFileNo(fclBlBC.getFileNumber(saveFclBl.getFileNo()));
                    if (quotation != null && quotation.getHazmat() != null && quotation.getHazmat().equalsIgnoreCase("Y")) {
                        //request.setAttribute("message", "HAZARDOUS CARGO");
                        saveFclBl.setHazmat("Y");
                    }
                    fclBlBC.overPaidStatusforImports(saveFclBl);
                    if (buttonValue.equals("deleteAesDetails")) {
                        SedFilings sedFilings = new SedFilings();
                        String trnref = fclBillLaddingform.getTrnref();
                        sedFilings = new SedFilingsDAO().findById(null != trnref ? Integer.parseInt(trnref) : 0);
                        if (null != sedFilings) {
                            new SedFilingsDAO().delete(sedFilings);
                            notesBC.deleteAesDetails(saveFclBl.getFileNo(), userName, sedFilings.getTrnref() + " AES details is deleted");
                        }
                        forwardName = "success";
                    }
                    request.setAttribute("fclBl", saveFclBl);

                    if (buttonValue.equals("update") || buttonValue.equals("previousUpdate")) {
                        setFileList(session, saveFclBl);
                        forwardName = "success";
                        if (importFlag) {
                            session.setAttribute("trade", "fclBlLaddingForImport");
                        } else {
                            session.setAttribute("trade", "fclBlLadding");
                        }
                    } else {
                        forwardName = "success";
                    }

                } else if (buttonValue.equals("reverseToBooking")) {
                    BookingFcl bookingFcl = fclBlBC.reverseToBook(fclBillLaddingform.getBillofladding());
                    if (CommonFunctions.isNotNull(bookingFcl)) {
                        ProcessInfoBC processInfoBC = new ProcessInfoBC();
                        if (CommonFunctions.isNotNull(bookingFcl.getFileNo())) {
                            setFileList(session, bookingFcl);
                            Integer userId = (user != null) ? user.getUserId() : 0;
                            processInfoBC.releaseLoack(messageResources.getMessage("lockFclBlModule"),
                                    bookingFcl.getFileNo().toString(), userId);
                            new AccrualsDAO().updateBookingBillladdingNo(null, bookingFcl.getBookingNumber());
                            session.setAttribute("selectedFileNumber", bookingFcl.getFileNo());
                        }
                    }
                    session.setAttribute("screenName", "Bookings");
                    forwardName = "closeSearch";
                } else if ("reverseToQuote".equals(buttonValue)) {
                    Quotation quotation = fclBlBC.reverseToQuote(fclBillLaddingform.getBillofladding());
                    if (CommonFunctions.isNotNull(quotation)) {
                        ProcessInfoBC processInfoBC = new ProcessInfoBC();
                        if (CommonFunctions.isNotNull(quotation.getFileNo())) {
                            fclBlUtil.setFileList(session, quotation);
                            Integer userId = (user != null) ? user.getUserId() : 0;
                            processInfoBC.releaseLoack(messageResources.getMessage("lockFclBlModule"),
                                    quotation.getFileNo().toString(), userId);
                            session.setAttribute("selectedFileNumber", quotation.getFileNo());
                        }
                    }
                    session.setAttribute("screenName", "Bookings");
                    forwardName = "closeSearch";
                } else if (buttonValue.equals("previous")) {
                    FclBl saveFclBl = new FclBl();
                    List fclBl = fclblDAO.findBolId(fclBillLaddingform.getBillofladding());
                    if (fclBl.size() > 0) {
                        saveFclBl = (FclBl) fclBl.get(0);
                        //fclBlBC.save(fclBillLaddingform, saveFclBl);
                    }
                    ProcessInfoBC processInfoBC = new ProcessInfoBC();
                    if (CommonFunctions.isNotNull(saveFclBl.getFileNo())) {
                        Integer userId = (user != null) ? user.getUserId() : 0;
                        processInfoBC.releaseLoack(messageResources.getMessage("lockFclBlModule"),
                                saveFclBl.getFileNo(), userId);
                        session.setAttribute("selectedFileNumber", saveFclBl.getFileNo());
                        SessionForm oldSearchForm = (SessionForm) session.getAttribute("oldSearchForm");
                        if (null == oldSearchForm) {
                            oldSearchForm = new SessionForm();
                            Calendar cal = Calendar.getInstance();
                            if (importFlag) {
                                oldSearchForm.setImportFile(true);
                                cal.add(Calendar.MONTH, -6);
                            } else {
                                cal.add(Calendar.MONTH, -1);
                            }
                            oldSearchForm.setFromDate(DateUtils.formatDate(cal.getTime(), "MM/dd/yyyy"));
                            oldSearchForm.setToDate(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                            oldSearchForm.setFileNumber(saveFclBl.getFileNo());
                            session.setAttribute("oldSearchForm", oldSearchForm);
                        } else if (CommonUtils.isNotEqualIgnoreEmpty(oldSearchForm.getFileNumber(), saveFclBl.getFileNo())) {
                            oldSearchForm.setFileNumber(saveFclBl.getFileNo());
                            session.setAttribute("oldSearchForm", oldSearchForm);
                        }
                    }
                    setFileList(session, saveFclBl);
                    session.setAttribute("screenName", "fileSearch");
                    forwardName = "closeSearch";
                }
                // this block wil call after calling update block
                if (buttonValue.equals("previousUpdate")) {
                    FclBl saveFclBl = new FclBl();
                    boolean hasMasterBlChanged = false;
                    List fclBl = fclblDAO.findBolId(fclBillLaddingform.getBillofladding());
                    if (fclBl.size() > 0) {
                        saveFclBl = (FclBl) fclBl.get(0);
                        if (null != fclBillLaddingform.getNewMasterBL() && !fclBillLaddingform.getNewMasterBL().equals(saveFclBl.getNewMasterBL())) {
                            hasMasterBlChanged = true;
                        }
                        fclBlBC.save(fclBillLaddingform, saveFclBl);
                        setFileList(session, saveFclBl);
                        fclBlBC.SetRequestForFclChargesandCostCode(request, saveFclBl, buttonValue, messageResources, hasMasterBlChanged);
                        ProcessInfoBC processInfoBC = new ProcessInfoBC();
                        if (CommonFunctions.isNotNull(saveFclBl.getFileNo())) {
                            Integer userId = (user != null) ? user.getUserId() : 0;
                            processInfoBC.releaseLoack(messageResources.getMessage("lockFclBlModule"),
                                    saveFclBl.getFileNo(), userId);
                            session.setAttribute("selectedFileNumber", saveFclBl.getFileNo());
                            SessionForm oldSearchForm = (SessionForm) session.getAttribute("oldSearchForm");
                            if (null == oldSearchForm) {
                                oldSearchForm = new SessionForm();
                                Calendar cal = Calendar.getInstance();
                                if (importFlag) {
                                    oldSearchForm.setImportFile(true);
                                    cal.add(Calendar.MONTH, -6);
                                } else {
                                    cal.add(Calendar.MONTH, -1);
                                }
                                oldSearchForm.setFromDate(DateUtils.formatDate(cal.getTime(), "MM/dd/yyyy"));
                                oldSearchForm.setToDate(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                                oldSearchForm.setFileNumber(saveFclBl.getFileNo());
                                session.setAttribute("oldSearchForm", oldSearchForm);
                            } else if (CommonUtils.isNotEqualIgnoreEmpty(oldSearchForm.getFileNumber(), saveFclBl.getFileNo())) {
                                oldSearchForm.setFileNumber(saveFclBl.getFileNo());
                                session.setAttribute("oldSearchForm", oldSearchForm);
                            }
                        }
                    }
                    session.setAttribute("screenName", "fileSearch");
                    forwardName = "closeSearch";
                }
                if (buttonValue.equals("manifest")) {
                    List fclBl = fclblDAO.findBolId(fclBillLaddingform.getBillofladding());
                    FclBl saveFclBl = (FclBl) fclBl.get(0);
                    if (!"M".equalsIgnoreCase(saveFclBl.getReadyToPost())) {
                        fclBlBC.save(fclBillLaddingform, saveFclBl);
                        saveFclBl.setReadyToPost("M");
                        fclBlBC.calculateDateOfYard(saveFclBl);
                        saveFclBl.setManifestedBy(userName);
                        saveFclBl.setManifestedDate(new Date());
                        fclblDAO.update(saveFclBl);
                        fclBlBC.manifest(saveFclBl, user);
                        String realPath = this.getServlet().getServletContext().getRealPath("/");
                        String manifestRev = fclBlBC.setRevisionFlagAndSendFreightPDF(fclBillLaddingform, request, saveFclBl, user, realPath, "manifest");
                        saveFclBl.setManifestRev(manifestRev);
                        setRequest(fclBillLaddingform, session, saveFclBl, request, messageResources, buttonValue);
                    }
                    forwardName = "success";
                }
                if (buttonValue.equals("copy")) {
                    List fclBl = fclblDAO.findBolId(fclBillLaddingform.getBillofladding());
                    FclBl saveFclBl = new FclBl();
                    if (fclBl.size() > 0) {
                        saveFclBl = (FclBl) fclBl.get(0);
                    }
                    ProcessInfoBC processInfoBC = new ProcessInfoBC();
                    if (CommonFunctions.isNotNull(saveFclBl.getFileNo())) {
                        Integer userId = (user != null) ? user.getUserId() : 0;
                        processInfoBC.releaseLoack(messageResources.getMessage("lockFclBlModule"),
                                saveFclBl.getFileNo(), userId);
                    }
                    //---TO SET QUOTEBY AND BOOKINGBY AND THEIR CREATION DATES----
                    FclBl copyFclBl = fclBlBC.copy(saveFclBl, fclBillLaddingform.getCopyBol());
                    copyFclBl.setQuoteBy(fclBillLaddingform.getQuoteBy());
                    Date date = null;
                    if (fclBillLaddingform.getQuoteDate() != null) {
                        date = DateUtils.parseToDateForMonthMMMandTime(fclBillLaddingform.getQuoteDate());
                        copyFclBl.setQuoteDate(date);
                    }
                    copyFclBl.setBookingBy(fclBillLaddingform.getBookingBy());
                    Date date1 = null;
                    if (fclBillLaddingform.getBookingDate() != null) {
                        date1 = DateUtils.parseToDateForMonthMMMandTime(fclBillLaddingform.getBookingDate());
                        copyFclBl.setBookingDate(date1);
                    }
                    request.setAttribute(FclBlConstants.FCLBL, copyFclBl);
                    fclBlBC.SetRequestForFclChargesandCostCode(request, copyFclBl, buttonValue, messageResources, false);
                    Integer userId = (user != null) ? user.getUserId() : 0;
                    processInfoBC.cheackFileNumberForLoack(copyFclBl.getFileNo(), userId.toString(), FclBlConstants.FCLBL);
                    session.setAttribute("check", "edit");
                    forwardName = "success";
                    //===========
                    //fclBlBC.setFlagforCostandCharges(copyFclBl, null);
                }

                //...........BUTTON ACTIONS TO PERFORM FCLBL CONTAINER'S FUNCTIONALITY............................
                if (buttonValue.equals("updateContainerWithCharges")) {
                    request.setAttribute("updateContainerWithCharges", "yes");
                } else {
                    request.setAttribute("updateContainerWithCharges", "no");
                }
                if (buttonValue != null && (buttonValue.equals("add") || buttonValue.equals("saveContainer")
                        || buttonValue.equals("popup") || buttonValue.equals("hazmat") || buttonValue.equals("addAES")
                        || buttonValue.equals("addBookingHazmat") || buttonValue.equals("getContainerData")
                        || buttonValue.equals("getUpdatedContainerDetails") || buttonValue.equals("updateContainer"))
                        || buttonValue.equals("updateContainerWithCharges") || buttonValue.equals("updateContainerAndCharges")) {

                    fclBlBC.fclBlContainerProcess(fclBillLaddingform, buttonValue, request, session, userName, messageResources);
                    if (buttonValue.trim().equals("getUpdatedContainerDetails") || buttonValue.equals("updateContainerAndCharges")) {
                        if (buttonValue.equals("updateContainerAndCharges")) {
                            new FclDwr().updateFclChargesBasedOnContainersUpdation(Integer.parseInt(fclBillLaddingform.getBol()), request);
                            FclBl fclBl = fclblDAO.findById(null != fclBillLaddingform.getBol() && !"".equals(fclBillLaddingform.getBol()) ? Integer.parseInt(fclBillLaddingform.getBol()) : 0);
                            fclBlBC.SetRequestForFclChargesandCostCode(request, fclBl, buttonValue, messageResources, false);
                        }
                        List updatedContainerList = null;
                        updatedContainerList = fclBlBC.getUpdatedContainerList(fclBillLaddingform.getBol());
                        request.setAttribute("fclBlContainerList", updatedContainerList);
                    }

                    if (buttonValue.equals("updateContainer") || buttonValue.equals("updateContainerWithCharges")) {
                        request.setAttribute(FclBlConstants.REQUESTPAGE, fclBillLaddingform);
                        forwardName = "goToAddFclBlContainerJsp";
                    } else {
                        forwardName = "success";
                    }
                    selectedTab = "container";

                }
                if (buttonValue.equals("addContainer")) {
                    fclBillLaddingform.setUserName(userName);
                    fclBlBC.addContainerDetails(fclBillLaddingform, messageResources);
                    request.setAttribute(FclBlConstants.REQUESTPAGE, fclBillLaddingform);
                    selectedTab = "container";
                    forwardName = "goToAddFclBlContainerJsp";
                }
                if (buttonValue.equals("editContainer")) {
                    fclBlBC.editContainerDetails(fclBillLaddingform, request);
                    selectedTab = "container";
                    forwardName = "goToAddFclBlContainerJsp";
                }
                if (buttonValue.equals("addContainerList")) {
                    if (null != request.getParameter("breakBulk")) {
                        String breakBulk = request.getParameter("breakBulk");
                        if ("Y".equalsIgnoreCase(breakBulk)) {
                            FclBlContainer fclBlContainer = new FclBlContainer();
                            fclBlContainer.setTrailerNo("BBLK-999999-9");
                            request.setAttribute("FclBlContainer", fclBlContainer);
                            String id = new GenericCodeDAO().getFieldByCodeAndCodetypeId("Unit Sizes", "Z", "id");
                            request.setAttribute("sizeLegend", id);
                            request.setAttribute("breakBulk", breakBulk);
                        }
                    }
                    forwardName = "goToAddFclBlContainerJsp";
                }
                if (buttonValue.equals("disableThisContainer")) {
                    String bol = request.getParameter("bolId");
                    String containerId = request.getParameter("idOfContainer");
                    String sizeOfContainer = request.getParameter("sizeOfContainer");
                    String fileNo = request.getParameter("currentFileNo");

                    //--function to deduct booking charges amount from bl charges and cost amount-----
                    fclBlBC.deductChargesAndCostAmountOnContainerDisabled(fileNo, bol, containerId, sizeOfContainer, fclBillLaddingform.getContainerComments(), user);
                    FclBl fclBl = fclblDAO.findById(null != fclBillLaddingform.getBol() && !"".equals(fclBillLaddingform.getBol()) ? Integer.parseInt(fclBillLaddingform.getBol()) : 0);
                    if (!"M".equalsIgnoreCase(fclBl.getReadyToPost()) && !"I".equalsIgnoreCase(fclBl.getImportFlag()) && !"P-Prepaid".equalsIgnoreCase(fclBl.getHouseBl())) {
                        fclBlUtil.recalculateCAF(fclBl.getBol(), fclBl.getFinalDestination());
                    }
                    //----------TO GET UPDATED BL------------
                    fclBlBC.getUpdatedBLDetails(fclBillLaddingform, request, messageResources);
                    forwardName = "success";
                }
                if ("R".equals(fclBillLaddingform.getRatesNonRates()) && !"M".equals(fclBillLaddingform.getManuallyAddedFlag()) && buttonValue.equals("deleteContainer")) {
                    String bol = request.getParameter("bolId");
                    String containerId = request.getParameter("idOfContainer");
                    String sizeOfContainer = request.getParameter("sizeOfContainer");
                    String fileNo = request.getParameter("currentFileNo");

                    //--function to delete charges from bl for particular container-----
                    fclBlBC.deleteChargesAndCostAmountOnContainer(fileNo, bol, containerId, sizeOfContainer, user);
                    //----------TO GET UPDATED BL------------
                    fclBlBC.getUpdatedBLDetails(fclBillLaddingform, request, messageResources);
                    forwardName = "success";
                }
                if (buttonValue.equals("enableThisContainer")) {
                    String bol = request.getParameter("bolId");
                    String containerId = request.getParameter("idOfContainer");
                    String sizeOfContainer = request.getParameter("sizeOfContainer");
                    String fileNo = request.getParameter("currentFileNo");

                    //--function to add booking charges amount to bl charges and cost amount-----
                    fclBlBC.addChargesAndCostAmountOnContainerEnabled(fileNo, bol, containerId, sizeOfContainer, fclBillLaddingform.getTempContainerComments(), user);
                    FclBl fclBl = fclblDAO.findById(null != fclBillLaddingform.getBol() && !"".equals(fclBillLaddingform.getBol()) ? Integer.parseInt(fclBillLaddingform.getBol()) : 0);
                    if (!"M".equalsIgnoreCase(fclBl.getReadyToPost()) && !"I".equalsIgnoreCase(fclBl.getImportFlag()) && !"P-Prepaid".equalsIgnoreCase(fclBl.getHouseBl())) {
                        fclBlUtil.recalculateCAF(fclBl.getBol(), fclBl.getFinalDestination());
                    }
                    //----------TO GET UPDATED BL------------
                    fclBlBC.getUpdatedBLDetails(fclBillLaddingform, request, messageResources);
                    forwardName = "success";
                }

                //...............BUTTON ACTIONS TO PERFORM FCLBL CHARGES FUNCTIONALITY............................
                if (buttonValue.equals("addNewCharges")) {
                    if (request.getParameter("collexpand") != null) {
                        request.setAttribute("collexpand", request.getParameter("collexpand"));
                    }
                    if (request.getParameter("fclSsblGoCollect") != null) {
                        request.setAttribute("fclSsblGoCollect", request.getParameter("fclSsblGoCollect"));
                    }
                    if (request.getParameter("noFFComm") != null) {
                        request.setAttribute("noFFComm", request.getParameter("noFFComm"));
                    }
                    forwardName = "goToAddFclBlChargesJsp";
                }
                if (buttonValue.equals("addNewCostAndCharges")) {
                    if (request.getParameter("collexpand") != null) {
                        request.setAttribute("collexpand", request.getParameter("collexpand"));
                    }
                    if (request.getParameter("fclSsblGoCollect") != null) {
                        request.setAttribute("fclSsblGoCollect", request.getParameter("fclSsblGoCollect"));
                    }
                    forwardName = "goToAddFclBlCostAndChargesJsp";
                }
                if (buttonValue.equals("chargesEdit") || "editBlCharges".equalsIgnoreCase(buttonValue)) {
                    String chargesId = request.getParameter("chargesId");
                    String rollUpAmount = request.getParameter("rollUpAmount");
                    String adjestmentAmount = request.getParameter("adjestmentAmount");
                    String manualCharge = request.getParameter("manualCharge");
                    String oldAmount = request.getParameter("oldAmount");
                    String newAmount = null;
                    if ("manualCharge".equalsIgnoreCase(manualCharge)) {
                        newAmount = rollUpAmount;
                        Double Amount = new Double((null != oldAmount && !"undefined".equalsIgnoreCase(oldAmount)) ? oldAmount.replaceAll(",", "") : "");
                        rollUpAmount = numberFormat.format(Amount).replaceAll(",", "");
                    } else if (CommonFunctions.isNotNull(adjestmentAmount) && CommonFunctions.isNotNull(rollUpAmount)) {
                        adjestmentAmount = adjestmentAmount.replace(",", "");
                        rollUpAmount = rollUpAmount.replace(",", "");
                        Double Amount = new Double(adjestmentAmount) + new Double(rollUpAmount);
                        newAmount = numberFormat.format(Amount).replaceAll(",", "");
                    } else {
                        newAmount = rollUpAmount;
                    }
                    if (null != chargesId) {
                        FclBlChargesDAO fclBlChargesDAO = new FclBlChargesDAO();
                        FclBlCharges fclBlCharges = fclBlChargesDAO.findById(Integer.parseInt(chargesId));
                        FclBl bl = new FclBlDAO().findById(fclBlCharges.getBolId());
                        if (CommonUtils.isEqualIgnoreCase(bl.getReadyToPost(), "M")) {
                            request.setAttribute("manifested", true);
                        } else {
                            request.setAttribute("manifested", false);
                        }
                        fclBillLaddingform = new FclBillLaddingForm();
                        if (null != fclBlCharges) {
                            fclBillLaddingform.setChargeCode(null != fclBlCharges.getChargeCode() ? fclBlCharges.getChargeCode() : "");
                            fclBillLaddingform.setChargeCodeDesc(null != fclBlCharges.getCharges() ? fclBlCharges.getCharges() : "");
                            fclBillLaddingform.setBillToCode(null != fclBlCharges.getBillTo() ? fclBlCharges.getBillTo() : "");
                            //fclBillLaddingform.setChargeAmount(null != fclBlCharges.getAmount() ? numberFormat.format(fclBlCharges.getAmount()) : "");
                            fclBillLaddingform.setChargeAmount(newAmount);
                            fclBillLaddingform.setOldAmount(rollUpAmount);
                            fclBillLaddingform.setRollUpAmount(rollUpAmount);
                            //fclBillLaddingform.setAdjustmentAmount(null!=fclBlCharges.getAdjustment()?numberFormat.format(fclBlCharges.getAdjustment()):"");
                            //fclBillLaddingform.setOldAmount(null!=fclBlCharges.getOldAmount()?numberFormat.format(fclBlCharges.getOldAmount()):"");
                            if ("manualCharge".equalsIgnoreCase(manualCharge)) {

                                Double Amount = new Double(CommonUtils.isNotEmpty(newAmount) ? newAmount.replace(",", "") : "0") - new Double(CommonUtils.isNotEmpty(rollUpAmount) ? rollUpAmount.replace(",", "") : "0");
                                fclBillLaddingform.setAdjustmentAmount(numberFormat.format(Amount));
                            } else {
                                fclBillLaddingform.setAdjustmentAmount(null != fclBlCharges.getAdjustment() ? numberFormat.format(fclBlCharges.getAdjustment()) : "");
                            }
                            fclBillLaddingform.setChargeBillTo(null != fclBlCharges.getBillTo() ? fclBlCharges.getBillTo() : "");
                            fclBillLaddingform.setChargePrintBl(null != fclBlCharges.getPrintOnBl() ? fclBlCharges.getPrintOnBl() : "");
                            fclBillLaddingform.setBundleIntoOfr(null != fclBlCharges.getBundleIntoOfr() ? fclBlCharges.getBundleIntoOfr() : "");
                            fclBillLaddingform.setChargeCurrency(null != fclBlCharges.getCurrencyCode() ? fclBlCharges.getCurrencyCode() : "");
                            fclBillLaddingform.setChargeId(null != fclBlCharges.getChargesId() ? fclBlCharges.getChargesId().toString() : "");
                            fclBillLaddingform.setChargesRemarks(null != fclBlCharges.getChargesRemarks() ? fclBlCharges.getChargesRemarks().toString() : "");
                            fclBillLaddingform.setChargeFlag(null != fclBlCharges.getReadOnlyFlag() ? fclBlCharges.getReadOnlyFlag() : "");
                            fclBillLaddingform.setReadyToPost(null != fclBlCharges.getReadyToPost() ? fclBlCharges.getReadyToPost() : "");
                        }
                        request.setAttribute("fclBillLaddingform", fclBillLaddingform);
                        request.setAttribute("manualCharge", manualCharge);
                        request.setAttribute("faeIncentFlag", new com.logiware.hibernate.dao.FclBlDAO().getBilltoForIncent(fclBlCharges.getBolId(), fclBlCharges.getChargeCode()));
                    }
                    if (request.getParameter("collexpand") != null) {
                        request.setAttribute("collexpand", request.getParameter("collexpand"));
                    }
                    if (request.getParameter("noFFComm") != null) {
                        request.setAttribute("noFFComm", request.getParameter("noFFComm"));
                    }
                    if ("editBlCharges".equalsIgnoreCase(buttonValue)) {
                        forwardName = "addBlCharges";
                    } else {
                        forwardName = "goToAddFclBlChargesJsp";
                    }
                }
                if (buttonValue.equals("addChargesInfo") || "addBlChargesInfo".equalsIgnoreCase(buttonValue)) {
                    fclBlBC.addChargesDetails(fclBillLaddingform, request);
                    FclBl fclBl = fclblDAO.findById(null != fclBillLaddingform.getBol() && !"".equals(fclBillLaddingform.getBol()) ? Integer.parseInt(fclBillLaddingform.getBol()) : 0);
                    fclBlBC.FaeReCalculation(fclBillLaddingform, messageResources, request);
                    if ("Y".equalsIgnoreCase(fclBl.getInsurance())) {
                        calculateInsurance(fclBillLaddingform, fclBl, request, userName);
                    }
                    if (!"M".equalsIgnoreCase(fclBl.getReadyToPost()) && !"I".equalsIgnoreCase(fclBl.getImportFlag()) && !"P-Prepaid".equalsIgnoreCase(fclBl.getHouseBl())) {
                        fclBlUtil.recalculateCAF(fclBl.getBol(), fclBl.getFinalDestination());
                    }
                    fclblDAO.lockFclBlTemp(fclBl);
                    request.setAttribute(FclBlConstants.REQUESTPAGE, fclBillLaddingform);
                    if ("addBlChargesInfo".equalsIgnoreCase(buttonValue)) {
                        forwardName = "addBlCharges";
                    } else {
                        forwardName = "goToAddFclBlChargesJsp";
                    }
                }
                if (buttonValue.equals("addCostAndChargesInfo")) {
                    FclBl fclBl = fclblDAO.findById(null != fclBillLaddingform.getBol() && !"".equals(fclBillLaddingform.getBol()) ? Integer.parseInt(fclBillLaddingform.getBol()) : 0);
                    fclBlBC.addCostAndChargesDetails(fclBillLaddingform, request, userName, fclBl);
                    if (CommonUtils.notMatches(fclBl.getFileNo(), "(\\d+)-([a-zA-Z])")) {
                        fclBlBC.FaeReCalculation(fclBillLaddingform, messageResources, request);
                    }
                    if ("Y".equalsIgnoreCase(fclBl.getInsurance())) {
                        calculateInsurance(fclBillLaddingform, fclBl, request, userName);
                    }
                    if (!"M".equalsIgnoreCase(fclBl.getReadyToPost()) && !"I".equalsIgnoreCase(fclBl.getImportFlag()) && !"P-Prepaid".equalsIgnoreCase(fclBl.getHouseBl())) {
                        fclBlUtil.recalculateCAF(fclBl.getBol(), fclBl.getFinalDestination());
                    }
                    request.setAttribute(FclBlConstants.REQUESTPAGE, fclBillLaddingform);
                    fclBlBC.overPaidStatusforImports(fclBl);
                    forwardName = "goToAddFclBlCostAndChargesJsp";
                }
                if (buttonValue.equals("updateChargesInfo") || "updateBlChargesInfo".equalsIgnoreCase(buttonValue)) {
                    FclBl fclBl = fclblDAO.findById(Integer.parseInt(fclBillLaddingform.getBol()));
                    boolean flag = false;
                    if (fclBillLaddingform.getChargeCode() != null && (fclBillLaddingform.getChargeCode().trim().equalsIgnoreCase(FclBlConstants.ADVANCEFFCODE)
                            || fclBillLaddingform.getChargeCode().trim().equalsIgnoreCase(FclBlConstants.ADVANCESHIPPERCODE))) {
                        fclBlBC.deletecodeCodes(fclBl, fclBillLaddingform.getChargeCode());
                        flag = true;
                    }
                    boolean hasUpdated = fclBlBC.updateChargesDetails(fclBillLaddingform, userName);
                    if (hasUpdated) {
                        fclBlBC.FaeReCalculation(fclBillLaddingform, messageResources, request);
                    }
                    if (flag) {
                        fclBlBC.calculatePBAADVSHPWhileManifest(fclBl, request);
                    }
                    if (!"M".equalsIgnoreCase(fclBl.getReadyToPost()) && !"I".equalsIgnoreCase(fclBl.getImportFlag()) && !"P-Prepaid".equalsIgnoreCase(fclBl.getHouseBl())) {
                        fclBlUtil.recalculateCAF(fclBl.getBol(), fclBl.getFinalDestination());
                    }
                    request.setAttribute(FclBlConstants.REQUESTPAGE, fclBillLaddingform);
                    fclBlBC.overPaidStatusforImports(fclBl);
                    fclblDAO.lockFclBlTemp(fclBl);
                    if ("updateBlChargesInfo".equalsIgnoreCase(buttonValue)) {
                        forwardName = "addBlCharges";
                    } else {
                        forwardName = "goToAddFclBlChargesJsp";
                    }
                }
                if (buttonValue.equals("deleteCharges")) {
                    String charges = fclBlBC.deleteChargesDetails(fclBillLaddingform.getChargeId(), userName, messageResources, request);
                    //----------TO GET UPDATED BL------------
                    if (CommonUtils.isNotEmpty(charges)) {
                        FclBl fclBl = fclblDAO.findById(Integer.parseInt(fclBillLaddingform.getBol()));
                        // delete advff and advsho from costcode and update pbasurcharge
                        fclBlBC.deletecodeCodes(fclBl, charges);
                    }
                    FclBl fclBl = fclblDAO.findById(null != fclBillLaddingform.getBol() && !"".equals(fclBillLaddingform.getBol()) ? Integer.parseInt(fclBillLaddingform.getBol()) : 0);
                    fclBlBC.FaeReCalculation(fclBillLaddingform, messageResources, request);
                    if ("Y".equalsIgnoreCase(fclBl.getInsurance())) {
                        calculateInsurance(fclBillLaddingform, fclBl, request, userName);
                    }
                    fclBlBC.getUpdatedBLDetails(fclBillLaddingform, request, messageResources);
                    forwardName = "success";
                }
                if ("deleteInsurance".equals(buttonValue)) {
                    List list = fclblDAO.findBolId(fclBillLaddingform.getBillofladding());
                    if (list.size() > 0) {
                        FclBl fclBl = (FclBl) list.get(0);
                        fclBl.setInsurance("N");
                        fclBl.setCostOfGoods(0d);
                        fclBl.setInsuranceRate(0d);
                        new FclBlChargesDAO().deleteInsuranceCharges(fclBl.getBol());
                    }
                    fclBlBC.getUpdatedBLDetails(fclBillLaddingform, request, messageResources);
                    forwardName = "success";
                }
                if ("calculateInsurance".equals(buttonValue)) {
                    List l = fclblDAO.findBolId(fclBillLaddingform.getBillofladding());
                    if (l.size() > 0) {
                        FclBl fclBl = (FclBl) l.get(0);
                        fclBl.setInsuranceRate(Double.parseDouble(dbUtil.removeComma(fclBillLaddingform.getInsuranceRate())));
                        fclBl.setCostOfGoods(Double.parseDouble(dbUtil.removeComma(fclBillLaddingform.getCostOfGoods())));
                        calculateInsurance(fclBillLaddingform, fclBl, request, userName);
                        fclBlBC.save(fclBillLaddingform, fclBl);
                    }
                    fclBlBC.getUpdatedBLDetails(fclBillLaddingform, request, messageResources);
                    forwardName = "success";
                }
                if (buttonValue.equals("refresh")) {
                    forwardName = "success";
                }

                //...............BUTTON ACTIONS TO PERFORM FCLBL COSTS FUNCTIONALITY............
                if (buttonValue.equals("addCostDetails") || "addBlCost".equalsIgnoreCase(buttonValue)) {
                    fclBlBC.addCostDetails(fclBillLaddingform, request, userName);
                    fclBlBC.FaeReCalculation(fclBillLaddingform, messageResources, request);
                    request.setAttribute(FclBlConstants.REQUESTPAGE, fclBillLaddingform);
                    if ("addBlCost".equalsIgnoreCase(buttonValue)) {
                        forwardName = "addBlCost";
                    } else {
                        forwardName = "goToAddFclBlCostsPopUpJsp";
                    }
                }
                if (buttonValue.equals("editCostDetails") || "editBlCost".equalsIgnoreCase(buttonValue)) {
                    String costId = request.getParameter("costId");
                    if (null != costId) {
                        FclBlCostCodesDAO fclBlCostCodesDAO = new FclBlCostCodesDAO();
                        FclBlCostCodes fclBlCostCodes = fclBlCostCodesDAO.findById(Integer.parseInt(costId));
                        fclBillLaddingform = new FclBillLaddingForm();
                        String rollpAmount = request.getParameter("rollUpAmount");
                        String masterbl = request.getParameter("masterBl");
                        String invoiceNumber = "";
                        if (null != fclBlCostCodes) {
                            invoiceNumber = fclBlCostCodes.getInvoiceNumber();
                            if ("DEFER".equalsIgnoreCase(fclBlCostCodes.getCostCode()) && CommonUtils.isEmpty(fclBlCostCodes.getInvoiceNumber())) {
                                invoiceNumber = masterbl;
                            }
                            fclBillLaddingform.setCostCode(null != fclBlCostCodes.getCostCode() ? fclBlCostCodes.getCostCode() : "");
                            fclBillLaddingform.setCostCodeDesc(null != fclBlCostCodes.getCostCodeDesc() ? fclBlCostCodes.getCostCodeDesc() : "");
                            fclBillLaddingform.setAccountName(null != fclBlCostCodes.getAccName() ? fclBlCostCodes.getAccName() : "");
                            fclBillLaddingform.setAccountNo(null != fclBlCostCodes.getAccNo() ? fclBlCostCodes.getAccNo() : "");
                            fclBillLaddingform.setInvoiceNumber(null != invoiceNumber ? invoiceNumber : "");
                            fclBillLaddingform.setCostAmount(rollpAmount);
                            fclBillLaddingform.setRollUpAmount(rollpAmount);
                            //fclBillLaddingform.setCostAmount(null!=fclBlCostCodes.getAmount()?numberFormat.format(fclBlCostCodes.getAmount()):"");
                            fclBillLaddingform.setCostCurrency(null != fclBlCostCodes.getCurrencyCode() ? fclBlCostCodes.getCurrencyCode() : "");
                            if (null != fclBlCostCodes.getDatePaid()) {
                                String date = sdf.format(fclBlCostCodes.getDatePaid());
                                fclBillLaddingform.setDatePaid(date);
                            }
                            fclBillLaddingform.setCostComments(null != fclBlCostCodes.getCostComments() ? fclBlCostCodes.getCostComments() : "");
                            fclBillLaddingform.setChequeNumber(null != fclBlCostCodes.getCheckNo() ? fclBlCostCodes.getCheckNo() : "");
                            //--setting the id----
                            fclBillLaddingform.setCostCodeId(costId);
                        }

                        request.setAttribute(FclBlConstants.FCLBL_FORM, fclBillLaddingform);
                    }
                    if ("editBlCost".equalsIgnoreCase(buttonValue)) {
                        forwardName = "addBlCost";
                    } else {
                        forwardName = "goToAddFclBlCostsPopUpJsp";
                    }
                }
                if (buttonValue.equals("updateCostDetails") || "updateBlCost".equalsIgnoreCase(buttonValue)) {
                    request.setAttribute(FclBlConstants.REQUESTPAGE, fclBillLaddingform);
                    boolean hasUpdated = fclBlBC.updateCostDetails(fclBillLaddingform, userName);
                    if (hasUpdated && !"FAECOMM".equalsIgnoreCase(fclBillLaddingform.getCostCode())) {
                        fclBlBC.FaeReCalculation(fclBillLaddingform, messageResources, request);
                    }
                    if ("updateBlCost".equalsIgnoreCase(buttonValue)) {
                        forwardName = "addBlCost";
                    } else {
                        forwardName = "goToAddFclBlCostsPopUpJsp";
                    }
                }

                //.............BL MAIN PRINT IN ITEXT 22-03-09.................
                if (buttonValue.equals("BlMainPrint")) {
                    //@TODO
                }
                if (buttonValue.equals("nonNegotiation")) {
                    //@TODO
                }
                if (buttonValue.equals("fromARTransaction")) {
                    //@TODO
                } else if (buttonValue.equals("cancelReadyToPost")) {
                    List fclBl = fclblDAO.findBolId(fclBillLaddingform.getBillofladding());
                    FclBl saveFclBl = (FclBl) fclBl.get(0);
                    if ("M".equalsIgnoreCase(saveFclBl.getReadyToPost())) {
                        saveFclBl.setBlVoid("Y");
                        saveFclBl.setVoidBy(userName);
                        saveFclBl.setVoidDate(new Date());
                        fclBlBC.unmanifest(saveFclBl, user);
                        setRequest(fclBillLaddingform, session, saveFclBl, request, messageResources, buttonValue);
                    }
                    forwardName = "success";

                } else if (buttonValue.equals("viewBLVoid")) {
                    fclBlBC.getListVoidBlCost(fclBillLaddingform.getBillofladding(), request);
                    forwardName = "vlvoidJsp";
                } else if (buttonValue.equals("FEACalculation")) {
                    fclBlBC.FaeCalculation(Integer.parseInt(fclBillLaddingform.getBol()), request);
                    List fclBl = fclblDAO.findBolId(fclBillLaddingform.getBillofladding());
                    FclBl saveFclBl = new FclBl();
                    if (fclBl.size() > 0) {
                        saveFclBl = (FclBl) fclBl.get(0);
                        //fclBlBC.save(fclBillLaddingform, saveFclBl);
                    }
                    setRequest(fclBillLaddingform, session, saveFclBl, request, messageResources, buttonValue);
                    forwardName = "success";
                } else if (buttonValue.equals("unManifest")) {
                    List fclBl = fclblDAO.findBolId(fclBillLaddingform.getBillofladding());
                    FclBl saveFclBl = (FclBl) fclBl.get(0);
                    if ("M".equalsIgnoreCase(saveFclBl.getReadyToPost())) {
                        saveFclBl.setReadyToPost(null);
                        saveFclBl.setManifestedBy(null);
                        saveFclBl.setManifestedDate(null);
                        fclblDAO.update(saveFclBl);
                        fclBlBC.unmanifest(saveFclBl, user);
                        List<FclBlCharges> list2 = (null != saveFclBl.getFclcharge()) ? new ArrayList(saveFclBl.getFclcharge()) : Collections.EMPTY_LIST;
                        fclBlBC.deleteChargeCode(FclBlConstants.PERDMCODE, list2, saveFclBl);
                        String manifestRev = fclBlBC.setRevisionFlagAndSendFreightPDF(fclBillLaddingform, null, saveFclBl, null, null, "unManifest");
                        if (manifestRev != null) {
                            fclblDAO.setManifestRev(saveFclBl.getBol(), manifestRev);
                        }
                        fclBlBC.deleteFlagDetails(fclBillLaddingform);
                        fclBlBC.reSetOldAmount(fclBillLaddingform);
                        setRequest(fclBillLaddingform, session, saveFclBl, request, messageResources, buttonValue);
                    }
                    forwardName = "success";
                } else if (null != buttonValue && buttonValue.equals("addBrandValue")) {
                    FclBl saveFclBl = new FclBl();
                    boolean hasMasterBlChanged = false;
                    List fclBl = fclblDAO.findBolId(fclBillLaddingform.getBillofladding());
                    if (fclBl.size() > 0) {
                        saveFclBl = (FclBl) fclBl.get(0);
                    }

                    saveFclBl.setUpdateBy(userName);
                    fclBlBC.save(fclBillLaddingform, saveFclBl);
                    setRequest(fclBillLaddingform, session, saveFclBl, request, messageResources, buttonValue);
                    forwardName = "success";
                }
                if (buttonValue != null && buttonValue.equalsIgnoreCase("showArTransactions")) {
                    String fileNo = request.getParameter("fileNo");
                    List<ResultModel> postedTransactions = new ArTransactionHistoryDAO().getPostedTransactions(null, fileNo, "FCL");
                    request.setAttribute("postedTransactions", postedTransactions);
                    forwardName = "showArTransactionDetails";

                }
            }
            //-- Get Aes details--//
            if (CommonUtils.isNotEmpty(fileNumber)) {
                List list = new SedFilingsDAO().findByDr(fileNumber);
                List<SedFilings> aesList = new ArrayList<SedFilings>();
                for (Object object : list) {
                    SedFilings sedFilings = (SedFilings) object;
                    sedFilings.setItnStatus(new LogFileEdiDAO().findITNStatusFileNo(sedFilings.getTrnref()));
                    aesList.add(sedFilings);
                }
                request.setAttribute("aesList", aesList);
            }
            request.setAttribute("buttonValue", buttonValue);
            //-----REQUEST FOR JQUERY TAB-----
            request.setAttribute("selectedTab", selectedTab);
            request.setAttribute("fclSsblGoCollect", fclBillLaddingform.getFclSsblGoCollect());
            request.setAttribute("importFlag", importFlag);
            return mapping.findForward(forwardName);
        }
    }

    public void setRequest(FclBillLaddingForm fclBillLaddingform, HttpSession session, FclBl saveFclBl, HttpServletRequest request, MessageResources messageResources, String buttonValue) throws Exception {
        FclBlBC fclBlBC = new FclBlBC();
        fclBlBC.SetRequestForFclChargesandCostCode(request, saveFclBl, buttonValue, messageResources, false);
        //---to set quoteby and bookingby and their creation dates----
        saveFclBl.setQuoteBy(fclBillLaddingform.getQuoteBy());
        Date date = null;
        if (fclBillLaddingform.getQuoteDate() != null) {
            date = DateUtils.parseToDateForMonthMMMandTime(fclBillLaddingform.getQuoteDate());
            saveFclBl.setQuoteDate(date);
        }
        saveFclBl.setBookingBy(fclBillLaddingform.getBookingBy());
        Date date1 = null;
        if (fclBillLaddingform.getBookingDate() != null) {
            date1 = DateUtils.parseToDateForMonthMMMandTime(fclBillLaddingform.getBookingDate());
            saveFclBl.setBookingDate(date1);
        }
        request.setAttribute(FclBlConstants.FCLBL, saveFclBl);
        Quotation quotation = fclBlBC.getQuoteByFileNo(fclBlBC.getFileNumber(saveFclBl.getFileNo()));
        if (null != quotation && quotation.getHazmat() != null && quotation.getHazmat().equalsIgnoreCase("Y")) {
            //request.setAttribute("message", "HAZARDOUS CARGO");
            saveFclBl.setHazmat("Y");
        }
    }

    public void setFileList(HttpSession session, FclBl saveFclBl) throws Exception {
        HazmatMaterialDAO hazmatMaterialDAO = new HazmatMaterialDAO();
        FclBlBC fclBlBC = new FclBlBC();
        StringFormatter stringFormatter = new StringFormatter();
        boolean falg = true;
        String status = "";
        LogFileEdiDAO logFileEdiDAO = new LogFileEdiDAO();
        if (session.getAttribute("SearchListByfileNumber") != null) {
            List getFileList = (List) session.getAttribute("SearchListByfileNumber");
            for (int i = 0; i < getFileList.size(); i++) {
                FileNumberForQuotaionBLBooking fileNumberForQuotaionBLBooking
                        = (FileNumberForQuotaionBLBooking) getFileList.get(i);
                if (fileNumberForQuotaionBLBooking.getFileNo() != null && saveFclBl != null
                        && fileNumberForQuotaionBLBooking.getFileNo().equals(saveFclBl.getFileNo())) {
                    falg = false;
                    fileNumberForQuotaionBLBooking.setDisplayColor("RED");
                    if (null != fileNumberForQuotaionBLBooking.getFclBlStatus()) {
                        status = fileNumberForQuotaionBLBooking.getFclBlStatus().replaceAll("null", "");
                    }
                    if (fileNumberForQuotaionBLBooking.getFclBlId() != null) {
                        if (saveFclBl != null) {

                            //-----COMPARING ALL THE STATUS HERE------------
                            status = stringFormatter.compareStatus(status, saveFclBl);
                            //--ends.
                            QuotationBC quotationBC = new QuotationBC();
                            if (saveFclBl.getFclcontainer() != null) {
                                Iterator iter = saveFclBl.getFclcontainer().iterator();
                                while (iter.hasNext()) {
                                    FclBlContainer fclBlConatiner = (FclBlContainer) iter.next();
                                    List hazmatList = quotationBC.getHazmatList("FclBl", fclBlConatiner.getTrailerNoId().toString());
                                    if (hazmatList.size() > 0) {
                                        fileNumberForQuotaionBLBooking.setHazmat("H");
                                        break;
                                    }
                                }
                            }
                            fileNumberForQuotaionBLBooking.setBlvoid(saveFclBl.getBlVoid());
                            if (saveFclBl.getReadyToPost() != null) {
                                fileNumberForQuotaionBLBooking.setManifest("M");
                            } else {
                                fileNumberForQuotaionBLBooking.setManifest("");
                            }
                            //---make fileicon appear red color if corrections are present---
                            if (saveFclBl.getBolId() != null) {
                                FclBlCorrectionsDAO fbcdao = new FclBlCorrectionsDAO();
                                if (null != fbcdao.getLatestUnPostedNotice(saveFclBl.getBolId(), null)) {
                                    fileNumberForQuotaionBLBooking.setCorrectionsPresent("Corrections Exist");
                                } else {
                                    fileNumberForQuotaionBLBooking.setCorrectionsPresent(null);
                                }
                                if ("I".equalsIgnoreCase(saveFclBl.getImportFlag())) {
                                    fileNumberForQuotaionBLBooking.setTrailerNo(logFileEdiDAO.getContainerNumber(saveFclBl.getBol()));
                                }
                            }
                        }
                        fileNumberForQuotaionBLBooking.setFclBlStatus(CommonFunctions.isNotNull(status) ? status.replaceAll(",,", ",") : "");
                        fileNumberForQuotaionBLBooking.setDoorOrigin(saveFclBl.getDoorOfOrigin());
                        String importRelease = "";
                        if (CommonFunctions.isNotNull(saveFclBl.getPaymentRelease())) {
                            importRelease = (CommonFunctions.isNotNull(saveFclBl.getImportRelease())) ? saveFclBl.getImportRelease() + saveFclBl.getPaymentRelease() : "N" + saveFclBl.getPaymentRelease().toString();
                        } else {
                            importRelease = (CommonFunctions.isNotNull(saveFclBl.getImportRelease())) ? saveFclBl.getImportRelease() + "N" : "NN";
                        }
                        fileNumberForQuotaionBLBooking.setImportRelease(importRelease);
                        fileNumberForQuotaionBLBooking.setBlClosed(saveFclBl.getBlClosed());
                        fileNumberForQuotaionBLBooking.setBlAudit(saveFclBl.getBlAudit());
                        Integer aesStatus = logFileEdiDAO.getSedCount(saveFclBl.getFileNo());
                        String masterstatus = logFileEdiDAO.findMasterStatusFileNo(saveFclBl.getFileNo());
                        String dockReceipt = "04" + saveFclBl.getFileNo();
                        String _304Succcess = logFileEdiDAO.findDrNumberStatus(dockReceipt, "success");
                        String _304Failure = logFileEdiDAO.findDrNumberStatus(dockReceipt, "failure");
                        fileNumberForQuotaionBLBooking.setAesCount(aesStatus);
                        fileNumberForQuotaionBLBooking.setDocumentStatus(masterstatus);
                        fileNumberForQuotaionBLBooking.set304Success(_304Succcess);
                        fileNumberForQuotaionBLBooking.set304Failure(_304Failure);
                    }
                    getFileList.set(i, fileNumberForQuotaionBLBooking);
                    break;
                }
            }
            if (falg) {
                BookingFclDAO bookingFclDAO = new BookingFclDAO();
                QuotationDAO quotationDAO = new QuotationDAO();
                BookingFclBC bookingFclBC = new BookingFclBC();
                FileNumberForQuotaionBLBooking fileNumberForQuotaionBLBooking = new FileNumberForQuotaionBLBooking(null, null, saveFclBl);
                BookingFcl bookingFcl = bookingFclDAO.getFileNoObject(fclBlBC.getFileNumber(saveFclBl.getFileNo()));
                Quotation quotation = quotationDAO.getFileNoObject(fclBlBC.getFileNumber(saveFclBl.getFileNo()));
                if (bookingFcl != null) {
                    fileNumberForQuotaionBLBooking.setBookingId(bookingFcl.getBookingId());
                    fileNumberForQuotaionBLBooking.setSsBkgNo(bookingFcl.getSSBookingNo());
                    fileNumberForQuotaionBLBooking.setBookedBy(bookingFcl.getBookedBy());
                    status = bookingFclBC.setStatus(bookingFcl);
                }
                if (quotation != null) {
                    fileNumberForQuotaionBLBooking.setQuotId(quotation.getQuoteId());
                    fileNumberForQuotaionBLBooking.setClient(quotation.getClientname());
                }
                QuotationBC quotationBC = new QuotationBC();
                if (saveFclBl.getFclcontainer() != null) {
                    Iterator iter = saveFclBl.getFclcontainer().iterator();
                    while (iter.hasNext()) {
                        FclBlContainer fclBlConatiner = (FclBlContainer) iter.next();
                        List hazmatList = quotationBC.getHazmatList("FclBl", fclBlConatiner.getTrailerNoId().toString());
                        if (hazmatList.size() > 0) {
                            fileNumberForQuotaionBLBooking.setHazmat("H");
                            break;
                        }
                    }
                }
                if (saveFclBl.getBlVoid() != null) {
                    fileNumberForQuotaionBLBooking.setBlvoid("Y");
                }
                if (saveFclBl.getReadyToPost() != null) {
                    fileNumberForQuotaionBLBooking.setManifest("M");
                } else {
                    fileNumberForQuotaionBLBooking.setManifest("");
                }
                //---make fileicon appear red color if corrections are present---
                if (saveFclBl.getBolId() != null) {
                    FclBlCorrectionsDAO fbcdao = new FclBlCorrectionsDAO();
                    if (null != fbcdao.getLatestUnPostedNotice(saveFclBl.getBolId(), null)) {
                        fileNumberForQuotaionBLBooking.setCorrectionsPresent("Corrections Exist");
                    } else {
                        fileNumberForQuotaionBLBooking.setCorrectionsPresent(null);
                    }
                }
                status = stringFormatter.compareStatus(status, saveFclBl);
                fileNumberForQuotaionBLBooking.setFclBlStatus(status);
                fileNumberForQuotaionBLBooking.setDisplayColor("RED");

                fileNumberForQuotaionBLBooking.setUser(saveFclBl.getBlBy());
                fileNumberForQuotaionBLBooking.setDoorOrigin(saveFclBl.getDoorOfOrigin());
                String importRelease = "";
                if (CommonFunctions.isNotNull(saveFclBl.getPaymentRelease())) {
                    importRelease = (CommonFunctions.isNotNull(saveFclBl.getImportRelease())) ? saveFclBl.getImportRelease() + saveFclBl.getPaymentRelease() : "N" + saveFclBl.getPaymentRelease().toString();
                } else {
                    importRelease = (CommonFunctions.isNotNull(saveFclBl.getImportRelease())) ? saveFclBl.getImportRelease() + "N" : "NN";
                }
                fileNumberForQuotaionBLBooking.setImportRelease(importRelease);
                fileNumberForQuotaionBLBooking.setBlClosed(saveFclBl.getBlClosed());
                fileNumberForQuotaionBLBooking.setBlAudit(saveFclBl.getBlAudit());
                getFileList.add(fileNumberForQuotaionBLBooking);
            }
            Collections.sort(getFileList, new FileNumberForQuotaionBLBooking());
            session.setAttribute(QuotationConstants.FILESEARCHLIST, getFileList);
        }
    }

    public void setFileList(HttpSession session, BookingFcl bookingFcl) throws Exception {
        BookingFclBC bookingFclBC = new BookingFclBC();
        if (session.getAttribute("SearchListByfileNumber") != null) {
            //QuotationDAO  quotationDAO = new QuotationDAO();
            List getFileList = (List) session.getAttribute("SearchListByfileNumber");
            for (int i = 0; i < getFileList.size(); i++) {
                FileNumberForQuotaionBLBooking fileNumberForQuotaionBLBooking
                        = (FileNumberForQuotaionBLBooking) getFileList.get(i);
                if (fileNumberForQuotaionBLBooking.getFileNo() != null && bookingFcl != null && bookingFcl.getFileNo() != null
                        && fileNumberForQuotaionBLBooking.getFileNo().equals(bookingFcl.getFileNo().toString())) {
                    //--updating the session along with all the status -------
                    bookingFclBC.updateBookingInSession(fileNumberForQuotaionBLBooking, bookingFcl);
                    fileNumberForQuotaionBLBooking.setFclBlId(null);
                    getFileList.set(i, fileNumberForQuotaionBLBooking);
                    break;
                } else {
                    fileNumberForQuotaionBLBooking.setDisplayColor(null);
                }
            }
            session.setAttribute(QuotationConstants.FILESEARCHLIST, getFileList);

        }
    }

    public void setFileListAfterDeletingBl(HttpSession session, FclBl saveFclBl) throws Exception {
        if (session.getAttribute("SearchListByfileNumber") != null) {
            List getFileList = (List) session.getAttribute("SearchListByfileNumber");
            for (int i = 0; i < getFileList.size(); i++) {
                FileNumberForQuotaionBLBooking fileNumberForQuotaionBLBooking = (FileNumberForQuotaionBLBooking) getFileList.get(i);
                if (fileNumberForQuotaionBLBooking.getFileNo() != null && saveFclBl != null && saveFclBl.getFileNo() != null
                        && fileNumberForQuotaionBLBooking.getFileNo().equals(saveFclBl.getFileNo().toString())) {
                    fileNumberForQuotaionBLBooking.setFclBlId(null);
                    getFileList.remove(fileNumberForQuotaionBLBooking);
                    break;
                }
            }
            session.setAttribute(QuotationConstants.FILESEARCHLIST, getFileList);
        }
    }

    public void redirectToReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.sendRedirect(request.getContextPath() + "/report");
    }

    public void calculateInsurance(FclBillLaddingForm fclBillLaddingForm, FclBl bl, HttpServletRequest request, String userName) throws Exception {
        Double currentSumOfCharges = 0.0;
        DecimalFormat formatter = new DecimalFormat("#0.00");
        Object totalAmount = new FclBlChargesDAO().sumOFCharges(bl.getBol());
        if (null != totalAmount && null != bl.getCostOfGoods() && null != bl.getInsuranceRate()) {
            currentSumOfCharges = (Double) totalAmount;
            Double a = bl.getCostOfGoods();
            Double insureAmt = bl.getInsuranceRate();
            Double b = currentSumOfCharges;
            Double c = ((a + b) * insureAmt) / 100;
            Double d = ((a + b + c) * 10) / 100;
            Double cif = a + b + c + d;
            Double insuranceCharge = Double.parseDouble(formatter.format((cif * insureAmt) / 100));
            List l = new FclBlChargesDAO().findByPropertyAndBlNumber("chargeCode", "INSURE", bl.getBol());
            FclBlCharges fclBlCharges = null;
            Date postedDate = new AccrualsDAO().getPostedDate("I".equalsIgnoreCase(bl.getImportFlag()) ? bl.getEta() : bl.getSailDate());
            if (!l.isEmpty()) {
                fclBlCharges = (FclBlCharges) l.get(0);
                double balanceAmount = insuranceCharge - fclBlCharges.getAmount();
                if (null != fclBlCharges.getAdjustment() && fclBlCharges.getAdjustment() != 0d) {
                    fclBlCharges.setAmount(insuranceCharge + fclBlCharges.getAdjustment());
                } else {
                    fclBlCharges.setAmount(insuranceCharge);
                }
                fclBlCharges.setOldAmount(insuranceCharge);
                if ("M".equals(bl.getReadyToPost())) {
                    TransactionLedger transactionLedger = new TransactionLedgerDAO().getArTaransactionByChargeId(fclBlCharges.getChargesId());
                    if (null != transactionLedger) {
                        transactionLedger.setTransactionAmt(insuranceCharge);
                        Transaction transaction = new TransactionDAO().findByTransactionByBillNoAndCustomer(bl.getBolId(), transactionLedger.getCustNo());
                        if (null != transaction) {
                            transaction.setTransactionAmt(transaction.getTransactionAmt() + balanceAmount);
                            transaction.setBalance(transaction.getBalance() + balanceAmount);
                            transaction.setBalanceInProcess(transaction.getBalanceInProcess() + balanceAmount);
                            if (fclBlCharges.getAmount() != 0d) {
                                ArTransactionHistory arTransactionHistory = new ArTransactionHistory();
                                arTransactionHistory.setInvoiceDate(transaction.getTransactionDate());
                                arTransactionHistory.setPostedDate(postedDate);
                                arTransactionHistory.setTransactionDate(transaction.getTransactionDate());
                                arTransactionHistory.setTransactionAmount(balanceAmount);
                                arTransactionHistory.setBlNumber(bl.getBolId());
                                arTransactionHistory.setCustomerNumber(transaction.getCustNo());
                                arTransactionHistory.setVoyageNumber(transaction.getVoyageNo());
                                arTransactionHistory.setCreatedBy(userName);
                                arTransactionHistory.setCreatedDate(new Date());
                                arTransactionHistory.setTransactionType("FCL BL");
                                new ArTransactionHistoryDAO().save(arTransactionHistory);
                            }

                        }
                    }
                }
            } else {
                fclBlCharges = new FclBlCharges();
                fclBlCharges.setBolId(bl.getBol());
                fclBlCharges.setAmount(insuranceCharge);
                fclBlCharges.setOldAmount(insuranceCharge);
                fclBlCharges.setCharges("INSURANCE");
                fclBlCharges.setChargeCode("INSURE");
                fclBlCharges.setCurrencyCode("USD");
                fclBlCharges.setPrintOnBl("Yes");
                if ("F".equalsIgnoreCase(bl.getBillToCode())) {
                    fclBlCharges.setBillTo(CommonConstants.FORWARDER);
                } else if ("S".equalsIgnoreCase(bl.getBillToCode())) {
                    fclBlCharges.setBillTo(CommonConstants.SHIPPER);
                } else if ("T".equalsIgnoreCase(bl.getBillToCode())) {
                    fclBlCharges.setBillTo(CommonConstants.THIRDPARTY);
                } else if ("C".equalsIgnoreCase(bl.getBillToCode())) {
                    fclBlCharges.setBillTo(FclBlConstants.CONSIGNEE);
                } else if ("A".equalsIgnoreCase(bl.getBillToCode())) {
                    fclBlCharges.setBillTo(CommonConstants.AGENT);
                }
                if (null != bl && "M".equals(bl.getReadyToPost())) {
                    bl.setCorrectedAfterManifest("Y");
                    fclBlCharges.setReadyToPost("M");
                    List fclBlChargesSummeryList = new ArrayList();
                    fclBlChargesSummeryList.add(fclBlCharges);
                    if ("M".equals(bl.getReadyToPost())) {
                        new ManifestBC().getTransactionObject(bl, new FclBlBC().getFclBlCostBeanobject(fclBlChargesSummeryList, null, null, null, null), request);
                        TransactionLedger transactionLedger = new TransactionLedgerDAO().getArTaransactionByChargeId(fclBlCharges.getChargesId());
                        Transaction transaction = new TransactionDAO().findByTransactionByBillNoAndCustomer(bl.getBolId(), transactionLedger.getCustNo());
                        if (null != transaction) {
                            transaction.setTransactionAmt(transaction.getTransactionAmt() + fclBlCharges.getAmount());
                            transaction.setBalance(transaction.getBalance() + fclBlCharges.getAmount());
                            transaction.setBalanceInProcess(transaction.getBalanceInProcess() + fclBlCharges.getAmount());
                            if (fclBlCharges.getAmount() != 0d) {
                                ArTransactionHistory arTransactionHistory = new ArTransactionHistory();
                                arTransactionHistory.setInvoiceDate(transaction.getTransactionDate());
                                arTransactionHistory.setPostedDate(postedDate);
                                arTransactionHistory.setTransactionDate(transaction.getTransactionDate());
                                arTransactionHistory.setTransactionAmount(fclBlCharges.getAmount());
                                arTransactionHistory.setBlNumber(bl.getBolId());
                                arTransactionHistory.setCustomerNumber(transaction.getCustNo());
                                arTransactionHistory.setVoyageNumber(transaction.getVoyageNo());
                                arTransactionHistory.setCreatedBy(userName);
                                arTransactionHistory.setCreatedDate(new Date());
                                arTransactionHistory.setTransactionType("FCL BL");
                                new ArTransactionHistoryDAO().save(arTransactionHistory);
                            }

                        }
                    }
                }
            }
            new FclBlChargesDAO().save(fclBlCharges);
        }
    }

    public void setFileList(HttpSession session, Quotation q1) throws Exception {
        QuotationBC quotationBC = new QuotationBC();
        String status = "";
        if (session.getAttribute("SearchListByfileNumber") != null && q1.getFileNo() != null) {
            List getFileList = (List) session.getAttribute("SearchListByfileNumber");
            boolean flag = true;
            for (int i = 0; i < getFileList.size(); i++) {
                FileNumberForQuotaionBLBooking fileNumberForQuotaionBLBooking
                        = (FileNumberForQuotaionBLBooking) getFileList.get(i);
                if (fileNumberForQuotaionBLBooking.getQuotId() != null && q1 != null
                        && fileNumberForQuotaionBLBooking.getQuotId().toString().equals(q1.getQuoteId().toString())) {
                    BookingFclDAO bookingFclDAO = new BookingFclDAO();
                    QuotationDAO quote = new QuotationDAO();
                    fileNumberForQuotaionBLBooking.setFileNo(q1.getFileNo().toString());
                    Quotation quo = quote.getFileNoObject(fileNumberForQuotaionBLBooking.getFileNo());
                    FileNumberForQuotaionBLBooking fileNoObject = new FileNumberForQuotaionBLBooking(quo, null, null);
                    if (null != quo && null != fileNoObject) {
                        fileNoObject.setRatesNonRates(quo.getRatesNonRates());
                        fileNoObject.setDoorOrigin(quo.getDoorOrigin());
                        if (quo.getHazmat() != null && quo.getHazmat().equalsIgnoreCase("Y")) {
                            fileNoObject.setHazmat("H");
                        }
                        if (null != fileNumberForQuotaionBLBooking.getFclBlStatus()) {
                            status = fileNumberForQuotaionBLBooking.getFclBlStatus().replaceAll("null", "");
                        }
                        status = ("P".equals(q1.getFileType())) ? ((status.indexOf("P") > -1) ? status
                                : status + "P" + ",")
                                : (status.indexOf("P") > -1) ? status.replace(",P", ",") : status;
                        status = ("N".equals(q1.getRatesNonRates())) ? ((status.indexOf("NR") > -1) ? status
                                : status + "NR" + ",")
                                : (status.indexOf("NR") > -1) ? status.replace(",NR", ",") : status;
                        BookingFcl bookingFcl = bookingFclDAO.getFileNoObject(q1.getFileNo());
                        fileNoObject.setFclBlStatus(CommonFunctions.isNotNull(status) ? status.replaceAll(",,", ",") : "");
                        if (bookingFcl != null) {
                            fileNoObject.setBookedBy(bookingFcl.getUsername());
                        }
                        FclBl fclBl = quotationBC.getfclby(q1.getFileNo());
                        if (fclBl != null) {
                            if (null != bookingFcl && null != bookingFcl.getBookingId()) {
                                fileNoObject.setFclBlId(bookingFcl.getBookingId());
                            } else {
                                fileNoObject.setFclBlId(fclBl.getBol());
                                fileNoObject.setFileNo(q1.getFileNo());
                                fileNoObject.setFclBlStatus("1U,");
                                fileNoObject.setDocReceived("Y");
                                fileNoObject.setBookingComplete("Y");
                            }
                            fileNoObject.setQuotId(null);
                            fileNoObject.setBookingId(null);
                        } else if (bookingFcl != null) {
                            fileNoObject.setBookingId(bookingFcl.getBookingId());
                            //fileNoObject.setQuotId(null);
                        }
                        fileNoObject.setDisplayColor("RED");
                        getFileList.set(i, fileNoObject);
                        flag = false;
                    }
                    break;
                }
            }
            if (flag) {
            } else {
                session.setAttribute(QuotationConstants.FILESEARCHLIST, getFileList);
            }

        } else {
            BookingFclDAO bookingFclDAO = new BookingFclDAO();
            QuotationDAO quote = new QuotationDAO();
            List getFileList = new ArrayList();
            Quotation quo = q1;
            FileNumberForQuotaionBLBooking fileNoObject = new FileNumberForQuotaionBLBooking(quo, null, null);
            fileNoObject.setRatesNonRates(quo.getRatesNonRates());
            fileNoObject.setDoorOrigin(quo.getDoorOrigin());
            if (quo.getHazmat() != null && quo.getHazmat().equalsIgnoreCase("Y")) {
                fileNoObject.setHazmat("H");
            }
            BookingFcl bookingFcl = bookingFclDAO.getFileNoObject(q1.getFileNo());
            if (bookingFcl != null) {
                fileNoObject.setBookedBy(bookingFcl.getUsername());
            }
            FclBl fclBl = quotationBC.getfclby(q1.getFileNo());
            if (fclBl != null) {
                if (null != bookingFcl.getBookingId()) {
                    fileNoObject.setFclBlId(bookingFcl.getBookingId());
                }
                fileNoObject.setQuotId(null);
                fileNoObject.setBookingId(null);
            } else if (bookingFcl != null) {
                fileNoObject.setBookingId(bookingFcl.getBookingId());
                //fileNoObject.setQuotId(null);
            }
            fileNoObject.setDisplayColor("RED");
            getFileList.add(fileNoObject);
            session.setAttribute(QuotationConstants.FILESEARCHLIST, getFileList);
        }
    }
}

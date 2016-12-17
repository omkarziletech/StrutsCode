/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import static com.gp.cong.common.ConstantsInterface.TRANSACTION_TYPE_ACCRUALS;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.ExportQuoteUtils;
import com.gp.cong.lcl.common.constant.ImportQuoteUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.dwr.LclDwr;
import com.gp.cong.lcl.dwr.LclSession;
import com.gp.cong.lcl.webservices.CallCTSWebServices;
import com.gp.cong.lcl.webservices.dom.Carrier;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclQuoteAc;
import com.gp.cong.logisoft.domain.lcl.LclQuotePad;
import com.gp.cong.logisoft.domain.lcl.LclQuotePiece;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuotePadDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuotePieceDAO;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclQuotePickupInfoForm;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclQuote;
import com.gp.cong.logisoft.domain.lcl.LclQuoteImport;
import com.gp.cong.logisoft.hibernate.dao.WarehouseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLQuoteDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteImportDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.lcl.bc.LclBookingUtils;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import java.io.PrintWriter;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author lakshh
 */
public class LclQuotePickupInfoAction extends LogiwareDispatchAction implements LclCommonConstant {

    private static final String CARRIER = "carrier";
    private static final String PICKUPCHARGE = "pickupCharge";

    public ActionForward display(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclQuotePickupInfoForm lclQuotePickupInfoForm = (LclQuotePickupInfoForm) form;
        TradingPartner accountNumber = null;
        LCLQuoteDAO lclQuoteDAO = new LCLQuoteDAO();
        String FRWD_PAGE = "";
        if (lclQuotePickupInfoForm.getLclQuotePad().getPickupInstructions() == null) {
            String pickupEnable = LoadLogisoftProperties.getProperty("application.pickupInstructionEnable");
            if (pickupEnable != null && pickupEnable.equals("Y")) {
                String pickUpInstruction = LoadLogisoftProperties.getProperty("application.lclPickupInstruction");
                if (pickUpInstruction != null && !pickUpInstruction.equals("")) {
                    lclQuotePickupInfoForm.getLclQuotePad().setPickupInstructions(pickUpInstruction.toUpperCase());
                }
            }
        }
        LclQuote lclQuote = lclQuoteDAO.findById(lclQuotePickupInfoForm.getFileNumberId());
        lclQuoteDAO.getCurrentSession().evict(lclQuote);
        LclQuotePad lclQuotePad = lclQuotePickupInfoForm.getLclQuotePad();
        if ("I".equalsIgnoreCase(lclQuote.getQuoteType())) {
            LclQuoteImport lclQuoteImport = new LclQuoteImportDAO().findById(lclQuote.getLclFileNumber().getId());
            if (lclQuoteImport != null) {
                if (lclQuoteImport.getPodSignedBy() != null) {
                    lclQuotePickupInfoForm.setPodSigned(lclQuoteImport.getPodSignedBy());
                }
                if (lclQuoteImport.getPodSignedDatetime() != null) {
                    lclQuotePickupInfoForm.setPodDate(DateUtils.formatDate(lclQuoteImport.getPodSignedDatetime(), "dd-MMM-yyyy hh:mm:ss"));
                }
                if (lclQuoteImport.getDoorDeliveryEta() != null) {
                    lclQuotePickupInfoForm.setDoorDeliveryEta(DateUtils.formatDate(lclQuoteImport.getDoorDeliveryEta(), "dd-MMM-yyyy"));
                }
                if (lclQuoteImport.getDoorDeliveryStatus() != null) {
                    lclQuotePickupInfoForm.setDoorDeliveryStatus(lclQuoteImport.getDoorDeliveryStatus());
                }
            }
            if (null != lclQuotePad.getPickupContact() && lclQuotePad.getPickupContact().getTradingPartner() != null
                    && lclQuotePad.getPickupContact().getTradingPartner().getAccountno() != null) {
                lclQuotePickupInfoForm.setCompanyName(lclQuotePad.getPickupContact().getTradingPartner().getAccountName());
                lclQuotePickupInfoForm.setAccountNo(lclQuotePad.getPickupContact().getTradingPartner().getAccountno());
            } else {
                if (null != lclQuotePad.getDeliveryContact() && lclQuotePad.getDeliveryContact().getCompanyName() != null) {
                    lclQuotePickupInfoForm.setManualCompanyName(lclQuotePad.getPickupContact().getCompanyName());
                    if (lclQuotePad.getDeliveryContact().getTradingPartner() != null) {
                        lclQuotePickupInfoForm.setWhseAccountNo(lclQuotePad.getDeliveryContact().getTradingPartner().getAccountno());
                    }
                }
            }
            FRWD_PAGE = "impQtPickUp";
        } else {
            if (null != lclQuotePad.getPickupContact() && lclQuotePad.getPickupContact().getTradingPartner() != null
                    && lclQuotePad.getPickupContact().getTradingPartner().getAccountno() != null) {
                lclQuotePickupInfoForm.setCompanyName(lclQuotePad.getPickupContact().getTradingPartner().getAccountName());
                lclQuotePickupInfoForm.setShipperAccountNo(lclQuotePad.getPickupContact().getTradingPartner().getAccountno());
                lclQuotePickupInfoForm.setNewCompanyName("off");
            } else {
                if (null != lclQuotePad.getPickupContact() && CommonUtils.isNotEmpty(lclQuotePad.getPickupContact().getCompanyName())) {
                    lclQuotePickupInfoForm.setCompanyNameDup(lclQuotePad.getPickupContact().getCompanyName());
                    lclQuotePickupInfoForm.setShipperAccountNo(null);
                    lclQuotePickupInfoForm.setNewCompanyName("on");
                }
            }
            if (lclQuotePickupInfoForm.getLclQuotePad().getPickUpTo() != null) {
                accountNumber = new TradingPartnerDAO().findAccountNumberByPassingAccountName(lclQuotePickupInfoForm.getLclQuotePad().getPickUpTo());

                if (accountNumber != null && accountNumber.getAccountno() != null) {
                    lclQuotePickupInfoForm.setToVendorName(lclQuotePickupInfoForm.getLclQuotePad().getPickUpTo());
                    lclQuotePickupInfoForm.setManualShipper("off");
                } else {
                    lclQuotePickupInfoForm.setManualCompanyName(lclQuotePickupInfoForm.getLclQuotePad().getPickUpTo());
                    lclQuotePickupInfoForm.setManualShipper("on");

                }
            }
            FRWD_PAGE = "success";
        }
        String chargeCode = "";
        String shipmentType = "";
        if (LclCommonConstant.LCL_IMPORT.equalsIgnoreCase(lclQuotePickupInfoForm.getModuleName())) {
            chargeCode = CHARGE_CODE_DOOR;
            shipmentType = LCL_SHIPMENT_TYPE_IMPORT;
        } else {
            chargeCode = CHARGE_CODE_INLAND;
            shipmentType = LCL_SHIPMENT_TYPE_EXPORT;
        }
        LclBookingUtils lclBookingUtils = new LclBookingUtils();
        String data[] = lclBookingUtils.validateForChargeandCost(chargeCode, shipmentType);
        request.setAttribute("arGlMappingFlag", data[0]);
        request.setAttribute("apGlMappingFlag", data[1]);
        LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
        LclQuoteAc lclQuoteAc = lclQuoteAcDAO.getLclQuoteAcByChargeCode(lclQuotePickupInfoForm.getFileNumberId(), chargeCode);
        if (lclQuoteAc != null) {
            lclQuotePickupInfoForm.setLclQuoteAc(lclQuoteAc);
            if (lclQuoteAc.getApAmount() != null) {
                request.setAttribute("pickupCost", lclQuoteAc.getApAmount());
            }
            if (lclQuoteAc.getArAmount() != null) {
                request.setAttribute("pickupCharge", lclQuoteAc.getArAmount());
            }
            if (lclQuoteAc.getSupAcct() != null) {
                lclQuotePickupInfoForm.setCostVendorAcct(lclQuoteAc.getSupAcct().getAccountName());
                lclQuotePickupInfoForm.setCostVendorNo(lclQuoteAc.getSupAcct().getAccountno());
            }
        }
        if (null != lclQuotePad.getPickupContact()) {
            lclQuotePickupInfoForm.setAddress(lclQuotePad.getPickupContact().getAddress());
            lclQuotePickupInfoForm.setZip(lclQuotePad.getPickupContact().getZip());
            lclQuotePickupInfoForm.setShipperState(lclQuotePad.getPickupContact().getState());
            lclQuotePickupInfoForm.setShipperCity(lclQuotePad.getPickupContact().getCity());
        }
        if (lclQuotePickupInfoForm.getLclQuotePad().getPickupContact() != null) {
            request.setAttribute("fax1", lclQuotePickupInfoForm.getLclQuotePad().getPickupContact().getFax1());
            request.setAttribute("phone1", lclQuotePickupInfoForm.getLclQuotePad().getPickupContact().getPhone1());
            request.setAttribute("contactName", lclQuotePickupInfoForm.getLclQuotePad().getPickupContact().getContactName());
            request.setAttribute("email1", lclQuotePickupInfoForm.getLclQuotePad().getPickupContact().getEmail1());
        }
        request.setAttribute("pickupHours", lclQuotePickupInfoForm.getLclQuotePad().getPickupHours());
        request.setAttribute("scac", lclQuotePickupInfoForm.getLclQuotePad().getScac());
        request.setAttribute("pickupReadyNote", lclQuotePickupInfoForm.getLclQuotePad().getPickupReadyNote());
        //get Values and Set into delivery field
        if (lclQuotePad.getDeliveryContact() != null && lclQuotePad.getDeliveryContact().getId() != null) {
            lclQuotePickupInfoForm.setWhsecompanyName(lclQuotePad.getDeliveryContact().getCompanyName());
            lclQuotePickupInfoForm.setManualWhseName(lclQuotePad.getDeliveryContact().getCompanyName());
            lclQuotePickupInfoForm.setWhseAddress(lclQuotePad.getDeliveryContact().getAddress());
            lclQuotePickupInfoForm.setWhseCity(lclQuotePad.getDeliveryContact().getCity());
            lclQuotePickupInfoForm.setWhseState(lclQuotePad.getDeliveryContact().getState());
            lclQuotePickupInfoForm.setWhseZip(lclQuotePad.getDeliveryContact().getZip());
            lclQuotePickupInfoForm.setWhsePhone(lclQuotePad.getDeliveryContact().getPhone1());
            if (lclQuotePad.getDeliveryContact().getWarehouse() != null) {
                lclQuotePickupInfoForm.setWhsePhone(String.valueOf(lclQuotePad.getDeliveryContact().getWarehouse().getId()));
            }
        }

        if (CommonUtils.isEmpty(lclQuotePickupInfoForm.getCommodityDesc()) && null != lclQuote && null != lclQuote.getLclFileNumber() && !lclQuote.getLclFileNumber().getLclQuotePieceList().isEmpty() && CommonUtils.isNotEmpty(lclQuote.getLclFileNumber().getLclQuotePieceList().get(0).getPieceDesc())) {
            lclQuotePickupInfoForm.setCommodityDesc(lclQuote.getLclFileNumber().getLclQuotePieceList().get(0).getPieceDesc());
        }
        if (lclQuote != null) {
            lclQuotePickupInfoForm.setTrmnum(lclQuote.getEnteredBy().getTerminal().getTrmnum());
            lclQuotePickupInfoForm.setIssuingTerminal(lclQuote.getEnteredBy().getLoginName().toUpperCase() + "/" + lclQuote.getEnteredBy().getTerminal().getTrmnum());
        } else {
            User loginUser = getCurrentUser(request);
            lclQuotePickupInfoForm.setTrmnum(loginUser.getTerminal().getTrmnum());
            lclQuotePickupInfoForm.setIssuingTerminal(loginUser.getLoginName().toUpperCase() + "/" + loginUser.getTerminal().getTrmnum());
        }
        request.setAttribute("lclQuotePickupInfoForm", lclQuotePickupInfoForm);
        return mapping.findForward(FRWD_PAGE);
    }

    public ActionForward save(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclQuotePickupInfoForm pickUpForm = (LclQuotePickupInfoForm) form;
        User loginUser = getCurrentUser(request);
        Date now = new Date();
        LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
        LclQuotePadDAO lclQuotePadDAO = new LclQuotePadDAO();
        LCLQuoteDAO quoteDAO = new LCLQuoteDAO();
        LclQuote lclQuote = quoteDAO.getByProperty("lclFileNumber.id", pickUpForm.getFileNumberId());
        lclQuote.setPooDoor(true);
        lclQuote.setModifiedBy(loginUser);
        lclQuote.setModifiedDatetime(now);
        quoteDAO.getCurrentSession().clear();
        quoteDAO.saveOrUpdate(lclQuote);
        String destination = lclQuote.getFinalDestination().getUnLocationName();
        String origin = "";
        if (CommonFunctions.isNotNull(lclQuote.getPortOfOrigin()) && !"I".equalsIgnoreCase(lclQuote.getQuoteType())) {
            origin = lclQuote.getPortOfOrigin().getUnLocationName();
        } else if (CommonFunctions.isNotNull(lclQuote.getPortOfLoading())) {
            origin = lclQuote.getPortOfLoading().getUnLocationName();
        }
        LclQuotePad lclQuotePad = pickUpForm.getLclQuotePad();
        BigDecimal pickCost = CommonUtils.isNotEmpty(pickUpForm.getPickupCost()) ? new BigDecimal(pickUpForm.getPickupCost()) : new BigDecimal(0.00);
        BigDecimal pickupCharge = CommonUtils.isNotEmpty(pickUpForm.getChargeAmount()) ? new BigDecimal(pickUpForm.getChargeAmount()) : new BigDecimal(0.00);
        if ((pickCost.doubleValue() != 0 || pickupCharge.doubleValue() != 0)) {
            LclQuoteAc lclQuoteAc = setPickUpCharge(pickUpForm, lclQuote,
                    pickupCharge, pickCost, loginUser, pickUpForm.getCostVendorNo());
            pickUpForm.getLclQuotePad().setLclQuoteAc(lclQuoteAc);
            pickUpForm.getLclQuotePad().getLclQuoteAc().setApAmount(pickCost);
            pickUpForm.getLclQuotePad().getLclQuoteAc().setArAmount(pickupCharge);
        }
        lclQuotePad.setLclFileNumber(lclQuote.getLclFileNumber());
        if (lclQuotePad.getEnteredBy() == null) {
            lclQuotePad.setEnteredBy(loginUser);
            lclQuotePad.setModifiedBy(loginUser);
            lclQuotePad.setEnteredDatetime(now);
            lclQuotePad.setModifiedDatetime(now);
        }
        if (pickUpForm.isPickUpInfo()) {
            LclRemarksDAO lclRemarks = new LclRemarksDAO();
            lclRemarks.insertLclRemarks(lclQuote.getFileNumberId(), "QT-AutoNotes", "Accepted warning about zip code discrepancy", loginUser.getUserId());
            pickUpForm.setPickUpInfo(false);
        }
        //Save PickUp Values and Delivery Values
        lclQuotePad.setPickUpCity(pickUpForm.getCityStateZip());
        saveContact(lclQuotePad, pickUpForm, lclQuote.getLclFileNumber(), lclQuote.getQuoteType());
        this.setUserAndDateTime(pickUpForm.getLclQuotePad().getDeliveryContact(), now, loginUser);
        this.setUserAndDateTime(pickUpForm.getLclQuotePad().getPickupContact(), now, loginUser);
        lclQuotePad.setModifiedBy(getCurrentUser(request));
        lclQuotePad.setModifiedDatetime(now);
        lclQuotePadDAO.saveOrUpdate(lclQuotePad);
        request.setAttribute("lclQuotePad", lclQuotePad);
        request.setAttribute("fileNumberId", pickUpForm.getLclQuotePad().getLclFileNumber().getId());
        request.setAttribute("fileNumber", pickUpForm.getLclQuotePad().getLclFileNumber().getFileNumber());
        request.setAttribute("pickupCharge", pickUpForm.getChargeAmount());
        List<LclQuotePiece> lclCommodityList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id",
                lclQuote.getFileNumberId());
        List<LclQuoteAc> chargeList = lclQuoteAcDAO.getLclCostByFileNumberAsc(lclQuote.getFileNumberId(),
                pickUpForm.getModuleName());
        if ("I".equalsIgnoreCase(lclQuote.getQuoteType())) {
            LclQuoteImport lclQuoteImport = new LclQuoteImportDAO().findById(lclQuote.getFileNumberId());
            if (lclQuoteImport != null) {
                if (pickUpForm.getDoorDeliveryStatus() != null) {
                    lclQuoteImport.setDoorDeliveryStatus(pickUpForm.getDoorDeliveryStatus());
                }
                if (pickUpForm.getPodSigned() != null) {
                    lclQuoteImport.setPodSignedBy(pickUpForm.getPodSigned());
                }
                if (pickUpForm.getPodDate() != null) {
                    lclQuoteImport.setPodSignedDatetime(DateUtils.parseDate(pickUpForm.getPodDate(), "dd-MMM-yyyy hh:mm:ss"));
                }
                if (pickUpForm.getDoorDeliveryEta() != null) {
                    lclQuoteImport.setDoorDeliveryEta(DateUtils.parseDate(pickUpForm.getDoorDeliveryEta(), "dd-MMM-yyyy"));
                }
                new LclQuoteImportDAO().saveOrUpdate(lclQuoteImport);
            }
            ImportQuoteUtils importQuoteUtils = new ImportQuoteUtils();
            importQuoteUtils.setWeighMeasureForImpQuote(request, lclCommodityList);
            importQuoteUtils.setImpRolledUpChargesForQuote(chargeList, request, lclQuote.getFileNumberId(),
                    lclCommodityList, lclQuote.getBillingType());
        } else {
            ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
            exportQuoteUtils.setExpChargesDetails(lclQuote, lclCommodityList, chargeList, loginUser, false, request);//set Export Rate Details
        }

        request.setAttribute("origin", origin);
        request.setAttribute("lclQuote", lclQuote);
        request.setAttribute("destination", destination);
        request.setAttribute("chargeList", chargeList);
        return mapping.findForward("chargeDescription");
    }
// Save pickUp and Delivery Contact

    public void saveContact(LclQuotePad lclQuotePad, LclQuotePickupInfoForm lclQuotePickupInfoForm,
            LclFileNumber lclFileNumber, String moduleName) throws Exception {
        //Save Both imp and Exp Del TO WareHouse Details
        if (lclQuotePickupInfoForm.getLclQuotePad().getPickupContact() == null) {
            lclQuotePickupInfoForm.getLclQuotePad().setPickupContact(new LclContact());
        }
        if (lclQuotePickupInfoForm.getLclQuotePad().getDeliveryContact() == null) {
            lclQuotePickupInfoForm.getLclQuotePad().setDeliveryContact(new LclContact());
        }
        if (lclQuotePickupInfoForm.getWhsecompanyName() != null) {
            lclQuotePickupInfoForm.getLclQuotePad().getDeliveryContact().setCompanyName(lclQuotePickupInfoForm.getWhsecompanyName().toUpperCase());
        } else {
            if (lclQuotePickupInfoForm.getManualWhseName() != null) {
                lclQuotePickupInfoForm.getLclQuotePad().getDeliveryContact().setCompanyName(lclQuotePickupInfoForm.getManualWhseName().toUpperCase());
            }
        }
        // lclQuotePickupInfoForm.getLclQuotePad().getDeliveryContact().setTradingPartner(CommonUtils.isNotEmpty(lclQuotePickupInfoForm.getWhseAccountNo()) ? new TradingPartnerDAO().findById(lclQuotePickupInfoForm.getWhseAccountNo().toUpperCase()) : null);
        lclQuotePickupInfoForm.getLclQuotePad().getDeliveryContact().setAddress(lclQuotePickupInfoForm.getWhseAddress().toUpperCase());
        lclQuotePickupInfoForm.getLclQuotePad().getDeliveryContact().setCity(lclQuotePickupInfoForm.getWhseCity().toUpperCase());
        lclQuotePickupInfoForm.getLclQuotePad().getDeliveryContact().setState(lclQuotePickupInfoForm.getWhseState().toUpperCase());
        lclQuotePickupInfoForm.getLclQuotePad().getDeliveryContact().setZip(lclQuotePickupInfoForm.getWhseZip());
        lclQuotePickupInfoForm.getLclQuotePad().getDeliveryContact().setPhone1(lclQuotePickupInfoForm.getWhsePhone());
        lclQuotePickupInfoForm.getLclQuotePad().getDeliveryContact().setLclFileNumber(lclFileNumber);
        if (lclQuotePickupInfoForm.getWhseId() != null && !lclQuotePickupInfoForm.getWhseId().equals("")) {
            lclQuotePickupInfoForm.getLclQuotePad().getDeliveryContact().setWarehouse(new WarehouseDAO().findById(Integer.parseInt(lclQuotePickupInfoForm.getWhseId())));
        }
        //save Trading Partner in Imports,Exports Warehouse values
        if (moduleName.equalsIgnoreCase("I")) {
            if (CommonUtils.isNotEmpty(lclQuotePickupInfoForm.getAccountNo())) {
                lclQuotePickupInfoForm.getLclQuotePad().getPickupContact().setTradingPartner(new TradingPartnerDAO().findById(lclQuotePickupInfoForm.getAccountNo().toUpperCase()));
                lclQuotePickupInfoForm.getLclQuotePad().getPickupContact().setCompanyName(lclQuotePickupInfoForm.getCompanyName().toUpperCase());
            } else {
                if (CommonUtils.isNotEmpty(lclQuotePickupInfoForm.getManualCompanyName())) {
                    lclQuotePickupInfoForm.getLclQuotePad().getPickupContact().setCompanyName(lclQuotePickupInfoForm.getManualCompanyName().toUpperCase());
                    lclQuotePickupInfoForm.getLclQuotePad().getPickupContact().setTradingPartner(null);
                } else {
                    lclQuotePickupInfoForm.getLclQuotePad().getPickupContact().setCompanyName("");
                    lclQuotePickupInfoForm.getLclQuotePad().getPickupContact().setTradingPartner(null);
                }
            }
        } else {
            if (CommonUtils.isNotEmpty(lclQuotePickupInfoForm.getShipperAccountNo())) {
                lclQuotePickupInfoForm.getLclQuotePad().getPickupContact().setTradingPartner(new TradingPartnerDAO().findById(lclQuotePickupInfoForm.getShipperAccountNo().toUpperCase()));
                lclQuotePickupInfoForm.getLclQuotePad().getPickupContact().setCompanyName(lclQuotePickupInfoForm.getCompanyName().toUpperCase());
            } else {
                if (CommonUtils.isNotEmpty(lclQuotePickupInfoForm.getCompanyNameDup())) {
                    lclQuotePickupInfoForm.getLclQuotePad().getPickupContact().setCompanyName(lclQuotePickupInfoForm.getCompanyNameDup().toUpperCase());
                    lclQuotePickupInfoForm.getLclQuotePad().getPickupContact().setTradingPartner(null);
                } else {
                    lclQuotePickupInfoForm.getLclQuotePad().getPickupContact().setCompanyName("");
                    lclQuotePickupInfoForm.getLclQuotePad().getPickupContact().setTradingPartner(null);
                }
            }
            if (CommonUtils.isNotEmpty(lclQuotePickupInfoForm.getManualCompanyName())) {
                lclQuotePickupInfoForm.getLclQuotePad().setPickUpTo(lclQuotePickupInfoForm.getManualCompanyName().toUpperCase());
            }
        }
        if (lclQuotePickupInfoForm.getLclQuotePad().getPickupContact() != null) {
            lclQuotePickupInfoForm.getLclQuotePad().getPickupContact().setContactName(lclQuotePickupInfoForm.getContactName().toUpperCase());
            lclQuotePickupInfoForm.getLclQuotePad().getPickupContact().setAddress(lclQuotePickupInfoForm.getAddress().toUpperCase());
            lclQuotePickupInfoForm.getLclQuotePad().getPickupContact().setEmail1(lclQuotePickupInfoForm.getEmail1());
            lclQuotePickupInfoForm.getLclQuotePad().getPickupContact().setPhone1(lclQuotePickupInfoForm.getPhone1());
            lclQuotePickupInfoForm.getLclQuotePad().getPickupContact().setFax1(lclQuotePickupInfoForm.getFax1());
            lclQuotePickupInfoForm.getLclQuotePad().getPickupContact().setZip(lclQuotePickupInfoForm.getZip());
            lclQuotePickupInfoForm.getLclQuotePad().getPickupContact().setCity(lclQuotePickupInfoForm.getShipperCity());
            lclQuotePickupInfoForm.getLclQuotePad().getPickupContact().setState(lclQuotePickupInfoForm.getShipperState());
            lclQuotePickupInfoForm.getLclQuotePad().getPickupContact().setLclFileNumber(lclFileNumber);
        }
    }

    public void setUserAndDateTime(LclContact lclContact, Date now, User loginUser) {
        if (lclContact != null) {
            if (lclContact.getEnteredBy() == null || lclContact.getEnteredDatetime() == null) {
                lclContact.setEnteredBy(loginUser);
                lclContact.setEnteredDatetime(now);
            }
            lclContact.setModifiedBy(loginUser);
            lclContact.setModifiedDatetime(now);
        }
    }

    public ActionForward carrier(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclQuotePickupInfoForm lclQuotePickupInfoForm = (LclQuotePickupInfoForm) form;
        HttpSession session = request.getSession();
        LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        session.setAttribute("lclSession", lclSession);
        request.setAttribute("fileNumberId", lclQuotePickupInfoForm.getFileNumberId());
        return mapping.findForward(CARRIER);
    }

    public ActionForward addPickupChargeToAcct(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclQuotePickupInfoForm lclQuotePickupInfoForm = (LclQuotePickupInfoForm) form;
        User loginUser = getCurrentUser(request);
        HttpSession session = request.getSession();
        LclSession lclSession = null != (LclSession) session.getAttribute("lclSession")
                ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        Long fileNumberId = Long.parseLong(request.getParameter("fileNumberId"));
        String scac = request.getParameter("scac");
        String fax1 = request.getParameter("fax");
        String spAcct = request.getParameter("spAcct");
        String email1 = request.getParameter("email1");
        String phone1 = request.getParameter("phone1");
        String contactName = request.getParameter("contactName");
        String pickupHours = request.getParameter("pickupHours");
        String pickupReadyNote = request.getParameter("pickupReadyNote");
        String pickupCharge = request.getParameter("pickupCharge");
        String pickupReadyDate = request.getParameter("pickupReadyDate");
        String cityStateZip = request.getParameter("fromZip");
        String toZipValues = request.getParameter("toZip");
        String toZip = "";
        String fromZipValues = "";
        String fromZip = "";
        if (cityStateZip.indexOf("-") > 1) {
            fromZipValues = cityStateZip.substring(0, cityStateZip.indexOf("-"));
        }
        String costVendor = null;
        LCLQuoteDAO quoteDAO = new LCLQuoteDAO();
        LclQuote lclQuote = quoteDAO.getByProperty("lclFileNumber.id", fileNumberId);
        lclQuote.setPooDoor(true);
        lclQuote.setModifiedBy(loginUser);
        lclQuote.setModifiedDatetime(new Date());
        quoteDAO.getCurrentSession().clear();
        quoteDAO.saveOrUpdate(lclQuote);
        if (LCL_IMPORT_TYPE.equalsIgnoreCase(lclQuote.getQuoteType())) {
            lclQuotePickupInfoForm.setModuleName(LCL_IMPORT);
            fromZip = toZipValues;
            toZip = fromZipValues;
        } else {
            lclQuotePickupInfoForm.setModuleName(LCL_EXPORT);
            fromZip = fromZipValues;
            toZip = toZipValues;
            costVendor = LoadLogisoftProperties.getProperty("PickupVendor");
        }
        BigDecimal weight = new BigDecimal(0.000);
        BigDecimal measure = new BigDecimal(0.000);
        BigDecimal pickUpCost = new BigDecimal(0.000);
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            List<LclQuotePiece> commList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", fileNumberId);
            for (LclQuotePiece lqp : commList) {
                if (lqp.getActualWeightImperial() != null) {
                    weight = weight.add(lqp.getActualWeightImperial());
                } else if (lqp.getBookedWeightImperial() != null) {
                    weight = weight.add(lqp.getBookedWeightImperial());
                }
                if (lqp.getActualVolumeImperial() != null) {
                    measure = measure.add(lqp.getActualVolumeImperial());
                } else if (lqp.getBookedVolumeImperial() != null) {
                    measure = measure.add(lqp.getBookedVolumeImperial());
                }
            }
        }
        String realPath = session.getServletContext().getRealPath("/xml/");
        String fileName = "ctsresponse" + session.getId() + ".xml";
        CallCTSWebServices ctsweb = new CallCTSWebServices();
        lclSession = ctsweb.processCTSWebService(lclSession, realPath, fileName, fromZip,
                toZip, pickupReadyDate, "" + weight, "" + measure, null, "CARRIER_COST", lclQuotePickupInfoForm.getModuleName());
        List<Carrier> carrierCostList = lclSession.getCarrierCostList();
        if (!carrierCostList.isEmpty()) {
            for (int j = 0; j < carrierCostList.size(); j++) {
                Carrier carrier = carrierCostList.get(j);
                if (carrier != null && carrier.getScac() != null && carrier.getScac().trim().equalsIgnoreCase(scac)
                        && carrier.getFinalcharge() != null && !carrier.getFinalcharge().trim().equals("")) {
                    pickUpCost = new BigDecimal(carrier.getFinalcharge());
                    break;
                }
            }
        }
        if (CommonUtils.isNotEmpty(pickupCharge)) {
            LclQuoteAc lclQuoteAc = setPickUpCharge(lclQuotePickupInfoForm, lclQuote,
                    new BigDecimal(pickupCharge), pickUpCost, loginUser, costVendor);
            LclQuotePad lclQuotePad = new LclQuotePadDAO().getLclQuotePadByFileNumber(fileNumberId);
            request.setAttribute("lclQuotePad", lclQuotePad);
            request.setAttribute("lclQuoteAc", lclQuoteAc);
        }
        request.setAttribute("fax1", fax1);
        request.setAttribute("spAcct", spAcct);
        request.setAttribute("email1", email1);
        request.setAttribute("contactName", contactName);
        request.setAttribute("phone1", phone1);
        request.setAttribute("pickupHours", pickupHours);
        request.setAttribute("pickupReadyNote", pickupReadyNote);
        request.setAttribute("scac", scac);
        request.setAttribute("pickupCharge", pickupCharge);
        request.setAttribute("pickupCost", pickUpCost);
        request.setAttribute("lclQuotePickupInfoForm", lclQuotePickupInfoForm);
        return mapping.findForward(PICKUPCHARGE);
    }

    public ActionForward validateCarrierRates(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclQuotePickupInfoForm lclQuotePickupInfoForm = (LclQuotePickupInfoForm) form;
        Long fileId = lclQuotePickupInfoForm.getFileNumberId();
        String errorMessage = null;
        if (CommonUtils.isNotEmpty(fileId)) {
            String hasBookingPiece = new LclQuotePieceDAO().hasQuotePiece(fileId);
            if ("false".equalsIgnoreCase(hasBookingPiece)) {
                errorMessage = "Please add Weight and Measure from Commodity";
            } else if (errorMessage == null) {
                String pickUpStatus = LoadLogisoftProperties.getProperty("application.enableCTS");
                if (pickUpStatus != null && !pickUpStatus.equals("") && "N".equalsIgnoreCase(pickUpStatus)) {
                    errorMessage = "CTS is disabled";
                } else {
                    String fromZip = request.getParameter("fromZip");///Import fromZip is warehouse Zip,Export---pickUpZip
                    String pickupReadyDate = request.getParameter("pickupReadyDate");
                    String fileNumberId = request.getParameter("fileNumberId");
                    String toZip = request.getParameter("toZip");//Import toZip is DoorZip,Export--warehouseZip
                    HttpSession session = request.getSession();
                    LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
                    lclSession.setXmlObjMap(null);
                    lclSession.setCarrierList(null);
                    session.setAttribute("lclSession", lclSession);
                    BigDecimal weight = new BigDecimal(0.000);
                    BigDecimal measure = new BigDecimal(0.000);
                    if (CommonUtils.isNotEmpty(fileNumberId)) {
                        List<LclQuotePiece> commList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
                        for (LclQuotePiece lqp : commList) {
                            if (lqp.getActualWeightImperial() != null) {
                                weight = weight.add(lqp.getActualWeightImperial());
                            } else if (lqp.getBookedWeightImperial() != null) {
                                weight = weight.add(lqp.getBookedWeightImperial());
                            }
                            if (lqp.getActualVolumeImperial() != null) {
                                measure = measure.add(lqp.getActualVolumeImperial());
                            } else if (lqp.getBookedVolumeImperial() != null) {
                                measure = measure.add(lqp.getBookedVolumeImperial());
                            }
                        }
                    }
                    String realPath = session.getServletContext().getRealPath("/xml/");
                    String fileName = "ctsresponse" + session.getId() + ".xml";
                    CallCTSWebServices ctsweb = new CallCTSWebServices();
                    lclSession = ctsweb.processCTSWebService(lclSession, realPath, fileName, fromZip, toZip, pickupReadyDate,
                            "" + weight, "" + measure, "CARRIER_CHARGE", null, lclQuotePickupInfoForm.getModuleName());
                    session.setAttribute("lclSession", lclSession);
                }
            }
        }
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(errorMessage);
        return null;
    }

    public ActionForward ShipperSearch(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("searchByValue", request.getParameter("searchByValue"));
        return mapping.findForward("ShipperSearch");
    }

    public LclQuoteAc setPickUpCharge(LclQuotePickupInfoForm pickUpForm, LclQuote lclQuote, BigDecimal pickUpCharge,
            BigDecimal pickUpCost, User loginUser, String costVendor) throws Exception {
        GlMappingDAO glMappingDAO = new GlMappingDAO();
        LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
        Date now = new Date();
        String shipmentType = "", chargeCode = "", arBillToParty = "";
        if (LCL_IMPORT_TYPE.equalsIgnoreCase(lclQuote.getQuoteType())) {
            shipmentType = LCL_SHIPMENT_TYPE_IMPORT;
            chargeCode = CHARGE_CODE_DOOR;
            arBillToParty = "C";
            pickUpForm.setModuleName(LCL_IMPORT);
        } else {
            shipmentType = LCL_SHIPMENT_TYPE_EXPORT;
            chargeCode = LoadLogisoftProperties.getProperty("InlandChargeCode");
            pickUpForm.setModuleName(LCL_EXPORT);
            arBillToParty = null;
        }
        LclQuoteAc lclQuoteAc = lclQuoteAcDAO.getLclQuoteAcByChargeCode(lclQuote.getFileNumberId(), chargeCode);
        if (lclQuoteAc == null) {
            lclQuoteAc = new LclQuoteAc();
            lclQuoteAc.setEnteredDatetime(now);
            lclQuoteAc.setEnteredBy(loginUser);
            lclQuoteAc.setLclFileNumber(lclQuote.getLclFileNumber());
            GlMapping gp = glMappingDAO.findByChargeCode(chargeCode, shipmentType, "AR");
            lclQuoteAc.setArglMapping(gp);
            GlMapping apGlmapping = glMappingDAO.findByChargeCode(chargeCode, shipmentType, TRANSACTION_TYPE_ACCRUALS);
            lclQuoteAc.setApglMapping(apGlmapping);
            lclQuoteAc.setRatePerUnitUom("FL");
            lclQuoteAc.setRateUom("I");
            lclQuoteAc.setAdjustmentAmount(new BigDecimal(0.00));
            lclQuoteAc.setBundleIntoOf(false);
            lclQuoteAc.setPrintOnBl(true);
            lclQuoteAc.setManualEntry(true);
        }
        lclQuoteAc.setArBillToParty(arBillToParty);
        lclQuoteAc.setArAmount(pickUpCharge);
        lclQuoteAc.setRatePerUnit(pickUpCharge);
        lclQuoteAc.setApAmount(pickUpCost);
        lclQuoteAc.setCostFlatrateAmount(pickUpCost);
        lclQuoteAc.setTransDatetime(now);
        lclQuoteAc.setSupAcct(null != costVendor ? new TradingPartnerDAO().findById(costVendor) : null);
        lclQuoteAc.setModifiedBy(loginUser);
        lclQuoteAc.setModifiedDatetime(now);
        lclQuoteAcDAO.saveOrUpdate(lclQuoteAc);
        return lclQuoteAc;
    }
}

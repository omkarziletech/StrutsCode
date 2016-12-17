    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.lcl.dwr.LclChargesCalculation;
import com.gp.cong.lcl.dwr.LclSession;
import com.gp.cong.lcl.webservices.CallCTSWebServices;
import com.gp.cong.lcl.webservices.dom.Carrier;
import com.gp.cong.logisoft.bc.fcl.EdiTrackingBC;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cong.logisoft.domain.lcl.LclBookingImport;
import com.gp.cong.logisoft.domain.lcl.LclBookingPad;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.WarehouseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingAcTransDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPadDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO;
import com.gp.cong.logisoft.lcl.bc.LclBookingUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclPickupInfoForm;
import com.logiware.common.reports.CtsBooking;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author lakshh
 */
public class LclPickupInfoAction extends LogiwareDispatchAction implements LclCommonConstant, ConstantsInterface {

    private static final String CARRIER = "carrier";
    private static final String PICKUPCHARGE = "pickupCharge";
    private LclBookingPadDAO lclBookingPadDAO = new LclBookingPadDAO();
    private LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
    private LclUtils lclUtils = new LclUtils();

    public ActionForward display(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String FRWD_PAGE = "";
        TradingPartner accountNumber = null;
        LclPickupInfoForm lclPickupInfoForm = (LclPickupInfoForm) form;
        if (lclPickupInfoForm.getLclBookingPad().getPickupInstructions() == null) {
            String pickupEnable = LoadLogisoftProperties.getProperty("application.pickupInstructionEnable");
            if (pickupEnable != null && pickupEnable.equals("Y")) {
                String pickUpInstruction = LoadLogisoftProperties.getProperty("application.lclPickupInstruction");
                if (pickUpInstruction != null && !pickUpInstruction.equals("")) {
                    lclPickupInfoForm.getLclBookingPad().setPickupInstructions(pickUpInstruction.toUpperCase());
                }
            }
        }
        String chargeCode = "", shipmentType = "";
        if (LCL_IMPORT.equalsIgnoreCase(lclPickupInfoForm.getModuleName())) {
            chargeCode = CHARGE_CODE_DOOR;
            shipmentType = LCL_SHIPMENT_TYPE_IMPORT;
        } else {
            chargeCode = LoadLogisoftProperties.getProperty("InlandChargeCode");
            shipmentType = LCL_SHIPMENT_TYPE_EXPORT;
        }
        LclBookingUtils lclBookingUtils = new LclBookingUtils();
        String data[] = lclBookingUtils.validateForChargeandCost(chargeCode, shipmentType);
        request.setAttribute("arGlMappingFlag", data[0]);
        request.setAttribute("apGlMappingFlag", data[1]);
        LclBookingAc lclBookingAc = new LclCostChargeDAO().getLclBookingAcByChargeCode(lclPickupInfoForm.getFileNumberId(), chargeCode);
        if (lclBookingAc != null) {
            lclPickupInfoForm.setLclBookingAc(lclBookingAc);
            if (lclBookingAc.getApAmount() != null) {
                request.setAttribute("pickupCost", lclBookingAc.getApAmount());
            }
            if (lclBookingAc.getArAmount() != null) {
                request.setAttribute("pickupCharge", lclBookingAc.getArAmount());
            }
            if (lclBookingAc.getSupAcct() != null) {
                lclPickupInfoForm.setCostVendorAcct(lclBookingAc.getSupAcct().getAccountName());
                lclPickupInfoForm.setCostVendorNo(lclBookingAc.getSupAcct().getAccountno());
            }
            if (lclBookingAc.getSpReferenceNo() != null) {
                request.setAttribute("invoiceStatus", "true");
            }
            if (CommonUtils.isNotEmpty(lclBookingAc.getArBillToParty())) {
                request.setAttribute("billToParty", lclBookingAc.getArBillToParty());
            }
            String transType = new LCLBookingAcTransDAO().getTransType(lclBookingAc.getId());
            request.setAttribute("transType", transType);
        }
        request.setAttribute("scac", lclPickupInfoForm.getLclBookingPad().getScac());
        LclBookingPad bookingPad = lclPickupInfoForm.getLclBookingPad();
        if (bookingPad.getPickupContact() != null) {
            request.setAttribute("fax1", bookingPad.getPickupContact().getFax1());
            request.setAttribute("phone1", bookingPad.getPickupContact().getPhone1());
            request.setAttribute("contactName", bookingPad.getPickupContact().getContactName());
            request.setAttribute("email1", bookingPad.getPickupContact().getEmail1());
        }
        request.setAttribute("pickupHours", bookingPad.getPickupHours());
        request.setAttribute("pickupReadyNote", bookingPad.getPickupReadyNote());
        request.setAttribute("scac", bookingPad.getScac());
        User loginUser = getCurrentUser(request);
        LclBooking lclBooking = new LCLBookingDAO().findById(lclPickupInfoForm.getFileNumberId());
        //get Values and Set into pickUp and delivery field
        if (null != bookingPad && bookingPad.getPickupContact() != null
                && bookingPad.getPickupContact().getId() != null) {
            if (LCL_IMPORT.equalsIgnoreCase(lclPickupInfoForm.getModuleName())) {
                if (bookingPad.getPickupContact().getTradingPartner() != null
                        && bookingPad.getPickupContact().getTradingPartner().getAccountno() != null) {
                    lclPickupInfoForm.setCompanyName(bookingPad.getPickupContact().getTradingPartner().getAccountName());
                    lclPickupInfoForm.setAccountNo(bookingPad.getPickupContact().getTradingPartner().getAccountno());
                } else {
                    if (bookingPad.getDeliveryContact() != null
                            && bookingPad.getDeliveryContact().getCompanyName() != null) {
                        lclPickupInfoForm.setManualCompanyName(bookingPad.getPickupContact().getCompanyName());
                    }
                }
            } else {
                if (bookingPad.getPickupContact().getTradingPartner() != null
                        && bookingPad.getPickupContact().getTradingPartner().getAccountno() != null) {
                    lclPickupInfoForm.setCompanyName(bookingPad.getPickupContact().getTradingPartner().getAccountName());
                    lclPickupInfoForm.setShipperAccountNo(bookingPad.getPickupContact().getTradingPartner().getAccountno());
                    lclPickupInfoForm.setNewCompanyName("off");
                } else {
                    if (CommonUtils.isNotEmpty(bookingPad.getPickupContact().getCompanyName())) {
                        lclPickupInfoForm.setCompanyNameDup(bookingPad.getPickupContact().getCompanyName());
                        lclPickupInfoForm.setShipperAccountNo(null);
                        lclPickupInfoForm.setNewCompanyName("on");
                    }
                }
                if (bookingPad.getPickUpTo() != null) {
                    accountNumber = new TradingPartnerDAO().findAccountNumberByPassingAccountName(bookingPad.getPickUpTo());

                    if (accountNumber != null && accountNumber.getAccountno() != null) {
                        lclPickupInfoForm.setToAccountName(bookingPad.getPickUpTo());
                        lclPickupInfoForm.setManualShipper("off");
                    } else {
                        lclPickupInfoForm.setManualCompanyName(bookingPad.getPickUpTo());
                        lclPickupInfoForm.setManualShipper("on");

                    }
                }
            }
            lclPickupInfoForm.setAddress(bookingPad.getPickupContact().getAddress());
            lclPickupInfoForm.setShipperCity(bookingPad.getPickupContact().getCity());
            lclPickupInfoForm.setShipperState(bookingPad.getPickupContact().getState());
            lclPickupInfoForm.setShipperZip(bookingPad.getPickupContact().getZip());
        }
        if (CommonUtils.isEmpty(lclPickupInfoForm.getCommodityDesc())
                && CommonUtils.isNotEmpty(lclBooking.getLclFileNumber().getLclBookingPieceList())
                && CommonUtils.isNotEmpty(lclBooking.getLclFileNumber().getLclBookingPieceList().get(0).getPieceDesc())) {
            lclPickupInfoForm.setCommodityDesc(lclBooking.getLclFileNumber().getLclBookingPieceList().get(0).getPieceDesc());
        }
        if (bookingPad != null && bookingPad.getDeliveryContact() != null
                && bookingPad.getDeliveryContact().getId() != null && bookingPad.getDeliveryContact().getCompanyName() != null) {
            lclPickupInfoForm.setWhsecompanyName(bookingPad.getDeliveryContact().getCompanyName());
            lclPickupInfoForm.setWhseAddress(bookingPad.getDeliveryContact().getAddress());
            lclPickupInfoForm.setWhseCity(bookingPad.getDeliveryContact().getCity());
            lclPickupInfoForm.setWhseState(bookingPad.getDeliveryContact().getState());
            lclPickupInfoForm.setWhseZip(bookingPad.getDeliveryContact().getZip());
            lclPickupInfoForm.setWhsePhone(bookingPad.getDeliveryContact().getPhone1());
            if (bookingPad.getDeliveryContact().getWarehouse() != null) {
                lclPickupInfoForm.setWhseId(String.valueOf(bookingPad.getDeliveryContact().getWarehouse().getId()));
            }
        }
        lclPickupInfoForm.setPickedupDatetime(DateUtils.formatDate(bookingPad.getPickedupDatetime(), "dd-MMM-yyyy HH:mm:ss"));
        lclPickupInfoForm.setDeliveredDatetime(DateUtils.formatDate(bookingPad.getDeliveredDatetime(), "dd-MMM-yyyy HH:mm:ss"));
        lclPickupInfoForm.setEstPickupDate(DateUtils.formatDate(bookingPad.getEstPickupDate(), "dd-MMM-yyyy"));
        lclPickupInfoForm.setEstimatedDeliveryDate(DateUtils.formatDate(bookingPad.getEstimatedDeliveryDate(), "dd-MMM-yyyy"));
        if (lclBooking != null) {
            lclPickupInfoForm.setTrmnum(lclBooking.getEnteredBy().getTerminal().getTrmnum());
            lclPickupInfoForm.setIssuingTerminal(lclBooking.getEnteredBy().getLoginName().toUpperCase()
                    + "/" + lclBooking.getEnteredBy().getTerminal().getTrmnum());
            request.setAttribute("fileNumberStatus", lclBooking.getLclFileNumber().getStatus());
        } else {
            lclPickupInfoForm.setTrmnum(loginUser.getTerminal().getTrmnum());
            lclPickupInfoForm.setIssuingTerminal(loginUser.getLoginName().toUpperCase()
                    + "/" + loginUser.getTerminal().getTrmnum());
        }
        if (LCL_IMPORT.equalsIgnoreCase(lclPickupInfoForm.getModuleName())) {
            LclBookingImport lclBookingImport = new LclBookingImportDAO().findById(lclBooking.getLclFileNumber().getId());
            if (lclBookingImport != null) {
                if (lclPickupInfoForm.getLclBookingPad() == null || lclPickupInfoForm.getLclBookingPad().getLastFreeDate() == null) {
                    lclPickupInfoForm.setLastFreeDate(lclBookingImport.getLastFreeDateTime() != null
                            ? DateUtils.formatDate(lclBookingImport.getLastFreeDateTime(), "dd-MMM-yyyy") : "");
                } else {
                    lclPickupInfoForm.setLastFreeDate(lclPickupInfoForm.getLclBookingPad().getLastFreeDate() != null
                            ? DateUtils.formatDate(lclPickupInfoForm.getLclBookingPad().getLastFreeDate(), "dd-MMM-yyyy") : "");
                }
                if (lclBookingImport.getPodSignedBy() != null) {
                    lclPickupInfoForm.setPodSigned(lclBookingImport.getPodSignedBy());
                }
                if (lclBookingImport.getPodSignedDatetime() != null) {
                    lclPickupInfoForm.setPodDate(DateUtils.formatDate(lclBookingImport.getPodSignedDatetime(), "dd-MMM-yyyy hh:mm:ss"));
                }
                if (lclBookingImport.getDoorDeliveryEta() != null) {
                    lclPickupInfoForm.setDoorDeliveryEta(DateUtils.formatDate(lclBookingImport.getDoorDeliveryEta(), "dd-MMM-yyyy"));
                }
                if (lclBookingImport.getDoorDeliveryStatus() != null) {
                    lclPickupInfoForm.setDoorDeliveryStatus(lclBookingImport.getDoorDeliveryStatus());
                }
            }
            FRWD_PAGE = "impPickUp";
        } else {
            FRWD_PAGE = "success";
        }
        request.setAttribute("closedBy", request.getParameter("closedBy"));
        request.setAttribute("ediStatus", lclUtils.getEdiStatus(lclPickupInfoForm.getFileNumber()));
        request.setAttribute("lclPickupInfoForm", lclPickupInfoForm);
        return mapping.findForward(FRWD_PAGE);
    }

    public ActionForward save(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String FORWARD_PAGE = "";
        LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
        User loginUser = getCurrentUser(request);
        LclPickupInfoForm pickUpForm = (LclPickupInfoForm) form;
        String moduleName = pickUpForm.getModuleName();
        String headerid = pickUpForm.getHeaderId();
        Long fileId = pickUpForm.getFileNumberId();
        LclBooking lclBooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", fileId);
        lclBooking.setPooPickup(true);
        lclBooking.setModifiedBy(loginUser);
        lclBooking.setModifiedDatetime(new Date());
        // lclBookingDAO.getCurrentSession().clear();
        lclBookingDAO.saveOrUpdate(lclBooking);
        LclBookingPad lclBookingPad = pickUpForm.getLclBookingPad();
        BigDecimal pickUpCost = CommonUtils.isNotEmpty(pickUpForm.getPickupCost())
                ? new BigDecimal(pickUpForm.getPickupCost()) : new BigDecimal(0.00);
        BigDecimal pickUpCharge = CommonUtils.isNotEmpty(pickUpForm.getChargeAmount())
                ? new BigDecimal(pickUpForm.getChargeAmount()) : new BigDecimal(0.00);
        if (pickUpCost.doubleValue() != 0 || pickUpCharge.doubleValue() != 0) {
            LclBookingAc lclBookingAc = setPickUpCharge(pickUpForm, lclBooking,
                    pickUpCharge, pickUpCost, loginUser, pickUpForm.getCostVendorNo());
            pickUpForm.getLclBookingPad().setLclBookingAc(lclBookingAc);
        }
        lclBookingPad.setLclFileNumber(lclBooking.getLclFileNumber());
        if (lclBookingPad.getEnteredBy() == null) {
            lclBookingPad.setEnteredBy(getCurrentUser(request));
            lclBookingPad.setEnteredDatetime(new Date());
        }
        //Save PickUp Values and Delivery Values
        saveContact(lclBookingPad, pickUpForm, lclBooking.getLclFileNumber(), moduleName, request);
        this.setUserAndDateTime(pickUpForm.getLclBookingPad().getDeliveryContact(), request);
        this.setUserAndDateTime(pickUpForm.getLclBookingPad().getPickupContact(), request);

        lclBookingPad.setPickUpTo(CommonUtils.isNotEmpty(pickUpForm.getManualCompanyName()) ? pickUpForm.getManualCompanyName()
                : CommonUtils.isNotEmpty(pickUpForm.getToAccountName()) ? pickUpForm.getToAccountName() : null);

        lclBookingPad.setPickUpCity(pickUpForm.getCityStateZip());
        lclBookingPad.setModifiedBy(loginUser);
        lclBookingPad.setModifiedDatetime(new Date());
        if (pickUpForm.getLastFreeDate() != null) {
            lclBookingPad.setLastFreeDate(DateUtils.parseDate(pickUpForm.getLastFreeDate(), "dd-MMM-yyyy"));
        }
        if (CommonUtils.isNotEmpty(pickUpForm.getPickedupDatetime())) {
            lclBookingPad.setPickedupDatetime(DateUtils.parseToDateForMonthMMMandTimeTT(pickUpForm.getPickedupDatetime()));
        } else {
            lclBookingPad.setPickedupDatetime(null);
        }
        if (CommonUtils.isNotEmpty(pickUpForm.getDeliveredDatetime())) {
            lclBookingPad.setDeliveredDatetime(DateUtils.parseToDateForMonthMMMandTimeTT(pickUpForm.getDeliveredDatetime()));
        } else {
            lclBookingPad.setDeliveredDatetime(null);
        }
        if (null != pickUpForm.getEstPickupDate()) {
            lclBookingPad.setEstPickupDate(DateUtils.parseDate(pickUpForm.getEstPickupDate(), "dd-MMM-yyyy"));
        }
        if (null != pickUpForm.getEstimatedDeliveryDate()) {
            lclBookingPad.setEstimatedDeliveryDate(DateUtils.parseDate(pickUpForm.getEstimatedDeliveryDate(), "dd-MMM-yyyy"));
        }
        
        
        if (pickUpForm.isPickUpInfo()) {
            User user = getCurrentUser(request);
            LclRemarksDAO lclRemarks = new LclRemarksDAO();
            lclRemarks.insertLclRemarks(fileId, REMARKS_DR_AUTO_NOTES, "Accepted warning about zip code discrepancy", user.getUserId());
            pickUpForm.setPickUpInfo(false);

        }
        lclBookingPadDAO.saveOrUpdate(lclBookingPad);
        //  lclBookingPadDAO.flush();
        request.setAttribute("lclBookingPad", lclBookingPad);
        request.setAttribute("fileNumberId", pickUpForm.getLclBookingPad().getLclFileNumber().getId());
        request.setAttribute("fileNumber", pickUpForm.getLclBookingPad().getLclFileNumber().getFileNumber());
        request.setAttribute("pickupCharge", pickUpForm.getChargeAmount());

        String fdEngmet = "";
        Ports ports = null;
        List<LclBookingPiece> lclCommodityList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", fileId);
        if (LCL_EXPORT_TYPE.equalsIgnoreCase(lclBooking.getBookingType())) {
            String pooTrmnum = "", polTrmnum = "", podEciPortCode = "", fdEciPortCode = "";
            String pooUnloCode = lclBooking.getPortOfOrigin() != null ? lclBooking.getPortOfOrigin().getUnLocationCode() : "";
            String polUnloCode = lclBooking.getPortOfLoading() != null ? lclBooking.getPortOfLoading().getUnLocationCode() : "";
            String podUnloCode = lclBooking.getPortOfDestination() != null ? lclBooking.getPortOfDestination().getUnLocationCode() : "";
            String fdUnloCode = lclBooking.getFinalDestination() != null ? lclBooking.getFinalDestination().getUnLocationCode() : "";
            String rateType = "R".equalsIgnoreCase(lclBooking.getRateType()) ? "Y" : lclBooking.getRateType();
            List l = new LclUtils().getTrmNumandEciPortCode(pooUnloCode, polUnloCode, podUnloCode, fdUnloCode, rateType);
            for (Object row : l) {
                Object[] col = (Object[]) row;
                if (col[2].toString().equalsIgnoreCase("POO")) {
                    pooTrmnum = (String) col[0];
                }
                if (col[2].toString().equalsIgnoreCase("POL")) {
                    polTrmnum = (String) col[0];
                }
                if (col[2].toString().equalsIgnoreCase("POD")) {
                    podEciPortCode = (String) col[0];
                }
                if (col[2].toString().equalsIgnoreCase("FD")) {
                    fdEciPortCode = (String) col[0];
                    fdEngmet = (String) col[1];
                }
            }
            ports = new PortsDAO().getByProperty("unLocationCode", lclBooking.getFinalDestination().getUnLocationCode());
            LclChargesCalculation lclChargesCalculation = new LclChargesCalculation();
            LclBookingAc cafCharge = lclCostChargeDAO.manaualChargeValidate(lclBooking.getLclFileNumber().getId(), "CAF", false);
            if (cafCharge != null) {
                lclChargesCalculation.calculateCAFCharge(pooTrmnum, polTrmnum, fdEciPortCode,
                        podEciPortCode, lclCommodityList, lclBooking.getBillingType(),
                        loginUser, lclBooking.getLclFileNumber().getId(), request, ports, lclBooking.getBillToParty());
            }
        }
        List<LclBookingAc> chargeList = lclCostChargeDAO.getLclCostByFileNumberAsc(fileId, moduleName);
        if (CommonUtils.isEqual(moduleName, LCL_IMPORT)) {
            LclBookingImport lclBookingImport = new LclBookingImportDAO().findById(lclBooking.getLclFileNumber().getId());
            if (lclBookingImport != null) {
                if (pickUpForm.getDoorDeliveryStatus() != null) {
                    lclBookingImport.setDoorDeliveryStatus(pickUpForm.getDoorDeliveryStatus());
                }
                if (pickUpForm.getPodSigned() != null) {
                    lclBookingImport.setPodSignedBy(pickUpForm.getPodSigned());
                }
                if (pickUpForm.getPodDate() != null) {
                    lclBookingImport.setPodSignedDatetime(DateUtils.parseDate(pickUpForm.getPodDate(), "dd-MMM-yyyy hh:mm:ss"));
                }
                if (pickUpForm.getDoorDeliveryEta() != null) {
                    lclBookingImport.setDoorDeliveryEta(DateUtils.parseDate(pickUpForm.getDoorDeliveryEta(), "dd-MMM-yyyy"));
                }
                lclBookingImport.setModifiedBy(loginUser);
                lclBookingImport.setModifiedDatetime(new Date());
                new LclBookingImportDAO().saveOrUpdate(lclBookingImport);
            }
            lclUtils.setWeighMeasureForImportBooking(request, lclCommodityList, null);
            lclUtils.setImportRolledUpChargesForBooking(chargeList, request, fileId, lclCostChargeDAO,
                    lclCommodityList, lclBooking.getBillingType(), "", "");
            LclSsHeader lclssheader = CommonUtils.isNotEmpty(pickUpForm.getHeaderId())
                    ? new LclSsHeaderDAO().findById(Long.parseLong(pickUpForm.getHeaderId())) : null;
            request.setAttribute("lclssheader", lclssheader);
        } else {
            lclUtils.setWeighMeasureForBooking(request, lclCommodityList, ports);
            lclUtils.setRolledUpChargesForBooking(chargeList, request, fileId, lclCostChargeDAO,
                    lclCommodityList, lclBooking.getBillingType(), fdEngmet, "No");
        }
        FORWARD_PAGE = "chargeDescription";
        //Performing Execution of  Send Edi to Cts method***************************
        String sendEdiToCtsFlag = pickUpForm.getSendEdiToCtsFlag();
        if (null != sendEdiToCtsFlag && sendEdiToCtsFlag.equals("true")) {
            StringBuilder fileNumber = new StringBuilder();
            EdiTrackingBC editracking = new EdiTrackingBC();
            SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyyHHmmss");
            String date = sdf.format(new Date());
            CtsBooking edi = new CtsBooking();
            String csvpath = LoadLogisoftProperties.getProperty("edi.cts");
            if (LCL_IMPORT.equalsIgnoreCase(pickUpForm.getModuleName())) {
                fileNumber.append("IMP");
                FORWARD_PAGE = "impPickUp";
            } else {
                fileNumber.append(lclBooking.getPortOfOrigin().getUnLocationCode().substring(2, 5));
                FORWARD_PAGE = "success";
            }
            fileNumber.append("-").append(lclBooking.getLclFileNumber().getFileNumber());
            String filename = edi.createCSV(csvpath, headerid, fileId, moduleName, lclBooking, loginUser, pickUpForm);
            if (filename != null) {
                editracking.setEdiLogCtsforLcl(filename, date, "success", "No Error", "C", "304",
                        lclBooking.getLclFileNumber().getFileNumber(), "", "", null, lclBooking.getLclFileNumber().getFileNumber());
                new LclRemarksDAO().insertLclRemarks(lclBooking.getLclFileNumber(), "CTS", "Sent EDI", loginUser);
            } else {
                editracking.setEdiLogCtsforLcl(filename, date, "failure", " Error", "C", "304",
                        fileNumber.toString(), "", "", null, lclBooking.getLclFileNumber().getFileNumber());
            }
            request.setAttribute("ediStatus", lclUtils.getEdiStatus(lclBooking.getLclFileNumber().getFileNumber()));
            request.setAttribute("fax1", pickUpForm.getFax1());
            request.setAttribute("email1", pickUpForm.getEmail1());
            request.setAttribute("pickupReadyNote", pickUpForm.getPickupReadyNote());
            request.setAttribute("contactName", pickUpForm.getContactName());
            request.setAttribute("phone1", pickUpForm.getPhone1());
            request.setAttribute("pickupHours", pickUpForm.getPickupHours());
            request.setAttribute("scac", pickUpForm.getScacCode());
            request.setAttribute("pickupCharge", pickUpForm.getChargeAmount());
            request.setAttribute("pickupCost", pickUpForm.getPickupCost());
        }

        //**********************************************************************
        request.setAttribute("fileNumberStatus", lclBooking.getLclFileNumber().getStatus());
        request.setAttribute("lclBooking", lclBooking);
        return mapping.findForward(FORWARD_PAGE);
    }

    public void setUserAndDateTime(LclContact lclContact, HttpServletRequest request) {
        if (lclContact != null) {
            if (lclContact.getEnteredBy() == null || lclContact.getEnteredDatetime() == null) {
                lclContact.setEnteredBy(getCurrentUser(request));
                lclContact.setEnteredDatetime(new Date());
            }
            lclContact.setModifiedBy(getCurrentUser(request));
            lclContact.setModifiedDatetime(new Date());
        }
    }
    // Save pickUp and Delivery Contact

    public void saveContact(LclBookingPad lclBookingPad, LclPickupInfoForm lclPickupInfoForm,
            LclFileNumber lclFileNumber, String moduleName, HttpServletRequest request) throws Exception {
        //Save Both imp and Exp Del TO WareHouse Details
        setLclcontactRemarks(lclBookingPad, lclPickupInfoForm, lclFileNumber, request);
        if (lclPickupInfoForm.getLclBookingPad().getDeliveryContact() == null) {
            lclPickupInfoForm.getLclBookingPad().setDeliveryContact(new LclContact());
        }
        if (lclPickupInfoForm.getLclBookingPad().getPickupContact() == null) {
            lclPickupInfoForm.getLclBookingPad().setPickupContact(new LclContact());
        }
        lclPickupInfoForm.getLclBookingPad().getDeliveryContact().setCompanyName(lclPickupInfoForm.getWhsecompanyName().toUpperCase());
        lclPickupInfoForm.getLclBookingPad().getDeliveryContact().setAddress(lclPickupInfoForm.getWhseAddress().toUpperCase());
        lclPickupInfoForm.getLclBookingPad().getDeliveryContact().setCity(lclPickupInfoForm.getWhseCity().toUpperCase());
        lclPickupInfoForm.getLclBookingPad().getDeliveryContact().setState(lclPickupInfoForm.getWhseState().toUpperCase());
        lclPickupInfoForm.getLclBookingPad().getDeliveryContact().setZip(lclPickupInfoForm.getWhseZip());
        lclPickupInfoForm.getLclBookingPad().getDeliveryContact().setPhone1(lclPickupInfoForm.getWhsePhone());
        lclPickupInfoForm.getLclBookingPad().getDeliveryContact().setFax1(lclPickupInfoForm.getWhseFax());
        lclPickupInfoForm.getLclBookingPad().getDeliveryContact().setLclFileNumber(lclFileNumber);
        if (lclPickupInfoForm.getWhseId() != null && !lclPickupInfoForm.getWhseId().equals("")) {
            lclPickupInfoForm.getLclBookingPad().getDeliveryContact().setWarehouse(new WarehouseDAO().findById(Integer.parseInt(lclPickupInfoForm.getWhseId())));
        }
        //save Trading Partner in Imports,Exports Warehouse values
        if (CommonUtils.isNotEmpty(moduleName) && moduleName.equalsIgnoreCase("Imports")) {
            if (CommonUtils.isNotEmpty(lclPickupInfoForm.getAccountNo())) {
                lclPickupInfoForm.getLclBookingPad().getPickupContact().setTradingPartner(new TradingPartnerDAO().findById(lclPickupInfoForm.getAccountNo().toUpperCase()));
                lclPickupInfoForm.getLclBookingPad().getPickupContact().setCompanyName(lclPickupInfoForm.getCompanyName().toUpperCase());
            } else {
                if (CommonUtils.isNotEmpty(lclPickupInfoForm.getManualCompanyName())) {
                    lclPickupInfoForm.getLclBookingPad().getPickupContact().setCompanyName(lclPickupInfoForm.getManualCompanyName().toUpperCase());
                    lclPickupInfoForm.getLclBookingPad().getPickupContact().setTradingPartner(null);
                } else {
                    lclPickupInfoForm.getLclBookingPad().getPickupContact().setCompanyName("");
                    lclPickupInfoForm.getLclBookingPad().getPickupContact().setTradingPartner(null);
                }
            }
        } else {
            if (CommonUtils.isNotEmpty(lclPickupInfoForm.getShipperAccountNo())) {
                lclPickupInfoForm.getLclBookingPad().getPickupContact().setTradingPartner(new TradingPartnerDAO().findById(lclPickupInfoForm.getShipperAccountNo().toUpperCase()));
                lclPickupInfoForm.getLclBookingPad().getPickupContact().setCompanyName(lclPickupInfoForm.getCompanyName().toUpperCase());
            } else {
                if (CommonUtils.isNotEmpty(lclPickupInfoForm.getCompanyNameDup())) {
                    lclPickupInfoForm.getLclBookingPad().getPickupContact().setCompanyName(lclPickupInfoForm.getCompanyNameDup().toUpperCase());
                    lclPickupInfoForm.getLclBookingPad().getPickupContact().setTradingPartner(null);
                } else {
                    lclPickupInfoForm.getLclBookingPad().getPickupContact().setCompanyName("");
                    lclPickupInfoForm.getLclBookingPad().getPickupContact().setTradingPartner(null);
                }
            }
            if (CommonUtils.isNotEmpty(lclPickupInfoForm.getManualCompanyName())) {
                lclPickupInfoForm.getLclBookingPad().setPickUpTo(lclPickupInfoForm.getManualCompanyName().toUpperCase());
            }
        }
        if (lclPickupInfoForm.getLclBookingPad().getPickupContact() != null) {
            lclPickupInfoForm.getLclBookingPad().getPickupContact().setContactName(null != lclPickupInfoForm.getContactName() ? lclPickupInfoForm.getContactName().toUpperCase() : "");
            lclPickupInfoForm.getLclBookingPad().getPickupContact().setAddress(null != lclPickupInfoForm.getAddress() ? lclPickupInfoForm.getAddress() : lclPickupInfoForm.getAddress().toUpperCase());
            lclPickupInfoForm.getLclBookingPad().getPickupContact().setEmail1(lclPickupInfoForm.getEmail1());
            lclPickupInfoForm.getLclBookingPad().getPickupContact().setPhone1(lclPickupInfoForm.getPhone1());
            lclPickupInfoForm.getLclBookingPad().getPickupContact().setFax1(lclPickupInfoForm.getFax1());
            lclPickupInfoForm.getLclBookingPad().getPickupContact().setCity(lclPickupInfoForm.getShipperCity());
            lclPickupInfoForm.getLclBookingPad().getPickupContact().setState(lclPickupInfoForm.getShipperState());
            lclPickupInfoForm.getLclBookingPad().getPickupContact().setZip(lclPickupInfoForm.getShipperZip());
            lclPickupInfoForm.getLclBookingPad().getPickupContact().setLclFileNumber(lclFileNumber);
        }
    }

    public ActionForward carrier(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclPickupInfoForm lclPickupInfoForm = (LclPickupInfoForm) form;
        String doorDellStatus = "false";
        HttpSession session = request.getSession();
        LclFileNumber fileNumber = new LclFileNumberDAO().findById(lclPickupInfoForm.getFileNumberId());
        LclSession lclSession = null != (LclSession) session.getAttribute("lclSession")
                ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        session.setAttribute("lclSession", lclSession);
        request.setAttribute("fileNumberId", lclPickupInfoForm.getFileNumberId());
        request.setAttribute("headerId", lclPickupInfoForm.getHeaderId());
        request.setAttribute("fileNumberStatus", request.getParameter("fileNumberStatus"));
        request.setAttribute("invoiceStatus", request.getParameter("invoiceStatus"));
        request.setAttribute("transType", request.getParameter("transType"));
        request.setAttribute("billToParty", request.getParameter("billToParty"));
        if ("M".equalsIgnoreCase(fileNumber.getStatus())) {
            doorDellStatus = (String) new LclCostChargeDAO().getDoorDelStatus(fileNumber.getFileNumber());
        }
        request.setAttribute("doorDellStatus", doorDellStatus);
        return mapping.findForward(CARRIER);
    }

    public ActionForward addPickupChargeToAcct(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclPickupInfoForm lclPickupInfoForm = (LclPickupInfoForm) form;
        HttpSession session = request.getSession();
        LclSession lclSession = null != (LclSession) session.getAttribute("lclSession")
                ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        User loginUser = getCurrentUser(request);
        String fileNumberId = request.getParameter("fileNumberId");
        String scac = request.getParameter("scac");
        String fax1 = request.getParameter("fax");
        String spAcct = request.getParameter("spAcct");
        String email1 = request.getParameter("email1");
        String phone1 = request.getParameter("phone1");
        String contactName = request.getParameter("contactName");
        String pickupHours = request.getParameter("pickupHours");
        String pickupReadyNote = request.getParameter("pickupReadyNote");
        String pickupCharge = request.getParameter("pickupCharge");
        //To ZipCode Values
        String toZipValues = request.getParameter("toZip");
        String pickupReadyDate = request.getParameter("pickupReadyDate");
        String cityStateZip = request.getParameter("fromZip");
        String toZip = "";
        String fromZip = "";
        String fromZipValues = "", costVendor = null;
        LCLBookingDAO bookingDAO = new LCLBookingDAO();
        LclBooking lclBooking = bookingDAO.getByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
        lclBooking.setPooPickup(true);
        lclBooking.setModifiedBy(loginUser);
        lclBooking.setModifiedDatetime(new Date());
        bookingDAO.getCurrentSession().clear();
        bookingDAO.saveOrUpdate(lclBooking);
        if (cityStateZip.indexOf("-") > 1) {
            fromZipValues = cityStateZip.substring(0, cityStateZip.indexOf("-"));
        }
        if (lclBooking != null && LCL_IMPORT_TYPE.equalsIgnoreCase(lclBooking.getBookingType())) {
            lclPickupInfoForm.setModuleName(LCL_IMPORT);
            fromZip = toZipValues;
            toZip = fromZipValues;
        } else {
            lclPickupInfoForm.setModuleName(LCL_EXPORT);
            fromZip = fromZipValues;
            toZip = toZipValues;
            costVendor = LoadLogisoftProperties.getProperty("PickupVendor");
        }
        BigDecimal weight = new BigDecimal(0.000);
        BigDecimal measure = new BigDecimal(0.000);
        BigDecimal pickUpCost = new BigDecimal(0.000);
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            List<LclBookingPiece> commList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
            for (LclBookingPiece lbp : commList) {
                if (lbp.getActualWeightImperial() != null) {
                    weight = weight.add(lbp.getActualWeightImperial());
                } else if (lbp.getBookedWeightImperial() != null) {
                    weight = weight.add(lbp.getBookedWeightImperial());
                }
                if (lbp.getActualVolumeImperial() != null) {
                    measure = measure.add(lbp.getActualVolumeImperial());
                } else if (lbp.getBookedVolumeImperial() != null) {
                    measure = measure.add(lbp.getBookedVolumeImperial());
                }
            }
        }
        CallCTSWebServices ctsweb = new CallCTSWebServices();
        lclSession.setXmlObjMap(null);
        lclSession.setCarrierList(null);
        String realPath = session.getServletContext().getRealPath("/xml/");
        String fileName = "ctsresponse" + session.getId() + ".xml";
        lclSession = ctsweb.processCTSWebService(lclSession, realPath, fileName, fromZip.trim(), toZip.trim(),
                pickupReadyDate, "" + weight, "" + measure, null, "CARRIER_COST", lclPickupInfoForm.getModuleName());
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
            LclBookingAc lclBookingAc = setPickUpCharge(lclPickupInfoForm, lclBooking,
                    new BigDecimal(pickupCharge), pickUpCost, loginUser, costVendor);
            LclBookingPad lclBookingPad = new LclBookingPadDAO().getByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
            request.setAttribute("lclBookingPad", lclBookingPad);
            request.setAttribute("lclBookingAc", lclBookingAc);
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
        request.setAttribute("lclPickupInfoForm", lclPickupInfoForm);
        request.setAttribute("pickupCost", pickUpCost);
        return mapping.findForward(PICKUPCHARGE);
    }

    public ActionForward validateCarrierRates(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclPickupInfoForm lclPickupInfoForm = (LclPickupInfoForm) form;
        Long fileId = lclPickupInfoForm.getFileNumberId();
        String errorMessage = null;
        if (CommonUtils.isNotEmpty(fileId)) {
            String hasBookingPiece = new LclBookingPieceDAO().hasBookingPiece(fileId);
            if ("false".equalsIgnoreCase(hasBookingPiece)) {
                errorMessage = "Please add Weight and Measure from Commodity";
            } else if (errorMessage == null) {
                String pickUpStatus = LoadLogisoftProperties.getProperty("application.enableCTS");
                if (pickUpStatus != null && !pickUpStatus.equals("") && "N".equalsIgnoreCase(pickUpStatus)) {
                    errorMessage = "CTS is disabled";
                } else {
                    HttpSession session = request.getSession();
                    LclSession lclSession = null != (LclSession) session.getAttribute("lclSession")
                            ? (LclSession) session.getAttribute("lclSession") : new LclSession();
                    lclSession.setXmlObjMap(null);
                    lclSession.setCarrierList(null);
                    session.setAttribute("lclSession", lclSession);
                    String fromZip = request.getParameter("fromZip");///Import fromZip is warehouse Zip,Export---pickUpZip
                    String pickupReadyDate = request.getParameter("pickupReadyDate");
                    String fileNumberId = request.getParameter("fileNumberId");
                    String toZip = request.getParameter("toZip");//Import toZip is DoorZip,Export--warehouseZip
                    BigDecimal weight = new BigDecimal(0.000);
                    BigDecimal measure = new BigDecimal(0.000);
                    if (CommonUtils.isNotEmpty(fileNumberId)) {
                        List<LclBookingPiece> commList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
                        for (LclBookingPiece lbp : commList) {
                            if (lbp.getActualWeightImperial() != null) {
                                weight = weight.add(lbp.getActualWeightImperial());
                            } else if (lbp.getBookedWeightImperial() != null) {
                                weight = weight.add(lbp.getBookedWeightImperial());
                            }
                            if (lbp.getActualVolumeImperial() != null) {
                                measure = measure.add(lbp.getActualVolumeImperial());
                            } else if (lbp.getBookedVolumeImperial() != null) {
                                measure = measure.add(lbp.getBookedVolumeImperial());
                            }
                        }
                    }
                    String realPath = session.getServletContext().getRealPath("/xml/");
                    String fileName = "ctsresponse" + session.getId() + ".xml";
                    CallCTSWebServices ctsweb = new CallCTSWebServices();
                    lclSession = ctsweb.processCTSWebService(lclSession, realPath, fileName, fromZip.trim(), toZip.trim(),
                            pickupReadyDate, "" + weight, "" + measure, "CARRIER_CHARGE", null, lclPickupInfoForm.getModuleName());
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

    public ActionForward getScacCodeList(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclPickupInfoForm lclPickupInfoForm = (LclPickupInfoForm) form;
        List scacCodelist = new LclBookingPadDAO().getScacCodeList(lclPickupInfoForm.getScac(), lclPickupInfoForm.getCarrier());
        request.setAttribute("scacCodelist", scacCodelist);
        if (CommonUtils.isNotEmpty(lclPickupInfoForm.getScac())) {
            request.setAttribute("scac", lclPickupInfoForm.getScac());
        }
        if (CommonUtils.isNotEmpty(lclPickupInfoForm.getCarrier())) {
            request.setAttribute("carrier", lclPickupInfoForm.getCarrier());
        }
        return mapping.findForward("scac");
    }

    public void setLclcontactRemarks(LclBookingPad lclBookingPad, LclPickupInfoForm lclPickupInfoForm,
            LclFileNumber lclFileNumber, HttpServletRequest request) throws Exception {
        User user = getCurrentUser(request);
        StringBuilder remarks = new StringBuilder();
        String updateRemarks = "UPDATED ->";
        //deliverycontact remarks
        if (lclBookingPad != null && lclBookingPad.getPickupContact() != null) {
            if (CommonUtils.notIn(lclBookingPad.getPickupContact().getCompanyName(), lclPickupInfoForm.getCompanyName())) {
                remarks.append("Ship To -> ").append(lclBookingPad.getPickupContact().getCompanyName()).append(" to ").append(lclPickupInfoForm.getCompanyName());
            }
            if (CommonUtils.notIn(lclBookingPad.getPickupContact().getAddress(), lclPickupInfoForm.getAddress())) {
                remarks.append(" Address -> ").append(lclBookingPad.getPickupContact().getAddress()).append(" to ").append(lclPickupInfoForm.getAddress());
            }
            if (CommonUtils.notIn(lclBookingPad.getPickupContact().getFax1(), lclPickupInfoForm.getFax1())) {
                remarks.append(" Fax -> ").append(lclBookingPad.getPickupContact().getFax1()).append(" to ").append(lclPickupInfoForm.getFax1()).append(", ");
            }
            if (CommonUtils.notIn(lclBookingPad.getPickupContact().getEmail1(), lclPickupInfoForm.getEmail1())) {
                remarks.append(" Contact Email -> ").append(lclBookingPad.getPickupContact().getEmail1()).append(" to ").append(lclPickupInfoForm.getEmail1());
            }
            if (CommonUtils.notIn(lclBookingPad.getPickupContact().getContactName(), lclPickupInfoForm.getContactName())) {
                remarks.append(" Contact Name -> ").append(lclBookingPad.getPickupContact().getContactName()).append(" to ").append(lclPickupInfoForm.getContactName());
            }
            if (CommonUtils.notIn(lclBookingPad.getPickupContact().getPhone1(), lclPickupInfoForm.getPhone1())) {
                remarks.append(" Phone -> ").append(lclBookingPad.getPickupContact().getPhone1()).append(" to ").append(lclPickupInfoForm.getPhone1());
            }
            if (CommonUtils.notIn(lclBookingPad.getPickupContact().getCity(), lclPickupInfoForm.getShipperCity())) {
                remarks.append(" City -> ").append(lclBookingPad.getPickupContact().getCity()).append(" to ").append(lclPickupInfoForm.getShipperCity());
            }
            if (CommonUtils.notIn(lclBookingPad.getPickupContact().getState(), lclPickupInfoForm.getShipperState())) {
                remarks.append(" State -> ").append(lclBookingPad.getPickupContact().getCity()).append(" to ").append(lclPickupInfoForm.getShipperState());
            }
            if (CommonUtils.notIn(lclBookingPad.getPickupContact().getZip(), lclPickupInfoForm.getShipperZip())) {
                remarks.append(" Zip -> ").append(lclBookingPad.getPickupContact().getZip()).append(" to ").append(lclPickupInfoForm.getShipperZip());
            }
        }
        if (lclBookingPad != null && lclBookingPad.getDeliveryContact() != null) {
            if (CommonUtils.notIn(lclBookingPad.getDeliveryContact().getCompanyName(), lclPickupInfoForm.getWhsecompanyName())) {
                remarks.append(" WhseName -> ").append(lclBookingPad.getDeliveryContact().getCompanyName()).append(" to ").append(lclPickupInfoForm.getWhsecompanyName());
            }
            if (CommonUtils.notIn(lclBookingPad.getDeliveryContact().getAddress(), lclPickupInfoForm.getWhseAddress())) {
                remarks.append(" WhseAddress -> ").append(lclBookingPad.getDeliveryContact().getAddress()).append(" to ").append(lclPickupInfoForm.getWhseAddress());
            }
            if (CommonUtils.notIn(lclBookingPad.getDeliveryContact().getCity(), lclPickupInfoForm.getWhseCity())) {
                remarks.append(" WhseCity -> ").append(lclBookingPad.getDeliveryContact().getCity()).append(" to ").append(lclPickupInfoForm.getWhseCity());
            }
            if (CommonUtils.notIn(lclBookingPad.getDeliveryContact().getState(), lclPickupInfoForm.getWhseState())) {
                remarks.append(" WhseState -> ").append(lclBookingPad.getDeliveryContact().getState()).append(" to ").append(lclPickupInfoForm.getWhseState());
            }
            if (CommonUtils.notIn(lclBookingPad.getDeliveryContact().getPhone1(), lclPickupInfoForm.getWhsePhone())) {
                remarks.append(" Whsephone -> ").append(lclBookingPad.getDeliveryContact().getPhone1()).append(" to ").append(lclPickupInfoForm.getWhsePhone());
            }
        }
        if (CommonUtils.isNotEmpty(remarks)) {
            updateRemarks = updateRemarks.concat(remarks.toString());
            new LclRemarksDAO().insertLclRemarks(lclFileNumber, REMARKS_DR_AUTO_NOTES, updateRemarks, user);
        }
    }

    public ActionForward ShipperSearch(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("searchByValue", request.getParameter("searchByValue"));
        return mapping.findForward("ShipperSearch");
    }

    public LclBookingAc setPickUpCharge(LclPickupInfoForm pickUpForm, LclBooking lclBooking, BigDecimal pickUpCharge,
            BigDecimal pickUpCost, User loginUser, String costVendor) throws Exception {
        GlMappingDAO glMappingDAO = new GlMappingDAO();
        Date now = new Date();
        Boolean relsToInvoice = false;
        String shipmentType = "", chargeCode = "", arBillToParty = "";
        if (LCL_IMPORT_TYPE.equalsIgnoreCase(lclBooking.getBookingType())) {
            shipmentType = LCL_SHIPMENT_TYPE_IMPORT;
            chargeCode = CHARGE_CODE_DOOR;
            arBillToParty = "A";
            pickUpForm.setModuleName(LCL_IMPORT);
            relsToInvoice = true;
        } else {
            pickUpForm.setModuleName(LCL_EXPORT);
            shipmentType = LCL_SHIPMENT_TYPE_EXPORT;
            chargeCode = LoadLogisoftProperties.getProperty("InlandChargeCode");
            arBillToParty = lclBooking.getBillToParty();
        }
        LclBookingAc lclBookingAc = lclCostChargeDAO.getLclBookingAcByChargeCode(lclBooking.getFileNumberId(), chargeCode);
        if (lclBookingAc == null) {
            lclBookingAc = new LclBookingAc();
            lclBookingAc.setEnteredDatetime(now);
            lclBookingAc.setEnteredBy(loginUser);
            GlMapping arGlMapping = glMappingDAO.findByChargeCode(chargeCode, shipmentType, TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
            lclBookingAc.setArglMapping(arGlMapping);
            lclBookingAc.setBundleIntoOf(Boolean.FALSE);
            lclBookingAc.setPrintOnBl(Boolean.TRUE);
            BigDecimal bigDecimal = new BigDecimal(0.00);
            lclBookingAc.setRatePerWeightUnit(bigDecimal);
            lclBookingAc.setRatePerVolumeUnit(bigDecimal);
            lclBookingAc.setRateFlatMinimum(bigDecimal);
            lclBookingAc.setCostWeight(bigDecimal);
            lclBookingAc.setCostMeasure(bigDecimal);
            lclBookingAc.setCostMinimum(bigDecimal);
            lclBookingAc.setAdjustmentAmount(bigDecimal);
            lclBookingAc.setRelsToInv(relsToInvoice);
            lclBookingAc.setArBillToParty(arBillToParty);
        }
        lclBookingAc.setLclFileNumber(lclBooking.getLclFileNumber());
        lclBookingAc.setArAmount(pickUpCharge);
        lclBookingAc.setRatePerUnit(pickUpCharge);
        lclBookingAc.setApAmount(pickUpCost);
        lclBookingAc.setCostFlatrateAmount(pickUpCost);
        lclBookingAc.setDeleted(Boolean.TRUE);
        GlMapping apGlmapping = glMappingDAO.findByChargeCode(chargeCode, shipmentType, TRANSACTION_TYPE_ACCRUALS);
        lclBookingAc.setApglMapping(apGlmapping);
        if (LCL_EXPORT.equalsIgnoreCase(pickUpForm.getModuleName())) {
            lclBookingAc.setArBillToParty(arBillToParty);
        }
        if (CommonUtils.isNotEmpty(costVendor)) {
            TradingPartner costAcct = new TradingPartnerDAO().findById(costVendor);
            if (null != costAcct) {
                pickUpForm.setCostVendorAcct(costAcct.getAccountName());
                pickUpForm.setCostVendorNo(costAcct.getAccountno());
                lclBookingAc.setSupAcct(costAcct);
                lclBookingAc.setDeleted(Boolean.FALSE);
            } else {
                lclBookingAc.setSupAcct(null);
            }
        } else {
            lclBookingAc.setSupAcct(null);
        }
        lclBookingAc.setManualEntry(Boolean.TRUE);
        lclBookingAc.setTransDatetime(now);
        lclBookingAc.setRatePerUnitUom("FL");
        lclBookingAc.setRateUom("I");
        lclBookingAc.setModifiedBy(loginUser);
        lclBookingAc.setModifiedDatetime(now);
        lclCostChargeDAO.saveOrUpdate(lclBookingAc);
        return lclBookingAc;
    }
}

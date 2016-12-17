package com.gp.cong.logisoft.lcl.bc;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.lcl.dwr.LclBlChargesCalculation;
import com.gp.cong.lcl.dwr.LclChargesCalculation;
import com.gp.cong.lcl.dwr.LclSession;
import com.gp.cong.lcl.model.ExportVoyageSearchModel;
import com.gp.cong.logisoft.beans.BookingChargesBean;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.Warehouse;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cong.logisoft.domain.lcl.LclBookingExport;
import com.gp.cong.logisoft.domain.lcl.LclBookingImport;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceDetail;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceWhse;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclRemarks;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlAc;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPiece;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.WarehouseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBlAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingExportDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPlanDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclConsolidateDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LclBLPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.commodityTypeDAO;
import com.gp.cong.logisoft.lcl.model.LclBookingPlanBean;
import com.gp.cong.logisoft.lcl.model.LclBookingVoyageBean;
import com.gp.cong.logisoft.lcl.report.OceanManifestBean;
import com.gp.cvst.logisoft.struts.form.lcl.LCLBookingForm;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.PropertyUtils;

/**
 *
 * @author Mei
 */
public class ExportBookingUtils implements LclCommonConstant {

    public void setTrmandEciPortCode(LCLBookingForm bookingForm, LclBooking booking, LclUtils lclUtils) throws Exception {
        String pooUnloCode = booking.getPortOfOrigin() != null ? booking.getPortOfOrigin().getUnLocationCode() : "";
        String polUnloCode = booking.getPortOfLoading() != null ? booking.getPortOfLoading().getUnLocationCode() : "";
        String podUnloCode = booking.getPortOfDestination() != null ? booking.getPortOfDestination().getUnLocationCode() : "";
        String fdUnloCode = booking.getFinalDestination() != null ? booking.getFinalDestination().getUnLocationCode() : "";
        String rateType = "R".equalsIgnoreCase(booking.getRateType()) ? "Y" : booking.getRateType();
        List l = lclUtils.getTrmNumandEciPortCode(pooUnloCode, polUnloCode, podUnloCode, fdUnloCode, rateType);
        for (Object row : l) {
            Object[] col = (Object[]) row;
            if (col[2].toString().equalsIgnoreCase("POO")) {
                bookingForm.setPooTrmNum((String) col[0]);
            }
            if (col[2].toString().equalsIgnoreCase("POL")) {
                bookingForm.setPolTrmNum((String) col[0]);
            }
            if (col[2].toString().equalsIgnoreCase("POD")) {
                bookingForm.setPodEciPortCode((String) col[0]);
            }
            if (col[2].toString().equalsIgnoreCase("FD")) {
                bookingForm.setFdEciPortCode((String) col[0]);
                bookingForm.setFdEngmet((String) col[1]);
            }
        }
    }

    public ExportVoyageSearchModel getPickedExpVoyageDetails(Long fileId, String serviceType) throws Exception {
        ExportVoyageSearchModel pickedVoyageDetails = new LclUnitSsDAO().getPickedVoyageByVessel(fileId, serviceType);
        return pickedVoyageDetails;
    }

    public void setExpVoyageDetails(Long fileId, String serviceType, LCLBookingForm lclBookingForm, HttpServletRequest request) throws Exception {
        LclConsolidateDAO consolidateDao = new LclConsolidateDAO();
        ExportVoyageSearchModel pickedDetails = null;
        if (consolidateDao.isConsoildateFile(fileId.toString())) {
            String[] file = consolidateDao.getParentConsolidateFile(fileId.toString());
            pickedDetails = getPickedExpVoyageDetails(Long.parseLong(file[0]), serviceType);
            if (null == pickedDetails) {
                pickedDetails = getPickedExpVoyageDetails(fileId, serviceType);
            }
        } else {
            pickedDetails = getPickedExpVoyageDetails(fileId, serviceType);
        }
        if (pickedDetails != null && CommonUtils.isNotEmpty(pickedDetails.getUnitSsId())) {
            LclUnitSs lclUnitSs = new LclUnitSsDAO().findById(Long.parseLong(pickedDetails.getUnitSsId()));
            lclBookingForm.setCob(lclUnitSs.getCob());
            request.setAttribute("unitNum", lclUnitSs.getLclUnit().getUnitNo());
            request.setAttribute("unitSize", lclUnitSs.getLclUnit().getUnitType().getDescription());
            request.setAttribute("pickOnVoyage", lclUnitSs.getLclSsHeader().getVesselSsDetail());
            request.setAttribute("lclssheader", lclUnitSs.getLclSsHeader());
            request.setAttribute("blUnitCob", lclUnitSs.getCob());
        }
    }

    public void setLclBookingExport(LCLBookingForm lclBookingForm, LclFileNumber lclFileNumber,
            HttpServletRequest request, User loginUser) throws Exception {
        lclFileNumber = lclFileNumber == null ? lclBookingForm.getLclFileNumber() : lclFileNumber;
        HttpSession session = request.getSession();
        LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        Warehouse wareHouse = null;
        LclBookingExport lclBookingExport = new LclBookingExportDAO().getByProperty("lclFileNumber.id", lclFileNumber.getId());
        if (null == lclBookingExport) {
            lclBookingExport = new LclBookingExport();
            lclBookingExport.setEnteredBy(loginUser);
            lclBookingExport.setEnteredDatetime(new Date());
            lclBookingExport.setFileNumberId(lclFileNumber.getId());
            lclBookingExport.setDeliverPickup("P");
            lclBookingExport.setDeliverPickupDatetime(new Date());
        }
        String unLocation = "";
        if ("T".equalsIgnoreCase(lclBookingForm.getLclBooking().getBookingType())) {
            unLocation = CommonUtils.isNotEmpty(lclBookingForm.getPortExit()) ? lclBookingForm.getPortExit() : "";
        } else {
            unLocation = CommonUtils.isNotEmpty(lclBookingForm.getPortOfOrigin()) ? lclBookingForm.getPortOfOrigin() : "";
        }
        if (!"".equalsIgnoreCase(unLocation)) {
            unLocation = unLocation.substring(unLocation.indexOf("(") + 1, unLocation.indexOf(")"));
            RefTerminal terminal = new RefTerminalDAO().getTerminalByUnLocation(unLocation, "Y");
            wareHouse = new WarehouseDAO().getWareHouseBywarehsNo(terminal != null ? "W" + terminal.getTrmnum() : "");
        }
        lclBookingExport.setOrginWarehouse(null != wareHouse ? wareHouse : null);
        lclBookingExport.setRtAgentAcct(null);
        lclBookingExport.setModifiedBy(loginUser);
        lclBookingExport.setModifiedDatetime(new Date());
        lclBookingExport.setCfcl(null != lclBookingForm.getCfcl() ? lclBookingForm.getCfcl() : false);
        lclBookingExport.setAes(null != lclBookingForm.getAesBy() ? lclBookingForm.getAesBy() : false);
        lclBookingExport.setUps(null != lclBookingForm.getUps() ? lclBookingForm.getUps() : false);
        lclBookingExport.setNoBlRequired(null != lclBookingForm.getNoBLRequired()? lclBookingForm.getNoBLRequired(): false);
        lclBookingExport.setCfclAcctNo(CommonUtils.isNotEmpty(lclBookingForm.getCfclAcctNo())
                ? new TradingPartnerDAO().findById(lclBookingForm.getCfclAcctNo()) : null);
        lclBookingExport.setStorageDatetime(CommonUtils.isNotEmpty(lclBookingForm.getStorageDatetime())
                ? DateUtils.parseDate(lclBookingForm.getStorageDatetime(), "dd-MMM-yyyy hh:mm:ss a") : null);
        lclBookingExport.setIncludeDestfees(lclSession.isIncludeDestfees());
        new LclBookingExportDAO().saveOrUpdate(lclBookingExport);
        request.setAttribute("lclBookingExport", lclBookingExport);
    }

    public void setUpcomingSailings(LclBooking lclBooking, HttpServletRequest request) throws Exception {
        LclBookingPlanDAO bookingPlanDAO = new LclBookingPlanDAO();
        List<LclBookingVoyageBean> upcomingSailings = null;
        String cfcl;
        if (null != lclBooking.getLclFileNumber() && null != lclBooking.getLclFileNumber().getLclBookingExport()) {
            cfcl = lclBooking.getLclFileNumber().getLclBookingExport().isCfcl() ? "C" : "E";
        } else {
            cfcl = "E";
        }
        if ("T".equalsIgnoreCase(lclBooking.getBookingType())) {
            LclBookingImport bookingImport = lclBooking.getLclFileNumber().getLclBookingImport();
            if (lclBooking.getPortOfDestination() != null && bookingImport.getUsaPortOfExit() != null
                    && bookingImport.getForeignPortOfDischarge() != null && lclBooking.getFinalDestination() != null) {
                LclBookingPlanBean bookingPlanBean = bookingPlanDAO.getRelayOverride(bookingImport.getUsaPortOfExit().getId(),
                        bookingImport.getUsaPortOfExit().getId(), bookingImport.getForeignPortOfDischarge().getId(),
                        bookingImport.getForeignPortOfDischarge().getId(), 0);
                if (bookingPlanBean != null) {
                    upcomingSailings = bookingPlanDAO.getUpComingSailingsSchedule(bookingImport.getUsaPortOfExit().getId(),
                            bookingImport.getUsaPortOfExit().getId(), bookingImport.getForeignPortOfDischarge().getId(),
                            bookingImport.getForeignPortOfDischarge().getId(), "V", bookingPlanBean, cfcl);
                }
            }
        } else {
            if (lclBooking.getPortOfOrigin() != null && lclBooking.getFinalDestination() != null) {

                if (!lclBooking.getRelayOverride()) {
                    LclBookingPlanBean bookingPlanBean = bookingPlanDAO.getRelay(lclBooking.getPortOfOrigin().getId(),
                            lclBooking.getFinalDestination().getId(), "N");
                    if (bookingPlanBean != null) {
                        upcomingSailings = bookingPlanDAO.getUpComingSailingsSchedule(lclBooking.getPortOfOrigin().getId(), bookingPlanBean.getPol_id(), bookingPlanBean.getPod_id(),
                                lclBooking.getFinalDestination().getId(), "V", bookingPlanBean, cfcl);
                    }
                } else if (lclBooking.getPortOfLoading() != null && lclBooking.getPortOfDestination() != null
                        && lclBooking.getRelayOverride()) {
                    LclBookingPlanBean bookingPlanBean = bookingPlanDAO.getRelayOverride(lclBooking.getPortOfOrigin().getId(),
                            lclBooking.getPortOfLoading().getId(), lclBooking.getPortOfDestination().getId(),
                            lclBooking.getFinalDestination().getId(), 0);
                    if (bookingPlanBean != null) {
                        upcomingSailings = bookingPlanDAO.getUpComingSailingsSchedule(lclBooking.getPortOfLoading().getId(),
                                lclBooking.getPortOfLoading().getId(), lclBooking.getPortOfDestination().getId(),
                                lclBooking.getPortOfDestination().getId(), "V", bookingPlanBean, cfcl);
                    }
                }
            }
        }
        request.setAttribute("voyageList", upcomingSailings);
        request.setAttribute("voyageAction", false);
    }

    public void updateConsolidateManualCharge(List conoslidatelist, Long current_file_id,
            String billToParty, HttpServletRequest request) throws Exception {
        List<LclBookingAc> consolidateManualChargeList = new LclCostChargeDAO().getConsolidatedChargeME(conoslidatelist);
        List<BookingChargesBean> consolidateAutoChargeList = new LclCostChargeDAO().getConsolidatedAutoCharge(conoslidatelist);

        if (CommonUtils.isNotEmpty(consolidateAutoChargeList)) {
            for (BookingChargesBean charge_bean : consolidateAutoChargeList) {
                if (!charge_bean.getAdjustmentAmt().equals(BigDecimal.ZERO)) {
                    new LclBlAcDAO().updateBLAdjustmentWithBlueScreenCode(charge_bean.getBlueScreencode(),
                            charge_bean.getChargeCode(), current_file_id, charge_bean.getAdjustmentAmt());
                }
            }
        }
        Map<String, LclBlAc> manualFlatCharge = new HashMap<String, LclBlAc>();
        BigDecimal weightUnit, volumeUnit, flatMin, arAmount;

        if (CommonUtils.isNotEmpty(consolidateManualChargeList)) {
            for (LclBookingAc bookingAc : consolidateManualChargeList) {
                if (manualFlatCharge.containsKey(bookingAc.getArglMapping().getChargeCode())) {
                    LclBlAc blAc = manualFlatCharge.get(bookingAc.getArglMapping().getChargeCode());
                    weightUnit = blAc.getRatePerWeightUnit().add(bookingAc.getRatePerWeightUnit());
                    volumeUnit = blAc.getRatePerVolumeUnit().add(bookingAc.getRatePerVolumeUnit());
                    flatMin = blAc.getRateFlatMinimum().add(bookingAc.getRateFlatMinimum());
                    arAmount = blAc.getRatePerUnit().add(bookingAc.getRatePerUnit());
                    blAc.setRatePerWeightUnit(weightUnit);
                    blAc.setRatePerVolumeUnit(volumeUnit);
                    blAc.setRateFlatMinimum(flatMin);
                    blAc.setRatePerUnit(arAmount);
                    manualFlatCharge.put(blAc.getArglMapping().getChargeCode(), blAc);
                } else {
                    LclBlAc blAc = new LclBlAc();
                    PropertyUtils.copyProperties(blAc, bookingAc);
                    blAc.setId(null);
                    manualFlatCharge.put(blAc.getArglMapping().getChargeCode(), blAc);
                }
            }
        }
        for (Map.Entry ch : manualFlatCharge.entrySet()) {
            LclBlAc blAc = (LclBlAc) ch.getValue();
            blAc.setArBillToParty(billToParty);
            blAc.setApBillToParty(billToParty);
            this.saveCalculatedWeightMeasureManualCharge(current_file_id, blAc, request);
        }
    }

    public void updateConsolidationBLCharges(Long current_file_id, String remarks,
            User loginUser, HttpServletRequest request) throws Exception {
        LclBl lclbl = new LCLBlDAO().getByProperty("lclFileNumber.id", current_file_id);
        if (lclbl == null) {
            current_file_id = new LCLBlDAO().findConsolidateBl(current_file_id);
            lclbl = new LCLBlDAO().getByProperty("lclFileNumber.id", current_file_id);
        }
        new LclBlAcDAO().deleteBlAcByFileNumber(current_file_id);
        List conoslidatelist = new LclConsolidateDAO().getConsolidatesFiles(current_file_id);
        String billToParty = "B".equalsIgnoreCase(lclbl.getBillingType()) ? "F" : lclbl.getBillToParty();
        List<LclBlPiece> lclBlPiece = new LclBLPieceDAO().findByProperty("lclFileNumber.id", current_file_id);
        BigDecimal cft, cbm, lbs, kgs;
        int piece;
        for (LclBlPiece lbp : lclBlPiece) {
            if (lbp.equals(lclBlPiece.get(lclBlPiece.size() - 1))) {  // Updating the Piece weight for first commodity in BL.
                piece = !CommonUtils.isEmpty(lbp.getActualPieceCount()) ? lbp.getActualPieceCount() : lbp.getBookedPieceCount();
                cft = !CommonUtils.isEmpty(lbp.getActualVolumeImperial()) ? lbp.getActualVolumeImperial() : lbp.getBookedVolumeImperial();
                cbm = !CommonUtils.isEmpty(lbp.getActualVolumeMetric()) ? lbp.getActualVolumeMetric() : lbp.getBookedVolumeMetric();
                lbs = !CommonUtils.isEmpty(lbp.getActualWeightImperial()) ? lbp.getActualWeightImperial() : lbp.getBookedWeightImperial();
                kgs = !CommonUtils.isEmpty(lbp.getActualWeightMetric()) ? lbp.getActualWeightMetric() : lbp.getBookedWeightMetric();

                List<OceanManifestBean> bookingPiece = new LclBookingPieceDAO().getTotalWeightForComm(conoslidatelist);
                OceanManifestBean totalcommodity = bookingPiece.get(bookingPiece.size() - 1);
                lbp.setActualPieceCount(totalcommodity.getPiece() + piece);
                lbp.setActualVolumeImperial(new BigDecimal(null != totalcommodity.getCft() ? totalcommodity.getCft() : 0.00).add(null != cft ? cft : BigDecimal.ZERO));
                lbp.setActualVolumeMetric(new BigDecimal(null != totalcommodity.getCbm() ? totalcommodity.getCbm() : 0.00).add(null != cbm ? cbm : BigDecimal.ZERO));
                lbp.setActualWeightImperial(new BigDecimal(null != totalcommodity.getLbs() ? totalcommodity.getLbs() : 0.00).add(null != lbs ? lbs : BigDecimal.ZERO));
                lbp.setActualWeightMetric(new BigDecimal(null != totalcommodity.getKgs() ? totalcommodity.getKgs() : 0.00).add(null != kgs ? kgs : BigDecimal.ZERO));
                new LclBookingPieceDAO().saveOrUpdate(lbp);
            }
        }
        conoslidatelist.add(current_file_id);
        List lclBlPiecesList = new LclBLPieceDAO().findByProperty("lclFileNumber.id", current_file_id);
        String rateType = "R".equalsIgnoreCase(lclbl.getRateType()) ? "Y" : lclbl.getRateType();
        String finalDestination = null != lclbl.getFinalDestination()
                ? lclbl.getFinalDestination().getUnLocationCode() : lclbl.getPortOfDestination().getUnLocationCode();
        // Recalculation the BL Rates.//
        new LclBlChargesCalculation().calculateRates(lclbl.getPortOfOrigin().getUnLocationCode(), finalDestination,
                lclbl.getPortOfLoading().getUnLocationCode(), lclbl.getPortOfDestination().getUnLocationCode(), current_file_id,
                lclBlPiecesList, loginUser, "", lclbl.getInsurance() ? "Y" : "N", lclbl.getValueOfGoods(), rateType, "C", null, null,
                null, null, null, null, lclbl.getBillingType(), null, lclbl.getBillToParty(), request, true, false);
        updateConsolidateManualCharge(conoslidatelist, current_file_id, billToParty, request); // updating manual charges.
        if (!"".equalsIgnoreCase(remarks)) {
            for (Object fileId : conoslidatelist) {
                new LclRemarksDAO().insertLclRemarks(Long.parseLong(fileId.toString()), REMARKS_BL_AUTO_NOTES, remarks, loginUser.getUserId());
            }
        }
    }

    public void setDataForConsolidationBL(Long fileId, HttpServletRequest request) throws Exception {
        LclConsolidateDAO consolidateDAO = new LclConsolidateDAO();
        LCLBlDAO blDAO = new LCLBlDAO();
        if (consolidateDAO.isConsolidatedByFileAB(fileId)) {
            Long consolidate_FileId = blDAO.findConsolidateBl(fileId);
            if (CommonUtils.isNotEmpty(consolidate_FileId)) {
                request.setAttribute("consolidateBl", blDAO.findById(consolidate_FileId));
            }
        }
    }

    public void addRemarksForConsolidationFiles(String current_file_id, String remarks, String user_id) throws Exception {
        if (CommonUtils.isNotEmpty(current_file_id) && CommonUtils.isNotEmpty(user_id)) {
            List conoslidatelist = new LclConsolidateDAO().getConsolidatesFiles(Long.parseLong(current_file_id));
            conoslidatelist.add(current_file_id);
            if (!"".equalsIgnoreCase(remarks)) {
                for (Object fileId : conoslidatelist) {
                    new LclRemarksDAO().insertLclRemarks(Long.parseLong(fileId.toString()), REMARKS_DR_AUTO_NOTES, remarks, Integer.parseInt(user_id));
                }
            }
        }
    }

    public void refreshRates(LCLBookingForm bookingForm, HttpServletRequest request,
            Long fileId, LclBooking lclBooking, User loginUser, List<LclBookingPiece> bookingPieceList) throws Exception {
        LclChargesCalculation lclChargesCalculation = new LclChargesCalculation();
        String rateType = "R".equalsIgnoreCase(bookingForm.getRateType()) ? "Y" : bookingForm.getRateType();
        String fromZip = "";
        if (bookingForm.getDoorOriginCityZip() != null && !bookingForm.getDoorOriginCityZip().trim().equals("")) {
            String[] zip = bookingForm.getDoorOriginCityZip().split("-");
            fromZip = zip[0];
        }
        String pickupReadyDate = new LCLBookingDAO().getPickupReadyDate(fileId);
        lclChargesCalculation.calculateRates(bookingForm.getOriginCode(), bookingForm.getDestinationCode(), bookingForm.getPolCode(),
                bookingForm.getPodCode(), fileId, bookingPieceList, loginUser,
                bookingForm.getPooDoor(), bookingForm.getInsurance(), bookingForm.getLclBooking().getValueOfGoods(),
                rateType, "C", null, pickupReadyDate, fromZip, null, bookingForm.getCalcHeavy(),
                bookingForm.getDeliveryMetro(), bookingForm.getPcBoth(), null, "", request, lclBooking.getBillToParty());
        request.setAttribute("highVolumeMessage", lclChargesCalculation.getHighVolumeMessage());
        HttpSession session = request.getSession();
        LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        request.setAttribute("lclSession", lclSession);
        if (CommonUtils.isNotEmpty(lclChargesCalculation.getHighVolumeMessage())) {
            LclRemarksDAO remarksDAO = new LclRemarksDAO();
            LclRemarks lclRemarks = remarksDAO.getRemarks(fileId, "AutoRates", null);
            if (lclRemarks == null) {
                lclRemarks = new LclRemarks();
                lclRemarks.setEnteredDatetime(new Date());
                lclRemarks.setEnteredBy(loginUser);
            }
            lclRemarks.setLclFileNumber(lclBooking.getLclFileNumber());
            lclRemarks.setType("AutoRates");
            lclRemarks.setRemarks(lclChargesCalculation.getHighVolumeMessage());
            lclRemarks.setModifiedBy(loginUser);
            lclRemarks.setModifiedDatetime(new Date());
            remarksDAO.saveOrUpdate(lclRemarks);
        }
        request.setAttribute("lclBooking", lclBooking);
        Ports ports = new PortsDAO().getByProperty("unLocationCode", lclBooking.getFinalDestination().getUnLocationCode());
        String engmet = "";
        if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
            engmet = ports.getEngmet();
        }
        LclCostChargeDAO costChargeDAO = new LclCostChargeDAO();
        LclUtils utils = new LclUtils();
        List<LclBookingAc> chargeList = costChargeDAO.getLclCostByFileNumberAsc(fileId, LclCommonConstant.LCL_EXPORT);
        utils.setWeighMeasureForBooking(request, bookingPieceList, lclChargesCalculation.getPorts());
        utils.setRolledUpChargesForBooking(chargeList, request, fileId, costChargeDAO, bookingPieceList,
                bookingForm.getPcBoth(), engmet, "No");
    }

    public void calculateSpotRate(Long file_number_id, LclBooking lclBooking, String billing_type, String spot_Rate_cbm,
            String Spot_rate_cft, String rateType, Boolean isOnlyOcnfrt, Boolean spotCheckBottom,
            String spotComment, String spotRateCommodity, HttpServletRequest request,
            List<LclBookingPiece> lclBookingPiecesList, String engmet) throws Exception {
        String rate_uom = "";
        Spot_rate_cft = "".equalsIgnoreCase(Spot_rate_cft) ? "0.00" : Spot_rate_cft;
        spot_Rate_cbm = "".equalsIgnoreCase(spot_Rate_cbm) ? "0.00" : spot_Rate_cbm;
        Double CFT = 0.00, CBM = 0.00, OCN_CFT = 0.00, OCN_CBM = 0.00;
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        LclBookingAc ocean_Freight_Rate = lclCostChargeDAO.manaualChargeValidate(file_number_id, "OCNFRT", false);
        User user = (User) request.getSession().getAttribute("loginuser");
        LclBookingPiece commodity = lclBookingPiecesList.isEmpty() ? new LclBookingPiece() : lclBookingPiecesList.get(0);
        if ("Y".equalsIgnoreCase(request.getParameter("updateTariff"))) {
            commodity.setCommodityType(new commodityTypeDAO().getByProperty("code", spotRateCommodity));
            new LclBookingPieceDAO().update(commodity);
        }
        if (null == ocean_Freight_Rate) {
            lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", file_number_id);

            String origin = null != lclBooking.getPortOfOrigin() ? lclBooking.getPortOfOrigin().getUnLocationCode() : "";
            String pol = null != lclBooking.getPortOfLoading() ? lclBooking.getPortOfLoading().getUnLocationCode() : "";
            String pod = null != lclBooking.getPortOfDestination() ? lclBooking.getPortOfDestination().getUnLocationCode() : "";
            String fd = null != lclBooking.getFinalDestination() ? lclBooking.getFinalDestination().getUnLocationCode() : "";

            new LclChargesCalculation().calculateRates(origin, fd, pol, pod, file_number_id, lclBookingPiecesList,
                    user, lclBooking.getPooPickup().toString(), lclBooking.getInsurance().toString(), lclBooking.getValueOfGoods(), rateType,
                    "C", null, "", "", null, false, lclBooking.getDeliveryMetro(), lclBooking.getBillingType(), null,
                    "", request, lclBooking.getBillToParty());
        }
        ocean_Freight_Rate = lclCostChargeDAO.manaualChargeValidate(file_number_id, "OCNFRT", false);
        List<Object[]> rates_list = lclCostChargeDAO.getBookingSpotRateCharge(file_number_id);
        for (Object[] obj : rates_list) {
            CFT = Double.parseDouble(obj[0].toString());
            CBM = Double.parseDouble(obj[1].toString());
            OCN_CFT = Double.parseDouble(obj[2].toString());
            OCN_CBM = Double.parseDouble(obj[3].toString());
        }
        LclBookingAc TT_rev_charge = lclCostChargeDAO.getTTCharges(file_number_id, false);
        BigDecimal total_weight = BigDecimal.ZERO;
        BigDecimal total_measure = BigDecimal.ZERO;
        BigDecimal comm_measure = BigDecimal.ZERO;
        BigDecimal comm_weight = BigDecimal.ZERO;
        if ("E".equalsIgnoreCase(engmet)) {
            comm_measure = commodity.getActualVolumeImperial() != null && commodity.getActualVolumeImperial().compareTo(BigDecimal.ZERO) > 0
                    ? commodity.getActualVolumeImperial() : commodity.getBookedVolumeImperial();
            comm_weight = commodity.getActualWeightImperial() != null && commodity.getActualWeightImperial().compareTo(BigDecimal.ZERO) > 0
                    ? commodity.getActualWeightImperial() : commodity.getBookedWeightImperial();
        } else {
            comm_measure = commodity.getActualVolumeMetric() != null && commodity.getActualVolumeMetric().compareTo(BigDecimal.ZERO) > 0
                    ? commodity.getActualVolumeMetric() : commodity.getBookedVolumeMetric();
            comm_weight = commodity.getActualWeightMetric() != null && commodity.getActualWeightMetric().compareTo(BigDecimal.ZERO) > 0
                    ? commodity.getActualWeightMetric() : commodity.getBookedWeightMetric();
        }
        if (CommonUtils.isNotEmpty(Spot_rate_cft)) {
            lclBooking.setSpotWmRate(new BigDecimal(Spot_rate_cft));
            rate_uom = "M";
            Double calculated_cft = 0.00;
            if (isOnlyOcnfrt) {
                calculated_cft = null != TT_rev_charge ? Double.parseDouble(Spot_rate_cft)
                        - TT_rev_charge.getRatePerVolumeUnit().doubleValue() : Double.parseDouble(Spot_rate_cft);
            } else {
                calculated_cft = Double.parseDouble(Spot_rate_cft) - CFT;
            }
            calculated_cft = calculated_cft < 0 ? 0.00 : calculated_cft;
            ocean_Freight_Rate.setRatePerVolumeUnit(new BigDecimal(calculated_cft));
            total_measure = new BigDecimal(calculated_cft).multiply(comm_measure);
        }
        if (CommonUtils.isNotEmpty(spot_Rate_cbm)) {
            lclBooking.setSpotRateMeasure(new BigDecimal(spot_Rate_cbm));
            rate_uom = "W";
            Double calculated_cbm = 0.00;
            if (isOnlyOcnfrt) {
                calculated_cbm = null != TT_rev_charge ? Double.parseDouble(spot_Rate_cbm)
                        - TT_rev_charge.getRatePerWeightUnit().doubleValue() : Double.parseDouble(spot_Rate_cbm);
            } else {
                calculated_cbm = Double.parseDouble(spot_Rate_cbm) - CBM;
            }
            calculated_cbm = calculated_cbm < 0 ? 0.00 : calculated_cbm;
            total_weight = new BigDecimal(calculated_cbm).multiply(comm_weight);
            total_weight = new BigDecimal(calculated_cbm).divide(new BigDecimal(engmet.equalsIgnoreCase("E") ? 100 : 1000));
            ocean_Freight_Rate.setRatePerWeightUnit(new BigDecimal(calculated_cbm));
        }
        if (total_measure.doubleValue() > total_weight.doubleValue()
                && total_measure.doubleValue() > ocean_Freight_Rate.getRateFlatMinimum().doubleValue()) {
            ocean_Freight_Rate.setArAmount(total_measure.setScale(2, RoundingMode.HALF_DOWN));
            ocean_Freight_Rate.setRatePerUnitUom("FRV");
        } else if (total_weight.doubleValue() > total_measure.doubleValue()
                && total_weight.doubleValue() > ocean_Freight_Rate.getRateFlatMinimum().doubleValue()) {
            ocean_Freight_Rate.setArAmount(total_weight.setScale(2, RoundingMode.HALF_DOWN));
            ocean_Freight_Rate.setRatePerUnitUom("FRW");
        } else {
            ocean_Freight_Rate.setArAmount(ocean_Freight_Rate.getRateFlatMinimum().setScale(2, RoundingMode.HALF_DOWN));
            ocean_Freight_Rate.setRatePerUnitUom("FRM");
        }
        lclCostChargeDAO.update(ocean_Freight_Rate);
        lclCostChargeDAO.getSession().clear();
        lclBooking.setSpotRateBottom(spotCheckBottom);
        lclBooking.setSpotOfRate(isOnlyOcnfrt);
        lclBooking.setSpotRateUom("".equalsIgnoreCase(rate_uom) ? null : rate_uom);
        lclBooking.setSpotComment(spotComment);
        lclBooking.setSpotRate(true);
        new LCLBookingDAO().saveOrUpdate(lclBooking);
    }

    public List<LclBookingAc> setLabelCharges(List<LclBookingAc> chargeList,
            List<LclBookingPiece> lclCommodityList, String engmet) {
        List<LclBookingAc> formatChargeList = null;
        if (lclCommodityList.size() == 1) {
            formatChargeList = getFormattedLabelCharges(lclCommodityList, chargeList, engmet);
        } else {
            formatChargeList = getRolledUpCharges(lclCommodityList, chargeList, engmet);
        }
        return formatChargeList;
    }

    public List getFormattedLabelCharges(List<LclBookingPiece> lclCommodityList,
            List<LclBookingAc> lclBookingAcList, String engmet) {
        for (int i = 0; i < lclBookingAcList.size(); i++) {
            LclBookingAc lclBookingAc = lclBookingAcList.get(i);
            lclBookingAc.setRolledupCharges(lclBookingAc.getArAmount());
            formatLabelChargeBookingPDF(lclBookingAc, engmet);
        }
        return lclBookingAcList;
    }

    public void formatLabelChargeBookingPDF(LclBookingAc lclBookingAc, String engmet) {
        if (lclBookingAc.getRatePerUnitUom() != null && !lclBookingAc.getRatePerUnitUom().trim().equals("")) {
            if (lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("FL")) {
                if (lclBookingAc.getArglMapping() != null && lclBookingAc.getArglMapping().getBlueScreenChargeCode() != null) {
                    if (lclBookingAc.getArglMapping().getBlueScreenChargeCode().equals("0006")) {
                        String ratePerUnit = lclBookingAc.getRatePerWeightUnit() != null ? lclBookingAc.getRatePerWeightUnit().toString() : "";
                        String ratePeUnitDiv = lclBookingAc.getRatePerWeightUnit() != null ? lclBookingAc.getRatePerWeightUnit().toString() : "";  
                        lclBookingAc.setLabel2("(" + ratePerUnit + " PER " + ratePeUnitDiv + " CIF)");
                    } else {
                        lclBookingAc.setLabel2("FLAT RATE:  " + "$" + lclBookingAc.getArAmount().toString());
                    }
                }
                if (lclBookingAc.getArglMapping().getChargeCode() != null && (lclBookingAc.getArglMapping().getChargeCode().equals("OFBARR")
                        || lclBookingAc.getArglMapping().getChargeCode().equals("TTBARR"))) {
                    lclBookingAc.setLabel2("PER BARREL:  " + "$" + lclBookingAc.getRatePerWeightUnit());
                }

            }
            if (lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("W")) {
                lclBookingAc.setLabel1("*** TO WEIGHT ***");
            }
            if (lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("V")) {
                lclBookingAc.setLabel1("*** VOLUME ***");
            }
            if (lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("M")) {
                lclBookingAc.setLabel1("*** MINIMUM ***");
            }
            if (lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("PCT")) {
                if (lclBookingAc.getRatePerUnit() != null) {
                    int ratePercentage = (int) (lclBookingAc.getRatePerUnit().doubleValue() * 100);
                    lclBookingAc.setLabel2(String.valueOf(ratePercentage) + " PERCENT OF THE B/L.");
                }
            }
            if (lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("FRW")) {
                lclBookingAc.setLabel1("* O/F - TO WEIGHT *");
            }
            if (lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("FRV")) {
                lclBookingAc.setLabel1("* O/F - VOLUME *");
            }
            if (lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("FRM")) {
                lclBookingAc.setLabel1("* O/F - MINIMUM *");
            }
            if (lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("W") || lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("V")
                    || lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("M") || lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("FRW")
                    || lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("FRV") || lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("FRM")) {
                if (engmet != null) {
                    if (engmet.equalsIgnoreCase("E")) {
                        lclBookingAc.setLabel2("$" + NumberUtils.convertToTwoDecimal(lclBookingAc.getRatePerVolumeUnit().doubleValue()) + " CFT " + NumberUtils.convertToTwoDecimal(lclBookingAc.getRatePerWeightUnit().doubleValue())
                                + "/" + NumberUtils.convertToTwoDecimal(lclBookingAc.getRatePerWeightUnitDiv().doubleValue()) + " LBS ($" + NumberUtils.convertToTwoDecimal(lclBookingAc.getRateFlatMinimum().doubleValue()) + " Min)");
                    } else if (engmet.equalsIgnoreCase("M")) {
                        lclBookingAc.setLabel2("$" + NumberUtils.convertToTwoDecimal(lclBookingAc.getRatePerVolumeUnit().doubleValue()) + " CBM " + NumberUtils.convertToTwoDecimal(lclBookingAc.getRatePerWeightUnit().doubleValue())
                                + "/" + NumberUtils.convertToTwoDecimal(lclBookingAc.getRatePerWeightUnitDiv().doubleValue()) + " KGS ($" + NumberUtils.convertToTwoDecimal(lclBookingAc.getRateFlatMinimum().doubleValue()) + " Min)");
                    }
                }
            }
        }
    }

    public List getRolledUpCharges(List<LclBookingPiece> lclCommodityList, List<LclBookingAc> lclBookingAcList, String engmet) {
        Map chargesInfoMap = new LinkedHashMap();
        Double minchg = 0.0;
        Double calculatedWeight = 0.0;
        Double calculatedMeasure = 0.0;
        for (int i = 0; i < lclBookingAcList.size(); i++) {
            LclBookingAc lclBookingAc = lclBookingAcList.get(i);
            if (!chargesInfoMap.containsKey(lclBookingAc.getArglMapping().getChargeCode())) {
                lclBookingAc.setRolledupCharges(lclBookingAc.getArAmount());
                if (!lclBookingAc.isManualEntry() && lclBookingAc.getRatePerUnitUom() != null && lclBookingAc.getRateUom() != null && lclBookingAc.getArglMapping() != null
                        && lclBookingAc.getArglMapping().getBlueScreenChargeCode() != null && !lclBookingAc.getArglMapping().getBlueScreenChargeCode().equals("0032")
                        && !lclBookingAc.getArglMapping().getBlueScreenChargeCode().equals("0232")) {
                    if (lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("V") || lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("W")
                            || lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("M")) {
                        if (lclBookingAc.getRateUom().equalsIgnoreCase("I")) {
                            if (lclBookingAc.getLclBookingPiece() != null) {
                                if (lclBookingAc.getLclBookingPiece().getActualWeightImperial() != null && lclBookingAc.getLclBookingPiece().getActualWeightImperial().doubleValue() != 0.00) {
                                    lclBookingAc.setTotalWeight(lclBookingAc.getLclBookingPiece().getActualWeightImperial());
                                } else if (lclBookingAc.getLclBookingPiece().getBookedWeightImperial() != null && lclBookingAc.getLclBookingPiece().getBookedWeightImperial().doubleValue() != 0.00) {
                                    lclBookingAc.setTotalWeight(lclBookingAc.getLclBookingPiece().getBookedWeightImperial());
                                }
                                if (lclBookingAc.getLclBookingPiece().getActualVolumeImperial() != null && lclBookingAc.getLclBookingPiece().getActualVolumeImperial().doubleValue() != 0.00) {
                                    lclBookingAc.setTotalMeasure(lclBookingAc.getLclBookingPiece().getActualVolumeImperial());
                                } else if (lclBookingAc.getLclBookingPiece().getBookedVolumeImperial() != null && lclBookingAc.getLclBookingPiece().getBookedVolumeImperial().doubleValue() != 0.00) {
                                    lclBookingAc.setTotalMeasure(lclBookingAc.getLclBookingPiece().getBookedVolumeImperial());
                                }
                            }

                        } else if (lclBookingAc.getRateUom().equalsIgnoreCase("M")) {
                            if (lclBookingAc.getLclBookingPiece() != null) {
                                if (lclBookingAc.getLclBookingPiece().getActualWeightMetric() != null && lclBookingAc.getLclBookingPiece().getActualWeightMetric().doubleValue() != 0.00) {
                                    lclBookingAc.setTotalWeight(lclBookingAc.getLclBookingPiece().getActualWeightMetric());
                                } else if (lclBookingAc.getLclBookingPiece().getBookedWeightMetric() != null && lclBookingAc.getLclBookingPiece().getBookedWeightMetric().doubleValue() != 0.00) {
                                    lclBookingAc.setTotalWeight(lclBookingAc.getLclBookingPiece().getBookedWeightMetric());
                                }
                                if (lclBookingAc.getLclBookingPiece().getActualVolumeMetric() != null && lclBookingAc.getLclBookingPiece().getActualVolumeMetric().doubleValue() != 0.00) {
                                    lclBookingAc.setTotalMeasure(lclBookingAc.getLclBookingPiece().getActualVolumeMetric());
                                } else if (lclBookingAc.getLclBookingPiece().getBookedVolumeMetric() != null && lclBookingAc.getLclBookingPiece().getBookedVolumeMetric().doubleValue() != 0.00) {
                                    lclBookingAc.setTotalMeasure(lclBookingAc.getLclBookingPiece().getBookedVolumeMetric());
                                }
                            }
                        }
                    }
                }
                formatLabelChargeBookingPDF(lclBookingAc, engmet);
                chargesInfoMap.put(lclBookingAc.getArglMapping().getChargeCode(), lclBookingAc);
            } else {
                LclBookingAc lclBookingAcFromMap = (LclBookingAc) chargesInfoMap.get(lclBookingAc.getArglMapping().getChargeCode());
                if (!lclBookingAcFromMap.getRatePerUnitUom().equalsIgnoreCase("FL") || lclBookingAc.getArglMapping().getChargeCode().equalsIgnoreCase("OFBARR")
                        || lclBookingAc.getArglMapping().getChargeCode().equalsIgnoreCase("TTBARR")) {
                    BigDecimal total = new BigDecimal(lclBookingAc.getArAmount().doubleValue() + lclBookingAcFromMap.getRolledupCharges().doubleValue());
                    total = total.setScale(2, BigDecimal.ROUND_HALF_UP);
                    lclBookingAcFromMap.setRolledupCharges(total);
                }
                if (lclBookingAcFromMap.getRatePerUnitUom() != null && lclBookingAcFromMap.getRateUom() != null && lclBookingAcFromMap.getArglMapping() != null
                        && lclBookingAcFromMap.getArglMapping().getBlueScreenChargeCode() != null && !lclBookingAcFromMap.getArglMapping().getBlueScreenChargeCode().equals("0032")
                        && !lclBookingAcFromMap.getArglMapping().getBlueScreenChargeCode().equals("0232")) {
                    if (lclBookingAcFromMap.getRatePerUnitUom().equalsIgnoreCase("V") || lclBookingAcFromMap.getRatePerUnitUom().equalsIgnoreCase("W")
                            || lclBookingAcFromMap.getRatePerUnitUom().equalsIgnoreCase("M")) {
                        if (lclBookingAcFromMap.getRateUom().equalsIgnoreCase("I")) {
                            if (lclBookingAc.getLclBookingPiece() != null) {
                                if (lclBookingAc.getLclBookingPiece().getActualWeightImperial() != null && lclBookingAc.getLclBookingPiece().getActualWeightImperial().doubleValue() != 0.00) {
                                    if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalWeight())) {
                                        lclBookingAcFromMap.setTotalWeight(new BigDecimal(lclBookingAcFromMap.getTotalWeight().doubleValue()
                                                + lclBookingAc.getLclBookingPiece().getActualWeightImperial().doubleValue()));
                                    }
                                } else if (lclBookingAc.getLclBookingPiece().getBookedWeightImperial() != null && lclBookingAc.getLclBookingPiece().getBookedWeightImperial().doubleValue() != 0.00) {
                                    if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalWeight())) {
                                        lclBookingAcFromMap.setTotalWeight(new BigDecimal(lclBookingAcFromMap.getTotalWeight().doubleValue()
                                                + lclBookingAc.getLclBookingPiece().getBookedWeightImperial().doubleValue()));
                                    }
                                }
                                if (lclBookingAc.getLclBookingPiece().getActualVolumeImperial() != null && lclBookingAc.getLclBookingPiece().getActualVolumeImperial().doubleValue() != 0.00) {
                                    if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalMeasure())) {
                                        lclBookingAcFromMap.setTotalMeasure(new BigDecimal(lclBookingAcFromMap.getTotalMeasure().doubleValue()
                                                + lclBookingAc.getLclBookingPiece().getActualVolumeImperial().doubleValue()));
                                    }
                                } else if (lclBookingAc.getLclBookingPiece().getBookedVolumeImperial() != null && lclBookingAc.getLclBookingPiece().getBookedVolumeImperial().doubleValue() != 0.00) {
                                    if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalMeasure())) {
                                        lclBookingAcFromMap.setTotalMeasure(new BigDecimal(lclBookingAcFromMap.getTotalMeasure().doubleValue()
                                                + lclBookingAc.getLclBookingPiece().getBookedVolumeImperial().doubleValue()));
                                    }
                                }
                                if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalWeight())) {
                                    calculatedWeight = (lclBookingAcFromMap.getTotalWeight().doubleValue() / 100) * calculatedWeight;
                                }
                            }
                        } else if (lclBookingAcFromMap.getRateUom().equalsIgnoreCase("M")) {
                            if (lclBookingAc.getLclBookingPiece() != null) {
                                if (lclBookingAc.getLclBookingPiece().getActualWeightMetric() != null && lclBookingAc.getLclBookingPiece().getActualWeightMetric().doubleValue() != 0.00) {
                                    if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalWeight())) {
                                        lclBookingAcFromMap.setTotalWeight(new BigDecimal(lclBookingAcFromMap.getTotalWeight().doubleValue()
                                                + lclBookingAc.getLclBookingPiece().getActualWeightMetric().doubleValue()));
                                    }
                                } else if (lclBookingAc.getLclBookingPiece().getBookedWeightMetric() != null && lclBookingAc.getLclBookingPiece().getBookedWeightMetric().doubleValue() != 0.00) {
                                    if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalWeight())) {
                                        lclBookingAcFromMap.setTotalWeight(new BigDecimal(lclBookingAcFromMap.getTotalWeight().doubleValue()
                                                + lclBookingAc.getLclBookingPiece().getBookedWeightMetric().doubleValue()));
                                    }
                                }
                                if (lclBookingAc.getLclBookingPiece().getActualVolumeMetric() != null && lclBookingAc.getLclBookingPiece().getActualVolumeMetric().doubleValue() != 0.00) {
                                    if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalMeasure())) {
                                        lclBookingAcFromMap.setTotalMeasure(new BigDecimal(lclBookingAcFromMap.getTotalMeasure().doubleValue()
                                                + lclBookingAc.getLclBookingPiece().getActualVolumeMetric().doubleValue()));
                                    }
                                } else if (lclBookingAc.getLclBookingPiece().getBookedVolumeMetric() != null && lclBookingAc.getLclBookingPiece().getBookedVolumeMetric().doubleValue() != 0.00) {
                                    if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalMeasure())) {
                                        lclBookingAcFromMap.setTotalMeasure(new BigDecimal(lclBookingAcFromMap.getTotalMeasure().doubleValue()
                                                + lclBookingAc.getLclBookingPiece().getBookedVolumeMetric().doubleValue()));
                                    }
                                }
                                if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalWeight())) {
                                    calculatedWeight = (lclBookingAcFromMap.getTotalWeight().doubleValue() / 1000) * lclBookingAc.getRatePerWeightUnit().doubleValue();
                                }
                            }
                        }
                        if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalMeasure()) && !CommonUtils.isEmpty(lclBookingAc.getRatePerVolumeUnit())) {
                            calculatedMeasure = lclBookingAcFromMap.getTotalMeasure().doubleValue() * lclBookingAc.getRatePerVolumeUnit().doubleValue();
                        }
                        minchg = lclBookingAc.getRateFlatMinimum().doubleValue();
                        if (calculatedWeight >= calculatedMeasure && calculatedWeight >= minchg) {
                            lclBookingAcFromMap.setRolledupCharges(new BigDecimal(calculatedWeight).setScale(2, BigDecimal.ROUND_HALF_UP));
                        } else if (calculatedMeasure >= calculatedWeight && calculatedMeasure >= minchg) {
                            lclBookingAcFromMap.setRolledupCharges(new BigDecimal(calculatedMeasure).setScale(2, BigDecimal.ROUND_HALF_UP));
                        } else {
                            lclBookingAcFromMap.setRolledupCharges(new BigDecimal(minchg).setScale(2, BigDecimal.ROUND_HALF_UP));
                        }
                    }
                }
                formatLabelChargeBookingPDF(lclBookingAcFromMap, engmet);

                chargesInfoMap.put(lclBookingAc.getArglMapping().getChargeCode(), lclBookingAcFromMap);
            }
        }
        List rolledChargesList = new ArrayList(chargesInfoMap.values());
        return rolledChargesList;
    }

    public String getWarehouseLineLocation(List<LclBookingPiece> pieceList, boolean needfirstWarehouse) throws Exception {
        String wareHouseLine = "";
        for (LclBookingPiece piece : pieceList) {
            List<LclBookingPieceWhse> warehouseList = piece.getLclBookingPieceWhseList();
            if (CommonUtils.isNotEmpty(warehouseList)) {
                Collections.reverse(warehouseList);
                if (!needfirstWarehouse) {
                    for (LclBookingPieceWhse warehse : warehouseList) {
                        wareHouseLine += CommonUtils.isNotEmpty(warehse.getLocation()) ? warehse.getLocation() + "," : "";
                    }
                } else {
                    LclBookingPieceWhse warehse = warehouseList.get(warehouseList.size() - 1);
                    wareHouseLine = CommonUtils.isNotEmpty(warehse.getLocation()) ? warehse.getLocation() + "," : "";
                }
            }
            List<LclBookingPieceDetail> pieceDetailList = piece.getLclBookingPieceDetailList();
//            if (CommonUtils.isNotEmpty(pieceList)) {
//                for (LclBookingPieceDetail pieceDetail : pieceDetailList) {
//                    wareHouseLine += CommonUtils.isNotEmpty(pieceDetail.getStowedLocation()) ? pieceDetail.getStowedLocation() + "," : "";
//                }
//            }
        }
        return wareHouseLine;
    }

    public void saveCalculatedWeightMeasureManualCharge(Long fileId,
            LclBlAc lclBlAc, HttpServletRequest request) throws Exception {
        double totalMeasure = 0.00, totalWeight = 0.00;
        Double calculatedWeight = 0.00, calculatedMeasure = 0.00;
        User user = (User) request.getSession().getAttribute("loginuser");
        String engmet = "";
        LclBooking booking = lclBlAc.getLclFileNumber().getLclBooking();
        Ports ports = new PortsDAO().getByProperty("unLocationCode", booking.getPortOfDestination().getUnLocationCode());
        if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
            engmet = ports.getEngmet();
        }
        List<LclBlPiece> lclBlPiecesList = new LclBLPieceDAO().findByProperty("lclFileNumber.id", fileId);
        lclBlAc.setId(null);
        if (!CommonUtils.isEmpty(lclBlAc.getRatePerWeightUnit())
                || !CommonUtils.isEmpty(lclBlAc.getRatePerVolumeUnit())) {
            if (CommonUtils.isNotEmpty(lclBlPiecesList)) {
                totalMeasure = 0.00;
                totalWeight = 0.00;
                for (LclBlPiece piece : lclBlPiecesList) {
                    Double weightDouble = 0.00;
                    Double weightMeasure = 0.00;
                    if (engmet != null) {
                        if (engmet.equals("E")) {
                            if (piece.getActualWeightImperial() != null && piece.getActualWeightImperial().doubleValue() != 0.00) {
                                weightDouble = piece.getActualWeightImperial().doubleValue();
                            } else if (piece.getBookedWeightImperial() != null && piece.getBookedWeightImperial().doubleValue() != 0.00) {
                                weightDouble = piece.getBookedWeightImperial().doubleValue();
                            }

                            if (piece.getActualVolumeImperial() != null && piece.getActualVolumeImperial().doubleValue() != 0.00) {
                                weightMeasure = piece.getActualVolumeImperial().doubleValue();
                            } else if (piece.getBookedVolumeImperial() != null && piece.getBookedVolumeImperial().doubleValue() != 0.00) {
                                weightMeasure = piece.getBookedVolumeImperial().doubleValue();
                            }
                        } else if (engmet.equals("M")) {
                            if (piece.getActualWeightMetric() != null && piece.getActualWeightMetric().doubleValue() != 0.00) {
                                weightDouble = piece.getActualWeightMetric().doubleValue();
                            } else if (piece.getBookedWeightMetric() != null && piece.getBookedWeightMetric().doubleValue() != 0.00) {
                                weightDouble = piece.getBookedWeightMetric().doubleValue();
                            }
                            if (piece.getActualVolumeMetric() != null && piece.getActualVolumeMetric().doubleValue() != 0.00) {
                                weightMeasure = piece.getActualVolumeMetric().doubleValue();
                            } else if (piece.getBookedVolumeMetric() != null && piece.getBookedVolumeMetric().doubleValue() != 0.00) {
                                weightMeasure = piece.getBookedVolumeMetric().doubleValue();
                            }
                        }
                        totalWeight = totalWeight + weightDouble;
                        totalMeasure = totalMeasure + weightMeasure;
                    }
                }
            }
            if (engmet.equals("E")) {
                if (engmet.equals("E")) {
                    calculatedWeight = (totalWeight / 100) * lclBlAc.getRatePerWeightUnit().doubleValue();
                    lclBlAc.setRatePerWeightUnitDiv(new BigDecimal(100));
                } else if (engmet.equals("M")) {
                    calculatedWeight = (totalWeight / 1000) * lclBlAc.getRatePerWeightUnit().doubleValue();
                    lclBlAc.setRatePerWeightUnitDiv(new BigDecimal(1000));
                }
            }
            calculatedMeasure = totalMeasure * lclBlAc.getRatePerVolumeUnit().doubleValue();
            lclBlAc.setRatePerUnitDiv(lclBlAc.getRatePerWeightUnitDiv());
            lclBlAc.setRatePerVolumeUnitDiv(new BigDecimal(1000));
            if (calculatedWeight >= calculatedMeasure && calculatedWeight >= lclBlAc.getRateFlatMinimum().doubleValue()) {
                lclBlAc.setArAmount(new BigDecimal(calculatedWeight));
                lclBlAc.setRatePerUnitUom("W");
                lclBlAc.setRatePerUnitDiv(lclBlAc.getRatePerVolumeUnitDiv());
            } else if (calculatedMeasure >= calculatedWeight && calculatedMeasure >= lclBlAc.getRateFlatMinimum().doubleValue()) {
                lclBlAc.setArAmount(new BigDecimal(calculatedMeasure));
                lclBlAc.setRatePerUnitUom("V");
                lclBlAc.setRatePerUnitDiv(lclBlAc.getRatePerVolumeUnitDiv());

            } else {
                lclBlAc.setArAmount(lclBlAc.getRateFlatMinimum());
                lclBlAc.setRatePerUnitUom("M");
            }
        }
        if (!CommonUtils.isEmpty(lclBlAc.getRatePerUnit()) && CommonUtils.isEmpty(lclBlAc.getRatePerWeightUnit())
                && CommonUtils.isEmpty(lclBlAc.getRatePerVolumeUnit())) {
            lclBlAc.setRatePerUnitUom("FL");
            lclBlAc.setArAmount(lclBlAc.getRatePerUnit());
        }
        lclBlAc.setLclFileNumber(new LclFileNumber(fileId));
        lclBlAc.setBundleIntoOf(false);
        lclBlAc.setPrintOnBl(true);
        lclBlAc.setAdjustmentAmount(BigDecimal.ZERO);
        lclBlAc.setManualEntry(true);
        lclBlAc.setEnteredBy(user);
        lclBlAc.setModifiedBy(user);
        lclBlAc.setEnteredDatetime(new Date());
        lclBlAc.setModifiedDatetime(new Date());
        lclBlAc.setApAmount(null);
        lclBlAc.setApglMapping(null);
        new LclBlAcDAO().saveOrUpdate(lclBlAc);
    }

    public void saveNewDRNumber(String newfileNo, String oldFileNo, HttpServletRequest request) throws Exception {
        User user = (User) request.getSession().getAttribute("loginuser");
        List<Long> fileIdList = new LclFileNumberDAO().getFileIDIncudeShortShip(oldFileNo);
        new LclFileNumberDAO().updateNewFileNumber(oldFileNo, newfileNo);
        String remarks = "D/R Number Changed From " + oldFileNo + " To " + newfileNo;
        for (Long fileId : fileIdList) {
            new LclRemarksDAO().insertLclRemarks(fileId, "DR-AutoNotes", remarks, user.getUserId());
        }
    }
}

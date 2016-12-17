/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.beans.LclImpAlarmRevenueCostBean;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceUnit;
import com.gp.cong.logisoft.domain.lcl.LclSsAc;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsManifest;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceUnitDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.logiware.excel.BaseExcelCreator;
import com.logiware.hibernate.domain.ArRedInvoiceCharges;
import java.text.DecimalFormat;
import java.util.List;

/**
 *
 * @author Vijay Gupta
 */
public class LclImportExceptionsAndAlarms extends BaseExcelCreator implements LclReportConstants {

    public String filePath;
    public String unitSSId;
    public String unitNo;
    public List<LclSsAc> lclSsAcList = null;
    public LclUnitSs lclUnitSs;
    DecimalFormat df = new DecimalFormat("#.##");
    private Double totalKgs = 0d;

    public LclImportExceptionsAndAlarms() {
    }

    public LclImportExceptionsAndAlarms(String fileLocPath, String unitSsId, String unitNum) {
        this.filePath = fileLocPath;
        this.unitSSId = unitSsId;
        this.unitNo = unitNum;
    }

    public void createReport() {
        try {
            init(filePath, "Imports Exceptions Alarm");
            setLclPrintSize();
            writeContent(unitSSId);
            writeIntoFile();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exit();
        }
    }

    public void writeContent(String unitSSId) throws Exception {
        // setColumnAutoSize();
        createRow();
        createHeaderCell("Exceptions/Alarms", lclCellStyleLeftBoldArial15);
        mergeCells(rowIndex, rowIndex, 0, 3);
        createRow();
        resetColumnIndex();
        createRow();
        createHeaderCell("General Imports", lclCellStyleLeftBoldArial12);
        mergeCells(rowIndex, rowIndex, 0, 2);
        createRow();
        resetColumnIndex();
        createRow();
        setCellBorder(lclCellStyleBoldArialBorder, (short) 2, (short) 2, (short) 2, (short) 2);
        createHeaderCell("", lclCellStyleBoldArialBorder);
        mergeCells(rowIndex, rowIndex, 0, 13);
        row.setHeightInPoints(6);
        createRow();
        resetColumnIndex();
        createRow();
        resetColumnIndex();
        unitDetails();
        resetColumnIndex();
        createRow();
        createHeaderCell("", lclCellStyleBoldArialBorder);
        mergeCells(rowIndex, rowIndex, 0, 13);
        row.setHeightInPoints(6);
        createRow();
        resetColumnIndex();
        createRow();
        bookingDetails();
        createRow();
    }

    public void unitDetails() throws Exception {
        LclUnitSs lclUnitSstemp = new LclUnitSsDAO().findById(new Long(unitSSId));
        LclSsHeader lclSsHeader = lclUnitSstemp.getLclSsHeader();
        createHeaderCell("voyage#-" + lclSsHeader.getScheduleNo(), lclCellStyleLeftBoldArial12);
        createCell();
        createCell();
        createHeaderCell("origin -" + lclSsHeader.getOrigin().getUnLocationName(), lclCellStyleLeftBoldArial12);
        createCell();
        createCell();
        createHeaderCell("destn -" + lclSsHeader.getDestination().getUnLocationName(), lclCellStyleLeftBoldArial12);
        createCell();
        createCell();
        createHeaderCell("unit# -" + unitNo, lclCellStyleLeftBoldArial12);
        createRow();
        resetColumnIndex();
        createHeaderCell("unit size -" + lclUnitSstemp.getLclUnit().getUnitType().getDescription(), lclCellStyleLeftBoldArial12);
        //**********************************************************************
        //5. customs exam (Look at charge/cost codes:  CESINSP, CESMIS,  CETEXA.
        Double revanueAmmountCESINSP = 0.0, revanueAmmountCESMIS = 0.0, revanueAmmountCETEXA = 0.0;
        List<LclBookingPieceUnit> bookPieceUnitList = new LclBookingPieceUnitDAO().findByProperty("lclUnitSs.id", Long.parseLong(unitSSId));
        if (CommonUtils.isNotEmpty(bookPieceUnitList)) {
            for (LclBookingPieceUnit bookPieceUnit : bookPieceUnitList) {
                List<LclBookingPiece> listBookPiece = new LclBookingPieceDAO().findByProperty("id", bookPieceUnit.getLclBookingPiece().getId());
                if (CommonUtils.isNotEmpty(listBookPiece)) {
                    for (LclBookingPiece bookPiece : listBookPiece) {
                        List<LclBookingAc> listBookAc = new LclCostChargeDAO().getLclCostByFileNumberAsc(bookPiece.getLclFileNumber().getId(), "Imports");
                        if (CommonUtils.isNotEmpty(listBookAc)) {
                            for (LclBookingAc list : listBookAc) {
                                if (list.getArglMapping().getChargeCode().equals("CESINSP")) {
                                    revanueAmmountCESINSP = revanueAmmountCESINSP + getRevanueAmountSingle(list);
                                }
                                if (list.getArglMapping().getChargeCode().equals("CESMIS")) {
                                    revanueAmmountCESMIS = revanueAmmountCESMIS + getRevanueAmountSingle(list);
                                }
                                if (list.getArglMapping().getChargeCode().equals("CETEXA")) {
                                    revanueAmmountCETEXA = revanueAmmountCETEXA + getRevanueAmountSingle(list);
                                }
                            }

                        }
                    }
                }
            }
            Double costAmmountCESINSP = 0.0, costAmmountCESMIS = 0.0, costAmmountCETEXA = 0.0;
            List<LclSsAc> lsaCustExam1 = new LclSsAcDAO().getLclSsAcListByChargeCode(Long.parseLong(unitSSId), "CESINSP");
            List<LclSsAc> lsaCustExam2 = new LclSsAcDAO().getLclSsAcListByChargeCode(Long.parseLong(unitSSId), "CESMIS");
            List<LclSsAc> lsaCustExam3 = new LclSsAcDAO().getLclSsAcListByChargeCode(Long.parseLong(unitSSId), "CETEXA");
            if (CommonUtils.isNotEmpty(lsaCustExam1) || CommonUtils.isNotEmpty(lsaCustExam2) || CommonUtils.isNotEmpty(lsaCustExam3)) {
                boolean alaramFlag = false;
                if (CommonUtils.isNotEmpty(lsaCustExam1)) {
                    costAmmountCESINSP = getCostAmountSsAc(lsaCustExam1);
                    if (CommonUtils.isEmpty(revanueAmmountCESINSP) && CommonUtils.isNotEmpty(costAmmountCESINSP)) {
                        createRow();
                        resetColumnIndex();
                        createHeaderCell("5. customs exam (Look at charge/cost codes:  CESINSP, CESMIS,  CETEXA.) ", lclCellStyleLeftBoldArial10);
                        mergeCells(rowIndex, rowIndex, 0, 7);
                        customizedLclAlaramCell("CESINSP -> A – There exists a C.E. cost at unit but no revenue on a D/R ", revanueAmmountCESINSP, costAmmountCESINSP);
                        alaramFlag = true;
                    }
                    if (costAmmountCESINSP > revanueAmmountCESINSP) {
                        if (alaramFlag = false) {
                            createRow();
                            resetColumnIndex();
                            createHeaderCell("5. customs exam (Look at charge/cost codes:  CESINSP, CESMIS,  CETEXA.) ", lclCellStyleLeftBoldArial10);
                            mergeCells(rowIndex, rowIndex, 0, 7);
                        }
                        customizedLclAlaramCell("CESINSP -> B – C.E. cost (at unit level)  is higher than total C.E. revenue from  D/Rs ", revanueAmmountCESINSP, costAmmountCESINSP);
                        alaramFlag = true;
                    }
                }
                if (CommonUtils.isNotEmpty(lsaCustExam2)) {
                    costAmmountCESMIS = getCostAmountSsAc(lsaCustExam2);
                    if (CommonUtils.isEmpty(revanueAmmountCESMIS) && CommonUtils.isNotEmpty(costAmmountCESMIS)) {
                        if (alaramFlag = false) {
                            createRow();
                            resetColumnIndex();
                            createHeaderCell("5. customs exam (Look at charge/cost codes:  CESINSP, CESMIS,  CETEXA.) ", lclCellStyleLeftBoldArial10);
                            mergeCells(rowIndex, rowIndex, 0, 7);
                        }
                        customizedLclAlaramCell("CESMIS -> A – There exists a C.E. cost at unit but no revenue on a D/R ", revanueAmmountCESMIS, costAmmountCESMIS);
                        alaramFlag = true;
                    }
                    if (costAmmountCESMIS > revanueAmmountCESMIS) {
                        if (alaramFlag = false) {
                            createRow();
                            resetColumnIndex();
                            createHeaderCell("5. customs exam (Look at charge/cost codes:  CESINSP, CESMIS,  CETEXA.) ", lclCellStyleLeftBoldArial10);
                            mergeCells(rowIndex, rowIndex, 0, 7);
                        }
                        customizedLclAlaramCell("CESMIS -> B – C.E. cost (at unit level)  is higher than total C.E. revenue from  D/Rs ", revanueAmmountCESMIS, costAmmountCESMIS);
                        alaramFlag = true;
                    }
                }
                if (CommonUtils.isNotEmpty(lsaCustExam3)) {
                    costAmmountCETEXA = getCostAmountSsAc(lsaCustExam3);
                    if (CommonUtils.isEmpty(revanueAmmountCETEXA) && CommonUtils.isNotEmpty(costAmmountCETEXA)) {
                        if (alaramFlag = false) {
                            createRow();
                            resetColumnIndex();
                            createHeaderCell("5. customs exam (Look at charge/cost codes:  CESINSP, CESMIS,  CETEXA.) ", lclCellStyleLeftBoldArial10);
                            mergeCells(rowIndex, rowIndex, 0, 7);
                        }
                        customizedLclAlaramCell("CETEXA -> A – There exists a C.E. cost at unit but no revenue on a D/R", revanueAmmountCETEXA, costAmmountCETEXA);
                        alaramFlag = true;
                    }
                    if (costAmmountCETEXA > revanueAmmountCETEXA) {
                        if (alaramFlag = false) {
                            createRow();
                            resetColumnIndex();
                            createHeaderCell("5. customs exam (Look at charge/cost codes:  CESINSP, CESMIS,  CETEXA.) ", lclCellStyleLeftBoldArial10);
                            mergeCells(rowIndex, rowIndex, 0, 7);
                        }
                        customizedLclAlaramCell("CETEXA -> B – C.E. cost (at unit level)  is higher than total C.E. revenue from  D/Rs. ", revanueAmmountCETEXA, costAmmountCETEXA);
                        alaramFlag = true;
                    }
                }
            }
            //******************************************************************
        }
    }

    public void headingDetails(LclBookingPiece bookPiece) throws Exception {
        resetColumnIndex();
        createRow();
        resetColumnIndex();
        createHeaderCell("IMP-" + bookPiece.getLclFileNumber().getFileNumber(), lclCellStyleLeftBoldArial11);
        createEmptyCell();
        createHeaderCell(bookPiece.getBookedPieceCount() + " " + bookPiece.getPackageType().getDescription() + " " + bookPiece.getBookedWeightMetric() + " kgs " + " - " + bookPiece.getBookedVolumeMetric() + " cbm", lclCellStyleLeftBoldArial10);
        createCell();
        createCell();
        createCell();
        if (bookPiece != null && bookPiece.getLclFileNumber() != null && bookPiece.getLclFileNumber().getLclBooking() != null) {
            if (bookPiece.getLclFileNumber().getLclBooking().getFinalDestination().getUnLocationName() != null) {
                createHeaderCell("Final dest." + bookPiece.getLclFileNumber().getLclBooking().getFinalDestination().getUnLocationName().toString(), lclCellStyleLeftBoldArial11);
            }
        }
        resetColumnIndex();
    }

    public void bookingDetails() throws Exception {
        List<LclBookingPieceUnit> bookPieceUnitList = new LclBookingPieceUnitDAO().findByProperty("lclUnitSs.id", Long.parseLong(unitSSId));
        if (CommonUtils.isNotEmpty(bookPieceUnitList)) {
            for (LclBookingPieceUnit bookPieceUnit : bookPieceUnitList) {
                List<LclBookingPiece> listBookPiece = new LclBookingPieceDAO().findByProperty("id", bookPieceUnit.getLclBookingPiece().getId());
                if (CommonUtils.isNotEmpty(listBookPiece)) {
                    for (LclBookingPiece bookPiece : listBookPiece) {
                        boolean bookingFlag = false;
                        boolean alaramFlag = false;
                        boolean headingFlag = false;
                        List<LclBookingAc> listBookAc = new LclCostChargeDAO().getLclCostByFileNumberAsc(bookPiece.getLclFileNumber().getId(), "Imports");
                        if (CommonUtils.isNotEmpty(listBookAc)) {
                            Double revanueAmmount, costAmmount;
                            //**************************************************
                            //1. Oceanfreight Import (look at charge/cost codes OCNFRT and OFIMP)
                            LclImpAlarmRevenueCostBean lbaOcean1 = new LclCostChargeDAO().getLclAlaramsRevanueAndCost(bookPiece.getLclFileNumber().getId(), "OCNFRT");
                            LclImpAlarmRevenueCostBean lbaOcean2 = new LclCostChargeDAO().getLclAlaramsRevanueAndCost(bookPiece.getLclFileNumber().getId(), "OFIMP");
                            if (lbaOcean1 != null || lbaOcean2 != null) {
                                if (lbaOcean1 != null) {
                                    revanueAmmount = Double.parseDouble(lbaOcean1.getRevenueAmount() == null ? "0.00" : lbaOcean1.getRevenueAmount());
                                    costAmmount = Double.parseDouble(lbaOcean1.getCostAmount() == null ? "0.00" : lbaOcean1.getCostAmount());
                                    if (CommonUtils.isEmpty(revanueAmmount) && CommonUtils.isNotEmpty(costAmmount)) {
                                        if (headingFlag == false) {
                                            headingDetails(bookPiece);
                                            headingFlag = true;
                                        }
                                        createRow();
                                        resetColumnIndex();
                                        createHeaderCell("1. Oceanfreight Import (look at charge/cost codes OCNFRT and OFIMP) ", lclCellStyleLeftBoldArial10);
                                    } else if (CommonUtils.isNotEmpty(revanueAmmount) && CommonUtils.isNotEmpty(costAmmount) && checkMoreThanRevanueandCost(revanueAmmount, costAmmount)) {
                                        if (headingFlag == false) {
                                            headingDetails(bookPiece);
                                            headingFlag = true;
                                        }
                                        createRow();
                                        resetColumnIndex();
                                        createHeaderCell("1. Oceanfreight Import (look at charge/cost codes OCNFRT and OFIMP) ", lclCellStyleLeftBoldArial10);
                                    } else if (costAmmount > revanueAmmount) {
                                        if (headingFlag == false) {
                                            headingDetails(bookPiece);
                                            headingFlag = true;
                                        }
                                        createRow();
                                        resetColumnIndex();
                                        createHeaderCell("1. Oceanfreight Import (look at charge/cost codes OCNFRT and OFIMP) ", lclCellStyleLeftBoldArial10);
                                    }
                                    if (CommonUtils.isEmpty(revanueAmmount) && CommonUtils.isNotEmpty(costAmmount)) {
                                        customizedLclAlaramCell("OCNFRT -> A – There exists OCNFRT cost but no revenue ", revanueAmmount, costAmmount);
                                        alaramFlag = true;
                                    }
                                    if (CommonUtils.isNotEmpty(revanueAmmount) && CommonUtils.isNotEmpty(costAmmount)) {
                                        if (checkMoreThanRevanueandCost(revanueAmmount, costAmmount)) {
                                            customizedLclAlaramCell("OCNFRT -> B -  difference between OCNFRT revenue and cost is more than 5% ", revanueAmmount, costAmmount);
                                            alaramFlag = true;
                                        }
                                    }
                                    if (costAmmount > revanueAmmount) {
                                        customizedLclAlaramCell("OCNFRT -> C - OCNFRT cost is higher than OCNFRT revenue ", revanueAmmount, costAmmount);
                                        alaramFlag = true;
                                    }
                                }
                                if (lbaOcean2 != null) {
                                    revanueAmmount = Double.parseDouble(lbaOcean2.getRevenueAmount() == null ? "0.00" : lbaOcean2.getRevenueAmount());
                                    costAmmount = Double.parseDouble(lbaOcean2.getCostAmount() == null ? "0.00" : lbaOcean2.getCostAmount());
                                    if (CommonUtils.isEmpty(revanueAmmount) && CommonUtils.isNotEmpty(costAmmount)) {
                                        if (headingFlag == false) {
                                            headingDetails(bookPiece);
                                            headingFlag = true;
                                        }
                                        createRow();
                                        resetColumnIndex();
                                        createHeaderCell("1. Oceanfreight Import (look at charge/cost codes OCNFRT and OFIMP) ", lclCellStyleLeftBoldArial10);
                                    } else if (CommonUtils.isNotEmpty(revanueAmmount) && CommonUtils.isNotEmpty(costAmmount) && (((revanueAmmount - costAmmount) / revanueAmmount * 100 > 5) || ((costAmmount - revanueAmmount) / costAmmount * 100 > 5))) {
                                        if (headingFlag == false) {
                                            headingDetails(bookPiece);
                                            headingFlag = true;
                                        }
                                        createRow();
                                        resetColumnIndex();
                                        createHeaderCell("1. Oceanfreight Import (look at charge/cost codes OCNFRT and OFIMP) ", lclCellStyleLeftBoldArial10);
                                    } else if (costAmmount > revanueAmmount) {
                                        if (headingFlag == false) {
                                            headingDetails(bookPiece);
                                            headingFlag = true;
                                        }
                                        createRow();
                                        resetColumnIndex();
                                        createHeaderCell("1. Oceanfreight Import (look at charge/cost codes OCNFRT and OFIMP) ", lclCellStyleLeftBoldArial10);
                                    }
                                    if (CommonUtils.isEmpty(revanueAmmount) && CommonUtils.isNotEmpty(costAmmount)) {
                                        customizedLclAlaramCell("OFIMP -> A – There exists OFIMP cost but no revenue ", revanueAmmount, costAmmount);
                                        alaramFlag = true;
                                    }
                                    if (CommonUtils.isNotEmpty(revanueAmmount) && CommonUtils.isNotEmpty(costAmmount)) {
                                        if (((revanueAmmount - costAmmount) / revanueAmmount * 100 > 5) || ((costAmmount - revanueAmmount) / costAmmount * 100 > 5)) {
                                            customizedLclAlaramCell("OFIMP -> B -  difference between OFIMP revenue and cost is more than 5% ", revanueAmmount, costAmmount);
                                            alaramFlag = true;
                                        }
                                    }
                                    if (costAmmount > revanueAmmount) {
                                        customizedLclAlaramCell("OFIMP -> C - OFIMP cost is higher than OFIMP revenue ", revanueAmmount, costAmmount);
                                        alaramFlag = true;
                                    }
                                }
                                if (alaramFlag == true) {
                                    bookingFlag = true;
                                }
                            }
                            //**************************************************
                            //2. Door Delivery  (look at charge/cost code DOORDEL and INLAN)
                            alaramFlag = false;
                            LclImpAlarmRevenueCostBean lbaDorDel1 = new LclCostChargeDAO().getLclAlaramsRevanueAndCost(bookPiece.getLclFileNumber().getId(), "DOORDEL");
                            LclImpAlarmRevenueCostBean lbaDorDel2 = new LclCostChargeDAO().getLclAlaramsRevanueAndCost(bookPiece.getLclFileNumber().getId(), "INLAND");
                            if (lbaDorDel1 != null || lbaDorDel2 != null) {
                                if (lbaDorDel1 != null) {
                                    revanueAmmount = Double.parseDouble(lbaDorDel1.getRevenueAmount() == null ? "0.00" : lbaDorDel1.getRevenueAmount());
                                    costAmmount = Double.parseDouble(lbaDorDel1.getCostAmount() == null ? "0.00" : lbaDorDel1.getCostAmount());
                                    if (CommonUtils.isEmpty(revanueAmmount) && CommonUtils.isNotEmpty(costAmmount)) {
                                        if (headingFlag == false) {
                                            headingDetails(bookPiece);
                                            headingFlag = true;
                                        }
                                        createRow();
                                        resetColumnIndex();
                                        createHeaderCell("2. Door Delivery  (look at charge/cost code DOORDEL and INLAND) ", lclCellStyleLeftBoldArial10);
                                    } else if (CommonUtils.isNotEmpty(revanueAmmount) && CommonUtils.isNotEmpty(costAmmount) && checkLessThanRevanueandCost(revanueAmmount, costAmmount)) {
                                        if (headingFlag == false) {
                                            headingDetails(bookPiece);
                                            headingFlag = true;
                                        }
                                        createRow();
                                        resetColumnIndex();
                                        createHeaderCell("2. Door Delivery  (look at charge/cost code DOORDEL and INLAND) ", lclCellStyleLeftBoldArial10);
                                    } else if (costAmmount > revanueAmmount) {
                                        if (headingFlag == false) {
                                            headingDetails(bookPiece);
                                            headingFlag = true;
                                        }
                                        createRow();
                                        resetColumnIndex();
                                        createHeaderCell("2. Door Delivery  (look at charge/cost code DOORDEL and INLAND) ", lclCellStyleLeftBoldArial10);
                                    }
                                    if (CommonUtils.isEmpty(revanueAmmount) && CommonUtils.isNotEmpty(costAmmount)) {
                                        customizedLclAlaramCell("DOORDEL -> A – There exists DOORDEL cost but no revenue ", revanueAmmount, costAmmount);
                                        alaramFlag = true;
                                    }
                                    if (CommonUtils.isNotEmpty(revanueAmmount) && CommonUtils.isNotEmpty(costAmmount)) {
                                        if (checkLessThanRevanueandCost(revanueAmmount, costAmmount)) {
                                            customizedLclAlaramCell("DOORDEL -> B -  difference between DOORDEL revenue and cost is less than 5% ", revanueAmmount, costAmmount);
                                            alaramFlag = true;
                                        }
                                    }
                                    if (costAmmount > revanueAmmount) {
                                        customizedLclAlaramCell("DOORDEL -> C - DOORDEL cost is higher than DOORDEL revenue ", revanueAmmount, costAmmount);
                                        alaramFlag = true;
                                    }
                                }
                                if (lbaDorDel2 != null) {
                                    revanueAmmount = Double.parseDouble(lbaDorDel2.getRevenueAmount() == null ? "0.00" : lbaDorDel2.getRevenueAmount());
                                    costAmmount = Double.parseDouble(lbaDorDel2.getCostAmount() == null ? "0.00" : lbaDorDel2.getCostAmount());
                                    if (CommonUtils.isEmpty(revanueAmmount) && CommonUtils.isNotEmpty(costAmmount)) {
                                        if (headingFlag == false) {
                                            headingDetails(bookPiece);
                                            headingFlag = true;
                                        }
                                        createRow();
                                        resetColumnIndex();
                                        createHeaderCell("2. Door Delivery  (look at charge/cost code DOORDEL and INLAND) ", lclCellStyleLeftBoldArial10);
                                    } else if (CommonUtils.isNotEmpty(revanueAmmount) && CommonUtils.isNotEmpty(costAmmount) && checkLessThanRevanueandCost(revanueAmmount, costAmmount)) {
                                        if (headingFlag == false) {
                                            headingDetails(bookPiece);
                                            headingFlag = true;
                                        }
                                        createRow();
                                        resetColumnIndex();
                                        createHeaderCell("2. Door Delivery  (look at charge/cost code DOORDEL and INLAND) ", lclCellStyleLeftBoldArial10);
                                    } else if (costAmmount > revanueAmmount) {
                                        if (headingFlag == false) {
                                            headingDetails(bookPiece);
                                            headingFlag = true;
                                        }
                                        createRow();
                                        resetColumnIndex();
                                        createHeaderCell("2. Door Delivery  (look at charge/cost code DOORDEL and INLAND) ", lclCellStyleLeftBoldArial10);
                                    }
                                    if (CommonUtils.isEmpty(revanueAmmount) && CommonUtils.isNotEmpty(costAmmount)) {
                                        customizedLclAlaramCell("INLAND -> A – There exists INLAND cost but no revenue ", revanueAmmount, costAmmount);
                                        alaramFlag = true;
                                    }
                                    if (CommonUtils.isNotEmpty(revanueAmmount) && CommonUtils.isNotEmpty(costAmmount)) {
                                        if (checkLessThanRevanueandCost(revanueAmmount, costAmmount)) {
                                            customizedLclAlaramCell("INLAND -> B -  difference between INLAND revenue and cost is less than 5% ", revanueAmmount, costAmmount);
                                            alaramFlag = true;
                                        }
                                    }
                                    if (costAmmount > revanueAmmount) {
                                        customizedLclAlaramCell("INLAND ->C - INLAND cost is higher than INLAND revenue ", revanueAmmount, costAmmount);
                                        alaramFlag = true;
                                    }
                                }
                                if (alaramFlag == true) {
                                    bookingFlag = true;
                                }
                            }
                            //**************************************************
                            //3. Storage (look at charge/cost codes STORAG, STORE1, STORE2)
                            alaramFlag = false;
                            LclImpAlarmRevenueCostBean lbaStorage1 = new LclCostChargeDAO().getLclAlaramsRevanueAndCost(bookPiece.getLclFileNumber().getId(), "STORAG");
                            LclImpAlarmRevenueCostBean lbaStorage2 = new LclCostChargeDAO().getLclAlaramsRevanueAndCost(bookPiece.getLclFileNumber().getId(), "STORE1");
                            LclImpAlarmRevenueCostBean lbaStorage3 = new LclCostChargeDAO().getLclAlaramsRevanueAndCost(bookPiece.getLclFileNumber().getId(), "STORE2");
                            if (lbaStorage1 != null || lbaStorage2 != null || lbaStorage3 != null) {
                                boolean alaramStoragFlag = false;
                                if (lbaStorage1 != null) {
                                    revanueAmmount = Double.parseDouble(lbaStorage1.getRevenueAmount() == null ? "0.00" : lbaStorage1.getRevenueAmount());
                                    costAmmount = Double.parseDouble(lbaStorage1.getCostAmount() == null ? "0.00" : lbaStorage1.getCostAmount());
                                    if (CommonUtils.isEmpty(revanueAmmount) && CommonUtils.isNotEmpty(costAmmount)) {
                                        if (headingFlag == false) {
                                            headingDetails(bookPiece);
                                            headingFlag = true;
                                        }
                                        createRow();
                                        resetColumnIndex();
                                        createHeaderCell("3. Storage (look at charge/cost codes STORAG, STORE1, STORE2) ", lclCellStyleLeftBoldArial10);
                                        customizedLclAlaramCell("STORAG -> A – There is a cost but no revenue ", revanueAmmount, costAmmount);
                                        alaramStoragFlag = true;
                                    }
                                }
                                if (lbaStorage2 != null) {
                                    revanueAmmount = Double.parseDouble(lbaStorage2.getRevenueAmount() == null ? "0.00" : lbaStorage2.getRevenueAmount());
                                    costAmmount = Double.parseDouble(lbaStorage2.getCostAmount() == null ? "0.00" : lbaStorage2.getCostAmount());
                                    if (CommonUtils.isEmpty(revanueAmmount) && CommonUtils.isNotEmpty(costAmmount)) {
                                        if (alaramStoragFlag = false) {
                                            if (headingFlag == false) {
                                                headingDetails(bookPiece);
                                                headingFlag = true;
                                            }
                                            createRow();
                                            resetColumnIndex();
                                            createHeaderCell("3. Storage (look at charge/cost codes STORAG, STORE1, STORE2) ", lclCellStyleLeftBoldArial10);
                                        }
                                        customizedLclAlaramCell("STORE1 -> A – There is a cost but no revenue ", revanueAmmount, costAmmount);
                                        alaramFlag = true;
                                    }
                                }
                                if (lbaStorage3 != null) {
                                    revanueAmmount = Double.parseDouble(lbaStorage3.getRevenueAmount() == null ? "0.00" : lbaStorage3.getRevenueAmount());
                                    costAmmount = Double.parseDouble(lbaStorage3.getCostAmount() == null ? "0.00" : lbaStorage3.getCostAmount());
                                    if (CommonUtils.isEmpty(revanueAmmount) && CommonUtils.isNotEmpty(costAmmount)) {
                                        if (alaramStoragFlag = false) {
                                            if (headingFlag == false) {
                                                headingDetails(bookPiece);
                                                headingFlag = true;
                                            }
                                            createRow();
                                            resetColumnIndex();
                                            createHeaderCell("3. Storage (look at charge/cost codes STORAG, STORE1, STORE2) ", lclCellStyleLeftBoldArial10);
                                        }
                                        customizedLclAlaramCell("STORE2 -> A – There is a cost but no revenue ", revanueAmmount, costAmmount);
                                        alaramFlag = true;
                                    }
                                }
                                if (alaramFlag == true) {
                                    bookingFlag = true;
                                }
                            }
                            //**************************************************
                            //4. IPI (look at charge/cost code IPI)
                            alaramFlag = false;
                            LclImpAlarmRevenueCostBean lbaIPI = new LclCostChargeDAO().getLclAlaramsRevanueAndCost(bookPiece.getLclFileNumber().getId(), "IPI");
                            if (lbaIPI != null) {
                                costAmmount = Double.parseDouble(lbaIPI.getCostAmount() == null ? "0.00" : lbaIPI.getCostAmount());
                                revanueAmmount = Double.parseDouble(lbaIPI.getRevenueAmount() == null ? "0.00" : lbaIPI.getRevenueAmount());
                                if (CommonUtils.isEmpty(revanueAmmount) && CommonUtils.isNotEmpty(costAmmount)) {
                                    if (headingFlag == false) {
                                        headingDetails(bookPiece);
                                        headingFlag = true;
                                    }
                                    createRow();
                                    resetColumnIndex();
                                    createHeaderCell("4. IPI (look at charge/cost code IPI) ", lclCellStyleLeftBoldArial10);
                                } else if (costAmmount > revanueAmmount) {
                                    if (headingFlag == false) {
                                        headingDetails(bookPiece);
                                        headingFlag = true;
                                    }
                                    createRow();
                                    resetColumnIndex();
                                    createHeaderCell("4. IPI (look at charge/cost code IPI) ", lclCellStyleLeftBoldArial10);
                                }
                                if (CommonUtils.isEmpty(revanueAmmount) && CommonUtils.isNotEmpty(costAmmount)) {
                                    customizedLclAlaramCell("IPI -> A – There exists IPI cost but no revenue ", revanueAmmount, costAmmount);
                                    alaramFlag = true;
                                }
                                //Commented temporarly By Client , Can be added in future
                                /*if (CommonUtils.isNotEmpty(revanueAmmount) && CommonUtils.isNotEmpty(costAmmount)) {
                                if (checkLessThanRevanueandCost(revanueAmmount, costAmmount)) {
                                customizedLclAlaramCell("IPI -> B -  difference between OF revenue and cost is less than 5% ", revanueAmmount, costAmmount);
                                alaramFlag = true;
                                }
                                }*/
                                if (costAmmount > revanueAmmount) {
                                    customizedLclAlaramCell("IPI ->C - IPI cost is higher than IPI revenue ", revanueAmmount, costAmmount);
                                    alaramFlag = true;
                                }
                                if (alaramFlag == true) {
                                    bookingFlag = true;
                                }
                            }
                            //6. Paid on Behalf (look at charge/cost code PAIDBH)
                            alaramFlag = false;
                            LclImpAlarmRevenueCostBean lbaPaid = new LclCostChargeDAO().getLclAlaramsRevanueAndCost(bookPiece.getLclFileNumber().getId(), "PAIDBH");
                            if (lbaPaid != null) {
                                revanueAmmount = Double.parseDouble(lbaPaid.getRevenueAmount() == null ? "0.00" : lbaPaid.getRevenueAmount());
                                costAmmount = Double.parseDouble(lbaPaid.getCostAmount() == null ? "0.00" : lbaPaid.getCostAmount());
                                if (CommonUtils.isNotEmpty(revanueAmmount) && CommonUtils.isEmpty(costAmmount)) {
                                    if (headingFlag == false) {
                                        headingDetails(bookPiece);
                                        headingFlag = true;
                                    }
                                    createRow();
                                    resetColumnIndex();
                                    createHeaderCell("6. Paid on Behalf (look at charge/cost code PAIDBH) ", lclCellStyleLeftBoldArial10);
                                } else if (CommonUtils.isNotEmpty(costAmmount) && CommonUtils.isEmpty(revanueAmmount)) {
                                    if (headingFlag == false) {
                                        headingDetails(bookPiece);
                                        headingFlag = true;
                                    }
                                    createRow();
                                    resetColumnIndex();
                                    createHeaderCell("6. Paid on Behalf (look at charge/cost code PAIDBH) ", lclCellStyleLeftBoldArial10);
                                } else if (CommonUtils.isNotEqual(costAmmount, revanueAmmount)) {
                                    if (headingFlag == false) {
                                        headingDetails(bookPiece);
                                        headingFlag = true;
                                    }
                                    createRow();
                                    resetColumnIndex();
                                    createHeaderCell("6. Paid on Behalf (look at charge/cost code PAIDBH) ", lclCellStyleLeftBoldArial10);
                                }
                                if (CommonUtils.isNotEmpty(revanueAmmount) && CommonUtils.isEmpty(costAmmount)) {
                                    customizedLclAlaramCell("PAIDBH -> A – POB revenue but no cost  ", revanueAmmount, costAmmount);
                                    alaramFlag = true;
                                }
                                if (CommonUtils.isNotEmpty(costAmmount) && CommonUtils.isEmpty(revanueAmmount)) {
                                    customizedLclAlaramCell("PAIDBH -> B - POB cost but no revenue  ", revanueAmmount, costAmmount);
                                    alaramFlag = true;
                                }
                                if (CommonUtils.isNotEqual(costAmmount, revanueAmmount)) {
                                    customizedLclAlaramCell("PAIDBH -> C - POB revenue does not equal cost", revanueAmmount, costAmmount);
                                    alaramFlag = true;
                                }
                                if (alaramFlag == true) {
                                    bookingFlag = true;
                                }
                            }
                            //**************************************************
                            alaramFlag = false;
                            //7. Hazardous surcharge (look at charge/cost code HAZFEE)
                            LclImpAlarmRevenueCostBean lbaHaz = new LclCostChargeDAO().getLclAlaramsRevanueAndCost(bookPiece.getLclFileNumber().getId(), "HAZFEE");
                            if (lbaHaz != null) {
                                revanueAmmount = Double.parseDouble(lbaHaz.getRevenueAmount() == null ? "0.00" : lbaHaz.getRevenueAmount());
                                costAmmount = Double.parseDouble(lbaHaz.getCostAmount() == null ? "0.00" : lbaHaz.getCostAmount());
                                if (CommonUtils.isEmpty(revanueAmmount) && CommonUtils.isEmpty(costAmmount)) {
                                    if (headingFlag == false) {
                                        headingDetails(bookPiece);
                                        headingFlag = true;
                                    }
                                    createRow();
                                    resetColumnIndex();
                                    createHeaderCell("7. Hazardous surcharge (look at charge/cost code HAZFEE)", lclCellStyleLeftBoldArial10);
                                    customizedLclAlaramCell("HAZFEE -> A - Shipment marked as HAZ but no revenue or cost entered ", revanueAmmount, costAmmount);
                                    alaramFlag = true;
                                }
                                if (CommonUtils.isNotEmpty(costAmmount) && CommonUtils.isEmpty(revanueAmmount)) {
                                    if (headingFlag == false) {
                                        headingDetails(bookPiece);
                                        headingFlag = true;
                                    }
                                    createRow();
                                    resetColumnIndex();
                                    createHeaderCell("7. Hazardous surcharge (look at charge/cost code HAZFEE)", lclCellStyleLeftBoldArial10);
                                    customizedLclAlaramCell("HAZFEE -> B - Haz cost but no revenue  ", revanueAmmount, costAmmount);
                                    alaramFlag = true;
                                }
                                if (alaramFlag == true) {
                                    bookingFlag = true;
                                }
                            }
                            //**************************************************
                            alaramFlag = false;
                            //8. Overweight surcharge (look at charge/cost code OVRWGT)
                            LclUnitSs lclUnitSstemp = new LclUnitSsDAO().findById(new Long(unitSSId));
                            LclSsHeader lclSsHeader = lclUnitSstemp.getLclSsHeader();
                            LclCostChargeDAO costChargeDAO = new LclCostChargeDAO();
                            String weightValues[] = costChargeDAO.getoverWeightCharge(lclSsHeader.getDestination().getUnLocationCode());
                            List<LclUnitSsManifest> lclUnitSsManifests = (List<LclUnitSsManifest>) lclUnitSstemp.getLclSsHeader().getLclUnitSsManifestList();
                            String unitSize = lclUnitSstemp.getLclUnit().getUnitType().getDescription();
                            totalKgs = lclUnitSsManifests.get(0).getCalculatedWeightMetric().doubleValue();
                            if (unitSize.length() > 4) {
                                unitSize = unitSize.substring(0, 4);
                            }
                            if ((unitSize.equalsIgnoreCase("20ft") && totalKgs > Double.parseDouble(weightValues[0])) || (unitSize.equalsIgnoreCase("40ft") && totalKgs > Double.parseDouble(weightValues[1]))) {
                                if ((null != weightValues[0] && !weightValues[0].toString().equals("0.00")) || (null != weightValues[1] && !weightValues[1].toString().equals("0.00"))) {
                                    //Dr Level
                                    List<LclBookingAc> lbaSubCharge = costChargeDAO.getLclBookingAcListByChargeCode(bookPiece.getLclFileNumber().getId(), "OVRWGT");
                                    //ArInvoice unit Level
                                    List<ArRedInvoiceCharges> arRedinvoiceCharge = new LclSsAcDAO().getArRedInvoiceChargesCode(unitNo, "OVRWGT");
                                    //ArInvoice DR Level
                                    List<ArRedInvoiceCharges> drLevelArRedinvoiceCharge = new LclSsAcDAO().getDRLevelArRedInvoiceChargesCode(bookPiece.getLclFileNumber().getFileNumber(), "OVRWGT");
                                    if (CommonUtils.isEmpty(lbaSubCharge) && CommonUtils.isEmpty(arRedinvoiceCharge) && CommonUtils.isEmpty(drLevelArRedinvoiceCharge)) {
                                        if (headingFlag == false) {
                                            headingDetails(bookPiece);
                                            headingFlag = true;
                                        }
                                        createRow();
                                        resetColumnIndex();
                                        createHeaderCell("8. Overweight surcharge (look at charge/cost code OVRWGT)", lclCellStyleLeftBoldArial10);
                                        alaramFlag = true;
                                    }
                                }

                            }
                            //**************************************************
                            alaramFlag = false;
                        }

                    }
                }
            }
        }
    }

    public void createEmptyCell() throws Exception {
        createCell();
        createCell();
        createCell();
        createCell();
        createCell();
        createCell();
        createCell();
    }

    public double getRevanueAmount(List<LclBookingAc> lbaList) {
        double revanueAmt = 0.0;
        for (LclBookingAc lba : lbaList) {
            if (lba.getArAmount() != null) {
                revanueAmt = revanueAmt + lba.getArAmount().doubleValue();
            }
        }
        return revanueAmt;
    }

    public double getCostAmount(List<LclBookingAc> lbaList) {
        double costAmt = 0.0;
        for (LclBookingAc lba : lbaList) {
            if (lba.getApAmount() != null) {
                costAmt = costAmt + lba.getApAmount().doubleValue();
            }
        }
        return costAmt;
    }

    public double getRevanueAmountSingle(LclBookingAc lba) {
        double revanueAmt = 0.0;
        if (lba.getArAmount() != null) {
            revanueAmt = lba.getArAmount().doubleValue();
        }
        return revanueAmt;
    }

    public double getCostAmountSsAc(List<LclSsAc> lbaList) {
        double costAmt = 0.0;
        for (LclSsAc lba : lbaList) {
            if (lba.getApAmount() != null) {
                costAmt = costAmt + lba.getApAmount().doubleValue();
            }
        }
        return costAmt;
    }

    boolean checkLessThanRevanueandCost(double revanue, double cost) {
        boolean returnValue = false;
        if (revanue > cost) {
            if (((((revanue - cost) / revanue) * 100) < 5)) {
                returnValue = true;
            }
        } else if (cost > revanue) {
            if (((((cost - revanue) / cost) * 100) < 5)) {
                returnValue = true;
            }
        }
        return returnValue;
    }

    boolean checkMoreThanRevanueandCost(double revanue, double cost) {
        boolean returnValue = false;
        if (revanue > cost) {
            if (((((revanue - cost) / revanue) * 100) > 5)) {
                returnValue = true;
            }
        } else if (cost > revanue) {
            if (((((cost - revanue) / cost) * 100) > 5)) {
                returnValue = true;
            }
        }
        return returnValue;
    }
}

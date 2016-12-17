/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.ratemangement.utills;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.struts.ratemangement.form.SearchFCLForm;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.reports.dto.FclRatesReportDTO;
import com.logiware.excel.BaseExcelCreator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Mei
 */
public class FclRatesReportExcelCreator extends BaseExcelCreator {

    private FclRatesReportDTO containerSizeDetails;

    private void writeHeader(SearchFCLForm searchFCLForm, User loginUser) throws IOException {
        createRow();
        createHeaderCell("PRINTED BY- " + loginUser.getLoginName(), cellStyleLeftBold);
        //  setCellBorder(cellStyleLeftBold, (short) 0, (short) 0, (short) 0, (short) 0);
        mergeCells(rowIndex, rowIndex, 0, 5);
        createRow();
        resetColumnIndex();
        if (CommonUtils.isNotEmpty(searchFCLForm.getOrigin())) {
            createHeaderCell("Origin: " + searchFCLForm.getOriginSchnum() + "-" + searchFCLForm.getOrigin(), cellStyleLeftBold);
        } else {
            createHeaderCell("Origin Region: " + searchFCLForm.getOriginName(), cellStyleLeftBold);
        }
        mergeCells(rowIndex, rowIndex, 0, 5);
        createRow();
        resetColumnIndex();
        if (CommonUtils.isNotEmpty(searchFCLForm.getDestination())) {
            createHeaderCell("Destination: " + searchFCLForm.getDestinationSchnum() + "-" + searchFCLForm.getDestination(), cellStyleLeftBold);
        } else {
            createHeaderCell("Destination Region: " + searchFCLForm.getDestinationName(), cellStyleLeftBold);
        }
        mergeCells(rowIndex, rowIndex, 0, 5);
        createRow();
        resetColumnIndex();
        createHeaderCell("Commodity: " + searchFCLForm.getCommodityNumber() + " " + searchFCLForm.getCommodityName(), cellStyleLeftBold);
        mergeCells(rowIndex, rowIndex, 0, 5);
        createRow();
        resetColumnIndex();
        createHeaderCell("Stream Ship Line: " + searchFCLForm.getSslinenumber() + " - " + searchFCLForm.getSslinename(), cellStyleLeftBold);
        mergeCells(rowIndex, rowIndex, 0, 8);
        createRow();
        resetColumnIndex();
        createHeaderCell("Cost Code Rate Sheet", cellStyleLeftBold);
        mergeCells(rowIndex, rowIndex, 0, 5);
        createRow();
        resetColumnIndex();
//        createHeaderCell("Customer Number", tableHeaderCellStyleCenterBold);
//        createHeaderCell("Notes Description", tableHeaderCellStyleCenterBold);
//        createHeaderCell("Followup Date", tableHeaderCellStyleCenterBold);
//        createHeaderCell("Created Date", tableHeaderCellStyleCenterBold);
//        createHeaderCell("Created By", tableHeaderCellStyleCenterBold);
    }

    private void writeContent(SearchFCLForm searchFCLForm) throws IOException, Exception {
        FclRatesReportUtills fclRatesReportUtills = new FclRatesReportUtills();
        String destinationRegion[] = {}, destinationName[] = {};
        if (CommonUtils.isNotEmpty(searchFCLForm.getDestinationSchnum())) {
            searchFCLForm.setDestinationSchnum(searchFCLForm.getDestinationSchnum() + ",");
            searchFCLForm.setDestination(searchFCLForm.getDestination() + ",");
            destinationRegion = searchFCLForm.getDestinationSchnum().split(",");
        } else {
            destinationRegion = searchFCLForm.getDestRegion().split(",");
            destinationName = searchFCLForm.getDestinationName().split(",");
        }
        for (int i = 0; i < destinationRegion.length; i++) {
            searchFCLForm.setDestRegion(destinationRegion[i].toString());
            List<FclRatesReportDTO> chargeCodeList = fclRatesReportUtills.getChargeCodeList(searchFCLForm);
            if (null != chargeCodeList && chargeCodeList.size() > 0) {
                List<FclRatesReportDTO> ratesList = fclRatesReportUtills.getFclRates(searchFCLForm);
                FclRatesReportDTO totalChargeAmt = new FclRatesReportDTO();
                totalChargeAmt.setCostCodeDesc("Total");
                chargeCodeList.add(totalChargeAmt);
                for (int j = 0; j < chargeCodeList.size(); j++) {
                    FclRatesReportDTO chargeCode = chargeCodeList.get(j);
                    if ("O/F".equalsIgnoreCase(chargeCode.getCostCodeDesc())) {
                        FclRatesReportDTO markuP = new FclRatesReportDTO();
                        markuP.setCostCodeDesc("mrkup");
                        chargeCodeList.add(j + 1, markuP);
                    }
                    if ("Haz".equalsIgnoreCase(chargeCode.getCostCodeDesc())) {
                        FclRatesReportDTO markuP = new FclRatesReportDTO();
                        markuP.setCostCodeDesc("mrkup");
                        chargeCodeList.add(j + 1, markuP);
                    }
                    if ("RAMP".equalsIgnoreCase(chargeCode.getCostCodeDesc())) {
                        FclRatesReportDTO markuP = new FclRatesReportDTO();
                        markuP.setCostCodeDesc("mrkup");
                        chargeCodeList.add(j + 1, markuP);
                    }
                }
                List<FclRatesReportDTO> containerList = setContainerList(searchFCLForm, fclRatesReportUtills);
                Map<String, Map<String, FclRatesReportDTO>> originMap = new HashMap();
                FclRatesReportDTO ratesDto = null;
                for (FclRatesReportDTO rates : ratesList) {
                    Map<String, FclRatesReportDTO> destMap;
                    String destKey = rates.getFdName();
                    String orgKey = rates.getOriginName();
                    if (originMap.containsKey(orgKey)) {
                        destMap = originMap.get(orgKey);
                    } else {
                        destMap = new HashMap();
                        originMap.put(orgKey, destMap);
                    }
                    if (destMap.containsKey(destKey)) {
                        ratesDto = destMap.get(destKey);
                        ratesDto.getMap().put(rates.getCostCodeDesc(), rates);

                    } else {
                        ratesDto = rates;
                        ratesDto.getMap().put(rates.getCostCodeDesc(), ratesDto);
                    }
                    destMap.put(destKey, ratesDto);
                }

                createRow();
                if (CommonUtils.isNotEmpty(searchFCLForm.getDestinationSchnum())) {
                    createHeaderCell("" + searchFCLForm.getDestinationSchnum().replaceAll(",", "") + " - " + searchFCLForm.getDestination(), redCellStyleLeftBold);
                } else {
                    createHeaderCell("" + destinationName[i].toString(), redCellStyleLeftBold);
                }
                mergeCells(rowIndex, rowIndex, 0, 4);
                setCellBorder(redCellStyleLeftBold, (short) 0, (short) 0, (short) 0, (short) 0);
                resetColumnIndex();
                createRow();
                resetColumnIndex();
                for (Map.Entry<String, Map<String, FclRatesReportDTO>> entry : originMap.entrySet()) {
                    createRow();
                    resetColumnIndex();
                    createRow();
                    createHeaderCell("" + entry.getKey(), lightAshCellStyleLeftBold);
                    setCellBorder(lightAshCellStyleLeftBold, (short) 0, (short) 0, (short) 0, (short) 0);
                    mergeCells(rowIndex, rowIndex, 0, 4);
                    resetColumnIndex();
                    createRow();
                    resetColumnIndex();
                    createRow();
                    resetColumnIndex();
                    createHeaderCell("", cellStyleLeftBold);
                    createHeaderCell("", cellStyleLeftBold);
                    createHeaderCell("", cellStyleLeftBold);
                    createHeaderCell("", cellStyleLeftBold);
                    createHeaderCell("", cellStyleLeftBold);
                    createHeaderCell("", cellStyleLeftBold);
                    createHeaderCell("", cellStyleLeftBold);
                    for (FclRatesReportDTO container : containerList) {
                        for (FclRatesReportDTO chargeCodeLis : chargeCodeList) {
                            createHeaderCell("" + container.getContainerSize(), cellStyleLeftBold);
                        }
                    }
                    createRow();
                    resetColumnIndex();
                    createHeaderCell("Destination", cellStyleLeftBold);
                    createHeaderCell("Origin", cellStyleCenterBold);
                    createHeaderCell("Pol", cellStyleCenterBold);
                    createHeaderCell("Pod", cellStyleCenterBold);
                    createHeaderCell("Fd", cellStyleCenterBold);
                    createHeaderCell("TT", cellStyleCenterBold);
                    createHeaderCell("Remarks", cellStyleCenterBold);
                    for (FclRatesReportDTO container : containerList) {
                        for (FclRatesReportDTO charge : chargeCodeList) {
                            createHeaderCell("" + charge.getCostCodeDesc(), cellStyleCenterBold);
                        }
                    }
                    createRow();
                    resetColumnIndex();
                    for (Map.Entry<String, FclRatesReportDTO> entrys : entry.getValue().entrySet()) {
                        FclRatesReportDTO fdKeyValues = entrys.getValue();
                        createHeaderCell("" + entrys.getKey(), cellStyleLeftBold);
                        if ("00000".equalsIgnoreCase(fdKeyValues.getOriginSchnum())) {
                            createHeaderCell("", cellStyleLeftBold);
                        } else {
                            createHeaderCell("" + fdKeyValues.getOriginSchnum(), cellStyleLeftBold);
                        }
                        if ("00000".equalsIgnoreCase(fdKeyValues.getPolSchnum())) {
                            createHeaderCell("", cellStyleLeftBold);
                        } else {
                            createHeaderCell("" + fdKeyValues.getPolSchnum(), cellStyleLeftBold);
                        }
                        if ("00000".equalsIgnoreCase(fdKeyValues.getPodSchnum())) {
                            createHeaderCell("", cellStyleLeftBold);
                        } else {
                            createHeaderCell("" + fdKeyValues.getPodSchnum(), cellStyleLeftBold);
                        }
                        if ("00000".equalsIgnoreCase(fdKeyValues.getFdSchnum())) {
                            createHeaderCell("", cellStyleLeftBold);
                        } else {
                            createHeaderCell("" + fdKeyValues.getFdSchnum(), cellStyleLeftBold);
                        }
                        createHeaderCell("" + fdKeyValues.getTransitDays(), cellStyleLeftBold);
                        createHeaderCell("" + fdKeyValues.getRemarks(), cellStyleLeftBold);
                        Map<String, FclRatesReportDTO> chargeMap = entrys.getValue().getMap();

                        Double totala20Amt = 0.0;
                        Double markUp = 0.0;
                        for (FclRatesReportDTO a20Charge : chargeCodeList) {
                            FclRatesReportDTO dto = chargeMap.get(a20Charge.getCostCodeDesc());
                            if (null != dto && null != dto.getA20Amt() && !"0.00".equalsIgnoreCase(dto.getA20Amt())) {
                                totala20Amt += Double.parseDouble(dto.getA20Amt());
                                createHeaderCell("" + dto.getA20Amt(), cellStyleLeftBold);
                                if ("O/F".equalsIgnoreCase(dto.getCostCodeDesc())) {
                                    createHeaderCell("" + dto.getMrkupa(), cellStyleLeftBold);
                                    totala20Amt += Double.parseDouble(dto.getMrkupa());
                                }
                                if ("HAZ".equalsIgnoreCase(dto.getCostCodeDesc())) {
                                    createHeaderCell("" + dto.getMrkupa(), cellStyleLeftBold);
                                    totala20Amt += Double.parseDouble(dto.getMrkupa());
                                }
                                if ("RAMP".equalsIgnoreCase(dto.getCostCodeDesc())) {
                                    createHeaderCell("" + dto.getMrkupa(), cellStyleLeftBold);
                                    totala20Amt += Double.parseDouble(dto.getMrkupa());
                                }
                            } else if (a20Charge.getCostCodeDesc().equalsIgnoreCase("Total")) {
                                createHeaderCell("" + totala20Amt, cellStyleLeftBold);
                            } else if (!a20Charge.getCostCodeDesc().equalsIgnoreCase("mrkup")) {

                                createHeaderCell("", cellStyleLeftBold);
                                if (dto == null) {
                                    if ("O/F".equalsIgnoreCase(a20Charge.getCostCodeDesc())) {
                                        createHeaderCell("", cellStyleLeftBold);
                                    }
                                    if ("HAZ".equalsIgnoreCase(a20Charge.getCostCodeDesc())) {
                                        createHeaderCell("", cellStyleLeftBold);
                                    }
                                    if ("RAMP".equalsIgnoreCase(a20Charge.getCostCodeDesc())) {
                                        createHeaderCell("", cellStyleLeftBold);
                                    }
                                }
                            }
                        }
                        // createHeaderCell("To", cellStyleLeftBold);
                        Double totalb40Amt = 0.0;
                        for (FclRatesReportDTO charge : chargeCodeList) {
                            FclRatesReportDTO dto = chargeMap.get(charge.getCostCodeDesc());
                            if (null != dto && null != dto.getB40Amt() && !"0.00".equalsIgnoreCase(dto.getB40Amt())) {
                                totalb40Amt += Double.parseDouble(dto.getB40Amt());
                                createHeaderCell("" + dto.getB40Amt(), cellStyleLeftBold);
                                if ("O/F".equalsIgnoreCase(dto.getCostCodeDesc())) {
                                    totalb40Amt += Double.parseDouble(dto.getMrkupb());
                                    createHeaderCell("" + dto.getMrkupb(), cellStyleLeftBold);
                                }
                                if ("HAZ".equalsIgnoreCase(dto.getCostCodeDesc())) {
                                    totalb40Amt += Double.parseDouble(dto.getMrkupb());
                                    createHeaderCell("" + dto.getMrkupb(), cellStyleLeftBold);
                                }
                                if ("RAMP".equalsIgnoreCase(dto.getCostCodeDesc())) {
                                    totalb40Amt += Double.parseDouble(dto.getMrkupb());
                                    createHeaderCell("" + dto.getMrkupb(), cellStyleLeftBold);
                                }
                            } else if (charge.getCostCodeDesc().equalsIgnoreCase("Total")) {
                                createHeaderCell("" + totalb40Amt, cellStyleLeftBold);
                            } else if (!charge.getCostCodeDesc().equalsIgnoreCase("mrkup")) {
                                createHeaderCell("", cellStyleLeftBold);
                                if (dto == null) {
                                    if ("O/F".equalsIgnoreCase(charge.getCostCodeDesc())) {
                                        createHeaderCell("", cellStyleLeftBold);
                                    }
                                    if ("HAZ".equalsIgnoreCase(charge.getCostCodeDesc())) {
                                        createHeaderCell("", cellStyleLeftBold);
                                    }
                                    if ("RAMP".equalsIgnoreCase(charge.getCostCodeDesc())) {
                                        createHeaderCell("", cellStyleLeftBold);
                                    }
                                }
                            }
                        }
                        //createHeaderCell("To", cellStyleLeftBold);
                        Double totalc40Amt = 0.0;
                        for (FclRatesReportDTO c40hcCharge : chargeCodeList) {
                            FclRatesReportDTO dto = chargeMap.get(c40hcCharge.getCostCodeDesc());
                            if (null != dto && null != dto.getC40hcAmt() && !"0.00".equalsIgnoreCase(dto.getC40hcAmt())) {
                                totalc40Amt += Double.parseDouble(dto.getC40hcAmt());
                                createHeaderCell("" + dto.getC40hcAmt(), cellStyleLeftBold);
                                if ("O/F".equalsIgnoreCase(dto.getCostCodeDesc())) {
                                    totalc40Amt += Double.parseDouble(dto.getMrkupc());
                                    createHeaderCell("" + dto.getMrkupc(), cellStyleLeftBold);
                                }
                                if ("HAZ".equalsIgnoreCase(dto.getCostCodeDesc())) {
                                    totalc40Amt += Double.parseDouble(dto.getMrkupc());
                                    createHeaderCell("" + dto.getMrkupc(), cellStyleLeftBold);
                                }
                                if ("RAMP".equalsIgnoreCase(dto.getCostCodeDesc())) {
                                    totalc40Amt += Double.parseDouble(dto.getMrkupc());
                                    createHeaderCell("" + dto.getMrkupc(), cellStyleLeftBold);
                                }
                            } else if (c40hcCharge.getCostCodeDesc().equalsIgnoreCase("Total")) {
                                createHeaderCell("" + totalc40Amt, cellStyleLeftBold);
                            } else if (!c40hcCharge.getCostCodeDesc().equalsIgnoreCase("mrkup")) {
                                createHeaderCell("", cellStyleLeftBold);
                                if (dto == null) {
                                    if ("O/F".equalsIgnoreCase(c40hcCharge.getCostCodeDesc())) {
                                        createHeaderCell("", cellStyleLeftBold);
                                    }
                                    if ("HAZ".equalsIgnoreCase(c40hcCharge.getCostCodeDesc())) {
                                        createHeaderCell("", cellStyleLeftBold);
                                    }
                                    if ("RAMP".equalsIgnoreCase(c40hcCharge.getCostCodeDesc())) {
                                        createHeaderCell("", cellStyleLeftBold);
                                    }
                                }
                            }
                        }
                        if (!"0.00".equalsIgnoreCase(containerSizeDetails.getD45Amt())) {
                            Double totald45Amt = 0.0;
                            for (FclRatesReportDTO d45Charge : chargeCodeList) {
                                FclRatesReportDTO dto = chargeMap.get(d45Charge.getCostCodeDesc());
                                if (null != dto && null != dto.getD45Amt() && !"0.00".equalsIgnoreCase(dto.getD45Amt())) {
                                    totald45Amt += Double.parseDouble(dto.getD45Amt());
                                    createHeaderCell("" + dto.getD45Amt(), cellStyleLeftBold);
                                    if ("O/F".equalsIgnoreCase(dto.getCostCodeDesc())) {
                                        totald45Amt += Double.parseDouble(dto.getMrkupc());
                                        createHeaderCell("" + dto.getMrkupc(), cellStyleLeftBold);
                                    }
                                    if ("HAZ".equalsIgnoreCase(dto.getCostCodeDesc())) {
                                        totald45Amt += Double.parseDouble(dto.getMrkupc());
                                        createHeaderCell("" + dto.getMrkupc(), cellStyleLeftBold);
                                    }
                                    if ("RAMP".equalsIgnoreCase(dto.getCostCodeDesc())) {
                                        totald45Amt += Double.parseDouble(dto.getMrkupc());
                                        createHeaderCell("" + dto.getMrkupc(), cellStyleLeftBold);
                                    }
                                } else if (d45Charge.getCostCodeDesc().equalsIgnoreCase("Total") && totald45Amt != 0.0) {
                                    createHeaderCell("" + totald45Amt, cellStyleLeftBold);
                                } else if (!d45Charge.getCostCodeDesc().equalsIgnoreCase("mrkup")) {
                                    createHeaderCell("", cellStyleLeftBold);
                                    if (dto == null) {
                                        if ("O/F".equalsIgnoreCase(d45Charge.getCostCodeDesc())) {
                                            createHeaderCell("", cellStyleLeftBold);
                                        }
                                        if ("HAZ".equalsIgnoreCase(d45Charge.getCostCodeDesc())) {
                                            createHeaderCell("", cellStyleLeftBold);
                                        }
                                        if ("RAMP".equalsIgnoreCase(d45Charge.getCostCodeDesc())) {
                                            createHeaderCell("", cellStyleLeftBold);
                                        }
                                    }
                                }
                            }
                        }
                        if (!"0.00".equalsIgnoreCase(containerSizeDetails.getE48Amt())) {
                            Double totale48Amt = 0.0;
                            for (FclRatesReportDTO e48Charge : chargeCodeList) {
                                FclRatesReportDTO dto = chargeMap.get(e48Charge.getCostCodeDesc());
                                if (null != dto && null != dto.getE48Amt() && !"0.00".equalsIgnoreCase(dto.getE48Amt())) {
                                    totale48Amt += Double.parseDouble(dto.getE48Amt());
                                    createHeaderCell("" + dto.getE48Amt(), cellStyleLeftBold);
                                    if ("O/F".equalsIgnoreCase(dto.getCostCodeDesc())) {
                                        totale48Amt += Double.parseDouble(dto.getMrkupc());
                                        createHeaderCell("" + dto.getMrkupc(), cellStyleLeftBold);
                                    }
                                    if ("HAZ".equalsIgnoreCase(dto.getCostCodeDesc())) {
                                        totale48Amt += Double.parseDouble(dto.getMrkupc());
                                        createHeaderCell("" + dto.getMrkupc(), cellStyleLeftBold);
                                    }
                                    if ("RAMP".equalsIgnoreCase(dto.getCostCodeDesc())) {
                                        totale48Amt += Double.parseDouble(dto.getMrkupc());
                                        createHeaderCell("" + dto.getMrkupc(), cellStyleLeftBold);
                                    }
                                } else if (e48Charge.getCostCodeDesc().equalsIgnoreCase("Total") && totale48Amt != 0.0) {
                                    createHeaderCell("" + totale48Amt, cellStyleLeftBold);
                                } else if (!e48Charge.getCostCodeDesc().equalsIgnoreCase("mrkup")) {
                                    createHeaderCell("", cellStyleLeftBold);
                                    if (dto == null) {
                                        if ("O/F".equalsIgnoreCase(e48Charge.getCostCodeDesc())) {
                                            createHeaderCell("", cellStyleLeftBold);
                                        }
                                        if ("HAZ".equalsIgnoreCase(e48Charge.getCostCodeDesc())) {
                                            createHeaderCell("", cellStyleLeftBold);
                                        }
                                        if ("RAMP".equalsIgnoreCase(e48Charge.getCostCodeDesc())) {
                                            createHeaderCell("", cellStyleLeftBold);
                                        }
                                    }
                                }
                            }
                        }
                        if (!"0.00".equalsIgnoreCase(containerSizeDetails.getF40norAmt())) {
                            Double totalf40norAmt = 0.0;
                            for (FclRatesReportDTO f40Charge : chargeCodeList) {
                                FclRatesReportDTO dto = chargeMap.get(f40Charge.getCostCodeDesc());
                                if (null != dto && null != dto.getF40norAmt() && !"0.00".equalsIgnoreCase(dto.getF40norAmt())) {
                                    totalf40norAmt += Double.parseDouble(dto.getF40norAmt());
                                    createHeaderCell("" + dto.getF40norAmt(), cellStyleLeftBold);
                                    if ("O/F".equalsIgnoreCase(dto.getCostCodeDesc())) {
                                        totalf40norAmt += Double.parseDouble(dto.getMrkupc());
                                        createHeaderCell("" + dto.getMrkupc(), cellStyleLeftBold);
                                    }
                                    if ("HAZ".equalsIgnoreCase(dto.getCostCodeDesc())) {
                                        totalf40norAmt += Double.parseDouble(dto.getMrkupc());
                                        createHeaderCell("" + dto.getMrkupc(), cellStyleLeftBold);
                                    }
                                    if ("RAMP".equalsIgnoreCase(dto.getCostCodeDesc())) {
                                        totalf40norAmt += Double.parseDouble(dto.getMrkupc());
                                        createHeaderCell("" + dto.getMrkupc(), cellStyleLeftBold);
                                    }
                                } else if (f40Charge.getCostCodeDesc().equalsIgnoreCase("Total") && totalf40norAmt != 0.0) {
                                    createHeaderCell("" + totalf40norAmt, cellStyleLeftBold);
                                } else if (!f40Charge.getCostCodeDesc().equalsIgnoreCase("mrkup")) {
                                    createHeaderCell("", cellStyleLeftBold);
                                    if (dto == null) {
                                        if ("O/F".equalsIgnoreCase(f40Charge.getCostCodeDesc())) {
                                            createHeaderCell("", cellStyleLeftBold);
                                        }
                                        if ("HAZ".equalsIgnoreCase(f40Charge.getCostCodeDesc())) {
                                            createHeaderCell("", cellStyleLeftBold);
                                        }
                                        if ("RAMP".equalsIgnoreCase(f40Charge.getCostCodeDesc())) {
                                            createHeaderCell("", cellStyleLeftBold);
                                        }
                                    }
                                }
                            }
                        }
                        if (!"0.00".equalsIgnoreCase(containerSizeDetails.getG45102Amt())) {
                            Double totalg45Amt = 0.0;
                            for (FclRatesReportDTO g45Charge : chargeCodeList) {
                                FclRatesReportDTO dto = chargeMap.get(g45Charge.getCostCodeDesc());
                                if (null != dto && null != dto.getG45102Amt() && !"0.00".equalsIgnoreCase(dto.getG45102Amt())) {
                                    totalg45Amt += Double.parseDouble(dto.getG45102Amt());
                                    createHeaderCell("" + dto.getG45102Amt(), cellStyleLeftBold);
                                    if ("O/F".equalsIgnoreCase(dto.getCostCodeDesc())) {
                                        totalg45Amt += Double.parseDouble(dto.getMrkupc());
                                        createHeaderCell("" + dto.getMrkupc(), cellStyleLeftBold);
                                    }
                                    if ("HAZ".equalsIgnoreCase(dto.getCostCodeDesc())) {
                                        totalg45Amt += Double.parseDouble(dto.getMrkupc());
                                        createHeaderCell("" + dto.getMrkupc(), cellStyleLeftBold);
                                    }
                                    if ("RAMP".equalsIgnoreCase(dto.getCostCodeDesc())) {
                                        totalg45Amt += Double.parseDouble(dto.getMrkupc());
                                        createHeaderCell("" + dto.getMrkupc(), cellStyleLeftBold);
                                    }
                                } else if (g45Charge.getCostCodeDesc().equalsIgnoreCase("Total") && totalg45Amt != 0.0) {
                                    createHeaderCell("" + totalg45Amt, cellStyleLeftBold);
                                } else if (!g45Charge.getCostCodeDesc().equalsIgnoreCase("mrkup")) {
                                    createHeaderCell("", cellStyleLeftBold);
                                    if (dto == null) {
                                        if ("O/F".equalsIgnoreCase(g45Charge.getCostCodeDesc())) {
                                            createHeaderCell("", cellStyleLeftBold);
                                        }
                                        if ("HAZ".equalsIgnoreCase(g45Charge.getCostCodeDesc())) {
                                            createHeaderCell("", cellStyleLeftBold);
                                        }
                                        if ("RAMP".equalsIgnoreCase(g45Charge.getCostCodeDesc())) {
                                            createHeaderCell("", cellStyleLeftBold);
                                        }
                                    }
                                }
                            }
                        }
                        createRow();
                        resetColumnIndex();
                    }
                }
            }
        }

//        List<FclRatesReportDTO> containerList = setContainerList();
//        createRow();
//        resetColumnIndex();
//        int i = 0;
//        FclRatesReportDTO ratesDto = null;
//        Map<String, FclRatesReportDTO> lclRemarksMap = new LinkedHashMap();
//        for (FclRatesReportDTO rates : ratesList) {
//            if (lclRemarksMap.containsKey(rates.getOriginName())) {
//                ratesDto = lclRemarksMap.get(rates.getOriginName());
//            } else {
//                ratesDto = rates;
//            }
//            ratesDto.addCharge(rates);
//            lclRemarksMap.put(rates.getOriginName(), ratesDto);
//        }
//        Map<String, FclRatesReportDTO> unitChargeMap = new LinkedHashMap();
////        for (Map.Entry<String, FclRatesReportDTO> entry : lclRemarksMap.entrySet()) {
////            String key = entry.getKey();
////            System.out.println("key--->" + key);
////            System.out.println("value--?" + entry.getValue());
////            for (FclRatesReportDTO rates : entry.getValue().getChargeList()) {
////                System.out.println("" + rates.getOriginName());
////                System.out.println("" + rates.getFdName());
////                System.out.println("" + rates.getA20Amt());
////                for (FclRatesReportDTO container : containerList) {
////                    for (FclRatesReportDTO charge : chargeCodeList) {
////                        String keyValue = container.getContainerSize() + charge.getCostCodeDesc().subSequence(0, 4);
////                        if (unitChargeMap.containsKey(keyValue)) {
////                        } else {
////                        }
////                        //  unitChargeMap.put(keyValue, charge)
////                    }
////                }
////            }
////        }
//        Map<String, FclRatesReportDTO> chargesMap = new LinkedHashMap();
//
//        for (Map.Entry<String, FclRatesReportDTO> entry : lclRemarksMap.entrySet()) {
//            FclRatesReportDTO listValue = lclRemarksMap.get(entry.getKey());
//            for (FclRatesReportDTO ratesL : listValue.getChargeList()) {
//                if (lclRemarksMap.containsKey(ratesL.getFdName())) {
//                    ratesDto = lclRemarksMap.get(ratesL.getFdName());
//                } else {
//                    ratesDto = ratesL;
//                }
//                ratesDto.addCharge(ratesL);
//                lclRemarksMap.put(ratesL.getFdName(), ratesDto);
//            }
//        }
//
//        final int rowValue = 7;
//        row = sheet.getRow(rowValue);
//        for (Map.Entry<String, FclRatesReportDTO> entry : lclRemarksMap.entrySet()) {
//            int rowCell = 0;
//            String values = entry.getKey();
//            createRow();
//            createHeaderCell("" + values, cellStyleLeftBold);
//            //     mergeCells(rowIndex, rowIndex, 0, 4);
//            resetColumnIndex();
//            for (FclRatesReportDTO container : containerList) {
//                for (FclRatesReportDTO lis : chargeCodeList) {
//                    createHeaderCell("" + container.getContainerSize(), cellStyleLeftBold);
//                }
//            }
//            createRow();
//            resetColumnIndex();
//            for (FclRatesReportDTO container : containerList) {
//                //createHeaderCell("" + container.getContainerSize(), cellStyleLeftBold);
//                for (FclRatesReportDTO charge : chargeCodeList) {
//                    createHeaderCell("" + charge.getCostCodeDesc().substring(0, 4), cellStyleLeftBold);
//                }
//            }
//            createRow();
//            resetColumnIndex();
//            System.out.println("---" + values);
//            FclRatesReportDTO listValue = lclRemarksMap.get(values);
//            System.out.println("listValue" + listValue);
//            String destinationNAme = "";
//            String originalName = "";
//            for (FclRatesReportDTO charge : chargeCodeList) {
//                FclRatesReportDTO dto = listValue.getMap().get(charge.getCostCodeDesc());
//                dto.getA20Amt();
//            }
//            for (FclRatesReportDTO charge : chargeCodeList) {
//                FclRatesReportDTO dto = listValue.getMap().get(charge.getCostCodeDesc());
//                dto.getB40Amt();
//            }
//            for (FclRatesReportDTO charge : chargeCodeList) {
//                FclRatesReportDTO dto = listValue.getMap().get(charge.getCostCodeDesc());
//                dto.getC40hcAmt();
//            }
//            for (FclRatesReportDTO rates : listValue.getChargeList()) {
//                originalName = rates.getFdName();
//                if (!destinationNAme.equalsIgnoreCase(originalName)) {
//                    System.out.println("originalName---" + originalName);
//                    System.out.println("destinationNAme---" + destinationNAme);
//                    createRow();
//                    resetColumnIndex();
//                    rowCell = 0;
//                    System.out.println("destinationNAme---" + rowCell);
//                }
//                destinationNAme = rates.getFdName();
//                System.out.println("rowValue---" + rowValue);
//                System.out.println("rowCell---" + rowCell + "row---" + row);
//                cell = row.getCell(rowCell);
//                System.out.println("cell--" + cell);
//                //System.out.println("cell1--" + cell.getStringCellValue());
//                if (null != cell && cell.getStringCellValue().equalsIgnoreCase(rates.getCostCodeDesc().substring(0, 4))) {
//                    createHeaderCell("" + rates.getA20Amt(), cellStyleLeftNormal);
//                } else {
//                    createHeaderCell("", cellStyleLeftNormal);
//
//                }
//
//                rowCell++;
//                //System.out.println("rate" + rates);
//            }
//        }
////        if (null != originList) {
////            for (FclRatesReportDTO origin : originList) {
////                createHeaderCell("" + origin.getOriginName(), cellStyleLeftBold);
////                mergeCells(rowIndex, rowIndex, 0, 4);
////                createRow();
////                resetColumnIndex();
////                for (FclRatesReportDTO container : containerList) {
////                    for (FclRatesReportDTO lis : chargeCodeList) {
////                        createHeaderCell("" + container.getContainerSize(), cellStyleLeftBold);
////                    }
////                }
////                createRow();
////                resetColumnIndex();
////                for (FclRatesReportDTO container : containerList) {
////                    //createHeaderCell("" + container.getContainerSize(), cellStyleLeftBold);
////                    for (FclRatesReportDTO charge : chargeCodeList) {
////                        createHeaderCell("" + charge.getCostCodeDesc().substring(0, 4), cellStyleLeftBold);
////                    }
////                }
////                createRow();
////                resetColumnIndex();
////                for (FclRatesReportDTO container : containerList) {
////                    for (FclRatesReportDTO rates : ratesList) {
////                        FclRatesReportDTO rate = ratesList.get(i);
////                        if ("A20".equalsIgnoreCase(container.getContainerSize())) {
////                            createHeaderCell("" + rate.getA20Amt(), cellStyleLeftBold);
////                        }
////                        if ("B40".equalsIgnoreCase(container.getContainerSize())) {
////                            createHeaderCell("" + rate.getB40Amt(), cellStyleLeftBold);
////                        }
////                        if ("C40HC".equalsIgnoreCase(container.getContainerSize())) {
////                            createHeaderCell("" + rate.getC40hcAmt(), cellStyleLeftBold);
////                        }
////                        if ("D45".equalsIgnoreCase(container.getContainerSize())) {
////                            createHeaderCell("" + rate.getD45Amt(), cellStyleLeftBold);
////                        }
////                        if ("E48".equalsIgnoreCase(container.getContainerSize())) {
////                            createHeaderCell("" + rate.getE48Amt(), cellStyleLeftBold);
////                        }
////                        if ("F40NOR".equalsIgnoreCase(container.getContainerSize())) {
////                            createHeaderCell("" + rate.getF40norAmt(), cellStyleLeftBold);
////                        }
////                        if ("G45102".equalsIgnoreCase(container.getContainerSize())) {
////                            createHeaderCell("" + rate.getG45102Amt(), cellStyleLeftBold);
////                        }
////                        if ("G45102".equalsIgnoreCase(container.getContainerSize())) {
////                            createHeaderCell("" + rate.getG45102Amt(), cellStyleLeftBold);
////                        }
////                    }
////                }
////                createRow();
////                resetColumnIndex();
////                i++;
////            }
////        }
////        for (FclRatesReportDTO origin : originList) {
////            for (Map.Entry<String, List<FclRatesReportDTO>> entry : lclRemarksMap.entrySet()) {
////                for (FclRatesReportDTO container : containerList) {
////                    String key = entry.getKey();
////                    remarksList = lclRemarksMap.get(key);
////                    for (FclRatesReportDTO charge : chargeCodeList) {
////                        for (FclRatesReportDTO rate : remarksList) {
////                            if ("A20".equalsIgnoreCase(container.getContainerSize())) {
////                                if (rate.getA20Amt() != null) {
////                                    createHeaderCell("" + rate.getA20Amt(), cellStyleLeftBold);
////                                } else {
////                                    createHeaderCell("", cellStyleLeftBold);
////                                }
////                                break;
////                            }
////                            if ("B40".equalsIgnoreCase(container.getContainerSize())) {
////                                createHeaderCell("" + rate.getB40Amt(), cellStyleLeftBold);
////                            }
////                            if ("C40HC".equalsIgnoreCase(container.getContainerSize())) {
////                                createHeaderCell("" + rate.getC40hcAmt(), cellStyleLeftBold);
////                            }
////                            if ("D45".equalsIgnoreCase(container.getContainerSize())) {
////                                createHeaderCell("" + rate.getD45Amt(), cellStyleLeftBold);
////                            }
////                            if ("E48".equalsIgnoreCase(container.getContainerSize())) {
////                                createHeaderCell("" + rate.getE48Amt(), cellStyleLeftBold);
////                            }
////                            if ("F40NOR".equalsIgnoreCase(container.getContainerSize())) {
////                                createHeaderCell("" + rate.getF40norAmt(), cellStyleLeftBold);
////                            }
////                            if ("G45102".equalsIgnoreCase(container.getContainerSize())) {
////                                createHeaderCell("" + rate.getG45102Amt(), cellStyleLeftBold);
////                            }
////                            if ("G45102".equalsIgnoreCase(container.getContainerSize())) {
////                                createHeaderCell("" + rate.getG45102Amt(), cellStyleLeftBold);
////                            }
////                        }
////                    }
////                }
////                createRow();
////                resetColumnIndex();
////            }
////            createRow();
////            resetColumnIndex();
////        }
    }

    public String createExcel(SearchFCLForm searchFCLForm, User loginUser) throws Exception {
        try {
            String xlsSheetName = "FCL RATES REPORT";
            StringBuilder outFileName = new StringBuilder();
            outFileName.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/FclReport/Statement/");
            outFileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
            File file = new File(outFileName.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            outFileName.append("fclRatesReport-").append(searchFCLForm.getSslinenumber()).append(".xlsx");
            init(outFileName.toString(), xlsSheetName);
            writeHeader(searchFCLForm, loginUser);
            writeContent(searchFCLForm);
            writeIntoFile();
            return outFileName.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            exit();
        }
    }

    public List<FclRatesReportDTO> setContainerList(SearchFCLForm searchFCLForm, FclRatesReportUtills fclRatesReportUtills) throws Exception {
        List<FclRatesReportDTO> containerList = new ArrayList<FclRatesReportDTO>();
        List<FclRatesReportDTO> containerSize = fclRatesReportUtills.getContainerSize(searchFCLForm);
        containerSizeDetails = containerSize.get(0);

        if (!"0.00".equalsIgnoreCase(containerSizeDetails.getA20Amt())) {
            FclRatesReportDTO a20Size = new FclRatesReportDTO();
            a20Size.setContainerSize("A20");
            containerList.add(a20Size);
        }
        if (!"0.00".equalsIgnoreCase(containerSizeDetails.getB40Amt())) {
            FclRatesReportDTO a20Size = new FclRatesReportDTO();
            a20Size.setContainerSize("B40");
            containerList.add(a20Size);
        }
        if (!"0.00".equalsIgnoreCase(containerSizeDetails.getC40hcAmt())) {
            FclRatesReportDTO a20Size = new FclRatesReportDTO();
            a20Size.setContainerSize("C40HC");
            containerList.add(a20Size);
        }
        if (!"0.00".equalsIgnoreCase(containerSizeDetails.getD45Amt())) {
            FclRatesReportDTO a20Size = new FclRatesReportDTO();
            a20Size.setContainerSize("D45");
            containerList.add(a20Size);
        }
        if (!"0.00".equalsIgnoreCase(containerSizeDetails.getE48Amt())) {
            FclRatesReportDTO a20Size = new FclRatesReportDTO();
            a20Size.setContainerSize("E48");
            containerList.add(a20Size);
        }
        if (!"0.00".equalsIgnoreCase(containerSizeDetails.getF40norAmt())) {
            FclRatesReportDTO a20Size = new FclRatesReportDTO();
            a20Size.setContainerSize("F40NOR");
            containerList.add(a20Size);
        }
        if (!"0.00".equalsIgnoreCase(containerSizeDetails.getG45102Amt())) {
            FclRatesReportDTO a20Size = new FclRatesReportDTO();
            a20Size.setContainerSize("G45102");
            containerList.add(a20Size);
        }





//        String container[] = {"A20", "B40", "C40HC", "D45", "E48", "F40NOR", "G45102"};
//        for (int i = 0; i < container.length; i++) {
//            FclRatesReportDTO conat = new FclRatesReportDTO();
//            conat.setContainerSize(container[i].toString());
//            containerList.add(conat);
//        }
        return containerList;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitWhseDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.excel.BaseExcelCreator;
import java.util.Date;

/**
 *
 * @author i3
 */
public class LclImportInspectionExcelCreator extends BaseExcelCreator {

    public LclImportInspectionExcelCreator() {
    }

    public void writeContent(String unitSsId) throws Exception {
        String unitNo = "";
        String unitType = "";
        String sealNo = "";
        String brand = "";
        String companyName = "";
        LclUnitSs lclUnitSs = new LclUnitSsDAO().findById(Long.parseLong(unitSsId));
        sealNo = new LclUnitWhseDAO().getLCLUnitWhseSeal(lclUnitSs.getLclUnit().getId(), lclUnitSs.getLclSsHeader().getId());

        if (lclUnitSs != null && lclUnitSs.getLclUnit().getUnitNo() != null) {
            unitNo = lclUnitSs.getLclUnit().getUnitNo();
        }

        if (lclUnitSs != null && lclUnitSs.getLclUnit().getUnitType().getDescription() != null) {
            unitType = lclUnitSs.getLclUnit().getUnitType().getDescription();
        }
        if ((lclUnitSs != null && lclUnitSs.getLclSsHeader() != null && !lclUnitSs.getLclSsHeader().getLclUnitSsImportsList().isEmpty())
                && lclUnitSs.getLclSsHeader().getLclUnitSsImportsList().get(0).getOriginAcctNo().getAccountno() != null) {
            brand = new TradingPartnerDAO().getBusinessUnit(lclUnitSs.getLclSsHeader().getLclUnitSsImportsList().get(0).getOriginAcctNo().getAccountno());
        }
        if (CommonUtils.isNotEmpty(brand)) {
            companyName = LoadLogisoftProperties.getProperty("ECI".equalsIgnoreCase(brand) ? "application.Econo.companyname"
                    : "OTI".equalsIgnoreCase(brand) ? "application.OTI.companyname" : "application.ECU.companyname");
        }
        createRow();
        row.setHeightInPoints(15f);
        createHeaderCell(companyName, lclCellStyleBoldArialBottomRightLine);
        mergeCells(rowIndex, rowIndex, 0, 4);
        createHeaderCell("Date:", cellStyleRightBold);
        createHeaderCell(DateUtils.parseDateToString(new Date()), cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 6, 12);
        resetColumnIndex();

        createRow();
        row.setHeightInPoints(15f);
        resetColumnIndex();
        //1st table
        createRow();
        createHeaderCell("", blackCellBackground);
        mergeCells(rowIndex, rowIndex, 0, 12);
        row.setHeightInPoints(15f);

        createRow();
        resetColumnIndex();
        createHeaderCell("CARGO COMPANY NAME:", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 0, 4);
        createHeaderCell("", cellStyleCenterNormalForTruker);
        mergeCells(rowIndex, rowIndex, 5, 12);
        resetColumnIndex();
        createRow();
        createHeaderCell("CONTAINER TYPE:", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 0, 4);
        createHeaderCell(unitType, cellStyleCenterNormalForTruker);
        mergeCells(rowIndex, rowIndex, 5, 12);
        resetColumnIndex();
        createRow();
        createHeaderCell("CONTAINER NUMBER:", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 0, 4);
        createHeaderCell(unitNo, cellStyleCenterNormalForTruker);
        mergeCells(rowIndex, rowIndex, 5, 12);
        resetColumnIndex();
        createRow();
        createHeaderCell("ARRIVAL TIME:", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 0, 4);
        createHeaderCell("", cellStyleCenterNormalForTruker);
        mergeCells(rowIndex, rowIndex, 5, 12);
        resetColumnIndex();
        createRow();
        createHeaderCell("DEPARTURE TIME:", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 0, 4);
        createHeaderCell("", cellStyleCenterNormalForTruker);
        mergeCells(rowIndex, rowIndex, 5, 12);
        resetColumnIndex();
        createRow();
        createHeaderCell("LEVEL OF CLEANLINESS:", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 0, 4);
        createHeaderCell("", cellStyleCenterNormalForTruker);
        mergeCells(rowIndex, rowIndex, 5, 12);
        resetColumnIndex();
        createRow();
        createHeaderCell("CARGO SEAL NUMBER:", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 0, 4);
        createHeaderCell(sealNo, cellStyleCenterNormalForTruker);
        mergeCells(rowIndex, rowIndex, 5, 12);
        resetColumnIndex();
        createRow();
        createHeaderCell("CHASSIS NUMBER:", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 0, 4);
        createHeaderCell("", cellStyleCenterNormalForTruker);
        mergeCells(rowIndex, rowIndex, 5, 12);
        resetColumnIndex();
        // 2nd table
        createRow();
        resetColumnIndex();
        createRow();
        createHeaderCell("CONTAINER (INTERNAL INSPECTION)", blackCellBackground);
        mergeCells(rowIndex, rowIndex, 0, 4);
        createHeaderCell("INITIALS", blackCellBackground);
        createHeaderCell("", lclCellStyleLeftNormal);
        createHeaderCell("CONTAINER (EXTERNAL INSPECTION)", blackCellBackground);
        mergeCells(rowIndex, rowIndex, 7, 11);
        createHeaderCell("INITIALS", blackCellBackground);
        resetColumnIndex();

        createRow();
        createHeaderCell("INTERNAL CEILING:", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 0, 4);
        createHeaderCell("OK", cellStyleLeftNormalForTruker);
        createHeaderCell("", lclCellStyleLeftNormal);
        createHeaderCell("OUTSIDE DOORS", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 7, 11);
        createHeaderCell("OK", cellStyleLeftNormalForTruker);
        resetColumnIndex();

        createRow();
        createHeaderCell("FRONT WALL:", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 0, 4);
        createHeaderCell("OK", cellStyleLeftNormalForTruker);
        createHeaderCell("", lclCellStyleLeftNormal);
        createHeaderCell("RIGHT WALL", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 7, 11);
        createHeaderCell("OK", cellStyleLeftNormalForTruker);
        resetColumnIndex();

        createRow();
        createHeaderCell("FLOOR:", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 0, 4);
        createHeaderCell("OK", cellStyleLeftNormalForTruker);
        createHeaderCell("", lclCellStyleLeftNormal);
        createHeaderCell("LEFT WALL", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 7, 11);
        createHeaderCell("OK", cellStyleLeftNormalForTruker);
        resetColumnIndex();

        createRow();
        createHeaderCell("LEFT WALL:", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 0, 4);
        createHeaderCell("OK", cellStyleLeftNormalForTruker);
        createHeaderCell("", lclCellStyleLeftNormal);
        createHeaderCell("FRONT WALL", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 7, 11);
        createHeaderCell("OK", cellStyleLeftNormalForTruker);
        resetColumnIndex();

        createRow();
        createHeaderCell("RIGHT WALL:", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 0, 4);
        createHeaderCell("OK", cellStyleLeftNormalForTruker);
        resetColumnIndex();

        createRow();
        createHeaderCell("INSIDE DOORS:", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 0, 4);
        createHeaderCell("OK", cellStyleLeftNormalForTruker);
        createHeaderCell("", lclCellStyleLeftNormal);
        createHeaderCell("OBSERVATION OF CONTAINER (IF APPLICABLE)", blackCellBackground);
        mergeCells(rowIndex, rowIndex, 7, 12);
        resetColumnIndex();

        createRow();
        createHeaderCell("", lclCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 0, 6);
        createHeaderCell("", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 7, 12);
        resetColumnIndex();

        createRow();
        createHeaderCell("TRUCKING COMPANY", blackCellBackground);
        mergeCells(rowIndex, rowIndex, 0, 4);
        createHeaderCell("INITIALS", blackCellBackground);
        createHeaderCell("", lclCellStyleLeftNormal);
        createHeaderCell("CONTAINER WITH OLD DENTS AND SCRATCHES", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 7, 12);
        resetColumnIndex();

        createRow();
        createHeaderCell("TIRES:", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 0, 4);
        createHeaderCell("OK", cellStyleLeftNormalForTruker);
        createHeaderCell("", lclCellStyleLeftNormal);
        createHeaderCell("", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 7, 12);
        resetColumnIndex();

        createRow();
        createHeaderCell("CAB APPEARANCE:", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 0, 4);
        createHeaderCell("OK", cellStyleLeftNormalForTruker);
        createHeaderCell("", lclCellStyleLeftNormal);
        createHeaderCell("", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 7, 12);
        resetColumnIndex();

        createRow();
        createHeaderCell("CHASSIS APPEARANCE:", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 0, 4);
        createHeaderCell("OK", cellStyleLeftNormalForTruker);
        createHeaderCell("", lclCellStyleLeftNormal);
        createHeaderCell("", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 7, 12);
        resetColumnIndex();

        createRow();
        resetColumnIndex();
        createRow();
        createHeaderCell("TRUCKING COMPANY:", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 0, 4);
        createHeaderCell("", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 5, 12);
        resetColumnIndex();

        createRow();
        createHeaderCell("TRUCKER'S FULL NAME:", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 0, 4);
        createHeaderCell("", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 5, 12);
        resetColumnIndex();

        createRow();
        createHeaderCell("PLATE NUMBERS:", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 0, 4);
        createHeaderCell("", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 5, 12);
        resetColumnIndex();

        createRow();
        row.setHeightInPoints(30f);
        createHeaderCell("WAS THE CONTAINER PRESENT DURING THE \nLOADING OF THE CONTAINER?", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 0, 4);
        createHeaderCell("Yes______________ No _____________X__________", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 5, 9);
        createHeaderCell("MARK WITH AN X", blackCellBackground);
        mergeCells(rowIndex, rowIndex, 10, 12);
        resetColumnIndex();

        createRow();
        createRow();
        createHeaderCell("INSPECTED AND SUPERVISED BY (NAME AND SIGNATURE)", blackCellBackground);
        mergeCells(rowIndex, rowIndex, 0, 5);
        createHeaderCell("", lclCellStyleLeftNormal);
        createHeaderCell("SHIPMENT AUTHERIZED BY (NAME AND SIGNATURE)", blackCellBackground);
        mergeCells(rowIndex, rowIndex, 7, 12);
        resetColumnIndex();

        createRow();
        row.setHeightInPoints(50f);
        createHeaderCell("", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 0, 5);
        createHeaderCell("", lclCellStyleLeftNormal);
        createHeaderCell("", cellStyleLeftNormalForTruker);
        mergeCells(rowIndex, rowIndex, 7, 12);
        resetColumnIndex();
    }

    public void create(String reportLocation, String unitSsId) throws Exception {
        try {
            init(reportLocation, "InspectionControlForm");
            writeContent(unitSsId);
            writeIntoFile();
        } catch (Exception e) {
            throw e;
        } finally {
            exit();
        }
    }
}

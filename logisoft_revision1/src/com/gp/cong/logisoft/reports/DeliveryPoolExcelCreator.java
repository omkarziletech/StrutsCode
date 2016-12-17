/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.reports;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.model.LclDoorDeliverySearchBean;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclDoorDeliverySearchDAO;
import com.gp.cvst.logisoft.struts.form.lcl.lclDoorDeliverySearchForm;
import com.logiware.common.dao.PropertyDAO;
import com.logiware.excel.BaseExcelCreator;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Logiware
 */
public class DeliveryPoolExcelCreator extends BaseExcelCreator {

  
    private void writeHeader() throws Exception {
        createRow();
        resetColumnIndex();
        createHeaderCell("Voyage", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);
        createHeaderCell("POL", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 25);
        createHeaderCell("POD", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 25);
        createHeaderCell("ETA", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);
        createHeaderCell("DISP", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);
        createHeaderCell("LFD", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);
        createHeaderCell("DR#", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);
        createHeaderCell("HBL#", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 25);
        createHeaderCell("CCR", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);
        createHeaderCell("DO", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);
        createHeaderCell("COM", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);
        createHeaderCell("Lift Gate", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);
        createHeaderCell("HAZ", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);
        createHeaderCell("EPD", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);
        createHeaderCell("APD", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);
        createHeaderCell("EDD", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);
        createHeaderCell("ADD", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);
        createHeaderCell("Buy", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);
        createHeaderCell("Sell", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);
        createHeaderCell("Profit", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);
        createHeaderCell("Status", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 25);
        createHeaderCell("Carrier", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("PRO#", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 25);
        createHeaderCell("Need POD", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);
        createHeaderCell("ZipCode", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);
        createHeaderCell("City", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);

    }

    private void writeContent(List<LclDoorDeliverySearchBean> lclDoorDeliverySearchResult) throws Exception {
        int count = 0;
        for (LclDoorDeliverySearchBean bean : lclDoorDeliverySearchResult) {
            createRow();
            resetColumnIndex();
            if (count % 2 == 0) {
                createTextCell(CommonUtils.isNotEmpty(bean.getVoyageNo()) ? bean.getVoyageNo() : "", tableEvenRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getPolUnCode()) ? bean.getPolUnCode() : "", tableEvenRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getPodUnCode()) ? bean.getPodUnCode() : "", tableEvenRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getEtaDate()) ? bean.getEtaDate() : "", tableEvenRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getDispoCode()) ? bean.getDispoCode() : "", tableEvenRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getLastFreeDate()) ? bean.getLastFreeDate() : "", tableEvenRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getFileNumber()) ? bean.getFileNumber() : "", tableEvenRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getHouseBl()) ? bean.getHouseBl() : "", tableEvenRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getCustomsClearanceReceived()) ? bean.getCustomsClearanceReceived() : "", tableEvenRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getDeliveryOrderReceived()) ? bean.getDeliveryOrderReceived() : "", tableEvenRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getDeliveryCommercial()) ? bean.getDeliveryCommercial() : "", tableEvenRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getLiftGate()) ? bean.getLiftGate() : "", tableEvenRowCellStyleLeftNormal);
                createTextCell(true == bean.getHazmat() ? "Y" : "N", tableEvenRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getPickupEstDate()) ? bean.getPickupEstDate() : "", tableEvenRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getDeliveryEstDate()) ? bean.getDeliveryEstDate() : "", tableEvenRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getDeliveredDateTime()) ? bean.getDeliveredDateTime() : "", tableEvenRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getPickedUpDateTime()) ? bean.getPickedUpDateTime() : "", tableEvenRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getBuy()) ? bean.getBuy() : 0.00, tableEvenRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getSell()) ? bean.getSell() : 0.00, tableEvenRowCellStyleLeftNormal);
                createTextCell(bean.getSell() - bean.getBuy(), tableEvenRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getDoorDeliveryDesc()) ? bean.getDoorDeliveryDesc() : "", tableEvenRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getScacCode()) ? bean.getScacCode() : "", tableEvenRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getPickupReferenceNo()) ? bean.getPickupReferenceNo() : "", tableEvenRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getNeedPod()) ? bean.getNeedPod() : "", tableEvenRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getZipCode()) ? bean.getZipCode() : "", tableEvenRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getCity()) ? bean.getCity() : "", tableEvenRowCellStyleLeftNormal); 
            } else {
                createTextCell(CommonUtils.isNotEmpty(bean.getVoyageNo()) ? bean.getVoyageNo() : "", tableOddRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getPolUnCode()) ? bean.getPolUnCode() : "", tableOddRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getPodUnCode()) ? bean.getPodUnCode() : "", tableOddRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getEtaDate()) ? bean.getEtaDate() : "", tableOddRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getDispoCode()) ? bean.getDispoCode() : "", tableOddRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getLastFreeDate()) ? bean.getLastFreeDate() : "", tableOddRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getFileNumber()) ? bean.getFileNumber() : "", tableOddRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getHouseBl()) ? bean.getHouseBl() : "", tableOddRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getCustomsClearanceReceived()) ? bean.getCustomsClearanceReceived() : "", tableOddRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getDeliveryOrderReceived()) ? bean.getDeliveryOrderReceived() : "", tableOddRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getDeliveryCommercial()) ? bean.getDeliveryCommercial() : "", tableOddRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getLiftGate()) ? bean.getLiftGate() : "", tableOddRowCellStyleLeftNormal);
                createTextCell(true == bean.getHazmat() ? "Y" : "N", tableOddRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getPickupEstDate()) ? bean.getPickupEstDate() : "", tableOddRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getDeliveryEstDate()) ? bean.getDeliveryEstDate() : "", tableOddRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getDeliveredDateTime()) ? bean.getDeliveredDateTime() : "", tableOddRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getPickedUpDateTime()) ? bean.getPickedUpDateTime() : "", tableOddRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getBuy()) ? bean.getBuy() : 0.00, tableOddRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getSell()) ? bean.getSell() : 0.00, tableOddRowCellStyleLeftNormal);
                createTextCell(bean.getSell() - bean.getBuy(), tableOddRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getDoorDeliveryDesc()) ? bean.getDoorDeliveryDesc() : "", tableOddRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getScacCode()) ? bean.getScacCode() : "", tableOddRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getPickupReferenceNo()) ? bean.getPickupReferenceNo() : "", tableOddRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getNeedPod()) ? bean.getNeedPod() : "", tableOddRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getZipCode()) ? bean.getZipCode() : "", tableOddRowCellStyleLeftNormal);
                createTextCell(CommonUtils.isNotEmpty(bean.getCity()) ? bean.getCity() : "", tableOddRowCellStyleLeftNormal); 
         
            }
            count++;
        }

    }

    public String create(List<LclDoorDeliverySearchBean> lclDoorDeliverySearchResult) throws Exception {
        StringBuilder fileBuilder = new StringBuilder();
        try {
            fileBuilder.append(new PropertyDAO().getProperty("reportLocation")).append("/Reports/");
            fileBuilder.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
            File file = new File(fileBuilder.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            fileBuilder.append("Delivery_Pool_Report").append("_").append(DateUtils.formatDate(new Date(), "yyyyMMdd_kkmmmssss")).append(".xlsx");
            init(fileBuilder.toString(), "DeliveryPool");
            writeHeader();
            if (null != lclDoorDeliverySearchResult) {
                writeContent(lclDoorDeliverySearchResult);
            }
            writeIntoFile();
            return fileBuilder.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            exit();
        }

    }
}

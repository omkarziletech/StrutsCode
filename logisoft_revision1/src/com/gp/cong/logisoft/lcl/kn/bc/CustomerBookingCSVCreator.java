/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.kn.bc;

import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.kn.beans.BookingBean;
import com.logiware.excel.BaseExcelCreator;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author palraj
 */
public class CustomerBookingCSVCreator extends BaseExcelCreator {

    private static Logger log = Logger.getLogger(CustomerBookingCSVCreator.class);

    public String createCSV(User user, List<BookingBean> bkg) throws Exception {
        try {
            String property = "java.io.tmpdir";
            String temp = System.getProperty(property);
            StringBuilder filePath = new StringBuilder();
            String todayDate = DateUtils.formatDate(new Date(), "MMddyyyyHHmmss");
            filePath.append(temp).append("/");
            File file = new File(filePath.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            filePath.append(user.getLoginName().toUpperCase()).append("_").append(todayDate).append(".xlsx");
            init(filePath.toString(), "Customer Booking Result");
            writeHeader();
            if (bkg != null) {
                writeContent(bkg);
            }
            writeIntoFile();
            return filePath.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            exit();
        }
    }

    private void writeHeader() throws Exception {
        createRow();
        resetColumnIndex();
        createHeaderCell("Booking#", tableHeaderCellStyleLeftBold);
        sheet.setColumnWidth(columnIndex, 256 * 11);
        createHeaderCell("Sender ID", tableHeaderCellStyleLeftBold);
        sheet.setColumnWidth(columnIndex, 256 * 10);
        createHeaderCell("Sender Mapping ID", tableHeaderCellStyleLeftBold);
        sheet.setColumnWidth(columnIndex, 256 * 12);
        createHeaderCell("Customer Control Code", tableHeaderCellStyleLeftBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);
        createHeaderCell("Orgin", tableHeaderCellStyleLeftBold);
        sheet.setColumnWidth(columnIndex, 256 * 8);
        createHeaderCell("Destination", tableHeaderCellStyleLeftBold);
        sheet.setColumnWidth(columnIndex, 256 * 11);
        createHeaderCell("AMS", tableHeaderCellStyleLeftBold);
        sheet.setColumnWidth(columnIndex, 256 * 8);
        createHeaderCell("AES", tableHeaderCellStyleLeftBold);
        sheet.setColumnWidth(columnIndex, 256 * 8);
        createHeaderCell("Booking Date", tableHeaderCellStyleLeftBold);
        sheet.setColumnWidth(columnIndex, 256 * 12);
        createHeaderCell("PCs", tableHeaderCellStyleLeftBold);
        sheet.setColumnWidth(columnIndex, 256 * 8);
        createHeaderCell("Wgt", tableHeaderCellStyleLeftBold);
        sheet.setColumnWidth(columnIndex, 256 * 10);
        createHeaderCell("Vol", tableHeaderCellStyleLeftBold);
        sheet.setColumnWidth(columnIndex, 256 * 8);
        createHeaderCell("Vessel Voyage ID", tableHeaderCellStyleLeftBold);
        sheet.setColumnWidth(columnIndex, 256 * 16);
        createHeaderCell("Vessel Name", tableHeaderCellStyleLeftBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);
        createHeaderCell("IMO#", tableHeaderCellStyleLeftBold);
        sheet.setColumnWidth(columnIndex, 256 * 10);
        createHeaderCell("Voyage", tableHeaderCellStyleLeftBold);
        sheet.setColumnWidth(columnIndex, 256 * 8);
        createHeaderCell("ETD", tableHeaderCellStyleLeftBold);
        sheet.setColumnWidth(columnIndex, 256 * 12);
        createHeaderCell("ETA", tableHeaderCellStyleLeftBold);
        sheet.setColumnWidth(columnIndex, 256 * 12);
        createHeaderCell("Created On", tableHeaderCellStyleLeftBold);
        sheet.setColumnWidth(columnIndex, 256 * 12);
    }

    private void writeContent(List<BookingBean> bkg) throws Exception {
        SimpleDateFormat sdf =  new SimpleDateFormat("dd-MMM-yyyy");
        for (BookingBean bookingBean : bkg) {
            createRow();
            resetColumnIndex();
            createTextCell(bookingBean.getBookingNumber(), cellStyleLeftNormal);
            createTextCell(bookingBean.getSenderId(), cellStyleLeftNormal);
            createTextCell(bookingBean.getSenderMappingId(), cellStyleLeftNormal);
            createTextCell(bookingBean.getCustomerControlCode(), cellStyleLeftNormal);
            createTextCell(bookingBean.getCfsOrigin(), cellStyleLeftNormal);
            createTextCell(bookingBean.getCfsDestination(), cellStyleLeftNormal);
            createTextCell(bookingBean.getAmsFlag(), cellStyleLeftNormal);
            createTextCell(bookingBean.getAesFlag(), cellStyleLeftNormal);
            createTextCell(DateUtils.formatStringDateToAppFormatMMM(sdf.parse(bookingBean.getBookingDate())), cellStyleLeftNormal);
            createTextCell(bookingBean.getPieces(), cellStyleLeftNormal);
            createTextCell(bookingBean.getWeight(), cellStyleLeftNormal);
            createTextCell(bookingBean.getVolume(), cellStyleLeftNormal);
            createTextCell(bookingBean.getVesselVoyageId(), cellStyleLeftNormal);
            createTextCell(bookingBean.getVesselName(), cellStyleLeftNormal);
            createTextCell(bookingBean.getImoNumber(), cellStyleLeftNormal);
            createTextCell(bookingBean.getVoyage(), cellStyleLeftNormal);
            createTextCell(DateUtils.formatStringDateToAppFormatMMM(sdf.parse(bookingBean.getEtd())), cellStyleLeftNormal);
            createTextCell(DateUtils.formatStringDateToAppFormatMMM(sdf.parse(bookingBean.getEta())), cellStyleLeftNormal);
            createTextCell(DateUtils.formatStringDateToAppFormatMMM(sdf.parse(bookingBean.getCreatedOn())), cellStyleLeftNormal);
        }
    }
}

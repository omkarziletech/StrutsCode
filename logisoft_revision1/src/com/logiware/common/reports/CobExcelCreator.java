package com.logiware.common.reports;

import com.gp.cong.common.DateUtils;
import com.logiware.common.dao.PropertyDAO;
import com.logiware.common.dao.ReportDAO;
import com.logiware.common.form.ReportForm;
import com.logiware.common.model.ReportModel;
import com.logiware.excel.BaseExcelCreator;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Lakshmi Narayanan
 */
public class CobExcelCreator extends BaseExcelCreator {

    private ReportForm reportForm;

    public CobExcelCreator() {
    }

    public CobExcelCreator(ReportForm reportForm) {
        this.reportForm = reportForm;
    }

    private void writeContent() throws Exception {
        List<ReportModel> cobList = new ReportDAO().getCobReportList(reportForm);
        createRow();
        resetColumnIndex();
        createHeaderCell("File No", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);
        createHeaderCell("Booking No", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("POD", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 45);
        createHeaderCell("Carrier", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 50);
        createHeaderCell("Client", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 60);
        createHeaderCell("Doc Cut-Off", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);
        createHeaderCell("Carrier Doc Cut-Off", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("ETD", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);
        createHeaderCell("ETA", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);
        createHeaderCell("Confirmed On Board", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Received SSL Master", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Booked By", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);

//        createHeaderCell("POL", tableHeaderCellStyleCenterBold);
//        sheet.setColumnWidth(columnIndex, 256 * 45);
//        createHeaderCell("Consignee", tableHeaderCellStyleCenterBold);
//        sheet.setColumnWidth(columnIndex, 256 * 60);      
//        createHeaderCell("Consignee On MBL", tableHeaderCellStyleCenterBold);
//        sheet.setColumnWidth(columnIndex, 256 * 60);
//        createHeaderCell("Delivery Agent On HBL", tableHeaderCellStyleCenterBold);
//        sheet.setColumnWidth(columnIndex, 256 * 60);
//        createHeaderCell("House No. Originals", tableHeaderCellStyleCenterBold);
//        sheet.setColumnWidth(columnIndex, 256 * 90);
//        createHeaderCell("Master No. Originals", tableHeaderCellStyleCenterBold);
//        sheet.setColumnWidth(columnIndex, 256 * 90);
        int rowCount = 0;
        for (ReportModel model : cobList) {
            createRow();
            resetColumnIndex();
            if (rowCount % 2 == 0) {
                createTextCell(model.getFileNo(), tableEvenRowCellStyleLeftNormal);
                createTextCell(model.getBookingNo(), tableEvenRowCellStyleLeftNormal);
                createTextCell(model.getPod(), tableEvenRowCellStyleLeftNormal);
                createTextCell(model.getCarrier(), tableEvenRowCellStyleLeftNormal);
                createTextCell(model.getClient(), tableEvenRowCellStyleLeftNormal);
                createTextCell(model.getDocCutOff(), tableEvenRowCellStyleLeftNormal);
                createTextCell(model.getCarrierDocCutOff(), tableEvenRowCellStyleLeftNormal);
                createTextCell(model.getEtd(), tableEvenRowCellStyleLeftNormal);
                createTextCell(model.getEta(), tableEvenRowCellStyleLeftNormal);
                createTextCell(model.getConfirmedOnBoard(), tableEvenRowCellStyleLeftNormal);
                createTextCell(model.getReceivedSslMaster(), tableEvenRowCellStyleLeftNormal);
                createTextCell(model.getBookedBy(), tableEvenRowCellStyleLeftNormal);
//                createTextCell(model.getPol(), tableEvenRowCellStyleLeftNormal);
//                createTextCell(model.getConsignee(), tableEvenRowCellStyleLeftNormal);              
//                createTextCell(model.getConsigneeOnMBL(), tableEvenRowCellStyleLeftNormal);
//                createTextCell(model.getDeliveryAgentOnHBL(), tableEvenRowCellStyleLeftNormal);
//                createTextCell(model.getHouseNoOriginals(), tableEvenRowCellStyleLeftNormal);
//                createTextCell(model.getMasterNoOriginals(), tableEvenRowCellStyleLeftNormal);
            } else {
                createTextCell(model.getFileNo(), tableOddRowCellStyleLeftNormal);
                createTextCell(model.getBookingNo(), tableOddRowCellStyleLeftNormal);
                createTextCell(model.getPod(), tableOddRowCellStyleLeftNormal);
                createTextCell(model.getCarrier(), tableOddRowCellStyleLeftNormal);
                createTextCell(model.getClient(), tableOddRowCellStyleLeftNormal);
                createTextCell(model.getDocCutOff(), tableOddRowCellStyleLeftNormal);
                createTextCell(model.getCarrierDocCutOff(), tableOddRowCellStyleLeftNormal);
                createTextCell(model.getEtd(), tableOddRowCellStyleLeftNormal);
                createTextCell(model.getEta(), tableOddRowCellStyleLeftNormal);
                createTextCell(model.getConfirmedOnBoard(), tableOddRowCellStyleLeftNormal);
                createTextCell(model.getReceivedSslMaster(), tableOddRowCellStyleLeftNormal);
                createTextCell(model.getBookedBy(), tableOddRowCellStyleLeftNormal);
//                createTextCell(model.getPol(), tableOddRowCellStyleLeftNormal);
//                createTextCell(model.getConsignee(), tableOddRowCellStyleLeftNormal);          
//                createTextCell(model.getConsigneeOnMBL(), tableOddRowCellStyleLeftNormal);
//                createTextCell(model.getDeliveryAgentOnHBL(), tableOddRowCellStyleLeftNormal);
//                createTextCell(model.getHouseNoOriginals(), tableOddRowCellStyleLeftNormal);
//                createTextCell(model.getMasterNoOriginals(), tableOddRowCellStyleLeftNormal);
            }
            rowCount++;
        }
    }

    public String create() throws Exception {
        try {
            StringBuilder fileNameBuilder = new StringBuilder();
            fileNameBuilder.append(new PropertyDAO().getProperty("reportLocation")).append("/Reports/");
            fileNameBuilder.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
            File file = new File(fileNameBuilder.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            fileNameBuilder.append("FCL_BL_COB_REPORT").append("_").append(DateUtils.formatDate(new Date(), "yyyyMMdd_kkmmssSSS")).append(".xlsx");
            init(fileNameBuilder.toString(), "COB Report");
            writeContent();
            writeIntoFile();
            return fileNameBuilder.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            exit();
        }
    }
}

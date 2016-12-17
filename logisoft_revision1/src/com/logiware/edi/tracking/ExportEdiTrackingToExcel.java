/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.edi.tracking;

import com.gp.cong.logisoft.ExcelGenerator.BaseExcelGenerator;
import com.gp.cong.logisoft.ExcelGenerator.ExcelSheetConstants;
import java.util.Date;
import java.util.List;
import jxl.write.Label;

/**
 *
 * @author vellaisamy
 */
public class ExportEdiTrackingToExcel extends BaseExcelGenerator {

    private void generateExcelSheet(List<EdiSystemBean> ediList) throws Exception {
        if (null != ediList && !ediList.isEmpty()) {
            String sheetName = "EdiTracking";
            writableSheet = writableWorkbook.createSheet(sheetName, 0);
            int row = 0;
            writableSheet.mergeCells(0, row, 1, row);
            writableSheet.mergeCells(2, row, 4, row);
            writableSheet.addCell(new Label(0, row, EdiTrackingConstant.EDITRACKING_HEADER, headerCell));
            writableSheet.addCell(new Label(2, row, ExcelSheetConstants.DATE + ":" + sdf.format(new Date()), headerCell));

            /*Table Column Header*/

            row++;
            writableSheet.setColumnView(0, 15);
            writableSheet.setColumnView(1, 25);
            writableSheet.setColumnView(2, 20);
            writableSheet.setColumnView(3, 12);
            writableSheet.setColumnView(4, 15);
            writableSheet.setColumnView(5, 10);
            writableSheet.setColumnView(6, 10);
            writableSheet.setColumnView(7, 10);
            writableSheet.setColumnView(8, 15);
            writableSheet.setColumnView(9, 20);
            writableSheet.setColumnView(10, 21);
            writableSheet.setColumnView(11, 14);
            writableSheet.setColumnView(12, 13);
            writableSheet.setColumnView(13, 14);
            writableSheet.setColumnView(14, 15);
            writableSheet.setColumnView(15, 60);

            writableSheet.addCell(new Label(0, row, EdiTrackingConstant.EDITRACKING_FILENO, headerCell));
            writableSheet.addCell(new Label(1, row, EdiTrackingConstant.EDITRACKING_BOOKINGNO, headerCell));
            writableSheet.addCell(new Label(2, row, EdiTrackingConstant.EDITRACKING_REQUESTOR, headerCell));
            writableSheet.addCell(new Label(3, row, EdiTrackingConstant.EDITRACKING_MSGTYPE, headerCell));
            writableSheet.addCell(new Label(4, row, EdiTrackingConstant.EDITRACKING_COMPANY, headerCell));
            writableSheet.addCell(new Label(5, row, EdiTrackingConstant.EDITRACKING_ORIGIN, headerCell));
            writableSheet.addCell(new Label(6, row, EdiTrackingConstant.EDITRACKING_POL, headerCell));
            writableSheet.addCell(new Label(7, row, EdiTrackingConstant.EDITRACKING_POD, headerCell));
            writableSheet.addCell(new Label(8, row, EdiTrackingConstant.EDITRACKING_DESTINATION, headerCell));
            writableSheet.addCell(new Label(9, row, EdiTrackingConstant.EDITRACKING_PROCESSEDDATE, headerCell));
            writableSheet.addCell(new Label(10, row, EdiTrackingConstant.EDITRACKING_ACKCREATEDDATE, headerCell));
            writableSheet.addCell(new Label(11, row, EdiTrackingConstant.EDITRACKING_ACKSTATUS, headerCell));
            writableSheet.addCell(new Label(12, row, EdiTrackingConstant.EDITRACKING_STATUS, headerCell));
            writableSheet.addCell(new Label(13, row, EdiTrackingConstant.EDITRACKING_TRANSPORTSERVICE, headerCell));
            writableSheet.addCell(new Label(14, row, EdiTrackingConstant.EDITRACKING_TRANSACTIONSTATUS, headerCell));
            writableSheet.addCell(new Label(15, row, EdiTrackingConstant.EDITRACKING_FILENAME, headerCell));

            /*Table Data*/
            row++;

            for (EdiSystemBean ediSystemBean : ediList) {
                writableSheet.addCell(new Label(0, row, ediSystemBean.getDrNumber(), thinBorderCell));
                writableSheet.addCell(new Label(1, row, ediSystemBean.getBookingNumber(), thinBorderCell));
                writableSheet.addCell(new Label(2, row, ediSystemBean.getRequestor(), thinBorderCell));
                writableSheet.addCell(new Label(3, row, ediSystemBean.getMessageType(), thinBorderCell));
                writableSheet.addCell(new Label(4, row, ediSystemBean.getEdiCompany(), thinBorderCell));
                writableSheet.addCell(new Label(5, row, ediSystemBean.getPlaceOfReceipt(), thinBorderCell));
                writableSheet.addCell(new Label(6, row, ediSystemBean.getPortOfLoad(), thinBorderCell));
                writableSheet.addCell(new Label(7, row, ediSystemBean.getPortOfDischarge(), thinBorderCell));
                writableSheet.addCell(new Label(8, row, ediSystemBean.getPlaceOfDelivery(), thinBorderCell));
                writableSheet.addCell(new Label(9, row, ediSystemBean.getProcessedDate(), thinBorderCell));
                writableSheet.addCell(new Label(10, row, ediSystemBean.getAckRecievedDate(), thinBorderCell));
                writableSheet.addCell(new Label(11, row, ediSystemBean.getAckStatus(), thinBorderCell));
                writableSheet.addCell(new Label(12, row, ediSystemBean.getAckStatus(), thinBorderCell));
                writableSheet.addCell(new Label(13, row, ediSystemBean.getTransportService(), thinBorderCell));
                writableSheet.addCell(new Label(14, row, ediSystemBean.getTransactionStatus(), thinBorderCell));
                writableSheet.addCell(new Label(15, row, ediSystemBean.getFileName(), thinBorderCell));
                row++;
            }
        }
    }

    public void exportToExcel(String excelFilePath, List<EdiSystemBean> ediList) throws Exception {
        super.init(excelFilePath);
        this.generateExcelSheet(ediList);
        super.write();
        super.close();
    }
}

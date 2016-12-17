/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.excel;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.ExcelGenerator.BaseExcelGenerator;
import com.gp.cong.logisoft.ExcelGenerator.ExcelSheetConstants;
import com.gp.cong.logisoft.beans.ManifestBean;
import java.util.Date;
import java.util.List;
import jxl.write.Label;
import jxl.write.Number;

/**
 *
 * @author Shanmugam
 */
public class ExportLclUnitsViewDRToExcel extends BaseExcelGenerator {

    private void generateExcelSheet(List<ManifestBean> drList) throws Exception {
        if (null != drList && !drList.isEmpty()) {
            String sheetName = "ViewDR";
            writableSheet = writableWorkbook.createSheet(sheetName, 0);
            int row = 0;
            writableSheet.mergeCells(0, row, 1, row);
            writableSheet.mergeCells(2, row, 4, row);
            writableSheet.addCell(new Label(0, row, "List of View DR", headerCell));
            writableSheet.addCell(new Label(2, row, ExcelSheetConstants.DATE + ":" + sdf.format(new Date()), headerCell));

            /*Table Column Header*/

            row++;
            writableSheet.setColumnView(0, 15);
            writableSheet.setColumnView(1, 25);
            writableSheet.setColumnView(2, 10);
            writableSheet.setColumnView(3, 20);
            writableSheet.setColumnView(4, 20);
            writableSheet.setColumnView(5, 10);
            writableSheet.setColumnView(6, 11);
            writableSheet.setColumnView(7, 10);
            writableSheet.setColumnView(8, 10);
            writableSheet.setColumnView(9, 10);
            writableSheet.setColumnView(10, 10);
            writableSheet.setColumnView(11, 10);
            writableSheet.setColumnView(12, 10);
            writableSheet.setColumnView(13, 10);
            writableSheet.setColumnView(14, 50);
            writableSheet.setColumnView(15, 50);
            writableSheet.setColumnView(16, 50);
            writableSheet.setColumnView(17, 50);
            writableSheet.setColumnView(18, 10);
            writableSheet.setColumnView(19, 10);
            writableSheet.setColumnView(20, 10);
            writableSheet.setColumnView(21, 10);
            writableSheet.setColumnView(22, 10);
            writableSheet.setColumnView(23, 10);
            writableSheet.setColumnView(24, 10);
            writableSheet.setColumnView(25, 10);
            writableSheet.setColumnView(26, 10);
            writableSheet.setColumnView(27, 10);
            writableSheet.setColumnView(28, 10);
            writableSheet.setColumnView(29, 10);
            writableSheet.setColumnView(30, 10);

            writableSheet.addCell(new Label(0, row, "File#", headerCell));
            writableSheet.addCell(new Label(1, row, "BL#", headerCell));
            writableSheet.addCell(new Label(2, row, "Type", headerCell));
            writableSheet.addCell(new Label(3, row, "BL Status", headerCell));
            writableSheet.addCell(new Label(4, row, "BL Invoice#", headerCell));
            writableSheet.addCell(new Label(5, row, "Disposition", headerCell));
            writableSheet.addCell(new Label(6, row, "CurrentLocation", headerCell));
            writableSheet.addCell(new Label(7, row, "Doc", headerCell));
            writableSheet.addCell(new Label(8, row, "Pieces", headerCell));
            writableSheet.addCell(new Label(9, row, "Cuft", headerCell));
            writableSheet.addCell(new Label(10, row, "Pounds", headerCell));
            writableSheet.addCell(new Label(11, row, "Origin", headerCell));
            writableSheet.addCell(new Label(12, row, "Pol", headerCell));
            writableSheet.addCell(new Label(13, row, "Pod", headerCell));
            writableSheet.addCell(new Label(14, row, "Destn", headerCell));
            writableSheet.addCell(new Label(15, row, "BookedVoy", headerCell));
            writableSheet.addCell(new Label(16, row, "Shipper", headerCell));
            writableSheet.addCell(new Label(17, row, "Forwarder", headerCell));
            writableSheet.addCell(new Label(18, row, "Consignee", headerCell));
            writableSheet.addCell(new Label(19, row, "Bill TM", headerCell));
            writableSheet.addCell(new Label(20, row, "Hot Codes", headerCell));
            writableSheet.addCell(new Label(21, row, "FF Comm", headerCell));
            writableSheet.addCell(new Label(22, row, "FTF Fee", headerCell));
            writableSheet.addCell(new Label(23, row, "Incentive", headerCell));
            writableSheet.addCell(new Label(24, row, "PCB", headerCell));
            writableSheet.addCell(new Label(25, row, "BL CFT", headerCell));
            writableSheet.addCell(new Label(26, row, "BL LBS", headerCell));
            writableSheet.addCell(new Label(27, row, "BL CBM", headerCell));
            writableSheet.addCell(new Label(28, row, "BL KGS", headerCell));
            writableSheet.addCell(new Label(29, row, "COL", headerCell));
            writableSheet.addCell(new Label(30, row, "PPD", headerCell));

            /*Table Data*/
            row++;

            for (ManifestBean bean : drList) {
                writableSheet.addCell(new Label(0, row, bean.getFileNo(), thinBorderCell));
                writableSheet.addCell(new Label(1, row, bean.getBlNo(), thinBorderCell));
                writableSheet.addCell(new Label(2, row, bean.getRateType(), thinBorderCell));
                writableSheet.addCell(new Label(3, row, bean.getStatusLabel(), thinBorderCell));
                writableSheet.addCell(new Label(4, row, bean.getBlInvoiceNo(), thinBorderCell));
                writableSheet.addCell(new Label(5, row, CommonUtils.isNotEmpty(bean.getDisposition()) ? 
                        bean.getDisposition().split("~~~")[0]: "", thinBorderCell));
                writableSheet.addCell(new Label(6, row, CommonUtils.isNotEmpty(bean.getDisposition()) ? 
                        bean.getDisposition().split("~~~")[2]: "", thinBorderCell));
                writableSheet.addCell(new Label(7, row, bean.getDoc(), thinBorderCell));
                writableSheet.addCell(new Number(8, row, bean.getTotalPieceCount(), thinBorderCell));
                writableSheet.addCell(new Label(9, row, ""+bean.getTotalVolumeImperial(), thinBorderCell));
                writableSheet.addCell(new Label(10, row, ""+bean.getTotalWeightImperial(), thinBorderCell));
                writableSheet.addCell(new Label(11, row, bean.getOrigin(), thinBorderCell));
                writableSheet.addCell(new Label(12, row, bean.getPol(), thinBorderCell));
                writableSheet.addCell(new Label(13, row, bean.getPod(), thinBorderCell));
                writableSheet.addCell(new Label(14, row, bean.getDestination(), thinBorderCell));
                writableSheet.addCell(new Label(15, row, bean.getBookedVoyageNo(), thinBorderCell));
                writableSheet.addCell(new Label(16, row, bean.getShipperName(), thinBorderCell));
                writableSheet.addCell(new Label(17, row, bean.getForwarderName(), thinBorderCell));
                writableSheet.addCell(new Label(18, row, bean.getConsigneeName(), thinBorderCell));
                writableSheet.addCell(new Label(19, row, bean.getTerminalLocation(), thinBorderCell));
                writableSheet.addCell(new Label(20, row, CommonUtils.isNotEmpty(bean.getHotCodes()) && !bean.getHotCodes().equals("0")
                        ? bean.getHotCodes().split("~~~")[2] : "", thinBorderCell));
                writableSheet.addCell(new Label(21, row, appendString(bean.getFfComm()), thinBorderCell));
                writableSheet.addCell(new Label(22, row,  appendString(bean.getFtfFee()), thinBorderCell));
                writableSheet.addCell(new Label(23, row, "", thinBorderCell));
                writableSheet.addCell(new Label(24, row, bean.getBillingType(), thinBorderCell));
                writableSheet.addCell(new Label(25, row, appendString(bean.getBlCft()), thinBorderCell));
                writableSheet.addCell(new Label(26, row, appendString(bean.getBlLbs()), thinBorderCell));
                writableSheet.addCell(new Label(27, row, appendString(bean.getBlCbm()), thinBorderCell));
                writableSheet.addCell(new Label(28, row, appendString(bean.getBlKgs()), thinBorderCell));
                writableSheet.addCell(new Label(29, row, appendString(bean.getColCharge()), thinBorderCell));
                writableSheet.addCell(new Label(30, row, appendPpdParties(bean.getPpdCharge(),bean.getPpdParties()), thinBorderCell));
                row++;
            }
        }
    }

    public void exportToExcel(String excelFilePath, List<ManifestBean> drList) throws Exception {
        super.init(excelFilePath);
        this.generateExcelSheet(drList);
        super.write();
        super.close();
    }
    private String appendString(Object value){
        if(null != value){
            return ""+value;
        }
        return "";
    }
    private String appendPpdParties(Object value,String parties){
        if(null != value){
            return "("+parties+") "+value;
        }
        return "";
    }
}

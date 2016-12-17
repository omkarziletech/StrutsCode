package com.gp.cong.logisoft.ExcelGenerator;

import java.util.Date;
import java.util.List;

import jxl.write.Label;

import com.gp.cong.logisoft.bc.accounting.GLMappingConstant;
import com.gp.cvst.logisoft.domain.GlMapping;

public class ExportGLMappingToExcel extends BaseExcelGenerator {

    private void generateExcelSheet(List<GlMapping> glMappingList) throws Exception {
        String sheetName = "GLMapping";
        writableSheet = writableWorkbook.createSheet(sheetName, 0);
        int row = 0;
        writableSheet.mergeCells(0, row, 6, row);
        writableSheet.mergeCells(6, row, 10, row);
        writableSheet.addCell(new Label(0, row, GLMappingConstant.GLMAPPING_HEADER, headerCell));
        writableSheet.addCell(new Label(7, row, ExcelSheetConstants.DATE + ":" + sdf.format(new Date()), headerCell));

        /*Table Column Header*/

        row++;
        writableSheet.setColumnView(0, 10);
        writableSheet.setColumnView(1, 20);
        writableSheet.setColumnView(2, 18);
        writableSheet.setColumnView(3, 20);
        writableSheet.setColumnView(4, 25);
        writableSheet.setColumnView(5, 15);
        writableSheet.setColumnView(6, 15);
        writableSheet.setColumnView(7, 25);
        writableSheet.setColumnView(8, 25);
        writableSheet.setColumnView(9, 15);
        writableSheet.setColumnView(10, 35);
        writableSheet.addCell(new Label(0, row, GLMappingConstant.GLMAPPING_BLUE_SCREEN_CHARGE_CODE, headerCell));
        writableSheet.addCell(new Label(1, row, GLMappingConstant.GLMAPPING_CHARGE_CODE, headerCell));
        writableSheet.addCell(new Label(2, row, GLMappingConstant.GLMAPPING_GL_ACCOUNT, headerCell));
        writableSheet.addCell(new Label(3, row, GLMappingConstant.GLMAPPING_SHIPMENT_TYPE, headerCell));
        writableSheet.addCell(new Label(4, row, GLMappingConstant.GLMAPPING_TRANSACTION_TYPE, headerCell));
        writableSheet.addCell(new Label(5, row, GLMappingConstant.GLMAPPING_SUFFIX_VALUE, headerCell));
        writableSheet.addCell(new Label(6, row, GLMappingConstant.GLMAPPING_SUFFIX_ALTERNATE, headerCell));
        writableSheet.addCell(new Label(7, row, GLMappingConstant.GL_MAPPING_DERIVE_YN, headerCell));
        writableSheet.addCell(new Label(8, row, GLMappingConstant.GL_MAPPING_SUBLEDGER_CODE, headerCell));
        writableSheet.addCell(new Label(9, row, GLMappingConstant.GL_MAPPING_REV_EXP, headerCell));
        writableSheet.addCell(new Label(10, row, GLMappingConstant.GLMAPPING_CHARGE_DESCRIPTION, headerCell));

        /*Table Data*/
        row++;
        if (null != glMappingList && !glMappingList.isEmpty()) {
            for (GlMapping glMapping : glMappingList) {
                writableSheet.addCell(new Label(0, row, glMapping.getBlueScreenChargeCode(), thinBorderCell));
                writableSheet.addCell(new Label(1, row, glMapping.getChargeCode(), thinBorderCell));
                writableSheet.addCell(new Label(2, row, glMapping.getGlAcct(), thinBorderCell));
                writableSheet.addCell(new Label(3, row, glMapping.getShipmentType(), thinBorderCell));
                writableSheet.addCell(new Label(4, row, glMapping.getTransactionType(), thinBorderCell));
                writableSheet.addCell(new Label(5, row, glMapping.getSuffixValue(), thinBorderCell));
                writableSheet.addCell(new Label(6, row, glMapping.getSuffixAlternate(), thinBorderCell));
                writableSheet.addCell(new Label(7, row, glMapping.getDeriveYn(), thinBorderCell));
                writableSheet.addCell(new Label(8, row, glMapping.getSubLedgerCode(), thinBorderCell));
                writableSheet.addCell(new Label(9, row, glMapping.getRevExp(), thinBorderCell));
                writableSheet.addCell(new Label(10, row, glMapping.getChargeDescriptions(), thinBorderCell));
                row++;
            }
        }
    }

    public void exportToExcel(String excelFilePath, List<GlMapping> glMappingList) throws Exception {
        super.init(excelFilePath);
        this.generateExcelSheet(glMappingList);
        super.write();
        super.close();
    }
}

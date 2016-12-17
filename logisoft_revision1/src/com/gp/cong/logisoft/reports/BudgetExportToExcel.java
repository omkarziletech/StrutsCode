package com.gp.cong.logisoft.reports;

import java.util.List;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import com.gp.cong.logisoft.bc.accounting.ReportConstants;
import com.gp.cvst.logisoft.beans.Budgetbean;
import com.gp.cvst.logisoft.struts.form.BudgetForm;

public class BudgetExportToExcel {

    public WritableSheet getExcelData(WritableWorkbook writableWorkbook, List budgetList, BudgetForm budgetForm) throws Exception {
        WritableSheet writableSheet = writableWorkbook.createSheet("BudgetSheet", 0);
        WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
        WritableFont wf1 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
        WritableCellFormat cf = new WritableCellFormat(wf);
        cf.setBackground(Colour.YELLOW);
        WritableCellFormat cf1 = new WritableCellFormat(wf);
        cf1.setBackground(Colour.GRAY_25);
        WritableCellFormat cf2 = new WritableCellFormat(wf);
        cf2.setBackground(Colour.YELLOW);
        cf2.setAlignment(Alignment.CENTRE);
        cf2.setVerticalAlignment(VerticalAlignment.CENTRE);
        WritableCellFormat cf3 = new WritableCellFormat(wf);
        cf3.setBackground(Colour.GRAY_25);
        cf3.setAlignment(Alignment.CENTRE);
        WritableCellFormat cf4 = new WritableCellFormat(wf);
        cf4.setBackground(Colour.YELLOW);
        cf4.setAlignment(Alignment.RIGHT);
        WritableCellFormat cf5 = new WritableCellFormat(wf1);
        cf5.setAlignment(Alignment.RIGHT);
        Budgetbean budgetbean = new Budgetbean();
        writableSheet.addCell(new Label(1, 1, ReportConstants.BUDGETS, cf2));
        for (int col = 5; col < 18; col++) {
            if (col == 11) {
                writableSheet.addCell(new Label(col, 2, ReportConstants.BUDGETPERIOD, cf3));
            } else {
                writableSheet.addCell(new Label(col, 2, "", cf1));
            }
        }
        writableSheet.addCell(new Label(1, 3, ReportConstants.BUDACCTNUM, cf));
        writableSheet.addCell(new Label(2, 3, ReportConstants.BUDYEAR, cf));
        writableSheet.addCell(new Label(3, 3, ReportConstants.BUDSET, cf));
        writableSheet.addCell(new Label(4, 3, ReportConstants.BUDCURRENCY, cf));
        int column = 5;
        for (int i = 1; i <= 13; i++) {
            writableSheet.addCell(new Label(column, 3, "" + i, cf4));
            column++;
        }
        if (null != budgetList && !budgetList.equals("")) {
            int size = budgetList.size();
            int index = 0, i = 5, dataColumnSet = 1;
            while (size > 0) {
                budgetbean = (Budgetbean) budgetList.get(index);
                if (dataColumnSet == 1) {
                    writableSheet.addCell(new Label(1, 4, budgetbean.getBudgetaccount()));
                    writableSheet.addCell(new Label(2, 4, budgetbean.getYear()));
                    writableSheet.addCell(new Label(3, 4, budgetbean.getBudgetSet()));
                    //writableSheet.addCell(new Label(4, 4, budgetForm.getCurrency1()));
                    writableSheet.addCell(new Label(5, 4, budgetbean.getBudgetamount(), cf5));
                } else {
                    writableSheet.addCell(new Label(i, 4, budgetbean.getBudgetamount(), cf5));
                }
                index++;
                size--;
                i++;
                dataColumnSet++;
            }
        }
        return writableSheet;
    }
}

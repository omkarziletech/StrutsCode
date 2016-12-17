package com.gp.cvst.logisoft.util;

import java.io.File;
import java.io.IOException;

import com.gp.cvst.logisoft.beans.Budgetbean;
import java.util.Date;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.log4j.Logger;

public class BudgetExcelReader {

    private static final Logger log = Logger.getLogger(BudgetExcelReader.class);

    public void retrieveValuesFromExcel(File file) {
	try {
	    Workbook workbook = null;
	    Sheet sheet = null;
	    workbook = Workbook.getWorkbook(file);
	    Sheet sheets[] = workbook.getSheets();
	    for (int i = 0; i < sheets.length; i++) {
		sheet = workbook.getSheet(i);
		String name = sheet.getName();
		int rows = sheet.getRows();
		int cols = sheet.getColumns();
		int count = 0;
		Budgetbean bean = null;
		for (int r = 0; r < rows; r++) {
		    for (int c = 0; c < cols; c++) {
			if (r == 4 && c > 4 && c < 17) {
			    bean.setBudgetamount(sheet.getCell(c, r).getContents());
			    count++;
			}
		    }
		}
	    }
	} catch (BiffException e) {
	    log.info("retrieveValuesFromExcel failed on " + new Date(),e);
	} catch (IOException e) {
	    log.info("retrieveValuesFromExcel failed on " + new Date(),e);
	}
    }
}

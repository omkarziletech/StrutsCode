package com.gp.cvst.logisoft.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import jxl.Cell;
import jxl.Sheet;

import org.apache.log4j.Logger;




public class ExcelReader {
private static final Logger log = Logger.getLogger(ExcelReader.class);
    FileInputStream fs = null;
    String[] header = null;

    private void init(String filePath) {
        try {
            fs = new FileInputStream(new File(filePath));
        } catch (IOException e) {
            log.info("init failed on " + new Date(),e);
        } catch (Exception e) {
            log.info("init failed on " + new Date(),e);
        }
    }
    //Returns the Headings used inside the excel sheet

    private void getHeadingFromXlsFile(Sheet sheet) {
        if (sheet.getRows() >= 2) {
            Cell rowData[] = sheet.getRow(0);
            header = new String[rowData.length];
            for (int j = 0; j < rowData.length; j++) {
                header[j] = rowData[j].getContents();
                if (!rowData[j].getContents().trim().equals("")) {
                    System.out.print(rowData[j].getContents() + "\t");
                }
            }
        }
    }

    

    private void close() {
        try {
            if (null != fs) {
                fs.close();
            }
        } catch (IOException e) {
            log.info("close failed on " + new Date(),e);
        }
    }

    public boolean getExcelContents(String filePath, String pageAction) {
        try {
            if (null != filePath && !filePath.trim().equals("")) {
                this.init(filePath);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.info("getExcelContents failed on " + new Date(),e);
            return false;
        }

    }
}

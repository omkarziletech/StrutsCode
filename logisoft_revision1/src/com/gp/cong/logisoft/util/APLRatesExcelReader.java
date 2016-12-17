package com.gp.cong.logisoft.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

public class APLRatesExcelReader {

    public List<APLRatesUSCargo> getAPLRatesUSCargoAsList(File file) throws Exception {
        List<APLRatesUSCargo> aplRatesUSCargoList = null;
        Workbook workbook = null;
        Sheet sheet = null;
        workbook = Workbook.getWorkbook(file);
        Sheet sheets[] = workbook.getSheets();
        aplRatesUSCargoList = new ArrayList<APLRatesUSCargo>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        for (int i = 0; i < sheets.length; i++) {
            sheet = workbook.getSheet(i);
            String name = sheet.getName();
            int rows = sheet.getRows();
            int cols = sheet.getColumns();
            for (int j = 1; j < rows; j++) {
                if (cols > 10 && !sheet.getCell(8, j).getContents().equalsIgnoreCase("Start Date") && !sheet.getCell(9, j).getContents().equalsIgnoreCase("End Date")) {
                    if (sheet.getCell(0, j).getContents() != null && !sheet.getCell(0, j).getContents().trim().equals("")) {
                        APLRatesUSCargo aplRatesUSCargo = new APLRatesUSCargo();
                        aplRatesUSCargo.setOriginCode(sheet.getCell(0, j).getContents());
                        aplRatesUSCargo.setOrigin(sheet.getCell(1, j).getContents());
                        aplRatesUSCargo.setPOL(sheet.getCell(2, j).getContents());
                        aplRatesUSCargo.setDestinationCode(sheet.getCell(3, j).getContents());
                        aplRatesUSCargo.setDestination(sheet.getCell(4, j).getContents());
                        aplRatesUSCargo.setCommodityCode(sheet.getCell(5, j).getContents());
                        aplRatesUSCargo.setSteamshipLine(sheet.getCell(6, j).getContents());
                        aplRatesUSCargo.setContractNo(sheet.getCell(7, j).getContents());
                        if (sheet.getCell(8, j).getContents() != null && !sheet.getCell(8, j).getContents().equals("")) {
                            String date = sheet.getCell(8, j).getContents();
                            aplRatesUSCargo.setStartDate(simpleDateFormat.parse(sheet.getCell(8, j).getContents()));
                        }
                        if (sheet.getCell(9, j).getContents() != null && !sheet.getCell(9, j).getContents().equals("")) {
                            String date = sheet.getCell(9, j).getContents();
                            aplRatesUSCargo.setEndDate(simpleDateFormat.parse(date));
                        }
                        for (int k = 10; k < cols; k++) {
                            aplRatesUSCargo.addCostCode(sheet.getCell(k, j).getContents());
                        }

                        aplRatesUSCargoList.add(aplRatesUSCargo);

                    }
                }
            }

        }



        workbook.close();
        return aplRatesUSCargoList;
    }
}

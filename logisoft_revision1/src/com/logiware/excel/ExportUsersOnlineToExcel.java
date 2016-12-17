/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.logiware.excel;

import com.gp.cong.logisoft.ExcelGenerator.BaseExcelGenerator;
import java.util.List;
import com.logiware.bean.UserObjectBean;
import java.util.Date;
import jxl.write.Label;
import jxl.write.WriteException;

import org.apache.log4j.Logger;

/**
 *
 * @author Vinay
 */
public class ExportUsersOnlineToExcel extends BaseExcelGenerator {
private static final Logger log = Logger.getLogger(ExportUsersOnlineToExcel.class);
    int row = 0;

    private boolean generateExcelSheet(List<UserObjectBean> usersOnlineList) {
        try {
            writableSheet = writableWorkbook.createSheet("Users Online", 0);
            addHeaders();
            for(UserObjectBean user: usersOnlineList) {
                addToWorkbook(user);
            }
            return true;
        } catch (Exception e) {

        log.info("generateExcelSheet failed on " + new Date(),e);
        }
        return false;
    }

    private void addHeaders() throws WriteException {
        int col = 0;
        writableSheet.setColumnView(0, 20);
        writableSheet.setColumnView(1, 20);
        writableSheet.setColumnView(2, 20);
        writableSheet.setColumnView(3, 20);
        writableSheet.setColumnView(4, 20);
        writableSheet.setColumnView(5, 20);
        writableSheet.setColumnView(6, 20);
        writableSheet.setColumnView(7, 20);
        writableSheet.setColumnView(8, 20);
        writableSheet.setColumnView(9, 20);
        writableSheet.setColumnView(10, 20);
        writableSheet.setColumnView(11, 20);
        writableSheet.setColumnView(12, 20);
        writableSheet.addCell(new Label(col++, row, "User ID", headerCell));
        writableSheet.addCell(new Label(col++, row, "User Name", headerCell));
        writableSheet.addCell(new Label(col++, row, "First Name", headerCell));
        writableSheet.addCell(new Label(col++, row, "Last Name", headerCell));
        writableSheet.addCell(new Label(col++, row, "Terminal", headerCell));
        writableSheet.addCell(new Label(col++, row, "Telephone #", headerCell));
        writableSheet.addCell(new Label(col++, row, "Address 1", headerCell));
        writableSheet.addCell(new Label(col++, row, "Address 2", headerCell));
        writableSheet.addCell(new Label(col++, row, "Country", headerCell));
        writableSheet.addCell(new Label(col++, row, "State", headerCell));
        writableSheet.addCell(new Label(col++, row, "City", headerCell));
        writableSheet.addCell(new Label(col++, row, "User IP", headerCell));
        writableSheet.addCell(new Label(col++, row, "Logged On", headerCell));
    }

    private void addToWorkbook(UserObjectBean user) throws WriteException {
        row++;
        int col = 0;
        writableSheet.addCell(new Label(col++, row, "" + user.getUserId(), thinBorderCell));
        writableSheet.addCell(new Label(col++, row, user.getLoginName(), thinBorderCell));
        writableSheet.addCell(new Label(col++, row, user.getFirstName(), thinBorderCell));
        writableSheet.addCell(new Label(col++, row, user.getLastName(), thinBorderCell));
        writableSheet.addCell(new Label(col++, row, user.getTerm(), thinBorderCell));
        writableSheet.addCell(new Label(col++, row, user.getTelephone(), thinBorderCell));
        writableSheet.addCell(new Label(col++, row, user.getAddress1(), thinBorderCell));
        writableSheet.addCell(new Label(col++, row, user.getAddress2(), thinBorderCell));
        writableSheet.addCell(new Label(col++, row, user.getCountry(), thinBorderCell));
        writableSheet.addCell(new Label(col++, row, user.getState(), thinBorderCell));
        writableSheet.addCell(new Label(col++, row, user.getCity(), thinBorderCell));
        writableSheet.addCell(new Label(col++, row, user.getUserIpAddress(), thinBorderCell));
        writableSheet.addCell(new Label(col++, row, user.getDate(), thinBorderCell));
    }

    public String exportToExcel(String excelFilePath, List<UserObjectBean> usersOnlineList) {
        String excelFileName = null;
        if (null != excelFilePath) {
            init(excelFilePath);
            if (null != writableWorkbook) {
                if (this.generateExcelSheet(usersOnlineList)) {
                    try {
                        write();
                        close();
                        excelFileName = excelFilePath;
                    } catch (Exception e) {
                        excelFileName = null;
                        log.info("exportToExcel failed on " + new Date(),e);
                    }
                }
            }
        }
        return excelFileName;
    }

}

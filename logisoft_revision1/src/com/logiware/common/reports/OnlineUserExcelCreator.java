package com.logiware.common.reports;

import com.gp.cong.common.DateUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.common.dao.OnlineUserDAO;
import com.logiware.common.model.ResultModel;
import com.logiware.excel.BaseExcelCreator;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Lakshmi Narayanan
 */
public class OnlineUserExcelCreator extends BaseExcelCreator {

    private void writeHeader() throws Exception {
        createRow();
        createHeaderCell("Online Users", headerCellStyleLeftBold);
        mergeCells(rowIndex, rowIndex, 0, 11);
        row.setHeightInPoints(20);
        createRow();
        resetColumnIndex();
        createHeaderCell("Date : ", subHeaderOneCellStyleLeftBold);
        createHeaderCell(DateUtils.formatDate(new Date(), "MMM dd, yyyy hh:mm:ss a"), subHeaderOneCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 1, 11);
        createRow();
        resetColumnIndex();
        createHeaderCell("Login Name", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("First Name", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 25);
        createHeaderCell("Last Name", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 25);
        createHeaderCell("Terminal", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 25);
        createHeaderCell("Phone", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);
        createHeaderCell("Email", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 35);
        createHeaderCell("Address", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 25);
        createHeaderCell("City", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);
        createHeaderCell("State", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 10);
        createHeaderCell("Country", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("IP Address", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);
        createHeaderCell("Logged On", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 30);
    }

    private void writeContent() throws Exception {
        List<ResultModel> onlineUsers = new OnlineUserDAO().getOnlineUsers("loggedOn", "desc", null, null);
        int rowCount = 0;
        for (ResultModel onlineUser : onlineUsers) {
            createRow();
            resetColumnIndex();
            if (rowCount % 2 == 0) {
                createTextCell(onlineUser.getLoginName(), tableEvenRowCellStyleLeftNormal);
                createTextCell(onlineUser.getFirstName(), tableEvenRowCellStyleLeftNormal);
                createTextCell(onlineUser.getLastName(), tableEvenRowCellStyleLeftNormal);
                createTextCell(onlineUser.getTerminal(), tableEvenRowCellStyleLeftNormal);
                createTextCell(onlineUser.getPhone(), tableEvenRowCellStyleLeftNormal);
                createTextCell(onlineUser.getEmail(), tableEvenRowCellStyleLeftNormal);
                createTextCell(onlineUser.getAddress(), tableEvenRowCellStyleLeftNormal);
                createTextCell(onlineUser.getCity(), tableEvenRowCellStyleLeftNormal);
                createTextCell(onlineUser.getState(), tableEvenRowCellStyleLeftNormal);
                createTextCell(onlineUser.getCountry(), tableEvenRowCellStyleLeftNormal);
                createTextCell(onlineUser.getIpAddress(), tableEvenRowCellStyleLeftNormal);
                createTextCell(onlineUser.getLoggedOn(), tableEvenRowCellStyleLeftNormal);
            } else {
                createTextCell(onlineUser.getLoginName(), tableOddRowCellStyleLeftNormal);
                createTextCell(onlineUser.getFirstName(), tableOddRowCellStyleLeftNormal);
                createTextCell(onlineUser.getLastName(), tableOddRowCellStyleLeftNormal);
                createTextCell(onlineUser.getTerminal(), tableOddRowCellStyleLeftNormal);
                createTextCell(onlineUser.getPhone(), tableOddRowCellStyleLeftNormal);
                createTextCell(onlineUser.getEmail(), tableOddRowCellStyleLeftNormal);
                createTextCell(onlineUser.getAddress(), tableOddRowCellStyleLeftNormal);
                createTextCell(onlineUser.getCity(), tableOddRowCellStyleLeftNormal);
                createTextCell(onlineUser.getState(), tableOddRowCellStyleLeftNormal);
                createTextCell(onlineUser.getCountry(), tableOddRowCellStyleLeftNormal);
                createTextCell(onlineUser.getIpAddress(), tableOddRowCellStyleLeftNormal);
                createTextCell(onlineUser.getLoggedOn(), tableOddRowCellStyleLeftNormal);
            }
            rowCount++;
        }
    }

    public String create() throws Exception {
        try {
            StringBuilder filePath = new StringBuilder();
            filePath.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/OnlineUsers/");
            filePath.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
            File file = new File(filePath.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            filePath.append("Online_Users").append(".xlsx");
            init(filePath.toString(), "Online Users");
            writeHeader();
            writeContent();
            writeIntoFile();
            return filePath.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            exit();
        }
    }
}

package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.beans.ReleaseReportBean;
import com.logiware.excel.BaseExcelCreator;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 *
 * @author NambuRajasekar
 */
public class SendReleaseReportXls extends BaseExcelCreator {

    public String createExcel(String originId, String unLocName) throws Exception {
        UnLocation unLocation = new UnLocationDAO().findById(Integer.parseInt(originId));
        StringBuilder fileNameBuilder = new StringBuilder(LoadLogisoftProperties.getProperty("reportLocation"));
        fileNameBuilder.append("/LclReleaseReport/Export/").append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
        File file = new File(fileNameBuilder.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        fileNameBuilder.append("ReleaseReport_").append(unLocation.getUnLocationName()).append("(").append(unLocation.getUnLocationCode()).append(")");
        fileNameBuilder.append(DateUtils.formatDate(new Date(), "_yyyyMMdd_HHmmss")).append(".xlsx");
        init(fileNameBuilder.toString(), originId);
        writeContents(originId, unLocName);
        writeIntoFile();
        return fileNameBuilder.toString();
    }

    private void writeContents(String originId, String unLocName) throws Exception {
        createRow();
        resetColumnIndex();
        createHeaderCell("POL", redCellStyleCenterBold);
        createHeaderCell(unLocName, redCellStyleCenterBold);
        createHeaderCell(DateUtils.formatStringDateToAppFormat(new Date()), redCellStyleCenterBold);
        createRow();
        resetColumnIndex();
        createRow();
        resetColumnIndex();
        createHeaderCell("CityName/UnLocation", tableHeaderCellStyleCenterBold);
        createHeaderCell("PieceCount", tableHeaderCellStyleCenterBold);
        createHeaderCell("Weight", tableHeaderCellStyleCenterBold);
        createHeaderCell("Cube", tableHeaderCellStyleCenterBold);
        createHeaderCell("TotalDrs", tableHeaderCellStyleCenterBold);
        LCLBookingDAO lCLBookingDAO = new LCLBookingDAO();
        List<ReleaseReportBean> reportBean = lCLBookingDAO.getBookingDetails(originId);
        for (ReleaseReportBean bean : reportBean) {
            createRow();
            resetColumnIndex();
            createTextCell(bean.getUnLocation(), tableEvenRowCellStyleLeftNormal);
            createIntegerCell(bean.getPieceCount(), tableEvenRowCellStyleLeftNormal);
            createDoubleCell(bean.getWeight(), tableEvenRowCellStyleLeftNormal);
            createDoubleCell(bean.getCube(), tableEvenRowCellStyleLeftNormal);
            createIntegerCell(bean.getTotalDr(), tableEvenRowCellStyleLeftNormal);
        }
    }
}

package com.logiware.excel;

import com.gp.cong.common.DateUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.hibernate.domain.TruckerRates;
import java.io.File;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 *
 * @author Lakshmi Narayanan
 */
public class TruckerRatesExcelCreator extends BaseExcelCreator {

    private void writeContents(List<TruckerRates> errorRatesList) throws Exception {
	createRow();
	resetColumnIndex();
	createHeaderCell("Trucker", cellStyleLeftBold);
	createHeaderCell("Vendor Account", cellStyleLeftBold);
	createHeaderCell("From Zip Code", cellStyleLeftBold);
	createHeaderCell("From City", cellStyleLeftBold);
	createHeaderCell("From State", cellStyleCenterBold);
	createHeaderCell("To Port", cellStyleLeftBold);
	createHeaderCell("Rate", cellStyleLeftBold);
	createHeaderCell("Fuel", cellStyleLeftBold);
	createHeaderCell("Buy", cellStyleLeftBold);
	createHeaderCell("Markup %", cellStyleLeftBold);
	createHeaderCell("Sell", cellStyleLeftBold);
	CellStyle cellStyle = createCellStyle(WHITE, blackNormalFont11, CellStyle.ALIGN_CENTER);
	for (TruckerRates truckerRates : errorRatesList) {
	    createRow();
	    resetColumnIndex();
	    createTextCell(truckerRates.getTruckerName(), cellStyleLeftNormalForTruker);
	    createTextCell(truckerRates.getTruckerNumber(), null == truckerRates.getTrucker() ? redCellStyleLeftNormal : cellStyleLeftNormalForTruker);
	    createIntegerCell(truckerRates.getFromZipCode(), null == truckerRates.getFromZip() ? redCellStyleCenterNormal : cellStyleCenterNormalForTruker);
	    createTextCell(truckerRates.getFromCity(), cellStyleLeftNormalForTruker);
	    createTextCell(truckerRates.getFromState(), cellStyleCenterNormalForTruker);
	    createTextCell(truckerRates.getToPortCode(), null == truckerRates.getToPort() ? redCellStyleLeftNormal : cellStyleLeftNormalForTruker);
	    createAmountCell(truckerRates.getRate(), cellStyleCenterNormalForTruker);
	    createDollarCell(truckerRates.getFuel(), cellStyleCenterNormalForTruker);
	    createDollarCell(truckerRates.getBuy(), cellStyleCenterNormalForTruker);
	    createPercentageCell(truckerRates.getMarkup(), cellStyle);
	    createDollarCell(truckerRates.getSell(), cellStyleCenterNormalForTruker);
	}
	setColumnAutoSize();
    }

    public String createExcel(List<TruckerRates> errorRatesList, String ratesErrorFileName) throws Exception {
	try {
	    StringBuilder fileNameBuilder = new StringBuilder(LoadLogisoftProperties.getProperty("reportLocation"));
	    fileNameBuilder.append("/Documents/TruckerRates/");
            fileNameBuilder.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
	    File file = new File(fileNameBuilder.toString());
	    if (!file.exists()) {
		file.mkdirs();
	    }
	    fileNameBuilder.append(ratesErrorFileName).append("_errors").append(".xlsx");
	    init(fileNameBuilder.toString(), ratesErrorFileName);
	    writeContents(errorRatesList);
	    writeIntoFile();
	    errorRatesList.clear();
	    return fileNameBuilder.toString();
	} catch (Exception e) {
	    throw e;
	} finally {
	    exit();
	}
    }
}

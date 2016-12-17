package com.logiware.bc;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.ExcelGenerator.ExportGLMappingToExcel;
import com.gp.cong.logisoft.bc.accounting.GLMappingConstant;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.AccountDetails;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.hibernate.dao.AccountDetailsDAO;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.logiware.form.GlMappingForm;
import com.gp.cvst.logisoft.util.ExcelReader;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.commons.lang3.ArrayUtils;

public class GlMappingBC {

    /**
     * export gl mapping to excel
     * @param glMappingForm
     * @param contextPath
     * @return excel file name
     */
    public String exportGlMapping(GlMappingForm glMappingForm) throws Exception {
	List<GlMapping> glMappings = new GlMappingDAO().getGlMappings(glMappingForm.getSearchBychargeCode(), glMappingForm.getStartAccount(), glMappingForm.getEndAccount());
	String fileName = "GlMapping.xls";
	String folderPath = LoadLogisoftProperties.getProperty("reportLocation");
	File file = new File(folderPath);
	if (!file.exists()) {
	    file.mkdir();
	}
	String excelFilePath = LoadLogisoftProperties.getProperty("reportLocation") + "/" + fileName;
	new ExportGLMappingToExcel().exportToExcel(excelFilePath, glMappings);
	return excelFilePath;
    }

    /**
     * upload gl mapping sheet
     * @param glMappingForm
     * @throws Exception
     */
    public void uploadGlMapping(GlMappingForm glMappingForm, String userName) throws Exception {
	Workbook workbook = Workbook.getWorkbook(glMappingForm.getGlMappingSheet().getInputStream());
	int totalSheet = workbook.getNumberOfSheets();
	if (totalSheet > 0) {
	    Sheet sheet = null;
	    if (totalSheet > 1) {
		for (int j = 0; j < totalSheet; j++) {
		    if (workbook.getSheet(j).getName().trim().equals(GLMappingConstant.GL_MAPPING_GL_EXP)) {
			sheet = workbook.getSheet(j);
		    }
		}
		if (null == sheet) {
		    sheet = workbook.getSheet(0);
		}
	    } else {
		sheet = workbook.getSheet(0);
	    }
	    if (null != sheet) {
		this.readContents(sheet, userName);
	    }
	}
    }

    private void readContents(Sheet sheet, String userName) throws Exception {
	//Reading content from GL-EXP sheet
	if (null != sheet) {
	    GlMappingDAO glMappingDAO = new GlMappingDAO();
	    //Get Header Cell
	    String[] headers = new String[sheet.getRows()];
	    int firstDataRow = 0;
	    for (int i = 0; i < sheet.getRows(); i++) {
		Cell[] cells = sheet.getRow(i);
		if (null != cells) {
		    int index = 0;
		    for (Cell cell : cells) {
			headers[index] = cell.getContents();
			index++;
		    }
		    firstDataRow++;
		    int chargeCodeIndex = ArrayUtils.indexOf(headers, GLMappingConstant.GLMAPPING_CHARGE_CODE);
		    if (chargeCodeIndex >= 0) {
			break;
		    }
		}
	    }
	    int blueScreenChargeCodeIndex = ArrayUtils.indexOf(headers, GLMappingConstant.GLMAPPING_BLUE_SCREEN_CHARGE_CODE);
	    int chargeCodeIndex = ArrayUtils.indexOf(headers, GLMappingConstant.GLMAPPING_CHARGE_CODE);
	    int glAccountIndex = ArrayUtils.indexOf(headers, GLMappingConstant.GLMAPPING_GL_ACCOUNT);
	    int shipmentTypeIndex = ArrayUtils.indexOf(headers, GLMappingConstant.GLMAPPING_SHIPMENT_TYPE);
	    int transactionTypeIndex = ArrayUtils.indexOf(headers, GLMappingConstant.GLMAPPING_TRANSACTION_TYPE);
	    int suffixValueIndex = ArrayUtils.indexOf(headers, GLMappingConstant.GLMAPPING_SUFFIX_VALUE);
	    int suffixAlternateIndex = ArrayUtils.indexOf(headers, GLMappingConstant.GLMAPPING_SUFFIX_ALTERNATE);
	    int deriveYnIndex = ArrayUtils.indexOf(headers, GLMappingConstant.GL_MAPPING_DERIVE_YN);
	    int subledgerCodeIndex = ArrayUtils.indexOf(headers, GLMappingConstant.GL_MAPPING_SUBLEDGER_CODE);
	    int revExpCodeIndex = ArrayUtils.indexOf(headers, GLMappingConstant.GL_MAPPING_REV_EXP);
	    int chargeDescriptionIndex = ArrayUtils.indexOf(headers, GLMappingConstant.GLMAPPING_CHARGE_DESCRIPTION);
	    for (int i = firstDataRow; i < sheet.getRows(); i++) {
		//Get Individual Row
		Cell[] rowData = sheet.getRow(i);
		if (null != rowData && !rowData[0].getContents().trim().equals("")) {
		    GlMapping glMapping = new GlMapping();
		    if (blueScreenChargeCodeIndex != -1 && rowData.length > blueScreenChargeCodeIndex
			    && CommonUtils.isNotEmpty(rowData[blueScreenChargeCodeIndex].getContents())) {
			glMapping.setBlueScreenChargeCode(rowData[blueScreenChargeCodeIndex].getContents().trim());
		    }
		    if (chargeCodeIndex != -1 && rowData.length > chargeCodeIndex
			    && CommonUtils.isNotEmpty(rowData[chargeCodeIndex].getContents())) {
			glMapping.setChargeCode(rowData[chargeCodeIndex].getContents().trim());
		    }
		    if (glAccountIndex != -1 && rowData.length > glAccountIndex
			    && CommonUtils.isNotEmpty(rowData[glAccountIndex].getContents())) {
			glMapping.setGlAcct(rowData[glAccountIndex].getContents().trim());
			List<AccountDetails> accountDetailses = new AccountDetailsDAO().findByProperty("account", "%" + glMapping.getGlAcct() + "%");
			if (CommonUtils.isEmpty(accountDetailses)) {
			    throw new Exception(glMapping.getGlAcct() + " is not a valid GL account");
			}
		    }
		    if (shipmentTypeIndex != -1 && rowData.length > shipmentTypeIndex
			    && CommonUtils.isNotEmpty(rowData[shipmentTypeIndex].getContents())) {
			glMapping.setShipmentType(rowData[shipmentTypeIndex].getContents().trim());
		    }
		    if (transactionTypeIndex != -1 && rowData.length > transactionTypeIndex
			    && CommonUtils.isNotEmpty(rowData[transactionTypeIndex].getContents())) {
			glMapping.setTransactionType(rowData[transactionTypeIndex].getContents().trim());
		    }
		    if (suffixValueIndex != -1 && rowData.length > suffixValueIndex
			    && CommonUtils.isNotEmpty(rowData[suffixValueIndex].getContents())) {
			glMapping.setSuffixValue(rowData[suffixValueIndex].getContents().trim());
		    }
		    if (suffixAlternateIndex != -1 && rowData.length > suffixAlternateIndex
			    && CommonUtils.isNotEmpty(rowData[suffixAlternateIndex].getContents())) {
			glMapping.setSuffixAlternate(rowData[suffixAlternateIndex].getContents().trim());
		    }
		    if (deriveYnIndex != -1 && rowData.length > deriveYnIndex
			    && CommonUtils.isNotEmpty(rowData[deriveYnIndex].getContents())) {
			glMapping.setDeriveYn(rowData[deriveYnIndex].getContents().trim());
		    }
		    if (subledgerCodeIndex != -1 && rowData.length > subledgerCodeIndex
			    && CommonUtils.isNotEmpty(rowData[subledgerCodeIndex].getContents())) {
			glMapping.setSubLedgerCode(rowData[subledgerCodeIndex].getContents().trim());
		    }
		    if (revExpCodeIndex != -1 && rowData.length > revExpCodeIndex
			    && CommonUtils.isNotEmpty(rowData[revExpCodeIndex].getContents())) {
			glMapping.setRevExp(rowData[revExpCodeIndex].getContents().trim());
		    }
		    if (chargeDescriptionIndex != -1 && rowData.length > chargeDescriptionIndex
			    && CommonUtils.isNotEmpty(rowData[chargeDescriptionIndex].getContents())) {
			glMapping.setChargeDescriptions(rowData[chargeDescriptionIndex].getContents().trim());
		    }
		    if (CommonUtils.isNotEmpty(glMapping.getChargeCode())) {
			if (glMappingDAO.isValidBlueScreenChargeCode(glMapping.getBlueScreenChargeCode(), glMapping.getChargeCode(), glMapping.getTransactionType(), glMapping.getShipmentType())) {
			    glMappingDAO.findAndSaveOrUpdate(glMapping, userName);
			}else{
			    StringBuilder message = new StringBuilder();
			    message.append("Blue Screen Charge Code - ").append(glMapping.getBlueScreenChargeCode());
			    message.append(" is already mapped with different Charge/Cost Code");
			    throw new Exception(message.toString());
			}
		    }
		}
	    }
	}
    }
}

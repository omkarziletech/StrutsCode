package com.logiware.utils;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cvst.logisoft.domain.AccountDetails;
import com.gp.cvst.logisoft.domain.Batch;
import com.gp.cvst.logisoft.domain.JournalEntry;
import com.gp.cvst.logisoft.domain.LineItem;
import com.gp.cvst.logisoft.hibernate.dao.AccountDetailsDAO;
import com.logiware.bean.GlBatchBean;
import com.logiware.bean.JournalEntryBean;
import com.logiware.bean.LineItemBean;
import com.logiware.form.GlBatchForm;
import com.logiware.hibernate.dao.GlBatchDAO;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class JournalEntryUploader {

    //Total debit from line items
    private Double totalDebit = Double.valueOf(0d);
    //Total credit from line items
    private Double totalCredit = Double.valueOf(0d);
    //Set of errors
    private TreeSet<String> errors = new TreeSet();

    /**
     * importBatch - import line items based on batch level and add it into new batch
     * @param glBatchForm
     * @param request
     * @throws Exception 
     */
    public void importBatch(GlBatchForm glBatchForm, HttpServletRequest request) throws Exception {
	InputStream inputStream = null;
	try {
	    GlBatchDAO glBatchDAO = new GlBatchDAO();
	    //Get excel sheet in input stream
	    inputStream = new FileInputStream(glBatchForm.getBatchFileName());
	    //Create workbook from input stream
	    Workbook workbook = WorkbookFactory.create(inputStream);
	    //Get first sheet
	    Sheet sheet = workbook.getSheetAt(0);
	    //Get description from sheet
	    String description = getDescription(sheet.getRow(0));
	    //Get period from sheet
	    String period = getPeriod(sheet.getRow(0));
	    //Get subledger from database
	    GenericCode subledgerCode = new GenericCodeDAO().getGenericCodeByCode("GL-JE");
	    //Create batch
	    Batch batch = createBatch(description, period, subledgerCode);
	    //Create journal entry
	    JournalEntry journalEntry = createJournalEntry(batch.getBatchId(), null, description, period, subledgerCode);
	    //Create line items from sheet
	    List<LineItem> lineItems = createLineItems(sheet, journalEntry.getJournalEntryId(), glBatchForm);
	    //Set debit and credit values
	    journalEntry.setDebit(this.totalDebit);
	    journalEntry.setCredit(this.totalCredit);
	    batch.setTotalDebit(this.totalDebit);
	    batch.setTotalCredit(this.totalCredit);
	    Set lineItemSet = new HashSet(lineItems);
	    journalEntry.setLineItemSet(lineItemSet);
	    Set journalEntries = new HashSet();
	    journalEntries.add(journalEntry);
	    batch.setJournalEntrySet(journalEntries);
	    //Update batch in database
	    glBatchDAO.update(batch);
	    request.setAttribute("message", "Journal Entry Sheet uploaded successfully");
	    Integer batchId = batch.getBatchId();
	    String journalEntryId = journalEntry.getJournalEntryId();
	    //Set form values
	    glBatchForm.setGlBatch(new GlBatchBean(batch));
	    glBatchForm.setJournalEntryIndex(glBatchDAO.getJournalEntryIndex(batchId, journalEntryId));
	    glBatchForm.setBatchDebit(glBatchDAO.getTotalDebitFromJEs(batchId, journalEntryId).toString());
	    glBatchForm.setBatchCredit(glBatchDAO.getTotalCreditFromJEs(batchId, journalEntryId).toString());
	    glBatchForm.setJournalEntry(new JournalEntryBean(journalEntry));
	    glBatchForm.setBatchIndex(glBatchDAO.getBatchIndex(batchId));
	    if ((!this.errors.isEmpty()) && (this.errors.size() == 1)) {
		request.setAttribute("error", "GL Account - " + this.errors.toString() + " is not found in System and got skipped.");
	    } else if (!this.errors.isEmpty()) {
		request.setAttribute("error", "GL Accounts - " + this.errors.toString() + " are not found in System and got skipped.");
	    }
	    //Close the input stream
	    if (null != inputStream) {
		inputStream.close();
	    }
	} catch (Exception e) {
	    request.setAttribute("error", "Upload failed - " + e.getMessage());
	    //Close the input stream
	    if (null != inputStream) {
		inputStream.close();
	    }
	} finally {
	    //Close the input stream
	    if (null != inputStream) {
		inputStream.close();
	    }
	}
    }

    /**
     * importJournalEntry - import line items based on journal entry level and add it into current batch
     * @param glBatchForm
     * @param request
     * @throws Exception 
     */
    public void importJournalEntry(GlBatchForm glBatchForm, HttpServletRequest request) throws Exception {
	InputStream inputStream = null;
	try {
	    GlBatchDAO glBatchDAO = new GlBatchDAO();
	    //Commit old hibernate transaction to save the unsaved changes in previous journal entry
	    glBatchDAO.getCurrentSession().getTransaction().commit();
	    //Create new hibernate transaction
	    glBatchDAO.getCurrentSession().beginTransaction();
	    //Get excel sheet in input stream
	    inputStream = new FileInputStream(glBatchForm.getJournalEntryFileName());
	    //Create workbook from input stream
	    Workbook workbook = WorkbookFactory.create(inputStream);
	    //Get first sheet
	    Sheet sheet = workbook.getSheetAt(0);
	    //Get description from sheet
	    String description = getDescription(sheet.getRow(0));
	    //Get period from sheet
	    String period = getPeriod(sheet.getRow(0));
	    //Get subledger from database
	    GenericCode subledgerCode = new GenericCodeDAO().getGenericCodeByCode("GL-JE");
	    //Get batch from form
	    GlBatchBean glBatch = glBatchForm.getGlBatch();
	    Integer batchId = Integer.valueOf(Integer.parseInt(glBatch.getId()));
	    Batch batch = glBatchDAO.findById(batchId);
	    Integer suffix = glBatchDAO.getLastJournalEntrySuffix(batchId);
	    suffix++;
	    String journalEntryId = batchId + "-" + StringUtils.leftPad(suffix.toString(), 3, "0");
	    //Create journal entry 
	    JournalEntry journalEntry = createJournalEntry(batch.getBatchId(), journalEntryId, description, period, subledgerCode);
	    //Create line items from sheet
	    List lineItems = createLineItems(sheet, journalEntry.getJournalEntryId(), glBatchForm);
	    //Set debit and credit values 
	    journalEntry.setDebit(this.totalDebit);
	    journalEntry.setCredit(this.totalCredit);
	    batch.setTotalDebit(batch.getTotalDebit() + this.totalDebit);
	    batch.setTotalCredit(batch.getTotalCredit() + this.totalCredit);
	    Set lineItemSet = new HashSet(lineItems);
	    journalEntry.setLineItemSet(lineItemSet);
	    Set journalEntries = batch.getJournalEntrySet();
	    if (null == journalEntries || journalEntries.isEmpty()) {
		journalEntries = new HashSet();
	    }
	    journalEntries.add(journalEntry);
	    batch.setJournalEntrySet(journalEntries);
	    //Update batch in database
	    glBatchDAO.update(batch);
	    request.setAttribute("message", "Journal Entry Sheet uploaded successfully");
	    //Set form values
	    glBatchForm.setGlBatch(new GlBatchBean(batch));
	    glBatchForm.setJournalEntryIndex(glBatchDAO.getJournalEntryIndex(batchId, journalEntryId));
	    glBatchForm.setBatchDebit(glBatchDAO.getTotalDebitFromJEs(batchId, journalEntryId).toString());
	    glBatchForm.setBatchCredit(glBatchDAO.getTotalCreditFromJEs(batchId, journalEntryId).toString());
	    glBatchForm.setJournalEntry(new JournalEntryBean(journalEntry));
	    glBatchForm.setBatchIndex(glBatchDAO.getBatchIndex(batchId));
	    if ((!this.errors.isEmpty()) && (this.errors.size() == 1)) {
		request.setAttribute("error", "GL Account - " + this.errors.toString() + " is not found in System and got skipped.");
	    } else if (!this.errors.isEmpty()) {
		request.setAttribute("error", "GL Accounts - " + this.errors.toString() + " are not found in System and got skipped.");
	    }
	    //Close input stream
	    if (null != inputStream) {
		inputStream.close();
	    }
	} catch (Exception e) {
	    request.setAttribute("error", "Upload failed - " + e.getMessage());
	    //Close input stream
	    if (null != inputStream) {
		inputStream.close();
	    }
	} finally {
	    //Close input stream
	    if (null != inputStream) {
		inputStream.close();
	    }
	}
    }

    /**
     * getDescription - get description for journal entry from first cell of first row of sheet
     * @param row
     * @return 
     */
    private String getDescription(Row row) {
	Cell cell = row.getCell(0);
	return cell.getStringCellValue();
    }

    /**
     * getPeriod - get period for journal entry from second cell of first row of sheet
     * @param row
     * @return 
     */
    private String getPeriod(Row row) {
	Cell cell = row.getCell(1);
	return "" + (int) cell.getNumericCellValue();
    }

    /**
     * createBatch - create batch based on description,period and subledger code
     * @param description
     * @param period
     * @param subledgerCode
     * @return 
     */
    private Batch createBatch(String description, String period, GenericCode subledgerCode) throws Exception {
	Batch batch = new Batch();
	batch.setStatus("open");
	batch.setType("manual");
	batch.setBatchDesc(null == description && description.isEmpty() ? "Journal entry uploaded for " + period : description);
	batch.setSourceLedger(subledgerCode);
	batch.setTotalCredit(Double.valueOf(0d));
	batch.setTotalDebit(Double.valueOf(0d));
	//Create batch and return from database
	return new GlBatchDAO().saveAndReturn(batch);
    }

    /**
     * createJournalEntry - create journal entry with batch id,description, period and subledger code
     * @param batchId
     * @param journalEntryId
     * @param description
     * @param period
     * @param subledgerCode
     * @return 
     */
    private JournalEntry createJournalEntry(Integer batchId, String journalEntryId, String description, String period, GenericCode subledgerCode) {
	JournalEntry journalEntry = new JournalEntry();
	journalEntry.setBatchId(batchId);
	journalEntry.setJournalEntryId(null != journalEntryId ? journalEntryId : batchId + "-001");
	journalEntry.setJournalEntryDesc((null == description && description.isEmpty() ? "Journal entry uploaded for " + period : description));
	journalEntry.setPeriod(period);
	journalEntry.setJeDate(new Date());
	journalEntry.setSourceCode(subledgerCode);
	journalEntry.setSourceCodeDesc(subledgerCode.getCodedesc());
	journalEntry.setMemo("");
	journalEntry.setDebit(Double.valueOf(0d));
	journalEntry.setCredit(Double.valueOf(0d));
	journalEntry.setSubledgerClose("N");
	return journalEntry;
    }

    /**
     * createLineItems - create line items from sheet with journal entryid,
     * @param sheet
     * @param journalEntryId
     * @param currentPeriod
     * @param glBatchForm
     * @return
     * @throws Exception 
     */
    private List<LineItem> createLineItems(Sheet sheet, String journalEntryId, GlBatchForm glBatchForm) throws Exception {
	List<LineItem> lineItems = new ArrayList();
	AccountDetailsDAO accountDetailsDAO = new AccountDetailsDAO();
	Integer suffix = Integer.valueOf(1);
	StringBuilder lineItemId = null;
	String account = null;
	String description = null;
	Double debit = Double.valueOf(0d);
	Double credit = Double.valueOf(0d);
	Double balance = Double.valueOf(0d);
	glBatchForm.setLineItems(new ArrayList<LineItemBean>());
	//Start reading line items from sheet on fourth row
	for (int rowIndex = 3; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
	    Row row = sheet.getRow(rowIndex);
	    if ((null != row.getCell(0)) && (row.getLastCellNum() >= 3) && (!row.getCell(0).getStringCellValue().trim().equals(""))) {
		account = getString(row.getCell(0));
		description = getString(row.getCell(1));
		debit = getDouble(row.getCell(2));
		credit = getDouble(row.getCell(3));
		balance = Double.valueOf(debit.doubleValue() - credit.doubleValue());
		//Get account details from database to validate
		AccountDetails accountDetails = accountDetailsDAO.findById(account);
		if (null == accountDetails) {
		    //Add the not available account in errors set and skip in creating line item
		    this.errors.add(account);
		} else if (CommonUtils.isNotEmpty(balance)) {
		    //Create line item and set values if balance is not empty
		    LineItem lineItem = new LineItem();
		    lineItemId = new StringBuilder();
		    lineItemId.append(journalEntryId).append("-").append(StringUtils.leftPad(suffix.toString(), 3, "0"));
		    lineItem.setJournalEntryId(journalEntryId);
		    lineItem.setLineItemId(lineItemId.toString());
		    lineItem.setAccount(account);
		    lineItem.setAccountDesc(description);
		    lineItem.setCurrency("USD");
		    lineItem.setReference("");
		    lineItem.setReferenceDesc("");
		    lineItem.setDate(new Date());
		    if (balance.doubleValue() < 0d) {
			lineItem.setDebit(Double.valueOf(0d));
			lineItem.setCredit(Double.valueOf(-balance.doubleValue()));
			this.totalCredit = Double.valueOf(this.totalCredit.doubleValue() + -balance.doubleValue());
		    } else {
			lineItem.setDebit(balance);
			lineItem.setCredit(Double.valueOf(0d));
			this.totalDebit = Double.valueOf(this.totalDebit.doubleValue() + balance.doubleValue());
		    }
		    lineItems.add(lineItem);
		    glBatchForm.getLineItems().add(new LineItemBean(lineItem));
		    suffix++;
		}
	    }
	}
	return lineItems;
    }

    /**
     * Get string value from cell
     * @param cell
     * @return 
     */
    private String getString(Cell cell) {
	return null == cell ? "" : cell.getStringCellValue().trim();
    }

    /**
     * Get double value from cell
     * @param cell
     * @return 
     */
    private Double getDouble(Cell cell) {
	return Double.valueOf(null != cell ? cell.getNumericCellValue() : 0d);
    }
}
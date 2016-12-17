package com.logiware.utils;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.AccountDetails;
import com.gp.cvst.logisoft.domain.Batch;
import com.gp.cvst.logisoft.domain.FiscalPeriod;
import com.gp.cvst.logisoft.domain.JournalEntry;
import com.gp.cvst.logisoft.domain.LineItem;
import com.gp.cvst.logisoft.hibernate.dao.AccountDetailsDAO;
import com.gp.cvst.logisoft.hibernate.dao.FiscalPeriodDAO;
import com.logiware.bean.GlBatchBean;
import com.logiware.bean.JournalEntryBean;
import com.logiware.bean.LineItemBean;
import com.logiware.excel.GlBatchExcelCreator;
import com.logiware.excel.JournalEntryExcelCreator;
import com.logiware.excel.SubledgerHistoryExcelCreator;
import com.logiware.form.GlBatchForm;
import com.logiware.form.ReconcileForm;
import com.logiware.hibernate.dao.GlBatchDAO;
import com.logiware.reports.GlBatchReportCreator;
import com.logiware.reports.JournalEntryReportCreator;
import com.logiware.reports.SubledgerHistoryReportCreator;
import com.oreilly.servlet.ServletUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * GlBatchUtils.java - utility class used for create, edit, update, reverse, copy, auto-reverse, post, delete the batch and its journal entries
 * @since Sep-15-2011
 * @version 1.0.0
 * @author Lakshmi Narayanan
 */
public class GlBatchUtils {

    private static String REVERSE_FLAG = "R";
    private static String AUTO_REVERSE_FLAG = "A";

    /**
     * addBatch - Create batch and add it to batch table
     * @param glBatchForm
     * @param request
     * @throws Exception 
     */
    public static void addBatch(GlBatchForm glBatchForm, HttpServletRequest request) throws Exception {
	GlBatchDAO glBatchDAO = new GlBatchDAO();
	//Create batch and return from database
	Batch batch = glBatchDAO.saveAndReturn(new Batch());
	Integer batchId = batch.getBatchId();
	//Form journal id
	String journalEntryId = batchId + "-001";
	//Get GL-JE subledger from database
	GenericCode subledgerCode = new GenericCodeDAO().getGenericCodeByCode("GL-JE");
	//Get current open period
	FiscalPeriod fiscalPeriod = new FiscalPeriodDAO().getCurrentOpenPeriod();
	//Create journal entry and set values
	JournalEntry journalEntry = new JournalEntry();
	journalEntry.setBatchId(batchId);
	journalEntry.setJournalEntryId(journalEntryId);
	journalEntry.setDebit(Double.valueOf(0.0D));
	journalEntry.setCredit(Double.valueOf(0.0D));
	journalEntry.setSourceCode(subledgerCode);
	journalEntry.setSourceCodeDesc(subledgerCode.getCodedesc());
	journalEntry.setPeriod(fiscalPeriod.getPeriodDis());
	//Create new journal entry set and add journal entry to it
	batch.setJournalEntrySet(new HashSet());
	batch.getJournalEntrySet().add(journalEntry);
	//Set subledger 
	batch.setSourceLedger(subledgerCode);
	//Update batch to database
	glBatchDAO.update(batch);
	//Set form values
	glBatchForm.setBatchIndex(glBatchDAO.getBatchIndex(batchId));
	glBatchForm.setJournalEntryIndex(glBatchDAO.getJournalEntryIndex(batchId, journalEntryId));
	glBatchForm.setBatchDebit(glBatchDAO.getTotalDebitFromJEs(batchId, journalEntryId).toString());
	glBatchForm.setBatchCredit(glBatchDAO.getTotalCreditFromJEs(batchId, journalEntryId).toString());
	glBatchForm.setGlBatch(new GlBatchBean(batch));
	List<LineItemBean> lineItems = new ArrayList();
	for (int i = 1; i <= 10; i++) {
	    lineItems.add(new LineItemBean(Integer.valueOf(i), journalEntryId));
	}
	glBatchForm.setJournalEntry(new JournalEntryBean(journalEntry));
	glBatchForm.setLineItems(lineItems);
	request.setAttribute("message", "Batch - " + batchId + " created successfully");
    }

    /**
     * editBatch - Edit batch based on batch id
     * @param glBatchForm
     * @param request
     * @throws Exception 
     */
    public static void editBatch(GlBatchForm glBatchForm, HttpServletRequest request) throws Exception {
	GlBatchDAO glBatchDAO = new GlBatchDAO();
	//Get batch id from form
	Integer batchId = Integer.valueOf(Integer.parseInt(glBatchForm.getBatchId()));
	//Get batch from database
	Batch batch = glBatchDAO.findById(batchId);
	//Set form values
	glBatchForm.setBatchIndex(glBatchDAO.getBatchIndex(batchId));
	glBatchForm.setGlBatch(new GlBatchBean(batch));
	JournalEntry journalEntry = glBatchDAO.getJournalEntry(batchId, glBatchForm.getJournalEntryIndex());
	if (null != journalEntry) {
	    String journalEntryId = journalEntry.getJournalEntryId();
	    glBatchForm.setJournalEntryIndex(glBatchDAO.getJournalEntryIndex(batchId, journalEntryId));
	    glBatchForm.setBatchDebit(glBatchDAO.getTotalDebitFromJEs(batchId, journalEntryId).toString());
	    glBatchForm.setBatchCredit(glBatchDAO.getTotalCreditFromJEs(batchId, journalEntryId).toString());
	    glBatchForm.setJournalEntry(new JournalEntryBean(journalEntry));
	    List<LineItem> lineItems = glBatchDAO.getLineItems(journalEntryId);
	    if (CommonUtils.isNotEmpty(lineItems)) {
		for (LineItem lineItem : lineItems) {
		    glBatchForm.getLineItems().add(new LineItemBean(lineItem));
		}
	    } else {
		for (int i = 1; i <= 10; i++) {
		    glBatchForm.getLineItems().add(new LineItemBean(Integer.valueOf(i), journalEntryId));
		}
	    }
	} else {
	    glBatchForm.setJournalEntry(new JournalEntryBean());
	    request.setAttribute("error", "No Journal Entries found");
	}
    }

    /**
     *gotoBatch -  Go to batch based on index
     * @param glBatchForm
     * @param request
     * @throws Exception 
     */
    public static void gotoBatch(GlBatchForm glBatchForm, HttpServletRequest request) throws Exception {
	GlBatchDAO glBatchDAO = new GlBatchDAO();
	//Get batch from form by index
	Batch batch = glBatchDAO.getBatchByIndex(glBatchForm.getBatchIndex());
	if (null != batch) {
	    //Set form values
	    Integer batchId = batch.getBatchId();
	    glBatchForm.setBatchIndex(glBatchDAO.getBatchIndex(batchId));
	    glBatchForm.setGlBatch(new GlBatchBean(batch));
	    JournalEntry journalEntry = glBatchDAO.getJournalEntry(batchId, glBatchForm.getJournalEntryIndex());
	    if (null != journalEntry) {
		String journalEntryId = journalEntry.getJournalEntryId();
		glBatchForm.setJournalEntryIndex(glBatchDAO.getJournalEntryIndex(batchId, journalEntryId));
		glBatchForm.setBatchDebit(glBatchDAO.getTotalDebitFromJEs(batchId, journalEntryId).toString());
		glBatchForm.setBatchCredit(glBatchDAO.getTotalCreditFromJEs(batchId, journalEntryId).toString());
		glBatchForm.setJournalEntry(new JournalEntryBean(journalEntry));
		List<LineItem> lineItems = glBatchDAO.getLineItems(journalEntryId);
		if (CommonUtils.isNotEmpty(lineItems)) {
		    for (LineItem lineItem : lineItems) {
			glBatchForm.getLineItems().add(new LineItemBean(lineItem));
		    }
		} else {
		    for (int i = 1; i <= 10; i++) {
			glBatchForm.getLineItems().add(new LineItemBean(Integer.valueOf(i), journalEntryId));
		    }
		}
	    } else {
		glBatchForm.setJournalEntry(new JournalEntryBean());
		request.setAttribute("error", "No more Journal Entries found");
	    }
	} else {
	    glBatchForm.setGlBatch(new GlBatchBean());
	    request.setAttribute("error", "No more Batches found");
	}
    }

    /**
     * gotoJournalEntry - Go to journal entry based on index
     * @param glBatchForm
     * @param request
     * @throws Exception 
     */
    public static void gotoJournalEntry(GlBatchForm glBatchForm, HttpServletRequest request) throws Exception {
	GlBatchDAO glBatchDAO = new GlBatchDAO();
	if (CommonUtils.isNotEmpty(glBatchForm.getGlBatch().getId())) {
	    //Get batch from database
	    Integer batchId = Integer.valueOf(Integer.parseInt(glBatchForm.getGlBatch().getId()));
	    Batch batch = glBatchDAO.findById(batchId);
	    glBatchForm.setBatchIndex(glBatchDAO.getBatchIndex(batchId));
	    glBatchForm.setGlBatch(new GlBatchBean(batch));
	    //Get journal entry from database by index
	    JournalEntry journalEntry = glBatchDAO.getJournalEntry(batchId, glBatchForm.getJournalEntryIndex());
	    if (null != journalEntry) {
		//Set form values
		String journalEntryId = journalEntry.getJournalEntryId();
		glBatchForm.setJournalEntryIndex(glBatchDAO.getJournalEntryIndex(batchId, journalEntryId));
		glBatchForm.setBatchDebit(glBatchDAO.getTotalDebitFromJEs(batchId, journalEntryId).toString());
		glBatchForm.setBatchCredit(glBatchDAO.getTotalCreditFromJEs(batchId, journalEntryId).toString());
		glBatchForm.setJournalEntry(new JournalEntryBean(journalEntry));
		List<LineItem> lineItems = glBatchDAO.getLineItems(journalEntryId);
		if (CommonUtils.isNotEmpty(lineItems)) {
		    for (LineItem lineItem : lineItems) {
			glBatchForm.getLineItems().add(new LineItemBean(lineItem));
		    }
		} else {
		    for (int i = 1; i <= 10; i++) {
			glBatchForm.getLineItems().add(new LineItemBean(Integer.valueOf(i), journalEntryId));
		    }
		}
	    } else {
		glBatchForm.setJournalEntry(new JournalEntryBean());
		request.setAttribute("error", "No more Journal Entries found");
	    }
	} else {
	    //Get journal entry from database by index
	    JournalEntry journalEntry = glBatchDAO.getJournalEntry(glBatchForm.getJournalEntry().getId());
	    Integer batchId = journalEntry.getBatchId();
	    Batch batch = glBatchDAO.findById(batchId);
	    glBatchForm.setBatchIndex(glBatchDAO.getBatchIndex(batchId));
	    glBatchForm.setGlBatch(new GlBatchBean(batch));
	    String journalEntryId = journalEntry.getJournalEntryId();
	    glBatchForm.setJournalEntryIndex(glBatchDAO.getJournalEntryIndex(batchId, journalEntryId));
	    glBatchForm.setBatchDebit(glBatchDAO.getTotalDebitFromJEs(batchId, journalEntryId).toString());
	    glBatchForm.setBatchCredit(glBatchDAO.getTotalCreditFromJEs(batchId, journalEntryId).toString());
	    glBatchForm.setJournalEntry(new JournalEntryBean(journalEntry));
	    List<LineItem> lineItems = glBatchDAO.getLineItems(journalEntryId);
	    for (LineItem lineItem : lineItems) {
		glBatchForm.getLineItems().add(new LineItemBean(lineItem));
	    }
	    request.setAttribute("from", "Transction History");
	}
    }

    /**
     * saveOrUpdate - Save or update batch, it's journal entry and line items
     * @param glBatchForm
     * @param request
     * @throws Exception 
     */
    public static void saveOrUpdate(GlBatchForm glBatchForm, HttpServletRequest request) throws Exception {
        System.out.println(request.getParameterMap().toString());
	GlBatchDAO glBatchDAO = new GlBatchDAO();
	//Get batch from form
	GlBatchBean glBatch = glBatchForm.getGlBatch();
	Integer batchId = Integer.valueOf(Integer.parseInt(glBatch.getId()));
	//Get journal entry from form
	JournalEntryBean journalEntryBean = glBatchForm.getJournalEntry();
	String journalEntryId = journalEntryBean.getId();
	String subledgerId = journalEntryBean.getSubledgerType();
	//Get subledger from database
	GenericCode subledgerCode = new GenericCodeDAO().findById(Integer.valueOf(Integer.parseInt(subledgerId)));
	//Get line items from form
	List<LineItemBean> lineItems = glBatchForm.getLineItems();
	List lineItemIds = new ArrayList();
	if (CommonUtils.isNotEmpty(lineItems)) {
	    for (LineItemBean lineItemBean : lineItems) {
		lineItemIds.add("'" + lineItemBean.getId() + "'");
		//Get line item from database
		LineItem lineItem = glBatchDAO.getLineItem(lineItemBean.getId());
		if (null != lineItem) {
		    //Update line item into database
		    lineItem.setAccount(lineItemBean.getAccount());
		    lineItem.setAccountDesc(lineItemBean.getAccountDescription());
		    lineItem.setDebit(Double.valueOf(Double.parseDouble(lineItemBean.getDebit())));
		    lineItem.setCredit(Double.valueOf(Double.parseDouble(lineItemBean.getCredit())));
		    lineItem.setReference(lineItemBean.getReference());
		    lineItem.setReferenceDesc(lineItemBean.getReferenceDescription());
		    lineItem.setCurrency(lineItemBean.getCurrency());
		} else {
		    //Save line item into database
		    lineItem = new LineItem();
		    lineItem.setLineItemId(lineItemBean.getId());
		    lineItem.setJournalEntryId(journalEntryId);
		    lineItem.setAccount(lineItemBean.getAccount());
		    lineItem.setAccountDesc(lineItemBean.getAccountDescription());
		    lineItem.setDebit(Double.valueOf(Double.parseDouble(lineItemBean.getDebit())));
		    lineItem.setCredit(Double.valueOf(Double.parseDouble(lineItemBean.getCredit())));
		    lineItem.setReference(lineItemBean.getReference());
		    lineItem.setReferenceDesc(lineItemBean.getReferenceDescription());
		    lineItem.setCurrency(lineItemBean.getCurrency());
		    lineItem.setDate(new Date());
		    glBatchDAO.save(lineItem);
		}
	    }
	}
	//Delete line item from database
	glBatchDAO.deleteLineItems(journalEntryId, lineItemIds);
	//Get journal entry from database and set values
	JournalEntry journalEntry = glBatchDAO.getJournalEntry(journalEntryId);
	journalEntry.setJournalEntryDesc(journalEntryBean.getDescription());
	journalEntry.setPeriod(journalEntryBean.getPeriod());
	journalEntry.setSourceCode(subledgerCode);
	journalEntry.setSourceCodeDesc(subledgerCode.getCodedesc());
	journalEntry.setDebit(glBatchDAO.getTotalDebitFromLineItems(journalEntryId));
	journalEntry.setCredit(glBatchDAO.getTotalCreditFromLineItems(journalEntryId));
	journalEntry.setMemo(journalEntryBean.getMemo());
	glBatchForm.setJournalEntry(new JournalEntryBean(journalEntry));
	//Get batch from database and set values
	Batch batch = glBatchDAO.findById(batchId);
	batch.setBatchDesc(glBatch.getDescription());
	batch.setTotalDebit(glBatchDAO.getTotalDebitFromJEs(batchId));
	batch.setTotalCredit(glBatchDAO.getTotalCreditFromJEs(batchId));
	batch.setSourceLedger(subledgerCode);
	glBatchForm.setGlBatch(new GlBatchBean(batch));
	glBatchForm.setBatchDebit(glBatchDAO.getTotalDebitFromJEs(batchId, journalEntryId).toString());
	glBatchForm.setBatchCredit(glBatchDAO.getTotalCreditFromJEs(batchId, journalEntryId).toString());
	request.setAttribute("message", "Journal Entry - " + journalEntryId + " of GL Batch - " + batchId + " updated successfully");
    }

    /**
     * autoReverseBatch - Auto reverse the journal entry from batch
     * @param glBatchForm
     * @param request
     * @throws Exception 
     */
    public static void autoReverseBatch(GlBatchForm glBatchForm, HttpServletRequest request) throws Exception {
	GlBatchDAO glBatchDAO = new GlBatchDAO();
	//Get batch from form
	GlBatchBean glBatch = glBatchForm.getGlBatch();
	Integer batchId = Integer.valueOf(Integer.parseInt(glBatch.getId()));
	//Get journal entry form form
	JournalEntryBean journalEntryBean = glBatchForm.getJournalEntry();
	String journalEntryId = journalEntryBean.getId();

	String autoReverseJournalEntryId = journalEntryBean.getId() + "R";
	String subledgerId = journalEntryBean.getSubledgerType();
	//Get subledger from database
	GenericCode subledgerCode = new GenericCodeDAO().findById(Integer.valueOf(Integer.parseInt(subledgerId)));
	//Get line items from batch
	List<LineItemBean> lineItems = glBatchForm.getLineItems();
	List autoReverseLineItems = new ArrayList();
	List lineItemIds = new ArrayList();
	Integer suffix = 1;
	if (CommonUtils.isNotEmpty(lineItems)) {
	    for (LineItemBean lineItemBean : lineItems) {
		lineItemIds.add("'" + lineItemBean.getId() + "'");
		//Get line item from database
		LineItem lineItem = glBatchDAO.getLineItem(lineItemBean.getId());
		if (null != lineItem) {
		    //Update line item
		    lineItem.setAccount(lineItemBean.getAccount());
		    lineItem.setAccountDesc(lineItemBean.getAccountDescription());
		    lineItem.setDebit(Double.valueOf(Double.parseDouble(lineItemBean.getDebit())));
		    lineItem.setCredit(Double.valueOf(Double.parseDouble(lineItemBean.getCredit())));
		    lineItem.setReference(lineItemBean.getReference());
		    lineItem.setReferenceDesc(lineItemBean.getReferenceDescription());
		    lineItem.setCurrency(lineItemBean.getCurrency());
		} else {
		    //Save line item
		    lineItem = new LineItem();
		    lineItem.setLineItemId(lineItemBean.getId());
		    lineItem.setJournalEntryId(journalEntryId);
		    lineItem.setAccount(lineItemBean.getAccount());
		    lineItem.setAccountDesc(lineItemBean.getAccountDescription());
		    lineItem.setDebit(Double.valueOf(Double.parseDouble(lineItemBean.getDebit())));
		    lineItem.setCredit(Double.valueOf(Double.parseDouble(lineItemBean.getCredit())));
		    lineItem.setReference(lineItemBean.getReference());
		    lineItem.setReferenceDesc(lineItemBean.getReferenceDescription());
		    lineItem.setCurrency(lineItemBean.getCurrency());
		    lineItem.setDate(new Date());
		    glBatchDAO.save(lineItem);
		}
		//Create auto reverse line item and save into database
		StringBuilder autoReverseLineItemId = new StringBuilder();
		autoReverseLineItemId.append(autoReverseJournalEntryId).append("-").append(StringUtils.leftPad(suffix.toString(), 3, "0"));
		LineItem autoReverseLineItem = new LineItem();
		autoReverseLineItem.setLineItemId(autoReverseLineItemId.toString());
		autoReverseLineItem.setJournalEntryId(autoReverseJournalEntryId);
		autoReverseLineItem.setAccount(lineItemBean.getAccount());
		autoReverseLineItem.setAccountDesc(lineItemBean.getAccountDescription());
		autoReverseLineItem.setDebit(Double.valueOf(Double.parseDouble(lineItemBean.getCredit())));
		autoReverseLineItem.setCredit(Double.valueOf(Double.parseDouble(lineItemBean.getDebit())));
		autoReverseLineItem.setReference(lineItemBean.getReference());
		autoReverseLineItem.setReferenceDesc(lineItemBean.getReferenceDescription());
		autoReverseLineItem.setCurrency(lineItemBean.getCurrency());
		autoReverseLineItem.setDate(new Date());
		glBatchDAO.save(autoReverseLineItem);
		autoReverseLineItems.add(new LineItemBean(autoReverseLineItem));
		suffix++;
	    }
	}
	glBatchForm.setLineItems(autoReverseLineItems);
	//Delete line items from database
	glBatchDAO.deleteLineItems(journalEntryId, lineItemIds);
	//Get journal entry from database and set values
	JournalEntry journalEntry = glBatchDAO.getJournalEntry(journalEntryId);
	journalEntry.setJournalEntryDesc(journalEntryBean.getDescription());
	journalEntry.setPeriod(journalEntryBean.getPeriod());
	journalEntry.setSourceCode(subledgerCode);
	journalEntry.setSourceCodeDesc(subledgerCode.getCodedesc());
	journalEntry.setDebit(glBatchDAO.getTotalDebitFromLineItems(journalEntryId));
	journalEntry.setCredit(glBatchDAO.getTotalCreditFromLineItems(journalEntryId));
	journalEntry.setMemo(journalEntryBean.getMemo());
	journalEntry.setFlag(AUTO_REVERSE_FLAG);
	//Create auto reverse journal entry, set values and save it into database
	JournalEntry autoReverseJournalEntry = new JournalEntry();
	autoReverseJournalEntry.setBatchId(batchId);
	autoReverseJournalEntry.setJournalEntryId(autoReverseJournalEntryId);
	autoReverseJournalEntry.setJournalEntryDesc("Auto Reverse of " + journalEntryId);
	//Get next open period
	FiscalPeriod fiscalPeriod = new FiscalPeriodDAO().getNextOpenPeriod(journalEntryBean.getPeriod());
	if (null == fiscalPeriod) {
	    //Get current open period
	    fiscalPeriod = new FiscalPeriodDAO().getCurrentOpenPeriod();
	}
	autoReverseJournalEntry.setPeriod(fiscalPeriod.getPeriodDis());
	autoReverseJournalEntry.setSourceCode(subledgerCode);
	autoReverseJournalEntry.setSourceCodeDesc(subledgerCode.getCodedesc());
	autoReverseJournalEntry.setDebit(glBatchDAO.getTotalDebitFromLineItems(autoReverseJournalEntryId));
	autoReverseJournalEntry.setCredit(glBatchDAO.getTotalCreditFromLineItems(autoReverseJournalEntryId));
	autoReverseJournalEntry.setMemo(journalEntryBean.getMemo());
	glBatchDAO.save(autoReverseJournalEntry);
	glBatchForm.setJournalEntry(new JournalEntryBean(autoReverseJournalEntry));
	//Get batch from database and set values
	Batch batch = glBatchDAO.findById(batchId);
	batch.setBatchDesc(glBatch.getDescription());
	batch.setTotalDebit(glBatchDAO.getTotalDebitFromJEs(batchId));
	batch.setTotalCredit(glBatchDAO.getTotalCreditFromJEs(batchId));
	batch.setSourceLedger(subledgerCode);
	//Set form values
	glBatchForm.setGlBatch(new GlBatchBean(batch));
	glBatchForm.setBatchIndex(glBatchDAO.getBatchIndex(batchId));
	glBatchForm.setJournalEntryIndex(glBatchDAO.getJournalEntryIndex(batchId, journalEntryId));
	glBatchForm.setBatchDebit(glBatchDAO.getTotalDebitFromJEs(batchId, autoReverseJournalEntryId).toString());
	glBatchForm.setBatchCredit(glBatchDAO.getTotalCreditFromJEs(batchId, autoReverseJournalEntryId).toString());
	request.setAttribute("message", "Auto Reverse of " + journalEntryId + " done successfully");
    }

    /**
     * reverseBatch - Reverse the journal entry from  batch
     * @param glBatchForm
     * @param request
     * @throws Exception 
     */
    public static void reverseBatch(GlBatchForm glBatchForm, HttpServletRequest request) throws Exception {
	GlBatchDAO glBatchDAO = new GlBatchDAO();
	//Get batch from form
	GlBatchBean glBatch = glBatchForm.getGlBatch();
	String reverseGlBatchId = glBatchForm.getNewGlBatchId();
	Batch reverseBatch = null;
	String reverseJournalEntryId = null;
	Integer reverseBatchId = null;
	if (CommonUtils.isEmpty(reverseGlBatchId)) {
	    //Create reverse batch and return from database
	    reverseBatch = glBatchDAO.saveAndReturn(new Batch());
	    reverseBatchId = reverseBatch.getBatchId();
	    reverseJournalEntryId = reverseBatchId + "-001";
	} else {
	    reverseBatchId = Integer.valueOf(Integer.parseInt(reverseGlBatchId));
	    //Get old batch from database for reverse
	    reverseBatch = glBatchDAO.findById(reverseBatchId);
	    Integer suffix = glBatchDAO.getLastJournalEntrySuffix(reverseBatchId);
	    suffix++;
	    reverseJournalEntryId = reverseBatchId + "-" + StringUtils.leftPad(suffix.toString(), 3, "0");
	}
	//Get journal entry from database
	JournalEntryBean journalEntryBean = glBatchForm.getJournalEntry();
	String journalEntryId = journalEntryBean.getId();
	//Create reverse journal entry
	JournalEntry reverseJournalEntry = new JournalEntry();
	reverseJournalEntry.setBatchId(reverseBatchId);
	reverseJournalEntry.setJournalEntryId(reverseJournalEntryId);
	reverseJournalEntry.setJournalEntryDesc("Reverse of " + journalEntryId);
	reverseJournalEntry.setDebit(Double.valueOf(0.0D));
	reverseJournalEntry.setCredit(Double.valueOf(0.0D));
	//Get subledger from database
	String subledgerId = journalEntryBean.getSubledgerType();
	GenericCode subledgerCode = new GenericCodeDAO().findById(Integer.valueOf(Integer.parseInt(subledgerId)));
	//Get line items from database
	List<LineItem> lineItems = glBatchDAO.getLineItems(journalEntryId);
	List reverseLineItems = new ArrayList();
	Integer suffix = 1;
	if (CommonUtils.isNotEmpty(lineItems)) {
	    for (LineItem lineItem : lineItems) {
		//Create reverse line item and save it into database
		LineItem reverseLineItem = new LineItem();
		StringBuilder reverseLineItemId = new StringBuilder();
		reverseLineItemId.append(reverseJournalEntryId).append("-").append(StringUtils.leftPad(suffix.toString(), 3, "0"));
		reverseLineItem.setLineItemId(reverseLineItemId.toString());
		reverseLineItem.setJournalEntryId(reverseJournalEntryId);
		reverseLineItem.setAccount(lineItem.getAccount());
		reverseLineItem.setAccountDesc(lineItem.getAccountDesc());
		reverseLineItem.setDebit(lineItem.getCredit());
		reverseLineItem.setCredit(lineItem.getDebit());
		reverseLineItem.setReference(lineItem.getReference());
		reverseLineItem.setReferenceDesc(lineItem.getReferenceDesc());
		reverseLineItem.setCurrency(lineItem.getCurrency());
		reverseLineItem.setDate(new Date());
		glBatchDAO.save(reverseLineItem);
		reverseLineItems.add(new LineItemBean(reverseLineItem));
		suffix++;
	    }
	}
	glBatchForm.setLineItems(reverseLineItems);
	//Get journal entry and set reverse flag on it
	JournalEntry journalEntry = glBatchDAO.getJournalEntry(journalEntryId);
	journalEntry.setFlag(REVERSE_FLAG);
	//Get next open period
	FiscalPeriod fiscalPeriod = new FiscalPeriodDAO().getNextOpenPeriod(journalEntryBean.getPeriod());
	if (null == fiscalPeriod) {
	    //Get current open period
	    fiscalPeriod = new FiscalPeriodDAO().getCurrentOpenPeriod();
	}
	//Set reverse journal entry values and save it
	reverseJournalEntry.setPeriod(fiscalPeriod.getPeriodDis());
	reverseJournalEntry.setSourceCode(subledgerCode);
	reverseJournalEntry.setSourceCodeDesc(subledgerCode.getCodedesc());
	reverseJournalEntry.setDebit(glBatchDAO.getTotalDebitFromLineItems(reverseJournalEntryId));
	reverseJournalEntry.setCredit(glBatchDAO.getTotalCreditFromLineItems(reverseJournalEntryId));
	reverseJournalEntry.setMemo(journalEntryBean.getMemo());
	glBatchDAO.save(reverseJournalEntry);
	glBatchForm.setJournalEntry(new JournalEntryBean(reverseJournalEntry));
	//Get reverse batch and set values
	reverseBatch = glBatchDAO.findById(reverseBatchId);
	reverseBatch.setBatchDesc(glBatch.getDescription());
	reverseBatch.setTotalDebit(glBatchDAO.getTotalDebitFromJEs(reverseBatchId));
	reverseBatch.setTotalCredit(glBatchDAO.getTotalCreditFromJEs(reverseBatchId));
	reverseBatch.setSourceLedger(subledgerCode);
	//Set form values
	glBatchForm.setGlBatch(new GlBatchBean(reverseBatch));
	glBatchForm.setBatchIndex(glBatchDAO.getBatchIndex(reverseBatchId));
	glBatchForm.setJournalEntryIndex(glBatchDAO.getJournalEntryIndex(reverseBatchId, reverseJournalEntryId));
	glBatchForm.setBatchDebit(glBatchDAO.getTotalDebitFromJEs(reverseBatchId, reverseJournalEntryId).toString());
	glBatchForm.setBatchCredit(glBatchDAO.getTotalCreditFromJEs(reverseBatchId, reverseJournalEntryId).toString());
	request.setAttribute("message", "Reverse of " + journalEntryId + " done successfully");
    }

    /** 
     * copyOpenBatch - Copy the journal entry from open batch
     * @param glBatchForm
     * @param request
     * @throws Exception 
     */
    public static void copyOpenBatch(GlBatchForm glBatchForm, HttpServletRequest request) throws Exception {
	GlBatchDAO glBatchDAO = new GlBatchDAO();
	//Get journal entry from form
	GlBatchBean glBatch = glBatchForm.getGlBatch();
	Integer batchId = Integer.valueOf(Integer.parseInt(glBatch.getId()));
	String copyGlBatchId = glBatchForm.getNewGlBatchId();
	Batch copyBatch = null;
	String copyJournalEntryId = null;
	Integer copyBatchId = null;
	if (CommonUtils.isEmpty(copyGlBatchId)) {
	    //Create copy batch and return from database
	    copyBatch = glBatchDAO.saveAndReturn(new Batch());
	    copyBatchId = copyBatch.getBatchId();
	    copyJournalEntryId = copyBatchId + "-001";
	} else {
	    //Get copy batch from database
	    copyBatchId = Integer.valueOf(Integer.parseInt(copyGlBatchId));
	    copyBatch = glBatchDAO.findById(copyBatchId);
	    Integer suffix = glBatchDAO.getLastJournalEntrySuffix(copyBatchId);
	    suffix++;
	    copyJournalEntryId = copyBatchId + "-" + StringUtils.leftPad(suffix.toString(), 3, "0");
	}
	//Get journal entry from form
	JournalEntryBean journalEntryBean = glBatchForm.getJournalEntry();
	String journalEntryId = journalEntryBean.getId();
	//Create copy journal entry and set values
	JournalEntry copyJournalEntry = new JournalEntry();
	copyJournalEntry.setBatchId(copyBatchId);
	copyJournalEntry.setJournalEntryId(copyJournalEntryId);
	copyJournalEntry.setJournalEntryDesc("Copy of " + journalEntryId);
	copyJournalEntry.setDebit(Double.valueOf(0.0D));
	copyJournalEntry.setCredit(Double.valueOf(0.0D));
	//Get subledger from database
	String subledgerId = journalEntryBean.getSubledgerType();
	GenericCode subledgerCode = new GenericCodeDAO().findById(Integer.valueOf(Integer.parseInt(subledgerId)));
	//Get line items from form
	List<LineItemBean> lineItems = glBatchForm.getLineItems();
	List copyLineItems = new ArrayList();
	List lineItemIds = new ArrayList();
	Integer suffix = 1;
	if (CommonUtils.isNotEmpty(lineItems)) {
	    for (LineItemBean lineItemBean : lineItems) {
		lineItemIds.add("'" + lineItemBean.getId() + "'");
		//Get line item from database
		LineItem lineItem = glBatchDAO.getLineItem(lineItemBean.getId());
		if (null != lineItem) {
		    //Update line item
		    lineItem.setAccount(lineItemBean.getAccount());
		    lineItem.setAccountDesc(lineItemBean.getAccountDescription());
		    lineItem.setDebit(Double.valueOf(Double.parseDouble(lineItemBean.getDebit())));
		    lineItem.setCredit(Double.valueOf(Double.parseDouble(lineItemBean.getCredit())));
		    lineItem.setReference(lineItemBean.getReference());
		    lineItem.setReferenceDesc(lineItemBean.getReferenceDescription());
		    lineItem.setCurrency(lineItemBean.getCurrency());
		} else {
		    //Create line item
		    lineItem = new LineItem();
		    lineItem.setLineItemId(lineItemBean.getId());
		    lineItem.setJournalEntryId(journalEntryId);
		    lineItem.setAccount(lineItemBean.getAccount());
		    lineItem.setAccountDesc(lineItemBean.getAccountDescription());
		    lineItem.setDebit(Double.valueOf(Double.parseDouble(lineItemBean.getDebit())));
		    lineItem.setCredit(Double.valueOf(Double.parseDouble(lineItemBean.getCredit())));
		    lineItem.setReference(lineItemBean.getReference());
		    lineItem.setReferenceDesc(lineItemBean.getReferenceDescription());
		    lineItem.setCurrency(lineItemBean.getCurrency());
		    lineItem.setDate(new Date());
		    glBatchDAO.save(lineItem);
		}
		//Create copy line item and set values
		LineItem copyLineItem = new LineItem();
		StringBuilder copyLineItemId = new StringBuilder();
		copyLineItemId.append(copyJournalEntryId).append("-").append(StringUtils.leftPad(suffix.toString(), 3, "0"));
		copyLineItem.setLineItemId(copyLineItemId.toString());
		copyLineItem.setJournalEntryId(copyJournalEntryId);
		copyLineItem.setAccount(lineItemBean.getAccount());
		copyLineItem.setAccountDesc(lineItemBean.getAccountDescription());
		copyLineItem.setDebit(Double.valueOf(Double.parseDouble(lineItemBean.getDebit())));
		copyLineItem.setCredit(Double.valueOf(Double.parseDouble(lineItemBean.getCredit())));
		copyLineItem.setReference(lineItemBean.getReference());
		copyLineItem.setReferenceDesc(lineItemBean.getReferenceDescription());
		copyLineItem.setCurrency(lineItemBean.getCurrency());
		copyLineItem.setDate(new Date());
		glBatchDAO.save(copyLineItem);
		copyLineItems.add(new LineItemBean(copyLineItem));
		suffix++;
	    }
	}
	glBatchForm.setLineItems(copyLineItems);
	//Delete line items from database
	glBatchDAO.deleteLineItems(journalEntryId, lineItemIds);
	//Get journal entry and set values
	JournalEntry journalEntry = glBatchDAO.getJournalEntry(journalEntryId);
	journalEntry.setJournalEntryDesc(journalEntryBean.getDescription());
	journalEntry.setPeriod(journalEntryBean.getPeriod());
	journalEntry.setSourceCode(subledgerCode);
	journalEntry.setSourceCodeDesc(subledgerCode.getCodedesc());
	journalEntry.setDebit(glBatchDAO.getTotalDebitFromLineItems(journalEntryId));
	journalEntry.setCredit(glBatchDAO.getTotalCreditFromLineItems(journalEntryId));
	journalEntry.setMemo(journalEntryBean.getMemo());
	//Get next open period
	FiscalPeriod fiscalPeriod = new FiscalPeriodDAO().getNextOpenPeriod(journalEntryBean.getPeriod());
	if (null == fiscalPeriod) {
	    //Get current open period
	    fiscalPeriod = new FiscalPeriodDAO().getCurrentOpenPeriod();
	}
	//Set copy journal entry values and save it into database
	copyJournalEntry.setPeriod(fiscalPeriod.getPeriodDis());
	copyJournalEntry.setSourceCode(subledgerCode);
	copyJournalEntry.setSourceCodeDesc(subledgerCode.getCodedesc());
	copyJournalEntry.setDebit(glBatchDAO.getTotalDebitFromLineItems(copyJournalEntryId));
	copyJournalEntry.setCredit(glBatchDAO.getTotalCreditFromLineItems(copyJournalEntryId));
	copyJournalEntry.setMemo(journalEntryBean.getMemo());
	glBatchDAO.save(copyJournalEntry);
	glBatchForm.setJournalEntry(new JournalEntryBean(copyJournalEntry));
	//Get batch from database and set values
	Batch batch = glBatchDAO.findById(batchId);
	batch.setBatchDesc(glBatch.getDescription());
	batch.setTotalDebit(glBatchDAO.getTotalDebitFromJEs(batchId));
	batch.setTotalCredit(glBatchDAO.getTotalCreditFromJEs(batchId));
	batch.setSourceLedger(subledgerCode);
	//Get copy batch from database and set values
	copyBatch = glBatchDAO.findById(copyBatchId);
	copyBatch.setBatchDesc(glBatch.getDescription());
	copyBatch.setTotalDebit(glBatchDAO.getTotalDebitFromJEs(copyBatchId));
	copyBatch.setTotalCredit(glBatchDAO.getTotalCreditFromJEs(copyBatchId));
	copyBatch.setSourceLedger(subledgerCode);
	//Set form values
	glBatchForm.setGlBatch(new GlBatchBean(copyBatch));
	glBatchForm.setBatchDebit(glBatchDAO.getTotalDebitFromJEs(copyBatchId, copyJournalEntryId).toString());
	glBatchForm.setBatchCredit(glBatchDAO.getTotalCreditFromJEs(copyBatchId, copyJournalEntryId).toString());
	request.setAttribute("message", "Copy of " + journalEntryId + " done successfully");
    }

    /**
     * copyPostedBatch - Copy the journal entry from already posted batch
     * @param glBatchForm
     * @param request
     * @throws Exception 
     */
    public static void copyPostedBatch(GlBatchForm glBatchForm, HttpServletRequest request) throws Exception {
	GlBatchDAO glBatchDAO = new GlBatchDAO();
	//Get batch from form
	GlBatchBean glBatch = glBatchForm.getGlBatch();
	String copyGlBatchId = glBatchForm.getNewGlBatchId();
	Batch copyBatch = null;
	String copyJournalEntryId = null;
	Integer copyBatchId = null;
	if (CommonUtils.isEmpty(copyGlBatchId)) {
	    //Create copy batch and return from database
	    copyBatch = glBatchDAO.saveAndReturn(new Batch());
	    copyBatchId = copyBatch.getBatchId();
	    copyJournalEntryId = copyBatchId + "-001";
	} else {
	    //Get copy batch from database
	    copyBatchId = Integer.valueOf(Integer.parseInt(copyGlBatchId));
	    copyBatch = glBatchDAO.findById(copyBatchId);
	    Integer suffix = glBatchDAO.getLastJournalEntrySuffix(copyBatchId);
	    suffix++;
	    copyJournalEntryId = copyBatchId + "-" + StringUtils.leftPad(suffix.toString(), 3, "0");
	}
	//Get journal entry from form
	JournalEntryBean journalEntryBean = glBatchForm.getJournalEntry();
	String journalEntryId = journalEntryBean.getId();
	//Create copy journal entry
	JournalEntry copyJournalEntry = new JournalEntry();
	copyJournalEntry.setBatchId(copyBatchId);
	copyJournalEntry.setJournalEntryId(copyJournalEntryId);
	copyJournalEntry.setJournalEntryDesc("Copy of " + journalEntryId);
	copyJournalEntry.setDebit(Double.valueOf(0.0D));
	copyJournalEntry.setCredit(Double.valueOf(0.0D));
	//Get subledger from database
	String subledgerId = journalEntryBean.getSubledgerType();
	GenericCode subledgerCode = new GenericCodeDAO().findById(Integer.valueOf(Integer.parseInt(subledgerId)));
	//Get line items from database
	List<LineItem> lineItems = glBatchDAO.getLineItems(journalEntryId);
	List copyLineItems = new ArrayList();
	Integer suffix = 1;
	if (CommonUtils.isNotEmpty(lineItems)) {
	    for (LineItem lineItem : lineItems) {
		//Create copy line item, set values and save it into database
		LineItem copyLineItem = new LineItem();
		StringBuilder copyLineItemId = new StringBuilder();
		copyLineItemId.append(copyJournalEntryId).append("-").append(StringUtils.leftPad(suffix.toString(), 3, "0"));
		copyLineItem.setLineItemId(copyLineItemId.toString());
		copyLineItem.setJournalEntryId(copyJournalEntryId);
		copyLineItem.setAccount(lineItem.getAccount());
		copyLineItem.setAccountDesc(lineItem.getAccountDesc());
		copyLineItem.setDebit(lineItem.getDebit());
		copyLineItem.setCredit(lineItem.getCredit());
		copyLineItem.setReference(lineItem.getReference());
		copyLineItem.setReferenceDesc(lineItem.getReferenceDesc());
		copyLineItem.setCurrency(lineItem.getCurrency());
		copyLineItem.setDate(new Date());
		glBatchDAO.save(copyLineItem);
		copyLineItems.add(new LineItemBean(copyLineItem));
		suffix++;
	    }
	}
	//Get next open period
	FiscalPeriod fiscalPeriod = new FiscalPeriodDAO().getNextOpenPeriod(journalEntryBean.getPeriod());
	if (null == fiscalPeriod) {
	    //Get current open period
	    fiscalPeriod = new FiscalPeriodDAO().getCurrentOpenPeriod();
	}
	//Set copy journal  entry values and save it into database
	copyJournalEntry.setPeriod(fiscalPeriod.getPeriodDis());
	copyJournalEntry.setSourceCode(subledgerCode);
	copyJournalEntry.setSourceCodeDesc(subledgerCode.getCodedesc());
	copyJournalEntry.setDebit(glBatchDAO.getTotalDebitFromLineItems(copyJournalEntryId));
	copyJournalEntry.setCredit(glBatchDAO.getTotalCreditFromLineItems(copyJournalEntryId));
	copyJournalEntry.setMemo(journalEntryBean.getMemo());
	glBatchDAO.save(copyJournalEntry);
	//Get copy batch from database and set values
	copyBatch = glBatchDAO.findById(copyBatchId);
	copyBatch.setBatchDesc(glBatch.getDescription());
	copyBatch.setTotalDebit(glBatchDAO.getTotalDebitFromJEs(copyBatchId));
	copyBatch.setTotalCredit(glBatchDAO.getTotalCreditFromJEs(copyBatchId));
	copyBatch.setSourceLedger(subledgerCode);
	//Set form values
	glBatchForm.setGlBatch(new GlBatchBean(copyBatch));
	glBatchForm.setBatchDebit(glBatchDAO.getTotalDebitFromJEs(copyBatchId, copyJournalEntryId).toString());
	glBatchForm.setBatchCredit(glBatchDAO.getTotalCreditFromJEs(copyBatchId, copyJournalEntryId).toString());
	glBatchForm.setJournalEntry(new JournalEntryBean(copyJournalEntry));
	glBatchForm.setLineItems(copyLineItems);
	request.setAttribute("message", "Copy of " + journalEntryId + " done successfully");
    }

    /**
     * addJournalEntry - Add new journal entry to the batch
     * @param glBatchForm
     * @param request
     * @throws Exception 
     */
    public static void addJournalEntry(GlBatchForm glBatchForm, HttpServletRequest request) throws Exception {
	User loginUser = (User) request.getSession().getAttribute("loginuser");
	GlBatchDAO glBatchDAO = new GlBatchDAO();
	//Get batch from form
	GlBatchBean glBatch = glBatchForm.getGlBatch();
	Integer batchId = Integer.valueOf(Integer.parseInt(glBatch.getId()));
	Batch batch = glBatchDAO.findById(batchId);
	Integer suffix = glBatchDAO.getLastJournalEntrySuffix(batchId);
	suffix++;
	String journalEntryId = batchId + "-" + StringUtils.leftPad(suffix.toString(), 3, "0");
	GenericCode subledgerCode = new GenericCodeDAO().getGenericCodeByCode("GL-JE");
	FiscalPeriod fiscalPeriod = new FiscalPeriodDAO().getCurrentOpenPeriod();
	//Create journal entry and save it into database
	JournalEntry journalEntry = new JournalEntry();
	journalEntry.setBatchId(batchId);
	journalEntry.setJournalEntryId(journalEntryId);
	journalEntry.setDebit(Double.valueOf(0.0D));
	journalEntry.setCredit(Double.valueOf(0.0D));
	journalEntry.setSourceCode(subledgerCode);
	journalEntry.setSourceCodeDesc(subledgerCode.getCodedesc());
	journalEntry.setPeriod(fiscalPeriod.getPeriodDis());
	glBatchDAO.save(journalEntry);
	//Set form values
	glBatchForm.setJournalEntry(new JournalEntryBean(journalEntry));
	glBatchForm.setBatchIndex(glBatchDAO.getBatchIndex(batchId));
	glBatchForm.setJournalEntryIndex(glBatchDAO.getJournalEntryIndex(batchId, journalEntryId));
	glBatchForm.setGlBatch(new GlBatchBean(batch));
	glBatchForm.setJournalEntry(new JournalEntryBean(journalEntry));
	List<LineItemBean> lineItems = new ArrayList();
	for (int i = 1; i <= 10; i++) {
	    lineItems.add(new LineItemBean(Integer.valueOf(i), journalEntryId));
	}
	glBatchForm.setLineItems(lineItems);
	glBatchForm.setBatchDebit(glBatchDAO.getTotalDebitFromJEs(batchId, journalEntryId).toString());
	glBatchForm.setBatchCredit(glBatchDAO.getTotalCreditFromJEs(batchId, journalEntryId).toString());
	StringBuilder desc = new StringBuilder("Journal Entry - ").append(journalEntryId);
	desc.append(" of GL Batch - ").append(batchId);
	desc.append(" added by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
	request.setAttribute("message", "Journal Entry - " + journalEntryId + " added to batch - " + batchId + " successfully");
    }

    /**
     * deleteJournalEntry - Delete journal entry from the batch
     * @param glBatchForm
     * @param request
     * @throws Exception 
     */
    public static void deleteJournalEntry(GlBatchForm glBatchForm, HttpServletRequest request) throws Exception {
	User loginUser = (User) request.getSession().getAttribute("loginuser");
	GlBatchDAO glBatchDAO = new GlBatchDAO();
	//Get batch from form
	GlBatchBean glBatch = glBatchForm.getGlBatch();
	Integer batchId = Integer.valueOf(Integer.parseInt(glBatch.getId()));
	//Get journal entry from form
	JournalEntryBean journalEntryBean = glBatchForm.getJournalEntry();
	String journalEntryId = journalEntryBean.getId();
	//Get subledger from database
	String subledgerId = journalEntryBean.getSubledgerType();
	GenericCode subledgerCode = new GenericCodeDAO().findById(Integer.valueOf(Integer.parseInt(subledgerId)));
	//Delete line items from database
	glBatchDAO.deleteLineItems(journalEntryId, null);
	//Delete journal entry from database
	glBatchDAO.deleteJournalEntry(journalEntryId);
	//Get batch from database and update it's values
	Batch batch = glBatchDAO.findById(batchId);
	batch.setBatchDesc(glBatch.getDescription());
	batch.setTotalDebit(glBatchDAO.getTotalDebitFromJEs(batchId));
	batch.setTotalCredit(glBatchDAO.getTotalCreditFromJEs(batchId));
	batch.setSourceLedger(subledgerCode);
	//Create notes and save it into database 
	StringBuilder desc = new StringBuilder("Journal Entry - ").append(journalEntryId);
	desc.append(" of GL Batch - ").append(batchId);
	desc.append(" deleted by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
	AuditNotesUtils.insertAuditNotes(desc.toString(), "GL_BATCH", batchId.toString(), "GL_BATCH", loginUser);
	//Set form values
	glBatchForm.setGlBatch(new GlBatchBean(batch));
	glBatchForm.setBatchIndex(glBatchDAO.getBatchIndex(batchId));
	glBatchForm.setJournalEntryIndex(glBatchDAO.getJournalEntryIndex(batchId, journalEntryId));

	request.setAttribute("message", "Journal Entry - " + journalEntryId + " deleted from batch - " + batchId + " successfully");
	//Get the last journal entry for the batch from database and set it into form
	JournalEntry journalEntry = glBatchDAO.getJournalEntry(batchId, glBatchForm.getJournalEntryIndex());
	if (null != journalEntry) {
	    glBatchForm.setBatchDebit(glBatchDAO.getTotalDebitFromJEs(batchId, journalEntry.getJournalEntryId()).toString());
	    glBatchForm.setBatchCredit(glBatchDAO.getTotalCreditFromJEs(batchId, journalEntry.getJournalEntryId()).toString());
	    glBatchForm.setJournalEntry(new JournalEntryBean(journalEntry));
	    List<LineItem> lineItems = glBatchDAO.getLineItems(journalEntry.getJournalEntryId());
	    if (CommonUtils.isNotEmpty(lineItems)) {
		for (LineItem lineItem : lineItems) {
		    glBatchForm.getLineItems().add(new LineItemBean(lineItem));
		}
	    } else {
		for (int i = 1; i <= 10; i++) {
		    glBatchForm.getLineItems().add(new LineItemBean(Integer.valueOf(i), journalEntry.getJournalEntryId()));
		}
	    }
	} else {
	    glBatchForm.setJournalEntry(new JournalEntryBean());
	    request.setAttribute("error", "No more Journal Entries found");
	}
    }

    /**
     * postBatch - Post the batch and update gl account balance based on fiscal period
     * @param glBatchForm
     * @param request
     * @throws Exception 
     */
    public static void postBatch(GlBatchForm glBatchForm, HttpServletRequest request) throws Exception {
	User loginUser = (User) request.getSession().getAttribute("loginuser");
	GlBatchDAO glBatchDAO = new GlBatchDAO();
	Integer batchId = Integer.valueOf(Integer.parseInt(glBatchForm.getBatchId()));
	//Get batch from database
	Batch batch = glBatchDAO.findById(batchId);
	//Validate batch for posting
	String error = glBatchDAO.validateGlBatchForPosting(batchId);
	if (null != error) {
	    request.setAttribute("error", "Cannot post Batch - " + error);
	} else {
	    batch.setStatus("posted");
	    //Update gl account balance in database
	    glBatchDAO.updateAccountBalance(batchId);
	    //Create notes and save it into database
	    StringBuilder desc = new StringBuilder("GL Batch - ").append(batchId);
	    desc.append(" posted by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
	    AuditNotesUtils.insertAuditNotes(desc.toString(), "GL_BATCH", batchId.toString(), "GL_BATCH", loginUser);
	    request.setAttribute("message", "Batch - " + batchId + " posted successfully.");
	}
    }

    /**
     * voidBatch - Void the batch and delete it's journal entries and line items
     * @param glBatchForm
     * @param request
     * @throws Exception 
     */
    public static void voidBatch(GlBatchForm glBatchForm, HttpServletRequest request) throws Exception {
	User loginUser = (User) request.getSession().getAttribute("loginuser");
	Integer batchId = Integer.valueOf(Integer.parseInt(glBatchForm.getBatchId()));
	//Void batch in database
	new GlBatchDAO().voidBatch(batchId);
	//Create notes and save it into database
	StringBuilder desc = new StringBuilder("GL Batch - ").append(batchId);
	desc.append(" voided by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
	AuditNotesUtils.insertAuditNotes(desc.toString(), "GL_BATCH", batchId.toString(), "GL_BATCH", loginUser);
	request.setAttribute("message", "Batch - " + batchId + " voided successfully.");
    }

    /**
     * printBatch - Create batch report in pdf and show it in screen for print
     * @param contextPath
     * @param batchId
     * @param response
     * @throws Exception 
     */
    public static void printBatch(String contextPath, Integer batchId, HttpServletResponse response) throws Exception {
	//Get batch from database
	Batch batch = new GlBatchDAO().findById(batchId);
	//Call report creator to create the pdf
	String fileName = new GlBatchReportCreator().createReport(contextPath, batch);
	if (CommonUtils.isNotEmpty(fileName)) {
	    //Put the pdf file into response as inline document
	    response.addHeader("Content-Disposition", "inline; filename=" + FilenameUtils.getName(fileName));
	    response.setContentType("application/pdf;charset=utf-8");
	    ServletUtils.returnFile(fileName, response.getOutputStream());
	}
    }

    /**
     * exportBatch - Export the batch into excel sheet and put it into screen for download
     * @param batchId
     * @param response
     * @throws Exception 
     */
    public static void exportBatch(Integer batchId, HttpServletResponse response) throws Exception {
	//Get batch from database
	Batch batch = new GlBatchDAO().findById(batchId);
	//Call excel creator to create the xls sheet
	String fileName = new GlBatchExcelCreator().exportToExcel(batch);
	if (CommonUtils.isNotEmpty(fileName)) {
	    //Put the xls file into response as attachment
	    response.addHeader("Content-Disposition", "attachment; filename=" + FilenameUtils.getName(fileName));
	    response.setContentType("application/vnd.ms-excel;charset=utf-8");
	    ServletUtils.returnFile(fileName, response.getOutputStream());
	}
    }

    /**
     * printJournalEntry - Create journal entry report in pdf and show it in screen for print
     * @param contextPath
     * @param journalEntryId
     * @param response
     * @throws Exception 
     */
    public static void printJournalEntry(String contextPath, String journalEntryId, HttpServletResponse response) throws Exception {
	//Get journal entry from database
	JournalEntry journalEntry = new GlBatchDAO().getJournalEntry(journalEntryId);
	//Call report creator to create the pdf
	String fileName = new JournalEntryReportCreator().createReport(contextPath, journalEntry);
	if (CommonUtils.isNotEmpty(fileName)) {
	    //Put the pdf file in response as inline document
	    response.addHeader("Content-Disposition", "inline; filename=" + FilenameUtils.getName(fileName));
	    response.setContentType("application/pdf;charset=utf-8");
	    ServletUtils.returnFile(fileName, response.getOutputStream());
	}
    }

    /**
     * exportJournalEntry - Export the journal entry into excel sheet and put it into screen for download
     * @param journalEntryId
     * @param response
     * @throws Exception 
     */
    public static void exportJournalEntry(String journalEntryId, HttpServletResponse response) throws Exception {
	//Get journal entry from database
	JournalEntry journalEntry = new GlBatchDAO().getJournalEntry(journalEntryId);
	//Call excel creator to create the xls sheet
	String fileName = new JournalEntryExcelCreator().exportToExcel(journalEntry);
	if (CommonUtils.isNotEmpty(fileName)) {
	    //Put the xls file in reponse as attachment
	    response.addHeader("Content-Disposition", "attachment; filename=" + FilenameUtils.getName(fileName));
	    response.setContentType("application/vnd.ms-excel;charset=utf-8");
	    ServletUtils.returnFile(fileName, response.getOutputStream());
	}
    }

    /**
     * printHistory - Create journal entry history report in pdf and show it in screen for print
     * @param contextPath
     * @param journalEntryId
     * @param response
     * @throws Exception 
     */
    public static void printHistory(String contextPath, String journalEntryId, HttpServletResponse response) throws Exception {
	GlBatchDAO glBatchDAO = new GlBatchDAO();
	//Get journal entry from database
	JournalEntry journalEntry = glBatchDAO.getJournalEntry(journalEntryId);
	List transactions = new ArrayList();
	//Get subledger from journal entry
	String subledgerType = journalEntry.getSourceCode().getCode();
	if (CommonUtils.isEqualIgnoreCase(subledgerType, "ACC")) {
	    //Get ACC subledger history from database
	    transactions.addAll(glBatchDAO.getACCSubledgersHistory(journalEntryId));
	} else if (CommonUtils.isEqualIgnoreCase(subledgerType, "PJ")) {
	    //Get PJ subledger history from database
	    transactions.addAll(glBatchDAO.getPJSubledgersHistory(journalEntryId));
	} else if (CommonUtils.isEqualIgnoreCase(subledgerType, "CD")) {
	    //Get CD subledger history from database
	    transactions.addAll(glBatchDAO.getCDSubledgersHistory(journalEntryId));
	} else if (CommonUtils.isEqualIgnoreCase(subledgerType, "NET SETT")) {
	    //Get NS subledger history from database
	    transactions.addAll(glBatchDAO.getNSSubledgersHistory(journalEntryId));
	} else if (CommonUtils.isEqualIgnoreCase(subledgerType, "RCT")) {
	    //Get RCT subledger history from database
	    transactions.addAll(glBatchDAO.getRCTSubledgersHistory(journalEntryId));
	} else {
	    //Get AR subledger history from database
	    transactions.addAll(glBatchDAO.getARSubledgersHistory(journalEntryId));
	}
	String period = journalEntry.getPeriod();
	//Call report creator to create the pdf
	String fileName = new SubledgerHistoryReportCreator().createReport(contextPath, journalEntryId, subledgerType, period, transactions);
	if (CommonUtils.isNotEmpty(fileName)) {
	    //Put the pdf file in response as inline document
	    response.addHeader("Content-Disposition", "inline; filename=" + FilenameUtils.getName(fileName));
	    response.setContentType("application/pdf;charset=utf-8");
	    ServletUtils.returnFile(fileName, response.getOutputStream());
	}
    }

    /**
     * exportHistory - Export the journal entry history into excel sheet and put it into screen for download
     * @param journalEntryId
     * @param response
     * @throws Exception 
     */
    public static void exportHistory(String journalEntryId, HttpServletResponse response) throws Exception {
	GlBatchDAO glBatchDAO = new GlBatchDAO();
	//Get journal entry from database
	JournalEntry journalEntry = glBatchDAO.getJournalEntry(journalEntryId);
	List transactions = new ArrayList();
	//Get subledger from journal entry
	String subledgerType = journalEntry.getSourceCode().getCode();
	if (CommonUtils.isEqualIgnoreCase(subledgerType, "ACC")) {
	    //Get ACC subledger history from database
	    transactions.addAll(glBatchDAO.getACCSubledgersHistory(journalEntryId));
	} else if (CommonUtils.isEqualIgnoreCase(subledgerType, "PJ")) {
	    //Get PJ subledger history from database
	    transactions.addAll(glBatchDAO.getPJSubledgersHistory(journalEntryId));
	} else if (CommonUtils.isEqualIgnoreCase(subledgerType, "CD")) {
	    //Get CD subledger history from database
	    transactions.addAll(glBatchDAO.getCDSubledgersHistory(journalEntryId));
	} else if (CommonUtils.isEqualIgnoreCase(subledgerType, "NET SETT")) {
	    //Get NS subledger history from database
	    transactions.addAll(glBatchDAO.getNSSubledgersHistory(journalEntryId));
	} else if (CommonUtils.isEqualIgnoreCase(subledgerType, "RCT")) {
	    //Get RCT subledger history from database
	    transactions.addAll(glBatchDAO.getRCTSubledgersHistory(journalEntryId));
	} else {
	    //Get AR subledger history from database
	    transactions.addAll(glBatchDAO.getARSubledgersHistory(journalEntryId));
	}
	String period = journalEntry.getPeriod();
	//Call excel creator to create the xls sheet
	String fileName = new SubledgerHistoryExcelCreator().exportToExcel(journalEntryId, subledgerType, period, transactions);
	if (CommonUtils.isNotEmpty(fileName)) {
	    //Put the xls file in reponse as attachment
	    response.addHeader("Content-Disposition", "attachment; filename=" + FilenameUtils.getName(fileName));
	    response.setContentType("application/vnd.ms-excel;charset=utf-8");
	    ServletUtils.returnFile(fileName, response.getOutputStream());
	}
    }

    public static void createReconcileJournalEntry(ReconcileForm reconcileForm, HttpServletRequest request) throws Exception {
	GlBatchDAO glBatchDAO = new GlBatchDAO();
	AccountDetailsDAO accountDetailsDAO = new AccountDetailsDAO();
	double difference = Double.parseDouble(reconcileForm.getDifference().replace(",", ""));
	//Create batch and return from database
	Batch batch = glBatchDAO.saveAndReturn(new Batch());
	Integer batchId = batch.getBatchId();
	//Form journal id
	String journalEntryId = batchId + "-001";
	//Get GL-JE subledger from database
	GenericCode subledgerCode = new GenericCodeDAO().getGenericCodeByCode("GL-JE");
	//Get current open period
	String reconcilePeriod = DateUtils.formatDate(DateUtils.parseDate(reconcileForm.getReconcileDate(), "MM/dd/yyyy"), "yyyyMM");
	FiscalPeriod fiscalPeriod = new FiscalPeriodDAO().getFiscalPeriod(reconcilePeriod);
	//Create journal entry and set values
	JournalEntry journalEntry = new JournalEntry();
	journalEntry.setBatchId(batchId);
	journalEntry.setJournalEntryId(journalEntryId);
	journalEntry.setJournalEntryDesc("Auto Reconcile");
	journalEntry.setDebit(Math.abs(difference));
	journalEntry.setCredit(Math.abs(difference));
	journalEntry.setSourceCode(subledgerCode);
	journalEntry.setSourceCodeDesc(subledgerCode.getCodedesc());
	journalEntry.setPeriod(fiscalPeriod.getPeriodDis());
	//Create line items and set values
	List<LineItem> lineItems = new ArrayList<LineItem>();
	LineItem bankGlLineItem = new LineItem();
	bankGlLineItem.setLineItemId(journalEntry.getJournalEntryId() + "-001");
	bankGlLineItem.setJournalEntryId(journalEntryId);
	AccountDetails bankGlAccount = accountDetailsDAO.findById(reconcileForm.getGlAccount());
	bankGlLineItem.setAccount(bankGlAccount.getAccount());
	bankGlLineItem.setAccountDesc(bankGlAccount.getAcctDesc());
	if (difference < 0) {
	    bankGlLineItem.setDebit(0.00);
	    bankGlLineItem.setCredit(-difference);
	} else {
	    bankGlLineItem.setDebit(difference);
	    bankGlLineItem.setCredit(0.00);
	}
	bankGlLineItem.setReference("");
	bankGlLineItem.setReferenceDesc("");
	bankGlLineItem.setCurrency("USD");
	bankGlLineItem.setDate(new Date());
	lineItems.add(bankGlLineItem);
	LineItem offsetLineItem = new LineItem();
	offsetLineItem.setLineItemId(journalEntry.getJournalEntryId() + "-002");
	offsetLineItem.setJournalEntryId(journalEntryId);
	AccountDetails offsetGlAccount = accountDetailsDAO.findById(LoadLogisoftProperties.getProperty("reconcile.offset.account"));
	offsetLineItem.setAccount(offsetGlAccount.getAccount());
	offsetLineItem.setAccountDesc(offsetGlAccount.getAcctDesc());
	if (difference < 0) {
	    offsetLineItem.setDebit(-difference);
	    offsetLineItem.setCredit(0.00);
	} else {
	    offsetLineItem.setDebit(0.00);
	    offsetLineItem.setCredit(difference);
	}
	offsetLineItem.setReference("");
	offsetLineItem.setReferenceDesc("");
	offsetLineItem.setCurrency("USD");
	offsetLineItem.setDate(new Date());
	lineItems.add(offsetLineItem);
	//Create new line item set and add line items to it
	journalEntry.setLineItemSet(new HashSet());
	journalEntry.getLineItemSet().add(bankGlLineItem);
	journalEntry.getLineItemSet().add(offsetLineItem);
	//Create new journal entry set and add journal entry to it
	batch.setJournalEntrySet(new HashSet());
	batch.getJournalEntrySet().add(journalEntry);
	//Set subledger 
	batch.setSourceLedger(subledgerCode);
	batch.setBatchDesc("Auto Reconcile");
	batch.setTotalDebit(Math.abs(difference));
	batch.setTotalCredit(Math.abs(difference));
	batch.setBatchDesc("Auto Reconcile");
	//Update batch to database
	glBatchDAO.update(batch);
	//Set form values
	GlBatchForm glBatchForm = new GlBatchForm();
	glBatchForm.setBatchIndex(glBatchDAO.getBatchIndex(batchId));
	glBatchForm.setJournalEntryIndex(glBatchDAO.getJournalEntryIndex(batchId, journalEntryId));
	glBatchForm.setBatchDebit(glBatchDAO.getTotalDebitFromJEs(batchId, journalEntryId).toString());
	glBatchForm.setBatchCredit(glBatchDAO.getTotalCreditFromJEs(batchId, journalEntryId).toString());
	glBatchForm.setGlBatch(new GlBatchBean(batch));
	glBatchForm.setJournalEntry(new JournalEntryBean(journalEntry));
	for (LineItem lineItem : lineItems) {
	    glBatchForm.getLineItems().add(new LineItemBean(lineItem));
	}
	glBatchForm.setFrom("Reconcile");
	request.setAttribute("message", "Auto JE Batch - " + batchId + " created successfully");
	request.setAttribute("glBatchForm", glBatchForm);
    }

    /**
     * saveAndPost - Save and Post batch, it's journal entry and line items
     * @param glBatchForm
     * @param request
     * @throws Exception 
     */
    public static void saveAndPost(GlBatchForm glBatchForm, HttpServletRequest request) throws Exception {
	User loginUser = (User) request.getSession().getAttribute("loginuser");
	GlBatchDAO glBatchDAO = new GlBatchDAO();
	//Get batch from form
	GlBatchBean glBatch = glBatchForm.getGlBatch();
	Integer batchId = Integer.valueOf(Integer.parseInt(glBatch.getId()));
	//Get journal entry from form
	JournalEntryBean journalEntryBean = glBatchForm.getJournalEntry();
	String journalEntryId = journalEntryBean.getId();
	String subledgerId = journalEntryBean.getSubledgerType();
	//Get subledger from database
	GenericCode subledgerCode = new GenericCodeDAO().findById(Integer.valueOf(Integer.parseInt(subledgerId)));
	//Get line items from form
	List<LineItemBean> lineItems = glBatchForm.getLineItems();
	List lineItemIds = new ArrayList();
	if (CommonUtils.isNotEmpty(lineItems)) {
	    for (LineItemBean lineItemBean : lineItems) {
		lineItemIds.add("'" + lineItemBean.getId() + "'");
		//Get line item from database
		LineItem lineItem = glBatchDAO.getLineItem(lineItemBean.getId());
		if (null != lineItem) {
		    //Update line item into database
		    lineItem.setAccount(lineItemBean.getAccount());
		    lineItem.setAccountDesc(lineItemBean.getAccountDescription());
		    lineItem.setDebit(Double.valueOf(Double.parseDouble(lineItemBean.getDebit())));
		    lineItem.setCredit(Double.valueOf(Double.parseDouble(lineItemBean.getCredit())));
		    lineItem.setReference(lineItemBean.getReference());
		    lineItem.setReferenceDesc(lineItemBean.getReferenceDescription());
		    lineItem.setCurrency(lineItemBean.getCurrency());
		} else {
		    //Save line item into database
		    lineItem = new LineItem();
		    lineItem.setLineItemId(lineItemBean.getId());
		    lineItem.setJournalEntryId(journalEntryId);
		    lineItem.setAccount(lineItemBean.getAccount());
		    lineItem.setAccountDesc(lineItemBean.getAccountDescription());
		    lineItem.setDebit(Double.valueOf(Double.parseDouble(lineItemBean.getDebit())));
		    lineItem.setCredit(Double.valueOf(Double.parseDouble(lineItemBean.getCredit())));
		    lineItem.setReference(lineItemBean.getReference());
		    lineItem.setReferenceDesc(lineItemBean.getReferenceDescription());
		    lineItem.setCurrency(lineItemBean.getCurrency());
		    lineItem.setDate(new Date());
		    glBatchDAO.save(lineItem);
		}
	    }
	}
	//Delete line item from database
	glBatchDAO.deleteLineItems(journalEntryId, lineItemIds);
	//Get journal entry from database and set values
	JournalEntry journalEntry = glBatchDAO.getJournalEntry(journalEntryId);
	journalEntry.setJournalEntryDesc(journalEntryBean.getDescription());
	journalEntry.setPeriod(journalEntryBean.getPeriod());
	journalEntry.setSourceCode(subledgerCode);
	journalEntry.setSourceCodeDesc(subledgerCode.getCodedesc());
	journalEntry.setDebit(glBatchDAO.getTotalDebitFromLineItems(journalEntryId));
	journalEntry.setCredit(glBatchDAO.getTotalCreditFromLineItems(journalEntryId));
	journalEntry.setMemo(journalEntryBean.getMemo());
	//Get batch from database and set values
	Batch batch = glBatchDAO.findById(batchId);
	batch.setBatchDesc(glBatch.getDescription());
	batch.setTotalDebit(glBatchDAO.getTotalDebitFromJEs(batchId));
	batch.setTotalCredit(glBatchDAO.getTotalCreditFromJEs(batchId));
	batch.setSourceLedger(subledgerCode);
	//Get batch from database
	//Validate batch for posting
	String error = glBatchDAO.validateGlBatchForPosting(batchId);
	if (null != error) {
	    request.setAttribute("error", "Cannot post Batch - " + error);
	} else {
	    batch.setStatus("posted");
	    //Update gl account balance in database
	    glBatchDAO.updateAccountBalance(batchId);
	    //Create notes and save it into database
	    StringBuilder desc = new StringBuilder("GL Batch - ").append(batchId);
	    desc.append(" posted by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
	    AuditNotesUtils.insertAuditNotes(desc.toString(), "GL_BATCH", batchId.toString(), "GL_BATCH", loginUser);
	    request.setAttribute("message", "Batch - " + batchId + " posted successfully.");
	}
    }
}
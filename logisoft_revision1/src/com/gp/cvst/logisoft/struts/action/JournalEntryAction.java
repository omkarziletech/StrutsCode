package com.gp.cvst.logisoft.struts.action;

import com.gp.cong.common.CommonUtils;
import java.io.File;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.bc.accounting.JournalEntryBC;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.util.DBUtil;
import com.gp.cvst.logisoft.domain.AccountDetails;
import com.gp.cvst.logisoft.domain.Batch;
import com.gp.cvst.logisoft.domain.FiscalPeriod;
import com.gp.cvst.logisoft.domain.JournalEntry;
import com.gp.cvst.logisoft.domain.LineItem;
import com.gp.cvst.logisoft.hibernate.dao.AccountBalanceDAO;
import com.gp.cvst.logisoft.hibernate.dao.AccountDetailsDAO;
import com.gp.cvst.logisoft.hibernate.dao.BatchDAO;
import com.gp.cvst.logisoft.hibernate.dao.FiscalPeriodDAO;
import com.gp.cvst.logisoft.hibernate.dao.JournalEntryDAO;
import com.gp.cvst.logisoft.struts.form.JournalEntryForm;
import com.oreilly.servlet.ServletUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

public class JournalEntryAction extends Action {
//
//    public ActionForward execute(ActionMapping mapping, ActionForm form,
//	    HttpServletRequest request, HttpServletResponse response) throws Exception {
//	JournalEntryForm journalEntryForm = (JournalEntryForm) form;// TODO Auto-generated method stub
//
//	GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
//	AccountDetailsDAO accountDetailsDAO = new AccountDetailsDAO();
//	String forwardName = "";
//	String buttonValue = journalEntryForm.getButtonValue();
//	if (CommonUtils.isEqualIgnoreCase(buttonValue, "uploadJournalEntry")) {
//	    try {
//		new JournalEntryUtils().uploadJournalEntry(journalEntryForm.getJournalEntrySheet().getInputStream());
//		request.setAttribute("message", "Journal Entry Sheet uploaded successfully");
//	    } catch (Exception e) {
//		e.printStackTrace();
//		request.setAttribute("error", e.getMessage());
//	    }
//	    return mapping.findForward("go");
//	}
//	HttpSession session = request.getSession();
//	String batchId = "";
//	String journalId = journalEntryForm.getJournalid();
//	// Exporting the history of the related Subledgers for the journal entry
//	if (CommonUtils.isEqualIgnoreCase(buttonValue, "exportToExcel")) {
//	    String excelFileName = new JournalEntryBC().exportToExcel(journalId);
//	    if (CommonUtils.isNotEmpty(excelFileName)) {
//		response.addHeader("Content-Disposition", "attachment; filename=" + FilenameUtils.getName(excelFileName));
//		response.setContentType("application/vnd.ms-excel" + ";charset=utf-8");
//		ServletUtils.returnFile(excelFileName, response.getOutputStream());
//	    }
//	    return null;
//	}
//
//
//	if (journalEntryForm.getBatch() != null && !journalEntryForm.getBatch().equals("")) {
//	    batchId = journalEntryForm.getBatch();
//	}
//	Set newLineSet = new HashSet();
//	String batchdesc = journalEntryForm.getBdesc();
//	String debitTotal = journalEntryForm.getBtotaldebit();
//	DBUtil dbUtil = new DBUtil();
//	String creditTotal = journalEntryForm.getBtotalcredit();
//	creditTotal = dbUtil.removecomma(creditTotal);
//	String status = journalEntryForm.getBstatus();
//	String txtCal = journalEntryForm.getTxtCal();
//	String journalDesc = journalEntryForm.getJedesc();
//	String jeperiod = journalEntryForm.getJper();
//	String sourcecode = journalEntryForm.getJesourcecode();
//	String currency = journalEntryForm.getCurrency();
//	String jedebit = journalEntryForm.getJedebit();
//	jedebit = dbUtil.removecomma(jedebit);
//	String jecredit = journalEntryForm.getJecredit();
//	jecredit = dbUtil.removecomma(jecredit);
//	String memo = journalEntryForm.getJememo();
//	String itemNo = journalEntryForm.getItemNo();
//	String reference = journalEntryForm.getReference();
//	String refDesc = journalEntryForm.getDescription();
//	String account = journalEntryForm.getAccount();
//	String linedebit = journalEntryForm.getDebits();
//	String linecredit = journalEntryForm.getCredits();
//	String index = journalEntryForm.getIndex();
//	LineItem line = new LineItem();
//	String[] dispitemNo = journalEntryForm.getDisplineItemId();
//	String[] dispaccount = journalEntryForm.getDispaccount();
//	String[] dispcredit = journalEntryForm.getDispcredit();
//	String[] dispdebit = journalEntryForm.getDispdebit();
//	String[] dispRef = journalEntryForm.getDispreference();
//	String[] dispRefDesc = journalEntryForm.getDispreferenceDesc();
//	String[] dispCurrency = journalEntryForm.getDispcurrency();
//	String hiddenLineItemId = journalEntryForm.getHiddenItemNo();
//	session.setAttribute("itemNo", hiddenLineItemId);
//	if (session.getAttribute("buttonValue") != null) {
//	    session.removeAttribute("buttonValue");
//	}
//	journalEntryForm.setReverse("off");
//	request.setAttribute("journalEntryForm", journalEntryForm);
//	if (request.getParameter("paramid") == null) {
//	    List batchListchanged = new ArrayList();
//	    Batch batchchange = new Batch();
//	    JournalEntry journalchange = new JournalEntry();
//	    if (session.getAttribute("batchList") != null) {
//		batchListchanged = (List) session.getAttribute("batchList");
//	    }
//	    for (int i = 0; i < batchListchanged.size(); i++) {
//		batchchange = (Batch) batchListchanged.get(i);
//		if (batchchange.getBatchId().equals(batchId)) {
//		    batchchange.setBatchId(batchId);
//		    batchchange.setBatchDesc(batchdesc);
//		    batchchange.setTotalDebit(batchchange.getTotalDebit());
//		    batchchange.setTotalCredit(batchchange.getTotalCredit());
//		    batchchange.setStatus(batchchange.getStatus());
//		    session.setAttribute("batch", batchchange);
//		}
//	    }
//	    if (session.getAttribute("journalEntry") != null) {
//		journalchange = (JournalEntry) session.getAttribute("journalEntry");
//	    }
//	    journalchange.setJournalEntryId(journalId);
//	    journalchange.setJournalEntryDesc(journalDesc);
//	    if (txtCal != null && !txtCal.equals("")) {
//		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
//		java.util.Date javaDate = null;
//		try {
//		    javaDate = sdf.parse(txtCal);
//		} catch (ParseException e) {
//		    // TODO Auto-generated catch block
//		    e.printStackTrace();
//		}
//		journalchange.setJeDate(javaDate);
//	    }
//	    if (jeperiod != null && !jeperiod.equals("0")) {
//		FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
//		List fiscal = fiscalPeriodDAO.getPeriodList3(jeperiod);
//		FiscalPeriod fis = new FiscalPeriod();
//		if (fiscal != null && fiscal.size() > 0) {
//		    fis = (FiscalPeriod) fiscal.get(0);
//		}
//		if (fis != null) {
//		    journalchange.setPeriod(fis.getPeriodDis());
//		}
//	    }
//	    if (sourcecode != null && !sourcecode.equals("0")) {
//		GenericCode genericCode = genericCodeDAO.findById(Integer.parseInt(sourcecode));
//		journalchange.setSourceCode(genericCode);
//		journalchange.setSourceCodeDesc(genericCode.getCodedesc());
//	    }
//	    if (jecredit != null && !jecredit.equals("")) {
//		jecredit = dbUtil.getDecimal(jecredit);
//		journalchange.setCredit(new Double(jecredit));
//	    }
//	    if (jedebit != null && !jedebit.equals("")) {
//		jedebit = dbUtil.getDecimal(jedebit);
//		journalchange.setDebit(new Double(jedebit));
//	    }
//	    journalchange.setMemo(memo);
//	    session.setAttribute("journalEntry", journalchange);
//	    List lineItemList = new ArrayList();
//	    Double credit = 0.00;
//	    Double debit = 0.00;
//
//	    if (session.getAttribute("lineItemList") != null) {
//		lineItemList = (List) session.getAttribute("lineItemList");
//		line = new LineItem();
//		for (int i = 0; i < lineItemList.size(); i++) {
//		    line = (LineItem) lineItemList.get(i);
//		    if (line != null && line.getLineItemId() != null && dispitemNo != null && dispitemNo[i] != null) {
//			if (line.getLineItemId().equals(dispitemNo[i])) {
//			    line.setLineItemId(dispitemNo[i]);
//			    if (dispaccount[i] != null) {
//				List accountList = accountDetailsDAO.findAccoutnNo1(dispaccount[i]);
//				if (accountList != null && accountList.size() > 0) {
//				    line.setAccount(dispaccount[i]);
//				} else {
//				    line.setAccount("");
//				}
//			    }
//			    if (jeperiod != null && !jeperiod.equals("0")) {
//				FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
//				List fiscal = fiscalPeriodDAO.getPeriodList3(jeperiod);
//				FiscalPeriod fis = new FiscalPeriod();
//				if (fiscal != null && fiscal.size() > 0) {
//				    fis = (FiscalPeriod) fiscal.get(0);
//				}
//				line.setPeriod(fis);
//			    }
//			    if (dispcredit[i] != null && !dispcredit[i].equals("")) {
//				if (!dispcredit[i].equals("")) {
//				    line.setCredit(Double.parseDouble(dbUtil.removecomma(dispcredit[i])));
//				}
//				credit = credit + line.getCredit();
//			    } else {
//				line.setCredit(Double.parseDouble(("0.00")));
//				credit = credit + line.getCredit();
//			    }
//			    if (dispdebit[i] != null && !dispdebit[i].equals("")) {
//				if (!dispdebit[i].equals("")) {
//				    line.setDebit(Double.parseDouble(dbUtil.removecomma(dispdebit[i])));
//				}
//				debit = debit + line.getDebit();
//			    } else {
//				line.setDebit(new Double(0.00));
//				debit = debit + line.getDebit();
//			    }
//			    line.setReference(dispRef[i]);
//			    line.setReferenceDesc(dispRefDesc[i]);
//			    line.setCurrency(dispCurrency[i]);
//			    Date date = new Date(System.currentTimeMillis());
//			    line.setDate(date);
//			    if (!dispaccount[i].equals("")) {
//				List acctList = accountDetailsDAO.findAccoutnNo1(dispaccount[i]);
//				if (acctList != null && acctList.size() > 0) {
//				    AccountDetails acct = (AccountDetails) acctList.get(0);
//				    line.setAccount(acct.getAccount());
//				}
//			    }
//			}
//		    }
//		}
//		session.setAttribute("lineItemList", lineItemList);
//	    }
//	    forwardName = "go";
//	}
//	List batchList = new ArrayList();
//	JournalEntry journalEntry = new JournalEntry();
//	line = new LineItem();
//	List journalEntryList = new ArrayList();
//	List lineItemList = new ArrayList();
//	String message = "";
//	if (request.getParameter("paramid") != null) {
//	    String ind = request.getParameter("paramid");
//	    List lineList = null;
//	    if (session.getAttribute("lineItemList") != null) {
//		lineList = (List) session.getAttribute("lineItemList");
//		for (int i = 0; i < lineList.size(); i++) {
//		    LineItem l3 = (LineItem) lineList.get(i);
//		    if (l3.getLineItemId().equals(ind)) {
//			session.setAttribute("line", l3);
//		    }
//		}
//	    }
//	    request.setAttribute("addline", "addlineitem");
//	    buttonValue = "linepopup";
//	    forwardName = "go";
//	    if (session.getAttribute("search") != null) {
//		session.removeAttribute("search");
//	    }
//	} else {
//	    if (buttonValue.equals("autoreverse")) {
//		journalEntryForm.setReverse("on");
//		request.setAttribute("journalEntryForm", journalEntryForm);
//		Batch batch = new BatchDAO().findById(journalEntryForm.getBatch());
//		if (null != batch && null != batch.getJournalEntrySet()) {
//		    Batch batchToSession = new Batch();
//		    PropertyUtils.copyProperties(batchToSession, batch);
//		    JournalEntry originalJournalEntry = new JournalEntryDAO().findById(journalEntryForm.getJournalid());
//		    if (null != originalJournalEntry && null != originalJournalEntry.getLineItemSet()) {
//			JournalEntry autoReverseJournalEntry = new JournalEntry();
//			String autoReverseJournalEntryId = originalJournalEntry.getJournalEntryId() + "R";
//			autoReverseJournalEntry.setBatchId(originalJournalEntry.getBatchId());
//			autoReverseJournalEntry.setJournalEntryId(autoReverseJournalEntryId);
//			autoReverseJournalEntry.setJournalEntryDesc("AutoReverse of " + journalEntryForm.getJournalid());
//			autoReverseJournalEntry.setJeDate(new Date());
//			boolean noOpenPeriod = true;
//			if (CommonUtils.isNotEqual(jeperiod, "0")) {
//			    FiscalPeriod fiscalPeriod = new FiscalPeriodDAO().getNextOpenPeriod(jeperiod);
//			    if (null != fiscalPeriod) {
//				autoReverseJournalEntry.setPeriod(fiscalPeriod.getPeriodDis());
//				noOpenPeriod = false;
//			    }
//			}
//			if (noOpenPeriod) {
//			    FiscalPeriod fiscalPeriod = new FiscalPeriodDAO().getCurrentOpenPeriod();
//			    autoReverseJournalEntry.setPeriod(fiscalPeriod.getPeriodDis());
//			}
//			autoReverseJournalEntry.setMemo(memo);
//			if (CommonUtils.isNotEqual(sourcecode, "0")) {
//			    GenericCode genericCode = genericCodeDAO.findById(Integer.parseInt(sourcecode));
//			    autoReverseJournalEntry.setSourceCode(genericCode);
//			    autoReverseJournalEntry.setSourceCodeDesc(genericCode.getCodedesc());
//			} else if (null != originalJournalEntry.getSourceCode()) {
//			    autoReverseJournalEntry.setSourceCode(originalJournalEntry.getSourceCode());
//			    autoReverseJournalEntry.setSourceCodeDesc(originalJournalEntry.getSourceCode().getCodedesc());
//			}
//			Set<LineItem> autoReverseLineItems = new HashSet<LineItem>();
//			List<LineItem> autoReverseLineItemList = new ArrayList<LineItem>();
//			int i = 1;
//			double jeCredit = 0d;
//			double jeDebit = 0d;
//			for (LineItem lineItem : (Set<LineItem>) originalJournalEntry.getLineItemSet()) {
//			    LineItem autoReverseLineItem = new LineItem();
//			    PropertyUtils.copyProperties(autoReverseLineItem, lineItem);
//			    String autoReverseLineItemId = autoReverseJournalEntryId + "-" + StringUtils.leftPad("" + i, 3, "00");
//			    autoReverseLineItem.setLineItemId(autoReverseLineItemId);
//			    autoReverseLineItem.setCredit(lineItem.getDebit());
//			    autoReverseLineItem.setDebit(lineItem.getCredit());
//			    autoReverseLineItems.add(autoReverseLineItem);
//			    autoReverseLineItemList.add(autoReverseLineItem);
//			    jeCredit += autoReverseLineItem.getCredit();
//			    jeDebit += autoReverseLineItem.getDebit();
//			    session.setAttribute("line", autoReverseLineItem);
//			    i++;
//			}
//			autoReverseJournalEntry.setLineItemSet(autoReverseLineItems);
//			autoReverseJournalEntry.setCredit(jeCredit);
//			autoReverseJournalEntry.setDebit(jeDebit);
//			for (JournalEntry journalEntryInSet : (Set<JournalEntry>) batchToSession.getJournalEntrySet()) {
//			    if (journalEntryInSet.equals(originalJournalEntry)) {
//				journalEntryInSet.setFlag("A");
//			    }
//			}
//			batchToSession.getJournalEntrySet().add(autoReverseJournalEntry);
//			batchToSession.setTotalCredit(batchToSession.getTotalCredit() + autoReverseJournalEntry.getCredit());
//			batchToSession.setTotalDebit(batchToSession.getTotalDebit() + autoReverseJournalEntry.getDebit());
//			session.setAttribute("autoReverseLineItemList", autoReverseLineItemList);
//			session.setAttribute("lineItemList", autoReverseLineItemList);
//			session.setAttribute("journalEntry", autoReverseJournalEntry);
//			session.setAttribute("batch", batchToSession);
//			session.removeAttribute("journal");
//			batchList = new DBUtil().batchList();
//			batchList.remove(batchToSession);
//			batchList.add(batchToSession);
//			session.setAttribute("batchList", batchList);
//		    }
//		}
//		if (session.getAttribute("search") != null) {
//		    session.removeAttribute("search");
//		}
//		forwardName = "go";
//	    } else if (buttonValue.equals("onchangeline")) {
//		forwardName = "go";
//	    } else if (buttonValue.equals("copy")) {
//		if (session.getAttribute("search") != null) {
//		    session.removeAttribute("search");
//		}
//		Batch batch1 = new Batch();
//		Double credit = 0.00;
//		Double debit = 0.00;
//		Double batchCredit = 0.00;
//		Double batchDebit = 0.00;
//		BatchDAO batchDAO = new BatchDAO();
//		if (session.getAttribute("batchList") != null) {
//		    batchList = (List) session.getAttribute("batchList");
//		}
//		for (int i = 0; i < batchList.size(); i++) {
//		    batch1 = (Batch) batchList.get(i);
//		    if (batch1.getBatchId().toString().equals(journalEntryForm.getBatch())) {
//			batchCredit = 0.00;
//			batchDebit = 0.00;
//			if (batch1.getJournalEntrySet() != null) {
//			    Iterator biter = batch1.getJournalEntrySet().iterator();
//			    while (biter.hasNext()) {
//				JournalEntry je = (JournalEntry) biter.next();
//				if (je.getJournalEntryId().equals(journalEntryForm.getJournalid())) {
//				    credit = 0000000000.00;
//				    debit = 0000000000.00;
//				    if (je.getLineItemSet() != null) {
//					Iterator liter = je.getLineItemSet().iterator();
//					while (liter.hasNext()) {
//					    LineItem lt = (LineItem) liter.next();
//					    if (lt.getAccount() == null || lt.getAccount().trim().equals("")) {
//						liter.remove();
//					    } else {
//						if (lt.getDebit() != null) {
//						    debit = debit + lt.getDebit();
//						}
//						if (lt.getCredit() != null) {
//						    credit = credit + lt.getCredit();
//						}
//						lt.setJournalEntryId(je.getJournalEntryId());
//					    }
//					}
//				    }
//				    je.setCredit(credit);
//				    je.setDebit(debit);
//				    batchCredit = batchCredit + je.getCredit();
//				    batchDebit = batchDebit + je.getDebit();
//				}
//			    }
//			}
//			batch1.setTotalCredit(batchCredit);
//			batch1.setTotalDebit(batchDebit);
//			batchDAO.save(batch1);
//		    }
//		}
//		lineItemList = new ArrayList();
//		journalEntryList = new ArrayList();
//		List jList = new ArrayList();
//		HashMap hashMap = new HashMap();
//		if (session.getAttribute("batchList") != null) {
//		    batchList = (List) session.getAttribute("batchList");
//		    for (int i = 0; i < batchList.size(); i++) {
//			batch1 = (Batch) batchList.get(i);
//			if (batch1.getBatchId().equals(batchId.toString())) {
//			    session.setAttribute("batch", batch1);
//			    if (batch1.getJournalEntrySet() != null) {
//				Iterator iter = (Iterator) batch1.getJournalEntrySet().iterator();
//				while (iter.hasNext()) {
//				    JournalEntry j1 = (JournalEntry) iter.next();
//				    hashMap.put(j1.getJournalEntryId(), j1);
//				    jList.add(j1.getJournalEntryId());
//				}
//			    }
//			}
//		    }
//		}
//		Collections.sort(jList);
//		for (int i = 0; i < jList.size(); i++) {
//		    JournalEntry jEntry = (JournalEntry) hashMap.get(jList.get(i));
//		    journalEntryList.add(jEntry);
//		}
//		for (int i = 0; i < journalEntryList.size(); i++) {
//		    JournalEntry j1 = (JournalEntry) journalEntryList.get(i);
//		    if (j1.getJournalEntryId().equals(journalId)) {
//			session.setAttribute("journalEntry", j1);
//			if (j1.getLineItemSet() != null) {
//			    Iterator iter1 = (Iterator) j1.getLineItemSet().iterator();
//			    while (iter1.hasNext()) {
//				LineItem l1 = (LineItem) iter1.next();
//				lineItemList.add(l1);
//			    }
//			}
//			break;
//		    }
//		}
//		session.setAttribute("lineItemList", lineItemList);
//		forwardName = "go";
//	    } else if (buttonValue.equals("batchdec")) {
//		if (session.getAttribute("batchList") != null) {
//		    batchList = (List) session.getAttribute("batchList");
//		}
//		if (batchList.size() > 0) {
//		    Batch b1 = (Batch) batchList.get(0);
//		    if (batchId.equals(b1.getBatchId())) {
//			message = "No Records Found";
//			request.setAttribute("message", message);
//
//		    } else {
//			HashMap hashMap = new HashMap();
//			List jList = new ArrayList();
//			HashMap lmap = new HashMap();
//			List lList = new ArrayList();
//			Batch batch = new Batch();
//			for (int i = 0; i < batchList.size(); i++) {
//			    Batch b3 = (Batch) batchList.get(i);
//			    if (b3.getBatchId().equals(batchId)) {
//				batch = (Batch) batchList.get(i - 1);
//				break;
//			    }
//			}
//			if (batch != null) {
//			    session.setAttribute("batch", batch);
//			    if (batch.getJournalEntrySet() != null) {
//				Iterator iter = batch.getJournalEntrySet().iterator();
//				while (iter.hasNext()) {
//				    JournalEntry journal = (JournalEntry) iter.next();
//				    hashMap.put(journal.getJournalEntryId(), journal);
//				    jList.add(journal.getJournalEntryId());
//				    if (journal != null) {
//					if (journal.getLineItemSet() != null) {
//					    Iterator iter1 = journal.getLineItemSet().iterator();
//					    while (iter1.hasNext()) {
//						LineItem lineItem = (LineItem) iter1.next();
//						lmap.put(lineItem.getLineItemId(), lineItem);
//						lList.add(lineItem.getLineItemId());
//					    }
//					    Collections.sort(lList);
//					    lineItemList = new ArrayList();
//					    for (int i = 0; i < lList.size(); i++) {
//						LineItem lItem = (LineItem) lmap.get(lList.get(i));
//						lineItemList.add(lItem);
//					    }
//					}
//				    }
//				}
//				session.setAttribute("lineItemList", lineItemList);
//				Collections.sort(jList);
//				for (int i = 0; i < jList.size(); i++) {
//				    String jid = (String) jList.get(i);
//				    JournalEntry journal = (JournalEntry) hashMap.get(jid);
//				    journalEntryList.add(journal);
//				}
//				session.setAttribute("journalEntryList", journalEntryList);
//			    }
//			    if (journalEntryList.size() > 0) {
//				journalEntry = (JournalEntry) journalEntryList.get(journalEntryList.size() - 1);
//				session.setAttribute("journalEntry", journalEntry);
//			    }
//			    if (journalEntryList == null || journalEntryList.isEmpty()) {
//				if (session.getAttribute("journalEntry") != null) {
//				    session.removeAttribute("journalEntry");
//				}
//			    }
//			}
//		    }
//		}
//		if (session.getAttribute("line") != null) {
//		    session.removeAttribute("line");
//		}
//		if (session.getAttribute("search") != null) {
//		    session.removeAttribute("search");
//		}
//		forwardName = "go";
//	    } else if (buttonValue.equals("lastbatchdec")) {
//		if (session.getAttribute("batchList") != null) {
//		    batchList = (List) session.getAttribute("batchList");
//		}
//		if (batchList != null && batchList.size() > 0) {
//		    Batch batch = (Batch) batchList.get(0);
//		    if (batchId.equals(batch.getBatchId())) {
//			message = "No Records Found";
//			request.setAttribute("message", message);
//
//		    } else {
//			HashMap hashMap = new HashMap();
//			List jList = new ArrayList();
//			HashMap lmap = new HashMap();
//			List lList = new ArrayList();
//			if (batch != null) {
//			    if (batch.getJournalEntrySet() != null) {
//				Iterator iter = batch.getJournalEntrySet().iterator();
//				while (iter.hasNext()) {
//				    JournalEntry journal = (JournalEntry) iter.next();
//				    hashMap.put(journal.getJournalEntryId(), journal);
//				    jList.add(journal.getJournalEntryId());
//				    if (journal != null) {
//					if (journal.getLineItemSet() != null) {
//					    Iterator iter1 = journal.getLineItemSet().iterator();
//					    while (iter1.hasNext()) {
//						LineItem lineItem = (LineItem) iter1.next();
//						lmap.put(lineItem.getLineItemId(), lineItem);
//						lList.add(lineItem.getLineItemId());
//					    }
//					    Collections.sort(lList);
//					    for (int i = 0; i < lList.size(); i++) {
//						LineItem litem = (LineItem) lmap.get(lList.get(i));
//						lineItemList.add(litem);
//					    }
//					    session.setAttribute("lineItemList", lineItemList);
//					}
//				    }
//				}
//				Collections.sort(jList);
//				for (int i = 0; i < jList.size(); i++) {
//				    String jid = (String) jList.get(i);
//				    JournalEntry journal = (JournalEntry) hashMap.get(jid);
//				    journalEntryList.add(journal);
//				}
//				session.setAttribute("journalEntryList", journalEntryList);
//			    }
//			    if (journalEntryList.size() > 0) {
//				journalEntry = (JournalEntry) journalEntryList.get(journalEntryList.size() - 1);
//				session.setAttribute("journalEntry", journalEntry);
//			    }
//			    if (journalEntryList == null || journalEntryList.isEmpty()) {
//				if (session.getAttribute("journalEntry") != null) {
//				    session.removeAttribute("journalEntry");
//				}
//			    }
//			}
//		    }
//		    session.setAttribute("batch", batch);
//		}
//		if (session.getAttribute("line") != null) {
//		    session.removeAttribute("line");
//		}

//		if (session.getAttribute("search") != null) {
//		    session.removeAttribute("search");
//		}
//		forwardName = "go";
//	    } else if (buttonValue.equals("batchinc")) {
//		if (session.getAttribute("batchList") != null) {
//		    batchList = (List) session.getAttribute("batchList");
//		}
//		if (batchList != null && batchList.size() > 0) {
//		    Batch b2 = (Batch) batchList.get(batchList.size() - 1);
//		    if (batchId.equals(b2.getBatchId())) {
//			message = "No Records Found";
//			request.setAttribute("message", message);
//
//		    } else {
//			HashMap hashMap = new HashMap();
//			List jList = new ArrayList();
//			HashMap lmap = new HashMap();
//			List lList = new ArrayList();
//			Batch batch = new Batch();
//			for (int i = 0; i < batchList.size(); i++) {
//			    Batch b3 = (Batch) batchList.get(i);
//			    if (b3.getBatchId().equals(batchId)) {
//				batch = (Batch) batchList.get(i + 1);
//				break;
//			    }
//			}
//			if (batch != null) {
//			    session.setAttribute("batch", batch);
//			    if (batch.getJournalEntrySet() != null) {
//				Iterator iter = batch.getJournalEntrySet().iterator();
//				while (iter.hasNext()) {
//				    JournalEntry journal = (JournalEntry) iter.next();
//				    hashMap.put(journal.getJournalEntryId(), journal);
//				    jList.add(journal.getJournalEntryId());
//				    if (journal != null) {
//					if (journal.getLineItemSet() != null) {
//					    Iterator iter1 = journal.getLineItemSet().iterator();
//					    while (iter1.hasNext()) {
//						LineItem lineItem = (LineItem) iter1.next();
//						lmap.put(lineItem.getLineItemId(), lineItem);
//						lList.add(lineItem.getLineItemId());
//					    }
//					}
//					Collections.sort(lList);
//					lineItemList = new ArrayList();
//					for (int i = 0; i < lList.size(); i++) {
//					    LineItem litem = (LineItem) lmap.get(lList.get(i));
//					    lineItemList.add(litem);
//					}
//					session.setAttribute("lineItemList", lineItemList);
//				    }
//				}
//				Collections.sort(jList);
//				for (int i = 0; i < jList.size(); i++) {
//				    JournalEntry jEntry = (JournalEntry) hashMap.get(jList.get(i));
//				    journalEntryList.add(jEntry);
//				}
//				session.setAttribute("journalEntryList", journalEntryList);
//			    } else {
//				session.removeAttribute("journalEntry");
//			    }
//			    if (journalEntryList.size() > 0) {
//				journalEntry = (JournalEntry) journalEntryList.get(journalEntryList.size() - 1);
//				session.setAttribute("journalEntry", journalEntry);
//			    } else {
//				session.removeAttribute("journalEntry");
//			    }
//			    if (journalEntryList == null || journalEntryList.isEmpty()) {
//				if (session.getAttribute("journalEntry") != null) {
//				    session.removeAttribute("journalEntry");
//				}
//			    }
//			}
//		    }
//		}
//		if (session.getAttribute("line") != null) {
//		    session.removeAttribute("line");
//		}
//		if (session.getAttribute("search") != null) {
//		    session.removeAttribute("search");
//		}
//		forwardName = "go";
//	    } else if (buttonValue.equals("lastbatchinc")) {
//		HashMap hashMap = new HashMap();
//		List jList = new ArrayList();
//		HashMap lmap = new HashMap();
//		List lList = new ArrayList();
//		if (session.getAttribute("batchList") != null) {
//		    batchList = (List) session.getAttribute("batchList");
//		}
//		if (batchList != null && batchList.size() > 0) {
//		    Batch b2 = (Batch) batchList.get(batchList.size() - 1);
//		    if (batchId.equals(b2.getBatchId())) {
//			message = "No Records Found";
//			request.setAttribute("message", message);
//		    } else {
//			if (batchList.size() > 0) {
//			    Batch batch = (Batch) batchList.get(batchList.size() - 1);
//			    session.setAttribute("batch", batch);
//			    if (batch != null) {
//				session.setAttribute("batch", batch);
//				if (batch.getJournalEntrySet() != null) {
//				    Iterator iter = batch.getJournalEntrySet().iterator();
//				    while (iter.hasNext()) {
//					JournalEntry journal = (JournalEntry) iter.next();
//					hashMap.put(journal.getJournalEntryId(), journal);
//					jList.add(journal.getJournalEntryId());
//
//				    }
//				    Collections.sort(jList);
//				    for (int i = 0; i < jList.size(); i++) {
//					JournalEntry jEntry = (JournalEntry) hashMap.get(jList.get(i));
//					journalEntryList.add(jEntry);
//				    }
//				    session.setAttribute("journalEntryList", journalEntryList);
//				} else {
//				    session.removeAttribute("journalEntry");
//				}
//				lineItemList = new ArrayList();
//				if (journalEntryList.size() > 0) {
//				    journalEntry = (JournalEntry) journalEntryList.get(0);
//
//				    if (journalEntry.getLineItemSet() != null) {
//					Iterator iter1 = journalEntry.getLineItemSet().iterator();
//					while (iter1.hasNext()) {
//					    LineItem lineItem = (LineItem) iter1.next();
//					    lmap.put(lineItem.getLineItemId(), lineItem);
//					    lList.add(lineItem.getLineItemId());
//					}
//					Collections.sort(lList);
//					for (int i = 0; i < lList.size(); i++) {
//					    LineItem lItem = (LineItem) lmap.get(lList.get(i));
//					    lineItemList.add(lItem);
//					}
//
//					session.setAttribute("lineItemList", lineItemList);
//				    }
//				    session.setAttribute("journalEntry", journalEntry);
//				} else {
//				    session.removeAttribute("journalEntry");
//				}
//				if (journalEntryList == null || journalEntryList.isEmpty()) {
//				    if (session.getAttribute("journalEntry") != null) {
//					session.removeAttribute("journalEntry");
//				    }
//				}
//			    }
//			}
//		    }
//		}
//		if (session.getAttribute("line") != null) {
//		    session.removeAttribute("line");
//		}
//		if (session.getAttribute("search") != null) {
//		    session.removeAttribute("search");
//		}
//		forwardName = "go";
//	    } else if (buttonValue.equals("journaldec")) {
//		HashMap hashMap = new HashMap();
//		List jList = new ArrayList();
//		HashMap lmap = new HashMap();
//		List lList = new ArrayList();
//		if (session.getAttribute("batch") != null) {
//		    session.removeAttribute("batch");
//		}
//		Batch batch = new Batch();
//		if (session.getAttribute("batchList") != null) {
//		    batchList = (List) session.getAttribute("batchList");
//		}
//		for (int i = 0; i < batchList.size(); i++) {
//		    Batch b1 = (Batch) batchList.get(i);
//		    if (b1.getBatchId().equals(batchId)) {
//			session.setAttribute("batch", b1);
//		    }
//		}
//		if (session.getAttribute("batch") != null) {
//		    batch = (Batch) session.getAttribute("batch");
//		}
//		if (batch.getJournalEntrySet() != null) {
//		    Iterator iter1 = batch.getJournalEntrySet().iterator();
//		    while (iter1.hasNext()) {
//			JournalEntry journalEntry1 = (JournalEntry) iter1.next();
//			hashMap.put(journalEntry1.getJournalEntryId(), journalEntry1);
//			jList.add(journalEntry1.getJournalEntryId());
//		    }
//		    Collections.sort(jList);
//		    for (int i = 0; i < jList.size(); i++) {
//			JournalEntry journal = (JournalEntry) hashMap.get(jList.get(i));
//			journalEntryList.add(journal);
//		    }
//		    if (journalEntryList.size() > 0) {
//			JournalEntry journal1 = (JournalEntry) journalEntryList.get(0);
//			if (journal1.getJournalEntryId().equals(journalId)) {
//			    message = "No Records Found";
//			    request.setAttribute("message", message);
//			} else {
//			    for (int i = 0; i < journalEntryList.size(); i++) {
//				JournalEntry j1 = (JournalEntry) journalEntryList.get(i);
//				if (j1.getJournalEntryId().equals(journalId)) {
//				    JournalEntry jEntry = (JournalEntry) journalEntryList.get(i - 1);
//				    session.setAttribute("journalEntry", jEntry);
//				}
//			    }
//			    if (session.getAttribute("line") != null) {
//				session.removeAttribute("line");
//			    }
//			    if (session.getAttribute("journalEntry") != null) {
//				journal1 = (JournalEntry) session.getAttribute("journalEntry");
//			    }
//			    if (journal1 != null) {
//				if (journal1.getLineItemSet() != null) {
//				    Iterator iter2 = journal1.getLineItemSet().iterator();
//				    while (iter2.hasNext()) {
//					LineItem lineItem = (LineItem) iter2.next();
//					lmap.put(lineItem.getLineItemId(), lineItem);
//					lList.add(lineItem.getLineItemId());
//
//				    }
//				    Collections.sort(lList);
//				    for (int j = 0; j < lList.size(); j++) {
//					LineItem lItem = (LineItem) lmap.get(lList.get(j));
//					lineItemList.add(lItem);
//				    }
//				    session.setAttribute("lineItemList", lineItemList);
//				}
//			    }
//			}
//		    }
//		}
//		if (batch.getJournalEntrySet() != null && batch.getJournalEntrySet().size() == 0) {
//		    if (session.getAttribute("journalEntry") != null) {
//			session.removeAttribute("journalEntry");
//		    }
//		}
//		if (session.getAttribute("line") != null) {
//		    session.removeAttribute("line");
//		}
//		if (session.getAttribute("search") != null) {
//		    session.removeAttribute("search");
//		}
//		forwardName = "go";
//	    } else if (buttonValue.equals("lastjournaldec")) {
//		HashMap hashMap = new HashMap();
//		List jList = new ArrayList();
//		HashMap lmap = new HashMap();
//		List lList = new ArrayList();
//		List batchList1 = new ArrayList();
//		Batch batch = new Batch();
//		if (session.getAttribute("batchList") != null) {
//		    batchList1 = (List) session.getAttribute("batchList");
//		    for (int i = 0; i < batchList1.size(); i++) {
//			Batch b1 = (Batch) batchList1.get(i);
//			if (b1.getBatchId().equals(batchId)) {
//			    session.setAttribute("batch", b1);
//			}
//		    }
//		}
//		if (session.getAttribute("batch") != null) {
//		    batch = (Batch) session.getAttribute("batch");
//		}
//		if (batch.getJournalEntrySet() != null) {
//		    Iterator iter1 = batch.getJournalEntrySet().iterator();
//		    while (iter1.hasNext()) {
//			JournalEntry journalEntry1 = (JournalEntry) iter1.next();
//			hashMap.put(journalEntry1.getJournalEntryId(), journalEntry1);
//			jList.add(journalEntry1.getJournalEntryId());
//		    }
//		    Collections.sort(jList);
//		    for (int i = 0; i < jList.size(); i++) {
//			JournalEntry jEntry = (JournalEntry) hashMap.get(jList.get(i));
//			journalEntryList.add(jEntry);
//		    }
//		    if (journalEntryList.size() > 0) {
//			JournalEntry journal1 = (JournalEntry) journalEntryList.get(0);
//			if (journal1.getJournalEntryId().equals(journalId)) {
//			    message = "No Records Found";
//			    request.setAttribute("message", message);
//			} else {
//			    session.setAttribute("journalEntry", journal1);
//			    if (journal1.getLineItemSet() != null) {
//				Iterator iter2 = journal1.getLineItemSet().iterator();
//				while (iter2.hasNext()) {
//				    LineItem lineItem = (LineItem) iter2.next();
//				    lmap.put(lineItem.getLineItemId(), lineItem);
//				    lList.add(lineItem.getLineItemId());
//				}
//				Collections.sort(lList);
//				for (int j = 0; j < lList.size(); j++) {
//				    LineItem lItem = (LineItem) lmap.get(lList.get(j));
//				    lineItemList.add(lItem);
//				}
//				session.setAttribute("lineItemList", lineItemList);
//			    }
//			}
//		    }
//		}
//		if (session.getAttribute("line") != null) {
//		    session.removeAttribute("line");
//		}
//		if (session.getAttribute("search") != null) {
//		    session.removeAttribute("search");
//		}
//		if (batch.getJournalEntrySet() != null && batch.getJournalEntrySet().size() == 0) {
//		    if (session.getAttribute("journalEntry") != null) {
//			session.removeAttribute("journalEntry");
//		    }
//		}
//		forwardName = "go";
//	    } else if (buttonValue.equals("journalinc")) {
//		HashMap hashMap = new HashMap();
//		List jList = new ArrayList();
//		HashMap lmap = new HashMap();
//		List lList = new ArrayList();
//		Batch batch = new Batch();
//		JournalEntry journal = new JournalEntry();
//		if (session.getAttribute("batch") != null) {
//		    session.removeAttribute("batch");
//		}
//		if (session.getAttribute("batchList") != null) {
//		    batchList = (List) session.getAttribute("batchList");
//		    for (int i = 0; i < batchList.size(); i++) {
//			Batch b1 = (Batch) batchList.get(i);
//			if (b1.getBatchId().equals(batchId)) {
//			    session.setAttribute("batch", b1);
//			}
//		    }
//		}
//		if (session.getAttribute("batch") != null) {
//		    batch = (Batch) session.getAttribute("batch");
//		}
//		if (batch.getJournalEntrySet() != null) {
//		    Iterator iter1 = batch.getJournalEntrySet().iterator();
//		    while (iter1.hasNext()) {
//			JournalEntry journalEntry1 = (JournalEntry) iter1.next();
//			hashMap.put(journalEntry1.getJournalEntryId(), journalEntry1);
//			jList.add(journalEntry1.getJournalEntryId());
//
//		    }
//		    Collections.sort(jList);
//		    for (int i = 0; i < jList.size(); i++) {
//			JournalEntry jEntry = (JournalEntry) hashMap.get(jList.get(i));
//			journalEntryList.add(jEntry);
//		    }
//		    if (journalEntryList.size() > 0) {
//			JournalEntry journal1 = (JournalEntry) journalEntryList.get(journalEntryList.size() - 1);
//			if (journal1.getJournalEntryId().equals(journalId)) {
//			    message = "No Records Found";
//			    request.setAttribute("message", message);
//			} else {
//			    for (int i = 0; i < journalEntryList.size(); i++) {
//				JournalEntry j1 = (JournalEntry) journalEntryList.get(i);
//				if (j1.getJournalEntryId().equals(journalId)) {
//				    JournalEntry jEntry = (JournalEntry) journalEntryList.get(i + 1);
//				    session.setAttribute("journalEntry", jEntry);
//				}
//			    }
//			    if (session.getAttribute("line") != null) {
//				session.removeAttribute("line");
//			    }
//			    if (session.getAttribute("journalEntry") != null) {
//				journal = (JournalEntry) session.getAttribute("journalEntry");
//			    }
//			    if (journal != null) {
//				int jid = Integer.parseInt(dbUtil.batchid(journal.getJournalEntryId()));
//				if (batchId.equals(String.valueOf(jid))) {
//				    if (journal.getLineItemSet() != null) {
//					Iterator iter2 = journal.getLineItemSet().iterator();
//					while (iter2.hasNext()) {
//					    LineItem lineItem = (LineItem) iter2.next();
//					    lmap.put(lineItem.getLineItemId(), lineItem);
//					    lList.add(lineItem.getLineItemId());
//					}
//					Collections.sort(lList);
//					for (int i = 0; i < lList.size(); i++) {
//					    LineItem lItem = (LineItem) lmap.get(lList.get(i));
//					    lineItemList.add(lItem);
//					}
//					session.setAttribute("lineItemList", lineItemList);
//				    }
//
//				}
//			    }
//			}
//		    }
//		}
//		if (session.getAttribute("line") != null) {
//		    session.removeAttribute("line");
//		}
//		if (session.getAttribute("search") != null) {
//		    session.removeAttribute("search");
//		}
//		if (batch.getJournalEntrySet() != null && batch.getJournalEntrySet().size() == 0) {
//		    if (session.getAttribute("journalEntry") != null) {
//			session.removeAttribute("journalEntry");
//		    }
//		}
//		forwardName = "go";
//	    } else if (buttonValue.equals("lastjournalinc")) {
//		HashMap hashMap = new HashMap();
//		List jList = new ArrayList();
//		HashMap lmap = new HashMap();
//		List lList = new ArrayList();
//		Batch batch = new Batch();
//		List batchList1 = new ArrayList();
//		if (session.getAttribute("batchList") != null) {
//		    batchList1 = (List) session.getAttribute("batchList");
//		}
//		for (int i = 0; i < batchList1.size(); i++) {
//		    Batch b1 = (Batch) batchList1.get(i);
//		    if (b1.getBatchId().equals(batchId)) {
//			session.setAttribute("batch", b1);
//		    }
//		}
//		if (session.getAttribute("batch") != null) {
//		    batch = (Batch) session.getAttribute("batch");
//		}
//		if (batch.getJournalEntrySet() != null) {
//		    Iterator iter1 = batch.getJournalEntrySet().iterator();
//		    while (iter1.hasNext()) {
//			JournalEntry journalEntry1 = (JournalEntry) iter1.next();
//			hashMap.put(journalEntry1.getJournalEntryId(), journalEntry1);
//			jList.add(journalEntry1.getJournalEntryId());
//		    }
//		    Collections.sort(jList);
//		    for (int i = 0; i < jList.size(); i++) {
//			JournalEntry jEntry = (JournalEntry) hashMap.get(jList.get(i));
//			journalEntryList.add(jEntry);
//		    }
//		    if (journalEntryList.size() > 0) {
//			JournalEntry journal1 = (JournalEntry) journalEntryList.get(journalEntryList.size() - 1);
//			if (journal1.getJournalEntryId().equals(journalId)) {
//			    message = "No Records Found";
//			    request.setAttribute("message", message);
//			} else {
//			    session.setAttribute("journalEntry", journal1);
//			    if (journal1.getLineItemSet() != null) {
//				Iterator iter2 = journal1.getLineItemSet().iterator();
//				while (iter2.hasNext()) {
//				    LineItem lineItem = (LineItem) iter2.next();
//				    lmap.put(lineItem.getLineItemId(), lineItem);
//				    lList.add(lineItem.getLineItemId());
//				}
//				Collections.sort(lList);
//				for (int j = 0; j < lList.size(); j++) {
//				    LineItem lItem = (LineItem) lmap.get(lList.get(j));
//				    lineItemList.add(lItem);
//				}
//				session.setAttribute("lineItemList", lineItemList);
//			    }
//			}
//		    }
//		}
//		if (session.getAttribute("line") != null) {
//		    session.removeAttribute("line");
//		}
//		if (session.getAttribute("search") != null) {
//		    session.removeAttribute("search");
//		}
//		if (batch.getJournalEntrySet() != null && batch.getJournalEntrySet().size() == 0) {
//		    if (session.getAttribute("journalEntry") != null) {
//			session.removeAttribute("journalEntry");
//		    }
//		}
//		forwardName = "go";
//	    } else if (buttonValue.equals("addbatch")) {
//		if (session.getAttribute("batch") != null) {
//		    session.removeAttribute("batch");
//		}
//		if (session.getAttribute("journalEntry") != null) {
//		    session.removeAttribute("journalEntry");
//		}
//		if (session.getAttribute("batchList") != null) {
//		    batchList = (List) session.getAttribute("batchList");
//		}
//		BatchDAO batchDAO = new BatchDAO();
//		batchId = batchDAO.getMaxBatchNumber();
//		if (CommonUtils.isNotEmpty(batchId)) {
//		    batchId = "" + (Integer.parseInt(batchId) + 1);
//		} else {
//		    batchId = "10000";
//		}
//		Batch batch = new Batch();
//		batch.setBatchId(batchId);
//		batch.setStatus("open");
//		batch.setType("manual");
//		batch.setTotalCredit(0.00);
//		batch.setTotalDebit(0.00);
//		journalEntry = new JournalEntry();
//		journalEntry.setJournalEntryId(batchId + "-001");
//		FiscalPeriod fiscalPeriod = dbUtil.getPeriodForCurrentDate();
//		if (fiscalPeriod != null) {
//		    journalEntry.setPeriod(fiscalPeriod.getPeriodDis());
//		}
//		journalEntry.setJeDate(new Date());
//		String jesourcecode = "11285";
//		GenericCode gen1 = genericCodeDAO.findById(Integer.parseInt(jesourcecode));
//		journalEntry.setSourceCode(gen1);
//		journalEntry.setDebit(0.00);
//		journalEntry.setCredit(0.00);
//		lineItemList = new ArrayList();
//		for (int i = 1; i < 10; i++) {
//		    LineItem lineItem = new LineItem();
//		    lineItem.setDebit(0.00);
//		    lineItem.setCredit(0.00);
//		    lineItem.setLineItemId(journalEntry.getJournalEntryId() + "-00" + i);
//		    lineItem.setCurrency("USD");
//		    lineItemList.add(lineItem);
//		}
//		LineItem l1 = new LineItem();
//		l1.setDebit(0.00);
//		l1.setCredit(0.00);
//		l1.setLineItemId(journalEntry.getJournalEntryId() + "-010");
//		l1.setCurrency("USD");
//		lineItemList.add(l1);
//		Set lineItemSet = new LinkedHashSet<LineItem>();
//		for (Iterator iter = lineItemList.iterator(); iter.hasNext();) {
//		    LineItem lineItem = (LineItem) iter.next();
//		    lineItemSet.add(lineItem);
//		}
//		journalEntry.setLineItemSet(lineItemSet);
//		Set journalEntrySet = new LinkedHashSet<JournalEntry>();
//		journalEntrySet.add(journalEntry);
//		batch.setJournalEntrySet(journalEntrySet);
//		batchDAO.save(batch);
//		request.setAttribute("message", "Batch " + batch.getBatchId() + " saved successfully");
//		if (session.getAttribute("line") != null) {
//		    session.removeAttribute("line");
//		}
//		session.setAttribute("batch", batch);
//		session.setAttribute("journalEntry", journalEntry);
//		journalEntryList = new ArrayList();
//		journalEntryList.add(journalEntry);
//		session.setAttribute("journalEntryList", journalEntryList);
//		session.setAttribute("lineItemList", lineItemList);
//		batchList.add(batch);
//		session.setAttribute("batchList", batchList);
//		if (session.getAttribute("search") != null) {
//		    session.removeAttribute("search");
//		}
//		forwardName = "go";
//	    } else if (buttonValue.equals("accountNo")) {
//	    } else if (buttonValue.equals("journaldelete")) {
//		if (session.getAttribute("batchList") != null) {
//		    batchList = (List) session.getAttribute("batchList");
//		}
//		JournalEntryDAO journalEntryDAO = new JournalEntryDAO();
//		for (int i = 0; i < batchList.size(); i++) {
//		    Batch batch = (Batch) batchList.get(i);
//		    if (batch.getJournalEntrySet() != null) {
//			Iterator iter = batch.getJournalEntrySet().iterator();
//			while (iter.hasNext()) {
//			    JournalEntry j1 = (JournalEntry) iter.next();
//			    if (j1.getJournalEntryId().equals(journalId)) {
//				batch.getJournalEntrySet().remove(j1);
//				journalEntryDAO.delete(j1);
//				batch.setTotalDebit(batch.getTotalDebit() - j1.getDebit());
//				batch.setTotalCredit(batch.getTotalCredit() - j1.getCredit());
//				break;
//			    }
//			}
//		    }
//		}
//	    } else if (buttonValue.equals("saveline")) {
//		BatchDAO batchDAO = new BatchDAO();
//		batchList = new ArrayList();
//		if (session.getAttribute("batchList") != null) {
//		    batchList = (List) session.getAttribute("batchList");
//		}
//		Double credit = 0.00;
//		Double debit = 0.00;
//		Double totalCredit = 0.00;
//		Double totalDebit = 0.00;
//		Batch batch1 = new Batch();
//		for (int i = 0; i < batchList.size(); i++) {
//		    batch1 = (Batch) batchList.get(i);
//		    totalCredit = 0.0;
//		    totalDebit = 0.0;
//		    if (batch1.getJournalEntrySet() != null) {
//			Iterator biter = batch1.getJournalEntrySet().iterator();
//			while (biter.hasNext()) {
//			    JournalEntry je = (JournalEntry) biter.next();
//			    credit = 0.00;
//			    debit = 0.00;
//			    if (je.getLineItemSet() != null) {
//				Iterator liter = je.getLineItemSet().iterator();
//				while (liter.hasNext()) {
//				    LineItem lt = (LineItem) liter.next();
//				    if (lt.getAccount() == null && lt.getAccount().trim().equals("")) {
//					liter.remove();
//				    } else {
//					if (lt.getDebit() != null) {
//					    debit = debit + lt.getDebit();
//					}
//					if (lt.getCredit() != null) {
//					    credit = credit + lt.getCredit();
//					}
//				    }
//				}
//			    }
//			    je.setCredit(credit);
//			    je.setDebit(debit);
//			    totalCredit = totalCredit + je.getCredit();
//			    totalDebit = totalDebit + je.getDebit();
//			}
//		    }
//		    batch1.getSourceLedger();
//		    batch1.setTotalCredit(totalCredit);
//		    batch1.setTotalDebit(totalDebit);
//		    batchDAO.save(batch1);
//		    if (batch1.getBatchId().toString().equals(batchId)) {
//			session.setAttribute("batch", batch1);
//		    }
//		}
//		boolean flag = false;
//		lineItemList = new ArrayList();
//		if (session.getAttribute("lineItemList") != null) {
//		    lineItemList = (List) session.getAttribute("lineItemList");
//		    LineItem l1 = (LineItem) lineItemList.get(lineItemList.size() - 1);
//		    if (l1.getLineItemId().equals(hiddenLineItemId)) {
//			flag = true;
//		    }
//		}
//		if (flag) {
//		    if (session.getAttribute("batchList") != null) {
//			batchList = (List) session.getAttribute("batchList");
//		    }
//		    List litemList = new ArrayList();
//		    if (session.getAttribute("lineItemList") != null) {
//			lineItemList = (List) session.getAttribute("lineItemList");
//			for (int i = 0; i < lineItemList.size(); i++) {
//			    LineItem l2 = (LineItem) lineItemList.get(i);
//			    if (l2.getLineItemId() != null) {
//				String jId1 = String.valueOf(dbUtil.lineitemidid(l2.getLineItemId()));
//				if (jId1.equals(journalId)) {
//				    litemList.add(l2);
//				}
//			    }
//			}
//		    }
//		    String lineItemId = "";
//		    Set lineSet = new HashSet<LineItem>();
//		    for (int i = 0; i < 10; i++) {
//			if (litemList.size() == 0) {
//			    lineItemId = journalId + "-" + "001";
//			    line = new LineItem();
//			    line.setLineItemId(lineItemId);
//			    line.setCurrency("USD");
//
//
//			} else if (litemList != null && litemList.size() > 0) {
//			    LineItem l4 = (LineItem) litemList.get(litemList.size() - 1);
//			    lineItemId = dbUtil.lineitemidid1(l4.getLineItemId());
//			    int l1 = Integer.parseInt(lineItemId);
//			    l1 = l1 + 1;
//			    if (l1 >= 0 && l1 < 10) {
//				lineItemId = journalId + "-" + "00" + l1;
//			    } else if (l1 >= 10 && l1 < 100) {
//				lineItemId = journalId + "-" + "0" + l1;
//			    } else if (l1 >= 100 && l1 < 1000) {
//				lineItemId = journalId + "-" + l1;
//			    }
//			}
//			line = new LineItem();
//			line.setLineItemId(lineItemId);
//			line.setCurrency("USD");
//			line.setCredit(0.00);
//			line.setDebit(0.00);
//			litemList.add(line);
//		    }
//		    session.setAttribute("lineItemList", litemList);
//		    for (int i = 0; i < litemList.size(); i++) {
//			LineItem l11 = (LineItem) litemList.get(i);
//			lineSet.add(l11);
//		    }
//		    if (session.getAttribute("journalEntryList") != null) {
//			journalEntryList = (List) session.getAttribute("journalEntryList");
//		    }
//		    if (journalEntryList.size() > 0) {
//			for (int i = 0; i < journalEntryList.size(); i++) {
//			    JournalEntry j1 = (JournalEntry) journalEntryList.get(i);
//			    if (j1.getJournalEntryId().equals(journalId)) {
//				j1.setLineItemSet(lineSet);
//				session.setAttribute("journalEntry", j1);
//			    }
//			}
//		    }
//		}
//	    } else if (buttonValue.equals("addjournal")) {
//		journalEntryForm.setReverse("off");
//		request.setAttribute("journalEntryForm", journalEntryForm);
//		List journalList = new ArrayList();
//		Batch batch1 = new Batch();
//		Batch batch = new Batch();
//		Double credit = 0.00;
//		Double debit = 0.00;
//		Double batchCredit = 0.00;
//		Double batchDebit = 0.00;
//		BatchDAO batchDAO = new BatchDAO();
//		if (session.getAttribute("batchList") != null) {
//		    batchList = (List) session.getAttribute("batchList");
//		}
//		for (int i = 0; i < batchList.size(); i++) {
//		    batch1 = (Batch) batchList.get(i);
//		    if (batch1.getBatchId().toString().equals(journalEntryForm.getBatch())) {
//			batchCredit = 0.00;
//			batchDebit = 0.00;
//			if (batch1.getJournalEntrySet() != null) {
//			    Iterator biter = batch1.getJournalEntrySet().iterator();
//			    while (biter.hasNext()) {
//				JournalEntry je = (JournalEntry) biter.next();
//				if (je.getJournalEntryId().equals(journalEntryForm.getJournalid())) {
//				    credit = 0000000000.00;
//				    debit = 0000000000.00;
//				    if (je.getLineItemSet() != null) {
//					Iterator liter = je.getLineItemSet().iterator();
//					while (liter.hasNext()) {
//					    LineItem lt = (LineItem) liter.next();
//					    if (lt.getAccount() == null || lt.getAccount().trim().equals("")) {
//						liter.remove();
//					    } else {
//						if (lt.getDebit() != null) {
//						    debit = debit + lt.getDebit();
//						}
//						if (lt.getCredit() != null) {
//						    credit = credit + lt.getCredit();
//						}
//					    }
//					}
//				    }
//				    je.setCredit(credit);
//				    je.setDebit(debit);
//				}
//				batchCredit = batchCredit + je.getCredit();
//				batchDebit = batchDebit + je.getDebit();
//			    }
//			}
//			batch1.setTotalCredit(batchCredit);
//			batch1.setTotalDebit(batchDebit);
//			batchDAO.merge(batch1);
//			request.setAttribute("message", "Journal Entry " + journalEntryForm.getJournalid() + " saved successfully");
//		    }
//		}
//		for (int i = 0; i < batchList.size(); i++) {
//		    Batch b1 = (Batch) batchList.get(i);
//		    if (b1.getBatchId().equals(batchId)) {
//			batch.setBatchId(batchId);
//			batch.setBatchDesc(batchdesc);
//			if (debitTotal == null || debitTotal.equals("")) {
//			    debitTotal = "0.00";
//			}
//			if (debitTotal.contains(",")) {
//			    debitTotal = debitTotal.replace(",", "");
//			}
//			batch.setTotalDebit(batchDebit);
//			if (creditTotal == null || creditTotal.equals("")) {
//			    creditTotal = "0.00";
//			}
//			if (creditTotal.contains(",")) {
//			    creditTotal = creditTotal.replace(",", "");
//			}
//			batch.setTotalCredit(batchCredit);
//			batch.setStatus(status);
//		    }
//		}
//		JournalEntry journal = new JournalEntry();
//		if (session.getAttribute("journalEntryList") != null) {
//		    journalEntryList = (List) session.getAttribute("journalEntryList");
//
//		    for (int i = 0; i < journalEntryList.size(); i++) {
//			JournalEntry j1 = (JournalEntry) journalEntryList.get(i);
//
//			if (j1.getJournalEntryId().length() > 5) {
//			    String jid = dbUtil.batchid(j1.getJournalEntryId());
//
//			    if (jid.equals(String.valueOf(batchId))) {
//				journalList.add(j1);
//			    }
//			}
//		    }
//		}
//		JournalEntryDAO journalEntryDAO = new JournalEntryDAO();
//		//journalList=journalEntryDAO.findByBatchId(batchId);
//
//		String journalId1 = "";
//		if (journalList.size() == 0) {
//		    journalId1 = batchId + "-" + "001";
//		} else if (journalList != null && journalList.size() > 0) {
//		    JournalEntry j2 = (JournalEntry) journalList.get(journalList.size() - 1);
//		    journalId = dbUtil.journalid(j2.getJournalEntryId());
//		    if (journalId.endsWith("R")) {
//			journalId = journalId.substring(0, journalId.length() - 1);
//		    }
//		    int j1 = Integer.parseInt(journalId);
//
//		    j1 = j1 + 1;
//		    if (j1 >= 0 && j1 < 10) {
//			journalId1 = batchId + "-" + "00" + j1;
//		    } else if (j1 >= 10 && j1 < 100) {
//			journalId1 = batchId + "-" + "0" + j1;
//		    } else if (j1 >= 100 && j1 < 1000) {
//			journalId1 = batchId + "-" + j1;
//		    }
//
//		}
//
//		journal.setJournalEntryId(journalId1);
//		Date date = new Date(System.currentTimeMillis());
//		journal.setJeDate(date);
//		journal.setDebit(0.00);
//		journal.setCredit(0.00);
//		FiscalPeriod fiscalPeriod = dbUtil.getPeriodForCurrentDate();
//		if (fiscalPeriod != null) {
//		    journal.setPeriod(fiscalPeriod.getPeriodDis());
//		}
//		List litemList = new ArrayList();
//		if (session.getAttribute("lineItemList") != null) {
//		    lineItemList = (List) session.getAttribute("lineItemList");
//		    for (int i = 0; i < lineItemList.size(); i++) {
//			LineItem l2 = (LineItem) lineItemList.get(i);
//			String jId1 = dbUtil.lineitemidid(l2.getLineItemId());
//			if (jId1.equals(journalId1)) {
//			    litemList.add(l2);
//			}
//		    }
//		}
//		String lineItemId = "";
//		Set lineSet = new HashSet<LineItem>();
//		for (int i = 0; i < 10; i++) {
//		    if (litemList.size() == 0) {
//			lineItemId = journalId1 + "-" + "001";
//			line = new LineItem();
//			line.setLineItemId(lineItemId);
//			line.setCurrency("USD");
//			line.setCredit(null);
//			line.setDebit(null);
//			line.setDate(date);
//			litemList.add(line);
//			lineSet.add(line);
//		    } else if (litemList != null && litemList.size() > 0) {
//			LineItem l4 = (LineItem) litemList.get(litemList.size() - 1);
//			lineItemId = l4.getLineItemId().substring(10, 13);
//			int l1 = Integer.parseInt(lineItemId);
//			l1 = l1 + 1;
//			if (l1 >= 0 && l1 < 10) {
//			    lineItemId = journalId1 + "-" + "00" + l1;
//			} else if (l1 >= 10 && l1 < 100) {
//			    lineItemId = journalId1 + "-" + "0" + l1;
//			} else {
//			    lineItemId = journalId1 + "-" + l1;
//			}
//			line = new LineItem();
//			line.setLineItemId(lineItemId);
//			line.setCurrency("USD");
//			line.setCredit(null);
//			line.setDebit(null);
//			litemList.add(line);
//			lineSet.add(line);
//		    }
//		}
//		journal.setLineItemSet(lineSet);
//		session.setAttribute("lineItemList", litemList);
//		if (session.getAttribute("journalEntryList") != null) {
//		    journalEntryList = (List) session.getAttribute("journalEntryList");
//		}
//		journalEntryList.add(journal);
//		session.setAttribute("journalEntryList", journalEntryList);
//		Set journalSet = new HashSet<JournalEntry>();
//		for (int i = 0; i < journalEntryList.size(); i++) {
//		    JournalEntry jEntry = (JournalEntry) journalEntryList.get(i);
//		    if (jEntry.getJournalEntryId().length() > 5) {
//			if (dbUtil.batchid(jEntry.getJournalEntryId()).equals(String.valueOf(batchId))) {
//			    journalSet.add(jEntry);
//			}
//		    }
//		}
//		if (batchList != null) {
//		    for (int i = 0; i < batchList.size(); i++) {
//			Batch b1 = (Batch) batchList.get(i);
//			if (b1.getBatchId().equals(batchId)) {
//			    b1.setJournalEntrySet(journalSet);
//			}
//		    }
//		}
//		batch.setJournalEntrySet(journalSet);
//		session.setAttribute("journalEntry", journal);
//		session.setAttribute("journalEntryList", journalEntryList);
//		List lineitemTestList = new ArrayList();
//		lineitemTestList = (List) session.getAttribute("lineItemList");
//		for (int k = 0; k < lineitemTestList.size(); k++) {
//		    LineItem l4 = (LineItem) lineitemTestList.get(k);
//		}
//		session.setAttribute("batch", batch);
//		if (session.getAttribute("search") != null) {
//		    session.removeAttribute("search");
//		}
//		forwardName = "go";
//	    } else if (buttonValue.equals("addline")) {
//		Batch batch = new Batch();
//		if (session.getAttribute("batchList") != null) {
//		    batchList = (List) session.getAttribute("batchList");
//		}
//		for (int i = 0; i < batchList.size(); i++) {
//		    batch = (Batch) batchList.get(i);
//		    if (batch.getBatchId().equals(batchId)) {
//			batch.setBatchId(batchId);
//			batch.setBatchDesc(batchdesc);
//			if (debitTotal == null || debitTotal.equals("")) {
//			    debitTotal = "0.00";
//			}
//			batch.setTotalDebit(Double.parseDouble(dbUtil.removecomma(debitTotal)));
//			if (creditTotal == null || creditTotal.equals("")) {
//			    creditTotal = "0.00";
//			}
//			batch.setTotalCredit(Double.parseDouble(dbUtil.removecomma(creditTotal)));
//			batch.setStatus(status);
//			session.setAttribute("batch", batch);
//			break;
//		    }
//		}
//		if (session.getAttribute("lineItemList") != null) {
//		    lineItemList = (List) session.getAttribute("lineItemList");
//		}
//		session.setAttribute("lineItemList", lineItemList);
//		Double credit1 = 0.00;
//		Double debit1 = 0.00;
//		if (lineItemList != null && lineItemList.size() > 0) {
//		    for (int i = 0; i < lineItemList.size(); i++) {
//			LineItem l1 = (LineItem) lineItemList.get(i);
//			if (l1.getLineItemId() != null) {
//			    String je = dbUtil.lineitemidid(l1.getLineItemId());
//			    if (je.equals(journalId)) {
//				if (l1.getCredit() != null) {
//				    credit1 = credit1 + l1.getCredit();
//				}
//				if (l1.getDebit() != null) {
//				    debit1 = debit1 + l1.getDebit();
//				}
//			    }
//			}
//		    }
//		}
//		List litemList = new ArrayList();
//		if (session.getAttribute("lineItemList") != null) {
//		    lineItemList = (List) session.getAttribute("lineItemList");
//		    for (int i = 0; i < lineItemList.size(); i++) {
//			LineItem l2 = (LineItem) lineItemList.get(i);
//			if (l2.getLineItemId() != null) {
//			    String jId1 = String.valueOf(dbUtil.lineitemidid(l2.getLineItemId()));
//			    if (jId1.equals(journalId)) {
//				litemList.add(l2);
//			    }
//			}
//		    }
//		}
//		String lineItemId = "";
//		Set lineSet = new HashSet<LineItem>();
//		for (int i = 0; i < 10; i++) {
//		    if (litemList.size() == 0) {
//			lineItemId = journalId + "-" + "001";
//			line = new LineItem();
//			line.setLineItemId(lineItemId);
//			line.setCurrency("USD");
//		    } else if (litemList != null && litemList.size() > 0) {
//			LineItem l4 = (LineItem) litemList.get(litemList.size() - 1);
//			lineItemId = dbUtil.lineitemidid1(l4.getLineItemId());
//			int l1 = Integer.parseInt(lineItemId);
//			l1 = l1 + 1;
//			if (l1 >= 0 && l1 < 10) {
//			    lineItemId = journalId + "-" + "00" + l1;
//			} else if (l1 >= 10 && l1 < 100) {
//			    lineItemId = journalId + "-" + "0" + l1;
//			} else if (l1 >= 100 && l1 < 1000) {
//			    lineItemId = journalId + "-" + l1;
//			}
//		    }
//		    line = new LineItem();
//		    line.setLineItemId(lineItemId);
//		    line.setCurrency("USD");
//		    line.setCredit(null);
//		    line.setDebit(null);
//		    litemList.add(line);
//		}
//		session.setAttribute("lineItemList", litemList);
//		for (int i = 0; i < litemList.size(); i++) {
//		    LineItem l11 = (LineItem) litemList.get(i);
//		    lineSet.add(l11);
//		}
//		if (session.getAttribute("journalEntryList") != null) {
//		    journalEntryList = (List) session.getAttribute("journalEntryList");
//		}
//		if (journalEntryList.size() > 0) {
//		    for (int i = 0; i < journalEntryList.size(); i++) {
//			JournalEntry j1 = (JournalEntry) journalEntryList.get(i);
//			if (j1.getJournalEntryId().equals(journalId)) {
//			    j1.setLineItemSet(lineSet);
//			    session.setAttribute("journalEntry", j1);
//			}
//		    }
//		}
//		if (batch.getJournalEntrySet() != null) {
//		    Iterator iter = (Iterator) batch.getJournalEntrySet().iterator();
//		    while (iter.hasNext()) {
//			JournalEntry journal1 = (JournalEntry) iter.next();
//			if (journal1.getJournalEntryId().equals(journalId)) {
//			    journal1.setLineItemSet(lineSet);
//			}
//		    }
//		}
//		if (session.getAttribute("line") != null) {
//		    session.removeAttribute("line");
//		}
//		if (session.getAttribute("search") != null) {
//		    session.removeAttribute("search");
//		}
//		if (session.getAttribute("search1") != null) {
//		    session.removeAttribute("search1");
//		}
//	    } else if (buttonValue.equals("savebatch") || buttonValue.equals("saveprint") || buttonValue.equals("print")) {
//		Batch batch = null;
//		Double debit = 0.00;
//		Double credit = 0.00;
//		BatchDAO batchDAO = new BatchDAO();
//		Set jSet = new HashSet<JournalEntry>();
//		if (session.getAttribute("batchList") != null) {
//		    batchList = (List) session.getAttribute("batchList");
//		}
//		Double totalCredit = 0.0;
//		Double totalDebit = 0.0;
//		for (int i = 0; i < batchList.size(); i++) {
//		    batch = new Batch();
//		    batch = (Batch) batchList.get(i);
//		    if (batch.getBatchId().trim().equals(batchId)) {
//			totalCredit = 0.00;
//			totalDebit = 0.00;
//			if (batch.getJournalEntrySet() != null) {
//			    Iterator biter = batch.getJournalEntrySet().iterator();
//			    while (biter.hasNext()) {
//				JournalEntry je = new JournalEntry();
//				je = (JournalEntry) biter.next();
//				if (je.getJournalEntryId().equals(journalId)) {
//				    if (buttonValue.equals("print")) {
//					je.setPeriod(je.getPeriod());
//				    } else {
//					FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
//					List fiscal = fiscalPeriodDAO.getPeriodList3(jeperiod);
//					FiscalPeriod fis = new FiscalPeriod();
//					if (fiscal != null && fiscal.size() > 0) {
//					    fis = (FiscalPeriod) fiscal.get(0);
//					}
//					if (fis != null) {
//					    je.setPeriod(fis.getPeriodDis());
//					}
//
//				    }
//				}
//				je.setBatchId(batch.getBatchId());
//				credit = 0.00;
//				debit = 0.00;
//				if (je.getLineItemSet() != null) {
//				    Iterator liter = je.getLineItemSet().iterator();
//				    newLineSet = new HashSet();
//				    while (liter.hasNext()) {
//					LineItem lt = new LineItem();
//					lt = (LineItem) liter.next();
//					String jeNumber = je.getJournalEntryId();
//					String itemNumber = lt.getLineItemId();
//					if (itemNumber != null & itemNumber.length() == 14) {
//					    itemNumber = itemNumber.substring(0, 10);
//					} else {
//					    itemNumber = itemNumber.substring(0, 9);
//					}
//					if (itemNumber != null && jeNumber != null && itemNumber.equals(jeNumber)) {
//					    if (lt.getAccount() == null || lt.getAccount().trim().equals("")) {
//						liter.remove();
//					    } else {
//						lt.setJournalEntryId(je.getJournalEntryId());
//						newLineSet.add(lt);
//						if (lt.getDebit() != null) {
//						    debit = debit + lt.getDebit();
//						}
//						if (lt.getCredit() != null) {
//						    credit = credit + lt.getCredit();
//						}
//					    }
//					}
//				    }
//				    je.setLineItemSet(null);
//				    je.setLineItemSet(newLineSet);
//				}
//				je.setCredit(credit);
//				je.setDebit(debit);
//				totalCredit = totalCredit + je.getCredit();
//				totalDebit = totalDebit + je.getDebit();
//			    }
//			}
//			batch.setTotalCredit(totalCredit);
//			batch.setTotalDebit(totalDebit);
//			batchDAO.save(batch);
//			break;
//		    }
//		    if (session.getAttribute("search") != null) {
//			session.removeAttribute("search");
//		    }
//		    forwardName = "go";
//		    List lineItems = (List) session.getAttribute("lineItemList");
//		    for (Iterator iterator = lineItems.iterator(); iterator.hasNext();) {
//			LineItem lineItem = (LineItem) iterator.next();
//			if (CommonUtils.isEmpty(lineItem.getAccount())) {
//			    iterator.remove();
//			}
//		    }
//		    session.setAttribute("lineItemList", lineItems);
//		}
//	    } else if (buttonValue.equals("batchdetails")) {
//		if (session.getAttribute("batch") != null) {
//		    session.removeAttribute("batch");
//		}
//		if (session.getAttribute("journalEntry") != null) {
//		    session.removeAttribute("journalEntry");
//		}
//		if (session.getAttribute("lineItemList") != null) {
//		    session.removeAttribute("lineItemList");
//		}
//		String dessc = "";
//
//
//		lineItemList = new ArrayList();
//		journalEntryList = new ArrayList();
//		List jList = new ArrayList();
//		HashMap hashMap = new HashMap();
//		if (session.getAttribute("batchList") != null) {
//		    batchList = (List) session.getAttribute("batchList");
//		    for (int i = 0; i < batchList.size(); i++) {
//			Batch batch1 = (Batch) batchList.get(i);
//			if (batch1.getBatchId().equals(batchId.toString())) {
//			    BatchDAO batchDAO = new BatchDAO();
//			    Batch batch2 = batchDAO.findById(batchId);
//			    session.setAttribute("batch", batch2);
//			    if (batch2.getJournalEntrySet() != null) {
//				Iterator iter = (Iterator) batch2.getJournalEntrySet().iterator();
//				while (iter.hasNext()) {
//				    JournalEntry j1 = (JournalEntry) iter.next();
//				    hashMap.put(j1.getJournalEntryId(), j1);
//				    jList.add(j1.getJournalEntryId());
//				}
//			    }
//			}
//		    }
//		}
//		Collections.sort(jList);
//		for (int i = 0; i < jList.size(); i++) {
//		    JournalEntry jEntry = (JournalEntry) hashMap.get(jList.get(i));
//		    journalEntryList.add(jEntry);
//		}
//		if (journalEntryList.size() > 0) {
//		    JournalEntry j1 = (JournalEntry) journalEntryList.get(journalEntryList.size() - 1);
//		    session.setAttribute("journalEntry", j1);
//		    if (j1.getLineItemSet() != null) {
//			Iterator iter1 = (Iterator) j1.getLineItemSet().iterator();
//			while (iter1.hasNext()) {
//			    LineItem l1 = (LineItem) iter1.next();
//			    lineItemList.add(l1);
//			}
//		    }
//		}
//		session.setAttribute("lineItemList", lineItemList);
//		forwardName = "go";
//	    }
//	    if (buttonValue.equals("print") || buttonValue.equals("saveprint")) {
//		LoadLogisoftProperties loadLogisoftProperties = new LoadLogisoftProperties();
//		String outputFileName = loadLogisoftProperties.getProperty("reportLocation");
//		File file = new File(outputFileName + "/JournalEntry");
//		if (!file.exists()) {
//		    file.mkdir();
//		}
//		outputFileName = outputFileName + "/JournalEntry/" + batchId + ".pdf";
//		JournalEntryBC journalEntryBC = new JournalEntryBC();
//		try {
//		    String realPath = this.getServlet().getServletContext().getRealPath("/");
//		    journalEntryBC.createJournalEntryReport(journalEntryForm.getBatch(), outputFileName, realPath);
//		} catch (Exception e) {
//		    // TODO Auto-generated catch block
//		    e.printStackTrace();
//		}
//		request.setAttribute("fileName", outputFileName);
//		lineItemList = new ArrayList();
//		journalEntryList = new ArrayList();
//		List jList = new ArrayList();
//		HashMap hashMap = new HashMap();
//		if (session.getAttribute("batchList") != null) {
//		    batchList = (List) session.getAttribute("batchList");
//		    for (int i = 0; i < batchList.size(); i++) {
//			Batch batch1 = (Batch) batchList.get(i);
//			if (batch1.getBatchId().equals(batchId.toString())) {
//			    session.setAttribute("batch", batch1);
//			    if (batch1.getJournalEntrySet() != null) {
//				Iterator iter = (Iterator) batch1.getJournalEntrySet().iterator();
//				while (iter.hasNext()) {
//				    JournalEntry j1 = (JournalEntry) iter.next();
//				    hashMap.put(j1.getJournalEntryId(), j1);
//				    jList.add(j1.getJournalEntryId());
//				}
//			    }
//			}
//		    }
//		}
//		Collections.sort(jList);
//		for (int i = 0; i < jList.size(); i++) {
//		    JournalEntry jEntry = (JournalEntry) hashMap.get(jList.get(i));
//		    journalEntryList.add(jEntry);
//		}
//		HashMap lineMap = new HashMap();
//		List lList = new ArrayList();
//		if (journalEntryList.size() > 0) {
//		    JournalEntry j1 = (JournalEntry) journalEntryList.get(journalEntryList.size() - 1);
//		    session.setAttribute("journalEntry", j1);
//		    if (j1.getLineItemSet() != null) {
//			Iterator iter1 = (Iterator) j1.getLineItemSet().iterator();
//			while (iter1.hasNext()) {
//			    LineItem l1 = (LineItem) iter1.next();
//			    lineMap.put(l1.getLineItemId(), l1);
//			    lList.add(l1.getLineItemId());
//			}
//		    }
//		}
//		Collections.sort(lList);
//		for (int i = 0; i < lList.size(); i++) {
//		    LineItem lineItem = (LineItem) lineMap.get(lList.get(i));
//		    lineItemList.add(lineItem);
//		}
//		session.setAttribute("lineItemList", lineItemList);
//		forwardName = "go";
//	    } else if (buttonValue.equals("sourcecode")
//		    || buttonValue.equals("addaccount")) {
//		LineItem line1 = new LineItem();
//		List lineItemList1 = null;
//		Batch batch = new Batch();
//		JournalEntry journal = new JournalEntry();
//		if (session.getAttribute("batchList") != null) {
//		    batchList = (List) session.getAttribute("batchList");
//		    for (int i = 0; i < batchList.size(); i++) {
//			Batch b1 = (Batch) batchList.get(i);
//			if (b1.getBatchId().equals(batchId)) {
//			    session.setAttribute("batch", b1);
//			}
//		    }
//		}
//		batch.setBatchId(batchId);
//		batch.setBatchDesc(batchdesc);
//		if (debitTotal.contains(",")) {
//		    debitTotal = debitTotal.replace(",", "");
//		}
//		batch.setTotalDebit(Double.parseDouble(debitTotal));
//		if (creditTotal.contains(",")) {
//		    creditTotal.replace(",", "");
//		}
//		batch.setTotalCredit(Double.parseDouble(creditTotal));
//		batch.setStatus(status);
//		session.setAttribute("batch", batch);
//		if (session.getAttribute("journalEntry") != null) {
//		    journal = (JournalEntry) session.getAttribute("journalEntry");
//		}
//		journal.setJournalEntryId(journalId);
//		journal.setJournalEntryDesc(journalDesc);
//		if (txtCal != null && !txtCal.equals("")) {
//		    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
//		    java.util.Date javaDate = null;
//		    try {
//			javaDate = sdf.parse(txtCal);
//		    } catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		    }
//		    journal.setJeDate(javaDate);
//		}
//		if (jeperiod != null && !jeperiod.equals("0")) {
//		    FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
//		    List fiscal = fiscalPeriodDAO.getPeriodList3(jeperiod);
//		    FiscalPeriod fis = new FiscalPeriod();
//		    if (fiscal != null && fiscal.size() > 0) {
//			fis = (FiscalPeriod) fiscal.get(0);
//			journal.setPeriod(fis.getPeriodDis());
//		    }
//
//		}
//		if (sourcecode != null && !sourcecode.equals("0")) {
//		    GenericCode genericCode = genericCodeDAO.findById(Integer.parseInt(sourcecode));
//		    journal.setSourceCode(genericCode);
//		    journal.setSourceCodeDesc(genericCode.getCodedesc());
//		}
//		if (jecredit != null && !jecredit.equals("")) {
//		    if (jecredit.contains(",")) {
//			jecredit = jecredit.replace(",", "");
//		    }
//		    journal.setCredit(Double.parseDouble(jecredit));
//		}
//		if (jedebit != null && !jedebit.equals("")) {
//		    if (jedebit.contains(",")) {
//			jedebit = jedebit.replace(",", "");
//		    }
//		    journal.setDebit(Double.parseDouble(jedebit));
//		}
//		journal.setMemo(memo);
//		if (session.getAttribute("journalEntryList") != null) {
//		    journalEntryList = (List) session.getAttribute("journalEntryList");
//		}
//
//		if (session.getAttribute("line") != null) {
//		    line = (LineItem) session.getAttribute("line");
//		}
//		line1.setLineItemId(itemNo);
//		line1.setReference(reference);
//		line1.setReferenceDesc(refDesc);
//		line1.setAccount(account);
//		//line1.setAccountDesc(accountDesc);
//		if (linedebit != null && !linedebit.equals("")) {
//		    if (linedebit.contains(",")) {
//			linedebit = linedebit.replace(",", "");
//		    }
//		    line1.setDebit(Double.parseDouble(linedebit));
//		}
//		if (linecredit != null && !linecredit.equals("")) {
//		    if (linecredit.contains(",")) {
//			linecredit = linecredit.replace(",", "");
//		    }
//		    line1.setCredit(Double.parseDouble(linecredit));
//		}
//		if (jeperiod != null && !jeperiod.equals("0")) {
//		    FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
//		    List fiscal = fiscalPeriodDAO.getPeriodList3(jeperiod);
//		    FiscalPeriod fis = new FiscalPeriod();
//		    if (fiscal != null && fiscal.size() > 0) {
//			fis = (FiscalPeriod) fiscal.get(0);
//		    }
//		    line1.setPeriod(fis);
//		}
//		if (currency != null) {
//		    line1.setCurrency(currency);
//		}
//		Date date = new Date(System.currentTimeMillis());
//		line1.setDate(date);
//		if (session.getAttribute("lineItemList") != null) {
//		    lineItemList1 = (List) session.getAttribute("lineItemList");
//		}
//		session.setAttribute("line", line1);
//		session.setAttribute("lineItemList", lineItemList1);
//		session.setAttribute("batch", batch);
//		session.setAttribute("journalEntry", journal);
//		session.setAttribute("batchList", batchList);
//		if (session.getAttribute("search") != null) {
//		    session.removeAttribute("search");
//		}
//		forwardName = "go";
//	    } else if (buttonValue.equals("delete")) {
//		BatchDAO batchDAO = new BatchDAO();
//		lineItemList = new ArrayList();
//		batchList = new ArrayList();
//		if (session.getAttribute("lineItemList") != null) {
//		    lineItemList = (List) session.getAttribute("lineItemList");
//		}
//		LineItem lDelete = (LineItem) lineItemList.get(Integer.parseInt(index));
//		if (session.getAttribute("batchList") != null) {
//		    batchList = (List) session.getAttribute("batchList");
//		}
//		LineItem lineItem = new LineItem();
//		List itemLIst = new ArrayList();
//		JournalEntry j1 = null;
//		HashMap lMap = new HashMap();
//		for (int i = 0; i < batchList.size(); i++) {
//		    Batch batch = (Batch) batchList.get(i);
//		    if (batch.getBatchId().equals(batchId)) {
//			if (batch.getJournalEntrySet() != null) {
//			    Iterator iter = batch.getJournalEntrySet().iterator();
//			    while (iter.hasNext()) {
//				j1 = (JournalEntry) iter.next();
//				if (j1.getJournalEntryId().equals(journalId)) {
//				    if (j1.getLineItemSet() != null) {
//					Iterator iter1 = j1.getLineItemSet().iterator();
//					while (iter1.hasNext()) {
//					    LineItem l1 = (LineItem) iter1.next();
//					    lMap.put(l1.getLineItemId(), l1);
//					    itemLIst.add(l1.getLineItemId());
//					}
//				    }
//				    if (j1.getLineItemSet() != null) {
//					Iterator iter1 = j1.getLineItemSet().iterator();
//					while (iter1.hasNext()) {
//					    LineItem l1 = (LineItem) iter1.next();
//					    boolean b = false;
//					    if (l1.getLineItemId().equals(lDelete.getLineItemId())) {
//						j1.setDebit(j1.getDebit() - l1.getDebit());
//						j1.setCredit(j1.getCredit() - l1.getCredit());
//						batch.setTotalDebit(batch.getTotalDebit() - l1.getDebit());
//						batch.setTotalCredit(batch.getTotalCredit() - l1.getCredit());
//						j1.getLineItemSet().remove(l1);
//						b = true;
//						break;
//					    }
//					}
//				    }
//
//				}
//
//			    }
//			    break;
//			}
//			batchDAO.save(batch);
//		    }
//		}
//		Set setLineItem = new HashSet();
//		String lineItemId = "";
//		int id_no = 0;
//		List lList = new ArrayList();
//		String first_sub = "";
//		Collections.sort(itemLIst);
//		for (int i = 0; i < itemLIst.size(); i++) {
//		    LineItem l1 = (LineItem) lMap.get(itemLIst.get(i));
//
//		    lList.add(l1);
//		}
//		LineItem l1 = (LineItem) lList.get(lList.size() - 1);
//
//		if (l1.getLineItemId() != null && !l1.getLineItemId().trim().equals("")) {
//		    int si = l1.getLineItemId().lastIndexOf("-");
//		    first_sub = l1.getLineItemId().substring(0, si);
//		    String sub = l1.getLineItemId().substring(si + 1);
//		    if (sub != null && !sub.equals("")) {
//			id_no = Integer.parseInt(sub);
//		    }
//		    //id_no=itemLIst.size()-1;
//		    id_no += 1;
//		    if (id_no >= 0 && id_no < 10) {
//			lineItemId = journalId + "-" + "00" + id_no;
//		    } else if (id_no >= 10 && id_no < 100) {
//			lineItemId = journalId + "-" + "0" + id_no;
//		    } else if (id_no >= 100 && id_no < 1000) {
//			lineItemId = journalId + "-" + id_no;
//		    }
//		    line = new LineItem();
//		    line.setLineItemId(lineItemId);
//		    line.setCurrency("USD");
//		    j1.getLineItemSet().add(line);
//		}
//		for (int i = 0; i < batchList.size(); i++) {
//		    Batch b1 = (Batch) batchList.get(i);
//		    if (b1.getBatchId().equals(batchId)) {
//			session.setAttribute("batch", b1);
//			Iterator iter = b1.getJournalEntrySet().iterator();
//			while (iter.hasNext()) {
//			    j1 = (JournalEntry) iter.next();
//			}
//			break;
//		    }
//		}
//	    } else if (buttonValue.equals("reverse")) {
//		String jeid = "";
//		if (session.getAttribute("journalId") != null) {
//		    jeid = (String) session.getAttribute("journalId");
//		}
//		batchList = new ArrayList();
//		FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
//		if (session.getAttribute("batchList") != null) {
//		    batchList = (List) session.getAttribute("batchList");
//		}
//		batchId = new BatchDAO().getMaxBatchNumber();
//		if (CommonUtils.isNotEmpty(batchId)) {
//		    batchId = "" + (Integer.parseInt(batchId) + 1);
//		} else {
//		    batchId = "10000";
//		}
//		Batch batch = new Batch();
//		batch.setBatchId(batchId);
//		Double totalCredit = 0.00;
//		Double totalDebit = 0.00;
//		BatchDAO batchDAO = new BatchDAO();
//		if (session.getAttribute("batchList") != null) {
//		    batchList = (List) session.getAttribute("batchList");
//		    for (int i = 0; i < batchList.size(); i++) {
//			Batch b1 = (Batch) batchList.get(i);
//			if (b1.getBatchId() == journalEntryForm.getBatch()) {
//			    totalCredit = 0.00;
//			    totalDebit = 0.00;
//			    if (b1 != null) {
//				batch.setBatchDesc(b1.getBatchDesc());
//				batch.setTotalDebit(b1.getTotalDebit());
//				batch.setTotalCredit(b1.getTotalCredit());
//				batch.setStatus("open");
//				batch.setSourceLedger(b1.getSourceLedger());
//				batch.setType("manual");
//			    }
//			    journalEntryList = new ArrayList();
//			    if (b1.getJournalEntrySet() != null) {
//				Iterator iter1 = b1.getJournalEntrySet().iterator();
//				while (iter1.hasNext()) {
//				    JournalEntry j1 = (JournalEntry) iter1.next();
//				    if (j1.getJournalEntryId().equals(jeid)) {
//					JournalEntry journal = new JournalEntry();
//					j1.setFlag("R");
//					journal.setFlag("R");
//					journal.setJournalEntryId(batchId + "-" + "001");
//					journal.setJournalEntryDesc(jeid + "Reverse");
//					journal.setJeDate(j1.getJeDate());
//					int p1 = 0;
//					List fiscal = fiscalPeriodDAO.getPeriodList3(jeperiod);
//					FiscalPeriod fis = new FiscalPeriod();
//					if (fiscal != null && fiscal.size() > 0) {
//					    fis = (FiscalPeriod) fiscal.get(0);
//					    FiscalPeriod fiscalPeriod = fiscalPeriodDAO.findById(fis.getId() + 1);
//					    while (!fiscalPeriod.getStatus().equalsIgnoreCase("Open")) {
//						fiscalPeriod = fiscalPeriodDAO.findById(fiscalPeriod.getId() + 1);
//					    }
//					    journal.setPeriod(fiscalPeriod.getPeriodDis());
//					}
//					journal.setSourceCode(j1.getSourceCode());
//					journal.setSourceCodeDesc(j1.getSourceCodeDesc());
//					journal.setDebit(0.00);
//					journal.setCredit(0.00);
//					journal.setMemo(j1.getMemo());
//					lineItemList = new ArrayList();
//					if (j1.getLineItemSet() != null) {
//					    Iterator iter2 = j1.getLineItemSet().iterator();
//					    while (iter2.hasNext()) {
//						LineItem l1 = (LineItem) iter2.next();
//						line = new LineItem();
//						if (l1.getLineItemId().length() == 14) {
//						    line.setLineItemId(journal.getJournalEntryId() + "-" + l1.getLineItemId().substring(11, 14));
//						} else {
//						    line.setLineItemId(journal.getJournalEntryId() + "-" + l1.getLineItemId().substring(10, 13));
//						}
//						line.setAccount(l1.getAccount());
//						line.setReference(l1.getReference());
//						line.setReferenceDesc(l1.getReferenceDesc());
//						line.setCurrency(l1.getCurrency());
//						line.setPeriod(l1.getPeriod());
//						if (l1.getCredit() != null && !l1.getCredit().equals("")) {
//						    line.setDebit(l1.getCredit());
//						} else {
//						    line.setDebit(0.00);
//						}
//						if (l1.getDebit() != null && !l1.getDebit().equals("")) {
//						    line.setCredit(l1.getDebit());
//						} else {
//						    line.setCredit(0.00);
//						}
//						journal.setDebit(journal.getDebit() + line.getDebit());
//						journal.setDebit(journal.getDebit());
//						journal.setCredit(journal.getCredit() + line.getCredit());
//						journal.setCredit(journal.getCredit());
//						totalCredit = totalCredit + journal.getDebit();
//						totalDebit = totalDebit + journal.getCredit();
//						lineItemList.add(line);
//					    }
//					    Set lineItemSet = new HashSet<LineItem>();
//					    for (int k = 0; k < lineItemList.size(); k++) {
//						LineItem l6 = (LineItem) lineItemList.get(k);
//						lineItemSet.add(l6);
//					    }
//					    journal.setLineItemSet(lineItemSet);
//					}
//					journalEntryList.add(journal);
//				    }
//				}
//				break;
//			    }
//			}
//		    }
//		    Set journalEntrySet = new HashSet<JournalEntry>();
//		    for (int i = 0; i < journalEntryList.size(); i++) {
//			JournalEntry j2 = (JournalEntry) journalEntryList.get(i);
//			batch.setTotalCredit(j2.getCredit());
//			batch.setTotalDebit(j2.getDebit());
//			journalEntrySet.add(j2);
//		    }
//		    batch.setJournalEntrySet(journalEntrySet);
//		}
//		session.setAttribute("batch", batch);
//		HashMap hashMap = new HashMap();
//		List jList = new ArrayList();
//		HashMap lmap = new HashMap();
//		List llist = new ArrayList();
//		journalEntryList = new ArrayList();
//		if (batch.getJournalEntrySet() != null) {
//		    Iterator iter1 = batch.getJournalEntrySet().iterator();
//		    while (iter1.hasNext()) {
//			JournalEntry journalEntry1 = (JournalEntry) iter1.next();
//			hashMap.put(journalEntry1.getJournalEntryId(), journalEntry1);
//			jList.add(journalEntry1.getJournalEntryId());
//		    }
//		    Collections.sort(jList);
//		    for (int i = 0; i < jList.size(); i++) {
//			JournalEntry jEntry = (JournalEntry) hashMap.get(jList.get(i));
//			journalEntryList.add(jEntry);
//		    }
//		    session.setAttribute("journalEntryList", journalEntryList);
//		    lineItemList = new ArrayList();
//		    if (journalEntryList.size() > 0) {
//			JournalEntry jEntry = (JournalEntry) journalEntryList.get(journalEntryList.size() - 1);
//			session.setAttribute("journalEntry", jEntry);
//			if (jEntry.getLineItemSet() != null) {
//			    Iterator iter = jEntry.getLineItemSet().iterator();
//			    while (iter.hasNext()) {
//				LineItem lineItem = (LineItem) iter.next();
//				lmap.put(lineItem.getLineItemId(), lineItem);
//				llist.add(lineItem.getLineItemId());
//			    }
//			    Collections.sort(llist);
//			    for (int i = 0; i < llist.size(); i++) {
//				LineItem lItem = (LineItem) lmap.get(llist.get(i));
//				lineItemList.add(lItem);
//			    }
//			    session.setAttribute("lineItemList", lineItemList);
//			}
//			batchList.add(batch);
//			session.setAttribute("batchList", batchList);
//		    }
//		}
//		forwardName = "go";
//	    } else if (buttonValue != null && buttonValue.equals("previous")) {
//		session.setAttribute("trade", "TrasactionHistory");
//		forwardName = "previous";
//	    } else if (buttonValue.equals("backToBatch")) {
//		session.setAttribute("trade", "Batch");
//		forwardName = "previous";
//	    }
//	}
//	request.setAttribute("buttonValue", buttonValue);
//
//	return mapping.findForward(forwardName);
//    }
}

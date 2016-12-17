package com.gp.cvst.logisoft.struts.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.ExcelGenerator.SubLedgerReportExcelCreator;
import com.gp.cong.logisoft.bc.accounting.ReceiptsLedgerBc;
import com.gp.cong.logisoft.domain.User;
import com.gp.cvst.logisoft.AccountingConstants;
import com.gp.cvst.logisoft.beans.TransactionBean;
import com.gp.cvst.logisoft.domain.Batch;
import com.gp.cvst.logisoft.domain.FiscalPeriod;
import com.gp.cvst.logisoft.domain.JournalEntry;
import com.gp.cvst.logisoft.domain.Subledger;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.hibernate.dao.BatchDAO;
import com.gp.cvst.logisoft.hibernate.dao.FiscalPeriodDAO;
import com.gp.cvst.logisoft.hibernate.dao.JournalEntryDAO;
import com.gp.cvst.logisoft.hibernate.dao.LineItemDAO;
import com.gp.cvst.logisoft.hibernate.dao.SubledgerDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionLedgerDAO;
import com.gp.cvst.logisoft.struts.form.RecieptsLedgerForm;
import com.gp.cvst.logisoft.util.DBUtil;
import com.logiware.hibernate.dao.AccountingLedgerDAO;
import com.logiware.utils.SubledgerUtils;
import com.oreilly.servlet.ServletUtils;
import org.apache.commons.io.FilenameUtils;

public class RecieptsLedgerAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)throws Exception {
        RecieptsLedgerForm recieptsLedgerForm = (RecieptsLedgerForm) form;
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        String buttonValue = recieptsLedgerForm.getButtonValue();
        String forwardName = "";
        String period = recieptsLedgerForm.getPeriod();
        String startDate = recieptsLedgerForm.getStartDate();
        String endDate = recieptsLedgerForm.getEndDate();
        String subledgerType = recieptsLedgerForm.getSubLedgerType();
        TransactionLedgerDAO transactionledgerDAO = new TransactionLedgerDAO();
        List<TransactionBean> subledgers = new ArrayList<TransactionBean>();
        if (null == buttonValue || buttonValue.trim().equals("")) {
            forwardName = "success";
        } else if (buttonValue.equals("getRecordsOfNullReportingDate")) {
            request.setAttribute("subledgerList", new AccountingLedgerDAO().getSubledgers(recieptsLedgerForm));
            request.setAttribute("summary", "no");
            forwardName = "success";
        } else if (buttonValue.equals("exportRecordsOfNullReportingDate")) {
            subledgers = new AccountingLedgerDAO().getSubledgers(recieptsLedgerForm);
            String excelFileName = new ReceiptsLedgerBc().generateExcelSheet(recieptsLedgerForm, subledgers);
            if (CommonUtils.isNotEmpty(excelFileName)) {
                response.addHeader("Content-Disposition", "attachment; filename=" + FilenameUtils.getName(excelFileName));
                response.setContentType("application/vnd.ms-excel" + ";charset=utf-8");
                ServletUtils.returnFile(excelFileName, response.getOutputStream());
            }
            return null;
        } else if (buttonValue.equals("searchDetails")) {
            request.setAttribute("subledgerList", new AccountingLedgerDAO().getSubledgers(recieptsLedgerForm));
            request.setAttribute("summary", "no");
            forwardName = "success";
        } else if (buttonValue.equals("generateReport")) {
            if (CommonUtils.isEqualIgnoreCase(recieptsLedgerForm.getSubLedgerType(), CommonConstants.SUB_LEDGER_CODE_PURCHASE_JOURNAL)) {
                subledgers = new AccountingLedgerDAO().getPJSubLedgers(recieptsLedgerForm);
            } else {
                subledgers = new AccountingLedgerDAO().getSubledgers(recieptsLedgerForm);
            }
            request.setAttribute("subledgerList", subledgers);
            request.setAttribute("summary", "no");
            String realPath = this.getServlet().getServletContext().getRealPath("/");
            ReceiptsLedgerBc receiptsLedgerBc = new ReceiptsLedgerBc();
            request.setAttribute("reportFileName", receiptsLedgerBc.generateReport(recieptsLedgerForm, subledgers, realPath));
            forwardName = "success";
        } else if (buttonValue.equals("exportToExcel")) {
            if (CommonUtils.isEqualIgnoreCase(recieptsLedgerForm.getSubLedgerType(), CommonConstants.SUB_LEDGER_CODE_PURCHASE_JOURNAL)) {
                subledgers = new AccountingLedgerDAO().getPJSubLedgers(recieptsLedgerForm);
            } else {
                subledgers = new AccountingLedgerDAO().getSubledgers(recieptsLedgerForm);
            }
            request.setAttribute("subledgerList", subledgers);
            request.setAttribute("summary", "no");
//            String excelFileName = new ReceiptsLedgerBc().generateExcelSheet(recieptsLedgerForm, subledgers);
            String excelFileName = new SubLedgerReportExcelCreator(recieptsLedgerForm).createExcel(subledgers);
            if (CommonUtils.isNotEmpty(excelFileName)) {
                response.addHeader("Content-Disposition", "attachment; filename=" + FilenameUtils.getName(excelFileName));
                response.setContentType("application/vnd.ms-excel" + ";charset=utf-8");
                ServletUtils.returnFile(excelFileName, response.getOutputStream());
            }
            return null;
        } else if (buttonValue.equals("summary")) {
            request.setAttribute("summary", "yes");
            request.setAttribute("subledgerList", new AccountingLedgerDAO().getSubledgers(recieptsLedgerForm));
            forwardName = "success";
        } else if (buttonValue.equals("postGL")) {
            try {
                new SubledgerUtils().postSubledger(recieptsLedgerForm, loginUser);
                request.setAttribute("errorMsg", "Posted Successfully");
            } catch (Exception e) {
                request.setAttribute("errorMsg", e.getMessage());
            }
//	    subledgers = new AccountingLedgerDAO().getSubledgers(recieptsLedgerForm);
//	    Boolean flag = true;
//	    String invalidGlAccount = "";
//	    List<TransactionBean> validSubLedgerList = new ArrayList<TransactionBean>();
//	    if (subledgers != null && !subledgers.isEmpty()) {
//		for (TransactionBean tbean : subledgers) {
//		    if (null != tbean.getGlAcctNo() && !tbean.getGlAcctNo().trim().equals("")) {
//			validSubLedgerList.add(tbean);
//		    }
//		}
//		subledgers = validSubLedgerList;
//		// Creating & Saving an Auto generated Batch
//		ARApplypaymentsBC arApplypaymentsBC = new ARApplypaymentsBC();
//		for (TransactionBean tb : subledgers) {
//		    if (tb.getGlAcctNo() != null
//			    && !tb.getGlAcctNo().trim().equals("")) {
//			flag = Boolean.valueOf(arApplypaymentsBC.validateChargeCode(tb.getGlAcctNo()));
//			if (!flag) {
//			    invalidGlAccount = tb.getGlAcctNo();
//			    break;
//			}
//		    }
//		}
//		if (flag) {
//		    List<String> subLedgerList = new ArrayList<String>();
//		    for (TransactionBean transactionBean : subledgers) {
//			if (transactionBean != null
//				&& transactionBean.getSubLedgerCode() != null) {
//			    if (!subLedgerList.contains(transactionBean.getSubLedgerCode())) {
//				subLedgerList.add(transactionBean.getSubLedgerCode());
//			    }
//			}
//		    }
//		    FiscalPeriod fiscalPeriod = new FiscalPeriodDAO().findById(Integer.parseInt(period));
//		    if (CommonUtils.isNotEqual(fiscalPeriod.getStatus(), CommonConstants.STATUS_OPEN)) {
//			fiscalPeriod = new FiscalPeriodDAO().getCurrentOpenPeriod();
//		    }
//		    String perioddis = fiscalPeriod.getPeriodDis();
//
//		    BatchDAO bdao = new BatchDAO();
//		    String batchId = new BatchDAO().getMaxBatchNumber();
//		    if (CommonUtils.isNotEmpty(batchId)) {
//			batchId = "" + (Integer.parseInt(batchId) + 1);
//		    } else {
//			batchId = "10000";
//		    }
//		    GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
//		    GenericCode genericCode = genericCodeDAO.findByCodeName(
//			    subledgerType, 33);
//
//		    String batchDesc = subledgerType + " Subledger Close for " + perioddis;
//		    Batch batch = new Batch();
//		    batch.setBatchId(batchId);
//		    batch.setBatchDesc(batchDesc);
//		    batch.setReadyToPost("yes");
//		    batch.setStatus("ready to post");
//		    batch.setTotalCredit(new Double("0.00"));
//		    batch.setTotalDebit(new Double("0.00"));
//		    batch.setSourceLedger(genericCode);
//		    bdao.save(batch);
//		    StringBuilder desc = new StringBuilder("GL Batch '").append(batch.getBatchId()).append("' on ");
//		    desc.append(subledgerType).append(" Subledger Close for ").append(perioddis);
//		    desc.append(" added by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
//		    new AuditNotesUtils().insertAuditNotes(desc.toString(), NotesConstants.GL_BATCH, batch.getBatchId(), NotesConstants.GL_BATCH, loginUser);
//		    // Creating & Saving an Auto Generated Journal Entry for the
//		    // Batch
//		    JournalEntryDAO jeDAO = new JournalEntryDAO();
//		    JournalEntry je = new JournalEntry();
//		    String jeid = batchId + "-" + "001";
//		    je.setJournalEntryId(jeid);
//		    je.setBatchId(batchId);
//		    je.setMemo("");
//		    je.setCredit(0.0);
//		    je.setDebit(0.0);
//		    // setting JE Period
//		    je.setPeriod(perioddis);
//		    je.setJournalEntryDesc(subledgerType
//			    + " SubLedger Close for " + perioddis);
//		    je.setSubledgerClose(CommonConstants.YES);
//		    je.setJeDate(new Date());
//		    je.setSourceCode(genericCode);
//		    if (null != genericCode) {
//			je.setSourceCodeDesc(null != genericCode.getCodedesc() ? genericCode.getCodedesc() : "");
//		    }
//
//		    jeDAO.save(je);
//		    // Creating & Saving an Auto Generated Line Items for the
//		    // Journal Entry
//		    recieptsLedgerForm.setButtonValue("summary");
//		    subledgers = new AccountingLedgerDAO().getSubledgers(recieptsLedgerForm);
//		    if (subledgers != null && !subledgers.isEmpty()) {
//			List duplicateList = new ArrayList();
//			for (TransactionBean transBean : subledgers) {
//			    if (transBean != null
//				    && (transBean.getGlAcctNo() != null && !transBean.getGlAcctNo().trim().equals(""))) {
//				duplicateList.add(transBean);
//			    }
//			}
//			subledgers = duplicateList;
//		    }
//		    Double jeCredit = 0.0;
//		    Double jeDebit = 0.0;
//		    int i = 1;
//		    int transactionCount = 0;
//		    String lineItemId = "";
//		    TransactionBean tbean = new TransactionBean();
//		    AccountDetailsDAO adDAO = new AccountDetailsDAO();
//		    LineItem line = null;
//		    LineItemDAO lineDAO = null;
//		    HashMap<String, Double> chargeCodeMap = new HashMap<String, Double>();
//		    List<LineItem> lineItemObjectList = new ArrayList<LineItem>();
//		    for (TransactionBean transactionBean : subledgers) {
//			lineDAO = new LineItemDAO();
//			line = new LineItem();
//			if (i >= 0 && i < 10) {
//			    lineItemId = jeid + "-" + "00" + i;
//			} else if (i >= 10 && i < 100) {
//			    lineItemId = jeid + "-" + "0" + i;
//			} else {
//			    lineItemId = jeid + "-" + i;
//			}
//			line.setLineItemId(lineItemId);
//			line.setJournalEntryId(jeid);
//			line.setReference(subledgerType);
//			line.setReferenceDesc(subledgerType + " SubLedger Close for " + perioddis);
//			// To get credit
//			Double credit = 0d;
//			Double debit = 0d;
//			Double totalAmount = 0d;
//			if (null != transactionBean.getCredit()
//				&& !transactionBean.getCredit().trim().equals("")) {
//			    credit = Double.parseDouble(transactionBean.getCredit().replaceAll(",", ""));
//			}
//			if (null != transactionBean.getDebit()
//				&& !transactionBean.getDebit().trim().equals("")) {
//			    debit = Double.parseDouble(transactionBean.getDebit().replaceAll(",", ""));
//			}
//			totalAmount = debit - credit;
//			if (totalAmount < 0d) {
//			    debit = 0d;
//			    credit = (-1) * totalAmount;
//			} else {
//			    debit = totalAmount;
//			    credit = 0d;
//			}
//			jeCredit += credit;
//			jeDebit += debit;
//			if (transactionBean.getStatus() != null && transactionBean.getStatus().equals(AccountingConstants.STATUS_CHARGECODE)) {
//			    // In case of Charge Code
//			    line.setCredit(credit);
//			    line.setDebit(0d);
//			    if (!chargeCodeMap.containsKey(transactionBean.getGlAcctNo())) {
//				chargeCodeMap.put(transactionBean.getGlAcctNo(), credit);
//			    } else {
//				chargeCodeMap.put(transactionBean.getGlAcctNo(), chargeCodeMap.get(transactionBean.getGlAcctNo()) + credit);
//			    }
//			} else {
//			    line.setCredit(credit);
//			    line.setDebit(debit);
//			}
//			Date date = new Date();
//			line.setDate(date);
//			String account = transactionBean.getGlAcctNo();
//			line.setAccount(account);
//			String acctDesc = adDAO.getDescforAccount(account);
//			line.setAccountDesc(acctDesc);
//			line.setCurrency("USD");
//			lineDAO.save(line);
//			lineItemObjectList.add(line);
//			i++;
//			transactionCount++;
//		    }
//		    // **********************************Inserting Debit Record
//		    // In line item.*******************************************
//		    Double totalAmt = jeDebit - jeCredit;
//		    if (!subLedgerList.isEmpty()) {
//			for (String subledgername : subLedgerList) {
//			    if (null != subledgername
//				    && !subledgername.trim().equals(
//				    AccountingConstants.SUBLEDGER_CODE_NETSETT)) {
//				lineDAO = new LineItemDAO();
//				line = new LineItem();
//				if (i >= 0 && i < 10) {
//				    lineItemId = jeid + "-" + "00" + i;
//				} else if (i >= 10 && i < 100) {
//				    lineItemId = jeid + "-" + "0" + i;
//				} else {
//				    lineItemId = jeid + "-" + i;
//				}
//				line.setLineItemId(lineItemId);
//				line.setJournalEntryId(jeid);
//				line.setReference(subledgerType);
//				line.setReferenceDesc(subledgerType + " SubLedger Close for Period " + perioddis);
//				if (totalAmt > 0) {
//				    line.setCredit(totalAmt);
//				    line.setDebit(0d);
//				} else {
//				    line.setDebit(-totalAmt);
//				    line.setCredit(0d);
//				}
//				Date date = new Date();
//				line.setDate(date);
//				SubledgerDAO sldao = new SubledgerDAO();
//				String subledgerid = sldao.getid(subledgername);
//				SubledgerAcctsDAO slaDAO = new SubledgerAcctsDAO();
//				List controleAcctList = null;
//				String controlacct = "02-1132-01";
//				if (!controleAcctList.isEmpty()) {
//				    controlacct = (String) controleAcctList.get(0);
//				}
//				line.setAccount(controlacct);
//				String acctDesc = adDAO.getDescforAccount(controlacct);
//				line.setAccountDesc(acctDesc);
//				line.setCurrency("USD");
//				lineDAO.save(line);
//				i++;
//				line = null;
//			    }// for
//			}
//		    }// if
//		    if (chargeCodeMap != null && !chargeCodeMap.isEmpty()) {
//			Set<String> accountkeySet = chargeCodeMap.keySet();
//			HashMap<String, String> chargeCodeGLcontrolMap = new HashMap<String, String>();
//			List<TransactionBean> chargeCodedetailList = transactionledgerDAO.detialsListForChargeCode(startDate, endDate,
//				subledgerType, "no", null);
//			for (TransactionBean ctbean : chargeCodedetailList) {
//			    if (!chargeCodeGLcontrolMap.containsKey(ctbean.getGlAcctNo())) {
//				chargeCodeGLcontrolMap.put(
//					ctbean.getGlAcctNo(), ctbean.getBankAccountNumber());
//			    }
//			}
//			for (Iterator iterator = accountkeySet.iterator(); iterator.hasNext();) {
//			    String glaccount = (String) iterator.next();
//			    lineDAO = new LineItemDAO();
//			    line = new LineItem();
//			    if (i >= 0 && i < 10) {
//				lineItemId = jeid + "-" + "00" + i;
//			    } else if (i >= 10 && i < 100) {
//				lineItemId = jeid + "-" + "0" + i;
//			    } else {
//				lineItemId = jeid + "-" + i;
//			    }
//			    line.setLineItemId(lineItemId);
//			    line.setJournalEntryId(jeid);
//			    line.setReference(subledgerType);
//			    line.setReferenceDesc(subledgerType + " SubLedger Close for Period " + perioddis);
//			    if (chargeCodeMap.get(glaccount) < 0) {
//				line.setCredit(chargeCodeMap.get(glaccount));
//				line.setDebit(0d);
//				jeCredit += chargeCodeMap.get(glaccount);
//			    } else {
//				line.setCredit(0d);
//				line.setDebit(chargeCodeMap.get(glaccount));
//				jeDebit += chargeCodeMap.get(glaccount);
//			    }
//			    Date date = new Date();
//			    line.setDate(date);
//			    String controlacct = "02-1132-01";
//			    try {
//				controlacct = chargeCodeGLcontrolMap.get(glaccount);
//			    } catch (RuntimeException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			    }
//			    line.setAccount(controlacct);
//			    String acctDesc = adDAO.getDescforAccount(controlacct);
//			    line.setAccountDesc(acctDesc);
//			    line.setCurrency("USD");
//			    lineDAO.save(line);
//			    i++;
//			    line = null;
//			}
//		    }
//		    totalAmt = Math.abs(jeDebit - jeCredit);
//		    jeDAO.updateJournalEntry(jeid, totalAmt, totalAmt);
//
//		    // Updating Transaction Ledger Status
//
//		    recieptsLedgerForm.setButtonValue("searchDetails");
//		    subledgers = new AccountingLedgerDAO().getSubledgers(recieptsLedgerForm);
//		    int lsize = subledgers.size();
//		    int j = 0;
//		    int liolSize = lineItemObjectList.size();
//		    while (lsize > 0) {
//			tbean = (TransactionBean) subledgers.get(j);
//			String tranLedId = tbean.getTransactionId();
//			String glAcctNo = tbean.getGlAcctNo();
//
//			// String status="PostedtoGL";
//			TransactionLedger transactionLedger = new TransactionLedger();
//			transactionLedger = transactionledgerDAO.findById(Integer.parseInt(tranLedId));
//			if (transactionLedger != null) {
//			    if (null != transactionLedger.getGlAccountNumber()
//				    && !transactionLedger.getGlAccountNumber().trim().equals("")) {
//				int temp = 0;
//				while (temp < liolSize) {
//				    LineItem templine = new LineItem();
//				    templine = lineItemObjectList.get(temp);
//				    String lineglacctno = templine.getAccount();
//				    if (lineglacctno.equals(glAcctNo)) {
//					transactionLedger.setJournalEntryNumber(templine.getJournalEntryId());
//					transactionLedger.setLineItemNumber(templine.getLineItemId());
//				    }
//				    temp++;
//				}
//				if (transactionLedger.getStatus() != null
//					&& CommonUtils.isNotEqual(transactionLedger.getSubledgerSourceCode(), CommonConstants.SUB_LEDGER_CODE_ACCRUALS)) {
//				    if (transactionLedger.getStatus().equals(AccountingConstants.STATUS_CHARGECODE)) {
//					transactionLedger.setStatus(AccountingConstants.STATUS_CHARGECODEPOSTED);
//				    } else {
//					transactionLedger.setStatus("PostedtoGL");
//				    }
//				}
//			    }
//			    transactionLedger.setPostedToGlDate(new Date());
//			    TransactionLedgerHistory transactionLedgerHistory = new TransactionLedgerHistory(transactionLedger);
//			    new AccountingLedgerDAO().saveHistory(transactionLedgerHistory);
//			}
//			// transactionledgerDAO.updateTransLedgerStatus(tranLedId,status);
//			j++;
//			lsize--;
//		    }
//		    // Success Message
//		    recieptsLedgerForm.setButtonValue("postGL");
//		    request.setAttribute("errorMsg", "Posted Successfully");
//		}
//		if (null != invalidGlAccount
//			&& !invalidGlAccount.trim().equals("")) {
//		    request.setAttribute("errorMsg", "Gl Account#"
//			    + invalidGlAccount
//			    + " is Invalid.Please Check Before Posting");
//		}
//	    } else {
//		request.setAttribute("errorMsg", "No Records to Post");
//	    }
//	    request.removeAttribute("subledgerList");
            forwardName = "success";
        } else if (buttonValue.equals("undoBatch")) {

            Batch batch = null;
            BatchDAO batchDAO = new BatchDAO();
            batch = batchDAO.findById(recieptsLedgerForm.getUndoBatch());
            if (batch != null) {
                if (batch.getStatus() != null
                        && batch.getStatus().equals("ready to post")) {
                    JournalEntryDAO journalEntryDAO = new JournalEntryDAO();
                    List jeIdList = new ArrayList();
                    List jeList = journalEntryDAO.findByBatchId(batch.getBatchId());
                    if (!jeList.isEmpty()) {
                        int index = 0;
                        LineItemDAO lineItemDAO = new LineItemDAO();
                        while (jeList.size() > index) {
                            JournalEntry journalEntry = null;
                            journalEntry = (JournalEntry) jeList.get(index);
                            jeIdList.add(journalEntry.getJournalEntryId());
                            /*
                             * List
                             * lineItemList=lineItemDAO.findByProperty("journalEntryId",
                             * journalEntry.getJournalEntryId()); for (Iterator
                             * iterator = lineItemList.iterator();
                             * iterator.hasNext();) { LineItem lineItem =
                             * (LineItem) iterator.next();
                             *
                             * lineItemDAO.delete(lineItem);
                             *  }
                             */
                            lineItemDAO.deleteLineItemsForJE(journalEntry.getJournalEntryId(), journalEntry.getBatchId().toString());

                            // journalEntryDAO.delete(journalEntry);t
                            index++;
                        }
                    }
                    // Check for JeIdList is empty Or Not
                    if (!jeIdList.isEmpty()) {

                        for (Iterator iterator = jeIdList.iterator(); iterator.hasNext();) {
                            String jename = (String) iterator.next();
                            List transactionLedgerList = transactionledgerDAO.findByProperty("journalEntryNumber",
                                    jename);
                            if (!transactionLedgerList.isEmpty()) {
                                for (Iterator iterator2 = transactionLedgerList.iterator(); iterator2.hasNext();) {
                                    TransactionLedger transactionLedger = (TransactionLedger) iterator2.next();
                                    transactionLedger.setJournalEntryNumber(null);
                                    transactionLedger.setLineItemNumber(null);
                                    if (transactionLedger.getStatus() != null
                                            && transactionLedger.getStatus().equals(
                                            AccountingConstants.STATUS_CHARGECODEPOSTED)) {
                                        transactionLedger.setStatus(AccountingConstants.STATUS_CHARGECODE);
                                    } else {
                                        transactionLedger.setStatus("Open");
                                    }
                                    transactionledgerDAO.update(transactionLedger);
                                    transactionLedger = null;

                                }

                            }

                        }
                    }
                    // batchDAO.delete(batch);
                    request.setAttribute("errorMsg", "Batch # "
                            + batch.getBatchId() + " Succesfully Deleted");
                } else {
                    if (batch != null) {
                        request.setAttribute("errorMsg", "Batch # "
                                + batch.getBatchId() + " can not Undo");
                    }
                }
            }
            request.removeAttribute("subledgerList");
            forwardName = "success";
        } else if (buttonValue.trim().equals("changeChargeCodeOptions")) {
            forwardName = "success";
        }
        DBUtil dbUtil = new DBUtil();
        //request.setAttribute("periodList", dbUtil.getperiodList());
        com.gp.cong.logisoft.util.DBUtil dBUtil = new com.gp.cong.logisoft.util.DBUtil();
        request.setAttribute("sortByList", dBUtil.getSortByList(CommonConstants.PAGE_SUB_LEDGER, CommonConstants.SORT_FOR_PJ_SUBLEDGER));
        request.setAttribute("sortByListForAllSubledgers", dBUtil.getSortByList(CommonConstants.PAGE_SUB_LEDGER, CommonConstants.SORT_FOR_ALL_SUBLEDGERS));
        request.setAttribute("sourceCodeList", dbUtil.getSubledgerListforRL());
        request.setAttribute("chargeCodeList", dBUtil.getChargeCodeList(recieptsLedgerForm.getRevOrExp()));
        request.setAttribute("revOrExpList", dBUtil.getRevOrExpList());
        if (CommonUtils.isNotEmpty(period)) {
            FiscalPeriod fiscalPeriod = new FiscalPeriodDAO().findById(Integer.parseInt(period));
            if (null != fiscalPeriod) {
                request.setAttribute("periodDis", fiscalPeriod.getPeriodDis());
                request.setAttribute("startDate", DateUtils.formatDate(fiscalPeriod.getStartDate(), "MM/dd/yyyy"));
                request.setAttribute("endDate", DateUtils.formatDate(fiscalPeriod.getEndDate(), "MM/dd/yyyy"));
            }
        }
        if (CommonUtils.isNotEmpty(startDate)) {
            request.setAttribute("startDate", startDate);
        }
        if (CommonUtils.isNotEmpty(endDate)) {
            request.setAttribute("endDate", endDate);
        }
        request.setAttribute("recieptsLedgerForm", recieptsLedgerForm);
        String subLedgerType = "All SubLedgers";
        if (null != recieptsLedgerForm.getSubLedgerType() && !recieptsLedgerForm.getSubLedgerType().trim().equals(CommonConstants.ALL)) {
            SubledgerDAO subledgerDAO = new SubledgerDAO();
            List<Subledger> list = subledgerDAO.findByProperty("subLedgerCode", recieptsLedgerForm.getSubLedgerType());
            for (Subledger subledger : list) {
                subLedgerType = null != subledger.getSubLedgerDesc() ? subledger.getSubLedgerDesc() + " SubLedger" : "All SubLedgers";
            }
        }
        request.setAttribute("subLedgerType", subLedgerType);
        return mapping.findForward(forwardName);
    }
}

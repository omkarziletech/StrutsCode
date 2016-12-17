package com.gp.cvst.logisoft.struts.action;

import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cvst.logisoft.beans.BatchesBean;
import com.gp.cvst.logisoft.domain.Batch;
import com.gp.cvst.logisoft.hibernate.dao.BatchDAO;
import com.gp.cvst.logisoft.struts.form.NewBatchForm;
import com.gp.cvst.logisoft.util.DBUtil;
import com.logiware.utils.AuditNotesUtils;
import java.util.Date;

public class NewBatchAction extends Action {
//    public ActionForward execute(ActionMapping mapping, ActionForm form,
//            HttpServletRequest request, HttpServletResponse response) {
//        NewBatchForm newBatchForm = (NewBatchForm) form;// TODO Auto-generated method stub
//        HttpSession session = request.getSession();
//        User loginUser = (User) session.getAttribute("loginuser");
//        String batchId = (request.getParameter("batchno"));
//        String batchDesc = request.getParameter("desc");
//        String sourceLedger = newBatchForm.getSourceLedger();
//        String batchtype = request.getParameter("type");
//        String totalCredit = newBatchForm.getTotalCredit();
//        DBUtil dbUtil = new DBUtil();
//
//        String totalDebit = newBatchForm.getTotalDebit();
//        totalDebit = dbUtil.removecomma(totalDebit);
//        String post = request.getParameter("post");
//        String status = request.getParameter("status");
//        String buttonValue = newBatchForm.getButtonValue();
//        String forward = "";
//        Batch batch = new Batch();
//        BatchDAO batchDao = new BatchDAO();
//        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
//
//
//        batch.setBatchId((batchId));
//        if (buttonValue.equals("save")) {
//            List batchesList = new ArrayList();
//            batch.setBatchDesc(batchDesc);
//            if (sourceLedger != null && !sourceLedger.equals("0")) {
//                GenericCode gen = (GenericCode) genericCodeDAO.findById(Integer.parseInt(sourceLedger));
//                batch.setSourceLedger(gen);
//            }
//
//            batch.setType(batchtype);
//            if (totalCredit == null || totalCredit.equals("")) {
//                totalCredit = "0.00";
//            }
//            if (totalCredit.contains(",")) {
//                totalCredit = totalCredit.replace(",", "");
//            }
//
//            batch.setTotalCredit(Double.parseDouble(totalCredit));
//            if (totalDebit == null || totalDebit.equals("")) {
//                totalDebit = "0.00";
//            }
//            if (totalDebit.contains(",")) {
//                totalDebit = totalDebit.replace(",", "");
//            }
//            batch.setTotalDebit(Double.parseDouble(totalDebit));
//            if (post != null && post.equals("on")) {
//                batch.setReadyToPost(post);
//            } else {
//                batch.setReadyToPost("off");
//            }
//            batch.setStatus(status);
//            batchDao.save(batch);
//            StringBuilder desc = new StringBuilder("GL Batch '").append(batch.getBatchId()).append("'");
//            desc.append(" added by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
//            new AuditNotesUtils().insertAuditNotes(desc.toString(), NotesConstants.GL_BATCH, batch.getBatchId(), NotesConstants.GL_BATCH, loginUser);
//            forward = "addJournalEntry";
//
//            if (session.getAttribute("batchesList") != null) {
//                batchesList = (List) session.getAttribute("batchesList");
//            } else {
//                batchesList = new ArrayList();
//            }
//            BatchesBean bBean = new BatchesBean();
//            bBean.setBatchno(batch.getBatchId().toString());
//            bBean.setDesc(batch.getBatchDesc());
//
//            bBean.setSourceLedger(batch.getSourceLedger().getCode());
//            bBean.setType(batch.getType());
//            bBean.setTotalCredit(batch.getTotalCredit().toString());
//            bBean.setTotalDebit(batch.getTotalDebit().toString());
//            bBean.setPost(batch.getReadyToPost());
//            bBean.setStatus(batch.getStatus());
//            batchesList.add(bBean);
//            session.setAttribute("batchesList", batchesList);
//            List batchList = new ArrayList();
//            if (session.getAttribute("batchList") != null) {
//                batchList = (List) session.getAttribute("batchList");
//            } else {
//                batchList = new ArrayList();
//            }
//            batchList.add(batch);
//            session.setAttribute("batch", batch);
//            session.setAttribute("batchList", batchList);
//            session.setAttribute("from", "directFromBatch");
//            session.setAttribute("trade", "journalentry");
//        } else if (buttonValue.equals("update")) {
//            List batchList = new ArrayList();
//            batch = batchDao.findById(batchId);
//            batch.setBatchDesc(batchDesc);
//            if (post != null && post.equals("on")) {
//                batch.setReadyToPost(post);
//            } else {
//                batch.setReadyToPost("off");
//            }
//            if (sourceLedger != null && !sourceLedger.equals("0") && !sourceLedger.equals("")) {
//                GenericCode gen = genericCodeDAO.findById(Integer.parseInt(sourceLedger));
//                batch.setSourceLedger(gen);
//            }
//
//            batch.setStatus(status);
//            if (totalCredit.contains(",")) {
//                totalCredit = totalCredit.replace(",", "");
//            }
//            if (totalDebit.contains(",")) {
//                totalDebit = totalDebit.replace(",", "");
//            }
//            batch.setTotalCredit(Double.parseDouble(totalCredit));
//            batch.setTotalDebit(Double.parseDouble(totalDebit));
//            batch.setType(batchtype);
//            batchDao.update(batch);
//            if (session.getAttribute("batchList") != null) {
//                batchList = (List) session.getAttribute("batchList");
//            } else {
//                batchList = new ArrayList();
//            }
//            if (batchList != null) {
//                for (int i = 0; i < batchList.size(); i++) {
//                    Batch b1 = (Batch) batchList.get(i);
//                    if (b1.getBatchId().toString().equals(batchId)) {
//                        b1.setBatchDesc(batch.getBatchDesc());
//                        b1.setReadyToPost(batch.getReadyToPost());
//                        b1.setSourceLedger(batch.getSourceLedger());
//                        b1.setStatus(batch.getStatus());
//                        b1.setTotalCredit(batch.getTotalCredit());
//                        b1.setTotalDebit(batch.getTotalDebit());
//                        b1.setType(batch.getType());
//                    }
//                }
//            }
//            List batchesList = (List) session.getAttribute("batchesList");
//            for (int i = 0; i < batchesList.size(); i++) {
//                BatchesBean bb = ((BatchesBean) batchesList.get(i));
//                if (bb.getBatchno().equals(batchId)) {
//                    bb.setDesc(batchDesc);
//                    if (post != null && post.equals("on")) {
//                        bb.setPost(post);
//                    } else {
//                        bb.setPost("off");
//                    }
//                    if (sourceLedger != null && !sourceLedger.equals("0") && !sourceLedger.equals("")) {
//                        GenericCode gen = genericCodeDAO.findById(Integer.parseInt(sourceLedger));
//                        bb.setSourceLedger(gen.getCode());
//                    }
//
//                    bb.setStatus(status);
//                    bb.setTotalCredit(totalCredit);
//                    bb.setTotalDebit(totalDebit);
//                    bb.setType(batchtype);
//                }
//            }
//            forward = "save";
//
//        } else if (buttonValue.equals("cancel")) {
//            batchDao.delete(batch);
//            forward = "cancel";
//        } else if (buttonValue.equals("editcancel")) {
//            forward = "cancel";
//        }
//        return mapping.findForward(forward);
//    }
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.TpNote;
import com.gp.cong.logisoft.hibernate.dao.NotesDAO;
import com.gp.cong.logisoft.hibernate.dao.TpNoteDao;
import com.gp.cvst.logisoft.struts.form.BlueScreenNotesForm;
import com.logiware.action.LogiwareEventAction;
import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Meiyazhakan R
 */
public class BlueScreenNotesAction extends LogiwareEventAction {

    public ActionForward displayNotes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BlueScreenNotesForm blueScreenNotesForm = (BlueScreenNotesForm) form;
        NotesDAO notesDAO = new NotesDAO();
        request.setAttribute("notesList", notesDAO.getBlueScreenNotes(blueScreenNotesForm));
        String VoidSymbol =notesDAO.getBlueScreenVoidSymbol(blueScreenNotesForm.getCustomerNo(), blueScreenNotesForm.getNoteSymbol());
        List<Object[]> noteSymbolList = notesDAO.getBlueScreenNoteSymbol(blueScreenNotesForm.getCustomerNo());
        StringBuilder sb =new StringBuilder();
        Map<String, String> notesMap = new LinkedHashMap();
        for (int i = 0; i < noteSymbolList.size(); i++) {
            Object[] notes = (Object[]) noteSymbolList.get(i);
            if ("*".equalsIgnoreCase(notes[0].toString())) {
                notesMap.put("auto", "*");
                sb.append("*");
            }
            if ("$".equalsIgnoreCase(notes[0].toString())) {
                notesMap.put("Account", "$");
                sb.append("$");
            }
            if ("A".equalsIgnoreCase(notes[0].toString())) {
                notesMap.put("Air", "A");
                sb.append("A");
            }
            if ("D".equalsIgnoreCase(notes[0].toString())) {
                notesMap.put("Doc", "D");
                sb.append("D");
            }
            if ("G".equalsIgnoreCase(notes[0].toString())) {
                notesMap.put("General", "G");
                sb.append("G");
            }
            if ("F".equalsIgnoreCase(notes[0].toString())) {
                notesMap.put("Fcl", "F");
                sb.append("F");
            }
            if ("I".equalsIgnoreCase(notes[0].toString())) {
                notesMap.put("Imports", "I");
                sb.append("I");
            }
            if ("L".equalsIgnoreCase(notes[0].toString())) {
                notesMap.put("Lcl", "L");
                sb.append("L");
            }
            if ("S".equalsIgnoreCase(notes[0].toString())) {
                notesMap.put("Sales", "S");
                sb.append("S");
            }
            if ("W".equalsIgnoreCase(notes[0].toString())) {
                notesMap.put("whse", "W");
                sb.append("W");
            }
        }
            if ("V".equalsIgnoreCase(VoidSymbol)) {
                notesMap.put("void", "V");
                sb.append("V");
            }
        request.setAttribute("noteSymbolList", sb.toString());
        request.setAttribute("notesStatusMap", notesMap);
        request.setAttribute("noteTypeSymbol", blueScreenNotesForm.getNoteSymbol());
        request.setAttribute("blueScreenNotesForm", blueScreenNotesForm);
        return mapping.findForward("success");
    }

    public ActionForward addTpNote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BlueScreenNotesForm blueScreenNotesForm = (BlueScreenNotesForm) form;
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        TpNote tpNote = new TpNote();
        List shcnAcctNo = null;
        TpNoteDao tpNoteDao = new TpNoteDao();
        if (CommonUtils.isNotEmpty(blueScreenNotesForm.getCustomerNo())) {
            shcnAcctNo = tpNoteDao.getAcctNo(blueScreenNotesForm.getCustomerNo());
        }
        if (CommonUtils.isNotEmpty(shcnAcctNo)) {
            Object[] acctNo = (Object[]) shcnAcctNo.get(0);
            if (acctNo[0] != null && !acctNo[0].equals("")) {
                tpNote.setActnum((String) acctNo[0]);
            } else {
                tpNote.setActnum("");
            }
            if (acctNo[1] != null && !acctNo[1].equals("")) {
                tpNote.setConnum((String) acctNo[1]);
            } else {
                tpNote.setConnum("");
            }
        } else {
            tpNote.setActnum("");
            tpNote.setConnum("");
        }
        tpNote.setNoteType(blueScreenNotesForm.getTpNoteType().toUpperCase());
        if (CommonUtils.isNotEmpty(blueScreenNotesForm.getTpNoteDesc())) {
            tpNote.setNoteDesc(blueScreenNotesForm.getTpNoteDesc().toUpperCase());
        }
        tpNote.setVoidedDatetime(null);
        tpNote.setEnteredDatetime(new Date());
        tpNote.setModifiedDatetime(new Date());
        tpNote.setEnteredBy(user);
        tpNote.setModifiedBy(user);
        tpNote.setVoidedBy(user);
        tpNote.setVoided(false);
        if (CommonUtils.isNotEmpty(blueScreenNotesForm.getTpId())) {
            tpNote.setId(blueScreenNotesForm.getTpId());
            tpNoteDao.saveOrUpdate(tpNote);
        } else {
            tpNoteDao.save(tpNote);
        }
        out.println(blueScreenNotesForm.getTpNoteType());
        out.flush();
        out.close();
        return null;
    }

    public ActionForward deleteTpNote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BlueScreenNotesForm blueScreenNotesForm = (BlueScreenNotesForm) form;
        TpNoteDao tpNoteDao = new TpNoteDao();
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        TpNote tpNote = new TpNote();
        List shcnAcctNo = null;
        if (CommonUtils.isNotEmpty(blueScreenNotesForm.getCustomerNo())) {
            shcnAcctNo = tpNoteDao.getAcctNo(blueScreenNotesForm.getCustomerNo());
        }
        if (CommonUtils.isNotEmpty(shcnAcctNo)) {
            Object[] acctNo = (Object[]) shcnAcctNo.get(0);
            if (acctNo[0] != null && !acctNo[0].equals("")) {
                tpNote.setActnum((String) acctNo[0]);
            } else {
                tpNote.setActnum("");
            }
            if (acctNo[1] != null && !acctNo[1].equals("")) {
                tpNote.setConnum((String) acctNo[1]);
            } else {
                tpNote.setConnum("");
            }
        } else {
            tpNote.setActnum("");
            tpNote.setConnum("");
        }
        tpNote.setNoteType("V");
        if (CommonUtils.isNotEmpty(blueScreenNotesForm.getTpNoteDesc())) {
            tpNote.setNoteDesc(blueScreenNotesForm.getTpNoteDesc().toUpperCase());
        }
        tpNote.setVoidedDatetime(new Date());
        tpNote.setEnteredDatetime(new Date());
        tpNote.setModifiedDatetime(new Date());
        tpNote.setEnteredBy(user);
        tpNote.setModifiedBy(user);
        tpNote.setVoidedBy(user);
        tpNote.setVoided(true);
        tpNoteDao.delete(blueScreenNotesForm.getTpId());
        tpNoteDao.save(tpNote);
        return null;
    }
}

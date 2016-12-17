/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclRemarksForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclRemarks;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.domain.lcl.TpNote;
import com.gp.cong.logisoft.hibernate.dao.TpNoteDao;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingHotCodeDAO;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Owner
 */
public class LclRemarksAction extends LogiwareDispatchAction implements LclCommonConstant {

    public ActionForward addRemarks(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclRemarksForm remarksForm = (LclRemarksForm) form;
        LclRemarksDAO remarksDAO = new LclRemarksDAO();
        String priority = request.getParameter("priorityView");
        String userMail = request.getParameter("userMail");
        User user = getCurrentUser(request);
        Date now = new Date();
        if (CommonUtils.isNotEmpty(remarksForm.getEventNotes())) {
            remarksForm.getLclRemarks().setType(remarksForm.getEventNotes());
        } else {
            remarksForm.getLclRemarks().setType("Quote".equalsIgnoreCase(remarksForm.getModuleId()) ? REMARKS_QT_MANUAL_NOTES
                    : "Booking".equalsIgnoreCase(remarksForm.getModuleId()) ? REMARKS_DR_MANUAL_NOTES : REMARKS_BL_MANUAL_NOTES);
        }
        if (remarksForm.getModuleId().equals("Quote")) {
        } else {
            if (userMail.equals("on")) {
                remarksForm.getLclRemarks().setFollowupEmail(remarksForm.getFollowupEmail() + "," + user.getEmail());
            } else {
                remarksForm.getLclRemarks().setFollowupEmail(remarksForm.getFollowupEmail());
            }
        }
        remarksForm.getLclRemarks().setEnteredBy(user);
        remarksForm.getLclRemarks().setModifiedBy(user);
        remarksForm.getLclRemarks().setEnteredDatetime(now);
        remarksForm.getLclRemarks().setModifiedDatetime(now);
        if (remarksForm.getLclRemarks().getFollowupDate() != null) {
            remarksForm.getLclRemarks().setFollowUpUser(user);
        }
        if (priority.equals("on")) {
            remarksForm.getLclRemarks().setType("Priority View");
        }
        remarksDAO.save(remarksForm.getLclRemarks());
        if (!remarksForm.getModuleId().equals("Quote")) {
            LclBooking lclBooking = new LCLBookingDAO().executeUniqueQuery("from LclBooking where lclFileNumber.id = " + remarksForm.getFileNumberId() + "");
            request.setAttribute("status", lclBooking.getLclFileNumber().getStatus());
        }
        setColor(remarksDAO, remarksForm.getFileNumberId(), remarksForm.getModuleName(), remarksForm.getModuleId(), request);
        request.setAttribute("remarksList", remarksDAO.getRemarksList(remarksForm.getFileNumberId(),
                remarksForm.getActions(), remarksForm.getModuleId()));
        request.setAttribute("fileNumberId", remarksForm.getLclRemarks().getLclFileNumber().getId());
        request.setAttribute("fileNumber", remarksForm.getLclRemarks().getLclFileNumber().getFileNumber());
        remarksForm.setActions("show All");
        return mapping.findForward("newLclNotes");
    }

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        LclRemarksForm lclRemarksForm = (LclRemarksForm) form;
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        setColor(lclRemarksDAO, lclRemarksForm.getFileNumberId(), lclRemarksForm.getModuleName(),
                lclRemarksForm.getModuleId(), request);
        if (!"specialNotes".equalsIgnoreCase(lclRemarksForm.getActions())) {
            request.setAttribute("remarksList", lclRemarksDAO.getRemarksList(lclRemarksForm.getFileNumberId(),
                    lclRemarksForm.getActions(), lclRemarksForm.getModuleId()));
        } else if (lclRemarksForm.getActions() != null && lclRemarksForm.getActions().equalsIgnoreCase("specialNotes")) {
            String clntId = request.getParameter("clntId");
            request.setAttribute("clntId", clntId);
            String shpId = request.getParameter("shpId");
            request.setAttribute("shpId", shpId);
            String fwdId = request.getParameter("fwdId");
            request.setAttribute("fwdId", fwdId);
            String consId = request.getParameter("consId");
            request.setAttribute("consId", consId);
            String splNotes = request.getParameter("splNotes");
            request.setAttribute("splNotes", splNotes);
            StringBuilder allSymbols = new StringBuilder();
            String eciNoFf = null, eciNoCn = null;
            List clntEciAcctNo = null;
            List shpEciAcctNo = null;
            List fwEciAcctNo = null;
            List conEciAcctNo = null;
            List clntList = null, shpList = null, fwdList = null, consList = null;
            List<String> clNoteSymlList = null;
            List<String> shNoteSymlList = null;
            List<String> fwdNoteSymlList = null;
            List<String> consNoteSymlList = null;
            List<String> cmmnList = null;
            List<String> voidList = null;
            List spclNodesList = null;

            if (null != lclRemarksForm.getClntAcctNo() && CommonUtils.isNotEmpty(lclRemarksForm.getClntAcctNo())) {
                clntEciAcctNo = lclRemarksDAO.getEciAccnNo(lclRemarksForm.getClntAcctNo());
                if (CommonUtils.isNotEmpty(clntEciAcctNo)) {
                    Object[] eciNo = (Object[]) clntEciAcctNo.get(0);
                    if (eciNo[0] != null && !eciNo[0].toString().trim().equals("")) {
                        eciNoFf = eciNo[0].toString();
                    }
                    if (eciNo[1] != null && !eciNo[1].toString().trim().equals("")) {
                        eciNoCn = eciNo[1].toString();
                    }
                }
                if (null != eciNoFf || null != eciNoCn) {
                    if (null != clntId && CommonUtils.isNotEmpty(clntId)) {
                        clntList = lclRemarksDAO.getSpecialNotesSymbl(eciNoFf, eciNoCn, null);
                        voidList = lclRemarksDAO.getNoteSymbolVoid(eciNoFf, eciNoCn);
                        clNoteSymlList = lclRemarksDAO.getNoteSymbol(eciNoFf, eciNoCn);
                        cmmnList = clNoteSymlList;
                    } else {
                        clntList = lclRemarksDAO.getSpecialNotesProp(eciNoFf, eciNoCn);
                    }
                }
            }
            if (null != lclRemarksForm.getShpAcctNo() && CommonUtils.isNotEmpty(lclRemarksForm.getShpAcctNo())) {
                shpEciAcctNo = lclRemarksDAO.getEciAccnNo(lclRemarksForm.getShpAcctNo());
                if (CommonUtils.isNotEmpty(shpEciAcctNo)) {
                    Object[] eciNo = (Object[]) shpEciAcctNo.get(0);
                    if (eciNo[0] != null && !eciNo[0].toString().trim().equals("")) {
                        eciNoFf = eciNo[0].toString();
                    }
                    if (eciNo[1] != null && !eciNo[1].toString().trim().equals("")) {
                        eciNoCn = eciNo[1].toString();
                    }
                }
                if (null != eciNoFf || null != eciNoCn) {
                    if (null != shpId && CommonUtils.isNotEmpty(shpId)) {
                        shpList = lclRemarksDAO.getSpecialNotesSymbl(eciNoFf, eciNoCn, null);
                        voidList = lclRemarksDAO.getNoteSymbolVoid(eciNoFf, eciNoCn);
                        shNoteSymlList = lclRemarksDAO.getNoteSymbol(eciNoFf, eciNoCn);
                        cmmnList = shNoteSymlList;
                    } else {
                        shpList = lclRemarksDAO.getSpecialNotesProp(eciNoFf, null);
                    }
                }
            }
            if (null != lclRemarksForm.getFrwdAcctNo() && CommonUtils.isNotEmpty(lclRemarksForm.getFrwdAcctNo())) {
                fwEciAcctNo = lclRemarksDAO.getEciAccnNo(lclRemarksForm.getFrwdAcctNo());
                if (CommonUtils.isNotEmpty(fwEciAcctNo)) {
                    Object[] eciNo = (Object[]) fwEciAcctNo.get(0);
                    if (eciNo[0] != null && !eciNo[0].toString().trim().equals("")) {
                        eciNoFf = eciNo[0].toString();
                    }
                    if (eciNo[1] != null && !eciNo[1].toString().trim().equals("")) {
                        eciNoCn = eciNo[1].toString();
                    }
                }
                if (null != eciNoFf || null != eciNoCn) {
                    if (null != fwdId && CommonUtils.isNotEmpty(fwdId)) {
                        fwdList = lclRemarksDAO.getSpecialNotesSymbl(eciNoFf, eciNoCn, null);
                        voidList = lclRemarksDAO.getNoteSymbolVoid(eciNoFf, eciNoCn);
                        fwdNoteSymlList = lclRemarksDAO.getNoteSymbol(eciNoFf, eciNoCn);
                        cmmnList = fwdNoteSymlList;
                    } else {
                        fwdList = lclRemarksDAO.getSpecialNotesProp(eciNoFf, eciNoCn);
                    }
                }
            }
            if (null != lclRemarksForm.getConsAcctNo() && CommonUtils.isNotEmpty(lclRemarksForm.getConsAcctNo())) {
                conEciAcctNo = lclRemarksDAO.getEciAccnNo(lclRemarksForm.getConsAcctNo());
                if (CommonUtils.isNotEmpty(conEciAcctNo)) {
                    Object[] eciNo = (Object[]) conEciAcctNo.get(0);
                    if (eciNo[0] != null && !eciNo[0].toString().trim().equals("")) {
                        eciNoFf = eciNo[0].toString();
                    }
                    if (eciNo[1] != null && !eciNo[1].toString().trim().equals("")) {
                        eciNoCn = eciNo[1].toString();
                    }
                }
                if (null != eciNoFf || null != eciNoCn) {
                    if (null != consId && CommonUtils.isNotEmpty(consId)) {
                        consList = lclRemarksDAO.getSpecialNotesSymbl(eciNoFf, eciNoCn, null);
                        voidList = lclRemarksDAO.getNoteSymbolVoid(eciNoFf, eciNoCn);
                        consNoteSymlList = lclRemarksDAO.getNoteSymbol(eciNoFf, eciNoCn);
                        cmmnList = consNoteSymlList;
                    } else {
                        consList = lclRemarksDAO.getSpecialNotesProp(eciNoFf, eciNoCn);
                    }
                }
            }
            if (null != cmmnList && CommonUtils.isNotEmpty(cmmnList)) {
                for (String symbol : cmmnList) {
                    allSymbols.append(symbol).append('-');
                }
                if (null != voidList && CommonUtils.isNotEmpty(voidList)) {
                    allSymbols.append('V');
                }
                request.setAttribute("ntesymb", allSymbols.toString().trim());
            }
            if (null != lclRemarksForm.getSplNotes() && CommonUtils.isNotEmpty(lclRemarksForm.getSplNotes())) {
                if ("acctNotes".equalsIgnoreCase(lclRemarksForm.getSplNotes())) {
                    if (null != eciNoFf || null != eciNoCn) {
                        spclNodesList = lclRemarksDAO.getSpecialNotesSymbl(eciNoFf, eciNoCn, "$");
                        request.setAttribute("remarksList", spclNodesList);
                    }
                }
                if ("airNotes".equalsIgnoreCase(lclRemarksForm.getSplNotes())) {
                    if (null != eciNoFf || null != eciNoCn) {
                        spclNodesList = lclRemarksDAO.getSpecialNotesSymbl(eciNoFf, eciNoCn, "A");
                        request.setAttribute("remarksList", spclNodesList);
                    }
                }
                if ("autoNotes".equalsIgnoreCase(lclRemarksForm.getSplNotes())) {
                    if (null != eciNoFf || null != eciNoCn) {
                        spclNodesList = lclRemarksDAO.getSpecialNotesSymbl(eciNoFf, eciNoCn, "*");
                        request.setAttribute("remarksList", spclNodesList);
                    }
                }
                if ("docNotes".equalsIgnoreCase(lclRemarksForm.getSplNotes())) {
                    if (null != eciNoFf || null != eciNoCn) {
                        spclNodesList = lclRemarksDAO.getSpecialNotesSymbl(eciNoFf, eciNoCn, "D");
                        request.setAttribute("remarksList", spclNodesList);
                    }
                }
                if ("fclNotes".equalsIgnoreCase(lclRemarksForm.getSplNotes())) {
                    if (null != eciNoFf || null != eciNoCn) {
                        spclNodesList = lclRemarksDAO.getSpecialNotesSymbl(eciNoFf, eciNoCn, "F");
                        request.setAttribute("remarksList", spclNodesList);
                    }
                }
                if ("genrlNotes".equalsIgnoreCase(lclRemarksForm.getSplNotes())) {
                    if (null != eciNoFf || null != eciNoCn) {
                        spclNodesList = lclRemarksDAO.getSpecialNotesSymbl(eciNoFf, eciNoCn, "G");
                        request.setAttribute("remarksList", spclNodesList);
                    }
                }
                if ("lclNotes".equalsIgnoreCase(lclRemarksForm.getSplNotes())) {
                    if (null != eciNoFf || null != eciNoCn) {
                        spclNodesList = lclRemarksDAO.getSpecialNotesSymbl(eciNoFf, eciNoCn, "L");
                        request.setAttribute("remarksList", spclNodesList);
                    }
                }
                if ("salesNotes".equalsIgnoreCase(lclRemarksForm.getSplNotes())) {
                    if (null != eciNoFf || null != eciNoCn) {
                        spclNodesList = lclRemarksDAO.getSpecialNotesSymbl(eciNoFf, eciNoCn, "S");
                        request.setAttribute("remarksList", spclNodesList);
                    }
                }
                if ("voidNotes".equalsIgnoreCase(lclRemarksForm.getSplNotes())) {
                    if (null != eciNoFf || null != eciNoCn) {
                        spclNodesList = lclRemarksDAO.getSpecialNotesSymbl(eciNoFf, eciNoCn, "V");
                        request.setAttribute("remarksList", spclNodesList);
                    }
                }
                if ("whseNotes".equalsIgnoreCase(lclRemarksForm.getSplNotes())) {
                    if (null != eciNoFf || null != eciNoCn) {
                        spclNodesList = lclRemarksDAO.getSpecialNotesSymbl(eciNoFf, eciNoCn, "W");
                        request.setAttribute("remarksList", spclNodesList);
                    }
                }
                if ("importNotes".equalsIgnoreCase(lclRemarksForm.getSplNotes())) {
                    if (null != eciNoFf || null != eciNoCn) {
                        spclNodesList = lclRemarksDAO.getSpecialNotesSymbl(eciNoFf, eciNoCn, "I");
                        request.setAttribute("remarksList", spclNodesList);
                    }
                }
            } else {
                if (null != clntList && CommonUtils.isNotEmpty(clntList) && shpList == null && fwdList == null && consList == null) {
                    request.setAttribute("remarksList", clntList);
                }
                if (null != shpList && CommonUtils.isNotEmpty(shpList) && clntList == null && fwdList == null && consList == null) {
                    request.setAttribute("remarksList", shpList);
                }
                if (null != fwdList && CommonUtils.isNotEmpty(fwdList) && clntList == null && shpList == null && consList == null) {
                    request.setAttribute("remarksList", fwdList);
                }
                if (null != consList && CommonUtils.isNotEmpty(consList) && clntList == null && shpList == null && fwdList == null) {
                    request.setAttribute("remarksList", consList);
                }

                if ((null != clntList && CommonUtils.isNotEmpty(clntList)) && (null != shpList && CommonUtils.isNotEmpty(shpList)) && null == fwdList && null == consList) {
                    shpList.addAll(clntList);
                    request.setAttribute("remarksList", shpList);
                }
                if ((null != clntList && CommonUtils.isNotEmpty(clntList)) && (null != fwdList && CommonUtils.isNotEmpty(fwdList)) && null == shpList && null == consList) {
                    fwdList.addAll(clntList);
                    request.setAttribute("remarksList", fwdList);
                }
                if ((null != clntList && CommonUtils.isNotEmpty(clntList)) && (null != consList && CommonUtils.isNotEmpty(consList)) && null == shpList && null == fwdList) {
                    consList.addAll(clntList);
                    request.setAttribute("remarksList", consList);
                }
                if ((null != shpList && CommonUtils.isNotEmpty(shpList)) && (null != fwdList && CommonUtils.isNotEmpty(fwdList)) && null == consList && null == clntList) {
                    fwdList.addAll(shpList);
                    request.setAttribute("remarksList", fwdList);
                }
                if ((null != shpList && CommonUtils.isNotEmpty(shpList)) && (null != consList && CommonUtils.isNotEmpty(consList)) && null == fwdList && null == clntList) {
                    consList.addAll(shpList);
                    request.setAttribute("remarksList", consList);
                }
                if ((null != fwdList && CommonUtils.isNotEmpty(fwdList)) && (null != consList && CommonUtils.isNotEmpty(consList)) && null == clntList && null == shpList) {
                    consList.addAll(fwdList);
                    request.setAttribute("remarksList", consList);
                }
                if ((null != shpList && CommonUtils.isNotEmpty(shpList)) && (null != fwdList && CommonUtils.isNotEmpty(fwdList)) && (null != consList && CommonUtils.isNotEmpty(consList)) && null == clntList) {
                    fwdList.addAll(shpList);
                    consList.addAll(fwdList);
                    request.setAttribute("remarksList", consList);
                }
                if ((null != clntList && CommonUtils.isNotEmpty(clntList)) && (null != fwdList && CommonUtils.isNotEmpty(fwdList)) && (null != consList && CommonUtils.isNotEmpty(consList)) && null == shpList) {
                    fwdList.addAll(clntList);
                    consList.addAll(fwdList);
                    request.setAttribute("remarksList", consList);
                }
                if ((null != clntList && CommonUtils.isNotEmpty(clntList)) && (null != shpList && CommonUtils.isNotEmpty(shpList)) && (null != consList && CommonUtils.isNotEmpty(consList)) && null == fwdList) {
                    shpList.addAll(clntList);
                    consList.addAll(shpList);
                    request.setAttribute("remarksList", consList);
                }
                if ((null != clntList && CommonUtils.isNotEmpty(clntList)) && (null != shpList && CommonUtils.isNotEmpty(shpList)) && (null != fwdList && CommonUtils.isNotEmpty(fwdList)) && null == consList) {
                    shpList.addAll(clntList);
                    fwdList.addAll(shpList);
                    request.setAttribute("remarksList", fwdList);
                }
                if ((null != clntList && CommonUtils.isNotEmpty(clntList)) && (null != shpList && CommonUtils.isNotEmpty(shpList)) && (null != fwdList && CommonUtils.isNotEmpty(fwdList)) && (null != consList && CommonUtils.isNotEmpty(consList))) {
                    shpList.addAll(clntList);
                    fwdList.addAll(shpList);
                    consList.addAll(fwdList);
                    request.setAttribute("remarksList", consList);
                }
            }
        }
        if (lclRemarksForm.getModuleId() == null) {
            LclBooking lclBooking = new LCLBookingDAO().executeUniqueQuery("from LclBooking where lclFileNumber.id = " + lclRemarksForm.getFileNumberId() + "");
            request.setAttribute("status", lclBooking.getLclFileNumber().getStatus());
        }
        request.setAttribute("lclRemarksForm", lclRemarksForm);
        return mapping.findForward("newLclNotes");
    }

    public ActionForward closeNotes(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclRemarksForm lclRemarksForm = (LclRemarksForm) form;
        User loginUser = getCurrentUser(request);
        LclRemarksDAO remarksDAO = new LclRemarksDAO();
        LclRemarks lclRemarks = remarksDAO.findById(lclRemarksForm.getId());
        String remarks = "Deleted -> Manual Note : " + lclRemarks.getRemarks();
        String remarksType = "Quote".equalsIgnoreCase(lclRemarksForm.getModuleId()) ? REMARKS_QT_AUTO_NOTES
                : "Booking".equalsIgnoreCase(lclRemarksForm.getModuleId()) ? REMARKS_DR_AUTO_NOTES : REMARKS_BL_AUTO_NOTES;
        remarksDAO.insertLclRemarks(lclRemarks.getLclFileNumber().getId(), remarksType, remarks, loginUser.getUserId());
        lclRemarks.setStatus("Closed");
        lclRemarks.setType("void");
        lclRemarks.setModifiedDatetime(new Date());
        lclRemarks.setModifiedBy(loginUser);
        remarksDAO.update(lclRemarks);
        request.setAttribute("fileNumberId", lclRemarksForm.getLclRemarks().getLclFileNumber().getId());
        request.setAttribute("fileNumber", lclRemarksForm.getLclRemarks().getLclFileNumber().getFileNumber());

        setColor(remarksDAO, lclRemarksForm.getFileNumberId(), lclRemarksForm.getModuleName(),
                lclRemarksForm.getModuleId(), request);
        return display(mapping, form, request, response);
    }

    public ActionForward refreshPriorityNotes(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclRemarksForm lclRemarksForm = (LclRemarksForm) form;
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        User loginUser = getCurrentUser(request);
        Date now = new Date();
        String priority = request.getParameter("priorityView");
        if (CommonUtils.isNotEmpty(lclRemarksForm.getEventNotes())) {
            lclRemarksForm.getLclRemarks().setType(lclRemarksForm.getEventNotes());
        }
        lclRemarksForm.getLclRemarks().setEnteredBy(loginUser);
        lclRemarksForm.getLclRemarks().setModifiedBy(loginUser);
        lclRemarksForm.getLclRemarks().setEnteredDatetime(now);
        lclRemarksForm.getLclRemarks().setModifiedDatetime(now);
        if (priority.equals("on")) {
            lclRemarksForm.getLclRemarks().setType("Priority View");
        }
        lclRemarksDAO.save(lclRemarksForm.getLclRemarks());
        request.setAttribute("lclRemarksPriority", lclRemarksDAO.executeQuery("Select remarks FROM LclRemarks WHERE lclFileNumber.id=" + lclRemarksForm.getFileNumberId() + " AND type='Priority View'  ORDER BY modified_datetime DESC"));
        return mapping.findForward("refreshPriorityNotes");
    }
    
    public ActionForward  displayPriorityNotes(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclRemarksForm lclRemarksForm = (LclRemarksForm) form;
         LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        String fileId = request.getParameter("fileNumberId");      
        String[] type = {"Priority View"};
        if (null != fileId && !"".equals(fileId)) {
            List<LclRemarks> lcl = lclRemarksDAO.getAllRemarksByType(fileId, type);
            request.setAttribute("remarksList", lcl);
        }
        request.setAttribute("lclRemarksForm", lclRemarksForm);
        return mapping.findForward("displayPriorityNotes");
    }   

    public ActionForward sendEdiToCtsList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        String fileId = request.getParameter("fileNumberId");
        String histEdi = request.getParameter("histEdi");
        request.setAttribute("histEdi", histEdi);
        String[] type = {"CTS", "CTS_EDI"};
        if (null != fileId && !"".equals(fileId)) {
            List<LclRemarks> lcl = lclRemarksDAO.getAllRemarksByType(fileId, type);
            request.setAttribute("remarksList", lcl);
        }
        return mapping.findForward("newLclNotes");
    }

    public ActionForward updateRemarks(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclRemarksForm lclRemarksForm = (LclRemarksForm) form;
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        User user = (User) request.getSession().getAttribute("loginuser");
        Date now = new Date();
        LclRemarks lclRemarks = lclRemarksDAO.findById(lclRemarksForm.getLclRemarks().getId());
        String remarksType = "Quote".equalsIgnoreCase(lclRemarksForm.getModuleId()) ? REMARKS_QT_AUTO_NOTES
                : "Booking".equalsIgnoreCase(lclRemarksForm.getModuleId()) ? REMARKS_DR_AUTO_NOTES : REMARKS_BL_AUTO_NOTES;
        LclRemarks autoLclRemarks = new LclRemarks();
        autoLclRemarks.setEnteredBy(getCurrentUser(request));
        autoLclRemarks.setModifiedBy(getCurrentUser(request));
        autoLclRemarks.setEnteredDatetime(now);
        autoLclRemarks.setModifiedDatetime(now);

        lclRemarks.setModifiedBy(getCurrentUser(request));
        lclRemarks.setModifiedDatetime(now);
        if (lclRemarksForm.getLclRemarks().getFollowupDate() != null) {
            lclRemarks.setFollowUpUser(user);
            lclRemarks.setFollowupDate(lclRemarksForm.getLclRemarks().getFollowupDate());
        }
        lclRemarks.setFollowupEmail(lclRemarksForm.getLclRemarks().getFollowupEmail());
        autoLclRemarks.setFollowupEmail(lclRemarksForm.getLclRemarks().getFollowupEmail());
        autoLclRemarks.setRemarks("Manual Note : " + lclRemarks.getRemarks() + " -> " + lclRemarksForm.getLclRemarks().getRemarks());
        autoLclRemarks.setLclFileNumber(lclRemarks.getLclFileNumber());
        autoLclRemarks.setType(remarksType);
        String oldRemark = lclRemarks.getRemarks();
        lclRemarks.setRemarks(lclRemarksForm.getLclRemarks().getRemarks());
        if (!CommonUtils.isEqual(oldRemark, lclRemarksForm.getLclRemarks().getRemarks())) {
            lclRemarksDAO.save(autoLclRemarks);
        }
        if (!lclRemarksForm.getModuleId().equals("Quote")) {
            LclBooking lclBooking = new LCLBookingDAO().executeUniqueQuery("from LclBooking where lclFileNumber.id = " + lclRemarksForm.getFileNumberId() + "");
            request.setAttribute("status", lclBooking.getLclFileNumber().getStatus());
        }
//	request.setAttribute("remarksList", lclRemarksDAO.executeQuery("from LclRemarks where lclFileNumber.id= " + lclRemarksForm.getFileNumberId() + " AND type = 'Manual Note' AND  status is null ORDER BY id DESC"));
        request.setAttribute("fileNumberId", lclRemarksForm.getLclRemarks().getLclFileNumber().getId());
        request.setAttribute("fileNumber", lclRemarksForm.getLclRemarks().getLclFileNumber().getFileNumber());

        setColor(lclRemarksDAO, lclRemarksForm.getFileNumberId(), lclRemarksForm.getModuleName(),
                lclRemarksForm.getModuleId(), request);
        return display(mapping, form, request, response);
    }

    public ActionForward addLclTpNote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclRemarksForm lclRemarksForm = (LclRemarksForm) form;
        List shipAcctNo = null;
        List consAcctNo = null;
        List frwAcctNo = null;
        PrintWriter out = response.getWriter();
        TpNote tpNote = new TpNote();
        TpNoteDao tpNoteDao = new TpNoteDao();
        shipAcctNo = tpNoteDao.getAcctNo(lclRemarksForm.getShpAcctNo());
        consAcctNo = tpNoteDao.getAcctNo(lclRemarksForm.getConsAcctNo());
        frwAcctNo = tpNoteDao.getAcctNo(lclRemarksForm.getFrwdAcctNo());
        if (CommonUtils.isNotEmpty(shipAcctNo)) {
            Object[] shipAcct = (Object[]) shipAcctNo.get(0);
            if (shipAcct[0] != null && !shipAcct[0].equals("")) {
                tpNote.setActnum((String) shipAcct[0]);
            } else {
                tpNote.setActnum("");
            }
            if (shipAcct[1] != null && !shipAcct[1].equals("")) {
                tpNote.setConnum((String) shipAcct[1]);
            } else {
                tpNote.setConnum("");
            }
        } else if (CommonUtils.isNotEmpty(consAcctNo)) {
            Object[] consAcct = (Object[]) consAcctNo.get(0);
            if (consAcct[0] != null && !consAcct[0].equals("")) {
                tpNote.setActnum((String) consAcct[0]);
            } else {
                tpNote.setActnum("");
            }
            if (consAcct[1] != null && !consAcct[1].equals("")) {
                tpNote.setConnum((String) consAcct[1]);
            } else {
                tpNote.setConnum("");
            }
        } else if (CommonUtils.isNotEmpty(frwAcctNo)) {
            Object[] frwAcct = (Object[]) consAcctNo.get(0);
            if (frwAcct[0] != null && !frwAcct[0].equals("")) {
                tpNote.setActnum((String) frwAcct[0]);
            } else {
                tpNote.setActnum("");
            }
            if (frwAcct[1] != null && !frwAcct[1].equals("")) {
                tpNote.setConnum((String) frwAcct[1]);
            } else {
                tpNote.setConnum("");
            }
        } else {
            tpNote.setActnum("");
            tpNote.setConnum("");
        }
        tpNote.setNoteType(lclRemarksForm.getNoteType().toUpperCase());
        if (CommonUtils.isNotEmpty(lclRemarksForm.getNoteDesc())) {
            tpNote.setNoteDesc(lclRemarksForm.getNoteDesc().toUpperCase());
        }
        tpNote.setVoidedDatetime(null);
        tpNote.setEnteredDatetime(new Date());
        tpNote.setModifiedDatetime(new Date());
        tpNote.setEnteredBy(getCurrentUser(request));
        tpNote.setModifiedBy(getCurrentUser(request));
        tpNote.setVoidedBy(getCurrentUser(request));
        tpNote.setVoided(false);
        if (CommonUtils.isNotEmpty(lclRemarksForm.getTpId())) {
            tpNote.setId(lclRemarksForm.getTpId());
            tpNoteDao.saveOrUpdate(tpNote);
        } else {
            tpNoteDao.save(tpNote);
        }
        if (lclRemarksForm.getNoteType().equalsIgnoreCase("$")) {
            out.println("acctNotes");
        } else if (lclRemarksForm.getNoteType().equalsIgnoreCase("A")) {
            out.println("airNotes");
        } else if (lclRemarksForm.getNoteType().equalsIgnoreCase("D")) {
            out.println("docNotes");
        } else if (lclRemarksForm.getNoteType().equalsIgnoreCase("F")) {
            out.println("fclNotes");
        } else if (lclRemarksForm.getNoteType().equalsIgnoreCase("G")) {
            out.println("genrlNotes");
        } else if (lclRemarksForm.getNoteType().equalsIgnoreCase("L")) {
            out.println("lclNotes");
        } else if (lclRemarksForm.getNoteType().equalsIgnoreCase("W")) {
            out.println("whseNotes");
        } else if (lclRemarksForm.getNoteType().equalsIgnoreCase("I")) {
            out.println("importNotes");
        }
        out.flush();
        out.close();
        return null;
    }
    public ActionForward displayOnHold(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclRemarksForm lclRemarksForm = (LclRemarksForm) form;
        request.setAttribute("lclRemarksForm", lclRemarksForm);
        return mapping.findForward("display");
    }
    public ActionForward deleteLclTpNote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclRemarksForm lclRemarksForm = (LclRemarksForm) form;
        TpNoteDao tpNoteDao = new TpNoteDao();
        List shipAcctNo = null;
        List consAcctNo = null;
        List frwAcctNo = null;
        TpNote tpNote = new TpNote();
        shipAcctNo = tpNoteDao.getAcctNo(lclRemarksForm.getShpAcctNo());
        consAcctNo = tpNoteDao.getAcctNo(lclRemarksForm.getConsAcctNo());
        frwAcctNo = tpNoteDao.getAcctNo(lclRemarksForm.getFrwdAcctNo());
        if (CommonUtils.isNotEmpty(shipAcctNo)) {
            Object[] shipAcct = (Object[]) shipAcctNo.get(0);
            if (shipAcct[0] != null && !shipAcct[0].equals("")) {
                tpNote.setActnum((String) shipAcct[0]);
            } else {
                tpNote.setActnum("");
            }
            if (shipAcct[1] != null && !shipAcct[1].equals("")) {
                tpNote.setConnum((String) shipAcct[1]);
            } else {
                tpNote.setConnum("");
            }
        } else if (CommonUtils.isNotEmpty(consAcctNo)) {
            Object[] consAcct = (Object[]) consAcctNo.get(0);
            if (consAcct[0] != null && !consAcct[0].equals("")) {
                tpNote.setActnum((String) consAcct[0]);
            } else {
                tpNote.setActnum("");
            }
            if (consAcct[1] != null && !consAcct[1].equals("")) {
                tpNote.setConnum((String) consAcct[1]);
            } else {
                tpNote.setConnum("");
            }
        } else if (CommonUtils.isNotEmpty(frwAcctNo)) {
            Object[] frwAcct = (Object[]) consAcctNo.get(0);
            if (frwAcct[0] != null && !frwAcct[0].equals("")) {
                tpNote.setActnum((String) frwAcct[0]);
            } else {
                tpNote.setActnum("");
            }
            if (frwAcct[1] != null && !frwAcct[1].equals("")) {
                tpNote.setConnum((String) frwAcct[1]);
            } else {
                tpNote.setConnum("");
            }
        } else {
            tpNote.setActnum("");
            tpNote.setConnum("");
        }
        tpNote.setNoteType("V");
        if (CommonUtils.isNotEmpty(lclRemarksForm.getNoteDesc())) {
            tpNote.setNoteDesc(lclRemarksForm.getNoteDesc().toUpperCase());
        }
        tpNote.setVoidedDatetime(new Date());
        tpNote.setEnteredDatetime(new Date());
        tpNote.setModifiedDatetime(new Date());
        tpNote.setEnteredBy(getCurrentUser(request));
        tpNote.setModifiedBy(getCurrentUser(request));
        tpNote.setVoidedBy(getCurrentUser(request));
        tpNote.setVoided(true);
        tpNoteDao.delete(lclRemarksForm.getTpId());
        tpNoteDao.save(tpNote);
        return null;
    }

    public void setColor(LclRemarksDAO remarksDAO, Long fileId, String moduleName,
            String screenName, HttpServletRequest request) throws Exception {
        if (CommonUtils.isNotEmpty(fileId)) {
            String[] remarkTypes = new String[]{"Manual Note", "Quote".equalsIgnoreCase(screenName)
                ? REMARKS_QT_MANUAL_NOTES : "Booking".equalsIgnoreCase(screenName) ? REMARKS_DR_MANUAL_NOTES : REMARKS_BL_MANUAL_NOTES};
            Boolean manualFlag = remarksDAO.isRemarks(fileId, remarkTypes);
            if ("Exports".equalsIgnoreCase(moduleName)) {
                String[] voidList = new String[]{"void"};
                Boolean voidedNotesFlag = remarksDAO.isRemarks(fileId, voidList);
                if (voidedNotesFlag) {
                    request.setAttribute("setColor", "true");
                }
                Boolean hotCodeFlag = new LclBookingHotCodeDAO().isHotCodeValidate(fileId, "XXX");
                manualFlag = !manualFlag ? hotCodeFlag : manualFlag;
            }
            request.setAttribute("greenColor", manualFlag);
        }
    }
}

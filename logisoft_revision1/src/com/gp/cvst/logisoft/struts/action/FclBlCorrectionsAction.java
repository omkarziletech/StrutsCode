/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cvst.logisoft.struts.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.gp.cong.logisoft.bc.fcl.FclBlConstants;
import com.gp.cong.logisoft.bc.fcl.FclBlCorrectionsBC;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.CodetypeDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.ItemDAO;
import com.gp.cong.logisoft.hibernate.dao.NotesDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlCorrections;
import com.gp.cvst.logisoft.hibernate.dao.FclBlCorrectionsDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cvst.logisoft.struts.form.FclBlCorrectionsForm;
import com.logiware.action.EventAction;
import com.logiware.fcl.form.SessionForm;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * MyEclipse Struts Creation date: 05-28-2009
 *
 * XDoclet definition:
 *
 * @struts.action path="/fclBlCorrections" name="fclBlCorrectionsForm"
 * input="/jsps/fclQuotes/FclBlCorrections.jsp" scope="request" validate="true"
 */
public class FclBlCorrectionsAction extends EventAction {
    /*
     * Generated Methods
     */

    /**
     * Method execute
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlCorrectionsForm fclBlCorrectionsForm = (FclBlCorrectionsForm) form;// TODO
        // Auto-generated
        // method
        // stub
        super.registerEvent(form, request, response);
        String forwardName = "";
        String fileNo = "";
        String blNumber;
        MessageResources messageResources = getResources(request);
        String buttonValue = fclBlCorrectionsForm.getButtonValue();
        HttpSession session = request.getSession(true);
        FclBlCorrectionsBC correctionsBC = new FclBlCorrectionsBC();
        FclBlCorrections fclBlCorrections = new FclBlCorrections();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        String userName = null;
        User user = new User();
        if (session.getAttribute("loginuser") != null) {
            user = (User) session.getAttribute("loginuser");
            userName = user.getLoginName();
        }
        String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
        if ("02".equals(companyCode)) {
            request.setAttribute("companyCode", "OTIC");
        } else {
            request.setAttribute("companyCode", "ECCI");
        }
        if (fclBlCorrectionsForm.getCorrectionType() == null) {
            fclBlCorrectionsForm.setCorrectionType(fclBlCorrectionsForm.getNewTempCorrectionType());
        }
        if (fclBlCorrectionsForm.getCorrectionType() != null
                && !fclBlCorrectionsForm.getCorrectionType().equals("")) {
            GenericCode crType = genericCodeDAO.findById(Integer.parseInt(fclBlCorrectionsForm.getCorrectionType()));
            fclBlCorrections.setCorrectionType(crType);
        }
        FclBl bl = new FclBl();
        if (fclBlCorrectionsForm.getFileNo() != null && !fclBlCorrectionsForm.getFileNo().isEmpty()) {
            fileNo = fclBlCorrectionsForm.getFileNo();
            request.setAttribute("fileNo", fileNo);
            bl = new FclBlDAO().getFileNoObject(fileNo);
        }
        boolean importFlag = null != bl && "I".equalsIgnoreCase(bl.getImportFlag());
        if (fclBlCorrectionsForm.getBlNumber() != null) {
            blNumber = fclBlCorrectionsForm.getBlNumber();
            request.setAttribute("displayBlNumber", blNumber);
        }
        if (request.getParameter("buttonValue") != null && request.getParameter("buttonValue").equalsIgnoreCase("FclBl")) {
            fclBlCorrectionsForm.setTemp("FclBl");
            fclBlCorrectionsForm.setBlNumber(request.getParameter("blNumber"));
            fclBlCorrectionsForm.setFileNo(request.getParameter("fileNo"));
            correctionsBC = new FclBlCorrectionsBC();
            if (request.getParameter("blNumber") != null) {
                String checkStatus = correctionsBC.checkStatus(request.getParameter("blNumber"), request);
                if (checkStatus != null) {
                    fclBlCorrectionsForm.setCheckStatus(checkStatus);
                }
            }
            request.setAttribute(FclBlConstants.BLCORRECTIONFORM, fclBlCorrectionsForm);
            List serachResultList = correctionsBC.listToDisplayOnSearchPage(fclBlCorrectionsForm);
            request.setAttribute(FclBlConstants.BLCORRECTIONLIST, serachResultList);
            if (fclBlCorrectionsForm.getFileNo() != null) {
                request.setAttribute("ManualNotes", new NotesDAO().isManualNotesCorrection(fileNo.substring(0, -1 != fileNo.indexOf("-") ? fileNo.indexOf("-") : fileNo.length())));
            }
            forwardName = "fclBlCorrections";
            if (fclBlCorrectionsForm.isQuickCn()) {
                Integer correctionTypeId = new CodetypeDAO().getCodeTypeId("Bl Correction Type");
                GenericCode correctionType = new GenericCodeDAO().getGenericCodeByCode("A", correctionTypeId);
                request.setAttribute("correctionType", correctionType);
                Integer correctionCodeId = new CodetypeDAO().getCodeTypeId("Bl Correction Code");
                GenericCode correctionCode = new GenericCodeDAO().getGenericCodeByCode("003", correctionCodeId);
                request.setAttribute("correctionCode", correctionCode);
            }
            request.setAttribute("importFlag", importFlag);
            return mapping.findForward(forwardName);
        }
        if (buttonValue != null && buttonValue.equals("search")) {
            request.setAttribute("userId", user.getUserId());
            if (importFlag) {
                request.setAttribute("itemId", new ItemDAO().getItemId("Quotes, Bookings, and BLs", "IMP"));
                request.setAttribute("selectedMenu", "Imports");
                fclBlCorrectionsForm.setFileType("I");
            } else {
                request.setAttribute("itemId", new ItemDAO().getItemId("Quotes, Bookings, and BLs"));
                request.setAttribute("selectedMenu", "Exports");
                fclBlCorrectionsForm.setFileType("E");
            }
            List serachResultList = correctionsBC.listToDisplayOnSearchPage(fclBlCorrectionsForm);
            request.setAttribute(FclBlConstants.BLCORRECTIONLIST, serachResultList);
            request.setAttribute(FclBlConstants.BLCORRECTIONFORM, fclBlCorrectionsForm);
            if (CommonFunctions.isNotNullOrNotEmpty(serachResultList)) {
                SessionForm sessionForm = new SessionForm();
                PropertyUtils.copyProperties(sessionForm, fclBlCorrectionsForm);
                session.setAttribute(FclBlConstants.BLCORRECTIONFORMFORSESSION, sessionForm);
            } else {
                if (null != session.getAttribute(FclBlConstants.BLCORRECTIONFORMFORSESSION)) {
                    session.removeAttribute(FclBlConstants.BLCORRECTIONFORMFORSESSION);
                }
            }
            forwardName = "fclBlCorrections";// search page
        } else if (buttonValue != null && buttonValue.equals("add")) {
            fclBlCorrectionsForm.setIsFax("on");
            fclBlCorrectionsForm.setIsPost("on");
            request.setAttribute(FclBlConstants.BLCORRECTIONFORM, fclBlCorrectionsForm);
            request.setAttribute("importFlag", importFlag);
            forwardName = "addFclBlCorrections";
        } else if (buttonValue != null && buttonValue.equals("goBack")) {
            if ((fclBlCorrectionsForm.getTemp() != null && fclBlCorrectionsForm.getTemp().equalsIgnoreCase("FclBl"))
                    || (fclBlCorrectionsForm.getCheckStatus() != null && !fclBlCorrectionsForm.getCheckStatus().equals(""))) {
                FclBlCorrectionsForm fclBlCorectForm = new FclBlCorrectionsForm();
                fclBlCorectForm.setQuickCn(fclBlCorrectionsForm.isQuickCn());
                fclBlCorectForm.setTemp("FclBl");
                String checkStatus = correctionsBC.checkStatus(fclBlCorrectionsForm.getBlNumber(), request);
                if (checkStatus != null) {
                    fclBlCorrectionsForm.setCheckStatus(checkStatus);
                }
                request.setAttribute(FclBlConstants.BLCORRECTIONFORM, fclBlCorectForm);
                fclBlCorrectionsForm.setCorrectionCode("0");
                fclBlCorrectionsForm.setBlNumber("");
                fclBlCorrectionsForm.setNoticeNo("");
                request.setAttribute(FclBlConstants.BLCORRECTIONLIST, correctionsBC.listToDisplayOnSearchPage(fclBlCorrectionsForm));
            } else {
                if (null != session.getAttribute(FclBlConstants.BLCORRECTIONFORMFORSESSION)) {
                    SessionForm sessionForm = (SessionForm) session.getAttribute(FclBlConstants.BLCORRECTIONFORMFORSESSION);
                    PropertyUtils.copyProperties(fclBlCorrectionsForm, sessionForm);
                }
                request.setAttribute(FclBlConstants.BLCORRECTIONLIST, correctionsBC.listToDisplayOnSearchPage(fclBlCorrectionsForm));

            }
            request.setAttribute("userId", user.getUserId());
            if (importFlag) {
                request.setAttribute("itemId", new ItemDAO().getItemId("Quotes, Bookings, and BLs", "IMP"));
                request.setAttribute("selectedMenu", "Imports");
                fclBlCorrectionsForm.setFileType("I");
            } else {
                request.setAttribute("itemId", new ItemDAO().getItemId("Quotes, Bookings, and BLs"));
                request.setAttribute("selectedMenu", "Exports");
                fclBlCorrectionsForm.setFileType("E");
            }
            forwardName = "fclBlCorrections";
        } else if (buttonValue != null && buttonValue.equals("save")) {
            correctionsBC.saveCorrections(fclBlCorrectionsForm, user.getLoginName(), request, messageResources);
            if (fclBlCorrectionsForm.getCorrectionType().length() == 1) {
                fclBlCorrectionsForm.setCorrectionType(genericCodeDAO.findByCodeName(fclBlCorrectionsForm.getCorrectionType(), FclBlConstants.CODETYPEID).getId().toString());
            }
            correctionsBC.saveFclBlCorrectionDetails(fclBlCorrectionsForm, user.getLoginName(), request);
            request.setAttribute(FclBlConstants.BLCORRECTIONFORM, fclBlCorrectionsForm);
            forwardName = "addFclBlCorrections";
        } else if (buttonValue != null && buttonValue.equals("edit")) {
            if (null != session.getAttribute("FclBlChargesList")) {
                session.removeAttribute("FclBlChargesList");
            }
            correctionsBC.editCorrectionRecord(fclBlCorrectionsForm, request, messageResources);
            forwardName = "addFclBlCorrections";
        } else if (buttonValue != null && buttonValue.equals("saveMainCorrection")) {
            correctionsBC.saveFclBlCorrectionDetails(fclBlCorrectionsForm,
                    user.getLoginName(), request);
            if (null != session.getAttribute(FclBlConstants.BLCORRECTIONFORMFORSESSION)) {
                if ((fclBlCorrectionsForm.getTemp() != null && fclBlCorrectionsForm.getTemp().equalsIgnoreCase("FclBl"))
                        || (fclBlCorrectionsForm.getCheckStatus() != null && !fclBlCorrectionsForm.getCheckStatus().equals(""))) {
                    FclBlCorrectionsForm fclBlCorectForm = new FclBlCorrectionsForm();
                    fclBlCorectForm.setQuickCn(fclBlCorrectionsForm.isQuickCn());
                    fclBlCorectForm.setTemp("FclBl");
                    String checkStatus = correctionsBC.checkStatus(fclBlCorrectionsForm.getBlNumber(), request);
                    if (checkStatus != null) {
                        fclBlCorrectionsForm.setCheckStatus(checkStatus);
                    }
                    request.setAttribute(FclBlConstants.BLCORRECTIONFORM, fclBlCorectForm);
                } else {
                    if (null != session.getAttribute(FclBlConstants.BLCORRECTIONFORMFORSESSION)) {
                        SessionForm sessionForm = (SessionForm) session.getAttribute(FclBlConstants.BLCORRECTIONFORMFORSESSION);
                        PropertyUtils.copyProperties(fclBlCorrectionsForm, sessionForm);
                    }

                    request.setAttribute("userId", user.getUserId());
                    if (importFlag) {
                        request.setAttribute("itemId", new ItemDAO().getItemId("Quotes, Bookings, and BLs", "IMP"));
                        request.setAttribute("selectedMenu", "Imports");
                        fclBlCorrectionsForm.setFileType("I");
                    } else {
                        request.setAttribute("itemId", new ItemDAO().getItemId("Quotes, Bookings, and BLs"));
                        request.setAttribute("selectedMenu", "Exports");
                        fclBlCorrectionsForm.setFileType("E");
                    }
                    List serachResultList = correctionsBC.listToDisplayOnSearchPage(fclBlCorrectionsForm);
                    request.setAttribute(FclBlConstants.BLCORRECTIONLIST, serachResultList);
                    request.setAttribute(FclBlConstants.BLCORRECTIONFORM, fclBlCorrectionsForm);
                    if (CommonFunctions.isNotNullOrNotEmpty(serachResultList)) {
                        session.setAttribute(FclBlConstants.BLCORRECTIONFORMFORSESSION, fclBlCorrectionsForm);
                    } else {
                        if (null != session.getAttribute(FclBlConstants.BLCORRECTIONFORMFORSESSION)) {
                            session.removeAttribute(FclBlConstants.BLCORRECTIONFORMFORSESSION);
                        }
                    }

                }
            } else {
                fclBlCorrectionsForm.setTemp("FclBl");
                List serachResultList = correctionsBC.listToDisplayOnSearchPage(fclBlCorrectionsForm);
                request.setAttribute(FclBlConstants.BLCORRECTIONLIST, serachResultList);
                request.setAttribute(FclBlConstants.BLCORRECTIONFORM, fclBlCorrectionsForm);
            }
            forwardName = "fclBlCorrections";
            if (fclBlCorrectionsForm.isQuickCn()) {
                Integer correctionTypeId = new CodetypeDAO().getCodeTypeId("Bl Correction Type");
                GenericCode correctionType = new GenericCodeDAO().getGenericCodeByCode("A", correctionTypeId);
                request.setAttribute("correctionType", correctionType);
                Integer correctionCodeId = new CodetypeDAO().getCodeTypeId("Bl Correction Code");
                GenericCode correctionCode = new GenericCodeDAO().getGenericCodeByCode("003", correctionCodeId);
                request.setAttribute("correctionCode", correctionCode);
            }
            request.setAttribute("importFlag", importFlag);
            return mapping.findForward(forwardName);
        } else if (buttonValue != null && buttonValue.equals("postQuickCn")) {
            fclBlCorrectionsForm.setId(fclBlCorrectionsForm.getNoticeNo());
            correctionsBC.saveFclBlCorrectionDetails(fclBlCorrectionsForm, user.getLoginName(), request);
            correctionsBC.createdLatesBl(fclBlCorrectionsForm, user.getLoginName(), request);
            correctionsBC.manifestCorrections(fclBlCorrectionsForm.getBlNumber(), fclBlCorrectionsForm.getNoticeNo(), true, request);
            if (null != session.getAttribute(FclBlConstants.BLCORRECTIONFORMFORSESSION)) {
                if ((fclBlCorrectionsForm.getTemp() != null && fclBlCorrectionsForm.getTemp().equalsIgnoreCase("FclBl"))
                        || (fclBlCorrectionsForm.getCheckStatus() != null && !fclBlCorrectionsForm.getCheckStatus().equals(""))) {
                    FclBlCorrectionsForm fclBlCorectForm = new FclBlCorrectionsForm();
                    fclBlCorectForm.setQuickCn(fclBlCorrectionsForm.isQuickCn());
                    fclBlCorectForm.setTemp("FclBl");
                    String checkStatus = correctionsBC.checkStatus(fclBlCorrectionsForm.getBlNumber(), request);
                    if (checkStatus != null) {
                        fclBlCorrectionsForm.setCheckStatus(checkStatus);
                    }
                    request.setAttribute(FclBlConstants.BLCORRECTIONFORM, fclBlCorectForm);
                } else {
                    if (null != session.getAttribute(FclBlConstants.BLCORRECTIONFORMFORSESSION)) {
                        SessionForm sessionForm = (SessionForm) session.getAttribute(FclBlConstants.BLCORRECTIONFORMFORSESSION);
                        PropertyUtils.copyProperties(fclBlCorrectionsForm, sessionForm);
                    }
                    request.setAttribute("userId", user.getUserId());
                    if (importFlag) {
                        request.setAttribute("itemId", new ItemDAO().getItemId("Quotes, Bookings, and BLs", "IMP"));
                        request.setAttribute("selectedMenu", "Imports");
                        fclBlCorrectionsForm.setFileType("I");
                    } else {
                        request.setAttribute("itemId", new ItemDAO().getItemId("Quotes, Bookings, and BLs"));
                        request.setAttribute("selectedMenu", "Exports");
                        fclBlCorrectionsForm.setFileType("E");
                    }
                    List serachResultList = correctionsBC.listToDisplayOnSearchPage(fclBlCorrectionsForm);
                    request.setAttribute(FclBlConstants.BLCORRECTIONLIST, serachResultList);
                    request.setAttribute(FclBlConstants.BLCORRECTIONFORM, fclBlCorrectionsForm);
                    if (CommonFunctions.isNotNullOrNotEmpty(serachResultList)) {
                        session.setAttribute(FclBlConstants.BLCORRECTIONFORMFORSESSION, fclBlCorrectionsForm);
                    } else {
                        if (null != session.getAttribute(FclBlConstants.BLCORRECTIONFORMFORSESSION)) {
                            session.removeAttribute(FclBlConstants.BLCORRECTIONFORMFORSESSION);
                        }
                    }
                }
            } else {
                fclBlCorrectionsForm.setTemp("FclBl");
                List serachResultList = correctionsBC.listToDisplayOnSearchPage(fclBlCorrectionsForm);
                request.setAttribute(FclBlConstants.BLCORRECTIONLIST, serachResultList);
                request.setAttribute(FclBlConstants.BLCORRECTIONFORM, fclBlCorrectionsForm);
            }
            request.setAttribute("msg", "Quick CN is posted successfully");
            request.setAttribute("action", "postQuickCn");
            request.setAttribute("blId", new FclBlDAO().getOriginalBl(fileNo).getBol());
            request.setAttribute("fileNumber", fileNo);
            forwardName = "fclBlCorrections";
            return mapping.findForward(forwardName);
        }
        if (buttonValue != null && buttonValue.equals("searchFromBLPage")) {
            setRequestForAddPage(fclBlCorrectionsForm, request, session);
            forwardName = "fclBlCorrections";
        }
        if (buttonValue != null && buttonValue.equals("deleteCorrectionRecord")) {
            fclBlCorrectionsForm.setUserName(userName);
            correctionsBC.deleteCorrectionRecord(fclBlCorrectionsForm);
            fclBlCorrectionsForm.setNoticeNo("");
            // its getting disapprove while click on unapprove icon
            // FclBlBC fclBlBC = new FclBlBC();
            // fclBlBC.disAproveCorrectedBL(fclBlCorrectionsForm.getTestBoxNoticeNumber(),fclBlCorrectionsForm.getTestBoxBlNumber());
            //message = "Correction Deleted";
            //request.setAttribute("msg", message);
            setRequestForAddPage(fclBlCorrectionsForm, request, session);
            forwardName = "fclBlCorrections";
        }
        if (buttonValue != null && buttonValue.equals("displayingCharges")) {
            fclBlCorrectionsForm.setIsFax("on");
            fclBlCorrectionsForm.setIsPost("on");
            FclBlCorrectionsDAO fclBlCorrectionsDAO = new FclBlCorrectionsDAO();
            String profits = fclBlCorrectionsDAO.correctionProfits(fileNo, "");
            String currentProfit = profits.substring(0, profits.indexOf(","));
            String profitAftrCn = profits.substring(profits.indexOf(",") + 1);
            fclBlCorrectionsForm.setCurrentProfit(currentProfit);
            fclBlCorrectionsForm.setProfitAfterCn(profitAftrCn);
            correctionsBC.getListFromFCLBLForAddnew(fclBlCorrectionsForm, request, messageResources);
            request.setAttribute(FclBlConstants.BLCORRECTIONFORM, fclBlCorrectionsForm);
            forwardName = "addFclBlCorrections";
        }
        if (buttonValue != null && buttonValue.equals("deleteCharges")) {
            correctionsBC.deleteEachRowofCharges(fclBlCorrectionsForm,
                    request, messageResources);
            if (fclBlCorrectionsForm.getCorrectionType().length() == 1) {
                fclBlCorrectionsForm.setCorrectionType(genericCodeDAO.findByCodeName(fclBlCorrectionsForm.getCorrectionType(), FclBlConstants.CODETYPEID).getId().toString());
            }
            correctionsBC.saveFclBlCorrectionDetails(fclBlCorrectionsForm, user.getLoginName(), request);
            request.setAttribute(FclBlConstants.BLCORRECTIONFORM, fclBlCorrectionsForm);
            forwardName = "addFclBlCorrections";
        } else if (buttonValue != null && buttonValue.equals("CheckPassword")) {
            User userobject = new User();
            if (session.getAttribute("loginuser") != null) {
                userobject = (User) session.getAttribute("loginuser");
            }
            String validUser = correctionsBC.checkPassword(userobject.getLoginName(), fclBlCorrectionsForm);
            if ("valid".equalsIgnoreCase(validUser)) {
                request.setAttribute("valid", fclBlCorrectionsForm.getIndex1());
                correctionsBC.createdLatesBl(fclBlCorrectionsForm, userobject.getLoginName(), request);
                correctionsBC.manifestCorrections(fclBlCorrectionsForm.getBlNumber(), fclBlCorrectionsForm.getId(), false, request);
            } else {
                request.setAttribute("valid", "inValid");
            }
            request.setAttribute(FclBlConstants.BLCORRECTIONFORM, fclBlCorrectionsForm);
            forwardName = "authanticatePassword";
        } else if (buttonValue != null && buttonValue.equals("clearSearch")) {
            if (session.getAttribute(FclBlConstants.BLCORRECTIONLIST) != null) {
                session.removeAttribute(FclBlConstants.BLCORRECTIONLIST);
            }
            if (null != session.getAttribute(FclBlConstants.BLCORRECTIONFORMFORSESSION)) {
                session.removeAttribute(FclBlConstants.BLCORRECTIONFORMFORSESSION);
            }
            forwardName = "fclBlCorrections";
        } else if (buttonValue != null
                && buttonValue.equals("manifestCorrections")) {
            fclBlCorrectionsForm.setViewMode("view");
            correctionsBC.editCorrectionRecord(fclBlCorrectionsForm,
                    request, messageResources);
            correctionsBC.manifestCorrections(fclBlCorrectionsForm.getBlNumber(), fclBlCorrectionsForm.getNoticeNo(), false, request);
            forwardName = "addFclBlCorrections";
        }
        correctionsBC.setRequestObject(request, fclBlCorrectionsForm, messageResources);
        if (fclBlCorrectionsForm.isQuickCn()) {
            Integer correctionTypeId = new CodetypeDAO().getCodeTypeId("Bl Correction Type");
            GenericCode correctionType = new GenericCodeDAO().getGenericCodeByCode("A", correctionTypeId);
            request.setAttribute("correctionType", correctionType);
            Integer correctionCodeId = new CodetypeDAO().getCodeTypeId("Bl Correction Code");
            GenericCode correctionCode = new GenericCodeDAO().getGenericCodeByCode("003", correctionCodeId);
            request.setAttribute("correctionCode", correctionCode);
        }
        request.setAttribute("importFlag", importFlag);
        return mapping.findForward(forwardName);
    }

    public void futureCode() {/*
         * // taken from save method List savedList=new
         * ArrayList(); HashMap searchMap=new HashMap();
         * savedList=(List)fclBlCorrectionsBC.getAllCorrections(fclBlCorrectionsForm);
         * for(Iterator
         * iter=savedList.iterator();iter.hasNext();){
         * fclBlCorrections=(FclBlCorrections)iter.next();
         * if(searchMap.get(fclBlCorrections.getBlNumber()+"-"+fclBlCorrections.getNoticeNo())==null){
         * searchMap.put(fclBlCorrections.getBlNumber()+"-"+fclBlCorrections.getNoticeNo(),
         * fclBlCorrections); } } Set
         * searchResultSet=searchMap.keySet(); Iterator
         * iter=searchResultSet.iterator(); List
         * resultList=new ArrayList();
         * while(iter.hasNext()){ fclBlCorrections=new
         * FclBlCorrections();
         * fclBlCorrections=(FclBlCorrections)searchMap.get(iter.next());
         * resultList.add(fclBlCorrections); }
         * request.setAttribute(FclBlConstants.BLCORRECTIONLIST,resultList);
         * //request.setAttribute(FclBlConstants.BLCORRECTIONFORM,fclBlCorrectionsForm);
         * request.setAttribute("BlNumber",
         * fclBlCorrectionsForm.getBlNumber());
         * message="Correction Saved";
         * request.setAttribute("msg", message);
         * forwardName="fclBlCorrections";
         * if(fclBlCorrectionsForm.getTemp()!=null &&
         * fclBlCorrectionsForm.getTemp().equalsIgnoreCase("FclBl")){
         * FclBlCorrectionsForm fclBlCorectForm =new
         * FclBlCorrectionsForm() ;
         * fclBlCorectForm.setTemp("FclBl");
         * fclBlCorectForm.setBlNumber(fclBlCorrectionsForm.getBlNumber());
         * fclBlCorectForm.setFileNo(fclBlCorrectionsForm.getFileNo());
         * request.setAttribute(FclBlConstants.BLCORRECTIONFORM,fclBlCorectForm); }
         */

    }

    public void setRequestForAddPage(FclBlCorrectionsForm fclBlCorrectionsForm, HttpServletRequest request, HttpSession session) throws Exception {
        FclBlCorrectionsBC fclBlCorrectionsBC = new FclBlCorrectionsBC();
        if ((fclBlCorrectionsForm.getTemp() != null && fclBlCorrectionsForm.getTemp().equalsIgnoreCase("FclBl")) || (fclBlCorrectionsForm.getCheckStatus() != null && !fclBlCorrectionsForm.getCheckStatus().equals(""))) {
            FclBlCorrectionsForm fclBlCorectForm = new FclBlCorrectionsForm();
            fclBlCorectForm.setQuickCn(fclBlCorrectionsForm.isQuickCn());
            fclBlCorectForm.setTemp("FclBl");
            String checkStatus = fclBlCorrectionsBC.checkStatus(fclBlCorrectionsForm.getBlNumber(), request);
            if (checkStatus != null) {
                fclBlCorrectionsForm.setCheckStatus(checkStatus);
            }
            request.setAttribute(FclBlConstants.BLCORRECTIONFORM, fclBlCorectForm);
        } else {
            if (null != session.getAttribute(FclBlConstants.BLCORRECTIONFORMFORSESSION)) {
                SessionForm sessionForm = (SessionForm) session.getAttribute(FclBlConstants.BLCORRECTIONFORMFORSESSION);
                PropertyUtils.copyProperties(fclBlCorrectionsForm, sessionForm);
            }
            request.setAttribute(FclBlConstants.BLCORRECTIONLIST, fclBlCorrectionsBC.listToDisplayOnSearchPage(fclBlCorrectionsForm));
        }
    }
}

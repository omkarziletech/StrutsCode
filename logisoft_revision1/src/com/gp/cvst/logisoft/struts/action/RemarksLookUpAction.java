/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cvst.logisoft.struts.action;

import com.gp.cong.common.CommonUtils;
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
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cvst.logisoft.struts.form.RemarksLookUpForm;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/** 
 * MyEclipse Struts
 * Creation date: 07-24-2009
 * 
 * XDoclet definition:
 * @struts.action path="/remarksLookUp" name="remarksLookUpForm" input="/jsps/fclQuotes/RemarksLookUp.jsp" scope="request" validate="true"
 */
public class RemarksLookUpAction extends Action {
    /*
     * Generated Methods
     */

    /**
     * Method execute
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        RemarksLookUpForm remarksLookUpForm = (RemarksLookUpForm) form;// TODO Auto-generated method stub
        HttpSession session = ((HttpServletRequest) request).getSession();
        String buttonValue = remarksLookUpForm.getButtonValue();
        String forwardName = "";
        String remarks = "", codeType = "";
        GenericCodeDAO genericDAO = new GenericCodeDAO();
        GenericCode genericCodeNew = new GenericCode();
        List newList = new ArrayList();
        String[] selectedChecks = remarksLookUpForm.getRcheck();
        String action = request.getParameter("Action");

        String addRemarksValue = remarksLookUpForm.getAddRemarks();
        boolean importFlag = remarksLookUpForm.isImportFlag();

        if (request.getParameter("buttonValue") != null && (request.getParameter("buttonValue").equals("Quotation")
                || request.getParameter("buttonValue").equals("aesDetails")
                || request.getParameter("buttonValue").equals("BlImports")|| request.getParameter("buttonValue").equals("HouseDesc")
                || request.getParameter("buttonValue").equals("MasterDesc"))) {
             if (CommonFunctions.isNotNull(action) && action.equalsIgnoreCase("save")) {
                codeType = "63";
                newList = genericDAO.getLastRecords(codeType);
                String code = null;
                if (CommonFunctions.isNotNullOrNotEmpty(newList)) {
                    Integer genericCodeID = (Integer) newList.get(0);
                    if (genericCodeID != null) {
                        GenericCode genericCode = genericDAO.findById(genericCodeID);
                        if (genericCode.getCode() != null) {
                            String[] values = genericCode.getCode().split("PR0");
                            if (values != null && values.length > 1) {
                                Integer number = new Integer(values[1]);
                                number = number + 1;
                                code = values[0] + "PR0" + number;
                            }else {
                            code = "IPR050";
                         }
                        }
                    }
                }else{
                   code = "IPR050";
                }
                GenericCode genericCode = new GenericCode();
                genericCode.setCodetypeid(63);
                genericCode.setCode(code);
                addRemarksValue=(null!=addRemarksValue)?addRemarksValue.toUpperCase():addRemarksValue;
                genericCode.setCodedesc(addRemarksValue);
                genericDAO.save(genericCode);
            }
            remarks = request.getParameter("remarksCode");
            if (remarks != null && remarks.equals("percent")) {
                remarks = "%";
            }
            List remarksList = new ArrayList();

            if (request.getParameter("buttonValue").equals("aesDetails")) {
                codeType = "56";//----this will populate the aesExceptions list.
            } else {
                if (importFlag) {
                    codeType = "63";
                } else {
                    codeType = "53";
                }
            }
            remarksList = genericDAO.findForChargeCodesForAirRates(remarks, "", codeType);
            session.setAttribute("buttonValue", request.getParameter("buttonValue"));
            session.setAttribute("remarksList", remarksList);
            if (request.getParameter("buttonValue").equals("aesDetails")) {
                forwardName = "aesExceptionsLookUp";//--forwarding to AesExceptionsLookUp jsp.
            } else {
                forwardName = "remarksLookUp";
            }
           
        }
        if (buttonValue != null && buttonValue.equals("Search")) {
            remarks = remarksLookUpForm.getRemarkscode();
            if (null != request.getParameter("requestFrom")) {
                codeType = "56";
            } else {
                codeType = "53";
            }
            if (!remarks.equals("")) {
                newList = genericDAO.findForChargeCodesForAirRates("", remarks, codeType);
            }
            session.setAttribute("remarksList", newList);
            session.removeAttribute("checkSet");
            if (null != request.getParameter("requestFrom")) {
                forwardName = "aesExceptionsLookUp";//--forwarding to AesExceptionsLookUp jsp.
            } else {
                forwardName = "remarksLookUp";
            }
        }
        if (buttonValue != null && buttonValue.equals("Go")) {
            List checkedRemarksList = new ArrayList();
            String id = "";
            if (importFlag) {
                codeType = "63";
            } else {
                codeType = "53";
            }
            if (session.getAttribute("remarksList") != null) {
                newList = (List) session.getAttribute("remarksList");
            }
            Set s = (HashSet)request.getSession().getAttribute("checkSet");
            if(null != s){
                for (Iterator it = s.iterator(); it.hasNext();) {
                    String str = (String)it.next();
                    GenericCode genericCode = (GenericCode) genericDAO.getGenericCodeId(codeType, str);
                    if(null!=genericCode && null!=genericCode.getCodedesc()){
                     remarks = remarks + "" + genericCode.getCodedesc() + ">>";
                    }
                }
            }else{
                 if (null != selectedChecks) {
                    for (int i = 0; i < selectedChecks.length; i++) {
                        id = selectedChecks[i];
                        GenericCode genericCode = (GenericCode) newList.get(Integer.parseInt(id));
                        if(null!=genericCode && null!=genericCode.getCodedesc()){
                        remarks = remarks + "" + genericCode.getCodedesc() + ">>";
                        }
                     }
                }
            }
            remarks = remarks.replace("null", "");
            if(CommonUtils.isNotEmpty(remarks)){
                remarks = remarks.substring(0, (remarks.length() - 2));
            }
            checkedRemarksList.add(remarks);
            request.setAttribute("checkedRemarksList", checkedRemarksList);
            session.removeAttribute("checkSet");
            if (remarksLookUpForm.getButtonParameter() != null
                    && remarksLookUpForm.getButtonParameter().equalsIgnoreCase("Quotation")) {
                request.setAttribute("buttonValue", "QuotationForRemarks");
            } else if (remarksLookUpForm.getButtonParameter() != null
                    && remarksLookUpForm.getButtonParameter().equalsIgnoreCase("aesDetails")) {
                request.setAttribute("buttonValue", "aesDetailsForRemarks");
            }else if (remarksLookUpForm.getButtonParameter() != null
                    && remarksLookUpForm.getButtonParameter().equalsIgnoreCase("BlImports")) {
                request.setAttribute("buttonValue", "BlImportsForRemarks");
            }else if (remarksLookUpForm.getButtonParameter() != null
                    && remarksLookUpForm.getButtonParameter().equalsIgnoreCase("HouseDesc")) {
                request.setAttribute("buttonValue", "HouseDescForRemarks");
            }else if (remarksLookUpForm.getButtonParameter() != null
                    && remarksLookUpForm.getButtonParameter().equalsIgnoreCase("MasterDesc")) {
                request.setAttribute("buttonValue", "MasterDescForRemarks");
            } 
            if (session.getAttribute("remarksList") != null) {
                session.removeAttribute("remarksList");
            }
            if (session.getAttribute("buttonValue") != null) {
                session.removeAttribute("buttonValue");
            }
            forwardName = "remarksLookUp";
        }
        if (buttonValue != null && buttonValue.equals("addRemarks")) {
            request.setAttribute("addNewRemarks", "addNewRemarks");
            forwardName = "remarksLookUp";
        }
        if (buttonValue != null && buttonValue.equals("saveRemarks")) {
            genericCodeNew.setCodetypeid(53);
            genericCodeNew.setCodedesc(remarksLookUpForm.getRemarks());
            genericCodeNew.setCode("");
            genericDAO.save(genericCodeNew);
            codeType = "53";
            newList = genericDAO.findForChargeCodesForAirRates("", "", codeType);
            session.setAttribute("remarksList", newList);
            forwardName = "remarksLookUp";
        }
        request.setAttribute("importFlag", importFlag);
        return mapping.findForward(forwardName);
    }
}
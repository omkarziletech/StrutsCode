/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.ratemangement.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.domain.AirFreightDocumentCharges;
import com.gp.cong.logisoft.domain.RetailFreightDocumentCharges;
import com.gp.cong.logisoft.domain.RetailStandardCharges;
import com.gp.cong.logisoft.struts.ratemangement.form.RetailEditDocChargesForm;

/** 
 * MyEclipse Struts
 * Creation date: 05-23-2008
 * 
 * XDoclet definition:
 * @struts.action path="/retailEditDocCharges" name="retailEditDocChargesForm" input="/jsps/ratemanagement/retailEditDocCharges.jsp" scope="request" validate="true"
 */
public class RetailEditDocChargesAction extends Action {
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
            HttpServletRequest request, HttpServletResponse response) {
        RetailEditDocChargesForm retailEditDocChargesForm = (RetailEditDocChargesForm) form;// TODO Auto-generated method stub
        HttpSession session = ((HttpServletRequest) request).getSession();
        String forwardName = "";
        String buttonValue = retailEditDocChargesForm.getButtonValue();
        RetailFreightDocumentCharges documentCharges = null;

        AirFreightDocumentCharges docChargesObj = null;
        List docChargesList = new ArrayList();
        int index = 0;
        RetailStandardCharges standardChrg = new RetailStandardCharges();

        if (session.getAttribute("retailstandardCharges") == null) {
            standardChrg = new RetailStandardCharges();
        } else {
            standardChrg = (RetailStandardCharges) session.getAttribute("retailstandardCharges");
        }
        if (session.getAttribute("retaildocumentCharges") != null) {
            documentCharges = (RetailFreightDocumentCharges) session.getAttribute("retaildocumentCharges");
        } else {
            documentCharges = new RetailFreightDocumentCharges();
        }
        if (!buttonValue.equals("delete") && !buttonValue.equals("cancel")) {
            if (retailEditDocChargesForm.getMaxDocCharge() != null && !retailEditDocChargesForm.getMaxDocCharge().equals("")) {
                standardChrg.setMaxDocCharge(new Double(retailEditDocChargesForm.getMaxDocCharge()));
            } else {
                standardChrg.setMaxDocCharge(0.0);
            }
            if (retailEditDocChargesForm.getFfCommision() != null && !retailEditDocChargesForm.getFfCommision().equals("")) {
                standardChrg.setFfCommission(new Double(retailEditDocChargesForm.getFfCommision()));
            } else {
                standardChrg.setFfCommission(0.0);
            }
            if (retailEditDocChargesForm.getBlBottomLine() != null && !retailEditDocChargesForm.getBlBottomLine().equals("")) {
                standardChrg.setBlBottomLine(new Double(retailEditDocChargesForm.getBlBottomLine()));
            } else {
                standardChrg.setBlBottomLine(0.0);
            }
            if (retailEditDocChargesForm.getCocbm() != null && !retailEditDocChargesForm.getCocbm().equals("")) {
                standardChrg.setCostCbm(new Double(retailEditDocChargesForm.getCocbm()));
            } else {
                standardChrg.setCostCbm(0.0);
            }
            session.setAttribute("retailstandardCharges", standardChrg);

            if (retailEditDocChargesForm.getAmount() != null && !retailEditDocChargesForm.getAmount().equals("")) {
                documentCharges.setAmount(new Double(retailEditDocChargesForm.getAmount()));
            } else {
                documentCharges.setAmount(0.0);
            }
        }
        forwardName = "docChargesEdit";
        if (buttonValue.equals("add")) {
            if (session.getAttribute("retaildocChargesAdd") != null) {
                docChargesList = (List) session.getAttribute("retaildocChargesAdd");
                for (int i = 0; i < docChargesList.size(); i++) {

                    RetailFreightDocumentCharges retailStandard = (RetailFreightDocumentCharges) docChargesList.get(i);
                    if (retailStandard.getIndex() == documentCharges.getIndex()) {
                        documentCharges.setIndex(retailStandard.getIndex());
                        docChargesList.remove(documentCharges);
                    }
                }
            }

            docChargesList.add(documentCharges);
            session.setAttribute("retaildocChargesAdd", docChargesList);
            if (session.getAttribute("retaildocumentCharges") != null) {
                session.removeAttribute("retaildocumentCharges");
            }
            forwardName = "docChargesAdd";
        } else if (buttonValue.equals("cancel")) {
            if (session.getAttribute("retaildocumentCharges") != null) {
                session.removeAttribute("retaildocumentCharges");
            }
            forwardName = "docChargesAdd";
        } else if (buttonValue.equals("delete")) {
            if (session.getAttribute("retaildocChargesAdd") != null) {
                docChargesList = (List) session.getAttribute("retaildocChargesAdd");

            }
            for (int i = 0; i < docChargesList.size(); i++) {
                RetailFreightDocumentCharges retailStandard = (RetailFreightDocumentCharges) docChargesList.get(i);


                if (retailStandard.getIndex() == documentCharges.getIndex()) {
                    docChargesList.remove(retailStandard);
                }
            }
            if (session.getAttribute("retaildocumentCharges") != null) {
                session.removeAttribute("retaildocumentCharges");
            }
            session.setAttribute("retaildocChargesAdd", docChargesList);
            forwardName = "docChargesAdd";
        }
        request.setAttribute("buttonValue", buttonValue);
        return mapping.findForward(forwardName);

    }
}
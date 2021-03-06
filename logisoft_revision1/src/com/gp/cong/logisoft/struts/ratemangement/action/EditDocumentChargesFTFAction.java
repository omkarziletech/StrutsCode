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

import com.gp.cong.logisoft.domain.FTFDocumentCharges;
import com.gp.cong.logisoft.domain.FTFMaster;
import com.gp.cong.logisoft.struts.ratemangement.form.EditDocumentChargesFTFForm;

/** 
 * MyEclipse Struts
 * Creation date: 06-25-2008
 * 
 * XDoclet definition:
 * @struts.action path="/editDocumentChargesFTF" name="editDocumentChargesFTFForm" input="/jsps/ratemanagement/editDocumentChargesFTF.jsp" scope="request" validate="true"
 */
public class EditDocumentChargesFTFAction extends Action {
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
        EditDocumentChargesFTFForm editDocumentChargesFTFForm = (EditDocumentChargesFTFForm) form;// TODO Auto-generated method stub
        HttpSession session = ((HttpServletRequest) request).getSession();
        String forwardName = "";
        String buttonValue = editDocumentChargesFTFForm.getButtonValue();
        FTFDocumentCharges documentCharges = null;

        FTFDocumentCharges docChargesObj = null;
        List docChargesList = new ArrayList();
        int index = 0;
        FTFMaster ftfMaster = new FTFMaster();

        if (session.getAttribute("addftfMaster") == null) {
            ftfMaster = new FTFMaster();
        } else {
            ftfMaster = (FTFMaster) session.getAttribute("addftfMaster");
        }
        if (editDocumentChargesFTFForm.getMaxDocCharge() != null && !editDocumentChargesFTFForm.getMaxDocCharge().equals("")) {
            ftfMaster.setMaxDocCharge(new Double(editDocumentChargesFTFForm.getMaxDocCharge()));
        } else {
            ftfMaster.setMaxDocCharge(0.0);
        }
        if (editDocumentChargesFTFForm.getFfCommision() != null && !editDocumentChargesFTFForm.getFfCommision().equals("")) {
            ftfMaster.setFfCommission(new Double(editDocumentChargesFTFForm.getFfCommision()));
        } else {
            ftfMaster.setFfCommission(0.0);
        }
        if (editDocumentChargesFTFForm.getBlBottomLine() != null && !editDocumentChargesFTFForm.getBlBottomLine().equals("")) {
            ftfMaster.setBlBottomLine(new Double(editDocumentChargesFTFForm.getBlBottomLine()));
        } else {
            ftfMaster.setBlBottomLine(0.0);
        }

        session.setAttribute("addftfMaster", ftfMaster);
        if (session.getAttribute("ftfdocumentCharges") != null) {
            documentCharges = (FTFDocumentCharges) session.getAttribute("ftfdocumentCharges");
        } else {
            documentCharges = new FTFDocumentCharges();
        }
        if (editDocumentChargesFTFForm.getAmount() != null && !editDocumentChargesFTFForm.getAmount().equals("")) {
            documentCharges.setAmount(new Double(editDocumentChargesFTFForm.getAmount()));
        }

        forwardName = "ftfdocChargesEdit";
        if (buttonValue.equals("add")) {
            if (session.getAttribute("ftfdocChargesAdd") != null) {
                docChargesList = (List) session.getAttribute("ftfdocChargesAdd");
                for (int i = 0; i < docChargesList.size(); i++) {

                    FTFDocumentCharges coMaster = (FTFDocumentCharges) docChargesList.get(i);
                    if (coMaster.getIndex() == documentCharges.getIndex()) {
                        documentCharges.setIndex(coMaster.getIndex());
                        docChargesList.remove(documentCharges);
                    }
                }
            }

            docChargesList.add(documentCharges);
            session.setAttribute("ftfdocChargesAdd", docChargesList);
            if (session.getAttribute("ftfdocumentCharges") != null) {
                session.removeAttribute("ftfdocumentCharges");
            }
            forwardName = "ftfdocChargesAdd";
        } else if (buttonValue.equals("cancel")) {
            if (session.getAttribute("ftfdocumentCharges") != null) {
                session.removeAttribute("ftfdocumentCharges");
            }
            forwardName = "ftfdocChargesAdd";
        } else if (buttonValue.equals("delete")) {
            if (session.getAttribute("ftfdocChargesAdd") != null) {
                docChargesList = (List) session.getAttribute("ftfdocChargesAdd");
            }
            for (int i = 0; i < docChargesList.size(); i++) {
                FTFDocumentCharges coMaster = (FTFDocumentCharges) docChargesList.get(i);
                if (coMaster.getIndex() == documentCharges.getIndex()) {
                    docChargesList.remove(coMaster);
                }
                session.setAttribute("ftfdocChargesAdd", docChargesList);
            }
            if (session.getAttribute("ftfdocumentCharges") != null) {
                session.removeAttribute("ftfdocumentCharges");
            }
            forwardName = "ftfdocChargesAdd";
        }
        request.setAttribute("buttonValue", buttonValue);
        return mapping.findForward(forwardName);

    }
}
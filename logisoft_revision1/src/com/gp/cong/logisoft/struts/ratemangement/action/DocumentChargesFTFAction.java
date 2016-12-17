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
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.struts.ratemangement.form.DocumentChargesFTFForm;

/** 
 * MyEclipse Struts
 * Creation date: 06-25-2008
 * 
 * XDoclet definition:
 * @struts.action path="/documentChargesFTF" name="documentChargesFTFForm" input="/jsps/ratemanagement/documentChargesFTF.jsp" scope="request" validate="true"
 */
public class DocumentChargesFTFAction extends Action {
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
        DocumentChargesFTFForm documentChargesFTFForm = (DocumentChargesFTFForm) form;// TODO Auto-generated method stub
        HttpSession session = ((HttpServletRequest) request).getSession();
        String forwardName = "";
        String code = documentChargesFTFForm.getCharge();
        String codeDesc = documentChargesFTFForm.getDesc();
        String buttonValue = documentChargesFTFForm.getButtonValue();

        FTFDocumentCharges documentCharges = null;
        GenericCode genericCodeObj = new GenericCode();
        FTFDocumentCharges docChargesObj = null;
        List docChargesList = new ArrayList();
        int index = 0;


        if (request.getParameter("index") != null && !request.getParameter("index").equals("")) {

            int ind = Integer.parseInt(request.getParameter("index"));

            List codeList1 = (List) session.getAttribute("ftfdocChargesAdd");
            docChargesObj = (FTFDocumentCharges) codeList1.get(ind);
            session.setAttribute("ftfdocumentCharges", docChargesObj);
            forwardName = "ftfdocChargesEdit";
        } else {
            FTFMaster ftfMaster = new FTFMaster();
            if (session.getAttribute("addftfMaster") == null) {
                ftfMaster = new FTFMaster();
            } else {
                ftfMaster = (FTFMaster) session.getAttribute("addftfMaster");
            }
            if (documentChargesFTFForm.getMaxDocCharge() != null && !documentChargesFTFForm.getMaxDocCharge().equals("")) {
                ftfMaster.setMaxDocCharge(new Double(documentChargesFTFForm.getMaxDocCharge()));
            } else {
                ftfMaster.setMaxDocCharge(0.0);
            }
            if (documentChargesFTFForm.getFfCommision() != null && !documentChargesFTFForm.getFfCommision().equals("")) {
                ftfMaster.setFfCommission(new Double(documentChargesFTFForm.getFfCommision()));
            } else {
                ftfMaster.setFfCommission(0.0);
            }
            if (documentChargesFTFForm.getBlBottomLine() != null && !documentChargesFTFForm.getBlBottomLine().equals("")) {
                ftfMaster.setBlBottomLine(new Double(documentChargesFTFForm.getBlBottomLine()));
            } else {
                ftfMaster.setBlBottomLine(0.0);
            }
            session.setAttribute("addftfMaster", ftfMaster);

            if (session.getAttribute("ftfdocumentCharges") != null) {
                documentCharges = (FTFDocumentCharges) session.getAttribute("ftfdocumentCharges");
            } else {
                documentCharges = new FTFDocumentCharges();
            }
            if (documentChargesFTFForm.getAmount() != null && !documentChargesFTFForm.getAmount().equals("")) {
                documentCharges.setAmount(new Double(documentChargesFTFForm.getAmount()));
            }

            forwardName = "ftfdocChargesAdd";
            if (buttonValue.equals("add")) {
                if (documentCharges.getChargeCode() != null) {
                    if (session.getAttribute("ftfdocChargesAdd") != null) {
                        docChargesList = (List) session.getAttribute("ftfdocChargesAdd");
                        for (int i = 0; i < docChargesList.size(); i++) {
                            FTFDocumentCharges docCharges = (FTFDocumentCharges) docChargesList.get(i);
                            if (docCharges.getIndex() != null) {
                                if (docCharges.getIndex() > index) {
                                    index = docCharges.getIndex();
                                }
                            } else {
                                index = docChargesList.size() - 1;
                            }
                        }
                        index++;
                    } else {
                        index++;
                        docChargesList = new ArrayList();

                    }
                    docChargesList.add(documentCharges);
                    if (session.getAttribute("ftfdocumentCharges") != null) {
                        session.removeAttribute("ftfdocumentCharges");
                    }
                    session.setAttribute("ftfdocChargesAdd", docChargesList);
                } else {
                    String msg = "Please Select correct Charge Code... ";
                    request.setAttribute("exist", msg);
                }
                forwardName = "ftfdocChargesAdd";
            }
            if (buttonValue != null && buttonValue.equals("chargeCode")) {
                List list = new ArrayList();
                String msg = "Charge Code already exists... ";
                if (session.getAttribute("ftfdocChargesAdd") != null) {
                    list = (List) session.getAttribute("ftfdocChargesAdd");

                    for (int i = 0; i < list.size(); i++) {
                        documentCharges = (FTFDocumentCharges) list.get(i);
                        if (documentCharges.getChargeCode().getCode().equals(code)) {
                            request.setAttribute("exist", msg);
                        }
                        if (documentCharges.getChargeCode().getCodedesc().equals(codeDesc)) {
                            request.setAttribute("exist", msg);
                        }
                    }
                }

                if (request.getAttribute("exist") == null && (code != null || !code.equals(""))) {
                    FTFDocumentCharges documentCharges2 = new FTFDocumentCharges();
                    GenericCodeDAO genericDAO = new GenericCodeDAO();
                    List codeList = genericDAO.findGenericCode("35", code);
                    genericCodeObj = null;
                    if (codeList.size() > 0) {
                        genericCodeObj = (GenericCode) codeList.get(0);
                    }
                    if (genericCodeObj != null) {
                        documentCharges2.setChargeCode(genericCodeObj);
                        session.setAttribute("ftfdocumentCharges", documentCharges2);
                    }
                }

                if (request.getAttribute("exist") == null && (codeDesc != null || !codeDesc.equals(""))) {
                    FTFDocumentCharges documentCharges2 = new FTFDocumentCharges();
                    GenericCodeDAO genericDAO = new GenericCodeDAO();
                    List codeList = genericDAO.findByCodedesc(codeDesc);
                    genericCodeObj = null;
                    if (codeList.size() > 0) {
                        genericCodeObj = (GenericCode) codeList.get(0);
                    }
                    if (genericCodeObj != null) {
                        documentCharges2.setChargeCode(genericCodeObj);
                        session.setAttribute("ftfdocumentCharges", documentCharges2);
                    }
                }



            }
        }
        request.setAttribute("buttonValue", buttonValue);
        return mapping.findForward(forwardName);

    }
}
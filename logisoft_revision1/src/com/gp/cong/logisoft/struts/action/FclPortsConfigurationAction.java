  /*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.action;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.beans.PortsBean;
import com.gp.cong.logisoft.domain.FCLPortConfiguration;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.struts.form.FclPortsConfigurationForm;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;

/** 
 * MyEclipse Struts
 * Creation date: 01-03-2008
 * 
 * XDoclet definition:
 * @struts.action path="/fclPortsConfiguration" name="fclPortsConfigurationForm" input="/jsps/datareference/fclPortsConfiguration.jsp" scope="request" validate="true"
 */
public class FclPortsConfigurationAction extends Action {
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
            HttpServletRequest request, HttpServletResponse response)throws Exception {
        FclPortsConfigurationForm fclPortsConfigurationForm = (FclPortsConfigurationForm) form;// TODO Auto-generated method stub
        HttpSession session = ((HttpServletRequest) request).getSession();
        String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
        String temporaryText = fclPortsConfigurationForm.getTemporaryText();
        String temporaryDate = fclPortsConfigurationForm.getTxtCal();
        String expirationDate = fclPortsConfigurationForm.getTxtCal1();
        String originRemarks = fclPortsConfigurationForm.getOriginRemarks();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        GenericCode genObj = null;
        GenericCode genObjRuleRouteByAgentAdmin = null;
        GenericCode genObjRuleRouteByAgentCommn = null;
        GenericCode genObjRuleRouteNotAgentAdmin = null;
        GenericCode genObjRuleRouteNotAgentCommn = null;
        //source changed
        String srvcFcl = fclPortsConfigurationForm.getSrvcFcl();
        if (srvcFcl == null) {
            srvcFcl = "N";

        } else {
            srvcFcl = "Y";
        }
        //source changed
        String cubeWtMandatoryFcl = fclPortsConfigurationForm.getCubeWtMandatoryFcl();
        if (cubeWtMandatoryFcl == null) {
            cubeWtMandatoryFcl = "N";
        } else {
            cubeWtMandatoryFcl = "Y";

        }
        String fclSsBlGoCollect = fclPortsConfigurationForm.getFclSsBlGoCollect();
        if (fclSsBlGoCollect == null) {
            fclSsBlGoCollect = "N";
        } else {
            fclSsBlGoCollect = "Y";
        }
        String fclHouseBlGoCollect = fclPortsConfigurationForm.getFclHouseBlGoCollect();
        if (fclHouseBlGoCollect == null) {
            fclHouseBlGoCollect = "N";
        } else {
            fclHouseBlGoCollect = "Y";
        }
        String insuranceAllowed = fclPortsConfigurationForm.getInsuranceAllowed();
        if (insuranceAllowed == null) {
            insuranceAllowed = "N";
        } else {
            insuranceAllowed = "Y";
        }
        String specialRemarks = fclPortsConfigurationForm.getSpecialRemarks();
        if (specialRemarks == null) {
            specialRemarks = "N";

        } else {
            specialRemarks = "Y";
        }
        String brandField=fclPortsConfigurationForm.getBrandField();
        if(brandField == null){
           brandField = "Ecu Worldwide";
        } else if(brandField != null && ("03").equals(companyCode)){
           brandField = "Econocaribe";
        } else if(brandField != null && ("02").equals(companyCode)){
            brandField = "OTI";
        }
        PortsBean portBean = new PortsBean();
        portBean.setServiceFcl(srvcFcl);
        portBean.setCubewt(cubeWtMandatoryFcl);
        portBean.setFclssbl(fclSsBlGoCollect);
        portBean.setFclhousebl(fclHouseBlGoCollect);
        portBean.setInsuranceAllowed(insuranceAllowed);
        portBean.setSpclRemark(specialRemarks);
        portBean.setQuoteClause(fclPortsConfigurationForm.getQuoteClause());
        FCLPortConfiguration fclPortobj = new FCLPortConfiguration();
        if (session.getAttribute("fclPortobj") != null) {
            fclPortobj = (FCLPortConfiguration) session.getAttribute("fclPortobj");
        } else {
            fclPortobj = new FCLPortConfiguration();
        }
        fclPortobj.setSrvcFcl(srvcFcl);
        fclPortobj.setCubeWtMandatoryFcl(cubeWtMandatoryFcl);
        fclPortobj.setFclSsBlGoCollect(fclSsBlGoCollect);
        fclPortobj.setFclHouseBlGoCollect(fclHouseBlGoCollect);
        fclPortobj.setInsuranceAllowed(insuranceAllowed);
        fclPortobj.setQuoteClause(fclPortsConfigurationForm.getQuoteClause());
        fclPortobj.setTemporaryText(temporaryText);
        fclPortobj.setOriginRemarks(originRemarks);
        fclPortobj.setBrandField(brandField);
        if (temporaryDate != null && temporaryDate != "") {
            java.util.Date javaDate = null;
                javaDate = sdf.parse(temporaryDate);
            fclPortobj.setTemporaryDate(javaDate);
        }
        if (expirationDate != null && expirationDate != "") {
            java.util.Date expJavaDate = null;
                expJavaDate = sdf.parse(expirationDate);
            fclPortobj.setExpirationDate(expJavaDate);
        }

        if (fclPortsConfigurationForm.getBlClauses() != null && !fclPortsConfigurationForm.getBlClauses().equals("0")) {
            GenericCodeDAO genericDAO = new GenericCodeDAO();
            genObj = genericDAO.findById(new Integer(fclPortsConfigurationForm.getBlClauses()));
            fclPortobj.setBlClauseId(genObj);
        //fclPortobj.setBlClauseDesc(genObj.getCodedesc());

        }
        if (fclPortsConfigurationForm.getAmountRouteByAgentAdmin() != null && !fclPortsConfigurationForm.getAmountRouteByAgentAdmin().equals("")) {
            fclPortobj.setRadmAm(new Double(fclPortsConfigurationForm.getAmountRouteByAgentAdmin()));
        }
        if (fclPortsConfigurationForm.getTierAmountRouteByAgentAdmin() != null && !fclPortsConfigurationForm.getTierAmountRouteByAgentAdmin().equals("")) {
            fclPortobj.setRadmTierAmt(new Double(fclPortsConfigurationForm.getTierAmountRouteByAgentAdmin()));
        }
        if (fclPortsConfigurationForm.getRuleRouteByAgentAdmin() != null && !fclPortsConfigurationForm.getRuleRouteByAgentAdmin().equals("")) {
            GenericCodeDAO genericDAO = new GenericCodeDAO();
            genObjRuleRouteByAgentAdmin = genericDAO.findById(new Integer(fclPortsConfigurationForm.getRuleRouteByAgentAdmin()));
            fclPortobj.setRadmRule(genObjRuleRouteByAgentAdmin);
        }
        if (fclPortsConfigurationForm.getAmountRouteByAgentCommn() != null && !fclPortsConfigurationForm.getAmountRouteByAgentCommn().equals("")) {
            fclPortobj.setRcomAm(new Double(fclPortsConfigurationForm.getAmountRouteByAgentCommn()));
        }
        if (fclPortsConfigurationForm.getTierAmountRouteByAgentCommn() != null && !fclPortsConfigurationForm.getTierAmountRouteByAgentCommn().equals("")) {
            fclPortobj.setRcomTierAmt(new Double(fclPortsConfigurationForm.getTierAmountRouteByAgentCommn()));
        }
        if (fclPortsConfigurationForm.getRuleRouteByAgentCommn() != null && !fclPortsConfigurationForm.getRuleRouteByAgentCommn().equals("")) {
            GenericCodeDAO genericDAO = new GenericCodeDAO();
            genObjRuleRouteByAgentCommn = genericDAO.findById(new Integer(fclPortsConfigurationForm.getRuleRouteByAgentCommn()));
            fclPortobj.setRcomRule(genObjRuleRouteByAgentCommn);
        }
        if (fclPortsConfigurationForm.getAmountRouteNotAgentAdmin() != null && !fclPortsConfigurationForm.getAmountRouteNotAgentAdmin().equals("")) {
            fclPortobj.setNadmAm(new Double(fclPortsConfigurationForm.getAmountRouteNotAgentAdmin()));
        }
        // changed value
        if (fclPortsConfigurationForm.getTierAmountRouteNotAgentAdmin() != null && !fclPortsConfigurationForm.getTierAmountRouteNotAgentAdmin().equals("")) {
            fclPortobj.setNadmTierAmt(new Double(fclPortsConfigurationForm.getTierAmountRouteNotAgentAdmin()));
        }
        // changed value
        if (fclPortsConfigurationForm.getRuleRouteNotAgentAdmin() != null && !fclPortsConfigurationForm.getRuleRouteNotAgentAdmin().equals("")) {
            GenericCodeDAO genericDAO = new GenericCodeDAO();
            genObjRuleRouteNotAgentAdmin = genericDAO.findById(new Integer(fclPortsConfigurationForm.getRuleRouteNotAgentAdmin()));
            fclPortobj.setNadmRule(genObjRuleRouteNotAgentAdmin);
        }//changed value
        if (fclPortsConfigurationForm.getAmountRouteNotAgentCommn() != null && !fclPortsConfigurationForm.getAmountRouteNotAgentCommn().equals("")) {
            fclPortobj.setNcomAm(new Double(fclPortsConfigurationForm.getAmountRouteNotAgentCommn()));
        }
        if (fclPortsConfigurationForm.getTierAmountRouteNotAgentCommn() != null && !fclPortsConfigurationForm.getTierAmountRouteNotAgentCommn().equals("")) {
            fclPortobj.setNcomTierAmt(new Double(fclPortsConfigurationForm.getTierAmountRouteNotAgentCommn()));
        }
        if (fclPortsConfigurationForm.getRuleRouteNotAgentCommn() != null && !fclPortsConfigurationForm.getRuleRouteNotAgentCommn().equals("")) {
            GenericCodeDAO genericDAO = new GenericCodeDAO();
            genObjRuleRouteNotAgentCommn = genericDAO.findById(new Integer(fclPortsConfigurationForm.getRuleRouteNotAgentCommn()));
            fclPortobj.setNcomRule(genObjRuleRouteNotAgentCommn);
        }
        if (fclPortsConfigurationForm.getAmountCurrentAdjFactor() != null && !fclPortsConfigurationForm.getAmountCurrentAdjFactor().equals("")) {

            fclPortobj.setCurrentAdjFactor(new Double(fclPortsConfigurationForm.getAmountCurrentAdjFactor()));
        }

        fclPortobj.setSpecialRemarks(specialRemarks);
        fclPortobj.setSpecialRemarksForQuot(fclPortsConfigurationForm.getSpecialRemarksforQuotation());
        fclPortobj.setTranshipment(fclPortsConfigurationForm.getTranshipment());
        fclPortobj.setLineManager(fclPortsConfigurationForm.getLineManager());
        fclPortobj.setDefaultPortOfDischarge(fclPortsConfigurationForm.getDefaultPortOfDischarge());
        request.setAttribute("portBean", portBean);
        session.setAttribute("fclPortobj", fclPortobj);

        return mapping.findForward("fclConfig");
    }
}
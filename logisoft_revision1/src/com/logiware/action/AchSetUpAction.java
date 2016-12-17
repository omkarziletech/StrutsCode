package com.logiware.action;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cvst.logisoft.domain.BankDetails;
import com.gp.cvst.logisoft.hibernate.dao.BankDetailsDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.logiware.form.AchSetUpForm;
import com.logiware.hibernate.domain.AchSetUp;
import com.logiware.utils.AchSetUpUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Lakshminarayanan
 */
public class AchSetUpAction extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";

    /**
     * This is the action called from the Struts framework.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        AchSetUpForm achSetUpForm = (AchSetUpForm) form;
        String buttonAction = achSetUpForm.getButtonAction();
        BankDetailsDAO bankDetailsDAO = new BankDetailsDAO();
        SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
        if (CommonUtils.isEqual(buttonAction, "showAchSetUp") && CommonUtils.isNotEmpty(achSetUpForm.getBankId())) {
            BankDetails bankDetails = bankDetailsDAO.findById(Integer.parseInt(achSetUpForm.getBankId()));
            if (null != bankDetails) {
                AchSetUp achSetUp = bankDetails.getAchSetUp();
                if (null != achSetUp) {
                    PropertyUtils.copyProperties(achSetUpForm, achSetUp);
                    if (null != achSetUp.getPublicKey() && achSetUp.getPublicKey().length > 0) {
                        achSetUpForm.setHavePublicKey(true);
                    } else {
                        achSetUpForm.setHavePublicKey(false);
                    }
                    if (null != achSetUp.getSshPrivateKey() && achSetUp.getSshPrivateKey().length > 0) {
                        achSetUpForm.setHasSshPrivateKey(true);
                    } else {
                        achSetUpForm.setHasSshPrivateKey(false);
                    }
                } else {
                    achSetUpForm = new AchSetUpForm();
                    achSetUpForm.setImmediateOrigin(systemRulesDAO.getSystemRulesByCode(CommonConstants.SYSTEM_RULE_CODE_FEDERAL_ID));
                    achSetUpForm.setCompanyName(systemRulesDAO.getSystemRulesByCode(CommonConstants.SYSTEM_RULE_CODE_COMPANY_NAME));
                    achSetUpForm.setCompanyIdentification(systemRulesDAO.getSystemRulesByCode(CommonConstants.SYSTEM_RULE_CODE_FEDERAL_ID));
                }
                achSetUpForm.setBankId("" + bankDetails.getId());
                achSetUpForm.setImmediateDestination(bankDetails.getBankRoutingNumber());
                achSetUpForm.setImmediateDestinationName(bankDetails.getBankName());
            }
            request.setAttribute("achSetUpForm", achSetUpForm);
        } else if (CommonUtils.isEqual(buttonAction, "Save")) {
            if (CommonUtils.isNotEmpty(achSetUpForm.getBankId())) {
                BankDetails bankDetails = bankDetailsDAO.findById(Integer.parseInt(achSetUpForm.getBankId()));
                AchSetUp achSetUp;
                if (null != bankDetails.getAchSetUp() && CommonUtils.isNotEmpty(bankDetails.getAchSetUp().getAchId())) {
                    achSetUp = AchSetUpUtils.copyProperties(achSetUpForm, bankDetails.getAchSetUp());
                } else {
                    achSetUp = AchSetUpUtils.copyProperties(achSetUpForm, new AchSetUp());
                }
                bankDetails.setAchSetUp(achSetUp);
                request.setAttribute("message", "setupCompleted");
            }
        }
        return mapping.findForward(SUCCESS);
    }
}

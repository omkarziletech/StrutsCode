package com.logiware.action;

import com.gp.cvst.logisoft.domain.SystemRules;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.logiware.form.SystemRulesForm;
import java.util.List;

/**
 *
 * @author N K Bala
 */
public class SystemRulesAction extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    private static final String UPDATE = "update";
    private static final String SYSTEM_RULES_SUB_LIST1="systemRulesSublist1";
    private static final String SYSTEM_RULES_SUB_LIST2="systemRulesSubList2";

    /**
     * This is the action called from the Struts framework.
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
            throws Exception,SQLException {
        SystemRulesForm systemRulesForm = (SystemRulesForm) form;
        SystemRulesDAO systemRulesDAO = new SystemRulesDAO();

        if(UPDATE.equals(systemRulesForm.getButtonValue())){
            for (int i = 0; i < systemRulesForm.getRuleId().length; i++) {
                SystemRules systemRules = new SystemRules();
                String id = systemRulesForm.getRuleId()[i];
                String roleName = systemRulesForm.getRuleName()[i];
                systemRules = new SystemRulesDAO().findById(Integer.parseInt(id));
                systemRules.setRuleName(roleName);
            }
            getSystemRules(request);
        }else{
            getSystemRules(request);
        }
        return mapping.findForward(SUCCESS);
    }
    public void getSystemRules(HttpServletRequest request) throws Exception {
        List<SystemRules> systemRuleList =new SystemRulesDAO().getAllRuleRecords();
        int listSize = systemRuleList.size();
        List<SystemRules> systemRulesList1 = systemRuleList.subList(0, listSize%2==0?listSize/2:listSize/2+1);
        List<SystemRules> systemRulesList2 = systemRuleList.subList(listSize%2==0?listSize/2:listSize/2+1,listSize);
        request.setAttribute(SYSTEM_RULES_SUB_LIST1, systemRulesList1);
        request.setAttribute(SYSTEM_RULES_SUB_LIST2, systemRulesList2);
    }
}

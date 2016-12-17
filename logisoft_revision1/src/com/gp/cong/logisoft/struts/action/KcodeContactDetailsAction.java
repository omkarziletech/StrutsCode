/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gp.cong.logisoft.struts.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.bc.tradingpartner.ContactConfigurationBC;
import com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerConstants;
import com.gp.cong.logisoft.domain.CustomerContact;
import com.gp.cong.logisoft.hibernate.dao.CustomerContactDAO;
import com.gp.cong.logisoft.struts.form.TradingPartnerForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 *
 * @author PALRAJ
 */
public class KcodeContactDetailsAction extends DispatchAction {

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TradingPartnerForm tradingPartnerForm = (TradingPartnerForm) form;
        ContactConfigurationBC contactConfigurationBC = new ContactConfigurationBC();
        tradingPartnerForm = contactConfigurationBC.findCustomerRecordForSelect(tradingPartnerForm);
        request.setAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM, tradingPartnerForm);
        request.setAttribute("custContactId", tradingPartnerForm.getIndex());
        return mapping.findForward("kcodeContact");
    }

    public ActionForward saveKcodeContact(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TradingPartnerForm tradingPartnerForm = (TradingPartnerForm) form;
        CustomerContact customerContact = new ContactConfigurationBC().findEachCustomerRecord(tradingPartnerForm);
        customerContact.setLclExports(tradingPartnerForm.isLclExports());
        customerContact.setLclImports(tradingPartnerForm.isLclImports());
        customerContact.setFclExports(tradingPartnerForm.isFclExports());
        customerContact.setFclImports(tradingPartnerForm.isFclImports());
        customerContact.setApplicableToAllShipmentsCodeK(tradingPartnerForm.isApplicableToAllShipmentsCodeK());
        customerContact.setOnlyWhenBookingContactCodeK(tradingPartnerForm.isOnlyWhenBookingContactCodeK());
        new CustomerContactDAO().saveOrUpdate(customerContact);
        return null;
    }
}


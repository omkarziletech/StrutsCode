/*
 * To change this template, choose Tools | Templates
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
public class JcodeContactDetailsAction extends DispatchAction {

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TradingPartnerForm tradingPartnerForm = (TradingPartnerForm) form;
        ContactConfigurationBC contactConfigurationBC = new ContactConfigurationBC();
        tradingPartnerForm = contactConfigurationBC.findCustomerRecordForSelect(tradingPartnerForm);
        request.setAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM, tradingPartnerForm);
        request.setAttribute("custContactId", tradingPartnerForm.getIndex());
        return mapping.findForward("jcodeContact");
    }

    public ActionForward saveJcodeContact(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TradingPartnerForm tradingPartnerForm = (TradingPartnerForm) form;
        CustomerContact customerContact = new ContactConfigurationBC().findEachCustomerRecord(tradingPartnerForm);
        customerContact.setApplicableToAllShipments(tradingPartnerForm.isApplicableToAllShipments());
        customerContact.setOnlyWhenBookingContact(tradingPartnerForm.isOnlyWhenBookingContact());
        // Non Neg Rated
        customerContact.setNonNegRatedPosting(tradingPartnerForm.isNonNegRatedPosting());
        customerContact.setNonNegRatedManifest(tradingPartnerForm.isNonNegRatedManifest());
        customerContact.setNonNegRatedCob(tradingPartnerForm.isNonNegRatedCob());
        customerContact.setNonNegRatedChanges(tradingPartnerForm.isNonNegRatedChanges());
        // Non Neg Unrated
        customerContact.setNonNegUnratedManifest(tradingPartnerForm.isNonNegUnratedManifest());
        customerContact.setNonNegUnratedCob(tradingPartnerForm.isNonNegUnratedCob());
        customerContact.setNonNegUnratedChanges(tradingPartnerForm.isNonNegUnratedChanges());
        customerContact.setNonNegUnratedPosting(tradingPartnerForm.isNonNegUnratedPosting());
        // Freight Invoice
        customerContact.setFreightInvoiceManifest(tradingPartnerForm.isFreightInvoiceManifest());
        customerContact.setFreightInvoiceCob(tradingPartnerForm.isFreightInvoiceCob());
        // Confirm On Board
        customerContact.setConfirmOnBoardCob(tradingPartnerForm.isConfirmOnBoardCob());
        // Dr From Codes 
        customerContact.setObkgToAny(tradingPartnerForm.isObkgToAny());
        customerContact.setRunvToRcvd(tradingPartnerForm.isRunvToRcvd());
        customerContact.setAny(tradingPartnerForm.isAny());
        new CustomerContactDAO().saveOrUpdate(customerContact);
        return null;
    }
}

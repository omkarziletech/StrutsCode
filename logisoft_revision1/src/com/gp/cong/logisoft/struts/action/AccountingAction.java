/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.action;

import com.gp.cong.logisoft.hibernate.dao.CustomerDAO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC;
import com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerConstants;
import com.gp.cong.logisoft.domain.Customer;
import com.gp.cong.logisoft.domain.CustomerAddress;
import com.gp.cong.logisoft.domain.CustomerContact;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.struts.form.AccountingForm;

/** 
 * MyEclipse Struts
 * Creation date: 12-26-2007
 * 
 * XDoclet definition:
 * @struts.action path="/accounting" name="accountingForm" input="/jsps/Tradingpartnermaintainance/Accounting.jsp" scope="request" validate="true"
 */
public class AccountingAction extends Action {
    /*
     * Generated Methods

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
        AccountingForm accountingForm = (AccountingForm) form;// TODO Auto-generated method stub
        HttpSession session = ((HttpServletRequest) request).getSession();
        String buttonValue = accountingForm.getButtonValue();
        //TODO - get from session
        TradingPartnerBC tradingBC = new TradingPartnerBC();
        TradingPartner tradingPartner = tradingBC.findTradingPartnerById("ABCCOU0001");



        if (null != buttonValue) {
            if (buttonValue.equals(TradingPartnerConstants.LOAD_CONTACT_DETAILS)) {
                Set<CustomerContact> customerContactList = (Set<CustomerContact>) tradingPartner.getCustomerContact();
                request.setAttribute(TradingPartnerConstants.CUSTOMER_CONTACT_DETAILS, customerContactList);
                request.setAttribute(TradingPartnerConstants.LOAD_CONTACT_DETAILS, true);

            } else if (buttonValue.equals(TradingPartnerConstants.LOAD_INVOICE_ADDRESS_DETAILS)) {
                Set<CustomerAddress> customerAddressList = (Set<CustomerAddress>) tradingPartner.getCustomerAddressSet();
                request.setAttribute(TradingPartnerConstants.CUSTOMER_ADDRESS_DETAILS, customerAddressList);
                request.setAttribute(TradingPartnerConstants.LOAD_INVOICE_ADDRESS_DETAILS, true);

            } else if (buttonValue.equals(TradingPartnerConstants.LOAD_PAYMENT_ADDRESS_DETAILS)) {
                Set<CustomerAddress> customerAddressList = (Set<CustomerAddress>) tradingPartner.getCustomerAddressSet();
                request.setAttribute(TradingPartnerConstants.CUSTOMER_ADDRESS_DETAILS, customerAddressList);
                request.setAttribute(TradingPartnerConstants.LOAD_PAYMENT_ADDRESS_DETAILS, true);

            } else if (buttonValue.equals(TradingPartnerConstants.LOAD_MASTER_ADDRESS)) {
                CustomerDAO customerDAO = new CustomerDAO();
                List id = customerDAO.findId();
                Iterator it = id.iterator();
                List list = new ArrayList();
                while (it.hasNext()) {
                    Customer customer = customerDAO.findById2(new Integer(it.next().toString()));
                    session.setAttribute("addressMaster", customer);
                    list.add(customer);
                }
                for (int i = 0; i < list.size(); i++) {
                    Customer cus = (Customer) list.get(i);
                }
                request.setAttribute(TradingPartnerConstants.LOAD_MASTER_ADDRESS, true);
                request.setAttribute(TradingPartnerConstants.MASTER_ADDRESS_DETAILS, list);
            }
        }

        return mapping.findForward("addaccount");
    }
}
package com.gp.cong.logisoft.struts.action;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.bc.tradingpartner.APConfigurationBC;
import com.gp.cong.logisoft.bc.tradingpartner.ARConfigurationBC;
import com.gp.cong.logisoft.bc.tradingpartner.AddressBC;
import com.gp.cong.logisoft.bc.tradingpartner.ContactConfigurationBC;
import com.gp.cong.logisoft.bc.tradingpartner.GeneralInformationBC;
import com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC;
import com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerConstants;
import com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerInfo;
import com.gp.cong.logisoft.domain.CustomerAddress;
import com.gp.cong.logisoft.domain.CustomerContact;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.CustomerContactDAO;
import com.gp.cong.logisoft.hibernate.dao.NotesDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.servlet.FileViewerServlet;
import com.gp.cong.logisoft.struts.form.TradingPartnerForm;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.struts.form.CustomerAddressForm;
import com.oreilly.servlet.ServletUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class TradingPartnerAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        TradingPartnerForm tradingPartnerForm = (TradingPartnerForm) form;// TODO Auto-generated method stub
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        String buttonAction = tradingPartnerForm.getButtonValue();
        TradingPartnerBC tradingPartnerBC = new TradingPartnerBC();
        APConfigurationBC apConfigurationBC = new APConfigurationBC();
        if (CommonUtils.isEqual(buttonAction, "gotoTradingPartner")) {
            HttpSession session = request.getSession();
            TradingPartner tradingPartner = new TradingPartnerDAO().findById(request.getParameter("accountNumber"));
            tradingPartnerForm = tradingPartnerBC.setFormValue(tradingPartner);
            tradingPartnerForm.setPaymentSet(tradingPartner.getPaymentset());
            tradingPartnerForm.setAccountType(tradingPartner.getAcctType());
            session.setAttribute(TradingPartnerConstants.TRADINGPARTNER, tradingPartner);
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM, tradingPartnerForm);
            session.removeAttribute(TradingPartnerConstants.VIEW);
            request.setAttribute("trade", "searchcustomer");
            return mapping.findForward("addCustomerFrame");
        } else if (buttonAction != null && buttonAction.equals("addAddressToApConfiguration")) {
            if (null == tradingPartnerForm.getDefaultPaymentMethod() || !tradingPartnerForm.getDefaultPaymentMethod().equals("Y")) {
                tradingPartnerForm.setDefaultPaymentMethod("N");
            }
            TradingPartnerForm tradingPartnerSetForm = apConfigurationBC.saveAPConfigurationAddress(tradingPartnerForm);
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM, tradingPartnerSetForm);// setting request parameter
            return mapping.findForward("sendToVendorPage");
        } // this  method used by AP config page to add or update payament method
        else if (buttonAction != null && buttonAction.equals("addOrUpdatePayementMethod")) {
            apConfigurationBC.saveAPconfiguration(tradingPartnerForm, loginUser);
            apConfigurationBC.saveOrUpdatePaymentMethod(tradingPartnerForm, loginUser);
            tradingPartnerForm.setPaymentSet(new TradingPartnerDAO().getPaymentMethods(tradingPartnerForm.getTradingPartnerId()));
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM, tradingPartnerForm);
            return mapping.findForward("sendToVendorPage");
        }//this  method used by AP config page to delete  payament method
        else if (buttonAction != null && buttonAction.equals("deletePaymentMethod")) {
            apConfigurationBC.deletePaymentMethod(tradingPartnerForm, loginUser);
            tradingPartnerForm.setPaymentSet(new TradingPartnerDAO().getPaymentMethods(tradingPartnerForm.getTradingPartnerId()));
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM, tradingPartnerForm);
            return mapping.findForward("sendToVendorPage");
        } // This is Action has set on vendor page  to get all Customer address
        else if (buttonAction != null && buttonAction.equals("getAllAPAddress")) {// for AP config screen
            TradingPartner tradingPartner = tradingPartnerBC.findTradingPartnerById(tradingPartnerForm.getTradingPartnerId());
            tradingPartnerForm.setCustomerContactList(tradingPartner.getCustomerContact());
            tradingPartnerForm.setPaymentSet(tradingPartner.getPaymentset());
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM, tradingPartnerForm);
            request.setAttribute("apAddress", "apAddress");
            return mapping.findForward("sendToVendorPage");
        } //This is Action has set on vendor page  to save Ap Configuration
        else if (buttonAction != null && buttonAction.equals("saveAPConfig")) {// for AP config screen
            apConfigurationBC.saveAPconfiguration(tradingPartnerForm, loginUser);
            tradingPartnerForm.setPaymentSet(new TradingPartnerDAO().getPaymentMethods(tradingPartnerForm.getTradingPartnerId()));
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM, tradingPartnerForm);
            return mapping.findForward("sendToVendorPage");
        } else if (buttonAction != null && buttonAction.equals("contactCodeManualPdf")) {// for Contact Code PDF
            String outputFileName = LoadLogisoftProperties.getProperty("application.contactCodeManual");
            if (outputFileName != null && !outputFileName.equals("")) {
                File outputFile = new File(outputFileName);
                response.addHeader("Content-Disposition", "inline; filename=" + outputFile.getName());
                response.setContentType(new FileViewerServlet().getMimeType(outputFile) + ";charset=utf-8");
                ServletUtils.returnFile(outputFileName, response.getOutputStream());
            }
            return null;
        } else {
            String index = tradingPartnerForm.getIndex();
            String forward = null;
            TradingPartnerBC tradingBC = new TradingPartnerBC();
            //if we call from booking then attribute is set
            //Start of ARConfiguraton methods
            if (buttonAction != null && !buttonAction.trim().equals("")) {
                forward = buttonActions(buttonAction, request, tradingPartnerForm, "");
            }
            if (request.getParameter("callFrom") != null && !request.getParameter("callFrom").equalsIgnoreCase("")) {
                request.setAttribute("callFrom", request.getParameter("callFrom"));
            }
            if (request.getParameter("field") != null && !request.getParameter("field").equalsIgnoreCase("")) {
                request.setAttribute("field", request.getParameter("field"));
            }
            if (buttonAction != null && buttonAction.equals("saveAccountType")) {
                if (request.getParameter("callFrom") != null && !request.getParameter("callFrom").equalsIgnoreCase("")) {
                    request.setAttribute("callFrom", "close");
                }
                if (request.getParameter("field") != null && !request.getParameter("field").equalsIgnoreCase("")) {
                    request.setAttribute("field", request.getParameter("field"));
                }
            }
            return mapping.findForward(forward);
        }
    }

    /**
     * @param buttonAction
     * @return
     */
    public String buttonActions(String buttonAction, HttpServletRequest request, TradingPartnerForm tradingPartnerForm, String index) throws Exception {
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        TradingPartnerBC tradingPartnerBC = null;
        GeneralInformationBC generalInformationBC = null;
        ContactConfigurationBC contactConfigurationBC = null;
        HttpSession session = ((HttpServletRequest) request).getSession();

        String forward = null;
        // update trading partner(customer) records
        if (buttonAction != null && buttonAction.equals("getFormValues")) {
            tradingPartnerBC = new TradingPartnerBC();
            TradingPartner tradingPartner = tradingPartnerBC.getCustomer(tradingPartnerForm);
            tradingPartnerForm = tradingPartnerBC.setFormValue(tradingPartner);
            tradingPartnerForm.setPaymentSet(tradingPartner.getPaymentset());
            tradingPartnerForm.setAccountType(tradingPartner.getAcctType());
            tradingPartnerForm.setMaster(tradingPartner.getMaster());
            tradingPartnerForm.setDisabled(tradingPartner.getDisabled());
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM, tradingPartnerForm);
            request.setAttribute(TradingPartnerConstants.VIEW, request.getParameter(TradingPartnerConstants.VIEW));
            request.setAttribute("trade", "searchcustomer");
            forward = request.getParameter("forward");
        } else if (buttonAction != null && buttonAction.equals("editCustomer")) {
            tradingPartnerBC = new TradingPartnerBC();
            TradingPartner tradingPartner = tradingPartnerBC.getCustomer(tradingPartnerForm);
            tradingPartnerForm = tradingPartnerBC.setFormValue(tradingPartner);
            tradingPartnerForm.setPaymentSet(tradingPartner.getPaymentset());
            tradingPartnerForm.setAccountType(tradingPartner.getAcctType());
            tradingPartnerForm.setMaster(tradingPartner.getMaster());
            tradingPartnerForm.setDisabled(tradingPartner.getDisabled());
            session.setAttribute(TradingPartnerConstants.TRADINGPARTNER, tradingPartner);
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM, tradingPartnerForm);
            session.removeAttribute(TradingPartnerConstants.VIEW);
            request.setAttribute("trade", "searchcustomer");
            request.setAttribute("isNotes", new NotesDAO().isCustomerNotes(tradingPartner.getAccountno()));
            forward = "addCustomerFrame";
        } else if (buttonAction != null && buttonAction.equals("moreInfoCustomer")) {
            tradingPartnerBC = new TradingPartnerBC();
            TradingPartner tradingPartner = tradingPartnerBC.getCustomer(tradingPartnerForm);
            tradingPartnerForm = tradingPartnerBC.setFormValue(tradingPartner);
            tradingPartnerForm.setPaymentSet(tradingPartner.getPaymentset());
            tradingPartnerForm.setAccountType(tradingPartner.getAcctType());
            tradingPartnerForm.setMaster(tradingPartner.getMaster());
            tradingPartnerForm.setDisabled(tradingPartner.getDisabled());
            session.setAttribute(TradingPartnerConstants.TRADINGPARTNER, tradingPartner);
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM, tradingPartnerForm);
            request.setAttribute(TradingPartnerConstants.VIEW, "3");
            request.setAttribute("trade", "searchcustomer");
            forward = "addCustomerFrame";
        } //update MasterTradingPartner records
        else if (buttonAction.equals("editMasterCustomer")) {
            tradingPartnerBC = new TradingPartnerBC();
            TradingPartner tradingPartner = tradingPartnerBC.getCustomer(tradingPartnerForm);
            tradingPartnerForm = tradingPartnerBC.setFormValue(tradingPartner);
            tradingPartnerForm.setPaymentSet(tradingPartner.getPaymentset());
            tradingPartnerForm.setAccountType(tradingPartner.getAcctType());
            tradingPartnerForm.setMaster(tradingPartner.getMaster());
            tradingPartnerForm.setDisabled(tradingPartner.getDisabled());
            session.setAttribute(TradingPartnerConstants.TRADINGPARTNER, tradingPartner);
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM, tradingPartnerForm);
            session.removeAttribute(TradingPartnerConstants.VIEW);
            request.setAttribute("trade", "mastercustomer");
            request.setAttribute("accountType", tradingPartner.getAcctType());
            forward = "addCustomerFrame";
        } //------**** THIS ACTION USED BY GENERALINFORMATION TAB TO STORE GENERAL INFORMATION FOR CUSTOMER ****-----
        else if (buttonAction != null && buttonAction.equals("saveGeneralInformation")) {// for General Information screen
            generalInformationBC = new GeneralInformationBC();
            generalInformationBC.saveGeneralInformation(tradingPartnerForm, loginUser);
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM, tradingPartnerForm);
            //request.setAttribute("accountType",tradingPartner.getAcctType());
            request.setAttribute("selectedTab", request.getParameter("selectedTab"));
            forward = "sendToGeneralInformationPage";

        } // ---------for custom.jsp and Master Custom page----------
        else if (buttonAction != null && buttonAction.equals("cancelCustomer")) {
            tradingPartnerBC = new TradingPartnerBC();
            //remove view attribute from session
            if (session.getAttribute(TradingPartnerConstants.VIEW) != null) {
                session.removeAttribute(TradingPartnerConstants.VIEW);
            }
            //---to update the Master in edit of each record.
            if (tradingPartnerForm.getMaster() != null) {
                tradingPartnerBC.updateMaster(tradingPartnerForm);
            }
            if (session.getAttribute("tradingPartnerId") != null) {
                session.removeAttribute("tradingPartnerId");
            }
            if (session.getAttribute(TradingPartnerConstants.TRADINGPARTNER) != null) {
                session.removeAttribute(TradingPartnerConstants.TRADINGPARTNER);
            }
            if (session.getAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM) != null) {
                session.removeAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM);
            }
            if (session.getAttribute(TradingPartnerConstants.TRADINGPARTNER_SEARCH_LIST) != null) {
                session.removeAttribute(TradingPartnerConstants.TRADINGPARTNER_SEARCH_LIST);
            }

            if (tradingPartnerForm.getFromMaster() != null && tradingPartnerForm.getFromMaster().equals("frmMasterCustom")) {
                session.setAttribute("trade", "mastercustomer");
            } else {
                session.setAttribute("trade", "searchcustomer");
            }
            //forwarding to CloseSearch jsp-----------
            forward = "searchcustomerPage";

        } //--- to save Account type in Custom jsp & Master Custom jsp-----
        else if (buttonAction != null && buttonAction.equals("saveAccountType")) {
            generalInformationBC = new GeneralInformationBC();
            generalInformationBC.saveAccountTypeWhileSavingMainSave(tradingPartnerForm, session);
            request.setAttribute("tradingPartnerAcctType", tradingPartnerForm);

            if (request.getParameter("callFrom") != null && !request.getParameter("callFrom").equalsIgnoreCase("")) {

                tradingPartnerBC = new TradingPartnerBC();
                //---to update the Master in edit of each record.
                if (tradingPartnerForm.getMaster() != null) {
                    tradingPartnerBC.updateMaster(tradingPartnerForm);
                }
                if (request.getParameter("accountNameTemp") != null) {
                    request.setAttribute("accountNameTemp", request.getParameter("accountNameTemp"));
                }
                if (session.getAttribute("tradingPartnerId") != null) {
                    session.removeAttribute("tradingPartnerId");
                }
                if (session.getAttribute(TradingPartnerConstants.TRADINGPARTNER) != null) {
                    session.removeAttribute(TradingPartnerConstants.TRADINGPARTNER);
                }
                if (session.getAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM) != null) {
                    session.removeAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM);
                }
            }
            if (tradingPartnerForm.getFromMaster() != null && tradingPartnerForm.getFromMaster().equals("frmMasterCustom")) {
                forward = "mastercustomjsp";
            } else {
                forward = "addCustomerFrame";
            }
        } //------- for SearchCustomer.jsp and MasterSearchCustomer.jsp page------------
        else if (buttonAction != null && buttonAction.equals("SearchMachList")) {
            tradingPartnerBC = new TradingPartnerBC();
            List<TradingPartnerInfo> tradingPartners = tradingPartnerBC.getCustomerMatchList(tradingPartnerForm);
            if (CommonUtils.isNotEmpty(tradingPartners)) {
                TradingPartnerInfo tradingPartnerInfo = (TradingPartnerInfo) tradingPartners.get(0);
                if (StringUtils.contains(tradingPartnerInfo.getAccountType(), TradingPartnerConstants.VENDOR)) {
                    request.setAttribute("subType", tradingPartnerInfo.getSubType());
                    request.setAttribute("sslineNumber", tradingPartnerInfo.getSslineNumber());
                }
                request.setAttribute("accountType", tradingPartnerInfo.getAccountType());
            }
            session.setAttribute(TradingPartnerConstants.TRADINGPARTNER_SEARCH_LIST, tradingPartners);
            forward = "searchcustomerPage";
        } else if (buttonAction != null && buttonAction.equals("searchMasterCustomers")) {
            tradingPartnerBC = new TradingPartnerBC();
            session.setAttribute(TradingPartnerConstants.TRADINGPARTNER_MASTER_SEARCH_LIST, tradingPartnerBC.getCustomerMatchList(tradingPartnerForm));
            forward = "masterSearchCustomerPage";
        } else if (buttonAction != null && buttonAction.equals("removesession")) {
            session.removeAttribute(TradingPartnerConstants.TRADINGPARTNER_SEARCH_LIST);
            forward = "searchcustomerPage";
        } else if (buttonAction != null && buttonAction.equals("removesessionmaster")) {
            session.removeAttribute(TradingPartnerConstants.TRADINGPARTNER_MASTER_SEARCH_LIST);
            forward = "masterSearchCustomerPage";
        } //-------******* FOR CUSTOMER ADDRESS *******-------------
        else if (buttonAction != null && buttonAction.equals("addCustAdrress")) {
            tradingPartnerBC = new TradingPartnerBC();
            AddressBC addressBC = new AddressBC();
            TradingPartner tradingPartner = addressBC.saveCustomerAddress(tradingPartnerForm, loginUser);
            tradingPartnerForm = tradingPartnerBC.setFormValue(tradingPartner);
            tradingPartnerForm.setPaymentSet(tradingPartner.getPaymentset());
            tradingPartnerForm.setAccountType(tradingPartner.getAcctType());
            tradingPartnerForm.setMaster(tradingPartner.getMaster());
            tradingPartnerForm.setDisabled(tradingPartner.getDisabled());
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM, tradingPartnerForm);
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER, tradingPartner);
            forward = "sendToCustomerPage";
        } else if (buttonAction != null && buttonAction.equals("deleteCustomerAddress")) {
            AddressBC addressBC = new AddressBC();
            TradingPartner tradingPartner = addressBC.deleteCustomerAddress(tradingPartnerForm);
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER, tradingPartner);
            forward = "sendToCustomerPage";
        } else if (buttonAction != null && buttonAction.equals("editCustomerAddress")) {
            AddressBC addressBC = new AddressBC();
            request.setAttribute(TradingPartnerConstants.CUST_ADDRESS, addressBC.editCustomerAddress(tradingPartnerForm));
            request.setAttribute(TradingPartnerConstants.ADD_ADDRESS, "addNewAddress");
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER, addressBC.getCustomerDetails(tradingPartnerForm));
            forward = "sendToCustomerPage";
        } else if (buttonAction != null && buttonAction.equals("updateCustomerAddress")) {
            AddressBC addressBC = new AddressBC();
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER, addressBC.updateCustomerAddress(tradingPartnerForm, loginUser));
            forward = "sendToCustomerPage";
        } else if (buttonAction != null && buttonAction.equals("addNewCustomerAddress")) {
            AddressBC addressBC = new AddressBC();
            CustomerAddress customerAddress = new CustomerAddress();
            TradingPartner tradingPartner = addressBC.getCustomerDetails(tradingPartnerForm);
            if (null != tradingPartner && null != tradingPartner.getCustomerLocation()) {
                customerAddress.setCity2(tradingPartner.getCustomerLocation().getUnLocationName());
                customerAddress.setUnLocCode(tradingPartner.getCustomerLocation().getUnLocationCode());
                customerAddress.setState(null != tradingPartner.getCustomerLocation().getStateId() ? tradingPartner.getCustomerLocation().getStateId().getCode() : "");
                customerAddress.setCuntry(tradingPartner.getCustomerLocation().getCountryId());
                if (null != tradingPartnerForm.getAddress1()) {
                    customerAddress.setAddress1(tradingPartnerForm.getAddress1());
                }
                request.setAttribute(TradingPartnerConstants.CUST_ADDRESS, customerAddress);
            }
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER, tradingPartner);
            request.setAttribute(TradingPartnerConstants.ADD_ADDRESS, "addNewAddress");
            forward = "sendToCustomerPage";
        } else if (buttonAction != null && buttonAction.equals("cancelCustomerAddress")) {
            AddressBC addressBC = new AddressBC();
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER, addressBC.getCustomerDetails(tradingPartnerForm));
            request.setAttribute(TradingPartnerConstants.ADD_ADDRESS, null);
            forward = "sendToCustomerPage";
        }

        //-------******** FOR CONTACT CONFIGURATION ***********------------
        if (buttonAction != null && buttonAction.equals("addNewContactDetails")) {
            contactConfigurationBC = new ContactConfigurationBC();
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER, contactConfigurationBC.getCustomerDetails(tradingPartnerForm));
            request.setAttribute(TradingPartnerConstants.ADD_CONTACT_DETAILS, "addNewContactDetails");
            forward = "contactconfig";

        }
        if (buttonAction != null && buttonAction.equals("saveContactConfiguration")) {
            List contactList = new ArrayList();
            contactConfigurationBC = new ContactConfigurationBC();
            TradingPartner tradingPartner = contactConfigurationBC.saveContactDetails(tradingPartnerForm, loginUser);
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER, tradingPartner);

            //----forwarding to CustomerAddress jsp if Contact is added from Quotes--------
            if (tradingPartnerForm.getFromMaster() != null && tradingPartnerForm.getFromMaster().equalsIgnoreCase("QuotesContactLookUp")) {
                CustomerContactDAO customerContactDAO = new CustomerContactDAO();
                //contactList=customerContactDAO.findContactsByAcctNo(tradingPartnerForm.getTradingPartnerId());
                //Iterator iter=contactList.iterator();
                TradingPartner tradingPartnerNew = contactConfigurationBC.getCustomerDetails(tradingPartnerForm);
                List customerList = customerContactDAO.findContactsByAcctNo(tradingPartnerNew.getAccountno());
                /* if (customerList != null) {
                 Iterator iter = customerList.iterator();
                 while (iter.hasNext()) {
                 CustAddress custAddress = new CustAddress();
                 CustomerContact customerContact = (CustomerContact) iter.next();
                 custAddress.setPhone(customerContact.getPhone());
                 custAddress.setFax(customerContact.getFax());
                 custAddress.setEmail1(customerContact.getEmail());
                 custAddress.setAcctNo(tradingPartnerForm.getTradingPartnerId());
                 custAddress.setAcctName(tradingPartnerForm.getAccountName());
                 custAddress.setContactName(customerContact.getFirstName() + " " + customerContact.getLastName());
                 custAddress.setPosition(customerContact.getPosition());
                 custAddress.setId(customerContact.getId());
                 contactList.add(custAddress);
                 }
                 }*/
                CustomerAddressForm customerAddressForm = new CustomerAddressForm();
                customerAddressForm.setCustName(tradingPartnerForm.getAccountName());
                customerAddressForm.setCustNo(tradingPartnerForm.getTradingPartnerId());
                request.setAttribute("customerAddressForm", customerAddressForm);

                session.setAttribute("UpdatedContactList", customerList);
                //----setting request to get custNo and CustName------
                request.setAttribute("custNo", tradingPartnerForm.getTradingPartnerId());
                request.setAttribute("customerName", tradingPartnerForm.getAccountName());
                forward = "customerAddressJsp";
            } else {
                forward = "contactconfig";
            }
        } else if (buttonAction != null && buttonAction.equals("editContactForImageIcon")) {
            CustomerContact customerContact = new CustomerContact();
            contactConfigurationBC = new ContactConfigurationBC();
            TradingPartnerForm tradingPartnerFormNew = new TradingPartnerForm();
            tradingPartnerFormNew = contactConfigurationBC.findCustomerRecordForSelect(tradingPartnerForm);
            request.setAttribute(TradingPartnerConstants.CUSTOMER_CONTACT_DETAILS, contactConfigurationBC.findEachCustomerRecord(tradingPartnerForm));
            request.setAttribute(TradingPartnerConstants.ADD_CONTACT_DETAILS, "addNewContactDetails");
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER, contactConfigurationBC.getCustomerDetails(tradingPartnerForm));
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM, tradingPartnerFormNew);
            forward = "contactconfig";
        } else if (buttonAction != null && buttonAction.equals("UpdateContactConfiguration")) {
            CustomerContactDAO customerContactDAO = new CustomerContactDAO();
            contactConfigurationBC = new ContactConfigurationBC();
            TradingPartnerForm tradingPartnerFormNew = new TradingPartnerForm();
            tradingPartnerFormNew = null;
            List updatedList = new ArrayList();
            TradingPartner tradingPartner = contactConfigurationBC.updateContactDetails(tradingPartnerForm, loginUser);
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER, tradingPartner);
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM, tradingPartnerFormNew);

            //---forwarding to CustomerAddress jsp if Contact is added from Quotes-----
            if (tradingPartnerForm.getFromMaster() != null && tradingPartnerForm.getFromMaster().equalsIgnoreCase("QuotesContactLookUp")) {
                List customerList = customerContactDAO.findContactsByAcctNo(tradingPartner.getAccountno());
                /*if (customerList != null) {
                 Iterator iter = customerList.iterator();
                 while (iter.hasNext()) {
                 CustAddress custAddress = new CustAddress();
                 CustomerContact customerContact = (CustomerContact) iter.next();
                 custAddress.setPhone(customerContact.getPhone());
                 custAddress.setFax(customerContact.getFax());
                 custAddress.setEmail1(customerContact.getEmail());
                 custAddress.setAcctNo(tradingPartnerForm.getTradingPartnerId());
                 custAddress.setAcctName(tradingPartnerForm.getAccountName());
                 custAddress.setContactName(customerContact.getFirstName() + " " + customerContact.getLastName());
                 custAddress.setPosition(customerContact.getPosition());
                 custAddress.setId(customerContact.getId());
                 updatedList.add(custAddress);
                 }
                 }*/
                session.setAttribute("UpdatedContactList", customerList);
                //----setting request to get custNo and custName------
                request.setAttribute("custNo", tradingPartnerForm.getTradingPartnerId());
                request.setAttribute("customerName", tradingPartnerForm.getAccountName());
                CustomerAddressForm customerAddressForm = new CustomerAddressForm();
                customerAddressForm.setCustName(tradingPartnerForm.getAccountName());
                customerAddressForm.setCustNo(tradingPartnerForm.getTradingPartnerId());
                request.setAttribute("customerAddressForm", customerAddressForm);
                forward = "customerAddressJsp";
            } else {
                forward = "contactconfig";
            }
        } else if (buttonAction != null && buttonAction.equals("deleteContactConfiguration")) {
            contactConfigurationBC = new ContactConfigurationBC();
            TradingPartner tradingPartner = contactConfigurationBC.deleteContactDetails(tradingPartnerForm, loginUser);
            //TradingPartner tradingPartner=tradingPartnerBC.getCustomer(tradingPartnerForm);
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER, tradingPartner);
            forward = "contactconfig";
        } else if (buttonAction != null && buttonAction.equals("editcancel")) {
            contactConfigurationBC = new ContactConfigurationBC();
            TradingPartner tradingPartner = contactConfigurationBC.getCustomerDetails(tradingPartnerForm);
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER, tradingPartner);
            //---forwarding to CustomerAddress jsp if Contact is added from Quotes-----
            if (tradingPartnerForm.getFromMaster() != null && tradingPartnerForm.getFromMaster().equalsIgnoreCase("QuotesContactLookUp")) {
                CustomerContactDAO customerContactDAO = new CustomerContactDAO();
                List updatedList = new ArrayList();
                List customerList = new ArrayList();
                if (tradingPartner != null) {
                    customerList = customerContactDAO.findContactsByAcctNo(tradingPartner.getAccountno());
                }
                /*if (customerList != null) {
                 Iterator iter = customerList.iterator();
                 while (iter.hasNext()) {
                 CustAddress custAddress = new CustAddress();
                 CustomerContact customerContact = (CustomerContact) iter.next();
                 custAddress.setPhone(customerContact.getPhone());
                 custAddress.setFax(customerContact.getFax());
                 custAddress.setEmail1(customerContact.getEmail());
                 custAddress.setAcctNo(tradingPartnerForm.getTradingPartnerId());
                 custAddress.setAcctName(tradingPartnerForm.getAccountName());
                 custAddress.setContactName(customerContact.getFirstName() + " " + customerContact.getLastName());
                 custAddress.setPosition(customerContact.getPosition());
                 custAddress.setId(customerContact.getId());
                 updatedList.add(custAddress);
                 }
                 }*/
                session.setAttribute("UpdatedContactList", customerList);
                //----setting request to get custNo and custName------
                request.setAttribute("custNo", tradingPartnerForm.getTradingPartnerId());
                request.setAttribute("customerName", tradingPartnerForm.getAccountName());
                CustomerAddressForm customerAddressForm = new CustomerAddressForm();
                customerAddressForm.setCustName(tradingPartnerForm.getAccountName());
                customerAddressForm.setCustNo(tradingPartnerForm.getTradingPartnerId());
                request.setAttribute("customerAddressForm", customerAddressForm);
                forward = "customerAddressJsp";
            } else {
                forward = "contactconfig";
            }
        } else if (buttonAction != null && buttonAction.equals("cancel")) {
            contactConfigurationBC = new ContactConfigurationBC();
            TradingPartner tradingPartner = contactConfigurationBC.getCustomerDetails(tradingPartnerForm);
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER, tradingPartner);
            //---forwarding to CustomerAddress jsp if Contact is added from Quotes-----
            if (tradingPartnerForm.getFromMaster() != null && tradingPartnerForm.getFromMaster().equalsIgnoreCase("QuotesContactLookUp")) {
                CustomerContactDAO customerContactDAO = new CustomerContactDAO();
                List customerList = customerContactDAO.findContactsByAcctNo(tradingPartner.getAccountno());
                /* List updatedList = new ArrayList();
                 if (customerList != null) {
                 Iterator iter = customerList.iterator();
                 while (iter.hasNext()) {
                 CustAddress custAddress = new CustAddress();
                 CustomerContact customerContact = (CustomerContact) iter.next();
                 custAddress.setPhone(customerContact.getPhone());
                 custAddress.setFax(customerContact.getFax());
                 custAddress.setEmail1(customerContact.getEmail());
                 custAddress.setAcctNo(tradingPartnerForm.getTradingPartnerId());
                 custAddress.setAcctName(tradingPartnerForm.getAccountName());
                 custAddress.setContactName(customerContact.getFirstName() + " " + customerContact.getLastName());
                 custAddress.setPosition(customerContact.getPosition());
                 custAddress.setId(customerContact.getId());
                 updatedList.add(custAddress);
                 }
                 }*/
                session.setAttribute("UpdatedContactList", customerList);
                request.setAttribute("custNo", tradingPartnerForm.getTradingPartnerId());
                CustomerAddressForm customerAddressForm = new CustomerAddressForm();
                customerAddressForm.setCustName(tradingPartnerForm.getAccountName());
                customerAddressForm.setCustNo(tradingPartnerForm.getTradingPartnerId());
                request.setAttribute("customerAddressForm", customerAddressForm);
                forward = "customerAddressJsp";
            } else {
                forward = "contactconfig";
            }
        } //--------------****** FOR AR CONFIGURATION   ********------------------------------
        else if (buttonAction.equals(TradingPartnerConstants.LOAD_CONTACT_DETAILS)) {
            ARConfigurationBC arConfigurationBC = new ARConfigurationBC();
            Set<CustomerContact> customerContactList = arConfigurationBC.getCustomerContacts(tradingPartnerForm.getTradingPartnerId());
            request.setAttribute(TradingPartnerConstants.LOAD_CONTACT_DETAILS, true);
            tradingPartnerForm.setCustomerContactList(customerContactList);
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM, tradingPartnerForm);
            forward = TradingPartnerConstants.ARCONFIGURATION_PAGE;

        } else if (buttonAction.equals(TradingPartnerConstants.LOAD_INVOICE_ADDRESS_DETAILS)) {
            ARConfigurationBC arConfigurationBC = new ARConfigurationBC();
            Set<CustomerAddress> customerAddressList = arConfigurationBC.getCustomerAddress(tradingPartnerForm.getTradingPartnerId());
            request.setAttribute(TradingPartnerConstants.LOAD_INVOICE_ADDRESS_DETAILS, true);
            tradingPartnerForm.setCustomerAddressList(customerAddressList);
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM, tradingPartnerForm);
            forward = TradingPartnerConstants.ARCONFIGURATION_PAGE;

        } else if (buttonAction.equals(TradingPartnerConstants.LOAD_PAYMENT_ADDRESS_DETAILS)) {
            ARConfigurationBC arConfigurationBC = new ARConfigurationBC();
            Set<CustomerAddress> customerAddressList = arConfigurationBC.getCustomerAddress(tradingPartnerForm.getTradingPartnerId());
            request.setAttribute(TradingPartnerConstants.LOAD_PAYMENT_ADDRESS_DETAILS, true);
            tradingPartnerForm.setCustomerAddressList(customerAddressList);
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM, tradingPartnerForm);
            forward = TradingPartnerConstants.ARCONFIGURATION_PAGE;

        } else if (buttonAction.equals(TradingPartnerConstants.LOAD_MASTER_ADDRESS)) {
            ARConfigurationBC arConfigurationBC = new ARConfigurationBC();
            List masterAddressList = arConfigurationBC.getMasterCustomerAddress();
            request.setAttribute(TradingPartnerConstants.LOAD_MASTER_ADDRESS, true);
            tradingPartnerForm.setMasterAddressList(masterAddressList);
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM, tradingPartnerForm);
            forward = TradingPartnerConstants.ARCONFIGURATION_PAGE;

        } else if (buttonAction.equals(TradingPartnerConstants.ADD_CONTACT_DETAILS)) {
            String contactId = tradingPartnerForm.getContactId();
            Integer selectedContactId = 0;
            //get selected contactId from ARConfiguration Tab
            if (null != contactId && !contactId.trim().equals("")) {
                selectedContactId = new Integer(contactId);
            }
            ARConfigurationBC arConfigurationBC = new ARConfigurationBC();
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM,
                    arConfigurationBC.setContactDetails(tradingPartnerForm, tradingPartnerForm.getTradingPartnerId(), selectedContactId));
            arConfigurationBC.saveCustomerAccounting(tradingPartnerForm, loginUser, request);
            forward = TradingPartnerConstants.ARCONFIGURATION_PAGE;

        } else if (buttonAction.equals(TradingPartnerConstants.ADD_INVOICE_ADDRESS)) {
            String contactId = tradingPartnerForm.getContactId();
            Integer selectedContactId = 0;
            //get selected contactId from ARConfiguration Tab
            if (null != contactId && !contactId.trim().equals("")) {
                selectedContactId = new Integer(contactId);
            }
            ARConfigurationBC arConfigurationBC = new ARConfigurationBC();
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM,
                    arConfigurationBC.setInvoiceAddressDetails(tradingPartnerForm, tradingPartnerForm.getTradingPartnerId(), selectedContactId));
            forward = TradingPartnerConstants.ARCONFIGURATION_PAGE;

        } else if (buttonAction.equals(TradingPartnerConstants.ADD_PAYMENT_ADDRESS)) {
            String contactId = tradingPartnerForm.getContactId();
            Integer selectedContactId = 0;
            //get selected contactId from ARConfiguration Tab
            if (null != contactId && !contactId.trim().equals("")) {
                selectedContactId = new Integer(contactId);
            }
            ARConfigurationBC arConfigurationBC = new ARConfigurationBC();
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM,
                    arConfigurationBC.setPaymentAddressDetails(tradingPartnerForm, tradingPartnerForm.getTradingPartnerId(), selectedContactId));
            forward = TradingPartnerConstants.ARCONFIGURATION_PAGE;

        } else if (buttonAction.equals(TradingPartnerConstants.ADD_MASTER_ADDRESS)) {
            String contactId = tradingPartnerForm.getContactId();
            Integer selectedContactId = 0;
            //get selected contactId from ARConfiguration Tab
            if (null != contactId && !contactId.trim().equals("")) {
                selectedContactId = new Integer(contactId);
            }
            ARConfigurationBC arConfigurationBC = new ARConfigurationBC();
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM,
                    arConfigurationBC.setMasterAddressDetails(tradingPartnerForm, selectedContactId));
            forward = TradingPartnerConstants.ARCONFIGURATION_PAGE;

        } else if (TradingPartnerConstants.SAVE_ARCONFIGURATION.equals(buttonAction)) {
            ARConfigurationBC arConfigurationBC = new ARConfigurationBC();
            arConfigurationBC.saveCustomerAccounting(tradingPartnerForm, loginUser, request);
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM, tradingPartnerForm);
            forward = TradingPartnerConstants.ARCONFIGURATION_PAGE;

        } else if (TradingPartnerConstants.SHOW_SUBSIDIARIERS.equals(buttonAction)) {
            tradingPartnerBC = new TradingPartnerBC();
            request.setAttribute(TradingPartnerConstants.SUBSIDIARIERS_LIST, tradingPartnerBC.getSubsidiariesAccountForMaster(tradingPartnerForm.getMaster()));
            forward = TradingPartnerConstants.SHOW_SUBSIDIARIERS_PAGE;
        } else if ("addTradingPartner".equals(buttonAction)) {
            forward = TradingPartnerConstants.SHOW_SUBSIDIARIERS_PAGE;
        } else if (buttonAction.equals("addNewTradingPartner")) {
            // This  Action is used by search page to add a new Trading Partner
            String[] accountTypes = StringUtils.split(StringUtils.removeEnd(tradingPartnerForm.getAccountType(), ","), ",");
            for (String accountType : accountTypes) {
                if (accountType.trim().equals("S")) {
                    tradingPartnerForm.setAccountType1(CommonConstants.ON);
                } else if (accountType.trim().equals("N")) {
                    tradingPartnerForm.setAccountType3(CommonConstants.ON);
                } else if (accountType.trim().equals("C")) {
                    tradingPartnerForm.setAccountType4(CommonConstants.ON);
                } else if (accountType.trim().equals("I")) {
                    tradingPartnerForm.setAccountType8(CommonConstants.ON);
                } else if (accountType.trim().equals("E")) {
                    tradingPartnerForm.setAccountType9(CommonConstants.ON);
                } else if (accountType.trim().equals("V")) {
                    tradingPartnerForm.setAccountType10(CommonConstants.ON);
                } else if (accountType.trim().equals("O")) {
                    tradingPartnerForm.setAccountType11(CommonConstants.ON);
                } else if (accountType.trim().equals("Z")) {
                    tradingPartnerForm.setAccountType13(CommonConstants.ON);
                }
            }
            TradingPartner tradingPartner = new TradingPartnerBC().addNewTradingPartner(tradingPartnerForm, loginUser);
            session.setAttribute("tradingPartnerId", tradingPartner.getAccountName() + "\\" + tradingPartner.getAccountno() + "\\" + tradingPartner.getMaster() + "");
            tradingPartnerForm.setEciAccountNo(tradingPartner.getEciAccountNo());
            tradingPartnerForm.setEciAccountNo2(tradingPartner.getECIFWNO());
            tradingPartnerForm.setEciAccountNo3(tradingPartner.getECIVENDNO());
            tradingPartnerForm.setAccountNo(tradingPartner.getAccountno());
            tradingPartnerForm.setClimit("0.00");
            tradingPartnerForm.setCterms("11344");
            tradingPartnerForm.setTradingPartnerId(tradingPartner.getAccountno());
            request.setAttribute("tradingPartnerAcctType", tradingPartnerForm);
            forward = "addCustomerFrame";
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM, tradingPartnerForm);
        } else if (TradingPartnerConstants.SAVE_CONSIGNEE_INFO.equals(buttonAction)) {
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER, new TradingPartnerBC().saveConsigneeInfo(tradingPartnerForm));
            forward = TradingPartnerConstants.SHOW_CONSIGNEE_INFO_PAGE;
        } else if (TradingPartnerConstants.SAVE_CTS_INFO.equals(buttonAction)) {
            request.setAttribute(TradingPartnerConstants.TRADINGPARTNER, new TradingPartnerBC().saveCtsInfo(tradingPartnerForm));
            forward = TradingPartnerConstants.SHOW_CTS_INFO_PAGE;
        }
        return forward;
    }
    /**
     * @param constantParameter
     * @return
     */
}

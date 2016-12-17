/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cvst.logisoft.struts.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.bc.fcl.FclBlConstants;
import com.gp.cong.logisoft.hibernate.dao.CustomerDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.struts.form.QuoteCustomerForm;

/**
 * MyEclipse Struts Creation date: 02-04-2009
 * 
 * XDoclet definition:
 * 
 * @struts.action path="/quoteCustomer" name="quoteCustomerForm"
 *                input="/jsps/fclQuotes/QuoteCustomer.jsp" scope="request"
 *                validate="true"
 */
public class QuoteCustomerAction extends Action {
    /*
     * Generated Methods
     */

    /**
     * Method execute
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        QuoteCustomerForm quoteCustomerForm = (QuoteCustomerForm) form;// TODO
        // Auto-generated
        // method
        // stub

        String button = quoteCustomerForm.getButton();
        String customerName = quoteCustomerForm.getCustomerName();
        String customerNo = quoteCustomerForm.getCustomerNo();
        HttpSession session = ((HttpServletRequest) request).getSession();
        String custName = "";
        List customerList = null;
        CustAddressDAO custAddressDAO = new CustAddressDAO();
        CustomerDAO customerDAO = new CustomerDAO();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        if (request.getParameter("buttonValue") != null && (request.getParameter("buttonValue").equals("Quotation") || request.getParameter("buttonValue").equals(
                "CarrierQuotation") || request.getParameter("buttonValue").equals("Shipper") || request.getParameter("buttonValue").equals(
                "MasterShipper") || request.getParameter("buttonValue").equals(
                "Consignee") || request.getParameter("buttonValue").equals(
                "MasterConsignee") || request.getParameter("buttonValue").equals(
                "NotifyParty") || request.getParameter("buttonValue").equals(
                "MasterNotifyParty") || request.getParameter("buttonValue").equals(
                "Forwarder") || request.getParameter("buttonValue").equals(
                "Forwarder") || request.getParameter("buttonValue").equals(
                "BookingShipper") || request.getParameter("buttonValue").equals(
                "AccountNameShipper") || request.getParameter("buttonValue").equals(
                "AccountNameAgent") || request.getParameter("buttonValue").equals(
                "AccountNameForwarder") || request.getParameter("buttonValue").equals("BookingTruker") || request.getParameter("buttonValue").equals(
                "AccountNameThirdParty") || request.getParameter("buttonValue").equals(
                "BookingForwarder") || request.getParameter("buttonValue").equals(
                "BookingConsignee") || request.getParameter("buttonValue").equals(
                "RoutedQuotation") || request.getParameter("buttonValue").equals(
                "RoutedBooking"))) {

            custName = request.getParameter("clientName");
            if (custName != null && custName.equals("percent")) {
                custName = "%";
            }
            if (custName != null && !custName.equals("")) {
                custName = custName.replace("amp;", "&");
                if (request.getParameter("buttonValue") != null && request.getParameter("buttonValue").equals(
                        "CarrierQuotation")) {
                    String acctType = "V%SS";
                    customerList = custAddressDAO.getCustomerForLookUp(custName, acctType, "");
                } else if (request.getParameter("buttonValue") != null && request.getParameter("buttonValue").equals(
                        "AccountNameShipper")) {
                    String acctType = "S";
                    customerList = custAddressDAO.getCustomerForLookUp(custName, acctType, "");
                } else if (request.getParameter("buttonValue") != null && request.getParameter("buttonValue").equals(
                        "AccountNameAgent")) {
                    String acctType = "A";
                    customerList = custAddressDAO.getCustomerForLookUp(custName, acctType, "");
                } else if (request.getParameter("buttonValue") != null && request.getParameter("buttonValue").equals("AccountNameForwarder")) {
                    String acctType = "F";
                    customerList = custAddressDAO.getCustomerForLookUp(custName, acctType, "");
                } else if (request.getParameter("buttonValue") != null && request.getParameter("buttonValue").equals("AccountNameThirdParty")) {
                    String acctType = "";
                    customerList = custAddressDAO.getCustomerForLookUp(custName, acctType, "");
                } else if (request.getParameter("buttonValue").equals("RoutedQuotation")) {
                    customerList = custAddressDAO.getCustomerForLookUp(custName, "E", "");

                } else if (request.getParameter("buttonValue").equals("RoutedBooking")) {
                    customerList = custAddressDAO.getCustomerForLookUp(custName, "E", "");

                } else if (request.getParameter("buttonValue").equals("BookingForwarder")) {
                    customerList = custAddressDAO.getCustomerForLookUp(custName, "", "");

                } else {
                    customerList = custAddressDAO.getCustomerForLookUp(custName, "", "");
                }
            }
            session.setAttribute("buttonValue", request.getParameter("buttonValue"));

            session.setAttribute("customerList", customerList);

        } else if (request.getParameter("paramId") != null) {
            if (session.getAttribute("customerList") != null) {
                customerList = (List) session.getAttribute("customerList");
                CustAddress custAddress = (CustAddress) customerList.get(Integer.parseInt(request.getParameter("paramId")));
                List clientList = new ArrayList();
                String clientTypes = "";
                custAddress.setAcctName(custAddress.getAcctName().replace("'", ":"));
                clientList.add(custAddress.getAcctName());
                clientList.add(custAddress.getAcctNo());
                clientList.add(custAddress.getCoName());
                String clientType[] = StringUtils.split(custAddress.getAcctType(), ",");
                if (clientType != null) {
                    for (int i = 0; i < clientType.length; i++) {
                        String clienttype = clientType[i];
                        if (clienttype.equals("S")) {
                            clientTypes = clientTypes + "S" + ",";
                        }
                        if (clienttype.equals("F")) {
                            clientTypes = clientTypes + "F" + ",";
                        }
                        if (clienttype.equals("C")) {
                            clientTypes = clientTypes + "C" + ",";
                        }
                        if (clienttype.equals("N")) {
                            clientTypes = clientTypes + "N" + ",";
                        }
                        if (clienttype.equals("SS")) {
                            clientTypes = clientTypes + "SS" + ",";
                        }
                        if (clienttype.equals("T")) {
                            clientTypes = clientTypes + "T" + ",";
                        }
                        if (clienttype.equals("A")) {
                            clientTypes = clientTypes + "A" + ",";
                        }
                        if (clienttype.equals("I")) {
                            clientTypes = clientTypes + "I" + ",";
                        }
                        if (clienttype.equals("E")) {
                            clientTypes = clientTypes + "E" + ",";
                        }
                        if (clienttype.equals("V")) {
                            clientTypes = clientTypes + "V" + ",";
                        }
                        if (clienttype.equals("O")) {
                            clientTypes = clientTypes + "O" + ",";
                        }
                    }
                }
                clientList.add(clientTypes);
                clientList.add(custAddress.getContactName());
                clientList.add(custAddress.getPhone());
                clientList.add(custAddress.getFax());
                clientList.add(custAddress.getEmail1());
                String addr = "";

                //custAddress.setAddress1(custAddress.getAddress1().replace("'", ":"));
                String add[] = StringUtils.split(custAddress.getAddress1(), "\n");
                if (add != null) {
                    for (int i = 0; i < add.length; i++) {
                        addr = addr + add[i].trim();
                    }
                }
                StringBuilder addressBuilder = new StringBuilder();
                if (CommonFunctions.isNotNull(custAddress.getCoName())) {
                    addressBuilder.append("C/O ");
                    addressBuilder.append(custAddress.getCoName());
                    addressBuilder.append(" ");
                    addressBuilder.append("\\u000a");
                }
                addressBuilder.append(addr);
                addressBuilder.append(" ");
                addressBuilder.append("\\u000a");
                if (null != custAddress.getCity1() && !custAddress.getCity1().equalsIgnoreCase("")) {
                    addressBuilder.append(custAddress.getCity1());
                    addressBuilder.append(", ");
                }
                if (null != custAddress.getState() && !custAddress.getState().equalsIgnoreCase("")) {
                    addressBuilder.append(custAddress.getState());
                    addressBuilder.append(" ");
                }
                if (null != custAddress.getZip() && !custAddress.getZip().equalsIgnoreCase("")) {
                    addressBuilder.append(custAddress.getZip());
                    addressBuilder.append(" ");
                }
                addressBuilder.append("\\u000a");
                if (custAddress.getCuntry() != null) {
                    if (null != custAddress.getCuntry().getCodedesc() && !custAddress.getCuntry().getCodedesc().equalsIgnoreCase("")
                            && !custAddress.getCuntry().getCodedesc().equalsIgnoreCase(FclBlConstants.COUNTRYNAME)) {
                        addressBuilder.append(custAddress.getCuntry().getCodedesc());
                    }
                    addressBuilder.append("\\u000a");
                }
                if (null != custAddress.getPhone() && !custAddress.getPhone().equalsIgnoreCase("")) {
                    addressBuilder.append("PHONE ");
                    addressBuilder.append(custAddress.getPhone());
                }
                clientList.add(addressBuilder.toString());
                clientList.add(request.getParameter("button"));
                clientList.add(custAddress.getCity1());
                clientList.add(custAddress.getState());
                clientList.add(custAddress.getZip());
                if (custAddress.getCuntry() != null) {
                    clientList.add(custAddress.getCuntry().getCodedesc());
                }
                //clientList.add(custAddress.getComCode());
                //clientList.add(custAddress.getComDesc());
                request.setAttribute("clientList", clientList);
                if (request.getParameter("button").equals("Quotation") || request.getParameter("button").equals("Shipper") || request.getParameter("button").equals(
                        "MasterShipper") || request.getParameter("button").equals("Consignee") || request.getParameter("button").equals(
                        "MasterConsignee") || request.getParameter("button").equals("NotifyParty") || request.getParameter("button").equals(
                        "MasterNotifyParty") || request.getParameter("button").equals("Forwarder")) {
                    request.setAttribute("buttonValue", "QuotationClient");
                } else if (request.getParameter("button").equals(
                        "CarrierQuotation")) {
                    request.setAttribute("buttonValue", "QuotationCarrier");
                } else if (request.getParameter("button").equals("BookingShipper") || request.getParameter("button").equals(
                        "BookingForwarder") || request.getParameter("button").equals("BookingTruker") || request.getParameter("button").equals(
                        "BookingConsignee")) {
                    request.setAttribute("buttonValue", "Booking");
                } else if (request.getParameter("button").equals(
                        "RoutedQuotation")) {
                    request.setAttribute("buttonValue", "RoutedQuotation");
                } else if (request.getParameter("button").equals(
                        "RoutedBooking")) {
                    request.setAttribute("buttonValue", "RoutedBooking");
                } else if (request.getParameter("button").equals(
                        "AccountNameShipper")) {
                    request.setAttribute("buttonValue", "AccountNameShipper");
                } else if (request.getParameter("button").equals(
                        "AccountNameAgent")) {
                    request.setAttribute("buttonValue", "AccountNameAgent");
                } else if (request.getParameter("button").equals(
                        "AccountNameForwarder")) {
                    request.setAttribute("buttonValue", "AccountNameForwarder");
                } else if (request.getParameter("button").equals(
                        "AccountNameThirdParty")) {
                    request.setAttribute("buttonValue", "AccountNameThirdParty");
                } else if (request.getParameter("button").equals("lclSupplier")) {
                    request.setAttribute("buttonValue", "lclSupplier");
                } else if (request.getParameter("button").equals("lclShipper")) {
                    request.setAttribute("buttonValue", "lclShipper");
                } else if (request.getParameter("button").equals("lclForwarder")) {
                    request.setAttribute("buttonValue", "lclForwarder");
                } else if (request.getParameter("button").equals("lclConsignee")) {
                    request.setAttribute("buttonValue", "lclConsignee");
                } else if (request.getParameter("button").equals("lclNotify")) {
                    request.setAttribute("buttonValue", "lclNotify");
                }
                if (session.getAttribute("customerList") != null) {
                    session.removeAttribute("customerList");
                }
                if (session.getAttribute("buttonValue") != null) {
                    session.removeAttribute("buttonValue");
                }
            }
        }
        if (button != null && button.equals("Go")) {
            if (request.getParameter("action") != null && request.getParameter("action").equals("RoutedQuotation")) {
                customerList = custAddressDAO.getCustomerForLookUp(customerName, "E", customerNo);
            } else if (request.getParameter("action") != null && request.getParameter("action").equals("RoutedBooking")) {
                customerList = custAddressDAO.getCustomerForLookUp(customerName, "E", customerNo);
            } else if (null!=session.getAttribute("buttonValue") && session.getAttribute("buttonValue").equals("CarrierQuotation")) {
                customerList = custAddressDAO.getCustomerForLookUp(customerName, "V%SS", customerNo);
            } else if (null!=session.getAttribute("buttonValue") && session.getAttribute("buttonValue").equals("AccountNameShipper")) {
                customerList = custAddressDAO.getCustomerForLookUp(customerName, "S", customerNo);
            } else if (null!=session.getAttribute("buttonValue") && session.getAttribute("buttonValue").equals("AccountNameAgent")) {
                customerList = custAddressDAO.getCustomerForLookUp(customerName, "A", customerNo);
            } else if (null!=session.getAttribute("buttonValue") && session.getAttribute("buttonValue").equals("AccountNameForwarder") || (session.getAttribute("buttonValue").equals("BookingForwarder"))) {
                customerList = custAddressDAO.getCustomerForLookUp(customerName, "", customerNo);
            } else if (null!=session.getAttribute("buttonValue") && session.getAttribute("buttonValue").equals("AccountNameThirdParty")) {
                customerList = custAddressDAO.getCustomerForLookUp(customerName, "", customerNo);
            }/* else if ((request.getParameter("action") != null && request.getParameter("action").equals("NotifyParty")) || (request.getParameter("action") != null && request.getParameter("action").equals("MasterNotifyParty"))) {
            customerList = custAddressDAO.getCustomerForLookUp(customerName, "N", customerNo);
            } */ else {
                customerList = custAddressDAO.getCustomerForLookUp(customerName, "", customerNo);
            }
            /*List custList=new ArrayList();
            for (Iterator iter = custList.iterator(); iter.hasNext();) {
            if(iter instanceof TradingPartner) {
            TradingPartner tradingPartner = (TradingPartner) iter.next();
            CustAddress custAddress=new CustAddress();
            custAddress.setAcctName(tradingPartner.getAccountName());
            custAddress.setAcctNo(tradingPartner.getAccountno());
            custAddress.setAcctType(tradingPartner.getAcctType());
            if(tradingPartner.getCustomerAddressSet()!=null){
            Iterator iter1=(Iterator)tradingPartner.getCustomerAddressSet().iterator();
            while(iter1.hasNext()){
            CustAddress custAddr=(CustAddress)iter1.next();
            if(custAddr.getPrimeAddress().equals("on")){
            custAddress.setContactName(custAddr.getContactName());
            custAddress.setPhone(custAddr.getPhone());
            custAddress.setFax(custAddr.getFax());
            custAddress.setEmail1(custAddr.getEmail1());
            custAddress.setAddress1(custAddr.getAddress1());
            custAddress.setCity1(custAddr.getCity1());
            custAddress.setState(custAddr.getState());
            custAddress.setZip(custAddr.getZip());
            custAddress.setCuntry(custAddr.getCuntry());
            break;
            }
            }
            }
            if(tradingPartner.getGeneralInformation()!=null){
            Iterator iter1=tradingPartner.getGeneralInformation().iterator();
            while(iter1.hasNext()){
            GeneralInformation generalInformation=(GeneralInformation)iter1.next();
            custAddress.setComCode(generalInformation.getCommodityNo());
            custAddress.setComDesc(generalInformation.getCommodityDesc());
            }
            }
            custList.add(custAddress);
            }
            }
            if(custList.size()==0){
            custList=new ArrayList(customerList);
            }
             */ session.setAttribute("customerList", customerList);
        }
        return mapping.findForward("quoteCustomer");
    }
}
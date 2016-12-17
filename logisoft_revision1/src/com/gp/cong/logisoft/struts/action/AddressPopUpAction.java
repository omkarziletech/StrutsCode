package com.gp.cong.logisoft.struts.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.gp.cong.logisoft.hibernate.dao.CustomerDAO;
import com.gp.cong.logisoft.struts.form.AddressPopUpForm;
import com.gp.cong.logisoft.domain.Customer;
import com.gp.cong.logisoft.domain.CustomerAccounting;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.Vendor;

public class AddressPopUpAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        String value = "";

        AddressPopUpForm addressPopUpForm = (AddressPopUpForm) form;
        HttpSession session = ((HttpServletRequest) request).getSession();
        String button = addressPopUpForm.getButtonValue();
        Customer cust = new Customer();
        if (session.getAttribute("button") != null) {
            value = (String) session.getAttribute("button");
        }
        List aList = new ArrayList();
        CustomerAccounting accntList = new CustomerAccounting();
        Vendor vendobj = new Vendor();

        String path = " ";
        if (request.getParameter("ind") != null) {

            int ind = Integer.parseInt(request.getParameter("ind"));


            if (value.equals("econoaddress") || value.equals("ffaddress") || value.equals("editeconoaddress") || value.equals("editffaddress") || value.equals("payaddress") || value.equals("editpayaddress")) {
                if (session.getAttribute("addressList") != null) {
                    aList = (List) session.getAttribute("addressList");
                }
            } else if (value.equals("masterEcoaddress") || value.equals("masterFfaddress") || value.equals("masterEditEcoaddress") || value.equals("masterEditffaddress") || value.equals("checkBoxValue") || value.equals("editcheckBoxValue") || value.equals("mpay") || value.equals("meditpay")) {
                if (session.getAttribute("masteraddressList") != null) {
                    aList = (List) session.getAttribute("masteraddressList");
                }
            }


            cust = (Customer) aList.get(ind);
            if (value.equals("econoaddress") || value.equals("ffaddress") || value.equals("editeconoaddress") || value.equals("editffaddress") || value.equals("checkBoxValue") || value.equals("editchecked")) {
                if (session.getAttribute("accounting") != null) {
                    accntList = (CustomerAccounting) session.getAttribute("accounting");

                } else {
                    accntList = new CustomerAccounting();
                }
                if (value.equals("econoaddress") || value.equals("editeconoaddress") || value.equals("payaddress") || value.equals("editpayaddress")) {
                    accntList.setAddress1(cust.getAddress1());
                    accntList.setCity2(cust.getCity2());
                    accntList.setState(cust.getState());
                    accntList.setZip(cust.getZip());
                    accntList.setCuntry(cust.getCuntry());
                    accntList.setCompany(cust.getCoName());
                } else if (value.equals("ffaddress") || value.equals("editffaddress") || value.equals("checked") || value.equals("editchecked")) {
                    accntList.setPayAddress1(cust.getAddress1());
                    accntList.setPaycity2(cust.getCity2());
                    accntList.setPayZip(cust.getZip());
                    accntList.setPayState(cust.getState());
                    accntList.setPayCuntry(cust.getCuntry());
                    accntList.setPayCompany(cust.getCoName());
                }
                session.setAttribute("accounting", accntList);//-------------SESSION FOR ACCOUNTING PAGE-----------
            } else if (value.equals("payaddress") || value.equals("editpayaddress")) {
                if (session.getAttribute("VendorInfoList") != null) {
                    vendobj = (Vendor) session.getAttribute("VendorInfoList");

                } else {
                    vendobj = new Vendor();
                }
                if (value.equals("payaddress") || value.equals("editpayaddress")) {
                    vendobj.setAddress1(cust.getAddress1());
                    vendobj.setCity2(cust.getCity2());
                    vendobj.setState(cust.getState());
                    vendobj.setZip(cust.getZip());
                    vendobj.setCuntry(cust.getCuntry());
                    vendobj.setCompany(cust.getCoName());
                }
                session.setAttribute("VaddressList", vendobj);//---------SESSION FOR VENDOR PAGE----------
            } else if (value.equals("masterEcoaddress") || value.equals("masterFfaddress") || value.equals("masterEditEcoaddress") || value.equals("masterEditffaddress") || value.equals("checked") || value.equals("editchecked")) {
                if (session.getAttribute("masteraccounting") != null) {
                    accntList = (CustomerAccounting) session.getAttribute("masteraccounting");
                } else {
                    accntList = new CustomerAccounting();
                }
                if (value.equals("masterEcoaddress") || value.equals("masterEditEcoaddress")) {
                    accntList.setAddress1(cust.getAddress1());
                    accntList.setCity2(cust.getCity2());
                    accntList.setState(cust.getState());
                    accntList.setZip(cust.getZip());
                    accntList.setCuntry(cust.getCuntry());
                    accntList.setCompany(cust.getCoName());
                } else if (value.equals("masterFfaddress") || value.equals("masterEditffaddress") || value.equals("checked") || value.equals("editchecked")) {
                    accntList.setPayAddress1(cust.getAddress1());
                    accntList.setPaycity2(cust.getCity2());
                    accntList.setPayZip(cust.getZip());
                    accntList.setPayState(cust.getState());
                    accntList.setPayCuntry(cust.getCuntry());
                    accntList.setPayCompany(cust.getCoName());
                }
                session.setAttribute("masteraccounting", accntList);//---------SESSION FOR MASTER ACCOUNTING PAGE-------
            } else if (value.equals("mpay") || value.equals("meditpay")) {
                if (session.getAttribute("MasterVendorInfoList") != null) {
                    vendobj = (Vendor) session.getAttribute("MasterVendorInfoList");

                } else {
                    vendobj = new Vendor();
                }
                if (value.equals("mpay") || value.equals("meditpay")) {
                    vendobj.setAddress1(cust.getAddress1());
                    vendobj.setCity2(cust.getCity2());
                    vendobj.setState(cust.getState());
                    vendobj.setZip(cust.getZip());
                    vendobj.setCuntry(cust.getCuntry());
                    vendobj.setCompany(cust.getCoName());
                }
                session.setAttribute("VMasteraddress", vendobj);//----------SESSION FOR MASTER VENDOR PAGE-----------

            }


            request.setAttribute("accounting", "accounting");

            if (value.equals("econoaddress") || value.equals("ffaddress") || value.equals("checked")) {
                path = "/jsps/Tradingpartnermaintainance/Accounting.jsp";
            }
            if (value.equals("editeconoaddress") || value.equals("editffaddress") || value.equals("editchecked")) {
                path = "/jsps/Tradingpartnermaintainance/EditAccounting.jsp";
            }
            if (value.equals("masterEcoaddress") || value.equals("masterFfaddress")) {
                path = "/jsps/Tradingpartnermaintainance/MasterAccounting.jsp";
            }
            if (value.equals("masterEditEcoaddress") || value.equals("masterEditffaddress")) {
                path = "/jsps/Tradingpartnermaintainance/masterEditAccounting.jsp";
            }
            if (value.equals("payaddress")) {
                path = "/jsps/Tradingpartnermaintainance/vendor.jsp";
            }
            if (value.equals("editpayaddress")) {
                path = "/jsps/Tradingpartnermaintainance/Editvendor.jsp";
            }
            if (value.equals("mpay")) {
                path = "/jsps/Tradingpartnermaintainance/MasterVendor.jsp";
            }
            if (value.equals("meditpay")) {
                path = "/jsps/Tradingpartnermaintainance/MasterEditVendor.jsp";
            }
            request.setAttribute("path1", path);
        }
        if (button.equals("checkboxValue") || button.equals("editcheckBoxValue")) {
            TradingPartner tradingPartner = null;
//			 if(session.getAttribute("TradingPartner")!=null){
//			 tradingPartner = (TradingPartner)session.getAttribute("TradingPartner");
//			 }else{
//				 tradingPartner = (TradingPartner)session.getAttribute("tradingpartner"); 
//			 }	

            CustomerDAO customerDAO = new CustomerDAO();
            List id = customerDAO.findId();
            Iterator it = id.iterator();
            List list = new ArrayList();
            while (it.hasNext()) {
                Customer customer = customerDAO.findById2(new Integer(it.next().toString()));
                session.setAttribute("addressMaster", customer);
                list.add(customer);
            }
            session.setAttribute("masteraddressList", list);
            if (button.equals("checkboxValue")) {
                request.setAttribute("bvalue", "bvalue");
            }
            if (button.equals("editcheckBoxValue")) {
                request.setAttribute("editbvalue", "bvalue");
            }
        }
        return mapping.findForward("AddressPopUp");

    }
}

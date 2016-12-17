/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.google.gson.Gson;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC;
import com.gp.cong.logisoft.domain.CustomerAddress;
import com.gp.cong.logisoft.domain.GeneralInformation;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.CodetypeDAO;
import com.gp.cong.logisoft.hibernate.dao.GeneralInformationDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.struts.form.TradingPartnerForm;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author LogiwareInc
 */
public class LclCreateNewTPAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TradingPartnerForm tradingPartnerForm = (TradingPartnerForm) form;
        if (tradingPartnerForm.getUnLocCode() == null || tradingPartnerForm.getUnLocCode().equals("")) {
            tradingPartnerForm.setUnLocCode(new UnLocationDAO().getUnLocCode(tradingPartnerForm.getCity(), tradingPartnerForm.getState()));
        }
        tradingPartnerForm.setMaster("0");
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        TradingPartner tradingPartner = new TradingPartnerBC().addNewTradingPartner(tradingPartnerForm, loginUser);
        if (null != tradingPartner) {
            CustomerAddress customerAddress = new CustomerAddress();
            customerAddress.setAccountName(tradingPartner.getAccountName().toUpperCase());
            customerAddress.setAccountNo(tradingPartner.getAccountno().toUpperCase());
            customerAddress.setAccounttype(tradingPartner.getAcctType());
            customerAddress.setAddress1(tradingPartnerForm.getAddress1().toUpperCase());
            if (CommonUtils.isNotEmpty(tradingPartnerForm.getUnLocCode())) {
                UnLocation unLocation = new UnLocationDAO().getUnlocation(tradingPartnerForm.getUnLocCode());
                if (unLocation != null) {
                    GenericCode genericCode = (GenericCode) unLocation.getCountryId();
                    customerAddress.setCuntry(genericCode);
                    customerAddress.setCity1(unLocation);
                }
                customerAddress.setCity2(tradingPartnerForm.getCity().toUpperCase());
                customerAddress.setUnLocCode(tradingPartnerForm.getUnLocCode().toUpperCase());
            }
            if (tradingPartnerForm.getState() != null && !tradingPartnerForm.getState().equals("")) {
                customerAddress.setState(tradingPartnerForm.getState().toUpperCase());
            }
            if (tradingPartnerForm.getPhone() != null && !tradingPartnerForm.getPhone().equals("")) {
                customerAddress.setPhone(tradingPartnerForm.getPhone());
            }
            if (tradingPartnerForm.getZip() != null && !tradingPartnerForm.getZip().equals("")) {
                customerAddress.setZip(tradingPartnerForm.getZip());
            }
            if (tradingPartnerForm.getFax() != null && !tradingPartnerForm.getFax().equals("")) {
                customerAddress.setFax(tradingPartnerForm.getFax());
            }
            if (tradingPartnerForm.getEmail() != null && !tradingPartnerForm.getEmail().equals("")) {
                customerAddress.setEmail1(tradingPartnerForm.getEmail());
            }
            customerAddress.setCheckAddress(false);
            customerAddress.setPrimary("ON");
            customerAddress.setType("mb");
            customerAddress.setUpdateBy(loginUser.getLoginName());
            new CustAddressDAO().save(customerAddress);
            if (CommonUtils.isNotEmpty(tradingPartnerForm.getSalesCode())) {
                tradingPartner = new TradingPartnerDAO().findById(tradingPartner.getAccountno());
                GeneralInformation generalInformation = tradingPartner.getGeneralInfo();
                if (null == generalInformation) {
                    generalInformation = new GeneralInformation();
                    generalInformation.setAccountNo(tradingPartner.getAccountno());
                }
                Integer codeTypeId = new CodetypeDAO().getCodeTypeId("Sales Code");
                GenericCode salesCode = new GenericCodeDAO().getGenericCodeByCode(tradingPartnerForm.getSalesCode(), codeTypeId);
                generalInformation.setSalesCode(salesCode.getCode());
                generalInformation.setSalesCodeName(salesCode.getCodedesc());
                generalInformation.setSalescode(salesCode);
                new GeneralInformationDAO().save(generalInformation);
            }
        }
        if (tradingPartnerForm.getButtonValue().equalsIgnoreCase("LCL_EXPORT")) {
            PrintWriter out = null;
            try {
                out = response.getWriter();
                response.setContentType("application/json");
                Map<String, String> result = new HashMap<String, String>();
                result.put("accountNo", tradingPartner != null ? tradingPartner.getAccountno(): "");
                Gson gson = new Gson();
                out.print(gson.toJson(result));
                return null;
            } catch (Exception e) {
                throw e;
            } finally {
                if (null != out) {
                    out.flush();
                    out.close();
                }
            }
        } else {
            request.setAttribute("tradingPartnerForm", tradingPartnerForm);
            request.setAttribute("tradingPartner", tradingPartner);
            return mapping.findForward("sucess");
        }
    }
}

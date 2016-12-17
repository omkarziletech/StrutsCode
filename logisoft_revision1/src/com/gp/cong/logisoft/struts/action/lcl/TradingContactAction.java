/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.GeneralInformation;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.domain.lcl.LclQuote;
import com.gp.cong.logisoft.hibernate.dao.GeneralInformationDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLContactDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLQuoteDAO;
import com.gp.cvst.logisoft.struts.form.lcl.TradingContactForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Administrator
 */
public class TradingContactAction extends LogiwareDispatchAction {

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TradingContactForm tradingContactForm = (TradingContactForm) form;
        if (tradingContactForm.getFileType() != null && CommonUtils.isEqual(tradingContactForm.getFileType(), "Booking")) {
            LclBooking lclBooking = new LCLBookingDAO().findById(Long.parseLong(tradingContactForm.getFileId()));
            if(lclBooking.getConsContact().isSendNotification()){
                tradingContactForm.setSendNotification("on");
            }else{
                tradingContactForm.setSendNotification("off");
            }
            request.setAttribute("consigneeFlag", lclBooking.getConsContact().isSendNotification());
            if ("Shipper".equalsIgnoreCase(tradingContactForm.getVendorType()) && lclBooking.getShipReference() != null) {
                tradingContactForm.setClientReference(lclBooking.getShipReference());
            } else if ("Consignee".equalsIgnoreCase(tradingContactForm.getVendorType()) && lclBooking.getConsReference() != null) {
                tradingContactForm.setClientReference(lclBooking.getConsReference());
            } else if ("Notify".equalsIgnoreCase(tradingContactForm.getVendorType()) && lclBooking.getNotyReference() != null) {
                tradingContactForm.setClientReference(lclBooking.getNotyReference());
            } else if ("Notify2".equalsIgnoreCase(tradingContactForm.getVendorType()) && lclBooking.getNoty2Reference() != null) {
                tradingContactForm.setClientReference(lclBooking.getNoty2Reference());
            }
        }
        if (tradingContactForm.getFileType() != null && CommonUtils.isEqual(tradingContactForm.getFileType(), "Quote")) {
            LclQuote lclQuote = new LCLQuoteDAO().findById(Long.parseLong(tradingContactForm.getFileId()));
            if ("Shipper".equalsIgnoreCase(tradingContactForm.getVendorType()) && lclQuote.getShipReference() != null) {
                tradingContactForm.setClientReference(lclQuote.getShipReference());
            } else if ("Consignee".equalsIgnoreCase(tradingContactForm.getVendorType()) && lclQuote.getConsReference() != null) {
                tradingContactForm.setClientReference(lclQuote.getConsReference());
            } else if ("Notify".equalsIgnoreCase(tradingContactForm.getVendorType()) && lclQuote.getNotyReference() != null) {
                tradingContactForm.setClientReference(lclQuote.getNotyReference());
            }
        }
        GeneralInformationDAO generalDAO = new GeneralInformationDAO();
        if (CommonUtils.isNotEmpty(tradingContactForm.getAcctNo())) {
            GeneralInformation generalInformation = generalDAO.getGeneralInformationByAccountNumber(tradingContactForm.getAcctNo());
            if (generalInformation != null && generalInformation.getPoa() != null) {
                request.setAttribute("poa", generalInformation.getPoa());
            }
        }
        request.setAttribute("tradingContactForm", tradingContactForm);
        return mapping.findForward("display");
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TradingContactForm tradingContactForm = (TradingContactForm) form;
        LCLContactDAO lclContactDAO = new LCLContactDAO();
        LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
        LCLQuoteDAO lclQuoteDAO = new LCLQuoteDAO();
        if (tradingContactForm.getFileId() != null) {
            LclContact contact = null;
            if (tradingContactForm.getFileType() != null && CommonUtils.isEqual(tradingContactForm.getFileType(), "Booking")) {
                LclBooking lclBooking = lclBookingDAO.findById(Long.parseLong(tradingContactForm.getFileId()));
                if ("Shipper".equalsIgnoreCase(tradingContactForm.getVendorType())) {
                    contact = lclBooking.getShipContact();
                    if (CommonUtils.isNotEmpty(tradingContactForm.getClientReference())) {
                        lclBooking.setShipReference(tradingContactForm.getClientReference().toUpperCase());
                    }
                } else if ("Consignee".equalsIgnoreCase(tradingContactForm.getVendorType())) {
                    contact = lclBooking.getConsContact();
                    if ("on".equalsIgnoreCase(tradingContactForm.getSendNotification())) {
                        contact.setSendNotification(true);
                    } else {
                        contact.setSendNotification(false);
                    }
                    if (CommonUtils.isNotEmpty(tradingContactForm.getClientReference())) {
                        lclBooking.setConsReference(tradingContactForm.getClientReference().toUpperCase());
                    }
                } else if ("Notify".equalsIgnoreCase(tradingContactForm.getVendorType())) {
                    contact = lclBooking.getNotyContact();
                    if (CommonUtils.isNotEmpty(tradingContactForm.getClientReference())) {
                        lclBooking.setNotyReference(tradingContactForm.getClientReference().toUpperCase());
                    }
                } else if ("Notify2".equalsIgnoreCase(tradingContactForm.getVendorType())) {
                    contact = lclBooking.getNotify2Contact();
                    if (CommonUtils.isNotEmpty(tradingContactForm.getClientReference())) {
                        lclBooking.setNoty2Reference(tradingContactForm.getClientReference().toUpperCase());
                    }
                }
                lclBookingDAO.update(lclBooking);
            }else if (tradingContactForm.getFileType() != null && CommonUtils.isEqual(tradingContactForm.getFileType(), "Quote")) {
                LclQuote lclQuote = lclQuoteDAO.findById(Long.parseLong(tradingContactForm.getFileId()));
                if ("Shipper".equalsIgnoreCase(tradingContactForm.getVendorType())) {
                    if (CommonUtils.isNotEmpty(tradingContactForm.getClientReference())) {
                        lclQuote.setShipReference(tradingContactForm.getClientReference().toUpperCase());
                    }
                    contact = lclQuote.getShipContact();
                } else if ("Consignee".equalsIgnoreCase(tradingContactForm.getVendorType())) {
                    contact = lclQuote.getConsContact();
                    if (CommonUtils.isNotEmpty(tradingContactForm.getClientReference())) {
                        lclQuote.setConsReference(tradingContactForm.getClientReference().toUpperCase());
                    }
                } else if ("Notify".equalsIgnoreCase(tradingContactForm.getVendorType())) {
                    contact = lclQuote.getNotyContact();
                    if (CommonUtils.isNotEmpty(tradingContactForm.getClientReference())) {
                        lclQuote.setNotyReference(tradingContactForm.getClientReference().toUpperCase());
                    }
                }
            }
            contact.setAddress(tradingContactForm.getAddress().toUpperCase());
            contact.setCity(tradingContactForm.getCity().toUpperCase());
            contact.setState(tradingContactForm.getState().toUpperCase());
            contact.setZip(tradingContactForm.getZip());
            contact.setCountry(tradingContactForm.getCountry().toUpperCase());
            contact.setPhone1(tradingContactForm.getPhone());
            contact.setFax1(tradingContactForm.getFax());
            contact.setEmail1(tradingContactForm.getEmail());
            contact.setSalesPersonCode(tradingContactForm.getSalesPersonCode());
            lclContactDAO.saveOrUpdate(contact);
        }
        return mapping.findForward("display");

    }
}

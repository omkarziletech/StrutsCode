package com.gp.cong.logisoft.bc.tradingpartner;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.CustomerAccounting;
import com.gp.cong.logisoft.domain.CustomerAddress;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.CustomerAccountingDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.struts.form.TradingPartnerForm;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import java.util.Date;

public class AddressBC {

    public TradingPartner saveCustomerAddress(TradingPartnerForm tradingPartnerForm, User user) throws Exception {
        CustomerAddress customerAddress = new CustomerAddress(tradingPartnerForm);
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        TradingPartner tradingPartner = tradingPartnerDAO.findById(tradingPartnerForm.getTradingPartnerId());
        if (tradingPartnerForm.getCity() != null && !tradingPartnerForm.getCity().equals("")) {
            UnLocationDAO unLocationDAO = new UnLocationDAO();
            String city = tradingPartnerForm.getCity();
            String unLoc = tradingPartnerForm.getUnLocCode();
            if (CommonUtils.isNotEmpty(unLoc)) {
                UnLocation unLocation = unLocationDAO.getUnlocation(unLoc);
                if (null != unLocation) {
                    GenericCode genericCode = (GenericCode) unLocation.getCountryId();
                    customerAddress.setCity1(unLocation);
                    customerAddress.setCity2((null != city) ? city.toUpperCase() : city);
                    customerAddress.setCuntry(genericCode);
                }
            } else if (CommonUtils.isNotEmpty(tradingPartnerForm.getCountryCode())) {
                GenericCode genericCode = new GenericCodeDAO().findById(tradingPartnerForm.getCountryCode());
                customerAddress.setCuntry(genericCode);
            }
        }
        customerAddress.setMasteracctno((null != tradingPartner.getMaster()) ? tradingPartner.getMaster().toUpperCase() : tradingPartner.getMaster());
        customerAddress.setType(tradingPartner.getType());
        if (tradingPartnerForm.getPhone() != null) {
            customerAddress.setPhone((null != tradingPartnerForm.getPhone()) ? tradingPartnerForm.getPhone().toUpperCase() : tradingPartnerForm.getPhone());
        }
        if (tradingPartnerForm.getFax() != null) {
            customerAddress.setFax((null != tradingPartnerForm.getFax()) ? tradingPartnerForm.getFax().toUpperCase() : tradingPartnerForm.getFax());
        }
        if (tradingPartnerForm.getEmail1() != null) {
            customerAddress.setEmail1((null != tradingPartnerForm.getEmail1()) ? tradingPartnerForm.getEmail1().toUpperCase() : tradingPartnerForm.getEmail1());
        }
        if (tradingPartner.getMaster() != null) {
            customerAddress.setMasteracctno((null != tradingPartner.getMaster()) ? tradingPartner.getMaster().toUpperCase() : tradingPartner.getMaster());
        }
        if (tradingPartner.getAcctType() != null) {
            customerAddress.setAccounttype((null != tradingPartner.getAcctType()) ? tradingPartner.getAcctType().toUpperCase() : tradingPartner.getAcctType());
        }
        customerAddress.setSubType(tradingPartner.getSubType());
        customerAddress.setUpdateBy(user.getLoginName());
        if (CommonUtils.isNotEmpty(tradingPartner.getCustomerAddressSet())) {
            for (CustomerAddress customerAddressOld : tradingPartner.getCustomerAddressSet()) {
                if (CommonUtils.isEqualIgnoreCase(customerAddress.getPrimary(), CommonConstants.ON)) {
                    customerAddressOld.setPrimary(CommonConstants.OFF.toUpperCase());
                }
                if (customerAddress.isCheckAddress()) {
                    customerAddressOld.setCheckAddress(false);
                }
            }
        } else {
            customerAddress.setPrimary(CommonConstants.ON.toUpperCase());
            customerAddress.setCheckAddress(true);
            new CustAddressDAO().save(customerAddress);
        }
        tradingPartner.getCustomerAddressSet().add(customerAddress);
        tradingPartner.setEnterDate(new Date());
        tradingPartnerDAO.update(tradingPartner, null);
        CustomerAccountingDAO arConfigDAO = new CustomerAccountingDAO();
        CustomerAccounting arConfig = arConfigDAO.findByProperty("accountNo", tradingPartner.getAccountno());
        if (null != arConfig && CommonUtils.isEmpty(arConfig.getCustAddressId())) {
            arConfig.setCustAddressId(customerAddress.getId());
            arConfigDAO.update(arConfig);
        }
        return tradingPartner;
    }

    public TradingPartner deleteCustomerAddress(TradingPartnerForm tradingPartnerForm) throws Exception {
        CustAddressDAO custAddressDAO = new CustAddressDAO();
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        CustomerAccountingDAO arConfigDAO = new CustomerAccountingDAO();
        CustomerAddress customerAddress = custAddressDAO.findByID(tradingPartnerForm.getIndex());
        CustomerAccounting arConfig = arConfigDAO.findByProperty("custAddressId", customerAddress.getId());
        if (null != arConfig) {
            arConfig.setCustAddressId(null);
            arConfigDAO.update(arConfig);
        }
        custAddressDAO.deleteByCustomerAddress(customerAddress);
        custAddressDAO.getCurrentSession().flush();
        TradingPartner tradingPartner = tradingPartnerDAO.findById(tradingPartnerForm.getTradingPartnerId());
        tradingPartner.setEnterDate(new Date());
        tradingPartnerDAO.update(tradingPartner, null);
        return tradingPartner;
    }

    public CustomerAddress editCustomerAddress(TradingPartnerForm tradingPartnerForm) throws Exception {
        CustAddressDAO custAddressDAO = new CustAddressDAO();
        CustomerAddress customerAddress = custAddressDAO.findByID(tradingPartnerForm.getIndex());
        return customerAddress;
    }

    public TradingPartner updateCustomerAddress(TradingPartnerForm tradingPartnerForm, User loginUser) throws Exception {
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        TradingPartner tradingPartnerNew = tradingPartnerDAO.findById(tradingPartnerForm.getTradingPartnerId());
        TradingPartner tradingPartner = new TradingPartner();
        boolean updated = false;
        if (tradingPartnerForm.getTradingPartnerId() != null) {
            tradingPartner = tradingPartnerDAO.findById(tradingPartnerForm.getTradingPartnerId());
            if (CommonUtils.isNotEmpty(tradingPartner.getCustomerAddressSet())) {
                for (CustomerAddress customerAddress : tradingPartner.getCustomerAddressSet()) {
                    if (CommonUtils.isEqual(Integer.parseInt(tradingPartnerForm.getIndex()), customerAddress.getId())) {
                        if (CommonUtils.isNotEqual(tradingPartnerForm.getCoName(), customerAddress.getCoName())) {
                            updated = true;
                        } else if (CommonUtils.isNotEqual(tradingPartnerForm.getState(), customerAddress.getState())) {
                            updated = true;
                        } else if (CommonUtils.isNotEqual(tradingPartnerForm.getZip(), customerAddress.getZip())) {
                            updated = true;
                        } else if (CommonUtils.isNotEqual(tradingPartnerForm.getEmail1(), customerAddress.getEmail1())) {
                            updated = true;
                        } else if (CommonUtils.isNotEqual(tradingPartnerForm.getEmail2(), customerAddress.getEmail2())) {
                            updated = true;
                        } else if (CommonUtils.isNotEqual(tradingPartnerForm.getPhone(), customerAddress.getPhone())) {
                            updated = true;
                        } else if (CommonUtils.isNotEqual(tradingPartnerForm.getFax(), customerAddress.getFax())) {
                            updated = true;
                        } else if (CommonUtils.isNotEqual(tradingPartnerForm.getContactName(), customerAddress.getContactName())) {
                            updated = true;
                        } else if (CommonUtils.isNotEqual(tradingPartnerForm.getAddress1(), customerAddress.getAddress1())) {
                            updated = true;
                        } else if (CommonUtils.isNotEqual(tradingPartnerForm.getCity(), customerAddress.getCity2())) {
                            updated = true;
                        } else if (CommonUtils.isNotEqual(tradingPartnerForm.getUnLocCode(), customerAddress.getUnLocCode())) {
                            updated = true;
                        }
                        customerAddress.setCoName(null != tradingPartnerForm.getCoName() ? tradingPartnerForm.getCoName().toUpperCase() : tradingPartnerForm.getCoName());
                        customerAddress.setAddress1(null != tradingPartnerForm.getAddress1() ? tradingPartnerForm.getAddress1().toUpperCase() : tradingPartnerForm.getAddress1());
                        customerAddress.setAddress2(null != tradingPartnerForm.getAddress2() ? tradingPartnerForm.getAddress2().toUpperCase() : tradingPartnerForm.getAddress2());
                        customerAddress.setState(null != tradingPartnerForm.getState() ? tradingPartnerForm.getState().toUpperCase() : tradingPartnerForm.getState());
                        customerAddress.setPhone(null != tradingPartnerForm.getPhone() ? tradingPartnerForm.getPhone().toUpperCase() : tradingPartnerForm.getPhone());
                        customerAddress.setContactName(null != tradingPartnerForm.getContactName() ? tradingPartnerForm.getContactName().toUpperCase() : tradingPartnerForm.getContactName());
                        customerAddress.setZip(null != tradingPartnerForm.getZip() ? tradingPartnerForm.getZip().toUpperCase() : tradingPartnerForm.getZip());
                        customerAddress.setFax(null != tradingPartnerForm.getFax() ? tradingPartnerForm.getFax().toUpperCase() : tradingPartnerForm.getFax());
                        customerAddress.setEmail1(null != tradingPartnerForm.getEmail1() ? tradingPartnerForm.getEmail1().toUpperCase() : tradingPartnerForm.getEmail1());
                        customerAddress.setEmail2(null != tradingPartnerForm.getEmail2() ? tradingPartnerForm.getEmail2().toUpperCase() : tradingPartnerForm.getEmail2());
                        customerAddress.setExtension(null != tradingPartnerForm.getExtension() ? tradingPartnerForm.getExtension().toUpperCase() : tradingPartnerForm.getExtension());
                        customerAddress.setPortName(null != tradingPartnerForm.getPortName() ? tradingPartnerForm.getPortName().toUpperCase() : tradingPartnerForm.getPortName());
                        customerAddress.setSchNum(null != tradingPartnerForm.getSchNum() ? tradingPartnerForm.getSchNum().toUpperCase() : tradingPartnerForm.getSchNum());
                        customerAddress.setCheckAddress(tradingPartnerForm.isCheckAddress());
                        customerAddress.setPrimary(null != tradingPartnerForm.getPrimary() ? tradingPartnerForm.getPrimary().toUpperCase() : tradingPartnerForm.getPrimary());
                        customerAddress.setNotifyParty(null != tradingPartnerForm.getNotifyParty() ? tradingPartnerForm.getNotifyParty().toUpperCase() : tradingPartnerForm.getNotifyParty());
                        customerAddress.setUnLocCode(null != tradingPartnerForm.getUnLocCode() ? tradingPartnerForm.getUnLocCode().toUpperCase() : tradingPartnerForm.getUnLocCode());
                        if (null != loginUser) {
                            customerAddress.setUpdateBy(loginUser.getLoginName());
                        }
                        UnLocation city = null;
                        GenericCode country = null;
                        if (tradingPartnerForm.getCity() != null && !tradingPartnerForm.getCity().equals("")) {
                            UnLocationDAO unLocationDAO = new UnLocationDAO();
                            String cityName = tradingPartnerForm.getCity();
                            String unLoc = tradingPartnerForm.getUnLocCode();
                            if (CommonUtils.isNotEmpty(unLoc)) {
                                city = unLocationDAO.getUnlocation(unLoc);
                                if (city != null) {
                                    country = (GenericCode) city.getCountryId();
                                    customerAddress.setCuntry(country);
                                }
                                customerAddress.setCity1(city);
                                customerAddress.setCity2((null != cityName) ? cityName.toUpperCase().toUpperCase() : cityName);
                                customerAddress.setMasteracctno((null != tradingPartner.getMaster()) ? tradingPartner.getMaster().toUpperCase() : tradingPartner.getMaster());
                            } else if (CommonUtils.isNotEmpty(tradingPartnerForm.getCountryCode())) {
                                country = new GenericCodeDAO().findById(tradingPartnerForm.getCountryCode());
                                customerAddress.setCuntry(country);
                            }
                        }
                        if (!updated) {
                            if (null != city && null != customerAddress.getCity1() && CommonUtils.isNotEqual(city.getUnLocationName(), customerAddress.getCity1().getUnLocationName())) {
                                updated = true;
                            } else if ((null != city && null == customerAddress.getCity1()) || (null == city && null != customerAddress.getCity1())) {
                                updated = true;
                            } else if (null != country && null != customerAddress.getCuntry() && CommonUtils.isNotEqual(country.getCodedesc(), customerAddress.getCuntry().getCodedesc())) {
                                updated = true;
                            } else if ((null != country && null == customerAddress.getCuntry()) || (null == country && null != customerAddress.getCuntry())) {
                                updated = true;
                            }
                        }

                        customerAddress.setType((null != tradingPartnerNew.getType()) ? tradingPartnerNew.getType().toUpperCase() : tradingPartnerNew.getType());
                        if (tradingPartner.getMaster() != null) {
                            customerAddress.setMasteracctno((null != tradingPartner.getMaster()) ? tradingPartner.getMaster().toUpperCase() : tradingPartner.getMaster());
                        }
                        if (tradingPartner.getAcctType() != null) {
                            customerAddress.setAccounttype((null != tradingPartner.getAcctType()) ? tradingPartner.getAcctType().toUpperCase() : tradingPartner.getAcctType());
                        }
                        customerAddress.setSubType(tradingPartner.getSubType());
                        CustomerAccountingDAO arConfigDAO = new CustomerAccountingDAO();
                        CustomerAccounting arConfig = arConfigDAO.findByProperty("accountNo", tradingPartner.getAccountno());
                        if (null != arConfig && CommonUtils.isEmpty(arConfig.getCustAddressId())) {
                            arConfig.setCustAddressId(customerAddress.getId());
                            arConfigDAO.update(arConfig);
                        }
                    } else {
                        if (CommonUtils.isEqualIgnoreCase(tradingPartnerForm.getPrimary(), CommonConstants.ON)) {
                            customerAddress.setPrimary(CommonConstants.OFF.toUpperCase());
                        }
                        if (tradingPartnerForm.isCheckAddress()) {
                            customerAddress.setCheckAddress(false);
                        }
                    }
                }
            }
            if(updated){
                tradingPartner.setEnterDate(new Date());
            }
            tradingPartnerDAO.update(tradingPartner, null);
        }
        return tradingPartner;
    }

    public TradingPartner getCustomerDetails(TradingPartnerForm tradingPartnerForm) throws Exception {
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        TradingPartner tradingPartner = tradingPartnerDAO.findById(tradingPartnerForm.getTradingPartnerId());
        return tradingPartner;
    }
}

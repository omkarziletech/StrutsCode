package com.gp.cvst.logisoft.struts.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.GenerateFileNumber;
import com.gp.cong.logisoft.bc.fcl.QuotationBC;
import com.gp.cong.logisoft.bc.fcl.QuotationConstants;
import com.gp.cong.logisoft.bc.scheduler.ProcessInfoBC;
import com.gp.cong.logisoft.domain.FclBuy;
import com.gp.cong.logisoft.domain.FclBuyCost;
import com.gp.cong.logisoft.domain.FclBuyCostTypeRates;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.FclBuyCostDAO;
import com.gp.cong.logisoft.hibernate.dao.FclBuyDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cvst.logisoft.beans.MultiChargesOrderBean;
import com.gp.cvst.logisoft.beans.MultiQuoteBean;
import com.gp.cvst.logisoft.beans.MultiQuoteChargesBean;
import com.gp.cvst.logisoft.beans.MultiQuoteComparator;
import com.gp.cvst.logisoft.domain.Charges;
import com.gp.cvst.logisoft.domain.MultiQuote;
import com.gp.cvst.logisoft.domain.MultiQuoteCharges;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.hibernate.dao.ChargesDAO;
import com.gp.cvst.logisoft.hibernate.dao.MultiQuoteChargesDao;
import com.gp.cvst.logisoft.hibernate.dao.MultiQuoteDao;
import com.gp.cvst.logisoft.hibernate.dao.QuotationDAO;
import com.gp.cvst.logisoft.struts.form.MultiQuotesForm;
import com.logiware.fcl.form.SessionForm;
import com.logiware.hibernate.domain.FclBuyOtherCommodity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;
import org.apache.struts.util.MessageResources;

/**
 *
 * @author NambuRajasekar.M
 */
public class MultiQuotationAction extends DispatchAction {

    PortsDAO portsDAO = new PortsDAO();
    GenericCodeDAO genericCodeDAO = new GenericCodeDAO();

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        MultiQuotesForm multiQuotesForm = new MultiQuotesForm();
        multiQuotesForm.setCommcode("006100");
        multiQuotesForm.setImportFlag(false);
        HttpSession session = request.getSession();
        if (session.getAttribute("loginuser") != null) {
            User user = (User) session.getAttribute("loginuser");
            request.setAttribute("quoteBy", user.getLoginName().toUpperCase());
            multiQuotesForm.setQuoteBy(user.getLoginName().toUpperCase());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        request.setAttribute("quoteDate", sdf.format(new Date()));
        request.setAttribute("quotationNo", "NEW");
        request.setAttribute("importFlag", false);
        request.setAttribute(QuotationConstants.QUOTATIONFORM, multiQuotesForm);
        return mapping.findForward("multiQuote");
    }

    public ActionForward getRates(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        MultiQuotesForm multiQuotesForm = (MultiQuotesForm) form;
        Quotation quotation = null;

        if (multiQuotesForm.getQuoteId() == 0) {
            quotation = new MultiQuoteDao().saveForm(multiQuotesForm);
        } else {
            quotation = new QuotationDAO().findById(multiQuotesForm.getQuoteId());
        }
        MessageResources messageResources = getResources(request);
        this.getFclBuyRates(quotation, multiQuotesForm, messageResources, request);
        multiQuotesForm.setChargeFlag(true);
        multiQuotesForm.setQuoteId(quotation.getQuoteId());
        request.setAttribute("quotationNo", multiQuotesForm.getFileNo());
        request.setAttribute("quoteBy", multiQuotesForm.getQuoteBy());
        request.setAttribute("quoteDate", multiQuotesForm.getQuoteDate());
        multiQuotesForm.setSsLine(null);
        multiQuotesForm.setSelectionInsert(null);
        multiQuotesForm.setUnitTypes(null);
        request.setAttribute(QuotationConstants.QUOTATIONFORM, multiQuotesForm);
        return mapping.findForward("multiQuote");
    }

    public void getFclBuyRates(Quotation quotation, MultiQuotesForm quotesForm, MessageResources messageResources, HttpServletRequest request) throws Exception {
        List otherCommList = new FclBuyCostDAO().getOtherCommodity(quotesForm.getCommcode(), "markup1");
        List<FclBuyOtherCommodity> otherCommodityList = new ArrayList<FclBuyOtherCommodity>();
//        List comList = genericCodeDAO.findForGenericCode(quotesForm.getCommcode());
//        GenericCode genObj = (GenericCode) comList.get(0);
//        commodity = genObj.getId().toString();
        if (CommonUtils.isNotEmpty(otherCommList)) {
            for (Object otherComm : otherCommList) {
                Object[] object = (Object[]) otherComm;
//                commodity = null != object[3] ? object[3].toString() : "";
                FclBuyOtherCommodity fclBuyOtherCommodity = new FclBuyOtherCommodity();
                fclBuyOtherCommodity.setAddSub(null != object[0] ? object[0].toString() : "");
                fclBuyOtherCommodity.setMarkUp(null != object[1] ? Double.parseDouble(object[1].toString()) : 0);
                fclBuyOtherCommodity.setCostCode(null != object[2] ? genericCodeDAO.getFieldByCodeAndCodetypeId("Cost Code", object[2].toString(), "codedesc") : "");
                fclBuyOtherCommodity.setBaseCommodityCode(null != object[3] ? object[3].toString() : "");
                fclBuyOtherCommodity.setMarkUp2(null != object[4] ? Double.parseDouble(object[4].toString()) : 0);
                otherCommodityList.add(fclBuyOtherCommodity);
            }
        }

        // split unitTypes
        String unitType = quotesForm.getUnitTypes();
        String unitTypes[] = unitType.split(",");
        // Split polpodCarrierCode
        String totalCarrierPolPod = quotesForm.getSsLine();
        String[] polPodCarrier = totalCarrierPolPod.split(",");

        for (String polpodcarrierCode : polPodCarrier) {
            String[] str = StringUtils.split(polpodcarrierCode, ";");//Ex:14575;48810;ANTMAR0008;1703
            MultiQuote multiQuote = new MultiQuote(); // multiQuote Object       
            multiQuote.setQuotation(quotation);
            multiQuote.setOriginCode(str[0]);
            multiQuote.setDestinationCode(str[1]);
            multiQuote.setCarrierNo(str[2]);
            multiQuote.setCommodity(str[3]);
            multiQuote.setBulletRates(quotesForm.getBulletRatesCheck());
            multiQuote.setHazmat(quotesForm.getHazmat());
            this.getUnLocation(multiQuote);
            new MultiQuoteDao().save(multiQuote);

//          --------------GETTING RATESLIST------
            List ratesList = new FclBuyDAO().findForSearchFclBuyRatesForCompressList2(multiQuote.getOriginCode(), multiQuote.getDestinationCode(), multiQuote.getCommodity(), multiQuote.getCarrierNo());
            for (Object rates : ratesList) {
                FclBuy fclBuy = (FclBuy) rates;
                List<MultiQuoteCharges> chargesList = new ArrayList<MultiQuoteCharges>();
                TreeSet<String> tree = new TreeSet<String>();
                if (fclBuy.getFclBuyCostsSet() != null) {
                    Iterator iter = fclBuy.getFclBuyCostsSet().iterator();
                    while (iter.hasNext()) {
                        FclBuyCost fclBuyCost = (FclBuyCost) iter.next();
                        if (fclBuyCost.getFclBuyUnitTypesSet() != null) {
                            Iterator iterator = fclBuyCost.getFclBuyUnitTypesSet().iterator();
                            while (iterator.hasNext()) {
                                FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iterator.next();
                                MultiQuoteCharges charges = new MultiQuoteCharges();
                                charges.setMultiQuote(multiQuote);
                                charges.setNumber("1");
                                if (fclBuyCost.getCostId() != null) {
//                                    charges.setChgCode(fclBuyCost.getCostId());
                                    charges.setChargeCode(fclBuyCost.getCostId().getCode());
                                    charges.setChargeCodeDesc(fclBuyCost.getCostId().getCodedesc());
                                }

                                if (fclBuyCost.getContType() != null) {
                                    quotesForm.setCosttype(fclBuyCost.getContType());
                                    charges.setCostType(fclBuyCost.getContType().getCodedesc());
                                    if (fclBuyCost.getContType().getCodedesc().equalsIgnoreCase(messageResources.getMessage("FlatRatePerConatinerSize"))) {
                                        charges.setAmount(fclBuyCostTypeRates.getActiveAmt());
                                    } else {
                                        charges.setAmount(fclBuyCostTypeRates.getRatAmount());
                                    }
                                    charges.setMinimum(fclBuyCostTypeRates.getMinimumAmt());
                                }
                                double markup = 0.00;
                                if (CommonUtils.isNotEmpty(otherCommodityList)) {
                                    boolean noMarkup = true;
                                    for (FclBuyOtherCommodity fclBuyOtherCommodity : otherCommodityList) {
                                        if (CommonUtils.isEqualIgnoreCase(charges.getChargeCode(), fclBuyOtherCommodity.getCostCode())) {
                                            if ("A".equalsIgnoreCase(fclBuyOtherCommodity.getAddSub())) {
                                                Ports port = portsDAO.findById(fclBuy.getDestinationPort().getId());
                                                if (null != port && null != port.getRegioncode() && "Y".equalsIgnoreCase(port.getRegioncode().getField3())) {
                                                    markup += fclBuyCostTypeRates.getMarkup() + fclBuyOtherCommodity.getMarkUp2();
                                                } else {
                                                    markup += fclBuyCostTypeRates.getMarkup() + fclBuyOtherCommodity.getMarkUp();
                                                }
                                            } else {
                                                Ports port = portsDAO.findById(fclBuy.getDestinationPort().getId());
                                                if (null != port && null != port.getRegioncode() && "Y".equalsIgnoreCase(port.getRegioncode().getField3())) {
                                                    markup += fclBuyCostTypeRates.getMarkup() - fclBuyOtherCommodity.getMarkUp2();
                                                } else {
                                                    markup += fclBuyCostTypeRates.getMarkup() - fclBuyOtherCommodity.getMarkUp();
                                                }
                                            }
                                            noMarkup = false;
                                        }
                                    }
                                    if (noMarkup) {
                                        markup += fclBuyCostTypeRates.getMarkup();
                                    }
                                } else {
                                    markup += fclBuyCostTypeRates.getMarkup();
                                }
                                charges.setMarkUp(markup);
                                charges.setEffectiveDate(new Date());
                                if (fclBuy.getSslineNo() != null) {
                                    charges.setAccountName(fclBuy.getSslineNo().getAccountName());
                                    charges.setAccountNo(fclBuy.getSslineNo().getAccountno());
                                }
                                if (fclBuyCostTypeRates.getCurrency() != null) {
//                                charges.setCurrency1(fclBuyCostTypeRates.getCurrency());
                                    charges.setCurrency(fclBuyCostTypeRates.getCurrency().getCodedesc());
                                }
                                charges.setStandardCharge("Y");//Default Value

                                if (fclBuyCostTypeRates.getUnitType() != null) {
                                    for (String unitType1 : unitTypes) {
                                        if (unitType1.equalsIgnoreCase(fclBuyCostTypeRates.getUnitType().getCodedesc())) {
                                            charges.setUnitNo(fclBuyCostTypeRates.getUnitType().getCodedesc());
                                            charges.setUnitType(fclBuyCostTypeRates.getUnitType().getId().toString());
                                            tree.add(fclBuyCostTypeRates.getUnitType().getCodedesc());
                                            if (quotesForm.getHazmat().equalsIgnoreCase("N") && !fclBuyCost.getCostId().getCode().equalsIgnoreCase("HAZFEE")) {// NO HAZARDOUS
                                                chargesList.add(charges);
                                            } else if (quotesForm.getHazmat().equalsIgnoreCase("Y")) {
                                                chargesList.add(charges);
                                            }
                                        }
                                    }
                                }

                            }

                        }
                    }
                }

                multiQuote.setSelected_Units(tree.toString().replace("[", "").replace("]", "").replace(" ", ""));
                new MultiQuoteDao().update(multiQuote);

                Collections.sort(chargesList, new MultiQuoteComparator());
                if (chargesList.get(0).getChargeCode().equalsIgnoreCase("OCNFRT") || chargesList.get(0).getChargeCode().equalsIgnoreCase("OFIMP")) {
                    for (MultiQuoteCharges charges : chargesList) {
                        new MultiQuoteChargesDao().save(charges);
                    }
                } else {
                    String temp = "";
                    int j = 0;
                    int k = 0;
                    LinkedList linkedList = new LinkedList();
                    List<MultiQuoteCharges> newList = new ArrayList();
                    for (Iterator iterator = chargesList.iterator(); iterator.hasNext();) {
                        MultiQuoteCharges q = (MultiQuoteCharges) iterator.next();
                        if (null != q.getUnitType()) {
                            if (!temp.equals(q.getUnitType())) {
                                k = j;
                            }
                        }
                        if (q.getChargeCode().equals("OCNFRT") || q.getChargeCode().equals("OFIMP")) {
                            linkedList.add(k, q);
                        } else {
                            linkedList.add(q);
                        }
                        if (null != q.getUnitType()) {
                            temp = q.getUnitType();
                        }
                        j++;
                    }
                    newList.addAll(linkedList);
                    for (MultiQuoteCharges Mcharges : newList) {
                        new MultiQuoteChargesDao().save(Mcharges);
                    }
                }
            }
        }// parent Loop

        List<MultiQuoteBean> originDestination = new MultiQuoteDao().getMultiQuoteBean(quotation.getQuoteId());
        List multiQuoteIdList = new MultiQuoteDao().getMultiQuoteId(quotation.getQuoteId());
        List<MultiQuoteChargesBean> fclRatesCharges = new MultiQuoteChargesDao().getChargesExpandList(multiQuoteIdList);
        List<MultiChargesOrderBean> collapseRates = new MultiQuoteChargesDao().getChargesCollapseList(multiQuoteIdList);
        request.setAttribute("collapseRates", collapseRates);
        request.setAttribute("charges", fclRatesCharges);
        request.setAttribute("originDestination", originDestination);
    }

    public ActionForward convertToQuote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        MultiQuotesForm multiQuotesForm = (MultiQuotesForm) form;
        MultiQuoteDao multiQuoteDao = new MultiQuoteDao();
        MultiQuote multiQuote = multiQuoteDao.findById(Long.parseLong(multiQuotesForm.getMultiQuoteRadioId()));
        Quotation quotation = new QuotationDAO().findById(multiQuotesForm.getQuoteId());

        List<MultiQuoteCharges> mChargeList = multiQuote.getMultiQuoteCharges();
        for (MultiQuoteCharges multiCharge : mChargeList) {
            Charges charges = new Charges();
            charges.setQouteId(quotation.getQuoteId());
            charges.setUnitType(multiCharge.getUnitType());
            charges.setChgCode(multiCharge.getChargeCodeDesc());
            charges.setChargeCodeDesc(multiCharge.getChargeCode());
            charges.setAmount(multiCharge.getAmount());
            charges.setNumber(multiCharge.getNumber());
            charges.setCostType(multiCharge.getCostType());
            charges.setMarkUp(multiCharge.getMarkUp());
            charges.setMinimum(multiCharge.getMinimum());
            charges.setRetail(multiCharge.getRetail());
            charges.setCurrecny(multiCharge.getCurrency());
            charges.setEfectiveDate(multiCharge.getEffectiveDate());
            charges.setAccountNo(multiCharge.getAccountNo());
            charges.setAccountName(multiCharge.getAccountName());
            charges.setChargeFlag(multiCharge.getChargeFlag());
            charges.setNewFlag(multiCharge.getNewFlag());
            charges.setComment(multiCharge.getComment());
            charges.setAdjestment(multiCharge.getAdjustment());
            charges.setUpdateBy(multiCharge.getUpdateBy());
            charges.setUpdateOn(multiCharge.getUpdateOn());
            charges.setDefaultCarrier(multiCharge.getDefaultCarrier());
            charges.setOutOfGauge(multiCharge.getOutOfGauge());
            charges.setOutOfGaugeComment(multiCharge.getOutOfGaugeComment());
            charges.setAdjustmentChargeComments(multiCharge.getAdjustmentChargeComments());
            charges.setInclude(multiCharge.getInclude());
            charges.setPrint(multiCharge.getPrint());
            charges.setStandardCharge(multiCharge.getStandardCharge());
            new ChargesDAO().save(charges);

        }
        quotation = multiQuoteDao.convertQuote(multiQuotesForm, multiQuote, quotation, request);// UPDATE Quote before covert
        HttpSession session = request.getSession();
        session.setAttribute("selectedFileNumber", quotation.getFileNo());
        session.setAttribute("screenName", "Quotes");
        return mapping.findForward("closeSearch");
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        MultiQuotesForm multiQuotesForm = (MultiQuotesForm) form;
        String findForward = "";
        HttpSession session = request.getSession();
        Quotation quotation = new QuotationDAO().findById(multiQuotesForm.getQuoteId());
        quotation = new MultiQuoteDao().updateQuotes(multiQuotesForm, quotation, request);//while save and quoteComplete
        if (session.getAttribute("loginuser") != null) {
            User userid = (User) session.getAttribute("loginuser");
            quotation.setUpdateBy(userid.getLoginName().toUpperCase());
        }
        if (quotation.getFileNo() == null) {
            GenerateFileNumber generateFileNumber = new GenerateFileNumber();// wil generate file number
            generateFileNumber.join();// it wil force thread to complete the task before move to next step
            quotation.setFileNo("" + generateFileNumber.getFileNumber());
            quotation.setMultiQuoteFlag("M");
        }
        multiQuotesForm.setMultiQuoteRadioId("");
        if (multiQuotesForm.getFocusValue().equalsIgnoreCase("saveAndExit")) {// FocusValue -->saveAndExit
            boolean importFlag = false;
            if ("I".equalsIgnoreCase(quotation.getFileType())) {
                importFlag = true;
            }
            session.setAttribute("selectedFileNumber", quotation.getFileNo());
            SessionForm oldSearchForm = (SessionForm) session.getAttribute("oldSearchForm");
//             oldSearchForm.setFilterBy("Quotation");
            if (null == oldSearchForm) {
                oldSearchForm = new SessionForm();
                Calendar cal = Calendar.getInstance();
                if (importFlag) {
                    oldSearchForm.setImportFile(true);
                    cal.add(Calendar.MONTH, -6);
                } else {
                    cal.add(Calendar.MONTH, -1);
                }
                oldSearchForm.setFromDate(DateUtils.formatDate(cal.getTime(), "MM/dd/yyyy"));
                oldSearchForm.setToDate(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                oldSearchForm.setFileNumber(quotation.getFileNo());
                session.setAttribute("oldSearchForm", oldSearchForm);
            } else if (CommonUtils.isNotEqualIgnoreEmpty(oldSearchForm.getFileNumber(), quotation.getFileNo())) {
                oldSearchForm.setFileNumber(quotation.getFileNo());
                session.setAttribute("oldSearchForm", oldSearchForm);
            }

            session.setAttribute("screenName", "fileSearch");
            findForward = "closeSearch";
        } else if (multiQuotesForm.getFocusValue().equalsIgnoreCase("quoteComplete")) {
            List<MultiQuoteBean> originDestination = new MultiQuoteDao().getMultiQuoteBean(quotation.getQuoteId());
            List multiQuoteIdList = new MultiQuoteDao().getMultiQuoteId(quotation.getQuoteId());
            List<MultiQuoteChargesBean> fclRatesCharges = new MultiQuoteChargesDao().getChargesExpandList(multiQuoteIdList);
            List<MultiChargesOrderBean> collapseRates = new MultiQuoteChargesDao().getChargesCollapseList(multiQuoteIdList);
            quotation.setMultiQuoteFlag("C");
//            quotation.setMultiQuoteComplete(true);
            multiQuotesForm.setConvertButtonFlag(true);//show convertButton
            multiQuotesForm.setChargeFlag(true);//Show Charges
            multiQuotesForm.setFocusValue(null);//save AND GoBACK
            MessageResources messageResources = getResources(request);
            request.setAttribute("quotationNo", messageResources.getMessage("fileNumberPrefix") + quotation.getFileNo());
            request.setAttribute("quoteBy", quotation.getQuoteBy());
            request.setAttribute("quoteDate", multiQuotesForm.getQuoteDate());
            request.setAttribute(QuotationConstants.QUOTATIONFORM, multiQuotesForm);
            request.setAttribute("charges", fclRatesCharges);
            request.setAttribute("collapseRates", collapseRates);
            request.setAttribute("originDestination", originDestination);
            findForward = "multiQuote";
        } else {
            List<MultiQuoteBean> originDestination = new MultiQuoteDao().getMultiQuoteBean(quotation.getQuoteId());
            List multiQuoteIdList = new MultiQuoteDao().getMultiQuoteId(quotation.getQuoteId());
            List<MultiQuoteChargesBean> fclRatesCharges = new MultiQuoteChargesDao().getChargesExpandList(multiQuoteIdList);
            List<MultiChargesOrderBean> collapseRates = new MultiQuoteChargesDao().getChargesCollapseList(multiQuoteIdList);
            if (quotation.getMultiQuoteFlag().equalsIgnoreCase("C")) {
                multiQuotesForm.setConvertButtonFlag(true);//show convertButton
            }
            multiQuotesForm.setChargeFlag(true);//Show Charges
            multiQuotesForm.setFocusValue(null);//save AND GoBACK
            MessageResources messageResources = getResources(request);
            request.setAttribute("quotationNo", messageResources.getMessage("fileNumberPrefix") + quotation.getFileNo());
            request.setAttribute("quoteBy", quotation.getQuoteBy());
            request.setAttribute("quoteDate", multiQuotesForm.getQuoteDate());
            request.setAttribute(QuotationConstants.QUOTATIONFORM, multiQuotesForm);
            request.setAttribute("collapseRates", collapseRates);
            request.setAttribute("charges", fclRatesCharges);
            request.setAttribute("originDestination", originDestination);
            findForward = "multiQuote";
        }
        new QuotationDAO().update(quotation);
        return mapping.findForward(findForward);
    }

    public ActionForward goBack(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {// for Empty
        MultiQuotesForm quotesForm = (MultiQuotesForm) form;
        HttpSession session = request.getSession();
        MessageResources messageResources = getResources(request);
        User user = null;
        if (session.getAttribute("loginuser") != null) {
            user = (User) session.getAttribute("loginuser");
        }
        if (quotesForm.getQuoteId() != 0) {
            Quotation quotation = new QuotationDAO().findById(quotesForm.getQuoteId());
            boolean importFlag = false;
            if ("I".equalsIgnoreCase(quotation.getFileType())) {
                importFlag = true;
            }
            ProcessInfoBC processInfoBC = new ProcessInfoBC();
            if (CommonFunctions.isNotNull(quotation.getFileNo())) {
                Integer userId = (user != null) ? user.getUserId() : 0;
                processInfoBC.releaseLoack(messageResources.getMessage("lockQuoteModule"), quotation.getFileNo(), userId);
                session.setAttribute("selectedFileNumber", quotation.getFileNo());
                SessionForm oldSearchForm = (SessionForm) session.getAttribute("oldSearchForm");
                if (null == oldSearchForm) {
                    oldSearchForm = new SessionForm();
                    Calendar cal = Calendar.getInstance();
                    if (importFlag) {
                        oldSearchForm.setImportFile(true);
                        cal.add(Calendar.MONTH, -6);
                    } else {
                        cal.add(Calendar.MONTH, -1);
                    }
                    oldSearchForm.setFileNumber(quotation.getFileNo());
                    oldSearchForm.setFromDate(DateUtils.formatDate(cal.getTime(), "MM/dd/yyyy"));
                    oldSearchForm.setToDate(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                    oldSearchForm.setFileNumber(quotation.getFileNo());
                    session.setAttribute("oldSearchForm", oldSearchForm);
                } else if (CommonUtils.isNotEqualIgnoreEmpty(oldSearchForm.getFileNumber(), quotation.getFileNo())) {
                    oldSearchForm.setFileNumber(quotation.getFileNo());
                    session.setAttribute("oldSearchForm", oldSearchForm);
                }
            }
        }
        session.setAttribute("screenName", "fileSearch");
        return mapping.findForward("closeSearch");
    }

    public ActionForward viewFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        MultiQuotesForm multiQuotesForm = new MultiQuotesForm();
        MessageResources messageResources = getResources(request);
        String paramid = request.getParameter("paramid1");
        Quotation quotation = new Quotation();
        quotation = new QuotationDAO().findById(Integer.parseInt(paramid));
        if (quotation.getMultiQuoteFlag().equalsIgnoreCase("C")) {
            multiQuotesForm.setConvertButtonFlag(true);//show convertButton
        }
        //  ---- for Record Lock ------    
        QuotationBC quotationBC = new QuotationBC();
        ProcessInfoBC processInfoBC = new ProcessInfoBC();
        List list = quotationBC.getQuatationInfo(quotation.getQuoteId());
        QuatationInfo quatationInfo = new QuatationInfo(list);
        User user = null;
        Integer userId = 0;
        HttpSession session = request.getSession();
        if (session.getAttribute("loginuser") != null) {
            user = (User) session.getAttribute("loginuser");
            userId = user.getUserId();
        }
        String returnResult = "";
        if (quotationBC.findConvertedRecords(quotation.getFileNo(), true)) {
            returnResult = processInfoBC.cheackFileNumberForLoack(quotation.getFileNo(), userId.toString(), QuotationConstants.QUOTE);
            if (CommonFunctions.isNotNull(returnResult)) {
                request.setAttribute(QuotationConstants.LOCK, "This Record is Used by " + returnResult);
            }
        } else {
            returnResult = processInfoBC.cheackFileINDB(quotation.getFileNo(), userId.toString()); //bookingInfo.getUsedBy();
            String processInfoDate = DateUtils.formatDate(quatationInfo.getProcessInfoDate(), "MM/dd/yyyy hh:mm a");
            if (CommonFunctions.isNotNull(returnResult) && !returnResult.equals("sameUser")) {
                request.setAttribute(QuotationConstants.LOCK, "This Record is Used by " + returnResult + " " + processInfoDate);
            }
        }
        multiQuotesForm.setChargeFlag(true);//Show Charges
        multiQuotesForm.setFocusValue(null);//CONVERT AND GoBACK
        multiQuotesForm.setQuoteId(quotation.getQuoteId());
        multiQuotesForm.setCustomerName(quotation.getClientname());
        multiQuotesForm.setClientNumber(quotation.getClientnumber());
        multiQuotesForm.setClienttype(quotation.getClienttype());
        request.setAttribute("quotationNo", messageResources.getMessage("fileNumberPrefix") + quotation.getFileNo());
        request.setAttribute("quoteBy", quotation.getQuoteBy());
        String qDate = DateUtils.formatDate(quotation.getQuoteDate(), "dd-MMM-yyyy HH:mm");
        multiQuotesForm.setQuoteDate(qDate);
        request.setAttribute("quoteDate", qDate);
        request.setAttribute("importFlag", false);
        List<MultiQuoteBean> originDestination = new MultiQuoteDao().getMultiQuoteBean(quotation.getQuoteId());
        List multiQuoteIdList = new MultiQuoteDao().getMultiQuoteId(quotation.getQuoteId());
        List<MultiQuoteChargesBean> fclRatesCharges = new MultiQuoteChargesDao().getChargesExpandList(multiQuoteIdList);
        List<MultiChargesOrderBean> collapseRates = new MultiQuoteChargesDao().getChargesCollapseList(multiQuoteIdList);
        request.setAttribute("collapseRates", collapseRates);
        request.setAttribute("charges", fclRatesCharges);//EXPAND view List
        request.setAttribute("originDestination", originDestination);
        request.setAttribute(QuotationConstants.QUOTATIONFORM, multiQuotesForm);
        return mapping.findForward("multiQuote");
    }

    public void getUnLocation(MultiQuote multiQuote) throws Exception {
//      --for Tp AcctName---
        multiQuote.setCarrier(new TradingPartnerDAO().getAccountName(multiQuote.getCarrierNo()));
        UnLocationDAO unLocationDAO = new UnLocationDAO();
//       ---for pol---
        UnLocation polUnLocation = unLocationDAO.findById(Integer.parseInt(multiQuote.getOriginCode()));
        if (null != polUnLocation) {
            if (polUnLocation.getCountryId() != null) {
                if (polUnLocation.getStateId() != null) {
                    multiQuote.setOrigin(polUnLocation.getUnLocationName() + "/" + polUnLocation.getStateId().getCode() + "/"
                            + polUnLocation.getCountryId().getCodedesc() + "(" + polUnLocation.getUnLocationCode() + ")");
                } else {
                    multiQuote.setOrigin(polUnLocation.getUnLocationName() + "/" + polUnLocation.getCountryId().getCodedesc() + "(" + polUnLocation.getUnLocationCode()
                            + ")");
                }
            } else {
                if (polUnLocation.getStateId() != null) {
                    multiQuote.setOrigin(polUnLocation.getUnLocationName() + "/" + polUnLocation.getStateId().getCode() + "/" + polUnLocation.getCountryId().getCodedesc() + "(" + polUnLocation.getUnLocationCode() + ")");
                } else {
                    multiQuote.setOrigin(polUnLocation.getUnLocationName() + "/" + polUnLocation.getCountryId().getCodedesc() + "(" + polUnLocation.getUnLocationCode()
                            + ")");
                }
            }
        }

//           ---for pol---
        UnLocation podUnLocation = unLocationDAO.findById(Integer.parseInt(multiQuote.getDestinationCode()));
        if (null != podUnLocation) {
            if (podUnLocation.getCountryId() != null) {
                if (podUnLocation.getStateId() != null) {
                    multiQuote.setDestination(podUnLocation.getUnLocationName() + "/" + podUnLocation.getStateId().getCode() + "/"
                            + podUnLocation.getCountryId().getCodedesc() + "(" + podUnLocation.getUnLocationCode() + ")");
                } else {
                    multiQuote.setDestination(podUnLocation.getUnLocationName() + "/"
                            + podUnLocation.getCountryId().getCodedesc() + "(" + podUnLocation.getUnLocationCode() + ")");
                }
            } else {
                if (podUnLocation.getStateId() != null) {
                    multiQuote.setDestination(podUnLocation.getUnLocationName() + "/" + podUnLocation.getStateId().getCode() + "("
                            + podUnLocation.getUnLocationCode() + ")");
                } else {
                    multiQuote.setDestination(podUnLocation.getUnLocationName() + "("
                            + podUnLocation.getUnLocationCode() + ")");
                }

            }
        }
    }

    public ActionForward convertQuoteNewRates(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        MultiQuotesForm multiQuotesForm = (MultiQuotesForm) form;
        MultiQuoteDao multiQuoteDao = new MultiQuoteDao();
        MultiQuote multiQuote = multiQuoteDao.findById(Long.parseLong(multiQuotesForm.getMultiQuoteRadioId()));
        Quotation quotation = new QuotationDAO().findById(multiQuotesForm.getQuoteId());
        quotation = multiQuoteDao.convertQuote(multiQuotesForm, multiQuote, quotation, request);// UPDATE Quote before covert
        String unitType = multiQuote.getSelected_Units();
        String unitTypes[] = unitType.split(",");
        MessageResources messageResources = getResources(request);
        GenericCode genericCode = genericCodeDAO.findById(Integer.parseInt(multiQuote.getCommodity()));

        List otherCommList = new FclBuyCostDAO().getOtherCommodity(genericCode.getCode(), "markup1");
        List<FclBuyOtherCommodity> otherCommodityList = new ArrayList<FclBuyOtherCommodity>();
        if (CommonUtils.isNotEmpty(otherCommList)) {
            for (Object otherComm : otherCommList) {
                Object[] object = (Object[]) otherComm;
//                commodity = null != object[3] ? object[3].toString() : "";
                FclBuyOtherCommodity fclBuyOtherCommodity = new FclBuyOtherCommodity();
                fclBuyOtherCommodity.setAddSub(null != object[0] ? object[0].toString() : "");
                fclBuyOtherCommodity.setMarkUp(null != object[1] ? Double.parseDouble(object[1].toString()) : 0);
                fclBuyOtherCommodity.setCostCode(null != object[2] ? genericCodeDAO.getFieldByCodeAndCodetypeId("Cost Code", object[2].toString(), "codedesc") : "");
                fclBuyOtherCommodity.setBaseCommodityCode(null != object[3] ? object[3].toString() : "");
                fclBuyOtherCommodity.setMarkUp2(null != object[4] ? Double.parseDouble(object[4].toString()) : 0);
                otherCommodityList.add(fclBuyOtherCommodity);
            }
        }

        List ratesList = new FclBuyDAO().findForSearchFclBuyRatesForCompressList2(multiQuote.getOriginCode(), multiQuote.getDestinationCode(), multiQuote.getCommodity(), multiQuote.getCarrierNo());
        for (Object rates : ratesList) {
            FclBuy fclBuy = (FclBuy) rates;
            List<Charges> chargesList = new ArrayList<Charges>();
            if (fclBuy.getFclBuyCostsSet() != null) {
                Iterator iter = fclBuy.getFclBuyCostsSet().iterator();
                while (iter.hasNext()) {
                    FclBuyCost fclBuyCost = (FclBuyCost) iter.next();
                    if (fclBuyCost.getFclBuyUnitTypesSet() != null) {
                        Iterator iterator = fclBuyCost.getFclBuyUnitTypesSet().iterator();
                        while (iterator.hasNext()) {
                            FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iterator.next();
                            Charges charges = new Charges();
                            charges.setQouteId(quotation.getQuoteId());
                            charges.setNumber("1");
                            if (fclBuyCost.getCostId() != null) {
                                charges.setChargeCode(fclBuyCost.getCostId());
                                charges.setChargeCodeDesc(fclBuyCost.getCostId().getCode());
                                charges.setChgCode(fclBuyCost.getCostId().getCodedesc());
                            }
                            if (fclBuyCost.getContType() != null) {
                                charges.setCosttype(fclBuyCost.getContType());
                                charges.setCostType(fclBuyCost.getContType().getCodedesc());
                                if (fclBuyCost.getContType().getCodedesc().equalsIgnoreCase(messageResources.getMessage("FlatRatePerConatinerSize"))) {
                                    charges.setAmount(fclBuyCostTypeRates.getActiveAmt());
                                } else {
                                    charges.setAmount(fclBuyCostTypeRates.getRatAmount());
                                }
                                charges.setMinimum(fclBuyCostTypeRates.getMinimumAmt());
                            }

                            double markup = 0.00;
                            if (CommonUtils.isNotEmpty(otherCommodityList)) {
                                boolean noMarkup = true;
                                for (FclBuyOtherCommodity fclBuyOtherCommodity : otherCommodityList) {
                                    if (CommonUtils.isEqualIgnoreCase(charges.getChgCode(), fclBuyOtherCommodity.getCostCode())) {
                                        if ("A".equalsIgnoreCase(fclBuyOtherCommodity.getAddSub())) {
                                            Ports port = portsDAO.findById(fclBuy.getDestinationPort().getId());
                                            if (null != port && null != port.getRegioncode() && "Y".equalsIgnoreCase(port.getRegioncode().getField3())) {
                                                markup += fclBuyCostTypeRates.getMarkup() + fclBuyOtherCommodity.getMarkUp2();
                                            } else {
                                                markup += fclBuyCostTypeRates.getMarkup() + fclBuyOtherCommodity.getMarkUp();
                                            }
                                        } else {
                                            Ports port = portsDAO.findById(fclBuy.getDestinationPort().getId());
                                            if (null != port && null != port.getRegioncode() && "Y".equalsIgnoreCase(port.getRegioncode().getField3())) {
                                                markup += fclBuyCostTypeRates.getMarkup() - fclBuyOtherCommodity.getMarkUp2();
                                            } else {
                                                markup += fclBuyCostTypeRates.getMarkup() - fclBuyOtherCommodity.getMarkUp();
                                            }
                                        }
                                        noMarkup = false;
                                    }
                                }
                                if (noMarkup) {
                                    markup += fclBuyCostTypeRates.getMarkup();
                                }
                            } else {
                                markup += fclBuyCostTypeRates.getMarkup();
                            }
                            charges.setMarkUp(markup);
                            charges.setEfectiveDate(new Date());
                            if (fclBuy.getSslineNo() != null) {
                                charges.setAccountName(fclBuy.getSslineNo().getAccountName());
                                charges.setAccountNo(fclBuy.getSslineNo().getAccountno());
                            }
                            if (fclBuyCostTypeRates.getCurrency() != null) {
                                charges.setCurrency1(fclBuyCostTypeRates.getCurrency());
                                charges.setCurrecny(fclBuyCostTypeRates.getCurrency().getCodedesc());
                            }
                            charges.setStandardCharge("Y");//Default Value
                            if (fclBuyCostTypeRates.getUnitType() != null) {
                                for (String unitType1 : unitTypes) {
                                    if (unitType1.equalsIgnoreCase(fclBuyCostTypeRates.getUnitType().getCodedesc())) {
                                        charges.setUnitType(fclBuyCostTypeRates.getUnitType().getId().toString());

                                        if (multiQuotesForm.getHazmat().equalsIgnoreCase("N") && !fclBuyCost.getCostId().getCode().equalsIgnoreCase("HAZFEE")) {// NO HAZARDOUS
                                            chargesList.add(charges);
                                        } else if (multiQuotesForm.getHazmat().equalsIgnoreCase("Y")) {
                                            chargesList.add(charges);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Collections.sort(chargesList, new MultiQuoteComparator());
            if (chargesList.get(0).getChargeCodeDesc().equalsIgnoreCase("OCNFRT") || chargesList.get(0).getChargeCodeDesc().equalsIgnoreCase("OFIMP")) {
                for (Charges charges : chargesList) {
                    new ChargesDAO().save(charges);
                }
            } else {
                String temp = "";
                int j = 0;
                int k = 0;
                LinkedList linkedList = new LinkedList();
                List<Charges> newList = new ArrayList();
                for (Iterator iterator = chargesList.iterator(); iterator.hasNext();) {
                    Charges q = (Charges) iterator.next();
                    if (null != q.getUnitType()) {
                        if (!temp.equals(q.getUnitType())) {
                            k = j;
                        }
                    }
                    if (q.getChargeCode().equals("OCNFRT") || q.getChargeCode().equals("OFIMP")) {
                        linkedList.add(k, q);
                    } else {
                        linkedList.add(q);
                    }
                    if (null != q.getUnitType()) {
                        temp = q.getUnitType();
                    }
                    j++;
                }
                newList.addAll(linkedList);
                for (Charges charges : newList) {
                    new ChargesDAO().save(charges);
                }
            }
        }
        HttpSession session = request.getSession();
        session.setAttribute("selectedFileNumber", quotation.getFileNo());
        session.setAttribute("screenName", "Quotes");
        return mapping.findForward("closeSearch");
    }

}

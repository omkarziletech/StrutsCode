package com.gp.cvst.logisoft.hibernate.dao;

import com.gp.cong.common.DateUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.FclOrgDestMiscData;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.hibernate.dao.AgencyInfoDAO;
import com.gp.cong.logisoft.hibernate.dao.FclOrgDestMiscDataDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cvst.logisoft.beans.MultiQuoteBean;
import com.gp.cvst.logisoft.domain.MultiQuote;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.struts.form.MultiQuotesForm;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

/**
 *
 * @author NambuRajasekar.M
 */
public class MultiQuoteDao extends BaseHibernateDAO<MultiQuote> {

    public MultiQuoteDao() {
        super(MultiQuote.class);
    }

    public MultiQuote findById(java.lang.Long id) throws Exception {
        MultiQuote instance = (MultiQuote) getSession().get("com.gp.cvst.logisoft.domain.MultiQuote", id);
        return instance;
    }

    public Quotation saveForm(MultiQuotesForm quotesForm) throws Exception {
        Quotation quotation = new Quotation();
        Date date = null;
        if (quotesForm.getQuoteDate() != null) {
            date = DateUtils.parseDate(quotesForm.getQuoteDate(), "dd-MMM-yyyy HH:mm");
        } else {
            date = new Date();
        }
        quotation.setQuoteDate(date);
        quotation.setQuoteBy(quotesForm.getQuoteBy().toUpperCase());
//         quotation.setBulletRatesCheck(quotesForm.getBulletRatesCheck());
        if (quotesForm.getNewClient() != null && quotesForm.getNewClient().equalsIgnoreCase("on")) {
            quotation.setClientname(quotesForm.getCustomerName1());
        } else {
            quotation.setClientname(quotesForm.getCustomerName());
        }
        quotation.setClientnumber(quotesForm.getClientNumber());
        quotation.setClienttype(quotesForm.getClienttype());
        quotation.setContactname(quotesForm.getContactName());
        quotation.setFax(quotesForm.getFax());
        quotation.setPhone(quotesForm.getPhone());
        quotation.setEmail1(quotesForm.getEmail());
        quotation.setFileType("S");
        quotation.setRatesNonRates("R");
        if (quotesForm.getClientConsigneeCheck() != null) {
            quotation.setClientConsigneeCheck(quotesForm.getClientConsigneeCheck());
        } else {
            quotation.setClientConsigneeCheck("off");
        }

        if (quotesForm.getAmount() == null || quotesForm.getAmount().equals("")) {
            quotesForm.setAmount("0.00");
        }
        if (quotesForm.getAmount1() == null || quotesForm.getAmount1().equals("")) {
            quotesForm.setAmount1("0.00");
        }
        if (quotesForm.getCostofgoods() == null || quotesForm.getCostofgoods().equals("")) {
            quotesForm.setCostofgoods("0.00");
        }
//        quotation.setDocsInquiries("on");
        new QuotationDAO().save(quotation);
        return quotation;
    }

    public Quotation updateQuotes(MultiQuotesForm quotesForm, Quotation quotation, HttpServletRequest request) throws Exception {
        if (quotesForm.getNewClient() != null && quotesForm.getNewClient().equalsIgnoreCase("on")) {
            quotation.setClientname(quotesForm.getCustomerName1());
        } else {
            quotation.setClientname(quotesForm.getCustomerName());
        }
        if (quotesForm.getClientConsigneeCheck() != null) {
            quotation.setClientConsigneeCheck(quotesForm.getClientConsigneeCheck());
        } else {
            quotation.setClientConsigneeCheck("off");
        }
        quotation.setClientnumber(quotesForm.getClientNumber());
        quotation.setClienttype(quotesForm.getClienttype());
        quotation.setContactname(quotesForm.getContactName());
        quotation.setFax(quotesForm.getFax());
        quotation.setPhone(quotesForm.getPhone());
        quotation.setEmail1(quotesForm.getEmail());
        new QuotationDAO().update(quotation);
        return quotation;
    }

    public Quotation convertQuote(MultiQuotesForm quotesForm, MultiQuote mq, Quotation quotation, HttpServletRequest request) throws Exception {
        quotation.setQuoteDate(new Date());// Need to check
        quotation.setOrigin_terminal(mq.getOrigin());
        quotation.setDestination_port(mq.getDestination());
        GenericCode genericCode = new GenericCodeDAO().findById(Integer.parseInt(mq.getCommodity()));
        quotation.setCommcode(genericCode);
        quotation.setDescription(genericCode.getCodedesc());
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        if (loginUser.getTerminal() != null) {
            quotation.setIssuingTerminal(loginUser.getTerminal().getTerminalLocation() + "-" + loginUser.getTerminal().getTrmnum());
        }
        quotation.setCarrier(mq.getCarrierNo());
        quotation.setSsline(mq.getCarrierNo());
        quotation.setSslname(mq.getCarrier());
        quotation.setSelectedUnits(mq.getSelected_Units());
        quotation.setBulletRatesCheck(mq.getBulletRates());
        quotation.setHazmat(mq.getHazmat());
        quotation.setPlor(mq.getOrigin());
        quotation.setFinaldestination(mq.getDestination());

        //form Value
        if (quotesForm.getNewClient() != null && quotesForm.getNewClient().equalsIgnoreCase("on")) {
            quotation.setClientname(quotesForm.getCustomerName1());
        } else {
            quotation.setClientname(quotesForm.getCustomerName());
        }
        quotation.setContactname(quotesForm.getContactName());
        quotation.setClientnumber(quotesForm.getClientNumber());
        quotation.setClienttype(quotesForm.getClienttype());
        quotation.setFax(quotesForm.getFax());
        quotation.setPhone(quotesForm.getPhone());
        quotation.setEmail1(quotesForm.getEmail());
        if (quotesForm.getClientConsigneeCheck() != null) {
            quotation.setClientConsigneeCheck(quotesForm.getClientConsigneeCheck());
        } else {
            quotation.setClientConsigneeCheck("off");
        }
        //Set Default Value
        quotation.setDirectConsignmntCheck("off");
        quotation.setTypeofMove("PORT TO PORT");
        quotation.setAlternateagent("Y");
        quotation.setDefaultAgent("Y");
        quotation.setSpecialequipment("N");
        quotation.setLocaldryage("N");
        quotation.setAmount(0.00);
        quotation.setAmount1(0.00);
        quotation.setIntermodel("N");
        quotation.setInsurance("N");
        quotation.setCostofgoods(0.00);
        quotation.setCustomertoprovideSed("N");
        quotation.setDeductFfcomm("N");//Need to check
        quotation.setOutofgage("N");
        quotation.setInsurancamt(0.00);
        quotation.setComment1("");//Need to check
        quotation.setRoutedAgentCheck("");
        //       --- for  TransitDays ---
        List<FclOrgDestMiscData> fclOrgDestMiscData = new FclOrgDestMiscDataDAO().getorgdestmiscdate1(mq.getOriginCode(), mq.getDestinationCode(), mq.getCarrierNo());
        if (null != fclOrgDestMiscData && fclOrgDestMiscData.size() > 0) {
            quotation.setRatesRemarks(fclOrgDestMiscData.get(0).getRemarks());
            if (fclOrgDestMiscData.get(0).getDaysInTransit() != null) {
                quotation.setNoOfDays(String.valueOf(fclOrgDestMiscData.get(0).getDaysInTransit()));
            }
        }
//     -- for DefaultAgent--
//        UnLocation location = new UnLocationDAO().findById(Integer.parseInt(mq.getDestinationCode()));
//        AgencyInfo linkAcct = new AgencyInfoDAO().getLinkAcct(Integer.parseInt(mq.getDestinationCode()));
        String tpAcctNo = new AgencyInfoDAO().getTpAcct(Integer.parseInt(mq.getDestinationCode()));
        if (!tpAcctNo.isEmpty()) {
            TradingPartner tp = new TradingPartnerDAO().findById(tpAcctNo);
            if (tp.getDisabled() != null && tp.getDisabled().equalsIgnoreCase("Y")) {
                quotation.setAgentNo(tp.getForwardAccount());//Disabled With ForwardAcct
                quotation.setAgent(new TradingPartnerDAO().getAccountName(tp.getForwardAccount()));
            } else {
                quotation.setAgentNo(tp.getAccountno());//Link Acct
                quotation.setAgent(tp.getAccountName());
            }
        }
        quotation.setDrayageMarkUp(0.00);
        quotation.setIntermodalMarkUp(0.00);
        quotation.setBreakBulk("N");
        quotation.setDocCharge("N");
        quotation.setQuoteFlag("Open");
        quotation.setCarrierPrint("on");//Need to check
        quotation.setFinalized("");
        quotation.setBaht(0.00);
        quotation.setBdt(0.00);
        quotation.setCyp(0.00);
        quotation.setEur(0.00);
        quotation.setHkd(0.00);
        quotation.setLkr(0.00);
        quotation.setNt(0.00);
        quotation.setPrs(0.00);
        quotation.setRmb(0.00);
        quotation.setWon(0.00);
        quotation.setYen(0.00);
        quotation.setCarrierContact("");
        quotation.setCarrierPhone("");
        quotation.setCarrierFax("");
        quotation.setCarrierEmail("");
        quotation.setInsuranceCharge(0.00);
        quotation.setDocsInquiries("off");
        quotation.setImportantDisclosures("off");
        quotation.setDocumentAmount(0.00);
        quotation.setChangeIssuingTerminal("N");
        quotation.setMultiQuoteFlag(null);// this is show In Seacrh Screen
        new QuotationDAO().update(quotation);
        return quotation;
    }

    public List<MultiQuoteBean> getMultiQuoteBean(int quoteId) throws Exception {
        String queryString = "SELECT mq.id AS id, mq.origin AS origin, mq.destination AS destination,mq.carrier_no AS carrierNo, mq.carrier AS carrier"
                + " FROM `multi_quote` mq WHERE quote_id=:quoteId";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setResultTransformer(Transformers.aliasToBean(MultiQuoteBean.class));
        query.addScalar("id", LongType.INSTANCE);
        query.addScalar("origin", StringType.INSTANCE);
        query.addScalar("destination", StringType.INSTANCE);
        query.addScalar("carrierNo", StringType.INSTANCE);
        query.addScalar("carrier", StringType.INSTANCE);
        query.setParameter("quoteId", quoteId);
        return query.list();
    }

    public List getMultiQuoteId(int quoteId) throws Exception {
        String queryString = "SELECT mq.id FROM `multi_quote` mq WHERE quote_id=:quoteId";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setParameter("quoteId", quoteId);
        return query.list();
    }

    public List findByPropertyAsc(String propertyName, Object value) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(MultiQuote.class);
        criteria.add(Restrictions.eq(propertyName, value));
        return criteria.list();
    }
}

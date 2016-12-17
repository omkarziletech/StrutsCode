package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.bc.fcl.QuotationBC;
import com.gp.cong.logisoft.beans.CostBean;
import com.gp.cong.logisoft.beans.GenericCodeCacheManager;
import com.gp.cong.logisoft.beans.NonRates;
import com.gp.cong.logisoft.beans.Rates;
import com.gp.cong.logisoft.domain.CarriersOrLineTemp;
import com.gp.cong.logisoft.domain.FclBuy;
import com.gp.cong.logisoft.domain.FclBuyCost;
import com.gp.cong.logisoft.domain.FclBuyCostTypeFutureRates;
import com.gp.cong.logisoft.domain.FclBuyCostTypeFutureRatesHisptory;
import com.gp.cong.logisoft.domain.FclBuyCostTypeRates;
import com.gp.cong.logisoft.domain.FclOrgDestMiscData;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.GlobalRates;
import com.gp.cong.logisoft.domain.HistoryRates;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.TradingPartnerTemp;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.rates.Commodity;
import com.gp.cong.logisoft.util.DBUtil;
import com.gp.cong.logisoft.util.StringFormatter;
import com.gp.cvst.logisoft.domain.BookingfclUnits;
import com.gp.cvst.logisoft.domain.Charges;
import com.gp.cvst.logisoft.domain.MultiQuoteCharges;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.hibernate.dao.ZipCodeDAO;
import com.logiware.hibernate.domain.FclBuyOtherCommodity;
import com.logiware.ims.client.ImsModel;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;

public class FclBuyDAO extends BaseHibernateDAO {

    private static final Logger log = Logger.getLogger(FclBuyDAO.class);
    DBUtil dbUtil = new DBUtil();
    FclBuyCostDAO fclBuyCostDAO = new FclBuyCostDAO();
    FclBuyCostTypeRatesDAO fclBuyCostTypeRatesDAO = new FclBuyCostTypeRatesDAO();
    FclOrgDestMiscDataDAO fclOrgDestMiscDataDAO = new FclOrgDestMiscDataDAO();
    TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
    GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
    PortsDAO portsDAO = new PortsDAO();

    public void save(FclBuy transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
        getCurrentSession().flush();
    }

    public void historySave(HistoryRates transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
        getCurrentSession().flush();
    }

    public List getFutureRates(Date date) throws Exception {
        String queryString = "from GlobalRates where effectiveDate=?";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setDate(0, date);
        List li = (List) queryObject.list();
        return li;
    }

    public void FclHistoryRatessave(FclBuyCostTypeFutureRatesHisptory transientInstance) throws Exception {
        Transaction t = getCurrentSession().beginTransaction();
        t.begin();
        getCurrentSession().save(transientInstance);
        t.commit();
    }

    public void updateCurrentRates(FclBuyCostTypeRates transientInstance) throws Exception {
        Transaction t = getCurrentSession().beginTransaction();
        t.begin();
        getCurrentSession().update(transientInstance);
        t.commit();
    }

    public void FclHistoryRatesDelete(FclBuyCostTypeFutureRates transientInstance) throws Exception {
        Transaction t = getCurrentSession().beginTransaction();
        t.begin();
        getCurrentSession().delete(transientInstance);
        t.commit();
    }

    public List getFclHistory(Integer id) throws Exception {
        String queryString = "from FclBuyCostTypeFutureRatesHisptory where fcl_id=?";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setInteger(0, id);
        List li = (List) queryObject.list();
        return li;
    }

    public List getCurrentRates(Integer id, String unit_type) throws Exception {
        String queryString = null;
        Query queryObject = null;
        List li = null;
        if (id != null && unit_type != null) {
            queryString = "from FclBuyCostTypeRates where fclCostId=? and unitType=?";
            queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setInteger(0, id);
            queryObject.setString(1, unit_type);
        } else if (id != null) {
            queryString = "from FclBuyCostTypeRates where fclCostId=?";
            queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setInteger(0, id);

        }

        if (queryObject != null) {
            li = (List) queryObject.list();
        }
        return li;
    }

    public void update(FclBuy persistanceInstance) throws Exception {
        getCurrentSession().update(persistanceInstance);
        getCurrentSession().flush();
    }

    public void updateForFClRates(FclBuy persistanceInstance) throws Exception {
        getCurrentSession().saveOrUpdate(persistanceInstance);
        getCurrentSession().flush();
    }
    
    
    public void updateGlobalRates(GlobalRates persistanceInstance) throws Exception {
        getCurrentSession().update(persistanceInstance);
        getCurrentSession().flush();
    }
    /*
     public void removeRecords(FclBuy updatefclBuy){

     Set retrive=new HashSet<FclBuyCost>();
     if(updatefclBuy.getFclBuyCostsSet()!=null){

     retrive=updatefclBuy.getFclBuyCostsSet();
     Iterator it=retrive.iterator();
     while(it.hasNext()){
     FclBuyCost fclBuyCost=(FclBuyCost)it.next();

     if(fclBuyCost.getFclStdId()==null){
     updatefclBuy.getFclBuyCostsSet().remove(fclBuyCost);

     }
     }
     }

     }
     */

    public List findAllDetails(UnLocation org, UnLocation des, GenericCode com_code, TradingPartnerTemp SSLine, String orgRegion, String destRegion) throws Exception {
        List li = null;
        String queryString = " from FclBuy where originTerminal=?0 and destinationPort=?1 and comNum=?2 and sslineNo=?3 and originalRegion=?4 and destinationRegion=?5";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", org);
        queryObject.setParameter("1", des);
        queryObject.setParameter("2", com_code);
        queryObject.setParameter("3", SSLine);
        queryObject.setParameter("4", orgRegion);
        queryObject.setParameter("5", destRegion);
        li = queryObject.list();
        return li;
    }

    public void delete(FclBuy persistanceInstance) throws Exception {
        getSession().delete(persistanceInstance);
        getSession().flush();
    }

    public List findForUpdatingFutureRates(String originTerminal, String destAirPort, String comCode,
            String orgRegion, String destRegion, String contract, Date endDate) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(FclBuy.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (originTerminal != null && !originTerminal.equals("")) {
            criteria.createCriteria("originTerminal").add(Restrictions.eq("trmnum", originTerminal));
        }
        if (destAirPort != null && !destAirPort.equals("")) {
            criteria.createCriteria("destinationPort").add(Restrictions.eq("shedulenumber", destAirPort));
        }
        if (comCode != null && !comCode.equals("")) {
            criteria.createCriteria("comNum").add(Restrictions.eq("code", comCode));
        }
        if (orgRegion != null && !orgRegion.equals("")) {
            criteria.add(Restrictions.eq("originalRegion", orgRegion));
        }
        if (destRegion != null && !destRegion.equals("")) {
            criteria.add(Restrictions.eq("destinationRegion", destRegion));
        }
        if (contract != null && !contract.equals("")) {
            criteria.add(Restrictions.eq("contract", contract));
        }
        if (endDate != null && !endDate.equals("")) {
            criteria.add(Restrictions.eq("endDate", endDate));
        }
        if (endDate != null && !endDate.equals("")) {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String strSODate = dateFormat.format(endDate);
            //criteria.add(Restrictions.eq("logisticOrderDate", strSODate));
            Date soStartDate = (Date) dateFormat.parse(strSODate);
            Calendar endCal = Calendar.getInstance();
            endCal.setTime(soStartDate);
            endCal.add(Calendar.DATE, 1);
            Date soEndDate = endCal.getTime();
            criteria.add(Restrictions.between("endDate", soStartDate, soEndDate));
        }
        return criteria.list();
    }

    public List findForSearchFclBuyRates(String originTerminal, String destAirPort, String comCode, String carries) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(FclBuy.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (originTerminal != null && !originTerminal.equals("")) {
            //criteria.add(Restrictions.like("orgTerminal",originTerminal));
            criteria.createCriteria("originTerminal").add(Restrictions.ge("trmnum", originTerminal));
        }
        if (destAirPort != null && !destAirPort.equals("")) {

            criteria.createCriteria("destinationPort").add(Restrictions.ge("shedulenumber", destAirPort));
        }
        if (comCode != null && !comCode.equals("")) {

            criteria.createCriteria("comNum").add(Restrictions.ge("code", comCode));
        }
        if (carries != null && !carries.equals("")) {
            criteria.createCriteria("sslineNo").add(Restrictions.ge("accountno", carries));

        }

        return criteria.list();
    }

    public List findForSearchFclBuyRatesForCompressList1(String originTerminal, String destAirPort, String comCode, String carries, String orgRegion, String destRegion) throws Exception {
        String queryString5 = "from FclBuy where originTerminal.id='" + originTerminal + "' and destinationPort.id='" + destAirPort + "' and comNum.id='" + comCode + "' and sslineNo.accountno='" + carries + "' and originalRegion='" + orgRegion + "' and destinationRegion='" + destRegion + "' order by sslineNo.accountName";
        List queryObj = getCurrentSession().createQuery(queryString5).list();
        return queryObj;
    }

    public List findForSearchFclBuyRatesMatch(String originTerminal, String destAirPort, String comCode, String carries, String orgRegion, String destRegion) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(FclBuy.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (originTerminal != null && !originTerminal.equals("")) {
            criteria.createCriteria("originTerminal").add(Restrictions.like("unLocationCode", originTerminal + "%"));
        }

        if (destAirPort != null && !destAirPort.equals("")) {

            criteria.createCriteria("destinationPort").add(Restrictions.like("unLocationCode", destAirPort + "%"));
        }

        if (comCode != null && !comCode.equals("")) {

            criteria.createCriteria("comNum").add(Restrictions.like("code", comCode));
        }
        if (carries != null && !carries.equals("")) {
            criteria.createCriteria("sslineNo").add(Restrictions.like("accountno", carries));

        }
        if (orgRegion != null && !orgRegion.equals("")) {
            criteria.add(Restrictions.like("originalRegion", orgRegion + "%"));
        }
        if (destRegion != null && !destRegion.equals("")) {
            criteria.add(Restrictions.like("destinationRegion", destRegion + "%"));
        }
        criteria.addOrder(Order.asc("sslineNo"));
        return criteria.list();

    }

    public List findForSearchFclBuyRatesMatch(UnLocation originTerminal, UnLocation destAirPort, GenericCode comCode, CarriersOrLineTemp carries) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(FclBuy.class);

        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        if (originTerminal != null && !originTerminal.equals("")) {
            criteria.add(Restrictions.like("originTerminal", originTerminal));
            criteria.addOrder(Order.asc("originTerminal"));
        }
        if (destAirPort != null && !destAirPort.equals("")) {
            criteria.add(Restrictions.like("destinationPort", destAirPort));
            criteria.addOrder(Order.asc("destinationPort"));
        }
        if (comCode != null && !comCode.equals("")) {
            criteria.add(Restrictions.like("comNum", comCode));
            criteria.addOrder(Order.asc("comNum"));
        }
        if (carries != null && !carries.equals("")) {

            criteria.add(Restrictions.like("sslineNo", carries));
            criteria.addOrder(Order.asc("sslineNo"));
        }
        return criteria.list();
    }

    public FclBuy findById(Integer id) throws Exception {
        FclBuy instance = (FclBuy) getCurrentSession().get("com.gp.cong.logisoft.domain.FclBuy", id);
        return instance;
    }

    private void addEmptyUnitTypes(FclBuyCost fclBuyCost, String unitTypes) throws Exception {
        String modifiedUnitTypes = new String(unitTypes);
        if (modifiedUnitTypes.startsWith(",")) {
            modifiedUnitTypes = modifiedUnitTypes.substring(1);
        }
        if (modifiedUnitTypes.endsWith(",")) {
            modifiedUnitTypes = modifiedUnitTypes.substring(0, modifiedUnitTypes.length() - 1);
        }
        if (CommonUtils.isNotEmpty(modifiedUnitTypes)) {
            String[] unitTypeAry = modifiedUnitTypes.split(",");
            for (int i = 0; i < unitTypeAry.length; i++) {
                if (CommonUtils.isEmpty(unitTypeAry[i])) {
                    FclBuyCostTypeRates blankFclBuyCostTypeRates = new FclBuyCostTypeRates();
                    blankFclBuyCostTypeRates.setUnitAmount(0d);
                    blankFclBuyCostTypeRates.setCurrency(new GenericCode());
                    blankFclBuyCostTypeRates.setUnitname(unitTypeAry[i]);
                    boolean flag = false;
                    if (fclBuyCost.getUnitTypeList() != null) {
                        for (Iterator iterator = fclBuyCost.getUnitTypeList().iterator(); iterator.hasNext();) {
                            FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iterator.next();
                            if (fclBuyCostTypeRates != null && fclBuyCostTypeRates.getUnitname() != null && fclBuyCostTypeRates.getUnitname().equals(unitTypeAry[i])) {
                                flag = true;
                                break;
                            }
                        }
                    }
                    if (!flag) {
                        if (fclBuyCost.getUnitTypeList() != null) {
                            fclBuyCost.getUnitTypeList().add(blankFclBuyCostTypeRates);
                        } else {
                            List tempList = new ArrayList();
                            tempList.add(blankFclBuyCostTypeRates);
                            fclBuyCost.setUnitTypeList(tempList);
                        }
                    }
                }
            }
            HashMap hashMap = new HashMap();
            List tempList = new ArrayList();
            List tempUnitTypeList = new ArrayList();
            List unitTypeList = fclBuyCost.getUnitTypeList();
            fclBuyCost.setUnitTypeList(new ArrayList());
            for (Iterator iterator = unitTypeList.iterator(); iterator.hasNext();) {
                FclBuyCostTypeRates tempFclBuyCostTypeRates = (FclBuyCostTypeRates) iterator.next();
                if (tempFclBuyCostTypeRates.getUnitname() != null) {
                    hashMap.put(tempFclBuyCostTypeRates.getUnitname(), tempFclBuyCostTypeRates);
                    tempList.add(tempFclBuyCostTypeRates.getUnitname());
                }
            }
            Collections.sort(tempList);
            for (int i = 0; i < tempList.size(); i++) {
                FclBuyCostTypeRates tempFclBuyCostTypeRates = (FclBuyCostTypeRates) hashMap.get(tempList.get(i));
                tempUnitTypeList.add(tempFclBuyCostTypeRates);
            }
            fclBuyCost.setUnitTypeList(tempUnitTypeList);
        }
    }

    public List addingAmountforOtherCommodity(List fclBuyList, List nonRatesList) throws Exception {
        for (Iterator iterator = nonRatesList.iterator(); iterator.hasNext();) {
            NonRates nonRates = (NonRates) iterator.next();
            for (Iterator iterator2 = fclBuyList.iterator(); iterator2.hasNext();) {
                Rates rates = (Rates) iterator2.next();
                rates.setComNum(nonRates.getCommodity().toString());
                String code = (String) GenericCodeCacheManager.getCode(rates.getCostId());
                if (nonRates.getChargeCode().equals(code)) {
                    if (rates.getUnitType() != null) {
                        if (nonRates.getAddsub().equals("A")) {
                            rates.setRatAmount(rates.getRatAmount() + nonRates.getAmount());
                        } else {
                            rates.setRatAmount(rates.getRatAmount() - nonRates.getAmount());
                        }
                    } else {
                        if (nonRates.getAddsub().equals("A")) {
                            rates.setActiveAmt(rates.getActiveAmt() + nonRates.getAmount());
                        } else {
                            rates.setActiveAmt(rates.getActiveAmt() - nonRates.getAmount());
                        }
                    }
                }
            }
        }
        return fclBuyList;
    }

    public List findRatesForOtherCommodity(Integer id, String markUp) throws Exception {
        String queryString = "select * from fclbuyothercomm where commodity_code='" + id + "'";
        List queryObj = getCurrentSession().createSQLQuery(queryString).list();
        NonRates nonRates = new NonRates();
        List nonRatesList = new ArrayList();
        for (Iterator iterator = queryObj.iterator(); iterator.hasNext();) {
            Object[] row = (Object[]) iterator.next();
            nonRates = new NonRates();
            if (row[1] != null) {
                nonRates.setCommodity((Integer) row[1]);
            }
            if (row[2] != null) {
                nonRates.setBaseCommodity((Integer) row[2]);
            }
            if (row[3] != null) {
                nonRates.setAddsub((String) row[3]);
            }
            if ("markup2".equalsIgnoreCase(markUp)) {
                if (row[5] != null) {
                    String a = ((BigDecimal) row[5]).toString();
                    nonRates.setAmount(Double.parseDouble(a));
                }
            } else {
                if (row[4] != null) {
                    String a = ((BigDecimal) row[4]).toString();
                    nonRates.setAmount(Double.parseDouble(a));
                }
            }
            if (row[6] != null) {
                nonRates.setChargeCode((String) row[6]);
            }
            nonRatesList.add(nonRates);
        }
        return nonRatesList;
    }

    public Commodity findMultipleRates(String originTerminal, String destAirPort, String comCode, boolean hazardous, String service, Map<String, Double> distanceMap, List<FclBuyOtherCommodity> otherCommodityList, Map<String, ImsModel> imsQuoteMap, String zipCode, String originPort, String destinationPort, String path,String fileType) throws Exception {
        String originTerminalCount[] = StringUtils.split(originTerminal, ",");
        String destAirPortCount[] = StringUtils.split(destAirPort, ",");
        originTerminal = null != originTerminal ? "'" + StringUtils.join(originTerminal.split(","), "','") + "'" : "''";
        destAirPort = null != destAirPort ? "'" + StringUtils.join(destAirPort.split(","), "','") + "'" : "''";
        String queryString5 = "SELECT r.fcl_std_id,"
                + "r.start_date,"
                + "r.end_date,"
                + "r.origin_terminal,"
                + "r.destination_port,"
                + "r.ssline_no,"
                + "r.com_num,"
                + "r.contract,"
                + "r.origin_region,"
                + "r.destination_region,"
                + "r.fcl_cost_id,"
                + "r.cost_id,"
                + "r.cost_type,"
                + "r.fcl_cost_type_id,"
                + "r.unit_type,"
                + "r.active_amt,"
                + "r.ctc_amt,"
                + "r.ftf_amt,"
                + "r.minimum_amt,"
                + "r.rat_amount,"
                + "r.standard,";
        if (CommonUtils.isNotEmpty(otherCommodityList)) {
            queryString5 = queryString5 + "CASE ";
            for (FclBuyOtherCommodity fclBuyOtherCommodity : otherCommodityList) {
                if ("A".equalsIgnoreCase(fclBuyOtherCommodity.getAddSub())) {
                    queryString5 = queryString5 + "WHEN r.codedesc = '" + fclBuyOtherCommodity.getCostCode() + "' and g.field3 = 'Y' then r.markup+" + fclBuyOtherCommodity.getMarkUp2() + "  "
                            + "WHEN r.codedesc = '" + fclBuyOtherCommodity.getCostCode() + "'  then r.markup+" + fclBuyOtherCommodity.getMarkUp() + " ";
                } else {
                    queryString5 = queryString5 + "WHEN r.codedesc = '" + fclBuyOtherCommodity.getCostCode() + "' and g.field3 = 'Y' then r.markup-" + fclBuyOtherCommodity.getMarkUp2() + "  "
                            + "WHEN r.codedesc = '" + fclBuyOtherCommodity.getCostCode() + "'  then r.markup-" + fclBuyOtherCommodity.getMarkUp() + " ";
                }
            }
            queryString5 = queryString5 + "ELSE r.markup END AS markup,";
        } else {
            queryString5 = queryString5 + "r.markup,";
        }
        queryString5 = queryString5 + "r.currency_type,"
                + "r.codedesc,"
                + "r.polcode,"
                + "r.cost_code_desc,"
                + "r.acct_name,"
                + "r.unit_code,"
                + "r.unit_code_desc,"
                + "r.transit_time,"
                + "r.remarks,"
                + "r.hazardous_flag,"
                + "r.port_of_exit,"
                + "r.dray ,IF(org.statecode IS NOT NULL,CONCAT(org.portname,'/',org.statecode,'/',org.countryname,'(',org.unlocationcode,')'),CONCAT(org.portname,'/',org.countryname,'(',org.unlocationcode,')')) AS origin, "
                + "IF(dest.statecode IS NOT NULL,CONCAT(dest.portname,'/',dest.statecode,'/',dest.countryname,'(',dest.unlocationcode,')') ,CONCAT(dest.portname,'/',dest.countryname,'(',dest.unlocationcode,')'))AS destination,"
                + "IF(org.statecode IS NOT NULL,CONCAT(org.portname,',',org.statecode,',',org.countryname),concat(org.portname,',',org.countryname)) AS origin_name, "
                + "IF(dest.statecode IS NOT NULL,CONCAT(dest.portname,',',dest.statecode,',',dest.countryname),concat(dest.portname,',',dest.countryname)) AS destination_name, r.lat, r.lng, r.chargecode from rates_list r JOIN ports org ON org.id = r.origin_terminal JOIN ports dest ON dest.id=r.destination_port JOIN genericcode_dup g ON g.id = dest.regioncode where "
                + "r.origin_terminal IN (" + originTerminal + ") and r.destination_port IN (" + destAirPort + ") and r.com_num='" + comCode + "'";
        if (!hazardous) {
            queryString5 = queryString5 + " and r.cost_id != 11668";
        }
        queryString5 += " order by ";
        if (CommonUtils.isNotEmpty(originPort) && CommonUtils.isNotEmpty(destinationPort)) {
            if (originPort.contains("/")) {
                originPort = originPort.substring(0, originPort.indexOf("/"));
            }
            if (destinationPort.contains("/")) {
                destinationPort = destinationPort.substring(0, destinationPort.indexOf("/"));
            }
            queryString5 += "field(concat(org.portname,dest.portname),'" + originPort.replace("'", "\\'") + destinationPort.replace("'", "\\'") + "') desc,";
        }
        queryString5 += "origin,destination,ssline_no,r.cost_id,r.unit_type";
        List queryObj = getCurrentSession().createSQLQuery(queryString5).list();
        Commodity commodity = new Commodity(queryObj, distanceMap, service, imsQuoteMap, hazardous, new ZipCodeDAO().findByZip(zipCode), path,fileType);
        return commodity;
    }

    public String findMultipleSsl(String originTerminal, String destAirPort, String comCode, boolean hazardous) throws Exception {
        String originTerminalCount[] = StringUtils.split(originTerminal, ",");
        String destAirPortCount[] = StringUtils.split(destAirPort, ",");
        String comCodeCount[] = StringUtils.split(comCode, ",");
        originTerminal = null != originTerminal ? "'" + StringUtils.join(originTerminal.split(","), "','") + "'" : "''";
        destAirPort = null != destAirPort ? "'" + StringUtils.join(destAirPort.split(","), "','") + "'" : "''";
        comCode = comCode.replaceAll("'", "");
        comCode = "'" + StringUtils.join(comCode.split(","), "','") + "'";
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" select concat(\"'\",r.ssline_no,\"'\") ");
        queryBuilder.append(" from rates_list r ");
        queryBuilder.append(" join ports org on org.id = r.origin_terminal ");
        queryBuilder.append(" join ports dest on dest.id=r.destination_port ");
        queryBuilder.append(" join genericcode_dup g on g.id = dest.regioncode where ");
        if (null != originTerminalCount && originTerminalCount.length > 1) {
            queryBuilder.append(" r.origin_terminal in (").append(originTerminal).append(") and ");
        } else {
            queryBuilder.append(" r.origin_terminal = ").append(originTerminal).append(" and ");
        }
        if (null != destAirPortCount && destAirPortCount.length > 1) {
            queryBuilder.append(" r.destination_port in (").append(destAirPort).append(") and ");
        } else {
            queryBuilder.append(" r.destination_port = ").append(destAirPort).append(" and ");
        }
        if (comCodeCount.length > 1) {
            queryBuilder.append(" r.com_num in (").append(comCode).append(") ");
        } else {
            queryBuilder.append(" r.com_num = ").append(comCode).append("");
        }
        if (!hazardous) {
            queryBuilder.append(" and r.cost_id != 11668");
        }
        queryBuilder.append(" group by r.ssline_no ");
        List<String> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        return CommonUtils.isNotEmpty(result) ? result.toString().replace("[", "").replace("]", "") : null;
    }

    public List<Rates> findRates(String originTerminal, String destAirPort, String comId) {
        String queryString5 = "select * from rates_list rates, genericcode_dup code, trading_partner where "
                + "origin_terminal='" + originTerminal + "' and destination_port='" + destAirPort + "' and com_num='" + comId + "' and  "
                + "rates.cost_type=code.id and rates.ssline_no=trading_partner.acct_no order by rates.ssline_no";
        List queryObj = getCurrentSession().createSQLQuery(queryString5).list();
        Iterator itr = queryObj.iterator();
        List<Rates> ratesList = new ArrayList<Rates>();
        Rates rates = new Rates();
        while (itr.hasNext()) {
            Object[] row = (Object[]) itr.next();
            rates = new Rates();
            if (row[0] != null) {
                rates.setFclStdId((Integer) row[0]);
            }
            if (row[1] != null) {
                rates.setStartDate((Date) row[1]);
            }
            if (row[2] != null) {
                rates.setEndDate((Date) row[2]);
            }
            if (row[3] != null) {
                rates.setOriginTerminal((String) row[3]);
            }
            if (row[4] != null) {
                rates.setDestinationPort((String) row[4]);
            }
            if (row[5] != null) {
                rates.setSslineNo((String) row[5]);
            }
            if (row[6] != null) {
                rates.setComNum((String) row[6]);
            }
            if (row[10] != null) {
                rates.setFclCostId((Integer) row[10]);
            }
            if (row[11] != null) {
                rates.setCostId((Integer) row[11]);
            }
            if (row[12] != null) {
                rates.setCostType((Integer) row[12]);
            }
            if (row[13] != null) {
                rates.setFclCostTypeId((Integer) row[13]);
            }
            if (row[14] != null) {
                rates.setUnitType((Integer) row[14]);
            }
            if (row[15] != null) {
                String a = ((BigDecimal) row[15]).toString();
                rates.setActiveAmt(Double.parseDouble(a));
            }
            if (row[16] != null) {
                String a = ((BigDecimal) row[16]).toString();
                rates.setCtcAmt(Double.parseDouble(a));
            }
            if (row[17] != null) {
                String a = ((BigDecimal) row[17]).toString();
                rates.setFtfAmt(Double.parseDouble(a));
            }
            if (row[18] != null) {
                String a = ((BigDecimal) row[18]).toString();
                rates.setMinimumAmt(Double.parseDouble(a));
            }
            if (row[19] != null) {
                String a = ((BigDecimal) row[19]).toString();
                rates.setRatAmount(Double.parseDouble(a));
            }
            if (row[20] != null) {
                Character a = (Character) row[20];
                rates.setStandard(a.toString());
            }
            if (row[21] != null) {
                String a = ((BigDecimal) row[21]).toString();
                rates.setMarkUp(Double.parseDouble(a));
            }
            if (row[22] != null) {
                rates.setCurrencyType((Integer) row[22]);
            }
            if (row[23] != null) {
                rates.setCodeDesc((String) row[23]);
            }
            if (row[24] != null) {
                rates.setRateFclCostId((Integer) row[24]);
            }
            if (row[25] != null) {
                rates.setPortOfExit((String) row[25]);
            }
            if (row[26] != null) {
                rates.setGenericCodeId((Integer) row[26]);
            }
            if (row[27] != null) {
                rates.setCodeTypeId((Integer) row[27]);
            }
            if (row[28] != null) {
                rates.setCode((String) row[28]);
            }
            if (row[29] != null) {
                rates.setCodeDesc1((String) row[29]);
            }
            if (row[30] != null) {
                rates.setAccountNumber((String) row[30]);
            }
            if (row[31] != null) {
                rates.setAccountName((String) row[31]);
            }

            ratesList.add(rates);
        }
        String temp = "";
        int i = 0;
        int k = 0;
        boolean flag = false;
        LinkedList linkedList = new LinkedList();
        List newList = new ArrayList();
        String unitTyep = "";
        for (Iterator iterator = ratesList.iterator(); iterator.hasNext();) {
            Rates tempRates = (Rates) iterator.next();
            if (!temp.equals(tempRates.getSslineNo())) {
                flag = false;
                k = i;
            }
            if (tempRates.getCostId().toString().equals("11694")) {
                flag = true;
                linkedList.add(k, tempRates);
            } else if (tempRates.getCostId().toString().equals("11588")) {
                if (flag) {
                    linkedList.add(k + 1, tempRates);
                } else {
                    linkedList.add(k, tempRates);
                }
            } else if (tempRates.getCostId().toString().equals("11390") || tempRates.getCostId().toString().equals("11668") || tempRates.getCostId().toString().equals("11414")) {
                if (flag) {
                    linkedList.add(k + 1, tempRates);
                } else {
                    linkedList.add(k, tempRates);
                }
            } else {
                linkedList.add(tempRates);
            }
            temp = tempRates.getSslineNo();
            i++;
        }
        newList.addAll(linkedList);

        return newList;
    }

    public List<Rates> findRates1(String originTerminal, String destAirPort) {
        String queryString5 = "select * from rates_list rates, genericcode_dup code, trading_partner where "
                + "origin_terminal='" + originTerminal + "' and destination_port='" + destAirPort + "' and  "
                + "rates.cost_type=code.id and rates.ssline_no=trading_partner.acct_no order by rates.ssline_no";
        List queryObj = getCurrentSession().createSQLQuery(queryString5).list();
        Iterator itr = queryObj.iterator();
        List<Rates> ratesList = new ArrayList<Rates>();
        Rates rates = new Rates();
        while (itr.hasNext()) {
            Object[] row = (Object[]) itr.next();
            rates = new Rates();
            if (row[0] != null) {
                rates.setFclStdId((Integer) row[0]);
            }
            if (row[1] != null) {
                rates.setStartDate((Date) row[1]);
            }
            if (row[2] != null) {
                rates.setEndDate((Date) row[2]);
            }
            if (row[3] != null) {
                rates.setOriginTerminal((String) row[3]);
            }
            if (row[4] != null) {
                rates.setDestinationPort((String) row[4]);
            }
            if (row[5] != null) {
                rates.setSslineNo((String) row[5]);
            }
            if (row[6] != null) {
                rates.setComNum((String) row[6]);
            }
            if (row[10] != null) {
                rates.setFclCostId((Integer) row[10]);
            }
            if (row[11] != null) {
                rates.setCostId((Integer) row[11]);
            }
            if (row[12] != null) {
                rates.setCostType((Integer) row[12]);
            }
            if (row[13] != null) {
                rates.setFclCostTypeId((Integer) row[13]);
            }
            if (row[14] != null) {
                rates.setUnitType((Integer) row[14]);
            }
            if (row[15] != null) {
                String a = ((BigDecimal) row[15]).toString();
                rates.setActiveAmt(Double.parseDouble(a));
            }
            if (row[16] != null) {
                String a = ((BigDecimal) row[16]).toString();
                rates.setCtcAmt(Double.parseDouble(a));
            }
            if (row[17] != null) {
                String a = ((BigDecimal) row[17]).toString();
                rates.setFtfAmt(Double.parseDouble(a));
            }
            if (row[18] != null) {
                String a = ((BigDecimal) row[18]).toString();
                rates.setMinimumAmt(Double.parseDouble(a));
            }
            if (row[19] != null) {
                String a = ((BigDecimal) row[19]).toString();
                rates.setRatAmount(Double.parseDouble(a));
            }
            if (row[20] != null) {
                Character a = (Character) row[20];
                rates.setStandard(a.toString());
            }
            if (row[21] != null) {
                String a = ((BigDecimal) row[21]).toString();
                rates.setMarkUp(Double.parseDouble(a));
            }
            if (row[22] != null) {
                rates.setCurrencyType((Integer) row[22]);
            }
            if (row[23] != null) {
                rates.setCodeDesc((String) row[23]);
            }
            if (row[24] != null) {
                rates.setRateFclCostId((Integer) row[24]);
            }
            if (row[25] != null) {
                rates.setPortOfExit((String) row[25]);
            }
            if (row[26] != null) {
                rates.setGenericCodeId((Integer) row[26]);
            }
            if (row[27] != null) {
                rates.setCodeTypeId((Integer) row[27]);
            }
            if (row[28] != null) {
                rates.setCode((String) row[28]);
            }
            if (row[29] != null) {
                rates.setCodeDesc1((String) row[29]);
            }
            if (row[38] != null) {
                rates.setAccountNumber((String) row[38]);
            }
            if (row[39] != null) {
                rates.setAccountName((String) row[39]);
            }
            ratesList.add(rates);
        }
        String temp = "";
        int i = 0;
        int k = 0;
        LinkedList linkedList = new LinkedList();
        List newList = new ArrayList();
        String unitTyep = "";
        for (Iterator iterator = ratesList.iterator(); iterator.hasNext();) {
            Rates tempRates = (Rates) iterator.next();
            if (!temp.equals(tempRates.getSslineNo())) {
                k = i;
            }
            if (tempRates.getCostId().toString().equals("11694")) {
                linkedList.add(k, tempRates);
            } else {
                linkedList.add(tempRates);
            }
            temp = tempRates.getSslineNo();
            i++;
        }
        newList.addAll(linkedList);
        return newList;
    }

    public List<Rates> findRates2(String originTerminal, String destAirPort, String comCode, String carrier,
            String quoteDate, String bookingDate) throws Exception {
        Integer term = (null != originTerminal) ? Integer.parseInt(originTerminal) : 0;
        Integer dest = (null != destAirPort) ? Integer.parseInt(destAirPort) : 0;
        Integer com = (null != comCode) ? Integer.parseInt(comCode) : 0;
        String queryString5 = "from FclBuy where "
                + "originTerminal.id='" + term + "' and destinationPort.id='" + dest + "' and comNum.id='" + com + "' and sslineNo.accountno='" + carrier + "' "
                + " order by sslineNo.accountno";
        Query query = getCurrentSession().createQuery(queryString5);
        List queryObj = query.list();
        return queryObj;
    }

    public List findForSearchFclBuyRatesForCompressList(String originTerminal, String destAirPort, String comCode) throws Exception {
        Integer origin = Integer.parseInt(originTerminal);
        Integer dest = Integer.parseInt(destAirPort);

        String queryString5 = "from FclBuy where originTerminal.id=" + origin + " and destinationPort.id=" + dest + " and comNum.id=" + comCode + " order by sslineNo.accountName";
        List queryObj = getCurrentSession().createQuery(queryString5).list();
        return queryObj;
    }

    public List findForSearchFclBuyRatesForCompressList2(String originTerminal, String destAirPort, String comCode, String ssline) throws Exception {
//        if (!comCode.equals("1703")) {
//            comCode = "1703";
//        }
        String queryString5 = "from FclBuy where originTerminal.id='" + originTerminal + "' and destinationPort.id='" + destAirPort + "' and comNum.id='" + comCode + "' and sslineNo.accountno='" + ssline + "'  order by sslineNo.accountName";
        List queryObj = getCurrentSession().createQuery(queryString5).list();
        return queryObj;
    }

    public List getSslineId(List fclcostidList, Date noOfDays, MessageResources messageResources) throws Exception {
        String addedUnitType = ",";
        for (int l = 0; l < fclcostidList.size(); l++) {
            FclBuyCost fclcostid = (FclBuyCost) fclcostidList.get(l);

            List fclBuyCostTypeList = new ArrayList();
            String queryString5 = "from FclBuyCostTypeRates fbcr where fbcr.fclCostId='" + fclcostid.getFclCostId() + "'";
            List queryObj = getCurrentSession().createQuery(queryString5).list();
            for (Iterator iter = queryObj.iterator(); iter.hasNext();) {
                FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iter.next();
                if (fclBuyCostTypeRates.getUnitType() != null) {
                    if (addedUnitType.indexOf("," + fclBuyCostTypeRates.getUnitType().getCodedesc() + ",") == -1) {
                        addedUnitType += fclBuyCostTypeRates.getUnitType().getCodedesc() + ",";
                    }
                }
                List fclOrgDestMiscDataList = fclOrgDestMiscDataDAO.getorgdestmiscdate(fclcostid.getOrgTerminalName(), fclcostid.getDestinationPortName(), fclcostid.getSsLineName());
                if (fclOrgDestMiscDataList.size() > 0) {
                    FclOrgDestMiscData fclOrgDestMiscData = (FclOrgDestMiscData) fclOrgDestMiscDataList.get(0);
                    if (fclOrgDestMiscData.getDaysInTransit() != null) {
                        fclcostid.setTransitTime(fclOrgDestMiscData.getDaysInTransit().toString());
                    }
                    if (fclOrgDestMiscData.getRemarks() != null) {
                        fclcostid.setOrgDestCarrierRemarks(fclOrgDestMiscData.getRemarks());
                    }
                    if (null != fclOrgDestMiscData.getPoe() && !fclOrgDestMiscData.getPoe().equalsIgnoreCase("")) {
                        fclcostid.setPortOfExit(setPortOfExit(fclOrgDestMiscData.getPoe()));
                    }
                }
                if (fclBuyCostTypeRates.getUnitType() != null) {
                    fclBuyCostTypeRates.setUnitname(fclBuyCostTypeRates.getUnitType().getCodedesc());
                }
                fclcostid.setRetail(fclBuyCostTypeRates.getRatAmount());
                boolean flag = false;
                for (Iterator iterator = fclBuyCostTypeList.iterator(); iterator.hasNext();) {
                    FclBuyCostTypeRates newFclBuyCostTypeRates = (FclBuyCostTypeRates) iterator.next();
                    if (newFclBuyCostTypeRates.getUnitType() != null && fclBuyCostTypeRates.getUnitType() != null
                            && newFclBuyCostTypeRates.getUnitType().getId().toString().equals(fclBuyCostTypeRates.getUnitType().getId().toString())) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    fclBuyCostTypeList.add(fclBuyCostTypeRates);
                }
            }
            fclcostid.setUnitTypeList(fclBuyCostTypeList);
            fclcostid.setCostCode(fclcostid.getCostId().getCodedesc());
        }
        List tempFclBuyCostList = new ArrayList();
        for (Iterator iter = fclcostidList.iterator(); iter.hasNext();) {
            FclBuyCost fclBuyCost = (FclBuyCost) iter.next();
            addEmptyUnitTypes(fclBuyCost, addedUnitType);
            tempFclBuyCostList.add(fclBuyCost);
        }
        return tempFclBuyCostList;
    }

    public List getSslineId3(List ratesList, Date noOfDays, MessageResources messageResources, String hazmat) throws Exception {
        String addedUnitType = ",";
        HashMap map = new HashMap();
        HashMap fclBuyMap = new HashMap();
        List fclBuyCostList = new ArrayList();
        String OFR = messageResources.getMessage("OceanFreight");
        String oceanFreight = messageResources.getMessage("OceanFreightPopUp");
        String interModel = messageResources.getMessage("Intermodel");
        String interModelRate = "";
        String consolidatorRates[] = new String[10];
        if (OFR.indexOf(",") != -1) {
            consolidatorRates = OFR.split(",");
        }
        String interModelRates[] = new String[10];
        if (interModel.indexOf(",") != -1) {
            interModelRates = interModel.split(",");
        }
        String temp = "";
        FclBuyCost fclBuyCost = new FclBuyCost();
        for (int l = 0; l < ratesList.size(); l++) {
            Rates rates = (Rates) ratesList.get(l);
            String code = (String) GenericCodeCacheManager.getCode(rates.getCostId());
            String codeDesc = (String) GenericCodeCacheManager.getCodeDesc(rates.getCostId());
            String unitType = "";
            if (rates.getUnitType() != null) {
                unitType = (String) GenericCodeCacheManager.getCodeDesc(rates.getUnitType());
                if (unitType != null) {
                    if (addedUnitType.indexOf("," + unitType + ",") == -1) {
                        addedUnitType += unitType + ",";
                    }
                }
            }
            boolean flag1 = false;
            boolean interModelFlag = false;
            if (hazmat != null && hazmat.equals("N") && (codeDesc.trim().equals(messageResources.getMessage("hazardousSurcharge"))
                    || codeDesc.trim().equals(messageResources.getMessage("hazardousSurchargeland"))
                    || codeDesc.trim().equals(messageResources.getMessage("hazardousSurchargesea"))
                    || codeDesc.trim().equalsIgnoreCase(messageResources.getMessage("hazfeecertun"))
                    || codeDesc.trim().equalsIgnoreCase(messageResources.getMessage("hazardcertificate"))
                    || code.trim().equalsIgnoreCase(messageResources.getMessage("hazardous"))
                    || codeDesc.trim().equalsIgnoreCase(messageResources.getMessage("hazardousfee")))) {
            } else if (fclBuyMap.get(rates.getCostId() + "-" + rates.getAccountNumber()) == null) {
                fclBuyCost = new FclBuyCost();
                fclBuyCost.setSsLineName(tradingPartnerDAO.findById1(rates.getAccountNumber()));
                //fclBuyCost.setPortOfExit(setPortOfExit(rates.getPortOfExit()));
                fclBuyCost.setCommodityCodeDesc((String) GenericCodeCacheManager.getCode(Integer.parseInt(rates.getComNum())));
                if (fclBuyCost.getSsLineName() != null && !temp.equals("")
                        && !fclBuyCost.getSsLineName().getAccountName().equals(temp)) {
                    FclBuyCost fclBuyCostNew = new FclBuyCost();
                    fclBuyCostNew.setTempRate("A");
                    fclBuyCostList.add(fclBuyCostNew);
                }
                temp = fclBuyCost.getSsLineName().getAccountName();
                String costCode = (String) GenericCodeCacheManager.getCode(rates.getCostId());
                for (int i = 0; i < consolidatorRates.length; i++) {
                    if (costCode.equals(consolidatorRates[i])) {
                        flag1 = true;
                        break;
                    }
                }
                for (int i = 0; i < interModelRates.length; i++) {
                    if (costCode.equals(interModelRates[i])) {
                        interModelFlag = true;
                        break;
                    }
                }

                fclBuyCost.setCostCode((String) GenericCodeCacheManager.getCodeDesc(rates.getCostId()));
                fclBuyCost.setFclCostId(rates.getFclCostId());
                fclBuyCost.setCostCodeDesc((String) GenericCodeCacheManager.getCode(rates.getCostId()));
                if (flag1 && !interModelFlag) {
                    boolean testFlag = false;
                    if (!testFlag) {
                        fclBuyCost.setRetail(rates.getActiveAmt());
                        List fclOrgDestMiscDataList
                                = fclOrgDestMiscDataDAO.getorgdestmiscdate1(rates.getOriginTerminal(),
                                        rates.getDestinationPort(), rates.getAccountNumber());
                        if (fclOrgDestMiscDataList.size() > 0) {
                            FclOrgDestMiscData fclOrgDestMiscData = (FclOrgDestMiscData) fclOrgDestMiscDataList.get(0);
                            if (fclOrgDestMiscData.getDaysInTransit() != null) {
                                fclBuyCost.setTransitTime(fclOrgDestMiscData.getDaysInTransit().toString());
                            }
                            if (fclOrgDestMiscData.getRemarks() != null) {
                                fclBuyCost.setOrgDestCarrierRemarks(fclOrgDestMiscData.getRemarks().trim());

                            }
                            if (null != fclOrgDestMiscData.getPoe() && !fclOrgDestMiscData.getPoe().equalsIgnoreCase("")) {
                                fclBuyCost.setPortOfExit(setPortOfExit(fclOrgDestMiscData.getPoe()));
                            }

                        }

                        List fclBuyCostTypeList = new ArrayList();
                        FclBuyCostTypeRates fclBuyCostTypeRates = new FclBuyCostTypeRates();
                        if (unitType != null) {
                            fclBuyCostTypeRates.setUnitname(unitType);
                        }
                        boolean flag = false;
                        for (Iterator iterator = ratesList.iterator(); iterator.hasNext();) {
                            Rates newFclBuyCostTypeRates = (Rates) iterator.next();
                            fclBuyCostTypeRates = new FclBuyCostTypeRates();
                            if (newFclBuyCostTypeRates.getRateFclCostId().toString().equals(rates.getFclCostId().toString())) {
                                if (map.get(newFclBuyCostTypeRates.getUnitType() + "-" + newFclBuyCostTypeRates.getCostId() + "-" + newFclBuyCostTypeRates.getAccountNumber()) == null) {
                                    if (newFclBuyCostTypeRates.getUnitType() != null) {
                                        String unitName = (String) GenericCodeCacheManager.getCodeDesc(newFclBuyCostTypeRates.getUnitType());
                                        fclBuyCostTypeRates.setUnitname(unitName);
                                    }
                                    fclBuyCostTypeRates.setActiveAmt(newFclBuyCostTypeRates.getRatAmount() + newFclBuyCostTypeRates.getMarkUp());
                                    map.put(newFclBuyCostTypeRates.getUnitType() + "-" + newFclBuyCostTypeRates.getCostId() + "-" + newFclBuyCostTypeRates.getAccountNumber(), newFclBuyCostTypeRates);
                                    fclBuyCostTypeList.add(fclBuyCostTypeRates);
                                }
                            }
                        }
                        fclBuyCost.setUnitTypeList(fclBuyCostTypeList);
                        fclBuyCostList.add(fclBuyCost);
                        fclBuyMap.put(rates.getCostId() + "-" + rates.getAccountNumber(), rates);
                    }
                } else if (interModelFlag) {
                    if (fclBuyMap.containsKey(interModelRate + "-" + rates.getAccountNumber())) {
                        boolean testFlag = false;
                        boolean tempFlag = false;
                        for (Iterator iterator = fclBuyCostList.iterator(); iterator.hasNext();) {
                            FclBuyCost tempFclBuyCost = (FclBuyCost) iterator.next();
                            if (tempFclBuyCost.getSsLineName() != null && fclBuyCost.getSsLineName() != null
                                    && tempFclBuyCost.getSsLineName().getAccountName().equals(fclBuyCost.getSsLineName().getAccountName())) {
                                for (int i = 0; i < interModelRates.length; i++) {
                                    if (tempFclBuyCost.getCostCodeDesc().equals(interModelRates[i])) {
                                        tempFlag = true;
                                        break;
                                    }
                                }
                                if (tempFlag) {
                                    if (tempFclBuyCost.getUnitTypeList() != null) {
                                        for (Iterator iterator2 = tempFclBuyCost.getUnitTypeList().iterator(); iterator2.hasNext();) {
                                            FclBuyCostTypeRates tempFclBuyCostTypeRates = (FclBuyCostTypeRates) iterator2.next();
                                            String unitName = (String) GenericCodeCacheManager.getCodeDesc(rates.getUnitType());
                                            if (tempFclBuyCostTypeRates.getUnitname().equals(unitName)) {
                                                tempFclBuyCostTypeRates.setActiveAmt(tempFclBuyCostTypeRates.getActiveAmt() + rates.getRatAmount()
                                                        + rates.getMarkUp());
                                                testFlag = true;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        fclBuyCost.setRetail(rates.getActiveAmt());
                        List fclOrgDestMiscDataList = fclOrgDestMiscDataDAO.getorgdestmiscdate1(rates.getOriginTerminal(), rates.getDestinationPort(), rates.getAccountNumber());
                        if (fclOrgDestMiscDataList.size() > 0) {
                            FclOrgDestMiscData fclOrgDestMiscData = (FclOrgDestMiscData) fclOrgDestMiscDataList.get(0);
                            if (fclOrgDestMiscData.getDaysInTransit() != null) {
                                fclBuyCost.setTransitTime(fclOrgDestMiscData.getDaysInTransit().toString());
                            }
                            if (fclOrgDestMiscData.getRemarks() != null) {
                                fclBuyCost.setOrgDestCarrierRemarks(fclOrgDestMiscData.getRemarks());
                            }
                            if (null != fclOrgDestMiscData.getPoe() && !fclOrgDestMiscData.getPoe().equalsIgnoreCase("")) {
                                fclBuyCost.setPortOfExit(setPortOfExit(fclOrgDestMiscData.getPoe()));
                            }
                        }
                        List fclBuyCostTypeList = new ArrayList();
                        FclBuyCostTypeRates fclBuyCostTypeRates = new FclBuyCostTypeRates();
                        if (unitType != null) {
                            fclBuyCostTypeRates.setUnitname(unitType);
                        }
                        boolean flag = false;
                        for (Iterator iterator = ratesList.iterator(); iterator.hasNext();) {
                            Rates newFclBuyCostTypeRates = (Rates) iterator.next();
                            fclBuyCostTypeRates = new FclBuyCostTypeRates();
                            if (newFclBuyCostTypeRates.getRateFclCostId().toString().equals(rates.getFclCostId().toString())) {
                                if (map.get(newFclBuyCostTypeRates.getUnitType() + "-" + newFclBuyCostTypeRates.getCostId() + "-" + newFclBuyCostTypeRates.getAccountNumber()) == null) {
                                    String unitName = (String) GenericCodeCacheManager.getCodeDesc(newFclBuyCostTypeRates.getUnitType());
                                    fclBuyCostTypeRates.setUnitname(unitName);
                                    fclBuyCostTypeRates.setActiveAmt(newFclBuyCostTypeRates.getRatAmount() + newFclBuyCostTypeRates.getMarkUp());

                                    map.put(newFclBuyCostTypeRates.getUnitType() + "-" + newFclBuyCostTypeRates.getCostId() + "-" + newFclBuyCostTypeRates.getAccountNumber(), newFclBuyCostTypeRates);
                                    fclBuyCostTypeList.add(fclBuyCostTypeRates);
                                }
                            }
                        }
                        fclBuyCost.setUnitTypeList(fclBuyCostTypeList);
                        fclBuyCost.setCostCode("INTERMODAL RAMP");
                        fclBuyCostList.add(fclBuyCost);
                        fclBuyMap.put(rates.getCostId() + "-" + rates.getAccountNumber(), rates);
                        interModelRate = rates.getCostId().toString();
                    }
                } else {
                    boolean testFlag = false;
                    for (Iterator iterator = fclBuyCostList.iterator(); iterator.hasNext();) {
                        FclBuyCost tempFclBuyCost = (FclBuyCost) iterator.next();
                        if (tempFclBuyCost.getSsLineName() != null && fclBuyCost.getSsLineName() != null
                                && tempFclBuyCost.getSsLineName().getAccountName().equals(fclBuyCost.getSsLineName().getAccountName())) {
                            if (tempFclBuyCost.getCostCode().equalsIgnoreCase(oceanFreight)) {
                                if (tempFclBuyCost.getUnitTypeList() != null) {
                                    for (Iterator iterator2 = tempFclBuyCost.getUnitTypeList().iterator(); iterator2.hasNext();) {
                                        FclBuyCostTypeRates tempFclBuyCostTypeRates = (FclBuyCostTypeRates) iterator2.next();
                                        if (rates.getUnitType() != null) {
                                            String unitName = (String) GenericCodeCacheManager.getCodeDesc(rates.getUnitType());
                                            if (tempFclBuyCostTypeRates.getUnitname().equals(unitName)) {
                                                tempFclBuyCostTypeRates.setActiveAmt(tempFclBuyCostTypeRates.getActiveAmt() + rates.getRatAmount()
                                                        + rates.getMarkUp());
                                                testFlag = true;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        List tempFclBuyCostList = new ArrayList();
        for (Iterator iter = fclBuyCostList.iterator(); iter.hasNext();) {
            FclBuyCost fclBuyCostNew = (FclBuyCost) iter.next();
            addEmptyUnitTypes(fclBuyCostNew, addedUnitType);
            tempFclBuyCostList.add(fclBuyCostNew);
        }

        return tempFclBuyCostList;
    }

    public List getSslineId4(List ratesList, Date noOfDays, MessageResources messageResources, String hazmat) throws Exception {
        String addedUnitType = ",";
        HashMap map = new HashMap();
        HashMap fclBuyMap = new HashMap();
        List fclBuyCostList = new ArrayList();
        String OFR = messageResources.getMessage("OceanFreight");
        String oceanFreight = messageResources.getMessage("OceanFreightPopUp");

        for (int l = 0; l < ratesList.size(); l++) {
            Rates rates = (Rates) ratesList.get(l);
            String code = (String) GenericCodeCacheManager.getCode(rates.getCostId());
            String codeDesc = (String) GenericCodeCacheManager.getCodeDesc(rates.getCostId());
            String unitType = "";
            if (rates.getUnitType() != null) {
                unitType = (String) GenericCodeCacheManager.getCodeDesc(rates.getUnitType());
                if (unitType != null) {
                    if (addedUnitType.indexOf("," + unitType + ",") == -1) {
                        addedUnitType += unitType + ",";
                    }
                }
            }
            boolean flag1 = false;
            if (hazmat != null && hazmat.equals("N") && (codeDesc.trim().equals(messageResources.getMessage("hazardousSurcharge"))
                    || codeDesc.trim().equals(messageResources.getMessage("hazardousSurchargeland"))
                    || codeDesc.trim().equals(messageResources.getMessage("hazardousSurchargesea"))
                    || codeDesc.trim().equalsIgnoreCase(messageResources.getMessage("hazfeecertun"))
                    || codeDesc.trim().equalsIgnoreCase(messageResources.getMessage("hazardcertificate"))
                    || code.trim().equalsIgnoreCase(messageResources.getMessage("hazardous"))
                    || codeDesc.trim().equalsIgnoreCase(messageResources.getMessage("hazardousfee")))) {
            } else if (fclBuyMap.get(rates.getCostId() + "-" + rates.getAccountNumber()) == null) {
                String costCode = (String) GenericCodeCacheManager.getCode(rates.getCostId());
                FclBuyCost fclBuyCost = new FclBuyCost();
                fclBuyCost.setCommodityCodeDesc((String) GenericCodeCacheManager.getCode(Integer.parseInt(rates.getComNum())));
                fclBuyCost.setSsLineName(tradingPartnerDAO.findById1(rates.getAccountNumber()));
                //fclBuyCost.setPortOfExit(setPortOfExit(rates.getPortOfExit()));
                fclBuyCost.setCostCode((String) GenericCodeCacheManager.getCodeDesc(rates.getCostId()));
                fclBuyCost.setFclCostId(rates.getFclCostId());
                if (fclBuyCost.getCostCode().equalsIgnoreCase(oceanFreight)) {
                    boolean testFlag = false;
                    if (!testFlag) {
                        fclBuyCost.setRetail(rates.getActiveAmt());
                        List fclOrgDestMiscDataList = fclOrgDestMiscDataDAO.getorgdestmiscdate1(rates.getOriginTerminal(), rates.getDestinationPort(), rates.getAccountNumber());
                        if (fclOrgDestMiscDataList.size() > 0) {
                            FclOrgDestMiscData fclOrgDestMiscData = (FclOrgDestMiscData) fclOrgDestMiscDataList.get(0);
                            if (fclOrgDestMiscData.getDaysInTransit() != null) {
                                fclBuyCost.setTransitTime(fclOrgDestMiscData.getDaysInTransit().toString());
                            }
                            if (fclOrgDestMiscData.getRemarks() != null) {
                                fclBuyCost.setOrgDestCarrierRemarks(fclOrgDestMiscData.getRemarks());
                            }
                            if (null != fclOrgDestMiscData.getPoe() && !fclOrgDestMiscData.getPoe().equalsIgnoreCase("")) {
                                fclBuyCost.setPortOfExit(setPortOfExit(fclOrgDestMiscData.getPoe()));
                            }
                        }
                        List fclBuyCostTypeList = new ArrayList();
                        FclBuyCostTypeRates fclBuyCostTypeRates = new FclBuyCostTypeRates();
                        if (unitType != null) {
                            fclBuyCostTypeRates.setUnitname(unitType);
                        }
                        boolean flag = false;
                        for (Iterator iterator = ratesList.iterator(); iterator.hasNext();) {
                            Rates newFclBuyCostTypeRates = (Rates) iterator.next();
                            fclBuyCostTypeRates = new FclBuyCostTypeRates();
                            if (newFclBuyCostTypeRates.getRateFclCostId().toString().equals(rates.getFclCostId().toString())) {
                                if (map.get(newFclBuyCostTypeRates.getUnitType() + "-" + newFclBuyCostTypeRates.getCostId() + "-" + newFclBuyCostTypeRates.getAccountNumber()) == null) {
                                    String unitName = (String) GenericCodeCacheManager.getCodeDesc(newFclBuyCostTypeRates.getUnitType());
                                    fclBuyCostTypeRates.setUnitname(unitName);
                                    fclBuyCostTypeRates.setActiveAmt(newFclBuyCostTypeRates.getRatAmount() + newFclBuyCostTypeRates.getMarkUp());
                                    map.put(newFclBuyCostTypeRates.getUnitType() + "-" + newFclBuyCostTypeRates.getCostId() + "-" + newFclBuyCostTypeRates.getAccountNumber(), newFclBuyCostTypeRates);
                                    fclBuyCostTypeList.add(fclBuyCostTypeRates);
                                }
                            }
                        }
                        fclBuyCost.setUnitTypeList(fclBuyCostTypeList);
                        fclBuyCostList.add(fclBuyCost);
                        fclBuyMap.put(rates.getCostId() + "-" + rates.getAccountNumber(), rates);
                    }
                } else {
                    boolean testFlag = false;
                    for (Iterator iterator = fclBuyCostList.iterator(); iterator.hasNext();) {
                        FclBuyCost tempFclBuyCost = (FclBuyCost) iterator.next();
                        if (tempFclBuyCost.getSsLineName().getAccountName().equals(fclBuyCost.getSsLineName().getAccountName())) {
                            if (tempFclBuyCost.getCostCode().equalsIgnoreCase(oceanFreight)) {
                                if (tempFclBuyCost.getUnitTypeList() != null) {
                                    for (Iterator iterator2 = tempFclBuyCost.getUnitTypeList().iterator(); iterator2.hasNext();) {
                                        FclBuyCostTypeRates tempFclBuyCostTypeRates = (FclBuyCostTypeRates) iterator2.next();
                                        if (rates.getUnitType() != null) {
                                            String unitName = (String) GenericCodeCacheManager.getCodeDesc(rates.getUnitType());
                                            if (tempFclBuyCostTypeRates.getUnitname() != null && tempFclBuyCostTypeRates.getUnitname().equals(unitName)) {
                                                tempFclBuyCostTypeRates.setActiveAmt(tempFclBuyCostTypeRates.getActiveAmt() + rates.getRatAmount()
                                                        + rates.getMarkUp());
                                                testFlag = true;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        List tempFclBuyCostList = new ArrayList();
        for (Iterator iter = fclBuyCostList.iterator(); iter.hasNext();) {
            FclBuyCost fclBuyCost = (FclBuyCost) iter.next();
            addEmptyUnitTypes(fclBuyCost, addedUnitType);
            tempFclBuyCostList.add(fclBuyCost);
        }

        return tempFclBuyCostList;
    }

    public List getSslineId1(List ratesList, Date noOfDays, MessageResources messageResources, String hazmat) throws Exception {
        String addedUnitType = ",";
        HashMap map = new HashMap();
        HashMap fclBuyMap = new HashMap();
        List fclBuyCostList = new ArrayList();
        String temp = "";
        FclBuyCost fclBuyCost = new FclBuyCost();
        for (int l = 0; l < ratesList.size(); l++) {
            Rates rates = (Rates) ratesList.get(l);
            String code = (String) GenericCodeCacheManager.getCodeDesc(rates.getCostId());
            String codeDesc = (String) GenericCodeCacheManager.getCodeDesc(rates.getCostId());
            String unitType = "";
            if (rates.getUnitType() != null) {
                unitType = (String) GenericCodeCacheManager.getCodeDesc(rates.getUnitType());
                if (unitType != null) {
                    if (addedUnitType.indexOf("," + unitType + ",") == -1) {
                        addedUnitType += unitType + ",";
                    }
                }
            }
            if (hazmat != null && hazmat.equals("N") && (codeDesc.trim().equals(messageResources.getMessage("hazardousSurcharge"))
                    || codeDesc.trim().equals(messageResources.getMessage("hazardousSurchargeland"))
                    || codeDesc.trim().equals(messageResources.getMessage("hazardousSurchargesea"))
                    || codeDesc.trim().equalsIgnoreCase(messageResources.getMessage("hazfeecertun"))
                    || codeDesc.trim().equalsIgnoreCase(messageResources.getMessage("hazardcertificate"))
                    || code.trim().equalsIgnoreCase(messageResources.getMessage("hazardous"))
                    || codeDesc.trim().equalsIgnoreCase(messageResources.getMessage("hazardousfee")))) {
            } else {

                if (fclBuyMap.get(rates.getCostId() + "-" + rates.getAccountNumber()) == null) {
                    fclBuyCost = new FclBuyCost();
                    fclBuyCost.setCommodityCodeDesc((String) GenericCodeCacheManager.getCode(Integer.parseInt(rates.getComNum())));
                    fclBuyCost.setSsLineName(tradingPartnerDAO.findById1(rates.getAccountNumber()));
                    //  fclBuyCost.setPortOfExit(setPortOfExit(rates.getPortOfExit()));
                    if (fclBuyCost.getSsLineName() != null && !temp.equals("")
                            && !fclBuyCost.getSsLineName().getAccountName().equals(temp)) {
                        FclBuyCost fclBuyCostNew = new FclBuyCost();
                        fclBuyCostNew.setTempRate("A");
                        fclBuyCostList.add(fclBuyCostNew);
                    }
                    temp = fclBuyCost.getSsLineName().getAccountName();
                    fclBuyCost.setCostCode((String) GenericCodeCacheManager.getCodeDesc(rates.getCostId()));
                    fclBuyCost.setFclCostId(rates.getFclCostId());
                    fclBuyCost.setRetail(rates.getActiveAmt());
                    List fclOrgDestMiscDataList = fclOrgDestMiscDataDAO.getorgdestmiscdate1(rates.getOriginTerminal(), rates.getDestinationPort(), rates.getAccountNumber());
                    if (fclOrgDestMiscDataList.size() > 0) {
                        FclOrgDestMiscData fclOrgDestMiscData = (FclOrgDestMiscData) fclOrgDestMiscDataList.get(0);
                        if (fclOrgDestMiscData.getDaysInTransit() != null) {
                            fclBuyCost.setTransitTime(fclOrgDestMiscData.getDaysInTransit().toString());
                        }
                        if (fclOrgDestMiscData.getRemarks() != null) {
                            fclBuyCost.setOrgDestCarrierRemarks(fclOrgDestMiscData.getRemarks());
                        }
                        if (null != fclOrgDestMiscData.getPoe() && !fclOrgDestMiscData.getPoe().equalsIgnoreCase("")) {
                            fclBuyCost.setPortOfExit(setPortOfExit(fclOrgDestMiscData.getPoe()));
                        }
                    }
                    List fclBuyCostTypeList = new ArrayList();
                    FclBuyCostTypeRates fclBuyCostTypeRates = new FclBuyCostTypeRates();
                    if (unitType != null) {
                        fclBuyCostTypeRates.setUnitname(unitType);
                    }
                    boolean flag = false;
                    for (Iterator iterator = ratesList.iterator(); iterator.hasNext();) {
                        Rates newFclBuyCostTypeRates = (Rates) iterator.next();
                        fclBuyCostTypeRates = new FclBuyCostTypeRates();
                        if (newFclBuyCostTypeRates.getRateFclCostId().toString().equals(rates.getFclCostId().toString())) {
                            if (map.get(newFclBuyCostTypeRates.getUnitType() + "-" + newFclBuyCostTypeRates.getCostId() + "-" + newFclBuyCostTypeRates.getAccountNumber()) == null) {
                                if (newFclBuyCostTypeRates.getUnitType() != null) {
                                    String unitName = (String) GenericCodeCacheManager.getCodeDesc(newFclBuyCostTypeRates.getUnitType());
                                    fclBuyCostTypeRates.setUnitname(unitName);
                                }
                                fclBuyCostTypeRates.setMarkup(newFclBuyCostTypeRates.getMarkUp());
                                fclBuyCostTypeRates.setActiveAmt(newFclBuyCostTypeRates.getRatAmount());
                                map.put(newFclBuyCostTypeRates.getUnitType() + "-" + newFclBuyCostTypeRates.getCostId() + "-" + newFclBuyCostTypeRates.getAccountNumber(), newFclBuyCostTypeRates);
                                fclBuyCostTypeList.add(fclBuyCostTypeRates);
                            }
                        }
                    }
                    fclBuyCost.setUnitTypeList(fclBuyCostTypeList);
                    fclBuyCostList.add(fclBuyCost);
                    fclBuyMap.put(rates.getCostId() + "-" + rates.getAccountNumber(), rates);
                }
            }
        }
        List tempFclBuyCostList = new ArrayList();
        for (Iterator iter = fclBuyCostList.iterator(); iter.hasNext();) {
            FclBuyCost fclBuyCostNew = (FclBuyCost) iter.next();
            addEmptyUnitTypes(fclBuyCostNew, addedUnitType);
            tempFclBuyCostList.add(fclBuyCostNew);
        }
        return tempFclBuyCostList;
    }

    public List getSslineIdForBooking(List ratesList, MessageResources messageResources, String hazmat, HashMap hashMap, HashMap unitHashMap, HttpServletRequest request, Quotation quotation, List otherCommodityList, String region) throws Exception {
        String addedUnitType = ",";
        boolean newFlag = false;
        List tempFclBuyList = new ArrayList();
        String addsub = null;
        Double markup1 = null;
        String costCode = null;
        Double markup2 = null;
        if (CommonUtils.isNotEmpty(otherCommodityList)) {
            for (Object row : otherCommodityList) {
                Object[] cols = (Object[]) row;
                addsub = null != cols[0] ? cols[0].toString() : "";
                markup1 = null != cols[1] ? Double.parseDouble(cols[1].toString()) : 0;
                costCode = null != cols[2] ? new GenericCodeDAO().getFieldByCodeAndCodetypeId("Cost Code", cols[2].toString(), "codedesc") : "";
                markup2 = null != cols[4] ? Double.parseDouble(cols[4].toString()) : 0;
                break;
            }
        }
        if(quotation.getMultiQuoteFlag() == null){
        for (Iterator iterator = ratesList.iterator(); iterator.hasNext();) {
            FclBuy fclBuy = (FclBuy) iterator.next();
            if (fclBuy.getFclBuyCostsSet() != null) {
                Iterator iter = fclBuy.getFclBuyCostsSet().iterator();
                while (iter.hasNext()) {
                    FclBuyCost fclBuyCost = (FclBuyCost) iter.next();
                    FclBuyCost newFclBuyCost = new FclBuyCost();
                    if (hazmat != null && hazmat.equals("N") && (fclBuyCost.getCostId().getCodedesc().trim().equals(messageResources.getMessage("hazardousSurcharge"))
                            || fclBuyCost.getCostId().getCodedesc().trim().equals(messageResources.getMessage("hazardousSurchargeland"))
                            || fclBuyCost.getCostId().getCodedesc().trim().equals(messageResources.getMessage("hazardousSurchargesea"))
                            || fclBuyCost.getCostId().getCodedesc().trim().equalsIgnoreCase(messageResources.getMessage("hazfeecertun"))
                            || fclBuyCost.getCostId().getCodedesc().trim().equalsIgnoreCase(messageResources.getMessage("hazardcertificate"))
                            || fclBuyCost.getCostId().getCode().trim().equalsIgnoreCase(messageResources.getMessage("hazardous"))
                            || fclBuyCost.getCostId().getCodedesc().trim().equalsIgnoreCase(messageResources.getMessage("hazardousfee")))) {
                    } else {

                            if (fclBuyCost.getFclBuyUnitTypesSet() != null) {
                                List unitTypeList = new ArrayList();
                                boolean flag = false;
                                for (Iterator iterator2 = fclBuyCost.getFclBuyUnitTypesSet().iterator(); iterator2.hasNext();) {
                                    FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iterator2.next();
                                    if (fclBuyCostTypeRates.getEffectiveDate() != null && quotation.getQuoteDate().before(fclBuyCostTypeRates.getEffectiveDate()) && fclBuyCostTypeRates.getEffectiveDate().before(new Date())) {
                                        if (fclBuyCostTypeRates.getUnitType() != null) {
                                            if (hashMap.containsKey(fclBuyCost.getCostId().getCodedesc() + "-" + fclBuyCostTypeRates.getUnitType().getId().toString())) {
                                                String unitType = "";
                                                if (fclBuyCostTypeRates.getUnitType().getId() != null) {
                                                    unitType = (String) GenericCodeCacheManager.getCodeDesc(fclBuyCostTypeRates.getUnitType().getId());
                                                    if (unitType != null) {
                                                        if (!addedUnitType.contains("," + unitType + ",")) {
                                                            addedUnitType += unitType + ",";
                                                        }
                                                    }
                                                }
                                                flag = true;
                                                FclBuyCostTypeRates newFclBuyCostTypeRates = new FclBuyCostTypeRates();
                                                PropertyUtils.copyProperties(newFclBuyCostTypeRates, fclBuyCostTypeRates);
                                                if (CommonUtils.isEqualIgnoreCase(fclBuyCost.getCostId().getCodedesc(), costCode) && CommonUtils.isNotEmpty(addsub)) {
                                                    if ("A".equalsIgnoreCase(addsub)) {
                                                        if ("Y".equalsIgnoreCase(region)) {
                                                            newFclBuyCostTypeRates.setMarkup(newFclBuyCostTypeRates.getMarkup() + markup2);
                                                        } else {
                                                            newFclBuyCostTypeRates.setMarkup(newFclBuyCostTypeRates.getMarkup() + markup1);
                                                        }
                                                    } else {
                                                        if ("Y".equalsIgnoreCase(region)) {
                                                            newFclBuyCostTypeRates.setMarkup(newFclBuyCostTypeRates.getMarkup() - markup2);
                                                        } else {
                                                            newFclBuyCostTypeRates.setMarkup(newFclBuyCostTypeRates.getMarkup() - markup1);
                                                        }
                                                    }
                                                }
                                                Charges charges = (Charges) hashMap.get(fclBuyCost.getCostId().getCodedesc() + "-" + fclBuyCostTypeRates.getUnitType().getId().toString());
                                                if (newFclBuyCostTypeRates.getActiveAmt() > charges.getAmount() || newFclBuyCostTypeRates.getMarkup() > charges.getMarkUp()) {
                                                    newFclBuyCostTypeRates.setUnitname(newFclBuyCostTypeRates.getUnitType().getCodedesc());
                                                    newFclBuyCostTypeRates.setOldAmount(charges.getAmount());
                                                    newFclBuyCostTypeRates.setOldMarkUp(charges.getMarkUp());
                                                    unitTypeList.add(newFclBuyCostTypeRates);
                                                    newFlag = true;
                                                }
                                            }
                                        } else {
                                            Charges charges = (Charges) hashMap.get(fclBuyCost.getCostId().getCodedesc());
                                            if (charges != null) {
                                                if (charges.getAmount() == null) {
                                                    charges.setAmount(0.00);
                                                }
                                                if (fclBuyCostTypeRates.getRatAmount() > charges.getAmount()) {
                                                    fclBuyCost.setRetail(charges.getAmount());
                                                    fclBuyCost.setFutureRetail(fclBuyCostTypeRates.getRatAmount());
                                                    fclBuyCost.setEffectiveDate(fclBuyCostTypeRates.getEffectiveDate());
                                                    newFlag = true;
                                                }
                                            } else {
                                                fclBuyCost.setFutureRetail(fclBuyCostTypeRates.getRatAmount());
                                                fclBuyCost.setEffectiveDate(fclBuyCostTypeRates.getEffectiveDate());
                                                newFlag = true;
                                            }
                                        }
                                    }
                                }
                                PropertyUtils.copyProperties(newFclBuyCost, fclBuyCost);
                                newFclBuyCost.setCostCode(fclBuyCost.getCostId().getCodedesc());
                                newFclBuyCost.setUnitTypeList(unitTypeList);
                                newFclBuyCost.setOrgTerminalName(fclBuy.getOriginTerminal());
                                newFclBuyCost.setDestinationPortName(fclBuy.getDestinationPort());
                                newFclBuyCost.setCommodityCode(fclBuy.getComNum());
                                newFclBuyCost.setSsLineName(fclBuy.getSslineNo());
                                if (null == newFclBuyCost.getFutureRetail()) {
                                    newFclBuyCost.setFutureRetail(0d);
                                }

                                if (unitTypeList.size() > 0 || !newFclBuyCost.getFutureRetail().equals(0.0)) {
                                    tempFclBuyList.add(newFclBuyCost);
                                }
                                if (!flag) {
                                    List tempList = new ArrayList();
                                    for (Iterator iterator2 = fclBuyCost.getFclBuyUnitTypesSet().iterator(); iterator2.hasNext();) {
                                        FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iterator2.next();
                                        if (fclBuyCostTypeRates.getEffectiveDate() != null && quotation.getQuoteDate().before(fclBuyCostTypeRates.getEffectiveDate()) && fclBuyCostTypeRates.getEffectiveDate().before(new Date())) {
                                            if (fclBuyCostTypeRates.getUnitType() != null) {
                                                if (unitHashMap.containsKey(fclBuyCostTypeRates.getUnitType().getId().toString())) {
                                                    fclBuyCostTypeRates.setUnitname(fclBuyCostTypeRates.getUnitType().getCodedesc());
                                                    tempList.add(fclBuyCostTypeRates);
                                                }
                                            }
                                        }
                                    }
                                    if (tempList.size() > 0) {
                                        newFlag = true;
                                        newFclBuyCost.setUnitTypeList(tempList);
                                        tempFclBuyList.add(newFclBuyCost);
                                    }
                                }
                            }
                       
                    }

                    }
                }
            }
    }
        else if(quotation.getMultiQuoteFlag() != null){
           for (Iterator iterator = ratesList.iterator(); iterator.hasNext();) {
            FclBuy fclBuy = (FclBuy) iterator.next();
            if (fclBuy.getFclBuyCostsSet() != null) {
                Iterator iter = fclBuy.getFclBuyCostsSet().iterator();
                while (iter.hasNext()) {
                    FclBuyCost fclBuyCost = (FclBuyCost) iter.next();
                    FclBuyCost newFclBuyCost = new FclBuyCost();
                    if (hazmat != null && hazmat.equals("N") && (fclBuyCost.getCostId().getCodedesc().trim().equals(messageResources.getMessage("hazardousSurcharge"))
                            || fclBuyCost.getCostId().getCodedesc().trim().equals(messageResources.getMessage("hazardousSurchargeland"))
                            || fclBuyCost.getCostId().getCodedesc().trim().equals(messageResources.getMessage("hazardousSurchargesea"))
                            || fclBuyCost.getCostId().getCodedesc().trim().equalsIgnoreCase(messageResources.getMessage("hazfeecertun"))
                            || fclBuyCost.getCostId().getCodedesc().trim().equalsIgnoreCase(messageResources.getMessage("hazardcertificate"))
                            || fclBuyCost.getCostId().getCode().trim().equalsIgnoreCase(messageResources.getMessage("hazardous"))
                            || fclBuyCost.getCostId().getCodedesc().trim().equalsIgnoreCase(messageResources.getMessage("hazardousfee")))) {
                    } else {

                            if (fclBuyCost.getFclBuyUnitTypesSet() != null) {
                                List unitTypeList = new ArrayList();
                                boolean flag = false;
                                for (Iterator iterator2 = fclBuyCost.getFclBuyUnitTypesSet().iterator(); iterator2.hasNext();) {
                                    FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iterator2.next();
                                    if (fclBuyCostTypeRates.getEffectiveDate() != null && quotation.getQuoteDate().before(fclBuyCostTypeRates.getEffectiveDate()) && fclBuyCostTypeRates.getEffectiveDate().before(new Date())) {
                                        if (fclBuyCostTypeRates.getUnitType() != null) {
                                            if (hashMap.containsKey(fclBuyCost.getCostId().getCodedesc() + "-" + fclBuyCostTypeRates.getUnitType().getId().toString())) {
                                                String unitType = "";
                                                if (fclBuyCostTypeRates.getUnitType().getId() != null) {
                                                    unitType = (String) GenericCodeCacheManager.getCodeDesc(fclBuyCostTypeRates.getUnitType().getId());
                                                    if (unitType != null) {
                                                        if (!addedUnitType.contains("," + unitType + ",")) {
                                                            addedUnitType += unitType + ",";
                                                        }
                                                    }
                                                }
                                                flag = true;
                                                FclBuyCostTypeRates newFclBuyCostTypeRates = new FclBuyCostTypeRates();
                                                PropertyUtils.copyProperties(newFclBuyCostTypeRates, fclBuyCostTypeRates);
                                                if (CommonUtils.isEqualIgnoreCase(fclBuyCost.getCostId().getCodedesc(), costCode) && CommonUtils.isNotEmpty(addsub)) {
                                                    if ("A".equalsIgnoreCase(addsub)) {
                                                        if ("Y".equalsIgnoreCase(region)) {
                                                            newFclBuyCostTypeRates.setMarkup(newFclBuyCostTypeRates.getMarkup() + markup2);
                                                        } else {
                                                            newFclBuyCostTypeRates.setMarkup(newFclBuyCostTypeRates.getMarkup() + markup1);
                                                        }
                                                    } else {
                                                        if ("Y".equalsIgnoreCase(region)) {
                                                            newFclBuyCostTypeRates.setMarkup(newFclBuyCostTypeRates.getMarkup() - markup2);
                                                        } else {
                                                            newFclBuyCostTypeRates.setMarkup(newFclBuyCostTypeRates.getMarkup() - markup1);
                                                        }
                                                    }
                                                }
                                                MultiQuoteCharges mQcharges = (MultiQuoteCharges) hashMap.get(fclBuyCost.getCostId().getCodedesc() + "-" + fclBuyCostTypeRates.getUnitType().getId().toString());
                                                if (newFclBuyCostTypeRates.getActiveAmt() > mQcharges.getAmount() || newFclBuyCostTypeRates.getMarkup() > mQcharges.getMarkUp()) {
                                                    newFclBuyCostTypeRates.setUnitname(newFclBuyCostTypeRates.getUnitType().getCodedesc());
                                                    newFclBuyCostTypeRates.setOldAmount(mQcharges.getAmount());
                                                    newFclBuyCostTypeRates.setOldMarkUp(mQcharges.getMarkUp());
                                                    unitTypeList.add(newFclBuyCostTypeRates);
                                                    newFlag = true;
                                                }
                                            }
                                        } else {
                                            MultiQuoteCharges mQcharges = (MultiQuoteCharges) hashMap.get(fclBuyCost.getCostId().getCodedesc());
                                            if (mQcharges != null) {
                                                if (mQcharges.getAmount() == null) {
                                                    mQcharges.setAmount(0.00);
                                                }
                                                if (fclBuyCostTypeRates.getRatAmount() > mQcharges.getAmount()) {
                                                    fclBuyCost.setRetail(mQcharges.getAmount());
                                                    fclBuyCost.setFutureRetail(fclBuyCostTypeRates.getRatAmount());
                                                    fclBuyCost.setEffectiveDate(fclBuyCostTypeRates.getEffectiveDate());
                                                    newFlag = true;
                                                }
                                            } else {
                                                fclBuyCost.setFutureRetail(fclBuyCostTypeRates.getRatAmount());
                                                fclBuyCost.setEffectiveDate(fclBuyCostTypeRates.getEffectiveDate());
                                                newFlag = true;
                                            }
                                        }
                                    }
                                }
                                PropertyUtils.copyProperties(newFclBuyCost, fclBuyCost);
                                newFclBuyCost.setCostCode(fclBuyCost.getCostId().getCodedesc());
                                newFclBuyCost.setUnitTypeList(unitTypeList);
                                newFclBuyCost.setOrgTerminalName(fclBuy.getOriginTerminal());
                                newFclBuyCost.setDestinationPortName(fclBuy.getDestinationPort());
                                newFclBuyCost.setCommodityCode(fclBuy.getComNum());
                                newFclBuyCost.setSsLineName(fclBuy.getSslineNo());
                                if (null == newFclBuyCost.getFutureRetail()) {
                                    newFclBuyCost.setFutureRetail(0d);
                                }

                                if (unitTypeList.size() > 0 || !newFclBuyCost.getFutureRetail().equals(0.0)) {
                                    tempFclBuyList.add(newFclBuyCost);
                                }
                                if (!flag) {
                                    List tempList = new ArrayList();
                                    for (Iterator iterator2 = fclBuyCost.getFclBuyUnitTypesSet().iterator(); iterator2.hasNext();) {
                                        FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iterator2.next();
                                        if (fclBuyCostTypeRates.getEffectiveDate() != null && quotation.getQuoteDate().before(fclBuyCostTypeRates.getEffectiveDate()) && fclBuyCostTypeRates.getEffectiveDate().before(new Date())) {
                                            if (fclBuyCostTypeRates.getUnitType() != null) {
                                                if (unitHashMap.containsKey(fclBuyCostTypeRates.getUnitType().getId().toString())) {
                                                    fclBuyCostTypeRates.setUnitname(fclBuyCostTypeRates.getUnitType().getCodedesc());
                                                    tempList.add(fclBuyCostTypeRates);
                                                }
                                            }
                                        }
                                    }
                                    if (tempList.size() > 0) {
                                        newFlag = true;
                                        newFclBuyCost.setUnitTypeList(tempList);
                                        tempFclBuyList.add(newFclBuyCost);
                                    }
                                }
                            }
                       
                    }

                    }
                }
            }
          }
        List tempFclBuyCostList = new ArrayList();
        for (Iterator iter = tempFclBuyList.iterator(); iter.hasNext();) {
            FclBuyCost fclBuyCost = (FclBuyCost) iter.next();
            addEmptyUnitTypes(fclBuyCost, addedUnitType);
            tempFclBuyCostList.add(fclBuyCost);
        }
        if (!newFlag) {
            request.setAttribute("msg", "BkgRates");
            List ratesPopupList = new ArrayList();
            ratesPopupList.add("");
            ratesPopupList.add("");
            ratesPopupList.add("");
            ratesPopupList.add("");
            ratesPopupList.add("");
            ratesPopupList.add("");
            ratesPopupList.add("oldgetRatesBKG");
            request.setAttribute("ratesPopupList", ratesPopupList);
        }
        return tempFclBuyList;
    }

    public List getFclblChargesForBooking(List ratesList, MessageResources messageResources, String hazmat,
            HashMap<String, BookingfclUnits> hashMap, HashMap<Integer, Integer> unitHashMap,
            HttpServletRequest request, Quotation quotation, List otherCommodityList, String region) throws Exception {
        String addedUnitType = ",";
        boolean newFlag = false;
        List tempFclBuyList = new ArrayList();
        String addsub = null;
        Double markup1 = null;
        String costCode = null;
        Double markup2 = null;
        if (CommonUtils.isNotEmpty(otherCommodityList)) {
            for (Object row : otherCommodityList) {
                Object[] cols = (Object[]) row;
                addsub = null != cols[0] ? cols[0].toString() : "";
                markup1 = null != cols[1] ? Double.parseDouble(cols[1].toString()) : 0;
                costCode = null != cols[2] ? new GenericCodeDAO().getFieldByCodeAndCodetypeId("Cost Code", cols[2].toString(), "codedesc") : "";
                markup2 = null != cols[4] ? Double.parseDouble(cols[4].toString()) : 0;
                break;
            }
        }
        for (Iterator iterator = ratesList.iterator(); iterator.hasNext();) {
            FclBuy fclBuy = (FclBuy) iterator.next();
            if (fclBuy.getFclBuyCostsSet() != null) {
                Iterator iter = fclBuy.getFclBuyCostsSet().iterator();
                while (iter.hasNext()) {
                    FclBuyCost fclBuyCost = (FclBuyCost) iter.next();
                    FclBuyCost newFclBuyCost = new FclBuyCost();
                    if (hazmat != null && hazmat.equals("N") && (fclBuyCost.getCostId().getCodedesc().trim().equals(messageResources.getMessage("hazardousSurcharge"))
                            || fclBuyCost.getCostId().getCodedesc().trim().equals(messageResources.getMessage("hazardousSurchargeland"))
                            || fclBuyCost.getCostId().getCodedesc().trim().equals(messageResources.getMessage("hazardousSurchargesea"))
                            || fclBuyCost.getCostId().getCodedesc().trim().equalsIgnoreCase(messageResources.getMessage("hazfeecertun"))
                            || fclBuyCost.getCostId().getCodedesc().trim().equalsIgnoreCase(messageResources.getMessage("hazardcertificate"))
                            || fclBuyCost.getCostId().getCode().trim().equalsIgnoreCase(messageResources.getMessage("hazardous"))
                            || fclBuyCost.getCostId().getCodedesc().trim().equalsIgnoreCase(messageResources.getMessage("hazardousfee")))) {
                    } else {
                        if (fclBuyCost.getFclBuyUnitTypesSet() != null) {
                            List unitTypeList = new ArrayList();
                            boolean flag = false;
                            for (Iterator iterator2 = fclBuyCost.getFclBuyUnitTypesSet().iterator(); iterator2.hasNext();) {
                                FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iterator2.next();
                                //  taking quote date........
                                if (fclBuyCostTypeRates.getEffectiveDate() != null
                                        && quotation.getQuoteDate().before(fclBuyCostTypeRates.getEffectiveDate())
                                        && fclBuyCostTypeRates.getEffectiveDate().before(new Date())) {
                                    if (fclBuyCostTypeRates.getUnitType() != null) {
                                        if (hashMap.containsKey(fclBuyCost.getCostId().getCodedesc() + "-" + fclBuyCostTypeRates.getUnitType().getId().toString())) {
                                            String unitType = "";
                                            if (fclBuyCostTypeRates.getUnitType().getId() != null) {
                                                unitType = (String) GenericCodeCacheManager.getCodeDesc(fclBuyCostTypeRates.getUnitType().getId());
                                                if (unitType != null) {
                                                    if (!addedUnitType.contains("," + unitType + ",")) {
                                                        addedUnitType += unitType + ",";
                                                    }
                                                }
                                            }
                                            flag = true;
                                            FclBuyCostTypeRates newFclBuyCostTypeRates = new FclBuyCostTypeRates();
                                            PropertyUtils.copyProperties(newFclBuyCostTypeRates, fclBuyCostTypeRates);
                                            if (CommonUtils.isEqualIgnoreCase(fclBuyCost.getCostId().getCodedesc(), costCode) && CommonUtils.isNotEmpty(addsub)) {
                                                if ("A".equalsIgnoreCase(addsub)) {
                                                    if ("Y".equalsIgnoreCase(region)) {
                                                        newFclBuyCostTypeRates.setMarkup(newFclBuyCostTypeRates.getMarkup() + markup2);
                                                    } else {
                                                        newFclBuyCostTypeRates.setMarkup(newFclBuyCostTypeRates.getMarkup() + markup1);
                                                    }
                                                } else {
                                                    if ("Y".equalsIgnoreCase(region)) {
                                                        newFclBuyCostTypeRates.setMarkup(newFclBuyCostTypeRates.getMarkup() - markup2);
                                                    } else {
                                                        newFclBuyCostTypeRates.setMarkup(newFclBuyCostTypeRates.getMarkup() - markup1);
                                                    }
                                                }
                                            }
                                            BookingfclUnits bookingfclUnits = (BookingfclUnits) hashMap.get(fclBuyCost.getCostId().getCodedesc() + "-" + newFclBuyCostTypeRates.getUnitType().getId().toString());
                                            if (newFclBuyCostTypeRates.getActiveAmt().compareTo(bookingfclUnits.getAmount()) != 0 || newFclBuyCostTypeRates.getMarkup().compareTo(bookingfclUnits.getMarkUp()) != 0) {
                                                newFclBuyCostTypeRates.setUnitname(newFclBuyCostTypeRates.getUnitType().getCodedesc());
                                                newFclBuyCostTypeRates.setOldMarkUp(bookingfclUnits.getMarkUp());
                                                newFclBuyCostTypeRates.setOldAmount(bookingfclUnits.getAmount());
                                                unitTypeList.add(newFclBuyCostTypeRates);
                                                newFlag = true;
                                            }
                                        }
                                    } else {
                                        BookingfclUnits bookingfclUnits = (BookingfclUnits) hashMap.get(fclBuyCost.getCostId().getCodedesc());
                                        if (bookingfclUnits != null) {
                                            if (bookingfclUnits.getAmount() == null) {
                                                bookingfclUnits.setAmount(0.00);
                                            }
                                            if (fclBuyCostTypeRates.getRatAmount().compareTo(bookingfclUnits.getAmount()) != 0) {
                                                fclBuyCost.setRetail(bookingfclUnits.getAmount());
                                                fclBuyCost.setFutureRetail(fclBuyCostTypeRates.getRatAmount());
                                                fclBuyCost.setEffectiveDate(fclBuyCostTypeRates.getEffectiveDate());
                                                newFlag = true;
                                            }
                                        } else {
                                            fclBuyCost.setFutureRetail(fclBuyCostTypeRates.getRatAmount());
                                            fclBuyCost.setEffectiveDate(fclBuyCostTypeRates.getEffectiveDate());
                                            newFlag = true;
                                        }
                                    }
                                }
                            }
                            PropertyUtils.copyProperties(newFclBuyCost, fclBuyCost);
                            newFclBuyCost.setCostCode(fclBuyCost.getCostId().getCodedesc());
                            newFclBuyCost.setUnitTypeList(unitTypeList);
                            newFclBuyCost.setOrgTerminalName(fclBuy.getOriginTerminal());
                            newFclBuyCost.setDestinationPortName(fclBuy.getDestinationPort());
                            newFclBuyCost.setCommodityCode(fclBuy.getComNum());
                            newFclBuyCost.setSsLineName(fclBuy.getSslineNo());
                            if (null == newFclBuyCost.getFutureRetail()) {
                                newFclBuyCost.setFutureRetail(0d);
                            }
                            if (unitTypeList.size() > 0 || !newFclBuyCost.getFutureRetail().equals(0.0)) {
                                tempFclBuyList.add(newFclBuyCost);
                            }
                            if (!flag) {
                                List tempList = new ArrayList();
                                for (Iterator iterator2 = fclBuyCost.getFclBuyUnitTypesSet().iterator(); iterator2.hasNext();) {
                                    FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iterator2.next();
                                    if (fclBuyCostTypeRates.getEffectiveDate() != null
                                            && quotation.getQuoteDate().before(fclBuyCostTypeRates.getEffectiveDate())
                                            && fclBuyCostTypeRates.getEffectiveDate().before(new Date())) {
                                        if (fclBuyCostTypeRates.getUnitType() != null) {
                                            if (unitHashMap.containsKey(fclBuyCostTypeRates.getUnitType().getId().toString())) {
                                                fclBuyCostTypeRates.setUnitname(fclBuyCostTypeRates.getUnitType().getCodedesc());
                                                tempList.add(fclBuyCostTypeRates);
                                            }
                                        }
                                    }
                                }
                                if (tempList.size() > 0) {
                                    newFlag = true;
                                    newFclBuyCost.setUnitTypeList(tempList);
                                    tempFclBuyList.add(newFclBuyCost);
                                }
                            }
                        }

                    }

                }
            }
        }
        List tempFclBuyCostList = new ArrayList();
        for (Iterator iter = tempFclBuyList.iterator(); iter.hasNext();) {
            FclBuyCost fclBuyCost = (FclBuyCost) iter.next();
            addEmptyUnitTypes(fclBuyCost, addedUnitType);
            tempFclBuyCostList.add(fclBuyCost);
        }

        if (!newFlag) {
            request.setAttribute("msg", "BkgRates");
            List ratesPopupList = new ArrayList();
            ratesPopupList.add("");
            ratesPopupList.add("");
            ratesPopupList.add("");
            ratesPopupList.add("");
            ratesPopupList.add("");
            ratesPopupList.add("");
            ratesPopupList.add("oldgetRatesBKG");
            request.setAttribute("ratesPopupList", ratesPopupList);
        }
        return tempFclBuyList;
    }

    public List getCollapseListForBooking(List<FclBuyCost> fclBuyCostList, MessageResources messageResources, Map map) throws Exception {
//fclBuyCostList ==> list conating buycost objects
        List<FclBuyCost> collapseList = new ArrayList<FclBuyCost>();
        String oceanFreight = messageResources.getMessage("OceanFreightPopUp");// ocen fright taking from property file
        Map fclBuyMap = new HashMap();
        Map fclUnitMap = new HashMap();
        Map unitTypetMap = new HashMap();
        List unitTypeListtest = new ArrayList();
        for (Iterator iterator = fclBuyCostList.iterator(); iterator.hasNext();) {
            FclBuyCost fclBuyCost = (FclBuyCost) iterator.next();
            FclBuyCost newFclBuyCost = new FclBuyCost();// new object
            PropertyUtils.copyProperties(newFclBuyCost, fclBuyCost);// copy to new
            newFclBuyCost.setCostCode(oceanFreight);
            if (fclBuyMap.get(newFclBuyCost.getCostCode()) != null) {
                FclBuyCost tempFclBuyCost = (FclBuyCost) fclBuyMap.get(newFclBuyCost.getCostCode());
                List unitTypeList = new ArrayList();
                if (newFclBuyCost.getUnitTypeList() != null) {
                    for (Iterator iterator2 = newFclBuyCost.getUnitTypeList().iterator(); iterator2.hasNext();) {
                        FclBuyCostTypeRates fclBuyCostTypeRates = new FclBuyCostTypeRates();
                        FclBuyCostTypeRates junkFclBuyCostTypeRates = new FclBuyCostTypeRates();
                        junkFclBuyCostTypeRates = (FclBuyCostTypeRates) iterator2.next();

                        PropertyUtils.copyProperties(fclBuyCostTypeRates, junkFclBuyCostTypeRates);
                        if (fclBuyCostTypeRates.getUnitType() != null) {
                            if (fclUnitMap.get(fclBuyCostTypeRates.getUnitType().getCode() + "-" + newFclBuyCost.getCostCode()) != null) {
                                FclBuyCostTypeRates tempFclBuyCostTypeRates = (FclBuyCostTypeRates) fclUnitMap.get(fclBuyCostTypeRates.getUnitType().getCode() + "-" + newFclBuyCost.getCostCode());
                                tempFclBuyCostTypeRates.setActiveAmt(tempFclBuyCostTypeRates.getActiveAmt()
                                        + fclBuyCostTypeRates.getActiveAmt());
                                tempFclBuyCostTypeRates.setOldAmount(tempFclBuyCostTypeRates.getOldAmount()
                                        + fclBuyCostTypeRates.getOldAmount());
                                tempFclBuyCostTypeRates.setMarkup(tempFclBuyCostTypeRates.getMarkup()
                                        + fclBuyCostTypeRates.getMarkup());
                                tempFclBuyCostTypeRates.setOldMarkUp(tempFclBuyCostTypeRates.getOldMarkUp()
                                        + fclBuyCostTypeRates.getOldMarkUp());

                                fclUnitMap.put(fclBuyCostTypeRates.getUnitType().getCode() + "-" + newFclBuyCost.getCostCode(), tempFclBuyCostTypeRates);
                                unitTypeList.add(tempFclBuyCostTypeRates);
                            } else {
                                fclUnitMap.put(fclBuyCostTypeRates.getUnitType().getCode() + "-" + newFclBuyCost.getCostCode(), fclBuyCostTypeRates);
                                unitTypeList.add(fclBuyCostTypeRates);
                                unitTypetMap.put(fclBuyCostTypeRates.getUnitType().getCodedesc(), fclBuyCostTypeRates);
                            }
                        }
                    }
                }
                tempFclBuyCost.setUnitTypeList(unitTypeList);
                // unitTypeListtest.addAll(unitTypeList);
                fclBuyMap.put(tempFclBuyCost.getCostCode(), tempFclBuyCost);
            } else {
                fclBuyMap.put(newFclBuyCost.getCostCode(), newFclBuyCost);

                List unitTypeList = new ArrayList();
                for (Iterator iterator2 = newFclBuyCost.getUnitTypeList().iterator(); iterator2.hasNext();) {
                    FclBuyCostTypeRates tempFclBuyCostTypeRates = new FclBuyCostTypeRates();
                    FclBuyCostTypeRates junkFclBuyCostTypeRates = new FclBuyCostTypeRates();
                    junkFclBuyCostTypeRates = (FclBuyCostTypeRates) iterator2.next();

                    PropertyUtils.copyProperties(tempFclBuyCostTypeRates, junkFclBuyCostTypeRates);

                    if (tempFclBuyCostTypeRates.getUnitType() != null) {
                        fclUnitMap.put(tempFclBuyCostTypeRates.getUnitType().getCode() + "-" + newFclBuyCost.getCostCode(), tempFclBuyCostTypeRates);
                        unitTypeList.add(tempFclBuyCostTypeRates);
                        unitTypetMap.put(tempFclBuyCostTypeRates.getUnitType().getCodedesc(), tempFclBuyCostTypeRates);
                    }
                }
                newFclBuyCost.setUnitTypeList(unitTypeList);
                collapseList.add(newFclBuyCost);
            }
        }

        //unitTypetMap;- this map object wil hold the  unique fclBuyRates object like.A=20,B=20;
        Set set = unitTypetMap.keySet();
        for (String unit : CommonConstants.getUnitCostTypeListInOrder()) {
            for (Iterator iterator = set.iterator(); iterator.hasNext();) {
                String key = (String) iterator.next();
                if (null != unit && unit.equalsIgnoreCase(key)) {
                    FclBuyCostTypeRates tempFclBuyCostTypeRates = (FclBuyCostTypeRates) unitTypetMap.get(key);
                    unitTypeListtest.add(tempFclBuyCostTypeRates);
                }
            }
        }
        QuotationBC quotationBC = new QuotationBC();
        //getOFRUpdatedAmount():--sending unitTypeListtest list to update with  Ocen Fright Amount for all containers
        quotationBC.getOFRUpdatedAmount(unitTypeListtest, map);
        //collapseList :-- update FclBuyCost object with latest rates and containers
        for (int i = 0; i < collapseList.size(); i++) {
            FclBuyCost newFclBuyCost2 = (FclBuyCost) collapseList.get(i);
            newFclBuyCost2.setUnitTypeList(unitTypeListtest);
        }

        return collapseList;
    }

    public List getCompressListForBooking(List<FclBuyCost> fclBuyCostList, MessageResources messageResources, Map ofrAmountCollapseMap) throws Exception {
        List<FclBuyCost> collapseList = new ArrayList<FclBuyCost>();
        String addedUnitType = ",";
        String oceanFreight = messageResources.getMessage("oceanfreight");
        String OFR = messageResources.getMessage("OceanFreight");
        String intermodel = messageResources.getMessage("Intermodel");
        String consolidatorRates[] = new String[10];
        if (OFR.indexOf(",") != -1) {
            consolidatorRates = OFR.split(",");
        }
        String interModelRate[] = new String[5];
        if (intermodel.indexOf(",") != -1) {
            interModelRate = intermodel.split(",");
        }
        Double newOfrAmount = 0d;
        Map fclBuyMap = new HashMap();
        Map fclUnitMap = new HashMap();
        Map<String, String> unitNameMap = new HashMap<String, String>();
        fclBuyCostList = (fclBuyCostList == null) ? Collections.EMPTY_LIST : fclBuyCostList;
        for (Iterator iterator = fclBuyCostList.iterator(); iterator.hasNext();) {
            FclBuyCost fclBuyCost = (FclBuyCost) iterator.next();
            FclBuyCost newFclBuyCost = new FclBuyCost();
            PropertyUtils.copyProperties(newFclBuyCost, fclBuyCost);
            newFclBuyCost.setCostCodeDesc(newFclBuyCost.getCostId().getCode());
            boolean flag = false;
            for (int i = 0; i < consolidatorRates.length; i++) {
                String temp = consolidatorRates[i];
                if (temp.equals(newFclBuyCost.getCostCodeDesc())) {
                    flag = true;
                    break;
                }
            }
            for (int i = 0; i < interModelRate.length; i++) {
                String temp = interModelRate[i];
                if (temp.equals(newFclBuyCost.getCostCodeDesc())) {
                    flag = true;
                    break;
                }
            }
            if (newFclBuyCost.getCostCode().equalsIgnoreCase(oceanFreight)) {
                flag = false;
            }
            if (flag) {
                fclBuyMap.put(newFclBuyCost.getCostCode(), newFclBuyCost);
                List unitTypeList = new ArrayList();
                for (Iterator iterator2 = newFclBuyCost.getUnitTypeList().iterator(); iterator2.hasNext();) {
                    FclBuyCostTypeRates tempFclBuyCostTypeRates = new FclBuyCostTypeRates();
                    FclBuyCostTypeRates junkFclBuyCostTypeRates = new FclBuyCostTypeRates();
                    junkFclBuyCostTypeRates = (FclBuyCostTypeRates) iterator2.next();
                    PropertyUtils.copyProperties(tempFclBuyCostTypeRates, junkFclBuyCostTypeRates);
                    if (tempFclBuyCostTypeRates.getUnitname() != null) {
                        if (addedUnitType.indexOf("," + tempFclBuyCostTypeRates.getUnitname() + ",") == -1) {
                            addedUnitType += tempFclBuyCostTypeRates.getUnitname() + ",";
                        }
                    }
                    if (tempFclBuyCostTypeRates.getUnitType() != null) {
                        tempFclBuyCostTypeRates.setActiveAmt(tempFclBuyCostTypeRates.getActiveAmt() + tempFclBuyCostTypeRates.getMarkup());
                        tempFclBuyCostTypeRates.setOldAmount(tempFclBuyCostTypeRates.getOldAmount() + tempFclBuyCostTypeRates.getOldMarkUp());
                        fclUnitMap.put(tempFclBuyCostTypeRates.getUnitType().getCode() + "-" + newFclBuyCost.getCostCode(), tempFclBuyCostTypeRates);
                        unitTypeList.add(tempFclBuyCostTypeRates);
                    }
                }
                newFclBuyCost.setUnitTypeList(unitTypeList);
                collapseList.add(newFclBuyCost);
            }
            if (!flag) {
                String costCode = newFclBuyCost.getCostCodeDesc();
                //newFclBuyCost.setCostCodeDesc(oceanFreight);
                newFclBuyCost.setCostCode(oceanFreight);
                if (fclBuyMap.get(newFclBuyCost.getCostCode()) != null) {
                    FclBuyCost tempFclBuyCost = (FclBuyCost) fclBuyMap.get(newFclBuyCost.getCostCode());
                    List unitTypeList = new ArrayList();
                    if (newFclBuyCost.getUnitTypeList() != null) {
                        for (Iterator iterator2 = newFclBuyCost.getUnitTypeList().iterator(); iterator2.hasNext();) {
                            FclBuyCostTypeRates fclBuyCostTypeRates = new FclBuyCostTypeRates();
                            FclBuyCostTypeRates junkFclBuyCostTypeRates = new FclBuyCostTypeRates();
                            junkFclBuyCostTypeRates = (FclBuyCostTypeRates) iterator2.next();
                            PropertyUtils.copyProperties(fclBuyCostTypeRates, junkFclBuyCostTypeRates);
                            if (junkFclBuyCostTypeRates.getUnitname() != null) {
                                if (addedUnitType.indexOf("," + junkFclBuyCostTypeRates.getUnitname() + ",") == -1) {
                                    addedUnitType += junkFclBuyCostTypeRates.getUnitname() + ",";
                                }
                            }
                            if (fclBuyCostTypeRates.getUnitType() != null && fclUnitMap.get(fclBuyCostTypeRates.getUnitType().getCode() + "-" + newFclBuyCost.getCostCode()) != null) {
                                FclBuyCostTypeRates tempFclBuyCostTypeRates = (FclBuyCostTypeRates) fclUnitMap.get(fclBuyCostTypeRates.getUnitType().getCode() + "-" + newFclBuyCost.getCostCode());
                                newOfrAmount = 0d;
                                newOfrAmount += tempFclBuyCostTypeRates.getActiveAmt() + fclBuyCostTypeRates.getActiveAmt() - fclBuyCostTypeRates.getOldAmount() + fclBuyCostTypeRates.getMarkup() - fclBuyCostTypeRates.getOldMarkUp();
                                tempFclBuyCostTypeRates.setActiveAmt(newOfrAmount);
                                fclUnitMap.put(tempFclBuyCostTypeRates.getUnitType().getCode() + "-" + tempFclBuyCostTypeRates.getCostCode(), tempFclBuyCostTypeRates);
                                unitTypeList.add(tempFclBuyCostTypeRates);
                            } else {
                                if (fclBuyCostTypeRates.getUnitType() != null) {
                                    Double OFROldAmt = 0d;
                                    if (null != ofrAmountCollapseMap.get(fclBuyCostTypeRates.getUnitname())) {
                                        OFROldAmt = (Double) ofrAmountCollapseMap.get(fclBuyCostTypeRates.getUnitname());
                                    }
                                    newOfrAmount = 0d;
                                    newOfrAmount += fclBuyCostTypeRates.getActiveAmt() - fclBuyCostTypeRates.getOldAmount() + fclBuyCostTypeRates.getMarkup() - fclBuyCostTypeRates.getOldMarkUp() + OFROldAmt;
                                    fclBuyCostTypeRates.setActiveAmt(newOfrAmount);
                                    fclBuyCostTypeRates.setOldAmount(OFROldAmt);
                                    fclUnitMap.put(fclBuyCostTypeRates.getUnitType().getCode() + "-" + newFclBuyCost.getCostCode(), fclBuyCostTypeRates);
                                    unitTypeList.add(fclBuyCostTypeRates);
                                }
                            }
                        }
                    }
                    tempFclBuyCost.setUnitTypeList(unitTypeList);
                    fclBuyMap.put(tempFclBuyCost.getCostCode(), tempFclBuyCost);
                } else {
                    fclBuyMap.put(newFclBuyCost.getCostCode(), newFclBuyCost);
                    List unitTypeList = new ArrayList();
                    for (Iterator iterator2 = newFclBuyCost.getUnitTypeList().iterator(); iterator2.hasNext();) {
                        FclBuyCostTypeRates tempFclBuyCostTypeRates = new FclBuyCostTypeRates();
                        FclBuyCostTypeRates junkFclBuyCostTypeRates = new FclBuyCostTypeRates();
                        junkFclBuyCostTypeRates = (FclBuyCostTypeRates) iterator2.next();
                        PropertyUtils.copyProperties(tempFclBuyCostTypeRates, junkFclBuyCostTypeRates);
                        if (junkFclBuyCostTypeRates.getUnitname() != null) {
                            if (addedUnitType.indexOf("," + junkFclBuyCostTypeRates.getUnitname() + ",") == -1) {
                                addedUnitType += junkFclBuyCostTypeRates.getUnitname() + ",";
                            }
                        }
                        Double OFROldAmt = 0d;
                        if (null != ofrAmountCollapseMap.get(tempFclBuyCostTypeRates.getUnitname())) {
                            OFROldAmt = (Double) ofrAmountCollapseMap.get(tempFclBuyCostTypeRates.getUnitname());
                        }
                        newOfrAmount = 0d;
                        newOfrAmount += tempFclBuyCostTypeRates.getActiveAmt() - tempFclBuyCostTypeRates.getOldAmount() + tempFclBuyCostTypeRates.getMarkup() - tempFclBuyCostTypeRates.getOldMarkUp() + OFROldAmt;
                        tempFclBuyCostTypeRates.setActiveAmt(newOfrAmount);
                        tempFclBuyCostTypeRates.setOldAmount(OFROldAmt);
                        if (tempFclBuyCostTypeRates.getUnitType() != null) {
                            fclUnitMap.put(tempFclBuyCostTypeRates.getUnitType().getCode() + "-" + newFclBuyCost.getCostCode(), tempFclBuyCostTypeRates);
                            unitTypeList.add(tempFclBuyCostTypeRates);
                        }
                    }
                    newFclBuyCost.setUnitTypeList(unitTypeList);
                    collapseList.add(newFclBuyCost);
                }
            }
        }
        List tempFclBuyCostList = new ArrayList();
        for (Iterator iter = collapseList.iterator(); iter.hasNext();) {
            FclBuyCost fclBuyCost = (FclBuyCost) iter.next();
            addEmptyUnitTypes(fclBuyCost, addedUnitType);
            tempFclBuyCostList.add(fclBuyCost);
        }
        return collapseList;
    }

    public List getSslineId2(String org, String Dest, String ssline, String commodity, String ssline1, Date noOfDays, MessageResources messageResources) throws Exception {
        List percentList = new ArrayList();
        List<CostBean> fclcontList = new ArrayList<CostBean>();
        List<CostBean> costlist = new ArrayList<CostBean>();
        String queryString1 = "Select fb.fclStdId from FclBuy fb where fb.originTerminal.id='" + org + "'and fb.destinationPort.id='" + Dest + "' and fb.comNum.id='" + commodity + "' and fb.sslineNo.accountno='" + ssline1 + "' ";
        List listofFclStdId = getCurrentSession().createQuery(queryString1).list();
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        List fclcostidList = new ArrayList();
        List perkglbsList = new ArrayList();
        List CostCodedesc = new ArrayList();
        for (int i = 0; i < listofFclStdId.size(); i++) {
            int fclstdid = (Integer) listofFclStdId.get(i);
            String queryString3 = "Select fbc.fclCostId from FclBuyCost fbc where fbc.fclStdId='" + fclstdid + "' order by fbc.costId.id";
            List tempfclcostidList = getCurrentSession().createQuery(queryString3).list();
            if (!tempfclcostidList.isEmpty()) {
                int j = 0;
                while (j < tempfclcostidList.size()) {
                    fclcostidList.add(tempfclcostidList.get(j));
                    j++;
                }
            }
        }
        for (int l = 0; l < fclcostidList.size(); l++) {
            int fclcostid = (Integer) fclcostidList.get(l);
            String queryString5 = "Select fbcr.unitType.id,fbcr.ratAmount,fbcr.fclCostId,fbcr.markup,fbcr.activeAmt,fbcr.currency.code,fbcr.minimumAmt from FclBuyCostTypeRates fbcr where fbcr.fclCostId='" + fclcostid + "'";
            List queryObj = getCurrentSession().createQuery(queryString5).list();
            Iterator itr = queryObj.iterator();
            while (itr.hasNext()) {
                Object[] row = (Object[]) itr.next();
                if (row[0] == null) {
                    CostBean c1 = new CostBean();
                    if (row[1] != null) {
                        c1.setActiveAmt(number.format(row[4]));
                    }
                    Integer fclcostId = 0;
                    if (row[2] != null) {
                        fclcostId = (Integer) row[2];
                        String costType = getCostType(fclcostId);
                        c1.setCostType(costType);
                        String chargecodedesc = getchargecodeDesc(fclcostId);
                        String chargeCode = getchargecode(fclcostId);
                        c1.setChargecode(chargecodedesc);
                        c1.setChargeCodedesc(chargeCode);
                        String sslineNo = getSslineName(fclcostId);
                        c1.setSsLineNumber(sslineNo);
                    }
                    Double retail = 0.00;
                    if (row[1] != null) {
                        retail = (Double) row[1];
                    }

                    c1.setUnitType("0.00");
                    String currency = "";
                    if (row[5] != null) {
                        currency = (String) row[5];
                    }
                    Double minimum = 0.00;
                    if (row[6] != null) {
                        minimum = (Double) row[6];
                    }
                    c1.setCurrency(currency);
                    c1.setRetail(number.format(retail));
                    c1.setMinimum(number.format(minimum));

                    if (c1.getCostType().trim().equalsIgnoreCase(messageResources.getMessage("per1000kg")) || c1.getCostType().trim().equalsIgnoreCase(messageResources.getMessage("per2000lbs"))) {
                        perkglbsList.add(c1);
                    }
                    if (c1.getCostType().equals(messageResources.getMessage("percentofr"))) {
                        c1.setNumber("1");
                        percentList.add(c1);
                    }
                    if (c1.getCostType().trim().equals(messageResources.getMessage("perbl")) || c1.getCostType().trim().equals(messageResources.getMessage("Flatratepercontainer"))) {
                        boolean flag = false;
                        if (fclcontList.size() > 0) {
                            for (int i = 0; i < fclcontList.size(); i++) {
                                CostBean c2 = (CostBean) fclcontList.get(i);
                                if (c2.getChargecode().equals(c1.getChargecode())) {
                                    flag = true;
                                    break;
                                }
                            }
                            if (!flag) {
                                fclcontList.add(c1);
                            }
                        } else {
                            fclcontList.add(c1);
                        }
                    }
                } else {
                    int unit = (Integer) row[0];
                    String unittype = String.valueOf(unit);
                    Double actAmt = (Double) row[4];
                    if (actAmt == null || actAmt.equals("0.0")) {
                        actAmt = 0.00;
                    }
                    Integer fclcostId = 0;
                    String chargecodedesc = "";
                    String chargeCode = "";
                    String costType = "";
                    String sslineNo = "";
                    if (row[2] != null) {
                        fclcostId = (Integer) row[2];
                        chargecodedesc = getchargecodeDesc(fclcostId);
                        chargeCode = getchargecode(fclcostId);
                        costType = getCostType(fclcostId);
                        sslineNo = getSslineName(fclcostId);
                    }
                    String currency = "";
                    if (row[5] != null) {
                        currency = (String) row[5];
                    }
                    Double markUp = (Double) row[3];
                    CostBean cbean = new CostBean();
                    for (int i = 0; i < costlist.size(); i++) {
                        cbean = (CostBean) costlist.get(i);
                        if (cbean.getUnitType().equals(unittype) && cbean.getChargecode().equals(chargecodedesc)) {
                        } else {
                            cbean = new CostBean();
                        }
                    }
                    if (markUp == null) {
                        markUp = 0.00;
                    }
                    cbean.setSsLineNumber(sslineNo);
                    cbean.setChargecode(chargecodedesc);
                    cbean.setChargeCodedesc(chargeCode);
                    cbean.setCostType(costType);
                    cbean.setUnitType(unittype);
                    cbean.setCurrency(currency);
                    cbean.setNumber("1");
                    String unitType[] = messageResources.getMessage("unittype").split(",");

                    if (unittype.equals(unitType[0])) {
                        cbean.setSetA(number.format(actAmt));
                        cbean.setMarkUpA(number.format(markUp));
                    } else if (unittype.equals(unitType[1])) {
                        cbean.setSetD(number.format(actAmt));
                        cbean.setMarkUpD(number.format(markUp));
                    } else if (unittype.equals(unitType[2])) {
                        cbean.setSetB(number.format(actAmt));
                        cbean.setMarkUpB(number.format(markUp));
                    } else if (unittype.equals(unitType[3])) {
                        cbean.setSetE(number.format(actAmt));
                        cbean.setMarkUpE(number.format(markUp));
                    } else if (unittype.equals(unitType[4])) {
                        cbean.setSetC(number.format(actAmt));
                        cbean.setMarkUpC(number.format(markUp));
                    } else if (unittype.equals(unitType[5])) {
                        cbean.setSetF(number.format(actAmt));
                        cbean.setMarkUpF(number.format(markUp));
                    } else if (unittype.equals(unitType[6])) {
                        cbean.setSetG(number.format(actAmt));
                        cbean.setMarkUpG(number.format(markUp));
                    } else if (unittype.equals(unitType[7])) {

                        cbean.setSetH(number.format(actAmt));
                        cbean.setMarkUpH(number.format(markUp));
                    } else if (unittype.equals(unitType[8])) {
                        cbean.setSetI(number.format(actAmt));
                        cbean.setMarkUpI(number.format(markUp));
                    } else if (unittype.equals(unitType[9])) {
                        cbean.setSetJ(number.format(actAmt));
                        cbean.setMarkUpJ(number.format(markUp));
                    } else if (unittype.equals(unitType[10])) {
                        cbean.setSetK(number.format(actAmt));
                        cbean.setMarkUpK(number.format(markUp));
                    } else if (unittype.equals(unitType[11])) {
                        cbean.setSetL(number.format(actAmt));
                        cbean.setMarkUpL(number.format(markUp));
                    } else if (unittype.equals(unitType[12])) {
                        cbean.setSetM(number.format(actAmt));
                        cbean.setMarkUpM(number.format(markUp));
                    } else if (unittype.equals(unitType[13])) {
                        cbean.setSetN(number.format(actAmt));
                        cbean.setMarkUpN(number.format(markUp));
                    } else if (unittype.equals(unitType[14])) {
                        cbean.setSetO(number.format(actAmt));
                        cbean.setMarkUpO(number.format(markUp));
                    } else if (unittype.equals(unitType[15])) {
                        cbean.setSetP(number.format(actAmt));
                        cbean.setMarkUpP(number.format(markUp));
                    } else if (unittype.equals(unitType[16])) {
                        cbean.setSetQ(number.format(actAmt));
                        cbean.setMarkUpQ(number.format(markUp));
                    }
                    if (ssline.equals(sslineNo)) {
                        if (!unittype.equals("0")) {
                            if (actAmt.toString().trim().equals("0.0")) {
                            } else {
                                if (cbean.getCostType().equals(messageResources.getMessage("percentofr"))) {
                                } else {
                                    costlist.add(cbean);
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int j = 0; j < perkglbsList.size(); j++) {
            CostBean c1 = (CostBean) perkglbsList.get(j);
            costlist.add(c1);
        }
        for (int i = 0; i < fclcontList.size(); i++) {
            CostBean c1 = (CostBean) fclcontList.get(i);
            costlist.add(c1);
        }
        List percentList1 = new ArrayList();
        String retail = "0.00";
        Double actAmt = 0.00;
        for (int J = 0; J < percentList.size(); J++) {
            CostBean cbean = (CostBean) percentList.get(J);
            String tempchargeCode = cbean.getChargecode();
            String tempcostType = cbean.getCostType();
            String tempChargeCodedesc = cbean.getChargeCodedesc();
            String tempSlline = cbean.getSsLineNumber();
            String tempREtail = cbean.getRetail();
            String tempCurrency = cbean.getCurrency();
            if (cbean.getCostType() != null && cbean.getCostType().equals(messageResources.getMessage("percentofr"))) {
                String amt = "";
                for (int i = 0; i < costlist.size(); i++) {
                    CostBean c1 = (CostBean) costlist.get(i);
                    cbean.setCostType(tempcostType);
                    cbean.setChargecode(tempchargeCode);
                    cbean.setChargeCodedesc(tempChargeCodedesc);
                    cbean.setSsLineNumber(tempSlline);
                    cbean.setRetail(tempREtail);
                    cbean.setCurrency(tempCurrency);
                    if (c1.getChargecode() != null && c1.getChargecode().equalsIgnoreCase(messageResources.getMessage("oceanfreight")) && c1.getSsLineNumber().equals(cbean.getSsLineNumber())) {
                        if (c1.getCostType().equals(messageResources.getMessage("FlatRatePerConatinerSize"))) {
                            cbean.setNumber("1");
                            String unitType[] = messageResources.getMessage("unittype").split(",");
                            if (c1.getUnitType().equals(unitType[0])) {
                                cbean.setUnitType(unitType[0]);
                                amt = c1.getSetA();
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetA(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpA("0.00");
                            } else if (c1.getUnitType().equals(unitType[1])) {
                                cbean.setUnitType(unitType[1]);
                                amt = c1.getSetD();
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetD(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpD("0.00");
                            } else if (c1.getUnitType().equals(unitType[2])) {
                                cbean.setUnitType(unitType[2]);
                                amt = c1.getSetB();
                                if (cbean.getRetail() == null) {
                                    cbean.setRetail("0.00");
                                }
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetB(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpB("0.00");
                            } else if (c1.getUnitType().equals(unitType[3])) {
                                cbean.setUnitType(unitType[3]);
                                amt = c1.getSetE();
                                if (cbean.getRetail() == null) {
                                    cbean.setRetail("0.00");
                                }
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetE(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpE("0.00");
                            } else if (c1.getUnitType().equals(unitType[4])) {
                                cbean.setUnitType(unitType[4]);
                                amt = c1.getSetC();
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetC(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpC("0.00");
                            } else if (c1.getUnitType().equals(unitType[5])) {
                                cbean.setUnitType(unitType[5]);
                                amt = c1.getSetF();
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetF(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpF("0.00");
                            } else if (c1.getUnitType().equals(unitType[6])) {
                                cbean.setUnitType(unitType[6]);
                                amt = c1.getSetG();
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetG(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpG("0.00");
                            } else if (c1.getUnitType().equals(unitType[7])) {
                                cbean.setUnitType(unitType[7]);
                                amt = c1.getSetH();
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetH(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpH("0.00");
                            } else if (c1.getUnitType().equals(unitType[8])) {
                                cbean.setUnitType(unitType[8]);
                                amt = c1.getSetI();
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetI(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpI("0.00");
                            } else if (c1.getUnitType().equals(unitType[9])) {
                                cbean.setUnitType(unitType[9]);
                                amt = c1.getSetJ();
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetJ(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpJ("0.00");
                            } else if (c1.getUnitType().equals(unitType[10])) {
                                cbean.setUnitType(unitType[10]);
                                amt = c1.getSetK();
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetK(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpK("0.00");
                            } else if (c1.getUnitType().equals(unitType[11])) {
                                cbean.setUnitType(unitType[11]);
                                amt = c1.getSetL();
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetL(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpL("0.00");
                            } else if (c1.getUnitType().equals(unitType[12])) {
                                cbean.setUnitType(unitType[12]);
                                amt = c1.getSetM();
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetM(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpM("0.00");
                            } else if (c1.getUnitType().equals(unitType[13])) {
                                cbean.setUnitType(unitType[13]);
                                amt = c1.getSetN();
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetN(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpN("0.00");
                            } else if (c1.getUnitType().equals(unitType[14])) {
                                cbean.setUnitType(unitType[14]);
                                amt = c1.getSetO();
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetO(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpO("0.00");
                            } else if (c1.getUnitType().equals(unitType[15])) {
                                cbean.setUnitType(unitType[15]);
                                amt = c1.getSetP();
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetP(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpP("0.00");
                            } else if (c1.getUnitType().equals(unitType[16])) {
                                cbean.setUnitType(unitType[16]);
                                amt = c1.getSetQ();
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetQ(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpQ("0.00");
                            }
                        } else {
                            amt = c1.getRetail();
                            retail = cbean.getRetail();
                            actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                            cbean.setRetail(number.format(actAmt));
                            cbean.setNumber("1");
                        }
                    }
                    percentList1.add(cbean);
                    cbean = new CostBean();
                }
            }
        }
        for (int i = 0; i < percentList1.size(); i++) {
            CostBean c1 = (CostBean) percentList1.get(i);
            if (c1.getUnitType() != null) {
                costlist.add(c1);
            }
        }
        for (int l = 0; l < fclcostidList.size(); l++) {
            int fclcostid = (Integer) fclcostidList.get(l);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            Iterator itr = null;
            String noOfDay = sdf.format(noOfDays);
            Query queryString5 = getCurrentSession().createQuery("Select fbcr.unitType.id,fbcr.ratAmount,fbcr.fclCostId,fbcr.markup,fbcr.activeAmt,fbcr.effectiveDate,fbcr.currency.code from FclBuyCostTypeFutureRates fbcr where fbcr.fclCostId='" + fclcostid + "' and fbcr.effectiveDate between fbcr.effectiveDate and ?0");
            queryString5.setParameter("0", df.parse(noOfDay));
            List queryObj = queryString5.list();
            itr = queryObj.iterator();
            if (itr != null) {
                while (itr.hasNext()) {
                    Object[] row = (Object[]) itr.next();
                    if (row[0] == null) {
                        row[0] = 0;
                    }
                    int unit = (Integer) row[0];
                    String unittype = String.valueOf(unit);
                    actAmt = (Double) row[1];
                    if (actAmt == null || actAmt.equals("0.0")) {
                        actAmt = 0.00;
                    }
                    Integer fclcostId = (Integer) row[2];
                    Double markUp = (Double) row[3];
                    if (markUp == null || markUp.equals("0.0")) {
                        markUp = 0.00;
                    }
                    Double ratAmount = (Double) row[4];
                    if (ratAmount == null || ratAmount.equals("0.0")) {
                        ratAmount = 0.00;
                    }
                    Date effectiveDate = (Date) row[5];
                    String currency = (String) row[6];
                    String chargeCode = getchargecode(fclcostId);
                    String chargecodedesc = getchargecodeDesc(fclcostId);
                    String costType = getCostType(fclcostId);
                    String sslineNo = getSslineName(fclcostId);
                    if (effectiveDate != null) {
                        boolean flag = false;
                        for (int i = 0; i < costlist.size(); i++) {
                            CostBean c1 = (CostBean) costlist.get(i);
                            if (c1.getSsLineNumber().equals(sslineNo) && c1.getCostType().equals(costType) && c1.getChargecode().equals(chargecodedesc)) {
                                flag = true;
                                if (costType.equals(messageResources.getMessage("FlatRatePerConatinerSize"))) {
                                    if (c1.getUnitType() != null && c1.getUnitType().equals(unittype)) {
                                        if (c1.getEffectiveDate() == null) {
                                            c1.setEffectiveDate(sdf.format(effectiveDate));
                                        } else {
                                            c1.setEffectiveDate(" ");
                                        }
                                        String unitType[] = messageResources.getMessage("unittype").split(",");
                                        if (unittype.equals(unitType[0])) {
                                            c1.setFutureRateA(number.format(actAmt));
                                        } else if (unittype.equals(unitType[1])) {
                                            c1.setFutureRateD(number.format(actAmt));
                                        } else if (unittype.equals(unitType[2])) {
                                            c1.setFutureRateB(number.format(actAmt));
                                        } else if (unittype.equals(unitType[3])) {
                                            c1.setFutureRateE(number.format(actAmt));
                                        } else if (unittype.equals(unitType[4])) {
                                            c1.setFutureRateC(number.format(actAmt));
                                        } else if (unittype.equals(unitType[5])) {
                                            c1.setFutureRateF(number.format(actAmt));
                                        } else if (unittype.equals(unitType[6])) {
                                            c1.setFutureRateG(number.format(actAmt));
                                        } else if (unittype.equals(unitType[7])) {
                                            c1.setFutureRateH(number.format(actAmt));
                                        } else if (unittype.equals(unitType[8])) {
                                            c1.setFutureRateI(number.format(actAmt));
                                        } else if (unittype.equals(unitType[9])) {
                                            c1.setFutureRateJ(number.format(actAmt));
                                        } else if (unittype.equals(unitType[10])) {
                                            c1.setFutureRateK(number.format(actAmt));
                                        } else if (unittype.equals(unitType[11])) {
                                            c1.setFutureRateL(number.format(actAmt));
                                        } else if (unittype.equals(unitType[12])) {
                                            c1.setFutureRateM(number.format(actAmt));
                                        } else if (unittype.equals(unitType[13])) {
                                            c1.setFutureRateN(number.format(actAmt));
                                        } else if (unittype.equals(unitType[14])) {
                                            c1.setFutureRateO(number.format(actAmt));
                                        } else if (unittype.equals(unitType[15])) {
                                            c1.setFutureRateP(number.format(actAmt));
                                        } else if (unittype.equals(unitType[16])) {
                                            c1.setFutureRateQ(number.format(actAmt));
                                        }
                                    }
                                } else if (costType.equals(messageResources.getMessage("Flatratepercontainer"))) {
                                    c1.setRetailFuture(number.format(ratAmount));
                                    if (c1.getEffectiveDate() == null) {
                                        c1.setEffectiveDate(sdf.format(effectiveDate));
                                    }
                                } else if (costType.equals(messageResources.getMessage("perbl"))) {
                                    c1.setRetailFuture(number.format(ratAmount));
                                    if (c1.getOtherEffectiveDate() == null) {
                                        c1.setOtherEffectiveDate(sdf.format(effectiveDate));
                                    }
                                }
                            }
                        }
                        if (!flag) {
                            CostBean c4 = new CostBean();
                            c4.setSsLineNumber(sslineNo);
                            c4.setChargecode(chargecodedesc);
                            c4.setCostType(costType);
                            c4.setChargeCodedesc(chargeCode);
                            c4.setNumber("1");
                            c4.setCurrency(currency);
                            if (costType.equals(messageResources.getMessage("FlatRatePerConatinerSize"))) {
                                c4.setUnitType(unittype);
                                if (c4.getEffectiveDate() == null) {
                                    c4.setEffectiveDate(sdf.format(effectiveDate));
                                } else {
                                    c4.setEffectiveDate(" ");
                                }
                                c4.setInclude("on");
                                c4.setPrint("on");
                                String unitTypes[] = messageResources.getMessage("unittype").split(",");
                                if (unittype.equals(unitTypes[0])) {
                                    c4.setSetA("0.00");
                                    c4.setFutureRateA(number.format(actAmt));
                                    c4.setMarkUpA("0.00");
                                } else if (unittype.equals(unitTypes[1])) {
                                    c4.setFutureRateD(number.format(actAmt));
                                    c4.setSetD("0.00");
                                    c4.setMarkUpD("0.00");
                                } else if (unittype.equals(unitTypes[2])) {
                                    c4.setFutureRateB(number.format(actAmt));
                                    c4.setSetB("0.00");
                                    c4.setMarkUpB("0.00");
                                } else if (unittype.equals(unitTypes[3])) {
                                    c4.setFutureRateE(number.format(actAmt));
                                    c4.setSetE("0.00");
                                    c4.setMarkUpE("0.00");
                                } else if (unittype.equals(unitTypes[4])) {
                                    c4.setFutureRateC(number.format(actAmt));
                                    c4.setSetC("0.00");
                                    c4.setMarkUpC("0.00");
                                } else if (unittype.equals(unitTypes[5])) {
                                    c4.setFutureRateF(number.format(actAmt));
                                    c4.setSetF("0.00");
                                    c4.setMarkUpF("0.00");
                                } else if (unittype.equals(unitTypes[6])) {
                                    c4.setFutureRateG(number.format(actAmt));
                                    c4.setSetG("0.00");
                                    c4.setMarkUpG("0.00");
                                } else if (unittype.equals(unitTypes[7])) {
                                    c4.setFutureRateH(number.format(actAmt));
                                    c4.setSetH("0.00");
                                    c4.setMarkUpH("0.00");
                                } else if (unittype.equals(unitTypes[8])) {
                                    c4.setFutureRateI(number.format(actAmt));
                                    c4.setSetI("0.00");
                                    c4.setMarkUpI("0.00");
                                } else if (unittype.equals(unitTypes[9])) {
                                    c4.setFutureRateJ(number.format(actAmt));
                                    c4.setSetJ("0.00");
                                    c4.setMarkUpJ("0.00");
                                } else if (unittype.equals(unitTypes[10])) {
                                    c4.setFutureRateK(number.format(actAmt));
                                    c4.setSetK("0.00");
                                    c4.setMarkUpK("0.00");
                                } else if (unittype.equals(unitTypes[11])) {
                                    c4.setFutureRateL(number.format(actAmt));
                                    c4.setSetL("0.00");
                                    c4.setMarkUpL("0.00");
                                } else if (unittype.equals(unitTypes[12])) {
                                    c4.setFutureRateM(number.format(actAmt));
                                    c4.setSetM("0.00");
                                    c4.setMarkUpM("0.00");
                                } else if (unittype.equals(unitTypes[13])) {
                                    c4.setFutureRateN(number.format(actAmt));
                                    c4.setSetN("0.00");
                                    c4.setMarkUpN("0.00");
                                } else if (unittype.equals(unitTypes[14])) {
                                    c4.setFutureRateO(number.format(actAmt));
                                    c4.setSetO("0.00");
                                    c4.setMarkUpO("0.00");
                                } else if (unittype.equals(unitTypes[15])) {
                                    c4.setFutureRateP(number.format(actAmt));
                                    c4.setSetP("0.00");
                                    c4.setMarkUpP("0.00");
                                } else if (unittype.equals(unitTypes[16])) {
                                    c4.setFutureRateQ(number.format(actAmt));
                                    c4.setSetQ("0.00");
                                    c4.setMarkUpQ("0.00");
                                }
                            } else if (costType.equals(messageResources.getMessage("Flatratepercontainer"))) {
                                c4.setRetailFuture(number.format(ratAmount));
                                c4.setRetail("0.00");
                                c4.setPrint("on");
                                c4.setInclude("on");
                                if (c4.getEffectiveDate() == null) {
                                    c4.setEffectiveDate(sdf.format(effectiveDate));
                                } else {
                                    c4.setEffectiveDate(" ");
                                }
                                c4.setUnitType("0.00");
                            } else if (costType.equals(messageResources.getMessage("perbl"))) {
                                c4.setRetailFuture(number.format(ratAmount));
                                c4.setRetail("0.00");
                                c4.setOtherinclude("on");
                                c4.setOtherprint("on");
                                if (c4.getOtherEffectiveDate() == null) {
                                    c4.setOtherEffectiveDate(sdf.format(effectiveDate));
                                } else {
                                    c4.setOtherEffectiveDate(" ");
                                }
                                c4.setUnitType("0.00");
                            }
                            if (costlist.size() > 0) {
                                boolean flag1 = false;
                                for (int a = 0; a < costlist.size(); a++) {
                                    CostBean c5 = (CostBean) costlist.get(a);
                                    if (c5.getChargecode().equals(c4.getChargecode()) && c5.getUnitType().equals(c4.getUnitType())) {
                                        flag1 = true;
                                        break;
                                    }
                                }
                                if (!flag1) {
                                    costlist.add(c4);

                                }
                            } else {
                                costlist.add(c4);
                            }
                        }
                    }
                }
            }
        }
        return costlist;
    }

    public List getOriginsForDestination(String destinationcode, String originDescription, String searchBy, String importFlag) throws Exception {
        List listofFclStdId = new ArrayList();
        originDescription = originDescription.replace("'", "").replace(" ", "");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select concat(org.un_loc_name,if(st.code != '',concat('/', st.code),''),concat('/', cn.codedesc),");
        queryBuilder.append("concat('(', org.un_loc_code, ')')) as origin ");
        queryBuilder.append("from un_location org ");
        queryBuilder.append("join fcl_buy fb ");
        queryBuilder.append("on (org.id = fb.origin_terminal) ");
        if (CommonUtils.isNotEmpty(destinationcode)) {
            queryBuilder.append("join un_location dest on (fb.destination_port = dest.id and dest.un_loc_code = '");
            queryBuilder.append(destinationcode).append("')");
        }
        queryBuilder.append("join fcl_buy_cost fbc on (fb.fcl_std_id = fbc.fcl_std_id) join genericcode_dup ge on (");
        queryBuilder.append("fbc.cost_id = ge.id and (ge.code = 'OCNFRT' or ge.code = 'OFIMP')) left join genericcode_dup st ");
        queryBuilder.append("on (org.statecode = st.id) join genericcode_dup cn on (org.countrycode = cn.id) ");
        queryBuilder.append(" JOIN fcl_buy_cost_type_rates rates ON rates.fcl_cost_id= fbc.fcl_cost_id where ");
        if (searchBy.equalsIgnoreCase("city")) {
            queryBuilder.append("(org.un_loc_code like '").append(originDescription).append("%' or org.search_un_loc_name like '");
            queryBuilder.append(originDescription).append("%') ");
        } else {
            queryBuilder.append("cn.codedesc like '").append(originDescription).append("%' ");
        }
        if (null != importFlag && importFlag.equals("true")) {
            queryBuilder.append("and (cn.code != 'US' or org.un_loc_code = 'USJAX') ");
        }
        queryBuilder.append("group by fb.origin_terminal order by org.search_un_loc_name");
        listofFclStdId = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        return listofFclStdId;
    }

    public Map<String, Map<String, String>> getOriginsByDestination(String desc, String code, String fclRatesOriginService, String searchBy, String comCode) throws Exception {
        List list = new FclBuyCostDAO().getOtherCommodity(comCode, null);
        String baseCommodity = "";
        if (CommonUtils.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                Object[] object = (Object[]) list.get(i);
                baseCommodity = null != object[3] ? object[3].toString() : "";
                break;
            }
        } else {
            GenericCode genericCode = new GenericCodeDAO().getGenericCodeId("4", comCode);
            if (null != genericCode) {
                baseCommodity = "" + genericCode.getId();
            }
        }
        List listofFclStdId = new ArrayList();
        Map<String, String> newSet = new LinkedHashMap<String, String>();
        Map<String, Map<String, String>> regionMap = new LinkedHashMap<String, Map<String, String>>();
        String previousRegion = null;
        String fclRatesOriginServiceDup = "";
        String queryString = "SELECT buy.origin_terminal, IF(org.statecode != '' and org.statecode IS NOT NULL,CONCAT(org.portname,'/',org.statecode,"
                + "IF(org.unlocationcode != '' and org.unlocationcode IS NOT NULL,CONCAT('(',org.unlocationcode,')'),'')),CONCAT(org.portname,"
                + "IF(org.unlocationcode != '' and org.unlocationcode IS NOT NULL,CONCAT('(',org.unlocationcode,')'),''))) AS originname, org_reg.codedesc as region "
                + "FROM fcl_buy buy JOIN ports org ON buy.origin_terminal=org.id left outer join genericcode_dup org_reg on org_reg.codetypeid=19 and org_reg.id=org.regioncode "
                + "JOIN ports dest ON buy.destination_port=dest.id, "
                + "fcl_buy_cost cost join genericcode_dup gen on cost.cost_id = gen.id and gen.code = 'OCNFRT' "
                + "WHERE buy.com_num = '" + baseCommodity + "' and buy.fcl_std_id=cost.fcl_std_id and cost.cost_id=11694";
        if (fclRatesOriginService.length() > 1) {
            fclRatesOriginServiceDup = fclRatesOriginService.replaceAll(" ", "-");
        }
        if (searchBy.equalsIgnoreCase("city")) {
            desc = desc.replace("'", "'+'");
            // or fb.originTerminal.unLocationName" + " like '" + fclRatesOriginServiceDup + "%')
            if (CommonUtils.isNotEmpty(code) && CommonUtils.isNotEmpty(desc)) {
                if (CommonUtils.isNotEmpty(fclRatesOriginService) && CommonUtils.isNotEmpty(fclRatesOriginServiceDup)) {
                    queryString = queryString + " and dest.portname='" + desc + "' and dest.unlocationcode='" + code + "'"
                            + " and (org.portname like '" + fclRatesOriginService + "%' or org.portname" + " like '" + fclRatesOriginServiceDup + "%')";
                } else {
                    queryString = queryString + " and dest.portname='" + desc + "' and dest.unlocationcode='" + code + "'"
                            + " and org.portname like '" + fclRatesOriginService + "%'";
                }
            } else {
                if (CommonUtils.isNotEmpty(fclRatesOriginService) && CommonUtils.isNotEmpty(fclRatesOriginServiceDup)) {
                    queryString = queryString + " and (org.portname like '" + fclRatesOriginService + "%' or org.portname" + " like '" + fclRatesOriginServiceDup + "%')";
                } else {
                    queryString = queryString + " and org.portname like '" + fclRatesOriginService + "%'";
                }

            }
        } else {
            if (CommonUtils.isNotEmpty(code) && CommonUtils.isNotEmpty(desc)) {
                desc = desc.replace("'", "\\'");
                queryString = queryString + " and dest.portname='" + desc + "' and dest.unlocationcode='" + code + "'"
                        + " and org.countryname like '" + fclRatesOriginService + "%'";
            } else {
                queryString = queryString + " and org.countryname like '" + fclRatesOriginService + "%'";
            }
        }
        queryString = queryString + " GROUP BY org.countryname, org.portname order by org.countryname, org_reg.codedesc, org.portname";
        listofFclStdId = getCurrentSession().createSQLQuery(queryString).addScalar("buy.origin_terminal", StringType.INSTANCE).addScalar("originname", StringType.INSTANCE).addScalar("region", StringType.INSTANCE).list();
        Iterator iter = listofFclStdId.iterator();
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            if (null != previousRegion && null != row[2] && !previousRegion.equals(row[2].toString())) {
                regionMap.put(previousRegion, newSet);
                newSet = new LinkedHashMap<String, String>();
                newSet.put(null != row[0] ? row[0].toString() : "", null != row[1] ? row[1].toString() : "");
                previousRegion = null != row[2] ? row[2].toString() : "NO Region";
            } else {
                newSet.put(null != row[0] ? row[0].toString() : "", null != row[1] ? row[1].toString() : "");
                previousRegion = null != row[2] ? row[2].toString() : "NO Region";
            }
        }
        regionMap.put(previousRegion, newSet);
        return regionMap;
    }

    public List getOriginsForDestinationforDwr(String origin, String country) throws Exception {
        String code = "";
        String desc = "";

        GenericCode genericCode = genericCodeDAO.findByCodeDescName(country, 11);
        desc = StringFormatter.getTerminalFromInputStringr(origin);
        code = StringFormatter.orgDestStringFormatter(origin);
        String queryString1 = "Select fb.originTerminal.id,fb.originTerminal.id from FclBuy fb,FclBuyCost fbc where "
                + "fb.destinationPort.unLocationName='" + desc + "' and fb.destinationPort.unLocationCode='" + code + "' and fb.fclStdId=fbc.fclStdId and fbc.costId.code='OCNFRT'";
        List listofFclStdId = getCurrentSession().createQuery(queryString1).list();
        Iterator iter = listofFclStdId.iterator();
        Set newSet = new HashSet();

        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();

            Integer id = (Integer) row[0];
            String queryString2 = "from UnLocation where id='" + id + "' and countryId.id='" + genericCode.getId() + "'";
            List ratesList = getCurrentSession().createQuery(queryString2).list();
            for (Iterator iterator = ratesList.iterator(); iterator.hasNext();) {
                UnLocation unLocation = (UnLocation) iterator.next();
                if (unLocation != null) {
                    newSet.add(StringFormatter.formatForOrigin(unLocation));
                }
            }
        }
        listofFclStdId.clear();
        listofFclStdId.addAll(newSet);
        Collections.sort(listofFclStdId);
        List newList = new ArrayList();
        for (Iterator iterator = listofFclStdId.iterator(); iterator.hasNext();) {
            String name = (String) iterator.next();
            newList.add(new LabelValueBean(name, name));
        }
        return newList;
    }

    public List getOriginCountriesForDestinationforDwr(String origin, Integer regionCode) throws Exception {

        String code = "";
        String desc = "";
        desc = StringFormatter.getTerminalFromInputStringr(origin);
        code = StringFormatter.orgDestStringFormatter(origin);

        desc = desc.replace("'", "'+'");
        String queryString1 = "Select fb.originTerminal.id,fb.originTerminal.id from FclBuy fb,FclBuyCost fbc where "
                + "fb.destinationPort.unLocationName='" + desc + "' and fb.destinationPort.unLocationCode='" + code + "' and fb.fclStdId=fbc.fclStdId and fbc.costId.code='OCNFRT'";
        List listofFclStdId = getCurrentSession().createQuery(queryString1).list();
        Iterator iter = listofFclStdId.iterator();
        Set newSet = new HashSet();

        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();

            Integer id = (Integer) row[0];
            String queryString2 = "from UnLocation where id='" + id + "'";
            List ratesList = getCurrentSession().createQuery(queryString2).list();
            for (Iterator iterator = ratesList.iterator(); iterator.hasNext();) {
                UnLocation unLocation = (UnLocation) iterator.next();
                List tempList = portsDAO.getAllCountryByRegion1("" + regionCode, unLocation.getUnLocationName());
                if (tempList.size() > 0) {
                    if (unLocation.getCountryId() != null && unLocation.getCountryId().getCodetypeid().equals(11)) {
                        newSet.add((String) unLocation.getCountryId().getCodedesc());
                    }
                }
            }
        }
        listofFclStdId.clear();
        listofFclStdId.addAll(newSet);
        Collections.sort(listofFclStdId);
        List newList = new ArrayList();
        newList.add(new LabelValueBean("Select One", "0"));
        for (Iterator iterator = listofFclStdId.iterator(); iterator.hasNext();) {
            String name = (String) iterator.next();
            newList.add(new LabelValueBean(name, name));
        }
        return newList;
    }

    public Map<String, Map<String, String>> getDestinationsByOrigin(String desc, String code, String fclRatesOriginService, String searchBy, String comCode) throws Exception {
        List list = new FclBuyCostDAO().getOtherCommodity(comCode, null);
        String baseCommodity = "";
        if (CommonUtils.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                Object[] object = (Object[]) list.get(i);
                baseCommodity = null != object[3] ? object[3].toString() : "";
                break;
            }
        } else {
            GenericCode genericCode = new GenericCodeDAO().getGenericCodeId("4", comCode);
            if (null != genericCode) {
                baseCommodity = "" + genericCode.getId();
            }
        }
        List listofFclStdId = new ArrayList();
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, String> newSet = new LinkedHashMap<String, String>();
        Map<String, Map<String, String>> regionMap = new LinkedHashMap<String, Map<String, String>>();
        String previousRegion = null;
        String fclRatesOriginServiceDup = "";
        queryBuilder.append("select buy.destination_port, if(dest.statecode != '' and dest.statecode ");
        queryBuilder.append("is not null,concat(dest.portname,'/',dest.statecode,");
        queryBuilder.append("if(dest.countryname != '' and dest.countryname is not null,concat('/','<font color=blue><b>',");
        queryBuilder.append("dest.countryname,'</b></font>'),''),if(dest.unlocationcode != '' and dest.unlocationcode ");
        queryBuilder.append("is not null,concat('(',dest.unlocationcode,')'),'')),concat(dest.portname,if(dest.countryname != '' ");
        queryBuilder.append("and dest.countryname is not null,concat('/','<font color=blue><b>',dest.countryname,'</b></font>'),''),");
        queryBuilder.append("if(dest.unlocationcode != '' and dest.unlocationcode is not null,concat('(',dest.unlocationcode,");
        queryBuilder.append("')'),''))) as destinationname from fcl_buy buy join ports org on buy.origin_terminal=org.id join ");
        queryBuilder.append("ports dest on buy.destination_port=dest.id,fcl_buy_cost cost where buy.com_num = '").append(baseCommodity).append("' ");
        queryBuilder.append("and buy.fcl_std_id=cost.fcl_std_id");
        if (fclRatesOriginService.length() > 1) {
            fclRatesOriginServiceDup = fclRatesOriginService.replaceAll(" ", "-");
        }
        if (searchBy.equalsIgnoreCase("city")) {
            desc = desc.replace("'", "'+'");
            if (CommonUtils.isNotEmpty(code) && CommonUtils.isNotEmpty(desc)) {
                if (CommonUtils.isNotEmpty(fclRatesOriginService) && CommonUtils.isNotEmpty(fclRatesOriginServiceDup)) {
                    queryBuilder.append(" and org.portname='").append(desc).append("' and org.unlocationcode='").append(code);
                    queryBuilder.append("' and (dest.portname like '").append(fclRatesOriginService).append("%' or dest.portname like '");
                    queryBuilder.append(fclRatesOriginServiceDup).append("%')");
                } else {
                    queryBuilder.append(" and org.portname='").append(desc).append("' and org.unlocationcode='").append(code);
                    queryBuilder.append("' and dest.portname like '").append(fclRatesOriginService).append("%'");
                }
            } else {
                if (CommonUtils.isNotEmpty(fclRatesOriginService) && CommonUtils.isNotEmpty(fclRatesOriginServiceDup)) {
                    queryBuilder.append(" and (dest.portname like '").append(fclRatesOriginService);
                    queryBuilder.append("%' or dest.portname like '").append(fclRatesOriginServiceDup).append("%')");
                } else {
                    queryBuilder.append(" and dest.portname like '").append(fclRatesOriginService).append("%'");
                }
            }
        } else {
            if (CommonUtils.isNotEmpty(code) && CommonUtils.isNotEmpty(desc)) {
                desc = desc.replace("'", "\\'");
                queryBuilder.append(" and org.portname='").append(desc).append("' and org.unlocationcode='").append(code);
                queryBuilder.append("' and dest.countryname like '").append(fclRatesOriginService).append("%'");
            } else {
                queryBuilder.append(" and dest.countryname like '").append(fclRatesOriginService).append("%'");
            }
        }
        queryBuilder.append(" group by dest.countryname, dest.portname order by dest.countryname, dest.portname");
        listofFclStdId = getCurrentSession().createSQLQuery(queryBuilder.toString()).addScalar("buy.destination_port", StringType.INSTANCE).addScalar("destinationname", StringType.INSTANCE).list();
        Iterator iter = listofFclStdId.iterator();
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            if (null != previousRegion && row.length > 2 && !previousRegion.equals(row[2].toString())) {
                regionMap.put(previousRegion, newSet);
                newSet = new LinkedHashMap<String, String>();
                newSet.put(null != row[0] ? row[0].toString() : "", null != row[1] ? row[1].toString() : "");
                previousRegion = row.length > 2 ? row[2].toString() : "NO Region";
            } else {
                newSet.put(null != row[0] ? row[0].toString() : "", null != row[1] ? row[1].toString() : "");
                previousRegion = row.length > 2 ? row[2].toString() : "NO Region";
            }
        }
        regionMap.put(previousRegion, newSet);
        return regionMap;
    }

    public List getDestinationsForOrigin(String originCode, String destinationDescrip, String searchBy) throws Exception {
        List listofFclStdId = new ArrayList();
        StringBuilder queryBuilder = new StringBuilder();
        destinationDescrip = destinationDescrip.replace("'", "");
        queryBuilder.append("select concat(dest.un_loc_name,if(st.code != '',concat('/', st.code),''),concat('/', cn.codedesc),");
        queryBuilder.append("concat('(', dest.un_loc_code, ')')) as origin");
        queryBuilder.append(" from fcl_buy fb");
        queryBuilder.append(" join fcl_buy_cost fbc");
        queryBuilder.append(" on (fb.fcl_std_id = fbc.fcl_std_id)");
        if (CommonUtils.isNotEmpty(originCode)) {
            queryBuilder.append(" join un_location org on (fb.origin_terminal = org.id and org.un_loc_code = '");
            queryBuilder.append(originCode).append("')");
        }
        queryBuilder.append("join un_location dest on (fb.destination_port = dest.id ");
        if (searchBy.equalsIgnoreCase("city")) {
            queryBuilder.append(" and (dest.search_un_loc_name like '");
            queryBuilder.append(destinationDescrip).append("%' or dest.search_un_loc_name like '").append(destinationDescrip);
            queryBuilder.append("%' or dest.un_loc_code like'").append(destinationDescrip).append("%')");
        }
        queryBuilder.append(")join genericcode_dup ge  on (fbc.cost_id = ge.id and (ge.code = 'OCNFRT' or ge.code = 'OFIMP')) left join genericcode_dup st ");
        queryBuilder.append("on (dest.statecode = st.id)");
        queryBuilder.append(" join genericcode_dup cn on (dest.countrycode = cn.id");
        if (!searchBy.equalsIgnoreCase("city")) {
            queryBuilder.append(" and  cn.codedesc like '").append(destinationDescrip).append("%'");
        }
        queryBuilder.append(")");
        queryBuilder.append(" JOIN fcl_buy_cost_type_rates rates ON rates.fcl_cost_id= fbc.fcl_cost_id ");
        queryBuilder.append("group by fb.destination_port order by dest.search_un_loc_name ");
        listofFclStdId = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        return listofFclStdId;
    }

    public List getDestinationsForOriginFromDwr(String origin, String country) throws Exception {
        GenericCode genericCode = genericCodeDAO.findByCodeDescName(country, 11);
        String desc = StringFormatter.getTerminalFromInputStringr(origin);
        String code = StringFormatter.orgDestStringFormatter(origin);
        desc = desc.replace("'", "'+'");
        String queryString1 = "Select fb.destinationPort.id,fb.destinationPort.id from FclBuy fb,FclBuyCost fbc where "
                + "fb.originTerminal.unLocationName='" + desc + "' and fb.originTerminal.unLocationCode='" + code + "' and fb.fclStdId=fbc.fclStdId and fbc.costId.code='OCNFRT'";
        List listofFclStdId = getCurrentSession().createQuery(queryString1).list();

        Iterator iter = listofFclStdId.iterator();
        Set newSet = new HashSet();
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer id = (Integer) row[0];
            String queryString2 = "from UnLocation where id='" + id + "' and countryId.id='" + genericCode.getId() + "'";
            List ratesList = getCurrentSession().createQuery(queryString2).list();
            for (Iterator iterator = ratesList.iterator(); iterator.hasNext();) {
                UnLocation unLocation = (UnLocation) iterator.next();
                if (unLocation.getStateId() != null) {
                    newSet.add(StringFormatter.formatForDestination(unLocation));
                } else {
                    newSet.add(StringFormatter.formatForDestination(unLocation));
                }
            }
        }
        listofFclStdId.clear();
        listofFclStdId.addAll(newSet);
        Collections.sort(listofFclStdId);
        List newList = new ArrayList();
        for (Iterator iterator = listofFclStdId.iterator(); iterator.hasNext();) {
            String name = (String) iterator.next();
            newList.add(new LabelValueBean(name, name));
        }
        return newList;
    }

    public List getDestinationCountriesForOriginFromDwr(String origin, Integer regionCode) throws Exception {
        String code = "";
        String desc = "";
        if (origin.startsWith("Minneapolis/St Paul Apt/USMSP/MN") || origin.startsWith("Coussol/Fos sur Mer/FRCOU")) {
            if (origin.indexOf("/") != -1) {
                String array[] = origin.split("/");
                desc = array[0] + "/" + array[1];
                int index = origin.lastIndexOf("(");
                if (index != -1) {
                    code = origin.substring(index + 1, origin.lastIndexOf(")"));
                }
            }
        } else {
            if (origin.indexOf("/") != -1) {
                String array[] = origin.split("/");
                desc = array[0];
                int index = origin.lastIndexOf("(");
                if (index != -1) {
                    code = origin.substring(index + 1, origin.lastIndexOf(")"));
                }
            }
        }
        desc = desc.replace("'", "'+'");

        String queryString1 = "Select fb.destinationPort.id,fb.destinationPort.id from FclBuy fb,FclBuyCost fbc where "
                + "fb.originTerminal.unLocationName='" + desc + "' and fb.originTerminal.unLocationCode='" + code + "' and fb.fclStdId=fbc.fclStdId and fbc.costId.code='OCNFRT'";
        List listofFclStdId = getCurrentSession().createQuery(queryString1).list();

        Iterator iter = listofFclStdId.iterator();
        Set newSet = new HashSet();
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer id = (Integer) row[0];
            String queryString2 = "from UnLocation where id='" + id + "'";
            List ratesList = getCurrentSession().createQuery(queryString2).list();
            for (Iterator iterator = ratesList.iterator(); iterator.hasNext();) {
                UnLocation unLocation = (UnLocation) iterator.next();
                List tempList = portsDAO.getAllCountryByRegion1("" + regionCode, unLocation.getUnLocationName());
                if (tempList.size() > 0) {
                    if (unLocation.getCountryId() != null && unLocation.getCountryId().getCodetypeid().equals(11)) {
                        newSet.add((String) unLocation.getCountryId().getCodedesc());
                    }
                }
            }
        }
        listofFclStdId.clear();
        listofFclStdId.addAll(newSet);
        Collections.sort(listofFclStdId);
        List newList = new ArrayList();

        newList.add(new LabelValueBean("Select One", "0"));
        for (Iterator iterator = listofFclStdId.iterator(); iterator.hasNext();) {
            String name = (String) iterator.next();
            newList.add(new LabelValueBean(name, name));
        }
        return newList;
    }

    public List getSslineId3(String org, String Dest, String ssline, String commodity, String ssline1, Date noOfDays, MessageResources messageResources) throws Exception {
        List percentList = new ArrayList();
        List<CostBean> fclcontList = new ArrayList<CostBean>();
        List<CostBean> costlist = new ArrayList<CostBean>();
        String queryString1 = "Select fb.fclStdId from FclBuy fb where fb.originTerminal.id='" + org + "'and fb.destinationPort.id='" + Dest + "' and fb.comNum.id='" + commodity + "' and fb.sslineNo.accountno='" + ssline1 + "' ";
        List listofFclStdId = getCurrentSession().createQuery(queryString1).list();
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        List fclcostidList = new ArrayList();
        List perkglbsList = new ArrayList();
        List CostCodedesc = new ArrayList();
        for (int i = 0; i < listofFclStdId.size(); i++) {
            int fclstdid = (Integer) listofFclStdId.get(i);
            String queryString3 = "Select fbc.fclCostId from FclBuyCost fbc where fbc.fclStdId='" + fclstdid + "' order by fbc.costId.id";
            List tempfclcostidList = getCurrentSession().createQuery(queryString3).list();
            if (!tempfclcostidList.isEmpty()) {
                int j = 0;
                while (j < tempfclcostidList.size()) {
                    fclcostidList.add(tempfclcostidList.get(j));
                    j++;
                }
            }
        }
        for (int l = 0; l < fclcostidList.size(); l++) {
            int fclcostid = (Integer) fclcostidList.get(l);
            String queryString5 = "Select fbcr.unitType.id,fbcr.ratAmount,fbcr.fclCostId,fbcr.markup,fbcr.activeAmt,fbcr.currency.code,fbcr.minimumAmt from FclBuyCostTypeRates fbcr where fbcr.fclCostId='" + fclcostid + "'";
            List queryObj = getCurrentSession().createQuery(queryString5).list();
            Iterator itr = queryObj.iterator();
            while (itr.hasNext()) {
                Object[] row = (Object[]) itr.next();
                if (row[0] == null) {
                    CostBean c1 = new CostBean();
                    if (row[1] != null) {
                        c1.setActiveAmt(number.format(row[4]));
                    }
                    Integer fclcostId = 0;
                    if (row[2] != null) {
                        fclcostId = (Integer) row[2];
                        String costType = getCostType(fclcostId);
                        c1.setCostType(costType);
                        String chargecodedesc = getchargecodeDesc(fclcostId);
                        String chargeCode = getchargecode(fclcostId);
                        c1.setChargecode(chargecodedesc);
                        c1.setChargeCodedesc(chargeCode);
                        String sslineNo = getSslineName(fclcostId);
                        c1.setSsLineNumber(sslineNo);
                    }
                    Double retail = 0.00;
                    if (row[1] != null) {
                        retail = (Double) row[1];
                    }

                    c1.setUnitType("0.00");
                    String currency = "";
                    if (row[5] != null) {
                        currency = (String) row[5];
                    }
                    Double minimum = 0.00;
                    if (row[6] != null) {
                        minimum = (Double) row[6];
                    }
                    c1.setCurrency(currency);
                    c1.setRetail(number.format(retail));
                    c1.setMinimum(number.format(minimum));

                    if (c1.getCostType().trim().equalsIgnoreCase(messageResources.getMessage("per1000kg")) || c1.getCostType().trim().equalsIgnoreCase(messageResources.getMessage("per2000lbs"))) {
                        perkglbsList.add(c1);
                    }
                    if (c1.getCostType().equals(messageResources.getMessage("percentofr"))) {
                        c1.setNumber("1");
                        percentList.add(c1);
                    }
                    if (c1.getCostType().trim().equals(messageResources.getMessage("perbl")) || c1.getCostType().trim().equals(messageResources.getMessage("Flatratepercontainer"))) {
                        boolean flag = false;
                        if (fclcontList.size() > 0) {
                            for (int i = 0; i < fclcontList.size(); i++) {
                                CostBean c2 = (CostBean) fclcontList.get(i);
                                if (c2.getChargecode().equals(c1.getChargecode())) {
                                    flag = true;
                                    break;
                                }
                            }
                            if (!flag) {
                                fclcontList.add(c1);
                            }
                        } else {
                            fclcontList.add(c1);
                        }
                    }
                } else {
                    int unit = (Integer) row[0];
                    String unittype = String.valueOf(unit);
                    Double actAmt = (Double) row[4];
                    if (actAmt == null || actAmt.equals("0.0")) {
                        actAmt = 0.00;
                    }
                    Integer fclcostId = 0;
                    String chargecodedesc = "";
                    String chargeCode = "";
                    String costType = "";
                    String sslineNo = "";
                    if (row[2] != null) {
                        fclcostId = (Integer) row[2];
                        chargecodedesc = getchargecodeDesc(fclcostId);
                        chargeCode = getchargecode(fclcostId);
                        costType = getCostType(fclcostId);
                        sslineNo = getSslineName(fclcostId);
                    }
                    String currency = "";
                    if (row[5] != null) {
                        currency = (String) row[5];
                    }
                    Double markUp = (Double) row[3];
                    CostBean cbean = new CostBean();
                    for (int i = 0; i < costlist.size(); i++) {
                        cbean = (CostBean) costlist.get(i);
                        if (cbean.getUnitType().equals(unittype) && cbean.getChargecode().equals(chargecodedesc)) {
                        } else {
                            cbean = new CostBean();
                        }
                    }
                    if (markUp == null) {
                        markUp = 0.00;
                    }
                    cbean.setSsLineNumber(sslineNo);
                    cbean.setChargecode(chargecodedesc);
                    cbean.setChargeCodedesc(chargeCode);
                    cbean.setCostType(costType);
                    cbean.setUnitType(unittype);
                    cbean.setCurrency(currency);
                    cbean.setNumber("1");
                    String unitType[] = messageResources.getMessage("unittype").split(",");

                    if (unittype.equals(unitType[0])) {
                        cbean.setSetA(number.format(actAmt));
                        cbean.setMarkUpA(number.format(markUp));
                    } else if (unittype.equals(unitType[1])) {
                        cbean.setSetD(number.format(actAmt));
                        cbean.setMarkUpD(number.format(markUp));
                    } else if (unittype.equals(unitType[2])) {
                        cbean.setSetB(number.format(actAmt));
                        cbean.setMarkUpB(number.format(markUp));
                    } else if (unittype.equals(unitType[3])) {
                        cbean.setSetE(number.format(actAmt));
                        cbean.setMarkUpE(number.format(markUp));
                    } else if (unittype.equals(unitType[4])) {
                        cbean.setSetC(number.format(actAmt));
                        cbean.setMarkUpC(number.format(markUp));
                    } else if (unittype.equals(unitType[5])) {
                        cbean.setSetF(number.format(actAmt));
                        cbean.setMarkUpF(number.format(markUp));
                    } else if (unittype.equals(unitType[6])) {
                        cbean.setSetG(number.format(actAmt));
                        cbean.setMarkUpG(number.format(markUp));
                    } else if (unittype.equals(unitType[7])) {

                        cbean.setSetH(number.format(actAmt));
                        cbean.setMarkUpH(number.format(markUp));
                    } else if (unittype.equals(unitType[8])) {
                        cbean.setSetI(number.format(actAmt));
                        cbean.setMarkUpI(number.format(markUp));
                    } else if (unittype.equals(unitType[9])) {
                        cbean.setSetJ(number.format(actAmt));
                        cbean.setMarkUpJ(number.format(markUp));
                    } else if (unittype.equals(unitType[10])) {
                        cbean.setSetK(number.format(actAmt));
                        cbean.setMarkUpK(number.format(markUp));
                    } else if (unittype.equals(unitType[11])) {
                        cbean.setSetL(number.format(actAmt));
                        cbean.setMarkUpL(number.format(markUp));
                    } else if (unittype.equals(unitType[12])) {
                        cbean.setSetM(number.format(actAmt));
                        cbean.setMarkUpM(number.format(markUp));
                    } else if (unittype.equals(unitType[13])) {
                        cbean.setSetN(number.format(actAmt));
                        cbean.setMarkUpN(number.format(markUp));
                    } else if (unittype.equals(unitType[14])) {
                        cbean.setSetO(number.format(actAmt));
                        cbean.setMarkUpO(number.format(markUp));
                    } else if (unittype.equals(unitType[15])) {
                        cbean.setSetP(number.format(actAmt));
                        cbean.setMarkUpP(number.format(markUp));
                    } else if (unittype.equals(unitType[16])) {
                        cbean.setSetQ(number.format(actAmt));
                        cbean.setMarkUpQ(number.format(markUp));
                    }
                    if (ssline.equals(sslineNo)) {
                        if (!unittype.equals("0")) {
                            if (actAmt.toString().trim().equals("0.0")) {
                            } else {
                                if (cbean.getCostType().equals(messageResources.getMessage("percentofr"))) {
                                } else {
                                    costlist.add(cbean);
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int j = 0; j < perkglbsList.size(); j++) {
            CostBean c1 = (CostBean) perkglbsList.get(j);
            costlist.add(c1);
        }
        for (int i = 0; i < fclcontList.size(); i++) {
            CostBean c1 = (CostBean) fclcontList.get(i);
            costlist.add(c1);
        }
        List percentList1 = new ArrayList();
        String retail = "0.00";
        Double actAmt = 0.00;
        for (int J = 0; J < percentList.size(); J++) {
            CostBean cbean = (CostBean) percentList.get(J);
            String tempchargeCode = cbean.getChargecode();
            String tempcostType = cbean.getCostType();
            String tempChargeCodedesc = cbean.getChargeCodedesc();
            String tempSlline = cbean.getSsLineNumber();
            String tempREtail = cbean.getRetail();
            String tempCurrency = cbean.getCurrency();
            if (cbean.getCostType() != null && cbean.getCostType().equals(messageResources.getMessage("percentofr"))) {
                String amt = "";
                for (int i = 0; i < costlist.size(); i++) {
                    CostBean c1 = (CostBean) costlist.get(i);
                    cbean.setCostType(tempcostType);
                    cbean.setChargecode(tempchargeCode);
                    cbean.setChargeCodedesc(tempChargeCodedesc);
                    cbean.setSsLineNumber(tempSlline);
                    cbean.setRetail(tempREtail);
                    cbean.setCurrency(tempCurrency);
                    if (c1.getChargecode() != null && c1.getChargecode().equalsIgnoreCase(messageResources.getMessage("oceanfreight")) && c1.getSsLineNumber().equals(cbean.getSsLineNumber())) {
                        if (c1.getCostType().equals(messageResources.getMessage("FlatRatePerConatinerSize"))) {
                            cbean.setNumber("1");
                            String unitType[] = messageResources.getMessage("unittype").split(",");
                            if (c1.getUnitType().equals(unitType[0])) {
                                cbean.setUnitType(unitType[0]);
                                amt = c1.getSetA();
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetA(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpA("0.00");
                            } else if (c1.getUnitType().equals(unitType[1])) {
                                cbean.setUnitType(unitType[1]);
                                amt = c1.getSetD();
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetD(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpD("0.00");
                            } else if (c1.getUnitType().equals(unitType[2])) {
                                cbean.setUnitType(unitType[2]);
                                amt = c1.getSetB();
                                if (cbean.getRetail() == null) {
                                    cbean.setRetail("0.00");
                                }
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetB(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpB("0.00");
                            } else if (c1.getUnitType().equals(unitType[3])) {
                                cbean.setUnitType(unitType[3]);
                                amt = c1.getSetE();
                                if (cbean.getRetail() == null) {
                                    cbean.setRetail("0.00");
                                }
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetE(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpE("0.00");
                            } else if (c1.getUnitType().equals(unitType[4])) {
                                cbean.setUnitType(unitType[4]);
                                amt = c1.getSetC();
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetC(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpC("0.00");
                            } else if (c1.getUnitType().equals(unitType[5])) {
                                cbean.setUnitType(unitType[5]);
                                amt = c1.getSetF();
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetF(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpF("0.00");
                            } else if (c1.getUnitType().equals(unitType[6])) {
                                cbean.setUnitType(unitType[6]);
                                amt = c1.getSetG();
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetG(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpG("0.00");
                            } else if (c1.getUnitType().equals(unitType[7])) {
                                cbean.setUnitType(unitType[7]);
                                amt = c1.getSetH();
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetH(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpH("0.00");
                            } else if (c1.getUnitType().equals(unitType[8])) {
                                cbean.setUnitType(unitType[8]);
                                amt = c1.getSetI();
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetI(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpI("0.00");
                            } else if (c1.getUnitType().equals(unitType[9])) {
                                cbean.setUnitType(unitType[9]);
                                amt = c1.getSetJ();
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetJ(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpJ("0.00");
                            } else if (c1.getUnitType().equals(unitType[10])) {
                                cbean.setUnitType(unitType[10]);
                                amt = c1.getSetK();
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetK(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpK("0.00");
                            } else if (c1.getUnitType().equals(unitType[11])) {
                                cbean.setUnitType(unitType[11]);
                                amt = c1.getSetL();
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetL(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpL("0.00");
                            } else if (c1.getUnitType().equals(unitType[12])) {
                                cbean.setUnitType(unitType[12]);
                                amt = c1.getSetM();
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetM(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpM("0.00");
                            } else if (c1.getUnitType().equals(unitType[13])) {
                                cbean.setUnitType(unitType[13]);
                                amt = c1.getSetN();
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetN(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpN("0.00");
                            } else if (c1.getUnitType().equals(unitType[14])) {
                                cbean.setUnitType(unitType[14]);
                                amt = c1.getSetO();
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetO(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpO("0.00");
                            } else if (c1.getUnitType().equals(unitType[15])) {
                                cbean.setUnitType(unitType[15]);
                                amt = c1.getSetP();
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetP(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpP("0.00");
                            } else if (c1.getUnitType().equals(unitType[16])) {
                                cbean.setUnitType(unitType[16]);
                                amt = c1.getSetQ();
                                retail = cbean.getRetail();
                                actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                                cbean.setSetQ(number.format(actAmt));
                                cbean.setRetail("0.00");
                                cbean.setMarkUpQ("0.00");
                            }
                        } else {
                            amt = c1.getRetail();
                            retail = cbean.getRetail();
                            actAmt = Double.parseDouble(dbUtil.removeComma(amt)) * Double.parseDouble(dbUtil.removeComma(retail)) / 100;
                            cbean.setRetail(number.format(actAmt));
                            cbean.setNumber("1");
                        }
                    }
                    percentList1.add(cbean);
                    cbean = new CostBean();
                }
            }
        }
        for (int i = 0; i < percentList1.size(); i++) {
            CostBean c1 = (CostBean) percentList1.get(i);
            if (c1.getUnitType() != null) {
                costlist.add(c1);
            }
        }
        for (int l = 0; l < fclcostidList.size(); l++) {
            int fclcostid = (Integer) fclcostidList.get(l);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            Iterator itr = null;
            String noOfDay = sdf.format(noOfDays);
            Query queryString5 = getCurrentSession().createQuery("Select fbcr.unitType.id,fbcr.ratAmount,fbcr.fclCostId,fbcr.markup,fbcr.activeAmt,fbcr.effectiveDate,fbcr.currency.code from FclBuyCostTypeFutureRates fbcr where fbcr.fclCostId='" + fclcostid + "' and fbcr.effectiveDate between fbcr.effectiveDate and ?0");
            queryString5.setParameter("0", df.parse(noOfDay));
            List queryObj = queryString5.list();
            itr = queryObj.iterator();
            if (itr != null) {
                while (itr.hasNext()) {
                    Object[] row = (Object[]) itr.next();
                    if (row[0] == null) {
                        row[0] = 0;
                    }
                    int unit = (Integer) row[0];
                    String unittype = String.valueOf(unit);
                    actAmt = (Double) row[1];
                    if (actAmt == null || actAmt.equals("0.0")) {
                        actAmt = 0.00;
                    }
                    Integer fclcostId = (Integer) row[2];
                    Double markUp = (Double) row[3];
                    if (markUp == null || markUp.equals("0.0")) {
                        markUp = 0.00;
                    }
                    Double ratAmount = (Double) row[4];
                    if (ratAmount == null || ratAmount.equals("0.0")) {
                        ratAmount = 0.00;
                    }
                    Date effectiveDate = (Date) row[5];
                    String currency = (String) row[6];
                    String chargeCode = getchargecode(fclcostId);
                    String chargecodedesc = getchargecodeDesc(fclcostId);
                    String costType = getCostType(fclcostId);
                    String sslineNo = getSslineName(fclcostId);
                    if (effectiveDate != null) {
                        boolean flag = false;
                        for (int i = 0; i < costlist.size(); i++) {
                            CostBean c1 = (CostBean) costlist.get(i);
                            if (c1.getSsLineNumber().equals(sslineNo) && c1.getCostType().equals(costType) && c1.getChargecode().equals(chargecodedesc)) {
                                flag = true;
                                if (costType.equals(messageResources.getMessage("FlatRatePerConatinerSize"))) {
                                    if (c1.getUnitType() != null && c1.getUnitType().equals(unittype)) {
                                        if (c1.getEffectiveDate() == null) {
                                            c1.setEffectiveDate(sdf.format(effectiveDate));
                                        } else {
                                            c1.setEffectiveDate(" ");
                                        }
                                        String unitType[] = messageResources.getMessage("unittype").split(",");
                                        if (unittype.equals(unitType[0])) {
                                            c1.setFutureRateA(number.format(actAmt));
                                        } else if (unittype.equals(unitType[1])) {
                                            c1.setFutureRateD(number.format(actAmt));
                                        } else if (unittype.equals(unitType[2])) {
                                            c1.setFutureRateB(number.format(actAmt));
                                        } else if (unittype.equals(unitType[3])) {
                                            c1.setFutureRateE(number.format(actAmt));
                                        } else if (unittype.equals(unitType[4])) {
                                            c1.setFutureRateC(number.format(actAmt));
                                        } else if (unittype.equals(unitType[5])) {
                                            c1.setFutureRateF(number.format(actAmt));
                                        } else if (unittype.equals(unitType[6])) {
                                            c1.setFutureRateG(number.format(actAmt));
                                        } else if (unittype.equals(unitType[7])) {
                                            c1.setFutureRateH(number.format(actAmt));
                                        } else if (unittype.equals(unitType[8])) {
                                            c1.setFutureRateI(number.format(actAmt));
                                        } else if (unittype.equals(unitType[9])) {
                                            c1.setFutureRateJ(number.format(actAmt));
                                        } else if (unittype.equals(unitType[10])) {
                                            c1.setFutureRateK(number.format(actAmt));
                                        } else if (unittype.equals(unitType[11])) {
                                            c1.setFutureRateL(number.format(actAmt));
                                        } else if (unittype.equals(unitType[12])) {
                                            c1.setFutureRateM(number.format(actAmt));
                                        } else if (unittype.equals(unitType[13])) {
                                            c1.setFutureRateN(number.format(actAmt));
                                        } else if (unittype.equals(unitType[14])) {
                                            c1.setFutureRateO(number.format(actAmt));
                                        } else if (unittype.equals(unitType[15])) {
                                            c1.setFutureRateP(number.format(actAmt));
                                        } else if (unittype.equals(unitType[16])) {
                                            c1.setFutureRateQ(number.format(actAmt));
                                        }
                                    }
                                } else if (costType.equals(messageResources.getMessage("Flatratepercontainer"))) {
                                    c1.setRetailFuture(number.format(ratAmount));
                                    if (c1.getEffectiveDate() == null) {
                                        c1.setEffectiveDate(sdf.format(effectiveDate));
                                    }
                                } else if (costType.equals(messageResources.getMessage("perbl"))) {
                                    c1.setRetailFuture(number.format(ratAmount));
                                    if (c1.getOtherEffectiveDate() == null) {
                                        c1.setOtherEffectiveDate(sdf.format(effectiveDate));
                                    }
                                }
                            }
                        }
                        if (!flag) {
                            CostBean c4 = new CostBean();
                            c4.setSsLineNumber(sslineNo);
                            c4.setChargecode(chargecodedesc);
                            c4.setCostType(costType);
                            c4.setChargeCodedesc(chargeCode);
                            c4.setNumber("1");
                            c4.setCurrency(currency);
                            if (costType.equals(messageResources.getMessage("FlatRatePerConatinerSize"))) {
                                c4.setUnitType(unittype);
                                if (c4.getEffectiveDate() == null) {
                                    c4.setEffectiveDate(sdf.format(effectiveDate));
                                } else {
                                    c4.setEffectiveDate(" ");
                                }
                                c4.setInclude("on");
                                c4.setPrint("on");
                                String unitTypes[] = messageResources.getMessage("unittype").split(",");
                                if (unittype.equals(unitTypes[0])) {
                                    c4.setSetA("0.00");
                                    c4.setFutureRateA(number.format(actAmt));
                                    c4.setMarkUpA("0.00");
                                } else if (unittype.equals(unitTypes[1])) {
                                    c4.setFutureRateD(number.format(actAmt));
                                    c4.setSetD("0.00");
                                    c4.setMarkUpD("0.00");
                                } else if (unittype.equals(unitTypes[2])) {
                                    c4.setFutureRateB(number.format(actAmt));
                                    c4.setSetB("0.00");
                                    c4.setMarkUpB("0.00");
                                } else if (unittype.equals(unitTypes[3])) {
                                    c4.setFutureRateE(number.format(actAmt));
                                    c4.setSetE("0.00");
                                    c4.setMarkUpE("0.00");
                                } else if (unittype.equals(unitTypes[4])) {
                                    c4.setFutureRateC(number.format(actAmt));
                                    c4.setSetC("0.00");
                                    c4.setMarkUpC("0.00");
                                } else if (unittype.equals(unitTypes[5])) {
                                    c4.setFutureRateF(number.format(actAmt));
                                    c4.setSetF("0.00");
                                    c4.setMarkUpF("0.00");
                                } else if (unittype.equals(unitTypes[6])) {
                                    c4.setFutureRateG(number.format(actAmt));
                                    c4.setSetG("0.00");
                                    c4.setMarkUpG("0.00");
                                } else if (unittype.equals(unitTypes[7])) {
                                    c4.setFutureRateH(number.format(actAmt));
                                    c4.setSetH("0.00");
                                    c4.setMarkUpH("0.00");
                                } else if (unittype.equals(unitTypes[8])) {
                                    c4.setFutureRateI(number.format(actAmt));
                                    c4.setSetI("0.00");
                                    c4.setMarkUpI("0.00");
                                } else if (unittype.equals(unitTypes[9])) {
                                    c4.setFutureRateJ(number.format(actAmt));
                                    c4.setSetJ("0.00");
                                    c4.setMarkUpJ("0.00");
                                } else if (unittype.equals(unitTypes[10])) {
                                    c4.setFutureRateK(number.format(actAmt));
                                    c4.setSetK("0.00");
                                    c4.setMarkUpK("0.00");
                                } else if (unittype.equals(unitTypes[11])) {
                                    c4.setFutureRateL(number.format(actAmt));
                                    c4.setSetL("0.00");
                                    c4.setMarkUpL("0.00");
                                } else if (unittype.equals(unitTypes[12])) {
                                    c4.setFutureRateM(number.format(actAmt));
                                    c4.setSetM("0.00");
                                    c4.setMarkUpM("0.00");
                                } else if (unittype.equals(unitTypes[13])) {
                                    c4.setFutureRateN(number.format(actAmt));
                                    c4.setSetN("0.00");
                                    c4.setMarkUpN("0.00");
                                } else if (unittype.equals(unitTypes[14])) {
                                    c4.setFutureRateO(number.format(actAmt));
                                    c4.setSetO("0.00");
                                    c4.setMarkUpO("0.00");
                                } else if (unittype.equals(unitTypes[15])) {
                                    c4.setFutureRateP(number.format(actAmt));
                                    c4.setSetP("0.00");
                                    c4.setMarkUpP("0.00");
                                } else if (unittype.equals(unitTypes[16])) {
                                    c4.setFutureRateQ(number.format(actAmt));
                                    c4.setSetQ("0.00");
                                    c4.setMarkUpQ("0.00");
                                }
                            } else if (costType.equals(messageResources.getMessage("Flatratepercontainer"))) {
                                c4.setRetailFuture(number.format(ratAmount));
                                c4.setRetail("0.00");
                                c4.setPrint("on");
                                c4.setInclude("on");
                                if (c4.getEffectiveDate() == null) {
                                    c4.setEffectiveDate(sdf.format(effectiveDate));
                                } else {
                                    c4.setEffectiveDate(" ");
                                }
                                c4.setUnitType("0.00");
                            } else if (costType.equals(messageResources.getMessage("perbl"))) {
                                c4.setRetailFuture(number.format(ratAmount));
                                c4.setRetail("0.00");
                                c4.setOtherinclude("on");
                                c4.setOtherprint("on");
                                if (c4.getOtherEffectiveDate() == null) {
                                    c4.setOtherEffectiveDate(sdf.format(effectiveDate));
                                } else {
                                    c4.setOtherEffectiveDate(" ");
                                }
                                c4.setUnitType("0.00");
                            }
                            if (costlist.size() > 0) {
                                boolean flag1 = false;
                                for (int a = 0; a < costlist.size(); a++) {
                                    CostBean c5 = (CostBean) costlist.get(a);
                                    if (c5.getChargecode().equals(c4.getChargecode()) && c5.getUnitType().equals(c4.getUnitType())) {
                                        flag1 = true;
                                        break;
                                    }
                                }
                                if (!flag1) {
                                    costlist.add(c4);

                                }
                            } else {
                                costlist.add(c4);
                            }
                        }
                    }
                }
            }
        }
        return costlist;
    }

    public String getchargecodeDesc(Integer fclcostid) throws Exception {
        String queryString = "Select fbc.costId.codedesc from FclBuyCost fbc where fbc.fclCostId='" + fclcostid + "'";
        Object result = getCurrentSession().createQuery(queryString).setMaxResults(1).uniqueResult();
        return null != result ? result.toString() : "";
    }

    public String getchargecode(Integer fclcostid) throws Exception {
        String queryString = "Select fbc.costId.code from FclBuyCost fbc where fbc.fclCostId='" + fclcostid + "'";
        Object result = getCurrentSession().createQuery(queryString).setMaxResults(1).uniqueResult();
        return null != result ? result.toString() : "";
    }

    public String getSslineName(Integer fclcostid) throws Exception {
        String result = "";
        String queryString = "Select fbc.fclStdId from FclBuyCost fbc where fbc.fclCostId='" + fclcostid + "'";
        Object fclstdid = getCurrentSession().createQuery(queryString).setMaxResults(1).uniqueResult();
        if (null != fclstdid && (Integer) fclstdid != 0) {
            String queryString2 = "Select fb.sslineNo.accountName from FclBuy fb where fb.fclStdId='" + fclstdid + "'";
            result = getCurrentSession().createQuery(queryString2).setMaxResults(1).uniqueResult().toString();
        }
        return result;
    }

    public String getCostType(Integer fclcostid) throws Exception {
        String queryString = "Select fbc.contType.codedesc from FclBuyCost fbc where fbc.fclCostId='" + fclcostid + "'";
        Object result = getCurrentSession().createQuery(queryString).setMaxResults(1).uniqueResult();
        return null != result ? result.toString() : "";

    }

    public String setPortOfExit(String unLoc) throws Exception {
        PortsDAO portsDAO = new PortsDAO();
        String poe = "";
        if (null != unLoc && !unLoc.equalsIgnoreCase("")) {
            Ports ports = portsDAO.getPorts(unLoc);
            if (null != ports.getPortname()) {
                poe = ports.getPortname();
            }
            if (null != ports.getCountryName()) {
                poe = poe + "/" + ports.getCountryName();
            }
            if (null != ports.getCountryName()) {
                poe = poe + "(" + unLoc + ")";
            }
        }
        return poe;
    }

    public String getTerminal(String unlocId, String flag) throws Exception {
        String queryString = "";
        if (flag.equalsIgnoreCase("Origin")) {
            queryString = "select count(*) from fcl_buy where origin_terminal='" + unlocId + "' ";
        } else {
            queryString = "select count(*) from fcl_buy where destination_port='" + unlocId + "' ";
        }
        String terminalCount = getSession().createSQLQuery(queryString).uniqueResult().toString();
        return terminalCount.equals("0") ? "" : "rates present";
    }

    public List<FclBuyCostTypeRates> getHazmatRates(String originTerminal, String destinationPort, String comCode, String ssline) throws Exception {
        try {
            List list = new FclBuyCostDAO().getOtherCommodity(comCode, null);
            String baseCommodity = "";
            if (CommonUtils.isNotEmpty(list)) {
                for (int i = 0; i < list.size(); i++) {
                    Object[] object = (Object[]) list.get(i);
                    baseCommodity = null != object[3] ? object[3].toString() : "";
                    break;
                }
            } else {
                GenericCode genericCode = new GenericCodeDAO().getGenericCodeId("4", comCode);
                if (null != genericCode) {
                    baseCommodity = "" + genericCode.getId();
                }
            }
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT *");
            queryBuilder.append(" FROM fcl_buy_cost_type_rates");
            queryBuilder.append(" WHERE fcl_cost_id = ");
            queryBuilder.append("(SELECT fcl_cost_id FROM fcl_buy_cost");
            queryBuilder.append(" WHERE fcl_std_id = ");
            queryBuilder.append(" (SELECT fcl_std_id FROM Fcl_Buy");
            queryBuilder.append(" WHERE origin_Terminal = '").append(originTerminal).append("'");
            queryBuilder.append(" AND destination_Port = '").append(destinationPort).append("'");
            queryBuilder.append(" AND com_Num='").append(baseCommodity).append("'");
            queryBuilder.append(" AND ssline_No = '").append(ssline).append("'");
            queryBuilder.append(" ORDER BY ssline_No)");
            queryBuilder.append(" AND cost_id='11668')");
            SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
            List<FclBuyCostTypeRates> fclBuyCostTypeRatesList = query.addEntity(FclBuyCostTypeRates.class).list();
            return fclBuyCostTypeRatesList;
        } catch (RuntimeException re) {
            log.info("Getting HazmatRates failed on " + new Date(), re);
            throw re;
        }
    }
}

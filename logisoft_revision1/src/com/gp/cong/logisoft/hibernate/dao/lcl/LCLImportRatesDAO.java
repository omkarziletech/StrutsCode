package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.beans.LclImportsRatesBean;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.GeneralInformationDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.BooleanType;
import org.hibernate.type.StringType;

/**
 *
 * @author lakshh
 */
public class LCLImportRatesDAO extends BaseHibernateDAO {

    private final String databaseSchema;

    public LCLImportRatesDAO() throws Exception {
        this.databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
    }

    public List<LclImportsRatesBean> getLCLImportCharges(String polNo, String podNo, List commodityList,
            List chargeCodeList, List<String> billingTermsList) throws Exception {
        String commodities = commodityList.toString().replace("[", "").replace("]", "");
        StringBuilder sb = new StringBuilder();
        sb.append(appendQuery());
        sb.append(" WHERE r.imppc IN(:billingterms)");
        sb.append(" AND r.orgnum=:polNum");
        sb.append(" AND r.dstnum=:podNum");
        if (null != chargeCodeList && !chargeCodeList.isEmpty()) {
            sb.append(" AND r.chgcod NOT IN (:chargeCode)");
        }
        if (CommonUtils.isNotEmpty(commodityList)) {
            sb.append("  AND r.comnum IN (").append(commodities).append(")");
        }
        sb.append(" AND r.typert <> '6'");
        sb.append(" HAVING totAmount <> 0.000 ");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameterList("billingterms", billingTermsList);
        queryObject.setParameter("polNum", polNo);
        queryObject.setParameter("podNum", podNo);
        if (null != chargeCodeList && !chargeCodeList.isEmpty()) {
            queryObject.setParameterList("chargeCode", chargeCodeList);
        }
        return getQueryObjectList(queryObject);
    }
    
    public List<LclImportsRatesBean> getLclImpStorageCharges(String polNo, String podNo, String destNo, List commodityList) throws Exception {//Import Storage Charges
        String commodities = commodityList.toString().replace("[", "").replace("]", "");
        StringBuilder sb = new StringBuilder();
        sb.append(buildSelectQuery());
        sb.append(" FROM ").append(databaseSchema);
        sb.append(".rates r LEFT JOIN ").append(databaseSchema).append(".chgs c ON r.chgcod=c.chgcde WHERE r.orgnum=").append(polNo);
        sb.append(" AND r.dstnum=").append(podNo).append(" AND r.chgcod =1603").append(" AND r.comnum in  (").append(commodities).append(")");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        return getQueryObjectList(queryObject);
    }

    public List<LclImportsRatesBean> getLCLImportIPICharges(String podNo, String destNo, List commodityList, String ipiIgnoreStatus, List chargeCodeList) throws Exception {
        String commodities = commodityList.toString().replace("[", "").replace("]", "");
        List<LclImportsRatesBean> resultList = new ArrayList();
        String ipiIgnoreQuery = "";
        StringBuilder sb =new StringBuilder();
        if ("Y".equalsIgnoreCase(ipiIgnoreStatus)) {
            ipiIgnoreQuery = " AND r.chgcod <> 1607";
        }
        sb.append(buildSelectQuery());
        sb.append(" FROM ").append(databaseSchema).append(".rates r LEFT JOIN ").append(databaseSchema);
        sb.append(" .chgs c ON r.chgcod=c.chgcde WHERE r.orgnum=:podNo AND r.dstnum=:destNo  AND r.comnum in (:commodities) ");
         if (null != chargeCodeList && !chargeCodeList.isEmpty()) {
            sb.append(" AND r.chgcod NOT IN (:chargeCode)");
        }
        sb.append(ipiIgnoreQuery).append(" HAVING totAmount <> 0.000");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("podNo", podNo);
        queryObject.setParameter("destNo", destNo);
        queryObject.setParameter("commodities", commodities);
        if (null != chargeCodeList && !chargeCodeList.isEmpty()) {
            queryObject.setParameterList("chargeCode", chargeCodeList);
        }
        resultList = getQueryObjectList(queryObject);
        if (CommonUtils.isEmpty(resultList)) {
            sb.setLength(0);
            commodities = "223500";
            sb.append(buildSelectQuery());
            sb.append(" FROM ").append(databaseSchema).append(".rates r LEFT JOIN ").append(databaseSchema);
            sb.append(" .chgs c ON r.chgcod=c.chgcde WHERE r.orgnum=:podNo AND r.dstnum=:destNo AND r.chgcod='1607' ");
            sb.append(" AND r.comnum in (:commodities)").append(ipiIgnoreQuery).append(" HAVING totAmount <> 0.000");
            queryObject = getCurrentSession().createSQLQuery(sb.toString());
            queryObject.setParameter("podNo", podNo);
            queryObject.setParameter("destNo", destNo);
            queryObject.setParameter("commodities", commodities);
            resultList = getQueryObjectList(queryObject);
        }
        return resultList;
    }

    public Iterator getLclImpIPICost(String podNo, String destNo, String impCfsId, List costCodes) throws Exception {
        StringBuilder sb = new StringBuilder();
        List ipiCostList = null;
        String costCode = costCodes.toString().replace("[", "").replace("]", "");
        sb.append("SELECT r.chgcod,r.mincst,r.wghtcs,r.cuftcs,r.cbmcs,r.kgscs,w.vendorno,r.typert,r.blpct,r.inrate,r.insamt,r.flatrt ");
        sb.append("FROM warehouse w JOIN ").append(databaseSchema).append(".rates r ON r.comnum=w.commodityno ");
        sb.append("WHERE w.id=").append(impCfsId).append(" AND r.orgnum='").append(podNo).append("' AND r.dstnum='").append(destNo).append("' AND r.chgcod IN (").append(costCode).append(")");
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        ipiCostList = queryObject.list();
//        }
        return ipiCostList.iterator();
    }

    public String isRates(String polNo, String podNo, String chargeCode, String commodityNo, String billToParty) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT IF(COUNT(*) >0,'true','false') ");
        sb.append("FROM ").append(databaseSchema).append(".rates r ");
        sb.append("LEFT JOIN ").append(databaseSchema).append(".chgs c ON r.chgcod=c.chgcde ");
        sb.append(" WHERE r.imppc= '").append(billToParty.replaceAll("'", "''")).append("'").append(" AND r.orgnum= ").append(polNo.replaceAll("'", "''")).append(" AND r.dstnum=").append(podNo);
        sb.append(" AND r.chgcod NOT IN (").append(chargeCode.replaceAll("'", "''")).append(")");
        sb.append("  AND r.comnum IN ('").append(commodityNo.replaceAll("'", "''")).append("')");
        return (String) getCurrentSession().createSQLQuery(sb.toString()).uniqueResult();
    }

    public List<LclImportsRatesBean> getImportRates(String polNo, String podNo, String chargeCode, String commodityNo, String billToParty) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(appendQuery());
        sb.append(" WHERE r.imppc= '").append(billToParty).append("'").append(" AND r.orgnum= ").append(polNo).append(" AND r.dstnum=").append(podNo);
        sb.append(" AND r.chgcod NOT IN (").append(chargeCode).append(")");
        sb.append("  AND r.comnum IN (").append(commodityNo).append(")");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        return getQueryObjectList(queryObject);
    }

    public String appendQuery() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(buildSelectQuery());
        sb.append("FROM ").append(databaseSchema).append(".rates r ");
        sb.append("LEFT JOIN ").append(databaseSchema).append(".chgs c ON r.chgcod=c.chgcde ");
        return sb.toString();
    }

    public String getChargeCode(String podSchnum, String fdSchnum, String billToParty, String commodityNo) throws Exception {
        StringBuilder sb = new StringBuilder();
        String chargeCode = "";
        sb.append("SELECT CONVERT(GROUP_CONCAT(r.chgcod)USING utf8) FROM ").append(databaseSchema).append(".rates r LEFT JOIN ").append(databaseSchema).append(".chgs c ");
        sb.append("ON r.chgcod=c.chgcde ");
        sb.append(" WHERE r.orgnum= ").append(podSchnum).append(" AND r.dstnum=").append(fdSchnum);
        sb.append("  AND r.comnum IN (").append(commodityNo).append(")");
        chargeCode = (String) getCurrentSession().createSQLQuery(sb.toString()).uniqueResult();
        if ("".equals(chargeCode) || chargeCode == null) {
            StringBuilder sb1 = new StringBuilder();
            sb1.append("SELECT CONVERT(GROUP_CONCAT(r.chgcod)USING utf8) FROM ").append(databaseSchema).append(".rates r LEFT JOIN ").append(databaseSchema).append(".chgs c  ");
            sb1.append("ON r.chgcod=c.chgcde ");
            sb1.append(" WHERE r.orgnum= ").append(podSchnum).append(" AND r.dstnum=").append(fdSchnum);
            sb1.append(" AND r.chgcod IN ('1607')");
            sb1.append("  AND r.comnum IN ('223500')");
            chargeCode = (String) getCurrentSession().createSQLQuery(sb1.toString()).uniqueResult();
        }
        return chargeCode;
    }

    public String isERTYorN(String coloadComm) {
        String sQLQuery = "SELECT o.`impert` FROM " + databaseSchema + ".`ofcomm` o WHERE o.`comcde`= ?0";
        Query queryObject = getCurrentSession().createSQLQuery(sQLQuery);
        queryObject.setParameter("0", coloadComm);
        return (String) queryObject.uniqueResult();
    }

    public String getERT(String[] origin, String[] destination, String commNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT ");
        queryBuilder.append(" IF(");
        queryBuilder.append("  c.impert = 'X',");
        queryBuilder.append("  (SELECT ");
        queryBuilder.append("   IF(COUNT(*) = 0, 'Y', 'N') ");
        queryBuilder.append("   FROM");
        queryBuilder.append("      ").append(databaseSchema).append(".`ofcert` e");
        queryBuilder.append("   WHERE e.`comcde` = c.`comcde` ");
        queryBuilder.append("          and (");
        queryBuilder.append("            e.`origin` = :origin");
        queryBuilder.append("            or e.`orgreg` = :orgreg");
        queryBuilder.append("          ) ");
        queryBuilder.append("          and (");
        queryBuilder.append("            e.`destin` = :destin");
        queryBuilder.append("            or e.`desreg` = :desreg");
        queryBuilder.append("          )),");
        queryBuilder.append("  if(");
        queryBuilder.append("    c.`impert` = 'Y',");
        queryBuilder.append("    (select");
        queryBuilder.append("      if(");
        queryBuilder.append("        count(*) > 0,");
        queryBuilder.append("        (select ");
        queryBuilder.append("          if(count(*) > 0, 'Y', 'N')");

        queryBuilder.append("        from");
        queryBuilder.append("          ").append(databaseSchema).append(".`ofcert` e");
        queryBuilder.append("        where e.`comcde` = c.`comcde` ");
        queryBuilder.append("          and (");
        queryBuilder.append("            e.`origin` = :origin");
        queryBuilder.append("            or e.`orgreg` = :orgreg");
        queryBuilder.append("          ) ");
        queryBuilder.append("          and (");
        queryBuilder.append("            e.`destin` = :destin");
        queryBuilder.append("            or e.`desreg` = :desreg");
        queryBuilder.append("          )),");
        queryBuilder.append("        'Y'");
        queryBuilder.append("      ) ");
        queryBuilder.append("    from");
        queryBuilder.append("      ").append(databaseSchema).append(".`ofcert` e");
        queryBuilder.append("    where e.`comcde` = c.`comcde`),");
        queryBuilder.append("    'N'");
        queryBuilder.append("  )) as ert ");
        queryBuilder.append("from");
        queryBuilder.append("  ").append(databaseSchema).append(".`ofcomm` c ");
        queryBuilder.append("where");
        queryBuilder.append("  c.`comcde` = :comcde");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("origin", origin[0]);
        query.setString("orgreg", origin[1]);
        query.setString("destin", destination[0]);
        query.setString("desreg", destination[1]);
        query.setString("comcde", commNo);
        query.addScalar("ert", StringType.INSTANCE);
        return (String) query.uniqueResult();
    }

    public Boolean isRatesExists(String orgnum, String dstnum, String comnum, String imppc) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  if(count(*) > 0, true, false) as result ");
        queryBuilder.append("from");
        queryBuilder.append("  ").append(databaseSchema).append(".rates r ");
        queryBuilder.append("where");
        queryBuilder.append("  r.`orgnum` = :orgnum");
        queryBuilder.append("  and r.`dstnum` = :dstnum");
        queryBuilder.append("  and r.`comnum` = :comnum");
        queryBuilder.append("  and r.`imppc` = :imppc");
        queryBuilder.append("  and r.`chgcod` <> '1625'");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("orgnum", orgnum);
        query.setString("dstnum", dstnum);
        query.setString("comnum", comnum);
        query.setString("imppc", imppc);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public Map<String, String> checkErtAndRates(String orgUncode, String polUncode, String podUncode, String billingType, String clientNo, String notifyNo, String consNo, String agentNo, String trigger) throws Exception {
        PortsDAO portsDAO = new PortsDAO();
        GeneralInformationDAO generalInformationDAO = new GeneralInformationDAO();
        String[] commodity = null;
        Map<String, String> result = new HashMap<String, String>();
        if ("Client".equalsIgnoreCase(trigger)) {
            commodity = generalInformationDAO.getCommodity(clientNo, "Client", null);
        } else if ("Notify".equalsIgnoreCase(trigger)) {
            if (CommonUtils.isNotEmpty(clientNo)) {
                commodity = generalInformationDAO.getCommodity(clientNo, "Client", null);
            }
            if (null != commodity && CommonUtils.isNotEmpty(commodity[0])) {
                result.put("message", "Client rate exists");
                return result;
            }
            commodity = generalInformationDAO.getCommodity(notifyNo, "Notify", null);
        } else if ("Consignee".equalsIgnoreCase(trigger)) {
            if (CommonUtils.isNotEmpty(clientNo)) {
                commodity = generalInformationDAO.getCommodity(clientNo, "Client", null);
            }
            if (null != commodity && CommonUtils.isNotEmpty(commodity[0])) {
                result.put("message", "Client rate exists");
                return result;
            }
            if (CommonUtils.isNotEmpty(notifyNo)) {
                commodity = generalInformationDAO.getCommodity(notifyNo, "Notify", null);
            }
            if (null != commodity && CommonUtils.isNotEmpty(commodity[0])) {
                result.put("message", "Notify rate exists");
                return result;
            }
            commodity = generalInformationDAO.getCommodity(consNo, "Consignee", null);
        } else {
            if (CommonUtils.isNotEmpty(clientNo)) {
                commodity = generalInformationDAO.getCommodity(clientNo, "Client", null);
            }
            if (null != commodity && CommonUtils.isNotEmpty(commodity[0])) {
                result.put("message", "Client rate exists");
                return result;
            }
            if (CommonUtils.isNotEmpty(notifyNo)) {
                commodity = generalInformationDAO.getCommodity(notifyNo, "Notify", null);
            }
            if (null != commodity && CommonUtils.isNotEmpty(commodity[0])) {
                result.put("message", "Notify rate exists");
                return result;
            }
            if (CommonUtils.isNotEmpty(consNo)) {
                commodity = generalInformationDAO.getCommodity(consNo, "Consignee", null);
            }
            if (null != commodity && CommonUtils.isNotEmpty(commodity[0])) {
                result.put("message", "Consignee rate exists");
                return result;
            }
            commodity = generalInformationDAO.getCommodity(agentNo, "Agent", notifyNo);
        }
        if (null == commodity || CommonUtils.isEmpty(commodity[0])) {
            return null;
        }
        String ert = null;
        String orgSchedNo = null;
        String destSchedNo = null;
        if (CommonUtils.isNotEmpty(orgUncode)) {
            String[] origin = portsDAO.getSchedNoAndRegion(orgUncode);
            String[] destination = portsDAO.getSchedNoAndRegion(podUncode);
            orgSchedNo = origin[0];
            destSchedNo = destination[0];
            ert = getERT(origin, destination, commodity[0]);
        }
        if (CommonUtils.isEmpty(ert)) {
            String[] origin = portsDAO.getSchedNoAndRegion(polUncode);
            String[] destination = portsDAO.getSchedNoAndRegion(podUncode);
            orgSchedNo = origin[0];
            destSchedNo = destination[0];
            ert = getERT(origin, destination, commodity[0]);
        }
        result.put("commodityNo", commodity[0]);
        if ("Y".equalsIgnoreCase(ert)) {
            result.put("message", "ERT will change to Yes and the " + trigger + "'s " + commodity[1] + " commodity rates will be applied.");
        } else {
            if (isRatesExists(orgSchedNo, destSchedNo, commodity[0], billingType)) {
                result.put("message", "This " + trigger + " has a " + commodity[1] + " commodity# and rates. Do you want to change the tariff# and recalculate rates?");
            } else {
                result.put("message", "There is no rate found for " + commodity[1] + " commodity# " + commodity[0] + ".");
            }
        }
        return result;
    }

    public String checkErt(String orgUncode, String polUncode, String podUncode, String commodityNo) throws Exception {
        PortsDAO portsDAO = new PortsDAO();
        String ert = null;
        if (CommonUtils.isNotEmpty(orgUncode)) {
            String[] origin = portsDAO.getSchedNoAndRegion(orgUncode);
            String[] destination = portsDAO.getSchedNoAndRegion(podUncode);
            ert = getERT(origin, destination, commodityNo);
        }
        if (CommonUtils.isEmpty(ert)) {
            String[] origin = portsDAO.getSchedNoAndRegion(polUncode);
            String[] destination = portsDAO.getSchedNoAndRegion(podUncode);
            ert = getERT(origin, destination, commodityNo);
        }
        return ert;
    }

    public String checkNewIpiCost(String commodityNo) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  f.`ignipi` ");
        sb.append("FROM ");
        sb.append(databaseSchema).append(".ofcomm f ");
        sb.append("WHERE f.`comcde` =:commodityNo");
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setString("commodityNo", commodityNo);
        return (String) queryObject.uniqueResult();
    }

    public String getRatesChargeCode(String polSchnum, String podSchnum1, List commodityList) {
        String commodities = commodityList.toString().replace("[", "").replace("]", "");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT GROUP_CONCAT(DISTINCT r.chgcod) as result");
        queryBuilder.append(" FROM ").append(databaseSchema).append(".rates r ");
        queryBuilder.append(" JOIN ").append(databaseSchema).append(".chgs c ON r.chgcod=c.chgcde ");
//        queryBuilder.append(" JOIN `gl_mapping` gl ON (gl.bluescreen_chargecode = c.chgcde   ");
//        queryBuilder.append(" AND gl.transaction_type = 'AR' AND shipment_type = 'LCLI')  ");
        queryBuilder.append(" WHERE r.orgnum = :polSchnum AND dstnum = :polSchnum1   ");
        if (CommonUtils.isNotEmpty(commodityList)) {
            queryBuilder.append(" AND comnum IN(").append(commodities).append(")");
        }
        SQLQuery queryObject = getCurrentSession().createSQLQuery(queryBuilder.toString());
        queryObject.setParameter("polSchnum", polSchnum);
        queryObject.setParameter("polSchnum1", podSchnum1);
        queryObject.addScalar("result", StringType.INSTANCE);
        return (String) queryObject.setMaxResults(1).uniqueResult();
    }

    public List<LclImportsRatesBean> ignoreIpiRatesTrigger(String polNo, String fdNo, List commodityList,
            List chargeCodeList, List<String> billingTermsList) throws Exception {
        String commodities = commodityList.toString().replace("[", "").replace("]", "");
        StringBuilder sb = new StringBuilder();
        sb.append(appendQuery());
        sb.append(" WHERE r.imppc IN( :billingterms)");
        sb.append(" AND r.orgnum=:polNum");
        sb.append(" AND r.dstnum=:podNum");
        if (null != chargeCodeList && !chargeCodeList.isEmpty()) {
            sb.append(" AND r.chgcod NOT IN (:chargeCode)");
        }
        if (CommonUtils.isNotEmpty(commodityList)) {
            sb.append("  AND r.comnum IN (").append(commodities).append(")");
        }
        sb.append(" AND r.typert <> '6'");
        sb.append(" HAVING totAmount <> 0.000 ");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameterList("billingterms", billingTermsList);
        queryObject.setParameter("polNum", polNo);
        queryObject.setParameter("podNum", fdNo);
        if (null != chargeCodeList && !chargeCodeList.isEmpty()) {
            queryObject.setParameterList("chargeCode", chargeCodeList);
        }
        return getQueryObjectList(queryObject);
    }
    
    public List<LclImportsRatesBean> getExceptionList(String poonum, String polnum, String podnum, String fdnum, List commodityList) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  re.`chgcod` as chargeCode,");
        sb.append("  c.chgdsc as chargeDesc,");
        sb.append("  (");
        sb.append("    re.`flatrt` + re.`totpct` + re.`cuftrt` + re.`wghtrt` + re.`minamt` + re.`cbmrt` + re.`kgsrt`");
        sb.append("  ) as totAmount,");
        sb.append("  re.`minamt` as minCharge,");
        sb.append("  re.`chgtyp` as chargeType,");
        sb.append("  re.`totpct` as blpct,");
        sb.append("  re.`wghtrt` as lbs,");
        sb.append("  0.00 as inrate,");
        sb.append("  0.00 as insamt,");
        sb.append("  re.`cbmrt` as cbmrt,");
        sb.append("  re.`kgsrt` as kgsrt,");
        sb.append("  re.`cuftrt` as cft,");
        sb.append("  re.`flatrt` as flatrt,");
        sb.append("  0.00 as maximumCharge,");
        sb.append("  re.`imppcw`as billingType, ");
        sb.append("  re.actcod as actCode");
        sb.append(" FROM ").append(databaseSchema);
        sb.append(".`ratexc` re ");
        sb.append("  LEFT JOIN ").append(databaseSchema).append(".`chgs` c");
        sb.append("  ON (re.`chgcod` = c.`chgcde`) ");
        sb.append(" WHERE re.`poonum` = :poonum ");
        sb.append("  AND re.`polnum` = :polnum ");
        sb.append("  AND re.`podnum` = :podnum ");
        sb.append("  AND re.`fdnum` = :fdnum");
        sb.append("  AND re.`comnum` in (:comnum)");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("poonum", poonum);
        queryObject.setParameter("polnum", polnum);
        queryObject.setParameter("podnum", podnum);
        queryObject.setParameter("fdnum", fdnum);
        queryObject.setParameterList("comnum", commodityList);
        return getQueryObjectList(queryObject);
    }
    
    public List<LclImportsRatesBean> filterOriginalRatesList(List<LclImportsRatesBean> originalRatesList, List<LclImportsRatesBean> exceptionRatesList, String transhipment) {
        List<LclImportsRatesBean> filterRatesList = new ArrayList();
        if (exceptionRatesList.size() > 0 && "N".equalsIgnoreCase(transhipment)) {
            for (LclImportsRatesBean exceptionRatesbean : exceptionRatesList) {
                for (LclImportsRatesBean originalRatesBean : originalRatesList) {
                    if ("A".equalsIgnoreCase(exceptionRatesbean.getActCode())) {
                        filterRatesList.add(exceptionRatesbean);//Added
                        break;
                    } else if (originalRatesBean.getChargeCode().equalsIgnoreCase(exceptionRatesbean.getChargeCode())
                            && "R".equalsIgnoreCase(exceptionRatesbean.getActCode())) {//Replaced
                        filterRatesList.add(exceptionRatesbean);
                        originalRatesList.remove(originalRatesBean);
                    } else if (originalRatesBean.getChargeCode().equalsIgnoreCase(exceptionRatesbean.getChargeCode())
                            && "D".equalsIgnoreCase(exceptionRatesbean.getActCode())) {//Deleted
                        originalRatesList.remove(originalRatesBean);
                        break;
                    }
                }
            }
            originalRatesList.addAll(filterRatesList);
        }
        return originalRatesList;
    }
    
    private StringBuilder buildSelectQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append(" r.chgcod AS chargeCode,");
        sb.append(" c.chgdsc AS chargeDesc,");
        sb.append(" (");
        sb.append("  r.flatrt+r.blpct+r.cf$+r.cw$+r.min+r.inrate+r.insamt+r.cbmrt+r.kgsrt");
        sb.append(" ) AS totAmount,");
        sb.append(" r.min AS minCharge,");
        sb.append(" r.typert AS chargeType,");
        sb.append(" r.blpct AS blpct,");
        sb.append(" r.cw$ AS lbs,");
        sb.append(" r.inrate AS inrate,");
        sb.append(" r.insamt AS insamt,");
        sb.append(" r.cbmrt AS cbmrt,");
        sb.append(" r.kgsrt AS kgsrt,");
        sb.append(" r.cf$ AS cft,");
        sb.append(" r.flatrt AS flatrt,");
        sb.append(" r.max AS maximumCharge,");
        sb.append(" r.imppc AS billingType,");
        sb.append(" '' as actCode ");
        return sb;
    }
    
    private List<LclImportsRatesBean> getQueryObjectList(SQLQuery queryObject) {
        queryObject.setResultTransformer(Transformers.aliasToBean(LclImportsRatesBean.class));
        queryObject.addScalar("chargeCode", StringType.INSTANCE);
        queryObject.addScalar("chargeDesc", StringType.INSTANCE);
        queryObject.addScalar("totAmount", BigDecimalType.INSTANCE);
        queryObject.addScalar("minCharge", BigDecimalType.INSTANCE);
        queryObject.addScalar("chargeType", StringType.INSTANCE);
        queryObject.addScalar("blpct", BigDecimalType.INSTANCE);
        queryObject.addScalar("lbs", BigDecimalType.INSTANCE);
        queryObject.addScalar("inrate", BigDecimalType.INSTANCE);
        queryObject.addScalar("insamt", BigDecimalType.INSTANCE);
        queryObject.addScalar("cbmrt", BigDecimalType.INSTANCE);
        queryObject.addScalar("kgsrt", BigDecimalType.INSTANCE);
        queryObject.addScalar("cft", BigDecimalType.INSTANCE);
        queryObject.addScalar("flatrt", BigDecimalType.INSTANCE);
        queryObject.addScalar("maximumCharge", BigDecimalType.INSTANCE);
        queryObject.addScalar("billingType", StringType.INSTANCE);
        queryObject.addScalar("actCode", StringType.INSTANCE);
        return queryObject.list();
    }
    
    public List<LclImportsRatesBean> getLCLImportHazCharges(String polNo, String podNo, List commodityList,
            List chargeCodeList, List<String> billingTermsList) throws Exception {
        String commodities = commodityList.toString().replace("[", "").replace("]", "");
        StringBuilder sb = new StringBuilder();
        sb.append(appendQuery());
        sb.append(" WHERE r.imppc IN(:billingterms)");
        sb.append(" AND r.orgnum=:polNum");
        sb.append(" AND r.dstnum=:podNum");
        if (null != chargeCodeList && !chargeCodeList.isEmpty()) {
            sb.append(" AND r.chgcod IN (:chargeCode)");
        }
        if (CommonUtils.isNotEmpty(commodityList)) {
            sb.append("  AND r.comnum IN (").append(commodities).append(")");
        }
        sb.append(" AND r.typert <> '6'");
        sb.append(" HAVING totAmount <> 0.000 ");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameterList("billingterms", billingTermsList);
        queryObject.setParameter("polNum", polNo);
        queryObject.setParameter("podNum", podNo);
        if (null != chargeCodeList && !chargeCodeList.isEmpty()) {
            queryObject.setParameterList("chargeCode", chargeCodeList);
        }
        return getQueryObjectList(queryObject);
    }

    public String getRetailCommodity(String acctNo) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append("  (SELECT ");
        sb.append("    c.`code` ");
        sb.append("  FROM");
        sb.append("    `genericcode_dup` c ");
        sb.append("  WHERE c.`id` = g.`retail_commodity` ");
        sb.append("    AND c.`code` <> '000000') AS commodityNo");
        sb.append(" FROM");
        sb.append("  `cust_general_info` g ");
        sb.append(" WHERE g.`acct_no` = :acctNo");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("acctNo", acctNo);
        queryObject.addScalar("commodityNo", StringType.INSTANCE);
        return (String) queryObject.uniqueResult();
    }
    
    public List<LclImportsRatesBean> getLCLImportOceanFreightRate(String polNo, String podNo, List commodityList, List<String> billingTermsList, String unitSsId) throws Exception {
        String commNo = commodityList.toString().replace("[", "").replace("]", "");
        String sailingDate = this.getSailingDate(unitSsId);
        StringBuilder sb = new StringBuilder();
        sb.append(buildSelectQuery());
        sb.append("FROM ").append(databaseSchema).append(".ratehs r ");
        sb.append("LEFT JOIN ").append(databaseSchema).append(".chgs c ON r.chgcod=c.chgcde ");
        sb.append(" WHERE r.imppc IN(:billingterms)");
        sb.append(" AND r.orgnum=:polNum");
        sb.append(" AND r.dstnum=:podNum");
        if (CommonUtils.isNotEmpty(commodityList)) {
            sb.append("  AND r.comnum IN (").append(commNo).append(")");
        }
        sb.append(" AND r.typert <> '6'");
        sb.append(" AND r.effdte > :sailingDate");
        sb.append(" HAVING totAmount <> 0.000 ");
        sb.append(" ORDER BY r.effdte DESC LIMIT 1");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameterList("billingterms", billingTermsList);
        queryObject.setParameter("polNum", polNo);
        queryObject.setParameter("podNum", podNo);
        queryObject.setParameter("sailingDate", sailingDate);
        return getQueryObjectList(queryObject);
    }
    
    private String getSailingDate(String unitSsId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  DATE_FORMAT(lsd.std, '%Y-%m-%d') as sailingDate ");
        sb.append(" FROM");
        sb.append("  lcl_ss_detail lsd ");
        sb.append("  JOIN lcl_unit_ss lus ");
        sb.append("    ON (");
        sb.append("      lsd.ss_header_id = lus.ss_header_id");
        sb.append("    ) ");
        sb.append(" WHERE lus.id =:unitSsId ");
        sb.append(" LIMIT 1 ");
        SQLQuery queryObj = getCurrentSession().createSQLQuery(sb.toString());
        queryObj.setParameter("unitSsId", unitSsId);
        queryObj.addScalar("sailingDate", StringType.INSTANCE);
        return (String) queryObj.uniqueResult();
    }
}
 
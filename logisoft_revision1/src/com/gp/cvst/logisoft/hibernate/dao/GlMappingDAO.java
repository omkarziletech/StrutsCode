package com.gp.cvst.logisoft.hibernate.dao;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.bc.accounting.GLMappingConstant;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.lcl.bc.LclBookingUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.AccountDetails;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.logiware.hibernate.dao.TerminalGlMappingDAO;
import com.logiware.hibernate.domain.TerminalGlMapping;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.util.LabelValueBean;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

/**
 * Data access object (DAO) for domain model class GlMapping.
 *
 * @see com.gp.cvst.logisoft.hibernate.dao.GlMapping
 * @author MyEclipse - Hibernate Tools
 */
public class GlMappingDAO extends BaseHibernateDAO {

    public void save(GlMapping transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void saveOrUpdate(GlMapping transientInstance) throws Exception {
        getSession().saveOrUpdate(transientInstance);
        getSession().flush();

    }

    public void update(GlMapping persistentInstance) throws Exception {
        getSession().update(persistentInstance);
        getSession().flush();
    }

    public void delete(GlMapping persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
        getSession().flush();
    }

    public GlMapping findById(java.lang.Integer id) throws Exception {
        GlMapping instance = (GlMapping) getSession().get("com.gp.cvst.logisoft.domain.GlMapping", id);
        return instance;
    }

    public List findByExample(GlMapping instance) throws Exception {
        List results = getSession().createCriteria("com.gp.cvst.logisoft.hibernate.dao.GlMapping").add(Example.create(instance)).list();
        return results;
    }

    @SuppressWarnings("unchecked")
    public List<GlMapping> findByProperty(String propertyName, Object value) throws Exception {
        String queryString = "from GlMapping as model where model." + propertyName + " like ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value + "%");
        return queryObject.list();
    }

    @SuppressWarnings("unchecked")
    public GlMapping getByProperty(String propertyName, Object value) throws Exception {
        String queryString = "from GlMapping as model where model." + propertyName + " like ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value + "%");
        return (GlMapping) queryObject.uniqueResult();
    }

    public List<GlMapping> getChargeCodeForAutocomplete(String chargeCode) throws Exception {
        List<GlMapping> glMappings = new ArrayList<GlMapping>();
        StringBuilder queryBuilder = new StringBuilder("select charge_code,cast(group_concat(distinct if(charge_description!='',charge_description,null)) as char)");
        queryBuilder.append(" from gl_mapping where charge_code like '%").append(chargeCode).append("%' group by charge_code");
        List result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            GlMapping glMapping = new GlMapping();
            glMapping.setChargeCode((String) col[0]);
            glMapping.setChargeDescriptions(null != col[1] ? col[1].toString() : "");
            glMappings.add(glMapping);
        }
        return glMappings;
    }

    public GlMapping merge(GlMapping detachedInstance) throws Exception {
        GlMapping result = (GlMapping) getSession().merge(detachedInstance);
        return result;
    }

    public void attachDirty(GlMapping instance) throws Exception {
        getSession().saveOrUpdate(instance);
        getSession().flush();
    }

    public void attachClean(GlMapping instance) throws Exception {
        getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    /**
     * Derive Gl Account based on Charge Code,Shipment Type and Terminal
     *
     * @param chargeCode
     * @param shipmentType
     * @param orginTerminal
     * @param shipmentType
     * @return glAccount
     */
    public String dervieGlAccount(String chargeCode, String shipmentType, String orginTerminal, String revExp) throws Exception {
        StringBuilder glAccount = new StringBuilder();
        Criteria criteria = getCurrentSession().createCriteria(GlMapping.class);
        criteria.add(Restrictions.eq("chargeCode", chargeCode));
        criteria.add(Restrictions.eq("shipmentType", shipmentType));
        criteria.add(Restrictions.eq("revExp", revExp));
        List<GlMapping> result = criteria.list();
        for (GlMapping glMapping : result) {
            String companyCode = new SystemRulesDAO().getSystemRulesByCode(CommonConstants.SYSTEM_RULE_CODE_COMPANY_CODE);
            glAccount = new StringBuilder(companyCode).append("-").append(glMapping.getGlAcct()).append("-");
            String suffixValue = null;
            if (CommonUtils.isEqualIgnoreCase(glMapping.getDeriveYn(), GLMappingConstant.FIXED)
                    || CommonUtils.isEqualIgnoreCase(glMapping.getDeriveYn(), CommonConstants.NO)) {
                suffixValue = glMapping.getSuffixValue();
            } else {
                TerminalGlMapping terminalGlMapping = null;
                if (CommonUtils.isNotEmpty(orginTerminal)) {
                    terminalGlMapping = new TerminalGlMappingDAO().findById(Integer.parseInt(orginTerminal.trim()));
                }
                if (null != terminalGlMapping) {
                    Integer suffixTerminal = null;
                    if (CommonUtils.isEqualIgnoreCase(shipmentType, GLMappingConstant.LCLE)) {
                        if (CommonUtils.isEqualIgnoreCase(glMapping.getSuffixValue(), GLMappingConstant.BILLING)) {
                            suffixTerminal = terminalGlMapping.getLclExportBilling();
                        } else if (CommonUtils.isEqualIgnoreCase(glMapping.getSuffixValue(), GLMappingConstant.LOADING)) {
                            suffixTerminal = terminalGlMapping.getLclExportLoading();
                        } else if (CommonUtils.isEqualIgnoreCase(glMapping.getSuffixValue(), GLMappingConstant.DOCK_RECEIPT)) {
                            suffixTerminal = terminalGlMapping.getLclExportDockreceipt();
                        }
                    } else if (CommonUtils.isEqualIgnoreCase(shipmentType, GLMappingConstant.FCLE)) {
                        if (CommonUtils.isEqualIgnoreCase(glMapping.getSuffixValue(), GLMappingConstant.BILLING)) {
                            suffixTerminal = terminalGlMapping.getFclExportBilling();
                        } else if (CommonUtils.isEqualIgnoreCase(glMapping.getSuffixValue(), GLMappingConstant.LOADING)) {
                            suffixTerminal = terminalGlMapping.getFclExportLoading();
                        } else if (CommonUtils.isEqualIgnoreCase(glMapping.getSuffixValue(), GLMappingConstant.DOCK_RECEIPT)) {
                            suffixTerminal = terminalGlMapping.getFclExportDockreceipt();
                        }
                    } else if (CommonUtils.isEqualIgnoreCase(shipmentType, GLMappingConstant.AIRE)) {
                        if (CommonUtils.isEqualIgnoreCase(glMapping.getSuffixValue(), GLMappingConstant.BILLING)) {
                            suffixTerminal = terminalGlMapping.getAirExportBilling();
                        } else if (CommonUtils.isEqualIgnoreCase(glMapping.getSuffixValue(), GLMappingConstant.LOADING)) {
                            suffixTerminal = terminalGlMapping.getAirExportLoading();
                        } else if (CommonUtils.isEqualIgnoreCase(glMapping.getSuffixValue(), GLMappingConstant.DOCK_RECEIPT)) {
                            suffixTerminal = terminalGlMapping.getAirExportDockreceipt();
                        }
                    } else if (CommonUtils.isEqualIgnoreCase(shipmentType, GLMappingConstant.LCLI)) {
                        if (CommonUtils.isEqualIgnoreCase(glMapping.getSuffixValue(), GLMappingConstant.BILLING)) {
                            suffixTerminal = terminalGlMapping.getLclImportBilling();
                        } else if (CommonUtils.isEqualIgnoreCase(glMapping.getSuffixValue(), GLMappingConstant.LOADING)) {
                            suffixTerminal = terminalGlMapping.getLclImportLoading();
                        }
                    } else if (CommonUtils.isEqualIgnoreCase(shipmentType, GLMappingConstant.FCLI)) {
                        if (CommonUtils.isEqualIgnoreCase(glMapping.getSuffixValue(), GLMappingConstant.BILLING)) {
                            suffixTerminal = terminalGlMapping.getFclImportBilling();
                        } else if (CommonUtils.isEqualIgnoreCase(glMapping.getSuffixValue(), GLMappingConstant.LOADING)) {
                            suffixTerminal = terminalGlMapping.getFclImportLoading();
                        }
                    } else if (CommonUtils.isEqualIgnoreCase(shipmentType, GLMappingConstant.AIRI)) {
                        if (CommonUtils.isEqualIgnoreCase(glMapping.getSuffixValue(), GLMappingConstant.BILLING)) {
                            suffixTerminal = terminalGlMapping.getAirImportBilling();
                        } else if (CommonUtils.isEqualIgnoreCase(glMapping.getSuffixValue(), GLMappingConstant.LOADING)) {
                            suffixTerminal = terminalGlMapping.getAirImportLoading();
                        }
                    } else if (CommonUtils.isEqualIgnoreCase(shipmentType, GLMappingConstant.INLE)
                            && CommonUtils.isEqualIgnoreCase(glMapping.getSuffixValue(), GLMappingConstant.LOADING)) {
                        suffixTerminal = terminalGlMapping.getInlandExportLoading();
                    }
                    suffixValue = NumberUtils.formatNumber(null != suffixTerminal ? suffixTerminal : Integer.parseInt(orginTerminal), "##00");
                }
            }
            glAccount.append(suffixValue);
            break;
        }
        AccountDetails accountDetails = new AccountDetailsDAO().findById(glAccount.toString());
        return null != accountDetails ? accountDetails.getAccount() : null;
    }

    /**
     * Deriving GLAccountNo by GL mapping
     *
     * @author Lakshmi Narayanan V
     * @param shipmentType
     * @param chargecode
     * @param originTerminal
     * @param billingTerminal
     * @param rev
     * @return GL AccountNo
     */
    public String getGLAccountNo(String shipmentType, String chargecode, String originTerminal, String billingTerminal, String rev) throws Exception {
        String result = null;
        String queryString = "Select gl.glAcct,gl.transactionType,gl.suffixValue,gl.deriveYn"
                + " from GlMapping gl where gl.shipmentType ='" + shipmentType + "' and gl.chargeCode='"
                + chargecode + "' and gl.revExp='" + rev + "'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        SystemRulesDAO srDAO = new SystemRulesDAO();
        String ruleCode = CommonConstants.SYSTEM_RULE_CODE_COMPANY_CODE;
        String Company_Code = srDAO.getSystemRulesByCode(ruleCode);
        for (Object object : queryObject) {
            Object[] row = (Object[]) object;
            String glAcctNo = (String) row[0];
            //String transType=(String)row[1];
            String suffixValue = (String) row[2];
            String deriveYN = (String) row[3];
            //result is Nothing but GLAccountNumber Derived
            if (deriveYN.equalsIgnoreCase("N")) {
                result = glAcctNo;
            } else if (deriveYN.equalsIgnoreCase("F")) {
                //Customer_code needs to be prefixed
                result = Company_Code + "-" + glAcctNo + "-" + suffixValue;
            } else if (deriveYN.equalsIgnoreCase("L") && null != originTerminal) {
                //Customer_code needs to be prefixed
                result = Company_Code + "-" + glAcctNo + "-" + originTerminal;
            } else if (deriveYN.equalsIgnoreCase("B") && null != billingTerminal) {
                //Customer_code needs to be prefixed
                result = Company_Code + "-" + glAcctNo + "-" + billingTerminal;
            } else {
                result = glAcctNo;
            }
        }
        return result;
    }

    public String getGLAccountNoWithTransactionType(String shipmentType, String chargecode, String originTerminal, String billingTerminal, String rev, String transactionType) throws Exception {
        String result = "";
        String queryString = "Select gl.glAcct,gl.transactionType,gl.suffixValue,gl.deriveYn"
                + " from GlMapping gl where gl.shipmentType ='" + shipmentType + "' and gl.chargeCode='"
                + chargecode + "' and gl.revExp='" + rev + "' and gl.transactionType='" + transactionType + "'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        SystemRulesDAO srDAO = new SystemRulesDAO();
        String ruleCode = CommonConstants.SYSTEM_RULE_CODE_COMPANY_CODE;
        String Company_Code = srDAO.getSystemRulesByCode(ruleCode);
        for (Object object : queryObject) {
            Object[] row = (Object[]) object;
            String glAcctNo = (String) row[0];
            //String transType=(String)row[1];
            String suffixValue = (String) row[2];
            String deriveYN = (String) row[3];
            //result is Nothing but GLAccountNumber Derived
            if (deriveYN.equalsIgnoreCase("N")) {
                result = glAcctNo;
            } else if (deriveYN.equalsIgnoreCase("F")) {
                //Customer_code needs to be prefixed
                result = Company_Code + "-" + glAcctNo + "-" + suffixValue;
            } else if (deriveYN.equalsIgnoreCase("L") && null != originTerminal) {
                //Customer_code needs to be prefixed
                result = Company_Code + "-" + glAcctNo + "-" + originTerminal;
            } else if (deriveYN.equalsIgnoreCase("B") && null != billingTerminal) {
                //Customer_code needs to be prefixed
                result = Company_Code + "-" + glAcctNo + "-" + billingTerminal;
            } else {
                result = glAcctNo;
            }
            break;
        }
        return result;
    }

    public String getGLAccountNoWithPrefixAndSuffix(String shipmentType, String chargecode, String rev, String suffixValue) throws Exception {
        String result = null;
        String queryString = "Select gl.glAcct,gl.transactionType,gl.deriveYn"
                + " from GlMapping gl where gl.shipmentType ='" + shipmentType + "' and gl.chargeCode='"
                + chargecode + "' and gl.revExp='" + rev + "'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        SystemRulesDAO srDAO = new SystemRulesDAO();
        String ruleCode = CommonConstants.SYSTEM_RULE_CODE_COMPANY_CODE;
        String Company_Code = srDAO.getSystemRulesByCode(ruleCode);
        for (Object object : queryObject) {
            Object[] row = (Object[]) object;
            String glAcctNo = (String) row[0];
            result = Company_Code + "-" + glAcctNo + "-" + suffixValue;
        }
        return result;
    }

    public String getGLAccountNo1(String shipmentType, String chargecode, String originTerminal, String billingTerminal, String rev) {
        String result = null;
        /*SELECT  Gl_Account, Transaction_type, Subledger_Code, Suffix_Value, Derive_YN
         From GL_MAPPING
         WHERE Shipment_type = <Shipment_type> (got in STEP 1)
         AND Charge_code = <Charge_code> (got in STEP 2)
         AND Rev_Exp = �R�;*/

        String queryString = "Select gl.subLedgerCode,gl.glAcct from GlMapping gl"
                + " where gl.shipmentType ='" + shipmentType + "' and gl.chargeCode='" + chargecode + "'"
                + " and gl.revExp='" + rev + "'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        Iterator itr = queryObject.iterator();

        while (itr.hasNext()) {
            Object[] row = (Object[]) itr.next();

            if (null != row[0]) {
                result = (String) row[0];
            }

        }

        return result;
    }

    public String getSubLedgerSourceCode(String shipmentType, String chargecode, String transactionType, String rev) {
        String result = "";
        String queryString = "Select gl.subLedgerCode,gl.glAcct from GlMapping gl"
                + " where gl.shipmentType ='" + shipmentType + "' and gl.chargeCode='" + chargecode + "'"
                + " and gl.revExp='" + rev + "' and gl.transactionType='" + transactionType + "'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        Iterator itr = queryObject.iterator();
        while (itr.hasNext()) {
            Object[] row = (Object[]) itr.next();
            if (null != row && null != row[0]) {
                result = (String) row[0];
                break;
            }
        }

        return result;
    }

    public String getBlueScreenChargeCode(String shipmentType, String chargecode, String transactionType, String rev) {
        String result = "";
        String queryString = "Select gl.blueScreenChargeCode from GlMapping gl"
                + " where gl.shipmentType ='" + shipmentType + "' and gl.chargeCode='" + chargecode + "'"
                + " and gl.revExp='" + rev + "' and gl.transactionType='" + transactionType + "' and gl.blueScreenChargeCode is not null";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        Iterator itr = queryObject.iterator();
        while (itr.hasNext()) {
            result = (String) itr.next();
            break;
        }
        return result;
    }

    public Object[] getBlueScreenChargeCodeRevExp(String shipmentType, String chargeCode, String transactionType) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select bluescreen_chargecode,Rev_Exp FROM gl_mapping WHERE Shipment_Type= '").append(shipmentType).append("' and Transaction_Type='").append(transactionType).append("' and Charge_code='").append(chargeCode).append("'");
        Query queryObject = getSession().createSQLQuery(queryBuilder.toString());

        return (Object[]) queryObject.uniqueResult();
    }

    public String getGLAccountNo2(String shipmentType, String chargecode, String originTerminal,
            String billingTerminal, String rev) {
        String result = null;

        String queryString = "Select gl.transactionType,gl.shipmentType from GlMapping gl "
                + "where gl.shipmentType ='" + shipmentType + "' and gl.chargeCode='" + chargecode + "' "
                + "and gl.revExp='" + rev + "'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        Iterator itr = queryObject.iterator();

        while (itr.hasNext()) {
            Object[] row = (Object[]) itr.next();
            result = (String) row[0];
        }

        return result;
    }

    /**
     * getting gl Mappings
     *
     * @param startendchargeCode
     * @return getGlMappings
     * @author Lakshmi Narayanan.V
     */
    public List<GlMapping> getGlMappings(String chargeCode, String startAccount, String endAccount) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(GlMapping.class);
        if (CommonUtils.isNotEmpty(chargeCode)) {
            criteria.add(Restrictions.eq("chargeCode", chargeCode));
        } else {
            criteria.add(Restrictions.isNotNull("chargeCode"));
        }
        if (CommonUtils.isNotEmpty(startAccount) && CommonUtils.isNotEmpty(endAccount)) {
            criteria.add(Restrictions.between("glAcct", startAccount, endAccount));
        }
        criteria.addOrder(Order.asc("chargeCode"));
        return criteria.list();
    }

    public List<GlMapping> findGLAccountNo(String glAcct) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(GlMapping.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.like("glAcct", "%" + glAcct + "%"));
        criteria.setMaxResults(50);
        return criteria.list();
    }

    public void findAndSaveOrUpdate(GlMapping glMapping, String userName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder("from GlMapping");
        queryBuilder.append(" where chargeCode = '").append(glMapping.getChargeCode()).append("'");
        if (CommonUtils.isNotEmpty(glMapping.getBlueScreenChargeCode())) {
            queryBuilder.append(" and (blueScreenChargeCode = '").append(glMapping.getBlueScreenChargeCode()).append("'");
            queryBuilder.append(" or blueScreenChargeCode = '' or blueScreenChargeCode is null)");
        } else {
            queryBuilder.append(" and (blueScreenChargeCode = '' or blueScreenChargeCode is null)");
        }
        if (CommonUtils.isNotEmpty(glMapping.getShipmentType())) {
            queryBuilder.append(" and shipmentType = '").append(glMapping.getShipmentType()).append("'");
        } else {
            queryBuilder.append(" and (shipmentType = '' or shipmentType is null)");
        }
        if (CommonUtils.isNotEmpty(glMapping.getTransactionType())) {
            queryBuilder.append(" and transactionType = '").append(glMapping.getTransactionType()).append("'");
        } else {
            queryBuilder.append(" and (transactionType = '' or transactionType is null)");
        }
        List<GlMapping> glMappings = getCurrentSession().createQuery(queryBuilder.toString()).list();
        if (CommonUtils.isNotEmpty(glMappings)) {
            for (GlMapping persistentGlMapping : glMappings) {
                glMapping.setId(persistentGlMapping.getId());
                PropertyUtils.copyProperties(persistentGlMapping, glMapping);
                persistentGlMapping.setUpdatedDate(new Date());
                persistentGlMapping.setUpdatedBy(userName);
            }
        } else {
            glMapping.setCreatedDate(new Date());
            glMapping.setCreatedBy(userName);
            save(glMapping);
        }
    }

    @SuppressWarnings("unchecked")
    public List<LabelValueBean> getChargeCodeByRevOrExp(String revExp) throws Exception {
        List<LabelValueBean> chargeCodeList = new ArrayList<LabelValueBean>();
        String queryString = "select distinct chargeCode from GlMapping as glMapping where glMapping.revExp like '" + revExp + "%'"
                + "and (glMapping.transactionType='" + CommonConstants.TRANSACTION_TYPE_ACCRUALS + "' or glMapping.transactionType='" + CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE + "')";
        Query queryObject = getSession().createQuery(queryString);
        List<String> resultList = queryObject.list();
        for (String chargeCode : resultList) {
            chargeCodeList.add(new LabelValueBean(chargeCode, chargeCode));
        }
        return chargeCodeList;
    }

    public List<LabelValueBean> getChargeCodeByShipmentType(String shipmentType, String transactionType) throws Exception {
        List<LabelValueBean> chargeCodeList = new ArrayList<LabelValueBean>();
        StringBuilder queryString = new StringBuilder();
        queryString.append("SELECT DISTINCT gl.charge_code,TRIM(IF(ge.codedesc <> '',ge.codedesc,gl.charge_code)) FROM gl_mapping gl JOIN genericcode_dup ge ON(gl.charge_code = ge.code)");
        queryString.append("  AND ge.codetypeid = (SELECT codetypeid FROM codetype WHERE description = 'Cost Code' LIMIT 1)");
        queryString.append("  WHERE gl.charge_code <> '' AND gl.Shipment_Type=:shipmentType");
        queryString.append("  AND gl.Transaction_Type = :transactionType");
        queryString.append("  GROUP BY gl.charge_code ORDER BY gl.charge_code");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(queryString.toString());
        queryObject.setString("shipmentType", shipmentType);
        queryObject.setString("transactionType", transactionType);
        List resultList = queryObject.list();
        for (Object chargeCode : resultList) {
            Object[] col = (Object[]) chargeCode;
            chargeCodeList.add(new LabelValueBean(col[1].toString(), col[0].toString()));
        }
        return chargeCodeList;
    }

    public List<LabelValueBean> getChargeCodeArInvoice(String shipmentType, String transactionType) throws Exception {
        List<LabelValueBean> chargeCodeList = new ArrayList<LabelValueBean>();
        StringBuffer queryString = new StringBuffer();
        queryString.append("SELECT g.code, UCASE(g.codedesc) FROM genericcode_dup g, gl_mapping gl");
        queryString.append(" WHERE g.codetypeid = (SELECT codetypeid FROM codetype WHERE description = 'Cost Code' LIMIT 1)");
        queryString.append(" AND g.code=gl.charge_code AND gl.Shipment_Type='" + shipmentType + "' AND gl.Transaction_Type='" + transactionType + "'");
        queryString.append(" AND gl.bluescreen_chargecode is not null AND gl.bluescreen_chargecode != '' ORDER BY remove_special_characters(g.codedesc)");
        List<Object[]> resultList = getCurrentSession().createSQLQuery(queryString.toString()).list();
        for (Object[] genericCode : resultList) {
            if (null != genericCode[1] && null != genericCode[0]) {
                chargeCodeList.add(new LabelValueBean(genericCode[1].toString(), genericCode[0].toString()));
            }
        }
        return chargeCodeList;
    }

    public List<GlMapping> getGLMappingForAccruals(String value) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(GlMapping.class);
        Disjunction disjunction = Restrictions.disjunction();
        disjunction.add(Restrictions.like("blueScreenChargeCode", value));
        disjunction.add(Restrictions.like("chargeCode", value));
        disjunction.add(Restrictions.like("chargeDescriptions", value));
        criteria.add(disjunction);
        criteria.add(Restrictions.eq("transactionType", CommonConstants.TRANSACTION_TYPE_ACCRUALS));
        criteria.add(Restrictions.eq("revExp", "E"));
        criteria.add(Restrictions.isNotNull("chargeCode"));
        criteria.add(Restrictions.isNotNull("shipmentType"));
        criteria.add(Restrictions.isNotNull("deriveYn"));
        return criteria.list();
    }

    public boolean isValidBlueScreenChargeCode(String bluescreenChargeCode, String chargeCode, String transactionType, String shipmentType) throws Exception {
        StringBuilder queryBuilder = new StringBuilder("select count(*) from gl_mapping");
        queryBuilder.append(" where bluescreen_chargecode='").append(bluescreenChargeCode).append("'");
        queryBuilder.append(" and charge_code!='").append(chargeCode).append("'");
        queryBuilder.append(" and transaction_type='").append(transactionType).append("'");
        queryBuilder.append(" and shipment_type='").append(shipmentType).append("'");
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return Integer.parseInt(result.toString()) > 0 ? false : true;
    }

    public boolean isValidChargeCode(String bluescreenChargeCode, String chargeCode, String transactionType, String shipmentType) {
        StringBuilder queryBuilder = new StringBuilder("select count(*) from gl_mapping");
        queryBuilder.append(" where bluescreen_chargecode='").append(bluescreenChargeCode).append("'");
        queryBuilder.append(" and charge_code='").append(chargeCode).append("'");
        queryBuilder.append(" and transaction_type='").append(transactionType).append("'");
        queryBuilder.append(" and shipment_type='").append(shipmentType).append("'");
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return Integer.parseInt(result.toString()) > 0 ? false : true;
    }

    public boolean isChargeCodeFound(String chargeCode, String transactionType, String shipmentType) {
        StringBuilder queryBuilder = new StringBuilder("select count(*) from gl_mapping");
        queryBuilder.append(" where charge_code='").append(chargeCode).append("'");
        queryBuilder.append(" and transaction_type='").append(transactionType).append("'");
        queryBuilder.append(" and shipment_type='").append(shipmentType).append("'");
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return Integer.parseInt(result.toString()) > 0;
    }

    public boolean isChargeCodeFound(String chargeCode) {
        StringBuilder queryBuilder = new StringBuilder("select count(*) from gl_mapping");
        queryBuilder.append(" where charge_code='").append(chargeCode).append("'");
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return Integer.parseInt(result.toString()) > 0;
    }

    public boolean isValidBlueScreenFeedBack(String bluescreenChargeCode, String chargeCode, String transactionType, String shipmentType,
            String revExp, boolean bluescreenFeedback) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select count(*) from gl_mapping");
        queryBuilder.append(" where bluescreen_chargecode!='").append(bluescreenChargeCode).append("'");
        queryBuilder.append(" and charge_code='").append(chargeCode).append("'");
        queryBuilder.append(" and transaction_type='").append(transactionType).append("'");
        queryBuilder.append(" and shipment_type='").append(shipmentType).append("'");
        queryBuilder.append(" and rev_exp='").append(revExp).append("'");
        queryBuilder.append(" and bluescreen_feedback=").append(bluescreenFeedback).append("");
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return Integer.parseInt(result.toString()) <= 0;
    }

    /**
     * getting gl Mapping
     *
     * @param chashipmtransactionType
     * @return getGlMappings
     * @author Saravanan.R
     */
    public GlMapping findByBlueScreenChargeCode(String blueScreenChargeCode, String shipmentType, String transactionType) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(GlMapping.class);
        if (CommonUtils.isNotEmpty(blueScreenChargeCode)) {
            criteria.add(Restrictions.eq("blueScreenChargeCode", blueScreenChargeCode));
        } else {
            criteria.add(Restrictions.isNotNull("blueScreenChargeCode"));
        }

        if (CommonUtils.isNotEmpty(shipmentType)) {
            criteria.add(Restrictions.eq("shipmentType", shipmentType));
        }
        if (CommonUtils.isNotEmpty(transactionType)) {
            criteria.add(Restrictions.eq("transactionType", transactionType));
        }
        List<GlMapping> l = (List<GlMapping>) criteria.list();
        for (GlMapping glMapping : l) {
            if (glMapping.isBluescreenFeedback()) {
                return glMapping;
            }
        }
        if (null != l && !l.isEmpty()) {
            return l.get(0);
        }
        return null;
    }

    public String getStringValueById(String field1, Integer id) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append(field1);
        queryBuilder.append(" from gl_mapping where id = ");
        queryBuilder.append(id);
        return (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public GlMapping findByChargeCode(String chargeCode, String shipmentType, String transactionType) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(GlMapping.class);
        if (CommonUtils.isNotEmpty(chargeCode)) {
            criteria.add(Restrictions.eq("chargeCode", chargeCode));
        } else {
            criteria.add(Restrictions.isNotNull("chargeCode"));
        }
        if (CommonUtils.isNotEmpty(shipmentType)) {
            criteria.add(Restrictions.eq("shipmentType", shipmentType));
        }
        if (CommonUtils.isNotEmpty(transactionType)) {
            criteria.add(Restrictions.eq("transactionType", transactionType));
        }
        List<GlMapping> l = (List<GlMapping>) criteria.list();
        for (GlMapping glMapping : l) {
            if (glMapping.isBluescreenFeedback()) {
                return glMapping;
            }
        }
        if (null != l && !l.isEmpty()) {
            return l.get(0);
        }
        return null;
    }

    public GlMapping findByGlMappingId(String chargeCode, Integer chargeCodeId, String shipmentType, String transactionType) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(GlMapping.class);
        if (null != chargeCodeId) {
            criteria.add(Restrictions.eq("id", chargeCodeId));
        }
        if (CommonUtils.isNotEmpty(chargeCode)) {
            criteria.add(Restrictions.eq("chargeCode", chargeCode));
        } else {
            criteria.add(Restrictions.isNotNull("chargeCode"));
        }
        if (CommonUtils.isNotEmpty(shipmentType)) {
            criteria.add(Restrictions.eq("shipmentType", shipmentType));
        }
        if (CommonUtils.isNotEmpty(transactionType)) {
            criteria.add(Restrictions.eq("transactionType", transactionType));
        }
        return (GlMapping) criteria.setMaxResults(1).uniqueResult();
    }

    public List<LabelValueBean> getLclChargeCodeArInvoice() throws Exception {
        String shipmentType = "LCLE";
        String transactionType = "AR";
        List<LabelValueBean> chargeCodeList = new ArrayList<LabelValueBean>();
        try {
            StringBuilder queryString = new StringBuilder();
            queryString.append("SELECT g.code, UCASE(g.codedesc) FROM genericcode_dup g, gl_mapping gl");
            queryString.append(" WHERE g.codetypeid = (SELECT codetypeid FROM codetype WHERE description = 'Cost Code' LIMIT 1)");
            queryString.append(" AND g.code=gl.charge_code AND gl.Shipment_Type='").append(shipmentType).append("' AND gl.Transaction_Type='").append(transactionType).append("'");
            queryString.append(" AND gl.bluescreen_chargecode is not null AND gl.bluescreen_chargecode != ''");
            List<Object[]> resultList = getCurrentSession().createSQLQuery(queryString.toString()).list();
            for (Object[] genericCode : resultList) {
                if (null != genericCode[1] && null != genericCode[0]) {
                    chargeCodeList.add(new LabelValueBean(genericCode[1].toString(), genericCode[0].toString()));
                }
            }
            return chargeCodeList;
        } catch (RuntimeException re) {
            throw re;
        } finally {
            closeSession();
        }
    }

    public String getGlMappingId(String chargeCode, String revExp, String transactionType, String shipmentType) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  id");
        queryBuilder.append("  ");
        queryBuilder.append("from");
        queryBuilder.append("  gl_mapping");
        queryBuilder.append("  ");
        queryBuilder.append("where charge_code = '").append(chargeCode).append("'");
        queryBuilder.append("  and rev_exp = '").append(revExp).append("'");
        queryBuilder.append("  and transaction_type = '").append(transactionType).append("'");
        queryBuilder.append("  and shipment_type = '").append(shipmentType).append("'");
        queryBuilder.append("  ");
        queryBuilder.append("order by bluescreen_chargecode");
        queryBuilder.append("  ");
        queryBuilder.append("limit 1");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("id", StringType.INSTANCE);
        return (String) query.uniqueResult();
    }

    public String getglMappingIdUsingBlueScreenCode(String blueScreenChargeCode, String shipmentType, String transactionType) throws Exception {
        String glMappingId = "";
        StringBuilder sb = new StringBuilder();
        sb.append("select CONVERT(GROUP_CONCAT(id)USING utf8) from gl_mapping where bluescreen_chargecode IN(").append(blueScreenChargeCode).append(")");
        sb.append(" and shipment_type='").append(shipmentType).append("'").append(" and transaction_type='").append(transactionType).append("'");
        Query queryObject = getSession().createSQLQuery(sb.toString());
        Object o = queryObject.uniqueResult();
        if (o != null) {
            glMappingId = o.toString();
        }
        return glMappingId;
    }

    public List getchargeCodeListForDestinationServices() {
        List<LabelValueBean> chargeCodeList = new ArrayList<LabelValueBean>();
        StringBuilder queryString = new StringBuilder();
        queryString.append("SELECT charge_code,charge_description, "
                + " (CASE WHEN charge_code ='ONCARR' THEN 2 WHEN charge_code ='DTHC PREPAID' THEN 1 WHEN charge_code ='DELIV' THEN 3 WHEN charge_code ='DAP' THEN 4 ELSE 5 END) AS sort" // the case statement is to order the charge list whether given by document
                + " FROM gl_mapping WHERE destination_services=1 AND subLedger_code='AR-LCLE' order by sort ASC");
        List<Object[]> resultList = getCurrentSession().createSQLQuery(queryString.toString()).list();
        for (Object[] row : resultList) {
            String code = (String) row[0];
            String name = (String) row[1];
            name = CommonUtils.isNotEmpty(name) ? name : code;
            if (CommonUtils.isNotEmpty(code)) {
                chargeCodeList.add(new LabelValueBean(name, code));
            }
        }
        return chargeCodeList;
    }

    public boolean isBlLevelCost(String chargeCode) {
        StringBuilder queryString = new StringBuilder();
        queryString.append("select count(*) from gl_mapping where Charge_Code='").append(chargeCode);
        queryString.append("'and Shipment_Type='FCLE' and transaction_type = 'AC' and bl_level_cost = 1");
        String isBlCost = getCurrentSession().createSQLQuery(queryString.toString()).uniqueResult().toString();
        return !isBlCost.equals("0");
    }

    public boolean isValidChargeCodeByBlue(String bluescreenChargeCode, String transactionType, String shipmentType) {
        StringBuilder queryBuilder = new StringBuilder("select count(*) from gl_mapping");
        queryBuilder.append(" where bluescreen_chargecode='").append(bluescreenChargeCode).append("'");
        queryBuilder.append(" and transaction_type='").append(transactionType).append("'");
        queryBuilder.append(" and shipment_type='").append(shipmentType).append("'");
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return Integer.parseInt(result.toString()) > 0 ? true : false;
    }

    public List checkCommodityInOfrate(String commodityNo) throws Exception {
        List commodity = Arrays.asList(commodityNo.split(","));
        String eliteDbName = LoadLogisoftProperties.getProperty("elite.database.name");
        SQLQuery query = getCurrentSession().createSQLQuery(" SELECT DISTINCT comcde FROM " + eliteDbName + ".ofrate  WHERE comcde IN (:commodityNo)");
        query.setParameterList("commodityNo", commodity);
        return query.list();
    }

    public String blueChgCodeForBarrel(String chargeCode, String transactionType, String shipmentType) throws Exception {
        String chgCode = "";
        StringBuilder sb = new StringBuilder();
        sb.append("select bluescreen_chargecode from gl_mapping where Charge_code ='").append(chargeCode).append("'");
        sb.append(" and shipment_type='").append(shipmentType).append("'").append(" and transaction_type='").append(transactionType).append("'");
        Query queryObject = getSession().createSQLQuery(sb.toString());
        Object o = queryObject.setMaxResults(1).uniqueResult();
        if (o != null) {
            chgCode = o.toString();
        }
        return chgCode;
    }

    public Integer getGLMappingId(String chargeCode, String shipment_Type, String transaction_Type) {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT id FROM gl_mapping WHERE charge_code='").append(chargeCode).append("'");
        queryStr.append(" AND shipment_type='").append(shipment_Type).append("'");
        queryStr.append(" AND transaction_type='").append(transaction_Type).append("'");
        queryStr.append(" Limit 1");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.addScalar("id", IntegerType.INSTANCE);
        return (Integer) query.uniqueResult();
    }

    public String validateLclExportGlAccount(String chargeCode, String originId, String billingTerminal, String voyOriginId, String tType) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT  CONCAT(CASE ");
        sb.append(" WHEN no_charge_code THEN ");
        sb.append(" CONCAT('No Gl Mapping found for  ChargeCode ---- >','").append(chargeCode).append("')");
        sb.append(" WHEN glAcct.glAccount IS NULL THEN ");
        sb.append(" CONCAT('ChargeCode is not mapped with any gl account in accounting') ");
        sb.append(" WHEN IsValidGlAccount (glAcct.glAccount) = 0 THEN ");
        sb.append(" CONCAT('Derived GL Account <span class=\''red\''> ',glAcct.glAccount,'</span> is not valid one.') ");
        sb.append(" ELSE ''  END ) AS error ");
        sb.append(" FROM (SELECT DeriveLCLExportGlAccount(gl.id,:billingTerminal,:originId,:voyOriginId) AS glAccount, ");
        sb.append(" IF(COUNT(*) <= 0, TRUE, FALSE) AS no_charge_code  ");
        sb.append(" from gl_mapping gl ");
        sb.append(" where gl.charge_code =:chargeCode and gl.Transaction_Type =:tType and gl.shipment_type ='LCLE' ");
        sb.append(" ORDER BY gl.id DESC LIMIT 1) AS glAcct ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("chargeCode", chargeCode);
        query.setParameter("originId", originId);
        query.setParameter("voyOriginId", voyOriginId);
        query.setParameter("tType", tType);
        query.setParameter("billingTerminal", billingTerminal);
        return (String) query.setMaxResults(1).uniqueResult();
    }

    public String getLclExportDerivedGlAccount(String chargeId, String billingTerminal, String originId,String voyOriginId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("select DeriveLCLExportGlAccount(:chargeId,");
        sb.append(":billingTerminal,:originId,:voyOriginId) as glAccount");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("chargeId", chargeId);
        query.setParameter("originId", originId);
        query.setParameter("billingTerminal", billingTerminal);
        query.setParameter("voyOriginId", voyOriginId);
        return (String) query.addScalar("glAccount", StringType.INSTANCE).uniqueResult();
    }

    public GlMapping findByDestinationChargeCode(String chargeCode, String shipmentType, String transactionType) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(GlMapping.class);
        if (CommonUtils.isNotEmpty(chargeCode)) {
            criteria.add(Restrictions.eq("chargeCode", chargeCode));
        } else {
            criteria.add(Restrictions.isNotNull("chargeCode"));
        }
        if (CommonUtils.isNotEmpty(shipmentType)) {
            criteria.add(Restrictions.eq("shipmentType", shipmentType));
        }
        if (CommonUtils.isNotEmpty(transactionType)) {
            criteria.add(Restrictions.eq("transactionType", transactionType));
        }
        criteria.add(Restrictions.eq("destinationServices", true));
        List<GlMapping> l = (List<GlMapping>) criteria.list();
        for (GlMapping glMapping : l) {
            if (glMapping.isBluescreenFeedback()) {
                return glMapping;
            }
        }
        if (null != l && !l.isEmpty()) {
            return l.get(0);
        }
        return null;
    }
}

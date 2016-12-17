package com.gp.cvst.logisoft.hibernate.dao;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cvst.logisoft.beans.MultiChargesOrderBean;
import com.gp.cvst.logisoft.beans.MultiQuoteChargesBean;
import com.gp.cvst.logisoft.domain.MultiQuoteCharges;
import java.util.List;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

/**
 *
 * @author NambuRajasekar.M
 */
public class MultiQuoteChargesDao extends BaseHibernateDAO<MultiQuoteCharges> {

    public MultiQuoteChargesDao() {
        super(MultiQuoteCharges.class);
    }

    public MultiQuoteChargesDao findById(java.lang.Long id) throws Exception {
        MultiQuoteChargesDao instance = (MultiQuoteChargesDao) getSession().get("com.gp.cvst.logisoft.domain.MultiQuoteCharges", id);
        return instance;
    }

    public List<MultiQuoteChargesBean> getChargesExpandList(List multiQuoteId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT m.multi_quote_id AS id, m.unitType AS unitType,m.unit_no AS unitNo, m.charge_code_desc AS chargeCodeDesc, ");
        sb.append("m.amount AS amount,m.number AS number,m.cost_type AS costType, ");
        sb.append("m.markup AS markup,m.adjustment AS adjustment,m.comment AS comments, ");
        sb.append("m.account_no AS acctNo,m.account_name AS acctName,m.adjustment_charge_comments AS adjustmentComments ");
        sb.append("FROM multi_quote_charges m WHERE m.multi_quote_id IN(:multiQuoteId)");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setResultTransformer(Transformers.aliasToBean(MultiQuoteChargesBean.class));
        query.addScalar("id", LongType.INSTANCE);
        query.addScalar("unitType", StringType.INSTANCE);
        query.addScalar("unitNo", StringType.INSTANCE);
        query.addScalar("chargeCodeDesc", StringType.INSTANCE);
        query.addScalar("amount", BigDecimalType.INSTANCE);
        query.addScalar("number", StringType.INSTANCE);
        query.addScalar("costType", StringType.INSTANCE);
        query.addScalar("markup", BigDecimalType.INSTANCE);
        query.addScalar("adjustment", BigDecimalType.INSTANCE);
        query.addScalar("comments", StringType.INSTANCE);
        query.addScalar("acctNo", StringType.INSTANCE);
        query.addScalar("acctName", StringType.INSTANCE);
        query.addScalar("adjustmentComments", StringType.INSTANCE);
        query.setParameterList("multiQuoteId", multiQuoteId);
        return query.list();
    }

    public List<MultiChargesOrderBean> getChargesCollapseList(List Id) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT multi_quote_id AS id,  mq.id AS order_id, SUM(mq.amount + mq.markup) AS Amount,mq.unit_no AS unitNo,");
        sb.append(" mq.charge_code AS chargeCode, mq.charge_code_desc AS chargeCodeDesc,mq.currency AS currency,mq.account_no AS acctNo,");
        sb.append(" mq.account_name AS acctName FROM  `multi_quote_charges` mq WHERE mq.`multi_quote_id` IN(:Id)");
        sb.append(" AND mq.charge_code NOT IN ('INTMDL','BKRSUR','INTFS','INTRAMP','NASLAN','OFIMP','HAZFEE')");
        sb.append(" AND mq.unit_no IN ('A=20','B=40','C=40HC','D=45','E=48')");
        sb.append(" GROUP BY mq.unit_no,mq.multi_quote_id");
        sb.append(" UNION");
        sb.append(" SELECT  multi_quote_id AS id,  mq.id AS order_id,  SUM(mq.amount + mq.markup) AS Amount,  mq.unit_no AS unitNo,");
        sb.append(" mq.charge_code AS chargeCode,  mq.charge_code_desc AS chargeCodeDesc,  mq.currency AS currency,  mq.account_no AS acctNo,");
        sb.append(" mq.account_name AS acctName FROM `multi_quote_charges` mq  WHERE mq.`multi_quote_id` IN(:Id)");
        sb.append(" AND mq.charge_code IN ('INTMDL','BKRSUR','INTFS','INTRAMP','NASLAN','OFIMP','HAZFEE')");
        sb.append(" AND mq.unit_no IN ('A=20','B=40','C=40HC','D=45','E=48')");
        sb.append(" GROUP BY mq.unit_no,mq.multi_quote_id ORDER BY order_id");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setResultTransformer(Transformers.aliasToBean(MultiChargesOrderBean.class));
        query.addScalar("id", LongType.INSTANCE);
        query.addScalar("order_id", LongType.INSTANCE);
        query.addScalar("Amount", BigDecimalType.INSTANCE);
        query.addScalar("unitNo", StringType.INSTANCE);
        query.addScalar("chargeCode", StringType.INSTANCE);
        query.addScalar("chargeCodeDesc", StringType.INSTANCE);
        query.addScalar("currency", StringType.INSTANCE);
        query.addScalar("acctNo", StringType.INSTANCE);
        query.addScalar("acctName", StringType.INSTANCE);
        query.setParameterList("Id", Id);
        return query.list();

    }

    public List<MultiChargesOrderBean> getChargesListForPdf(Long Id) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT multi_quote_id AS id, mq.id AS order_id, SUM(mq.amount+mq.markup) AS Amount, mq.unit_no AS unitNo,");
        sb.append(" mq.charge_code AS chargeCode,mq.charge_code_desc AS chargeCodeDesc,mq.currency AS currency,");
        sb.append(" mq.account_no AS acctNo, mq.account_name AS acctName");
        sb.append(" FROM `multi_quote_charges` mq WHERE mq.`multi_quote_id`=:Id");
        sb.append(" AND  mq.charge_code NOT IN('INTMDL','BKRSUR','INTFS','INTRAMP','NASLAN','OFIMP','HAZFEE')");
        sb.append(" AND mq.unit_no IN ('A=20','B=40','C=40HC','D=45','E=48') GROUP BY mq.unit_no");
        sb.append(" UNION");
        sb.append(" SELECT multi_quote_id AS id, mq.id AS order_id, SUM(mq.amount+mq.markup) AS Amount,mq.unit_no AS unitNo,");
        sb.append(" mq.charge_code AS chargeCode,mq.charge_code_desc AS chargeCodeDesc,mq.currency AS currency,");
        sb.append(" mq.account_no AS acctNo, mq.account_name AS acctName");
        sb.append(" FROM `multi_quote_charges` mq WHERE mq.`multi_quote_id`=:Id");
        sb.append(" AND  mq.charge_code IN('INTMDL','BKRSUR','INTFS','INTRAMP','NASLAN','OFIMP','HAZFEE')");
        sb.append(" AND mq.unit_no IN ('A=20','B=40','C=40HC','D=45','E=48') GROUP BY mq.unit_no ORDER BY order_id ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setResultTransformer(Transformers.aliasToBean(MultiChargesOrderBean.class));
        query.addScalar("id", LongType.INSTANCE);
        query.addScalar("order_id", LongType.INSTANCE);
        query.addScalar("Amount", BigDecimalType.INSTANCE);
        query.addScalar("unitNo", StringType.INSTANCE);
        query.addScalar("chargeCode", StringType.INSTANCE);
        query.addScalar("chargeCodeDesc", StringType.INSTANCE);
        query.addScalar("currency", StringType.INSTANCE);
        query.addScalar("acctNo", StringType.INSTANCE);
        query.addScalar("acctName", StringType.INSTANCE);
        query.setParameter("Id", Id);
        return query.list();

    }

    //RatesChageAlert
    public List getOceanfrt(Long Id) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT multi_quote_id AS id, mq.id AS order_id, SUM(mq.amount+mq.markup) AS Amount,mq.unit_no AS unitNo,");
        sb.append(" mq.charge_code AS chargeCode,mq.charge_code_desc AS chargeCodeDesc,mq.currency AS currency,");
        sb.append(" mq.account_no AS acctNo, mq.account_name AS acctName");
        sb.append(" FROM `multi_quote_charges` mq WHERE mq.`multi_quote_id`=:Id");
        sb.append(" AND mq.unit_no IN ('A=20','B=40','C=40HC','D=45','E=48') GROUP BY mq.unit_no ORDER BY order_id ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setResultTransformer(Transformers.aliasToBean(MultiChargesOrderBean.class));
        query.addScalar("id", LongType.INSTANCE);
        query.addScalar("order_id", LongType.INSTANCE);
        query.addScalar("Amount", BigDecimalType.INSTANCE);
        query.addScalar("unitNo", StringType.INSTANCE);
        query.addScalar("chargeCode", StringType.INSTANCE);
        query.addScalar("chargeCodeDesc", StringType.INSTANCE);
        query.addScalar("currency", StringType.INSTANCE);
        query.addScalar("acctNo", StringType.INSTANCE);
        query.addScalar("acctName", StringType.INSTANCE);
        query.setParameter("Id", Id);
        return query.list();
    }
}

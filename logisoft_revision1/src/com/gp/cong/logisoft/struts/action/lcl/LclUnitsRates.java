package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.beans.ImportsManifestBean;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

/**
 *
 * @author Lakshmi Narayanan
 */
public class LclUnitsRates extends BaseHibernateDAO {

    public List<ImportsManifestBean> findAutoCosts(int originId, int destinationId, int warehouseId, Long unitTypeId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  gl.`id` as chargeId,");
        queryBuilder.append("  gl.`charge_code` as chargeCode,");
        queryBuilder.append("  coalesce(gl.`charge_description`, cost.`codedesc`) as customerNumber,");
        queryBuilder.append("  rates.`rat_amount` as totalIPI,");
        queryBuilder.append("  rates.`ssline_no` as agentNo ");
        queryBuilder.append("from");
        queryBuilder.append("  `rates_list` rates ");
        queryBuilder.append("  join `genericcode_dup` cost ");
        queryBuilder.append("    on (rates.`cost_id` = cost.`id`) ");
        queryBuilder.append("  join `gl_mapping` gl ");
        queryBuilder.append("    on (");
        queryBuilder.append("      cost.`code` = gl.`charge_code` ");
        queryBuilder.append("      and gl.`shipment_type` = 'LCLI' ");
        queryBuilder.append("      and gl.`transaction_type` = 'AC'");
        queryBuilder.append("    ) ");
        queryBuilder.append("where");
        queryBuilder.append("  rates.`origin_terminal` = :originId ");
        queryBuilder.append("  and rates.`destination_port` = :destinationId ");
        queryBuilder.append("  and (");
        queryBuilder.append("    (");
        queryBuilder.append("       rates.`com_num` = (select commodity.`id` from `genericcode_dup` commodity where commodity.`code` = :commodityCode limit 1)");
        queryBuilder.append("       and rates.`ssline_no` = (select ssline.`acct_no` from `trading_partner` ssline where ssline.`ssline_number` = :sslineNo limit 1)");
        queryBuilder.append("    )");
        queryBuilder.append("    or ");
        queryBuilder.append("    (");
        queryBuilder.append("       rates.`com_num` = (select com.`id` from `genericcode_dup` com where com.`code` = (select wh.`commodityno` from `warehouse` wh where wh.`id` = :warehouseId limit 1) limit 1)");
        queryBuilder.append("       and rates.`ssline_no` = (select wh.`vendorno` from `warehouse` wh where wh.`id` = :warehouseId limit 1)");
        queryBuilder.append("    )  ");
        queryBuilder.append("  )");
        queryBuilder.append("  and rates.`unit_type` in (select gu.`id` from `genericcode_dup` gu where gu.`code` = (select ut.`elite_type` from `unit_type` ut where ut.`id` = :unitTypeId limit 1))");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setInteger("originId", originId);
        query.setInteger("destinationId", destinationId);
        query.setString("commodityCode", "926400");
        query.setString("sslineNo", "00719");
        query.setInteger("warehouseId", warehouseId);
        query.setLong("unitTypeId", unitTypeId);
        query.addScalar("chargeId", LongType.INSTANCE);
        query.addScalar("chargeCode", StringType.INSTANCE);
        query.addScalar("customerNumber", StringType.INSTANCE);
        query.addScalar("totalIPI", BigDecimalType.INSTANCE);
        query.addScalar("agentNo", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(ImportsManifestBean.class));
        return query.list();
    }

    public List<ImportsManifestBean> findAutoCostsByLCLE(Integer originId, int destinationId,
            Long unitTypeId, String ssLineNo, Integer departId, String hazFlag) throws Exception {
        String commodityNo = "009900";
        List<ImportsManifestBean> autoCostList = getAutoCostByLCLE(originId, destinationId, unitTypeId, ssLineNo, commodityNo, hazFlag);
        if (CommonUtils.isEmpty(autoCostList)) {
            commodityNo = "006100";
            autoCostList = getAutoCostByLCLE(originId, destinationId, unitTypeId, ssLineNo, commodityNo, hazFlag);
        }
        if (originId.intValue() != departId.intValue() && CommonUtils.isEmpty(autoCostList)) {
            autoCostList = getAutoCostByLCLE(departId, destinationId, unitTypeId, ssLineNo, "009900", hazFlag);
        }
        if (originId.intValue() != departId.intValue() && CommonUtils.isEmpty(autoCostList)) {
            autoCostList = getAutoCostByLCLE(departId, destinationId, unitTypeId, ssLineNo, "006100", hazFlag);
        }
        return autoCostList;

    }

    public List<ImportsManifestBean> getAutoCostByLCLE(int originId, int destinationId,
            Long unitTypeId, String ssLineNo, String commodityNo, String hazFlag) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("select ");
//        queryStr.append(" (SELECT g.`id` FROM gl_mapping g WHERE g.`charge_code` =cost.`code` ");
//        queryStr.append(" AND g.`shipment_type` = 'LCLE' AND g.`transaction_type` = 'AC' LIMIT 1)  AS chargeId,  ");
        queryStr.append(" cost.`code` as chargeCode, ");
        queryStr.append("  rates.`rat_amount` as totalIPI,");
        queryStr.append("  rates.`ssline_no` as agentNo ");
        queryStr.append("from");
        queryStr.append("  `rates_list` rates ");
        queryStr.append("  join `genericcode_dup` cost ");
        queryStr.append("    on (rates.`cost_id` = cost.`id`) ");
        queryStr.append("where");
        queryStr.append("  rates.`origin_terminal` = :originId ");
        queryStr.append("  and rates.`destination_port` = :destinationId ");
        queryStr.append("  and ");
        queryStr.append(" rates.`com_num` = (select commodity.`id` from `genericcode_dup` commodity where commodity.`code` = :commodityCode limit 1)");
        queryStr.append(" and rates.`ssline_no` =:sslineNo ");
        queryStr.append("  and rates.`unit_type` in (select gu.`id` from `genericcode_dup` gu where gu.`code` = (select ut.`elite_type` from `unit_type` ut where ut.`id` = :unitTypeId limit 1)");
        queryStr.append(" AND gu.Codetypeid=(SELECT c.codetypeid FROM codetype c WHERE c.description='Unit Sizes')) ");
        if (CommonUtils.isEmpty(hazFlag)) {
            queryStr.append(" and cost.code <> 'HAZFEE' ");
        }
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setInteger("originId", originId);
        query.setInteger("destinationId", destinationId);
        query.setString("commodityCode", commodityNo);
        query.setString("sslineNo", ssLineNo);
        query.setLong("unitTypeId", unitTypeId);
        //query.addScalar("chargeId", LongType.INSTANCE);
        query.addScalar("totalIPI", BigDecimalType.INSTANCE);
        query.addScalar("agentNo", StringType.INSTANCE);
        query.addScalar("chargeCode", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(ImportsManifestBean.class));
        return query.list();
    }
}

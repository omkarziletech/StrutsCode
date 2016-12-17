package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.beans.EculineEdiBean;
import com.gp.cong.logisoft.beans.EculineEdiBlBean;
import com.gp.cong.logisoft.beans.EculineEdiCargoDetailsBean;
import com.gp.cong.logisoft.domain.lcl.BlInfo;
import com.gp.cong.logisoft.domain.lcl.ContainerInfo;
import com.gp.cong.logisoft.domain.lcl.EculineEdi;
import com.gp.cong.logisoft.domain.lcl.EculineediNotes;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.BigIntegerType;
import org.hibernate.type.BooleanType;
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

/**
 *
 * @author Rajesh
 */
public class EculineEdiDao extends BaseHibernateDAO<EculineEdi> {

    public EculineEdiDao() {
        super(EculineEdi.class);
    }

    public EculineEdi SearchById(Long id) throws Exception {
        return (EculineEdi) getSession().getNamedQuery("EculineEdi.findById").setLong("id", id).uniqueResult();
    }

    public EculineEdi getVoyDetails(String voyNo, String cntrName) throws Exception {
        Criteria criteria = getSession().createCriteria(ContainerInfo.class, "container");
        criteria.add(Restrictions.eq("container.cntrName", cntrName));
        criteria.createAlias("container.eculineEdi", "eculineEdi");
        criteria.add(Restrictions.eq("eculineEdi.voyNo", voyNo));
        ContainerInfo containerInfo = (ContainerInfo) criteria.uniqueResult();
        return (EculineEdi) new EculineEdiDao().findById(containerInfo.getEculineEdi().getId());
    }

    public List<EculineEdiBean> search(Map<String, Object> filters, Integer limit) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  edi.`id` as id,");
        queryBuilder.append("  edi.`cntr_name` as containerNo,");
        queryBuilder.append("  edi.`etd` as sailDate,");
        queryBuilder.append("  edi.`eta` as arvlDate,");
        queryBuilder.append("  edi.`voy_no` as voyNo,");
        queryBuilder.append("  edi.`pol_uncode` as polUncode,");
        queryBuilder.append("  edi.`origin` as origin,");
        queryBuilder.append("  edi.`origin_id` as originId,");
        queryBuilder.append("  edi.`pod_uncode` as podUncode,");
        queryBuilder.append("  edi.`destination` as destination,");
        queryBuilder.append("  edi.`destination_id` as destinationId,");
        queryBuilder.append("  edi.`vessel_code` as vesselCode,");
        queryBuilder.append("  edi.`vessel_name` as vesselName,");
        queryBuilder.append("  edi.`container_size` as contSize,");
        queryBuilder.append("  edi.`short_container_size` as shortContSize,");
        queryBuilder.append("  edi.`pieces` as pieces,");
        queryBuilder.append("  edi.`cube` as cube,");
        queryBuilder.append("  edi.`weight` as weight,");
        queryBuilder.append("  edi.`header_ref` as refNo,");
        queryBuilder.append("  edi.`no_of_bl` as noOfBl,");
        queryBuilder.append("  count(distinct ei.`id`) as invCount,");
        queryBuilder.append("  count(distinct case when eid.`invoice_status` = true then ei.`id` else null end) as invProcessed,");
        queryBuilder.append("  edi.`scac_code` as scacCode,");
        queryBuilder.append("  edi.`shipping_scac` as sslineNo,");
        queryBuilder.append("  edi.`shipping_name` as sslineName,");
        queryBuilder.append("  edi.`lloyds_no` as lloydsNo,");
        queryBuilder.append("  edi.`seal_no` as sealNo,");
        queryBuilder.append("  edi.`master_bl` as masterBl,");
        queryBuilder.append("  edi.`sender_code` as sender,");
        queryBuilder.append("  edi.`sender_email` as senderEmail,");
        queryBuilder.append("  edi.`receiver_code` as receiver,");
        queryBuilder.append("  edi.`receiver_email` as receiverEmail,");
        queryBuilder.append("  edi.`terminal_id` as terminalNo,");
        queryBuilder.append("  edi.`agent_no` as agentNo,");
        queryBuilder.append("  edi.`warehouse_id` as warehouseNo,");
        queryBuilder.append("  concat(wh.`warehsname`, ' - ', wh.`warehsno`)  as warehouseName,");
        queryBuilder.append("  edi.`warehouse_address` as warehouseAddress,");
        queryBuilder.append("  ssh.`id` as eciVoy,");
        queryBuilder.append("  uss.`unit_id` as unitId,");
        queryBuilder.append("  ssh.`schedule_no` as scheduleNo,");
        queryBuilder.append("  concat(tm.`terminal_location`, ',', tm.`trmnum`) as billingTerminal,");
        queryBuilder.append("  edi.`is_approved` as approved,");
        queryBuilder.append("  edi.`cntr_remarks` as containerRemarks,");
        queryBuilder.append("  edi.`unit_type_id` as unitTypeId,");
        queryBuilder.append("  edi.`vessel_error_check` as vesselErrorCheck,");
        queryBuilder.append("  count(edi.`origin_id`) as unPolCount,");
        queryBuilder.append("  count(edi.`destination_id`) as unPodCount,");
        queryBuilder.append("  edi.`adjudicated` as adjudicated, ");
        queryBuilder.append("  DATE_FORMAT(edi.doc_received,'%d-%b-%Y')  AS docReceived ");
        queryBuilder.append("from");
        queryBuilder.append("  (select ");
        queryBuilder.append("    edi.*,");
        queryBuilder.append("    sum(cd.`package_amount`) as pieces,");
        queryBuilder.append("    sum(cd.`volume_values`) as cube,");
        queryBuilder.append("    sum(cd.`weight_values`) as weight,");
        queryBuilder.append("    count(distinct bi.id) as no_of_bl,");
        queryBuilder.append("    bi.`bl_no` as bl_no ");
        queryBuilder.append("  from");
        queryBuilder.append("    (select ");
        queryBuilder.append("      edi.*,");
        queryBuilder.append("      pol.`id` as origin_id,");
        queryBuilder.append("      pol.`un_loc_code` as pol_un_code,");
        queryBuilder.append("      `UnLocationGetNameStateCntryByID` (pol.`id`) as origin,");
        queryBuilder.append("      pod.`id` as destination_id,");
        queryBuilder.append("      pod.`un_loc_code` as pod_un_code,");
        queryBuilder.append("      `UnLocationGetNameStateCntryByID` (pod.`id`) as destination,");
        queryBuilder.append("      ves.`code` as vessel_code,");
        queryBuilder.append("      coalesce(ut.`description`,  edi.`cntr_type`) as container_size,");
        queryBuilder.append("      ut.`short_desc` as short_container_size,");
        queryBuilder.append("      ut.`id` as unit_type_id ");
        queryBuilder.append("    from");
        queryBuilder.append("      (select ");
        queryBuilder.append("        edi.`id`,");
        queryBuilder.append("        ci.`cntr_name`,");
        queryBuilder.append("        edi.`etd`,");
        queryBuilder.append("        edi.`eta`,");
        queryBuilder.append("        edi.`voy_no`,");
        queryBuilder.append("        edi.`pol_uncode`,");
        queryBuilder.append("        edi.`pod_uncode`,");
        queryBuilder.append("        ci.`cntr_type`,");
        queryBuilder.append("        edi.`vessel_error_check`,");
        queryBuilder.append("        edi.`vessel_name`,");
        queryBuilder.append("        edi.`header_ref`,");
        queryBuilder.append("        edi.`shipping_scac`,");
        queryBuilder.append("        edi.`scac_code`,");
        queryBuilder.append("        edi.`shipping_name`,");
        queryBuilder.append("        edi.`lloyds_No`,");
        queryBuilder.append("        ci.`seal_no`,");
        queryBuilder.append("        ci.`master_bl`,");
        queryBuilder.append("        edi.`sender_code`,");
        queryBuilder.append("        edi.`sender_email`,");
        queryBuilder.append("        edi.`receiver_code`,");
        queryBuilder.append("        edi.`receiver_email`,");
        queryBuilder.append("        edi.`terminal_id`,");
        queryBuilder.append("        ci.`agent_no`,");
        queryBuilder.append("        ci.`warehouse_id`,");
        queryBuilder.append("        ci.`warehouse_address`,");
        queryBuilder.append("        edi.`is_approved`,");
        queryBuilder.append("        ci.`cntr_remarks`,");
        queryBuilder.append("        edi.`unitss_id`,");
        queryBuilder.append("        edi.`adjudicated`,");
        queryBuilder.append("        ci.`id` as container_info_id, ");
        queryBuilder.append("        edi.doc_received ");
        queryBuilder.append("      from");
        queryBuilder.append("        eculine_edi edi ");
        queryBuilder.append("        join `container_info` ci ");
        queryBuilder.append("          on (edi.`id` = ci.`eculine_edi_id`) ");
        if (CommonUtils.isNotEmpty(filters)) {
            queryBuilder.append("      where");
            boolean isNotFirst = false;
            for (String fieldName : filters.keySet()) {
                if ("ignoreApproved".equalsIgnoreCase(fieldName)) {
                    queryBuilder.append("        ").append(isNotFirst ? "and " : "").append("(");
                    queryBuilder.append("           (edi.`adjudicated` = 1 and edi.`is_approved` = 0)");
                    queryBuilder.append("           or (edi.`adjudicated` = 0 and edi.`is_approved` = 0)");
                    queryBuilder.append("         )");
                } else if ("ignoreUnapproved".equalsIgnoreCase(fieldName)) {
                    queryBuilder.append("        ").append(isNotFirst ? "and " : "").append("(");
                    queryBuilder.append("         (edi.`adjudicated` = 1 and edi.`is_approved` = 0)");
                    queryBuilder.append("         or edi.`is_approved` = 1)");
                } else if ("ignoreReadyToApprove".equalsIgnoreCase(fieldName)) {
                    queryBuilder.append("        ").append(isNotFirst ? "and " : "").append("(");
                    queryBuilder.append("         (edi.`adjudicated` = 0 and edi.`is_approved` = 0)");
                    queryBuilder.append("         or edi.`is_approved` = 1)");
                } else if ("approvedOnly".equalsIgnoreCase(fieldName)) {
                    queryBuilder.append("        ").append(isNotFirst ? "and " : "").append("edi.`is_approved` = 1");
                } else if ("unapprovedOnly".equalsIgnoreCase(fieldName)) {
                    queryBuilder.append("        ").append(isNotFirst ? "and " : "").append("edi.`is_approved` = 0");
                    queryBuilder.append("        and edi.`adjudicated` = 0");
                } else if ("readyToApproveOnly".equalsIgnoreCase(fieldName)) {
                    queryBuilder.append("        ").append(isNotFirst ? "and " : "").append("edi.`adjudicated` = 1");
                    queryBuilder.append("         and edi.`is_approved` = 0");
                } else if ("ei.`invoice_number`".equalsIgnoreCase(fieldName)) {
                    queryBuilder.append("        ").append(isNotFirst ? "and " : "").append("ci.`cntr_name` <= (select ");
                    queryBuilder.append("          ci.`cntr_name` as cntr_name");
                    queryBuilder.append("        from");
                    queryBuilder.append("          `edi_invoice` ei");
                    queryBuilder.append("          join `edi_invoice_detail` eid");
                    queryBuilder.append("            on (eid.`edi_invoice_id` = ei.`id`)");
                    queryBuilder.append("          join `bl_info` bi");
                    queryBuilder.append("            on (bi.`bl_no` = eid.`bl_reference`)");
                    queryBuilder.append("          join `container_info` ci");
                    queryBuilder.append("            on (ci.`id` = bi.`container_info_id`)");
                    queryBuilder.append("        where ").append(fieldName).append(" = :").append(fieldName.replace(".", "_").replace("`", ""));
                    queryBuilder.append("          and ei.`status` <> 'EDI Duplicate'");
                    queryBuilder.append("          and ei.`status` <> 'EDI Archive') as container");
                    queryBuilder.append("        )");
                } else if ("ci.`cntr_name`".equalsIgnoreCase(fieldName)) {
                    queryBuilder.append("        ").append(isNotFirst ? "and " : "").append(fieldName).append(" <= :").append(fieldName.replace(".", "_").replace("`", ""));
                } else {
                    queryBuilder.append("        ").append(isNotFirst ? "and " : "").append(fieldName).append(" = :").append(fieldName.replace(".", "_").replace("`", ""));
                }
                isNotFirst = true;
            }
        }
        queryBuilder.append("      group by edi.`id` ");
        if (CommonUtils.isNotEmpty(filters) && (filters.containsKey("ci.`cntr_name`") || filters.containsKey("ei.`invoice_number`"))) {
            queryBuilder.append("      order by ci.`cntr_name` desc");
        } else {
            queryBuilder.append("      order by edi.`eta` desc");
        }
        queryBuilder.append("      limit ").append(limit).append(") as edi ");
        queryBuilder.append("      left join `un_location` pol ");
        queryBuilder.append("        on (pol.`un_loc_code` = edi.`pol_uncode`) ");
        queryBuilder.append("      left join `un_location` pod ");
        queryBuilder.append("        on (pod.`un_loc_code` = edi.`pod_uncode`) ");
        queryBuilder.append("      left join `genericcode_dup` ves ");
        queryBuilder.append("        on (");
        queryBuilder.append("          edi.`vessel_name` = ves.`codedesc` ");
        queryBuilder.append("          and ves.`codetypeid` = CodeTypeGetIDByDesc ('Vessel Codes')");
        queryBuilder.append("        ) ");
        queryBuilder.append("      left join `unit_type` ut ");
        queryBuilder.append("        on (ut.`description` = edi.`cntr_type`) ");
        queryBuilder.append("    group by edi.`id`) as edi ");
        queryBuilder.append("    left join `bl_info` bi ");
        queryBuilder.append("      on (edi.`container_info_id` = bi.`container_info_id`) ");
        queryBuilder.append("    left join cargo_details cd ");
        queryBuilder.append("      on (bi.`id` = cd.`bl_info_id`) ");
        queryBuilder.append("  group by edi.`id`) as edi ");
        queryBuilder.append("  left join `edi_invoice_detail` eid ");
        queryBuilder.append("    on (eid.`bl_reference` = edi.`bl_no`) ");
        queryBuilder.append("  left join `edi_invoice` ei ");
        queryBuilder.append("    on (");
        queryBuilder.append("      eid.`edi_invoice_id` = ei.`id` ");
        queryBuilder.append("      and ei.`status` <> 'EDI Duplicate' ");
        queryBuilder.append("      and ei.`status` <> 'EDI Archive'");
        queryBuilder.append("    ) ");
        queryBuilder.append("  left join `lcl_unit_ss` uss ");
        queryBuilder.append("    on (uss.`id` = edi.`unitss_id`) ");
        queryBuilder.append("  left join `lcl_ss_header` ssh ");
        queryBuilder.append("    on (ssh.`id` = uss.`ss_header_id`) ");
        queryBuilder.append("  left join `terminal` tm ");
        queryBuilder.append("    on (tm.`trmnum` = edi.`terminal_id`) ");
        queryBuilder.append("  left join `warehouse` wh ");
        queryBuilder.append("    on (wh.`id` = edi.`warehouse_id`) ");
        queryBuilder.append("group by edi.`id` ");
        if (CommonUtils.isNotEmpty(filters) && (filters.containsKey("ci.`cntr_name`") || filters.containsKey("ei.`invoice_number`"))) {
            queryBuilder.append("order by edi.`cntr_name` desc");
        } else {
            queryBuilder.append("order by edi.`eta` desc");
        }
        SQLQuery query = getSession().createSQLQuery(queryBuilder.toString());
        if (CommonUtils.isNotEmpty(filters)) {
            for (String fieldName : filters.keySet()) {
                if (CommonUtils.in(fieldName, "ignoreApproved", "ignoreUnapproved", "ignoreReadyToApprove", "approvedOnly", "unapprovedOnly", "readyToApproveOnly")) {
                    //no need to set parameters 
                } else {
                    Object value = filters.get(fieldName);
                    if (value instanceof String) {
                        query.setString(fieldName.replace(".", "_").replace("`", ""), (String) value);
                    } else if (value instanceof Integer) {
                        query.setInteger(fieldName.replace(".", "_").replace("`", ""), (Integer) value);
                    } else if (value instanceof Double) {
                        query.setDouble(fieldName.replace(".", "_").replace("`", ""), (Double) value);
                    } else if (value instanceof Long) {
                        query.setLong(fieldName.replace(".", "_").replace("`", ""), (Long) value);
                    } else if (value instanceof BigDecimal) {
                        query.setBigDecimal(fieldName.replace(".", "_").replace("`", ""), (BigDecimal) value);
                    } else if (value instanceof BigInteger) {
                        query.setBigInteger(fieldName.replace(".", "_").replace("`", ""), (BigInteger) value);
                    } else if (value instanceof Boolean) {
                        query.setBoolean(fieldName.replace(".", "_").replace("`", ""), (Boolean) value);
                    } else if (value instanceof Date) {
                        query.setDate(fieldName.replace(".", "_").replace("`", ""), (Date) value);
                    } else if (value instanceof Float) {
                        query.setFloat(fieldName.replace(".", "_").replace("`", ""), (Float) value);
                    }
                }
            }
        }
        query.setResultTransformer(Transformers.aliasToBean(EculineEdiBean.class));
        query.addScalar("id", BigIntegerType.INSTANCE);
        query.addScalar("containerNo", StringType.INSTANCE);
        query.addScalar("sailDate", DateType.INSTANCE);
        query.addScalar("arvlDate", DateType.INSTANCE);
        query.addScalar("voyNo", StringType.INSTANCE);
        query.addScalar("polUncode", StringType.INSTANCE);
        query.addScalar("podUncode", StringType.INSTANCE);
        query.addScalar("origin", StringType.INSTANCE);
        query.addScalar("destination", StringType.INSTANCE);
        query.addScalar("originId", BigIntegerType.INSTANCE);
        query.addScalar("destinationId", BigIntegerType.INSTANCE);
        query.addScalar("vesselName", StringType.INSTANCE);
        query.addScalar("vesselCode", StringType.INSTANCE);
        query.addScalar("contSize", StringType.INSTANCE);
        query.addScalar("shortContSize", StringType.INSTANCE);
        query.addScalar("pieces", StringType.INSTANCE);
        query.addScalar("cube", StringType.INSTANCE);
        query.addScalar("weight", StringType.INSTANCE);
        query.addScalar("refNo", StringType.INSTANCE);
        query.addScalar("invCount", IntegerType.INSTANCE);
        query.addScalar("invProcessed", IntegerType.INSTANCE);
        query.addScalar("noOfBl", StringType.INSTANCE);
        query.addScalar("sslineNo", StringType.INSTANCE);
        query.addScalar("scacCode", StringType.INSTANCE);
        query.addScalar("sslineName", StringType.INSTANCE);
        query.addScalar("lloydsNo", StringType.INSTANCE);
        query.addScalar("sealNo", StringType.INSTANCE);
        query.addScalar("masterBl", StringType.INSTANCE);
        query.addScalar("sender", StringType.INSTANCE);
        query.addScalar("senderEmail", StringType.INSTANCE);
        query.addScalar("receiver", StringType.INSTANCE);
        query.addScalar("receiverEmail", StringType.INSTANCE);
        query.addScalar("terminalNo", StringType.INSTANCE);
        query.addScalar("agentNo", StringType.INSTANCE);
        query.addScalar("warehouseNo", StringType.INSTANCE);
        query.addScalar("warehouseName", StringType.INSTANCE);
        query.addScalar("warehouseAddress", StringType.INSTANCE);
        query.addScalar("eciVoy", StringType.INSTANCE);
        query.addScalar("unitId", StringType.INSTANCE);
        query.addScalar("scheduleNo", StringType.INSTANCE);
        query.addScalar("billingTerminal", StringType.INSTANCE);
        query.addScalar("approved", BooleanType.INSTANCE);
        query.addScalar("containerRemarks", StringType.INSTANCE);
        query.addScalar("unitTypeId", StringType.INSTANCE);
        query.addScalar("adjudicated", BooleanType.INSTANCE);
        query.addScalar("vesselErrorCheck", BooleanType.INSTANCE);
        query.addScalar("unPolCount", StringType.INSTANCE);
        query.addScalar("unPodCount", StringType.INSTANCE);
        query.addScalar("docReceived", StringType.INSTANCE);
        return query.list();
    }

    public List<BigInteger> validate(String voyNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT DISTINCT ecu.id");
        queryBuilder.append(" FROM eculine_edi ecu");
        queryBuilder.append(" LEFT JOIN container_info c_info ");
        queryBuilder.append(" ON ecu.id = c_info.eculine_edi_id");
        queryBuilder.append(" LEFT JOIN bl_info bl");
        queryBuilder.append(" ON c_info.id = bl.container_info_id ");
        queryBuilder.append(" LEFT JOIN cargo_details cargo");
        queryBuilder.append(" ON bl.id = cargo.bl_info_id");
        queryBuilder.append(buildWhereCondition());
        SQLQuery query = getSession().createSQLQuery(queryBuilder.toString());
        return query.list();
    }

    public List<BigInteger> validateBl() throws Exception {
        StringBuilder query = new StringBuilder();
        query.append("SELECT DISTINCT bl.id");
        query.append(" FROM bl_info bl");
        query.append(" JOIN cargo_details cargo");
        query.append(" ON bl.id = cargo.bl_info_id");
        query.append(buildWhereCondition());
        SQLQuery sqlQuery = getSession().createSQLQuery(query.toString());
        return sqlQuery.list();
    }

    private String buildWhereCondition() throws Exception {
        StringBuilder whereCondition = new StringBuilder();
        whereCondition.append(" WHERE cargo.package_desc NOT IN");
        whereCondition.append(" (SELECT DISTINCT CONCAT(TYPE, plural) FROM package_type)");
        whereCondition.append(" AND cargo.package_desc NOT IN");
        whereCondition.append(" (SELECT DISTINCT TYPE FROM package_type ORDER BY TYPE)");
        whereCondition.append(" AND cargo.package_desc NOT IN");
        whereCondition.append(" (SELECT DISTINCT description FROM package_type)");
        whereCondition.append(" AND cargo.package_desc NOT IN");
        whereCondition.append(" (SELECT abbr01 FROM package_type)");
        return whereCondition.toString();
    }

    public List<EculineEdiBlBean> getBillOflading(Map<String, String> values) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  bi.`id` as id,");
        queryBuilder.append("  bi.`bl_no` as blNo,");
        queryBuilder.append("  bi.`ams_scac` as amsScac,");
        queryBuilder.append("  fn.`id` as fileId,");
        queryBuilder.append("  fn.`file_number` as fileNo,");
        queryBuilder.append("  eid.`gl_id` as  arGlMapping,");
        queryBuilder.append("  eid.ap_gl_mapping_id as apGlMapping,");
        queryBuilder.append("  coalesce(min(case when eid.charge_status = 'P' or eid.charge_status = 'D'  then 1 else null end), 0) as chargesPostedStatus,");
        queryBuilder.append("  ei.`invoice_number` as invoiceNo,");
        queryBuilder.append("  coalesce(min(eid.`invoice_status`), 0) as invoiceStatus,");
        queryBuilder.append("  coalesce(shipa.`acct_no`, bi.`shipper_code`) as shipperNo,");
        queryBuilder.append("  coalesce(shipa.`address1`, substring(bi.`shipper_nad`, 1, 20)) as shipper,");
        queryBuilder.append("  coalesce(consa.`acct_no`, bi.`cons_code`) as consigneeNo,");
        queryBuilder.append("  coalesce(consa.`address1`, substring(bi.`cons_nad`, 1, 20)) as consignee,");
        queryBuilder.append("  coalesce(notia.`acct_no`, bi.`notify1_code`) as notifyNo,");
        queryBuilder.append("  coalesce(notia.`address1`, substring(bi.`notify1_nad`, 1, 20)) as notify,");
        queryBuilder.append("  bi.`place_of_receipt` as por,");
        queryBuilder.append("  pol.`un_loc_code` as pol,");
        queryBuilder.append("  UnLocationGetNameStateCntryByID(pol.`id`) as origin,");
        queryBuilder.append("  pod.`un_loc_code` as pod,");
        queryBuilder.append("  UnLocationGetNameStateCntryByID(pod.`id`) as destn,");
        queryBuilder.append("  podv.`un_loc_code` as delivery,");
        queryBuilder.append("  UnLocationGetNameStateCntryByID(podv.`id`) as destination,");
        queryBuilder.append("  bi.`precarriage_by`,");
        queryBuilder.append("  bi.`prepaid_collect` as shipTerms,");
        queryBuilder.append("  bi.`no_docs_original` as expressRelease,");
        queryBuilder.append("  bi.`pieces` as pieces,");
        queryBuilder.append("  bi.`package_type` as packType,");
        queryBuilder.append("  pt.`id` as packageId,");
        queryBuilder.append("  bi.`weight` as weight,");
        queryBuilder.append("  bi.`weight_unit` as weightUnit,");
        queryBuilder.append("  bi.`volume` as volume,");
        queryBuilder.append("  bi.`volume_unit` as volumeUnit,");
        queryBuilder.append("  bi.`is_approved` as approved,");
        queryBuilder.append("  bi.`adjudicated` as adjudicated,");
        queryBuilder.append("  bi.`conainer_id` as cntrId,");
        queryBuilder.append("  count(pol.`id`) as unPolCount,");
        queryBuilder.append("  count(pod.`id`) as unPodCount,");
        queryBuilder.append("  count(podv.`id`) as unDelCount ");
        queryBuilder.append("from");
        queryBuilder.append("  (select ");
        queryBuilder.append("    bi.`id`,");
        queryBuilder.append("    bi.`bl_no`,");
        queryBuilder.append("    bi.`ams_scac`,");
        queryBuilder.append("    bi.`file_number_id`,");
        queryBuilder.append("    bi.`shipper_code`,");
        queryBuilder.append("    bi.`shipper_nad`,");
        queryBuilder.append("    bi.`cons_code`,");
        queryBuilder.append("    bi.`cons_nad`,");
        queryBuilder.append("    bi.`notify1_code`,");
        queryBuilder.append("    bi.`notify1_nad`,");
        queryBuilder.append("    bi.`place_of_receipt`,");
        queryBuilder.append("    bi.`pol_uncode`,");
        queryBuilder.append("    bi.`pod_uncode`,");
        queryBuilder.append("    bi.`poddelivery_uncode`,");
        queryBuilder.append("    bi.`poddelivery_desc`,");
        queryBuilder.append("    bi.`precarriage_by`,");
        queryBuilder.append("    bi.`prepaid_collect`,");
        queryBuilder.append("    bi.`no_docs_original`,");
        queryBuilder.append("    sum(cd.`package_amount`) as pieces,");
        queryBuilder.append("    cd.`package_desc` as package_type,");
        queryBuilder.append("    sum(cd.`weight_values`) as weight,");
        queryBuilder.append("    cd.`weight_unit`,");
        queryBuilder.append("    sum(cd.`volume_values`) as volume,");
        queryBuilder.append("    cd.`volume_unit`,");
        queryBuilder.append("    ci.`id` as conainer_id,");
        queryBuilder.append("    edi.`pol_uncode` as edi_pol_uncode,");
        queryBuilder.append("    edi.`pod_uncode` as edi_pod_uncode,");
        queryBuilder.append("    bi.`adjudicated`,");
        queryBuilder.append("    bi.`is_approved` ");
        queryBuilder.append("  from");
        queryBuilder.append("    `eculine_edi` edi ");
        queryBuilder.append("    join `container_info` ci ");
        queryBuilder.append("      on (edi.`id` = ci.`eculine_edi_id`) ");
        queryBuilder.append("    join `bl_info` bi ");
        queryBuilder.append("      on (ci.`id` = bi.`container_info_id`) ");
        queryBuilder.append("    join `cargo_details` cd ");
        queryBuilder.append("      on (bi.`id` = cd.`bl_info_id`)");
        if (CommonUtils.isNotEmpty(values.get("id"))) {
            queryBuilder.append("  where edi.`id` = ").append(values.get("id"));
        } else if (CommonUtils.isNotEmpty(values.get("fileId"))) {
            queryBuilder.append("  where bi.`file_number_id` = ").append(values.get("fileId"));
        }
        queryBuilder.append("  group by bi.`id`) as bi ");
        queryBuilder.append("  left join `lcl_file_number` fn");
        queryBuilder.append("    on (bi.`file_number_id` = fn.`id`)  ");
        queryBuilder.append("  left join `edi_invoice_detail` eid");
        queryBuilder.append("    on (bi.`bl_no` = eid.`bl_reference`)");
        queryBuilder.append("  left join `edi_invoice` ei");
        queryBuilder.append("    on (eid.`edi_invoice_id` = ei.`id` and ei.`status` <> 'EDI Duplicate' and ei.`status` <> 'EDI Archive')");
        queryBuilder.append("  left join `cust_address` shipa");
        queryBuilder.append("    on (bi.`shipper_code` = shipa.`acct_no` and shipa.`prime` = 'on')");
        queryBuilder.append("  left join `cust_address` consa");
        queryBuilder.append("    on (bi.`cons_code` = consa.`acct_no` and consa.`prime` = 'on')");
        queryBuilder.append("  left join `cust_address` notia");
        queryBuilder.append("    on (bi.`notify1_code` = notia.`acct_no` and notia.`prime` = 'on')");
        queryBuilder.append("  left join `un_location` pol");
        queryBuilder.append("    on (bi.`pol_uncode` = pol.`un_loc_code`)");
        queryBuilder.append("  left join `un_location` pod ");
        queryBuilder.append("    on (bi.`pod_uncode` = pod.`un_loc_code`)");
        queryBuilder.append("  left join `un_location` podv ");
        queryBuilder.append("    on (bi.`poddelivery_uncode` = podv.`un_loc_code`)");
        queryBuilder.append("  left join `eculine_package_type_mapping` ptm ");
        queryBuilder.append("    on (");
        queryBuilder.append("      bi.`package_type` = ptm.`package_desc`");
        queryBuilder.append("    )");
        queryBuilder.append("  left join package_type pt ");
        queryBuilder.append("    on (");
        queryBuilder.append("      bi.`package_type` <> '' ");
        queryBuilder.append("      and (");
        queryBuilder.append("        ptm.`package_type_id` = pt.`id`");
        queryBuilder.append("        or (");
        queryBuilder.append("          ptm.`package_type_id` is null ");
        queryBuilder.append("          and (");
        queryBuilder.append("            bi.`package_type` = pt.`type` ");
        queryBuilder.append("            or bi.`package_type` = pt.`description` ");
        queryBuilder.append("            or bi.`package_type` = pt.`abbr01` ");
        queryBuilder.append("            or bi.`package_type` = concat(pt.`type`, pt.`plural`)");
        queryBuilder.append("          )");
        queryBuilder.append("        )");
        queryBuilder.append("      )");
        queryBuilder.append("    )");
        queryBuilder.append("group by bi.`id`");
        SQLQuery query = getSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(EculineEdiBlBean.class));
        query.addScalar("id", BigIntegerType.INSTANCE);
        query.addScalar("blNo", StringType.INSTANCE);
        query.addScalar("invoiceNo", StringType.INSTANCE);
        query.addScalar("amsScac", StringType.INSTANCE);
        query.addScalar("arGlMapping", StringType.INSTANCE);
        query.addScalar("apGlMapping", StringType.INSTANCE);
        query.addScalar("chargesPostedStatus", StringType.INSTANCE);
        query.addScalar("invoiceStatus", BigIntegerType.INSTANCE);
        query.addScalar("shipper", StringType.INSTANCE);
        query.addScalar("shipperNo", StringType.INSTANCE);
        query.addScalar("consignee", StringType.INSTANCE);
        query.addScalar("consigneeNo", StringType.INSTANCE);
        query.addScalar("notify", StringType.INSTANCE);
        query.addScalar("notifyNo", StringType.INSTANCE);
        query.addScalar("pieces", BigDecimalType.INSTANCE);
        query.addScalar("weight", BigDecimalType.INSTANCE);
        query.addScalar("weightUnit", StringType.INSTANCE);
        query.addScalar("volume", BigDecimalType.INSTANCE);
        query.addScalar("volumeUnit", StringType.INSTANCE);
        query.addScalar("packType", StringType.INSTANCE);
        query.addScalar("packageId", StringType.INSTANCE);
        query.addScalar("shipTerms", StringType.INSTANCE);
        query.addScalar("expressRelease", StringType.INSTANCE);
        query.addScalar("approved", BooleanType.INSTANCE);
        query.addScalar("fileId", BigIntegerType.INSTANCE);
        query.addScalar("fileNo", StringType.INSTANCE);
        query.addScalar("por", StringType.INSTANCE);
        query.addScalar("pol", StringType.INSTANCE);
        query.addScalar("pod", StringType.INSTANCE);
        query.addScalar("delivery", StringType.INSTANCE);
        query.addScalar("origin", StringType.INSTANCE);
        query.addScalar("destn", StringType.INSTANCE);
        query.addScalar("destination", StringType.INSTANCE);
        query.addScalar("adjudicated", BooleanType.INSTANCE);
        query.addScalar("cntrId", StringType.INSTANCE);
        query.addScalar("unPolCount", StringType.INSTANCE);
        query.addScalar("unPodCount", StringType.INSTANCE);
        query.addScalar("unDelCount", StringType.INSTANCE);
        return query.list();
    }

    public BlInfo getBol(String id) throws Exception {
        return (BlInfo) getSession().createQuery("SELECT bl FROM BlInfo bl WHERE bl.id = :id ").setString("id", id).uniqueResult();
    }

    public String getEcuId(String field, String value) throws Exception { // fetch from bl_info
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT id");
        queryBuilder.append(" FROM eculine_edi WHERE id =");
        queryBuilder.append(" (SELECT eculine_edi_id");
        queryBuilder.append(" FROM container_info");
        queryBuilder.append(" WHERE id =");
        queryBuilder.append(" (SELECT container_info_id");
        queryBuilder.append(" FROM bl_info WHERE ").append(field).append(" = ").append(value).append(" limit 1))");
        return String.valueOf(getSession().createSQLQuery(queryBuilder.toString()).uniqueResult());
    }

    public String getContainerInfoId(String eculineEdiId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" select id from container_info where eculine_edi_id = '").append(eculineEdiId).append("'");
        return String.valueOf(getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult());
    }

    public List getScacHbl(String containerInfoId) throws Exception {
        List queryObject = new ArrayList();
        String queryString = "SELECT concat( 'DR# - ', l.file_number, ' already has this scac - ',b.ams_scac,' and ams# - ', b.ams_ref,'. Please enter another one.' ) ams FROM bl_info b JOIN lcl_booking_import_ams j ON j.`scac` =b.ams_scac JOIN lcl_file_number l ON l.id=j.file_number_id AND j.`ams_no` = REPLACE(b.ams_ref ,'/','') WHERE b.container_info_id ='" + containerInfoId + "'";
        queryObject = getSession().createSQLQuery(queryString).list();
        return queryObject;
    }

    public BigDecimal sum(Long unitId, String columnName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT SUM(cargo.").append(columnName).append(")");
        queryBuilder.append(" FROM cargo_details cargo");
        queryBuilder.append(" WHERE bl_info_id IN (");
        queryBuilder.append(" SELECT id FROM bl_info bl WHERE bl.container_info_id = ").append(unitId).append(")");
        return (BigDecimal) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public BigInteger countDr(Long unitId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT COUNT(id)");
        queryBuilder.append(" FROM bl_info bl");
        queryBuilder.append(" WHERE bl.container_info_id = ").append(unitId);
        return (BigInteger) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public Boolean checkAlreadyExistsVoyageInLcl(String voyNo, String cntrName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT if(COUNT(*), true, false) as result FROM lcl_ss_detail lsd ");
        queryBuilder.append("JOIN lcl_ss_header lsh ON lsd.ss_header_id = lsh.`id` ");
        queryBuilder.append("LEFT JOIN lcl_unit_ss lus ON lus.ss_header_id = lsh.`id` ");
        queryBuilder.append("LEFT JOIN lcl_unit lu ON lus.unit_id = lu.`id` ");
        queryBuilder.append("WHERE lu.`unit_no`= :unitNo AND lsd.`sp_reference_no` = :voyNo ");
        queryBuilder.append("AND lsh.status <> 'V' AND lsh.closed_by_user_id IS NULL AND lsh.audited_by_user_id IS NULL");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("unitNo", cntrName);
        query.setString("voyNo", voyNo);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public List<BlInfo> getBls(Long containerId) throws Exception {
        Query query = getCurrentSession().createQuery("FROM BlInfo WHERE containerInfo.id = " + containerId);
        return query.list();
    }

    public List<EculineediNotes> getNotesDetails(String voyNo, String containerNo, String blNo, String noteType) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("FROM EculineediNotes WHERE noteType='").append(noteType).append("' AND  ");
        if (voyNo != null && !"".equals(voyNo)) {
            queryBuilder.append(" referenceNumber='").append(voyNo.trim()).append("'");
        }
        if (containerNo != null && !"".equals(containerNo)) {
            queryBuilder.append(" OR  referenceNumber='").append(containerNo.trim()).append("'");
        }
        if (blNo != null && !"".equals(blNo)) {
            queryBuilder.append(" referenceNumber='").append(blNo.trim()).append("'");
        }
        queryBuilder.append("  ORDER BY id DESC");
        return (List<EculineediNotes>) getSession().createQuery(queryBuilder.toString()).list();
    }

    public List<EculineEdiCargoDetailsBean> getBolDetails(String blId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(getBolSelectQuery());
        queryBuilder.append(getBolFromQuery(blId));
        SQLQuery query = getSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(EculineEdiCargoDetailsBean.class));
        query.addScalar("blId", StringType.INSTANCE);
        query.addScalar("blNo", StringType.INSTANCE);
        query.addScalar("fileNumberId", StringType.INSTANCE);
        query.addScalar("shipperAcctName", StringType.INSTANCE);
        query.addScalar("consAcctName", StringType.INSTANCE);
        query.addScalar("notify1AcctName", StringType.INSTANCE);
        query.addScalar("notify2AcctName", StringType.INSTANCE);
        query.addScalar("shipperCode", StringType.INSTANCE);
        query.addScalar("manualShipper", StringType.INSTANCE);
        query.addScalar("consCode", StringType.INSTANCE);
        query.addScalar("manualCons", StringType.INSTANCE);
        query.addScalar("notify1Code", StringType.INSTANCE);
        query.addScalar("notify2Code", StringType.INSTANCE);
        query.addScalar("manualNotify1", StringType.INSTANCE);
        query.addScalar("shipperNad", StringType.INSTANCE);
        query.addScalar("consNad", StringType.INSTANCE);
        query.addScalar("notify1Nad", StringType.INSTANCE);
        query.addScalar("notify2Nad", StringType.INSTANCE);
        query.addScalar("preCarriageBy", StringType.INSTANCE);
        query.addScalar("placeOfReceipt", StringType.INSTANCE);
        query.addScalar("porUncode", StringType.INSTANCE);
        query.addScalar("polUncode", StringType.INSTANCE);
        query.addScalar("polDesc", StringType.INSTANCE);
        query.addScalar("podUncode", StringType.INSTANCE);
        query.addScalar("podDesc", StringType.INSTANCE);
        query.addScalar("poddeliveryUncode", StringType.INSTANCE);
        query.addScalar("poddeliveryDesc", StringType.INSTANCE);
        query.addScalar("approved", StringType.INSTANCE);
        query.addScalar("unitApproved", StringType.INSTANCE);
        query.addScalar("packageAmount", StringType.INSTANCE);
        query.addScalar("packageDesc", StringType.INSTANCE);
        query.addScalar("goodDesc", StringType.INSTANCE);
        query.addScalar("dischargeInstruction", StringType.INSTANCE);
        query.addScalar("weightValues", StringType.INSTANCE);
        query.addScalar("volumeValues", StringType.INSTANCE);
        query.addScalar("commercialValue", StringType.INSTANCE);
        query.addScalar("currency", StringType.INSTANCE);
        query.addScalar("marksNo", StringType.INSTANCE);
        query.addScalar("packageId", StringType.INSTANCE);
        query.addScalar("emShipperCode", StringType.INSTANCE);
        query.addScalar("emConsCode", StringType.INSTANCE);
        query.addScalar("emNotify1Code", StringType.INSTANCE);
        query.addScalar("emNotify2Code", StringType.INSTANCE);
        query.addScalar("noOfOriginal", StringType.INSTANCE);
        query.addScalar("noOfCopies", StringType.INSTANCE);
        return query.list();
    }

    private StringBuilder getBolSelectQuery() throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select bl.id AS blId, bl.bl_no AS blNo, bl.file_number_id AS fileNumberId,");
        queryBuilder.append(" CASE WHEN bl.shipper_code ='' OR bl.shipper_code IS NULL THEN etpmShip.trading_partner_acct_no  ELSE bl.shipper_code  END AS shipperCode,");
        queryBuilder.append(" CASE WHEN bl.shipper_code = '' OR bl.shipper_code IS NULL THEN caShip.acct_name ELSE blShip.acct_name  END AS shipperAcctName,");
        queryBuilder.append(" bl.manual_shipper AS manualShipper,");
        queryBuilder.append(" CASE WHEN bl.cons_code ='' OR bl.cons_code IS NULL THEN etpmCon.trading_partner_acct_no ELSE bl.cons_code  END AS consCode ,");
        queryBuilder.append(" CASE WHEN bl.cons_code = '' OR bl.cons_code IS NULL THEN caCon.acct_name ELSE blCon.acct_name  END AS consAcctName,");
        queryBuilder.append(" bl.manual_cons AS manualCons,");
        queryBuilder.append(" CASE WHEN bl.notify1_code ='' OR bl.notify1_code IS NULL THEN etpmNot1.trading_partner_acct_no ELSE bl.notify1_code END AS notify1Code,");
        queryBuilder.append(" CASE WHEN bl.notify1_code = '' OR bl.notify1_code IS NULL THEN caNot1.acct_name ELSE blNot1.acct_name  END AS notify1AcctName,");
        queryBuilder.append(" bl.manual_notify1 AS manualNotify1,");
        queryBuilder.append(" CASE WHEN bl.notify2_code ='' OR bl.notify2_code IS NULL THEN etpmNot2.trading_partner_acct_no ELSE bl.notify2_code END AS notify2Code ,");
        queryBuilder.append(" CASE WHEN bl.notify2_code = '' OR bl.notify2_code IS NULL THEN caNot2.acct_name ELSE blNot2.acct_name  END AS notify2AcctName,");
        queryBuilder.append(" bl.manual_notify2 AS manualNotify2,");
        queryBuilder.append(" CASE WHEN caShip.address1 IS NULL THEN bl.shipper_nad ELSE CONCAT (caShip.address1 ,', ',caShip.city1,', ', caShip.state  ,', ',COALESCE(cyShip.codedesc,'') ,', ',caShip.zip ) END AS shipperNad ,");
        queryBuilder.append(" CASE WHEN caCon.address1 IS NULL THEN bl.cons_nad ELSE CONCAT (caCon.address1 ,', ',caCon.city1,', ', caCon.state  ,', ',COALESCE(cyCon.codedesc,'') ,', ',caCon.zip ) END AS consNad ,");
        queryBuilder.append(" CASE WHEN caNot1.address1 IS NULL THEN bl.notify1_nad ELSE CONCAT (caNot1.address1 ,', ',caNot1.city1,', ', caNot1.state  ,', ',COALESCE(cyNot1.codedesc,'') ,', ',caNot1.zip ) END AS notify1Nad ,");
        queryBuilder.append(" CASE WHEN caNot2.address1 IS NULL THEN bl.notify2_nad ELSE CONCAT (caNot2.address1 ,', ',caNot2.city1,', ', caNot2.state  ,', ',COALESCE(cyNot2.codedesc,'') ,', ',caNot2.zip ) END AS notify2Nad ,");
        queryBuilder.append(" bl.precarriage_by AS preCarriageBy,");
        queryBuilder.append(" bl.place_of_receipt AS placeOfReceipt,");
        queryBuilder.append(" bl.por_uncode AS porUncode,");
        queryBuilder.append(" CASE WHEN bl.pol_uncode = '' OR bl.pol_uncode IS NULL THEN eumPOL.un_location_code ELSE bl.pol_uncode END AS polUncode,");
        queryBuilder.append(" CASE WHEN eumPOL.un_location_code IS NULL THEN bl.pol_desc ELSE CONCAT(UPPER(ulPOL.un_loc_name),'/',IFNULL(UPPER(pPOL.statecode), ''),'(',ulPOL.un_loc_code, ')' ) END AS polDesc,");
        queryBuilder.append(" CASE WHEN bl.pod_uncode = '' OR bl.pod_uncode IS NULL THEN eumPOD.un_location_code ELSE bl.pod_uncode END AS podUncode,");
        queryBuilder.append(" CASE WHEN eumPOD.un_location_code IS NULL THEN bl.pod_desc ELSE CONCAT(UPPER(ulPOD.un_loc_name),'/',IFNULL(UPPER(pPOD.statecode), ''),'(',ulPOD.un_loc_code, ')' ) END AS podDesc,");
        queryBuilder.append(" CASE WHEN bl.poddelivery_uncode = '' OR bl.poddelivery_uncode IS NULL THEN eumDelivery.un_location_code ELSE bl.poddelivery_uncode END AS poddeliveryUncode,");
        queryBuilder.append(" CASE WHEN eumDelivery.un_location_code IS NULL THEN bl.poddelivery_desc ELSE CONCAT(UPPER(ulDelivery.un_loc_name),'/',IFNULL(UPPER(pDelivery.statecode), ''),'(',ulDelivery.un_loc_code, ')' ) END AS poddeliveryDesc,");
        queryBuilder.append(" bl.is_approved AS approved,");
        queryBuilder.append(" ecu.is_approved unitApproved,");
        queryBuilder.append(" cargo.package_amount AS packageAmount,");
        queryBuilder.append(" CASE WHEN ePack.id IS NOT NULL THEN (IF(cargo.package_amount>1 ,CONCAT(ePack.abbr01,ePack.plural),ePack.abbr01)) WHEN pack.id IS NOT NULL THEN (IF( cargo.package_amount > 1,CONCAT(pack.abbr01, pack.plural),pack.abbr01 ))  ELSE cargo.package_desc END AS packageDesc,");
        queryBuilder.append(" cargo.good_desc AS goodDesc,");
        queryBuilder.append(" cargo.discharge_instruction AS dischargeInstruction,");
        queryBuilder.append(" cargo.weight_values AS weightValues,");
        queryBuilder.append(" cargo.volume_values AS volumeValues,");
        queryBuilder.append(" cargo.commercial_value AS commercialValue,");
        queryBuilder.append(" cargo.currency AS currency,");
        queryBuilder.append(" cargo.marks_no_desc marksNo,");
        queryBuilder.append(" CASE WHEN pack.id IS NULL THEN ePack.id ELSE pack.id END AS packageId,");
        queryBuilder.append(" etpmShip.trading_partner_acct_no AS emShipperCode,etpmCon.trading_partner_acct_no AS emConsCode,");
        queryBuilder.append(" etpmNot1.trading_partner_acct_no AS emNotify1Code,etpmNot2.trading_partner_acct_no AS emNotify2Code,");
        queryBuilder.append(" bl.no_docs_original AS noOfOriginal,");
        queryBuilder.append(" bl.no_docs_copies AS noOfCopies");
        return queryBuilder;
    }

    private StringBuilder getBolFromQuery(String blId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" from bl_info bl");
        queryBuilder.append(" join cargo_details cargo");
        queryBuilder.append(" on bl.id = ").append(blId);
        queryBuilder.append(" and bl.id = cargo.bl_info_id ");
        queryBuilder.append(" join container_info cntr");
        queryBuilder.append(" on bl.container_info_id = cntr.id");
        queryBuilder.append(" join eculine_edi ecu");
        queryBuilder.append(" on cntr.eculine_edi_id = ecu.id");
        queryBuilder.append(" left join package_type pack");
        queryBuilder.append(" on cargo.package_desc != ''");
        queryBuilder.append(" and (cargo.package_desc = concat(pack.type, pack.plural)");
        queryBuilder.append(" or cargo.package_desc = pack.type");
        queryBuilder.append(" or cargo.package_desc = pack.description");
        queryBuilder.append(" or cargo.package_desc = pack.abbr01)");
        queryBuilder.append(" LEFT JOIN `eculine_package_type_mapping` eptm");
        queryBuilder.append(" ON eptm.`package_desc` = cargo.package_desc");
        queryBuilder.append(" LEFT JOIN `package_type` ePack");
        queryBuilder.append(" ON ePack.`id` = eptm.`package_type_id`");
        queryBuilder.append(" LEFT JOIN trading_partner blShip ON bl.shipper_code = blShip.acct_no");
        queryBuilder.append(" LEFT JOIN trading_partner blCon ON bl.cons_code = blCon.acct_no");
        queryBuilder.append(" LEFT JOIN trading_partner blNot1 ON bl.notify1_code = blNot1.acct_no");
        queryBuilder.append(" LEFT JOIN trading_partner blNot2 ON bl.notify2_code = blNot2.acct_no");
        queryBuilder.append(" /* Mapping Shipping Address */");
        queryBuilder.append(" LEFT JOIN `eculine_trading_partner_mapping` etpmShip ON bl.shipper_nad = etpmShip.address ");
        queryBuilder.append(" LEFT JOIN cust_address caShip ON (etpmShip.trading_partner_acct_no = caShip.acct_no AND caShip.prime = 'on')");
        queryBuilder.append(" LEFT JOIN genericcode_dup cyShip ON caShip.country = cyShip.id ");
        queryBuilder.append(" /* Mapping Consignee Address */");
        queryBuilder.append(" LEFT JOIN `eculine_trading_partner_mapping` etpmCon ON bl.cons_nad = etpmCon.address");
        queryBuilder.append(" LEFT JOIN cust_address caCon ON (etpmCon.trading_partner_acct_no = caCon.acct_no AND caCon.prime = 'on')");
        queryBuilder.append(" LEFT JOIN genericcode_dup cyCon ON caCon.country = cyCon.id ");
        queryBuilder.append(" /* Mapping Notify1 Address */");
        queryBuilder.append(" LEFT JOIN `eculine_trading_partner_mapping` etpmNot1 ON bl.notify1_nad = etpmNot1.address ");
        queryBuilder.append(" LEFT JOIN cust_address caNot1 ON (etpmNot1.trading_partner_acct_no = caNot1.acct_no AND caNot1.prime = 'on')");
        queryBuilder.append(" LEFT JOIN genericcode_dup cyNot1 ON caNot1.country = cyNot1.id");
        queryBuilder.append(" /* Mapping Notify2 Address */");
        queryBuilder.append(" LEFT JOIN `eculine_trading_partner_mapping` etpmNot2 ON bl.notify2_nad = etpmNot2.address ");
        queryBuilder.append(" LEFT JOIN cust_address caNot2 ON ( etpmNot2.trading_partner_acct_no = caNot2.acct_no AND caNot2.prime = 'on')");
        queryBuilder.append("  /* Mapping POL Description */");
        queryBuilder.append(" LEFT JOIN `eculine_un_location_mapping` eumPOL ON bl.pol_desc = eumPOL.un_location_desc");
        queryBuilder.append(" LEFT JOIN `un_location` ulPOL ON  eumPOL.un_location_code = ulPOL.un_loc_code ");
        queryBuilder.append(" LEFT JOIN ports pPOL ON ulPOL.un_loc_code = pPOL.unlocationcode ");
        queryBuilder.append(" /* Mapping POD Description */");
        queryBuilder.append(" LEFT JOIN `eculine_un_location_mapping` eumPOD ON bl.pod_desc = eumPOD.un_location_desc ");
        queryBuilder.append(" LEFT JOIN `un_location` ulPOD ON  eumPOD.un_location_code = ulPOD.un_loc_code ");
        queryBuilder.append(" LEFT JOIN ports pPOD ON ulPOD.un_loc_code = pPOD.unlocationcode ");
        queryBuilder.append("  /* Mapping Delivery Description */");
        queryBuilder.append(" LEFT JOIN `eculine_un_location_mapping` eumDelivery ON bl.poddelivery_desc = eumDelivery.un_location_desc ");
        queryBuilder.append(" LEFT JOIN `un_location` ulDelivery ON  eumDelivery.un_location_code = ulDelivery.un_loc_code");
        queryBuilder.append(" LEFT JOIN ports pDelivery ON ulDelivery.un_loc_code = pDelivery.unlocationcode ");
        queryBuilder.append(" LEFT JOIN genericcode_dup cyNot2 ON caNot2.country = cyNot2.id");
        queryBuilder.append(" group by cargo.id");
        return queryBuilder;
    }

    public void adjudicate(String procedureName, Map<String, Object> values) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("call `").append(procedureName).append("` (");
        boolean isNotFirst = false;
        for (String fieldName : values.keySet()) {
            queryBuilder.append(" ").append(isNotFirst ? ", " : "").append(":").append(fieldName);
        }
        queryBuilder.append(")");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        for (String fieldName : values.keySet()) {
            Object value = values.get(fieldName);
            if (value instanceof String) {
                query.setString(fieldName, (String) value);
            } else if (value instanceof Integer) {
                query.setInteger(fieldName, (Integer) value);
            } else if (value instanceof Double) {
                query.setDouble(fieldName, (Double) value);
            } else if (value instanceof Long) {
                query.setLong(fieldName, (Long) value);
            } else if (value instanceof BigDecimal) {
                query.setBigDecimal(fieldName, (BigDecimal) value);
            } else if (value instanceof BigInteger) {
                query.setBigInteger(fieldName, (BigInteger) value);
            } else if (value instanceof Boolean) {
                query.setBoolean(fieldName, (Boolean) value);
            } else if (value instanceof Date) {
                query.setDate(fieldName, (Date) value);
            } else if (value instanceof Float) {
                query.setFloat(fieldName, (Float) value);
            }
        }
        query.executeUpdate();
        getCurrentSession().flush();
    }
}

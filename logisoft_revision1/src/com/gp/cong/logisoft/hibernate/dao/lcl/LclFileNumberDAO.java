/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Transaction;
import org.hibernate.type.BooleanType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

/**
 *
 * @author Owner
 */
                public class LclFileNumberDAO extends BaseHibernateDAO<LclFileNumber> {

    public static final Logger log = Logger.getLogger(LclFileNumberDAO.class);

    public LclFileNumberDAO() {
        super(LclFileNumber.class);
    }

//    public void addLclHsCode(HsCodeForm hsCodeForm) throws Exception {
//        String queryString;
//        String fileNo = "16";
//        String hscode = hsCodeForm.getHsCode();
//        String type = "HS code";
//        queryString = "insert into lcl_3p_ref_no (file_number_id, type, reference) values(" + fileNo + ",'" + type + "','" + hscode + "')";
//        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
//        query.executeUpdate();
//        getCurrentSession().flush();
//    }
//
//    public void addLclNcmNumber(NcmNumberForm ncmNumberForm) throws Exception {
//        String queryString;
//        String fileNo = "16";
//        String ncmNumber = ncmNumberForm.getNcmNumaber();
//        String type = "NCM Number";
//        queryString = "insert into lcl_3p_ref_no (file_number_id, type, reference) values(" + fileNo + ",'" + type + "','" + ncmNumber + "')";
//        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
//        query.executeUpdate();
//        getCurrentSession().flush();
//    }
//
//    public List<Lcl3pRefNo> getAllLclNcmNumberList(String fileNumber, String type) throws Exception {
//        Criteria criteria = getSession().createCriteria(Lcl3pRefNo.class, "NcmList");
//        criteria.createAlias("lclFileNumber", "lclFileNumber");
//        criteria.add(Restrictions.eq("lclFileNumber.id", Long.parseLong(fileNumber)));
//        criteria.add(Restrictions.eq("NcmList.type", type));
//        criteria.addOrder(Order.desc("id"));
//        return criteria.list();
//    }
//
//    public List<Lcl3pRefNo> getAllLclHsCodeList(String fileNumber, String type) throws Exception {
//        Criteria criteria = getSession().createCriteria(Lcl3pRefNo.class, "HscodeList");
//        criteria.createAlias("lclFileNumber", "lclFileNumber");
//        criteria.add(Restrictions.eq("lclFileNumber.id", Long.parseLong(fileNumber)));
//        criteria.add(Restrictions.eq("HscodeList.type", type));
//        criteria.addOrder(Order.desc("id"));
//        return criteria.list();
//    }
//    public void addLclNotes(LclRemarksForm lclNotesForm) throws Exception {
//        String fileNo = "16";
//        String type = "Notes";
//        String notes = lclNotesForm.getRemarks();
//        String cDate = "2012-01-06 16:49:35";
//        String user = "1";
//        String queryString = "insert into lcl_remarks (file_number_id, type, remarks, entered_datetime, "
//                + "entered_by_user_id, modified_datetime, modified_by_user_id) "
//                + "values(" + fileNo + ",'" + type + "','" + notes + "','" + cDate + "','" + user + "','" + cDate + "','" + user + "')";
//        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
//        query.executeUpdate();
//        getCurrentSession().flush();
//    }
    public Long getFileIdByFileNumber(String fileNumber) throws Exception {
        SQLQuery query = getCurrentSession().createSQLQuery("select id from lcl_file_number where file_number = :fileNumber");
        query.setString("fileNumber", fileNumber);
        query.addScalar("id", LongType.INSTANCE);
        query.setMaxResults(1);
        Long id = (Long) query.uniqueResult();
        return id != null ? id : 0L;
    }

    public String getFileNumberByFileId(String id) throws Exception {
        LclFileNumber lclFileNumber = (LclFileNumber) getCurrentSession().createQuery("from LclFileNumber where id=" + id + "").uniqueResult();
        return null != lclFileNumber ? lclFileNumber.getFileNumber() : null;
    }

    public String getBookingTypeByFileId(String fileNumber, String bookingType, String fileStatus) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT CONVERT(lfn.id USING utf8) as fileId FROM lcl_file_number lfn JOIN lcl_booking lb ON lfn.id=lb.file_number_id ");
        sb.append("WHERE lfn.file_number='").append(fileNumber).append("'").append(" AND lb.booking_type='").append(bookingType);
        sb.append("'").append(" AND lfn.short_ship!=1");
        if (CommonUtils.isNotEmpty(fileStatus)) {
            sb.append(" AND lfn.status IN (").append(fileStatus).append(")");
        }

        return (String) getSession().createSQLQuery(sb.toString()).uniqueResult();
    }

    public String[] getLabelPrintDataByFileNumber(Long fileId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  if(fd.`un_loc_code` not like 'US%' or b.booking_type = 'T', fd.`un_loc_code`, pod.`un_loc_code`) as destUnlocCode,");
        queryBuilder.append("  if(fd.`un_loc_code` not like 'US%' or b.booking_type = 'T', fd.`un_loc_name`, pod.`un_loc_name`) as destUnlocName,");
        queryBuilder.append("  if(fd.`un_loc_code` not like 'US%' or b.booking_type = 'T', fdc.`codedesc`, if(pod.`un_loc_code` like 'US%', pods.`code`, podc.`codedesc`)) as destCountry,");
        queryBuilder.append("  b.`booking_type` as bookingType,");
        //queryBuilder.append("  if(b.`booking_type` = 'E', b.billing_terminal, 13) as terminalNumber,");
        queryBuilder.append("  if(b.`booking_type` = 'E',(select un_loc_code from un_location where b.poo_id=id),");
        queryBuilder.append("  (select un_loc_code from un_location where b.pol_id=id)) as originUnLocCode,");
        queryBuilder.append("  f.`file_number` as fileNumber,");
        queryBuilder.append(" SUM(IF(p.actual_piece_count IS ");
        queryBuilder.append("NOT NULL AND p.actual_piece_count !=0,p.actual_piece_count,p.booked_piece_count)) AS pieceCount, ");
        queryBuilder.append("  f.`id` as fileNumberId,f.business_unit as businessUnit, ");
        queryBuilder.append("  IF(bx.`cfcl` AND b.`booking_type` = 'E', ");
        queryBuilder.append(" (SELECT acct_name FROM trading_partner WHERE acct_no = bx.cfcl_acct_no),'') AS cfclAcctName ");
        queryBuilder.append("from");
        queryBuilder.append("  `lcl_file_number` f ");
        queryBuilder.append("  join `lcl_booking` b ");
        queryBuilder.append("    on b.`file_number_id` = f.`id`");
        queryBuilder.append("  join `lcl_booking_piece` p ");
        queryBuilder.append("    on p.`file_number_id` = f.`id`");
        queryBuilder.append("  left join `un_location` pod ");
        queryBuilder.append("    on b.`pod_id` = pod.`id` ");
        queryBuilder.append("  left join `genericcode_dup` podc ");
        queryBuilder.append("    on pod.`countrycode`  = podc.`id`");
        queryBuilder.append("  left join `genericcode_dup` pods ");
        queryBuilder.append("    on pod.`statecode` = pods.`id` ");
        queryBuilder.append("  left join `un_location` fd ");
        queryBuilder.append("    on fd.`id` = b.`fd_id` ");
        queryBuilder.append("  left join `genericcode_dup` fdc ");
        queryBuilder.append("    on fd.`countrycode` = fdc.`id` ");
        queryBuilder.append("   LEFT JOIN lcl_booking_export bx  ");
        queryBuilder.append("    ON bx.`file_number_id`= f.`id`  ");
        queryBuilder.append("  where f.id = '").append(fileId).append("'");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("destUnlocCode", StringType.INSTANCE);
        query.addScalar("destUnlocName", StringType.INSTANCE);
        query.addScalar("destCountry", StringType.INSTANCE);
        query.addScalar("bookingType", StringType.INSTANCE);
        query.addScalar("originUnLocCode", StringType.INSTANCE);
        query.addScalar("fileNumber", StringType.INSTANCE);
        query.addScalar("pieceCount", StringType.INSTANCE);
        query.addScalar("fileNumberId", StringType.INSTANCE);
        query.addScalar("businessUnit", StringType.INSTANCE);
        query.addScalar("cfclAcctName", StringType.INSTANCE);
        Object result = query.uniqueResult();
        String[] data = new String[10];
        int index = 0;
        for (Object value : (Object[]) result) {
            data[index] = (String) value;
            index++;
        }
        return data;
    }

    public String deriveLclBlNoByFileId(String fileId) throws Exception {
        String query = "SELECT CONCAT(UPPER(RIGHT(org.un_loc_code, 3)), '-',dest.un_loc_code,'-',file.file_number) AS blNumber FROM lcl_file_number FILE JOIN lcl_bl bl ON file.id = bl.file_number_id JOIN un_location org ON org.id = bl.poo_id JOIN un_location dest ON dest.id = bl.fd_id WHERE file.id = " + fileId;
        return (String) getSession().createSQLQuery(query).uniqueResult();
    }

    public Object getStatusByField(String fieldName, String value) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select status from lcl_file_number where ").append(fieldName).append(" ='").append(value).append("'");
        SQLQuery query = getSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("status", StringType.INSTANCE);
        return (Object) getSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public Object[] getStateStatusByField(String fieldName, String value) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select CONVERT(f.state USING utf8),CONVERT(f.status USING utf8),b.pol_id,b.pod_id,bl.posted_by_user_id FROM lcl_file_number f LEFT JOIN ");
        queryBuilder.append("lcl_booking b ON f.id = b.file_number_id LEFT JOIN lcl_bl bl on f.id=bl.file_number_id where ").append(fieldName).append("='").append(value).append("'");
        return (Object[]) getSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public Object[] checkImpBkg(String fileNumber) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT lfn.id,lfn.state,lb.pol_id,lb.pod_id FROM lcl_file_number lfn JOIN lcl_booking lb ON lfn.id=lb.file_number_id ");
        queryBuilder.append("WHERE lfn.file_number='").append(fileNumber).append("'").append("AND lb.booking_type IN('I','T')");
        return (Object[]) getSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public Integer getFileCount(String fileNo) throws Exception {
        String query = "SELECT COUNT(*) FROM LclFileNumber WHERE fileNumber LIKE '%" + fileNo + "'" + " AND short_ship != 1";
        return ((Long) getSession().createQuery(query).uniqueResult()).intValue();
    }

    public Integer getFileCount(Long unitSsId, String fileStatus, Boolean isEqual) throws Exception {
        StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT(*) FROM lcl_booking_piece_unit bpu JOIN lcl_booking_piece bp ON bpu.booking_piece_id = bp.id ");
        query.append(" JOIN lcl_file_number fn ON bp.file_number_id=fn.id JOIN lcl_booking lb ON lb.file_number_id = fn.id WHERE fn.status");
        if (isEqual) {
            query.append("=");
        } else {
            query.append("!=");
        }
        query.append("'").append(fileStatus).append("' and bpu.lcl_unit_ss_id = ");
        query.append(unitSsId).append(" AND lb.booking_type='I'");
        return ((BigInteger) getSession().createSQLQuery(query.toString()).uniqueResult()).intValue();
    }

    public LclFileNumber getLclFileNumber(String fileNo) throws Exception {
        String query = "from LclFileNumber where fileNumber = '" + fileNo + "'";
        return (LclFileNumber) getSession().createQuery(query).uniqueResult();
    }

    public void updateLclFileNumbersStatus(String fileId, String status) throws Exception {
        StringBuilder queryString = new StringBuilder();
        queryString.append("update lcl_file_number set status='").append(status).append("' where id IN (").append(fileId).append(")");
        SQLQuery query = getCurrentSession().createSQLQuery(queryString.toString());
        query.executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public void updateFileStatus(Long fileId, String status) throws Exception {
        StringBuilder queryString = new StringBuilder();
        queryString.append("update lcl_file_number set status=:status where id=:fileId");
        SQLQuery query = getCurrentSession().createSQLQuery(queryString.toString());
        query.setParameter("status", status);
        query.setParameter("fileId", fileId);
        query.executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public List<Object[]> getAllFileIdsByUnitSSId(Long unitSSId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT f.id,f.file_number,SUM(IF(p.actual_volume_metric IS NOT NULL AND p.actual_volume_metric != 0.000,");
        queryBuilder.append("p.actual_volume_metric,p.booked_volume_metric)) AS total_volume_metric,unfd.un_loc_code AS finalDestination ,lb.`bill_to_party` FROM ");
        queryBuilder.append("lcl_booking_piece_unit u JOIN lcl_booking_piece p ON u.booking_piece_id = p.id ");
        queryBuilder.append("JOIN lcl_file_number f ON p.file_number_id = f.id JOIN lcl_booking lb ON lb.file_number_id = f.id JOIN un_location unfd ON ");
        queryBuilder.append("unfd.id = lb.fd_id WHERE u.lcl_unit_ss_id = ");
        queryBuilder.append(unitSSId);
        queryBuilder.append(" GROUP BY f.id DESC");
        Query queryObject = getSession().createSQLQuery(queryBuilder.toString());
        return (List<Object[]>) queryObject.list();
    }

    public LclFileNumber getLclFileNumberShortShipment(String fileNo, Integer shortShipmentSequence) throws Exception {
        String query = "from LclFileNumber where fileNumber = '" + fileNo + "' and short_ship_sequence = " + shortShipmentSequence;
        return (LclFileNumber) getSession().createQuery(query).uniqueResult();
    }

    public Integer getMaxShortShipSequence(String fileNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT MAX(short_ship_sequence) FROM lcl_file_number WHERE file_number='");
        queryBuilder.append(fileNo);
        queryBuilder.append("'");
        Query queryObject = getSession().createSQLQuery(queryBuilder.toString());
        return (Integer) queryObject.uniqueResult();
    }

    public BigDecimal getTotalCbmCftByUnitSSId(Long unitSSId, String weightMeasure) throws Exception {
        BigDecimal total = new BigDecimal(0.00);
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT ");
        if (weightMeasure.equalsIgnoreCase("M")) {
            queryBuilder.append("SUM(IF(p.actual_volume_metric IS NOT NULL AND p.actual_volume_metric != 0.000,p.actual_volume_metric");
            queryBuilder.append(",p.booked_volume_metric)) AS total_volume_metric ");
        } else {
            queryBuilder.append("SUM(IF(p.actual_weight_metric IS NOT NULL AND p.actual_weight_metric != 0.000,p.actual_weight_metric");
            queryBuilder.append(",p.booked_weight_metric)) AS total_weight_metric ");
        }
        queryBuilder.append("FROM lcl_booking_piece_unit u JOIN lcl_booking_piece p ON ");
        queryBuilder.append("u.booking_piece_id = p.id JOIN lcl_file_number f ON p.file_number_id = f.id WHERE u.lcl_unit_ss_id = ");
        queryBuilder.append(unitSSId);
        Query queryObject = getSession().createSQLQuery(queryBuilder.toString());
        Object o = queryObject.uniqueResult();
        if (o != null) {
            total = (BigDecimal) o;
        }
        return total;
    }

    public void updateConsolidateBl(Long fileId, String state) throws Exception {
        StringBuilder query = new StringBuilder();
        query.append("update lcl_file_number set state = '");
        query.append(state).append("' where id in");
        query.append("(select lcl_file_number_id_a from lcl_consolidation where lcl_file_number_id_b =  ");
        query.append(" (SELECT lcl_file_number_id_b FROM lcl_consolidation WHERE lcl_file_number_id_a =");
        query.append(fileId).append(") )");
        getSession().createSQLQuery(query.toString()).executeUpdate();
    }

    public String getUnitSsId(final Long fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT lbpu.lcl_unit_ss_id as unitSsId FROM lcl_booking_piece lbp ");
        sb.append("JOIN lcl_booking_piece_unit lbpu ON lbpu.booking_piece_id=lbp.id ");
        sb.append(" WHERE lbp.file_number_id=").append(fileId).append(" LIMIT 1 ");
        return (String) getCurrentSession().createSQLQuery(sb.toString()).addScalar("unitSsId", StringType.INSTANCE).uniqueResult();
    }

    public void updateFileNumberState(String fileId, String status) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update lcl_file_number set state  = '").append(status);
        queryBuilder.append("' where id IN( ").append(fileId).append(")");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public void updateFileStatus(Long fileId, String state, String status) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" update lcl_file_number set ");
        queryStr.append("state= :state ");
        if (!"".equalsIgnoreCase(status)) {
            queryStr.append(" , status=:status ");
        }
        queryStr.append("  where id=:fileId");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setLong("fileId", fileId);
        query.setString("state", state);
        if (!"".equalsIgnoreCase(status)) {
            query.setString("status", status);
        }
        query.executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public LclFileNumber createFileNumber(String fileNumber, String state) throws Exception {
        Transaction transaction = getCurrentSession().getTransaction();
        if (!transaction.isActive()) {
            transaction.begin();
        }
        LclFileNumber lclFileNumber = new LclFileNumber();
        lclFileNumber.setFileNumber(fileNumber);
        lclFileNumber.setState(state);
        lclFileNumber.setStatus("B");
        lclFileNumber.setCreatedDatetime(new Date());
        save(lclFileNumber);
        transaction.commit();
        return lclFileNumber;
    }

    public void uploadBlueToLogi(Integer fdId) throws Exception {
        String queryStr = "CALL LCLBooking_blue_to_logiware(" + fdId + ")";
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr);
        query.executeUpdate();
    }

    public void deleteBlueToLogi(Integer fdId) throws Exception {
        String queryStr = "CALL DeleteLCLFileByDestination(" + fdId + ")";
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr);
        query.executeUpdate();
    }

    public String getFileState(String fileNumber) throws Exception {
        String strQuery = "Select state as fileState from lcl_file_number where file_number='" + fileNumber + "'";
        SQLQuery query = getCurrentSession().createSQLQuery(strQuery);
        return (String) query.addScalar("fileState", StringType.INSTANCE).setMaxResults(1).uniqueResult();
    }
    public Long getFileForTerminate(String fileNumber) throws Exception {
        String strQuery = "SELECT fn.id FROM lcl_file_number fn  JOIN lcl_booking b ON b.file_number_id=fn.id JOIN lcl_booking_export AS  bl "
                + "ON fn.id = bl.file_number_id WHERE fn.status NOT IN('X')  AND bl.released_datetime is null AND fn.file_number ='"+fileNumber+"'";
        SQLQuery query = getCurrentSession().createSQLQuery(strQuery);
        query.addScalar("id", LongType.INSTANCE);
        query.setMaxResults(1);
        Long id = (Long) query.uniqueResult();
        return id != null ? id : 0L;
    }

    public String fileStatus(Long fileId) throws Exception {
        String strQuery = "Select status as fileStatus from lcl_file_number where id=:fileId";
        SQLQuery query = getCurrentSession().createSQLQuery(strQuery);
        query.setLong("fileId", fileId);
        return (String) query.addScalar("fileStatus", StringType.INSTANCE).setMaxResults(1).uniqueResult();
    }

    public String getUnCodeForLabelPrint(Long fileNumberId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT IF(lb.booking_type = 'E' ,podPort.eciportcode,fpodPort.eciportcode) AS eciPortCode  ");
        sb.append("FROM lcl_file_number lfn ");
        sb.append("JOIN lcl_booking lb ON lb.`file_number_id` = lfn.`id` ");
        sb.append(" left JOIN lcl_booking_import lbi ON lbi.`file_number_id` = lfn.`id` ");
        sb.append("LEFT JOIN un_location podUn ON podUn.id = lb.pod_id ");
        sb.append("LEFT JOIN ports podPort ON (podPort.`unlocationcode` = podUn.`un_loc_code` AND podUn.un_loc_code NOT LIKE 'US%')");
        sb.append("LEFT JOIN un_location fpodUn ON fpodUn.id = lbi.`foreign_port_of_discharge_id` ");
        sb.append("LEFT JOIN ports fpodPort ON (fpodPort.`unlocationcode` = fpodUn.`un_loc_code` AND fpodUn.un_loc_code NOT LIKE 'US%')");
        sb.append("WHERE lfn.id = :fileNumberId");
        Query query = getCurrentSession().createSQLQuery(sb.toString());
        query.setLong("fileNumberId", fileNumberId);
        return query.uniqueResult() != null ? query.uniqueResult().toString() : "";
    }

    public void updateEconoEculine(String bussinessUnit, String fileId) throws Exception {
        SQLQuery query = getCurrentSession().createSQLQuery("update lcl_file_number set  business_unit =:bussinessUnit where id=:fileId");
        query.setString("bussinessUnit", bussinessUnit);
        query.setParameter("fileId", fileId);
        query.executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public String getFileStateById(String fileId) throws Exception { // for consolidation logic also for  short ship 
        String strQuery = "Select state as fileState from lcl_file_number where id='" + fileId + "'";
        SQLQuery query = getCurrentSession().createSQLQuery(strQuery);
        return (String) query.addScalar("fileState", StringType.INSTANCE).setMaxResults(1).uniqueResult();
    }

    public String getConcatedUnlocationById(Integer unlocationId) throws Exception {
        String queryStr = "select UnLocationGetNameStateCntryByID(:unlocationId) as unlocation";
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr);
        query.setInteger("unlocationId", unlocationId);
        query.addScalar("unlocation", StringType.INSTANCE);
        return (String) query.setMaxResults(1).uniqueResult();
    }

    public void updateFileIdByList(List fileId, String status) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" update lcl_file_number set status  =:status where id IN(:fileIds)");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameterList("fileIds", fileId);
        query.setParameter("status", status);
        query.executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public String getBusinessUnit(String fileId) throws Exception {
        String strQuery = "Select business_unit as businessUnit from lcl_file_number where id='" + fileId + "'";
        SQLQuery query = getCurrentSession().createSQLQuery(strQuery);
        return (String) query.addScalar("businessUnit", StringType.INSTANCE).setMaxResults(1).uniqueResult();
    }

    public String getBusinessUnitInImport(String fileId) throws Exception {
        String strQuery = "SELECT IF(business_unit='Ecu Worldwide','ECU',IF(business_unit='Econo','ECI',business_unit)) AS businessUnit FROM lcl_file_number WHERE id =:fileId";
        SQLQuery query = getCurrentSession().createSQLQuery(strQuery);
        query.setParameter("fileId", fileId);
        return (String) query.addScalar("businessUnit", StringType.INSTANCE).setMaxResults(1).uniqueResult();
    }

    public void updatePreviousStatus(String fileId) throws Exception {
        StringBuilder queryString = new StringBuilder();
        queryString.append("update lcl_file_number set status=previous_status where id =:fileId");
        SQLQuery query = getCurrentSession().createSQLQuery(queryString.toString());
        query.setParameter("fileId", fileId);
        query.executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public String getFileStatus(Long fileId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT CASE WHEN (lfn.status ='L' or lfn.status ='M') then  lfn.status ");
        queryStr.append(" WHEN lfn.status <> 'B' and lfn.eliteCode <> 'RUNV' and e.released_datetime IS NOT NULL THEN 'R'    ");
        queryStr.append(" WHEN lfn.eliteCode='RCVD' THEN 'WV' WHEN lfn.eliteCode='RUNV' THEN 'WU'  ");
        queryStr.append(" when lfn.status='W' then 'WV' else lfn.status END AS STATUS  ");
        queryStr.append(" FROM (SELECT f.id AS fileId,f.status AS STATUS,  ");
        queryStr.append(" (SELECT d.elite_code FROM lcl_booking_dispo bd   ");
        queryStr.append(" JOIN disposition d ON d.id = bd.`disposition_id`   ");
        queryStr.append(" WHERE bd.file_number_id = f.id ORDER BY bd.id DESC   ");
        queryStr.append(" LIMIT 1) AS eliteCode FROM lcl_file_number f WHERE id = :fileId) lfn   ");
        queryStr.append(" LEFT JOIN lcl_booking_export e ON e.`file_number_id` = lfn.fileId   ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setLong("fileId", fileId);
        String fileStatus = (String) query.setMaxResults(1).uniqueResult();
        return null != fileStatus ? fileStatus : "B";
    }

    public Long getFileId(String fileNumber) throws Exception {
        String strQuery = "Select id as fileId from lcl_file_number where file_number=:fileNumber and short_ship=:shortShip";
        SQLQuery query = getCurrentSession().createSQLQuery(strQuery);
        query.setString("fileNumber", fileNumber);
        query.setBoolean("shortShip", false);
        return (Long) query.addScalar("fileId", LongType.INSTANCE).setMaxResults(1).uniqueResult();
    }

    public String[] getFileNo(String fileNumber) throws Exception {
        String strQuery = "Select state,status from lcl_file_number where file_number=:fileNumber and short_ship=:shortShip";
        SQLQuery query = getCurrentSession().createSQLQuery(strQuery);
        query.setString("fileNumber", fileNumber);
        query.setBoolean("shortShip", false);
        Object result = query.uniqueResult();
        String[] data = new String[2];
        int index = 0;
        for (Object value : (Object[]) result) {
            data[index] = (String) value;
            index++;
        }
        return data;
    }

    public boolean isImportFile(String fileNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  coalesce(");
        queryBuilder.append("    (select b.`booking_type` = 'I' from `lcl_booking` b where b.`file_number_id` = f.`id`),");
        queryBuilder.append("    (select q.`quote_type` = 'I' from `lcl_quote` q where q.`file_number_id` = f.`id`),");
        queryBuilder.append("    false");
        queryBuilder.append("  ) isImportFile ");
        queryBuilder.append("from");
        queryBuilder.append("  `lcl_file_number` f ");
        queryBuilder.append("where f.`file_number` = :fileNo");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("fileNo", fileNo);
        query.addScalar("isImportFile", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }


    public void updateNewFileNumber(String oldFileNo, String newFileNo) throws Exception {
        SQLQuery query = getCurrentSession().createSQLQuery("update lcl_file_number set file_number =:newFileNo where file_number=:oldFileNo");
        query.setParameter("newFileNo", newFileNo);
        query.setParameter("oldFileNo", oldFileNo);
        query.executeUpdate();
    }

    public List<Long> getFileIDIncudeShortShip(String fileNumber) throws Exception {
        SQLQuery query = getCurrentSession().createSQLQuery("select id from lcl_file_number where file_number=:fileNumber");
        query.setParameter("fileNumber", fileNumber);
        List fileIdList = query.list();
        List<Long> result = new ArrayList<>();
        for (Object obj : fileIdList) {
            result.add(Long.parseLong(obj.toString()));
        }
        return result;
    }
    
    public String getFileNumberId(List fileNumbers) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT GROUP_CONCAT(id) AS fileNumberId FROM `lcl_file_number` ");
        sb.append("  WHERE file_number IN(").append(":fileNumbers").append(")");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameterList("fileNumbers", fileNumbers);
        return null != queryObject.uniqueResult() ? (String) queryObject.uniqueResult() : "";
    }
    
    public String getCurrentFileNo(String fileId) throws Exception {
        String query = "SELECT file_number AS fileNo FROM lcl_file_number WHERE id =:fileId LIMIT 1";
        SQLQuery queryObj = getCurrentSession().createSQLQuery(query);
        queryObj.setParameter("fileId", fileId);
        queryObj.addScalar("fileNo", StringType.INSTANCE);
        return (String) queryObj.uniqueResult();
    }
}

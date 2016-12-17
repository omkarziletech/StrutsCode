/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.lcl.model.LclBookingModel;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclConsolidate;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.lcl.model.LclConsolidateBean;
import com.gp.cvst.logisoft.struts.form.lcl.LclConsolidateForm;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

/**
 * e
 *
 * @author lakshh
 */
public class LclConsolidateDAO extends BaseHibernateDAO<LclConsolidate> {

    public LclConsolidateDAO() {
        super(LclConsolidate.class);
    }

    public LclConsolidate findByFileAandFileB(Long fileA, Long FileB) throws Exception {
        LclConsolidate lclConsolidate = null;
        String queryString = "from LclConsolidate where lclFileNumberA=" + fileA + " AND lclFileNumberB=" + FileB;
        lclConsolidate = (LclConsolidate) getCurrentSession().createQuery(queryString).uniqueResult();
        return lclConsolidate;
    }

    public List<Object> getAllFileNumbersById(Long fileId) throws Exception {
        String queryString = "SELECT f.file_number FROM ((SELECT b.file_number FROM lcl_consolidation cons JOIN lcl_file_number b ON "
                + "cons.lcl_file_number_id_b = b.id WHERE cons.lcl_file_number_id_a = " + fileId + ") UNION (SELECT a.file_number "
                + "FROM lcl_consolidation cons JOIN lcl_file_number a ON cons.lcl_file_number_id_a = a.id WHERE "
                + "cons.lcl_file_number_id_b = " + fileId + ")) f ORDER BY f.file_number DESC";
        Query queryObject = getSession().createSQLQuery(queryString);
        return queryObject.list();
    }

    public List<Object> getAllFileIdsById(String fileId) throws Exception {
        String queryString = "SELECT f.id FROM ((SELECT b.id FROM lcl_consolidation cons JOIN lcl_file_number b ON "
                + "cons.lcl_file_number_id_b = b.id WHERE cons.lcl_file_number_id_a = " + fileId + ") UNION (SELECT a.id "
                + "FROM lcl_consolidation cons JOIN lcl_file_number a ON cons.lcl_file_number_id_a = a.id WHERE "
                + "cons.lcl_file_number_id_b = " + fileId + ")) f ORDER BY f.id DESC";
        Query queryObject = getSession().createSQLQuery(queryString);
        return queryObject.list();
    }

    public List<Long> getAllFileIds(Long fileId) throws Exception {
        String queryString = "SELECT lcl_file_number_id_a  FROM lcl_consolidation WHERE lcl_file_number_id_b = " + fileId
                + " UNION SELECT lcl_file_number_id_b  FROM lcl_consolidation WHERE lcl_file_number_id_a =  " + fileId;
        Query queryObject = getSession().createSQLQuery(queryString);
        List l = queryObject.list();
        List<Long> fileIdBList = new ArrayList<Long>();
        for (Object obj : l) {
            fileIdBList.add(Long.parseLong(obj.toString()));
        }
        return fileIdBList;
    }

    public List<Object[]> getAllFileIdAndNumbers(Long fileId) throws Exception {
        String queryString = "SELECT c.lcl_file_number_id_a,f.file_number FROM lcl_consolidation c JOIN lcl_file_number f ON "
                + "f.id=c.lcl_file_number_id_a WHERE lcl_file_number_id_b =  " + fileId + " UNION SELECT c.lcl_file_number_id_b,f.file_number "
                + "FROM lcl_consolidation c JOIN lcl_file_number f ON f.id = c.lcl_file_number_id_b WHERE lcl_file_number_id_a = " + fileId;
        Query queryObject = getSession().createSQLQuery(queryString);
        return queryObject.list();
    }

    public int deleteAllConsolidatedFiles(Long fileId) throws Exception {
        String queryString = "delete from  lcl_consolidation WHERE lcl_file_number_id_b = " + fileId + " or lcl_file_number_id_a = " + fileId;
        Query queryObject = getSession().createSQLQuery(queryString);
        int deletedRows = queryObject.executeUpdate();
        return deletedRows;
    }

    public List<LclConsolidate> getLclCostByChargeCode(Long fileId, String status) throws Exception {
        Criteria criteria = getSession().createCriteria(LclConsolidate.class, "lclConsolidate");
        criteria.createAlias("lclConsolidate.lclFileNumberB", "lclfilenumberB");
        criteria.createAlias("lclConsolidate.lclFileNumberA", "lclfilenumberA");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclfilenumberA.id", fileId));
            criteria.add(Restrictions.ne("lclfilenumberB.id", fileId));
        }
        if (!CommonUtils.isEmpty(status)) {
            criteria.add(Restrictions.ne("lclfilenumberB.status", status));
        }
        return criteria.list();
    }

    public boolean isConsolidated(Long file) throws Exception {
        String queryString = "select if(count(*)>0,true,false) as result from lcl_consolidation where lcl_file_number_id_a=:fileId AND lcl_file_number_id_b <> :fileId";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setLong("fileId", file);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public boolean isConsolidatedByFileAB(Long file) throws Exception {
        Integer count = 0;
        String queryString = "select count(*) as count from lcl_consolidation where lcl_file_number_id_a=" + file + " OR lcl_file_number_id_b=" + file;
        count = (Integer) getCurrentSession().createSQLQuery(queryString).addScalar("count", IntegerType.INSTANCE).uniqueResult();
        if (count > 0) {
            return true;
        }
        return false;
    }

    public Integer getConsolidateCountByFileAFileB(Long fileIdA, Long fileIdB) throws Exception {
        BigInteger count = new BigInteger("0");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT COUNT(*) FROM lcl_consolidation WHERE (lcl_file_number_id_a = ").append(fileIdA).append(" AND lcl_file_number_id_b = ").append(fileIdB).append(") OR (lcl_file_number_id_b = ").append(fileIdA).append(" AND lcl_file_number_id_a = ").append(fileIdB).append(")");
        Query queryObject = getSession().createSQLQuery(queryBuilder.toString());
        Object o = queryObject.uniqueResult();
        if (o != null) {
            count = (BigInteger) o;
        }
        return count.intValue();
    }

    public List getConsolidatesFiles(Long fileID) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT lcl_file_number_id_a AS fileId   FROM lcl_consolidation   WHERE `lcl_file_number_id_b` = ");
        queryBuilder.append(" (SELECT lcl_file_number_id_b FROM lcl_consolidation WHERE lcl_file_number_id_a=:fileID)    ");
        queryBuilder.append(" and lcl_file_number_id_a  <>:fileID  ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setLong("fileID", fileID);
        List result = query.list();
        List<Long> resultList = new ArrayList<Long>();
        if (CommonUtils.isNotEmpty(result)) {
            for (Object obj : result) {
                resultList.add(Long.parseLong(obj.toString()));
            }
        }
        return resultList;
    }

    public List getUnTerminateConsolidatesFiles(Long fileID) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT con.lcl_file_number_id_a AS fileId   FROM lcl_consolidation con");
        queryBuilder.append(" JOIN lcl_file_number file ON file.id = con.lcl_file_number_id_a ");
        queryBuilder.append(" WHERE con.lcl_file_number_id_b = (SELECT lcl_file_number_id_b FROM lcl_consolidation WHERE lcl_file_number_id_a=:fileID)");
        queryBuilder.append(" and file.status  != 'X' ");
        queryBuilder.append(" and con.lcl_file_number_id_a  <>:fileID  ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setLong("fileID", fileID);
        List result = query.list();
        List<Long> resultList = new ArrayList<Long>();
        if (CommonUtils.isNotEmpty(result)) {
            for (Object obj : result) {
                resultList.add(Long.parseLong(obj.toString()));
            }
        }
        return resultList;
    }

    public String[] getReleaseORPreReleaseConsolidate(String fileId, String buttonValue, String isRelease) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        String data[] = new String[2];
        queryBuilder.append(" SELECT group_concat(file.id) AS fileId,group_concat(file.file_number) AS fileNumber ");
        queryBuilder.append(" FROM lcl_consolidation con ");
        queryBuilder.append(" JOIN lcl_booking_export export ON con.lcl_file_number_id_a =  export.file_number_id ");
        queryBuilder.append(" JOIN lcl_file_number FILE ON file.id = con.lcl_file_number_id_a ");
        queryBuilder.append(" WHERE con.lcl_file_number_id_b = (SELECT lcl_file_number_id_b FROM lcl_consolidation WHERE lcl_file_number_id_a=:fileId) ");
        queryBuilder.append(" and lcl_file_number_id_a  <>:fileId  ");
        if (isRelease.equalsIgnoreCase("true")) {
            if (buttonValue.equalsIgnoreCase("R")) {
                queryBuilder.append(" AND export.released_datetime IS NULL  ");
            } else if (buttonValue.equalsIgnoreCase("PR")) {
                queryBuilder.append(" AND export.prerelease_datetime IS NULL  ");
            }
        } else {
            if (buttonValue.equalsIgnoreCase("R")) {
                queryBuilder.append(" AND export.released_datetime IS not NULL  ");
            } else if (buttonValue.equalsIgnoreCase("PR")) {
                queryBuilder.append(" AND export.prerelease_datetime IS not NULL  ");
            }
        }
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameter("fileId", fileId);
        List result = query.list();
        if (CommonUtils.isNotEmpty(result)) {
            for (Object obj : result) {
                Object[] row = (Object[]) obj;
                data[0] = null != row[0] ? row[0].toString() : "";
                data[1] = null != row[1] ? row[1].toString() : "";
            }
        }
        return data;
    }   

    public String getConsolidatesFileNumbers(String fileID) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT group_concat(lf.file_number) AS fileNumbers   FROM lcl_consolidation lc join lcl_file_number lf on lf.id = lc.lcl_file_number_id_a ");
        queryBuilder.append("   WHERE lc.`lcl_file_number_id_b` = ");
        queryBuilder.append(" (SELECT lcl_file_number_id_b FROM lcl_consolidation WHERE lcl_file_number_id_a=:fileID)    ");
        queryBuilder.append(" and lc.lcl_file_number_id_a  <>:fileID  ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameter("fileID", fileID);
        return (String) query.uniqueResult();
    }

    public void insertLCLConsolidation(Long ida, Long idb, User user, Date d) throws Exception {
        LclConsolidate lclConsolidate = new LclConsolidate();
        lclConsolidate.setLclFileNumberA(new LclFileNumber(ida));
        lclConsolidate.setLclFileNumberB(new LclFileNumber(idb));
        lclConsolidate.setEnteredBy(user);
        lclConsolidate.setModifiedBy(user);
        lclConsolidate.setEnteredDatetime(d);
        lclConsolidate.setModifiedDatetime(d);
        this.save(lclConsolidate);
    }

    public String getConsolidatesFilesComma(Long fileID) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT group_concat(lcl_file_number_id_b) AS fileId   FROM lcl_consolidation WHERE `lcl_file_number_id_a` =:fileID ");
        sb.append(" UNION SELECT lcl_file_number_id_a AS fileId   FROM lcl_consolidation WHERE `lcl_file_number_id_b` =:fileID ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("fileID", fileID);
        Object result = query.uniqueResult();
        return null != result ? result.toString() : "";
    }

    public List<LclBookingModel> getConsolidateBkgList(List fileId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("  SELECT lfn.id AS fileId,  ");
        queryBuilder.append("  lfn.file_number AS fileNo,  ");
        queryBuilder.append("  lfn.state AS fileState,  ");
        queryBuilder.append("  lc.lcl_file_number_id_b AS consolidateFile,  ");
        queryBuilder.append("  CASE WHEN lfn.status = 'M'  THEN lfn.previous_status ");
        queryBuilder.append("  WHEN lfn.status = 'L'  THEN lfn.status  WHEN lbe.released_datetime IS NOT NULL  THEN 'R'  ");
        queryBuilder.append(" WHEN lbe.prerelease_datetime IS NOT NULL THEN 'PR'  ELSE lfn.status   END AS  fileStatus,   ");
        queryBuilder.append("  UnLocationGetNameStateCntryByID(lb.poo_id) AS pooName,  ");
        queryBuilder.append("  (SELECT lc.company_name FROM lcl_contact lc WHERE lc.id=lb.client_contact_id) AS clientName,  ");
        queryBuilder.append("  lb.client_acct_no AS clientAcctNo,  ");
        queryBuilder.append("  (SELECT DATE_FORMAT(STD,'%d-%b-%Y') FROM lcl_ss_detail ");
        queryBuilder.append("  WHERE ss_header_id =lb.booked_ss_header_id AND trans_mode='V') AS bkgSailDate,  ");
        queryBuilder.append("  COALESCE(SUM(lbp.actual_piece_count),SUM(lbp.booked_piece_count)) AS piece,");
        queryBuilder.append(" COALESCE(SUM(lbp.actual_weight_imperial), SUM(lbp.booked_weight_imperial)) AS weightImpLbs,  ");
        queryBuilder.append(" COALESCE(SUM(lbp.actual_volume_imperial),SUM(lbp.booked_volume_imperial)) AS volumeImpcft,  ");
        queryBuilder.append(" COALESCE(SUM(lbp.actual_volume_metric),SUM(lbp.booked_volume_metric)) AS volumeMetricCbm,  ");
        queryBuilder.append(" COALESCE(SUM(lbp.actual_weight_metric),SUM(lbp.booked_weight_metric)) AS weightMetricKgs,  ");
        queryBuilder.append("   (SELECT d.elite_code FROM lcl_booking_dispo lbd JOIN disposition d ON   ");
        queryBuilder.append(" d.id=lbd.disposition_id WHERE lbd.file_number_id=lfn.id ORDER BY lbd.id DESC LIMIT 1) AS dispoStatus   ");
        queryBuilder.append("  FROM lcl_file_number lfn   ");
        queryBuilder.append("  JOIN lcl_booking lb ON lb.file_number_id=lfn.id  ");
        queryBuilder.append("  JOIN lcl_booking_piece lbp ON lbp.file_number_id=lfn.id  ");
        queryBuilder.append("   JOIN lcl_consolidation lc ON lc.lcl_file_number_id_a = lfn.id   ");
        queryBuilder.append("  LEFT JOIN lcl_booking_export lbe  ON  lbe.`file_number_id` = lfn.id  ");
        queryBuilder.append(" WHERE lfn.id IN(:fileId) ");
        queryBuilder.append(" GROUP BY lbp.file_number_id  ");
        queryBuilder.append(" ORDER BY lfn.file_number DESC ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameterList("fileId", fileId);
        query.setResultTransformer(Transformers.aliasToBean(LclBookingModel.class));
        query.addScalar("fileId", LongType.INSTANCE);
        query.addScalar("fileNo", StringType.INSTANCE);
        query.addScalar("fileStatus", StringType.INSTANCE);
        query.addScalar("bkgSailDate", StringType.INSTANCE);
        query.addScalar("pooName", StringType.INSTANCE);
        query.addScalar("clientName", StringType.INSTANCE);
        query.addScalar("clientAcctNo", StringType.INSTANCE);
        query.addScalar("dispoStatus", StringType.INSTANCE);
        query.addScalar("piece", IntegerType.INSTANCE);
        query.addScalar("weightImpLbs", BigDecimalType.INSTANCE);
        query.addScalar("volumeImpcft", BigDecimalType.INSTANCE);
        query.addScalar("volumeMetricCbm", BigDecimalType.INSTANCE);
        query.addScalar("weightMetricKgs", BigDecimalType.INSTANCE);
        query.addScalar("consolidateFile", StringType.INSTANCE);
        query.addScalar("fileState", StringType.INSTANCE);
        return query.list();
    }

    public String commonQueryForConsolidation() {
        StringBuilder sb = new StringBuilder();
        sb.append(" (SELECT lc.`lcl_file_number_id_a`  AS id , IF(lc.lcl_file_number_id_a <> lc.`lcl_file_number_id_b`,lc.id ,'') AS consolidate_id  FROM lcl_consolidation lc ");
        sb.append("  WHERE lc.`lcl_file_number_id_a` = lc.`lcl_file_number_id_b` ");
        sb.append(" AND lc.lcl_file_number_id_a NOT IN (:currentFileId) AND lc.`lcl_file_number_id_a` NOT IN ( ");
        sb.append(" SELECT lc.`lcl_file_number_id_b`  FROM lcl_consolidation lc  ");
        sb.append(" WHERE lc.`lcl_file_number_id_a` <> lc.`lcl_file_number_id_b` )) f ");
        return sb.toString();
    }

    public List<LclConsolidateBean> getAllConsolidateFiles(Long currentFileId,
            LclConsolidateForm lclConsolidateForm) throws Exception {
        List<LclConsolidateBean> lclConsolidateBeanList = new ArrayList<LclConsolidateBean>();
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT lf.id,lf.file_number,UnLocationGetNameStateCntryByID (b.poo_id) AS poo, ");
        sb.append(" ccont.company_name AS client_name,scont.company_name AS shipper_name, ");
        sb.append(" fcont.company_name AS forwarder_name,conscont.company_name AS consignee_name, ");
        sb.append(" CASE WHEN lf.status = 'M' THEN lf.previous_status  ");
        sb.append(" WHEN (lf.status = 'L' or lf.status='X') THEN lf.status  WHEN lbe.released_datetime IS NOT NULL  THEN 'R' ");
        sb.append(" ELSE lf.`status`   END AS  status, ");
        sb.append(" (SELECT STD FROM  lcl_ss_detail  WHERE ls.id = ss_header_id  AND trans_mode = 'V') AS pol_etd, piece.total_piece_count, ");
        sb.append(" piece.total_weight_imperial,piece.total_volume_imperial,f.consolidate_id, ");
        sb.append(" (select bp.file_number_id from lcl_booking_piece bp JOIN lcl_booking_piece_unit bpu ON bpu.booking_piece_id = bp.id ");
        sb.append(" JOIN lcl_unit_ss luss ON luss.id = bpu.lcl_unit_ss_id where bp.file_number_id = lf.id limit 1) as pickedId  ");
        sb.append("FROM ").append(commonQueryForConsolidation());
        sb.append("  JOIN lcl_file_number lf ON (lf.`id` = f.id AND lf.`state`not in ('Q','BL')  AND lf.`status` <> 'X') ");
        sb.append("  JOIN lcl_booking b ON (b.`file_number_id` = lf.id AND b.pod_id =:podId  ");
        sb.append("  AND b.fd_id =:fdId  AND b.pol_id =:polId AND b.booking_type <> 'I') ");
        sb.append("  left join lcl_booking_export lbe on lbe.file_number_id = lf.id ");
        sb.append("  LEFT JOIN lcl_contact ccont ON b.client_contact_id = ccont.id  ");
        sb.append("  LEFT JOIN lcl_contact scont ON b.ship_contact_id = scont.id ");
        sb.append("  LEFT JOIN lcl_contact fcont ON b.fwd_contact_id = fcont.id ");
        sb.append("  LEFT JOIN lcl_contact conscont ON b.cons_contact_id = conscont.id  ");
        sb.append("  LEFT JOIN lcl_ss_header ls ON b.booked_ss_header_id = ls.id   ");
        sb.append("  LEFT JOIN(SELECT p.file_number_id, SUM( IF( p.actual_piece_count IS NOT NULL ");
        sb.append("  AND p.actual_piece_count != 0,p.actual_piece_count, p.booked_piece_count)) AS total_piece_count, ");
        sb.append("  SUM(IF(p.actual_weight_imperial IS NOT NULL AND p.actual_weight_imperial != 0.000, ");
        sb.append("  p.actual_weight_imperial,p.booked_weight_imperial )) AS total_weight_imperial, ");
        sb.append("  SUM(IF( p.actual_volume_imperial IS NOT NULL AND p.actual_volume_imperial != 0.000, ");
        sb.append("  p.actual_volume_imperial,p.booked_volume_imperial)) AS total_volume_imperial ");
        sb.append("  FROM lcl_booking_piece p GROUP BY p.file_number_id )piece  ON  piece.file_number_id = lf.id having pickedId is null ");
        sb.append(getSortBy(lclConsolidateForm));
        Query queryObject = getSession().createSQLQuery(sb.toString());
        queryObject.setParameter("podId", lclConsolidateForm.getPodId());
        queryObject.setParameter("fdId", lclConsolidateForm.getFdId());
        queryObject.setParameter("polId", lclConsolidateForm.getPolId());
        queryObject.setParameter("currentFileId", currentFileId);
        List l = queryObject.list();
        for (Object obj : l) {
            Object[] row = (Object[]) obj;
            LclConsolidateBean model = new LclConsolidateBean(row);
            lclConsolidateBeanList.add(model);
        }
        return lclConsolidateBeanList;
    }

    private StringBuilder getSortBy(LclConsolidateForm lclConsolidateForm) {
        StringBuilder queryBuilder = new StringBuilder();
        if (CommonUtils.isNotEmpty(lclConsolidateForm.getSortByValue())) {
            if ("up".equals(lclConsolidateForm.getSearchType())) {
                queryBuilder.append(" order by ").append(lclConsolidateForm.getSortByValue()).append(" asc");
            } else {
                queryBuilder.append(" order by ").append(lclConsolidateForm.getSortByValue()).append(" desc");
            }
        } else {
            queryBuilder.append("   order by lf.file_number desc");
        }
        return queryBuilder;
    }

    public boolean isConsoildateFile(String fileNumberId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT count(*) as result   FROM lcl_consolidation   WHERE `lcl_file_number_id_b` = ");
        queryBuilder.append(" (SELECT lcl_file_number_id_b FROM lcl_consolidation WHERE lcl_file_number_id_a=:fileID )    ");
        queryBuilder.append(" and lcl_file_number_id_a  <>:fileID  ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameter("fileID", fileNumberId);
        return (Integer) query.addScalar("result", IntegerType.INSTANCE).uniqueResult() > 0;
    }

    public String[] getParentConsolidateFile(String fileId) throws Exception {
        String[] result = new String[2];
        StringBuilder sb = new StringBuilder();
        sb.append(" select lc.lcl_file_number_id_b,lf.file_number from lcl_consolidation lc join ");
        sb.append(" lcl_file_number lf on lf.id = lc.lcl_file_number_id_b where lcl_file_number_id_a=:fileId ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("fileId", fileId);
        List li = query.list();
        for (Object obj : li) {
            Object[] row = (Object[]) obj;
            result[0] = row[0].toString();
            result[1] = row[1].toString();
        }
        return result;
    }

    public void updateConsolidateFile(Long childId, Long parentId, String state) throws Exception {
        SQLQuery query = getCurrentSession()
                .createSQLQuery(" update lcl_consolidation set lcl_file_number_id_b =:parentId where lcl_file_number_id_a in(:childId) ");
        query.setParameter("parentId", parentId);
        query.setParameter("childId", childId);
        query.executeUpdate();
        if (state.equalsIgnoreCase("BL")) {
            query = getCurrentSession()
                    .createSQLQuery("update lcl_file_number set state =:state where id in(:childId) ");
            query.setParameter("childId", childId);
            query.setParameter("state", state);
            query.executeUpdate();
        }
    }

    public String getConsolitdateFileStatus(List fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT GROUP_CONCAT(dis.fileNo) FROM (SELECT fn.file_number AS fileNo, ");
        sb.append(" (SELECT dp.`elite_code` FROM lcl_booking_dispo d  ");
        sb.append("  JOIN disposition dp ON d.disposition_id = dp.id ");
        sb.append("  WHERE d.file_number_id = fn.id ORDER BY d.id DESC LIMIT 1) AS eliteCode  ");
        sb.append("  FROM lcl_booking bk JOIN `lcl_file_number` fn  ON (bk.`file_number_id` = fn.`id`)  ");
        sb.append("  WHERE bk.`booking_type` <> 'I' AND fn.id IN (:fileId)");
        sb.append("  HAVING ( eliteCode !='RCVD' AND eliteCode !='RUNV' ))  AS dis");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameterList("fileId", fileId);
        return (String) query.uniqueResult();
    }

    public List getReleasedConsolidatesFilesId(Long fileID) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT file.id AS fileId ");
        queryBuilder.append(" FROM lcl_consolidation con ");
        queryBuilder.append(" JOIN lcl_booking_export export ON con.lcl_file_number_id_a =  export.file_number_id ");
        queryBuilder.append(" JOIN lcl_file_number FILE ON file.id = con.lcl_file_number_id_a ");
        queryBuilder.append(" WHERE con.lcl_file_number_id_b = (SELECT lcl_file_number_id_b FROM lcl_consolidation WHERE lcl_file_number_id_a=:fileID)    ");
        queryBuilder.append(" and lcl_file_number_id_a  <>:fileID  ");
        queryBuilder.append(" AND export.released_datetime IS NULL  ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setLong("fileID", fileID);
        List result = query.list();
        List<String> resultList = new ArrayList<String>();
        if (CommonUtils.isNotEmpty(result)) {
            for (Object obj : result) {
                resultList.add(obj.toString());
            }
        }
        return resultList;
    }

    public boolean isReleased(Long fileID) throws Exception {
        String queryString = "select if(count(*)>0,false,true) as result FROM lcl_file_number fn  JOIN lcl_booking b ON b.file_number_id=fn.id JOIN lcl_booking_export AS  bl ON fn.id = bl.file_number_id WHERE  bl.released_datetime is null AND b.file_number_id=:fileId ";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setLong("fileId", fileID);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public Map<String, String> validateFilesToConsolidate(List<String> fileNumberList, Integer pol, Integer pod, Integer fd) throws Exception {
        StringBuilder sb = new StringBuilder();
        Map<String, String> result = new HashMap<String, String>();
        StringBuilder fileNumbers = new StringBuilder();
        StringBuilder errorMsg = new StringBuilder();
        sb.append(" SELECT ");
        sb.append("  GROUP_CONCAT(f.file_number),");
        sb.append("  CONCAT(\"The DR# \",\"<span style='color:red'>\",GROUP_CONCAT(f.file_number),\"</span>\",\" is already consolidated on a different parent DR. </br> \" ) AS error");
        sb.append(" FROM");
        sb.append("  lcl_file_number f ");
        sb.append("  JOIN lcl_consolidation c ");
        sb.append("    ON f.id = c.lcl_file_number_id_a ");
        sb.append(" WHERE f.file_number IN (:fileNumbers) ");
        sb.append("  AND c.lcl_file_number_id_a != c.lcl_file_number_id_b ");
        sb.append(" UNION");

        sb.append(" SELECT ");
        sb.append("  GROUP_CONCAT(f.file_number),");
        sb.append("  CONCAT(\"The DR# \",\"<span style='color:red'>\",GROUP_CONCAT(f.file_number),\"</span>\",\" POL/POD/FD DO NOT MATCH (i.e. they must MATCH). </br> \" ) AS error ");
        sb.append("    ");
        sb.append(" FROM");
        sb.append("  lcl_file_number f ");
        sb.append("  JOIN lcl_booking b ");
        sb.append("    ON f.id = b.file_number_id ");
        sb.append(" WHERE f.file_number IN (:fileNumbers) ");
        sb.append("  AND (");
        sb.append("    b.pol_id != :pol ");
        sb.append("    OR b.pod_id != :pod ");
        sb.append("    OR b.fd_id != :fd");
        sb.append("  ) ");
        sb.append(" UNION");

        sb.append(" SELECT ");
        sb.append("  GROUP_CONCAT(f.file_number),");
        sb.append("  CONCAT(\"The DR# \",\"<span style='color:red'>\",GROUP_CONCAT(f.file_number),\"</span>\",\" is a CFCL type DR. </br> \") AS error ");
        sb.append(" FROM");
        sb.append("  lcl_file_number f ");
        sb.append("  JOIN lcl_booking_export bx ");
        sb.append("    ON f.id = bx.file_number_id ");
        sb.append(" WHERE f.file_number IN (:fileNumbers) ");
        sb.append("  AND bx.cfcl = 1 ");
        sb.append(" UNION");

        sb.append(" SELECT ");
        sb.append("  GROUP_CONCAT(f.file_number),");
        sb.append("  CONCAT(\"The DR# \",\"<span style='color:red'>\",GROUP_CONCAT(f.file_number),\"</span>\",\" has been terminated. </br> \") AS error ");
        sb.append(" FROM");
        sb.append("  lcl_file_number f ");
        sb.append(" WHERE f.file_number IN (:fileNumbers) ");
        sb.append("  AND f.status = 'X' ");
        sb.append(" UNION");

        sb.append(" SELECT ");
        sb.append("  GROUP_CONCAT(f.file_number),");
        sb.append("  CONCAT(\"The DR# \",\"<span style='color:red'>\",GROUP_CONCAT(f.file_number),\"</span>\",\" has BL already. </br> \" ) AS error ");
        sb.append(" FROM");
        sb.append("  lcl_file_number f ");
        sb.append("  JOIN lcl_bl bl ");
        sb.append("    ON f.id = bl.file_number_id ");
        sb.append(" WHERE f.file_number IN (:fileNumbers)");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setInteger("pol", pol);
        query.setInteger("pod", pod);
        query.setInteger("fd", fd);
        query.setParameterList("fileNumbers", fileNumberList);
        List resultList = query.list();
        if (CommonUtils.isNotEmpty(resultList)) {
            for (Object obj : resultList) {
                Object[] row = (Object[]) obj;
                if (row[0] != null) {
                    fileNumbers.append(row[0].toString().replace("DR#", "DR#" + row[0].toString().toUpperCase())).append(",");
                    errorMsg.append(row[1].toString());
                }
            }
            result.put("fileNumbers", fileNumbers.toString().replaceAll(",$", ""));
            result.put("errorMsg", errorMsg.toString());
        }
        return result;
    }

    public boolean getConsolidateParentFileFlag(String fileId) throws Exception {
        String query = "SELECT IF(COUNT(*) > 0,TRUE,FALSE) AS flag FROM `lcl_consolidation`  WHERE lcl_file_number_id_b =:fileId";
        SQLQuery queryObject = getCurrentSession().createSQLQuery(query);
        queryObject.setParameter("fileId", fileId);
        queryObject.addScalar("flag", BooleanType.INSTANCE);
        return (boolean) queryObject.uniqueResult();
    }

    public String getConsFileNumbersAndFileId(String fileId) throws Exception {
        StringBuilder builder = new StringBuilder();
        if (isConsoildateFile(fileId) && getConsolidateParentFileFlag(fileId)) {
            builder.append("SELECT GROUP_CONCAT(DISTINCT (lf.file_number)) AS fileNumbers FROM  lcl_consolidation lc ");
            builder.append(" JOIN lcl_file_number lf ON lf.id = lc.lcl_file_number_id_a ");
            builder.append(" JOIN lcl_booking_piece lbp ON lbp.`file_number_id`=lf.`id`");
            builder.append(" WHERE lc.`lcl_file_number_id_b` = (SELECT lcl_file_number_id_b FROM lcl_consolidation ");
            builder.append(" WHERE lcl_file_number_id_a =:fileId) ");
            builder.append(" AND IF(lbp.`actual_piece_count` IS NOT NULL,((lbp.`actual_piece_count` = 0.00  OR lbp.actual_piece_count IS NULL ) ");
            builder.append(" OR (lbp.`actual_weight_imperial` = 0.00  OR lbp.actual_weight_imperial IS NULL) ");
            builder.append(" OR (lbp.`actual_volume_imperial` = 0.00  OR lbp.actual_volume_imperial IS NULL ) ");
            builder.append(" OR (lbp.`actual_volume_metric` = 0.00  OR lbp.actual_volume_metric IS NULL ) ");
            builder.append(" OR (lbp.`actual_weight_metric` = 0.00  OR lbp.actual_weight_metric IS NULL)),");
            builder.append(" (( lbp.`booked_piece_count` = 0.00 OR lbp.`booked_piece_count` IS NULL) ");
            builder.append(" OR (lbp.`booked_weight_imperial` = 0.00  OR lbp.`booked_weight_imperial` IS NULL ) ");
            builder.append(" OR (lbp.`booked_volume_imperial` = 0.00  OR lbp.booked_volume_imperial IS NULL) ");
            builder.append(" OR ( lbp.`booked_volume_metric` = 0.00 OR lbp.booked_volume_metric IS NULL) ");
            builder.append(" OR (lbp.`booked_weight_metric` = 0.00 OR lbp.booked_weight_metric IS NULL)))");

        } else {
            builder.append("SELECT GROUP_CONCAT(DISTINCT(lcf.`file_number`)) FROM lcl_booking_piece lbp ");
            builder.append("JOIN lcl_file_number lcf ON lcf.`id`=lbp.`file_number_id` WHERE lcf.id=:fileId ");
            builder.append(" AND IF(lbp.`actual_piece_count` IS NOT NULL,((lbp.`actual_piece_count` = 0.00  OR lbp.actual_piece_count IS NULL ) ");
            builder.append(" OR (lbp.`actual_weight_imperial` = 0.00  OR lbp.actual_weight_imperial IS NULL) ");
            builder.append(" OR (lbp.`actual_volume_imperial` = 0.00  OR lbp.actual_volume_imperial IS NULL ) ");
            builder.append(" OR (lbp.`actual_volume_metric` = 0.00  OR lbp.actual_volume_metric IS NULL ) ");
            builder.append(" OR (lbp.`actual_weight_metric` = 0.00  OR lbp.actual_weight_metric IS NULL)),");
            builder.append(" (( lbp.`booked_piece_count` = 0.00 OR lbp.`booked_piece_count` IS NULL) ");
            builder.append(" OR (lbp.`booked_weight_imperial` = 0.00  OR lbp.`booked_weight_imperial` IS NULL ) ");
            builder.append(" OR (lbp.`booked_volume_imperial` = 0.00  OR lbp.booked_volume_imperial IS NULL) ");
            builder.append(" OR ( lbp.`booked_volume_metric` = 0.00 OR lbp.booked_volume_metric IS NULL) ");
            builder.append(" OR (lbp.`booked_weight_metric` = 0.00 OR lbp.booked_weight_metric IS NULL)))");
        }
        SQLQuery query = getCurrentSession().createSQLQuery(builder.toString());
        query.setParameter("fileId", fileId);
        return (String) query.uniqueResult();
    }
}

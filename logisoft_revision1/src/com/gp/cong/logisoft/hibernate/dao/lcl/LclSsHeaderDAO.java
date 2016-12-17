/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import java.util.List;
import org.hibernate.Query;
import java.math.BigInteger;
import org.hibernate.SQLQuery;
import org.hibernate.type.BigIntegerType;
import org.hibernate.type.BooleanType;
import org.hibernate.type.LongType;

public class LclSsHeaderDAO extends BaseHibernateDAO<LclSsHeader> {

    public LclSsHeaderDAO() {
        super(LclSsHeader.class);
    }

    public synchronized String getAlphabetValues(final int podId, final String voySchNo, final String billTrmnum, final String voyageType) throws Exception {
        String voyageSuffix = "";
        Object[] voyageValues = this.getVoyageSuffixValue(podId, voySchNo, billTrmnum, voyageType);
        if (podId == Integer.parseInt(voyageValues[0].toString()) && voySchNo.equalsIgnoreCase(voyageValues[1].toString())
                && billTrmnum.equalsIgnoreCase(voyageValues[2].toString())) {
            if (voyageValues[3] == null) {
                voyageSuffix = "A";
            } else if (voyageValues[3].toString().length() == 1) {
                if ("Z".equalsIgnoreCase(voyageValues[3].toString())) {
                    voyageSuffix = "AA";
                } else if (voyageValues[3] != null && !voyageValues[3].equals("")) {
                    int charValue = voyageValues[3].toString().charAt(0);
                    voyageSuffix = String.valueOf((char) (charValue + 1));
                }
            } else {
                String prefix = voyageValues[3].toString().substring(0, 1);
                String suffix = voyageValues[3].toString().substring(1);
                if ("Z".equalsIgnoreCase(suffix)) {
                    int charValue = prefix.charAt(0);
                    voyageSuffix = String.valueOf((char) (charValue + 1));
                    voyageSuffix = voyageSuffix + "A";
                } else {
                    int charValue = suffix.charAt(0);
                    voyageSuffix = String.valueOf((char) (charValue + 1));
                    voyageSuffix = prefix + voyageSuffix;
                }
            }
        }
        return voyageSuffix;
    }

    public Object[] getVoyageSuffixValue(final int podId, final String voySchNo, final String billTrmnum, final String voyageType) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append("un.id,lsh.schedule_no,t.trmnum,lsh.suffix ");
        sb.append("FROM lcl_ss_header lsh ");
        sb.append(" JOIN un_location un ON un.id=lsh.destination_id JOIN terminal t ON t.trmnum=lsh.billing_trmnum ");
        sb.append(" WHERE lsh.schedule_no='").append(voySchNo).append("'");
        sb.append(" AND lsh.service_type='").append(voyageType).append("'");
        sb.append(" AND un.id=").append(podId).append(" AND t.trmnum=").append(billTrmnum);
        sb.append("  ORDER BY lsh.id DESC LIMIT 1");
        return (Object[]) getSession().createSQLQuery(sb.toString()).uniqueResult();
    }

    public Integer getVoyageSuffixCountValue(final int podId, final String voySchNo, final String billTrmnum, final String voyageType) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT count(*)");
        sb.append("FROM lcl_ss_header lsh ");
        sb.append(" JOIN un_location un ON un.id=lsh.destination_id JOIN terminal t ON t.trmnum=lsh.billing_trmnum ");
        sb.append(" WHERE lsh.schedule_no='").append(voySchNo).append("'");
        sb.append(" AND lsh.service_type='").append(voyageType).append("'");
        sb.append(" AND un.id=").append(podId).append(" AND t.trmnum=").append(billTrmnum);
        Object result = getCurrentSession().createSQLQuery(sb.toString()).uniqueResult();
        if (null != result) {
            return Integer.parseInt(result.toString());
        }
        return 0;
    }

    public BigInteger getHeaderId(String pol, String pod, String scheduleNo) throws Exception {
        String query = "SELECT id FROM lcl_ss_header WHERE schedule_no='" + scheduleNo + "' AND origin_id='" + pol + "' AND destination_id='" + pod + "' limit 1";
        Object id = getSession().createSQLQuery(query).uniqueResult();
        return null != id ? (BigInteger) id : null;
    }

    public void updateModifiedDateTime(Long headerId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE lcl_ss_header SET modified_datetime = SYSDATE() WHERE  id= ").append(headerId);
        getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public String[] getLclSsHeaderValues(Long headerId) throws Exception {
        StringBuilder sb = new StringBuilder();
        String[] lclSsHeaderValues = new String[4];
        sb.append("select lsh.closed_datetime,lsh.audited_datetime,CONCAT(lsh.billing_trmnum,'-',ter.terminal_location) from lcl_ss_header lsh LEFT JOIN terminal ter ON ter.trmnum=lsh.billing_trmnum where id=").append(headerId);
        Query query = getCurrentSession().createSQLQuery(sb.toString());
        List list = query.list();
        if (list != null && !list.isEmpty()) {
            Object[] object = (Object[]) list.get(0);
            if (object[0] != null) {
                lclSsHeaderValues[0] = object[0].toString();
            } else {
                lclSsHeaderValues[0] = "";
            }
            if (object[1] != null) {
                lclSsHeaderValues[1] = object[1].toString();
            } else {
                lclSsHeaderValues[1] = "";
            }
            if (object[2] != null) {
                lclSsHeaderValues[2] = object[2].toString();
            } else {
                lclSsHeaderValues[2] = "";
            }
        }
        return lclSsHeaderValues;
    }

    public Integer getDispCount(Long headerId, String eliteCode) throws Exception {
        BigInteger count = new BigInteger("0");
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(*)");
        sb.append(" from `lcl_ss_header` sh");
        sb.append(" JOIN `lcl_ss_detail` sd");
        sb.append(" ON (sh.`id` = sd.`ss_header_id`)");
        sb.append(" JOIN `lcl_unit_ss_dispo` lusd");
        sb.append("  ON (sd.`id` = lusd.`ss_detail_id`)");
        sb.append(" JOIN `lcl_unit` u ");
        sb.append(" ON (u.`id` =lusd.`unit_id`) ");
        sb.append(" JOIN disposition d ");
        sb.append(" ON (lusd.disposition_id = d.id)  ");
        sb.append(" WHERE d.elite_code='").append(eliteCode).append("' AND sh.`id`=").append(headerId);
        Object ob = getCurrentSession().createSQLQuery(sb.toString()).uniqueResult();
        if (ob != null) {
            count = (BigInteger) ob;
        }
        return count.intValue();
    }

    public String checkFileNumberLocking(String fileNumber, String userId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        String select1 = "File " + fileNumber + " is already opened in another window";
        String select2 = "This record is being used by ";
        queryBuilder.append("select if(u.user_id = '").append(userId).append("','").append(select1).append("',");
        queryBuilder.append("concat('").append(select2).append("',u.login_name,' on ',date_format(p.process_info_date,'%d-%b-%Y %h:%i %p'))) as result");
        queryBuilder.append(" from process_info p");
        queryBuilder.append(" join user_details u");
        queryBuilder.append(" on (p.user_id = u.user_id)");
        queryBuilder.append(" where p.record_id = '").append(fileNumber).append("'");
        queryBuilder.append(" AND module_id='LCL FILE' order by p.id desc limit 1");
        return (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public Integer differpodidandArrivalId(String masterScheuleId) throws Exception {
        String queryString = "SELECT ssd.arrival_id FROM lcl_schedule s  "
                + "JOIN lcl_ss_header ssh ON s.schedule_no=ssh.schedule_no AND s.pol_id=ssh.origin_id AND s.pod_id=ssh.destination_id  "
                + "JOIN lcl_ss_detail ssd ON ssh.id=ssd.ss_header_id AND ssd.trans_mode = 'V'  "
                + "WHERE s.id=?0 limit 1";
        Query queryObject = getCurrentSession().createSQLQuery(queryString);
        queryObject.setParameter("0", masterScheuleId);
        Object podValues = queryObject.uniqueResult();
        return null != podValues ? (Integer) podValues : new Integer(0);
    }

    public String masterSchedulescacCode(String masterScheuleId) throws Exception {
        String queryString = "SELECT cl.SCAC FROM trading_partner tp "
                + " JOIN carriers_or_line cl ON cl.carrier_code=tp.ssline_number "
                + " WHERE tp.ssline_number=:ssLineNo LIMIT 1";
        Query queryObject = getCurrentSession().createSQLQuery(queryString);
        queryObject.setParameter("ssLineNo", masterScheuleId);
        Object scacValues = queryObject.uniqueResult();
        return null != scacValues ? scacValues.toString() : "";
    }

    public boolean isImportVoyage(String voyageNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  coalesce(");
        queryBuilder.append("    h.`service_type` = 'I',");
        queryBuilder.append("    false");
        queryBuilder.append("  ) isImportVoyage ");
        queryBuilder.append("from");
        queryBuilder.append("  `lcl_ss_header` h ");
        queryBuilder.append("where h.`schedule_no` = :voyageNo");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("voyageNo", voyageNo);
        query.addScalar("isImportVoyage", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public Long getHeaderId(String voyageNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  id ");
        queryBuilder.append("from");
        queryBuilder.append("  `lcl_ss_header` h ");
        queryBuilder.append("where h.`schedule_no` = :voyageNo");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("voyageNo", voyageNo);
        query.addScalar("id", LongType.INSTANCE);
        return (Long) query.uniqueResult();
    }

    public String getExportVoyageColumnValue(String columnName, String headerId) throws Exception {
        SQLQuery query = getCurrentSession().createSQLQuery("select " + columnName + " from lcl_ss_header where id=:headerId ");
        query.setParameter("headerId", headerId);
        return query.uniqueResult().toString();
    }

    public BigInteger getSSmasterContactId(Long ssHeaderId, String bookingNo) throws Exception {
        String query = "SELECT lsm.`ship_ss_contact_id` AS contactId FROM `lcl_ss_masterbl` lsm  WHERE lsm.`ss_header_id`=:ssHeaderId AND lsm.`sp_booking_no`=:bookingNo LIMIT 1";
        SQLQuery queryObject = getCurrentSession().createSQLQuery(query);
        queryObject.setLong("ssHeaderId", ssHeaderId);
        queryObject.setParameter("bookingNo", bookingNo);
        queryObject.addScalar("contactId", BigIntegerType.INSTANCE);
        return (BigInteger) queryObject.uniqueResult();
    }

    public String getvoyageOriginIdWithFileId(String fileId) throws Exception {
        SQLQuery query = getCurrentSession()
                .createSQLQuery("select lsh.origin_id  from  lcl_ss_header lsh "
                        + " where lsh.id =(SELECT GetPickedORBookedVoyage(:fileId))"
                        + " and lsh.service_type in('E','C')");
        query.setParameter("fileId", fileId);
        Object result = query.setMaxResults(1).uniqueResult();
        return result != null ? result.toString() : "0";
    }

    public String getPickedOrBookedVoyOriginID(String scheduleNo) throws Exception {
        SQLQuery query = getCurrentSession()
                .createSQLQuery("select lsh.origin_id  from  lcl_ss_header lsh where lsh.schedule_no=:scheduleNo");
        query.setParameter("scheduleNo", scheduleNo);
        Object result = query.uniqueResult();
        return result != null ? result.toString() : "";
    }
}

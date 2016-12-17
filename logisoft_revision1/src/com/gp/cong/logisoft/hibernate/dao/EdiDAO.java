package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.LogFileEdi;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cvst.logisoft.domain.BookingFcl;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

public class EdiDAO extends BaseHibernateDAO {

    public String getRoutingInstruction(String fclblCluase) throws Exception {
        String queryString = "select codedesc from genericcode_dup where codetypeid=7 and code=?0";
        Query queryObject = getSession().createSQLQuery(queryString);
        Object routingInstruction = queryObject.setParameter("0", fclblCluase).uniqueResult();
        return null != routingInstruction ? routingInstruction.toString() : "";
    }

    public String getCountry(String id) throws Exception {
        String queryString = "select codedesc from genericcode_dup where id = ?0 limit 1";
        Query queryObject = getSession().createSQLQuery(queryString);
        Object country = queryObject.setParameter("0", id).uniqueResult();
        return null != country ? country.toString() : "";
    }

    public String getSsLine(String ssline) throws Exception {
        String queryString = "select ssline_number from trading_partner where acct_no = ?0 limit 1";
        Object sslLine = getSession().createSQLQuery(queryString).setParameter("0", ssline).uniqueResult();
        return null != sslLine ? sslLine.toString() : "";
    }

    public String getScacOrContract(String ssline, String fieldName) throws Exception {
        String queryString = "select " + fieldName + " from carriers_or_line where carrier_code = ?0 limit 1";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", ssline);
        Object scacOrContract = queryObject.uniqueResult();
        return null != scacOrContract ? scacOrContract.toString() : "";
    }

    public LogFileEdi getfilestatus(String fileno) {
        String query = " FROM LogFileEdi where file_no='" + fileno + "' ORDER BY id DESC ";
        Query queryObject = getSession().createQuery(query);
        return (LogFileEdi) queryObject.setMaxResults(1).uniqueResult();
    }

    public String getMoveType(String codedesc, String selectField) throws Exception {
        String queryString = "select " + selectField + " from genericcode_dup where codetypeid=48 and codedesc =?0 limit 1";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", codedesc);
        Object moveType = queryObject.uniqueResult();
        return null != moveType ? moveType.toString() : "";
    }

    public String getEquipmantType(String codedesc, String fieldName) throws Exception {
        String queryString = "select " + fieldName + " from genericcode_dup where codetypeid=38 and id='" + codedesc + "' limit 1";
        Query queryObject = getSession().createSQLQuery(queryString);
        Object equipmantType = queryObject.uniqueResult();
        return null != equipmantType ? equipmantType.toString() : "";
    }

    public String getEquipmentType(String code, String fieldName) throws Exception {
        String queryString = "select " + fieldName + " from genericcode_dup where codetypeid=38 and code=:code limit 1";
        SQLQuery queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("code", code);
        String equipmentType = (String) queryObject.uniqueResult();
        return null != equipmentType ? equipmentType : "";
    }

    public String getUserEmail(String itmnum) throws Exception {
        String queryString = "select email from user_details where terminal_id = ?0 limit 1";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", itmnum);
        Object email = queryObject.uniqueResult();
        return null != email ? email.toString() : "";
    }

    public int getDocVersion(String fileName) throws Exception {
        int count = 0;
        String queryString = "select COUNT(*) from logfile_edi where filename like ?0";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", fileName);
        count = Integer.parseInt(queryObject.uniqueResult().toString());
        return count;
    }

    public Integer getDocVersion(String fileNo, String messageType) throws Exception {
        String queryString = "select count(*) + 1 as docVersion from logfile_edi where file_no = :fileNo and message_type = :messageType";
        SQLQuery query = getSession().createSQLQuery(queryString);
        query.setString("fileNo", fileNo);
        query.setString("messageType", messageType);
        query.addScalar("docVersion", IntegerType.INSTANCE);
        return (Integer) query.uniqueResult();
    }

    public Boolean validate997(String fileNo, String bookingNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" select message_type as messageType from logfile_edi where file_no = :fileNo ");
        if (!"".equalsIgnoreCase(bookingNo)) {
            queryBuilder.append(" and booking_number='").append(bookingNo).append("'");
        }
        queryBuilder.append(" and message_type in ('304', '997') order by id desc ");
        SQLQuery query = getSession().createSQLQuery(queryBuilder.toString());
        query.setString("fileNo", fileNo);
        query.addScalar("messageType", StringType.INSTANCE);
        query.setMaxResults(1);
        String messageType = (String) query.uniqueResult();
        return "997".equalsIgnoreCase(messageType) || CommonUtils.isEmpty(messageType);
    }

    public BookingFcl getBookingDetails(String fileNo) throws Exception {
        String queryString = "";
        queryString = "from BookingFcl where fileNo=?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", fileNo);
        return (BookingFcl) queryObject.setMaxResults(1).uniqueResult();
    }

    public RefTerminal findByTerminal(String trmnum) throws Exception {
        String queryString = " from RefTerminal where trmnum=?0";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", trmnum);
        return (RefTerminal) queryObject.setMaxResults(1).uniqueResult();
    }

    public List findFclBlMarks(Integer trailerNoId) throws Exception {
        String queryString = " from FclBlMarks where trailerNoId=?0";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", trailerNoId);
        return queryObject.list();
    }

    public String getShpmentIdForCancelBooking(String fileNo, String msgTyp) throws Exception {
        String query = "select concat(Shipment_id, ',', if(booking_number is not null and booking_number <> '',booking_number,''))  "
                + "from logfile_edi where file_no='" + fileNo + "' and status <> 'failure' and message_type ='" + msgTyp + "' and doc_type='Booking' and shipment_id is not null order by id desc limit 1";
        Object result = getCurrentSession().createSQLQuery(query).uniqueResult();
        return null != result ? result.toString() : "";
    }

    public boolean isValidForCancelOrAmendment(String fileNo, String msgTyp) throws Exception {
        String q300 = "select count(*) from logfile_edi where file_no = '" + fileNo + "' and status <> 'failure' and message_type = '300' and doc_type = 'Booking'";
        String q300Count = getCurrentSession().createSQLQuery(q300).uniqueResult().toString();

        String q301 = "select count(*) from logfile_edi where file_no = '" + fileNo + "' and status <> 'failure' and message_type = '" + msgTyp + "' and doc_type = 'Booking' and shipment_id is not null";
        String q301Count = getCurrentSession().createSQLQuery(q301).uniqueResult().toString();

        String q301PendingOrPartial = "select count(*) from logfile_edi where file_no = '" + fileNo + "' and status in('pending','conditionaccepted','declined','replaced') and message_type = '301' and doc_type = 'Booking' and shipment_id is not null";
        String q301PendingOrPartialCount = getCurrentSession().createSQLQuery(q301PendingOrPartial).uniqueResult().toString();

        if (q301PendingOrPartialCount.equals("0")) {
            return !q300Count.equals(q301Count);
        }
        return q301PendingOrPartialCount.equals("0");
    }
    
    public String getKNCompanyName(String bkgNumber) {
        String query = "SELECT sender_id as companyName FROM `kn_shipping_instruction` WHERE bkg_number=:bkgNumber LIMIT 1";
        SQLQuery queryObject = getCurrentSession().createSQLQuery(query);
        queryObject.setParameter("bkgNumber", bkgNumber);
        queryObject.addScalar("companyName", StringType.INSTANCE);
        return (String) queryObject.uniqueResult();
    }
}

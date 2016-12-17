package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import java.util.List;
import org.hibernate.Query;

import com.gp.cong.logisoft.domain.LogFileEdi;
import com.gp.cong.logisoft.domain.Notes;
import com.logiware.common.model.Edi997RemainderModel;
import com.logiware.edi.xml.Inttra997Data;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

public class LogFileEdiDAO extends BaseHibernateDAO {

    NotesDAO notesDAO = new NotesDAO();

    public List findAllEdi(String drnumber, String messageType, String ediCompany) throws Exception {
        String queryString = "from LogFileEdi";
        boolean found = false;
        if (drnumber != null && !drnumber.equals("") && !drnumber.equals("0")) {
            found = true;
            queryString += " where drnumber like '" + drnumber + "%'";
        }
        if (messageType != null && !messageType.equals("") && !messageType.equals("0")) {
            if (found) {
                queryString += " and messageType like '" + messageType + "%'";
            } else {
                found = true;
                queryString += " where messageType like '" + messageType + "%'";
            }
        }
        if (ediCompany != null && !ediCompany.equals("") && !ediCompany.equals("0")) {
            if (found) {
                queryString += " and ediCompany like '" + ediCompany + "%'";
            } else {
                found = true;
                queryString += " where ediCompany like '" + ediCompany + "%'";
            }
        }
        queryString += " group by filename,messageType order by processedDate desc";

        Query queryObject = getCurrentSession().createQuery(queryString);
        return queryObject.list();
    }

    public List findByDrNumber(String drNumber, String msgType) throws Exception {
        List msgTypList = new ArrayList();
        String appendQuery = "";
        if (msgType.equals("booking")) {
            msgTypList.add("300");
            msgTypList.add("997");
            msgTypList.add("301");
            appendQuery = " and docType= 'Booking' ";
        } else {
            msgTypList.add("304");
            msgTypList.add("997");
            msgTypList.add("315");
            appendQuery = " and docType is null ";
        }
        String queryString = "from LogFileEdi where drnumber=?0 and messageType in (?1) " + appendQuery + "group by filename,messageType,processedDate order by processedDate desc";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", drNumber);
        queryObject.setParameterList("1", msgTypList);
        return queryObject.list();
    }

    public String findDrNumberStatus(String drNumber, String status) throws Exception {
        String queryString = "select status from logfile_edi where DRNumber=?0 and message_type = '304' and status = '" + status + "' limit 1";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", drNumber);
        Object result = queryObject.uniqueResult();
        return null != result ? result.toString() : "";
    }

    public Integer find997Status(String drNumber) throws Exception {
        String queryString = "SELECT edi_997_ack.si_id FROM logfile_edi LEFT JOIN edi_997_ack ON edi_997_ack.si_id =  logfile_edi.id   WHERE  logfile_edi.file_no = ?0 AND logfile_edi.message_type = '304' AND logfile_edi.status = 'success' ORDER BY logfile_edi.id DESC LIMIT 1";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", drNumber);
        Object status = queryObject.uniqueResult();
        return null != status ? (Integer) status : null;
    }

    public String findTrackingStatus(String drNumber) throws Exception {
        String queryString = "select tracking_status from logfile_edi where message_type='315' and  drnumber='" + drNumber + "' and status = 'success' ORDER BY id DESC limit 1";
        Query queryObject = getSession().createSQLQuery(queryString);
        Object status = queryObject.uniqueResult();
        return null != status ? status.toString() : "";
    }

    public String findAckStatus(String fileNo) throws Exception {
        String queryString = "select ack_status from logfile_edi where message_type='304' and  file_no='" + fileNo + "' and status = 'success' ORDER BY id DESC limit 1";
        Query queryObject = getSession().createSQLQuery(queryString);
        Object status = queryObject.uniqueResult();
        return null != status ? status.toString() : "";
    }

    public String findStatusDrNumber(String fileNo) throws Exception {
        String queryString = "select status from logfile_edi where message_type='304' and  file_no='" + fileNo + "' ORDER BY id DESC limit 1";
        Query queryObject = getSession().createSQLQuery(queryString);
        Object status = queryObject.uniqueResult();
        return null != status ? status.toString() : "";
    }

    public List<LogFileEdi> findByBookingNo(String bookingNo, String voyageNo) throws Exception {
        String queryString = "from LogFileEdi where fileNo=:voyageNo and bookingNumber=:bookingNo  order by id desc";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("voyageNo", voyageNo);
        queryObject.setParameter("bookingNo", bookingNo);
        return queryObject.list();
    }

    public String find304Status(String bookingNo, String voyageNo) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" select ack_status from logfile_edi where message_type='304' and  ");
        queryStr.append(" file_no=:voyageNo and booking_number=:bookingNo  ");
        queryStr.append(" and status = 'success' ORDER BY id DESC limit 1 ");
        Query queryObject = getSession().createSQLQuery(queryStr.toString());
        queryObject.setParameter("voyageNo", voyageNo);
        queryObject.setParameter("bookingNo", bookingNo);
        Object status = queryObject.uniqueResult();
        return null != status ? status.toString() : "";
    }

    public String findAckStatus(String bookingNo, String voyageNo) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" select status from logfile_edi where message_type='304' and  ");
        queryStr.append(" file_no=:voyageNo and booking_number=:bookingNo  ");
        queryStr.append(" and status = 'success' ORDER BY id DESC limit 1 ");
        Query queryObject = getSession().createSQLQuery(queryStr.toString());
        queryObject.setParameter("voyageNo", voyageNo);
        queryObject.setParameter("bookingNo", bookingNo);
        Object status = queryObject.uniqueResult();
        return null != status ? status.toString() : "";
    }

    public String findITNStatus(String itnNumber) throws Exception {
        String queryString = "SELECT status FROM aes_history WHERE itn = '" + itnNumber + "' ORDER BY id desc limit 1";
        Query queryObject = getSession().createSQLQuery(queryString);
        Object status = queryObject.uniqueResult();
        return null != status ? status.toString() : "";
    }

    public String findITNStatusFileNo(String fileNumber) throws Exception {
        String queryString = "SELECT status FROM aes_history WHERE file_number like '%" + fileNumber + "%' ORDER BY id desc limit 1";
        Query queryObject = getSession().createSQLQuery(queryString);
        Object status = queryObject.uniqueResult();
        return null != status ? status.toString() : "";
    }

    public String getITNStatus(String fileNumber) throws Exception {
        String queryString = "SELECT status FROM aes_history WHERE file_no=:fileNumber  ORDER BY id desc limit 1";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("fileNumber", fileNumber);
        Object status = queryObject.uniqueResult();
        return null != status ? status.toString() : "";
    }

    public String findMasterStatusFileNo(String fileNumber) throws Exception {
        Object status = null;
        if (null != fileNumber && !fileNumber.equals("")) {
            String queryString = "SELECT STATUS FROM document_store_log WHERE document_id = '" + fileNumber + "' ORDER BY id DESC LIMIT 1;";
            Query queryObject = getSession().createSQLQuery(queryString);
            status = queryObject.uniqueResult();
        }
        return null != status ? status.toString() : "";
    }

    public void saveLogFileEdi(LogFileEdi logFileEdi) throws Exception {
        getSession().save(logFileEdi);
        getSession().flush();
    }

    public String findDrNumber(String bookingNo) throws Exception {
        String queryString = "select concat(Quote_Term,Quote_DR) as DRNumber ,BookingNumber from edi_997_ack where BookingNumber = ?0 limit 1";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", bookingNo);
        Object drNumber = queryObject.uniqueResult();
        return null != drNumber ? drNumber.toString() : "";
    }

    public String findDrNumberFromBooking(String bookingNo) throws Exception {
        String queryString = "SELECT file_no FROM fcl_bl where BookingNo = '" + bookingNo + "' limit 1";
        Query queryObject = getSession().createSQLQuery(queryString);
        Object drNumber = queryObject.uniqueResult();
        if (null != drNumber) {
            return drNumber.toString();
        } else {
            queryString = "SELECT file_no FROM booking_fcl where SSBookingNo = '" + bookingNo + "' limit 1";
            queryObject = getSession().createSQLQuery(queryString);
            drNumber = queryObject.uniqueResult().toString();
            if (null != drNumber) {
                return drNumber.toString();
            }
        }
        return "";
    }

    public Integer findSiId(String fileName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select id as id");
        queryBuilder.append(" from logfile_edi");
        queryBuilder.append(" where message_type = '304'");
        queryBuilder.append(" and filename like '%").append(fileName).append("%'");
        queryBuilder.append(" order by id desc limit 1");
        SQLQuery query = getSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("id", IntegerType.INSTANCE);
        Integer id = (Integer) query.uniqueResult();
        return null != id ? id : 0;
    }

    public Object[] findSiIdAndFileName(String shipmentId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select id,filename ");
        queryBuilder.append(" from logfile_edi");
        queryBuilder.append(" where message_type = '304'");
        queryBuilder.append(" and filename like '%").append(shipmentId).append("%'");
        queryBuilder.append(" order by id desc limit 1");
        SQLQuery query = getSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("id", IntegerType.INSTANCE);
        query.addScalar("filename", StringType.INSTANCE);
        Object result = query.uniqueResult();
        if (null != result) {
            return (Object[]) result;
        }
        return null;
    }

    public int getSedCount(String fileName) throws Exception {
        int count = 0;
        String queryString = "select COUNT(*) from sed_filings where SHPDR like '%" + fileName + "%' and status = 'S' limit 1";
        Query queryObject = getSession().createSQLQuery(queryString);
        count = Integer.parseInt(queryObject.uniqueResult().toString());
        return count;
    }

    public String findTrackingStatus(String eventCode, String containerStatus, String eventLocation, String companyName) throws Exception {
        String status = "";
        String queryString = "select event_name from edi_tracking_status where event_code = '" + eventCode + "' ";
        if (CommonUtils.isNotEmpty(containerStatus)) {
            queryString += " and container_status ='" + containerStatus.substring(0, 1) + "' ";
        }
        if (CommonUtils.isNotEmpty(eventLocation)) {
            queryString += " and event_location_function ='" + eventLocation + "' ";
        }
        queryString += " and edi_company ='" + companyName + "' ";
        Query queryObject = getSession().createSQLQuery(queryString);
        List list = queryObject.list();
        if (null != list && !list.isEmpty()) {
            status = (String) list.get(0);
        }
        return status;
    }

    public String getContainerNumber(Integer bol) throws Exception {
        String queryString = "SELECT CAST(GROUP_CONCAT(fc.trailer_no SEPARATOR ',') AS CHAR CHARSET utf8) AS trailer_no FROM fcl_bl_container_dtls fc WHERE fc.BolId = " + bol + "";
        Query queryObject = getSession().createSQLQuery(queryString);
        List list = queryObject.list();
        if (null != list && !list.isEmpty()) {
            String status = (String) list.get(0);
            if (null != status) {
                String[] str = status.toString().split(",");
                String unit = "";
                int count = 0;
                for (String string : str) {
                    if (!"".equals(string)) {
                        count++;
                        if (!"".equals(unit)) {
                            if (count == 1) {
                                unit = unit + "" + string;
                            } else {
                                unit = unit + "," + string;
                            }
                        } else {
                            unit = string;
                        }
                        if (count == 2) {
                            count = 0;
                            unit = unit + "<br>";
                        }
                    }
                }
                return unit;
            }
        }
        return null;
    }

    public int getEDI304Count(String fileName) throws Exception {
        int count = 0;
        String queryString = "select COUNT(*) from logfile_edi where filename like '" + fileName + "'";
        Query queryObject = getSession().createSQLQuery(queryString);
        count = Integer.parseInt(queryObject.uniqueResult().toString());
        return count;
    }

    public void saveBooking997(Inttra997Data ediBookingAck, String fileName) throws Exception {
        Inttra997Data.Header header = ediBookingAck.getHeader();
        String docIdentyfier = Integer.toString(header.getDocumentIdentifier());
        docIdentyfier = "0" + docIdentyfier;
        String fileNo = docIdentyfier.replaceFirst("04", "");
        if (isValid997File(fileNo)) {
            Inttra997Data.MessageBody messageBody = ediBookingAck.getMessageBody();
            String ackstatus = messageBody.getMessageProperties().getShipmentID().getShipmentIdentifier().getAcknowledgment();
            LogFileEdi logFileEdi = new LogFileEdi();
            logFileEdi.setFilename(fileName);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date();
            String currentDate = sdf.format(date);
            logFileEdi.setProcessedDate(currentDate);
            if (ackstatus.equalsIgnoreCase("Accepted")) {
                logFileEdi.setStatus("success");
                logFileEdi.setDescription("No Error");
            } else {
                logFileEdi.setStatus("failure");
                logFileEdi.setDescription("Error");
            }
            logFileEdi.setEdiCompany("I");
            logFileEdi.setMessageType("997");
            logFileEdi.setDocType("Booking");
            logFileEdi.setDrnumber(docIdentyfier);
            logFileEdi.setFileNo(fileNo);
            logFileEdi.setAckStatus(ackstatus);
            logFileEdi.setAckReceivedDate(date);
            String shpmntId = messageBody.getMessageProperties().getShipmentID().getShipmentIdentifier().getValue();
            logFileEdi.setShipmentId(shpmntId);
            saveLogFileEdi(logFileEdi);
            bookingEdiNotesForAck(fileNo, "EDI Booking Acknowledgement(997) Received with ShipmentId :" + shpmntId + "");
        }
    }

    public void bookingEdiNotesForAck(String fileNo, String message) throws Exception {
        Notes note = new Notes();
        note.setModuleId(NotesConstants.FILE);
        note.setModuleRefId(fileNo);
        note.setNoteType(NotesConstants.NOTES_TYPE_EVENT);
        note.setUpdateDate(new Date());
        note.setUpdatedBy("Bkg EDI");
        note.setNoteDesc(message);
        notesDAO.save(note);
    }

    public boolean isValid997File(String fileNo) throws Exception {
        String q = "select count(*) from logfile_edi where file_no='" + fileNo + "' and message_type='300'";
        String count = getCurrentSession().createSQLQuery(q).uniqueResult().toString();
        return !count.equals("0");
    }

    public void insertEdi(String filePath, String drNo, String bookingNo, String dateNow, String status,
            String ediCompany, String fileNo, String msgType) throws Exception {
        LogFileEdi logFileEdi = new LogFileEdi();
        logFileEdi.setFilename(filePath);
        logFileEdi.setProcessedDate(dateNow);
        logFileEdi.setStatus(status);
        logFileEdi.setEdiCompany(ediCompany);
        logFileEdi.setMessageType(msgType);
        logFileEdi.setDrnumber(drNo);
        logFileEdi.setBookingNumber(bookingNo);
        logFileEdi.setFileNo(fileNo);
        this.saveLogFileEdi(logFileEdi);
    }
    
    public List<Edi997RemainderModel> getLogFileEdiList() throws Exception {
        StringBuilder qBuilder = new StringBuilder();
        qBuilder.append("SELECT DISTINCT * FROM(SELECT a.file_no as fileNo,a.edi_company as ediCompany,a.booking_number as bookingNumber, ");
        qBuilder.append("STR_TO_DATE( a.processed_date, '%Y%m%d%H%i%s' ) AS Sent304, ");
        qBuilder.append("(SELECT COUNT( * )FROM logfile_edi b WHERE b.DRNumber = a.DRNumber ");
        qBuilder.append("AND b.message_type = 304 AND b.status = 'success' AND b.edi_company = a.edi_company)AS Num304, ");
        qBuilder.append("(SELECT COUNT( * )FROM logfile_edi b WHERE b.DRNumber = a.DRNumber ");
        qBuilder.append("AND b.message_type = 997 AND b.status = 'success' AND b.edi_company = a.edi_company ");
        qBuilder.append(")AS Num997,f.edi_created_by as ediCreatedBy,u.email as ToEmail FROM logfile_edi a ");
        qBuilder.append("JOIN fcl_bl f ON(a.`file_no` = f.`file_no`) ");
        qBuilder.append("JOIN user_details u ON(u.`login_name` = f.`edi_created_by`) ");
        qBuilder.append("WHERE a.message_type = 304 AND a.processed_date BETWEEN NOW()- INTERVAL 2 DAY AND NOW() ");
        qBuilder.append("AND a.status = 'success' GROUP BY a.file_no ORDER BY a.file_no) f WHERE f.Num304 > f.Num997");
        SQLQuery query = getCurrentSession().createSQLQuery(qBuilder.toString());
        query.addScalar("fileNo", StringType.INSTANCE);
        query.addScalar("ediCompany", StringType.INSTANCE);
        query.addScalar("bookingNumber", StringType.INSTANCE);
        query.addScalar("Sent304", StringType.INSTANCE);
        query.addScalar("Num304", StringType.INSTANCE);
        query.addScalar("Num997", StringType.INSTANCE);
        query.addScalar("ediCreatedBy", StringType.INSTANCE);
        query.addScalar("ToEmail", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(Edi997RemainderModel.class));
        return query.list();
    }
}

package com.gp.cvst.logisoft.hibernate.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.domain.DocumentStoreLog;
import com.logiware.accounting.utils.BeanUtils;
import java.math.BigInteger;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.BooleanType;
import org.hibernate.type.StringType;

public class DocumentStoreLogDAO extends BaseHibernateDAO {

    public void save(DocumentStoreLog documentStoreLog) throws Exception {
        getSession().save(documentStoreLog);
        getSession().flush();
    }

    public void update(DocumentStoreLog documentStoreLog) throws Exception {
        getSession().update(documentStoreLog);
    }

    public void SaveOrUpdate(DocumentStoreLog documentStoreLog) {
        getSession().saveOrUpdate(documentStoreLog);
        getSession().flush();
    }

    public void delete(DocumentStoreLog documentStoreLog) {
        getSession().delete(documentStoreLog);
        getSession().flush();
    }

    @SuppressWarnings("unchecked")
    public List<DocumentStoreLog> getDocumentStoreLog(String documentID,
            String screenName, String documentName) throws Exception {
        List<DocumentStoreLog> docViewList = new ArrayList<DocumentStoreLog>();
        String queryString = "from DocumentStoreLog where documentID = ?0 and documentName = ?1 and screenName=?2 order by dateOprDone DESC";
        Query query = getSession().createQuery(queryString);
        query.setParameter("0", documentID);
        query.setParameter("1", documentName);
        query.setParameter("2", screenName);
        List list = query.list();
        if (null != list && !list.isEmpty()) {
            docViewList = list;
        }
        return docViewList;
    }

    public List<DocumentStoreLog> getDocumentStoreLogForViewAll(String documentID,
            String screenName) throws Exception {
        List<DocumentStoreLog> docViewAllList = new ArrayList<DocumentStoreLog>();
        String queryString = "from DocumentStoreLog where documentID = ?0 and screenName=?1 order by dateOprDone DESC";
        Query query = getSession().createQuery(queryString);
        query.setParameter("0", documentID);
        query.setParameter("1", screenName);
        List list = query.list();
        if (null != list && !list.isEmpty()) {
            docViewAllList = list;
        }
        return docViewAllList;
    }

    public String getSsMasterStatus(String documentID,
            String screenName, String documentName) throws Exception {
        String status = "";
        if (null != documentName && documentName.equals(CommonConstants.SS_MASTER_BL)) {
            List docStoreList = getDocumentStoreLog(documentID, screenName, documentName);
            if (CommonUtils.isNotEmpty(docStoreList)) {
                DocumentStoreLog documentStoreLog = (DocumentStoreLog) docStoreList.get(0);
                status = documentStoreLog.getStatus();
            }
        }
        return status;
    }

    public String getHouseBlStatusOrOrginStatus(String documentID,
            String screenName, String documentName) throws Exception {
        String operation = "";
        if (null != documentName) {
            List docStoreList = getDocumentStoreLog(documentID, screenName, documentName);
            if (CommonUtils.isNotEmpty(docStoreList)) {
                DocumentStoreLog documentStoreLog = (DocumentStoreLog) docStoreList.get(0);
                operation = documentStoreLog.getOperation();
            }
        }
        return operation;
    }

    public Integer getNoOfFilesScannedOrAttached(String documentId,
            String screenName, String documentName, String operationType) throws Exception {
        Integer total = 0;
        String queryString = "select count(operation) from DocumentStoreLog where screenName=?0 and documentID = '" + documentId + "'";
        if (null != documentName && !documentName.trim().equals("")) {
            queryString += " and documentName=?1";
        }
        if (operationType.trim().equalsIgnoreCase("Scan or Attach")) {
            queryString += " and (operation='" + CommonConstants.PAGE_ACTION_SCAN + "' or operation='" + CommonConstants.PAGE_ACTION_ATTACH + "')";
        } else {
            queryString += " and operation='" + operationType + "'";
        }
        Query query = getSession().createQuery(queryString);
        query.setParameter("0", screenName);
        if (null != documentName && !documentName.trim().equals("")) {
            query.setParameter("1", documentName);
        }
        total = Integer.parseInt(query.uniqueResult().toString());
        if (null != total) {
            return total;
        } else {
            return 0;
        }
    }

    public DocumentStoreLog findById(java.lang.Integer id) throws Exception {
        DocumentStoreLog instance = (DocumentStoreLog) getSession().get(DocumentStoreLog.class, id);
        return instance;
    }

    public List getDisputedDocList(HashMap fieldsMap, String status) throws Exception {
        List disputedList = new ArrayList();
        Set<Map.Entry<String, String>> keySet = fieldsMap.entrySet();
        String queryString = "SELECT dis.file_No,dis.terminal ,dis.Port, dis.Port_of_Loading, dis.PortofDischarge ,dis.comments,dis.eta,dis.sail_date ,dis.ssline_name,dis.ssline_no,dis.ack_comments,dis.bol,"
                + "dis.House_BL,dis.STATUS FROM (SELECT f.file_No,f.terminal , f.Port, f.Port_of_Loading, f.PortofDischarge ,f.eta,f.sail_date ,f.ssline_name,f.ssline_no,f.bol,f.House_BL,"
                + "(SELECT d.status FROM document_store_log d WHERE d.Document_ID=f.file_no ORDER BY d.Date_Opr_done DESC LIMIT 1) AS STATUS,"
                + "(SELECT d.comments FROM document_store_log d WHERE d.Document_ID=f.file_no ORDER BY d.Date_Opr_done DESC LIMIT 1) AS comments,"
                + "(SELECT d.ack_comments FROM document_store_log d WHERE d.Document_ID=f.file_no ORDER BY d.Date_Opr_done DESC LIMIT 1) AS ack_comments"
                + " FROM fcl_bl f) dis WHERE dis.status = 'Disputed'";
        for (Iterator iter = keySet.iterator(); iter.hasNext();) {
            Map.Entry mEntry = (Map.Entry) iter.next();
            if (mEntry.getKey().equals("terminal")) {
                queryString += " AND " + "dis." + mEntry.getKey() + " LIKE '" + mEntry.getValue() + "'";
            } else {
                queryString += " AND " + "dis." + mEntry.getKey() + "='" + mEntry.getValue() + "'";
            }
        }
        queryString += " GROUP BY dis.file_No DESC";
        disputedList = getSession().createSQLQuery(queryString).list();
        return disputedList;
    }

    public int updateAck(String comments, String fileNo) throws Exception {
        String queryString = "update document_store_log set ack_comments = '" + comments + "' where Document_ID = '" + fileNo + "' and status='Disputed'";
        int id = getCurrentSession().createSQLQuery(queryString).executeUpdate();
        return id;
    }

    public String getAckComments(String fileNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ack_comments from document_store_log where document_id = '").append(fileNo).append("' and status='Disputed' limit 1");
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null != result ? result.toString() : "";
    }

    public List<DocumentStoreLog> getSSMasterApprovedDocuments(String fileNo) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(DocumentStoreLog.class);
        criteria.add(Restrictions.eq("documentID", fileNo));
        criteria.add(Restrictions.eq("screenName", "FCLFILE"));
        criteria.add(Restrictions.eq("documentName", "SS LINE MASTER BL"));
        criteria.add(Restrictions.eq("status", "Approved"));
        return criteria.list();
    }
    public List<DocumentStoreLog> getLclSSMasterApprovedDocuments(String fileNo) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(DocumentStoreLog.class);
        criteria.add(Restrictions.eq("documentID", fileNo));
        criteria.add(Restrictions.eq("screenName", "LCL SS MASTER BL"));
        criteria.add(Restrictions.eq("documentName", "SS LINE MASTER BL"));
        criteria.add(Restrictions.eq("status", "Approved"));
        return criteria.list();
    }
    public List<DocumentStoreLog> getNsBatchDocuments(String batchId) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(DocumentStoreLog.class);
        criteria.add(Restrictions.eq("documentID", batchId));
        criteria.add(Restrictions.eq("screenName", "AR BATCH"));
        criteria.add(Restrictions.eq("documentName", "AR BATCH"));
        return criteria.list();
    }

    public List<DocumentStoreLog> getInvoiceDocuments(String invoiceNumber) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(DocumentStoreLog.class);
        criteria.add(Restrictions.eq("documentID", invoiceNumber));
        criteria.add(Restrictions.eq("screenName", "INVOICE"));
        criteria.add(Restrictions.eq("documentName", "INVOICE"));
        return criteria.list();
    }

    public void overwriteDocumentId(String screenName, String documentName, String oldDocumentId, String newDocumentId) throws Exception {
        StringBuilder query = new StringBuilder("update document_store_log");
        query.append(" set document_id='").append(newDocumentId).append("'");
        query.append(" where screen_name='").append(screenName).append("'");
        query.append(" and document_name='").append(documentName).append("'");
        query.append(" and document_id='").append(oldDocumentId).append("'");
        getCurrentSession().createSQLQuery(query.toString()).executeUpdate();
    }

    public void copyDocuments(String screenName, String documentName, String oldDocumentId, String newDocumentId) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(DocumentStoreLog.class);
        criteria.add(Restrictions.eq("screenName", screenName));
        criteria.add(Restrictions.eq("documentName", documentName));
        criteria.add(Restrictions.eq("documentID", oldDocumentId));
        List<DocumentStoreLog> oldDocuments = criteria.list();
        if (CommonUtils.isNotEmpty(oldDocuments)) {
            for (DocumentStoreLog oldDocument : oldDocuments) {
                DocumentStoreLog newDocument = new DocumentStoreLog();
                BeanUtils.copyProperties(oldDocument, newDocument, "id");
                newDocument.setDocumentID(newDocumentId);
                save(newDocument);
            }
        }
    }

    public String getLatestDocument(String screenName, String documentName, String documentId) throws Exception {
        getCurrentSession().flush();
        StringBuilder query = new StringBuilder("select concat(file_location,'/',file_name) as file_path from document_store_log");
        query.append(" where screen_name='").append(screenName).append("'");
        query.append(" and document_name='").append(documentName).append("'");
        query.append(" and document_id='").append(documentId).append("'");
        query.append(" order by id desc limit 1");
        return (String) getCurrentSession().createSQLQuery(query.toString()).uniqueResult();
    }

    public boolean isUploaded(String documentId, String screenName, String documentName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select if(count(*)>0,'true','false') as uploaded");
        queryBuilder.append(" from document_store_log");
        queryBuilder.append(" where screen_name = '").append(screenName).append("'");
        queryBuilder.append(" and document_name = '").append(screenName).append("'");
        queryBuilder.append(" and document_id = '").append(documentId).append("'");
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null != result ? Boolean.valueOf(result.toString()) : false;
    }

    public String getFiles(String documentId, String screenName, String documentName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select cast(group_concat(concat(file_location,file_name) separator ';') as char character set latin1) as files");
        queryBuilder.append(" from document_store_log");
        queryBuilder.append(" where screen_name = '").append(screenName).append("'");
        queryBuilder.append(" and document_name = '").append(screenName).append("'");
        queryBuilder.append(" and document_id = '").append(documentId).append("'");
        return (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public void deleteDocuments(String screenName, String documentName, String documentId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete from document_store_log");
        queryBuilder.append(" where screen_name = '").append(screenName).append("'");
        queryBuilder.append(" and document_name = '").append(documentName).append("'");
        queryBuilder.append(" and document_id = '").append(documentId).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public Boolean isScanCountChecked(String screenName, String documentId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT IF(COUNT(*)>0,true,false) as result FROM document_store_log");
        sb.append(" WHERE document_id=:documentId AND screen_name=:screenName LIMIT 1");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setString("documentId", documentId);
        query.setString("screenName", screenName);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public String getScanStatus(String screenName, String documentId,String documentName) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT IF(STATUS IS NOT NULL ,CONCAT('Yes','(',STATUS,')'),'No') AS result FROM document_store_log ");
        sb.append(" WHERE document_id=:documentId AND screen_name=:screenName ");
        sb.append(" AND document_name=:documentName order by id desc LIMIT 1 ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setString("documentId", documentId);
        query.setString("screenName", screenName);
        query.setString("documentName", documentName);
        query.addScalar("result", StringType.INSTANCE);
        return (String) query.uniqueResult();
    }

    public List<DocumentStoreLog> getSSMasterDocuments(String fileNo, String screenName) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(DocumentStoreLog.class);
        criteria.add(Restrictions.eq("documentID", fileNo));
        criteria.add(Restrictions.eq("screenName", screenName));
        criteria.add(Restrictions.eq("documentName", "SS LINE MASTER BL"));
        return criteria.list();
    }
    public String getSSMasterDocumentsName(String fileNo, String screenName) throws Exception {
        String result = "";
        String Query = "select distinct document_name from document_store_log d where d.Document_ID='" + fileNo + "' and screen_name='" + screenName + "' and document_name='SS LINE MASTER BL'";
        result = (String) getCurrentSession().createSQLQuery(Query).uniqueResult();
        return null != result ? result : "";
    }

    public BigInteger getSSMasterCount(String fileNo, String screenName) {
        BigInteger result;
        String query = "select count(d.`document_id`) from document_store_log d where d.Document_ID='" + fileNo + "' and screen_name='" + screenName + "'";
        result = (BigInteger) getCurrentSession().createSQLQuery(query).uniqueResult();
        return result;
    }
   
    public BigInteger getSSLineMasterCount(String fileNo, String screenName) {
        BigInteger result;
        String query = "select count(d.`document_id`) from document_store_log d where d.Document_ID='" + fileNo + "' and screen_name='" + screenName + "' and document_name='SS LINE MASTER BL'";
        result = (BigInteger) getCurrentSession().createSQLQuery(query).uniqueResult();
        return result;
    }
}

package com.gp.cong.logisoft.hibernate.dao;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.ScanConfig;
import com.gp.cong.logisoft.struts.form.ScanForm;
import com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO;
import com.logiware.common.model.ResultModel;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

public class ScanDAO extends BaseHibernateDAO {

    public void save(ScanConfig scanConfig) throws Exception {
        getSession().save(scanConfig);
        getSession().flush();
    }

    public void update(ScanConfig scanConfig) throws Exception {
        getSession().update(scanConfig);
        getSession().flush();
    }

    public void delete(ScanConfig scanConfig) throws Exception {
        getSession().delete(scanConfig);
        getSession().flush();
    }

    public void SaveOrUpdate(ScanConfig scanConfig) throws Exception {
        getSession().saveOrUpdate(scanConfig);
        getSession().flush();
    }

    public ScanConfig findById(Long id) throws Exception {
        ScanConfig instance = (ScanConfig) getSession().get(
                "com.gp.cong.logisoft.domain.ScanConfig", id);
        return instance;
    }

    public List<ScanConfig> findAllScanConfig() throws Exception {
        List<ScanConfig> scanconfigList = null;
        scanconfigList = getCurrentSession().createCriteria(ScanConfig.class).list();
        return scanconfigList;
    }

    public List<ScanConfig> findScanConfigByScreenName(String screenName, String documentId) throws Exception {
        List<ScanConfig> scanconfigList = null;
        List<ScanConfig> scanList = new ArrayList<ScanConfig>();
        DocumentStoreLogDAO documentStoreLogDAO = new DocumentStoreLogDAO();
        Criteria criteria = getSession().createCriteria(ScanConfig.class);
        if (null != screenName && !screenName.trim().equalsIgnoreCase(CommonConstants.ALL)) {
            criteria.add(Restrictions.eq("screenName", screenName));
        } else {
            criteria.add(Restrictions.like("screenName", "%"));
        }
        criteria.addOrder(Order.asc("documentName"));
        scanconfigList = criteria.list();
        if (null != scanconfigList) {
            for (ScanConfig scanConfig : scanconfigList) {
                Integer totalScan = 0;
                Integer totalAttach = 0;

                if (null != documentId && !documentId.trim().equals("")) {
                    totalScan = documentStoreLogDAO.getNoOfFilesScannedOrAttached(documentId, scanConfig.getScreenName(), scanConfig.getDocumentName(), CommonConstants.PAGE_ACTION_SCAN);
                    totalAttach = documentStoreLogDAO.getNoOfFilesScannedOrAttached(documentId, scanConfig.getScreenName(), scanConfig.getDocumentName(), CommonConstants.PAGE_ACTION_ATTACH);
                }
                scanConfig.setTotalScan(totalScan.toString());
                scanConfig.setTotalAttach(totalAttach.toString());
                scanList.add(scanConfig);
            }
            return scanList;
        }
        return new ArrayList<ScanConfig>();
    }

    public boolean validateScanConfig(ScanForm scanForm) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(ScanConfig.class);
        criteria.add(Restrictions.eq("screenName", scanForm.getScreenName()));
//			criteria.add(Restrictions.eq("documentType", scanForm.getDocumentType()));
        criteria.add(Restrictions.eq("documentName", scanForm.getDocumentName()));
        List list = criteria.list();
        if (null != list && list.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public String getSsMasterStatus(String fileNo) throws Exception {
        String result = "";
        String Query = "select status from document_store_log d where d.Document_ID='" + fileNo + "' order by d.Date_Opr_done desc limit 1";
        result = (String) getCurrentSession().createSQLQuery(Query).uniqueResult();
        return result;
    }

    public void updateMasterReceived(String fileNo, String status) {
        String query = "update fcl_bl set received_master = '" + status + "' where file_no like '" + fileNo + "%'";
        getCurrentSession().createSQLQuery(query).executeUpdate();
    }

    public boolean isDocumentExists(String screenName, String documentName, Long id) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  if(count(*) > 0, 1, 0) as result ");
        queryBuilder.append("from");
        queryBuilder.append("  `scan_config` ");
        queryBuilder.append("where");
        queryBuilder.append("  `screen_name` = :screenName");
        queryBuilder.append("  and `document_name` = :documentName");
        if (CommonUtils.isNotEmpty(id)) {
            queryBuilder.append("  and `id` <> :id");
        }
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("screenName", screenName);
        query.setString("documentName", documentName);
        if (CommonUtils.isNotEmpty(id)) {
            query.setLong("id", id);
        }
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public List<ResultModel> getDocumentTypes(String screenName, String ignoreDocumentName, String documentId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  s.`id` as id,");
        queryBuilder.append("  s.`screen_name` as screenName,");
        queryBuilder.append("  s.`document_name` as documentName ");
        if (CommonUtils.isNotEmpty(documentId)) {
            queryBuilder.append(" ,(select count(d.`document_id`) from `document_store_log` d where d.`screen_name` = s.`screen_name` and s.`document_name` = d.`document_name` and d.`document_id` = :documentId) as uploadCount ");
        }
        queryBuilder.append("from");
        queryBuilder.append("  `scan_config` s ");
        queryBuilder.append("where");
        if (CommonUtils.isNotEmpty(documentId)) {
            queryBuilder.append("  s.`screen_name` = :screenName ");
        } else {
            queryBuilder.append("  s.`screen_name` like :screenName ");
        }
        if (CommonUtils.isNotEmpty(ignoreDocumentName)) {
            queryBuilder.append("  and s.`document_name` <> :ignoreDocumentName ");
        }
        queryBuilder.append("order by s.`screen_name` asc, s.`document_name` asc");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        if (CommonUtils.isNotEmpty(documentId)) {
            query.setString("documentId", documentId);
            query.setString("screenName", screenName);
        } else {
            query.setString("screenName", screenName + "%");
        }
        if (CommonUtils.isNotEmpty(ignoreDocumentName)) {
            query.setString("ignoreDocumentName", ignoreDocumentName);
        }
        query.addScalar("id", IntegerType.INSTANCE);
        query.addScalar("screenName", StringType.INSTANCE);
        query.addScalar("documentName", StringType.INSTANCE);
        if (CommonUtils.isNotEmpty(documentId)) {
            query.addScalar("uploadCount", IntegerType.INSTANCE);
        }
        query.setResultTransformer(Transformers.aliasToBean(ResultModel.class));
        return query.list();
    }

    public List<ResultModel> getSopDocument(String screenName, String DocumentName, String documentId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  s.`id` as id,");
        queryBuilder.append("  s.`screen_name` as screenName,");
        queryBuilder.append("  s.`document_name` as documentName ");
        if (CommonUtils.isNotEmpty(documentId)) {
            queryBuilder.append(" ,(select count(d.`document_id`) from `document_store_log` d where d.`screen_name` = s.`screen_name` and s.`document_name` = d.`document_name` and d.`document_id` = :documentId) as uploadCount ");
        }
        queryBuilder.append("from");
        queryBuilder.append("  `scan_config` s ");
        queryBuilder.append("where");
        if (CommonUtils.isNotEmpty(documentId)) {
            queryBuilder.append("  s.`screen_name` = :screenName ");
        } else {
            queryBuilder.append("  s.`screen_name` like :screenName ");
        }
        if (CommonUtils.isNotEmpty(DocumentName)) {
            queryBuilder.append("  and s.`document_name` = :DocumentName ");
        }
        queryBuilder.append("order by s.`screen_name` asc, s.`document_name` asc");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        if (CommonUtils.isNotEmpty(documentId)) {
            query.setString("documentId", documentId);
            query.setString("screenName", screenName);
        } else {
            query.setString("screenName", screenName + "%");
        }
        if (CommonUtils.isNotEmpty(DocumentName)) {
            query.setString("DocumentName", DocumentName);
        }
        query.addScalar("id", IntegerType.INSTANCE);
        query.addScalar("screenName", StringType.INSTANCE);
        query.addScalar("documentName", StringType.INSTANCE);
        if (CommonUtils.isNotEmpty(documentId)) {
            query.addScalar("uploadCount", IntegerType.INSTANCE);
        }
        query.setResultTransformer(Transformers.aliasToBean(ResultModel.class));
        return query.list();
    }

    public List<ResultModel> getDocuments(String screenName, String documentName, String documentId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  `id` as id,");
        queryBuilder.append("  `document_name` as documentName,");
        queryBuilder.append("  `file_location` as fileLocation,");
        queryBuilder.append("  `file_name` as fileName,");
        queryBuilder.append("  `file_size` as fileSize,");
        queryBuilder.append("  `operation` as operation,");
        queryBuilder.append("  date_format(`date_opr_done`, '%d-%b-%Y %H:%i') as operationDate,");
        queryBuilder.append("  `status` as status,");
        queryBuilder.append("  `comments` as comments ");
        queryBuilder.append("from");
        queryBuilder.append("  `document_store_log` ");
        queryBuilder.append("where");
        queryBuilder.append("  `screen_name` = :screenName");
        if (CommonUtils.isNotEmpty(documentName)) {
            queryBuilder.append("  and `document_name` = :documentName");
        }
        queryBuilder.append("  and `document_id` = :documentId ");
        queryBuilder.append("order by `date_opr_done` desc");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("screenName", screenName);
        if (CommonUtils.isNotEmpty(documentName)) {
            query.setString("documentName", documentName);
        }
        query.setString("documentId", documentId);
        query.addScalar("id", IntegerType.INSTANCE);
        query.addScalar("documentName", StringType.INSTANCE);
        query.addScalar("fileLocation", StringType.INSTANCE);
        query.addScalar("fileName", StringType.INSTANCE);
        query.addScalar("fileSize", StringType.INSTANCE);
        query.addScalar("operation", StringType.INSTANCE);
        query.addScalar("operationDate", StringType.INSTANCE);
        query.addScalar("status", StringType.INSTANCE);
        query.addScalar("comments", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(ResultModel.class));
        return query.list();
    }
    public Boolean isScanDocument(String screenName, String fileNo,String documentName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  if(count(*) > 0, true, false) as result ");
        queryBuilder.append("from");
        queryBuilder.append("  `document_store_log` ");
        queryBuilder.append("where document_name=:documentName");
        queryBuilder.append(" and `screen_name` = :screenName");
        queryBuilder.append("  and `document_id` = :documentId");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("documentName", documentName);
        query.setString("screenName", screenName);
        query.setString("documentId", fileNo);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }
}

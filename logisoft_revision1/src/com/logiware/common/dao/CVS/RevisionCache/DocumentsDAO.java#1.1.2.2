package com.logiware.common.dao;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.logiware.common.model.ResultModel;
import java.util.List;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;

/**
 *
 * @author Lakshmi Narayanan
 */
public class DocumentsDAO extends BaseHibernateDAO {

    public List<ResultModel> getDocuments(String documentId, String documentName, String screenName) throws Exception {
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("select");
	queryBuilder.append("  document_name as documentName,");
	queryBuilder.append("  file_location as fileLocation,");
	queryBuilder.append("  file_name as fileName,");
	queryBuilder.append("  file_size as fileSize,");
	queryBuilder.append("  operation as operation,");
	queryBuilder.append("  date_format(date_opr_done, '%d-%b-%Y %H:%i') as operationDate,");
	queryBuilder.append("  status as status,");
	queryBuilder.append("  comments as comments");
	queryBuilder.append("  ");
	queryBuilder.append("from");
	queryBuilder.append("  document_store_log");
	queryBuilder.append("  ");
	queryBuilder.append("where document_id = '").append(documentId).append("'");
	queryBuilder.append("  and document_name = '").append(documentName).append("'");
	queryBuilder.append("  and screen_name = '").append(screenName).append("'");
	queryBuilder.append("  ");
	queryBuilder.append("order by date_opr_done DESC");
	SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
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
}

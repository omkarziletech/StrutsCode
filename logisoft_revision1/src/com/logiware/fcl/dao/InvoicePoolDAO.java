package com.logiware.fcl.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.logiware.fcl.form.InvoicePoolForm;
import com.logiware.fcl.model.ResultModel;
import java.util.List;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

/**
 *
 * @author Lakshmi Narayanan
 */
public class InvoicePoolDAO extends BaseHibernateDAO implements ConstantsInterface {

    private String buildSelectExpression() {
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("select");
	queryBuilder.append("  inv.invoice_number as invoiceNumber,");
	queryBuilder.append("  inv.file_no as fileNumber,");
	queryBuilder.append("  date_format(inv.date, '%d-%b-%Y') as createdDate,");
	queryBuilder.append("  date_format(inv.posted_date, '%d-%b-%Y') as postedDate,");
	queryBuilder.append("  format(inv.invoice_amount, 2) as invoiceAmount,");
	queryBuilder.append("  inv.customer_name as billToParty,");
	queryBuilder.append("  inv.invoice_by as user,");
	queryBuilder.append("  inv.notes as description,");
	queryBuilder.append("  if(");
	queryBuilder.append("    inv.status = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("',");
	queryBuilder.append("    'Posted',");
	queryBuilder.append("    'In Progress'");
	queryBuilder.append("  ) as status,");
	queryBuilder.append("  inv.id as id");
	return queryBuilder.toString();
    }

    private String buildWhereCondition(InvoicePoolForm form) throws Exception {
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("from");
	queryBuilder.append("  ar_red_invoice inv");
	queryBuilder.append("  ");
	queryBuilder.append("where");
	queryBuilder.append("  inv.file_no != ''");
	if (form.isImportFile()) {
	    queryBuilder.append("  and inv.file_type = 'I'");
	} else {
	    queryBuilder.append("  and (");
	    queryBuilder.append("    inv.file_type is null");
	    queryBuilder.append("    or inv.file_type != 'I'");
	    queryBuilder.append("  )");
	}
	if (CommonUtils.isNotEmpty(form.getInvoiceNumber())) {
	    queryBuilder.append("  and inv.invoice_number like '").append(form.getInvoiceNumber()).append("%'");
	}
	if (CommonUtils.isNotEmpty(form.getFileNumber())) {
	    queryBuilder.append("  and inv.file_no = '").append(form.getFileNumber()).append("'");
	}
	if (CommonUtils.isNotEmpty(form.getCustomerNumber())) {
	    queryBuilder.append("  and inv.customer_number = '").append(form.getCustomerNumber()).append("'");
	}
	if (CommonUtils.isEqualIgnoreCase(form.getStatus(), "Posted")) {
	    queryBuilder.append("  and inv.status = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
	} else if (CommonUtils.isEqualIgnoreCase(form.getStatus(), "Un Posted")) {
	    queryBuilder.append("  and (");
	    queryBuilder.append("    inv.status is null");
	    queryBuilder.append("    or inv.status != '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
	    queryBuilder.append("  )");
	}
	if (CommonUtils.isAllNotEmpty(form.getFromDate(), form.getToDate())) {
	    String fromDate = DateUtils.formatDate(DateUtils.parseDate(form.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
	    String toDate = DateUtils.formatDate(DateUtils.parseDate(form.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
	    queryBuilder.append("  and inv.date between '").append(fromDate).append("' and '").append(toDate).append("'");
	}
	return queryBuilder.toString();
    }

    private Integer getTotalRows(String whereCondition) throws Exception {
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("select");
	queryBuilder.append("  count(*) as result");
	queryBuilder.append("  ");
	queryBuilder.append(whereCondition);
	SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
	query.addScalar("result", IntegerType.INSTANCE);
	return (Integer) query.uniqueResult();
    }

    private List<ResultModel> getResults(String whereCondition, String sortBy, String orderBy, int start, int limit) throws Exception {
	String selectExpression = buildSelectExpression();
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append(selectExpression);
	queryBuilder.append("  ");
	queryBuilder.append(whereCondition);
	queryBuilder.append("  order by");
	if (CommonUtils.in(sortBy, "invoice_number", "file_no")) {
	    queryBuilder.append(" convert(inv.").append(sortBy).append(",unsigned integer)");
	} else {
	    queryBuilder.append(" inv.").append(sortBy);
	}
	queryBuilder.append(" ").append(orderBy);
	queryBuilder.append("  limit ").append(start).append(", ").append(limit);
	SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
	query.addScalar("invoiceNumber", StringType.INSTANCE);
	query.addScalar("fileNumber", StringType.INSTANCE);
	query.addScalar("createdDate", StringType.INSTANCE);
	query.addScalar("postedDate", StringType.INSTANCE);
	query.addScalar("invoiceAmount", StringType.INSTANCE);
	query.addScalar("billToParty", StringType.INSTANCE);
	query.addScalar("user", StringType.INSTANCE);
	query.addScalar("description", StringType.INSTANCE);
	query.addScalar("status", StringType.INSTANCE);
	query.addScalar("id", LongType.INSTANCE);
	query.setResultTransformer(Transformers.aliasToBean(ResultModel.class));
	return query.list();
    }

    public void search(InvoicePoolForm form) throws Exception {
	String whereCondition = buildWhereCondition(form);
	int totalRows = getTotalRows(whereCondition);
	if (totalRows > 0) {
	    form.setTotalRows(totalRows);
	    int limit = form.getLimit();
	    int totalPages = (totalRows / limit) + (totalRows % limit > 0 ? 1 : 0);
	    form.setTotalPages(totalPages);
	    int start = limit * (form.getSelectedPage() - 1);
	    String sortBy = (CommonUtils.isNotEmpty(form.getOrderByField())) ? form.getOrderByField() : form.getSortBy();
	    String orderBy = form.getOrderBy();
	    List<ResultModel> results = getResults(whereCondition, sortBy, orderBy, start, limit);
	    form.setResults(results);
	    form.setSelectedRows(results.size());
	}
    }
}

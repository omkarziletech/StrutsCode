/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.beans.ImportsManifestBean;
import com.gp.cvst.logisoft.struts.form.lcl.LclInvoicePoolForm;
import java.util.List;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

/**
 *
 * @author Meiyazhakan
 */
public class LclInvoicePoolDAO extends BaseHibernateDAO implements ConstantsInterface {

    public List<ImportsManifestBean> getARInvoiceResult(String fileType, LclInvoicePoolForm lclInvoicePoolForm) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(appendQuery());
        sb.append(appendWhereQuery(fileType, lclInvoicePoolForm));
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setResultTransformer(Transformers.aliasToBean(ImportsManifestBean.class));
        query.addScalar("invoiceNo", StringType.INSTANCE);
        query.addScalar("agentName", StringType.INSTANCE);
        query.addScalar("agentNo", StringType.INSTANCE);
        query.addScalar("fileNo", StringType.INSTANCE);
        query.addScalar("createdDate", StringType.INSTANCE);
        query.addScalar("postedDate", StringType.INSTANCE);
        query.addScalar("agentrelInv", StringType.INSTANCE);
        query.addScalar("createdUser", StringType.INSTANCE);
        query.addScalar("description", StringType.INSTANCE);
        query.addScalar("invoiceStatus", StringType.INSTANCE);
        query.addScalar("fileId", LongType.INSTANCE);
        query.addScalar("invoiceId", LongType.INSTANCE);
        query.addScalar("className", StringType.INSTANCE);
        return query.list();
    }

    public String appendQuery() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT  inv.invoice_number AS invoiceNo,inv.customer_name AS agentName,inv.customer_number AS agentNo,");
        sb.append("inv.bl_number AS fileNo,DATE_FORMAT(inv.date, '%d-%b-%Y') AS createdDate, DATE_FORMAT(inv.posted_date, '%d-%b-%Y') AS postedDate,");
        sb.append("FORMAT(inv.invoice_amount, 2) AS agentrelInv,inv.invoice_by AS createdUser,inv.notes AS description,");
        sb.append("IF(inv.status = 'AR', 'Posted', 'In Progress'  ) AS invoiceStatus,inv.file_no AS fileId,inv.id AS invoiceId, ");
        sb.append("inv.screen_name as className ");
        sb.append(" FROM  ar_red_invoice inv ");
        return sb.toString();
    }

    public String appendWhereQuery(String fileType, LclInvoicePoolForm lclInvoicePoolForm) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" WHERE inv.file_type='").append(fileType).append("'");
        if (CommonUtils.isNotEmpty(lclInvoicePoolForm.getInvoiceNumber())) {
            sb.append(" AND inv.invoice_number LIKE '").append(lclInvoicePoolForm.getInvoiceNumber()).append("%'");
        }
        if (CommonUtils.isNotEmpty(lclInvoicePoolForm.getCustomerNumber())) {
            sb.append(" AND inv.customer_number='").append(lclInvoicePoolForm.getCustomerNumber()).append("'");
        }
        if (CommonUtils.isNotEmpty(lclInvoicePoolForm.getFileNumber())) {
            sb.append(" AND inv.bl_number='").append(lclInvoicePoolForm.getFileNumber()).append("'");
        }
        if (CommonUtils.isEqualIgnoreCase(lclInvoicePoolForm.getStatus(), "Posted")) {
            sb.append("  and inv.status = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        } else if (CommonUtils.isEqualIgnoreCase(lclInvoicePoolForm.getStatus(), "Un Posted")) {
            sb.append("  and (");
            sb.append("    inv.status is null");
            sb.append("    or inv.status != '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
            sb.append("  )");
        }
        if (CommonUtils.isAllNotEmpty(lclInvoicePoolForm.getFromDate(), lclInvoicePoolForm.getToDate())) {
            String fromDate = DateUtils.formatDate(DateUtils.parseDate(lclInvoicePoolForm.getFromDate(), "dd-MMM-yyyy"), "yyyy-MM-dd");
	    String toDate = DateUtils.formatDate(DateUtils.parseDate(lclInvoicePoolForm.getToDate(), "dd-MMM-yyyy"), "yyyy-MM-dd");
            sb.append("  and inv.date between '").append(fromDate).append("' and '").append(toDate).append("'");
        }
        sb.append(" ORDER BY inv.id DESC ");

        return sb.toString();
    }
}

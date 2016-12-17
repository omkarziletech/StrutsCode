/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.struts.form.ArAccountNotesReportForm;
import com.logiware.bean.ReportBean;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author logiware
 */
public class ArReportsDAO extends BaseHibernateDAO implements ConstantsInterface {

    public List<ReportBean> getArAccountNotesList(ArAccountNotesReportForm arAccountNotesReportForm)throws Exception {
        List<ReportBean> arAccountNotesList = new ArrayList<ReportBean>();
        String fromDate = DateUtils.formatDate(DateUtils.parseDate(arAccountNotesReportForm.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd 00:00:00");
        String toDate = DateUtils.formatDate(DateUtils.parseDate(arAccountNotesReportForm.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tp.acct_name as customername,tp.acct_no as customernumber,n.note_desc as notesdescription,");
        queryBuilder.append("date_format(n.followup_date,'%m/%d/%Y') as followupdate,n.updated_by as createdby,");
        queryBuilder.append("date_format(n.updatedate,'%m/%d/%Y') as createddate");
        queryBuilder.append(" from notes n");
        queryBuilder.append(" join trading_partner tp on tp.acct_no=n.module_ref_id");
        if (CommonUtils.isNotEmpty(arAccountNotesReportForm.getAccountAssignedTo())) {
            queryBuilder.append(" join cust_accounting ca on ca.acct_no = tp.acct_no");
            queryBuilder.append(" join user_details user on user.user_id = ca.ar_contact_code");
            queryBuilder.append(" and user.login_name = '").append(arAccountNotesReportForm.getAccountAssignedTo()).append("'");
        } else if (CommonUtils.isNotEmpty(arAccountNotesReportForm.getNotesEnteredBy())) {
            queryBuilder.append(" join user_details user on user.login_name = n.updated_by");
            queryBuilder.append(" and user.login_name = '").append(arAccountNotesReportForm.getNotesEnteredBy()).append("'");
        } else {
            queryBuilder.append(" and tp.acct_no='").append(arAccountNotesReportForm.getCustomerNumber()).append("'");
        }
        queryBuilder.append(" where n.module_id='AR_CONFIGURATION'");
        queryBuilder.append(" and n.note_type='manual' and n.updated_by!='System'");
        queryBuilder.append(" and n.updateDate between '").append(fromDate).append("' and '").append(toDate).append("'");
        queryBuilder.append(" order by n.updateDate");
        Query query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        List<Object> result = query.list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            ReportBean reportBean = new ReportBean();
            reportBean.setCustomerName((String) col[0]);
            reportBean.setCustomerNumber((String) col[1]);
            reportBean.setNotesDescription((String) col[2]);
            reportBean.setFollowupDate((String) col[3]);
            reportBean.setCreatedBy((String) col[4]);
            reportBean.setCreatedDate((String) col[5]);
            arAccountNotesList.add(reportBean);
        }
        return arAccountNotesList;
    }
}

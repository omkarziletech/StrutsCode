package com.logiware.fcl.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.logiware.fcl.form.SsMasterDisputedBlForm;
import com.logiware.fcl.model.ResultModel;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;

/**
 *
 * @author Lakshmi Narayanan
 */
public class SsMasterDisputedBlDAO extends BaseHibernateDAO implements ConstantsInterface {

    private String buildOuterSelectExpression() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  f.file_no as fileNumber,");
        queryBuilder.append("  f.origin as origin,");
        queryBuilder.append("  f.destination as destination,");
        queryBuilder.append("  f.pol as pol,");
        queryBuilder.append("  f.pod as pod,");
        queryBuilder.append("  date_format(f.Date_Opr_done, '%d-%b-%Y') as dateOprDone,");
        queryBuilder.append("  date_format(f.eta, '%d-%b-%Y') as eta,");
        queryBuilder.append("  date_format(f.etd, '%d-%b-%Y') as etd,");
        queryBuilder.append("  f.ssline_name as sslineName,");
        queryBuilder.append("  f.ssline_bl as sslineBl,");
        queryBuilder.append("  f.comments as comments,");
        queryBuilder.append("  f.ack_comments as ackComments");
        return queryBuilder.toString();
    }

    private String buildSecondInnerSelectExpression() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("  ");
        queryBuilder.append("from");
        queryBuilder.append("  (select");
        queryBuilder.append("    f.file_no as file_no,");
        queryBuilder.append("    f.origin as origin,");
        queryBuilder.append("    f.destination as destination,");
        queryBuilder.append("    f.pol as pol,");
        queryBuilder.append("    f.pod as pod,");
        queryBuilder.append("    f.eta as eta,");
        queryBuilder.append("    f.etd as etd,");
        queryBuilder.append("    f.ssline_name as ssline_name,");
        queryBuilder.append("    f.ssline_bl as ssline_bl,");
        queryBuilder.append("    f.comments as comments,");
        queryBuilder.append("    f.ack_comments as ack_comments,");
        queryBuilder.append("    f.status as status,");
        queryBuilder.append("    f.Date_Opr_done as Date_Opr_done ");
        return queryBuilder.toString();
    }

    private String buildFirstInnerSelectExpression() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("  from");
        queryBuilder.append("    (select");
        queryBuilder.append("      f.file_no as file_no,");
        queryBuilder.append("      f.terminal as origin,");
        queryBuilder.append("      f.port as destination,");
        queryBuilder.append("      f.port_of_loading as pol,");
        queryBuilder.append("      f.portofdischarge as pod,");
        queryBuilder.append("      f.eta as eta,");
        queryBuilder.append("      f.sail_date as etd,");
        queryBuilder.append("      f.ssline_name as ssline_name,");
        queryBuilder.append("      f.steam_ship_bl as ssline_bl,");
        queryBuilder.append("      d.comments as comments,");
        queryBuilder.append("      d.ack_comments as ack_comments,");
        queryBuilder.append("      d.status as status,");
        queryBuilder.append("      d.Date_Opr_done as Date_Opr_done");
        return queryBuilder.toString();
    }

 private String buildFirstInnerWhereCondition(SsMasterDisputedBlForm form) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("    from");
        queryBuilder.append("      fcl_bl f");
        queryBuilder.append("      join document_store_log d");
        queryBuilder.append("        on (");
        queryBuilder.append("          f.file_no = d.document_id");
        queryBuilder.append("          and d.document_name = 'SS LINE MASTER BL'");
        queryBuilder.append("        )");
        queryBuilder.append("    where f.received_master = 'Yes'");
        queryBuilder.append("      and (");
        queryBuilder.append("        f.importFlag != 'I'");
        queryBuilder.append("        or f.importFlag is null");
        queryBuilder.append("      )");
        if (CommonUtils.isNotEmpty(form.getFileNumber())) {
            queryBuilder.append("      and f.file_no = '").append(form.getFileNumber().replaceAll("[^\\p{Digit}]+", "")).append("'");
        }
        if (CommonUtils.isNotEmpty(form.getOrigin())) {
            queryBuilder.append("      and f.terminal = '").append(form.getOrigin().replace("'", "\\'")).append("'");
        }
        if (CommonUtils.isNotEmpty(form.getDestination())) {
            queryBuilder.append("      and f.port = '").append(form.getDestination().replace("'", "\\'")).append("'");
        }
        if (CommonUtils.isNotEmpty(form.getPol())) {
            queryBuilder.append("      and f.port_of_loading = '").append(form.getPol().replace("'", "\\'")).append("'");
        }
        if (CommonUtils.isNotEmpty(form.getPod())) {
            queryBuilder.append("      and f.portofdischarge = '").append(form.getPod().replace("'", "\\'")).append("'");
        }
        if (CommonUtils.isNotEmpty(form.getSslineNumber())) {
            queryBuilder.append("      and f.ssline_no = '").append(form.getSslineNumber()).append("'");
        }
        if (CommonUtils.isNotEmpty(form.getEta())) {
            String eta = DateUtils.formatDate(DateUtils.parseDate(form.getEta(), "MM/dd/yyyy"), "yyyy-MM-dd");
            queryBuilder.append("      and f.eta = '").append(eta).append("'");
        }
        if (CommonUtils.isNotEmpty(form.getEtd())) {
            String etd = DateUtils.formatDate(DateUtils.parseDate(form.getEtd(), "MM/dd/yyyy"), "yyyy-MM-dd");
            queryBuilder.append("      and f.sail_date = '").append(etd).append("'");
        }
        if (CommonUtils.isNotEmpty(form.getSslBl())) {
            queryBuilder.append("      and f.steam_ship_bl = '").append(form.getSslBl()).append("'");
        }
        queryBuilder.append("    order by d.date_opr_done desc");
        queryBuilder.append("    ) as f");
        return queryBuilder.toString();
    }

    private String buildSecondInnerWhereCondition() throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("  group by f.file_no");
        queryBuilder.append("  ) as f");
        return queryBuilder.toString();
    }

private String buildOuterWhereCondition(SsMasterDisputedBlForm form) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("  ");
        queryBuilder.append("where status = 'Disputed'");
        queryBuilder.append("  ");
        queryBuilder.append("order by f.").append(form.getSortBy()).append(" ").append(form.getOrderBy());
        return queryBuilder.toString();
    }

    public void search(SsMasterDisputedBlForm form) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        String outerSelectExpression = buildOuterSelectExpression();
        String secondInnerSelectExpression = buildSecondInnerSelectExpression();
        String firstInnerSelectExpression = buildFirstInnerSelectExpression();
        String firstInnerWhereCondition = buildFirstInnerWhereCondition(form);
        String secondInnerWhereCondition = buildSecondInnerWhereCondition();
        String outerWhereCondition = buildOuterWhereCondition(form);
        queryBuilder.append(outerSelectExpression);
        queryBuilder.append(secondInnerSelectExpression);
        queryBuilder.append(firstInnerSelectExpression);
        queryBuilder.append(firstInnerWhereCondition);
        queryBuilder.append(secondInnerWhereCondition);
        queryBuilder.append(outerWhereCondition);
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("fileNumber", StringType.INSTANCE);
        query.addScalar("origin", StringType.INSTANCE);
        query.addScalar("destination", StringType.INSTANCE);
        query.addScalar("pol", StringType.INSTANCE);
        query.addScalar("pod", StringType.INSTANCE);
        query.addScalar("eta", StringType.INSTANCE);
        query.addScalar("etd", StringType.INSTANCE);
        query.addScalar("sslineName", StringType.INSTANCE);
        query.addScalar("sslineBl", StringType.INSTANCE);
        query.addScalar("comments", StringType.INSTANCE);
        query.addScalar("ackComments", StringType.INSTANCE);
        query.addScalar("dateOprDone", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(ResultModel.class));
        form.setResults(query.list());
    }
}

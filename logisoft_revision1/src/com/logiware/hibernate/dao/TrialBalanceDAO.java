package com.logiware.hibernate.dao;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.hibernate.dao.FiscalYearDAO;
import com.logiware.bean.ReportBean;
import java.util.List;
import org.hibernate.transform.Transformers;

/**
 *
 * @author Lakshmi Naryanan
 */
public class TrialBalanceDAO extends BaseHibernateDAO {

    private String buildQuery(String startAccount, String endAccount, String year, String period, boolean ecuReport) throws Exception {
        Integer lastYear = Integer.parseInt(year) - 1;
        String status = new FiscalYearDAO().getFiscalYearStatus(lastYear.toString());
        StringBuilder queryBuilder = new StringBuilder();
        if (ecuReport) {
            queryBuilder.append("select");
            queryBuilder.append("  t1.report_category as reportCategory,");
            queryBuilder.append("  t1.account_type as accountType,");
            queryBuilder.append("  format(if(t1.balance >= 0, t1.balance, 0), 2) as debit,");
            queryBuilder.append("  format(if(t1.balance < 0, -t1.balance, 0), 2) as credit ");
            queryBuilder.append("from");
            queryBuilder.append("  (");
            queryBuilder.append("    select");
            queryBuilder.append("      t2.report_category as report_category,");
            queryBuilder.append("      t2.account_type as account_type,");
            queryBuilder.append("      sum(if(t2.balance <> 0.00, t2.balance, 0.00)) as balance");
            queryBuilder.append("    from");
            queryBuilder.append("      (");
            queryBuilder.append("        (select");
            queryBuilder.append("           ecu.report_category as report_category,");
            queryBuilder.append("           ecu.account_type as account_type,");
            queryBuilder.append("           sum(ab.total_debit - ab.toatl_credit) as balance");
            queryBuilder.append("         from");
            queryBuilder.append("           ecu_account_mapping ecu");
            queryBuilder.append("           left join account_details ad");
            queryBuilder.append("             on (ecu.report_category = ad.report_category)");
            queryBuilder.append("           left join account_balance ab");
            queryBuilder.append("             on (");
            queryBuilder.append("               ad.account = ab.account");
            if (CommonUtils.isEqualIgnoreCase(status, CommonConstants.STATUS_OPEN)) {
                queryBuilder.append("               and (");
                queryBuilder.append("                 (year = ").append(lastYear).append(" and period <= '12')");
                queryBuilder.append("                 or (year = ").append(year).append(" and period <= '").append(period).append("')");
                queryBuilder.append("               )");
                lastYear--;
            } else {
                queryBuilder.append("             and period <= '").append(period).append("'");
                queryBuilder.append("             and year = '").append(year).append("'");
            }
            queryBuilder.append("             )");
            if (CommonUtils.isNotEmpty(startAccount) && CommonUtils.isNotEmpty(endAccount)) {
                queryBuilder.append("         where");
                queryBuilder.append("           ecu.report_category between '").append(startAccount).append("' and '").append(endAccount).append("'");
            }
            queryBuilder.append("         group by ecu.report_category");
            queryBuilder.append("        )");
            queryBuilder.append("        union ");
            queryBuilder.append("        (select");
            queryBuilder.append("           ecu.report_category as report_category,");
            queryBuilder.append("           ecu.account_type as account_type,");
            queryBuilder.append("           ayeb.closing_balance as balance");
            queryBuilder.append("         from");
            queryBuilder.append("           ecu_account_mapping ecu");
            queryBuilder.append("           left join account_details ad");
            queryBuilder.append("             on (ecu.report_category = ad.report_category)");
            queryBuilder.append("           left join account_year_end_balance ayeb");
            queryBuilder.append("             on (");
            queryBuilder.append("               ad.account = ayeb.account");
            queryBuilder.append("               and ayeb.year = ").append(lastYear);
            queryBuilder.append("             )");
            if (CommonUtils.isNotEmpty(startAccount) && CommonUtils.isNotEmpty(endAccount)) {
                queryBuilder.append("         where");
                queryBuilder.append("           ecu.report_category between '").append(startAccount).append("' and '").append(endAccount).append("'");
            }
            queryBuilder.append("         order by ecu.report_category");
            queryBuilder.append("        )");
            queryBuilder.append("      ) as t2");
            queryBuilder.append("    group by t2.report_category");
            queryBuilder.append("  ) as t1 ");
            queryBuilder.append("order by t1.report_category");
        } else {
            queryBuilder.append("select");
            queryBuilder.append("  tb.account as account,");
            queryBuilder.append("  tb.description as description,");
            queryBuilder.append("  format(if(sum(tb.balance) > 0, sum(tb.balance), 0), 2) as debit,");
            queryBuilder.append("  format(if(sum(tb.balance) < 0, -sum(tb.balance), 0), 2) as credit ");
            queryBuilder.append("from");
            queryBuilder.append("  (");
            queryBuilder.append("    (select");
            queryBuilder.append("       ad.account as account,");
            queryBuilder.append("       ad.acct_desc as description,");
            queryBuilder.append("       sum(ab.total_debit - ab.toatl_credit) as balance");
            queryBuilder.append("     from");
            queryBuilder.append("       account_details ad");
            queryBuilder.append("       left join account_balance ab");
            queryBuilder.append("         on (");
            queryBuilder.append("           ad.account = ab.account");
            if (CommonUtils.isEqualIgnoreCase(status, CommonConstants.STATUS_OPEN)) {
                queryBuilder.append("           and (");
                queryBuilder.append("             (year = ").append(lastYear).append(" and period <= '12')");
                queryBuilder.append("             or (year = ").append(year).append(" and period <= '").append(period).append("')");
                queryBuilder.append("           )");
                lastYear--;
            } else {
                queryBuilder.append("           and period <= '").append(period).append("'");
                queryBuilder.append("           and year = '").append(year).append("'");
            }
            queryBuilder.append("         )");
            if (CommonUtils.isNotEmpty(startAccount) && CommonUtils.isNotEmpty(endAccount)) {
                queryBuilder.append("     where");
                queryBuilder.append("       ad.account between '").append(startAccount).append("' and '").append(endAccount).append("'");
            }
            queryBuilder.append("     group by ad.account");
            queryBuilder.append("    )");
            queryBuilder.append("    union ");
            queryBuilder.append("    (select");
            queryBuilder.append("       ad.account as account,");
            queryBuilder.append("       ad.acct_desc as description,");
            queryBuilder.append("       ayeb.closing_balance as balance");
            queryBuilder.append("     from");
            queryBuilder.append("       account_details ad");
            queryBuilder.append("       join account_year_end_balance ayeb");
            queryBuilder.append("         on (");
            queryBuilder.append("           ad.account = ayeb.account");
            queryBuilder.append("           and ayeb.year = ").append(lastYear);
            queryBuilder.append("         )");
            if (CommonUtils.isNotEmpty(startAccount) && CommonUtils.isNotEmpty(endAccount)) {
                queryBuilder.append("     where");
                queryBuilder.append("       ad.account between '").append(startAccount).append("' and '").append(endAccount).append("'");
            }
            queryBuilder.append("     order by ad.account");
            queryBuilder.append("    )");
            queryBuilder.append("  ) as tb ");
            queryBuilder.append("group by tb.account");
        }
        return queryBuilder.toString();
    }

    public List getTrialBalances(String startAccount, String endAccount, String year, String period, boolean reportType) throws Exception {
        String query = buildQuery(startAccount, endAccount, year, period, reportType);
        return getCurrentSession().createSQLQuery(query).setResultTransformer(Transformers.aliasToBean(ReportBean.class)).list();
    }
}

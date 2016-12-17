package com.logiware.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.logiware.form.TruckerRatesForm;
import com.logiware.hibernate.domain.TruckerRates;
import com.logiware.ims.client.IMSQuote;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

/**
 *
 * @author Lakshmi Narayanan
 */
public class TruckerRatesDAO extends BaseHibernateDAO {

    public void truncate() throws Exception {
	String queryString = "truncate table trucker_rates";
	getCurrentSession().createSQLQuery(queryString).executeUpdate();
	getCurrentSession().flush();
    }
    
    public int loadCsv(String filePath) throws Exception {
        StringBuilder queryBuilder=new StringBuilder();
        queryBuilder.append("load data local infile '").append(filePath).append("' into table trucker_rates character set");
        queryBuilder.append("'latin1' fields terminated by ',' optionally enclosed by '\"' lines terminated ");
        queryBuilder.append("by '\\r\\n' ignore 1 lines (@trucker_name, @trucker_number, @from_zip_code, @from_city,");
        queryBuilder.append("@from_state, @to_port_code, @rate, @fuel, @buy, @haz, @markup, @sell) ");
        queryBuilder.append("set trucker_name = trim(@trucker_name),");
        queryBuilder.append("trucker_number = trim(@trucker_number),");
        queryBuilder.append("from_zip_code = trim(@from_zip_code),");
        queryBuilder.append("from_city = trim(@from_city),");
        queryBuilder.append("from_state = trim(@from_state),");
        queryBuilder.append("to_port_code = trim(@to_port_code),");
        queryBuilder.append("rate = trim(replace(replace(replace(@rate, '$', ''), ' ', ''),',','')),");
        queryBuilder.append("fuel = trim(replace(replace(replace(@fuel, '$', ''), ' ', ''),',','')),");
        queryBuilder.append("buy = trim(replace(replace(replace(@buy, '$', ''), ' ', ''),',','')),");
        queryBuilder.append("haz = trim(replace(replace(replace(@haz, '$', ''), ' ', ''),',','')),");
        queryBuilder.append("markup = trim(replace(replace(replace(@markup, '%', ''), ' ', ''),',','')),");
        queryBuilder.append("sell = trim(replace(replace(replace(@sell, '$', ''), ' ', ''),',',''))");
	return getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }
    
    public void save(TruckerRates truckerRates) throws Exception {
        getSession().save(truckerRates);
        getSession().flush();
    }

    public void validateTruckerRates() throws Exception {
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("update");
	queryBuilder.append("  trucker_rates tr");
	queryBuilder.append("  join trading_partner tp");
	queryBuilder.append("    on tr.trucker_number = tp.acct_no ");
	queryBuilder.append("set tr.trucker = tp.acct_no");
	getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();

	queryBuilder.delete(0, queryBuilder.length());
	queryBuilder.append("update");
	queryBuilder.append("  trucker_rates tr");
	queryBuilder.append("  join zip_code zc");
	queryBuilder.append("    on tr.from_zip_code = zc.zip ");
	queryBuilder.append("set tr.from_zip = zc.zip");
	getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();

	queryBuilder.delete(0, queryBuilder.length());
	queryBuilder.append("update");
	queryBuilder.append("  trucker_rates tr");
	queryBuilder.append("  join un_location ul");
	queryBuilder.append("    on tr.to_port_code = ul.un_loc_code ");
	queryBuilder.append("set tr.to_port = ul.id");
	getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    private String buildSelectExpression() {
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("select");
	queryBuilder.append("  tp.acct_name as truckerName,");
	queryBuilder.append("  tp.acct_no as truckerNumber,");
	queryBuilder.append("  tr.from_zip as fromZip,");
	queryBuilder.append("  tr.from_city as fromCity,");
	queryBuilder.append("  tr.from_state as fromState,");
	queryBuilder.append("  un.un_loc_code as toPortCode,");
	queryBuilder.append("  concat('$',format(tr.rate,2)) as rateValue,");
	queryBuilder.append("  concat('$',format(tr.fuel,2)) as fuelValue,");
	queryBuilder.append("  concat('$',format(tr.buy,2)) as buyValue,");
	queryBuilder.append("  concat('$',format(tr.haz,2)) AS hazValue,");
	queryBuilder.append("  concat(cast(tr.markup as signed),'%') as markup,");
	queryBuilder.append("  concat('$',format(tr.sell,2)) as sellValue,");
	queryBuilder.append("  tr.id as id");
	return queryBuilder.toString();
    }

    private String buildWhereCondition(TruckerRatesForm form) throws Exception {
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("where");
	queryBuilder.append("  (");
	if (form.isErrorRates()) {
	    queryBuilder.append("    (tr.trucker is null or tr.trucker = '')");
	    queryBuilder.append("    or (tr.from_zip is null or tr.from_zip = '')");
	    queryBuilder.append("    or (tr.to_port is null or tr.to_port = '')");
	    queryBuilder.append("    or (char_length(tr.from_state)>2) ");
	} else {
	    if (CommonUtils.isNotEmpty(form.getTruckerRates().getTruckerNumber())) {
		queryBuilder.append("    tr.trucker = '").append(form.getTruckerRates().getTruckerNumber()).append("'");
	    } else {
		queryBuilder.append("    tr.trucker != ''");
	    }
	    if (CommonUtils.isNotEmpty(form.getTruckerRates().getFromZip())) {
		queryBuilder.append("    and tr.from_zip = '").append(form.getTruckerRates().getFromZip()).append("'");
	    } else {
		queryBuilder.append("    and tr.from_zip != ''");
	    }
	    if (CommonUtils.isNotEmpty(form.getTruckerRates().getToPort())) {
		queryBuilder.append("    and tr.to_port = '").append(form.getTruckerRates().getToPort()).append("'");
	    } else {
		queryBuilder.append("    and tr.to_port != ''");
	    }
		queryBuilder.append("    and char_length(tr.from_state)=2");
	}
	queryBuilder.append("  )");
	return queryBuilder.toString();
    }

    private Integer getTotalRows(String whereCondition) throws Exception {
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("select");
	queryBuilder.append("  count(tr.id) as result ");
	queryBuilder.append("from");
	queryBuilder.append("  trucker_rates tr");
	queryBuilder.append("  ");
	queryBuilder.append(whereCondition);
	Object count = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
	return null != count ? Integer.parseInt(count.toString()) : 0;
    }

    private List<TruckerRates> getTruckerRates(String whereCondition, String sortBy, String orderBy, Integer start, Integer limit) throws Exception {
	StringBuilder queryBuilder = new StringBuilder();
	String selectExpression = buildSelectExpression();
	queryBuilder.append(selectExpression);
	queryBuilder.append("  ");
	queryBuilder.append("from");
	queryBuilder.append("  trucker_rates tr");
	queryBuilder.append("  left join trading_partner tp");
	queryBuilder.append("    on (tr.trucker = tp.acct_no)");
	queryBuilder.append("  left join un_location un");
	queryBuilder.append("    on (tr.to_port = un.id)");
	queryBuilder.append("  ");
	queryBuilder.append(whereCondition);
	queryBuilder.append("  ");
	queryBuilder.append("group by tr.id ");
	queryBuilder.append("order by ").append(sortBy).append(" ").append(orderBy).append(" ");
	queryBuilder.append("limit ").append(start).append(",").append(limit);
	SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
	query.addScalar("truckerName", StringType.INSTANCE);
	query.addScalar("truckerNumber", StringType.INSTANCE);
	query.addScalar("fromZip", StringType.INSTANCE);
	query.addScalar("fromCity", StringType.INSTANCE);
	query.addScalar("fromState", StringType.INSTANCE);
	query.addScalar("toPortCode", StringType.INSTANCE);
	query.addScalar("rateValue", StringType.INSTANCE);
	query.addScalar("fuelValue", StringType.INSTANCE);
	query.addScalar("buyValue", StringType.INSTANCE);
	query.addScalar("hazValue", StringType.INSTANCE);
	query.addScalar("markup", StringType.INSTANCE);
	query.addScalar("sellValue", StringType.INSTANCE);
	query.addScalar("id", LongType.INSTANCE);
	query.setResultTransformer(Transformers.aliasToBean(TruckerRates.class));
	return query.list();
    }

    public void search(TruckerRatesForm form) throws Exception {
	String whereCondition = buildWhereCondition(form);
	int totalRows = getTotalRows(whereCondition);
	if (totalRows > 0) {
	    form.setTotalRows(totalRows);
	    Integer limit = form.getLimit();
	    Integer totalPages = (totalRows / limit) + (totalRows % limit > 0 ? 1 : 0);
	    form.setTotalPages(totalPages);
	    Integer start = limit * (form.getSelectedPage() - 1);
	    List<TruckerRates> truckerRatesList = getTruckerRates(whereCondition, form.getSortBy(), form.getOrderBy(), start, limit);
	    form.setTruckerRatesList(truckerRatesList);
	    form.setSelectedRows(truckerRatesList.size());
	}
    }

    public void update(Serializable id, Map<Serializable, Serializable> fields) throws Exception {
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("update");
	queryBuilder.append("  trucker_rates ");
	queryBuilder.append("set");
	int rowCount = 0;
	for (Serializable key : fields.keySet()) {
	    Serializable value = fields.get(key);
	    queryBuilder.append("  ").append(key);
	    if (null == value) {
		queryBuilder.append(" = null");
	    } else {
		queryBuilder.append(" = '").append(value).append("'");
	    }
	    if (rowCount < fields.size() - 1) {
		queryBuilder.append(",");
	    }
	    rowCount++;
	}
	queryBuilder.append("  where id = ").append(id);
	getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public List<IMSQuote> getTruckerRates(String fromZip, String unlocationCode, String portName, boolean isHaz,String alternatePortName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("select tr.trucker as trucker,");
	queryBuilder.append("tp.acct_name as truckerName,");
	queryBuilder.append("replace(format(tr.rate,2),',','') as quoteAmount,");
	queryBuilder.append("replace(format(tr.fuel,2),',','') as fuelFees,");
	if (isHaz) {
	    queryBuilder.append("replace(format((tr.buy+tr.haz),2),',','') as allInRate,");
	} else {
	    queryBuilder.append("replace(format(tr.buy,2),',','') as allInRate,");
	}
	queryBuilder.append("replace(format(tr.rate + (tr.rate * tr.markup / 100),2),',','') as quote2Amt,");
	queryBuilder.append("replace(format(tr.fuel + (tr.fuel * tr.markup / 100),2),',','') as fuel2Fees,");
	if (isHaz) {
	    queryBuilder.append("replace(format((tr.sell+tr.haz),2),',','') as allIn2Rate,");
	} else {
	    queryBuilder.append("replace(format(tr.sell,2),',','') as allIn2Rate,");
	}
	queryBuilder.append("'").append(portName).append("' as originName,");
	queryBuilder.append("upper('").append(portName).append("') as emptyName");
	if (isHaz) {
	    queryBuilder.append(",replace(format(tr.haz,2),',','') as hazardousFees");
	}
	queryBuilder.append(" from trucker_rates tr");
	queryBuilder.append(" join un_location un");
	queryBuilder.append(" on (tr.to_port = un.id");
        if(CommonUtils.isNotEmpty(alternatePortName)){
            queryBuilder.append(" and un.alternate_port_name = '").append(alternatePortName).append("')");
        }else{
            queryBuilder.append(" and un.un_loc_code = '").append(unlocationCode).append("')");
        }
	queryBuilder.append(" join trading_partner tp");
	queryBuilder.append(" on (tp.acct_no=tr.trucker)");
	queryBuilder.append(" where tr.from_zip = '").append(fromZip).append("'");
        if(isHaz){
            queryBuilder.append(" and (tr.haz is not null and tr.haz <> '' and tr.haz <> 0.00)");
        }
	SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
	query.setResultTransformer(Transformers.aliasToBean(IMSQuote.class));
	return query.list();
    }
}

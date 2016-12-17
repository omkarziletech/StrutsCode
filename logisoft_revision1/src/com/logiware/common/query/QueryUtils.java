package com.logiware.common.query;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.hibernate.SQLQuery;

/**
 *
 * @author Lakshmi Narayanan
 */
public class QueryUtils {

    public static List<Object> getResults(String property, String param, Map paramMap) throws Exception {
        String queryString = QueryProperties.getProperty(property);
        List<String> params = new ArrayList<String>();
        params.add(param.trim());
        for (Object k : paramMap.keySet()) {
            if (k.toString().contains("param")) {
                params.add(((String[]) paramMap.get(k))[0].trim());
            }
        }
        
        List<String> values = new ArrayList<String>();
        Pattern pattern = Pattern.compile("\\{(.*?)\\}");
        Matcher matcher = pattern.matcher(queryString);
        int valueCount = 0;
        int paramCount;
        while (matcher.find()) {
            paramCount = Integer.parseInt(matcher.group(1));
            queryString = queryString.replaceFirst("\\{" + paramCount + "\\}", "?" + valueCount);
            if (queryString.contains("?" + valueCount + "%")) {
                queryString = queryString.replaceFirst("%", "");
                values.add(params.get(paramCount) + "%");
            } else {
                values.add(params.get(paramCount));
            }
            valueCount++;
        }
        SQLQuery query = new BaseHibernateDAO().getCurrentSession().createSQLQuery(queryString);
        valueCount = 0;
        for (String value : values) {
            query.setParameter(""+valueCount, value);
            valueCount++;
        }
        query.setMaxResults(50);
        return query.list();
    }
}

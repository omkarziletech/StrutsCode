package com.logiware.accounting.utils;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.logiware.accounting.properties.QueryProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
            if (NumberUtils.isNumber(matcher.group(1))) {
                paramCount = Integer.parseInt(matcher.group(1));
                queryString = queryString.replaceFirst("\\{" + paramCount + "\\}", "?" + valueCount);
                if (queryString.contains("?" + valueCount + "%")) {
                    queryString = queryString.replaceFirst("\\?" + valueCount + "%", "\\?" + valueCount);
                    values.add(params.get(paramCount) + "%");
                } else {
                    values.add(params.get(paramCount));
                }
                valueCount++;
            }
        }
        SQLQuery query = new BaseHibernateDAO().getCurrentSession().createSQLQuery(queryString);
        valueCount = 0;
        for (String value : values) {
            query.setParameter("" + valueCount, value);
            valueCount++;
        }
        query.setMaxResults(50);
        return query.list();
    }
}

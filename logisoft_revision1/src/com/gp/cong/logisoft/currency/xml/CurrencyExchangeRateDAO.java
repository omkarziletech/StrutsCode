/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.currency.xml;

import com.gp.cong.logisoft.currency.xml.CurrencyXml.Results.Rate;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import org.hibernate.SQLQuery;

/**
 *
 * @author Mei
 */
public class CurrencyExchangeRateDAO extends BaseHibernateDAO {

    public void insertCurrency(Rate rates) {
        String strQuery = "INSERT INTO currency_exchange_rate (currency_name, currency_rate, ask, bid, currency_date, currency_time)";
        strQuery += "VALUES(:currencyName,:currencyRate,:ask,:bid,:currencyDate,:currencyTime)";
        SQLQuery query = getCurrentSession().createSQLQuery(strQuery);
        query.setParameter("currencyName", rates.getName());
        query.setParameter("currencyRate", rates.getRate());
        query.setParameter("ask", rates.getAsk());
        query.setParameter("bid", rates.getBid());
        query.setParameter("currencyDate", rates.getDate());
        query.setParameter("currencyTime", rates.getTime());
        query.executeUpdate();
    }

    public void deleteCurrency() {
        String strQuery = "DELETE FROM currency_exchange_rate";
        SQLQuery query = getCurrentSession().createSQLQuery(strQuery);
        query.executeUpdate();

    }
}

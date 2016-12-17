/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.LclQuoteHotCode;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.type.BooleanType;

public class LclQuoteHotCodeDAO extends BaseHibernateDAO<LclQuoteHotCode> {

    public LclQuoteHotCodeDAO() {
        super(LclQuoteHotCode.class);
    }

    public boolean isHotCodeExist(String Code, String fileId) throws Exception {
        SQLQuery query = getCurrentSession().createSQLQuery("select if(count(*)<1,true,false) as result from lcl_quote_hot_code "
                + " where code=:code and file_number_id=:fileId");
        query.setParameter("fileId", fileId);
        query.setParameter("code", Code);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public void insertQuery(String fileId, String code, Integer user) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into lcl_quote_hot_code(file_number_id,code,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id) ");
        sb.append(" values(:fileId,:code,:now,:user,:now,:user)");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("fileId", fileId);
        query.setParameter("code", code);
        query.setParameter("now", new Date());
        query.setParameter("user", user);
        query.executeUpdate();
    }

    public List<LclQuoteHotCode> getHotCodeList(Long fileId) throws Exception {
        Query query = getCurrentSession().createQuery(" from LclQuoteHotCode where lclFileNumber.id=" + fileId);
        return query.list();
    }

    public LclQuoteHotCode getQuoteHotCode(Long fileId, String code) throws Exception {
        Query query = getCurrentSession().createQuery(" from LclQuoteHotCode where lclFileNumber.id=:fileId and code=:code");
        query.setParameter("fileId", fileId);
        query.setParameter("code", code);
        return (LclQuoteHotCode) query.uniqueResult();
    }

}

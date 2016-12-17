/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.logisoft.domain.lcl.TpNote;
import com.gp.cong.hibernate.BaseHibernateDAO;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author PALRAJ
 */
public class TpNoteDao extends BaseHibernateDAO<TpNote> {
    
    public TpNoteDao() {
        super(TpNote.class);
    }

    public List getAcctNo(String AcctNo) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  eci_acct_no,");
        sb.append("  ecifwno ");
        sb.append("FROM");
        sb.append("  `trading_partner` ");
        sb.append("WHERE acct_no='").append(AcctNo).append("'");
        Query queryObject= getCurrentSession().createSQLQuery(sb.toString());
        List list=queryObject.list();
        return list;
    }
}

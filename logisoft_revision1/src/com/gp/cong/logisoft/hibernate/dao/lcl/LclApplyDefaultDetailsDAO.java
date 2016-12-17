/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.LclApplyDefaultDetails;
import java.util.ArrayList;
import java.util.List;
import org.apache.struts.util.LabelValueBean;
import org.hibernate.SQLQuery;

/**
 *
 * @author aravindhan.v
 */
public class LclApplyDefaultDetailsDAO extends BaseHibernateDAO<LclApplyDefaultDetails> {

    public LclApplyDefaultDetailsDAO() {
        super(LclApplyDefaultDetails.class);
    }

    public void save(LclApplyDefaultDetails lclUserDefaults) throws Exception {
        getCurrentSession().save(lclUserDefaults);
    }

    public void update(LclApplyDefaultDetails lclUserDefaults) throws Exception {
        getSession().update(lclUserDefaults);
    }

    public void saveOrUpdate(LclApplyDefaultDetails lclUserDefaults) throws Exception {
        getSession().saveOrUpdate(lclUserDefaults);
    }

    public List getLclDefaultNameList(Integer userId) throws Exception {
        List<LabelValueBean> applyDefaultList = new ArrayList<LabelValueBean>();
        SQLQuery query = getCurrentSession().createSQLQuery("select id, upper(apply_default_name) from lcl_apply_default_details where user_id =:userId");
        query.setInteger("userId", userId);
        List<Object[]> resultList = query.list();
        for (Object[] row : resultList) {
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            if (null != row[1] && null != row[0]) {
                applyDefaultList.add(new LabelValueBean(cname, cid.toString()));
            }
        }
        return applyDefaultList;
    }
    
}

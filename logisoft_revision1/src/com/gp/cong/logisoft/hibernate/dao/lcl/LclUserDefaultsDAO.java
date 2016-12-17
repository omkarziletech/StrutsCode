/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.LclSearchTemplate;
import com.gp.cong.logisoft.domain.lcl.LclUserDefaults;
import java.util.ArrayList;
import java.util.List;
import org.apache.struts.util.LabelValueBean;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;

public class LclUserDefaultsDAO extends BaseHibernateDAO<LclUserDefaults> {

    public LclUserDefaultsDAO() {
        super(LclUserDefaults.class);
    }

    public void save(LclUserDefaults lclUserDefaults) throws Exception {
        getCurrentSession().save(lclUserDefaults);
    }

    public void update(LclUserDefaults lclUserDefaults) throws Exception {
        getSession().update(lclUserDefaults);
    }

    public void saveOrUpdate(LclUserDefaults lclUserDefaults) throws Exception {
        getSession().saveOrUpdate(lclUserDefaults);
    }

    public LclUserDefaults getLclUserDefaultById(Integer userId) throws Exception {
        Criteria criteria = getSession().createCriteria(LclUserDefaults.class, "lclUserDefaults");
        criteria.createAlias("lclUserDefaults.userDetails", "userDetails");
        if (!CommonUtils.isEmpty(userId)) {
            criteria.add(Restrictions.eq("userDetails.id", userId));
        }
        return (LclUserDefaults) criteria.setMaxResults(1).uniqueResult();
    }

    public List getLclDefaultNameList(Integer userId) throws Exception {
        List<LabelValueBean> applyDefaultList = new ArrayList<LabelValueBean>();
        SQLQuery query = getCurrentSession().createSQLQuery("select id,apply_default_name from lcl_user_defaults where user_details_id =:userId");
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

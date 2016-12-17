package com.logiware.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.logiware.hibernate.domain.AchProcessHistory;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Lakshminarayanan
 */
public class AchProcessDAO extends BaseHibernateDAO {

    public void insertAchProcessHistory(AchProcessHistory achProcessHistory) throws Exception {
        getCurrentSession().save(achProcessHistory);
        getCurrentSession().flush();
    }

    public void updateAchProcessHistory(AchProcessHistory achProcessHistory) throws Exception {
	getCurrentSession().update(achProcessHistory);
	getCurrentSession().flush();
    }

    public void removeAchProcessHistory(AchProcessHistory achProcessHistory) throws Exception {
	getCurrentSession().delete(achProcessHistory);
	getCurrentSession().flush();
    }

    public AchProcessHistory findAchProcessHistoryById(Integer id) throws Exception {
	AchProcessHistory instance = (AchProcessHistory) getCurrentSession().get(
		"com.logiware.hibernate.domain.AchProcessHistory", id);
	return instance;
    }

    public List findByProperties(List<String> properties, List<String> operators, List<Object> value) throws Exception {
	Criteria criteria = getSession().createCriteria(AchProcessHistory.class);
	int i = 0;
	for (String property : properties) {
	    if (CommonUtils.isEqual(operators.get(i), "=")) {
		criteria.add(Restrictions.eq(property, value.get(i)));
	    }
	    if (CommonUtils.isEqual(operators.get(i), "like")) {
		criteria.add(Restrictions.like(property, "%" + value.get(i) + "%"));
	    }
	    if (CommonUtils.isEqual(operators.get(i), "<")) {
		criteria.add(Restrictions.lt(property, value.get(i)));
	    }
	    if (CommonUtils.isEqual(operators.get(i), ">")) {
		criteria.add(Restrictions.gt(property, value.get(i)));
	    }
	    if (CommonUtils.isEqual(operators.get(i), "<=")) {
		criteria.add(Restrictions.le(property, value.get(i)));
	    }
	    if (CommonUtils.isEqual(operators.get(i), ">=")) {
		criteria.add(Restrictions.ge(property, value.get(i)));
	    }
	    if (CommonUtils.isEqual(operators.get(i), "!=") || CommonUtils.isEqual(operators.get(i), "<>")) {
		criteria.add(Restrictions.ne(property, value.get(i)));
	    }
	    i++;
	}
	criteria.addOrder(Order.desc("processId"));
	return criteria.list();
    }
}

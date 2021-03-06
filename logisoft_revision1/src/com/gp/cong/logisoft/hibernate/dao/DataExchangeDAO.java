package com.gp.cong.logisoft.hibernate.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import com.gp.cong.logisoft.domain.DataExchangeUsecases;
import com.gp.cong.logisoft.domain.Role;
import com.gp.cong.logisoft.domain.Usecases;

public class DataExchangeDAO extends BaseHibernateDAO {

    public void save(DataExchangeUsecases transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
        getCurrentSession().flush();
    }

    public Iterator getAllUsecasesForDisplay()throws Exception {
        Iterator results = null;
            results = getCurrentSession().createQuery("select usecaseId,usecaseCode from Usecases").list().iterator();
        return results;
    }

    public DataExchangeUsecases findById(Integer id)throws Exception {
            DataExchangeUsecases instance = (DataExchangeUsecases) getCurrentSession().get("com.gp.cong.logisoft.domain.DataExchangeUsecases", id);
            return instance;
    }

    public List findRoleName(String roleName)throws Exception {
        List list = new ArrayList();
            String queryString = "from Role where roleDesc=?0";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", roleName);
            list = queryObject.list();
        return list;
    }

    public List findAllDataExchange()throws Exception {
            String queryString = "from DataExchangeUsecases order by id";
            Query queryObject = getCurrentSession().createQuery(queryString);
            return queryObject.list();
    }

    public Iterator findAllFlowFrom(Usecases usecase)throws Exception {
            String queryString = " select flowFrom from DataExchangeUsecases where usecaseId=?0";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", usecase);
            return queryObject.list().iterator();
    }

    public List findForManagement(String roleDesc, Date roleCreatedDate)throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(Role.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (roleDesc != null && !roleDesc.equals("")) {
                criteria.add(Restrictions.like("roleDesc", roleDesc + "%"));
            }
            if (roleCreatedDate != null && !roleCreatedDate.equals("")) {
                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    String strSODate = dateFormat.format(roleCreatedDate);
                    //criteria.add(Restrictions.eq("logisticOrderDate", strSODate));
                    Date soStartDate = (Date) dateFormat.parse(strSODate);
                    Date soEndDate = new Date(soStartDate.getYear(), soStartDate.getMonth(), soStartDate.getDate() + 1);
                    criteria.add(Restrictions.between("roleCreatedOn", soStartDate, soEndDate));
        }
            criteria.addOrder(Order.asc("roleDesc"));
            return criteria.list();
    }

    public List findRoleName(Role roleId)throws Exception {
        List list = new ArrayList();
            String queryString = "from User where role=?0";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", roleId);
            list = queryObject.list();
        return list;
    }

    public void delete(DataExchangeUsecases persistanceInstance)throws Exception {
            getSession().delete(persistanceInstance);
            getSession().flush();
    }

    public void update(Role persistanceInstance)throws Exception {
            getSession().saveOrUpdate(persistanceInstance);
    }
}

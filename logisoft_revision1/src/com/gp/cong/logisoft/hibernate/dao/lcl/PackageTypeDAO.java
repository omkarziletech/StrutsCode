/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.PackageType;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;

/**
 *
 * @author Administrator
 */
public class PackageTypeDAO extends BaseHibernateDAO<PackageType> {

    private static final Logger log = Logger.getLogger(BaseHibernateDAO.class);

    public PackageTypeDAO() {
        super(PackageType.class);
    }

    public PackageType findPackage(String desc) throws Exception {
        if (null != desc) { // remove whitespace
            desc = desc.trim();
        }
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("FROM PackageType p WHERE CONCAT(p.type, p.plural) = :desc");
        queryBuilder.append(" OR p.type = :desc");
        queryBuilder.append(" OR p.description = :desc");
        queryBuilder.append(" OR p.abbr01 = :desc");
        queryBuilder.append(" OR CONCAT(p.abbr01, p.plural)= :desc");
        Query query = getCurrentSession().createQuery(queryBuilder.toString()).setString("desc", desc).setMaxResults(1);
        return (PackageType) query.uniqueResult();
    }

    public PackageType findByDesc(String desc) throws Exception {
        Query query = getCurrentSession().createQuery("FROM PackageType WHERE description = :description").setString("description", desc);
        return (PackageType) query.uniqueResult();
    }

    public List<PackageType> getAllPackages() throws Exception {
        String queryString = "from PackageType";
        return getSession().createQuery(queryString).list();
    }

    public List<PackageType> setHazmetPkgType() throws Exception {
        return this.getAllPackages();
    }
}

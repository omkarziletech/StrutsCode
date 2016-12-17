package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.ContainerInfo;
import org.hibernate.Query;

/**
 *
 * @author Rajesh
 */
public class ContainerInfoDao extends BaseHibernateDAO<ContainerInfo> {

    public ContainerInfoDao() {
        super(ContainerInfo.class);
    }

    public ContainerInfo getContainer(String containerName, Long eculineId) throws Exception {
        Query query = getCurrentSession().createQuery("FROM ContainerInfo WHERE eculineEdi.id = :ecuId AND cntrName = :containerName");
        query.setLong("ecuId", eculineId);
        query.setString("containerName", containerName);
        return (ContainerInfo) query.uniqueResult();
    }
}

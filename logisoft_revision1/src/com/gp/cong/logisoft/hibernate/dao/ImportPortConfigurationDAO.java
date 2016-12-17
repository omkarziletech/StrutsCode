package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;
import org.hibernate.Query;

public class ImportPortConfigurationDAO extends BaseHibernateDAO {

    public List getLclPortsForAgentInfoWithDefaultForImports(String pod)throws Exception {
            pod = pod.trim();
            String queryString = "SELECT m.importAgentNumber FROM ImportPortConfiguration m,Ports p WHERE p.unLocationCode=?0 AND p.id=m.sheduleNumber AND m.importsService='Y'";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", pod);
            return queryObject.list();
    }
    
    public String getImportTerminal(Integer schnum)throws Exception {
            String queryString = "SELECT trm_num FROM import_port_configuration WHERE schnum="+schnum+" limit 1";
            Query queryObject = getCurrentSession().createSQLQuery(queryString);
            Object ImportTerminalObj = queryObject.uniqueResult();
            return null!=ImportTerminalObj?ImportTerminalObj.toString():null;
    }
}

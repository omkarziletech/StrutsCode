/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.logisoft.domain.FCLData;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.struts.form.FCLSearchForm;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Vinay
 */
public class FCLSearchDAO extends BaseHibernateDAO {

    public FCLSearchDAO(FCLSearchForm fsf) {
	this.fsf = fsf;
    }

    public List search() throws Exception {
	StringBuilder queryBuilder = new StringBuilder(search);
	queryBuilder.append(tables);
	queryBuilder.append(defaultConditions);
	ArrayList<FCLData> results = new ArrayList<FCLData>();
	if (fsf.getDestinationPort() != 0) {
	    queryBuilder.append(" AND fcl.destination_port='").append(fsf.getDestinationPort()).append("'");
	}
	if (fsf.getOriginTerminal() != 0) {
	    queryBuilder.append(" AND fcl.origin_terminal='").append(fsf.getOriginTerminal()).append("'");
	}
	if (CommonUtils.isNotEmpty(fsf.getPortOfExit())) {
	    queryBuilder.append(" AND fcl.port_of_exit='").append(fsf.getPortOfExit()).append("'");
	}
	if (CommonUtils.isNotEmpty(fsf.getSslineNo())) {
	    queryBuilder.append(" AND fcl.ssline_no='").append(fsf.getSslineNo()).append("'");
	}
	List almostResults = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
	FCLData fd;
	if (CommonUtils.isNotEmpty(almostResults)) {
	    for (Object row : almostResults) {
		fd = convert((Object[]) row);
		results.add(fd);
	    }
	}
	return results;
    }

    public List find() {
	Criteria crit = getSession().createCriteria(FCLData.class);
	if (fsf.getDestinationPort() != 0) {
	    crit.add(Restrictions.eq("destinationPort", fsf.getDestinationPort()));
	}
	if (fsf.getOriginTerminal() != 0) {
	    crit.add(Restrictions.eq("originTerminal", fsf.getOriginTerminal()));
	}
	if (CommonUtils.isNotEmpty(fsf.getPortOfExit())) {
	    crit.add(Restrictions.eq("portOfExit", fsf.getPortOfExit()));
	}
	if (CommonUtils.isNotEmpty(fsf.getSslineNo())) {
	    crit.add(Restrictions.eq("sslineNo", fsf.getSslineNo()));
	}
	List temp = crit.list();
	return crit.list();
    }

    public FCLData getFCLData(int id) throws Exception {
	StringBuilder queryBuilder = new StringBuilder(search);
	queryBuilder.append(tables);
	queryBuilder.append(defaultConditions).append(" AND fcl.id='").append(id).append("' ");
	List almostResults = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
	FCLData fd = new FCLData();
	if (CommonUtils.isNotEmpty(almostResults)) {
	    for (Object row : almostResults) {
		Object[] col = (Object[]) row;
		fd = convert(col);
		if (("" + col[6]).equalsIgnoreCase("null")) {
		    fd.setPortOfExit("");
		}
	    }
	}
	return fd;
    }

    public void update() {
	FCLData fd = (FCLData) getSession().createCriteria(FCLData.class).add(Restrictions.eq("id", fsf.getId())).uniqueResult();
	fd.setDaysInTransit(fsf.getDaysInTransit());
	fd.setRemarks(fsf.getRemarks());
	fd.setPortOfExit(fsf.getPortOfExit());
	fd.setHazardous(CommonConstants.NO);
	if (fsf.isHaz()) {
	    fd.setHazardous(CommonConstants.YES);
	}
	fd.setLocalDrayage(CommonConstants.NO);
	if (fsf.isLocD()) {
	    fd.setLocalDrayage(CommonConstants.YES);
	}
	getSession().saveOrUpdate(fd);
	getSession().flush();
	fsf.setOriginTerminal(fd.getOriginTerminal());
	fsf.setDestinationPort(fd.getDestinationPort());
	fsf.setSslineNo(fd.getSslineNo());
    }

    private FCLData convert(Object[] col) {
	FCLData fd = new FCLData();
	fd.setId((Integer) col[0]);
	fd.setOrigTerm("" + col[1]);
	fd.setDesPort("" + col[2]);
	fd.setDaysInTransit((Integer) col[3]);
	fd.setSslineName("" + col[4]);
	fd.setSslineNo("" + col[5]);
	fd.setPortOfExit("" + col[6]);
	fd.setRemarks("" + col[7]);
	fd.setHazardous("" + col[8]);
	fd.setLocalDrayage("" + col[9]);
	fd.setHaz();
	fd.setLocD();
	return fd;
    }
    private FCLSearchForm fsf;
    private final String search = "SELECT fcl.id, org_pr.portname as originName, "
	    + "dest_pr.portname as destName, fcl.days_in_transit, tp.acct_name, fcl.ssline_no, "
	    + "fcl.port_of_exit, fcl.remarks, fcl.hazardous_flag, fcl.local_drayage";
    private final String tables = " FROM fcl_org_dest_misc_data fcl, ports org_pr, "
	    + "ports dest_pr, trading_partner tp";
    private final String defaultConditions = " WHERE fcl.origin_terminal=org_pr.id AND fcl.destination_port=dest_pr.id AND fcl.ssline_no=tp.acct_no";
}

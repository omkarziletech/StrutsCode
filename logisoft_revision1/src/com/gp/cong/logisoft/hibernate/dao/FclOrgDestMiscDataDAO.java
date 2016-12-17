package com.gp.cong.logisoft.hibernate.dao;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import com.gp.cong.logisoft.domain.FclOrgDestMiscData;
import com.gp.cong.logisoft.domain.TradingPartnerTemp;
import com.gp.cong.logisoft.domain.UnLocation;

public class FclOrgDestMiscDataDAO extends BaseHibernateDAO {

    public void save(FclOrgDestMiscData transientInstance)throws Exception {
            getSession().saveOrUpdate(transientInstance);
            getSession().flush();
    }

    public void update(FclOrgDestMiscData persistentInstance)throws Exception {
            getSession().update(persistentInstance);
            getSession().flush();
    }

    public void delete(FclOrgDestMiscData persistentInstance)throws Exception {
            getSession().delete(persistentInstance);
            getSession().flush();
    }

    public FclOrgDestMiscData findById(java.lang.Integer id) throws Exception {
            FclOrgDestMiscData instance = (FclOrgDestMiscData) getSession().get("com.gp.cvst.logisoft.hibernate.dao.AccountBalance", id);
            return instance;
    }

    public List getorgdestmiscdate(UnLocation orgTerminal, UnLocation destinationPort, TradingPartnerTemp tradingPartnerTemp)throws Exception {
        List orgdestmiscdatelist = new ArrayList();
            String queryString = "from FclOrgDestMiscData where originalTerminal.id='" + orgTerminal.getId() + "' and destinationPort.id='" + destinationPort.getId() + "' and sslineNo='" + tradingPartnerTemp.getAccountno() + "'";
            Query queryObject = getCurrentSession().createQuery(queryString);
            orgdestmiscdatelist = queryObject.list();
        return orgdestmiscdatelist;
    }

    public List getorgdestmiscdate1(String orgTerminal, String destinationPort, String tradingPartnerTemp)throws Exception {
        List orgdestmiscdatelist = new ArrayList();
            String queryString = "from FclOrgDestMiscData where originalTerminal.id='" + orgTerminal + "' and destinationPort.id='" + destinationPort + "' and sslineNo='" + tradingPartnerTemp + "'";
            Query queryObject = getCurrentSession().createQuery(queryString);
            orgdestmiscdatelist = queryObject.list();
        return orgdestmiscdatelist;
    }
}

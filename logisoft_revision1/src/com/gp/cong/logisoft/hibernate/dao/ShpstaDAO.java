package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.common.CommonUtils;

import com.gp.cong.logisoft.domain.Shpsta;
import java.util.ArrayList;
import java.util.List;

public class ShpstaDAO extends BaseHibernateDAO {

    public void save(Shpsta shpsta) throws Exception {
        getSession().save(shpsta);
        getSession().flush();
    }

    public boolean getLogiwareFile(String bkgNumber)throws Exception{
            List resultList = new ArrayList();
            if (CommonUtils.isNotEmpty(bkgNumber)) {
                String queryString = "SELECT * FROM fcl_bl where BookingNo = '" + bkgNumber + "'";
                resultList = getSession().createSQLQuery(queryString).list();
                if (CommonUtils.isNotEmpty(resultList)) {
                    return true;
                }
            }
            return false;
    }
}

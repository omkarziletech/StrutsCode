package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.EdiAck;
import java.util.ArrayList;
import java.util.List;

public class EdiAckDAO extends BaseHibernateDAO {

    public void save(EdiAck ediAck) throws Exception {
        getSession().save(ediAck);
    }
    public boolean getLogiwareFile(String bkgNumber){
            List resultList = new ArrayList();
            if(CommonUtils.isNotEmpty(bkgNumber)){
                String queryString = "SELECT * FROM fcl_bl where BookingNo = '"+bkgNumber+"'";
                resultList = getSession().createSQLQuery(queryString).list();
                if(CommonUtils.isNotEmpty(resultList)){
                    return true;
                }
            }
            return false;
        }
    }
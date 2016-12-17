package com.gp.cvst.logisoft.hibernate.dao;

import java.util.ArrayList;


import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.domain.PaymentChargecode;

public class PaymentChargecodeDAO extends BaseHibernateDAO {

    public void save(PaymentChargecode transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void delete(PaymentChargecode persistentInstance) throws Exception{
            getSession().delete(persistentInstance);
            getSession().flush();
    }

    public PaymentChargecode findById(java.lang.Integer id) throws Exception{
            PaymentChargecode instance = (PaymentChargecode) getSession().get("com.gp.cvst.logisoft.hibernate.dao.PaymentPrepay", id);
            return instance;
    }

    public java.util.List<PaymentChargecode> findPaymentsByBatchAndCheckNo(String batchId, String checkNo, Integer paymentCheckId) throws Exception{
        java.util.List<PaymentChargecode> paymentChargeCodeList = new ArrayList<PaymentChargecode>();
        String query = " from PaymentChargecode paymentChargecode where paymentChargecode.batchId='" + batchId + "' and paymentChargecode.checkNo='" + checkNo + "'";
        if (null != paymentCheckId) {
            query = query + " and paymentChargecode.paymentCheckId=" + paymentCheckId;
        }
            paymentChargeCodeList = getCurrentSession().createQuery(query).list();
        return paymentChargeCodeList;
    }
}

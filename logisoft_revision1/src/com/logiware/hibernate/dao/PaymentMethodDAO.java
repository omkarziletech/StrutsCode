package com.logiware.hibernate.dao;

import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.logisoft.domain.PaymentMethod;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import java.util.ArrayList;
import java.util.List;
import org.apache.struts.util.LabelValueBean;

/**
 *
 * @author Lakshminarayanan
 */
public class PaymentMethodDAO extends BaseHibernateDAO implements java.io.Serializable,ConstantsInterface {

    private static final long serialVersionUID = 923291650376998703L;

    public PaymentMethod findById(Integer id)  throws Exception{
	    PaymentMethod instance = (PaymentMethod) getSession().get(
		    "com.gp.cong.logisoft.domain.PaymentMethod", id);
	    return instance;
    }

    public List<LabelValueBean> getPaymentMethods(String vendor) throws Exception{
	List<LabelValueBean> paymentMethods = new ArrayList<LabelValueBean>();
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("select default_pay_method,pay_method from payment_method");
	queryBuilder.append(" where pay_accno='").append(vendor).append("'");
	List result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
	if(null==result || result.isEmpty()){
	    paymentMethods.add(new LabelValueBean(PAYMENT_METHOD_CHECK.toUpperCase(), PAYMENT_METHOD_CHECK.toUpperCase()));
	}else{
	    int i = 0;
	    for(Object row : result){
		Object[] col = (Object[])row;
		String defaultMathod = col[0].toString().toUpperCase();
		String paymentMethod = col[1].toString().toUpperCase();
		if (defaultMathod.equals(YES)) {
		    paymentMethods.add(0, new LabelValueBean(paymentMethod,paymentMethod));
		} else {
		    paymentMethods.add(i, new LabelValueBean(paymentMethod,paymentMethod));
		}
		i++;
	    }
	}
	return paymentMethods;
    }
}

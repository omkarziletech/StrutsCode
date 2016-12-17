package com.gp.cong.logisoft.hibernate.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import com.gp.cong.logisoft.domain.Usecases;
import com.gp.cong.logisoft.domain.DataExchangeTransaction;
import org.apache.log4j.Logger;

public class DataExchangeTransactionDAO extends BaseHibernateDAO {

    //private Session session;
    private static final Logger log = Logger.getLogger(DataExchangeTransactionDAO.class);

    public List findAllUseCases() throws Exception {
            String queryString = "from DataExchangeTransaction";
            Query queryObject = getCurrentSession().createQuery(queryString);
            return queryObject.list();
    }

    public List findForUseCase(String useCaseId, Date javaDate, String docSetKeyValue, String flowFrom, String status)throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(DataExchangeTransaction.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            UsecasesDAO usecasesDAO = new UsecasesDAO();
            if (useCaseId != null && !useCaseId.equals("") && !useCaseId.equals("0")) {
                Usecases usecases = usecasesDAO.findById(Integer.parseInt(useCaseId));
                if (usecases != null) {
                    criteria.add(Restrictions.like("usecases", usecases));
                }
            }
            if (docSetKeyValue != null && !docSetKeyValue.equals("")) {
                criteria.add(Restrictions.like("docSetKeyValue", docSetKeyValue + "%"));
            }
            if (flowFrom != null && !flowFrom.equals("") && !flowFrom.equals("0")) {
                criteria.add(Restrictions.like("flowFrom", flowFrom));
                //criteria.toString();
            }
            if (status != null && !status.equals("") && !status.equals("0")) {
                criteria.add(Restrictions.like("status", status));
            }
            if (javaDate != null && !javaDate.equals("")) {
                try {
                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm");
                    String strSODate = dateFormat.format(javaDate);
                    //criteria.add(Restrictions.eq("logisticOrderDate", strSODate));
                    Date soStartDate = (Date) dateFormat.parse(strSODate);
                    Date soEndDate = new Date(soStartDate.getYear(), soStartDate.getMonth(), soStartDate.getDate() + 1);
                    criteria.add(Restrictions.between("date", soStartDate, soEndDate));
                } catch (Exception e) {
                    log.info("Error while parsing date" ,e);
                }
            }
            return criteria.list();
    }
}

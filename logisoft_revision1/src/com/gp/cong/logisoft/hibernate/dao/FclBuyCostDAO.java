package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.logisoft.domain.FclBuy;
import com.gp.cong.logisoft.domain.FclBuyCost;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.hibernate.Query;

public class FclBuyCostDAO extends BaseHibernateDAO {

    public FclBuyCost findById(Integer id)throws Exception {
            FclBuyCost instance = (FclBuyCost) getCurrentSession().get("com.gp.cong.logisoft.domain.FclBuyCost", id);
            return instance;
    }

    public List findAllUsers(Integer id)throws Exception {
            String queryString1 = " from FclBuyCost where fclStdId=?0 order by costId.id";
            Query queryObject = getCurrentSession().createQuery(queryString1);
            queryObject.setParameter("0", id);
            return queryObject.list();
    }

    public List findAllUsers1(Integer id)throws Exception {
            String queryString1 = " from FclBuyCost where fclStdId=?0 order by costId.id";
            Query queryObject = getCurrentSession().createQuery(queryString1);
            queryObject.setParameter("0", id);
            String temp = "";
            int i = 0;
            int k = 0;
            LinkedList linkedList = new LinkedList();
            List newList = new ArrayList();
            String unitTyep = "";
            for (Iterator iterator = queryObject.list().iterator(); iterator.hasNext();) {
                FclBuyCost tempRates = (FclBuyCost) iterator.next();
                if (!temp.equals(tempRates.getFclStdId().toString())) {
                    k = i;
                }
                if (tempRates.getCostId().getId().toString().equals("11694")) {
                    linkedList.add(k, tempRates);
                } else {
                    linkedList.add(tempRates);
                }
                temp = tempRates.getFclStdId().toString();
                i++;
            }
            newList.addAll(linkedList);


            return newList;
    }

    public void delete(FclBuyCost persistanceInstance)throws Exception {
            getSession().delete(persistanceInstance);
            getSession().flush();
    }
    public List getOtherCommodity(String commCode,String markUp)throws Exception {
            String stringQuery = "SELECT f.addsub,f.markup1,f.costcode,f.base_commodity_code,f.markup2 FROM genericcode_dup g,`fclbuyothercomm` f WHERE g.code = '"+commCode+"' AND g.id = f.commodity_code";
            Query queryString = getCurrentSession().createSQLQuery(stringQuery);
            return queryString.list();
    }


    public void removeRecords(FclBuy updatefclBuy)throws Exception {
        Set retrive = new HashSet<FclBuyCost>();
        if (updatefclBuy.getFclBuyCostsSet() != null) {
            retrive = updatefclBuy.getFclBuyCostsSet();
            Iterator it = retrive.iterator();
            while (it.hasNext()) {
                FclBuyCost fclBuyCost = (FclBuyCost) it.next();

                if (fclBuyCost.getFclStdId() == null) {
                    FclBuyCostDAO fcldao = new FclBuyCostDAO();
                    FclBuyCost fclbuyForDelete = fcldao.findById(fclBuyCost.getFclCostId());

                    fclbuyForDelete.setFclStdId(null);
                    fcldao.delete(fclbuyForDelete);
                    //updatefclBuy.getFclBuyCostsSet().remove(fclbuyForDelete); 
                }
            }
        }

    }
}

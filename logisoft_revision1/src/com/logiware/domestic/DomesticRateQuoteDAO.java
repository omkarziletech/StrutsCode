/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.domestic;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.User;
import com.logiware.domestic.form.RateQuoteForm;
import org.hibernate.Query;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class DomesticRateQuoteDAO extends BaseHibernateDAO<DomesticRateQuote> {

    private static final Logger log = Logger.getLogger(DomesticRateQuoteDAO.class);
    public DomesticRateQuoteDAO() {
        super(DomesticRateQuote.class);
    }

   public List<DomesticRateQuote> searchDomesticQuote(User user,RateQuoteForm rateQuoteForm){
       List<DomesticRateQuote> l = null;
       StringBuilder sb = new StringBuilder();
       if(null != rateQuoteForm.getUserId()){
           if(rateQuoteForm.getUserId() != 0){
                sb.append("From DomesticRateQuote where rateBy.id=").append(rateQuoteForm.getUserId());
           }else{
               sb.append("From DomesticRateQuote where rateBy.id>").append(rateQuoteForm.getUserId());
           }
       }else{
            sb.append("From DomesticRateQuote where rateBy.id=").append(user.getUserId());
       }
       if(CommonUtils.isNotEmpty(rateQuoteForm.getShipmentId())){
           sb.append(" AND shipmentId='").append(rateQuoteForm.getShipmentId()).append("'");
       }
       if(CommonUtils.isNotEmpty(rateQuoteForm.getFromZip())){
           sb.append(" AND originZip='").append(rateQuoteForm.getFromZip()).append("'");
       }
       if(CommonUtils.isNotEmpty(rateQuoteForm.getToZip())){
           sb.append(" AND destinationZip='").append(rateQuoteForm.getToZip()).append("'");
       }
       sb.append("order by id desc");
        try {
            Query queryObject = getSession().createQuery(sb.toString());
            l = queryObject.list();
        } catch (Exception ex) {
            log.info("Exception on class DomesticRateQuoteDAO in method searchDomesticQuote" + new Date(), ex);
        }
       return l;
   }
}

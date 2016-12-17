/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.domestic;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.User;
import com.logiware.domestic.form.BookingForm;
import org.hibernate.Query;
import java.util.*;

/**
 *
 * @author Shanmugam
 */
public class DomesticBookingDAO extends BaseHibernateDAO<DomesticBooking> {


    public DomesticBookingDAO() {
        super(DomesticBooking.class);
    }

    public List<DomesticBooking> searchDomesticBooking(User user,BookingForm bookingForm){
        List<DomesticBooking> l = null;
       StringBuilder sb = new StringBuilder();
       if(null != bookingForm.getUserId()){
           if(bookingForm.getUserId() != 0){
                sb.append("From DomesticBooking where bookedBy.id=").append(bookingForm.getUserId());
           }else{
               sb.append("From DomesticBooking where bookedBy.id>").append(bookingForm.getUserId());
           }
       }else{
            sb.append("From DomesticBooking where bookedBy.id=").append(user.getUserId());
       }
       if(CommonUtils.isNotEmpty(bookingForm.getFromZip())){
           sb.append(" AND fromZip='").append(bookingForm.getFromZip().substring(0, 5)).append("'");
       }
       if(CommonUtils.isNotEmpty(bookingForm.getToZip())){
           sb.append(" AND toZip='").append(bookingForm.getToZip().substring(0, 5)).append("'");
       }
       if(CommonUtils.isNotEmpty(bookingForm.getBookingNumber())){
           sb.append(" AND bookingNumber like '").append(bookingForm.getBookingNumber()).append("%'");
       }
       if(CommonUtils.isNotEmpty(bookingForm.getCarrierName())){
           sb.append(" AND carrierName like '").append(bookingForm.getCarrierName()).append("%'");
       }
       if(CommonUtils.isNotEmpty(bookingForm.getBookingId())){
           sb.append(" AND id = ").append(bookingForm.getBookingId());
           bookingForm.setBookingId("");
       }
       sb.append(" order by id desc");
        try {
            Query queryObject = getSession().createQuery(sb.toString());
            l = queryObject.list();
        } catch (Exception ex) {
        }
       return l;
    }
}

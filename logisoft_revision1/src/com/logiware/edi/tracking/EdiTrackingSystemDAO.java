package com.logiware.edi.tracking;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.edi.entity.Shipment;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

public class EdiTrackingSystemDAO extends BaseHibernateDAO{
	public EdiTrackingSystem findById(java.lang.String id)throws Exception {
        EdiTrackingSystem instance = (EdiTrackingSystem) getSession().get("com.logiware.edi.tracking.EdiTrackingSystem", Integer.parseInt(id));
        return instance;
    }
	public List findAllEdi(String drnumber,String messageType,String ediCompany
            ,String origin, String destination, String pol, String pod, String bookingNo,String fromDate,
            String toDate)throws Exception {
        String queryString = "from EdiTrackingSystem";
        boolean found = false;
     	   if(drnumber!=null && !drnumber.equals("") && !drnumber.equals("0")){
            found = true;
     		   queryString += " where drnumber like '"+drnumber+"%'" ;
        }
     	   if(messageType!=null && !messageType.equals("") && !messageType.equals("0")){
     		  if(found){
    			  queryString += " and messageType like '"+messageType+"%'";
    		  }else{
                found = true;
    			  queryString += " where messageType like '"+messageType+"%'";
            }
        }
     	   if(ediCompany!=null && !ediCompany.equals("") && !ediCompany.equals("0")){
     		 if(found){
    			  queryString += " and ediCompany like '"+ediCompany+"%'";
    		  }else{
                found = true;
    			  queryString += " where ediCompany like '"+ediCompany+"%'";
            }
        }
           if(origin!=null && !origin.equals("") && !origin.equals("0")){
     		  if(found){
    			  queryString += " and placeOfReceipt like '"+origin+"%'";
    		  }else{
                found = true;
    			  queryString += " where placeOfReceipt like '"+origin+"%'";
            }
        }
     	   if(destination!=null && !destination.equals("") && !destination.equals("0")){
     		 if(found){
    			  queryString += " and placeOfDelivery like '"+destination+"%'";
    		  }else{
                found = true;
    			  queryString += " where placeOfDelivery like '"+destination+"%'";
            }
        }
           if(pol!=null && !pol.equals("") && !pol.equals("0")){
     		  if(found){
    			  queryString += " and portOfLoad like '"+pol+"%'";
    		  }else{
                found = true;
    			  queryString += " where portOfLoad like '"+pol+"%'";
            }
        }
     	   if(pod!=null && !pod.equals("") && !pod.equals("0")){
     		 if(found){
    			  queryString += " and portOfDischarge like '"+pod+"%'";
    		  }else{
                found = true;
    			  queryString += " where portOfDischarge like '"+pod+"%'";
            }
        }
           if(bookingNo!=null && !bookingNo.equals("") && !bookingNo.equals("0")){
     		  if(found){
    			  queryString += " and REPLACE(bookingNo,'-','') like '"+bookingNo.replaceAll("-","") +"%'";
    		  }else{
                found = true;
    			  queryString += " where REPLACE(bookingNo,'-','') like '"+bookingNo.replaceAll("-","")+"%'";
            }
        }
           if(fromDate!=null && !fromDate.equals("") && !fromDate.equals("0") && toDate!=null && !toDate.equals("") && !toDate.equals(0)){
               if(found){
                   queryString += " and date_format(processedDate,'%Y%m%d') between '"+fromDate+"' and '"+toDate+"'";
               }else{
                found = true;
                   queryString += " where date_format(processedDate,'%Y%m%d') between '"+fromDate+"' and '"+toDate+"'";
            }
        }
     	 queryString+= " order by processedDate desc";
        Query queryObject = getCurrentSession().createQuery(queryString);
        return queryObject.list();
    }
	public List findByDrNumber(String drNumber)throws Exception {
        String queryString = "from EdiTrackingSystem where drnumber=?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", drNumber);
        return queryObject.list();
    }
	public String findDrNumberStatus(String drNumber)throws Exception {
        String status = "";
        String queryString = "select status from logfile_edi where DRNumber=?0";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", drNumber);
        List list = queryObject.list();
        for (int i = 0; i < list.size(); i++) {
                status = (String)list.get(i);
                if("success".equalsIgnoreCase(status)){
                break;
            }
        }
        return status;
    }
	public String findTrackingStatus(String drNumber)throws Exception {
        String queryString = "select tracking_status from logfile_edi where message_type='315' and  drnumber='"+drNumber+"' and status = 'success' ORDER BY id DESC limit 1";
        Query queryObject = getSession().createSQLQuery(queryString);
        Object status = queryObject.uniqueResult();
        return null!=status?status.toString():"";
    }
        public String findITNStatus(String itnNumber)throws Exception {
            String queryString = "SELECT status FROM aes_history WHERE itn = '"+itnNumber+"' ORDER BY id desc limit 1";
        Query queryObject = getSession().createSQLQuery(queryString);
        Object status = queryObject.uniqueResult();
        return null!=status?status.toString():"";
    }
        public String findITNStatusFileNo(String fileNumber)throws Exception {
        String queryString = "SELECT status FROM aes_history WHERE file_number = '"+fileNumber+"' ORDER BY id desc limit 1";
        Query queryObject = getSession().createSQLQuery(queryString);
        Object status= queryObject.uniqueResult();
        return null!=status?status.toString():"";
    }
    public void saveLogFileEdi(EdiTrackingSystem logFileEdi) throws Exception {
        getSession().save(logFileEdi);
        getSession().flush();
    }
	public String findDrNumber(String bookingNo)throws Exception {
        String queryString = "select concat(Quote_Term,Quote_DR) as DRNumber ,BookingNumber from edi_997_ack where BookingNumber = ?0 limit 1";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", bookingNo);
        Object drNumber=queryObject.uniqueResult();
        return null!=drNumber?drNumber.toString():"";
    }
    public Integer findSiId(String fileName) throws Exception {
        String queryString = "select id from logfile_edi  where filename = ?0 limit 1";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", fileName);
        Object siId= queryObject.uniqueResult();
        return null!=siId?(Integer)siId:0;
    }
        public String findTrackingStatus(String eventCode,String containerStatus,String eventLocation)throws Exception {
        String queryString = "select event_name from edi_tracking_status where event_code = '"+eventCode+"' ";
        if(CommonUtils.isNotEmpty(containerStatus)){
            queryString+=" and container_status ='"+containerStatus.substring(0,1)+"' ";
        }
        if(CommonUtils.isNotEmpty(eventLocation)){
            queryString+=" and event_location_function ='"+eventLocation+"' ";
        }
        Query queryObject = getSession().createSQLQuery(queryString);
        Object status = queryObject.setMaxResults(1).uniqueResult();
        return null!=status?status.toString():"";
    }
    public EdiTrackingSystem findByBookingNo(String bookingNo) throws Exception {
        Criteria criteria = getSession().createCriteria(EdiTrackingSystem.class, "ediTrackingSystem");
        criteria.add(Restrictions.eq("ediTrackingSystem.bookingNo", bookingNo));
        return (EdiTrackingSystem) criteria.setMaxResults(1).uniqueResult();
    }
    public Shipment findShipmentByEdiTrackingSystem(EdiTrackingSystem ediTrackingSystem, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginuser");
        String location = LoadLogisoftProperties.getProperty("reportLocation");
        location = location + "/ediTracking";
        File xmlFile = new File(location);
        if (!xmlFile.exists()) {
            xmlFile.mkdir();
        }
        File file = new File(location + "/" + user.getLoginName() + "_xml.xml");
        FileOutputStream outPutStream = new FileOutputStream(file);
        BufferedOutputStream bout = new BufferedOutputStream(outPutStream);
        bout.write(ediTrackingSystem.getXml());
        bout.flush();
        bout.close();
        Shipment shipment = new ParseInttraXML().parseXml(file.getAbsolutePath());
        return (Shipment) shipment;
    }
    
    public KnShippingInstruction getKnShippingInstruction(String bkgNo) {
        String query = "from KnShippingInstruction where bkgNumber=:bkgNo";
        Query queryObject = getCurrentSession().createQuery(query);
        queryObject.setParameter("bkgNo", bkgNo);
        return (KnShippingInstruction) queryObject.uniqueResult();
    }
}

package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.CustomerContact;
import java.util.ArrayList;
import java.util.List;

public class CustContactReportDAO extends BaseHibernateDAO<CustContactReport> {

    public CustContactReportDAO() {
        super(CustContactReport.class);
    }

    public List<CustomerContact> getContactReports(String contactId){
        List<CustomerContact> customerlist = new ArrayList<CustomerContact>();
        
        // method to fetch the customer contact list for contact drop down.
        return customerlist;
    }
    
    public List<CustContactReport> getReportOption(String type,String defId){
        List<CustContactReport> reportlist = new ArrayList<CustContactReport>();
        
        // method to fetch the report list for contact drop down.
        return reportlist;
    }
    
    public List<CustReportDefine> getReportDefineList(){
        List<CustReportDefine> definelist = new ArrayList<CustReportDefine>();
        
        // method to fetch the report define list for contact drop down.
        return definelist;
    }
    
}

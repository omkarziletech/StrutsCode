package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.PrintConfig;
import com.gp.cong.logisoft.lcl.report.LclReportConstants;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO;
import java.util.ArrayList;
import java.util.List;
import org.apache.struts.util.LabelValueBean;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

public class PrintConfigDAO extends BaseHibernateDAO {

    public void save(PrintConfig printConfig) throws Exception {
        getSession().save(printConfig);
        getSession().flush();
    }

    public void update(PrintConfig printConfig) throws Exception {
        getSession().update(printConfig);
        getSession().flush();
    }

    public void SaveOrUpdate(PrintConfig printConfig) throws Exception {
        getSession().saveOrUpdate(printConfig);
        getSession().flush();
    }

    public void delete(PrintConfig printConfig) throws Exception {
        getSession().delete(printConfig);
        getSession().flush();
    }

    public PrintConfig findById(Long id) throws Exception {
        PrintConfig instance = (PrintConfig) getSession().get(
                "com.gp.cong.logisoft.domain.PrintConfig", id);
        return instance;
    }

    public List<PrintConfig> findAllPrintConfig() throws Exception {
        List<PrintConfig> scanconfigList = null;
        scanconfigList = getCurrentSession().createCriteria(PrintConfig.class).list();
        return scanconfigList;
    }

    public List<PrintConfig> findPrintConfigByScreenName(String screenName, String documentId) throws Exception {
        List<PrintConfig> printConfigList = null;
        Criteria criteria = getSession().createCriteria(PrintConfig.class);
        if (null != screenName && !screenName.trim().equals("")) {
            criteria.add(Restrictions.like("screenName", screenName + "%"));
            criteria.add(Restrictions.ne("documentName", CommonConstants.DOCUMENT_NAME_PICKUPORDER));
            criteria.add(Restrictions.ne("documentName", CommonConstants.DOCUMENT_NAME_BOOKINGCOVERSHEET));
        }
        if (null != documentId && !documentId.trim().equals("")) {
            criteria.add(Restrictions.eq("id", Long.parseLong(documentId)));
        }
        printConfigList = criteria.list();
        return printConfigList;
    }

    public PrintConfig findPrintConfigByScreenNameAndDocumentName(String screenName, String documentName) throws Exception {
        Criteria criteria = getSession().createCriteria(PrintConfig.class, "printConfig");
        criteria.add(Restrictions.eq("printConfig.screenName", screenName));
        criteria.add(Restrictions.eq("printConfig.documentName", documentName));
        criteria.add(Restrictions.eq("printConfig.showOnScreen", true));
        return (PrintConfig) criteria.uniqueResult();
    }

    public List<PrintConfig> findPrintConfigByScreenNameAndDocumentNameList(String[] documentName) throws Exception {
        List<PrintConfig> printConfigList = null;
        Criteria criteria = getSession().createCriteria(PrintConfig.class, "printConfig");
        criteria.add(Restrictions.eq("screenName", "LCLBL"));
        criteria.add(Restrictions.in("documentName", documentName));
        criteria.add(Restrictions.eq("showOnScreen", true));
        printConfigList = criteria.list();
        return printConfigList;
    }

    public List<PrintConfig> findLclPrintConfigByScreenName(String screenName, String documentId,
            String transhipment, String fileNo) throws Exception {
        List<PrintConfig> printConfigList = null;
        Criteria criteria = getSession().createCriteria(PrintConfig.class);
        if (null != screenName && !screenName.trim().equals("")) {
            criteria.add(Restrictions.like("screenName", screenName + "%"));
        }
        if (transhipment != null && transhipment.equalsIgnoreCase("true")) {
            criteria.add(Restrictions.ne("documentName", LclReportConstants.DOCUMENT_LCL_ARRIVAL_ADVICE_STATUS));
        }
        if (null != documentId && !documentId.trim().equals("")) {
            criteria.add(Restrictions.eq("id", Long.parseLong(documentId)));
        }

        if ("LCLBL".equals(screenName) && "unPostBl".equalsIgnoreCase(transhipment)) {
            criteria.add(Restrictions.ne("documentName", "Bill of Lading (Original)"));
            criteria.add(Restrictions.ne("documentName", "Bill of Lading (Original SIGNED)"));
        }
        if ("Booking".equals(screenName)) {
            BookingFcl bookingFcl = new BookingFclDAO().findbyFileNo(fileNo);
            if (CommonUtils.isEmpty(bookingFcl.getZip())) {
                criteria.add(Restrictions.ne("documentName", CommonConstants.DOCUMENT_NAME_PICKUPORDER));
            }
            if ("I".equalsIgnoreCase(bookingFcl.getFileType())) {
                criteria.add(Restrictions.ne("documentName", CommonConstants.DOCUMENT_NAME_BOOKINGCOVERSHEET));
            }
        }
        criteria.add(Restrictions.eq("showOnScreen", Boolean.TRUE));
        printConfigList = criteria.list();
        return printConfigList;
    }

    public List<PrintConfig> findPrintConfigByScreenName(String screenName, String documentId, String documentName) throws Exception {
        List<PrintConfig> printConfigList = null;
        Criteria criteria = getSession().createCriteria(PrintConfig.class);
        if (null != screenName && !screenName.trim().equals("")) {
            criteria.add(Restrictions.like("screenName", screenName + "%"));
        }
        if (null != documentId && !documentId.trim().equals("")) {
            criteria.add(Restrictions.eq("id", Long.parseLong(documentId)));
        }
        if (null != documentName && !documentName.trim().equals("")) {
            criteria.add(Restrictions.ne("documentName", documentName));
            criteria.add(Restrictions.ne("documentName", CommonConstants.DOCUMENT_NAME_FCL_ARRIVALNOTICE_NONRATED));
            criteria.add(Restrictions.ne("documentName", CommonConstants.CONTAINER_RESPONSIBILITY_WAIVER));
            criteria.add(Restrictions.ne("documentName", CommonConstants.AUTHORITY_TO_MAKE_ENTRY));
        }
        printConfigList = criteria.list();
        return printConfigList;
    }

    public List<PrintConfig> findPrintConfigByScreenName(String screenName, String documentId, String[] documentName) throws Exception {
        List<PrintConfig> printConfigList = null;
        Criteria criteria = getSession().createCriteria(PrintConfig.class);
        if (null != screenName && !screenName.trim().equals("")) {
            criteria.add(Restrictions.like("screenName", screenName + "%"));
        }
        if (null != documentId && !documentId.trim().equals("")) {
            criteria.add(Restrictions.eq("id", Long.parseLong(documentId)));
        }
        if (null != documentName && documentName.length > 0) {
            criteria.add(Restrictions.in("documentName", documentName));
        }
        printConfigList = criteria.list();
        return printConfigList;
    }

    public boolean updatePrintConfigByScreenName(PrintConfig printConfig) throws Exception {
        String queryString = "update PrintConfig set fileLocation='" + printConfig.getFileLocation() + "' where screenName='" + printConfig.getScreenName() + "'";
        int result = getSession().createQuery(queryString).executeUpdate();
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    public List<LabelValueBean> findAllPrintConfigForUser(Integer userId) {
        try {
            StringBuilder queryStr = new StringBuilder();
            queryStr.append(" SELECT id, CASE WHEN screen_name = 'LCLIMPBooking'  ");
            queryStr.append(" THEN  CONCAT(document_name,' -----> LCL Import') ");
            queryStr.append(" WHEN screen_name = 'LCLBooking' THEN  CONCAT(document_name,' -----> LCL Export') ");
            queryStr.append(" ELSE  CONCAT(document_name,' ----->',screen_name)  END AS document FROM print_config ");
            queryStr.append(" WHERE allow_print = :allowPrint ");
            queryStr.append(" and id NOT IN(SELECT document_id FROM  ");
            queryStr.append(" user_printer_association WHERE user_id=:userId) ");
            List<LabelValueBean> scanconfigList = new ArrayList<LabelValueBean>();
            getCurrentSession().flush();
            SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
            query.setString("allowPrint", "Yes");
            query.setInteger("userId", userId);
            List list = query.list();
            scanconfigList.add(new LabelValueBean("Select One", "0"));
            for (Object obj : list) {
                Object[] config = (Object[]) obj;
                scanconfigList.add(new LabelValueBean(config[1].toString(), config[0].toString()));
            }
            return scanconfigList;
        } catch (RuntimeException re) {
            throw re;
        }
    }

    public String isPrintAllowed(Long id, Integer userId) throws Exception {
        String queryString = "SELECT printer_name FROM user_printer_association WHERE user_id=" + userId + " and document_id=" + id + " limit 1";
        Object printCheck = getCurrentSession().createSQLQuery(queryString).addScalar("printer_name", StringType.INSTANCE).uniqueResult();
        return null != printCheck ? printCheck.toString() : "";
    }

    public Long findDocumentIdByName(String docName, String screenName) {
        String queryString = "SELECT id as result FROM print_config WHERE allow_print = 'Yes' and document_name=:documentName and screen_name=:screenName  limit 1";
        SQLQuery query = getSession().createSQLQuery(queryString);
        query.setParameter("documentName", docName);
        query.setParameter("screenName", screenName);
        query.addScalar("result", LongType.INSTANCE);
        return (Long) query.uniqueResult();
    }

    public String getCompanyName(String companyname) {
        String queryString = "SELECT ct.acct_name FROM cust_address AS ct JOIN trading_partner AS tp  ON ct.acct_no = tp.acct_no where ct.fax='" + companyname + "' limit 1";
        Object acctName = getCurrentSession().createSQLQuery(queryString).uniqueResult();
        if (acctName == null || acctName.equals("")) {
            String querString = "SELECT w.warehsname FROM warehouse w WHERE w.fax='" + companyname + "' limit 1";
            acctName = getCurrentSession().createSQLQuery(querString).uniqueResult();
        }
        return null != acctName ? acctName.toString() : "";
    }

    public String getBillToPartyListForExport(Long file_id) throws Exception {
        Query queryObject = getSession().createSQLQuery("SELECT  group_concat(distinct CASE ar_bill_to_party  "
                + " WHEN 's' THEN 'Shipper' WHEN 'F' THEN 'Forwarder' WHEN 'A' THEN 'Agent' WHEN 'T' THEN 'Third Party' "
                + " END ) AS billToParty   FROM  lcl_bl_ac  WHERE file_number_id=:fileId");
        queryObject.setParameter("fileId", file_id);
        return null != queryObject.uniqueResult() ? queryObject.uniqueResult().toString() : "";
    }
}

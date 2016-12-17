package com.gp.cvst.logisoft.hibernate.dao;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import static com.gp.cong.common.ConstantsInterface.CONTACT_MODE_EMAIL;
import static com.gp.cong.common.ConstantsInterface.EMAIL_STATUS_PENDING;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.accounting.ReportConstants;
import com.gp.cong.logisoft.bc.fcl.FclBlBC;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.User;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.util.StringFormatter;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlCharges;
import com.gp.cvst.logisoft.domain.FclBlContainer;
import com.gp.cvst.logisoft.domain.FclBlCorrections;
import com.gp.cvst.logisoft.domain.FclBlCostCodes;
import com.gp.cvst.logisoft.domain.FileNumberForQuotaionBLBooking;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.reports.dto.BlMainDTO;
import com.logiware.common.job.JobScheduler;
import java.io.File;
import javax.servlet.ServletContext;
import org.apache.struts.util.MessageResources;
import org.hibernate.LockOptions;
import org.hibernate.type.IntegerType;

/**
 * Data access object (DAO) for domain model class FclBl.
 *
 * @see com.gp.cvst.logisoft.hibernate.dao.FclBl
 * @author MyEclipse Persistence Tools
 */
public class FclBlDAO extends BaseHibernateDAO {

    public void save(FclBl transientInstance) {
        getSession().saveOrUpdate(transientInstance);
        getSession().flush();
        getSession().clear();
    }

    public void saveFcl(FclBlContainer fclBlContainer) throws Exception {
        getSession().save(fclBlContainer);
        getSession().flush();

    }

    public void saveFclCharges(FclBlCharges fclBlCharges) throws Exception {
        getSession().save(fclBlCharges);
        getSession().flush();
    }

    public void saveFclCost(FclBlCostCodes fclBlCostCodes) throws Exception {
        getSession().save(fclBlCostCodes);
        getSession().flush();
    }

    public void update(FclBl persistanceInstance) throws Exception {
        getSession().saveOrUpdate(persistanceInstance);
        getSession().flush();
        getSession().clear();
    }

    public int readytoUpdate(String readyToPost, Integer bolId) throws Exception {
        String queryString = "update FclBl set readyToPost='" + readyToPost + "' where bol='" + bolId + "'";
        int id = getCurrentSession().createQuery(queryString).executeUpdate();
        return id;
    }

    public void delete(FclBl persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
        getSession().flush();
    }

    public FclBl findById(Integer id) throws Exception {
        FclBl instance = (FclBl) getSession().get(
                "com.gp.cvst.logisoft.domain.FclBl", id);
        return instance;
    }

    public FclBl findById(String bolId) throws Exception {
        FclBl instance = (FclBl) getSession().createCriteria(FclBl.class).add(Restrictions.eq("bolId", bolId)).setMaxResults(1).uniqueResult();
        return instance;
    }

    public List findByExample(FclBl instance) throws Exception {
        List results = getSession().createCriteria(
                "com.gp.cvst.logisoft.hibernate.dao.FclBl").add(
                        Example.create(instance)).list();
        return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
        String queryString = "from FclBl as model where model." + propertyName + "= ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        return queryObject.list();
    }

    public List getClauseForPortname(String portname) throws Exception {
        portname = portname.replace("\"", "\\\"").replace(":", "").replace("'", "").replace("\"", "").replace("''", "").replace(" ", "");
        List clauseList = new ArrayList();
        Object[] clause = null;
        String queryString = "select f.bl_clause_id from fcl_port_configuration f,ports p where p.search_port_name like '" + portname + "%' and f.schnum = p.id";
        SQLQuery queryObject = getCurrentSession().createSQLQuery(queryString);
        Object idObject = queryObject.setMaxResults(1).uniqueResult();
        if (idObject != null) {
            int id = Integer.parseInt(idObject.toString());
            String queryString1 = "select d.code,d.codedesc from genericcode_dup d where d.id =" + id;
            SQLQuery queryObject1 = getCurrentSession().createSQLQuery(queryString1);
            List resultList = queryObject1.list();
            for (int i = 0; i < resultList.size(); i++) {
                clause = (Object[]) resultList.get(0);
                clauseList.add(0, clause[0]);
                clauseList.add(1, clause[1]);
            }
        }
        return clauseList;
    }

    public List findAll() throws Exception {
        String queryString = "from FclBl";
        Query queryObject = getSession().createQuery(queryString);
        return queryObject.list();
    }

    public List getQuotationAndBookingFileNo(String fileno) throws Exception {
        FileNumberForQuotaionBLBooking fileNumberForQuotaionBLBooking = null;
        List<FileNumberForQuotaionBLBooking> fileNumberList = new ArrayList<FileNumberForQuotaionBLBooking>();
        QuotationDAO quotationDAO = new QuotationDAO();
        //String queryString="select q.quoteId , b.bookingId from Quotation q,BookingFcl b where q.fileNo='"+fileno+"' and b.fileNo='"+fileno+"' ";
        String queryString = "select quotation.Quote_ID,booking_fcl.BookingId"
                + "			 from quotation left join  booking_fcl on quotation.file_no=booking_fcl.file_no"
                + "			 where quotation.file_no='" + fileno + "' or booking_fcl.file_no='" + fileno + "'";

        Query queryObject = getSession().createSQLQuery(queryString);
        List filelist = queryObject.list();

        if (filelist.isEmpty()) {
            queryString = "select quotation.Quote_ID,booking_fcl.BookingId"
                    + "			 from quotation right join  booking_fcl on quotation.file_no=booking_fcl.file_no"
                    + "			 where quotation.file_no='" + fileno + "' or booking_fcl.file_no='" + fileno + "'";

            queryObject = getSession().createSQLQuery(queryString);
            filelist = queryObject.list();
        }
        for (Iterator iter = filelist.iterator(); iter.hasNext();) {
            Object[] quoteBookingFclId = (Object[]) iter.next();

            if (quoteBookingFclId[0] != null) {
                Quotation quotation = quotationDAO.findById((Integer) quoteBookingFclId[0]);
                fileNumberForQuotaionBLBooking = new FileNumberForQuotaionBLBooking(quotation, null, null);
            } else if (quoteBookingFclId[1] != null) {
                BookingFclDAO bookingFclDAO = new BookingFclDAO();
                BookingFcl bookingFcl = bookingFclDAO.findById((Integer) quoteBookingFclId[1]);
                fileNumberForQuotaionBLBooking = new FileNumberForQuotaionBLBooking(null, bookingFcl, null);
            }
            fileNumberForQuotaionBLBooking.setQuotId((Integer) quoteBookingFclId[0]);
            fileNumberForQuotaionBLBooking.setBookingId((Integer) quoteBookingFclId[1]);
            fileNumberList.add(fileNumberForQuotaionBLBooking);
        }
        return fileNumberList;
    }

    public List findBolId(String bolId) throws Exception {
        getCurrentSession().flush();
        Criteria criteria = getCurrentSession().createCriteria(FclBl.class);
        criteria.add(Restrictions.eq("bolId", bolId));
        return criteria.list();
    }

    public String findBolIdObject(String BolId) throws Exception {
        String queryString = "select count (*) from fcl_bl where bolId=?0";
        Query queryObject = getSession().createQuery(queryString);
        return !queryObject.setParameter("0", BolId).uniqueResult().toString().equals("0") ? "data" : "";
    }

    public FclBl merge(FclBl detachedInstance) throws Exception {
        FclBl result = (FclBl) getSession().merge(detachedInstance);
        return result;
    }

    public void attachDirty(FclBl instance) throws Exception {
        getSession().saveOrUpdate(instance);
    }

    public void attachClean(FclBl instance) throws Exception {
        getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public int getquoteid(String quoteno) throws Exception {
        String qeury = "select  q. bolId from FclBl q where q.bol='" + quoteno + "'";
        Object result = getCurrentSession().createQuery(qeury).uniqueResult();
        return null != result ? Integer.parseInt(result.toString()) : 0;
    }

    public List getFclBlList(String bol, String bolSdate, String bolEdate,
            String booking, String quote, String consignee, String shipper,
            String forwardingAgent, String voyage, String portOfDischarge,
            String portOfLoading, String fileno) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(FclBl.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (bol != null && !bol.equals("")) {
            criteria.add(Restrictions.like("bolId", bol + "%"));
        }
        if (bolSdate != null && !bolSdate.equals("")) {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date soStartDate = (Date) dateFormat.parse(bolSdate);
            criteria.add(Restrictions.ge("bolDate", soStartDate));
        }
        if (bolEdate != null && !bolEdate.equals("")) {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date soEndDate = (Date) dateFormat.parse(bolEdate);
            criteria.add(Restrictions.le("bolDate", soEndDate));
        }
        if (booking != null && !booking.equals("")) {
            criteria.add(Restrictions.eq("bookingNo", booking));
        }
        if (quote != null && !quote.equals("")) {
            criteria.add(Restrictions.eq("quuoteNo", quote));
        }
        if (consignee != null && !consignee.equals("")) {
            criteria.add(Restrictions.like("consigneeNo", consignee + "%"));
        }
        if (forwardingAgent != null && !forwardingAgent.equals("")) {
            criteria.add(Restrictions.like("forwardAgentNo",
                    forwardingAgent + "%"));
        }
        if (shipper != null && !shipper.equals("")) {
            criteria.add(Restrictions.like("shipperNo", shipper + "%"));
        }
        if (voyage != null && !voyage.equals("")) {
            criteria.add(Restrictions.eq("voyages", voyage));
        }
        if (portOfDischarge != null && !portOfDischarge.equals("")) {
            criteria.add(Restrictions.like("portofDischarge",
                    portOfDischarge + "%"));
        }
        if (portOfLoading != null && !portOfLoading.equals("")) {
            criteria.add(Restrictions.like("portOfLoading", portOfLoading + "%"));
        }
        if (fileno != null && !fileno.equals("")) {
            criteria.add(Restrictions.like("fileNo", Integer.parseInt(fileno)));
        }
        return criteria.list();
    }

    public List showall() throws Exception {
        List list = new ArrayList();
        String queryString = "from FclBl";
        Query queryObject = getCurrentSession().createQuery(queryString);
        list = queryObject.list();
        return list;
    }

    public List showall1(String bookingNo) throws Exception {
        List list = new ArrayList();
        String queryString = "from FclBl where bookNo='" + bookingNo + "'";
        Query queryObject = getCurrentSession().createQuery(queryString);
        list = queryObject.list();
        return list;
    }

    public Integer getBOLId() throws Exception {
        String Query = "select max(f.bolId) from  FclBl f ";
        Integer result = (Integer) getCurrentSession().createQuery(Query).uniqueResult();
        return result;
    }

    public List getblMainListBuycharges(String bookingNumber, String billofladding) throws Exception {
        List<BlMainDTO> blMainListBuycharges = new ArrayList<BlMainDTO>();
        String QueryString = "select fb.charges,fb.pcollect,fb.printOnBl,fb.amount from FclBlCharges fb where fb.bolId ='" + billofladding + "'";
        List QueryObject = getCurrentSession().createQuery(QueryString).list();
        Iterator itr = QueryObject.iterator();
        BlMainDTO MainListBuychargesDTO = null;
        while (itr.hasNext()) {
            MainListBuychargesDTO = new BlMainDTO();
            Object[] row = (Object[]) itr.next();
            String costCodeDesc = (String) row[0];
            String pcollect = (String) row[1];
            String printOnBl = (String) row[2];
            Double amount = (Double) row[3];
            if (amount == null) {
                amount = 0.0;
            }
            MainListBuychargesDTO.setFrightCharges(costCodeDesc);

            MainListBuychargesDTO.setPrepaid(pcollect);
            MainListBuychargesDTO.setPrintOnBl(printOnBl);
            MainListBuychargesDTO.setAmount(amount);
            blMainListBuycharges.add(MainListBuychargesDTO);
            MainListBuychargesDTO = null;
        }
        if (QueryObject.isEmpty()) {
            MainListBuychargesDTO = new BlMainDTO();
            MainListBuychargesDTO.setFrightCharges("");
            blMainListBuycharges.add(MainListBuychargesDTO);
        }
        return blMainListBuycharges;
    }

    public List getBlMianFieldList(String bookingNumber, String billofladding,
            String comment1, String oPrinting) throws Exception {
        NumberFormat number = new DecimalFormat("##,###,##0.00");
        List<BlMainDTO> blmainFieldList = new ArrayList<BlMainDTO>();
        String QueryString = "select fbc.trailerNo,fbm.markNo,fbm.descPckgs,fbm.noOfPkgs,fbm.weightKgs,fbm.weightLbs,fbm.measureCbm,fbm.measureCft from FclBlContainer fbc, FclBlMarks fbm where fbc.bolId ='" + billofladding + "' and fbc.trailerNoId = fbm.trailerNoId";
        List QueryObject = getCurrentSession().createQuery(QueryString).list();
        Iterator itr = QueryObject.iterator();
        BlMainDTO blmainfieldDTO = null;
        while (itr.hasNext()) {
            blmainfieldDTO = new BlMainDTO();
            Object[] row = (Object[]) itr.next();
            String trailerNo = (String) row[0];
            String markNo = (String) row[1];
            String descPckgs = (String) row[2];
            Integer noOfPkgs = (Integer) row[3];
            Double weightKgs = (Double) row[4];
            Double weightLbs = (Double) row[5];
            Double measureCbm = (Double) row[6];
            Double measureCft = (Double) row[7];
            String marksnumbers = "";
            int mn = 0;
            if (trailerNo != null && !trailerNo.equals("")) {
                marksnumbers = trailerNo;
                mn = 1;
            }
            if (markNo != null && !markNo.equals("")) {
                if (mn == 0) {
                    marksnumbers = markNo + "/";
                } else {
                    marksnumbers = markNo + "/" + marksnumbers;
                }
            }

            if (weightKgs == null) {
                weightKgs = 0.0;
            }
            if (weightLbs == null) {
                weightLbs = 0.0;
            }

            if (measureCbm == null) {
                measureCbm = 0.0;
            }
            if (measureCft == null) {
                measureCft = 0.0;
            }
            if (noOfPkgs != null) {
                blmainfieldDTO.setNoofpkgs(String.valueOf(noOfPkgs));
            }
            blmainfieldDTO.setMarksandnumbers(marksnumbers);
            if (oPrinting.equals("Yes")) {
                blmainfieldDTO.setDescription(comment1 + "" + descPckgs);
            } else {
                blmainfieldDTO.setDescription(descPckgs);
            }
            blmainfieldDTO.setGoodsweightkgs(number.format(weightKgs) + " KGS, \n" + number.format(weightLbs) + " LBS");
            blmainfieldDTO.setMeasurment(number.format(measureCbm) + "CBM, \n" + number.format(measureCft) + "CFT");
            blmainFieldList.add(blmainfieldDTO);
            blmainfieldDTO = null;
        }
        if (QueryObject.isEmpty()) {
            blmainfieldDTO = new BlMainDTO();
            blmainfieldDTO.setMarksandnumbers("");
            blmainFieldList.add(blmainfieldDTO);
        }
        return blmainFieldList;
    }

    public List getsystemRules() throws Exception {
        List Addresslist = new ArrayList();
        String queryString = "select sr.ruleName from SystemRules sr";
        Query queryObject = getCurrentSession().createQuery(queryString);
        Addresslist = queryObject.list();
        return Addresslist;
    }

    public List getListOfApprovedFclBlCorrections(String BlNumber) throws Exception {
        List<FclBlCorrections> FclBlcorrectionList = null;
        String queryString = " FROM FclBlCorrections WHERE approval IS NOT "
                + "NULL AND blNumber='" + BlNumber + "' and status <> 'Disable' GROUP BY noticeNo ORDER BY  noticeNo DESC ";
        Query queryObject = getCurrentSession().createQuery(queryString);
        FclBlcorrectionList = queryObject.list();
        return FclBlcorrectionList;
    }

    public void saveCorrectionCount(int count, String bolid) throws Exception {
        String query = "UPDATE `fcl_bl` SET `correction_count`='" + count + "' WHERE `bolid`='" + bolid + "';";
        getCurrentSession().createSQLQuery(query).executeUpdate();
    }

    public List getAllBlNumbers(String BlNumber) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(FclBl.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (BlNumber != null && !BlNumber.trim().equals("")) {
            criteria.add(Restrictions.like("bolId", BlNumber + "%"));
            criteria.addOrder(Order.asc("bolId"));
        }
        criteria.setMaxResults(50);
        return criteria.list();
    }

    public List getManifestedBlNumbers(String BlNumber, String readyToPost) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(FclBl.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (BlNumber != null && !BlNumber.trim().equals("")) {
            criteria.add(Restrictions.like("bolId", BlNumber + "%"));
            criteria.addOrder(Order.asc("bolId"));
        }
        if (readyToPost != null && !readyToPost.trim().equals("")) {
            criteria.add(Restrictions.like("readyToPost", readyToPost));
            criteria.addOrder(Order.asc("readyToPost"));
        }
        criteria.setMaxResults(50);
        return criteria.list();
    }

    public Integer getmaxFile() throws Exception {
        int fileId = 0;
        String QueryString = "select max(fileNo)+1 from FclBl";
        List fileList = getCurrentSession().createQuery(QueryString).list();
        if (fileList != null && !fileList.isEmpty()) {
            fileId = (Integer) fileList.get(0);
        }
        return fileId;
    }

    public List getFclListBycontainerNumber(String containerNumber) throws Exception {
        List Addresslist = new ArrayList();
        List fclListBycontainerlist = new ArrayList();
        String queryString = "select bolId from FclBlContainer where trailerNo='" + containerNumber + "'";
        Query queryObject = getCurrentSession().createQuery(queryString);
        Addresslist = queryObject.list();
        for (Object Addresslist1 : Addresslist) {
            FclBl fclBl = new FclBl();
            Object fclBlId = (Object) Addresslist1;
            if (fclBlId != null && fclBlId != "") {
                fclBl = findById((Integer) fclBlId);
                fclListBycontainerlist.add(fclBl);
            }
        }
        return fclListBycontainerlist;
    }
    // get FileNumber

    public FclBl getFileNoObject(String fileNo) throws Exception {
        getCurrentSession().flush();
        Criteria criteria = getCurrentSession().createCriteria(FclBl.class);
        criteria.add(Restrictions.eq("fileNo", fileNo));
        criteria.addOrder(Order.desc("bol"));
        criteria.setMaxResults(1);
        return (FclBl) criteria.uniqueResult();
    }

    public FclBl getOriginalBl(String fileNo) throws Exception {
        getCurrentSession().flush();
        Criteria criteria = getCurrentSession().createCriteria(FclBl.class);
        criteria.add(Restrictions.eq("fileNo", fileNo));
        criteria.addOrder(Order.asc("bol"));
        criteria.setMaxResults(1);
        return (FclBl) criteria.uniqueResult();
    }

    public FclBl getPreviousBl(String fileNo, Integer bol) throws Exception {
        getCurrentSession().flush();
        Criteria criteria = getCurrentSession().createCriteria(FclBl.class);
        criteria.add(Restrictions.eq("fileNo", fileNo));
        criteria.add(Restrictions.ne("bol", bol));
        criteria.addOrder(Order.desc("bol"));
        criteria.setMaxResults(1);
        return (FclBl) criteria.uniqueResult();
    }

    public List<FclBl> getAllBls(String blNumber) throws Exception {
        getCurrentSession().flush();
        Criteria criteria = getCurrentSession().createCriteria(FclBl.class);
        criteria.add(Restrictions.like("bolId", blNumber + "%"));
        criteria.addOrder(Order.desc("bol"));
        return criteria.list();
    }

    public List<FclBl> getAllCorrectedBls(String blNumber) throws Exception {
        getCurrentSession().flush();
        Criteria criteria = getCurrentSession().createCriteria(FclBl.class);
        criteria.add(Restrictions.like("bolId", blNumber + "%==%"));
        criteria.addOrder(Order.desc("bol"));
        return criteria.list();
    }

    public List getAllBlUsingFileNumber(String fileNo) throws Exception {
        String queryString = "";
        queryString = "from FclBl where fileNo like '" + fileNo + "%'";
        Query queryObject = getSession().createQuery(queryString);
        return queryObject.list();
    }

    public String getBlNumberbyBLNumber(String blNumber) throws Exception {
        String queryString = "select count(bolId) from FclBl where bolId=?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", blNumber);
        String result = (String) queryObject.uniqueResult().toString();
        return result.equals("0") ? "error" : "";
    }

    public List findLatestBolId(String BolId) throws Exception {
        String queryString = "from FclBl where bolId like ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", BolId + "%");
        return queryObject.list();
    }

    public String findBol(String BolId) throws Exception {
        String queryString = "select b.bol from fcl_bl b where b.BolId = '" + BolId + "'";
        Query queryObject = getSession().createSQLQuery(queryString);
        return queryObject.uniqueResult().toString();
    }

    public String getBOLId(String fileNo) throws Exception {
        String result = "";
        String Query = "select max(f.fileNo) from  FclBl f where  f.fileNo like '" + fileNo + "%'";
        result = (String) getCurrentSession().createQuery(Query).uniqueResult();
        return result;
    }

    public String getFileNo(String bolId) throws Exception {
        String result = "";
        String Query = "select f.fileNo from  FclBl f where  f.bol = " + bolId + " ";
        result = (String) getCurrentSession().createQuery(Query).uniqueResult();
        return result;
    }

    public String getBOLId1(String bolNumber) throws Exception {
        String Query = "SELECT Bol FROM fcl_bl WHERE bolId = '" + bolNumber + "'";
        List queryList = getCurrentSession().createSQLQuery(Query).list();
        return queryList.get(0).toString();
    }
    /*public void updateCOBdetails(String Bol){
     try{
     String query = "update fcl_bl f set f.conf_onboard_comments = null,f.closed_by = null,f.confirm_on=null," +
     "f.confirm_on_board = 'N',f.verfiy_ETA =null  where f.Bol = '"+Bol+"'";
     getCurrentSession().createSQLQuery(query).executeUpdate();
     }catch(Exception e){
     e.printStackTrace();
     }
     }*/
    //testinbg corrected Bl

    public List correctedBL() throws Exception {
        String Query = "FROM FclBl WHERE bolId LIKE '%==%' order by bol DESC  ";
        return getCurrentSession().createQuery(Query).list();
    }

    public List findByDrNo(String drNumber) throws Exception {
        String queryString = "from FclBl where bolId like '%" + drNumber + "' ";
        return getCurrentSession().createQuery(queryString).list();
    }

    public List getAllFileNumber(String drNumber) throws Exception {
        String queryString = "select distinct(file_no) from fcl_bl where file_no like '" + drNumber + "%' or CONCAT('04',file_no) like '" + drNumber + "%'";
        return getCurrentSession().createSQLQuery(queryString).list();
    }

    public boolean checkAccrualPosted(String billLaddingNo) throws Exception {
        return CommonUtils.isNotEmpty(new TransactionLedgerDAO().getAllAccrualsByPassingBillNumber(billLaddingNo));
    }

    public String getBol(String fileNo) throws Exception {
        String Query = "SELECT bol FROM fcl_bl WHERE file_No='" + fileNo + "' LIMIT 1";
        Object result = getCurrentSession().createSQLQuery(Query).uniqueResult();
        return null != result ? result.toString() : "";
    }

    private String getOfrBundledCostIds(Integer blId) throws Exception {
        String costCodes = "'HAZFEE','BKRSUR','INTMDL','INTFS','INTRAMP','DEFER','INSURE','FAEXP','FFCOMM','FAECOMM','NASLAN','OCNFRT'";
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("    code_id");
        queryBuilder.append("  from");
        queryBuilder.append("    fcl_bl_costcodes");
        queryBuilder.append("  where bolid =").append(blId);
        queryBuilder.append("    and cost_code not in (").append(costCodes).append(")");
        queryBuilder.append("    and read_only_flag != ''");
        queryBuilder.append("    and booking_flag is null");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("code_id", IntegerType.INSTANCE);
        List<Integer> result = query.list();
        return CommonUtils.isNotEmpty(result) ? result.toString().replace("[", "").replace("]", "") : null;
    }

    public Integer resendToBlueScreen(FclBl fclBl, User user) throws Exception {
        int result = 0;
        if (!"M".equalsIgnoreCase(fclBl.getReadyToPost())) {
            String query = "update fcl_bl_temp set temp_update_date = SYSDATE() WHERE temp_file_No='" + fclBl.getFileNo() + "'";
            result = getCurrentSession().createSQLQuery(query).executeUpdate();
            if (result < 1) {
                query = "insert into fcl_bl_temp(temp_file_No,temp_update_date) values('" + fclBl.getFileNo() + "',SYSDATE())";
                result = getCurrentSession().createSQLQuery(query).executeUpdate();
            }
        } else {
            String ofrBundledCostIds = getOfrBundledCostIds(fclBl.getBol());
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("update");
            queryBuilder.append("  fcl_bl_costcodes");
            queryBuilder.append("  ");
            queryBuilder.append("set");
            queryBuilder.append("  manifest_modify_flag = 'yes',");
            queryBuilder.append("  accruals_updated_by = '").append(user.getLoginName()).append("',");
            queryBuilder.append("  accruals_updated_date = sysdate()");
            queryBuilder.append("  ");
            queryBuilder.append("where bolid = ").append(fclBl.getBol());
            if (CommonUtils.isNotEmpty(ofrBundledCostIds)) {
                queryBuilder.append("  and code_id not in (").append(ofrBundledCostIds).append(")");
            }
            getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
            if (CommonUtils.isNotEmpty(ofrBundledCostIds)) {
                queryBuilder = new StringBuilder();
                queryBuilder.append("update");
                queryBuilder.append("  fcl_bl_costcodes");
                queryBuilder.append("  ");
                queryBuilder.append("set");
                queryBuilder.append("  manifest_modify_flag = 'no'");
                queryBuilder.append("  ");
                queryBuilder.append("where bolid = ").append(fclBl.getBol());
                queryBuilder.append("  and code_id in (").append(ofrBundledCostIds).append(")");
                getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
            }
        }
        return result;
    }

    public void lockFclBlTemp(FclBl fclBl) throws Exception {
        String Query = "";
        String fileNo = fclBl != null ? fclBl.getFileNo() : "";
        int result = 0;
        if (fileNo != null && !fileNo.equals("")) {
            if (fileNo.contains("-")) {
                fileNo = fileNo.substring(0, fileNo.indexOf("-"));
            }
            Query = "update fcl_bl_temp set temp_update_date = SYSDATE() WHERE temp_file_No='" + fileNo + "'";
            result = getCurrentSession().createSQLQuery(Query).executeUpdate();
            if (result < 1) {
                Query = "insert into fcl_bl_temp(temp_file_No,temp_update_date) values('" + fileNo + "',SYSDATE())";
                getCurrentSession().createSQLQuery(Query).executeUpdate();
            }
        }
    }

    public List<FclBl> findByFileNo(String fileNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from FclBl where fileNo like '").append(fileNo).append("-%'");
        Query queryObject = getSession().createQuery(queryBuilder.toString());
        return queryObject.list();
    }

    public void setOverPaidStatusForImports(boolean overPaidStatus, String fileNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update fcl_bl set over_paid_status=").append(overPaidStatus).append(" where file_no='").append(fileNo).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public int getContainerCountForMainAndMultibleBl(String fileNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select count(c.trailer_no_id) from fcl_bl b ");
        queryBuilder.append("join fcl_bl_container_dtls c ");
        queryBuilder.append("on b.bol=c.bolid ");
        queryBuilder.append("where b.file_no like '").append(fileNo).append("%' ");
        queryBuilder.append("and (c.disabled_flag ='E' or c.disabled_flag is null)");
        String containerCount = getSession().createSQLQuery(queryBuilder.toString()).uniqueResult().toString();
        return Integer.parseInt(containerCount);
    }

    public List getAutomatedConfirmOnBoardReminderList() throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select bl.file_no,bl.bolid,u.email ");
        queryBuilder.append("from fcl_bl bl ");
        queryBuilder.append("left join logfile_edi edi ");
        queryBuilder.append("on(edi.file_no = bl.file_no ");
        queryBuilder.append("and edi.message_type = '315' ");
        queryBuilder.append("and edi.event_code = 'VD' ");
        queryBuilder.append("and datediff(now(),str_to_date(edi.processed_date,'%Y%m%d%k%i%s'))between 1 and 3) ");
        queryBuilder.append("join user_details u on(u.login_name = bl.edi_created_by ");
        queryBuilder.append("and u.status = 'ACTIVE'  and u.email is not null and u.email<>'')");
        queryBuilder.append("where datediff(current_date(), bl.sail_date) = 3 ");
        queryBuilder.append("and (bl.confirm_on_board is null or bl.confirm_on_board <> 'Y') ");
        queryBuilder.append("and bl.edi_created_by <> '' ");
        queryBuilder.append("and (bl.importFlag is null or bl.importflag <> 'I')");
        queryBuilder.append("and edi.id is null group by bl.file_no");
        return getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
    }

    public FclBl findByBookingNoForCOB(String bookingNo) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("from FclBl where bookingNo='").append(bookingNo);
        builder.append("' and (importFlag is null or importFlag<>'I') and ");
        builder.append("(ediCreatedBy is not null and ediCreatedBy <>'') and ");
        builder.append("(confirmOnBorad is null or confirmOnBorad <> 'Y')");
        return (FclBl) getCurrentSession().createQuery(builder.toString()).setMaxResults(1).uniqueResult();
    }

    public void autoConfirmOnBoardReminder(FclBl fclBl) throws Exception {
        User user = new UserDAO().getUserInfo(fclBl.getEdiCreatedBy());
        String outputFileName = LoadLogisoftProperties.getProperty("reportLocation");
        File file = new File(outputFileName + "/Documents/" + ReportConstants.BILLOFLADINGFILENAME + "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/");
        if (!file.exists()) {
            file.mkdirs();
        }
        ServletContext servletContext = JobScheduler.servletContext;
        String contextPath = servletContext.getRealPath("/");
        MessageResources messageResources = CommonConstants.loadMessageResources();
        String fileNo = fclBl.getFileNo();
        outputFileName = outputFileName + "/Documents/" + ReportConstants.BILLOFLADINGFILENAME + "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/ConfirmOnBoardNotice_" + fileNo + ".pdf";
        new FclBlBC().createFclBillLadingReport(fclBl.getBol().toString(), outputFileName, contextPath, messageResources, user, "ConfirmOnBoardNotice");
        String fileNoPrefix = CommonConstants.loadMessageResources().getMessage("fileNumberPrefix");
        String fileNoWithPrefix = fileNoPrefix + fileNo;
        try {
            EmailSchedulerVO mailTransaction = new EmailSchedulerVO();
            mailTransaction.setFromAddress(user.getEmail());
            mailTransaction.setToAddress(fclBl.getBookingContact());
            mailTransaction.setSubject("FCL- " + fileNoWithPrefix + " ConfirmOnBoardNotice.");
            mailTransaction.setHtmlMessage("Please Find the ConfirmOnBoardNotice Attachment.");
            mailTransaction.setTextMessage("Please Find the ConfirmOnBoardNotice Attachment.");
            mailTransaction.setName("BL");
            mailTransaction.setType(CONTACT_MODE_EMAIL);
            mailTransaction.setStatus(EMAIL_STATUS_PENDING);
            mailTransaction.setFileLocation(outputFileName);
            mailTransaction.setNoOfTries(0);
            mailTransaction.setEmailDate(new Date());
            mailTransaction.setModuleName("BL");
            mailTransaction.setModuleId(fileNo);
            new EmailschedulerDAO().save(mailTransaction);
        } catch (Exception e) {
            throw e;
        }
    }

    public Integer getLatestBol(String fileNo) throws Exception {
        String q = "select max(bol) from fcl_bl where file_no='" + fileNo + "'";
        String r = getCurrentSession().createSQLQuery(q).uniqueResult().toString();
        return Integer.parseInt(r);
    }

    public boolean isFaeReCalculaionRequiredWhileDelete(Integer bol, String chargeOrCost, String code) throws Exception {
        FclBl fclBl = new FclBlDAO().findById(bol);
        String unLocationCode = new StringFormatter().getBreketValue(fclBl.getFinalDestination());
        String ert = fclBl.getRoutedAgentCheck();
        String commisionRule = null != ert && ert.equalsIgnoreCase("yes") ? "rcom_rule" : "ncom_rule";

        String q = "select count(*) from ports p join fcl_port_configuration fp on(fp.schnum=p.id) where p.unlocationcode='" + unLocationCode + "' and " + commisionRule + "=318";
        String r = getCurrentSession().createSQLQuery(q).uniqueResult().toString();
        boolean isFaeReCalculaionRequired = !r.equals("0");

        if (isFaeReCalculaionRequired) {
            if (chargeOrCost.equals("charge")) {
                String chargeCode = code;
                isFaeReCalculaionRequired = "OCNFRT".equalsIgnoreCase(chargeCode) || "HAZFEE".equalsIgnoreCase(chargeCode)
                        || "BKRSUR".equalsIgnoreCase(chargeCode) || "INTRAMP".equalsIgnoreCase(chargeCode)
                        || "INTFS".equalsIgnoreCase(chargeCode);
            } else if (chargeOrCost.equals("cost")) {
                String costCode = code;
                isFaeReCalculaionRequired = "OCNFRT".equalsIgnoreCase(costCode) || "HAZFEE".equalsIgnoreCase(costCode)
                        || "DEFER".equalsIgnoreCase(costCode) || "INTRAMP".equalsIgnoreCase(costCode)
                        || "INTFS".equalsIgnoreCase(costCode) || "FFCOMM".equalsIgnoreCase(costCode)
                        || "TERCOMM".equalsIgnoreCase(costCode) || "BKRSUR".equalsIgnoreCase(costCode);
            }
        }

        return isFaeReCalculaionRequired;
    }

    public void saveMasterBlNo(String bol, String masterBlNo) throws Exception {
        String query = "update FclBl set newMasterBL='" + masterBlNo + "' where bol='" + bol + "'";
        getCurrentSession().createQuery(query).executeUpdate();
    }

    public String brandValue(String bol) throws Exception {
        String q = "select brand from fcl_bl where bolid ='" + bol + "'";
        Object result = getCurrentSession().createSQLQuery(q).uniqueResult();
        return null != result.toString() ? result.toString() : "";
    }

    public String getBrand(String fileNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select brand from fcl_bl where file_no ='").append(fileNo).append("' limit 1");
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null != result.toString() ? result.toString() : "";
    }

    public Integer getBolId(String fileNo) throws Exception {
        String q = "select bol from fcl_bl where file_no='" + fileNo + "'";
        String r = getCurrentSession().createSQLQuery(q).uniqueResult().toString();
        return Integer.parseInt(r);
    }
     public void setManifestRev(Integer bol, String manifestrev) throws Exception {
        String query = "update FclBl set manifest_rev='" + manifestrev + "' where bol='" + bol + "'";
        getCurrentSession().createQuery(query).executeUpdate();
    }
    
     public String getCorrectedBolId(String fileNo) throws Exception {
        String queryString = "SELECT BolId FROM fcl_bl WHERE file_no ='" +fileNo + "' AND bolId LIKE '%==%' ORDER BY bol DESC LIMIT 1";
        Query queryObject = getSession().createSQLQuery(queryString);
        return queryObject.uniqueResult().toString();
    }
     
     public String getBillLaddingNo(String fileNo) throws Exception {
        String queryString = "SELECT `BolId` FROM fcl_bl WHERE file_No='" +fileNo + "' LIMIT 1";
        Query queryObject = getCurrentSession().createSQLQuery(queryString);
        return (String) queryObject.uniqueResult();
    }
}

package com.gp.cvst.logisoft.hibernate.dao;

import com.gp.cong.logisoft.domain.GenericCode;
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
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.beans.FclCfclChargeBean;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.BookingfclUnits;
import com.gp.cvst.logisoft.domain.Charges;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlCostCodes;
import com.gp.cvst.logisoft.domain.HazmatMaterial;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.reports.dto.SearchBookingReportDTO;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.LockOptions;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.ListType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

/**
 * Data access object (DAO) for domain model class BookingFcl.
 *
 * @see com.gp.cvst.logisoft.hibernate.dao.BookingFcl
 * @author MyEclipse - Hibernate Tools
 */
public class BookingFclDAO extends BaseHibernateDAO {

    public void save(BookingFcl transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public Iterator getAllbookingNo() throws Exception {
        Iterator results = null;
        String queryString = "";
        queryString = "select b.bookingId,b.bookingNumber from BookingFcl b";
        Query queryObject = getSession().createQuery(queryString);
        results = queryObject.list().iterator();
        return results;
    }

    public List findAccountPrefix() throws Exception {
        List list = new ArrayList();
        String queryString = "from BookingFcl order by bookingId desc";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setMaxResults(1);
        list = queryObject.list();
        return list;
    }

    public Integer getmaxFile() throws Exception {
        int fileId = 0;
        String QueryString = "select max(fileNo)+1 from BookingFcl";
        List fileList = getCurrentSession().createQuery(QueryString).list();
        if (fileList.size() > 0) {
            fileId = (Integer) fileList.get(0);
        }
        return fileId;
    }

    public void delete(BookingFcl persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
        getSession().flush();
    }

    public void deleteFromScheduler(BookingFcl persistentInstance) throws Exception {
        Transaction transaction = getCurrentSession().beginTransaction();
        getCurrentSession().delete(persistentInstance);
        getCurrentSession().flush();
        transaction.commit();
    }

    public BookingFcl findById(java.lang.Integer id) throws Exception {
        BookingFcl instance = (BookingFcl) getSession().get("com.gp.cvst.logisoft.domain.BookingFcl", id);
        return instance;
    }

    public List findByExample(BookingFcl instance) throws Exception {
        List results = getSession().createCriteria("com.gp.cvst.logisoft.hibernate.dao.BookingFcl").add(Example.create(instance)).list();
        return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
        String queryString = "from BookingFcl as model where model." + propertyName + "= ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        return queryObject.list();
    }

    public BookingFcl merge(BookingFcl detachedInstance) throws Exception {
        BookingFcl result = (BookingFcl) getSession().merge(detachedInstance);
        return result;
    }

    public void attachDirty(BookingFcl instance) throws Exception {
        getSession().saveOrUpdate(instance);
        getSession().flush();
    }

    public void attachClean(BookingFcl instance) throws Exception {
        getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public void update(BookingFcl persistanceInstance) throws Exception {
        getSession().saveOrUpdate(persistanceInstance);
        getSession().flush();
    }
    //Nagendra

    public List searchForBookingNo(String bookingNo) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(BookingFcl.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (bookingNo != null && !bookingNo.equals("")) {
            criteria.add(Restrictions.like("SSBookingNo", bookingNo + "%"));
            criteria.addOrder(Order.asc("SSBookingNo"));
        }
        return criteria.list();
    }

    public List getSearchBookingList(String bookingEndDate, String bookingStartDate, String bookingNo, String portofOrigin, String Destination, String OriginTerminal, String portofDischarge, String userName, String sslBooking) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(BookingFcl.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        if (bookingNo != null && !bookingNo.equals("")) {
            criteria.add(Restrictions.like("fileNo", bookingNo));

            criteria.addOrder(Order.asc("fileNo"));
        }

        if (portofOrigin != null && !portofOrigin.equals("")) {
            criteria.add(Restrictions.like("portofOrgin", portofOrigin + "%"));
            criteria.addOrder(Order.asc("portofOrgin"));
        }
        if (OriginTerminal != null && !OriginTerminal.equals("")) {
            criteria.add(Restrictions.like("originTerminal", OriginTerminal + "%"));
        }
        if (Destination != null && !Destination.equals("")) {
            criteria.add(Restrictions.like("destination", Destination + "%"));
            criteria.addOrder(Order.asc("destination"));
        }
        if (portofDischarge != null && !portofDischarge.equals("")) {
            criteria.add(Restrictions.like("portofDischarge", portofDischarge + "%"));
        }
        if (userName != null && !userName.equals("")) {
            criteria.add(Restrictions.like("username", userName + "%"));
            criteria.addOrder(Order.asc("username"));
        }
        if (sslBooking != null && !sslBooking.equals("")) {
            criteria.add(Restrictions.like("SSLine", sslBooking + "%"));
            criteria.addOrder(Order.asc("SSLine"));
        }
        if (bookingStartDate != null && !bookingStartDate.equals("")) {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date soStartDate = (Date) dateFormat.parse(bookingStartDate);
            criteria.add(Restrictions.ge("bookingDate", soStartDate));
            criteria.addOrder(Order.asc("bookingDate"));
        }
        if (bookingEndDate != null && !bookingEndDate.equals("")) {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date soEndDate = (Date) dateFormat.parse(bookingEndDate);
            criteria.add(Restrictions.le("bookingDate", soEndDate));
            criteria.addOrder(Order.asc("bookingDate"));
        }
        return criteria.list();
    }

    public int getBookingid(String quoteno) throws Exception {
        int result = 0;
        String qeury = "select  q. bookingId from BookingFcl q where q.bookingNumber='" + quoteno + "'";
        Iterator it = getCurrentSession().createQuery(qeury).list().iterator();
        result = (Integer) it.next();
        return result;
    }

    public String getBookingId(String fileNumber) throws Exception {
        String fileNo = fileNumber.indexOf("-") > 1 ? fileNumber.substring(0, fileNumber.indexOf("-")) : fileNumber;
        String qeury = "select q. bookingId from Booking_Fcl q where q.file_No='" + fileNo + "'";
        Object bookingId = getCurrentSession().createSQLQuery(qeury).setMaxResults(1).uniqueResult();
        return null != bookingId ? bookingId.toString() : "";
    }

    public String getFileNo(String bookingId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select if(file_no!='',file_no,'0') as file_no");
        queryBuilder.append(" from booking_fcl");
        queryBuilder.append(" where bookingid = ").append(bookingId);
        queryBuilder.append(" limit 1");
        return (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public String getQuoteNo(String bookingId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select if(quote_no!='',quote_no,'0') as file_no");
        queryBuilder.append(" from booking_fcl");
        queryBuilder.append(" where bookingid = ").append(bookingId);
        queryBuilder.append(" limit 1");
        return (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public BookingFcl getBookingidUnique(String quoteno) throws Exception {
        String qeury = "from BookingFcl q where q.SSBookingNo='" + quoteno + "'";
        return (BookingFcl) getCurrentSession().createSQLQuery(qeury).setMaxResults(1).uniqueResult();
    }
    //for show all

    public List getShowallList() throws Exception {
        String queryStringwshow = "from BookingFcl";
        List QueryObject = getCurrentSession().createQuery(queryStringwshow).list();
        return QueryObject;
    }

    public List getbookingListIterator(String bookingNumber) throws Exception {
        NumberFormat number = new DecimalFormat("##,###,##0.00");
        List<SearchBookingReportDTO> searchbookingList = new ArrayList<SearchBookingReportDTO>();
        String queryString = "";
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        SearchBookingReportDTO BookingDTO = null;
        if (!bookingNumber.equals("")) {
            queryString = "select bl.bookingNumber,bl.bookingDate,bl.shipper,bl.addressforShipper,bl.addressforForwarder,bl.originTerminal.terminalLocation,bl.portofDischarge.portname,bl.destination,bl.portofOrgin,bl.attenName,bl.etd,bl.eta,bl.vessel,bl.voyageCarrier,bl.cutofDate,bl.remarks,bl.exportPositoningPickup,bl.exportDevliery,bl.truckerCode,bl.positioningDate,bl.shipper,bl.addressforShipper,bl.username,bl.addressForExpPositioning,bl.totalCharges from BookingFcl bl where bl.bookingNumber='" + bookingNumber + "'";
        }
        List QueryObject = getCurrentSession().createQuery(queryString).list();
        Iterator itr = QueryObject.iterator();
        while (itr.hasNext()) {

            BookingDTO = new SearchBookingReportDTO();
            Object[] row = (Object[]) itr.next();
            String bookingNumbers = (String) row[0];
            Date bookingDate = (Date) row[1];
            String Shipper = (String) row[2];
            String addressforShipper = (String) row[3];
            String addressforForwarder = (String) row[4];
            String originTerminal = (String) row[5];
            String Portofdischare = (String) row[6];
            String destination = (String) row[7];
            String portoforigin = (String) row[8];
            String attn = (String) row[9];
            Date etd = (Date) row[10];
            Date eta = (Date) row[11];
            String vessel = (String) row[12];
            String voyageCarrier = (String) row[13];
            Date cutofDate = (Date) row[14];
            String remarks = (String) row[15];
            String exportPosition = (String) row[16];
            String exportDelivery = (String) row[17];
            String tuckerCode = (String) row[18];
            Date positioningDt = (Date) row[19];
            String shipper = (String) row[20];
            String username = (String) row[22];
            String addForExpPositioning = (String) row[23];
            Double totalcharges = (Double) row[24];
            if (totalcharges == null) {
                totalcharges = 0.0;
            }
            String bdate = "";
            if (bookingDate != null) {
                bdate = sdf.format(bookingDate);
            }
            String etd1 = "";
            if (etd != null) {

                etd1 = sdf.format(etd);

            }
            String eta1 = "";
            if (eta != null) {
                eta1 = sdf.format(eta);

            }
            String cutofdate = "";
            if (cutofDate != null) {
                cutofdate = sdf.format(cutofDate);
            }
            String positioningDt1 = "";
            if (positioningDt != null) {
                positioningDt1 = sdf.format(positioningDt);

            }
            BookingDTO.setBookingnumber(bookingNumbers);
            BookingDTO.setBookingdate(bdate);
            BookingDTO.setShipper(Shipper);
            BookingDTO.setAddressforShipper(addressforShipper);
            BookingDTO.setAddressforForwarder(addressforForwarder);
            BookingDTO.setPortoforigin(portoforigin);
            BookingDTO.setPortofdischarge(Portofdischare);
            BookingDTO.setOriginterminal(originTerminal);
            BookingDTO.setDestination(destination);
            BookingDTO.setRemarks(remarks);
            BookingDTO.setAttenName(attn);
            BookingDTO.setEtd(etd1);
            BookingDTO.setEta(eta1);
            BookingDTO.setVessel(vessel);
            BookingDTO.setVoyageCarrier(voyageCarrier);
            BookingDTO.setCutofDate(cutofdate);
            BookingDTO.setExportPositoningPickup(exportPosition);
            BookingDTO.setExportDevliery(exportDelivery);
            BookingDTO.setTruckerCode(tuckerCode);
            BookingDTO.setPositioningDate(positioningDt1);
            BookingDTO.setAddressforShipper(addressforShipper);
            BookingDTO.setShipper(shipper);
            BookingDTO.setUsername(username);
            BookingDTO.setAddressForExpPositioning(addForExpPositioning);
            BookingDTO.setRates(number.format(totalcharges));
            searchbookingList.add(BookingDTO);
            BookingDTO = null;
        }
        return searchbookingList;
    }

    public List getbookingList(String bookingNumber) throws Exception {
        List<SearchBookingReportDTO> searchbookingList = new ArrayList<SearchBookingReportDTO>();
        String queryString = "";
        String QueryString1 = "Select Distinct bu.unitType.codedesc from BookingfclUnits bu where bu.bookingNumber='" + bookingNumber + "'";
        List qo = getCurrentSession().createQuery(QueryString1).list();
        int i = 0;
        while (qo.size() > i) {
            String unitid = (String) qo.get(i);
            if (!bookingNumber.equals("") && unitid != null) {

                queryString = "Select bu.unitType.codedesc,bu.numbers from BookingfclUnits bu where bu.bookingNumber='" + bookingNumber + "' and bu.unitType.codedesc='" + unitid + "'";
            }
            List QueryObject = getCurrentSession().createQuery(queryString).list();
            Iterator itr = QueryObject.iterator();
            SearchBookingReportDTO BookingDTO = null;

            int j = 0;
            while (itr.hasNext()) {
                BookingDTO = new SearchBookingReportDTO();
                Object[] row = (Object[]) itr.next();
                String unitType = (String) row[0];
                String numbers = (String) row[1];
                BookingDTO.setUnitType(unitType);
                BookingDTO.setNumbers(numbers + "X " + unitType);
                if (j == 0) {
                    searchbookingList.add(BookingDTO);
                    j++;
                }
                BookingDTO = null;
            }

            i++;
        }//while(qo.size())
        return searchbookingList;
    }
    //Nagendra For Reports
    NumberFormat number = new DecimalFormat("##,###,##0.00");
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    SimpleDateFormat newSdf = new SimpleDateFormat("yyyy-MM-dd");

    public List getSearchBookingListReport(String bookingNo, String portofOrigin, String OriginTerminal, String portofDischarge, String Destination) throws Exception {
        List<SearchBookingReportDTO> searchbookingList = new ArrayList<SearchBookingReportDTO>();
        String queryString = "";
        if (!bookingNo.equals("") && portofOrigin.equals("") && Destination.equals("") && OriginTerminal.equals("") && portofDischarge.equals("")) {
            queryString = "select bl.bookingNumber,bl.bookingDate,bl.shipper,bl.addressforShipper,"
                    + "bl.addressforForwarder,bl.originTerminal.terminalLocation,bl.portofDischarge.portname,"
                    + "bl.destination,bl.portofOrgin,bu.unitType,bu.numbers,bu.rates,bu.ratesdescription  from BookingFcl bl,BookingfclUnits bu where bl.bookingNumber='" + bookingNo + "'and bu.bookingNumber='" + bookingNo + "' ";
        }
        List QueryObject = getCurrentSession().createQuery(queryString).list();
        Iterator itr = QueryObject.iterator();
        SearchBookingReportDTO BookingDTO = null;
        while (itr.hasNext()) {
            BookingDTO = new SearchBookingReportDTO();
            Object[] row = (Object[]) itr.next();
            String bookingNumber = (String) row[0];
            Date bookingDate = (Date) row[1];
            String date = String.valueOf(bookingDate);
            String Shipper = (String) row[2];
            String addressforShipper = (String) row[3];
            String addressforForwarder = (String) row[4];
            String originTerminal = (String) row[5];
            String Portofdischare = (String) row[6];
            String destination = (String) row[7];
            String portoforigin = (String) row[8];
            String unitType = (String) row[9];
            String numbers = (String) row[10];
            String ratesDesc = (String) row[12];
            Double rates = (Double) row[11];

            List QueryObject1 = getCurrentSession().createQuery(queryString).list();
            Iterator itr1 = QueryObject1.iterator();
            while (itr1.hasNext()) {

                if (bookingNumber == null) {
                    bookingNumber = "";
                }
                if (bookingDate == null) {
                    bookingDate = null;

                }
                if (Shipper == null) {
                    Shipper = "";
                }
                if (addressforShipper == null) {
                    addressforShipper = "";
                }
                if (addressforForwarder == null) {
                    addressforForwarder = "";
                }

                if (originTerminal == null) {
                    originTerminal = "";
                }
                if (Portofdischare == null) {
                    Portofdischare = "";
                }
                if (unitType == null) {
                    unitType = "";
                }
                if (numbers == null) {
                    numbers = "";
                }
                if (rates == null) {
                    rates = 0.00;
                }
                if (ratesDesc == null) {
                    ratesDesc = "";
                }
                BookingDTO.setBookingnumber(bookingNumber);
                BookingDTO.setBookingdate(date);
                BookingDTO.setPortoforigin(portoforigin);
                BookingDTO.setPortofdischarge(Portofdischare);
                BookingDTO.setOriginterminal(originTerminal);
                BookingDTO.setDestination(destination);
                BookingDTO.setNumbers(numbers);
                BookingDTO.setRates(number.format(rates));
                BookingDTO.setRatedesc(ratesDesc);
                searchbookingList.add(BookingDTO);
                BookingDTO = null;
            }

        }
        return searchbookingList;
    }

// Booking Reports
    public List getbookingListIterator1(String bookingNumber) throws Exception {
        List<SearchBookingReportDTO> searchbookingList = new ArrayList<SearchBookingReportDTO>();
        String queryString = "";
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        SearchBookingReportDTO BookingDTO = null;
        if (!bookingNumber.equals("")) {
            //queryString ="select bl.SSBookingNo,bl.bookingDate,bl.shipper,bl.addressforShipper,bl.addressforForwarder,bl.originTerminal.terminalLocation,bl.portofDischarge.portname,bl.destination,bl.portofOrgin,bl.attenName,bl.etd,bl.eta,bl.vessel,bl.voyageCarrier,bl.cutofDate,bl.remarks,bl.exportPositoningPickup,bl.exportDevliery,bl.name,bl.positioningDate,bl.shipper,bl.addressforShipper,bl.username,bl.addressForExpPositioning,bl.comdesc,bl.SSLine,bl.EarliestPickUpDate,bl.EmptyReturnDate,bl.VoyDocCutOff,bl.shipperPhone,bl.shipperFax,bl.TruckerPhone,bl.LoadDate,bl.sslname,bl.portCutOff,bl.TruckerEmail,bl.address,bl.totalCharges,bl.contractReference,bl.shipperEmail,bl.docCutOff,bl.portCutOff,bl.VoyDocCutOff,bl.dateInYard,bl.positioningDate,bl.sslname,bl.forward,bl.consignee,bl.addressforConsingee,bl.ForwarderPhone,bl.loadcontact,bl.loadphone,bl.forwarderFax,bl.forwarderZip,bl.ForwarderEmail,bl.ConsingeePhone,bl.ConsingeeEmail,bl.consigneeZip,bl.consigneeFax,bl.EmptyPickUpDate,bl.yen,bl.baht,bl.bdt,bl.cyp,bl.eur,bl.hkd,bl.lkr,bl.nt,bl.prs,bl.rmb,bl.won,bl.addessForExpDelivery,bl.exportPositoningPickup,bl.addressForExpPositioning,bl.LoadLocation,bl.positionlocation,bl.emptypickupaddress from BookingFcl bl where bl.bookingNumber='"+bookingNumber+"'";
            queryString = "select bl.SSBookingNo,bl.bookingDate,bl.shipper,bl.addressforShipper,bl.addressforForwarder,bl.originTerminal,bl.portofDischarge,bl.destination,bl.portofOrgin,bl.attenName,bl.etd,bl.eta,bl.vessel,bl.voyageCarrier,bl.cutofDate,bl.remarks,bl.exportPositoningPickup,bl.exportDevliery,bl.name,bl.positioningDate,bl.shipper,bl.addressforShipper,bl.username,bl.addressForExpPositioning,bl.comdesc,bl.SSLine,bl.EarliestPickUpDate,bl.EmptyReturnDate,bl.VoyDocCutOff,bl.shipperPhone,bl.shipperFax,bl.TruckerPhone,bl.LoadDate,bl.sslname,bl.portCutOff,bl.TruckerEmail,bl.address,bl.totalCharges,bl.contractReference,bl.shipperEmail,bl.docCutOff,bl.portCutOff,bl.VoyDocCutOff,bl.dateInYard,bl.positioningDate,bl.sslname,bl.forward,bl.consignee,bl.addressforConsingee,bl.ForwarderPhone,bl.loadcontact,bl.loadphone,bl.forwarderFax,bl.forwarderZip,bl.ForwarderEmail,bl.ConsingeePhone,bl.ConsingeeEmail,bl.consigneeZip,bl.consigneeFax,bl.EmptyPickUpDate,bl.yen,bl.baht,bl.bdt,bl.cyp,bl.eur,bl.hkd,bl.lkr,bl.nt,bl.prs,bl.rmb,bl.won,bl.addessForExpDelivery,bl.exportPositoningPickup,bl.addressForExpPositioning,bl.LoadLocation,bl.positionlocation,bl.emptypickupaddress from BookingFcl bl where bl.bookingNumber='" + bookingNumber + "'";

        }
        List QueryObject = getCurrentSession().createQuery(queryString).list();
        Iterator itr = QueryObject.iterator();
        while (itr.hasNext()) {
            BookingDTO = new SearchBookingReportDTO();
            Object[] row = (Object[]) itr.next();
            String bookingNumbers = (String) row[0];
            Date bookingDate = (Date) row[1];
            String date = "";

            if (bookingDate != null) {
                date = sdf.format(bookingDate);
            }
            String Shipper = (String) row[2];
            String addressforShipper = (String) row[3];
            String addressforForwarder = (String) row[4];
            String originTerminal = (String) row[5];
            String Portofdischare = (String) row[6];
            String destination = (String) row[7];
            String portoforigin = (String) row[8];
            String attn = (String) row[9];
            Date etd = (Date) row[10];
            Date eta = (Date) row[11];
            String vessel = (String) row[12];
            String voyageCarrier = (String) row[13];
            Date cutofDate = (Date) row[14];
            String remarks = (String) row[15];
            String exportPosition = (String) row[16];
            String exportDelivery = (String) row[17];
            String tuckerCode = (String) row[18];
            Date positioningDt = (Date) row[19];
            String shipper = (String) row[20];
            String addrForShipper = (String) row[21];
            String username = (String) row[22];
            String addForExpPositioning = (String) row[23];
            String comodity = (String) row[24];
            String ssline = (String) row[25];
            Date EarliestPickUpDate = (Date) row[26];
            Date EmptyReturnDate = (Date) row[27];
            Date VoyDocCutOff = (Date) row[28];
            String shipperFax = (String) row[29];
            String shipperphone = (String) row[30];
            String truckerPhone = (String) row[31];
            Date LoadDate = (Date) row[32];
            String sslName = (String) row[33];
            Date portCut = (Date) row[34];
            String truckerEmail = (String) row[35];
            String truckerAddress = (String) row[36];
            Double totalCharges = (Double) row[37];
            String contractReference = (String) row[38];
            String shipperEmail = (String) row[39];
            Date doccut = (Date) row[40];
            Date portCutOff = (Date) row[41];
            Date VoyDocCutOffdate = (Date) row[42];
            Date dateInYard = (Date) row[43];
            Date positioningDate = (Date) row[44];
            String carriercode = (String) row[45];
            String forward = (String) row[46];
            String consignee = (String) row[47];
            String addressforConsingee = (String) row[48];
            String ForwarderPhone = (String) row[49];
            String loadcontact = (String) row[50];
            String loadphone = (String) row[51];
            String forwarderFax = (String) row[52];
            String forwarderZip = (String) row[53];
            String forwarderemail = (String) row[54];
            String ConsingeePhone = (String) row[55];
            String ConsingeeEmail = (String) row[56];
            String consigneeZip = (String) row[57];
            String consigneeFax = (String) row[58];
            Date equipdate = (Date) row[59];
            Double yen = (Double) row[60];
            Double baht = (Double) row[61];
            Double bdt = (Double) row[62];
            Double cyp = (Double) row[63];
            Double eur = (Double) row[64];
            Double hkd = (Double) row[65];
            Double lkr = (Double) row[66];
            Double nt = (Double) row[67];
            Double prs = (Double) row[68];
            Double rmb = (Double) row[69];
            Double won = (Double) row[70];
            String addessForExpDelivery = (String) row[71];
            String exportPositoningPickup = (String) row[72];
            String addressForExpPositioning = (String) row[73];
            String LoadLocation = (String) row[74];
            String positionlocation = (String) row[75];
            String emptypickupaddress = (String) row[76];

            String positioningDate1 = "";
            String autocutoff = "";
            String doccut1 = "";
            String portCutOff1 = "";
            String VoyDocCutOff1 = "";
            if (positioningDate != null && !positioningDate.equals("")) {
                positioningDate1 = sdf.format(positioningDate);
            }
            if (VoyDocCutOffdate != null && !VoyDocCutOffdate.equals("")) {
                VoyDocCutOff1 = sdf.format(VoyDocCutOffdate);
            }
            if (portCutOff != null && !portCutOff.equals("")) {
                portCutOff1 = sdf.format(portCutOff);
            }
            if (doccut != null && !doccut.equals("")) {
                doccut1 = sdf1.format(doccut);
            }
            if (dateInYard != null && !dateInYard.equals("")) {
                autocutoff = sdf.format(dateInYard);
            }
            String portcut = "";
            if (portCut != null && !portCut.equals("")) {
                portcut = sdf.format(portCut);
            }
            String etd1 = "";
            if (etd != null && !etd.equals("")) {
                etd1 = sdf.format(etd);
            }
            String earlistreturndate = "";
            if (LoadDate != null && !LoadDate.equals("")) {
                earlistreturndate = sdf.format(LoadDate);
            }
            String eta1 = "";
            if (eta != null && !eta.equals("")) {
                eta1 = sdf.format(eta);
            }
            String cutofdate = "";
            if (cutofDate != null && !cutofDate.equals("")) {
                cutofdate = sdf.format(cutofDate);
            }
            String positioningDt1 = "";
            if (positioningDt != null && !positioningDt.equals("")) {
                positioningDt1 = sdf.format(positioningDt);
            }
            String earlistPickUp = "";
            String earlistpickupdate = "";
            if (EarliestPickUpDate != null && !EarliestPickUpDate.equals("")) {
                earlistpickupdate = sdf.format(EarliestPickUpDate);
            }
            if (EmptyReturnDate != null && !EmptyReturnDate.equals("")) {
                earlistPickUp = sdf.format(EmptyReturnDate);
            }

            BookingDTO.setBookingnumber(bookingNumbers);
            BookingDTO.setBookingdate(date);
            BookingDTO.setShipper(Shipper);
            BookingDTO.setAddressforShipper(addressforShipper);
            BookingDTO.setAddressforForwarder(addressforForwarder);
            BookingDTO.setPortoforigin(portoforigin);
            BookingDTO.setPortofdischarge(Portofdischare);
            BookingDTO.setOriginterminal(originTerminal);
            BookingDTO.setDestination(destination);
            BookingDTO.setEtd(etd1);
            BookingDTO.setEta(eta1);
            BookingDTO.setVessel(vessel);
            BookingDTO.setVoyageCarrier(voyageCarrier);
            BookingDTO.setCutofDate(cutofdate);
            BookingDTO.setPoe(exportDelivery);
            BookingDTO.setTrucker(tuckerCode);
            BookingDTO.setPositioningDate(positioningDt1);
            BookingDTO.setAddressforShipper(addressforShipper);
            BookingDTO.setShipper(shipper);
            BookingDTO.setUsername(username);
            BookingDTO.setAddressForExpPositioning(addForExpPositioning);
            BookingDTO.setEarlistreturndate(earlistreturndate);
            BookingDTO.setEarlistpickupdate(earlistpickupdate);
            BookingDTO.setSsline(sslName);
            BookingDTO.setComodity(comodity);
            BookingDTO.setBargecut(VoyDocCutOff1);
            BookingDTO.setPhone(loadphone);
            BookingDTO.setFax(shipperFax);
            BookingDTO.setTruckerphone(truckerPhone);
            BookingDTO.setPortCutOff(portcut);
            BookingDTO.setContact(loadcontact);
            BookingDTO.setAddress(truckerAddress);
            BookingDTO.setRateconfirmation(String.valueOf(totalCharges));
            BookingDTO.setBookingno(contractReference);
            BookingDTO.setAttname(attn);
            BookingDTO.setEmail(shipperEmail);
            BookingDTO.setDoccut(doccut1);
            BookingDTO.setPortcut(portCutOff1);
            BookingDTO.setAutocutoff(cutofdate);
            BookingDTO.setPosdate(positioningDate1);
            BookingDTO.setCarriername(carriercode);
            BookingDTO.setShipperName(shipper);
            BookingDTO.setForwarderName(forward);
            BookingDTO.setConsigneeName(consignee);
            BookingDTO.setShipperAddress(addrForShipper);
            BookingDTO.setForwarderAddress(addressforForwarder);
            BookingDTO.setConsigneeAddress(addressforConsingee);
            BookingDTO.setForwarderPhone(ForwarderPhone);
            BookingDTO.setForwardfax(forwarderFax);
            BookingDTO.setForwardzip(forwarderZip);
            BookingDTO.setForwardemail(forwarderemail);
            BookingDTO.setConsigneephone(ConsingeePhone);
            BookingDTO.setConsigneeemail(ConsingeeEmail);
            BookingDTO.setConsigneefax(consigneeFax);
            BookingDTO.setConsigneezip(consigneeZip);
            BookingDTO.setShipperPhone(shipperphone);
            BookingDTO.setRemarks(remarks);
            BookingDTO.setYen(String.valueOf(yen));
            BookingDTO.setBaht(String.valueOf(baht));
            BookingDTO.setBdt(String.valueOf(bdt));
            BookingDTO.setCyp(String.valueOf(cyp));
            BookingDTO.setEur(String.valueOf(eur));
            BookingDTO.setHkd(String.valueOf(hkd));
            BookingDTO.setLkr(String.valueOf(lkr));
            BookingDTO.setNt(String.valueOf(nt));
            BookingDTO.setPrs(String.valueOf(prs));
            BookingDTO.setRmb(String.valueOf(rmb));
            BookingDTO.setWon(String.valueOf(won));
            BookingDTO.setAddessForExpDelivery(addessForExpDelivery);
            BookingDTO.setExportPositoningPickup(exportPositoningPickup);
            BookingDTO.setEmptypickupaddress(emptypickupaddress);
            BookingDTO.setLoadLocation(LoadLocation);
            BookingDTO.setPositionlocation(positionlocation);
            searchbookingList.add(BookingDTO);
            BookingDTO = null;
        }
        return searchbookingList;
    }

    public String getBreakBulk(String fileNo) {
        String queryString = "select break_bulk from booking_fcl where file_no = ?0";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", fileNo);
        queryObject.setMaxResults(1);
        String result = (String) queryObject.uniqueResult();
        return result;
    }

    public List getBKGConfFieldsList1(String bookingNumber) {
        List<SearchBookingReportDTO> bookingConformationfieldList = new ArrayList<SearchBookingReportDTO>();
        String queryString = "";
        String numbers = "";
        queryString = "select Distinct(bf.unitType.codedesc) from BookingfclUnits bf where bf.bookingNumber='" + bookingNumber + "' order by unitType.id";
        List QueryObjectList = getCurrentSession().createQuery(queryString).list();
        SearchBookingReportDTO bookingConfDTO = null;
        if (!QueryObjectList.isEmpty()) {
            int lsize = QueryObjectList.size();
            int t = 0;
            while (lsize > t) {
                String equipment = (String) QueryObjectList.get(t);
                String queryString1 = "SELECT bfu.numbers FROM bookingfcl_units bfu,genericcode_dup gc WHERE REPLACE(gc.codedesc,\"\'\",\"\")='" + equipment.replace("'", "") + "' AND bfu.bookingNumber='" + bookingNumber + "' AND gc.id = bfu.unittype";
                List QueryObjectList1 = getCurrentSession().createSQLQuery(queryString1).list();

                if (!QueryObjectList1.isEmpty()) {
                    bookingConfDTO = new SearchBookingReportDTO();
                    numbers = (String) QueryObjectList1.get(0);
                    bookingConfDTO.setEquipment(numbers + " x " + equipment);

                    bookingConformationfieldList.add(bookingConfDTO);
                    bookingConfDTO = null;
                }
                //if
                t++;
            }//while

        } else {
            bookingConfDTO = new SearchBookingReportDTO();
            bookingConfDTO.setEquipment("");
            bookingConformationfieldList.add(bookingConfDTO);
            bookingConfDTO = null;
        }

        return bookingConformationfieldList;
    }

    public List getworkOrderFieldsList(String bookingNumber) {
        List<SearchBookingReportDTO> bookingConformationfieldList = new ArrayList<SearchBookingReportDTO>();
        String queryString = "select Distinct(bf.unitType.codedesc) from BookingfclUnits bf where bf.bookingNumber='" + bookingNumber + "' and "
                + "bf.approveBl='Yes' and bf.unitType IS NOT NULL";
        List QueryObjectList = getCurrentSession().createQuery(queryString).list();
        SearchBookingReportDTO WorkOrderDTO = null;
        if (!QueryObjectList.isEmpty()) {
            int lsize = QueryObjectList.size();
            int count = 0;
            while (lsize > 0) {
                WorkOrderDTO = new SearchBookingReportDTO();
                String equipment = (String) QueryObjectList.get(count);
                String queryString1 = "select sum(bf.amount+bf.markUp) from BookingfclUnits bf where bf.unitType.codedesc='" + equipment + "' and bf.bookingNumber='" + bookingNumber + "'";
                List QueryObjectList1 = getCurrentSession().createQuery(queryString1).list();
                Double totAmount = 0.0;
                if (!QueryObjectList1.isEmpty()) {
                    totAmount = (Double) QueryObjectList1.get(0);
                }
                WorkOrderDTO.setEquipment(equipment);
                WorkOrderDTO.setAmount1(number.format(totAmount));
                bookingConformationfieldList.add(WorkOrderDTO);
                count++;
                lsize--;
            }
        }
        return bookingConformationfieldList;
    }
    //COST SHEET LIST

    public List getcostSheetFieldList(String bookingNumber) throws Exception {
        List<SearchBookingReportDTO> costSheetFieldList = new ArrayList<SearchBookingReportDTO>();
        String queryString = "";
        Double sellRateTotal = 0.00;
        Double buyRateTotal = 0.00;
        queryString = "select bu.chgCode,bu.sellRate,bu.amount from BookingfclUnits bu"
                + " where bu.bookingNumber='" + bookingNumber + "' and approveBl = 'Yes'";
        List QueryObject = getCurrentSession().createQuery(queryString).list();
        Iterator itr = QueryObject.iterator();
        SearchBookingReportDTO BookingConformationDTO = null;
        if (!QueryObject.isEmpty()) {
            while (itr.hasNext()) {
                BookingConformationDTO = new SearchBookingReportDTO();
                Object[] row = (Object[]) itr.next();
                String chgCode = (String) row[0];
                Double sellrate = (Double) row[1];
                Double amount = (Double) row[2];
                String sellRate = "";
                if (sellrate == null) {
                    sellrate = 0.0;
                }
                if (amount == null) {
                    amount = 0.0;
                }
                if (sellrate != null && !sellrate.equals("0.0")) {
                    sellRate = number.format(sellrate);
                }
                String buyRate = "";
                if (amount != null && !amount.equals("0.0")) {
                    buyRate = number.format(amount);
                }
                sellRateTotal = sellRateTotal + sellrate;
                buyRateTotal = buyRateTotal + amount;
                BookingConformationDTO.setFieldName(chgCode);
                BookingConformationDTO.setSellRate(sellRate);
                BookingConformationDTO.setBuyRate(buyRate);
                costSheetFieldList.add(BookingConformationDTO);
                BookingConformationDTO = null;
            }
            SearchBookingReportDTO totalListDTO = new SearchBookingReportDTO();
            totalListDTO.setSellRateTotal(number.format(sellRateTotal));
            totalListDTO.setBuyRateTotal(number.format(buyRateTotal));
            costSheetFieldList.add(totalListDTO);
            totalListDTO = null;
        }
        return costSheetFieldList;
    }
    // getFile Number

    public BookingFcl getFileNoObject(String fileNo) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(BookingFcl.class);
        criteria.add(Restrictions.eq("fileNo", fileNo));
        criteria.addOrder(Order.desc("bookingId"));
        criteria.setMaxResults(1);
        return (BookingFcl) criteria.uniqueResult();
    }

    public List getAllChargesGroupByUnitTye(Integer bookingId) throws Exception {
        List bookingList = null;
        String queryString = "";
        queryString = "from BookingFcl where bookingid=?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", bookingId);
        bookingList = queryObject.list();
        return bookingList;
    }

    public List getBookingList(String bookingDate) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(BookingFcl.class);
        if (bookingDate != null && !bookingDate.equals("")) {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date soStartDate = (Date) dateFormat.parse(bookingDate);
            criteria.add(Restrictions.le("bookingDate", soStartDate));
        }
        criteria.add(Restrictions.eq("blFlag", "off"));
        return criteria.list();
    }

    public List getAutoReverseToQuoteFileList() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT b.file_no FROM booking_fcl b WHERE b.file_no NOT IN(SELECT f.file_no FROM fcl_bl f where f.file_no = b.file_no) AND DATEDIFF(SYSDATE(),b.BookingDate) > ");
        builder.append(LoadLogisoftProperties.getProperty("bookingExpiryDate"));
        return getCurrentSession().createSQLQuery(builder.toString()).list();
    }

    public String getDestCode(String dest) throws Exception {

        String queryString = "SELECT fpc.fcl_ss_bl_go_collect FROM fcl_port_configuration fpc WHERE fpc.schnum = (SELECT p.id FROM ports p  WHERE p.unlocationcode = '" + dest + "' limit 1) limit 1";
        Query queryObject = getSession().createSQLQuery(queryString);
        String result = (String) queryObject.uniqueResult();
        return result;
    }

    public String getDestCodeforHBL(String dest) throws Exception {
        String queryString = "SELECT fpc.fcl_house_bl_go_collect FROM fcl_port_configuration fpc WHERE fpc.schnum = (SELECT p.id FROM ports p  WHERE p.unlocationcode = '" + dest + "' limit 1) limit 1";
        Query queryObject = getSession().createSQLQuery(queryString);
        String result = (String) queryObject.uniqueResult();
        return result;
    }

    public String updateVesselName(String vessel, String fileNO, String userName) throws Exception {
        String result = "";
        String queryString = "UPDATE booking_fcl bkg SET bkg.update_by = '" + userName + "',bkg.Vessel = '" + vessel + "' WHERE bkg.file_no = '" + fileNO + "'";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.executeUpdate();
        GenericCode genericCode = new GenericCodeDAO().findByCodeDescName(vessel, 14);
        if (null != genericCode) {
            FclBl fclBl = new FclBlDAO().getFileNoObject(fileNO);
            fclBl.setUpdateBy(userName);
            fclBl.setVessel(genericCode);
            new FclBlDAO().update(fclBl);
            return "" + genericCode.getCode();
        }
        return result;

    }

    public void updateSSVoyValue(String ssvoyage, String fileNO, String userName) throws Exception {
        String queryString = "UPDATE booking_fcl bkg SET bkg.update_by = '" + userName + "',bkg.VoyageCarrier = '" + ssvoyage + "' WHERE bkg.file_no = '" + fileNO + "'";
        String queryStringbl = "UPDATE fcl_bl bl SET bl.update_by = '" + userName + "',bl.Voyages = '" + ssvoyage + "' WHERE bl.file_no = '" + fileNO + "'";
        Query queryObject = getSession().createSQLQuery(queryString);
        Query queryObject1 = getSession().createSQLQuery(queryStringbl);
        queryObject.executeUpdate();
        queryObject1.executeUpdate();
    }

    public String getmanifestFlag(String fileNO) throws Exception {
        String result = "";
        if (fileNO != null && !fileNO.equals("")) {
            FclBl fclBl = new FclBlDAO().getFileNoObject(fileNO);
            result = fclBl.getReadyToPost();
        }
        return result;
    }

    public String getFieldByCodeAndCodetypeId(String codeDescription, String code, String field7, String field8) throws Exception {
        String result = "false";
        String queryString = "Select " + field7 + "," + field8 + " from genericcode_dup where codetypeid=(select codetypeid from codetype where description = '" + codeDescription + "') and code= '" + code + "' ";
        List QueryObject = getCurrentSession().createSQLQuery(queryString).list();
        Iterator itr = QueryObject.iterator();
        while (itr.hasNext()) {
            Object[] row = (Object[]) itr.next();
            String sellCostFlag = (String) row[0];
            String vendorFlag = (String) row[1];
            if (sellCostFlag != null && !sellCostFlag.equals("")) {
                if (vendorFlag != null && !vendorFlag.equals("")) {
                    if ((sellCostFlag.equals("S") || (sellCostFlag.equals("CS"))) && vendorFlag.equals("N")) {
                        result = "true";
                    }
                }
            }
        }
        return result;
    }

    public String getFieldByCodeAndCodetypeIdforCost(String codeDescription, String code, String field7, String field8) throws Exception {
        String result = "false";
        String queryString = "Select " + field7 + "," + field8 + " from genericcode_dup where codetypeid=(select codetypeid from codetype where description = '" + codeDescription + "') and code= '" + code + "' ";
        List QueryObject = getCurrentSession().createSQLQuery(queryString).list();
        Iterator itr = QueryObject.iterator();
        while (itr.hasNext()) {
            Object[] row = (Object[]) itr.next();
            String sellCostFlag = (String) row[0];
            String vendorFlag = (String) row[1];
            if (sellCostFlag != null && !sellCostFlag.equals("")) {
                if (vendorFlag != null && !vendorFlag.equals("")) {
                    if ((sellCostFlag.equals("C") || (sellCostFlag.equals("CS"))) && vendorFlag.equals("N")) {
                        result = "true";
                    }
                }
            }
        }
        return result;
    }

    public String getPoa(String code) throws Exception {
        String result = "";
        if (code != null && !code.equals("")) {
            String queryString = "SELECT poa FROM cust_general_info WHERE acct_no = '" + code + "'";
            Query queryObject = getSession().createSQLQuery(queryString);
            result = null != queryObject.uniqueResult() ? (String) queryObject.uniqueResult().toString() : "";
        }
        return result;
    }

    public Integer resendToBlueScreen(String fileNo) throws Exception {
        String Query = "";
        int result = 0;
        Query = "update fcl_bl_temp set temp_update_date = SYSDATE() WHERE temp_file_No='" + fileNo + "'";
        result = getCurrentSession().createSQLQuery(Query).executeUpdate();
        if (result < 1) {
            Query = "insert into fcl_bl_temp(temp_file_No,temp_update_date) values('" + fileNo + "',SYSDATE())";
            result = getCurrentSession().createSQLQuery(Query).executeUpdate();
        }
        return result;
    }

    public void deleteHazmatEntry(Integer quoteId) throws Exception {
        String query = "DELETE FROM Hazmat_Material WHERE bolid='" + quoteId + "'";
        getCurrentSession().createSQLQuery(query).executeUpdate();
    }

    public String getHazmat(String fileNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT hazmat from booking_fcl where file_no='").append(fileNo).append("'");
        return (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public List<FclBlCostCodes> getBookingAccruals(Integer bookingId) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(FclBlCostCodes.class);
        criteria.add(Restrictions.eq("bookingId", bookingId));
        criteria.add(Restrictions.eq("deleteFlag", "no"));
        return criteria.list();
    }

    public void deleteBookingCharges(String codeId) throws Exception {
        String query = "delete from fcl_bl_costcodes where code_id='" + codeId + "'";
        getCurrentSession().createSQLQuery(query).executeUpdate();
        getCurrentSession().flush();
    }

    public String getomit2LetterCountryCodeStatus(String unlocationCode) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select omit_letter_countrycode from ports where unlocationcode='").append(unlocationCode).append("'");
        return (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public List getAutomatedPaperWorkReminderList() throws Exception {
        String betweenDate = newSdf.format(new Date());
        StringBuilder sb = new StringBuilder();
        sb.append("select b.file_no as file_no,coalesce(r.email, u.email) as from_email,b.booking_contact as to_email ");
        sb.append("from booking_fcl b left join retadd r on(r.blterm = substring_index(b.issuing_terminal, '-', - 1) and r.code='F') ");
        sb.append("left join user_details u on(u.login_name = b.booked_by and u.status = 'ACTIVE') where b.doc_cut_off between '");
        sb.append(betweenDate).append(" 00:00:00' and '").append(betweenDate).append(" 23:59:59' and (b.documents_received <> 'Y') ");
        sb.append("and (b.booking_contact is not null) and (r.email <> '' or u.email <> '') group by b.file_no");
        return getCurrentSession().createSQLQuery(sb.toString()).list();
    }

    public BookingFcl findbyFileNo(String fileNo) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(BookingFcl.class);
        criteria.add(Restrictions.eq("fileNo", fileNo));
        return (BookingFcl) criteria.setMaxResults(1).uniqueResult();
    }

    public String[] getCountryDetails(String unLocCode) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  u.`un_loc_name` as cityName,");
        queryBuilder.append("  c.`codedesc` as countryName,");
        queryBuilder.append("  c.`code` as countryCode ");
        queryBuilder.append("from");
        queryBuilder.append("  `un_location` u ");
        queryBuilder.append("  join `genericcode_dup` c");
        queryBuilder.append("    on (u.`countrycode` = c.`id`) ");
        queryBuilder.append("where u.`un_loc_code` = :unLocCode");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setMaxResults(1);
        query.setString("unLocCode", unLocCode);
        query.addScalar("cityName", StringType.INSTANCE);
        query.addScalar("countryName", StringType.INSTANCE);
        query.addScalar("countryCode", StringType.INSTANCE);
        Object[] cols = (Object[]) query.uniqueResult();
        return Arrays.copyOf(cols, cols.length, String[].class);
    }

    public List getWebToolsBooking() {
        String webToolsDb = "";
        try {
            webToolsDb = LoadLogisoftProperties.getProperty("webTools.database.name");
        } catch (Exception ex) {
            Logger.getLogger(BookingFclDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT fb.file_no,fb.email,fb.con_name,fb.con_number,fb.ware_hours,fb.ware_address,");
        queryBuilder.append("fb.cargo_date,fb.ware_name,fb.notes FROM ").append(webToolsDb).append(".fcl_booking fb WHERE status='pending'");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        return query.list();
    }

    public Object getWebToolsQuote(String fileNo) {
        String webToolsDb = "";
        try {
            webToolsDb = LoadLogisoftProperties.getProperty("webTools.database.name");
        } catch (Exception ex) {
            Logger.getLogger(BookingFclDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT fq.date,fq.origin,fq.destination,fq.pol,fq.pod,fq.client_name,fq.book_SSL,fq.client_phone,fq.client_fax,");
        queryBuilder.append("fq.client_email,fq.remarks,fq.zip_city,fq.client,fq.client_accno,fq.description,fq.hazmat,fq.insurance,");
        queryBuilder.append("fq.goods_cost,fq.ssl_accno,fq.tt_days,fq.iss_term,fq.ssl_conname,fq.ssl_phone,fq.ssl_fax,fq.ssl_email,fq.doorMove,");
        queryBuilder.append("fq.door_dest,fq.file_type,fq.inland,fq.comm_code,fq.nvo_move,fq.booking_date,fq.quote_no,fq.brand,fq.account_user_id FROM ").append(webToolsDb).append(".fclqlog fq WHERE fq.bkgnum='").append(fileNo).append("'");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        return query.uniqueResult();
    }

    public Object getCommodityDetails(String commodity) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT id, codedesc FROM genericcode_dup WHERE Codetypeid=4 AND CODE='").append(commodity).append("'");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        return query.uniqueResult();
    }

    public List getWebToolsRates(String quoteNo) {
        String webToolsDb = "";
        try {
            webToolsDb = LoadLogisoftProperties.getProperty("webTools.database.name");
        } catch (Exception ex) {
            Logger.getLogger(BookingFclDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT fr.unitType,fr.containerSize,fr.chg_code,fr.amount,fr.mrkup,fr.chargeCodeDesc,fr.total_rates,");
        queryBuilder.append("fr.remarks,fr.trucker_name,fr.trucker_accNo,fr.comment FROM ").append(webToolsDb).append(".fcl_rates fr WHERE fr.quote_no='").append(quoteNo).append("'");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        return query.list();
    }

    public List getWebToolsHazmat(String quoteNo) {
        String webToolsDb = "";
        try {
            webToolsDb = LoadLogisoftProperties.getProperty("webTools.database.name");
        } catch (Exception ex) {
            Logger.getLogger(BookingFclDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT fh.un_number,fh.proper_shipper_name,fh.emergency_ph_no,fh.tech_chemical_name,fh.haz_class,fh.imo_subsidiary_class,");
        queryBuilder.append("fh.imo_secondary_class,fh.packing_group,fh.excepted_quantity,fh.marine_pollutant,fh.limited_quantity,fh.inner_pack_pieces,");
        queryBuilder.append("fh.outer_pack_piece,fh.inner_pack_comp,fh.outer_pack_comp,fh.inner_pack_type,fh.outer_pack_type,fh.inner_pack_wgt_vol,");
        queryBuilder.append("fh.total_gross_wgt,fh.total_vol,fh.reportable_quantity,fh.inhalation_hazard,fh.residue,fh.ems_code,fh.emergency_contact,");
        queryBuilder.append("fh.inner_pack_uom,fh.flash_point,fh.free_format,fh.line1,fh.line2,fh.line3,fh.line4,fh.line5,fh.line6,");
        queryBuilder.append("fh.line7,fh.total_net_wgt FROM ").append(webToolsDb).append(".fcl_hazardous fh WHERE fh.quotation_number='").append(quoteNo).append("'");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        return query.list();
    }

    public String getClientType(int accUserId) {
        String webToolsDb = "";
        try {
            webToolsDb = LoadLogisoftProperties.getProperty("webTools.database.name");
        } catch (Exception ex) {
            Logger.getLogger(BookingFclDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT a.account_type FROM ").append(webToolsDb).append(".account a JOIN ").append(webToolsDb);
        queryBuilder.append(".account_user au ON au.account_id=a.id WHERE au.id=").append(accUserId);
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        return query.uniqueResult().toString();
    }

    public void update(Quotation persistanceInstance) throws Exception {
        getSession().saveOrUpdate(persistanceInstance);
        getSession().flush();
    }

    public void update(Charges persistanceInstance) throws Exception {
        getSession().save(persistanceInstance);
        getSession().flush();
    }

    public void update(BookingfclUnits persistanceInstance) throws Exception {
        getSession().saveOrUpdate(persistanceInstance);
        getSession().flush();
    }

    public Quotation findQuoteByFileNo(String fileNo) throws Exception {
        getCurrentSession().flush();
        Criteria criteria = getCurrentSession().createCriteria(Quotation.class);
        criteria.add(Restrictions.eq("fileNo", fileNo).ignoreCase());
        return (Quotation) criteria.uniqueResult();
    }

    public BookingFcl findBookingByFileNo(String fileNo) throws Exception {
        getCurrentSession().flush();
        Criteria criteria = getCurrentSession().createCriteria(BookingFcl.class);
        criteria.add(Restrictions.eq("fileNo", fileNo).ignoreCase());
        return (BookingFcl) criteria.uniqueResult();
    }

    public void updateStatus(String fileNo) {
        String webToolsDb = "";
        try {
            webToolsDb = LoadLogisoftProperties.getProperty("webTools.database.name");
        } catch (Exception ex) {
            Logger.getLogger(BookingFclDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("UPDATE ").append(webToolsDb).append(".fcl_booking SET status='completed' WHERE file_no='").append(fileNo).append("'");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.executeUpdate();
    }

    public void update(HazmatMaterial persistanceInstance) throws Exception {
        getSession().saveOrUpdate(persistanceInstance);
        getSession().flush();
    }

    public boolean canWeCancelEdi(String fileNo) throws Exception {
        String q = "select count(*) from logfile_edi where file_no='" + fileNo + "' and message_type = '301' and status ='cancel'";
        String r = getSession().createSQLQuery(q).uniqueResult().toString();
        return r.equals("0");
    }

    public String createOrChangeXml(String fileNo) throws Exception {
        String q = "select count(*) from logfile_edi where file_no='" + fileNo + "' and message_type = '300' and status ='success'";
        String r = getSession().createSQLQuery(q).uniqueResult().toString();
        return r.equals("0") ? "create" : "change";
    }

    public String isNotSpotRate(String bookingNo) throws Exception {
        String q = "select count(*) from bookingfcl_units where bookingnumber='" + bookingNo + "'  and approve_bl='Yes' and (manual_charges is null and spotrate_amt is null)";
        String r = getCurrentSession().createSQLQuery(q).uniqueResult().toString();
        return r.equals("0") ? "Spot Rate" : "No Spot Rate";
    }

    public String getEdiCreatedUserEmail(String fileNo) throws Exception {
        String q = "select u.email from user_details u join booking_fcl b on(b.edi_created_by=u.login_name) where b.file_no='" + fileNo + "' order by b.bookingid desc limit 1";
        Object email = getCurrentSession().createSQLQuery(q).uniqueResult();
        return null != email ? email.toString() : "";
    }

    public void clearSpotCost(String fileNo) throws Exception {
        String b = "update bookingfcl_units u join booking_fcl b on(b.bookingnumber=u.bookingnumber) set u.spotrate_amt=null,u.spotrate_markup=null,u.standard_chk='off',u.spotrate_chk='off',b.spot_rate='N' where b.file_no='" + fileNo + "'";
        getCurrentSession().createSQLQuery(b).executeUpdate();
    }

    public Iterator getVoyageNo(Integer origin5Digit, Integer destination5Digit, String voyageNo) {
        List voyageNoList = null;
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  lclssh.id AS ssHeaderId,");
        sb.append("  lclssh.schedule_no AS scheduleNo");
        sb.append(" FROM");
        sb.append("  lcl_ss_header lclssh ");
        sb.append(" WHERE lclssh.origin_id =:origin5Digit");
        sb.append("  AND lclssh.destination_id =:destination5Digit ");
        sb.append("  AND lclssh.service_type ='C' ");
        sb.append("  AND lclssh.schedule_no Like :voyageNo");
        sb.append(" GROUP BY lclssh.id DESC ");
        sb.append(" LIMIT 50 ");
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("origin5Digit", origin5Digit);
        queryObject.setParameter("destination5Digit", destination5Digit);
        queryObject.setParameter("voyageNo", voyageNo + "%");
        voyageNoList = queryObject.list();
        return voyageNoList.iterator();
    }

    public List<FclCfclChargeBean> getCfclCharge(BigInteger headerId) {
        StringBuilder sb = new StringBuilder();
        sb.append("	SELECT ");
        sb.append("	  fn.fileNumber AS fileNumber,");
        sb.append("	  fn.fileId AS fileId,");
        sb.append("	  SUM(lbc.ar_amount) AS arAmount,");
        sb.append("	  (SELECT ");
        sb.append("	    GROUP_CONCAT(ar.`invoice_number`) ");
        sb.append("	  FROM");
        sb.append("	    `ar_red_invoice` ar ");
        sb.append("	  WHERE ar.`bl_number` = fn.fileNumber) AS invoiceNumber,");
        sb.append("	  (SELECT ");
        sb.append("	    SUM(ar.`invoice_amount`) ");
        sb.append("	  FROM");
        sb.append("	    `ar_red_invoice` ar ");
        sb.append("	  WHERE ar.`bl_number` = fn.fileNumber) AS invoiceAmount,");
        sb.append("	  (SELECT ");
        sb.append("	    IF(");
        sb.append("	      GROUP_CONCAT(bh.un_hazmat_no) IS NOT NULL,");
        sb.append("	      TRUE,");
        sb.append("	      FALSE");
        sb.append("	    ) ");
        sb.append("	  FROM");
        sb.append("	    lcl_booking_hazmat bh ");
        sb.append("	  WHERE bh.file_number_id = fn.fileId) AS hazmat ");
        sb.append("	FROM");
        sb.append("	  (SELECT ");
        sb.append("	    lb.`file_number_id` AS fileId,");
        sb.append("	    lf.`file_number` AS fileNumber ");
        sb.append("	  FROM");
        sb.append("	    `lcl_unit_ss` lus ");
        sb.append("	    JOIN `lcl_booking_piece_unit` lbpu ");
        sb.append("	      ON (lus.`id` = lbpu.`lcl_unit_ss_id`) ");
        sb.append("	    JOIN `lcl_booking_piece` lbp ");
        sb.append("	      ON (");
        sb.append("		lbp.`id` = lbpu.`booking_piece_id`");
        sb.append("	      ) ");
        sb.append("	    JOIN `lcl_booking` lb ");
        sb.append("	      ON (");
        sb.append("		lb.`file_number_id` = lbp.`file_number_id`");
        sb.append("	      ) ");
        sb.append("	    JOIN `lcl_file_number` lf ");
        sb.append("	      ON (lf.`id` = lb.`file_number_id`) ");
        sb.append("	  WHERE lus.`ss_header_id` = :headerId) AS fn ");
        sb.append("	  JOIN `lcl_booking_ac` lbc ");
        sb.append("	    ON (");
        sb.append("	      lbc.`file_number_id` = fn.fileId ");
        sb.append("	      AND lbc.`manual_entry` = TRUE");
        sb.append("	    ) ");
        sb.append("	GROUP BY fn.fileId ");
        SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
        queryObject.setParameter("headerId", headerId);
        queryObject.setResultTransformer(Transformers.aliasToBean(FclCfclChargeBean.class));
        queryObject.addScalar("fileNumber", StringType.INSTANCE);
        queryObject.addScalar("fileId", LongType.INSTANCE);
        queryObject.addScalar("arAmount", DoubleType.INSTANCE);
        queryObject.addScalar("invoiceNumber", StringType.INSTANCE);
        queryObject.addScalar("invoiceAmount", DoubleType.INSTANCE);
        queryObject.addScalar("hazmat", BooleanType.INSTANCE);
        return queryObject.list();
    }

    public List<FclCfclChargeBean> getCfclLinkedAllDrCharge(BigInteger headerId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" f.fileId AS fileId,");
        sb.append(" gl.charge_code AS chargeCode,");
        sb.append(" bac.ar_amount AS arAmount,");
        sb.append(" bac.ap_amount AS apAmount,");
        sb.append(" bac.`sp_acct_no` AS customerAcct,");
        sb.append(" (SELECT `acct_name` FROM `trading_partner` WHERE acct_no = bac.`sp_acct_no` LIMIT 1) AS customerName,");
        sb.append(" (SELECT GROUP_CONCAT(ut.elite_type,'=',LEFT(ut.short_desc, 2)) FROM unit_type ut JOIN lcl_unit lu ");
        sb.append(" ON lu.unit_type_id = ut.id WHERE  lu.id = f.unitId) AS containerSize ");
        sb.append(" FROM ");
        sb.append("(SELECT ");
        sb.append("    lbp.`file_number_id` AS fileId,");
        sb.append("    lus.id AS unitSsId ,");
        sb.append("    lus.unit_id AS unitId");
        sb.append("  FROM");
        sb.append("    `lcl_unit_ss` lus ");
        sb.append("    JOIN `lcl_booking_piece_unit` lbpu ON (lus.`id` = lbpu.`lcl_unit_ss_id`) ");
        sb.append("    JOIN `lcl_booking_piece` lbp ON (lbp.`id` = lbpu.`booking_piece_id`)  ");
        sb.append("  WHERE lus.`ss_header_id` = :headerId) f");
        sb.append("  JOIN lcl_booking_ac bac ON bac.file_number_id = fileId ");
        sb.append("  AND bac.`manual_entry` = TRUE AND bac.`ar_amount` <> '0.00'");
        sb.append("  JOIN gl_mapping gl ON gl.`id` = bac.`ar_gl_mapping_id`");
        sb.append("  GROUP BY charge_code ,f.fileId ");
        sb.append("  ORDER BY f.fileId  DESC");
        SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
        queryObject.setParameter("headerId", headerId);
        queryObject.setResultTransformer(Transformers.aliasToBean(FclCfclChargeBean.class));
        queryObject.addScalar("arAmount", DoubleType.INSTANCE);
        queryObject.addScalar("apAmount", DoubleType.INSTANCE);
        queryObject.addScalar("containerSize", StringType.INSTANCE);
        queryObject.addScalar("chargeCode", StringType.INSTANCE);
        queryObject.addScalar("customerAcct", StringType.INSTANCE);
        queryObject.addScalar("customerName", StringType.INSTANCE);
        return queryObject.list();
    }

    public BigInteger getSsHeaderId(String voyageNo) {
        String query = "SELECT lsh.`id` FROM `lcl_ss_header` lsh WHERE lsh.`schedule_no`=:voyageNo LIMIT 1";
        Query queryObject = getCurrentSession().createSQLQuery(query);
        queryObject.setParameter("voyageNo", voyageNo);
        return (BigInteger) queryObject.uniqueResult();
    }

    public List getFileNumber(BigInteger headerId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("    GROUP_CONCAT(lb.`file_number_id`) AS fileId,");
        sb.append("    GROUP_CONCAT(lf.`file_number`) AS fileNumber");
        sb.append("  FROM");
        sb.append("    `lcl_unit_ss` lus ");
        sb.append("    JOIN `lcl_booking_piece_unit` lbpu ");
        sb.append("      ON (lus.`id` = lbpu.`lcl_unit_ss_id`) ");
        sb.append("    JOIN `lcl_unit` lu ");
        sb.append("      ON (lu.`id` = lus.`unit_id`) ");
        sb.append("    JOIN `unit_type` ut ");
        sb.append("      ON (lu.`unit_type_id` = ut.`id`) ");
        sb.append("    JOIN `lcl_booking_piece` lbp ");
        sb.append("      ON (");
        sb.append("        lbp.`id` = lbpu.`booking_piece_id`");
        sb.append("      ) ");
        sb.append("    JOIN `lcl_booking` lb ");
        sb.append("      ON (");
        sb.append("        lb.`file_number_id` = lbp.`file_number_id`");
        sb.append("      ) ");
        sb.append("    JOIN `lcl_file_number` lf ");
        sb.append("      ON (lf.`id` = lb.`file_number_id`) ");
        sb.append("  WHERE lus.`ss_header_id` = :headerId");
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("headerId", headerId);
        return queryObject.list();
    }

    public List<Long> getFileId(BigInteger headerId) {
        List result = null;
        List<Long> resultList = new ArrayList<Long>();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("    lb.`file_number_id` AS fileId");
        sb.append("  FROM");
        sb.append("    `lcl_unit_ss` lus ");
        sb.append("    JOIN `lcl_booking_piece_unit` lbpu ");
        sb.append("      ON (lus.`id` = lbpu.`lcl_unit_ss_id`) ");
        sb.append("    JOIN `lcl_unit` lu ");
        sb.append("      ON (lu.`id` = lus.`unit_id`) ");
        sb.append("    JOIN `unit_type` ut ");
        sb.append("      ON (lu.`unit_type_id` = ut.`id`) ");
        sb.append("    JOIN `lcl_booking_piece` lbp ");
        sb.append("      ON (");
        sb.append("        lbp.`id` = lbpu.`booking_piece_id`");
        sb.append("      ) ");
        sb.append("    JOIN `lcl_booking` lb ");
        sb.append("      ON (");
        sb.append("        lb.`file_number_id` = lbp.`file_number_id`");
        sb.append("      ) ");
        sb.append("    JOIN `lcl_file_number` lf ");
        sb.append("      ON (lf.`id` = lb.`file_number_id`) ");
        sb.append("  WHERE lus.`ss_header_id` = :headerId");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("headerId", headerId);
        result = queryObject.list();
        for (Object obj : result) {
            resultList.add(Long.parseLong(obj.toString()));
        }
        return resultList;
    }
    public boolean getHazFlag(List<Long> groupFileId) {
        String query = "SELECT IF(COUNT(*) > 0, TRUE, FALSE) AS STATUS FROM `lcl_booking_hazmat` WHERE file_number_id IN(:groupFileId)";
        SQLQuery queryObject = getCurrentSession().createSQLQuery(query);
        queryObject.setParameterList("groupFileId", groupFileId);
        queryObject.addScalar("status", BooleanType.INSTANCE);
        return (Boolean) queryObject.uniqueResult();
    }

    public void updateHaz(String bookingId) {
        String query = "UPDATE `booking_fcl` bf SET bf.`hazmat`=:hazmat WHERE bf.`BookingNumber`=:BookingNumber";
        Query queryObject = getCurrentSession().createSQLQuery(query);
        queryObject.setParameter("BookingNumber", bookingId);
        queryObject.setParameter("hazmat", "Y");
        queryObject.executeUpdate();
    }

    public boolean checkVoyage(String bookingId) {
        String query = "SELECT IF(`VoyageInternal` IS NULL OR VoyageInternal =\"\",TRUE,FALSE) AS STATUS  FROM `booking_fcl` bf WHERE bf.`BookingNumber`=:BookingNumber";
        SQLQuery queryObject = getCurrentSession().createSQLQuery(query);
        queryObject.setParameter("BookingNumber", bookingId);
        queryObject.addScalar("STATUS", BooleanType.INSTANCE);
        return (boolean) queryObject.uniqueResult();
    }
    
    public String getBrandStatus(String fileNo, String screenName) {
        String queryString = "";
        if ("QUOTE".equalsIgnoreCase(screenName)) {
            queryString = "SELECT `brand`FROM `quotation` WHERE file_no =:fileNo LIMIT 1";
        } else if ("BOOKING".equalsIgnoreCase(screenName)) {
            queryString = "SELECT `brand`FROM `booking_fcl` WHERE file_no =:fileNo LIMIT 1";
        } else {
            queryString = "SELECT brand FROM fcl_bl WHERE file_no =:fileNo LIMIT 1";
        }
        Query queryObject = getCurrentSession().createSQLQuery(queryString);
        queryObject.setParameter("fileNo", fileNo);
        return (String) queryObject.uniqueResult();
    }
    
    public boolean checkAccountExistInAccountDetailsForAccount(String account) throws Exception {
        StringBuilder sb = new StringBuilder();
        BigInteger count = new BigInteger("0");
        sb.append("SELECT COUNT(*) FROM account_details ac WHERE ac.account ='").append(account).append("'");
        Object object = getCurrentSession().createSQLQuery(sb.toString()).uniqueResult();
        if (object != null) {
            count = (BigInteger) object;
        }
        return count.intValue() > 0 ? true : false;
    }
    
    public boolean getAccountForCostAndChargeCodeBookingFromGlAndTerminal(String chargesCode, String fileNO, String transactionType) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append("  `IsValidGlAccount` (t.`account`) AS valid ");
        sb.append(" FROM");
        sb.append("  (SELECT ");
        sb.append("    `DeriveGlAccount` (");
        sb.append("      :chargeCode,");
        sb.append("      IF(");
        sb.append("        bf.`importFlag` = 'I',");
        sb.append("        'FCLI',");
        sb.append("        'FCLE'");
        sb.append("      ),");
        sb.append("      :transactionType,");
        sb.append("      SUBSTRING_INDEX(bf.`issuing_terminal`, '-', - 1)");
        sb.append("    ) AS account ");
        sb.append("  FROM");
        sb.append("    `booking_fcl` bf ");
        sb.append("  WHERE bf.`file_no` = :file_no) AS t ");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("chargeCode", chargesCode);
        queryObject.setParameter("transactionType", transactionType);
        queryObject.setParameter("file_no", fileNO);
        queryObject.addScalar("valid", BooleanType.INSTANCE);
        return (boolean) queryObject.uniqueResult();
    }
    
    public boolean getAccountForCostAndChargeCodeBlFromGlAndTerminal(String chargesCode, String fileNumber, String transactionType) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  `IsValidGlAccount` (t.`account`) AS valid ");
        sb.append("FROM");
        sb.append("  (SELECT ");
        sb.append("    `DeriveGlAccount` (");
        sb.append("      :chargeCode,");
        sb.append("      IF(");
        sb.append("        bf.`importFlag` = 'I',");
        sb.append("        'FCLI',");
        sb.append("        'FCLE'");
        sb.append("      ),");
        sb.append("      :transactionType,");
        sb.append("      SUBSTRING_INDEX(bf.billing_terminal, '-', - 1)");
        sb.append("    ) AS account ");
        sb.append("  FROM");
        sb.append("    `fcl_bl` bf ");
        sb.append("  WHERE bf.file_no = :fileNumber) AS t ");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("chargeCode", chargesCode);
        queryObject.setParameter("transactionType", transactionType);
        queryObject.setParameter("fileNumber", fileNumber);
        queryObject.addScalar("valid", BooleanType.INSTANCE);
        return (boolean) queryObject.uniqueResult();
    }
    
    public boolean getAccountForCostAndChargeCodeQtFromGlAndTerminal(String chargesCode, String fileNO, String transactionType) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append("  `IsValidGlAccount` (t.`account`) AS valid ");
        sb.append(" FROM");
        sb.append("  (SELECT ");
        sb.append("    `DeriveGlAccount` (");
        sb.append("      :chargeCode,");
        sb.append("      IF(");
        sb.append("        q.file_type = 'I',");
        sb.append("        'FCLI',");
        sb.append("        'FCLE'");
        sb.append("      ),");
        sb.append("      :transactionType,");
        sb.append("      SUBSTRING_INDEX(q.`issuing_terminal`, '-', - 1)");
        sb.append("    ) AS account ");
        sb.append("  FROM");
        sb.append("    `quotation` q ");
        sb.append("  WHERE q.`file_no` = :file_no) AS t ");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("chargeCode", chargesCode);
        queryObject.setParameter("transactionType", transactionType);
        queryObject.setParameter("file_no", fileNO);
        queryObject.addScalar("valid", BooleanType.INSTANCE);
        return (boolean) queryObject.uniqueResult();
    }
}

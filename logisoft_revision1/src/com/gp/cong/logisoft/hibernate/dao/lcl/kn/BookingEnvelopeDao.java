package com.gp.cong.logisoft.hibernate.dao.lcl.kn;

import com.gp.cong.logisoft.domain.lcl.kn.BookingEnvelope;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.kn.beans.Booking;
import com.gp.cong.logisoft.kn.beans.BookingBean;
import com.gp.cong.logisoft.kn.beans.BookingHaz;
import com.gp.cong.logisoft.lcl.kn.bc.BookingQueryBuilder;
import com.gp.cong.logisoft.lcl.kn.bc.QueryUtil;
import com.gp.cvst.logisoft.struts.lcl.kn.form.SearchForm;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;

/**
 *
 * @author palraj.p
 */
public class BookingEnvelopeDao extends BaseHibernateDAO<BookingEnvelope> {

    private static final Logger log = Logger.getLogger(BookingEnvelopeDao.class);

    public BookingEnvelope searchById(Long id) throws Exception {
        Query query = getSession().createQuery("from BookingEnvelope where id = :id");
        query.setLong("id", id);
        return (BookingEnvelope) query.uniqueResult();
    }

    public List<BookingBean> search(SearchForm searchForm) throws Exception {
        BookingQueryBuilder queryBuilder = new BookingQueryBuilder();
        SQLQuery query = getSession().createSQLQuery(queryBuilder.search(searchForm).toString());
        query.setResultTransformer(Transformers.aliasToBean(BookingBean.class));
        query.addScalar("bookingId", LongType.INSTANCE);
        query.addScalar("bookingType", StringType.INSTANCE);
        query.addScalar("bookingDate", StringType.INSTANCE);
        query.addScalar("requestType", StringType.INSTANCE);
        query.addScalar("bookingNumber", StringType.INSTANCE);
        query.addScalar("cfsOrigin", StringType.INSTANCE);
        query.addScalar("cfsDestination", StringType.INSTANCE);
        query.addScalar("amsFlag", StringType.INSTANCE);
        query.addScalar("aesFlag", StringType.INSTANCE);
        query.addScalar("pieces", StringType.INSTANCE);
        query.addScalar("weight", StringType.INSTANCE);
        query.addScalar("volume", StringType.INSTANCE);
        query.addScalar("createdOn", StringType.INSTANCE);
        query.addScalar("companyName", StringType.INSTANCE);
        query.addScalar("vesselVoyageId", StringType.INSTANCE);
        query.addScalar("vesselName", StringType.INSTANCE);
        query.addScalar("imoNumber", StringType.INSTANCE);
        query.addScalar("voyage", StringType.INSTANCE);
        query.addScalar("etd", StringType.INSTANCE);
        query.addScalar("eta", StringType.INSTANCE);
        query.addScalar("customerControlCode", StringType.INSTANCE);
        query.addScalar("senderId", StringType.INSTANCE);
        query.addScalar("senderMappingId", StringType.INSTANCE);
        query.addScalar("remarks", StringType.INSTANCE);
        return query.list();
    }

    public void update(Long id, String bkgNo) throws Exception, Exception {
        Session currentSession = getSession();
        String query = QueryUtil.updateBkgNo(id, bkgNo);
        currentSession.createSQLQuery(query).executeUpdate();
    }

    public void updateEnvelope(BookingEnvelope bkgEnvelope, byte[] bcFile) throws Exception {
        Session currentSession = getSession();
        bkgEnvelope.setBookingConfirmationFile(bcFile);
        currentSession.update(bkgEnvelope);

    }

    public void updateError(BookingEnvelope bkgEnvelope, String errorMessage) throws Exception {
        Session currentSession = getSession();
        bkgEnvelope.setRemarks(errorMessage);
        currentSession.update(bkgEnvelope);
    }

    public Booking getBooking(Map<String, String> valueMap) throws IllegalArgumentException, Exception {
        Session currentSession = getSession();
        Booking bkg = null;
        String query = QueryUtil.fetchBooking(valueMap).toString();
        SQLQuery sqlQuery = currentSession.createSQLQuery(query);
        sqlQuery.setResultTransformer(Transformers.aliasToBean(Booking.class));
        sqlQuery.addScalar("originTerminal", StringType.INSTANCE);
        sqlQuery.addScalar("dockReceiptNo", StringType.INSTANCE);
        sqlQuery.addScalar("bkgTerminal", StringType.INSTANCE);
        sqlQuery.addScalar("portNo", StringType.INSTANCE);
        sqlQuery.addScalar("voyageNumber", StringType.INSTANCE);
        sqlQuery.addScalar("billing", StringType.INSTANCE);
        sqlQuery.addScalar("customerContact", StringType.INSTANCE);
        sqlQuery.addScalar("shipperCar", StringType.INSTANCE);
        sqlQuery.addScalar("shipperAddress1", StringType.INSTANCE);
        sqlQuery.addScalar("shipperAddress2", StringType.INSTANCE);
        sqlQuery.addScalar("shipperCity", StringType.INSTANCE);
        sqlQuery.addScalar("shipperState", StringType.INSTANCE);
        sqlQuery.addScalar("shipperCountry", StringType.INSTANCE);
        sqlQuery.addScalar("shipperPos", StringType.INSTANCE);
        sqlQuery.addScalar("shipperPhone", StringType.INSTANCE);
        sqlQuery.addScalar("shipperFax", StringType.INSTANCE);
        sqlQuery.addScalar("fwdName", StringType.INSTANCE);
        sqlQuery.addScalar("fwdCar", StringType.INSTANCE);
        sqlQuery.addScalar("fwdAddress1", StringType.INSTANCE);
        sqlQuery.addScalar("fwdAddress2", StringType.INSTANCE);
        sqlQuery.addScalar("fwdCity", StringType.INSTANCE);
        sqlQuery.addScalar("fwdState", StringType.INSTANCE);
        sqlQuery.addScalar("fwdCountry", StringType.INSTANCE);
        sqlQuery.addScalar("fwdPos", StringType.INSTANCE);
        sqlQuery.addScalar("fwdPhone", StringType.INSTANCE);
        sqlQuery.addScalar("fwdFax", StringType.INSTANCE);
        sqlQuery.addScalar("conName", StringType.INSTANCE);
        sqlQuery.addScalar("conCar", StringType.INSTANCE);
        sqlQuery.addScalar("conAddress1", StringType.INSTANCE);
        sqlQuery.addScalar("conAddress2", StringType.INSTANCE);
        sqlQuery.addScalar("conCity", StringType.INSTANCE);
        sqlQuery.addScalar("conState", StringType.INSTANCE);
        sqlQuery.addScalar("conCountry", StringType.INSTANCE);
        sqlQuery.addScalar("conPos", StringType.INSTANCE);
        sqlQuery.addScalar("conPhone", StringType.INSTANCE);
        sqlQuery.addScalar("conFax", StringType.INSTANCE);
        sqlQuery.addScalar("notifyName", StringType.INSTANCE);
        sqlQuery.addScalar("notifyCar", StringType.INSTANCE);
        sqlQuery.addScalar("notifyAddress1", StringType.INSTANCE);
        sqlQuery.addScalar("notifyCity", StringType.INSTANCE);
        sqlQuery.addScalar("notifyState", StringType.INSTANCE);
        sqlQuery.addScalar("notifyCountry", StringType.INSTANCE);
        sqlQuery.addScalar("notifyzip", StringType.INSTANCE);
        sqlQuery.addScalar("notifyPhone", StringType.INSTANCE);
        sqlQuery.addScalar("notifyFax", StringType.INSTANCE);
        sqlQuery.addScalar("pc1", IntegerType.INSTANCE);
        sqlQuery.addScalar("pc2", IntegerType.INSTANCE);
        sqlQuery.addScalar("pc3", IntegerType.INSTANCE);
        sqlQuery.addScalar("pc4", IntegerType.INSTANCE);
        sqlQuery.addScalar("pct1", StringType.INSTANCE);
        sqlQuery.addScalar("pct2", StringType.INSTANCE);
        sqlQuery.addScalar("pct3", StringType.INSTANCE);
        sqlQuery.addScalar("pct4", StringType.INSTANCE);
        sqlQuery.addScalar("wgt1", DoubleType.INSTANCE);
        sqlQuery.addScalar("wgt2", DoubleType.INSTANCE);
        sqlQuery.addScalar("wgt3", DoubleType.INSTANCE);
        sqlQuery.addScalar("wgt4", DoubleType.INSTANCE);
        sqlQuery.addScalar("wtp1", StringType.INSTANCE);
        sqlQuery.addScalar("wtp2", StringType.INSTANCE);
        sqlQuery.addScalar("wtp3", StringType.INSTANCE);
        sqlQuery.addScalar("wtp4", StringType.INSTANCE);
        sqlQuery.addScalar("msr1", DoubleType.INSTANCE);
        sqlQuery.addScalar("msr2", DoubleType.INSTANCE);
        sqlQuery.addScalar("msr3", DoubleType.INSTANCE);
        sqlQuery.addScalar("msr4", DoubleType.INSTANCE);
        sqlQuery.addScalar("mtp1", StringType.INSTANCE);
        sqlQuery.addScalar("mtp2", StringType.INSTANCE);
        sqlQuery.addScalar("mtp3", StringType.INSTANCE);
        sqlQuery.addScalar("mtp4", StringType.INSTANCE);
        sqlQuery.addScalar("cm1", StringType.INSTANCE);
        sqlQuery.addScalar("cm2", StringType.INSTANCE);
        sqlQuery.addScalar("cm3", StringType.INSTANCE);
        sqlQuery.addScalar("cm4", StringType.INSTANCE);
        sqlQuery.addScalar("hazFlag", StringType.INSTANCE);
        sqlQuery.addScalar("pickupFlag", StringType.INSTANCE);
        sqlQuery.addScalar("bonded", StringType.INSTANCE);
        sqlQuery.addScalar("licnsd", StringType.INSTANCE);
        sqlQuery.addScalar("rtngds", StringType.INSTANCE);
        sqlQuery.addScalar("elcexd", StringType.INSTANCE);
        sqlQuery.addScalar("xtnnm1", StringType.INSTANCE);
        sqlQuery.addScalar("xtnnm2", StringType.INSTANCE);
        sqlQuery.addScalar("pshpnm", StringType.INSTANCE);
        sqlQuery.addScalar("tchmnm", StringType.INSTANCE);
        sqlQuery.addScalar("hazClass", StringType.INSTANCE);
        sqlQuery.addScalar("unnumb", StringType.INSTANCE);
        sqlQuery.addScalar("pkggrp", StringType.INSTANCE);
        sqlQuery.addScalar("flshpt", StringType.INSTANCE);
        sqlQuery.addScalar("flshtm", StringType.INSTANCE);
        sqlQuery.addScalar("outpkg", StringType.INSTANCE);
        sqlQuery.addScalar("innpkg", StringType.INSTANCE);
        sqlQuery.addScalar("othpk1", StringType.INSTANCE);
        sqlQuery.addScalar("othpk2", StringType.INSTANCE);
        sqlQuery.addScalar("lmtqty", StringType.INSTANCE);
        sqlQuery.addScalar("marplt", StringType.INSTANCE);
        sqlQuery.addScalar("rptqty", StringType.INSTANCE);
        sqlQuery.addScalar("inhhaz", StringType.INSTANCE);
        sqlQuery.addScalar("inhzon", StringType.INSTANCE);
        sqlQuery.addScalar("emgcon", StringType.INSTANCE);
        sqlQuery.addScalar("attch1", StringType.INSTANCE);
        sqlQuery.addScalar("attch2", StringType.INSTANCE);
        sqlQuery.addScalar("attch3", StringType.INSTANCE);
        sqlQuery.addScalar("logip", StringType.INSTANCE);
        sqlQuery.addScalar("logses", StringType.INSTANCE);
        sqlQuery.addScalar("logid", StringType.INSTANCE);
        sqlQuery.addScalar("logcmp", StringType.INSTANCE);
        sqlQuery.addScalar("logtme", TimestampType.INSTANCE);
        sqlQuery.addScalar("cmpnum", StringType.INSTANCE);
        sqlQuery.addScalar("contact", StringType.INSTANCE);
        sqlQuery.addScalar("pucomp", StringType.INSTANCE);
        sqlQuery.addScalar("puadr1", StringType.INSTANCE);
        sqlQuery.addScalar("pucity", StringType.INSTANCE);
        sqlQuery.addScalar("pustat", StringType.INSTANCE);
        sqlQuery.addScalar("pucname", StringType.INSTANCE);
        sqlQuery.addScalar("puzipc", StringType.INSTANCE);
        sqlQuery.addScalar("puphon", StringType.INSTANCE);
        sqlQuery.addScalar("pufax", StringType.INSTANCE);
        sqlQuery.addScalar("puhrs", StringType.INSTANCE);
        sqlQuery.addScalar("pudate", DateType.INSTANCE);
        sqlQuery.addScalar("pucmmt", StringType.INSTANCE);
        sqlQuery.addScalar("ecquot", StringType.INSTANCE);
        sqlQuery.addScalar("ecqtnm", StringType.INSTANCE);
        sqlQuery.addScalar("cmmnt1", StringType.INSTANCE);
        sqlQuery.addScalar("cmmnt2", StringType.INSTANCE);
        sqlQuery.addScalar("cmmnt3", StringType.INSTANCE);
        sqlQuery.addScalar("cmmnt4", StringType.INSTANCE);
        sqlQuery.addScalar("cmmnt5", StringType.INSTANCE);
        sqlQuery.addScalar("cmptyp", StringType.INSTANCE);
        sqlQuery.addScalar("cnteml", StringType.INSTANCE);
        sqlQuery.addScalar("ovdims", StringType.INSTANCE);
        sqlQuery.addScalar("ecidte", StringType.INSTANCE);
        sqlQuery.addScalar("poo", StringType.INSTANCE);
        sqlQuery.addScalar("pol", StringType.INSTANCE);
        sqlQuery.addScalar("pod", StringType.INSTANCE);
        sqlQuery.addScalar("fd", StringType.INSTANCE);
        sqlQuery.addScalar("gr", StringType.INSTANCE);
        sqlQuery.addScalar("frtref", StringType.INSTANCE);
        sqlQuery.addScalar("shpref", StringType.INSTANCE);
        sqlQuery.addScalar("conref", StringType.INSTANCE);
        sqlQuery.addScalar("updatedDate", StringType.INSTANCE);
        sqlQuery.addScalar("updatedTime", StringType.INSTANCE);
        sqlQuery.addScalar("senderMappingId", StringType.INSTANCE);
        bkg = (Booking) sqlQuery.uniqueResult();
        return bkg;
    }

    public List<BookingHaz> getBookingHaz(Map<String, String> valueMap) throws IllegalArgumentException {
        List<BookingHaz> bkgHazes = null;
        try {
            Session currentSession = getSession();
            String query = QueryUtil.fetchBookingHaz(valueMap).toString();
            SQLQuery sqlQuery = currentSession.createSQLQuery(query);
            sqlQuery.setResultTransformer(Transformers.aliasToBean(BookingHaz.class));
            sqlQuery.addScalar("unNumber", StringType.INSTANCE);
            sqlQuery.addScalar("properShippingName", StringType.INSTANCE);
            sqlQuery.addScalar("hazClass", StringType.INSTANCE);
            sqlQuery.addScalar("flashPoint", StringType.INSTANCE);
            sqlQuery.addScalar("flashPointFlag", StringType.INSTANCE);
            sqlQuery.addScalar("packageGroup", StringType.INSTANCE);
            bkgHazes = (List<BookingHaz>) sqlQuery.list();
        } catch (HibernateException exception) {
            log.error(exception);
        } catch (NullPointerException exception) {
            log.error(exception);
        } catch (Exception exception) {
            log.error(exception);
        }
        return bkgHazes;
    }
    
    public boolean getKnOblFlag(String filenumber) throws Exception {
    String query ="SELECT IF(COUNT(*)>0,TRUE,FALSE) AS status FROM `kn_shipping_instruction` WHERE bkg_number=:fileNumber";
    SQLQuery queryObject= getCurrentSession().createSQLQuery(query);
    queryObject.setParameter("fileNumber", filenumber);
    queryObject.addScalar("status", BooleanType.INSTANCE);
    return (boolean) queryObject.uniqueResult();
    }
    
    public List getKnOblSyatemTracking(String fileNumber) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  ksi.`customer_reference` AS exportRefrenceNumber,");
        sb.append("  ksi.`marks` AS marksAndNumber,");
        sb.append("  ksi.`commodity` ");
        sb.append(" FROM");
        sb.append("  `kn_shipping_instruction` ksi ");
        sb.append(" WHERE ksi.`bkg_number` = :fileNumber  ");
        SQLQuery queryObject =getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("fileNumber", fileNumber);
        return queryObject.list();
    }
}

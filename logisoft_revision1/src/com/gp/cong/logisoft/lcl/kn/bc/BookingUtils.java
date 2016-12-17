package com.gp.cong.logisoft.lcl.kn.bc;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.lcl.kn.BookingDetail;
import com.gp.cong.logisoft.domain.lcl.kn.BookingEnvelope;
import com.gp.cong.logisoft.hibernate.dao.lcl.kn.BookingEnvelopeDao;
import com.gp.cvst.logisoft.struts.lcl.kn.form.SearchForm;
import com.mysql.jdbc.CommunicationsException;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author palraj.p
 */
public class BookingUtils {

    private static final String DD_MM_YYYY_HH_MM_SS = "dd-MMM-yyyy HH:mm:ss";
    private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    private static Logger log = Logger.getLogger(XMLUtil.class);

    public Map<String, String> getSearchFilters(SearchForm searchForm) throws Exception {

        Date startDate = DateUtils.parseDate(searchForm.getStartDate() , DD_MM_YYYY_HH_MM_SS);
        Date endDate = DateUtils.parseDate(searchForm.getEndDate(), DD_MM_YYYY_HH_MM_SS);
        String sDate = DateUtils.formatDate(startDate, YYYY_MM_DD_HH_MM_SS);
        String eDate = DateUtils.formatDate(endDate, YYYY_MM_DD_HH_MM_SS);
        Map<String, String> filters = new HashMap<String, String>();
        if (CommonUtils.isNotEmpty(searchForm.getSearchBy()) && !"all".equals(searchForm.getSearchBy())) {
            filters.put("searchBy", searchForm.getSearchBy());
            if (CommonUtils.isNotEmpty(searchForm.getBkgNo())) {
                filters.put("bookingNumber", searchForm.getBkgNo());
            }
            if (CommonUtils.isNotEmpty(sDate)) {
                filters.put("startDate", sDate);
            }
            if (CommonUtils.isNotEmpty(eDate)) {
                filters.put("endDate", eDate);
            }
        } else {
            filters.put("limitRecord", searchForm.getLimitRecord());
        }
        if (CommonUtils.isNotEmpty(searchForm.getSearchType())) {
            filters.put("searchType", searchForm.getSearchType());
        }
        if (CommonUtils.isNotEmpty(searchForm.getSortBy())) {
            filters.put("sortBy", searchForm.getSortBy());
        } else {
            filters.put("sortBy", "createdOn");
        }
        return filters;
    }

    public void persistIntoWebtools(BookingEnvelope logiBkg) throws Exception {
        Map<String, String> valueMap = new HashMap<String, String>();
        WebtoolsDBUtil webtoolsDBUtil = new WebtoolsDBUtil();
        BookingEnvelopeDao bkgEnvelopeDao = new BookingEnvelopeDao();
        XMLUtil xmlUtil = new XMLUtil();
        File confirmationFile = null;
        byte[] bcFile = new byte[1024];
        try {
            for (BookingDetail bkgDetail : logiBkg.getBookingDetailList()) {
                valueMap.put("bookingIds", String.valueOf(bkgDetail.getId()));
                String[] values = webtoolsDBUtil.saveBooking(valueMap);
                webtoolsDBUtil.saveBookingHaz(valueMap, values);
                bkgDetail.setBkgNumber(values[0]);
                bkgEnvelopeDao.update(bkgDetail.getId(), values[0]);
            }
            confirmationFile = xmlUtil.generateXML(logiBkg);
            bcFile = xmlUtil.getConfirmation(confirmationFile);
            bkgEnvelopeDao.updateEnvelope(logiBkg, bcFile);
        } catch (CommunicationsException exception) {
            log.error("Webtools Database Unavailabe." + exception);
            bkgEnvelopeDao.updateError(logiBkg, "Webtools Database Unavailabe.");
        } catch (IllegalArgumentException exception) {
            log.error(exception);
            bkgEnvelopeDao.updateError(logiBkg, exception.getMessage());
        }
    }
}

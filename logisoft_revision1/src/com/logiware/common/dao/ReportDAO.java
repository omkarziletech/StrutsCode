package com.logiware.common.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.logiware.common.constants.ReportType;
import com.logiware.common.domain.Report;
import com.logiware.common.form.ReportForm;
import com.logiware.common.model.ReportModel;
import com.logiware.common.reports.CsvCreator;
import com.logiware.common.reports.ExcelCreator;
import com.logiware.common.reports.PdfCreator;
import com.logiware.common.transformer.AliasToEntityMapResultTransformer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ReportDAO extends BaseHibernateDAO implements ConstantsInterface {

    public String validateQueries(String query1, String query2) {
        try {
            List<Map<String, Object>> query1Result = getQuery1Results(query1, 1);
            if (CommonUtils.isNotEmpty(query1Result) && CommonUtils.isNotEmpty(query2)) {
                for (Map<String, Object> row : query1Result) {
                    if (null == row.get("email") && null == row.get("Email")) {
                        throw new Exception("Email column must be required.");
                    }
                    Set<String> columnNames = row.keySet();
                    for (String columnName : columnNames) {
                        query2 = query2.replace("query1." + columnName, row.get(columnName).toString());
                    }
                    try {
                        SQLQuery query = getCurrentSession().createSQLQuery(query2);
                        query.setMaxResults(1);
                        query.list();
                    } catch (Exception e) {
                        return "Query 2 is not valid one.\n" + (null != e.getCause() ? e.getCause().getMessage() : e.getMessage());
                    }
                }
            }
            return "valid";
        } catch (Exception e) {
            return "Query 1 is not valid one.\n" + (null != e.getCause() ? e.getCause().getMessage() : e.getMessage());
        }
    }

    public void save(Report report) {
        getCurrentSession().save(report);
        getCurrentSession().flush();
    }

    public void update(Report report) {
        getCurrentSession().update(report);
        getCurrentSession().flush();
    }

    public void saveOrUpdate(Report report) {
        getCurrentSession().saveOrUpdate(report);
        getCurrentSession().flush();
    }

    public void delete(Report report) {
        getCurrentSession().delete(report);
        getCurrentSession().flush();
    }

    public Report findById(Integer id) {
        return (Report) getCurrentSession().get(Report.class, id);
    }

    public Report findById(String id) {
        return findById(Integer.parseInt(id));
    }

    public List<Report> getEnabledReports() {
        Criteria criteria = getCurrentSession().createCriteria(Report.class);
        criteria.add(Restrictions.eq("enabled", true));
        return criteria.list();
    }

    private Criteria createCriteria() {
        Criteria criteria = getCurrentSession().createCriteria(Report.class);
        return criteria;
    }

    private Integer getTotalRows(Criteria criteria) {
        criteria.setProjection(Projections.count("id"));
        Long count = (Long) criteria.uniqueResult();
        return null != count ? count.intValue() : 0;
    }

    private List<Report> getResults(Criteria criteria, String sortBy, String orderBy, int first, int limit) {
        if (CommonUtils.isEqualIgnoreCase(orderBy, "asc")) {
            criteria.addOrder(Order.asc(sortBy));
        } else {
            criteria.addOrder(Order.desc(sortBy));
        }
        criteria.setFirstResult(first);
        criteria.setMaxResults(limit);
        criteria.setProjection(null);
        return criteria.list();
    }

    public void search(ReportForm reportForm) {
        Criteria criteria = createCriteria();
        int totalRows = getTotalRows(criteria);
        if (totalRows > 0) {
            int limit = reportForm.getLimit();
            int first = limit * (reportForm.getSelectedPage() - 1);
            List<Report> reports = getResults(criteria, reportForm.getSortBy(), reportForm.getOrderBy(), first, limit);
            reportForm.setReports(reports);
            reportForm.setSelectedRows(reports.size());
            int totalPages = (totalRows / limit) + (totalRows % limit > 0 ? 1 : 0);
            reportForm.setTotalPages(totalPages);
            reportForm.setTotalRows(totalRows);
        }
    }

    private List<Map<String, Object>> getQuery1Results(String query1, int limit) {
        SQLQuery query = getCurrentSession().createSQLQuery(query1);
        if (limit != 0) {
            query.setMaxResults(limit);
        }
        query.setResultTransformer(new AliasToEntityMapResultTransformer());
        return query.list();
    }

    private List<Map<String, Object>> getQuery2Results(String query2) {
        SQLQuery query = getCurrentSession().createSQLQuery(query2);
        query.setResultTransformer(new AliasToEntityMapResultTransformer());
        return query.list();
    }

    private List<String[]> getData(boolean isHeader, List<Map<String, Object>> result) {
        List<String[]> data = new ArrayList<String[]>();
        boolean isFirstRow = true;
        for (Map<String, Object> row : result) {
            Set<String> columnNames = row.keySet();
            if (isHeader && isFirstRow) {
                int count = 0;
                String[] string = new String[columnNames.size()];
                for (String columnName : columnNames) {
                    string[count] = columnName;
                    count++;
                }
                data.add(string);
                isFirstRow = false;
            }
            String[] string = new String[columnNames.size()];
            int count = 0;
            for (String columnName : columnNames) {
                string[count] = null != row.get(columnName) ? row.get(columnName).toString() : null;
                count++;
            }
            data.add(string);
        }
        return data;
    }

    public void preview(ReportForm reportForm, String contextPath) throws Exception {
        List<String[]> data = new ArrayList<String[]>();
        Report report = findById(reportForm.getId());
        String query1 = report.getQuery1();
        List<Map<String, Object>> query1Result = getQuery1Results(query1, 5);
        if (CommonUtils.isNotEmpty(query1Result)) {
            if (CommonUtils.isNotEmpty(report.getQuery2())) {
                for (Map<String, Object> row : query1Result) {
                    String query2 = report.getQuery2();
                    Set<String> columnNames = row.keySet();
                    for (String columnName : columnNames) {
                        query2 = query2.replace("query1." + columnName, row.get(columnName).toString());
                    }
                    List<Map<String, Object>> query2Result = getQuery2Results(query2);
                    if (CommonUtils.isNotEmpty(query2Result)) {
                        data.addAll(getData(report.isHeader(), query2Result));
                        break;
                    }
                }
            } else {
                data.addAll(getData(report.isHeader(), query1Result));
            }
            if (CommonUtils.isNotEmpty(data)) {
                if (report.getReportType().equals(ReportType.CSV)) {
                    reportForm.setFileName(new CsvCreator().create(report.getReportName(), data));
                } else if (report.getReportType().equals(ReportType.XLS)) {
                    reportForm.setFileName(new ExcelCreator().create(report.getReportName(), report.isHeader(), data));
                } else if (report.getReportType().equals(ReportType.PDF)) {
                    reportForm.setFileName(new PdfCreator(report.getReportName(), contextPath).create(report.isHeader(), data));
                }
            }
        }
    }

    private void saveEmail(Report report, User user, String toAddress, String fileName) throws Exception {
        EmailSchedulerVO email = new EmailSchedulerVO();
        email.setFileLocation(fileName);
        email.setType(CONTACT_MODE_EMAIL);
        email.setStatus(EMAIL_STATUS_PENDING);
        email.setNoOfTries(0);
        email.setEmailDate(new Date());
        email.setModuleName("REPORT");
        email.setModuleId(report.getReportName());
        email.setName(report.getReportName());
        email.setUserName(user.getLoginName());
        email.setFromName(user.getFirstName() + " " + user.getLastName());
        email.setFromAddress(user.getEmail());
        if (CommonUtils.isNotEmpty(toAddress)) {
            if (CommonUtils.isNotEmpty(report.getEmailId())) {
                if (toAddress.contains(";")) {
                    toAddress += ";" + report.getEmailId();
                } else {
                    toAddress += "," + report.getEmailId();
                }
            }
            email.setToAddress(toAddress);
        } else {
            email.setToAddress(report.getEmailId());
        }
        email.setCcAddress(null);
        email.setBccAddress(null);
        email.setSubject(report.getReportName());
        email.setTextMessage(report.getEmailBody());
        email.setHtmlMessage(report.getEmailBody());
        new EmailschedulerDAO().save(email);
    }

    public void send(Report report, String contextPath) throws Exception {
        String query1 = report.getQuery1();
        List<Map<String, Object>> query1Result = getQuery1Results(query1, 0);
        if (CommonUtils.isNotEmpty(query1Result)) {
            User user = new UserDAO().getUserInfo(report.getSender());
            if (CommonUtils.isNotEmpty(report.getQuery2())) {
                for (Map<String, Object> row : query1Result) {
                    String query2 = report.getQuery2();
                    String email = (String) row.get("email");
                    if (CommonUtils.isEmpty(email)) {
                        email = (String) row.get("Email");
                    }
                    Set<String> columnNames = row.keySet();
                    for (String columnName : columnNames) {
                        query2 = query2.replace("query1." + columnName, row.get(columnName).toString());
                    }
                    List<Map<String, Object>> query2Result = getQuery2Results(query2);
                    if (CommonUtils.isNotEmpty(query2Result)) {
                        List<String[]> data = getData(report.isHeader(), query2Result);
                        if (report.getReportType().equals(ReportType.CSV)) {
                            String fileName = new CsvCreator().create(report.getReportName(), data);
                            saveEmail(report, user, email, fileName);
                        } else if (report.getReportType().equals(ReportType.XLS)) {
                            String fileName = new ExcelCreator().create(report.getReportName(), report.isHeader(), data);
                            saveEmail(report, user, email, fileName);
                        } else if (report.getReportType().equals(ReportType.PDF)) {
                            String fileName = new PdfCreator(report.getReportName(), contextPath).create(report.isHeader(), data);
                            saveEmail(report, user, email, fileName);
                        }
                    }
                }
            } else {
                List<String[]> data = getData(report.isHeader(), query1Result);
                if (report.getReportType().equals(ReportType.CSV)) {
                    String fileName = new CsvCreator().create(report.getReportName(), data);
                    saveEmail(report, user, null, fileName);
                } else if (report.getReportType().equals(ReportType.XLS)) {
                    String fileName = new ExcelCreator().create(report.getReportName(), report.isHeader(), data);
                    saveEmail(report, user, null, fileName);
                } else if (report.getReportType().equals(ReportType.PDF)) {
                    String fileName = new PdfCreator(report.getReportName(), contextPath).create(report.isHeader(), data);
                    saveEmail(report, user, null, fileName);
                }
            }
        }
    }

    private String bookingQuery(ReportForm reportForm){
      StringBuilder queryBuilder = new StringBuilder();
      queryBuilder.append(" ( select ");
        queryBuilder.append("  CONCAT('04-', bkg.`file_no`) AS fileNo,");
        queryBuilder.append("  bkg.`bookingnumber` AS bookingNo,");
        queryBuilder.append("  bkg.`portofdischarge` AS pod,");
        queryBuilder.append("  bkg.`sslname` AS carrier,");
        queryBuilder.append("  IF(bkg.`billtocode` = 'F',bkg.`Forward`,IF(bkg.`billtocode` = 'S',bkg.`Shipper`,IF(bkg.`billtocode` = 'T',bkg.`account_name`,bkg.`agent`))) AS client,");
        queryBuilder.append(" DATE_FORMAT(bkg.`doc_cut_off`, '%m/%d/%Y') AS docCutOff, ");
        queryBuilder.append(" DATE_FORMAT(bkg.`carrier_doc_cut`, '%m/%d/%Y') AS carrierDocCutOff, ");
        queryBuilder.append("  date_format(bkg.`etd`, '%m/%d/%Y') as etd,");
        queryBuilder.append("  date_format(bkg.`eta`, '%m/%d/%Y') as eta,");
        queryBuilder.append("  '' as confirmedOnBoard,");
        queryBuilder.append("  '' as receivedSslMaster,  ");
        queryBuilder.append("  ucase(bkg.`username`) as bookedBy ");
        queryBuilder.append("from");
        queryBuilder.append( " `booking_fcl` bkg LEFT JOIN fcl_bl bl ON (bkg.file_no = bl.file_no) ");
        queryBuilder.append(" where  ");
        queryBuilder.append( "  bkg.file_no IS NOT NULL");
        queryBuilder.append( "   AND bl.bol IS NULL");
        if (reportForm.getDateRange().equals("sailDate")) {
            queryBuilder.append("  AND  date(bkg.`etd`) between :fromDate and :toDate ");
        } else if (reportForm.getDateRange().equals("carrierDocCutOff")) {
            queryBuilder.append("  AND  date(bkg.`carrier_doc_cut`) between :fromDate and :toDate ");
        } else if (reportForm.getDateRange().equals("docCutOff")) {
            queryBuilder.append("  AND date(bkg.`doc_cut_off`) between :fromDate and :toDate ");
        }
        queryBuilder.append("  and bkg.`issuing_terminal` in :billingTerminals");
        queryBuilder.append("  and bkg.`portofdischarge` in (select concat(p.`portname`, '/', if(p.`statecode` <> '', concat(p.`statecode`, '/'), ''), p.`countryname`, concat('(', p.`unlocationcode`,')')) from `ports` p where p.`regioncode` in (:destinationRegions))");
        queryBuilder.append("  and (");
        queryBuilder.append("    bkg.`importflag` is null");
        queryBuilder.append("    or bkg.`importflag` <> 'I'");
        queryBuilder.append("  ) ");
        queryBuilder.append("group by bkg.`file_no` )");
      return queryBuilder.toString();
    }
    private String blQuery(ReportForm reportForm) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" ( select ");
        queryBuilder.append("  concat('04-', bl.`file_no`) as fileNo,");
        queryBuilder.append("  bl.`bookingno` as bookingNo,");
        queryBuilder.append("  bl.`portofdischarge` as pod,");
        queryBuilder.append("  bl.`ssline_name` as carrier,");
        queryBuilder.append("  if(bl.`bill_to_code` = 'F', bl.`forwarding_agent_name`, if(bl.`bill_to_code` = 'S', bl.`house_shipper_name`, if(bl.`bill_to_code` = 'T', bl.`third_party_name`, bl.`agent`))) as client,");
        queryBuilder.append(" DATE_FORMAT(bkg.`doc_cut_off`, '%m/%d/%Y') AS docCutOff, ");
        queryBuilder.append(" DATE_FORMAT(bkg.`carrier_doc_cut`, '%m/%d/%Y') AS carrierDocCutOff, ");
        queryBuilder.append("  date_format(bl.`sail_date`, '%m/%d/%Y') as etd,");
        queryBuilder.append("  date_format(bl.`eta`, '%m/%d/%Y') as eta,");
        queryBuilder.append("  if(bl.`confirm_on_board` = 'Y', 'Yes', 'No') as confirmedOnBoard,");
        queryBuilder.append("  bl.`received_master` as receivedSslMaster,  ");
        queryBuilder.append("  ucase(bkg.`username`) as bookedBy ");
        queryBuilder.append("from");
        queryBuilder.append("  `fcl_bl` bl ");
        queryBuilder.append("  join `booking_fcl` bkg");
        queryBuilder.append("    on (bl.`file_no` = bkg.`file_no`)");
        queryBuilder.append("where ");
        if (reportForm.getDateRange().equals("sailDate")) {
            queryBuilder.append("  date(bl.`sail_date`) between :fromDate and :toDate ");
        } else if (reportForm.getDateRange().equals("carrierDocCutOff")) {
            queryBuilder.append("  date(bkg.`carrier_doc_cut`) between :fromDate and :toDate ");
        } else if (reportForm.getDateRange().equals("docCutOff")) {
            queryBuilder.append("  date(bkg.`doc_cut_off`) between :fromDate and :toDate ");
        }
        queryBuilder.append("  and bl.`billing_terminal` in :billingTerminals");
        queryBuilder.append("  and bl.`portofdischarge` in (select concat(p.`portname`, '/', if(p.`statecode` <> '', concat(p.`statecode`, '/'), ''), p.`countryname`, concat('(', p.`unlocationcode`,')')) from `ports` p where p.`regioncode` in (:destinationRegions))");
        queryBuilder.append("  and (");
        queryBuilder.append("    bl.`importflag` is null");
        queryBuilder.append("    or bl.`importflag` <> 'I'");
        queryBuilder.append("  ) ");
        queryBuilder.append("group by bl.`file_no` )");
        return queryBuilder.toString();
    }
    public List<ReportModel> getCobReportList(ReportForm reportForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        if (reportForm.getIncludeBookings().equals("true")) {
            queryBuilder.append(bookingQuery(reportForm));
            queryBuilder.append("UNION ALL");
        } 
        queryBuilder.append(blQuery(reportForm));
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        List<String> billingTerminals = Arrays.asList(reportForm.getBillingTerminal().split("<-->"));
        List<Long> destinationRegions = new ArrayList<Long>();
        for (String id : reportForm.getDestinationRegions().split(",")) {
            destinationRegions.add(Long.parseLong(id));
        }
         query.setString("fromDate", DateUtils.formatDate(DateUtils.parseDate(reportForm.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd"));
        query.setString("toDate", DateUtils.formatDate(DateUtils.parseDate(reportForm.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd"));
        query.setParameterList("billingTerminals", billingTerminals);
        query.setParameterList("destinationRegions", destinationRegions);
        query.setResultTransformer(Transformers.aliasToBean(ReportModel.class));
        query.addScalar("fileNo", StringType.INSTANCE);
        query.addScalar("bookingNo", StringType.INSTANCE);
        query.addScalar("pod", StringType.INSTANCE);
        query.addScalar("carrier", StringType.INSTANCE);
        query.addScalar("client", StringType.INSTANCE);
        query.addScalar("docCutOff", StringType.INSTANCE);
        query.addScalar("carrierDocCutOff", StringType.INSTANCE);
        query.addScalar("etd", StringType.INSTANCE);
        query.addScalar("eta", StringType.INSTANCE);
        query.addScalar("confirmedOnBoard", StringType.INSTANCE);
        query.addScalar("receivedSslMaster", StringType.INSTANCE);
        query.addScalar("bookedBy", StringType.INSTANCE);
//        query.addScalar("pol", StringType.INSTANCE);
//        query.addScalar("consignee", StringType.INSTANCE);       
//        query.addScalar("consigneeOnMBL", StringType.INSTANCE);
//        query.addScalar("deliveryAgentOnHBL", StringType.INSTANCE);
//        query.addScalar("houseNoOriginals", StringType.INSTANCE);
//        query.addScalar("masterNoOriginals", StringType.INSTANCE);
        return query.list();
    }
}

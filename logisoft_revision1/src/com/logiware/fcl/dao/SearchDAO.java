package com.logiware.fcl.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import static com.gp.cong.common.ConstantsInterface.NO;
import static com.gp.cong.common.ConstantsInterface.YES;
import com.gp.cong.common.DateUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.logiware.fcl.form.SearchForm;
import com.logiware.fcl.model.PortModel;
import com.logiware.fcl.model.ResultModel;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

/**
 *
 * @author Lakshmi Narayanan
 */
public class SearchDAO extends BaseHibernateDAO implements ConstantsInterface {

    private String getPorts(String regionCode) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select concat('\"',replace(concat(portname,'/',if(statecode!='',concat(statecode,'/'),''),");
        queryBuilder.append("countryname,if(unlocationcode!='',concat('(',unlocationcode,')'),'')),'\"','\\\"'),'\"') as port");
        queryBuilder.append(" from ports");
        queryBuilder.append(" where regioncode = ").append(regionCode);
        queryBuilder.append(" order by portname");
        List<String> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        return result.toString().replace("[", "").replace("]", "");
    }

    private String getNoMasterFiles(SearchForm form) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  concat(\"'\",f.file_no,\"'\") as fileNumber ");
        queryBuilder.append("from");
        queryBuilder.append("  fcl_bl f ");
        if (CommonUtils.isNotEmpty(form.getContainerNumber())) {
            String containerNumber = form.getContainerNumber().replace("'", "\\'");
            queryBuilder.append("  join fcl_bl_container_dtls c");
            queryBuilder.append("    on (");
            queryBuilder.append("      f.bol = c.bolid");
            queryBuilder.append("      and c.trailer_no like '%").append(containerNumber).append("%'");
            queryBuilder.append("    )");
        }
        if (CommonUtils.isNotEmpty(form.getInboundNumber())) {
            String inboundNumber = form.getInboundNumber().replace("'", "\\'");
            queryBuilder.append("   join fcl_inbond_details i");
            queryBuilder.append("    on (");
            queryBuilder.append("      f.bol = i.bol_id");
            queryBuilder.append("      and i.inbond_number like '%").append(inboundNumber).append("%'");
            queryBuilder.append("    )");
        }
        if (CommonUtils.isNotEmpty(form.getAesItn())) {
            String aesItn = form.getAesItn().replace("'", "\\'");
            queryBuilder.append("   join fcl_aes_details a");
            queryBuilder.append("    on (");
            queryBuilder.append("      f.file_no = a.file_no");
            queryBuilder.append("      and a.aes_details like '%").append(aesItn).append("%'");
            queryBuilder.append("    )");
        }
        queryBuilder.append("where f.received_master = 'No'");
        if (form.isImportFile()) {
            queryBuilder.append("  and f.importflag = 'I'");
        } else {
            queryBuilder.append("  and (f.importflag != 'I' or f.importflag is null)");
        }
        queryBuilder.append("  and f.file_no not like '%-%'");
        if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Manifested No Master")) {
            queryBuilder.append("	and f.manifested_date is not null");
            queryBuilder.append("	and f.ready_to_post = 'M'");
        }
        if (CommonUtils.isNotEmpty(form.getSslBookingNumber())) {
            String sslBookingNumber = form.getSslBookingNumber().replace("'", "\\'");
            queryBuilder.append("  and f.bookingno = '").append(sslBookingNumber).append("'");
        }
        if (CommonUtils.isNotEmpty(form.getMasterBl())) {
            String masterBl = form.getMasterBl().replace("'", "\\'");
            queryBuilder.append("  and f.new_master_bl like '%").append(masterBl).append("%'");
        }
        if (CommonUtils.isAllNotEmpty(form.getFromDate(), form.getToDate())) {
            String fromDate = DateUtils.formatDate(DateUtils.parseDate(form.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
            String toDate = DateUtils.formatDate(DateUtils.parseDate(form.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
            if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Manifested No Master")) {
                queryBuilder.append("	and date(f.manifested_date) between '").append(fromDate).append("' and '").append(toDate).append("'");
            } else if (form.isSailingDate()) {
                queryBuilder.append("	and date(f.sail_date) between '").append(fromDate).append("' and '").append(toDate).append("'");
            } else {
                queryBuilder.append("   and date(f.bol_date) between '").append(fromDate).append("' and '").append(toDate).append("'");
            }
        }
        queryBuilder.append("  group by f.file_no");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("fileNumber", StringType.INSTANCE);
        List<String> result = query.list();
        return CommonUtils.isNotEmpty(result) ? result.toString().replace("[", "").replace("]", "") : null;
    }

    private String buildOuterSelectExpression(boolean isImportFile) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append(" f.file_no as file_no,");
        queryBuilder.append(" f.file_type as fileType,");
        queryBuilder.append("if(f.file_type = 'Quote',");
        queryBuilder.append("	if(f.rated, 'Rated', 'Non Rated'),");
        queryBuilder.append("	''");
        queryBuilder.append(") as quoteStatus,");
        if (!isImportFile) {
            queryBuilder.append("if(f.file_type = 'Quote',");
            queryBuilder.append("	if(f.multiQuote,'M',''),");
            queryBuilder.append("	''");
            queryBuilder.append(") as multiStatus,");
        }
        queryBuilder.append("if(f.file_type = 'Booking',");
        queryBuilder.append("	if(f.booking_complete,'Booking Complete',");
        queryBuilder.append("	    if(f.rated,'Rated In Process','Non Rated')");
        queryBuilder.append("   ),");
        queryBuilder.append("   ''");
        queryBuilder.append(") as bookingStatus,");
        queryBuilder.append("if(f.file_type = 'BL',");
        queryBuilder.append("	if(not(f.manifested) and f.etd > current_date() and datediff(current_date(), f.bl_date) > 15,'BL not Manifested',");
        queryBuilder.append("	    if(f.voided,'Void BL',");
        queryBuilder.append("          if(not(f.manifested) and f.etd < current_date(),'Sailing Date Past',");
        queryBuilder.append("            if((select count(c.file_no)");
        queryBuilder.append("			from fclblcorrections c");
        queryBuilder.append("			    where (f.file_no = c.file_no");
        queryBuilder.append("				and (c.status is null or c.status != 'Disable')");
        queryBuilder.append("			    )");
        queryBuilder.append("		    ) >= 1,'Corrected',");
        queryBuilder.append("		    if(f.manifested and f.closed and f.audited,'Manifested, Closed, Audited',");
        queryBuilder.append("			if(f.manifested and f.closed,'Manifested, Closed',");
        queryBuilder.append("			    if(f.manifested, 'Manifested', 'BL In Process')");
        queryBuilder.append("			)");
        queryBuilder.append("              )");
        queryBuilder.append("            )");
        queryBuilder.append("          )");
        queryBuilder.append("	    )");
        queryBuilder.append("	),");
        queryBuilder.append("	''");
        queryBuilder.append(") as blStatus,");
        queryBuilder.append("f.docCutOff as docCutOff,f.portCutOff as portCutOff,f.etd as etd,");
        queryBuilder.append("  hazmat as hazmat,");
        queryBuilder.append("(select");
        queryBuilder.append(" if(edi.status = 'success' and edi997.si_id is not null,'997 success',");
        queryBuilder.append("	if(edi.status = 'success','304 success',");
        queryBuilder.append("	    if(edi.status = 'failure','304 failure','')");
        queryBuilder.append("	)");
        queryBuilder.append(" )");
        queryBuilder.append(" from logfile_edi edi");
        queryBuilder.append(" left join edi_997_ack edi997");
        queryBuilder.append("	on (edi.id = edi997.si_id)");
        queryBuilder.append(" where edi.file_no = f.file_no");
        queryBuilder.append("	and edi.message_type = '304'");
        queryBuilder.append(" and edi.doc_type is null ");
        queryBuilder.append(" order by edi.id desc limit 1");
        queryBuilder.append(") as ediStatus,");
        queryBuilder.append("(select ");
        queryBuilder.append("if(edi.status = 'cancel' and (edi.message_type  = '301'),'Cancel',");
        queryBuilder.append("if(edi.status = 'success' and edi.message_type = '300','300 Success',");
        queryBuilder.append("if(edi.status = 'change' and edi.message_type = '300','300 Change',");
        queryBuilder.append("if(edi.status = 'failure' and edi.message_type = '300','300 Failure',");
        queryBuilder.append("if(edi.status = 'success' and edi.message_type = '997','997 Booking Success',");
        queryBuilder.append("if(edi.status = 'failure' and edi.message_type = '997','997 Booking Failure',");
        queryBuilder.append("if(edi.status = 'success' and edi.message_type = '301','301 Success',");
        queryBuilder.append("if(edi.status = 'failure' and edi.message_type = '301','301 Failure',");
        queryBuilder.append("if(edi.status = 'pending' and edi.message_type = '301','301 Pending',");
        queryBuilder.append("if(edi.status = 'conditionaccepted' and edi.message_type = '301','301 Conditionally Accepted',");
        queryBuilder.append("if(edi.status = 'declined' and edi.message_type = '301','301 Declined',");
        queryBuilder.append("if(edi.status = 'replaced' and edi.message_type = '301','301 Replaced','')");
        queryBuilder.append("))))))))))) ");
        queryBuilder.append("from logfile_edi edi ");
        queryBuilder.append("where edi.file_no = f.file_no ");
        queryBuilder.append("and edi.message_type in('300','997','301') ");
        queryBuilder.append("and edi.doc_type='Booking' ");
        queryBuilder.append("order by edi.id desc limit 1 ");
        queryBuilder.append(") as bookingEdiStatus,");
        queryBuilder.append("f.file_no as fileNumber,");
        queryBuilder.append("f.bl_id as blId,");
        queryBuilder.append("f.booking_id as bookingId,");
        queryBuilder.append("f.quote_id as quoteId,");
        queryBuilder.append("concat_ws(',',");
        queryBuilder.append("	if(f.doc_cut_off > 0,concat(f.doc_cut_off, 'D'),null),");
        queryBuilder.append("	if(f.container_cut_off > 0,concat(f.container_cut_off, 'C'),null),");
        queryBuilder.append("	if(datediff(f.etd, current_date()) > 0,concat(datediff(f.etd, current_date()), 'S'),null),");
        queryBuilder.append("	if(not (f.rated), concat('NR'), null),");
        queryBuilder.append("	if(not (f.rated) and break_bulk = '").append(YES).append("',concat('1U'),");
        queryBuilder.append("	    if(container_size > 0,concat(container_size, 'U'),null)");
        queryBuilder.append("	),");
        queryBuilder.append("	if(f.inttra != '', f.inttra, null),");
        queryBuilder.append("	if(f.ready_to_edi != '', 'E', null),");
        queryBuilder.append("	if(f.audited, 'A', null),");
        queryBuilder.append("	if(f.received_master = 'Yes','RM',null),");
        queryBuilder.append("	if(f.closed, 'CL', null),");
        queryBuilder.append("	if(f.voided, 'V', null),");
        if (!isImportFile) {
            queryBuilder.append("	if(f.spot_rate='Y', 'SP', null),");
        }
        queryBuilder.append("	if(f.manifested,");
        queryBuilder.append("	    concat_ws(',',");
        queryBuilder.append("		'M',");
        queryBuilder.append("		if(");
        queryBuilder.append("		    (select count(c.file_no)");
        queryBuilder.append("			from fclblcorrections c");
        queryBuilder.append("			    where (f.file_no = c.file_no");
        queryBuilder.append("				and (c.status is null or c.status != 'Disable')");
        queryBuilder.append("			    )");
        queryBuilder.append("		    ) >= 1,");
        queryBuilder.append("		'CN',null)");
        queryBuilder.append("	    ),");
        queryBuilder.append("	    null");
        queryBuilder.append("	)");
        queryBuilder.append(") as fileStatus,");
        if (isImportFile) {
            queryBuilder.append("f.release_type as releaseType,");
            queryBuilder.append("cast(group_concat(cn.trailer_no) as char character set latin1) as containerNumber,");
        } else {
            queryBuilder.append("if(f.documents_received = '").append(YES).append("', true, false) as documentReceived,");
        }
        queryBuilder.append("(select doc.status");
        queryBuilder.append("	 from document_store_log doc");
        queryBuilder.append("	    where document_id = f.file_no");
        queryBuilder.append("	    order by doc.id desc limit 1");
        queryBuilder.append(") as documentStatus,");
        queryBuilder.append("f.ssl_booking_no as sslBookingNumber,");
        queryBuilder.append("date_format(f.start_date, '%d-%b-%Y') as startDate,");
        queryBuilder.append("f.start_date as start_date,");
        queryBuilder.append("date_format(f.sail_date, '%d-%b-%Y') as sailDate,");
        queryBuilder.append("f.sail_date as sail_date,");
        queryBuilder.append("f.origin as origin,");
        queryBuilder.append("f.door_origin as doorOrigin,");
        queryBuilder.append("f.pol as pol,");
        queryBuilder.append("f.pod as pod,");
        queryBuilder.append("f.destination as destination,");
        queryBuilder.append("f.door_destination as doorDestination,");
        queryBuilder.append("f.client_name as clientName,");
        queryBuilder.append("f.ssline_name as sslineName,");
        queryBuilder.append("f.issuing_terminal as issuingTerminal,");
        if (isImportFile) {
            queryBuilder.append("date_format(f.eta, '%d-%b-%Y') as eta,");
            queryBuilder.append("f.eta as eta_date,");
        } else {
            queryBuilder.append("(select if(aes.status != '',aes.status,aes.itn)");
            queryBuilder.append("	from aes_history aes");
            queryBuilder.append("	    where (aes.file_no = f.file_no)");
            queryBuilder.append("	    order by aes.id desc limit 1");
            queryBuilder.append(") as aesStatus,");
            queryBuilder.append("(select count(aes.id)");
            queryBuilder.append("	from sed_filings aes");
            queryBuilder.append("	where aes.shpdr = f.file_no");
            queryBuilder.append("	    and aes.status = 'S'");
            queryBuilder.append(") as aesCount,");
        }
        queryBuilder.append("f.created_by as createdBy,");
        queryBuilder.append("f.booked_by as bookedBy ");
        return queryBuilder.toString();
    }

    private String buildInnerSelectExpression(boolean isImportFile) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("if(");
        queryBuilder.append("    f.file_no is not null,");
        queryBuilder.append("    f.file_no,");
        queryBuilder.append("    if(");
        queryBuilder.append("      b.file_no is not null,");
        queryBuilder.append("      b.file_no,");
        queryBuilder.append("      q.file_no");
        queryBuilder.append("    )");
        queryBuilder.append("  ) as file_no,");
        queryBuilder.append("if(");
        queryBuilder.append("    f.file_no is not null,");
        queryBuilder.append("    f.spot_rate,");
        queryBuilder.append("    if(");
        queryBuilder.append("      b.file_no is not null,");
        queryBuilder.append("      b.spot_rate,");
        queryBuilder.append("      q.spot_rate");
        queryBuilder.append("    )");
        queryBuilder.append("  ) as spot_rate,");
        queryBuilder.append("if(f.bol is not null,'BL',");
        queryBuilder.append("	if(b.bookingid is not null,'Booking','Quote')");
        queryBuilder.append(") as file_type,");
        queryBuilder.append("q.quote_id as quote_id,");
        queryBuilder.append("b.bookingid as booking_id,");
        queryBuilder.append("f.bol as bl_id,");
        queryBuilder.append("if((f.rates_non_rates = 'R' or b.rates_non_rates = 'R' or q.rates_non_rates = 'R'),true,false) as rated,");
        queryBuilder.append("if(q.multi_quote_flag IN('M','C'),true,false) AS multiQuote,");//export
        queryBuilder.append("if(b.bookingcomplete = '").append(YES).append("',true,false) as booking_complete,");
        queryBuilder.append("if(f.ready_to_post = 'M', true, false) as manifested,");
        queryBuilder.append("if(f.bl_closed = '").append(YES).append("', true, false) as closed,");
        queryBuilder.append("if(f.bl_audited = '").append(YES).append("', true, false) as audited,");
        queryBuilder.append("if(f.sail_date is not null,f.sail_date,b.etd) as etd,");
        queryBuilder.append("f.bol_date as bl_date,");
        queryBuilder.append("if(f.void = '").append(YES).append("', true, false) as voided,");
        queryBuilder.append("q.origin_terminal as origin,");
        queryBuilder.append("q.door_origin as door_origin,");
        queryBuilder.append("q.destination_port as destination,");
        queryBuilder.append("if(f.door_of_destination != '',f.door_of_destination,");
        queryBuilder.append("	if(b.door_destination != '',b.door_destination,q.door_destination)");
        queryBuilder.append(") as door_destination,");
        queryBuilder.append("if(f.port_of_loading != '',f.port_of_loading,");
        queryBuilder.append("	if(b.portoforgin != '',b.portoforgin,q.plor)");
        queryBuilder.append(") as pol,");
        queryBuilder.append("if(f.portofdischarge != '',f.portofdischarge,");
        queryBuilder.append("	if(b.portofdischarge != '',b.portofdischarge,q.finaldestination)");
        queryBuilder.append(") as pod,");
        queryBuilder.append("q.clientname as client_name,");
        queryBuilder.append("substring_index(");
        queryBuilder.append("	if(f.ssline_name != '',f.ssline_name,");
        queryBuilder.append("	    if(b.sslname != '',b.sslname,q.carrier)");
        queryBuilder.append("	),'//',1");
        queryBuilder.append(") as ssline_name,");
        queryBuilder.append("if(f.hazmat = '").append(YES).append("' or b.hazmat = '").append(YES).append("' or q.hazmat = '").append(YES).append("',true,false) as hazmat,");
        queryBuilder.append("datediff(if(f.bol is not null,f.doc_cut_off,if(b.bookingid is not null,b.doc_cut_off,'')), current_date()) as doc_cut_off,");
        queryBuilder.append("datediff(if(f.bol is not null,f.port_cut_off,if(b.bookingid is not null,b.portcutoff,'')), current_date())");
        queryBuilder.append(" as container_cut_off,");
        queryBuilder.append("if(f.bol is not null,f.doc_cut_off,if(b.bookingid is not null,b.doc_cut_off,'')) as docCutOff,");
        queryBuilder.append("if(f.bol is not null,f.port_cut_off,if(b.bookingid is not null,b.portcutoff,'')) as portCutOff,");
        queryBuilder.append("f.break_bulk as break_bulk,");
        queryBuilder.append("b.container_size as container_size,");
        queryBuilder.append("f.inttra as inttra,");
        queryBuilder.append("f.ready_to_edi as ready_to_edi,");
        queryBuilder.append("f.received_master as received_master,");
        if (isImportFile) {
            queryBuilder.append("if(");
            queryBuilder.append("  f.payment_release != '',  concat(f.payment_release,if(f.import_release != '', f.import_release, '").append(NO).append("')),");
            queryBuilder.append("  if(f.import_release != '',concat('").append(NO).append("', f.import_release),'')");
            queryBuilder.append(") as release_type,");
        } else {
            queryBuilder.append("b.documents_received as documents_received,");
        }
        queryBuilder.append("if(f.bookingno != '',f.bookingno,b.ssbookingno) as ssl_booking_no,");
        queryBuilder.append("q.quote_date as start_date,");
        queryBuilder.append("if(f.bol is not null,f.sail_date,if(b.bookingid is not null,b.etd,'')) as sail_date,");
        queryBuilder.append("if(f.billing_terminal != '',f.billing_terminal,");
        queryBuilder.append("	if(b.issuing_terminal != '',b.issuing_terminal,q.issuing_terminal)");
        queryBuilder.append(") as issuing_terminal,");
        queryBuilder.append("upper(if(q.quote_by != '',q.quote_by,b.username)) as created_by,");
        queryBuilder.append("upper(b.username) as booked_by ");
        return queryBuilder.toString();
    }

    private String buildExportInnerWhereCondition(SearchForm form) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" from");
        queryBuilder.append("	quotation q");
        queryBuilder.append("   left join booking_fcl b ");
        queryBuilder.append("	    on (");
        queryBuilder.append("		q.file_no = b.file_no");
        queryBuilder.append("	    )");
        queryBuilder.append("   left join fcl_bl f ");
        queryBuilder.append("	    on (");
        queryBuilder.append("		q.file_no = f.file_number");
        queryBuilder.append("	    )");
        if (CommonUtils.isNotEmpty(form.getFileNumber())) {
//            String fileNumber = form.getFileNumber().replaceAll("[^\\p{Digit}]+", "");
            queryBuilder.append("   where (q.file_type != 'I' or q.file_type is null)");
            queryBuilder.append("	and q.file_no = '").append(form.getFileNumber()).append("'");
        } else {
            List<String> blFilters = new ArrayList<String>();
            blFilters.add("BL");
            blFilters.add("Un Manifested");
            blFilters.add("Manifested");
            blFilters.add("Manifested No Master");
            blFilters.add("Master Not Received");
            blFilters.add("FAE Not Applied");
            blFilters.add("No COB");
            List<String> blFields = new ArrayList<String>();
            blFields.add(form.getContainerNumber());
            blFields.add(form.getMasterBl());
            blFields.add(form.getVessel());
            blFields.add(form.getInboundNumber());
            blFields.add(form.getAesItn());
            boolean isQuote = CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Quotation");
            boolean isBooking = CommonUtils.in(form.getFilterBy(), "Booking", "Doc's Not Received");
            boolean isBl = false;
            if (!isQuote && !isBooking
                    && (form.isSailingDate()
                    || CommonUtils.in(form.getFilterBy(), (String[]) blFilters.toArray(new String[blFilters.size()]))
                    || CommonUtils.isAtLeastOneNotEmpty((String[]) blFields.toArray(new String[blFields.size()])))) {
                isBl = true;
            }
            if (isBl) {
                if (CommonUtils.isNotEmpty(form.getContainerNumber())) {
                    String containerNumber = form.getContainerNumber().replace("'", "\\'");
                    queryBuilder.append("   join fcl_bl_container_dtls c");
                    queryBuilder.append("	on (");
                    queryBuilder.append("	    f.bol = c.bolid");
                    queryBuilder.append("	    and c.trailer_no like '%").append(containerNumber).append("%'");
                    queryBuilder.append("	)");
                }
                if (CommonUtils.isNotEmpty(form.getInboundNumber())) {
                    String inboundNumber = form.getInboundNumber().replace("'", "\\'");
                    queryBuilder.append("   join fcl_inbond_details i");
                    queryBuilder.append("	on (");
                    queryBuilder.append("	    f.bol = i.bol_id");
                    queryBuilder.append("	    and i.inbond_number like '%").append(inboundNumber).append("%'");
                    queryBuilder.append("	)");
                }
                if (CommonUtils.isNotEmpty(form.getAesItn())) {
                    String aesItn = form.getAesItn().replace("'", "\\'");
                    queryBuilder.append("   join fcl_aes_details a");
                    queryBuilder.append("	on (");
                    queryBuilder.append("	    f.file_no = a.file_no");
                    queryBuilder.append("	    and a.aes_details like '%").append(aesItn).append("%'");
                    queryBuilder.append("	)");
                }
                if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "FAE Not Applied")) {
                    queryBuilder.append("   left join fcl_bl_costcodes co");
                    queryBuilder.append("	on (");
                    queryBuilder.append("	    f.bol = co.bolid");
                    queryBuilder.append("	    and	co.cost_code = 'FAECOMM'");
                    queryBuilder.append("	    and co.delete_flag = 'no'");
                    queryBuilder.append("	)");
                }
            }
            queryBuilder.append("   where (q.file_type != 'I' or q.file_type is null)");
            if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "Doc Cut off")) {
                queryBuilder.append("  and b.doc_cut_off is not null and b.doc_cut_off <> ''");
            }
            queryBuilder.append("	and q.file_no is not null");
            if (isQuote) {
                queryBuilder.append("   and b.bookingid is null");
                queryBuilder.append("   and f.bol is null");
                if (form.isOlySpotRate()) {
                    queryBuilder.append(" and q.spot_rate='Y' ");
                }
            } else if (isBooking) {
                queryBuilder.append("   and f.bol is null");
                if (form.isOlySpotRate()) {
                    queryBuilder.append(" and b.spot_rate='Y' ");
                }
            } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "FAE Not Applied")) {
                queryBuilder.append("   and co.bolid is null");
            }
            if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Online Bookings")) {
                queryBuilder.append("	and ((q.file_no like 'F%' OR q.file_no like 'A%') ");
                queryBuilder.append("	or (b.file_no like 'F%' OR b.file_no like 'A%') ");
                queryBuilder.append("	or (f.file_no like 'F%' OR f.file_no like 'A%')) ");
            }
            if (CommonUtils.isNotEmpty(form.getOriginRegion())) {
                String origins = getPorts(form.getOriginRegion());
                queryBuilder.append("   and q.origin_terminal");
                if (CommonUtils.isNotEmpty(origins)) {
                    queryBuilder.append(" in (").append(origins).append(")");
                } else {
                    queryBuilder.append(" is null");
                }
            } else if (CommonUtils.isNotEmpty(form.getOrigin())) {
                String origin = form.getOrigin().replace("'", "\\'");
                queryBuilder.append("	and q.origin_terminal = '").append(origin).append("'");
            }
            if (CommonUtils.isNotEmpty(form.getDestinationRegion())) {
                String destinations = getPorts(form.getDestinationRegion());
                queryBuilder.append("	and q.destination_port");
                if (CommonUtils.isNotEmpty(destinations)) {
                    queryBuilder.append(" in (").append(destinations).append(")");
                } else {
                    queryBuilder.append(" is null");
                }
            } else if (CommonUtils.isNotEmpty(form.getDestination())) {
                String destination = form.getDestination().replace("'", "\\'");
                queryBuilder.append("	and q.destination_port = '").append(destination).append("'");
            }
            if (CommonUtils.isNotEmpty(form.getPol())) {
                String pol = form.getPol().replace("'", "\\'");
                queryBuilder.append("	and q.plor = '").append(pol).append("'");
            }
            if (CommonUtils.isNotEmpty(form.getPod())) {
                String pod = form.getPod().replace("'", "\\'");
                queryBuilder.append("	and q.finaldestination = '").append(pod).append("'");
            }
            if (isBooking) {
                if (CommonUtils.in(form.getFilterBy(), "Booking", "Doc's Not Received")
                        && CommonUtils.isAllNotEmpty(form.getFromDate(), form.getToDate())) {
                    String fromDate = DateUtils.formatDate(DateUtils.parseDate(form.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
                    String toDate = DateUtils.formatDate(DateUtils.parseDate(form.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
                    queryBuilder.append("	and date(b.bookingdate) between '").append(fromDate).append("' and '").append(toDate).append("'");
                }
                if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Doc's Not Received")) {
                    queryBuilder.append("	and b.file_no is not null");
                    queryBuilder.append("	and (");
                    queryBuilder.append("	    b.documents_received = '").append(NO).append("'");
                    queryBuilder.append("	    or b.doc_cut_off is null");
                    queryBuilder.append("	)");
                }
            }
            if (isBl) {
                queryBuilder.append("	and f.bol is not null");
                if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Un Manifested")) {
                    queryBuilder.append("	and f.manifested_date is null and f.file_no is not null");
                } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Manifested")) {
                    queryBuilder.append("	and f.manifested_date is not null");
                    queryBuilder.append("	and f.ready_to_post = 'M'");
                } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Manifested No Master")) {
                    queryBuilder.append("	and f.manifested_date is not null");
                    queryBuilder.append("	and f.ready_to_post = 'M'");
                    String fileNumbers = getNoMasterFiles(form);
                    if (CommonUtils.isNotEmpty(fileNumbers)) {
                        queryBuilder.append("	and substring_index(f.file_no, '-', 1) in (").append(fileNumbers).append(")");
                    } else {
                        queryBuilder.append("	and f.file_no is null");
                    }
                } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Master Not Received")) {
                    String fileNumbers = getNoMasterFiles(form);
                    if (CommonUtils.isNotEmpty(fileNumbers)) {
                        queryBuilder.append("	and substring_index(f.file_no, '-', 1) in (").append(fileNumbers).append(")");
                    } else {
                        queryBuilder.append("	and f.file_no is null");
                    }
                } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "No COB")) {
                    queryBuilder.append(" and f.confirm_on_board='N'");
                }
                if (CommonUtils.isNotEmpty(form.getMasterBl())) {
                    String masterBl = form.getMasterBl().replace("'", "\\'");
                    queryBuilder.append("	and f.new_master_bl like '%").append(masterBl).append("%'");
                }
                if (CommonUtils.isNotEmpty(form.getVessel())) {
                    queryBuilder.append("  and f.vessel like '%").append(getVesselIdFromDesc(form.getVessel())).append("%'");
                }
                if (CommonUtils.isAllNotEmpty(form.getFromDate(), form.getToDate())) {
                    String fromDate = DateUtils.formatDate(DateUtils.parseDate(form.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
                    String toDate = DateUtils.formatDate(DateUtils.parseDate(form.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
                    if (CommonUtils.in(form.getFilterBy(), "Manifested", "Manifested No Master")) {
                        queryBuilder.append("	and date(f.manifested_date) between '").append(fromDate).append("' and '").append(toDate).append("'");
                    } else if (form.isSailingDate()) {
                        queryBuilder.append("	and date(f.sail_date) between '").append(fromDate).append("' and '").append(toDate).append("'");
                    } else {
                        queryBuilder.append("   and date(f.bol_date) between '").append(fromDate).append("' and '").append(toDate).append("'");
                    }
                }
                if (form.isOlySpotRate()) {
                    queryBuilder.append(" and f.spot_rate='Y' ");
                }
            }
            if (CommonUtils.isNotEmpty(form.getSalesCode())) {
                queryBuilder.append("	and ( q.clientnumber in (").append(getSalesCode(form.getSalesCode())).append(")");
            } else if (form.isDisableClient() && CommonUtils.isNotEmpty(form.getManualClientName())) {
                String clientName = form.getManualClientName().replace("'", "\\'");
                queryBuilder.append("	and q.clientname like '%").append(clientName).append("%'");
            } else if (CommonUtils.isNotEmpty(form.getClientNumber())) {
                queryBuilder.append("	and q.clientnumber = '").append(form.getClientNumber()).append("'");
            }
            if (!isQuote) {
                if (CommonUtils.isNotEmpty(form.getSalesCode())) {
                    queryBuilder.append("	 or (");
                    queryBuilder.append("	    b.shipno in ( ").append(getSalesCode(form.getSalesCode())).append(")");
                    if (!isBooking) {
                        queryBuilder.append("	    or f.house_shipper_no in ( ").append(getSalesCode(form.getSalesCode())).append(")");
                    }
                    queryBuilder.append("	)");
                } else if (CommonUtils.isNotEmpty(form.getShipperNumber())) {
                    queryBuilder.append("	 and (");
                    queryBuilder.append("	    b.shipno = '").append(form.getShipperNumber()).append("'");
                    if (!isBooking) {
                        queryBuilder.append("	    or f.house_shipper_no = '").append(form.getShipperNumber()).append("'");
                    }
                    queryBuilder.append("	)");
                }
                if (CommonUtils.isNotEmpty(form.getSalesCode())) {
                    queryBuilder.append("	 or (");
                    queryBuilder.append("	    b.consigneenumber in ( ").append(getSalesCode(form.getSalesCode())).append(")");
                    if (!isBooking) {
                        queryBuilder.append("	    or f.houseconsignee in ( ").append(getSalesCode(form.getSalesCode())).append(")");
                    }
                    queryBuilder.append("	)");
                } else if (CommonUtils.isNotEmpty(form.getConsigneeNumber())) {
                    queryBuilder.append("	 and (");
                    queryBuilder.append("	    b.consigneenumber = '").append(form.getConsigneeNumber()).append("'");
                    if (!isBooking) {
                        queryBuilder.append("	    or f.houseconsignee = '").append(form.getConsigneeNumber()).append("'");
                    }
                    queryBuilder.append("	)");
                }
                if (CommonUtils.isNotEmpty(form.getSalesCode())) {
                    queryBuilder.append("	or (");
                    queryBuilder.append("	    b.forwardnumber in ( ").append(getSalesCode(form.getSalesCode())).append(")");
                    if (!isBooking) {
                        queryBuilder.append("	    or f.forward_agent_no in ( ").append(getSalesCode(form.getSalesCode())).append(")");
                    }
                    queryBuilder.append("	) ) ");
                } else if (CommonUtils.isNotEmpty(form.getForwarderNumber())) {
                    queryBuilder.append("	and (");
                    queryBuilder.append("	    b.forwardnumber = '").append(form.getForwarderNumber()).append("'");
                    if (!isBooking) {
                        queryBuilder.append("	    or f.forward_agent_no = '").append(form.getForwarderNumber()).append("'");
                    }
                    queryBuilder.append("	)");
                }
                if (CommonUtils.isNotEmpty(form.getSslBookingNumber())) {
                    String sslBookingNumber = form.getSslBookingNumber().replace("'", "\\'");
                    queryBuilder.append("	and (");
                    queryBuilder.append("	    b.ssbookingno = '").append(sslBookingNumber).append("'");
                    if (!isBooking) {
                        queryBuilder.append("	    or f.bookingno = '").append(sslBookingNumber).append("'");
                    }
                    queryBuilder.append("	)");
                }
                if (CommonUtils.isNotEmpty(form.getBookedBy())) {
                    String bookedBy = form.getBookedBy().replace("'", "\\'");
                    queryBuilder.append("	and b.username = '").append(bookedBy).append("'");
                }
            }
            if (CommonUtils.isAllNotEmpty(form.getSslName(), form.getSslNumber())) {
                String sslname = form.getSslName().replace("'", "\\'") + "//" + form.getSslNumber();
                queryBuilder.append("	and (");
                queryBuilder.append("	    q.ssline = '").append(form.getSslNumber()).append("'");
                if (!isQuote) {
                    queryBuilder.append("   or b.sslname = '").append(sslname).append("'");
                }
                if (!isBooking) {
                    queryBuilder.append("   or f.ssline_no = '").append(form.getSslNumber()).append("'");
                }
                queryBuilder.append("	)");
            }
            if (CommonUtils.isNotEmpty(form.getIssuingTerminal())) {
                String issuingTerminal = form.getIssuingTerminal().replace("'", "\\'");
                queryBuilder.append("	and (");
                queryBuilder.append("	    q.issuing_terminal = '").append(issuingTerminal).append("'");
                if (!isQuote) {
                    queryBuilder.append("   or b.issuing_terminal = '").append(issuingTerminal).append("'");
                }
                if (!isBooking) {
                    queryBuilder.append("   or f.billing_terminal = '").append(issuingTerminal).append("'");
                }
                queryBuilder.append("	)");
            }
            if (CommonUtils.isNotEmpty(form.getCreatedBy())) {
                String createdBy = form.getCreatedBy().replace("'", "\\'");
                queryBuilder.append("	and (");
                queryBuilder.append("	    q.quote_by = '").append(createdBy).append("'");
                if (!isQuote) {
                    queryBuilder.append("   or (");
                    queryBuilder.append("	q.quote_by is null");
                    queryBuilder.append("	and  b.booked_by = '").append(createdBy).append("'");
                    queryBuilder.append("   )");
                }
                queryBuilder.append("	)");
            }
            if (!isBl && CommonUtils.notIn(form.getFilterBy(), "Booking", "Doc's Not Received")
                    && CommonUtils.isAllNotEmpty(form.getFromDate(), form.getToDate())) {
                String fromDate = DateUtils.formatDate(DateUtils.parseDate(form.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
                String toDate = DateUtils.formatDate(DateUtils.parseDate(form.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
                queryBuilder.append("	and date(q.quote_date) between '").append(fromDate).append("' and '").append(toDate).append("'");
            }

            if (form.isOlySpotRate() && form.getFilterBy().equals("")) {
                queryBuilder.append(" and if(b.file_no is not null,b.spot_rate='Y',q.spot_rate='Y') ");
            }
        }
        queryBuilder.append("   group by file_no ");
        queryBuilder.append("   order by");
        if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "Container Cut off")) {
            queryBuilder.append(" portCutOff desc");
        } else if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "Doc Cut off")) {
            queryBuilder.append(" docCutOff asc");
        } else if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "ETD")) {
            queryBuilder.append(" etd desc");
        } else {
            queryBuilder.append(" q.file_no desc ");
        }
        queryBuilder.append("   limit ").append(form.getLimit());
        return queryBuilder.toString();
    }

    private String buildImportQuoteSelectExpression() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("if(");
        queryBuilder.append("    f.file_no is not null,");
        queryBuilder.append("    f.file_no,");
        queryBuilder.append("    if(");
        queryBuilder.append("      b.file_no is not null,");
        queryBuilder.append("      b.file_no,");
        queryBuilder.append("      q.file_no");
        queryBuilder.append("    )");
        queryBuilder.append("  ) as file_no,");
        queryBuilder.append("if(f.bol is not null,'BL',");
        queryBuilder.append("	if(b.bookingid is not null,'Booking','Quote')");
        queryBuilder.append(") as file_type,");
        queryBuilder.append("q.quote_id as quote_id,");
        queryBuilder.append("b.bookingid as booking_id,");
        queryBuilder.append("f.bol as bl_id,");
        queryBuilder.append("if((f.rates_non_rates = 'R' or b.rates_non_rates = 'R' or q.rates_non_rates = 'R'),true,false) as rated,");
//        queryBuilder.append("IF(q.multi_quote_flag IN('M','C'),TRUE,FALSE) AS multiQuote,");// import
        queryBuilder.append("if(b.bookingcomplete = '").append(YES).append("',true,false) as booking_complete,");
        queryBuilder.append("if(f.ready_to_post = 'M', true, false) as manifested,");
        queryBuilder.append("if(f.bl_closed = '").append(YES).append("', true, false) as closed,");
        queryBuilder.append("if(f.bl_audited = '").append(YES).append("', true, false) as audited,");
        queryBuilder.append("if(f.sail_date is not null,f.sail_date,b.etd) as etd,");
        queryBuilder.append("f.bol_date as bl_date,");
        queryBuilder.append("if(f.void = '").append(YES).append("', true, false) as voided,");
        queryBuilder.append("q.origin_terminal as origin,");
        queryBuilder.append("q.door_origin as door_origin,");
        queryBuilder.append("q.destination_port as destination,");
        queryBuilder.append("if(f.door_of_destination != '',f.door_of_destination,");
        queryBuilder.append("	if(b.door_destination != '',b.door_destination,q.door_destination)");
        queryBuilder.append(") as door_destination,");
        queryBuilder.append("if(f.port_of_loading != '',f.port_of_loading,");
        queryBuilder.append("	if(b.portoforgin != '',b.portoforgin,q.plor)");
        queryBuilder.append(") as pol,");
        queryBuilder.append("if(f.portofdischarge != '',f.portofdischarge,");
        queryBuilder.append("	if(b.portofdischarge != '',b.portofdischarge,q.finaldestination)");
        queryBuilder.append(") as pod,");
        queryBuilder.append("q.clientname as client_name,");
        queryBuilder.append("substring_index(");
        queryBuilder.append("	if(f.ssline_name != '',f.ssline_name,");
        queryBuilder.append("	    if(b.sslname != '',b.sslname,q.carrier)");
        queryBuilder.append("	),'//',1");
        queryBuilder.append(") as ssline_name,");
        queryBuilder.append("if(f.hazmat = '").append(YES).append("' or b.hazmat = '").append(YES).append("' or q.hazmat = '").append(YES).append("',true,false) as hazmat,");
        queryBuilder.append("datediff(b.doc_cut_off, current_date()) as doc_cut_off,");
        queryBuilder.append("datediff(b.portcutoff, current_date()) as container_cut_off,");
        queryBuilder.append("b.doc_cut_off as docCutOff,");
        queryBuilder.append("b.portcutoff as portCutOff,");
        queryBuilder.append("f.break_bulk as break_bulk,");
        queryBuilder.append("b.container_size as container_size,");
        queryBuilder.append("f.inttra as inttra,");
        queryBuilder.append("f.ready_to_edi as ready_to_edi,");
        queryBuilder.append("f.received_master as received_master,");
        queryBuilder.append("if(f.payment_release != '',");
        queryBuilder.append("  concat(f.payment_release,if(f.import_release != '', f.import_release, '").append(NO).append("')),");
        queryBuilder.append("  if(f.import_release != '',concat('").append(NO).append("', f.import_release),'')");
        queryBuilder.append(") as release_type,");
        queryBuilder.append("if(f.bookingno != '',f.bookingno,b.ssbookingno) as ssl_booking_no,");
        queryBuilder.append("q.quote_date as start_date,");
        queryBuilder.append("if(f.bol is not null,f.sail_date,if(b.bookingid is not null,b.etd,'')) as sail_date,");
        queryBuilder.append("if(f.billing_terminal != '',f.billing_terminal,");
        queryBuilder.append("	if(b.issuing_terminal != '',b.issuing_terminal,q.issuing_terminal)");
        queryBuilder.append(") as issuing_terminal,");
        queryBuilder.append("if(f.eta != '',f.eta,b.eta) as eta, f.eta AS eta_date,");
        queryBuilder.append("upper(if(q.quote_by != '',q.quote_by,b.username)) as created_by,");
        queryBuilder.append("upper(b.username) as booked_by ");
        return queryBuilder.toString();
    }

    private String buildImportBookingSelectExpression() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("b.file_no as file_no,");
        queryBuilder.append("if(f.bol is not null,'BL','Booking') as file_type,");
        queryBuilder.append("null as quote_id,");
        queryBuilder.append("b.bookingid as booking_id,");
        queryBuilder.append("f.bol as bl_id,");
        queryBuilder.append("if((f.rates_non_rates = 'R' or b.rates_non_rates = 'R'),true,false) as rated,");
        queryBuilder.append("if(b.bookingcomplete = '").append(YES).append("',true,false) as booking_complete,");
        queryBuilder.append("if(f.ready_to_post = 'M', true, false) as manifested,");
        queryBuilder.append("if(f.bl_closed = '").append(YES).append("', true, false) as closed,");
        queryBuilder.append("if(f.bl_audited = '").append(YES).append("', true, false) as audited,");
        queryBuilder.append("if(f.sail_date is not null,f.sail_date,b.etd) as etd,");
        queryBuilder.append("f.bol_date as bl_date,");
        queryBuilder.append("if(f.void = '").append(YES).append("', true, false) as voided,");
        queryBuilder.append("b.originterminal as origin,");
        queryBuilder.append("b.door_origin as door_origin,");
        queryBuilder.append("b.destination as destination,");
        queryBuilder.append("if(f.door_of_destination != '',f.door_of_destination,b.door_destination) as door_destination,");
        queryBuilder.append("if(f.port_of_loading != '',f.port_of_loading,b.portoforgin) as pol,");
        queryBuilder.append("if(f.portofdischarge != '',f.portofdischarge,b.portofdischarge) as pod,");
        queryBuilder.append("null as client_name,");
        queryBuilder.append("substring_index(if(f.ssline_name != '',f.ssline_name,b.sslname),'//',1) as ssline_name,");
        queryBuilder.append("if(f.hazmat = '").append(YES).append("' or b.hazmat = '").append(YES).append("',true,false) as hazmat,");
        queryBuilder.append("datediff(b.doc_cut_off, current_date()) as doc_cut_off,");
        queryBuilder.append("datediff(b.portcutoff, current_date()) as container_cut_off,");
        queryBuilder.append("b.doc_cut_off as docCutOff,");
        queryBuilder.append("b.portcutoff as portCutOff,");
        queryBuilder.append("f.break_bulk as break_bulk,");
        queryBuilder.append("b.container_size as container_size,");
        queryBuilder.append("f.inttra as inttra,");
        queryBuilder.append("f.ready_to_edi as ready_to_edi,");
        queryBuilder.append("f.received_master as received_master,");
        queryBuilder.append("if(f.payment_release != '',");
        queryBuilder.append("  concat(f.payment_release,if(f.import_release != '', f.import_release, '").append(NO).append("')),");
        queryBuilder.append("  if(f.import_release != '',concat('").append(NO).append("', f.import_release),'')");
        queryBuilder.append(") as release_type,");
        queryBuilder.append("if(f.bookingno != '',f.bookingno,b.ssbookingno) as ssl_booking_no,");
        queryBuilder.append("b.bookingdate as start_date,");
        queryBuilder.append("if(f.bol is not null,f.sail_date,if(b.bookingid is not null,b.etd,'')) as sail_date,");
        queryBuilder.append("if(f.billing_terminal != '',f.billing_terminal,b.issuing_terminal) as issuing_terminal,");
        queryBuilder.append("if(f.eta != '',f.eta,b.eta) as eta, f.eta AS eta_date,");
        queryBuilder.append("upper(b.username) as created_by,");
        queryBuilder.append("upper(b.username) as booked_by");
        return queryBuilder.toString();
    }

    private String buildImportBlSelectExpression() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("f.file_no as file_no,");
        queryBuilder.append("'BL' as file_type,");
        queryBuilder.append("null as quote_id,");
        queryBuilder.append("null as booking_id,");
        queryBuilder.append("f.bol as bl_id,");
        queryBuilder.append("if(f.rates_non_rates = 'R',true,false) as rated,");
        queryBuilder.append("false as booking_complete,");
        queryBuilder.append("if(f.ready_to_post = 'M', true, false) as manifested,");
        queryBuilder.append("if(f.bl_closed = '").append(YES).append("', true, false) as closed,");
        queryBuilder.append("if(f.bl_audited = '").append(YES).append("', true, false) as audited,");
        queryBuilder.append("if(f.sail_date is not null,f.sail_date,b.etd) as etd,");
        queryBuilder.append("f.bol_date as bl_date,");
        queryBuilder.append("if(f.void = '").append(YES).append("', true, false) as voided,");
        queryBuilder.append("f.original_terminal as origin,");
        queryBuilder.append("f.door_of_origin as door_origin,");
        queryBuilder.append("f.port as destination,");
        queryBuilder.append("f.door_of_destination as door_destination,");
        queryBuilder.append("f.port_of_loading as pol,");
        queryBuilder.append("f.portofdischarge as pod,");
        queryBuilder.append("null as client_name,");
        queryBuilder.append("substring_index(f.ssline_name,'//',1) as ssline_name,");
        queryBuilder.append("if(f.hazmat = '").append(YES).append("',true,false) as hazmat,");
        queryBuilder.append("datediff(b.doc_cut_off, current_date()) as doc_cut_off,");
        queryBuilder.append("datediff(b.portcutoff, current_date()) as container_cut_off,");
        queryBuilder.append("b.doc_cut_off as docCutOff,");
        queryBuilder.append("b.portcutoff as portCutOff,");
        queryBuilder.append("f.break_bulk as break_bulk,");
        queryBuilder.append("null as container_size,");
        queryBuilder.append("f.inttra as inttra,");
        queryBuilder.append("f.ready_to_edi as ready_to_edi,");
        queryBuilder.append("f.received_master as received_master,");
        queryBuilder.append("if(f.payment_release != '',");
        queryBuilder.append("  concat(f.payment_release,if(f.import_release != '', f.import_release, '").append(NO).append("')),");
        queryBuilder.append("  if(f.import_release != '',concat('").append(NO).append("', f.import_release),'')");
        queryBuilder.append(") as release_type,");
        queryBuilder.append("f.bookingno as ssl_booking_no,");
        queryBuilder.append("f.bol_date as start_date,");
        queryBuilder.append("if(f.bol is not null,f.sail_date,if(b.bookingid is not null,b.etd,'')) as sail_date,");
        queryBuilder.append("f.billing_terminal as issuing_terminal,");
        queryBuilder.append("if(f.eta != '',f.eta,b.eta) as eta, f.eta AS eta_date,");
        queryBuilder.append("upper(f.bl_by) as created_by,");
        queryBuilder.append("null as booked_by ");
        return queryBuilder.toString();
    }

    private String buildImportQuoteWhereCondition(SearchForm form) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" from");
        queryBuilder.append("	quotation q");
        queryBuilder.append("   left join booking_fcl b ");
        queryBuilder.append("	    on (");
        queryBuilder.append("		q.file_no = b.file_no");
        queryBuilder.append("		and b.importflag = 'I'");
        queryBuilder.append("	    )");
        queryBuilder.append("   left join fcl_bl f ");
        queryBuilder.append("	    on (");
        queryBuilder.append("		q.file_no = f.file_number");
        queryBuilder.append("		and f.importflag = 'I'");
        queryBuilder.append("	    )");
        if (CommonUtils.isNotEmpty(form.getFileNumber())) {
//            String fileNumber = form.getFileNumber().replaceAll("[^\\p{Digit}]+", "");
            queryBuilder.append("   where q.file_type = 'I'");
            queryBuilder.append("	and q.file_no = '").append(form.getFileNumber()).append("'");
        } else if (form.isOlySpotRate()) {
        } else {
            List<String> blFilters = new ArrayList<String>();
            blFilters.add("BL");
            blFilters.add("Un Manifested");
            blFilters.add("Manifested");
            blFilters.add("Manifested No Master");
            blFilters.add("Master Not Received");
            blFilters.add("FAE Not Applied");
            blFilters.add("Imp Release");
            blFilters.add("No Release");
            blFilters.add("Doc Release");
            blFilters.add("Pmt Release");
            blFilters.add("Over Paid");
            blFilters.add("Closed");
            blFilters.add("Audited");
            blFilters.add("Voided");
            List<String> blFields = new ArrayList<String>();
            blFields.add(form.getContainerNumber());
            blFields.add(form.getInboundNumber());
            blFields.add(form.getMasterBl());
            blFields.add(form.getVessel());
            blFields.add(form.getAms());
            blFields.add(form.getSubHouseBl());
            blFields.add(form.getMasterShipperNumber());
            boolean isQuote = CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Quotation");
            boolean isBooking = CommonUtils.in(form.getFilterBy(), "Booking", "Doc's Not Received");
            boolean isBl = false;
            if (!isQuote && !isBooking
                    && (form.isSailingDate()
                    || CommonUtils.in(form.getFilterBy(), (String[]) blFilters.toArray(new String[blFilters.size()]))
                    || CommonUtils.isAtLeastOneNotEmpty((String[]) blFields.toArray(new String[blFields.size()])))) {
                isBl = true;
            }
            if (!isBooking && !isBl && CommonUtils.in(form.getSortByDate(), "Container Cut off", "Doc Cut off")) {
                isBooking = true;
            }
            if (isBl) {
                if (CommonUtils.isNotEmpty(form.getContainerNumber())) {
                    String containerNumber = form.getContainerNumber().replace("'", "\\'");
                    queryBuilder.append("   join fcl_bl_container_dtls c");
                    queryBuilder.append("	on (");
                    queryBuilder.append("	    f.bol = c.bolid");
                    queryBuilder.append("	    and c.trailer_no like '%").append(containerNumber).append("%'");
                    queryBuilder.append("	)");
                }
                if (CommonUtils.isNotEmpty(form.getInboundNumber())) {
                    String inboundNumber = form.getInboundNumber().replace("'", "\\'");
                    queryBuilder.append("   join fcl_inbond_details i");
                    queryBuilder.append("	on (");
                    queryBuilder.append("	    f.bol = i.bol_id");
                    queryBuilder.append("	    and i.inbond_number like '%").append(inboundNumber).append("%'");
                    queryBuilder.append("	)");
                }
                if (CommonUtils.isNotEmpty(form.getAesItn())) {
                    String aesItn = form.getAesItn().replace("'", "\\'");
                    queryBuilder.append("   join fcl_aes_details a");
                    queryBuilder.append("	on (");
                    queryBuilder.append("	    f.file_no = a.file_no");
                    queryBuilder.append("	    and a.aes_details like '%").append(aesItn).append("%'");
                    queryBuilder.append("	)");
                }
                if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "FAE Not Applied")) {
                    queryBuilder.append("   left join fcl_bl_costcodes co");
                    queryBuilder.append("	on (");
                    queryBuilder.append("		f.bol = co.bolid");
                    queryBuilder.append("	    and	co.cost_code = 'FAECOMM'");
                    queryBuilder.append("	    and co.delete_flag = 'no'");
                    queryBuilder.append("	)");
                }
            }
            queryBuilder.append("   where q.file_no is not null");
            queryBuilder.append("	and q.file_type = 'I'");
            if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Online Bookings")) {
                queryBuilder.append("	and q.file_no like 'R%'");
            }
            if (isQuote) {
                queryBuilder.append("   and b.bookingid is null");
                queryBuilder.append("   and f.bol is null");
            } else if (isBooking) {
                queryBuilder.append("   and f.bol is null");
            } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "FAE Not Applied")) {
                queryBuilder.append("   and co.bolid is null");
            }
            if (CommonUtils.isNotEmpty(form.getOriginRegion())) {
                String origins = getPorts(form.getOriginRegion());
                queryBuilder.append("   and q.origin_terminal");
                if (CommonUtils.isNotEmpty(origins)) {
                    queryBuilder.append(" in (").append(origins).append(")");
                } else {
                    queryBuilder.append(" is null");
                }
            } else if (CommonUtils.isNotEmpty(form.getOrigin())) {
                String origin = form.getOrigin().replace("'", "\\'");
                queryBuilder.append("	and q.origin_terminal = '").append(origin).append("'");
            }
            if (CommonUtils.isNotEmpty(form.getDestinationRegion())) {
                String destinations = getPorts(form.getDestinationRegion());
                queryBuilder.append("	and q.destination_port");
                if (CommonUtils.isNotEmpty(destinations)) {
                    queryBuilder.append(" in (").append(destinations).append(")");
                } else {
                    queryBuilder.append(" is null");
                }
            } else if (CommonUtils.isNotEmpty(form.getDestination())) {
                String destination = form.getDestination().replace("'", "\\'");
                queryBuilder.append("	and q.destination_port = '").append(destination).append("'");
            }
            if (CommonUtils.isNotEmpty(form.getPol())) {
                String pol = form.getPol().replace("'", "\\'");
                queryBuilder.append("	and q.plor = '").append(pol).append("'");
            }
            if (CommonUtils.isNotEmpty(form.getPod())) {
                String pod = form.getPod().replace("'", "\\'");
                queryBuilder.append("	and q.finaldestination = '").append(pod).append("'");
            } else if (CommonUtils.isNotEmpty(form.getMasterShipperNumber())) {
                String masterShipperNo = form.getMasterShipperNumber().replace("'", "\\'");
                queryBuilder.append("	and f.Shipper_No ='").append(masterShipperNo).append("'");
            }
            if (isBooking) {
                if (CommonUtils.in(form.getFilterBy(), "Booking", "Doc's Not Received")
                        && CommonUtils.isAllNotEmpty(form.getFromDate(), form.getToDate())) {
                    String fromDate = DateUtils.formatDate(DateUtils.parseDate(form.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
                    String toDate = DateUtils.formatDate(DateUtils.parseDate(form.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
                    queryBuilder.append("	and date(b.bookingdate) between '").append(fromDate).append("' and '").append(toDate).append("'");
                }
                if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Doc's Not Received")) {
                    queryBuilder.append("	and b.file_no is not null");
                    queryBuilder.append("	and (");
                    queryBuilder.append("	    b.documents_received = '").append(NO).append("'");
                    queryBuilder.append("	    or b.doc_cut_off is null");
                    queryBuilder.append("	)");
                }
            }
            if (isBl) {
                queryBuilder.append("	and f.bol is not null");
                if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Un Manifested")) {
                    queryBuilder.append("	and f.manifested_date is null and f.file_number is not null");
                } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Manifested")) {
                    queryBuilder.append("	and f.manifested_date is not null");
                    queryBuilder.append("	and f.ready_to_post = 'M'");
                } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Manifested No Master")) {
                    queryBuilder.append("	and f.manifested_date is not null");
                    queryBuilder.append("	and f.ready_to_post = 'M'");
                    String fileNumbers = getNoMasterFiles(form);
                    if (CommonUtils.isNotEmpty(fileNumbers)) {
                        queryBuilder.append("	and substring_index(f.file_no, '-', 1) in (").append(fileNumbers).append(")");
                    } else {
                        queryBuilder.append("	and f.file_no is null");
                    }
                } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Master Not Received")) {
                    String fileNumbers = getNoMasterFiles(form);
                    if (CommonUtils.isNotEmpty(fileNumbers)) {
                        queryBuilder.append("	and substring_index(f.file_no, '-', 1) in (").append(fileNumbers).append(")");
                    } else {
                        queryBuilder.append("	and f.file_no is null");
                    }
                } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Imp Release")) {
                    queryBuilder.append("	and f.import_release = '").append(YES).append("'");
                    queryBuilder.append("	and f.payment_release = '").append(YES).append("'");
                } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "No Release")) {
                    queryBuilder.append("	and (f.import_release = '").append(NO).append("' or f.import_release is null)");
                    queryBuilder.append("	and (f.payment_release = '").append(NO).append("' or f.payment_release is null)");
                } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Doc Release")) {
                    queryBuilder.append("	and f.import_release = '").append(YES).append("'");
                    queryBuilder.append("	and (f.payment_release = '").append(NO).append("' or f.payment_release is null)");
                } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Pmt Release")) {
                    queryBuilder.append("	and (f.import_release = '").append(NO).append("' or f.import_release is null)");
                    queryBuilder.append("	and f.payment_release = '").append(YES).append("'");
                } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Over Paid")) {
                    queryBuilder.append("	and f.over_paid_status = '1'");
                } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Closed")) {
                    queryBuilder.append("	and f.bl_closed = '").append(YES).append("'");
                } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Audited")) {
                    queryBuilder.append("	and f.bl_audited = '").append(YES).append("'");
                } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Voided")) {
                    queryBuilder.append("	and f.void is not null");
                } 
                if (CommonUtils.isNotEmpty(form.getMasterBl())) {
                    String masterBl = form.getMasterBl().replace("'", "\\'");
                    queryBuilder.append("	and f.new_master_bl like '%").append(masterBl).append("%'");
                }
                if (CommonUtils.isNotEmpty(form.getVessel())) {
                    queryBuilder.append("  and f.vessel like '%").append(getVesselIdFromDesc(form.getVessel())).append("%'");
                }
                if (CommonUtils.isNotEmpty(form.getAms())) {
                    String ams = form.getAms().replace("'", "\\'");
                    queryBuilder.append("	and f.import_ams_house_bl like '").append(ams).append("%'");
                }
                if (CommonUtils.isNotEmpty(form.getSubHouseBl())) {
                    String subHouseBl = form.getSubHouseBl().replace("'", "\\'");
                    queryBuilder.append("	and f.import_orgin_blno like '").append(subHouseBl).append("%'");
                }
                if (CommonUtils.isAllNotEmpty(form.getFromDate(), form.getToDate())) {
                    String fromDate = DateUtils.formatDate(DateUtils.parseDate(form.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
                    String toDate = DateUtils.formatDate(DateUtils.parseDate(form.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
                    if (CommonUtils.in(form.getFilterBy(), "Manifested", "Manifested No Master")) {
                        queryBuilder.append("	and date(f.manifested_date) between '").append(fromDate).append("' and '").append(toDate).append("'");
                    } else if (form.isSailingDate()) {
                        queryBuilder.append("	and date(f.sail_date) between '").append(fromDate).append("' and '").append(toDate).append("'");
                    } else {
                        queryBuilder.append("   and date(f.bol_date) between '").append(fromDate).append("' and '").append(toDate).append("'");
                    }
                }
            }
            if (CommonUtils.isNotEmpty(form.getSalesCode())) {
                queryBuilder.append("	and ( q.clientnumber in (").append(getSalesCode(form.getSalesCode())).append(")");
            } else if (form.isDisableClient() && CommonUtils.isNotEmpty(form.getManualClientName())) {
                String clientName = form.getManualClientName().replace("'", "\\'");
                queryBuilder.append("	and q.clientname like '%").append(clientName).append("%'");
            } else if (CommonUtils.isNotEmpty(form.getClientNumber())) {
                queryBuilder.append("	and q.clientnumber = '").append(form.getClientNumber()).append("'");
            }
            if (!isQuote) {
                if (CommonUtils.isNotEmpty(form.getSalesCode())) {
                    queryBuilder.append("	 or (");
                    queryBuilder.append("	    b.shipno in ( ").append(getSalesCode(form.getSalesCode())).append(")");
                    if (!isBooking) {
                        queryBuilder.append("	    or f.house_shipper_no in (").append(getSalesCode(form.getSalesCode())).append(")");
                    }
                    queryBuilder.append("	)");
                } else if (CommonUtils.isNotEmpty(form.getShipperNumber())) {
                    queryBuilder.append("	 and (");
                    queryBuilder.append("	    b.shipno = '").append(form.getShipperNumber()).append("'");
                    if (!isBooking) {
                        queryBuilder.append("	    or f.house_shipper_no = '").append(form.getShipperNumber()).append("'");
                    }
                    queryBuilder.append("	)");
                }
                if (CommonUtils.isNotEmpty(form.getSalesCode())) {
                    queryBuilder.append("	 or (");
                    queryBuilder.append("	    b.consigneenumber in (").append(getSalesCode(form.getSalesCode())).append(")");
                    if (!isBooking) {
                        queryBuilder.append("	    or f.houseconsignee in (").append(getSalesCode(form.getSalesCode())).append(")");
                    }
                    queryBuilder.append("	)");
                } else if (CommonUtils.isNotEmpty(form.getConsigneeNumber())) {
                    queryBuilder.append("	 and (");
                    queryBuilder.append("	    b.consigneenumber = '").append(form.getConsigneeNumber()).append("'");
                    if (!isBooking) {
                        queryBuilder.append("	    or f.houseconsignee = '").append(form.getConsigneeNumber()).append("'");
                    }
                    queryBuilder.append("	)");
                }
                if (CommonUtils.isNotEmpty(form.getSalesCode())) {
                    queryBuilder.append("	or (");
                    queryBuilder.append("	    b.forwardnumber in ( ").append(getSalesCode(form.getSalesCode())).append(")");
                    if (!isBooking) {
                        queryBuilder.append("	    or f.forward_agent_no in ( ").append(getSalesCode(form.getSalesCode())).append(")");
                    }
                    queryBuilder.append("	) ) ");
                } else if (CommonUtils.isNotEmpty(form.getForwarderNumber())) {
                    queryBuilder.append("	and (");
                    queryBuilder.append("	    b.forwardnumber = '").append(form.getForwarderNumber()).append("'");
                    if (!isBooking) {
                        queryBuilder.append("	    or f.forward_agent_no = '").append(form.getForwarderNumber()).append("'");
                    }
                    queryBuilder.append("	)");
                }
                if (CommonUtils.isNotEmpty(form.getSslBookingNumber())) {
                    String sslBookingNumber = form.getSslBookingNumber().replace("'", "\\'");
                    queryBuilder.append("	and (");
                    queryBuilder.append("	    b.ssbookingno = '").append(sslBookingNumber).append("'");
                    if (!isBooking) {
                        queryBuilder.append("	    or f.bookingno = '").append(sslBookingNumber).append("'");
                    }
                    queryBuilder.append("	)");
                }
                if (CommonUtils.isNotEmpty(form.getBookedBy())) {
                    String bookedBy = form.getBookedBy().replace("'", "\\'");
                    queryBuilder.append("	and b.username = '").append(bookedBy).append("'");
                }
            }
            if (CommonUtils.isAllNotEmpty(form.getSslName(), form.getSslNumber())) {
                String sslname = form.getSslName().replace("'", "\\'") + "//" + form.getSslNumber();
                queryBuilder.append("	and (");
                queryBuilder.append("	    q.ssline = '").append(form.getSslNumber()).append("'");
                if (!isQuote) {
                    queryBuilder.append("   or b.sslname = '").append(sslname).append("'");
                }
                if (!isBooking) {
                    queryBuilder.append("   or f.ssline_no = '").append(form.getSslNumber()).append("'");
                }
                queryBuilder.append("	)");
            }
            if (CommonUtils.isNotEmpty(form.getIssuingTerminal())) {
                String issuingTerminal = form.getIssuingTerminal().replace("'", "\\'");
                queryBuilder.append("	and (");
                queryBuilder.append("	    q.issuing_terminal = '").append(issuingTerminal).append("'");
                if (!isQuote) {
                    queryBuilder.append("   or b.issuing_terminal = '").append(issuingTerminal).append("'");
                }
                if (!isBooking) {
                    queryBuilder.append("   or f.billing_terminal = '").append(issuingTerminal).append("'");
                }
                queryBuilder.append("	)");
            }
            if (CommonUtils.isNotEmpty(form.getCreatedBy())) {
                String createdBy = form.getCreatedBy().replace("'", "\\'");
                queryBuilder.append("	and (");
                queryBuilder.append("	    q.quote_by = '").append(createdBy).append("'");
                if (!isQuote) {
                    queryBuilder.append("   or (");
                    queryBuilder.append("	q.quote_by is null");
                    queryBuilder.append("	and  b.booked_by = '").append(createdBy).append("'");
                    queryBuilder.append("   )");
                }
                queryBuilder.append("	)");
            }
            if (!isBl && CommonUtils.notIn(form.getFilterBy(), "Booking", "Doc's Not Received")
                    && CommonUtils.isAllNotEmpty(form.getFromDate(), form.getToDate())) {
                String fromDate = DateUtils.formatDate(DateUtils.parseDate(form.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
                String toDate = DateUtils.formatDate(DateUtils.parseDate(form.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
                queryBuilder.append("	and date(q.quote_date) between '").append(fromDate).append("' and '").append(toDate).append("'");
            }
        }
        if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "Container Cut off")) {
            queryBuilder.append("	and b.portcutoff >= current_date()");
        } else if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "Doc Cut off")) {
            queryBuilder.append("	and b.doc_cut_off >= current_date()");
        } else if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "ETD")) {
            queryBuilder.append("	and if(isnull(f.sail_date),b.etd,f.sail_date) >= current_date()");
        } else if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "ETA")) {
            queryBuilder.append("	and if(isnull(f.eta),b.eta,f.eta) >= current_date()");
        }
        queryBuilder.append("   group by file_no");
        queryBuilder.append("   order by");
        if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "Container Cut off")) {
            queryBuilder.append(" portCutOff desc");
        } else if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "Doc Cut off")) {
            queryBuilder.append(" docCutOff asc");
        } else if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "ETD")) {
            queryBuilder.append(" etd desc");
        } else if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "ETA")) {
            queryBuilder.append(" eta desc");
        } else {
            queryBuilder.append(" q.file_no desc");
        }
        queryBuilder.append("   limit ").append(form.getLimit());
        return queryBuilder.toString();
    }

    private String buildImportBookingWhereCondition(SearchForm form) throws Exception {
        if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Quotation")
                || form.isDisableClient() && CommonUtils.isNotEmpty(form.getManualClientName())
                || CommonUtils.isNotEmpty(form.getClientNumber())) {
            return null;
        } else {
            List<String> blFilters = new ArrayList<String>();
            blFilters.add("BL");
            blFilters.add("Un Manifested");
            blFilters.add("Manifested");
            blFilters.add("Manifested No Master");
            blFilters.add("Master Not Received");
            blFilters.add("FAE Not Applied");
            blFilters.add("Imp Release");
            blFilters.add("No Release");
            blFilters.add("Doc Release");
            blFilters.add("Pmt Release");
            blFilters.add("Over Paid");
            blFilters.add("Closed");
            blFilters.add("Audited");
            blFilters.add("Voided");
            List<String> blFields = new ArrayList<String>();
            blFields.add(form.getContainerNumber());
            blFields.add(form.getInboundNumber());
            blFields.add(form.getMasterBl());
            blFields.add(form.getVessel());
            blFields.add(form.getAms());
            blFields.add(form.getSubHouseBl());
            blFields.add(form.getMasterShipperNumber());
            boolean isBooking = CommonUtils.in(form.getFilterBy(), "Booking", "Doc's Not Received");
            boolean isBl = false;
            if (!isBooking
                    && (form.isSailingDate()
                    || CommonUtils.in(form.getFilterBy(), (String[]) blFilters.toArray(new String[blFilters.size()]))
                    || CommonUtils.isAtLeastOneNotEmpty((String[]) blFields.toArray(new String[blFields.size()])))) {
                isBl = true;
            }
            if (!isBooking && !isBl && CommonUtils.in(form.getSortByDate(), "Container Cut off", "Doc Cut off")) {
                isBooking = true;
            }
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append(" from");
            queryBuilder.append("   booking_fcl b ");
            queryBuilder.append("   left join quotation q");
            queryBuilder.append("	on (");
            queryBuilder.append("	    b.file_no = q.file_no");
            queryBuilder.append("	    and q.file_type = 'I'");
            queryBuilder.append("	)");
            queryBuilder.append("   left join fcl_bl f ");
            queryBuilder.append("	on (");
            queryBuilder.append("	    b.file_no = f.file_number");
            queryBuilder.append("	    and f.importflag = 'I'");
            queryBuilder.append("	)");
            if (isBl) {
                if (CommonUtils.isNotEmpty(form.getContainerNumber())) {
                    String containerNumber = form.getContainerNumber().replace("'", "\\'");
                    queryBuilder.append("   join fcl_bl_container_dtls c");
                    queryBuilder.append("	on (");
                    queryBuilder.append("	    f.bol = c.bolid");
                    queryBuilder.append("	    and c.trailer_no like '%").append(containerNumber).append("%'");
                    queryBuilder.append("	)");
                }
                if (CommonUtils.isNotEmpty(form.getInboundNumber())) {
                    String inboundNumber = form.getInboundNumber().replace("'", "\\'");
                    queryBuilder.append("   join fcl_inbond_details i");
                    queryBuilder.append("	on (");
                    queryBuilder.append("	    f.bol = i.bol_id");
                    queryBuilder.append("	    and i.inbond_number like '%").append(inboundNumber).append("%'");
                    queryBuilder.append("	)");
                }
                if (CommonUtils.isNotEmpty(form.getAesItn())) {
                    String aesItn = form.getAesItn().replace("'", "\\'");
                    queryBuilder.append("   join fcl_aes_details a");
                    queryBuilder.append("	on (");
                    queryBuilder.append("	    f.file_no = a.file_no");
                    queryBuilder.append("	    and a.aes_details like '%").append(aesItn).append("%'");
                    queryBuilder.append("	)");
                }
                if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "FAE Not Applied")) {
                    queryBuilder.append("   left join fcl_bl_costcodes co");
                    queryBuilder.append("	on (");
                    queryBuilder.append("		f.bol = co.bolid");
                    queryBuilder.append("	    and	co.cost_code = 'FAECOMM'");
                    queryBuilder.append("	    and co.delete_flag = 'no'");
                    queryBuilder.append("	)");
                }
            }
            queryBuilder.append("   where b.importflag = 'I'");
            queryBuilder.append("	and q.quote_id is null");
            if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Online Bookings")) {
                queryBuilder.append("	and (b.file_no like 'R%') ");
            }
            if (isBooking) {
                queryBuilder.append("	and f.bol is null");
            }
            if (CommonUtils.isNotEmpty(form.getFileNumber())) {
//                String fileNumber = form.getFileNumber().replaceAll("[^\\p{Digit}]+", "");
                queryBuilder.append("	and b.file_no = '").append(form.getFileNumber()).append("'");
            } else {
                queryBuilder.append("	and b.file_no is not null");
                if (isBl) {
                    queryBuilder.append("	and f.bol is not null");
                    if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Un Manifested")) {
                        queryBuilder.append("	and f.manifested_date is null and f.file_number is not null");
                    } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Manifested")) {
                        queryBuilder.append("	and f.manifested_date is not null");
                        queryBuilder.append("	and f.ready_to_post = 'M'");
                    } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Manifested No Master")) {
                        queryBuilder.append("	and f.manifested_date is not null");
                        queryBuilder.append("	and f.ready_to_post = 'M'");
                        String fileNumbers = getNoMasterFiles(form);
                        if (CommonUtils.isNotEmpty(fileNumbers)) {
                            queryBuilder.append("	and substring_index(f.file_no, '-', 1) in (").append(fileNumbers).append(")");
                        } else {
                            queryBuilder.append("	and f.file_no is null");
                        }
                    } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Master Not Received")) {
                        String fileNumbers = getNoMasterFiles(form);
                        if (CommonUtils.isNotEmpty(fileNumbers)) {
                            queryBuilder.append("	and substring_index(f.file_no, '-', 1) in (").append(fileNumbers).append(")");
                        } else {
                            queryBuilder.append("	and f.file_no is null");
                        }
                    } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Imp Release")) {
                        queryBuilder.append("	and f.import_release = '").append(YES).append("'");
                        queryBuilder.append("	and f.payment_release = '").append(YES).append("'");
                    } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "No Release")) {
                        queryBuilder.append("	and (f.import_release = '").append(NO).append("' or f.import_release is null)");
                        queryBuilder.append("	and (f.payment_release = '").append(NO).append("' or f.payment_release is null)");
                    } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Doc Release")) {
                        queryBuilder.append("	and f.import_release = '").append(YES).append("'");
                        queryBuilder.append("	and (f.payment_release = '").append(NO).append("' or f.payment_release is null)");
                    } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Pmt Release")) {
                        queryBuilder.append("	and (f.import_release = '").append(NO).append("' or f.import_release is null)");
                        queryBuilder.append("	and f.payment_release = '").append(YES).append("'");
                    } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Over Paid")) {
                        queryBuilder.append("	and f.over_paid_status = '1'");
                    } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Closed")) {
                        queryBuilder.append("	and f.bl_closed = '").append(YES).append("'");
                    } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Audited")) {
                        queryBuilder.append("	and f.bl_audited = '").append(YES).append("'");
                    } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Voided")) {
                        queryBuilder.append("	and f.void is not null");
                    }
                    if (CommonUtils.isNotEmpty(form.getMasterBl())) {
                        String masterBl = form.getMasterBl().replace("'", "\\'");
                        queryBuilder.append("	and f.new_master_bl like '%").append(masterBl).append("%'");
                    }
                    if (CommonUtils.isNotEmpty(form.getVessel())) {
                        queryBuilder.append("  and f.vessel like '%").append(getVesselIdFromDesc(form.getVessel())).append("%'");
                    }
                    if (CommonUtils.isNotEmpty(form.getAms())) {
                        String ams = form.getAms().replace("'", "\\'");
                        queryBuilder.append("	and f.import_ams_house_bl like '").append(ams).append("%'");
                    }
                    if (CommonUtils.isNotEmpty(form.getSubHouseBl())) {
                        String subHouseBl = form.getSubHouseBl().replace("'", "\\'");
                        queryBuilder.append("	and f.import_orgin_blno like '").append(subHouseBl).append("%'");
                    }
                    if (CommonUtils.isAllNotEmpty(form.getFromDate(), form.getToDate())) {
                        String fromDate = DateUtils.formatDate(DateUtils.parseDate(form.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
                        String toDate = DateUtils.formatDate(DateUtils.parseDate(form.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
                        if (CommonUtils.in(form.getFilterBy(), "Manifested", "Manifested No Master")) {
                            queryBuilder.append("	and date(f.manifested_date) between '").append(fromDate).append("' and '").append(toDate).append("'");
                        } else if (form.isSailingDate()) {
                            queryBuilder.append("	and date(f.sail_date) between '").append(fromDate).append("' and '").append(toDate).append("'");
                        } else {
                            queryBuilder.append("   and date(f.bol_date) between '").append(fromDate).append("' and '").append(toDate).append("'");
                        }
                    }
                } else if (CommonUtils.isAllNotEmpty(form.getFromDate(), form.getToDate())) {
                    String fromDate = DateUtils.formatDate(DateUtils.parseDate(form.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
                    String toDate = DateUtils.formatDate(DateUtils.parseDate(form.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
                    queryBuilder.append("   and date(b.bookingdate) between '").append(fromDate).append("' and '").append(toDate).append("'");
                }
                if (CommonUtils.isNotEmpty(form.getOriginRegion())) {
                    String origins = getPorts(form.getOriginRegion());
                    if (CommonUtils.isNotEmpty(origins)) {
                        queryBuilder.append("	and b.originterminal in (").append(origins).append(")");
                    } else {
                        queryBuilder.append("	and b.originterminal is null");
                    }
                } else if (CommonUtils.isNotEmpty(form.getOrigin())) {
                    String origin = form.getOrigin().replace("'", "\\'");
                    queryBuilder.append("	and b.originterminal = '").append(origin).append("'");
                }
                if (CommonUtils.isNotEmpty(form.getDestinationRegion())) {
                    String destinations = getPorts(form.getDestinationRegion());
                    if (CommonUtils.isNotEmpty(destinations)) {
                        queryBuilder.append("	and b.destination in (").append(destinations).append(")");
                    } else {
                        queryBuilder.append("	and b.destination is null");
                    }
                } else if (CommonUtils.isNotEmpty(form.getDestination())) {
                    String destination = form.getDestination().replace("'", "\\'");
                    queryBuilder.append("	and b.destination = '").append(destination).append("'");
                }
                if (CommonUtils.isNotEmpty(form.getPol())) {
                    String pol = form.getPol().replace("'", "\\'");
                    queryBuilder.append("	and b.portoforgin = '").append(pol).append("'");
                }
                if (CommonUtils.isNotEmpty(form.getPod())) {
                    String pod = form.getPod().replace("'", "\\'");
                    queryBuilder.append("	and b.portofdischarge = '").append(pod).append("'");
                }
                if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Doc's Not Received")) {
                    queryBuilder.append("	and (");
                    queryBuilder.append("		b.documents_received = '").append(NO).append("'");
                    queryBuilder.append("	    or b.doc_cut_off is null");
                    queryBuilder.append("	)");
                }
                if (CommonUtils.isNotEmpty(form.getSalesCode())) {
                    queryBuilder.append("	and ( (");
                    queryBuilder.append("	    b.shipno in ( ").append(getSalesCode(form.getSalesCode())).append(")");
                    if (!isBooking) {
                        queryBuilder.append("	    or f.house_shipper_no in ( ").append(getSalesCode(form.getSalesCode())).append(")");
                    }
                    queryBuilder.append("	)");
                } else if (CommonUtils.isNotEmpty(form.getShipperNumber())) {
                    queryBuilder.append("	and (");
                    queryBuilder.append("	    b.shipno = '").append(form.getShipperNumber()).append("'");
                    if (!isBooking) {
                        queryBuilder.append("	    or f.house_shipper_no = '").append(form.getShipperNumber()).append("'");
                    }
                    queryBuilder.append("	)");
                }
                if (CommonUtils.isNotEmpty(form.getSalesCode())) {
                    queryBuilder.append("	 or (");
                    queryBuilder.append("	    b.consigneenumber in ( ").append(getSalesCode(form.getSalesCode())).append(")");
                    if (!isBooking) {
                        queryBuilder.append("	    or f.houseconsignee in ( ").append(getSalesCode(form.getSalesCode())).append(")");
                    }
                    queryBuilder.append("	)");
                } else if (CommonUtils.isNotEmpty(form.getConsigneeNumber())) {
                    queryBuilder.append("	 and (");
                    queryBuilder.append("	    b.consigneenumber = '").append(form.getConsigneeNumber()).append("'");
                    if (!isBooking) {
                        queryBuilder.append("	    or f.houseconsignee = '").append(form.getConsigneeNumber()).append("'");
                    }
                    queryBuilder.append("	)");
                }
                if (CommonUtils.isNotEmpty(form.getSalesCode())) {
                    queryBuilder.append("	or (");
                    queryBuilder.append("	    b.forwardnumber in ( ").append(getSalesCode(form.getSalesCode())).append(")");
                    if (!isBooking) {
                        queryBuilder.append("	    or f.forward_agent_no in ( ").append(getSalesCode(form.getSalesCode())).append(")");
                    }
                    queryBuilder.append("	) )");
                } else if (CommonUtils.isNotEmpty(form.getForwarderNumber())) {
                    queryBuilder.append("	and (");
                    queryBuilder.append("	    b.forwardnumber = '").append(form.getForwarderNumber()).append("'");
                    if (!isBooking) {
                        queryBuilder.append("	    or f.forward_agent_no = '").append(form.getForwarderNumber()).append("'");
                    }
                    queryBuilder.append("	)");
                }
                if (CommonUtils.isNotEmpty(form.getSslBookingNumber())) {
                    String sslBookingNumber = form.getSslBookingNumber().replace("'", "\\'");
                    queryBuilder.append("	and (");
                    queryBuilder.append("	    b.ssbookingno = '").append(sslBookingNumber).append("'");
                    if (!isBooking) {
                        queryBuilder.append("	    or f.bookingno = '").append(sslBookingNumber).append("'");
                    }
                    queryBuilder.append("	)");
                }
                if (CommonUtils.isNotEmpty(form.getBookedBy())) {
                    String bookedBy = form.getBookedBy().replace("'", "\\'");
                    queryBuilder.append("	and  b.username = '").append(bookedBy).append("'");
                }
                if (CommonUtils.isAllNotEmpty(form.getSslName(), form.getSslNumber())) {
                    String sslname = form.getSslName().replace("'", "\\'") + "//" + form.getSslNumber();
                    queryBuilder.append("	and (");
                    queryBuilder.append("	    b.sslname = '").append(sslname).append("'");
                    if (!isBooking) {
                        queryBuilder.append("	    or f.ssline_no = '").append(form.getSslNumber()).append("'");
                    }
                    queryBuilder.append("	)");
                }
                if (CommonUtils.isNotEmpty(form.getIssuingTerminal())) {
                    String issuingTerminal = form.getIssuingTerminal().replace("'", "\\'");
                    queryBuilder.append("	and (");
                    queryBuilder.append("	    b.issuing_terminal = '").append(issuingTerminal).append("'");
                    if (!isBooking) {
                        queryBuilder.append("	    or f.billing_terminal = '").append(issuingTerminal).append("'");
                    }
                    queryBuilder.append("	)");
                }
                if (CommonUtils.isNotEmpty(form.getCreatedBy())) {
                    String createdBy = form.getCreatedBy().replace("'", "\\'");
                    queryBuilder.append("	and b.booked_by = '").append(createdBy).append("'");
                } else if (CommonUtils.isNotEmpty(form.getMasterShipperNumber())) {
                    String masterShipperNo = form.getMasterShipperNumber().replace("'", "\\'");
                    queryBuilder.append("	and f.Shipper_No ='").append(masterShipperNo).append("'");
                }
            }
            if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "Container Cut off")) {
                queryBuilder.append("	and b.portcutoff >= current_date()");
            } else if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "Doc Cut off")) {
                queryBuilder.append("	and b.doc_cut_off >= current_date()");
            } else if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "ETD")) {
                queryBuilder.append("	and if(isnull(f.sail_date),b.etd,f.sail_date) >= current_date()");
            } else if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "ETA")) {
                queryBuilder.append("	and if(isnull(f.eta),b.eta,f.eta) >= current_date()");
            }
            queryBuilder.append("   group by b.file_no");
            queryBuilder.append("   order by");
            if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "Container Cut off")) {
                queryBuilder.append(" portCutOff desc");
            } else if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "Doc Cut off")) {
                queryBuilder.append(" docCutOff asc");
            } else if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "ETD")) {
                queryBuilder.append(" etd desc");
            } else if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "ETA")) {
                queryBuilder.append(" eta desc");
            } else {
                queryBuilder.append(" b.file_no desc");
            }
            queryBuilder.append("   limit ").append(form.getLimit());
            return queryBuilder.toString();
        }
    }

    private String buildImportBlWhereCondition(SearchForm form) throws Exception {
        List<String> blFilters = new ArrayList<String>();
        blFilters.add("BL");
        blFilters.add("Un Manifested");
        blFilters.add("Manifested");
        blFilters.add("Manifested No Master");
        blFilters.add("Master Not Received");
        blFilters.add("FAE Not Applied");
        blFilters.add("Imp Release");
        blFilters.add("No Release");
        blFilters.add("Doc Release");
        blFilters.add("Pmt Release");
        blFilters.add("Over Paid");
        blFilters.add("Closed");
        blFilters.add("Audited");
        blFilters.add("Voided");
        List<String> blFields = new ArrayList<String>();
        blFields.add(form.getContainerNumber());
        blFields.add(form.getInboundNumber());
        blFields.add(form.getMasterBl());
        blFields.add(form.getVessel());
        blFields.add(form.getAms());
        blFields.add(form.getSubHouseBl());
        blFields.add(form.getMasterShipperNumber());
        boolean isBooking = CommonUtils.in(form.getFilterBy(), "Booking", "Doc's Not Received");
        boolean isBl = false;
        if (!isBooking
                && (form.isSailingDate()
                || CommonUtils.in(form.getFilterBy(), (String[]) blFilters.toArray(new String[blFilters.size()]))
                || CommonUtils.isAtLeastOneNotEmpty((String[]) blFields.toArray(new String[blFields.size()])))) {
            isBl = true;
        }
        if (!isBooking && !isBl && CommonUtils.in(form.getSortByDate(), "Container Cut off", "Doc Cut off")) {
            isBooking = true;
        }
        if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Quotation")
                || form.isDisableClient() && CommonUtils.isNotEmpty(form.getManualClientName())
                || CommonUtils.isNotEmpty(form.getClientNumber())
                || isBooking
                || (!isBl && CommonUtils.isNotEmpty(form.getBookedBy()))) {
            return null;
        } else {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append(" from");
            queryBuilder.append("   fcl_bl f");
            queryBuilder.append("   left join quotation q");
            queryBuilder.append("	on (");
            queryBuilder.append("	    f.file_number = q.file_no");
            queryBuilder.append("	    and q.file_type = 'I'");
            queryBuilder.append("	)");
            queryBuilder.append("   left join booking_fcl b");
            queryBuilder.append("	on (");
            queryBuilder.append("	    f.file_number = b.file_no");
            queryBuilder.append("	    and b.importflag = 'I'");
            queryBuilder.append("	)");
            if (CommonUtils.isNotEmpty(form.getContainerNumber())) {
                String containerNumber = form.getContainerNumber().replace("'", "\\'");
                queryBuilder.append("   join fcl_bl_container_dtls c");
                queryBuilder.append("	on (");
                queryBuilder.append("	    f.bol = c.bolid");
                queryBuilder.append("	    and c.trailer_no like '%").append(containerNumber).append("%'");
                queryBuilder.append("	)");
            }
            if (CommonUtils.isNotEmpty(form.getInboundNumber())) {
                String inboundNumber = form.getInboundNumber().replace("'", "\\'");
                queryBuilder.append("   join fcl_inbond_details i");
                queryBuilder.append("	on (");
                queryBuilder.append("	    f.bol = i.bol_id");
                queryBuilder.append("	    and i.inbond_number like '%").append(inboundNumber).append("%'");
                queryBuilder.append("	)");
            }
            if (CommonUtils.isNotEmpty(form.getAesItn())) {
                String aesItn = form.getAesItn().replace("'", "\\'");
                queryBuilder.append("   join fcl_aes_details a");
                queryBuilder.append("	on (");
                queryBuilder.append("	    f.file_no = a.file_no");
                queryBuilder.append("	    and a.aes_details like '%").append(aesItn).append("%'");
                queryBuilder.append("	)");
            }
            if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "FAE Not Applied")) {
                queryBuilder.append("   left join fcl_bl_costcodes co");
                queryBuilder.append("	on (");
                queryBuilder.append("		f.bol = co.bolid");
                queryBuilder.append("	    and	co.cost_code = 'FAECOMM'");
                queryBuilder.append("	    and co.delete_flag = 'no'");
                queryBuilder.append("	)");
            }
            queryBuilder.append("   where f.importflag = 'I'");
            queryBuilder.append("	and q.quote_id is null");
            queryBuilder.append("	and b.bookingid is null");
             if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Online Bookings")) {
                queryBuilder.append("	and f.file_no like 'R%' ");
            }
            if (CommonUtils.isNotEmpty(form.getFileNumber())) {
//                String fileNumber = form.getFileNumber().replaceAll("[^\\p{Digit}]+", "");
                queryBuilder.append("	and f.file_number = '").append(form.getFileNumber()).append("'");
            } else {
                queryBuilder.append("	and f.file_number is not null");
                queryBuilder.append("	and f.bol is not null");
                if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Un Manifested")) {
                    queryBuilder.append("	and f.manifested_date is null and f.file_number is not null");
                } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Manifested")) {
                    queryBuilder.append("	and f.manifested_date is not null");
                    queryBuilder.append("	and f.ready_to_post = 'M'");
                } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Manifested No Master")) {
                    queryBuilder.append("	and f.manifested_date is not null");
                    queryBuilder.append("	and f.ready_to_post = 'M'");
                    String fileNumbers = getNoMasterFiles(form);
                    if (CommonUtils.isNotEmpty(fileNumbers)) {
                        queryBuilder.append("	and substring_index(f.file_no, '-', 1) in (").append(fileNumbers).append(")");
                    } else {
                        queryBuilder.append("	and f.file_no is null");
                    }
                } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Master Not Received")) {
                    String fileNumbers = getNoMasterFiles(form);
                    if (CommonUtils.isNotEmpty(fileNumbers)) {
                        queryBuilder.append("	and substring_index(f.file_no, '-', 1) in (").append(fileNumbers).append(")");
                    } else {
                        queryBuilder.append("	and f.file_no is null");
                    }
                } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Imp Release")) {
                    queryBuilder.append("	and f.import_release = '").append(YES).append("'");
                    queryBuilder.append("	and f.payment_release = '").append(YES).append("'");
                } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "No Release")) {
                    queryBuilder.append("	and (f.import_release = '").append(NO).append("' or f.import_release is null)");
                    queryBuilder.append("	and (f.payment_release = '").append(NO).append("' or f.payment_release is null)");
                } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Doc Release")) {
                    queryBuilder.append("	and f.import_release = '").append(YES).append("'");
                    queryBuilder.append("	and (f.payment_release = '").append(NO).append("' or f.payment_release is null)");
                } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Pmt Release")) {
                    queryBuilder.append("	and (f.import_release = '").append(NO).append("' or f.import_release is null)");
                    queryBuilder.append("	and f.payment_release = '").append(YES).append("'");
                } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Over Paid")) {
                    queryBuilder.append("	and f.over_paid_status = '1'");
                } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Closed")) {
                    queryBuilder.append("	and f.bl_closed = '").append(YES).append("'");
                } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Audited")) {
                    queryBuilder.append("	and f.bl_audited = '").append(YES).append("'");
                } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "Voided")) {
                    queryBuilder.append("	and f.void is not null");
                }
                 if (CommonUtils.isNotEmpty(form.getMasterBl())) {
                    String masterBl = form.getMasterBl().replace("'", "\\'");
                    queryBuilder.append("	and f.new_master_bl like '%").append(masterBl).append("%'");
                }
                if (CommonUtils.isNotEmpty(form.getVessel())) {
                    queryBuilder.append("  and f.vessel like '%").append(getVesselIdFromDesc(form.getVessel())).append("%'");
                }
                if (CommonUtils.isNotEmpty(form.getAms())) {
                    String ams = form.getAms().replace("'", "\\'");
                    queryBuilder.append("	and f.import_ams_house_bl like '").append(ams).append("%'");
                }
                if (CommonUtils.isNotEmpty(form.getSubHouseBl())) {
                    String subHouseBl = form.getSubHouseBl().replace("'", "\\'");
                    queryBuilder.append("	and f.import_orgin_blno like '").append(subHouseBl).append("%'");
                } else if (CommonUtils.isNotEmpty(form.getMasterShipperNumber())) {
                    String masterShipperNo = form.getMasterShipperNumber().replace("'", "\\'");
                    queryBuilder.append("	and f.Shipper_No ='").append(masterShipperNo).append("'");
                }
                if (CommonUtils.isAllNotEmpty(form.getFromDate(), form.getToDate())) {
                    String fromDate = DateUtils.formatDate(DateUtils.parseDate(form.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
                    String toDate = DateUtils.formatDate(DateUtils.parseDate(form.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
                    if (CommonUtils.in(form.getFilterBy(), "Manifested", "Manifested No Master")) {
                        queryBuilder.append("	and date(f.manifested_date) between '").append(fromDate).append("' and '").append(toDate).append("'");
                    } else if (form.isSailingDate()) {
                        queryBuilder.append("	and date(f.sail_date) between '").append(fromDate).append("' and '").append(toDate).append("'");
                    } else {
                        queryBuilder.append("   and date(f.bol_date) between '").append(fromDate).append("' and '").append(toDate).append("'");
                    }
                }
                if (CommonUtils.isNotEmpty(form.getOriginRegion())) {
                    String origins = getPorts(form.getOriginRegion());
                    if (CommonUtils.isNotEmpty(origins)) {
                        queryBuilder.append("	and f.original_terminal in (").append(origins).append(")");
                    } else {
                        queryBuilder.append("	and f.original_terminal is null");
                    }
                } else if (CommonUtils.isNotEmpty(form.getOrigin())) {
                    String origin = form.getOrigin().replace("'", "\\'");
                    queryBuilder.append("	and f.original_terminal = '").append(origin).append("'");
                }
                if (CommonUtils.isNotEmpty(form.getDestinationRegion())) {
                    String destinations = getPorts(form.getDestinationRegion());
                    if (CommonUtils.isNotEmpty(destinations)) {
                        queryBuilder.append("	and f.port in (").append(destinations).append(")");
                    } else {
                        queryBuilder.append("	and f.port is null");
                    }
                } else if (CommonUtils.isNotEmpty(form.getDestination())) {
                    String destination = form.getDestination().replace("'", "\\'");
                    queryBuilder.append("	and f.port = '").append(destination).append("'");
                }
                if (CommonUtils.isNotEmpty(form.getPol())) {
                    String pol = form.getPol().replace("'", "\\'");
                    queryBuilder.append("	and f.port_of_loading = '").append(pol).append("'");
                }
                if (CommonUtils.isNotEmpty(form.getPod())) {
                    String pod = form.getPod().replace("'", "\\'");
                    queryBuilder.append("	and f.portofdischarge = '").append(pod).append("'");
                }
                if (CommonUtils.isNotEmpty(form.getSalesCode())) {
                    queryBuilder.append("	and ( f.house_shipper_no in ( ").append(getSalesCode(form.getSalesCode())).append(")");
                } else if (CommonUtils.isNotEmpty(form.getShipperNumber())) {
                    queryBuilder.append("	and f.house_shipper_no = '").append(form.getShipperNumber()).append("'");
                }
                if (CommonUtils.isNotEmpty(form.getSalesCode())) {
                    queryBuilder.append("	 or f.houseconsignee in ( ").append(getSalesCode(form.getSalesCode())).append(")");
                } else if (CommonUtils.isNotEmpty(form.getConsigneeNumber())) {
                    queryBuilder.append("	 and f.houseconsignee = '").append(form.getConsigneeNumber()).append("'");
                }
                if (CommonUtils.isNotEmpty(form.getSalesCode())) {
                    queryBuilder.append("	or f.forward_agent_no in ( ").append(getSalesCode(form.getSalesCode())).append(") )");
                } else if (CommonUtils.isNotEmpty(form.getForwarderNumber())) {
                    queryBuilder.append("	and f.forward_agent_no = '").append(form.getForwarderNumber()).append("'");
                }
                if (CommonUtils.isNotEmpty(form.getSslBookingNumber())) {
                    String sslBookingNumber = form.getSslBookingNumber().replace("'", "\\'");
                    queryBuilder.append("	and f.bookingno = '").append(sslBookingNumber).append("'");
                }
                if (CommonUtils.isAllNotEmpty(form.getSslNumber())) {
                    queryBuilder.append("	and f.ssline_no = '").append(form.getSslNumber()).append("'");
                }
                if (CommonUtils.isNotEmpty(form.getIssuingTerminal())) {
                    String issuingTerminal = form.getIssuingTerminal().replace("'", "\\'");
                    queryBuilder.append("	and f.billing_terminal = '").append(issuingTerminal).append("'");
                }
                if (CommonUtils.isNotEmpty(form.getCreatedBy())) {
                    String createdBy = form.getCreatedBy().replace("'", "\\'");
                    queryBuilder.append("	and f.bl_by = '").append(createdBy).append("'");
                }
            }
            if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "Container Cut off")) {
                queryBuilder.append("	and b.portcutoff >= current_date()");
            } else if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "Doc Cut off")) {
                queryBuilder.append("	and b.doc_cut_off >= current_date()");
            } else if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "ETD")) {
                queryBuilder.append("	and if(isnull(f.sail_date),b.etd,f.sail_date) >= current_date()");
            } else if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "ETA")) {
                queryBuilder.append("	and if(isnull(f.eta),b.eta,f.eta) >= current_date()");
            }
            queryBuilder.append("   group by f.file_no ");
            queryBuilder.append("   order by");
            if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "Container Cut off")) {
                queryBuilder.append(" portCutOff desc");
            } else if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "Doc Cut off")) {
                queryBuilder.append(" docCutOff asc");
            } else if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "ETD")) {
                queryBuilder.append(" etd desc");
            } else if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "ETA")) {
                queryBuilder.append(" eta desc");
            } else {
                queryBuilder.append(" f.file_no desc ");
            }
            queryBuilder.append("   limit ").append(form.getLimit());
            return queryBuilder.toString();
        }
    }

    private String buildOuterWhereCondition(SearchForm form) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        if (form.isImportFile()) {
            queryBuilder.append(" left join fcl_bl_container_dtls cn");
            queryBuilder.append(" on (f.bl_id = cn.bolid)");
        }
        queryBuilder.append("	group by f.file_no");
        queryBuilder.append("   order by ");
        if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "Container Cut off")) {
            queryBuilder.append(" f.portCutOff desc");
        } else if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "Doc Cut off")) {
            queryBuilder.append(" f.docCutOff asc");
        } else if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "ETD")) {
            queryBuilder.append(" f.etd desc");
        } else if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "ETA")) {
            queryBuilder.append(" f.eta desc");
        } else if (CommonUtils.isEqualIgnoreCase(form.getSortBy(), "pod") || CommonUtils.isEqualIgnoreCase(form.getSortBy(), "origin")
                || CommonUtils.isEqualIgnoreCase(form.getSortBy(), "pol") || CommonUtils.isEqualIgnoreCase(form.getSortBy(), "destination")) {
            queryBuilder.append(" substring( ").append(form.getSortBy()).append(", -6, 5) ").append(form.getOrderBy());
        } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "No COB")) {
            queryBuilder.append("f.etd").append(" ").append(form.getOrderBy().equals("desc") ? "asc " : "desc ");
        } else {
            queryBuilder.append(form.getSortBy()).append(" ").append(form.getOrderBy());
        }
        return queryBuilder.toString();
    }

    public void search(SearchForm form) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        String outerSelectExpression = buildOuterSelectExpression(form.isImportFile());
        String outerWhereExpression = buildOuterWhereCondition(form);
        queryBuilder.append(" select f.*, group_concat(f.container_details separator '<br>') as containers from ");
        queryBuilder.append(" (select f.*, concat(count(cd.size_legend),'x',gd.codedesc) as container_details from ");
        queryBuilder.append("(select f.*,");
        queryBuilder.append("edi.tracking_status as trackingStatus");
        queryBuilder.append("	from (");
        queryBuilder.append(outerSelectExpression);
        queryBuilder.append(" from (");
        if (form.isImportFile()) {
            queryBuilder.append("   select * ");
            queryBuilder.append("	from (");
            String quoteSelectExpression = buildImportQuoteSelectExpression();
            String quoteWhereCondition = buildImportQuoteWhereCondition(form);
            queryBuilder.append("	    (");
            queryBuilder.append(quoteSelectExpression);
            queryBuilder.append(quoteWhereCondition);
            queryBuilder.append("	    )");
            String bookingWhereCondition = buildImportBookingWhereCondition(form);
            if (CommonUtils.isNotEmpty(bookingWhereCondition)) {
                String bookingSelectExpression = buildImportBookingSelectExpression();
                queryBuilder.append("	union");
                queryBuilder.append("	    (");
                queryBuilder.append(bookingSelectExpression);
                queryBuilder.append(bookingWhereCondition);
                queryBuilder.append("	    )");
            }
            String blWhereCondition = buildImportBlWhereCondition(form);
            if (CommonUtils.isNotEmpty(blWhereCondition)) {
                String blSelectExpression = buildImportBlSelectExpression();
                queryBuilder.append("	union");
                queryBuilder.append("	    (");
                queryBuilder.append(blSelectExpression);
                queryBuilder.append(blWhereCondition);
                queryBuilder.append("	    )");
            }
            queryBuilder.append("   ) as f");
            queryBuilder.append("   group by f.file_no");
            queryBuilder.append("   order by");
            if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "Container Cut off")) {
                queryBuilder.append(" f.portCutOff desc");
            } else if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "Doc Cut off")) {
                queryBuilder.append(" f.docCutOff asc");
            } else if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "ETD")) {
                queryBuilder.append(" f.etd desc");
            } else if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "ETA")) {
                queryBuilder.append(" f.eta desc");
            } else {
                queryBuilder.append(" f.file_no desc ");
            }
            queryBuilder.append("   limit ").append(form.getLimit());
        } else {
            String innerSelectExpression = buildInnerSelectExpression(form.isImportFile());
            String innerWhereExpression = buildExportInnerWhereCondition(form);
            queryBuilder.append(innerSelectExpression);
            queryBuilder.append(innerWhereExpression);
        }
        queryBuilder.append(") as f");
        queryBuilder.append(outerWhereExpression);
        queryBuilder.append(") as f");
        queryBuilder.append(" left join logfile_edi edi");
        queryBuilder.append("	on (f.file_no = edi.file_no");
        queryBuilder.append("	    and edi.status = 'success'");
        queryBuilder.append("	    and edi.message_type = '315')");
        queryBuilder.append("	group by f.file_no");
        queryBuilder.append("   order by ");
        if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "Container Cut off")) {
            queryBuilder.append(" f.portCutOff desc");
        } else if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "Doc Cut off")) {
            queryBuilder.append(" f.docCutOff asc");
        } else if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "ETD")) {
            queryBuilder.append(" f.etd desc");
        } else if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "ETA")) {
            queryBuilder.append(" f.eta desc");
        } else if (CommonUtils.isEqualIgnoreCase(form.getSortBy(), "pod") || CommonUtils.isEqualIgnoreCase(form.getSortBy(), "origin")
                || CommonUtils.isEqualIgnoreCase(form.getSortBy(), "pol") || CommonUtils.isEqualIgnoreCase(form.getSortBy(), "destination")) {
            queryBuilder.append(" substring( ").append(form.getSortBy()).append(", -6, 5) ").append(form.getOrderBy());
        } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "No COB")) {
            queryBuilder.append("f.etd").append(" ").append(form.getOrderBy().equals("desc") ? "asc " : "desc ");
        } else {
            queryBuilder.append(form.getSortBy()).append(" ").append(form.getOrderBy());
        }
        queryBuilder.append(" ) as f ");
        queryBuilder.append(" left join fcl_bl bl on (f.file_no = bl.file_number and bl.bolid not like '%==%') ");
        queryBuilder.append(" left join fcl_bl_container_dtls cd on bl.bol = cd.bolid and (cd.disabled_flag is null or cd.disabled_flag = 'e') ");
        queryBuilder.append(" left join genericcode_dup gd on gd.id = cd.size_legend ");
        queryBuilder.append(" group by f.file_no,cd.size_legend) as f ");
        queryBuilder.append("	group by f.file_no");
        queryBuilder.append("   order by ");
        if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "Container Cut off")) {
            queryBuilder.append(" f.portCutOff desc");
        } else if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "Doc Cut off")) {
            queryBuilder.append(" f.docCutOff asc");
        } else if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "ETD")) {
            queryBuilder.append(" f.etd desc");
        } else if (CommonUtils.isEqualIgnoreCase(form.getSortByDate(), "ETA")) {
            queryBuilder.append(" f.eta desc");
        } else if (CommonUtils.isEqualIgnoreCase(form.getSortBy(), "pod") || CommonUtils.isEqualIgnoreCase(form.getSortBy(), "origin")
                || CommonUtils.isEqualIgnoreCase(form.getSortBy(), "pol") || CommonUtils.isEqualIgnoreCase(form.getSortBy(), "destination")) {
            queryBuilder.append(" substring( ").append(form.getSortBy()).append(", -6, 5) ").append(form.getOrderBy());
        } else if (CommonUtils.isEqualIgnoreCase(form.getFilterBy(), "No COB")) {
            queryBuilder.append("f.etd").append(" ").append(form.getOrderBy().equals("desc") ? "asc " : "desc ");
        } else {
            queryBuilder.append(form.getSortBy()).append(" ").append(form.getOrderBy());
        }
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("fileType", StringType.INSTANCE);
        query.addScalar("quoteStatus", StringType.INSTANCE);
        if (!form.isImportFile()) {
            query.addScalar("multiStatus", StringType.INSTANCE);
        }
        query.addScalar("bookingStatus", StringType.INSTANCE);
        query.addScalar("blStatus", StringType.INSTANCE);
        query.addScalar("hazmat", BooleanType.INSTANCE);
        query.addScalar("ediStatus", StringType.INSTANCE);
        query.addScalar("bookingEdiStatus", StringType.INSTANCE);
        query.addScalar("fileNumber", StringType.INSTANCE);
        query.addScalar("blId", StringType.INSTANCE);
        query.addScalar("bookingId", StringType.INSTANCE);
        query.addScalar("quoteId", StringType.INSTANCE);
        query.addScalar("fileStatus", StringType.INSTANCE);
        if (form.isImportFile()) {
            query.addScalar("releaseType", StringType.INSTANCE);
            query.addScalar("containerNumber", StringType.INSTANCE);
        } else {
            query.addScalar("documentReceived", BooleanType.INSTANCE);
        }
        query.addScalar("documentStatus", StringType.INSTANCE);
        query.addScalar("startDate", StringType.INSTANCE);
        query.addScalar("sailDate", StringType.INSTANCE);
        query.addScalar("sslBookingNumber", StringType.INSTANCE);
        query.addScalar("origin", StringType.INSTANCE);
        query.addScalar("doorOrigin", StringType.INSTANCE);
        query.addScalar("pol", StringType.INSTANCE);
        query.addScalar("pod", StringType.INSTANCE);
        query.addScalar("destination", StringType.INSTANCE);
        query.addScalar("doorDestination", StringType.INSTANCE);
        query.addScalar("clientName", StringType.INSTANCE);
        query.addScalar("sslineName", StringType.INSTANCE);
        query.addScalar("issuingTerminal", StringType.INSTANCE);
        query.addScalar("trackingStatus", StringType.INSTANCE);
        query.addScalar("containers", StringType.INSTANCE);
        if (form.isImportFile()) {
            query.addScalar("eta", StringType.INSTANCE);
        } else {
            query.addScalar("aesStatus", StringType.INSTANCE);
            query.addScalar("aesCount", IntegerType.INSTANCE);
        }
        query.addScalar("createdBy", StringType.INSTANCE);
        query.addScalar("bookedBy", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(ResultModel.class));
        form.setResults(query.list());
    }

    public String checkLocking(String fileNumber, int userId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        String select1 = "File " + fileNumber + " is already opened in another window";
        String select2 = "This record is being used by ";
        queryBuilder.append("select if(u.user_id = '").append(userId).append("','").append(select1).append("',");
        queryBuilder.append("concat('").append(select2).append("',u.login_name,' on ',date_format(p.process_info_date,'%m/%d/%Y %h:%i %p'))) as result");
        queryBuilder.append(" from process_info p");
        queryBuilder.append(" join user_details u");
        queryBuilder.append(" on (p.user_id = u.user_id)");
        queryBuilder.append(" where p.record_id = '").append(fileNumber).append("'");
        queryBuilder.append(" order by p.id desc limit 1");
        return (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public List<PortModel> getOrigins(String destinationCode, String commodityCode) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  org.id as id,");
        queryBuilder.append("  concat(");
        queryBuilder.append("    org.portname,");
        queryBuilder.append("    if(");
        queryBuilder.append("      org.statecode != '',");
        queryBuilder.append("      concat('/', org.statecode),");
        queryBuilder.append("      ''");
        queryBuilder.append("    ),");
        queryBuilder.append("    concat('(', org.unlocationcode, ')')");
        queryBuilder.append("  ) as name,");
        queryBuilder.append("  reg.codedesc as region");
        queryBuilder.append("  ");
        queryBuilder.append("from");
        queryBuilder.append("  ports dest");
        queryBuilder.append("  join fcl_buy buy");
        queryBuilder.append("    on (buy.destination_port = dest.id)");
        queryBuilder.append("  join genericcode_dup com");
        queryBuilder.append("    on (buy.com_num = com.id");
        queryBuilder.append("    and com.code = '").append(commodityCode).append("')");
        queryBuilder.append("  join ports org");
        queryBuilder.append("    on (buy.origin_terminal = org.id)");
        queryBuilder.append("  join genericcode_dup reg");
        queryBuilder.append("    on (org.regioncode = reg.id)");
        queryBuilder.append("  join fcl_buy_cost cost");
        queryBuilder.append("    on (buy.fcl_std_id = cost.fcl_std_id)");
        queryBuilder.append("  join genericcode_dup gen");
        queryBuilder.append("    on (cost.cost_id = gen.id");
        queryBuilder.append("    and gen.code = 'OCNFRT')");
        queryBuilder.append("  ");
        queryBuilder.append("where dest.unlocationcode = '").append(destinationCode).append("'");
        queryBuilder.append("  ");
        queryBuilder.append("group by org.regioncode, org.unlocationcode");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("id", IntegerType.INSTANCE);
        query.addScalar("name", StringType.INSTANCE);
        query.addScalar("region", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(PortModel.class));
        return query.list();
    }

    public List<PortModel> getDestinations(String originCode, String commodityCode) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  dest.id as id,");
        queryBuilder.append("  concat(");
        queryBuilder.append("    dest.portname,");
        queryBuilder.append("    if(");
        queryBuilder.append("      dest.statecode != '',");
        queryBuilder.append("      concat('/', dest.statecode),");
        queryBuilder.append("      ''");
        queryBuilder.append("    ),");
        queryBuilder.append("    if(");
        queryBuilder.append("      dest.countryname != '',");
        queryBuilder.append("      concat(");
        queryBuilder.append("        '/',");
        queryBuilder.append("        '<span class=blue bold>',");
        queryBuilder.append("        dest.countryname,");
        queryBuilder.append("        '</span>'");
        queryBuilder.append("      ),");
        queryBuilder.append("      ''");
        queryBuilder.append("    ),");
        queryBuilder.append("    concat('(', dest.unlocationcode, ')')");
        queryBuilder.append("  ) as name");
        queryBuilder.append("  ");
        queryBuilder.append("from");
        queryBuilder.append("  ports org");
        queryBuilder.append("  join fcl_buy buy");
        queryBuilder.append("    on (buy.origin_terminal = org.id)");
        queryBuilder.append("  join genericcode_dup com");
        queryBuilder.append("    on (buy.com_num = com.id");
        queryBuilder.append("    and com.code = '").append(commodityCode).append("')");
        queryBuilder.append("  join ports dest");
        queryBuilder.append("    on (buy.destination_port = dest.id)");
        queryBuilder.append("  join genericcode_dup reg");
        queryBuilder.append("    on (dest.regioncode = reg.id)");
        queryBuilder.append("  join fcl_buy_cost cost");
        queryBuilder.append("    on (buy.fcl_std_id = cost.fcl_std_id)");
        queryBuilder.append("  join genericcode_dup gen");
        queryBuilder.append("    on (cost.cost_id = gen.id");
        queryBuilder.append("    and gen.code = 'OCNFRT')");
        queryBuilder.append("  ");
        queryBuilder.append("where org.unlocationcode = '").append(originCode).append("'");
        queryBuilder.append("  ");
        queryBuilder.append("group by dest.regioncode, dest.unlocationcode");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("id", IntegerType.INSTANCE);
        query.addScalar("name", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(PortModel.class));
        return query.list();
    }

    public String getDestinationsForCountry(String country) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  cast(dest.id as char character set latin1) as id ");
        queryBuilder.append("from");
        queryBuilder.append("  ports dest");
        queryBuilder.append("  join fcl_buy buy");
        queryBuilder.append("    on buy.destination_port = dest.id ");
        queryBuilder.append("where dest.countryname = '").append(country).append("' ");
        queryBuilder.append("group by dest.id");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        List<String> result = query.list();
        return CommonUtils.isNotEmpty(result) ? result.toString().replace("[", "").replace("]", "") : null;
    }

    public String getVesselIdFromDesc(String codeDesc) throws Exception {
        String queryString = "select id from genericcode_dup where codetypeid = '14' and codedesc= '" + codeDesc + "'";
        Object result = getCurrentSession().createSQLQuery(queryString).uniqueResult();
        return null != result ? result.toString() : "";
    }

    private String getSalesCode(String salesCode) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        salesCode = salesCode.replace(",", "','");
        queryBuilder.append("select concat(\"'\", t.acct_no, \"'\") from genericcode_dup s");
        queryBuilder.append(" join cust_general_info g on (s.id = g.sales_code)");
        queryBuilder.append(" join trading_partner t on (g.acct_no = t.acct_no)");
        queryBuilder.append(" where s.code in ").append("('").append(salesCode).append("')");
        queryBuilder.append(" and s.codetypeid = 23 ");
        List<String> query = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        return CommonUtils.isNotEmpty(query) ? query.toString().replace("[", "").replace("]", "") : null;
    }
}

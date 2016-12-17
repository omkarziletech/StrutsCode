/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.DocumentStoreLog;
import com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO;
import com.gp.cvst.logisoft.struts.form.SSMastersApprovedForm;
import com.logiware.bean.AccountingBean;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.hibernate.Query;

import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class SSMastersApprovedDAO extends BaseHibernateDAO implements ConstantsInterface {

    private static final Logger log = Logger.getLogger(SSMastersApprovedDAO.class);

    public String buildQuery(SSMastersApprovedForm ssMatsersApprovedForm) {
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("  ");
	queryBuilder.append("from");
	queryBuilder.append("  fcl_bl bl");
	queryBuilder.append("  join document_store_log doc");
	queryBuilder.append("    on (");
	queryBuilder.append("      doc.document_id = bl.file_no");
	if (CommonUtils.isEqual(ssMatsersApprovedForm.getModuleName(), "FCLI")) {
	    queryBuilder.append("      and doc.screen_name = 'IMPORT FILE'");
	} else  {
	    queryBuilder.append("      and doc.screen_name = 'FCLFILE'");
	}
	queryBuilder.append("      and doc.document_name = 'SS LINE MASTER BL'");
	queryBuilder.append("      and doc.status = 'Approved'");
	queryBuilder.append("    ) ");
	queryBuilder.append("where");
	queryBuilder.append("  bl.bolid not like '%==%'");
	queryBuilder.append("  and bl.ready_to_post = 'M'");
	queryBuilder.append("  and bl.converted_to_ap = 0");
	queryBuilder.append("  and doc.date_opr_done =");
	queryBuilder.append("  (select");
	queryBuilder.append("    max(date_opr_done)");
	queryBuilder.append("  from");
	queryBuilder.append("    document_store_log");
	queryBuilder.append("  where document_name = 'SS LINE MASTER BL'");
	if (CommonUtils.isEqual(ssMatsersApprovedForm.getModuleName(), "FCLE")) {
	    queryBuilder.append("      and screen_name = 'FCLFILE'");
	} else if (CommonUtils.isEqual(ssMatsersApprovedForm.getModuleName(), "FCLI")) {
	    queryBuilder.append("      and screen_name = 'IMPORT FILE'");
	}else if (CommonUtils.isEqual(ssMatsersApprovedForm.getModuleName(), "ALL")) {
	    queryBuilder.append("      and (screen_name = 'IMPORT FILE' or screen_name = 'FCLFILE')");
	}
	queryBuilder.append("    and document_id = bl.file_no)");
	if (CommonUtils.in(ssMatsersApprovedForm.getModuleName(), "FCLE", "FCLI","ALL")) {
	    queryBuilder.append("  and (");
	    queryBuilder.append("    select");
	    queryBuilder.append("      count(*)");
	    queryBuilder.append("    from");
	    queryBuilder.append("      fcl_bl_costcodes cost");
	    queryBuilder.append("    where cost.bolid = bl.bol");
	    queryBuilder.append("      and cost.cost_code = 'OCNFRT'");
	    queryBuilder.append("      and cost.account_no = if(bl.steam_ship_bl = 'C-Collect', bl.agent_no, bl.ssline_no)");
	    queryBuilder.append("      and (");
	    queryBuilder.append("        cost.transaction_type = 'AC'");
	    queryBuilder.append("        or cost.transaction_type = ''");
	    queryBuilder.append("        or cost.transaction_type is null");
	    queryBuilder.append("      )");
	    queryBuilder.append("  ) > 0");
	}
	if (CommonUtils.isEqual(ssMatsersApprovedForm.getModuleName(), "FCLE")) {
	    queryBuilder.append("  and (");
	    queryBuilder.append("    bl.importFlag is null");
	    queryBuilder.append("    or bl.importFlag != 'I'");
	    queryBuilder.append("  )");
	} else if (CommonUtils.isEqual(ssMatsersApprovedForm.getModuleName(), "FCLI")) {
	    queryBuilder.append("  and bl.importFlag='I'");
	} else if (CommonUtils.isEqual(ssMatsersApprovedForm.getModuleName(), "ALL")) {
	    queryBuilder.append("  and (");
	    queryBuilder.append("    bl.importFlag is null");
	    queryBuilder.append("    or bl.importFlag != 'A'");
	    queryBuilder.append("  )");
	} else {
	    //Need to change in future once LCL and AIR modules are added
	    queryBuilder.append("  and bl.importFlag='A'");
	}
	if (CommonUtils.isNotEmpty(ssMatsersApprovedForm.getFileNo())) {
	    queryBuilder.append("  and bl.file_no = '").append(ssMatsersApprovedForm.getFileNo()).append("'");
	}
	if (CommonUtils.isNotEmpty(ssMatsersApprovedForm.getSslineNo())) {
	    queryBuilder.append("  and bl.ssline_no = '").append(ssMatsersApprovedForm.getSslineNo()).append("'");
	}
	if (CommonUtils.isNotEmpty(ssMatsersApprovedForm.getBookingNo())) {
	    queryBuilder.append("  and bl.bookingno = '").append(ssMatsersApprovedForm.getBookingNo()).append("'");
	}
	if (CommonUtils.isNotEmpty(ssMatsersApprovedForm.getMasterNo())) {
	    queryBuilder.append("  and bl.master_bl = '").append(ssMatsersApprovedForm.getMasterNo()).append("'");
	}
	if (CommonUtils.isNotEmpty(ssMatsersApprovedForm.getContainerNo())) {
	    queryBuilder.append("  and bl.container_no = '").append(ssMatsersApprovedForm.getContainerNo()).append("'");
	}
	if (CommonUtils.isNotEmpty(ssMatsersApprovedForm.getContainerNo())) {
	    queryBuilder.append("  and bl.container_no = '").append(ssMatsersApprovedForm.getContainerNo()).append("'");
	}
	if (CommonUtils.isNotEmpty(ssMatsersApprovedForm.getPrepaidOrCollect())) {
	    queryBuilder.append("  and bl.steam_ship_bl = '").append(ssMatsersApprovedForm.getPrepaidOrCollect()).append("'");
	}
	queryBuilder.append("  group by bl.file_no");
	return queryBuilder.toString();
    }

    public Integer getNoOfRecords(String condition) {
	getCurrentSession().flush();
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("select");
	queryBuilder.append("  count(*) ");
	queryBuilder.append("from");
	queryBuilder.append("  (select");
	queryBuilder.append("    count(distinct (bl.file_no))");
	queryBuilder.append(condition);
	queryBuilder.append("  ) as t");
	Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
	return Integer.valueOf(null != result ? Integer.parseInt(result.toString()) : 0);
    }

    public List<AccountingBean> search(String condition, String sortBy, String orderBy, int start, int end) {
	getCurrentSession().flush();
	List<AccountingBean> ssMastersApprovedList = new ArrayList<AccountingBean>();
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("select");
	queryBuilder.append("  if(bl.importFlag = 'I', 'FCLI', 'FCLE') as module_name,");
	queryBuilder.append("  bl.file_no as file_no,");
	queryBuilder.append("  bl.ssline_name as ssline_name,");
	queryBuilder.append("  bl.ssline_no as ssline_no,");
	queryBuilder.append("  bl.agent as agent_name,");
	queryBuilder.append("  bl.agent_no as agent_no,");
	queryBuilder.append("  date_format(bl.sail_date, '%m/%d/%y') as etd,");
	queryBuilder.append("  date_format(bl.eta, '%m/%d/%y') as eta,");
	queryBuilder.append("  bl.steam_ship_bl as prepaid_or_collect");
	queryBuilder.append(condition);
	queryBuilder.append("  order by ").append(sortBy).append(" ").append(orderBy);
	Query query = getSession().createSQLQuery(queryBuilder.toString());
	query.setFirstResult(start).setMaxResults(end);
	List<Object> result = query.list();
	for (Object row : result) {
	    Object[] col = (Object[]) row;
	    AccountingBean accountingBean = new AccountingBean();
	    accountingBean.setModuleName((String) col[0]);
	    accountingBean.setFileNo((String) col[1]);
	    accountingBean.setSslineName((String) col[2]);
	    accountingBean.setSslineNo((String) col[3]);
	    accountingBean.setAgentName((String) col[4]);
	    accountingBean.setAgentNo((String) col[5]);
	    accountingBean.setEtd((String) col[6]);
	    accountingBean.setEta((String) col[7]);
	    accountingBean.setPrepaidOrCollect((String) col[8]);
	    ssMastersApprovedList.add(accountingBean);
	}
	return ssMastersApprovedList;
    }
    public void copyLclSSMasterApprovedDocumentsToAp(String vendorNo, String fileNo, String blNumber) throws Exception {
	DocumentStoreLogDAO documentStoreLogDAO = new DocumentStoreLogDAO();
	List<DocumentStoreLog> invoiceDocuments = documentStoreLogDAO.getInvoiceDocuments(vendorNo + "-" + blNumber);
	Set<String> fileNames = new HashSet<String>();
	if (null != invoiceDocuments && !invoiceDocuments.isEmpty()) {
	    for (DocumentStoreLog document : invoiceDocuments) {
		fileNames.add(document.getFileName());
	    }
	}
	List<DocumentStoreLog> ssMasterApprovedDocuments = documentStoreLogDAO.getLclSSMasterApprovedDocuments(fileNo);
	StringBuilder copyFolderPath = new StringBuilder(LoadLogisoftProperties.getProperty("reportLocation"));
	copyFolderPath.append("/Documents/INVOICE/").append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
	File copyFolder = new File(copyFolderPath.toString());
	if (!copyFolder.exists()) {
	    copyFolder.mkdirs();
	}
	for (DocumentStoreLog document : ssMasterApprovedDocuments) {
	    if (!fileNames.contains(document.getFileName())) {
		String originalFolderPath = document.getFileLocation().endsWith("/") ? document.getFileLocation() : (document.getFileLocation() + "/");
		File originalFile = new File(originalFolderPath, document.getFileName());
		if (originalFile.exists()) {
		    File copyFile = new File(copyFolderPath.toString(), originalFile.getName());
		    InputStream in = null;
		    OutputStream out = null;
		    try {
			in = new FileInputStream(originalFile);
			out = new FileOutputStream(copyFile);
			byte[] fileContent = IOUtils.toByteArray(in);
			IOUtils.write(fileContent, out);
		    } catch (Exception e) {
			log.info("copySSMasterApprovedDocumentsToAp failed on " + new Date(), e);
		    } finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		    }
		    DocumentStoreLog copyDocument = new DocumentStoreLog();
		    copyDocument.setScreenName("INVOICE");
		    copyDocument.setDocumentName("INVOICE");
		    copyDocument.setDocumentID(vendorNo + "-" + blNumber);
		    copyDocument.setFileLocation(copyFolderPath.toString());
		    copyDocument.setFileName(originalFile.getName());
		    copyDocument.setOperation(document.getOperation());
		    copyDocument.setDateOprDone(document.getDateOprDone());
		    copyDocument.setComments(document.getComments());
		    copyDocument.setFileSize(document.getFileSize());
		    documentStoreLogDAO.save(copyDocument);
		}
	    }
	}
    }
    public void copySSMasterApprovedDocumentsToAp(String vendorNo, String fileNo, String blNumber) throws Exception {
	DocumentStoreLogDAO documentStoreLogDAO = new DocumentStoreLogDAO();
	List<DocumentStoreLog> invoiceDocuments = documentStoreLogDAO.getInvoiceDocuments(vendorNo + "-" + blNumber);
	Set<String> fileNames = new HashSet<String>();
	if (null != invoiceDocuments && !invoiceDocuments.isEmpty()) {
	    for (DocumentStoreLog document : invoiceDocuments) {
		fileNames.add(document.getFileName());
	    }
	}
	List<DocumentStoreLog> ssMasterApprovedDocuments = documentStoreLogDAO.getSSMasterApprovedDocuments(fileNo);
	StringBuilder copyFolderPath = new StringBuilder(LoadLogisoftProperties.getProperty("reportLocation"));
	copyFolderPath.append("/Documents/INVOICE/").append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
	File copyFolder = new File(copyFolderPath.toString());
	if (!copyFolder.exists()) {
	    copyFolder.mkdirs();
	}
	for (DocumentStoreLog document : ssMasterApprovedDocuments) {
	    if (!fileNames.contains(document.getFileName())) {
		String originalFolderPath = document.getFileLocation().endsWith("/") ? document.getFileLocation() : (document.getFileLocation() + "/");
		File originalFile = new File(originalFolderPath, document.getFileName());
		if (originalFile.exists()) {
		    File copyFile = new File(copyFolderPath.toString(), originalFile.getName());
		    InputStream in = null;
		    OutputStream out = null;
		    try {
			in = new FileInputStream(originalFile);
			out = new FileOutputStream(copyFile);
			byte[] fileContent = IOUtils.toByteArray(in);
			IOUtils.write(fileContent, out);
		    } catch (Exception e) {
			log.info("copySSMasterApprovedDocumentsToAp failed on " + new Date(), e);
		    } finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		    }
		    DocumentStoreLog copyDocument = new DocumentStoreLog();
		    copyDocument.setScreenName("INVOICE");
		    copyDocument.setDocumentName("INVOICE");
		    copyDocument.setDocumentID(vendorNo + "-" + blNumber);
		    copyDocument.setFileLocation(copyFolderPath.toString());
		    copyDocument.setFileName(originalFile.getName());
		    copyDocument.setOperation(document.getOperation());
		    copyDocument.setDateOprDone(document.getDateOprDone());
		    copyDocument.setComments(document.getComments());
		    copyDocument.setFileSize(document.getFileSize());
		    documentStoreLogDAO.save(copyDocument);
		}
	    }
	}
    }
    public String buildLCLQuery(SSMastersApprovedForm ssMatsersApprovedForm) {
        StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("  ");
	queryBuilder.append("FROM lcl_ss_header header");
	queryBuilder.append(" JOIN lcl_ss_masterbl ss  ON header.id = ss.ss_header_id");
	queryBuilder.append(" JOIN lcl_ss_detail detail ON  header.id = detail.ss_header_id");
	queryBuilder.append(" JOIN document_store_log doc ON (doc.document_id = CONCAT( ss.sp_booking_no,'-',ss.id)");
	queryBuilder.append(" AND doc.document_name = 'SS LINE MASTER BL'");
	queryBuilder.append(" AND  doc.screen_name = 'LCL SS MASTER BL'");
	queryBuilder.append(" AND doc.status = 'Approved')");
	queryBuilder.append(" JOIN trading_partner tp ON detail.sp_acct_no = tp.acct_no");
	queryBuilder.append(" LEFT JOIN lcl_ss_exports ssexp ON header.id = ssexp.ss_header_id");
	queryBuilder.append(" LEFT JOIN  trading_partner agent ON ssexp.export_agent_acct_no = agent.acct_no");
	queryBuilder.append(" WHERE  ss.converted_to_ap = 0 ");
        queryBuilder.append("  and doc.date_opr_done =");
	queryBuilder.append("  (select");
	queryBuilder.append("    max(date_opr_done)");
	queryBuilder.append("  from");
	queryBuilder.append("    document_store_log");
	queryBuilder.append("  where document_name = 'SS LINE MASTER BL'");
	queryBuilder.append("      and screen_name = 'LCL SS MASTER BL'");
	queryBuilder.append("    and document_id = CONCAT( ss.sp_booking_no,'-',ss.id))");
	queryBuilder.append("    AND detail.id = (SELECT MAX(id) FROM lcl_ss_detail WHERE ss_header_id = header.id)");
        
        queryBuilder.append("  AND (");
        queryBuilder.append(" (SELECT COUNT(*) FROM lcl_ss_header ssheader ");
        queryBuilder.append("  JOIN lcl_ss_masterbl ssmaster ");
        queryBuilder.append("   ON ssheader.id = ssmaster.ss_header_id ");
        queryBuilder.append("  JOIN lcl_unit_ss unitss ");
        queryBuilder.append("   ON ssheader.id = unitss.ss_header_id ");
        queryBuilder.append("  JOIN lcl_unit unit ");
        queryBuilder.append("   ON unit.id = unitss.unit_id ");
        queryBuilder.append("  JOIN lcl_ss_ac ssac ");
        queryBuilder.append("   ON ssac.unit_ss_id = unitss.id ");
        queryBuilder.append("  JOIN transaction_ledger ac ");
        queryBuilder.append("   ON ac.Container_No = unit.unit_no ");
        queryBuilder.append("  WHERE ac.transaction_type = 'AC' ");
        queryBuilder.append("   AND ac.status = 'Open' ");
        queryBuilder.append("   AND ac.transaction_amt <> 0.00 ");
        queryBuilder.append("   AND ac.balance <> 0.00 ");
        queryBuilder.append("   AND unitss.sp_booking_no = ssmaster.sp_booking_no ");
        queryBuilder.append("   AND ssac.id = ac.cost_id ");
        queryBuilder.append("   AND ssmaster.sp_booking_no = ss.sp_booking_no ");
        queryBuilder.append("   AND ssheader.id = header.id ");
//        queryBuilder.append("   AND ac.cust_no = tp.acct_no ");
        queryBuilder.append("   AND ac.shipment_type = 'LCLE'  GROUP BY ssheader.id) > 0 ");
        queryBuilder.append("   OR ");
        queryBuilder.append("  (SELECT COUNT(*) FROM lcl_ss_header ssheader ");
        queryBuilder.append("   JOIN lcl_ss_masterbl ssmaster ");
        queryBuilder.append("   ON ssheader.id = ssmaster.ss_header_id ");
        queryBuilder.append("   JOIN lcl_unit_ss unitss ");
        queryBuilder.append("   ON ssheader.id = unitss.ss_header_id ");
        queryBuilder.append("   JOIN lcl_unit unit ");
        queryBuilder.append("   ON unit.id = unitss.unit_id ");
        queryBuilder.append("   JOIN lcl_ss_ac ssac");
        queryBuilder.append("   ON ssac.unit_ss_id = unitss.id");
        queryBuilder.append("   JOIN transaction_ledger ac");
        queryBuilder.append("   ON ac.Container_No = unit.unit_no");
        queryBuilder.append("   JOIN lcl_booking_piece_unit bpu");
        queryBuilder.append("   ON bpu.lcl_unit_ss_id = unitss.id");
        queryBuilder.append("   JOIN lcl_booking_piece bp");
        queryBuilder.append("   ON (bpu.booking_piece_id = bp.id)");
        queryBuilder.append("   JOIN lcl_booking_ac ba");
        queryBuilder.append("   ON (bp.file_number_id = ba.file_number_id)");
        queryBuilder.append("   WHERE ac.transaction_type = 'AC'");
        queryBuilder.append("   AND ac.status = 'Open'");
        queryBuilder.append("   AND ac.transaction_amt <> 0.00");
        queryBuilder.append("   AND ac.balance <> 0.00");
        queryBuilder.append("   AND unitss.sp_booking_no = ssmaster.sp_booking_no");
        queryBuilder.append("   AND ssmaster.sp_booking_no = ss.sp_booking_no");
        queryBuilder.append("   AND ssheader.id = header.id");
//        queryBuilder.append("   AND ac.cust_no = tp.acct_no");
        queryBuilder.append("   AND ac.shipment_type = 'LCLE'");
        queryBuilder.append("   AND ba.id = ac.cost_id");
        queryBuilder.append("   GROUP BY ssheader.id) > 0");
        queryBuilder.append("   )");
        if (CommonUtils.isNotEmpty(ssMatsersApprovedForm.getFileNo())) {
	    queryBuilder.append("  and header.schedule_no = '").append(ssMatsersApprovedForm.getFileNo()).append("'");
	}
	if (CommonUtils.isNotEmpty(ssMatsersApprovedForm.getSslineNo())) {
	    queryBuilder.append("  and tp.acct_no = '").append(ssMatsersApprovedForm.getSslineNo()).append("'");
	}
	if (CommonUtils.isNotEmpty(ssMatsersApprovedForm.getBookingNo())) {
	    queryBuilder.append("  and ss.sp_booking_no = '").append(ssMatsersApprovedForm.getBookingNo()).append("'");
	}
	if (CommonUtils.isNotEmpty(ssMatsersApprovedForm.getMasterNo())) {
	    queryBuilder.append("  and ss.master_bl = '").append(ssMatsersApprovedForm.getMasterNo()).append("'");
	}
	if (CommonUtils.isNotEmpty(ssMatsersApprovedForm.getPrepaidOrCollect())) {
            if("P-Prepaid".equalsIgnoreCase(ssMatsersApprovedForm.getPrepaidOrCollect())){
                queryBuilder.append("  and ss.prepaid_collect = 'P'");
            }else if("C-Collect".equalsIgnoreCase(ssMatsersApprovedForm.getPrepaidOrCollect())){
                queryBuilder.append("  and ss.prepaid_collect = 'C'");
            }else{
                queryBuilder.append("  and ss.prepaid_collect = 'B'");
            }
	}
	queryBuilder.append(" GROUP BY  CONCAT( ss.sp_booking_no,'-',ss.id) ");
        return queryBuilder.toString();
    }
    public Integer getNoOfRecordsForLCL(String condition) {
	getCurrentSession().flush();
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("select");
	queryBuilder.append("  count(*) ");
	queryBuilder.append("from");
	queryBuilder.append("  (select");
	queryBuilder.append("    count(distinct (CONCAT( ss.sp_booking_no,'-',ss.id) ))");
	queryBuilder.append(condition);
	queryBuilder.append("  ) as t");
	Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
	return Integer.valueOf(null != result ? Integer.parseInt(result.toString()) : 0);
    }
    public List<AccountingBean> searchLCL(String condition, String sortBy, String orderBy, int start, int end) {
	getCurrentSession().flush();
	List<AccountingBean> ssMastersApprovedList = new ArrayList<AccountingBean>();
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("select");
	queryBuilder.append("  'LCLE' as module_name,");
	queryBuilder.append("  header.schedule_no as file_no,");
	queryBuilder.append("  tp.acct_name as ssline_name,");
	queryBuilder.append("  tp.acct_no as ssline_no,");
	queryBuilder.append("  agent.acct_name as agent_name,");
	queryBuilder.append("  agent.acct_no as agent_no,");
	queryBuilder.append("  date_format(detail.std, '%m/%d/%y') as etd,");
	queryBuilder.append("  date_format(detail.sta, '%m/%d/%y') as eta,");
	queryBuilder.append("  CASE WHEN ss.prepaid_collect = 'C' THEN 'C-Collect' ELSE 'P-Prepaid' END as prepaid_or_collect,");
	queryBuilder.append("  header.id as noteRefId,");
	queryBuilder.append("  ss.sp_booking_no as booking_no,");
	queryBuilder.append("  ss.id as ssId,");
	queryBuilder.append("  ss.master_bl as ssMasterBl");
	queryBuilder.append(condition);
	queryBuilder.append("  order by ").append(sortBy).append(" ").append(orderBy);
	Query query = getSession().createSQLQuery(queryBuilder.toString());
	query.setFirstResult(start).setMaxResults(end);
	List<Object> result = query.list();
	for (Object row : result) {
	    Object[] col = (Object[]) row;
	    AccountingBean accountingBean = new AccountingBean();
	    accountingBean.setModuleName((String) col[0]);
	    accountingBean.setFileNo((String) col[1]);
	    accountingBean.setSslineName((String) col[2]);
	    accountingBean.setSslineNo((String) col[3]);
	    accountingBean.setAgentName((String) col[4]);
	    accountingBean.setAgentNo((String) col[5]);
	    accountingBean.setEtd((String) col[6]);
	    accountingBean.setEta((String) col[7]);
	    accountingBean.setPrepaidOrCollect((String) col[8]);
	    accountingBean.setHeaderId((BigInteger) col[9]);
	    accountingBean.setBookingNumber((String) col[10]);
            accountingBean.setSsMasterId((BigInteger) col[11]);
            accountingBean.setSsMasterBl((String) col[12]);
	    ssMastersApprovedList.add(accountingBean);
	}
	return ssMastersApprovedList;
    }
    
    public List<AccountingBean> searchAll(String condition,String condition1, String sortBy, String orderBy, int start, int end) {
	getCurrentSession().flush();
	List<AccountingBean> ssMastersApprovedList = new ArrayList<AccountingBean>();
	StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
	queryBuilder.append("  if(bl.importFlag = 'I', 'FCLI', 'FCLE') as module_name,");
	queryBuilder.append("  bl.file_no as file_no,");
	queryBuilder.append("  bl.ssline_name as ssline_name,");
	queryBuilder.append("  bl.ssline_no as ssline_no,");
	queryBuilder.append("  bl.agent as agent_name,");
	queryBuilder.append("  bl.agent_no as agent_no,");
	queryBuilder.append("  date_format(bl.sail_date, '%m/%d/%y') as etd,");
	queryBuilder.append("  date_format(bl.eta, '%m/%d/%y') as eta,");
	queryBuilder.append("  bl.steam_ship_bl as prepaid_or_collect,");
        queryBuilder.append("  null as noteRefId,");
	queryBuilder.append("  null as booking_no,");
	queryBuilder.append("  null as ssId,");
        queryBuilder.append("  null as ssMasterBl");
	queryBuilder.append(condition);
        queryBuilder.append(" UNION ALL ");
	queryBuilder.append("select");
	queryBuilder.append("  'LCLE' as module_name,");
	queryBuilder.append("  header.schedule_no as file_no,");
	queryBuilder.append("  tp.acct_name as ssline_name,");
	queryBuilder.append("  tp.acct_no as ssline_no,");
	queryBuilder.append("  agent.acct_name as agent_name,");
	queryBuilder.append("  agent.acct_no as agent_no,");
	queryBuilder.append("  date_format(detail.std, '%m/%d/%y') as etd,");
	queryBuilder.append("  date_format(detail.sta, '%m/%d/%y') as eta,");
	queryBuilder.append("  CASE WHEN ss.prepaid_collect = 'C' THEN 'C-Collect' ELSE 'P-Prepaid' END as prepaid_or_collect,");
	queryBuilder.append("  header.id as noteRefId,");
	queryBuilder.append("  ss.sp_booking_no as booking_no,");
	queryBuilder.append("  ss.id as ssId,");
        queryBuilder.append("  ss.master_bl as ssMasterBl");
	queryBuilder.append(condition1);
	queryBuilder.append("  order by ").append(sortBy).append(" ").append(orderBy);
	Query query = getSession().createSQLQuery(queryBuilder.toString());
	query.setFirstResult(start).setMaxResults(end);
	List<Object> result = query.list();
	for (Object row : result) {
	    Object[] col = (Object[]) row;
	    AccountingBean accountingBean = new AccountingBean();
	    accountingBean.setModuleName((String) col[0]);
	    accountingBean.setFileNo((String) col[1]);
	    accountingBean.setSslineName((String) col[2]);
	    accountingBean.setSslineNo((String) col[3]);
	    accountingBean.setAgentName((String) col[4]);
	    accountingBean.setAgentNo((String) col[5]);
	    accountingBean.setEtd((String) col[6]);
	    accountingBean.setEta((String) col[7]);
	    accountingBean.setPrepaidOrCollect((String) col[8]);
	    accountingBean.setHeaderId((BigInteger) col[9]);
	    accountingBean.setBookingNumber((String) col[10]);
	    accountingBean.setSsMasterId((BigInteger) col[11]);
            accountingBean.setSsMasterBl((String) col[12]);
	    ssMastersApprovedList.add(accountingBean);
	}
	return ssMastersApprovedList;
    }
}

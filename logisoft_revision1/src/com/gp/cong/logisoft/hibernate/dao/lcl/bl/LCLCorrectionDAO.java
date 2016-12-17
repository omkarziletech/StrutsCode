/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl.bl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.domain.lcl.bl.LclCorrection;
import com.gp.cong.logisoft.beans.LCLCorrectionNoticeBean;
import com.gp.cong.logisoft.beans.LCLCorrectionViewBean;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.hibernate.dao.CodetypeDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LCLCorrectionForm;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.Query;
import org.hibernate.type.BooleanType;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

/**
 *
 * @author saravanan
 */
public class LCLCorrectionDAO extends BaseHibernateDAO<LclCorrection> implements LclCommonConstant {

    public LCLCorrectionDAO() {
        super(LclCorrection.class);
    }

    public List<LclCorrection> findByFileId(Long fileId, Boolean voidFlag) throws Exception {
        String queryStr = "from LclCorrection where lclFileNumber.id=:fileId and void1=:voidFlag order by id DESC";
        Query query = getSession().createQuery(queryStr);
        query.setParameter("fileId", fileId);
        query.setBoolean("voidFlag", voidFlag);
        return query.list();
    }

    public Boolean isVoidCorrection(Long fileId) throws Exception {
        String queryStr = "select if(count(*)>0,true,false) as result from lcl_correction where file_number_id=:fileId and void <> 0";
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr);
        query.setParameter("fileId", fileId);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.setMaxResults(1).uniqueResult();
    }

    public void insertCorrections(Long fileId, Integer type, Integer code, String partyAccountNumber, Integer correctionNo,
            String comments, String status, String debitEMail, String creditEMail, Integer userId, Integer voidNo, String billToParty) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("INSERT INTO lcl_correction (file_number_id,TYPE,CODE,");
        if ("C".equalsIgnoreCase(billToParty)) {
            queryBuilder.append("cons_acct_no,");
        } else if ("N".equalsIgnoreCase(billToParty)) {
            queryBuilder.append("noty_acct_no,");
        } else {
            queryBuilder.append("third_party_acct_no,");
        }
        queryBuilder.append("correction_no,comments,STATUS,debit_email,credit_email,void,entered_datetime,entered_by_user_id,modified_datetime, ");
        queryBuilder.append("modified_by_user_id) VALUES (").append(fileId).append(",").append(type).append(",").append(code).append(",");
        if ("C".equalsIgnoreCase(billToParty)) {
            queryBuilder.append("'").append(partyAccountNumber).append("',");
        } else if ("N".equalsIgnoreCase(billToParty)) {
            queryBuilder.append("'").append(partyAccountNumber).append("',");
        } else {
            queryBuilder.append("'").append(partyAccountNumber).append("',");
        }
        queryBuilder.append(correctionNo).append(",'").append(comments.toUpperCase()).append("','").append(status).append("','");
        queryBuilder.append(debitEMail).append("','").append(creditEMail).append("',").append(voidNo).append(",SYSDATE(),").append(userId).append(",SYSDATE(),").append(userId).append(")");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public void saveCorrection(LCLCorrectionForm lclCorrectionForm, User loginUser) throws Exception {
        Date now = new Date();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        LclCorrection lclCorrection = null;
        if (CommonUtils.isEmpty(lclCorrectionForm.getCorrectionId())) {
            LclUtils lclUtils = new LclUtils();
            BigInteger lastCorrectionNo = this.getIntegerDescByFileIdWithoutVoid(lclCorrectionForm.getFileId(), "correction_no");
            if (lastCorrectionNo == null) {
                lastCorrectionNo = new BigInteger("0");
            }
            String concatenatedBlNo = "(" + lclCorrectionForm.getConcatenatedBlNo() + "-C-" + String.valueOf(lastCorrectionNo.intValue() + 1) + ")";
            lclCorrection = new LclCorrection();
            lclCorrection.setEnteredBy(loginUser);
            lclCorrection.setEnteredDatetime(now);
            lclCorrection.setCorrectionNo(lastCorrectionNo.intValue() + 1);
            lclUtils.insertLCLCorrectionRemarks(REMARKS_TYPE_LCL_CORRECTIONS, lclCorrectionForm.getFileId(), concatenatedBlNo, "saved", loginUser);
        } else {
            lclCorrection = this.findById(lclCorrectionForm.getCorrectionId());
        }
        lclCorrection.setModifiedBy(loginUser);
        lclCorrection.setModifiedDatetime(now);
        lclCorrection.setComments(CommonUtils.isNotEmpty(lclCorrectionForm.getComments()) ? lclCorrectionForm.getComments() : "");
        lclCorrection.setCreditEmail(CommonUtils.isNotEmpty(lclCorrectionForm.getCreditMemoEmail()) ? lclCorrectionForm.getCreditMemoEmail() : null);
        lclCorrection.setDebitEmail(CommonUtils.isNotEmpty(lclCorrectionForm.getDebitMemoEmail()) ? lclCorrectionForm.getDebitMemoEmail() : null);
        lclCorrection.setConsAcct(CommonUtils.isNotEmpty(lclCorrectionForm.getConsigneeNo()) ? tradingPartnerDAO.findById(lclCorrectionForm.getConsigneeNo()) : null);
        lclCorrection.setNotyAcct(CommonUtils.isNotEmpty(lclCorrectionForm.getNotifyNo()) ? tradingPartnerDAO.findById(lclCorrectionForm.getNotifyNo()) : null);
        lclCorrection.setThirdPartyAcct(CommonUtils.isNotEmpty(lclCorrectionForm.getThirdPartyAcctNo()) ? tradingPartnerDAO.findById(lclCorrectionForm.getThirdPartyAcctNo()) : null);
        lclCorrection.setCfsDevAcctNo(CommonUtils.isNotEmpty(lclCorrectionForm.getCfsDevAcctNo()) ? tradingPartnerDAO.findById(lclCorrectionForm.getCfsDevAcctNo()) : null);
        lclCorrection.setCode(genericCodeDAO.findById(lclCorrectionForm.getCorrectionCode()));
        lclCorrection.setType(genericCodeDAO.findById(lclCorrectionForm.getCorrectionType()));
        if (lclCorrection.getType() != null && lclCorrection.getType().getCode() != null && lclCorrection.getType().getCode().equals("S")) {
            lclCorrection.setPartyAcctNo(tradingPartnerDAO.findById(lclCorrectionForm.getThirdPartyAcctNo()));
        } else {
            lclCorrection.setPartyAcctNo(tradingPartnerDAO.findById(lclCorrectionForm.getPartyNo()));
        }
        lclCorrection.setLclFileNumber(new LclFileNumber(lclCorrectionForm.getFileId()));
        lclCorrection.setStatus("O");
        lclCorrection.setVoid1(Boolean.FALSE);
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getCurrentProfit())) {
            lclCorrection.setCurrentProfit(new BigDecimal(lclCorrectionForm.getCurrentProfit()));
            Double profitAfterCN = (Double.parseDouble(lclCorrectionForm.getNewAmount()) - Double.parseDouble(lclCorrectionForm.getOldAmount()))
                    + Double.parseDouble(lclCorrectionForm.getProfitAfterCN());
            lclCorrection.setProfitAfterCN(new BigDecimal(NumberUtils.convertToTwoDecimal(profitAfterCN)));
        }
        lclCorrection.setBillToParty(lclCorrectionForm.getBillToCode());
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getBillingType())) {
            lclCorrection.setBillingType(lclCorrectionForm.getBillingType().substring(0, 1));
        }
        this.saveOrUpdate(lclCorrection);
        lclCorrectionForm.setCorrectionId(lclCorrection.getId());
    }

    public void updateCorrections(Long correctionId, Integer type, Integer code, String partyAccountNumber, String comments, Integer userId,
            String debitEMail, String creditEMail) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update lcl_correction set comments= '").append(comments.toUpperCase()).append("',TYPE=").append(type).append(",CODE=").append(code).append(",modified_datetime=SYSDATE(), modified_by_user_id = ").append(userId).append(",debit_email = '").append(debitEMail).append("',credit_email = '").append(creditEMail).append("',modified_by_user_id = ").append(userId);
        if (CommonUtils.isNotEmpty(partyAccountNumber)) {
            queryBuilder.append(",party_acct_no = '").append(partyAccountNumber).append("'");
        }
        queryBuilder.append(" where id=").append(correctionId);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public void reverseCorrections(String correctionIds, Integer userId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update lcl_correction set status='O',approved_date=NULL, approved_by = NULL,void=1,");
        queryBuilder.append("posted_date=NULL, posted_by = NULL,modified_datetime=SYSDATE(), modified_by_user_id = ");
        queryBuilder.append(userId);
        queryBuilder.append(" where id IN(");
        queryBuilder.append(correctionIds);
        queryBuilder.append(")");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public void approveCorrections(Long correctionId, Integer userId, String status) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update lcl_correction set status='").append(status);
        queryBuilder.append("',approved_date=SYSDATE(), approved_by =");
        queryBuilder.append(userId);
        queryBuilder.append(",posted_date=SYSDATE(), posted_by = ");
        queryBuilder.append(userId);
        queryBuilder.append(",modified_datetime=SYSDATE(), modified_by_user_id = ");
        queryBuilder.append(userId);
        queryBuilder.append(" where id = ");
        queryBuilder.append(correctionId);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public void deleteCorrections(String correctionId, Integer userId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update lcl_correction set void=1,modified_datetime = SYSDATE(),voided_date = SYSDATE(),modified_by_user_id = ");
        queryBuilder.append(userId);
        queryBuilder.append(",voided_by = ");
        queryBuilder.append(userId);
        queryBuilder.append(" where id = ");
        queryBuilder.append(correctionId);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public BigInteger getIntegerDescByFileIdWithVoid(Long fileId, String fieldName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ").append(fieldName).append(" from lcl_correction where void = 0 and file_number_id = ").append(fileId).append(" order by id desc limit 1");
        return (BigInteger) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public BigInteger getIntegerDescByFileIdWithoutVoid(Long fileId, String fieldName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ").append(fieldName).append(" from lcl_correction where file_number_id = ").append(fileId).append(" order by id desc limit 1");
        return (BigInteger) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public Object getFieldDescByFileId(Long fileId, String fieldName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ").append(fieldName).append(" from lcl_correction where void = 0 and file_number_id = ").append(fileId).append(" order by id desc limit 1");
        return (Object) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public Long getCorrectionNumberDescByFileId(Long fileId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select correction_no from lcl_correction where  file_number_id = ").append(fileId).append(" order by desc limit 1");
        return (Long) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public BigInteger getCorrectionIdByFileNoCno(Long correctionNo, Long fileId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select id from lcl_correction where  correction_no = ");
        queryBuilder.append(correctionNo);
        queryBuilder.append(" and file_number_id = ");
        queryBuilder.append(fileId);
        return (BigInteger) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public BigInteger getCorrectionIdByFileId(Long fileId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select id from lcl_correction where  file_number_id = ").append(fileId).append(" order by correction_no desc limit 1");
        return (BigInteger) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public Object getLastApprovedFieldsByFileId(Long fileId, String fieldName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ").append(fieldName).append(" FROM lcl_correction WHERE void = 0 AND STATUS='A' AND file_number_id =  ").append(fileId).append(" order by id desc limit 1");
        return (Object) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public Object getLastApprovedCodeByFileId(Long fileId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select gendup.code FROM lcl_correction lc JOIN genericcode_dup gendup ON lc.TYPE = gendup.id where lc.void = 0 and ").append("lc.status = 'A' AND lc.file_number_id = ").append(fileId).append(" and gendup.code!='A' and gendup.code!='Y' ").append("order by lc.id desc limit 1");
        return (Object) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public List<LCLCorrectionNoticeBean> getAllApprovedCorrectionsByFileId(Long fileId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT BlNumberSystemForLclExports(lclb.file_number_id) AS blNo,lcl_corr.correction_no AS noticeNo,DATE_FORMAT(lcl_corr.entered_datetime,'%d-%b-%Y') AS ");
        queryBuilder.append("noticeDate,gen_dup_type.code AS correctionTypeValue,lcl_corr.id as correctedId FROM lcl_bl lclb JOIN lcl_file_number FILE ON lclb.file_number_id = ");
        queryBuilder.append("file.id JOIN lcl_correction lcl_corr ON lclb.file_number_id = lcl_corr.file_number_id JOIN genericcode_dup gen_dup_type ");
        queryBuilder.append("ON lcl_corr.TYPE = gen_dup_type.id JOIN un_location org ON org.id = lclb.pol_id JOIN un_location dest ON dest.id = IF(");
        queryBuilder.append("lclb.fd_id IS NOT NULL,lclb.fd_id,lclb.pod_id) WHERE lcl_corr.void = 0 AND lcl_corr.STATUS = 'A' AND lclb.file_number_id = ");
        queryBuilder.append(fileId);
        queryBuilder.append(" ORDER BY lcl_corr.id DESC");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(LCLCorrectionNoticeBean.class));
        query.addScalar("blNo", StringType.INSTANCE);
        query.addScalar("noticeNo", StringType.INSTANCE);
        query.addScalar("correctionTypeValue", StringType.INSTANCE);
        query.addScalar("noticeDate", StringType.INSTANCE);
        query.addScalar("correctedId", LongType.INSTANCE);
        return query.list();
    }

    public Object[] getCreditDebitEMails(Long correctionId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select credit_email,debit_email FROM lcl_correction WHERE id =  ").append(correctionId);
        return (Object[]) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public LCLCorrectionNoticeBean getAllCorrectionByFileId(String fileId, Long correctionId, String selectedMenu) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT IF(bl.booking_type='E',CONCAT(UPPER(IF (t.unLocationCode1 IS NOT NULL,RIGHT(t.unLocationCode1, 3),");
        queryBuilder.append("RIGHT(org.un_loc_code, 3))),'-',IF(dest.bl_numbering = 'Y',RIGHT(dest.un_loc_code, 3),dest.un_loc_code),'-',file.file_number),");
        queryBuilder.append("file.file_number) AS blNo,CONCAT(org.un_loc_name,'/',IF(org_cntry.code = 'US',org_st.code,org_cntry.codedesc),'(',");
        queryBuilder.append("org.un_loc_code,')') AS origin,CONCAT(dest.un_loc_name,'/',IF(dest_cntry.code = 'US',dest_st.code,dest_cntry.codedesc),");
        queryBuilder.append("'(',dest.un_loc_code,')') AS destination,ship.acct_no AS shipperNo,ship.acct_name AS shipperName,fwd.acct_no AS forwarderNo,");
        queryBuilder.append("fwd.acct_name AS forwarderName,tprty.acct_no AS thirdPartyNo,tprty.acct_name AS thirdPartyName,agent.acct_no AS agentNo,");
        queryBuilder.append("agent.acct_name AS agentName,notify.acct_no AS notifyNo,notify.acct_name AS notifyName,cons.acct_no AS consigneeNo,");
        queryBuilder.append("cons.acct_name AS consigneeName,ss_head.schedule_no AS voyageNumber,ss_detail.std AS sailDate,bl.bill_to_party AS ");
        queryBuilder.append("billToParty,bl.billing_type AS billingType,lcl_corr.comments AS comments,lcl_corr.TYPE AS correctionType,");
        queryBuilder.append("lcl_corr.correction_no AS noticeNo,lcl_corr.CODE AS correctionCode,user_det.login_name AS enteredBy,DATE_FORMAT(");
        queryBuilder.append("lcl_corr.entered_datetime,'%d-%b-%Y %H:%i') AS noticeDate,lcl_corr.status AS correctionStatus,DATE_FORMAT(");
        queryBuilder.append("lcl_corr.posted_date, '%d-%b-%Y %T %p') AS postedDate,lcl_corr.current_profit as currentProfit,lcl_corr.profit_aftercn as ");
        queryBuilder.append("profitAfterCN,bl.bill_to_party AS billToCode FROM lcl_file_number FILE JOIN ");
        if (selectedMenu.equalsIgnoreCase("Imports")) {
            queryBuilder.append("lcl_booking ");
        } else {
            queryBuilder.append("lcl_bl ");
        }
        queryBuilder.append("bl ON file.id = bl.file_number_id JOIN lcl_booking_piece book_piece  ON book_piece.file_number_id = file.id  JOIN ");
        queryBuilder.append("lcl_booking_piece_unit unit_pieces ON unit_pieces.booking_piece_id = book_piece.id JOIN lcl_unit_ss unit_ss ON unit_pieces.");
        queryBuilder.append("lcl_unit_ss_id = unit_ss.id JOIN lcl_ss_header ss_head ON unit_ss.ss_header_id = ss_head.id ");
        if (!selectedMenu.equalsIgnoreCase("Imports")) {
            queryBuilder.append("AND ss_head.service_type = 'E' ");
        }
        queryBuilder.append("JOIN lcl_ss_detail ss_detail ON ");
        queryBuilder.append("ss_head.id = ss_detail.ss_header_id AND ss_detail.trans_mode = 'V' JOIN un_location org ON org.id = bl.");
        if (selectedMenu.equalsIgnoreCase("Imports")) {
            queryBuilder.append("pol_id  ");
        } else {
            queryBuilder.append("poo_id ");
        }
        queryBuilder.append("JOIN genericcode_dup org_cntry ON org.countrycode = org_cntry.id LEFT JOIN terminal t ON (bl.billing_terminal = t.trmnum) ");
        queryBuilder.append("LEFT JOIN genericcode_dup org_st ON org.statecode = org_st.id ");
        queryBuilder.append("JOIN un_location dest ON dest.id = ");
        if (selectedMenu.equalsIgnoreCase("Imports")) {
            queryBuilder.append("bl.fd_id  ");
        } else {
            queryBuilder.append("IF(bl.fd_id  IS NOT NULL,bl.fd_id ,bl.pod_id)  ");
        }
        queryBuilder.append("JOIN un_location pod ON pod.id = bl.pod_id JOIN genericcode_dup dest_cntry ");
        queryBuilder.append("ON dest.countrycode = dest_cntry.id  LEFT JOIN genericcode_dup dest_st ON dest.statecode = dest_st.id LEFT JOIN ");
        queryBuilder.append("trading_partner ship ON bl.ship_acct_no = ship.acct_no LEFT JOIN trading_partner fwd ON bl.fwd_acct_no = fwd.acct_no ");
        queryBuilder.append("LEFT JOIN trading_partner tprty ON bl.third_party_acct_no = tprty.acct_no LEFT JOIN trading_partner agent ON bl.");
        if (selectedMenu.equalsIgnoreCase("Imports")) {
            queryBuilder.append("sup_acct_no ");
        } else {
            queryBuilder.append("agent_acct_no ");
        }
        queryBuilder.append(" = agent.acct_no LEFT JOIN trading_partner notify ON bl.noty_acct_no = notify.acct_no LEFT JOIN ");
        queryBuilder.append("trading_partner cons ON bl.cons_acct_no = cons.acct_no LEFT JOIN lcl_correction lcl_corr  ON file.id = ");
        queryBuilder.append("lcl_corr.file_number_id AND lcl_corr.id = ");
        queryBuilder.append(correctionId);
        queryBuilder.append(" LEFT JOIN user_details user_det ON lcl_corr.entered_by_user_id = user_det.user_id where file.id = ");
        queryBuilder.append(fileId);
        queryBuilder.append(" GROUP BY file.id");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(LCLCorrectionNoticeBean.class));
        query.addScalar("blNo", StringType.INSTANCE);
        query.addScalar("noticeNo", StringType.INSTANCE);
        query.addScalar("origin", StringType.INSTANCE);
        query.addScalar("destination", StringType.INSTANCE);
        query.addScalar("sailDate", DateType.INSTANCE);
        query.addScalar("voyageNumber", StringType.INSTANCE);
        query.addScalar("shipperNo", StringType.INSTANCE);
        query.addScalar("shipperName", StringType.INSTANCE);
        query.addScalar("forwarderNo", StringType.INSTANCE);
        query.addScalar("forwarderName", StringType.INSTANCE);
        query.addScalar("thirdPartyNo", StringType.INSTANCE);
        query.addScalar("thirdPartyName", StringType.INSTANCE);
        query.addScalar("agentNo", StringType.INSTANCE);
        query.addScalar("agentName", StringType.INSTANCE);
        query.addScalar("notifyNo", StringType.INSTANCE);
        query.addScalar("notifyName", StringType.INSTANCE);
        query.addScalar("consigneeNo", StringType.INSTANCE);
        query.addScalar("consigneeName", StringType.INSTANCE);
        query.addScalar("billToParty", StringType.INSTANCE);
        query.addScalar("billingType", StringType.INSTANCE);
        query.addScalar("comments", StringType.INSTANCE);
        query.addScalar("correctionType", IntegerType.INSTANCE);
        query.addScalar("correctionCode", IntegerType.INSTANCE);
        query.addScalar("enteredBy", StringType.INSTANCE);
        query.addScalar("noticeDate", StringType.INSTANCE);
        query.addScalar("postedDate", StringType.INSTANCE);
        query.addScalar("correctionStatus", StringType.INSTANCE);
        query.addScalar("currentProfit", StringType.INSTANCE);
        query.addScalar("profitAfterCN", StringType.INSTANCE);
        query.addScalar("billToCode", StringType.INSTANCE);
        return (LCLCorrectionNoticeBean) query.uniqueResult();
    }

    public LCLCorrectionNoticeBean getAllCorrectionByFileIdReports(String fileId, Long correctionId, String selectedMenu) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        CodetypeDAO codeTypeDAO = new CodetypeDAO();
        Integer codeTypeId = codeTypeDAO.getCodeTypeId("Vessel Codes");
        queryBuilder.append("SELECT f.*,caddress.contact_name AS attn FROM (SELECT IF(bl.booking_type='E',CONCAT(UPPER(IF (t.unLocationCode1 IS NOT NULL,");
        queryBuilder.append("RIGHT(t.unLocationCode1,3),RIGHT(org.un_loc_code,3))),'-',IF(dest.bl_numbering='Y',RIGHT(dest.un_loc_code,3),");
        queryBuilder.append("dest.un_loc_code),'-',file.file_number),file.file_number) AS blNo,CONCAT(org.un_loc_name,'/',IF(org_cntry.code = 'US',");
        queryBuilder.append("org_st.code,org_cntry.codedesc),");
        queryBuilder.append("'(',org.un_loc_code,')') AS origin,CONCAT(dest.un_loc_name,'/',IF(dest_cntry.code = 'US',dest_st.code,");
        queryBuilder.append("dest_cntry.codedesc),'(',dest.un_loc_code,')') AS destination,ship.acct_name AS shipperName,fwd.acct_name AS ");
        queryBuilder.append("forwarderName,cons.acct_name AS consigneeName,IF(bl.bill_to_party = 'A',agent.acct_no,IF(bl.bill_to_party = 'C',cons.acct_no,");
        queryBuilder.append("IF(bl.bill_to_party = 'N',notify.acct_no,IF(bl.bill_to_party = 'S',ship.acct_no,IF(bl.bill_to_party = 'F',fwd.acct_no,tparty.acct_no ");
        queryBuilder.append("))))) AS customerAcctNo,IF(bl.bill_to_party = 'A',agent.acct_name,IF(bl.bill_to_party = 'C',cons.acct_name,IF(bl.bill_to_party = 'N',");
        queryBuilder.append("notify.acct_name,IF(bl.bill_to_party = 'S',ship.acct_name,IF(bl.bill_to_party = 'F',fwd.acct_name,tparty.acct_name))))) AS customer,");
        queryBuilder.append("vessel.codedesc AS vesselName,ss_detail.std AS sailDate,lcl_corr.comments AS ");
        queryBuilder.append("comments,lcl_corr.entered_datetime AS correctionDate,bl.bill_to_party AS billToParty,ctype.code AS strCorrectionType,");
        queryBuilder.append("lcl_corr.void AS voidStatus,lcl_corr.STATUS AS correctionStatus,CONCAT(ctype.code,'-',ctype.codedesc) AS correctionTypeValue,");
        queryBuilder.append("CONCAT(ccode.code,' ',ccode.codedesc) AS correctionCodeValue,DATE_FORMAT(lcl_corr.posted_date,'%d-%b-%Y %T %p') AS postedDate,");
        queryBuilder.append("ss_head.schedule_no AS voyageNumber,unit.unit_no AS unitNo,CONCAT(ss_head.billing_trmnum,'-',org.un_loc_code,'-',dest.un_loc_code,");
        queryBuilder.append("'-',ss_head.schedule_no) AS eciShipmentFileNo ");
        queryBuilder.append(" FROM lcl_file_number FILE JOIN ");
        if (selectedMenu.equalsIgnoreCase("Imports")) {
            queryBuilder.append("lcl_booking ");
        } else {
            queryBuilder.append("lcl_bl ");
        }
        queryBuilder.append("bl ON file.id = bl.file_number_id JOIN lcl_booking_piece book_piece  ON book_piece.file_number_id = file.id  JOIN ");
        queryBuilder.append("lcl_booking_piece_unit unit_pieces ON unit_pieces.booking_piece_id = book_piece.id JOIN lcl_unit_ss unit_ss ON unit_pieces.");
        queryBuilder.append("lcl_unit_ss_id = unit_ss.id JOIN lcl_unit unit ON unit.id = unit_ss.unit_id JOIN lcl_ss_header ss_head ON unit_ss.ss_header_id = ss_head.id ");
        if (!selectedMenu.equalsIgnoreCase("Imports")) {
            //queryBuilder.append("AND ss_head.service_type = 'E' ");
        }
        queryBuilder.append("JOIN lcl_ss_detail ss_detail ON ss_head.id = ss_detail.ss_header_id AND ss_detail.trans_mode = 'V' JOIN un_location org ON org.id = bl.");
        if (selectedMenu.equalsIgnoreCase("Imports")) {
            queryBuilder.append("pol_id  ");
        } else {
            queryBuilder.append("poo_id ");
        }
        queryBuilder.append("JOIN genericcode_dup org_cntry ON org.countrycode = org_cntry.id LEFT JOIN genericcode_dup org_st ON org.statecode = org_st.id ");
        queryBuilder.append("JOIN un_location dest ON dest.id = ");
        if (selectedMenu.equalsIgnoreCase("Imports")) {
            queryBuilder.append("bl.fd_id  ");
        } else {
            queryBuilder.append("IF(bl.fd_id IS NOT NULL,bl.fd_id ,bl.pod_id) ");
        }
        queryBuilder.append("LEFT JOIN terminal t ON (bl.billing_terminal = t.trmnum) ");
        queryBuilder.append("JOIN un_location pod ON pod.id = bl.pod_id JOIN genericcode_dup dest_cntry ");
        queryBuilder.append("ON dest.countrycode = dest_cntry.id  LEFT JOIN genericcode_dup dest_st ON dest.statecode = dest_st.id LEFT JOIN ");
        queryBuilder.append("trading_partner ship ON bl.ship_acct_no = ship.acct_no LEFT JOIN trading_partner fwd ON bl.fwd_acct_no = fwd.acct_no ");
        queryBuilder.append("LEFT JOIN trading_partner cons ON bl.cons_acct_no = cons.acct_no LEFT JOIN trading_partner agent ON bl. ");
        if (selectedMenu.equalsIgnoreCase("Imports")) {
            queryBuilder.append("sup_acct_no ");
        } else {
            queryBuilder.append("agent_acct_no ");
        }
        queryBuilder.append("= agent.acct_no LEFT JOIN trading_partner notify ON bl.noty_acct_no = notify.acct_no LEFT JOIN trading_partner tparty ON ");
        queryBuilder.append("bl.third_party_acct_no = tparty.acct_no LEFT JOIN genericcode_dup vessel ON ss_detail.sp_reference_name = vessel.codedesc ");
        queryBuilder.append("AND vessel.codetypeid = ");
        queryBuilder.append(codeTypeId);
        queryBuilder.append(" LEFT JOIN lcl_correction lcl_corr  ON file.id = lcl_corr.file_number_id AND lcl_corr.id = ");
        queryBuilder.append(correctionId);
        queryBuilder.append(" LEFT JOIN genericcode_dup ctype ON ctype.id =  lcl_corr.TYPE LEFT JOIN genericcode_dup ccode ON ccode.id = lcl_corr.CODE ");
        queryBuilder.append("where file.id = ");
        queryBuilder.append(fileId);
        queryBuilder.append(" GROUP BY file.id) f LEFT JOIN cust_address caddress ON caddress.acct_no = customerAcctNo");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(LCLCorrectionNoticeBean.class));
        query.addScalar("blNo", StringType.INSTANCE);
        query.addScalar("origin", StringType.INSTANCE);
        query.addScalar("destination", StringType.INSTANCE);
        query.addScalar("voyageNumber", StringType.INSTANCE);
        query.addScalar("unitNo", StringType.INSTANCE);
        query.addScalar("eciShipmentFileNo", StringType.INSTANCE);
        query.addScalar("sailDate", DateType.INSTANCE);
        query.addScalar("correctionDate", DateType.INSTANCE);
        query.addScalar("vesselName", StringType.INSTANCE);
        query.addScalar("shipperName", StringType.INSTANCE);
        query.addScalar("forwarderName", StringType.INSTANCE);
        query.addScalar("consigneeName", StringType.INSTANCE);
        query.addScalar("comments", StringType.INSTANCE);
        query.addScalar("billToParty", StringType.INSTANCE);
        query.addScalar("strCorrectionType", StringType.INSTANCE);
        query.addScalar("customer", StringType.INSTANCE);
        query.addScalar("customerAcctNo", StringType.INSTANCE);
        query.addScalar("correctionTypeValue", StringType.INSTANCE);
        query.addScalar("correctionCodeValue", StringType.INSTANCE);
        query.addScalar("correctionStatus", StringType.INSTANCE);
        query.addScalar("voidStatus", IntegerType.INSTANCE);
        query.addScalar("postedDate", StringType.INSTANCE);
        query.addScalar("attn", StringType.INSTANCE);
        query.setMaxResults(1);
        return (LCLCorrectionNoticeBean) query.uniqueResult();
    }

    public List<LCLCorrectionViewBean> getAllAddedCorrectionsByFileId(LCLCorrectionForm lclCorrectionForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT IF(lclb.booking_type = 'E',CONCAT(UPPER(RIGHT(org.un_loc_code, 3)),'-',dest.un_loc_code,'-',file.file_number),file.file_number) AS blNo,");
        queryBuilder.append("lcl_corr.id AS correctionId,lcl_corr.correction_no AS noticeNo,lcl_corr.current_profit as currentProfit,");
        queryBuilder.append("lcl_corr.profit_aftercn as profitAfterCN,user_det.login_name AS userName,lclb.billing_type ");
        queryBuilder.append("AS prepaidCollect,DATE_FORMAT(lcl_corr.entered_datetime, '%d-%b-%Y %T %p') AS noticeDate,gen_dup_type.code AS correctionType,");
        queryBuilder.append("gen_dup_code.code AS correctionCode,ss_detail.std AS sailDate,user_det_approval.login_name AS approval,user_det_posted.login_name AS ");
        queryBuilder.append("whoPosted,DATE_FORMAT(lcl_corr.posted_date, '%d-%b-%Y %T %p') AS postedDate,IF(lcl_corr.posted_date IS NULL OR lcl_corr.posted_date='',");
        queryBuilder.append("'No','Yes') AS posted,lcl_corr.status AS STATUS,lcl_corr.party_acct_no AS partyNo,ca.send_debit_credit_notes AS creditDebit  from ");
        if (lclCorrectionForm.getSelectedMenu().equalsIgnoreCase("Imports")) {
            queryBuilder.append("lcl_booking ");
        } else {
            queryBuilder.append("lcl_bl ");
        }
        queryBuilder.append("lclb JOIN lcl_file_number FILE ON lclb.file_number_id = file.id  JOIN lcl_booking_piece book_piece ON book_piece.file_number_id = ");
        queryBuilder.append("lclb.file_number_id  JOIN lcl_booking_piece_unit unit_pieces ON unit_pieces.booking_piece_id = book_piece.id  JOIN ");
        queryBuilder.append("lcl_unit_ss unit_ss ON unit_pieces.lcl_unit_ss_id = unit_ss.id JOIN lcl_ss_header ss_head ON unit_ss.ss_header_id = ss_head.id ");
        if (!lclCorrectionForm.getSelectedMenu().equalsIgnoreCase("Imports")) {
            queryBuilder.append("AND ss_head.service_type = 'E' ");
        }
        queryBuilder.append("JOIN lcl_ss_detail ss_detail ON ss_head.id = ss_detail.ss_header_id AND ss_detail.trans_mode = 'V' JOIN ");
        queryBuilder.append("lcl_correction lcl_corr ON lclb.file_number_id = lcl_corr.file_number_id JOIN user_details user_det ON ");
        queryBuilder.append("lcl_corr.entered_by_user_id = user_det.user_id LEFT JOIN user_details user_det_approval ON lcl_corr.approved_by = ");
        queryBuilder.append("user_det_approval.user_id LEFT JOIN user_details user_det_posted ON lcl_corr.posted_by = user_det_posted.user_id ");
        queryBuilder.append("LEFT JOIN cust_accounting ca ON ca.acct_no=lcl_corr.party_acct_no JOIN ");
        queryBuilder.append("genericcode_dup gen_dup_type ON lcl_corr.TYPE = gen_dup_type.id JOIN genericcode_dup gen_dup_code ON lcl_corr.CODE = ");
        queryBuilder.append("gen_dup_code.id JOIN un_location org ON org.id = lclb.pol_id JOIN un_location dest ON dest.id = ");
        if (lclCorrectionForm.getSelectedMenu().equalsIgnoreCase("Imports")) {
            queryBuilder.append("lclb.fd_id  ");
        } else {
            queryBuilder.append("IF(lclb.fd_id IS NOT NULL,lclb.fd_id ,lclb.pod_id) ");
        }
        queryBuilder.append("WHERE lcl_corr.void = 0 and lclb.file_number_id = ");
        queryBuilder.append(lclCorrectionForm.getFileId().toString());
        queryBuilder.append(" group by lcl_corr.id DESC");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(LCLCorrectionViewBean.class));
        query.addScalar("blNo", StringType.INSTANCE);
        query.addScalar("correctionId", LongType.INSTANCE);
        query.addScalar("noticeNo", LongType.INSTANCE);
        query.addScalar("userName", StringType.INSTANCE);
        query.addScalar("sailDate", DateType.INSTANCE);
        query.addScalar("prepaidCollect", StringType.INSTANCE);
        query.addScalar("noticeDate", StringType.INSTANCE);
        query.addScalar("correctionType", StringType.INSTANCE);
        query.addScalar("correctionCode", StringType.INSTANCE);
        query.addScalar("approval", StringType.INSTANCE);
        query.addScalar("whoPosted", StringType.INSTANCE);
        query.addScalar("postedDate", StringType.INSTANCE);
        query.addScalar("posted", StringType.INSTANCE);
        query.addScalar("status", StringType.INSTANCE);
        query.addScalar("partyNo", StringType.INSTANCE);
        query.addScalar("creditDebit", StringType.INSTANCE);
        query.addScalar("currentProfit", StringType.INSTANCE);
        query.addScalar("profitAfterCN", StringType.INSTANCE);
        return query.list();
    }

    public List<LCLCorrectionViewBean> getAllVoidCorrectionsByFileId(LCLCorrectionForm lclCorrectionForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT lcl_corr.id AS correctionId,lcl_corr.correction_no AS noticeNo,user_det.login_name AS userName,lclb.billing_type ");
        queryBuilder.append("AS prepaidCollect,DATE_FORMAT(lcl_corr.entered_datetime, '%d-%b-%Y %T %p') AS noticeDate,gen_dup_type.code AS correctionType,");
        queryBuilder.append("gen_dup_code.code AS correctionCode,ss_detail.std AS sailDate,user_det_approval.login_name AS approval,user_det_posted.login_name AS ");
        queryBuilder.append("whoPosted,user_det_voided.login_name AS whoVoided,lcl_corr.posted_date AS postedDate,DATE_FORMAT(lcl_corr.voided_date, '%d-%b-%Y %T %p') ");
        queryBuilder.append("AS voidedDate,IF(lcl_corr.posted_date IS NULL OR lcl_corr.posted_date='','No','Yes') AS posted,lcl_corr.status AS STATUS from ");
        if (lclCorrectionForm.getSelectedMenu().equalsIgnoreCase("Imports")) {
            queryBuilder.append("lcl_booking ");
        } else {
            queryBuilder.append("lcl_bl ");
        }
        queryBuilder.append("lclb JOIN lcl_booking_piece book_piece ON book_piece.file_number_id = ");
        queryBuilder.append("lclb.file_number_id  JOIN lcl_booking_piece_unit unit_pieces ON unit_pieces.booking_piece_id = book_piece.id  JOIN ");
        queryBuilder.append("lcl_unit_ss unit_ss ON unit_pieces.lcl_unit_ss_id = unit_ss.id JOIN lcl_ss_header ss_head ON unit_ss.ss_header_id = ss_head.id ");
        if (!lclCorrectionForm.getSelectedMenu().equalsIgnoreCase("Imports")) {
            queryBuilder.append("AND ss_head.service_type = 'E' ");
        }
        queryBuilder.append("JOIN lcl_ss_detail ss_detail ON ss_head.id = ss_detail.ss_header_id AND ss_detail.trans_mode = 'V' JOIN ");
        queryBuilder.append("lcl_correction lcl_corr ON lclb.file_number_id = lcl_corr.file_number_id JOIN user_details user_det ON ");
        queryBuilder.append("lcl_corr.entered_by_user_id = user_det.user_id LEFT JOIN user_details user_det_approval ON lcl_corr.approved_by = ");
        queryBuilder.append("user_det_approval.user_id LEFT JOIN user_details user_det_posted ON lcl_corr.posted_by = user_det_posted.user_id ");
        queryBuilder.append("LEFT JOIN user_details user_det_voided ON lcl_corr.voided_by = user_det_voided.user_id JOIN ");
        queryBuilder.append("genericcode_dup gen_dup_type ON lcl_corr.TYPE = gen_dup_type.id JOIN genericcode_dup gen_dup_code ON lcl_corr.CODE = ");
        queryBuilder.append("gen_dup_code.id WHERE lcl_corr.void = 1 and lclb.file_number_id = ");
        queryBuilder.append(lclCorrectionForm.getFileId().toString());
        queryBuilder.append(" group by lcl_corr.id DESC");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(LCLCorrectionViewBean.class));
        query.addScalar("correctionId", LongType.INSTANCE);
        query.addScalar("noticeNo", LongType.INSTANCE);
        query.addScalar("userName", StringType.INSTANCE);
        query.addScalar("sailDate", DateType.INSTANCE);
        query.addScalar("prepaidCollect", StringType.INSTANCE);
        query.addScalar("noticeDate", StringType.INSTANCE);
        query.addScalar("whoVoided", StringType.INSTANCE);
        query.addScalar("voidedDate", StringType.INSTANCE);
        query.addScalar("correctionType", StringType.INSTANCE);
        query.addScalar("correctionCode", StringType.INSTANCE);
        return query.list();
    }

    public List<LCLCorrectionViewBean> getSearchCorrectionsImports(LCLCorrectionForm lclCorrectionForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        boolean conditionAdded = false;
        queryBuilder.append("SELECT file.file_number AS blNo,file.file_number AS fileNo,file.id AS fileId,file.state AS fileState,lcl_corr.id AS correctionId,");
        queryBuilder.append("lcl_corr.correction_no AS noticeNo,user_det.login_name AS userName,");
        queryBuilder.append("lclb.billing_type AS prepaidCollect,DATE_FORMAT(lcl_corr.entered_datetime, '%d-%b-%Y %T %p') AS noticeDate,");
        queryBuilder.append("lcl_corr.status AS STATUS,gen_dup_type.code AS correctionType,gen_dup_code.code AS correctionCode,ss_detail.std AS sailDate,");
        queryBuilder.append("user_det_approval.login_name AS approval,DATE_FORMAT(lcl_corr.posted_date, '%d-%b-%Y %T %p') AS postedDate,lcl_corr.void AS voidStatus ");
        queryBuilder.append("FROM lcl_correction lcl_corr JOIN lcl_file_number FILE ON lcl_corr.file_number_id = file.id JOIN lcl_booking ");
        queryBuilder.append("lclb ON lcl_corr.file_number_id = lclb.file_number_id JOIN lcl_booking_piece book_piece ON book_piece.file_number_id = ");
        queryBuilder.append("lclb.file_number_id  JOIN lcl_booking_piece_unit unit_pieces ON unit_pieces.booking_piece_id = book_piece.id  ");
        queryBuilder.append("JOIN lcl_unit_ss unit_ss ON unit_pieces.lcl_unit_ss_id = unit_ss.id JOIN lcl_ss_header ss_head ON ");
        queryBuilder.append("unit_ss.ss_header_id = ss_head.id JOIN lcl_ss_detail ss_detail ON ss_head.id = ss_detail.ss_header_id AND ");
        queryBuilder.append("ss_detail.trans_mode = 'V' JOIN user_details user_det ON lcl_corr.entered_by_user_id = user_det.user_id LEFT JOIN user_details ");
        queryBuilder.append("user_det_approval ON lcl_corr.approved_by = user_det_approval.user_id LEFT JOIN user_details user_det_posted ON ");
        queryBuilder.append("lcl_corr.posted_by = user_det_posted.user_id JOIN genericcode_dup gen_dup_type ON lcl_corr.TYPE = gen_dup_type.id ");
        queryBuilder.append("JOIN genericcode_dup gen_dup_code ON lcl_corr.CODE = gen_dup_code.id JOIN un_location org ON org.id = lclb.pol_id ");
        queryBuilder.append(" JOIN un_location dest ON dest.id = lclb.fd_id ");
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getFileNo())) {
            queryBuilder.append("WHERE file.file_number = '").append(lclCorrectionForm.getFileNo()).append("'");
            conditionAdded = true;
        } else if (CommonUtils.isNotEmpty(lclCorrectionForm.getBlNo())) {
            if (lclCorrectionForm.getBlNo().contains("-")) {
                String blNumber = lclCorrectionForm.getBlNo().substring(lclCorrectionForm.getBlNo().lastIndexOf("-") + 1, lclCorrectionForm.getBlNo().length());
                conditionAdded = true;
                queryBuilder.append("WHERE file.file_number = '").append(blNumber).append("'");
            }
        }
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getSearchCorrectionCode())) {
            if (conditionAdded) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" WHERE ");
            }
            conditionAdded = true;
            queryBuilder.append(" gen_dup_code.id = ").append(lclCorrectionForm.getSearchCorrectionCode());
        }
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getFilterBy()) && !lclCorrectionForm.getFilterBy().equalsIgnoreCase("All")) {
            if (conditionAdded) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" WHERE ");
            }
            conditionAdded = true;
            if (lclCorrectionForm.getFilterBy().equalsIgnoreCase("1")) {
                queryBuilder.append(" lcl_corr.void = '").append(lclCorrectionForm.getFilterBy()).append("'");
            } else if (lclCorrectionForm.getFilterBy().equalsIgnoreCase("O")) {
                queryBuilder.append(" lcl_corr.status = '").append(lclCorrectionForm.getFilterBy()).append("'");
                queryBuilder.append(" AND lcl_corr.void <> 1");
            } else {
                queryBuilder.append(" lcl_corr.status = '").append(lclCorrectionForm.getFilterBy()).append("'");
            }
        }
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getNoticeNo())) {
            if (conditionAdded) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" WHERE ");
            }
            conditionAdded = true;
            queryBuilder.append(" lcl_corr.correction_no = ").append(lclCorrectionForm.getNoticeNo());
        }
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getCreatedByUserId())) {
            if (conditionAdded) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" WHERE ");
            }
            conditionAdded = true;
            queryBuilder.append(" lcl_corr.entered_by_user_id = ").append(lclCorrectionForm.getCreatedByUserId());
        }
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getApprovedByUserId())) {
            if (conditionAdded) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" WHERE ");
            }
            conditionAdded = true;
            queryBuilder.append(" lcl_corr.approved_by = ").append(lclCorrectionForm.getApprovedByUserId());
        }
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getSearchShipperNo())) {
            if (conditionAdded) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" WHERE ");
            }
            conditionAdded = true;
            queryBuilder.append(" lclb.ship_acct_no = '").append(lclCorrectionForm.getSearchShipperNo()).append("'");
        }
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getSearchForwarderNo())) {
            if (conditionAdded) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" WHERE ");
            }
            conditionAdded = true;
            queryBuilder.append(" lclb.fwd_acct_no = '").append(lclCorrectionForm.getSearchForwarderNo()).append("'");
        }
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getSearchThirdPartyAcctNo())) {
            if (conditionAdded) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" WHERE ");
            }
            conditionAdded = true;
            queryBuilder.append(" lclb.third_party_acct_no = '").append(lclCorrectionForm.getSearchThirdPartyAcctNo()).append("'");
        }
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getPortOfOriginId())) {
            if (conditionAdded) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" WHERE ");
            }
            conditionAdded = true;
            queryBuilder.append(" lclb.poo_id = ").append(lclCorrectionForm.getPortOfOriginId());
        }
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getPolId())) {
            if (conditionAdded) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" WHERE ");
            }
            conditionAdded = true;
            queryBuilder.append(" lclb.pol_id = ").append(lclCorrectionForm.getPolId());
        }
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getPodId())) {
            if (conditionAdded) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" WHERE ");
            }
            conditionAdded = true;
            queryBuilder.append(" lclb.pod_id = ").append(lclCorrectionForm.getPodId());
        }
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getFinalDestinationId())) {
            if (conditionAdded) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" WHERE ");
            }
            conditionAdded = true;
            queryBuilder.append(" lclb.fd_id = ").append(lclCorrectionForm.getFinalDestinationId());
        }
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getDateFilter())) {
            if (conditionAdded) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" WHERE ");
            }
            conditionAdded = true;
            queryBuilder.append(" ss_detail.std = '").append(DateUtils.parseDateToMYSQLFormat(lclCorrectionForm.getDateFilter())).append("'");
        }
        if (conditionAdded) {
            queryBuilder.append(" AND ");
        } else {
            queryBuilder.append(" WHERE ");
        }
        queryBuilder.append(" lclb.booking_type in ('I') GROUP BY lcl_corr.id DESC");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(LCLCorrectionViewBean.class));
        query.addScalar("correctionId", LongType.INSTANCE);
        query.addScalar("noticeNo", LongType.INSTANCE);
        query.addScalar("userName", StringType.INSTANCE);
        query.addScalar("sailDate", DateType.INSTANCE);
        query.addScalar("prepaidCollect", StringType.INSTANCE);
        query.addScalar("noticeDate", StringType.INSTANCE);
        query.addScalar("correctionType", StringType.INSTANCE);
        query.addScalar("correctionCode", StringType.INSTANCE);
        query.addScalar("status", StringType.INSTANCE);
        query.addScalar("approval", StringType.INSTANCE);
        query.addScalar("blNo", StringType.INSTANCE);
        query.addScalar("fileNo", StringType.INSTANCE);
        query.addScalar("fileId", LongType.INSTANCE);
        query.addScalar("postedDate", StringType.INSTANCE);
        query.addScalar("voidStatus", IntegerType.INSTANCE);
        query.addScalar("fileState", StringType.INSTANCE);
        return query.list();
    }

    public List<LCLCorrectionViewBean> getSearchCorrections(LCLCorrectionForm lclCorrectionForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        boolean conditionAdded = false;
        queryBuilder.append("SELECT CONCAT(UPPER(IF (t.unLocationCode1 IS NOT NULL,RIGHT(t.unLocationCode1,3),RIGHT(org.un_loc_code,3))),'-',");
        queryBuilder.append("IF(dest.bl_numbering='Y',RIGHT(dest.un_loc_code,3),dest.un_loc_code),'-',file.file_number) AS blNo,file.file_number AS fileNo,");
        queryBuilder.append("file.id AS fileId,file.state AS fileState,lcl_corr.id AS correctionId,lcl_corr.current_profit as currentProfit,");
        queryBuilder.append("lcl_corr.profit_aftercn as profitAfterCN,lcl_corr.correction_no AS noticeNo,user_det.login_name AS userName,");
        queryBuilder.append("lclb.billing_type AS prepaidCollect,DATE_FORMAT(lcl_corr.entered_datetime, '%d-%b-%Y %T %p') AS noticeDate,");
        queryBuilder.append("lcl_corr.status AS STATUS,gen_dup_type.code AS correctionType,gen_dup_code.code AS correctionCode,ss_detail.std AS sailDate,");
        queryBuilder.append("user_det_approval.login_name AS approval,DATE_FORMAT(lcl_corr.posted_date, '%d-%b-%Y %T %p') AS postedDate,lcl_corr.void AS voidStatus ");
        queryBuilder.append("FROM lcl_correction lcl_corr JOIN lcl_file_number FILE ON lcl_corr.file_number_id = file.id JOIN lcl_bl ");
        queryBuilder.append("lclb ON lcl_corr.file_number_id = lclb.file_number_id JOIN lcl_booking_piece book_piece ON book_piece.file_number_id = ");
        queryBuilder.append("lclb.file_number_id  JOIN lcl_booking_piece_unit unit_pieces ON unit_pieces.booking_piece_id = book_piece.id  ");
        queryBuilder.append("JOIN lcl_unit_ss unit_ss ON unit_pieces.lcl_unit_ss_id = unit_ss.id JOIN lcl_ss_header ss_head ON ");
        queryBuilder.append("unit_ss.ss_header_id = ss_head.id AND ss_head.service_type = 'E' JOIN lcl_ss_detail ss_detail ON ss_head.id = ss_detail.ss_header_id AND ");
        queryBuilder.append("ss_detail.trans_mode = 'V' JOIN user_details user_det ON lcl_corr.entered_by_user_id = user_det.user_id LEFT JOIN user_details ");
        queryBuilder.append("user_det_approval ON lcl_corr.approved_by = user_det_approval.user_id LEFT JOIN user_details user_det_posted ON ");
        queryBuilder.append("lcl_corr.posted_by = user_det_posted.user_id JOIN genericcode_dup gen_dup_type ON lcl_corr.TYPE = gen_dup_type.id ");
        queryBuilder.append("JOIN genericcode_dup gen_dup_code ON lcl_corr.CODE = gen_dup_code.id JOIN un_location org ON org.id = lclb.pol_id ");
        queryBuilder.append(" JOIN un_location dest ON dest.id = IF(lclb.fd_id  IS NOT NULL,lclb.fd_id ,lclb.pod_id) LEFT JOIN terminal t ON ");
        queryBuilder.append("(lclb.billing_terminal = t.trmnum) ");
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getFileNo())) {
            queryBuilder.append("WHERE file.file_number = '").append(lclCorrectionForm.getFileNo()).append("'");
            conditionAdded = true;
        } else if (CommonUtils.isNotEmpty(lclCorrectionForm.getBlNo())) {
            if (lclCorrectionForm.getBlNo().contains("-")) {
                String blNumber = lclCorrectionForm.getBlNo().substring(lclCorrectionForm.getBlNo().lastIndexOf("-") + 1, lclCorrectionForm.getBlNo().length());
                conditionAdded = true;
                queryBuilder.append("WHERE file.file_number = '").append(blNumber).append("'");
            }
        }
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getSearchCorrectionCode())) {
            if (conditionAdded) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" WHERE ");
            }
            conditionAdded = true;
            queryBuilder.append(" gen_dup_code.id = ").append(lclCorrectionForm.getSearchCorrectionCode());
        }
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getFilterBy()) && !lclCorrectionForm.getFilterBy().equalsIgnoreCase("All")) {
            if (conditionAdded) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" WHERE ");
            }
            conditionAdded = true;
            queryBuilder.append(" lcl_corr.status = '").append(lclCorrectionForm.getFilterBy()).append("'");
        }
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getNoticeNo())) {
            if (conditionAdded) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" WHERE ");
            }
            conditionAdded = true;
            queryBuilder.append(" lcl_corr.correction_no = ").append(lclCorrectionForm.getNoticeNo());
        }
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getCreatedByUserId())) {
            if (conditionAdded) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" WHERE ");
            }
            conditionAdded = true;
            queryBuilder.append(" lcl_corr.entered_by_user_id = ").append(lclCorrectionForm.getCreatedByUserId());
        }
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getApprovedByUserId())) {
            if (conditionAdded) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" WHERE ");
            }
            conditionAdded = true;
            queryBuilder.append(" lcl_corr.approved_by = ").append(lclCorrectionForm.getApprovedByUserId());
        }
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getSearchShipperNo())) {
            if (conditionAdded) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" WHERE ");
            }
            conditionAdded = true;
            queryBuilder.append(" lclb.ship_acct_no = '").append(lclCorrectionForm.getSearchShipperNo()).append("'");
        }
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getSearchForwarderNo())) {
            if (conditionAdded) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" WHERE ");
            }
            conditionAdded = true;
            queryBuilder.append(" lclb.fwd_acct_no = '").append(lclCorrectionForm.getSearchForwarderNo()).append("'");
        }
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getSearchThirdPartyAcctNo())) {
            if (conditionAdded) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" WHERE ");
            }
            conditionAdded = true;
            queryBuilder.append(" lclb.third_party_acct_no = '").append(lclCorrectionForm.getSearchThirdPartyAcctNo()).append("'");
        }
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getPortOfOriginId())) {
            if (conditionAdded) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" WHERE ");
            }
            conditionAdded = true;
            queryBuilder.append(" lclb.poo_id = ").append(lclCorrectionForm.getPortOfOriginId());
        }
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getPolId())) {
            if (conditionAdded) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" WHERE ");
            }
            conditionAdded = true;
            queryBuilder.append(" lclb.pol_id = ").append(lclCorrectionForm.getPolId());
        }
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getPodId())) {
            if (conditionAdded) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" WHERE ");
            }
            conditionAdded = true;
            queryBuilder.append(" lclb.pod_id = ").append(lclCorrectionForm.getPodId());
        }
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getFinalDestinationId())) {
            if (conditionAdded) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" WHERE ");
            }
            conditionAdded = true;
            queryBuilder.append(" lclb.fd_id = ").append(lclCorrectionForm.getFinalDestinationId());
        }
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getDateFilter())) {
            if (conditionAdded) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" WHERE ");
            }
            conditionAdded = true;
            queryBuilder.append(" ss_detail.std = '").append(DateUtils.parseDateToMYSQLFormat(lclCorrectionForm.getDateFilter())).append("'");
        }
        queryBuilder.append(" GROUP BY lcl_corr.id DESC");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(LCLCorrectionViewBean.class));
        query.addScalar("correctionId", LongType.INSTANCE);
        query.addScalar("noticeNo", LongType.INSTANCE);
        query.addScalar("userName", StringType.INSTANCE);
        query.addScalar("sailDate", DateType.INSTANCE);
        query.addScalar("prepaidCollect", StringType.INSTANCE);
        query.addScalar("noticeDate", StringType.INSTANCE);
        query.addScalar("correctionType", StringType.INSTANCE);
        query.addScalar("correctionCode", StringType.INSTANCE);
        query.addScalar("status", StringType.INSTANCE);
        query.addScalar("approval", StringType.INSTANCE);
        query.addScalar("blNo", StringType.INSTANCE);
        query.addScalar("fileNo", StringType.INSTANCE);
        query.addScalar("fileId", LongType.INSTANCE);
        query.addScalar("postedDate", StringType.INSTANCE);
        query.addScalar("voidStatus", IntegerType.INSTANCE);
        query.addScalar("fileState", StringType.INSTANCE);
        query.addScalar("currentProfit", StringType.INSTANCE);
        query.addScalar("profitAfterCN", StringType.INSTANCE);
        return query.list();
    }

    public Integer getCorrectionCountByFileId(Long fileId) throws Exception {
        BigInteger count = new BigInteger("0");
        String queryString = "SELECT COUNT(*) FROM lcl_correction WHERE file_number_id = " + fileId;
        Query queryObject = getSession().createSQLQuery(queryString);
        Object o = queryObject.uniqueResult();
        if (o != null) {
            count = (BigInteger) o;
        }
        return count.intValue();
    }

    public Integer getVoidedCorrectionCountByFileId(Long fileId, String voidValue) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        BigInteger count = new BigInteger("0");
        queryBuilder.append("SELECT COUNT(*) FROM lcl_correction WHERE file_number_id = ");
        queryBuilder.append(fileId);
        queryBuilder.append(" AND void = ");
        queryBuilder.append(voidValue);
        Query queryObject = getSession().createSQLQuery(queryBuilder.toString());
        Object o = queryObject.uniqueResult();
        if (o != null) {
            count = (BigInteger) o;
        }
        return count.intValue();
    }

    public Integer getApprovedCorrectionCountByFileId(Long fileId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        BigInteger count = new BigInteger("0");
        queryBuilder.append("SELECT COUNT(*) FROM lcl_correction WHERE file_number_id = ");
        queryBuilder.append(fileId);
        queryBuilder.append(" AND status = 'A'");
        Query queryObject = getSession().createSQLQuery(queryBuilder.toString());
        Object o = queryObject.uniqueResult();
        if (o != null) {
            count = (BigInteger) o;
        }
        return count.intValue();
    }

    public Integer getCountByBillToPartyAcct(Long fileId, String acctNo, String billToParty) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        BigInteger count = new BigInteger("0");
        queryBuilder.append("SELECT COUNT(*) FROM lcl_correction WHERE file_number_id = ");
        queryBuilder.append(fileId);
        queryBuilder.append(" AND status = 'A' and ");
        if (!billToParty.equalsIgnoreCase("A")) {
            queryBuilder.append("party_acct_no");
        } else {
            queryBuilder.append("cons_acct_no");
        }
        queryBuilder.append(" = '");
        queryBuilder.append(acctNo);
        queryBuilder.append("'");
        Query queryObject = getSession().createSQLQuery(queryBuilder.toString());
        Object o = queryObject.uniqueResult();
        if (o != null) {
            count = (BigInteger) o;
        }
        return count.intValue();
    }

    public Integer getVoidedCorrectionCountByFileIds(String fileIds, String voidValue) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        BigInteger count = new BigInteger("0");
        queryBuilder.append("SELECT COUNT(*) FROM lcl_correction WHERE file_number_id IN(");
        queryBuilder.append(fileIds);
        queryBuilder.append(") AND void = ");
        queryBuilder.append(voidValue);
        Query queryObject = getSession().createSQLQuery(queryBuilder.toString());
        Object o = queryObject.uniqueResult();
        if (o != null) {
            count = (BigInteger) o;
        }
        return count.intValue();
    }

    public String getAllCorrectionIds(Long unitSSId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        String correctionIds = null;
        queryBuilder.append("SELECT GROUP_CONCAT(lcl_corr.id ORDER BY lcl_corr.id DESC) AS correctionIds ");
        queryBuilder.append("FROM lcl_unit_ss unit_ss JOIN lcl_booking_piece_unit unit_pieces ON unit_pieces.lcl_unit_ss_id = unit_ss.id ");
        queryBuilder.append("JOIN lcl_booking_piece book_piece ON unit_pieces.booking_piece_id = book_piece.id JOIN lcl_file_number FILE ON ");
        queryBuilder.append("book_piece.file_number_id = file.id LEFT JOIN lcl_correction lcl_corr ON file.id = lcl_corr.file_number_id  AND ");
        queryBuilder.append("lcl_corr.approved_by IS NOT NULL WHERE unit_ss.id = ");
        queryBuilder.append(unitSSId);
        Query queryObject = getSession().createSQLQuery(queryBuilder.toString());
        Object o = queryObject.uniqueResult();
        if (o != null) {
            correctionIds = (String) o;
        }
        return correctionIds;
    }

    public String isCorrection(Long fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT IF(COUNT(*)>0,'true','false') FROM lcl_correction");
        sb.append(" WHERE file_number_id='").append(fileId).append("' LIMIT 1");
        return (String) getCurrentSession().createSQLQuery(sb.toString()).uniqueResult();
    }

    public Integer getChargeCodeByCorrection(Long fileId, int arGlMappingId, Double arAmt) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT lcc.gl_mapping_id AS arGlId FROM lcl_correction lc JOIN lcl_correction_charge lcc ON lcc.correction_id=lc.id ");
        sb.append(" WHERE lc.file_number_id=").append(fileId).append(" AND lcc.gl_mapping_id =").append(arGlMappingId);
        sb.append(" AND lcc.old_amount=").append(arAmt).append(" LIMIT 1");
        return (Integer) getCurrentSession().createSQLQuery(sb.toString()).uniqueResult();
    }

    public String getFileType(String fileNo) throws Exception {
        String q = "select b.booking_type from lcl_file_number f join lcl_booking b  on(b.file_number_id=f.id) where f.file_number='" + fileNo + "' limit 1";
        String fileType = "";
        if (getCurrentSession().createSQLQuery(q).uniqueResult() != null) {
            fileType = getCurrentSession().createSQLQuery(q).uniqueResult().toString();
        }
        return fileType;
    }

    public Long getLclCorrectionId(String fileNo, String correctionNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  c.id as correctionId ");
        queryBuilder.append("from");
        queryBuilder.append("  lcl_file_number f");
        queryBuilder.append("  join lcl_correction c ");
        queryBuilder.append("    on (");
        queryBuilder.append("      f.id = c.file_number_id");
        queryBuilder.append("      and c.correction_no = '").append(correctionNo).append("'");
        queryBuilder.append("    ) ");
        queryBuilder.append("where");
        queryBuilder.append("  f.file_number = '").append(fileNo).append("'");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("correctionId", LongType.INSTANCE);
        return (Long) query.uniqueResult();
    }

    public void saveCorrections(Long fileId, Integer type, Integer code, Integer correctionNo,
            String comments, String status, String debitEMail, String creditEMail, Integer userId, Integer voidNo, LclFileNumber lclFileNumber) throws Exception {
        StringBuilder sb = new StringBuilder();
        Date d = new Date();
        sb.append("INSERT INTO lcl_correction (");
        sb.append("  file_number_id,");
        sb.append("  TYPE,");
        sb.append("  CODE,");
        sb.append("  cons_acct_no,");
        sb.append("  correction_no,");
        sb.append("  comments,");
        sb.append("  STATUS,");
        sb.append("  debit_email,");
        sb.append("  credit_email,");
        sb.append("  void,");
        sb.append("  entered_datetime,");
        sb.append("  entered_by_user_id,");
        sb.append("  modified_datetime,");
        sb.append("  modified_by_user_id");
        sb.append(") ");
        sb.append(" VALUES(:fileId,:type,:code,:consAcctNo,:correctionNo,:comments,:status,:debitEmail,");
        sb.append(":creditEmail,:void,:enteredDatetime,:enteredByUserId,:modifiedDetime,:modifiedByUserId)");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setLong("fileId", fileId);
        queryObject.setInteger("type", type);
        queryObject.setInteger("code", code);
        queryObject.setString("consAcctNo", lclFileNumber.getLclBooking().getConsAcct().getAccountno());
        queryObject.setInteger("correctionNo", correctionNo);
        queryObject.setString("comments", comments);
        queryObject.setString("status", status);
        queryObject.setString("debitEmail", debitEMail);
        queryObject.setString("creditEmail", creditEMail);
        queryObject.setInteger("void", voidNo);
        queryObject.setDate("enteredDatetime", d);
        queryObject.setInteger("enteredByUserId", userId);
        queryObject.setDate("modifiedDetime", d);
        queryObject.setInteger("modifiedByUserId", userId);
        queryObject.executeUpdate();
    }

    public LCLCorrectionNoticeBean getExportCorrectionFileds(Long correctionId, Long fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT  SUBSTRING(BlNumberSystemForLclExports(lf.id),6) AS blNo, ");
        sb.append(" UnLocationGetNameStateCntryByID(org.id) AS origin, UnLocationGetNameStateCntryByID(dest.id) AS destination, ");
        sb.append(" ss_head.schedule_no AS voyageNumber, unit.unit_no AS unitNo, ");
        sb.append(" CONCAT( IF(ss_head.billing_trmnum IS NULL,'',CONCAT(ss_head.billing_trmnum,'-')), ");
        sb.append(" org.un_loc_code,'-',dest.un_loc_code,'-',ss_head.schedule_no) AS eciShipmentFileNo, ");
        sb.append(" ss_detail.sp_reference_name AS vesselName,ss_detail.std AS sailDate,ship.acct_name AS shipperName, ");
        sb.append(" fwd.acct_name AS forwarderName,cons.acct_name AS consigneeName,bl.bill_to_party AS billToParty, ");
        sb.append(" CASE WHEN bl.bill_to_party = 'F' THEN fwd.acct_name WHEN bl.bill_to_party = 'S' THEN ship.acct_name ");
        sb.append(" WHEN bl.bill_to_party = 'A' THEN agent.acct_name WHEN bl.bill_to_party = 'T' THEN third.acct_name ");
        sb.append(" ELSE NULL END AS customer,  ");
        sb.append(" CASE WHEN bl.bill_to_party = 'F' THEN bl.fwd_acct_no ");
        sb.append(" WHEN bl.bill_to_party = 'S' THEN bl.ship_acct_no WHEN bl.bill_to_party = 'A' THEN bl.agent_Acct_no ");
        sb.append(" ELSE NULL END AS customerAcctNo,  ");
        sb.append(" co.comments AS comments,co.entered_datetime AS correctionDate,  ");
        sb.append(" ctype.code AS strCorrectionType,CONCAT(ctype.code,'-',ctype.codedesc) AS correctionTypeValue,  ");
        sb.append(" CONCAT(ccode.code,' ',ccode.codedesc) AS correctionCodeValue,  ");
        sb.append(" DATE_FORMAT(co.posted_date,'%d-%b-%Y %T %p') AS postedDate,  ");
        sb.append(" co.status AS correctionStatus, co.void AS voidStatus, ");
        sb.append(" (SELECT contact_name FROM cust_address WHERE acct_no = customerAcctNo LIMIT 1) AS custContact  ");
        sb.append(" FROM lcl_file_number lf JOIN lcl_bl bl ON bl.file_number_id = lf.id JOIN lcl_booking_piece piece ON piece.`file_number_id` = lf.`id` ");
        sb.append(" JOIN lcl_booking_piece_unit u_piece ON u_piece.booking_piece_id = piece.id ");
        sb.append(" JOIN lcl_unit_ss luss ON luss.id = u_piece.lcl_unit_ss_id  JOIN lcl_unit unit ON unit.id = luss.unit_id ");
        sb.append(" JOIN lcl_ss_header ss_head ON luss.ss_header_id = ss_head.id  ");
        sb.append(" LEFT JOIN lcl_ss_detail ss_detail ON ss_head.id = ss_detail.ss_header_id AND ss_detail.trans_mode = 'V' ");
        sb.append(" JOIN un_location org ON org.id = bl.`poo_id` JOIN un_location dest ON dest.id = bl.`pod_id` ");
        sb.append(" LEFT JOIN trading_partner ship ON bl.ship_acct_no = ship.acct_no LEFT JOIN trading_partner fwd ON bl.fwd_acct_no = fwd.acct_no ");
        sb.append(" LEFT JOIN trading_partner cons ON bl.cons_acct_no = cons.acct_no ");
        sb.append(" LEFT JOIN trading_partner agent ON bl.agent_acct_no =agent.acct_no ");
        sb.append(" LEFT JOIN trading_partner third ON bl.third_party_acct_no =third.acct_no ");
        sb.append(" JOIN lcl_correction co ON co.file_number_id = lf.id AND co.id =:correctionId ");
        sb.append(" JOIN genericcode_dup ctype ON ctype.id =  co.type JOIN genericcode_dup ccode ON ccode.id = co.CODE ");
        sb.append(" WHERE lf.id =:fileId ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("fileId", fileId);
        query.setParameter("correctionId", correctionId);
        query.setResultTransformer(Transformers.aliasToBean(LCLCorrectionNoticeBean.class));
        query.addScalar("blNo", StringType.INSTANCE);
        query.addScalar("origin", StringType.INSTANCE);
        query.addScalar("destination", StringType.INSTANCE);
        query.addScalar("voyageNumber", StringType.INSTANCE);
        query.addScalar("unitNo", StringType.INSTANCE);
        query.addScalar("eciShipmentFileNo", StringType.INSTANCE);
        query.addScalar("sailDate", DateType.INSTANCE);
        query.addScalar("correctionDate", DateType.INSTANCE);
        query.addScalar("vesselName", StringType.INSTANCE);
        query.addScalar("shipperName", StringType.INSTANCE);
        query.addScalar("forwarderName", StringType.INSTANCE);
        query.addScalar("consigneeName", StringType.INSTANCE);
        query.addScalar("comments", StringType.INSTANCE);
        query.addScalar("billToParty", StringType.INSTANCE);
        query.addScalar("strCorrectionType", StringType.INSTANCE);
        query.addScalar("customer", StringType.INSTANCE);
        query.addScalar("customerAcctNo", StringType.INSTANCE);
        query.addScalar("correctionTypeValue", StringType.INSTANCE);
        query.addScalar("correctionCodeValue", StringType.INSTANCE);
        query.addScalar("correctionStatus", StringType.INSTANCE);
        query.addScalar("voidStatus", IntegerType.INSTANCE);
        query.addScalar("postedDate", StringType.INSTANCE);
        query.addScalar("custContact", StringType.INSTANCE);
        query.setMaxResults(1);
        return (LCLCorrectionNoticeBean) query.uniqueResult();
    }

    public void updateCorrectionByField(Long correctionId, String columnName, String value) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" update lcl_correction set ").append(columnName).append("=:value where id=:correctionId");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("value", value);
        query.setParameter("correctionId", correctionId);
        query.executeUpdate();
    }

    public String getCorrectedBillToPartyListForExport(Long file_id, Long correctionId) throws Exception {
        Query queryObject = getSession().createSQLQuery("SELECT  group_concat(distinct CASE lcc.bill_to_party  "
                + " WHEN 's' THEN 'Shipper' WHEN 'F' THEN 'Forwarder' WHEN 'A' THEN 'Agent' WHEN 'T' THEN 'Third Party' "
                + " END ) AS billToParty  from lcl_correction_charge lcc JOIN lcl_correction lc  ON (lc.id = lcc.`correction_id` "
                + " and  lc.id=:correctionId) WHERE lc.`file_number_id`=:fileId ");
        queryObject.setParameter("fileId", file_id);
        queryObject.setParameter("correctionId", correctionId);
        return null != queryObject.uniqueResult() ? queryObject.uniqueResult().toString() : "";
    }
     public List<Object[]> getCorrectionCharge(Long file_number_id) throws Exception {
         StringBuilder queryBuilder = new StringBuilder();
         queryBuilder.append("SELECT lcc.new_amount As new_amount,lcc.`bill_to_party` FROM lcl_correction_charge lcc WHERE lcc.correction_id =(SELECT id FROM `lcl_correction` lc WHERE lc.`file_number_id`=");
         queryBuilder.append(file_number_id);
         queryBuilder.append(" ORDER BY id DESC LIMIT 1)");
         SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
         query.addScalar("new_amount",  DoubleType.INSTANCE);
         query.addScalar("bill_to_party", StringType.INSTANCE);
         return (List<Object[]>) query.list();
   }
}

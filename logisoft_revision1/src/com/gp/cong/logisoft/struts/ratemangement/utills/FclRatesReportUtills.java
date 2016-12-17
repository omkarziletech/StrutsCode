/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.ratemangement.utills;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.struts.ratemangement.form.SearchFCLForm;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.reports.dto.FclRatesReportDTO;
import java.util.List;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;

/**
 *
 * @author Mei
 */
public class FclRatesReportUtills {

    String databaseSchema;

    public FclRatesReportUtills() {
        try {
            databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<FclRatesReportDTO> getFclRates(SearchFCLForm searchFCLForm) {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("SELECT crt.grorg as regionOrigin,crt.grdst as regionDestination,origin.locnam AS originName,");
        strQuery.append("crt.origin as originSchnum,f.extcty as polSchnum,f.seapod as podSchnum,");
        strQuery.append("fd.locnam AS fdName,crt.destin as fdSchnum,f.trnday as transitDays,f.remark as remarks, t.linnam AS carrierName,");
        strQuery.append("t.linnum as ssLineNum,cost.abrvds as costCodeDesc,crt.a20 as a20Amt,crt.b40 as b40Amt,crt.c40hc as c40hcAmt,");
        strQuery.append("crt.d45 as d45Amt,crt.e48 as e48Amt,crt.f40nor as f40norAmt,crt.g45102 as g45102Amt,f.hazamt as hazamtAmt, ");
        strQuery.append("crt.mrkupa AS mrkupa,crt.mrkupb AS mrkupb, crt.mrkupc AS mrkupc,");
        strQuery.append("crt.mrkupd AS mrkupd,crt.mrkupe AS mrkupe,crt.mrkupf AS mrkupf,crt.mrkupg AS mrkupg");
        strQuery.append(appendFromQuery(searchFCLForm));
        SQLQuery query = new BaseHibernateDAO().getCurrentSession().createSQLQuery(strQuery.toString());
        query.setResultTransformer(Transformers.aliasToBean(FclRatesReportDTO.class));
        query.addScalar("regionOrigin", StringType.INSTANCE);
        query.addScalar("regionDestination", StringType.INSTANCE);
        query.addScalar("originName", StringType.INSTANCE);
        query.addScalar("originSchnum", StringType.INSTANCE);
        query.addScalar("polSchnum", StringType.INSTANCE);
        query.addScalar("podSchnum", StringType.INSTANCE);
        query.addScalar("fdName", StringType.INSTANCE);
        query.addScalar("fdSchnum", StringType.INSTANCE);
        query.addScalar("transitDays", StringType.INSTANCE);
        query.addScalar("remarks", StringType.INSTANCE);
        query.addScalar("carrierName", StringType.INSTANCE);
        query.addScalar("ssLineNum", StringType.INSTANCE);
        query.addScalar("costCodeDesc", StringType.INSTANCE);
        query.addScalar("a20Amt", StringType.INSTANCE);
        query.addScalar("b40Amt", StringType.INSTANCE);
        query.addScalar("c40hcAmt", StringType.INSTANCE);
        query.addScalar("d45Amt", StringType.INSTANCE);
        query.addScalar("e48Amt", StringType.INSTANCE);
        query.addScalar("f40norAmt", StringType.INSTANCE);
        query.addScalar("g45102Amt", StringType.INSTANCE);
        query.addScalar("hazamtAmt", StringType.INSTANCE);
        query.addScalar("mrkupa", StringType.INSTANCE);
        query.addScalar("mrkupb", StringType.INSTANCE);
        query.addScalar("mrkupc", StringType.INSTANCE);
        query.addScalar("mrkupd", StringType.INSTANCE);
        query.addScalar("mrkupe", StringType.INSTANCE);
        query.addScalar("mrkupf", StringType.INSTANCE);
        query.addScalar("mrkupg", StringType.INSTANCE);
        return query.list();
    }

//    public List<FclRatesReportDTO> setReportRateLogic(List<FclRatesReportDTO> reportRateList) {
//        List<FclRatesReportDTO> ratesList = new ArrayList();
//        List<FclRatesReportDTO> chargeCodeList = getChargeCode();
//        List<FclRatesReportDTO> originList = getOriginDetails();
//        Map<String, List<FclRatesReportDTO>> rateMap = new LinkedHashMap();
//        for (FclRatesReportDTO chargeCode : chargeCodeList) {
//            for (FclRatesReportDTO rate : reportRateList) {
//            }
//        }
//        return ratesList;
//    }
    public String appendFromQuery(SearchFCLForm searchFCLForm) {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" FROM ").append(databaseSchema);
        strQuery.append(".fclcrt crt JOIN ");
        strQuery.append(databaseSchema).append(".sched origin ON origin.schnum=crt.origin AND origin.cntnum=1 JOIN ");
        strQuery.append(databaseSchema);
        strQuery.append(".sched fd ON fd.schnum=crt.destin AND fd.cntnum=1 JOIN ");
        strQuery.append(databaseSchema).append(".tlines t ON t.linnum=crt.ssline JOIN ");
        strQuery.append(databaseSchema).append(".fclcod cost ON cost.fclcod=crt.costcd LEFT JOIN  ");
        strQuery.append(databaseSchema).append(".fclstd f ON crt.origin=f.trmnum AND crt.destin=f.prtnum AND crt.ssline=f.ssline ");
        strQuery.append(" WHERE ");
        if (CommonUtils.isNotEmpty(searchFCLForm.getOriginSchnum())) {
            strQuery.append(" crt.origin='").append(searchFCLForm.getOriginSchnum()).append("'");
        } else if (CommonUtils.isNotEmpty(searchFCLForm.getOrgRegion())) {
            strQuery.append("crt.grorg IN(").append(searchFCLForm.getOrgRegion()).append(") ");
        }

        if (CommonUtils.isNotEmpty(searchFCLForm.getDestinationSchnum())) {
            strQuery.append(" AND crt.destin='").append(searchFCLForm.getDestinationSchnum().replaceAll(",", "")).append("'");
        } else if (CommonUtils.isNotEmpty(searchFCLForm.getDestRegion())) {
            strQuery.append(" AND crt.grdst IN(").append(searchFCLForm.getDestRegion()).append(")"); //.append(" crt.origin=02811 AND crt.destin=20107 ")
        }

        //strQuery.append("AND (crt.comnum = '017900' OR comnum='006100')");
        strQuery.append(" AND crt.comnum = '").append(searchFCLForm.getCommodityNumber()).append("' ");
        strQuery.append(" AND t.linnum=").append(searchFCLForm.getSslinenumber());
        return strQuery.toString();
    }

    public List<FclRatesReportDTO> getOriginList(SearchFCLForm searchFCLForm) {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("SELECT  DISTINCT origin.locnam AS originName ").append(appendFromQuery(searchFCLForm));
        SQLQuery query = new BaseHibernateDAO().getCurrentSession().createSQLQuery(strQuery.toString());
        query.setResultTransformer(Transformers.aliasToBean(FclRatesReportDTO.class));
        query.addScalar("originName", StringType.INSTANCE);
        return query.list();
    }

    public List<FclRatesReportDTO> getChargeCodeList(SearchFCLForm searchFCLForm) {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("SELECT DISTINCT cost.abrvds as costCodeDesc ");
        strQuery.append(appendFromQuery(searchFCLForm));
        strQuery.append(" ORDER BY cost.fclcod ASC ");
        SQLQuery query = new BaseHibernateDAO().getCurrentSession().createSQLQuery(strQuery.toString());
        query.setResultTransformer(Transformers.aliasToBean(FclRatesReportDTO.class));
        query.addScalar("costCodeDesc", StringType.INSTANCE);
        return query.list();
    }

    public List<FclRatesReportDTO> getContainerSize(SearchFCLForm searchFCLForm) {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("SELECT SUM(crt.a20) AS a20Amt,SUM(crt.b40) as b40Amt, ");
        strQuery.append(" SUM(crt.c40hc) as c40hcAmt,SUM(crt.d45) as d45Amt,SUM(crt.e48) as e48Amt, ");
        strQuery.append(" SUM(crt.f40nor) as f40norAmt,SUM(crt.g45102) as g45102Amt, ");
        strQuery.append("SUM(crt.mrkupa) AS mrkupa,SUM(crt.mrkupb) AS mrkupb, SUM(crt.mrkupc) AS mrkupc,");
        strQuery.append("SUM(crt.mrkupd) AS mrkupd,SUM(crt.mrkupe) AS mrkupe,SUM(crt.mrkupf) AS mrkupf,SUM(crt.mrkupg) AS mrkupg ");
        strQuery.append(appendFromQuery(searchFCLForm));
        strQuery.append(" GROUP BY crt.grorg,crt.grdst ");
        SQLQuery query = new BaseHibernateDAO().getCurrentSession().createSQLQuery(strQuery.toString());
        query.setResultTransformer(Transformers.aliasToBean(FclRatesReportDTO.class));
        query.addScalar("a20Amt", StringType.INSTANCE);
        query.addScalar("b40Amt", StringType.INSTANCE);
        query.addScalar("c40hcAmt", StringType.INSTANCE);
        query.addScalar("d45Amt", StringType.INSTANCE);
        query.addScalar("e48Amt", StringType.INSTANCE);
        query.addScalar("f40norAmt", StringType.INSTANCE);
        query.addScalar("g45102Amt", StringType.INSTANCE);
        query.addScalar("mrkupa", StringType.INSTANCE);
        query.addScalar("mrkupb", StringType.INSTANCE);
        query.addScalar("mrkupc", StringType.INSTANCE);
        query.addScalar("mrkupd", StringType.INSTANCE);
        query.addScalar("mrkupe", StringType.INSTANCE);
        query.addScalar("mrkupf", StringType.INSTANCE);
        query.addScalar("mrkupg", StringType.INSTANCE);
        return query.list();
    }
}

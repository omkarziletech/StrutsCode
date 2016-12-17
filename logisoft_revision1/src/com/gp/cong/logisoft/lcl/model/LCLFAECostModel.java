package com.gp.cong.logisoft.lcl.model;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.domain.lcl.LclSsExports;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import java.util.LinkedHashMap;
import java.util.List;
import org.hibernate.SQLQuery;

public class LCLFAECostModel extends BaseHibernateDAO {

    private double blTotalRetailCBM = 0.00;
    private double blTotalCTCCBM = 0.00;
    private double blTotalFTFCBM = 0.00;

    private double blTotalRetailCFT = 0.00;
    private double blTotalCTCCFT = 0.00;
    private double blTotalFTFCFT = 0.00;

    private double blTotalRetailOFT = 0.00;
    private double blTotalCTCOFT = 0.00;
    private double blTotalFTFOFT = 0.00;

    private double blTotalRetailCollect = 0.00;
    private double blTotalCTCCollect = 0.00;
    private double blTotalFTFCollect = 0.00;

    private String dbName;

    private String chargeCode;
    private String bluescreenCharge;
    private String costAmount;
    private String vendorNo;
    private String chargeType;
    private int costACId;

    private LinkedHashMap<String, LCLFAECostModel> faeCostList = new LinkedHashMap<String, LCLFAECostModel>();
    private LclUnitSs unitSs;
    private Long headerId;

    public LCLFAECostModel() {
    }

    public LCLFAECostModel getBlData(Long unitSsId, Long headerId) throws Exception {
        unitSs = new LclUnitSsDAO().findById(unitSsId);
        LCLFAECostModel model = new LCLFAECostModel();
        this.headerId = headerId;
        this.dbName = LoadLogisoftProperties.getProperty("elite.database.name");
        double[] retailValue = this.getBLCFTCBMValues(unitSsId, "R");
        this.blTotalRetailCFT = retailValue[0];
        this.blTotalRetailCBM = retailValue[1];
        double[] ctcValue = this.getBLCFTCBMValues(unitSsId, "C");
        this.blTotalCTCCFT = ctcValue[0];
        this.blTotalCTCCBM = ctcValue[1];
        double[] ftfValue = this.getBLCFTCBMValues(unitSsId, "F");
        this.blTotalFTFCFT = ftfValue[0];
        this.blTotalFTFCBM = ftfValue[1];

        this.blTotalRetailOFT = this.getSumOFOceanFreightCharge(unitSsId, "R");
        this.blTotalCTCOFT = this.getSumOFOceanFreightCharge(unitSsId, "C");
        this.blTotalFTFOFT = this.getSumOFOceanFreightCharge(unitSsId, "F");

        this.blTotalRetailCollect = this.getSumOFCollectCharge(unitSsId, "R");
        this.blTotalCTCCollect = this.getSumOFCollectCharge(unitSsId, "C");
        this.blTotalFTFCollect = this.getSumOFCollectCharge(unitSsId, "F");

        String originCode = unitSs.getLclSsHeader().getOrigin().getUnLocationCode();
        String destCode = unitSs.getLclSsHeader().getDestination().getUnLocationCode();

        model.setBlTotalCTCCBM(blTotalCTCCBM);
        model.setBlTotalCTCCFT(blTotalCTCCFT);
        model.setBlTotalCTCCollect(blTotalCTCCollect);
        model.setBlTotalCTCOFT(blTotalCTCOFT);

        model.setBlTotalRetailCBM(blTotalRetailCBM);
        model.setBlTotalRetailCFT(blTotalRetailCFT);
        model.setBlTotalRetailCollect(blTotalRetailCollect);
        model.setBlTotalRetailOFT(blTotalRetailOFT);

        model.setBlTotalFTFCBM(blTotalFTFCBM);
        model.setBlTotalFTFCFT(blTotalFTFCFT);
        model.setBlTotalFTFCollect(blTotalFTFCollect);
        model.setBlTotalFTFOFT(blTotalFTFOFT);
        model.setHeaderId(headerId);
        model.setUnitSs(unitSs);
        LclSsExports sSExports = unitSs.getLclSsHeader().getLclSsExports();

        model.setVendorNo(null != sSExports && null != sSExports.getExportAgentAcctoNo()
                ? sSExports.getExportAgentAcctoNo().getAccountno() : "");

        this.getFAECostType_A(model, originCode, destCode);
        this.getFAECostType_B(model, originCode, destCode);
        this.getFAECostType_C(model, originCode, destCode);
        this.getFAECostType_D(model, originCode, destCode);
        this.getFAECostType_E(model, originCode, destCode);
        this.getFAECostType_F(model, originCode, destCode);
        this.getFAECostType_G(model, originCode, destCode);
        this.getFAECostType_H(model, originCode, destCode);
        model.setFaeCostList(faeCostList);
        return model;
    }

    public double[] getBLCFTCBMValues(Long unitSsId, String rateType) throws Exception {
        double[] value = new double[2];
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT SUM(cft) AS cft, SUM(cbm) AS cbm  FROM( SELECT ");
        sb.append(" (SELECT SUM(actual_volume_imperial) FROM lcl_bl_piece WHERE file_number_id = lf.id) AS cft, ");
        sb.append(" (SELECT SUM(actual_volume_metric) FROM lcl_bl_piece WHERE file_number_id = lf.id) AS cbm ");
        sb.append(" FROM ");
        sb.append(" lcl_booking_piece bp ");
        sb.append(" JOIN lcl_file_number lf ON lf.`id` = getHouseBLForConsolidateDr(bp.`file_number_id`) ");
        sb.append(" JOIN lcl_bl_piece blp ON blp.`file_number_id` = lf.`id` ");
        sb.append(" JOIN lcl_bl bl ON bl.`file_number_id` = lf.`id` ");
        sb.append(" JOIN lcl_booking_piece_unit bpu ON bpu.`booking_piece_id` = bp.id ");
        sb.append(" JOIN lcl_unit_ss lus ON lus.`id` = bpu.`lcl_unit_ss_id` ");
        sb.append(" WHERE lus.id =:unitSsId AND  bl.`rate_type` =:rateType GROUP BY lf.`id` ) AS fn");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("unitSsId", unitSsId);
        query.setParameter("rateType", rateType);
        List<Object[]> obj = query.list();
        if (CommonUtils.isNotEmpty(obj)) {
            for (Object[] row : obj) {
                value[0] = null != row[0] && !"".equalsIgnoreCase(row[0].toString()) ? Double.parseDouble(row[0].toString()) : 0.00;
                value[1] = null != row[1] && !"".equalsIgnoreCase(row[1].toString()) ? Double.parseDouble(row[1].toString()) : 0.00;
            }
        }
        return value;
    }

    public double getSumOFOceanFreightCharge(Long unitSsId, String rateType) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT SUM(filter.ar_amount + adjustment_Amount) AS ar_amount ");
        sb.append(" FROM (SELECT  ac.`ar_amount` AS ar_amount, ac.`adjustment_amount` AS adjustment_Amount ");
        sb.append(" FROM (SELECT  lf.id AS fileId  FROM lcl_booking_piece bp ");
        sb.append(" JOIN lcl_file_number lf ");
        sb.append(" ON lf.`id` = getHouseBLForConsolidateDr(bp.`file_number_id`) ");
        sb.append(" JOIN lcl_bl bl ");
        sb.append("  ON bl.`file_number_id` = lf.`id` ");
        sb.append(" JOIN lcl_booking_piece_unit bpu ");
        sb.append(" ON bpu.`booking_piece_id` = bp.id ");
        sb.append(" JOIN lcl_unit_ss lus ");
        sb.append("  ON lus.`id` = bpu.`lcl_unit_ss_id` ");
        sb.append(" WHERE lus.id =:unitSsId ");
        sb.append("   AND bl.`rate_type` = :rateType ");
        sb.append(" GROUP BY lf.`id`) AS fn ");
        sb.append(" JOIN lcl_bl_ac ac ");
        sb.append("  ON ac.`file_number_id` = fn.fileId ");
        sb.append(" JOIN gl_mapping gl ");
        sb.append("  ON gl.id = ac.`ar_gl_mapping_id` ");
        sb.append("  AND gl.`Charge_code` = 'OCNFRT' ");
        sb.append(" GROUP BY gl.`Charge_code`, fn.fileId ) AS filter ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("unitSsId", unitSsId);
        query.setParameter("rateType", rateType);
        Object obj = query.uniqueResult();
        return obj != null && !"".equalsIgnoreCase(obj.toString()) ? Double.parseDouble(obj.toString()) : 0.00;
    }

    public double getSumOFCollectCharge(Long unitSsId, String rateType) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT SUM(filter.ar_amount + adjustment_Amount) AS ar_amount ");
        sb.append(" FROM (SELECT  ac.`ar_amount` AS ar_amount, ac.`adjustment_amount` AS adjustment_Amount ");
        sb.append(" FROM (SELECT  lf.id AS fileId  FROM lcl_booking_piece bp ");
        sb.append(" JOIN lcl_file_number lf ");
        sb.append(" ON lf.`id` = getHouseBLForConsolidateDr(bp.`file_number_id`) ");
        sb.append(" JOIN lcl_bl bl ");
        sb.append("  ON bl.`file_number_id` = lf.`id` ");
        sb.append(" JOIN lcl_booking_piece_unit bpu ");
        sb.append(" ON bpu.`booking_piece_id` = bp.id ");
        sb.append(" JOIN lcl_unit_ss lus ");
        sb.append("  ON lus.`id` = bpu.`lcl_unit_ss_id` ");
        sb.append(" WHERE lus.id =:unitSsId ");
        sb.append("   AND bl.`rate_type` = :rateType ");
        sb.append(" GROUP BY lf.`id`) AS fn ");
        sb.append(" JOIN lcl_bl_ac ac ");
        sb.append("  ON ac.`file_number_id` = fn.fileId ");
        sb.append(" JOIN gl_mapping gl ");
        sb.append("  ON gl.id = ac.`ar_gl_mapping_id` ");
        sb.append("  AND gl.`Charge_code` <> 'OCNFRT' ");
        sb.append("  AND ac.`ar_bill_to_party` = 'A' ");
        sb.append(" GROUP BY gl.`Charge_code`,fn.fileId ) AS filter ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("unitSsId", unitSsId);
        query.setParameter("rateType", rateType);
        Object obj = query.uniqueResult();
        return obj != null && !"".equalsIgnoreCase(obj.toString()) ? Double.parseDouble(obj.toString()) : 0.00;
    }

    public void getFAECostType_A(LCLFAECostModel model, String origin, String destination) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append(" gl.charge_code AS chargeCode, gl.id, ");
        sb.append(" f.`faeccd`, f.`typea1`, f.`typea2`, f.`typea3` FROM ").append(this.dbName).append(".`faecrt` f ");
        sb.append(" join gl_mapping gl on gl.bluescreen_chargeCode = f.`faeccd` ");
        sb.append(" WHERE trmnum = (SELECT trmnum FROM terminal ");
        sb.append(" WHERE unlocationcode1=:origin LIMIT 1) AND ");
        sb.append(" prtnum = (SELECT eciportcode FROM ports WHERE ");
        sb.append("  unlocationcode =:destination ) AND f.`chgtyp` ='A' ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("origin", origin);
        query.setParameter("destination", destination);
        List<Object[]> obj = query.list();
        double retailValue = 0.00, ctcValue = 0.00, ftfValue = 0.00;
        double totalCost = 0.00;
        if (CommonUtils.isNotEmpty(obj)) {
            for (Object[] row : obj) {
                model = new LCLFAECostModel();
                model.setChargeType("A");
                model.setChargeCode(null != row[0] ? row[0].toString() : "");
                model.setCostACId(null != row[1] ? Integer.parseInt(row[1].toString()) : 0);
                model.setBluescreenCharge(null != row[2] ? row[2].toString() : "");
                retailValue = null != row[3] ? Double.parseDouble(row[3].toString()) : retailValue;
                retailValue *= this.blTotalRetailOFT;
                ctcValue = null != row[4] ? Double.parseDouble(row[4].toString()) : ctcValue;
                ctcValue *= this.blTotalCTCOFT;
                ftfValue = null != row[5] ? Double.parseDouble(row[5].toString()) : ftfValue;
                ftfValue *= this.blTotalFTFOFT;
                totalCost = retailValue + ctcValue + ftfValue;
                model.setCostAmount(NumberUtils.convertToThreeDecimal(totalCost));
                faeCostList.put(model.getChargeCode() + "#" + model.getBluescreenCharge(), model);
            }
        }
    }

    public void getFAECostType_B(LCLFAECostModel model, String origin, String destination) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append(" gl.charge_code AS chargeCode, gl.id, ");
        sb.append(" f.`faeccd`, f.`typeb1`, f.`typeb2`, f.`typeb3`,f.typeb4 FROM ").append(this.dbName).append(".`faecrt` f ");
        sb.append(" join gl_mapping gl on gl.bluescreen_chargeCode = f.`faeccd` ");
        sb.append(" WHERE trmnum = (SELECT trmnum FROM terminal ");
        sb.append(" WHERE unlocationcode1=:origin LIMIT 1) AND ");
        sb.append(" prtnum = (SELECT eciportcode FROM ports WHERE ");
        sb.append("  unlocationcode =:destination ) AND f.`chgtyp` ='B' ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("origin", origin);
        query.setParameter("destination", destination);
        List<Object[]> obj = query.list();
        double retailValue = 0.00, ctcValue = 0.00, ftfValue = 0.00, perBl = 0.00;
        double totalCost = 0.00;
        if (CommonUtils.isNotEmpty(obj)) {
            for (Object[] row : obj) {
                model = new LCLFAECostModel();
                model.setChargeType("B");
                model.setChargeCode(null != row[0] ? row[0].toString() : "");
                model.setCostACId(null != row[1] ? Integer.parseInt(row[1].toString()) : 0);
                model.setBluescreenCharge(null != row[2] ? row[2].toString() : "");
                retailValue = null != row[3] ? Double.parseDouble(row[3].toString()) : retailValue;
                retailValue *= this.blTotalRetailOFT;
                ctcValue = null != row[4] ? Double.parseDouble(row[4].toString()) : ctcValue;
                ctcValue *= this.blTotalCTCOFT;
                ftfValue = null != row[5] ? Double.parseDouble(row[5].toString()) : ftfValue;
                ftfValue *= this.blTotalFTFOFT;
                perBl = null != row[6] ? Double.parseDouble(row[6].toString()) : perBl;
                totalCost = (retailValue + ctcValue + ftfValue) + perBl;
                model.setCostAmount(NumberUtils.convertToThreeDecimal(totalCost));
                faeCostList.put(model.getChargeCode() + "#" + model.getBluescreenCharge(), model);
            }
        }
    }

    public void getFAECostType_C(LCLFAECostModel model, String origin, String destination) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append(" gl.charge_code AS chargeCode, gl.id, ");
        sb.append(" f.`faeccd`, f.`typec1`, f.`typec2`, f.typec3 FROM ").append(this.dbName).append(".`faecrt` f ");
        sb.append(" join gl_mapping gl on gl.bluescreen_chargeCode = f.`faeccd` ");
        sb.append(" WHERE trmnum = (SELECT trmnum FROM terminal ");
        sb.append(" WHERE unlocationcode1=:origin LIMIT 1) AND ");
        sb.append(" prtnum = (SELECT eciportcode FROM ports WHERE ");
        sb.append("  unlocationcode =:destination ) AND f.`chgtyp` ='C' ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("origin", origin);
        query.setParameter("destination", destination);
        List<Object[]> obj = query.list();
        double retailValue = 0.00, ctcValue = 0.00, ftfValue = 0.00, perBl = 0.00;
        double totalCost = 0.00;
        if (CommonUtils.isNotEmpty(obj)) {
            for (Object[] row : obj) {
                model = new LCLFAECostModel();
                model.setChargeType("C");
                model.setChargeCode(null != row[0] ? row[0].toString() : "");
                model.setCostACId(null != row[1] ? Integer.parseInt(row[1].toString()) : 0);
                model.setBluescreenCharge(null != row[2] ? row[2].toString() : "");
                retailValue = null != row[3] ? Double.parseDouble(row[3].toString()) : retailValue;
                retailValue *= this.blTotalRetailOFT;
                retailValue += this.blTotalRetailCollect;
                ctcValue = null != row[4] ? Double.parseDouble(row[4].toString()) : ctcValue;
                ctcValue *= this.blTotalCTCOFT;
                retailValue += this.blTotalCTCCollect;
                ftfValue = null != row[5] ? Double.parseDouble(row[5].toString()) : ftfValue;
                ftfValue *= this.blTotalFTFOFT;
                ftfValue += this.blTotalFTFCollect;
                totalCost = (retailValue + ctcValue + ftfValue);
                model.setCostAmount(NumberUtils.convertToThreeDecimal(totalCost));
                faeCostList.put(model.getChargeCode() + "#" + model.getBluescreenCharge(), model);
            }
        }
    }

    public void getFAECostType_D(LCLFAECostModel model, String origin, String destination) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append(" gl.charge_code AS chargeCode, gl.id, ");
        sb.append(" f.`faeccd`, f.typed FROM ").append(this.dbName).append(".`faecrt` f ");
        sb.append(" join gl_mapping gl on gl.bluescreen_chargeCode = f.`faeccd` ");
        sb.append(" WHERE trmnum = (SELECT trmnum FROM terminal ");
        sb.append(" WHERE unlocationcode1=:origin LIMIT 1) AND ");
        sb.append(" prtnum = (SELECT eciportcode FROM ports WHERE ");
        sb.append("  unlocationcode =:destination ) AND f.`chgtyp` ='D' ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("origin", origin);
        query.setParameter("destination", destination);
        List<Object[]> obj = query.list();
        double totalCost = 0.00;
        if (CommonUtils.isNotEmpty(obj)) {
            for (Object[] row : obj) {
                model = new LCLFAECostModel();
                model.setChargeType("D");
                model.setChargeCode(null != row[0] ? row[0].toString() : "");
                model.setCostACId(null != row[1] ? Integer.parseInt(row[1].toString()) : 0);
                model.setBluescreenCharge(null != row[2] ? row[2].toString() : "");
                totalCost = null != row[3] ? Double.parseDouble(row[3].toString()) : 0.00;
                model.setCostAmount(NumberUtils.convertToThreeDecimal(totalCost));
                faeCostList.put(model.getChargeCode() + "#" + model.getBluescreenCharge(), model);
            }
        }
    }

    public void getFAECostType_E(LCLFAECostModel model, String origin, String destination) throws Exception {
        String unitSize = unitSs.getLclUnit().getUnitType().getDescription();
        String size = !unitSize.equalsIgnoreCase("LCL") ? unitSize.substring(0, 4) : "LCL";
        String columnName = size.equalsIgnoreCase("20ft") ? "typee1"
                : unitSize.contains("40ft high cube") ? "typee4"
                        : size.equalsIgnoreCase("40ft") ? "typee2"
                                : size.equalsIgnoreCase("45ft") ? "typee3"
                                        : size.equalsIgnoreCase("48ft") ? "typee5"
                                                : size.equalsIgnoreCase("53ft") ? "typee6" : "";
        if (!columnName.equalsIgnoreCase("")) {
            StringBuilder sb = new StringBuilder();
            sb.append(" SELECT ");
            sb.append(" gl.charge_code AS chargeCode, gl.id, ");
            sb.append(" f.`faeccd`, f.").append(columnName).append(" FROM ").append(this.dbName).append(".`faecrt` f ");
            sb.append(" join gl_mapping gl on gl.bluescreen_chargeCode = f.`faeccd` ");
            sb.append(" WHERE trmnum = (SELECT trmnum FROM terminal ");
            sb.append(" WHERE unlocationcode1=:origin LIMIT 1) AND ");
            sb.append(" prtnum = (SELECT eciportcode FROM ports WHERE ");
            sb.append("  unlocationcode =:destination ) AND f.`chgtyp` ='E' ");
            SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
            query.setParameter("origin", origin);
            query.setParameter("destination", destination);
            List<Object[]> obj = query.list();
            double totalCost = 0.00;
            if (CommonUtils.isNotEmpty(obj)) {
                for (Object[] row : obj) {
                    model = new LCLFAECostModel();
                    model.setChargeType("E");
                    model.setChargeCode(null != row[0] ? row[0].toString() : "");
                    model.setCostACId(null != row[1] ? Integer.parseInt(row[1].toString()) : 0);
                    model.setBluescreenCharge(null != row[2] ? row[2].toString() : "");
                    totalCost = null != row[3] ? Double.parseDouble(row[3].toString()) : 0.00;
                    model.setCostAmount(NumberUtils.convertToThreeDecimal(totalCost));
                    faeCostList.put(model.getChargeCode() + "#" + model.getBluescreenCharge(), model);
                }
            }
        }
    }

    public void getFAECostType_F(LCLFAECostModel model, String origin, String destination) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append(" gl.charge_code AS chargeCode, gl.id, ");
        sb.append(" f.`faeccd`, f.typef1,f.typef2,f.typef3 FROM ").append(this.dbName).append(".`faecrt` f ");
        sb.append(" join gl_mapping gl on gl.bluescreen_chargeCode = f.`faeccd` ");
        sb.append(" WHERE f.trmnum = (SELECT trmnum FROM terminal ");
        sb.append(" WHERE unlocationcode1=:origin LIMIT 1) AND ");
        sb.append(" f.prtnum = (SELECT eciportcode FROM ports WHERE ");
        sb.append("  unlocationcode =:destination ) AND f.`chgtyp` ='F' ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("origin", origin);
        query.setParameter("destination", destination);
        List<Object[]> obj = query.list();
        double retailValue = 0.00, ctcValue = 0.00, ftfValue = 0.00;
        double totalCost = 0.00;
        if (CommonUtils.isNotEmpty(obj)) {
            for (Object[] row : obj) {
                model = new LCLFAECostModel();
                model.setChargeType("F");
                model.setChargeCode(null != row[0] ? row[0].toString() : "");
                model.setCostACId(null != row[1] ? Integer.parseInt(row[1].toString()) : 0);
                model.setBluescreenCharge(null != row[2] ? row[2].toString() : "");
                retailValue = null != row[3] ? Double.parseDouble(row[3].toString()) : retailValue;
                retailValue *= this.blTotalRetailCFT;
                ctcValue = null != row[4] ? Double.parseDouble(row[4].toString()) : ctcValue;
                ctcValue *= this.blTotalCTCCFT;
                ftfValue = null != row[5] ? Double.parseDouble(row[5].toString()) : ftfValue;
                ftfValue *= this.blTotalFTFCFT;
                totalCost = retailValue + ctcValue + ftfValue;
                model.setCostAmount(NumberUtils.convertToThreeDecimal(totalCost));
                faeCostList.put(model.getChargeCode() + "#" + model.getBluescreenCharge(), model);
            }
        }
    }

    public void getFAECostType_G(LCLFAECostModel model, String origin, String destination) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append(" gl.charge_code AS chargeCode, gl.id, ");
        sb.append(" f.`faeccd`, f.typeg1,f.typeg2,f.typeg3 FROM ").append(this.dbName).append(".`faecrt` f ");
        sb.append(" join gl_mapping gl on gl.bluescreen_chargeCode = f.`faeccd` ");
        sb.append(" WHERE f.trmnum = (SELECT trmnum FROM terminal ");
        sb.append(" WHERE unlocationcode1=:origin LIMIT 1) AND ");
        sb.append(" f.prtnum = (SELECT eciportcode FROM ports WHERE ");
        sb.append("  unlocationcode =:destination ) AND f.`chgtyp` ='G' ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("origin", origin);
        query.setParameter("destination", destination);
        List<Object[]> obj = query.list();
        double retailValue = 0.00, ctcValue = 0.00, ftfValue = 0.00;
        double totalCost = 0.00;
        if (CommonUtils.isNotEmpty(obj)) {
            for (Object[] row : obj) {
                model = new LCLFAECostModel();
                model.setChargeType("G");
                model.setChargeCode(null != row[0] ? row[0].toString() : "");
                model.setCostACId(null != row[1] ? Integer.parseInt(row[1].toString()) : 0);
                model.setBluescreenCharge(null != row[2] ? row[2].toString() : "");
                retailValue = null != row[3] ? Double.parseDouble(row[3].toString()) : retailValue;
                retailValue *= this.blTotalRetailCBM;
                ctcValue = null != row[4] ? Double.parseDouble(row[4].toString()) : ctcValue;
                ctcValue *= this.blTotalCTCCBM;
                ftfValue = null != row[5] ? Double.parseDouble(row[5].toString()) : ftfValue;
                ftfValue *= this.blTotalFTFCBM;
                totalCost = retailValue + ctcValue + ftfValue;
                model.setCostAmount(NumberUtils.convertToThreeDecimal(totalCost));
                faeCostList.put(model.getChargeCode() + "#" + model.getBluescreenCharge(), model);
            }
        }
    }

    public void getFAECostType_H(LCLFAECostModel model, String origin, String destination) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append(" gl.charge_code AS chargeCode, gl.id, ");
        sb.append(" f.`faeccd`, f.typeh  FROM ").append(this.dbName).append(".`faecrt` f ");
        sb.append(" join gl_mapping gl on gl.bluescreen_chargeCode = f.`faeccd` ");
        sb.append(" WHERE f.trmnum = (SELECT trmnum FROM terminal ");
        sb.append(" WHERE unlocationcode1=:origin LIMIT 1) AND ");
        sb.append(" f.prtnum = (SELECT eciportcode FROM ports WHERE ");
        sb.append("  unlocationcode =:destination ) AND f.`chgtyp` ='H' ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("origin", origin);
        query.setParameter("destination", destination);
        List<Object[]> obj = query.list();
        double totalCost = 0.00;
        if (CommonUtils.isNotEmpty(obj)) {
            for (Object[] row : obj) {
                model = new LCLFAECostModel();
                model.setChargeType("H");
                model.setChargeCode(null != row[0] ? row[0].toString() : "");
                model.setCostACId(null != row[1] ? Integer.parseInt(row[1].toString()) : 0);
                boolean isCostExists = new LclSsAcDAO().isFAECostExists(headerId, model.getChargeCode());
                if (!isCostExists) {
                    model.setBluescreenCharge(null != row[2] ? row[2].toString() : "");
                    totalCost = null != row[3] ? Double.parseDouble(row[3].toString()) : 0.00;
                    model.setCostAmount(NumberUtils.convertToThreeDecimal(totalCost));
                    faeCostList.put(model.getChargeCode() + "#" + model.getBluescreenCharge(), model);
                }
            }
        }
    }

    public double getBlTotalRetailCBM() {
        return blTotalRetailCBM;
    }

    public void setBlTotalRetailCBM(double blTotalRetailCBM) {
        this.blTotalRetailCBM = blTotalRetailCBM;
    }

    public double getBlTotalCTCCBM() {
        return blTotalCTCCBM;
    }

    public void setBlTotalCTCCBM(double blTotalCTCCBM) {
        this.blTotalCTCCBM = blTotalCTCCBM;
    }

    public double getBlTotalFTFCBM() {
        return blTotalFTFCBM;
    }

    public void setBlTotalFTFCBM(double blTotalFTFCBM) {
        this.blTotalFTFCBM = blTotalFTFCBM;
    }

    public double getBlTotalRetailCFT() {
        return blTotalRetailCFT;
    }

    public void setBlTotalRetailCFT(double blTotalRetailCFT) {
        this.blTotalRetailCFT = blTotalRetailCFT;
    }

    public double getBlTotalCTCCFT() {
        return blTotalCTCCFT;
    }

    public void setBlTotalCTCCFT(double blTotalCTCCFT) {
        this.blTotalCTCCFT = blTotalCTCCFT;
    }

    public double getBlTotalFTFCFT() {
        return blTotalFTFCFT;
    }

    public void setBlTotalFTFCFT(double blTotalFTFCFT) {
        this.blTotalFTFCFT = blTotalFTFCFT;
    }

    public double getBlTotalRetailOFT() {
        return blTotalRetailOFT;
    }

    public void setBlTotalRetailOFT(double blTotalRetailOFT) {
        this.blTotalRetailOFT = blTotalRetailOFT;
    }

    public double getBlTotalCTCOFT() {
        return blTotalCTCOFT;
    }

    public void setBlTotalCTCOFT(double blTotalCTCOFT) {
        this.blTotalCTCOFT = blTotalCTCOFT;
    }

    public double getBlTotalFTFOFT() {
        return blTotalFTFOFT;
    }

    public void setBlTotalFTFOFT(double blTotalFTFOFT) {
        this.blTotalFTFOFT = blTotalFTFOFT;
    }

    public double getBlTotalRetailCollect() {
        return blTotalRetailCollect;
    }

    public void setBlTotalRetailCollect(double blTotalRetailCollect) {
        this.blTotalRetailCollect = blTotalRetailCollect;
    }

    public double getBlTotalCTCCollect() {
        return blTotalCTCCollect;
    }

    public void setBlTotalCTCCollect(double blTotalCTCCollect) {
        this.blTotalCTCCollect = blTotalCTCCollect;
    }

    public double getBlTotalFTFCollect() {
        return blTotalFTFCollect;
    }

    public void setBlTotalFTFCollect(double blTotalFTFCollect) {
        this.blTotalFTFCollect = blTotalFTFCollect;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getBluescreenCharge() {
        return bluescreenCharge;
    }

    public void setBluescreenCharge(String bluescreenCharge) {
        this.bluescreenCharge = bluescreenCharge;
    }

    public String getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(String costAmount) {
        this.costAmount = costAmount;
    }

    public LinkedHashMap<String, LCLFAECostModel> getFaeCostList() {
        return faeCostList;
    }

    public void setFaeCostList(LinkedHashMap<String, LCLFAECostModel> faeCostList) {
        this.faeCostList = faeCostList;
    }

    public String getVendorNo() {
        return vendorNo;
    }

    public void setVendorNo(String vendorNo) {
        this.vendorNo = vendorNo;
    }

    public Long getHeaderId() {
        return headerId;
    }

    public void setHeaderId(Long headerId) {
        this.headerId = headerId;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public LclUnitSs getUnitSs() {
        return unitSs;
    }

    public void setUnitSs(LclUnitSs unitSs) {
        this.unitSs = unitSs;
    }

    public int getCostACId() {
        return costACId;
    }

    public void setCostACId(int costACId) {
        this.costACId = costACId;
    }

}

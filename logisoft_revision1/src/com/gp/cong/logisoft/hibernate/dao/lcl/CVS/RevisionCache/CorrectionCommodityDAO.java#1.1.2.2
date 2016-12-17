/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.CorrectionCommodity;
import com.gp.cong.logisoft.domain.lcl.bl.LclCorrection;
import com.gp.cong.logisoft.lcl.model.RateModel;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author Aravindhan
 */
public class CorrectionCommodityDAO extends BaseHibernateDAO<CorrectionCommodity> {

    private static final Logger log = Logger.getLogger(CorrectionCommodityDAO.class);

    public CorrectionCommodityDAO() {
        super(CorrectionCommodity.class);
    }

    public List<CorrectionCommodity> getCommodityCodeList(Long correctionId) throws Exception {
        String queryStr = "from CorrectionCommodity where lclCorrection.id=:correctionId";
        Query query = getCurrentSession().createQuery(queryStr);
        query.setLong("correctionId", correctionId);
        return (List<CorrectionCommodity>) query.list();
    }

    public CorrectionCommodity findByCommodity(String commodityCode, Long correctionId) throws Exception {
        String queryStr = "from CorrectionCommodity where lclCorrection.id=:correctionId and commodityType.code=:commodityCode";
        Query query = getCurrentSession().createQuery(queryStr);
        query.setString("commodityCode", commodityCode);
        query.setLong("correctionId", correctionId);
        return (CorrectionCommodity) query.setMaxResults(1).uniqueResult();
    }

    public void createInstance(RateModel commodity, LclCorrection correction) throws Exception {
        CorrectionCommodity correctionCommodity = this.findByCommodity(commodity.getCommodityType().getCode(), correction.getId());
        if (correctionCommodity == null) {
            correctionCommodity = new CorrectionCommodity();
        }
        correctionCommodity.setCommodityType(new commodityTypeDAO()
                .getCommodityCode(commodity.getCommodityType().getCode()));
        correctionCommodity.setLclCorrection(correction);
        correctionCommodity.setTotalCbm(commodity.getCbm());
        correctionCommodity.setTotalCft(commodity.getCft());
        correctionCommodity.setTotalKgs(commodity.getKgs());
        correctionCommodity.setTotalLbs(commodity.getLbs());
        this.saveOrUpdate(correctionCommodity);
    }
}

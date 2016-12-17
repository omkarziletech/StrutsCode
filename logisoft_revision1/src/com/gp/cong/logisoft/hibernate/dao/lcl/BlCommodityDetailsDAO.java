/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPieceDetail;
import java.util.List;
import org.hibernate.criterion.Order;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author lakshh
 */
public class BlCommodityDetailsDAO extends BaseHibernateDAO<LclBlPieceDetail> {

    public BlCommodityDetailsDAO() {
        super(LclBlPieceDetail.class);
    }

    public List findDetailProperty(String propertyName, Object value) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(LclBlPieceDetail.class);
        criteria.add(Restrictions.eq(propertyName, value));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }

    public String[] displayBlDimsDetails(String blPieceId) throws Exception {
        StringBuilder sb = new StringBuilder();
        String[] result = new String[2];
        List detailList = null;
        if (blPieceId != null && !blPieceId.equals("")) {
            detailList = new BlCommodityDetailsDAO().findDetailProperty("lclBlPiece.id", Long.parseLong(blPieceId));
        }
        showBlDimsDetails(sb, detailList);
        result[0] = sb.toString();
        result[1] = CommonUtils.isNotEmpty(detailList) ? "true" : "false";
        return result;
    }

    private StringBuilder showBlDimsDetails(StringBuilder sb, List detailList) {
        LclUtils lclUtils = new LclUtils();
        sb.append("<HTML><BODY>");
        sb.append("<table width=\"100%\" style=\"border:2px solid black;\">");
        sb.append("<tr bgcolor='#FFFF00'>");
        sb.append("<td colspan='10' style=\"font-weight:bold;font-family:Calibri;font-size: 11px;text-align: center; color=#008000\">");
        sb.append("DIMS DETAILS");
        sb.append("</td>");
        sb.append("</tr>");
        if (CommonUtils.isNotEmpty(detailList)) {
            sb.append("<tr  bgcolor='#9BC2E6' style=\"border:2px solid black;font-weight:bold;font-family:Calibri;font-size: 11px;text-align: center;\"><td  colspan='2' style=\"border-right:2px solid black;\">LENGTH</td><td  colspan='2' style=\"border-right:2px solid black;\">WIDTH</td><td  colspan='2' style=\"border-right:2px solid black;\">HEIGHT</td><td  colspan='2' style=\"border-right:2px solid black;\">WEIGHT PER PIECE</td><td style=\"border-right:2px solid black;\">PIECES</td><td>WAREHOUSE</td></tr>");
            sb.append("<tr style=\"border:2px solid black;font-weight:bold;font-family:Calibri;font-size: 11px;text-align: center;\"><b><td bgcolor='#99F27A' style=\"border-right:1px solid #dcdcdc;\">(IN)</td><td  bgcolor='#FFE699' style=\"border-right:2px solid black;\">(CM)</td><td bgcolor='#99F27A'  style=\"border-right:2px solid #dcdcdc;\">(IN)</td><td  bgcolor='#FFE699' style=\"border-right:2px solid black;\">(CM)</td><td bgcolor='#99F27A'>(IN)</td><td  bgcolor='#FFE699' style=\"border-right:2px solid black;\">(CM)</td><td bgcolor='#99F27A'  style=\"border-right:2px solid #dcdcdc;\">(KGS)</td><td  bgcolor='#FFE699' style=\"border-right:2px solid black;\">(LBS)</td><td  bgcolor='#99F27A' style=\"border-right:2px solid black;\"></td><td  bgcolor='#FFE699' style=\"border-right:2px solid black;\"></td></b></tr>");
            for (int i = 0; i < detailList.size(); i++) {
                LclBlPieceDetail detail = (LclBlPieceDetail) detailList.get(i);
                sb.append("<tr style=\"font-family:Calibri;font-size: 11px;text-align: center;border-bottom:1px solid #dcdcdc;\"><td bgcolor='#99F27A' style=\"border-right:1px solid #dcdcdc;\">");
                sb.append(detail.getActualLength());
                sb.append("</td><td bgcolor='#FFE699' style=\"border-right:2px solid black;\">");
                lclUtils.cbmcftConversion(sb, detail.getActualUom(), detail.getActualLength().toString());
                sb.append("</td><td bgcolor='#99F27A' style=\"border-right:1px solid #dcdcdc;\">");
                sb.append(detail.getActualWidth());
                sb.append("</td><td bgcolor='#FFE699' style=\"border-right:2px solid black;\">");
                lclUtils.cbmcftConversion(sb, detail.getActualUom(), detail.getActualWidth().toString());
                sb.append("</td><td bgcolor='#99F27A' style=\"border-right:1px solid #dcdcdc;\">");
                sb.append(detail.getActualHeight());
                sb.append("</td><td bgcolor='#FFE699' style=\"border-right:2px solid black;\">");
                lclUtils.cbmcftConversion(sb, detail.getActualUom(), detail.getActualHeight().toString());
                sb.append("</td><td bgcolor='#99F27A' style=\"border-right:1px solid #dcdcdc;\">");
                if (detail.getActualWeight() != null && !detail.getActualWeight().equals("")) {
                    sb.append(detail.getActualWeight());
                } else {
                    sb.append("");
                }
                sb.append("</td><td bgcolor='#FFE699' style=\"border-right:2px solid black;\">");
                if (detail.getActualWeight() != null && !detail.getActualWeight().equals("")) {
                    lclUtils.cbmcftConversion(sb, detail.getActualUom(), detail.getActualWeight().toString());
                } else {
                    sb.append("");
                }
                sb.append("</td><td bgcolor='#99F27A' style=\"border-right:2px solid black;\">");
                sb.append(detail.getPieceCount());
                sb.append("</td><td bgcolor='#FFE699' style=\"border-right:2px solid black;\">");
                if (detail.getStowedLocation() != null && !detail.getStowedLocation().equals("")) {
                    sb.append(detail.getStowedLocation());
                } else {
                    sb.append("");
                }
                sb.append("</td></tr>");
            }
        }
        //sb.append("</table>");
        sb.append("</table>");
        sb.append("</BODY></HTML>");
        return sb;
    }
}

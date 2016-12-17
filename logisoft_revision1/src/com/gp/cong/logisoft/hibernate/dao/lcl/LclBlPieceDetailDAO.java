/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPieceDetail;

/**
 *
 * @author lakshh
 */
public class LclBlPieceDetailDAO extends BaseHibernateDAO<LclBlPieceDetail> {

    public LclBlPieceDetailDAO() {
        super(LclBlPieceDetail.class);
    }

    public void deleteByPiece(Long pieceId) throws Exception {
        String query = "delete LclBlPiece where lclFileNumber.id =" + pieceId;
        getSession().createQuery(query).executeUpdate();

    }

    public void deleteByPieceDetails(Long pieceId) throws Exception {
        String query = "delete LclBlPieceDetail where lclBlPiece.id =" + pieceId;
        getSession().createQuery(query).executeUpdate();

    }
}

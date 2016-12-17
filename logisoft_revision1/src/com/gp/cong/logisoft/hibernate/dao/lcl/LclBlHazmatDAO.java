/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.lcl.model.LclHazmatModel;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.lcl.PackageType;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlHazmat;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.StringType;

/**
 *
 * @author Administrator
 */
public class LclBlHazmatDAO extends BaseHibernateDAO<LclBlHazmat> {

    private static final Logger log = Logger.getLogger(BaseHibernateDAO.class);

    public LclBlHazmatDAO() {
        super(LclBlHazmat.class);
    }

    public LclBlHazmat findByFileAndCommodity(Long fileNumberId, Long blPieceId) throws Exception {
        String queryString = "from LclBlHazmat where lclFileNumber='" + fileNumberId + "' and lclBlPiece='" + blPieceId + "'";
        Query query = getSession().createQuery(queryString);
        return (LclBlHazmat) query.setMaxResults(1).uniqueResult();
    }

    public List<LclBlHazmat> findByFileAndCommodityList(Long fileNumberId, Long blPieceId) throws Exception {
        String queryString = "from LclBlHazmat where lclFileNumber='" + fileNumberId + "' and lclBlPiece='" + blPieceId + "'";
        Query query = getSession().createQuery(queryString);
        List list = query.list();
        return list;
    }

    public int deleteBlHazmatByFileId(Long id) throws Exception {
        int deletedRows = 0;
        String queryString = "delete from lcl_bl_hazmat where id=?0";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", id);
        deletedRows = queryObject.executeUpdate();
        return deletedRows;
    }

    public List<PackageType> getAllPackages() throws Exception {
        String queryString = "from PackageType";
        return getSession().createQuery(queryString).list();
    }

    public List<GenericCode> getAllPackageComposition() throws Exception {
        String queryString = "from GenericCode where codetypeid=50 order by codedesc";
        return getSession().createQuery(queryString).list();
    }

    public Boolean isCheckedHazmat(List fileId) throws Exception {
        String queryStr = "select if(Count(*)>0,true,false) as result from lcl_booking_hazmat where file_number_id in(:fileId)";
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr);
        query.setParameterList("fileId", fileId);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public List<LclHazmatModel> getBkgHazmatList(List fileId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT lfn.file_number AS fileNo, ");
        queryStr.append(" lfn.id AS fileId, ");
        queryStr.append(" lbh.id AS bkgHazmatId, ");
        queryStr.append(" lbh.booking_piece_id AS bkgPieceHazmatId, ");
        queryStr.append(" lbh.un_hazmat_no AS unHazmatNo, ");
        queryStr.append(" lbh.proper_shipping_name AS shippingName, ");
        queryStr.append(" lbh.technical_name AS technicalName, ");
        queryStr.append(" lbh.imo_pri_class_code AS priclassCode ");
        queryStr.append(" FROM lcl_booking_hazmat lbh ");
        queryStr.append(" JOIN lcl_file_number lfn ON lfn.id=lbh.file_number_id ");
        queryStr.append(" WHERE lfn.id IN(:fileId) ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameterList("fileId", fileId);
        query.setResultTransformer(Transformers.aliasToBean(LclHazmatModel.class));
        query.addScalar("fileNo", StringType.INSTANCE);
        query.addScalar("fileId", StringType.INSTANCE);
        query.addScalar("bkgHazmatId", StringType.INSTANCE);
        query.addScalar("bkgPieceHazmatId", StringType.INSTANCE);
        query.addScalar("unHazmatNo", StringType.INSTANCE);
        query.addScalar("shippingName", StringType.INSTANCE);
        query.addScalar("technicalName", StringType.INSTANCE);
        query.addScalar("priclassCode", StringType.INSTANCE);
        return query.list();
    }
}

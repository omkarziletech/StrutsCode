package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.LclBookingImportAms;
import java.math.BigInteger;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.type.StringType;

/**
 *
 * @author Logiware
 */
public class LclBookingImportAmsDAO extends BaseHibernateDAO<LclBookingImportAms> {

    public LclBookingImportAmsDAO() {
        super(LclBookingImportAms.class);
    }

    /**
     * * Get very first ams no. **
     */
    public LclBookingImportAms getFirstBkgAms(Long fileId) throws Exception {
        Query query = getCurrentSession().createQuery("from LclBookingImportAms where lclFileNumber.id = :fileId order by id asc");
        query.setMaxResults(1);
        query.setLong("fileId", fileId);
        return (LclBookingImportAms) query.uniqueResult();
    }

    //get latest AMS no
    public LclBookingImportAms getBkgAms(Long fileId) throws Exception {
        Query query = getCurrentSession().createQuery("from LclBookingImportAms where lclFileNumber.id = :fileId order by enteredDatetime asc");
        query.setMaxResults(1);
        query.setLong("fileId", fileId);
        return (LclBookingImportAms) query.uniqueResult();
    }

    /**
     * * Find all with order by entered date **
     */
    public List<LclBookingImportAms> findAll(Long fileId) throws Exception {
        Query query = getCurrentSession().createQuery("from LclBookingImportAms where lclFileNumber.id = :fileId order by enteredDatetime asc");
        query.setLong("fileId", fileId);
        return query.list();
    }

    public void deleteBkgAmsByFileNumber(Long fileNumberId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete LclBookingImportAms where lclFileNumber = ").append(fileNumberId);
        getSession().createQuery(queryBuilder.toString()).executeUpdate();
    }

    public String validateScacAndAms(String scac, String amsNo, String fileNumberId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  if(t.result <> '', result, 'available') as result ");
        queryBuilder.append("from");
        queryBuilder.append("  (select");
        queryBuilder.append("    if(");
        queryBuilder.append("      count(*) > 0,");
        queryBuilder.append("      concat(");
        queryBuilder.append("        'DR# - ',");
        queryBuilder.append("        (select fn.`file_number` from `lcl_file_number` fn where fn.`id` = ams.`file_number_id`),");
        queryBuilder.append("        ' already has this scac - ',");
        queryBuilder.append("        UPPER(ams.`scac`),");
        queryBuilder.append("        ' and ams# - ',");
        queryBuilder.append("        ams.`ams_no`,");
        queryBuilder.append("        '. Please enter another one.'");
        queryBuilder.append("      ),");
        queryBuilder.append("      'available'");
        queryBuilder.append("    ) as result ");
        queryBuilder.append("  from");
        queryBuilder.append("    `lcl_booking_import_ams` ams ");
        queryBuilder.append("  where ams.`scac` = :scac ");
        queryBuilder.append("    and ams.`ams_no` = :amsNo");
        queryBuilder.append("    and ams.`file_number_id` <> :fileNumberId");
        queryBuilder.append("  ) as t");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("scac", scac);
        query.setString("amsNo", amsNo);
        query.setString("fileNumberId", fileNumberId);
        return (String) query.uniqueResult();
    }

    public String validateSubHouseBl(String SubHouseBl, String fileNumberId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  if(t.result <> '', result, 'available') as result ");
        queryBuilder.append("from");
        queryBuilder.append("  (select");
        queryBuilder.append("    if(");
        queryBuilder.append("      count(*) > 0,");
        queryBuilder.append("      concat(");
        queryBuilder.append("        'DR# - ',");
        queryBuilder.append("        (select fn.`file_number` from `lcl_file_number` fn where fn.`id` = lcl.`file_number_id`),");
        queryBuilder.append("        ' already has this Sub-HouseBL - ',");
        queryBuilder.append("        UPPER(lcl.`sub_house_bl`),");
        queryBuilder.append("        '. Please enter another one.'");
        queryBuilder.append("      ),");
        queryBuilder.append("      'available'");
        queryBuilder.append("    ) as result ");
        queryBuilder.append("  from");
        queryBuilder.append("    `lcl_booking_import` lcl ");
        queryBuilder.append("  where lcl.`sub_house_bl`  = :SubHouseBl ");
        queryBuilder.append("    and lcl.`file_number_id` <> :fileNumberId");
        queryBuilder.append("  ) as t");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("SubHouseBl", SubHouseBl);
        query.setString("fileNumberId", fileNumberId);
        return (String) query.uniqueResult();
    }

    public BigInteger getAmsAndScac(String scac, String amsNo) throws Exception {
        Query query = getCurrentSession().createSQLQuery("select count(*) from lcl_booking_import_ams scac where scac.scac = :scac and scac.ams_no = :amsno");
        query.setString("scac", scac);
        query.setString("amsno", amsNo);
        return (BigInteger) query.uniqueResult();
    }

    public void updateSegregationFileNumber(Long id, Long segFileId, Integer userId) throws Exception {
        StringBuilder query = new StringBuilder();
        query.append("update lcl_booking_import_ams set seg_file_number_id = :segFileId, entered_by_user_id = :enteredBy,");
        query.append(" modified_by_user_id = :modifiedBy, entered_datetime = SYSDATE(), modified_datetime = SYSDATE() where id = :id");
        SQLQuery queryObject = getSession().createSQLQuery(query.toString());
        queryObject.setLong("id", id);
        queryObject.setLong("segFileId", segFileId);
        queryObject.setInteger("enteredBy", userId);
        queryObject.setInteger("modifiedBy", userId);
        queryObject.executeUpdate();
    }

    public void updateSegFileIdAsNull(String fileId, Integer userId) throws Exception {
        StringBuilder query = new StringBuilder();
        query.append("update lcl_booking_import_ams set seg_file_number_id = NULL, entered_by_user_id = :enteredBy,");
        query.append(" modified_by_user_id = :modifiedBy, entered_datetime = SYSDATE(), modified_datetime = SYSDATE() where file_number_id = :fileId");
        SQLQuery queryObject = getSession().createSQLQuery(query.toString());
        queryObject.setString("fileId", fileId);
        queryObject.setInteger("enteredBy", userId);
        queryObject.setInteger("modifiedBy", userId);
        queryObject.executeUpdate();
    }

    public String getAmsNoGroup(String fileId) throws Exception {
        String query = "SELECT GROUP_CONCAT(ams_no) FROM `lcl_booking_import_ams` WHERE file_number_id=" + fileId + "";
        return (String) getCurrentSession().createSQLQuery(query).uniqueResult();
    }

    public List<LclBookingImportAms> findBysSegFileNumberId(Long fileId) throws Exception {
        Query query = getCurrentSession().createQuery("from LclBookingImportAms where segregationLclFileNumber.id = :fileId order by enteredDatetime asc");
        query.setLong("fileId", fileId);
        return query.list();
    }

    public String getAmsNo(String fileId) throws Exception {
        String queryStr = "SELECT ams_no as amsNo FROM `lcl_booking_import_ams` WHERE seg_file_number_id=:fileId";
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr);
        query.setParameter("fileId", fileId);
        query.addScalar("amsNo", StringType.INSTANCE);
        return (String) query.setMaxResults(1).uniqueResult();
    }
}

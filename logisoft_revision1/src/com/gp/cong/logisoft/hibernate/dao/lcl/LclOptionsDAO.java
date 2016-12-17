/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclOptions;
import java.util.Date;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.BooleanType;

/**
 *
 * @author MohanaPriya
 */
public class LclOptionsDAO extends BaseHibernateDAO<LclOptions> {

    public LclOptionsDAO() {
        super(LclOptions.class);
    }

    public LclOptions createInstance(Long fileId, String keyValue, User loginUser) throws Exception {
        LclOptions option = this.getOption(fileId, keyValue);
        if (option == null) {
            option = new LclOptions();
            option.setOptionValue(keyValue);
            option.setLclFileNumber(new LclFileNumber(fileId));
            option.setEnteredBy(loginUser);
            option.setEnteredDatetime(new Date());
        }
        option.setModifiedBy(loginUser);
        option.setModifiedDatetime(new Date());
        return option;
    }

    public LclOptions getOption(Long fileId, String keyValue) throws Exception {
        String queryStr = "from LclOptions where lclFileNumber.id=:fileId and optionValue=:optionValue";
        Query queryObject = getSession().createQuery(queryStr);
        queryObject.setLong("fileId", fileId);
        queryObject.setString("optionValue", keyValue);
        return (LclOptions) queryObject.setMaxResults(1).uniqueResult();
    }

    public void deleteOption(Long fileId) throws Exception {
        String queryStr = "delete from lcl_options where file_number_id=:fileId";
        SQLQuery queryObject = getSession().createSQLQuery(queryStr);
        queryObject.setLong("fileId", fileId);
        queryObject.executeUpdate();
    }

    public String getOptionValue(Long fileId, String optionValue) throws Exception {
        String queryStr = "SELECT l.option_key FROM lcl_options l WHERE l.option_value=:value AND l.file_number_id=:fileId";
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr);
        query.setParameter("value", optionValue);
        query.setParameter("fileId", fileId);
        String keyValue = (String) query.setMaxResults(1).uniqueResult();
        return CommonUtils.isNotEmpty(keyValue) ? keyValue : "";
    }

    public LclOptions getLclOptionsByValue(Long fileId, String optionValue) throws Exception {
        Criteria criteria = getSession().createCriteria(LclOptions.class, "lclOptions");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        if (!CommonUtils.isEmpty(optionValue)) {
            criteria.add(Restrictions.eq("lclOptions.optionValue", optionValue));
        }
        return (LclOptions) criteria.uniqueResult();
    }

    public void insertLclOptions(Long fileNumberId, int userId, String keyValue, String optionValue) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO `lcl_options`(`file_number_id`,`option_key`,`option_value`,`entered_datetime`,`entered_by_user_id`,`modified_datetime`,`modified_by_user_id`) VALUES (");
        sb.append(fileNumberId).append(",'").append(keyValue).append("','").append(optionValue).append("',").append("SYSDATE(),").append(userId).
                append(",").append("SYSDATE(),").append(userId).append(")");
        getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
    }

    public void updateLclOptions(Long fileNumberId, int userId, String keyValue, String optionValue) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE `lcl_options` SET `option_key`='");
        sb.append(keyValue).append("'").append(" WHERE `option_value`='").append(optionValue).append(" ' AND ").append("file_number_id=").append(fileNumberId).append("");
        getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
    }

    public boolean checkValueReceive(Long fileNumberId, String optionValue) throws Exception {
        String query = "SELECT IF(COUNT(*)>0,TRUE,FALSE) as result FROM lcl_options WHERE file_number_id=:fileId AND option_value =:optionValue";
        SQLQuery queryObj = getCurrentSession().createSQLQuery(query);
        queryObj.setLong("fileId", fileNumberId);
        queryObj.setString("optionValue", optionValue);
        queryObj.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) queryObj.uniqueResult();
    }
}

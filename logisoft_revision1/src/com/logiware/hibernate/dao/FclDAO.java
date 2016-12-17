/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.hibernate.dao;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.hibernate.FclBlNew;
import org.hibernate.SQLQuery;
import org.hibernate.type.BooleanType;
import org.hibernate.type.StringType;

/**
 *
 * @author Owner
 */
public class FclDAO extends BaseHibernateDAO<FclBlNew> {

    public FclDAO() {
        super(FclBlNew.class);
    }

    public boolean isImportFile(String fileNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  max(f.`is_import_file`) isImportFile ");
        queryBuilder.append("from");
        queryBuilder.append("  (select");
        queryBuilder.append("    coalesce(f.`importflag` = 'I', false) is_import_file");
        queryBuilder.append("  from");
        queryBuilder.append("    fcl_bl f ");
        queryBuilder.append("  where f.`file_no` = :fileNo");
        queryBuilder.append("  union");
        queryBuilder.append("  select");
        queryBuilder.append("    coalesce(f.`importflag` = 'I', false) as is_import_file");
        queryBuilder.append("  from");
        queryBuilder.append("    booking_fcl f");
        queryBuilder.append("  where f.`file_no` = :fileNo");
        queryBuilder.append("  union");
        queryBuilder.append("  select");
        queryBuilder.append("    coalesce(f.`file_type` = 'I', false) as is_import_file");
        queryBuilder.append("  from");
        queryBuilder.append("    quotation f");
        queryBuilder.append("  where f.`file_no` = :fileNo) as f");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("fileNo", fileNo);
        query.addScalar("isImportFile", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }
}

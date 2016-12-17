package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.Notes;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.struts.form.BlueScreenNotesForm;
import com.logiware.accounting.model.NotesModel;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.TimestampType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.BooleanType;
import org.hibernate.type.LongType;

public class NotesDAO extends BaseHibernateDAO implements NotesConstants {

    public void save(Notes notes) throws Exception {
        notes.setStatus("Pending");
        getSession().save(notes);
        getSession().flush();
    }

    public void saveFromScheduler(Notes notes) throws Exception {
        notes.setStatus("Pending");
        Transaction transaction = getCurrentSession().beginTransaction();
        getCurrentSession().save(notes);
        transaction.commit();
    }

    public void delete(Notes notes) throws Exception {
        getSession().delete(notes);
        getSession().flush();
    }

    public Notes findById(Integer id) throws Exception {
        Notes instance = (Notes) getSession().get("com.gp.cong.logisoft.domain.Notes", id);
        return instance;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
        String queryString = "from Notes as model where model." + propertyName + "= ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        return queryObject.list();
    }

    public List findNotesByModuleId(String moduleId, String moduleRefId, String itemName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select id,module_id as moduleId,updateDate,note_type as noteType,note_desc as noteDesc,");
        queryBuilder.append("updated_by as updatedBy,item_Name as itemName,void as voidNote,module_ref_id as moduleRefId,");
        queryBuilder.append("followup_date as followupDate,status,unique_id as uniqueId,removed,print_on_report as printOnReport ");
        queryBuilder.append("from notes ");
        int i = 0;
        String[] moduleRefIds = null;
        if (CommonUtils.isEqualIgnoreCase(moduleId, ACCRUALS)) {
            if (moduleRefId.contains("<---->")) {
                moduleRefIds = StringUtils.splitByWholeSeparator(moduleRefId, "<---->");
                queryBuilder.append("where module_id='").append(AP_INVOICE).append("' ");
                queryBuilder.append("and module_ref_id ='").append(moduleRefIds[0]).append("'");

                queryBuilder.append(" union select id,module_id as moduleId,updateDate,note_type as noteType,note_desc as noteDesc,");
                queryBuilder.append("updated_by as updatedBy,item_Name as itemName,void as voidNote,module_ref_id as moduleRefId,");
                queryBuilder.append("followup_date as followupDate,status,unique_id as uniqueId,removed,print_on_report as printOnReport ");
                queryBuilder.append("from notes where ");
                queryBuilder.append("module_id='").append(ACCRUALS).append("' ");
                queryBuilder.append("and module_ref_id in(?0) ");
                i = 1;
            } else {
                queryBuilder.append("where module_id='").append(ACCRUALS).append("' ");
                queryBuilder.append("and module_ref_id in(?1) ");
                i = 2;
            }
        } else {
            queryBuilder.append(" where module_id='").append(moduleId).append("' and module_ref_id ='").append(moduleRefId).append("' ");
            if (CommonUtils.isEqualIgnoreCase(itemName, DISPUTEDBLCODE)) {
                queryBuilder.append(" and item_Name='").append(itemName).append("'");
            } else if (CommonUtils.isEqualIgnoreCase(itemName, "100018")) {
                queryBuilder.append(" and item_Name='").append(itemName).append("'");
            } else {
                queryBuilder.append(" and (item_Name <> '100018' or item_Name is null) ");
            }
        }
        queryBuilder.append(" and void <> 'Y' order by id desc");
        SQLQuery q = getCurrentSession().createSQLQuery(queryBuilder.toString());
        if (i == 1) {
            q.setParameterList("0", StringUtils.splitByWholeSeparator(moduleRefIds[1], ","));
        } else if (i == 2) {
            q.setParameterList("1", StringUtils.splitByWholeSeparator(moduleRefId, ","));
        }
        Field[] allFields = Notes.class.getDeclaredFields();
        for (Field field : allFields) {
            if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "String")) {
                q.addScalar(field.getName(), StringType.INSTANCE);
            } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Boolean")) {
                q.addScalar(field.getName(), BooleanType.INSTANCE);
            } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Date")) {
                q.addScalar(field.getName(), TimestampType.INSTANCE);
            } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Integer")) {
                q.addScalar(field.getName(), IntegerType.INSTANCE);
            }
        }
        q.setResultTransformer(Transformers.aliasToBean(Notes.class));
        return q.list();
    }

    public List getAccrualNotes(String accrualsRefId, String invoiceRefId, String costRefId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select id,module_id as moduleId,updateDate,note_type as noteType,note_desc as noteDesc,");
        queryBuilder.append("updated_by as updatedBy,item_Name as itemName,void as voidNote,module_ref_id as moduleRefId,");
        queryBuilder.append("followup_date as followupDate,status,unique_id as uniqueId,removed,print_on_report as printOnReport ");
        queryBuilder.append("from notes ");
        queryBuilder.append("where module_id='").append(ACCRUALS).append("' and module_ref_id in(?0)");

        if (null != invoiceRefId && !invoiceRefId.trim().isEmpty()) {
            queryBuilder.append(" union select id,module_id as moduleId,updateDate,note_type as noteType,note_desc as noteDesc,");
            queryBuilder.append("updated_by as updatedBy,item_Name as itemName,void as voidNote,module_ref_id as moduleRefId,");
            queryBuilder.append("followup_date as followupDate,status,unique_id as uniqueId,removed,print_on_report as printOnReport ");
            queryBuilder.append("from notes ");
            queryBuilder.append("where module_id='").append(AP_INVOICE).append("' and module_ref_id ='");
            queryBuilder.append(invoiceRefId).append("'");
        }

        if (null != costRefId && !costRefId.trim().isEmpty()) {
            queryBuilder.append(" union select id,module_id as moduleId,updateDate,note_type as noteType,note_desc as noteDesc,");
            queryBuilder.append("updated_by as updatedBy,item_Name as itemName,void as voidNote,module_ref_id as moduleRefId,");
            queryBuilder.append("followup_date as followupDate,status,unique_id as uniqueId,removed,print_on_report as printOnReport ");
            queryBuilder.append("from notes ");
            queryBuilder.append("where module_id='").append(FILE).append("' and unique_id in(?1) ");
        }
        queryBuilder.append(" and void <> 'Y'order by id desc");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(queryBuilder.toString());
        if (null != accrualsRefId && !accrualsRefId.trim().isEmpty()) {
            queryObject.setParameterList("0", StringUtils.splitByWholeSeparator(accrualsRefId, ","));
        }
        if (null != costRefId && !costRefId.trim().isEmpty()) {
            queryObject.setParameterList("1", StringUtils.splitByWholeSeparator(costRefId, ","));
        }
        Field[] allFields = Notes.class.getDeclaredFields();
        for (Field field : allFields) {
            if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "String")) {
                queryObject.addScalar(field.getName(), StringType.INSTANCE);
            } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Boolean")) {
                queryObject.addScalar(field.getName(), BooleanType.INSTANCE);
            } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Date")) {
                queryObject.addScalar(field.getName(), TimestampType.INSTANCE);
            } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Integer")) {
                queryObject.addScalar(field.getName(), IntegerType.INSTANCE);
            }
        }
        queryObject.setResultTransformer(Transformers.aliasToBean(Notes.class));
        return queryObject.list();
    }

    public List<Notes> findNotes(String moduleId, String moduleRefId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select id,module_id as moduleId,updateDate,note_type as noteType,note_desc as noteDesc,");
        queryBuilder.append("updated_by as updatedBy,item_Name as itemName,void as voidNote,module_ref_id as moduleRefId,");
        queryBuilder.append("followup_date as followupDate,status,unique_id as uniqueId,removed,print_on_report as printOnReport ");
        queryBuilder.append("from notes where module_id=?0 and module_ref_id=?1 and void=?2 and status= ?3 order by updateDate desc limit 10 ");
        SQLQuery queryObject = getSession().createSQLQuery(queryBuilder.toString());
        queryObject.setParameter("0", moduleId);
        queryObject.setParameter("1", moduleRefId);
        queryObject.setParameter("2", UNVOID_NOTES);
        queryObject.setParameter("3", STATUS_PENDING);
        Field[] allFields = Notes.class.getDeclaredFields();
        for (Field field : allFields) {
            if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "String")) {
                queryObject.addScalar(field.getName(), StringType.INSTANCE);
            } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Boolean")) {
                queryObject.addScalar(field.getName(), BooleanType.INSTANCE);
            } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Date")) {
                queryObject.addScalar(field.getName(), TimestampType.INSTANCE);
            } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Integer")) {
                queryObject.addScalar(field.getName(), IntegerType.INSTANCE);
            }
        }
        queryObject.setResultTransformer(Transformers.aliasToBean(Notes.class));
        return queryObject.list();
    }

    public List<Notes> findEventNotes(String moduleId, String moduleRefId, String noteype) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select id,module_id as moduleId,updateDate,note_type as noteType,note_desc as noteDesc,");
        queryBuilder.append("updated_by as updatedBy,item_Name as itemName,void as voidNote,module_ref_id as moduleRefId,");
        queryBuilder.append("followup_date as followupDate,status,unique_id as uniqueId,removed,print_on_report as printOnReport ");
        queryBuilder.append("from notes where module_id=?0 and module_ref_id=?1 and note_type=?2 order by updateDate desc");
        SQLQuery queryObject = getSession().createSQLQuery(queryBuilder.toString());
        queryObject.setParameter("0", moduleId);
        queryObject.setParameter("1", moduleRefId);
        queryObject.setParameter("2", noteype);
        Field[] allFields = Notes.class.getDeclaredFields();
        for (Field field : allFields) {
            if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "String")) {
                queryObject.addScalar(field.getName(), StringType.INSTANCE);
            } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Boolean")) {
                queryObject.addScalar(field.getName(), BooleanType.INSTANCE);
            } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Date")) {
                queryObject.addScalar(field.getName(), TimestampType.INSTANCE);
            } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Integer")) {
                queryObject.addScalar(field.getName(), IntegerType.INSTANCE);
            }
        }
        queryObject.setResultTransformer(Transformers.aliasToBean(Notes.class));
        return queryObject.list();
    }

    public void setASVoid(Integer noteId) throws Exception {
        String queryString = "update Notes set void=?0 where id=?1";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", VOID_NOTES);
        queryObject.setParameter("1", noteId);
        queryObject.executeUpdate();
    }

    public void setASClosed(Integer noteId) throws Exception {
        String queryString = "update Notes set status=?0 where id=?1";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", STATUS_COMPLETED);
        queryObject.setParameter("1", noteId);
        queryObject.executeUpdate();
    }

    public List findVoidNotesByModuleId(String moduleId, String moduleRefId, String itemName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        String itmNam = "";
        if (CommonFunctions.isNotNull(itemName) && DISPUTEDBLCODE.equals(itemName)) {
            itmNam = " and item_Name='" + itemName + "'";
        }
        queryBuilder.append("select id,module_id as moduleId,updateDate,note_type as noteType,note_desc as noteDesc,");
        queryBuilder.append("updated_by as updatedBy,item_Name as itemName,void as voidNote,module_ref_id as moduleRefId,");
        queryBuilder.append("followup_date as followupDate,status,unique_id as uniqueId,removed,print_on_report as printOnReport ");
        queryBuilder.append("from notes where module_id=?0 and module_ref_id=?1 and void=?2 and status = ?3 ").append(itmNam);
        queryBuilder.append(" order by updateDate desc limit 10");

        SQLQuery queryObject = getSession().createSQLQuery(queryBuilder.toString());
        queryObject.setParameter("0", moduleId);
        queryObject.setParameter("1", moduleRefId);
        queryObject.setParameter("2", VOID_NOTES);
        queryObject.setParameter("3", STATUS_PENDING);
        Field[] allFields = Notes.class.getDeclaredFields();
        for (Field field : allFields) {
            if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "String")) {
                queryObject.addScalar(field.getName(), StringType.INSTANCE);
            } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Boolean")) {
                queryObject.addScalar(field.getName(), BooleanType.INSTANCE);
            } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Date")) {
                queryObject.addScalar(field.getName(), TimestampType.INSTANCE);
            } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Integer")) {
                queryObject.addScalar(field.getName(), IntegerType.INSTANCE);
            }
        }
        queryObject.setResultTransformer(Transformers.aliasToBean(Notes.class));
        return queryObject.list();
    }

    public List findPastFollowupDateNotes(String moduleId, String moduleRefId, String itemName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        String itmNam = "";
        if (CommonFunctions.isNotNull(itemName) && DISPUTEDBLCODE.equals(itemName)) {
            itmNam = " and item_Name='" + itemName + "'";
        }
        queryBuilder.append("select id,module_id as moduleId,updateDate,note_type as noteType,note_desc as noteDesc,");
        queryBuilder.append("updated_by as updatedBy,item_Name as itemName,void as voidNote,module_ref_id as moduleRefId,");
        queryBuilder.append("followup_date as followupDate,status,unique_id as uniqueId,removed,print_on_report as printOnReport ");
        queryBuilder.append("from notes where module_id=?0 and module_ref_id=?1 and void=?2 and status = ?3 ").append(itmNam);
        queryBuilder.append(" order by followup_date desc limit 10");

        SQLQuery queryObject = getSession().createSQLQuery(queryBuilder.toString());
        queryObject.setParameter("0", moduleId);
        queryObject.setParameter("1", moduleRefId);
        queryObject.setParameter("2", UNVOID_NOTES);
        queryObject.setParameter("3", STATUS_COMPLETED);
        Field[] allFields = Notes.class.getDeclaredFields();
        for (Field field : allFields) {
            if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "String")) {
                queryObject.addScalar(field.getName(), StringType.INSTANCE);
            } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Boolean")) {
                queryObject.addScalar(field.getName(), BooleanType.INSTANCE);
            } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Date")) {
                queryObject.addScalar(field.getName(), TimestampType.INSTANCE);
            } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Integer")) {
                queryObject.addScalar(field.getName(), IntegerType.INSTANCE);
            }
        }
        queryObject.setResultTransformer(Transformers.aliasToBean(Notes.class));
        return queryObject.list();
    }

    public List findExistsFollowupDateNotes(String moduleId, String moduleRefId, String itemName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        String itmNam = "";
        if (CommonFunctions.isNotNull(itemName) && DISPUTEDBLCODE.equals(itemName)) {
            itmNam = " and item_Name='" + itemName + "'";
        }
        queryBuilder.append("select id,module_id as moduleId,updateDate,note_type as noteType,note_desc as noteDesc,");
        queryBuilder.append("updated_by as updatedBy,item_Name as itemName,void as voidNote,module_ref_id as moduleRefId,");
        queryBuilder.append("followup_date as followupDate,status,unique_id as uniqueId,removed,print_on_report as printOnReport ");
        queryBuilder.append("from notes where module_id=?0 and module_ref_id=?1 and void=?2 and status = ?3 ").append(itmNam);
        queryBuilder.append(" and followup_date is not null order by followup_date desc limit 10");

        SQLQuery queryObject = getSession().createSQLQuery(queryBuilder.toString());
        queryObject.setParameter("0", moduleId);
        queryObject.setParameter("1", moduleRefId);
        queryObject.setParameter("2", UNVOID_NOTES);
        queryObject.setParameter("3", STATUS_PENDING);
        Field[] allFields = Notes.class.getDeclaredFields();
        for (Field field : allFields) {
            if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "String")) {
                queryObject.addScalar(field.getName(), StringType.INSTANCE);
            } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Boolean")) {
                queryObject.addScalar(field.getName(), BooleanType.INSTANCE);
            } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Date")) {
                queryObject.addScalar(field.getName(), TimestampType.INSTANCE);
            } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Integer")) {
                queryObject.addScalar(field.getName(), IntegerType.INSTANCE);
            }
        }
        queryObject.setResultTransformer(Transformers.aliasToBean(Notes.class));
        return queryObject.list();
    }

    public List findByNoteType(String moduleId, String moduleRefId, String noteType, String itemName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        String itmNam = "";
        if (CommonFunctions.isNotNull(itemName) && DISPUTEDBLCODE.equals(itemName)) {
            itmNam = " and item_Name='" + itemName + "'";
        }
        queryBuilder.append("select id,module_id as moduleId,updateDate,note_type as noteType,note_desc as noteDesc,");
        queryBuilder.append("updated_by as updatedBy,item_Name as itemName,void as voidNote,module_ref_id as moduleRefId,");
        queryBuilder.append("followup_date as followupDate,status,unique_id as uniqueId,removed,print_on_report as printOnReport ");
        queryBuilder.append("from notes where module_id=?0 and module_ref_id=?1 and note_type=?2").append(itmNam);
        queryBuilder.append(" order by updateDate desc");

        SQLQuery queryObject = getSession().createSQLQuery(queryBuilder.toString());
        queryObject.setParameter("0", moduleId);
        queryObject.setParameter("1", moduleRefId);
        queryObject.setParameter("2", noteType);
        Field[] allFields = Notes.class.getDeclaredFields();
        for (Field field : allFields) {
            if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "String")) {
                queryObject.addScalar(field.getName(), StringType.INSTANCE);
            } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Boolean")) {
                queryObject.addScalar(field.getName(), BooleanType.INSTANCE);
            } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Date")) {
                queryObject.addScalar(field.getName(), TimestampType.INSTANCE);
            } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Integer")) {
                queryObject.addScalar(field.getName(), IntegerType.INSTANCE);
            }
        }
        queryObject.setResultTransformer(Transformers.aliasToBean(Notes.class));
        return queryObject.list();
    }

    public List findByDisputedNotes(String moduleId, String moduleRefId, String itemName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select id,module_id as moduleId,updateDate,note_type as noteType,note_desc as noteDesc,");
        queryBuilder.append("updated_by as updatedBy,item_Name as itemName,void as voidNote,module_ref_id as moduleRefId,");
        queryBuilder.append("followup_date as followupDate,status,unique_id as uniqueId,removed,print_on_report as printOnReport ");
        queryBuilder.append("from notes where module_id=?0 and module_ref_id=?1 and item_Name=?2");
        queryBuilder.append(" order by updateDate desc limit 10");
        SQLQuery queryObject = getSession().createSQLQuery(queryBuilder.toString());
        queryObject.setParameter("0", moduleId);
        queryObject.setParameter("1", moduleRefId);
        queryObject.setParameter("2", itemName);
        Field[] allFields = Notes.class.getDeclaredFields();
        for (Field field : allFields) {
            if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "String")) {
                queryObject.addScalar(field.getName(), StringType.INSTANCE);
            } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Boolean")) {
                queryObject.addScalar(field.getName(), BooleanType.INSTANCE);
            } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Date")) {
                queryObject.addScalar(field.getName(), TimestampType.INSTANCE);
            } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Integer")) {
                queryObject.addScalar(field.getName(), IntegerType.INSTANCE);
            }
        }
        queryObject.setResultTransformer(Transformers.aliasToBean(Notes.class));
        return queryObject.list();
    }

    public List findByTrackingNotes(String moduleId, String moduleRefId, String itemName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select id,module_id as moduleId,updateDate,note_type as noteType,note_desc as noteDesc,");
        queryBuilder.append("updated_by as updatedBy,item_Name as itemName,void as voidNote,module_ref_id as moduleRefId,");
        queryBuilder.append("followup_date as followupDate,status,unique_id as uniqueId,removed,print_on_report as printOnReport");
        queryBuilder.append("from notes where module_id=?0 and module_ref_id=?1 and item_Name=?2");
        queryBuilder.append(" order by id desc limit 10");
        SQLQuery queryObject = getSession().createSQLQuery(queryBuilder.toString());
        queryObject.setParameter("0", moduleId);
        queryObject.setParameter("1", moduleRefId);
        queryObject.setParameter("2", itemName);
        Field[] allFields = Notes.class.getDeclaredFields();
        for (Field field : allFields) {
            if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "String")) {
                queryObject.addScalar(field.getName(), StringType.INSTANCE);
            } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Boolean")) {
                queryObject.addScalar(field.getName(), BooleanType.INSTANCE);
            } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Date")) {
                queryObject.addScalar(field.getName(), TimestampType.INSTANCE);
            } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Integer")) {
                queryObject.addScalar(field.getName(), IntegerType.INSTANCE);
            }
        }
        queryObject.setResultTransformer(Transformers.aliasToBean(Notes.class));
        return queryObject.list();

    }

    public int getNotesCountForFollowupDatePastAndNotClosed(String moduleId, String moduleRefId) throws Exception {
        int count = 0;
        String queryString = "select count(*) from notes where module_id=?0 and module_ref_id=?1 and void=?2 and status = ?3 and followup_date < current_date()";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", moduleId);
        queryObject.setParameter("1", moduleRefId);
        queryObject.setParameter("2", UNVOID_NOTES);
        queryObject.setParameter("3", STATUS_PENDING);
        count = Integer.parseInt(queryObject.uniqueResult().toString());
        return count;
    }

    public int getNotesCountForFollowupDateExistsAndNotClosed(String moduleId, String moduleRefId) throws Exception {
        int count = 0;
        String queryString = "select count(*) from notes where module_id=?0 and module_ref_id=?1 and void=?2 and status = ?3 and followup_date >= current_date()";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", moduleId);
        queryObject.setParameter("1", moduleRefId);
        queryObject.setParameter("2", UNVOID_NOTES);
        queryObject.setParameter("3", STATUS_PENDING);
        count = Integer.parseInt(queryObject.uniqueResult().toString());
        return count;
    }

    public int getNotesCount(String moduleId, String moduleRefId) throws Exception {
        int count = 0;
        String queryString = "select count(*) from notes where module_id=?0 and module_ref_id=?1";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", moduleId);
        queryObject.setParameter("1", moduleRefId);
        count = Integer.parseInt(queryObject.uniqueResult().toString());
        return count;
    }

    public List getAllDeletedNotes(String moduleId, String moduleRefId, String noteDesc) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Notes.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (CommonFunctions.isNotNull(moduleId)) {
            criteria.add(Restrictions.like("moduleId", moduleId));
        }
        if (CommonFunctions.isNotNull(moduleId)) {
            criteria.add(Restrictions.like("moduleRefId", moduleRefId));
        }
        if (CommonFunctions.isNotNull(moduleId)) {
            criteria.add(Restrictions.like("noteDesc", "%" + noteDesc + "%"));
        }
        criteria.addOrder(Order.desc("updateDate"));
        criteria.addOrder(Order.desc("id"));
        return criteria.list();
    }

    public List<Notes> getFollowUpNotes() throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Notes.class);
        criteria.add(Restrictions.eq("status", "Pending"));
        Date startDate = DateUtils.parseDate(DateUtils.formatDate(new Date(), "yyyy-MM-dd 00:00:00"), "yyyy-MM-dd HH:mm:ss");
        Date endDate = DateUtils.parseDate(DateUtils.formatDate(new Date(), "yyyy-MM-dd 23:59:59"), "yyyy-MM-dd HH:mm:ss");
        criteria.add(Restrictions.between("followupDate", startDate, endDate));
        criteria.add(Restrictions.ne("voidNote", VOID_NOTES));
        criteria.addOrder(Order.asc("updatedBy"));
        return criteria.list();
    }

    public List getAccrualNotes(String fileNo) throws Exception {
        String transIds = "";
        List queryList = new ArrayList();
        if (fileNo != null && !fileNo.equals("")) {
            transIds = getTransactionLedgerId(fileNo);
            if (null != transIds && !transIds.equals("")) {
                StringBuilder queryBuilder = new StringBuilder();
                queryBuilder.append("select id,module_id as moduleId,updateDate,note_type as noteType,note_desc as noteDesc,");
                queryBuilder.append("updated_by as updatedBy,item_Name as itemName,void as voidNote,module_ref_id as moduleRefId,");
                queryBuilder.append("followup_date as followupDate,status,unique_id as uniqueId,removed,print_on_report as printOnReport ");
                queryBuilder.append("from notes where module_id='ACCRUALS' and void<>'Y' and module_ref_id in");
                queryBuilder.append("(").append(transIds).append(") order by updateDate desc");
                SQLQuery queryObject = getSession().createSQLQuery(queryBuilder.toString());
                Field[] allFields = Notes.class.getDeclaredFields();
                for (Field field : allFields) {
                    if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "String")) {
                        queryObject.addScalar(field.getName(), StringType.INSTANCE);
                    } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Boolean")) {
                        queryObject.addScalar(field.getName(), BooleanType.INSTANCE);
                    } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Date")) {
                        queryObject.addScalar(field.getName(), TimestampType.INSTANCE);
                    } else if (CommonUtils.isEqualIgnoreCase(field.getType().getSimpleName(), "Integer")) {
                        queryObject.addScalar(field.getName(), IntegerType.INSTANCE);
                    }
                }
                queryList = queryObject.setResultTransformer(Transformers.aliasToBean(Notes.class)).list();
            }
        }
        return queryList;

    }

    public String getTransactionLedgerId(String fileNo) throws Exception {
        String result = "";
        if (fileNo != null && !fileNo.equals("")) {
            String queryString = "SELECT CAST(GROUP_CONCAT(CONCAT(" + "\"'\"" + ",tl.transaction_Id," + "\"'\"" + ")) AS CHAR) FROM Transaction_Ledger tl WHERE tl.transaction_Type='AC' AND tl.bill_Ladding_No =(SELECT bolid FROM fcl_bl WHERE file_no='" + fileNo + "' ORDER BY bolid LIMIT 1)";
            SQLQuery queryObject = getSession().createSQLQuery(queryString);
            result = null != queryObject.uniqueResult() ? queryObject.uniqueResult().toString() : "";
        }
        return result;
    }

    public String buildCommonFollowupCondition(String showNotes) {
        StringBuilder queryBuilder = new StringBuilder();
        if (CommonUtils.isEqualIgnoreCase(showNotes, "last3days")) {
            queryBuilder.append("      n.`followup_date` between current_date() - interval 3 day");
            queryBuilder.append("      and current_date() + interval 23 hour + interval 59 minute + interval 59 second");
        } else if (CommonUtils.isEqualIgnoreCase(showNotes, "next3days")) {
            queryBuilder.append("      n.`followup_date` between current_date()");
            queryBuilder.append("      and current_date() + interval 3 day + interval 23 hour + interval 59 minute + interval 59 second");
        } else if (CommonUtils.isEqualIgnoreCase(showNotes, "todayDate")) {
            queryBuilder.append("      n.`followup_date` between current_date()");
            queryBuilder.append("      and current_date() + interval 23 hour + interval 59 minute + interval 59 second");
        } else {
            queryBuilder.append("      n.`followup_date` is not null");
        }
        queryBuilder.append("      and n.`updated_by` = :userName");
        queryBuilder.append("      and n.`note_type` = 'manual'");
        queryBuilder.append("      and n.`removed` = false");
        queryBuilder.append("      and n.`status` = 'Pending'");
        queryBuilder.append("      and n.`void` = 'N'");
        return queryBuilder.toString();
    }

    private String buildCommonFollowUpQuery(String showNotes) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("    (select");
        queryBuilder.append("      n.`followup_date` as followup_date,");
        queryBuilder.append("      if(");
        queryBuilder.append("        n.`module_id` in ('AR_CONFIGURATION', 'AR_INVOICE'),");
        queryBuilder.append("        'AR_INQUIRY',");
        queryBuilder.append("        if(");
        queryBuilder.append("          n.`module_id` in ('AP_CONFIGURATION', 'AP_INVOICE'),");
        queryBuilder.append("          'AP_INQUIRY',");
        queryBuilder.append("          if(");
        queryBuilder.append("            n.`module_id` = 'FILE',");
        queryBuilder.append("            'FCL_FILE',");
        queryBuilder.append("            n.`module_id`");
        queryBuilder.append("          )");
        queryBuilder.append("        )");
        queryBuilder.append("      ) as module,");
        queryBuilder.append("      n.`module_ref_id` as reference,");
        queryBuilder.append("      n.`note_desc` as description,");
        queryBuilder.append("      n.`id` as id");
        queryBuilder.append("    from");
        queryBuilder.append("      `notes` n");
        queryBuilder.append("    where ").append(buildCommonFollowupCondition(showNotes));
        queryBuilder.append("    )");
        return queryBuilder.toString();
    }

    public String buildLclFollowupCondition(String showNotes) {
        StringBuilder queryBuilder = new StringBuilder();
        if (CommonUtils.isEqualIgnoreCase(showNotes, "last3days")) {
            queryBuilder.append("      n.`followup_datetime` between current_date() - interval 3 day");
            queryBuilder.append("      and current_date() + interval 23 hour + interval 59 minute + interval 59 second");
        } else if (CommonUtils.isEqualIgnoreCase(showNotes, "next3days")) {
            queryBuilder.append("      n.`followup_datetime` between current_date()");
            queryBuilder.append("      and current_date() + interval 3 day + interval 23 hour + interval 59 minute + interval 59 second");
        } else if (CommonUtils.isEqualIgnoreCase(showNotes, "todayDate")) {
            queryBuilder.append("      n.`followup_datetime` between current_date()");
            queryBuilder.append("      and current_date() + interval 23 hour + interval 59 minute + interval 59 second");
        } else {
            queryBuilder.append("      n.`followup_datetime` is not null");
        }
        queryBuilder.append("      and n.`modified_by_user_id` = :userId");
        queryBuilder.append("      and n.`type` <> 'void'");
        return queryBuilder.toString();
    }

    private String buildLclVoyageFollowUpQuery(String showNotes) {
        String condition = buildLclFollowupCondition(showNotes);
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("    (select");
        queryBuilder.append("      n.`followup_datetime` as followup_date,");
        queryBuilder.append("      'LCL_VOYAGE' as module,");
        queryBuilder.append("      (select h.`schedule_no` from `lcl_ss_header` h where h.`id` = n.`ss_header_id`) as reference,");
        queryBuilder.append("      n.`remarks` as description,");
        queryBuilder.append("      n.`id`");
        queryBuilder.append("    from");
        queryBuilder.append("      `lcl_unit_ss_remarks` n");
        queryBuilder.append("    where ").append(condition);
        queryBuilder.append("    )");
        queryBuilder.append("    union");
        queryBuilder.append("    (select");
        queryBuilder.append("      n.`followup_datetime` as followup_date,");
        queryBuilder.append("      'LCL_VOYAGE' as module,");
        queryBuilder.append("      (select h.`schedule_no` from `lcl_ss_header` h where h.`id` = n.`ss_header_id`) as reference,");
        queryBuilder.append("      n.`remarks` as description,");
        queryBuilder.append("      n.`id`");
        queryBuilder.append("    from");
        queryBuilder.append("      `lcl_ss_remarks` n");
        queryBuilder.append("    where ").append(condition);
        queryBuilder.append("    )");
        return queryBuilder.toString();
    }

    private String buildLclFileFollowUpQuery(String showNotes) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("    (select");
        queryBuilder.append("      n.`followup_datetime` as followup_date,");
        queryBuilder.append("      'LCL_FILE' as module,");
        queryBuilder.append("      (select ");
        queryBuilder.append("        f.`file_number`");
        queryBuilder.append("      from");
        queryBuilder.append("        `lcl_file_number` f");
        queryBuilder.append("      where f.`id` = n.`file_number_id`) as reference,");
        queryBuilder.append("      n.`remarks` as description,");
        queryBuilder.append("      n.`id` ");
        queryBuilder.append("    from");
        queryBuilder.append("      `lcl_remarks` n ");
        queryBuilder.append("    where ").append(buildLclFollowupCondition(showNotes));
        queryBuilder.append("    )");
        return queryBuilder.toString();
    }

    private String buildApPastDueFollowUpQuery() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("    (select");
        queryBuilder.append("      sysdate() as followup_date,");
        queryBuilder.append("      'ACCOUNT_PAYABLE' as module,");
        queryBuilder.append("      ap.`acct_no` as reference,");
        queryBuilder.append("      concat(");
        queryBuilder.append("        ap.`invoice_count`,");
        queryBuilder.append("        ' invoices past due for ',");
        queryBuilder.append("        format(ap.`balance`, 2),");
        queryBuilder.append("        ' USD'");
        queryBuilder.append("      ) as description,");
        queryBuilder.append("      null as id");
        queryBuilder.append("    from");
        queryBuilder.append("      (select");
        queryBuilder.append("        ap.`acct_name`,");
        queryBuilder.append("        ap.`acct_no`,");
        queryBuilder.append("        count(tr.`transaction_id`) as invoice_count,");
        queryBuilder.append("        sum(tr.`balance`) as balance ");
        queryBuilder.append("      from");
        queryBuilder.append("        (select");
        queryBuilder.append("          tp.`acct_name`,");
        queryBuilder.append("          tp.`acct_no`,");
        queryBuilder.append("          coalesce(");
        queryBuilder.append("            `CodeGetCodeById`(ap.`credit_terms`),");
        queryBuilder.append("            0");
        queryBuilder.append("          ) as credit_terms");
        queryBuilder.append("        from");
        queryBuilder.append("          `trading_partner` tp");
        queryBuilder.append("          join `vendor_info` ap");
        queryBuilder.append("            on (");
        queryBuilder.append("              ap.`cust_accno` = tp.`acct_no`");
        queryBuilder.append("              and ap.`ap_specialist` = :apSpecialist");
        queryBuilder.append("            )");
        queryBuilder.append("        where tp.`ecu_designation` = 'LO'");
        queryBuilder.append("        group by tp.`acct_no`) as ap");
        queryBuilder.append("        join `transaction` tr");
        queryBuilder.append("          on (");
        queryBuilder.append("            tr.`cust_no` = ap.`acct_no`");
        queryBuilder.append("            and tr.`transaction_type` = 'AP'");
        queryBuilder.append("            and tr.`status` = 'Open'");
        queryBuilder.append("            and tr.`balance` <> 0.00");
        queryBuilder.append("            and datediff(sysdate(), tr.`transaction_date`) > ap.`credit_terms`");
        queryBuilder.append("          ) ");
        queryBuilder.append("      group by tr.`cust_no`) as ap");
        queryBuilder.append("    where ap.`balance` <> 0.00)");
        return queryBuilder.toString();
    }

    private String buildApPastDue30DaysFollowUpQuery() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("    (select ");
        queryBuilder.append("      sysdate() as followup_date,");
        queryBuilder.append("      'ACCOUNT_PAYABLE' as module,");
        queryBuilder.append("      ap.`acct_no` as reference,");
        queryBuilder.append("      concat(");
        queryBuilder.append("        ap.`invoice_count`,");
        queryBuilder.append("        ' invoices past due for ',");
        queryBuilder.append("        format(ap.`balance`, 2),");
        queryBuilder.append("        ' USD'");
        queryBuilder.append("      ) as description,");
        queryBuilder.append("      null as id");
        queryBuilder.append("    from");
        queryBuilder.append("      (select");
        queryBuilder.append("        ap.`acct_name`,");
        queryBuilder.append("        ap.`acct_no`,");
        queryBuilder.append("        count(tr.`transaction_id`) as invoice_count,");
        queryBuilder.append("        sum(tr.`balance`) as balance ");
        queryBuilder.append("      from");
        queryBuilder.append("        (select");
        queryBuilder.append("          tp.`acct_name`,");
        queryBuilder.append("          tp.`acct_no`,");
        queryBuilder.append("          coalesce(");
        queryBuilder.append("            `CodeGetCodeById`(ap.`credit_terms`),");
        queryBuilder.append("            0");
        queryBuilder.append("          ) as credit_terms");
        queryBuilder.append("        from");
        queryBuilder.append("          `trading_partner` tp");
        queryBuilder.append("          join `vendor_info` ap");
        queryBuilder.append("            on (ap.`cust_accno` = tp.`acct_no`)");
        queryBuilder.append("        where tp.`ecu_designation` = 'LO'");
        queryBuilder.append("        group by tp.`acct_no`) as ap");
        queryBuilder.append("        join `transaction` tr");
        queryBuilder.append("          on (");
        queryBuilder.append("            tr.`cust_no` = ap.`acct_no`");
        queryBuilder.append("            and tr.`transaction_type` = 'AP'");
        queryBuilder.append("            and tr.`status` = 'Open'");
        queryBuilder.append("            and tr.`balance` <> 0.00");
        queryBuilder.append("            and datediff(sysdate(), tr.`transaction_date`) > (ap.`credit_terms` + 30)");
        queryBuilder.append("          )");
        queryBuilder.append("      group by tr.`cust_no`) as ap");
        queryBuilder.append("    where ap.`balance` <> 0.00)");
        return queryBuilder.toString();
    }

    private String buildArPastDue120DaysFollowUpQuery() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("    (select");
        queryBuilder.append("      sysdate() as followup_date,");
        queryBuilder.append("      'AR_INQUIRY' as module,");
        queryBuilder.append("      ar.`acct_no` as reference,");
        queryBuilder.append("      concat(");
        queryBuilder.append("        ar.`invoice_count`,");
        queryBuilder.append("        ' invoices for ',");
        queryBuilder.append("        format(ar.`balance`, 2),");
        queryBuilder.append("        ' USD'");
        queryBuilder.append("      ) as description,");
        queryBuilder.append("      null as id");
        queryBuilder.append("    from");
        queryBuilder.append("      (select");
        queryBuilder.append("        tp.`acct_name`,");
        queryBuilder.append("        tp.`acct_no`,");
        queryBuilder.append("        sum(tr.`balance`) as balance,");
        queryBuilder.append("        count(tr.`transaction_id`) as invoice_count");
        queryBuilder.append("      from");
        queryBuilder.append("        (select");
        queryBuilder.append("          tp.`acct_name`,");
        queryBuilder.append("          tp.`acct_no`");
        queryBuilder.append("        from");
        queryBuilder.append("          `trading_partner` tp");
        queryBuilder.append("        where tp.`ecu_designation` <> 'LO') as tp");
        queryBuilder.append("        join `transaction` tr");
        queryBuilder.append("          on (");
        queryBuilder.append("            tr.`cust_no` = tp.`acct_no`");
        queryBuilder.append("            and tr.`transaction_type` = 'AR'");
        queryBuilder.append("            and datediff(sysdate(), tr.`transaction_date`) > 120");
        queryBuilder.append("            and tr.`balance` <> 0.00");
        queryBuilder.append("          )");
        queryBuilder.append("      group by tr.`cust_no`) as ar");
        queryBuilder.append("    where ar.`balance` > 25000.00)");
        return queryBuilder.toString();
    }

    public List<NotesModel> getFollowUpTasks(User user, String showNotes) throws Exception {
        getCurrentSession().flush();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  date_format(n.`followup_date`, '%m/%d/%Y %H:%i %p') as followupDate,");
        queryBuilder.append("  n.`module` as module,");
        queryBuilder.append("  n.`reference` as reference,");
        queryBuilder.append("  n.`description` as description,");
        queryBuilder.append("  n.`id` as id ");
        queryBuilder.append("from");
        queryBuilder.append("  (");
        queryBuilder.append("    ").append(buildCommonFollowUpQuery(showNotes));
        queryBuilder.append("    union");
        queryBuilder.append("    ").append(buildLclVoyageFollowUpQuery(showNotes));
        queryBuilder.append("    union");
        queryBuilder.append("    ").append(buildLclFileFollowUpQuery(showNotes));
        if (ConstantsInterface.ROLE_NAME_APSPECIALIST.equalsIgnoreCase(user.getRole().getRoleDesc())) {
            queryBuilder.append("    union");
            queryBuilder.append("    ").append(buildApPastDueFollowUpQuery());
        }
        if (ConstantsInterface.ROLE_NAME_AP_MANAGER.equalsIgnoreCase(user.getRole().getRoleDesc())) {
            queryBuilder.append("    union");
            queryBuilder.append("    ").append(buildApPastDue30DaysFollowUpQuery());
            queryBuilder.append("    union");
            queryBuilder.append("    ").append(buildArPastDue120DaysFollowUpQuery());
        }
        queryBuilder.append("  ) as n ");
        queryBuilder.append("order by n.`followup_date` desc ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("userName", user.getLoginName());
        query.setInteger("userId", user.getUserId());
        query.addScalar("followupDate", StringType.INSTANCE);
        if (ConstantsInterface.ROLE_NAME_APSPECIALIST.equalsIgnoreCase(user.getRole().getRoleDesc())) {
            query.setInteger("apSpecialist", user.getUserId());
        }
        query.addScalar("module", StringType.INSTANCE);
        query.addScalar("reference", StringType.INSTANCE);
        query.addScalar("description", StringType.INSTANCE);
        query.addScalar("id", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(NotesModel.class));
        return query.list();
    }

    public boolean isManualNotes(String documentId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select if(count(*)>0,'true','false') as manualNotes from notes");
        queryBuilder.append(" where module_id = 'FILE'");
        queryBuilder.append(" and module_ref_id = '").append(documentId).append("'");
        queryBuilder.append(" and note_type = 'manual'");
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null != result ? Boolean.valueOf(result.toString()) : false;
    }

    public boolean isManualNotesCorrection(String documentId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select if(count(*)>0,'true','false') as manualNotes from notes");
        queryBuilder.append(" where module_id = 'Correction'");
        queryBuilder.append(" and module_ref_id = '").append(documentId).append("'");
        queryBuilder.append(" and note_type = 'manual'");
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null != result ? Boolean.valueOf(result.toString()) : false;
    }

    public boolean isCustomerNotes(String acctNo) throws Exception {
        String databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        StringBuilder sb = new StringBuilder();
        sb.append("(SELECT IF(CONVERT(IF (tp.eci_acct_no IS NOT NULL, (SELECT COUNT(*) FROM ").append(databaseSchema).append(".ffnote blueShip WHERE blueShip.actnum = tp.eci_acct_no ");
        sb.append("AND (blueShip.autnot != '*')), '' ) USING utf8 )!=0,TRUE,FALSE) AS countvalues FROM trading_partner tp WHERE tp.acct_no ='").append(acctNo).append("')");
        sb.append("UNION");
        sb.append("(SELECT IF(CONVERT(IF (tp.ECIFWNO IS NOT NULL, (SELECT COUNT(*) FROM ").append(databaseSchema).append(".cnnote blueCon WHERE blueCon.actnum = tp.ECIFWNO ");
        sb.append("AND (blueCon.autnot != '*')), '' ) USING utf8 )!=0,TRUE,FALSE) AS countvalues FROM trading_partner tp WHERE tp.acct_no ='").append(acctNo).append("')");
        ArrayList<BigInteger> notesCount = (ArrayList) getCurrentSession().createSQLQuery(sb.toString()).list();
        return null != notesCount && notesCount.contains(BigInteger.ONE);
    }

    public boolean isCustomerNotesForImports(String acctNo) throws Exception {
        String databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        StringBuilder sb = new StringBuilder();
        sb.append("(SELECT IF(CONVERT(IF (tp.eci_acct_no IS NOT NULL, (SELECT COUNT(*) FROM ").append(databaseSchema).append(".ffnote blueShip WHERE blueShip.actnum = tp.eci_acct_no ");
        sb.append("AND (blueShip.autnot = 'I' OR blueShip.autnot = 'G')), '' ) USING utf8 )!=0,TRUE,FALSE) AS countvalues FROM trading_partner tp WHERE tp.acct_no ='").append(acctNo).append("')");
        sb.append("UNION");
        sb.append("(SELECT IF(CONVERT(IF (tp.ECIFWNO IS NOT NULL, (SELECT COUNT(*) FROM ").append(databaseSchema).append(".cnnote blueCon WHERE blueCon.actnum = tp.ECIFWNO ");
        sb.append("AND (blueCon.autnot = 'I' OR blueCon.autnot = 'G')), '' ) USING utf8 )!=0,TRUE,FALSE) AS countvalues FROM trading_partner tp WHERE tp.acct_no ='").append(acctNo).append("')");
        ArrayList<BigInteger> notesCount = (ArrayList) getCurrentSession().createSQLQuery(sb.toString()).list();
        return null != notesCount && notesCount.contains(BigInteger.ONE);
    }

    public boolean isShipCustomerNotesForImports(String acctNo) throws Exception {
        String databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT CONVERT(IF (tp.eci_acct_no IS NOT NULL,(SELECT COUNT(*) FROM ").append(databaseSchema).append(".ffnote blue WHERE blue.actnum = tp.eci_acct_no AND blue.autnot!='*'),'')");
        sb.append(" using utf8) AS countvalues FROM trading_partner tp WHERE tp.acct_no = '").append(acctNo).append("'");
        Object notesCount = (String) getCurrentSession().createSQLQuery(sb.toString()).uniqueResult();
        return null != notesCount && !notesCount.toString().equals("") && !notesCount.toString().equals("0");
    }

    public List<Notes> getBlueScreenNotes(BlueScreenNotesForm blueScreenNotesForm) throws Exception {
        String databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        StringBuilder sb = new StringBuilder();
        sb.append("(SELECT ff.autnot AS noteType,ff.ntvoid AS status,ff.notlne AS noteDesc,TIMESTAMP(datent,timent) AS itemName,ff.user AS updatedBy, '' AS tpId ");
        sb.append("FROM ").append(databaseSchema).append(".ffnote ff JOIN trading_partner tp ON tp.eci_acct_no=ff.actnum ");
        sb.append(appendWhereCondition(blueScreenNotesForm.getCustomerNo(), blueScreenNotesForm.getNoteSymbol()));
        sb.append(" UNION ");
        sb.append("(SELECT ff.autnot AS noteType,ff.ntvoid AS status,ff.notlne AS noteDesc,TIMESTAMP(datent,timent) AS itemName,ff.user AS updatedBy, '' AS tpId ");
        sb.append("FROM ").append(databaseSchema).append(".cnnote ff JOIN trading_partner tp ON tp.ecifwno=ff.actnum ");
        sb.append(appendWhereCondition(blueScreenNotesForm.getCustomerNo(), blueScreenNotesForm.getNoteSymbol()));
        sb.append(" UNION ");
        sb.append("(SELECT ");
        sb.append("  tp.note_type AS noteType,");
        sb.append("  '',");
        sb.append("  tp.note_desc AS noteDesc,");
        sb.append("  TIMESTAMP(tp.entered_datetime) AS itemName,");
        sb.append("  ud.login_name AS updatedBy, ");
        sb.append(" tp.id AS tpId");
        sb.append(" FROM ");
        sb.append("  tp_note tp ");
        sb.append("  JOIN `user_details` ud ");
        sb.append("    ON tp.`entered_by_user_id` = ud.user_id ");
        sb.append(tpWhereCondition(blueScreenNotesForm.getCustomerNo(), blueScreenNotesForm.getNoteSymbol()));
        if (null == blueScreenNotesForm.getSortBy() || CommonUtils.isEmpty(blueScreenNotesForm.getSortBy())) {
            sb.append(" order by  itemName desc ");
        } else {
            sb.append(" order by  itemName ").append(blueScreenNotesForm.getSortBy());
        }
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setResultTransformer(Transformers.aliasToBean(Notes.class));
        query.addScalar("noteType", StringType.INSTANCE);
        query.addScalar("status", StringType.INSTANCE);
        query.addScalar("noteDesc", StringType.INSTANCE);
        query.addScalar("itemName", StringType.INSTANCE);
        query.addScalar("updatedBy", StringType.INSTANCE);
        query.addScalar("tpId", LongType.INSTANCE);
        return query.list();
    }

    private String tpWhereCondition(String acctNo, String symbol) throws Exception {
        List shcnAcctNo = null;
        StringBuilder sb = new StringBuilder();
        TpNoteDao tpNoteDao = new TpNoteDao();
        shcnAcctNo = tpNoteDao.getAcctNo(acctNo);
        if (CommonUtils.isNotEmpty(shcnAcctNo)) {
            Object[] tpAcctNo = (Object[]) shcnAcctNo.get(0);
            sb.append(" WHERE ");
            if (tpAcctNo[0] != null && !tpAcctNo[0].equals("") && tpAcctNo[1] != null && !tpAcctNo[1].equals("")) {
                sb.append(" ( ");
                sb.append("  tp.actnum ='").append(tpAcctNo[0].toString()).append("'");
                sb.append(" OR ");
                sb.append(" tp.connum ='").append(tpAcctNo[1].toString()).append("'");
                sb.append(")");
            } else {
                if (tpAcctNo[0] != null && !tpAcctNo[0].equals("")) {
                    sb.append("  tp.actnum ='").append(tpAcctNo[0].toString()).append("'");
                }
                if (tpAcctNo[1] != null && !tpAcctNo[1].equals("")) {
                    sb.append(" tp.connum ='").append(tpAcctNo[1].toString()).append("'");
                }
            }

        }
        if (null != symbol && !"".equalsIgnoreCase(symbol)) {
            if (!"V".equalsIgnoreCase(symbol)) {
                sb.append(" AND tp.note_type='").append(symbol).append("' AND tp.note_type <> 'V'");
            } else {
                sb.append(" AND tp.note_type ='V'");
            }
        } else {
            sb.append(" AND tp.note_type <>'V'");
        }
        sb.append(")");
        return sb.toString();
    }

    private String tpWhereConditionSymbol(String acctNo) throws Exception {
        StringBuilder sb = new StringBuilder();
        List shcnAcctNo = null;
        TpNoteDao tpNoteDao = new TpNoteDao();
        shcnAcctNo = tpNoteDao.getAcctNo(acctNo);
        if (CommonUtils.isNotEmpty(shcnAcctNo)) {
            Object[] tpAcctNo = (Object[]) shcnAcctNo.get(0);
            sb.append(" WHERE ");
            if (tpAcctNo[0] != null && !tpAcctNo[0].equals("") && tpAcctNo[1] != null && !tpAcctNo[1].equals("")) {
                sb.append(" ( ");
                sb.append("  tp.actnum ='").append(tpAcctNo[0].toString()).append("'");
                sb.append(" OR ");
                sb.append(" tp.connum ='").append(tpAcctNo[1].toString()).append("'");
                sb.append(")");
            } else {
                if (tpAcctNo[0] != null && !tpAcctNo[0].equals("")) {
                    sb.append("  tp.actnum ='").append(tpAcctNo[0].toString()).append("'");
                }
                if (tpAcctNo[1] != null && !tpAcctNo[1].equals("")) {
                    sb.append(" tp.connum ='").append(tpAcctNo[1].toString()).append("'");
                }
            }
        }
        return sb.toString();
    }

    public String appendWhereCondition(String acctNo, String noteType) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" WHERE tp.acct_no='").append(acctNo).append("'");
        if (null != noteType && !"".equalsIgnoreCase(noteType)) {
            if (!"V".equalsIgnoreCase(noteType)) {
                sb.append(" AND ff.autnot='").append(noteType).append("'").append(" AND ff.ntvoid!='V'");
            } else {
                sb.append(" AND ff.ntvoid='V'");
            }
        } else {
            sb.append(" AND ff.ntvoid!='V'");
        }
        sb.append(")");
        return sb.toString();
    }

    public List getBlueScreenNoteSymbol(String acctNo) throws Exception {
        String databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ff.autnot AS noteType,ff.ntvoid AS voidSymbol ");
        sb.append("FROM ").append(databaseSchema).append(".ffnote ff JOIN trading_partner tp ON tp.eci_acct_no=ff.actnum ");
        sb.append("WHERE tp.acct_no='").append(acctNo).append("'");
        sb.append(" UNION ");
        sb.append("SELECT ff.autnot AS noteType,ff.ntvoid AS voidSymbol ");
        sb.append("FROM ").append(databaseSchema).append(".cnnote ff JOIN trading_partner tp ON tp.ecifwno=ff.actnum ");
        sb.append("WHERE tp.acct_no='").append(acctNo).append("'");
        sb.append(" UNION ");
        sb.append("SELECT ");
        sb.append("  tp.note_type AS noteType,");
        sb.append(" '' ");
        sb.append("FROM");
        sb.append("  tp_note tp ");
        sb.append(tpWhereConditionSymbol(acctNo));
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        return query.list();
    }

    public String getBlueScreenVoidSymbol(String acctNo, String symbol) throws Exception {
        String databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ff.ntvoid AS voidSymbol ");
        sb.append("FROM ").append(databaseSchema).append(".ffnote ff JOIN trading_partner tp ON tp.eci_acct_no=ff.actnum ");
        sb.append("WHERE tp.acct_no='").append(acctNo).append("'").append(" AND ntvoid='V'");
        sb.append(" UNION ");
        sb.append("SELECT ff.ntvoid AS voidSymbol ");
        sb.append("FROM ").append(databaseSchema).append(".cnnote ff JOIN trading_partner tp ON tp.ecifwno=ff.actnum ");
        sb.append("WHERE tp.acct_no='").append(acctNo).append("'").append(" AND ntvoid='V'");
        sb.append(" UNION ");
        sb.append("SELECT ");
        sb.append("  tp.note_type AS noteType");
        sb.append(" FROM");
        sb.append("  tp_note tp ");
        sb.append(tpWhereConditionSymbol(acctNo)).append(" AND tp.note_type ='V'");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        return (String) query.uniqueResult();
    }

    public List<Notes> getChargeCodeNotes(String id) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  'manual' as noteType,");
        queryBuilder.append("  cc.notes as noteDesc,");
        queryBuilder.append("  ab.deposit_date as updateDate,");
        queryBuilder.append("  'Completed' as status,");
        queryBuilder.append("  cc.userName as updatedBy,");
        queryBuilder.append("  cc.id as id ");
        queryBuilder.append("from");
        queryBuilder.append("  payments cc");
        queryBuilder.append("  join ar_batch ab");
        queryBuilder.append("    on (cc.batch_id = ab.batch_id) ");
        queryBuilder.append("where cc.id = ").append(id);
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("noteType", StringType.INSTANCE);
        query.addScalar("noteDesc", StringType.INSTANCE);
        query.addScalar("updateDate", TimestampType.INSTANCE);
        query.addScalar("status", StringType.INSTANCE);
        query.addScalar("updatedBy", StringType.INSTANCE);
        query.addScalar("id", IntegerType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(Notes.class));
        return query.list();
    }

    public void markNotesAsCompleted(String moduleId, String moduleRefId, String noteDesc) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update");
        queryBuilder.append("  notes ");
        queryBuilder.append("set");
        queryBuilder.append("  status = 'Completed' ");
        queryBuilder.append("where");
        queryBuilder.append("  module_id = :moduleId");
        queryBuilder.append("  and module_ref_id = :moduleRefId");
        queryBuilder.append("  and note_desc = :noteDesc");
        queryBuilder.append("  and status = :status");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("moduleId", moduleId);
        query.setString("moduleRefId", moduleRefId);
        query.setString("noteDesc", noteDesc);
        query.setString("status", "Pending");
        query.executeUpdate();
    }
}

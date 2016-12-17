package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.hibernate.HibernateSessionFactory;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

/**
 * Data access object (DAO) for domain model
 *
 * @author MyEclipse - Hibernate Tools
 */
public class BaseHibernateDAO implements IBaseHibernateDAO {

    @Override
    public Session getSession() {
        return this.getCurrentSession();
    }

    public void closeSession() {
        HibernateSessionFactory.closeSession();
    }

    public Session getCurrentSession() {
        return HibernateSessionFactory.getSessionFactory().getCurrentSession();
    }

    public void initializeSessionFactory() {
        HibernateSessionFactory.rebuildSessionFactory();
    }

    public String getConnectionId() {
        String query = "select cast(connection_id() as char character set latin1) as connection_id";
        return (String) getCurrentSession().createSQLQuery(query).setMaxResults(1).uniqueResult();
    }

    public void setGroupConcatMaxLength() {
        String query = "set session group_concat_max_len=128*128*1024";
        getCurrentSession().createSQLQuery(query).executeUpdate();
    }

    public void update(String table, String keyField, Object keyValue, Map<String, Object> values) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update");
        queryBuilder.append(" ");
        queryBuilder.append(table);
        queryBuilder.append(" ");
        queryBuilder.append("set");
        boolean isFirst = true;
        for (String field : values.keySet()) {
            queryBuilder.append(isFirst ? " " : ", ").append(field).append(" = :").append(field);
            isFirst = false;
        }
        queryBuilder.append(" ");
        queryBuilder.append("where");
        queryBuilder.append(" ").append(keyField).append(" = :").append(keyField);
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        for (String field : values.keySet()) {
            Object value = values.get(field);
            if (value instanceof String) {
                query.setString(field, (String) value);
            } else if (value instanceof Integer) {
                query.setInteger(field, (Integer) value);
            } else if (value instanceof Double) {
                query.setDouble(field, (Double) value);
            } else if (value instanceof Long) {
                query.setLong(field, (Long) value);
            } else if (value instanceof BigDecimal) {
                query.setBigDecimal(field, (BigDecimal) value);
            } else if (value instanceof BigInteger) {
                query.setBigInteger(field, (BigInteger) value);
            } else if (value instanceof Boolean) {
                query.setBoolean(field, (Boolean) value);
            } else if (value instanceof Date) {
                query.setDate(field, (Date) value);
            } else if (value instanceof Float) {
                query.setFloat(field, (Float) value);
            }
        }
        if (keyValue instanceof String) {
            query.setString(keyField, (String) keyValue);
        } else if (keyValue instanceof Integer) {
            query.setInteger(keyField, (Integer) keyValue);
        } else if (keyValue instanceof Double) {
            query.setDouble(keyField, (Double) keyValue);
        } else if (keyValue instanceof Long) {
            query.setLong(keyField, (Long) keyValue);
        } else if (keyValue instanceof BigDecimal) {
            query.setBigDecimal(keyField, (BigDecimal) keyValue);
        } else if (keyValue instanceof BigInteger) {
            query.setBigInteger(keyField, (BigInteger) keyValue);
        } else if (keyValue instanceof Boolean) {
            query.setBoolean(keyField, (Boolean) keyValue);
        } else if (keyValue instanceof Date) {
            query.setDate(keyField, (Date) keyValue);
        } else if (keyValue instanceof Float) {
            query.setFloat(keyField, (Float) keyValue);
        }
        query.executeUpdate();
    }

    public Object getFieldValue(Class table, String keyField, Serializable keyValue, String fieldName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append(fieldName);
        queryBuilder.append(" from ");
        queryBuilder.append(table.getSimpleName());
        queryBuilder.append(" where ").append(keyField).append(" = :keyField");
        Query query = getCurrentSession().createQuery(queryBuilder.toString());
        if (keyValue instanceof String) {
            query.setString("keyField", (String) keyValue);
        } else if (keyValue instanceof Integer) {
            query.setInteger("keyField", (Integer) keyValue);
        } else if (keyValue instanceof Long) {
            query.setLong("keyField", (Long) keyValue);
        } else if (keyValue instanceof BigInteger) {
            query.setBigInteger("keyField", (BigInteger) keyValue);
        }
        query.setMaxResults(1);
        return query.uniqueResult();
    }

}

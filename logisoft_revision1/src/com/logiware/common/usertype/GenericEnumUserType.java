package com.logiware.common.usertype;

import org.hibernate.HibernateException;
import org.hibernate.usertype.ParameterizedType;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.BasicTypeRegistry;
import org.hibernate.type.SingleColumnType;
import org.hibernate.usertype.EnhancedUserType;

public class GenericEnumUserType implements EnhancedUserType, ParameterizedType {

    private static final String DEFAULT_IDENTIFIER_METHOD_NAME = "getId";
    private static final String DEFAULT_VALUE_OF_METHOD_NAME = "fromId";
    private static final Class[] NULL_CLASS_VARARG = null;
    private static final Object[] NULL_OBJECT_VARARG = null;
    private static final char SINGLE_QUOTE = '\'';
    private Class<? extends Enum> enumClass;
    private Method identifierMethod;
    private int[] sqlTypes;
    private SingleColumnType<Object> type;
    private Method valueOfMethod;

    public Object assemble(Serializable cached, Object owner) throws HibernateException {
	return cached;
    }

    public Object deepCopy(Object value) throws HibernateException {
	return value;
    }

    public Serializable disassemble(Object value) throws HibernateException {
	return (Serializable) value;
    }

    public boolean equals(Object x, Object y) throws HibernateException {
	return x == y;
    }

    public Object fromXMLString(String xmlValue) {
	return Enum.valueOf(enumClass, xmlValue);
    }

    public int hashCode(Object x) throws HibernateException {
	return x.hashCode();
    }

    public boolean isMutable() {
	return false;
    }

    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
	    throws HibernateException, SQLException {
	Object identifier = type.nullSafeGet(rs, names[0], session);
	if (identifier == null || rs.wasNull()) {
	    return null;
	}
	try {
	    return valueOfMethod.invoke(enumClass, identifier);
	} catch (Exception exception) {
	    String msg = "Exception while invoking valueOfMethod [" + valueOfMethod.getName()
		    + "] of Enum class [" + enumClass.getName() + "] with argument of type ["
		    + identifier.getClass().getName() + "], value=[" + identifier + "]";
	    throw new HibernateException(msg, exception);
	}
    }

    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
	    throws HibernateException, SQLException {
	if (value == null) {
	    st.setNull(index, sqlTypes[0]);
	} else {
	    try {
		Object identifier = identifierMethod.invoke(value, NULL_OBJECT_VARARG);
		type.set(st, identifier, index, session);
	    } catch (Exception exception) {
		String msg = "Exception while invoking identifierMethod [" + identifierMethod.getName()
			+ "] of Enum class [" + enumClass.getName()
			+ "] with argument of type [" + value.getClass().getName() + "], value=[" + value + "]";
		throw new HibernateException(msg, exception);
	    }
	}
    }

    public String objectToSQLString(Object value) {
	return SINGLE_QUOTE + ((Enum) value).name() + SINGLE_QUOTE;
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException {
	return original;
    }

    public Class returnedClass() {
	return enumClass;
    }

    @SuppressWarnings("unchecked")
    public void setParameterValues(Properties parameters) {
	String enumClassName = parameters.getProperty("enumClass");
	try {
	    enumClass = Class.forName(enumClassName).asSubclass(Enum.class);
	} catch (ClassNotFoundException exception) {
	    throw new HibernateException("Enum class not found", exception);
	}

	String identifierMethodName = parameters.getProperty("identifierMethod", DEFAULT_IDENTIFIER_METHOD_NAME);
	try {
	    identifierMethod = enumClass.getMethod(identifierMethodName, NULL_CLASS_VARARG);
	} catch (Exception exception) {
	    throw new HibernateException("Failed to obtain identifier method", exception);
	}
	Class<?> identifierType = identifierMethod.getReturnType();

	String valueOfMethodName = parameters.getProperty("valueOfMethod", DEFAULT_VALUE_OF_METHOD_NAME);
	try {
	    valueOfMethod = enumClass.getMethod(valueOfMethodName, identifierType);
	} catch (Exception exception) {
	    throw new HibernateException("Failed to obtain valueOf method", exception);
	}

	//TODO: We really shouldn't be instantiating this, but I don't know how to get the SessionImplementor here
	BasicTypeRegistry registry = new BasicTypeRegistry();

	type = (SingleColumnType<Object>) registry.getRegisteredType(identifierType.getName());
	if (type == null) {
	    throw new HibernateException("Unsupported identifier type " + identifierType.getName());
	}
	sqlTypes = new int[]{type.sqlType()};
    }

    public int[] sqlTypes() {
	return sqlTypes;
    }

    public String toXMLString(Object value) {
	return ((Enum<?>) value).name();
    }
}
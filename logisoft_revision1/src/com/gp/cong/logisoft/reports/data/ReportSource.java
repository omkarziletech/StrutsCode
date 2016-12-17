package com.gp.cong.logisoft.reports.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRAbstractBeanDataSource;

import org.apache.log4j.Logger;

public class ReportSource extends JRAbstractBeanDataSource {

    private static final Logger log = Logger.getLogger(ReportSource.class);
    private ReportDataSource dataSource;
    protected int index = 0;
    protected Object bean;
    private static Map fieldNameMap = new HashMap();

    public ReportSource(ReportDataSource dataSource) {
	super(true);
	this.dataSource = dataSource;
	index = 0;
    }

    public boolean next() throws JRException {
	bean = dataSource.getObject(index++);
	return (bean != null);
    }

    public void moveFirst() throws JRException {
	index = 0;
	bean = dataSource.getObject(index);
    }

    public Object getFieldValue(JRField field) throws JRException {
	String nameField = getFieldName(field.getName());
	try {
	    return PropertyUtils.getProperty(bean, nameField);
	} catch (Exception e) {
	    log.info("getFieldValue failed on " + new Date(),e);
	}
	return null;
    }

    /**
     * Replace the character "_" by a ".".
     *
     * @param fieldName the name of the field
     * @return the value in the cache or make
     *         the replacement and return this value
     */
    private String getFieldName(String fieldName) {
	String filteredFieldName = (String) fieldNameMap.get(fieldName);
	if (filteredFieldName == null) {
	    filteredFieldName = fieldName.replace('_', '.');
	    fieldNameMap.put(fieldName, filteredFieldName);
	}
	return filteredFieldName;
    }
}
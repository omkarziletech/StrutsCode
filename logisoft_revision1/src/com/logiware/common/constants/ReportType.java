package com.logiware.common.constants;

/**
 *
 * @author Lakshmi Narayanan
 */
public enum ReportType {

    CSV("CSV"), XLS("XLS"), PDF("PDF");
    private String value;

    private ReportType(String reportType) {
	this.value = reportType;
    }

    public String getValue() {
	return value;
    }

    public static ReportType fromString(String value) {
	if (value != null) {
	    for (ReportType reportType : values()) {
		if (reportType.value.equals(value)) {
		    return reportType;
		}
	    }
	}
	return getDefault();
    }

    @Override
    public String toString() {
	return value;
    }

    public static ReportType getDefault() {
	return CSV;
    }
}

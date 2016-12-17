package com.logiware.common.constants;

/**
 *
 * @author Lakshmi Narayanan
 */
public enum Company {

    ECU_LINE("ECU LINE"), MAERSK_LINE("MAERSK LINE");
    private String value;

    private Company(String company) {
	this.value = company;
    }

    public String getCompany() {
	return value;
    }

    public static Company fromString(String value) {
	if (value != null) {
	    for (Company company : values()) {
		if (company.value.equals(value)) {
		    return company;
		}
	    }
	}
	return getDefault();
    }

    @Override
    public String toString() {
	return value;
    }

    public static Company getDefault() {
	return ECU_LINE;
    }
}

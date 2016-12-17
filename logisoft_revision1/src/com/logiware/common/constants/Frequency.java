package com.logiware.common.constants;

/**
 *
 * @author Lakshmi Narayanan
 */
public enum Frequency {

    MINUTELY("MINUTELY"), HOURLY("HOURLY"), DAILY("DAILY"), WEEKLY("WEEKLY"), MONTHLY("MONTHLY"), TWICE_A_MONTH("TWICE A MONTH");
    private String value;

    public String getValue() {
	return value;
    }

    private Frequency(String value) {
	this.value = value;
    }

    public static Frequency fromString(String value) {
	if (value != null) {
	    for (Frequency frequency : values()) {
		if (frequency.value.equals(value)) {
		    return frequency;
		}
	    }
	}
	return getDefault();
    }

    @Override
    public String toString() {
	return value;
    }

    public static Frequency getDefault() {
	return DAILY;
    }
}

package com.logiware.common.constants;

/**
 *
 * @author Lakshmi Narayanan
 */
public enum ScheduleFrequency {

    DAILY("DAILY"), WEEKLY("WEEKLY"), MONTHLY("MONTHLY"), TWICE_A_MONTH("TWICE A MONTH");
    private String value;

    public String getValue() {
	return value;
    }

    private ScheduleFrequency(String value) {
	this.value = value;
    }

    public static ScheduleFrequency fromString(String value) {
	if (value != null) {
	    for (ScheduleFrequency frequency : values()) {
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

    public static ScheduleFrequency getDefault() {
	return DAILY;
    }
}

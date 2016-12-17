package com.logiware.tags;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.edi.gtnexus.HelperClass;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Balaji.E
 */
public class String {

    private static java.lang.String splitByLength(java.lang.String string, java.lang.Integer length, java.lang.String replace) {
	List<java.lang.String> strings = Arrays.asList(string.replace("<br>", "").trim().split("(?<=\\G.{" + length + "})"));
	StringBuilder builder = new StringBuilder();
	boolean isFirst = true;
	for (java.lang.String str : strings) {
	    builder.append(isFirst ? "" : replace).append(str);
	    isFirst = false;
	}
	return builder.toString();
    }

    public static java.lang.String split(java.lang.String string, java.lang.Integer length, java.lang.String replace) {

	List<java.lang.String> strings = Arrays.asList(string.split("\\ "));
	StringBuilder builder = new StringBuilder();
	int count = 0;
	boolean isFirst = true;
	for (java.lang.String str : strings) {
	    if (str.length() > length) {
		builder.append(isFirst ? "" : replace).append(splitByLength(str, length, replace));
		count = 0;
	    } else {
		count += str.length() + 1;
		if (count >= 30) {
		    count = 0;
		    if (isFirst) {
			builder.append(str).append(replace);
		    } else {
			builder.append(replace).append(str);
		    }
		} else {
		    builder.append(isFirst ? "" : " ").append(str);
		    isFirst = false;
		}
	    }
	}
	return builder.toString();
    }

    public static java.lang.Boolean in(java.lang.String str, java.lang.String strs) {
	return null != strs && null != str && Arrays.asList(strs.split(",")).contains(str);
    }

    public static java.lang.String splitter(java.lang.String string, java.lang.Integer length, java.lang.String replace) throws Exception {
if(!string.contains("<br/>")){
	List<java.lang.String> strings = new HelperClass().splitString(string.replace("<br/>", "").trim(), length);
	StringBuilder builder = new StringBuilder();
	boolean isFirst = true;
	for (java.lang.String str : strings) {
	    builder.append(isFirst ? "" : replace).append(str);
	    isFirst = false;
	}
	return builder.toString();
    }
    return string;
    }

    public static java.lang.Integer lastIndexOf(java.lang.String string, java.lang.String substring) {
	return string.lastIndexOf(substring);
    }

    public static java.lang.String lastSubString(java.lang.String string, java.lang.String search) {
	return CommonUtils.isNotEmpty(string) ? StringUtils.substring(string, StringUtils.lastIndexOf(string, search) + 1) : "";
    }

    public static java.lang.String blankSpaceWord(java.lang.String string, java.lang.Integer length, java.lang.String replace) throws Exception {
	List<java.lang.String> strings = new HelperClass().splitString(string.replace(" ", " ").trim(), length);
	StringBuilder builder = new StringBuilder();
	boolean isFirst = true;
	for (java.lang.String str : strings) {
	    builder.append(isFirst ? "" : replace).append(str);
	    isFirst = false;
	}
	return builder.toString();
    }

    public static java.lang.String abbreviate(java.lang.String string, java.lang.Integer length) {
	return StringUtils.abbreviate(string, length);
    }

    public static java.lang.Boolean contains(java.lang.String[] strs, java.lang.String str) {
	return null != str && null != strs && Arrays.asList(strs).contains(str);
    }

    public static java.lang.String removeEnd(java.lang.String string, java.lang.String remove) {
	return StringUtils.removeEnd(string, remove);
    }
}

package com.logiware.common.utils;

/**
 *
 * @author Lucky
 */
public class RegexUtil {

    public static final String EMAIL_REGEX = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    public static final String FAX_REGEX = "^(?:\\+?1[-. ]?)?\\(?([0-9]{3})\\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})$";

    public static boolean isEmail(String email) {
        return null != email && email.matches(EMAIL_REGEX);
    }

    public static boolean isFax(String fax) {
        return null != fax && fax.matches(FAX_REGEX);
    }
}

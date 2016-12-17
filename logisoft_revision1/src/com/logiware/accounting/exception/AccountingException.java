package com.logiware.accounting.exception;

/**
 *
 * @author Lakshmi Naryanan
 */
public class AccountingException extends Exception {

    public AccountingException(String message) {
	super(message);
    }

    public AccountingException(Throwable cause) {
	super(cause);
    }

    public AccountingException(String message, Throwable cause) {
	super(message, cause);
    }
}

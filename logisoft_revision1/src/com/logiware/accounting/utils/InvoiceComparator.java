package com.logiware.accounting.utils;

import com.logiware.accounting.model.InvoiceModel;

/**
 *
 * @author Lakshmi Narayanan
 */
public class InvoiceComparator implements java.util.Comparator {

    @Override
    public int compare(Object obj1, Object obj2) {
        if (obj1 instanceof InvoiceModel && obj2 instanceof InvoiceModel) {
            return ((InvoiceModel) obj1).getInvoiceOrBl().compareTo(((InvoiceModel) obj2).getInvoiceOrBl());
        }
        return 0;
    }
}

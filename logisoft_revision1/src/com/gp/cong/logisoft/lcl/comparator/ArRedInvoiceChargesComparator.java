/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.comparator;

import com.logiware.hibernate.domain.ArRedInvoiceCharges;
import java.util.Comparator;

/**
 *
 * @author Administrator
 */
public class ArRedInvoiceChargesComparator implements Comparator<ArRedInvoiceCharges> {

    public int compare(ArRedInvoiceCharges rob1, ArRedInvoiceCharges rob2) {
        if (rob1 == null || rob1.getLclDrNumber() == null) {
            return -1;
        }
        if (rob2 == null || rob2.getLclDrNumber() == null) {
            return 1;
        }
        return rob1.getLclDrNumber().compareTo(rob2.getLclDrNumber());
    }
}

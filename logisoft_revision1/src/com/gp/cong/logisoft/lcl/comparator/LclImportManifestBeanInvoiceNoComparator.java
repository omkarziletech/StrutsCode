/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.comparator;

import com.gp.cong.logisoft.beans.ImportsManifestBean;
import java.util.Comparator;

/**
 *
 * @author vijaygupta.m
 */
public class LclImportManifestBeanInvoiceNoComparator implements Comparator<ImportsManifestBean> {

    @Override
    public int compare(ImportsManifestBean rob1, ImportsManifestBean rob2) {
        if (null == rob1 || null == rob2 || null == rob1.getInvoiceNo() || null == rob2.getInvoiceNo() || "".equals(rob1.getInvoiceNo().replaceAll("/", "").trim()) || "".equals(rob2.getInvoiceNo().replaceAll("/", "").trim())) {
            return 0;
        } else {
            return rob1.getInvoiceNo().replaceAll("/", "").trim().compareTo((rob2.getInvoiceNo().replaceAll("/", "").trim()));
        }
    }
}

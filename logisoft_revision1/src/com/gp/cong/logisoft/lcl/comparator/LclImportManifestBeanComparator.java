/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.comparator;

import com.gp.cong.logisoft.beans.ImportsManifestBean;
import java.util.Comparator;

/**
 *
 * @author Administrator
 */
public class LclImportManifestBeanComparator implements Comparator<ImportsManifestBean> {

    public int compare(ImportsManifestBean rob1, ImportsManifestBean rob2) {
        if (rob1 == null || rob1.getFileNo() == null) {
            return -1;
        }
        if (rob2 == null || rob2.getFileNo() == null) {
            return 1;
        }
        return rob1.getFileNo().compareTo(rob2.getFileNo());
    }
}

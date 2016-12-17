/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.comparator;

import com.gp.cong.lcl.model.ExportVoyageSearchModel;
import java.util.Comparator;

/**
 *
 * @author Administrator
 */
public class LclVoyageComparator implements Comparator<ExportVoyageSearchModel> {

    @Override
    public int compare(ExportVoyageSearchModel voy1, ExportVoyageSearchModel voy2) {
        if (voy1.getServiceType().equalsIgnoreCase("C") || voy2.getServiceType().equalsIgnoreCase("C")) {
            return voy2.getServiceType().compareTo(voy1.getServiceType());// cfcl always last in the list
        } else {
            return 0;
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.comparator;

import com.gp.cong.logisoft.beans.RoutingOriginBean;
import java.util.Comparator;

/**
 *
 * @author Administrator
 */
public class LclRoutingOriginComparator implements Comparator<RoutingOriginBean> {

    public int compare(RoutingOriginBean rob1, RoutingOriginBean rob2) {
        if (rob1 == null || rob1.getMiles() == null) {
            return -1;
        }
        if (rob2 == null || rob2.getMiles() == null) {
            return 1;
        }
        return rob1.getMiles().compareTo(rob2.getMiles());
    }
}

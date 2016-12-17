/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.beans;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Logiware
 */
public class ImportBookingUnitsBean extends ArrayList<ImportUnitsBean> implements Comparator<ImportUnitsBean> {

   

    public ImportBookingUnitsBean() {
    }

    public ImportBookingUnitsBean(List l) throws Exception {
        for (Object obj : l) {
            Object[] row = (Object[]) obj;
           ImportUnitsBean iub = new ImportUnitsBean(row);
           if(contains(iub)){
               get(iub).add(new ImportBookingBean(row));
           }else {
               iub.add(new ImportBookingBean(row));
               add(iub);
           }
        }
    }

    private ImportUnitsBean get(ImportUnitsBean unit) {
        for(ImportUnitsBean iub : this) {
            if(iub.equals(unit)) {
                return iub;
            }
        }
        return null;
    }

    public int compare(ImportUnitsBean o1, ImportUnitsBean o2) {
        if (null != o1.getUnitId() && null != o2.getUnitId()) {
	    return o1.compareTo(o2);
	}
	return 0;
    }
   
}

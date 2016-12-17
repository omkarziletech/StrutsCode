package com.gp.cvst.logisoft.beans;

import com.gp.cvst.logisoft.domain.FclBlCharges;
import com.gp.cvst.logisoft.domain.FclBlContainer;
import com.gp.cvst.logisoft.domain.FclBlCorrections;
import com.gp.cvst.logisoft.domain.FclBlCostCodes;
import java.io.Serializable;

public class Comparator implements java.util.Comparator, Serializable {

    public int compare(Object obj1, Object obj2) {
        if (obj1 instanceof FclBlCharges && obj2 instanceof FclBlCharges) {
            String name1 = ((FclBlCharges) obj1).getChargeCode();
            String name2 = ((FclBlCharges) obj2).getChargeCode();
            return name1.compareTo(name2);
        }
        if (obj1 instanceof FclBlCostCodes && obj2 instanceof FclBlCostCodes) {
            String name1 = ((FclBlCostCodes) obj1).getCostCode();
            String name2 = ((FclBlCostCodes) obj2).getCostCode();
            return name1.compareTo(name2);
        }
        if (obj1 instanceof FclBlContainer && obj2 instanceof FclBlContainer) {
            Integer name1 = ((FclBlContainer) obj1).getTrailerNoId();
            Integer name2 = ((FclBlContainer) obj2).getTrailerNoId();
            return name1.compareTo(name2);
        }
        if (obj1 instanceof FclBlCorrections && obj2 instanceof FclBlCorrections) {
            String name1 = ((FclBlCorrections) obj1).getChargeCode();
            String name2 = ((FclBlCorrections) obj2).getChargeCode();
            return name1.compareTo(name2);
        }
        return 0;
    }
}

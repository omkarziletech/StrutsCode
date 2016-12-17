
package com.gp.cvst.logisoft.beans;
import com.gp.cvst.logisoft.domain.Charges;
import com.gp.cvst.logisoft.domain.MultiQuoteCharges;
import java.util.Comparator;
/**
 *
 * @author NambuRajasekar.M
 */
public class MultiQuoteComparator implements Comparator {

    @Override
    public int compare(Object obj1, Object obj2) {
        if (obj1 instanceof MultiQuoteCharges && obj2 instanceof MultiQuoteCharges) {
            MultiQuoteCharges m1 = (MultiQuoteCharges) obj1;
            MultiQuoteCharges m2 = (MultiQuoteCharges) obj2;
            return m1.getUnitNo().compareTo(m2.getUnitNo());
            
        } else {
            Charges c1 = (Charges) obj1;
            Charges c2 = (Charges) obj2;
            return c1.getUnitType().compareTo(c2.getUnitType());
        }
    }
}

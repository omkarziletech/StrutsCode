/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.rates;

/**
 *
 * @author Administrator
 */
public class UnitCostImpl extends UnitCost {

    public UnitCostImpl(Object[] objects) {
        super(objects);
    }

    public UnitCostImpl(String currentUnitCodeDesc) {
        super(currentUnitCodeDesc);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UnitCostImpl other = (UnitCostImpl) obj;
        if ((this.unitCode == null) ? (other.unitCode != null) : !this.unitCode.equals(other.unitCode)) {
            return false;
        }
        if (this.currentActiveAmount != other.currentActiveAmount) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.unitCode != null ? this.unitCode.hashCode() : 0);
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.currentActiveAmount) ^ (Double.doubleToLongBits(this.currentActiveAmount) >>> 32));
        return hash;
    }
    

}

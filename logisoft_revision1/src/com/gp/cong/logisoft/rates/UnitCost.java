/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.rates;

import java.math.BigDecimal;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Administrator
 */
public class UnitCost implements Cloneable, Rates {

    protected String unitId;
    protected String unitCode;
    protected String currentUnitCodeDesc;
    protected double total;
    //Total amount for current
    protected double currentTotal;   //2
    protected double currentActiveAmount; //1
    protected double currentCtcAmount;
    protected double currentFtfAmount;
    protected double currentMinimumAmount;
    //Per Container
    protected double currentRetailAmount;  //
    protected double currentMarkup;  //3
    protected String effectiveDate;
    protected double activeAmount;//For Collapse Mode
    protected double activeTotalAmount;//For Collapse Mode
    protected double activeMarkupAmount;//For Collapse Mode
    public static Set<String> costTypes = new TreeSet<String>();
    
    public static enum Type{
        A,B,C,D;
    }

    public UnitCost(String currentUnitCodeDesc) {
        this.currentUnitCodeDesc = currentUnitCodeDesc;
    }

    public UnitCost(Object[] objects) {
        if (isNotNull(objects[UNIT_CODE])) {
            unitCode = objects[UNIT_CODE].toString();
        }
        if (isNotNull(objects[UNIT_ID])) {
            unitId = objects[UNIT_ID].toString();
        }
        if (isNotNull(objects[UNIT_CODE_DESC])) {
            currentUnitCodeDesc = objects[UNIT_CODE_DESC].toString();
            costTypes.add(currentUnitCodeDesc);
        }
        if (isNotNull(objects[ACTIVE_AMOUNT])) {
            currentActiveAmount = ((BigDecimal) objects[ACTIVE_AMOUNT]).doubleValue();            
            currentTotal += currentActiveAmount;
        }
        if (isNotNull(objects[CTC_AMOUNT])) {
            currentCtcAmount = ((BigDecimal) objects[CTC_AMOUNT]).doubleValue();
            currentTotal += currentCtcAmount;
        }
        if (isNotNull(objects[FTF_AMOUNT])) {
            currentFtfAmount = ((BigDecimal) objects[FTF_AMOUNT]).doubleValue();
            currentTotal += currentFtfAmount;
        }
        if (isNotNull(objects[MINIMUM_AMOUNT])) {
            currentMinimumAmount = ((BigDecimal) objects[MINIMUM_AMOUNT]).doubleValue();
            currentTotal += currentMinimumAmount;

        }
        if (isNotNull(objects[RETAIL_AMOUNT])) {
            currentRetailAmount = ((BigDecimal) objects[RETAIL_AMOUNT]).doubleValue();
            currentTotal += currentRetailAmount;
        }
        if (isNotNull(objects[MARKUP])) {
            currentMarkup = Double.parseDouble(objects[MARKUP].toString());
        }
        total = currentTotal + currentMarkup;
        activeAmount = total;
        activeTotalAmount = total;
    }

    public static Set<String> getCostTypes() {
        return costTypes;
    }

    public static void setCostTypes(Set<String> costTypes) {
        UnitCost.costTypes = costTypes;
    }

    /**
     *   Reset the costypes for each request.
     *   It will erase the previous cost type from cost Type container
     */
    public static void resetCostTypes() {
        costTypes = new TreeSet<String>();
    }

    public double getCurrentActiveAmount() {
        return currentActiveAmount;
    }

    public void setCurrentActiveAmount(double currentActiveAmount) {
        this.currentActiveAmount = currentActiveAmount;
    }

    public double getCurrentCtcAmount() {
        return currentCtcAmount;
    }

    public void setCurrentCtcAmount(double currentCtcAmount) {
        this.currentCtcAmount = currentCtcAmount;
    }

    public double getCurrentFtfAmount() {
        return currentFtfAmount;
    }

    public void setCurrentFtfAmount(double currentFtfAmount) {
        this.currentFtfAmount = currentFtfAmount;
    }

    public double getCurrentMarkup() {
        return currentMarkup;
    }

    public void setCurrentMarkup(double currentMarkup) {
        this.currentMarkup = currentMarkup;
    }

    public double getCurrentMinimumAmount() {
        return currentMinimumAmount;
    }

    public void setCurrentMinimumAmount(double currentMinimumAmount) {
        this.currentMinimumAmount = currentMinimumAmount;
    }

    public double getCurrentRetailAmount() {
        return currentRetailAmount;
    }

    public void setCurrentRetailAmount(double currentRetailAmount) {
        this.currentRetailAmount = currentRetailAmount;
    }

    public double getCurrentTotal() {
        return currentTotal;
    }

    public void setCurrentTotal(double currentTotal) {
        this.currentTotal = currentTotal;
    }

    public String getCurrentUnitCode() {
        return unitCode;
    }

    public void setCurrentUnitCode(String currentUnitCode) {
        this.unitCode = currentUnitCode;
    }

    public String getCurrentUnitCodeDesc() {
        return currentUnitCodeDesc;
    }

    public void setCurrentUnitCodeDesc(String currentUnitCodeDesc) {
        this.currentUnitCodeDesc = currentUnitCodeDesc;
    }

    public boolean isNotNull(Object object) {
        return object != null;
    }

    public double getTotal() {
        return total;
    }
    public double setTotal(Double total) {
        return this.total = total;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }
  
  @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UnitCost other = (UnitCost) obj;
        if ((this.currentUnitCodeDesc == null) ? (other.currentUnitCodeDesc != null) : !this.currentUnitCodeDesc.equals(other.currentUnitCodeDesc)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + (this.currentUnitCodeDesc != null ? this.currentUnitCodeDesc.hashCode() : 0);
        return hash;
    }
    

    public double getActiveAmount() {
        return activeAmount;
    }

    public void setActiveAmount(double activeAmount) {
        this.activeAmount = activeAmount;
    }

    public double getActiveTotalAmount() {
        return activeTotalAmount;
    }

    public void setActiveTotalAmount(double activeTotalAmount) {
        this.activeTotalAmount = activeTotalAmount;
    }

    public double getActiveMarkupAmount() {
        return activeMarkupAmount;
    }

    public void setActiveMarkupAmount(double activeMarkupAmount) {
        this.activeMarkupAmount = activeMarkupAmount;
    }
}

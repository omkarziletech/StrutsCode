/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.rates.charge;

import com.gp.cong.logisoft.rates.Rates;
import com.gp.cong.logisoft.rates.UnitCost;
import java.util.ArrayList;
import java.util.Set;

/**
 *
 * @author Administrator
 */
public class ChargeCode extends ArrayList<UnitCost> implements Cloneable, Rates {

    public static final String FLAT_RATE_PER_CONTAINER_SIZE = "Flat Rate Per Container Size";
    public static final String FLAT_RATE_PER_CONTAINER = "Flat Rate Per Container";
    public static final String PER_BL = "Per BL Charges";
    //charges
    public static final String HAZARDOUS = "HAZFEE";
    public static final String NASSAU_LANDING = "NASSAU LANDING";
    public static final String OCEAN_FREIGHT = "OCEAN FREIGHT";
    public static final String OCEAN_FREIGHT_IMPORT = "OCEAN FREIGHT IMPORT";
    public static final String OCEAN_FREIGHT_IMPORT_Code = "OFIMP";
    public static final String GENERAL_RATE_INCREASE = "GENERAL RATE INCREASE";
    public static final String RAIL = "RAIL";
    public static final String DRAY = "DRAY";
    public static final String BUNKER_SURCHARGE = "BUNKER SURCHARGE";
    public static final String INTERMODAL_RAMP = "INTERMODAL RAMP";
    public static final String INTERMODAL_FS = "INTERMODAL F/S";
   
    public static final String AMS_FEE = "AMS FEE";
    public static final String AMS_FEE_Code = "AMSFEE";
    public static final String DOC_TURNOVER = "DOC TURNOVER";
    public static final String DOC_TURNOVER_Code = "DOCTURN";
   
    private static Set<String> ingnoreChageCodes;
    private String chargeCodeDesc;
    private String chargeCode;
    private String costCode;
    private String service;
    private String costCodeDesc;    
    //private String chargeType; //ORGINAL CHARGE or INLAND CHARGE
    private String costType; // "Flat Rate Per Container Size" or "Flat Rate Per Container" or "Per Bl" etc.
    private double perContainer;
    private String chargCode;

    public ChargeCode() {
    }

    public ChargeCode(String chargeCodeDesc) {
        this.chargeCodeDesc = chargeCodeDesc;
    }

    public ChargeCode(Object[] objects) {
        if (isNotNull(objects[COST_TYPE])) {
            costCode = objects[COST_TYPE].toString();
        }
        if (isNotNull(objects[COST_TYPE_DESC])) {
            costCodeDesc = objects[COST_TYPE_DESC].toString();
        }
        if (isNotNull(objects[COST_CODE])) {
            chargeCode = objects[COST_CODE].toString();
        }
        if (isNotNull(objects[COST_CODE_DESC])) {
            chargeCodeDesc = objects[COST_CODE_DESC].toString();
        }
        if (isNotNull(objects[COST_TYPE])) {
            costType = objects[COST_TYPE].toString();
        }
        if(isNotNull(objects[CHARGE_CODE])){
            chargCode = objects[CHARGE_CODE].toString();
        }
        UnitCost unitCost = null;
        
        unitCost = new UnitCost(objects);
        addUnitCost(unitCost);
    }

    public boolean addUnitCost(UnitCost unitCost) {
        perContainer += unitCost.getCurrentRetailAmount();
        return add(unitCost);
    }

    public String getChargeCodeDesc() {
        return chargeCodeDesc;
    }

    public void setChargeCodeDesc(String chargeCode) {
        this.chargeCodeDesc = chargeCode;
    }

    public String getCostCode() {
        return costCode;
    }

    public void setCostCode(String costCode) {
        this.costCode = costCode;
    }

    public String getCostCodeDesc() {
        return costCodeDesc;
    }

    public void setCostCodeDesc(String costCodeDesc) {
        this.costCodeDesc = costCodeDesc;
    }

    public static Set<String> getIngnoreChageCodes() {
        return ingnoreChageCodes;
    }

    public static void setIngnoreChageCodes(Set<String> ingnoreChageCodes) {
        ChargeCode.ingnoreChageCodes = ingnoreChageCodes;
    }

    public double getPerContainer() {
        return perContainer;
    }

    public void setPerContainer(double perContainer) {
        this.perContainer = perContainer;
    }

    public boolean isNotNull(Object object) {
        return object != null;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public boolean canBeAdded() {
        boolean canBeAdded = false;
        if (chargeCodeDesc != null) {
            chargeCodeDesc = chargeCodeDesc.trim();
            //if (CommonConstants.chargeCodeList.contains(chargeCodeDesc)) {
                 if (chargeCode.equals(HAZARDOUS)
                || chargeCodeDesc.equals(OCEAN_FREIGHT)
                || chargeCodeDesc.equals(BUNKER_SURCHARGE)
                || chargeCodeDesc.equals(INTERMODAL_RAMP)
                || chargeCodeDesc.equals(NASSAU_LANDING)
                || chargeCodeDesc.equals(OCEAN_FREIGHT_IMPORT)) {
                canBeAdded = true;
            }
        }
        return canBeAdded;
    }
     public boolean canBeAddedForImports() {
        boolean canBeAdded = false;
        if (chargCode != null) {
            chargCode = chargCode.trim();
            //if (CommonConstants.chargeCodeList.contains(chargeCodeDesc)) {
                 if (chargCode.equals(OCEAN_FREIGHT_IMPORT_Code)
                || chargCode.equals(AMS_FEE_Code)
                || chargCode.equals(DOC_TURNOVER_Code)) {
                canBeAdded = true;
            }
        }
        return canBeAdded;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ChargeCode other = (ChargeCode) obj;
        if ((this.chargeCodeDesc == null) ? (other.chargeCodeDesc != null) : !this.chargeCodeDesc.equals(other.chargeCodeDesc)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.chargeCodeDesc != null ? this.chargeCodeDesc.hashCode() : 0);
        return hash;
    }

    public boolean isPercontainer() {
        return FLAT_RATE_PER_CONTAINER.equalsIgnoreCase(costType);
    }

    public boolean isPercontainerSize() {
        return FLAT_RATE_PER_CONTAINER_SIZE.equalsIgnoreCase(costType);
    }

    public boolean isPerBL() {
        return PER_BL.equalsIgnoreCase(costType);
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getCostType() {
        return costType;
    }

    public void setCostType(String costType) {
        this.costType = costType;
    }

    public String getChargCode() {
        return chargCode;
    }

    public void setChargCode(String chargCode) {
        this.chargCode = chargCode;
    }
    
    
}

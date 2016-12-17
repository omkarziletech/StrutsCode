/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.rates;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.rates.charge.ChargeCode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.beanutils.PropertyUtils;

/**
 *
 * @author Administrator
 */
public class SSLine extends LinkedList<ChargeCode> implements Rates, Comparable<SSLine> {

    private String ssLineNumber;
    private String ssLineName;
    private String service;
    protected ChargeCode previousChargeCode;
    private String transitTime;
    private String portOfExit = "";
    private String localDrayage;
    private ChargeCode collapsedChargeCode = null;
    private List<ChargeCode> expandedChargeCode = null;
    private String remarks;

    public SSLine() {
    }

    public SSLine(Object[] objects) {
        if (objects[SS_LINE_NUMBER] != null) {
            ssLineNumber = objects[SS_LINE_NUMBER].toString();
            ssLineName = objects[SS_LINE_NAME].toString();
        }
        if (objects[TRANSIT_TIME] != null) {
            transitTime = objects[TRANSIT_TIME].toString();
        }
        if (objects[REMARKS] != null) {
            remarks = objects[REMARKS].toString();
        }
        if (objects[POE] != null) {
            portOfExit = objects[POE].toString();
        }
        if (objects[LOCAL_DRAYAGE] != null) {
            localDrayage = objects[LOCAL_DRAYAGE].toString();
        }
        addChargeCode(objects);
    }

    public String getSsLineNumber() {
        return ssLineNumber;
    }

    public void setSsLineNumber(String ssLineNumber) {
        this.ssLineNumber = ssLineNumber;
    }

    public ChargeCode addChargeCode(Object[] row) {
        ChargeCode currentChargeCode = new ChargeCode(row);
        if (currentChargeCode != null && currentChargeCode.equals(previousChargeCode)) {
            previousChargeCode.addUnitCost(new UnitCost(row));
        } else {
            add(currentChargeCode);
            previousChargeCode = currentChargeCode;
            //addOFRChargeCode(currentChargeCode,row);
        }
        return currentChargeCode;
    }

    public String getSsLineName() {
        return ssLineName;
    }

    public String getPortOfExit() {
        return portOfExit;
    }

    public void setPortOfExit(String portOfExit) {
        this.portOfExit = portOfExit;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTransitTime() {
        return transitTime;
    }

    public void setTransitTime(String transitTime) {
        this.transitTime = transitTime;
    }

    public void setSsLineName(String ssLineName) {
        this.ssLineName = ssLineName;
    }

    /**
     * Get the collapsed charge code by added the fields of all element of
     * charge code list which can be added.
     *
     * @return ChargeCode
     */
    public ChargeCode getCollapsedChargeCode(String fileType) throws Exception {
        if (collapsedChargeCode == null) {
            if (!"I".equalsIgnoreCase(fileType)) {
                ChargeCode chargeCode = new ChargeCode(ChargeCode.OCEAN_FREIGHT);
                for (ChargeCode code : this) {
                    //if (ChargeCode.OCEAN_FREIGHT.equalsIgnoreCase(code.getChargeCodeDesc())) {
                    for (UnitCost cost : code) {
                        //if unit cost can be added to collapsed list
                        int index = chargeCode.indexOf(cost);
                        if (index > -1) {
                            UnitCost unitCost = chargeCode.get(index);
                            Double total = cost.getActiveAmount() + unitCost.getActiveAmount();
                            unitCost.setActiveAmount(total);
                        } else {
                            UnitCost newCost = new UnitCost("DEFAULT");
                            PropertyUtils.copyProperties(newCost, cost);
                            chargeCode.add(cost);
                        }
                    }
                    //}
                }
                setCollapsedChargeCode(chargeCode);
            } else {
                ChargeCode chargeCode = new ChargeCode(ChargeCode.OCEAN_FREIGHT_IMPORT);
                 boolean isImportFound = false;
                for (ChargeCode code : this) {
                    if (code.getChargCode().equals(ChargeCode.OCEAN_FREIGHT_IMPORT_Code)) {
                        isImportFound = true;
                    }
                }
                if (!isImportFound) {
                    chargeCode = new ChargeCode();
                    return chargeCode;
                }
                for (ChargeCode code : this) {
                for (UnitCost cost : code) {
                        //if unit cost can be added to collapsed list
                        int index = chargeCode.indexOf(cost);
                        if (index > -1) {
                            UnitCost unitCost = chargeCode.get(index);
                            Double total = cost.getActiveAmount() + unitCost.getActiveAmount();
                            unitCost.setActiveAmount(total);
                        } else {
                            UnitCost newCost = new UnitCost("DEFAULT");
                            PropertyUtils.copyProperties(newCost, cost);
                            chargeCode.add(cost);
                        }
                    }
                    //}
                }
                setCollapsedChargeCode(chargeCode);
            }
        }
        return collapsedChargeCode;
    }
//    public ChargeCode getCollapsedChargeCode(String fileType) throws Exception {
//        if (collapsedChargeCode == null) {
//            ChargeCode chargeCode = null;
//            if ("I".equalsIgnoreCase(fileType)) {
//                chargeCode = new ChargeCode(ChargeCode.OCEAN_FREIGHT_IMPORT);
//                boolean isImportFound = false;
//                for (ChargeCode code : this) {
//                    if (code.getChargeCodeDesc().equals(ChargeCode.OCEAN_FREIGHT_IMPORT)) {
//                        isImportFound = true;
//                    }
//                }
//                if (!isImportFound) {
//                    chargeCode = new ChargeCode();
//                    return chargeCode;
//                }
//                for (ChargeCode code : this) {
//                    if (ChargeCode.OCEAN_FREIGHT_IMPORT.equals(code.getChargeCodeDesc())) {
//                        for (UnitCost cost : code) {
//                            //if unit cost can be added to collapsed list
//                            int index = chargeCode.indexOf(cost);
//                            if (index > -1) {
//                                UnitCost unitCost = chargeCode.get(index);
//                                Double total = cost.getActiveAmount() + unitCost.getActiveAmount();
//                                unitCost.setActiveAmount(total);
//                            } else {
//                                UnitCost newCost = new UnitCost("DEFAULT");
//                                PropertyUtils.copyProperties(newCost, cost);
//                                chargeCode.add(cost);
//                            }
//                        }
//                    }
//
//                }
//            } else {
//                chargeCode = new ChargeCode(ChargeCode.OCEAN_FREIGHT);
//                for (ChargeCode code : this) {
//                    if (ChargeCode.OCEAN_FREIGHT.equalsIgnoreCase(code.getChargeCodeDesc())) {
//                        for (UnitCost cost : code) {
//                            //if unit cost can be added to collapsed list
//                            int index = chargeCode.indexOf(cost);
//                            if (index > -1) {
//                                UnitCost unitCost = chargeCode.get(index);
//                                Double total = cost.getActiveAmount() + unitCost.getActiveAmount();
//                                unitCost.setActiveAmount(total);
//                            } else {
//                                UnitCost newCost = new UnitCost("DEFAULT");
//                                PropertyUtils.copyProperties(newCost, cost);
//                                chargeCode.add(cost);
//                            }
//                        }
//                    }
//                }
//            }
//            setCollapsedChargeCode(chargeCode);
//        }
//        return collapsedChargeCode;
//    }

    public List<ChargeCode> getExpandedChargeList(String fileType) throws Exception {
        List<ChargeCode> chargeCodeList = new ArrayList<ChargeCode>();
        if (null == expandedChargeCode) {
            ChargeCode ofrChargeCode = null;

            if ("I".equalsIgnoreCase(fileType)) {
                ofrChargeCode = new ChargeCode(ChargeCode.OCEAN_FREIGHT_IMPORT);
                ChargeCode intRampChargeCode = new ChargeCode(ChargeCode.INTERMODAL_RAMP);
                chargeCodeList.add(ofrChargeCode);
                chargeCodeList.add(intRampChargeCode);
                boolean isImportFound = false;
                for (ChargeCode code : this) {
                    if (code.getChargCode().equals(ChargeCode.OCEAN_FREIGHT_IMPORT_Code)) {
                        isImportFound = true;
                    }
                }
                if (!isImportFound) {
                    chargeCodeList = new ArrayList<ChargeCode>();
                    return chargeCodeList;
                }

                for (ChargeCode code : this) {

                    if ((!code.canBeAddedForImports() || ChargeCode.OCEAN_FREIGHT_IMPORT_Code.equals(code.getChargCode())) && !ChargeCode.INTERMODAL_FS.equals(code.getChargeCodeDesc())) {
                        for (UnitCost cost : code) {
                            //if unit cost can be added to collapsed list
                            int index = ofrChargeCode.indexOf(cost);
                            if (index > -1) {
                                UnitCost unitCost = ofrChargeCode.get(index);
                                Double total = cost.getActiveTotalAmount() + unitCost.getActiveTotalAmount();
                                unitCost.setActiveTotalAmount(total);
                            } else {
                                UnitCost newCost = new UnitCost("DEFAULT");
                                PropertyUtils.copyProperties(newCost, cost);
                                ofrChargeCode.add(cost);
                            }
                        }
                    } else if (ChargeCode.INTERMODAL_RAMP.equals(code.getChargeCodeDesc()) || ChargeCode.INTERMODAL_FS.equals(code.getChargeCodeDesc())) {
                        for (UnitCost cost : code) {
                            //if unit cost can be added to collapsed list
                            int index = intRampChargeCode.indexOf(cost);
                            if (index > -1) {
                                UnitCost unitCost = intRampChargeCode.get(index);
                                Double total = cost.getActiveTotalAmount() + unitCost.getActiveTotalAmount();
                                unitCost.setActiveTotalAmount(total);
                            } else {
                                UnitCost newCost = new UnitCost("DEFAULT");
                                PropertyUtils.copyProperties(newCost, cost);
                                intRampChargeCode.add(cost);
                            }
                        }
                    } else {
                        chargeCodeList.add(code);
                    }

                }
            } else {
                ofrChargeCode = new ChargeCode(ChargeCode.OCEAN_FREIGHT);
                ChargeCode intRampChargeCode = new ChargeCode(ChargeCode.INTERMODAL_RAMP);
                chargeCodeList.add(ofrChargeCode);
                chargeCodeList.add(intRampChargeCode);
                for (ChargeCode code : this) {
                    if ((!code.canBeAdded() || ChargeCode.OCEAN_FREIGHT.equals(code.getChargeCodeDesc())) && !ChargeCode.INTERMODAL_FS.equals(code.getChargeCodeDesc())) {
                        for (UnitCost cost : code) {
                            //if unit cost can be added to collapsed list
                            int index = ofrChargeCode.indexOf(cost);
                            if (index > -1) {
                                UnitCost unitCost = ofrChargeCode.get(index);
                                Double total = cost.getActiveTotalAmount() + unitCost.getActiveTotalAmount();
                                unitCost.setActiveTotalAmount(total);
                            } else {
                                UnitCost newCost = new UnitCost("DEFAULT");
                                PropertyUtils.copyProperties(newCost, cost);
                                ofrChargeCode.add(cost);
                            }
                        }
                    } else if (ChargeCode.INTERMODAL_RAMP.equals(code.getChargeCodeDesc()) || ChargeCode.INTERMODAL_FS.equals(code.getChargeCodeDesc())) {
                        for (UnitCost cost : code) {
                            //if unit cost can be added to collapsed list
                            int index = intRampChargeCode.indexOf(cost);
                            if (index > -1) {
                                UnitCost unitCost = intRampChargeCode.get(index);
                                Double total = cost.getActiveTotalAmount() + unitCost.getActiveTotalAmount();
                                unitCost.setActiveTotalAmount(total);
                            } else {
                                UnitCost newCost = new UnitCost("DEFAULT");
                                PropertyUtils.copyProperties(newCost, cost);
                                intRampChargeCode.add(cost);
                            }
                        }
                    } else {
                        chargeCodeList.add(code);
                    }
                    setExpandedChargeCode(chargeCodeList);
                }
            }
      
        }
        return chargeCodeList;
    }

//    public List<ChargeCode> getExpandedChargeList(String fileType) throws Exception {
//        if (null == expandedChargeCode) {
//            ChargeCode ofrChargeCode = null;
//            List<ChargeCode> chargeCodeList = new ArrayList<ChargeCode>();
//            if (fileType != "" && fileType != null && fileType.equalsIgnoreCase("I")) {
//                ofrChargeCode = new ChargeCode(ChargeCode.OCEAN_FREIGHT_IMPORT);
//            } else {
//                ofrChargeCode = new ChargeCode(ChargeCode.OCEAN_FREIGHT);
//            }
//            ChargeCode intRampChargeCode = new ChargeCode(ChargeCode.INTERMODAL_RAMP);
//            chargeCodeList.add(ofrChargeCode);
//            chargeCodeList.add(intRampChargeCode);
//            for (ChargeCode code : this) {
//                if ((!code.canBeAdded() || ChargeCode.OCEAN_FREIGHT.equals(code.getChargeCodeDesc())) && !ChargeCode.INTERMODAL_FS.equals(code.getChargeCodeDesc())) {
//                    for (UnitCost cost : code) {
//                        //if unit cost can be added to collapsed list
//                        int index = ofrChargeCode.indexOf(cost);
//                        if (index > -1) {
//                            UnitCost unitCost = ofrChargeCode.get(index);
//                            Double total = cost.getActiveTotalAmount() + unitCost.getActiveTotalAmount();
//                            unitCost.setActiveTotalAmount(total);
//                        } else {
//                            UnitCost newCost = new UnitCost("DEFAULT");
//                            PropertyUtils.copyProperties(newCost, cost);
//                            ofrChargeCode.add(cost);
//                        }
//                    }
//                }
//                if ((!code.canBeAdded() || ChargeCode.OCEAN_FREIGHT_IMPORT.equals(code.getChargeCodeDesc())) && !ChargeCode.INTERMODAL_FS.equals(code.getChargeCodeDesc())) {
//                    for (UnitCost cost : code) {
//                        //if unit cost can be added to collapsed list
//                        int index = ofrChargeCode.indexOf(cost);
//                        if (index > -1) {
//                            UnitCost unitCost = ofrChargeCode.get(index);
//                            Double total = cost.getActiveTotalAmount() + unitCost.getActiveTotalAmount();
//                            unitCost.setActiveTotalAmount(total);
//                        } else {
//                            UnitCost newCost = new UnitCost("DEFAULT");
//                            PropertyUtils.copyProperties(newCost, cost);
//                            ofrChargeCode.add(cost);
//                        }
//                    }
//                } else if (ChargeCode.INTERMODAL_RAMP.equals(code.getChargeCodeDesc()) || ChargeCode.INTERMODAL_FS.equals(code.getChargeCodeDesc())) {
//                    for (UnitCost cost : code) {
//                        //if unit cost can be added to collapsed list
//                        int index = intRampChargeCode.indexOf(cost);
//                        if (index > -1) {
//                            UnitCost unitCost = intRampChargeCode.get(index);
//                            Double total = cost.getActiveTotalAmount() + unitCost.getActiveTotalAmount();
//                            unitCost.setActiveTotalAmount(total);
//                        } else {
//                            UnitCost newCost = new UnitCost("DEFAULT");
//                            PropertyUtils.copyProperties(newCost, cost);
//                            intRampChargeCode.add(cost);
//                        }
//                    }
//                } else {
//                    chargeCodeList.add(code);
//                }
//                setExpandedChargeCode(chargeCodeList);
//            }
//        }
//        return expandedChargeCode;
//    }
    public List<ChargeCode> getNormalChargeList(String fileType) throws Exception {
        List<ChargeCode> chargeCodeList = new ArrayList<ChargeCode>();
        ChargeCode ofrChargeCode = null;
        if (!"I".equalsIgnoreCase(fileType)) {
            ofrChargeCode = new ChargeCode(ChargeCode.OCEAN_FREIGHT);
            chargeCodeList.add(ofrChargeCode);

            boolean isImportFound = false;
            for (ChargeCode code : this) {
                if (ChargeCode.OCEAN_FREIGHT.equals(code.getChargeCodeDesc())) {
                    for (UnitCost cost : code) {
                        //if unit cost can be added to collapsed list
                        int index = ofrChargeCode.indexOf(cost);
                        if (index > -1) {
                            UnitCost unitCost = ofrChargeCode.get(index);
                            Double total = cost.getActiveTotalAmount() + unitCost.getActiveTotalAmount();
                            unitCost.setActiveTotalAmount(total);
                        } else {
                            UnitCost newCost = new UnitCost("DEFAULT");
                            PropertyUtils.copyProperties(newCost, cost);
                            ofrChargeCode.add(cost);
                        }
                    }
                } else {
                    chargeCodeList.add(code);
                }
            }
        } else {
            ofrChargeCode = new ChargeCode(ChargeCode.OCEAN_FREIGHT_IMPORT);
            chargeCodeList.add(ofrChargeCode);

            boolean isImportFound = false;
            for (ChargeCode code : this) {
                if (code.getChargCode().equals(ChargeCode.OCEAN_FREIGHT_IMPORT_Code)) {
                    isImportFound = true;
                }
            }
            if (!isImportFound) {
                chargeCodeList = new ArrayList<ChargeCode>();
                return chargeCodeList;
            }

            for (ChargeCode code : this) {

                if ((ChargeCode.OCEAN_FREIGHT_IMPORT_Code.equals(code.getChargCode()))) {
                    for (UnitCost cost : code) {
                        //if unit cost can be added to collapsed list
                        int index = ofrChargeCode.indexOf(cost);
                        if (index > -1) {
                            UnitCost unitCost = ofrChargeCode.get(index);
                            Double total = cost.getActiveTotalAmount() + unitCost.getActiveTotalAmount();
                            unitCost.setActiveTotalAmount(total);
                        } else {
                            UnitCost newCost = new UnitCost("DEFAULT");
                            PropertyUtils.copyProperties(newCost, cost);
                            ofrChargeCode.add(cost);
                        }
                    }
                } else {
                    chargeCodeList.add(code);
                }
            }
        }
        return chargeCodeList;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SSLine other = (SSLine) obj;
        if ((this.ssLineNumber == null) ? (other.ssLineNumber != null) : !this.ssLineNumber.equals(other.ssLineNumber)) {
            return false;
        }
        if ((this.service == null) ? (other.service != null) : !this.service.equals(other.service)) {
            return false;
        }
        return true;
    }

    public String getService() {
        return service;
    }

    public String getSslinenameWithService() {
        return ssLineName + service;
    }

    public void setCollapsedChargeCode(ChargeCode collapsedChargeCode) {
        this.collapsedChargeCode = collapsedChargeCode;
    }

    public List<ChargeCode> getExpandedChargeCode() {
        return expandedChargeCode;
    }

    public void setExpandedChargeCode(List<ChargeCode> expandedChargeCode) {
        this.expandedChargeCode = expandedChargeCode;
    }

    public ChargeCode getPreviousChargeCode() {
        return previousChargeCode;
    }

    public void setPreviousChargeCode(ChargeCode previousChargeCode) {
        this.previousChargeCode = previousChargeCode;
    }

    public String getLocalDrayage() {
        return null == localDrayage ? "N" : localDrayage;
    }

    public void setLocalDrayage(String localDrayage) {
        this.localDrayage = localDrayage;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.ssLineNumber != null ? this.ssLineNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public int compareTo(SSLine o) {
        if (o != null && ssLineName != null) {
            if (service != null && o.getService() != null) {
                return (service + ssLineName).compareTo(o.getService() + o.getSsLineName());
            } else {
                return ssLineName.compareTo(o.getSsLineName());
            }
        } else {
            return -1;
        }
    }
}

/**
 *
 */
package com.gp.cong.logisoft.domain;

import java.util.*;
import java.io.Serializable;

/**
 * @author Yogesh
 *
 */
public class UnitCost implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer unitId;
    private String voyageOrUnitCost;
    private String vendor;
    private String vendorName;
    private String costAmt;
    private Date invDate;
    private String invNo;

    public String getCostAmt() {
        return costAmt;
    }

    public void setCostAmt(String costAmt) {
        this.costAmt = costAmt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInvNo() {
        return invNo;
    }

    public void setInvNo(String invNo) {
        this.invNo = invNo;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVoyageOrUnitCost() {
        return voyageOrUnitCost;
    }

    public void setVoyageOrUnitCost(String voyageOrUnitCost) {
        this.voyageOrUnitCost = voyageOrUnitCost;
    }

    public Date getInvDate() {
        return invDate;
    }

    public void setInvDate(Date invDate) {
        this.invDate = invDate;
    }
}

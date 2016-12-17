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
public class UnitLocation implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer unitId;
    private String currentLocation;
    private Date dateIn;
    private Date dateHandled;
    private String sealIn;
    private String sealOut;
    private String stripBy;
    private String loadBy;
    private String truckingInQuality;
    private String truckingOutQuality;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Date getDateHandled() {
        return dateHandled;
    }

    public void setDateHandled(Date dateHandled) {
        this.dateHandled = dateHandled;
    }

    public Date getDateIn() {
        return dateIn;
    }

    public void setDateIn(Date dateIn) {
        this.dateIn = dateIn;
    }

    public String getSealIn() {
        return sealIn;
    }

    public void setSealIn(String sealIn) {
        this.sealIn = sealIn;
    }

    public String getSealOut() {
        return sealOut;
    }

    public void setSealOut(String sealOut) {
        this.sealOut = sealOut;
    }

    public String getStripBy() {
        return stripBy;
    }

    public void setStripBy(String stripBy) {
        this.stripBy = stripBy;
    }

    public String getLoadBy() {
        return loadBy;
    }

    public void setLoadBy(String loadBy) {
        this.loadBy = loadBy;
    }

    public String getTruckingInQuality() {
        return truckingInQuality;
    }

    public void setTruckingInQuality(String truckingInQuality) {
        this.truckingInQuality = truckingInQuality;
    }

    public String getTruckingOutQuality() {
        return truckingOutQuality;
    }

    public void setTruckingOutQuality(String truckingOutQuality) {
        this.truckingOutQuality = truckingOutQuality;
    }
}

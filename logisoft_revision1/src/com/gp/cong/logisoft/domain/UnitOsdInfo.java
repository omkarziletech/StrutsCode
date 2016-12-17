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
public class UnitOsdInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer unitId;
    private String received;
    private String enteredBy;
    private Date date;
    private String time;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEnteredBy() {
        return enteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        this.enteredBy = enteredBy;
    }

    public String getReceived() {
        return received;
    }

    public void setReceived(String received) {
        this.received = received;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

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
public class UnitInvoice implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer unitId;
    private String enteredBy;
    private Date dateEntered;
    private String time;
    private String readyToInvoice;

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

    public String getEnteredBy() {
        return enteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        this.enteredBy = enteredBy;
    }

    public Date getDateEntered() {
        return dateEntered;
    }

    public void setDateEntered(Date dateEntered) {
        this.dateEntered = dateEntered;
    }

    public String getReadyToInvoice() {
        return readyToInvoice;
    }

    public void setReadyToInvoice(String readyToInvoice) {
        this.readyToInvoice = readyToInvoice;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

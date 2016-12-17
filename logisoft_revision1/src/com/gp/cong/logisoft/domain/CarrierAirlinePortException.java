/**
 *
 */
package com.gp.cong.logisoft.domain;

import java.io.Serializable;

/**
 * @author Yogesh
 *
 */
public class CarrierAirlinePortException implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer airlineid;
    private PortsTemp shedulenumber;

    public Integer getAirlineid() {
        return airlineid;
    }

    public void setAirlineid(Integer airlineid) {
        this.airlineid = airlineid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PortsTemp getShedulenumber() {
        return shedulenumber;
    }

    public void setShedulenumber(PortsTemp shedulenumber) {
        this.shedulenumber = shedulenumber;
    }
}

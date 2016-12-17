/**
 *
 */
package com.gp.cong.logisoft.domain;

import java.io.Serializable;

/**
 * @author Rohith
 *
 */
public class RelayOrigin implements Auditable, Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer relayId;
    private PortsTemp originId;
    private Integer ttToPol;
    private GenericCode cutOffDayOfWeek;
    private Integer cutOffTime;
    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public GenericCode getCutOffDayOfWeek() {
        return cutOffDayOfWeek;
    }

    public void setCutOffDayOfWeek(GenericCode cutOffDayOfWeek) {
        this.cutOffDayOfWeek = cutOffDayOfWeek;
    }

    public Integer getCutOffTime() {
        return cutOffTime;
    }

    public void setCutOffTime(Integer cutOffTime) {
        this.cutOffTime = cutOffTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PortsTemp getOriginId() {
        return originId;
    }

    public void setOriginId(PortsTemp originId) {
        this.originId = originId;
    }

    public Integer getRelayId() {
        return relayId;
    }

    public void setRelayId(Integer relayId) {
        this.relayId = relayId;
    }

    public Integer getTtToPol() {
        return ttToPol;
    }

    public void setTtToPol(Integer ttToPol) {
        this.ttToPol = ttToPol;
    }

    public AuditInfo getAuditInfo() {
        // TODO Auto-generated method stub
        return null;
    }
}

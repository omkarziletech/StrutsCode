/**
 *
 */
package com.gp.cong.logisoft.domain;

import java.io.Serializable;

/**
 * @author Rohith
 *
 */
public class PortException implements Auditable, Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer relayId;
    private PortsTemp originId;
    private PortsTemp destinationId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRelayId() {
        return relayId;
    }

    public void setRelayId(Integer relayId) {
        this.relayId = relayId;
    }

    public PortsTemp getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(PortsTemp destinationId) {
        this.destinationId = destinationId;
    }

    public PortsTemp getOriginId() {
        return originId;
    }

    public void setOriginId(PortsTemp originId) {
        this.originId = originId;
    }

    public AuditInfo getAuditInfo() {
        // TODO Auto-generated method stub
        return null;
    }
}

/**
 *
 */
package com.gp.cong.logisoft.domain;

import java.io.Serializable;

/**
 * @author Rohith
 *
 */
public class RelayDestination implements Auditable, Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer relayId;
    private PortsTemp destinationId;
    private Integer ttFromPodToFd;

    public PortsTemp getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(PortsTemp destinationId) {
        this.destinationId = destinationId;
    }

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

    public Integer getTtFromPodToFd() {
        return ttFromPodToFd;
    }

    public void setTtFromPodToFd(Integer ttFromPodToFd) {
        this.ttFromPodToFd = ttFromPodToFd;
    }

    public AuditInfo getAuditInfo() {
        // TODO Auto-generated method stub
        return null;
    }
}

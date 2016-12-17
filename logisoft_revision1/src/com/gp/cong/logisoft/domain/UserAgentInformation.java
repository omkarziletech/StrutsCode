package com.gp.cong.logisoft.domain;

import java.io.Serializable;

public class UserAgentInformation implements Serializable {

    private Integer id;
    private Ports portId;
    private User userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Ports getPortId() {
        return portId;
    }

    public void setPortId(Ports portId) {
        this.portId = portId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }
}

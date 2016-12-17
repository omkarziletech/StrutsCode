package com.gp.cong.logisoft.domain;

public class APSpecialistTradingPartner implements java.io.Serializable {

    private Integer id;
    private User user;
    private String subType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
}

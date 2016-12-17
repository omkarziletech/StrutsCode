package com.gp.cong.logisoft.domain;

import java.io.Serializable;

public class ItemTree implements Auditable, Serializable {

    private Integer id;
    private Integer itemId;
    private Item parentId;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Item getParentId() {
        return parentId;
    }

    public void setParentId(Item parentId) {
        this.parentId = parentId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AuditInfo getAuditInfo() {
        // TODO Auto-generated method stub
        return null;
    }
}

package com.gp.cong.logisoft.domain;

import java.io.Serializable;

public interface Auditable extends Serializable {
    /**
     * Instances must always return a non-null instance of AuditInfo
     */
    public AuditInfo getAuditInfo();
    
    public Object getId();
    
}
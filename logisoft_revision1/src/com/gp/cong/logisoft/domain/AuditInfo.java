package com.gp.cong.logisoft.domain;

import java.io.Serializable;
import java.sql.Timestamp;

public final class AuditInfo implements Serializable {
    private Timestamp lastUpdated ;
    private Timestamp created ;
    private Long updatedBy ;
    private Long createdBy ;

    public Timestamp getLastUpdated()                 { return lastUpdated;  }
    public void setLastUpdated(Timestamp lastUpdated) { this.lastUpdated = lastUpdated; }
    public Timestamp getCreated()                     { return created; }
    public void setCreated(Timestamp created)         { this.created = created; }
    public Long getUpdatedBy()                        { return updatedBy; }
    public void setUpdatedBy(Long updatedBy)          { this.updatedBy = updatedBy; }
    public Long getCreatedBy()                        { return createdBy; }
    public void setCreatedBy(Long createdBy)          { this.createdBy = createdBy; }
}
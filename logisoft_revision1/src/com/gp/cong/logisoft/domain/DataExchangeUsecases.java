/**
 *
 */
package com.gp.cong.logisoft.domain;

import java.io.Serializable;

/**
 * @author Rohith
 *
 */
public class DataExchangeUsecases implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private Usecases usecaseId;
    private String flowFrom;
    private String usecaseCode;
    private String usecaseName;

    public String getUsecaseCode() {
        return usecaseCode;
    }

    public void setUsecaseCode(String usecaseCode) {
        this.usecaseCode = usecaseCode;
    }

    public String getUsecaseName() {
        return usecaseName;
    }

    public void setUsecaseName(String usecaseName) {
        this.usecaseName = usecaseName;
    }

    public String getFlowFrom() {
        return flowFrom;
    }

    public void setFlowFrom(String flowFrom) {
        this.flowFrom = flowFrom;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usecases getUsecaseId() {
        return usecaseId;
    }

    public void setUsecaseId(Usecases usecaseId) {

        this.usecaseId = usecaseId;
        if (this.usecaseId != null) {
            this.setUsecaseCode(this.usecaseId.getUsecaseCode());
            this.setUsecaseName(this.usecaseId.getUsecaseName());
        }
    }
}
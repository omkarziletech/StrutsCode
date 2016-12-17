package com.gp.cong.logisoft.domain;

/**
 * @author Rohith
 *
 */
import java.io.Serializable;

public class Usecases implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer usecaseId;
    private String usecaseCode;
    private String usecaseName;
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUsecaseCode() {
        return usecaseCode;
    }

    public void setUsecaseCode(String usecaseCode) {
        this.usecaseCode = usecaseCode;
    }

    public Integer getUsecaseId() {
        return usecaseId;
    }

    public void setUsecaseId(Integer usecaseId) {
        this.usecaseId = usecaseId;
    }

    public String getUsecaseName() {
        return usecaseName;
    }

    public void setUsecaseName(String usecaseName) {
        if (usecaseName != null && usecaseName != "") {
            this.usecaseName = usecaseName;
        } else {
            this.usecaseName = "";
        }
    }
}

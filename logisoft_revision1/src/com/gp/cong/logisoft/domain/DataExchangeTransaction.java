package com.gp.cong.logisoft.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataExchangeTransaction implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private Usecases usecases;
    private String docSetKeyValue;
    private String flowFrom;
    private String status;
    private Date date;
    private String name;
    private String usecaseCode;
    private String usecaseName;
    private String useCaseDate;

    public String getUseCaseDate() {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm");
            return sdf.format(date);
        }
        return useCaseDate;
    }

    public void setUseCaseDate(String useCaseDate) {
        this.useCaseDate = useCaseDate;
    }

    public String getUsecaseName() {
        return usecaseName;
    }

    public void setUsecaseName(String usecaseName) {

        this.usecaseName = usecaseName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDocSetKeyValue() {
        return docSetKeyValue;
    }

    public void setDocSetKeyValue(String docSetKeyValue) {
        this.docSetKeyValue = docSetKeyValue;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Usecases getUsecases() {
        return usecases;
    }

    public void setUsecases(Usecases usecases) {
        this.usecases = usecases;
        if (this.usecases != null) {
            this.setUsecaseCode(this.usecases.getUsecaseCode());
            this.setUsecaseName(this.usecases.getUsecaseName());
        }
    }

    public String getUsecaseCode() {
        return usecaseCode;
    }

    public void setUsecaseCode(String usecaseCode) {
        this.usecaseCode = usecaseCode;
    }
}

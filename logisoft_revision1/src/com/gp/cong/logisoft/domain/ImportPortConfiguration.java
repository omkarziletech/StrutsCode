/**
 *
 */
package com.gp.cong.logisoft.domain;

import java.io.Serializable;

public class ImportPortConfiguration implements Auditable, Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer sheduleNumber;
    private RefTerminalTemp trmnum;
    private String lineManager;
    private Integer cityCode;
    private GenericCode ofChargeCode;
    private String importsService;
    private String importAgentNumber;
    private String importAgentWarehouse;
    private Double overWeightLimit20;
    private Double overWeightLimit40;

    public Integer getCityCode() {
        return cityCode;
    }

    public void setCityCode(Integer cityCode) {
        this.cityCode = cityCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLineManager() {
        return lineManager;
    }

    public void setLineManager(String lineManager) {
        this.lineManager = lineManager;
    }

    public RefTerminalTemp getTrmnum() {
        return trmnum;
    }

    public void setTrmnum(RefTerminalTemp trmnum) {
        this.trmnum = trmnum;
    }

    public GenericCode getOfChargeCode() {
        return ofChargeCode;
    }

    public void setOfChargeCode(GenericCode ofChargeCode) {
        this.ofChargeCode = ofChargeCode;
    }

    public Integer getSheduleNumber() {
        return sheduleNumber;
    }

    public void setSheduleNumber(Integer sheduleNumber) {
        this.sheduleNumber = sheduleNumber;
    }

    public AuditInfo getAuditInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getImportAgentNumber() {
        return importAgentNumber;
    }

    public void setImportAgentNumber(String importAgentNumber) {
        this.importAgentNumber = importAgentNumber;
    }

    public String getImportAgentWarehouse() {
        return importAgentWarehouse;
    }

    public void setImportAgentWarehouse(String importAgentWarehouse) {
        this.importAgentWarehouse = importAgentWarehouse;
    }

    public String getImportsService() {
        return importsService;
    }

    public void setImportsService(String importsService) {
        this.importsService = importsService;
    }

    public Double getOverWeightLimit20() {
        return overWeightLimit20;
    }

    public void setOverWeightLimit20(Double overWeightLimit20) {
        this.overWeightLimit20 = overWeightLimit20;
    }

    public Double getOverWeightLimit40() {
        return overWeightLimit40;
    }

    public void setOverWeightLimit40(Double overWeightLimit40) {
        this.overWeightLimit40 = overWeightLimit40;
    }
}

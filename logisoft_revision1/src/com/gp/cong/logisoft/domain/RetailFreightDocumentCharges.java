/**
 *
 */
package com.gp.cong.logisoft.domain;

import java.io.Serializable;

/**
 * @author Yogesh
 *
 */
public class RetailFreightDocumentCharges implements Auditable, Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private GenericCode chargeCode;
    private Integer standardId;
    private Double amount;
    private Integer index;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public GenericCode getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(GenericCode chargeCode) {
        this.chargeCode = chargeCode;
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

    public Integer getStandardId() {
        return standardId;
    }

    public void setStandardId(Integer standardId) {
        this.standardId = standardId;
    }
}

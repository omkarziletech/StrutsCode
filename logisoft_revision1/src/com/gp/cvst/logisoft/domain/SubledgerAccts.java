package com.gp.cvst.logisoft.domain;

public class SubledgerAccts implements java.io.Serializable {

    // Fields    
    private Integer id;
    private Integer subLedgerId;
    private String controlAcct;

    // Constructors
    /**
     * default constructor
     */
    public SubledgerAccts() {
    }

    /**
     * full constructor
     */
    public SubledgerAccts(Integer subLedgerId, String controlAcct) {
        this.subLedgerId = subLedgerId;
        this.controlAcct = controlAcct;
    }

    // Property accessors
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSubLedgerId() {
        return this.subLedgerId;
    }

    public void setSubLedgerId(Integer subLedgerId) {
        this.subLedgerId = subLedgerId;
    }

    public String getControlAcct() {
        return this.controlAcct;
    }

    public void setControlAcct(String controlAcct) {
        this.controlAcct = controlAcct;
    }
}
package com.logiware.hibernate.domain;

import java.util.Date;

/**
 * @author Lakshmi Narayanan
 */
public class TerminalGlMapping implements java.io.Serializable {

    private static final long serialVersionUID = -2650615418724921308L;
    private Integer terminal;
    private Integer lclExportBilling;
    private Integer lclExportLoading;
    private Integer lclExportDockreceipt;
    private Integer fclExportBilling;
    private Integer fclExportLoading;
    private Integer fclExportDockreceipt;
    private Integer airExportBilling;
    private Integer airExportLoading;
    private Integer airExportDockreceipt;
    private Integer lclImportBilling;
    private Integer fclImportBilling;
    private Integer inlandExportLoading;
    private Integer lclImportLoading;
    private Integer fclImportLoading;
    private Integer airImportBilling;
    private Integer airImportLoading;
    private Date createdDate;
    private String createdBy;
    private Date updatedDate;
    private String updatedBy;

    public Integer getTerminal() {
        return terminal;
    }

    public void setTerminal(Integer terminal) {
        this.terminal = terminal;
    }

    public Integer getLclExportBilling() {
        if (null == lclExportBilling || lclExportBilling == 0) {
            lclExportBilling = null;
        }
        return lclExportBilling;
    }

    public void setLclExportBilling(Integer lclExportBilling) {
        this.lclExportBilling = lclExportBilling;
    }

    public Integer getLclExportLoading() {
        if (null == lclExportLoading || lclExportLoading == 0) {
            lclExportLoading = null;
        }
        return lclExportLoading;
    }

    public void setLclExportLoading(Integer lclExportLoading) {
        this.lclExportLoading = lclExportLoading;
    }

    public Integer getLclExportDockreceipt() {
        if (null == lclExportDockreceipt || lclExportDockreceipt == 0) {
            lclExportDockreceipt = null;
        }
        return lclExportDockreceipt;
    }

    public void setLclExportDockreceipt(Integer lclExportDockreceipt) {
        this.lclExportDockreceipt = lclExportDockreceipt;
    }

    public Integer getFclExportBilling() {
        if (null == fclExportBilling || fclExportBilling == 0) {
            fclExportBilling = null;
        }
        return fclExportBilling;
    }

    public void setFclExportBilling(Integer fclExportBilling) {
        this.fclExportBilling = fclExportBilling;
    }

    public Integer getFclExportLoading() {
        if (null == fclExportLoading || fclExportLoading == 0) {
            fclExportLoading = null;
        }
        return fclExportLoading;
    }

    public void setFclExportLoading(Integer fclExportLoading) {
        this.fclExportLoading = fclExportLoading;
    }

    public Integer getFclExportDockreceipt() {
        if (null == fclExportDockreceipt || fclExportDockreceipt == 0) {
            fclExportDockreceipt = null;
        }
        return fclExportDockreceipt;
    }

    public void setFclExportDockreceipt(Integer fclExportDockreceipt) {
        this.fclExportDockreceipt = fclExportDockreceipt;
    }

    public Integer getAirExportBilling() {
        if (null == airExportBilling || airExportBilling == 0) {
            airExportBilling = null;
        }
        return airExportBilling;
    }

    public void setAirExportBilling(Integer airExportBilling) {
        this.airExportBilling = airExportBilling;
    }

    public Integer getAirExportLoading() {
        if (null == airExportLoading || airExportLoading == 0) {
            airExportLoading = null;
        }
        return airExportLoading;
    }

    public void setAirExportLoading(Integer airExportLoading) {
        this.airExportLoading = airExportLoading;
    }

    public Integer getAirExportDockreceipt() {
        if (null == airExportDockreceipt || airExportDockreceipt == 0) {
            airExportDockreceipt = null;
        }
        return airExportDockreceipt;
    }

    public void setAirExportDockreceipt(Integer airExportDockreceipt) {
        this.airExportDockreceipt = airExportDockreceipt;
    }

    public Integer getLclImportBilling() {
        if (null == lclImportBilling || lclImportBilling == 0) {
            lclImportBilling = null;
        }
        return lclImportBilling;
    }

    public void setLclImportBilling(Integer lclImportBilling) {
        this.lclImportBilling = lclImportBilling;
    }

    public Integer getFclImportBilling() {
        if (null == fclImportBilling || fclImportBilling == 0) {
            fclImportBilling = null;
        }
        return fclImportBilling;
    }

    public void setFclImportBilling(Integer fclImportBilling) {
        this.fclImportBilling = fclImportBilling;
    }

    public Integer getInlandExportLoading() {
        if (null == inlandExportLoading || inlandExportLoading == 0) {
            inlandExportLoading = null;
        }
        return inlandExportLoading;
    }

    public void setInlandExportLoading(Integer inlandExportLoading) {
        this.inlandExportLoading = inlandExportLoading;
    }

    public Integer getLclImportLoading() {
        if (null == lclImportLoading || lclImportLoading == 0) {
            lclImportLoading = null;
        }
        return lclImportLoading;
    }

    public void setLclImportLoading(Integer lclImportLoading) {
        this.lclImportLoading = lclImportLoading;
    }

    public Integer getFclImportLoading() {
        if (null == fclImportLoading || fclImportLoading == 0) {
            fclImportLoading = null;
        }
        return fclImportLoading;
    }

    public void setFclImportLoading(Integer fclImportLoading) {
        this.fclImportLoading = fclImportLoading;
    }

    public Integer getAirImportBilling() {
       if (null == airImportBilling || airImportBilling == 0) {
            airImportBilling = null;
        }
        return airImportBilling;
    }

    public void setAirImportBilling(Integer airImportBilling) {
        this.airImportBilling = airImportBilling;
    }

    public Integer getAirImportLoading() {
         if (null == airImportLoading || airImportLoading == 0) {
            airImportLoading = null;
        }
        return airImportLoading;
    }

    public void setAirImportLoading(Integer airImportLoading) {
        this.airImportLoading = airImportLoading;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}

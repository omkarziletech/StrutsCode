package com.gp.cvst.logisoft.domain;

import java.util.Date;


/**
 * Dockreceipt generated by MyEclipse - Hibernate Tools
 */

public class Dockreceipt  implements java.io.Serializable {


    // Fields    

     private Integer dockId;
     private Integer dockReceipt;
     private Integer piece;
     private String cuftWarehouse;
     private String remarks;
     private String hazmat;
     private Date dateIn;
     private Date eta;
     private Date etd;
     private String status;
     private String consignee;
     private String hazNotes;
     private String genNotes;
     private String loadingInstr;
     
     private String weight;
     private String cft;
     private String unitnumbers;
     
     


    // Constructors

    public String getUnitnumbers() {
		return unitnumbers;
	}

	public void setUnitnumbers(String unitnumbers) {
		this.unitnumbers = unitnumbers;
	}

	public String getCft() {
		return cft;
	}

	public void setCft(String cft) {
		this.cft = cft;
	}

	 
	/** default constructor */
    public Dockreceipt() {
    }

	/** minimal constructor */
    public Dockreceipt(Integer dockReceipt) {
        this.dockReceipt = dockReceipt;
    }
    
    /** full constructor */
    public Dockreceipt(Integer dockReceipt, Integer piece, String cuftWarehouse, String remarks, String hazmat, Date dateIn, Date eta, Date etd, String status, String consignee, String hazNotes, String genNotes, String loadingInstr) {
        this.dockReceipt = dockReceipt;
        this.piece = piece;
        this.cuftWarehouse = cuftWarehouse;
        this.remarks = remarks;
        this.hazmat = hazmat;
        this.dateIn = dateIn;
        this.eta = eta;
        this.etd = etd;
        this.status = status;
        this.consignee = consignee;
        this.hazNotes = hazNotes;
        this.genNotes = genNotes;
        this.loadingInstr = loadingInstr;
    }

   
    // Property accessors

    public Integer getDockId() {
        return this.dockId;
    }
    
    public void setDockId(Integer dockId) {
        this.dockId = dockId;
    }

    public Integer getDockReceipt() {
        return this.dockReceipt;
    }
    
    public void setDockReceipt(Integer dockReceipt) {
        this.dockReceipt = dockReceipt;
    }

    public Integer getPiece() {
        return this.piece;
    }
    
    public void setPiece(Integer piece) {
        this.piece = piece;
    }

    public String getCuftWarehouse() {
        return this.cuftWarehouse;
    }
    
    public void setCuftWarehouse(String cuftWarehouse) {
        this.cuftWarehouse = cuftWarehouse;
    }

    public String getRemarks() {
        return this.remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getHazmat() {
        return this.hazmat;
    }
    
    public void setHazmat(String hazmat) {
        this.hazmat = hazmat;
    }

    public Date getDateIn() {
        return this.dateIn;
    }
    
    public void setDateIn(Date dateIn) {
        this.dateIn = dateIn;
    }

    public Date getEta() {
        return this.eta;
    }
    
    public void setEta(Date eta) {
        this.eta = eta;
    }

    public Date getEtd() {
        return this.etd;
    }
    
    public void setEtd(Date etd) {
        this.etd = etd;
    }

    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

    public String getConsignee() {
        return this.consignee;
    }
    
    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getHazNotes() {
        return this.hazNotes;
    }
    
    public void setHazNotes(String hazNotes) {
        this.hazNotes = hazNotes;
    }

    public String getGenNotes() {
        return this.genNotes;
    }
    
    public void setGenNotes(String genNotes) {
        this.genNotes = genNotes;
    }

    public String getLoadingInstr() {
        return this.loadingInstr;
    }
    
    public void setLoadingInstr(String loadingInstr) {
        this.loadingInstr = loadingInstr;
    }

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}
   








}
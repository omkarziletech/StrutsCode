package com.logiware.hibernate.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Lakshmi Naryanan
 */
@Entity
@Table(name = "accrual_migration_error_file")
public class AccrualMigrationErrorFile implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @Column(name = "askfld")
    private String askfld;
    @Basic(optional = false)
    @Column(name = "cntrl")
    private String cntrl;
    @Column(name = "cstcde")
    private String cstcde;
    @Column(name = "amount")
    private Double amount = 0d;
    @Column(name = "paiddt")
    private Integer paiddt;
    @Column(name = "eacode")
    private String eacode;
    @Column(name = "gltrml")
    private String gltrml;
    @Column(name = "vendnm")
    private String vendnm;
    @Column(name = "vend")
    private String vend;
    @Column(name = "invnum")
    private String invnum;
    @Column(name = "invdat")
    private Integer invdat;
    @Column(name = "entdat")
    private Integer entdat;
    @Column(name = "entrby")
    private String entrby;
    @Column(name = "enttim")
    private String enttim;
    @Column(name = "postby")
    private String postby;
    @Column(name = "posted")
    private String posted;
    @Column(name = "posttm")
    private String posttm;
    @Column(name = "chknum")
    private Integer chknum;
    @Column(name = "venref")
    private String venref;
    @Column(name = "expvoy")
    private String expvoy;
    @Column(name = "inlvoy")
    private String inlvoy;
    @Column(name = "unit")
    private String unit;
    @Column(name = "cstdr")
    private String cstdr;
    @Column(name = "updtby")
    private String updtby;
    @Column(name = "upddat")
    private Integer upddat;
    @Column(name = "updtim")
    private String updtim;
    @Column(name = "postdt")
    private Integer postdt;
    @Column(name = "mstvn")
    private String mstvn;
    @Column(name = "uadrs1")
    private String uadrs1;
    @Column(name = "uadrs2")
    private String uadrs2;
    @Column(name = "uacity")
    private String uacity;
    @Column(name = "uastat")
    private String uastat;
    @Column(name = "uazip")
    private String uazip;
    @Column(name = "uaphn")
    private String uaphn;
    @Column(name = "uafax")
    private String uafax;
    @Column(name = "editky")
    private String editky;
    @Column(name = "appddt")
    private Integer appddt;
    @Column(name = "appdby")
    private String appdby;
    @Column(name = "appdtm")
    private String appdtm;
    @Column(name = "autocd")
    private String autocd;
    @Column(name = "cmmnts")
    private String cmmnts;
    @Column(name = "mstck")
    private Integer mstck;
    @Column(name = "agtvn")
    private String agtvn;
    @Column(name = "key022")
    private String key022;
    @Column(name = "faecde")
    private String faecde;
    @Column(name = "actcod")
    private String actcod;
    @Column(name = "cutamt")
    private Double cutamt = 0d;
    @Column(name = "cutdte")
    private Integer cutdte;
    @Column(name = "cuttim")
    private String cuttim;
    @Column(name = "voidby")
    private String voidby;
    @Column(name = "voiddt")
    private Integer voiddt;
    @Column(name = "voidtm")
    private String voidtm;
    @JoinColumn(name = "accrual_migration_log_id", referencedColumnName = "id")
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private AccrualMigrationLog accrualMigrationLog;

    public AccrualMigrationErrorFile() {
    }

    public AccrualMigrationErrorFile(Integer id) {
	this.id = id;
    }

    public AccrualMigrationErrorFile(Integer id, String type, String askfld, String cntrl) {
	this.id = id;
	this.type = type;
	this.askfld = askfld;
	this.cntrl = cntrl;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getAskfld() {
	return askfld;
    }

    public void setAskfld(String askfld) {
	this.askfld = askfld;
    }

    public String getCntrl() {
	return cntrl;
    }

    public void setCntrl(String cntrl) {
	this.cntrl = cntrl;
    }

    public String getCstcde() {
	return cstcde;
    }

    public void setCstcde(String cstcde) {
	this.cstcde = cstcde;
    }

    public Double getAmount() {
	return amount;
    }

    public void setAmount(Double amount) {
	this.amount = amount;
    }

    public Integer getPaiddt() {
	return paiddt;
    }

    public void setPaiddt(Integer paiddt) {
	this.paiddt = paiddt;
    }

    public String getEacode() {
	return eacode;
    }

    public void setEacode(String eacode) {
	this.eacode = eacode;
    }

    public String getGltrml() {
	return gltrml;
    }

    public void setGltrml(String gltrml) {
	this.gltrml = gltrml;
    }

    public String getVendnm() {
	return vendnm;
    }

    public void setVendnm(String vendnm) {
	this.vendnm = vendnm;
    }

    public String getVend() {
	return vend;
    }

    public void setVend(String vend) {
	this.vend = vend;
    }

    public String getInvnum() {
	return invnum;
    }

    public void setInvnum(String invnum) {
	this.invnum = invnum;
    }

    public Integer getInvdat() {
	return invdat;
    }

    public void setInvdat(Integer invdat) {
	this.invdat = invdat;
    }

    public Integer getEntdat() {
	return entdat;
    }

    public void setEntdat(Integer entdat) {
	this.entdat = entdat;
    }

    public String getEntrby() {
	return entrby;
    }

    public void setEntrby(String entrby) {
	this.entrby = entrby;
    }

    public String getEnttim() {
	return enttim;
    }

    public void setEnttim(String enttim) {
	this.enttim = enttim;
    }

    public String getPostby() {
	return postby;
    }

    public void setPostby(String postby) {
	this.postby = postby;
    }

    public String getPosted() {
	return posted;
    }

    public void setPosted(String posted) {
	this.posted = posted;
    }

    public String getPosttm() {
	return posttm;
    }

    public void setPosttm(String posttm) {
	this.posttm = posttm;
    }

    public Integer getChknum() {
	return chknum;
    }

    public void setChknum(Integer chknum) {
	this.chknum = chknum;
    }

    public String getVenref() {
	return venref;
    }

    public void setVenref(String venref) {
	this.venref = venref;
    }

    public String getExpvoy() {
	return expvoy;
    }

    public void setExpvoy(String expvoy) {
	this.expvoy = expvoy;
    }

    public String getInlvoy() {
	return inlvoy;
    }

    public void setInlvoy(String inlvoy) {
	this.inlvoy = inlvoy;
    }

    public String getUnit() {
	return unit;
    }

    public void setUnit(String unit) {
	this.unit = unit;
    }

    public String getCstdr() {
	return cstdr;
    }

    public void setCstdr(String cstdr) {
	this.cstdr = cstdr;
    }

    public String getUpdtby() {
	return updtby;
    }

    public void setUpdtby(String updtby) {
	this.updtby = updtby;
    }

    public Integer getUpddat() {
	return upddat;
    }

    public void setUpddat(Integer upddat) {
	this.upddat = upddat;
    }

    public String getUpdtim() {
	return updtim;
    }

    public void setUpdtim(String updtim) {
	this.updtim = updtim;
    }

    public Integer getPostdt() {
	return postdt;
    }

    public void setPostdt(Integer postdt) {
	this.postdt = postdt;
    }

    public String getMstvn() {
	return mstvn;
    }

    public void setMstvn(String mstvn) {
	this.mstvn = mstvn;
    }

    public String getUadrs1() {
	return uadrs1;
    }

    public void setUadrs1(String uadrs1) {
	this.uadrs1 = uadrs1;
    }

    public String getUadrs2() {
	return uadrs2;
    }

    public void setUadrs2(String uadrs2) {
	this.uadrs2 = uadrs2;
    }

    public String getUacity() {
	return uacity;
    }

    public void setUacity(String uacity) {
	this.uacity = uacity;
    }

    public String getUastat() {
	return uastat;
    }

    public void setUastat(String uastat) {
	this.uastat = uastat;
    }

    public String getUazip() {
	return uazip;
    }

    public void setUazip(String uazip) {
	this.uazip = uazip;
    }

    public String getUaphn() {
	return uaphn;
    }

    public void setUaphn(String uaphn) {
	this.uaphn = uaphn;
    }

    public String getUafax() {
	return uafax;
    }

    public void setUafax(String uafax) {
	this.uafax = uafax;
    }

    public String getEditky() {
	return editky;
    }

    public void setEditky(String editky) {
	this.editky = editky;
    }

    public Integer getAppddt() {
	return appddt;
    }

    public void setAppddt(Integer appddt) {
	this.appddt = appddt;
    }

    public String getAppdby() {
	return appdby;
    }

    public void setAppdby(String appdby) {
	this.appdby = appdby;
    }

    public String getAppdtm() {
	return appdtm;
    }

    public void setAppdtm(String appdtm) {
	this.appdtm = appdtm;
    }

    public String getAutocd() {
	return autocd;
    }

    public void setAutocd(String autocd) {
	this.autocd = autocd;
    }

    public String getCmmnts() {
	return cmmnts;
    }

    public void setCmmnts(String cmmnts) {
	this.cmmnts = cmmnts;
    }

    public Integer getMstck() {
	return mstck;
    }

    public void setMstck(Integer mstck) {
	this.mstck = mstck;
    }

    public String getAgtvn() {
	return agtvn;
    }

    public void setAgtvn(String agtvn) {
	this.agtvn = agtvn;
    }

    public String getKey022() {
	return key022;
    }

    public void setKey022(String key022) {
	this.key022 = key022;
    }

    public String getFaecde() {
	return faecde;
    }

    public void setFaecde(String faecde) {
	this.faecde = faecde;
    }

    public String getActcod() {
	return actcod;
    }

    public void setActcod(String actcod) {
	this.actcod = actcod;
    }

    public Double getCutamt() {
	return cutamt;
    }

    public void setCutamt(Double cutamt) {
	this.cutamt = cutamt;
    }

    public Integer getCutdte() {
	return cutdte;
    }

    public void setCutdte(Integer cutdte) {
	this.cutdte = cutdte;
    }

    public String getCuttim() {
	return cuttim;
    }

    public void setCuttim(String cuttim) {
	this.cuttim = cuttim;
    }

    public String getVoidby() {
	return voidby;
    }

    public void setVoidby(String voidby) {
	this.voidby = voidby;
    }

    public Integer getVoiddt() {
	return voiddt;
    }

    public void setVoiddt(Integer voiddt) {
	this.voiddt = voiddt;
    }

    public String getVoidtm() {
	return voidtm;
    }

    public void setVoidtm(String voidtm) {
	this.voidtm = voidtm;
    }

    public AccrualMigrationLog getAccrualMigrationLog() {
	return accrualMigrationLog;
    }

    public void setAccrualMigrationLog(AccrualMigrationLog accrualMigrationLog) {
	this.accrualMigrationLog = accrualMigrationLog;
    }

    @Override
    public int hashCode() {
	int hash = 0;
	hash += (id != null ? id.hashCode() : 0);
	return hash;
    }

    @Override
    public boolean equals(Object object) {
	// TODO: Warning - this method won't work in the case the id fields are not set
	if (!(object instanceof AccrualMigrationErrorFile)) {
	    return false;
	}
	AccrualMigrationErrorFile other = (AccrualMigrationErrorFile) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.luckyboy.AccrualMigrationErrorFile[ id=" + id + " ]";
    }
}

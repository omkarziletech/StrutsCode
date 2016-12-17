package com.gp.cong.logisoft.domain;

import com.gp.cong.hibernate.Domain;
import java.util.Date;

/**
 * SedFilings entity.
 *
 * @author MyEclipse Persistence Tools
 */
public class SedFilings extends Domain implements java.io.Serializable {

    // Fields
    private Integer id;
    private Date entrdt;
    private String entnam;
    private String expnum;
    private String expnam;
    private String expatn;
    private String expadd;
    private String expcty;
    private String expsta;
    private String expzip;
    private String expicd;
    private String expirs;
    private String exppoa;
    private String expcfn;
    private String expcln;
    private String expcpn;
    private String frtnum;
    private String frtnam;
    private String frtatn;
    private String frtadd;
    private String frtcty;
    private String frtsta;
    private String frtzip;
    private String frticd;
    private String frtirs;
    private String connum;
    private String connam;
    private String conatn;
    private String conadd;
    private String concty;
    private String conpst;
    private String concfn;
    private String concln;
    private String concpn;
    private String conpoa;
    private String itn;
    private String email;
    private String shpdr;
    private String trnref;
    private String ftzone;
    private String inbtyp;
    private String inbind;
    private String inbnd;
    private String orgsta;
    private String exppt;
    private String exppnm;
    private String cntdes;
    private String unptn;
    private String upptna;
    private Date depdat;
    private String modtrn;
    private String scac;
    private String vesnum;
    private String vesnam;
    private String routed;
    private String relate;
    private String waiver;
    private String hazard;
    private String voytrm;
    private String voyprt;
    private String voyvoy;
    private String voysuf;
    private String status;
    private String stsusr;
    private Date stsdte;
    private String bkgnum;
    private String blnum;
    private String consta;
    private String forcfn;
    private String forcln;
    private String forcpn;
    private String origin;
    private String destn;
    private String pol;
    private String pod;
    private String inbent;
    private String unitno;
    private String forwarderCheck;
    private String consigneeCheck;
    private String shipperCheck;
    private String frtirsTp;
    private String expirsTp;
    private String itnStatus;
    private String expctry;
    private String conctry;
    private String forctry;
    private String passportNumber;
    private boolean sched = false;
    private String aesComment;
    private String aesDisabledFlag;
    private String CONTYP;
    private String aesSubmitYear;
    // Constructors

    /**
     * default constructor
     */
    public SedFilings() {
    }

    public SedFilings(Date entrdt, String entnam) {
        this.entrdt = entrdt;
        this.entnam = entnam;
    }

    /**
     * minimal constructor
     */
    /**
     * full constructor
     */
    public String getExpnum() {
        return this.expnum;
    }

    public void setExpnum(String expnum) {
        this.expnum = expnum;
    }

    public String getExpnam() {
        return this.expnam;
    }

    public void setExpnam(String expnam) {
        this.expnam = expnam;
    }

    public String getExpatn() {
        return this.expatn;
    }

    public void setExpatn(String expatn) {
        this.expatn = expatn;
    }

    public String getExpcty() {
        return this.expcty;
    }

    public void setExpcty(String expcty) {
        this.expcty = expcty;
    }

    public String getExpsta() {
        return this.expsta;
    }

    public void setExpsta(String expsta) {
        this.expsta = expsta;
    }

    public String getExpzip() {
        return this.expzip;
    }

    public void setExpzip(String expzip) {
        this.expzip = expzip;
    }

    public String getExpicd() {
        return this.expicd;
    }

    public void setExpicd(String expicd) {
        this.expicd = expicd;
    }

    public String getExpirs() {
        return this.expirs;
    }

    public void setExpirs(String expirs) {
        this.expirs = expirs;
    }

    public String getExppoa() {
        return this.exppoa;
    }

    public void setExppoa(String exppoa) {
        this.exppoa = exppoa;
    }

    public String getExpcfn() {
        return this.expcfn;
    }

    public void setExpcfn(String expcfn) {
        this.expcfn = expcfn;
    }

    public String getExpcln() {
        return this.expcln;
    }

    public void setExpcln(String expcln) {
        this.expcln = expcln;
    }

    public String getExpcpn() {
        return this.expcpn;
    }

    public void setExpcpn(String expcpn) {
        this.expcpn = expcpn;
    }

    public String getFrtnum() {
        return this.frtnum;
    }

    public void setFrtnum(String frtnum) {
        this.frtnum = frtnum;
    }

    public String getFrtnam() {
        return this.frtnam;
    }

    public void setFrtnam(String frtnam) {
        this.frtnam = frtnam;
    }

    public String getFrtatn() {
        return this.frtatn;
    }

    public void setFrtatn(String frtatn) {
        this.frtatn = frtatn;
    }

    public String getFrtcty() {
        return this.frtcty;
    }

    public void setFrtcty(String frtcty) {
        this.frtcty = frtcty;
    }

    public String getFrtsta() {
        return this.frtsta;
    }

    public void setFrtsta(String frtsta) {

        this.frtsta = frtsta;
    }

    public String getFrtzip() {
        return this.frtzip;
    }

    public void setFrtzip(String frtzip) {
        this.frtzip = frtzip;
    }

    public String getFrticd() {
        return this.frticd;
    }

    public void setFrticd(String frticd) {
        this.frticd = frticd;
    }

    public String getFrtirs() {
        return this.frtirs;
    }

    public void setFrtirs(String frtirs) {
        this.frtirs = frtirs;
    }

    public String getConnum() {
        return this.connum;
    }

    public void setConnum(String connum) {
        this.connum = connum;
    }

    public String getConnam() {
        return this.connam;
    }

    public void setConnam(String connam) {
        this.connam = connam;
    }

    public String getConatn() {
        return this.conatn;
    }

    public void setConatn(String conatn) {
        this.conatn = conatn;
    }

    public String getExpadd() {
        return expadd;
    }

    public void setExpadd(String expadd) {
        this.expadd = expadd;
    }

    public String getFrtadd() {
        return frtadd;
    }

    public void setFrtadd(String frtadd) {
        this.frtadd = frtadd;
    }

    public String getConadd() {
        return conadd;
    }

    public void setConadd(String conadd) {
        this.conadd = conadd;
    }

    public String getConcty() {
        return this.concty;
    }

    public void setConcty(String concty) {
        this.concty = concty;
    }

    public String getConpst() {
        return this.conpst;
    }

    public void setConpst(String conpst) {
        this.conpst = conpst;
    }

    public String getConcfn() {
        return this.concfn;
    }

    public void setConcfn(String concfn) {
        this.concfn = concfn;
    }

    public String getConcln() {
        return this.concln;
    }

    public void setConcln(String concln) {
        this.concln = concln;
    }

    public String getConcpn() {
        return this.concpn;
    }

    public void setConcpn(String concpn) {
        this.concpn = concpn;
    }

    public String getConpoa() {
        return this.conpoa;
    }

    public void setConpoa(String conpoa) {
        this.conpoa = conpoa;
    }

    public String getItn() {
        return this.itn;
    }

    public void setItn(String itn) {
        this.itn = itn;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getShpdr() {
        return this.shpdr;
    }

    public void setShpdr(String shpdr) {
        this.shpdr = shpdr;
    }

    public String getTrnref() {
        return this.trnref;
    }

    public void setTrnref(String trnref) {
        this.trnref = trnref;
    }

    public String getFtzone() {
        return this.ftzone;
    }

    public void setFtzone(String ftzone) {
        this.ftzone = ftzone;
    }

    public String getInbtyp() {
        return this.inbtyp;
    }

    public void setInbtyp(String inbtyp) {
        this.inbtyp = inbtyp;
    }

    public String getInbind() {
        return this.inbind;
    }

    public void setInbind(String inbind) {
        this.inbind = inbind;
    }

    public String getInbnd() {
        return this.inbnd;
    }

    public void setInbnd(String inbnd) {
        this.inbnd = inbnd;
    }

    public String getOrgsta() {
        return this.orgsta;
    }

    public void setOrgsta(String orgsta) {
        this.orgsta = orgsta;
    }

    public String getExppt() {
        return this.exppt;
    }

    public void setExppt(String exppt) {
        this.exppt = exppt;
    }

    public String getExppnm() {
        return this.exppnm;
    }

    public void setExppnm(String exppnm) {
        this.exppnm = exppnm;
    }

    public String getCntdes() {
        return this.cntdes;
    }

    public void setCntdes(String cntdes) {
        this.cntdes = cntdes;
    }

    public String getUnptn() {
        return this.unptn;
    }

    public void setUnptn(String unptn) {
        this.unptn = unptn;
    }

    public String getUpptna() {
        return this.upptna;
    }

    public void setUpptna(String upptna) {
        this.upptna = upptna;
    }

    public String getModtrn() {
        return this.modtrn;
    }

    public void setModtrn(String modtrn) {
        this.modtrn = modtrn;
    }

    public String getScac() {
        return this.scac;
    }

    public void setScac(String scac) {
        this.scac = scac;
    }

    public String getVesnum() {
        return this.vesnum;
    }

    public void setVesnum(String vesnum) {
        this.vesnum = vesnum;
    }

    public String getVesnam() {
        return this.vesnam;
    }

    public void setVesnam(String vesnam) {
        this.vesnam = vesnam;
    }

    public String getRouted() {
        return this.routed;
    }

    public void setRouted(String routed) {
        this.routed = routed;
    }

    public String getRelate() {
        return this.relate;
    }

    public void setRelate(String relate) {
        this.relate = relate;
    }

    public String getWaiver() {
        return this.waiver;
    }

    public void setWaiver(String waiver) {
        this.waiver = waiver;
    }

    public String getHazard() {
        return this.hazard;
    }

    public void setHazard(String hazard) {
        this.hazard = hazard;
    }

    public String getVoytrm() {
        return this.voytrm;
    }

    public void setVoytrm(String voytrm) {
        this.voytrm = voytrm;
    }

    public String getVoyprt() {
        return this.voyprt;
    }

    public void setVoyprt(String voyprt) {
        this.voyprt = voyprt;
    }

    public String getVoyvoy() {
        return this.voyvoy;
    }

    public void setVoyvoy(String voyvoy) {
        this.voyvoy = voyvoy;
    }

    public String getVoysuf() {
        return this.voysuf;
    }

    public void setVoysuf(String voysuf) {
        this.voysuf = voysuf;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStsusr() {
        return this.stsusr;
    }

    public void setStsusr(String stsusr) {
        this.stsusr = stsusr;
    }

    public Date getStsdte() {
        return this.stsdte;
    }

    public void setStsdte(Date stsdte) {
        this.stsdte = stsdte;
    }

    public String getBkgnum() {
        return this.bkgnum;
    }

    public void setBkgnum(String bkgnum) {
        this.bkgnum = bkgnum;
    }

    public String getBlnum() {
        return this.blnum;
    }

    public void setBlnum(String blnum) {
        this.blnum = blnum;
    }

    public Date getEntrdt() {
        return entrdt;
    }

    public void setEntrdt(Date entrdt) {
        this.entrdt = entrdt;
    }

    public String getEntnam() {
        return entnam;
    }

    public void setEntnam(String entnam) {
        this.entnam = entnam;
    }

    public Date getDepdat() {
        return depdat;
    }

    public void setDepdat(Date depdat) {
        this.depdat = depdat;
    }

    public String getConsta() {
        return consta;
    }

    public void setConsta(String consta) {
        this.consta = consta;
    }

    public String getForcfn() {
        return forcfn;
    }

    public void setForcfn(String forcfn) {
        this.forcfn = forcfn;
    }

    public String getForcln() {
        return forcln;
    }

    public void setForcln(String forcln) {
        this.forcln = forcln;
    }

    public String getForcpn() {
        return forcpn;
    }

    public void setForcpn(String forcpn) {
        this.forcpn = forcpn;
    }

    public String getDestn() {
        return destn;
    }

    public void setDestn(String destn) {
        this.destn = destn;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getInbent() {
        return inbent;
    }

    public void setInbent(String inbent) {
        this.inbent = inbent;
    }

    public String getUnitno() {
        return unitno;
    }

    public void setUnitno(String unitno) {
        this.unitno = unitno;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConsigneeCheck() {
        return consigneeCheck;
    }

    public void setConsigneeCheck(String consigneeCheck) {
        this.consigneeCheck = consigneeCheck;
    }

    public String getForwarderCheck() {
        return forwarderCheck;
    }

    public void setForwarderCheck(String forwarderCheck) {
        this.forwarderCheck = forwarderCheck;
    }

    public String getShipperCheck() {
        return shipperCheck;
    }

    public void setShipperCheck(String shipperCheck) {
        this.shipperCheck = shipperCheck;
    }

    public String getExpirsTp() {
        return expirsTp;
    }

    public void setExpirsTp(String expirsTp) {
        this.expirsTp = expirsTp;
    }

    public String getFrtirsTp() {
        return frtirsTp;
    }

    public void setFrtirsTp(String frtirsTp) {
        this.frtirsTp = frtirsTp;
    }

    public String getItnStatus() {
        return itnStatus;
    }

    public void setItnStatus(String itnStatus) {
        this.itnStatus = itnStatus;
    }

    public String getConctry() {
        return conctry;
    }

    public void setConctry(String conctry) {
        this.conctry = conctry;
    }

    public String getExpctry() {
        return expctry;
    }

    public void setExpctry(String expctry) {
        this.expctry = expctry;
    }

    public String getForctry() {
        return forctry;
    }

    public void setForctry(String forctry) {
        this.forctry = forctry;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public boolean isSched() {
        return sched;
    }

    public void setSched(boolean sched) {
        this.sched = sched;
    }

    public String getAesComment() {
        return aesComment;
    }

    public void setAesComment(String aesComment) {
        this.aesComment = aesComment;
    }

    public String getAesDisabledFlag() {
        return aesDisabledFlag;
    }

    public void setAesDisabledFlag(String aesDisabledFlag) {
        this.aesDisabledFlag = aesDisabledFlag;
    }

    public String getCONTYP() {
        return CONTYP;
    }

    public void setCONTYP(String CONTYP) {
        this.CONTYP = CONTYP;
    }

    public String getAesSubmitYear() {
        return aesSubmitYear;
    }

    public void setAesSubmitYear(String aesSubmitYear) {
        this.aesSubmitYear = aesSubmitYear;
    }
    
}
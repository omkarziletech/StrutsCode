/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gp.cong.hibernate;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Owner
 */
@Entity
@Table(name = "sed_filings")
@NamedQueries({
    @NamedQuery(name = "Sedfilings.findAll", query = "SELECT s FROM Sedfilings s"),
    @NamedQuery(name = "Sedfilings.findById", query = "SELECT s FROM Sedfilings s WHERE s.id = :id"),
    @NamedQuery(name = "Sedfilings.findByEntrdt", query = "SELECT s FROM Sedfilings s WHERE s.entrdt = :entrdt"),
    @NamedQuery(name = "Sedfilings.findByEntnam", query = "SELECT s FROM Sedfilings s WHERE s.entnam = :entnam"),
    @NamedQuery(name = "Sedfilings.findByExpnum", query = "SELECT s FROM Sedfilings s WHERE s.expnum = :expnum"),
    @NamedQuery(name = "Sedfilings.findByExpnam", query = "SELECT s FROM Sedfilings s WHERE s.expnam = :expnam"),
    @NamedQuery(name = "Sedfilings.findByExpatn", query = "SELECT s FROM Sedfilings s WHERE s.expatn = :expatn"),
    @NamedQuery(name = "Sedfilings.findByExpadd", query = "SELECT s FROM Sedfilings s WHERE s.expadd = :expadd"),
    @NamedQuery(name = "Sedfilings.findByExpcty", query = "SELECT s FROM Sedfilings s WHERE s.expcty = :expcty"),
    @NamedQuery(name = "Sedfilings.findByExpsta", query = "SELECT s FROM Sedfilings s WHERE s.expsta = :expsta"),
    @NamedQuery(name = "Sedfilings.findByExpzip", query = "SELECT s FROM Sedfilings s WHERE s.expzip = :expzip"),
    @NamedQuery(name = "Sedfilings.findByExpicd", query = "SELECT s FROM Sedfilings s WHERE s.expicd = :expicd"),
    @NamedQuery(name = "Sedfilings.findByExpirs", query = "SELECT s FROM Sedfilings s WHERE s.expirs = :expirs"),
    @NamedQuery(name = "Sedfilings.findByExppoa", query = "SELECT s FROM Sedfilings s WHERE s.exppoa = :exppoa"),
    @NamedQuery(name = "Sedfilings.findByExpcfn", query = "SELECT s FROM Sedfilings s WHERE s.expcfn = :expcfn"),
    @NamedQuery(name = "Sedfilings.findByExpcln", query = "SELECT s FROM Sedfilings s WHERE s.expcln = :expcln"),
    @NamedQuery(name = "Sedfilings.findByExpcpn", query = "SELECT s FROM Sedfilings s WHERE s.expcpn = :expcpn"),
    @NamedQuery(name = "Sedfilings.findByFrtnum", query = "SELECT s FROM Sedfilings s WHERE s.frtnum = :frtnum"),
    @NamedQuery(name = "Sedfilings.findByFrtnam", query = "SELECT s FROM Sedfilings s WHERE s.frtnam = :frtnam"),
    @NamedQuery(name = "Sedfilings.findByFrtatn", query = "SELECT s FROM Sedfilings s WHERE s.frtatn = :frtatn"),
    @NamedQuery(name = "Sedfilings.findByFrtadd", query = "SELECT s FROM Sedfilings s WHERE s.frtadd = :frtadd"),
    @NamedQuery(name = "Sedfilings.findByFrtcty", query = "SELECT s FROM Sedfilings s WHERE s.frtcty = :frtcty"),
    @NamedQuery(name = "Sedfilings.findByFrtsta", query = "SELECT s FROM Sedfilings s WHERE s.frtsta = :frtsta"),
    @NamedQuery(name = "Sedfilings.findByFrtzip", query = "SELECT s FROM Sedfilings s WHERE s.frtzip = :frtzip"),
    @NamedQuery(name = "Sedfilings.findByFrticd", query = "SELECT s FROM Sedfilings s WHERE s.frticd = :frticd"),
    @NamedQuery(name = "Sedfilings.findByFrtirs", query = "SELECT s FROM Sedfilings s WHERE s.frtirs = :frtirs"),
    @NamedQuery(name = "Sedfilings.findByConnum", query = "SELECT s FROM Sedfilings s WHERE s.connum = :connum"),
    @NamedQuery(name = "Sedfilings.findByConnam", query = "SELECT s FROM Sedfilings s WHERE s.connam = :connam"),
    @NamedQuery(name = "Sedfilings.findByConatn", query = "SELECT s FROM Sedfilings s WHERE s.conatn = :conatn"),
    @NamedQuery(name = "Sedfilings.findByConadd", query = "SELECT s FROM Sedfilings s WHERE s.conadd = :conadd"),
    @NamedQuery(name = "Sedfilings.findByConcty", query = "SELECT s FROM Sedfilings s WHERE s.concty = :concty"),
    @NamedQuery(name = "Sedfilings.findByConpst", query = "SELECT s FROM Sedfilings s WHERE s.conpst = :conpst"),
    @NamedQuery(name = "Sedfilings.findByConcfn", query = "SELECT s FROM Sedfilings s WHERE s.concfn = :concfn"),
    @NamedQuery(name = "Sedfilings.findByConcln", query = "SELECT s FROM Sedfilings s WHERE s.concln = :concln"),
    @NamedQuery(name = "Sedfilings.findByConcpn", query = "SELECT s FROM Sedfilings s WHERE s.concpn = :concpn"),
    @NamedQuery(name = "Sedfilings.findByConpoa", query = "SELECT s FROM Sedfilings s WHERE s.conpoa = :conpoa"),
    @NamedQuery(name = "Sedfilings.findByItn", query = "SELECT s FROM Sedfilings s WHERE s.itn = :itn"),
    @NamedQuery(name = "Sedfilings.findByEmail", query = "SELECT s FROM Sedfilings s WHERE s.email = :email"),
    @NamedQuery(name = "Sedfilings.findByShpdr", query = "SELECT s FROM Sedfilings s WHERE s.shpdr = :shpdr"),
    @NamedQuery(name = "Sedfilings.findByTrnref", query = "SELECT s FROM Sedfilings s WHERE s.trnref = :trnref"),
    @NamedQuery(name = "Sedfilings.findByFtzone", query = "SELECT s FROM Sedfilings s WHERE s.ftzone = :ftzone"),
    @NamedQuery(name = "Sedfilings.findByInbtyp", query = "SELECT s FROM Sedfilings s WHERE s.inbtyp = :inbtyp"),
    @NamedQuery(name = "Sedfilings.findByInbind", query = "SELECT s FROM Sedfilings s WHERE s.inbind = :inbind"),
    @NamedQuery(name = "Sedfilings.findByInbnd", query = "SELECT s FROM Sedfilings s WHERE s.inbnd = :inbnd"),
    @NamedQuery(name = "Sedfilings.findByOrgsta", query = "SELECT s FROM Sedfilings s WHERE s.orgsta = :orgsta"),
    @NamedQuery(name = "Sedfilings.findByExppt", query = "SELECT s FROM Sedfilings s WHERE s.exppt = :exppt"),
    @NamedQuery(name = "Sedfilings.findByExppnm", query = "SELECT s FROM Sedfilings s WHERE s.exppnm = :exppnm"),
    @NamedQuery(name = "Sedfilings.findByCntdes", query = "SELECT s FROM Sedfilings s WHERE s.cntdes = :cntdes"),
    @NamedQuery(name = "Sedfilings.findByUnptn", query = "SELECT s FROM Sedfilings s WHERE s.unptn = :unptn"),
    @NamedQuery(name = "Sedfilings.findByUpptna", query = "SELECT s FROM Sedfilings s WHERE s.upptna = :upptna"),
    @NamedQuery(name = "Sedfilings.findByDepdat", query = "SELECT s FROM Sedfilings s WHERE s.depdat = :depdat"),
    @NamedQuery(name = "Sedfilings.findByModtrn", query = "SELECT s FROM Sedfilings s WHERE s.modtrn = :modtrn"),
    @NamedQuery(name = "Sedfilings.findByScac", query = "SELECT s FROM Sedfilings s WHERE s.scac = :scac"),
    @NamedQuery(name = "Sedfilings.findByVesnum", query = "SELECT s FROM Sedfilings s WHERE s.vesnum = :vesnum"),
    @NamedQuery(name = "Sedfilings.findByVesnam", query = "SELECT s FROM Sedfilings s WHERE s.vesnam = :vesnam"),
    @NamedQuery(name = "Sedfilings.findByRouted", query = "SELECT s FROM Sedfilings s WHERE s.routed = :routed"),
    @NamedQuery(name = "Sedfilings.findByRelate", query = "SELECT s FROM Sedfilings s WHERE s.relate = :relate"),
    @NamedQuery(name = "Sedfilings.findByWaiver", query = "SELECT s FROM Sedfilings s WHERE s.waiver = :waiver"),
    @NamedQuery(name = "Sedfilings.findByHazard", query = "SELECT s FROM Sedfilings s WHERE s.hazard = :hazard"),
    @NamedQuery(name = "Sedfilings.findByVoytrm", query = "SELECT s FROM Sedfilings s WHERE s.voytrm = :voytrm"),
    @NamedQuery(name = "Sedfilings.findByVoyprt", query = "SELECT s FROM Sedfilings s WHERE s.voyprt = :voyprt"),
    @NamedQuery(name = "Sedfilings.findByVoyvoy", query = "SELECT s FROM Sedfilings s WHERE s.voyvoy = :voyvoy"),
    @NamedQuery(name = "Sedfilings.findByVoysuf", query = "SELECT s FROM Sedfilings s WHERE s.voysuf = :voysuf"),
    @NamedQuery(name = "Sedfilings.findByStatus", query = "SELECT s FROM Sedfilings s WHERE s.status = :status"),
    @NamedQuery(name = "Sedfilings.findByStsusr", query = "SELECT s FROM Sedfilings s WHERE s.stsusr = :stsusr"),
    @NamedQuery(name = "Sedfilings.findByStsdte", query = "SELECT s FROM Sedfilings s WHERE s.stsdte = :stsdte"),
    @NamedQuery(name = "Sedfilings.findByBkgnum", query = "SELECT s FROM Sedfilings s WHERE s.bkgnum = :bkgnum"),
    @NamedQuery(name = "Sedfilings.findByConsta", query = "SELECT s FROM Sedfilings s WHERE s.consta = :consta"),
    @NamedQuery(name = "Sedfilings.findByForcln", query = "SELECT s FROM Sedfilings s WHERE s.forcln = :forcln"),
    @NamedQuery(name = "Sedfilings.findByForcfn", query = "SELECT s FROM Sedfilings s WHERE s.forcfn = :forcfn"),
    @NamedQuery(name = "Sedfilings.findByForcpn", query = "SELECT s FROM Sedfilings s WHERE s.forcpn = :forcpn"),
    @NamedQuery(name = "Sedfilings.findByOrigin", query = "SELECT s FROM Sedfilings s WHERE s.origin = :origin"),
    @NamedQuery(name = "Sedfilings.findByDestn", query = "SELECT s FROM Sedfilings s WHERE s.destn = :destn"),
    @NamedQuery(name = "Sedfilings.findByPol", query = "SELECT s FROM Sedfilings s WHERE s.pol = :pol"),
    @NamedQuery(name = "Sedfilings.findByPod", query = "SELECT s FROM Sedfilings s WHERE s.pod = :pod"),
    @NamedQuery(name = "Sedfilings.findByInbent", query = "SELECT s FROM Sedfilings s WHERE s.inbent = :inbent"),
    @NamedQuery(name = "Sedfilings.findByUnitno", query = "SELECT s FROM Sedfilings s WHERE s.unitno = :unitno"),
    @NamedQuery(name = "Sedfilings.findByConsigneeCheck", query = "SELECT s FROM Sedfilings s WHERE s.consigneeCheck = :consigneeCheck"),
    @NamedQuery(name = "Sedfilings.findByForwarderCheck", query = "SELECT s FROM Sedfilings s WHERE s.forwarderCheck = :forwarderCheck"),
    @NamedQuery(name = "Sedfilings.findByShipperCheck", query = "SELECT s FROM Sedfilings s WHERE s.shipperCheck = :shipperCheck"),
    @NamedQuery(name = "Sedfilings.findByExpirsTp", query = "SELECT s FROM Sedfilings s WHERE s.expirsTp = :expirsTp"),
    @NamedQuery(name = "Sedfilings.findByFrtirsTp", query = "SELECT s FROM Sedfilings s WHERE s.frtirsTp = :frtirsTp"),
    @NamedQuery(name = "Sedfilings.findByExpctry", query = "SELECT s FROM Sedfilings s WHERE s.expctry = :expctry"),
    @NamedQuery(name = "Sedfilings.findByConctry", query = "SELECT s FROM Sedfilings s WHERE s.conctry = :conctry"),
    @NamedQuery(name = "Sedfilings.findByForctry", query = "SELECT s FROM Sedfilings s WHERE s.forctry = :forctry"),
    @NamedQuery(name = "Sedfilings.findByPassportNumber", query = "SELECT s FROM Sedfilings s WHERE s.passportNumber = :passportNumber")})
public class Sedfilings implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "ENTRDT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date entrdt;
    @Column(name = "ENTNAM")
    private String entnam;
    @Column(name = "EXPNUM")
    private String expnum;
    @Column(name = "EXPNAM")
    private String expnam;
    @Column(name = "EXPATN")
    private String expatn;
    @Column(name = "EXPADD")
    private String expadd;
    @Column(name = "EXPCTY")
    private String expcty;
    @Column(name = "EXPSTA")
    private String expsta;
    @Column(name = "EXPZIP")
    private String expzip;
    @Column(name = "EXPICD")
    private String expicd;
    @Column(name = "EXPIRS")
    private String expirs;
    @Column(name = "EXPPOA")
    private String exppoa;
    @Column(name = "EXPCFN")
    private String expcfn;
    @Column(name = "EXPCLN")
    private String expcln;
    @Column(name = "EXPCPN")
    private String expcpn;
    @Column(name = "FRTNUM")
    private String frtnum;
    @Column(name = "FRTNAM")
    private String frtnam;
    @Column(name = "FRTATN")
    private String frtatn;
    @Column(name = "FRTADD")
    private String frtadd;
    @Column(name = "FRTCTY")
    private String frtcty;
    @Column(name = "FRTSTA")
    private String frtsta;
    @Column(name = "FRTZIP")
    private String frtzip;
    @Column(name = "FRTICD")
    private String frticd;
    @Column(name = "FRTIRS")
    private String frtirs;
    @Column(name = "CONNUM")
    private String connum;
    @Column(name = "CONNAM")
    private String connam;
    @Column(name = "CONATN")
    private String conatn;
    @Column(name = "CONADD")
    private String conadd;
    @Column(name = "CONCTY")
    private String concty;
    @Column(name = "CONPST")
    private String conpst;
    @Column(name = "CONCFN")
    private String concfn;
    @Column(name = "CONCLN")
    private String concln;
    @Column(name = "CONCPN")
    private String concpn;
    @Column(name = "CONPOA")
    private String conpoa;
    @Column(name = "ITN")
    private String itn;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "SHPDR")
    private String shpdr;
    @Column(name = "TRNREF")
    private String trnref;
    @Column(name = "FTZONE")
    private String ftzone;
    @Column(name = "inbtyp")
    private String inbtyp;
    @Column(name = "INBIND")
    private String inbind;
    @Column(name = "inbnd")
    private String inbnd;
    @Column(name = "ORGSTA")
    private String orgsta;
    @Column(name = "EXPPT")
    private String exppt;
    @Column(name = "EXPPNM")
    private String exppnm;
    @Column(name = "CNTDES")
    private String cntdes;
    @Column(name = "UNPTN")
    private String unptn;
    @Column(name = "UPPTNA")
    private String upptna;
    @Column(name = "DEPDAT")
    @Temporal(TemporalType.DATE)
    private Date depdat;
    @Column(name = "MODTRN")
    private String modtrn;
    @Column(name = "SCAC")
    private String scac;
    @Column(name = "VESNUM")
    private String vesnum;
    @Column(name = "VESNAM")
    private String vesnam;
    @Column(name = "ROUTED")
    private String routed;
    @Column(name = "RELATE")
    private String relate;
    @Column(name = "WAIVER")
    private String waiver;
    @Column(name = "HAZARD")
    private String hazard;
    @Column(name = "VOYTRM")
    private String voytrm;
    @Column(name = "VOYPRT")
    private String voyprt;
    @Column(name = "VOYVOY")
    private String voyvoy;
    @Column(name = "VOYSUF")
    private String voysuf;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "STSUSR")
    private String stsusr;
    @Column(name = "STSDTE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date stsdte;
    @Column(name = "BKGNUM")
    private String bkgnum;
    @Column(name = "CONSTA")
    private String consta;
    @Column(name = "FORCLN")
    private String forcln;
    @Column(name = "FORCFN")
    private String forcfn;
    @Column(name = "FORCPN")
    private String forcpn;
    @Column(name = "ORIGIN")
    private String origin;
    @Column(name = "DESTN")
    private String destn;
    @Column(name = "POL")
    private String pol;
    @Column(name = "POD")
    private String pod;
    @Column(name = "INBENT")
    private String inbent;
    @Column(name = "UNITNO")
    private String unitno;
    @Column(name = "consignee_check")
    private String consigneeCheck;
    @Column(name = "forwarder_check")
    private String forwarderCheck;
    @Column(name = "shipper_check")
    private String shipperCheck;
    @Column(name = "expirs_tp")
    private String expirsTp;
    @Column(name = "frtirs_tp")
    private String frtirsTp;
    @Column(name = "EXPCTRY")
    private String expctry;
    @Column(name = "CONCTRY")
    private String conctry;
    @Column(name = "FORCTRY")
    private String forctry;
    @Column(name = "passport_number")
    private String passportNumber;
    @Column(name = "aes_comment")
    private String aesComment;
    @Column(name = "aes_disabled_flag")
    private String aesDisabledFlag;
    @JoinColumn(name = "BLNUM", referencedColumnName = "BolId")
    @ManyToOne(fetch = FetchType.LAZY)
    private FclBlNew fclBl;

    public Sedfilings() {
    }

    public Sedfilings(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getExpnum() {
        return expnum;
    }

    public void setExpnum(String expnum) {
        this.expnum = expnum;
    }

    public String getExpnam() {
        return expnam;
    }

    public void setExpnam(String expnam) {
        this.expnam = expnam;
    }

    public String getExpatn() {
        return expatn;
    }

    public void setExpatn(String expatn) {
        this.expatn = expatn;
    }

    public String getExpadd() {
        return expadd;
    }

    public void setExpadd(String expadd) {
        this.expadd = expadd;
    }

    public String getExpcty() {
        return expcty;
    }

    public void setExpcty(String expcty) {
        this.expcty = expcty;
    }

    public String getExpsta() {
        return expsta;
    }

    public void setExpsta(String expsta) {
        this.expsta = expsta;
    }

    public String getExpzip() {
        return expzip;
    }

    public void setExpzip(String expzip) {
        this.expzip = expzip;
    }

    public String getExpicd() {
        return expicd;
    }

    public void setExpicd(String expicd) {
        this.expicd = expicd;
    }

    public String getExpirs() {
        return expirs;
    }

    public void setExpirs(String expirs) {
        this.expirs = expirs;
    }

    public String getExppoa() {
        return exppoa;
    }

    public void setExppoa(String exppoa) {
        this.exppoa = exppoa;
    }

    public String getExpcfn() {
        return expcfn;
    }

    public void setExpcfn(String expcfn) {
        this.expcfn = expcfn;
    }

    public String getExpcln() {
        return expcln;
    }

    public void setExpcln(String expcln) {
        this.expcln = expcln;
    }

    public String getExpcpn() {
        return expcpn;
    }

    public void setExpcpn(String expcpn) {
        this.expcpn = expcpn;
    }

    public String getFrtnum() {
        return frtnum;
    }

    public void setFrtnum(String frtnum) {
        this.frtnum = frtnum;
    }

    public String getFrtnam() {
        return frtnam;
    }

    public void setFrtnam(String frtnam) {
        this.frtnam = frtnam;
    }

    public String getFrtatn() {
        return frtatn;
    }

    public void setFrtatn(String frtatn) {
        this.frtatn = frtatn;
    }

    public String getFrtadd() {
        return frtadd;
    }

    public void setFrtadd(String frtadd) {
        this.frtadd = frtadd;
    }

    public String getFrtcty() {
        return frtcty;
    }

    public void setFrtcty(String frtcty) {
        this.frtcty = frtcty;
    }

    public String getFrtsta() {
        return frtsta;
    }

    public void setFrtsta(String frtsta) {
        this.frtsta = frtsta;
    }

    public String getFrtzip() {
        return frtzip;
    }

    public void setFrtzip(String frtzip) {
        this.frtzip = frtzip;
    }

    public String getFrticd() {
        return frticd;
    }

    public void setFrticd(String frticd) {
        this.frticd = frticd;
    }

    public String getFrtirs() {
        return frtirs;
    }

    public void setFrtirs(String frtirs) {
        this.frtirs = frtirs;
    }

    public String getConnum() {
        return connum;
    }

    public void setConnum(String connum) {
        this.connum = connum;
    }

    public String getConnam() {
        return connam;
    }

    public void setConnam(String connam) {
        this.connam = connam;
    }

    public String getConatn() {
        return conatn;
    }

    public void setConatn(String conatn) {
        this.conatn = conatn;
    }

    public String getConadd() {
        return conadd;
    }

    public void setConadd(String conadd) {
        this.conadd = conadd;
    }

    public String getConcty() {
        return concty;
    }

    public void setConcty(String concty) {
        this.concty = concty;
    }

    public String getConpst() {
        return conpst;
    }

    public void setConpst(String conpst) {
        this.conpst = conpst;
    }

    public String getConcfn() {
        return concfn;
    }

    public void setConcfn(String concfn) {
        this.concfn = concfn;
    }

    public String getConcln() {
        return concln;
    }

    public void setConcln(String concln) {
        this.concln = concln;
    }

    public String getConcpn() {
        return concpn;
    }

    public void setConcpn(String concpn) {
        this.concpn = concpn;
    }

    public String getConpoa() {
        return conpoa;
    }

    public void setConpoa(String conpoa) {
        this.conpoa = conpoa;
    }

    public String getItn() {
        return itn;
    }

    public void setItn(String itn) {
        this.itn = itn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getShpdr() {
        return shpdr;
    }

    public void setShpdr(String shpdr) {
        this.shpdr = shpdr;
    }

    public String getTrnref() {
        return trnref;
    }

    public void setTrnref(String trnref) {
        this.trnref = trnref;
    }

    public String getFtzone() {
        return ftzone;
    }

    public void setFtzone(String ftzone) {
        this.ftzone = ftzone;
    }

    public String getInbtyp() {
        return inbtyp;
    }

    public void setInbtyp(String inbtyp) {
        this.inbtyp = inbtyp;
    }

    public String getInbind() {
        return inbind;
    }

    public void setInbind(String inbind) {
        this.inbind = inbind;
    }

    public String getInbnd() {
        return inbnd;
    }

    public void setInbnd(String inbnd) {
        this.inbnd = inbnd;
    }

    public String getOrgsta() {
        return orgsta;
    }

    public void setOrgsta(String orgsta) {
        this.orgsta = orgsta;
    }

    public String getExppt() {
        return exppt;
    }

    public void setExppt(String exppt) {
        this.exppt = exppt;
    }

    public String getExppnm() {
        return exppnm;
    }

    public void setExppnm(String exppnm) {
        this.exppnm = exppnm;
    }

    public String getCntdes() {
        return cntdes;
    }

    public void setCntdes(String cntdes) {
        this.cntdes = cntdes;
    }

    public String getUnptn() {
        return unptn;
    }

    public void setUnptn(String unptn) {
        this.unptn = unptn;
    }

    public String getUpptna() {
        return upptna;
    }

    public void setUpptna(String upptna) {
        this.upptna = upptna;
    }

    public Date getDepdat() {
        return depdat;
    }

    public void setDepdat(Date depdat) {
        this.depdat = depdat;
    }

    public String getModtrn() {
        return modtrn;
    }

    public void setModtrn(String modtrn) {
        this.modtrn = modtrn;
    }

    public String getScac() {
        return scac;
    }

    public void setScac(String scac) {
        this.scac = scac;
    }

    public String getVesnum() {
        return vesnum;
    }

    public void setVesnum(String vesnum) {
        this.vesnum = vesnum;
    }

    public String getVesnam() {
        return vesnam;
    }

    public void setVesnam(String vesnam) {
        this.vesnam = vesnam;
    }

    public String getRouted() {
        return routed;
    }

    public void setRouted(String routed) {
        this.routed = routed;
    }

    public String getRelate() {
        return relate;
    }

    public void setRelate(String relate) {
        this.relate = relate;
    }

    public String getWaiver() {
        return waiver;
    }

    public void setWaiver(String waiver) {
        this.waiver = waiver;
    }

    public String getHazard() {
        return hazard;
    }

    public void setHazard(String hazard) {
        this.hazard = hazard;
    }

    public String getVoytrm() {
        return voytrm;
    }

    public void setVoytrm(String voytrm) {
        this.voytrm = voytrm;
    }

    public String getVoyprt() {
        return voyprt;
    }

    public void setVoyprt(String voyprt) {
        this.voyprt = voyprt;
    }

    public String getVoyvoy() {
        return voyvoy;
    }

    public void setVoyvoy(String voyvoy) {
        this.voyvoy = voyvoy;
    }

    public String getVoysuf() {
        return voysuf;
    }

    public void setVoysuf(String voysuf) {
        this.voysuf = voysuf;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStsusr() {
        return stsusr;
    }

    public void setStsusr(String stsusr) {
        this.stsusr = stsusr;
    }

    public Date getStsdte() {
        return stsdte;
    }

    public void setStsdte(Date stsdte) {
        this.stsdte = stsdte;
    }

    public String getBkgnum() {
        return bkgnum;
    }

    public void setBkgnum(String bkgnum) {
        this.bkgnum = bkgnum;
    }

    public String getConsta() {
        return consta;
    }

    public void setConsta(String consta) {
        this.consta = consta;
    }

    public String getForcln() {
        return forcln;
    }

    public void setForcln(String forcln) {
        this.forcln = forcln;
    }

    public String getForcfn() {
        return forcfn;
    }

    public void setForcfn(String forcfn) {
        this.forcfn = forcfn;
    }

    public String getForcpn() {
        return forcpn;
    }

    public void setForcpn(String forcpn) {
        this.forcpn = forcpn;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestn() {
        return destn;
    }

    public void setDestn(String destn) {
        this.destn = destn;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
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

    public String getExpctry() {
        return expctry;
    }

    public void setExpctry(String expctry) {
        this.expctry = expctry;
    }

    public String getConctry() {
        return conctry;
    }

    public void setConctry(String conctry) {
        this.conctry = conctry;
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
    
    public FclBlNew getFclBl() {
        return fclBl;
    }

    public void setFclBl(FclBlNew fclBl) {
        this.fclBl = fclBl;
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
        if (!(object instanceof Sedfilings)) {
            return false;
        }
        Sedfilings other = (Sedfilings) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.hibernate.Sedfilings[id=" + id + "]";
    }

}

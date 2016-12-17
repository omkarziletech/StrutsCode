package com.gp.cong.logisoft.domain;

/**
 * Shpsta entity.
 *
 * @author MyEclipse Persistence Tools
 */
public class Shpsta implements java.io.Serializable {

    private Integer id;
    private String msgid;
    private Integer msgdte;
    private Integer msgtim;
    private String sndidt;
    private String rcpidt;
    private String sndnm1;
    private String sndnm2;
    private String rcpnm1;
    private String rcpnm2;
    private String sndcon;
    private String sndtel;
    private String sndfax;
    private String sndeml;
    private String rcpcon;
    private String rcptel;
    private String rcpfax;
    private String rcpeml;
    private String msgtyp;
    private String msgver;
    private String xmlnam;
    private String evncod;
    private String evnlfn;
    private String evltyp;
    private String evnlqa;
    private String evlcva;
    private String locnam;
    private String locntr;
    private String lostat;
    private Integer evndat;
    private Integer evntim;
    private String evnzon;
    private String evdtyp;
    private String evterm;
    private String bkgnum;
    private String blldg1;
    private String blldg2;
    private String blldg3;
    private String blldg4;
    private String blldg5;
    private String blldg6;
    private String blldg7;
    private String blldg8;
    private String blldg9;
    private String blld10;
    private String blshid;
    private String conord;
    private String cntrnu;
    private String cntrpa;
    private String conref;
    private String frtref;
    private String ponum1;
    private String ponum2;
    private String ponum3;
    private String ponum4;
    private String ponum5;
    private String ponum6;
    private String ponum7;
    private String ponum8;
    private String ponum9;
    private String ponu10;
    private String shpide;
    private String salres;
    private String intrbk;
    private String shpcmt;
    private String trnstg;
    private String trnmod;
    private String vesnam;
    private String voynum;
    private String scac;
    private String llycod;
    private String mutdef;
    private String shprcs;
    private String lnenum;
    private String cntsta;
    private String cntnum;
    private String cnttyp;

    // Constructors
    /**
     * default constructor
     */
    public Shpsta() {
    }

    /**
     * minimal constructor
     */
    public Shpsta(String msgid, Integer msgdte, Integer msgtim, String sndidt) {
        this.msgid = msgid;
        this.msgdte = msgdte;
        this.msgtim = msgtim;
        this.sndidt = sndidt;
    }

    /**
     * full constructor
     */
    public Shpsta(String msgid, Integer msgdte, Integer msgtim, String sndidt,
            String rcpidt, String sndnm1, String sndnm2, String rcpnm1,
            String rcpnm2, String sndcon, String sndtel, String sndfax,
            String sndeml, String rcpcon, String rcptel, String rcpfax,
            String rcpeml, String msgtyp, String msgver, String xmlnam,
            String evncod, String evnlfn, String evltyp, String evnlqa,
            String evlcva, String locnam, String locntr, String lostat,
            Integer evndat, Integer evntim, String evnzon, String evdtyp,
            String evterm, String bkgnum, String blldg1, String blldg2,
            String blldg3, String blldg4, String blldg5, String blldg6,
            String blldg7, String blldg8, String blldg9, String blld10,
            String blshid, String conord, String cntrnu, String cntrpa,
            String conref, String frtref, String ponum1, String ponum2,
            String ponum3, String ponum4, String ponum5, String ponum6,
            String ponum7, String ponum8, String ponum9, String ponu10,
            String shpide, String salres, String intrbk, String shpcmt,
            String trnstg, String trnmod, String vesnam, String voynum,
            String scac, String llycod, String mutdef, String shprcs,
            String lnenum, String cntsta, String cntnum, String cnttyp) {
        this.msgid = msgid;
        this.msgdte = msgdte;
        this.msgtim = msgtim;
        this.sndidt = sndidt;
        this.rcpidt = rcpidt;
        this.sndnm1 = sndnm1;
        this.sndnm2 = sndnm2;
        this.rcpnm1 = rcpnm1;
        this.rcpnm2 = rcpnm2;
        this.sndcon = sndcon;
        this.sndtel = sndtel;
        this.sndfax = sndfax;
        this.sndeml = sndeml;
        this.rcpcon = rcpcon;
        this.rcptel = rcptel;
        this.rcpfax = rcpfax;
        this.rcpeml = rcpeml;
        this.msgtyp = msgtyp;
        this.msgver = msgver;
        this.xmlnam = xmlnam;
        this.evncod = evncod;
        this.evnlfn = evnlfn;
        this.evltyp = evltyp;
        this.evnlqa = evnlqa;
        this.evlcva = evlcva;
        this.locnam = locnam;
        this.locntr = locntr;
        this.lostat = lostat;
        this.evndat = evndat;
        this.evntim = evntim;
        this.evnzon = evnzon;
        this.evdtyp = evdtyp;
        this.evterm = evterm;
        this.bkgnum = bkgnum;
        this.blldg1 = blldg1;
        this.blldg2 = blldg2;
        this.blldg3 = blldg3;
        this.blldg4 = blldg4;
        this.blldg5 = blldg5;
        this.blldg6 = blldg6;
        this.blldg7 = blldg7;
        this.blldg8 = blldg8;
        this.blldg9 = blldg9;
        this.blld10 = blld10;
        this.blshid = blshid;
        this.conord = conord;
        this.cntrnu = cntrnu;
        this.cntrpa = cntrpa;
        this.conref = conref;
        this.frtref = frtref;
        this.ponum1 = ponum1;
        this.ponum2 = ponum2;
        this.ponum3 = ponum3;
        this.ponum4 = ponum4;
        this.ponum5 = ponum5;
        this.ponum6 = ponum6;
        this.ponum7 = ponum7;
        this.ponum8 = ponum8;
        this.ponum9 = ponum9;
        this.ponu10 = ponu10;
        this.shpide = shpide;
        this.salres = salres;
        this.intrbk = intrbk;
        this.shpcmt = shpcmt;
        this.trnstg = trnstg;
        this.trnmod = trnmod;
        this.vesnam = vesnam;
        this.voynum = voynum;
        this.scac = scac;
        this.llycod = llycod;
        this.mutdef = mutdef;
        this.shprcs = shprcs;
        this.lnenum = lnenum;
        this.cntsta = cntsta;
        this.cntnum = cntnum;
        this.cnttyp = cnttyp;
    }

    // Property accessors
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMsgid() {
        return this.msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public Integer getMsgdte() {
        return this.msgdte;
    }

    public void setMsgdte(Integer msgdte) {
        this.msgdte = msgdte;
    }

    public Integer getMsgtim() {
        return this.msgtim;
    }

    public void setMsgtim(Integer msgtim) {
        this.msgtim = msgtim;
    }

    public String getSndidt() {
        return this.sndidt;
    }

    public void setSndidt(String sndidt) {
        this.sndidt = sndidt;
    }

    public String getRcpidt() {
        return this.rcpidt;
    }

    public void setRcpidt(String rcpidt) {
        this.rcpidt = rcpidt;
    }

    public String getSndnm1() {
        return this.sndnm1;
    }

    public void setSndnm1(String sndnm1) {
        this.sndnm1 = sndnm1;
    }

    public String getSndnm2() {
        return this.sndnm2;
    }

    public void setSndnm2(String sndnm2) {
        this.sndnm2 = sndnm2;
    }

    public String getRcpnm1() {
        return this.rcpnm1;
    }

    public void setRcpnm1(String rcpnm1) {
        this.rcpnm1 = rcpnm1;
    }

    public String getRcpnm2() {
        return this.rcpnm2;
    }

    public void setRcpnm2(String rcpnm2) {
        this.rcpnm2 = rcpnm2;
    }

    public String getSndcon() {
        return this.sndcon;
    }

    public void setSndcon(String sndcon) {
        this.sndcon = sndcon;
    }

    public String getSndtel() {
        return this.sndtel;
    }

    public void setSndtel(String sndtel) {
        this.sndtel = sndtel;
    }

    public String getSndfax() {
        return this.sndfax;
    }

    public void setSndfax(String sndfax) {
        this.sndfax = sndfax;
    }

    public String getSndeml() {
        return this.sndeml;
    }

    public void setSndeml(String sndeml) {
        this.sndeml = sndeml;
    }

    public String getRcpcon() {
        return this.rcpcon;
    }

    public void setRcpcon(String rcpcon) {
        this.rcpcon = rcpcon;
    }

    public String getRcptel() {
        return this.rcptel;
    }

    public void setRcptel(String rcptel) {
        this.rcptel = rcptel;
    }

    public String getRcpfax() {
        return this.rcpfax;
    }

    public void setRcpfax(String rcpfax) {
        this.rcpfax = rcpfax;
    }

    public String getRcpeml() {
        return this.rcpeml;
    }

    public void setRcpeml(String rcpeml) {
        this.rcpeml = rcpeml;
    }

    public String getMsgtyp() {
        return this.msgtyp;
    }

    public void setMsgtyp(String msgtyp) {
        this.msgtyp = msgtyp;
    }

    public String getMsgver() {
        return this.msgver;
    }

    public void setMsgver(String msgver) {
        this.msgver = msgver;
    }

    public String getXmlnam() {
        return this.xmlnam;
    }

    public void setXmlnam(String xmlnam) {
        this.xmlnam = xmlnam;
    }

    public String getEvncod() {
        return this.evncod;
    }

    public void setEvncod(String evncod) {
        this.evncod = evncod;
    }

    public String getEvnlfn() {
        return this.evnlfn;
    }

    public void setEvnlfn(String evnlfn) {
        this.evnlfn = evnlfn;
    }

    public String getEvltyp() {
        return this.evltyp;
    }

    public void setEvltyp(String evltyp) {
        this.evltyp = evltyp;
    }

    public String getEvnlqa() {
        return this.evnlqa;
    }

    public void setEvnlqa(String evnlqa) {
        this.evnlqa = evnlqa;
    }

    public String getEvlcva() {
        return this.evlcva;
    }

    public void setEvlcva(String evlcva) {
        this.evlcva = evlcva;
    }

    public String getLocnam() {
        return this.locnam;
    }

    public void setLocnam(String locnam) {
        this.locnam = locnam;
    }

    public String getLocntr() {
        return this.locntr;
    }

    public void setLocntr(String locntr) {
        this.locntr = locntr;
    }

    public String getLostat() {
        return this.lostat;
    }

    public void setLostat(String lostat) {
        this.lostat = lostat;
    }

    public Integer getEvndat() {
        return this.evndat;
    }

    public void setEvndat(Integer evndat) {
        this.evndat = evndat;
    }

    public Integer getEvntim() {
        return this.evntim;
    }

    public void setEvntim(Integer evntim) {
        this.evntim = evntim;
    }

    public String getEvnzon() {
        return this.evnzon;
    }

    public void setEvnzon(String evnzon) {
        this.evnzon = evnzon;
    }

    public String getEvdtyp() {
        return this.evdtyp;
    }

    public void setEvdtyp(String evdtyp) {
        this.evdtyp = evdtyp;
    }

    public String getEvterm() {
        return this.evterm;
    }

    public void setEvterm(String evterm) {
        this.evterm = evterm;
    }

    public String getBkgnum() {
        return this.bkgnum;
    }

    public void setBkgnum(String bkgnum) {
        this.bkgnum = bkgnum;
    }

    public String getBlldg1() {
        return this.blldg1;
    }

    public void setBlldg(String blldg, int index) {
        switch (index) {
            case 0:
                setBlldg1(blldg);
                break;
            case 1:
                setBlldg2(blldg);
                break;
            case 2:
                setBlldg3(blldg);
                break;
            case 3:
                setBlldg4(blldg);
                break;
            case 4:
                setBlldg5(blldg);
                break;
            case 5:
                setBlldg6(blldg);
                break;
            case 6:
                setBlldg7(blldg);
                break;
            case 7:
                setBlldg8(blldg);
                break;
            case 8:
                setBlldg9(blldg);
                break;
            case 9:
                setBlld10(blldg);
                break;
            default:
                throw new RuntimeException("Invalid Blldg index");
        }
    }

    public void setPonum(String ponum, int index) {
        switch (index) {
            case 0:
                setPonum1(ponum);
                break;
            case 1:
                setPonum2(ponum);
                break;
            case 2:
                setPonum3(ponum);
                break;
            case 3:
                setPonum4(ponum);
                break;
            case 4:
                setPonum5(ponum);
                break;
            case 5:
                setPonum6(ponum);
                break;
            case 6:
                setPonum7(ponum);
                break;
            case 7:
                setPonum8(ponum);
                break;
            case 8:
                setPonum9(ponum);
                break;
            case 9:
                setPonu10(ponum);
                break;
            default:
                throw new RuntimeException("Invalid Blldg index");
        }
    }

    public void setSndnm(String sndnm, int index) {
        switch (index) {
            case 0:
                setSndnm1(sndnm);
                break;
            case 1:
                setSndnm2(sndnm);
                break;
            default:
                throw new RuntimeException("Invalid Blldg index");
        }
    }

    public void setRcpnm(String rcpnm, int index) {
        switch (index) {
            case 0:
                setRcpnm1(rcpnm);
                break;
            case 1:
                setRcpnm2(rcpnm);
                break;
            default:
                throw new RuntimeException("Invalid Blldg index");
        }
    }

    public void setBlldg1(String blldg1) {
        this.blldg1 = blldg1;
    }

    public String getBlldg2() {
        return this.blldg2;
    }

    public void setBlldg2(String blldg2) {
        this.blldg2 = blldg2;
    }

    public String getBlldg3() {
        return this.blldg3;
    }

    public void setBlldg3(String blldg3) {
        this.blldg3 = blldg3;
    }

    public String getBlldg4() {
        return this.blldg4;
    }

    public void setBlldg4(String blldg4) {
        this.blldg4 = blldg4;
    }

    public String getBlldg5() {
        return this.blldg5;
    }

    public void setBlldg5(String blldg5) {
        this.blldg5 = blldg5;
    }

    public String getBlldg6() {
        return this.blldg6;
    }

    public void setBlldg6(String blldg6) {
        this.blldg6 = blldg6;
    }

    public String getBlldg7() {
        return this.blldg7;
    }

    public void setBlldg7(String blldg7) {
        this.blldg7 = blldg7;
    }

    public String getBlldg8() {
        return this.blldg8;
    }

    public void setBlldg8(String blldg8) {
        this.blldg8 = blldg8;
    }

    public String getBlldg9() {
        return this.blldg9;
    }

    public void setBlldg9(String blldg9) {
        this.blldg9 = blldg9;
    }

    public String getBlld10() {
        return this.blld10;
    }

    public void setBlld10(String blld10) {
        this.blld10 = blld10;
    }

    public String getBlshid() {
        return this.blshid;
    }

    public void setBlshid(String blshid) {
        this.blshid = blshid;
    }

    public String getConord() {
        return this.conord;
    }

    public void setConord(String conord) {
        this.conord = conord;
    }

    public String getCntrnu() {
        return this.cntrnu;
    }

    public void setCntrnu(String cntrnu) {
        this.cntrnu = cntrnu;
    }

    public String getCntrpa() {
        return this.cntrpa;
    }

    public void setCntrpa(String cntrpa) {
        this.cntrpa = cntrpa;
    }

    public String getConref() {
        return this.conref;
    }

    public void setConref(String conref) {
        this.conref = conref;
    }

    public String getFrtref() {
        return this.frtref;
    }

    public void setFrtref(String frtref) {
        this.frtref = frtref;
    }

    public String getPonum1() {
        return this.ponum1;
    }

    public void setPonum1(String ponum1) {
        this.ponum1 = ponum1;
    }

    public String getPonum2() {
        return this.ponum2;
    }

    public void setPonum2(String ponum2) {
        this.ponum2 = ponum2;
    }

    public String getPonum3() {
        return this.ponum3;
    }

    public void setPonum3(String ponum3) {
        this.ponum3 = ponum3;
    }

    public String getPonum4() {
        return this.ponum4;
    }

    public void setPonum4(String ponum4) {
        this.ponum4 = ponum4;
    }

    public String getPonum5() {
        return this.ponum5;
    }

    public void setPonum5(String ponum5) {
        this.ponum5 = ponum5;
    }

    public String getPonum6() {
        return this.ponum6;
    }

    public void setPonum6(String ponum6) {
        this.ponum6 = ponum6;
    }

    public String getPonum7() {
        return this.ponum7;
    }

    public void setPonum7(String ponum7) {
        this.ponum7 = ponum7;
    }

    public String getPonum8() {
        return this.ponum8;
    }

    public void setPonum8(String ponum8) {
        this.ponum8 = ponum8;
    }

    public String getPonum9() {
        return this.ponum9;
    }

    public void setPonum9(String ponum9) {
        this.ponum9 = ponum9;
    }

    public String getPonu10() {
        return this.ponu10;
    }

    public void setPonu10(String ponu10) {
        this.ponu10 = ponu10;
    }

    public String getShpide() {
        return this.shpide;
    }

    public void setShpide(String shpide) {
        this.shpide = shpide;
    }

    public String getSalres() {
        return this.salres;
    }

    public void setSalres(String salres) {
        this.salres = salres;
    }

    public String getIntrbk() {
        return this.intrbk;
    }

    public void setIntrbk(String intrbk) {
        this.intrbk = intrbk;
    }

    public String getShpcmt() {
        return this.shpcmt;
    }

    public void setShpcmt(String shpcmt) {
        this.shpcmt = shpcmt;
    }

    public String getTrnstg() {
        return this.trnstg;
    }

    public void setTrnstg(String trnstg) {
        this.trnstg = trnstg;
    }

    public String getTrnmod() {
        return this.trnmod;
    }

    public void setTrnmod(String trnmod) {
        this.trnmod = trnmod;
    }

    public String getVesnam() {
        return this.vesnam;
    }

    public void setVesnam(String vesnam) {
        this.vesnam = vesnam;
    }

    public String getVoynum() {
        return this.voynum;
    }

    public void setVoynum(String voynum) {
        this.voynum = voynum;
    }

    public String getScac() {
        return this.scac;
    }

    public void setScac(String scac) {
        this.scac = scac;
    }

    public String getLlycod() {
        return this.llycod;
    }

    public void setLlycod(String llycod) {
        this.llycod = llycod;
    }

    public String getMutdef() {
        return this.mutdef;
    }

    public void setMutdef(String mutdef) {
        this.mutdef = mutdef;
    }

    public String getShprcs() {
        return this.shprcs;
    }

    public void setShprcs(String shprcs) {
        this.shprcs = shprcs;
    }

    public String getLnenum() {
        return this.lnenum;
    }

    public void setLnenum(String lnenum) {
        this.lnenum = lnenum;
    }

    public String getCntsta() {
        return this.cntsta;
    }

    public void setCntsta(String cntsta) {
        this.cntsta = cntsta;
    }

    public String getCntnum() {
        return this.cntnum;
    }

    public void setCntnum(String cntnum) {
        this.cntnum = cntnum;
    }

    public String getCnttyp() {
        return this.cnttyp;
    }

    public void setCnttyp(String cnttyp) {
        this.cnttyp = cnttyp;
    }
}
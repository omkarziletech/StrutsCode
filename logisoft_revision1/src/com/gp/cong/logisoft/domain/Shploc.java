package com.gp.cong.logisoft.domain;

/**
 * Shploc entity.
 *
 * @author MyEclipse Persistence Tools
 */
public class Shploc implements java.io.Serializable {

    // Fields
    private Integer id;
    private String msgid;
    private Integer msgdte;
    private Integer msgtim;
    private String sndidt;
    private String rcpidt;
    private String plrfnc;
    private String plrcqa;
    private String plrcod;
    private String plrnam;
    private String plrcry;
    private String plrsta;
    private String plrtrm;
    private String plrdqa;
    private String plrzon;
    private Integer plrdat;
    private Integer plrtim;
    private String polfnc;
    private String polcqa;
    private String polcod;
    private String polnam;
    private String polcry;
    private String polsta;
    private String poltrm;
    private String poldqa;
    private String polzon;
    private Integer poldat;
    private Integer poltim;
    private String podfnc;
    private String podcqa;
    private String podcod;
    private String podnam;
    private String podcry;
    private String podsta;
    private String podtrm;
    private String poddqa;
    private String podzon;
    private Integer poddat;
    private Integer podtim;
    private String pldfnc;
    private String pldcqa;
    private String pldcod;
    private String pldnam;
    private String pldcry;
    private String pldsta;
    private String pldtrm;
    private String plddqa;
    private String pldzon;
    private Integer plddat;
    private Integer pldtim;
    private String intfnc;
    private String intcqa;
    private String intcod;
    private String intnam;
    private String intcry;
    private String intsta;
    private String inttrm;
    private String intdqa;
    private String intzon;
    private Integer intdat;
    private Integer inttim;
    private String carrid;
    private String carna1;
    private String carna2;
    private String carcon;
    private String cartel;
    private String carfax;
    private String careml;
    private String carad1;
    private String carad2;
    private String carad3;
    private String carad4;

    // Constructors
    /**
     * default constructor
     */
    public Shploc() {
    }

    /**
     * minimal constructor
     */
    public Shploc(String msgid, Integer msgdte, Integer msgtim, String sndidt) {
        this.msgid = msgid;
        this.msgdte = msgdte;
        this.msgtim = msgtim;
        this.sndidt = sndidt;
    }

    /**
     * full constructor
     */
    public Shploc(String msgid, Integer msgdte, Integer msgtim, String sndidt,
            String rcpidt, String plrfnc, String plrcqa, String plrcod,
            String plrnam, String plrcry, String plrsta, String plrtrm,
            String plrdqa, String plrzon, Integer plrdat, Integer plrtim,
            String polfnc, String polcqa, String polcod, String polnam,
            String polcry, String polsta, String poltrm, String poldqa,
            String polzon, Integer poldat, Integer poltim, String podfnc,
            String podcqa, String podcod, String podnam, String podcry,
            String podsta, String podtrm, String poddqa, String podzon,
            Integer poddat, Integer podtim, String pldfnc, String pldcqa,
            String pldcod, String pldnam, String pldcry, String pldsta,
            String pldtrm, String plddqa, String pldzon, Integer plddat,
            Integer pldtim, String intfnc, String intcqa, String intcod,
            String intnam, String intcry, String intsta, String inttrm,
            String intdqa, String intzon, Integer intdat, Integer inttim,
            String carrid, String carna1, String carna2, String carcon,
            String cartel, String carfax, String careml, String carad1,
            String carad2, String carad3, String carad4) {
        this.msgid = msgid;
        this.msgdte = msgdte;
        this.msgtim = msgtim;
        this.sndidt = sndidt;
        this.rcpidt = rcpidt;
        this.plrfnc = plrfnc;
        this.plrcqa = plrcqa;
        this.plrcod = plrcod;
        this.plrnam = plrnam;
        this.plrcry = plrcry;
        this.plrsta = plrsta;
        this.plrtrm = plrtrm;
        this.plrdqa = plrdqa;
        this.plrzon = plrzon;
        this.plrdat = plrdat;
        this.plrtim = plrtim;
        this.polfnc = polfnc;
        this.polcqa = polcqa;
        this.polcod = polcod;
        this.polnam = polnam;
        this.polcry = polcry;
        this.polsta = polsta;
        this.poltrm = poltrm;
        this.poldqa = poldqa;
        this.polzon = polzon;
        this.poldat = poldat;
        this.poltim = poltim;
        this.podfnc = podfnc;
        this.podcqa = podcqa;
        this.podcod = podcod;
        this.podnam = podnam;
        this.podcry = podcry;
        this.podsta = podsta;
        this.podtrm = podtrm;
        this.poddqa = poddqa;
        this.podzon = podzon;
        this.poddat = poddat;
        this.podtim = podtim;
        this.pldfnc = pldfnc;
        this.pldcqa = pldcqa;
        this.pldcod = pldcod;
        this.pldnam = pldnam;
        this.pldcry = pldcry;
        this.pldsta = pldsta;
        this.pldtrm = pldtrm;
        this.plddqa = plddqa;
        this.pldzon = pldzon;
        this.plddat = plddat;
        this.pldtim = pldtim;
        this.intfnc = intfnc;
        this.intcqa = intcqa;
        this.intcod = intcod;
        this.intnam = intnam;
        this.intcry = intcry;
        this.intsta = intsta;
        this.inttrm = inttrm;
        this.intdqa = intdqa;
        this.intzon = intzon;
        this.intdat = intdat;
        this.inttim = inttim;
        this.carrid = carrid;
        this.carna1 = carna1;
        this.carna2 = carna2;
        this.carcon = carcon;
        this.cartel = cartel;
        this.carfax = carfax;
        this.careml = careml;
        this.carad1 = carad1;
        this.carad2 = carad2;
        this.carad3 = carad3;
        this.carad4 = carad4;
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

    public String getPlrfnc() {
        return this.plrfnc;
    }

    public void setPlrfnc(String plrfnc) {
        this.plrfnc = plrfnc;
    }

    public String getPlrcqa() {
        return this.plrcqa;
    }

    public void setPlrcqa(String plrcqa) {
        this.plrcqa = plrcqa;
    }

    public String getPlrcod() {
        return this.plrcod;
    }

    public void setPlrcod(String plrcod) {
        this.plrcod = plrcod;
    }

    public String getPlrnam() {
        return this.plrnam;
    }

    public void setPlrnam(String plrnam) {
        this.plrnam = plrnam;
    }

    public String getPlrcry() {
        return this.plrcry;
    }

    public void setPlrcry(String plrcry) {
        this.plrcry = plrcry;
    }

    public String getPlrsta() {
        return this.plrsta;
    }

    public void setPlrsta(String plrsta) {
        this.plrsta = plrsta;
    }

    public String getPlrtrm() {
        return this.plrtrm;
    }

    public void setPlrtrm(String plrtrm) {
        this.plrtrm = plrtrm;
    }

    public String getPlrdqa() {
        return this.plrdqa;
    }

    public void setPlrdqa(String plrdqa) {
        this.plrdqa = plrdqa;
    }

    public String getPlrzon() {
        return this.plrzon;
    }

    public void setPlrzon(String plrzon) {
        this.plrzon = plrzon;
    }

    public Integer getPlrdat() {
        return this.plrdat;
    }

    public void setPlrdat(Integer plrdat) {
        this.plrdat = plrdat;
    }

    public Integer getPlrtim() {
        return this.plrtim;
    }

    public void setPlrtim(Integer plrtim) {
        this.plrtim = plrtim;
    }

    public String getPolfnc() {
        return this.polfnc;
    }

    public void setPolfnc(String polfnc) {
        this.polfnc = polfnc;
    }

    public String getPolcqa() {
        return this.polcqa;
    }

    public void setPolcqa(String polcqa) {
        this.polcqa = polcqa;
    }

    public String getPolcod() {
        return this.polcod;
    }

    public void setPolcod(String polcod) {
        this.polcod = polcod;
    }

    public String getPolnam() {
        return this.polnam;
    }

    public void setPolnam(String polnam) {
        this.polnam = polnam;
    }

    public String getPolcry() {
        return this.polcry;
    }

    public void setPolcry(String polcry) {
        this.polcry = polcry;
    }

    public String getPolsta() {
        return this.polsta;
    }

    public void setPolsta(String polsta) {
        this.polsta = polsta;
    }

    public String getPoltrm() {
        return this.poltrm;
    }

    public void setPoltrm(String poltrm) {
        this.poltrm = poltrm;
    }

    public String getPoldqa() {
        return this.poldqa;
    }

    public void setPoldqa(String poldqa) {
        this.poldqa = poldqa;
    }

    public String getPolzon() {
        return this.polzon;
    }

    public void setPolzon(String polzon) {
        this.polzon = polzon;
    }

    public Integer getPoldat() {
        return this.poldat;
    }

    public void setPoldat(Integer poldat) {
        this.poldat = poldat;
    }

    public Integer getPoltim() {
        return this.poltim;
    }

    public void setPoltim(Integer poltim) {
        this.poltim = poltim;
    }

    public String getPodfnc() {
        return this.podfnc;
    }

    public void setPodfnc(String podfnc) {
        this.podfnc = podfnc;
    }

    public String getPodcqa() {
        return this.podcqa;
    }

    public void setPodcqa(String podcqa) {
        this.podcqa = podcqa;
    }

    public String getPodcod() {
        return this.podcod;
    }

    public void setPodcod(String podcod) {
        this.podcod = podcod;
    }

    public String getPodnam() {
        return this.podnam;
    }

    public void setPodnam(String podnam) {
        this.podnam = podnam;
    }

    public String getPodcry() {
        return this.podcry;
    }

    public void setPodcry(String podcry) {
        this.podcry = podcry;
    }

    public String getPodsta() {
        return this.podsta;
    }

    public void setPodsta(String podsta) {
        this.podsta = podsta;
    }

    public String getPodtrm() {
        return this.podtrm;
    }

    public void setPodtrm(String podtrm) {
        this.podtrm = podtrm;
    }

    public String getPoddqa() {
        return this.poddqa;
    }

    public void setPoddqa(String poddqa) {
        this.poddqa = poddqa;
    }

    public String getPodzon() {
        return this.podzon;
    }

    public void setPodzon(String podzon) {
        this.podzon = podzon;
    }

    public Integer getPoddat() {
        return this.poddat;
    }

    public void setPoddat(Integer poddat) {
        this.poddat = poddat;
    }

    public Integer getPodtim() {
        return this.podtim;
    }

    public void setPodtim(Integer podtim) {
        this.podtim = podtim;
    }

    public String getPldfnc() {
        return this.pldfnc;
    }

    public void setPldfnc(String pldfnc) {
        this.pldfnc = pldfnc;
    }

    public String getPldcqa() {
        return this.pldcqa;
    }

    public void setPldcqa(String pldcqa) {
        this.pldcqa = pldcqa;
    }

    public String getPldcod() {
        return this.pldcod;
    }

    public void setPldcod(String pldcod) {
        this.pldcod = pldcod;
    }

    public String getPldnam() {
        return this.pldnam;
    }

    public void setPldnam(String pldnam) {
        this.pldnam = pldnam;
    }

    public String getPldcry() {
        return this.pldcry;
    }

    public void setPldcry(String pldcry) {
        this.pldcry = pldcry;
    }

    public String getPldsta() {
        return this.pldsta;
    }

    public void setPldsta(String pldsta) {
        this.pldsta = pldsta;
    }

    public String getPldtrm() {
        return this.pldtrm;
    }

    public void setPldtrm(String pldtrm) {
        this.pldtrm = pldtrm;
    }

    public String getPlddqa() {
        return this.plddqa;
    }

    public void setPlddqa(String plddqa) {
        this.plddqa = plddqa;
    }

    public String getPldzon() {
        return this.pldzon;
    }

    public void setPldzon(String pldzon) {
        this.pldzon = pldzon;
    }

    public Integer getPlddat() {
        return this.plddat;
    }

    public void setPlddat(Integer plddat) {
        this.plddat = plddat;
    }

    public Integer getPldtim() {
        return this.pldtim;
    }

    public void setPldtim(Integer pldtim) {
        this.pldtim = pldtim;
    }

    public String getIntfnc() {
        return this.intfnc;
    }

    public void setIntfnc(String intfnc) {
        this.intfnc = intfnc;
    }

    public String getIntcqa() {
        return this.intcqa;
    }

    public void setIntcqa(String intcqa) {
        this.intcqa = intcqa;
    }

    public String getIntcod() {
        return this.intcod;
    }

    public void setIntcod(String intcod) {
        this.intcod = intcod;
    }

    public String getIntnam() {
        return this.intnam;
    }

    public void setIntnam(String intnam) {
        this.intnam = intnam;
    }

    public String getIntcry() {
        return this.intcry;
    }

    public void setIntcry(String intcry) {
        this.intcry = intcry;
    }

    public String getIntsta() {
        return this.intsta;
    }

    public void setIntsta(String intsta) {
        this.intsta = intsta;
    }

    public String getInttrm() {
        return this.inttrm;
    }

    public void setInttrm(String inttrm) {
        this.inttrm = inttrm;
    }

    public String getIntdqa() {
        return this.intdqa;
    }

    public void setIntdqa(String intdqa) {
        this.intdqa = intdqa;
    }

    public String getIntzon() {
        return this.intzon;
    }

    public void setIntzon(String intzon) {
        this.intzon = intzon;
    }

    public Integer getIntdat() {
        return this.intdat;
    }

    public void setIntdat(Integer intdat) {
        this.intdat = intdat;
    }

    public Integer getInttim() {
        return this.inttim;
    }

    public void setInttim(Integer inttim) {
        this.inttim = inttim;
    }

    public String getCarrid() {
        return this.carrid;
    }

    public void setCarrid(String carrid) {
        this.carrid = carrid;
    }

    public String getCarna1() {
        return this.carna1;
    }

    public void setCarna1(String carna1) {
        this.carna1 = carna1;
    }

    public String getCarna2() {
        return this.carna2;
    }

    public void setCarna2(String carna2) {
        this.carna2 = carna2;
    }

    public String getCarcon() {
        return this.carcon;
    }

    public void setCarcon(String carcon) {
        this.carcon = carcon;
    }

    public String getCartel() {
        return this.cartel;
    }

    public void setCartel(String cartel) {
        this.cartel = cartel;
    }

    public String getCarfax() {
        return this.carfax;
    }

    public void setCarfax(String carfax) {
        this.carfax = carfax;
    }

    public String getCareml() {
        return this.careml;
    }

    public void setCareml(String careml) {
        this.careml = careml;
    }

    public String getCarad1() {
        return this.carad1;
    }

    public void setCarad1(String carad1) {
        this.carad1 = carad1;
    }

    public String getCarad2() {
        return this.carad2;
    }

    public void setCarad2(String carad2) {
        this.carad2 = carad2;
    }

    public String getCarad3() {
        return this.carad3;
    }

    public void setCarad3(String carad3) {
        this.carad3 = carad3;
    }

    public String getCarad4() {
        return this.carad4;
    }

    public void setCarad4(String carad4) {
        this.carad4 = carad4;
    }

    public void setCarName(String carName, int index) {
        switch (index) {
            case 0:
                setCarna1(carName);
                break;
            case 1:
                setCarna2(carName);
                break;
            default:
                throw new RuntimeException("Invalid Blldg index");
        }
    }

    public void setCarAddr(String carAddr, int index) {
        switch (index) {
            case 0:
                setCarad1(carAddr);
                break;
            case 1:
                setCarad2(carAddr);
                break;
            case 2:
                setCarad3(carAddr);
                break;
            case 3:
                setCarad4(carAddr);
                break;
            default:
                throw new RuntimeException("Invalid Blldg index");
        }
    }
}
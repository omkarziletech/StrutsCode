/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

import com.gp.cong.logisoft.domain.SedFilings;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.SedSchedulebDetails;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Thamizh
 */
public class LclAesDetailsForm extends LogiwareActionForm {

    private String methodName;
    private String expnam;
    private String expnum;
    private String expadd;
    private String expcty;
    private String expctry;
    private String expsta;
    private String expzip;
    private String expicd;
    private String exppoa;
    private String connam;
    private String exppnm;
    private String upptna;
    private String connum;
    private String conadd;
    private String concty;
    private String conpst;
    private String consZip;
    private String conpoa;
    private String itn;
    private String shpdr;
    private String trnref;
    private String blNo;
    private String sslBkg;
    private String email;
    private String status;
    private String origin;
    private String originState;
    private String routed;
    private String destnCty;
    private String relate;
    private String pol;
    private String waiver;
    private String pod;
    private String hazard;
    private String modtrn;
    private String vesnam;
    private String inbnd;
    private String vesnum;
    private String inbent;
    private String voyvoy;
    private String ftzone;
    private String scac;
    private String inbtyp;
    private String unitno;
    private String inbind;
    private String expcfn;
    private String expcln;
    private String expcpn;
    private String concfn;
    private String concln;
    private String concpn;
    private String fileNumberId;
    private String fileNumber;
    private String shipperCheck;
    private String expirs;
    private String consigneeCheck;
    private String consCountry;
    private String destn;
    private String depDate;
    private String units1;
    private String units2;
    private String buttonValue;
    private String vehicleState;
    private Integer schedId;
    private Date entrdt;
    private String entnam;
    private String domesticOrForeign;
    private String scheduleBNumber;
    private String scheduleBName;
    private String description1;
    private String description2;
    private Integer quantities1;
    private Integer quantities2;
    private String uom2;
    private String uom1;
    private Integer weight;
    private String weightType;
    private Integer value;
    private String exportInformationCode;
    private String licenseType;
    private String usedVehicle;
    private String exportLicense;
    private String eccn;
    private String vehicleIdType;
    private String vehicleIdNumber;
    private String vehicleTitleNumber;
    private String shipment;
    private String trnref1;
    private String cntdes;
    private Integer id;
    private Integer polId;
    private Integer podId;
    private SedFilings sedFilings;
    private SedSchedulebDetails schedB;
    private String consname;
    private String contyp;
    private String totalLicenseValue;
    private List<SedFilings> files;

    public LclAesDetailsForm() {
        if (null == sedFilings) {
            sedFilings = new SedFilings();
        }
        if (null == schedB) {
            schedB = new SedSchedulebDetails();
        }
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public List<SedFilings> getFiles() {
        return files;
    }

    public void setFiles(List<SedFilings> files) {
        this.files = files;
    }

    public Integer getId() {
        return sedFilings.getId();
    }

    public void setId(Integer id) {
        if (id != 0) {
            sedFilings.setId(id);
        }
    }

    public SedFilings getSedFilings() {
        return sedFilings;
    }

    public void setSedFilings(SedFilings sedFilings) {
        this.sedFilings = sedFilings;
    }

    public String getFileNumberId() {
        return fileNumberId;
    }

    public void setFileNumberId(String fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public String getBlNo() {
        return sedFilings.getBlnum();
    }

    public void setBlNo(String blNo) {
        sedFilings.setBlnum(blNo);
    }

    public String getConcfn() {
        return sedFilings.getConcfn();
    }

    public void setConcfn(String concfn) {
        if (CommonUtils.isNotEmpty(concfn)) {
            sedFilings.setConcfn(concfn.toUpperCase());
        }
    }

    public String getConcln() {
        return sedFilings.getConcln();
    }

    public void setConcln(String concln) {
        if (CommonUtils.isNotEmpty(concln)) {
            sedFilings.setConcln(concln.toUpperCase());
        }

    }

    public Integer getPodId() {
        return podId;
    }

    public void setPodId(Integer podId) {
        this.podId = podId;
    }

    public Integer getPolId() {
        return polId;
    }

    public void setPolId(Integer polId) {
        this.polId = polId;
    }

    public String getConsCountry() {
        return sedFilings.getConctry();
    }

    public void setConsCountry(String consCountry) {
        sedFilings.setConctry(consCountry);
    }

    public String getConadd() {
        return sedFilings.getConadd();
    }

    public void setConadd(String conadd) {
        if (CommonUtils.isNotEmpty(conadd)) {
            sedFilings.setConadd(conadd.toUpperCase());
        }
    }

    public String getConcty() {
        return sedFilings.getConcty();
    }

    public void setConcty(String concty) {
        if (CommonUtils.isNotEmpty(concty)) {
            sedFilings.setConcty(concty.toUpperCase());
        }

    }

    public String getConnum() {
        return sedFilings.getConnum();
    }

    public void setConnum(String connum) {
        sedFilings.setConnum(connum);
    }

    public String getConpst() {
        return sedFilings.getConpst();
    }

    public void setConpst(String conpst) {
        sedFilings.setConpst(conpst);
    }

    public String getConsZip() {
        return consZip;
    }

    public void setConsZip(String consZip) {
        this.consZip = consZip;
    }

    public String getConsigneeCheck() {
        return sedFilings.getConsigneeCheck();
    }

    public void setConsigneeCheck(String consigneeCheck) {
        sedFilings.setConsigneeCheck(consigneeCheck);
    }

    public String getExpadd() {
        if (sedFilings.getExpadd() != null) {
            return sedFilings.getExpadd();
        }
        return null;
    }

    public void setExpadd(String expadd) {
        if (CommonUtils.isNotEmpty(expadd)) {
            sedFilings.setExpadd(expadd.toUpperCase());
        }

    }

    public String getExpcty() {
        return sedFilings.getExpcty();
    }

    public void setExpcty(String expcty) {
        if (CommonUtils.isNotEmpty(expcty)) {
            sedFilings.setExpcty(expcty.toUpperCase());
        }
    }

    public String getExpirs() {
        return sedFilings.getExpirs();
    }

    public void setExpirs(String expirs) {
        sedFilings.setExpirs(expirs);
    }

    public String getExpnam() {
        return sedFilings.getExpnam();
    }

    public void setExpnam(String expnam) {
        sedFilings.setExpnam(expnam);
    }

    public String getExpnum() {
        return sedFilings.getExpnum();
    }

    public void setExpnum(String expnum) {
        sedFilings.setExpnum(expnum);
    }

    public String getExpsta() {
        return sedFilings.getExpsta();
    }

    public void setExpsta(String expsta) {
        sedFilings.setExpsta(expsta);
    }

    public String getExpzip() {
        return sedFilings.getExpzip();
    }

    public void setExpzip(String expzip) {
        sedFilings.setExpzip(expzip);
    }

    public String getItn() {
        return sedFilings.getItn();
    }

    public void setItn(String itn) {
        sedFilings.setItn(itn);
    }

    public String getShipperCheck() {
        return sedFilings.getShipperCheck();
    }

    public void setShipperCheck(String shipperCheck) {
        sedFilings.setShipperCheck(shipperCheck);
    }

    public String getShpdr() {
        return sedFilings.getShpdr();
    }

    public void setShpdr(String shpdr) {
        sedFilings.setShpdr(shpdr);
    }

    public String getTrnref() {
        return sedFilings.getTrnref();
    }

    public void setTrnref(String trnref) {
        sedFilings.setTrnref(trnref);
    }

    public String getDepDate() throws Exception {
        String d = DateUtils.formatDate(sedFilings.getDepdat(), "dd-MMM-yyyy");
        return null == d ? "" : d;
    }

    public void setDepDate(String depDate) throws Exception {
        if (CommonUtils.isNotEmpty(depDate)) {
            sedFilings.setDepdat(DateUtils.parseDate(depDate, "dd-MMM-yyyy"));
        }
    }

    public String getDestn() {
        return sedFilings.getDestn();
    }

    public void setDestn(String destn) {
        sedFilings.setDestn(destn);
    }

    public String getConcpn() {
        return sedFilings.getConcpn();
    }

    public void setConcpn(String concpn) {
        sedFilings.setConcpn(concpn);
    }

    public String getConpoa() {
        return sedFilings.getConpoa();
    }

    public void setConpoa(String conpoa) {
        sedFilings.setConpoa(conpoa);
    }

    public String getConnam() {
        return sedFilings.getConnam();
    }

    public void setConnam(String connam) {
        sedFilings.setConnam(connam);
    }

    public String getConsname() {
        return sedFilings.getConnam();
    }

    public void setConsname(String consname) {
        sedFilings.setConnam(consname);
    }

    public String getDestnCty() {
        return destnCty;
    }

    public void setDestnCty(String destnCty) {
        this.destnCty = destnCty;
    }

    public String getEmail() {
        return sedFilings.getEmail();
    }

    public void setEmail(String email) {
        if (CommonUtils.isNotEmpty(email)) {
            sedFilings.setEmail(email.toUpperCase());
        }
    }

    public String getExpcfn() {
        return sedFilings.getExpcfn();
    }

    public void setExpcfn(String expcfn) {
        sedFilings.setExpcfn(expcfn);
    }

    public String getExpcln() {
        return sedFilings.getExpcln();
    }

    public void setExpcln(String expcln) {
        sedFilings.setExpcln(expcln);
    }

    public String getExpcpn() {
        return sedFilings.getExpcpn();
    }

    public void setExpcpn(String expcpn) {
        sedFilings.setExpcpn(expcpn);
    }

    public String getExpicd() {
        return sedFilings.getExpicd();
    }

    public void setExpicd(String expicd) {
        sedFilings.setExpicd(expicd);
    }

    public String getExppoa() {
        return sedFilings.getExppoa();
    }

    public void setExppoa(String exppoa) {
        sedFilings.setExppoa(exppoa);
    }

    public String getFtzone() {
        return sedFilings.getFtzone();
    }

    public void setFtzone(String ftzone) {
        if (CommonUtils.isNotEmpty(ftzone)) {
            sedFilings.setFtzone(ftzone.toUpperCase());
        }
    }

    public String getHazard() {
        return sedFilings.getHazard();
    }

    public void setHazard(String hazard) {
        sedFilings.setHazard(hazard);
    }

    public String getInbent() {
        return sedFilings.getInbent();
    }

    public void setInbent(String inbent) {
        if (CommonUtils.isNotEmpty(inbent)) {
            sedFilings.setInbent(inbent.toUpperCase());
        }

    }

    public String getInbind() {
        return sedFilings.getInbind();
    }

    public void setInbind(String inbind) {
        sedFilings.setInbind(inbind);
    }

    public String getInbnd() {
        return sedFilings.getInbnd();
    }

    public void setInbnd(String inbnd) {
        if (CommonUtils.isNotEmpty(inbnd)) {
            sedFilings.setInbnd(inbnd.toUpperCase());
        }

    }

    public String getInbtyp() {
        return sedFilings.getInbtyp();
    }

    public void setInbtyp(String inbtyp) {
        sedFilings.setInbtyp(inbtyp);
    }

    public String getModtrn() {
        return sedFilings.getModtrn();
    }

    public void setModtrn(String modtrn) {
        sedFilings.setModtrn(modtrn);
    }

    public String getOrigin() {
        return sedFilings.getOrigin();
    }

    public void setOrigin(String origin) {
        sedFilings.setOrigin(origin);
    }

    public String getOriginState() {
        return sedFilings.getOrgsta();
    }

    public void setOriginState(String originState) {
        sedFilings.setOrgsta(originState);
    }

    public String getPod() {
        return sedFilings.getPod();
    }

    public void setPod(String pod) {
        sedFilings.setPod(pod);
    }

    public String getPol() {
        return sedFilings.getPol();
    }

    public void setPol(String pol) {
        sedFilings.setPol(pol);
    }

    public String getRelate() {
        return sedFilings.getRelate();
    }

    public void setRelate(String relate) {
        sedFilings.setRelate(relate);
    }

    public String getRouted() {
        return sedFilings.getRouted();
    }

    public void setRouted(String routed) {
        sedFilings.setRouted(routed);
    }

    public String getScac() {
        return sedFilings.getScac();
    }

    public void setScac(String scac) {
        if (CommonUtils.isNotEmpty(scac)) {
            sedFilings.setScac(scac.toUpperCase());
        }

    }

    public String getSslBkg() {
        return sedFilings.getBkgnum();
    }

    public void setSslBkg(String sslBkg) {
        sedFilings.setBkgnum(sslBkg);
    }

    public String getStatus() {
        return sedFilings.getStatus();
    }

    public void setStatus(String status) {
        sedFilings.setStatus(status);
    }

    public String getUnitno() {
        return sedFilings.getUnitno();
    }

    public void setUnitno(String unitno) {
        if (CommonUtils.isNotEmpty(unitno)) {
            sedFilings.setUnitno(unitno.toUpperCase());
        }

    }

    public String getVesnam() {
        return sedFilings.getVesnam();
    }

    public void setVesnam(String vesnam) {
        if (CommonUtils.isNotEmpty(vesnam)) {
            sedFilings.setVesnam(vesnam.toUpperCase());
        }

    }

    public String getVesnum() {
        return sedFilings.getVesnum();
    }

    public void setVesnum(String vesnum) {
        sedFilings.setVesnum(vesnum);
    }

    public String getExppnm() {
        return sedFilings.getExppnm();
    }

    public void setExppnm(String exppnm) {
        sedFilings.setExppnm(exppnm);
    }

    public String getUpptna() {
        return sedFilings.getUpptna();
    }

    public void setUpptna(String upptna) {
        sedFilings.setUpptna(upptna);
    }

    public String getVoyvoy() {
        return sedFilings.getVoyvoy();
    }

    public void setVoyvoy(String voyvoy) {
        sedFilings.setVoyvoy(voyvoy);
    }

    public String getWaiver() {
        return sedFilings.getWaiver();
    }

    public void setWaiver(String waiver) {
        sedFilings.setWaiver(waiver);
    }

    public String getButtonValue() {
        return buttonValue;
    }

    public void setButtonValue(String buttonValue) {
        this.buttonValue = buttonValue;
    }

    public String getDescription1() {
        return schedB.getDescription1();
    }

    public void setDescription1(String description1) {
        schedB.setDescription1(description1);
    }

    public String getDescription2() {
        return schedB.getDescription2();
    }

    public void setDescription2(String description2) {
        schedB.setDescription2(description2);
    }

    public String getDomesticOrForeign() {
        return schedB.getDomesticOrForeign();
    }

    public void setDomesticOrForeign(String domesticOrForeign) {
        schedB.setDomesticOrForeign(domesticOrForeign);
    }

    public String getEccn() {
        return schedB.getEccn();
    }

    public void setEccn(String eccn) {
        if (CommonUtils.isNotEmpty(eccn)) {
            schedB.setEccn(eccn.toUpperCase());
        }

    }

    public String getEntnam() {
        return schedB.getEntnam();
    }

    public void setEntnam(String entnam) {
        schedB.setEntnam(entnam);
    }

    public Date getEntrdt() {
        return schedB.getEntrdt();
    }

    public void setEntrdt(Date entrdt) {
        schedB.setEntrdt(entrdt);
    }

    public String getExportInformationCode() {
        return schedB.getExportInformationCode();
    }

    public void setExportInformationCode(String exportInformationCode) {
        schedB.setExportInformationCode(exportInformationCode);
    }

    public String getExportLicense() {
        return schedB.getExportLicense();
    }

    public void setExportLicense(String exportLicense) {
        if (CommonUtils.isNotEmpty(exportLicense)) {
            schedB.setExportLicense(exportLicense.toUpperCase());
        }

    }

    public String getLicenseType() {
        return schedB.getLicenseType();
    }

    public void setLicenseType(String licenseType) {
        schedB.setLicenseType(licenseType);
    }

    public Integer getQuantities1() {
        return schedB.getQuantities1();
    }

    public void setQuantities1(Integer quantities1) {
        schedB.setQuantities1(quantities1);
    }

    public Integer getQuantities2() {
        return schedB.getQuantities2();
    }

    public void setQuantities2(Integer quantities2) {
        schedB.setQuantities2(quantities2);
    }

    public SedSchedulebDetails getSchedB() {
        return schedB;
    }

    public void setSchedB(SedSchedulebDetails schedB) {
        this.schedB = schedB;
    }

    public String getScheduleBName() {
        return schedB.getScheduleBName();
    }

    public void setScheduleBName(String scheduleBName) {
        schedB.setScheduleBName(scheduleBName);
    }

    public String getScheduleBNumber() {
        return schedB.getScheduleBNumber();
    }

    public void setScheduleBNumber(String scheduleBNumber) {
        schedB.setScheduleBNumber(scheduleBNumber);
    }

    public String getShipment() {
        return schedB.getShipment();
    }

    public void setShipment(String shipment) {
        schedB.setShipment(shipment);
    }

    public String getTrnref1() {
        return schedB.getTrnref();
    }

    public void setTrnref1(String trnref1) {
        schedB.setTrnref(trnref1);
    }

    public String getUom1() {
        return schedB.getUom1();
    }

    public void setUom1(String uom1) {
        schedB.setUom1(uom1);
    }

    public String getUom2() {
        return schedB.getUom2();
    }

    public void setUom2(String uom2) {
        schedB.setUom2(uom2);
    }

    public String getUsedVehicle() {
        return schedB.getUsedVehicle();
    }

    public void setUsedVehicle(String usedVehicle) {
        schedB.setUsedVehicle(usedVehicle);
    }

    public Integer getValue() {
        return schedB.getValue();
    }

    public void setValue(Integer value) {
        schedB.setValue(value);
    }

    public String getVehicleIdNumber() {
        return schedB.getVehicleIdNumber();
    }

    public void setVehicleIdNumber(String vehicleIdNumber) {
        if (CommonUtils.isNotEmpty(vehicleIdNumber)) {
            schedB.setVehicleIdNumber(vehicleIdNumber.toUpperCase());
        }
    }

    public String getVehicleIdType() {
        return schedB.getVehicleIdType();
    }

    public void setVehicleIdType(String vehicleIdType) {
        schedB.setVehicleIdType(vehicleIdType);
    }

    public String getVehicleTitleNumber() {
        return schedB.getVehicleTitleNumber();
    }

    public void setVehicleTitleNumber(String vehicleTitleNumber) {
        if (CommonUtils.isNotEmpty(vehicleTitleNumber)) {
            schedB.setVehicleTitleNumber(vehicleTitleNumber.toUpperCase());
        }

    }

    public String getTotalLicenseValue() {
        return schedB.getTotalLicenseValue();
    }

    public void setTotalLicenseValue(String totalLicenseValue) {
        schedB.setTotalLicenseValue(totalLicenseValue);
    }

    public Integer getWeight() {
        return schedB.getWeight();
    }

    public void setWeight(Integer weight) {
        schedB.setWeight(weight);
    }

    public String getWeightType() {
        return schedB.getWeightType();
    }

    public void setWeightType(String weightType) {
        schedB.setWeightType(weightType);
    }

    public String getVehicleState() {
        return schedB.getVehicleState();
    }

    public void setVehicleState(String vehicleState) {
        schedB.setVehicleState(vehicleState);
    }

    public Integer getSchedId() {
        return schedB.getId();
    }

    public void setSchedId(Integer schedId) {
        if (schedId != 0) {
            schedB.setId(schedId);
        }
    }

    public String getUnits1() {
        return schedB.getUnits1();
    }

    public void setUnits1(String units1) {
        schedB.setUnits1(units1);
    }

    public String getUnits2() {
        return schedB.getUnits2();
    }

    public void setUnits2(String units2) {
        schedB.setUnits2(units2);
    }

    public String getCntdes() {
        return sedFilings.getCntdes();
    }

    public void setCntdes(String cntdes) {
        sedFilings.setCntdes(cntdes);
    }

    public String getExpctry() {
        return sedFilings.getExpctry();
    }

    public void setExpctry(String expctry) {
        sedFilings.setExpctry(expctry);
    }

    public String getCONTYP() {
        return sedFilings.getCONTYP();
    }

    public void setCONTYP(String contyp) {
        sedFilings.setCONTYP(contyp);
    }
}

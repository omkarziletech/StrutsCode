package com.gp.cong.logisoft.struts.form;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.fcl.CustAddressBC;
import com.gp.cong.logisoft.domain.FclInbondDetails;
import com.gp.cong.logisoft.domain.GeneralInformation;
import com.gp.cong.logisoft.domain.PortsTemp;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.hibernate.dao.EdiDAO;
import com.gp.cong.logisoft.hibernate.dao.GeneralInformationDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO;
import com.gp.cvst.logisoft.hibernate.dao.QuotationDAO;
import com.logiware.form.EventForm;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.struts.action.ActionForm;

public class SedFilingForm extends ActionForm{
	//private String entrdt;
	private String entrDate;
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
	private String exppoa = "N";
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
        private String forcfn;
	private String forcln;
	private String forcpn;
	private String itn;
	private String email;
	private String shpdr;
	private String trnref;
	private String ftzone;
	private String inbtyp;
	private String inbind = "N";
	private String inbnd;
	private String orgsta;
	private String exppt;
	private String exppnm;
	private String cntdes;
	private String unptn;
	private String upptna;
	private String depDate;
	private String modtrn;
	private String scac;
	private String vesnum;
	private String vesnam;
	private String routed;
	private String relate="N";
	private String waiver="N";
	private String hazard;
	private String voytrm;
	private String voyprt;
	private String voyvoy;
	private String voysuf;
	private String status;
	private String stsusr;
	private String stsDate;
	private String bkgnum;
	private String blnum;
	private String conpoa = "N";
	private String consta;
	private String inbent;
	private String unitno;
	
	private String schedId;
	private String domesticOrForeign;
	private String scheduleB_Number;
	private String scheduleB_Name;
	private String description1;
	private String description2;
	private Integer quantities1;
	private Integer quantities2;
	private String units1;
	private String units2;
	private String uom1;
	private String uom2;
	private Integer weight;
	private String weightType;
	private Integer value;
	private String exportInformationCode="OS";
	private String licenseType;
	private String usedVehicle="N";
	private String exportLicense;
	private String eccn;
	private String vehicleIdType;
	
	private String vehicleIdNumber;
	private String vehicleTitleNumber;
	private String vehicleState;
	private String shipment;
	private String origin;
	private String destn;
	private String pol;
	private String pod;
	private String buttonValue;
	private String selectedTab;
	private String forwarderCheck;
	private String consigneeCheck;
	private String shipperCheck;
	private String frtirsTp = "N";
	private String expirsTp = "N";
	private String expctry;
	private String conctry;
	private String forctry;
	private String passportNumber;
	private String CONTYP;
	private String totalLicenseValue;
	
	public SedFilingForm(){

        }
	public SedFilingForm(FclBl fclBl) throws Exception{
            //General Tab
            PortsDAO portsDAO = new PortsDAO();
            this.destn = fclBl.getFinalDestination();
            if (null != fclBl.getDoorOfOrigin() && !"".equals(fclBl.getDoorOfOrigin())) {
                String[] doorOrigin = fclBl.getDoorOfOrigin().split("/");
                if (doorOrigin.length == 2) {
                    String unlocCode = "";
                    String country = "";
                    List list = portsDAO.getPorts(doorOrigin);
                    for (Object obj : list) {
                        PortsTemp portsTemp = (PortsTemp)obj;
                        unlocCode = portsTemp.getUnLocationCode();
                        country = portsTemp.getCountryName();
                    }
                    StringBuilder builder = new StringBuilder();
                    builder.append(fclBl.getDoorOfOrigin());
                    if (null != country && !"".equals(country)) {
                       builder.append("/").append(country);
                    }
                    if (null != unlocCode && !"".equals(unlocCode)) {
                        builder.append("(").append(unlocCode).append(")");
                    }
                    this.origin = builder.toString();
                    this.orgsta = (null != doorOrigin[1] && !"".equals(doorOrigin[1])?doorOrigin[1]:"");
                } else {
                    this.origin = "";
                    this.orgsta = "";
                }
            } else {
                this.origin = fclBl.getTerminal();
                this.orgsta =  (null != fclBl.getTerminal() && fclBl.getTerminal().lastIndexOf("(")> -1) ?
                    portsDAO.getFieldsByUnlocCode("statecode", fclBl.getTerminal().substring(fclBl.getTerminal().lastIndexOf("(")+1,
                    fclBl.getTerminal().length()-1)):"";
            }
            this.pol = fclBl.getPortOfLoading();
            this.pod = fclBl.getPortofDischarge();
            this.cntdes =  getCountryCode((null != fclBl.getFinalDestination() && fclBl.getFinalDestination().lastIndexOf("(")> -1) ?
                fclBl.getFinalDestination().substring(fclBl.getFinalDestination().lastIndexOf("(")+1,
                fclBl.getFinalDestination().length()-1):"");
            this.exppnm =  (null != fclBl.getPortOfLoading() && fclBl.getPortOfLoading().lastIndexOf("(")> -1) ?
                portsDAO.getFieldsByUnlocCode("govschnum", fclBl.getPortOfLoading().substring(fclBl.getPortOfLoading().lastIndexOf("(")+1,
                fclBl.getPortOfLoading().length()-1)):"";
            this.upptna =  (null != fclBl.getPortofDischarge() && fclBl.getPortofDischarge().lastIndexOf("(")> -1) ?
                portsDAO.getFieldsByUnlocCode("govschnum", fclBl.getPortofDischarge().substring(fclBl.getPortofDischarge().lastIndexOf("(")+1,
                fclBl.getPortofDischarge().length()-1)):"";
            
            this.vesnam = null !=fclBl.getVessel()? fclBl.getVessel().getCodedesc():"";
            this.vesnum = null !=fclBl.getVessel()? fclBl.getVessel().getCode():"";
            this.shpdr = fclBl.getFileNo();
            this.blnum = fclBl.getBolId();
            this.voyvoy = fclBl.getVoyages();
            this.bkgnum = fclBl.getBookingNo();
            this.itn = fclBl.getAES();
            Quotation quotation = new QuotationDAO().getFileNoObject(null != fclBl.getFileNo() ? fclBl.getFileNo().indexOf("-")>-1?fclBl.getFileNo().substring(0,fclBl.getFileNo().indexOf("-")):fclBl.getFileNo() : "0");
            BookingFcl bookingFcl = new BookingFclDAO().getFileNoObject(null != fclBl.getFileNo() ? fclBl.getFileNo().indexOf("-")>-1?fclBl.getFileNo().substring(0,fclBl.getFileNo().indexOf("-")):fclBl.getFileNo() : "");
            if(null != quotation){
                 this.hazard = null != quotation.getHazmat()?quotation.getHazmat():"N";
                 this.routed = "yes".equalsIgnoreCase(quotation.getRoutedAgentCheck())?"Y":"N";
            }
            this.depDate = null != fclBl.getSailDate()?DateUtils.parseDateToString(fclBl.getSailDate()):null;
            // Shipper Forwarder And Consignee Tab
            if(null != bookingFcl && CommonUtils.isNotEmpty(bookingFcl.getSpottingAccountNo())){
                setShipperDetails(bookingFcl.getSpottingAccountNo());
            }else if(null != bookingFcl && 
                    (CommonUtils.isNotEmpty(bookingFcl.getSpottingAccountName()) || CommonUtils.isNotEmpty(bookingFcl.getAddressForExpPositioning()))){
                if(CommonUtils.isNotEmpty(bookingFcl.getSpottingAccountName())){
                    this.expnam = bookingFcl.getSpottingAccountName();
                }
                if(CommonUtils.isNotEmpty(bookingFcl.getAddressForExpPositioning())){
                    this.expadd = bookingFcl.getAddressForExpPositioning();
                }
                if(CommonUtils.isNotEmpty(bookingFcl.getSpotAddrCity())){
                    this.expcty = bookingFcl.getSpotAddrCity();
                    
                }
                if(CommonUtils.isNotEmpty(bookingFcl.getSpotAddrState())){
                    this.expsta = bookingFcl.getSpotAddrState();
                }
                if(CommonUtils.isNotEmpty(bookingFcl.getSpotAddrZip())){
                    this.expzip = bookingFcl.getSpotAddrZip();
                }
            }else{
                setShipperDetails(fclBl.getShipperNo());
            }
            setConsigneeDetails(fclBl.getConsigneeNo(),this.cntdes);
//            setForwarderDetails(fclBl.getForwardAgentNo());spottingAccountName
            if (CommonUtils.isNotEmpty(fclBl.getSslineNo())) {
                String carrierScac = new EdiDAO().getSsLine(fclBl.getSslineNo());
                if (null != carrierScac && !carrierScac.trim().equals("") && !carrierScac.trim().equals("00000")) {
                   this.scac =new EdiDAO().getScacOrContract(carrierScac,"SCAC");
                }
            }
            for (Object object : fclBl.getFclInbondDetails()) {
                FclInbondDetails fclInbondDetails = (FclInbondDetails) object;
                this.inbtyp = fclInbondDetails.getInbondType();
                this.inbnd = fclInbondDetails.getInbondNumber();
                break;
            }

        }
	public String getEntrDate() {
		return entrDate;
	}
	public void setEntrDate(String entrDate) {
		this.entrDate = entrDate;
	}
	public String getDepDate() {
		return depDate;
	}
	public void setDepDate(String depDate) {
		this.depDate = depDate;
	}
	public String getStsDate() {
		return stsDate;
	}
	public void setStsDate(String stsDate) {
		this.stsDate = stsDate;
	}
	public String getEntnam() {
		return null != entnam?entnam.toUpperCase():"";
	}
	public void setEntnam(String entnam) {
		this.entnam = null != entnam?entnam.toUpperCase():"";
	}
	public String getExpnum() {
		return null != expnum?expnum.toUpperCase():"";
	}
	public void setExpnum(String expnum) {
		this.expnum = null != expnum?expnum.toUpperCase():"";
	}
	public String getExpnam() {
		return null != expnam?expnam.toUpperCase():"";
	}
	public void setExpnam(String expnam) {
		this.expnam = null != expnam?expnam.toUpperCase():"";
	}
	public String getExpatn() {
		return null != expatn?expatn.toUpperCase():"";
	}
	public void setExpatn(String expatn) {
		this.expatn = null != expatn?expatn.toUpperCase():"";
	}
	public String getExpcty() {
		return null != expcty?expcty.toUpperCase():"";
	}
	public void setExpcty(String expcty) {
		this.expcty = null != expcty?expcty.toUpperCase():"";
	}
	public String getExpsta() {
		return null != expsta?expsta.toUpperCase():"";
	}
	public void setExpsta(String expsta) {
		this.expsta = null != expsta?expsta.toUpperCase():"";
	}
	public String getExpzip() {
		return null != expzip?expzip.toUpperCase():"";
	}
	public void setExpzip(String expzip) {
		this.expzip = null != expzip?expzip.toUpperCase():"";
	}
	public String getExpicd() {
		return null != expicd?expicd.toUpperCase():"";
	}
	public void setExpicd(String expicd) {
		this.expicd = null != expicd?expicd.toUpperCase():"";
	}
	public String getExpirs() {
		return null != expirs?expirs.toUpperCase():"";
	}
	public void setExpirs(String expirs) {
		this.expirs = null != expirs?expirs.toUpperCase():"";
	}
	public String getExppoa() {
		return exppoa;
	}
	public void setExppoa(String exppoa) {
		this.exppoa = exppoa;
	}
	public String getExpcfn() {
		return null != expcfn?expcfn.toUpperCase():"";
	}
	public void setExpcfn(String expcfn) {
		this.expcfn = null != expcfn?expcfn.toUpperCase():"";
	}
	public String getExpcln() {
		return null != expcln?expcln.toUpperCase():"";
	}
	public void setExpcln(String expcln) {
		this.expcln = null != expcln?expcln.toUpperCase():"";
	}
	public String getExpcpn() {
		return null != expcpn?removeSpace(expcpn).toUpperCase():"";
	}
	public void setExpcpn(String expcpn) {
		this.expcpn = null != expcpn?removeSpace(expcpn).toUpperCase():"";
	}
	public String getFrtnum() {
		return null != frtnum?frtnum.toUpperCase():"";
	}
	public void setFrtnum(String frtnum) {
		this.frtnum = null != frtnum?frtnum.toUpperCase():"";
	}
	public String getFrtnam() {
		return null != frtnam?frtnam.toUpperCase():"";
	}
	public void setFrtnam(String frtnam) {
		this.frtnam = null != frtnam?frtnam.toUpperCase():"";
	}
	public String getFrtatn() {
		return null != frtatn?frtatn.toUpperCase():"";
	}
	public void setFrtatn(String frtatn) {
		this.frtatn = null != frtatn?frtatn.toUpperCase():"";
	}
	public String getFrtcty() {
		return null != frtcty?frtcty.toUpperCase():"";
	}
	public void setFrtcty(String frtcty) {
		this.frtcty = null != frtcty?frtcty.toUpperCase():"";
	}
	public String getFrtsta() {
		return null != frtsta?frtsta.toUpperCase():"";
	}
	public void setFrtsta(String frtsta) {
		this.frtsta = null != frtsta?frtsta.toUpperCase():"";
	}
	public String getFrtzip() {
		return null != frtzip?frtzip.toUpperCase():"";
	}
	public void setFrtzip(String frtzip) {
		this.frtzip = null != frtzip?frtzip.toUpperCase():"";
	}
	public String getFrticd() {
		return null != frticd?frticd.toUpperCase():"";
	}
	public void setFrticd(String frticd) {
		this.frticd = null != frticd?frticd.toUpperCase():"";
	}
	public String getFrtirs() {
		return null != frtirs?frtirs.toUpperCase():"";
	}
	public void setFrtirs(String frtirs) {
		this.frtirs = null != frtirs?frtirs.toUpperCase():"";
	}
	public String getConnum() {
		return null != connum?connum.toUpperCase():"";
	}
	public void setConnum(String connum) {
		this.connum = null != connum?connum.toUpperCase():"";
	}
	public String getConnam() {
		return null != connam?connam.toUpperCase():"";
	}
	public void setConnam(String connam) {
		this.connam = null != connam?connam.toUpperCase():"";
	}
	public String getConatn() {
		return null != conatn?conatn.toUpperCase():"";
	}
	public void setConatn(String conatn) {
		this.conatn = null != conatn?conatn.toUpperCase():"";
	}
	public String getExpadd() {
		return null != expadd?expadd.toUpperCase():"";
	}
	public void setExpadd(String expadd) {
		this.expadd = null != expadd?expadd.toUpperCase():"";
	}
	public String getFrtadd() {
		return null != frtadd?frtadd.toUpperCase():"";
	}
	public void setFrtadd(String frtadd) {
		this.frtadd = null != frtadd?frtadd.toUpperCase():"";
	}
	public String getConadd() {
		return null != conadd?conadd.toUpperCase():"";
	}
	public void setConadd(String conadd) {
		this.conadd = null != conadd?conadd.toUpperCase():"";
	}
	public String getConcty() {
		return null != concty?concty.toUpperCase():"";
	}
	public void setConcty(String concty) {
		this.concty = null != concty?concty.toUpperCase():"";
	}
	public String getConpst() {
		return null != conpst?conpst.toUpperCase():"";
	}
	public void setConpst(String conpst) {
		this.conpst = null != conpst?conpst.toUpperCase():"";
	}
	public String getConcfn() {
		return null != concfn?concfn.toUpperCase():"";
	}
	public void setConcfn(String concfn) {
		this.concfn = null != concfn?concfn.toUpperCase():"";
	}
	public String getConcln() {
		return null != concln?concln.toUpperCase():"";
	}
	public void setConcln(String concln) {
		this.concln = null != concln?concln.toUpperCase():"";
	}
	public String getConcpn() {
		return null != concpn?removeSpace(concpn).toUpperCase():"";
	}
	public void setConcpn(String concpn) {
		this.concpn = null != concpn?removeSpace(concpn).toUpperCase():"";
	}
	public String getItn() {
		return null != itn?itn.toUpperCase():"";
	}
	public void setItn(String itn) {
		this.itn = null != itn?itn.toUpperCase():"";
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getShpdr() {
		return null != shpdr?shpdr.toUpperCase():"";
	}
	public void setShpdr(String shpdr) {
		this.shpdr = null != shpdr?shpdr.toUpperCase():"";
	}
	public String getTrnref() {
		return null != trnref?trnref.toUpperCase():"";
	}
	public void setTrnref(String trnref) {
		this.trnref = null != trnref?trnref.toUpperCase():"";
	}
	public String getFtzone() {
		return null != ftzone?ftzone.toUpperCase():"";
	}
	public void setFtzone(String ftzone) {
		this.ftzone = null != ftzone?ftzone.toUpperCase():"";
	}
	public String getInbtyp() {
		return null != inbtyp?inbtyp.toUpperCase():"";
	}
	public void setInbtyp(String inbtyp) {
		this.inbtyp = null != inbtyp?inbtyp.toUpperCase():"";
	}
	public String getInbind() {
		return null != inbind?inbind.toUpperCase():"";
	}
	public void setInbind(String inbind) {
		this.inbind = null != inbind?inbind.toUpperCase():"";
	}
	public String getOrgsta() {
		return null != orgsta?orgsta.toUpperCase():"";
	}
	public void setOrgsta(String orgsta) {
		this.orgsta = null != orgsta?orgsta.toUpperCase():"";
	}
	public String getInbnd() {
		return null != inbnd?inbnd.toUpperCase():"";
	}
	public void setInbnd(String inbnd) {
		this.inbnd = null != inbnd?inbnd.toUpperCase():"";
	}
	public String getExppt() {
		return null != exppt?exppt.toUpperCase():"";
	}
	public void setExppt(String exppt) {
		this.exppt = null != exppt?exppt.toUpperCase():"";
	}
	public String getExppnm() {
		return null != exppnm?exppnm.toUpperCase():"";
	}
	public void setExppnm(String exppnm) {
		this.exppnm = null != exppnm?exppnm.toUpperCase():"";
	}
	public String getCntdes() {
		return null != cntdes?cntdes.toUpperCase():"";
	}
	public void setCntdes(String cntdes) {
		this.cntdes = null != cntdes?cntdes.toUpperCase():"";
	}
	public String getUnptn() {
		return null != unptn?unptn.toUpperCase():"";
	}
	public void setUnptn(String unptn) {
		this.unptn = null != unptn?unptn.toUpperCase():"";
	}
	public String getUpptna() {
		return null != upptna?upptna.toUpperCase():"";
	}
	public void setUpptna(String upptna) {
		this.upptna = null != upptna?upptna.toUpperCase():"";
	}
	
	public String getModtrn() {
		return null != modtrn?modtrn.toUpperCase():"";
	}
	public void setModtrn(String modtrn) {
		this.modtrn = null != modtrn?modtrn.toUpperCase():"";
	}
	public String getScac() {
		return null != scac?scac.toUpperCase():"";
	}
	public void setScac(String scac) {
		this.scac = null != scac?scac.toUpperCase():"";
	}
	public String getVesnum() {
		return null != vesnum?vesnum.toUpperCase():"";
	}
	public void setVesnum(String vesnum) {
		this.vesnum = null != vesnum?vesnum.toUpperCase():"";
	}
	public String getVesnam() {
		return null != vesnam?vesnam.toUpperCase():"";
	}
	public void setVesnam(String vesnam) {
		this.vesnam = null != vesnam?vesnam.toUpperCase():"";
	}
	public String getRouted() {
		return null != routed?routed.toUpperCase():"";
	}
	public void setRouted(String routed) {
		this.routed = null != routed?routed.toUpperCase():"";
	}
	public String getRelate() {
		return null != relate?relate.toUpperCase():"";
	}
	public void setRelate(String relate) {
		this.relate = null != relate?relate.toUpperCase():"";
	}
	public String getWaiver() {
		return null != waiver?waiver.toUpperCase():"";
	}
	public void setWaiver(String waiver) {
		this.waiver = null != waiver?waiver.toUpperCase():"";
	}
	public String getHazard() {
		return null != hazard?hazard.toUpperCase():"";
	}
	public void setHazard(String hazard) {
		this.hazard = null != hazard?hazard.toUpperCase():"";
	}
	public String getVoytrm() {
		return null != voytrm?voytrm.toUpperCase():"";
	}
	public void setVoytrm(String voytrm) {
		this.voytrm = null != voytrm?voytrm.toUpperCase():"";
	}
	public String getVoyprt() {
		return null != voyprt?voyprt.toUpperCase():"";
	}
	public void setVoyprt(String voyprt) {
		this.voyprt = null != voyprt?voyprt.toUpperCase():"";
	}
	public String getVoyvoy() {
		return null != voyvoy?voyvoy.toUpperCase():"";
	}
	public void setVoyvoy(String voyvoy) {
		this.voyvoy = null != voyvoy?voyvoy.toUpperCase():"";
	}
	public String getVoysuf() {
		return null != voysuf?voysuf.toUpperCase():"";
	}
	public void setVoysuf(String voysuf) {
		this.voysuf = null != voysuf?voysuf.toUpperCase():"";
	}
	public String getStatus() {
		return null != status?status.toUpperCase():"";
	}
	public void setStatus(String status) {
		this.status = null != status?status.toUpperCase():"";
	}
	public String getStsusr() {
		return null != stsusr?stsusr.toUpperCase():"";
	}
	public void setStsusr(String stsusr) {
		this.stsusr = null != stsusr?stsusr.toUpperCase():"";
	}
	
	public String getBkgnum() {
		return bkgnum;
	}
	public void setBkgnum(String bkgnum) {
		this.bkgnum = bkgnum;
	}
	public String getBlnum() {
		return blnum;
	}
	public void setBlnum(String blnum) {
		this.blnum = blnum;
	}
	public String getConpoa() {
		return null != conpoa?conpoa.toUpperCase():"";
	}
	public void setConpoa(String conpoa) {
		this.conpoa = null != conpoa?conpoa.toUpperCase():"";
	}
	public String getDomesticOrForeign() {
		return domesticOrForeign;
	}
	public void setDomesticOrForeign(String domesticOrForeign) {
		this.domesticOrForeign = domesticOrForeign;
	}
	public String getScheduleB_Number() {
		return null != scheduleB_Number?scheduleB_Number.toUpperCase():"";
	}
	public void setScheduleB_Number(String scheduleB_Number) {
		this.scheduleB_Number = null != scheduleB_Number?scheduleB_Number.toUpperCase():"";
	}
	public String getScheduleB_Name() {
		return null != scheduleB_Name?scheduleB_Name.toUpperCase():"";
	}
	public void setScheduleB_Name(String scheduleB_Name) {
		this.scheduleB_Name = null != scheduleB_Name?scheduleB_Name.toUpperCase():"";
	}
	public String getDescription1() {
		return null != description1?description1.toUpperCase():"";
	}
	public void setDescription1(String description1) {
		this.description1 = null != description1?description1.toUpperCase():"";
	}
	public String getDescription2() {
		return null != description2?description2.toUpperCase():"";
	}
	public void setDescription2(String description2) {
		this.description2 = null != description2?description2.toUpperCase():"";
	}

	public String getUnits1() {
		return null != units1?units1.toUpperCase():"";
	}
	public void setUnits1(String units1) {
		this.units1 = null != units1?units1.toUpperCase():"";
	}
	public String getUnits2() {
		return null != units2?units2.toUpperCase():"";
	}
	public void setUnits2(String units2) {
		this.units2 = null != units2?units2.toUpperCase():"";
	}
	public Integer getQuantities1() {
		return quantities1;
	}
	public void setQuantities1(Integer quantities1) {
		this.quantities1 = quantities1;
	}
	public Integer getQuantities2() {
		return quantities2;
	}
	public void setQuantities2(Integer quantities2) {
		this.quantities2 = quantities2;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public String getWeightType() {
		return null != weightType?weightType.toUpperCase():"";
	}
	public void setWeightType(String weightType) {
		this.weightType = null != weightType?weightType.toUpperCase():"";
	}
	public String getExportInformationCode() {
		return null != exportInformationCode?exportInformationCode.toUpperCase():"";
	}
	public void setExportInformationCode(String exportInformationCode) {
		this.exportInformationCode = null != exportInformationCode?exportInformationCode.toUpperCase():"";
	}
	public String getLicenseType() {
		return null != licenseType?licenseType.toUpperCase():"";
	}
	public void setLicenseType(String licenseType) {
		this.licenseType = null != licenseType?licenseType.toUpperCase():"";
	}
	public String getUsedVehicle() {
		return null != usedVehicle?usedVehicle.toUpperCase():"";
	}
	public void setUsedVehicle(String usedVehicle) {
		this.usedVehicle = null != usedVehicle?usedVehicle.toUpperCase():"";
	}
	public String getExportLicense() {
		return null != exportLicense?exportLicense.toUpperCase():"";
	}
	public void setExportLicense(String exportLicense) {
		this.exportLicense = null != exportLicense?exportLicense.toUpperCase():"";
	}
	public String getEccn() {
		return null != eccn?eccn.toUpperCase():"";
	}
	public void setEccn(String eccn) {
		this.eccn = null != eccn?eccn.toUpperCase():"";
	}
	public String getVehicleIdType() {
		return null != vehicleIdType?vehicleIdType.toUpperCase():"";
	}
	public void setVehicleIdType(String vehicleIdType) {
		this.vehicleIdType = null != vehicleIdType?vehicleIdType.toUpperCase():"";
	}
	public String getVehicleIdNumber() {
		return null != vehicleIdNumber?vehicleIdNumber.toUpperCase():"";
	}
	public void setVehicleIdNumber(String vehicleIdNumber) {
		this.vehicleIdNumber = null != vehicleIdNumber?vehicleIdNumber.toUpperCase():"";
	}
	public String getVehicleTitleNumber() {
		return null != vehicleTitleNumber?vehicleTitleNumber.toUpperCase():"";
	}
	public void setVehicleTitleNumber(String vehicleTitleNumber) {
		this.vehicleTitleNumber = null != vehicleTitleNumber?vehicleTitleNumber.toUpperCase():"";
	}
	public String getVehicleState() {
		return null != vehicleState?vehicleState.toUpperCase():"";
	}
	public void setVehicleState(String vehicleState) {
		this.vehicleState = null != vehicleState?vehicleState.toUpperCase():"";
	}
	public String getButtonValue() {
		return buttonValue;
	}
	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
	}
	public String getSelectedTab() {
		return selectedTab;
	}
	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}
	public String getShipment() {
		return shipment;
	}
	public void setShipment(String shipment) {
		this.shipment = shipment;
	}
	public String getSchedId() {
		return schedId;
	}
	public void setSchedId(String schedId) {
		this.schedId = schedId;
	}
        public String getConsta() {
            return null != consta?consta.toUpperCase():"";
        }
        public void setConsta(String consta) {
            this.consta = null != consta?consta.toUpperCase():"";
        }

    public String getForcfn() {
        return null != forcfn?forcfn.toUpperCase():"";
    }

    public void setForcfn(String forcfn) {
        this.forcfn = null != forcfn?forcfn.toUpperCase():"";
    }

    public String getForcln() {
        return null != forcln?forcln.toUpperCase():"";
    }

    public void setForcln(String forcln) {
        this.forcln = null != forcln?forcln.toUpperCase():"";
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
        return null != inbent?inbent.toUpperCase():"";
    }

    public void setInbent(String inbent) {
        this.inbent = null != inbent?inbent.toUpperCase():"";
    }

    public String getUnitno() {
        return null != unitno?unitno.toUpperCase():"";
    }

    public void setUnitno(String unitno) {
        this.unitno = null != unitno?unitno.toUpperCase():"";
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

    public String getFrtirsTp() {
        return frtirsTp;
    }

    public void setFrtirsTp(String frtirsTp) {
        this.frtirsTp = frtirsTp;
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
        return null != passportNumber?passportNumber:"";
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = null != passportNumber?passportNumber:"";
    }

    public String getUom1() {
        return uom1;
    }

    public void setUom1(String uom1) {
        this.uom1 = uom1;
    }

    public String getUom2() {
        return uom2;
    }

    public void setUom2(String uom2) {
        this.uom2 = uom2;
    }

    public String getCONTYP() {
        return CONTYP;
    }

    public void setCONTYP(String CONTYP) {
        this.CONTYP = CONTYP;
    }

    public String getTotalLicenseValue() {
        return totalLicenseValue;
    }

    public void setTotalLicenseValue(String totalLicenseValue) {
        this.totalLicenseValue = totalLicenseValue;
    }
        
        public void setShipperDetails(String shipperNo) throws Exception{
            if(CommonFunctions.isNotNull(shipperNo)){
                CustAddress custAddress = new CustAddressBC().getCustInfoForCustNo(shipperNo);
                GeneralInformation generalInformation = new GeneralInformationDAO().getGeneralInformationByAccountNumber(shipperNo);
                 if(null != custAddress){
                    this.expnam = custAddress.getAcctName();
                    this.expnum = custAddress.getAcctNo();
                    this.expadd = custAddress.getAddress1();
                    this.expcty = custAddress.getCity1();
                    this.expsta = custAddress.getState();
                    this.expzip = custAddress.getZip();
                    this.expcfn = custAddress.getContactName();
                    this.expcpn = null != custAddress.getPhone()?removeSpace(custAddress.getPhone()):"";
                    this.expctry = null != custAddress.getCuntry()?custAddress.getCuntry().getCode():"";
                 }
                 if(null != generalInformation){
                     if(CommonUtils.isNotEmpty(generalInformation.getIdText())){
                        this.expirsTp = "Y";
                        this.expirs = generalInformation.getIdText();
                     }
                    this.expicd = generalInformation.getIdType();
                    this.exppoa = generalInformation.getPoa();
                 }
            }
        }
        public void setConsigneeDetails(String consigneeNo,String cntdes) throws Exception{
            CustAddress custAddress = new CustAddressBC().getCustInfoForCustNo(consigneeNo);
             GeneralInformation generalInformation = new GeneralInformationDAO().getGeneralInformationByAccountNumber(consigneeNo);
             if(null != custAddress){
                this.connam = custAddress.getAcctName();
                this.connum = custAddress.getAcctNo();
                this.conadd = custAddress.getAddress1();
                this.concty = custAddress.getCity1();
                this.conpst = custAddress.getZip();
                this.concfn = custAddress.getContactName();
                this.concpn = null != custAddress.getPhone()?removeSpace(custAddress.getPhone()):"";
                this.consta = null!=custAddress.getState()?custAddress.getState():cntdes;
                this.conctry = null != custAddress.getCuntry()?custAddress.getCuntry().getCode():"";
             }
             if(null != generalInformation){
                this.conpoa = generalInformation.getPoa();
             }
        }
        public void setForwarderDetails(String forwarderNo) throws Exception{
            CustAddress custAddress = new CustAddressBC().getCustInfoForCustNo(forwarderNo);
            GeneralInformation generalInformation = new GeneralInformationDAO().getGeneralInformationByAccountNumber(forwarderNo);
            if(null != custAddress){
                this.frtnam = custAddress.getAcctName();
                this.frtnum = custAddress.getAcctNo();
                this.frtadd = custAddress.getAddress1();
                this.frtcty = custAddress.getCity1();
                this.frtsta = custAddress.getState();
                this.frtzip = custAddress.getZip();
                this.forctry = null != custAddress.getCuntry()?custAddress.getCuntry().getCode():"";
            }
            if(null != generalInformation){
                if(CommonUtils.isNotEmpty(generalInformation.getIdText())){
                    this.frtirsTp = "Y";
                    this.frtirs = generalInformation.getIdText();
                 }
                this.frticd = generalInformation.getIdType();
            }
        }
        public String getCountryCode(String unlocCode){
            String countryCode = "";
            if(CommonUtils.isNotEmpty(unlocCode)){
                return unlocCode.substring(0, 2);
            }
            return countryCode;
        }
        public String removeSpace(String commonValue){
            if(CommonUtils.isNotEmpty(commonValue)){
//                Pattern pattern = Pattern.compile("\\s+");
//                Matcher matcher = pattern.matcher(commonValue);
//                commonValue = matcher.replaceAll(" ");
                  commonValue = commonValue.replaceAll("\\s+", "");
                  commonValue = commonValue.replaceAll("\\-", "");
                  commonValue = commonValue.replaceAll("\\,", "");
                  commonValue = commonValue.replaceAll("\\(+", "");
                  commonValue = commonValue.replaceAll("\\)+", "");
            }
            return commonValue;
        }
}

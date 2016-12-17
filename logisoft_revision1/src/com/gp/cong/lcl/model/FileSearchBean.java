/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.lcl.model;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import java.io.Serializable;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author lakshh
 */
public class FileSearchBean implements LclCommonConstant, Serializable {
// schedule_no(Bkg voy) ,pol_etd(sail date),pol_lrdt(Load Lrd),poo_lrdt(origin lrd)

    private static final Logger log = Logger.getLogger(FileSearchBean.class);
    final static int ID = 0;
    final static int FILENUMBER = 1;
    final static int FILEID = 2;
    final static int CLIENTPWK = 3;
    final static int STATE = 4;
    final static int STATUS = 5;
    final static int BOOKEDVOYAGE = 6;//in db schedule_no
    final static int SAILDATE = 7;//in db pol_etd
    final static int LOADLRD = 8;//in db pol_lrdt
    final static int ORIGINLRD = 9;//in db poo_lrdt
    final static int QUOTEBOOKTYPE = 10;
    final static int TRANSPOLCODE = 11;//Import Table USA
    final static int TRANSPOLNAME = 12;
    final static int TRANSPOLSTATE = 13;
    final static int TRANSPODCODE = 14;//Import Table Foreign
    final static int TRANSPODNAME = 15;
    final static int TRANSPODCOUNTRY = 16;
    final static int PODNAME = 17;
    final static int PODSTATE = 18;
    final static int PODUNLOCCODE = 19;
    final static int ORIGINNAME = 20;
    final static int ORIGINSTATE = 21;
    final static int ORIGINUNLOCCODE = 22;
    final static int POLNAME = 23;
    final static int POLSTATE = 24;
    final static int POLUNLOCCODE = 25;
    final static int FDNAME = 26;
    final static int FDSTATE = 27;
    final static int FDUNLOCCODE = 28;
    final static int SHIPNAME = 29;
    final static int SHIPACCT = 30;
    final static int SHIPADD = 31;
    final static int SHIPCITY = 32;
    final static int SHIPSTATE = 33;
    final static int SHIPZIP = 34;
    final static int FWDNAME = 35;
    final static int FWDACCT = 36;
    final static int FWDADD = 37;
    final static int FWDCITY = 38;
    final static int FWDSTATE = 39;
    final static int FWDZIP = 40;
    final static int CONSNAME = 41;
    final static int CONSACCT = 42;
    final static int CONSADD = 43;
    final static int CONSCITY = 44;
    final static int CONSSTATE = 45;
    final static int CONSZIP = 46;
    final static int BILLTM = 47;
    final static int BOOKEDBY = 48;
    final static int QUOTEBY = 49;
    final static int POOPICKUP = 50;
    final static int PICKUPCITY = 51;
    final static int CONSOLIDATEDFILES = 52;
    final static int CREDIT_S = 53;
    final static int CREDIT_F = 54;
    final static int CREDIT_T = 55;
    final static int BILLCODE = 56;
    final static int RELAYOVERIDE = 57;
    final static int ORIGINCOUNTRY = 58;
    final static int POLCOUNTRY = 59;
    final static int PODCOUNTRY = 60;
    final static int FDCOUNTRY = 61;
    final static int CURRENTLOCATION = 62;
    final static int QUOTECOMPLETE = 63;
    final static int TOTALPIECE = 78;
    final static int TOTALWEIGHTIMPERIAL = 79;
    final static int TOTALVOLUMEIMPERIAL = 80;
    final static int TOTALWEIGHTMETRIC = 81;
    final static int TOTALVOLUMEMETRIC = 81;
    final static int TOTALBOOKEDWEIGHT = 82;
    final static int TOTALACTUALWEIGHT = 83;
    final static int TOTALBOOKEDMEASURE = 84;
    final static int TOTALACTUALMEASURE = 85;
    final static int TOTALBOOKEDWEIGHTMETRIC = 86;
    final static int TOTALACTUALVOLUMEMETRIC = 87;
    final static int TOTALACTUALWEIGHTMETRIC = 88;
    final static int TOTALBOOKEDVOLUMEMETRIC = 89;
    final static int TOTALBOOKEDPIECE = 90;
    final static int TOTALACTUALPIECE = 91;
    final static int HAZMAT = 92;
    final static int BILLTERMNO = 93;
    final static int PICKUPDATE = 94;
    final static int ETADATE = 95;
    final static int DISPOCODE = 96;
    final static int DISPODESC = 97;
    final static int DISP = 98;
    final static int CURRENTLOCATIONNAME = 99;
    final static int CURRENTLOCATIONSTATECODE = 100;
    final static int POSTEDBYUSERID = 101;
    final static int IMPORTTRANSHIPMENT = 102;
    final static int SHORTSHIP = 103;
    final static int PIECEUNIT = 104;
    final static int WAREHOUSE = 105;
     final static int DATASOURCE = 106;
    final static int HOTCODES = 107;
    final static int CARGORECEIVEDDATE = 108;
    final static int AESSTATUS = 109;
    final static int SEDCOUNT = 110;
    private String id;
    private String fileNumber;
    private Boolean shortShip;
    private String fileId;
    private String state;
    private String status;
    private String bookedVoyage;//in db schedule_no
    private Date sailDate;//in db pol_etd
    private Date loadLrd;//in db pol_lrdt
    private Date originLrd;//in db poo_lrdt
    private String bookingType;
    private String transPolCode;
    private String transPolName;
    private String transPolState;
    private String transPodCode;
    private String transPodName;
    private String transPodCountry;
    private String podName;
    private String podState;
    private String podCountry;
    private String podUnLocCode;
    private String originName;
    private String originState;
    private String originCountry;
    private String originUnLocCode;
    private String polName;
    private String polState;
    private String polCountry;
    private String polUnLocCode;
    private String fdName;
    private String fdState;
    private String fdCountry;
    private String fdUnLocCode;
    private String shipName;
    private String shipAcct;
    private String shipAdd;
    private String shipCity;
    private String shipState;
    private String shipZip;
    private String fwdName;
    private String credit_t;
    private String credit_s;
    private String credit_f;
    private String fwdAcct;
    private String fwdAdd;
    private String fwdCity;
    private String fwdState;
    private String fwdZip;
    private String consName;
    private String consAcct;
    private String consAdd;
    private String consCity;
    private String consState;
    private String consZip;
    private String totalBookedPiece;
    private String billcode;
    private Boolean hazmat;
    private Boolean relayOveride;
    private String totalActualPiece;
    private String totalPiece;
    private Integer sedCount;
    private Double totalBookedWeight;
    private Double totalWeightImperial;
    private Double totalActualWeight;
    private Double totalBookedMeasure;
    private Double totalVolumeImperial;
    private Double totalActualMeasure;
    private Double totalBookedWeightMetric;
    private Double totalWeightMetric;
    private Double totalActualWeightMetric;
    private Double totalBookedVolumeMetric;
    private Double totalVolumeMetric;
    private Double totalActualVolumeMetric;
    private String hotCodes;
    private String billTm;
    private String bookedBy;
    private String quoteBy;
    private Boolean pooPickup;
    private String pickupCity;
    private String consolidatedFiles;
    private String clientPwk;
    private Boolean totalMeasureFlag;
    private Boolean totalWeightFlag;
    private String hotCodeKey = "";
    private Boolean totalWeightMetricFlag;
    private Boolean totalVolumeMetricFlag;
    private Date cargoReceivedDate;
    private String currentLocation;
    private String aesStatus;
    private Boolean quoteComplete;
    private String billTermNo;
    private Date pickupDate;
    private Date etaDate;
    private String dispoCode;
    private String pieceUnit;
    private String dispoDesc;
    private String dispo;
    private String currentLocationName;
    private String currentLocationStatecode;
    private Integer postedByUserId;
    private Boolean importTranshipment;
    private String datasource;
    private String currentLocWareHouse;
    
    public FileSearchBean() {
    }

    // public FileSearchBean(Object[] obj, Map<String, String> originList, Map<String, String> polList, Map<String, String> podList, Map<String, String> destList, LclSearchForm lclSearchForm) throws Exception {
    public FileSearchBean(Object[] obj, String moduleName) throws Exception {
        id = null == obj[ID] ? null : obj[ID].toString();
        fileNumber = null == obj[FILENUMBER] ? null : obj[FILENUMBER].toString();
        fileId = null == obj[FILEID] ? null : obj[FILEID].toString();
        clientPwk = null == obj[CLIENTPWK] ? null : obj[CLIENTPWK].toString();
        state = null == obj[STATE] ? null : obj[STATE].toString();
        status = null == obj[STATUS] ? null : obj[STATUS].toString();
        bookedVoyage = null == obj[BOOKEDVOYAGE] ? null : obj[BOOKEDVOYAGE].toString();
        String strSailDate = null == obj[SAILDATE] ? null : obj[SAILDATE].toString();
        if (null != strSailDate && !strSailDate.equals("0")) {
            sailDate = DateUtils.parseStringToDateWithTime(strSailDate);
        }
        String strLoadLrd = null == obj[LOADLRD] ? null : obj[LOADLRD].toString();
        if (null != strLoadLrd && !strLoadLrd.equals("0")) {
            loadLrd = DateUtils.parseStringToDateWithTime(strLoadLrd);
        }
        String strOriginLrd = null == obj[ORIGINLRD] ? null : obj[ORIGINLRD].toString();
        if (null != strOriginLrd && !strOriginLrd.equals("0")) {
            originLrd = DateUtils.parseStringToDateWithTime(strOriginLrd);
        }
        bookingType = null == obj[QUOTEBOOKTYPE] ? null : obj[QUOTEBOOKTYPE].toString();
        if (LCL_TRANSHIPMENT_TYPE.equalsIgnoreCase(bookingType) && LCL_EXPORT.equalsIgnoreCase(moduleName)) {
            podName = null == obj[TRANSPODNAME] ? null : obj[TRANSPODNAME].toString();
            podState = null == obj[TRANSPODCOUNTRY] ? null : obj[TRANSPODCOUNTRY].toString();
            podUnLocCode = null == obj[TRANSPODCODE] ? null : obj[TRANSPODCODE].toString();
        } else {
            podName = null == obj[PODNAME] ? null : obj[PODNAME].toString();
            podState = null == obj[PODSTATE] ? null : obj[PODSTATE].toString();
            podUnLocCode = null == obj[PODUNLOCCODE] ? null : obj[PODUNLOCCODE].toString();
        }
        if (LCL_TRANSHIPMENT_TYPE.equalsIgnoreCase(bookingType) && LCL_EXPORT.equalsIgnoreCase(moduleName)) {
            originName = null == obj[PODNAME] ? null : obj[PODNAME].toString();
            originState = null == obj[PODSTATE] ? null : obj[PODSTATE].toString();
            originUnLocCode = null == obj[PODUNLOCCODE] ? null : obj[PODUNLOCCODE].toString();
        } else {
            originName = null == obj[ORIGINNAME] ? null : obj[ORIGINNAME].toString();
            originState = null == obj[ORIGINSTATE] ? null : obj[ORIGINSTATE].toString();
            originUnLocCode = null == obj[ORIGINUNLOCCODE] ? null : obj[ORIGINUNLOCCODE].toString();
        }
        if (LCL_TRANSHIPMENT_TYPE.equalsIgnoreCase(bookingType) && LCL_EXPORT.equalsIgnoreCase(moduleName)) {
            polUnLocCode = null == obj[TRANSPOLCODE] ? null : obj[TRANSPOLCODE].toString();
            polName = null == obj[TRANSPOLNAME] ? null : obj[TRANSPOLNAME].toString();
            polState = null == obj[TRANSPOLSTATE] ? null : obj[TRANSPOLSTATE].toString();
            transPolCode = null == obj[POLUNLOCCODE] ? null : obj[POLUNLOCCODE].toString();
            transPolState = null == obj[POLSTATE] ? null : obj[POLSTATE].toString();
            transPolName = null == obj[POLNAME] ? null : obj[POLNAME].toString();
        } else {
            polName = null == obj[POLNAME] ? null : obj[POLNAME].toString();
            polState = null == obj[POLSTATE] ? null : obj[POLSTATE].toString();
            polUnLocCode = null == obj[POLUNLOCCODE] ? null : obj[POLUNLOCCODE].toString();
        }
        fdName = null == obj[FDNAME] ? null : obj[FDNAME].toString();
        fdState = null == obj[FDSTATE] ? null : obj[FDSTATE].toString();
        fdUnLocCode = null == obj[FDUNLOCCODE] ? null : obj[FDUNLOCCODE].toString();
        shipName = null == obj[SHIPNAME] ? null : obj[SHIPNAME].toString();
        shipAcct = null == obj[SHIPACCT] ? null : obj[SHIPACCT].toString();
        shipAdd = null == obj[SHIPADD] ? null : obj[SHIPADD].toString();
        shipCity = null == obj[SHIPCITY] ? null : obj[SHIPCITY].toString();
        shipState = null == obj[SHIPSTATE] ? null : obj[SHIPSTATE].toString();
        shipZip = null == obj[SHIPZIP] ? null : obj[SHIPZIP].toString();
        fwdName = null == obj[FWDNAME] ? null : obj[FWDNAME].toString();
        fwdAcct = null == obj[FWDACCT] ? null : obj[FWDACCT].toString();
        fwdAdd = null == obj[FWDADD] ? null : obj[FWDADD].toString();
        fwdCity = null == obj[FWDCITY] ? null : obj[FWDCITY].toString();
        fwdState = null == obj[FWDADD] ? null : obj[FWDSTATE].toString();
        fwdZip = null == obj[FWDZIP] ? null : obj[FWDZIP].toString();
        consName = null == obj[CONSNAME] ? null : obj[CONSNAME].toString();
        consAcct = null == obj[CONSACCT] ? null : obj[CONSACCT].toString();
        consAdd = null == obj[CONSADD] ? null : obj[CONSADD].toString();
        consCity = null == obj[CONSCITY] ? null : obj[CONSCITY].toString();
        consState = null == obj[CONSSTATE] ? null : obj[CONSSTATE].toString();
        consZip = null == obj[CONSZIP] ? null : obj[CONSZIP].toString();
        totalPiece = null == obj[TOTALPIECE] ? null : obj[TOTALPIECE].toString();
        totalWeightImperial = null == obj[TOTALWEIGHTIMPERIAL] ? null : Double.parseDouble(obj[TOTALWEIGHTIMPERIAL].toString());
        totalVolumeImperial = null == obj[TOTALVOLUMEIMPERIAL] ? null : Double.parseDouble(obj[TOTALVOLUMEIMPERIAL].toString());
        totalWeightMetric = null == obj[TOTALWEIGHTMETRIC] ? null : Double.parseDouble(obj[TOTALWEIGHTMETRIC].toString());
        totalVolumeMetric = null == obj[TOTALVOLUMEMETRIC] ? null : Double.parseDouble(obj[TOTALVOLUMEMETRIC].toString());
        totalBookedWeight = null == obj[TOTALBOOKEDWEIGHT] ? null : Double.parseDouble(obj[TOTALBOOKEDWEIGHT].toString());
        totalActualWeight = null == obj[TOTALACTUALWEIGHT] ? null : Double.parseDouble(obj[TOTALACTUALWEIGHT].toString());
        totalBookedMeasure = null == obj[TOTALBOOKEDMEASURE] ? null : Double.parseDouble(obj[TOTALBOOKEDMEASURE].toString());
        totalActualMeasure = null == obj[TOTALACTUALMEASURE] ? null : Double.parseDouble(obj[TOTALACTUALMEASURE].toString());
        totalBookedWeightMetric = null == obj[TOTALBOOKEDWEIGHTMETRIC] ? null : Double.parseDouble(obj[TOTALBOOKEDWEIGHTMETRIC].toString());
        totalActualWeightMetric = null == obj[TOTALACTUALWEIGHTMETRIC] ? null : Double.parseDouble(obj[TOTALACTUALWEIGHTMETRIC].toString());
        totalActualVolumeMetric = null == obj[TOTALACTUALVOLUMEMETRIC] ? null : Double.parseDouble(obj[TOTALACTUALVOLUMEMETRIC].toString());
        totalBookedVolumeMetric = null == obj[TOTALBOOKEDVOLUMEMETRIC] ? null : Double.parseDouble(obj[TOTALBOOKEDVOLUMEMETRIC].toString());
        totalBookedPiece = null == obj[TOTALBOOKEDPIECE] ? null : obj[TOTALBOOKEDPIECE].toString();
        totalActualPiece = null == obj[TOTALACTUALPIECE] ? null : obj[TOTALACTUALPIECE].toString();
        String strHazmat = null == obj[HAZMAT] ? null : obj[HAZMAT].toString();
        if (CommonUtils.isNotEmpty(strHazmat) && (strHazmat.equalsIgnoreCase("1") || "true".equalsIgnoreCase(strHazmat))) {
            hazmat = true;
        }
        billTermNo = null == obj[BILLTERMNO] ? null : obj[BILLTERMNO].toString();
        String strPickupDate = null == obj[PICKUPDATE] ? null : obj[PICKUPDATE].toString();
        if (null != strPickupDate && !strPickupDate.equals("0")) {
            pickupDate = DateUtils.parseStringToDateWithTime(strPickupDate);
        }
        String strEtaDate = null == obj[ETADATE] ? null : obj[ETADATE].toString();
        if (null != strEtaDate && !strEtaDate.equals("0")) {
            etaDate = DateUtils.parseStringToDateWithTime(strEtaDate);
        }
        //Unit Level Disposition Code and Descrip
        dispoCode = null == obj[DISPOCODE] ? null : obj[DISPOCODE].toString();
        dispoDesc = null == obj[DISPODESC] ? null : obj[DISPODESC].toString();
        dispo = null == obj[DISP] ? null : obj[DISP].toString();
        currentLocationName = null == obj[CURRENTLOCATIONNAME] ? null : obj[CURRENTLOCATIONNAME].toString();
        currentLocationStatecode = null == obj[CURRENTLOCATIONSTATECODE] ? null : obj[CURRENTLOCATIONSTATECODE].toString();
        currentLocWareHouse= null == obj[WAREHOUSE] ? null : obj[WAREHOUSE].toString();
        hotCodes = null == obj[HOTCODES] ? "" : obj[HOTCODES].toString();
        String[] hotCodeArr = hotCodes.split("<br>");
        for (int i = 0; i < hotCodeArr.length; i++) {
            if (CommonUtils.isNotEmpty(hotCodeArr[i]) && hotCodeArr[i].contains("/")) {
                hotCodeKey += hotCodeArr[i].substring(0, 3) + ",";
            }
        }
        int occurrence = getCharOccurrence(hotCodeKey);
        if (occurrence > 3) {
            hotCodeKey = hotCodeKey.substring(0, 11);
        }
        String strCargoDate = (null == obj[CARGORECEIVEDDATE] || "" == obj[CARGORECEIVEDDATE]) ? null : obj[CARGORECEIVEDDATE].toString();
        if (null != strCargoDate && !strCargoDate.equals("0")) {
            cargoReceivedDate = DateUtils.parseStringToDateWithTime(strCargoDate);
        }
        billTm = null == obj[BILLTM] ? null : obj[BILLTM].toString();
        bookedBy = null == obj[BOOKEDBY] ? null : obj[BOOKEDBY].toString();
        quoteBy = null == obj[QUOTEBY] ? null : obj[QUOTEBY].toString();
        String strPooPickup = null == obj[POOPICKUP] ? null : obj[POOPICKUP].toString();
        if (CommonUtils.isNotEmpty(strPooPickup) && strPooPickup.equalsIgnoreCase("1")) {
            pooPickup = true;
        } else if ("true".equalsIgnoreCase(strPooPickup)) {
            pooPickup = true;
        }
        pickupCity = null == obj[PICKUPCITY] ? null : obj[PICKUPCITY].toString();
        consolidatedFiles = null == obj[CONSOLIDATEDFILES] ? null : obj[CONSOLIDATEDFILES].toString();
        credit_s = null == obj[CREDIT_S] ? null : obj[CREDIT_S].toString();
        credit_f = null == obj[CREDIT_F] ? null : obj[CREDIT_F].toString();
        credit_t = null == obj[CREDIT_T] ? null : obj[CREDIT_T].toString();
        billcode = null == obj[BILLCODE] ? null : obj[BILLCODE].toString();
        String relayOverride = null == obj[RELAYOVERIDE] ? null : obj[RELAYOVERIDE].toString();
        if (CommonUtils.isNotEmpty(relayOverride) && (relayOverride.equalsIgnoreCase("1") || "true".equalsIgnoreCase(relayOverride))) {
            relayOveride = true;
        }
        originCountry = null == obj[ORIGINCOUNTRY] ? null : obj[ORIGINCOUNTRY].toString();
        polCountry = null == obj[POLCOUNTRY] ? null : obj[POLCOUNTRY].toString();
        if (LCL_TRANSHIPMENT_TYPE.equalsIgnoreCase(bookingType) && LCL_EXPORT.equalsIgnoreCase(moduleName)) {
            podCountry = null == obj[TRANSPODCOUNTRY] ? null : obj[TRANSPODCOUNTRY].toString();
        } else {
            podCountry = null == obj[PODCOUNTRY] ? null : obj[PODCOUNTRY].toString();
        }
        fdCountry = null == obj[FDCOUNTRY] ? null : obj[FDCOUNTRY].toString();
        postedByUserId = null == obj[POSTEDBYUSERID] ? null : Integer.parseInt(obj[POSTEDBYUSERID].toString());
        currentLocation = null == obj[CURRENTLOCATION] ? null : obj[CURRENTLOCATION].toString();
        aesStatus = null == obj[AESSTATUS] ? null : obj[AESSTATUS].toString();
        sedCount = null == obj[SEDCOUNT] ? null : Integer.parseInt(obj[SEDCOUNT].toString());
        String qComplete = null == obj[QUOTECOMPLETE] ? null : obj[QUOTECOMPLETE].toString();
        if (obj[QUOTECOMPLETE] instanceof Boolean) {
            quoteComplete = new Boolean(qComplete);
        } else if (CommonUtils.isNotEmpty(qComplete) && qComplete.equalsIgnoreCase("1")) {
            quoteComplete = true;
        }
        String transhipment = null == obj[IMPORTTRANSHIPMENT] ? null : obj[IMPORTTRANSHIPMENT].toString();
        if (CommonUtils.isNotEmpty(transhipment) && (transhipment.equalsIgnoreCase("1") || "true".equalsIgnoreCase(strPooPickup))) {
            importTranshipment = true;
        } else {
            importTranshipment = false;
        }
        shortShip = null == obj[SHORTSHIP] ? null : Boolean.parseBoolean(obj[SHORTSHIP].toString());
        String strShortShip = null == obj[SHORTSHIP] ? null : obj[SHORTSHIP].toString();
        if (CommonUtils.isNotEmpty(strShortShip) && (strShortShip.equalsIgnoreCase("1") || "true".equalsIgnoreCase(strShortShip))) {
            shortShip = true;
        }
        pieceUnit = null == obj[PIECEUNIT] ? null : obj[PIECEUNIT].toString();
        Double tempBookedWeight;
        Double tempBookedMeasure;
        Double tempActualWeight;
        Double tempActualMeasure;
        Double tempBookedWeightMetric;
        Double tempBookedVolumeMetric;
        Double tempActualWeightMetric;
        Double tempActualVolumeMetric;
        if (totalBookedWeight == null) {
            tempBookedWeight = 1.000;
        } else {
            tempBookedWeight = totalBookedWeight;
        }
        if (totalBookedMeasure == null) {
            tempBookedMeasure = 1.000;
        } else {
            tempBookedMeasure = totalBookedMeasure;
        }
        if (totalActualWeight == null) {
            tempActualWeight = 1.000;
        } else {
            tempActualWeight = totalActualWeight;
        }
        if (totalActualMeasure == null) {
            tempActualMeasure = 1.000;
        } else {
            tempActualMeasure = totalActualMeasure;
        }
        Double totalWeight = ((tempActualWeight * 100) / tempBookedWeight) - 100;
        if (totalWeight > 10 || totalWeight < -10) {
            totalWeightFlag = true;
        }
        Double totalMeasure = ((tempActualMeasure * 100) / tempBookedMeasure) - 100;
        if (totalMeasure > 10 || totalMeasure < -10) {
            totalMeasureFlag = true;
        }
        if (totalBookedWeightMetric == null) {
            tempBookedWeightMetric = 1.000;
        } else {
            tempBookedWeightMetric = totalBookedWeightMetric;
        }
        if (totalBookedVolumeMetric == null) {
            tempBookedVolumeMetric = 1.000;
        } else {
            tempBookedVolumeMetric = totalBookedVolumeMetric;
        }
        if (totalActualWeightMetric == null) {
            tempActualWeightMetric = 1.000;
        } else {
            tempActualWeightMetric = totalActualWeightMetric;
        }
        if (totalActualVolumeMetric == null) {
            tempActualVolumeMetric = 1.000;
        } else {
            tempActualVolumeMetric = totalActualVolumeMetric;
        }
        Double totalWeightMetric = ((tempActualWeightMetric * 100) / tempBookedWeightMetric) - 100;
        if (totalWeightMetric > 10 || totalWeightMetric < -10) {
            totalWeightMetricFlag = true;
        }
        Double totalVolumeMetric = ((tempActualVolumeMetric * 100) / tempBookedVolumeMetric) - 100;
        if (totalVolumeMetric > 10 || totalVolumeMetric < -10) {
            totalVolumeMetricFlag = true;
        }
        if (podCountry != null && podCountry.equalsIgnoreCase("UNITED STATES")) {
            podCountry = podState;
        }
        if (fdCountry != null && fdCountry.equalsIgnoreCase("UNITED STATES")) {
            fdCountry = fdState;
        }
        if (originState == null) {
            originState = originCountry;
        }
        if (polState == null) {
            polState = polCountry;
        }
        if(null != obj[DATASOURCE]){
            datasource = obj[DATASOURCE].toString();
        }
    }

    public String getBookedVoyage() {
        return bookedVoyage;
    }

    public void setBookedVoyage(String bookedVoyage) {
        this.bookedVoyage = bookedVoyage;
    }

    public String getConsAcct() {
        return consAcct;
    }

    public void setConsAcct(String consAcct) {
        this.consAcct = consAcct;
    }

    public String getConsName() {
        return consName;
    }

    public void setConsName(String consName) {
        this.consName = consName;
    }

    public String getFdName() {
        return fdName;
    }

    public void setFdName(String fdName) {
        this.fdName = fdName;
    }

    public Boolean getHazmat() {
        return hazmat;
    }

    public void setHazmat(Boolean hazmat) {
        this.hazmat = hazmat;
    }

    public Boolean getRelayOveride() {
        return relayOveride;
    }

    public void setRelayOveride(Boolean relayOveride) {
        this.relayOveride = relayOveride;
    }

    public String getFdState() {
        return fdState;
    }

    public void setFdState(String fdState) {
        this.fdState = fdState;
    }

    public String getFdCountry() {
        return fdCountry;
    }

    public void setFdCountry(String fdCountry) {
        this.fdCountry = fdCountry;
    }

    public String getBillcode() {
        return billcode;
    }

    public void setBillcode(String billcode) {
        this.billcode = billcode;
    }

    public String getFdUnLocCode() {
        return fdUnLocCode;
    }

    public void setFdUnLocCode(String fdUnLocCode) {
        this.fdUnLocCode = fdUnLocCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public Boolean getShortShip() {
        return shortShip;
    }

    public void setShortShip(Boolean shortShip) {
        this.shortShip = shortShip;
    }

    public String getFwdAcct() {
        return fwdAcct;
    }

    public void setFwdAcct(String fwdAcct) {
        this.fwdAcct = fwdAcct;
    }

    public String getFwdName() {
        return fwdName;
    }

    public void setFwdName(String fwdName) {
        this.fwdName = fwdName;
    }

    public String getConsAdd() {
        return consAdd;
    }

    public void setConsAdd(String consAdd) {
        this.consAdd = consAdd;
    }

    public String getFwdAdd() {
        return fwdAdd;
    }

    public void setFwdAdd(String fwdAdd) {
        this.fwdAdd = fwdAdd;
    }

    public String getConsCity() {
        return consCity;
    }

    public void setConsCity(String consCity) {
        this.consCity = consCity;
    }

    public String getConsState() {
        return consState;
    }

    public void setConsState(String consState) {
        this.consState = consState;
    }

    public String getConsZip() {
        return consZip;
    }

    public void setConsZip(String consZip) {
        this.consZip = consZip;
    }

    public String getFwdCity() {
        return fwdCity;
    }

    public void setFwdCity(String fwdCity) {
        this.fwdCity = fwdCity;
    }

    public String getFwdState() {
        return fwdState;
    }

    public void setFwdState(String fwdState) {
        this.fwdState = fwdState;
    }

    public String getFwdZip() {
        return fwdZip;
    }

    public void setFwdZip(String fwdZip) {
        this.fwdZip = fwdZip;
    }

    public String getShipCity() {
        return shipCity;
    }

    public void setShipCity(String shipCity) {
        this.shipCity = shipCity;
    }

    public String getShipState() {
        return shipState;
    }

    public void setShipState(String shipState) {
        this.shipState = shipState;
    }

    public String getShipZip() {
        return shipZip;
    }

    public void setShipZip(String shipZip) {
        this.shipZip = shipZip;
    }

    public String getHotCodes() {
        return hotCodes;
    }

    public void setHotCodes(String hotCodes) {
        this.hotCodes = hotCodes;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getOriginState() {
        return originState;
    }

    public void setOriginState(String originState) {
        this.originState = originState;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public String getOriginUnLocCode() {
        return originUnLocCode;
    }

    public void setOriginUnLocCode(String originUnLocCode) {
        this.originUnLocCode = originUnLocCode;
    }

    public String getPodName() {
        return podName;
    }

    public void setPodName(String podName) {
        this.podName = podName;
    }

    public String getPodState() {
        return podState;
    }

    public void setPodState(String podState) {
        this.podState = podState;
    }

    public String getPodCountry() {
        return podCountry;
    }

    public void setPodCountry(String podCountry) {
        this.podCountry = podCountry;
    }

    public String getPodUnLocCode() {
        return podUnLocCode;
    }

    public void setPodUnLocCode(String podUnLocCode) {
        this.podUnLocCode = podUnLocCode;
    }

    public String getPolName() {
        return polName;
    }

    public void setPolName(String polName) {
        this.polName = polName;
    }

    public String getPolState() {
        return polState;
    }

    public void setPolState(String polState) {
        this.polState = polState;
    }

    public String getPolCountry() {
        return polCountry;
    }

    public void setPolCountry(String polCountry) {
        this.polCountry = polCountry;
    }

    public String getPolUnLocCode() {
        return polUnLocCode;
    }

    public void setPolUnLocCode(String polUnLocCode) {
        this.polUnLocCode = polUnLocCode;
    }

    public Date getLoadLrd() {
        return loadLrd;
    }

    public void setLoadLrd(Date loadLrd) {
        this.loadLrd = loadLrd;
    }

    public Date getOriginLrd() {
        return originLrd;
    }

    public void setOriginLrd(Date originLrd) {
        this.originLrd = originLrd;
    }

    public Date getSailDate() {
        return sailDate;
    }

    public void setSailDate(Date sailDate) {
        this.sailDate = sailDate;
    }

    public String getShipAcct() {
        return shipAcct;
    }

    public void setShipAcct(String shipAcct) {
        this.shipAcct = shipAcct;
    }

    public String getShipAdd() {
        return shipAdd;
    }

    public void setShipAdd(String shipAdd) {
        this.shipAdd = shipAdd;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalActualPiece() {
        return totalActualPiece;
    }

    public void setTotalActualPiece(String totalActualPiece) {
        this.totalActualPiece = totalActualPiece;
    }

    public String getTotalBookedPiece() {
        return totalBookedPiece;
    }

    public void setTotalBookedPiece(String totalBookedPiece) {
        this.totalBookedPiece = totalBookedPiece;
    }

    public String getBillTm() {
        return billTm;
    }

    public void setBillTm(String billTm) {
        this.billTm = billTm;
    }

    public String getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(String bookedBy) {
        this.bookedBy = bookedBy;
    }

    public String getQuoteBy() {
        return quoteBy;
    }

    public void setQuoteBy(String quoteBy) {
        this.quoteBy = quoteBy;
    }

    public String getPickupCity() {
        return pickupCity;
    }

    public void setPickupCity(String pickupCity) {
        this.pickupCity = pickupCity;
    }

    public Boolean getPooPickup() {
        return pooPickup;
    }

    public void setPooPickup(Boolean pooPickup) {
        this.pooPickup = pooPickup;
    }

    public String getConsolidatedFiles() {
        return consolidatedFiles;
    }

    public void setConsolidatedFiles(String consolidatedFiles) {
        this.consolidatedFiles = consolidatedFiles;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getClientPwk() {
        return clientPwk;
    }

    public void setClientPwk(String clientPwk) {
        this.clientPwk = clientPwk;
    }

    public Double getTotalActualMeasure() {
        return totalActualMeasure;
    }

    public void setTotalActualMeasure(Double totalActualMeasure) {
        this.totalActualMeasure = totalActualMeasure;
    }

    public Double getTotalActualWeight() {
        return totalActualWeight;
    }

    public void setTotalActualWeight(Double totalActualWeight) {
        this.totalActualWeight = totalActualWeight;
    }

    public Double getTotalBookedMeasure() {
        return totalBookedMeasure;
    }

    public void setTotalBookedMeasure(Double totalBookedMeasure) {
        this.totalBookedMeasure = totalBookedMeasure;
    }

    public Double getTotalBookedWeight() {
        return totalBookedWeight;
    }

    public void setTotalBookedWeight(Double totalBookedWeight) {
        this.totalBookedWeight = totalBookedWeight;
    }

    public Boolean getTotalMeasureFlag() {
        return totalMeasureFlag;
    }

    public void setTotalMeasureFlag(Boolean totalMeasureFlag) {
        this.totalMeasureFlag = totalMeasureFlag;
    }

    public Boolean getTotalWeightFlag() {
        return totalWeightFlag;
    }

    public void setTotalWeightFlag(Boolean totalWeightFlag) {
        this.totalWeightFlag = totalWeightFlag;
    }

    public String getHotCodeKey() {
        return hotCodeKey;
    }

    public void setHotCodeKey(String hotCodeKey) {
        this.hotCodeKey = hotCodeKey;
    }

    public String getCredit_f() {
        return credit_f;
    }

    public void setCredit_f(String credit_f) {
        this.credit_f = credit_f;
    }

    public String getCredit_s() {
        return credit_s;
    }

    public void setCredit_s(String credit_s) {
        this.credit_s = credit_s;
    }

    public String getCredit_t() {
        return credit_t;
    }

    public void setCredit_t(String credit_t) {
        this.credit_t = credit_t;
    }

    private int getCharOccurrence(String str) {
        int count = 0;
        char[] ch = str.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            if (("" + ch[i]).equalsIgnoreCase(",")) {
                count++;
            }
        }
        return count;
    }

    public Date getCargoReceivedDate() {
        return cargoReceivedDate;
    }

    public void setCargoReceivedDate(Date cargoReceivedDate) {
        this.cargoReceivedDate = cargoReceivedDate;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Double getTotalActualVolumeMetric() {
        return totalActualVolumeMetric;
    }

    public void setTotalActualVolumeMetric(Double totalActualVolumeMetric) {
        this.totalActualVolumeMetric = totalActualVolumeMetric;
    }

    public Double getTotalActualWeightMetric() {
        return totalActualWeightMetric;
    }

    public void setTotalActualWeightMetric(Double totalActualWeightMetric) {
        this.totalActualWeightMetric = totalActualWeightMetric;
    }

    public Double getTotalBookedVolumeMetric() {
        return totalBookedVolumeMetric;
    }

    public void setTotalBookedVolumeMetric(Double totalBookedVolumeMetric) {
        this.totalBookedVolumeMetric = totalBookedVolumeMetric;
    }

    public Double getTotalBookedWeightMetric() {
        return totalBookedWeightMetric;
    }

    public void setTotalBookedWeightMetric(Double totalBookedWeightMetric) {
        this.totalBookedWeightMetric = totalBookedWeightMetric;
    }

    public Boolean getTotalVolumeMetricFlag() {
        return totalVolumeMetricFlag;
    }

    public void setTotalVolumeMetricFlag(Boolean totalVolumeMetricFlag) {
        this.totalVolumeMetricFlag = totalVolumeMetricFlag;
    }

    public Boolean getTotalWeightMetricFlag() {
        return totalWeightMetricFlag;
    }

    public void setTotalWeightMetricFlag(Boolean totalWeightMetricFlag) {
        this.totalWeightMetricFlag = totalWeightMetricFlag;
    }

    public String getTotalPiece() {
        return totalPiece;
    }

    public void setTotalPiece(String totalPiece) {
        this.totalPiece = totalPiece;
    }

    public Double getTotalVolumeImperial() {
        return totalVolumeImperial;
    }

    public void setTotalVolumeImperial(Double totalVolumeImperial) {
        this.totalVolumeImperial = totalVolumeImperial;
    }

    public Double getTotalVolumeMetric() {
        return totalVolumeMetric;
    }

    public void setTotalVolumeMetric(Double totalVolumeMetric) {
        this.totalVolumeMetric = totalVolumeMetric;
    }

    public Double getTotalWeightImperial() {
        return totalWeightImperial;
    }

    public void setTotalWeightImperial(Double totalWeightImperial) {
        this.totalWeightImperial = totalWeightImperial;
    }

    public Double getTotalWeightMetric() {
        return totalWeightMetric;
    }

    public void setTotalWeightMetric(Double totalWeightMetric) {
        this.totalWeightMetric = totalWeightMetric;
    }

    public String getAesStatus() {
        return aesStatus;
    }

    public void setAesStatus(String aesStatus) {
        this.aesStatus = aesStatus;
    }

    public Integer getSedCount() {
        return sedCount;
    }

    public void setSedCount(Integer sedCount) {
        this.sedCount = sedCount;
    }

    public Boolean getQuoteComplete() {
        return quoteComplete;
    }

    public void setQuoteComplete(Boolean quoteComplete) {
        this.quoteComplete = quoteComplete;
    }

    public String getBillTermNo() {
        return billTermNo;
    }

    public void setBillTermNo(String billTermNo) {
        this.billTermNo = billTermNo;
    }

    public String getDispoCode() {
        return dispoCode;
    }

    public void setDispoCode(String dispoCode) {
        this.dispoCode = dispoCode;
    }

    public String getDispoDesc() {
        return dispoDesc;
    }

    public void setDispoDesc(String dispoDesc) {
        this.dispoDesc = dispoDesc;
    }

    public String getDispo() {
        return dispo;
    }

    public void setDispo(String dispo) {
        this.dispo = dispo;
    }

    public Date getEtaDate() {
        return etaDate;
    }

    public void setEtaDate(Date etaDate) {
        this.etaDate = etaDate;
    }

    public Date getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(Date pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getCurrentLocationName() {
        return currentLocationName;
    }

    public void setCurrentLocationName(String currentLocationName) {
        this.currentLocationName = currentLocationName;
    }

    public String getCurrentLocationStatecode() {
        return currentLocationStatecode;
    }

    public void setCurrentLocationStatecode(String currentLocationStatecode) {
        this.currentLocationStatecode = currentLocationStatecode;
    }

    public Integer getPostedByUserId() {
        return postedByUserId;
    }

    public void setPostedByUserId(Integer postedByUserId) {
        this.postedByUserId = postedByUserId;
    }

    public Boolean getImportTranshipment() {
        return importTranshipment;
    }

    public void setImportTranshipment(Boolean importTranshipment) {
        this.importTranshipment = importTranshipment;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public String getTransPodCode() {
        return transPodCode;
    }

    public void setTransPodCode(String transPodCode) {
        this.transPodCode = transPodCode;
    }

    public String getTransPodCountry() {
        return transPodCountry;
    }

    public void setTransPodCountry(String transPodCountry) {
        this.transPodCountry = transPodCountry;
    }

    public String getTransPodName() {
        return transPodName;
    }

    public void setTransPodName(String transPodName) {
        this.transPodName = transPodName;
    }

    public String getTransPolCode() {
        return transPolCode;
    }

    public void setTransPolCode(String transPolCode) {
        this.transPolCode = transPolCode;
    }

    public String getTransPolName() {
        return transPolName;
    }

    public void setTransPolName(String transPolName) {
        this.transPolName = transPolName;
    }

    public String getTransPolState() {
        return transPolState;
    }

    public void setTransPolState(String transPolState) {
        this.transPolState = transPolState;
    }

    public String getPieceUnit() {
        return pieceUnit;
    }

    public void setPieceUnit(String pieceUnit) {
        this.pieceUnit = pieceUnit;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public String getCurrentLocWareHouse() {
        return currentLocWareHouse;
    }

    public void setCurrentLocWareHouse(String currentLocWareHouse) {
        this.currentLocWareHouse = currentLocWareHouse;
    }

}

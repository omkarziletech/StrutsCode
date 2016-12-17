package com.gp.cvst.logisoft.domain;

import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.fcl.FclBlConstants;
import com.gp.cong.logisoft.util.CommonFunctions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.gp.cong.logisoft.util.DBUtil;
import com.gp.cvst.logisoft.struts.form.SearchQuotationForm;
import java.io.Serializable;

public class FileNumberForQuotaionBLBooking implements Serializable, Comparator<FileNumberForQuotaionBLBooking> {

    public static final String FILE_NO = "q.file_no";
    public static final String ORIGIN_TERMINAL = "q.origin_terminal";
    public static final String PLOD = "q.finaldestination";
    public static final String CLIENT = "q.clientname"; //1
    public static final String QUOTE_DATE = "q.Quote_Date";
    public static final String DESTINATION = "q.destination";
    public static final String PLOR = "q.PLOR";
    public static final String CLIENT_TYPE = "q.clienttype";
    public static final String CONGINEE = "b.Consignee"; //2
    public static final String RAMP_CITY = "q.ramp_city";
    public static final String ISSUE_TERMINAL = "q.issuing_terminal";
    public static final String ORIGIN_REGION = "q.Door_Origin";
    public static final String DESTINATION_REGION = "q.destination_port";
    public static final String SHIPPER = "b.shipper";   //3
    public static final String BOOKED_BY = "b.booked_by";
    public static final String FORWARD = "b.forward";   //4
    public static final String SSL_BOOKING = "b.SSBookingNo";
    public static final String CARRIER = "q.Sslname";
    public static final String QUOTE_BY = "q.quote_by";
    public static final String CONTAINER_FOR_BL = "trailer_no";
    public static final String Bl_VOID = "f.void";
    public static final String QUOTATION = "Quotation";
    public static final String MASTERBL = "f.new_master_bl";
    public static final String FCLAMS = "f.ams";
    public static final String SUBHOUSE = "f.subHouse";
    // CLOUMN FOR bL
    public static final String BLCONGINEE = "f.house_consignee_name";
    public static final String BLFORWARDER = "f.forwarding_agent_name";
    public static final String BLSHIPPER = "f.house_shipper_name";
    public static final String AESDETAILS = "f.aes_details";
    public static final String SAIL_DATE = "f.sail_date";
    private String origin_terminal;
    private String destination_port;
    private String rampCity;
    private String fileNo;
    private String pol;
    private String pod;
    private Date fileDate;
    private String carrier;
    private String client;
    private String user;
    private Integer quotId;
    private Integer bookingId;
    private Integer fclBlId;
    private String issueTerminal;
    private String fclBlStatus;
    private String ssBkgNo;
    private String shipper;
    private String manifest;
    private boolean CorrectedBL;
    private String ratesNonRates;
    private String displayColor;
    private String hazmat;
    private String bookingComplete;
    private String bookedBy;
    private String doorOrigin;
    private String doorDestination;
    private String docReceived;
    private String blvoid;
    private String correctionsPresent;
    private String bolId;
    private String fclInttgra;
    private String readyToEDI;
    private String blAudit;
    private String master;
    private String blClosed;
    private String readyToPost;
    private String correctionId;
    private Date etd;
    private String importRelease;
    private String ams;
    private String subHouse;
    private String inbondNumber;
    private String itnNumber;
    private Date bolDate;
    private String aesItn;
    private String trackingStatus;
    private String aesStatus;
    private String bookingAesStatus;
    private String _304Success;
    private String _997Success;
    private String _304Failure;
    private Integer fclUnit = 0;
    private String documentStatus;
    private String trailerNo;
    private Integer aesCount;
    private Date etaDate;
    private Boolean docsNotReceivedFlag;

    public String getAesItn() {
        return aesItn;
    }

    public void setAesItn(String aesItn) {
        this.aesItn = aesItn;
    }

    public Boolean getDocsNotReceivedFlag() {
        return docsNotReceivedFlag;
    }

    public void setDocsNotReceivedFlag(Boolean docsNotReceivedFlag) {
        this.docsNotReceivedFlag = docsNotReceivedFlag;
    }

    public void setAms(String ams) {
        this.ams = ams;
    }

    public void setSubHouse(String subHouse) {
        this.subHouse = subHouse;
    }

    public String getAms() {
        return ams;
    }

    public String getSubHouse() {
        return subHouse;
    }

    public String getBlAudit() {
        return blAudit;
    }

    public void setBlAudit(String blAudit) {
        this.blAudit = blAudit;
    }

    public String getBlClosed() {
        return blClosed;
    }

    public void setBlClosed(String blClosed) {
        this.blClosed = blClosed;
    }

    public String getBolId() {
        return bolId;
    }

    public void setBolId(String bolId) {
        this.bolId = bolId;
    }

    public String getFclInttgra() {
        return fclInttgra;
    }

    public void setFclInttgra(String fclInttgra) {
        this.fclInttgra = fclInttgra;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getReadyToEDI() {
        return readyToEDI;
    }

    public void setReadyToEDI(String readyToEDI) {
        this.readyToEDI = readyToEDI;
    }

    public String getReadyToPost() {
        return readyToPost;
    }

    public void setReadyToPost(String readyToPost) {
        this.readyToPost = readyToPost;
    }

    public String getCorrectionId() {
        return correctionId;
    }

    public void setCorrectionId(String correctionId) {
        this.correctionId = correctionId;
    }

    public String getBlvoid() {
        return blvoid;
    }

    public void setBlvoid(String blvoid) {
        this.blvoid = blvoid;
    }

    public String getDocReceived() {
        return docReceived;
    }

    public void setDocReceived(String docReceived) {
        this.docReceived = docReceived;
    }

    public String getHazmat() {
        return hazmat;
    }

    public void setHazmat(String hazmat) {
        this.hazmat = hazmat;
    }

    public String getManifest() {
        return manifest;
    }

    public void setManifest(String manifest) {
        this.manifest = manifest;
    }

    public String getDisplayColor() {
        return displayColor;
    }

    public void setDisplayColor(String displayColor) {
        this.displayColor = displayColor;
    }

    public String getFclBlStatus() {
        return fclBlStatus;
    }

    public void setFclBlStatus(String fclBlStatus) {
        this.fclBlStatus = fclBlStatus;
    }

    public String get304Failure() {
        return _304Failure;
    }

    public void set304Failure(String _304Failure) {
        this._304Failure = _304Failure;
    }

    public String get304Success() {
        return _304Success;
    }

    public void set304Success(String _304Success) {
        this._304Success = _304Success;
    }

    public String getAesStatus() {
        return aesStatus;
    }

    public void setAesStatus(String aesStatus) {
        this.aesStatus = aesStatus;
    }

    public String getTrackingStatus() {
        return trackingStatus;
    }

    public void setTrackingStatus(String trackingStatus) {
        this.trackingStatus = trackingStatus;
    }

    public String getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(String documentStatus) {
        this.documentStatus = documentStatus;
    }

    public Integer getFclUnit() {
        return fclUnit;
    }

    public void setFclUnit(Integer fclUnit) {
        this.fclUnit = fclUnit;
    }

    public FileNumberForQuotaionBLBooking() {
    }

    public FileNumberForQuotaionBLBooking(Object objectArrayValues[], boolean importFlag) throws Exception {
        /*BookingId, BolId,Quote_ID,
         file_no,origin_terminal,PLOD,clientname,
         Carrier,Quote_Date,destination_port,PLOR,clienttype,quote_by*/
        String fileType = "";
        if (objectArrayValues.length > 28) {
            if (objectArrayValues[0] != null) {
                String a = objectArrayValues[0].toString();
                quotId = Integer.parseInt(a);
            }
            if (objectArrayValues[1] != null) {
                String a = objectArrayValues[1].toString();
                bookingId = Integer.parseInt(a);
            }
            if (objectArrayValues[2] != null) {
                String a = objectArrayValues[2].toString();
                fclBlId = Integer.parseInt(a);
            }
            if (objectArrayValues[3] != null) {
                if (objectArrayValues[3] instanceof Integer) {
                    Integer fileNumber = (Integer) objectArrayValues[3];
                    fileNo = fileNumber.toString();
                } else if (objectArrayValues[3] instanceof String) {
                    fileNo = (String) objectArrayValues[3];
                }
            }
            if (objectArrayValues[4] != null) {
                origin_terminal = (String) objectArrayValues[4];
            }
            if (objectArrayValues[5] != null) {
                pod = (String) objectArrayValues[5];
            }
            if (objectArrayValues[6] != null) {
                client = (String) objectArrayValues[6];
            }
            if (objectArrayValues[7] != null) {
                String _sslname = (String) objectArrayValues[7];
                if (!_sslname.equals("") && _sslname.indexOf("//") == -1) {
                    carrier = _sslname + "//" + (String) objectArrayValues[7];
                }
            }

            if (objectArrayValues[8] != null) {
                fileDate = (Date) objectArrayValues[8];
            }
            if (objectArrayValues[9] != null) {
                destination_port = (String) objectArrayValues[9];
            }
            if (objectArrayValues[10] != null) {
                pol = (String) objectArrayValues[10];
            }
            if (objectArrayValues[12] != null) {
                user = (String) objectArrayValues[12];
            }
            if (objectArrayValues[13] != null) {
                rampCity = (String) objectArrayValues[13];
            }
            if (objectArrayValues[14] != null) {
                issueTerminal = (String) objectArrayValues[14];
            }
            if (objectArrayValues[15] != null) {
                shipper = (String) objectArrayValues[15];
            }

            if (objectArrayValues[17] != null) {
                ratesNonRates = (String) objectArrayValues[17];
            }

            if (objectArrayValues[18] != null) {
                //TODO : check here
                //user = (String) objectArrayValues[18];
            }

            if (objectArrayValues[19] != null) {
                String _sslname = (String) objectArrayValues[19];
                if (!_sslname.equals("") && _sslname.indexOf("//") == -1) {
                    carrier = _sslname + "//" + (String) objectArrayValues[20];
                }
            }

            if (objectArrayValues[29] != null) {
                bolId = objectArrayValues[29].toString();
            }

            if (objectArrayValues[30] != null) {
                if (objectArrayValues[30].equals("Y")) {
                    hazmat = "H";
                }
            }

            if (objectArrayValues[21] != null) {
                doorOrigin = (String) objectArrayValues[21];
            }

            if (objectArrayValues[16] != null) {
                bookedBy = (String) objectArrayValues[16];
            }
            if (objectArrayValues[23] != null) {
                ssBkgNo = (String) objectArrayValues[23];
            }

            if (objectArrayValues[24] != null) {
                String s = (String) objectArrayValues[24];
                if (s.equals("Y")) {
                    docReceived = "Y";
                } else {
                    docReceived = "N";
                }
            } else {
                docReceived = "N";
            }

            if (objectArrayValues[25] != null) {
                String s = (String) objectArrayValues[25];
                if (s.equals("Y")) {
                    bookingComplete = "Y";
                } else {
                    bookingComplete = "N";
                }
            } else {
                bookingComplete = "N";
            }

            if (CommonFunctions.isNotNull(objectArrayValues[24])) {
                docReceived = (String) objectArrayValues[24];
            }
            if (CommonFunctions.isNotNull(objectArrayValues[26])) {
                setFclBlStatus(objectArrayValues[26], "D");//
                Date docCutDate = (Date) objectArrayValues[26];
                Date currentDate = new Date();
                if (docCutDate.before(currentDate) || docCutDate.equals(currentDate)) {
                    if ("N".equals(docReceived)) {
                        this.docsNotReceivedFlag = true;
                    } else {
                        this.docsNotReceivedFlag = false;
                    }
                } else {
                    this.docsNotReceivedFlag = false;
                }

            }
            if (CommonFunctions.isNotNull(objectArrayValues[27])) {
                setFclBlStatus(objectArrayValues[27], "C");
            }
            if (CommonFunctions.isNotNull(objectArrayValues[28])) {
                setFclBlStatus(objectArrayValues[28], "S", 0);
            }
            if (objectArrayValues[32] != null) {
                blvoid = (String) objectArrayValues[32];
            }
            /*  if (objectArrayValues[31] != null) {
             if (Integer.parseInt(objectArrayValues[31].toString()) > 0) {
             hazmat = "H";
             }
             }
            
             // If the Hazmat count for the FclBl is more than 0;
             if (objectArrayValues[33] != null) {
             if (Integer.parseInt(objectArrayValues[33].toString()) > 0) {
             hazmat = "H";
             }
             }*/

            if (objectArrayValues[34] != null) {
                fclInttgra = (String) objectArrayValues[34];
            }
            if (objectArrayValues[35] != null) {
                readyToEDI = (String) objectArrayValues[35];
            }
            if (objectArrayValues[36] != null) {
                blAudit = (String) objectArrayValues[36];
            }
            if (objectArrayValues[37] != null) {
                master = (String) objectArrayValues[37];
            }
            if (objectArrayValues[38] != null) {
                blClosed = (String) objectArrayValues[38];
            }
            if (objectArrayValues[39] != null) {
                readyToPost = (String) objectArrayValues[39];
            }
            if (objectArrayValues[40] != null) {
                correctionId = objectArrayValues[40].toString();
            }
            if (objectArrayValues[42] != null) {
                importRelease = objectArrayValues[42].toString();
            }
            if (objectArrayValues.length > 44 && CommonFunctions.isNotNull(objectArrayValues[45])) {
                importRelease = (CommonFunctions.isNotNull(importRelease)) ? importRelease + objectArrayValues[45].toString() : "N" + objectArrayValues[45].toString();
            } else {
                importRelease = (CommonFunctions.isNotNull(importRelease)) ? importRelease + "N" : "NN";
            }
            if (objectArrayValues.length > 45 && objectArrayValues[46] != null) {
                doorDestination = objectArrayValues[46].toString();
            }
            if (objectArrayValues.length > 48 && objectArrayValues[48] != null) {
                bolDate = (Date) objectArrayValues[48];
            }
            if (objectArrayValues.length > 50 && objectArrayValues[50] != null) {
                itnNumber = objectArrayValues[50].toString();
            }

            if (objectArrayValues.length > 53 && objectArrayValues[53] != null) {
                trackingStatus = objectArrayValues[53].toString();
            }

            if (objectArrayValues.length > 54 && objectArrayValues[54] != null) {
                aesStatus = objectArrayValues[54].toString();
            }

            if (objectArrayValues.length > 55 && objectArrayValues[55] != null) {
                _304Success = objectArrayValues[55].toString();
            }

            if (objectArrayValues.length > 56 && objectArrayValues[56] != null) {
                _304Failure = objectArrayValues[56].toString();
            }
            if (objectArrayValues.length > 57 && objectArrayValues[57] != null) {
                bookingAesStatus = objectArrayValues[57].toString();
            }

            if (objectArrayValues[40] != null) {
                try {
                    fclUnit = Integer.parseInt(objectArrayValues[40].toString());
                } catch (Exception e) {
                    fclUnit = 0;
                    throw e;
                }
            }

            if (objectArrayValues.length > 58 && objectArrayValues[58] != null) {
                    aesCount = Integer.parseInt(objectArrayValues[58].toString());
            }

            if (objectArrayValues.length > 59 && objectArrayValues[59] != null) {
                documentStatus = objectArrayValues[59].toString();
            }
            if (objectArrayValues.length > 60 && objectArrayValues[60] != null) {
                String[] str = objectArrayValues[60].toString().split(",");
                String unit = "";
                int count = 0;
                for (String string : str) {
                    if (!"".equals(string)) {
                        count++;
                        if (!"".equals(unit)) {
                            if (count == 1) {
                                unit = unit + "" + string;
                            } else {
                                unit = unit + "," + string;
                            }
                        } else {
                            unit = string;
                        }
                        if (count == 2) {
                            count = 0;
                            unit = unit + "<br>";
                        }
                    }
                }
                trailerNo = unit;
            }
            if (objectArrayValues.length > 61 && objectArrayValues[61] != null) {
                _997Success = objectArrayValues[61].toString();
            }
            if (objectArrayValues.length > 62 && objectArrayValues[62] != null) {
                etaDate = (Date)objectArrayValues[62];
            }

        }
        setUp(objectArrayValues[30], objectArrayValues[17], objectArrayValues[51], objectArrayValues[52], fileType, "");
    }

    private void setFclBlStatus(Object dateObject, String mark)throws Exception {
        setFclBlStatus(dateObject, mark, 0);
    }

    private void setFclBlStatus(Object dateObject, String mark, int delta)throws Exception {
        Date edtDate = null;
        String strDate = "";
        if (dateObject instanceof Date) {
            edtDate = (Date) dateObject;
        } else if (dateObject instanceof String) {
            strDate = (String) dateObject;
            edtDate = DateUtils.parseDate(strDate, "yyyy-MM-dd hh:mm:ss");
        }
        edtDate = DateUtils.formatDateAndParseToDate(edtDate);
        long diff = new DBUtil().getDaysBetweenTwoDays(edtDate, DateUtils.formatDateAndParseToDate(new Date()));
        if (diff >= 0) {
            if (fclBlStatus != null) {
                fclBlStatus = fclBlStatus + (diff + delta) + mark + ",";
            } else {
                fclBlStatus = (diff + delta) + mark + ",";
            }
        } else {
            //fclBlStatus="";
        }
        /*else{
         if (fclBlStatus != null) {
         fclBlStatus = fclBlStatus + (0) + mark + ",";
         } else {
         fclBlStatus = (0) + mark+ ",";
         }
         }*/
    }

    private void setUp(Object hazmat, Object rated, Object correctionCount, Object brakBulk, String fileType, Object booking_unit_count) {
        //FclBl fclBl = new FclBlDAO().getFileNoObject(fileNo);
        //Quotation quotation = new QuotationDAO().getFileNoObject(null != fileNo ? fileNo.indexOf("-")>-1?Integer.parseInt(fileNo.substring(0,fileNo.indexOf("-"))):Integer.parseInt(fileNo) : 0);
        if (null != rated && "N".equalsIgnoreCase(rated.toString())) {
            if (CommonFunctions.isNotNull(fclBlStatus)) {
                fclBlStatus = fclBlStatus + "NR" + ",";
            } else {
                fclBlStatus = "NR,";
            }
        }
        if ("P".equalsIgnoreCase(fileType)) {
            fclBlStatus = fclBlStatus + "P" + ",";
        }
        if (null != rated && "N".equalsIgnoreCase(rated.toString()) && null != brakBulk && "Y".equalsIgnoreCase(brakBulk.toString())) {
            //if (CommonFunctions.isNotNullOrNotEmpty(fclBl.getFclcontainer())) {
            if (CommonFunctions.isNotNull(fclBlStatus)) {
                fclBlStatus = fclBlStatus + "1U" + ",";
            } else {
                fclBlStatus = "1U,";
            }
            //}
        } else if (bookingId != null) {
            int unit = fclUnit;
            if (unit > 0) {
                if (CommonFunctions.isNotNull(fclBlStatus)) {
                    fclBlStatus = fclBlStatus + (unit) + "U" + ",";
                } else {
                    fclBlStatus = (unit) + "U,";
                }
            }
        }

        if (fclBlId != null) {
            StringBuilder statusBuffer = new StringBuilder("");
            if (CommonFunctions.isNotNull(getFclInttgra())) {
                statusBuffer.append(getFclInttgra());
                statusBuffer.append(",");
            }
            if (CommonFunctions.isNotNull(getReadyToEDI())) {
                statusBuffer.append("E");
                statusBuffer.append(",");
            }
            if (CommonFunctions.isNotNull(getBlAudit())) {
                statusBuffer.append("A");
                statusBuffer.append(",");
            }
            if (getMaster() != null && getMaster().equalsIgnoreCase("Yes")) {
                statusBuffer.append("RM");
                statusBuffer.append(",");
            }
            if (CommonFunctions.isNotNull(getBlClosed())) {
                statusBuffer.append("CL");
                statusBuffer.append(",");
            }
            if (CommonFunctions.isNotNull(getBlvoid())) {
                statusBuffer.append("V");
                statusBuffer.append(",");
            }
            manifest = getReadyToPost();
            if (bolId != null && bolId.contains(FclBlConstants.DELIMITER)) {
                CorrectedBL = true;
            }
            if (null != correctionCount && !"0".equals(correctionCount.toString())) {
                correctionsPresent = "Corrections Exist";
            }
            fclBlStatus += statusBuffer.toString();
        }
    }

    public FileNumberForQuotaionBLBooking(Quotation quotation, BookingFcl bookingFcl, FclBl fclBl) {
        if (quotation != null) {
            quotId = quotation.getQuoteId();
            origin_terminal = quotation.getOrigin_terminal();
            destination_port = quotation.getDestination_port();
            if (quotation.getFileNo() != null) {
                fileNo = quotation.getFileNo().toString();
            }
            pol = quotation.getPlor();
            pod = quotation.getFinaldestination();
            fileDate = quotation.getQuoteDate();
            carrier = quotation.getCarrier();
            client = quotation.getClientname();
            user = quotation.getQuoteBy();
            issueTerminal = quotation.getIssuingTerminal();
            rampCity = quotation.getRampCity();
        } else if (bookingFcl != null) {
            bookingId = bookingFcl.getBookingId();
            origin_terminal = bookingFcl.getOriginTerminal();
            destination_port = bookingFcl.getDestination();
            fileNo = bookingFcl.getFileNo().toString();
            pol = bookingFcl.getPortofOrgin();
            pod = bookingFcl.getPortofDischarge();
            fileDate = bookingFcl.getBookingDate();
            if (bookingFcl.getCarrierName() != null) {
                carrier = bookingFcl.getCarrierName().getCarriername();
            }
            client = bookingFcl.getConsignee();
            rampCity = bookingFcl.getRampCity();
            user = bookingFcl.getBookedBy();
            ssBkgNo = bookingFcl.getSSBookingNo();
        } else if (fclBl != null) {
            fclBlId = fclBl.getBol();
            origin_terminal = fclBl.getTerminal();
            destination_port = fclBl.getFinalDestination();
            fileNo = fclBl.getFileNo();
            pol = fclBl.getPortOfLoading();
            pod = fclBl.getPortofDischarge();
            fileDate = fclBl.getBolDate();
            if (fclBl.getSslineName() != null) {
                carrier = fclBl.getSslineName();
            }
            client = fclBl.getConsigneeName();
            user = fclBl.getBlBy();
            rampCity = fclBl.getRampCity();
            issueTerminal = fclBl.getBillingTerminal();
            importRelease = fclBl.getImportRelease();
        }

    }

    public Map getSearchProperty(SearchQuotationForm searchQuotationform) {
        Map<String, String> searchPreperty = new HashMap<String, String>();
        //File No
        if (isNotNull(searchQuotationform.getFileNumber())) {
            searchPreperty.put(FILE_NO, searchQuotationform.getFileNumber().replaceAll("[^a-zA-Z0-9]+",""));
        }
        if (isNotNull(searchQuotationform.getShowVoidBL()) && searchQuotationform.getShowVoidBL().equalsIgnoreCase("Y")) {
            searchPreperty.put(Bl_VOID, searchQuotationform.getShowVoidBL());
        }
        //Client
        if (isNotNull(searchQuotationform.getClient())) {
            searchPreperty.put(CLIENT, searchQuotationform.getClient().trim().replace("'", "+"));

        }
        //Shipper
        if (isNotNull(searchQuotationform.getShipper())) {
            if (!searchPreperty.containsKey(CLIENT)) {
                searchPreperty.put(CLIENT, searchQuotationform.getShipper().trim().replace("'", "+"));
            }//else{
            searchPreperty.put(SHIPPER, searchQuotationform.getShipper().trim().replace("'", "+"));
            //}
        }
        //Forwarder
        if (isNotNull(searchQuotationform.getForwarder())) {
            if (!searchPreperty.containsKey(CLIENT)) {
                searchPreperty.put(CLIENT, searchQuotationform.getForwarder().trim().replace("'", "+"));
            }//else{
            searchPreperty.put(FORWARD, searchQuotationform.getForwarder().trim().replace("'", "+"));
            //}
        }
        //Conginee
        if (isNotNull(searchQuotationform.getConginee())) {
            if (!searchPreperty.containsKey(CLIENT)) {
                searchPreperty.put(CLIENT, searchQuotationform.getConginee().trim().replace("'", "+"));
            }//else{
            searchPreperty.put(CONGINEE, searchQuotationform.getConginee().trim().replace("'", "+"));
            //}
        }
        //Origin
        if (searchQuotationform.getPol() != null && !searchQuotationform.getPol().trim().equals("")) {
            searchPreperty.put(ORIGIN_TERMINAL, searchQuotationform.getPol().trim().replace("'", "+"));
        }
        //Ams
        if (isNotNull(searchQuotationform.getAms())) {
            searchPreperty.put(FCLAMS, searchQuotationform.getForwarder().trim().replace("'", "+"));
            //}
        }
        //POL
        if (isNotNull(searchQuotationform.getPlor())) {
            searchPreperty.put(PLOR, searchQuotationform.getPlor().trim().replace("'", "+"));
        }
        //POD
        if (isNotNull(searchQuotationform.getPlod())) {
            searchPreperty.put(PLOD, searchQuotationform.getPlod().trim().replace("'", "+"));
        }
        //Destination
        if (isNotNull(searchQuotationform.getPod())) {
            searchPreperty.put(DESTINATION, searchQuotationform.getPod().trim().replace("'", "+"));
        }

        //From Date and To Date
        if (isNotNull(searchQuotationform.getQuotestartdate())) {
                String toDate = "";
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dateToSearch = new Date(searchQuotationform.getQuotestartdate());

                if (searchQuotationform.getToDate() != null && !searchQuotationform.getToDate().trim().equals("")) {
                        Date dateToSearchDate = new Date(searchQuotationform.getToDate());
                        toDate = dateFormat.format(dateToSearchDate);
                }
                String fromDate = dateFormat.format(dateToSearch);
                if (isNotNull(searchQuotationform.getFromSailDateCheck()) && (searchQuotationform.getFromSailDateCheck()).equals("on")) {
                    searchPreperty.put(SAIL_DATE, "'" + fromDate + " 00:00:00' and '" + toDate + " 23:59:59'");
                } else {
                    searchPreperty.put(QUOTE_DATE, "'" + fromDate + " 00:00:00' and '" + toDate + " 23:59:59'");
                }

        }
        //Issuing Terminal
        if (isNotNull(searchQuotationform.getIssuingTerminal())) {
            searchPreperty.put(ISSUE_TERMINAL, searchQuotationform.getIssuingTerminal().trim().replace("'", "+"));
        }
        //Origin Region
        if (isNotNull(searchQuotationform.getOriginRegion()) && !"0".equals(searchQuotationform.getOriginRegion())) {
            searchPreperty.put(ORIGIN_REGION, searchQuotationform.getOriginRegion().replace("'", "\\'"));
        }

        //Destination Region
        if (isNotNull(searchQuotationform.getDestinationRegion()) && !"0".equals(searchQuotationform.getDestinationRegion())) {
            searchPreperty.put(DESTINATION_REGION, searchQuotationform.getDestinationRegion().replace("'", "\\'"));
        }
        //SSL Booking
        if (isNotNull(searchQuotationform.getSsBkgNo())) {
            searchPreperty.put(SSL_BOOKING, searchQuotationform.getSsBkgNo().replace("'", "\\'"));
        }
        //Container No
        if (isNotNull(searchQuotationform.getContainer())) {
            searchPreperty.put(CONTAINER_FOR_BL, searchQuotationform.getContainer().replace("'", "\\'"));
        }
        //SSL
        if (isNotNull(searchQuotationform.getSslDescription())) {
            searchPreperty.put(CARRIER, searchQuotationform.getSslDescription().trim().replace("'", "\\'"));
        }
        //Created By
        if (isNotNull(searchQuotationform.getQuoteBy())) {
            searchPreperty.put(QUOTE_BY, searchQuotationform.getQuoteBy().trim().replace("'", "+"));
        }
        //Booked By
        if (isNotNull(searchQuotationform.getBookedBy())) {
            searchPreperty.put(BOOKED_BY, searchQuotationform.getBookedBy().trim().replace("'", "+"));
        }
        if (isNotNull(searchQuotationform.getRampCity())) {
            searchPreperty.put(RAMP_CITY, searchQuotationform.getRampCity().replace("'", "\\'"));
        }
        // Master BL
        if (isNotNull(searchQuotationform.getMasterBL())) {
            searchPreperty.put(MASTERBL, searchQuotationform.getMasterBL().replace("'",""));
        }
        if (isNotNull(searchQuotationform.getSubHouse())) {
            searchPreperty.put(SUBHOUSE, searchQuotationform.getSubHouse().trim().replace("'", "+"));
        }
        //Inbond Number
        if (isNotNull(searchQuotationform.getInbondNumber())) {
            searchPreperty.put("t.inbondNumber", searchQuotationform.getInbondNumber().replace("'",""));
        }
        if (isNotNull(searchQuotationform.getAesItn())) {
            searchPreperty.put(AESDETAILS, searchQuotationform.getAesItn().replace("'",""));
        }
        return searchPreperty;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getDestination_port() {
        return destination_port;
    }

    public void setDestination_port(String destination_port) {
        this.destination_port = destination_port;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getOrigin_terminal() {
        return origin_terminal;
    }

    public void setOrigin_terminal(String origin_terminal) {
        this.origin_terminal = origin_terminal;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getFileDate() {
        return fileDate;
    }

    public void setFileDate(Date fileDate) {
        this.fileDate = fileDate;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getQuotId() {
        return quotId;
    }

    public void setQuotId(Integer quotId) {
        this.quotId = quotId;
    }

    public Integer getFclBlId() {
        return fclBlId;
    }

    public void setFclBlId(Integer fclBlId) {
        this.fclBlId = fclBlId;
    }

    public String getIssueTerminal() {
        return issueTerminal;
    }

    public void setIssueTerminal(String issueTerminal) {
        this.issueTerminal = issueTerminal;
    }

    public String getRampCity() {
        return rampCity;
    }

    public void setRampCity(String rampCity) {
        this.rampCity = rampCity;
    }

    public String getSsBkgNo() {
        return ssBkgNo;
    }

    public void setSsBkgNo(String ssBkgNo) {
        this.ssBkgNo = ssBkgNo;
    }

    public String getShipper() {
        return shipper;
    }

    public void setShipper(String shipper) {
        this.shipper = shipper;
    }

    public boolean getCorrectedBL() {
        return CorrectedBL;
    }

    public void setCorrectedBL(boolean correctedBL) {
        CorrectedBL = correctedBL;
    }

    public String getRatesNonRates() {
        return ratesNonRates;
    }

    public void setRatesNonRates(String ratesNonRates) {
        this.ratesNonRates = ratesNonRates;
    }

    public String getBookingComplete() {
        return bookingComplete;
    }

    public void setBookingComplete(String bookingComplete) {
        this.bookingComplete = bookingComplete;
    }

    public String getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(String bookedBy) {
        this.bookedBy = bookedBy;
    }

    public String getDoorOrigin() {
        return doorOrigin;
    }

    public void setDoorOrigin(String doorOrigin) {
        this.doorOrigin = doorOrigin;
    }

    public Integer getAesCount() {
        return aesCount;
    }

    public void setAesCount(Integer aesCount) {
        this.aesCount = aesCount;
    }

    private boolean isNotNull(Object object) {
        if (object != null && !object.toString().trim().equals("")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FileNumberForQuotaionBLBooking other = (FileNumberForQuotaionBLBooking) obj;
        if ((this.fileNo == null) ? (other.fileNo != null) : !this.fileNo.equals(other.fileNo)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.fileNo != null ? this.fileNo.hashCode() : 0);
        return hash;
    }

    public int compare(FileNumberForQuotaionBLBooking o1, FileNumberForQuotaionBLBooking o2) {
        if (null == o2.getFileNo() || null == o1.getFileNo()) {
            return 0;
        } else {
            return o2.getFileNo().compareTo(o1.getFileNo());
        }
    }

    public String getCorrectionsPresent() {
        return correctionsPresent;
    }

    public void setCorrectionsPresent(String correctionsPresent) {
        this.correctionsPresent = correctionsPresent;
    }

    public Date getEtd() {
        return etd;
    }

    public void setEtd(Date etd) {
        this.etd = etd;
    }

    public Date getEtaDate() {
        return etaDate;
    }

    public void setEtaDate(Date etaDate) {
        this.etaDate = etaDate;
    }

    public String getImportRelease() {
        return importRelease;
    }

    public void setImportRelease(String importRelease) {
        this.importRelease = importRelease;
    }

    public String getDoorDestination() {
        return doorDestination;
    }

    public void setDoorDestination(String doorDestination) {
        this.doorDestination = doorDestination;
    }

    public String getInbondNumber() {
        return inbondNumber;
    }

    public void setInbondNumber(String inbondNumber) {
        this.inbondNumber = inbondNumber;
    }

    public Date getBolDate() {
        return bolDate;
    }

    public void setBolDate(Date bolDate) {
        this.bolDate = bolDate;
    }

    public String getItnNumber() {
        return itnNumber;
    }

    public void setItnNumber(String itnNumber) {
        this.itnNumber = itnNumber;
    }

    public String getBookingAesStatus() {
        return bookingAesStatus;
    }

    public void setBookingAesStatus(String bookingAesStatus) {
        this.bookingAesStatus = bookingAesStatus;
    }

    public String getTrailerNo() {
        return trailerNo;
    }

    public void setTrailerNo(String trailerNo) {
        this.trailerNo = trailerNo;
    }

    public String get997Success() {
        return _997Success;
    }

    public void set997Success(String _997Success) {
        this._997Success = _997Success;
    }
}

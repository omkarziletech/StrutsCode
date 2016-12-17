package com.gp.cong.logisoft.edi.gtnexus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.fcl.EdiTrackingBC;
import com.gp.cong.logisoft.bc.notes.NotesBC;
import com.gp.cong.logisoft.domain.Shploc;
import com.gp.cong.logisoft.domain.Shpsta;
import com.gp.cong.logisoft.edi.util.LogFileWriter;
import com.gp.cong.logisoft.hibernate.dao.LogFileEdiDAO;
import com.gp.cong.logisoft.hibernate.dao.ShplocDAO;
import com.gp.cong.logisoft.hibernate.dao.ShpstaDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSSMasterBlDAO;
import com.gp.cong.struts.LoadJobProperty;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;

import org.apache.log4j.Logger;

public class GtnexusShpStatus extends DefaultHandler {

    private static final Logger log = Logger.getLogger(GtnexusShpStatus.class);
    private String _startElement = "";
    private Vector v = null;
    String lastMethodCalled = "";
    String correctStr = "";
    private List files = null;
    private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
    private String company = "G";
    private String messageType = "315";
    private Shpsta shpsta;
    private boolean saveFlag = true;
    private String errorMessage = "";
    private String fileName = "";
    Date dt = new Date();
    private String strError = "";
    String currentDate = new String();
    String systemdate = new String();
    Properties prop = new Properties();
    private String dockReceiptNumber;
    private String trackingStatus;
    private boolean lclFlag = false;
    EdiTrackingBC ediTrackingBC = new EdiTrackingBC();
    ShplocDAO shplocDAO = new ShplocDAO();
    FclBlDAO fclBlDAO = new FclBlDAO();
    ShpstaDAO shpstaDAO = new ShpstaDAO();
    LogFileWriter logFileWriter = new LogFileWriter();
    LogFileEdiDAO logFileEdiDAO = new LogFileEdiDAO();
    UserDAO userDAO = new UserDAO();
    String dateFolder = "";

    public void execute(GtnexusShpStatus gtnexusShpStatus) {
        currentDate = sdf1.format(dt);
        String status = "";
        File file = null;
        File sourceFile = null;
        File destFile = null;
        try {
            prop.getClass().getResourceAsStream(CommonConstants.EDIPROPERTIES);
            createDirectory();
            ReadGTnexusXMLFiles readXMLFiles = new ReadGTnexusXMLFiles();
            files = readXMLFiles.readFiles(messageType, osName());
            DefaultHandler handler = gtnexusShpStatus;
            SAXParserFactory factory = SAXParserFactory.newInstance();
            // Parse the input
            SAXParser saxParser = factory.newSAXParser();
            dateFolder = DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
            if (files != null && files.size() > 0) {
                for (int i = 0; i < files.size(); i++) {
                    // Use an instance of ourselves as the SAX event handler
                    // Use the default (non-validating) parser
                    try {
                        file = (File) files.get(i);
                        fileName = file.getName();
                        String drNo = "";
                        saxParser.parse(file, handler);
                        if (CommonUtils.isNotEmpty(strError)) {
                            if (osName().contains("linux")) {
                                sourceFile = new File(prop.getProperty("linuxGtnexus315Directory") + file.getName());
                                destFile = new File(prop.getProperty("linuxGtnexus315Unprocessed") + dateFolder + file.getName());
                            } else {
                                sourceFile = new File(prop.getProperty("gtnexus315Directory") + file.getName());
                                destFile = new File(prop.getProperty("gtnexus315Unprocessed") + dateFolder + file.getName());
                            }
                            readXMLFiles.move(sourceFile, destFile);
                            errorMessage = strError;
                            if (CommonUtils.isNotEmpty(this.getDockReceiptNumber()) && null != this.getShpsta()) {
                                drNo = this.getDockReceiptNumber();
                                ediTrackingBC.setEdiLog(file.getName(), currentDate, "failed", errorMessage, company, messageType, drNo, this.getShpsta().getBkgnum(), "", null);
                                logFileWriter.doAppend("Error while reading XML file " + fileName + ". " + errorMessage, "error_logfile_" + fileName + "_" + currentDate + ".txt", company, osName(), messageType);
                            }
                        } else {
                            if (osName().contains("linux")) {
                                sourceFile = new File(prop.getProperty("linuxGtnexus315Directory") + file.getName());
                                destFile = new File(prop.getProperty("linuxGtnexus315Archive") + dateFolder + file.getName());
                            } else {
                                sourceFile = new File(prop.getProperty("gtnexus315Directory") + file.getName());
                                destFile = new File(prop.getProperty("gtnexus315Archive") + dateFolder + file.getName());
                            }
                            if (saveFlag) {
                                readXMLFiles.move(sourceFile, destFile);
                            } else {
                                if (osName().contains("linux")) {
                                    destFile = new File(prop.getProperty("linuxGtnexus315Unprocessed") + dateFolder + file.getName());
                                } else {
                                    destFile = new File(prop.getProperty("gtnexus315Unprocessed") + dateFolder + file.getName());
                                }
                                readXMLFiles.move(sourceFile, destFile);
                            }
                            //readXMLFiles.move(sourceFile, destFile);
                            if (CommonUtils.isNotEmpty(this.getDockReceiptNumber()) && null != this.getShpsta()) {
                                drNo = this.getDockReceiptNumber();
                                ediTrackingBC.setShipmentStatusLog(file.getName(), currentDate, "success", "No Error", company, messageType, drNo, this.getTrackingStatus(), this.getShpsta());
                            }
                        }
                    } catch (Exception e) {
                        log.info("Error inside class XmlToCsv  execute() method.  on " + new Date(), e);
                        errorMessage = e.toString();
                        log.info("Error while reading XML file3. " + file.getName() + " " + e);
                        if (osName().contains("linux")) {
                            sourceFile = new File(prop.getProperty("linuxGtnexus315Directory") + file.getName());
                            destFile = new File(prop.getProperty("linuxGtnexus315Unprocessed") + dateFolder + file.getName());
                        } else {
                            sourceFile = new File(prop.getProperty("gtnexus315Directory") + file.getName());
                            destFile = new File(prop.getProperty("gtnexus315Unprocessed") + dateFolder + file.getName());
                        }
                        readXMLFiles.move(sourceFile, destFile);
                        if (CommonUtils.isNotEmpty(this.getDockReceiptNumber())) {
                            ediTrackingBC.setEdiLog(file.getName(), currentDate, "failed", errorMessage, company, messageType, this.getDockReceiptNumber(), "", "", null);
                            logFileWriter.doAppend("Error while reading XML file " + fileName + ". Type of Error "
                                    + "is---" + e, "error_logfile_" + fileName + "_" + currentDate + ".txt", company, osName(), messageType);
                        }
                    }
                }//end of for loop    
            }//end of if
        } catch (Exception e) {
            log.info("Error inside class XmlToCsv  execute() method.  on " + new Date(), e);
        }
    }

    // SAX DocumentHandler methods
    public void startDocument()
            throws SAXException {
        v = new Vector();
        lastMethodCalled = "startDocument";
    }

    public void endDocument()
            throws SAXException {
        lastMethodCalled = "endDocument";
        try {
            showRecords();
        } catch (Exception e) {
            try {
                logFileWriter.doAppend("Error while reading XML Tag inside endDocument() Method. Type of Error " + "is---" + e, "error_logfile_" + fileName + "_" + currentDate + ".txt", company, osName(), messageType);
                //        	throw new SAXException("I/O error", e);
            } catch (Exception ex) {
                log.info("Error inside class XmlToCsv  endDocument() method.  on " + new Date(), ex);
            }
        }
    }

    public void startElement(String namespaceURI,
            String lName, // local name
            String qName, // qualified name
            Attributes attrs) throws SAXException {
        lastMethodCalled = "startElement";
        String eName = lName; // element name

        if ("".equals(eName)) {
            eName = qName; // namespaceAware = false
        }
        if (!eName.equalsIgnoreCase("ECUEDI")) {
            _startElement = _startElement + "-" + eName;
        }
        if (attrs != null) {
            for (int i = 0; i < attrs.getLength(); i++) {
                String aName = attrs.getLocalName(i); // Attr name 
                if ("".equals(aName)) {
                    aName = attrs.getQName(i);
                }
                if (!eName.equalsIgnoreCase("ECUEDI")) {
                    _startElement = _startElement + "::" + aName + "=\"" + attrs.getValue(i) + "\"";
                }
            }
        }
    }

    public void endElement(String namespaceURI,
            String sName, // simple name
            String qName // qualified name
    ) throws SAXException {
        if (lastMethodCalled.equalsIgnoreCase("startElement")) {
            String s = new String("startElement::" + qName);

            v.add(s);
        } else if (lastMethodCalled.equalsIgnoreCase("characters")) {

            String s = new String();
            s = this.correctStr;
            if (s.contains("\"")) {
                int it = s.indexOf("\"");
                while (it >= 0) {
                    s = s.substring(0, it) + "'" + s.substring(it + 1);
                    it = s.indexOf("\"");
                }
            }
            s = s.trim();
            s = _startElement + "=CONGRUENCE=" + s;
            v.add(s);
            this.correctStr = "";
        }
        lastMethodCalled = "endElement";
        int i = _startElement.indexOf(qName);
        if (i > 0) {
            _startElement = _startElement.substring(0, i - 1);
        }

    }

    public void characters(char buf[], int offset, int len) throws SAXException {
        lastMethodCalled = "characters";
        String ss1 = new String(buf, offset, len);
        String ss = new String();
        for (int i = 0; i < ss1.length(); i++) {
            int hashcode = new Character(ss1.charAt(i)).hashCode();
            if ((hashcode == 10)) {
                //System.out.println(" ***ENTER KEY REMOVED !!****");
                if (hashcode == 10) {
                    ss = ss + " ";
                }
            } else if (hashcode == 9) {
                //System.out.println(" ***TAB KEY REMOVED !!****");
            } else {
                ss = ss + ss1.charAt(i);
            }
        }

        ss = "A" + ss;
        ss = ss.trim();
        StringTokenizer st = new StringTokenizer(ss, " ");
        StringBuffer actSS = new StringBuffer();
        while (st.hasMoreTokens()) {
            actSS.append(st.nextToken() + " ");
        }
        ss = actSS.toString();
        ss = ss.trim();
        ss = ss.substring(1);
        if (ss.length() > 0) {
            if (this.correctStr.length() > 0) {
                this.correctStr = this.correctStr + ss;
            } else {
                this.correctStr = ss;
            }
        }
    }

    public String removeLastIndex(String originalStr) {
        if (originalStr.length() <= 2) {
            return originalStr;
        }
        originalStr = originalStr.substring(0, originalStr.length() - 2);
        return originalStr;

    }

    public void showRecords() throws Exception {
        Shpsta shpsta = new Shpsta();
        Shploc shploc = new Shploc();
        String vesselCodeQualifier = "";
        String vesselCode = "";
        String containerNumber = "";
        String containerPrefix = "";
        String containerCheckDigit = "";
        boolean cobJobOn = LoadJobProperty.getJobStatus("Automatic Confirm on Board Reminders");
        int count = 0;
        for (Enumeration e = v.elements(); e.hasMoreElements();) {
            String str = e.nextElement().toString();
//	   		System.out.println("INSIDE FOR====== "+str);
            if (str.indexOf("ShipmentStatusMessage-MessageID") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String messageID = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (messageID != null && !messageID.trim().equals("")) {
                    if (messageID.length() > 35) {
                        strError = "Length of the MessageId is more than 35 characters";
                    }
                    shpsta.setMsgid(messageID);
                    shploc.setMsgid(messageID);
                }
            }//end of ShipmentStatusMessage-MessageID if condition
            if (str.indexOf("ShipmentStatusMessage-MessageDate") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String messageDate = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (messageDate != null && !messageDate.trim().equals("")) {
                    if (messageDate.length() > 8) {
                        strError = "Length of the MessageDate is more than 8 characters";
                    }
                    shpsta.setMsgdte(Integer.parseInt(messageDate));
                    shploc.setMsgdte(Integer.parseInt(messageDate));
                }
            }//end of ShipmentStatusMessage-MessageDate if condition
            if (str.indexOf("ShipmentStatusMessage-MessageTime") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String messageTime = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (messageTime != null && !messageTime.trim().equals("")) {
                    if (messageTime.contains(":")) {
                        messageTime = messageTime.replace(":", "");
                    }
                    if (messageTime.length() > 6) {
                        strError = "Length of the MessageTime is more than 6 characters";
                    }
                    shpsta.setMsgtim(Integer.parseInt(messageTime));
                    shploc.setMsgtim(Integer.parseInt(messageTime));
                }
            }//end of ShipmentStatusMessage-MessageTime if condition
            if (str.indexOf("ShipmentStatusMessage-MessageSender") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String messageSender = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (messageSender.length() > 35) {
                    strError = "Length of the MessageSender is more than 35 characters";
                }
                shpsta.setSndidt(messageSender);
                shploc.setSndidt(messageSender);
            }//end of ShipmentStatusMessage-MessageSender if condition
            if (str.contains("ShipmentStatusMessage-MessageRecipient") && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String messageRecipient = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (messageRecipient.length() > 35) {
                    strError = "Length of the MessageRecipient is more than 35 characters";
                }
                shpsta.setRcpidt(messageRecipient);
                shploc.setRcpidt(messageRecipient);
            }//end of ShipmentStatusMessage-MessageRecipient if condition
            if (str.contains("ShipmentStatusMessage-FileName") && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String fileName = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (fileName != null && !fileName.trim().equals("")) {
                    if (fileName.length() > 30) {
                        strError = "Length of the FileName is more than 30 characters";
                    }
                    shpsta.setXmlnam(fileName);
                }
            }//end of ShipmentStatusMessage-Filename if condition
            if (str.contains("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusEvent-EventCode") && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String eventCode = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (eventCode != null && !eventCode.trim().equals("")) {
                    if (eventCode.length() > 3) {
                        strError = "Length of the EventCode is more than 3 characters";
                    }
                    shpsta.setEvncod(eventCode);
                }
            }//end of ShipmentStatusMessage-ShipmentStatus-ShipmentStatusEvent-EventCode if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusEvent-Even-EventLocation-Location-LocationFunction") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusEvent-Even-EventLocation-Location-LocationCode") == -1) {
                String locationFunction = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (locationFunction != null && !locationFunction.trim().equals("")) {
                    if (locationFunction.length() > 1) {
                        strError = "Length of the Event LocationFunction is more than 1 characters";
                    }
                    shpsta.setEvnlfn(locationFunction);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusEvent-Even-EventLocation-Location-LocationFunction") == -1
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusEvent-Even-EventLocation-Location-LocationCodeQualifier") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusEvent-Even-EventLocation-Location-LocationName") == -1) {
                String locationCodeQualifier = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (locationCodeQualifier != null && !locationCodeQualifier.trim().equals("")) {
                    if (locationCodeQualifier.length() > 35) {
                        strError = "Length of the Event LocationCodeQualifier is more than 35 characters";
                    }
                    shpsta.setEvnlqa(locationCodeQualifier);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusEvent-Even-EventLocation-Location-LocationFunction") == -1
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusEvent-Even-EventLocation-Location-LocationCode") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusEvent-Even-EventLocation-Location-LocationCodeQualifier") == -1) {
                String locationCode = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (locationCode != null && !locationCode.trim().equals("")) {
                    if (locationCode.length() > 35) {
                        strError = "Length of the Event LocationCode is more than 35 characters";
                    }
                    shpsta.setEvlcva(locationCode);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusEvent-Even-EventLocation-Location-LocationFunction") == -1
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusEvent-Even-EventLocation-Location-LocationCode") == -1
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusEvent-Even-EventLocation-Location-LocationCodeQualifier") == -1
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusEvent-Even-EventLocation-Location-LocationName") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusEvent-Even-EventLocation-Location-CountryCode") == -1) {
                String locationName = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (locationName != null && !locationName.trim().equals("")) {
                    if (locationName.length() > 256) {
                        strError = "Length of the Event LocationName is more than 256 characters";
                    }
                    shpsta.setLocnam(locationName);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusEvent-Even-EventLocation-Location-CountryCode") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String countryCode = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (countryCode != null && !countryCode.trim().equals("")) {
                    if (countryCode.length() > 25) {
                        strError = "Length of the Event CountryCode is more than 25 characters";
                    }
                    shpsta.setLocntr(countryCode);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusEvent-Even-EventLocation-Location-StateProvinceCode") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String stateProvinceCode = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (stateProvinceCode != null && !stateProvinceCode.trim().equals("")) {
                    if (stateProvinceCode.length() > 2) {
                        strError = "Length of the Event StateProvinceCode is more than 2 characters";
                    }
                    shpsta.setLostat(stateProvinceCode);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusEvent-EventDateTime-DateTime-Date") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String dateTimeDate = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (dateTimeDate != null && !dateTimeDate.trim().equals("")) {
                    if (dateTimeDate.length() > 8) {
                        strError = "Length of the Event Date is more than 8 characters";
                    }
                    shpsta.setEvndat(Integer.parseInt(dateTimeDate));
                }
            }//end of ShipmentStatusMessage-MessageID if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusEvent-Even-Time") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusEvent-Even-Timezone") == -1) {
                String dateTime = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (dateTime != null && !dateTime.trim().equals("")) {
                    dateTime = dateTime + "00";
                    if (dateTime.length() > 6) {
                        strError = "Length of the Event Time is more than 6 characters";
                    }
                    shpsta.setEvntim(Integer.parseInt(dateTime));
                }
            }//end of ShipmentStatusMessage-ShipmentStatus-ShipmentStatusEvent-EventDateTime-DateTime if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusEvent-Even-DateTimeQualifier") == -1
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusEvent-Even-Timezone") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String timeZone = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (timeZone != null && !timeZone.trim().equals("")) {
                    if (timeZone.length() > 3) {
                        strError = "Length of the Event Zone is more than 3 characters";
                    }
                    shpsta.setEvnzon(timeZone);
                }
            }//end of ShipmentStatusMessage-ShipmentStatus-ShipmentStatusEvent-EventDateTime-DateTime-Timezone if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusEvent-Even-DateTimeQualifier") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String dateTimeQualifier = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (dateTimeQualifier != null && !dateTimeQualifier.trim().equals("")) {
                    if (dateTimeQualifier.length() > 35) {
                        strError = "Length of the Event DateTimeQualifier is more than 35 characters";
                    }
                    if (dateTimeQualifier.contains("A")) {
                        dateTimeQualifier = "Actual";
                    }
                    if (dateTimeQualifier.contains("E")) {
                        dateTimeQualifier = "Estimated";
                    }
                    shpsta.setEvdtyp(dateTimeQualifier);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusEvent-Even-EventLocation-Location-TerminalName") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String terminalName = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (terminalName != null && !terminalName.trim().equals("")) {
                    if (terminalName.length() > 30) {
                        strError = "Length of the Event TerminalName is more than 30 characters";
                    }
                    shpsta.setEvterm(terminalName);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusReferences-BookingNumber") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String bookingNumber = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (bookingNumber != null && !bookingNumber.trim().equals("")) {
                    if (bookingNumber.length() > 30) {
                        strError = "Length of the  BookingNumber is more than 30 characters";
                    }
                    shpsta.setBkgnum(bookingNumber);
                    String voyageNo = new LclSSMasterBlDAO().findVoyageNoByBkgNo(bookingNumber);
                    if(CommonUtils.isNotEmpty(voyageNo)){
                        Object[] siIdAndFileName = (Object[]) logFileEdiDAO.findSiIdAndFileName(voyageNo+"_"+bookingNumber);
                        if (null != siIdAndFileName && siIdAndFileName.length == 2) {
                            lclFlag = true;
                            this.setDockReceiptNumber(voyageNo);
                        }
                    }
                    if(!lclFlag){
                        String drNumber = logFileEdiDAO.findDrNumberFromBooking(bookingNumber);
                        this.setDockReceiptNumber(drNumber);
                        if (CommonUtils.isNotEmpty(drNumber)) {
                            saveFlag = true;
                        } else {
                            saveFlag = false;
                            break;
                        }
                    }
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusReferences-BillOfLadingNumber") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String billOfLadingNumber1 = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (billOfLadingNumber1 != null && !billOfLadingNumber1.trim().equals("")) {
                    if (billOfLadingNumber1.length() > 35) {
                        strError = "Length of the  BillOfLadingNumber is more than 35 characters";
                    }
                    shpsta.setBlldg1(billOfLadingNumber1);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusReferences-BLShipmentID") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String blShipmentId = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (blShipmentId != null && !blShipmentId.trim().equals("")) {
                    if (blShipmentId.length() > 30) {
                        strError = "Length of the  BLShipmentID is more than 30 characters";
                    }
                    shpsta.setBlshid(blShipmentId);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusReferences-ConsigneeOrderNumber") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String consigneeOrderNumber = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (consigneeOrderNumber != null && !consigneeOrderNumber.trim().equals("")) {
                    if (consigneeOrderNumber.length() > 35) {
                        strError = "Length of the  ConsigneeOrderNumber is more than 35 characters";
                    }
                    shpsta.setConord(consigneeOrderNumber);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusReferences-ForwarderReferenceNumber") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String freightForwarderRefNo = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (freightForwarderRefNo != null && !freightForwarderRefNo.trim().equals("")) {
                    if (freightForwarderRefNo.length() > 35) {
                        strError = "Length of the  ForwarderReferenceNumber is more than 35 characters";
                    }
                    shpsta.setFrtref(freightForwarderRefNo);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusReferences-PurchaseOrderNumber") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String purchaseOrderNo1 = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (purchaseOrderNo1 != null && !purchaseOrderNo1.trim().equals("")) {
                    if (purchaseOrderNo1.length() > 35) {
                        strError = "Length of the  PurchaseOrderNumber is more than 35 characters";
                    }
                    shpsta.setPonum1(purchaseOrderNo1);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusReferences-ShipperReferenceNumber") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String shipperIdentifyingNo = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (shipperIdentifyingNo != null && !shipperIdentifyingNo.trim().equals("")) {
                    if (shipperIdentifyingNo.length() > 35) {
                        strError = "Length of the  ShipperReferenceNumber is more than 35 characters";
                    }
                    shpsta.setShpide(shipperIdentifyingNo);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusReferences-SalesResponsibility") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String salesResponsibility = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (salesResponsibility != null && !salesResponsibility.trim().equals("")) {
                    if (salesResponsibility.length() > 35) {
                        strError = "Length of the  SalesResponsibility is more than 35 characters";
                    }
                    shpsta.setSalres(salesResponsibility);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-VesselInformation-VesselName") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-VesselInformation-VesselCode") == -1) {
                String vesselName = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (vesselName != null && !vesselName.trim().equals("")) {
                    if (vesselName.length() > 35) {
                        strError = "Length of the  VesselName is more than 35 characters";
                    }
                    shpsta.setVesnam(vesselName);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-VesselInformation-VoyageNumber") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String voyageNumber = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (voyageNumber != null && !voyageNumber.trim().equals("")) {
                    if (voyageNumber.length() > 35) {
                        strError = "Length of the  VoyageNumber is more than 35 characters";
                    }
                    shpsta.setVoynum(voyageNumber);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusReferences-CarrierSCAC") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String carrierSCAC = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (carrierSCAC != null && !carrierSCAC.trim().equals("")) {
                    if (carrierSCAC.length() > 4) {
                        strError = "Length of the  CarrierSCAC is more than 4 characters";
                    }
                    shpsta.setScac(carrierSCAC);
                    shploc.setCarrid(carrierSCAC);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-VesselInformation-VesselName") == -1
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-VesselInformation-VesselCodeQualifier") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                vesselCodeQualifier = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                //xs.setVesselCodeQualifier(vesselCodeQualifier);
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-VesselInformation-VesselName") == -1
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-VesselInformation-VesselCodeQualifier") == -1
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-VesselInformation-VesselCode") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                vesselCode = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                //xs.setVesselCode(vesselCode);
            }
            if (vesselCodeQualifier != null && vesselCodeQualifier.trim().equals("L")) {
                if (vesselCode.length() > 9) {
                    strError = "Length of the  LloydsCode is more than 9 characters";
                }
                shpsta.setLlycod(vesselCode);
            }
            if (vesselCodeQualifier != null && vesselCodeQualifier.trim().equals("M")) {
                if (vesselCode.length() > 8) {
                    strError = "Length of the  MutuallyDefined is more than 8 characters";
                }
                shpsta.setMutdef(vesselCode);
            }
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusEvent-Even-Even-ContainerStatus") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String containerStatus = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (containerStatus != null && !containerStatus.trim().equals("")) {
                    if (containerStatus.length() > 35) {
                        strError = "Length of the ContainerStatus is more than 35 characters";
                    }
                    if (containerStatus.equals("L")) {
                        containerStatus = "Load/Full";
                    } else if (containerStatus.equals("E")) {
                        containerStatus = "Empty";
                    }
                    shpsta.setCntsta(containerStatus);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition	
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusEvent-Even-Even-ContainerNumber") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                if (null != str.substring(str.lastIndexOf("=CONGRUENCE=") + 12) && !str.substring(str.lastIndexOf("=CONGRUENCE=") + 12).trim().equals("")) {
                    containerNumber = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusEvent-Even-Even-ContainerPrefix") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                if (null != str.substring(str.lastIndexOf("=CONGRUENCE=") + 12) && !str.substring(str.lastIndexOf("=CONGRUENCE=") + 12).trim().equals("")) {
                    containerPrefix = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                }
            }
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusEvent-Even-Even-ContainerCheckDigit") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                if (null != str.substring(str.lastIndexOf("=CONGRUENCE=") + 12) && !str.substring(str.lastIndexOf("=CONGRUENCE=") + 12).trim().equals("")) {
                    containerCheckDigit = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                }
            }
            if (containerNumber != null && !containerNumber.trim().equals("")) {
                String containerNumbers = containerPrefix + containerNumber + containerCheckDigit;
                if (containerNumbers.length() > 17) {
                    strError = "Length of the ContainerNumber is more than 17 characters";
                }
                shpsta.setCntnum(containerNumbers);
            }
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusEvent-Even-Even-ContainerType") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String containerType = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (containerType != null && !containerType.trim().equals("")) {
//	   			 System.out.println("containerType..........."+containerType);
                    if (containerType.length() > 10) {
                        strError = "Length of the ContainerType is more than 10 characters";
                    }
                    shpsta.setCnttyp(containerType);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusLocations-PlaceOfReceipt-Location-LocationFunction") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusLocations-PlaceOfReceipt-Location-LocationCode") == -1) {
                String plorLocationFunction = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (plorLocationFunction != null && !plorLocationFunction.trim().equals("")) {
                    if (plorLocationFunction.length() > 1) {
                        strError = "Length of the PlaceOfReceipt LocationFunction is more than 1 character";
                    }
                    shploc.setPlrfnc(plorLocationFunction);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusLocations-PlaceOfReceipt-Location-LocationCodeQualifier") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusLocations-PlaceOfReceipt-Location-LocationName") == -1) {
                String plorLocationCodeQualifier = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (plorLocationCodeQualifier != null && !plorLocationCodeQualifier.trim().equals("")) {
                    if (plorLocationCodeQualifier.length() > 35) {
                        strError = "Length of the PlaceOfReceipt LocationCodeQualifier is more than 35 character";
                    }
                    shploc.setPlrcqa(plorLocationCodeQualifier);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusLocations-PlaceOfReceipt-Location-LocationFunction") == -1
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusLocations-PlaceOfReceipt-Location-LocationCode") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusLocations-PlaceOfReceipt-Location-LocationCodeQualifier") == -1) {
                String plorLocationCode = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (plorLocationCode != null && !plorLocationCode.trim().equals("")) {
                    if (plorLocationCode.length() > 35) {
                        strError = "Length of the PlaceOfReceipt LocationCode is more than 35 character";
                    }
                    shploc.setPlrcod(plorLocationCode);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusLocations-PlaceOfReceipt-Location-LocationCodeQualifier") == -1
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusLocations-PlaceOfReceipt-Location-LocationName") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusLocations-PlaceOfReceipt-Location-CountryCode") == -1) {
                String plorLocationName = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (plorLocationName != null && !plorLocationName.trim().equals("")) {
                    if (plorLocationName.length() > 256) {
                        strError = "Length of the PlaceOfReceipt LocationName is more than 256 character";
                    }
                    shploc.setPlrnam(plorLocationName);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusLocations-PlaceOfReceipt-Location-CountryCode") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String plorCountryCode = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (plorCountryCode != null && !plorCountryCode.trim().equals("")) {
                    if (plorCountryCode.length() > 25) {
                        strError = "Length of the PlaceOfReceipt CountryCode is more than 25 character";
                    }
                    shploc.setPlrcry(plorCountryCode);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusLocations-PlaceOfReceipt-Location-StateProvinceCode") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String plorStateProvinceCode = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (plorStateProvinceCode != null && !plorStateProvinceCode.trim().equals("")) {
                    if (plorStateProvinceCode.length() > 2) {
                        strError = "Length of the PlaceOfReceipt StateProvinceCode is more than 2 character";
                    }
                    shploc.setPlrsta(plorStateProvinceCode);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusLocations-PlaceOfReceipt-Location-TerminalName") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String plorTerminalName = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (plorTerminalName != null && !plorTerminalName.trim().equals("")) {
                    if (plorTerminalName.length() > 30) {
                        strError = "Length of the PlaceOfReceipt TerminalName is more than 30 character";
                    }
                    shploc.setPlrtrm(plorTerminalName);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusLocations-PlaceOfReceipt-Location-DateTimeQualifier") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String plorDateTimeQualifier = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (plorDateTimeQualifier != null && !plorDateTimeQualifier.trim().equals("")) {
                    if (plorDateTimeQualifier.length() > 35) {
                        strError = "Length of the PlaceOfReceipt DateTimeQualifier is more than 35 character";
                    }
                    if (plorDateTimeQualifier.contains("A")) {
                        plorDateTimeQualifier = "Actual";
                    }
                    if (plorDateTimeQualifier.contains("E")) {
                        plorDateTimeQualifier = "Estimated";
                    }
                    shploc.setPlrdqa(plorDateTimeQualifier);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusLocations-PlaceOfReceipt-Location-Timezone") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusLocations-PlaceOfReceipt-Location-DateTimeQualifier") == -1) {
                String plorTimezone = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (plorTimezone != null && !plorTimezone.trim().equals("")) {
                    if (plorTimezone.length() > 3) {
                        strError = "Length of the PlaceOfReceipt Timezone is more than 3 character";
                    }
                    shploc.setPlrzon(plorTimezone);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusLocations-PlaceOfReceipt-Location-DateTime-Date") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String plorDate = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (plorDate != null && !plorDate.trim().equals("")) {
                    if (plorDate.length() > 8) {
                        strError = "Length of the PlaceOfReceipt DateTime is more than 8 character";
                    }
                    shploc.setPlrdat(Integer.parseInt(plorDate));
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusLocations-PlaceOfReceipt-Location-Time") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatusLocations-PlaceOfReceipt-Location-Timezone") == -1) {
                String plorTime = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (plorTime != null && !plorTime.trim().equals("")) {
                    plorTime = plorTime + "00";
                    if (plorTime.length() > 6) {
                        strError = "Length of the PlaceOfReceipt Time is more than 6 character";
                    }
                    shploc.setPlrtim(Integer.parseInt(plorTime));
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfLoad-Location-LocationFunction") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfLoad-Location-LocationCode") == -1) {
                String polLocationFunction = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (polLocationFunction != null && !polLocationFunction.trim().equals("")) {
                    if (polLocationFunction.length() > 1) {
                        strError = "Length of the PortOfLoad LocationFunction is more than 1 character";
                    }
                    shploc.setPolfnc(polLocationFunction);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfLoad-Location-LocationCodeQualifier") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfLoad-Location-LocationName") == -1) {
                String polLocationCodeQualifier = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (polLocationCodeQualifier != null && !polLocationCodeQualifier.trim().equals("")) {
                    if (polLocationCodeQualifier.length() > 35) {
                        strError = "Length of the PortOfLoad LocationCodeQualifier is more than 35 character";
                    }
                    shploc.setPolcqa(polLocationCodeQualifier);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfLoad-Location-LocationFunction") == -1
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfLoad-Location-LocationCode") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfLoad-Location-LocationCodeQualifier") == -1) {
                String polLocationCode = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (polLocationCode != null && !polLocationCode.trim().equals("")) {
                    if (polLocationCode.length() > 35) {
                        strError = "Length of the PortOfLoad LocationCode is more than 35 character";
                    }
                    shploc.setPolcod(polLocationCode);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfLoad-Location-LocationCodeQualifier") == -1
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfLoad-Location-LocationName") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfLoad-Location-CountryCode") == -1) {
                String polLocationName = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (polLocationName != null && !polLocationName.trim().equals("")) {
                    if (polLocationName.length() > 256) {
                        strError = "Length of the PortOfLoad LocationName is more than 256 character";
                    }
                    shploc.setPolnam(polLocationName);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfLoad-Location-CountryCode") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String polCountryCode = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (polCountryCode != null && !polCountryCode.trim().equals("")) {
                    if (polCountryCode.length() > 25) {
                        strError = "Length of the PortOfLoad CountryCode is more than 25 character";
                    }
                    shploc.setPolcry(polCountryCode);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfLoad-Location-StateProvinceCode") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String polStateProvinceCode = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (polStateProvinceCode != null && !polStateProvinceCode.trim().equals("")) {
                    if (polStateProvinceCode.length() > 2) {
                        strError = "Length of the PortOfLoad StateProvinceCode is more than 2 character";
                    }
                    shploc.setPolsta(polStateProvinceCode);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfLoad-Location-TerminalName") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String polTerminalName = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (polTerminalName != null && !polTerminalName.trim().equals("")) {
                    if (polTerminalName.length() > 30) {
                        strError = "Length of the PortOfLoad TerminalName is more than 30 character";
                    }
                    shploc.setPoltrm(polTerminalName);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfLoad-Location-DateTimeQualifier") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String polDateTimeQualifier = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (polDateTimeQualifier != null && !polDateTimeQualifier.trim().equals("")) {
                    if (polDateTimeQualifier.length() > 35) {
                        strError = "Length of the PortOfLoad DateTimeQualifier is more than 35 character";
                    }
                    if (polDateTimeQualifier.contains("A")) {
                        polDateTimeQualifier = "Actual";
                    }
                    if (polDateTimeQualifier.contains("E")) {
                        polDateTimeQualifier = "Estimated";
                    }
                    shploc.setPoldqa(polDateTimeQualifier);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfLoad-Location-Timezone") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfLoad-Location-DateTimeQualifier") == -1) {
                String polTimezone = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (polTimezone != null && !polTimezone.trim().equals("")) {
                    if (polTimezone.length() > 3) {
                        strError = "Length of the PortOfLoad Timezone is more than 3 character";
                    }
                    shploc.setPolzon(polTimezone);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfLoad-Location-DateTime-Date") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String polDate = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (polDate != null && !polDate.trim().equals("")) {
                    if (polDate.length() > 8) {
                        strError = "Length of the PortOfLoad Date is more than 8 character";
                    }
                    shploc.setPoldat(Integer.parseInt(polDate));
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfLoad-Location-Time") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfLoad-Location-Timezone") == -1) {
                String polTime = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (polTime != null && !polTime.trim().equals("")) {
                    polTime = polTime + "00";
                    if (polTime.length() > 6) {
                        strError = "Length of the PortOfLoad Time is more than 6 character";
                    }
                    shploc.setPoltim(Integer.parseInt(polTime));
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfDischarge-Location-LocationFunction") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfDischarge-Location-LocationCode") == -1) {
                String podLocationFunction = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (podLocationFunction != null && !podLocationFunction.trim().equals("")) {
                    if (podLocationFunction.length() > 1) {
                        strError = "Length of the PortOfDischarge LocationFunction is more than 1 character";
                    }
                    shploc.setPodfnc(podLocationFunction);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfDischarge-Location-LocationCodeQualifier") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String podLocationCodeQualifier = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (podLocationCodeQualifier != null && !podLocationCodeQualifier.trim().equals("")) {
                    if (podLocationCodeQualifier.length() > 35) {
                        strError = "Length of the PortOfDischarge LocationCodeQualifier is more than 35 character";
                    }
                    shploc.setPodcqa(podLocationCodeQualifier);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfDischarge-Location-LocationFunction") == -1
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfDischarge-Location-LocationCode") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfDischarge-Location-LocationCodeQualifier") == -1) {
                String podLocationCode = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (podLocationCode != null && !podLocationCode.trim().equals("")) {
                    if (podLocationCode.length() > 35) {
                        strError = "Length of the PortOfDischarge LocationCode is more than 35 character";
                    }
                    shploc.setPodcod(podLocationCode);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfDischarge-Location-LocationName") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String podLocationName = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (podLocationName != null && !podLocationName.trim().equals("")) {
                    if (podLocationName.length() > 256) {
                        strError = "Length of the PortOfDischarge LocationName is more than 256 character";
                    }
                    shploc.setPodnam(podLocationName);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfDischarge-Location-CountryCode") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String podCountryCode = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (podCountryCode != null && !podCountryCode.trim().equals("")) {
                    if (podCountryCode.length() > 25) {
                        strError = "Length of the PortOfDischarge CountryCode is more than 25 character";
                    }
                    shploc.setPodcry(podCountryCode);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfDischarge-Location-StateProvinceCode") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String podStateProvinceCode = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (podStateProvinceCode != null && !podStateProvinceCode.trim().equals("")) {
                    if (podStateProvinceCode.length() > 2) {
                        strError = "Length of the PortOfDischarge StateProvinceCode is more than 2 character";
                    }
                    shploc.setPodsta(podStateProvinceCode);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfDischarge-Location-TerminalName") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String podTerminalName = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (podTerminalName != null && !podTerminalName.trim().equals("")) {
                    if (podTerminalName.length() > 30) {
                        strError = "Length of the PortOfDischarge TerminalName is more than 30 character";
                    }
                    shploc.setPodtrm(podTerminalName);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfDischarge-Location-DateTimeQualifier") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String podDateTimeQualifier = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (podDateTimeQualifier != null && !podDateTimeQualifier.trim().equals("")) {
                    if (podDateTimeQualifier.length() > 35) {
                        strError = "Length of the PortOfDischarge DateTimeQualifier is more than 35 character";
                    }
                    if (podDateTimeQualifier.contains("A")) {
                        podDateTimeQualifier = "Actual";
                    }
                    if (podDateTimeQualifier.contains("E")) {
                        podDateTimeQualifier = "Estimated";
                    }
                    shploc.setPoddqa(podDateTimeQualifier);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfDischarge-Location-Timezone") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfDischarge-Location-DateTimeQualifier") == -1) {
                String podTimezone = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (podTimezone != null && !podTimezone.trim().equals("")) {
                    if (podTimezone.length() > 3) {
                        strError = "Length of the PortOfDischarge Timezone is more than 3 character";
                    }
                    shploc.setPodzon(podTimezone);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfDischarge-Location-DateTime-Date") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String podDate = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (podDate != null && !podDate.trim().equals("")) {
                    if (podDate.length() > 8) {
                        strError = "Length of the PortOfDischarge Date is more than 8 character";
                    }
                    shploc.setPoddat(Integer.parseInt(podDate));
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfDischarge-Location-Time") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PortOfDischarge-Location-Timezone") == -1) {
                String podTime = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (podTime != null && !podTime.trim().equals("")) {
                    podTime = podTime + "00";
                    if (podTime.length() > 6) {
                        strError = "Length of the PortOfDischarge Time is more than 6 character";
                    }
                    shploc.setPodtim(Integer.parseInt(podTime));
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PlaceOfDelivery-Location-LocationFunction") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PlaceOfDelivery-Location-LocationCode") == -1) {
                String plodLocationFunction = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (plodLocationFunction != null && !plodLocationFunction.trim().equals("")) {
                    if (plodLocationFunction.length() > 1) {
                        strError = "Length of the PlaceOfDelivery LocationFunction is more than 1 character";
                    }
                    shploc.setPldfnc(plodLocationFunction);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PlaceOfDelivery-Location-LocationCodeQualifier") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String plodLocationCodeQualifier = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (plodLocationCodeQualifier != null && !plodLocationCodeQualifier.trim().equals("")) {
                    if (plodLocationCodeQualifier.length() > 35) {
                        strError = "Length of the PlaceOfDelivery LocationCodeQualifier is more than 35 character";
                    }
                    shploc.setPldcqa(plodLocationCodeQualifier);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PlaceOfDelivery-Location-LocationFunction") == -1
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PlaceOfDelivery-Location-LocationCode") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PlaceOfDelivery-Location-LocationCodeQualifier") == -1) {
                String plodLocationCode = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (plodLocationCode != null && !plodLocationCode.trim().equals("")) {
                    if (plodLocationCode.length() > 35) {
                        strError = "Length of the PlaceOfDelivery LocationCode is more than 35 character";
                    }
                    shploc.setPldcod(plodLocationCode);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PlaceOfDelivery-Location-LocationName") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String plodLocationName = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (plodLocationName != null && !plodLocationName.trim().equals("")) {
                    if (plodLocationName.length() > 256) {
                        strError = "Length of the PlaceOfDelivery LocationName is more than 256 character";
                    }
                    shploc.setPldnam(plodLocationName);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PlaceOfDelivery-Location-CountryCode") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String plodCountryCode = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (plodCountryCode != null && !plodCountryCode.trim().equals("")) {
                    if (plodCountryCode.length() > 25) {
                        strError = "Length of the PlaceOfDelivery CountryCode is more than 25 character";
                    }
                    shploc.setPldcry(plodCountryCode);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PlaceOfDelivery-Location-StateProvinceCode") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String plodStateProvinceCode = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (plodStateProvinceCode != null && !plodStateProvinceCode.trim().equals("")) {
                    if (plodStateProvinceCode.length() > 2) {
                        strError = "Length of the PlaceOfDelivery StateProvinceCode is more than 2 character";
                    }
                    shploc.setPldsta(plodStateProvinceCode);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PlaceOfDelivery-Location-TerminalName") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String plodTerminalName = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (plodTerminalName != null && !plodTerminalName.trim().equals("")) {
                    if (plodTerminalName.length() > 30) {
                        strError = "Length of the PlaceOfDelivery TerminalName is more than 30 character";
                    }
                    shploc.setPldtrm(plodTerminalName);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PlaceOfDelivery-Location-DateTimeQualifier") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String plodDateTimeQualifier = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (plodDateTimeQualifier != null && !plodDateTimeQualifier.trim().equals("")) {
                    if (plodDateTimeQualifier.length() > 35) {
                        strError = "Length of the PlaceOfDelivery DateTimeQualifier is more than 35 character";
                    }
                    if (plodDateTimeQualifier.contains("A")) {
                        plodDateTimeQualifier = "Actual";
                    }
                    if (plodDateTimeQualifier.contains("E")) {
                        plodDateTimeQualifier = "Estimated";
                    }
                    shploc.setPlddqa(plodDateTimeQualifier);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PlaceOfDelivery-Location-Timezone") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PlaceOfDelivery-Location-DateTimeQualifier") == -1) {
                String plodTimezone = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (plodTimezone != null && !plodTimezone.trim().equals("")) {
                    if (plodTimezone.length() > 3) {
                        strError = "Length of the PlaceOfDelivery Timezone is more than 3 character";
                    }
                    shploc.setPldzon(plodTimezone);
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PlaceOfDelivery-Location-DateTime-Date") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String plodDate = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (plodDate != null && !plodDate.trim().equals("")) {
                    if (plodDate.length() > 8) {
                        strError = "Length of the PlaceOfDelivery Date is more than 8 character";
                    }
                    shploc.setPlddat(Integer.parseInt(plodDate));
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
            if (str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PlaceOfDelivery-Location-Time") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0
                    && str.indexOf("ShipmentStatusMessage-ShipmentStatus-ShipmentStatu-PlaceOfDelivery-Location-Timezone") == -1) {
                String plodTime = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (plodTime != null && !plodTime.trim().equals("")) {
                    plodTime = plodTime + "00";
                    if (plodTime.length() > 6) {
                        strError = "Length of the PlaceOfDelivery Time is more than 6 character";
                    }
                    shploc.setPldtim(Integer.parseInt(plodTime));
                }
            }//end of ORDER-OrderReferences-vendorOrderNum if condition
        }// end of for loop
        if (saveFlag && (strError == null || strError.equals(""))) {
            this.setShpsta(shpsta);
            shplocDAO.save(shploc);
            shpstaDAO.save(shpsta);
            if(!lclFlag){
                FclBl fclBl = fclBlDAO.findByBookingNoForCOB(shpsta.getBkgnum());
                if (cobJobOn && CommonUtils.isNotEmpty(shpsta.getEvncod()) && null != fclBl && shpsta.getEvncod().equals("VD")) {
                    fclBl.setConfirmOnBorad("Y");
                    fclBl.setConfirmBy("SYSTEM");
                    fclBl.setConfirmOn(new Date());
                    fclBl.setVerifyETA(fclBl.getEta());
                    fclBlDAO.update(fclBl);
                    if (CommonUtils.isNotEmpty(fclBl.getBookingContact())) {
                        fclBlDAO.autoConfirmOnBoardReminder(fclBl);
                    }
                }
            }
            String ediTrackingStatus = new LogFileEdiDAO().findTrackingStatus(shpsta.getEvncod(), shpsta.getCntsta(), shpsta.getEvnlfn(), "G");
            String status = ediTrackingStatus;
            String locFun = "";
            if (CommonUtils.isNotEmpty(shpsta.getEvncod())) {
                locFun = shpsta.getEvncod();
                if (CommonUtils.isNotEmpty(shpsta.getCntsta())) {
                    locFun = locFun + "," + shpsta.getCntsta().substring(0, 1);
                }
                if (CommonUtils.isNotEmpty(shpsta.getEvnlfn())) {
                    locFun = locFun + "," + shpsta.getEvnlfn();
                }
            }
            if (CommonUtils.isNotEmpty(ediTrackingStatus)) {
                ediTrackingStatus = formatContainerNumber(shpsta.getCntnum()) + "  " + shpsta.getBkgnum() + "   " + locFun + "<br>" + ediTrackingStatus;
                if (CommonUtils.isNotEmpty(shpsta.getEvndat()) && CommonUtils.isNotEmpty(shpsta.getEvntim()) && CommonUtils.isNotEmpty(shpsta.getEvnzon())) {
                    ediTrackingStatus = ediTrackingStatus + " on " + shpsta.getEvndat() + " " + shpsta.getEvntim() + " " + shpsta.getEvnzon() + " " + shpsta.getEvdtyp() + " ";
                }
            }
            StringBuilder desc = new StringBuilder();
            if (CommonUtils.isNotEmpty(ediTrackingStatus)) {
                desc.append("ContainerNumber ->").append(formatContainerNumber(shpsta.getCntnum()));
                desc.append(" BookingNo ->").append(shpsta.getBkgnum());
                desc.append(" Event Code ->").append(locFun);
                desc.append(" Event date ->").append("<font color='green'>").append(shpsta.getEvndat()).append("</font>");
                desc.append(" Event time ->").append("<font color='green'>").append(shpsta.getEvntim()).append("</font>");
                desc.append(" Status ->").append("<font color='blue'><b>").append(status).append("</b></font>");
                desc.append(" LocationCode Agency ->").append("<font color='purple'>").append(shpsta.getEvlcva()).append("</font>");
            }
            this.setTrackingStatus(ediTrackingStatus);
            if(!lclFlag){
                new NotesBC().createEdiStatusNotes(desc.toString(), "04"+this.getDockReceiptNumber());
            }
        } else {
            this.setTrackingStatus("");
        }
    }

    public Shpsta getShpsta() {
        return shpsta;
    }

    public void setShpsta(Shpsta shpsta) {
        this.shpsta = shpsta;
    }

    private String osName() {
        return System.getProperty("os.name").toLowerCase();
    }

    public String getDockReceiptNumber() {
        return dockReceiptNumber;
    }

    public void setDockReceiptNumber(String dockReceiptNumber) {
        this.dockReceiptNumber = dockReceiptNumber;
    }

    public void createDirectory() {
        try {
            prop.load(getClass().getResourceAsStream(CommonConstants.EDIPROPERTIES));
            dateFolder = DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
            if (osName().contains("linux")) {
                File fileIn = new File(prop.getProperty("linuxGtnexus315Directory"));
                File fileArchive = new File(prop.getProperty("linuxGtnexus315Archive") + dateFolder);
                File fileUnprocessed = new File(prop.getProperty("linuxGtnexus315Unprocessed") + dateFolder);
                if (!fileIn.exists()) {
                    fileIn.mkdirs();
                }
                if (!fileArchive.exists()) {
                    fileArchive.mkdirs();
                }
                if (!fileUnprocessed.exists()) {
                    fileUnprocessed.mkdirs();
                }
            } else {
                File fileIn = new File(prop.getProperty("gtnexus315Directory"));
                File fileArchive = new File(prop.getProperty("gtnexus315Archive") + dateFolder);
                File fileUnprocessed = new File(prop.getProperty("gtnexus315Unprocessed") + dateFolder);
                if (!fileIn.exists()) {
                    fileIn.mkdirs();
                }
                if (!fileArchive.exists()) {
                    fileArchive.mkdirs();
                }
                if (!fileUnprocessed.exists()) {
                    fileUnprocessed.mkdirs();
                }
            }
        } catch (Exception e) {
            log.info("createDirectory failed on " + new Date(), e);
        }
    }

    public String getTrackingStatus() {
        return trackingStatus;
    }

    public void setTrackingStatus(String trackingStatus) {
        this.trackingStatus = trackingStatus;
    }

    public String formatContainerNumber(String contNo) {
        return contNo.substring(0, 4) + "-" + contNo.substring(4, 10) + "-" + contNo.substring(10, 11);
    }
}

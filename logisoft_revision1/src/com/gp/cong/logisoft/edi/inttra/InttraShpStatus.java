package com.gp.cong.logisoft.edi.inttra;

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
import com.gp.cong.logisoft.domain.lcl.LclSSMasterBl;
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

public class InttraShpStatus extends DefaultHandler {

    private static final Logger log = Logger.getLogger(InttraShpStatus.class);
    private String _startElement = "";
    private Vector v = null;
    String lastMethodCalled = "";
    private String gtnId = "";
    StringBuffer strBfr = new StringBuffer();
    String correctStr = "";
    private List files = null;
    private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
    private String company = "I";
    private String messageType = "315";
    private Shpsta shpsta;
    private boolean saveFlag = true;
    private boolean lclFlag = false;
    private String errorMessage = "";
    private String fileName = "";
    Date dt = new Date();
    private String strError = "";
    String currentDate = new String();
    Properties prop = new Properties();
    private String dockReceiptNumber;
    private String trackingStatus;
    EdiTrackingBC ediTrackingBC = new EdiTrackingBC();
    ShplocDAO shplocDAO = new ShplocDAO();
    ShpstaDAO shpstaDAO = new ShpstaDAO();
    LogFileWriter logFileWriter = new LogFileWriter();
    LogFileEdiDAO logFileEdiDAO = new LogFileEdiDAO();
    String dateFolder = "";
    FclBlDAO fclBlDAO = new FclBlDAO();
    UserDAO userDAO = new UserDAO();

    public void execute(InttraShpStatus inttraShpStatus) {
        currentDate = sdf1.format(dt);
        File file = null;
        String returnString = "";
        File sourceFile = null;
        File destFile = null;
        try {
            prop.getClass().getResourceAsStream(CommonConstants.EDIPROPERTIES);
            createDirectory();
            ReadInttraXMLFiles xmlFiles = new ReadInttraXMLFiles();
            files = xmlFiles.readFiles(messageType, osName());
            DefaultHandler handler = inttraShpStatus;
            SAXParserFactory factory = SAXParserFactory.newInstance();
            // Parse the input
            SAXParser saxParser = factory.newSAXParser();
            dateFolder = DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
            if (files != null && files.size() > 0) {
                for (int i = 0; i < files.size(); i++) {
                    try {
                        file = (File) files.get(i);
                        fileName = file.getName();
                        String drNo = "";
                        saxParser.parse(file, handler);
                        if (CommonUtils.isNotEmpty(strError)) {
                            if (osName().contains("linux")) {
                                sourceFile = new File(prop.getProperty("linuxInttra315Directory") + file.getName());
                                destFile = new File(prop.getProperty("linuxInttra315Unprocessed") + dateFolder + file.getName());
                            } else {
                                sourceFile = new File(prop.getProperty("inttra315Directory") + file.getName());
                                destFile = new File(prop.getProperty("inttra315Unprocessed") + dateFolder + file.getName());
                            }
                            xmlFiles.move(sourceFile, destFile);
                            if (CommonUtils.isNotEmpty(this.getDockReceiptNumber()) && null != this.getShpsta()) {
                                drNo = this.getDockReceiptNumber();
                                ediTrackingBC.setEdiLog(file.getName(), currentDate, "failed", strError, company, messageType, drNo, this.getShpsta().getBkgnum(), "", null);
                                logFileWriter.doAppend("Error while reading XML file " + fileName + " " + strError, "error_logfile_" + fileName + "_" + currentDate + ".txt", company, osName(), messageType);
                            }
                        } else {
                            if (osName().contains("linux")) {
                                sourceFile = new File(prop.getProperty("linuxInttra315Directory") + file.getName());
                                destFile = new File(prop.getProperty("linuxInttra315Archive") + dateFolder + file.getName());
                            } else {
                                sourceFile = new File(prop.getProperty("inttra315Directory") + file.getName());
                                destFile = new File(prop.getProperty("inttra315Archive") + dateFolder + file.getName());
                            }
                            if (saveFlag) {
                                xmlFiles.move(sourceFile, destFile);
                            } else {
                                if (osName().contains("linux")) {
                                    destFile = new File(prop.getProperty("linuxInttra315Unprocessed") + dateFolder + file.getName());
                                } else {
                                    destFile = new File(prop.getProperty("inttra315Unprocessed") + dateFolder + file.getName());
                                }
                                xmlFiles.move(sourceFile, destFile);
                            }
                            if (CommonUtils.isNotEmpty(this.getDockReceiptNumber()) && null != this.getShpsta()) {
                                drNo = this.getDockReceiptNumber();
                                ediTrackingBC.setShipmentStatusLog(file.getName(), currentDate, "success", "No Error", company, messageType, drNo, this.getTrackingStatus(), this.getShpsta());
                            }
                        }

                    } catch (Exception e) {
                        log.info("execute failed on " + new Date(), e);
                        errorMessage = e.toString();
                        if (osName().contains("linux")) {
                            sourceFile = new File(prop.getProperty("linuxInttra315Directory") + file.getName());
                            destFile = new File(prop.getProperty("linuxInttra315Unprocessed") + dateFolder + file.getName());
                        } else {
                            sourceFile = new File(prop.getProperty("inttra315Directory") + file.getName());
                            destFile = new File(prop.getProperty("inttra315Unprocessed") + dateFolder + file.getName());
                        }
                        xmlFiles.move(sourceFile, destFile);
                        if (this.getDockReceiptNumber() != null) {
                            ediTrackingBC.setEdiLog(file.getName(), currentDate, "failed", "errorMessage", company, messageType, this.getDockReceiptNumber(), "", "", null);
                            logFileWriter.doAppend("Error while reading XML file " + fileName + " Type of Error "
                                    + "is---" + e, "error_logfile_" + fileName + "_" + currentDate + ".txt", company, osName(), messageType);
                        }
                    }
                }//end of for loop    
            }//end of if
        } catch (Exception e) {
            log.info("Error inside class XmlToCsv  execute() method. ", e);
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
                log.info("endDocument failed on " + new Date(), e);
                //        	throw new SAXException("I/O error", e);
            } catch (Exception ex) {
                log.info("endDocument failed on " + new Date(), e);
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

    public void characters(char buf[], int offset, int len)
            throws SAXException {
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
        Shploc shploc = new Shploc();
        Shpsta shpsta = new Shpsta();
        int bolLimit = 0;
        int polLimit = 0;
        int sndLimit = 0;
        int rcptLimit = 0;
        int carNameLimit = 0;
        int carAddrLimit = 0;
        boolean cobJobOn = LoadJobProperty.getJobStatus("Automatic Confirm on Board Reminders");
        for (Enumeration e = v.elements(); e.hasMoreElements();) {
            String str = e.nextElement().toString();
            //System.out.println("str "+str);
            if (str.indexOf("Message-Header-DocumentIdentifier") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String msgid = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (msgid != null && !msgid.trim().equals("")) {
                    if (msgid.length() > 35) {
                        strError = "Length of the MessageId is more than 35 characters";
                    } else {
                        shpsta.setMsgid(msgid);
                        shploc.setMsgid(msgid);
                    }
                }
            }
            if (str.indexOf("Message-Header-DateTime::DateType") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String strdatetime = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                String msgdte = "";
                String msgtim = "";
                //System.out.println("strdatetime "+strdatetime.length());
                if (strdatetime != null && !strdatetime.trim().equals("")) {
                    if (strdatetime.length() > 10) {
                        strError = "Length of the MessageDate is more than 10 characters";
                    } else {
                        msgdte = strdatetime.substring(0, 6);
                        if (msgdte != null && msgdte.length() == 6) {
                            msgdte = "20" + msgdte;
                        }
                        shpsta.setMsgdte(Integer.parseInt(msgdte));
                        shploc.setMsgdte(Integer.parseInt(msgdte));
                        if (strdatetime.length() > 6) {
                            msgtim = strdatetime.substring(6, strdatetime.length());
                            if (msgtim != null && msgtim.length() <= 4) {
                                msgtim += "00";
                            }
                            shpsta.setMsgtim(Integer.parseInt(msgtim));
                            shploc.setMsgtim(Integer.parseInt(msgtim));
                        }
                    }
                }
            }//end of Message-Header-DateTime::DateType if condition
            if (str.indexOf("PartnerIdentifier::Agency=\"AssignedBySender\"") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String sndidt = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (sndidt != null && !sndidt.trim().equals("")) {
                    if (sndidt.length() > 35) {
                        strError = "Length of the MessageSender is more than 35 characters";
                    } else {
                        shpsta.setSndidt(sndidt);
                        shploc.setSndidt(sndidt);
                    }
                }
            }//end of PartnerIdentifier::Agency=\"AssignedBySender\"
            if (str.indexOf("PartnerIdentifier::Agency=\"AssignedByRecipient\"") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String rcpidt = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (rcpidt != null && !rcpidt.trim().equals("")) {
                    if (rcpidt.length() > 35) {
                        strError = "Length of the MessageRecipient is more than 35 characters";
                    } else {
                        shpsta.setRcpidt(rcpidt);
                        shploc.setRcpidt(rcpidt);
                    }
                }
            }//end of PartnerIdentifier::Agency=\"AssignedByRecipient\"
            if (str.indexOf("PartnerRole=\"Sender\"-PartnerName") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String strSndName = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (strSndName != null && !strSndName.trim().equals("")) {
                    if (strSndName.length() > 35) {
                        strError = "Length of the Sender Name is more than 35 characters";
                    } else {
                        if (sndLimit < 2) {
                            shpsta.setSndnm(strSndName, sndLimit);
                            sndLimit++;
                        }
                    }
                }
            }//end of PartnerRole=\"Sender\"-PartnerName
            if (str.indexOf("PartnerRole=\"Recipient\"-PartnerName") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String strRcptName = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (strRcptName != null && !strRcptName.trim().equals("")) {
                    if (strRcptName.length() > 35) {
                        strError = "Length of the Recipient Name is more than 35 characters";
                    } else {
                        if (rcptLimit < 2) {
                            shpsta.setSndnm(strRcptName, rcptLimit);
                            rcptLimit++;
                        }
                    }
                }
            }//end of PartnerRole=\"Recipient\"-PartnerName
            if (str.indexOf("PartnerRole=\"Sender\"-ContactInformation-ContactName") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String sndcon = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (sndcon != null && !sndcon.trim().equals("")) {
                    if (sndcon.length() > 35) {
                        strError = "Length of the Sender Contact Name is more than 35 characters";
                    } else {
                        shpsta.setSndcon(sndcon);
                    }
                }
            }//end of PartnerRole=\"Sender\"-ContactInformation-ContactName
            if (str.indexOf("PartnerRole=\"Sender\"-ContactInformation-CommunicationValue::CommunicationType=\"Telephone\"") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String sndtel = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (sndtel != null && !sndtel.trim().equals("")) {
                    if (sndtel.length() > 50) {
                        strError = "Length of the Sender Telephone is more than 50 characters";
                    } else {
                        shpsta.setSndtel(sndtel);
                    }
                }
            }//end of PartnerRole=\"Sender\"-ContactInformation-CommunicationValue::CommunicationType=\"Telephone\
            if (str.indexOf("PartnerRole=\"Sender\"-ContactInformation-CommunicationValue::CommunicationType=\"Fax\"") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String sndfax = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (sndfax != null && !sndfax.trim().equals("")) {
                    if (sndfax.length() > 50) {
                        strError = "Length of the Sender Fax is more than 50 characters";
                    } else {
                        shpsta.setSndfax(sndfax);
                    }
                }
            }//end of PartnerRole=\"Sender\"-ContactInformation-CommunicationValue::CommunicationType=\"Fax\
            if (str.indexOf("PartnerRole=\"Sender\"-ContactInformation-CommunicationValue::CommunicationType=\"Email\"") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String sndemail = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (sndemail != null && !sndemail.trim().equals("")) {
                    if (sndemail.length() > 50) {
                        strError = "Length of the Sender Email is more than 50 characters";
                    } else {
                        shpsta.setSndeml(sndemail);
                    }
                }
            }//end of PartnerRole=\"Sender\"-ContactInformation-CommunicationValue::CommunicationType=\"Email\
            if (str.indexOf("PartnerRole=\"Recipient\"-ContactInformation-ContactName") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String rcpcon = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (rcpcon != null && !rcpcon.trim().equals("")) {
                    if (rcpcon.length() > 35) {
                        strError = "Length of the Recipient Contact Name is more than 35 characters";
                    } else {
                        shpsta.setRcpcon(rcpcon);
                    }
                }
            }//end of PartnerRole=\"Recipient\"-ContactInformation-ContactName
            if (str.indexOf("PartnerRole=\"Recipient\"-ContactInformation-CommunicationValue::CommunicationType=\"Telephone\"") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String rcptel = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (rcptel != null && !rcptel.trim().equals("")) {
                    if (rcptel.length() > 50) {
                        strError = "Length of the  Recipient Telephone is more than 50 characters";
                    } else {
                        shpsta.setRcptel(rcptel);
                    }
                }
            }//end of PartnerRole=\"Recipient\"-ContactInformation-CommunicationValue::CommunicationType=\"Telephone\
            if (str.indexOf("PartnerRole=\"Recipient\"-ContactInformation-CommunicationValue::CommunicationType=\"Fax\"") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String rcpfax = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (rcpfax != null && !rcpfax.trim().equals("")) {
                    if (rcpfax.length() > 50) {
                        strError = "Length of the Recipient Fax is more than 50 characters";
                    } else {
                        shpsta.setRcpfax(rcpfax);
                    }
                }
            }//end of PartnerRole=\"Recipient\"-ContactInformation-CommunicationValue::CommunicationType=\"Fax\
            if (str.indexOf("PartnerRole=\"Recipient\"-ContactInformation-CommunicationValue::CommunicationType=\"Email\"") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String rcpemail = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (rcpemail != null && !rcpemail.trim().equals("")) {
                    if (rcpemail.length() > 50) {
                        strError = "Length of the Sender Email is more than 50 characters";
                    } else {
                        shpsta.setRcpeml(rcpemail);
                    }
                }
            }//end of PartnerRole=\"Recipient\"-ContactInformation-CommunicationValue::CommunicationType=\"Email\
            if (str.indexOf("Message-Header-MessageType") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String msgtyp = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (msgtyp != null && !msgtyp.trim().equals("")) {
                    if (msgtyp.length() > 35) {
                        strError = "Length of the Message Type is more than 35 characters";
                    } else {
                        shpsta.setMsgtyp(msgtyp);
                    }
                }
                if (str.indexOf("Message-Header-MessageType::MessageVersion") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                    String msgver = str.substring(str.lastIndexOf("MessageVersion=\"") + 16,
                            str.lastIndexOf("\"=CONGRUENCE="));
                    if (msgver != null && !msgver.trim().equals("")) {
                        if (msgver.length() > 10) {
                            strError = "Length of the Message Version is more than 10 characters";
                        } else {
                            shpsta.setMsgver(msgver);
                        }
                    }
                }
            }//end of Message-Header-MessageType if condition
            if (str.indexOf("Message-MessageBody-MessageProperties-EventCode") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String eventCode = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (eventCode != null && !eventCode.trim().equals("")) {
                    if (eventCode.length() > 3) {
                        strError = "Length of the Event Code is more than 3 characters";
                    } else {
                        shpsta.setEvncod(eventCode);
                    }
                }
            }//end of Message-MessageBody-MessageProperties-EventCode if condition
            if (str.indexOf("EventLocation-Location::LocationType=\"") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String congval = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (congval != null && congval.trim().equals("")) {
                    String loctype = str.substring(str.lastIndexOf("Location::LocationType=\"") + 24,
                            str.lastIndexOf("\"=CONGRUENCE"));
                    if (loctype != null && !loctype.trim().equals("")) {
                        if (loctype.length() > 35) {
                            strError = "Length of the Event Location Type is more than 35 characters";
                        } else {
                            shpsta.setEvltyp(loctype);
                        }
                    }
                }
            }//end of EventLocation-Location::"LocationType if condition
            if (str.indexOf("EventLocation-Location::"
                    + "LocationType=\"ActivityLocation\"-LocationCode::Agency") >= 0) {
                String agency = str.substring(str.lastIndexOf("LocationCode::Agency=\"") + 22,
                        str.lastIndexOf("\"=CONGRUENCE"));
                if (agency != null && !agency.trim().equals("")) {
                    if (agency.length() > 35) {
                        strError = "Length of the Event Location Code Qualifier is more than 35 characters";
                    } else {
                        shpsta.setEvnlqa(agency);
                    }
                }
            }//end of EventLocation-Location::"LocationType=\"ActivityLocation\"-LocationCode::Agency if condition
            if (str.indexOf("EventLocation-Location") >= 0
                    && str.indexOf("-LocationCode") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String evlcva = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (evlcva != null && !evlcva.trim().equals("")) {
                    if (evlcva.length() > 35) {
                        strError = "Length of the Event Location Code/ScheduleD/ScheduleK is more than 35 characters";
                    } else {
                        shpsta.setEvlcva(evlcva);
                    }
                }
            }//end of EventLocation-Location -LocationCode if condition
            if (str.indexOf("EventLocation-Location") >= 0
                    && str.indexOf("-LocationName") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String locnam = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (locnam != null && !locnam.trim().equals("")) {
                    if (locnam.length() > 256) {
                        strError = "Length of the Location Name is more than 256 characters";
                    } else {
                        shpsta.setLocnam(locnam);
                    }
                }
            }//end of EventLocation-Location -LocationName if condition
            if (str.indexOf("EventLocation-Location") >= 0
                    && str.indexOf("-LocationCountry") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String locntr = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (locntr != null && !locntr.trim().equals("")) {
                    if (locntr.length() > 25) {
                        strError = "Length of the Location Country Code/Country Name is more than 25 characters";
                    } else {
                        shpsta.setLocntr(locntr);
                    }
                }
            }//end of EventLocation-Location -LocationCountry if condition
            if (str.indexOf("EventLocation-Location") >= 0 && str.indexOf("DateTime") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String strdatetime = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                String msgdte = "";
                String msgtim = "";
                if (strdatetime != null && !strdatetime.trim().equals("")) {
                    if (strdatetime.length() > 12) {
                        strError = "Length of the Event Date is more than 12 characters";
                    } else {
                        msgdte = strdatetime.substring(0, 8);
                        shpsta.setEvndat(Integer.parseInt(msgdte));
                        if (strdatetime.length() > 8) {
                            msgtim = strdatetime.substring(8, strdatetime.length());
                            if (msgtim != null && msgtim.length() <= 4) {
                                msgtim += "00";
                            }
                            shpsta.setEvntim(Integer.parseInt(msgtim));
                        }
                    }
                }
            }//end of Even-DateTime if condition
            if (str.indexOf("EventLocation-Location") >= 0 && str.indexOf("DateTime::DateType=\"") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String evdtyp = str.substring(str.lastIndexOf("DateTime::DateType=\"") + 20,
                        str.lastIndexOf("\"=CONGRUENCE"));
                if (evdtyp != null && !evdtyp.trim().equals("")) {
                    if (evdtyp.length() > 35) {
                        strError = "Length of the Event DateTime Qualifier/Date Type is more than 35 characters";
                    } else {
                        shpsta.setEvdtyp(evdtyp);
                    }
                }
            }//end of Even-DateTime::DateType if condition
            if (str.indexOf("Even-ReferenceInformation::ReferenceType=\"BookingNumber\"") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String bkgnum = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (bkgnum != null && !bkgnum.trim().equals("")) {
                    if (bkgnum.length() > 35) {
                        strError = "Length of the Booking Number is more than 35 characters";
                    }
                    shpsta.setBkgnum(bkgnum);
                    String voyageNo = new LclSSMasterBlDAO().findVoyageNoByBkgNo(bkgnum);
                    if(CommonUtils.isNotEmpty(voyageNo)){
                        Object[] siIdAndFileName = (Object[]) logFileEdiDAO.findSiIdAndFileName(voyageNo+"_"+bkgnum.replace(" ", ""));
                        if (null != siIdAndFileName && siIdAndFileName.length == 2) {
                            lclFlag = true;
                            this.setDockReceiptNumber(voyageNo);
                            saveFlag = true;
                        }
                    }
                    if(!lclFlag){
                        String drNumber = logFileEdiDAO.findDrNumberFromBooking(bkgnum);
                        this.setDockReceiptNumber(drNumber);
                        if (CommonUtils.isNotEmpty(drNumber)) {
                            saveFlag = true;
                        } else {
                            saveFlag = false;
                            break;
                        }
                    }
                }
            }//end of Even-ReferenceInformation::ReferenceType=\"BookingNumber\""
            if (str.indexOf("Even-ReferenceInformation::ReferenceType=\"BillOfLadingNumber\"") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String strBill = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (strBill != null && !strBill.trim().equals("")) {
                    if (strBill.length() > 35) {
                        strError = "Length of the Bill Of Ladding Number is more than 35 characters";
                    } else {
                        if (bolLimit < 10) {
                            shpsta.setBlldg(strBill, bolLimit);
                            bolLimit++;
                        }
                    }
                }
            }//end of Even-ReferenceInformation::ReferenceType=\"BillOfLaddingNumber\""
            if (str.indexOf("Even-ReferenceInformation::ReferenceType=\"ConsigneeOrderNumber\"") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String conord = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (conord != null && !conord.trim().equals("")) {
                    if (conord.length() > 35) {
                        strError = "Length of the Consignee Order Number is more than 35 characters";
                    } else {
                        shpsta.setConord(conord);
                    }
                }
            }//end of Even-ReferenceInformation::ReferenceType=\"ConsigneeOrderNumber\""
            if (str.indexOf("Even-ReferenceInformation::ReferenceType=\"ContractNumber\"") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String cntrnu = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (cntrnu != null && !cntrnu.trim().equals("")) {
                    if (cntrnu.length() > 35) {
                        strError = "Length of the Contract Number is more than 35 characters";
                    } else {
                        shpsta.setCntrnu(cntrnu);
                    }
                }
            }//end of Even-ReferenceInformation::ReferenceType=\"ContractNumber\""
            if (str.indexOf("Even-ReferenceInformation::ReferenceType=\"ContractPartyReferenceNumber\"") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String cntrpa = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (cntrpa != null && !cntrpa.trim().equals("")) {
                    if (cntrpa.length() > 35) {
                        strError = "Length of the Contract Party Reference Number is more than 35 characters";
                    } else {
                        shpsta.setCntrpa(cntrpa);
                    }
                }
            }//end of Even-ReferenceInformation::ReferenceType=\"ContractPartyReferenceNumber\""
            if (str.indexOf("Even-ReferenceInformation::ReferenceType=\"ConsigneeReferenceNumber\"") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String conref = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (conref != null && !conref.trim().equals("")) {
                    if (conref.length() > 35) {
                        strError = "Length of the Consignee Reference Number is more than 35 characters";
                    } else {
                        shpsta.setConref(conref);
                    }
                }
            }//end of Even-ReferenceInformation::ReferenceType=\"ConsigneeReferenceNumber\""
            if (str.indexOf("Even-ReferenceInformation::ReferenceType=\"FreightForwarderReferenceNumber\"") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String frtref = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (frtref != null && !frtref.trim().equals("")) {
                    if (frtref.length() > 35) {
                        strError = "Length of the Freight Forwarder Reference Number is more than 35 characters";
                    } else {
                        shpsta.setFrtref(frtref);
                    }
                }
            }//end of Even-ReferenceInformation::ReferenceType=\"FreightForwarderReferenceNumber\""
            if (str.indexOf("Even-ReferenceInformation::ReferenceType=\"PurchaseOrderNumber\"") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String strPol = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (strPol != null && !strPol.trim().equals("")) {
                    if (strPol.length() > 35) {
                        strError = "Length of the Purchase Order Number is more than 35 characters";
                    } else {
                        if (polLimit < 10) {
                            shpsta.setPonum(strPol, polLimit);
                            polLimit++;
                        }
                    }
                }
            }//end of Even-ReferenceInformation::ReferenceType=\"PurchaseOrderNumber\""
            if (str.indexOf("Even-ReferenceInformation::ReferenceType=\"ShipperIdentifyingNumber\"") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String shpide = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (shpide != null && !shpide.trim().equals("")) {
                    if (shpide.length() > 35) {
                        strError = "Length of the Shipper Identifying Number is more than 35 characters";
                    } else {
                        shpsta.setShpide(shpide);
                    }
                }
            }//end of Even-ReferenceInformation::ReferenceType=\"ShipperIdentifyingNumber\""
            if (str.indexOf("Even-ReferenceInformation::ReferenceType=\"InttraBookingNumber\"") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String intrbk = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (intrbk != null && !intrbk.trim().equals("")) {
                    if (intrbk.length() > 35) {
                        strError = "Length of the Inttra Booking Number is more than 35 characters";
                    } else {
                        shpsta.setIntrbk(intrbk);
                    }
                }
            }//end of Even-ReferenceInformation::ReferenceType=\"InttraBookingNumber\""
            if (str.indexOf("Even-Instructions-ShipmentComments::CommentType=\"") >= 0) {
                String shpcmt = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (shpcmt != null && !shpcmt.trim().equals("")) {
                    if (shpcmt.length() > 70) {
                        strError = "Length of the Shipment Comments is more than 70 characters";
                    } else {
                        shpsta.setShpcmt(shpcmt);
                    }
                }
            }//end of Even-Instructions-ShipmentComments::CommentType=\ if condition
            if (str.indexOf("TransportationDetails::TransportStage=\"") >= 0
                    && str.indexOf("TransportMode=\"") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0
                    && str.indexOf("ConveyanceInformation") < 0 && str.indexOf("Location::LocationType") < 0) {
                String congval = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (congval != null && congval.trim().equals("")) {
                    String trnstg = str.substring(str.lastIndexOf("TransportStage=\"") + 16,
                            str.lastIndexOf("\"::TransportMode"));
                    String trnmod = str.substring(str.lastIndexOf("TransportMode=\"") + 15,
                            str.lastIndexOf("\"=CONGRUENCE"));
                    if (trnstg != null && !trnstg.trim().equals("")) {
                        if (trnstg.length() > 35) {
                            strError = "Length of the Transport Stage is more than 35 characters";
                        } else {
                            shpsta.setTrnstg(trnstg);
                        }
                    }
                    if (trnmod != null && !trnmod.trim().equals("")) {
                        if (trnmod.length() > 35) {
                            strError = "Length of the Transport Mode is more than 35 characters";
                        } else {
                            shpsta.setTrnmod(trnmod);
                        }
                    }
                }
            }//end of EventLocation-Location::"LocationType if condition
            if (str.indexOf("ConveyanceInformation-ConveyanceName") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String vesnam = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (vesnam != null && !vesnam.trim().equals("")) {
                    if (vesnam.length() > 35) {
                        strError = "Length of the Vessel Name is more than 35 characters";
                    } else {
                        shpsta.setVesnam(vesnam);
                    }
                }
            }//end of ConveyanceInformation-ConveyanceName if condition
            if (str.indexOf("ConveyanceInformation-VoyageTripNumber") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String voynum = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (voynum != null && !voynum.trim().equals("")) {
                    if (voynum.length() > 35) {
                        strError = "Length of the Voyage Number is more than 35 characters";
                    } else {
                        shpsta.setVoynum(voynum);
                    }
                }
            }//end of ConveyanceInformation-VoyageTripNumber if condition
            if (str.indexOf("ConveyanceInformation-CarrierSCAC") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String scac = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (scac != null && !scac.trim().equals("")) {
                    if (scac.length() > 4) {
                        strError = "Length of the Carrier SCAC is more than 4 characters";
                    } else {
                        shpsta.setScac(scac);
                    }
                }
            }//end of ConveyanceInformation-CarrierSCAC if condition
            if (str.indexOf("ConveyanceInformation-TransportIdentification::TransportIdentificationType=\"LloydsCode\"") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String llycod = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (llycod != null && !llycod.trim().equals("")) {
                    if (llycod.length() > 9) {
                        strError = "Length of the L1oyds Code is more than 9 characters";
                    } else {
                        shpsta.setLlycod(llycod);
                    }
                }
            }
            if (str.indexOf("ConveyanceInformation-TransportIdentification::TransportIdentificationType=\"ShipRadioCallSignal\"") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String shprcs = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (shprcs != null && !shprcs.trim().equals("")) {
                    if (shprcs.length() > 9) {
                        strError = "Length of the Ship Radio Call Signal is more than 9 characters";
                    } else {
                        shpsta.setShprcs(shprcs);
                    }
                }
            }
            if (str.indexOf("EquipmentDetails-LineNumber") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String lnenum = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (lnenum != null && !lnenum.trim().equals("")) {
                    if (lnenum.length() > 3) {
                        strError = "Length of the Ship Radio Call Signal is more than 3 characters";
                    } else {
                        shpsta.setLnenum(lnenum);
                    }
                }
            }// end of EquipmentDetails-LineNumber if condition
            if (str.indexOf("EquipmentDetails-EquipmentIdentifier::LoadType=\"") >= 0) {
                String cntsta = str.substring(str.lastIndexOf("EquipmentIdentifier::LoadType=\"") + 31,
                        str.lastIndexOf("\"=CONGRUENCE"));
                if (cntsta != null && !cntsta.trim().equals("")) {
                    if (cntsta.length() > 35) {
                        strError = "Length of the Load Type/Container Status is more than 35 characters";
                    } else {
                        shpsta.setCntsta(cntsta);
                    }
                }
            }// end of EquipmentDetails-EquipmentIdentifier::LoadType=\" if// condition
            if (str.indexOf("EquipmentDetails-EquipmentIdentifier") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String cntnum = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (cntnum != null && !cntnum.trim().equals("")) {
                    if (cntnum.length() > 17) {
                        strError = "Length of the Container Number is more than 17 characters";
                    } else {
                        shpsta.setCntnum(cntnum);
                    }
                }
            }//end of EquipmentDetails-EquipmentIdentifier if condition
            if (str.indexOf("EquipmentDetails-EquipmentType-EquipmentTypeCode") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String cnttyp = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (cnttyp != null && !cnttyp.trim().equals("")) {
                    if (cnttyp.length() > 10) {
                        strError = "Length of the Equipment Type Code/Container Type is more than 10 characters";
                    } else {
                        shpsta.setCnttyp(cnttyp);
                    }
                }
            }// end of EquipmentDetails-EquipmentType if condition
            //CODING FOR THE TABLE SHPLOC STARTS
            if (str.indexOf("Location::"
                    + "LocationType=\"PlaceOfReceipt\"-LocationCode::Agency") >= 0) {
                String plrcqa = str.substring(str.lastIndexOf("LocationCode::Agency=\"") + 22,
                        str.lastIndexOf("\"=CONGRUENCE"));
                if (plrcqa != null && !plrcqa.trim().equals("")) {
                    if (plrcqa.length() > 35) {
                        strError = "Length of the Place Of Receipt Location Code Qualifier is more than 35 characters";
                    } else {
                        shploc.setPlrcqa(plrcqa);
                    }
                }
            }
            if (str.indexOf("LocationType=\"PlaceOfReceipt\"") >= 0
                    && str.indexOf("-LocationCode") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String plrcod = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (plrcod != null && !plrcod.trim().equals("")) {
                    if (plrcod.length() > 35) {
                        strError = "Length of the Place Of Receipt Code/ScheduleD/ScheduleK is more than 35 characters";
                    } else {
                        shploc.setPlrcod(plrcod);
                    }
                }
            }//end of LocationType=\"PlaceOfReceipt\" -LocationCode if condition
            if (str.indexOf("LocationType=\"PlaceOfReceipt\"") >= 0
                    && str.indexOf("-LocationName") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String plrnam = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (plrnam != null && !plrnam.trim().equals("")) {
                    if (plrnam.length() > 256) {
                        strError = "Length of the PlaceOfReceipt Location Name is more than 256 characters";
                    } else {
                        shploc.setPlrnam(plrnam);
                    }
                }
            }
            if (str.indexOf("LocationType=\"PlaceOfReceipt\"") >= 0
                    && str.indexOf("-LocationCountry") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String plrcry = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (plrcry != null && !plrcry.trim().equals("")) {
                    if (plrcry.length() > 25) {
                        strError = "Length of the Place Of Receipt Location Country Code/Country Name is more than 25 characters";
                    } else {
                        shploc.setPlrcry(plrcry);
                    }
                }
            }//end of LocationType=\"PlaceOfReceipt\ -LocationCountry if condition
            if (str.indexOf("LocationType=\"PlaceOfReceipt\"") >= 0 && str.indexOf("DateTime") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String strdatetime = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                String msgdte = "";
                String msgtim = "";
                if (strdatetime != null && !strdatetime.trim().equals("")) {
                    if (strdatetime.length() > 12) {
                        strError = "Length of the Place Of Receipt Date is more than 12 characters";
                    } else {
                        msgdte = strdatetime.substring(0, 8);
                        shploc.setPlrdat(Integer.parseInt(msgdte));
                        if (strdatetime.length() > 8) {
                            msgtim = strdatetime.substring(8, strdatetime.length());
                            if (msgtim != null && msgtim.length() <= 4) {
                                msgtim += "00";
                            }
                            shploc.setPlrtim(Integer.parseInt(msgtim));
                        }
                    }
                }
            }//end of LocationType=\"PlaceOfReceipt\" DateTime if condition
            if (str.indexOf("LocationType=\"PlaceOfReceipt\"") >= 0 && str.indexOf("DateTime::DateType=\"") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String plrdqa = str.substring(str.lastIndexOf("DateTime::DateType=\"") + 20,
                        str.lastIndexOf("\"=CONGRUENCE"));
                if (plrdqa != null && !plrdqa.trim().equals("")) {
                    if (plrdqa.length() > 35) {
                        strError = "Length of the Place Of Receipt DateTime Qualifier is more than 35 characters";
                    } else {
                        shploc.setPlrdqa(plrdqa);
                    }
                }
            }//end of LocationType=\"PlaceOfReceipt\ DateTime::DateType if condition
            if (str.indexOf("Location::"
                    + "LocationType=\"PortOfLoading\"-LocationCode::Agency") >= 0) {
                String polcqa = str.substring(str.lastIndexOf("LocationCode::Agency=\"") + 22,
                        str.lastIndexOf("\"=CONGRUENCE"));
                if (polcqa != null && !polcqa.trim().equals("")) {
                    if (polcqa.length() > 35) {
                        strError = "Length of the Port Of Loading Location Code Qualifier is more than 35 characters";
                    } else {
                        shploc.setPolcqa(polcqa);
                    }
                }
            }
            if (str.indexOf("LocationType=\"PortOfLoading\"") >= 0
                    && str.indexOf("-LocationCode") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String polcod = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (polcod != null && !polcod.trim().equals("")) {
                    if (polcod.length() > 35) {
                        strError = "Length of the Port Of Loading Code/ScheduleD/ScheduleK is more than 35 characters";
                    } else {
                        shploc.setPolcod(polcod);
                    }

                }
            }//end of LocationType=\"PortOfLoading\" -LocationCode if condition
            if (str.indexOf("LocationType=\"PortOfLoading\"") >= 0
                    && str.indexOf("-LocationName") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String polnam = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (polnam != null && !polnam.trim().equals("")) {
                    if (polnam.length() > 256) {
                        strError = "Length of the Port Of Loading Location Name is more than 256 characters";
                    } else {
                        shploc.setPolnam(polnam);
                    }
                }
            }
            if (str.indexOf("LocationType=\"PortOfLoading\"") >= 0
                    && str.indexOf("-LocationCountry") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String polcry = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (polcry != null && !polcry.trim().equals("")) {
                    if (polcry.length() > 25) {
                        strError = "Length of the Port Of Loading Location Country Code/Country Name is more than 25 characters";
                    } else {
                        shploc.setPolcry(polcry);
                    }
                }
            }// end of LocationType=\"PortOfLoading\ -LocationCountry if
            if (str.indexOf("LocationType=\"PortOfLoading\"") >= 0 && str.indexOf("DateTime") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String strdatetime = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                String msgdte = "";
                String msgtim = "";
                if (strdatetime != null && !strdatetime.trim().equals("")) {
                    if (strdatetime.length() > 12) {
                        strError = "Length of the Port Of Loading Date is more than 12 characters";
                    } else {
                        msgdte = strdatetime.substring(0, 8);
                        shploc.setPoldat(Integer.parseInt(msgdte));
                        if (strdatetime.length() > 8) {
                            msgtim = strdatetime.substring(8, strdatetime.length());
                            if (msgtim != null && msgtim.length() <= 4) {
                                msgtim += "00";
                            }
                            shploc.setPoltim(Integer.parseInt(msgtim));
                        }
                    }
                }
            }//end of LocationType=\"PortOfLoading\" DateTime if condition
            if (str.indexOf("LocationType=\"PortOfLoading\"") >= 0 && str.indexOf("DateTime::DateType=\"") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String poldqa = str.substring(str.lastIndexOf("DateTime::DateType=\"") + 20,
                        str.lastIndexOf("\"=CONGRUENCE"));
                if (poldqa != null && !poldqa.trim().equals("")) {
                    if (poldqa.length() > 35) {
                        strError = "Length of the Port Of Loading DateTime Qualifier is more than 35 characters";
                    } else {
                        shploc.setPoldqa(poldqa);
                    }
                }
            }//end of LocationType=\"PortOfLoading\ DateTime::DateType if condition
            if (str.indexOf("Location::"
                    + "LocationType=\"PortOfDischarge\"-LocationCode::Agency") >= 0) {
                String podcqa = str.substring(str.lastIndexOf("LocationCode::Agency=\"") + 22,
                        str.lastIndexOf("\"=CONGRUENCE"));
                if (podcqa != null && !podcqa.trim().equals("")) {
                    if (podcqa.length() > 35) {
                        strError = "Length of the Port Of Discharge Location Code Qualifier is more than 35 characters";
                    } else {
                        shploc.setPodcqa(podcqa);
                    }
                }
            }
            if (str.indexOf("LocationType=\"PortOfDischarge\"") >= 0
                    && str.indexOf("-LocationCode") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String podcod = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (podcod != null && !podcod.trim().equals("")) {
                    if (podcod.length() > 35) {
                        strError = "Length of the Port Of Discharge Code/ScheduleD/ScheduleK is more than 35 characters";
                    } else {
                        shploc.setPodcod(podcod);
                    }
                }
            }// end of LocationType=\"PortOfDischarge\" -LocationCode if condition
            if (str.indexOf("LocationType=\"PortOfDischarge\"") >= 0
                    && str.indexOf("-LocationName") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String podnam = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (podnam != null && !podnam.trim().equals("")) {
                    if (podnam.length() > 256) {
                        strError = "Length of the Port Of Discharge Location Name is more than 256 characters";
                    } else {
                        shploc.setPodnam(podnam);
                    }
                }
            }//end of LocationType=\"PortOfDischarge\ -LocationName if condition
            if (str.indexOf("LocationType=\"PortOfDischarge\"") >= 0
                    && str.indexOf("-LocationCountry") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String podcry = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (podcry != null && !podcry.trim().equals("")) {
                    if (podcry.length() > 25) {
                        strError = "Length of the Port Of Discharge Location Country Code/Country Name is more than 25 characters";
                    } else {
                        shploc.setPodcry(podcry);
                    }
                }
            }//end of LocationType=\"PortOfDischarge\ -LocationCountry if condition
            if (str.indexOf("LocationType=\"PortOfDischarge\"") >= 0 && str.indexOf("DateTime") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String strdatetime = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                String msgdte = "";
                String msgtim = "";
                if (strdatetime != null && !strdatetime.trim().equals("")) {
                    if (strdatetime.length() > 12) {
                        strError = "Length of the Port Of Discharge Date is more than 12 characters";
                    } else {
                        msgdte = strdatetime.substring(0, 8);
                        shploc.setPoddat(Integer.parseInt(msgdte));

                        if (strdatetime.length() > 8) {
                            msgtim = strdatetime.substring(8, strdatetime.length());
                            if (msgtim != null && msgtim.length() <= 4) {
                                msgtim += "00";
                            }
                            shploc.setPodtim(Integer.parseInt(msgtim));
                        }
                    }
                }
            }//end of LocationType=\"PortOfDischarge\" DateTime if condition
            if (str.indexOf("LocationType=\"PortOfDischarge\"") >= 0 && str.indexOf("DateTime::DateType=\"") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String poddqa = str.substring(str.lastIndexOf("DateTime::DateType=\"") + 20,
                        str.lastIndexOf("\"=CONGRUENCE"));
                if (poddqa != null && !poddqa.trim().equals("")) {
                    if (poddqa.length() > 35) {
                        strError = "Length of the Port Of Discharge DateTime Qualifier is more than 35 characters";
                    } else {
                        shploc.setPoddqa(poddqa);
                    }
                }
            }// end of LocationType=\"PortOfDischarge\ DateTime::DateType if
            if (str.indexOf("Location::"
                    + "LocationType=\"PlaceOfDelivery\"-LocationCode::Agency") >= 0) {
                String pldcqa = str.substring(str.lastIndexOf("LocationCode::Agency=\"") + 22,
                        str.lastIndexOf("\"=CONGRUENCE"));
                if (pldcqa != null && !pldcqa.trim().equals("")) {
                    if (pldcqa.length() > 35) {
                        strError = "Length of the Place Of Delivery Location Code Qualifier is more than 35 characters";
                    } else {
                        shploc.setPldcqa(pldcqa);
                    }
                }
            }//end of Location::"LocationType=\"PlaceOfDelivery\"-LocationCode::Agency if condition
            if (str.indexOf("LocationType=\"PlaceOfDelivery\"") >= 0
                    && str.indexOf("-LocationCode") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String pldcod = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (pldcod != null && !pldcod.trim().equals("")) {
                    if (pldcod.length() > 35) {
                        strError = "Length of the Place Of Delivery Code/ScheduleD/ScheduleK is more than 35 characters";
                    } else {
                        shploc.setPldcod(pldcod);
                    }
                }
            }// end of LocationType=\"PlaceOfDelivery\" -LocationCode if
            if (str.indexOf("LocationType=\"PlaceOfDelivery\"") >= 0
                    && str.indexOf("-LocationName") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String pldnam = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (pldnam != null && !pldnam.trim().equals("")) {
                    if (pldnam.length() > 256) {
                        strError = "Length of the Port Of Discharge Location Name is more than 256 characters";
                    } else {
                        shploc.setPldnam(pldnam);
                    }
                }
            }// end of LocationType=\"PlaceOfDelivery\ -LocationName if
            if (str.indexOf("LocationType=\"PlaceOfDelivery\"") >= 0
                    && str.indexOf("-LocationCountry") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String pldcry = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (pldcry != null && !pldcry.trim().equals("")) {
                    if (pldcry.length() > 25) {
                        strError = "Length of the Place Of Delivery Location Country Code/Country Name is more than 25 characters";
                    } else {
                        shploc.setPldcry(pldcry);
                    }
                }
            }//end of LocationType=\"PlaceOfDelivery\ -LocationCountry if condition
            if (str.indexOf("LocationType=\"PlaceOfDelivery\"") >= 0 && str.indexOf("DateTime") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String strdatetime = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                String msgdte = "";
                String msgtim = "";
                if (strdatetime != null && !strdatetime.trim().equals("")) {
                    if (strdatetime.length() > 12) {
                        strError = "Length of the Place Of Delivery Date is more than 12 characters";
                    } else {
                        msgdte = strdatetime.substring(0, 8);
                        shploc.setPlddat(Integer.parseInt(msgdte));

                        if (strdatetime.length() > 8) {
                            msgtim = strdatetime.substring(8, strdatetime.length());
                            if (msgtim != null && msgtim.length() <= 4) {
                                msgtim += "00";
                            }
                            shploc.setPldtim(Integer.parseInt(msgtim));
                        }
                    }
                }
            }//end of LocationType=\"PlaceOfDelivery\" DateTime if condition
            if (str.indexOf("LocationType=\"PlaceOfDelivery\"") >= 0 && str.indexOf("DateTime::DateType=\"") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String plddqa = str.substring(str.lastIndexOf("DateTime::DateType=\"") + 20,
                        str.lastIndexOf("\"=CONGRUENCE"));
                if (plddqa != null && !plddqa.trim().equals("")) {
                    if (plddqa.length() > 35) {
                        strError = "Length of the Place Of Delivery DateTime Qualifier is more than 35 characters";
                    } else {
                        shploc.setPlddqa(plddqa);
                    }
                }
            }//end of LocationType=\"PlaceOfDelivery\ DateTime::DateType if condition
            if (str.indexOf("Location::"
                    + "LocationType=\"IntermediatePort\"-LocationCode::Agency") >= 0) {
                String intcqa = str.substring(str.lastIndexOf("LocationCode::Agency=\"") + 22,
                        str.lastIndexOf("\"=CONGRUENCE"));
                if (intcqa != null && !intcqa.trim().equals("")) {
                    if (intcqa.length() > 35) {
                        strError = "Length of the Intermediate Port Location Code Qualifier is more than 35 characters";
                    } else {
                        shploc.setIntcqa(intcqa);
                    }
                }
            }//end of Location::"LocationType=\"IntermediatePort\"-LocationCode::Agency if condition
            if (str.indexOf("LocationType=\"IntermediatePort\"") >= 0
                    && str.indexOf("-LocationCode") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String intcod = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (intcod != null && !intcod.trim().equals("")) {
                    if (intcod.length() > 35) {
                        strError = "Length of the Intermediate Port Code/ScheduleD/ScheduleK is more than 35 characters";
                    } else {
                        shploc.setIntcod(intcod);
                    }
                }
            }//end of LocationType=\"IntermediatePort\" -LocationCode if condition
            if (str.indexOf("LocationType=\"IntermediatePort\"") >= 0
                    && str.indexOf("-LocationName") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String intnam = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (intnam != null && !intnam.trim().equals("")) {
                    if (intnam.length() > 256) {
                        strError = "Length of the Intermediate Port Location Name is more than 256 characters";
                    } else {
                        shploc.setIntnam(intnam);
                    }
                }
            }//end of LocationType=\"IntermediatePort\ -LocationName if condition
            if (str.indexOf("LocationType=\"IntermediatePort\"") >= 0
                    && str.indexOf("-LocationCountry") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String intcry = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (intcry != null && !intcry.trim().equals("")) {
                    if (intcry.length() > 25) {
                        strError = "Length of the Intermediate Port Location Country Code/Country Name is more than 25 characters";
                    } else {
                        shploc.setIntcry(intcry);
                    }
                }
            }// end of LocationType=\"IntermediatePort\ -LocationCountry if
            if (str.indexOf("LocationType=\"IntermediatePort\"") >= 0 && str.indexOf("DateTime") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String strdatetime = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                String msgdte = "";
                String msgtim = "";
                if (strdatetime != null && !strdatetime.trim().equals("")) {
                    if (strdatetime.length() > 12) {
                        strError = "Length of the Intermediate Port Date is more than 12 characters";
                    } else {
                        msgdte = strdatetime.substring(0, 8);
                        shploc.setIntdat(Integer.parseInt(msgdte));
                        if (strdatetime.length() > 8) {
                            msgtim = strdatetime.substring(8, strdatetime.length());
                            if (msgtim != null && msgtim.length() <= 4) {
                                msgtim += "00";
                            }
                            shploc.setInttim(Integer.parseInt(msgtim));
                        }
                    }
                }
            }//end of LocationType=\"IntermediatePort\" DateTime if condition
            if (str.indexOf("LocationType=\"IntermediatePort\"") >= 0 && str.indexOf("DateTime::DateType=\"") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String intdqa = str.substring(str.lastIndexOf("DateTime::DateType=\"") + 20,
                        str.lastIndexOf("\"=CONGRUENCE"));
                if (intdqa != null && !intdqa.trim().equals("")) {
                    if (intdqa.length() > 35) {
                        strError = "Length of the Intermediate Port DateTime Qualifier is more than 35 characters";
                    } else {
                        shploc.setIntdqa(intdqa);
                    }
                }
            }//end of LocationType=\"IntermediatePort\ DateTime::DateType if condition
            if (str.indexOf("PartnerRole=\"Carrier\"-PartnerIdentifier") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String carrid = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (carrid != null && !carrid.trim().equals("")) {
                    if (carrid.length() > 35) {
                        strError = "Length of the Carrier Identifier is more than 35 characters";
                    } else {
                        shploc.setCarrid(carrid);
                    }
                }
            }//end of PartnerRole=\"Carrier\"-PartnerIdentifier\" if condition
            if (str.indexOf("PartnerRole=\"Carrier\"-PartnerName") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String strcarnam = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
//				System.out.println("strcarnam "+strcarnam);
                if (strcarnam != null && !strcarnam.trim().equals("")) {
                    if (strcarnam.length() > 35) {
                        strError = "Length of the Carrier Name is more than 35 characters";
                    } else {
                        if (carNameLimit < 2) {
                            shploc.setCarName(strcarnam, carNameLimit);
                            carNameLimit++;
                        }
                    }
                }
            }// end of PartnerRole=\"Carrier\"-ContactInformation-ContactName
            if (str.indexOf("PartnerRole=\"Carrier\"-ContactInformation-ContactName") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String carcon = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (carcon != null && !carcon.trim().equals("")) {
                    if (carcon.length() > 35) {
                        strError = "Length of the Carrier Contact Name is more than 35 characters";
                    } else {
                        shploc.setCarcon(carcon);
                    }
                }
            }//end of PartnerRole=\"Carrier\"-ContactInformation-ContactName if condition
            if (str.indexOf("PartnerRole=\"Carrier\"-ContactInformation-CommunicationValue::"
                    + "CommunicationType=\"Telephone\"") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String cartel = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (cartel != null && !cartel.trim().equals("")) {
                    if (cartel.length() > 50) {
                        strError = "Length of the Carrier Telephone is more than 50 characters";
                    } else {
                        shploc.setCartel(cartel);
                    }
                }
            }
            if (str.indexOf("PartnerRole=\"Carrier\"-ContactInformation-CommunicationValue::"
                    + "CommunicationType=\"Fax\"") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String carfax = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (carfax != null && !carfax.trim().equals("")) {
                    if (carfax.length() > 50) {
                        strError = "Length of the Carrier Fax is more than 50 characters";
                    } else {
                        shploc.setCarfax(carfax);
                    }
                }
            }//end of PartnerRole=\"Carrier\"-ContactInformation-CommunicationValue::CommunicationType=\"Fax-\" if condition
            if (str.indexOf("PartnerRole=\"Carrier\"-ContactInformation-CommunicationValue::"
                    + "CommunicationType=\"Email\"") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String careml = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (careml != null && !careml.trim().equals("")) {
                    if (careml.length() > 50) {
                        strError = "Length of the Carrier EMAIL is more than 50 characters";
                    } else {
                        shploc.setCareml(careml);
                    }
                }
            }
            if (str.indexOf("PartnerRole=\"Carrier\"-AddressInformation-AddressLine") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String strcaraddr = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (strcaraddr != null && !strcaraddr.trim().equals("")) {
                    if (strcaraddr.length() > 35) {
                        strError = "Length of the Carrier AddressLine is more than 35 characters";
                    } else {
                        if (carAddrLimit < 4) {
                            shploc.setCarAddr(strcaraddr, carAddrLimit);
                            carAddrLimit++;
                        }
                    }
                }
            }//end of PartnerRole=\"Carrier\"-AddressInformation-AddressLine if condition
        }
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
                String ediTrackingStatus = new LogFileEdiDAO().findTrackingStatus(shpsta.getEvncod(), "", "", "I");
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

    public String getTrackingStatus() {
        return trackingStatus;
    }

    public void setTrackingStatus(String trackingStatus) {
        this.trackingStatus = trackingStatus;
    }

    public void createDirectory() {
        try {
            prop.load(getClass().getResourceAsStream(CommonConstants.EDIPROPERTIES));
            dateFolder = DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
            if (osName().contains("linux")) {
                File fileIn = new File(prop.getProperty("linuxInttra315Directory"));
                File fileArchive = new File(prop.getProperty("linuxInttra315Archive") + dateFolder);
                File fileUnprocessed = new File(prop.getProperty("linuxInttra315Unprocessed") + dateFolder);
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
                File fileIn = new File(prop.getProperty("inttra315Directory"));
                File fileArchive = new File(prop.getProperty("inttra315Archive") + dateFolder);
                File fileUnprocessed = new File(prop.getProperty("inttra315Unprocessed") + dateFolder);
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

    public String formatContainerNumber(String contNo) {
        return contNo.substring(0, 4) + "-" + contNo.substring(4, 10) + "-" + contNo.substring(10, 11);
    }
}

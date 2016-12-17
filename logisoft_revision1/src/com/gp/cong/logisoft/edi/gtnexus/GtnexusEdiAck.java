package com.gp.cong.logisoft.edi.gtnexus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.fcl.EdiTrackingBC;
import com.gp.cong.logisoft.bc.notes.NotesBC;
import com.gp.cong.logisoft.domain.EdiAck;
import com.gp.cong.logisoft.edi.util.LogFileWriter;
import com.gp.cong.logisoft.hibernate.dao.EdiAckDAO;
import com.gp.cong.logisoft.hibernate.dao.LogFileEdiDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsRemarksDAO;
import java.util.Vector;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;

import org.apache.log4j.Logger;

public class GtnexusEdiAck extends DefaultHandler {

    private static final Logger log = Logger.getLogger(GtnexusEdiAck.class);
    private String _startElement = "";
    private Vector<String> v = null;
    String lastMethodCalled = "";
    private String gtnId = "";
    private String correctStr = "";
    //private List<EdiAck> edacigList = new ArrayList<EdiAck>();
    private List<File> files = null;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    //private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
    private String company = "G";
    private String messageType = "997";
    Properties prop = new Properties();
    private EdiAck ediAck;
    private boolean returnFlag = false;
    private boolean lclFlag = false;
    EdiAckDAO ediAckDAO = new EdiAckDAO();
    LogFileEdiDAO logFileEdiDAO = new LogFileEdiDAO();
    EdiTrackingBC ediTrackingBC = new EdiTrackingBC();
    String dateFolder = "";

    public void readXml(GtnexusEdiAck gtnexusEdiAck) {
        Date date = new Date();
        String currentDate = sdf.format(date);
        //String ackRecDate = sdf1.format(date);
        LogFileWriter logFileWriter = new LogFileWriter();
        //SQLFunctions sqlFunc = new SQLFunctions();
        File file = null;
        File sourceFile = null;
        File destFile = null;
        try {
            prop.load(getClass().getResourceAsStream(CommonConstants.EDIPROPERTIES));
            createDirectory();
            ReadGTnexusXMLFiles readXMLFiles = new ReadGTnexusXMLFiles();
            files = readXMLFiles.readFiles(messageType, osName());
            DefaultHandler handler = gtnexusEdiAck;
            SAXParserFactory saxFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxFactory.newSAXParser();
            dateFolder = DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
            if (files != null && files.size() > 0) {
                for (int i = 0; i < files.size(); i++) {
                    try {
                        file = (File) files.get(i);
                        saxParser.parse(file, handler);
                        if (osName().contains("linux")) {
                            sourceFile = new File(prop.getProperty("linuxGtnexus997Directory") + file.getName());
                            destFile = new File(prop.getProperty("linuxGtnexus997Archive") + dateFolder + file.getName());
                        } else {
                            sourceFile = new File(prop.getProperty("gtnexus997Directory") + file.getName());
                            destFile = new File(prop.getProperty("gtnexus997Archive") + dateFolder + file.getName());
                        }
                        if (this.getEdiAck() != null) {
                            readXMLFiles.move(sourceFile, destFile);
                            String comments = this.getEdiAck().getDetailCommentsInAckMessage();
                            String status = null != comments && comments.length() > 13 ? comments.substring(0, 13) : comments;
                            String drNo = null != this.getEdiAck().getQuoteTerm() ? this.getEdiAck().getQuoteTerm() + this.getEdiAck().getQuoteDr()
                                    : this.getEdiAck().getQuoteDr();
                            ediTrackingBC.setEdiLogs(file.getName(), currentDate, CommonConstants.SUCCESS, CommonConstants.NOERROR,
                                    company, messageType, drNo, this.getEdiAck().getBookingNumber(),
                                    status, null);
                        }

                    } catch (Exception e) {
//						 e.printStackTrace();
                        String drNumber = "";
                        if (this.getEdiAck() != null) {
                            String comments = this.getEdiAck().getDetailCommentsInAckMessage();
                            String status = null != comments && comments.length() > 13 ? comments.substring(0, 13) : "";
                            String drNo = null != this.getEdiAck().getQuoteTerm() ? this.getEdiAck().getQuoteTerm() + this.getEdiAck().getQuoteDr()
                                    : this.getEdiAck().getQuoteDr();
                            ediTrackingBC.setEdiLog(file.getName(), currentDate, CommonConstants.FAILURE, e.toString(),
                                    company, messageType, drNo, "", status, null);
                            logFileWriter.doAppend("Error while reading XML file " + file.getName() + ". Type of Error "
                                    + "is---" + e, "error_logfile_" + file.getName() + "_" + currentDate + ".txt", company, osName(), messageType);
                        }
                        if (osName().contains("linux")) {
                            sourceFile = new File(prop.getProperty("linuxGtnexus997Directory") + file.getName());
                            destFile = new File(prop.getProperty("linuxGtnexus997Unprocessed") + dateFolder + file.getName());
                        } else {
                            sourceFile = new File(prop.getProperty("gtnexus997Directory") + file.getName());
                            destFile = new File(prop.getProperty("gtnexus997Unprocessed") + dateFolder + file.getName());
                        }
                        readXMLFiles.move(sourceFile, destFile);
                    }
                }
            }
//			 for(int i=0;i<edacigList.size();i++){
//                            EdiAck ediAck = (EdiAck)edacigList.get(i);
//                            ediAck.setAckReceivedDate(currentDate);
//                            ediAckDAO.save(ediAck);
//                            new NotesBC().ackReceivedNotes(ediAck.getQuoteDr());
//	            	//sqlFunc.databaseWrite(ediAck);
//	         }
        } catch (Exception e) {
            log.info("readXml failed on " + new Date(), e);
            // TODO: handle exception
        }
    }
    //===========================================================
    // SAX DocumentHandler methods
    //===========================================================

    public void startDocument() throws SAXException {
        v = new Vector<String>();
        lastMethodCalled = "startDocument";
    }

    public void processingInstruction(String target, String data)
            throws SAXException {
    }

    public void endDocument() throws SAXException {
        lastMethodCalled = "endDocument";
        try {
            showRecords();
        } catch (Exception e) {
            log.info("endDocument failed on " + new Date(), e);
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
        this.setEdiAck(null);
        EdiAck ed = new EdiAck();
        for (Enumeration<String> e = v.elements(); e.hasMoreElements();) {
            String str = e.nextElement().toString();
            if (str.indexOf("AcknowledgementGroup-FileName") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String fileNameFromTag = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                String fileName = "";
                int siId = 0;
                if (fileNameFromTag != null && !fileNameFromTag.trim().equals("")) {
                    if (fileNameFromTag.lastIndexOf("304_") >= 0
                            && fileNameFromTag.lastIndexOf(".xml") >= 0) {
                        fileName = fileNameFromTag.substring(fileNameFromTag.lastIndexOf("304_"), fileNameFromTag.lastIndexOf(".xml") + 4);
                        siId = logFileEdiDAO.findSiId(fileName);
                    }//end of 304_ and .xml if condition
                }//end of fileNameFromTag null checking if condition
                if (siId != 0) {
                    returnFlag = true;
                    ed.setFileName(fileName);
                    ed.setSiId(siId);
                } else {
                    returnFlag = false;
                }
            }//end of ACKNOWLEDGEMENT GROUP FILENAME if condition
            else if (str.indexOf("TransactionInfo-MessageSender") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String msgSender = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (msgSender != null) {
                    if (msgSender.equalsIgnoreCase("gtnexus")) {
                        ed.setEdiCompanyIOrG("G");
                    } else if (msgSender.equalsIgnoreCase("INTTRA")) {
                        ed.setEdiCompanyIOrG("I");
                    }
                }
            }//end of TransactionInfo-MessageSender If Condition
            else if (str.indexOf("TransactionInfo-Created") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String ackTimeStamp = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (ackTimeStamp != null) {
                    ed.setAckCreatedTimeStamp(ackTimeStamp);
                }
            }//end of TransactionInfo-Created If Condition
            else if (str.indexOf("AcknowledgementGroup-Acknowledgement") >= 0) {
                if (str.indexOf("AcknowledgementGroup-Acknowledgement::ShipmentId") >= 0) {
                    int starti = str.indexOf("Acknowledgement::ShipmentId");
                    int endi = str.indexOf("::Sequence");
                    String shipmentId = str.substring(starti + 29, endi - 1);
                    if (shipmentId != null) {
                        ed.setShipmentId(shipmentId);
                    }
                }//end of AcknowledgementGroup-Acknowledgement::ShipmentId if condition
                String sequence = "";
                if (str.indexOf("::GtnId") >= 0) {
                    int gtnindex = str.indexOf("::GtnId");
                    int starti = str.indexOf("::Sequence");
                    sequence = str.substring(starti + 12, gtnindex - 1);
                    if (str.indexOf("-Detail::") >= 0) {
                        gtnId = str.substring(gtnindex + 9, str.indexOf("-Detail::") - 1);
                        ed.setShippingInstructionAuditNumber(gtnId);
                    }
                } else if (str.indexOf("-Detail::") >= 0) {
                    int starti = str.indexOf("::Sequence");
                    sequence = str.substring(starti + 12, str.indexOf("-Detail::") - 1);
                }
                if (str.indexOf("-Detail::") >= 0 && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                    String detailcomments = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                    if (detailcomments != null) {
                        ed.setDetailCommentsInAckMessage(detailcomments);
                    }
                }
                if (str.indexOf("-Detail::Severity") >= 0) {
                    String severity = str.substring(str.indexOf("-Detail::Severity") + 19,
                            str.indexOf("-Detail::Severity") + 20);
                    if (severity != null) {
                        ed.setSeverity(severity);
                    }
                }
                if (sequence != null) {
                    if (sequence.length() == 1) {
                        sequence = "000" + sequence;
                    }
                    if (sequence.length() == 2) {
                        sequence = "00" + sequence;
                    }
                    if (sequence.length() == 3) {
                        sequence = "0" + sequence;
                    }
                    ed.setControlSequenceNumber(sequence);
                }
                if (str.indexOf("referenceType=\"BookingNumber\"") >= 0
                        && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                    String bookingNumber = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                    ed.setBookingNumber(bookingNumber);
                }
                if (str.indexOf("referenceType=\"BillOfLadingNumber\"") >= 0
                        && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                    String bolNumber = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                    ed.setBillOfLadingNumber(bolNumber);
                }
                if (str.indexOf("referenceType=\"CarrierSCAC\"") >= 0
                        && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                    String carrierSCAC = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                    ed.setCarrierScac(carrierSCAC);
                }
                if (str.indexOf("referenceType=\"DivisonCode\"") >= 0
                        && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                    String divisonCode = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                    ed.setDivisionCode(divisonCode);
                }
                if (str.indexOf("referenceType=\"ForwardReferenceNumber\"") >= 0
                        && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                    String forrefNumber = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                    ed.setForwarderReferenceNumber(forrefNumber);
                }
                if (str.indexOf("referenceType=\"ConsigneeOrderNumber\"") >= 0
                        && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                    String conOrderNumber = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                    ed.setConsigneeOrderNumber(conOrderNumber);
                }
                if (str.indexOf("referenceType=\"PurchaseOrderNumber\"") >= 0
                        && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                    String poNumber = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                    ed.setPurchaseOrderNumber(poNumber);
                }
                if (str.indexOf("referenceType=\"ContractNumber\"") >= 0
                        && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                    String contractNumber = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                    ed.setContractNumber(contractNumber);
                }
                if (str.indexOf("referenceType=\"ExportReferenceNumber\"") >= 0
                        && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                    String expRefNum = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                    if (expRefNum != null && expRefNum.indexOf("Ref#") > -1) {
                        String drcpt = str.substring(str.indexOf("#") + 1, str.length()).trim().replace("-", "").trim();
                        ed.setQuoteTerm(drcpt.substring(0, 2));
                        ed.setQuoteDr(drcpt.substring(2));
                        ed.setExportReferenceNumber(expRefNum);
                    } else if (expRefNum != null && expRefNum.indexOf("Ref") > -1) {
                        String drcpt = str.substring(str.lastIndexOf("Ref") + 3, str.length()).trim().replace("-", "").trim();
                        if (drcpt.indexOf("_") > 1) {
                            lclFlag = true;
                            String bookingNo[] = drcpt.split("_");
                            ed.setQuoteTerm(null);
                            ed.setQuoteDr(bookingNo[0].trim());
                        } else {
                            ed.setQuoteTerm(drcpt.substring(0, 2));
                            ed.setQuoteDr(drcpt.substring(2));
                        }
                        ed.setExportReferenceNumber(expRefNum);
                    }
                }
                if (str.indexOf("referenceType=\"BrokerReferenceNumber\"") >= 0
                        && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                    String broRefNum = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                    ed.setBrokerReferenceNumber(broRefNum);
                }
                if (str.indexOf("referenceType=\"CustomerOrderNumber\"") >= 0
                        && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                    String cusOrdNum = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                    ed.setCustomerOrderNumber(cusOrdNum);
                }
                if (str.indexOf("referenceType=\"FederalMaritimeComNumber\"") >= 0
                        && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                    String fedComNo = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                    ed.setFederalMaritimeComNumber(fedComNo);
                }
                if (str.indexOf("referenceType=\"TransactionReferenceNumber\"") >= 0
                        && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                    String transRefNum = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                    ed.setTransactionReferenceNumber(transRefNum);
                }
                if (str.indexOf("referenceType=\"InvoiceNumber\"") >= 0
                        && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                    String invoiceNumber = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                    ed.setInvoiceNumber(invoiceNumber);
                }
                if (str.indexOf("::AuditID") >= 0
                        && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                    int starti = str.indexOf("::AuditID");
                    int endi = str.indexOf("=CONGRUENCE");
                    String auditId = str.substring(starti + 11, endi - 1);
                    if (auditId != null) {
                        ed.setDetailAuditId(auditId);
                    }
                }//end of A
            }
            //end of AcknowledgementGroup-Acknowledgement If Condition
        }
//                returnFlag = ediAckDAO.getLogiwareFile(this.getEdiAck().getBookingNumber());
        if (returnFlag) {
            this.setEdiAck(ed);
            ed.setAckReceivedDate(sdf.format(new Date()));
            ediAckDAO.save(ed);
            if (lclFlag) {
//                new LclSsRemarksDAO().insertSsHeaderRemarks(ed.getQuoteDr(), "EVENT", "Acknowledgement Received");
            } else {
                new NotesBC().ackReceivedNotes(ed.getQuoteDr());
            }
        }
    }

    public EdiAck getEdiAck() {
        return ediAck;
    }

    public void setEdiAck(EdiAck ediAck) {
        this.ediAck = ediAck;
    }

    private String osName() {
        return System.getProperty("os.name").toLowerCase();
    }

    public void createDirectory() {
        try {
            prop.load(getClass().getResourceAsStream(CommonConstants.EDIPROPERTIES));
            dateFolder = DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
            if (osName().contains("linux")) {
                File fileIn = new File(prop.getProperty("linuxGtnexus997Directory"));
                File fileArchive = new File(prop.getProperty("linuxGtnexus997Archive") + dateFolder);
                File fileUnprocessed = new File(prop.getProperty("linuxGtnexus997Unprocessed") + dateFolder);
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
                File fileIn = new File(prop.getProperty("gtnexus997Directory"));
                File fileArchive = new File(prop.getProperty("gtnexus997Archive") + dateFolder);
                File fileUnprocessed = new File(prop.getProperty("gtnexus997Unprocessed") + dateFolder);
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
}

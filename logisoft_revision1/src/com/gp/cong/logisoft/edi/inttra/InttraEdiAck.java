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
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.fcl.EdiTrackingBC;
import com.gp.cong.logisoft.bc.notes.NotesBC;
import com.gp.cong.logisoft.domain.EdiAck;
import com.gp.cong.logisoft.domain.lcl.LclSSMasterBl;
import com.gp.cong.logisoft.edi.util.LogFileWriter;
import com.gp.cong.logisoft.hibernate.dao.EdiAckDAO;
import com.gp.cong.logisoft.hibernate.dao.LogFileEdiDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSSMasterBlDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsRemarksDAO;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class InttraEdiAck extends DefaultHandler {

    private static final Logger log = Logger.getLogger(InttraEdiAck.class);
    private String _startElement = "";
    private Vector<String> v = null;
    String lastMethodCalled = "";
    private String correctStr = "";
    private List<EdiAck> edacigList = new ArrayList<EdiAck>();
    private List<File> files = null;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
    private String company = "I";
    private String messageType = "997";
    Properties prop = new Properties();
    private EdiAck ediAck;
    private boolean returnFlag = false;
    private boolean lclFileFlag = false;
    EdiAckDAO ediAckDAO = new EdiAckDAO();
    EdiTrackingBC ediTrackingBC = new EdiTrackingBC();
    LogFileEdiDAO logFileEdiDAO = new LogFileEdiDAO();
    String dateFolder = "";

    public void readXml(InttraEdiAck inttraEdiAck) throws Exception {
        Date date = new Date();
        String currentDate = sdf.format(date);
        LogFileWriter logFileWriter = new LogFileWriter();
//        SQLFunctions sqlFunc = new SQLFunctions();
        File file = null;
        File sourceFile = null;
        File destFile = null;
        try {
            prop.load(getClass().getResourceAsStream(CommonConstants.EDIPROPERTIES));
            createDirectory();
            ReadInttraXMLFiles readXMLFiles = new ReadInttraXMLFiles();
            files = readXMLFiles.readFiles(messageType, osName());
            DefaultHandler handler = inttraEdiAck;
            SAXParserFactory saxFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxFactory.newSAXParser();
            dateFolder = DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
            if (files != null && files.size() > 0) {
                for (int i = 0; i < files.size(); i++) {
                    try {
                        file = (File) files.get(i);
                        saxParser.parse(file, handler);
                        if (osName().contains("linux")) {
                            sourceFile = new File(prop.getProperty("linuxInttra997Directory") + file.getName());
                            destFile = new File(prop.getProperty("linuxInttra997Archive") + dateFolder + file.getName());
                        } else {
                            sourceFile = new File(prop.getProperty("inttra997Directory") + file.getName());
                            destFile = new File(prop.getProperty("inttra997Archive") + dateFolder + file.getName());
                        }

                        if (this.getEdiAck() != null) {
                            readXMLFiles.move(sourceFile, destFile);
                            String comments = this.getEdiAck().getDetailCommentsInAckMessage();
                            String status = null != comments && comments.length() > 13 ? comments.substring(0, 13) : comments;
                            ediTrackingBC.setEdiLog(file.getName(), currentDate, CommonConstants.SUCCESS, CommonConstants.NOERROR, company, messageType,
                                    this.getEdiAck().getQuoteDr(), this.getEdiAck().getBookingNumber(), status, new Date());
                        }
                    } catch (Exception e) {
                        if (osName().contains("linux")) {
                            sourceFile = new File(prop.getProperty("linuxInttra997Directory") + file.getName());
                            destFile = new File(prop.getProperty("linuxInttra997Unprocessed") + dateFolder + file.getName());
                        } else {
                            sourceFile = new File(prop.getProperty("inttra997Directory") + file.getName());
                            destFile = new File(prop.getProperty("inttra997Unprocessed") + dateFolder + file.getName());
                        }

                        readXMLFiles.move(sourceFile, destFile);
                        if (this.getEdiAck() != null) {
                            String comments = this.getEdiAck().getDetailCommentsInAckMessage();
                            String status = null != comments && comments.length() > 13 ? comments.substring(0, 13) : comments;
                            ediTrackingBC.setEdiLog(file.getName(), currentDate, CommonConstants.FAILURE, e.toString(), company, messageType, this.getEdiAck().getQuoteDr(), "", status, null);
                            logFileWriter.doAppend("Error while reading XML file " + file.getName() + ". Type of Error "
                                    + "is---" + e, "error_logfile_" + file.getName() + "_" + currentDate + ".txt", company, osName(), messageType);
                        }
                    }
                }
            }
//			 for(int i=0;i<edacigList.size();i++){
//				 EdiAck ack = (EdiAck)edacigList.get(i);
//                                 ack.setAckReceivedDate(currentDate);
//				 ediAckDAO.save(ack);
//                                 new NotesBC().ackReceivedNotes(ack.getQuoteDr());
//
//	            	//sqlFunc.databaseWrite(ediAck);
//	         }
        } catch (Exception e) {
            log.info("readXml failed on " + new Date(), e);
        }
    }
    //===========================================================
    // SAX DocumentHandler methods
    //===========================================================

    public void startDocument() throws SAXException {
        v = new Vector();
        lastMethodCalled = "startDocument";
    }

    public void endDocument() throws SAXException {
        lastMethodCalled = "endDocument";
        try {
            showRecords();
        } catch (Exception e) {
            log.info("Error in endDocument()  failed on " + new Date(), e);
//	           throw new SAXException("I/O error", e);
        }
    }

    public void startElement(String namespaceURI,
            String lName, // local name
            String qName, // qualified name
            Attributes attrs)
            throws SAXException {
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
        } else {
            //System.out.println("---------------------");
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

    public void showRecords() throws Exception {
        setEdiAck(null);
        EdiAck ed = new EdiAck();
        for (Enumeration e = v.elements(); e.hasMoreElements();) {
            String str = e.nextElement().toString();
            if (str.indexOf("DocumentVersion") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String cntlseqNo = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (cntlseqNo != null) {
                    ed.setControlSequenceNumber(cntlseqNo);
                }

            } else if (str.indexOf("PartnerRole=\"Sender\"-") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String ediCompany = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (ediCompany != null) {
                    if (ediCompany.equalsIgnoreCase("gtnexus")) {
                        ed.setEdiCompanyIOrG("G");
                    } else if (ediCompany.equalsIgnoreCase("INTTRA")) {
                        ed.setEdiCompanyIOrG("I");
                    }
                }
            } else if (str.indexOf("DateTime") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String ackTimeStamp = str.substring(str.lastIndexOf("=CONGRUENCE=") + 14);
                if (ackTimeStamp != null) {
                    ed.setAckCreatedTimeStamp(ackTimeStamp);
                }
            } else if (str.indexOf("ShipmentIdentifier") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String shipmentId = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                String shipmentIdAck = str.substring(str.lastIndexOf("::Acknowledgment=") + 18, str.lastIndexOf("=CONGRUENCE") - 1);
                Object[] siIdAndFileName = null;
                if (shipmentId.indexOf("_") > 1) {
                    lclFileFlag = true;
                    String fileNo[] = shipmentId.split("_");
                    if(fileNo.length > 1){
                        LclSSMasterBl lclSSMasterBl = new LclSSMasterBlDAO().findById(Long.parseLong(fileNo[1].trim()));
                        siIdAndFileName = (Object[]) logFileEdiDAO.findSiIdAndFileName(fileNo[0]+"_"+lclSSMasterBl.getSpBookingNo().replace(" ", ""));
                        ed.setQuoteDr(fileNo[0].trim());
                    }
                } else {
                    siIdAndFileName = (Object[]) logFileEdiDAO.findSiIdAndFileName(shipmentId);
                    lclFileFlag = false;
                }
                if (null != siIdAndFileName && siIdAndFileName.length == 2) {
                    returnFlag = true;
                    if (null != siIdAndFileName[0]) {
                        ed.setSiId((Integer) (siIdAndFileName[0]));
                    }
                    if (null != siIdAndFileName[1]) {
                        log.info("304 exist----------------" + siIdAndFileName[1]);
                        ed.setFileName((String) siIdAndFileName[1]);
                    }
                } else {
                    lclFileFlag = false;
                    returnFlag = false;
                }
                if (shipmentId != null) {
                    ed.setShipmentId(shipmentId);
                }
                if (shipmentIdAck.equalsIgnoreCase("Accepted")) {
                    ed.setSeverity("1");
                } else {
                    ed.setSeverity("3");
                }
            } else if (str.indexOf("ShipmentComments") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String detailComments = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (detailComments != null) {
                    ed.setDetailCommentsInAckMessage(detailComments);
                }
            } else if (str.indexOf("BookingNumber") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String bookingNum = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (bookingNum != null) {
                    ed.setBookingNumber(bookingNum);
                }
            } else if (str.indexOf("BillOfLadingNumber") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String billOfLadingNum = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (billOfLadingNum != null) {
                    ed.setBillOfLadingNumber(billOfLadingNum);
                }
            } else if (str.indexOf("CarrierSCAC") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String carrierSCAC = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (carrierSCAC != null) {
                    ed.setCarrierScac(carrierSCAC);
                }
            } else if (str.indexOf("DivisionCode") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String divisionCode = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (divisionCode != null) {
                    ed.setDivisionCode(divisionCode);
                }
            } else if (str.indexOf("FreightFowarderReference") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String frightForwarderRef = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (frightForwarderRef != null) {
                    ed.setForwarderReferenceNumber(frightForwarderRef);
                }
            } else if (str.indexOf("ConsigneeOrderNumber") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String consigneeOrderNum = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (consigneeOrderNum != null) {
                    ed.setConsigneeOrderNumber(consigneeOrderNum);
                }
            } else if (str.indexOf("PurchaseOrderNumber") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String purchaseOrderNum = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (purchaseOrderNum != null) {
                    ed.setPurchaseOrderNumber(purchaseOrderNum);
                }
            } else if (str.indexOf("ContractNumber") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String contractNum = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (contractNum != null) {
                    ed.setContractNumber(contractNum);
                }
            } else if (str.indexOf("ExportersReferenceNumber") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String exportRefNum = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (exportRefNum != null && exportRefNum.indexOf("Ref#") > -1) {
                    String drcpt = str.substring(str.indexOf("#") + 1, str.length()).trim().replace("-", "").trim();
                    ed.setQuoteTerm(drcpt.substring(0, 2));
                    ed.setQuoteDr(drcpt.substring(2));
                    ed.setExportReferenceNumber(exportRefNum);
                } else if (exportRefNum != null && exportRefNum.indexOf("Ref") > -1) {
                    String fileNo = str.substring(str.lastIndexOf("Ref") + 3, str.length());
                    if (fileNo.indexOf("_") > 1) {
                        String bookingNo[] = fileNo.split("_");
                        ed.setQuoteTerm(null);
                        ed.setQuoteDr(bookingNo[0].trim());
                    } else {
                        String drcpt = str.substring(str.lastIndexOf("Ref") + 3, str.length()).trim().replace("-", "").trim();
                        ed.setQuoteTerm(drcpt.substring(0, 2));
                        ed.setQuoteDr(drcpt.substring(2));
                    }
                    ed.setExportReferenceNumber(exportRefNum);
                }
            } else if (str.indexOf("BrokerReferenceNumber") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String brokerRefNum = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (brokerRefNum != null) {
                    ed.setBrokerReferenceNumber(brokerRefNum);
                }
            } else if (str.indexOf("CustomerOrderNumber") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String customerOrderNum = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (customerOrderNum != null) {
                    ed.setCustomerOrderNumber(customerOrderNum);
                }
            } else if (str.indexOf("FederalMaritimeComNumber") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String federalMaritimeComNum = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (federalMaritimeComNum != null) {
                    ed.setFederalMaritimeComNumber(federalMaritimeComNum);
                }
            } else if (str.indexOf("InvoiceNumber") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String invoiceNum = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (invoiceNum != null) {
                    ed.setInvoiceNumber(invoiceNum);
                }
            } else if (str.indexOf("TransactionReferenceNumber") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String transactionRefNum = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (transactionRefNum != null) {
                    ed.setTransactionReferenceNumber(transactionRefNum);
                }
            } else if (str.indexOf("DocumentIdentifier") >= 0
                    && str.lastIndexOf("=CONGRUENCE=") >= 0) {
                String shipmentAudNum = str.substring(str.lastIndexOf("=CONGRUENCE=") + 12);
                if (shipmentAudNum != null) {
                    ed.setShippingInstructionAuditNumber(shipmentAudNum);
                }
            }
        }
//                returnFlag = ediAckDAO.getLogiwareFile(this.getEdiAck().getBookingNumber());
        if (returnFlag) {
            this.setEdiAck(ed);
            ed.setAckReceivedDate(sdf.format(new Date()));
            ediAckDAO.save(ed);
            if (!lclFileFlag) {
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
                File fileIn = new File(prop.getProperty("linuxInttra997Directory"));
                File fileArchive = new File(prop.getProperty("linuxInttra997Archive") + dateFolder);
                File fileUnprocessed = new File(prop.getProperty("linuxInttra997Unprocessed") + dateFolder);
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
                File fileIn = new File(prop.getProperty("inttra997Directory"));
                File fileArchive = new File(prop.getProperty("inttra997Archive") + dateFolder);
                File fileUnprocessed = new File(prop.getProperty("inttra997Unprocessed") + dateFolder);
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

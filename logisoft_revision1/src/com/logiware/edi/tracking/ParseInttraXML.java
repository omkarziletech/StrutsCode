package com.logiware.edi.tracking;


import com.logiware.edi.entity.BlClause;
import com.logiware.edi.entity.ChargeCategory;
import com.logiware.edi.entity.Contact;
import com.logiware.edi.entity.CustomsClearanceInfo;
import com.logiware.edi.entity.Document;
import com.logiware.edi.entity.EquipmentDetail;
import com.logiware.edi.entity.HazardousGoods;
import com.logiware.edi.entity.PackageDetails;
import com.logiware.edi.entity.Party;
import com.logiware.edi.entity.Shipment;
import com.logiware.edi.entity.SplitgoodsDetails;
import com.logiware.edi.entity.XMLHelper;
import java.io.IOException;
import java.util.Calendar;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vellaisamy
 */
public class ParseInttraXML {
    public Shipment currentShipment;
    public Shipment parseXml(String filePath) throws ParserConfigurationException, SAXException, IOException {
       SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser saxParser = factory.newSAXParser();
                //initialize all required objects here
                    DefaultHandler handler = new DefaultHandler() {
                    public String currentObject = "Shipment";
                    XMLHelper helper = new XMLHelper();
                    BlClause currentBLClause = null;
                    CustomsClearanceInfo customsClearanceInstruction = null;
                    ChargeCategory chargeCategory = null;
                    Party party = null;
                    Contact contact = null;
                    Document document = null;
                    EquipmentDetail equipmentDetail = null;
                    PackageDetails packageDetails = null;
                    PackageDetails innerPack = null;
                    PackageDetails innerInnerPack = null;
                    SplitgoodsDetails splitgoodsDetails = null;
                    HazardousGoods hazardousGoods = null;
                    String value = null;
                    Attributes attributes = null;
                    String previousTag = null;
                    String previousValue = null;
                    boolean isParty = false;
                    boolean isContact = false;
                    boolean isDocument = false;
                    boolean isCustomsClearanceInstructions = false;
                    boolean isEquipmentDetails = false;
                    boolean ispackageDetails = false;
                    boolean isSplitgoodsDetails = false;
                    boolean isHazardousGoods = false;
                    boolean isInnerPack = false;
                    boolean isInnerInnerPack = false;
                    boolean isFirst = true;
                    Calendar cal = Calendar.getInstance();
                    Long timeMills = cal.getTimeInMillis();
                    @Override
                    public void startDocument() throws SAXException {
                        super.startDocument();
                        currentShipment = new Shipment();
                        currentShipment.setStatus("New");
                        isFirst = true;
                        currentBLClause = null;
                        customsClearanceInstruction = null;
                        chargeCategory = null;
                        party = null;
                        contact = null;
                        document = null;
                        equipmentDetail = null;
                        packageDetails = null;
                        innerPack = null;
                        innerInnerPack = null;
                        splitgoodsDetails = null;
                        hazardousGoods = null;
                    }

                    public void startElement(String uri, String localName,
                            String qName, Attributes attributes)
                            throws SAXException {
                        value = "";
                        this.attributes = attributes;
                        if(isFirst) {
                            if(!"SubmitShippingInstruction".equalsIgnoreCase(qName)) {
                                XMLHelper.setError(true);
                            }
                            isFirst = false;
                        }
                        //set current objects
                        if("BillOfLadingClause".equalsIgnoreCase(qName)) {
                            currentObject = "BlClause";
                            currentBLClause = new BlClause();
                        }else if("DetailCustomsClearanceInstructions".equalsIgnoreCase(qName) || isCustomsClearanceInstructions) {
                            currentObject = "CustomsClearanceInfo";
                            if("DetailCustomsClearanceInstructions".equalsIgnoreCase(qName)) {
                                isCustomsClearanceInstructions = true;
                                XMLHelper.setParentTag(qName);
                                customsClearanceInstruction = new CustomsClearanceInfo();
                            }
                        }else if("PrepaidCollector".equalsIgnoreCase(qName)) {
                            currentObject = "ChargeCategory";
                            chargeCategory = new ChargeCategory();
                        }else if("Contacts".equalsIgnoreCase(qName) || isContact) {
                            currentObject = "Contact";
                            if("Contacts".equalsIgnoreCase(qName)) {
                                isContact = true;
                                isParty = false;
                                XMLHelper.setParentTag(qName);
                                contact = new Contact();
                            }
                        }else if("Document".equalsIgnoreCase(qName) || isDocument) {
                            currentObject = "Document";
                            if("Document".equalsIgnoreCase(qName)) {
                                isDocument = true;
                                isParty = false;
                                XMLHelper.setParentTag(qName);
                                document = new Document();
                            }
                        }else if ("Party".equalsIgnoreCase(qName) || "HouseParty".equalsIgnoreCase(qName) || isParty) {
                            currentObject = "Party";
                            if("Party".equalsIgnoreCase(qName) || "HouseParty".equalsIgnoreCase(qName)) {
                                isParty = true;
                                XMLHelper.setParentTag(qName);
                                party = new Party();
                            }
                        }else if("EquipmentDetails".equalsIgnoreCase(qName) || isEquipmentDetails) {
                            currentObject = "EquipmentDetail";
                            if("EquipmentDetails".equalsIgnoreCase(qName)) {
                                isEquipmentDetails = true;
                                XMLHelper.setParentTag(qName);
                                XMLHelper.setContainerSupplier(attributes.getValue("EquipmentSupplier"));
                                equipmentDetail = new EquipmentDetail();
                            }
                        }else if("HazardousGoods".equalsIgnoreCase(qName) || isHazardousGoods) {
                            currentObject = "HazardousGoods";
                            if("HazardousGoods".equalsIgnoreCase(qName)) {
                                isHazardousGoods = true;
                                XMLHelper.setParentTag(qName);
                                hazardousGoods = new HazardousGoods();
                            }
                        }else if("SplitGoodsDetails".equalsIgnoreCase(qName) || isSplitgoodsDetails) {
                            currentObject = "SplitgoodsDetails";
                            if("SplitGoodsDetails".equalsIgnoreCase(qName)) {
                                isSplitgoodsDetails = true;
                                XMLHelper.setParentTag(qName);
                                splitgoodsDetails = new SplitgoodsDetails();
                            }
                        }else if("InnerInnerPack".equalsIgnoreCase(qName) || isInnerInnerPack) {
                            currentObject = "InnerInnerPack";
                            if("InnerInnerPack".equalsIgnoreCase(qName)) {
                                isInnerInnerPack = true;
                                XMLHelper.setParentTag(qName);
                                innerInnerPack = new PackageDetails();
                                innerInnerPack.setId(timeMills++);
                                innerInnerPack.setPackageType(qName);
                            }
                        }else if("InnerPack".equalsIgnoreCase(qName)|| isInnerPack) {
                            currentObject = "InnerPack";
                            if("InnerPack".equalsIgnoreCase(qName)) {
                                isInnerPack = true;
                                XMLHelper.setParentTag(qName);
                                innerPack = new PackageDetails();
                                innerPack.setId(timeMills++);
                                innerPack.setPackageType(qName);
                            }
                        }else if("OuterPack".equalsIgnoreCase(qName) || ispackageDetails) {
                            currentObject = "OuterPack";
                            if("OuterPack".equalsIgnoreCase(qName)) {
                                ispackageDetails = true;
                                XMLHelper.setParentTag(qName);
                                packageDetails = new PackageDetails();
                                packageDetails.setId(timeMills++);
                                packageDetails.setPackageType(qName);
                            }
                        } else {
                            currentObject = "Shipment";
                            if("GeneralInformation".equalsIgnoreCase(qName)) {
                                XMLHelper.setParentTag(qName);
                            }
                        }

                        //set current parent tag
                        if("Location".equalsIgnoreCase(qName)) {
                            XMLHelper.setParentTag(qName);
                        }else if("ReferenceInformation".equalsIgnoreCase(qName)) {
                            XMLHelper.setParentTag(qName);
                            XMLHelper.setRefInfoType(attributes.getValue("Type"));
                        }else if("ChargeCategory".equalsIgnoreCase(qName)) {
                            XMLHelper.setParentTag(qName);
                            XMLHelper.setChargeType(attributes.getValue("ChargeType"));
                        }else if("ExportLicense".equalsIgnoreCase(qName)) {
                            XMLHelper.setParentTag(qName);
                        }else if("LetterOfCredit".equalsIgnoreCase(qName)) {
                            XMLHelper.setParentTag(qName);
                        }else if("EquipmentComments".equalsIgnoreCase(qName)) {
                            XMLHelper.setParentTag(qName);
                        }else if("EquipmentReferenceInformation".equalsIgnoreCase(qName)) {
                            XMLHelper.setParentTag(qName);
                            XMLHelper.setRefInfoType(attributes.getValue("Type"));
                        }else if("ConveyanceInformation".equalsIgnoreCase(qName)) {
                            XMLHelper.setParentTag(qName);
                        }else if("TransportationDetails".equalsIgnoreCase(qName)) {
                            currentShipment.setTransaportStage(attributes.getValue("TransportStage"));
                            currentShipment.setTransaportMode(attributes.getValue("TransportMode"));
                       }else if("DetailsReferenceInformation".equalsIgnoreCase(qName)) {
                            XMLHelper.setParentTag(qName);
                            XMLHelper.setRefInfoType(attributes.getValue("Type"));
                       }else if("HazardousGoodsComments".equalsIgnoreCase(qName)) {
                            XMLHelper.setParentTag(qName);
                       }else if("EmergencyResponseContact".equalsIgnoreCase(qName)) {
                            XMLHelper.setParentTag(qName);
                       }
                    }

                    public void endElement(String uri, String localName,
                            String qName)
                            throws SAXException {
                        //add child here
                        if("Shipment".equalsIgnoreCase(currentObject)) {
                            currentShipment.setValue(qName,value,attributes, previousTag, previousValue);
                        }else if("BlClause".equalsIgnoreCase(currentObject)) {
                            currentBLClause.setValue(qName, value, attributes, previousTag, previousValue);
                            currentBLClause.setShipment(currentShipment);
                            currentShipment.addBlClause(currentBLClause);
                        }else if("ChargeCategory".equalsIgnoreCase(currentObject)) {
                            chargeCategory.setValue(qName, helper.getChargeType(), attributes, previousTag, previousValue);
                            chargeCategory.setShipment(currentShipment);
                            currentShipment.addChargeCategory(chargeCategory);
                        }else if("Contact".equalsIgnoreCase(currentObject)) {
                            contact.setValue(qName, value, attributes, previousTag, previousValue);
                        }else if("CustomsClearanceInfo".equalsIgnoreCase(currentObject)) {
                            customsClearanceInstruction.setValue(qName, value, attributes, previousTag, previousValue);
                        }else if("Document".equalsIgnoreCase(currentObject)) {
                            document.setValue(qName, value, attributes, previousTag, previousValue);
                        }else if("EquipmentDetail".equalsIgnoreCase(currentObject)) {
                            equipmentDetail.setValue(qName, value, attributes, previousTag, previousValue);
                        }else if ("Party".equalsIgnoreCase(currentObject)) {
                            party.setValue(qName, value, attributes, previousTag, previousValue);
                        }else if ("OuterPack".equalsIgnoreCase(currentObject)) {
                            packageDetails.setValue(qName, value, attributes, previousTag, previousValue);
                        }else if ("InnerPack".equalsIgnoreCase(currentObject)) {
                            innerPack.setValue(qName, value, attributes, previousTag, previousValue);
                        }else if ("InnerInnerPack".equalsIgnoreCase(currentObject)) {
                            innerInnerPack.setValue(qName, value, attributes, previousTag, previousValue);
                        }else if ("SplitgoodsDetails".equalsIgnoreCase(currentObject)) {
                            splitgoodsDetails.setValue(qName, value, attributes, previousTag, previousValue);
                        }else if ("HazardousGoods".equalsIgnoreCase(currentObject)) {
                            hazardousGoods.setValue(qName, value, attributes, previousTag, previousValue);
                        }

                        //add inner child objects here
                        if("Party".equalsIgnoreCase(qName) || "HouseParty".equalsIgnoreCase(qName)) {
                            isParty = false;
                            party.setShipment(currentShipment);
                            currentShipment.addParty(party);
                        }else if("Contacts".equalsIgnoreCase(qName)) {
                            isParty = true;
                            isContact = false;
                            contact.setParty(party);
                            party.addContact(contact);
                        }else if("Document".equalsIgnoreCase(qName)) {
                            isParty = true;
                            isDocument = false;
                            document.setParty(party);
                            party.addDocument(document);
                        }else if("DetailCustomsClearanceInstructions".equalsIgnoreCase(qName)) {
                            isCustomsClearanceInstructions = false;
                            customsClearanceInstruction.setShipment(currentShipment);
                            currentShipment.addDetailCustomsClearanceInstructions(customsClearanceInstruction);
                        }else if("EquipmentDetails".equalsIgnoreCase(qName)) {
                            isEquipmentDetails = false;
                            equipmentDetail.setShipment(currentShipment);
                            currentShipment.addEquipmentDetail(equipmentDetail);
                        }else if("HazardousGoods".equalsIgnoreCase(qName)) {
                            isHazardousGoods = false;
                            ispackageDetails = true;
                            hazardousGoods.setPackageDetails(packageDetails);
                            packageDetails.addHazardousGoods(hazardousGoods);
                        }else if("SplitGoodsDetails".equalsIgnoreCase(qName)) {
                            isSplitgoodsDetails = false;
                            ispackageDetails = true;
                            splitgoodsDetails.setPackageDetails(packageDetails);
                            packageDetails.addSplitgoodsDetails(splitgoodsDetails);
                        }else if("OuterPack".equalsIgnoreCase(qName)) {
                            ispackageDetails = false;
                            packageDetails.setShipment(currentShipment);
                            currentShipment.addPackageDetail(packageDetails);
                        }else if("InnerPack".equalsIgnoreCase(qName)) {
                            isInnerPack = false;
                            ispackageDetails = true;
                            innerPack.setParentId(packageDetails.getId());
                            innerPack.setShipment(currentShipment);
                            currentShipment.addPackageDetail(innerPack);
                        }else if("InnerInnerPack".equalsIgnoreCase(qName)) {
                            isInnerInnerPack = false;
                            isInnerPack = true;
                            innerInnerPack.setParentId(innerPack.getId());
                            innerInnerPack.setShipment(currentShipment);
                            currentShipment.addPackageDetail(innerInnerPack);
                        }
                        previousTag = qName;
                        previousValue = value;
                    }

                    public void characters(char ch[], int start, int length)
                            throws SAXException {
                        value = new String(ch,start,length);
                    }
                };
                saxParser.parse(filePath, handler);
                return currentShipment;
    }

}

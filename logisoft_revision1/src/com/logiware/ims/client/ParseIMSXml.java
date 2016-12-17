/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.logiware.ims.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;

/**
 *
 * @author Shan
 */
public class ParseIMSXml {
     public IMSQuote iMSQuote = null;
     public List<IMSQuote> IMSQuoteList;
    public List<IMSQuote> parseXml(String filePath) throws ParserConfigurationException, SAXException, IOException {
       SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser saxParser = factory.newSAXParser();
                //initialize all required objects here
                    DefaultHandler handler = new DefaultHandler() {
                        String value = null;
                        boolean isFirst = true;
                        public void startDocument() throws SAXException {
                            IMSQuoteList = new ArrayList<IMSQuote>();
                        }

                        public void endDocument()throws SAXException{
                        }

                        public void startElement(String uri, String localName,
                                String qName, Attributes attributes)
                                throws SAXException {
                            if("quote".equalsIgnoreCase(qName)){
                                iMSQuote = new IMSQuote();
                            }
                        }

                        public void endElement(String uri, String localName,
                                String qName)
                                throws SAXException {
                            if("uid".equalsIgnoreCase(qName)){
                                iMSQuote.setUniqueId(value);
                            }else if("originId".equalsIgnoreCase(qName)){
                                iMSQuote.setOriginId(value);
                            }else if("originName".equalsIgnoreCase(qName)){
                                iMSQuote.setOriginName(value);
                            }else if("destId".equalsIgnoreCase(qName)){
                                iMSQuote.setDestinationId(value);
                            }else if("destName".equalsIgnoreCase(qName)){
                                iMSQuote.setDestinationName(value);
                            }else if("emptyId".equalsIgnoreCase(qName)){
                                iMSQuote.setEmptyId(value);
                            }else if("emptyName".equalsIgnoreCase(qName)){
                                iMSQuote.setEmptyName(value);
                            }else if("importExportInd".equalsIgnoreCase(qName)){
                                iMSQuote.setImportExportInd(value);
                            }else if("containerSizeId".equalsIgnoreCase(qName)){
                                iMSQuote.setContainerSizeId(value);
                            }else if("containerSizeDesc".equalsIgnoreCase(qName)){
                                iMSQuote.setContainerSizeDesc(value);
                            }else if("containerTypeId".equalsIgnoreCase(qName)){
                                iMSQuote.setContainerTypeId(value);
                            }else if("containerTypeDesc".equalsIgnoreCase(qName)){
                                iMSQuote.setContainerTypeDesc(value);
                            }else if("mode".equalsIgnoreCase(qName)){
                                if(!isFirst){
                                    iMSQuote.setMode(value);
                                }else{
                                    isFirst = false;
                                }
                            }else if("viaId".equalsIgnoreCase(qName)){
                                iMSQuote.setViaId(value);
                            }else if("viaDesc".equalsIgnoreCase(qName)){
                                iMSQuote.setViaDesc(value);
                            }else if("quoteNbr".equalsIgnoreCase(qName)){
                                iMSQuote.setQuoteNumber(value);
                            }else if("hazardousFees".equalsIgnoreCase(qName)){
                                iMSQuote.setHazardousFees(value);
                            }else if("reeferFees".equalsIgnoreCase(qName)){
                                iMSQuote.setReeferFees(value);
                            }else if("overweightFees".equalsIgnoreCase(qName)){
                                iMSQuote.setOverWeightFees(value);
                            }else if("cleanTruckFees".equalsIgnoreCase(qName)){
                                iMSQuote.setCleanTruckFees(value);
                            }else if("quoteAmt".equalsIgnoreCase(qName)){
                                iMSQuote.setQuoteAmount(value);
                            }else if("fuelPct".equalsIgnoreCase(qName)){
                                iMSQuote.setFuelSurcharge(value);
                            }else if("fuelFees".equalsIgnoreCase(qName)){
                                iMSQuote.setFuelFees(value);
                            }else if("basePlusSpecial".equalsIgnoreCase(qName)){
                                iMSQuote.setBasePlusSpecial(value);
                            }else if("allInRate".equalsIgnoreCase(qName)){
                                iMSQuote.setAllInRate(value);
                            }else if("quote2Amt".equalsIgnoreCase(qName)){
                                iMSQuote.setQuote2Amt(value);
                            }else if("fuel2Pct".equalsIgnoreCase(qName)){
                                iMSQuote.setFuel2Pct(value);
                            }else if("fuel2Fees".equalsIgnoreCase(qName)){
                                iMSQuote.setFuel2Fees(value);
                            }else if("base2PlusSpecial".equalsIgnoreCase(qName)){
                                iMSQuote.setBase2PlusSpecial(value);
                            }else if("allIn2Rate".equalsIgnoreCase(qName)){
                                iMSQuote.setAllIn2Rate(value);
                            }else if("effDate".equalsIgnoreCase(qName)){
                                iMSQuote.setEffectiveDate(value);
                            }else if("expDate".equalsIgnoreCase(qName)){
                                iMSQuote.setExpiryDate(value);
                            }else if("createdBy".equalsIgnoreCase(qName)){
                                iMSQuote.setCreatedBy(value);
                            }else if("createdOn".equalsIgnoreCase(qName)){
                                iMSQuote.setCreatedOn(value);
                            }else if("requestedBy".equalsIgnoreCase(qName)){
                                iMSQuote.setRequestedBy(value);
                            }else if("requestedByEmail".equalsIgnoreCase(qName)){
                                iMSQuote.setRequestedByEmail(value);
                            }else if("requestedByPhone".equalsIgnoreCase(qName)){
                                iMSQuote.setRequestedByPhone(value);
                            }else if("requestedByExt".equalsIgnoreCase(qName)){
                                iMSQuote.setRequestedByExt(value);
                            }else if("quote".equalsIgnoreCase(qName)){
                                IMSQuoteList.add(iMSQuote);
                                iMSQuote = new IMSQuote();
                            }
                        }

                        public void characters(char ch[], int start, int length)
                                throws SAXException {
                            value = new String(ch, start, length);
                        }
                };
                saxParser.parse(filePath, handler);
                return IMSQuoteList;
    }
}

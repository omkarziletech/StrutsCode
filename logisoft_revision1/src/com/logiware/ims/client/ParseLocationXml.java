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
public class ParseLocationXml {
     public EmptyLocation emptyLocation = null;
     public List<EmptyLocation> locationList;
    public List<EmptyLocation> parseXml(String filePath) throws ParserConfigurationException, SAXException, IOException {
       SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser saxParser = factory.newSAXParser();
                //initialize all required objects here
                    DefaultHandler handler = new DefaultHandler() {
                        String value = null;
                        public void startDocument() throws SAXException {
                            locationList = new ArrayList<EmptyLocation>();
                        }

                        public void endDocument()throws SAXException{
                        }

                        public void startElement(String uri, String localName,
                                String qName, Attributes attributes)
                                throws SAXException {
                            if("location".equalsIgnoreCase(qName)){
                                emptyLocation = new EmptyLocation();
                            }
                        }

                        public void endElement(String uri, String localName,
                                String qName)
                                throws SAXException {
                            if("locationname".equalsIgnoreCase(qName)){
                                emptyLocation.setLocationName(value);
                            }else if("commonInd".equalsIgnoreCase(qName)){
                                emptyLocation.setCommonId(value);
                            }else if("miles".equalsIgnoreCase(qName)){
                                emptyLocation.setMiles(value);
                            }else if("location".equalsIgnoreCase(qName)){
                                locationList.add(emptyLocation);
                                emptyLocation = new EmptyLocation();
                            }
                        }
                        public void characters(char ch[], int start, int length)
                                throws SAXException {
                            value = new String(ch, start, length);
                        }
                };
                saxParser.parse(filePath, handler);
                return locationList;
    }
}

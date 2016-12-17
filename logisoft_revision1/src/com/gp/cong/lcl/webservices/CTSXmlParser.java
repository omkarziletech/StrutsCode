package com.gp.cong.lcl.webservices;

import com.gp.cong.lcl.webservices.dom.AdditionalSC;
import com.gp.cong.lcl.webservices.dom.Carrier;
import com.gp.cong.lcl.webservices.dom.Classes;
import com.gp.cong.lcl.webservices.dom.Destination;
import com.gp.cong.lcl.webservices.dom.Origin;
import com.gp.cong.lcl.webservices.dom.Rates;
import com.gp.cong.lcl.webservices.dom.Service;
import com.gp.cong.lcl.webservices.dom.Weights;
import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;
import java.util.List;
import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class CTSXmlParser extends DefaultHandler {

    private String _endElement;
    private String _startElement = "";
    private String _characters;
    Vector v = new Vector();
    String lastMethodCalled = "";
    private Map xmlObjMap;
    private List carrierList;
    private StringBuffer error = new StringBuffer();

    public void parseCTSXml(String filename)throws Exception {
        // Use an instance of ourselves as the SAX event handler
        DefaultHandler handler = this; //new CTSXmlParser();
        // Use the default (non-validating) parser
        SAXParserFactory factory = SAXParserFactory.newInstance();
            // Parse the input
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(new File(filename), handler);
    }

    //===========================================================
    // SAX DocumentHandler methods
    //===========================================================
    public void startDocument()
            throws SAXException {
        lastMethodCalled = "startDocument";
    }

    public void endDocument()
            throws SAXException {
        lastMethodCalled = "endDocument";

            showRecords();

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
        if (!eName.equalsIgnoreCase("RATES")) {
            _startElement = _startElement + "-" + eName;
        }

        if (attrs != null) {
            for (int i = 0; i < attrs.getLength(); i++) {
                String aName = attrs.getLocalName(i); // Attr name
                if ("".equals(aName)) {
                    aName = attrs.getQName(i);
                }
                if (!eName.equalsIgnoreCase("RATES")) {
                    _startElement = _startElement + "::" + aName + "=\"" + attrs.getValue(i) + "\"";
                }
            }
        }
    }

    public void endElement(String namespaceURI,
            String sName, // simple name
            String qName // qualified name
            )
            throws SAXException {
        if (lastMethodCalled.equalsIgnoreCase("startElement")) {
            String s = new String("startElement::" + qName);
            v.add(s);
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
        String s = new String(buf, offset, len);
        s = s.trim();
        s = _startElement + "=" + s;
        v.add(s);
    }

    public void showRecords() {
        xmlObjMap = new HashMap();
        carrierList = new ArrayList();
        String str = "";
        Origin origin = new Origin();
        Destination destination = new Destination();
        Rates rates = new Rates();
        Classes classes = new Classes();
        Weights weights = new Weights();
        Carrier carrier = null;
        int count = 0;
        String correctStr = "";
        for (int i = 0; i < v.size(); i++) {
            str = v.get(i).toString();
            if (str.contains("Origin-ZipCode")) {
                origin.setZipCode(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Origin-City")) {
                origin.setCity(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Origin-State")) {
                origin.setState(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Destination-ZipCode")) {
                destination.setZipCode(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Destination-City")) {
                destination.setCity(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Miles")) {
                rates.setMiles(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Classes-Class")) {
                classes.setClass1(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Weights-Weight")) {
                weights.setWeight(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Weights-cube")) {
                weights.setMeasure(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Carrier-name")) {
                correctStr = "";
                carrier = new Carrier();
                carrier.setName(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Carrier-scac")) {
                carrier.setScac(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Carrier-originterminal")) {
                carrier.setOriginterminal(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Carrier-destinationterminal")) {
                carrier.setDestinationterminal(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Carrier-relation")) {
                carrier.setRelation(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Carrier-days")) {
                carrier.setDays(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Carrier-initialcharge")) {
                carrier.setInitialcharges(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Carrier-fsc") && !str.contains("Carrier-fscpercent")) {
                correctStr = correctStr + str.substring(str.indexOf("=") + 1);
                carrier.setFuelsurcharge(correctStr);
            } else if (str.contains("Carrier-service-name")) {
                Service service = new Service();
                service.setName(str.substring(str.indexOf("=") + 1));
                carrier.setService(service);
            } else if (str.contains("Carrier-asc-total")) {
                AdditionalSC asc = new AdditionalSC();
                asc.setTotal(str.substring(str.indexOf("=") + 1));
                carrier.setAdditionalsc(asc);
            } else if (str.contains("Carrier-finalcharge")) {
                carrier.setFinalcharge(str.substring(str.indexOf("=") + 1));
                carrier.setIndex(String.valueOf(count));
                carrierList.add(carrier);
                count++;
            } else if (str.contains("error")) {
                str = str.substring(str.indexOf("=") + 1);
                str = str.trim();
                if (str.length() > 0) {
                    error.append(str);
                }
            }


        }

        xmlObjMap.put("origin", origin);
        xmlObjMap.put("destination", destination);
        xmlObjMap.put("rates", rates);
        xmlObjMap.put("classes", classes);
        xmlObjMap.put("weights", weights);
        xmlObjMap.put("error", error.toString());

    }

    public Map getXmlObjMap() {
        return this.xmlObjMap;
    }

    /**
     * @return the carrierList
     */
    public List getCarrierList() {
        return carrierList;
    }

    /**
     * @param carrierList the carrierList to set
     */
    public void setCarrierList(List carrierList) {
        this.carrierList = carrierList;
    }
}

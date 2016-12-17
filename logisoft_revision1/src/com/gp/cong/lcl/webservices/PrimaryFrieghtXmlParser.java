package com.gp.cong.lcl.webservices;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.webservices.dom.AdditionalSC;
import com.gp.cong.lcl.webservices.dom.Carrier;
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

public class PrimaryFrieghtXmlParser extends DefaultHandler {

    private String _endElement;
    private String _startElement = "";
    private String _characters;
    Vector v = new Vector();
    String lastMethodCalled = "";
    private Map xmlObjMap;
    private Map<String, Carrier> carrierMap;
    private Map<String, Double> chargesMap;
    private Map<String, String> classMap =  new HashMap<String, String>();
    private Map<String, Double> weightMap = new HashMap<String, Double>();
    private Map<String, Integer> palletMap = new HashMap<String, Integer>();
    private Map<String, Integer> packageMap = new HashMap<String, Integer>();
    private Map<String, String> palletTypeMap = new HashMap<String, String>();
    private Map<String, String> packageTypeMap = new HashMap<String, String>();
    private Map<String, Integer> heightMap = new HashMap<String, Integer>();
    private Map<String, Integer> lengthMap = new HashMap<String, Integer>();
    private Map<String, Integer> widthMap = new HashMap<String, Integer>();
    private Map<String, Double> cubicFeetMap = new HashMap<String, Double>();
    private List carrierList;
    private String shipDate;
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
        carrierMap = new HashMap<String,Carrier>();
        chargesMap = new HashMap<String, Double>();
        carrierList = new ArrayList();
        String str = "";
        Origin origin = new Origin();
        Destination destination = new Destination();
        Rates rates = new Rates();
        Weights weights = new Weights();
        Carrier carrier = null;
        int count = 0;
        String correctStr = "";
        String chargeName = "";
        String chargeAmount = "";
        for (int i = 0; i < v.size(); i++) {
            str = v.get(i).toString();
            if (str.contains("ShipmentID")) {
                rates.setShipmentId(str.substring(str.indexOf("=") + 1));
            }else if(str.contains("Origin-ZipCode")) {
                origin.setZipCode(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Origin-City")) {
                origin.setCity(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Origin-State")) {
                origin.setState(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Destination-ZipCode")) {
                destination.setZipCode(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Destination-City")) {
                destination.setCity(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Destination-State")) {
                destination.setState(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Miles")) {
                rates.setMiles(str.substring(str.indexOf("=") + 1));
            }
//            else if (str.contains("Class::Sequence")) {
//                String classes = str.substring(str.indexOf("=") + 1);
//                String[] splitStr = classes.split("=");
//                classMap.put(splitStr[0].replace("\"", ""), splitStr[1]);
//            } else if (str.contains("Weight::Sequence")) {
//                String weight = str.substring(str.indexOf("=") + 1);
//                String[] splitStr = weight.split("=");
//                weightMap.put(splitStr[0].replace("\"", ""), Double.parseDouble(splitStr[1]));
//            } else if (str.contains("Pallet::Sequence")) {
//                String pallet = str.substring(str.indexOf("=") + 1);
//                String[] splitStr = pallet.split("=");
//                palletMap.put(splitStr[0].replace("\"", ""), splitStr[1]);
//            }
            else if (str.contains("Weights-cube") || str.contains("-cube")) {
                rates.setCubicFeet(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Carrier-name")) {
                correctStr = "";
                carrier = new Carrier();
                carrier.setName(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("charge-name")) {
                chargeName = str.substring(str.indexOf("=") + 1);
                if("hazmat".equalsIgnoreCase(chargeName)){
                    carrier.setHazmat(true);
                }
            } else if (str.contains("charge-amount")) {
                chargeAmount = str.substring(str.indexOf("=") + 1);
                if(CommonUtils.isNotEmpty(chargeName) && CommonUtils.isNotEmpty(chargeAmount)){
                    chargesMap.put(chargeName, Double.parseDouble(chargeAmount));
                    chargeName ="";
                    chargeAmount ="";
                }
            } else if (str.contains("Carrier-scac")) {
                carrier.setScac(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Carrier-originterminal-code")) {
                carrier.setOriginCode(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Carrier-originterminal-name")) {
                carrier.setOriginName(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Carrier-originterminal-address")) {
                carrier.setOriginAddress(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Carrier-originterminal-city")) {
                carrier.setOriginCity(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Carrier-originterminal-state")) {
                carrier.setOriginState(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Carrier-originterminal-zip")) {
                carrier.setOriginZip(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Carrier-originterminal-phone")) {
                carrier.setOriginPhone(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Carrier-originterminal-fax")) {
                carrier.setOriginFax(str.substring(str.indexOf("=") + 1));
            }else if (str.contains("Carrier-destinationterminal-code")) {
                carrier.setDestinationCode(str.substring(str.indexOf("=") + 1));
            }else if (str.contains("Carrier-destinationterminal-name")) {
                carrier.setDestinationName(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Carrier-destinationterminal-address")) {
                carrier.setDestinationAddress(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Carrier-destinationterminal-city")) {
                carrier.setDestinationCity(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Carrier-destinationterminal-state")) {
                carrier.setDestinationState(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Carrier-destinationterminal-zip")) {
                carrier.setDestinationZip(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Carrier-destinationterminal-phone")) {
                carrier.setDestinationPhone(str.substring(str.indexOf("=") + 1));
            } else if (str.contains("Carrier-destinationterminal-fax")) {
                carrier.setDestinationFax(str.substring(str.indexOf("=") + 1));
            }else if (str.contains("Carrier-relation")) {
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
                carrier.setChargeMap(chargesMap);
                carrierList.add(carrier);
                carrierMap.put(carrier.getName(), carrier);
                chargesMap = new HashMap<String,Double>();
                count++;
            } else if (str.contains("error") || str.contains("Error")) {
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
        xmlObjMap.put("weights", weights);
        xmlObjMap.put("error", error.toString());

    }

    public Map getXmlObjMap() {
        return this.xmlObjMap;
    }
    public Map<String,Carrier> getCarrierMap() {
        return this.carrierMap;
    }

    public Map<String, String> getClassMap() {
        return classMap;
    }

    public void setClassMap(Map<String, String> classMap) {
        this.classMap = classMap;
    }

    public Map<String, Integer> getPalletMap() {
        return palletMap;
    }

    public void setPalletMap(Map<String, Integer> palletMap) {
        this.palletMap = palletMap;
    }

    public Map<String, Double> getWeightMap() {
        return weightMap;
    }

    public void setWeightMap(Map<String, Double> weightMap) {
        this.weightMap = weightMap;
    }
  

    public Map<String, Integer> getPackageMap() {
        return packageMap;
    }

    public void setPackageMap(Map<String, Integer> packageMap) {
        this.packageMap = packageMap;
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

    public String getShipDate() {
        return shipDate;
    }

    public void setShipDate(String shipDate) {
        this.shipDate = shipDate;
    }

    public Map<String, Double> getChargesMap() {
        return chargesMap;
    }

    public void setChargesMap(Map<String, Double> chargesMap) {
        this.chargesMap = chargesMap;
    }

    public Map<String, Integer> getHeightMap() {
        return heightMap;
    }

    public void setHeightMap(Map<String, Integer> heightMap) {
        this.heightMap = heightMap;
    }

    public Map<String, Integer> getLengthMap() {
        return lengthMap;
    }

    public void setLengthMap(Map<String, Integer> lengthMap) {
        this.lengthMap = lengthMap;
    }

    public Map<String, Integer> getWidthMap() {
        return widthMap;
    }

    public void setWidthMap(Map<String, Integer> widthMap) {
        this.widthMap = widthMap;
    }

    public Map<String, Double> getCubicFeetMap() {
        return cubicFeetMap;
    }

    public void setCubicFeetMap(Map<String, Double> cubicFeetMap) {
        this.cubicFeetMap = cubicFeetMap;
    }

    public Map<String, String> getPackageTypeMap() {
        return packageTypeMap;
    }

    public void setPackageTypeMap(Map<String, String> packageTypeMap) {
        this.packageTypeMap = packageTypeMap;
    }

    public Map<String, String> getPalletTypeMap() {
        return palletTypeMap;
    }

    public void setPalletTypeMap(Map<String, String> palletTypeMap) {
        this.palletTypeMap = palletTypeMap;
    }
    
}

package com.gp.cong.lcl.webservices;

import com.gp.cong.lcl.dwr.LclSession;
import com.gp.cong.struts.LoadLogisoftProperties;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.log4j.Logger;

public class CallCTSWebServices {

    private static Logger log = Logger.getLogger(CallCTSWebServices.class);

    public LclSession processCTSWebService(LclSession lclSession, String realPath, String fName,
            String fromZip, String toZip, String shipDate, String weight, String measure,
            String chargeFlag,String costFlag, String moduleFlag) {//carrierCharge,carrierCost
        try {
            if (chargeFlag != null && "CARRIER_CHARGE".equalsIgnoreCase(chargeFlag)) {
                String UID = LoadLogisoftProperties.getProperty("Imports".equalsIgnoreCase(moduleFlag)
                        ? "application.CTSDoorDeliveryWebServiceUID" : "application.CTSWebServiceUID");
                String data = URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode("xml", "UTF-8");
                data += "&" + URLEncoder.encode("UID", "UTF-8") + "=" + URLEncoder.encode(UID, "UTF-8");
                data += "&" + URLEncoder.encode("fromzip", "UTF-8") + "=" + URLEncoder.encode(fromZip, "UTF-8");
                data += "&" + URLEncoder.encode("tozip", "UTF-8") + "=" + URLEncoder.encode(toZip, "UTF-8");
                data += "&" + URLEncoder.encode("shipdate", "UTF-8") + "=" + URLEncoder.encode(shipDate, "UTF-8");
                data += "&" + URLEncoder.encode("class1", "UTF-8") + "=" + URLEncoder.encode("70", "UTF-8");
                data += "&" + URLEncoder.encode("weight1", "UTF-8") + "=" + URLEncoder.encode(weight, "UTF-8");
                data += "&" + URLEncoder.encode("cube", "UTF-8") + "=" + URLEncoder.encode(measure, "UTF-8");
                data += "&" + URLEncoder.encode("unitmeasurement", "UTF-8") + "=" + URLEncoder.encode("E", "UTF-8");
                // Send data
                URL url = new URL("http://www.shipwithcts.com/cts/shiprite/rater.cfm");
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();
                // Get the response
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                File file = new File(realPath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                String path = realPath + fName;
                BufferedWriter out = new BufferedWriter(new FileWriter(path));
                String line;
                while ((line = rd.readLine()) != null) {
                    out.write(line + "\n");
                }
                wr.close();
                rd.close();
                out.close();
                CTSXmlParser ctsxml = new CTSXmlParser();
                ctsxml.parseCTSXml(path);
                File f = new File(path);
                f.delete();
                Map xmlObjMap = ctsxml.getXmlObjMap();
                lclSession.setCarrierList(ctsxml.getCarrierList());
                lclSession.setXmlObjMap(xmlObjMap);
            }
            if (costFlag != null && "CARRIER_COST".equalsIgnoreCase(costFlag)) {
                String UidCost = LoadLogisoftProperties.getProperty("Imports".equalsIgnoreCase(moduleFlag)
                        ? "application.CTSDoorDeliveryCostUID" : "application.CTSCostUID");
                String data = URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode("xml", "UTF-8");
                data += "&" + URLEncoder.encode("UID", "UTF-8") + "=" + URLEncoder.encode(UidCost, "UTF-8");
                data += "&" + URLEncoder.encode("fromzip", "UTF-8") + "=" + URLEncoder.encode(fromZip, "UTF-8");
                data += "&" + URLEncoder.encode("tozip", "UTF-8") + "=" + URLEncoder.encode(toZip, "UTF-8");
                data += "&" + URLEncoder.encode("shipdate", "UTF-8") + "=" + URLEncoder.encode(shipDate, "UTF-8");
                data += "&" + URLEncoder.encode("class1", "UTF-8") + "=" + URLEncoder.encode("70", "UTF-8");
                data += "&" + URLEncoder.encode("weight1", "UTF-8") + "=" + URLEncoder.encode(weight, "UTF-8");
                data += "&" + URLEncoder.encode("cube", "UTF-8") + "=" + URLEncoder.encode(measure, "UTF-8");
                data += "&" + URLEncoder.encode("unitmeasurement", "UTF-8") + "=" + URLEncoder.encode("E", "UTF-8");
                // Send data
                URL url = new URL("http://www.shipwithcts.com/cts/shiprite/rater.cfm");
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();
                // Get the response
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                File file = new File(realPath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                String path = realPath + fName;
                BufferedWriter out = new BufferedWriter(new FileWriter(path));
                String line;
                while ((line = rd.readLine()) != null) {
                    out.write(line + "\n");
                }
                wr.close();
                rd.close();
                out.close();
                CTSXmlParser ctsxml = new CTSXmlParser();
                ctsxml.parseCTSXml(path);
                File f = new File(path);
                f.delete();
                Map xmlObjMap = ctsxml.getXmlObjMap();
                lclSession.setCarrierCostList(ctsxml.getCarrierList());
                lclSession.setXmlObjMap(xmlObjMap);
            }
        } catch (Exception e) {
            log.error("Exception in processCTSWebService() method-------- " + e);
        }
        return lclSession;
    }
}

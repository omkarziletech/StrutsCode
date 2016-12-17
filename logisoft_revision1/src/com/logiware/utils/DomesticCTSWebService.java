/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.logiware.utils;

import com.gp.cong.lcl.webservices.CTSXmlParser;
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
import org.apache.log4j.Logger;
/**
 *
 * @author logiware
 */
public class DomesticCTSWebService {

    private static org.apache.log4j.Logger log = Logger.getLogger(DomesticCTSWebService.class);

    public CTSXmlParser processCTSWebService(String realPath, String fName,String fromZip, String toZip, String shipDate, String weight, String measure) {
        CTSXmlParser ctsxml = new CTSXmlParser();
        try {
            String UID = LoadLogisoftProperties.getProperty("application.CTSWebServiceUID");
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
            ctsxml.parseCTSXml(path);
            File f = new File(path);
            boolean success = f.delete();
        } catch (Exception e) {
            log.error("Exception in processCTSWebService() method-------- " + e);
        }
        return ctsxml;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.logiware.domestic;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.webservices.PrimaryFrieghtXmlParser;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.domestic.form.RateQuoteForm;
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

    public PrimaryFrieghtXmlParser processCTSWebService(String realPath, String fileName,RateQuoteForm ratequoteform) {
        PrimaryFrieghtXmlParser ctsxml = new PrimaryFrieghtXmlParser();
        try {
            String UID = LoadLogisoftProperties.getProperty("application.CTSWebServiceUID");
            String data = URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode("xml", "UTF-8");
            data += "&" + URLEncoder.encode("UID", "UTF-8") + "=" + URLEncoder.encode(UID, "UTF-8");
            data += "&" + URLEncoder.encode("fromzip", "UTF-8") + "=" + URLEncoder.encode(ratequoteform.getFromZip(), "UTF-8");
            data += "&" + URLEncoder.encode("tozip", "UTF-8") + "=" + URLEncoder.encode(ratequoteform.getToZip(), "UTF-8");
            data += "&" + URLEncoder.encode("shipdate", "UTF-8") + "=" + URLEncoder.encode(ratequoteform.getShipDate(), "UTF-8");
            data += "&" + URLEncoder.encode("class1", "UTF-8") + "=" + URLEncoder.encode(ratequoteform.getClass1(), "UTF-8");
            data += "&" + URLEncoder.encode("weight1", "UTF-8") + "=" + URLEncoder.encode(ratequoteform.getWeight1(), "UTF-8");
            data += "&" + URLEncoder.encode("pallet1", "UTF-8") + "=" + URLEncoder.encode(""+ratequoteform.getPallet1(), "UTF-8");
            data += "&" + URLEncoder.encode("package1", "UTF-8") + "=" + URLEncoder.encode(""+ratequoteform.getPackage1(), "UTF-8");
            if(CommonUtils.isNotEmpty(ratequoteform.getWeight2())){
                data += "&" + URLEncoder.encode("class2", "UTF-8") + "=" + URLEncoder.encode(ratequoteform.getClass2(), "UTF-8");
                data += "&" + URLEncoder.encode("weight2", "UTF-8") + "=" + URLEncoder.encode(ratequoteform.getWeight2(), "UTF-8");
                data += "&" + URLEncoder.encode("pallet2", "UTF-8") + "=" + URLEncoder.encode(""+ratequoteform.getPallet2(), "UTF-8");
                data += "&" + URLEncoder.encode("package2", "UTF-8") + "=" + URLEncoder.encode(""+ratequoteform.getPackage2(), "UTF-8");
            }
            if(CommonUtils.isNotEmpty(ratequoteform.getWeight3())){
                data += "&" + URLEncoder.encode("class3", "UTF-8") + "=" + URLEncoder.encode(ratequoteform.getClass3(), "UTF-8");
                data += "&" + URLEncoder.encode("weight3", "UTF-8") + "=" + URLEncoder.encode(ratequoteform.getWeight3(), "UTF-8");
                data += "&" + URLEncoder.encode("pallet3", "UTF-8") + "=" + URLEncoder.encode(""+ratequoteform.getPallet3(), "UTF-8");
                data += "&" + URLEncoder.encode("package3", "UTF-8") + "=" + URLEncoder.encode(""+ratequoteform.getPackage3(), "UTF-8");
            }
            if(CommonUtils.isNotEmpty(ratequoteform.getWeight4())){
                data += "&" + URLEncoder.encode("class4", "UTF-8") + "=" + URLEncoder.encode(ratequoteform.getClass4(), "UTF-8");
                data += "&" + URLEncoder.encode("weight4", "UTF-8") + "=" + URLEncoder.encode(ratequoteform.getWeight4(), "UTF-8");
                data += "&" + URLEncoder.encode("pallet4", "UTF-8") + "=" + URLEncoder.encode(""+ratequoteform.getPallet4(), "UTF-8");
                data += "&" + URLEncoder.encode("package4", "UTF-8") + "=" + URLEncoder.encode(""+ratequoteform.getPackage4(), "UTF-8");
            }
            if(CommonUtils.isNotEmpty(ratequoteform.getWeight5())){
                data += "&" + URLEncoder.encode("class5", "UTF-8") + "=" + URLEncoder.encode(ratequoteform.getClass5(), "UTF-8");
                data += "&" + URLEncoder.encode("weight5", "UTF-8") + "=" + URLEncoder.encode(ratequoteform.getWeight5(), "UTF-8");
                data += "&" + URLEncoder.encode("pallet5", "UTF-8") + "=" + URLEncoder.encode(""+ratequoteform.getPallet5(), "UTF-8");
                data += "&" + URLEncoder.encode("package5", "UTF-8") + "=" + URLEncoder.encode(""+ratequoteform.getPackage5(), "UTF-8");
            }
            if(CommonUtils.isNotEmpty(ratequoteform.getWeight6())){
                data += "&" + URLEncoder.encode("class6", "UTF-8") + "=" + URLEncoder.encode(ratequoteform.getClass6(), "UTF-8");
                data += "&" + URLEncoder.encode("weight6", "UTF-8") + "=" + URLEncoder.encode(ratequoteform.getWeight6(), "UTF-8");
                data += "&" + URLEncoder.encode("pallet6", "UTF-8") + "=" + URLEncoder.encode(""+ratequoteform.getPallet6(), "UTF-8");
                data += "&" + URLEncoder.encode("package6", "UTF-8") + "=" + URLEncoder.encode(""+ratequoteform.getPackage6(), "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getCollectionFee())){
                data += "&" + URLEncoder.encode("ADVANCECOLLECTIONFEE", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getHourCharge())){
                data += "&" + URLEncoder.encode("AFTERHOURSCHARGE", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
                data += "&" + URLEncoder.encode("AFTERHOURSCHARGE_VAL", "UTF-8") + "=" + URLEncoder.encode(ratequoteform.getHourChargeNo(), "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getHourDelivery())){
                data += "&" + URLEncoder.encode("AFTERHOURSDELIVERY", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getCharge())){
                data += "&" + URLEncoder.encode("BONDCHARGE", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getCOD())){
                data += "&" + URLEncoder.encode("COD", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
                data += "&" + URLEncoder.encode("COD_VAL", "UTF-8") + "=" + URLEncoder.encode(ratequoteform.getCodVal(), "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getBOLFee())){
                data += "&" + URLEncoder.encode("CHANGEBOLFEE", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getConsolidate())){
                data += "&" + URLEncoder.encode("CONSOLIDATION", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getConstSite())){
                data += "&" + URLEncoder.encode("CONSTRUCTIONSITE", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getCorrectBOL())){
                data += "&" + URLEncoder.encode("CORRECTEDBOL", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getCrossBorderFee())){
                data += "&" + URLEncoder.encode("CROSSBORDERFEE", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getDelayCharge())){
                data += "&" + URLEncoder.encode("DELAYCHARGE", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getDescInspection())){
                data += "&" + URLEncoder.encode("DESCRIPTIONINSPECTION", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getDetentFee())){
                data += "&" + URLEncoder.encode("DETENTIONFEE", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getDriverAssist())){
                data += "&" + URLEncoder.encode("DRIVERASSIST", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getDryRunCharge())){
                data += "&" + URLEncoder.encode("DRYRUNCHARGE", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getExcessLength())){
                data += "&" + URLEncoder.encode("EXCESSLENGTH", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getTradeShow())){
                data += "&" + URLEncoder.encode("EXHIBITIONTRADESHOW", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getExtraLabor())){
                data += "&" + URLEncoder.encode("EXTRALABOR", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
                data += "&" + URLEncoder.encode("EXTRALABOR_VAL", "UTF-8") + "=" + URLEncoder.encode(ratequoteform.getExtraLaborVal(), "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getHazmat())){
                data += "&" + URLEncoder.encode("HAZMAT", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getHomeLandSec())){
                data += "&" + URLEncoder.encode("HOMELANDSECURITY", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getHomeLandSec())){
                data += "&" + URLEncoder.encode("HOMELANDSECURITY", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getInbondFreight())){
                data += "&" + URLEncoder.encode("INBONDFREIGHT", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getInsideDelivery())){
                data += "&" + URLEncoder.encode("INSIDEDELIVERY", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getLayOverFee())){
                data += "&" + URLEncoder.encode("LAYOVERFEE", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getLiftGateDelivery())){
                data += "&" + URLEncoder.encode("LIFTGATEATDELIVERY", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getLiftGatePickup())){
                data += "&" + URLEncoder.encode("LIFTGATEATPICKUP", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getLimitAccess())){
                data += "&" + URLEncoder.encode("LIMITEDACCESS", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getLumperFee())){
                data += "&" + URLEncoder.encode("LUMPERFEE", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
                data += "&" + URLEncoder.encode("LUMPERFEE_VAL", "UTF-8") + "=" + URLEncoder.encode(ratequoteform.getLumperFeeVal(), "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getMarkTag())){
                data += "&" + URLEncoder.encode("MARKINGANDTAGGING", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
                data += "&" + URLEncoder.encode("MARKINGANDTAGGING_VAL", "UTF-8") + "=" + URLEncoder.encode(ratequoteform.getMarkTagVal(), "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getMilitaryDelivery())){
                data += "&" + URLEncoder.encode("MILITARYBASEDELIVERY", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getNotify())){
                data += "&" + URLEncoder.encode("NOTIFICATION", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getPermitFee())){
                data += "&" + URLEncoder.encode("PERMITFEE", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getPierCharge())){
                data += "&" + URLEncoder.encode("PIERCHARGE", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getPortCharge())){
                data += "&" + URLEncoder.encode("PORTCHARGE", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getReconsignment())){
                data += "&" + URLEncoder.encode("RECONSIGNMENT", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getRedelivery())){
                data += "&" + URLEncoder.encode("REDELIVERY", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getRemoveDebris())){
                data += "&" + URLEncoder.encode("REMOVEDEBRIS", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getResidentDelivery())){
                data += "&" + URLEncoder.encode("RESIDENTIALDELIVERY", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getSatDelivery())){
                data += "&" + URLEncoder.encode("SATURDAYDELIVERY", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
                data += "&" + URLEncoder.encode("SATURDAYDELIVERY_VAL", "UTF-8") + "=" + URLEncoder.encode(ratequoteform.getSatDeliveryVal(), "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getSortSegregate())){
                data += "&" + URLEncoder.encode("SORTINGSEGREGATING", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
                data += "&" + URLEncoder.encode("SORTINGSEGREGATING_VAL", "UTF-8") + "=" + URLEncoder.encode(ratequoteform.getSortSegregateVal(), "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getStopCharge())){
                data += "&" + URLEncoder.encode("STOPOFFCHARGE", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getTarpCharge())){
                data += "&" + URLEncoder.encode("TARPCHARGE", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getTeamCharge())){
                data += "&" + URLEncoder.encode("TEAMCHARGE", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getTruckNotUsed())){
                data += "&" + URLEncoder.encode("TRUCKNOTUSED", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getTruckOrderedNotUsed())){
                data += "&" + URLEncoder.encode("TRUCKORDEREDNOTUSED", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getUnPack())){
                data += "&" + URLEncoder.encode("UNPACKING", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getHolidayDel())){
                data += "&" + URLEncoder.encode("WEEKENDORHOLIDAYDEL", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
                data += "&" + URLEncoder.encode("WEEKENDORHOLIDAYDEL_VAL", "UTF-8") + "=" + URLEncoder.encode(ratequoteform.getHolidayDelVal(), "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getWeightInspect())){
                data += "&" + URLEncoder.encode("WEIGHTINSPECTION", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            if("on".equalsIgnoreCase(ratequoteform.getWeightVerify())){
                data += "&" + URLEncoder.encode("WEIGHTVERIFICATION", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
            }
            data += "&" + URLEncoder.encode("cube", "UTF-8") + "=" + URLEncoder.encode(ratequoteform.getCubicFeet(), "UTF-8");
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
            String path = realPath + fileName;
            BufferedWriter out = new BufferedWriter(new FileWriter(path));
            String line;
            while ((line = rd.readLine()) != null) {
                out.write(line + "\n");
            }
            wr.close();
            rd.close();
            out.close();
            ctsxml.parseCTSXml(path);
            ctsxml.setShipDate(ratequoteform.getShipDate());
            File f = new File(path);
            boolean success = f.delete();
        } catch (Exception e) {
            log.error("Exception in processCTSWebService() method-------- " + e);
        }
        return ctsxml;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.logiware.domestic;

import com.gp.cong.logisoft.domain.User;
import com.gp.cong.struts.LoadLogisoftProperties;
import java.util.*;
import java.io.*;

/**
 *
 * @author Shanmugam
 */
public class CreateBOLXml {
    public String createXml(DomesticBooking booking,List<DomesticPurchaseOrder> purchaseOrderList,User user) {
        try {
            String filename = "BOL_" + booking.getBookingNumber() + ".xml";
            String fileLocation = LoadLogisoftProperties.getProperty("reportLocation") + "/CTSQuote/"+filename;
            File dir = new File(LoadLogisoftProperties.getProperty("reportLocation") + "/CTSQuote/");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileOutputStream fout = new FileOutputStream(fileLocation);
            BufferedOutputStream bout = new BufferedOutputStream(fout);
            OutputStreamWriter out = new OutputStreamWriter(bout, "8859_1");
            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version=\"1.0\" ");
	    sb.append("encoding=\"ISO-8859-1\"?>");
            sb.append("<Transmission xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"http://www.shipwithcts.com/cts/BOL/srdapp.xsd\">");
            sb.append("<SenderID>").append("420").append("</SenderID>");
            sb.append("<ReceiverID>").append("CTS").append("</ReceiverID>");
            sb.append("<MessageType>").append("B").append("</MessageType>");
            sb.append("<Contact>");
            sb.append("<Name>").append(user.getLoginName()).append("</Name>");
            sb.append("<Phone>").append(user.getTelephone()).append("</Phone>");
            sb.append("<Email>").append(user.getEmail()).append("</Email>");
            sb.append("</Contact>");
            sb.append("<SpecialInstructions>").append("Deliver before 3pm").append("</SpecialInstructions>");
            sb.append("<BillofLading>").append("123456789").append("</BillofLading>");
            sb.append("<ReferenceID>").append("</ReferenceID>");
            sb.append("<Terms>").append("P").append("</Terms>");
            sb.append("<PickupDate>").append("2006-03-20").append("</PickupDate>");
            sb.append("<DeliveryDate>").append("2006-03-24").append("</DeliveryDate>");
            sb.append("<Carrier>");
            sb.append("<Name>").append(booking.getCarrierName()).append("</Name>");
            sb.append("<Code>").append(booking.getScac()).append("</Code>");
            sb.append("</Carrier>");
            sb.append("<Shipper>");
            sb.append("<Name>").append(booking.getShipperName()).append("</Name>");
            sb.append("<Address1>").append(booking.getShipperAddress1()).append("</Address1>");
            sb.append("<Address2>").append(booking.getShipperAddress2()).append("</Address2>");
            sb.append("<City>").append(booking.getShipperCity()).append("</City>");
            sb.append("<State>").append(booking.getShipperState()).append("</State>");
            sb.append("<ZipCode>").append(booking.getShipperZipcode()).append("</ZipCode>");
            sb.append("<Contact>");
            sb.append("<Name>").append(booking.getShipperContactName()).append("</Name>");
            sb.append("<Phone>").append(booking.getShipperContactPhone()).append("</Phone>");
            sb.append("<Fax>").append(booking.getShipperContactFax()).append("</Fax>");
            sb.append("<Email>").append(booking.getShipperContactEmail()).append("</Email>");
            sb.append("</Contact>");
            sb.append("<Hours>").append(booking.getShipperHours()).append("</Hours>");
            sb.append("</Shipper>");
            sb.append("<Consignee>");
            sb.append("<Name>").append(booking.getConsigneeName()).append("</Name>");
            sb.append("<Address1>").append(booking.getConsigneeAddress1()).append("</Address1>");
            sb.append("<Address2>").append(booking.getConsigneeAddress2()).append("</Address2>");
            sb.append("<City>").append(booking.getConsigneeCity()).append("</City>");
            sb.append("<State>").append(booking.getConsigneeState()).append("</State>");
            sb.append("<ZipCode>").append(booking.getShipperZipcode()).append("</ZipCode>");
            sb.append("<Contact>");
            sb.append("<Name>").append(booking.getConsigneeContactName()).append("</Name>");
            sb.append("<Phone>").append(booking.getConsigneeContactPhone()).append("</Phone>");
            sb.append("<Fax>").append(booking.getConsigneeContactFax()).append("</Fax>");
            sb.append("<Email>").append(booking.getConsigneeContactEmail()).append("</Email>");
            sb.append("</Contact>");
            sb.append("<Hours>").append(booking.getConsigneeHours()).append("</Hours>");
            sb.append("</Consignee>");
            sb.append("<BillTo>");
            sb.append("<Name>").append(booking.getBilltoName()).append("</Name>");
            sb.append("<Address1>").append(booking.getBilltoAddress1()).append("</Address1>");
            sb.append("<Address2>").append(booking.getBilltoAddress2()).append("</Address2>");
            sb.append("<City>").append(booking.getBilltoCity()).append("</City>");
            sb.append("<State>").append(booking.getBilltoState()).append("</State>");
            sb.append("<ZipCode>").append(booking.getBilltoZipcode()).append("</ZipCode>");
            sb.append("</BillTo>");
            //Purchase Order start
            sb.append("<PurchaseOrders>");
            for (DomesticPurchaseOrder purchaseOrder : purchaseOrderList) {
            sb.append("<OrderNumber>").append(purchaseOrder.getPurchaseOrderNo()).append("</OrderNumber>");
            sb.append("<Packages>").append(purchaseOrder.getPackageQuantity()).append("</Packages>");
            sb.append("<Weight>").append(purchaseOrder.getWeight()).append("</Weight>");
            sb.append("<PalletSlip>").append("TRUE").append("</PalletSlip>");
            sb.append("<ExtraInfo>").append(purchaseOrder.getExtraInfo()).append("</ExtraInfo>");
            }
            sb.append("</PurchaseOrders>");
            //Line details start
            sb.append("<LineDetail>");
            for (DomesticPurchaseOrder purchaseOrder : purchaseOrderList) {
                sb.append("<Line>");
                sb.append("<Product>");
                sb.append("<Name>").append(purchaseOrder.getProductName()).append("</Name>");
                sb.append("<Description>").append(purchaseOrder.getDescription()).append("</Description>");
                sb.append("<Description2>").append(purchaseOrder.getDescription()).append("</Description2>");
                sb.append("<Hazmat>").append(purchaseOrder.isHazmat()).append("</Hazmat>");
                sb.append("<HazmatNumber>").append(purchaseOrder.getHazmatNumber()).append("</HazmatNumber>");
                sb.append("<NMFC>").append(purchaseOrder.getNmfc()).append("</NMFC>");
                sb.append("<Class>").append(purchaseOrder.getClasses()).append("</Class>");
                sb.append("</Product>");
                sb.append("<HandlingUnit>");
                sb.append("<Type>").append(purchaseOrder.getHandlingUnitType()).append("</Type>");
                sb.append("<Quantity>").append(purchaseOrder.getHandlingUnitQuantity()).append("</Quantity>");
                sb.append("</HandlingUnit>");
                sb.append("<Package>");
                sb.append("<Type>").append(purchaseOrder.getPackageType()).append("</Type>");
                sb.append("<Quantity>").append(purchaseOrder.getPackageQuantity()).append("</Quantity>");
                sb.append("</Package>");
                sb.append("<Length>").append(purchaseOrder.getLength()).append("</Length>");
                sb.append("<Width>").append(purchaseOrder.getWidth()).append("</Width>");
                sb.append("<Height>").append(purchaseOrder.getHeight()).append("</Height>");
                sb.append("<Cube>").append(purchaseOrder.getCube()).append("</Cube>");
                sb.append("<Weight>").append(purchaseOrder.getWeight()).append("</Weight>");
                sb.append("</Line>");
            }
            sb.append("</LineDetail>");
            sb.append("<ExtraServices>");
            sb.append("<ExtraService>").append("LIFT").append("</ExtraService>");
            sb.append("</ExtraServices>");
            sb.append("<DateStamp>").append("2006-03-20").append("</DateStamp>");
            sb.append("<TimeStamp>").append("09:22:19").append("</TimeStamp>");
            sb.append("</Transmission>");
            out.write(sb.toString());
            out.flush();  // Don't forget to flush!
            out.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}

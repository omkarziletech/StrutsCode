/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.ims.client;

import com.logiware.cts.Srdapp;
import com.logiware.cts.SrdappService;
import com.logiware.ims.IMSServicePort;
import com.logiware.ims.IMSServiceService;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Owner
 */
public class IMSTest {
    //USEAG
    //USNYC
    public static void main(String args[]){
//            IMSServicePort imsService = new IMSServiceService().getIMSServicePort();
//            String result = imsService.getQuote("3cca0150c7962ef8ea417e2f32ba1ff3", "rlowe", encrypt("testing1","77f7275b86bba27b0d19469ab155da37"), 1,"ALL","D","L","L","X","ALL","DV/HC","NEW YORK, NY","EATONTOWN, NJ","NEW YORK, NY","","N","N","N");
            String pickUpDoc = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><Transmission xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"http://www.shipwithcts.com/cts/BOL/srdapp.xsd\"><SenderID>420</SenderID><ReceiverID>CTS</ReceiverID><MessageType>B</MessageType><Contact><Name>gokul</Name><Phone>866 3266648</Phone><Email>gokulnath@logiwareinc.com</Email></Contact><SpecialInstructions>Deliver before 3pm</SpecialInstructions><BillofLading>123456789</BillofLading><ReferenceID></ReferenceID><Terms>P</Terms><PickupDate>2006-03-20</PickupDate><DeliveryDate>2006-03-24</DeliveryDate><Carrier><Name>UPS FREIGHT - Guaranteed</Name><Code>ertyu</Code></Carrier><Shipper><Name>abc company</Name><Address1>address line 1</Address1><Address2></Address2><City>ATLANTA</City><State>AL</State><ZipCode>232323</ZipCode><Contact><Name>shanmugam</Name><Phone>456789</Phone><Fax>232323</Fax><Email>sharang82@gmail.com</Email></Contact><Hours></Hours></Shipper><Consignee><Name>xyz company</Name><Address1>line1line2</Address1><Address2></Address2><City>miami</City><State>ma</State><ZipCode>232323</ZipCode><Contact><Name>dsad23324</Name><Phone>23424</Phone><Fax>43324</Fax><Email>33</Email></Contact><Hours></Hours></Consignee><BillTo><Name>CTS C/O ABC Company</Name><Address1>1915 Vaughn Rd</Address1><Address2></Address2><City>Kennesaw</City><State>GA</State><ZipCode>45678</ZipCode></BillTo><PurchaseOrders><OrderNumber>123456</OrderNumber><Packages>5</Packages><Weight>300.0</Weight><PalletSlip>TRUE</PalletSlip><ExtraInfo>LIFT</ExtraInfo><OrderNumber>45678</OrderNumber><Packages>5</Packages><Weight>500.0</Weight><PalletSlip>TRUE</PalletSlip><ExtraInfo>ghjk,</ExtraInfo><OrderNumber>3456789</OrderNumber><Packages>5</Packages><Weight>400.0</Weight><PalletSlip>TRUE</PalletSlip><ExtraInfo>werty</ExtraInfo></PurchaseOrders><LineDetail><Line><Product><Name>Toys</Name><Description>test</Description><Description2>test</Description2><Hazmat>False</Hazmat><HazmatNumber>345678</HazmatNumber><NMFC>345678</NMFC><Class>70</Class></Product><HandlingUnit><Type>test</Type><Quantity>5</Quantity></HandlingUnit><Package><Type>Bag</Type><Quantity>5</Quantity></Package><Length>30</Length><Width>10</Width><Height>20</Height><Cube>200.0</Cube><Weight>300.0</Weight></Line><Line><Product><Name>ghjk</Name><Description>ghjk</Description><Description2>ghjk</Description2><Hazmat>False</Hazmat><HazmatNumber>njmk,</HazmatNumber><NMFC>45678</NMFC><Class>50</Class></Product><HandlingUnit><Type>ertyu</Type><Quantity>10</Quantity></HandlingUnit><Package><Type>bag</Type><Quantity>10</Quantity></Package><Length>10</Length><Width>10</Width><Height>10</Height><Cube>200.0</Cube><Weight>500.0</Weight></Line><Line><Product><Name>wert</Name><Description>ewrty</Description><Description2>ewrty</Description2><Hazmat>False</Hazmat><HazmatNumber>rtty</HazmatNumber><NMFC>ert</NMFC><Class>60</Class></Product><HandlingUnit><Type>erty</Type><Quantity>10</Quantity></HandlingUnit><Package><Type>ghjs</Type><Quantity>23</Quantity></Package><Length>10</Length><Width>10</Width><Height>10</Height><Cube>200.0</Cube><Weight>400.0</Weight></Line></LineDetail><ExtraServices><ExtraService>LIFT</ExtraService></ExtraServices><DateStamp>2006-03-20</DateStamp><TimeStamp>09:22:19</TimeStamp></Transmission>";
            try {
                Srdapp ctsService = new SrdappService().getSrdappCfc();
                String result = ctsService.bImport("420", pickUpDoc);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    
    public static String encrypt(String password, String privateKey){
        String result = "";
        char thisChar;
        char keyChar;
        Integer startChar;
        for(int i=0; i < password.length();i++) {
            thisChar = password.charAt(i);
            startChar = (i % privateKey.length()) - 1;
            if(startChar < 0) {
                startChar = privateKey.length() + startChar;
            }
            keyChar = privateKey.charAt(startChar);
            int j = thisChar;
            int k = keyChar;
            result += (j + k) + "|";
        }
        return result;
    }
    
}

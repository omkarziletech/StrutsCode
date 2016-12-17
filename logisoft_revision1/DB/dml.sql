

/**
**This file is too big(>1MB). So, the content before March 2016 were deleted.
**/

-- Search Query Modification Lcl Exports Item by Aravindhan.V on 1th April 2016
DELIMITER $$

DROP FUNCTION IF EXISTS Filenumber_frontUnloccode$$

CREATE  FUNCTION `Filenumber_frontUnloccode` (pFileNumber VARCHAR(50),pFileNumberId INT(20),pID INT (11),pTranshipment INT(1),pShortShip INT(1)) 
RETURNS VARCHAR(255) CHARSET utf8 READS SQL DATA DETERMINISTIC
MAIN : BEGIN
DECLARE pUnCode VARCHAR(255) DEFAULT NULL;
DECLARE mCode VARCHAR(255) DEFAULT NULL;

    IF (ISNULL(pID) = FALSE AND pID > 0 ) THEN
        SELECT `un_loc_code` INTO pUnCode FROM `un_location` WHERE `id` = pID LIMIT 1 ;
    END IF ;
    IF (ISNULL(pTranshipment) = FALSE AND TRIM(pTranshipment) <> "" AND pTranshipment = 1 )THEN
       SELECT "IMP" INTO mCode;
    END IF;
    IF (ISNULL(pShortShip) = FALSE AND TRIM(pShortShip) <> "" AND pShortShip=1)THEN
        SELECT CONCAT('ZZ',short_ship_sequence) INTO mCode FROM `lcl_file_number` 
        WHERE `id` = pFileNumberId LIMIT 1;
    END IF;
    IF (ISNULL(pUnCode) = FALSE AND TRIM(pUnCode) <> "" AND pShortShip=0) THEN
         SELECT SUBSTR(pUnCode,3) INTO mCode;
      END IF;
    RETURN mCode;
END MAIN$$
DELIMITER ;

DELIMITER $$

DROP FUNCTION IF EXISTS `getHouseBLForConsolidateDr`$$

CREATE FUNCTION `getHouseBLForConsolidateDr`(inputFileId BIGINT) RETURNS BIGINT(20)
    READS SQL DATA   DETERMINISTIC
MAIN : BEGIN
   DECLARE result_fileId BIGINT DEFAULT 0;
   IF (ISNULL(inputFileId) = FALSE AND inputFileId > 0 ) THEN 
     SELECT bl.file_number_id INTO result_fileId FROM lcl_consolidation l1
     JOIN lcl_bl bl ON bl.file_number_id =l1.lcl_file_number_id_a
     WHERE l1.lcl_file_number_id_a  = inputFileId OR l1.lcl_file_number_id_b  = inputFileId LIMIT 1;
    IF (ISNULL(result_fileId) = TRUE OR result_fileId <= 0) THEN 
     SELECT bl.file_number_id INTO result_fileId FROM lcl_consolidation l2  
     JOIN lcl_bl bl ON bl.file_number_id =l2.lcl_file_number_id_b
     WHERE l2.lcl_file_number_id_a  = inputFileId OR l2.lcl_file_number_id_b  = inputFileId  LIMIT 1;
     END IF;	
   END IF ;
   IF (ISNULL(result_fileId) = TRUE OR result_fileId <= 0 )  THEN  
   SELECT inputFileId INTO result_fileId ;
   END IF ;
   RETURN result_fileId;
 END MAIN$$

DELIMITER ;

-- Mantis item 11025 lcl imports by Priyanka Rachote on 1 apr 2016

DELIMITER $$

DROP FUNCTION IF EXISTS `LclContactGetEmailFaxForStatusDailyUpdate`$$

CREATE  FUNCTION `LclContactGetEmailFaxForStatusDailyUpdate`(
  pFileId BIGINT (20),
  pCodeEmail VARCHAR (5),
  pCodeFax VARCHAR (5)
) RETURNS TEXT CHARSET utf8
    READS SQL DATA
    DETERMINISTIC
MAIN :
BEGIN
  DECLARE mToAddress TEXT DEFAULT NULL ;
  IF (
    ISNULL(pFileId) = FALSE 
    AND pFileId > 0 
    AND ISNULL(pCodeEmail) = FALSE 
    AND pCodeEmail <> '' 
    AND ISNULL(pCodeFax) = FALSE 
    AND pCodeFax <> ''
  ) 
  THEN 
  SELECT 
    GROUP_CONCAT(DISTINCT contact.to_address) 
  INTO
    mToAddress
  FROM
    (
      
      (SELECT 
        GROUP_CONCAT(
          DISTINCT 
          CASE
            genericcode.`code` 
            WHEN pCodeEmail 
            THEN TRIM(LOWER(contact.`email`)) 
            WHEN pCodeFax 
            THEN TRIM(contact.`fax`) 
            ELSE NULL 
          END
        ) AS to_address 
      FROM
        `lcl_booking` booking,
        `cust_contact` contact,
        `genericcode_dup` genericcode 
      WHERE booking.`file_number_id` = pFileId 
        AND contact.`acct_no` = booking.`cons_acct_no` 
        AND genericcode.`id` = contact.`code_f` 
        AND genericcode.`code` IN (pCodeEmail, pCodeFax)) 
      UNION
      (SELECT 
        GROUP_CONCAT(
          DISTINCT 
          CASE
            genericcode.`code` 
            WHEN pCodeEmail 
            THEN TRIM(LOWER(contact.`email`)) 
            WHEN pCodeFax 
            THEN TRIM(contact.`fax`) 
            ELSE NULL 
          END
        ) AS to_address 
      FROM
        `lcl_booking` booking,
        `cust_contact` contact,
        `genericcode_dup` genericcode 
      WHERE booking.`file_number_id` = pFileId 
        AND contact.`acct_no` = booking.`noty_acct_no` 
        AND genericcode.`id` = contact.`code_f` 
        AND genericcode.`code` IN (pCodeEmail, pCodeFax)) 
      UNION
      (SELECT 
        GROUP_CONCAT(
          DISTINCT 
          CASE
            genericcode.`code` 
            WHEN pCodeEmail 
            THEN TRIM(LOWER(contact.`email`)) 
            WHEN pCodeFax 
            THEN TRIM(contact.`fax`) 
            ELSE NULL 
          END
        ) AS to_address 
      FROM
        `lcl_booking` booking,
        `lcl_contact` noty2_contact,
        `cust_contact` contact,
        `genericcode_dup` genericcode 
      WHERE booking.`file_number_id` = pFileId 
        AND noty2_contact.`id` = booking.`noty2_contact_id` 
        AND contact.`acct_no` = noty2_contact.`tp_acct_no` 
        AND genericcode.`id` = contact.`code_f` 
        AND genericcode.`code` IN (pCodeEmail, pCodeFax)) 
    
    ) AS contact ;
  END IF ;
  RETURN mToAddress ;
END MAIN$$

DELIMITER ;


-- > Mantis Item-11341(FCL Exports) by Kuppusamy on 1 April 2016

DELIMITER $$

DROP TRIGGER  `gen_tr_BookingAudit_U`$$

CREATE
    TRIGGER `gen_tr_BookingAudit_U` AFTER UPDATE ON `booking_fcl`
    FOR EACH ROW BEGIN
       DECLARE updated_values TEXT;
       DECLARE update_flag TEXT;
			DECLARE table_name VARCHAR(100);
			DECLARE table_id BIGINT;
			IF (OLD.OriginTerminal != NULL OR OLD.OriginTerminal != '') AND NEW.OriginTerminal != OLD.OriginTerminal THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'OriginTerminal',OLD.OriginTerminal,NEW.OriginTerminal));
			END IF;
			IF (OLD.PortofOrgin != NULL OR OLD.PortofOrgin != '') AND NEW.PortofOrgin != OLD.PortofOrgin THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'PortofOrgin',OLD.PortofOrgin,NEW.PortofOrgin));
			END IF;
			IF (OLD.ExportDevliery != NULL OR OLD.ExportDevliery != '') AND NEW.ExportDevliery != OLD.ExportDevliery THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'ExportDevliery',OLD.ExportDevliery,NEW.ExportDevliery));
			END IF;
			IF  ((OLD.Destination IS NULL AND NEW.Destination IS NOT NULL) OR NEW.Destination != OLD.Destination) THEN
			  SET update_flag = TRUE;
			  IF (OLD.Destination != NULL OR OLD.Destination != '')THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Destination',OLD.Destination,NEW.Destination));
			  END IF;
			END IF;
			IF (OLD.portofDischarge != NULL OR OLD.portofDischarge != '') AND NEW.portofDischarge != OLD.portofDischarge THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'portofDischarge',OLD.portofDischarge,NEW.portofDischarge));
			END IF;
			IF ((OLD.sslname IS NULL AND NEW.sslname IS NOT NULL) OR  NEW.sslname != OLD.sslname)THEN
			  SET update_flag  = TRUE;
			  IF (OLD.sslname != NULL OR OLD.sslname != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'sslname',OLD.sslname,NEW.sslname));
			  END IF;
			END IF;
			IF (OLD.comdesc != NULL OR OLD.comdesc != '') AND NEW.comdesc != OLD.comdesc THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'comdesc',OLD.comdesc,NEW.comdesc));
			END IF;
			IF (OLD.SSBookingNo != NULL OR  OLD.SSBookingNo != '') AND NEW.SSBookingNo != OLD.SSBookingNo THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'SSBookingNo',OLD.SSBookingNo,NEW.SSBookingNo));
			END IF;
			IF (OLD.TelNo != NULL OR OLD.TelNo != '') AND NEW.TelNo != OLD.TelNo THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Contact/Customer Tel',OLD.TelNo,NEW.TelNo));
			END IF;
			IF (OLD.SSLineBookingRep != NULL OR OLD.SSLineBookingRep != '') AND NEW.SSLineBookingRep != OLD.SSLineBookingRep THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Booking Contact/Customer',OLD.SSLineBookingRep,NEW.SSLineBookingRep));
			END IF;
			IF (OLD.bookEmail != NULL OR OLD.bookEmail != '') AND NEW.bookEmail != OLD.bookEmail THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Contact/Customer Email',OLD.bookEmail,NEW.bookEmail));
			END IF;
			IF (OLD.rail_cut_off != NULL OR OLD.rail_cut_off != '') AND NEW.rail_cut_off != OLD.rail_cut_off THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'rail cut off',OLD.rail_cut_off,NEW.rail_cut_off));
			END IF;
			IF (( OLD.PortCutOff IS NULL AND NEW.PortCutOff IS NOT NULL)OR NEW.PortCutOff != OLD.PortCutOff )THEN
			  SET update_flag = TRUE;
			  IF (OLD.PortCutOff != NULL OR OLD.PortCutOff != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Container Cut off',OLD.PortCutOff,NEW.PortCutOff));
			  END IF;
			END IF;
			IF (OLD.doc_cut_off != NULL OR OLD.doc_cut_off != '') AND NEW.doc_cut_off != OLD.doc_cut_off THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'doc cut off',OLD.doc_cut_off,NEW.doc_cut_off));
			  SET update_flag = TRUE;
			END IF;
			IF (OLD.VoyDocCutOff != NULL OR OLD.VoyDocCutOff != '') AND NEW.VoyDocCutOff != OLD.VoyDocCutOff THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'VoyDocCutOff',OLD.VoyDocCutOff,NEW.VoyDocCutOff));
			END IF;
			IF (OLD.CutofDate != NULL OR OLD.CutofDate != '') AND NEW.CutofDate != OLD.CutofDate THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'CutofDate',OLD.CutofDate,NEW.CutofDate));
			END IF;
			IF (OLD.Vessel != NULL OR OLD.Vessel != '') AND NEW.Vessel != OLD.Vessel THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Vessel',OLD.Vessel,NEW.Vessel));
			END IF;
			IF (OLD.comcode != NULL OR OLD.comcode != '') AND NEW.comcode != OLD.comcode THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'comcode',OLD.comcode,NEW.comcode));
			END IF;
			IF (OLD.VoyageCarrier != NULL OR OLD.VoyageCarrier != '') AND NEW.VoyageCarrier != OLD.VoyageCarrier THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'ss voy',OLD.VoyageCarrier,NEW.VoyageCarrier));
			END IF;
			IF (OLD.ETD != NULL OR OLD.ETD != '') AND NEW.ETD != OLD.ETD THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'ETD',OLD.ETD,NEW.ETD));
			END IF;
			IF (OLD.VoyageInternal != NULL OR OLD.VoyageInternal != '') AND NEW.VoyageInternal != OLD.VoyageInternal THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Voy Internal(CFCL)',OLD.VoyageInternal,NEW.VoyageInternal));
			END IF;
			IF (OLD.ETA != NULL OR OLD.ETA != '') AND NEW.ETA != OLD.ETA THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'ETA',OLD.ETA,NEW.ETA));
			END IF;
			IF ((OLD.hazmat IS NULL AND NEW.hazmat IS NOT NULL) OR NEW.hazmat != OLD.hazmat) THEN
			  SET update_flag = TRUE;
			  IF (OLD.hazmat != NULL OR OLD.hazmat !='') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'hazmat',OLD.hazmat,NEW.hazmat));
			  END IF;
			END IF;
			IF ((OLD.PrepaidCollect IS NULL AND NEW.PrepaidCollect IS NOT NULL) OR NEW.PrepaidCollect != OLD.PrepaidCollect )THEN
			  SET update_flag = TRUE;
			  IF (OLD.PrepaidCollect != NULL OR OLD.PrepaidCollect != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Prepaid/Collect',OLD.PrepaidCollect,NEW.PrepaidCollect));
			  END IF;
			END IF;
		        IF (OLD.file_type != NULL OR OLD.file_type != '') AND NEW.file_type != OLD.file_type THEN
				SET updated_values = (SELECT `concat_string`(updated_values,'filetype',OLD.file_type,NEW.file_type));
			END IF;
			IF (OLD.BookingComplete != NULL OR OLD.BookingComplete != '') AND NEW.BookingComplete != OLD.BookingComplete THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'BookingComplete',OLD.BookingComplete,NEW.BookingComplete));
			END IF;
			IF ((OLD.BilltoCode IS NULL AND NEW.BilltoCode IS NOT NULL) OR NEW.BilltoCode != OLD.BilltoCode )THEN
  			  SET update_flag = TRUE;
			  IF (OLD.BilltoCode != NULL OR OLD.BilltoCode != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'BilltoCode',OLD.BilltoCode,NEW.BilltoCode));
			  END IF;
			END IF;
			IF (OLD.account_name != NULL OR OLD.account_name != '') AND NEW.account_name != OLD.account_name THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'account name',OLD.account_name,NEW.account_name));
			END IF;
			IF (OLD.account_number != NULL OR OLD.account_number != '') AND NEW.account_number != OLD.account_number THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'account number',OLD.account_number,NEW.account_number));
			END IF;
			IF (OLD.move_type != NULL OR OLD.move_type != '') AND NEW.move_type != OLD.move_type THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'move type',OLD.move_type,NEW.move_type));
			END IF;
			IF((OLD.Zip IS NULL AND NEW.Zip IS NOT NULL) OR  NEW.Zip != OLD.Zip) THEN
			  SET update_flag = TRUE;
			  IF (OLD.Zip != NULL OR OLD.Zip != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Zip',OLD.Zip,NEW.Zip));
			  END IF;
			END IF;
			IF (OLD.alternate_agent != NULL OR OLD.alternate_agent != '') AND NEW.alternate_agent != OLD.alternate_agent THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'alternate agent',OLD.alternate_agent,NEW.alternate_agent));
			END IF;
			IF (OLD.ramp_city != NULL OR OLD.ramp_city != '') AND NEW.ramp_city != OLD.ramp_city THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'ramp city',OLD.ramp_city,NEW.ramp_city));
			END IF;
			IF (OLD.no_days != NULL OR OLD.no_days != '') AND NEW.no_days != OLD.no_days THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'no days',OLD.no_days,NEW.no_days));
			END IF;
			IF (OLD.agent != NULL OR OLD.agent != '') AND NEW.agent != OLD.agent THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'agent',OLD.agent,NEW.agent));
			END IF;
			IF (OLD.issuing_terminal != NULL OR OLD.issuing_terminal != '') AND NEW.issuing_terminal != OLD.issuing_terminal THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'issuing terminal',OLD.issuing_terminal,NEW.issuing_terminal));
			END IF;
			IF (OLD.agent_no != NULL OR OLD.agent_no != '') AND NEW.agent_no != OLD.agent_no THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'agent no',OLD.agent_no,NEW.agent_no));
			END IF;
			IF ((OLD.routed_by_agent IS NULL AND NEW.routed_by_agent IS NOT NULL) OR NEW.routed_by_agent != OLD.routed_by_agent) THEN
			  SET update_flag = TRUE;
			  SET updated_values = (SELECT `concat_string`(updated_values,'routed by agent',OLD.routed_by_agent,NEW.routed_by_agent));
			END IF;
			IF (OLD.routedby_agents_country != NULL OR OLD.routedby_agents_country != '') AND NEW.routedby_agents_country != OLD.routedby_agents_country THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'routedby agents country',OLD.routedby_agents_country,NEW.routedby_agents_country));
			END IF;
			IF (OLD.dest_remarks != NULL OR OLD.dest_remarks != '') AND NEW.dest_remarks != OLD.dest_remarks THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'dest remarks',OLD.dest_remarks,NEW.dest_remarks));
			END IF;
			IF (OLD.rates_remarks != NULL OR OLD.rates_remarks != '') AND NEW.rates_remarks != OLD.rates_remarks THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'rates remarks',OLD.rates_remarks,NEW.rates_remarks));
			END IF;
			IF ((OLD.Shipper IS NULL AND NEW.Shipper IS NOT NULL) OR NEW.Shipper != OLD.Shipper )THEN
			  SET update_flag = TRUE;
			  IF (OLD.Shipper != NULL OR OLD.Shipper != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Shipper',OLD.Shipper,NEW.Shipper));
			  END IF;
			END IF;
			IF ((OLD.ShipNo IS NULL AND NEW.ShipNo IS NOT NULL) OR NEW.ShipNo != OLD.ShipNo) THEN
			  SET update_flag = TRUE;
			  IF (OLD.ShipNo != NULL OR OLD.ShipNo != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'ShipNo',OLD.ShipNo,NEW.ShipNo));
			  END IF;
			END IF;
			IF ((OLD.AddressforShipper IS NULL AND NEW.AddressforShipper IS NOT NULL) OR NEW.AddressforShipper != OLD.AddressforShipper) THEN
			  SET update_flag = TRUE;
			  IF (OLD.AddressforShipper != NULL OR OLD.AddressforShipper != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'AddressforShipper',OLD.AddressforShipper,NEW.AddressforShipper));
			  END IF;
			END IF;
			IF (OLD.shipper_city != NULL OR OLD.shipper_city != '') AND NEW.shipper_city != OLD.shipper_city THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'shipper city',OLD.shipper_city,NEW.shipper_city));
			END IF;
			IF (OLD.shipper_state != NULL OR OLD.shipper_state != '' ) AND NEW.shipper_state != OLD.shipper_state THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'shipper state',OLD.shipper_state,NEW.shipper_state));
			END IF;
			IF (OLD.shipper_zip != NULL OR OLD.shipper_zip != '') AND NEW.shipper_zip != OLD.shipper_zip THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'shipper zip',OLD.shipper_zip,NEW.shipper_zip));
			END IF;
			IF (OLD.shipper_country != NULL OR OLD.shipper_country != '') AND NEW.shipper_country != OLD.shipper_country THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'shipper country',OLD.shipper_country,NEW.shipper_country));
			END IF;
			IF (OLD.ShipperPhoneNumber != NULL OR OLD.ShipperPhoneNumber != '') AND NEW.ShipperPhoneNumber != OLD.ShipperPhoneNumber THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'ShipperPhoneNumber',OLD.ShipperPhoneNumber,NEW.ShipperPhoneNumber));
			END IF;
			IF (OLD.shipper_fax != NULL OR OLD.shipper_fax != '') AND NEW.shipper_fax != OLD.shipper_fax THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'shipper fax',OLD.shipper_fax,NEW.shipper_fax));
			END IF;
			IF (OLD.ShipperEmail != NULL OR OLD.ShipperEmail != '') AND NEW.ShipperEmail != OLD.ShipperEmail THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'ShipperEmail',OLD.ShipperEmail,NEW.ShipperEmail));
			END IF;
			IF (OLD.shipper_client_reference != NULL OR OLD.shipper_client_reference != '') AND NEW.shipper_client_reference != OLD.shipper_client_reference THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'shipper client reference',OLD.shipper_client_reference,NEW.shipper_client_reference));
			END IF;
			IF (OLD.shipperFlag != NULL OR OLD.shipperFlag != '') AND NEW.shipperFlag != OLD.shipperFlag THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'shipperFlag',OLD.shipperFlag,NEW.shipperFlag));
			END IF;
			IF ((OLD.Forward IS NULL AND NEW.Forward IS NOT NULL) OR NEW.Forward != OLD.Forward)THEN
			  SET update_flag = TRUE;
			  IF (OLD.Forward != NULL OR OLD.Forward != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Forward',OLD.Forward,NEW.Forward));
			  END IF;
			END IF;
			IF ((OLD.ForwardNumber IS NULL AND NEW.ForwardNumber IS NOT NULL)OR NEW.ForwardNumber != OLD.ForwardNumber) THEN
			  SET update_flag = TRUE;
			  IF (OLD.ForwardNumber != NULL OR OLD.ForwardNumber != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'ForwardNumber',OLD.ForwardNumber,NEW.ForwardNumber));
			  END IF;
			END IF;
			IF ((OLD.AddressforForwarder IS NULL AND NEW.AddressforForwarder IS NOT NULL) OR  NEW.AddressforForwarder != OLD.AddressforForwarder) THEN
			  SET update_flag = TRUE;
			  IF (OLD.AddressforForwarder != NULL OR OLD.AddressforForwarder != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'AddressforForwarder',OLD.AddressforForwarder,NEW.AddressforForwarder));
			  END IF;
			END IF;
			IF (OLD.forwarder_city != NULL OR OLD.forwarder_city != '') AND NEW.forwarder_city != OLD.forwarder_city THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'forwarder city',OLD.forwarder_city,NEW.forwarder_city));
			END IF;
			IF (OLD.forwarder_State != NULL OR OLD.forwarder_State != '') AND NEW.forwarder_State != OLD.forwarder_State THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'forwarder State',OLD.forwarder_State,NEW.forwarder_State));
			END IF;
			IF (OLD.forwarder_zip != NULL OR OLD.forwarder_zip != '') AND NEW.forwarder_zip != OLD.forwarder_zip THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'forwarder zip',OLD.forwarder_zip,NEW.forwarder_zip));
			END IF;
			IF (OLD.forwarder_country != NULL OR OLD.forwarder_country != '') AND NEW.forwarder_country != OLD.forwarder_country THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'forwarder country',OLD.forwarder_country,NEW.forwarder_country));
			END IF;
			IF (OLD.ForwarderPhoneNumber != NULL OR OLD.ForwarderPhoneNumber != '') AND NEW.ForwarderPhoneNumber != OLD.ForwarderPhoneNumber THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'ForwarderPhoneNumber',OLD.ForwarderPhoneNumber,NEW.ForwarderPhoneNumber));
			END IF;
			IF (OLD.forwarder_fax != NULL OR OLD.forwarder_fax != '') AND NEW.forwarder_fax != OLD.forwarder_fax THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'forwarder fax',OLD.forwarder_fax,NEW.forwarder_fax));
			END IF;
			IF (OLD.ForwarderEmail != NULL OR OLD.ForwarderEmail != '') AND NEW.ForwarderEmail != OLD.ForwarderEmail THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'ForwarderEmail',OLD.ForwarderEmail,NEW.ForwarderEmail));
			END IF;
			IF (OLD.forwarder_client_reference != NULL OR OLD.forwarder_client_reference != '') AND NEW.forwarder_client_reference != OLD.forwarder_client_reference THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'forwarder client reference',OLD.forwarder_client_reference,NEW.forwarder_client_reference));
			END IF;
			IF (OLD.forwarderFlag != NULL OR OLD.forwarderFlag != '') AND NEW.forwarderFlag != OLD.forwarderFlag THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'forwarderFlag',OLD.forwarderFlag,NEW.forwarderFlag));
			END IF;
			IF ((OLD.Consignee IS NULL AND NEW.Consignee IS NOT NULL) OR  NEW.Consignee != OLD.Consignee )THEN
			  SET update_flag = TRUE;
			  IF (OLD.Consignee != NULL OR OLD.Consignee != '')THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Consignee',OLD.Consignee,NEW.Consignee));
			  END IF;
			END IF;
			IF ((OLD.ConsigneeNumber IS NULL AND NEW.ConsigneeNumber IS NOT NULL) OR NEW.ConsigneeNumber != OLD.ConsigneeNumber) THEN
			  SET update_flag = TRUE;
			  IF (OLD.ConsigneeNumber != NULL OR OLD.ConsigneeNumber != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'ConsigneeNumber',OLD.ConsigneeNumber,NEW.ConsigneeNumber));
			  END IF;
			END IF;
			IF((OLD.AddressforConsingee IS NULL AND NEW.AddressforConsingee IS NOT NULL) OR NEW.AddressforConsingee != OLD.AddressforConsingee) THEN
			  SET update_flag = TRUE;
			  IF (OLD.AddressforConsingee != NULL OR OLD.AddressforConsingee != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'AddressforConsingee',OLD.AddressforConsingee,NEW.AddressforConsingee));
			  END IF;
			END IF;
			IF (OLD.consignee_city != NULL OR OLD.consignee_city != '') AND NEW.consignee_city != OLD.consignee_city THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'consignee city',OLD.consignee_city,NEW.consignee_city));
			END IF;
			IF (OLD.consignee_state != NULL OR OLD.consignee_state != '') AND NEW.consignee_state != OLD.consignee_state THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'consignee state',OLD.consignee_state,NEW.consignee_state));
			END IF;
			IF (OLD.consignee_zip != NULL OR OLD.consignee_zip != '') AND NEW.consignee_zip != OLD.consignee_zip THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'consignee zip',OLD.consignee_zip,NEW.consignee_zip));
			END IF;
			IF (OLD.consignee_country != NULL OR OLD.consignee_country != '') AND NEW.consignee_country != OLD.consignee_country THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'consignee country',OLD.consignee_country,NEW.consignee_country));
			END IF;
			IF (OLD.ConsigneePhoneNumber != NULL OR OLD.ConsigneePhoneNumber != '') AND NEW.ConsigneePhoneNumber != OLD.ConsigneePhoneNumber THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'ConsigneePhoneNumber',OLD.ConsigneePhoneNumber,NEW.ConsigneePhoneNumber));
			END IF;
			IF (OLD.consignee_fax != NULL OR OLD.consignee_fax != '') AND NEW.consignee_fax != OLD.consignee_fax THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'consignee fax',OLD.consignee_fax,NEW.consignee_fax));
			END IF;
			IF (OLD.ConsigneeEmail != NULL OR OLD.ConsigneeEmail != '') AND NEW.ConsigneeEmail != OLD.ConsigneeEmail THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'ConsigneeEmail',OLD.ConsigneeEmail,NEW.ConsigneeEmail));
			END IF;
			IF (OLD.consignee_client_reference != NULL OR OLD.consignee_client_reference != '') AND NEW.consignee_client_reference != OLD.consignee_client_reference THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'consignee client reference',OLD.consignee_client_reference,NEW.consignee_client_reference));
			END IF;
			IF (OLD.consigneeFlag != NULL OR OLD.consigneeFlag != '') AND NEW.consigneeFlag != OLD.consigneeFlag THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'consigneeFlag',OLD.consigneeFlag,NEW.consigneeFlag));
			END IF;
			IF ((OLD.Name IS NULL AND NEW.Name IS NOT NULL) OR NEW.Name != OLD.Name) THEN
			  SET update_flag = TRUE;
			  IF (OLD.Name != NULL OR OLD.Name != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Name',OLD.Name,NEW.Name));
			  END IF;
			END IF;
			IF ((OLD.TruckerCode IS NULL AND NEW.TruckerCode IS NOT NULL) OR NEW.TruckerCode != OLD.TruckerCode) THEN
			  SET update_flag = TRUE;
			  IF (OLD.TruckerCode != NULL OR OLD.TruckerCode != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'TruckerCode',OLD.TruckerCode,NEW.TruckerCode));
			  END IF;
			END IF;
			IF ((OLD.Address IS NULL AND NEW.Address IS NOT NULL) OR NEW.Address != OLD.Address) THEN
			  SET update_flag = TRUE;
			  IF (OLD.Address != NULL OR OLD.Address != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Address',OLD.Address,NEW.Address));
			  END IF;
			END IF;
			IF ((OLD.TruckerPhone IS NULL AND NEW.TruckerPhone != '') OR NEW.TruckerPhone != OLD.TruckerPhone) THEN
			  SET update_flag = TRUE;
			  IF (OLD.TruckerPhone != NULL OR OLD.TruckerPhone != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'TruckerPhone',OLD.TruckerPhone,NEW.TruckerPhone));
			  END IF;
			END IF;
			IF ((OLD.TruckerEmail IS NULL AND NEW.TruckerEmail IS NOT NULL) OR NEW.TruckerEmail != OLD.TruckerEmail) THEN
			  SET update_flag = TRUE;
			  IF (OLD.TruckerEmail != NULL OR OLD.TruckerEmail != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'TruckerEmail',OLD.TruckerEmail,NEW.TruckerEmail));
			  END IF;
			END IF;
			IF (OLD.trucker_client_reference != NULL OR OLD.trucker_client_reference != '') AND NEW.trucker_client_reference != OLD.trucker_client_reference THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'trucker client reference',OLD.trucker_client_reference,NEW.trucker_client_reference));
			END IF;
			IF (OLD.EmptyPickUpDate != NULL OR OLD.EmptyPickUpDate != '') AND NEW.EmptyPickUpDate != OLD.EmptyPickUpDate THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'EmptyPickUpDate',OLD.EmptyPickUpDate,NEW.EmptyPickUpDate));
			END IF;
			IF (OLD.EarliestPickUpDate != NULL OR OLD.EarliestPickUpDate != '') AND NEW.EarliestPickUpDate != OLD.EarliestPickUpDate THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'EarliestPickUpDate',OLD.EarliestPickUpDate,NEW.EarliestPickUpDate));
			END IF;
			IF (OLD.ExportPositoningPickup != NULL OR OLD.ExportPositoningPickup != '') AND NEW.ExportPositoningPickup != OLD.ExportPositoningPickup THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'ExportPositoningPickup',OLD.ExportPositoningPickup,NEW.ExportPositoningPickup));
			END IF;
			IF (OLD.emptypickAddress != NULL OR OLD.emptypickAddress != '') AND NEW.emptypickAddress != OLD.emptypickAddress THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'emptypickAddress',OLD.emptypickAddress,NEW.emptypickAddress));
			END IF;
			IF (OLD.pickup_remarks != NULL OR OLD.pickup_remarks != '') AND NEW.pickup_remarks != OLD.pickup_remarks THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'pickup remarks',OLD.pickup_remarks,NEW.pickup_remarks));
			END IF;
			IF ((OLD.PositioningDate IS NULL AND NEW.PositioningDate IS NOT NULL) OR NEW.PositioningDate != OLD.PositioningDate) THEN
			  SET update_flag = TRUE;
			  IF (OLD.PositioningDate != NULL OR OLD.PositioningDate != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'PositioningDate',OLD.PositioningDate,NEW.PositioningDate));
			  END IF;
			END IF;
			IF ((OLD.loadContact IS NULL AND NEW.loadContact IS NOT NULL) OR NEW.loadContact != OLD.loadContact) THEN
			  SET update_flag = TRUE;
			  IF (OLD.loadContact != NULL OR OLD.loadContact != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'loadContact',OLD.loadContact,NEW.loadContact));
			  END IF;
			END IF;
			IF (OLD.loadPhone != NULL OR OLD.loadPhone != '') AND NEW.loadPhone != OLD.loadPhone THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'loadPhone',OLD.loadPhone,NEW.loadPhone));
			END IF;
			IF ((OLD.AddressForExpPositioning IS NULL AND  NEW.AddressForExpPositioning IS NOT NULL) OR NEW.AddressForExpPositioning != OLD.AddressForExpPositioning) THEN
			  SET update_flag = TRUE;
			  IF (OLD.AddressForExpPositioning != NULL OR OLD.AddressForExpPositioning != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'AddressForExpPositioning',OLD.AddressForExpPositioning,NEW.AddressForExpPositioning));
			  END IF;
			END IF;
			IF ((OLD.load_remarks IS NULL AND NEW.load_remarks  IS NOT NULL) OR NEW.load_remarks != OLD.load_remarks) THEN
			  SET update_flag = TRUE;
			  IF (OLD.load_remarks!= NULL OR OLD.load_remarks !='') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'load remarks',OLD.load_remarks,NEW.load_remarks));
			  END IF;
			END IF;
			IF (OLD.LoadDate != NULL OR OLD.LoadDate != '') AND NEW.LoadDate != OLD.LoadDate THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'LoadDate',OLD.LoadDate,NEW.LoadDate));
			END IF;
			IF (OLD.AddessForExpDelivery != NULL OR OLD.AddessForExpDelivery != '') AND NEW.AddessForExpDelivery != OLD.AddessForExpDelivery THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'AddessForExpDelivery',OLD.AddessForExpDelivery,NEW.AddessForExpDelivery));
			END IF;
			IF (OLD.return_remarks != NULL OR OLD.return_remarks != '') AND NEW.return_remarks != OLD.return_remarks THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'return remarks',OLD.return_remarks,NEW.return_remarks));
			END IF;
			IF((OLD.DateoutYard IS NULL AND NEW.DateoutYard IS NOT NULL) OR  NEW.DateoutYard != OLD.DateoutYard) THEN
			  SET update_flag = TRUE;
			  IF (OLD.DateoutYard != NULL OR OLD.DateoutYard != '')THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Date out of Yard',OLD.DateoutYard,NEW.DateoutYard));
			  END IF;
			END IF;
			IF((OLD.DateInYard IS NULL AND NEW.DateInYard IS NOT NULL) OR  NEW.DateInYard != OLD.DateInYard) THEN
		          SET update_flag = TRUE;
		          IF (OLD.DateInYard != NULL OR OLD.DateInYard != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'DateInYard',OLD.DateInYard,NEW.DateInYard));
			  END IF;
			END IF;
			IF (OLD.specialequipment != NULL OR OLD.specialequipment != '') AND NEW.specialequipment != OLD.specialequipment THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'specialequipment',OLD.specialequipment,NEW.specialequipment));
			END IF;
			IF (OLD.soc != NULL OR OLD.soc != '') AND NEW.soc != OLD.soc THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'soc',OLD.soc,NEW.soc));
			END IF;
			IF ((OLD.outofgage IS NULL AND NEW.outofgage IS NOT NULL) OR NEW.outofgage != OLD.outofgage) THEN
			  SET update_flag = TRUE;
			  IF (OLD.outofgage != NULL OR OLD.outofgage != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Out of Gauge',OLD.outofgage,NEW.outofgage));
			  END IF;
			END IF;
			IF (OLD.customer_to_provide_SED != NULL OR OLD.customer_to_provide_SED != '') AND NEW.customer_to_provide_SED != OLD.customer_to_provide_SED THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'customer to provide SED',OLD.customer_to_provide_SED,NEW.customer_to_provide_SED));
			END IF;
			IF((OLD.localdryage IS NULL AND NEW.localdryage IS NOT NULL) OR NEW.localdryage != OLD.localdryage) THEN
			  SET update_flag = TRUE;
			  IF (OLD.localdryage != NULL OR OLD.localdryage != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'localdryage',OLD.localdryage,NEW.localdryage));
			  END IF;
			END IF;
			IF (OLD.amount != NULL OR  OLD.amount != '') AND NEW.amount != OLD.amount THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'amount',OLD.amount,NEW.amount));
			END IF;
			IF (OLD.drayage_markup != NULL OR OLD.drayage_markup != '') AND NEW.drayage_markup != OLD.drayage_markup THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'drayage markup',OLD.drayage_markup,NEW.drayage_markup));
			END IF;
			IF ((OLD.auto_deduct_ffcomm IS NULL AND NEW.auto_deduct_ffcomm IS NOT NULL) OR NEW.auto_deduct_ffcomm != OLD.auto_deduct_ffcomm)THEN
			  SET update_flag = TRUE;
			  IF (OLD.auto_deduct_ffcomm != NULL OR OLD.auto_deduct_ffcomm != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'auto deduct ffcomm',OLD.auto_deduct_ffcomm,NEW.auto_deduct_ffcomm));
			  END IF;
			END IF;
			IF ((OLD.intermodel IS NULL AND NEW.intermodel IS NOT NULL) OR NEW.intermodel != OLD.intermodel) THEN
			  SET update_flag = TRUE;
			  IF (OLD.intermodel != NULL OR OLD.intermodel != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'intermodel',OLD.intermodel,NEW.intermodel));
			  END IF;
			END IF;
			IF (OLD.amount1 != NULL OR OLD.amount1 != '') AND NEW.amount1 != OLD.amount1 THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'amount1',OLD.amount1,NEW.amount1));
			END IF;
			IF (OLD.intermodal_markup != NULL OR OLD.intermodal_markup != '') AND NEW.intermodal_markup != OLD.intermodal_markup THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'intermodal markup',OLD.intermodal_markup,NEW.intermodal_markup));
			END IF;
			IF((OLD.insurance IS NULL AND NEW.insurance IS NOT NULL) OR NEW.insurance != OLD.insurance) THEN
			  SET update_flag = TRUE;
			  IF (OLD.insurance != NULL OR OLD.insurance != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'insurance',OLD.insurance,NEW.insurance));
			  END IF;
			END IF;
			IF((OLD.costofgoods IS NULL AND NEW.costofgoods IS NOT NULL) OR NEW.costofgoods != OLD.costofgoods) THEN
			  SET update_flag = TRUE;
			  IF (OLD.costofgoods !=NULL OR OLD.costofgoods != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'costofgoods',OLD.costofgoods,NEW.costofgoods));
			  END IF;
			END IF;
			IF (OLD.insurance_markup != NULL OR OLD.insurance_markup != '') AND NEW.insurance_markup != OLD.insurance_markup THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'insurance markup',OLD.insurance_markup,NEW.insurance_markup));
			END IF;
			IF ((OLD.insurancamt IS NULL AND NEW.insurancamt IS NOT NULL) OR NEW.insurancamt != OLD.insurancamt) THEN
			  SET update_flag = TRUE;
			  IF (OLD.insurancamt != NULL OR OLD.insurancamt != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Insurance Rate',OLD.insurancamt,NEW.insurancamt));
			  END IF;
			END IF;
			IF (OLD.GoodsDescription != NULL OR OLD.GoodsDescription != '') AND NEW.GoodsDescription != OLD.GoodsDescription THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'GoodsDescription',OLD.GoodsDescription,NEW.GoodsDescription));
			END IF;
			IF ((OLD.Remarks IS NULL AND NEW.Remarks IS NOT NULL) OR NEW.Remarks != OLD.Remarks) THEN
			  SET update_flag = TRUE;
			  IF (OLD.Remarks != NULL OR OLD.Remarks != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Remarks',OLD.Remarks,NEW.Remarks));
			  END IF;
			END IF;
			IF (OLD.print_goods_description != NULL OR OLD.print_goods_description != '') AND NEW.print_goods_description != OLD.print_goods_description THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'print goods description',OLD.print_goods_description,NEW.print_goods_description));
			END IF;
			IF ((OLD.print_remarks IS NULL AND NEW.print_remarks IS NOT NULL) OR NEW.print_remarks != OLD.print_remarks) THEN
			  SET update_flag = TRUE;
			  SET updated_values = (SELECT `concat_string`(updated_values,'Routed By Agent Check',OLD.print_remarks,NEW.print_remarks));
			END IF;
			IF (OLD.documents_received != NULL OR OLD.documents_received != '') AND NEW.documents_received != OLD.documents_received THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'docs received',OLD.documents_received,NEW.documents_received));
			END IF;
			IF ((OLD.line_move IS NULL AND NEW.line_move IS NOT NULL) OR NEW.line_move != OLD.line_move) THEN
			  SET update_flag = TRUE;
			  IF (OLD.line_move != NULL OR OLD.line_move != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'line move',OLD.line_move,NEW.line_move));
			  END IF;
			END IF;
			IF((OLD.spotting_accountName IS NULL AND NEW.spotting_accountName IS NOT NULL) OR NEW.spotting_accountName != OLD.spotting_accountName) THEN
			  SET update_flag = TRUE;
			  IF (OLD.spotting_accountName != NULL OR OLD.spotting_accountName != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'spotting accountName',OLD.spotting_accountName,NEW.spotting_accountName));
			  END IF;
			END IF;
			IF (OLD.spotting_accountNo != NULL OR OLD.spotting_accountNo != '') AND NEW.spotting_accountNo != OLD.spotting_accountNo THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'spotting accountNo',OLD.spotting_accountNo,NEW.spotting_accountNo));
			END IF;
			IF ((OLD.trucker_state IS NULL AND NEW.trucker_state IS NOT NULL) OR NEW.trucker_state != OLD.trucker_state) THEN
			  SET update_flag = TRUE;
			  IF (OLD.trucker_state != NULL OR OLD.trucker_state != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'trucker state',OLD.trucker_state,NEW.trucker_state));
			  END IF;
			END IF;
			IF ((OLD.trucker_city IS NULL AND NEW.trucker_city IS NOT NULL) OR NEW.trucker_city != OLD.trucker_city) THEN
			  SET update_flag = TRUE;
			  IF (OLD.trucker_city != NULL OR OLD.trucker_city != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'trucker city',OLD.trucker_city,NEW.trucker_city));
			  END IF;
			END IF;
			IF ((OLD.trucker_zip IS NULL AND NEW.trucker_zip IS NOT NULL) OR NEW.trucker_zip != OLD.trucker_zip) THEN
			  SET update_flag = TRUE;
			  IF (OLD.trucker_zip != NULL OR OLD.trucker_zip != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'trucker zip',OLD.trucker_zip,NEW.trucker_zip));
			  END IF;
			END IF;
			IF((OLD.door_origin IS NULL AND NEW.door_origin IS NOT NULL) OR NEW.door_origin != OLD.door_origin) THEN
			  SET update_flag = TRUE;
			  IF (OLD.door_origin != NULL OR OLD.door_origin != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'door origin',OLD.door_origin,NEW.door_origin));
			  END IF;
			END IF;
			IF ((OLD.SSLine IS NULL AND NEW.SSLine IS NOT NULL) OR NEW.SSLine != OLD.SSLine) THEN
			  SET update_flag = TRUE;
			  IF (OLD.SSLine != NULL OR OLD.SSLine != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'SSLine',OLD.SSLine,NEW.SSLine));
			  END IF;
			END IF;
			IF ((OLD.door_destination IS NULL AND NEW.door_destination IS NOT NULL) OR NEW.door_destination != OLD.door_destination) THEN
			  SET update_flag = TRUE;
			  IF (OLD.door_destination != NULL OR OLD.door_destination != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'door destination',OLD.door_destination,NEW.door_destination));
			  END IF;
			END IF;
			IF ((OLD.brand IS NULL AND NEW.brand IS NOT NULL) OR NEW.brand != OLD.brand) THEN
			  IF (OLD.brand != NULL OR OLD.brand != '') THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'brand',OLD.brand,NEW.brand));
			  END IF;
			END IF;

			IF (update_flag = TRUE) THEN
				  IF EXISTS(SELECT *  FROM fcl_bl_temp WHERE temp_file_no = NEW.file_no) THEN
					UPDATE fcl_bl_temp SET temp_update_date = NOW()
					WHERE temp_file_no = NEW.file_no;
				  ELSE
					BEGIN
					INSERT INTO fcl_bl_temp(temp_file_no,temp_update_date) VALUES(NEW.file_no,NOW());
					END;
				  END IF;
			 END IF;
			IF updated_values IS NOT NULL THEN
			  INSERT INTO notes(module_id,module_ref_id,updateDate,note_type,note_desc,updated_by) VALUES('FILE',NEW.file_no,NOW(),'auto',updated_values,NEW.update_by);
			END IF;
    END;
$$
DELIMITER ;

--Mantis item 10833 by Priyanka on 4th apr 2016--LWT ECI and LL ECI
INSERT INTO property(NAME,VALUE,TYPE,description) VALUES('ECU.tariffterms','TRANSPORTATION PURSUANT TO THIS BILL OF LADING IS SUBJECT TO CONDITIONS SET FORTH IN ECU WORLDWIDE PUBLISHED TARIFF','common','Ecu Worldwide Tariff Terms');
INSERT INTO property(NAME,VALUE,TYPE,description) VALUES('Econo.tariffterms','TRANSPORTATION PURSUANT TO THIS BILL OF LADING IS SUBJECT TO CONDITIONS SET FORTH IN THE ECONOCARIBE PUBLISHED TARIFF','common','Econo Tariff Terms');


--Mantis item 10833 by Priyanka on 4th apr 2016--LWT OTI and LL OTI
INSERT INTO property(NAME,VALUE,TYPE,description) VALUES('ECU.tariffterms','TRANSPORTATION PURSUANT TO THIS BILL OF LADING IS SUBJECT TO CONDITIONS SET FORTH IN ECU WORLDWIDE PUBLISHED TARIFF','common','Ecu Worldwide Tariff Terms');
INSERT INTO property(NAME,VALUE,TYPE,description) VALUES('OTI.tariffterms','TRANSPORTATION PURSUANT TO THIS BILL OF LADING IS SUBJECT TO CONDITIONS SET FORTH IN OTI PUBLISHED TARIFF','common','OTI Tariff Terms');


---- LCL Exports Mantis #  by Sathiyapriya on April 4
ALTER TABLE `role_duties` ADD COLUMN `lcl_wghtchange_release` TINYINT(1) DEFAULT 0 NULL AFTER `lcl_manifest_postedbl`; 

--Mantis item 11437 by Vellaisamy on 7th April 2016 -- This query only for Local and Econo
ALTER TABLE `lcl_booking_import_ams` CHANGE `seg_file_number_id` `seg_file_number_id` BIGINT(20) UNSIGNED NULL; 
ALTER TABLE `lcl_booking_import_ams` ADD INDEX `lcl_booking_import_ams_idx02` (`seg_file_number_id`),ADD CONSTRAINT `lcl_booking_import_ams_fk4`FOREIGN KEY (`seg_file_number_id`) REFERENCES `lcl_file_number` (`id`) ON UPDATE CASCADE;
/**End**/
--Mantis item 9502 by Mei on 12April 2016 -- This query only for Local and Econo
ALTER TABLE `lcl_ss_detail`
  ADD COLUMN `general_loading_deadline` DATETIME NULL AFTER `arrival_location`,
  ADD COLUMN `hazmat_loading_deadline` DATETIME NULL AFTER `general_loading_deadline`;
--Mantis item 11473 by Mei on 13April 2016
DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBookingPiece_update`$$

CREATE TRIGGER `after_LclBookingPiece_update` AFTER UPDATE ON `lcl_booking_piece`
    FOR EACH ROW BEGIN
DECLARE updated_values TEXT ;
DECLARE change_value BOOLEAN ;
IF new.commodity_type_id!=old.commodity_type_id THEN
SET updated_values=(SELECT concat_string(updated_values,' Commodity',(SELECT CONCAT(desc_en,' (',CODE,')') FROM commodity_type WHERE id=old.commodity_type_id),(SELECT CONCAT(desc_en,' (',CODE,')') FROM commodity_type WHERE id=new.commodity_type_id)));
END IF;
IF new.booked_package_type_id!=old.booked_package_type_id THEN
SET updated_values=(SELECT concat_string(updated_values,' Package',(SELECT description FROM package_type WHERE id=old.booked_package_type_id),(SELECT description FROM package_type WHERE id=new.booked_package_type_id)));
END IF;
IF new.booked_piece_count!=old.booked_piece_count THEN
SET updated_values=(SELECT concat_string(updated_values,' Piece Count',old.booked_piece_count,new.booked_piece_count));
SET change_value=TRUE;
END IF;
IF new.booked_weight_imperial!=old.booked_weight_imperial THEN
SET updated_values=(SELECT concat_string(updated_values,' Weight  LBS',old.booked_weight_imperial,new.booked_weight_imperial));
SET change_value=TRUE;
END IF;
IF new.booked_weight_metric!=old.booked_weight_metric THEN
SET updated_values=(SELECT concat_string(updated_values,' Weight  KGS',old.booked_weight_metric,new.booked_weight_metric));
SET change_value=TRUE;
END IF;
IF new.booked_volume_imperial!=old.booked_volume_imperial THEN
SET updated_values=(SELECT concat_string(updated_values,' Volume  CFT',old.booked_volume_imperial,new.booked_volume_imperial));
SET change_value=TRUE;
END IF;
IF new.booked_volume_metric!=old.booked_volume_metric THEN
SET updated_values=(SELECT concat_string(updated_values,' Volume  CBM',old.booked_volume_metric,new.booked_volume_metric));
SET change_value=TRUE;
END IF;
IF ((OLD.actual_piece_count IS NULL AND NEW.actual_piece_count IS NOT NULL) OR NEW.actual_piece_count != OLD.actual_piece_count OR
(OLD.actual_piece_count IS NOT NULL AND NEW.actual_piece_count IS NULL)) THEN
SET updated_values=(SELECT concat_string(updated_values,' Actual Piece Count',IFNULL(old.actual_piece_count,''),IFNULL(new.actual_piece_count,'')));
END IF;
IF ((OLD.actual_package_type_id IS NULL AND NEW.actual_package_type_id IS NOT NULL) OR NEW.actual_package_type_id != OLD.actual_package_type_id
OR (OLD.actual_package_type_id IS NOT NULL AND NEW.actual_package_type_id IS NULL)) THEN
 SET @oldPackage=(SELECT description FROM package_type WHERE id=old.actual_package_type_id);
 SET @newPackage=(SELECT description FROM package_type WHERE id=new.actual_package_type_id);
 SET updated_values=(SELECT concat_string(updated_values,' Actual Package',IFNULL(@oldPackage,''),IFNULL(@newPackage,'')));
END IF;
IF ((OLD.actual_weight_imperial IS NULL AND NEW.actual_weight_imperial IS NOT NULL) OR NEW.actual_weight_imperial != OLD.actual_weight_imperial OR
(OLD.actual_weight_imperial IS NOT NULL AND NEW.actual_weight_imperial IS NULL)) THEN
SET updated_values=(SELECT concat_string(updated_values,' Actual Weight LBS',IFNULL(old.actual_weight_imperial,''),IFNULL(new.actual_weight_imperial,'')));
END IF;
IF ((OLD.actual_weight_metric IS NULL AND NEW.actual_weight_metric IS NOT NULL) OR NEW.actual_weight_metric != OLD.actual_weight_metric OR
(OLD.actual_weight_metric IS NOT NULL AND NEW.actual_weight_metric IS NULL)) THEN
SET updated_values=(SELECT concat_string(updated_values,' Actual Weight KGS',IFNULL(old.actual_weight_metric,''),IFNULL(new.actual_weight_metric,'')));
END IF;
IF ((OLD.actual_volume_imperial IS NULL AND NEW.actual_volume_imperial IS NOT NULL) OR NEW.actual_volume_imperial != OLD.actual_volume_imperial OR
(OLD.actual_volume_imperial IS NOT NULL AND NEW.actual_volume_imperial IS NULL)) THEN
SET updated_values=(SELECT concat_string(updated_values,' Actual Volume CFT',IFNULL(old.actual_volume_imperial,''),IFNULL(new.actual_volume_imperial,'')));
END IF;
IF ((OLD.actual_volume_metric IS NULL AND NEW.actual_volume_metric IS NOT NULL) OR NEW.actual_volume_metric != OLD.actual_volume_metric OR
(OLD.actual_volume_metric IS NOT NULL AND NEW.actual_volume_metric IS NULL)) THEN
SET updated_values=(SELECT concat_string(updated_values,' Actual Volume CBM',IFNULL(old.actual_volume_metric,''),IFNULL(new.actual_volume_metric,'')));
END IF;
IF new.hazmat <> old.hazmat THEN
  SET updated_values=(SELECT concat_string(updated_values,'HaZmat',IF(old.hazmat=0,'No','Yes'),IF(new.hazmat=0,'No','Yes')));
END IF;
IF ((OLD.piece_desc IS NULL AND NEW.piece_desc IS NOT NULL) OR NEW.piece_desc != OLD.piece_desc OR (OLD.piece_desc IS NOT NULL AND NEW.piece_desc IS NULL)) THEN
  SET updated_values=(SELECT CONCAT(IFNULL(updated_values,''),' Commodity Desc-->','Descriptions have been changed'));
        END IF;
       IF ((OLD.mark_no_desc IS NULL AND NEW.mark_no_desc IS NOT NULL) OR NEW.mark_no_desc != OLD.mark_no_desc OR (OLD.mark_no_desc IS NOT NULL AND NEW.mark_no_desc IS NULL)) THEN
         SET updated_values=CONCAT(IFNULL(updated_values,''),' Marks and Numbers-->','Marks have been changed');
       END IF;
IF  change_value=TRUE THEN
        SET @unit_ss_id=(SELECT lcl_unit_ss_id FROM lcl_booking_piece_unit WHERE booking_piece_id=old.id);
        CALL UpdateUnitWMValue(@unit_ss_id);
        END IF;
IF ((OLD.weight_verfied_user_id IS NULL AND NEW.weight_verfied_user_id IS NOT NULL) OR NEW.weight_verfied_user_id != OLD.weight_verfied_user_id OR (OLD.weight_verfied_user_id IS NOT NULL AND NEW.weight_verfied_user_id IS NULL)) THEN
     SET @oldUserName=(SELECT CONCAT(first_name,' ',last_name) FROM user_details WHERE user_id=old.weight_verfied_user_id);
     SET @newUserName=(SELECT CONCAT(first_name,' ',last_name) FROM user_details WHERE user_id=new.weight_verfied_user_id);
     SET updated_values=(SELECT concat_string(updated_values,'Weighed By',IFNULL(@oldUserName,''),IFNULL(@newUserName,'')));
END IF;
        IF updated_values IS NOT NULL THEN
        SET updated_values=CONCAT('UPDATED ->',updated_values);
INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
VALUES(OLD.file_number_id,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
END IF;
        END;
$$

DELIMITER ;


---- LCL Exports Mantis #9614  by Sathiyapriya on April 14

INSERT INTO item_master(item_desc,item_created_on,unique_code)
 VALUES('Reference Data',NOW(),'LCLRDMS');

INSERT INTO item_treestructure(item_id,parent_id) VALUES((SELECT item_id FROM item_master WHERE item_desc='Reference Data' AND unique_code = 'LCLRDMS' LIMIT 1),
(SELECT item_id FROM item_master WHERE item_desc='Data Management' LIMIT 1));

INSERT INTO `role_item_assoc` (`role_id`,`item_id`,`modify`,`deleted`) VALUES
(1,(SELECT item_id FROM item_master WHERE item_desc='Reference Data' AND unique_code = 'LCLRDMS' LIMIT 1),'modify','delete');

UPDATE item_treestructure SET parent_id=(SELECT item_id FROM item_master WHERE item_desc='Reference Data' AND unique_code = 'LCLRDMS' LIMIT 1)
WHERE item_id =(SELECT item_id FROM item_master WHERE item_desc='LCL Template' AND unique_code = 'LCLT' LIMIT 1);

UPDATE item_treestructure SET parent_id=(SELECT item_id FROM item_master WHERE item_desc='Reference Data' AND unique_code = 'LCLRDMS' LIMIT 1)
WHERE item_id =(SELECT item_id FROM item_master WHERE item_desc='UnitSs Auto Costing' AND unique_code = 'UNITSSAUCO' LIMIT 1);

UPDATE item_treestructure SET parent_id=(SELECT item_id FROM item_master WHERE item_desc='Reference Data' AND unique_code = 'LCLRDMS' LIMIT 1)
WHERE item_id =(SELECT item_id FROM item_master WHERE item_desc='Unit Size' AND unique_code = 'LCLUNIT' LIMIT 1);

UPDATE item_treestructure SET parent_id=(SELECT item_id FROM item_master WHERE item_desc='Reference Data' AND unique_code = 'LCLRDMS' LIMIT 1)
WHERE item_id =(SELECT item_id FROM item_master WHERE item_desc='Warehouse' LIMIT 1);

UPDATE item_treestructure SET parent_id=(SELECT item_id FROM item_master WHERE item_desc='Reference Data' AND unique_code = 'LCLRDMS' LIMIT 1)
WHERE item_id =(SELECT item_id FROM item_master WHERE item_desc='Relay Inquiry' LIMIT 1);

UPDATE item_treestructure SET parent_id=(SELECT item_id FROM item_master WHERE item_desc='Reference Data' AND unique_code = 'LCLRDMS' LIMIT 1)
WHERE item_id =(SELECT item_id FROM item_master WHERE item_desc='Commodity' AND unique_code = 'LCLCCODE' LIMIT 1);

UPDATE item_treestructure SET parent_id=(SELECT item_id FROM item_master WHERE item_desc='Reference Data' AND unique_code = 'LCLRDMS' LIMIT 1)
WHERE item_id =(SELECT item_id FROM item_master WHERE item_desc='Corporate Account Type' AND unique_code = 'CORACCTTYP' LIMIT 1);

UPDATE item_treestructure SET parent_id=(SELECT item_id FROM item_master WHERE item_desc='Reference Data' AND unique_code = 'LCLRDMS' LIMIT 1)
WHERE item_id =(SELECT item_id FROM item_master WHERE item_desc='Corporate Account' AND unique_code = 'CORACCT' LIMIT 1);

-- > Mantis Item-11561(Logiware Accounting) by Kuppusamy on 19 April 2016
ALTER TABLE `terminal_gl_mapping`
  ADD COLUMN `air_import_billing` INT(10) NULL AFTER `fcl_import_loading`,
  ADD COLUMN `air_import_loading` INT(10) NULL AFTER `air_import_billing`;
-- 19April16 Mantis 11485 Mei
INSERT INTO property(NAME,VALUE,TYPE,description)
VALUES('report.preview.location','','Common','ReportPreviewLocation');

-- Mantis Item-6245(MultiQuote) by Nambu on 19 April 2016
CREATE TABLE `multi_quote` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `quote_id` int(11) DEFAULT NULL,
  `origin` varchar(200) DEFAULT NULL,
  `destination` varchar(200) DEFAULT NULL,
  `origin_code` varchar(10) DEFAULT NULL,
  `desti_code` varchar(10) DEFAULT NULL,
  `commodity` varchar(10) DEFAULT NULL,
  `carrier_no` varchar(20) DEFAULT NULL,
  `carrier` varchar(80) DEFAULT NULL,
  `selected_units` varchar(100) DEFAULT NULL,
  `hazmat` varchar(1) DEFAULT NULL,
  `bullet_rates` varchar(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `quote_id_fk01` (`quote_id`),
  CONSTRAINT `quote_id_fk01` FOREIGN KEY (`quote_id`) REFERENCES `quotation` (`Quote_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `multi_quote_charges` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `multi_quote_id` bigint(20) unsigned NOT NULL,
  `unitType` varchar(20) DEFAULT NULL,
  `unit_no` varchar(10) DEFAULT NULL,
  `charge_code` varchar(20) DEFAULT NULL,
  `charge_code_desc` varchar(100) DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `number` varchar(3) DEFAULT NULL,
  `cost_type` varchar(40) DEFAULT NULL,
  `markup` decimal(10,2) DEFAULT NULL,
  `minimum` decimal(10,2) DEFAULT NULL,
  `retail` decimal(10,2) DEFAULT NULL,
  `currency` varchar(50) DEFAULT NULL,
  `effective_date` date DEFAULT NULL,
  `account_no` varchar(20) DEFAULT NULL,
  `account_name` varchar(80) DEFAULT NULL,
  `charge_flag` varchar(1) DEFAULT NULL,
  `new_flag` varchar(3) DEFAULT NULL,
  `comment` varchar(500) DEFAULT NULL,
  `adjustment` decimal(10,2) DEFAULT NULL,
  `update_by` varchar(50) DEFAULT NULL,
  `update_on` datetime DEFAULT NULL,
  `default_carrier` varchar(1) DEFAULT NULL,
  `out_of_gauge` varchar(1) DEFAULT NULL,
  `out_of_gauge_comment` varchar(200) DEFAULT NULL,
  `adjustment_charge_comments` varchar(500) DEFAULT NULL,
  `include` varchar(5) DEFAULT NULL,
  `print` varchar(5) DEFAULT NULL,
  `standard_charge` varchar(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `multi_quote_id_fk01` (`multi_quote_id`),
  CONSTRAINT `multi_quote_id_fk01` FOREIGN KEY (`multi_quote_id`) REFERENCES `multi_quote` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


INSERT INTO print_config(`screen_name`,`document_type`,`document_name`,`file_location`,`allow_print`)
VALUES('MultiQuote','IN','MultiQuote','c:/FCL_MultiQuote/1.pdf','No');

ALTER TABLE `quotation` ADD COLUMN `multi_quote_flag` VARCHAR(1) NULL AFTER `brand`;
  

-- Except Econo
INSERT INTO item_treestructure(item_id,parent_id,id) VALUES (60,55,57);
INSERT INTO item_master(item_id,item_desc,program,item_created_on,unique_code)
VALUES(60,'MultiQuote','/jsps/fclQuotes/SearchQuotation.jsp',NOW(),'MULTI');
INSERT INTO `role_item_assoc`(role_id,`item_id`,`modify`) VALUES(1,60,'modify');

--  Mantis Item-7710(LCL-Imports) by Stefy on 22 April 2016

DROP TRIGGER `after_lclBookingAc_trans_update`;
ALTER TABLE `lcl_booking_ac_trans` CHANGE `payment_type` `payment_type` ENUM('Cash','Credit Card','Credit Account','ACH','Check Copy','Check/Cash');

UPDATE lcl_booking_ac_trans SET payment_type = 'Check/Cash' WHERE payment_type = 'Cash';

ALTER TABLE `lcl_booking_ac_trans` CHANGE `payment_type` `payment_type` ENUM('Check/Cash','Credit Card','Credit Account','ACH','Check Copy');

INSERT INTO scan_config (screen_name,document_type,document_name,file_location) 
VALUES ('LCL IMPORTS DR',NULL,'CHECK COPY',NULL);

DELIMITER $$
DROP TRIGGER /*!50032 IF EXISTS */ `after_lclBookingAc_trans_update`$$
CREATE
    TRIGGER `after_lclBookingAc_trans_update` AFTER UPDATE ON `lcl_booking_ac_trans` 
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT ;
    IF (old.payment_type <> new.payment_type)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Payment Type Changed',old.payment_type,new.payment_type));
     END IF;
    IF updated_values IS NOT NULL THEN
    SET updated_values=CONCAT('UPDATED ->',updated_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(OLD.file_number_id,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END;
$$
DELIMITER ;

----Mantis # 11661 Exports by Sathiyapriya

ALTER TABLE `transaction_ledger` CHANGE `booking_no` `booking_no` VARCHAR(50) CHARSET latin1 COLLATE latin1_swedish_ci NULL; 
ALTER TABLE `transaction` CHANGE `booking_no` `booking_no` VARCHAR(50) CHARSET latin1 COLLATE latin1_swedish_ci NULL; 

--  Mantis Item-0010045(FCL-Exports) by palraj on 26 April 2016

ALTER TABLE `booking_inbond_details` ADD COLUMN `cfcl_flag` TINYINT(1) DEFAULT 0; 

ALTER TABLE `bookingfcl_units` ADD COLUMN `cfcl_flag` TINYINT(1) DEFAULT 0;

--  Mantis Item-0011193(Accounting) by Nambu on 27 April 2016

ALTER TABLE `ap_invoice`  ADD COLUMN `user_id` INT(11) NULL AFTER `resolved_date`;
ALTER TABLE `user_details` ADD COLUMN `billing_Terminal` VARCHAR(50) NULL AFTER `signature_image`;
INSERT INTO job (NAME,`frequency`,`day1`,`day2`,`hour`,`minute`,`enabled`,`updated_by`,`updated_date`,`class_name`) VALUES
('Dispute Report','WEEKLY',2,0,0,30,0,'NAMBU',NOW(),'com.logiware.common.job.DisputeJob');

--  Mantis Item-11681(Logiware FCL-Exports) by Stefy on 27 April 2016

INSERT INTO property(NAME,VALUE,TYPE,description) VALUES('application.image.dual.logo','/img/companyLogo/DualLogo.png','ACCOUNTING','Application Image Dual Logo');

-- Mantis #11665 Exports by sathiyapriya on Apr 27 2016

ALTER TABLE `lcl_unit_ss` CHANGE `weight_imperial` `weight_imperial` DECIMAL(13,3) UNSIGNED NULL, 
CHANGE `weight_metric` `weight_metric` DECIMAL(13,3) UNSIGNED NULL, CHANGE `volume_metric` `volume_metric` DECIMAL(13,3) 
UNSIGNED NULL, CHANGE `volume_imperial` `volume_imperial` DECIMAL(13,3) UNSIGNED NULL; 

UPDATE `genericcodelabels` SET `javascript` = '$(\"#codedesc\").limit(500);' WHERE 
codetypeid=(SELECT codetypeid FROM codetype WHERE description='Print Comments') AND label='Descritpion';

-- 27/04/16 Mantis#10309 @Mei
DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclQuote_insert`$$

CREATE TRIGGER `after_LclQuote_insert` AFTER INSERT ON `lcl_quote`
    FOR EACH ROW BEGIN
    DECLARE insert_values TEXT ;
   IF NEW.rtd_transaction <> '' THEN
   SET insert_values = concat_insert_values(insert_values,'ERT Y/N',IF(NEW.rtd_transaction=0,'No','Yes'));
   END IF;
   IF NEW.bill_to_party <> '' THEN
   SET insert_values = concat_insert_values(insert_values,'Bill To Party',NEW.bill_to_party);
   END IF;
   IF NEW.billing_type <> '' THEN
   SET insert_values = concat_insert_values(insert_values,'Billing Type',NEW.billing_type);
   END IF;
    IF insert_values IS NOT NULL THEN
    SET insert_values=CONCAT('INSERTED ->',insert_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(NEW.file_number_id,'QT-AutoNotes',insert_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;


DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclQuote_update`$$

CREATE TRIGGER `after_LclQuote_update` AFTER UPDATE ON `lcl_quote`
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT ;
    DECLARE updated_value TEXT ;
    IF new.delivery_metro!=old.delivery_metro THEN
       SET updated_values=(SELECT concat_string(updated_values,'Delivery Metro',old.delivery_metro,new.delivery_metro));
    END IF;
    IF new.insurance!=old.insurance THEN
       SET updated_values=(SELECT concat_string(updated_values,'Insurance',IF(old.insurance=0,'No','Yes'),IF(new.insurance=0,'No','Yes')));
    END IF;
    IF new.documentation!=old.documentation THEN
       SET updated_values=(SELECT concat_string(updated_values,'Documentation',IF(old.documentation=0,'No','Yes'),IF(new.documentation=0,'No','Yes')));
    END IF;
    IF new.spot_rate!=old.spot_rate THEN
      SET updated_values=(SELECT concat_string(updated_values,'SpotRate',IF(old.spot_rate=0,'No','Yes'),IF(new.spot_rate=0,'No','Yes')));
    END IF;
    IF new.spot_comment!=old.spot_comment THEN
      SET updated_values=(SELECT concat_string(updated_values,'SpotRate Comment',old.spot_comment,new.spot_comment));
    END IF;
    IF new.spot_rate_uom!=old.spot_rate_uom THEN
      SET updated_values=(SELECT concat_string(updated_values,'SpotRate W/M',old.spot_rate_uom,new.spot_rate_uom));
    END IF;
    IF new.spot_wm_rate!=old.spot_wm_rate THEN
      SET updated_values=(SELECT concat_string(updated_values,'SpotRate',CONCAT('$',old.spot_wm_rate),CONCAT('$',new.spot_wm_rate)));
    END IF;
    IF new.poo_door!=old.poo_door THEN
      SET updated_values=(SELECT concat_string(updated_values,'Pickup',IF(old.poo_door=0,'No','Yes'),IF(new.poo_door=0,'No','Yes')));
    END IF;
    IF new.rtd_transaction!=old.rtd_transaction THEN
      SET updated_values=(SELECT concat_string(updated_values,'ERT Y/N',IF(old.rtd_transaction=0,'No','Yes'),IF(new.rtd_transaction=0,'No','Yes')));
    END IF;
    IF new.quote_complete!=old.quote_complete THEN
      SET updated_values=(SELECT concat_string(updated_values,'Quote Complete',IF(old.quote_complete=0,'No','Yes'),IF(new.quote_complete=0,'No','Yes')));
      SET updated_value='Quote is Completed';
    END IF;
     IF new.client_pwk_recvd!=old.client_pwk_recvd THEN
      SET updated_values=(SELECT concat_string(updated_values,'PWK/Docs Received',IF(old.client_pwk_recvd=0,'No','Yes'),IF(new.client_pwk_recvd=0,'No','Yes')));
    END IF;
    IF isNotEqual(OLD.bill_to_party,NEW.bill_to_party) THEN
	SET updated_values = (SELECT concat_string(updated_values,'Bill To Party',OLD.bill_to_party,NEW.bill_to_party));
    END IF;
    IF isNotEqual(OLD.billing_type,NEW.billing_type) THEN
	SET updated_values = (SELECT concat_string(updated_values,'Billing Type',OLD.billing_type,NEW.billing_type));
    END IF;
     IF updated_values IS NOT NULL THEN
    SET updated_values=CONCAT(updated_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(OLD.file_number_id,'QT-AutoNotes',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
      END;
$$

DELIMITER ;



DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `lcl_quote_ac_insert_trigger`$$

CREATE TRIGGER `lcl_quote_ac_insert_trigger` AFTER INSERT ON `lcl_quote_ac`
    FOR EACH ROW BEGIN
    DECLARE insert_values TEXT;
     DECLARE changesFlag BOOLEAN;
    IF (NEW.ar_gl_mapping_id <>'' AND NEW.ar_gl_mapping_id IS NOT NULL)THEN
    SET insert_values = concat_insert_values(insert_values,'Code',(SELECT charge_code FROM gl_mapping WHERE id=NEW.ar_gl_mapping_id));
    SET changesFlag=TRUE;
    END IF;
    IF (NEW.ar_amount <>'' AND NEW.ar_amount IS NOT NULL)THEN
    SET insert_values = concat_insert_values(insert_values,' Charge Amount',NEW.ar_amount);
    SET changesFlag=TRUE;
    END IF;
    IF (NEW.ap_amount<>'' AND NEW.ap_amount IS NOT NULL)THEN
    SET insert_values = concat_insert_values(insert_values,' Cost Amount',NEW.ap_amount);
    SET changesFlag=TRUE;
    END IF;
    IF (NEW.sp_acct_no<>'' AND NEW.sp_acct_no IS NOT NULL)THEN
    SET insert_values = concat_insert_values(insert_values,' Vendor Name',(SELECT acct_name FROM trading_partner WHERE acct_no=new.sp_acct_no));
    SET changesFlag=TRUE;
    END IF;
    IF (NEW.sp_acct_no <>'' AND NEW.sp_acct_no IS NOT NULL)THEN
    SET insert_values = concat_insert_values(insert_values,' Vendor Number',NEW.sp_acct_no);
    SET changesFlag=TRUE;
    END IF;
    IF (NEW.rate_per_weight_unit <>'' AND NEW.rate_per_weight_unit IS NOT NULL) THEN
    SET insert_values = concat_insert_values(insert_values,' Rate Weight',NEW.rate_per_weight_unit);
    SET changesFlag=TRUE;
    END IF;
    IF (NEW.rate_per_volume_unit <>'' AND NEW.rate_per_volume_unit IS NOT NULL)THEN
    SET insert_values = concat_insert_values(insert_values,' Rate Measure',NEW.rate_per_volume_unit);
    SET changesFlag=TRUE;
    END IF;
    IF (NEW.rate_flat_minimum <>'' AND NEW.rate_flat_minimum IS NOT NULL)THEN
    SET insert_values = concat_insert_values(insert_values,' Rate Minimum',NEW.rate_flat_minimum);
    SET changesFlag=TRUE;
    END IF;
    IF (NEW.cost_weight <>'' AND NEW.cost_weight IS NOT NULL)THEN
    SET insert_values = concat_insert_values(insert_values,' Cost Weight',NEW.cost_weight);
    SET changesFlag=TRUE;
    END IF;
    IF (NEW.cost_measure<>'' AND  NEW.cost_measure IS NOT NULL)THEN
    SET insert_values = concat_insert_values(insert_values,' Cost Measure',NEW.cost_measure);
    SET changesFlag=TRUE;
    END IF;
    IF (NEW.cost_minimum <>'' AND NEW.cost_minimum IS NOT NULL)THEN
    SET insert_values = concat_insert_values(insert_values,' Cost Minimum',NEW.cost_minimum);
    SET changesFlag=TRUE;
    END IF;
    IF (NEW.invoice_number <>'' AND NEW.invoice_number IS NOT NULL)THEN
    SET insert_values = concat_insert_values(insert_values,' Invoice Number',NEW.invoice_number);
    SET changesFlag=TRUE;
    END IF;
        IF (NEW.ar_amount!=0) THEN
        IF (insert_values <>'' OR insert_values IS NOT NULL) THEN
        SET insert_values=CONCAT('INSERTED ->',insert_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(new.file_number_id,'QT-AutoNotes',insert_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END IF;
    END;
$$

DELIMITER ;



DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `lcl_quote_ac_update_trigger`$$

CREATE TRIGGER `lcl_quote_ac_update_trigger` AFTER UPDATE ON `lcl_quote_ac`
    FOR EACH ROW BEGIN
    DECLARE updatedValues TEXT;
    DECLARE mQuoteType VARCHAR(2);
    DECLARE chargeValue BOOLEAN DEFAULT FALSE;
    IF (NEW.ar_gl_mapping_id <> old.ar_gl_mapping_id)THEN
    SET updatedValues = concat_string(updatedValues, 'Charge Code', (SELECT charge_code FROM gl_mapping WHERE id = old.ar_gl_mapping_id),
    (SELECT charge_code FROM gl_mapping WHERE id = new.ar_gl_mapping_id)) ;
     SET chargeValue = TRUE ;
    END IF;
    IF (NEW.ar_amount <> old.ar_amount)THEN
    SET updatedValues = concat_string(updatedValues,' Charge Amount',old.ar_amount,NEW.ar_amount);
    END IF;
    IF (NEW.ap_amount <>  old.ap_amount)THEN
    SET updatedValues = concat_string(updatedValues,' Cost Amount',old.ap_amount,NEW.ap_amount);
    END IF;
   /* ************************Only For Lcl Exports when change the FLAT RATE****************************** */
  SET mQuoteType = (SELECT quote_type FROM lcl_quote WHERE file_number_id = new.file_number_id);
  IF (mQuoteType NOT IN('I','T')) THEN
     IF (new.rate_per_unit  <> old.rate_per_unit) THEN
         SET updatedValues = concat_string(updatedValues, 'Charge Flat Amount', old.rate_per_unit, new.rate_per_unit) ;
     END IF;
     IF (new.cost_flatrate_amount <> old.cost_flatrate_amount) THEN
         SET updatedValues = concat_string(updatedValues, 'Cost Flat Amount', old.cost_flatrate_amount, new.cost_flatrate_amount) ;
     END IF;
  END IF ;
   /* ************************************************************************************ */
    IF (NEW.sp_acct_no <>  old.sp_acct_no)THEN
    SET updatedValues = concat_string(updatedValues,' Vendor Name',
    (SELECT acct_name FROM trading_partner WHERE acct_no=old.sp_acct_no),
    (SELECT acct_name FROM trading_partner WHERE acct_no=new.sp_acct_no));
    END IF;
    IF (NEW.sp_acct_no <>  old.sp_acct_no)THEN
    SET updatedValues = concat_string(updatedValues,' Vendor Number',old.sp_acct_no,NEW.sp_acct_no);
    END IF;
    IF (NEW.rate_per_weight_unit <>  old.rate_per_weight_unit) THEN
    SET updatedValues = concat_string(updatedValues,' Rate Weight',old.rate_per_weight_unit,NEW.rate_per_weight_unit);
    END IF;
    IF (NEW.rate_per_volume_unit <>  old.rate_per_volume_unit)THEN
    SET updatedValues = concat_string(updatedValues,' Rate Measure',old.rate_per_volume_unit,NEW.rate_per_volume_unit);
    END IF;
    IF (NEW.rate_flat_minimum <>  old.rate_flat_minimum)THEN
    SET updatedValues = concat_string(updatedValues,' Rate Minimum',old.rate_flat_minimum,NEW.rate_flat_minimum);
    END IF;
    IF (NEW.cost_weight <>  old.cost_weight)THEN
    SET updatedValues = concat_string(updatedValues,' Cost Weight',old.cost_weight,NEW.cost_weight);
    END IF;
    IF (NEW.cost_measure<>   old.cost_measure)THEN
    SET updatedValues = concat_string(updatedValues,' Cost Measure',old.cost_measure,NEW.cost_measure);
    END IF;
    IF (NEW.cost_minimum <>  old.cost_minimum)THEN
    SET updatedValues = concat_string(updatedValues,' Cost Minimum',old.cost_minimum,NEW.cost_minimum);
    END IF;
    IF (NEW.invoice_number <>  old.invoice_number)THEN
    SET updatedValues = concat_string(updatedValues,' Invoice Number',old.invoice_number,NEW.invoice_number);
    END IF;
    IF (new.adjustment_amount <> old.adjustment_amount) THEN
    SET updatedValues = concat_string(updatedValues, 'Adjustment Amount', old.adjustment_amount, new.adjustment_amount);
    END IF ;
    IF (new.adjustment_comments <> old.adjustment_comments) THEN
    SET updatedValues = concat_string(updatedValues, 'Adjustment Comments', old.adjustment_comments, new.adjustment_comments);
   END IF ;
    IF (updatedValues<>'') THEN
        IF (chargeValue = TRUE) THEN
        SET updatedValues=CONCAT('UPDATED ->',updatedValues);
        ELSE
        SET updatedValues = CONCAT('UPDATED -> (Code -> ', (SELECT charge_code FROM gl_mapping WHERE id = old.ar_gl_mapping_id), ')', updatedValues) ;
        END IF ;
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(new.file_number_id,'QT-AutoNotes',updatedValues,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;



DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclQuoteHazmat_insert`$$

CREATE TRIGGER `after_LclQuoteHazmat_insert` AFTER INSERT ON `lcl_quote_hazmat`
    FOR EACH ROW BEGIN
     DECLARE inserted_values TEXT;
     SET @contact_name =(SELECT contact_name FROM lcl_contact WHERE id=new.emergency_contact_id);
     SET @phone1 =(SELECT phone1 FROM lcl_contact WHERE id=new.emergency_contact_id);
      IF NEW.un_hazmat_no  IS NOT NULL THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'UN#',NEW.un_hazmat_no));
     END IF;
     IF NEW.proper_shipping_name IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Proper Shipping Name',NEW.proper_shipping_name));
     END IF;
      IF NEW.imo_pri_class_code IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Class',NEW.imo_pri_class_code));
     END IF;
      IF NEW.packing_group_code IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Packaging Group Code',NEW.packing_group_code));
     END IF;
      IF NEW.flash_point IS NOT NULL THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Flash point',NEW.flash_point));
     END IF;
       IF NEW.outer_pkg_no_pieces IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Outer Packaging Pieces',NEW.outer_pkg_no_pieces));
     END IF;
       IF NEW.outer_pkg_type IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Outer Packaging Type',NEW.outer_pkg_type));
     END IF;
       IF NEW.outer_pkg_composition IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Outer Pkg Composition',NEW.outer_pkg_composition));
     END IF;
      IF NEW.total_net_weight IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Total Net Weight/Kgs',NEW.total_net_weight));
     END IF;
      IF NEW.liquid_volume IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Total Volume',NEW.liquid_volume));
     END IF;
       IF NEW.total_gross_weight IS NOT NULL THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Total Gross Weight/Kgs',NEW.total_gross_weight));
     END IF;
     IF @contact_name IS NOT NULL THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Emergency Contact',@contact_name));
     END IF;
     IF @phone1 IS NOT NULL THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Emergency Phone No',@phone1));
     END IF;
     IF NEW.ems_code IS NOT NULL THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'EMS code',NEW.ems_code));
     END IF;
      IF NEW.technical_name IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Tech Chemical Name',NEW.technical_name));
     END IF;
     IF NEW.imo_pri_sub_class_code IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'IMO Subsidary Class',NEW.imo_pri_sub_class_code));
     END IF;
     IF NEW.imo_sec_sub_class_code IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'IMO Secondary Class',NEW.imo_sec_sub_class_code));
     END IF;
      IF NEW.inner_pkg_no_pieces IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Inner Packing Pieces',NEW.inner_pkg_no_pieces));
     END IF;
      IF NEW.inner_pkg_type IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Inner Packaging Type',NEW.inner_pkg_type));
     END IF;
      IF NEW.inner_pkg_composition IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Inner Pkg Composition',NEW.inner_pkg_composition));
     END IF;
     IF NEW.inner_pkg_uom IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Inner Pkg UOM',NEW.inner_pkg_uom));
     END IF;
     IF NEW.inner_pkg_nwt_piece IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Inner Pkg Wt/Vol Per Piece',NEW.inner_pkg_nwt_piece));
     END IF;
      IF NEW.reportable_quantity IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Reportable Quantity',IF(NEW.reportable_quantity=0,'No','Yes')));
     END IF;
      IF NEW.marine_pollutant IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Marine pollutant',IF(NEW.marine_pollutant=0,'No','Yes')));
     END IF;
      IF NEW.excepted_quantity IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Excepted Quantity',IF(NEW.excepted_quantity=0,'No','Yes')));
     END IF;
       IF NEW.limited_quantity IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Limited Quantity',IF(NEW.limited_quantity=0,'No','Yes')));
     END IF;
       IF NEW.residue IS NOT NULL THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Residue',IF(NEW.residue=0,'No','Yes')));
     END IF;
     IF NEW.inhalation_hazard IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Inhalation Hazard',IF(NEW.inhalation_hazard=0,'No','Yes')));
     END IF;
     IF inserted_values IS NOT NULL THEN
     SET inserted_values=CONCAT('INSERTED ->',inserted_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(NEW.file_number_id,'QT-AutoNotes',inserted_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;


DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclQuoteHazmat_update`$$

CREATE TRIGGER `after_LclQuoteHazmat_update` AFTER UPDATE ON `lcl_quote_hazmat`
    FOR EACH ROW BEGIN
     DECLARE updated_values TEXT ;
      IF isNotEqual(OLD.un_hazmat_no,NEW.un_hazmat_no)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'UN#',OLD.un_hazmat_no,NEW.un_hazmat_no));
     END IF;
     IF isNotEqual(OLD.proper_shipping_name,NEW.proper_shipping_name)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Proper Shipping Name',OLD.proper_shipping_name,NEW.proper_shipping_name));
     END IF;
      IF isNotEqual(OLD.imo_pri_class_code,NEW.imo_pri_class_code)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Class',OLD.imo_pri_class_code,NEW.imo_pri_class_code));
     END IF;
      IF isNotEqual(OLD.packing_group_code,NEW.packing_group_code)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Packaging Group Code',OLD.packing_group_code,NEW.packing_group_code));
     END IF;
      IF isNotEqual(OLD.flash_point,NEW.flash_point)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Flash point',OLD.flash_point,NEW.flash_point));
     END IF;
       IF isNotEqual(OLD.outer_pkg_no_pieces,NEW.outer_pkg_no_pieces)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Outer Packaging Pieces',OLD.outer_pkg_no_pieces,NEW.outer_pkg_no_pieces));
     END IF;
       IF isNotEqual(OLD.outer_pkg_type,NEW.outer_pkg_type)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Outer Packaging Type',OLD.outer_pkg_type,NEW.outer_pkg_type));
     END IF;
       IF isNotEqual(OLD.outer_pkg_composition,NEW.outer_pkg_composition)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Outer Pkg Composition',OLD.outer_pkg_composition,NEW.outer_pkg_composition));
     END IF;
      IF isNotEqual(OLD.total_net_weight,NEW.total_net_weight)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Total Net Weight/Kgs',OLD.total_net_weight,NEW.total_net_weight));
     END IF;
      IF isNotEqual(OLD.liquid_volume,NEW.liquid_volume)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Total Volume',OLD.liquid_volume,NEW.liquid_volume));
     END IF;
       IF isNotEqual(OLD.total_gross_weight,NEW.total_gross_weight)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Total Gross Weight/Kgs',OLD.total_gross_weight,NEW.total_gross_weight));
     END IF;
      IF isNotEqual(OLD.ems_code,NEW.ems_code)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'EMS code',OLD.ems_code,NEW.ems_code));
     END IF;
      IF isNotEqual(OLD.technical_name,NEW.technical_name)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Tech Chemical Name',OLD.technical_name,NEW.technical_name));
     END IF;
     IF isNotEqual(OLD.imo_pri_sub_class_code,NEW.imo_pri_sub_class_code)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'IMO Subsidary Class',OLD.imo_pri_sub_class_code,NEW.imo_pri_sub_class_code));
     END IF;
     IF isNotEqual(OLD.imo_sec_sub_class_code,NEW.imo_sec_sub_class_code)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'IMO Secondary Class',OLD.imo_sec_sub_class_code,NEW.imo_sec_sub_class_code));
     END IF;
      IF isNotEqual(OLD.inner_pkg_no_pieces,NEW.inner_pkg_no_pieces)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Inner Packing Pieces',OLD.inner_pkg_no_pieces,NEW.inner_pkg_no_pieces));
     END IF;
      IF isNotEqual(OLD.inner_pkg_type,NEW.inner_pkg_type)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Inner Packaging Type',OLD.inner_pkg_type,NEW.inner_pkg_type));
     END IF;
      IF isNotEqual(OLD.inner_pkg_composition,NEW.inner_pkg_composition)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Inner Pkg Composition',OLD.inner_pkg_composition,NEW.inner_pkg_composition));
     END IF;
     IF isNotEqual(OLD.inner_pkg_uom,NEW.inner_pkg_uom)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Inner Pkg UOM',OLD.inner_pkg_uom,NEW.inner_pkg_uom));
     END IF;
     IF isNotEqual(OLD.inner_pkg_nwt_piece,NEW.inner_pkg_nwt_piece)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Inner Pkg Wt/Vol Per Piece',OLD.inner_pkg_nwt_piece,NEW.inner_pkg_nwt_piece));
     END IF;
      IF isNotEqual(OLD.reportable_quantity,NEW.reportable_quantity)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Reportable Quantity',IF(old.reportable_quantity=0,'No','Yes'),IF(new.reportable_quantity=0,'No','Yes')));
     END IF;
      IF isNotEqual(OLD.marine_pollutant,NEW.marine_pollutant)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Marine pollutant',IF(old.marine_pollutant=0,'No','Yes'),IF(new.marine_pollutant=0,'No','Yes')));
     END IF;
      IF isNotEqual(OLD.excepted_quantity,NEW.excepted_quantity)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Excepted Quantity',IF(old.excepted_quantity=0,'No','Yes'),IF(new.excepted_quantity=0,'No','Yes')));
     END IF;
       IF isNotEqual(OLD.limited_quantity,NEW.limited_quantity)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Limited Quantity',IF(old.limited_quantity=0,'No','Yes'),IF(new.limited_quantity=0,'No','Yes')));
     END IF;
       IF isNotEqual(OLD.residue,NEW.residue)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Residue',IF(old.residue=0,'No','Yes'),IF(new.residue=0,'No','Yes')));
     END IF;
     IF isNotEqual(OLD.inhalation_hazard,NEW.inhalation_hazard)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Inhalation Hazard',IF(old.inhalation_hazard=0,'No','Yes'),IF(new.inhalation_hazard=0,'No','Yes')));
     END IF;
     IF updated_values IS NOT NULL THEN
    SET updated_values=CONCAT(updated_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(OLD.file_number_id,'QT-AutoNotes',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclQuoteImport_update`$$

CREATE TRIGGER `after_LclQuoteImport_update` AFTER UPDATE ON `lcl_quote_import`
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT ;
       IF ((OLD.door_delivery_eta IS NULL AND NEW.door_delivery_eta IS NOT NULL) OR NEW.door_delivery_eta != OLD.door_delivery_eta OR (OLD.door_delivery_eta IS NOT NULL AND NEW.door_delivery_eta IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'Door Delivery ETA',IFNULL(DATE_FORMAT(old.door_delivery_eta,'%d-%b-%Y'),''),IFNULL(DATE_FORMAT(new.door_delivery_eta,'%d-%b-%Y'),'')));
    END IF;
     IF new.express_release!=old.express_release THEN
      SET updated_values=(SELECT concat_string(updated_values,'Express Release',IF(old.express_release=0,'No','Yes'),IF(new.express_release=0,'No','Yes')));
    END IF;
    IF updated_values IS NOT NULL THEN
      SET updated_values=CONCAT(updated_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(OLD.file_number_id,'QT-AutoNotes',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;


DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclQuotePiece_insert`$$

CREATE TRIGGER `after_LclQuotePiece_insert` AFTER INSERT ON `lcl_quote_piece`
    FOR EACH ROW BEGIN
    DECLARE inserted_values TEXT;
	IF (NEW.commodity_type_id <> '' AND NEW.commodity_type_id IS NOT NULL) THEN
		SET inserted_values=(SELECT concat_insert_values(inserted_values,' Commodity',(SELECT desc_en FROM commodity_type WHERE id=new.commodity_type_id LIMIT 1)));
	END IF;
	IF (NEW.packaging_type_id <> '' AND NEW.packaging_type_id IS NOT NULL) THEN
		SET inserted_values=(SELECT concat_insert_values(inserted_values,' Package',(SELECT description FROM package_type WHERE id=new.packaging_type_id LIMIT 1)));
	END IF;
	IF (NEW.booked_piece_count <> '' AND NEW.booked_piece_count IS NOT NULL) THEN
		SET inserted_values=(SELECT concat_insert_values(inserted_values,' Piece Count',new.booked_piece_count));
	END IF;
	IF (NEW.booked_weight_imperial <> '' AND NEW.booked_weight_imperial IS NOT NULL) THEN
		SET inserted_values=(SELECT concat_insert_values(inserted_values,' Weight  LBS',new.booked_weight_imperial));
	END IF;
	IF (NEW.booked_weight_metric <> '' AND NEW.booked_weight_metric IS NOT NULL) THEN
		SET inserted_values=(SELECT concat_insert_values(inserted_values,' Weight  KGS',new.booked_weight_metric));
	END IF;
	IF (NEW.booked_volume_imperial <> '' AND NEW.booked_volume_imperial IS NOT NULL) THEN
		SET inserted_values=(SELECT concat_insert_values(inserted_values,' Volume  CFT',new.booked_volume_imperial));
	END IF;
	IF (NEW.booked_volume_metric <> '' AND NEW.booked_volume_metric IS NOT NULL) THEN
           SET inserted_values=(SELECT concat_insert_values(inserted_values,' Volume  CBM',new.booked_volume_metric));
	END IF;
        IF NEW.hazmat IS NOT NULL THEN
         SET inserted_values = (SELECT concat_insert_values(inserted_values,'HaZmat',IF(NEW.hazmat=0,'No','Yes')));
         END IF;
	IF (NEW.piece_desc <> '' AND NEW.piece_desc IS NOT NULL) THEN
	   SET inserted_values=CONCAT(IFNULL(inserted_values,''),' Commodity Desc-->',NEW.piece_desc);
        END IF;
       IF (NEW.mark_no_desc <> '' AND NEW.mark_no_desc IS NOT NULL) THEN
         SET inserted_values=CONCAT(IFNULL(inserted_values,''),' Marks and Numbers-->',NEW.mark_no_desc);
       END IF;
        IF (inserted_values <> '') THEN
        SET inserted_values=CONCAT('Inserted ->',inserted_values);
		INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
		VALUES(new.file_number_id,'QT-AutoNotes',inserted_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
	END IF;
    END;
$$

DELIMITER ;


DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclQuotePiece_update`$$

CREATE TRIGGER `after_LclQuotePiece_update` AFTER UPDATE ON `lcl_quote_piece`
    FOR EACH ROW BEGIN
	DECLARE updated_values TEXT ;
	IF new.commodity_type_id!=old.commodity_type_id THEN
		SET updated_values=(SELECT concat_string(updated_values,' Commodity',(SELECT CONCAT(desc_en,'(',CODE,')') FROM commodity_type WHERE id=old.commodity_type_id),(SELECT CONCAT(desc_en ,'(',CODE,')') FROM commodity_type WHERE id=new.commodity_type_id)));
	END IF;
	IF new.packaging_type_id!=old.packaging_type_id THEN
		SET updated_values=(SELECT concat_string(updated_values,' Package',(SELECT description FROM package_type WHERE id=old.packaging_type_id),(SELECT description FROM package_type WHERE id=new.packaging_type_id)));
	END IF;
	IF new.booked_piece_count!=old.booked_piece_count THEN
		SET updated_values=(SELECT concat_string(updated_values,' Piece Count',old.booked_piece_count,new.booked_piece_count));
	END IF;
	IF new.booked_weight_imperial!=old.booked_weight_imperial THEN
		SET updated_values=(SELECT concat_string(updated_values,' Weight LBS',old.booked_weight_imperial,new.booked_weight_imperial));
	END IF;
	IF new.booked_weight_metric!=old.booked_weight_metric THEN
		SET updated_values=(SELECT concat_string(updated_values,' Weight KGS',old.booked_weight_metric,new.booked_weight_metric));
	END IF;
	IF new.booked_volume_imperial!=old.booked_volume_imperial THEN
		SET updated_values=(SELECT concat_string(updated_values,' Volume CFT',old.booked_volume_imperial,new.booked_volume_imperial));
	END IF;
	IF new.booked_volume_metric!=old.booked_volume_metric THEN
		SET updated_values=(SELECT concat_string(updated_values,' Volume CBM',old.booked_volume_metric,new.booked_volume_metric));
	END IF;
	IF new.hazmat <> old.hazmat THEN
          SET updated_values=(SELECT concat_string(updated_values,'HaZmat',IF(old.hazmat=0,'No','Yes'),IF(new.hazmat=0,'No','Yes')));
        END IF;
	IF new.actual_piece_count!=old.actual_piece_count THEN
		SET updated_values=(SELECT concat_string(updated_values,'Actual Piece Count',old.actual_piece_count,new.actual_piece_count));
	END IF;
	IF new.actual_weight_imperial!=old.actual_weight_imperial THEN
		SET updated_values=(SELECT concat_string(updated_values,'Actual Weight LBS',old.actual_weight_imperial,new.actual_weight_imperial));
	END IF;
	IF new.actual_weight_metric!=old.actual_weight_metric THEN
		SET updated_values=(SELECT concat_string(updated_values,'Actual Weight KGS',old.actual_weight_metric,new.actual_weight_metric));
	END IF;
	IF new.actual_volume_imperial!=old.actual_volume_imperial THEN
		SET updated_values=(SELECT concat_string(updated_values,'Actual Volume CFT',old.actual_volume_imperial,new.actual_volume_imperial));
	END IF;
	IF new.actual_volume_metric!=old.actual_volume_metric THEN
		SET updated_values=(SELECT concat_string(updated_values,'Actual Volume CBM',old.actual_volume_metric,new.actual_volume_metric));
	END IF;
        IF ((OLD.piece_desc IS NULL AND NEW.piece_desc IS NOT NULL) OR NEW.piece_desc != OLD.piece_desc OR (OLD.piece_desc IS NOT NULL AND NEW.piece_desc IS NULL)) THEN
        SET updated_values=CONCAT(IFNULL(updated_values,''),' Commodity Desc-->','Descriptions have been changed');
        END IF;
       IF ((OLD.mark_no_desc IS NULL AND NEW.mark_no_desc IS NOT NULL) OR NEW.mark_no_desc != OLD.mark_no_desc OR (OLD.mark_no_desc IS NOT NULL AND NEW.mark_no_desc IS NULL)) THEN
         SET updated_values=CONCAT(IFNULL(updated_values,''),' Marks and Numbers-->','Marks have been changed');
       END IF;
        IF updated_values IS NOT NULL THEN
        SET updated_values=CONCAT('UPDATED ->',updated_values);
		INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
		VALUES(OLD.file_number_id,'QT-AutoNotes',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
	END IF;
        END;
$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_lclQuotePieceWhse_insert`$$

CREATE TRIGGER `after_lclQuotePieceWhse_insert` AFTER INSERT ON `lcl_quote_piece_whse`
    FOR EACH ROW BEGIN
    DECLARE insert_values TEXT;
    SET insert_values = concat_insert_values(insert_values,'Warehouse Location',(SELECT CONCAT(warehsname,'(',warehsno,')') FROM warehouse WHERE id=new.warehouse_id));
    IF new.location!='' THEN
    SET insert_values=(SELECT concat_insert_values(insert_values,'LineLocation',UPPER(new.location)));
    END IF;
    IF insert_values IS NOT NULL THEN
       SET @fileId=(SELECT file_number_id FROM lcl_quote_piece WHERE id=new.quote_piece_id);
        SET insert_values=CONCAT('INSERTED ->',insert_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(@fileId,'QT-AutoNotes',insert_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;





DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBooking_insert`$$

CREATE TRIGGER `after_LclBooking_insert` AFTER INSERT ON `lcl_booking`
    FOR EACH ROW BEGIN
    DECLARE insert_values TEXT ;
    IF new.rtd_agent_acct_no <> '' THEN
    SET insert_values=concat_insert_values(insert_values,'Agent Info',new.rtd_agent_acct_no);
    END IF;
   IF new.rtd_transaction!='' THEN
   SET insert_values = concat_insert_values(insert_values,'ERT Y/N',IF(new.rtd_transaction=0,'No','Yes'));
   END IF;
   IF new.bill_to_party <> '' THEN
   SET insert_values=concat_insert_values(insert_values,'BillToCode',new.bill_to_party);
   END IF;
   IF new.poo_whse_contact_id <> '' THEN
   SET @newBillingTerminal=(SELECT address FROM lcl_contact WHERE id=new.poo_whse_contact_id);
    SET insert_values=concat_insert_values(insert_values,'Deliver cargo to',IFNULL(@newBillingTerminal,''));
    END IF;
     IF new.insurance  <> '' THEN
     SET insert_values=concat_insert_values(insert_values,'Insurance',new.insurance);
     END IF;
    IF insert_values IS NOT NULL THEN
    SET insert_values=CONCAT('INSERTED ->',insert_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(new.file_number_id,'DR-AutoNotes',insert_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;



DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBooking_update`$$

CREATE TRIGGER `after_LclBooking_update` AFTER UPDATE ON `lcl_booking`
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT;
    IF old.non_rated!=new.non_rated THEN
    SET updated_values=(SELECT concat_string(updated_values,'Unknown Dest',IF(old.non_rated=0,'No','Yes'),IF(new.non_rated=0,'No','Yes')));
    END IF;
    IF old.poo_id!=new.poo_id THEN
	SET updated_values=(SELECT concat_string(updated_values,'Origin CFS',(SELECT un_loc_name FROM un_location WHERE id=old.poo_id),(SELECT un_loc_name FROM un_location WHERE id=new.poo_id)));
    END IF;
    IF old.pol_id!=new.pol_id THEN
	SET updated_values=(SELECT concat_string(updated_values,'POL',(SELECT un_loc_name FROM un_location WHERE id=old.pol_id),(SELECT un_loc_name FROM un_location WHERE id=new.pol_id)));
    END IF;
    IF old.pod_id!=new.pod_id THEN
	SET updated_values=(SELECT concat_string(updated_values,'POD',(SELECT un_loc_name FROM un_location WHERE id=old.pod_id),(SELECT un_loc_name FROM un_location WHERE id=new.pod_id)));
    END IF;
    IF old.fd_id!=new.fd_id THEN
	SET updated_values=(SELECT concat_string(updated_values,'Destination',(SELECT un_loc_name FROM un_location WHERE id=old.fd_id),(SELECT un_loc_name FROM un_location WHERE id=new.fd_id)));
    END IF;
    IF ((old.client_acct_no IS NULL AND new.client_acct_no IS NOT NULL)OR new.client_acct_no!=old.client_acct_no OR(new.client_acct_no IS NULL AND old.client_acct_no IS NOT NULL))THEN
    SET @oldClientValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.client_acct_no);
    SET @newClientValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.client_acct_no);
    SET updated_values=(SELECT concat_string (updated_values,'Client',IFNULL(@oldClientValues,''),IFNULL(@newClientValues,'')));
    END IF;
    IF ((old.ship_acct_no IS NULL AND new.ship_acct_no IS NOT NULL)OR new.ship_acct_no!=old.ship_acct_no OR(new.ship_acct_no IS NULL AND old.ship_acct_no IS NOT NULL))THEN
    SET @oldClientValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.ship_acct_no);
    SET @newClientValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.ship_acct_no);
    SET updated_values=(SELECT concat_string (updated_values,'Shipper',IFNULL(@oldClientValues,''),IFNULL(@newClientValues,'')));
    END IF;
     IF ((old.fwd_acct_no IS NULL AND new.fwd_acct_no IS NOT NULL)OR new.fwd_acct_no!=old.fwd_acct_no OR(new.fwd_acct_no IS NULL AND old.fwd_acct_no IS NOT NULL))THEN
    SET @oldClientValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.fwd_acct_no);
    SET @newClientValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.fwd_acct_no);
    SET updated_values=(SELECT concat_string (updated_values,'Forwarder',IFNULL(@oldClientValues,''),IFNULL(@newClientValues,'')));
    END IF;
   IF ((OLD.third_party_acct_no IS NULL AND NEW.third_party_acct_no IS NOT NULL) OR NEW.third_party_acct_no != OLD.third_party_acct_no OR (OLD.third_party_acct_no IS NOT NULL AND NEW.third_party_acct_no IS NULL)) THEN
     SET @oldThirdValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.third_party_acct_no);
     SET @newThirdValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.third_party_acct_no);
     SET updated_values=(SELECT concat_string(updated_values,'Third Party',IFNULL(@oldThirdValues,''),IFNULL(@newThirdValues,'')));
   END IF;
   IF ((OLD.cons_acct_no IS NULL AND NEW.cons_acct_no IS NOT NULL) OR NEW.cons_acct_no != OLD.cons_acct_no OR (OLD.cons_acct_no IS NOT NULL AND NEW.cons_acct_no IS NULL)) THEN
     SET @oldConsValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.cons_acct_no);
     SET @newConsValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.cons_acct_no);
     SET updated_values=(SELECT concat_string(updated_values,'House Consignee',IFNULL(@oldConsValues,''),IFNULL(@newConsValues,'')));
   END IF;
    IF ((OLD.noty_acct_no IS NULL AND NEW.noty_acct_no IS NOT NULL) OR NEW.noty_acct_no != OLD.noty_acct_no OR (OLD.noty_acct_no IS NOT NULL AND NEW.noty_acct_no IS NULL)) THEN
     SET @oldNotyValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.noty_acct_no);
     SET @newNotyValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.noty_acct_no);
     SET updated_values=(SELECT concat_string(updated_values,'Notify Party',IFNULL(@oldNotyValues,''),IFNULL(@newNotyValues,'')));
   END IF;
   IF ((OLD.sup_acct_no IS NULL AND NEW.sup_acct_no IS NOT NULL) OR NEW.sup_acct_no!=OLD.sup_acct_no OR (OLD.sup_acct_no IS NOT NULL AND NEW.sup_acct_no IS NULL)) THEN
   SET @oldSupValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.sup_acct_no);
   SET @newSupValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.sup_acct_no);
   SET updated_values=(SELECT concat_string(updated_values,'Supplier',IFNULL(@oldSupValues,''),IFNULL(@newSupValues,'')));
   END IF;
   IF new.relay_override!=old.relay_override THEN
   SET updated_values =(SELECT concat_string(updated_values,'Relay override',IF(old.relay_override=0,'No','Yes'),IF(new.relay_override=0,'No','Yes')));
   END IF;
    IF new.delivery_metro!=old.delivery_metro THEN
       SET updated_values=(SELECT concat_string(updated_values,'Delivery Metro',old.delivery_metro,new.delivery_metro));
    END IF;
    IF new.insurance!=old.insurance THEN
      SET updated_values=(SELECT concat_string(updated_values,'Insurance',IF(old.insurance=0,'No','Yes'),IF(new.insurance=0,'No','Yes')));
    END IF;
    IF new.documentation!=old.documentation THEN
      SET updated_values=(SELECT concat_string(updated_values,'Documentation',IF(old.documentation=0,'No','Yes'),IF(new.documentation=0,'No','Yes')));
    END IF;
    IF new.spot_rate!=old.spot_rate THEN
      SET updated_values=(SELECT concat_string(updated_values,'SpotRate',IF(old.spot_rate=0,'No','Yes'),IF(new.spot_rate=0,'No','Yes')));
    END IF;
    IF new.spot_comment!=old.spot_comment THEN
      SET updated_values=(SELECT concat_string(updated_values,'SpotRate Comment',old.spot_comment,new.spot_comment));
    END IF;
    IF new.spot_rate_uom!=old.spot_rate_uom THEN
      SET updated_values=(SELECT concat_string(updated_values,'SpotRate W/M',old.spot_rate_uom,new.spot_rate_uom));
    END IF;
    IF new.spot_wm_rate!=old.spot_wm_rate THEN
      SET updated_values=(SELECT concat_string(updated_values,'SpotRate',CONCAT('$',old.spot_wm_rate),CONCAT('$',new.spot_wm_rate)));
    END IF;
    IF new.poo_pickup!=old.poo_pickup THEN
      SET updated_values=(SELECT concat_string(updated_values,'Pickup',IF(old.poo_pickup=0,'No','Yes'),IF(new.poo_pickup=0,'No','Yes')));
    END IF;
    IF new.billing_type!=old.billing_type THEN
    SET updated_values=(SELECT concat_string(updated_values,'BillingType',old.billing_type,new.billing_type));
    END IF;
    IF new.rate_type!=old.rate_type THEN
    SET updated_values=(SELECT concat_string(updated_values,'RateType',old.rate_type,new.rate_type));
    END IF;
    IF new.billing_terminal!=old.billing_terminal THEN
    SET @oldBillingTerminal=(SELECT CONCAT(terminal_location,'/',trmnum) FROM terminal WHERE trmnum=old.billing_terminal);
    SET @newBillingTerminal=(SELECT CONCAT(terminal_location,'/',trmnum) FROM terminal WHERE trmnum=new.billing_terminal);
    SET updated_values=(SELECT concat_string(updated_values,'Term to do BL',IFNULL(@oldBillingTerminal,''),IFNULL(@newBillingTerminal,'')));
    END IF;
    IF new.poo_whse_contact_id!=old.poo_whse_contact_id THEN
    SET @oldBillingTerminal=(SELECT address FROM lcl_contact WHERE lcl_contact.id=old.poo_whse_contact_id);
    SET @newBillingTerminal=(SELECT address FROM lcl_contact WHERE lcl_contact.id=new.poo_whse_contact_id);
    SET updated_values=(SELECT concat_string(updated_values,'Deliver cargo to',IFNULL(@oldBillingTerminal,''),IFNULL(@newBillingTerminal,'')));
    END IF;
    IF new.default_agent!=old.default_agent THEN
    SET updated_values=(SELECT concat_string(updated_values,'Default Agent',IF(old.default_agent=0,'NO','Yes'),IF(new.default_agent=0,'No','Yes')));
    END IF;
    IF new.rtd_agent_acct_no!=old.rtd_agent_acct_no THEN
    SET updated_values=(SELECT concat_string(updated_values,'Agent Info',old.rtd_agent_acct_no,new.rtd_agent_acct_no));
    END IF;
     IF new.rtd_transaction!=old.rtd_transaction THEN
      SET updated_values=(SELECT concat_string(updated_values,'ERT Y/N',IF(old.rtd_transaction=0,'No','Yes'),IF(new.rtd_transaction=0,'No','Yes')));
    END IF;
    IF new.bill_to_party!=old.bill_to_party THEN
    SET updated_values=(SELECT concat_string(updated_values,'BillToCode',old.bill_to_party,new.bill_to_party));
    END IF;
    IF ((OLD.booked_ss_header_id IS NULL AND NEW.booked_ss_header_id IS NOT NULL) OR NEW.booked_ss_header_id!=OLD.booked_ss_header_id OR (OLD.booked_ss_header_id IS NOT NULL AND NEW.booked_ss_header_id IS NULL)) THEN
     SET @oldScheduleValue = (SELECT CONCAT (l.`schedule_no` ,' / ', DATE_FORMAT((SELECT d.std FROM lcl_ss_detail d WHERE d.ss_header_id=old.booked_ss_header_id  AND d.trans_mode='V'),'%d-%b-%Y'))  FROM lcl_ss_header  l  WHERE  l.id=old.booked_ss_header_id);
     SET @newScheduleValue = (SELECT CONCAT (l.`schedule_no` ,' / ', DATE_FORMAT((SELECT d.std FROM lcl_ss_detail d WHERE d.ss_header_id=new.booked_ss_header_id  AND d.trans_mode='V'),'%d-%b-%Y'))  FROM lcl_ss_header  l  WHERE  l.id=new.booked_ss_header_id);
     SET updated_values=(SELECT concat_string(updated_values,'Booked Voyage is changed',IFNULL(@oldScheduleValue,''),IFNULL(@newScheduleValue,'')) );
    END IF;
    IF old.client_pwk_recvd!=new.client_pwk_recvd THEN
	SET updated_values = (SELECT concat_string(updated_values,'PWK/Docs Received',IF(old.client_pwk_recvd=0,'No','Yes'),IF(new.client_pwk_recvd=0,'No','Yes')));
    END IF;
    IF updated_values IS NOT NULL THEN
    SET updated_values=CONCAT(updated_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(OLD.file_number_id,'DR-AutoNotes',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
      END;
$$

DELIMITER ;



DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `lcl_booking_ac_insert_trigger`$$

CREATE TRIGGER `lcl_booking_ac_insert_trigger` AFTER INSERT ON `lcl_booking_ac`
    FOR EACH ROW BEGIN
  DECLARE insert_values TEXT DEFAULT '';
  DECLARE changesFlag BOOLEAN DEFAULT FALSE;
  DECLARE mFileNumberId BIGINT(20);
  DECLARE mShipmentType VARCHAR(4);
  DECLARE mBlNo VARCHAR(30);
  DECLARE mDrNo VARCHAR(7);
  DECLARE mBookingNo VARCHAR(20);
  DECLARE mVoyageNo VARCHAR(30);
  DECLARE mContainerNo VARCHAR(30);
  DECLARE mEta DATETIME;
  DECLARE mSsline VARCHAR(50);
  DECLARE mOrigin VARCHAR(100);
  DECLARE mDestination VARCHAR(100);
  DECLARE mShipperName VARCHAR(50);
  DECLARE mShipperNo VARCHAR(10);
  DECLARE mConsigneeName VARCHAR(50);
  DECLARE mConsigneeNo VARCHAR(10);
  DECLARE mForwarderName VARCHAR(50);
  DECLARE mForwarderNo VARCHAR(10);
  DECLARE mThirdPartyName VARCHAR(50);
  DECLARE mThirdPartyNo VARCHAR(10);
  DECLARE mAgentName VARCHAR(50);
  DECLARE mAgentNo VARCHAR(10);
  DECLARE mTerminal VARCHAR(5);
  DECLARE mVesselNo VARCHAR(20);
  DECLARE mMasterBl VARCHAR(50);
  DECLARE mSubhouseBl VARCHAR(50);
  DECLARE mApGlMappingId BIGINT(20);
  DECLARE mCostAmount DECIMAL(10,2);
  DECLARE mVendorName VARCHAR(50);
  DECLARE mVendorNo VARCHAR(10);
  DECLARE mInvoiceNo VARCHAR(50);
  DECLARE mCostId BIGINT(20);
  DECLARE mBsCostCode VARCHAR(5);
  DECLARE mCostCode VARCHAR(20);
  DECLARE mGlAccount VARCHAR(20);
  DECLARE mUserId INT(11);
  DECLARE mAccrualStatus VARCHAR(10);
  DECLARE mAccrualNotFound BOOLEAN DEFAULT FALSE;
  DECLARE mBkPieceUnitId BIGINT(20);
  IF (new.ar_gl_mapping_id <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Code', (SELECT charge_code FROM gl_mapping WHERE id = new.ar_gl_mapping_id)) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.ar_amount <> 0.00) THEN
    SET insert_values = concat_insert_values (insert_values, ' Charge Amount', new.ar_amount) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.ap_amount <> 0.00) THEN
    SET insert_values = concat_insert_values (insert_values, ' Cost Amount', new.ap_amount) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.rate_per_weight_unit <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Rate Weight', new.rate_per_weight_unit) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.rate_per_volume_unit <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Rate Measure', new.rate_per_volume_unit) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.rate_flat_minimum <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Rate Minimum', new.rate_flat_minimum) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.cost_weight <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Cost Weight', new.cost_weight) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.cost_measure <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Cost Measure', new.cost_measure) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.cost_minimum <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Cost Minimum', new.cost_minimum) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.sp_acct_no <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Vendor Name', (SELECT acct_name FROM trading_partner WHERE acct_no = new.sp_acct_no)) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.sp_acct_no <> '') THEN
     SET insert_values = concat_insert_values (insert_values, ' Vendor Number', new.sp_acct_no) ;
     SET changesFlag = TRUE ;
  END IF ;
  IF (new.invoice_number <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Invoice Number', new.invoice_number) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (insert_values <> '') THEN
    IF (changesFlag = TRUE) THEN
      CALL update_lclbooking_moddate (new.file_number_id) ;
    END IF ;
    SET insert_values = CONCAT('INSERTED ->', insert_values) ;
    IF (new.converted) THEN
      SET insert_values = CONCAT(insert_values, 'Converted from Eculine EDI') ;
    END IF ;
    INSERT INTO lcl_remarks (
      file_number_id,
      TYPE,
      remarks,
      entered_datetime,
      entered_by_user_id,
      modified_datetime,
      modified_by_user_id
    )
    VALUES (
      new.file_number_id,
      'DR-AutoNotes',
      insert_values,
      NOW(),
      new.entered_by_user_id,
      NOW(),
      new.modified_by_user_id
    ) ;
  END IF ;
  IF (new.ap_amount <> 0.00 AND new.`sp_acct_no` <> '' AND new.ap_gl_mapping_id IS NOT NULL AND new.`file_number_id` IS NOT NULL) THEN
  SELECT
     lbpu.id
  INTO mBkPieceUnitId
  FROM
     `lcl_booking_piece` lbp
      JOIN `lcl_booking_piece_unit` lbpu
        ON (lbp.`id` = lbpu.`booking_piece_id`)
       JOIN `lcl_unit_ss` lus
        ON (lbpu.`lcl_unit_ss_id` = lus.id)
       JOIN `lcl_ss_header` lssh
        ON (lus.`ss_header_id` = lssh.`id`)
        WHERE lbp.`file_number_id` = new.`file_number_id` AND  lssh.service_type <> 'N'
        GROUP BY lbp.`file_number_id`;
    SELECT
      fn.`id`,
      IF(bk.`booking_type` = 'E', 'LCLE', 'LCLI') AS shipmentType,
      IF(bk.`booking_type` = 'E', CONCAT_WS('-',
       CONVERT((SELECT IF(t.unlocationcode1 <> '',RIGHT(t.unlocationcode1,3),t.trmnum) FROM terminal t WHERE t.trmnum=bk.billing_terminal), CHARACTER CHARSET utf8),
       CONVERT((SELECT IF(u.bl_numbering = 'Y', RIGHT(`UnLocationGetCodeByID`(bk.`fd_id`),3),
       `UnLocationGetCodeByID` (bk.`fd_id`)) FROM un_location u WHERE u.id=bk.`fd_id`), CHARACTER CHARSET utf8),
        CONVERT(fn.`file_number`, CHARACTER CHARSET utf8)), CONCAT('IMP-', fn.`file_number`)) AS blNo,
      fn.`file_number` AS drNo,
      us.`sp_booking_no` AS bookingNo,
      CONCAT_WS('-', CONVERT(ssh.`billing_trmnum`, CHARACTER CHARSET utf8), `UnLocationGetCodeByID` (ssh.`origin_id`), `UnLocationGetCodeByID` (ssh.`destination_id`), ssh.`schedule_no`) AS voyageNo,
      (SELECT u.`unit_no` FROM `lcl_unit` u WHERE u.`id` = us.`unit_id` LIMIT 1) AS containerNo,
       IF(bk.`booking_type` = 'E',ssd.std,ssd.`sta`) AS eta,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = ssd.`sp_acct_no` LIMIT 1) AS ssline,
      `UnLocationGetNameStateCntryByID` (ssh.`origin_id`) AS origin,
      `UnLocationGetNameStateCntryByID` (ssh.`destination_id`) AS destination,
      bk.`ship_acct_no` AS shipperNo,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = bk.`ship_acct_no` LIMIT 1) AS shipperName,
      bk.`cons_acct_no` AS consigneeNo,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = bk.`cons_acct_no` LIMIT 1) AS consigneeName,
      bk.`fwd_acct_no` AS forwarderNo,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = bk.`fwd_acct_no` LIMIT 1) AS forwarderName,
      bk.`third_party_acct_no` AS thirdPartyNo,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = bk.`third_party_acct_no` LIMIT 1) AS thirdPartyName,
      bk.`agent_acct_no` AS agentNo,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = bk.`agent_acct_no` LIMIT 1) AS agentName,
      COALESCE(ssh.`billing_trmnum`, bk.`billing_terminal`) AS terminal,
      (SELECT `code` FROM `genericcode_dup` WHERE `codedesc` = ssd.`sp_reference_name` AND `codetypeid` = (SELECT `codetypeid` FROM `codetype` WHERE `description` = 'Vessel Codes' LIMIT 1) LIMIT 1) AS vesselNo,
      (SELECT `masterbl` FROM `lcl_unit_ss_manifest` WHERE `ss_header_id` = ssh.`id` AND `unit_id` = us.`unit_id` LIMIT 1) AS masterBl,
      (SELECT `sub_house_bl` FROM `lcl_booking_import` WHERE `file_number_id` = fn.`id` LIMIT 1) AS subhouseBl,
      new.`ap_gl_mapping_id` AS apGlMappingId,
      new.`ap_amount` AS costAmount,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = new.`sp_acct_no` LIMIT 1) AS vendorName,
      new.`sp_acct_no` AS vendorNo,
      new.`invoice_number` AS invoiceNo,
      new.`id` AS costId,
      new.`modified_by_user_id` AS userId
    INTO
      mFileNumberId,
      mShipmentType,
      mBlNo,
      mDrNo,
      mBookingNo,
      mVoyageNo,
      mContainerNo,
      mEta,
      mSsline,
      mOrigin,
      mDestination,
      mShipperNo,
      mShipperName,
      mConsigneeNo,
      mConsigneeName,
      mForwarderNo,
      mForwarderName,
      mThirdPartyNo,
      mThirdPartyName,
      mAgentNo,
      mAgentName,
      mTerminal,
      mVesselNo,
      mMasterBl,
      mSubhouseBl,
      mApGlMappingId,
      mCostAmount,
      mVendorName,
      mVendorNo,
      mInvoiceNo,
      mCostId,
      mUserId
    FROM
      `lcl_file_number` fn
      JOIN `lcl_booking` bk
        ON (fn.`id` = bk.`file_number_id`)
      JOIN `lcl_booking_piece` bp
        ON (fn.`id` = bp.`file_number_id`)
      LEFT JOIN `lcl_booking_piece_unit` bpu
        ON (bp.`id` = bpu.`booking_piece_id` AND bpu.id=mBkPieceUnitId)
      LEFT JOIN `lcl_unit_ss` us
        ON (bpu.`lcl_unit_ss_id` = us.id)
      LEFT JOIN `lcl_ss_header` ssh
        ON (us.`ss_header_id` = ssh.`id`)
      LEFT JOIN `lcl_ss_detail` ssd
        ON (ssh.`id` = ssd.`ss_header_id`)
    WHERE fn.`id` = new.`file_number_id`
    GROUP BY fn.`id` ;
        SELECT
      IF(COUNT(transaction_id)>0, FALSE, TRUE),
      `status`
    INTO
      mAccrualNotFound,
      mAccrualStatus
    FROM
      `transaction_ledger`
    WHERE `cost_id` = mCostId
      AND `transaction_type` = 'AC'
      AND `drcpt` = mDrNo;
    SELECT
      gl.`bluescreen_chargecode`,
      gl.`charge_code`,
      CONCAT_WS('-', (SELECT  sr.`rule_name` FROM `system_rules` sr WHERE sr.`rule_code` = 'CompanyCode'), gl.`gl_acct`, LPAD(IF(gl.`derive_yn` = 'Y', (SELECT IF(gl.`suffix_value` = 'B', IF(mShipmentType = 'LCLI', tm.`lcl_import_billing`, tm.`lcl_export_billing`), IF(gl.`suffix_value` = 'L', IF(mShipmentType = 'LCLI', tm.`lcl_import_loading`, tm.`lcl_export_loading`), IF(gl.`suffix_value` = 'D' AND mShipmentType = 'LCLE', tm.`lcl_export_dockreceipt`, tm.`terminal`))) FROM `terminal_gl_mapping` tm WHERE tm.`terminal` = mTerminal LIMIT 1), gl.`suffix_value`), 2, '00')) AS gl_account
    INTO
      mBsCostCode,
      mCostCode,
      mGlAccount
    FROM
      `gl_mapping` gl
    WHERE gl.`id` = mApGlMappingId ;
    IF (mAccrualNotFound) THEN
      INSERT INTO `lcl_booking_ac_trans` (
        `file_number_id`,
        `trans_type`,
        `trans_datetime`,
        `entry_type`,
        `reference_no`,
        `amount`,
        `entered_by_user_id`,
        `entered_datetime`,
        `modified_by_user_id`,
        `modified_datetime`
      )
      VALUES (
        mFileNumberId,
        'AC',
        NOW(),
        'A',
        '',
        mCostAmount,
        mUserId,
        NOW(),
        mUserId,
        NOW()
      );
      INSERT INTO `lcl_booking_ac_ta` (
        `lcl_booking_ac_trans_id`,
        `lcl_booking_ac_id`,
        `amount`,
        `entered_by_user_id`,
        `entered_datetime`,
        `modified_by_user_id`,
        `modified_datetime`
      )
      VALUES (
        (SELECT LAST_INSERT_ID()),
        mCostId,
        0.00,
        mUserId,
        NOW(),
        mUserId,
        NOW()
      );
      INSERT INTO `transaction_ledger` (
	  `cost_id`,
	  `cust_name`,
	  `cust_no`,
	  `invoice_number`,
	  `drcpt`,
	  `bill_ladding_no`,
	  `transaction_type`,
	  `status`,
	  `subledger_source_code`,
	  `shipment_type`,
	  `bluescreen_chargecode`,
	  `charge_code`,
	  `gl_account_number`,
	  `transaction_amt`,
	  `balance`,
	  `balance_in_process`,
	  `transaction_date`,
	  `sailing_date`,
	  `booking_no`,
	  `voyage_no`,
	  `container_no`,
	  `master_bl`,
	  `sub_house_bl`,
	  `originating_terminal`,
	  `destination`,
	  `ship_name`,
	  `ship_no`,
	  `cons_name`,
	  `cons_no`,
	  `fwd_name`,
	  `fwd_no`,
	  `third_pty_name`,
	  `third_pty_no`,
	  `agent_name`,
	  `agent_no`,
	  `billing_terminal`,
	  `terminal`,
	  `vessel_no`,
	  `bill_to`,
	  `ready_to_post`,
	  `currency_code`,
	  `manifest_flag`,
	  `created_on`,
	  `created_by`
      )
      VALUES (
	  mCostId,
	  mVendorName,
	  mVendorNo,
	  mInvoiceNo,
	  mDrNo,
	  mBlNo,
	  'AC',
	  'Open',
	  'ACC',
	  mShipmentType,
	  mBsCostCode,
	  mCostCode,
	  mGlAccount,
	  mCostAmount,
	  mCostAmount,
	  mCostAmount,
	  COALESCE(mEta, NOW()),
	  mEta,
	  mBookingNo,
	  mVoyageNo,
	  mContainerNo,
	  mMasterBl,
	  mSubhouseBl,
	  mOrigin,
	  mDestination,
	  mShipperName,
	  mShipperNo,
	  mConsigneeName,
	  mConsigneeNo,
	  mForwarderName,
	  mForwarderNo,
	  mThirdPartyName,
	  mThirdPartyNo,
	  mAgentName,
	  mAgentNo,
	  mTerminal,
	  mTerminal,
	  mVesselNo,
	  'Y',
	  'ON',
	  'USD',
	  'N',
	  NOW(),
	  mUserId
      );
    END IF;
  END IF;
END;
$$

DELIMITER ;


DELIMITER $$


DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBookingAc_update`$$

CREATE TRIGGER `after_LclBookingAc_update` AFTER UPDATE ON `lcl_booking_ac`
    FOR EACH ROW BEGIN
  DECLARE updatedValues TEXT DEFAULT '';
  DECLARE chargeValue BOOLEAN DEFAULT FALSE;
  DECLARE chageFlag BOOLEAN DEFAULT FALSE;
  DECLARE mFileNumberId BIGINT(20);
  DECLARE mShipmentType VARCHAR(4);
  DECLARE mBlNo VARCHAR(30);
  DECLARE mDrNo VARCHAR(7);
  DECLARE mBookingNo VARCHAR(20);
  DECLARE mVoyageNo VARCHAR(30);
  DECLARE mContainerNo VARCHAR(30);
  DECLARE mEta DATETIME;
  DECLARE mSsline VARCHAR(50);
  DECLARE mOrigin VARCHAR(100);
  DECLARE mDestination VARCHAR(100);
  DECLARE mShipperName VARCHAR(50);
  DECLARE mShipperNo VARCHAR(10);
  DECLARE mConsigneeName VARCHAR(50);
  DECLARE mConsigneeNo VARCHAR(10);
  DECLARE mForwarderName VARCHAR(50);
  DECLARE mForwarderNo VARCHAR(10);
  DECLARE mThirdPartyName VARCHAR(50);
  DECLARE mThirdPartyNo VARCHAR(10);
  DECLARE mAgentName VARCHAR(50);
  DECLARE mAgentNo VARCHAR(10);
  DECLARE mTerminal VARCHAR(5);
  DECLARE mVesselNo VARCHAR(20);
  DECLARE mMasterBl VARCHAR(50);
  DECLARE mSubhouseBl VARCHAR(50);
  DECLARE mApGlMappingId BIGINT(20);
  DECLARE mCostAmount DECIMAL(10,2);
  DECLARE mVendorName VARCHAR(50);
  DECLARE mVendorNo VARCHAR(10);
  DECLARE mInvoiceNo VARCHAR(50);
  DECLARE mCostId BIGINT(20);
  DECLARE mBsCostCode VARCHAR(5);
  DECLARE mCostCode VARCHAR(20);
  DECLARE mGlAccount VARCHAR(20);
  DECLARE mUserId INT(11);
  DECLARE mAccrualStatus VARCHAR(10);
  DECLARE mAccrualNotFound BOOLEAN DEFAULT FALSE;
  DECLARE mBookingType VARCHAR(2);
  DECLARE mBkPieceUnitId BIGINT(20);
  IF (new.ar_gl_mapping_id <> old.ar_gl_mapping_id) THEN
    SET updatedValues = concat_string(updatedValues, 'Charge Code', (SELECT charge_code FROM gl_mapping WHERE id = old.ar_gl_mapping_id), (SELECT charge_code FROM gl_mapping WHERE id = new.ar_gl_mapping_id)) ;
    SET chargeValue = TRUE ;
    SET chageFlag = TRUE ;
  END IF ;
  IF (new.ar_amount <> old.ar_amount) THEN
    SET updatedValues = concat_string(updatedValues, 'Charge Amount', old.ar_amount, new.ar_amount) ;
    SET chageFlag = TRUE ;
  END IF ;
  IF (new.ap_amount <> old.ap_amount) THEN
    SET updatedValues = concat_string(updatedValues, 'Cost Amount', old.ap_amount, new.ap_amount) ;
    SET chageFlag = TRUE ;
  END IF ;
   /* ************************Only For Lcl Exports when change the FLAT RATE****************************** */
  SET mBookingType = (SELECT booking_type FROM lcl_booking WHERE file_number_id = new.file_number_id);
  IF (mBookingType NOT IN('I','T')) THEN
     IF (new.rate_per_unit  <> old.rate_per_unit) THEN
         SET updatedValues = concat_string(updatedValues, 'Charge Flat Amount', old.rate_per_unit, new.rate_per_unit) ;
         SET chageFlag = TRUE ;
     END IF;
     IF (new.cost_flatrate_amount <> old.cost_flatrate_amount) THEN
         SET updatedValues = concat_string(updatedValues, 'Cost Flat Amount', old.cost_flatrate_amount, new.cost_flatrate_amount) ;
         SET chageFlag = TRUE ;
     END IF;
  END IF ;
   /* ************************************************************************************ */
  IF (new.adjustment_amount <> old.adjustment_amount) THEN
    SET updatedValues = concat_string(updatedValues, 'Adjustment Amount', old.adjustment_amount, new.adjustment_amount) ;
    SET chageFlag = TRUE ;
  END IF ;
  IF (new.adjustment_comments <> old.adjustment_comments) THEN
    SET updatedValues = concat_string(updatedValues, 'Adjustment Comments', old.adjustment_comments, new.adjustment_comments) ;
    SET chageFlag = TRUE ;
  END IF ;
  IF (new.rate_uom <> old.rate_uom) THEN
    SET updatedValues = concat_string(updatedValues, 'Rate UOM', old.rate_uom, new.rate_uom) ;
    SET chageFlag = TRUE ;
  END IF ;
  IF (new.rate_per_weight_unit <> old.rate_per_weight_unit) THEN
    SET updatedValues = concat_string(updatedValues, 'Weight', old.rate_per_weight_unit, new.rate_per_weight_unit) ;
    SET chageFlag = TRUE ;
  END IF ;
  IF (new.rate_per_volume_unit <> old.rate_per_volume_unit) THEN
    SET updatedValues = concat_string (updatedValues, 'Volume', old.rate_per_volume_unit, new.rate_per_volume_unit) ;
    SET chageFlag = TRUE ;
  END IF ;
  IF (new.rate_flat_minimum <> old.rate_flat_minimum) THEN
    SET updatedValues = concat_string(updatedValues, 'Minimum', old.rate_flat_minimum, new.rate_flat_minimum) ;
    SET chageFlag = TRUE ;
  END IF ;
  /* ************************Wnen Change the W/M Rate Cost in LCL Exports****************************** */
  IF (new.cost_weight <> old.cost_weight) THEN
    SET updatedValues = concat_string(updatedValues, 'Cost Weight', old.cost_weight, new.cost_weight) ;
    SET chageFlag = TRUE ;
  END IF ;
  IF (new.cost_measure <> old.cost_measure) THEN
    SET updatedValues = concat_string (updatedValues, 'Cost Volume', old.cost_measure, new.cost_measure) ;
    SET chageFlag = TRUE ;
  END IF ;
  IF (new.cost_minimum <> old.cost_minimum) THEN
    SET updatedValues = concat_string(updatedValues, 'Cost Minimum', old.cost_minimum, new.cost_minimum) ;
    SET chageFlag = TRUE ;
  END IF ;
  /* *************************************************************************************************** */
  IF (new.sp_acct_no <> old.sp_acct_no) THEN
    SET updatedValues = concat_string(updatedValues, 'Vendor Name', (SELECT acct_name FROM trading_partner WHERE acct_no = old.sp_acct_no), (SELECT acct_name FROM trading_partner WHERE acct_no = new.sp_acct_no));
    SET updatedValues = concat_string(updatedValues, 'Vendor Number', old.sp_acct_no, new.sp_acct_no);
    SET chageFlag = TRUE ;
  END IF ;
  IF (new.invoice_number <> old.invoice_number) THEN
    SET updatedValues = concat_string(updatedValues, 'Invoice Number', old.invoice_number, new.invoice_number) ;
  ELSEIF (new.invoice_number IS NOT NULL AND new.invoice_number <> '' AND old.invoice_number IS NULL) THEN
    SET updatedValues = concat_string(updatedValues, 'Invoice Number', '', new.invoice_number) ;
    SET chageFlag = TRUE ;
  END IF ;
  IF (updatedValues <> '') THEN
    IF (chageFlag = TRUE) THEN
      CALL update_lclbooking_moddate (OLD.file_number_id) ;
    END IF ;
    IF (chargeValue = TRUE) THEN
      SET updatedValues = CONCAT('UPDATED ->', updatedValues) ;
    ELSE
      SET updatedValues = CONCAT('UPDATED -> (Code -> ', (SELECT charge_code FROM gl_mapping WHERE id = old.ar_gl_mapping_id), ')', updatedValues) ;
    END IF ;
    INSERT INTO lcl_remarks (
      file_number_id,
      TYPE,
      remarks,
      entered_datetime,
      entered_by_user_id,
      modified_datetime,
      modified_by_user_id
    )
    VALUES (
      old.file_number_id,
      'DR-AutoNotes',
      updatedValues,
      NOW(),
      new.modified_by_user_id,
      NOW(),
      new.modified_by_user_id
    ) ;
  END IF ;
  IF (new.ap_amount <> 0.00 AND new.`file_number_id` IS NOT NULL AND (isNotEqual(new.ap_amount, old.ap_amount) OR isNotEqual(new.`sp_acct_no`, old.`sp_acct_no`) OR isNotEqual(new.`invoice_number`, old.`invoice_number`)) AND new.ap_gl_mapping_id IS NOT NULL AND new.deleted = FALSE) THEN
    SELECT
     lbpu.id
  INTO mBkPieceUnitId
  FROM
     `lcl_booking_piece` lbp
      JOIN `lcl_booking_piece_unit` lbpu
        ON (lbp.`id` = lbpu.`booking_piece_id`)
       JOIN `lcl_unit_ss` lus
        ON (lbpu.`lcl_unit_ss_id` = lus.id)
       JOIN `lcl_ss_header` lssh
        ON (lus.`ss_header_id` = lssh.`id`)
        WHERE lbp.`file_number_id` = new.`file_number_id` AND  lssh.service_type <> 'N'
        GROUP BY lbp.`file_number_id`;
    SELECT
      fn.`id`,
      IF(bk.`booking_type` = 'E', 'LCLE', 'LCLI') AS shipmentType,
      IF(bk.`booking_type` = 'E', CONCAT_WS('-',
      CONVERT((SELECT IF(t.unlocationcode1 <> '',RIGHT(t.unlocationcode1,3),t.trmnum) FROM terminal t WHERE t.trmnum=bk.billing_terminal),
       CHARACTER CHARSET utf8), CONVERT(
       (SELECT IF(u.bl_numbering = 'Y', RIGHT(`UnLocationGetCodeByID`(bk.`fd_id`),3),
       `UnLocationGetCodeByID` (bk.`fd_id`)) FROM un_location u WHERE u.id=bk.`fd_id`),
       CHARACTER CHARSET utf8), CONVERT(fn.`file_number`, CHARACTER CHARSET utf8)),
       CONCAT('IMP-', fn.`file_number`)) AS blNo,
      fn.`file_number` AS drNo,
      us.`sp_booking_no` AS bookingNo,
      CONCAT_WS('-', CONVERT(ssh.`billing_trmnum`, CHARACTER CHARSET utf8), `UnLocationGetCodeByID` (ssh.`origin_id`), `UnLocationGetCodeByID` (ssh.`destination_id`), ssh.`schedule_no`) AS voyageNo,
      (SELECT u.`unit_no` FROM `lcl_unit` u WHERE u.`id` = us.`unit_id` LIMIT 1) AS containerNo,
      IF(bk.`booking_type` = 'E',ssd.std,ssd.`sta`) AS eta,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = ssd.`sp_acct_no` LIMIT 1) AS ssline,
      `UnLocationGetNameStateCntryByID` (ssh.`origin_id`) AS origin,
      `UnLocationGetNameStateCntryByID` (ssh.`destination_id`) AS destination,
      bk.`ship_acct_no` AS shipperNo,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = bk.`ship_acct_no` LIMIT 1) AS shipperName,
      bk.`cons_acct_no` AS consigneeNo,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = bk.`cons_acct_no` LIMIT 1) AS consigneeName,
      bk.`fwd_acct_no` AS forwarderNo,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = bk.`fwd_acct_no` LIMIT 1) AS forwarderName,
      bk.`third_party_acct_no` AS thirdPartyNo,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = bk.`third_party_acct_no` LIMIT 1) AS thirdPartyName,
      bk.`agent_acct_no` AS agentNo,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = bk.`agent_acct_no` LIMIT 1) AS agentName,
      COALESCE(ssh.`billing_trmnum`, bk.`billing_terminal`) AS terminal,
      (SELECT `code` FROM `genericcode_dup` WHERE `codedesc` = ssd.`sp_reference_name` AND `codetypeid` = (SELECT `codetypeid` FROM `codetype` WHERE `description` = 'Vessel Codes' LIMIT 1) LIMIT 1) AS vesselNo,
      (SELECT `masterbl` FROM `lcl_unit_ss_manifest` WHERE `ss_header_id` = ssh.`id` AND `unit_id` = us.`unit_id` LIMIT 1) AS masterBl,
      (SELECT `sub_house_bl` FROM `lcl_booking_import` WHERE `file_number_id` = fn.`id` LIMIT 1) AS subhouseBl,
      new.`ap_gl_mapping_id` AS apGlMappingId,
      new.`ap_amount` AS costAmount,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = new.`sp_acct_no` LIMIT 1) AS vendorName,
      new.`sp_acct_no` AS vendorNo,
      new.`invoice_number` AS invoiceNo,
      new.`id` AS costId,
      new.`modified_by_user_id` AS userId
    INTO
      mFileNumberId,
      mShipmentType,
      mBlNo,
      mDrNo,
      mBookingNo,
      mVoyageNo,
      mContainerNo,
      mEta,
      mSsline,
      mOrigin,
      mDestination,
      mShipperNo,
      mShipperName,
      mConsigneeNo,
      mConsigneeName,
      mForwarderNo,
      mForwarderName,
      mThirdPartyNo,
      mThirdPartyName,
      mAgentNo,
      mAgentName,
      mTerminal,
      mVesselNo,
      mMasterBl,
      mSubhouseBl,
      mApGlMappingId,
      mCostAmount,
      mVendorName,
      mVendorNo,
      mInvoiceNo,
      mCostId,
      mUserId
    FROM
      `lcl_file_number` fn
      JOIN `lcl_booking` bk
        ON (fn.`id` = bk.`file_number_id`)
      JOIN `lcl_booking_piece` bp
        ON (fn.`id` = bp.`file_number_id`)
      LEFT JOIN `lcl_booking_piece_unit` bpu
        ON (bp.`id` = bpu.`booking_piece_id` AND bpu.id=mBkPieceUnitId)
      LEFT JOIN `lcl_unit_ss` us
        ON (bpu.`lcl_unit_ss_id` = us.`id`)
      LEFT JOIN `lcl_ss_header` ssh
        ON (us.`ss_header_id` = ssh.`id`)
      LEFT JOIN `lcl_ss_detail` ssd
        ON (ssh.`id` = ssd.`ss_header_id`)
    WHERE fn.`id` = new.`file_number_id`
    GROUP BY fn.`id`;
        SELECT
      IF(COUNT(transaction_id)>0, FALSE, TRUE),
      `status`
    INTO
      mAccrualNotFound,
      mAccrualStatus
    FROM
      `transaction_ledger`
    WHERE `cost_id` = mCostId
      AND `transaction_type` = 'AC'
      AND `drcpt` = mDrNo;
    SELECT
      gl.`bluescreen_chargecode`,
      gl.`charge_code`,
      CONCAT_WS('-', (SELECT  sr.`rule_name` FROM `system_rules` sr WHERE sr.`rule_code` = 'CompanyCode'), gl.`gl_acct`, LPAD(COALESCE((SELECT IF(gl.`derive_yn` = 'Y' AND gl.`suffix_value` = 'B', IF(mShipmentType = 'LCLI', tm.`lcl_import_billing`, tm.`lcl_export_billing`), IF(gl.`derive_yn` = 'Y' AND gl.`suffix_value` = 'L', IF(mShipmentType = 'LCLI', tm.`lcl_import_loading`, tm.`lcl_export_loading`), IF(gl.`derive_yn` = 'Y' AND gl.`suffix_value` = 'D' AND mShipmentType = 'LCLE', tm.`lcl_export_dockreceipt`, tm.`terminal`))) FROM `terminal_gl_mapping` tm WHERE tm.`terminal` = mTerminal LIMIT 1), gl.`suffix_value`), 2, '00')) AS gl_account
    INTO
      mBsCostCode,
      mCostCode,
      mGlAccount
    FROM
      `gl_mapping` gl
    WHERE gl.`id` = mApGlMappingId ;
    IF (mAccrualNotFound) THEN
      INSERT INTO `lcl_booking_ac_trans` (
        `file_number_id`,
        `trans_type`,
        `trans_datetime`,
        `entry_type`,
        `reference_no`,
        `amount`,
        `entered_by_user_id`,
        `entered_datetime`,
        `modified_by_user_id`,
        `modified_datetime`
      )
      VALUES (
        mFileNumberId,
        'AC',
        NOW(),
        'A',
        '',
        mCostAmount,
        mUserId,
        NOW(),
        mUserId,
        NOW()
      );
      INSERT INTO `lcl_booking_ac_ta` (
        `lcl_booking_ac_trans_id`,
        `lcl_booking_ac_id`,
        `amount`,
        `entered_by_user_id`,
        `entered_datetime`,
        `modified_by_user_id`,
        `modified_datetime`
      )
      VALUES (
        (SELECT LAST_INSERT_ID()),
        mCostId,
        0.00,
        mUserId,
        NOW(),
        mUserId,
        NOW()
      );
      INSERT INTO `transaction_ledger` (
	  `cost_id`,
	  `cust_name`,
	  `cust_no`,
	  `invoice_number`,
	  `drcpt`,
	  `bill_ladding_no`,
	  `transaction_type`,
	  `status`,
	  `subledger_source_code`,
	  `shipment_type`,
	  `bluescreen_chargecode`,
	  `charge_code`,
	  `gl_account_number`,
	  `transaction_amt`,
	  `balance`,
	  `balance_in_process`,
	  `transaction_date`,
	  `sailing_date`,
	  `booking_no`,
	  `voyage_no`,
	  `container_no`,
	  `master_bl`,
	  `sub_house_bl`,
	  `originating_terminal`,
	  `destination`,
	  `ship_name`,
	  `ship_no`,
	  `cons_name`,
	  `cons_no`,
	  `fwd_name`,
	  `fwd_no`,
	  `third_pty_name`,
	  `third_pty_no`,
	  `agent_name`,
	  `agent_no`,
	  `billing_terminal`,
	  `terminal`,
	  `vessel_no`,
	  `bill_to`,
	  `ready_to_post`,
	  `currency_code`,
	  `manifest_flag`,
	  `created_on`,
	  `created_by`
      )
      VALUES (
	  mCostId,
	  mVendorName,
	  mVendorNo,
	  mInvoiceNo,
	  mDrNo,
	  mBlNo,
	  'AC',
	  'Open',
	  'ACC',
	  mShipmentType,
	  mBsCostCode,
	  mCostCode,
	  mGlAccount,
	  mCostAmount,
	  mCostAmount,
	  mCostAmount,
	  COALESCE(mEta, NOW()),
	  mEta,
	  mBookingNo,
	  mVoyageNo,
	  mContainerNo,
	  mMasterBl,
	  mSubhouseBl,
	  mOrigin,
	  mDestination,
	  mShipperName,
	  mShipperNo,
	  mConsigneeName,
	  mConsigneeNo,
	  mForwarderName,
	  mForwarderNo,
	  mThirdPartyName,
	  mThirdPartyNo,
	  mAgentName,
	  mAgentNo,
	  mTerminal,
	  mTerminal,
	  mVesselNo,
	  'Y',
	  'ON',
	  'USD',
	  'N',
	  NOW(),
	  mUserId
      );
    ELSEIF (mAccrualStatus IN ('Open', 'Dispute', 'EDI Dispute')) THEN
      UPDATE
	  `transaction_ledger`
      SET
	  `cust_name` = mVendorName,
	  `cust_no` = mVendorNo,
	  `invoice_number` = mInvoiceNo,
	  `bill_ladding_no` = mBlNo,
	  `bluescreen_chargecode` = mBsCostCode,
	  `charge_code` = mCostCode,
	  `gl_account_number` = mGlAccount,
	  `transaction_amt` = mCostAmount,
	  `balance` = mCostAmount,
	  `balance_in_process` = mCostAmount,
	  `transaction_date` = COALESCE(mEta, NOW()),
	  `sailing_date` = mEta,
	  `booking_no` = mBookingNo,
	  `voyage_no` = mVoyageNo,
	  `container_no` = mContainerNo,
	  `master_bl` = mMasterBl,
	  `sub_house_bl` = mSubhouseBl,
	  `originating_terminal` = mOrigin,
	  `destination` = mDestination,
	  `ship_name` = mShipperName,
	  `ship_no` = mShipperNo,
	  `cons_name` = mConsigneeName,
	  `cons_no` = mConsigneeNo,
	  `fwd_name` = mForwarderName,
	  `fwd_no` = mForwarderNo,
	  `third_pty_name` = mThirdPartyName,
	  `third_pty_no` = mThirdPartyNo,
	  `agent_name` = mAgentName,
	  `agent_no` = mAgentNo,
	  `billing_terminal` = mTerminal,
	  `terminal` = mTerminal,
	  `vessel_no` = mVesselNo,
	  `updated_on` = NOW(),
	  `updated_by` = mUserId
      WHERE `cost_id` = mCostId
	  AND `transaction_type` = 'AC'
	  AND `drcpt` = mDrNo;
    END IF;
  END IF;
END;
$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_lclBookingDispo_insert`$$

CREATE TRIGGER `after_lclBookingDispo_insert` AFTER INSERT ON `lcl_booking_dispo`
    FOR EACH ROW BEGIN
    DECLARE insert_values TEXT ;
    SELECT file_number_id,disposition_id,IFNULL(un_location_id,'') INTO @fileId,@oldDispoId,@oldUnLocId FROM lcl_booking_dispo WHERE id=(SELECT id FROM lcl_booking_dispo WHERE file_number_id=new.file_number_id ORDER BY id DESC LIMIT 1,1);
    IF new.un_location_id!='' AND new.un_location_id!=@oldUnLocId THEN
	SET insert_values = concat_insert_values(insert_values,' Current Location',(SELECT CONCAT(UPPER(un.un_loc_name),',',IFNULL(UPPER(p.statecode),'')) FROM un_location un LEFT JOIN ports p ON un.un_loc_code=p.unlocationcode WHERE un.id=new.un_location_id));
        IF (insert_values!='') THEN
            INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
            VALUES(new.file_number_id,'DR-AutoNotes',insert_values,NOW(),new.entered_by_user_id,NOW(),new.modified_by_user_id);
        END IF;
    END IF;
    SET insert_values=NULL;
    IF new.disposition_id!='' AND new.disposition_id!=@oldDispoId AND @fileId=new.file_number_id THEN
            SET insert_values = concat_string(insert_values,' Disposition',(SELECT elite_code FROM disposition WHERE id=@oldDispoId),(SELECT elite_code FROM disposition WHERE id=new.disposition_id));
        ELSE IF @fileId='' OR new.disposition_id!=@oldDispoId THEN
            SET insert_values = concat_insert_values(insert_values,' Disposition',(SELECT elite_code FROM disposition WHERE id=new.disposition_id));
        END IF;
    END IF;
    IF (insert_values!='') THEN
        CALL update_lclbooking_moddate(new.file_number_id);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
         VALUES(new.file_number_id,'DR-AutoNotes',insert_values,NOW(),new.entered_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;


DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBookingHazmat_insert`$$

CREATE TRIGGER `after_LclBookingHazmat_insert` AFTER INSERT ON `lcl_booking_hazmat`
    FOR EACH ROW BEGIN
     DECLARE inserted_values TEXT ;
     SET @contact_name =(SELECT contact_name FROM lcl_contact WHERE id=new.emergency_contact_id);
     SET @phone1 =(SELECT phone1 FROM lcl_contact WHERE id=new.emergency_contact_id);
      IF NEW.un_hazmat_no  IS NOT NULL THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'UN#',NEW.un_hazmat_no));
     END IF;
     IF NEW.proper_shipping_name IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Proper Shipping Name',NEW.proper_shipping_name));
     END IF;
      IF NEW.imo_pri_class_code IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Class',NEW.imo_pri_class_code));
     END IF;
      IF NEW.packing_group_code IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Packaging Group Code',NEW.packing_group_code));
     END IF;
      IF NEW.flash_point IS NOT NULL THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Flash point',NEW.flash_point));
     END IF;
       IF NEW.outer_pkg_no_pieces IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Outer Packaging Pieces',NEW.outer_pkg_no_pieces));
     END IF;
       IF NEW.outer_pkg_type IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Outer Packaging Type',NEW.outer_pkg_type));
     END IF;
       IF NEW.outer_pkg_composition IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Outer Pkg Composition',NEW.outer_pkg_composition));
     END IF;
      IF NEW.total_net_weight IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Total Net Weight/Kgs',NEW.total_net_weight));
     END IF;
      IF NEW.liquid_volume IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Total Volume',NEW.liquid_volume));
     END IF;
       IF NEW.total_gross_weight IS NOT NULL THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Total Gross Weight/Kgs',NEW.total_gross_weight));
     END IF;
     IF @contact_name IS NOT NULL THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Emergency Contact',@contact_name));
     END IF;
     IF @phone1 IS NOT NULL THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Emergency Phone No',@phone1));
     END IF;
     IF NEW.ems_code IS NOT NULL THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'EMS code',NEW.ems_code));
     END IF;
      IF NEW.technical_name IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Tech Chemical Name',NEW.technical_name));
     END IF;
     IF NEW.imo_pri_sub_class_code IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'IMO Subsidary Class',NEW.imo_pri_sub_class_code));
     END IF;
     IF NEW.imo_sec_sub_class_code IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'IMO Secondary Class',NEW.imo_sec_sub_class_code));
     END IF;
      IF NEW.inner_pkg_no_pieces IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Inner Packing Pieces',NEW.inner_pkg_no_pieces));
     END IF;
      IF NEW.inner_pkg_type IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Inner Packaging Type',NEW.inner_pkg_type));
     END IF;
      IF NEW.inner_pkg_composition IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Inner Pkg Composition',NEW.inner_pkg_composition));
     END IF;
     IF NEW.inner_pkg_uom IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Inner Pkg UOM',NEW.inner_pkg_uom));
     END IF;
     IF NEW.inner_pkg_nwt_piece IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Inner Pkg Wt/Vol Per Piece',NEW.inner_pkg_nwt_piece));
     END IF;
      IF NEW.reportable_quantity IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Reportable Quantity',IF(NEW.reportable_quantity=0,'No','Yes')));
     END IF;
      IF NEW.marine_pollutant IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Marine pollutant',IF(NEW.marine_pollutant=0,'No','Yes')));
     END IF;
      IF NEW.excepted_quantity IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Excepted Quantity',IF(NEW.excepted_quantity=0,'No','Yes')));
     END IF;
       IF NEW.limited_quantity IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Limited Quantity',IF(NEW.limited_quantity=0,'No','Yes')));
     END IF;
       IF NEW.residue IS NOT NULL THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Residue',IF(NEW.residue=0,'No','Yes')));
     END IF;
     IF NEW.inhalation_hazard IS NOT NULL  THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Inhalation Hazard',IF(NEW.inhalation_hazard=0,'No','Yes')));
     END IF;
     IF inserted_values IS NOT NULL THEN
     SET inserted_values=CONCAT('INSERTED ->',inserted_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(NEW.file_number_id,'DR-AutoNotes',inserted_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;


DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBookingHazmat_update`$$

CREATE TRIGGER `after_LclBookingHazmat_update` AFTER UPDATE ON `lcl_booking_hazmat`
    FOR EACH ROW BEGIN
     DECLARE updated_values TEXT ;
      IF isNotEqual(OLD.un_hazmat_no,NEW.un_hazmat_no)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'UN#',OLD.un_hazmat_no,NEW.un_hazmat_no));
     END IF;
     IF isNotEqual(OLD.proper_shipping_name,NEW.proper_shipping_name)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Proper Shipping Name',OLD.proper_shipping_name,NEW.proper_shipping_name));
     END IF;
      IF isNotEqual(OLD.imo_pri_class_code,NEW.imo_pri_class_code)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Class',OLD.imo_pri_class_code,NEW.imo_pri_class_code));
     END IF;
      IF isNotEqual(OLD.packing_group_code,NEW.packing_group_code)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Packaging Group Code',OLD.packing_group_code,NEW.packing_group_code));
     END IF;
      IF isNotEqual(OLD.flash_point,NEW.flash_point)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Flash point',OLD.flash_point,NEW.flash_point));
     END IF;
       IF isNotEqual(OLD.outer_pkg_no_pieces,NEW.outer_pkg_no_pieces)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Outer Packaging Pieces',OLD.outer_pkg_no_pieces,NEW.outer_pkg_no_pieces));
     END IF;
       IF isNotEqual(OLD.outer_pkg_type,NEW.outer_pkg_type)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Outer Packaging Type',OLD.outer_pkg_type,NEW.outer_pkg_type));
     END IF;
       IF isNotEqual(OLD.outer_pkg_composition,NEW.outer_pkg_composition)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Outer Pkg Composition',OLD.outer_pkg_composition,NEW.outer_pkg_composition));
     END IF;
      IF isNotEqual(OLD.total_net_weight,NEW.total_net_weight)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Total Net Weight/Kgs',OLD.total_net_weight,NEW.total_net_weight));
     END IF;
      IF isNotEqual(OLD.liquid_volume,NEW.liquid_volume)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Total Volume',OLD.liquid_volume,NEW.liquid_volume));
     END IF;
       IF isNotEqual(OLD.total_gross_weight,NEW.total_gross_weight)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Total Gross Weight/Kgs',OLD.total_gross_weight,NEW.total_gross_weight));
     END IF;
     IF isNotEqual(OLD.ems_code,NEW.ems_code)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'EMS Code',OLD.ems_code,NEW.ems_code));
     END IF;
      IF isNotEqual(OLD.technical_name,NEW.technical_name)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Tech Chemical Name',OLD.technical_name,NEW.technical_name));
     END IF;
     IF isNotEqual(OLD.imo_pri_sub_class_code,NEW.imo_pri_sub_class_code)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'IMO Subsidary Class',OLD.imo_pri_sub_class_code,NEW.imo_pri_sub_class_code));
     END IF;
     IF isNotEqual(OLD.imo_sec_sub_class_code,NEW.imo_sec_sub_class_code)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'IMO Secondary Class',OLD.imo_sec_sub_class_code,NEW.imo_sec_sub_class_code));
     END IF;
      IF isNotEqual(OLD.inner_pkg_no_pieces,NEW.inner_pkg_no_pieces)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Inner Packing Pieces',OLD.inner_pkg_no_pieces,NEW.inner_pkg_no_pieces));
     END IF;
      IF isNotEqual(OLD.inner_pkg_type,NEW.inner_pkg_type)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Inner Packaging Type',OLD.inner_pkg_type,NEW.inner_pkg_type));
     END IF;
      IF isNotEqual(OLD.inner_pkg_composition,NEW.inner_pkg_composition)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Inner Pkg Composition',OLD.inner_pkg_composition,NEW.inner_pkg_composition));
     END IF;
     IF isNotEqual(OLD.inner_pkg_uom,NEW.inner_pkg_uom)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Inner Pkg UOM',OLD.inner_pkg_uom,NEW.inner_pkg_uom));
     END IF;
     IF isNotEqual(OLD.inner_pkg_nwt_piece,NEW.inner_pkg_nwt_piece)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Inner Pkg Wt/Vol Per Piece',OLD.inner_pkg_nwt_piece,NEW.inner_pkg_nwt_piece));
     END IF;
      IF isNotEqual(OLD.reportable_quantity,NEW.reportable_quantity)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Reportable Quantity',IF(old.reportable_quantity=0,'No','Yes'),IF(new.reportable_quantity=0,'No','Yes')));
     END IF;
      IF isNotEqual(OLD.marine_pollutant,NEW.marine_pollutant)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Marine pollutant',IF(old.marine_pollutant=0,'No','Yes'),IF(new.marine_pollutant=0,'No','Yes')));
     END IF;
      IF isNotEqual(OLD.excepted_quantity,NEW.excepted_quantity)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Excepted Quantity',IF(old.excepted_quantity=0,'No','Yes'),IF(new.excepted_quantity=0,'No','Yes')));
     END IF;
       IF isNotEqual(OLD.limited_quantity,NEW.limited_quantity)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Limited Quantity',IF(old.limited_quantity=0,'No','Yes'),IF(new.limited_quantity=0,'No','Yes')));
     END IF;
       IF isNotEqual(OLD.residue,NEW.residue)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Residue',IF(old.residue=0,'No','Yes'),IF(new.residue=0,'No','Yes')));
     END IF;
     IF isNotEqual(OLD.inhalation_hazard,NEW.inhalation_hazard)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Inhalation Hazard',IF(old.inhalation_hazard=0,'No','Yes'),IF(new.inhalation_hazard=0,'No','Yes')));
     END IF;
     IF updated_values IS NOT NULL THEN
    SET updated_values=CONCAT('UPDATED ->',updated_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(OLD.file_number_id,'DR-AutoNotes',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;


DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBookingImport_update`$$

CREATE TRIGGER `after_LclBookingImport_update` AFTER UPDATE ON `lcl_booking_import`
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT ;
    DECLARE eta_at_fd TEXT ;
   IF new.transshipment!=old.transshipment THEN
       SET updated_values=(SELECT concat_string(updated_values,'Transshipment',IF(old.transshipment=0,'No','Yes'),IF(new.transshipment=0,'No','Yes')));
    END IF;
   IF ((OLD.picked_up_datetime IS NULL AND NEW.picked_up_datetime IS NOT NULL) OR NEW.picked_up_datetime != OLD.picked_up_datetime OR (OLD.picked_up_datetime IS NOT NULL AND NEW.picked_up_datetime IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'PickUp Date',IFNULL(DATE_FORMAT(old.picked_up_datetime,'%d-%b-%Y'),''),IFNULL(DATE_FORMAT(new.picked_up_datetime,'%d-%b-%Y'),'')));
    END IF;
   IF ((OLD.go_datetime IS NULL AND NEW.go_datetime IS NOT NULL) OR NEW.go_datetime != OLD.go_datetime OR (OLD.go_datetime IS NOT NULL AND NEW.go_datetime IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'G/O Date',IFNULL(DATE_FORMAT(old.go_datetime,'%d-%b-%Y'),''),IFNULL(DATE_FORMAT(new.go_datetime,'%d-%b-%Y'),'')));
    END IF;
   IF ((OLD.fd_eta IS NULL AND NEW.fd_eta IS NOT NULL) OR NEW.fd_eta != OLD.fd_eta OR (OLD.fd_eta IS NOT NULL AND NEW.fd_eta IS NULL)) THEN
       SET eta_at_fd=(SELECT concat_string(eta_at_fd,'ETA at FD',IFNULL(DATE_FORMAT(old.fd_eta,'%d-%b-%Y'),''),IFNULL(DATE_FORMAT(new.fd_eta,'%d-%b-%Y'),'')));
    END IF;
   IF ((OLD.sub_house_bl IS NULL AND NEW.sub_house_bl IS NOT NULL) OR NEW.sub_house_bl != OLD.sub_house_bl OR (OLD.sub_house_bl IS NOT NULL AND NEW.sub_house_bl IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'Sub House BL',IFNULL(old.sub_house_bl,''),IFNULL(new.sub_house_bl,'')));
   END IF;
    IF ((OLD.customs_entry_no IS NULL AND NEW.customs_entry_no IS NOT NULL AND NEW.customs_entry_no !='') OR NEW.customs_entry_no != OLD.customs_entry_no OR (OLD.customs_entry_no IS NOT NULL AND OLD.customs_entry_no !='' AND NEW.customs_entry_no IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'Entry#',IFNULL(old.customs_entry_no,''),IFNULL(new.customs_entry_no,'')));
   END IF;
   IF new.declared_value_estimated!=old.declared_value_estimated THEN
       SET updated_values=(SELECT concat_string(updated_values,'Value Estimated',IF(old.declared_value_estimated=0,'No','Yes'),IF(new.declared_value_estimated=0,'No','Yes')));
   END IF;
   IF new.declared_weight_estimated!=old.declared_weight_estimated THEN
       SET updated_values=(SELECT concat_string(updated_values,'Weight Estimated',IF(old.declared_weight_estimated=0,'No','Yes'),IF(new.declared_weight_estimated=0,'No','Yes')));
   END IF;
   IF ((OLD.inbond_via IS NULL AND NEW.inbond_via IS NOT NULL) OR NEW.inbond_via != OLD.inbond_via OR (OLD.inbond_via IS NOT NULL AND NEW.inbond_via IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'Inbond Via',IFNULL(old.inbond_via,''),IFNULL(new.inbond_via,'')));
   END IF;
   IF ((OLD.it_class IS NULL AND NEW.it_class IS NOT NULL) OR NEW.it_class != OLD.it_class OR (OLD.it_class IS NOT NULL AND NEW.it_class IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'IT Class',IFNULL(old.it_class,''),IFNULL(new.it_class,'')));
   END IF;
   IF ((OLD.customs_entry_class IS NULL AND NEW.customs_entry_class IS NOT NULL) OR NEW.customs_entry_class != OLD.customs_entry_class OR (OLD.customs_entry_class IS NOT NULL AND NEW.customs_entry_class IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'Class of Entry',IFNULL(old.customs_entry_class,''),IFNULL(new.customs_entry_class,'')));
   END IF;
   IF new.elite_ipi_loaded !=old.elite_ipi_loaded THEN
       SET updated_values=(SELECT concat_string(updated_values,'Loaded on IPI Container',IF(old.elite_ipi_loaded=0,'No','Yes'),IF(new.elite_ipi_loaded=0,'No','Yes')));
       END IF;
   IF isNotEqual(OLD.picked_up_by,NEW.picked_up_by) THEN
       SET updated_values=(`concat_string`(updated_values,'Picked By',OLD.picked_up_by,NEW.picked_up_by));
       END IF;
   IF ((OLD.door_delivery_eta IS NULL AND NEW.door_delivery_eta IS NOT NULL) OR NEW.door_delivery_eta != OLD.door_delivery_eta OR (OLD.door_delivery_eta IS NOT NULL AND NEW.door_delivery_eta IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'Door Delivery ETA',IFNULL(DATE_FORMAT(old.door_delivery_eta,'%d-%b-%Y'),''),IFNULL(DATE_FORMAT(new.door_delivery_eta,'%d-%b-%Y'),'')));
    END IF;
    IF new.express_release!=old.express_release THEN
      SET updated_values=(SELECT concat_string(updated_values,'Express Release',IF(old.express_release=0,'No','Yes'),IF(new.express_release=0,'No','Yes')));
    END IF;
    IF updated_values IS NOT NULL THEN
      SET updated_values=CONCAT(updated_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(OLD.file_number_id,'DR-AutoNotes',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    IF eta_at_fd IS NOT NULL THEN
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(OLD.file_number_id,'DR-AutoNotes',eta_at_fd,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
  END;
$$

DELIMITER ;


DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBookingPad_insert`$$

CREATE TRIGGER `after_LclBookingPad_insert` AFTER INSERT ON `lcl_booking_pad`
    FOR EACH ROW BEGIN
    DECLARE insert_values TEXT;
      IF NEW.pickup_city IS NOT NULL THEN
     SET @bookingType =(SELECT booking_type FROM lcl_booking  WHERE file_number_id=NEW.file_number_id LIMIT 1);
     SET @pickValuess=IF(@bookingType='E','Door Origin/City/Zip','Door Dest/City/Zip');
       SET insert_values = concat_insert_values(insert_values,@pickValuess,new.pickup_city);
    END IF;
    IF NEW.issuing_terminal IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,' Issued By',(SELECT CONCAT(UPPER(login_name),'/',terminal_id) FROM user_details WHERE user_id=NEW.entered_by_user_id));
    END IF;
    IF NEW.pickup_contact_id IS NOT NULL THEN
       SELECT company_name,contact_name,city,address,zip,phone1,fax1,email1
       INTO @compName,@contName,@city,@address,@zip,@phone,@fax1,@email
       FROM lcl_contact WHERE id=NEW.pickup_contact_id;
       IF @city IS NOT NULL THEN
		SET insert_values = concat_insert_values(insert_values,', City',@city);
       END IF;
       IF @compName IS NOT NULL THEN
		SET insert_values = concat_insert_values(insert_values,', Ship To',@compName);
       END IF;
       IF @contName IS NOT NULL THEN
		SET insert_values = concat_insert_values(insert_values,', Contact Name',@contName);
       END IF;
       IF @address IS NOT NULL THEN
		SET insert_values = concat_insert_values(insert_values,', Address',@address);
       END IF;
       IF @zip IS NOT NULL THEN
		SET insert_values = concat_insert_values(insert_values,', Zip',@zip);
       END IF;
       IF @phone IS NOT NULL THEN
		SET insert_values = concat_insert_values(insert_values,', Phone',@phone);
       END IF;
       IF @fax IS NOT NULL THEN
		SET insert_values = concat_insert_values(insert_values,', Fax',@fax);
       END IF;
       IF @email IS NOT NULL THEN
		SET insert_values = concat_insert_values(insert_values,', Email',@email);
       END IF;
    END IF;
    IF NEW.pickup_hours IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,', Receiving Hours',NEW.pickup_hours);
    END IF;
    IF NEW.delivery_ready_date IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,', Ready Date',NEW.delivery_ready_date);
    END IF;
    IF NEW.last_free_date IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,', Last Free Day',NEW.last_free_date);
    END IF;
    IF NEW.scac IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,', Scac Code',NEW.scac);
    END IF;
    IF NEW.pickup_reference_no IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,', PRO#',NEW.pickup_reference_no);
    END IF;
    IF NEW.delivery_contact_id IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,', WhseName',(SELECT company_name FROM lcl_contact WHERE id=NEW.delivery_contact_id));
       SET insert_values = concat_insert_values(insert_values,', WhseAddress',(SELECT address FROM lcl_contact WHERE id=NEW.delivery_contact_id));
       SET insert_values = concat_insert_values(insert_values,', WhseCity/State/Zip',(SELECT CONCAT(city,'/',state,'/',zip) FROM lcl_contact WHERE id=NEW.delivery_contact_id));
       SET insert_values = concat_insert_values(insert_values,', WhsePhone',(SELECT phone1 FROM lcl_contact WHERE id=NEW.delivery_contact_id));
    END IF;
    IF NEW.delivery_instructions IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,', Instructions',NEW.delivery_instructions);
    END IF;
    IF NEW.pickup_instructions IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,', Bill To',NEW.pickup_instructions);
    END IF;
    IF NEW.commodity_desc IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,', Commodity',NEW.commodity_desc);
    END IF;
    IF NEW.terms_of_service IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,', TOS',NEW.terms_of_service);
    END IF;
    IF (insert_values IS NOT NULL) THEN
        SET insert_values=CONCAT('INSERTED ->',insert_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(NEW.file_number_id,'DR-AutoNotes',insert_values,NOW(),NEW.entered_by_user_id,NOW(),NEW.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;


DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBookingPad_update`$$

CREATE TRIGGER `after_LclBookingPad_update` AFTER UPDATE ON `lcl_booking_pad`
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT;
     IF (isNotEqual(OLD.pickup_city , NEW.pickup_city)) THEN
     SET @bookingType =(SELECT booking_type FROM lcl_booking  WHERE file_number_id=OLD.file_number_id LIMIT 1);
     SET @pickValuess=IF(@bookingType='E','Door Origin/City/Zip','Door Dest/City/Zip');
       SET updated_values = (SELECT concat_string(updated_values,@pickValuess,OLD.pickup_city,NEW.pickup_city));
    END IF;
    IF (isNotEqual(OLD.issuing_terminal, NEW.issuing_terminal))THEN
       SET updated_values = (SELECT concat_string(updated_values,'Issued By',(SELECT CONCAT(UPPER(login_name),'/',terminal_id) FROM user_details WHERE user_id=OLD.entered_by_user_id),(SELECT CONCAT(UPPER(login_name),'/',terminal_id) FROM user_details WHERE user_id=NEW.entered_by_user_id)));
    END IF;
    IF (isNotEqual(OLD.delivery_ready_date, NEW.delivery_ready_date)) THEN
       SET updated_values = (SELECT concat_string(updated_values,'Ready Date',OLD.delivery_ready_date,NEW.delivery_ready_date));
    END IF;
    IF (isNotEqual(OLD.last_free_date, NEW.last_free_date)) THEN
       SET updated_values = (SELECT concat_string(updated_values,'Last Free Day',OLD.last_free_date,NEW.last_free_date));
    END IF;
    IF (isNotEqual(OLD.scac, NEW.scac)) THEN
       SET updated_values = (SELECT concat_string(updated_values,'Scac Code',OLD.scac,NEW.scac));
    END IF;
     IF (isNotEqual(OLD.pickup_hours, NEW.pickup_hours)) THEN
       SET updated_values = (SELECT concat_string(updated_values,'Receiving Hours',OLD.pickup_hours,NEW.pickup_hours));
    END IF;
    IF (isNotEqual(OLD.pickup_reference_no, NEW.pickup_reference_no)) THEN
       SET updated_values = (SELECT concat_string(updated_values,'PRO#',OLD.pickup_reference_no,NEW.pickup_reference_no));
    END IF;
    IF (isNotEqual(OLD.pickup_instructions ,  NEW.delivery_instructions)) THEN
       SET updated_values = (SELECT concat_string(updated_values,'Instructions',OLD.delivery_instructions,NEW.delivery_instructions));
    END IF;
     IF (isNotEqual(OLD.pickup_instructions ,  NEW.pickup_instructions)) THEN
       SET updated_values = (SELECT concat_string(updated_values,'Bill To',OLD.pickup_instructions,NEW.pickup_instructions));
    END IF;
    IF (isNotEqual(OLD.commodity_desc , NEW.commodity_desc))  THEN
       SET updated_values = (SELECT concat_string(updated_values,'Commodity',OLD.commodity_desc,NEW.commodity_desc));
    END IF;
    IF (isNotEqual(OLD.terms_of_service , NEW.terms_of_service)) THEN
       SET updated_values = (SELECT concat_string(updated_values,'TOS',OLD.terms_of_service,NEW.terms_of_service));
    END IF;
    IF updated_values IS NOT NULL THEN
        SET updated_values=CONCAT('UPDATED ->',updated_values);
	     INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
	     VALUES(OLD.file_number_id,'DR-AutoNotes',updated_values,NOW(),NEW.modified_by_user_id,NOW(),NEW.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;


DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBookingPiece_insert`$$

CREATE TRIGGER `after_LclBookingPiece_insert` AFTER INSERT ON `lcl_booking_piece`
    FOR EACH ROW BEGIN
    DECLARE inserted_values TEXT;
	IF (NEW.commodity_type_id IS NOT NULL) THEN
		SET inserted_values=(SELECT concat_insert_values(inserted_values,' Commodity',(SELECT  CONCAT(desc_en,' (',CODE,')') FROM commodity_type WHERE id=new.commodity_type_id LIMIT 1)));
	END IF;
	IF (NEW.booked_package_type_id IS NOT NULL) THEN
		SET inserted_values=(SELECT concat_insert_values(inserted_values,' Package',(SELECT description FROM package_type WHERE id=new.booked_package_type_id LIMIT 1)));
	END IF;
	IF (NEW.booked_piece_count IS NOT NULL) THEN
		SET inserted_values=(SELECT concat_insert_values(inserted_values,' Piece Count',new.booked_piece_count));
	END IF;
	IF (NEW.booked_weight_imperial IS NOT NULL) THEN
		SET inserted_values=(SELECT concat_insert_values(inserted_values,' Weight  LBS',new.booked_weight_imperial));
	END IF;
	IF (NEW.booked_weight_metric IS NOT NULL) THEN
		SET inserted_values=(SELECT concat_insert_values(inserted_values,' Weight  KGS',new.booked_weight_metric));
	END IF;
	IF (NEW.booked_volume_imperial IS NOT NULL) THEN
		SET inserted_values=(SELECT concat_insert_values(inserted_values,' Volume  CFT',new.booked_volume_imperial));
	END IF;
	IF (NEW.booked_volume_metric IS NOT NULL) THEN
		SET inserted_values=(SELECT concat_insert_values(inserted_values,' Volume  CBM',new.booked_volume_metric));
	END IF;
	IF (NEW.actual_piece_count IS NOT NULL) THEN
		SET inserted_values=(SELECT concat_insert_values(inserted_values,' Actual Piece Count',new.actual_piece_count));
	END IF;
	IF (NEW.actual_weight_imperial IS NOT NULL) THEN
		SET inserted_values=(SELECT concat_insert_values(inserted_values,' Actual Weight LBS',new.actual_weight_imperial));
	END IF;
	IF (NEW.actual_weight_metric IS NOT NULL) THEN
		SET inserted_values=(SELECT concat_insert_values(inserted_values,' Actual Weight KGS',new.actual_weight_metric));
	END IF;
	IF (NEW.actual_volume_imperial IS NOT NULL) THEN
		SET inserted_values=(SELECT concat_insert_values(inserted_values,' Actual Volume CFT',new.actual_volume_imperial));
	END IF;
	IF (NEW.actual_volume_metric IS NOT NULL) THEN
		SET inserted_values=(SELECT concat_insert_values(inserted_values,' Actual Volume CBM',new.actual_volume_metric));
	END IF;
	IF NEW.hazmat IS NOT NULL THEN
         SET inserted_values = (SELECT concat_insert_values(inserted_values,'HaZmat',IF(NEW.hazmat=0,'No','Yes')));
         END IF;
	IF (NEW.piece_desc <> '') THEN
	   SET inserted_values=CONCAT(IFNULL(inserted_values,''),' Commodity Desc-->',NEW.piece_desc);
        END IF;
       IF (NEW.mark_no_desc <> '' ) THEN
         SET inserted_values=CONCAT(IFNULL(inserted_values,''),' Marks and Numbers-->',NEW.mark_no_desc);
       END IF;
       IF (NEW.weight_verfied_user_id IS NOT NULL) THEN
         SET @newUserName=(SELECT CONCAT(first_name,' ',last_name) FROM user_details WHERE user_id=new.weight_verfied_user_id);
         SET inserted_values=(SELECT concat_insert_values(inserted_values,' Weighed By',@newUserName));
       END IF;
        IF (inserted_values <> '') THEN
        SET inserted_values=CONCAT('Inserted ->',inserted_values);
		INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
		VALUES(new.file_number_id,'DR-AutoNotes',inserted_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
	END IF;
    END;
$$

DELIMITER ;



DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBookingPiece_update`$$

CREATE TRIGGER `after_LclBookingPiece_update` AFTER UPDATE ON `lcl_booking_piece`
    FOR EACH ROW BEGIN
DECLARE updated_values TEXT ;
DECLARE change_value BOOLEAN ;
IF new.commodity_type_id!=old.commodity_type_id THEN
SET updated_values=(SELECT concat_string(updated_values,' Commodity',(SELECT CONCAT(desc_en,' (',CODE,')') FROM commodity_type WHERE id=old.commodity_type_id),(SELECT CONCAT(desc_en,' (',CODE,')') FROM commodity_type WHERE id=new.commodity_type_id)));
END IF;
IF new.booked_package_type_id!=old.booked_package_type_id THEN
SET updated_values=(SELECT concat_string(updated_values,' Package',(SELECT description FROM package_type WHERE id=old.booked_package_type_id),(SELECT description FROM package_type WHERE id=new.booked_package_type_id)));
END IF;
IF new.booked_piece_count!=old.booked_piece_count THEN
SET updated_values=(SELECT concat_string(updated_values,' Piece Count',old.booked_piece_count,new.booked_piece_count));
SET change_value=TRUE;
END IF;
IF new.booked_weight_imperial!=old.booked_weight_imperial THEN
SET updated_values=(SELECT concat_string(updated_values,' Weight  LBS',old.booked_weight_imperial,new.booked_weight_imperial));
SET change_value=TRUE;
END IF;
IF new.booked_weight_metric!=old.booked_weight_metric THEN
SET updated_values=(SELECT concat_string(updated_values,' Weight  KGS',old.booked_weight_metric,new.booked_weight_metric));
SET change_value=TRUE;
END IF;
IF new.booked_volume_imperial!=old.booked_volume_imperial THEN
SET updated_values=(SELECT concat_string(updated_values,' Volume  CFT',old.booked_volume_imperial,new.booked_volume_imperial));
SET change_value=TRUE;
END IF;
IF new.booked_volume_metric!=old.booked_volume_metric THEN
SET updated_values=(SELECT concat_string(updated_values,' Volume  CBM',old.booked_volume_metric,new.booked_volume_metric));
SET change_value=TRUE;
END IF;
IF ((OLD.actual_piece_count IS NULL AND NEW.actual_piece_count IS NOT NULL) OR NEW.actual_piece_count != OLD.actual_piece_count OR
(OLD.actual_piece_count IS NOT NULL AND NEW.actual_piece_count IS NULL)) THEN
SET updated_values=(SELECT concat_string(updated_values,' Actual Piece Count',IFNULL(old.actual_piece_count,''),IFNULL(new.actual_piece_count,'')));
END IF;
IF ((OLD.actual_package_type_id IS NULL AND NEW.actual_package_type_id IS NOT NULL) OR NEW.actual_package_type_id != OLD.actual_package_type_id
OR (OLD.actual_package_type_id IS NOT NULL AND NEW.actual_package_type_id IS NULL)) THEN
 SET @oldPackage=(SELECT description FROM package_type WHERE id=old.actual_package_type_id);
 SET @newPackage=(SELECT description FROM package_type WHERE id=new.actual_package_type_id);
 SET updated_values=(SELECT concat_string(updated_values,' Actual Package',IFNULL(@oldPackage,''),IFNULL(@newPackage,'')));
END IF;
IF ((OLD.actual_weight_imperial IS NULL AND NEW.actual_weight_imperial IS NOT NULL) OR NEW.actual_weight_imperial != OLD.actual_weight_imperial OR
(OLD.actual_weight_imperial IS NOT NULL AND NEW.actual_weight_imperial IS NULL)) THEN
SET updated_values=(SELECT concat_string(updated_values,' Actual Weight LBS',IFNULL(old.actual_weight_imperial,''),IFNULL(new.actual_weight_imperial,'')));
END IF;
IF ((OLD.actual_weight_metric IS NULL AND NEW.actual_weight_metric IS NOT NULL) OR NEW.actual_weight_metric != OLD.actual_weight_metric OR
(OLD.actual_weight_metric IS NOT NULL AND NEW.actual_weight_metric IS NULL)) THEN
SET updated_values=(SELECT concat_string(updated_values,' Actual Weight KGS',IFNULL(old.actual_weight_metric,''),IFNULL(new.actual_weight_metric,'')));
END IF;
IF ((OLD.actual_volume_imperial IS NULL AND NEW.actual_volume_imperial IS NOT NULL) OR NEW.actual_volume_imperial != OLD.actual_volume_imperial OR
(OLD.actual_volume_imperial IS NOT NULL AND NEW.actual_volume_imperial IS NULL)) THEN
SET updated_values=(SELECT concat_string(updated_values,' Actual Volume CFT',IFNULL(old.actual_volume_imperial,''),IFNULL(new.actual_volume_imperial,'')));
END IF;
IF ((OLD.actual_volume_metric IS NULL AND NEW.actual_volume_metric IS NOT NULL) OR NEW.actual_volume_metric != OLD.actual_volume_metric OR
(OLD.actual_volume_metric IS NOT NULL AND NEW.actual_volume_metric IS NULL)) THEN
SET updated_values=(SELECT concat_string(updated_values,' Actual Volume CBM',IFNULL(old.actual_volume_metric,''),IFNULL(new.actual_volume_metric,'')));
END IF;
IF new.hazmat <> old.hazmat THEN
  SET updated_values=(SELECT concat_string(updated_values,'HaZmat',IF(old.hazmat=0,'No','Yes'),IF(new.hazmat=0,'No','Yes')));
END IF;
IF ((OLD.piece_desc IS NULL AND NEW.piece_desc IS NOT NULL) OR NEW.piece_desc != OLD.piece_desc OR (OLD.piece_desc IS NOT NULL AND NEW.piece_desc IS NULL)) THEN
  SET updated_values=(SELECT CONCAT(IFNULL(updated_values,''),' Commodity Desc-->','Descriptions have been changed'));
        END IF;
       IF ((OLD.mark_no_desc IS NULL AND NEW.mark_no_desc IS NOT NULL) OR NEW.mark_no_desc != OLD.mark_no_desc OR (OLD.mark_no_desc IS NOT NULL AND NEW.mark_no_desc IS NULL)) THEN
         SET updated_values=CONCAT(IFNULL(updated_values,''),' Marks and Numbers-->','Marks have been changed');
       END IF;
IF  change_value=TRUE THEN
        SET @unit_ss_id=(SELECT lcl_unit_ss_id FROM lcl_booking_piece_unit WHERE booking_piece_id=old.id);
        CALL UpdateUnitWMValue(@unit_ss_id);
        END IF;
IF ((OLD.weight_verfied_user_id IS NULL AND NEW.weight_verfied_user_id IS NOT NULL) OR NEW.weight_verfied_user_id != OLD.weight_verfied_user_id OR (OLD.weight_verfied_user_id IS NOT NULL AND NEW.weight_verfied_user_id IS NULL)) THEN
     SET @oldUserName=(SELECT CONCAT(first_name,' ',last_name) FROM user_details WHERE user_id=old.weight_verfied_user_id);
     SET @newUserName=(SELECT CONCAT(first_name,' ',last_name) FROM user_details WHERE user_id=new.weight_verfied_user_id);
     SET updated_values=(SELECT concat_string(updated_values,'Weighed By',IFNULL(@oldUserName,''),IFNULL(@newUserName,'')));
END IF;
        IF updated_values IS NOT NULL THEN
        SET updated_values=CONCAT('UPDATED ->',updated_values);
INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
VALUES(OLD.file_number_id,'DR-AutoNotes',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
END IF;
        END;
$$

DELIMITER ;


DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_lclBookingPieceWhse_update`$$

CREATE
    TRIGGER `after_lclBookingPieceWhse_update` AFTER UPDATE ON `lcl_booking_piece_whse`
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT ;
  IF ((OLD.warehouse_id IS NULL AND NEW.warehouse_id IS NOT NULL) OR NEW.warehouse_id != OLD.warehouse_id OR (OLD.warehouse_id IS NOT NULL AND NEW.warehouse_id IS NULL)) THEN
     SET @oldwarehouseValues=(SELECT CONCAT(warehsname,'(',warehsno,')') FROM warehouse WHERE id=old.warehouse_id);
     SET @newwarehouseValues=(SELECT CONCAT(warehsname,'(',warehsno,')') FROM warehouse WHERE id=new.warehouse_id);
     SET updated_values=(SELECT concat_string(updated_values,'Warehouse Location',IFNULL(@oldwarehouseValues,''),IFNULL(@newwarehouseValues,'')));
  END IF;
   IF ((OLD.location IS NULL AND NEW.location IS NOT NULL) OR NEW.location != OLD.location OR (OLD.location IS NOT NULL AND NEW.location IS NULL)) THEN
     SET updated_values=(SELECT concat_string(updated_values,'Line/Location',IFNULL(UPPER(old.location),''),IFNULL(UPPER(new.location),'')));
  END IF;
   IF((OLD.stowed_by_user_id IS NULL AND NEW.stowed_by_user_id IS NOT NULL) OR NEW.stowed_by_user_id != OLD.stowed_by_user_id OR
    (OLD.stowed_by_user_id IS NOT NULL AND NEW.stowed_by_user_id IS NULL)) THEN
     SET @oldStowedValues=(SELECT CONCAT(first_name,' ',IFNULL(last_name,'')) FROM user_details WHERE user_id=old.stowed_by_user_id);
     SET @newStowedValues=(SELECT CONCAT(first_name,' ',IFNULL(last_name,'')) FROM user_details WHERE user_id=new.stowed_by_user_id);
     SET updated_values=(SELECT concat_string(updated_values,'Stowed By',IFNULL(@oldStowedValues,''),IFNULL(@newStowedValues,'')));
  END IF;
    IF updated_values IS NOT NULL THEN
      SET @fileId=(SELECT file_number_id FROM lcl_booking_piece WHERE id=old.booking_piece_id);
      SET updated_values=CONCAT('UPDATED ->',updated_values);
      INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(@fileId,'DR-AutoNotes',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `lclbookingpiecewhse_insert_trigger`$$

CREATE TRIGGER `lclbookingpiecewhse_insert_trigger` AFTER INSERT ON `lcl_booking_piece_whse`
    FOR EACH ROW BEGIN
    DECLARE insert_values TEXT;
    SET insert_values = concat_insert_values(insert_values,'Warehouse Location',(SELECT CONCAT(warehsname,'(',warehsno,')') FROM warehouse WHERE id=new.warehouse_id));
    IF new.location!='' THEN
    SET insert_values=(SELECT concat_insert_values(insert_values,'LineLocation',UPPER(new.location)));
    END IF;
    IF insert_values IS NOT NULL THEN
       SET @fileId=(SELECT file_number_id FROM lcl_booking_piece WHERE id=new.booking_piece_id);
        SET insert_values=CONCAT('INSERTED ->',insert_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(@fileId,'DR-AutoNotes',insert_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;


DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclFileNumber_update`$$

CREATE TRIGGER `after_LclFileNumber_update` AFTER UPDATE ON `lcl_file_number`
    FOR EACH ROW BEGIN
        DECLARE uid BIGINT ;
        DECLARE entered_time DATETIME;
        DECLARE bkg_type VARCHAR(2);
        DECLARE loginName VARCHAR(100);
        DECLARE day_limit VARCHAR(5);
        SELECT VALUE INTO day_limit FROM property WHERE NAME='LclExportBookingExpiryDate' LIMIT 1;
        SELECT user_id INTO uid FROM user_details WHERE login_name ='system';
        SELECT entered_datetime INTO entered_time FROM lcl_booking WHERE file_number_id=new.id;
        SELECT booking_type INTO bkg_type FROM lcl_booking WHERE file_number_id=new.id;
        SELECT login_name INTO loginName FROM user_details WHERE login_name ='system';
      IF old.status!=new.status THEN
        CALL update_lclbooking_moddate(new.id);
      END IF;
      IF  old.status='B' AND new.status='X' AND  DATEDIFF(SYSDATE(), entered_time) >= day_limit AND new.state='B' AND bkg_type='E'  AND  loginName='system' THEN
              INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
            VALUES(new.id,'DR-AutoNotes',CONCAT('Cargo Terminated due to  ',day_limit,'  days expiration rule'),NOW(),uid,NOW(),uid);
       END IF;
       END;
$$

DELIMITER ;

DELIMITER $$
DROP TRIGGER /*!50032 IF EXISTS */ `after_lclBookingAc_trans_update`$$
CREATE
    TRIGGER `after_lclBookingAc_trans_update` AFTER UPDATE ON `lcl_booking_ac_trans`
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT ;
    IF (old.payment_type <> new.payment_type)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Payment Type Changed',old.payment_type,new.payment_type));
     END IF;
    IF updated_values IS NOT NULL THEN
    SET updated_values=CONCAT('UPDATED ->',updated_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(OLD.file_number_id,'DR-AutoNotes',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END;
$$
DELIMITER ;



DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBl_update`$$

CREATE TRIGGER `after_LclBl_update` AFTER UPDATE ON `lcl_bl`
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT;
    DECLARE fileStatus VARCHAR(2);
    DECLARE notiStatusFlag BOOLEAN DEFAULT FALSE;
    DECLARE bl_values TEXT ;
    DECLARE changesFlag BOOLEAN DEFAULT FALSE;
    SELECT lfn.status INTO fileStatus FROM lcl_file_number lfn WHERE lfn.id=OLD.file_number_id;
     IF ((old.ship_acct_no IS NULL AND new.ship_acct_no IS NOT NULL)OR new.ship_acct_no!=old.ship_acct_no OR(new.ship_acct_no IS NULL AND old.ship_acct_no IS NOT NULL))THEN
    SET @oldClientValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.ship_acct_no);
    SET @newClientValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.ship_acct_no);
    SET updated_values=(SELECT concat_string (updated_values,'Shipper',IFNULL(@oldClientValues,''),IFNULL(@newClientValues,'')));
    SET changesFlag = TRUE ;
    END IF;
     IF ((OLD.cons_acct_no IS NULL AND NEW.cons_acct_no IS NOT NULL) OR NEW.cons_acct_no != OLD.cons_acct_no OR (OLD.cons_acct_no IS NOT NULL AND NEW.cons_acct_no IS NULL)) THEN
     SET @oldConsValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.cons_acct_no);
     SET @newConsValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.cons_acct_no);
     SET updated_values=(SELECT concat_string(updated_values,'Consignee',IFNULL(@oldConsValues,''),IFNULL(@newConsValues,'')));
     SET changesFlag = TRUE ;
   END IF;
    IF ((OLD.noty_acct_no IS NULL AND NEW.noty_acct_no IS NOT NULL) OR NEW.noty_acct_no != OLD.noty_acct_no OR (OLD.noty_acct_no IS NOT NULL AND NEW.noty_acct_no IS NULL)) THEN
     SET @oldNotyValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.noty_acct_no);
     SET @newNotyValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.noty_acct_no);
     SET updated_values=(SELECT concat_string(updated_values,'Notify Party',IFNULL(@oldNotyValues,''),IFNULL(@newNotyValues,'')));
     SET changesFlag = TRUE ;
   END IF;
     IF ((old.fwd_acct_no IS NULL AND new.fwd_acct_no IS NOT NULL)OR new.fwd_acct_no!=old.fwd_acct_no OR(new.fwd_acct_no IS NULL AND old.fwd_acct_no IS NOT NULL))THEN
    SET @oldClientValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.fwd_acct_no);
    SET @newClientValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.fwd_acct_no);
    SET updated_values=(SELECT concat_string (updated_values,'Forwarder',IFNULL(@oldClientValues,''),IFNULL(@newClientValues,'')));
    SET changesFlag = TRUE ;
    END IF;
   IF ((OLD.third_party_acct_no IS NULL AND NEW.third_party_acct_no IS NOT NULL) OR NEW.third_party_acct_no != OLD.third_party_acct_no OR (OLD.third_party_acct_no IS NOT NULL AND NEW.third_party_acct_no IS NULL)) THEN
     SET @oldThirdValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.third_party_acct_no);
     SET @newThirdValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.third_party_acct_no);
     SET updated_values=(SELECT concat_string(updated_values,'Third Party',IFNULL(@oldThirdValues,''),IFNULL(@newThirdValues,'')));
     SET changesFlag = TRUE ;
   END IF;
     IF ((OLD.sup_acct_no IS NULL AND NEW.sup_acct_no IS NOT NULL) OR NEW.sup_acct_no!=OLD.sup_acct_no OR (OLD.sup_acct_no IS NOT NULL AND NEW.sup_acct_no IS NULL)) THEN
   SET @oldSupValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.sup_acct_no);
   SET @newSupValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.sup_acct_no);
   SET updated_values=(SELECT concat_string(updated_values,'Supplier',IFNULL(@oldSupValues,''),IFNULL(@newSupValues,'')));
   END IF;
      IF new.terms_type1!=old.terms_type1 THEN
       SET updated_values=(SELECT concat_string(updated_values,'Termstype1',old.terms_type1,new.terms_type1));
    END IF;
        IF new.terms_type2!=old.terms_type2 THEN
       SET updated_values=(SELECT concat_string(updated_values,'Termstype2',old.terms_type2,new.terms_type2));
    END IF;
         IF new.type2_date!=old.type2_date THEN
       SET updated_values=(SELECT concat_string(updated_values,'Type2Date',old.type2_date,new.type2_date));
    END IF;
         IF new.point_of_origin!=old.point_of_origin THEN
       SET updated_values=(SELECT concat_string(updated_values,'PointOfOrigin',old.point_of_origin,new.point_of_origin));
    END IF;
    IF new.bl_owner_id!=old.bl_owner_id THEN
     SET bl_values=(SELECT concat_string(updated_values,'BL Owner is Changed',(SELECT login_name FROM user_details WHERE user_id=old.bl_owner_id),(SELECT login_name FROM user_details WHERE user_id=new.bl_owner_id)));
       INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
       VALUES(OLD.file_number_id,'BL-AutoNotes',bl_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    IF old.poo_id!=new.poo_id THEN
	SET updated_values=(SELECT concat_string(updated_values,'PlaceOfReceipt',(SELECT un_loc_name FROM un_location WHERE id=old.poo_id),(SELECT un_loc_name FROM un_location WHERE id=new.poo_id)));
    END IF;
   IF old.fd_id!=new.fd_id THEN
	SET updated_values=(SELECT concat_string(updated_values,'Destination',(SELECT un_loc_name FROM un_location WHERE id=old.fd_id),(SELECT un_loc_name FROM un_location WHERE id=new.fd_id)));
	SET changesFlag = TRUE ;
    END IF;
          IF new.spot_rate!=old.spot_rate THEN
      SET updated_values=(SELECT concat_string(updated_values,'SpotRate',IF(old.spot_rate=0,'No','Yes'),IF(new.spot_rate=0,'No','Yes')));
    END IF;
    IF new.rate_type!=old.rate_type THEN
    SET updated_values=(SELECT concat_string(updated_values,'RateType',old.rate_type,new.rate_type));
    END IF;
    IF ((OLD.booked_ss_header_id IS NULL AND NEW.booked_ss_header_id IS NOT NULL) OR NEW.booked_ss_header_id!=OLD.booked_ss_header_id OR (OLD.booked_ss_header_id IS NOT NULL AND NEW.booked_ss_header_id IS NULL)) THEN
     SET @oldScheduleValue = (SELECT CONCAT (l.`schedule_no` ,' / ', DATE_FORMAT((SELECT d.std FROM lcl_ss_detail d WHERE d.ss_header_id=old.booked_ss_header_id  AND d.trans_mode='V'),'%d-%b-%Y'))  FROM lcl_ss_header  l  WHERE  l.id=old.booked_ss_header_id);
     SET @newScheduleValue = (SELECT CONCAT (l.`schedule_no` ,' / ', DATE_FORMAT((SELECT d.std FROM lcl_ss_detail d WHERE d.ss_header_id=new.booked_ss_header_id  AND d.trans_mode='V'),'%d-%b-%Y'))  FROM lcl_ss_header  l  WHERE  l.id=new.booked_ss_header_id);
     SET updated_values=(SELECT concat_string(updated_values,'Booked Voyage is changed',IFNULL(@oldScheduleValue,''),IFNULL(@newScheduleValue,'')) );
       SET changesFlag = TRUE ;
    END IF;
    IF new.billing_type!=old.billing_type THEN
    SET updated_values=(SELECT concat_string(updated_values,'Terms',old.billing_type,new.billing_type));
    SET changesFlag = TRUE ;
    END IF;
     IF new.billing_type!=old.billing_type THEN
      SET changesFlag = TRUE ;
    END IF;
     IF new.bill_to_party!=old.bill_to_party THEN
      SET changesFlag = TRUE ;
    END IF;
    IF new.free_bl!=old.free_bl THEN
    SET updated_values=(SELECT concat_string(updated_values,'Free B/L',IF(old.free_bl=0,'No','Yes'),IF(new.free_bl=0,'No','Yes')));
    SET changesFlag = TRUE ;
    END IF;
    IF updated_values IS NOT NULL THEN
    SET updated_values=CONCAT('UPDATED ->',updated_values);
       IF (fileStatus='M'  AND changesFlag = TRUE) THEN
       SELECT IF(COUNT(*)>0,TRUE,FALSE) INTO notiStatusFlag FROM lcl_export_notification len
       WHERE len.file_number_id=OLD.file_number_id AND len.file_status='Changes' AND len.status='Pending';
       IF (notiStatusFlag=FALSE) THEN
        INSERT INTO lcl_export_notification (file_number_id, file_status,STATUS,entered_datetime,entered_by_user_id)
        VALUES(OLD.file_number_id,'Changes','Pending',NOW(),new.modified_by_user_id);
        END IF;
        END IF;
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(OLD.file_number_id,'BL-AutoNotes',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;



DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_lcl_bl_piece_update`$$

CREATE TRIGGER `after_lcl_bl_piece_update` AFTER UPDATE ON `lcl_bl_piece`
    FOR EACH ROW BEGIN
DECLARE updated_values TEXT ;
DECLARE change_value BOOLEAN ;
DECLARE fileStatus VARCHAR(2);
DECLARE notiStatusFlag BOOLEAN DEFAULT FALSE;
SELECT lfn.status INTO fileStatus FROM lcl_file_number lfn WHERE lfn.id=old.file_number_id;
IF new.commodity_type_id!=old.commodity_type_id THEN
SET updated_values=(SELECT concat_string(updated_values,' Commodity',(SELECT CONCAT(desc_en,' (',CODE,')') FROM commodity_type WHERE id=old.commodity_type_id),(SELECT CONCAT(desc_en,' (',CODE,')') FROM commodity_type WHERE id=new.commodity_type_id)));
END IF;
IF new.packaging_type_id!=old.packaging_type_id THEN
SET updated_values=(SELECT concat_string(updated_values,' Package',(SELECT description FROM package_type WHERE id=old.packaging_type_id),(SELECT description FROM package_type WHERE id=new.packaging_type_id)));
END IF;
IF new.booked_piece_count!=old.booked_piece_count THEN
SET updated_values=(SELECT concat_string(updated_values,' Piece Count',old.booked_piece_count,new.booked_piece_count));
SET change_value=TRUE;
END IF;
IF new.booked_weight_imperial!=old.booked_weight_imperial THEN
SET updated_values=(SELECT concat_string(updated_values,' Weight  LBS',old.booked_weight_imperial,new.booked_weight_imperial));
SET change_value=TRUE;
END IF;
IF new.booked_weight_metric!=old.booked_weight_metric THEN
SET updated_values=(SELECT concat_string(updated_values,' Weight  KGS',old.booked_weight_metric,new.booked_weight_metric));
SET change_value=TRUE;
END IF;
IF new.booked_volume_imperial!=old.booked_volume_imperial THEN
SET updated_values=(SELECT concat_string(updated_values,' Volume  CFT',old.booked_volume_imperial,new.booked_volume_imperial));
SET change_value=TRUE;
END IF;
IF new.booked_volume_metric!=old.booked_volume_metric THEN
SET updated_values=(SELECT concat_string(updated_values,' Volume  CBM',old.booked_volume_metric,new.booked_volume_metric));
SET change_value=TRUE;
END IF;
IF new.actual_piece_count!=old.actual_piece_count THEN
SET updated_values=(SELECT concat_string(updated_values,' Actual Piece Count',old.actual_piece_count,new.actual_piece_count));
END IF;
IF new.actual_weight_imperial!=old.actual_weight_imperial THEN
SET updated_values=(SELECT concat_string(updated_values,' Actual Weight LBS',old.actual_weight_imperial,new.actual_weight_imperial));
END IF;
IF new.actual_weight_metric!=old.actual_weight_metric THEN
SET updated_values=(SELECT concat_string(updated_values,' Actual Weight KGS',old.actual_weight_metric,new.actual_weight_metric));
END IF;
IF new.actual_volume_imperial!=old.actual_volume_imperial THEN
SET updated_values=(SELECT concat_string(updated_values,' Actual Volume CFT',old.actual_volume_imperial,new.actual_volume_imperial));
END IF;
IF new.actual_volume_metric!=old.actual_volume_metric THEN
SET updated_values=(SELECT concat_string(updated_values,' Actual Volume CBM',old.actual_volume_metric,new.actual_volume_metric));
END IF;
IF ((OLD.piece_desc IS NULL AND NEW.piece_desc IS NOT NULL) OR NEW.piece_desc != OLD.piece_desc OR (OLD.piece_desc IS NOT NULL AND NEW.piece_desc IS NULL)) THEN
SET updated_values=(SELECT concat_string(updated_values,' Commodity Desc',OLD.piece_desc,NEW.piece_desc));
END IF;
IF ((OLD.mark_no_desc IS NULL AND NEW.mark_no_desc IS NOT NULL) OR NEW.mark_no_desc != OLD.mark_no_desc OR (OLD.mark_no_desc IS NOT NULL AND NEW.mark_no_desc IS NULL)) THEN
SET updated_values=(SELECT concat_string(updated_values,' Marks and Numbers',OLD.mark_no_desc,NEW.mark_no_desc));
END IF;
IF updated_values IS NOT NULL THEN
    IF fileStatus='M' THEN
         SELECT IF(COUNT(*)>0,TRUE,FALSE) INTO notiStatusFlag FROM lcl_export_notification len
         WHERE len.file_number_id=old.file_number_id AND len.file_status='Changes' AND len.status='Pending';
     IF (notiStatusFlag=FALSE) THEN
        INSERT INTO lcl_export_notification (file_number_id, file_status,STATUS,entered_datetime,entered_by_user_id)
        VALUES(old.file_number_id,'Changes','Pending',NOW(),new.modified_by_user_id);
     END IF;
     END IF;
        SET updated_values=CONCAT('UPDATED ->',updated_values);
     INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
     VALUES(OLD.file_number_id,'BL-AutoNotes',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
END IF;
 END;
$$

DELIMITER ;



DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_lclblac_update`$$

CREATE TRIGGER `after_lclblac_update` AFTER UPDATE ON `lcl_bl_ac`
    FOR EACH ROW BEGIN
  DECLARE updatedValues TEXT DEFAULT '';
  DECLARE chargeValue BOOLEAN DEFAULT FALSE;
  DECLARE changeValue BOOLEAN DEFAULT FALSE;
  DECLARE notiStatusFlag BOOLEAN DEFAULT FALSE;
  DECLARE fileStatus VARCHAR(2);
  SELECT lfn.status INTO fileStatus FROM lcl_file_number lfn WHERE lfn.id=OLD.file_number_id;
  IF (new.ar_gl_mapping_id <> old.ar_gl_mapping_id) THEN
    SET updatedValues = concat_string(updatedValues, 'Charge Code', (SELECT charge_code FROM gl_mapping WHERE id = old.ar_gl_mapping_id), (SELECT charge_code FROM gl_mapping WHERE id = new.ar_gl_mapping_id)) ;
    SET chargeValue = TRUE ;
    SET changeValue = TRUE;
  END IF ;
  IF (new.ar_amount <> old.ar_amount) THEN
    SET updatedValues = concat_string(updatedValues, 'Charge Amount', old.ar_amount, new.ar_amount) ;
    SET changeValue = TRUE;
  END IF ;
  IF (new.adjustment_amount <> old.adjustment_amount) THEN
    SET updatedValues = concat_string(updatedValues, 'Adjustment Amount', old.adjustment_amount, new.adjustment_amount) ;
    SET changeValue = TRUE;
  END IF ;
  IF ((new.adjustment_comments <> old.adjustment_comments) OR (new.adjustment_comments IS NOT NULL && old.adjustment_comments IS NULL)) THEN
    SET updatedValues = concat_string(updatedValues, 'Adjustment Comments', old.adjustment_comments, new.adjustment_comments) ;
    SET changeValue = TRUE;
  END IF ;
   IF (new.rate_uom <> old.rate_uom) THEN
    SET updatedValues = concat_string(updatedValues, 'Rate UOM', old.rate_uom, new.rate_uom) ;
    SET changeValue = TRUE;
  END IF ;
  IF (new.rate_per_weight_unit <> old.rate_per_weight_unit) THEN
    SET updatedValues = concat_string(updatedValues, 'Weight', old.rate_per_weight_unit, new.rate_per_weight_unit) ;
    SET changeValue = TRUE;
  END IF ;
  IF (new.rate_per_volume_unit <> old.rate_per_volume_unit) THEN
    SET updatedValues = concat_string (updatedValues, 'Volume', old.rate_per_volume_unit, new.rate_per_volume_unit) ;
    SET changeValue = TRUE;
  END IF ;
  IF (new.rate_flat_minimum <> old.rate_flat_minimum) THEN
    SET updatedValues = concat_string(updatedValues, 'Minimum', old.rate_flat_minimum, new.rate_flat_minimum) ;
    SET changeValue = TRUE;
  END IF ;
  IF (new.sp_acct_no <> old.sp_acct_no) THEN
    SET updatedValues = concat_string(updatedValues, 'Vendor Name', (SELECT acct_name FROM trading_partner WHERE acct_no = old.sp_acct_no), (SELECT acct_name FROM trading_partner WHERE acct_no = new.sp_acct_no));
    SET updatedValues = concat_string(updatedValues, 'Vendor Number', old.sp_acct_no, new.sp_acct_no);
    SET changeValue = TRUE;
  END IF ;
  IF ((new.invoice_number <> old.invoice_number) OR (new.invoice_number IS NOT NULL && old.invoice_number IS NULL)) THEN
    SET updatedValues = concat_string(updatedValues, 'Invoice Number', old.invoice_number, new.invoice_number) ;
    SET changeValue = TRUE;
  END IF ;
  IF (new.ar_bill_to_party <> old.ar_bill_to_party) THEN
    SET changeValue = TRUE;
  END IF;
   IF (chargeValue = TRUE) THEN
      SET updatedValues = CONCAT('UPDATED ->', updatedValues) ;
    ELSEIF (changeValue = TRUE AND  updatedValues <> '') THEN
      SET updatedValues = CONCAT('UPDATED -> (Code -> ', (SELECT charge_code FROM gl_mapping WHERE id = old.ar_gl_mapping_id), ')', updatedValues) ;
    END IF ;
    IF (fileStatus='M' AND changeValue = TRUE) THEN
    SELECT IF(COUNT(*)>0,TRUE,FALSE) INTO notiStatusFlag FROM lcl_export_notification len
    WHERE len.file_number_id=OLD.file_number_id AND len.file_status='Changes' AND len.status='Pending';
      IF (notiStatusFlag=FALSE) THEN
        INSERT INTO lcl_export_notification (file_number_id, file_status,STATUS,entered_datetime,entered_by_user_id)
        VALUES(OLD.file_number_id,'Changes','Pending',NOW(),new.modified_by_user_id);
    END IF;
   END IF;
   IF (updatedValues <> '') THEN
        INSERT INTO lcl_remarks ( file_number_id,  TYPE, remarks, entered_datetime, entered_by_user_id, modified_datetime, modified_by_user_id)
        VALUES (old.file_number_id,'BL-AutoNotes',updatedValues,NOW(), new.modified_by_user_id, NOW(), new.modified_by_user_id) ;
  END IF;
  END;
$$

DELIMITER ;



DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_lclblac_update`$$

CREATE TRIGGER `after_lclblac_update` AFTER UPDATE ON `lcl_bl_ac`
    FOR EACH ROW BEGIN
  DECLARE updatedValues TEXT DEFAULT '';
  DECLARE chargeValue BOOLEAN DEFAULT FALSE;
  DECLARE changeValue BOOLEAN DEFAULT FALSE;
  DECLARE notiStatusFlag BOOLEAN DEFAULT FALSE;
  DECLARE fileStatus VARCHAR(2);
  SELECT lfn.status INTO fileStatus FROM lcl_file_number lfn WHERE lfn.id=OLD.file_number_id;
  IF (new.ar_gl_mapping_id <> old.ar_gl_mapping_id) THEN
    SET updatedValues = concat_string(updatedValues, 'Charge Code', (SELECT charge_code FROM gl_mapping WHERE id = old.ar_gl_mapping_id), (SELECT charge_code FROM gl_mapping WHERE id = new.ar_gl_mapping_id)) ;
    SET chargeValue = TRUE ;
    SET changeValue = TRUE;
  END IF ;
  IF (new.ar_amount <> old.ar_amount) THEN
    SET updatedValues = concat_string(updatedValues, 'Charge Amount', old.ar_amount, new.ar_amount) ;
    SET changeValue = TRUE;
  END IF ;
  IF (new.adjustment_amount <> old.adjustment_amount) THEN
    SET updatedValues = concat_string(updatedValues, 'Adjustment Amount', old.adjustment_amount, new.adjustment_amount) ;
    SET changeValue = TRUE;
  END IF ;
  IF ((new.adjustment_comments <> old.adjustment_comments) OR (new.adjustment_comments IS NOT NULL && old.adjustment_comments IS NULL)) THEN
    SET updatedValues = concat_string(updatedValues, 'Adjustment Comments', old.adjustment_comments, new.adjustment_comments) ;
    SET changeValue = TRUE;
  END IF ;
   IF (new.rate_uom <> old.rate_uom) THEN
    SET updatedValues = concat_string(updatedValues, 'Rate UOM', old.rate_uom, new.rate_uom) ;
    SET changeValue = TRUE;
  END IF ;
  IF (new.rate_per_weight_unit <> old.rate_per_weight_unit) THEN
    SET updatedValues = concat_string(updatedValues, 'Weight', old.rate_per_weight_unit, new.rate_per_weight_unit) ;
    SET changeValue = TRUE;
  END IF ;
  IF (new.rate_per_volume_unit <> old.rate_per_volume_unit) THEN
    SET updatedValues = concat_string (updatedValues, 'Volume', old.rate_per_volume_unit, new.rate_per_volume_unit) ;
    SET changeValue = TRUE;
  END IF ;
  IF (new.rate_flat_minimum <> old.rate_flat_minimum) THEN
    SET updatedValues = concat_string(updatedValues, 'Minimum', old.rate_flat_minimum, new.rate_flat_minimum) ;
    SET changeValue = TRUE;
  END IF ;
  IF (new.sp_acct_no <> old.sp_acct_no) THEN
    SET updatedValues = concat_string(updatedValues, 'Vendor Name', (SELECT acct_name FROM trading_partner WHERE acct_no = old.sp_acct_no), (SELECT acct_name FROM trading_partner WHERE acct_no = new.sp_acct_no));
    SET updatedValues = concat_string(updatedValues, 'Vendor Number', old.sp_acct_no, new.sp_acct_no);
    SET changeValue = TRUE;
  END IF ;
  IF ((new.invoice_number <> old.invoice_number) OR (new.invoice_number IS NOT NULL && old.invoice_number IS NULL)) THEN
    SET updatedValues = concat_string(updatedValues, 'Invoice Number', old.invoice_number, new.invoice_number) ;
    SET changeValue = TRUE;
  END IF ;
  IF (new.ar_bill_to_party <> old.ar_bill_to_party) THEN
    SET changeValue = TRUE;
  END IF;
   IF (chargeValue = TRUE) THEN
      SET updatedValues = CONCAT('UPDATED ->', updatedValues) ;
    ELSEIF (changeValue = TRUE AND  updatedValues <> '') THEN
      SET updatedValues = CONCAT('UPDATED -> (Code -> ', (SELECT charge_code FROM gl_mapping WHERE id = old.ar_gl_mapping_id), ')', updatedValues) ;
    END IF ;
    IF (fileStatus='M' AND changeValue = TRUE) THEN
    SELECT IF(COUNT(*)>0,TRUE,FALSE) INTO notiStatusFlag FROM lcl_export_notification len
    WHERE len.file_number_id=OLD.file_number_id AND len.file_status='Changes' AND len.status='Pending';
      IF (notiStatusFlag=FALSE) THEN
        INSERT INTO lcl_export_notification (file_number_id, file_status,STATUS,entered_datetime,entered_by_user_id)
        VALUES(OLD.file_number_id,'Changes','Pending',NOW(),new.modified_by_user_id);
    END IF;
   END IF;
   IF (updatedValues <> '') THEN
        INSERT INTO lcl_remarks ( file_number_id,  TYPE, remarks, entered_datetime, entered_by_user_id, modified_datetime, modified_by_user_id)
        VALUES (old.file_number_id,'BL-AutoNotes',updatedValues,NOW(), new.modified_by_user_id, NOW(), new.modified_by_user_id) ;
  END IF;
  END;
$$

DELIMITER ;


DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `lcl_bl_ac_insert_trigger`$$

CREATE TRIGGER `lcl_bl_ac_insert_trigger` AFTER INSERT ON `lcl_bl_ac`
    FOR EACH ROW BEGIN
  DECLARE insert_values TEXT DEFAULT '';
  DECLARE changesFlag BOOLEAN DEFAULT FALSE;
  DECLARE fileStatus VARCHAR(2);
  DECLARE notiStatusFlag BOOLEAN DEFAULT FALSE;
  SELECT lfn.status INTO fileStatus FROM lcl_file_number lfn WHERE lfn.id=new.file_number_id;
    IF (new.ar_gl_mapping_id <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Code', (SELECT charge_code FROM gl_mapping WHERE id = new.ar_gl_mapping_id)) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.ar_amount <> 0.00) THEN
    SET insert_values = concat_insert_values (insert_values, ' Charge Amount', new.ar_amount) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.ap_amount <> 0.00) THEN
    SET insert_values = concat_insert_values (insert_values, ' Cost Amount', new.ap_amount) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.rate_per_weight_unit <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Rate Weight', new.rate_per_weight_unit) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.rate_per_volume_unit <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Rate Measure', new.rate_per_volume_unit) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.rate_flat_minimum <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Rate Minimum', new.rate_flat_minimum) ;
    SET changesFlag = TRUE ;
  END IF ;
          IF (new.sp_acct_no <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Vendor Name', (SELECT acct_name FROM trading_partner WHERE acct_no = new.sp_acct_no)) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.sp_acct_no <> '') THEN
     SET insert_values = concat_insert_values (insert_values, ' Vendor Number', new.sp_acct_no) ;
     SET changesFlag = TRUE ;
  END IF ;
  IF (new.invoice_number <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Invoice Number', new.invoice_number) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (insert_values <> '') THEN
     IF (fileStatus='M') THEN
     SELECT IF(COUNT(*)>0,TRUE,FALSE) INTO notiStatusFlag FROM lcl_export_notification len
       WHERE len.file_number_id=new.file_number_id AND len.file_status='Changes' AND len.status='Pending';
     IF (notiStatusFlag=FALSE) THEN
        INSERT INTO lcl_export_notification (file_number_id, file_status,STATUS,entered_datetime,entered_by_user_id)
        VALUES(new.file_number_id,'Changes','Pending',NOW(),new.modified_by_user_id);
     END IF;
     END IF;
        SET insert_values = CONCAT('INSERTED ->', insert_values) ;
        INSERT INTO lcl_remarks (
      file_number_id,
      TYPE,
      remarks,
      entered_datetime,
      entered_by_user_id,
      modified_datetime,
      modified_by_user_id
    )
    VALUES (
      new.file_number_id,
      'BL-AutoNotes',
      insert_values,
      NOW(),
      new.entered_by_user_id,
      NOW(),
      new.modified_by_user_id
    ) ;
  END IF ;
 END;
$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `lcl_consolidation_insert_trigger`$$

CREATE
    TRIGGER `lcl_consolidation_insert_trigger` AFTER INSERT ON `lcl_consolidation`
    FOR EACH ROW BEGIN
    DECLARE insert_values TEXT;
    SET insert_values = concat_insert_values(insert_values,' Consolidated',(SELECT file_number FROM lcl_file_number WHERE id=new.lcl_file_number_id_b));
    INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
    VALUES(new.lcl_file_number_id_a,'DR-AutoNotes',insert_values,NOW(),new.entered_by_user_id,NOW(),new.modified_by_user_id);
    SET insert_values = '';
    SET insert_values = concat_insert_values(insert_values,' Consolidated',(SELECT file_number FROM lcl_file_number WHERE id=new.lcl_file_number_id_a));
    INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
    VALUES(new.lcl_file_number_id_b,'DR-AutoNotes',insert_values,NOW(),new.entered_by_user_id,NOW(),new.modified_by_user_id);
    END;
$$

DELIMITER ;

--Mantis item 10833 by Vellaisamy on 30 April 2016
INSERT INTO property(NAME,VALUE,TYPE,description) VALUES('ECU.carrier','Ecu Worldwide','common','Ecu Worldwide Carrier');
INSERT INTO property(NAME,VALUE,TYPE,description) VALUES('Econo.carrier','Econocaribe','common','Econo Carrier');

-- Mantis Item# 11093  by Aravindhan.V on 2 May 2016
DROP FUNCTION IF EXISTS GetConoslidatedFiles;

DELIMITER $$
DROP FUNCTION IF EXISTS `getHouseBLForConsolidateDr`$$

CREATE FUNCTION `getHouseBLForConsolidateDr`(inputFileId BIGINT) RETURNS BIGINT(20)
    READS SQL DATA
    DETERMINISTIC
MAIN : BEGIN
   DECLARE result_fileId BIGINT DEFAULT 0;
   
   IF (ISNULL(inputFileId) = FALSE AND inputFileId > 0 ) THEN 
     SELECT IF(lcl_file_number_id_a <> lcl_file_number_id_b, lcl_file_number_id_b,0) 
     INTO result_fileId FROM lcl_consolidation  WHERE lcl_file_number_id_a = inputFileId limit 1;	
   END IF ;
   IF (ISNULL(result_fileId) = TRUE OR result_fileId <= 0 )  THEN  
   SELECT inputFileId INTO result_fileId ;
   END IF ;
   RETURN result_fileId;
 END MAIN$$

DELIMITER ;

-- mantis item# 11093 by Aravindhan.V on 3 May 2016
drop trigger if exists lcl_consolidation_insert_trigger;
-- Mantis#10433 Mei on 4May2016
INSERT INTO scan_config (screen_name,document_type,document_name,file_location)
VALUES ('LCL EXPORTS DR',NULL,'BL VOID',NULL);

-- Mantis#5337 Priyanka on 4th may 2016
ALTER TABLE `quotation` ADD COLUMN `chassis_charge` VARCHAR(1) DEFAULT 'N';

ALTER TABLE `booking_fcl` ADD COLUMN `chassis_charge` VARCHAR(1) DEFAULT 'N';

INSERT INTO property(NAME,VALUE,TYPE,description) VALUES('chassis.cost','50','FCL','FCL EXPORT Chassis Cost');
INSERT INTO property(NAME,VALUE,TYPE,description) VALUES('chassis.sell','50','FCL','FCL EXPORT Chassis Sell');
--  Mantis Item-11845(Logiware LCL-Imports) by Stefy on 4 May 2016
ALTER TABLE `terminal` CHANGE COLUMN `imports_contact_email` `imports_contact_email` VARCHAR(50);

-- Mantis Item-8062(Logiware Accounting) by Nambu on 4 May 2016
INSERT INTO `property`(NAME,VALUE,TYPE,description) VALUES('Auto.Emails.FreightPayments',
'freightcashier@econocaribe.com','ACCOUNTING','Auto Notification Emails for Freight Payments');

--Mantis #5337 by Priyanka on 4th may 2016

DELIMITER $$
DROP TRIGGER /*!50032 IF EXISTS */ `gen_tr_QuatationAudit_U`$$

CREATE TRIGGER `gen_tr_QuatationAudit_U` AFTER UPDATE ON `quotation` 
    FOR EACH ROW BEGIN
	DECLARE updated_values TEXT;
	DECLARE table_name VARCHAR(100);
			DECLARE table_id BIGINT;
			IF (OLD.description != NULL OR OLD.description != '') AND NEW.description != OLD.description THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Commodity Description',OLD.description,NEW.description));
			END IF;
			IF (OLD.origin_terminal != NULL OR OLD.origin_terminal != '') AND NEW.origin_terminal != OLD.origin_terminal THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Origin',OLD.origin_terminal,NEW.origin_terminal));
			END IF;
			IF (OLD.PLOR !=  NULL OR OLD.PLOR != '') AND NEW.PLOR != OLD.PLOR THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'POL',OLD.PLOR,NEW.PLOR));
			END IF;
			IF (OLD.poe != NULL OR OLD.poe != '') AND NEW.poe != OLD.poe THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'poe',OLD.poe,NEW.poe));
			END IF;
			IF (OLD.finaldestination != NULL OR OLD.finaldestination != '') AND NEW.finaldestination != OLD.finaldestination THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'POD',OLD.finaldestination,NEW.finaldestination));
			END IF;
			IF (OLD.destination_port != NULL OR OLD.destination_port != '') AND NEW.destination_port != OLD.destination_port THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Destination',OLD.destination_port,NEW.destination_port));
			END IF;
			IF (OLD.clientname != NULL OR OLD.clientname != '') AND NEW.clientname != OLD.clientname THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Client Name',OLD.clientname,NEW.clientname));
			END IF;
			IF (OLD.sslname != NULL OR OLD.sslname != '') AND NEW.sslname != OLD.sslname THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'SSL Name',OLD.sslname,NEW.sslname));
			END IF;
			IF (OLD.clientnumber != NULL OR OLD.clientnumber != '') AND NEW.clientnumber != OLD.clientnumber THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Acct NO',OLD.clientnumber,NEW.clientnumber));
			END IF;
			IF (OLD.clienttype != NULL OR OLD.clienttype != '') AND NEW.clienttype != OLD.clienttype THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Client Type',OLD.clienttype,NEW.clienttype));
			END IF;
			IF (OLD.contactname != NULL OR OLD.contactname != '') AND NEW.contactname != OLD.contactname THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Contact Name',OLD.contactname,NEW.contactname));
			END IF;
			IF (OLD.email != NULL OR OLD.email != '') AND NEW.email != OLD.email THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Email',OLD.email,NEW.email));
			END IF;
			IF (OLD.phone != NULL OR OLD.phone != '') AND NEW.phone != OLD.phone THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Phone',OLD.phone,NEW.phone));
			END IF;
			IF (OLD.fax != NULL OR OLD.fax != '') AND NEW.fax != OLD.fax THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Fax',OLD.fax,NEW.fax));
			END IF;
			IF (OLD.typeofMove != NULL OR OLD.typeofMove != '') AND NEW.typeofMove != OLD.typeofMove THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Typeof Move',OLD.typeofMove,NEW.typeofMove));
			END IF;
			IF (OLD.ramp_city != NULL OR OLD.ramp_city != '') AND NEW.ramp_city != OLD.ramp_city THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'ramp_city',OLD.ramp_city,NEW.ramp_city));
			END IF;
			IF (OLD.Zip != NULL OR OLD.Zip != '') AND NEW.Zip != OLD.Zip THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Zip',OLD.Zip,NEW.Zip));
			END IF;
			IF (OLD.no_days != NULL OR OLD.no_days != '') AND NEW.no_days != OLD.no_days THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Transit Days',OLD.no_days,NEW.no_days));
			END IF;
			IF (OLD.issuing_terminal != NULL OR OLD.issuing_terminal != '') AND NEW.issuing_terminal != OLD.issuing_terminal THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Iss Term',OLD.issuing_terminal,NEW.issuing_terminal));
			END IF;
			IF (OLD.default_agent != NULL OR OLD.default_agent != '') AND NEW.default_agent != OLD.default_agent THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Default Agent',OLD.default_agent,NEW.default_agent));
			END IF;
			IF (OLD.agent != NULL OR OLD.agent != '') AND NEW.agent != OLD.agent THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Agent Name',OLD.agent,NEW.agent));
			END IF;
			IF (OLD.agent_no != NULL OR OLD.agent_no != '') AND NEW.agent_no != OLD.agent_no THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Agent No',OLD.agent_no,NEW.agent_no));
			END IF;
			IF (OLD.routedbymsg != NULL OR OLD.routedbymsg != '') AND NEW.routedbymsg != OLD.routedbymsg THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Routed by Agent',OLD.routedbymsg,NEW.routedbymsg));
			END IF;
			IF (OLD.routedby_agents_country != NULL OR OLD.routedby_agents_country != '') AND NEW.routedby_agents_country != OLD.routedby_agents_country THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Routed By Agents Country',OLD.routedby_agents_country,NEW.routedby_agents_country));
			END IF;
			IF (OLD.rates_remarks != NULL OR OLD.rates_remarks != '') AND NEW.rates_remarks != OLD.rates_remarks THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Rates Remarks',OLD.rates_remarks,NEW.rates_remarks));
			END IF;
			IF (OLD.carrier_phone != NULL OR OLD.carrier_phone != '') AND NEW.carrier_phone != OLD.carrier_phone THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Phone',OLD.carrier_phone,NEW.carrier_phone));
			END IF;
			IF (OLD.hazmat != NULL OR OLD.hazmat != '') AND NEW.hazmat != OLD.hazmat THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Hazmat',OLD.hazmat,NEW.hazmat));
			END IF;
			IF (OLD.ssline != NULL OR OLD.ssline != '') AND NEW.ssline != OLD.ssline THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'SSL Acct No',OLD.ssline,NEW.ssline));
			END IF;
			IF (OLD.carrier_fax != NULL OR  OLD.carrier_fax != '') AND NEW.carrier_fax != OLD.carrier_fax THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Fax',OLD.carrier_fax,NEW.carrier_fax));
			END IF;
			IF (OLD.carrier_contact != NULL OR OLD.carrier_contact != '') AND NEW.carrier_contact != OLD.carrier_contact THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Cont Name',OLD.carrier_contact,NEW.carrier_contact));
			END IF;
			IF (OLD.carrier_email != NULL OR OLD.carrier_email != '') AND NEW.carrier_email != OLD.carrier_email THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Email',OLD.carrier_email,NEW.carrier_email));
			END IF;
			IF (OLD.carrier_print != NULL OR OLD.carrier_print != '') AND NEW.carrier_print != OLD.carrier_print THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Carrier Print',OLD.carrier_print,NEW.carrier_print));
			END IF;
			IF (OLD.commodity_print != NULL OR OLD.commodity_print != '') AND NEW.commodity_print != OLD.commodity_print THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Commodity Print',OLD.commodity_print,NEW.commodity_print));
			END IF;
			IF (OLD.print_desc != NULL OR OLD.print_desc != '') AND NEW.print_desc != OLD.print_desc THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Print Desc',OLD.print_desc,NEW.print_desc));
			END IF;
			IF (OLD.specialequipment != NULL OR OLD.specialequipment != '') AND NEW.specialequipment != OLD.specialequipment THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Special Equipment',OLD.specialequipment,NEW.specialequipment));
			END IF;
			IF (OLD.outofgage != NULL OR  OLD.outofgage != '') AND NEW.outofgage != OLD.outofgage THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Outof Gage',OLD.outofgage,NEW.outofgage));
			END IF;
			IF (OLD.localdryage != NULL OR OLD.localdryage != '') AND NEW.localdryage != OLD.localdryage THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Local Dryage',OLD.localdryage,NEW.localdryage));
			END IF;
			IF (OLD.intermodel != NULL OR OLD.intermodel != '') AND NEW.intermodel != OLD.intermodel THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Intermodal',OLD.intermodel,NEW.intermodel));
			END IF;
			IF (OLD.insurance != NULL OR OLD.insurance != NULL) AND NEW.insurance != OLD.insurance THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Insurance',OLD.insurance,NEW.insurance));
			END IF;
			IF (OLD.spcl_eqpmt != NULL OR OLD.spcl_eqpmt != '') AND NEW.spcl_eqpmt != OLD.spcl_eqpmt THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Spcl Eqpmt',OLD.spcl_eqpmt,NEW.spcl_eqpmt));
			END IF;
			IF (OLD.amount != NULL OR OLD.amount != '') AND NEW.amount != OLD.amount THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Loc Drayage Amount',OLD.amount,NEW.amount));
			END IF;
			IF (OLD.drayage_markup != NULL OR  OLD.drayage_markup != '') AND NEW.drayage_markup != OLD.drayage_markup THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Drayage Markup',OLD.drayage_markup,NEW.drayage_markup));
			END IF;
			IF (OLD.amount1 != NULL OR OLD.amount1 != '') AND NEW.amount1 != OLD.amount1 THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Intermodal Amount',OLD.amount1,NEW.amount1));
			END IF;
			IF (OLD.intermodal_markup != NULL OR OLD.intermodal_markup != '') AND NEW.intermodal_markup != OLD.intermodal_markup THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Intermodal Markup',OLD.intermodal_markup,NEW.intermodal_markup));
			END IF;
			IF (OLD.costofgoods != NULL OR OLD.costofgoods != '') AND NEW.costofgoods != OLD.costofgoods THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Cost of Goods',OLD.costofgoods,NEW.costofgoods));
			END IF;
			IF (OLD.insurance_markup != NULL OR OLD.insurance_markup != '') AND NEW.insurance_markup != OLD.insurance_markup THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Insurance Markup',OLD.insurance_markup,NEW.insurance_markup));
			END IF;
			IF (OLD.soc != NULL OR OLD.soc != '') AND NEW.soc != OLD.soc THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Soc',OLD.soc,NEW.soc));
			END IF;
			IF (OLD.customertoprovideSED != NULL OR OLD.customertoprovideSED != '')  AND NEW.customertoprovideSED != OLD.customertoprovideSED THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'CustomertoprovideSED',OLD.customertoprovideSED,NEW.customertoprovideSED));
			END IF;
			IF (OLD.deductFFcomm != NULL OR OLD.deductFFcomm != '') AND NEW.deductFFcomm != OLD.deductFFcomm THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Auto Deduct FF Comm',OLD.deductFFcomm,NEW.deductFFcomm));
			END IF;
			IF (OLD.insurance_charge != NULL OR OLD.insurance_charge != '') AND NEW.insurance_charge != OLD.insurance_charge THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Insurance Charge',OLD.insurance_charge,NEW.insurance_charge));
			END IF;
			IF (OLD.pier_pass != NULL OR OLD.pier_pass !='') AND NEW.pier_pass != OLD.pier_pass THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'PierPass',OLD.pier_pass,NEW.pier_pass));
			END IF;
			IF (OLD.comment != NULL OR OLD.comment != '') AND NEW.comment != OLD.comment THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Remarks',OLD.comment,NEW.comment));
			END IF;
			IF (OLD.goodsdesc != NULL OR OLD.goodsdesc != '') AND NEW.goodsdesc != OLD.goodsdesc THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Goods Description',OLD.goodsdesc,NEW.goodsdesc));
			END IF;
			IF (OLD.door_origin != NULL OR OLD.door_origin != '') AND NEW.door_origin != OLD.door_origin THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Door Origin',OLD.door_origin,NEW.door_origin));
			END IF;
			IF (OLD.door_destination != NULL OR OLD.door_destination != '') AND NEW.door_destination != OLD.door_destination THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Door Destination',OLD.door_destination,NEW.door_destination));
			END IF;
			IF (OLD.file_type != NULL OR OLD.file_type!= '') AND NEW.file_type != OLD.file_type THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'File Type',OLD.file_type,NEW.file_type));
			END IF;
			IF (OLD.aes_filling != NULL OR OLD.aes_filling != '') AND NEW.aes_filling != OLD.aes_filling THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Is OTI Filing AES',OLD.aes_filling,NEW.aes_filling));
			END IF;
			IF (OLD.brand != NULL OR OLD.brand != '') AND NEW.brand != OLD.brand THEN
			  SET updated_values = (SELECT `concat_string`(updated_values,'Brand',OLD.brand,NEW.brand));
			END IF;
			IF updated_values IS NOT NULL THEN
			  INSERT INTO notes(module_id,module_ref_id,updateDate,note_type,note_desc,updated_by) VALUES('FILE',NEW.file_no,NOW(),'auto',updated_values,NEW.update_by);
			END IF;
    END;
$$

DELIMITER ;

DELIMITER $$

--Mantis #11781 by Vellaisamy on 4th may 2016 -- This query only for Local and Econo
UPDATE genericcode_dup 
SET codedesc = 'All in-bond merchandise not cleared by Customs and Border Patrol will be Reported for General Order Storage as per Customs and Border Patrol regulations within 15 days of arrival.'
WHERE codetypeid = 39 AND CODE = 'AN405';
/**End**/
-- Mantis#11829 5May2016
UPDATE property SET TYPE='LCL' WHERE NAME='lcl.inttra.xmlLocation';
UPDATE property SET TYPE='LCL' WHERE NAME='lcl.gtnexus.xmlLocation';
UPDATE property SET TYPE='LCL' WHERE NAME='application.CTSDoorDeliveryWebServiceUID';
UPDATE property SET TYPE='LCL' WHERE NAME='application.CTSDoorDeliveryCostUID';
UPDATE property SET TYPE='LCL' WHERE NAME='application.CTSWebServiceUID';
UPDATE property SET TYPE='LCL' WHERE NAME='application.CTSCostUID';

-- General Issue by Nambu on 5th may 2016 

DELIMITER $$

DROP FUNCTION IF EXISTS `BLGetIsDisputed`$$

CREATE  FUNCTION `BLGetIsDisputed`( pBlNo VARCHAR(25), pVendorNo VARCHAR(20) ) RETURNS TINYINT(1)
    READS SQL DATA
    DETERMINISTIC
MAIN: BEGIN
	DECLARE mReturnData BOOLEAN DEFAULT FALSE;
	IF ( ISNULL( pBlNo ) = TRUE OR TRIM( pBlNo ) = '' OR ISNULL( pVendorNo ) = TRUE OR TRIM( pVendorNo ) = '' ) THEN
	  RETURN FALSE;
	END IF; 
	
	IF ( pBlNo LIKE '%-04-%' ) THEN
	  RETURN `FCLBLGetIsDisputed`(pBlNo, pVendorNo);
	END IF;
	
	RETURN mReturnData;
END MAIN$$

DELIMITER ;


DELIMITER $$

DROP FUNCTION IF EXISTS `FCLBLGetIsDisputed`$$

CREATE FUNCTION `FCLBLGetIsDisputed`( pBlNo VARCHAR(25), pVendorNo VARCHAR(20) ) RETURNS TINYINT(1)
    READS SQL DATA
    DETERMINISTIC
MAIN: BEGIN
	DECLARE mReturnData BOOLEAN DEFAULT FALSE;
	
	IF ( ISNULL( pBlNo ) = TRUE OR TRIM( pBlNo ) = '' OR ISNULL( pVendorNo ) = TRUE OR TRIM( pVendorNo ) = '' ) THEN
		RETURN FALSE;
	END IF; 
	
	SELECT 
	  IF(d.`status` = 'Disputed', IF(f.`steam_ship_bl` = 'C-Collect', f.`agent_no`, f.`ssline_no`) = pVendorNo, FALSE)
	INTO
	  mReturnData
	FROM 
	  `fcl_bl` f 
          JOIN `document_store_log` d 
            ON (
               d.`document_name` = 'SS LINE MASTER BL'
               AND d.`document_id` = f.file_no
            ) 
        WHERE f.`bolid` = pBlNo
          AND f.`received_master` = 'Yes'
        ORDER BY d.`id` DESC LIMIT 1;
	
	RETURN mReturnData;
END MAIN$$

DELIMITER ;
--- Mantis#11657 5May2016
INSERT INTO property(NAME,VALUE,TYPE,description) VALUES('barrel.email','ship@econocaribe.com','LCL','BarrelDR Email');

-- Mantis-11793(FCL Exports) by Kuppusamy on 9th may 2016
ALTER TABLE `terminal`
  ADD COLUMN `fclexp_iss_term` VARCHAR(1) DEFAULT 'N'   NOT NULL AFTER `zaccount`;


-- Functions need to derive gl account and validate
delimiter $$

drop function if exists `SystemRulesGetRuleName`$$

create function `SystemRulesGetRuleName`(
  pRuleCode varchar(30)
) returns varchar(100) charset latin1
    reads sql data
    deterministic
MAIN: begin
    declare mRuleName varchar(100) default null;
    if pRuleCode <> '' then
        select 
            sr.`rule_name` 
        into 
            mRuleName 
        from 
            `system_rules` sr
        where 
            sr.`rule_code` = pRuleCode
        limit 1;
    end if;
    return mRuleName;
end MAIN$$

delimiter ;

delimiter $$

drop function if exists `TerminalGLMapingGetTerminal`$$

create function `TerminalGLMapingGetTerminal`(
  pTerminal int(10),
  pShipmentType varchar(10),
  pType varchar(2)
) returns int(10)
    reads sql data
    deterministic
MAIN: begin
    declare mTerminal int(10) default null;
    if pTerminal <> '' then
        select 
          coalesce(
            if(
              pShipmentType = 'FCLE', 
              if(
                pType = 'B',
                tm.`fcl_export_billing`,
                if(
                  pType = 'L',
                  tm.`fcl_export_loading`,
                  if(
                    pType = 'D',
                    tm.`fcl_export_dockreceipt`,
                    null
                  )
                )
              ),
              if(
                pShipmentType = 'FCLI', 
                if(
                  pType = 'B',
                  tm.`fcl_import_billing`,
                  if(
                    pType = 'L',
                    tm.`fcl_import_loading`,
                    null
                  )
                ),
                if(
                  pShipmentType = 'LCLE', 
                  if(
                    pType = 'B',
                    tm.`lcl_export_billing`,
                    if(
                      pType = 'L',
                      tm.`lcl_export_loading`,
                      if(
                        pType = 'D',
                        tm.`lcl_export_dockreceipt`,
                        null
                      )
                    )
                  ),
                  if(
                    pShipmentType = 'LCLI', 
                    if(
                      pType = 'B',
                      tm.`lcl_import_billing`,
                      if(
                        pType = 'L',
                        tm.`lcl_import_loading`,
                        null
                      )
                    ),
                    if(
                      pShipmentType = 'AIRE', 
                      if(
                        pType = 'B',
                        tm.`air_export_billing`,
                        if(
                          pType = 'L',
                          tm.`air_export_loading`,
                          if(
                            pType = 'D',
                            tm.`air_export_dockreceipt`,
                            null
                         )
                       )
                     ),
                     if(
                       pShipmentType = 'AIRI', 
                       if(
                         pType = 'B',
                         tm.`air_import_billing`,
                         if(
                           pType = 'L',
                           tm.`air_import_loading`,
                           null
                         )
                       ),
                       if(
                         pShipmentType = 'INLE', 
                         if(
                           pType = 'L',
                           tm.`inland_export_loading`,
                           null
                         ),
                         null
                       )
                     )
                    )
                  )
                )
              )
            ),
            tm.`terminal`
          )
        into 
            mTerminal 
        from 
            `terminal_gl_mapping` tm
        where 
            tm.`terminal` = pTerminal
        limit 1;
    end if;
    return mTerminal;
end MAIN$$

delimiter ;

delimiter $$

drop function if exists `DeriveGlAccount`$$

create function `DeriveGlAccount`(
  pChargeCode varchar (30),
  pShipmentType varchar (10),
  pTransactionType varchar (10),
  pTerminal varchar(5)
) returns varchar(20) charset latin1
    reads sql data
    deterministic
MAIN : begin
  declare mAccount varchar (20) default null ;
  if (pChargeCode <> '' and pShipmentType <> '' and pTransactionType <> '' and pTerminal <> '') then 
    select
      concat_ws('-', 
        `SystemRulesGetRuleName`('CompanyCode'),
        gl.`gl_acct`,
        if(gl.`derive_yn` = 'Y',
          lpad(`TerminalGLMapingGetTerminal`(pTerminal, pShipmentType, gl.`suffix_value`), 2, '0'),
          gl.`suffix_value`
        )
      )
    into
      mAccount
    from
      `gl_mapping` gl
    where 
      gl.`charge_code` = pChargeCode
      and gl.`shipment_type` = pShipmentType
      and gl.`transaction_type` = pTransactionType
      and gl.`bluescreen_chargecode` <> ''
    limit 1;
  end if ;
  return mAccount ;
end MAIN$$

delimiter ;

delimiter $$

drop function if exists `IsValidGlAccount`$$

create function `IsValidGlAccount`(
  pAccount varchar(20)
) returns tinyint(1)
    reads sql data
    deterministic
MAIN : begin
  declare mValid tinyint(1) default 0;
  if (pAccount <> '') then 
    select
      (count(*) > 0)
    into
      mValid
    from
      `account_details` ad
    where 
      ad.account = pAccount
    limit 1;
  end if ;
  return mValid ;
end MAIN$$

delimiter ;

-- Mantis-11793(FCL Exports) by Kuppusamy on 11th may 2016
ALTER TABLE `terminal`
  DROP COLUMN `fclexp_iss_term`;


ALTER TABLE `terminal`
  ADD COLUMN `fcl_exp_iss_term` VARCHAR(1) DEFAULT 'N'   NOT NULL AFTER `zaccount`;


--- Lcl Exports Mantis #11773 by sathiya on May 12 2016

ALTER TABLE `lcl_port_configuration` ADD COLUMN `print_imp_on_metric` TINYINT(1) DEFAULT 0 NOT NULL AFTER `force_agent_released_dr`; 

ALTER TABLE `role_duties` ADD COLUMN `prevent_expRelease` TINYINT(1) DEFAULT 0 NULL AFTER `lcl_wghtchange_release`; 

-- Lcl Exports Mnatis Item# 11613 on 13th May 2016 by Aravindhan V
update  property set  name='Destination Services DAP/DDP/Delivery min profit'  where name = 'Destination Services minimum profit';

 update  property set  name='Destination Services DAP/DDP/Delivery max profit'  where name = 'Destination Services maximum profit';
 
INSERT INTO property(NAME,VALUE,TYPE,description) VALUES ('Destination Services O/C Min Profit',50,'LCL','ON-Carriage Min Profit');

INSERT INTO property(NAME,VALUE,TYPE,description) VALUES  ('Destination Services O/C Max Profit',200,'LCL','ON-Carriage Max Profit');

--- Lcl Exports Mantis #11773 by Sathiya on May 13 2016

ALTER TABLE `role_duties` DROP COLUMN `prevent_expRelease`;
  
ALTER TABLE `role_duties` ADD COLUMN `prevent_exp_release` TINYINT(1) DEFAULT 0 NULL AFTER `lcl_wghtchange_release`; 

-- LCL Exports Mantis # 11773 by sathiya on May 16 2016

ALTER TABLE lcl_port_configuration CHANGE  print_imp_on_metric  print_imp_on_metric  TINYINT(1) DEFAULT 0 NULL; 

-- LCL Exports mantis item# 11877 on May_16_2016 by Aravindhan_V

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBl_update`$$

CREATE TRIGGER `after_LclBl_update` AFTER UPDATE ON `lcl_bl`
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT;
    DECLARE fileStatus VARCHAR(2);
    DECLARE notiStatusFlag BOOLEAN DEFAULT FALSE;
    DECLARE bl_values TEXT ;
    DECLARE changesFlag BOOLEAN DEFAULT FALSE;
    SELECT lfn.status INTO fileStatus FROM lcl_file_number lfn WHERE lfn.id=OLD.file_number_id;
     IF ((old.ship_acct_no IS NULL AND new.ship_acct_no IS NOT NULL)OR new.ship_acct_no!=old.ship_acct_no OR(new.ship_acct_no IS NULL AND old.ship_acct_no IS NOT NULL))THEN
    SET @oldClientValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.ship_acct_no);
    SET @newClientValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.ship_acct_no);
    SET updated_values=(SELECT concat_string (updated_values,'Shipper',IFNULL(@oldClientValues,''),IFNULL(@newClientValues,'')));
    SET changesFlag = TRUE ;
    END IF;
     IF ((OLD.cons_acct_no IS NULL AND NEW.cons_acct_no IS NOT NULL) OR NEW.cons_acct_no != OLD.cons_acct_no OR (OLD.cons_acct_no IS NOT NULL AND NEW.cons_acct_no IS NULL)) THEN
     SET @oldConsValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.cons_acct_no);
     SET @newConsValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.cons_acct_no);
     SET updated_values=(SELECT concat_string(updated_values,'Consignee',IFNULL(@oldConsValues,''),IFNULL(@newConsValues,'')));
     SET changesFlag = TRUE ;
   END IF;
    IF ((OLD.noty_acct_no IS NULL AND NEW.noty_acct_no IS NOT NULL) OR NEW.noty_acct_no != OLD.noty_acct_no OR (OLD.noty_acct_no IS NOT NULL AND NEW.noty_acct_no IS NULL)) THEN
     SET @oldNotyValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.noty_acct_no);
     SET @newNotyValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.noty_acct_no);
     SET updated_values=(SELECT concat_string(updated_values,'Notify Party',IFNULL(@oldNotyValues,''),IFNULL(@newNotyValues,'')));
     SET changesFlag = TRUE ;
   END IF;
     IF ((old.fwd_acct_no IS NULL AND new.fwd_acct_no IS NOT NULL)OR new.fwd_acct_no!=old.fwd_acct_no OR(new.fwd_acct_no IS NULL AND old.fwd_acct_no IS NOT NULL))THEN
    SET @oldClientValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.fwd_acct_no);
    SET @newClientValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.fwd_acct_no);
    SET updated_values=(SELECT concat_string (updated_values,'Forwarder',IFNULL(@oldClientValues,''),IFNULL(@newClientValues,'')));
    SET changesFlag = TRUE ;
    END IF;
   IF ((OLD.third_party_acct_no IS NULL AND NEW.third_party_acct_no IS NOT NULL) OR NEW.third_party_acct_no != OLD.third_party_acct_no OR (OLD.third_party_acct_no IS NOT NULL AND NEW.third_party_acct_no IS NULL)) THEN
     SET @oldThirdValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.third_party_acct_no);
     SET @newThirdValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.third_party_acct_no);
     SET updated_values=(SELECT concat_string(updated_values,'Third Party',IFNULL(@oldThirdValues,''),IFNULL(@newThirdValues,'')));
     SET changesFlag = TRUE ;
   END IF;
     IF ((OLD.sup_acct_no IS NULL AND NEW.sup_acct_no IS NOT NULL) OR NEW.sup_acct_no!=OLD.sup_acct_no OR (OLD.sup_acct_no IS NOT NULL AND NEW.sup_acct_no IS NULL)) THEN
   SET @oldSupValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.sup_acct_no);
   SET @newSupValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.sup_acct_no);
   SET updated_values=(SELECT concat_string(updated_values,'Supplier',IFNULL(@oldSupValues,''),IFNULL(@newSupValues,'')));
   END IF;
   IF new.billing_terminal!=old.billing_terminal THEN
    SET @oldBillingTerminal=(SELECT CONCAT(terminal_location,'/',trmnum) FROM terminal WHERE trmnum=old.billing_terminal);
    SET @newBillingTerminal=(SELECT CONCAT(terminal_location,'/',trmnum) FROM terminal WHERE trmnum=new.billing_terminal);
    SET updated_values=(SELECT concat_string(updated_values,'Term to do BL',IFNULL(@oldBillingTerminal,''),IFNULL(@newBillingTerminal,'')));
   END IF;
      IF new.terms_type1!=old.terms_type1 THEN
       SET updated_values=(SELECT concat_string(updated_values,'Termstype1',old.terms_type1,new.terms_type1));
    END IF;
 
        IF new.terms_type2!=old.terms_type2 THEN
       SET updated_values=(SELECT concat_string(updated_values,'Termstype2',old.terms_type2,new.terms_type2));
    END IF;
         IF new.type2_date!=old.type2_date THEN
       SET updated_values=(SELECT concat_string(updated_values,'Type2Date',old.type2_date,new.type2_date));
    END IF;
         IF new.point_of_origin!=old.point_of_origin THEN
       SET updated_values=(SELECT concat_string(updated_values,'PointOfOrigin',old.point_of_origin,new.point_of_origin));
    END IF;
    IF new.bl_owner_id!=old.bl_owner_id THEN
     SET bl_values=(SELECT concat_string(updated_values,'BL Owner is Changed',(SELECT login_name FROM user_details WHERE user_id=old.bl_owner_id),(SELECT login_name FROM user_details WHERE user_id=new.bl_owner_id)));
       INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
       VALUES(OLD.file_number_id,'BL-AutoNotes',bl_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    IF old.poo_id!=new.poo_id THEN
	SET updated_values=(SELECT concat_string(updated_values,'PlaceOfReceipt',(SELECT un_loc_name FROM un_location WHERE id=old.poo_id),(SELECT un_loc_name FROM un_location WHERE id=new.poo_id)));
    END IF;
   IF old.fd_id!=new.fd_id THEN
	SET updated_values=(SELECT concat_string(updated_values,'Destination',(SELECT un_loc_name FROM un_location WHERE id=old.fd_id),(SELECT un_loc_name FROM un_location WHERE id=new.fd_id)));
	SET changesFlag = TRUE ;
    END IF;
          IF new.spot_rate!=old.spot_rate THEN
      SET updated_values=(SELECT concat_string(updated_values,'SpotRate',IF(old.spot_rate=0,'No','Yes'),IF(new.spot_rate=0,'No','Yes')));
    END IF;
    IF new.rate_type!=old.rate_type THEN
    SET updated_values=(SELECT concat_string(updated_values,'RateType',old.rate_type,new.rate_type));
    END IF;
    IF ((OLD.booked_ss_header_id IS NULL AND NEW.booked_ss_header_id IS NOT NULL) OR NEW.booked_ss_header_id!=OLD.booked_ss_header_id OR (OLD.booked_ss_header_id IS NOT NULL AND NEW.booked_ss_header_id IS NULL)) THEN
     SET @oldScheduleValue = (SELECT CONCAT (l.`schedule_no` ,' / ', DATE_FORMAT((SELECT d.std FROM lcl_ss_detail d WHERE d.ss_header_id=old.booked_ss_header_id  AND d.trans_mode='V'),'%d-%b-%Y'))  FROM lcl_ss_header  l  WHERE  l.id=old.booked_ss_header_id);
     SET @newScheduleValue = (SELECT CONCAT (l.`schedule_no` ,' / ', DATE_FORMAT((SELECT d.std FROM lcl_ss_detail d WHERE d.ss_header_id=new.booked_ss_header_id  AND d.trans_mode='V'),'%d-%b-%Y'))  FROM lcl_ss_header  l  WHERE  l.id=new.booked_ss_header_id);
     SET updated_values=(SELECT concat_string(updated_values,'Booked Voyage is changed',IFNULL(@oldScheduleValue,''),IFNULL(@newScheduleValue,'')) );
       SET changesFlag = TRUE ;
    END IF;
    IF new.billing_type!=old.billing_type THEN
    SET updated_values=(SELECT concat_string(updated_values,'Terms',old.billing_type,new.billing_type));
    SET changesFlag = TRUE ;
    END IF;
     IF new.billing_type!=old.billing_type THEN
      SET changesFlag = TRUE ;
    END IF;
     IF new.bill_to_party!=old.bill_to_party THEN
      SET changesFlag = TRUE ;
    END IF;
    IF new.free_bl!=old.free_bl THEN
    SET updated_values=(SELECT concat_string(updated_values,'Free B/L',IF(old.free_bl=0,'No','Yes'),IF(new.free_bl=0,'No','Yes')));
    SET changesFlag = TRUE ;
    END IF;
    IF updated_values IS NOT NULL THEN
    SET updated_values=CONCAT('UPDATED ->',updated_values);
       IF (fileStatus='M'  AND changesFlag = TRUE) THEN
       SELECT IF(COUNT(*)>0,TRUE,FALSE) INTO notiStatusFlag FROM lcl_export_notification len
       WHERE len.file_number_id=OLD.file_number_id AND len.file_status='Changes' AND len.status='Pending';
       IF (notiStatusFlag=FALSE) THEN
        INSERT INTO lcl_export_notification (file_number_id, file_status,STATUS,entered_datetime,entered_by_user_id)
        VALUES(OLD.file_number_id,'Changes','Pending',NOW(),new.modified_by_user_id);
        END IF;
        END IF;
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(OLD.file_number_id,'BL-AutoNotes',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;

--Mantis item 11149 by priyanka on 17th Mar 2016

DELIMITER $$

DROP PROCEDURE IF EXISTS `MergeFcl`$$

CREATE  PROCEDURE `MergeFcl`(
  IN pDisabledAcctNo VARCHAR (10),
  IN pMergedAcctNo VARCHAR (10),
  IN pMergedAcctName VARCHAR (50),
  IN pMergedAcctType VARCHAR (20),
  IN pMergedAcctContact VARCHAR (50),
  IN pMergedAcctCoName VARCHAR (50),
  IN pMergedAcctAddress TEXT,
  IN pMergedAcctCity VARCHAR (50),
  IN pMergedAcctState VARCHAR (2),
  IN pMergedAcctZip VARCHAR (20),
  IN pMergedAcctCountry VARCHAR (30),
  IN pMergedAcctPhone VARCHAR (25),
  IN pMergedAcctFax VARCHAR (25),
  IN pMergedAcctEmail1 VARCHAR (50),
  IN pConcattedAddress TEXT
)
    MODIFIES SQL DATA
BEGIN
  UPDATE 
    `fcl_bl` bl 
  SET
    bl.`shipper_no` = pMergedAcctNo,
    bl.`shipper_name` = pMergedAcctName,
    bl.`shipper_address` = pConcattedAddress 
  WHERE bl.`shipper_no` = pDisabledAcctNo ;
  UPDATE 
    `fcl_bl` bl 
  SET
    bl.`house_shipper_no` = pMergedAcctNo,
    bl.`house_shipper_name` = pMergedAcctName,
    bl.`house_shipper_address` = pConcattedAddress 
  WHERE bl.`house_shipper_no` = pDisabledAcctNo ;
  UPDATE 
    `fcl_bl` bl 
  SET
    bl.`consignee_no` = pMergedAcctNo,
    bl.`consignee_name` = pMergedAcctName,
    bl.`consignee_address` = pConcattedAddress 
  WHERE bl.`consignee_no` = pDisabledAcctNo ;
  UPDATE 
    `fcl_bl` bl 
  SET
    bl.`houseconsignee` = pMergedAcctNo,
    bl.`house_consignee_name` = pMergedAcctName,
    bl.`house_consignee_address` = pConcattedAddress 
  WHERE bl.`houseconsignee` = pDisabledAcctNo ;
  UPDATE 
    `fcl_bl` bl 
  SET
    bl.`notify_party` = pMergedAcctNo,
    bl.`notify_party_name` = pMergedAcctName,
    bl.`house_notify_party` = pConcattedAddress 
  WHERE bl.`notify_party` = pDisabledAcctNo ;
  UPDATE 
    `fcl_bl` bl 
  SET
    bl.`house_notify_party_no` = pMergedAcctNo,
    bl.`house_notify_party_name` = pMergedAcctName,
    bl.`streamship_notify_party` = pConcattedAddress 
  WHERE bl.`house_notify_party_no` = pDisabledAcctNo ;
  UPDATE 
    `fcl_bl` bl 
  SET
    bl.`forward_agent_no` = pMergedAcctNo,
    bl.`forwarding_agent_name` = pMergedAcctName,
    bl.`forwarding_agent` = pConcattedAddress 
  WHERE bl.`forward_agent_no` = pDisabledAcctNo ;
  UPDATE 
    `fcl_bl` bl 
  SET
    bl.`billtrdprty` = pMergedAcctNo,
    bl.`third_party_name` = pMergedAcctName 
  WHERE bl.`billtrdprty` = pDisabledAcctNo ;
  UPDATE 
    `fcl_bl` bl 
  SET
    bl.`agent_no` = pMergedAcctNo,
    bl.`agent` = pMergedAcctName 
  WHERE bl.`agent_no` = pDisabledAcctNo ;
  UPDATE 
    `fcl_bl` bl 
  SET
    bl.`routed_by_agent` = pMergedAcctNo 
  WHERE bl.`routed_by_agent` = pDisabledAcctNo ;
  UPDATE 
    `fcl_bl` bl 
  SET
    bl.`ssline_no` = pMergedAcctNo,
    bl.`ssline_name` = pMergedAcctName 
  WHERE bl.`ssline_no` = pDisabledAcctNo ;
  UPDATE 
    `fcl_bl_costcodes` bc 
  SET
    bc.`account_no` = pMergedAcctNo,
    bc.`account_name` = pMergedAcctName 
  WHERE bc.`account_no` = pDisabledAcctNo ;
  UPDATE 
    `fclblcorrections` bc 
  SET
    bc.`account_number` = pMergedAcctNo,
    bc.`account_name` = pMergedAcctName 
  WHERE bc.`account_number` = pDisabledAcctNo ;
  UPDATE 
    `booking_fcl` bk 
  SET
    bk.`shipno` = pMergedAcctNo,
    bk.`shipper` = pMergedAcctName,
    bk.`addressforshipper` = pMergedAcctAddress,
    bk.`shipper_city` = pMergedAcctCity,
    bk.`shipper_state` = pMergedAcctState,
    bk.`shipper_zip` = pMergedAcctZip,
    bk.`shipper_country` = pMergedAcctCountry,
    bk.`shipperphonenumber` = pMergedAcctPhone,
    bk.`shipper_fax` = pMergedAcctFax,
    bk.`shipperemail` = pMergedAcctEmail1 
  WHERE bk.`shipno` = pDisabledAcctNo ;
  UPDATE 
    `booking_fcl` bk 
  SET
    bk.`forwardnumber` = pMergedAcctNo,
    bk.`forward` = pMergedAcctName,
    bk.`addressforforwarder` = pMergedAcctAddress,
    bk.`forwarder_city` = pMergedAcctCity,
    bk.`forwarder_state` = pMergedAcctState,
    bk.`forwarder_zip` = pMergedAcctZip,
    bk.`forwarder_country` = pMergedAcctCountry,
    bk.`forwarderphonenumber` = pMergedAcctPhone,
    bk.`forwarder_fax` = pMergedAcctFax,
    bk.`forwarderemail` = pMergedAcctEmail1 
  WHERE bk.`forwardnumber` = pDisabledAcctNo ;
  UPDATE 
    `booking_fcl` bk 
  SET
    bk.`consigneenumber` = pMergedAcctNo,
    bk.`consignee` = pMergedAcctName,
    bk.`addressforconsingee` = pMergedAcctAddress,
    bk.`consignee_city` = pMergedAcctCity,
    bk.`consignee_state` = pMergedAcctState,
    bk.`consignee_zip` = pMergedAcctZip,
    bk.`consignee_country` = pMergedAcctCountry,
    bk.`consigneephonenumber` = pMergedAcctPhone,
    bk.`consignee_fax` = pMergedAcctFax,
    bk.`consigneeemail` = pMergedAcctEmail1 
  WHERE bk.`consigneenumber` = pDisabledAcctNo ;
  UPDATE 
    `booking_fcl` bk 
  SET
    bk.`account_number` = pMergedAcctNo,
    bk.`account_name` = pMergedAcctName 
  WHERE bk.`account_number` = pDisabledAcctNo ;
  UPDATE 
    `booking_fcl` bk 
  SET
    bk.`agent_no` = pMergedAcctNo,
    bk.`agent` = pMergedAcctName 
  WHERE bk.`agent_no` = pDisabledAcctNo ;
  UPDATE 
    `booking_fcl` bk 
  SET
    bk.`routed_by_agent` = pMergedAcctNo 
  WHERE bk.`routed_by_agent` = pDisabledAcctNo ;
  UPDATE 
    `booking_fcl` bk 
  SET
    bk.`ssline` = pMergedAcctNo,
    bk.`sslname` = CONCAT(
      pMergedAcctNo,
      '//',
      pMergedAcctName
    ) 
  WHERE bk.`ssline` = pDisabledAcctNo ;
  UPDATE 
    `bookingfcl_units` bu 
  SET
    bu.`account_no` = pMergedAcctNo,
    bu.`account_name` = pMergedAcctName 
  WHERE bu.`account_no` = pDisabledAcctNo ;
  UPDATE 
    `quotation` qt 
  SET
    qt.`clientnumber` = pMergedAcctNo,
    qt.`clientname` = pMergedAcctName,
    qt.`contactname` = pMergedAcctContact,
    qt.`email` = pMergedAcctEmail1,
    qt.`phone` = pMergedAcctPhone,
    qt.`fax` = pMergedAcctFax,
    qt.`clienttype` = pMergedAcctType 
  WHERE qt.`clientnumber` = pDisabledAcctNo ;
  UPDATE 
    `quotation` qt 
  SET
    qt.`agent_no` = pMergedAcctNo,
    qt.`agent` = pMergedAcctName 
  WHERE qt.`agent_no` = pDisabledAcctNo ;
  UPDATE 
    `quotation` qt 
  SET
    qt.`ssline` = pMergedAcctNo,
    qt.`sslname` = pMergedAcctName 
  WHERE qt.`ssline` = pDisabledAcctNo ;
  UPDATE 
    `quotation` qt 
  SET
    qt.`routedbymsg` = pMergedAcctNo 
  WHERE qt.`routedbymsg` = pDisabledAcctNo ;
  UPDATE 
    `charges` qt 
  SET
    qt.`account_no` = pMergedAcctNo,
    qt.`account_name` = pMergedAcctName 
  WHERE qt.`account_no` = pDisabledAcctNo ;
  UPDATE 
    `trucker_rates` tr 
  SET
    tr.`trucker` = pMergedAcctNo 
  WHERE tr.`trucker` = pDisabledAcctNo ;
  UPDATE 
`ar_red_invoice` ar
SET
ar.`customer_number` = pMergedAcctNo,
ar.`customer_name` = pMergedAcctName
WHERE ar.`customer_number` = pDisabledAcctNo;
END$$
DELIMITER ;

-- Mantis item 0011937 by pal raj on 17th May 2016

DELIMITER $$

DROP FUNCTION IF EXISTS `DeriveGlAccount`$$

CREATE FUNCTION `DeriveGlAccount`(
  pChargeCode VARCHAR (30),
  pShipmentType VARCHAR (10),
  pTransactionType VARCHAR (10),
  pTerminal VARCHAR(5)
) RETURNS VARCHAR(20) CHARSET latin1
    READS SQL DATA
    DETERMINISTIC
MAIN : BEGIN
  DECLARE mAccount VARCHAR (20) DEFAULT NULL ;
  IF (pChargeCode <> '' AND pShipmentType <> '' AND pTransactionType <> '' AND pTerminal <> '') THEN 
    SELECT
      CONCAT_WS('-', 
        `SystemRulesGetRuleName`('CompanyCode'),
        gl.`gl_acct`,
        IF(gl.`derive_yn` = 'Y',
          LPAD(`TerminalGLMapingGetTerminal`(pTerminal, pShipmentType, gl.`suffix_value`), 2, '0'),
          gl.`suffix_value`
        )
      )
    INTO
      mAccount
    FROM
      `gl_mapping` gl
    WHERE 
      gl.`charge_code` = pChargeCode
      AND gl.`shipment_type` = pShipmentType
      AND gl.`transaction_type` = pTransactionType
      AND gl.`bluescreen_chargecode` <> ''
    LIMIT 1;
  END IF ;
  RETURN mAccount ;
END MAIN$$

DELIMITER ;

-- Mantis item 0011229 by nambu on 18th May 2016

UPDATE TRANSACTION t SET t.drcpt=SUBSTRING_INDEX(t.drcpt,'-','1') WHERE t.`drcpt` LIKE '%-%' AND 
t.drcpt LIKE '04%'  AND t.bill_ladding_no LIKE '%-04%';

UPDATE TRANSACTION t SET t.drcpt=SUBSTR(t.drcpt,3) WHERE t.drcpt LIKE '04%' AND t.bill_ladding_no LIKE '%-04%';

UPDATE transaction_ledger t SET t.drcpt=SUBSTRING_INDEX(t.drcpt,'-','1') WHERE t.`drcpt` LIKE '%-%' AND 
t.drcpt LIKE '04%'  AND t.bill_ladding_no LIKE '%-04%';

UPDATE transaction_ledger t SET t.drcpt=SUBSTR(t.drcpt,3) WHERE 
t.drcpt LIKE '04%'  AND t.bill_ladding_no LIKE '%-04%';


-- only Local and econo Mantis item 0011229 by nambu on 19th May 2016

UPDATE transaction_ledger t SET t.drcpt=SUBSTRING_INDEX(t.drcpt,'-','1') WHERE t.`Bill_Ladding_No` LIKE '%-04%' 
AND t.drcpt LIKE '%-a' OR t.drcpt LIKE '%-b' OR t.drcpt LIKE '%-c' OR t.drcpt LIKE '%-d' OR t.drcpt LIKE '%-e' OR t.drcpt LIKE '%-f' 
OR t.drcpt LIKE '%-g' OR t.drcpt LIKE '%-h' OR t.drcpt LIKE '%-i' OR t.drcpt LIKE '%-j' OR t.drcpt LIKE '%-l' OR t.drcpt LIKE '%-k'
OR t.drcpt LIKE '%-l'OR t.drcpt LIKE '%-m'OR t.drcpt LIKE '%-n'OR t.drcpt LIKE '%-o'OR t.drcpt LIKE '%-p' OR t.drcpt LIKE '%-q'OR t.drcpt LIKE '%-r';
-- 24May2016 Mantis#3409 @Mei
TRUNCATE lcl_export_notification;
ALTER TABLE `lcl_export_notification` ADD COLUMN `to_name` VARCHAR(300) NULL AFTER `status`,
 ADD COLUMN `company_name` VARCHAR(200) NULL AFTER `to_name`; 

-- only LWT and LIVE  Mantis item 0011229 by nambu on 26 May 2016

UPDATE TRANSACTION t SET t.drcpt=SUBSTRING_INDEX(t.drcpt,'-','1') WHERE t.`drcpt` LIKE '%-%' AND 
t.drcpt LIKE '04%'  AND t.bill_ladding_no LIKE '%-04%';

UPDATE TRANSACTION t SET t.drcpt=SUBSTRING_INDEX(t.drcpt,'-','1') WHERE t.`drcpt` LIKE '%-%' AND 
t.drcpt LIKE '04%'  AND t.bill_ladding_no IS NULL AND t.transaction_type ='AR' AND t.`Invoice_number` LIKE '0%';

UPDATE TRANSACTION t SET t.drcpt=SUBSTR(t.drcpt,3) WHERE t.drcpt LIKE '04%' AND t.bill_ladding_no LIKE '%-04%';

UPDATE TRANSACTION t SET t.drcpt=SUBSTR(t.drcpt,3) WHERE t.drcpt LIKE '04%' AND t.bill_ladding_no IS NULL 
AND t.transaction_type ='AR' AND t.`Invoice_number` LIKE '0%';

UPDATE transaction_ledger t SET t.drcpt=SUBSTRING_INDEX(t.drcpt,'-','1') WHERE t.`drcpt` LIKE '%-%' AND 
t.drcpt LIKE '04%'  AND t.bill_ladding_no LIKE '%-04%';

UPDATE transaction_ledger t SET t.drcpt=SUBSTRING_INDEX(t.drcpt,'-','1') WHERE t.`drcpt` LIKE '%-%' AND 
t.drcpt LIKE '04%'  AND t.bill_ladding_no IS NULL AND t.transaction_type ='AR' AND t.`Invoice_number` LIKE '0%';

UPDATE transaction_ledger t SET t.drcpt=SUBSTR(t.drcpt,3) WHERE 
t.drcpt LIKE '04%'  AND t.bill_ladding_no LIKE '%-04%';

UPDATE transaction_ledger t SET t.drcpt=SUBSTR(t.drcpt,3) WHERE t.drcpt LIKE '04%' AND t.bill_ladding_no IS NULL 
AND t.transaction_type ='AR' AND t.`Invoice_number` LIKE '0%';

 -- Mantis item 0011193(Accounting) by nambu on 30 May 2016

ALTER TABLE `terminal`   
  CHANGE `manager_name` `manager_name` VARCHAR(200) CHARSET latin1 COLLATE latin1_swedish_ci NULL,
  CHANGE `manager_email` `manager_email` VARCHAR(200) CHARSET latin1 COLLATE latin1_swedish_ci NULL;

UPDATE terminal SET manager_name ='marlene gallego,dan rocke', manager_email ='mgallego@ecuworldwide.us,drocke@ecuworldwide.us' WHERE trmnum ='01';

UPDATE terminal SET manager_name ='chris garcia,dan rocke', manager_email ='cgarcia@ecuworldwide.us,drocke@ecuworldwide.us' WHERE trmnum ='03';

UPDATE terminal SET manager_name ='alberto villarubio,dan rocke', manager_email ='avillarrubia@ecuworldwide.us,drocke@ecuworldwide.us' WHERE trmnum ='04';

UPDATE terminal SET manager_name ='giancarlo jaramillo,dan rocke', manager_email ='gjaramil@ecuworldwide.us,drocke@ecuworldwide.us' WHERE trmnum ='08';

UPDATE terminal SET manager_name ='aida escobar,dan rocke', manager_email ='aescobar@ecuworldwide.us,drocke@ecuworldwide.us' WHERE trmnum ='09';

UPDATE terminal SET manager_name ='andrew aguirre,vince argenzio,alessandro bruni,dan rocke',manager_email ='aaguiree@ecuworldwide.us,vargenzio@ecuworldwide.us,abruni@ecuworldwide.us,drocke@ecuworldwide.us'
WHERE trmnum ='15';

UPDATE terminal SET manager_name ='victor gonzalez,dan rocke',manager_email ='vgonzalez@ecuworldwide.us,drocke@ecuworldwide.us' WHERE trmnum ='17';

UPDATE terminal SET manager_name ='sally baldwin,dan rocke',manager_email ='sbaldwin@ecuworldwide.us,drocke@ecuworldwide.us' WHERE trmnum ='18';

UPDATE terminal SET manager_name ='kelly kraus,dan rocke',manager_email ='kkraus@ecuworldwide.us,drocke@ecuworldwide.us' WHERE trmnum ='19';

UPDATE terminal SET manager_name ='gordon berment',manager_email ='gberment@ecuworldwide.us' WHERE trmnum ='59';

UPDATE terminal SET manager_name ='inge van rompaey',manager_email ='inge.van.rompaey@ecuworldwide.us' WHERE trmnum ='60';

UPDATE terminal SET manager_name ='jason anarumo,inge van rompaey',
manager_email ='janarumo@ecuworldwide.us,inge.van.rompaey@ecuworldwide.us' WHERE trmnum ='61';

UPDATE terminal SET manager_name ='inge van rompaey',manager_email ='inge.van.rompaey@ecuworldwide.us' WHERE trmnum ='62';

UPDATE terminal SET manager_name ='melinda deleon,inge van rompaey',
manager_email ='mdeleon@ecuworldwide.us,inge.van.rompaey@ecuworldwide.us' WHERE trmnum ='63';

UPDATE terminal SET manager_name ='vince argenzio,ruben sanchez,inge van rompaey',
manager_email ='vargenzio@ecuworldwide.us,rsanchez@ecuworldwide.us,inge.van.rompaey@ecuworldwide.us'
WHERE trmnum ='65';

UPDATE terminal SET manager_name ='aida escobar,inge van rompaey',manager_email ='aescobar@ecuworldwide.us,inge.van.rompaey@ecuworldwide.us'
WHERE trmnum ='69';

UPDATE terminal SET manager_name ='kelly kraus,dan rocke',manager_email ='kkraus@ecuworldwide.us,drocke@ecuworldwide.us' WHERE trmnum ='79';

UPDATE terminal SET manager_name ='marta arner',manager_email ='marner@ecuworldwide.us' WHERE trmnum IN ('80','81','82','85','86','88');

-- Mantis Item-12237(LCL-Imports) by Stefy on 30 May 2016 

ALTER TABLE ar_red_invoice MODIFY COLUMN customer_type VARCHAR(30);

-- Mantis Item-0010025(LCL-Imports) by palraj on 01 Jun 2016 

INSERT INTO job (`name`, `frequency`, `day1`, `day2`, `hour`, `minute`, `enabled`, `updated_by`, `updated_date`, `start_time`, `end_time`, `class_name`) VALUES('Freight Invoice Notification','DAILY','0','0','23','30','0','PALRAJ','2016-05-31 14:23:30','2016-05-30 14:23:30','2016-05-31 14:23:30','com.logiware.common.job.FreightInvoiceNotification');

INSERT INTO genericcode_dup (Codetypeid,CODE,codedesc,Field1) VALUES('22','EI','Email Imports Invoice Notification','K');

INSERT INTO genericcode_dup (Codetypeid,CODE,codedesc,Field1) VALUES('22','FI','Fax Imports Invoice Notification','K');

INSERT INTO genericcode_dup (Codetypeid,CODE,codedesc,Field1) VALUES('22','EB','Email Export/Import Invoice Notification','K');

INSERT INTO genericcode_dup (Codetypeid,CODE,codedesc,Field1) VALUES('22','FB','Fax Export/Import Invoice Notification','K');

DELIMITER $$

DROP FUNCTION IF EXISTS `ContactGetEmailFaxByAcctNoCodeK`$$

CREATE FUNCTION `ContactGetEmailFaxByAcctNoCodeK`(
  pAcctNo VARCHAR (10),
  pCodeEmail1 VARCHAR (5),
  pCodeEmail2 VARCHAR (5),
  pCodeFax1 VARCHAR (5),
  pCodeFax2 VARCHAR (5)
) RETURNS VARCHAR(255) CHARSET utf8
    READS SQL DATA
    DETERMINISTIC
MAIN :
BEGIN
  DECLARE mRet VARCHAR (255) DEFAULT NULL ;
  IF (ISNULL(pAcctNo) = FALSE AND pAcctNo <> '' ) THEN
    SELECT
      GROUP_CONCAT(
        CASE
          WHEN gc.`code` IN (pCodeEmail1, pCodeEmail2) THEN
            TRIM(LOWER(ct.`email`))
          WHEN gc.`code` IN (pCodeFax1, pCodeFax2) THEN
            TRIM(ct.`fax`)
          ELSE NULL
        END
      ) INTO mRET
    FROM
      `cust_contact` ct
      JOIN `genericcode_dup` gc
        ON (
          gc.`id` = ct.`code_k` 
          AND gc.`code` IN (pCodeEmail1, pCodeEmail2, pCodeFax1, pCodeFax2)
        )
    WHERE ct.`acct_no` = pAcctNo AND gc.Field1 ='K';
  END IF;
  RETURN mRet ;
END MAIN$$

DELIMITER ;
--General Issues By MEi
ALTER TABLE lcl_options CHANGE `key` `option_key` VARCHAR(255) CHARSET latin1 COLLATE latin1_swedish_ci NOT NULL,
 CHANGE `value` `option_value` TEXT CHARSET latin1 COLLATE latin1_swedish_ci NOT NULL;

-- Mantis Item-0012133(LCL-Exports) by saravanan on 03 Jun 2016
ALTER TABLE `lcl_search_template` ADD COLUMN `loading_remarks` TINYINT(1) DEFAULT 0 NOT NULL AFTER `lineLocation`;

-- Mantis Issue-12325(LCL-Exports) by Kuppusamy on 06 th jun 2016
ALTER TABLE `un_location`
  ADD COLUMN `do_not_express_release` TINYINT(1) DEFAULT 0  NOT NULL AFTER `bl_numbering`;

-- Mantis Issue 12349 (LCL-Exports) by Kuppusamy  on 06 th jun 2016
UPDATE cust_accounting SET credit_status=11844,credit_limit=0.00 WHERE credit_status IS NULL;

-- Mantis Issue 12393 (LCL-Exports) by RathnaPandian  on 08 th jun 2016
ALTER TABLE `lcl_ss_masterbl`
   ADD COLUMN `print_dock_recipt` TINYINT(1) DEFAULT 0  NULL;


------This query has to be executed in live (LL-ECI) Mantis item 12521(Lcl-Imports) by priyanka on 9th jun 2016

DELETE ac FROM transaction_ledger ac 
JOIN lcl_file_number lfn ON lfn.`file_number` = ac.`drcpt`
JOIN lcl_booking_Ac lba ON lba.`file_number_id`= lfn.`id`
JOIN gl_mapping gp ON gp.`id`=lba.`ap_gl_mapping_id` 
WHERE ac.`Transaction_amt` <> 0.00 AND lba.`ap_amount` = 0.00 
AND lfn.file_number=ac.`drcpt` AND lba.id=ac.`cost_id` AND ac.`Transaction_type`='AC' AND ac.`Status` ='Open';


--- > Mantis item# 10421  only for Local Machine By Aravindhan.V on 9th June 2016
ALTER TABLE lcl_correction
ADD COLUMN old_shipper_acct VARCHAR(10), 
ADD COLUMN old_agent_acct VARCHAR(10),
ADD COLUMN old_forwarder_acct VARCHAR(10),

ADD COLUMN new_shipper_acct VARCHAR(10), 
ADD COLUMN new_agent_acct VARCHAR(10),
ADD COLUMN new_forwarder_acct VARCHAR(10),
ADD COLUMN new_third_party_acct VARCHAR(10),

 ADD INDEX lcl_correction_fk16(old_shipper_acct),
 ADD INDEX lcl_correction_fk17(old_agent_acct),
 ADD INDEX lcl_correction_fk18(old_forwarder_acct),
 
 ADD INDEX lcl_correction_fk19(new_shipper_acct),
 ADD INDEX lcl_correction_fk20(new_agent_acct),
 ADD INDEX lcl_correction_fk21(new_forwarder_acct),
 ADD INDEX lcl_correction_fk22(new_third_party_acct),

 ADD CONSTRAINT lcl_correction_fk16 FOREIGN KEY(old_shipper_acct) REFERENCES trading_partner(acct_no),
 ADD CONSTRAINT lcl_correction_fk17 FOREIGN KEY(old_agent_acct) REFERENCES trading_partner(acct_no),
 ADD CONSTRAINT lcl_correction_fk18 FOREIGN KEY(old_forwarder_acct) REFERENCES trading_partner(acct_no),
 
 ADD CONSTRAINT lcl_correction_fk19 FOREIGN KEY(new_shipper_acct) REFERENCES trading_partner(acct_no),
 ADD CONSTRAINT lcl_correction_fk20 FOREIGN KEY(new_agent_acct) REFERENCES trading_partner(acct_no),
 ADD CONSTRAINT lcl_correction_fk21 FOREIGN KEY(new_forwarder_acct) REFERENCES trading_partner(acct_no),
 ADD CONSTRAINT lcl_correction_fk22 FOREIGN KEY(new_third_party_acct) REFERENCES trading_partner(acct_no);

ALTER TABLE lcl_correction_charge ADD COLUMN old_bill_to_party ENUM('A','C','F','S','T','N','W') DEFAULT NULL;

ALTER TABLE credit_debit_note ADD COLUMN bill_to_party CHAR(1) DEFAULT NULL;

-- Mantis Issue-12325(LCL-Exports) by Kuppusamy on 10 th jun 2016
ALTER TABLE `un_location`
  ADD COLUMN `memo_house_bill_of_lading` TINYINT(1) DEFAULT 0  NOT NULL AFTER `do_not_express_release`;

--- > For correction logic  By Aravindhan.V on 10th June 2016
CREATE TABLE lcl_credit_debit_charge (id INT(11) PRIMARY KEY AUTO_INCREMENT,
credit_debit_id INT(11) NOT NULL,
charge_id INT(11) NOT NULL,
amount DECIMAL(13,2) NOT NULL,
KEY credit_debit_fk1(credit_debit_id),
KEY credit_debit_fk2(charge_id),
FOREIGN KEY(credit_debit_id) REFERENCES credit_debit_note(id) ON DELETE CASCADE,
FOREIGN KEY(charge_id) REFERENCES gl_mapping(id));

-- Exception in Role Duty on 13th june 2016
ALTER TABLE `role_duties`   
  CHANGE `id` `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT;

-- Mantis# 12793 on 16 June by Sathiya
INSERT INTO `property` (NAME,VALUE,TYPE,description) VALUES
 ('FreightForwarderAccountBl','NOFRTA0001,NOFFAA0002','LCL','Freight Forwarder Account Bl');


--  Mantis Item-12145(LCL-Imports) by Stefy on 17 June 2016
DELIMITER $$


DROP PROCEDURE IF EXISTS `MergeLcl`$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `MergeLcl`(
  IN pDisabledAcctNo VARCHAR (10),
  IN pMergedAcctNo VARCHAR (10),
  IN pMergedAcctName VARCHAR (50),
  IN pMergedAcctContact VARCHAR (50),
  IN pMergedAcctAddress TEXT,
  IN pMergedAcctCity VARCHAR (50),
  IN pMergedAcctState VARCHAR (2),
  IN pMergedAcctZip VARCHAR (20),
  IN pMergedAcctCountry VARCHAR (30),
  IN pMergedAcctPhone VARCHAR (25),
  IN pMergedAcctFax VARCHAR (25),
  IN pMergedAcctEmail1 VARCHAR (50),
  IN pMergedAcctEmail2 VARCHAR (50),
  IN pUpdatedUserId INT (11)
)
    MODIFIES SQL DATA
BEGIN
  UPDATE
    `container_info` ci
  SET
    ci.`agent_no` = pMergedAcctNo
  WHERE ci.`agent_no` = pDisabledAcctNo ;
  UPDATE
    `eculine_trading_partner_mapping` tm
  SET
    tm.`trading_partner_acct_no` = pMergedAcctNo
  WHERE tm.`trading_partner_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_contact` lc,
    `lcl_booking` bk
  SET
    lc.`contact_name` = pMergedAcctContact,
    lc.`company_name` = pMergedAcctName,
    lc.`address` = pMergedAcctAddress,
    lc.`city` = pMergedAcctCity,
    lc.`state` = pMergedAcctState,
    lc.zip = pMergedAcctZip,
    lc.`country` = pMergedAcctCountry,
    lc.`phone1` = pMergedAcctPhone,
    lc.`fax1` = pMergedAcctFax,
    lc.`email1` = pMergedAcctEmail1,
    lc.`email2` = pMergedAcctEmail2,
    lc.`entered_datetime` = NOW(),
    lc.`entered_by_user_id` = pUpdatedUserId,
    lc.`modified_datetime` = NOW(),
    lc.`modified_by_user_id` = pUpdatedUserId
  WHERE lc.`id` = bk.`cons_contact_id`
    AND bk.`cons_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_booking` bk
  SET
    bk.`cons_acct_no` = pMergedAcctNo
  WHERE bk.`cons_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_contact` lc,
    `lcl_booking` bk
  SET
    lc.`contact_name` = pMergedAcctContact,
    lc.`company_name` = pMergedAcctName,
    lc.`address` = pMergedAcctAddress,
    lc.`city` = pMergedAcctCity,
    lc.`state` = pMergedAcctState,
    lc.zip = pMergedAcctZip,
    lc.`country` = pMergedAcctCountry,
    lc.`phone1` = pMergedAcctPhone,
    lc.`fax1` = pMergedAcctFax,
    lc.`email1` = pMergedAcctEmail1,
    lc.`email2` = pMergedAcctEmail2,
    lc.`entered_datetime` = NOW(),
    lc.`entered_by_user_id` = pUpdatedUserId,
    lc.`modified_datetime` = NOW(),
    lc.`modified_by_user_id` = pUpdatedUserId
  WHERE lc.`id` = bk.`sup_contact_id`
    AND bk.`sup_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_booking` bk
  SET
    bk.`sup_acct_no` = pMergedAcctNo
  WHERE bk.`sup_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_contact` lc,
    `lcl_booking` bk
  SET
    lc.`contact_name` = pMergedAcctContact,
    lc.`company_name` = pMergedAcctName,
    lc.`address` = pMergedAcctAddress,
    lc.`city` = pMergedAcctCity,
    lc.`state` = pMergedAcctState,
    lc.zip = pMergedAcctZip,
    lc.`country` = pMergedAcctCountry,
    lc.`phone1` = pMergedAcctPhone,
    lc.`fax1` = pMergedAcctFax,
    lc.`email1` = pMergedAcctEmail1,
    lc.`email2` = pMergedAcctEmail2,
    lc.`entered_datetime` = NOW(),
    lc.`entered_by_user_id` = pUpdatedUserId,
    lc.`modified_datetime` = NOW(),
    lc.`modified_by_user_id` = pUpdatedUserId
  WHERE lc.`id` = bk.`client_contact_id`
    AND bk.`client_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_booking` bk
  SET
    bk.`client_acct_no` = pMergedAcctNo
  WHERE bk.`client_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_contact` lc,
    `lcl_booking` bk
  SET
    lc.`contact_name` = pMergedAcctContact,
    lc.`company_name` = pMergedAcctName,
    lc.`address` = pMergedAcctAddress,
    lc.`city` = pMergedAcctCity,
    lc.`state` = pMergedAcctState,
    lc.zip = pMergedAcctZip,
    lc.`country` = pMergedAcctCountry,
    lc.`phone1` = pMergedAcctPhone,
    lc.`fax1` = pMergedAcctFax,
    lc.`email1` = pMergedAcctEmail1,
    lc.`email2` = pMergedAcctEmail2,
    lc.`entered_datetime` = NOW(),
    lc.`entered_by_user_id` = pUpdatedUserId,
    lc.`modified_datetime` = NOW(),
    lc.`modified_by_user_id` = pUpdatedUserId
  WHERE lc.`id` = bk.`third_party_contact_id`
    AND bk.`third_party_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_booking` bk
  SET
    bk.`third_party_acct_no` = pMergedAcctNo
  WHERE bk.`third_party_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_contact` lc,
    `lcl_booking` bk
  SET
    lc.`contact_name` = pMergedAcctContact,
    lc.`company_name` = pMergedAcctName,
    lc.`address` = pMergedAcctAddress,
    lc.`city` = pMergedAcctCity,
    lc.`state` = pMergedAcctState,
    lc.zip = pMergedAcctZip,
    lc.`country` = pMergedAcctCountry,
    lc.`phone1` = pMergedAcctPhone,
    lc.`fax1` = pMergedAcctFax,
    lc.`email1` = pMergedAcctEmail1,
    lc.`email2` = pMergedAcctEmail2,
    lc.`entered_datetime` = NOW(),
    lc.`entered_by_user_id` = pUpdatedUserId,
    lc.`modified_datetime` = NOW(),
    lc.`modified_by_user_id` = pUpdatedUserId
  WHERE lc.`id` = bk.`noty_contact_id`
    AND bk.`noty_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_booking` bk
  SET
    bk.`noty_acct_no` = pMergedAcctNo
  WHERE bk.`noty_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_contact` lc,
    `lcl_booking` bk
  SET
    lc.`contact_name` = pMergedAcctContact,
    lc.`company_name` = pMergedAcctName,
    lc.`address` = pMergedAcctAddress,
    lc.`city` = pMergedAcctCity,
    lc.`state` = pMergedAcctState,
    lc.zip = pMergedAcctZip,
    lc.`country` = pMergedAcctCountry,
    lc.`phone1` = pMergedAcctPhone,
    lc.`fax1` = pMergedAcctFax,
    lc.`email1` = pMergedAcctEmail1,
    lc.`email2` = pMergedAcctEmail2,
    lc.`entered_datetime` = NOW(),
    lc.`entered_by_user_id` = pUpdatedUserId,
    lc.`modified_datetime` = NOW(),
    lc.`modified_by_user_id` = pUpdatedUserId
  WHERE lc.`id` = bk.`ship_contact_id`
    AND bk.`ship_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_booking` bk
  SET
    bk.`ship_acct_no` = pMergedAcctNo
  WHERE bk.`ship_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_contact` lc,
    `lcl_booking` bk
  SET
    lc.`contact_name` = pMergedAcctContact,
    lc.`company_name` = pMergedAcctName,
    lc.`address` = pMergedAcctAddress,
    lc.`city` = pMergedAcctCity,
    lc.`state` = pMergedAcctState,
    lc.zip = pMergedAcctZip,
    lc.`country` = pMergedAcctCountry,
    lc.`phone1` = pMergedAcctPhone,
    lc.`fax1` = pMergedAcctFax,
    lc.`email1` = pMergedAcctEmail1,
    lc.`email2` = pMergedAcctEmail2,
    lc.`entered_datetime` = NOW(),
    lc.`entered_by_user_id` = pUpdatedUserId,
    lc.`modified_datetime` = NOW(),
    lc.`modified_by_user_id` = pUpdatedUserId
  WHERE lc.`id` = bk.`agent_contact_id`
    AND bk.`agent_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_booking` bk
  SET
    bk.`agent_acct_no` = pMergedAcctNo
  WHERE bk.`agent_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_contact` lc,
    `lcl_booking` bk
  SET
    lc.`contact_name` = pMergedAcctContact,
    lc.`company_name` = pMergedAcctName,
    lc.`address` = pMergedAcctAddress,
    lc.`city` = pMergedAcctCity,
    lc.`state` = pMergedAcctState,
    lc.zip = pMergedAcctZip,
    lc.`country` = pMergedAcctCountry,
    lc.`phone1` = pMergedAcctPhone,
    lc.`fax1` = pMergedAcctFax,
    lc.`email1` = pMergedAcctEmail1,
    lc.`email2` = pMergedAcctEmail2,
    lc.`entered_datetime` = NOW(),
    lc.`entered_by_user_id` = pUpdatedUserId,
    lc.`modified_datetime` = NOW(),
    lc.`modified_by_user_id` = pUpdatedUserId
  WHERE lc.`id` = bk.`fwd_contact_id`
    AND bk.`fwd_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_booking` bk
  SET
    bk.`fwd_acct_no` = pMergedAcctNo
  WHERE bk.`fwd_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_contact` lc,
    `lcl_booking` bk
  SET
    lc.`contact_name` = pMergedAcctContact,
    lc.`company_name` = pMergedAcctName,
    lc.`address` = pMergedAcctAddress,
    lc.`city` = pMergedAcctCity,
    lc.`state` = pMergedAcctState,
    lc.zip = pMergedAcctZip,
    lc.`country` = pMergedAcctCountry,
    lc.`phone1` = pMergedAcctPhone,
    lc.`fax1` = pMergedAcctFax,
    lc.`email1` = pMergedAcctEmail1,
    lc.`email2` = pMergedAcctEmail2,
    lc.`entered_datetime` = NOW(),
    lc.`entered_by_user_id` = pUpdatedUserId,
    lc.`modified_datetime` = NOW(),
    lc.`modified_by_user_id` = pUpdatedUserId
  WHERE lc.`id` = bk.`rtd_agent_contact_id`
    AND bk.`rtd_agent_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_booking` bk
  SET
    bk.`rtd_agent_acct_no` = pMergedAcctNo
  WHERE bk.`rtd_agent_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_contact` lc,
    `lcl_booking_ac` ac
  SET
    lc.`contact_name` = pMergedAcctContact,
    lc.`company_name` = pMergedAcctName,
    lc.`address` = pMergedAcctAddress,
    lc.`city` = pMergedAcctCity,
    lc.`state` = pMergedAcctState,
    lc.zip = pMergedAcctZip,
    lc.`country` = pMergedAcctCountry,
    lc.`phone1` = pMergedAcctPhone,
    lc.`fax1` = pMergedAcctFax,
    lc.`email1` = pMergedAcctEmail1,
    lc.`email2` = pMergedAcctEmail2,
    lc.`entered_datetime` = NOW(),
    lc.`entered_by_user_id` = pUpdatedUserId,
    lc.`modified_datetime` = NOW(),
    lc.`modified_by_user_id` = pUpdatedUserId
  WHERE lc.`id` = ac.`sp_contact_id`
    AND ac.`sp_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_booking_ac` ac
  SET
    ac.`sp_acct_no` = pMergedAcctNo
  WHERE ac.`sp_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_booking_export` ex
  SET
    ex.`rt_agent_acct_no` = pMergedAcctNo
  WHERE ex.`rt_agent_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_booking_import` im
  SET
    im.`export_agent_acct_no` = pMergedAcctNo
  WHERE im.`export_agent_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_booking_import` im
  SET
    im.`ipi_cfs_acct_no` = pMergedAcctNo
  WHERE im.`ipi_cfs_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_contact` lc,
    `lcl_booking_plan` pl
  SET
    lc.`contact_name` = pMergedAcctContact,
    lc.`company_name` = pMergedAcctName,
    lc.`address` = pMergedAcctAddress,
    lc.`city` = pMergedAcctCity,
    lc.`state` = pMergedAcctState,
    lc.zip = pMergedAcctZip,
    lc.`country` = pMergedAcctCountry,
    lc.`phone1` = pMergedAcctPhone,
    lc.`fax1` = pMergedAcctFax,
    lc.`email1` = pMergedAcctEmail1,
    lc.`email2` = pMergedAcctEmail2,
    lc.`entered_datetime` = NOW(),
    lc.`entered_by_user_id` = pUpdatedUserId,
    lc.`modified_datetime` = NOW(),
    lc.`modified_by_user_id` = pUpdatedUserId
  WHERE lc.`id` = pl.`sp_contact_id`
    AND pl.`sp_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_booking_plan` pl
  SET
    pl.`sp_acct_no` = pMergedAcctNo
  WHERE pl.`sp_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_contact` lc
  SET
    lc.`contact_name` = pMergedAcctContact,
    lc.`company_name` = pMergedAcctName,
    lc.`address` = pMergedAcctAddress,
    lc.`city` = pMergedAcctCity,
    lc.`state` = pMergedAcctState,
    lc.zip = pMergedAcctZip,
    lc.`country` = pMergedAcctCountry,
    lc.`phone1` = pMergedAcctPhone,
    lc.`fax1` = pMergedAcctFax,
    lc.`email1` = pMergedAcctEmail1,
    lc.`email2` = pMergedAcctEmail2,
    lc.`entered_datetime` = NOW(),
    lc.`entered_by_user_id` = pUpdatedUserId,
    lc.`modified_datetime` = NOW(),
    lc.`modified_by_user_id` = pUpdatedUserId,
    lc.`tp_acct_no` = pMergedAcctNo
  WHERE lc.`tp_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_correction` cn
  SET
    cn.`cons_acct_no` = pMergedAcctNo
  WHERE cn.`cons_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_correction` cn
  SET
    cn.`noty_acct_no` = pMergedAcctNo
  WHERE cn.`noty_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_correction` cn
  SET
    cn.`party_acct_no` = pMergedAcctNo
  WHERE cn.`party_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_correction` cn
  SET
    cn.`third_party_acct_no` = pMergedAcctNo
  WHERE cn.`third_party_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_contact` lc,
    `lcl_quote` qt
  SET
    lc.`contact_name` = pMergedAcctContact,
    lc.`company_name` = pMergedAcctName,
    lc.`address` = pMergedAcctAddress,
    lc.`city` = pMergedAcctCity,
    lc.`state` = pMergedAcctState,
    lc.zip = pMergedAcctZip,
    lc.`country` = pMergedAcctCountry,
    lc.`phone1` = pMergedAcctPhone,
    lc.`fax1` = pMergedAcctFax,
    lc.`email1` = pMergedAcctEmail1,
    lc.`email2` = pMergedAcctEmail2,
    lc.`entered_datetime` = NOW(),
    lc.`entered_by_user_id` = pUpdatedUserId,
    lc.`modified_datetime` = NOW(),
    lc.`modified_by_user_id` = pUpdatedUserId
  WHERE lc.`id` = qt.`cons_contact_id`
    AND qt.`cons_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_quote` qt
  SET
    qt.`cons_acct_no` = pMergedAcctNo
  WHERE qt.`cons_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_contact` lc,
    `lcl_quote` qt
  SET
    lc.`contact_name` = pMergedAcctContact,
    lc.`company_name` = pMergedAcctName,
    lc.`address` = pMergedAcctAddress,
    lc.`city` = pMergedAcctCity,
    lc.`state` = pMergedAcctState,
    lc.zip = pMergedAcctZip,
    lc.`country` = pMergedAcctCountry,
    lc.`phone1` = pMergedAcctPhone,
    lc.`fax1` = pMergedAcctFax,
    lc.`email1` = pMergedAcctEmail1,
    lc.`email2` = pMergedAcctEmail2,
    lc.`entered_datetime` = NOW(),
    lc.`entered_by_user_id` = pUpdatedUserId,
    lc.`modified_datetime` = NOW(),
    lc.`modified_by_user_id` = pUpdatedUserId
  WHERE lc.`id` = qt.`sup_contact_id`
    AND qt.`sup_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_quote` qt
  SET
    qt.`sup_acct_no` = pMergedAcctNo
  WHERE qt.`sup_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_contact` lc,
    `lcl_quote` qt
  SET
    lc.`contact_name` = pMergedAcctContact,
    lc.`company_name` = pMergedAcctName,
    lc.`address` = pMergedAcctAddress,
    lc.`city` = pMergedAcctCity,
    lc.`state` = pMergedAcctState,
    lc.zip = pMergedAcctZip,
    lc.`country` = pMergedAcctCountry,
    lc.`phone1` = pMergedAcctPhone,
    lc.`fax1` = pMergedAcctFax,
    lc.`email1` = pMergedAcctEmail1,
    lc.`email2` = pMergedAcctEmail2,
    lc.`entered_datetime` = NOW(),
    lc.`entered_by_user_id` = pUpdatedUserId,
    lc.`modified_datetime` = NOW(),
    lc.`modified_by_user_id` = pUpdatedUserId
  WHERE lc.`id` = qt.`noty_contact_id`
    AND qt.`noty_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_quote` qt
  SET
    qt.`noty_acct_no` = pMergedAcctNo
  WHERE qt.`noty_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_contact` lc,
    `lcl_quote` qt
  SET
    lc.`contact_name` = pMergedAcctContact,
    lc.`company_name` = pMergedAcctName,
    lc.`address` = pMergedAcctAddress,
    lc.`city` = pMergedAcctCity,
    lc.`state` = pMergedAcctState,
    lc.zip = pMergedAcctZip,
    lc.`country` = pMergedAcctCountry,
    lc.`phone1` = pMergedAcctPhone,
    lc.`fax1` = pMergedAcctFax,
    lc.`email1` = pMergedAcctEmail1,
    lc.`email2` = pMergedAcctEmail2,
    lc.`entered_datetime` = NOW(),
    lc.`entered_by_user_id` = pUpdatedUserId,
    lc.`modified_datetime` = NOW(),
    lc.`modified_by_user_id` = pUpdatedUserId
  WHERE lc.`id` = qt.`ship_contact_id`
    AND qt.`ship_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_quote` qt
  SET
    qt.`ship_acct_no` = pMergedAcctNo
  WHERE qt.`ship_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_contact` lc,
    `lcl_quote` qt
  SET
    lc.`contact_name` = pMergedAcctContact,
    lc.`company_name` = pMergedAcctName,
    lc.`address` = pMergedAcctAddress,
    lc.`city` = pMergedAcctCity,
    lc.`state` = pMergedAcctState,
    lc.zip = pMergedAcctZip,
    lc.`country` = pMergedAcctCountry,
    lc.`phone1` = pMergedAcctPhone,
    lc.`fax1` = pMergedAcctFax,
    lc.`email1` = pMergedAcctEmail1,
    lc.`email2` = pMergedAcctEmail2,
    lc.`entered_datetime` = NOW(),
    lc.`entered_by_user_id` = pUpdatedUserId,
    lc.`modified_datetime` = NOW(),
    lc.`modified_by_user_id` = pUpdatedUserId
  WHERE lc.`id` = qt.`client_contact_id`
    AND qt.`client_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_quote` qt
  SET
    qt.`client_acct_no` = pMergedAcctNo
  WHERE qt.`client_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_contact` lc,
    `lcl_quote` qt
  SET
    lc.`contact_name` = pMergedAcctContact,
    lc.`company_name` = pMergedAcctName,
    lc.`address` = pMergedAcctAddress,
    lc.`city` = pMergedAcctCity,
    lc.`state` = pMergedAcctState,
    lc.zip = pMergedAcctZip,
    lc.`country` = pMergedAcctCountry,
    lc.`phone1` = pMergedAcctPhone,
    lc.`fax1` = pMergedAcctFax,
    lc.`email1` = pMergedAcctEmail1,
    lc.`email2` = pMergedAcctEmail2,
    lc.`entered_datetime` = NOW(),
    lc.`entered_by_user_id` = pUpdatedUserId,
    lc.`modified_datetime` = NOW(),
    lc.`modified_by_user_id` = pUpdatedUserId
  WHERE lc.`id` = qt.`agent_contact_id`
    AND qt.`agent_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_quote` qt
  SET
    qt.`agent_acct_no` = pMergedAcctNo
  WHERE qt.`agent_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_contact` lc,
    `lcl_quote` qt
  SET
    lc.`contact_name` = pMergedAcctContact,
    lc.`company_name` = pMergedAcctName,
    lc.`address` = pMergedAcctAddress,
    lc.`city` = pMergedAcctCity,
    lc.`state` = pMergedAcctState,
    lc.zip = pMergedAcctZip,
    lc.`country` = pMergedAcctCountry,
    lc.`phone1` = pMergedAcctPhone,
    lc.`fax1` = pMergedAcctFax,
    lc.`email1` = pMergedAcctEmail1,
    lc.`email2` = pMergedAcctEmail2,
    lc.`entered_datetime` = NOW(),
    lc.`entered_by_user_id` = pUpdatedUserId,
    lc.`modified_datetime` = NOW(),
    lc.`modified_by_user_id` = pUpdatedUserId
  WHERE lc.`id` = qt.`fwd_contact_id`
    AND qt.`fwd_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_quote` qt
  SET
    qt.`fwd_acct_no` = pMergedAcctNo
  WHERE qt.`fwd_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_contact` lc,
    `lcl_quote` qt
  SET
    lc.`contact_name` = pMergedAcctContact,
    lc.`company_name` = pMergedAcctName,
    lc.`address` = pMergedAcctAddress,
    lc.`city` = pMergedAcctCity,
    lc.`state` = pMergedAcctState,
    lc.zip = pMergedAcctZip,
    lc.`country` = pMergedAcctCountry,
    lc.`phone1` = pMergedAcctPhone,
    lc.`fax1` = pMergedAcctFax,
    lc.`email1` = pMergedAcctEmail1,
    lc.`email2` = pMergedAcctEmail2,
    lc.`entered_datetime` = NOW(),
    lc.`entered_by_user_id` = pUpdatedUserId,
    lc.`modified_datetime` = NOW(),
    lc.`modified_by_user_id` = pUpdatedUserId
  WHERE lc.`id` = qt.`rtd_agent_contact_id`
    AND qt.`rtd_agent_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_quote` qt
  SET
    qt.`rtd_agent_acct_no` = pMergedAcctNo
  WHERE qt.`rtd_agent_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_contact` lc,
    `lcl_quote` qt
  SET
    lc.`contact_name` = pMergedAcctContact,
    lc.`company_name` = pMergedAcctName,
    lc.`address` = pMergedAcctAddress,
    lc.`city` = pMergedAcctCity,
    lc.`state` = pMergedAcctState,
    lc.zip = pMergedAcctZip,
    lc.`country` = pMergedAcctCountry,
    lc.`phone1` = pMergedAcctPhone,
    lc.`fax1` = pMergedAcctFax,
    lc.`email1` = pMergedAcctEmail1,
    lc.`email2` = pMergedAcctEmail2,
    lc.`entered_datetime` = NOW(),
    lc.`entered_by_user_id` = pUpdatedUserId,
    lc.`modified_datetime` = NOW(),
    lc.`modified_by_user_id` = pUpdatedUserId
  WHERE lc.`id` = qt.`third_party_contact_id`
    AND qt.`third_party_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_quote` qt
  SET
    qt.`third_party_acct_no` = pMergedAcctNo
  WHERE qt.`third_party_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_contact` lc,
    `lcl_quote_ac` ac
  SET
    lc.`contact_name` = pMergedAcctContact,
    lc.`company_name` = pMergedAcctName,
    lc.`address` = pMergedAcctAddress,
    lc.`city` = pMergedAcctCity,
    lc.`state` = pMergedAcctState,
    lc.zip = pMergedAcctZip,
    lc.`country` = pMergedAcctCountry,
    lc.`phone1` = pMergedAcctPhone,
    lc.`fax1` = pMergedAcctFax,
    lc.`email1` = pMergedAcctEmail1,
    lc.`email2` = pMergedAcctEmail2,
    lc.`entered_datetime` = NOW(),
    lc.`entered_by_user_id` = pUpdatedUserId,
    lc.`modified_datetime` = NOW(),
    lc.`modified_by_user_id` = pUpdatedUserId
  WHERE lc.`id` = ac.`sp_contact_id`
    AND ac.`sp_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_quote_ac` ac
  SET
    ac.`sp_acct_no` = pMergedAcctNo
  WHERE ac.`sp_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_quote_import` im
  SET
    im.`export_agent_acct_no` = pMergedAcctNo
  WHERE im.`export_agent_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_quote_import` im
  SET
    im.`ipi_cfs_acct_no` = pMergedAcctNo
  WHERE im.`ipi_cfs_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_contact` lc,
    `lcl_quote_plan` pl
  SET
    lc.`contact_name` = pMergedAcctContact,
    lc.`company_name` = pMergedAcctName,
    lc.`address` = pMergedAcctAddress,
    lc.`city` = pMergedAcctCity,
    lc.`state` = pMergedAcctState,
    lc.zip = pMergedAcctZip,
    lc.`country` = pMergedAcctCountry,
    lc.`phone1` = pMergedAcctPhone,
    lc.`fax1` = pMergedAcctFax,
    lc.`email1` = pMergedAcctEmail1,
    lc.`email2` = pMergedAcctEmail2,
    lc.`entered_datetime` = NOW(),
    lc.`entered_by_user_id` = pUpdatedUserId,
    lc.`modified_datetime` = NOW(),
    lc.`modified_by_user_id` = pUpdatedUserId
  WHERE lc.`id` = pl.`sp_contact_id`
    AND pl.`sp_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_quote_plan` pl
  SET
    pl.`sp_acct_no` = pMergedAcctNo
  WHERE pl.`sp_acct_no` = pDisabledAcctNo ;
 UPDATE
    `lcl_ss_ac` ac
  SET
    ac.`ap_acct_no` = pMergedAcctNo
  WHERE ac.`ap_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_ss_ac` ac
  SET
    ac.`ar_acct_no` = pMergedAcctNo
  WHERE ac.`ar_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_ss_contact` lc
  SET
    lc.`contact_name` = pMergedAcctContact,
    lc.`company_name` = pMergedAcctName,
    lc.`address` = pMergedAcctAddress,
    lc.`city` = pMergedAcctCity,
    lc.`state` = pMergedAcctState,
    lc.zip = pMergedAcctZip,
    lc.`country` = pMergedAcctCountry,
    lc.`phone1` = pMergedAcctPhone,
    lc.`fax1` = pMergedAcctFax,
    lc.`email1` = pMergedAcctEmail1,
    lc.`email2` = pMergedAcctEmail2,
    lc.`entered_datetime` = NOW(),
    lc.`entered_by_user_id` = pUpdatedUserId,
    lc.`modified_datetime` = NOW(),
    lc.`modified_by_user_id` = pUpdatedUserId,
    lc.`tp_acct_no` = pMergedAcctNo
  WHERE lc.`tp_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_ss_detail` sd
  SET
    sd.`sp_acct_no` = pMergedAcctNo
  WHERE sd.`sp_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_ss_detail` sd
  SET
    sd.`recv_agent_acct_no` = pMergedAcctNo
  WHERE sd.`recv_agent_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_ss_exports` ex
  SET
    ex.`land_exit_carrier_acct_no` = pMergedAcctNo
  WHERE ex.`land_exit_carrier_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_ss_exports` ex
  SET
    ex.`export_agent_acct_no` = pMergedAcctNo
  WHERE ex.`export_agent_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_unit_ss` us
  SET
    us.`sp_acct_no` = pMergedAcctNo
  WHERE us.`sp_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_unit_ss_imports` im
  SET
    im.`origin_acct_no` = pMergedAcctNo
  WHERE im.`origin_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_unit_ss_imports` im
  SET
    im.`coloader_acct_no` = pMergedAcctNo
  WHERE im.`coloader_acct_no` = pDisabledAcctNo ;
  UPDATE
    `lcl_unit_ss_imports` im
  SET
    im.`coloader_devanning_acct_no` = pMergedAcctNo
  WHERE im.`coloader_devanning_acct_no` = pDisabledAcctNo ;
     UPDATE
    `bl_info` bi
  SET
   bi.`shipper_code` = pMergedAcctNo,
   bi.`shipper_nad` = CONCAT_WS('',pMergedAcctAddress,pMergedAcctCity, pMergedAcctState, pMergedAcctCountry, pMergedAcctZip)
   WHERE bi.`shipper_code` = pDisabledAcctNo;
   UPDATE
    `bl_info` bi
  SET
   bi.`cons_code` = pMergedAcctNo,
   bi.`cons_nad` = CONCAT_WS('', pMergedAcctAddress, pMergedAcctCity, pMergedAcctState, pMergedAcctCountry, pMergedAcctZip)
   WHERE bi.`cons_code` = pDisabledAcctNo;
   UPDATE
    `bl_info` bi
  SET
   bi.`notify1_code` = pMergedAcctNo,
   bi.`notify1_nad` = CONCAT_WS('',pMergedAcctAddress, pMergedAcctCity, pMergedAcctState, pMergedAcctCountry, pMergedAcctZip)
   WHERE bi.`notify1_code` = pDisabledAcctNo;
   UPDATE
    `bl_info` bi
  SET
   bi.`notify2_code` = pMergedAcctNo,
   bi.`notify2_nad` = CONCAT_WS('',pMergedAcctAddress, pMergedAcctCity, pMergedAcctState, pMergedAcctCountry, pMergedAcctZip)
   WHERE bi.`notify2_code` = pDisabledAcctNo; 
  UPDATE
    `warehouse` wh
  SET
    wh.`vendorno` = pMergedAcctNo
  WHERE wh.`vendorno` = pDisabledAcctNo ;    
END$$

DELIMITER ;

-- Mantis#3409 20-June-2016 @Mei
ALTER TABLE lcl_export_notification CHANGE email email TEXT CHARSET utf8 COLLATE utf8_general_ci NULL; 


-- Mantis #12829(FCL-EXPORTS) by priyanka on 21 june 2016
UPDATE fcl_bl_costcodes fcl 
JOIN fcl_bl fclbl ON fclbl.`Bol`= fcl.`BolId`
JOIN transaction_ledger tl ON tl.`Bill_Ladding_No`=fclbl.`BolId` AND  tl.`cost_id` = fcl.`code_id` SET fcl.`transaction_type`='AC'
WHERE tl.`Transaction_type`='AC' AND fcl.`transaction_type`='AP' AND tl.`Status`='Open' AND tl.`shipment_type` IN ('FCLE','FCLI'); 

-- Mantis item #12945 (FCL-EXPORTS) by priyanka on 21st june
ALTER TABLE `booking_fcl` ADD COLUMN `vgm_cut_off` DATETIME NULL AFTER `chassis_charge`;


---LCL Exports RDMS Port Of Discharge  by sathiya on 22 June

ALTER TABLE `ports_econo`  ADD COLUMN `fdntfy` VARCHAR(1) NULL AFTER `hazall`, 
ADD COLUMN `podscd` VARCHAR(5) NULL AFTER `fdntfy`; 

ALTER TABLE `lcl_port_configuration` ADD COLUMN `default_port_unloc` VARCHAR(5) NULL AFTER `print_imp_on_metric`;

-- Mantis item 9630 (LCL Exports) by Kuppusamy on 22 June
ALTER TABLE `lcl_port_configuration`
   ADD COLUMN `printofdollars` VARCHAR(1) DEFAULT 'N' AFTER `domest`;

--Manti item 10025 (FCL-EXPORTS & FCL IMPORTS) by priyanka on 23rd june
INSERT INTO genericcode_dup (Codetypeid,CODE,codedesc,Field1) VALUES('22','EE','Email Exports Invoice Notification','K');

INSERT INTO genericcode_dup (Codetypeid,CODE,codedesc,Field1) VALUES('22','FE','Fax Exports Invoice Notification','K');

-- only for local machine by Aravindhan.V For Mantis Item# 12561 on June 29 2016.
  ALTER TABLE `lcl_unit_ss` 
  ADD COLUMN `solas_cargo_weight_imperial` DECIMAL(13,3) UNSIGNED NULL AFTER `total_pieces`,
  ADD COLUMN `solas_cargo_weight_metric` DECIMAL(13,3) UNSIGNED NULL AFTER `solas_cargo_weight_imperial`,
  ADD COLUMN `solas_dunnage_weight_imperial` DECIMAL(13,3) UNSIGNED NULL AFTER `solas_cargo_weight_metric`,
  ADD COLUMN `solas_dunnage_weight_metric` DECIMAL(13,3) UNSIGNED NULL AFTER `solas_dunnage_weight_imperial`,
  ADD COLUMN `solas_tare_weight_imperial` DECIMAL(13,3) UNSIGNED NULL AFTER `solas_dunnage_weight_metric`,
  ADD COLUMN `solas_tare_weight_metric` DECIMAL(13,3) UNSIGNED NULL AFTER `solas_tare_weight_imperial`,
  ADD COLUMN `solas_verification_signature` VARCHAR(50) NULL AFTER `solas_dunnage_weight_metric`,
  ADD COLUMN `solas_verification_date` DATE NULL AFTER `solas_verification_signature`;
  
  INSERT INTO print_config(`screen_name`,`document_type`,`document_name`,`file_location`,`allow_print`)
  VALUES('LCLUnits','IN','VGM Declaration','c:/VGM_Declaration/1.pdf','No');

-- by Aravindhan.V For Mantis Item# 12561 on June 29 2016.
  ALTER TABLE `un_location`
  ADD COLUMN `lcle_rates_source_id` INT(11) NULL AFTER `bl_numbering`,
  ADD  INDEX `un_location_fk02` (`lcle_rates_source_id`),
  ADD CONSTRAINT `un_location_fk02` FOREIGN KEY (`lcle_rates_source_id`)
  REFERENCES `un_location`(`id`) ON UPDATE CASCADE ON DELETE RESTRICT;

-- by Vellaisamy For Mantis Item# 12565 on June 29 2016.
ALTER TABLE `fcl_bl_marks_no` ADD COLUMN `tare_weight_kgs` DOUBLE(15,3) NULL AFTER `update_by`, ADD COLUMN `tare_weight_lbs` DOUBLE(15,3) NULL AFTER `tare_weight_kgs`, ADD COLUMN `bottom_line_vgm_weight_kgs` DOUBLE(15,3) NULL AFTER `tare_weight_lbs`, ADD COLUMN `bottom_line_vgm_weight_lbs` DOUBLE(15,3) NULL AFTER `bottom_line_vgm_weight_kgs`, ADD COLUMN `verification_signature` VARCHAR(50) NULL AFTER `bottom_line_vgm_weight_lbs`, ADD COLUMN `verification_date` DATETIME NULL AFTER `verification_signature`;

INSERT INTO print_config(`screen_name`,`document_type`,`document_name`,`file_location`,`allow_print`) VALUES('BL','IN','VGM Declaration',NULL,'No');

-- June 30th 2016 by Lakshmi
delimiter $$

drop function if exists `CodeGetCodeById`$$

create function `CodeGetCodeById`( pId int(11) ) returns varchar(500) charset latin1
    reads sql data
    deterministic
MAIN: begin
    declare mReturnData varchar(500) default null;
    if isnull( pID ) = true or pID is null then
      return null;
    end if;
    
    select 
      `code` 
    into 
      mReturnData
    from 
      `genericcode_dup` 
    where 
      `id` = pId
    limit 1;
    
    return mReturnData;
end MAIN$$

delimiter ;

-- LCL Exports Mantis Item# 3409 by Aravindhan.V on 1/7/2016
alter table `mail_transactions` add column  module_reference_id int default null;

alter table lcl_export_notification drop to_name, drop company_name;


-- LCL Exports Mantis Item# 12925 by Shanmugam on 4/7/2016
INSERT INTO `property`(`name`,`value`,`type`,`description`)VALUES('report.lclbl.original.signature','/opt/reports/lclblsignature.png','COMMON',NULL);
INSERT INTO `print_config`(`screen_name`,`document_type`,`document_name`,`file_location`,`allow_print`) VALUES ( 'LCLBL','IN','Bill of Lading (Original SIGNED)',NULL,'Yes');

-- LCL Exports Mantis Item# 12909 by Kuppusamy on 4/7/2016

ALTER TABLE `role_duties`   
  ADD COLUMN `delete_unit` TINYINT(1) DEFAULT 0  NULL AFTER `prevent_exp_release`;

-- Mantis item# 0011937 by pal raj on 6th Jul 2016

INSERT INTO `item_master` (`item_desc`, `program`, `item_created_on`, `unique_code`) VALUES('Door Delivery Pool',NULL,NOW(),'LCLIDDP');

INSERT INTO item_treestructure(item_id,parent_id) VALUES((SELECT item_id FROM item_master WHERE item_desc='Door Delivery Pool' AND unique_code = 'LCLIDDP' LIMIT 1),
(SELECT item_id FROM item_master WHERE unique_code='LCLI' LIMIT 1));

INSERT INTO `item_master` (`item_desc`,`program`,`item_created_on`,`unique_code`) VALUES ('Door Delivery Search','/LclDoorDeliverySearch.do?methodName=search',NOW(),'LCLIDDS');

INSERT INTO item_treestructure (item_id, parent_id) VALUES((SELECT item_id FROM item_master WHERE item_desc = 'Door Delivery Search' AND unique_code = 'LCLIDDS'
LIMIT 1), (SELECT item_id FROM item_master WHERE item_desc = 'Door Delivery Pool' AND unique_code = 'LCLIDDP' LIMIT 1)) ;

INSERT INTO `role_item_assoc` (`role_id`,`item_id`,`modify`,`deleted`) VALUES (1,(SELECT item_id FROM item_master WHERE item_desc = 'Door Delivery Search'
AND unique_code = 'LCLIDDS' LIMIT 1), 'modify', 'delete');


ALTER TABLE `lcl_booking_pad` 
  ADD COLUMN `pickup_est_date` DATE NULL AFTER `pickup_hours`,
  ADD COLUMN `delivery_est_date` DATE NULL AFTER `delivery_hours`,
  ADD COLUMN `delivery_commercial` BOOLEAN DEFAULT 0 NOT NULL AFTER `delivered_datetime`,
  ADD COLUMN `delivery_need_proof` BOOLEAN DEFAULT 0 NOT NULL AFTER `delivery_commercial`,
  ADD COLUMN `lift_gate` BOOLEAN DEFAULT 0 NOT NULL AFTER `last_free_date`;

--  Mantis Item-12657(FCL-Exports) by Stefy on 6 July 2016

ALTER TABLE `terminal` ADD COLUMN `intra_booker_id` VARCHAR(20) NULL;

--  Mantis Item-13137(LCL-Imports) by Stefy on 6 July 2016

ALTER TABLE `terminal` ADD COLUMN `imports_door_delivery_email` VARCHAR(100) NULL;

INSERT INTO property(NAME,VALUE,TYPE,description) VALUES('DoorDeliveryComment','Door Delivery - For a quote please email us at','LCL','Door Delivery Comment');

-- This query only for Local and Econo

ALTER TABLE `lcl_booking_import` 
ADD COLUMN `door_delivery_comment` BOOLEAN DEFAULT TRUE NOT NULL AFTER `door_delivery_eta`;

---Mantis Item-12901(LCL-Exports) by Rathnapandian on 6 July 2016

ALTER TABLE lcl_unit
ADD COLUMN comments  VARCHAR(25) NULL AFTER remarks;

--  Mantis Item-13137(LCL-Imports) by Stefy on 6 July 2016
DELIMITER $$


DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBookingImport_update`$$

CREATE
    
    TRIGGER `after_LclBookingImport_update` AFTER UPDATE ON `lcl_booking_import` 
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT ;
    DECLARE eta_at_fd TEXT ;
   IF new.transshipment!=old.transshipment THEN
       SET updated_values=(SELECT concat_string(updated_values,'Transshipment',IF(old.transshipment=0,'No','Yes'),IF(new.transshipment=0,'No','Yes')));
    END IF;
   IF ((OLD.picked_up_datetime IS NULL AND NEW.picked_up_datetime IS NOT NULL) OR NEW.picked_up_datetime != OLD.picked_up_datetime OR (OLD.picked_up_datetime IS NOT NULL AND NEW.picked_up_datetime IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'PickUp Date',IFNULL(DATE_FORMAT(old.picked_up_datetime,'%d-%b-%Y'),''),IFNULL(DATE_FORMAT(new.picked_up_datetime,'%d-%b-%Y'),'')));
    END IF;
   IF ((OLD.go_datetime IS NULL AND NEW.go_datetime IS NOT NULL) OR NEW.go_datetime != OLD.go_datetime OR (OLD.go_datetime IS NOT NULL AND NEW.go_datetime IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'G/O Date',IFNULL(DATE_FORMAT(old.go_datetime,'%d-%b-%Y'),''),IFNULL(DATE_FORMAT(new.go_datetime,'%d-%b-%Y'),'')));
    END IF;
   IF ((OLD.fd_eta IS NULL AND NEW.fd_eta IS NOT NULL) OR NEW.fd_eta != OLD.fd_eta OR (OLD.fd_eta IS NOT NULL AND NEW.fd_eta IS NULL)) THEN
       SET eta_at_fd=(SELECT concat_string(eta_at_fd,'ETA at FD',IFNULL(DATE_FORMAT(old.fd_eta,'%d-%b-%Y'),''),IFNULL(DATE_FORMAT(new.fd_eta,'%d-%b-%Y'),'')));
    END IF;
   IF ((OLD.sub_house_bl IS NULL AND NEW.sub_house_bl IS NOT NULL) OR NEW.sub_house_bl != OLD.sub_house_bl OR (OLD.sub_house_bl IS NOT NULL AND NEW.sub_house_bl IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'Sub House BL',IFNULL(old.sub_house_bl,''),IFNULL(new.sub_house_bl,'')));
   END IF;
    IF ((OLD.customs_entry_no IS NULL AND NEW.customs_entry_no IS NOT NULL AND NEW.customs_entry_no !='') OR NEW.customs_entry_no != OLD.customs_entry_no OR (OLD.customs_entry_no IS NOT NULL AND OLD.customs_entry_no !='' AND NEW.customs_entry_no IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'Entry#',IFNULL(old.customs_entry_no,''),IFNULL(new.customs_entry_no,'')));
   END IF;
   IF new.declared_value_estimated!=old.declared_value_estimated THEN
       SET updated_values=(SELECT concat_string(updated_values,'Value Estimated',IF(old.declared_value_estimated=0,'No','Yes'),IF(new.declared_value_estimated=0,'No','Yes')));
   END IF;
   IF new.declared_weight_estimated!=old.declared_weight_estimated THEN
       SET updated_values=(SELECT concat_string(updated_values,'Weight Estimated',IF(old.declared_weight_estimated=0,'No','Yes'),IF(new.declared_weight_estimated=0,'No','Yes')));
   END IF;
   IF ((OLD.inbond_via IS NULL AND NEW.inbond_via IS NOT NULL) OR NEW.inbond_via != OLD.inbond_via OR (OLD.inbond_via IS NOT NULL AND NEW.inbond_via IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'Inbond Via',IFNULL(old.inbond_via,''),IFNULL(new.inbond_via,'')));
   END IF;
   IF ((OLD.it_class IS NULL AND NEW.it_class IS NOT NULL) OR NEW.it_class != OLD.it_class OR (OLD.it_class IS NOT NULL AND NEW.it_class IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'IT Class',IFNULL(old.it_class,''),IFNULL(new.it_class,'')));
   END IF;
   IF ((OLD.customs_entry_class IS NULL AND NEW.customs_entry_class IS NOT NULL) OR NEW.customs_entry_class != OLD.customs_entry_class OR (OLD.customs_entry_class IS NOT NULL AND NEW.customs_entry_class IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'Class of Entry',IFNULL(old.customs_entry_class,''),IFNULL(new.customs_entry_class,'')));
   END IF;
   IF new.elite_ipi_loaded !=old.elite_ipi_loaded THEN
       SET updated_values=(SELECT concat_string(updated_values,'Loaded on IPI Container',IF(old.elite_ipi_loaded=0,'No','Yes'),IF(new.elite_ipi_loaded=0,'No','Yes')));
       END IF;
   IF isNotEqual(OLD.picked_up_by,NEW.picked_up_by) THEN
       SET updated_values=(`concat_string`(updated_values,'Picked By',OLD.picked_up_by,NEW.picked_up_by));
       END IF;
   IF ((OLD.door_delivery_eta IS NULL AND NEW.door_delivery_eta IS NOT NULL) OR NEW.door_delivery_eta != OLD.door_delivery_eta OR (OLD.door_delivery_eta IS NOT NULL AND NEW.door_delivery_eta IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'Door Delivery ETA',IFNULL(DATE_FORMAT(old.door_delivery_eta,'%d-%b-%Y'),''),IFNULL(DATE_FORMAT(new.door_delivery_eta,'%d-%b-%Y'),'')));
    END IF;
    IF new.express_release!=old.express_release THEN
      SET updated_values=(SELECT concat_string(updated_values,'Express Release',IF(old.express_release=0,'No','Yes'),IF(new.express_release=0,'No','Yes')));
    END IF;
    IF new.door_delivery_comment!=old.door_delivery_comment THEN
       SET updated_values=(SELECT concat_string(updated_values,'Door Delivery Comment',IF(old.door_delivery_comment=0,'No','Yes'),IF(new.door_delivery_comment=0,'No','Yes')));
    END IF;
    IF updated_values IS NOT NULL THEN
      SET updated_values=CONCAT(updated_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(OLD.file_number_id,'DR-AutoNotes',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    IF eta_at_fd IS NOT NULL THEN
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(OLD.file_number_id,'DR-AutoNotes',eta_at_fd,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    
  END;
$$

DELIMITER ;

--Mantis item 12921 LCL Exports by Priyanka on 6th july 2016
INSERT INTO print_config(`screen_name`,`document_type`,`document_name`,`file_location`,`allow_print`) VALUES('LCLBL','IN','Consolidation Mini Manifest','c:/LCL_BL/5.pdf','No');

--Mantis item 12785 LCL Exports by Shanmugam on 6th july 2016
ALTER TABLE `lcl_bl`     ADD COLUMN `value_of_goods` DECIMAL(10,2) NULL AFTER `spot_rate_ofrate`,     ADD COLUMN `documentation` TINYINT(1) NULL AFTER `value_of_goods`;

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBl_update`$$

CREATE
    TRIGGER `after_LclBl_update` AFTER UPDATE ON `lcl_bl` 
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT;
    DECLARE fileStatus VARCHAR(2);
    DECLARE notiStatusFlag BOOLEAN DEFAULT FALSE;
    DECLARE bl_values TEXT ;
    DECLARE changesFlag BOOLEAN DEFAULT FALSE;
    SELECT lfn.status INTO fileStatus FROM lcl_file_number lfn WHERE lfn.id=OLD.file_number_id;
     IF ((old.ship_acct_no IS NULL AND new.ship_acct_no IS NOT NULL)OR new.ship_acct_no!=old.ship_acct_no OR(new.ship_acct_no IS NULL AND old.ship_acct_no IS NOT NULL))THEN
    SET @oldClientValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.ship_acct_no);
    SET @newClientValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.ship_acct_no);
    SET updated_values=(SELECT concat_string (updated_values,'Shipper',IFNULL(@oldClientValues,''),IFNULL(@newClientValues,'')));
    SET changesFlag = TRUE ;
    END IF;
     IF ((OLD.cons_acct_no IS NULL AND NEW.cons_acct_no IS NOT NULL) OR NEW.cons_acct_no != OLD.cons_acct_no OR (OLD.cons_acct_no IS NOT NULL AND NEW.cons_acct_no IS NULL)) THEN
     SET @oldConsValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.cons_acct_no);
     SET @newConsValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.cons_acct_no);
     SET updated_values=(SELECT concat_string(updated_values,'Consignee',IFNULL(@oldConsValues,''),IFNULL(@newConsValues,'')));
     SET changesFlag = TRUE ;
   END IF;
    IF ((OLD.noty_acct_no IS NULL AND NEW.noty_acct_no IS NOT NULL) OR NEW.noty_acct_no != OLD.noty_acct_no OR (OLD.noty_acct_no IS NOT NULL AND NEW.noty_acct_no IS NULL)) THEN
     SET @oldNotyValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.noty_acct_no);
     SET @newNotyValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.noty_acct_no);
     SET updated_values=(SELECT concat_string(updated_values,'Notify Party',IFNULL(@oldNotyValues,''),IFNULL(@newNotyValues,'')));
     SET changesFlag = TRUE ;
   END IF;
     IF ((old.fwd_acct_no IS NULL AND new.fwd_acct_no IS NOT NULL)OR new.fwd_acct_no!=old.fwd_acct_no OR(new.fwd_acct_no IS NULL AND old.fwd_acct_no IS NOT NULL))THEN
    SET @oldClientValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.fwd_acct_no);
    SET @newClientValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.fwd_acct_no);
    SET updated_values=(SELECT concat_string (updated_values,'Forwarder',IFNULL(@oldClientValues,''),IFNULL(@newClientValues,'')));
    SET changesFlag = TRUE ;
    END IF;
   IF ((OLD.third_party_acct_no IS NULL AND NEW.third_party_acct_no IS NOT NULL) OR NEW.third_party_acct_no != OLD.third_party_acct_no OR (OLD.third_party_acct_no IS NOT NULL AND NEW.third_party_acct_no IS NULL)) THEN
     SET @oldThirdValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.third_party_acct_no);
     SET @newThirdValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.third_party_acct_no);
     SET updated_values=(SELECT concat_string(updated_values,'Third Party',IFNULL(@oldThirdValues,''),IFNULL(@newThirdValues,'')));
     SET changesFlag = TRUE ;
   END IF;
     IF ((OLD.sup_acct_no IS NULL AND NEW.sup_acct_no IS NOT NULL) OR NEW.sup_acct_no!=OLD.sup_acct_no OR (OLD.sup_acct_no IS NOT NULL AND NEW.sup_acct_no IS NULL)) THEN
   SET @oldSupValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.sup_acct_no);
   SET @newSupValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.sup_acct_no);
   SET updated_values=(SELECT concat_string(updated_values,'Supplier',IFNULL(@oldSupValues,''),IFNULL(@newSupValues,'')));
   END IF;
   IF new.billing_terminal!=old.billing_terminal THEN
    SET @oldBillingTerminal=(SELECT CONCAT(terminal_location,'/',trmnum) FROM terminal WHERE trmnum=old.billing_terminal);
    SET @newBillingTerminal=(SELECT CONCAT(terminal_location,'/',trmnum) FROM terminal WHERE trmnum=new.billing_terminal);
    SET updated_values=(SELECT concat_string(updated_values,'Term to do BL',IFNULL(@oldBillingTerminal,''),IFNULL(@newBillingTerminal,'')));
   END IF;
      IF new.terms_type1!=old.terms_type1 THEN
       SET updated_values=(SELECT concat_string(updated_values,'Termstype1',old.terms_type1,new.terms_type1));
    END IF;
 
        IF new.terms_type2!=old.terms_type2 THEN
       SET updated_values=(SELECT concat_string(updated_values,'Termstype2',old.terms_type2,new.terms_type2));
    END IF;
         IF new.type2_date!=old.type2_date THEN
       SET updated_values=(SELECT concat_string(updated_values,'Type2Date',old.type2_date,new.type2_date));
    END IF;
         IF new.point_of_origin!=old.point_of_origin THEN
       SET updated_values=(SELECT concat_string(updated_values,'PointOfOrigin',old.point_of_origin,new.point_of_origin));
    END IF;
    IF new.bl_owner_id!=old.bl_owner_id THEN
     SET bl_values=(SELECT concat_string(updated_values,'BL Owner is Changed',(SELECT login_name FROM user_details WHERE user_id=old.bl_owner_id),(SELECT login_name FROM user_details WHERE user_id=new.bl_owner_id)));
       INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
       VALUES(OLD.file_number_id,'BL-AutoNotes',bl_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    IF old.poo_id!=new.poo_id THEN
	SET updated_values=(SELECT concat_string(updated_values,'PlaceOfReceipt',(SELECT un_loc_name FROM un_location WHERE id=old.poo_id),(SELECT un_loc_name FROM un_location WHERE id=new.poo_id)));
    END IF;
   IF old.fd_id!=new.fd_id THEN
	SET updated_values=(SELECT concat_string(updated_values,'Destination',(SELECT un_loc_name FROM un_location WHERE id=old.fd_id),(SELECT un_loc_name FROM un_location WHERE id=new.fd_id)));
	SET changesFlag = TRUE ;
    END IF;
          IF new.spot_rate!=old.spot_rate THEN
      SET updated_values=(SELECT concat_string(updated_values,'SpotRate',IF(old.spot_rate=0,'No','Yes'),IF(new.spot_rate=0,'No','Yes')));
    END IF;
    IF new.rate_type!=old.rate_type THEN
    SET updated_values=(SELECT concat_string(updated_values,'RateType',old.rate_type,new.rate_type));
    END IF;
    IF ((OLD.booked_ss_header_id IS NULL AND NEW.booked_ss_header_id IS NOT NULL) OR NEW.booked_ss_header_id!=OLD.booked_ss_header_id OR (OLD.booked_ss_header_id IS NOT NULL AND NEW.booked_ss_header_id IS NULL)) THEN
     SET @oldScheduleValue = (SELECT CONCAT (l.`schedule_no` ,' / ', DATE_FORMAT((SELECT d.std FROM lcl_ss_detail d WHERE d.ss_header_id=old.booked_ss_header_id  AND d.trans_mode='V'),'%d-%b-%Y'))  FROM lcl_ss_header  l  WHERE  l.id=old.booked_ss_header_id);
     SET @newScheduleValue = (SELECT CONCAT (l.`schedule_no` ,' / ', DATE_FORMAT((SELECT d.std FROM lcl_ss_detail d WHERE d.ss_header_id=new.booked_ss_header_id  AND d.trans_mode='V'),'%d-%b-%Y'))  FROM lcl_ss_header  l  WHERE  l.id=new.booked_ss_header_id);
     SET updated_values=(SELECT concat_string(updated_values,'Booked Voyage is changed',IFNULL(@oldScheduleValue,''),IFNULL(@newScheduleValue,'')) );
       SET changesFlag = TRUE ;
    END IF;
    IF new.billing_type!=old.billing_type THEN
    SET updated_values=(SELECT concat_string(updated_values,'Terms',old.billing_type,new.billing_type));
    SET changesFlag = TRUE ;
    END IF;
     IF new.billing_type!=old.billing_type THEN
      SET changesFlag = TRUE ;
    END IF;
     IF new.bill_to_party!=old.bill_to_party THEN
      SET changesFlag = TRUE ;
    END IF;
    IF new.free_bl!=old.free_bl THEN
    SET updated_values=(SELECT concat_string(updated_values,'Free B/L',IF(old.free_bl=0,'No','Yes'),IF(new.free_bl=0,'No','Yes')));
    SET changesFlag = TRUE ;
    END IF;
    IF new.insurance!=old.insurance THEN
    SET updated_values=(SELECT concat_string(updated_values,'Insurance',IF(old.insurance=0,'No','Yes'),IF(new.insurance=0,'No','Yes')));
    SET changesFlag = TRUE ;
    END IF;
    IF new.value_of_goods!=old.value_of_goods THEN
    SET updated_values=(SELECT concat_string(updated_values,'Value Of Goods',old.value_of_goods,new.value_of_goods));
    SET changesFlag = TRUE ;
    END IF;
    IF new.documentation!=old.documentation THEN
    SET updated_values=(SELECT concat_string(updated_values,'Documentation',IF(old.documentation=0,'No','Yes'),IF(new.documentation=0,'No','Yes')));
    SET changesFlag = TRUE ;
    END IF;
    IF updated_values IS NOT NULL THEN
    SET updated_values=CONCAT('UPDATED ->',updated_values);
       IF (fileStatus='M'  AND changesFlag = TRUE) THEN
       SELECT IF(COUNT(*)>0,TRUE,FALSE) INTO notiStatusFlag FROM lcl_export_notification len
       WHERE len.file_number_id=OLD.file_number_id AND len.file_status='Changes' AND len.status='Pending';
       IF (notiStatusFlag=FALSE) THEN
        INSERT INTO lcl_export_notification (file_number_id, file_status,STATUS,entered_datetime,entered_by_user_id)
        VALUES(OLD.file_number_id,'Changes','Pending',NOW(),new.modified_by_user_id);
        END IF;
        END IF;
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(OLD.file_number_id,'BL-AutoNotes',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;

-- Mantis item# 0011937 by pal raj on 7th Jul 2016

ALTER TABLE `lcl_booking_pad` MODIFY scac VARCHAR(50);

INSERT INTO codetype (description,editable) VALUES ('Door Delivery Status',1);
INSERT INTO genericcode_dup (Codetypeid,CODE,codedesc) VALUES ((SELECT `codetypeid` FROM `codetype` WHERE `description` ='Door Delivery Status'),'P','Pending(Cargo at CFS)');
INSERT INTO genericcode_dup (Codetypeid,CODE,codedesc) VALUES ((SELECT `codetypeid` FROM `codetype` WHERE `description` ='Door Delivery Status'),'O','Out For Delivery');
INSERT INTO genericcode_dup (Codetypeid,CODE,codedesc) VALUES ((SELECT `codetypeid` FROM `codetype` WHERE `description` ='Door Delivery Status'),'D','Delivered');
INSERT INTO genericcode_dup (Codetypeid,CODE,codedesc) VALUES ((SELECT `codetypeid` FROM `codetype` WHERE `description` ='Door Delivery Status'),'F','Final/Closed');
INSERT INTO genericcode_dup (Codetypeid,CODE,codedesc) VALUES ((SELECT `codetypeid` FROM `codetype` WHERE `description` ='Door Delivery Status'),'PC','Pending contact');
INSERT INTO genericcode_dup (Codetypeid,CODE,codedesc) VALUES ((SELECT `codetypeid` FROM `codetype` WHERE `description` ='Door Delivery Status'),'DR','Docs requested');
INSERT INTO genericcode_dup (Codetypeid,CODE,codedesc) VALUES ((SELECT `codetypeid` FROM `codetype` WHERE `description` ='Door Delivery Status'),'PD','Pending docs');
INSERT INTO genericcode_dup (Codetypeid,CODE,codedesc) VALUES ((SELECT `codetypeid` FROM `codetype` WHERE `description` ='Door Delivery Status'),'PA','Pending Appt');
INSERT INTO genericcode_dup (Codetypeid,CODE,codedesc) VALUES ((SELECT `codetypeid` FROM `codetype` WHERE `description` ='Door Delivery Status'),'PB','Pending balance');
INSERT INTO genericcode_dup (Codetypeid,CODE,codedesc) VALUES ((SELECT `codetypeid` FROM `codetype` WHERE `description` ='Door Delivery Status'),'OH','Cargo on HOLD');
INSERT INTO genericcode_dup (Codetypeid,CODE,codedesc) VALUES ((SELECT `codetypeid` FROM `codetype` WHERE `description` ='Door Delivery Status'),'DP','Dispatched');

-- Mantis item# 0012785 by shanmugam on 7th Jul 2016 
UPDATE  lcl_bl bl JOIN lcl_booking bok ON bl.file_number_id = bok.file_number_id SET bl.insurance = bok.insurance;
UPDATE  lcl_bl bl JOIN lcl_booking bok ON bl.file_number_id = bok.file_number_id SET bl.documentation = bok.documentation;
UPDATE  lcl_bl bl JOIN lcl_booking bok ON bl.file_number_id = bok.file_number_id SET bl.value_of_goods = bok.value_of_goods;

-- by pal raj on 7th Jul 2016

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclSsDetail_update`$$

CREATE
   
    TRIGGER `after_LclSsDetail_update` AFTER UPDATE ON `lcl_ss_detail` 
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT ;
    DECLARE mServiceType VARCHAR(2);
    DECLARE mReportingDate DATETIME;
    DECLARE mReportingDateChanged BOOLEAN DEFAULT FALSE;
    IF new.sp_acct_no!=old.sp_acct_no THEN
		SET updated_values=(SELECT concat_string(updated_values,'Carrier',(SELECT acct_name FROM trading_partner WHERE acct_no=old.sp_acct_no),(SELECT acct_name FROM trading_partner WHERE acct_no=new.sp_acct_no)));
	END IF;
	    IF new.sp_reference_name!=old.sp_reference_name THEN
       SET updated_values=(SELECT concat_string(updated_values,'Vessel',old.sp_reference_name,new.sp_reference_name));
    END IF;
    IF new.departure_id!=old.departure_id THEN
		SET updated_values=(SELECT concat_string(updated_values,'Departure Pier',(SELECT un_loc_name FROM un_location WHERE id=old.departure_id),(SELECT un_loc_name FROM un_location WHERE id=new.departure_id)));
	END IF;
    IF new.arrival_id!=old.arrival_id THEN
       SET updated_values=(SELECT concat_string(updated_values,'Arrival Pier',(SELECT un_loc_name FROM un_location WHERE id=old.arrival_id),(SELECT un_loc_name FROM un_location WHERE id=new.arrival_id)));
    END IF;
    IF new.sp_reference_no!=old.sp_reference_no THEN
       SET updated_values=(SELECT concat_string(updated_values,'SS Voyage #',old.sp_reference_no,new.sp_reference_no));
    END IF;
    IF new.std!=old.std THEN
       SET updated_values=(SELECT concat_string(updated_values,'ETD Sailing Date',DATE_FORMAT(old.std,'%d-%b-%Y'),DATE_FORMAT(new.std,'%d-%b-%Y')));
    END IF;
    IF new.sta!=old.sta THEN
       SET updated_values=(SELECT concat_string(updated_values,'ETA at POD',DATE_FORMAT(old.sta,'%d-%b-%Y'),DATE_FORMAT(new.sta,'%d-%b-%Y')));
    END IF;
    IF new.remarks!=old.remarks THEN
       SET updated_values=(SELECT concat_string(updated_values,'Trucking Remarks',old.remarks,new.remarks));
    END IF;
    IF ((old.doc_received IS NULL AND new.doc_received IS NOT NULL) OR new.doc_received != old.doc_received OR (old.doc_received IS NOT NULL AND new.doc_received IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'Doc Received',IFNULL(DATE_FORMAT(old.doc_received,'%d-%b-%Y'),''),IFNULL(DATE_FORMAT(new.doc_received,'%d-%b-%Y'),'')));
    END IF;
        IF ((old.atd IS NULL AND new.atd IS NOT NULL) OR new.atd != old.atd OR (old.atd IS NOT NULL AND new.atd IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'ATD',IFNULL(DATE_FORMAT(old.atd,'%d-%b-%Y'),''),IFNULL(DATE_FORMAT(new.atd,'%d-%b-%Y'),'')));
    END IF;
    IF updated_values IS NOT NULL THEN
    SET updated_values=CONCAT(updated_values);
        INSERT INTO lcl_ss_remarks(ss_header_id,TYPE,STATUS,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(old.ss_header_id,'auto','',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;

-- Mantis item# 10737(Logiware Accounting) by Nambu on 12th Jul 2016 
INSERT INTO job (NAME,`frequency`,`day1`,`day2`,`hour`,`minute`,`enabled`,`updated_by`,`updated_date`,`class_name`) VALUES
('Aging SummaryReport Daily','DAILY',0,0,23,30,0,'NAMBU',NOW(),'com.logiware.common.job.AgingSummaryReportJob');

-- by pal raj on 13th Jul 2016 

INSERT INTO print_config(`screen_name`,`document_type`,`document_name`,`file_location`,`allow_print`) VALUES('LCLBL','IN','SS Master From House BL',NULL,'No');

-- Mantis Issue 12461 (LCL Exports) by Kuppusamy on 13th Jul 2016
ALTER TABLE `agency_info`
  ADD COLUMN `lcl_agent_level_brand` VARCHAR(10) NULL AFTER `default_agent`;

---Mantis item 11589 Lcl-Imports by priyanka on 13th Jul 2016

ALTER TABLE `lcl_booking_pad` ADD COLUMN `delivery_notes` VARCHAR(200) NULL AFTER `lift_gate`;


---- Mantis # 12661 LCL Exports by sathiya on 14th july 2016

DELIMITER $$

DROP PROCEDURE IF EXISTS `LCLScheduleList`$$

CREATE PROCEDURE `LCLScheduleList`(
  IN pPOO_ID INT (11),
  IN pPOL_ID INT (11),
  IN pPOD_ID INT (11),
  IN pFD_ID INT (11),
  IN pTransMode VARCHAR (50),
  IN pServiceType VARCHAR(1)
)
    READS SQL DATA
MAIN :
BEGIN
  DECLARE mSLS VARCHAR (64) DEFAULT "LCLScheduleList" ;
  DECLARE mSLH VARCHAR (256) DEFAULT "" ;
  DECLARE mSLT VARCHAR (128) DEFAULT UUID() ;
  DECLARE mErrMsg VARCHAR (1024) DEFAULT "" ;
  DECLARE mDEBUG BOOL DEFAULT FALSE ;
  DECLARE mTransMode VARCHAR (50) DEFAULT "" ;
  SET mSLH = CONCAT(": pPOO_ID (",pPOO_ID,"), pPOL_ID (",pPOL_ID,"), pPOD_ID (",pPOD_ID,")") ;
  SET mSLH = CONCAT(mSLH,", pFD_ID (",pFD_ID,"), pTransMode (",pTransMode,"): ") ;
  IF ISNULL(pPOO_ID) OR pPOO_ID < 1 THEN
  SET mErrMsg = CONCAT("VALIDATION",mSLH,"Stopped; pPOO_ID invalid.") ;
  CALL LogSQLEntry (3, mSLS, mSLT, mErrMsg) ;
  SELECT mErrMsg ;
  LEAVE MAIN ;
  END IF ;
  IF ISNULL(pPOL_ID) OR pPOL_ID < 1 THEN
  SET mErrMsg = CONCAT("VALIDATION",mSLH,"Stopped; pPOL_ID invalid.") ;
  CALL LogSQLEntry (3, mSLS, mSLT, mErrMsg) ;
  SELECT mErrMsg ;
  LEAVE MAIN ;
  END IF ;
  IF ISNULL(pPOD_ID) OR pPOD_ID < 1 THEN
   SET mErrMsg = CONCAT("VALIDATION",mSLH,"Stopped; pPOD_ID invalid.") ;
  CALL LogSQLEntry (3, mSLS, mSLT, mErrMsg) ;
  SELECT mErrMsg ;
  LEAVE MAIN ;
  END IF ;
  IF ISNULL(pFD_ID) OR pFD_ID < 1 THEN
   SET mErrMsg = CONCAT("VALIDATION",mSLH,"Stopped; pFD_ID invalid.") ;
  CALL LogSQLEntry (3, mSLS, mSLT, mErrMsg) ;
  SELECT mErrMsg ;
  LEAVE MAIN ;
  END IF ;
  IF ISNULL(pTransMode) OR TRIM(pTransMode) = "" OR pTransMode = "*" THEN
  SET mTransMode = "A,R,T,V" ;
  ELSE
  SET mTransMode = pTransMode ;
  END IF ;
  IF mDEBUG = TRUE
  THEN CALL LogSQLEntry (1,mSLS,mSLT,CONCAT("DEBUG", mSLH, "Begins.")) ;
  END IF ;
  SELECT
    lsh.id AS ssHeaderId,
    lsh.schedule_no AS voyageNo,
    UnLocationGetCodeByID (lsh.origin_id) AS pooUnlocCode,
    UnLocationGetCodeByID (lsh.destination_id) AS fdUnloCode,
    (
			SELECT
				acct_name
			FROM
				trading_partner
			WHERE acct_no = lsd.sp_acct_no
			LIMIT 1
		) AS carrierName,
    lsd.sp_acct_no AS carrierAcctNo,
    lsd.sp_reference_no AS ssVoyage,
    lsd.sp_reference_name AS vesselName,
    UnLocationGetCodeByID (lsd.departure_id) AS polUnlocCode,
    UnLocationGetCodeByID (lsd.arrival_id) AS podUnloCode,
    UnLocationGetNameStateCntryByID (lsd.departure_id) AS departPier,
    lsd.std AS sailingDate,
    lsd.sta AS etaPod,
    lsd.relay_lrd_override AS relaylrdOverride,
    lsd.relay_tt_override AS relayttOverride
  FROM
    lcl_ss_header lsh
    JOIN lcl_ss_detail lsd
			ON (
        lsd.ss_header_id = lsh.id
        AND lsd.trans_mode =pTransMode
      )
  WHERE lsh.service_type = pServiceType
    AND lsh.status <> 'V'
    AND
    (
			lsh.origin_id = pPOO_ID
			OR lsh.origin_id = pPOL_ID
		)
    AND
			(
				lsh.destination_id = pPOD_ID
				OR lsh.destination_id = pFD_ID
			)
    AND lsd.std > NOW()
    ORDER BY lsd.std ASC;
    IF mDEBUG = TRUE THEN
      CALL LogSQLEntry (1,mSLS,mSLT,"DEBUG: Ends normally.") ;
    END IF ;
END MAIN$$

DELIMITER ;


DELIMITER $$

DROP PROCEDURE IF EXISTS `BL_LCLScheduleListUpComing`$$

CREATE PROCEDURE `BL_LCLScheduleListUpComing`(
	IN pPOO_ID INT(11),
	IN pPOL_ID INT(11),
	IN pPOD_ID INT(11),
	IN pFD_ID INT(11),
	IN pTransMode VARCHAR(50),
	IN pServiceType VARCHAR(1)
)
    READS SQL DATA
MAIN: BEGIN
	DECLARE mSLS VARCHAR(64) DEFAULT "BLLCLScheduleListUpComing";
	DECLARE mSLH VARCHAR(256) DEFAULT "";
	DECLARE mSLT VARCHAR(128) DEFAULT UUID();
	DECLARE mErrMsg VARCHAR(1024) DEFAULT "";
	DECLARE mDEBUG BOOL DEFAULT FALSE;
	DECLARE mTransMode VARCHAR(50) DEFAULT "";
	SET mSLH = CONCAT( ": pPOO_ID (", pPOO_ID, "), pPOL_ID (", pPOL_ID, "), pPOD_ID (", pPOD_ID, ")" );
	SET mSLH = CONCAT( mSLH, ", pFD_ID (", pFD_ID, "), pTransMode (", pTransMode, "): " );
	IF ISNULL( pPOO_ID ) OR pPOO_ID < 1 THEN
		SET mErrMsg =  CONCAT( "VALIDATION", mSLH, "Stopped; pPOO_ID invalid." ) ;
		CALL LogSQLEntry( 3, mSLS, mSLT, mErrMsg );
		SELECT mErrMsg;
		LEAVE MAIN;
	END IF;
	IF ISNULL( pPOL_ID ) OR pPOL_ID < 1 THEN
		SET mErrMsg =  CONCAT( "VALIDATION", mSLH, "Stopped; pPOL_ID invalid." ) ;
		CALL LogSQLEntry( 3, mSLS, mSLT, mErrMsg );
		SELECT mErrMsg;
		LEAVE MAIN;
	END IF;
	IF ISNULL( pPOD_ID ) OR pPOD_ID < 1 THEN
		SET mErrMsg =  CONCAT( "VALIDATION", mSLH, "Stopped; pPOD_ID invalid." ) ;
		CALL LogSQLEntry( 3, mSLS, mSLT, mErrMsg );
		SELECT mErrMsg;
		LEAVE MAIN;
	END IF;
	IF ISNULL( pFD_ID ) OR pFD_ID < 1 THEN
		SET mErrMsg =  CONCAT( "VALIDATION", mSLH, "Stopped; pFD_ID invalid." ) ;
		CALL LogSQLEntry( 3, mSLS, mSLT, mErrMsg );
		SELECT mErrMsg;
		LEAVE MAIN;
	END IF;
	IF ISNULL( pTransMode ) OR TRIM( pTransMode ) = "" OR pTransMode = "*" THEN
		SET mTransMode = "A,R,T,V";
	ELSE
		SET mTransMode = pTransMode;
	END IF;
	IF mDEBUG = TRUE THEN
		CALL LogSQLEntry( 1, mSLS, mSLT, CONCAT( "DEBUG", mSLH, "Begins." ) );
	END IF;
	SELECT
    lsh.id AS ssHeaderId,
    lsh.schedule_no AS voyageNo,
    UnLocationGetCodeByID (lsh.origin_id) AS pooUnlocCode,
    UnLocationGetCodeByID (lsh.destination_id) AS fdUnloCode,
    (SELECT
      acct_name
    FROM
      trading_partner
    WHERE acct_no = lsd.sp_acct_no
    LIMIT 1) AS carrierName,
    lsd.sp_acct_no AS carrierAcctNo,
    lsd.sp_reference_no AS ssVoyage,
    lsd.sp_reference_name AS vesselName,
    UnLocationGetCodeByID (lsd.departure_id) AS polUnlocCode,
    UnLocationGetCodeByID (lsd.arrival_id) AS podUnloCode,
    UnLocationGetNameStateCntryByID (lsd.departure_id) AS departPier,
    lsd.std AS sailingDate,
    lsd.sta AS etaPod,
    lsd.relay_lrd_override AS relaylrdOverride,
    lsd.relay_tt_override AS relayttOverride
  FROM
    lcl_ss_header lsh
    JOIN lcl_ss_detail lsd
      ON (
        lsd.ss_header_id = lsh.id
        AND lsd.trans_mode =pTransMode
      )
     WHERE lsh.service_type = pServiceType
      AND lsh.status <> 'V'
   AND
    (
			lsh.origin_id = pPOO_ID
			OR lsh.origin_id = pPOL_ID
		)
    AND
			(
				lsh.destination_id = pPOD_ID
				OR lsh.destination_id = pFD_ID
			)
    AND lsd.std  >=DATE_SUB(NOW(),INTERVAL 60 DAY)
    ORDER BY lsd.std DESC;
    IF mDEBUG = TRUE THEN
      CALL LogSQLEntry( 1, mSLS, mSLT, "DEBUG: Ends normally." );
    END IF;
END MAIN$$

DELIMITER ;



-- Mantis item# 13453(Logiware FCL-Imports) by Stefy on 14th Jul 2016

INSERT INTO `genericcode_dup` (`Codetypeid`, `code`, `codedesc`, `Field1`, `Field2`, `Field3`, `Field4`, `Field5`, `Field6`, `Field7`, `Field8`, `Field9`, `Field10`, `Field11`, `Field12`) VALUES('60','100012','BL CLOSED',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

INSERT INTO `genericcode_dup` (`Codetypeid`, `code`, `codedesc`, `Field1`, `Field2`, `Field3`, `Field4`, `Field5`, `Field6`, `Field7`, `Field8`, `Field9`, `Field10`, `Field11`, `Field12`) VALUES('60','100013','BL OPENED',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

INSERT INTO `genericcode_dup` (`Codetypeid`, `code`, `codedesc`, `Field1`, `Field2`, `Field3`, `Field4`, `Field5`, `Field6`, `Field7`, `Field8`, `Field9`, `Field10`, `Field11`, `Field12`) VALUES('60','100014','BL AUDITED',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

INSERT INTO `genericcode_dup` (`Codetypeid`, `code`, `codedesc`, `Field1`, `Field2`, `Field3`, `Field4`, `Field5`, `Field6`, `Field7`, `Field8`, `Field9`, `Field10`, `Field11`, `Field12`) VALUES('60','100015','BL AUDIT CANCELLED',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);


--Mantis item 11589 (LCL-Imports) by priyanka on 14th July 2016

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBookingImport_update`$$

CREATE
   
    TRIGGER `after_LclBookingImport_update` AFTER UPDATE ON `lcl_booking_import` 
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT ;
    DECLARE eta_at_fd TEXT ;
   IF new.transshipment!=old.transshipment THEN
       SET updated_values=(SELECT concat_string(updated_values,'Transshipment',IF(old.transshipment=0,'No','Yes'),IF(new.transshipment=0,'No','Yes')));
    END IF;
   IF ((OLD.picked_up_datetime IS NULL AND NEW.picked_up_datetime IS NOT NULL) OR NEW.picked_up_datetime != OLD.picked_up_datetime OR (OLD.picked_up_datetime IS NOT NULL AND NEW.picked_up_datetime IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'PickUp Date',IFNULL(DATE_FORMAT(old.picked_up_datetime,'%d-%b-%Y'),''),IFNULL(DATE_FORMAT(new.picked_up_datetime,'%d-%b-%Y'),'')));
    END IF;
   IF ((OLD.go_datetime IS NULL AND NEW.go_datetime IS NOT NULL) OR NEW.go_datetime != OLD.go_datetime OR (OLD.go_datetime IS NOT NULL AND NEW.go_datetime IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'G/O Date',IFNULL(DATE_FORMAT(old.go_datetime,'%d-%b-%Y'),''),IFNULL(DATE_FORMAT(new.go_datetime,'%d-%b-%Y'),'')));
    END IF;
   IF ((OLD.fd_eta IS NULL AND NEW.fd_eta IS NOT NULL) OR NEW.fd_eta != OLD.fd_eta OR (OLD.fd_eta IS NOT NULL AND NEW.fd_eta IS NULL)) THEN
       SET eta_at_fd=(SELECT concat_string(eta_at_fd,'ETA at FD',IFNULL(DATE_FORMAT(old.fd_eta,'%d-%b-%Y'),''),IFNULL(DATE_FORMAT(new.fd_eta,'%d-%b-%Y'),'')));
    END IF;
   IF ((OLD.sub_house_bl IS NULL AND NEW.sub_house_bl IS NOT NULL) OR NEW.sub_house_bl != OLD.sub_house_bl OR (OLD.sub_house_bl IS NOT NULL AND NEW.sub_house_bl IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'Sub House BL',IFNULL(old.sub_house_bl,''),IFNULL(new.sub_house_bl,'')));
   END IF;
    IF ((OLD.customs_entry_no IS NULL AND NEW.customs_entry_no IS NOT NULL AND NEW.customs_entry_no !='') OR NEW.customs_entry_no != OLD.customs_entry_no OR (OLD.customs_entry_no IS NOT NULL AND OLD.customs_entry_no !='' AND NEW.customs_entry_no IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'Entry#',IFNULL(old.customs_entry_no,''),IFNULL(new.customs_entry_no,'')));
   END IF;
   IF new.declared_value_estimated!=old.declared_value_estimated THEN
       SET updated_values=(SELECT concat_string(updated_values,'Value Estimated',IF(old.declared_value_estimated=0,'No','Yes'),IF(new.declared_value_estimated=0,'No','Yes')));
   END IF;
   IF new.declared_weight_estimated!=old.declared_weight_estimated THEN
       SET updated_values=(SELECT concat_string(updated_values,'Weight Estimated',IF(old.declared_weight_estimated=0,'No','Yes'),IF(new.declared_weight_estimated=0,'No','Yes')));
   END IF;
   IF ((OLD.inbond_via IS NULL AND NEW.inbond_via IS NOT NULL) OR NEW.inbond_via != OLD.inbond_via OR (OLD.inbond_via IS NOT NULL AND NEW.inbond_via IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'Inbond Via',IFNULL(old.inbond_via,''),IFNULL(new.inbond_via,'')));
   END IF;
   IF ((OLD.it_class IS NULL AND NEW.it_class IS NOT NULL) OR NEW.it_class != OLD.it_class OR (OLD.it_class IS NOT NULL AND NEW.it_class IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'IT Class',IFNULL(old.it_class,''),IFNULL(new.it_class,'')));
   END IF;
   IF ((OLD.customs_entry_class IS NULL AND NEW.customs_entry_class IS NOT NULL) OR NEW.customs_entry_class != OLD.customs_entry_class OR (OLD.customs_entry_class IS NOT NULL AND NEW.customs_entry_class IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'Class of Entry',IFNULL(old.customs_entry_class,''),IFNULL(new.customs_entry_class,'')));
   END IF;
   IF new.elite_ipi_loaded !=old.elite_ipi_loaded THEN
       SET updated_values=(SELECT concat_string(updated_values,'Loaded on IPI Container',IF(old.elite_ipi_loaded=0,'No','Yes'),IF(new.elite_ipi_loaded=0,'No','Yes')));
       END IF;
   IF isNotEqual(OLD.picked_up_by,NEW.picked_up_by) THEN
       SET updated_values=(`concat_string`(updated_values,'Picked By',OLD.picked_up_by,NEW.picked_up_by));
       END IF;
   IF ((OLD.door_delivery_eta IS NULL AND NEW.door_delivery_eta IS NOT NULL) OR NEW.door_delivery_eta != OLD.door_delivery_eta OR (OLD.door_delivery_eta IS NOT NULL AND NEW.door_delivery_eta IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'Door Delivery ETA',IFNULL(DATE_FORMAT(old.door_delivery_eta,'%d-%b-%Y'),''),IFNULL(DATE_FORMAT(new.door_delivery_eta,'%d-%b-%Y'),'')));
    END IF;
    IF new.express_release!=old.express_release THEN
      SET updated_values=(SELECT concat_string(updated_values,'Express Release',IF(old.express_release=0,'No','Yes'),IF(new.express_release=0,'No','Yes')));
    END IF;
    IF new.door_delivery_comment!=old.door_delivery_comment THEN
       SET updated_values=(SELECT concat_string(updated_values,'Door Delivery Comment',IF(old.door_delivery_comment=0,'No','Yes'),IF(new.door_delivery_comment=0,'No','Yes')));
    END IF;
     IF OLD.door_delivery_status != NEW.door_delivery_status THEN
     SET updated_values=(SELECT concat_string(updated_values,'Door Delivery Status',(SELECT codedesc FROM `genericcode_dup` WHERE codetypeid=(SELECT codetypeid FROM codetype WHERE description = 'door delivery status' LIMIT 1) 
     AND CODE =old.door_delivery_status),
     (SELECT codedesc FROM `genericcode_dup` WHERE codetypeid=(SELECT codetypeid FROM codetype WHERE description = 'door delivery status' LIMIT 1) 
     AND CODE =new.door_delivery_status)));
       END IF;
    IF updated_values IS NOT NULL THEN
      SET updated_values=CONCAT(updated_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(OLD.file_number_id,'DR-AutoNotes',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    IF eta_at_fd IS NOT NULL THEN
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(OLD.file_number_id,'DR-AutoNotes',eta_at_fd,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    
  END;
$$

DELIMITER ;


DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBookingPad_insert`$$

CREATE
   
    TRIGGER `after_LclBookingPad_insert` AFTER INSERT ON `lcl_booking_pad` 
    FOR EACH ROW BEGIN
    DECLARE insert_values TEXT;
      IF NEW.pickup_city IS NOT NULL THEN
     SET @bookingType =(SELECT booking_type FROM lcl_booking  WHERE file_number_id=NEW.file_number_id LIMIT 1);
     SET @pickValuess=IF(@bookingType='E','Door Origin/City/Zip','Door Dest/City/Zip');
       SET insert_values = concat_insert_values(insert_values,@pickValuess,new.pickup_city);
    END IF;
    IF NEW.issuing_terminal IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,' Issued By',(SELECT CONCAT(UPPER(login_name),'/',terminal_id) FROM user_details WHERE user_id=NEW.entered_by_user_id));
    END IF;
    IF NEW.pickup_contact_id IS NOT NULL THEN
       SELECT company_name,contact_name,city,address,zip,phone1,fax1,email1
       INTO @compName,@contName,@city,@address,@zip,@phone,@fax1,@email
       FROM lcl_contact WHERE id=NEW.pickup_contact_id;
       IF @city IS NOT NULL THEN
		SET insert_values = concat_insert_values(insert_values,', City',@city);
       END IF;
       IF @compName IS NOT NULL THEN
		SET insert_values = concat_insert_values(insert_values,', Ship To',@compName);
       END IF;
       IF @contName IS NOT NULL THEN
		SET insert_values = concat_insert_values(insert_values,', Contact Name',@contName);
       END IF;
       IF @address IS NOT NULL THEN
		SET insert_values = concat_insert_values(insert_values,', Address',@address);
       END IF;
       IF @zip IS NOT NULL THEN
		SET insert_values = concat_insert_values(insert_values,', Zip',@zip);
       END IF;
       IF @phone IS NOT NULL THEN
		SET insert_values = concat_insert_values(insert_values,', Phone',@phone);
       END IF;
       IF @fax IS NOT NULL THEN
		SET insert_values = concat_insert_values(insert_values,', Fax',@fax);
       END IF;
       IF @email IS NOT NULL THEN
		SET insert_values = concat_insert_values(insert_values,', Email',@email);
       END IF;
    END IF;
    IF NEW.pickup_hours IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,', Receiving Hours',NEW.pickup_hours);
    END IF;
    IF NEW.delivery_ready_date IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,', Ready Date',NEW.delivery_ready_date);
    END IF;
    IF NEW.last_free_date IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,', Last Free Day',NEW.last_free_date);
    END IF;
    IF NEW.scac IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,', Scac Code',NEW.scac);
    END IF;
    IF NEW.pickup_reference_no IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,', PRO#',NEW.pickup_reference_no);
    END IF;
    IF NEW.pickedup_datetime IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,', Actual Pickup Date',NEW.pickedup_datetime);
    END IF;
    IF NEW.delivered_datetime IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,', Actual Delivery Date',NEW.delivered_datetime);
    END IF;
     IF NEW.pickup_est_date IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,', Estimated Pickup Date',NEW.pickup_est_date);
    END IF;
    IF NEW.delivery_est_date IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,', Estimated Delivery Date',NEW.delivery_est_date);
    END IF;
    IF NEW.delivery_commercial IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,', Delivery Commercial',IF(NEW.delivery_commercial=0,'No','Yes'));
    END IF;
    IF NEW.delivery_need_proof IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,', Need POD',IF(NEW.delivery_need_proof=0,'No','Yes'));
    END IF;
     IF NEW.lift_gate IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,', Lift gate',IF(NEW.lift_gate=0,'No','Yes'));
    END IF;
     IF NEW.delivery_notes IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,', Door Delivery Notes',NEW.delivery_notes);
    END IF;
    IF NEW.delivery_contact_id IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,', WhseName',(SELECT company_name FROM lcl_contact WHERE id=NEW.delivery_contact_id));
       SET insert_values = concat_insert_values(insert_values,', WhseAddress',(SELECT address FROM lcl_contact WHERE id=NEW.delivery_contact_id));
       SET insert_values = concat_insert_values(insert_values,', WhseCity/State/Zip',(SELECT CONCAT(city,'/',state,'/',zip) FROM lcl_contact WHERE id=NEW.delivery_contact_id));
       SET insert_values = concat_insert_values(insert_values,', WhsePhone',(SELECT phone1 FROM lcl_contact WHERE id=NEW.delivery_contact_id));
    END IF;
    IF NEW.delivery_instructions IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,', Instructions',NEW.delivery_instructions);
    END IF;
    IF NEW.pickup_instructions IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,', Bill To',NEW.pickup_instructions);
    END IF;
    IF NEW.commodity_desc IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,', Commodity',NEW.commodity_desc);
    END IF;
    IF NEW.terms_of_service IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,', TOS',NEW.terms_of_service);
    END IF;
    IF (insert_values IS NOT NULL) THEN
        SET insert_values=CONCAT('INSERTED ->',insert_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(NEW.file_number_id,'DR-AutoNotes',insert_values,NOW(),NEW.entered_by_user_id,NOW(),NEW.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;

DELIMITER $$


DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBookingPad_update`$$

CREATE
    
    TRIGGER `after_LclBookingPad_update` AFTER UPDATE ON `lcl_booking_pad` 
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT;
     IF (isNotEqual(OLD.pickup_city , NEW.pickup_city)) THEN
     SET @bookingType =(SELECT booking_type FROM lcl_booking  WHERE file_number_id=OLD.file_number_id LIMIT 1);
     SET @pickValuess=IF(@bookingType='E','Door Origin/City/Zip','Door Dest/City/Zip');
       SET updated_values = (SELECT concat_string(updated_values,@pickValuess,OLD.pickup_city,NEW.pickup_city));
    END IF;
    IF (isNotEqual(OLD.issuing_terminal, NEW.issuing_terminal))THEN
       SET updated_values = (SELECT concat_string(updated_values,'Issued By',(SELECT CONCAT(UPPER(login_name),'/',terminal_id) FROM user_details WHERE user_id=OLD.entered_by_user_id),(SELECT CONCAT(UPPER(login_name),'/',terminal_id) FROM user_details WHERE user_id=NEW.entered_by_user_id)));
    END IF;
    IF (isNotEqual(OLD.delivery_ready_date, NEW.delivery_ready_date)) THEN
       SET updated_values = (SELECT concat_string(updated_values,'Ready Date',OLD.delivery_ready_date,NEW.delivery_ready_date));
    END IF;
    IF (isNotEqual(OLD.last_free_date, NEW.last_free_date)) THEN
       SET updated_values = (SELECT concat_string(updated_values,'Last Free Day',OLD.last_free_date,NEW.last_free_date));
    END IF;
    IF (isNotEqual(OLD.scac, NEW.scac)) THEN
       SET updated_values = (SELECT concat_string(updated_values,'Scac Code',OLD.scac,NEW.scac));
    END IF;
     IF (isNotEqual(OLD.pickup_hours, NEW.pickup_hours)) THEN
       SET updated_values = (SELECT concat_string(updated_values,'Receiving Hours',OLD.pickup_hours,NEW.pickup_hours));
    END IF;
    IF (isNotEqual(OLD.pickup_reference_no, NEW.pickup_reference_no)) THEN
       SET updated_values = (SELECT concat_string(updated_values,'PRO#',OLD.pickup_reference_no,NEW.pickup_reference_no));
    END IF;
    IF (isNotEqual(OLD.pickup_instructions ,  NEW.delivery_instructions)) THEN
       SET updated_values = (SELECT concat_string(updated_values,'Instructions',OLD.delivery_instructions,NEW.delivery_instructions));
    END IF;
     IF (isNotEqual(OLD.pickup_instructions ,  NEW.pickup_instructions)) THEN
       SET updated_values = (SELECT concat_string(updated_values,'Bill To',OLD.pickup_instructions,NEW.pickup_instructions));
    END IF;
    IF (isNotEqual(OLD.commodity_desc , NEW.commodity_desc))  THEN
       SET updated_values = (SELECT concat_string(updated_values,'Commodity',OLD.commodity_desc,NEW.commodity_desc));
    END IF;
    IF (isNotEqual(OLD.terms_of_service , NEW.terms_of_service)) THEN
       SET updated_values = (SELECT concat_string(updated_values,'TOS',OLD.terms_of_service,NEW.terms_of_service));
    END IF;
    IF (isNotEqual(OLD.pickedup_datetime, NEW.pickedup_datetime)) THEN
       SET updated_values = (SELECT concat_string(updated_values,'Actual Pickup Date',OLD.pickedup_datetime,NEW.pickedup_datetime));
    END IF;
    IF (isNotEqual(OLD.delivered_datetime, NEW.delivered_datetime)) THEN
       SET updated_values = (SELECT concat_string(updated_values,'Actual Delivery Date',OLD.delivered_datetime,NEW.delivered_datetime));
    END IF;
    IF (isNotEqual(OLD.pickup_est_date, NEW.pickup_est_date)) THEN
       SET updated_values = (SELECT concat_string(updated_values,'Estimated Pickup Date',OLD.pickup_est_date,NEW.pickup_est_date));
    END IF;
    IF (isNotEqual(OLD.delivery_est_date, NEW.delivery_est_date)) THEN
       SET updated_values = (SELECT concat_string(updated_values,'Estimated Delivery Date',OLD.delivery_est_date,NEW.delivery_est_date));
    END IF;
    IF (isNotEqual(OLD.delivery_commercial, NEW.delivery_commercial)) THEN
       SET updated_values = (SELECT concat_string(updated_values,'Delivery Commercial',IF(OLD.delivery_commercial=0,'No','Yes'),IF(NEW.delivery_commercial=0,'No','Yes')));
    END IF;
    IF (isNotEqual(OLD.delivery_need_proof, NEW.delivery_need_proof)) THEN
       SET updated_values = (SELECT concat_string(updated_values,'Need POD',IF(OLD.delivery_need_proof=0,'No','Yes'),IF(NEW.delivery_need_proof=0,'No','Yes')));
    END IF;
    IF (isNotEqual(OLD.lift_gate, NEW.lift_gate)) THEN
       SET updated_values = (SELECT concat_string(updated_values,'Lift gate',IF(OLD.lift_gate=0,'No','Yes'),IF(NEW.lift_gate=0,'No','Yes')));
    END IF;
     IF (isNotEqual(OLD.delivery_notes, NEW.delivery_notes)) THEN
       SET updated_values = (SELECT concat_string(updated_values,'Door Delivery Notes',OLD.delivery_notes,NEW.delivery_notes));
    END IF;
    IF updated_values IS NOT NULL THEN
        SET updated_values=CONCAT('UPDATED ->',updated_values);
	     INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
	     VALUES(OLD.file_number_id,'DR-AutoNotes',updated_values,NOW(),NEW.modified_by_user_id,NOW(),NEW.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;

-- Mantis Issue 10065 (LCL Exports) by Shanmugam on 17th Jul 2016
ALTER TABLE `role_duties`  ADD COLUMN `remove_dr_hold` TINYINT(1) DEFAULT '0' NULL ;
ALTER TABLE `lcl_booking`  ADD COLUMN `hold` ENUM('Y','N') NULL ;

-- Mantis Issue 13469 (LCL Exports) by Aravindhan.V on 19th Jul 2016
ALTER TABLE lcl_correction MODIFY comments TEXT NOT NULL;

--Mantis # 13345 LCL Exports by sathiya 20 th july 2016

DELIMITER $$

DROP FUNCTION IF EXISTS `ScheduleKByUnlocation`$$

CREATE FUNCTION `ScheduleKByUnlocation`(pID INT (11)) RETURNS VARCHAR(255) CHARSET utf8
    READS SQL DATA
    DETERMINISTIC
MAIN : BEGIN
    DECLARE mCode VARCHAR (255) DEFAULT NULL ;
    IF (ISNULL(pID) = FALSE AND pID > 0) THEN 
        SELECT 
          `govschnum`
        INTO 
          mCode 
        FROM
          `ports`
        WHERE 
          `id` = pID 
        LIMIT 1 ;
    END IF ;
    RETURN mCode ;
END MAIN$$

DELIMITER ;

DELIMITER $$

DROP FUNCTION IF EXISTS `UnlocationGetNameScheduleKById`$$

CREATE FUNCTION `UnlocationGetNameScheduleKById`(pID INT (11)) RETURNS VARCHAR(255) CHARSET utf8
    READS SQL DATA
    DETERMINISTIC
MAIN : BEGIN
    DECLARE mValue VARCHAR (255) DEFAULT NULL ;
    IF (ISNULL(pID) = FALSE AND pID > 0) THEN 
        SELECT 
            CONCAT(
                un.`un_loc_name`,
                '/',
                COALESCE(
                    st.`code`,
                    cy.`codedesc`
                ), 
                '(',
                po.`govschnum`,
                ')'
            ) 
        INTO 
            mValue 
        FROM
            `un_location` un
            LEFT JOIN `genericcode_dup` cy
                ON (un.`countrycode` = cy.`id`)
            LEFT JOIN `genericcode_dup` st 
                ON (un.`statecode` = st.`id`)
            LEFT JOIN ports po ON (po.id=un.id)
        WHERE 
            un.`id` = pID 
        LIMIT 1 ;
    END IF ;
    RETURN mValue ;
END MAIN$$

DELIMITER ;

DELIMITER $$

DROP PROCEDURE IF EXISTS `LCLScheduleList`$$

CREATE PROCEDURE `LCLScheduleList`(
  IN pPOO_ID INT (11),
  IN pPOL_ID INT (11),
  IN pPOD_ID INT (11),
  IN pFD_ID INT (11),
  IN pTransMode VARCHAR (50),
  IN pServiceType VARCHAR(1)
)
    READS SQL DATA
MAIN :
BEGIN
  DECLARE mSLS VARCHAR (64) DEFAULT "LCLScheduleList" ;
  DECLARE mSLH VARCHAR (256) DEFAULT "" ;
  DECLARE mSLT VARCHAR (128) DEFAULT UUID() ;
  DECLARE mErrMsg VARCHAR (1024) DEFAULT "" ;
  DECLARE mDEBUG BOOL DEFAULT FALSE ;
  DECLARE mTransMode VARCHAR (50) DEFAULT "" ;
  SET mSLH = CONCAT(": pPOO_ID (",pPOO_ID,"), pPOL_ID (",pPOL_ID,"), pPOD_ID (",pPOD_ID,")") ;
  SET mSLH = CONCAT(mSLH,", pFD_ID (",pFD_ID,"), pTransMode (",pTransMode,"): ") ;
  IF ISNULL(pPOO_ID) OR pPOO_ID < 1 THEN
  SET mErrMsg = CONCAT("VALIDATION",mSLH,"Stopped; pPOO_ID invalid.") ;
  CALL LogSQLEntry (3, mSLS, mSLT, mErrMsg) ;
  SELECT mErrMsg ;
  LEAVE MAIN ;
  END IF ;
  IF ISNULL(pPOL_ID) OR pPOL_ID < 1 THEN
  SET mErrMsg = CONCAT("VALIDATION",mSLH,"Stopped; pPOL_ID invalid.") ;
  CALL LogSQLEntry (3, mSLS, mSLT, mErrMsg) ;
  SELECT mErrMsg ;
  LEAVE MAIN ;
  END IF ;
  IF ISNULL(pPOD_ID) OR pPOD_ID < 1 THEN
   SET mErrMsg = CONCAT("VALIDATION",mSLH,"Stopped; pPOD_ID invalid.") ;
  CALL LogSQLEntry (3, mSLS, mSLT, mErrMsg) ;
  SELECT mErrMsg ;
  LEAVE MAIN ;
  END IF ;
  IF ISNULL(pFD_ID) OR pFD_ID < 1 THEN
   SET mErrMsg = CONCAT("VALIDATION",mSLH,"Stopped; pFD_ID invalid.") ;
  CALL LogSQLEntry (3, mSLS, mSLT, mErrMsg) ;
  SELECT mErrMsg ;
  LEAVE MAIN ;
  END IF ;
  IF ISNULL(pTransMode) OR TRIM(pTransMode) = "" OR pTransMode = "*" THEN
  SET mTransMode = "A,R,T,V" ;
  ELSE
  SET mTransMode = pTransMode ;
  END IF ;
  IF mDEBUG = TRUE
  THEN CALL LogSQLEntry (1,mSLS,mSLT,CONCAT("DEBUG", mSLH, "Begins.")) ;
  END IF ;
  SELECT
    lsh.id AS ssHeaderId,
    lsh.schedule_no AS voyageNo,
    UnLocationGetCodeByID (lsh.origin_id) AS pooUnlocCode,
    UnLocationGetCodeByID (lsh.destination_id) AS fdUnloCode,
    (
			SELECT
				acct_name
			FROM
				trading_partner
			WHERE acct_no = lsd.sp_acct_no
			LIMIT 1
		) AS carrierName,
    lsd.sp_acct_no AS carrierAcctNo,
    lsd.sp_reference_no AS ssVoyage,
    lsd.sp_reference_name AS vesselName,
    UnLocationGetCodeByID (lsd.departure_id) AS polUnlocCode,
    UnLocationGetCodeByID (lsd.arrival_id) AS podUnloCode,
    UnLocationGetNameStateCntryByID (lsd.departure_id) AS departPier,
    lsd.std AS sailingDate,
    lsd.sta AS etaPod,
    lsd.relay_lrd_override AS relaylrdOverride,
    lsd.relay_tt_override AS relayttOverride,
    ScheduleKByUnlocation(lsd.departure_id) AS scheduleK,
    UnlocationGetNameScheduleKById(lsd.departure_id) AS departSched
  FROM
    lcl_ss_header lsh
    JOIN lcl_ss_detail lsd
			ON (
        lsd.ss_header_id = lsh.id
        AND lsd.trans_mode =pTransMode
      )
  WHERE lsh.service_type = pServiceType
    AND lsh.status <> 'V'
    AND
    (
			lsh.origin_id = pPOO_ID
			OR lsh.origin_id = pPOL_ID
		)
    AND
			(
				lsh.destination_id = pPOD_ID
				OR lsh.destination_id = pFD_ID
			)
    AND lsd.std > NOW()
    ORDER BY lsd.std ASC;
    IF mDEBUG = TRUE THEN
      CALL LogSQLEntry (1,mSLS,mSLT,"DEBUG: Ends normally.") ;
    END IF ;
END MAIN$$

DELIMITER ;

DELIMITER $$

DROP PROCEDURE IF EXISTS `BL_LCLScheduleListUpComing`$$

CREATE PROCEDURE `BL_LCLScheduleListUpComing`(
	IN pPOO_ID INT(11),
	IN pPOL_ID INT(11),
	IN pPOD_ID INT(11),
	IN pFD_ID INT(11),
	IN pTransMode VARCHAR(50),
	IN pServiceType VARCHAR(1)
)
    READS SQL DATA
MAIN: BEGIN
	DECLARE mSLS VARCHAR(64) DEFAULT "BLLCLScheduleListUpComing";
	DECLARE mSLH VARCHAR(256) DEFAULT "";
	DECLARE mSLT VARCHAR(128) DEFAULT UUID();
	DECLARE mErrMsg VARCHAR(1024) DEFAULT "";
	DECLARE mDEBUG BOOL DEFAULT FALSE;
	DECLARE mTransMode VARCHAR(50) DEFAULT "";
	SET mSLH = CONCAT( ": pPOO_ID (", pPOO_ID, "), pPOL_ID (", pPOL_ID, "), pPOD_ID (", pPOD_ID, ")" );
	SET mSLH = CONCAT( mSLH, ", pFD_ID (", pFD_ID, "), pTransMode (", pTransMode, "): " );
	IF ISNULL( pPOO_ID ) OR pPOO_ID < 1 THEN
		SET mErrMsg =  CONCAT( "VALIDATION", mSLH, "Stopped; pPOO_ID invalid." ) ;
		CALL LogSQLEntry( 3, mSLS, mSLT, mErrMsg );
		SELECT mErrMsg;
		LEAVE MAIN;
	END IF;
	IF ISNULL( pPOL_ID ) OR pPOL_ID < 1 THEN
		SET mErrMsg =  CONCAT( "VALIDATION", mSLH, "Stopped; pPOL_ID invalid." ) ;
		CALL LogSQLEntry( 3, mSLS, mSLT, mErrMsg );
		SELECT mErrMsg;
		LEAVE MAIN;
	END IF;
	IF ISNULL( pPOD_ID ) OR pPOD_ID < 1 THEN
		SET mErrMsg =  CONCAT( "VALIDATION", mSLH, "Stopped; pPOD_ID invalid." ) ;
		CALL LogSQLEntry( 3, mSLS, mSLT, mErrMsg );
		SELECT mErrMsg;
		LEAVE MAIN;
	END IF;
	IF ISNULL( pFD_ID ) OR pFD_ID < 1 THEN
		SET mErrMsg =  CONCAT( "VALIDATION", mSLH, "Stopped; pFD_ID invalid." ) ;
		CALL LogSQLEntry( 3, mSLS, mSLT, mErrMsg );
		SELECT mErrMsg;
		LEAVE MAIN;
	END IF;
	IF ISNULL( pTransMode ) OR TRIM( pTransMode ) = "" OR pTransMode = "*" THEN
		SET mTransMode = "A,R,T,V";
	ELSE
		SET mTransMode = pTransMode;
	END IF;
	IF mDEBUG = TRUE THEN
		CALL LogSQLEntry( 1, mSLS, mSLT, CONCAT( "DEBUG", mSLH, "Begins." ) );
	END IF;
	SELECT
    lsh.id AS ssHeaderId,
    lsh.schedule_no AS voyageNo,
    UnLocationGetCodeByID (lsh.origin_id) AS pooUnlocCode,
    UnLocationGetCodeByID (lsh.destination_id) AS fdUnloCode,
    (SELECT
      acct_name
    FROM
      trading_partner
    WHERE acct_no = lsd.sp_acct_no
    LIMIT 1) AS carrierName,
    lsd.sp_acct_no AS carrierAcctNo,
    lsd.sp_reference_no AS ssVoyage,
    lsd.sp_reference_name AS vesselName,
    UnLocationGetCodeByID (lsd.departure_id) AS polUnlocCode,
    UnLocationGetCodeByID (lsd.arrival_id) AS podUnloCode,
    UnLocationGetNameStateCntryByID (lsd.departure_id) AS departPier,
    lsd.std AS sailingDate,
    lsd.sta AS etaPod,
    lsd.relay_lrd_override AS relaylrdOverride,
    lsd.relay_tt_override AS relayttOverride,
    ScheduleKByUnlocation(lsd.departure_id) AS scheduleK,
    UnlocationGetNameScheduleKById(lsd.departure_id) AS departSched
  FROM
    lcl_ss_header lsh
    JOIN lcl_ss_detail lsd
      ON (
        lsd.ss_header_id = lsh.id
        AND lsd.trans_mode =pTransMode
      )
     WHERE lsh.service_type = pServiceType
      AND lsh.status <> 'V'
   AND
    (
			lsh.origin_id = pPOO_ID
			OR lsh.origin_id = pPOL_ID
		)
    AND
			(
				lsh.destination_id = pPOD_ID
				OR lsh.destination_id = pFD_ID
			)
    AND lsd.std  >=DATE_SUB(NOW(),INTERVAL 60 DAY)
    ORDER BY lsd.std DESC;
    IF mDEBUG = TRUE THEN
      CALL LogSQLEntry( 1, mSLS, mSLT, "DEBUG: Ends normally." );
    END IF;
END MAIN$$

DELIMITER ;

-- Mantis Issue 11229 (Accounting) by Nambu on 21st Jul 2016

UPDATE TRANSACTION t SET t.drcpt=SUBSTR(t.drcpt,3) WHERE t.drcpt LIKE '04%' AND LENGTH(t.`drcpt`) = 8; 
UPDATE TRANSACTION t SET t.drcpt=REPLACE(t.`drcpt`, ' ', '') WHERE t.drcpt LIKE '04%' AND LENGTH(t.`drcpt`) > 8;
UPDATE TRANSACTION t SET t.drcpt=NULL WHERE t.drcpt LIKE '04%' AND  t.invoice_number = 'PRE PAYMENT' AND LENGTH(t.`drcpt`) > 8;
UPDATE TRANSACTION t SET t.drcpt=SUBSTRING(t.drcpt,3,6) WHERE t.drcpt LIKE '04%'   AND LENGTH(t.`drcpt`) >= 8;
UPDATE transaction_ledger t SET t.drcpt=SUBSTR(t.drcpt,4) WHERE t.drcpt LIKE '04-%';
UPDATE transaction_ledger t SET t.drcpt=SUBSTRING_INDEX(t.drcpt,'-','1') WHERE t.`drcpt` LIKE '%-R' AND t.drcpt LIKE '04%';
UPDATE transaction_ledger t SET t.drcpt=SUBSTR(t.drcpt,3) WHERE t.drcpt LIKE '04%' AND LENGTH(t.`drcpt`) = 8; 
UPDATE transaction_ledger t SET t.drcpt=REPLACE(t.`drcpt`, ' ', '') WHERE t.drcpt LIKE '04%' AND LENGTH(t.`drcpt`) > 8;
UPDATE transaction_ledger t SET t.drcpt=NULL WHERE t.drcpt LIKE '04%' AND  t.invoice_number = 'PRE PAYMENT' AND LENGTH(t.`drcpt`) > 8;
UPDATE transaction_ledger t SET t.drcpt=SUBSTRING(t.drcpt,3,6) WHERE t.drcpt LIKE '04%'  AND LENGTH(t.`drcpt`) >= 8;
 
 
-- Mantis item# 7089(Logiware FCL-Exports) by Stefy on 25th Jul 2016 

INSERT INTO job (`name`, `frequency`, `day1`, `day2`, `hour`, `minute`, `enabled`, `updated_by`, `updated_date`, `start_time`, `end_time`, `class_name`) VALUES
('997 acknowledgement reminder','DAILY','0','0','23','30','0','STEFY','2016-07-12 16:23:30','2016-07-12 16:23:30','2016-07-12 14:23:30','com.logiware.common.job.Edi997AcknowledgementRemainderJob');

-- LCL Exports Mantis Item 13341 By Aravindhan.V on 25th July 2016
DELIMITER $$

DROP FUNCTION IF EXISTS `DeriveLCLExportGlAccount`$$

CREATE FUNCTION `DeriveLCLExportGlAccount`(
  pId INT(11),
  pBillingTerimal VARCHAR(5),
  pOriginId INT(11)
) RETURNS VARCHAR(20) CHARSET latin1
    READS SQL DATA
    DETERMINISTIC
MAIN : BEGIN
  DECLARE mAccount VARCHAR (20) DEFAULT NULL ;  
  
  IF (pId <> 0 AND (pBillingTerimal <> '' OR pOriginId <> 0)) THEN 
   
    SELECT 
      CONCAT_WS('-', 
        SystemRulesGetRuleName('CompanyCode'),gl.gl_acct,
        LPAD(IF(gl.`derive_yn` = 'Y', 
        `TerminalGLMapingGetTerminal`(IF(gl.`suffix_value` IN ('B', 'D') AND pBillingTerimal<>'', pBillingTerimal, `OriginGetTerminal`(pOriginId)), gl.`shipment_type`, gl.`suffix_value`), gl.`suffix_value`), 2, '0')
      )
    INTO
      mAccount
    FROM
      `gl_mapping` gl
    WHERE 
      gl.`id` = pId;
  END IF ;
  RETURN mAccount ;
END MAIN$$

DELIMITER ;

DELIMITER $$

DROP FUNCTION IF EXISTS `OriginGetTerminal`$$

CREATE  FUNCTION `OriginGetTerminal`(
  pOriginId INT(11)
) RETURNS VARCHAR(5) CHARSET latin1
    READS SQL DATA
    DETERMINISTIC
MAIN: BEGIN
    DECLARE mTerminal VARCHAR(5) DEFAULT NULL;
     IF pOriginId <> 0 THEN
      SELECT
        origin_terminal
      INTO 
        mTerminal 
      FROM 
        `un_location`
      WHERE 
        `id` = pOriginId;
     END IF;
    RETURN mTerminal;
END MAIN$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `lcl_booking_ac_insert_trigger`$$

CREATE
    TRIGGER `lcl_booking_ac_insert_trigger` AFTER INSERT ON `lcl_booking_ac` 
    FOR EACH ROW BEGIN
  DECLARE insert_values TEXT DEFAULT '';
  DECLARE changesFlag BOOLEAN DEFAULT FALSE;
  DECLARE mFileNumberId BIGINT(20);
  DECLARE mShipmentType VARCHAR(4);
  DECLARE mBlNo VARCHAR(30);
  DECLARE mDrNo VARCHAR(7);
  DECLARE mBookingNo VARCHAR(20);
  DECLARE mVoyageNo VARCHAR(30);
  DECLARE mContainerNo VARCHAR(30);
  DECLARE mEta DATETIME;
  DECLARE mSsline VARCHAR(50);
  DECLARE mOrigin VARCHAR(100);
  DECLARE mDestination VARCHAR(100);
  DECLARE mShipperName VARCHAR(50);
  DECLARE mShipperNo VARCHAR(10);
  DECLARE mConsigneeName VARCHAR(50);
  DECLARE mConsigneeNo VARCHAR(10);
  DECLARE mForwarderName VARCHAR(50);
  DECLARE mForwarderNo VARCHAR(10);
  DECLARE mThirdPartyName VARCHAR(50);
  DECLARE mThirdPartyNo VARCHAR(10);
  DECLARE mAgentName VARCHAR(50);
  DECLARE mAgentNo VARCHAR(10);
  DECLARE mTerminal VARCHAR(5);
  DECLARE mVesselNo VARCHAR(20);
  DECLARE mMasterBl VARCHAR(50);
  DECLARE mSubhouseBl VARCHAR(50);
  DECLARE mApGlMappingId BIGINT(20);
  DECLARE mCostAmount DECIMAL(10,2);
  DECLARE mVendorName VARCHAR(50);
  DECLARE mVendorNo VARCHAR(10);
  DECLARE mInvoiceNo VARCHAR(50);
  DECLARE mCostId BIGINT(20);
  DECLARE mBsCostCode VARCHAR(5);
  DECLARE mCostCode VARCHAR(20);
  DECLARE mGlAccount VARCHAR(20);
  DECLARE mUserId INT(11);
  DECLARE mAccrualStatus VARCHAR(10);
  DECLARE mAccrualNotFound BOOLEAN DEFAULT FALSE;
  DECLARE mBkPieceUnitId BIGINT(20);
  DECLARE mOriginId INT(11);
  DECLARE mExpBookingTerminal VARCHAR(5);

  IF (new.ar_gl_mapping_id <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Code', (SELECT charge_code FROM gl_mapping WHERE id = new.ar_gl_mapping_id)) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.ar_amount <> 0.00) THEN
    SET insert_values = concat_insert_values (insert_values, ' Charge Amount', new.ar_amount) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.ap_amount <> 0.00) THEN
    SET insert_values = concat_insert_values (insert_values, ' Cost Amount', new.ap_amount) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.rate_per_weight_unit <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Rate Weight', new.rate_per_weight_unit) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.rate_per_volume_unit <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Rate Measure', new.rate_per_volume_unit) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.rate_flat_minimum <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Rate Minimum', new.rate_flat_minimum) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.cost_weight <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Cost Weight', new.cost_weight) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.cost_measure <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Cost Measure', new.cost_measure) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.cost_minimum <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Cost Minimum', new.cost_minimum) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.sp_acct_no <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Vendor Name', (SELECT acct_name FROM trading_partner WHERE acct_no = new.sp_acct_no)) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.sp_acct_no <> '') THEN
     SET insert_values = concat_insert_values (insert_values, ' Vendor Number', new.sp_acct_no) ;
     SET changesFlag = TRUE ;
  END IF ;
  IF (new.invoice_number <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Invoice Number', new.invoice_number) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (insert_values <> '') THEN
    IF (changesFlag = TRUE) THEN
      CALL update_lclbooking_moddate (new.file_number_id) ;
    END IF ;
    SET insert_values = CONCAT('INSERTED ->', insert_values) ;
    IF (new.converted) THEN
      SET insert_values = CONCAT(insert_values, 'Converted from Eculine EDI') ;
    END IF ;
    INSERT INTO lcl_remarks (
      file_number_id,
      TYPE,
      remarks,
      entered_datetime,
      entered_by_user_id,
      modified_datetime,
      modified_by_user_id
    )
    VALUES (
      new.file_number_id,
      'DR-AutoNotes',
      insert_values,
      NOW(),
      new.entered_by_user_id,
      NOW(),
      new.modified_by_user_id
    ) ;
  END IF ;
  IF (new.ap_amount <> 0.00 AND new.`sp_acct_no` <> '' AND new.ap_gl_mapping_id IS NOT NULL AND new.`file_number_id` IS NOT NULL) THEN
  SELECT
     lbpu.id
  INTO mBkPieceUnitId
  FROM
     `lcl_booking_piece` lbp
      JOIN `lcl_booking_piece_unit` lbpu
        ON (lbp.`id` = lbpu.`booking_piece_id`)
       JOIN `lcl_unit_ss` lus
        ON (lbpu.`lcl_unit_ss_id` = lus.id)
       JOIN `lcl_ss_header` lssh
        ON (lus.`ss_header_id` = lssh.`id`)
        WHERE lbp.`file_number_id` = new.`file_number_id` AND  lssh.service_type <> 'N'
        GROUP BY lbp.`file_number_id`;
    SELECT
      fn.`id`,
      IF(bk.`booking_type` = 'E', 'LCLE', 'LCLI') AS shipmentType,
      IF(bk.`booking_type` = 'E', CONCAT_WS('-',
       CONVERT((SELECT IF(t.unlocationcode1 <> '',RIGHT(t.unlocationcode1,3),t.trmnum) FROM terminal t WHERE t.trmnum=bk.billing_terminal), CHARACTER CHARSET utf8),
       CONVERT((SELECT IF(u.bl_numbering = 'Y', RIGHT(`UnLocationGetCodeByID`(bk.`fd_id`),3),
       `UnLocationGetCodeByID` (bk.`fd_id`)) FROM un_location u WHERE u.id=bk.`fd_id`), CHARACTER CHARSET utf8),
        CONVERT(fn.`file_number`, CHARACTER CHARSET utf8)), CONCAT('IMP-', fn.`file_number`)) AS blNo,
      fn.`file_number` AS drNo,
      us.`sp_booking_no` AS bookingNo,
      CONCAT_WS('-', CONVERT(ssh.`billing_trmnum`, CHARACTER CHARSET utf8), `UnLocationGetCodeByID` (ssh.`origin_id`), `UnLocationGetCodeByID` (ssh.`destination_id`), ssh.`schedule_no`) AS voyageNo,
      (SELECT u.`unit_no` FROM `lcl_unit` u WHERE u.`id` = us.`unit_id` LIMIT 1) AS containerNo,
       IF(bk.`booking_type` = 'E',ssd.std,ssd.`sta`) AS eta,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = ssd.`sp_acct_no` LIMIT 1) AS ssline,
      `UnLocationGetNameStateCntryByID` (ssh.`origin_id`) AS origin,
      `UnLocationGetNameStateCntryByID` (ssh.`destination_id`) AS destination,
      bk.`ship_acct_no` AS shipperNo,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = bk.`ship_acct_no` LIMIT 1) AS shipperName,
      bk.`cons_acct_no` AS consigneeNo,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = bk.`cons_acct_no` LIMIT 1) AS consigneeName,
      bk.`fwd_acct_no` AS forwarderNo,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = bk.`fwd_acct_no` LIMIT 1) AS forwarderName,
      bk.`third_party_acct_no` AS thirdPartyNo,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = bk.`third_party_acct_no` LIMIT 1) AS thirdPartyName,
      bk.`agent_acct_no` AS agentNo,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = bk.`agent_acct_no` LIMIT 1) AS agentName,
      COALESCE(ssh.`billing_trmnum`, bk.`billing_terminal`) AS terminal,
      (SELECT `code` FROM `genericcode_dup` WHERE `codedesc` = ssd.`sp_reference_name` AND `codetypeid` = (SELECT `codetypeid` FROM `codetype` WHERE `description` = 'Vessel Codes' LIMIT 1) LIMIT 1) AS vesselNo,
      (SELECT `masterbl` FROM `lcl_unit_ss_manifest` WHERE `ss_header_id` = ssh.`id` AND `unit_id` = us.`unit_id` LIMIT 1) AS masterBl,
      (SELECT `sub_house_bl` FROM `lcl_booking_import` WHERE `file_number_id` = fn.`id` LIMIT 1) AS subhouseBl,
      new.`ap_gl_mapping_id` AS apGlMappingId,
      new.`ap_amount` AS costAmount,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = new.`sp_acct_no` LIMIT 1) AS vendorName,
      new.`sp_acct_no` AS vendorNo,
      new.`invoice_number` AS invoiceNo,
      new.`id` AS costId,
      new.`modified_by_user_id` AS userId,
      bk.poo_id AS mOriginId,
      bk.`billing_terminal` AS mExpBookingTerminal
    INTO
      mFileNumberId,
      mShipmentType,
      mBlNo,
      mDrNo,
      mBookingNo,
      mVoyageNo,
      mContainerNo,
      mEta,
      mSsline,
      mOrigin,
      mDestination,
      mShipperNo,
      mShipperName,
      mConsigneeNo,
      mConsigneeName,
      mForwarderNo,
      mForwarderName,
      mThirdPartyNo,
      mThirdPartyName,
      mAgentNo,
      mAgentName,
      mTerminal,
      mVesselNo,
      mMasterBl,
      mSubhouseBl,
      mApGlMappingId,
      mCostAmount,
      mVendorName,
      mVendorNo,
      mInvoiceNo,
      mCostId,
      mUserId,
      mOriginId,
      mExpBookingTerminal
    FROM
      `lcl_file_number` fn
      JOIN `lcl_booking` bk
        ON (fn.`id` = bk.`file_number_id`)
      JOIN `lcl_booking_piece` bp
        ON (fn.`id` = bp.`file_number_id`)
      LEFT JOIN `lcl_booking_piece_unit` bpu
        ON (bp.`id` = bpu.`booking_piece_id` AND bpu.id=mBkPieceUnitId)
      LEFT JOIN `lcl_unit_ss` us
        ON (bpu.`lcl_unit_ss_id` = us.id)
      LEFT JOIN `lcl_ss_header` ssh
        ON (us.`ss_header_id` = ssh.`id`)
      LEFT JOIN `lcl_ss_detail` ssd
        ON (ssh.`id` = ssd.`ss_header_id`)
    WHERE fn.`id` = new.`file_number_id`
    GROUP BY fn.`id` ;
        SELECT
      IF(COUNT(transaction_id)>0, FALSE, TRUE),
      `status`
    INTO
      mAccrualNotFound,
      mAccrualStatus
    FROM
      `transaction_ledger`
    WHERE `cost_id` = mCostId
      AND `transaction_type` = 'AC'
      AND `drcpt` = mDrNo;
    SELECT
      gl.`bluescreen_chargecode`,
      gl.`charge_code`,
       IF(mShipmentType = 'LCLE',
      `DeriveLCLExportGlAccount`(gl.id,mExpBookingTerminal,mOriginId),
      `DeriveGlAccount`(gl.charge_Code,mShipmentType,gl.transaction_type,mTerminal)
      ) AS gl_account
    INTO
      mBsCostCode,
      mCostCode,
      mGlAccount
    FROM
      `gl_mapping` gl
    WHERE gl.`id` = mApGlMappingId ;
    IF (mAccrualNotFound) THEN
      INSERT INTO `lcl_booking_ac_trans` (
        `file_number_id`,
        `trans_type`,
        `trans_datetime`,
        `entry_type`,
        `reference_no`,
        `amount`,
        `entered_by_user_id`,
        `entered_datetime`,
        `modified_by_user_id`,
        `modified_datetime`
      )
      VALUES (
        mFileNumberId,
        'AC',
        NOW(),
        'A',
        '',
        mCostAmount,
        mUserId,
        NOW(),
        mUserId,
        NOW()
      );
      INSERT INTO `lcl_booking_ac_ta` (
        `lcl_booking_ac_trans_id`,
        `lcl_booking_ac_id`,
        `amount`,
        `entered_by_user_id`,
        `entered_datetime`,
        `modified_by_user_id`,
        `modified_datetime`
      )
      VALUES (
        (SELECT LAST_INSERT_ID()),
        mCostId,
        0.00,
        mUserId,
        NOW(),
        mUserId,
        NOW()
      );
      INSERT INTO `transaction_ledger` (
	  `cost_id`,
	  `cust_name`,
	  `cust_no`,
	  `invoice_number`,
	  `drcpt`,
	  `bill_ladding_no`,
	  `transaction_type`,
	  `status`,
	  `subledger_source_code`,
	  `shipment_type`,
	  `bluescreen_chargecode`,
	  `charge_code`,
	  `gl_account_number`,
	  `transaction_amt`,
	  `balance`,
	  `balance_in_process`,
	  `transaction_date`,
	  `sailing_date`,
	  `booking_no`,
	  `voyage_no`,
	  `container_no`,
	  `master_bl`,
	  `sub_house_bl`,
	  `originating_terminal`,
	  `destination`,
	  `ship_name`,
	  `ship_no`,
	  `cons_name`,
	  `cons_no`,
	  `fwd_name`,
	  `fwd_no`,
	  `third_pty_name`,
	  `third_pty_no`,
	  `agent_name`,
	  `agent_no`,
	  `billing_terminal`,
	  `terminal`,
	  `vessel_no`,
	  `bill_to`,
	  `ready_to_post`,
	  `currency_code`,
	  `manifest_flag`,
	  `created_on`,
	  `created_by`
      )
      VALUES (
	  mCostId,
	  mVendorName,
	  mVendorNo,
	  mInvoiceNo,
	  mDrNo,
	  mBlNo,
	  'AC',
	  'Open',
	  'ACC',
	  mShipmentType,
	  mBsCostCode,
	  mCostCode,
	  mGlAccount,
	  mCostAmount,
	  mCostAmount,
	  mCostAmount,
	  COALESCE(mEta, NOW()),
	  mEta,
	  mBookingNo,
	  mVoyageNo,
	  mContainerNo,
	  mMasterBl,
	  mSubhouseBl,
	  mOrigin,
	  mDestination,
	  mShipperName,
	  mShipperNo,
	  mConsigneeName,
	  mConsigneeNo,
	  mForwarderName,
	  mForwarderNo,
	  mThirdPartyName,
	  mThirdPartyNo,
	  mAgentName,
	  mAgentNo,
	  mTerminal,
	  mTerminal,
	  mVesselNo,
	  'Y',
	  'ON',
	  'USD',
	  'N',
	  NOW(),
	  mUserId
      );
    END IF;
  END IF;
END;
$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBookingAc_update`$$

CREATE
    TRIGGER `after_LclBookingAc_update` AFTER UPDATE ON `lcl_booking_ac` 
    FOR EACH ROW BEGIN
  DECLARE updatedValues TEXT DEFAULT '';
  DECLARE chargeValue BOOLEAN DEFAULT FALSE;
  DECLARE chageFlag BOOLEAN DEFAULT FALSE;
  DECLARE mFileNumberId BIGINT(20);
  DECLARE mShipmentType VARCHAR(4);
  DECLARE mBlNo VARCHAR(30);
  DECLARE mDrNo VARCHAR(7);
  DECLARE mBookingNo VARCHAR(20);
  DECLARE mVoyageNo VARCHAR(30);
  DECLARE mContainerNo VARCHAR(30);
  DECLARE mEta DATETIME;
  DECLARE mSsline VARCHAR(50);
  DECLARE mOrigin VARCHAR(100);
  DECLARE mDestination VARCHAR(100);
  DECLARE mShipperName VARCHAR(50);
  DECLARE mShipperNo VARCHAR(10);
  DECLARE mConsigneeName VARCHAR(50);
  DECLARE mConsigneeNo VARCHAR(10);
  DECLARE mForwarderName VARCHAR(50);
  DECLARE mForwarderNo VARCHAR(10);
  DECLARE mThirdPartyName VARCHAR(50);
  DECLARE mThirdPartyNo VARCHAR(10);
  DECLARE mAgentName VARCHAR(50);
  DECLARE mAgentNo VARCHAR(10);
  DECLARE mTerminal VARCHAR(5);
  DECLARE mVesselNo VARCHAR(20);
  DECLARE mMasterBl VARCHAR(50);
  DECLARE mSubhouseBl VARCHAR(50);
  DECLARE mApGlMappingId BIGINT(20);
  DECLARE mCostAmount DECIMAL(10,2);
  DECLARE mVendorName VARCHAR(50);
  DECLARE mVendorNo VARCHAR(10);
  DECLARE mInvoiceNo VARCHAR(50);
  DECLARE mCostId BIGINT(20);
  DECLARE mBsCostCode VARCHAR(5);
  DECLARE mCostCode VARCHAR(20);
  DECLARE mGlAccount VARCHAR(20);
  DECLARE mUserId INT(11);
  DECLARE mAccrualStatus VARCHAR(10);
  DECLARE mAccrualNotFound BOOLEAN DEFAULT FALSE;
  DECLARE mBookingType VARCHAR(2);
  DECLARE mBkPieceUnitId BIGINT(20);
  DECLARE mOriginId INT(11);
  DECLARE mExpBookingTerminal VARCHAR(5);

  IF (new.ar_gl_mapping_id <> old.ar_gl_mapping_id) THEN
    SET updatedValues = concat_string(updatedValues, 'Charge Code', (SELECT charge_code FROM gl_mapping WHERE id = old.ar_gl_mapping_id), (SELECT charge_code FROM gl_mapping WHERE id = new.ar_gl_mapping_id)) ;
    SET chargeValue = TRUE ;
    SET chageFlag = TRUE ;
  END IF ;
  IF (new.ar_amount <> old.ar_amount) THEN
    SET updatedValues = concat_string(updatedValues, 'Charge Amount', old.ar_amount, new.ar_amount) ;
    SET chageFlag = TRUE ;
  END IF ;
  IF (new.ap_amount <> old.ap_amount) THEN
    SET updatedValues = concat_string(updatedValues, 'Cost Amount', old.ap_amount, new.ap_amount) ;
    SET chageFlag = TRUE ;
  END IF ;
   /* ************************Only For Lcl Exports when change the FLAT RATE****************************** */
  SET mBookingType = (SELECT booking_type FROM lcl_booking WHERE file_number_id = new.file_number_id);
  IF (mBookingType NOT IN('I','T')) THEN
     IF (new.rate_per_unit  <> old.rate_per_unit) THEN
         SET updatedValues = concat_string(updatedValues, 'Charge Flat Amount', old.rate_per_unit, new.rate_per_unit) ;
         SET chageFlag = TRUE ;
     END IF;
     IF (new.cost_flatrate_amount <> old.cost_flatrate_amount) THEN
         SET updatedValues = concat_string(updatedValues, 'Cost Flat Amount', old.cost_flatrate_amount, new.cost_flatrate_amount) ;
         SET chageFlag = TRUE ;
     END IF;
  END IF ;
   /* ************************************************************************************ */
  IF (new.adjustment_amount <> old.adjustment_amount) THEN
    SET updatedValues = concat_string(updatedValues, 'Adjustment Amount', old.adjustment_amount, new.adjustment_amount) ;
    SET chageFlag = TRUE ;
  END IF ;
  IF (new.adjustment_comments <> old.adjustment_comments) THEN
    SET updatedValues = concat_string(updatedValues, 'Adjustment Comments', old.adjustment_comments, new.adjustment_comments) ;
    SET chageFlag = TRUE ;
  END IF ;
  IF (new.rate_uom <> old.rate_uom) THEN
    SET updatedValues = concat_string(updatedValues, 'Rate UOM', old.rate_uom, new.rate_uom) ;
    SET chageFlag = TRUE ;
  END IF ;
  IF (new.rate_per_weight_unit <> old.rate_per_weight_unit) THEN
    SET updatedValues = concat_string(updatedValues, 'Weight', old.rate_per_weight_unit, new.rate_per_weight_unit) ;
    SET chageFlag = TRUE ;
  END IF ;
  IF (new.rate_per_volume_unit <> old.rate_per_volume_unit) THEN
    SET updatedValues = concat_string (updatedValues, 'Volume', old.rate_per_volume_unit, new.rate_per_volume_unit) ;
    SET chageFlag = TRUE ;
  END IF ;
  IF (new.rate_flat_minimum <> old.rate_flat_minimum) THEN
    SET updatedValues = concat_string(updatedValues, 'Minimum', old.rate_flat_minimum, new.rate_flat_minimum) ;
    SET chageFlag = TRUE ;
  END IF ;
  /* ************************Wnen Change the W/M Rate Cost in LCL Exports****************************** */
  IF (new.cost_weight <> old.cost_weight) THEN
    SET updatedValues = concat_string(updatedValues, 'Cost Weight', old.cost_weight, new.cost_weight) ;
    SET chageFlag = TRUE ;
  END IF ;
  IF (new.cost_measure <> old.cost_measure) THEN
    SET updatedValues = concat_string (updatedValues, 'Cost Volume', old.cost_measure, new.cost_measure) ;
    SET chageFlag = TRUE ;
  END IF ;
  IF (new.cost_minimum <> old.cost_minimum) THEN
    SET updatedValues = concat_string(updatedValues, 'Cost Minimum', old.cost_minimum, new.cost_minimum) ;
    SET chageFlag = TRUE ;
  END IF ;
  /* *************************************************************************************************** */
  IF (new.sp_acct_no <> old.sp_acct_no) THEN
    SET updatedValues = concat_string(updatedValues, 'Vendor Name', (SELECT acct_name FROM trading_partner WHERE acct_no = old.sp_acct_no), (SELECT acct_name FROM trading_partner WHERE acct_no = new.sp_acct_no));
    SET updatedValues = concat_string(updatedValues, 'Vendor Number', old.sp_acct_no, new.sp_acct_no);
    SET chageFlag = TRUE ;
  END IF ;
  IF (new.invoice_number <> old.invoice_number) THEN
    SET updatedValues = concat_string(updatedValues, 'Invoice Number', old.invoice_number, new.invoice_number) ;
  ELSEIF (new.invoice_number IS NOT NULL AND new.invoice_number <> '' AND old.invoice_number IS NULL) THEN
    SET updatedValues = concat_string(updatedValues, 'Invoice Number', '', new.invoice_number) ;
    SET chageFlag = TRUE ;
  END IF ;
  IF (updatedValues <> '') THEN
    IF (chageFlag = TRUE) THEN
      CALL update_lclbooking_moddate (OLD.file_number_id) ;
    END IF ;
    IF (chargeValue = TRUE) THEN
      SET updatedValues = CONCAT('UPDATED ->', updatedValues) ;
    ELSE
      SET updatedValues = CONCAT('UPDATED -> (Code -> ', (SELECT charge_code FROM gl_mapping WHERE id = old.ar_gl_mapping_id), ')', updatedValues) ;
    END IF ;
    INSERT INTO lcl_remarks (
      file_number_id,
      TYPE,
      remarks,
      entered_datetime,
      entered_by_user_id,
      modified_datetime,
      modified_by_user_id
    )
    VALUES (
      old.file_number_id,
      'DR-AutoNotes',
      updatedValues,
      NOW(),
      new.modified_by_user_id,
      NOW(),
      new.modified_by_user_id
    ) ;
  END IF ;
  IF (new.ap_amount <> 0.00 AND new.`file_number_id` IS NOT NULL AND (isNotEqual(new.ap_amount, old.ap_amount) OR isNotEqual(new.`sp_acct_no`, old.`sp_acct_no`) OR isNotEqual(new.`invoice_number`, old.`invoice_number`)) AND new.ap_gl_mapping_id IS NOT NULL AND new.deleted = FALSE) THEN
    SELECT
     lbpu.id
  INTO mBkPieceUnitId
  FROM
     `lcl_booking_piece` lbp
      JOIN `lcl_booking_piece_unit` lbpu
        ON (lbp.`id` = lbpu.`booking_piece_id`)
       JOIN `lcl_unit_ss` lus
        ON (lbpu.`lcl_unit_ss_id` = lus.id)
       JOIN `lcl_ss_header` lssh
        ON (lus.`ss_header_id` = lssh.`id`)
        WHERE lbp.`file_number_id` = new.`file_number_id` AND  lssh.service_type <> 'N'
        GROUP BY lbp.`file_number_id`;
    SELECT
      fn.`id`,
      IF(bk.`booking_type` = 'E', 'LCLE', 'LCLI') AS shipmentType,
      IF(bk.`booking_type` = 'E', CONCAT_WS('-',
      CONVERT((SELECT IF(t.unlocationcode1 <> '',
      RIGHT(t.unlocationcode1,3),t.trmnum) FROM terminal t WHERE t.trmnum=bk.billing_terminal),
       CHARACTER CHARSET utf8), CONVERT(
       (SELECT IF(u.bl_numbering = 'Y', RIGHT(`UnLocationGetCodeByID`(bk.`fd_id`),3),
       `UnLocationGetCodeByID` (bk.`fd_id`)) FROM un_location u WHERE u.id=bk.`fd_id`),
       CHARACTER CHARSET utf8), CONVERT(fn.`file_number`, CHARACTER CHARSET utf8)),
       CONCAT('IMP-', fn.`file_number`)) AS blNo,
      fn.`file_number` AS drNo,
      us.`sp_booking_no` AS bookingNo,
      CONCAT_WS('-', CONVERT(ssh.`billing_trmnum`, CHARACTER CHARSET utf8), `UnLocationGetCodeByID` (ssh.`origin_id`), `UnLocationGetCodeByID` (ssh.`destination_id`), ssh.`schedule_no`) AS voyageNo,
      (SELECT u.`unit_no` FROM `lcl_unit` u WHERE u.`id` = us.`unit_id` LIMIT 1) AS containerNo,
      IF(bk.`booking_type` = 'E',ssd.std,ssd.`sta`) AS eta,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = ssd.`sp_acct_no` LIMIT 1) AS ssline,
      `UnLocationGetNameStateCntryByID` (ssh.`origin_id`) AS origin,
      `UnLocationGetNameStateCntryByID` (ssh.`destination_id`) AS destination,
      bk.`ship_acct_no` AS shipperNo,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = bk.`ship_acct_no` LIMIT 1) AS shipperName,
      bk.`cons_acct_no` AS consigneeNo,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = bk.`cons_acct_no` LIMIT 1) AS consigneeName,
      bk.`fwd_acct_no` AS forwarderNo,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = bk.`fwd_acct_no` LIMIT 1) AS forwarderName,
      bk.`third_party_acct_no` AS thirdPartyNo,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = bk.`third_party_acct_no` LIMIT 1) AS thirdPartyName,
      bk.`agent_acct_no` AS agentNo,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = bk.`agent_acct_no` LIMIT 1) AS agentName,
      COALESCE(ssh.`billing_trmnum`, bk.`billing_terminal`) AS terminal,
      (SELECT `code` FROM `genericcode_dup` WHERE `codedesc` = ssd.`sp_reference_name` AND `codetypeid` = (SELECT `codetypeid` FROM `codetype` WHERE `description` = 'Vessel Codes' LIMIT 1) LIMIT 1) AS vesselNo,
      (SELECT `masterbl` FROM `lcl_unit_ss_manifest` WHERE `ss_header_id` = ssh.`id` AND `unit_id` = us.`unit_id` LIMIT 1) AS masterBl,
      (SELECT `sub_house_bl` FROM `lcl_booking_import` WHERE `file_number_id` = fn.`id` LIMIT 1) AS subhouseBl,
      new.`ap_gl_mapping_id` AS apGlMappingId,
      new.`ap_amount` AS costAmount,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = new.`sp_acct_no` LIMIT 1) AS vendorName,
      new.`sp_acct_no` AS vendorNo,
      new.`invoice_number` AS invoiceNo,
      new.`id` AS costId,
      new.`modified_by_user_id` AS userId,
      bk.poo_id AS mOriginId,
      bk.`billing_terminal` AS mExpBookingTerminal
    INTO
      mFileNumberId,
      mShipmentType,
      mBlNo,
      mDrNo,
      mBookingNo,
      mVoyageNo,
      mContainerNo,
      mEta,
      mSsline,
      mOrigin,
      mDestination,
      mShipperNo,
      mShipperName,
      mConsigneeNo,
      mConsigneeName,
      mForwarderNo,
      mForwarderName,
      mThirdPartyNo,
      mThirdPartyName,
      mAgentNo,
      mAgentName,
      mTerminal,
      mVesselNo,
      mMasterBl,
      mSubhouseBl,
      mApGlMappingId,
      mCostAmount,
      mVendorName,
      mVendorNo,
      mInvoiceNo,
      mCostId,
      mUserId,
      mOriginId,
      mExpBookingTerminal
    FROM
      `lcl_file_number` fn
      JOIN `lcl_booking` bk
        ON (fn.`id` = bk.`file_number_id`)
      JOIN `lcl_booking_piece` bp
        ON (fn.`id` = bp.`file_number_id`)
      LEFT JOIN `lcl_booking_piece_unit` bpu
        ON (bp.`id` = bpu.`booking_piece_id` AND bpu.id=mBkPieceUnitId)
      LEFT JOIN `lcl_unit_ss` us
        ON (bpu.`lcl_unit_ss_id` = us.`id`)
      LEFT JOIN `lcl_ss_header` ssh
        ON (us.`ss_header_id` = ssh.`id`)
      LEFT JOIN `lcl_ss_detail` ssd
        ON (ssh.`id` = ssd.`ss_header_id`)
    WHERE fn.`id` = new.`file_number_id`
    GROUP BY fn.`id`;
        SELECT
      IF(COUNT(transaction_id)>0, FALSE, TRUE),
      `status`
    INTO
      mAccrualNotFound,
      mAccrualStatus
    FROM
      `transaction_ledger`
    WHERE `cost_id` = mCostId
      AND `transaction_type` = 'AC'
      AND `drcpt` = mDrNo;
    SELECT
      gl.`bluescreen_chargecode`,
      gl.`charge_code`,      
      IF(mShipmentType = 'LCLE',
      `DeriveLCLExportGlAccount`(gl.id,mExpBookingTerminal,mOriginId),
      `DeriveGlAccount`(gl.charge_Code,mShipmentType,gl.transaction_type,mTerminal)
      ) AS gl_account
    INTO
      mBsCostCode,
      mCostCode,
      mGlAccount
    FROM
      `gl_mapping` gl
    WHERE gl.`id` = mApGlMappingId ;
    IF (mAccrualNotFound) THEN
      INSERT INTO `lcl_booking_ac_trans` (
        `file_number_id`,
        `trans_type`,
        `trans_datetime`,
        `entry_type`,
        `reference_no`,
        `amount`,
        `entered_by_user_id`,
        `entered_datetime`,
        `modified_by_user_id`,
        `modified_datetime`
      )
      VALUES (
        mFileNumberId,
        'AC',
        NOW(),
        'A',
        '',
        mCostAmount,
        mUserId,
        NOW(),
        mUserId,
        NOW()
      );
      INSERT INTO `lcl_booking_ac_ta` (
        `lcl_booking_ac_trans_id`,
        `lcl_booking_ac_id`,
        `amount`,
        `entered_by_user_id`,
        `entered_datetime`,
        `modified_by_user_id`,
        `modified_datetime`
      )
      VALUES (
        (SELECT LAST_INSERT_ID()),
        mCostId,
        0.00,
        mUserId,
        NOW(),
        mUserId,
        NOW()
      );
      INSERT INTO `transaction_ledger` (
	  `cost_id`,
	  `cust_name`,
	  `cust_no`,
	  `invoice_number`,
	  `drcpt`,
	  `bill_ladding_no`,
	  `transaction_type`,
	  `status`,
	  `subledger_source_code`,
	  `shipment_type`,
	  `bluescreen_chargecode`,
	  `charge_code`,
	  `gl_account_number`,
	  `transaction_amt`,
	  `balance`,
	  `balance_in_process`,
	  `transaction_date`,
	  `sailing_date`,
	  `booking_no`,
	  `voyage_no`,
	  `container_no`,
	  `master_bl`,
	  `sub_house_bl`,
	  `originating_terminal`,
	  `destination`,
	  `ship_name`,
	  `ship_no`,
	  `cons_name`,
	  `cons_no`,
	  `fwd_name`,
	  `fwd_no`,
	  `third_pty_name`,
	  `third_pty_no`,
	  `agent_name`,
	  `agent_no`,
	  `billing_terminal`,
	  `terminal`,
	  `vessel_no`,
	  `bill_to`,
	  `ready_to_post`,
	  `currency_code`,
	  `manifest_flag`,
	  `created_on`,
	  `created_by`
      )
      VALUES (
	  mCostId,
	  mVendorName,
	  mVendorNo,
	  mInvoiceNo,
	  mDrNo,
	  mBlNo,
	  'AC',
	  'Open',
	  'ACC',
	  mShipmentType,
	  mBsCostCode,
	  mCostCode,
	  mGlAccount,
	  mCostAmount,
	  mCostAmount,
	  mCostAmount,
	  COALESCE(mEta, NOW()),
	  mEta,
	  mBookingNo,
	  mVoyageNo,
	  mContainerNo,
	  mMasterBl,
	  mSubhouseBl,
	  mOrigin,
	  mDestination,
	  mShipperName,
	  mShipperNo,
	  mConsigneeName,
	  mConsigneeNo,
	  mForwarderName,
	  mForwarderNo,
	  mThirdPartyName,
	  mThirdPartyNo,
	  mAgentName,
	  mAgentNo,
	  mTerminal,
	  mTerminal,
	  mVesselNo,
	  'Y',
	  'ON',
	  'USD',
	  'N',
	  NOW(),
	  mUserId
      );
    ELSEIF (mAccrualStatus IN ('Open', 'Dispute', 'EDI Dispute')) THEN
      UPDATE
	  `transaction_ledger`
      SET
	  `cust_name` = mVendorName,
	  `cust_no` = mVendorNo,
	  `invoice_number` = mInvoiceNo,
	  `bill_ladding_no` = mBlNo,
	  `bluescreen_chargecode` = mBsCostCode,
	  `charge_code` = mCostCode,
	  `gl_account_number` = mGlAccount,
	  `transaction_amt` = mCostAmount,
	  `balance` = mCostAmount,
	  `balance_in_process` = mCostAmount,
	  `transaction_date` = COALESCE(mEta, NOW()),
	  `sailing_date` = mEta,
	  `booking_no` = mBookingNo,
	  `voyage_no` = mVoyageNo,
	  `container_no` = mContainerNo,
	  `master_bl` = mMasterBl,
	  `sub_house_bl` = mSubhouseBl,
	  `originating_terminal` = mOrigin,
	  `destination` = mDestination,
	  `ship_name` = mShipperName,
	  `ship_no` = mShipperNo,
	  `cons_name` = mConsigneeName,
	  `cons_no` = mConsigneeNo,
	  `fwd_name` = mForwarderName,
	  `fwd_no` = mForwarderNo,
	  `third_pty_name` = mThirdPartyName,
	  `third_pty_no` = mThirdPartyNo,
	  `agent_name` = mAgentName,
	  `agent_no` = mAgentNo,
	  `billing_terminal` = mTerminal,
	  `terminal` = mTerminal,
	  `vessel_no` = mVesselNo,
	  `updated_on` = NOW(),
	  `updated_by` = mUserId
      WHERE `cost_id` = mCostId
	  AND `transaction_type` = 'AC'
	  AND `drcpt` = mDrNo;
    END IF;
  END IF;
END;
$$

DELIMITER ;

ALTER TABLE `un_location`
  ADD COLUMN `origin_terminal` VARCHAR(5) NULL,
  ADD  INDEX `un_location_fk03` (`origin_terminal`),
  ADD CONSTRAINT `un_location_fk03` FOREIGN KEY (`origin_terminal`)
    REFERENCES `terminal`(`trmnum`) ON UPDATE CASCADE ON DELETE RESTRICT;
   	

--Mantis item 13565 FCL-EXPORTS by priyanka on 28th jul 2016
DELIMITER $$

DROP VIEW IF EXISTS `rates_list`$$

CREATE VIEW `rates_list` AS (
SELECT
  `buy`.`Fcl_std_id`         AS `Fcl_std_id`,
  `buy`.`start_date`         AS `start_date`,
  `buy`.`end_date`           AS `end_date`,
  `buy`.`origin_terminal`    AS `origin_terminal`,
  `buy`.`destination_port`   AS `destination_port`,
  `buy`.`ssline_no`          AS `ssline_no`,
  `buy`.`Com_num`            AS `Com_num`,
  `buy`.`contract`           AS `contract`,
  `buy`.`origin_region`      AS `origin_region`,
  `buy`.`destination_region` AS `destination_region`,
  `cost`.`fcl_cost_id`       AS `fcl_cost_id`,
  `cost`.`cost_id`           AS `cost_id`,
  `cost`.`cost_type`         AS `cost_type`,
  `rate`.`fcl_cost_type_id`  AS `fcl_cost_type_id`,
  `rate`.`unit_type`         AS `unit_type`,
  `rate`.`active_amt`        AS `active_amt`,
  `rate`.`ctc_amt`           AS `ctc_amt`,
  `rate`.`ftf_amt`           AS `ftf_amt`,
  `rate`.`minimum_amt`       AS `minimum_amt`,
  `rate`.`rat_amount`        AS `rat_amount`,
  `rate`.`Standard`          AS `Standard`,
  `rate`.`markup`            AS `markup`,
  `rate`.`currency_type`     AS `currency_type`,
  `code`.`codedesc`          AS `codedesc`,
  `rate`.`fcl_cost_id`       AS `fcl_cost_id1`,
  `buy`.`polCode`            AS `polCode`,
  `costtype`.`id`            AS `cost_type_id`,
  `costtype`.`Codetypeid`    AS `cost_codetype_id`,
  `costtype`.`code`          AS `cost_code`,
  `costtype`.`codedesc`      AS `cost_code_desc`,
  `tp`.`acct_no`             AS `acct_no`,
  `tp`.`acct_name`           AS `acct_name`,
  `unit`.`code`              AS `unit_code`,
  `unit`.`codedesc`          AS `unit_code_desc`,
  `misc`.`days_in_transit`   AS `transit_time`,
  `misc`.`remarks`           AS `remarks`,
  `misc`.`hazardous_flag`    AS `hazardous_flag`,
  `misc`.`port_of_exit`      AS `port_of_exit`,
  `misc`.`local_drayage`     AS `dray`,
  `org`.`lat`                AS `lat`,
  `org`.`lng`                AS `lng`,
  `code`.`code`              AS `chargecode`
FROM ((((((((`fcl_buy` `buy`
          LEFT JOIN `fcl_org_dest_misc_data` `misc`
            ON (((`misc`.`origin_terminal` = `buy`.`origin_terminal`)
                 AND (`misc`.`destination_port` = `buy`.`destination_port`)
                 AND (`misc`.`ssline_no` = `buy`.`ssline_no`))))
         JOIN `un_location` `org`
           ON ((`org`.`id` = `buy`.`origin_terminal`)))
        JOIN `fcl_buy_cost` `cost`)
       JOIN `fcl_buy_cost_type_rates` `rate`)
      JOIN `genericcode_dup` `code`)
     JOIN `genericcode_dup` `costtype`)
    JOIN `trading_partner` `tp`)
   JOIN `genericcode_dup` `unit`)
WHERE ((`buy`.`Fcl_std_id` = `cost`.`fcl_std_id`)
       AND (`code`.`id` = `cost`.`cost_id`)
       AND (`unit`.`id` = `rate`.`unit_type`)
       AND (`costtype`.`id` = `cost`.`cost_type`)
       AND (`buy`.`ssline_no` = `tp`.`acct_no`)
       AND (`rate`.`fcl_cost_id` = `cost`.`fcl_cost_id`)))$$

DELIMITER ;

--  Mantis Item-13593(LCL-Imports) by Stefy on 28 July 2016 (This query only for Local and Econo)
  ALTER TABLE lcl_booking_import DROP COLUMN 7512_print_entry,ADD COLUMN `customs_entry_no_prtloc` ENUM('B','L','R') DEFAULT 'R' NOT NULL AFTER `customs_entry_no`;

--  Mantis Item-13593(LCL-Imports) by Stefy on 28 July 2016 
DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBookingImport_update`$$

CREATE
    
    TRIGGER `after_LclBookingImport_update` AFTER UPDATE ON `lcl_booking_import` 
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT ;
    DECLARE eta_at_fd TEXT ;
   IF new.transshipment!=old.transshipment THEN
       SET updated_values=(SELECT concat_string(updated_values,'Transshipment',IF(old.transshipment=0,'No','Yes'),IF(new.transshipment=0,'No','Yes')));
    END IF;
   IF ((OLD.picked_up_datetime IS NULL AND NEW.picked_up_datetime IS NOT NULL) OR NEW.picked_up_datetime != OLD.picked_up_datetime OR (OLD.picked_up_datetime IS NOT NULL AND NEW.picked_up_datetime IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'PickUp Date',IFNULL(DATE_FORMAT(old.picked_up_datetime,'%d-%b-%Y'),''),IFNULL(DATE_FORMAT(new.picked_up_datetime,'%d-%b-%Y'),'')));
    END IF;
   IF ((OLD.go_datetime IS NULL AND NEW.go_datetime IS NOT NULL) OR NEW.go_datetime != OLD.go_datetime OR (OLD.go_datetime IS NOT NULL AND NEW.go_datetime IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'G/O Date',IFNULL(DATE_FORMAT(old.go_datetime,'%d-%b-%Y'),''),IFNULL(DATE_FORMAT(new.go_datetime,'%d-%b-%Y'),'')));
    END IF;
   IF ((OLD.fd_eta IS NULL AND NEW.fd_eta IS NOT NULL) OR NEW.fd_eta != OLD.fd_eta OR (OLD.fd_eta IS NOT NULL AND NEW.fd_eta IS NULL)) THEN
       SET eta_at_fd=(SELECT concat_string(eta_at_fd,'ETA at FD',IFNULL(DATE_FORMAT(old.fd_eta,'%d-%b-%Y'),''),IFNULL(DATE_FORMAT(new.fd_eta,'%d-%b-%Y'),'')));
    END IF;
   IF ((OLD.sub_house_bl IS NULL AND NEW.sub_house_bl IS NOT NULL) OR NEW.sub_house_bl != OLD.sub_house_bl OR (OLD.sub_house_bl IS NOT NULL AND NEW.sub_house_bl IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'Sub House BL',IFNULL(old.sub_house_bl,''),IFNULL(new.sub_house_bl,'')));
   END IF;
    IF ((OLD.customs_entry_no IS NULL AND NEW.customs_entry_no IS NOT NULL AND NEW.customs_entry_no !='') OR NEW.customs_entry_no != OLD.customs_entry_no OR (OLD.customs_entry_no IS NOT NULL AND OLD.customs_entry_no !='' AND NEW.customs_entry_no IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'Entry#',IFNULL(old.customs_entry_no,''),IFNULL(new.customs_entry_no,'')));
   END IF;
   IF new.declared_value_estimated!=old.declared_value_estimated THEN
       SET updated_values=(SELECT concat_string(updated_values,'Value Estimated',IF(old.declared_value_estimated=0,'No','Yes'),IF(new.declared_value_estimated=0,'No','Yes')));
   END IF;
   IF new.declared_weight_estimated!=old.declared_weight_estimated THEN
       SET updated_values=(SELECT concat_string(updated_values,'Weight Estimated',IF(old.declared_weight_estimated=0,'No','Yes'),IF(new.declared_weight_estimated=0,'No','Yes')));
   END IF;
   IF ((OLD.inbond_via IS NULL AND NEW.inbond_via IS NOT NULL) OR NEW.inbond_via != OLD.inbond_via OR (OLD.inbond_via IS NOT NULL AND NEW.inbond_via IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'Inbond Via',IFNULL(old.inbond_via,''),IFNULL(new.inbond_via,'')));
   END IF;
   IF ((OLD.it_class IS NULL AND NEW.it_class IS NOT NULL) OR NEW.it_class != OLD.it_class OR (OLD.it_class IS NOT NULL AND NEW.it_class IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'IT Class',IFNULL(old.it_class,''),IFNULL(new.it_class,'')));
   END IF;
   IF ((OLD.customs_entry_class IS NULL AND NEW.customs_entry_class IS NOT NULL) OR NEW.customs_entry_class != OLD.customs_entry_class OR (OLD.customs_entry_class IS NOT NULL AND NEW.customs_entry_class IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'Class of Entry',IFNULL(old.customs_entry_class,''),IFNULL(new.customs_entry_class,'')));
   END IF;
   IF new.elite_ipi_loaded !=old.elite_ipi_loaded THEN
       SET updated_values=(SELECT concat_string(updated_values,'Loaded on IPI Container',IF(old.elite_ipi_loaded=0,'No','Yes'),IF(new.elite_ipi_loaded=0,'No','Yes')));
       END IF;
   IF isNotEqual(OLD.picked_up_by,NEW.picked_up_by) THEN
       SET updated_values=(`concat_string`(updated_values,'Picked By',OLD.picked_up_by,NEW.picked_up_by));
       END IF;
   IF ((OLD.door_delivery_eta IS NULL AND NEW.door_delivery_eta IS NOT NULL) OR NEW.door_delivery_eta != OLD.door_delivery_eta OR (OLD.door_delivery_eta IS NOT NULL AND NEW.door_delivery_eta IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'Door Delivery ETA',IFNULL(DATE_FORMAT(old.door_delivery_eta,'%d-%b-%Y'),''),IFNULL(DATE_FORMAT(new.door_delivery_eta,'%d-%b-%Y'),'')));
    END IF;
    IF new.express_release!=old.express_release THEN
      SET updated_values=(SELECT concat_string(updated_values,'Express Release',IF(old.express_release=0,'No','Yes'),IF(new.express_release=0,'No','Yes')));
    END IF;
    IF new.door_delivery_comment!=old.door_delivery_comment THEN
       SET updated_values=(SELECT concat_string(updated_values,'Door Delivery Comment',IF(old.door_delivery_comment=0,'No','Yes'),IF(new.door_delivery_comment=0,'No','Yes')));
    END IF;
     IF OLD.door_delivery_status != NEW.door_delivery_status THEN
     SET updated_values=(SELECT concat_string(updated_values,'Door Delivery Status',(SELECT codedesc FROM `genericcode_dup` WHERE codetypeid=(SELECT codetypeid FROM codetype WHERE description = 'door delivery status' LIMIT 1) 
     AND CODE =old.door_delivery_status),
     (SELECT codedesc FROM `genericcode_dup` WHERE codetypeid=(SELECT codetypeid FROM codetype WHERE description = 'door delivery status' LIMIT 1) 
     AND CODE =new.door_delivery_status)));
       END IF;
       
      IF new.customs_entry_no_prtloc!=old.customs_entry_no_prtloc THEN
       SET updated_values=(SELECT concat_string(updated_values,'7512 Print Entry',IFNULL(old.customs_entry_no_prtloc,''),IFNULL(new.customs_entry_no_prtloc,'')));
    END IF;     
       
    IF updated_values IS NOT NULL THEN
      SET updated_values=CONCAT(updated_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(OLD.file_number_id,'DR-AutoNotes',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    IF eta_at_fd IS NOT NULL THEN
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(OLD.file_number_id,'DR-AutoNotes',eta_at_fd,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    
  END;
$$

DELIMITER ;

--  Mantis Item-13569(Accounting) by Nambu on 2nd August 2016 

CREATE TABLE `terminal_manager`(  
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) NOT NULL,
  `trmnum` VARCHAR(5) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `user_id_fk01` (`user_id`),
  INDEX `trmnum_fk02` (`trmnum`),
  CONSTRAINT `user_id_fk01` FOREIGN KEY (`user_id`) REFERENCES `eci_lw_test`.`user_details`(`user_id`),
  CONSTRAINT `trmnum_fk02` FOREIGN KEY (`trmnum`) REFERENCES `eci_lw_test`.`terminal`(`trmnum`)
) ENGINE=INNODB CHARSET=latin1 COLLATE=latin1_swedish_ci;


INSERT INTO terminal_manager (user_id,trmnum) VALUES 
  ((SELECT user_id FROM user_details WHERE login_name ='dan'),'01'),
  ((SELECT user_id FROM user_details WHERE login_name ='dan'),'03'),
  ((SELECT user_id FROM user_details WHERE login_name ='dan'),'04'),
  ((SELECT user_id FROM user_details WHERE login_name ='dan'),'08'),
  ((SELECT user_id FROM user_details WHERE login_name ='dan'),'19'),
  ((SELECT user_id FROM user_details WHERE login_name ='dan'),'09'),
  ((SELECT user_id FROM user_details WHERE login_name ='dan'),'15'),
  ((SELECT user_id FROM user_details WHERE login_name ='dan'),'17'),
  ((SELECT user_id FROM user_details WHERE login_name ='dan'),'18'),
  ((SELECT user_id FROM user_details WHERE login_name ='dan'),'79');
INSERT INTO terminal_manager (user_id,trmnum) VALUES 
  ((SELECT user_id FROM user_details WHERE login_name ='kkraus'),'12'),
  ((SELECT user_id FROM user_details WHERE login_name ='kkraus'),'19'),
  ((SELECT user_id FROM user_details WHERE login_name ='kkraus'),'24'),
  ((SELECT user_id FROM user_details WHERE login_name ='kkraus'),'42'),
  ((SELECT user_id FROM user_details WHERE login_name ='kkraus'),'49'),
  ((SELECT user_id FROM user_details WHERE login_name ='kkraus'),'54'),
  ((SELECT user_id FROM user_details WHERE login_name ='kkraus'),'79');
INSERT INTO terminal_manager (user_id,trmnum) VALUES 
  ((SELECT user_id FROM user_details WHERE login_name ='marner'),'02'),
  ((SELECT user_id FROM user_details WHERE login_name ='marner'),'80'),
  ((SELECT user_id FROM user_details WHERE login_name ='marner'),'81'),
  ((SELECT user_id FROM user_details WHERE login_name ='marner'),'82'),
  ((SELECT user_id FROM user_details WHERE login_name ='marner'),'83'),
  ((SELECT user_id FROM user_details WHERE login_name ='marner'),'84'),
  ((SELECT user_id FROM user_details WHERE login_name ='marner'),'85'),
  ((SELECT user_id FROM user_details WHERE login_name ='marner'),'86'),
  ((SELECT user_id FROM user_details WHERE login_name ='marner'),'88'); 
INSERT INTO terminal_manager (user_id,trmnum) VALUES 
  ((SELECT user_id FROM user_details WHERE login_name ='cgarcia'),'03');  
INSERT INTO terminal_manager (user_id,trmnum) VALUES 
  ((SELECT user_id FROM user_details WHERE login_name ='avillar'),'04'),
  ((SELECT user_id FROM user_details WHERE login_name ='avillar'),'73');
INSERT INTO terminal_manager (user_id,trmnum) VALUES 
  ((SELECT user_id FROM user_details WHERE login_name ='jpuig'),'05'),
  ((SELECT user_id FROM user_details WHERE login_name ='jpuig'),'30'),
  ((SELECT user_id FROM user_details WHERE login_name ='jpuig'),'35'); 
INSERT INTO terminal_manager (user_id,trmnum) VALUES 
  ((SELECT user_id FROM user_details WHERE login_name ='cmerino'),'07'),
  ((SELECT user_id FROM user_details WHERE login_name ='cmerino'),'64');
INSERT INTO terminal_manager (user_id,trmnum) VALUES 
  ((SELECT user_id FROM user_details WHERE login_name ='aescobar'),'09'),
  ((SELECT user_id FROM user_details WHERE login_name ='aescobar'),'23'),
  ((SELECT user_id FROM user_details WHERE login_name ='aescobar'),'39'),
  ((SELECT user_id FROM user_details WHERE login_name ='aescobar'),'53'),
  ((SELECT user_id FROM user_details WHERE login_name ='aescobar'),'69');
INSERT INTO terminal_manager (user_id,trmnum) VALUES 
  ((SELECT user_id FROM user_details WHERE login_name ='CPEREZDECO'),'13'),
  ((SELECT user_id FROM user_details WHERE login_name ='CPEREZDECO'),'66');
INSERT INTO terminal_manager (user_id,trmnum) VALUES 
  ((SELECT user_id FROM user_details WHERE login_name ='aaguirre'),'25'),
  ((SELECT user_id FROM user_details WHERE login_name ='aaguirre'),'26'),
  ((SELECT user_id FROM user_details WHERE login_name ='aaguirre'),'27'),
  ((SELECT user_id FROM user_details WHERE login_name ='aaguirre'),'28'),
  ((SELECT user_id FROM user_details WHERE login_name ='aaguirre'),'55'),
  ((SELECT user_id FROM user_details WHERE login_name ='aaguirre'),'56'),
  ((SELECT user_id FROM user_details WHERE login_name ='aaguirre'),'57'),
  ((SELECT user_id FROM user_details WHERE login_name ='aaguirre'),'45'),
  ((SELECT user_id FROM user_details WHERE login_name ='aaguirre'),'15'); 
INSERT INTO terminal_manager (user_id,trmnum) VALUES 
  ((SELECT user_id FROM user_details WHERE login_name ='abruni'),'15');     
INSERT INTO terminal_manager (user_id,trmnum) VALUES 
  ((SELECT user_id FROM user_details WHERE login_name ='SBALDWIN'),'18');
INSERT INTO terminal_manager (user_id,trmnum) VALUES 
  ((SELECT user_id FROM user_details WHERE login_name ='GBERMENT'),'59');
INSERT INTO terminal_manager (user_id,trmnum) VALUES 
  ((SELECT user_id FROM user_details WHERE login_name ='mdeleon'),'63');
INSERT INTO terminal_manager (user_id,trmnum) VALUES 
  ((SELECT user_id FROM user_details WHERE login_name ='rsanchez'),'65');
INSERT INTO terminal_manager (user_id,trmnum) VALUES 
  ((SELECT user_id FROM user_details WHERE login_name ='smulch'),'67'),
  ((SELECT user_id FROM user_details WHERE login_name ='smulch'),'68');
INSERT INTO terminal_manager (user_id,trmnum) VALUES 
  ((SELECT user_id FROM user_details WHERE login_name ='gjaramil'),'08'),
  ((SELECT user_id FROM user_details WHERE login_name ='gjaramil'),'10'),
  ((SELECT user_id FROM user_details WHERE login_name ='gjaramil'),'11'),
  ((SELECT user_id FROM user_details WHERE login_name ='gjaramil'),'51'),
  ((SELECT user_id FROM user_details WHERE login_name ='gjaramil'),'52'),
  ((SELECT user_id FROM user_details WHERE login_name ='gjaramil'),'21'),
  ((SELECT user_id FROM user_details WHERE login_name ='gjaramil'),'22'),
  ((SELECT user_id FROM user_details WHERE login_name ='gjaramil'),'38'),
  ((SELECT user_id FROM user_details WHERE login_name ='gjaramil'),'40'),
  ((SELECT user_id FROM user_details WHERE login_name ='gjaramil'),'41');
INSERT INTO terminal_manager (user_id,trmnum) VALUES 
  ((SELECT user_id FROM user_details WHERE login_name ='irompaey'),'60'),
  ((SELECT user_id FROM user_details WHERE login_name ='irompaey'),'61'),
  ((SELECT user_id FROM user_details WHERE login_name ='irompaey'),'62'),
  ((SELECT user_id FROM user_details WHERE login_name ='irompaey'),'63'),
  ((SELECT user_id FROM user_details WHERE login_name ='irompaey'),'65'),
  ((SELECT user_id FROM user_details WHERE login_name ='irompaey'),'69');
 INSERT INTO terminal_manager (user_id,trmnum) VALUES 
  ((SELECT user_id FROM user_details WHERE login_name ='mdunn'),'14'),
  ((SELECT user_id FROM user_details WHERE login_name ='mdunn'),'46'),
  ((SELECT user_id FROM user_details WHERE login_name ='mdunn'),'47'),
  ((SELECT user_id FROM user_details WHERE login_name ='mdunn'),'48'),
  ((SELECT user_id FROM user_details WHERE login_name ='mdunn'),'50'),
  ((SELECT user_id FROM user_details WHERE login_name ='mdunn'),'16'),
  ((SELECT user_id FROM user_details WHERE login_name ='mdunn'),'20'),
  ((SELECT user_id FROM user_details WHERE login_name ='mdunn'),'44');
INSERT INTO terminal_manager (user_id,trmnum) VALUES 
  ((SELECT user_id FROM user_details WHERE login_name ='janarumo'),'61');    
INSERT INTO terminal_manager (user_id,trmnum) VALUES 
  ((SELECT user_id FROM user_details WHERE login_name ='vgonzalez'),'17');  
INSERT INTO terminal_manager (user_id,trmnum) VALUES 
  ((SELECT user_id FROM user_details WHERE login_name ='vargenz'),'15'),
  ((SELECT user_id FROM user_details WHERE login_name ='vargenz'),'65');
 

--  Mantis Item-13405(LCL-Imports) by Stefy on 02 August 2016

UPDATE lcl_ss_remarks SET followup_datetime = NULL  
WHERE TYPE='auto' AND followup_datetime IS NOT NULL ;

--  Mantis Item-9342(Logiware) by Stefy on 02 August 2016

CREATE TABLE genericcode_dup_deleted ( 
id INT(11) NOT NULL AUTO_INCREMENT,
genericcode_dup_id INT(11) NOT NULL,
Codetypeid INT(11) DEFAULT NULL,
CODE VARCHAR(100) DEFAULT NULL,
codedesc VARCHAR(500) DEFAULT NULL,
Field1 VARCHAR(1000) DEFAULT NULL,Field2 VARCHAR(1000) DEFAULT NULL,Field3 VARCHAR(100) DEFAULT NULL,
Field4 VARCHAR(400) DEFAULT NULL,Field5 VARCHAR(100) DEFAULT NULL,Field6 VARCHAR(100) DEFAULT NULL,
Field7 VARCHAR(100) DEFAULT NULL,Field8 VARCHAR(100) DEFAULT NULL,Field9 VARCHAR(100) DEFAULT NULL,
Field10 VARCHAR(100) DEFAULT NULL,Field11 VARCHAR(100) DEFAULT NULL,Field12 VARCHAR(100) DEFAULT NULL,
deleted_by VARCHAR(50) DEFAULT NULL,
deleted_on DATETIME NULL,
 PRIMARY KEY (`id`));


DELIMITER $$

CREATE TRIGGER  genericode_dup_after_delete
AFTER DELETE
ON genericcode_dup FOR EACH ROW
 BEGIN

INSERT INTO genericcode_dup_deleted
   (genericcode_dup_id,
 Codetypeid,
 CODE,codedesc,
Field1,Field2,Field3,Field4,Field5,Field6,Field7,Field8,Field9,Field10,Field11,Field12,deleted_on
  )
   VALUES
   ( OLD.id,
   OLD.Codetypeid,
   OLD.code,OLD.codedesc,
   OLD.Field1,OLD.Field2,OLD.Field3,OLD.Field4,OLD.Field5,OLD.Field6,OLD.Field7,OLD.Field8,
   OLD.Field9,OLD.Field10,OLD.Field11,OLD.Field12,SYSDATE());
   END; 
   $$
DELIMITER ;

 -- Mantis Item-13569(Accounting) by Nambu on 3rd August 2016 

ALTER TABLE `terminal`  DROP COLUMN `manager_name`, DROP COLUMN `manager_email`;


--Mantis item -13977(FCL-Exports) by priyanka on 4th aug 2016

ALTER TABLE `transaction_ledger` CHANGE `voyage_no` `voyage_no` VARCHAR(50) CHARSET latin1 COLLATE latin1_swedish_ci NULL; 
ALTER TABLE `transaction` CHANGE `voyage_no` `voyage_no` VARCHAR(50) CHARSET latin1 COLLATE latin1_swedish_ci NULL; 

-- Mantis Item - 12357  By Aravindhan  on 5th August 2016 

CREATE TABLE lcl_correction_commodity (
  id INT PRIMARY KEY AUTO_INCREMENT,
  correction_id BIGINT (20) UNSIGNED,   
  commodityId BIGINT (20) UNSIGNED,
  totalCft DECIMAL (10, 3) UNSIGNED,
  totalCbm DECIMAL (10, 3) UNSIGNED,
  totalLbs DECIMAL (10, 3) UNSIGNED,
  totalKgs DECIMAL (10, 3) UNSIGNED,
  INDEX `lcl_correction_commodity_fk01`(correction_id),
  INDEX `lcl_correction_commodity_fk02`(commodityId),
  CONSTRAINT `lcl_correction_commodity_fk01` FOREIGN KEY (`correction_id`)
    REFERENCES `lcl_correction`(`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `lcl_correction_commodity_fk02` FOREIGN KEY (`commodityId`)
    REFERENCES `commodity_type`(`id`) ON UPDATE CASCADE ON DELETE RESTRICT);

--  Mantis Item-13993(Logiware FCL-Exports) by Stefy on 05 August 2016
ALTER TABLE `role_duties` ADD COLUMN `no_997_edi_submission` TINYINT(1) DEFAULT 0 NULL; 

--  Mantis Item-13257 LCL-Exports by Aravindhan on 05 August 2016
INSERT INTO genericcode_dup(codetypeid,CODE,codedesc) VALUES(52,'019','CHANGE COMMODITY/WEIGHT/MEASURE');

ALTER TABLE lcl_correction_charge ADD COLUMN manual_charge TINYINT(1) DEFAULT 0;

DELIMITER $$

DROP PROCEDURE IF EXISTS `GetLCLExportRates`$$

CREATE  PROCEDURE `GetLCLExportRates`(
  IN PooCode VARCHAR (5),
  IN PolCode VARCHAR (5),
  IN PodCode VARCHAR (5),
  IN FdCode VARCHAR (5),
  IN RateType CHAR(1),
  IN commNo VARCHAR(7),
  IN extraCharges VARCHAR(30)
) MODIFIES SQL DATA DETERMINISTIC

MAIN : BEGIN
  DECLARE mSLS VARCHAR (64) DEFAULT "CalculatingLCLExportRates" ;
  DECLARE mSLH VARCHAR (256) DEFAULT "" ;
  DECLARE mSLT VARCHAR (128) DEFAULT UUID() ;
  DECLARE pooterminal VARCHAR(2) DEFAULT '';  
  DECLARE polterminal VARCHAR(2) DEFAULT ''; 
  DECLARE podPort VARCHAR(3) DEFAULT '';
  DECLARE fdPort VARCHAR(3) DEFAULT '';
  DECLARE zeroCommodity VARCHAR(6) DEFAULT '000000';
  DECLARE bluecharges TEXT DEFAULT NULL;
  
SELECT trmnum INTO pooterminal FROM terminal WHERE unlocationcode1 =PooCode AND actyon = RateType LIMIT 1;

SELECT trmnum INTO polterminal FROM terminal WHERE unlocationcode1 =PolCode AND actyon = RateType LIMIT 1;

IF (ISNULL(pooterminal) AND ISNULL(polterminal)) THEN
  CALL LogSQLEntry (3, mSLS, mSLT, "Rates Claculation Stopped; Terminal Invalid.") ;
  LEAVE MAIN;
END IF ; 

SELECT eciportcode INTO podPort FROM ports WHERE unlocationcode =PodCode LIMIT 1;

SELECT eciportcode INTO fdPort FROM ports WHERE unlocationcode =FdCode LIMIT 1;

IF (ISNULL(fdPort) AND ISNULL(podPort))  THEN
  CALL LogSQLEntry (3, mSLS, mSLT, "Rates Claculation Stopped; Ports Invalid.") ;
  LEAVE MAIN;
END IF;

SELECT CONCAT_WS(',',cd01,cd02,cd03,cd04,cd05,cd06,cd07,cd08,cd09,cd10,cd11,cd12)
INTO bluecharges FROM eci_elite_prod.stdchg WHERE term= pooterminal 
 AND PORT=fdPort AND commod=commNo;
 
 IF (ISNULL(bluecharges)) THEN
 SELECT CONCAT_WS(',',cd01,cd02,cd03,cd04,cd05,cd06,cd07,cd08,cd09,cd10,cd11,cd12)
 INTO bluecharges FROM eci_elite_prod.stdchg WHERE term= pooterminal 
 AND PORT=fdPort AND commod=zeroCommodity;
 END IF ;
 
 IF (ISNULL(bluecharges)) THEN
 SELECT CONCAT_WS(',',cd01,cd02,cd03,cd04,cd05,cd06,cd07,cd08,cd09,cd10,cd11,cd12)
 INTO bluecharges FROM eci_elite_prod.stdchg WHERE term= pooterminal 
 AND PORT=podPort AND commod=commNo;
 END IF ;
 
 IF (ISNULL(bluecharges)) THEN
 SELECT CONCAT_WS(',',cd01,cd02,cd03,cd04,cd05,cd06,cd07,cd08,cd09,cd10,cd11,cd12)
 INTO bluecharges FROM eci_elite_prod.stdchg WHERE term= pooterminal 
 AND PORT=podPort AND commod=zeroCommodity;
 END IF ;
 
 IF (ISNULL(bluecharges)) THEN
 SELECT CONCAT_WS(',',cd01,cd02,cd03,cd04,cd05,cd06,cd07,cd08,cd09,cd10,cd11,cd12)
 INTO bluecharges FROM eci_elite_prod.stdchg WHERE term = polterminal 
 AND PORT=fdPort AND commod=commNo;
 END IF ;

 IF (ISNULL(bluecharges)) THEN
 SELECT CONCAT_WS(',',cd01,cd02,cd03,cd04,cd05,cd06,cd07,cd08,cd09,cd10,cd11,cd12)
 INTO bluecharges FROM eci_elite_prod.stdchg WHERE term= polterminal 
 AND PORT=fdPort AND commod=zeroCommodity;
  END IF ;
  
 IF (ISNULL(bluecharges)) THEN
 SELECT CONCAT_WS(',',cd01,cd02,cd03,cd04,cd05,cd06,cd07,cd08,cd09,cd10,cd11,cd12)
 INTO bluecharges FROM eci_elite_prod.stdchg WHERE term= polterminal 
 AND PORT=podPort AND commod=commNo;
 END IF ;
 
 IF (ISNULL(bluecharges)) THEN
 SELECT CONCAT_WS(',',cd01,cd02,cd03,cd04,cd05,cd06,cd07,cd08,cd09,cd10,cd11,cd12)
 INTO bluecharges FROM eci_elite_prod.stdchg WHERE term= pollterminal 
 AND PORT=podPort AND commod=zeroCommodity;
 END IF ;
  
 SELECT bluecharges;

END MAIN$$

DELIMITER ;

--  Mantis Item-13257 LCL-Exports by Aravindhan on 08 August 2016

DELIMITER $$

DROP PROCEDURE IF EXISTS `GetExportOceanFreightRate`$$

CREATE  PROCEDURE `GetExportOceanFreightRate`(
  IN PooCode VARCHAR (5),
  IN PolCode VARCHAR (5),
  IN PodCode VARCHAR (5),
  IN FdCode VARCHAR (5),
  IN RateType CHAR(1),
  IN commNo VARCHAR(7),
  IN isBarrel BOOLEAN
)MODIFIES SQL DATA DETERMINISTIC
MAIN : BEGIN
  
  DECLARE mSLS VARCHAR (64) DEFAULT "CalculatingOceanFrieghtRates" ;
  DECLARE mSLH VARCHAR (256) DEFAULT "" ;
  DECLARE mSLT VARCHAR (128) DEFAULT UUID() ;
  DECLARE pooterminal VARCHAR(2) DEFAULT '';  
  DECLARE polterminal VARCHAR(2) DEFAULT ''; 
  DECLARE podPort VARCHAR(3) DEFAULT '';
  DECLARE fdPort VARCHAR(3) DEFAULT '';
  DECLARE zeroCommodity VARCHAR(6) DEFAULT '000000';
  DECLARE marketvalue VARCHAR(2);
  DECLARE mBarrelOFRate DECIMAL(6,2) DEFAULT 0.00;  
  DECLARE mBarrelTTRate DECIMAL(6,2) DEFAULT 0.00;
  DECLARE chargeCode VARCHAR(4) DEFAULT '0001';
  DECLARE cft_e DECIMAL(6,2) DEFAULT 0.00;  
  DECLARE wgt_e DECIMAL(6,2) DEFAULT 0.00;
  DECLARE cft_m DECIMAL(6,2) DEFAULT 0.00;  
  DECLARE wgt_m DECIMAL(6,2) DEFAULT 0.00;
  DECLARE ofRateMin DECIMAL(6,2) DEFAULT 0.00;  
  DECLARE measure DECIMAL(6,2) DEFAULT 0.00;
  DECLARE weight DECIMAL(6,2) DEFAULT 0.00;  
  
SELECT trmnum INTO pooterminal FROM terminal WHERE unlocationcode1 =PooCode AND actyon = RateType LIMIT 1;
SELECT trmnum INTO polterminal FROM terminal WHERE unlocationcode1 =PolCode AND actyon = RateType LIMIT 1;
IF (ISNULL(pooterminal) AND ISNULL(polterminal)) THEN
  CALL LogSQLEntry (3, mSLS, mSLT, "Rates Claculation Stopped; Terminal Invalid.") ;
  LEAVE MAIN;
END IF ; 
SELECT eciportcode INTO podPort FROM ports WHERE unlocationcode =PodCode LIMIT 1;
SELECT eciportcode INTO fdPort FROM ports WHERE unlocationcode =FdCode LIMIT 1;
IF (ISNULL(fdPort) AND ISNULL(podPort))  THEN
  CALL LogSQLEntry (3, mSLS, mSLT, "Rates Claculation Stopped; Ports Invalid.") ;
  LEAVE MAIN;
END IF;
IF isBarrel THEN
SELECT brlofa,brltta INTO mBarrelOFRate,mBarrelTTRate 
FROM eci_elite_prod.ofrate WHERE trmnum=pooterminal AND prtnum=fdPort AND comcde=commNo LIMIT 1;
IF ISNULL(mBarrelOFRate) THEN 
SELECT brlofa,brltta INTO mBarrelOFRate,mBarrelTTRate 
FROM eci_elite_prod.ofrate WHERE trmnum=polterminal AND prtnum=podPort AND comcde=commNo LIMIT 1;
END IF ;
SELECT CASE 
WHEN mBarrelOFRate <> 0.00 AND mBarrelTTRate<>0.00 THEN 
 'OFBARR/TTBARR'
WHEN mBarrelOFRate <> 0.00 THEN 
'OFBARR' 
WHEN mBarrelTTRate <> 0.00 THEN 
'TTBARR'
END  
AS chargeCode,
mBarrelOFRate AS barrelOFRate,mBarrelTTRate AS barrelTTRate;
ELSE 
SELECT engcft,engwgt,metcft,metwgt,ofmin INTO cft_e,wgt_e,cft_m,wgt_m,ofRateMin
FROM eci_elite_prod.ofrate WHERE trmnum=pooterminal AND prtnum=fdPort AND comcde=commNo LIMIT 1;
SELECT engmet INTO marketvalue FROM ports WHERE unlocationcode = FdCode LIMIT 1;
IF marketvalue = 'E' THEN 
SET measure = cft_e;
SET weight = wgt_e;
ELSE IF  marketvalue = 'M' THEN  
SET measure = cft_m;
SET weight = wgt_m;
END IF ;
END IF ;
IF ofRateMin = 0.00 THEN
SELECT minchg INTO ofRateMin FROM eci_elite_prod.prtchg WHERE trmnum=pooterminal 
AND prtnum=fdPort AND commod=commNo AND chdcod=chargeCode LIMIT 1;
   IF ofRateMin = 0.00 THEN 
   SELECT minchg INTO ofRateMin FROM eci_elite_prod.prtchg WHERE trmnum=pooterminal 
   AND prtnum=fdPort AND commod=zeroCommodity AND chdcod=chargeCode LIMIT 1;
   END IF;
   IF ofRateMin = 0.00 THEN 
   SELECT minchg INTO ofRateMin FROM eci_elite_prod.prtchg WHERE trmnum=pooterminal 
   AND prtnum=podPort AND commod=commNo AND chdcod=chargeCode LIMIT 1;
   END IF;
   IF ofRateMin = 0.00 THEN 
   SELECT minchg INTO ofRateMin FROM eci_elite_prod.prtchg WHERE trmnum=pooterminal 
   AND prtnum=podPort AND commod=zeroCommodity AND chdcod=chargeCode LIMIT 1;
   END IF;
   IF ofRateMin = 0.00 THEN 
   SELECT minchg INTO ofRateMin FROM eci_elite_prod.prtchg WHERE trmnum=polterminal 
   AND prtnum=fdPort AND commod=commNo AND chdcod=chargeCode LIMIT 1;
   END IF;
   IF ofRateMin = 0.00 THEN 
   SELECT minchg INTO ofRateMin FROM eci_elite_prod.prtchg WHERE trmnum=polterminal 
   AND prtnum=fdPort AND commod=zeroCommodity AND chdcod=chargeCode LIMIT 1;
   END IF;
   IF ofRateMin = 0.00 THEN
   SELECT minchg INTO ofRateMin FROM eci_elite_prod.prtchg WHERE trmnum=polterminal 
   AND prtnum=podPort AND commod=commNo AND chdcod=chargeCode LIMIT 1;
   END IF;
   IF ofRateMin = 0.00 THEN 
   SELECT minchg INTO ofRateMin FROM eci_elite_prod.prtchg WHERE trmnum=polterminal 
   AND prtnum=podPort AND commod=zeroCommodity AND chdcod=chargeCode LIMIT 1;
   END IF;   
END IF ;
END IF ;
SELECT chargeCode,measure AS OfRateMeasure,weight AS OfRateWeight,ofRateMin;
END$$

DELIMITER ;

-- For Mantis # 13793 by sathiya On Aug 09 2016

DELIMITER $$

DROP PROCEDURE IF EXISTS `LCLScheduleListUpComing`$$

CREATE PROCEDURE `LCLScheduleListUpComing`(
  IN pPOO_ID INT (11),
  IN pPOL_ID INT (11),
  IN pPOD_ID INT (11),
  IN pFD_ID INT (11),
  IN pTransMode VARCHAR (50)
)
    READS SQL DATA
MAIN :
BEGIN
  DECLARE mSLS VARCHAR (64) DEFAULT "LCLScheduleListUpComing" ;
  DECLARE mSLH VARCHAR (256) DEFAULT "" ;
  DECLARE mSLT VARCHAR (128) DEFAULT UUID() ;
  DECLARE mErrMsg VARCHAR (1024) DEFAULT "" ;
  DECLARE mDEBUG BOOL DEFAULT FALSE ;
  DECLARE mTransMode VARCHAR (50) DEFAULT "" ;
  SET mSLH = CONCAT(": pPOO_ID (",pPOO_ID,"), pPOL_ID (",pPOL_ID,"), pPOD_ID (",pPOD_ID,")") ;
  SET mSLH = CONCAT(mSLH,", pFD_ID (",pFD_ID,"), pTransMode (",pTransMode,"): ") ;
  IF ISNULL(pPOO_ID) OR pPOO_ID < 1 THEN
  SET mErrMsg = CONCAT("VALIDATION",mSLH,"Stopped; pPOO_ID invalid.") ;
  CALL LogSQLEntry (3, mSLS, mSLT, mErrMsg) ;
  SELECT mErrMsg ;
  LEAVE MAIN ;
  END IF ;
  IF ISNULL(pPOL_ID) OR pPOL_ID < 1 THEN
  SET mErrMsg = CONCAT("VALIDATION",mSLH,"Stopped; pPOL_ID invalid.") ;
  CALL LogSQLEntry (3, mSLS, mSLT, mErrMsg) ;
  SELECT mErrMsg ;
  LEAVE MAIN ;
  END IF ;
  IF ISNULL(pPOD_ID) OR pPOD_ID < 1 THEN
   SET mErrMsg = CONCAT("VALIDATION",mSLH,"Stopped; pPOD_ID invalid.") ;
  CALL LogSQLEntry (3, mSLS, mSLT, mErrMsg) ;
  SELECT mErrMsg ;
  LEAVE MAIN ;
  END IF ;
  IF ISNULL(pFD_ID) OR pFD_ID < 1 THEN
   SET mErrMsg = CONCAT("VALIDATION",mSLH,"Stopped; pFD_ID invalid.") ;
  CALL LogSQLEntry (3, mSLS, mSLT, mErrMsg) ;
  SELECT mErrMsg ;
  LEAVE MAIN ;
  END IF ;
  IF ISNULL(pTransMode) OR TRIM(pTransMode) = "" OR pTransMode = "*" THEN
  SET mTransMode = "A,R,T,V" ;
  ELSE
  SET mTransMode = pTransMode ;
  END IF ;
  IF mDEBUG = TRUE
  THEN CALL LogSQLEntry (1,mSLS,mSLT,CONCAT("DEBUG", mSLH, "Begins.")) ;
  END IF ;
  SELECT
    lsh.id AS ssHeaderId,
    lsh.schedule_no AS voyageNo,
    UnLocationGetCodeByID (lsh.origin_id) AS pooUnlocCode,
    UnLocationGetCodeByID (lsh.destination_id) AS fdUnloCode,
    (
			SELECT
				acct_name
			FROM
				trading_partner
			WHERE acct_no = lsd.sp_acct_no
			LIMIT 1
		) AS carrierName,
    lsd.sp_acct_no AS carrierAcctNo,
    lsd.sp_reference_no AS ssVoyage,
    lsd.sp_reference_name AS vesselName,
    UnLocationGetCodeByID (lsd.departure_id) AS polUnlocCode,
    UnLocationGetCodeByID (lsd.arrival_id) AS podUnloCode,
    UnLocationGetNameStateCntryByID (lsd.departure_id) AS departPier,
    lsd.std AS sailingDate,
    lsd.sta AS etaPod,
    lsd.relay_lrd_override AS relaylrdOverride,
    lsd.relay_tt_override AS relayttOverride,
    ScheduleKByUnlocation(lsd.departure_id) AS scheduleK,
    UnlocationGetNameScheduleKById(lsd.departure_id) AS departSched
  FROM
    lcl_ss_header lsh
    JOIN lcl_ss_detail lsd
			ON (
        lsd.ss_header_id = lsh.id
        AND lsd.trans_mode =pTransMode
      )
  WHERE lsh.service_type IN ( 'E', 'C' )
    AND lsh.status <> 'V'
    AND
    (
			lsh.origin_id = pPOO_ID
			OR lsh.origin_id = pPOL_ID
		)
    AND
			(
				lsh.destination_id = pPOD_ID
				OR lsh.destination_id = pFD_ID
			)
    AND lsd.std > NOW()
    ORDER BY lsd.std ASC;
    IF mDEBUG = TRUE THEN
      CALL LogSQLEntry (1,mSLS,mSLT,"DEBUG: Ends normally.") ;
    END IF ;
END MAIN$$

DELIMITER ;

-- Mantis item#0012137 by pal raj on 9th Aug 2016

CREATE TABLE `kn_shipping_instruction` (
  `id` BIGINT (20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `sender_id` VARCHAR (11) NOT NULL,
  `receiver_id` VARCHAR (50) NOT NULL,
  `type` VARCHAR (40),
  `version` VARCHAR (8) NOT NULL,
  `envelope_id` VARCHAR (40) NOT NULL,
  `bkg_number` VARCHAR (20),
  `customer_control_code` VARCHAR (20),
  `communication_reference` VARCHAR (12),
  `customer_reference` VARCHAR (60),
  `customer_contact` VARCHAR (50),
  `customer_phone` VARCHAR (20),
  `customer_email` VARCHAR (50),
  `transport_type` VARCHAR (30),
  `obl_type` VARCHAR (30),
  `obl_number` VARCHAR (20),
  `number_of_originals` VARCHAR (30),
  `number_of_copies` VARCHAR (15),
  `fpi` VARCHAR (15),
  `place_of_issue` VARCHAR (120),
  `date_of_issue` DATE,
  `ff_customer_name` VARCHAR (120),
  `ff_address` TEXT,
  `sh_customer_name` VARCHAR (120),
  `sh_address` TEXT,
  `cn_customer_name` VARCHAR (120),
  `cn_address` TEXT,
  `ni_customer_name` VARCHAR (120),
  `ni_address` TEXT,
  `vessel_voyage_id` VARCHAR (20) NOT NULL,
  `vessel_name` VARCHAR (30) NOT NULL,
  `etd` DATE NOT NULL,
  `eta` DATE NOT NULL,
  `voyage` VARCHAR (30) NOT NULL,
  `place_of_receipt` VARCHAR (60),
  `cfs_origin` VARCHAR (15) NOT NULL,
  `port_of_loading` VARCHAR (15),
  `port_of_discharge` VARCHAR (15),
  `cfs_dtination` VARCHAR (15) NOT NULL,
  `place_of_delivery` VARCHAR (30),
  `cargo_id` VARCHAR (30),
  `level` VARCHAR (15),
  `package_count` INT (10),
  `packag_type` VARCHAR (25),
  `uom` ENUM ('M', 'E') NOT NULL,
  `weight` DECIMAL (10, 3) UNSIGNED,
  `volume` DECIMAL (10, 3) UNSIGNED,
  `commodity` TEXT,
  `marks` TEXT,
  `hazardous_flag` ENUM ('N', 'Y') NOT NULL,
  `obl_request_file_name` VARCHAR (40),
  `obl_request_file` BLOB,
  PRIMARY KEY (`id`));

ALTER TABLE mail_transactions CHANGE module_reference_id module_reference_id BIGINT(20) NULL;


--Mantis item 11589 (LCL-Imports) by priyanka on 10th aug 2016
DELIMITER $$



DROP FUNCTION IF EXISTS `doorDeliveryBuyAmount`$$

CREATE  FUNCTION `doorDeliveryBuyAmount`(fileNumberId BIGINT (20)) RETURNS DECIMAL(10,2)
    READS SQL DATA
    DETERMINISTIC
MAIN: BEGIN
    DECLARE mReturnData DECIMAL(10,2) DEFAULT 0.00;
    
 IF fileNumberId <> 0 THEN
     SELECT 
    IFNULL(SUM(lbc.`ap_amount`), 0) 
    INTO 
    mReturnData
  FROM
    `lcl_booking_ac` lbc 
    JOIN `gl_mapping` g 
      ON (g.`id` = lbc.`ap_gl_mapping_id`) 
  WHERE lbc.`file_number_id` = fileNumberId
    AND g.`Charge_code` = 'DOORDEL';
    END IF;
   
    RETURN mReturnData;
END MAIN$$

DELIMITER ;


DELIMITER $$

DROP FUNCTION IF EXISTS `doorDeliverySellAmount`$$

CREATE  FUNCTION `doorDeliverySellAmount`(fileNumberId BIGINT (20)) RETURNS DECIMAL(10,2)
    READS SQL DATA
    DETERMINISTIC
MAIN: BEGIN
    DECLARE mReturnData DECIMAL(10,2) DEFAULT 0.00;
    
 IF fileNumberId <> 0 THEN
     SELECT 
    IFNULL(SUM(lbc.`ar_amount`), 0) 
    INTO 
    mReturnData
  FROM
    `lcl_booking_ac` lbc 
    JOIN `gl_mapping` g 
      ON (g.`id` = lbc.`ap_gl_mapping_id`) 
  WHERE lbc.`file_number_id` = fileNumberId
    AND g.`Charge_code` = 'DOORDEL';
    END IF;
   
    RETURN mReturnData;
END MAIN$$

DELIMITER ;

DELIMITER $$


DROP FUNCTION IF EXISTS `doorDeliveryForCarrier`$$

CREATE FUNCTION `doorDeliveryForCarrier`( sacvalue  VARCHAR(100) ) RETURNS VARCHAR(100) CHARSET latin1
    READS SQL DATA
    DETERMINISTIC
MAIN: BEGIN
    DECLARE mReturnData VARCHAR(100) DEFAULT NULL;
    IF sacvalue IS NOT NULL THEN
    SELECT 
    carrier_name 
    INTO
    mReturnData
  FROM
    carriers_or_line 
  WHERE scac = sacvalue 
    AND carrier_name IS NOT NULL 
    AND carrier_name != '' 
  LIMIT 1;
  END IF;
RETURN mReturnData;
END MAIN$$

DELIMITER ;

DELIMITER $$

DROP FUNCTION IF EXISTS `GenericcodeDupForDoorDelivery`$$

CREATE  FUNCTION `GenericcodeDupForDoorDelivery`( codevalue  VARCHAR(100) ) RETURNS VARCHAR(100) CHARSET latin1
    READS SQL DATA
    DETERMINISTIC
MAIN: BEGIN
    DECLARE mReturnData VARCHAR(100) DEFAULT NULL;
    
    IF codevalue IS NOT NULL THEN
    SELECT 
    codedesc
    INTO 
    mReturnData 
  FROM
    `genericcode_dup` 
  WHERE codetypeid = 
    (SELECT 
      codetypeid 
    FROM
      codetype 
    WHERE description = 'door delivery status' 
    LIMIT 1) 
    AND CODE = codevalue
  LIMIT 1;
  END IF;
RETURN mReturnData;
END MAIN$$

DELIMITER ;


--Mantis item 13629 (LCL-Exports) by Shanmugam on 11th aug 2016
ALTER TABLE `lcl_ss_exports` ADD COLUMN `print_via_masterbl` BOOL DEFAULT 0 NOT NULL AFTER `export_agent_acct_no`;


--************ONLY FOR LOCAL USE,Executed already in all LW instances********************* ---- 11 Aug 2016 Mantis # 13245 by sathiya

ALTER TABLE `lcl_booking_export` 
  ADD COLUMN `include_dest_fees` BOOL DEFAULT FALSE NOT NULL AFTER `storage_datetime`;

--Mantis item 9262 (LCL-Exports) by Shanmugam on 11th aug 2016
ALTER TABLE `lcl_ss_masterbl` ADD COLUMN `converted_to_ap` BOOL DEFAULT 0 NOT NULL;


--Mantis item 0014077(FCL-EXPORTS) by priyanka on 17th aug 2016
ALTER TABLE `fcl_bl` ADD COLUMN `express_release` VARCHAR(2) NULL;
ALTER TABLE `fcl_bl` ADD COLUMN `express_release_comment` VARCHAR(200) NULL;
ALTER TABLE `fcl_bl` ADD COLUMN `express_released_on` DATE NULL;

ALTER TABLE `fcl_bl` ADD COLUMN `delivery_order` VARCHAR(2) NULL;
ALTER TABLE `fcl_bl` ADD COLUMN `delivery_order_comment` VARCHAR(200) NULL;
ALTER TABLE `fcl_bl` ADD COLUMN `delivery_order_on` DATE NULL;

ALTER TABLE `fcl_bl` ADD COLUMN `customs_clearance` VARCHAR(2) NULL;
ALTER TABLE `fcl_bl` ADD COLUMN `customs_clearance_comment` VARCHAR(200) NULL;
ALTER TABLE `fcl_bl` ADD COLUMN `customs_clearance_on` DATE NULL;

-- Mantis item 13845  by Aravindhan.V on 17th August 2016
 ALTER TABLE lcl_bl_piece MODIFY piece_desc TEXT DEFAULT NULL;

-- Mantis item 9482  by Shanmugam on 17th August 2016
 ALTER TABLE `lcl_booking` ADD COLUMN `terminate_desc` VARCHAR(200) NULL;

--  Mantis Item-14201(LCL-Exports) by Stefy on 22 August 2016 (This query only for Local and Econo)
ALTER TABLE `lcl_unit_ss` 
  ADD COLUMN `completed_datetime` DATETIME NULL AFTER `status`, 
  ADD INDEX `lcl_unit_ss_idx02` (`completed_datetime`);

--  Mantis Item-13893(LCL-Exports) by Stefy on 22 August 2016
ALTER TABLE `lcl_port_configuration` ADD COLUMN `default_trashipment_unloc` VARCHAR(5) NULL;

--  Mantis Item-14261(LCL-Exports) by Stefy on 22 August 2016
ALTER TABLE `role_duties` ADD COLUMN `bypass_relay_check` TINYINT(1) DEFAULT 0 NULL;

-- Mantis Item-13789(LCL-Exports) by Nambu on 22 August 2016
ALTER TABLE `role_duties` ADD COLUMN `default_load_released` TINYINT(1) DEFAULT 0  NULL AFTER `bypass_relay_check`;


--LCL Exports Mantis Item #14233 by sathya on 24 Aug 2016

ALTER TABLE `lcl_bl` ADD COLUMN `delivery_metro` ENUM('I','O','N') DEFAULT 'N' NULL AFTER `documentation`; 

-- Mantis Item-14237 (LCL-Exports) by Aravindhan.V on 24 August 2016
ALTER TABLE lcl_bl ADD COLUMN invoice_value DECIMAL(10,2) DEFAULT 0.00;
ALTER TABLE lcl_ss_masterbl ADD COLUMN invoice_value TINYINT(1) DEFAULT FALSE;

-- Mantis Item#0012137 (LCL-Exports) by PALRAJ on 24 August 2016

ALTER TABLE `kn_shipping_instruction` MODIFY `uom` ENUM('M','I');

--  Mantis Item-13893(LCL-Exports) by Stefy on 24 August 2016

ALTER TABLE lcl_port_configuration MODIFY COLUMN transhipment VARCHAR(100);

ALTER TABLE lcl_port_configuration MODIFY COLUMN default_port_of_discharge VARCHAR(100);


--- Mantis #14233 by sathiya on Aug 25 2016

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBl_update`$$

CREATE
    TRIGGER `after_LclBl_update` AFTER UPDATE ON `lcl_bl` 
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT;
    DECLARE fileStatus VARCHAR(2);
    DECLARE notiStatusFlag BOOLEAN DEFAULT FALSE;
    DECLARE bl_values TEXT ;
    DECLARE changesFlag BOOLEAN DEFAULT FALSE;
    SELECT lfn.status INTO fileStatus FROM lcl_file_number lfn WHERE lfn.id=OLD.file_number_id;
     IF ((old.ship_acct_no IS NULL AND new.ship_acct_no IS NOT NULL)OR new.ship_acct_no!=old.ship_acct_no OR(new.ship_acct_no IS NULL AND old.ship_acct_no IS NOT NULL))THEN
    SET @oldClientValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.ship_acct_no);
    SET @newClientValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.ship_acct_no);
    SET updated_values=(SELECT concat_string (updated_values,'Shipper',IFNULL(@oldClientValues,''),IFNULL(@newClientValues,'')));
    SET changesFlag = TRUE ;
    END IF;
     IF ((OLD.cons_acct_no IS NULL AND NEW.cons_acct_no IS NOT NULL) OR NEW.cons_acct_no != OLD.cons_acct_no OR (OLD.cons_acct_no IS NOT NULL AND NEW.cons_acct_no IS NULL)) THEN
     SET @oldConsValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.cons_acct_no);
     SET @newConsValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.cons_acct_no);
     SET updated_values=(SELECT concat_string(updated_values,'Consignee',IFNULL(@oldConsValues,''),IFNULL(@newConsValues,'')));
     SET changesFlag = TRUE ;
   END IF;
    IF ((OLD.noty_acct_no IS NULL AND NEW.noty_acct_no IS NOT NULL) OR NEW.noty_acct_no != OLD.noty_acct_no OR (OLD.noty_acct_no IS NOT NULL AND NEW.noty_acct_no IS NULL)) THEN
     SET @oldNotyValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.noty_acct_no);
     SET @newNotyValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.noty_acct_no);
     SET updated_values=(SELECT concat_string(updated_values,'Notify Party',IFNULL(@oldNotyValues,''),IFNULL(@newNotyValues,'')));
     SET changesFlag = TRUE ;
   END IF;
     IF ((old.fwd_acct_no IS NULL AND new.fwd_acct_no IS NOT NULL)OR new.fwd_acct_no!=old.fwd_acct_no OR(new.fwd_acct_no IS NULL AND old.fwd_acct_no IS NOT NULL))THEN
    SET @oldClientValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.fwd_acct_no);
    SET @newClientValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.fwd_acct_no);
    SET updated_values=(SELECT concat_string (updated_values,'Forwarder',IFNULL(@oldClientValues,''),IFNULL(@newClientValues,'')));
    SET changesFlag = TRUE ;
    END IF;
   IF ((OLD.third_party_acct_no IS NULL AND NEW.third_party_acct_no IS NOT NULL) OR NEW.third_party_acct_no != OLD.third_party_acct_no OR (OLD.third_party_acct_no IS NOT NULL AND NEW.third_party_acct_no IS NULL)) THEN
     SET @oldThirdValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.third_party_acct_no);
     SET @newThirdValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.third_party_acct_no);
     SET updated_values=(SELECT concat_string(updated_values,'Third Party',IFNULL(@oldThirdValues,''),IFNULL(@newThirdValues,'')));
     SET changesFlag = TRUE ;
   END IF;
     IF ((OLD.sup_acct_no IS NULL AND NEW.sup_acct_no IS NOT NULL) OR NEW.sup_acct_no!=OLD.sup_acct_no OR (OLD.sup_acct_no IS NOT NULL AND NEW.sup_acct_no IS NULL)) THEN
   SET @oldSupValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.sup_acct_no);
   SET @newSupValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.sup_acct_no);
   SET updated_values=(SELECT concat_string(updated_values,'Supplier',IFNULL(@oldSupValues,''),IFNULL(@newSupValues,'')));
   END IF;
   IF new.billing_terminal!=old.billing_terminal THEN
    SET @oldBillingTerminal=(SELECT CONCAT(terminal_location,'/',trmnum) FROM terminal WHERE trmnum=old.billing_terminal);
    SET @newBillingTerminal=(SELECT CONCAT(terminal_location,'/',trmnum) FROM terminal WHERE trmnum=new.billing_terminal);
    SET updated_values=(SELECT concat_string(updated_values,'Term to do BL',IFNULL(@oldBillingTerminal,''),IFNULL(@newBillingTerminal,'')));
   END IF;
      IF new.terms_type1!=old.terms_type1 THEN
       SET updated_values=(SELECT concat_string(updated_values,'Termstype1',old.terms_type1,new.terms_type1));
    END IF;
 
        IF new.terms_type2!=old.terms_type2 THEN
       SET updated_values=(SELECT concat_string(updated_values,'Termstype2',old.terms_type2,new.terms_type2));
    END IF;
         IF new.type2_date!=old.type2_date THEN
       SET updated_values=(SELECT concat_string(updated_values,'Type2Date',old.type2_date,new.type2_date));
    END IF;
         IF new.point_of_origin!=old.point_of_origin THEN
       SET updated_values=(SELECT concat_string(updated_values,'PointOfOrigin',old.point_of_origin,new.point_of_origin));
    END IF;
    IF new.bl_owner_id!=old.bl_owner_id THEN
     SET bl_values=(SELECT concat_string(updated_values,'BL Owner is Changed',(SELECT login_name FROM user_details WHERE user_id=old.bl_owner_id),(SELECT login_name FROM user_details WHERE user_id=new.bl_owner_id)));
       INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
       VALUES(OLD.file_number_id,'BL-AutoNotes',bl_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    IF old.poo_id!=new.poo_id THEN
	SET updated_values=(SELECT concat_string(updated_values,'PlaceOfReceipt',(SELECT un_loc_name FROM un_location WHERE id=old.poo_id),(SELECT un_loc_name FROM un_location WHERE id=new.poo_id)));
    END IF;
   IF old.fd_id!=new.fd_id THEN
	SET updated_values=(SELECT concat_string(updated_values,'Destination',(SELECT un_loc_name FROM un_location WHERE id=old.fd_id),(SELECT un_loc_name FROM un_location WHERE id=new.fd_id)));
	SET changesFlag = TRUE ;
    END IF;
          IF new.spot_rate!=old.spot_rate THEN
      SET updated_values=(SELECT concat_string(updated_values,'SpotRate',IF(old.spot_rate=0,'No','Yes'),IF(new.spot_rate=0,'No','Yes')));
    END IF;
    IF new.rate_type!=old.rate_type THEN
    SET updated_values=(SELECT concat_string(updated_values,'RateType',old.rate_type,new.rate_type));
    END IF;
    IF ((OLD.booked_ss_header_id IS NULL AND NEW.booked_ss_header_id IS NOT NULL) OR NEW.booked_ss_header_id!=OLD.booked_ss_header_id OR (OLD.booked_ss_header_id IS NOT NULL AND NEW.booked_ss_header_id IS NULL)) THEN
     SET @oldScheduleValue = (SELECT CONCAT (l.`schedule_no` ,' / ', DATE_FORMAT((SELECT d.std FROM lcl_ss_detail d WHERE d.ss_header_id=old.booked_ss_header_id  AND d.trans_mode='V'),'%d-%b-%Y'))  FROM lcl_ss_header  l  WHERE  l.id=old.booked_ss_header_id);
     SET @newScheduleValue = (SELECT CONCAT (l.`schedule_no` ,' / ', DATE_FORMAT((SELECT d.std FROM lcl_ss_detail d WHERE d.ss_header_id=new.booked_ss_header_id  AND d.trans_mode='V'),'%d-%b-%Y'))  FROM lcl_ss_header  l  WHERE  l.id=new.booked_ss_header_id);
     SET updated_values=(SELECT concat_string(updated_values,'Booked Voyage is changed',IFNULL(@oldScheduleValue,''),IFNULL(@newScheduleValue,'')) );
       SET changesFlag = TRUE ;
    END IF;
    IF new.billing_type!=old.billing_type THEN
    SET updated_values=(SELECT concat_string(updated_values,'Terms',old.billing_type,new.billing_type));
    SET changesFlag = TRUE ;
    END IF;
     IF new.billing_type!=old.billing_type THEN
      SET changesFlag = TRUE ;
    END IF;
     IF new.bill_to_party!=old.bill_to_party THEN
      SET changesFlag = TRUE ;
    END IF;
    IF new.free_bl!=old.free_bl THEN
    SET updated_values=(SELECT concat_string(updated_values,'Free B/L',IF(old.free_bl=0,'No','Yes'),IF(new.free_bl=0,'No','Yes')));
    SET changesFlag = TRUE ;
    END IF;
    IF new.insurance!=old.insurance THEN
    SET updated_values=(SELECT concat_string(updated_values,'Insurance',IF(old.insurance=0,'No','Yes'),IF(new.insurance=0,'No','Yes')));
    SET changesFlag = TRUE ;
    END IF;
    IF new.value_of_goods!=old.value_of_goods THEN
    SET updated_values=(SELECT concat_string(updated_values,'Value Of Goods',old.value_of_goods,new.value_of_goods));
    SET changesFlag = TRUE ;
    END IF;
    IF new.documentation!=old.documentation THEN
    SET updated_values=(SELECT concat_string(updated_values,'Documentation',IF(old.documentation=0,'No','Yes'),IF(new.documentation=0,'No','Yes')));
    SET changesFlag = TRUE ;
    END IF;
    IF new.delivery_metro!=old.delivery_metro THEN
       SET updated_values=(SELECT concat_string(updated_values,'Delivery Metro',old.delivery_metro,new.delivery_metro));
    END IF;
    IF updated_values IS NOT NULL THEN
    SET updated_values=CONCAT('UPDATED ->',updated_values);
       IF (fileStatus='M'  AND changesFlag = TRUE) THEN
       SELECT IF(COUNT(*)>0,TRUE,FALSE) INTO notiStatusFlag FROM lcl_export_notification len
       WHERE len.file_number_id=OLD.file_number_id AND len.file_status='Changes' AND len.status='Pending';
       IF (notiStatusFlag=FALSE) THEN
        INSERT INTO lcl_export_notification (file_number_id, file_status,STATUS,entered_datetime,entered_by_user_id)
        VALUES(OLD.file_number_id,'Changes','Pending',NOW(),new.modified_by_user_id);
        END IF;
        END IF;
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(OLD.file_number_id,'BL-AutoNotes',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;

-- > Mantis Item-12129 (LCL Exports) by Kuppusamy on 26 Aug 2016

ALTER TABLE `lcl_search_template`   
  ADD COLUMN `pn` TINYINT(1) DEFAULT 0  NOT NULL AFTER `tr`;

-----Mantis Item 13873 (LCL EXPORTS) by Rathnapandian on 26 Aug 2016

UPDATE `item_master` SET `item_desc`='LCL Search Imports' WHERE `unique_code`='LCLIS';

UPDATE `item_master` SET `item_desc`='LCL Search Exports' WHERE `unique_code`='LCLS';


-- > LCL Exports Mantis Item-12357 by Aravindhan.V on 30 Aug 2016

ALTER TABLE lcl_correction_charge 
ADD COLUMN rate_per_weight_unit DECIMAL(10,2) DEFAULT NULL,
ADD COLUMN rate_per_weight_unit_div DECIMAL(10,2) DEFAULT NULL,
ADD COLUMN rate_per_volume_unit DECIMAL(10,2) DEFAULT NULL,
ADD COLUMN rate_per_volume_unit_div DECIMAL(10,2) DEFAULT NULL,
ADD COLUMN minimum_rate DECIMAL(10,2) DEFAULT NULL,
ADD COLUMN rate_per_unit_uom ENUM('FL','PCT','V','W','FRW','FRV','FRM','M','MAX') NOT NULL,
ADD COLUMN print_on_bl TINYINT(1) DEFAULT 0;

--- LCL Exports Mantis # 14041 by sathiya on Aug 30 2016********Only for Local and Econo Server.Executed in all LW DB instances already*************

ALTER TABLE `lcl_bl` ADD COLUMN `insurance_cif` DECIMAL(10,2) UNSIGNED DEFAULT NULL AFTER `insurance`;
ALTER TABLE `lcl_booking` ADD COLUMN `insurance_cif` DECIMAL(10,2) UNSIGNED DEFAULT NULL AFTER `insurance`;

-- Mantis item 14289(LCL-exports) by priyanka on 31st august 2016

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_lcl_ss_masterbl_update`$$

CREATE
    
    TRIGGER `after_lcl_ss_masterbl_update` AFTER UPDATE ON `lcl_ss_masterbl` 
    FOR EACH ROW BEGIN
    
    DECLARE updated_values TEXT ;
    
     IF isNotEqual(OLD.sp_booking_no,NEW.sp_booking_no) THEN
     
     SET updated_values=(SELECT concat_string(updated_values,'Booking#',OLD.sp_booking_no,NEW.sp_booking_no));
     
     END IF;
     
      IF isNotEqual(OLD.ship_edi,NEW.ship_edi) THEN
     
     SET updated_values=(SELECT concat_string(updated_values,'Shipper Address',OLD.ship_edi,NEW.ship_edi));
     
     END IF;
     
     IF isNotEqual(OLD.export_ref_edi,NEW.export_ref_edi) THEN
     
     SET updated_values=(SELECT concat_string(updated_values,'Export Reference',OLD.export_ref_edi,NEW.export_ref_edi));
     
     END IF;
     
     IF isNotEqual(OLD.move_type,NEW.move_type) THEN
     
     SET updated_values=(SELECT concat_string(updated_values,'Move Type',OLD.move_type,NEW.move_type));
     
     END IF;
     
     IF isNotEqual(OLD.sp_contract_no,NEW.sp_contract_no) THEN
     
     SET updated_values=(SELECT concat_string(updated_values,'Contract#',OLD.sp_contract_no,NEW.sp_contract_no));
     
     END IF;
     
     IF isNotEqual(OLD.cons_edi,NEW.cons_edi) THEN
     
     SET updated_values=(SELECT concat_string(updated_values,'Consignee Address',OLD.cons_edi,NEW.cons_edi));
     
     END IF;
      
     
     IF isNotEqual(OLD.release_clause,NEW.release_clause) THEN
     
     SET updated_values=(SELECT concat_string(updated_values,'Release Clause',(SELECT CODE FROM genericcode_dup WHERE id=OLD.release_clause),(SELECT CODE FROM genericcode_dup WHERE id=NEW.release_clause)));
     
     END IF;
     
     IF isNotEqual(OLD.prepaid_collect,NEW.prepaid_collect) THEN
     
     SET updated_values=(SELECT concat_string(updated_values,'P/C',OLD.prepaid_collect,NEW.prepaid_collect));
     
     END IF;
     
     IF isNotEqual(OLD.noty_edi,NEW.noty_edi) THEN
     
     SET updated_values=(SELECT concat_string(updated_values,'Notify Address',OLD.noty_edi,NEW.noty_edi));
     
     END IF;
     
     IF isNotEqual(OLD.master_bl,NEW.master_bl) THEN
      
     SET  updated_values=(SELECT concat_string(updated_values,'MASTER BL#',OLD.master_bl,NEW.master_bl));
     
     END IF;
     
     IF isNotEqual(OLD.dest_prepaid_collect,NEW.dest_prepaid_collect) THEN
     
     SET  updated_values=(SELECT concat_string(updated_values,'Dest charges PPD/COL',OLD.dest_prepaid_collect,NEW.dest_prepaid_collect));
     
     END IF;
     
     IF isNotEqual(OLD.print_dock_recipt,NEW.print_dock_recipt) THEN
     
     SET  updated_values=(SELECT concat_string(updated_values,'Print Master with words Dock Receipt',OLD.print_dock_recipt,NEW.print_dock_recipt));
     
     END IF;
     
     IF isNotEqual(OLD.invoice_value,NEW.invoice_value) THEN
     
     SET  updated_values=(SELECT concat_string(updated_values,'Print Total of Invoice Values ',OLD.invoice_value,NEW.invoice_value));
     
     END IF;
      
     IF isNotEqual(OLD.remarks,NEW.remarks ) THEN
     
     SET updated_values=(SELECT concat_string(updated_values,'Special Instructions',OLD.remarks,NEW.remarks));
     
     END IF;
    
     IF updated_values IS NOT NULL THEN 
     SET updated_values=CONCAT('UPDATED ->',updated_values);
       INSERT INTO lcl_ss_remarks(ss_header_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
       VALUES(OLD.ss_header_id,'auto',updated_values,NOW(),new.entered_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
     
    END;
$$

DELIMITER ;

---LCL Exports Mantis # 14041 by sathiya on Aug 31

ALTER TABLE `lcl_quote` ADD COLUMN `insurance_cif` DECIMAL(10,2) UNSIGNED DEFAULT NULL AFTER `insurance`;

-- Mantis item 14289(LCL-exports) by priyanka on 31st august 2016

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_lclUnitSs_update`$$

CREATE
    
    TRIGGER `after_lclUnitSs_update` AFTER UPDATE ON `lcl_unit_ss` 
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT ;
    DECLARE checkMasterFlag BOOLEAN DEFAULT FALSE;
    IF new.status!=old.status THEN
IF new.status='C' THEN
SET updated_values=(SELECT concat_string(updated_values,'Load Completed',old.status,new.status));
        END IF;
        IF new.status='E' THEN
SET updated_values=(SELECT concat_string(updated_values,'Un Load Completed',old.status,new.status));
        END IF;
    END IF;
     IF new.cob!=old.cob THEN
       SET updated_values=(SELECT concat_string(updated_values,'Changed COB',IF(old.cob=0,'No','Yes'),IF(new.cob=0,'No','Yes')));
    END IF;
    IF new.stop_off!=old.stop_off THEN
       SET updated_values=(SELECT concat_string(updated_values,'Stop Off',IF(old.stop_off=0,'No','Yes'),IF(new.stop_off=0,'No','Yes')));
    END IF;
    IF new.intermodal_provided!=old.intermodal_provided THEN
       SET updated_values=(SELECT concat_string(updated_values,'InterModal Provided',IF(old.intermodal_provided=0,'No','Yes'),IF(new.intermodal_provided=0,'No','Yes')));
    END IF;
    IF new.drayage_provided!=old.drayage_provided THEN
       SET updated_values=(SELECT concat_string(updated_values,'Drayage Provided',IF(old.drayage_provided=0,'No','Yes'),IF(new.drayage_provided=0,'No','Yes')));
    END IF;
    IF ((OLD.su_heading_note IS NULL AND NEW.su_heading_note IS NOT NULL) OR NEW.su_heading_note != OLD.su_heading_note OR (OLD.su_heading_note IS NOT NULL AND NEW.su_heading_note IS NULL)) THEN
    SET updated_values=(SELECT concat_string(updated_values,'SU Heading Note',IFNULL(old.su_heading_note,''),IFNULL(new.su_heading_note,'')));
    END IF;
    IF ((OLD.trucking_remarks IS NULL AND NEW.trucking_remarks IS NOT NULL) OR NEW.trucking_remarks != OLD.trucking_remarks OR (OLD.trucking_remarks IS NOT NULL AND NEW.trucking_remarks IS NULL)) THEN
    SET updated_values=(SELECT concat_string(updated_values,'Trucking Information',IFNULL(old.trucking_remarks,''),IFNULL(new.trucking_remarks,'')));
    END IF;
    IF OLD.prepaid_collect!=NEW.prepaid_collect THEN
SET updated_values=(SELECT concat_string(updated_values,'Prepaid/Collect',old.prepaid_collect,new.prepaid_collect));
    END IF;
    IF ((OLD.bl_body IS NULL AND NEW.bl_body IS NOT NULL) OR (OLD.bl_body IS NOT NULL AND NEW.bl_body IS NULL) OR OLD.bl_body!=NEW.bl_body) THEN
    SET updated_values=(SELECT concat_string(updated_values,'BL Body',IFNULL(old.bl_body,''),IFNULL(new.bl_body,'')));
    SET checkMasterFlag = TRUE;
    END IF;
    IF OLD.volume_metric!=NEW.volume_metric THEN
    SET updated_values=(SELECT concat_string(updated_values,'CBM',old.volume_metric,new.volume_metric));
    SET checkMasterFlag = TRUE;    
    END IF;
    IF OLD.weight_metric!=NEW.weight_metric THEN
    SET updated_values=(SELECT concat_string(updated_values,'KGS',old.weight_metric,new.weight_metric));
    SET checkMasterFlag = TRUE;    
    END IF;
    IF OLD.volume_imperial!=NEW.volume_imperial THEN
    SET updated_values=(SELECT concat_string(updated_values,'CFT',old.volume_imperial,new.volume_imperial));
    SET checkMasterFlag = TRUE;    
    END IF;
    IF OLD.weight_imperial!=NEW.weight_imperial THEN
    SET updated_values=(SELECT concat_string(updated_values,'LBS',old.weight_imperial,new.weight_imperial));
    SET checkMasterFlag = TRUE;    
    END IF;
    IF OLD.total_pieces!=NEW.total_pieces THEN
    SET updated_values=(SELECT concat_string(updated_values,'Pieces',old.total_pieces,new.total_pieces));
    SET checkMasterFlag = TRUE;
    END IF;
   SET @unit_id=(SELECT unit_id FROM lcl_unit_ss WHERE id=old.id);
   SET @ss_header=(SELECT ss_header_id FROM lcl_unit_ss WHERE id=old.id );
    IF updated_values IS NOT NULL THEN
    IF checkMasterFlag = TRUE THEN
    SET updated_values=CONCAT('Updated Show On Master',updated_values);
    END IF;
        INSERT INTO lcl_unit_ss_remarks(unit_id,ss_header_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(@unit_id,@ss_header,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
      END;
$$

DELIMITER ;
 
-- > LCL Exports Mantis Item-13609 by Aravindhan.V on 1 sep 2016

INSERT INTO print_config(screen_name,document_type,document_name,allow_print) 
VALUES('LCLBL','IN','Freight Invoice','No');

--Mantis item 0014569(LCL_EXPORTS) by priyanka on 7th sep 2016
ALTER TABLE `role_duties`  ADD COLUMN `lcl_quote_client` TINYINT(1) DEFAULT '0' NULL ;

-- Mantis item 14289 (LCL-EXPORTS) by priyanka on 7th sep 2016

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_lclUnitSs_insert_trigger`$$

CREATE
   
    TRIGGER `after_lclUnitSs_insert_trigger` AFTER INSERT ON `lcl_unit_ss` 
    FOR EACH ROW BEGIN
      DECLARE insert_values TEXT ;
      DECLARE checkBookingFlag BOOLEAN DEFAULT FALSE;
    IF new.drayage_provided!='' THEN
       SET insert_values=(SELECT concat_insert_values(insert_values,'Drayage Provided',new.drayage_provided));
    END IF;
    IF new.intermodal_provided!='' THEN
       SET insert_values=(SELECT concat_insert_values(insert_values,'Intermodal Provided',new.intermodal_provided));
    END IF;
    IF new.stop_off!='' THEN
       SET insert_values=(SELECT concat_insert_values(insert_values,'Stop Off',new.stop_off));
    END IF;
    IF new.su_heading_note!='' THEN
       SET insert_values=(SELECT concat_insert_values(insert_values,'SU Heading Note',new.su_heading_note));
    END IF;
    IF new.prepaid_collect!='' THEN
       SET insert_values=(SELECT concat_insert_values(insert_values,'Prepaid/Collect',new.prepaid_collect));
    END IF;
    IF new.trucking_remarks!='' THEN
       SET insert_values=(SELECT concat_insert_values(insert_values,'Trucking Remarks',new.trucking_remarks));
    END IF;
    IF new.sp_booking_no!='' THEN
    SET insert_values=(SELECT concat_insert_values(insert_values,'Linked to Master',new.sp_booking_no));
    END IF;
    IF insert_values IS NOT NULL THEN
        SET insert_values=CONCAT('INSERTED ->',insert_values);
        INSERT INTO lcl_unit_ss_remarks(unit_id,ss_header_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(new.unit_id,new.ss_header_id,'auto',insert_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;        
    END;
$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_lcl_ss_masterbl_update`$$

CREATE
    
    TRIGGER `after_lcl_ss_masterbl_update` AFTER UPDATE ON `lcl_ss_masterbl` 
    FOR EACH ROW BEGIN
    
    DECLARE updated_values TEXT ;
    
     IF isNotEqual(OLD.sp_booking_no,NEW.sp_booking_no) THEN
     
     SET updated_values=(SELECT concat_string(updated_values,'Booking#',OLD.sp_booking_no,NEW.sp_booking_no));
     
     END IF;
     
      IF isNotEqual(OLD.ship_edi,NEW.ship_edi) THEN
     
     SET updated_values=(SELECT concat_string(updated_values,'Shipper Address',OLD.ship_edi,NEW.ship_edi));
     
     END IF;
     
     IF isNotEqual(OLD.export_ref_edi,NEW.export_ref_edi) THEN
     
     SET updated_values=(SELECT concat_string(updated_values,'Export Reference',OLD.export_ref_edi,NEW.export_ref_edi));
     
     END IF;
     
     IF isNotEqual(OLD.move_type,NEW.move_type) THEN
     
     SET updated_values=(SELECT concat_string(updated_values,'Move Type',OLD.move_type,NEW.move_type));
     
     END IF;
     
     IF isNotEqual(OLD.sp_contract_no,NEW.sp_contract_no) THEN
     
     SET updated_values=(SELECT concat_string(updated_values,'Contract#',OLD.sp_contract_no,NEW.sp_contract_no));
     
     END IF;
     
     IF isNotEqual(OLD.cons_edi,NEW.cons_edi) THEN
     
     SET updated_values=(SELECT concat_string(updated_values,'Consignee Address',OLD.cons_edi,NEW.cons_edi));
     
     END IF;
      
     
     IF isNotEqual(OLD.release_clause,NEW.release_clause) THEN
     
     SET updated_values=(SELECT concat_string(updated_values,'Release Clause',(SELECT CODE FROM genericcode_dup WHERE id=OLD.release_clause),(SELECT CODE FROM genericcode_dup WHERE id=NEW.release_clause)));
     
     END IF;
     
     IF isNotEqual(OLD.prepaid_collect,NEW.prepaid_collect) THEN
     
     SET updated_values=(SELECT concat_string(updated_values,'P/C',OLD.prepaid_collect,NEW.prepaid_collect));
     
     END IF;
     
     IF isNotEqual(OLD.noty_edi,NEW.noty_edi) THEN
     
     SET updated_values=(SELECT concat_string(updated_values,'Notify Address',OLD.noty_edi,NEW.noty_edi));
     
     END IF;
     
     IF isNotEqual(OLD.master_bl,NEW.master_bl) THEN
      
     SET  updated_values=(SELECT concat_string(updated_values,'MASTER BL#',OLD.master_bl,NEW.master_bl));
     
     END IF;
     
     IF isNotEqual(OLD.dest_prepaid_collect,NEW.dest_prepaid_collect) THEN
     
     SET  updated_values=(SELECT concat_string(updated_values,'Dest charges PPD/COL',OLD.dest_prepaid_collect,NEW.dest_prepaid_collect));
     
     END IF;
     
     IF isNotEqual(OLD.print_dock_recipt,NEW.print_dock_recipt) THEN
     
     SET  updated_values=(SELECT concat_string(updated_values,'Print Master with words Dock Receipt',OLD.print_dock_recipt,NEW.print_dock_recipt));
     
     END IF;
     
     IF isNotEqual(OLD.invoice_value,NEW.invoice_value) THEN
     
     SET  updated_values=(SELECT concat_string(updated_values,'Print Total of Invoice Values ',OLD.invoice_value,NEW.invoice_value));
     
     END IF;
      
     IF isNotEqual(OLD.remarks,NEW.remarks ) THEN
     
     SET updated_values=(SELECT concat_string(updated_values,'Special Instructions',OLD.remarks,NEW.remarks));
     
     END IF;
    
     IF updated_values IS NOT NULL THEN 
     SET updated_values=CONCAT('UPDATED ->',updated_values);
       INSERT INTO lcl_ss_remarks(ss_header_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
       VALUES(OLD.ss_header_id,'auto',updated_values,NOW(),new.entered_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
     
    END;
$$

DELIMITER ;

--Mantis item 14597 (LCL-Exports) by RathnaPandian on 8th sep 2016

ALTER TABLE lcl_port_configuration
ADD COLUMN print_invoice_value TINYINT(1) DEFAULT 0;

--Mantis item 9418 (LCL-Exports) by RathnaPandian on 12th sep 2016

INSERT INTO property (id ,NAME,VALUE,TYPE,description)VALUES (237,'CTS carriers use lowest rates',2,'LCL','CTS carriers use lowest rates');

-- Mantis item 13813 (LCL-Exports) by priyanka on 13th sep 2016
ALTER TABLE `role_duties` ADD COLUMN `pick_dr_warnings` TINYINT(1) DEFAULT 1 NULL ; 

-- Mantis item 13865 (LCL-Exports) by Aravindhan.V on 13th sep 2016

CREATE TABLE `template_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `template_id` int(11) NOT NULL,
  `template_key` varchar(50) NOT NULL,
  `sorted_order` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `export_template_fk1` (`template_id`),
  CONSTRAINT `export_template_fk1` FOREIGN KEY (`template_id`) REFERENCES `lcl_search_template` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8;

-- Mantis item 0014213 by pal raj on 14th Sep 2016

DELIMITER $$

DROP PROCEDURE IF EXISTS `delete_notes_for_commodity`$$

CREATE PROCEDURE `delete_notes_for_commodity`(IN fileId BIGINT, userId INT)
BEGIN
    DECLARE delete_values TEXT;
    
    DECLARE remarksType VARCHAR(25);
    
    SELECT state INTO @state FROM `lcl_file_number` WHERE id=fileId LIMIT 1;
    
    CASE @state
    
    WHEN 'Q' THEN
    
    SET remarksType='QT-AutoNotes';
    
    SELECT 
    
    commodity_type_id,
    packaging_type_id,
    booked_piece_count,
    booked_weight_imperial,
    booked_weight_metric,
    booked_volume_imperial,
    booked_volume_metric,
    actual_piece_count,
    actual_weight_imperial,
    actual_weight_metric,
    actual_volume_imperial,
    actual_volume_metric,
    hazmat,
    piece_desc,
    mark_no_desc 
    
    INTO 
    
    @commodityTypeId,
    @packageTypeId,
    @bPieceCount,
    @bWeightImperial,
    @bWeightMetric,
    @bVolumeImperial,
    @bVolumeMetric,
    @aPieceCount,
    @aWeightImperial,
    @aWeightMetric,
    @aVolumeImperial,
    @aVolumeMetric,
    @hazmat,
    @pieceDesc,
    @markNoDesc FROM lcl_quote_piece WHERE file_number_id=fileId LIMIT 1;
    
    WHEN 'B' THEN
    
    SET remarksType='DR-AutoNotes';
    
    SELECT 
    
    commodity_type_id,
    booked_package_type_id,
    booked_piece_count,
    booked_weight_imperial,
    booked_weight_metric,
    booked_volume_imperial,
    booked_volume_metric,
    actual_piece_count,
    actual_weight_imperial,
    actual_weight_metric,
    actual_volume_imperial,
    actual_volume_metric,
    hazmat,
    piece_desc,
    mark_no_desc 
    
    INTO 
    
    @commodityTypeId,
    @packageTypeId,
    @bPieceCount,
    @bWeightImperial,
    @bWeightMetric,
    @bVolumeImperial,
    @bVolumeMetric,
    @aPieceCount,
    @aWeightImperial,
    @aWeightMetric,
    @aVolumeImperial,
    @aVolumeMetric,
    @hazmat,
    @pieceDesc,
    @markNoDesc FROM lcl_booking_piece WHERE file_number_id=fileId LIMIT 1 ;
    
    ELSE
    
    SET remarksType='BL-AutoNotes';
    
    SELECT 
    
    commodity_type_id,
    packaging_type_id,
    booked_piece_count,
    booked_weight_imperial,
    booked_weight_metric,
    booked_volume_imperial,
    booked_volume_metric,
    actual_piece_count,
    actual_weight_imperial,
    actual_weight_metric,
    actual_volume_imperial,
    actual_volume_metric,
    hazmat,
    piece_desc,
    mark_no_desc 
    
    INTO 
    
    @commodityTypeId,
    @packageTypeId,
    @bPieceCount,
    @bWeightImperial,
    @bWeightMetric,
    @bVolumeImperial,
    @bVolumeMetric,
    @aPieceCount,
    @aWeightImperial,
    @aWeightMetric,
    @aVolumeImperial,
    @aVolumeMetric,
    @hazmat,
    @pieceDesc,
    @markNoDesc FROM lcl_bl_piece WHERE file_number_id=fileId LIMIT 1;
    
    END CASE;
    
    
    IF @commodityTypeId IS NOT NULL THEN
    
    SET delete_values =(SELECT concat_deleted_values(delete_values,'Commodity',(SELECT  CONCAT(desc_en,' (',CODE,')') FROM commodity_type WHERE id=@commodityTypeId LIMIT 1),'',''));
    
    END IF;
    
    IF @packageTypeId IS NOT NULL THEN
    
    SET delete_values =(SELECT concat_deleted_values(delete_values,'Package',(SELECT description FROM package_type WHERE id=@packageTypeId LIMIT 1),'',''));
    
    END IF;
    
    IF @bPieceCount IS NOT NULL THEN
    
    SET delete_values =(SELECT concat_deleted_values(delete_values,'Piece Count',@bPieceCount,'',''));
    
    END IF;
    
    IF @bWeightImperial IS NOT NULL THEN
    
    SET delete_values =(SELECT concat_deleted_values(delete_values,'Weight  LBS',@bWeightImperial,'',''));
    
    END IF;
    
    IF @bWeightMetric IS NOT NULL THEN
    
    SET delete_values =(SELECT concat_deleted_values(delete_values,'Weight  KGS',@bWeightMetric,'',''));
    
    END IF;
    
    IF @bVolumeImperial IS NOT NULL THEN
    
    SET delete_values =(SELECT concat_deleted_values(delete_values,'Volume  CFT',@bVolumeImperial,'',''));
    
    END IF;
    
    IF @bVolumeMetric IS NOT NULL THEN
    
    SET delete_values =(SELECT concat_deleted_values(delete_values,'Volume  CBM',@bVolumeMetric,'',''));
    
    END IF;
    
    IF @aPieceCount IS NOT NULL THEN
    
    SET delete_values =(SELECT concat_deleted_values(delete_values,'Actual Piece Count',@aPieceCount,'',''));
    
    END IF;
    
    IF @aWeightImperial IS NOT NULL THEN
    
    SET delete_values =(SELECT concat_deleted_values(delete_values,'Actual Weight LBS',@aWeightImperial,'',''));
    
    END IF;
    
    IF @aWeightMetric IS NOT NULL THEN
    
    SET delete_values =(SELECT concat_deleted_values(delete_values,'Actual Weight KGS',@aWeightMetric,'',''));
    
    END IF;
    
    IF @aVolumeImperial IS NOT NULL THEN
    
    SET delete_values =(SELECT concat_deleted_values(delete_values,'Actual Volume CFT',@aVolumeImperial,'',''));
    
    END IF;
    
    IF @aVolumeMetric IS NOT NULL THEN
    
    SET delete_values =(SELECT concat_deleted_values(delete_values,'Actual Volume CBM',@aVolumeMetric,'',''));
    
    END IF;
    
    IF @hazmat IS NOT NULL THEN
    
    SET delete_values =(SELECT concat_deleted_values(delete_values,'HaZmat',IF(@hazmat=0,'No','Yes'),'',''));
    
    END IF;
    
    IF @pieceDesc IS NOT NULL THEN
    
    SET delete_values =(SELECT concat_deleted_values(delete_values,'Commodity Desc',@pieceDesc,'',''));
    
    END IF;
    
    IF @markNoDesc IS NOT NULL THEN
    
    SET delete_values =(SELECT concat_deleted_values(delete_values,'Marks and Numbers',@markNoDesc,'',''));
    
    END IF;
    
    IF delete_values <> '' THEN 
    
    SET delete_values=CONCAT('Deleted ->',delete_values);
    
    INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
		VALUES(fileId,remarksType,delete_values,NOW(),userId,NOW(),userId);
    END IF;
    
    END$$

DELIMITER ;

-- Mantis item 13865 (LCL-Exports) by Aravindhan.V on 14th sep 2016

ALTER TABLE user_details ADD COLUMN user_default_template INT(11) DEFAULT NULL; 


--Mantis item 14601 (LCL-Exports) by RathnaPandian on 14th sep 2016

ALTER TABLE lcl_port_configuration
ADD COLUMN lock_port TINYINT(1) DEFAULT 0;

--Mantis item 14289(LCL-Exports) by priyanka on 16th sep 2016
DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_lclUnitSs_update`$$

CREATE
   
    TRIGGER `after_lclUnitSs_update` AFTER UPDATE ON `lcl_unit_ss` 
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT ;
    DECLARE checkMasterFlag BOOLEAN DEFAULT FALSE;
    DECLARE checkBookingFlag BOOLEAN DEFAULT FALSE;
    DECLARE updated_values_master TEXT;
    IF new.status!=old.status THEN
IF new.status='C' THEN
SET updated_values=(SELECT concat_string(updated_values,'Load Completed',old.status,new.status));
        END IF;
        IF new.status='E' THEN
SET updated_values=(SELECT concat_string(updated_values,'Un Load Completed',old.status,new.status));
        END IF;
    END IF;
     IF new.cob!=old.cob THEN
       SET updated_values=(SELECT concat_string(updated_values,'Changed COB',IF(old.cob=0,'No','Yes'),IF(new.cob=0,'No','Yes')));
    END IF;
    IF new.stop_off!=old.stop_off THEN
       SET updated_values=(SELECT concat_string(updated_values,'Stop Off',IF(old.stop_off=0,'No','Yes'),IF(new.stop_off=0,'No','Yes')));
    END IF;
    IF new.intermodal_provided!=old.intermodal_provided THEN
       SET updated_values=(SELECT concat_string(updated_values,'InterModal Provided',IF(old.intermodal_provided=0,'No','Yes'),IF(new.intermodal_provided=0,'No','Yes')));
    END IF;
    IF new.drayage_provided!=old.drayage_provided THEN
       SET updated_values=(SELECT concat_string(updated_values,'Drayage Provided',IF(old.drayage_provided=0,'No','Yes'),IF(new.drayage_provided=0,'No','Yes')));
    END IF;
    IF ((OLD.su_heading_note IS NULL AND NEW.su_heading_note IS NOT NULL) OR NEW.su_heading_note != OLD.su_heading_note OR (OLD.su_heading_note IS NOT NULL AND NEW.su_heading_note IS NULL)) THEN
    SET updated_values=(SELECT concat_string(updated_values,'SU Heading Note',IFNULL(old.su_heading_note,''),IFNULL(new.su_heading_note,'')));
    END IF;
    IF ((OLD.trucking_remarks IS NULL AND NEW.trucking_remarks IS NOT NULL) OR NEW.trucking_remarks != OLD.trucking_remarks OR (OLD.trucking_remarks IS NOT NULL AND NEW.trucking_remarks IS NULL)) THEN
    SET updated_values=(SELECT concat_string(updated_values,'Trucking Information',IFNULL(old.trucking_remarks,''),IFNULL(new.trucking_remarks,'')));
    END IF;
    IF OLD.prepaid_collect!=NEW.prepaid_collect THEN
    SET updated_values=(SELECT concat_string(updated_values,'Prepaid/Collect',old.prepaid_collect,new.prepaid_collect));
    END IF;
    
    IF ((OLD.sp_booking_no IS NULL AND NEW.sp_booking_no IS NOT NULL) OR (OLD.sp_booking_no IS NOT NULL AND NEW.sp_booking_no IS NULL) OR 
    (OLD.sp_booking_no!=NEW.sp_booking_no)) THEN
    SET checkBookingFlag = TRUE;
    SET updated_values= (SELECT concat_string(updated_values,'Linked to Master', old.sp_booking_no,new.sp_booking_no));
    END IF;
    
    IF ((OLD.bl_body IS NULL AND NEW.bl_body IS NOT NULL) OR (OLD.bl_body IS NOT NULL AND NEW.bl_body IS NULL) OR OLD.bl_body!=NEW.bl_body) THEN
    SET updated_values=(SELECT concat_string(updated_values,'BL Body',IFNULL(old.bl_body,''),IFNULL(new.bl_body,'')));
    SET checkMasterFlag = TRUE;
    END IF;
    
    IF ((OLD.volume_metric IS NULL AND NEW.volume_metric IS NOT NULL) OR (OLD.volume_metric IS NOT NULL AND NEW.volume_metric IS NULL)
    OR OLD.volume_metric!=NEW.volume_metric) THEN
    SET updated_values=(SELECT concat_string(updated_values,'CBM',IFNULL(old.volume_metric,''),IFNULL(new.volume_metric,'')));
    SET checkMasterFlag = TRUE;    
    END IF;
    
    IF ((OLD.weight_metric IS NULL AND NEW.weight_metric IS NOT NULL) OR (OLD.weight_metric IS NOT NULL AND NEW.weight_metric IS NULL)
    OR OLD.weight_metric!=NEW.weight_metric)THEN
    SET updated_values=(SELECT concat_string(updated_values,'KGS',IFNULL(old.weight_metric,''),IFNULL(new.weight_metric,'')));
    SET checkMasterFlag = TRUE;    
    END IF;
    
    IF ((OLD.volume_imperial IS NULL AND NEW.volume_imperial IS NOT NULL) OR (OLD.volume_imperial IS NOT NULL AND NEW.volume_imperial IS NULL)
    OR OLD.volume_imperial!=NEW.volume_imperial) THEN
    SET updated_values=(SELECT concat_string(updated_values,'CFT',IFNULL(old.volume_imperial,''),IFNULL(new.volume_imperial,'')));
    SET checkMasterFlag = TRUE;    
    END IF;
    
    IF ((OLD.weight_imperial IS NULL AND NEW.weight_imperial IS NOT NULL) OR (OLD.weight_imperial IS NOT NULL AND NEW.weight_imperial IS NULL)
    OR OLD.weight_imperial!=NEW.weight_imperial) THEN
    SET updated_values=(SELECT concat_string(updated_values,'LBS',IFNULL(old.weight_imperial,''),IFNULL(new.weight_imperial,'')));
    SET checkMasterFlag = TRUE;    
    END IF;
    
    IF ((OLD.total_pieces IS NULL AND NEW.total_pieces IS NOT NULL) OR (OLD.total_pieces IS NOT NULL AND NEW.total_pieces IS NULL)
    OR OLD.total_pieces!=NEW.total_pieces) THEN
    SET updated_values=(SELECT concat_string(updated_values,'Pieces',IFNULL(old.total_pieces,''),IFNULL(new.total_pieces,'')));
    SET checkMasterFlag = TRUE;
    END IF;
    
    SET @unit_id=(SELECT unit_id FROM lcl_unit_ss WHERE id=old.id);
    SET @ss_header=(SELECT ss_header_id FROM lcl_unit_ss WHERE id=old.id );
    SET @unlinkedunit_no=(SELECT unit_no FROM lcl_unit WHERE id=@unit_id);
    
    SET @masterbl_id= (SELECT id FROM lcl_ss_masterbl WHERE sp_booking_no= 
    (SELECT sp_booking_no FROM lcl_unit_ss WHERE ss_header_id=@ss_header AND unit_id=@unit_id ) LIMIT 1);
     
    SET @unit_no=(SELECT GROUP_CONCAT(unit_no) FROM lcl_unit lu JOIN lcl_unit_ss lus ON lus.unit_id=lu.id JOIN lcl_ss_header lsh ON lsh.id= lus.ss_header_id
    JOIN lcl_ss_masterbl lsm ON lsm.ss_header_id=lsh.id AND lsm.sp_booking_no=lus.sp_booking_no WHERE lsm.ss_header_id=@ss_header AND lsm.id=@masterbl_id 
    AND lus.id=old.id AND lus.unit_id=@unit_id);
        
    
    IF (@masterbl_id!='')  THEN
    SET updated_values_master=CONCAT('Linked to Unit->',@unit_no);   
    ELSE
    SET updated_values_master=CONCAT('UnLinked to Unit->',@unlinkedunit_no);    
    END IF; 
    
    IF checkMasterFlag = TRUE THEN
    SET updated_values=CONCAT('Updated Show On Master',updated_values);
    END IF;    
    
    IF updated_values IS NOT NULL THEN
    SET updated_values=CONCAT('UPDATED ->',updated_values);
        INSERT INTO lcl_unit_ss_remarks(unit_id,ss_header_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(@unit_id,@ss_header,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    
    IF (checkBookingFlag = TRUE) AND updated_values_master IS NOT NULL THEN
    INSERT INTO lcl_ss_remarks(ss_header_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
    VALUES(@ss_header,'auto',updated_values_master,NOW(),new.entered_by_user_id,NOW(),new.modified_by_user_id);
    END IF;    
    
    END IF;
      END;
$$

DELIMITER ;


-- Mantis item 14913 (LCL-Exports) by Nambu on 20th sep 2016 Only for Local & Econo

DROP FUNCTION currentLocaion_Disp;

-- Mantis Item# 41965 (Lcl Exports) By Aravindhan.V on 22/9/2016 (Only For Local Machine)

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `lcl_booking_ac_insert_trigger`$$

CREATE
    TRIGGER `lcl_booking_ac_insert_trigger` AFTER INSERT ON `lcl_booking_ac` 
    FOR EACH ROW BEGIN
  DECLARE insert_values TEXT DEFAULT '';
  DECLARE changesFlag BOOLEAN DEFAULT FALSE;
  DECLARE mFileNumberId BIGINT(20);
  DECLARE mShipmentType VARCHAR(4);
  DECLARE mBlNo VARCHAR(30);
  DECLARE mDrNo VARCHAR(7);
  DECLARE mBookingNo VARCHAR(20);
  DECLARE mVoyageNo VARCHAR(30);
  DECLARE mContainerNo VARCHAR(30);
  DECLARE mEta DATETIME;
  DECLARE mSsline VARCHAR(50);
  DECLARE mOrigin VARCHAR(100);
  DECLARE mDestination VARCHAR(100);
  DECLARE mShipperName VARCHAR(50);
  DECLARE mShipperNo VARCHAR(10);
  DECLARE mConsigneeName VARCHAR(50);
  DECLARE mConsigneeNo VARCHAR(10);
  DECLARE mForwarderName VARCHAR(50);
  DECLARE mForwarderNo VARCHAR(10);
  DECLARE mThirdPartyName VARCHAR(50);
  DECLARE mThirdPartyNo VARCHAR(10);
  DECLARE mAgentName VARCHAR(50);
  DECLARE mAgentNo VARCHAR(10);
  DECLARE mTerminal VARCHAR(5);
  DECLARE mVesselNo VARCHAR(20);
  DECLARE mMasterBl VARCHAR(50);
  DECLARE mSubhouseBl VARCHAR(50);
  DECLARE mApGlMappingId BIGINT(20);
  DECLARE mCostAmount DECIMAL(10,2);
  DECLARE mVendorName VARCHAR(50);
  DECLARE mVendorNo VARCHAR(10);
  DECLARE mInvoiceNo VARCHAR(50);
  DECLARE mCostId BIGINT(20);
  DECLARE mBsCostCode VARCHAR(5);
  DECLARE mCostCode VARCHAR(20);
  DECLARE mGlAccount VARCHAR(20);
  DECLARE mUserId INT(11);
  DECLARE mAccrualStatus VARCHAR(10);
  DECLARE mAccrualNotFound BOOLEAN DEFAULT FALSE;
  DECLARE mBkPieceUnitId BIGINT(20);
  -- Declare only for to get Gl Account 
  DECLARE mOriginId INT(11);  
  DECLARE mTermToBl VARCHAR(5);
  DECLARE mVoyOriginId INT(11);
  DECLARE mPolId INT(11);
  
  IF (new.ar_gl_mapping_id <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Code', (SELECT charge_code FROM gl_mapping WHERE id = new.ar_gl_mapping_id)) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.ar_amount <> 0.00) THEN
    SET insert_values = concat_insert_values (insert_values, ' Charge Amount', new.ar_amount) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.ap_amount <> 0.00) THEN
    SET insert_values = concat_insert_values (insert_values, ' Cost Amount', new.ap_amount) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.rate_per_weight_unit <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Rate Weight', new.rate_per_weight_unit) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.rate_per_volume_unit <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Rate Measure', new.rate_per_volume_unit) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.rate_flat_minimum <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Rate Minimum', new.rate_flat_minimum) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.cost_weight <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Cost Weight', new.cost_weight) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.cost_measure <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Cost Measure', new.cost_measure) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.cost_minimum <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Cost Minimum', new.cost_minimum) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.sp_acct_no <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Vendor Name', (SELECT acct_name FROM trading_partner WHERE acct_no = new.sp_acct_no)) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (new.sp_acct_no <> '') THEN
     SET insert_values = concat_insert_values (insert_values, ' Vendor Number', new.sp_acct_no) ;
     SET changesFlag = TRUE ;
  END IF ;
  IF (new.invoice_number <> '') THEN
    SET insert_values = concat_insert_values (insert_values, ' Invoice Number', new.invoice_number) ;
    SET changesFlag = TRUE ;
  END IF ;
  IF (insert_values <> '') THEN
    IF (changesFlag = TRUE) THEN
      CALL update_lclbooking_moddate (new.file_number_id) ;
    END IF ;
    SET insert_values = CONCAT('INSERTED ->', insert_values) ;
    IF (new.converted) THEN
      SET insert_values = CONCAT(insert_values, 'Converted from Eculine EDI') ;
    END IF ;
    INSERT INTO lcl_remarks (
      file_number_id,
      TYPE,
      remarks,
      entered_datetime,
      entered_by_user_id,
      modified_datetime,
      modified_by_user_id
    )
    VALUES (
      new.file_number_id,
      'DR-AutoNotes',
      insert_values,
      NOW(),
      new.entered_by_user_id,
      NOW(),
      new.modified_by_user_id
    ) ;
  END IF ;
  IF (new.ap_amount <> 0.00 AND new.`sp_acct_no` <> '' AND new.ap_gl_mapping_id IS NOT NULL AND new.`file_number_id` IS NOT NULL) THEN
  SELECT
     lbpu.id
  INTO mBkPieceUnitId
  FROM
     `lcl_booking_piece` lbp
      JOIN `lcl_booking_piece_unit` lbpu
        ON (lbp.`id` = lbpu.`booking_piece_id`)
       JOIN `lcl_unit_ss` lus
        ON (lbpu.`lcl_unit_ss_id` = lus.id)
       JOIN `lcl_ss_header` lssh
        ON (lus.`ss_header_id` = lssh.`id`)
        WHERE lbp.`file_number_id` = new.`file_number_id` AND  lssh.service_type <> 'N'
        GROUP BY lbp.`file_number_id`;
    SELECT
      fn.`id`,
      IF(bk.`booking_type` = 'E', 'LCLE', 'LCLI') AS shipmentType,
      IF(bk.`booking_type` = 'E', CONCAT_WS('-',
       CONVERT((SELECT IF(t.unlocationcode1 <> '',RIGHT(t.unlocationcode1,3),t.trmnum) FROM terminal t WHERE t.trmnum=bk.billing_terminal), CHARACTER CHARSET utf8),
       CONVERT((SELECT IF(u.bl_numbering = 'Y', RIGHT(`UnLocationGetCodeByID`(bk.`fd_id`),3),
       `UnLocationGetCodeByID` (bk.`fd_id`)) FROM un_location u WHERE u.id=bk.`fd_id`), CHARACTER CHARSET utf8),
        CONVERT(fn.`file_number`, CHARACTER CHARSET utf8)), CONCAT('IMP-', fn.`file_number`)) AS blNo,
      fn.`file_number` AS drNo,
      us.`sp_booking_no` AS bookingNo,
      CONCAT_WS('-', CONVERT(ssh.`billing_trmnum`, CHARACTER CHARSET utf8), `UnLocationGetCodeByID` (ssh.`origin_id`), `UnLocationGetCodeByID` (ssh.`destination_id`), ssh.`schedule_no`) AS voyageNo,
      (SELECT u.`unit_no` FROM `lcl_unit` u WHERE u.`id` = us.`unit_id` LIMIT 1) AS containerNo,
       IF(bk.`booking_type` = 'E',ssd.std,ssd.`sta`) AS eta,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = ssd.`sp_acct_no` LIMIT 1) AS ssline,
      `UnLocationGetNameStateCntryByID` (ssh.`origin_id`) AS origin,
      `UnLocationGetNameStateCntryByID` (ssh.`destination_id`) AS destination,
      bk.`ship_acct_no` AS shipperNo,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = bk.`ship_acct_no` LIMIT 1) AS shipperName,
      bk.`cons_acct_no` AS consigneeNo,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = bk.`cons_acct_no` LIMIT 1) AS consigneeName,
      bk.`fwd_acct_no` AS forwarderNo,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = bk.`fwd_acct_no` LIMIT 1) AS forwarderName,
      bk.`third_party_acct_no` AS thirdPartyNo,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = bk.`third_party_acct_no` LIMIT 1) AS thirdPartyName,
      bk.`agent_acct_no` AS agentNo,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = bk.`agent_acct_no` LIMIT 1) AS agentName,
      COALESCE(ssh.`billing_trmnum`, bk.`billing_terminal`) AS terminal,
      (SELECT `code` FROM `genericcode_dup` WHERE `codedesc` = ssd.`sp_reference_name` AND `codetypeid` = (SELECT `codetypeid` FROM `codetype` WHERE `description` = 'Vessel Codes' LIMIT 1) LIMIT 1) AS vesselNo,
      (SELECT `masterbl` FROM `lcl_unit_ss_manifest` WHERE `ss_header_id` = ssh.`id` AND `unit_id` = us.`unit_id` LIMIT 1) AS masterBl,
      (SELECT `sub_house_bl` FROM `lcl_booking_import` WHERE `file_number_id` = fn.`id` LIMIT 1) AS subhouseBl,
      new.`ap_gl_mapping_id` AS apGlMappingId,
      new.`ap_amount` AS costAmount,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = new.`sp_acct_no` LIMIT 1) AS vendorName,
      new.`sp_acct_no` AS vendorNo,
      new.`invoice_number` AS invoiceNo,
      new.`id` AS costId,
      new.`modified_by_user_id` AS userId,
      IF(bk.booking_type <> 'T', bk.poo_id,bk.pod_id )AS mOriginId,
      bk.`billing_terminal` AS mTermToBl,
      (SELECT origin_id FROM lcl_ss_header WHERE id = GetPickedORBookedVoyage(fn.id)) AS mVoyOriginId,
      IF(bk.booking_type <> 'T' ,bk.pol_id ,
      (SELECT usa_port_of_exit_id FROM lcl_booking_import WHERE file_number_id = fn.id)) AS mPolId
    INTO
      mFileNumberId,
      mShipmentType,
      mBlNo,
      mDrNo,
      mBookingNo,
      mVoyageNo,
      mContainerNo,
      mEta,
      mSsline,
      mOrigin,
      mDestination,
      mShipperNo,
      mShipperName,
      mConsigneeNo,
      mConsigneeName,
      mForwarderNo,
      mForwarderName,
      mThirdPartyNo,
      mThirdPartyName,
      mAgentNo,
      mAgentName,
      mTerminal,
      mVesselNo,
      mMasterBl,
      mSubhouseBl,
      mApGlMappingId,
      mCostAmount,
      mVendorName,
      mVendorNo,
      mInvoiceNo,
      mCostId,
      mUserId,
      mOriginId,
      mTermToBl,
      mVoyOriginId,
      mPolId
    FROM
      `lcl_file_number` fn
      JOIN `lcl_booking` bk
        ON (fn.`id` = bk.`file_number_id`)
      JOIN `lcl_booking_piece` bp
        ON (fn.`id` = bp.`file_number_id`)
      LEFT JOIN `lcl_booking_piece_unit` bpu
        ON (bp.`id` = bpu.`booking_piece_id` AND bpu.id=mBkPieceUnitId)
      LEFT JOIN `lcl_unit_ss` us
        ON (bpu.`lcl_unit_ss_id` = us.id)
      LEFT JOIN `lcl_ss_header` ssh
        ON (us.`ss_header_id` = ssh.`id`)
      LEFT JOIN `lcl_ss_detail` ssd
        ON (ssh.`id` = ssd.`ss_header_id`)
    WHERE fn.`id` = new.`file_number_id`
    GROUP BY fn.`id` ;
        SELECT
      IF(COUNT(transaction_id)>0, FALSE, TRUE),
      `status`
    INTO
      mAccrualNotFound,
      mAccrualStatus
    FROM
      `transaction_ledger`
    WHERE `cost_id` = mCostId
      AND `transaction_type` = 'AC'
      AND `drcpt` = mDrNo;
    SELECT
      gl.`bluescreen_chargecode`,
      gl.`charge_code`,
       IF(mShipmentType = 'LCLE',
      `DeriveLCLExportGlAccount`(gl.id,mTermToBl,mOriginId,IF(mVoyOriginId <> '',mVoyOriginId,mPolId)),
      `DeriveGlAccount`(gl.charge_Code,mShipmentType,gl.transaction_type,mTerminal)
      ) AS gl_account
    INTO
      mBsCostCode,
      mCostCode,
      mGlAccount
    FROM
      `gl_mapping` gl
    WHERE gl.`id` = mApGlMappingId ;
    IF (mAccrualNotFound) THEN
      INSERT INTO `lcl_booking_ac_trans` (
        `file_number_id`,
        `trans_type`,
        `trans_datetime`,
        `entry_type`,
        `reference_no`,
        `amount`,
        `entered_by_user_id`,
        `entered_datetime`,
        `modified_by_user_id`,
        `modified_datetime`
      )
      VALUES (
        mFileNumberId,
        'AC',
        NOW(),
        'A',
        '',
        mCostAmount,
        mUserId,
        NOW(),
        mUserId,
        NOW()
      );
      INSERT INTO `lcl_booking_ac_ta` (
        `lcl_booking_ac_trans_id`,
        `lcl_booking_ac_id`,
        `amount`,
        `entered_by_user_id`,
        `entered_datetime`,
        `modified_by_user_id`,
        `modified_datetime`
      )
      VALUES (
        (SELECT LAST_INSERT_ID()),
        mCostId,
        0.00,
        mUserId,
        NOW(),
        mUserId,
        NOW()
      );
      INSERT INTO `transaction_ledger` (
	  `cost_id`,
	  `cust_name`,
	  `cust_no`,
	  `invoice_number`,
	  `drcpt`,
	  `bill_ladding_no`,
	  `transaction_type`,
	  `status`,
	  `subledger_source_code`,
	  `shipment_type`,
	  `bluescreen_chargecode`,
	  `charge_code`,
	  `gl_account_number`,
	  `transaction_amt`,
	  `balance`,
	  `balance_in_process`,
	  `transaction_date`,
	  `sailing_date`,
	  `booking_no`,
	  `voyage_no`,
	  `container_no`,
	  `master_bl`,
	  `sub_house_bl`,
	  `originating_terminal`,
	  `destination`,
	  `ship_name`,
	  `ship_no`,
	  `cons_name`,
	  `cons_no`,
	  `fwd_name`,
	  `fwd_no`,
	  `third_pty_name`,
	  `third_pty_no`,
	  `agent_name`,
	  `agent_no`,
	  `billing_terminal`,
	  `terminal`,
	  `vessel_no`,
	  `bill_to`,
	  `ready_to_post`,
	  `currency_code`,
	  `manifest_flag`,
	  `created_on`,
	  `created_by`
      )
      VALUES (
	  mCostId,
	  mVendorName,
	  mVendorNo,
	  mInvoiceNo,
	  mDrNo,
	  mBlNo,
	  'AC',
	  'Open',
	  'ACC',
	  mShipmentType,
	  mBsCostCode,
	  mCostCode,
	  mGlAccount,
	  mCostAmount,
	  mCostAmount,
	  mCostAmount,
	  COALESCE(mEta, NOW()),
	  mEta,
	  mBookingNo,
	  mVoyageNo,
	  mContainerNo,
	  mMasterBl,
	  mSubhouseBl,
	  mOrigin,
	  mDestination,
	  mShipperName,
	  mShipperNo,
	  mConsigneeName,
	  mConsigneeNo,
	  mForwarderName,
	  mForwarderNo,
	  mThirdPartyName,
	  mThirdPartyNo,
	  mAgentName,
	  mAgentNo,
	  mTerminal,
	  mTerminal,
	  mVesselNo,
	  'Y',
	  'ON',
	  'USD',
	  'N',
	  NOW(),
	  mUserId
      );
    END IF;
  END IF;
END;
$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBookingAc_update`$$

CREATE
    TRIGGER `after_LclBookingAc_update` AFTER UPDATE ON `lcl_booking_ac` 
    FOR EACH ROW BEGIN
  DECLARE updatedValues TEXT DEFAULT '';
  DECLARE chargeValue BOOLEAN DEFAULT FALSE;
  DECLARE chageFlag BOOLEAN DEFAULT FALSE;
  DECLARE mFileNumberId BIGINT(20);
  DECLARE mShipmentType VARCHAR(4);
  DECLARE mBlNo VARCHAR(30);
  DECLARE mDrNo VARCHAR(7);
  DECLARE mBookingNo VARCHAR(20);
  DECLARE mVoyageNo VARCHAR(30);
  DECLARE mContainerNo VARCHAR(30);
  DECLARE mEta DATETIME;
  DECLARE mSsline VARCHAR(50);
  DECLARE mOrigin VARCHAR(100);
  DECLARE mDestination VARCHAR(100);
  DECLARE mShipperName VARCHAR(50);
  DECLARE mShipperNo VARCHAR(10);
  DECLARE mConsigneeName VARCHAR(50);
  DECLARE mConsigneeNo VARCHAR(10);
  DECLARE mForwarderName VARCHAR(50);
  DECLARE mForwarderNo VARCHAR(10);
  DECLARE mThirdPartyName VARCHAR(50);
  DECLARE mThirdPartyNo VARCHAR(10);
  DECLARE mAgentName VARCHAR(50);
  DECLARE mAgentNo VARCHAR(10);
  DECLARE mTerminal VARCHAR(5);
  DECLARE mVesselNo VARCHAR(20);
  DECLARE mMasterBl VARCHAR(50);
  DECLARE mSubhouseBl VARCHAR(50);
  DECLARE mApGlMappingId BIGINT(20);
  DECLARE mCostAmount DECIMAL(10,2);
  DECLARE mVendorName VARCHAR(50);
  DECLARE mVendorNo VARCHAR(10);
  DECLARE mInvoiceNo VARCHAR(50);
  DECLARE mCostId BIGINT(20);
  DECLARE mBsCostCode VARCHAR(5);
  DECLARE mCostCode VARCHAR(20);
  DECLARE mGlAccount VARCHAR(20);
  DECLARE mUserId INT(11);
  DECLARE mAccrualStatus VARCHAR(10);
  DECLARE mAccrualNotFound BOOLEAN DEFAULT FALSE;
  DECLARE mBookingType VARCHAR(2);
  DECLARE mBkPieceUnitId BIGINT(20);
  -- Declare only for to get Gl Account 
  DECLARE mOriginId INT(11);  
  DECLARE mTermToBl VARCHAR(5);
  DECLARE mVoyOriginId INT(11);
  DECLARE mPolId INT(11);
  
  IF (new.ar_gl_mapping_id <> old.ar_gl_mapping_id) THEN
    SET updatedValues = concat_string(updatedValues, 'Charge Code', (SELECT charge_code FROM gl_mapping WHERE id = old.ar_gl_mapping_id), (SELECT charge_code FROM gl_mapping WHERE id = new.ar_gl_mapping_id)) ;
    SET chargeValue = TRUE ;
    SET chageFlag = TRUE ;
  END IF ;
  IF (new.ar_amount <> old.ar_amount) THEN
    SET updatedValues = concat_string(updatedValues, 'Charge Amount', old.ar_amount, new.ar_amount) ;
    SET chageFlag = TRUE ;
  END IF ;
  IF (new.ap_amount <> old.ap_amount) THEN
    SET updatedValues = concat_string(updatedValues, 'Cost Amount', old.ap_amount, new.ap_amount) ;
    SET chageFlag = TRUE ;
  END IF ;
   /* ************************Only For Lcl Exports when change the FLAT RATE****************************** */
  SET mBookingType = (SELECT booking_type FROM lcl_booking WHERE file_number_id = new.file_number_id);
  IF (mBookingType NOT IN('I','T')) THEN
     IF (new.rate_per_unit  <> old.rate_per_unit) THEN
         SET updatedValues = concat_string(updatedValues, 'Charge Flat Amount', old.rate_per_unit, new.rate_per_unit) ;
         SET chageFlag = TRUE ;
     END IF;
     IF (new.cost_flatrate_amount <> old.cost_flatrate_amount) THEN
         SET updatedValues = concat_string(updatedValues, 'Cost Flat Amount', old.cost_flatrate_amount, new.cost_flatrate_amount) ;
         SET chageFlag = TRUE ;
     END IF;
  END IF ;
   /* ************************************************************************************ */
  IF (new.adjustment_amount <> old.adjustment_amount) THEN
    SET updatedValues = concat_string(updatedValues, 'Adjustment Amount', old.adjustment_amount, new.adjustment_amount) ;
    SET chageFlag = TRUE ;
  END IF ;
  IF (new.adjustment_comments <> old.adjustment_comments) THEN
    SET updatedValues = concat_string(updatedValues, 'Adjustment Comments', old.adjustment_comments, new.adjustment_comments) ;
    SET chageFlag = TRUE ;
  END IF ;
  IF (new.rate_uom <> old.rate_uom) THEN
    SET updatedValues = concat_string(updatedValues, 'Rate UOM', old.rate_uom, new.rate_uom) ;
    SET chageFlag = TRUE ;
  END IF ;
  IF (new.rate_per_weight_unit <> old.rate_per_weight_unit) THEN
    SET updatedValues = concat_string(updatedValues, 'Weight', old.rate_per_weight_unit, new.rate_per_weight_unit) ;
    SET chageFlag = TRUE ;
  END IF ;
  IF (new.rate_per_volume_unit <> old.rate_per_volume_unit) THEN
    SET updatedValues = concat_string (updatedValues, 'Volume', old.rate_per_volume_unit, new.rate_per_volume_unit) ;
    SET chageFlag = TRUE ;
  END IF ;
  IF (new.rate_flat_minimum <> old.rate_flat_minimum) THEN
    SET updatedValues = concat_string(updatedValues, 'Minimum', old.rate_flat_minimum, new.rate_flat_minimum) ;
    SET chageFlag = TRUE ;
  END IF ;
  /* ************************Wnen Change the W/M Rate Cost in LCL Exports****************************** */
  IF (new.cost_weight <> old.cost_weight) THEN
    SET updatedValues = concat_string(updatedValues, 'Cost Weight', old.cost_weight, new.cost_weight) ;
    SET chageFlag = TRUE ;
  END IF ;
  IF (new.cost_measure <> old.cost_measure) THEN
    SET updatedValues = concat_string (updatedValues, 'Cost Volume', old.cost_measure, new.cost_measure) ;
    SET chageFlag = TRUE ;
  END IF ;
  IF (new.cost_minimum <> old.cost_minimum) THEN
    SET updatedValues = concat_string(updatedValues, 'Cost Minimum', old.cost_minimum, new.cost_minimum) ;
    SET chageFlag = TRUE ;
  END IF ;
  /* *************************************************************************************************** */
  IF (new.sp_acct_no <> old.sp_acct_no) THEN
    SET updatedValues = concat_string(updatedValues, 'Vendor Name', (SELECT acct_name FROM trading_partner WHERE acct_no = old.sp_acct_no), (SELECT acct_name FROM trading_partner WHERE acct_no = new.sp_acct_no));
    SET updatedValues = concat_string(updatedValues, 'Vendor Number', old.sp_acct_no, new.sp_acct_no);
    SET chageFlag = TRUE ;
  END IF ;
  IF (new.invoice_number <> old.invoice_number) THEN
    SET updatedValues = concat_string(updatedValues, 'Invoice Number', old.invoice_number, new.invoice_number) ;
  ELSEIF (new.invoice_number IS NOT NULL AND new.invoice_number <> '' AND old.invoice_number IS NULL) THEN
    SET updatedValues = concat_string(updatedValues, 'Invoice Number', '', new.invoice_number) ;
    SET chageFlag = TRUE ;
  END IF ;
  IF (updatedValues <> '') THEN
    IF (chageFlag = TRUE) THEN
      CALL update_lclbooking_moddate (OLD.file_number_id) ;
    END IF ;
    IF (chargeValue = TRUE) THEN
      SET updatedValues = CONCAT('UPDATED ->', updatedValues) ;
    ELSE
      SET updatedValues = CONCAT('UPDATED -> (Code -> ', (SELECT charge_code FROM gl_mapping WHERE id = old.ar_gl_mapping_id), ')', updatedValues) ;
    END IF ;
    INSERT INTO lcl_remarks (
      file_number_id,
      TYPE,
      remarks,
      entered_datetime,
      entered_by_user_id,
      modified_datetime,
      modified_by_user_id
    )
    VALUES (
      old.file_number_id,
      'DR-AutoNotes',
      updatedValues,
      NOW(),
      new.modified_by_user_id,
      NOW(),
      new.modified_by_user_id
    ) ;
  END IF ;
  IF (new.ap_amount <> 0.00 AND new.`file_number_id` IS NOT NULL AND (isNotEqual(new.ap_amount, old.ap_amount) OR isNotEqual(new.`sp_acct_no`, old.`sp_acct_no`) OR isNotEqual(new.`invoice_number`, old.`invoice_number`)) AND new.ap_gl_mapping_id IS NOT NULL AND new.deleted = FALSE) THEN
    SELECT
     lbpu.id
  INTO mBkPieceUnitId
  FROM
     `lcl_booking_piece` lbp
      JOIN `lcl_booking_piece_unit` lbpu
        ON (lbp.`id` = lbpu.`booking_piece_id`)
       JOIN `lcl_unit_ss` lus
        ON (lbpu.`lcl_unit_ss_id` = lus.id)
       JOIN `lcl_ss_header` lssh
        ON (lus.`ss_header_id` = lssh.`id`)
        WHERE lbp.`file_number_id` = new.`file_number_id` AND  lssh.service_type <> 'N'
        GROUP BY lbp.`file_number_id`;
    SELECT
      fn.`id`,
      IF(bk.`booking_type` = 'E', 'LCLE', 'LCLI') AS shipmentType,
      IF(bk.`booking_type` = 'E', CONCAT_WS('-',
      CONVERT((SELECT IF(t.unlocationcode1 <> '',
      RIGHT(t.unlocationcode1,3),t.trmnum) FROM terminal t WHERE t.trmnum=bk.billing_terminal),
       CHARACTER CHARSET utf8), CONVERT(
       (SELECT IF(u.bl_numbering = 'Y', RIGHT(`UnLocationGetCodeByID`(bk.`fd_id`),3),
       `UnLocationGetCodeByID` (bk.`fd_id`)) FROM un_location u WHERE u.id=bk.`fd_id`),
       CHARACTER CHARSET utf8), CONVERT(fn.`file_number`, CHARACTER CHARSET utf8)),
       CONCAT('IMP-', fn.`file_number`)) AS blNo,
      fn.`file_number` AS drNo,
      us.`sp_booking_no` AS bookingNo,
      CONCAT_WS('-', CONVERT(ssh.`billing_trmnum`, CHARACTER CHARSET utf8), `UnLocationGetCodeByID` (ssh.`origin_id`), `UnLocationGetCodeByID` (ssh.`destination_id`), ssh.`schedule_no`) AS voyageNo,
      (SELECT u.`unit_no` FROM `lcl_unit` u WHERE u.`id` = us.`unit_id` LIMIT 1) AS containerNo,
      IF(bk.`booking_type` = 'E',ssd.std,ssd.`sta`) AS eta,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = ssd.`sp_acct_no` LIMIT 1) AS ssline,
      `UnLocationGetNameStateCntryByID` (ssh.`origin_id`) AS origin,
      `UnLocationGetNameStateCntryByID` (ssh.`destination_id`) AS destination,
      bk.`ship_acct_no` AS shipperNo,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = bk.`ship_acct_no` LIMIT 1) AS shipperName,
      bk.`cons_acct_no` AS consigneeNo,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = bk.`cons_acct_no` LIMIT 1) AS consigneeName,
      bk.`fwd_acct_no` AS forwarderNo,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = bk.`fwd_acct_no` LIMIT 1) AS forwarderName,
      bk.`third_party_acct_no` AS thirdPartyNo,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = bk.`third_party_acct_no` LIMIT 1) AS thirdPartyName,
      bk.`agent_acct_no` AS agentNo,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = bk.`agent_acct_no` LIMIT 1) AS agentName,
      COALESCE(ssh.`billing_trmnum`, bk.`billing_terminal`) AS terminal,
      (SELECT `code` FROM `genericcode_dup` WHERE `codedesc` = ssd.`sp_reference_name` AND `codetypeid` = (SELECT `codetypeid` FROM `codetype` WHERE `description` = 'Vessel Codes' LIMIT 1) LIMIT 1) AS vesselNo,
      (SELECT `masterbl` FROM `lcl_unit_ss_manifest` WHERE `ss_header_id` = ssh.`id` AND `unit_id` = us.`unit_id` LIMIT 1) AS masterBl,
      (SELECT `sub_house_bl` FROM `lcl_booking_import` WHERE `file_number_id` = fn.`id` LIMIT 1) AS subhouseBl,
      new.`ap_gl_mapping_id` AS apGlMappingId,
      new.`ap_amount` AS costAmount,
      (SELECT `acct_name` FROM `trading_partner` WHERE `acct_no` = new.`sp_acct_no` LIMIT 1) AS vendorName,
      new.`sp_acct_no` AS vendorNo,
      new.`invoice_number` AS invoiceNo,
      new.`id` AS costId,
      new.`modified_by_user_id` AS userId,
       IF(bk.booking_type <> 'T', bk.poo_id,bk.pod_id )AS mOriginId,
      bk.`billing_terminal` AS mTermToBl,
      (SELECT origin_id FROM lcl_ss_header WHERE id = GetPickedORBookedVoyage(fn.id)) AS mVoyOriginId,
      IF(bk.booking_type <> 'T' ,bk.pol_id ,
      (SELECT usa_port_of_exit_id FROM lcl_booking_import WHERE file_number_id = fn.id)) AS mPolId
    INTO
      mFileNumberId,
      mShipmentType,
      mBlNo,
      mDrNo,
      mBookingNo,
      mVoyageNo,
      mContainerNo,
      mEta,
      mSsline,
      mOrigin,
      mDestination,
      mShipperNo,
      mShipperName,
      mConsigneeNo,
      mConsigneeName,
      mForwarderNo,
      mForwarderName,
      mThirdPartyNo,
      mThirdPartyName,
      mAgentNo,
      mAgentName,
      mTerminal,
      mVesselNo,
      mMasterBl,
      mSubhouseBl,
      mApGlMappingId,
      mCostAmount,
      mVendorName,
      mVendorNo,
      mInvoiceNo,
      mCostId,
      mUserId,
      mOriginId,
      mTermToBl,
      mVoyOriginId,
      mPolId
    FROM
      `lcl_file_number` fn
      JOIN `lcl_booking` bk
        ON (fn.`id` = bk.`file_number_id`)
      JOIN `lcl_booking_piece` bp
        ON (fn.`id` = bp.`file_number_id`)
      LEFT JOIN `lcl_booking_piece_unit` bpu
        ON (bp.`id` = bpu.`booking_piece_id` AND bpu.id=mBkPieceUnitId)
      LEFT JOIN `lcl_unit_ss` us
        ON (bpu.`lcl_unit_ss_id` = us.`id`)
      LEFT JOIN `lcl_ss_header` ssh
        ON (us.`ss_header_id` = ssh.`id`)
      LEFT JOIN `lcl_ss_detail` ssd
        ON (ssh.`id` = ssd.`ss_header_id`)
    WHERE fn.`id` = new.`file_number_id`
    GROUP BY fn.`id`;
        SELECT
      IF(COUNT(transaction_id)>0, FALSE, TRUE),
      `status`
    INTO
      mAccrualNotFound,
      mAccrualStatus
    FROM
      `transaction_ledger`
    WHERE `cost_id` = mCostId
      AND `transaction_type` = 'AC'
      AND `drcpt` = mDrNo;
    SELECT
      gl.`bluescreen_chargecode`,
      gl.`charge_code`,      
      IF(mShipmentType = 'LCLE',
      `DeriveLCLExportGlAccount`(gl.id,mTermToBl,mOriginId,IF(mVoyOriginId <> '',mVoyOriginId,mPolId)),
      `DeriveGlAccount`(gl.charge_Code,mShipmentType,gl.transaction_type,mTerminal)
      ) AS gl_account
    INTO
      mBsCostCode,
      mCostCode,
      mGlAccount
    FROM
      `gl_mapping` gl
    WHERE gl.`id` = mApGlMappingId ;
    IF (mAccrualNotFound) THEN
      INSERT INTO `lcl_booking_ac_trans` (
        `file_number_id`,
        `trans_type`,
        `trans_datetime`,
        `entry_type`,
        `reference_no`,
        `amount`,
        `entered_by_user_id`,
        `entered_datetime`,
        `modified_by_user_id`,
        `modified_datetime`
      )
      VALUES (
        mFileNumberId,
        'AC',
        NOW(),
        'A',
        '',
        mCostAmount,
        mUserId,
        NOW(),
        mUserId,
        NOW()
      );
      INSERT INTO `lcl_booking_ac_ta` (
        `lcl_booking_ac_trans_id`,
        `lcl_booking_ac_id`,
        `amount`,
        `entered_by_user_id`,
        `entered_datetime`,
        `modified_by_user_id`,
        `modified_datetime`
      )
      VALUES (
        (SELECT LAST_INSERT_ID()),
        mCostId,
        0.00,
        mUserId,
        NOW(),
        mUserId,
        NOW()
      );
      INSERT INTO `transaction_ledger` (
	  `cost_id`,
	  `cust_name`,
	  `cust_no`,
	  `invoice_number`,
	  `drcpt`,
	  `bill_ladding_no`,
	  `transaction_type`,
	  `status`,
	  `subledger_source_code`,
	  `shipment_type`,
	  `bluescreen_chargecode`,
	  `charge_code`,
	  `gl_account_number`,
	  `transaction_amt`,
	  `balance`,
	  `balance_in_process`,
	  `transaction_date`,
	  `sailing_date`,
	  `booking_no`,
	  `voyage_no`,
	  `container_no`,
	  `master_bl`,
	  `sub_house_bl`,
	  `originating_terminal`,
	  `destination`,
	  `ship_name`,
	  `ship_no`,
	  `cons_name`,
	  `cons_no`,
	  `fwd_name`,
	  `fwd_no`,
	  `third_pty_name`,
	  `third_pty_no`,
	  `agent_name`,
	  `agent_no`,
	  `billing_terminal`,
	  `terminal`,
	  `vessel_no`,
	  `bill_to`,
	  `ready_to_post`,
	  `currency_code`,
	  `manifest_flag`,
	  `created_on`,
	  `created_by`
      )
      VALUES (
	  mCostId,
	  mVendorName,
	  mVendorNo,
	  mInvoiceNo,
	  mDrNo,
	  mBlNo,
	  'AC',
	  'Open',
	  'ACC',
	  mShipmentType,
	  mBsCostCode,
	  mCostCode,
	  mGlAccount,
	  mCostAmount,
	  mCostAmount,
	  mCostAmount,
	  COALESCE(mEta, NOW()),
	  mEta,
	  mBookingNo,
	  mVoyageNo,
	  mContainerNo,
	  mMasterBl,
	  mSubhouseBl,
	  mOrigin,
	  mDestination,
	  mShipperName,
	  mShipperNo,
	  mConsigneeName,
	  mConsigneeNo,
	  mForwarderName,
	  mForwarderNo,
	  mThirdPartyName,
	  mThirdPartyNo,
	  mAgentName,
	  mAgentNo,
	  mTerminal,
	  mTerminal,
	  mVesselNo,
	  'Y',
	  'ON',
	  'USD',
	  'N',
	  NOW(),
	  mUserId
      );
    ELSEIF (mAccrualStatus IN ('Open', 'Dispute', 'EDI Dispute')) THEN
      UPDATE
	  `transaction_ledger`
      SET
	  `cust_name` = mVendorName,
	  `cust_no` = mVendorNo,
	  `invoice_number` = mInvoiceNo,
	  `bill_ladding_no` = mBlNo,
	  `bluescreen_chargecode` = mBsCostCode,
	  `charge_code` = mCostCode,
	  `gl_account_number` = mGlAccount,
	  `transaction_amt` = mCostAmount,
	  `balance` = mCostAmount,
	  `balance_in_process` = mCostAmount,
	  `transaction_date` = COALESCE(mEta, NOW()),
	  `sailing_date` = mEta,
	  `booking_no` = mBookingNo,
	  `voyage_no` = mVoyageNo,
	  `container_no` = mContainerNo,
	  `master_bl` = mMasterBl,
	  `sub_house_bl` = mSubhouseBl,
	  `originating_terminal` = mOrigin,
	  `destination` = mDestination,
	  `ship_name` = mShipperName,
	  `ship_no` = mShipperNo,
	  `cons_name` = mConsigneeName,
	  `cons_no` = mConsigneeNo,
	  `fwd_name` = mForwarderName,
	  `fwd_no` = mForwarderNo,
	  `third_pty_name` = mThirdPartyName,
	  `third_pty_no` = mThirdPartyNo,
	  `agent_name` = mAgentName,
	  `agent_no` = mAgentNo,
	  `billing_terminal` = mTerminal,
	  `terminal` = mTerminal,
	  `vessel_no` = mVesselNo,
	  `updated_on` = NOW(),
	  `updated_by` = mUserId
      WHERE `cost_id` = mCostId
	  AND `transaction_type` = 'AC'
	  AND `drcpt` = mDrNo;
    END IF;
  END IF;
END;
$$

DELIMITER ;

DELIMITER $$

DROP FUNCTION IF EXISTS `DeriveLCLExportGlAccount`$$

CREATE FUNCTION `DeriveLCLExportGlAccount`(
  pId INT(11),
  pBillingTerimal VARCHAR(5),
  pOriginId INT(11),
  pPolId INT(11)
) RETURNS VARCHAR(20) CHARSET latin1
    READS SQL DATA
    DETERMINISTIC
MAIN : BEGIN
  DECLARE mAccount VARCHAR (20) DEFAULT NULL ;  
  DECLARE mSuffixValue VARCHAR (2) DEFAULT '' ;
  DECLARE mDeriveYN VARCHAR (1) DEFAULT 'N' ;
  DECLARE mTerminal VARCHAR (5) DEFAULT NULL ;
  DECLARE mGlAcct VARCHAR (20) DEFAULT '' ;
  
  IF ( pId <> 0 ) THEN 
  
   SELECT gl.`derive_yn` ,gl.`suffix_value`,gl.gl_acct INTO mDeriveYN,mSuffixValue,mGlAcct
   FROM `gl_mapping` gl  WHERE gl.`id` = pId;
   
   SELECT 
   CASE 
      WHEN mSuffixValue NOT IN('B','D','L') THEN
      mSuffixValue
      WHEN mSuffixValue = 'B' AND pBillingTerimal<>'' THEN  
      pBillingTerimal 
      WHEN mSuffixValue = 'B' AND pBillingTerimal = '' THEN 
      `OriginGetTerminal`(pPolId)
      WHEN mSuffixValue = 'D' AND pOriginId <> 0 THEN
       `OriginGetTerminal`(pOriginId)
      WHEN mSuffixValue = 'D' AND pOriginId = 0 THEN
      `OriginGetTerminal`(pPolId)
      ELSE
      `OriginGetTerminal`(pPolId)
      END INTO mTerminal;
      
    SELECT 
      CONCAT_WS('-', 
        SystemRulesGetRuleName('CompanyCode'),mGlAcct,
        LPAD(IF(mDeriveYN = 'Y', 
        `TerminalGLMapingGetTerminal`(mTerminal, 'LCLE', mSuffixValue), mSuffixValue), 2, '0'))
    INTO mAccount;
  END IF ;
  RETURN mAccount ;
END MAIN$$

DELIMITER ;

DELIMITER $$

DROP FUNCTION IF EXISTS `GetPickedORBookedVoyage`$$

CREATE FUNCTION `GetPickedORBookedVoyage`(pid INT (10)) RETURNS BIGINT(20) UNSIGNED
    READS SQL DATA
    DETERMINISTIC
MAIN :BEGIN
  DECLARE mVoyageId BIGINT (20) UNSIGNED DEFAULT NULL ;
  SELECT 
    lsh.id INTO mVoyageId 
  FROM
    lcl_booking_piece bp 
    JOIN lcl_booking_piece_unit bpu 
      ON bpu.`booking_piece_id` = bp.`id` 
    JOIN lcl_unit_ss lus 
      ON lus.`id` = bpu.`lcl_unit_ss_id` 
    JOIN lcl_ss_header lsh 
      ON lsh.id = lus.`ss_header_id` 
      AND lsh.`service_type` IN ('E','C')
  WHERE bp.`file_number_id` = getHouseBLForConsolidateDr (pid) limit 1;
  IF (mVoyageId IS NULL) 
  THEN 
  SELECT 
    b.booked_ss_header_id INTO mVoyageId 
  FROM
    lcl_booking b 
  WHERE b.file_number_id = getHouseBLForConsolidateDr (pid) ;
  END IF ;
  RETURN mVoyageId ;
END MAIN$$

DELIMITER ;

-- Mantis -14473 (LCL Exports) by Kuppusamy on 23 sep 2016

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclUnitwhse_update`$$

CREATE
    /*!50017 DEFINER = 'root'@'%' */
    TRIGGER `after_LclUnitwhse_update` AFTER UPDATE ON `lcl_unit_whse` 
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT ;
    IF((OLD.warehouse_id IS NULL AND NEW.warehouse_id IS NOT NULL) OR NEW.warehouse_id != OLD.warehouse_id OR (OLD.warehouse_id IS NOT NULL AND NEW.warehouse_id IS NULL)) THEN
     SET @oldwareValues=(SELECT CONCAT(warehsname,'(',warehsno,')') FROM warehouse WHERE id=old.warehouse_id);
     SET @newwareValues=(SELECT CONCAT(warehsname,'(',warehsno,')') FROM warehouse WHERE id=new.warehouse_id);
     SET updated_values=(SELECT concat_string(updated_values,'Warehouse',IFNULL(@oldwareValues,''),IFNULL(@newwareValues,'')));
     SET @unit_ss_id=(SELECT lus.id FROM lcl_unit_ss lus WHERE lus.ss_header_id=old.ss_header_id);
     CALL update_linkeddr_moddate(@unit_ss_id);
  END IF;
    IF ((OLD.stuffed_datetime IS NULL AND NEW.stuffed_datetime IS NOT NULL) OR NEW.stuffed_datetime != OLD.stuffed_datetime OR (OLD.stuffed_datetime IS NOT NULL AND NEW.stuffed_datetime IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'Stripped Date',IFNULL(DATE_FORMAT(old.stuffed_datetime,'%d-%b-%Y'),''),IFNULL(DATE_FORMAT(new.stuffed_datetime,'%d-%b-%Y'),'')));
    END IF;
    IF ((OLD.seal_no_in IS NULL AND NEW.seal_no_in IS NOT NULL) OR NEW.seal_no_in != OLD.seal_no_in OR (OLD.seal_no_in IS NOT NULL AND NEW.seal_no_in IS NULL)) THEN
	  SET updated_values = (SELECT concat_string(updated_values,'Seal In',IFNULL(OLD.seal_no_in,''),IFNULL(NEW.seal_no_in,'')));
	END IF;
    IF ((OLD.seal_no_out IS NULL AND NEW.seal_no_out IS NOT NULL) OR NEW.seal_no_out != OLD.seal_no_out OR (OLD.seal_no_out IS NOT NULL AND NEW.seal_no_out IS NULL)) THEN
	  SET updated_values = (SELECT concat_string(updated_values,'Seal Out',IFNULL(OLD.seal_no_out,''),IFNULL(NEW.seal_no_out,'')));
	END IF;
    IF ((OLD.stuffed_user_id IS NULL AND NEW.stuffed_user_id IS NOT NULL) OR NEW.stuffed_user_id != OLD.stuffed_user_id OR (OLD.stuffed_user_id IS NOT NULL AND NEW.stuffed_user_id IS NULL)) THEN
     SET @oldUserName=(SELECT CONCAT(first_name,' ',last_name) FROM user_details WHERE user_id=old.stuffed_user_id);
     SET @newUserName=(SELECT CONCAT(first_name,' ',last_name) FROM user_details WHERE user_id=new.stuffed_user_id);
     SET updated_values=(SELECT concat_string(updated_values,'Load By',IFNULL(@oldUserName,''),IFNULL(@newUserName,'')));
    END IF;
    IF ((OLD.destuffed_user_id IS NULL AND NEW.destuffed_user_id IS NOT NULL) OR NEW.destuffed_user_id != OLD.destuffed_user_id OR (OLD.destuffed_user_id IS NOT NULL AND NEW.destuffed_user_id IS NULL)) THEN
     SET @oldUserName=(SELECT login_name FROM user_details WHERE user_id=old.destuffed_user_id);
     SET @newUserName=(SELECT login_name FROM user_details WHERE user_id=new.destuffed_user_id);
     SET updated_values=(SELECT concat_string(updated_values,'Stripped By',IFNULL(@oldUserName,''),IFNULL(@newUserName,'')));
    END IF;
    IF ((OLD.location IS NULL AND NEW.location IS NOT NULL) OR NEW.location != OLD.location OR (OLD.location IS NOT NULL AND NEW.location IS NULL)) THEN
	  SET updated_values = (SELECT concat_string(updated_values,'DOOR',IFNULL(OLD.location,''),IFNULL(NEW.location,'')));
    END IF;
    
        IF ((OLD.destuffed_datetime IS NULL AND NEW.destuffed_datetime IS NOT NULL) OR NEW.destuffed_datetime != OLD.destuffed_datetime OR (OLD.destuffed_datetime IS NOT NULL AND NEW.destuffed_datetime IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'Stripped Date',IFNULL(DATE_FORMAT(old.destuffed_datetime,'%d-%b-%Y'),''),IFNULL(DATE_FORMAT(new.destuffed_datetime,'%d-%b-%Y'),'')));
    END IF;
    
    IF updated_values IS NOT NULL AND old.ss_header_id IS NOT NULL THEN
      SET updated_values=CONCAT(updated_values);
        INSERT INTO lcl_unit_ss_remarks(unit_id,ss_header_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(old.unit_id,old.ss_header_id,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;

-- Mantis Item# 8226 (Lcl Exports) by Aravindhan.V on 23/9/2016.

ALTER TABLE print_Config ADD COLUMN show_on_screen TINYINT(1) DEFAULT 1;

INSERT INTO print_config(screen_name,document_type,document_name,allow_print,show_on_screen)
VALUES('LCLBL','IN','Quick Print','Yes',FALSE);

-- Mantis Item#13249  (Lcl Exports) by Kuppusamy on 26/9/2016.
ALTER TABLE `role_duties`   
  ADD COLUMN `change_bl_commodity_after_cob` TINYINT(1) DEFAULT 0  NULL AFTER `pick_dr_warnings`;

--  Mantis Item-14085(LCL-Imports) by Stefy on 26 Sep 2016
INSERT INTO property(NAME,VALUE,TYPE,description) VALUES('AesException','NOEEI 30.2 (D)(1)','LCL','Aes Exception Value');

-- Mantis Item# 13621 (Lcl Exports) by Shanmugam on 28/9/2016.
INSERT INTO print_config(screen_name,document_type,document_name,allow_print,show_on_screen)
VALUES('LCLUnits','IN','Multimodal Dangerous Goods Form','No',TRUE);

-- Mantis Item# 14741 (Lcl Exports) by Sathish on 04/10/2016.
ALTER TABLE role_duties ADD COLUMN change_default_ff TINYINT(1) DEFAULT 0;

--Mantis Item #15209 (LCL Exports) by Priyanka on 12/10/2016

ALTER TABLE role_duties ADD COLUMN enable_batch_hot_code TINYINT(1) DEFAULT 0;

--  Mantis Item-14281(LCL-Imports) by Stefy on 12 Oct 2016

DROP TRIGGER `after_lclBookingAc_trans_update`;
ALTER TABLE `lcl_booking_ac_trans` CHANGE `payment_type` `payment_type` ENUM('Check/Cash','Credit Card','Credit Account','ACH','Check Copy','ACH/Wire/Electronic Payments');

UPDATE lcl_booking_ac_trans SET payment_type = 'ACH/Wire/Electronic Payments' WHERE payment_type = 'ACH';

ALTER TABLE `lcl_booking_ac_trans` CHANGE `payment_type` `payment_type` ENUM('Check/Cash','Credit Card','Credit Account','ACH/Wire/Electronic Payments','Check Copy');

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_lclBookingAc_trans_update`$$

CREATE
    TRIGGER `after_lclBookingAc_trans_update` AFTER UPDATE ON `lcl_booking_ac_trans` 
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT ;
    IF (old.payment_type <> new.payment_type)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Payment Type Changed',old.payment_type,new.payment_type));
     END IF;
    IF updated_values IS NOT NULL THEN
    SET updated_values=CONCAT('UPDATED ->',updated_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(OLD.file_number_id,'DR-AutoNotes',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;

-- Mantis Item# 14573 (Lcl Exports) by Nambu on 12/10/2016.

ALTER TABLE `agency_info`   
  ADD COLUMN `port_of_discharge` INT(11) NULL AFTER `lcl_agent_level_brand`,
  ADD COLUMN `final_deliveryTo` VARCHAR(25) NULL AFTER `port_of_discharge`, 
  ADD  INDEX `agency_info_fk1` (`port_of_discharge`),
  ADD CONSTRAINT
 `agency_info_fk1` FOREIGN KEY (`port_of_discharge`)
 REFERENCES `un_location`(`id`) ON UPDATE CASCADE;

-- Mantis Item# 12081 (Lcl Exports) by Kuppusamy on 12/10/2016.

ALTER TABLE `terminal`   
  ADD COLUMN `lcl_doc_dept_name` VARCHAR(50) NULL AFTER `customer_service_dept_email`,
  ADD COLUMN `lcl_doc_dept_email` VARCHAR(50) NULL AFTER `lcl_doc_dept_name`,
  ADD COLUMN `lcl_customer_service_dept_name` VARCHAR(50) NULL AFTER `lcl_doc_dept_email`,
  ADD COLUMN `lcl_customer_service_dept_email` VARCHAR(50) NULL AFTER `lcl_customer_service_dept_name`;

--  General Issue(Trading_Partner) by Stefy on 13 Oct 2016

DELIMITER $$

DROP PROCEDURE IF EXISTS `MergeTradingPartner`$$

CREATE PROCEDURE `MergeTradingPartner`(IN pDisabledAcctNo VARCHAR (10), IN pUpdatedUserId INT (11))
    MODIFIES SQL DATA
BEGIN
  DECLARE mMergedAcctNo VARCHAR (10) ;
  DECLARE mMergedAcctName VARCHAR (50) ;
  DECLARE mMergedAcctType VARCHAR (20) ;
  DECLARE mDisabledAcctType VARCHAR (20) ;
  DECLARE mDisabledBsShipFfNo VARCHAR(10);
  DECLARE mDisabledBsConsNo VARCHAR(10);
  DECLARE mDisabledBsVendNo VARCHAR(10);
  DECLARE mDisabledBsSslineNo VARCHAR(10);
  DECLARE mPortNumber VARCHAR (3) ;
  DECLARE mTaxExempt VARCHAR (10) ;
  DECLARE mFederalId VARCHAR (14) ;
  DECLARE mNotifyParty VARCHAR (40) ;
  DECLARE mNotifyPartyAddress VARCHAR (150) ;
  DECLARE mNotifyPartyCity VARCHAR (30) ;
  DECLARE mNotifyPartyState VARCHAR (2) ;
  DECLARE mNotifyPartyCountry VARCHAR (30) ;
  DECLARE mNotifyPartyPostalCode VARCHAR (20) ;
  DECLARE mMergedBsVendNo VARCHAR(10);
  DECLARE mMergedBsSslineNo VARCHAR(10);
  DECLARE mMergedAcctCoName VARCHAR (50) ;
  DECLARE mMergedAcctAddress TEXT ;
  DECLARE mMergedAcctCity VARCHAR (50) ;
  DECLARE mMergedAcctState VARCHAR (2) ;
  DECLARE mMergedAcctZip VARCHAR (20) ;
  DECLARE mMergedAcctCountry VARCHAR (30) ;
  DECLARE mMergedAcctPhone VARCHAR (25) ;
  DECLARE mMergedAcctFax VARCHAR (25) ;
  DECLARE mMergedAcctEmail1 VARCHAR (50) ;
  DECLARE mMergedAcctEmail2 VARCHAR (50) ;
  DECLARE mMergedAcctContact VARCHAR (50) ;
  DECLARE mConcattedAddress TEXT ;
  DECLARE mDisabledShipFfColoadCommodity VARCHAR(6);
  DECLARE mDisabledShipFfColoadCommodityDesc VARCHAR(200);
  DECLARE mDisabledShipFfRetailCommodity VARCHAR(6);
  DECLARE mDisabledShipFfFclCommodity VARCHAR(6);
  DECLARE mDisabledConsColoadCommodity INT(11);
  DECLARE mDisabledConsRetailCommodity INT(11);
  DECLARE mDisabledConsFclCommodity INT(11);
  DECLARE mDisabledOriginAgentImportCommodity VARCHAR(6);
  DECLARE mDisabledOriginAgentImportCommoditDesc VARCHAR(200);
  DECLARE mDisabledShipFfSalesCode VARCHAR(10);
  DECLARE mDisabledShipFfSalesCodeDesc VARCHAR(50);
  DECLARE mDisabledConsSalesCode INT(11);
  DECLARE mDisabledShipFfUserName VARCHAR(50);
  DECLARE mDisabledShipFfPassword VARCHAR(50);
  DECLARE mDisabledShipFfLastDatePasswordActivated DATETIME;
  DECLARE mDisabledShipFfAllowImportsWebDAPDDP VARCHAR(1);
  DECLARE mDisabledShipFfLclWebQuotes CHAR(1);
  DECLARE mDisabledShipFfFclWebQuotes CHAR(1);
  DECLARE mDisabledShipFfFclWebQuoteUseCommodity CHAR(1);
  DECLARE mDisabledShipFfAllowWebtoolsLogin CHAR(1);
  DECLARE mDisabledShipFfAllowCustomerControlledLogin VARCHAR(1);
  DECLARE mDisabledShipFfActivatePasswordWebQuotes CHAR(1);
  DECLARE mDisabledShipFfLclRateSheet VARCHAR(10);
  DECLARE mDisabledConsUserName VARCHAR(50);
  DECLARE mDisabledConsPassword VARCHAR(50);
  DECLARE mDisabledConsLastDatePasswordActivated DATETIME;
  DECLARE mDisabledConsAllowImportsWebDAPDDP VARCHAR(1);
  DECLARE mDisabledConsLclWebQuotes TINYINT(1);
  DECLARE mDisabledConsFclWebQuotes TINYINT(1);
  DECLARE mDisabledConsFclWebQuoteUseCommodity TINYINT(1);
  DECLARE mDisabledConsAllowWebtoolsLogin TINYINT(1);
  DECLARE mDisabledConsAllowCustomerControlledLogin VARCHAR(1);
  DECLARE mDisabledConsActivatePasswordWebQuotes TINYINT(1);
  DECLARE mDisabledConsLclRateSheet VARCHAR(1);
  DECLARE mDisabledConsLclImportsQuoting VARCHAR(1);
  DECLARE mDisabledConsImportsQuotesColoadRetail VARCHAR(1);
  DECLARE mMergedShipFfColoadCommodity VARCHAR(6);
  DECLARE mMergedShipFfColoadCommodityDesc VARCHAR(200);
  DECLARE mMergedShipFfRetailCommodity VARCHAR(6);
  DECLARE mMergedShipFfFclCommodity VARCHAR(6);
  DECLARE mMergedConsColoadCommodity INT(11);
  DECLARE mMergedConsRetailCommodity INT(11);
  DECLARE mMergedConsFclCommodity INT(11);
  DECLARE mMergedOriginAgentImportCommodity VARCHAR(6);
  DECLARE mMergedOriginAgentImportCommoditDesc VARCHAR(200);
  DECLARE mMergedShipFfSalesCode VARCHAR(10);
  DECLARE mMergedConsSalesCode INT(11);
  DECLARE mMergedShipFfUserName VARCHAR(50);
  DECLARE mMergedShipFfPassword VARCHAR(50);
  DECLARE mMergedConsUserName VARCHAR(50);
  DECLARE mMergedConsPassword VARCHAR(50);
  DECLARE mUpdatedBy VARCHAR(100);
  START TRANSACTION ;
  SELECT
    UCASE(u.`login_name`)
  INTO
    mUpdatedBy
  FROM
    `user_details` u
  WHERE u.`user_id` = pUpdatedUserId;
  SELECT
    m.`acct_no`,
    m.`acct_name`,
    CONCAT(
      m.`acct_type`,
      IF(
        m.`acct_type` LIKE '%V%'
        AND m.`sub_type` <> '',
        CONCAT(
          '-(',
          m.`sub_type`,
          IF(
            m.`sub_type` = 'Steamship Line'
            AND m.`ssline_number` <> '',
            CONCAT(' : ', m.`ssline_number`),
            ''
          ),
          ')'
        ),
        ''
      )
    ) AS acct_type,
    m.`ecivendno`,
    m.`ssline_number`,
    d.`port_number`,
    d.`tax_exempt`,
    d.`federal_id`,
    d.`notify_party`,
    d.`notify_party_address`,
    d.`notify_party_city`,
    d.`notify_party_state`,
    d.`notify_party_country`,
    d.`notify_party_postal_code`,
    d.`acct_type`,
    d.`eci_acct_no`,
    d.`ecifwno`,
    d.`ecivendno`,
    d.`ssline_number`
  INTO
    mMergedAcctNo,
    mMergedAcctName,
    mMergedAcctType,
    mMergedBsVendNo,
    mMergedBsSslineNo,
    mPortNumber,
    mTaxExempt,
    mFederalId,
    mNotifyParty,
    mNotifyPartyAddress,
    mNotifyPartyCity,
    mNotifyPartyState,
    mNotifyPartyCountry,
    mNotifyPartyPostalCode,
    mDisabledAcctType,
    mDisabledBsShipFfNo,
    mDisabledBsConsNo,
    mDisabledBsVendNo,
    mDisabledBsSslineNo
  FROM
    `trading_partner` m,
    (SELECT
      d.`forward_account`,
      d.`port_number`,
      d.`tax_exempt`,
      d.`federal_id`,
      d.`notify_party`,
      d.`notify_party_address`,
      d.`notify_party_city`,
      d.`notify_party_state`,
      d.`notify_party_country`,
      d.`notify_party_postal_code`,
      d.`acct_type`,
      d.`eci_acct_no`,
      d.`ecifwno`,
      d.`ecivendno`,
      d.`ssline_number`
    FROM
      `trading_partner` d
    WHERE d.`acct_no` = pDisabledAcctNo) AS d
  WHERE m.`acct_no` = d.`forward_account` ;
  SELECT
    d.`commodity_no`,
    d.`commodity_desc`,
    d.`retail_commodity`,
    d.`fcl_commodity`,
    d.`cons_coload_commodity`,
    d.`cons_retail_commodity`,
    d.`cons_fcl_commodity`,
    d.`imp_comm_no`,
    d.`imp_comm_desc`,
    d.`sales_code`,
    d.`sales_code_name`,
    d.`user_name`,
    d.`password`,
    d.`ld_pwd_activated`,
    d.`import_web_dap_ddp`,
    d.`allow_wlcl_quotes`,
    d.`allow_wfcl_quotes`,
    d.`fcl_webquote_use_commodity`,
    d.`import_tracking_screen2`,
    d.`shipff_cust_control_login`,
    d.`activate_pwd_quotes`,
    d.`lcl_rate_sheet`,
    d.`cons_sales_code`,
    d.`cons_user_name`,
    d.`cons_password`,
    d.`cons_last_pwd_activated_date`,
    d.`cons_import_web_dap_ddp`,
    d.`cons_allow_lcl_web_quotes`,
    d.`cons_allow_fcl_web_quotes`,
    d.`cons_fcl_web_quote_use_commodity`,
    d.`cons_import_tracking_screen`,
    d.`cons_cust_control_login`,
    d.`cons_activate_pwd_quotes`,
    d.`cons_lcl_rate_sheet`,
    d.`cons_lcl_import_quoting`,
    d.`import_quote_coload_retail`,
    m.`commodity_no`,
    m.`commodity_desc`,
    m.`retail_commodity`,
    m.`fcl_commodity`,
    m.`cons_coload_commodity`,
    m.`cons_retail_commodity`,
    m.`cons_fcl_commodity`,
    m.`imp_comm_no`,
    m.`imp_comm_desc`,
    m.`sales_code`,
    m.`user_name`,
    m.`password`,
    m.`cons_sales_code`,
    m.`cons_user_name`,
    m.`cons_password`
  INTO
    mDisabledShipFfColoadCommodity,
    mDisabledShipFfColoadCommodityDesc,
    mDisabledShipFfRetailCommodity,
    mDisabledShipFfFclCommodity,
    mDisabledConsColoadCommodity,
    mDisabledConsRetailCommodity,
    mDisabledConsFclCommodity,
    mDisabledOriginAgentImportCommodity,
    mDisabledOriginAgentImportCommoditDesc,
    mDisabledShipFfSalesCode,
    mDisabledShipFfSalesCodeDesc,
    mDisabledShipFfUserName,
    mDisabledShipFfPassword,
    mDisabledShipFfLastDatePasswordActivated,
    mDisabledShipFfAllowImportsWebDAPDDP,
    mDisabledShipFfLclWebQuotes,
    mDisabledShipFfFclWebQuotes,
    mDisabledShipFfFclWebQuoteUseCommodity,
    mDisabledShipFfAllowWebtoolsLogin,
    mDisabledShipFfAllowCustomerControlledLogin,
    mDisabledShipFfActivatePasswordWebQuotes,
    mDisabledShipFfLclRateSheet,
    mDisabledConsSalesCode,
    mDisabledConsUserName,
    mDisabledConsPassword,
    mDisabledConsLastDatePasswordActivated,
    mDisabledConsAllowImportsWebDAPDDP,
    mDisabledConsLclWebQuotes,
    mDisabledConsFclWebQuotes,
    mDisabledConsFclWebQuoteUseCommodity,
    mDisabledConsAllowWebtoolsLogin,
    mDisabledConsAllowCustomerControlledLogin,
    mDisabledConsActivatePasswordWebQuotes,
    mDisabledConsLclRateSheet,
    mDisabledConsLclImportsQuoting,
    mDisabledConsImportsQuotesColoadRetail,
    mMergedShipFfColoadCommodity,
    mMergedShipFfColoadCommodityDesc,
    mMergedShipFfRetailCommodity,
    mMergedShipFfFclCommodity,
    mMergedConsColoadCommodity,
    mMergedConsRetailCommodity,
    mMergedConsFclCommodity,
    mMergedOriginAgentImportCommodity,
    mMergedOriginAgentImportCommoditDesc,
    mMergedShipFfSalesCode,
    mMergedShipFfUserName,
    mMergedShipFfPassword,
    mMergedConsSalesCode,
    mMergedConsUserName,
    mMergedConsPassword
  FROM
    `cust_general_info` m,
    (SELECT
      t.`forward_account`,
      d.`commodity_no`,
      d.`commodity_desc`,
      d.`retail_commodity`,
      d.`fcl_commodity`,
      d.`cons_coload_commodity`,
      d.`cons_retail_commodity`,
      d.`cons_fcl_commodity`,
      d.`imp_comm_no`,
      d.`imp_comm_desc`,
      d.`sales_code`,
      d.`sales_code_name`,
      d.`user_name`,
      d.`password`,
      d.`ld_pwd_activated`,
      d.`import_web_dap_ddp`,
      d.`allow_wlcl_quotes`,
      d.`allow_wfcl_quotes`,
      d.`fcl_webquote_use_commodity`,
      d.`import_tracking_screen2`,
      d.`shipff_cust_control_login`,
      d.`activate_pwd_quotes`,
      d.`lcl_rate_sheet`,
      d.`cons_sales_code`,
      d.`cons_user_name`,
      d.`cons_password`,
      d.`cons_last_pwd_activated_date`,
      d.`cons_import_web_dap_ddp`,
      d.`cons_allow_lcl_web_quotes`,
      d.`cons_allow_fcl_web_quotes`,
      d.`cons_fcl_web_quote_use_commodity`,
      d.`cons_import_tracking_screen`,
      d.`cons_cust_control_login`,
      d.`cons_activate_pwd_quotes`,
      d.`cons_lcl_rate_sheet`,
      d.`cons_lcl_import_quoting`,
      d.`import_quote_coload_retail`
    FROM
      `cust_general_info` d,
      `trading_partner` t
    WHERE d.`acct_no` = pDisabledAcctNo
      AND d.`acct_no` = t.`acct_no`) AS d
  WHERE m.`acct_no` = d.`forward_account` ;
  SELECT
    UCASE(
      IF(
        ca.`co_name` <> '',
        ca.`co_name`,
        NULL
      )
    ),
    UCASE(
      IF(
        ca.`address1` <> '',
        ca.`address1`,
        NULL
      )
    ),
    UCASE(
      COALESCE(
        ct.`un_loc_name`,
        IF(ca.`city1` <> '', ca.`city1`, NULL)
      )
    ) AS city,
    UCASE(
      IF(ca.`state` <> '', ca.`state`, NULL)
    ),
    UCASE(IF(ca.`zip` <> '', ca.`zip`, NULL)),
    UCASE(
      IF(
        cy.`codedesc` <> 'UNITED STATES',
        cy.`codedesc`,
        NULL
      )
    ) AS country,
    UCASE(
      IF(ca.`phone` <> '', ca.`phone`, NULL)
    ),
    UCASE(IF(ca.`fax` <> '', ca.`fax`, NULL)),
    UCASE(
      IF(
        ca.`email1` <> '',
        ca.`email1`,
        NULL
      )
    ),
    UCASE(
      IF(
        ca.`email2` <> '',
        ca.`email2`,
        NULL
      )
    ),
    UCASE(
      IF(
        ca.`contact_name` <> '',
        ca.`contact_name`,
        NULL
      )
    )
  INTO
    mMergedAcctCoName,
    mMergedAcctAddress,
    mMergedAcctCity,
    mMergedAcctState,
    mMergedAcctZip,
    mMergedAcctCountry,
    mMergedAcctPhone,
    mMergedAcctFax,
    mMergedAcctEmail1,
    mMergedAcctEmail2,
    mMergedAcctContact
  FROM
    `cust_address` ca
    LEFT JOIN un_location ct
      ON (ca.city = ct.id)
    LEFT JOIN `genericcode_dup` cy
      ON (cy.`id` = ca.`country`)
  WHERE ca.`acct_no` = mMergedAcctNo ;
  SET mConcattedAddress = CONCAT_WS(
    '\n',
    mMergedAcctCoName,
    mMergedAcctAddress,
    CONCAT_WS(
      ' ',
      IF(
        mMergedAcctCity IS NOT NULL,
        CONCAT(mMergedAcctCity, ','),
        NULL
      ),
      mMergedAcctState,
      mMergedAcctZip
    ),
    IF(
      mMergedAcctCountry <> 'UNITED STATES',
      mMergedAcctCountry,
      NULL
    )
  ) ;
  CALL `mergeAccounting` (
    pDisabledAcctNo,
    mMergedAcctNo,
    mMergedAcctName
  ) ;
  CALL `mergeFcl` (
    pDisabledAcctNo,
    mMergedAcctNo,
    mMergedAcctName,
    mMergedAcctType,
    mMergedAcctContact,
    mMergedAcctCoName,
    mMergedAcctAddress,
    mMergedAcctCity,
    mMergedAcctState,
    mMergedAcctZip,
    mMergedAcctCountry,
    mMergedAcctPhone,
    mMergedAcctFax,
    mMergedAcctEmail1,
    mConcattedAddress
  ) ;
  CALL `MergeLcl` (
    pDisabledAcctNo,
    mMergedAcctNo,
    mMergedAcctName,
    mMergedAcctContact,
    mMergedAcctAddress,
    mMergedAcctCity,
    mMergedAcctState,
    mMergedAcctZip,
    mMergedAcctCountry,
    mMergedAcctPhone,
    mMergedAcctFax,
    mMergedAcctEmail1,
    mMergedAcctEmail2,
    pUpdatedUserId
  ) ;
  UPDATE
    `trading_partner` m
  SET
    m.`port_number` = mPortNumber,
    m.`tax_exempt` = mTaxExempt,
    m.`federal_id` = mFederalId,
    m.`notify_party` = mNotifyParty,
    m.`notify_party_address` = mNotifyPartyAddress,
    m.`notify_party_city` = mNotifyPartyCity,
    m.`notify_party_state` = mNotifyPartyState,
    m.`notify_party_country` = mNotifyPartyCountry,
    m.`notify_party_postal_code` = mNotifyPartyPostalCode
  WHERE m.`acct_no` = mMergedAcctNo ;
  IF (mDisabledAcctType LIKE '%S%' AND mMergedAcctType NOT LIKE '%S%') THEN
    UPDATE
      `trading_partner` m
    SET
      m.`acct_type` = CONCAT_WS(',', 'S', mMergedAcctType),
      m.`eci_acct_no` = mDisabledBsShipFfNo
    WHERE m.`acct_no` = mMergedAcctNo ;
  END IF;
  IF (mDisabledAcctType LIKE '%C%' AND mMergedAcctType NOT LIKE '%C%') THEN
    UPDATE
      `trading_partner` m
    SET
      m.`acct_type` = CONCAT_WS(',', 'C', mMergedAcctType),
      m.`ecifwno` = mDisabledBsConsNo
    WHERE m.`acct_no` = mMergedAcctNo ;
  END IF;
  IF (mDisabledBsVendNo <> '' AND (mMergedBsVendNo IS NULL OR TRIM(mMergedBsVendNo) = '')) THEN
    UPDATE
      `trading_partner` m
    SET
      m.`ecivendno` = mDisabledBsVendNo
    WHERE m.`acct_no` = mMergedAcctNo ;
  END IF;
  IF (mDisabledBsSslineNo <> '' AND (mMergedBsSslineNo IS NULL OR TRIM(mMergedBsSslineNo) = '')) THEN
    UPDATE
      `trading_partner` m
    SET
      m.`ssline_number` = mDisabledBsSslineNo
    WHERE m.`acct_no` = mMergedAcctNo ;
  END IF;
  IF (mDisabledShipFfColoadCommodity <> '' AND (mMergedShipFfColoadCommodity IS NULL OR TRIM(mMergedShipFfColoadCommodity) = '' OR mMergedShipFfColoadCommodity = '11292')) THEN
    UPDATE
      `cust_general_info` m
    SET
      m.`commodity_no` = mDisabledShipFfColoadCommodity,
      m.`commodity_desc` = mDisabledShipFfColoadCommodityDesc
    WHERE m.`acct_no` = mMergedAcctNo ;
  END IF;
  IF (mDisabledShipFfRetailCommodity <> '' AND (mMergedShipFfRetailCommodity IS NULL OR TRIM(mMergedShipFfRetailCommodity) = '' OR mMergedShipFfRetailCommodity = '11292')) THEN
    UPDATE
      `cust_general_info` m
    SET
      m.`retail_commodity` = mDisabledShipFfRetailCommodity,
      m.`updated_by` = mUpdatedBy
    WHERE m.`acct_no` = mMergedAcctNo ;
  END IF;
  IF (mDisabledShipFfFclCommodity <> '' AND (mMergedShipFfFclCommodity IS NULL OR TRIM(mMergedShipFfFclCommodity) = '' OR mMergedShipFfFclCommodity = '11292')) THEN
    UPDATE
      `cust_general_info` m
    SET
      m.`fcl_commodity` = mDisabledShipFfFclCommodity
    WHERE m.`acct_no` = mMergedAcctNo ;
  END IF;
  IF (mDisabledConsColoadCommodity IS NOT NULL AND (mMergedConsColoadCommodity IS NULL OR mMergedConsColoadCommodity = 11292)) THEN
    UPDATE
      `cust_general_info` m
    SET
      m.`cons_coload_commodity` = mDisabledConsColoadCommodity,
      m.`updated_by` = mUpdatedBy
    WHERE m.`acct_no` = mMergedAcctNo ;
  END IF;
  IF (mDisabledConsRetailCommodity IS NOT NULL AND (mMergedConsRetailCommodity IS NULL OR mMergedConsRetailCommodity = 11292)) THEN
    UPDATE
      `cust_general_info` m
    SET
      m.`cons_retail_commodity` = mDisabledConsRetailCommodity
    WHERE m.`acct_no` = mMergedAcctNo ;
  END IF;
  IF (mDisabledConsFclCommodity IS NOT NULL AND (mMergedConsFclCommodity IS NULL OR mMergedConsFclCommodity = 11292)) THEN
    UPDATE
      `cust_general_info` m
    SET
      m.`cons_fcl_commodity` = mDisabledConsFclCommodity,
      m.`updated_by` = mUpdatedBy
    WHERE m.`acct_no` = mMergedAcctNo ;
  END IF;
  IF (mDisabledOriginAgentImportCommodity <> '' AND (mMergedOriginAgentImportCommodity IS NULL OR TRIM(mMergedOriginAgentImportCommodity) = '' OR mMergedOriginAgentImportCommodity = '11292')) THEN
    UPDATE
      `cust_general_info` m
    SET
      m.`imp_comm_no` = mDisabledOriginAgentImportCommodity,
      m.`imp_comm_desc` = mDisabledOriginAgentImportCommoditDesc,
      m.`updated_by` = mUpdatedBy
    WHERE m.`acct_no` = mMergedAcctNo ;
  END IF;
  IF (mDisabledShipFfSalesCode <> '' AND (mMergedShipFfSalesCode IS NULL OR TRIM(mMergedShipFfSalesCode) = '')) THEN
    UPDATE
      `cust_general_info` m
    SET
      m.`sales_code` = mDisabledShipFfSalesCode,
      m.`sales_code_name` = mDisabledShipFfSalesCodeDesc,
      m.`updated_by` = mUpdatedBy
    WHERE m.`acct_no` = mMergedAcctNo ;
  END IF;
  IF (mDisabledConsSalesCode IS NOT NULL AND mMergedConsSalesCode IS NULL) THEN
    UPDATE
      `cust_general_info` m
    SET
      m.`cons_sales_code` = mDisabledConsSalesCode,
      m.`updated_by` = mUpdatedBy
    WHERE m.`acct_no` = mMergedAcctNo ;
  END IF;
  IF (mDisabledShipFfUserName <> '' AND (mMergedShipFfUserName IS NULL OR TRIM(mMergedShipFfUserName) = '')) THEN
    UPDATE
      `cust_general_info` m
    SET
      m.`user_name` = mDisabledShipFfUserName,
      m.`password` = mDisabledShipFfPassword,
      m.`ld_pwd_activated` = mDisabledShipFfLastDatePasswordActivated,
      m.`import_web_dap_ddp` = mDisabledShipFfAllowImportsWebDAPDDP,
      m.`allow_wlcl_quotes` = mDisabledShipFfLclWebQuotes,
      m.`allow_wfcl_quotes` = mDisabledShipFfFclWebQuotes,
      m.`fcl_webquote_use_commodity` = mDisabledShipFfFclWebQuoteUseCommodity,
      m.`import_tracking_screen2` = mDisabledShipFfAllowWebtoolsLogin,
      m.`shipff_cust_control_login` = mDisabledShipFfAllowCustomerControlledLogin,
      m.`activate_pwd_quotes` = mDisabledShipFfActivatePasswordWebQuotes,
      m.`lcl_rate_sheet` = mDisabledShipFfLclRateSheet,
      m.`updated_by` = mUpdatedBy
    WHERE m.`acct_no` = mMergedAcctNo ;
  END IF;
  IF (mDisabledConsUserName <> '' AND (mMergedConsUserName IS NULL OR TRIM(mMergedConsUserName) = '')) THEN
    UPDATE
      `cust_general_info` m
    SET
      m.`cons_user_name` = mDisabledConsUserName,
      m.`cons_password` = mDisabledConsPassword,
      m.`cons_last_pwd_activated_date` = mDisabledConsLastDatePasswordActivated,
      m.`cons_import_web_dap_ddp` = mDisabledConsAllowImportsWebDAPDDP,
      m.`cons_allow_lcl_web_quotes` = mDisabledConsLclWebQuotes,
      m.`cons_allow_fcl_web_quotes` = mDisabledConsFclWebQuotes,
      m.`cons_fcl_web_quote_use_commodity` = mDisabledConsFclWebQuoteUseCommodity,
      m.`cons_import_tracking_screen` = mDisabledConsAllowWebtoolsLogin,
      m.`cons_cust_control_login` = mDisabledConsAllowCustomerControlledLogin,
      m.`cons_activate_pwd_quotes` = mDisabledConsActivatePasswordWebQuotes,
      m.`cons_lcl_rate_sheet` = mDisabledConsLclRateSheet,
      m.`cons_lcl_import_quoting` = mDisabledConsLclImportsQuoting,
      m.`import_quote_coload_retail` = mDisabledConsImportsQuotesColoadRetail,
      m.`updated_by` = mUpdatedBy
    WHERE m.`acct_no` = mMergedAcctNo ;
  END IF;
  UPDATE
    `cust_contact` a,
    `cust_contact` b
  SET
    b.`code_a` = COALESCE(b.`code_a`, a.`code_a`),
    b.`code_b` = COALESCE(b.`code_b`, a.`code_b`),
    b.`code_c` = COALESCE(b.`code_c`, a.`code_c`),
    b.`code_d` = COALESCE(b.`code_d`, a.`code_d`),
    b.`code_e` = COALESCE(b.`code_e`, a.`code_e`),
    b.`code_f` = COALESCE(b.`code_f`, a.`code_f`),
    b.`code_g` = COALESCE(b.`code_g`, a.`code_g`),
    b.`code_h` = COALESCE(b.`code_h`, a.`code_h`),
    b.`code_i` = COALESCE(b.`code_i`, a.`code_i`),
    b.`code_j` = COALESCE(b.`code_j`, a.`code_j`),
    b.`position` = IF(
      b.`position` <> '',
      b.`position`,
      a.`position`
    ),
    b.`phone` = IF(
      b.`phone` <> '',
      b.`phone`,
      a.`phone`
    ),
    b.`extension` = IF(
      b.`extension` <> '',
      b.`extension`,
      a.`extension`
    ),
    b.`fax` = IF(b.`fax` <> '', b.`fax`, a.`fax`),
    b.`comment` = IF(
      b.`comment` <> '',
      b.`comment`,
      a.`comment`
    ),
    b.`update_by` = mUpdatedBy
  WHERE a.`acct_no` = pDisabledAcctNo
    AND b.acct_no = mMergedAcctNo
    AND a.`first_name` = b.`first_name`
    AND a.`last_name` = b.`last_name`
    AND a.`email` = b.`email` ;
  INSERT INTO `cust_contact` (
    `acct_no`,
    `first_name`,
    `last_name`,
    `position`,
    `phone`,
    `extension`,
    `fax`,
    `email`,
    `comment`,
    `code_a`,
    `code_b`,
    `code_c`,
    `code_d`,
    `code_e`,
    `code_f`,
    `code_g`,
    `code_h`,
    `code_i`,
    `code_j`,
    `update_by`
  )
  (SELECT
    mMergedAcctNo AS `acct_no`,
    `first_name`,
    `last_name`,
    `position`,
    `phone`,
    `extension`,
    `fax`,
    `email`,
    `comment`,
    `code_a`,
    `code_b`,
    `code_c`,
    `code_d`,
    `code_e`,
    `code_f`,
    `code_g`,
    `code_h`,
    `code_i`,
    `code_j`,
    mUpdatedBy
  FROM
    `cust_contact`
  WHERE `acct_no` = pDisabledAcctNo
    AND CONCAT(
      `first_name`,
      `last_name`,
      `email`
    ) NOT IN
    (SELECT
      CONCAT(
        `first_name`,
        `last_name`,
        `email`
      )
    FROM
      `cust_contact`
    WHERE `acct_no` = mMergedAcctNo)) ;
	UPDATE agency_info SET `agentid` = mMergedAcctNo WHERE `agentid` = pDisabledAcctNo;
	UPDATE agency_info SET `consigneeid` = mMergedAcctNo WHERE `consigneeid` = pDisabledAcctNo;
    INSERT INTO `notes` (
    `module_id`,
    `updatedate`,
    `note_type`,
    `note_desc`,
    `updated_by`,
    `item_name`,
    `void`,
    `module_ref_id`,
    `followup_date`,
    `status`,
    `unique_id`,
    `removed`,
    `print_on_report`
  )
  SELECT
    `module_id`,
    `updatedate`,
    `note_type`,
    `note_desc`,
    `updated_by`,
    `item_name`,
    `void`,
    mMergedAcctNo,
    `followup_date`,
    `status`,
    `unique_id`,
    `removed`,
    `print_on_report`
  FROM
    notes n
  WHERE n.`module_ref_id` = pDisabledAcctNo
    AND n.`module_id` = 'TRADING_PARTNER'
    AND n.`note_type` = 'manual' ;
  COMMIT ;
END$$

DELIMITER ;

--  General Issue(duplication in accounting) by Vellaisamy on 14 Oct 2016
DELETE 
FROM
  transaction_ledger 
WHERE subledger_source_code = 'ACC' 
  AND shipment_type LIKE 'FCL%' 
  AND bill_ladding_no LIKE '%-04-%' 
  AND cost_id NOT IN 
  (SELECT 
    code_id 
  FROM
    `fcl_bl_costcodes`) 
  AND STATUS NOT IN ('AS', 'EDI Assigned');

--  Mantis Item-14961(LCL-Exports) by Stefy on 18 Oct 2016
INSERT INTO `print_config`(`screen_name`,`document_type`,`document_name`,`file_location`,`allow_print`) 
VALUES ( 'LCLBL','IN','Unrated Bill Of Lading (Original)',NULL,'Yes');

--Mantis item -15329(LCL_Exports) by priyanka on 18 oct 2016
ALTER TABLE lcl_booking_hazmat ADD COLUMN liquid_volume_liters_gallons VARCHAR (10) NULL;
ALTER TABLE lcl_bl_hazmat ADD COLUMN liquid_volume_liters_gallons VARCHAR (10) NULL;

--Mantis item -14509(LCL_Exports) by Shanmugam on 19 oct 2016
ALTER TABLE `lcl_ss_header` 
  ADD COLUMN `bl_seq_num` SMALLINT UNSIGNED DEFAULT 0 NOT NULL AFTER `datasource`;

ALTER TABLE `lcl_bl` 
  ADD COLUMN `bl_seq_num` SMALLINT UNSIGNED DEFAULT 0 NOT NULL AFTER `invoice_value`;

--Mantis item ->0015721(LCL_Exports) by palraj on 19 oct 2016

INSERT INTO property (NAME, VALUE, TYPE, description) VALUES ('VoyageNotificationInternalEmail', 'voyagenotification@ecuworldwide.us', 'LCL', 'Voyage Notification Internal Email');

DELIMITER $$

DROP FUNCTION IF EXISTS `LclContactGetEmailFaxByFileIdCodeI`$$

CREATE FUNCTION `LclContactGetEmailFaxByFileIdCodeI`(fileId  BIGINT(20), emailCode  VARCHAR(5), faxCode VARCHAR(5),notificationId INT) RETURNS TEXT CHARSET utf8
    READS SQL DATA
    DETERMINISTIC
MAIN : BEGIN
DECLARE emailFaxList TEXT DEFAULT NULL;
IF (ISNULL(fileId) = FALSE AND fileId > 0  AND  ISNULL(emailCode) = FALSE  AND emailCode <> '' AND ISNULL(faxCode) = FALSE AND faxCode <> '') THEN
     SELECT  GROUP_CONCAT(DISTINCT contact.address) INTO emailFaxList FROM(
     	(SELECT GROUP_CONCAT(DISTINCT CONCAT('#',tp.acct_name,'$',contact.first_name,' ',contact.last_name,':'), CASE genericcode.code WHEN emailCode THEN TRIM(LOWER(contact.email)) WHEN faxCode THEN TRIM(contact.`fax`) ELSE NULL  END) AS address
               FROM `lcl_booking` booking, `cust_contact` contact,`genericcode_dup` genericcode, lcl_export_voyage_notification notification ,trading_partner tp WHERE booking.`file_number_id` = fileId
               AND notification.id=notificationId AND contact.`acct_no` = booking.`ship_acct_no`  AND genericcode.`id` = contact.`code_i`  AND genericcode.`code` IN (emailCode, faxCode)
               AND tp.acct_no = booking.ship_acct_no  AND notification.shipper=1)
         UNION
        (SELECT GROUP_CONCAT(DISTINCT CONCAT('#',tp.acct_name,'$',contact.first_name,' ',contact.last_name,':'), CASE genericcode.`code` WHEN emailCode THEN TRIM(LOWER(contact.`email`)) WHEN faxCode THEN TRIM(contact.`fax`) ELSE NULL  END) AS address
               FROM `lcl_booking` booking, `cust_contact` contact,`genericcode_dup` genericcode,lcl_export_voyage_notification notification,trading_partner tp  WHERE booking.`file_number_id` = fileId
               AND notification.id=notificationId AND contact.`acct_no` = booking.`cons_acct_no`  AND genericcode.`id` = contact.`code_i`  AND genericcode.`code` IN (emailCode, faxCode)
               AND tp.acct_no = booking.cons_acct_no AND notification.consignee=1)
	UNION
	  (SELECT  GROUP_CONCAT(DISTINCT CONCAT('#',tp.acct_name,'$',contact.first_name,' ',contact.last_name,':'), CASE genericcode.`code` WHEN emailCode THEN TRIM(LOWER(contact.`email`)) WHEN faxCode THEN TRIM(contact.`fax`) ELSE NULL  END) AS address
               FROM `lcl_booking` booking, `cust_contact` contact,`genericcode_dup` genericcode,lcl_export_voyage_notification notification,trading_partner tp  WHERE booking.`file_number_id` = fileId
               AND notification.id=notificationId AND contact.`acct_no` = booking.`fwd_acct_no`  AND genericcode.`id` = contact.`code_i`  AND genericcode.`code` IN (emailCode, faxCode)
               AND tp.acct_no = booking.fwd_acct_no AND notification.forwarder=1)
	UNION
	  (SELECT  GROUP_CONCAT(DISTINCT CONCAT('#',tp.acct_name,'$',contact.first_name,' ',contact.last_name,':'), CASE genericcode.`code` WHEN emailCode THEN TRIM(LOWER(contact.`email`)) WHEN faxCode THEN TRIM(contact.`fax`) ELSE NULL  END) AS address
               FROM `lcl_booking` booking, `cust_contact` contact,`genericcode_dup` genericcode,lcl_export_voyage_notification notification,trading_partner tp   WHERE booking.`file_number_id` = fileId
               AND notification.id=notificationId AND contact.`acct_no` = booking.`noty_acct_no`  AND genericcode.`id` = contact.`code_i`  AND genericcode.`code` IN (emailCode, faxCode)
               AND tp.acct_no = booking.noty_acct_no AND  notification.notify=1)
	UNION
	  (SELECT IF (cc.`email1` IS NOT NULL OR cc.`email1` <> '' OR sc.`email1` IS NOT NULL OR sc.`email1` <> '' OR fc.`email1` IS NOT NULL OR fc.`email1` <> ''
	         OR cnc.`email1` IS NOT NULL OR cnc.`email1` <> '' ,GROUP_CONCAT(DISTINCT '$',cc.contact_name,sc.contact_name,fc.contact_name,cnc.contact_name,':',cc.email1,sc.`email1`,fc.`email1`,cnc.`email1`),'') AS address
	         FROM lcl_export_voyage_notification notification , lcl_booking bkg JOIN lcl_contact cc ON bkg.`client_contact_id` = cc.id JOIN lcl_contact sc ON bkg.`ship_contact_id` = sc.id
	         JOIN lcl_contact fc ON bkg.`fwd_contact_id` = fc.`id` JOIN lcl_contact cnc ON bkg.`cons_contact_id` = cnc.id
                 WHERE notification.id=notificationId AND notification.booking_con=1 AND bkg.`file_number_id` =fileId)
	UNION
	  (SELECT  GROUP_CONCAT(DISTINCT CONCAT('#',tp.acct_name,'$',contact.first_name,' ',contact.last_name,':'), CASE genericcode.`code` WHEN emailCode THEN TRIM(LOWER(contact.`email`)) WHEN faxCode THEN TRIM(contact.`fax`) ELSE NULL  END) AS address
               FROM `lcl_booking` booking, `cust_contact` contact,`genericcode_dup` genericcode,lcl_export_voyage_notification notification ,trading_partner tp  WHERE booking.`file_number_id` = fileId
               AND notification.id=notificationId AND contact.`acct_no` = booking.`agent_acct_no`  AND genericcode.`id` = contact.`code_i`  AND genericcode.`code` IN (emailCode, faxCode)
               AND tp.acct_no = booking.agent_acct_no  AND notification.port_agent=1)
   )AS contact;
END IF;
  RETURN emailFaxList;
END MAIN$$

DELIMITER ;
--  Mantis Item-15485(LCL-Exports) by Stefy on 24 Oct 2016
INSERT INTO property(NAME,VALUE,TYPE,description) VALUES('InsuranceChargeComment','*** GOODS SHIPPED HEREUNDER ARE INSURED ***','LCL','Lcl Exports Insurance Charge Comment');

--  Mantis Item-15677(LCL-Exports) by Stefy on 25 Oct 2016
ALTER TABLE trading_partner MODIFY COLUMN tax_exempt VARCHAR(11);

--  Mantis Item-14441(LCL-Exports) by Rathnapandian on 25 Oct 2016

ALTER TABLE role_duties
ADD COLUMN allow_change_disposition  TINYINT(1) DEFAULT 0;

--  Mantis Item-0015577(LCL-Exports) by PALRAJ on 26 Oct 2016

ALTER TABLE lcl_unit_ss ADD COLUMN manifested_datetime DATETIME NULL AFTER solas_tare_weight_metric; 

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_lclUnitSs_insert_trigger`$$

CREATE TRIGGER `after_lclUnitSs_insert_trigger` AFTER INSERT ON `lcl_unit_ss` 
    FOR EACH ROW BEGIN
      DECLARE insert_values TEXT ;
      DECLARE checkBookingFlag BOOLEAN DEFAULT FALSE;
    IF new.drayage_provided!='' THEN
       SET insert_values=(SELECT concat_insert_values(insert_values,'Drayage Provided',new.drayage_provided));
    END IF;
    IF new.intermodal_provided!='' THEN
       SET insert_values=(SELECT concat_insert_values(insert_values,'Intermodal Provided',new.intermodal_provided));
    END IF;
    IF new.stop_off!='' THEN
       SET insert_values=(SELECT concat_insert_values(insert_values,'Stop Off',new.stop_off));
    END IF;
    IF new.su_heading_note!='' THEN
       SET insert_values=(SELECT concat_insert_values(insert_values,'SU Heading Note',new.su_heading_note));
    END IF;
    IF new.prepaid_collect!='' THEN
       SET insert_values=(SELECT concat_insert_values(insert_values,'Prepaid/Collect',new.prepaid_collect));
    END IF;
    IF new.trucking_remarks!='' THEN
       SET insert_values=(SELECT concat_insert_values(insert_values,'Trucking Remarks',new.trucking_remarks));
    END IF;
    IF new.sp_booking_no!='' THEN
    SET insert_values=(SELECT concat_insert_values(insert_values,'Linked to Master',new.sp_booking_no));
    END IF;
    IF new.manifested_datetime IS NOT NULL THEN
    SET insert_values=(SELECT concat_insert_values(insert_values,'Manifested Date',DATE_FORMAT(new.manifested_datetime,'%m/%d/%Y')));
    END IF;
    IF insert_values IS NOT NULL THEN
        SET insert_values=CONCAT('INSERTED ->',insert_values);
        INSERT INTO lcl_unit_ss_remarks(unit_id,ss_header_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(new.unit_id,new.ss_header_id,'auto',insert_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;        
    END;
$$

DELIMITER ;


DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_lclUnitSs_update`$$

CREATE TRIGGER `after_lclUnitSs_update` AFTER UPDATE ON `lcl_unit_ss` 
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT ;
    DECLARE checkMasterFlag BOOLEAN DEFAULT FALSE;
    DECLARE checkBookingFlag BOOLEAN DEFAULT FALSE;
    DECLARE updated_values_master TEXT;
    IF new.status!=old.status THEN
IF new.status='C' THEN
SET updated_values=(SELECT concat_string(updated_values,'Load Completed',old.status,new.status));
        END IF;
        IF new.status='E' THEN
SET updated_values=(SELECT concat_string(updated_values,'Un Load Completed',old.status,new.status));
        END IF;
    END IF;
     IF new.cob!=old.cob THEN
       SET updated_values=(SELECT concat_string(updated_values,'Changed COB',IF(old.cob=0,'No','Yes'),IF(new.cob=0,'No','Yes')));
    END IF;
    IF new.stop_off!=old.stop_off THEN
       SET updated_values=(SELECT concat_string(updated_values,'Stop Off',IF(old.stop_off=0,'No','Yes'),IF(new.stop_off=0,'No','Yes')));
    END IF;
    IF new.intermodal_provided!=old.intermodal_provided THEN
       SET updated_values=(SELECT concat_string(updated_values,'InterModal Provided',IF(old.intermodal_provided=0,'No','Yes'),IF(new.intermodal_provided=0,'No','Yes')));
    END IF;
    IF new.drayage_provided!=old.drayage_provided THEN
       SET updated_values=(SELECT concat_string(updated_values,'Drayage Provided',IF(old.drayage_provided=0,'No','Yes'),IF(new.drayage_provided=0,'No','Yes')));
    END IF;
    IF ((OLD.su_heading_note IS NULL AND NEW.su_heading_note IS NOT NULL) OR NEW.su_heading_note != OLD.su_heading_note OR (OLD.su_heading_note IS NOT NULL AND NEW.su_heading_note IS NULL)) THEN
    SET updated_values=(SELECT concat_string(updated_values,'SU Heading Note',IFNULL(old.su_heading_note,''),IFNULL(new.su_heading_note,'')));
    END IF;
    IF ((OLD.trucking_remarks IS NULL AND NEW.trucking_remarks IS NOT NULL) OR NEW.trucking_remarks != OLD.trucking_remarks OR (OLD.trucking_remarks IS NOT NULL AND NEW.trucking_remarks IS NULL)) THEN
    SET updated_values=(SELECT concat_string(updated_values,'Trucking Information',IFNULL(old.trucking_remarks,''),IFNULL(new.trucking_remarks,'')));
    END IF;
    IF OLD.prepaid_collect!=NEW.prepaid_collect THEN
    SET updated_values=(SELECT concat_string(updated_values,'Prepaid/Collect',old.prepaid_collect,new.prepaid_collect));
    END IF;
    
    IF ((OLD.sp_booking_no IS NULL AND NEW.sp_booking_no IS NOT NULL) OR (OLD.sp_booking_no IS NOT NULL AND NEW.sp_booking_no IS NULL) OR 
    (OLD.sp_booking_no!=NEW.sp_booking_no)) THEN
    SET checkBookingFlag = TRUE;
    SET updated_values= (SELECT concat_string(updated_values,'Linked to Master', old.sp_booking_no,new.sp_booking_no));
    END IF;
    
    IF ((OLD.bl_body IS NULL AND NEW.bl_body IS NOT NULL) OR (OLD.bl_body IS NOT NULL AND NEW.bl_body IS NULL) OR OLD.bl_body!=NEW.bl_body) THEN
    SET updated_values=(SELECT concat_string(updated_values,'BL Body',IFNULL(old.bl_body,''),IFNULL(new.bl_body,'')));
    SET checkMasterFlag = TRUE;
    END IF;
    
    IF ((OLD.volume_metric IS NULL AND NEW.volume_metric IS NOT NULL) OR (OLD.volume_metric IS NOT NULL AND NEW.volume_metric IS NULL)
    OR OLD.volume_metric!=NEW.volume_metric) THEN
    SET updated_values=(SELECT concat_string(updated_values,'CBM',IFNULL(old.volume_metric,''),IFNULL(new.volume_metric,'')));
    SET checkMasterFlag = TRUE;    
    END IF;
    
    IF ((OLD.weight_metric IS NULL AND NEW.weight_metric IS NOT NULL) OR (OLD.weight_metric IS NOT NULL AND NEW.weight_metric IS NULL)
    OR OLD.weight_metric!=NEW.weight_metric)THEN
    SET updated_values=(SELECT concat_string(updated_values,'KGS',IFNULL(old.weight_metric,''),IFNULL(new.weight_metric,'')));
    SET checkMasterFlag = TRUE;    
    END IF;
    
    IF ((OLD.volume_imperial IS NULL AND NEW.volume_imperial IS NOT NULL) OR (OLD.volume_imperial IS NOT NULL AND NEW.volume_imperial IS NULL)
    OR OLD.volume_imperial!=NEW.volume_imperial) THEN
    SET updated_values=(SELECT concat_string(updated_values,'CFT',IFNULL(old.volume_imperial,''),IFNULL(new.volume_imperial,'')));
    SET checkMasterFlag = TRUE;    
    END IF;
    
    IF ((OLD.weight_imperial IS NULL AND NEW.weight_imperial IS NOT NULL) OR (OLD.weight_imperial IS NOT NULL AND NEW.weight_imperial IS NULL)
    OR OLD.weight_imperial!=NEW.weight_imperial) THEN
    SET updated_values=(SELECT concat_string(updated_values,'LBS',IFNULL(old.weight_imperial,''),IFNULL(new.weight_imperial,'')));
    SET checkMasterFlag = TRUE;    
    END IF;
    
    IF ((OLD.total_pieces IS NULL AND NEW.total_pieces IS NOT NULL) OR (OLD.total_pieces IS NOT NULL AND NEW.total_pieces IS NULL)
    OR OLD.total_pieces!=NEW.total_pieces) THEN
    SET updated_values=(SELECT concat_string(updated_values,'Pieces',IFNULL(old.total_pieces,''),IFNULL(new.total_pieces,'')));
    SET checkMasterFlag = TRUE;
    END IF;
    
    IF OLD.manifested_datetime IS NOT NULL AND NEW.manifested_datetime IS NULL THEN
    SET updated_values=(SELECT concat_string(updated_values,'UnManifested Date',DATE_FORMAT(OLD.manifested_datetime,'%m/%d/%Y'),''));
    END IF;
    
    IF OLD.manifested_datetime IS NULL AND NEW.manifested_datetime IS NOT NULL THEN
    SET updated_values=(SELECT concat_string(updated_values,'Manifested Date','',DATE_FORMAT(NEW.manifested_datetime,'%m/%d/%Y')));
    END IF;
    
    SET @unit_id=(SELECT unit_id FROM lcl_unit_ss WHERE id=old.id);
    SET @ss_header=(SELECT ss_header_id FROM lcl_unit_ss WHERE id=old.id );
    SET @unlinkedunit_no=(SELECT unit_no FROM lcl_unit WHERE id=@unit_id);
    
    SET @masterbl_id= (SELECT id FROM lcl_ss_masterbl WHERE sp_booking_no= 
    (SELECT sp_booking_no FROM lcl_unit_ss WHERE ss_header_id=@ss_header AND unit_id=@unit_id ) LIMIT 1);
     
    SET @unit_no=(SELECT GROUP_CONCAT(unit_no) FROM lcl_unit lu JOIN lcl_unit_ss lus ON lus.unit_id=lu.id JOIN lcl_ss_header lsh ON lsh.id= lus.ss_header_id
    JOIN lcl_ss_masterbl lsm ON lsm.ss_header_id=lsh.id AND lsm.sp_booking_no=lus.sp_booking_no WHERE lsm.ss_header_id=@ss_header AND lsm.id=@masterbl_id 
    AND lus.id=old.id AND lus.unit_id=@unit_id);
        
    
    IF (@masterbl_id!='')  THEN
    SET updated_values_master=CONCAT('Linked to Unit->',@unit_no);   
    ELSE
    SET updated_values_master=CONCAT('UnLinked to Unit->',@unlinkedunit_no);    
    END IF; 
    
    IF checkMasterFlag = TRUE THEN
    SET updated_values=CONCAT('Updated Show On Master',updated_values);
    END IF;    
    
    IF updated_values IS NOT NULL THEN
    SET updated_values=CONCAT('UPDATED ->',updated_values);
        INSERT INTO lcl_unit_ss_remarks(unit_id,ss_header_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(@unit_id,@ss_header,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    
    IF (checkBookingFlag = TRUE) AND updated_values_master IS NOT NULL THEN
    INSERT INTO lcl_ss_remarks(ss_header_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
    VALUES(@ss_header,'auto',updated_values_master,NOW(),new.entered_by_user_id,NOW(),new.modified_by_user_id);
    END IF;    
    
    END IF;
      END;
$$

DELIMITER ;

-- Mantis Item-15701(Accounting) by Vellaisamy on 26 Oct 2016
DELETE 
FROM
  transaction_ledger 
WHERE subledger_source_code = 'ACC' 
  AND shipment_type LIKE 'FCL%' 
  AND bill_ladding_no LIKE '%-04-%' 
  AND cost_id NOT IN 
  (SELECT 
    code_id 
  FROM
    `fcl_bl_costcodes`) 
  AND STATUS NOT IN ('AS', 'EDI Assigned');

-- Mantis Item-14609(Accounting) by Shanugam on 28Oct 2016
ALTER TABLE `edi_997_ack`  CHANGE `Quote_DR` `Quote_DR` VARCHAR(20)  NULL ;

-- Mantis Item# 15433  (Lcl Exports) by Kuppusamy on 02/11/2016.

ALTER TABLE `role_duties`   
  ADD COLUMN `lcl_booking_default_ert` TINYINT(1) DEFAULT 0  NULL AFTER `allow_change_disposition`;


-- Mantis Item-10025(Logiware) by Stefy on 08 Nov 2016

ALTER TABLE cust_contact ADD COLUMN lcl_exports TINYINT(1) DEFAULT 0,ADD COLUMN lcl_imports TINYINT(1) DEFAULT 0,
ADD COLUMN fcl_exports TINYINT(1) DEFAULT 0,ADD COLUMN fcl_imports TINYINT(1) DEFAULT 0;

DELIMITER $$

DROP FUNCTION IF EXISTS `ContactGetEmailFaxByAcctNoCodeK`$$

CREATE FUNCTION `ContactGetEmailFaxByAcctNoCodeK`(
  pAcctNo VARCHAR (10),
  pCodeEmail1 VARCHAR (5),
  pCodeFax1 VARCHAR (5)
) RETURNS VARCHAR(255) CHARSET utf8
    READS SQL DATA
    DETERMINISTIC
MAIN :
BEGIN
  DECLARE mRet VARCHAR (255) DEFAULT NULL ;
  IF (ISNULL(pAcctNo) = FALSE AND pAcctNo <> '' ) THEN
    SELECT
      GROUP_CONCAT(
        CASE
          WHEN gc.`code` = pCodeEmail1 THEN
            TRIM(LOWER(ct.`email`))
          WHEN gc.`code` = pCodeFax1 THEN
            TRIM(ct.`fax`)
          ELSE NULL
        END
      ) INTO mRET
    FROM
      `cust_contact` ct
      JOIN `genericcode_dup` gc
        ON (
          gc.`id` = ct.`code_k` 
          AND gc.`code` IN (pCodeEmail1, pCodeFax1)
        )
    WHERE ct.`acct_no` = pAcctNo AND gc.Field1 ='K' AND ct.lcl_imports = TRUE;
  END IF;
  RETURN mRet ;
END MAIN$$

DELIMITER ;

-- Mantis Item# 15041  (Lcl Exports) by Kuppusamy on 09/11/2016.

ALTER TABLE `lcl_booking_export`   
     ADD COLUMN `no_bl_required` TINYINT(1) DEFAULT 0  NULL AFTER `aes`;


DELIMITER $$

DROP TRIGGER  `after_LclBookingExport_insert`$$

CREATE
    TRIGGER `after_LclBookingExport_insert` AFTER INSERT ON `lcl_booking_export` 
    FOR EACH ROW BEGIN
    DECLARE inserted_values TEXT ;
     IF NEW.no_bl_required IS NOT NULL THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'No B/L Required ',IF(NEW.no_bl_required=0,'was UnChecked','was Checked')));
     END IF;
     IF NEW.aes IS NOT NULL THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'AES By ECI',IF(NEW.aes=0,'No','Yes')));
     END IF;     
     IF NEW.ups IS NOT NULL THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Small Parcel',IF(NEW.ups=0,'No','Yes')));
     END IF;
     IF NEW.cfcl IS NOT NULL THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'CFCL',IF(NEW.cfcl=0,'No','Yes')));
     END IF;
   IF NEW.calc_heavy IS NOT NULL THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Calc Heavy/Dense/ExLen',IF(NEW.calc_heavy=0,'No','Yes')));
   END IF;
   IF NEW.storage_datetime IS NOT NULL THEN
     SET inserted_values=(SELECT concat_insert_values(inserted_values,'Storage Date',NEW.storage_datetime));
   END IF;
   
  IF inserted_values IS NOT NULL THEN
    SET inserted_values=CONCAT('INSERTED ->',inserted_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(NEW.file_number_id,'auto',inserted_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;

-- Mantis Item# 12341 (Lcl Exports) by Saravanan on 14/11/2016.
INSERT INTO property(NAME,VALUE,TYPE,description) VALUES('OlderVoyages','Number Of Older Voyages to be shown','LCL','Lcl Exports Older Voyages');

-- Mantis Item# 15401 (LCL Exports) by Aravindhan.v on 15/11/2016

CREATE TABLE `cust_reports_def` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `lob` enum('AIR','FCLE','FCLI','LCLE','LCLI') NOT NULL,
  `pgmkey` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `desc` text,
  `disabled` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cust_reports_def_uidx01` (`lob`,`pgmkey`),
  UNIQUE KEY `cust_reports_def_uidx02` (`lob`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `cust_report_opts`( 
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `reports_def_id` BIGINT UNSIGNED NOT NULL,
  `option` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `cust_report_opts_uidx01` (`reports_def_id`, `option`),
  CONSTRAINT `cust_report_opts_fk01` FOREIGN KEY (`reports_def_id`) REFERENCES `cust_reports_def`(`id`) ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE `cust_contact_reports` (
  `id` bigint(20) unsigned NOT NULL,
  `cust_contact_id` int(11) NOT NULL,
  `reports_def_id` bigint(20) unsigned NOT NULL,
  `reports_opts_id` bigint(20) unsigned DEFAULT NULL,
  `freq_min` varchar(50) NOT NULL DEFAULT '0',
  `freq_hour` varchar(50) NOT NULL DEFAULT '1',
  `freq_dom` varchar(50) NOT NULL DEFAULT '*',
  `freq_month` varchar(50) NOT NULL DEFAULT '*',
  `freq_dow` varchar(50) NOT NULL DEFAULT '*',
  `from_email` varchar(255) DEFAULT NULL,
  `disabled_at` datetime DEFAULT NULL,
  `lastran_at` datetime DEFAULT NULL,
  `entered_datetime` datetime NOT NULL,
  `modified_datetime` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cust_contact_reports_uidx01` (`cust_contact_id`,`reports_def_id`,`reports_opts_id`),
  KEY `cust_contact_reports_idx01` (`lastran_at`,`reports_def_id`),
  KEY `cust_contact_reports_fk02` (`reports_def_id`),
  KEY `cust_contact_reports_fk03` (`reports_opts_id`),
  CONSTRAINT `cust_contact_reports_fk01` FOREIGN KEY (`cust_contact_id`) REFERENCES `cust_contact` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `cust_contact_reports_fk02` FOREIGN KEY (`reports_def_id`) REFERENCES `cust_reports_def` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `cust_contact_reports_fk03` FOREIGN KEY (`reports_opts_id`) REFERENCES `cust_report_opts` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Mantis Item# 16201 (Lcl Exports) by Sathish on 16/11/2016.
ALTER TABLE role_duties ADD COLUMN default_noeei_low_val TINYINT(1) DEFAULT 0;

-- Mantis Item-10025(Logiware) by Stefy on 16 Nov 2016
ALTER TABLE cust_contact ADD COLUMN only_when_booking_contact_codeK TINYINT(1) DEFAULT 1,
ADD COLUMN applicable_to_all_shipments_codeK TINYINT(1) DEFAULT 0;

-- Mantis Item# 15413 (Lcl Exports) by Nambu on 16/11/2016.
INSERT INTO print_config(`screen_name`,`document_type`,`document_name`,`file_location`,`allow_print`)
 VALUES('LCLBL','IN','Unrated Bill of Lading (Original UNSIGNED)',NULL,'Yes');
  
-- Mantis Item: 10025 (Logiware) by Stefy on 18/11/2016.

DELIMITER $$

DROP FUNCTION IF EXISTS `CustomerGetIsNoCredit`$$

CREATE  FUNCTION `CustomerGetIsNoCredit`( pAcctNo VARCHAR(10) ) RETURNS TINYINT(1)
    READS SQL DATA
    DETERMINISTIC
MAIN: BEGIN
	DECLARE mReturnData BOOLEAN DEFAULT FALSE;
	
	IF ISNULL( pAcctNo ) = TRUE OR TRIM( pAcctNo ) = '' THEN
		RETURN FALSE;
	END IF; 
	
	SELECT 
	  IF(import_credit <> 'Y' OR `CodeGetDescById`(`credit_status`) NOT IN ('In Good Standing', 'Credit Hold')
	  , TRUE, FALSE)
	INTO
	  mReturnData
	FROM 
	  `cust_accounting`
	WHERE acct_no = pAcctNo;
	
	RETURN mReturnData;
END MAIN$$

DELIMITER ;

-- Mantis Item# 15413 (Lcl Exports) by Nambu on 21/11/2016.
UPDATE print_config SET DOCUMENT_NAME ='Bill of Lading (Original UNSIGNED)' WHERE
 Screen_Name ='lclbl' AND DOCUMENT_NAME ='Bill of Lading (Original SIGNED)';

-- Mantis Item# 0016577  (Lcl Exports) by Rajesh on 22/11/2016.
ALTER TABLE `lcl_bl_piece`   
  CHANGE `mark_no_desc` `mark_no_desc` TEXT CHARSET latin1 COLLATE latin1_swedish_ci NULL;

-- Mantis Item# 0016101  (Lcl Exports) by Kuppusamy on 22/11/2016.
ALTER TABLE `lcl_ss_exports`   
  ADD COLUMN `lcl_voyage_level_brand` VARCHAR(10) NULL AFTER `land_exit_date`;

--Mantis item 12341 (Lcl Exports) by priyanka on 22/11/2016
DELIMITER $$

DROP PROCEDURE IF EXISTS `LCLScheduleListShowOlder`$$

CREATE PROCEDURE `LCLScheduleListShowOlder`(
  IN pPOO_ID INT (11),
  IN pPOL_ID INT (11),
  IN pPOD_ID INT (11),
  IN pFD_ID INT (11),
  IN pTransMode VARCHAR (50),
  IN pServiceType VARCHAR(1),
  IN pLimitVoyage INT(4)
)
    READS SQL DATA
MAIN :
BEGIN
  DECLARE mSLS VARCHAR (64) DEFAULT "LCLScheduleListShowOlder" ;
  DECLARE mSLH VARCHAR (256) DEFAULT "" ;
  DECLARE mSLT VARCHAR (128) DEFAULT UUID() ;
  DECLARE mErrMsg VARCHAR (1024) DEFAULT "" ;
  DECLARE mDEBUG BOOL DEFAULT FALSE ;
  DECLARE mTransMode VARCHAR (50) DEFAULT "" ;
  SET mSLH = CONCAT(": pPOO_ID (",pPOO_ID,"), pPOL_ID (",pPOL_ID,"), pPOD_ID (",pPOD_ID,")") ;
  SET mSLH = CONCAT(mSLH,", pFD_ID (",pFD_ID,"), pTransMode (",pTransMode,"): ") ;
  IF ISNULL(pPOO_ID) OR pPOO_ID < 1 THEN
  SET mErrMsg = CONCAT("VALIDATION",mSLH,"Stopped; pPOO_ID invalid.") ;
  CALL LogSQLEntry (3, mSLS, mSLT, mErrMsg) ;
  SELECT mErrMsg ;
  LEAVE MAIN ;
  END IF ;
  IF ISNULL(pPOL_ID) OR pPOL_ID < 1 THEN
  SET mErrMsg = CONCAT("VALIDATION",mSLH,"Stopped; pPOL_ID invalid.") ;
  CALL LogSQLEntry (3, mSLS, mSLT, mErrMsg) ;
  SELECT mErrMsg ;
  LEAVE MAIN ;
  END IF ;
  IF ISNULL(pPOD_ID) OR pPOD_ID < 1 THEN
   SET mErrMsg = CONCAT("VALIDATION",mSLH,"Stopped; pPOD_ID invalid.") ;
  CALL LogSQLEntry (3, mSLS, mSLT, mErrMsg) ;
  SELECT mErrMsg ;
  LEAVE MAIN ;
  END IF ;
  IF ISNULL(pFD_ID) OR pFD_ID < 1 THEN
   SET mErrMsg = CONCAT("VALIDATION",mSLH,"Stopped; pFD_ID invalid.") ;
  CALL LogSQLEntry (3, mSLS, mSLT, mErrMsg) ;
  SELECT mErrMsg ;
  LEAVE MAIN ;
  END IF ;
  IF ISNULL(pTransMode) OR TRIM(pTransMode) = "" OR pTransMode = "*" THEN
  SET mTransMode = "A,R,T,V" ;
  ELSE
  SET mTransMode = pTransMode ;
  END IF ;
  IF mDEBUG = TRUE
  THEN CALL LogSQLEntry (1,mSLS,mSLT,CONCAT("DEBUG", mSLH, "Begins.")) ;
  END IF ;
  SELECT
    lsh.id AS ssHeaderId,
    lsh.schedule_no AS voyageNo,
    UnLocationGetCodeByID (lsh.origin_id) AS pooUnlocCode,
    UnLocationGetCodeByID (lsh.destination_id) AS fdUnloCode,
    (
			SELECT
				acct_name
			FROM
				trading_partner
			WHERE acct_no = lsd.sp_acct_no
			LIMIT 1
		) AS carrierName,
    lsd.sp_acct_no AS carrierAcctNo,
    lsd.sp_reference_no AS ssVoyage,
    lsd.sp_reference_name AS vesselName,
    UnLocationGetCodeByID (lsd.departure_id) AS polUnlocCode,
    UnLocationGetCodeByID (lsd.arrival_id) AS podUnloCode,
    UnLocationGetNameStateCntryByID (lsd.departure_id) AS departPier,
    lsd.std AS sailingDate,
    lsd.sta AS etaPod,
    lsd.relay_lrd_override AS relaylrdOverride,
    lsd.relay_tt_override AS relayttOverride,
    ScheduleKByUnlocation(lsd.departure_id) AS scheduleK,
    UnlocationGetNameScheduleKById(lsd.departure_id) AS departSched
  FROM
    lcl_ss_header lsh
    JOIN lcl_ss_detail lsd
			ON (
        lsd.ss_header_id = lsh.id
        AND lsd.trans_mode =pTransMode
      )
  WHERE lsh.service_type = pServiceType
    AND lsh.status <> 'V'
    AND
    (
			lsh.origin_id = pPOO_ID
			OR lsh.origin_id = pPOL_ID
		)
    AND
			(
				lsh.destination_id = pPOD_ID
				OR lsh.destination_id = pFD_ID
			)
			
    AND lsd.std > DATE_SUB(NOW(), INTERVAL pLimitVoyage DAY)
    ORDER BY lsd.std ASC;
    IF mDEBUG = TRUE THEN
      CALL LogSQLEntry (1,mSLS,mSLT,"DEBUG: Ends normally.") ;
    END IF ;
END MAIN$$

DELIMITER ;

-- Mantis Item# 16005  (Lcl Exports) by Kuppusamy on 23/11/2016.
ALTER TABLE `lcl_file_number`   
  CHANGE `business_unit` `business_unit` ENUM('ALL','ECI','ECU','OTI') CHARSET latin1 COLLATE latin1_swedish_ci NULL;

-- Mantis Item-10025(Logiware) by Stefy on 23 Nov 2016
INSERT INTO property(NAME,VALUE,TYPE,description) VALUES('ImportsCodeKContactId','mailer@docs.ecuworldwide.us','LCL','LCL Imports codeK FromAddress');

-- Mantis Item-10025(Logiware) by Stefy on 24 Nov 2016
UPDATE property SET NAME  = 'ImportsCodeKFromAddress' WHERE NAME = 'ImportsCodeKContactId';

-- Mantis Item-16661(Lcl Exports) by NambuRajasekar on 28 Nov 2016
ALTER TABLE `lcl_port_configuration` ADD COLUMN `bill_collects_fd_agent` TINYINT(1) DEFAULT 0  NULL AFTER `lock_port`;

-- Mantis Item-15993(Lcl Exports) by Sathish on 29/11/2016
ALTER TABLE role_duties ADD COLUMN default_docs_rcvd TINYINT(1) DEFAULT 0;

-- Mantis Item-15137(Logiware LCL Imports) by Stefy on 29 Nov 2016
ALTER TABLE `role_duties` ADD COLUMN `lcl_import_allow_transshipment` TINYINT(1) DEFAULT 0 NULL; 

-- Mantis Item-10025(Logiware) by Stefy on 29 Nov 2016

UPDATE genericcode_dup SET field1 = NULL WHERE codetypeid=22 AND field1='K' AND CODE NOT IN ('E','F');

-- Mantis Item-16593 (Lcl Exports) by NambuRajasekar on 29 Nov 2016

DELIMITER $$

DROP FUNCTION IF EXISTS `UnLocationGetNameByID`$$

CREATE FUNCTION `UnLocationGetNameByID`(pID INT (11)) RETURNS VARCHAR(100) CHARSET utf8
    READS SQL DATA
    DETERMINISTIC
MAIN :
BEGIN
  DECLARE mCode VARCHAR (100) DEFAULT NULL ;
   IF pID > 0
  THEN 
  SELECT 
    `un_loc_name` INTO mCode 
  FROM
    `un_location` 
  WHERE `id` = pID ;
  END IF ;
  RETURN mCode ;
END MAIN$$

DELIMITER $$

DROP FUNCTION IF EXISTS `TradingPartnerAcctName`$$

CREATE FUNCTION `TradingPartnerAcctName`(pAcctNo VARCHAR(15)) RETURNS VARCHAR(50) CHARSET utf8
    READS SQL DATA
    DETERMINISTIC
MAIN : BEGIN
    DECLARE mAcctName VARCHAR (50) DEFAULT NULL ;
    IF pAcctNo <> '' THEN
        SELECT 
          acct_name
        INTO 
          mAcctName 
        FROM
          `trading_partner`
        WHERE 
          `acct_no` = pAcctNo;
    END IF ;
    RETURN mAcctName ;
END MAIN$$

DELIMITER ;
-- Mantis Item-16593 (Lcl Exports) by NambuRajasekar on 1 Dec 2016
DELIMITER $$

DROP FUNCTION IF EXISTS `getStateCode`$$

CREATE  FUNCTION `getStateCode`(pId INT (11)) RETURNS VARCHAR(100) CHARSET latin1
    READS SQL DATA
    DETERMINISTIC
MAIN :
BEGIN
  DECLARE mReturnData VARCHAR (100) DEFAULT NULL ;
  IF pID > 0 
  THEN 
  SELECT 
    gen.`code` INTO mReturnData 
  FROM
    `genericcode_dup` gen 
    LEFT JOIN un_location un 
      ON un.statecode = gen.id 
  WHERE un.`id` = pId;
  END IF ;
  RETURN mReturnData ;
END MAIN$$

DELIMITER ;

DELIMITER $$

DROP FUNCTION IF EXISTS `getDisposion_UnLoc`$$

CREATE FUNCTION `getDisposion_UnLoc`(pId INT (22)) RETURNS VARCHAR(200) CHARSET latin1
    READS SQL DATA
    DETERMINISTIC
MAIN :
BEGIN
  DECLARE mReturnData VARCHAR (200) DEFAULT NULL ;
  IF pID > 0 
  THEN 
  SELECT 
    CONCAT_WS(
      '~~~',
      d.elite_code,
      d.`description`,
      p.unlocationcode,
      CONCAT(
        UPPER(un.un_loc_name),
        '/',
        IFNULL(UPPER(p.statecode), ''),
        '(',
        p.unlocationcode,
        ')'
      )
    ) INTO mReturnData 
  FROM
    lcl_booking_dispo lbd 
    LEFT JOIN disposition d 
      ON d.id = lbd.disposition_id 
    LEFT JOIN un_location un 
      ON lbd.un_location_id = un.id 
    LEFT JOIN ports p 
      ON un.un_loc_code = p.unlocationcode 
  WHERE lbd.file_number_id = pId 
  ORDER BY lbd.id DESC 
  LIMIT 1 ;
  END IF ;
  RETURN mReturnData ;
END MAIN$$

DELIMITER ;

/**
**This file is too big(>1MB). So, the content before September 2015 were deleted.
**/

-- by MEI at 6 oct 2015 (Only For LL)
DELETE FROM lcl_unit_ss_imports WHERE id=34651;

-- by Sathiyapriya on 08 OCT 2015

ALTER TABLE `lcl_booking_hazmat` CHANGE `inner_pkg_uom` `inner_pkg_uom` VARCHAR(10) CHARSET latin1 COLLATE latin1_swedish_ci NULL; 

ALTER TABLE `lcl_quote_hazmat` CHANGE `inner_pkg_uom` `inner_pkg_uom` VARCHAR(10) NULL; 

ALTER TABLE `lcl_bl_hazmat` CHANGE `inner_pkg_uom` `inner_pkg_uom` VARCHAR(10) CHARSET latin1 COLLATE latin1_swedish_ci NULL; 

--by sathiyapriya on 10 OCT 2015

ALTER TABLE `lcl_port_configuration` ADD COLUMN `bl_numbering` VARCHAR(3) NULL AFTER `originals_required`;

-- 13 OCT 2015 By Aravindhan.v
DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `lclUnitssDispo_beforeinsert`$$

CREATE  TRIGGER `lclUnitssDispo_beforeinsert` BEFORE INSERT ON `lcl_unit_ss_dispo` 
    FOR EACH ROW BEGIN
  DECLARE insert_values TEXT ;
  DECLARE dispos_insert_values TEXT ;
           
      SET @dispAvalCount= (SELECT COUNT(*) FROM lcl_unit_ss_dispo u JOIN disposition  d  ON  d.id = u.`disposition_id` 
      WHERE ss_detail_id = new.ss_detail_id AND  d.`elite_code` = 'AVAL');
      
      SET @dispoCount =(SELECT COUNT(*) FROM lcl_unit_ss_dispo u  WHERE u.ss_detail_id = new.ss_detail_id);
      SET @headerId = (SELECT ss_header_id FROM  lcl_ss_detail WHERE id = new.ss_detail_id);
      
      IF @headerId IS NOT NULL THEN
       
      SET @closeBy = (SELECT closed_by_user_id  FROM lcl_ss_header WHERE id = @headerId);
      SET @auditBy =(SELECT audited_by_user_id  FROM lcl_ss_header WHERE id  = @headerId);
      
      IF @dispAvalCount = 0 AND  @dispoCount >=1 AND @closeBy IS NULL AND @auditBy IS NULL THEN
        SET @unit_ss_id =(SELECT id  FROM lcl_unit_ss WHERE ss_header_id = @headerId LIMIT 1);
	INSERT INTO lcl_voyage_notification(unit_ss_id,MINUTE,Daily,entered_datetime)VALUES(@unit_ss_id,'Pending','Pending',NOW());
      END IF;   
   END IF;
END;
$$

DELIMITER ;

-- 14 OCT 2015 by SARAVANAN
ALTER TABLE `lcl_correction_charge` ADD COLUMN `lcl_bl_ac_id` BIGINT(20) NULL AFTER `lcl_booking_ac_id`;

-- 16 OCT 2015 by PAL RAJ

UPDATE job SET NAME='Imports Warehouse Rates via Strip Date' WHERE NAME='Warehouse Rate';

--18 OCT 2015 by Vellaisamy
DELIMITER $$
DROP PROCEDURE IF EXISTS `BL_LCLScheduleListUpComing`$$

CREATE PROCEDURE `BL_LCLScheduleListUpComing`(
	IN pPOO_ID INT(11),
	IN pPOL_ID INT(11),
	IN pPOD_ID INT(11),
	IN pFD_ID INT(11),
	IN pTransMode VARCHAR(50)
)
    READS SQL DATA
MAIN: BEGIN
	DECLARE mSLS VARCHAR(64) DEFAULT "LCLScheduleListUpComing";
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
    lcl_schedule.id,
    lcl_schedule.schedule_no,
    lcl_schedule.active,
    lcl_schedule.trans_mode,
    lcl_schedule.sp_acct_no,
    tp.acct_name AS sp_acct_name,
    lcl_schedule.sp_reference_no,
    lcl_schedule.sp_reference_name,
    lcl_schedule.pod_id,
    lcl_schedule.poo_lrdt,
    lcl_schedule.poo_pol_tt,
    lcl_schedule.poo_etd,
    lcl_schedule.poo_atd,
    lcl_schedule.pol_id AS `pod_id1`,
    lcl_schedule.pol_lrdt,
    lcl_schedule.pol_pod_tt,
    lcl_schedule.pol_pg,
    lcl_schedule.pol_etd,
    lcl_schedule.pol_atd,
    lcl_schedule.pod_id AS `pod_id2`,
    lcl_schedule.pod_eta,
    lcl_schedule.pod_ata,
    lcl_schedule.pod_pg,
    lcl_schedule.pod_fd_tt,
    lcl_schedule.fd_id,
    lcl_schedule.fd_eta,
    lcl_schedule.fd_ata,
    lcl_schedule.entered_datetime,
    lcl_schedule.entered_by_user_id,
    lcl_schedule.modified_datetime,
    lcl_schedule.modified_by_user_id,
    poo_unloc.un_loc_code AS `poo_code`,
    poo_unloc.un_loc_name AS `poo_name`,
    pol_unloc.un_loc_code AS `pol_code`,
    pol_unloc.un_loc_name AS `pol_name`,
    pod_unloc.un_loc_code AS `pod_code`,
    pod_unloc.un_loc_name AS `pod_name`,
    fd_unloc.un_loc_name AS `fd_code`,
    fd_unloc.un_loc_name AS `fd_name`,
    DATEDIFF( lcl_schedule.fd_eta, lcl_schedule.poo_lrdt ) AS `total_transit_time`,
    lcl_schedule.datasource
  FROM
    lcl_schedule,
    trading_partner tp,
    un_location poo_unloc,
    un_location pol_unloc,
    un_location pod_unloc,
    un_location fd_unloc
  WHERE lcl_schedule.poo_id = pPOO_ID
    AND lcl_schedule.pol_id = pPOL_ID
    AND lcl_schedule.pod_id = pPOD_ID
    AND lcl_schedule.fd_id = pFD_ID
    AND lcl_schedule.poo_lrdt >=DATE_SUB(NOW(),INTERVAL 60 DAY)
    AND tp.acct_no = lcl_schedule.sp_acct_no
    AND poo_unloc.id = lcl_schedule.poo_id
    AND pol_unloc.id = lcl_schedule.pol_id
    AND pod_unloc.id = lcl_schedule.pod_id
    AND fd_unloc.id = lcl_schedule.fd_id
  ORDER BY lcl_schedule.poo_lrdt ASC ;
	IF mDEBUG = TRUE THEN
		CALL LogSQLEntry( 1, mSLS, mSLT, "DEBUG: Ends normally." );
	END IF;
END MAIN$$

DELIMITER ;


--19 OCT 2015

 ALTER TABLE `role_duties` ADD COLUMN `lcl_exp_voyage_owner` TINYINT(1) DEFAULT 0 NULL AFTER `show_uncomplete_units`,
 ADD COLUMN `exp_delete_voyage` TINYINT(1) DEFAULT 0 NULL AFTER `lcl_exp_voyage_owner`; 
--19oct2015 Stefy Abraham
ALTER TABLE `lcl_booking_import` ADD COLUMN `door_delivery_eta` DATETIME NULL;

ALTER TABLE `lcl_quote_import` ADD COLUMN `door_delivery_eta` DATETIME NULL;

-- 26 OCT 2015 by SARAVANAN

INSERT INTO print_config(`screen_name`,`document_type`,`document_name`,`file_location`,`allow_print`)
VALUES('LCLUnits','IN','All House Bills of Lading','c:/LCL_Units/11.pdf','No');

INSERT INTO print_config(`screen_name`,`document_type`,`document_name`,`file_location`,`allow_print`)
VALUES('LCLUnits','IN','Prepaid Invoices','c:/LCL_Units/12.pdf','No');

INSERT INTO print_config(`screen_name`,`document_type`,`document_name`,`file_location`,`allow_print`)
VALUES('LCLUnits','IN','Collect Invoices','c:/LCL_Units/13.pdf','No');

--27Oct2015 Stefy Abraham
DELIMITER $$


DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBookingImport_update`$$

CREATE

    TRIGGER `after_LclBookingImport_update` AFTER UPDATE ON `lcl_booking_import`
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT ;
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
       SET updated_values=(SELECT concat_string(updated_values,'ETA at FD',IFNULL(DATE_FORMAT(old.fd_eta,'%d-%b-%Y'),''),IFNULL(DATE_FORMAT(new.fd_eta,'%d-%b-%Y'),'')));
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
    IF updated_values IS NOT NULL THEN
      SET updated_values=CONCAT(updated_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(OLD.file_number_id,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
  END;
$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclQuoteImport_update`$$
CREATE

    TRIGGER `after_LclQuoteImport_update` AFTER UPDATE ON `lcl_quote_import`

    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT ;
       IF ((OLD.door_delivery_eta IS NULL AND NEW.door_delivery_eta IS NOT NULL) OR NEW.door_delivery_eta != OLD.door_delivery_eta OR (OLD.door_delivery_eta IS NOT NULL AND NEW.door_delivery_eta IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'Door Delivery ETA',IFNULL(DATE_FORMAT(old.door_delivery_eta,'%d-%b-%Y'),''),IFNULL(DATE_FORMAT(new.door_delivery_eta,'%d-%b-%Y'),'')));
    END IF;
    IF updated_values IS NOT NULL THEN
      SET updated_values=CONCAT(updated_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(OLD.file_number_id,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END$$

DELIMITER ;

-- 27 OCT by Sathiya

ALTER TABLE `lcl_port_configuration`
 DROP COLUMN `express_release`, DROP COLUMN `originals_released_at_destination`, DROP COLUMN `originals_required`, 
DROP COLUMN `bl_numbering`; 

ALTER TABLE `un_location` ADD COLUMN `express_release` TINYINT(1) DEFAULT 0 NOT NULL AFTER `pier_pass`,
 ADD COLUMN `originals_released_at_destination` TINYINT(1) DEFAULT 0 NOT NULL AFTER `express_release`, 
ADD COLUMN `originals_required` TINYINT(1) DEFAULT 0 NOT NULL AFTER `originals_released_at_destination`,
 ADD COLUMN `bl_numbering` VARCHAR(1) NULL AFTER `originals_required`;

--27Oct2015 NambuRajasekar

ALTER TABLE `lcl_inbond`   
  ADD COLUMN `inbond_open_close` TINYINT(1) DEFAULT 1  NULL AFTER `inbond_datetime`,
  ADD COLUMN `eci_bond` TINYINT(1) DEFAULT 0  NULL AFTER `inbond_open_close`;

-- 30-OCT-2015 @ PAL RAJ

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_lcl_contact_update`$$

CREATE
   
    TRIGGER `after_lcl_contact_update` AFTER UPDATE ON `lcl_contact` 
    FOR EACH ROW BEGIN
    
	DECLARE updated_values TEXT;
	
         IF ((OLD.tp_acct_no IS NULL AND NEW.tp_acct_no IS NOT NULL) OR NEW.tp_acct_no != OLD.tp_acct_no OR (OLD.tp_acct_no IS NOT NULL AND NEW.tp_acct_no IS NULL)) THEN
        
	 SET @oldNotify2Values=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=OLD.tp_acct_no);
         SET @newNotify2Values=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=NEW.tp_acct_no);
         SET @notifyRemark =(SELECT IF(COUNT(*)>0,'true','false') FROM lcl_contact WHERE file_number_id = OLD.file_number_id AND OLD.remarks='Notify2');
         
         IF  @notifyRemark = 'true' THEN
         
         SET updated_values=(SELECT concat_string(updated_values,'2nd Notify Party',IFNULL(@oldNotify2Values,''),IFNULL(@newNotify2Values,'')));
         
         END IF;
         
        END IF;
        
        IF updated_values IS NOT NULL THEN           
		INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
		VALUES(OLD.file_number_id,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id); 
        END IF;
    END;
$$

DELIMITER ;

-- 03 Nov 2015 by Aravindhan.V

INSERT INTO codetype(description,editable) VALUES('Export Voyage Reason Codes',1);

INSERT INTO `genericcodelabels` (`codetypeid`, `label`, `field_type`, `field_name`, `field_id`, `max_length`, `style`, `style_class`, `javascript`, `options`) 
VALUES((SELECT codetypeid FROM codetype WHERE description='Export Voyage Reason Codes'),'Code','text','code','code','100',NULL,'textbox width-220px',NULL,NULL);

INSERT INTO `genericcodelabels` (`codetypeid`, `label`, `field_type`, `field_name`, `field_id`, `max_length`, `style`, `style_class`, `javascript`, `options`) 
VALUES((SELECT codetypeid FROM codetype WHERE description='Export Voyage Reason Codes'),'Description','textarea','codedesc','codedesc',NULL,NULL,'textarea width-320px height-80px','$(\"#codedesc\").limit(250);',NULL);

ALTER TABLE lcl_export_voyage_notification 
ADD COLUMN reason_code INT  AFTER voyage_change_reason,
ADD FOREIGN KEY lcl_export_voyage_notification_fk1(reason_code) REFERENCES `genericcode_dup`(id) ON UPDATE CASCADE;

DELIMITER $$

DROP FUNCTION IF EXISTS `LclContactGetEmailFaxByFileIdCodeI`$$

CREATE  FUNCTION `LclContactGetEmailFaxByFileIdCodeI`(fileId  BIGINT(20), emailCode  VARCHAR(5), faxCode VARCHAR(5),notificationId INT) RETURNS TEXT CHARSET utf8
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
	 UNION
	 (SELECT '#Econocaribe$Econocaribe Inc:voyagenotifications@econocaribe.com' AS address FROM lcl_export_voyage_notification notification WHERE notification.id=notificationId
	   AND  notification.internal_employees=1)
   )AS contact;
END IF;
  RETURN emailFaxList;
END MAIN$$

DELIMITER ;

-- 5 NOV 2015 by SARAVANAN

ALTER TABLE `lcl_booking_ac` ADD COLUMN `adjustment_comments` VARCHAR(100) DEFAULT '' NULL AFTER `post_ar`;
ALTER TABLE `lcl_correction` ADD COLUMN `current_profit` DECIMAL(10,2) DEFAULT 0.00 NULL AFTER `modified_by_user_id`,
ADD COLUMN `profit_aftercn` DECIMAL(10,2) DEFAULT 0.00 NULL AFTER `current_profit`;

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBookingAc_update`$$

CREATE
    /*!50017 DEFINER = 'root'@'localhost' */
    TRIGGER `after_LclBookingAc_update` AFTER UPDATE ON `lcl_booking_ac`
    FOR EACH ROW BEGIN
  DECLARE updatedValues TEXT DEFAULT '';
  DECLARE chargeValue BOOLEAN DEFAULT FALSE;
  DECLARE chageFlag BOOLEAN DEFAULT FALSE;
  DECLARE mFileNumberId BIGINT(20);
  DECLARE mShipmentType VARCHAR(4);
  DECLARE mBlNo VARCHAR(12);
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
      'auto',
      updatedValues,
      NOW(),
      new.modified_by_user_id,
      NOW(),
      new.modified_by_user_id
    ) ;
  END IF ;
  IF (new.ap_amount <> 0.00 AND new.`file_number_id` IS NOT NULL AND (isNotEqual(new.ap_amount, old.ap_amount) OR isNotEqual(new.`sp_acct_no`, old.`sp_acct_no`) OR isNotEqual(new.`invoice_number`, old.`invoice_number`)) AND new.ap_gl_mapping_id IS NOT NULL AND new.deleted = FALSE) THEN
    SELECT
      fn.`id`,
      IF(bk.`booking_type` = 'E', 'LCLE', 'LCLI') AS shipmentType,
      IF(bk.`booking_type` = 'E', CONCAT_WS('-', CONVERT(RIGHT(`UnLocationGetCodeByID` (ssh.`origin_id`), 3), CHARACTER CHARSET utf8), CONVERT(`UnLocationGetCodeByID` (ssh.`destination_id`), CHARACTER CHARSET utf8), CONVERT(fn.`file_number`, CHARACTER CHARSET utf8)), CONCAT('IMP-', fn.`file_number`)) AS blNo,
      fn.`file_number` AS drNo,
      us.`sp_booking_no` AS bookingNo,
      CONCAT_WS('-', CONVERT(ssh.`billing_trmnum`, CHARACTER CHARSET utf8), `UnLocationGetCodeByID` (ssh.`origin_id`), `UnLocationGetCodeByID` (ssh.`destination_id`), ssh.`schedule_no`) AS voyageNo,
      (SELECT u.`unit_no` FROM `lcl_unit` u WHERE u.`id` = us.`unit_id` LIMIT 1) AS containerNo,
      ssd.`sta` AS eta,
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
        ON (bp.`id` = bpu.`booking_piece_id`)
      LEFT JOIN `lcl_unit_ss` us
        ON (bpu.`lcl_unit_ss_id` = us.`id`)
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

-- 06 NOV 2015 by SARAVANAN

delete from print_config where screen_name = 'LCLUnits' and document_name = 'All House Bills of Lading';
delete from print_config where screen_name = 'LCLUnits' and document_name = 'Prepaid Invoices';
delete from print_config where screen_name = 'LCLUnits' and document_name = 'Collect Invoices';

-- 06 NOV 2015 by GOPAL
 INSERT INTO genericcode_dup (codetypeid,CODE,codedesc) VALUES((SELECT codetypeId FROM codetype WHERE description = 'Primary ClassCodes'),'2.3','2.3');

-- 13 NOV 2015 by SARAVANAN
ALTER TABLE `lcl_quote_ac` ADD COLUMN `adjustment_comments` VARCHAR(100) DEFAULT '' NULL AFTER `bill_charge`;


-- 13 Nov 2015 Namburajasekar
--- LOCAL AND ECONO ONLY
ALTER TABLE `lcl_inbond`   
  CHANGE `inbond_open_close` `inbond_open_close` TINYINT(1) DEFAULT 1  NOT NULL,
  CHANGE `eci_bond` `eci_bond` TINYINT(1) DEFAULT 0  NOT NULL;

-- 16 Nov 2015 by Aravindhan.V for Mantis Item# 7842

ALTER TABLE lcl_unit_ss ADD COLUMN received_master VARCHAR(20) DEFAULT NULL AFTER prepaid_collect;

INSERT INTO item_master (item_desc,program,item_created_on,unique_code) VALUES ('SS Masters Disputed', NULL, NOW(), 'LCLESSMAD');

INSERT INTO item_treestructure (item_id, parent_id) VALUES ( (SELECT item_id FROM item_master WHERE item_desc = 'SS Masters Disputed' AND unique_code = 'LCLESSMAD'
LIMIT 1), (SELECT item_id FROM item_master WHERE item_desc = 'LCL' AND unique_code = 'LCLE' LIMIT 1));

INSERT INTO item_master (item_desc,program,item_created_on,unique_code) VALUES ('SS MASTER DISPUTED BL','/lcl/ssMasterDisputedBl.do?methodName=display',NOW(),'SSMASEA');

INSERT INTO item_treestructure (item_id, parent_id) VALUES((SELECT item_id FROM item_master WHERE item_desc = 'SS MASTER DISPUTED BL' AND unique_code = 'SSMASEA'
LIMIT 1), (SELECT item_id FROM item_master WHERE item_desc = 'SS Masters Disputed' AND unique_code = 'LCLESSMAD' LIMIT 1));

INSERT INTO role_item_assoc (role_id,item_id,MODIFY,deleted) VALUES (1,(SELECT item_id FROM item_master WHERE item_desc = 'SS MASTER DISPUTED BL'
AND unique_code = 'SSMASEA' LIMIT 1), 'modify', 'delete');

INSERT INTO scan_config(screen_name, document_type, document_name, file_location) VALUES ('LCL SS MASTER BL', NULL, 'SS LINE MASTER BL', NULL);

-- 18 NOV 2015 @pal raj(Mantis Item:0008502)

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBookingPad_insert`$$

CREATE

    TRIGGER `after_LclBookingPad_insert` AFTER INSERT ON `lcl_booking_pad`
    FOR EACH ROW BEGIN
    DECLARE insert_values TEXT;


    IF NEW.issuing_terminal IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,' Issued By',(SELECT CONCAT(UPPER(login_name),'/',terminal_id) FROM user_details WHERE user_id=NEW.entered_by_user_id));
    END IF;

    IF NEW.pickup_contact_id IS NOT NULL THEN

       SELECT company_name,contact_name,city,address,zip,phone1,fax1,email1

       INTO @compName,@contName,@city,@address,@zip,@phone,@fax1,@email

       FROM lcl_contact WHERE id=NEW.pickup_contact_id;

       IF @city IS NOT NULL THEN
		SET insert_values = concat_insert_values(insert_values,', Door Origin/City/Zip',@city);
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

    IF NEW.delivery_reference_no IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,', Pickup#',NEW.delivery_reference_no);
    END IF;

    IF (insert_values IS NOT NULL) THEN
        SET insert_values=CONCAT('INSERTED ->',insert_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(NEW.file_number_id,'auto',insert_values,NOW(),NEW.entered_by_user_id,NOW(),NEW.modified_by_user_id);
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
	     VALUES(OLD.file_number_id,'auto',updated_values,NOW(),NEW.modified_by_user_id,NOW(),NEW.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;


DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBookingImport_update`$$

CREATE

    TRIGGER `after_LclBookingImport_update` AFTER UPDATE ON `lcl_booking_import`
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT ;
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
       SET updated_values=(SELECT concat_string(updated_values,'ETA at FD',IFNULL(DATE_FORMAT(old.fd_eta,'%d-%b-%Y'),''),IFNULL(DATE_FORMAT(new.fd_eta,'%d-%b-%Y'),'')));
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
       IF isNotEqual(OLD.door_delivery_status,NEW.door_delivery_status) THEN
       IF OLD.door_delivery_status ='P' &&  NEW.door_delivery_status ='F' THEN
       SET updated_values=(`concat_string`(updated_values,'Door Delivery Status','Pending(Cargo at CFS)','Final/Closed'));
       END IF;
       IF OLD.door_delivery_status ='P' &&  NEW.door_delivery_status ='O' THEN
       SET updated_values=(`concat_string`(updated_values,'Door Delivery Status','Pending(Cargo at CFS)','Out For Delivery'));
       END IF;
       IF OLD.door_delivery_status ='P' &&  NEW.door_delivery_status ='D' THEN
       SET updated_values=(`concat_string`(updated_values,'Door Delivery Status','Pending(Cargo at CFS)','Delivered'));
       END IF;

       IF OLD.door_delivery_status ='F' &&  NEW.door_delivery_status ='P' THEN
       SET updated_values=(`concat_string`(updated_values,'Door Delivery Status','Final/Closed','Pending(Cargo at CFS)'));
       END IF;
       IF OLD.door_delivery_status ='F' &&  NEW.door_delivery_status ='O' THEN
       SET updated_values=(`concat_string`(updated_values,'Door Delivery Status','Final/Closed','Out For Delivery'));
       END IF;
       IF OLD.door_delivery_status ='F' &&  NEW.door_delivery_status ='D' THEN
       SET updated_values=(`concat_string`(updated_values,'Door Delivery Status','Final/Closed','Delivered'));
       END IF;

       IF OLD.door_delivery_status ='D' &&  NEW.door_delivery_status ='O' THEN
       SET updated_values=(`concat_string`(updated_values,'Door Delivery Status','Delivered','Out For Delivery'));
       END IF;
       IF OLD.door_delivery_status ='D' &&  NEW.door_delivery_status ='P' THEN
       SET updated_values=(`concat_string`(updated_values,'Door Delivery Status','Delivered','Pending(Cargo at CFS)'));
       END IF;
       IF OLD.door_delivery_status ='D' &&  NEW.door_delivery_status ='F' THEN
       SET updated_values=(`concat_string`(updated_values,'Door Delivery Status','Delivered','Final/Closed'));
       END IF;
       END IF;
       IF isNotEqual(OLD.pod_signed_by,NEW.pod_signed_by) THEN
       SET updated_values=(`concat_string`(updated_values,'POD Signed',OLD.pod_signed_by,NEW.pod_signed_by));
       END IF;
         IF isNotEqual(OLD.pod_signed_datetime,NEW.pod_signed_datetime) THEN
       SET updated_values=(`concat_string`(updated_values,'POD Date',OLD.pod_signed_datetime,NEW.pod_signed_datetime));
       END IF;
       IF ((OLD.door_delivery_eta IS NULL AND NEW.door_delivery_eta IS NOT NULL) OR NEW.door_delivery_eta != OLD.door_delivery_eta OR (OLD.door_delivery_eta IS NOT NULL AND NEW.door_delivery_eta IS NULL)) THEN
       SET updated_values=(SELECT concat_string(updated_values,'Door Delivery ETA',IFNULL(DATE_FORMAT(old.door_delivery_eta,'%d-%b-%Y'),''),IFNULL(DATE_FORMAT(new.door_delivery_eta,'%d-%b-%Y'),'')));
    END IF;
    IF updated_values IS NOT NULL THEN
      SET updated_values=CONCAT('UPDATED ->',updated_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(OLD.file_number_id,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
  END;
$$

DELIMITER ;

-- 24 NOV 2015 @pal raj(Mantis Item:0008502)

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBookingPad_insert`$$

CREATE

    TRIGGER `after_LclBookingPad_insert` AFTER INSERT ON `lcl_booking_pad`
    FOR EACH ROW BEGIN
    DECLARE insert_values TEXT;

    SET @bookingType =(SELECT booking_type FROM lcl_booking  WHERE file_number_id=NEW.file_number_id LIMIT 1);

    IF NEW.issuing_terminal IS NOT NULL THEN
       SET insert_values = concat_insert_values(insert_values,' Issued By',(SELECT CONCAT(UPPER(login_name),'/',terminal_id) FROM user_details WHERE user_id=NEW.entered_by_user_id));
    END IF;
    IF NEW.pickup_contact_id IS NOT NULL THEN
       SELECT company_name,contact_name,city,address,zip,phone1,fax1,email1
       INTO @compName,@contName,@city,@address,@zip,@phone,@fax1,@email
       FROM lcl_contact WHERE id=NEW.pickup_contact_id;
       IF @city IS NOT NULL THEN
		SET insert_values = concat_insert_values(insert_values,', Door Origin/City/Zip',@city);
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

    IF (insert_values IS NOT NULL && @bookingType !='E') THEN
        SET insert_values=CONCAT('INSERTED ->',insert_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(NEW.file_number_id,'auto',insert_values,NOW(),NEW.entered_by_user_id,NOW(),NEW.modified_by_user_id);
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

    SET @bookingType =(SELECT booking_type FROM lcl_booking  WHERE file_number_id=OLD.file_number_id LIMIT 1);

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
    IF updated_values IS NOT NULL && @bookingType !='E' THEN
        SET updated_values=CONCAT('UPDATED ->',updated_values);
	     INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
	     VALUES(OLD.file_number_id,'auto',updated_values,NOW(),NEW.modified_by_user_id,NOW(),NEW.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;

-- 25 Nov 2015 by Aravindhan.V (Item No 8306)
INSERT INTO genericcode_dup(codetypeId,CODE,codedesc,field1) VALUES
((SELECT codetypeId FROM codetype  WHERE  description = 'Special Codes'),'E1','Email Every Day.','J'),
((SELECT codetypeId FROM codetype  WHERE  description = 'Special Codes'),'F1','Fax Every Day.','J'),
((SELECT codetypeId FROM codetype  WHERE  description = 'Special Codes'),'A1','Send Invoices.','J'); 

-- 26 NOV 2015 @pal raj(Mantis Item:0003409)

ALTER TABLE cust_contact
  ADD COLUMN applicable_to_all_shipments VARCHAR (1) NULL AFTER Extension,
  ADD COLUMN dr_from_codes VARCHAR (5) NULL AFTER applicable_to_all_shipments,
  ADD COLUMN non_neg_rate_bl_codes VARCHAR (5) NULL AFTER dr_from_codes,
  ADD COLUMN non_neg_unrated_bl_codes VARCHAR (5) NULL AFTER non_neg_rate_bl_codes,
  ADD COLUMN freight_invoice_code VARCHAR (5) NULL AFTER non_neg_unrated_bl_codes,
  ADD COLUMN confirm_on_board_code VARCHAR (5) NULL AFTER freight_invoice_code,
  ADD COLUMN rated_original_bl VARCHAR (5) NULL AFTER confirm_on_board_code,
  ADD COLUMN unrated_original_bl VARCHAR (5) NULL AFTER rated_original_bl ;

-- 26 NOV 2015 @pal raj(Mantis Item:0003193)

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBooking_update`$$

CREATE

    TRIGGER `after_LclBooking_update` AFTER UPDATE ON `lcl_booking`
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
    IF ((OLD.master_schedule_id IS NULL AND NEW.master_schedule_id IS NOT NULL) OR NEW.master_schedule_id!=OLD.master_schedule_id OR (OLD.master_schedule_id IS NOT NULL AND NEW.master_schedule_id IS NULL)) THEN
     SET @oldScheduleValue = (SELECT CONCAT (l.`schedule_no` ,' / ', DATE_FORMAT(l.`pol_etd`,'%d-%b-%Y'))  FROM lcl_schedule l  WHERE  l.id=old.master_schedule_id);
     SET @newScheduleValue = (SELECT CONCAT (l.`schedule_no` ,' / ', DATE_FORMAT(l.`pol_etd`,'%d-%b-%Y'))  FROM lcl_schedule l  WHERE  l.id=new.master_schedule_id);
     SET updated_values=(SELECT concat_string(updated_values,'Voyage is changed',IFNULL(@oldScheduleValue,''),IFNULL(@newScheduleValue,'')) );
    END IF;
    IF old.client_pwk_recvd!=new.client_pwk_recvd THEN
	SET updated_values = (SELECT concat_string(updated_values,'PWK/Docs Received',IF(old.client_pwk_recvd=0,'No','Yes'),IF(new.client_pwk_recvd=0,'No','Yes')));
    END IF;
    IF updated_values IS NOT NULL THEN
    SET updated_values=CONCAT(updated_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(OLD.file_number_id,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
      END;
$$

DELIMITER ;

-- 26 nov 2015 Stefy Abraham

ALTER TABLE `lcl_ss_detail` ADD COLUMN `doc_received` DATETIME NULL;

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclSsDetail_update`$$

CREATE

    TRIGGER `after_LclSsDetail_update` AFTER UPDATE ON `lcl_ss_detail`
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT ;
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
    IF updated_values IS NOT NULL THEN
    SET updated_values=CONCAT(updated_values);
        INSERT INTO lcl_ss_remarks(ss_header_id,TYPE,STATUS,remarks,followup_datetime,followup_user_id,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(old.ss_header_id,'auto','',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;

--26 nov 2015 Stefy Abraham

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
       
    IF updated_values IS NOT NULL THEN
      SET updated_values=CONCAT(updated_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(OLD.file_number_id,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    IF eta_at_fd IS NOT NULL THEN
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(OLD.file_number_id,'auto',eta_at_fd,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
  END;
$$

DELIMITER ;

-- 26 NOV 2015 @priyanka
alter table `terminal` add column `doc_dept_name` varchar(50) null after `doc_dept_email`;
alter table `terminal` add column `customer_service_dept_name` varchar(50) null after `doc_dept_name`;
alter table `terminal` add column `customer_service_dept_email` varchar(50) null after `customer_service_dept_name`;

--30Nov15 @Mei
INSERT INTO print_config(screen_name,document_type,document_name,file_location,allow_print)
VALUES('LCLBooking','IN','COB Notification','c:/LCL_Booking/2.pdf','No');
INSERT INTO print_config(`screen_name`,`document_type`,`document_name`,`file_location`,`allow_print`)
VALUES('LCLUnits','IN','Manifest Large Print Format','c:/LCL_Units/15.pdf','No');


-- 30 nov 2015 Stefy Abraham

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclSsDetail_update`$$

CREATE
        TRIGGER `after_LclSsDetail_update` AFTER UPDATE ON `lcl_ss_detail`
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT ;
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
        INSERT INTO lcl_ss_remarks(ss_header_id,TYPE,STATUS,remarks,followup_datetime,followup_user_id,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(old.ss_header_id,'auto','',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;

--Mei@2Dec
INSERT INTO print_config(`screen_name`,`document_type`,`document_name`,`file_location`,`allow_print`)
VALUES('LCLUnits','IN','B/L Rider Form','c:/LCL_Units/14.pdf','No');

-- 02 DEC 2015 by SARAVANAN
ALTER TABLE `lcl_booking_ac` ADD COLUMN `cost_flatrate_amount` DECIMAL(15,5) DEFAULT 0.00 NULL AFTER `adjustment_comments`;
ALTER TABLE `lcl_quote_ac`   ADD COLUMN `cost_flatrate_amount` DECIMAL(15,5) DEFAULT 0.00 NULL AFTER `adjustment_comments`;

-- 03 Dec 2015 @pal raj(Mantis Item:0003409)

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `cust_contact_update_trigger`$$

CREATE
    TRIGGER `cust_contact_update_trigger` AFTER UPDATE ON `cust_contact` 
    FOR EACH ROW BEGIN
	DECLARE lNote TEXT;
	DECLARE lUpdatedBy TEXT;
	
	SET lNote = buildUpdateNotes(lNote, 'First Name', old.`first_name`, new.`first_name`);
	SET lNote = buildUpdateNotes(lNote, 'Last Name', old.`last_name`, new.`last_name`);
	SET lNote = buildUpdateNotes(lNote, 'Position', old.`position`, new.`position`);
	SET lNote = buildUpdateNotes(lNote, 'Email', old.`email`, new.`email`);
	SET lNote = buildUpdateNotes(lNote, 'Phone', old.`phone`, new.`phone`);
	SET lNote = buildUpdateNotes(lNote, 'Extension', old.`extension`, new.`extension`);
	SET lNote = buildUpdateNotes(lNote, 'Fax', old.`fax`, new.`fax`);
	SET lNote = buildUpdateNotes(lNote, 'Code A', (SELECT `code` FROM `genericcode_dup` WHERE `id` = old.`code_a`), (SELECT `code` FROM `genericcode_dup` WHERE `id` = new.`code_a`));
	SET lNote = buildUpdateNotes(lNote, 'Code B', (SELECT `code` FROM `genericcode_dup` WHERE `id` = old.`code_b`), (SELECT `code` FROM `genericcode_dup` WHERE `id` = new.`code_b`));
	SET lNote = buildUpdateNotes(lNote, 'Code C', (SELECT `code` FROM `genericcode_dup` WHERE `id` = old.`code_c`), (SELECT `code` FROM `genericcode_dup` WHERE `id` = new.`code_c`));
	SET lNote = buildUpdateNotes(lNote, 'Code D', (SELECT `code` FROM `genericcode_dup` WHERE `id` = old.`code_d`), (SELECT `code` FROM `genericcode_dup` WHERE `id` = new.`code_d`));
	SET lNote = buildUpdateNotes(lNote, 'Code E', (SELECT `code` FROM `genericcode_dup` WHERE `id` = old.`code_e`), (SELECT `code` FROM `genericcode_dup` WHERE `id` = new.`code_e`));
	SET lNote = buildUpdateNotes(lNote, 'Code F', (SELECT `code` FROM `genericcode_dup` WHERE `id` = old.`code_f`), (SELECT `code` FROM `genericcode_dup` WHERE `id` = new.`code_f`));
	SET lNote = buildUpdateNotes(lNote, 'Code G', (SELECT `code` FROM `genericcode_dup` WHERE `id` = old.`code_g`), (SELECT `code` FROM `genericcode_dup` WHERE `id` = new.`code_g`));
	SET lNote = buildUpdateNotes(lNote, 'Code H', (SELECT `code` FROM `genericcode_dup` WHERE `id` = old.`code_h`), (SELECT `code` FROM `genericcode_dup` WHERE `id` = new.`code_h`));
	SET lNote = buildUpdateNotes(lNote, 'Code I', (SELECT `code` FROM `genericcode_dup` WHERE `id` = old.`code_i`), (SELECT `code` FROM `genericcode_dup` WHERE `id` = new.`code_i`));
	SET lNote = buildUpdateNotes(lNote, 'Code J', (SELECT `code` FROM `genericcode_dup` WHERE `id` = old.`code_j`), (SELECT `code` FROM `genericcode_dup` WHERE `id` = new.`code_j`));
	SET lNote = buildUpdateNotes(lNote, 'Comment', old.`comment`, new.`comment`);
	SET lNote = buildUpdateNotes(lNote, 'Applicable To All Shipments', old.`applicable_to_all_shipments`, new.`applicable_to_all_shipments`);
	SET lNote = buildUpdateNotes(lNote, 'Dr From Codes', old.`dr_from_codes`, new.`dr_from_codes`);
	SET lNote = buildUpdateNotes(lNote, 'Non Neg Rate B/L Codes', old.`non_neg_rate_bl_codes`, new.`non_neg_rate_bl_codes`);
	SET lNote = buildUpdateNotes(lNote, 'Non Neg Unrated B/L Codes', old.`non_neg_unrated_bl_codes`, new.`non_neg_unrated_bl_codes`);
	SET lNote = buildUpdateNotes(lNote, 'Freight Invoice Code', old.`freight_invoice_code`, new.`freight_invoice_code`);
	SET lNote = buildUpdateNotes(lNote, 'Confirm On Board Code', old.`confirm_on_board_code`, new.`confirm_on_board_code`);
	SET lNote = buildUpdateNotes(lNote, 'Rated Original B/L', old.`rated_original_bl`, new.`rated_original_bl`);
	SET lNote = buildUpdateNotes(lNote, 'Unrated Original B/L', old.`unrated_original_bl`, new.`unrated_original_bl`);
	IF (lNote <> '') THEN
	  SET lNote = CONCAT('CT: ', CONCAT_WS(' ', old.`first_name`, old.`last_name`), ', ', lNote);
	  SET lUpdatedBy = (CASE WHEN new.update_by <> '' THEN new.update_by ELSE 'System' END);
	  INSERT INTO notes (`module_id`, `item_name`, `module_ref_id`, `updatedate`, `note_type`, `status`, `note_desc`, `updated_by`) 
		VALUES ('TRADING_PARTNER', 'TRADING_PARTNER', new.`acct_no`, NOW(), 'auto', 'Pending', lNote, lUpdatedBy);
	END IF;		
	IF (new.update_by <> 'System' AND (isNotEqual(old.`first_name`, new.`first_name`) OR isNotEqual(old.`last_name`, new.`last_name`) OR isNotEqual(old.`position`, new.`position`) OR isNotEqual(old.`phone`, new.`phone`) OR isNotEqual(old.`fax`, new.`fax`) OR isNotEqual(old.`email`, new.`email`) OR isNotEqual(old.`comment`, new.`comment`) OR isNotEqual(old.`code_a`, new.`code_a`) OR isNotEqual(old.`code_b`, new.`code_b`) OR isNotEqual(old.`code_c`, new.`code_c`) OR isNotEqual(old.`code_d`, new.`code_d`) OR isNotEqual(old.`code_e`, new.`code_e`) OR isNotEqual(old.`code_f`, new.`code_f`) OR isNotEqual(old.`code_g`, new.`code_g`) OR isNotEqual(old.`code_h`, new.`code_h`) OR isNotEqual(old.`code_i`, new.`code_i`) OR isNotEqual(old.`code_j`, new.`code_j`))) THEN
	  UPDATE `trading_partner` tp SET tp.`enter_date` = CURRENT_DATE(), tp.`update_by` = new.`update_by` WHERE tp.`acct_no` = new.`acct_no`;
	END IF;
    END;
$$

DELIMITER ;

--03 Dec 2015 by sathiyapriya

DELIMITER $$

DROP TRIGGER `after_LclBl_update`$$

CREATE TRIGGER `after_LclBl_update` AFTER UPDATE ON `lcl_bl` 
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT ;
        IF ((old.ship_acct_no IS NULL AND new.ship_acct_no IS NOT NULL)OR new.ship_acct_no!=old.ship_acct_no OR(new.ship_acct_no IS NULL AND old.ship_acct_no IS NOT NULL))THEN
    SET @oldClientValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.ship_acct_no);
    SET @newClientValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.ship_acct_no);
    SET updated_values=(SELECT concat_string (updated_values,'Shipper',IFNULL(@oldClientValues,''),IFNULL(@newClientValues,'')));
    END IF;
     IF ((OLD.cons_acct_no IS NULL AND NEW.cons_acct_no IS NOT NULL) OR NEW.cons_acct_no != OLD.cons_acct_no OR (OLD.cons_acct_no IS NOT NULL AND NEW.cons_acct_no IS NULL)) THEN
     SET @oldConsValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.cons_acct_no);
     SET @newConsValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.cons_acct_no);
     SET updated_values=(SELECT concat_string(updated_values,'Consignee',IFNULL(@oldConsValues,''),IFNULL(@newConsValues,'')));
   END IF;
    IF ((OLD.noty_acct_no IS NULL AND NEW.noty_acct_no IS NOT NULL) OR NEW.noty_acct_no != OLD.noty_acct_no OR (OLD.noty_acct_no IS NOT NULL AND NEW.noty_acct_no IS NULL)) THEN
     SET @oldNotyValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.noty_acct_no);
     SET @newNotyValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.noty_acct_no);
     SET updated_values=(SELECT concat_string(updated_values,'Notify Party',IFNULL(@oldNotyValues,''),IFNULL(@newNotyValues,'')));
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
    IF new.entered_by_user_id!=old.entered_by_user_id THEN
       SET updated_values=(SELECT concat_string(updated_values,'Bl Owner',(SELECT login_name FROM user_details WHERE user_id=old.entered_by_user_id),(SELECT login_name FROM user_details WHERE user_id=new.entered_by_user_id)));
    END IF;
    IF old.poo_id!=new.poo_id THEN
	SET updated_values=(SELECT concat_string(updated_values,'PlaceOfReceipt',(SELECT un_loc_name FROM un_location WHERE id=old.poo_id),(SELECT un_loc_name FROM un_location WHERE id=new.poo_id)));
    END IF;
   IF old.fd_id!=new.fd_id THEN
	SET updated_values=(SELECT concat_string(updated_values,'Destination',(SELECT un_loc_name FROM un_location WHERE id=old.fd_id),(SELECT un_loc_name FROM un_location WHERE id=new.fd_id)));
    END IF;
          IF new.spot_rate!=old.spot_rate THEN
      SET updated_values=(SELECT concat_string(updated_values,'SpotRate',IF(old.spot_rate=0,'No','Yes'),IF(new.spot_rate=0,'No','Yes')));
    END IF;
    IF new.rate_type!=old.rate_type THEN
    SET updated_values=(SELECT concat_string(updated_values,'RateType',old.rate_type,new.rate_type));
    END IF;
    IF ((OLD.master_schedule_id IS NULL AND NEW.master_schedule_id IS NOT NULL) OR NEW.master_schedule_id!=OLD.master_schedule_id OR (OLD.master_schedule_id IS NOT NULL AND NEW.master_schedule_id IS NULL)) THEN
     SET @oldScheduleValue = (SELECT CONCAT (l.`schedule_no` ,' / ', DATE_FORMAT(l.`pol_etd`,'%d-%b-%Y'))  FROM lcl_schedule l  WHERE  l.id=old.master_schedule_id);
     SET @newScheduleValue = (SELECT CONCAT (l.`schedule_no` ,' / ', DATE_FORMAT(l.`pol_etd`,'%d-%b-%Y'))  FROM lcl_schedule l  WHERE  l.id=new.master_schedule_id);
     SET updated_values=(SELECT concat_string(updated_values,'Voyage is changed',IFNULL(@oldScheduleValue,''),IFNULL(@newScheduleValue,'')) );
    END IF;
    IF updated_values IS NOT NULL THEN
    SET updated_values=CONCAT('UPDATED ->',updated_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(OLD.file_number_id,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER `after_lcl_bl_piece_update`$$

CREATE TRIGGER `after_lcl_bl_piece_update` AFTER UPDATE ON `lcl_bl_piece` 
    FOR EACH ROW BEGIN
DECLARE updated_values TEXT ;
DECLARE change_value BOOLEAN ;
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
  SET updated_values=(SELECT CONCAT(IFNULL(updated_values,''),' Commodity Desc-->','Descriptions have been changed'));
        END IF;
       IF ((OLD.mark_no_desc IS NULL AND NEW.mark_no_desc IS NOT NULL) OR NEW.mark_no_desc != OLD.mark_no_desc OR (OLD.mark_no_desc IS NOT NULL AND NEW.mark_no_desc IS NULL)) THEN
         SET updated_values=CONCAT(IFNULL(updated_values,''),' Marks and Numbers-->','Marks have been changed');
       END IF;
IF  change_value=TRUE THEN
        SET @unit_ss_id=(SELECT lcl_unit_id FROM lcl_bl_piece_unit WHERE booking_piece_id=old.id);
        CALL UpdateUnitWMValue(@unit_ss_id);
        END IF;
        IF updated_values IS NOT NULL THEN
        SET updated_values=CONCAT('UPDATED ->',updated_values);
INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
VALUES(OLD.file_number_id,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
END IF;
        END;
$$


DELIMITER ;

-- 04 Dec 2015 @pal raj(Mantis Item:0003193)

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclQuote_update`$$

CREATE
    TRIGGER `after_LclQuote_update` AFTER UPDATE ON `lcl_quote` 
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
     IF updated_values IS NOT NULL THEN
    SET updated_values=CONCAT(updated_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(OLD.file_number_id,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    IF updated_value IS NOT NULL THEN
    SET updated_value=CONCAT(updated_value);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(OLD.file_number_id,'auto',updated_value,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
      END;
$$

DELIMITER ;


--04 Dec 2015 by sathiyapriya

DELIMITER $$

DROP TRIGGER  `lcl_bl_ac_insert_trigger`$$

CREATE TRIGGER `lcl_bl_ac_insert_trigger` AFTER INSERT ON `lcl_bl_ac` 
    FOR EACH ROW BEGIN
  DECLARE insert_values TEXT DEFAULT '';
  DECLARE changesFlag BOOLEAN DEFAULT FALSE;
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
      'auto',
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

DROP TRIGGER `after_lclblac_update`$$

CREATE TRIGGER `after_lclblac_update` AFTER UPDATE ON `lcl_bl_ac` 
    FOR EACH ROW BEGIN
  DECLARE updatedValues TEXT DEFAULT '';
  DECLARE chargeValue BOOLEAN DEFAULT FALSE;
  DECLARE chageFlag BOOLEAN DEFAULT FALSE;
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
  IF (new.adjustment_amount <> old.adjustment_amount) THEN
    SET updatedValues = concat_string(updatedValues, 'Adjustment Amount', old.adjustment_amount, new.adjustment_amount) ;
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
       SET updatedValues = CONCAT('UPDATED ->', updatedValues) ;
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
      'auto',
      updatedValues,
      NOW(),
      new.modified_by_user_id,
      NOW(),
      new.modified_by_user_id
    ) ;
  END IF;
  END;
$$

DELIMITER ;

--8Nov @Mei
DROP TABLE lcl_export;
ALTER TABLE lcl_booking_export CHANGE origin_whse_id origin_whse_id INT(11) NULL;
ALTER TABLE lcl_booking_export
ADD COLUMN calc_heavy TINYINT(1) NOT NULL DEFAULT '1' AFTER rt_agent_acct_no,
ADD COLUMN cfcl TINYINT(1) NOT NULL DEFAULT '0' COMMENT 'Consolidated FCL' AFTER calc_heavy,
ADD COLUMN cfcl_acct_no VARCHAR(10) DEFAULT NULL AFTER cfcl,
ADD CONSTRAINT lcl_booking_export_fk8 FOREIGN KEY (cfcl_acct_no) REFERENCES trading_partner(acct_no) ON UPDATE CASCADE;
--9Nov@Mei
UPDATE document_store_log d
JOIN lcl_unit lu ON lu.unit_no=d.Document_ID
JOIN lcl_unit_ss lus ON lus.unit_id=lu.id
JOIN lcl_ss_header lsh ON lsh.id=lus.ss_header_id
SET d.Document_ID= CONCAT(d.Document_ID,'--',lsh.schedule_no)
WHERE screen_name='LCL IMPORTS UNIT';

DROP TRIGGER after_LclExport_update;
DROP TRIGGER after_LclExport_insert;
DELIMITER $$
DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBookingExport_insert`$$
CREATE

    TRIGGER `after_LclBookingExport_insert` AFTER INSERT ON `lcl_booking_export`
    FOR EACH ROW BEGIN

    DECLARE inserted_values TEXT ;

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
  IF inserted_values IS NOT NULL THEN
    SET inserted_values=CONCAT('INSERTED ->',inserted_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(NEW.file_number_id,'auto',inserted_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;


DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBookingExport_update`$$

CREATE
    TRIGGER `after_LclBookingExport_update` AFTER UPDATE ON `lcl_booking_export`
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT ;
     IF isNotEqual(OLD.aes,NEW.aes)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'AES By ECI',IF(OLD.aes=0,'No','Yes'),IF(NEW.aes=0,'No','Yes')));
     END IF;
    IF isNotEqual(OLD.ups,NEW.ups)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Small Parcel',IF(OLD.ups=0,'No','Yes'),IF(NEW.ups=0,'No','Yes')));
     END IF;
     IF isNotEqual(OLD.cfcl,NEW.cfcl)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'CFCL',IF(OLD.cfcl=0,'No','Yes'),IF(NEW.cfcl=0,'No','Yes')));
     END IF;
   IF isNotEqual(OLD.calc_heavy,NEW.calc_heavy)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Calc Heavy/Dense/ExLen',IF(OLD.calc_heavy=0,'No','Yes'),IF(NEW.calc_heavy=0,'No','Yes')));
   END IF;
   IF ((old.cfcl_acct_no IS NULL AND new.cfcl_acct_no IS NOT NULL)OR new.cfcl_acct_no!=old.cfcl_acct_no OR(new.cfcl_acct_no IS NULL AND old.cfcl_acct_no IS NOT NULL))THEN
    SET @oldCfclValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.cfcl_acct_no);
    SET @newCfclValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.cfcl_acct_no);
    SET updated_values=(SELECT concat_string (updated_values,'CFCL Acct',IFNULL(@oldCfclValues,''),IFNULL(@newCfclValues,'')));
    END IF;
  IF updated_values IS NOT NULL THEN
    SET updated_values=CONCAT('UPDATED ->',updated_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(OLD.file_number_id,'auto',updated_values,NOW(),NEW.modified_by_user_id,NOW(),NEW.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_lclUnitssDispo_insert`$$

CREATE
    /*!50017 DEFINER = 'root'@'localhost' */
    TRIGGER `after_lclUnitssDispo_insert` AFTER INSERT ON `lcl_unit_ss_dispo`
    FOR EACH ROW BEGIN
    DECLARE inserted_values TEXT;
    SELECT disposition_id INTO @oldDispoId FROM lcl_unit_ss_dispo WHERE id=(SELECT id FROM lcl_unit_ss_dispo WHERE unit_id=new.unit_id AND ss_detail_id = new.ss_detail_id ORDER BY id DESC LIMIT 1,1);
      IF NEW.disposition_id IS NOT NULL AND @oldDispoId!=NEW.disposition_id THEN
    	SET inserted_values = concat_string(inserted_values,' Disposition',(SELECT elite_code FROM disposition WHERE id=@oldDispoId),(SELECT elite_code FROM disposition WHERE id=new.disposition_id));
    	ELSE
    	SET inserted_values = concat_insert_values(inserted_values,' Disposition',(SELECT elite_code FROM disposition WHERE id=new.disposition_id));
    END IF;
    IF inserted_values IS NOT NULL THEN
    	SET @ss_header_id=(SELECT ss_header_id FROM lcl_ss_detail WHERE id=new.ss_detail_id );
    	SET inserted_values=CONCAT('INSERTED ->',inserted_values);
	INSERT INTO lcl_unit_ss_remarks(unit_id,ss_header_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
         VALUES(new.unit_id,@ss_header_id,'auto',inserted_values,NOW(),new.entered_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
      END;
$$

DELIMITER ;

-- 10 Dec 2015 @pal raj

UPDATE lcl_booking_ac SET cost_flatrate_amount=ap_amount WHERE ap_amount!=0.00;
UPDATE lcl_quote_ac SET cost_flatrate_amount=ap_amount WHERE ap_amount!=0.00;
    
--- 11 DEC by Aravindhan.V
insert into property(name,value,type,description) values
('Sell Profit (%)',15,'LCL','Sell Profit For Export'),
('Sell Minimum',50,'LCL','Sell Minimum For Export'),
('Sell Maximum',200,'LCL','Sell Maximum For Export');
alter table lcl_booking modify column pod_fd_tt int UNSIGNED;
alter table lcl_quote add column pod_fd_tt int unsigned default null after non_rated;

-- 11 Dec 2015 @pal raj

ALTER TABLE `lcl_booking_export` 
  ADD COLUMN `storage_datetime` DATETIME NULL AFTER `released_user_id`;


DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBookingExport_insert`$$

CREATE

    TRIGGER `after_LclBookingExport_insert` AFTER INSERT ON `lcl_booking_export` 
    FOR EACH ROW BEGIN
    DECLARE inserted_values TEXT ;
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


DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBookingExport_update`$$

CREATE
   
    TRIGGER `after_LclBookingExport_update` AFTER UPDATE ON `lcl_booking_export` 
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT ;
     IF isNotEqual(OLD.aes,NEW.aes)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'AES By ECI',IF(OLD.aes=0,'No','Yes'),IF(NEW.aes=0,'No','Yes')));
     END IF;
    IF isNotEqual(OLD.ups,NEW.ups)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Small Parcel',IF(OLD.ups=0,'No','Yes'),IF(NEW.ups=0,'No','Yes')));
     END IF;
     IF isNotEqual(OLD.cfcl,NEW.cfcl)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'CFCL',IF(OLD.cfcl=0,'No','Yes'),IF(NEW.cfcl=0,'No','Yes')));
     END IF;
   IF isNotEqual(OLD.calc_heavy,NEW.calc_heavy)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Calc Heavy/Dense/ExLen',IF(OLD.calc_heavy=0,'No','Yes'),IF(NEW.calc_heavy=0,'No','Yes')));
   END IF;
   IF ((old.cfcl_acct_no IS NULL AND new.cfcl_acct_no IS NOT NULL)OR new.cfcl_acct_no!=old.cfcl_acct_no OR(new.cfcl_acct_no IS NULL AND old.cfcl_acct_no IS NOT NULL))THEN
    SET @oldCfclValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.cfcl_acct_no);
    SET @newCfclValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.cfcl_acct_no);
    SET updated_values=(SELECT concat_string (updated_values,'CFCL Acct',IFNULL(@oldCfclValues,''),IFNULL(@newCfclValues,'')));
    END IF;
    IF isNotEqual(OLD.storage_datetime,NEW.storage_datetime)  THEN
     SET updated_values=(SELECT concat_string(updated_values,'Storage Date',OLD.storage_datetime,NEW.storage_datetime));
     END IF;
  IF updated_values IS NOT NULL THEN
    SET updated_values=CONCAT('UPDATED ->',updated_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(OLD.file_number_id,'auto',updated_values,NOW(),NEW.modified_by_user_id,NOW(),NEW.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;

INSERT INTO `property` (NAME,VALUE,TYPE,description) VALUES ('FreightForwarderAccountNo','NOFFAA0001,NOFRTA0001','LCL','Freight Forwarder AccountNo');

--by sathiyapriya on 11 Dec 2015
ALTER TABLE `lcl_port_configuration` ADD COLUMN `asetup` VARCHAR(1) NULL AFTER `frm`,   
ADD COLUMN `asetup_account` VARCHAR(5) NULL AFTER `asetup`, ADD COLUMN `ac_acct_pickup` VARCHAR(5) NULL AFTER `asetup_account`, 
ADD COLUMN `domest` VARCHAR(1) NULL AFTER `ac_acct_pickup`, ADD COLUMN `haz_all` VARCHAR(1) NULL AFTER `domest`,
 ADD COLUMN `ftf_weight` INT(6) NULL AFTER `haz_all`, ADD COLUMN `ftf_measur` INT(6) NULL AFTER `ftf_weight`, 
ADD COLUMN `ftf_minimm` INT(6) NULL AFTER `ftf_measur`; 

-- 12 Dec 2015 @pal raj

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclQuote_insert`$$

CREATE
   
    TRIGGER `after_LclQuote_insert` AFTER INSERT ON `lcl_quote` 
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
        VALUES(NEW.file_number_id,'auto',insert_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclQuote_update`$$

CREATE
   
    TRIGGER `after_LclQuote_update` AFTER UPDATE ON `lcl_quote` 
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
        VALUES(OLD.file_number_id,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
      END;
$$

DELIMITER ;

--by gopal on 13 Dec 2015
INSERT INTO `property` (NAME,VALUE,TYPE,description) VALUES ('PickupVendor','CTSFRE0004','LCL','LCL Export PickupVendor');

-- 14 Dec 2015 @pal raj
ALTER TABLE user_details 
  ADD COLUMN warehouse_emplid VARCHAR(6) NULL AFTER warehouse,
  ADD UNIQUE INDEX user_details_uidx02 (warehouse_emplid);
--14 Dec@Mei @Mantis#9282
ALTER TABLE  lcl_booking DROP FOREIGN KEY `lcl_booking_fk6`, DROP INDEX lcl_booking_fk6, DROP COLUMN `master_schedule_id`;
ALTER TABLE `lcl_booking`
    ADD COLUMN `booked_ss_header_id` BIGINT(20) UNSIGNED NULL AFTER `fd_id`,
    ADD  INDEX `lcl_booking_fk6` (`booked_ss_header_id`),
    ADD CONSTRAINT `lcl_booking_fk6` FOREIGN KEY (`booked_ss_header_id`)
	REFERENCES `lcl_ss_header`(`id`) ON UPDATE CASCADE;
ALTER TABLE  lcl_bl DROP FOREIGN KEY `lcl_bl_fk6`, DROP INDEX lcl_bl_fk6, DROP COLUMN `master_schedule_id`;
ALTER TABLE `lcl_bl`
    ADD COLUMN `booked_ss_header_id` BIGINT(20) UNSIGNED NULL AFTER `fd_id`,
    ADD  INDEX `lcl_bl_fk6` (`booked_ss_header_id`),
    ADD CONSTRAINT `lcl_bl_fk6` FOREIGN KEY (`booked_ss_header_id`)
	REFERENCES `lcl_ss_header`(`id`) ON UPDATE CASCADE;

DELIMITER $$

DROP PROCEDURE IF EXISTS `LCLRelayFind`$$

CREATE  PROCEDURE `LCLRelayFind`( IN pPOO_ID INT(11), IN pFD_ID INT(11),IN relay_flag BOOL)
    READS SQL DATA
MAIN: BEGIN

	DECLARE mSLS VARCHAR(64) DEFAULT "LCLRelayFind";
	DECLARE mSLH VARCHAR(256) DEFAULT "";
	DECLARE mSLT VARCHAR(128) DEFAULT UUID();
	DECLARE mErrMsg VARCHAR(1024) DEFAULT "";
	DECLARE mDEBUG BOOL DEFAULT FALSE;

	SET mSLH = CONCAT( ": pPOO_ID (", pPOO_ID, "), pFD_ID (", pFD_ID, "): " );
	IF ISNULL( pPOO_ID ) OR pPOO_ID < 1 THEN
		SET mErrMsg =  CONCAT( "VALIDATION", mSLH, "Stopped; pPOO_ID invalid." ) ;
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

	IF mDEBUG = TRUE THEN
		CALL LogSQLEntry( 1, mSLS, mSLT, CONCAT( "DEBUG", mSLH, "Begins." ) );
	END IF;

	SELECT
		lcl_relay_poo.poo_id AS `poo_id`,
		poo_unloc.un_loc_code AS `poo_code`,
		poo_unloc.un_loc_name AS `poo_name`,
		lcl_relay_poo.co_dow AS `poo_co_dow`,
		lcl_relay_poo.co_tod AS `poo_co_tod`,
		lcl_relay_poo.transit_time  AS `poo_transit_time`,
		lcl_relay.pol_id AS `pol_id`,
		pol_unloc.un_loc_code AS `pol_code`,
		pol_unloc.un_loc_name AS `pol_name`,
		lcl_relay.co_dbd AS `pol_co_dbd`,
		lcl_relay.co_dow AS `pol_co_dow`,
		lcl_relay.co_tod AS `pol_co_tod`,
		lcl_relay.transit_time  AS `pol_transit_time`,
		lcl_relay.pod_id AS `pod_id`,
		pod_unloc.un_loc_code AS `pod_code`,
		pod_unloc.un_loc_name AS `pod_name`,
		lcl_relay_fd.fd_id AS `fd_id`,
		fd_unloc.un_loc_code AS `fd_code`,
		fd_unloc.un_loc_name AS `fd_name`,
		lcl_relay_fd.transit_time  AS `fd_transit_time`,
		(
			lcl_relay_poo.transit_time +
			lcl_relay.co_dbd +
			lcl_relay.transit_time +
			lcl_relay_fd.transit_time
		) AS `total_transit_time`
	FROM lcl_relay_poo, lcl_relay_fd, lcl_relay, un_location poo_unloc, un_location pol_unloc, un_location pod_unloc, un_location fd_unloc
	WHERE lcl_relay_poo.poo_id = pPOO_ID
	AND lcl_relay_poo.active = TRUE
	AND lcl_relay_fd.fd_id = pFD_ID
	AND lcl_relay_fd.active = TRUE
	AND lcl_relay_fd.relay_id = lcl_relay_poo.relay_id
	AND lcl_relay.id = lcl_relay_poo.relay_id
        AND IF(relay_flag, relay_flag, lcl_relay.active) = TRUE
	AND NOT EXISTS (
		SELECT lcl_relay_exception.id
		FROM lcl_relay_exception
		WHERE lcl_relay_exception.poo_id = lcl_relay_poo.poo_id
		AND  lcl_relay_exception.pol_id = lcl_relay.pol_id
		AND  lcl_relay_exception.pod_id = lcl_relay.pod_id
		AND  lcl_relay_exception.fd_id = lcl_relay_fd.fd_id
		AND lcl_relay_exception.active = TRUE
	)
	AND poo_unloc.id = lcl_relay_poo.poo_id
	AND pol_unloc.id = lcl_relay.pol_id
	AND pod_unloc.id = lcl_relay.pod_id
	AND fd_unloc.id = lcl_relay_fd.fd_id
	LIMIT 1;

	IF mDEBUG = TRUE THEN
		CALL LogSQLEntry( 1, mSLS, mSLT, "DEBUG: Ends normally." );
	END IF;

END MAIN$$

DELIMITER ;

DELIMITER $$

DROP PROCEDURE IF EXISTS `LCLScheduleListUpComing`$$

CREATE  PROCEDURE `LCLScheduleListUpComing`(
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
  WHERE lsh.service_type IN ('E', 'C')
    AND lsh.origin_id = pPOO_ID
    AND lsd.departure_id = pPOL_ID
    AND lsd.arrival_id = pPOD_ID
    AND lsh.destination_id = pFD_ID
    ORDER BY lsh.id DESC;
    IF mDEBUG = TRUE THEN
      CALL LogSQLEntry (1,mSLS,mSLT,"DEBUG: Ends normally.") ;
    END IF ;
END MAIN$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBooking_update`$$

CREATE
    TRIGGER `after_LclBooking_update` AFTER UPDATE ON `lcl_booking`
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
        VALUES(OLD.file_number_id,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
      END;
$$

DELIMITER ;


DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclBl_update`$$

CREATE
    TRIGGER `after_LclBl_update` AFTER UPDATE ON `lcl_bl`
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT ;
        IF ((old.ship_acct_no IS NULL AND new.ship_acct_no IS NOT NULL)OR new.ship_acct_no!=old.ship_acct_no OR(new.ship_acct_no IS NULL AND old.ship_acct_no IS NOT NULL))THEN
    SET @oldClientValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.ship_acct_no);
    SET @newClientValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.ship_acct_no);
    SET updated_values=(SELECT concat_string (updated_values,'Shipper',IFNULL(@oldClientValues,''),IFNULL(@newClientValues,'')));
    END IF;
     IF ((OLD.cons_acct_no IS NULL AND NEW.cons_acct_no IS NOT NULL) OR NEW.cons_acct_no != OLD.cons_acct_no OR (OLD.cons_acct_no IS NOT NULL AND NEW.cons_acct_no IS NULL)) THEN
     SET @oldConsValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.cons_acct_no);
     SET @newConsValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.cons_acct_no);
     SET updated_values=(SELECT concat_string(updated_values,'Consignee',IFNULL(@oldConsValues,''),IFNULL(@newConsValues,'')));
   END IF;
    IF ((OLD.noty_acct_no IS NULL AND NEW.noty_acct_no IS NOT NULL) OR NEW.noty_acct_no != OLD.noty_acct_no OR (OLD.noty_acct_no IS NOT NULL AND NEW.noty_acct_no IS NULL)) THEN
     SET @oldNotyValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=old.noty_acct_no);
     SET @newNotyValues=(SELECT CONCAT(acct_name,'(',acct_no,')') FROM trading_partner WHERE acct_no=new.noty_acct_no);
     SET updated_values=(SELECT concat_string(updated_values,'Notify Party',IFNULL(@oldNotyValues,''),IFNULL(@newNotyValues,'')));
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
    IF new.entered_by_user_id!=old.entered_by_user_id THEN
       SET updated_values=(SELECT concat_string(updated_values,'Bl Owner',(SELECT login_name FROM user_details WHERE user_id=old.entered_by_user_id),(SELECT login_name FROM user_details WHERE user_id=new.entered_by_user_id)));
    END IF;
    IF old.poo_id!=new.poo_id THEN
	SET updated_values=(SELECT concat_string(updated_values,'PlaceOfReceipt',(SELECT un_loc_name FROM un_location WHERE id=old.poo_id),(SELECT un_loc_name FROM un_location WHERE id=new.poo_id)));
    END IF;
   IF old.fd_id!=new.fd_id THEN
	SET updated_values=(SELECT concat_string(updated_values,'Destination',(SELECT un_loc_name FROM un_location WHERE id=old.fd_id),(SELECT un_loc_name FROM un_location WHERE id=new.fd_id)));
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
    END IF;
    IF updated_values IS NOT NULL THEN
    SET updated_values=CONCAT('UPDATED ->',updated_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(OLD.file_number_id,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;

--14 dec 2015 stefy Abraham (only for local and econo)

ALTER TABLE `lcl_unit_ss_manifest`
  ADD COLUMN `manifested_datetime` DATETIME NULL AFTER `override_dr_count`,
  ADD COLUMN `manifested_by_user_id` INT(11) NULL AFTER `manifested_datetime`,
  ADD INDEX `lcl_unit_ss_manifest_fk5` (`manifested_by_user_id`),
  ADD INDEX `lcl_unit_ss_manifest_idx01` (`manifested_datetime`),
  ADD CONSTRAINT `lcl_unit_ss_manifest_fk5` FOREIGN KEY (`manifested_by_user_id`)
   REFERENCES `user_details`(`user_id`) ON UPDATE CASCADE;

-- 15 dec 2015 sathiyapriya

DELIMITER $$

DROP TRIGGER `after_lcl_bl_piece_update`$$

CREATE TRIGGER `after_lcl_bl_piece_update` AFTER UPDATE ON `lcl_bl_piece` 
    FOR EACH ROW BEGIN
DECLARE updated_values TEXT ;
DECLARE change_value BOOLEAN ;
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
IF  change_value=TRUE THEN
        SET @unit_ss_id=(SELECT lcl_unit_id FROM lcl_bl_piece_unit WHERE booking_piece_id=old.id);
        CALL UpdateUnitWMValue(@unit_ss_id);
        END IF;
IF  change_value=TRUE THEN
        SET @unit_ss_id=(SELECT lcl_unit_id FROM lcl_bl_piece_unit WHERE booking_piece_id=old.id);
        CALL UpdateUnitWMValue(@unit_ss_id);
        END IF;
        IF updated_values IS NOT NULL THEN
        SET updated_values=CONCAT('UPDATED ->',updated_values);
INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
VALUES(OLD.file_number_id,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
END IF;
        END;
$$

DELIMITER ;


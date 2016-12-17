-- 26 Nov 2013 @ VinothS
-- Quote Module Insert Triggers

DELIMITER $$

DROP TRIGGER  lcl_quote_insert_trigger$$

CREATE  TRIGGER lcl_quote_insert_trigger AFTER INSERT ON lcl_quote
    FOR EACH ROW BEGIN
    DECLARE insert_values TEXT;
     IF new.poo_id!='' THEN
	SET insert_values = concat_insert_values(insert_values,' Origin CFS',(SELECT un_loc_name FROM un_location WHERE id=new.poo_id));
     END IF;
    
    IF new.pol_id!='' THEN
	SET insert_values = concat_insert_values(insert_values,', POL',(SELECT un_loc_name FROM un_location WHERE id=new.pol_id));
     END IF;
     
     IF new.pod_id!='' THEN
	SET insert_values = concat_insert_values(insert_values,', POD',(SELECT un_loc_name FROM un_location WHERE id=new.pod_id));
     END IF;
     
      IF new.fd_id!='' THEN
	SET insert_values = concat_insert_values(insert_values,', Destination',(SELECT un_loc_name FROM un_location WHERE id=new.fd_id));
     END IF;
      
     IF new.rate_type!='' THEN
	SET insert_values = concat_insert_values(insert_values,', Rate Type',new.rate_type);
     END IF;
     
	SET insert_values = concat_insert_values(insert_values,', Pickup',IF(new.poo_door=0,'No','Yes'));
    
	SET insert_values = concat_insert_values(insert_values,', Relay Ovr',IF(new.relay_override=0,'No','Yes')); 
     
     IF new.poo_whse_contact_id!='' THEN
	SET insert_values = concat_insert_values(insert_values,', Delivery Cargo to',(SELECT CONCAT(company_name,' / ',address) FROM lcl_contact WHERE id=new.poo_whse_contact_id));
     END IF;
     
     IF new.billing_terminal!='' THEN
	SET insert_values = concat_insert_values(insert_values,', Term to do BL',(SELECT terminal_location FROM terminal WHERE trmnum=new.billing_terminal));
     END IF;
      
	SET insert_values = concat_insert_values(insert_values,', Billing Type',new.billing_type);
     
	SET insert_values = concat_insert_values(insert_values,', Insurance',IF(new.insurance=0,'No','Yes'));
     
     IF new.value_of_goods!='' THEN
	SET insert_values = concat_insert_values(insert_values,', Value of Goods',new.value_of_goods);
     END IF;
    
	SET insert_values = concat_insert_values(insert_values,', Delivery Metro PRSJU',new.delivery_metro);
     
	SET insert_values = concat_insert_values(insert_values,', Documentation',IF(new.documentation=0,'No','Yes'));
      
	SET insert_values = concat_insert_values(insert_values,', Spot Rate',IF(new.spot_rate=0,'No','Yes'));
	
	SET insert_values = concat_insert_values(insert_values,', Unknown Dest',IF(new.non_rated=0,'No','Yes'));

	SET insert_values = concat_insert_values(insert_values,', Quote Completed',IF(new.quote_complete=0,'No','Yes'));
	
     	SET insert_values = concat_insert_values(insert_values,', Default Agent',IF(new.default_agent=0,'No','Yes'));
	
     IF new.agent_acct_no!='' THEN
	SET insert_values = concat_insert_values(insert_values,', Agent Acct No#',new.agent_acct_no);
	SET insert_values = concat_insert_values(insert_values,', Agent Acct Name',(SELECT acct_name FROM trading_partner WHERE acct_no=new.agent_acct_no));
     END IF;
	
	SET insert_values = concat_insert_values(insert_values,', ERT',IF(new.rtd_transaction=0,'No','Yes'));
	
     IF new.rtd_agent_acct_no!='' THEN
	SET insert_values = concat_insert_values(insert_values,', Agent Info',new.rtd_agent_acct_no);
     END IF;
     
	SET insert_values = concat_insert_values(insert_values,', PWK',IF(new.client_pwk_recvd=0,'No','Yes'));
	
     IF new.bill_to_party!='' THEN
	SET insert_values = concat_insert_values(insert_values,', Bill To Party',new.bill_to_party);
     END IF; 
	
     IF new.third_party_acct_no!='' THEN
	SET insert_values = concat_insert_values(insert_values,', Third Party Acct No#',new.third_party_acct_no);
	SET insert_values = concat_insert_values(insert_values,', Third Party Acct',(SELECT acct_name FROM trading_partner WHERE acct_no=new.third_party_acct_no));
     END IF; 
	
    IF (insert_values!='') THEN
        SET insert_values=CONCAT('INSERTED ->',insert_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(new.file_number_id,'auto',insert_values,NOW(),new.entered_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    
    END;
$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER  lcl_quote_piece_insert_trigger$$

CREATE TRIGGER lcl_quote_piece_insert_trigger AFTER INSERT ON lcl_quote_piece 
    FOR EACH ROW BEGIN
    DECLARE insert_values TEXT;
    
    IF new.commodity_type_id!='' THEN
        SET insert_values = concat_insert_values(insert_values,' Tariff',(SELECT desc_en FROM commodity_type WHERE id=new.commodity_type_id));
        SET insert_values = concat_insert_values(insert_values,', Tariff#',(SELECT CODE FROM commodity_type WHERE id=new.commodity_type_id));
     END IF;
        
     IF new.harmonized_code!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Harmonized Code',new.harmonized_code);
     END IF;
     
     IF new.booked_piece_count!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Booked Piece Count',new.booked_piece_count);
     END IF;
     
     IF new.packaging_type_id!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Pkg Type',(SELECT TYPE FROM package_type WHERE id=new.packaging_type_id));
     END IF;
     
     IF new.booked_volume_imperial!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Booked Volume Imperial(CFT)',new.booked_volume_imperial);
     END IF;
     
     IF new.booked_volume_metric!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Booked Volume Metric(CBM)',new.booked_volume_metric);
     END IF;
     
     IF new.booked_weight_imperial!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Booked Weight Imperial(LBS)',new.booked_weight_imperial);
     END IF;
     
     IF new.booked_weight_metric!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Booked Weight Metric(KGS)',new.booked_weight_metric);
     END IF;
     
	SET insert_values = concat_insert_values(insert_values,', HaZmat',IF(new.hazmat=0,'No','Yes'));
	SET insert_values = concat_insert_values(insert_values,', Personal Effects',new.personal_effects);
	SET insert_values = concat_insert_values(insert_values,', Refrigeration',IF(new.refrigeration_required=0,'No','Yes'));
	SET insert_values = concat_insert_values(insert_values,', Weight Verified',IF(new.weight_verified=0,'No','Yes'));
	SET insert_values = concat_insert_values(insert_values,', Barrel Business',IF(new.is_barrel=0,'No','Yes'));
     
    IF new.piece_desc!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Commodity Desc',new.piece_desc);
     END IF;
     
     IF new.mark_no_desc!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Marks & Numbers',new.mark_no_desc);
     END IF;
	
    IF (insert_values!='') THEN
        SET insert_values=CONCAT('INSERTED ->',insert_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(new.file_number_id,'auto',insert_values,NOW(),new.entered_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    
    END;
$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER  lcl_quote_piece_detail_insert_trigger$$

CREATE  TRIGGER lcl_quote_piece_detail_insert_trigger AFTER INSERT ON lcl_quote_piece_detail 
    FOR EACH ROW BEGIN
    DECLARE insert_values TEXT;
       
      IF new.actual_uom!='' THEN
       SET insert_values = concat_insert_values(insert_values,' UOM',new.actual_uom);
      END IF;
      
      IF new.actual_length!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Length(IN)',new.actual_length);
      END IF;
      
      IF new.actual_width!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Width(IN)',new.actual_width);
      END IF;
      
      IF new.actual_height!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Height(IN)',new.actual_height);
      END IF;
      
      IF new.actual_weight!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Weight Per Piece(LBS)',new.actual_weight);
      END IF;
      
      IF new.piece_count!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Piece',new.piece_count);
      END IF;
      
      IF new.stowed_location!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Warehouse Line',new.stowed_location);
      END IF;
      
      SET @fileId=(SELECT file_number_id FROM lcl_quote_piece WHERE id=new.quote_piece_id);
     
    IF (insert_values!='') THEN
        SET insert_values=CONCAT('INSERTED ->',insert_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(@fileId,'auto',insert_values,NOW(),new.entered_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    
    END;
$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER  lcl_quote_piece_whse_insert_trigger$$

CREATE  TRIGGER lcl_quote_piece_whse_insert_trigger AFTER INSERT ON lcl_quote_piece_whse 
    FOR EACH ROW BEGIN
    DECLARE insert_values TEXT;
       
      IF new.warehouse_id!='' THEN
       SET insert_values = concat_insert_values(insert_values,' Warehouse Location',(SELECT warehsname FROM warehouse WHERE id=new.warehouse_id));
       SET insert_values = concat_insert_values(insert_values,', Warehouse Location Code',(SELECT warehsno FROM warehouse WHERE id=new.warehouse_id));
      END IF;
      
      IF new.location!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Line Location',new.location);
      END IF;
      
      SET @fileId=(SELECT file_number_id FROM lcl_quote_piece WHERE id=new.quote_piece_id);
     
    IF (insert_values!='') THEN
        SET insert_values=CONCAT('INSERTED ->',insert_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(@fileId,'auto',insert_values,NOW(),new.entered_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    
    END;
$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER  lcl_quote_hazmat_insert_trigger$$

CREATE TRIGGER lcl_quote_hazmat_insert_trigger AFTER INSERT ON lcl_quote_hazmat 
    FOR EACH ROW BEGIN
    DECLARE insert_values TEXT;
       
      IF new.un_hazmat_no!='' THEN
       SET insert_values = concat_insert_values(insert_values,' UN#',new.un_hazmat_no);
      END IF;
      
      IF new.proper_shipping_name!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Proper Shipping Name',new.proper_shipping_name);
      END IF;
      
      IF new.technical_name!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Tech.Chemical Name',new.technical_name);
      END IF;
      
      IF new.imo_pri_class_code!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Class',new.imo_pri_class_code);
      END IF;
      
      IF new.imo_pri_sub_class_code!='' THEN
       SET insert_values = concat_insert_values(insert_values,', IMO Subsidary Class',new.imo_pri_sub_class_code);
      END IF;
      
      IF new.imo_sec_sub_class_code!='' THEN
       SET insert_values = concat_insert_values(insert_values,', IMO Secondary Class',new.imo_sec_sub_class_code);
      END IF;
      
      IF new.packing_group_code!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Packaging Group Code',new.packing_group_code);
      END IF;
      
      IF new.flash_point!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Flash point',new.flash_point);
      END IF;
      
      IF new.outer_pkg_no_pieces!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Outer Packaging Pieces',new.outer_pkg_no_pieces);
      END IF;
      
      IF new.outer_pkg_type!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Outer Packaging Type',new.outer_pkg_type);
      END IF;
      
      IF new.outer_pkg_composition!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Outer Pkg Composition',new.outer_pkg_composition);
      END IF;
      
       IF new.inner_pkg_no_pieces!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Inner Packaging Pieces',new.inner_pkg_no_pieces);
      END IF;
      
      IF new.inner_pkg_type!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Inner Packaging Type',new.inner_pkg_type);
      END IF;
      
      IF new.inner_pkg_composition!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Inner Pkg Composition',new.inner_pkg_composition);
      END IF;
      
      IF new.inner_pkg_uom!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Inner Pkg UOM',new.inner_pkg_uom);
      END IF;
      
      IF new.inner_pkg_nwt_piece!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Inner Pkg Wt/Vol Per Piece',new.inner_pkg_nwt_piece);
      END IF;
      
      IF new.total_net_weight!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Total Net Weight/Kgs',new.total_net_weight);
      END IF;
      
      IF new.liquid_volume!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Total Volume',new.liquid_volume);
      END IF;
      
      IF new.total_gross_weight!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Total Gross Weight/Kgs',new.total_gross_weight);
      END IF;
      
      IF new.ems_code!='' THEN
       SET insert_values = concat_insert_values(insert_values,', EMS code',new.ems_code);
      END IF;
      
      IF new.emergency_contact_id!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Emergency Contact',(SELECT contact_name FROM lcl_contact WHERE id=new.emergency_contact_id));
       SET insert_values = concat_insert_values(insert_values,', Emergency Phone No',(SELECT phone1 FROM lcl_contact WHERE id=new.emergency_contact_id));
      END IF;
      
       SET insert_values = concat_insert_values(insert_values,', Reportable Quantity',IF(new.reportable_quantity=0,'No','Yes'));
       SET insert_values = concat_insert_values(insert_values,', Marine pollutant',IF(new.marine_pollutant=0,'No','Yes'));
       SET insert_values = concat_insert_values(insert_values,', Excepted Quantity',IF(new.excepted_quantity=0,'No','Yes'));
       SET insert_values = concat_insert_values(insert_values,', Limited Quantity',IF(new.limited_quantity=0,'N0','Yes'));
       SET insert_values = concat_insert_values(insert_values,', Residue',IF(new.residue=0,'N0','Yes'));
       SET insert_values = concat_insert_values(insert_values,', Inhalation Hazard',IF(new.inhalation_hazard=0,'N0','Yes'));
      
    IF (insert_values!='') THEN
        SET insert_values=CONCAT('INSERTED ->',insert_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(new.file_number_id,'auto',insert_values,NOW(),new.entered_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    
    END;
$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER  lcl_quote_pad_insert_trigger$$

CREATE  TRIGGER  lcl_quote_pad_insert_trigger AFTER INSERT ON lcl_quote_pad 
    FOR EACH ROW BEGIN
    DECLARE insert_values TEXT;
    
    IF new.issuing_terminal!='' THEN
       SET insert_values = concat_insert_values(insert_values,' Issued By',(SELECT CONCAT(UPPER(login_name),'/',terminal_id) FROM user_details WHERE user_id=new.entered_by_user_id));
    END IF;
    
    IF new.pickup_to!='' THEN
       SET insert_values = concat_insert_values(insert_values,', To',new.pickup_to);
    END IF;
    
    IF new.pickup_contact_id!='' THEN
    
       SELECT company_name,contact_name,city,address,zip,phone1,fax1,email1 INTO @compName,@contName,@city,@address,@zip,@phone,@fax,@email 
       FROM lcl_contact WHERE id=new.pickup_contact_id;
       
       IF @city!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Door Origin/City/Zip',@city);
       END IF;
       IF @compName!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Ship/Supplier',@compName);
       END IF;
       IF @contName!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Contact Name',@contName);
       END IF;
       IF @address!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Address',@address);
       END IF;
       IF @zip!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Zip',@zip);
       END IF;
       IF @phone!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Phone',@phone);
       END IF;
       IF @fax!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Fax',@fax);
       END IF;
       IF @email!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Email',@email);
       END IF;
       
    END IF;
    
    IF new.pickup_hours!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Shipper Hours',new.pickup_hours);
    END IF;
    
    IF new.pickup_ready_note!='' THEN
       SET insert_values = concat_insert_values(insert_values,', CutOff Note',new.pickup_ready_note);
    END IF;
    
    IF new.pickup_ready_date!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Ready Date',new.pickup_ready_date);
    END IF;
    
    IF new.pickup_cutoff_date!='' THEN
       SET insert_values = concat_insert_values(insert_values,', CutOff Date',new.pickup_cutoff_date);
    END IF;
    
    IF new.scac!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Scac Code',new.scac);
    END IF;
    
    IF new.quote_ac_id!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Pickup Charge/Sell',(SELECT ar_amount FROM lcl_quote_ac WHERE id=new.quote_ac_id));
       SET insert_values = concat_insert_values(insert_values,', Pickup Cost',(SELECT ap_amount FROM lcl_quote_ac WHERE id=new.quote_ac_id));
    END IF;
    
    IF new.pickup_reference_no!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Ref#/PRO#',new.pickup_reference_no);
    END IF;
    
    IF new.delivery_contact_id!='' THEN
       SET insert_values = concat_insert_values(insert_values,', DEL TO WHSE WhseName',(SELECT company_name FROM lcl_contact WHERE id=new.delivery_contact_id));
       SET insert_values = concat_insert_values(insert_values,', Address',(SELECT address FROM lcl_contact WHERE id=new.delivery_contact_id));
       SET insert_values = concat_insert_values(insert_values,', City/State/Zip',(SELECT CONCAT(city,'/',state,'/',zip) FROM lcl_contact WHERE id=new.delivery_contact_id));
       SET insert_values = concat_insert_values(insert_values,', Phone',(SELECT phone1 FROM lcl_contact WHERE id=new.delivery_contact_id));
    END IF;
    
    IF new.pickup_instructions!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Instructions',new.pickup_instructions);
    END IF;
    
    IF new.commodity_desc!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Commodity',new.commodity_desc);
    END IF;
    
    IF new.terms_of_service!='' THEN
       SET insert_values = concat_insert_values(insert_values,', TOS',new.terms_of_service);
    END IF;
    
    IF (insert_values!='') THEN
        SET insert_values=CONCAT('INSERTED ->',insert_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(new.file_number_id,'auto',insert_values,NOW(),new.entered_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    
    END;
$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER  lcl_quote_hs_code_insert_trigger$$

CREATE TRIGGER lcl_quote_hs_code_insert_trigger AFTER INSERT ON lcl_quote_hs_code 
    FOR EACH ROW BEGIN
    DECLARE insert_values TEXT;
        
    IF new.codes!='' THEN
        SET insert_values = concat_insert_values(insert_values,' HS Code# -> Code',new.codes);
     END IF;
    
    IF new.no_pieces!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Pcs',new.no_pieces);
     END IF;
     
     IF new.weight_metric!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Wgt(KGS)',new.weight_metric);
     END IF;
     
     IF new.package_type_id!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Pkg Type',(SELECT description FROM package_type WHERE id=new.package_type_id));
     END IF;
     
    IF (insert_values!='') THEN
        SET insert_values=CONCAT('INSERTED ->',insert_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(new.file_number_id,'auto',insert_values,NOW(),new.entered_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    
    END;
$$

DELIMITER ;

-- Booking Module Insert Triggers

DELIMITER $$

DROP TRIGGER  lcl_booking_insert_trigger$$

CREATE TRIGGER lcl_booking_insert_trigger AFTER INSERT ON lcl_booking 
    FOR EACH ROW BEGIN
    DECLARE insert_values TEXT;
     IF new.poo_id!='' THEN
	SET insert_values = concat_insert_values(insert_values,' Origin CFS',(SELECT un_loc_name FROM un_location WHERE id=new.poo_id));
     END IF;
    
    IF new.pol_id!='' THEN
	SET insert_values = concat_insert_values(insert_values,', POL',(SELECT un_loc_name FROM un_location WHERE id=new.pol_id));
     END IF;
     
     IF new.pod_id!='' THEN
	SET insert_values = concat_insert_values(insert_values,', POD',(SELECT un_loc_name FROM un_location WHERE id=new.pod_id));
     END IF;
     
      IF new.fd_id!='' THEN
	SET insert_values = concat_insert_values(insert_values,', Destination',(SELECT un_loc_name FROM un_location WHERE id=new.fd_id));
     END IF;
      
     IF new.rate_type!='' THEN
	SET insert_values = concat_insert_values(insert_values,', Rate Type',new.rate_type);
     END IF;
     
	SET insert_values = concat_insert_values(insert_values,', Pickup',IF(new.poo_pickup=0,'No','Yes'));
    
	SET insert_values = concat_insert_values(insert_values,', Relay Ovr',IF(new.relay_override=0,'No','Yes')); 
     
     IF new.poo_whse_contact_id!='' THEN
	SET insert_values = concat_insert_values(insert_values,', Delivery Cargo to',(SELECT CONCAT(company_name,' / ',address) FROM lcl_contact WHERE id=new.poo_whse_contact_id));
     END IF;
     
     IF new.billing_terminal!='' THEN
	SET insert_values = concat_insert_values(insert_values,', Term to do BL',(SELECT terminal_location FROM terminal WHERE trmnum=new.billing_terminal));
     END IF;
     
     IF new.master_schedule_id!='' THEN
	
	SELECT ls.schedule_no,tp.acct_name,ls.sp_reference_no,ls.sp_reference_name,ls.pol_pg,ls.pol_etd,ls.poo_lrdt,ls.fd_eta
	INTO @schdNo,@spAcctNo,@spRefNo,@spRefName,@polPg,@polEtd,@pooLrd,@fdEta
	FROM lcl_schedule ls LEFT JOIN trading_partner tp ON ls.sp_acct_no=tp.acct_no WHERE ls.id=new.master_schedule_id;
	
	IF @schdNo!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Booked For Voyage -> ECI Voy#',@schdNo);
       END IF;
       IF @spAcctNo!='' THEN
		SET insert_values = concat_insert_values(insert_values,', SS Line',@spAcctNo);
       END IF;
       IF @spRefNo!='' THEN
		SET insert_values = concat_insert_values(insert_values,', SS Voy',@spRefNo);
       END IF;
       IF @spRefName!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Vessel Name',@spRefName);
       END IF;
       IF @polPg!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Pier',@polPg);
       END IF;
       IF @polEtd!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Sail Date',@polEtd);
       END IF;
       IF @pooLrd!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Origin LRD',@pooLrd);
       END IF;
       IF @fdEta!='' THEN
		SET insert_values = concat_insert_values(insert_values,', ETA FD',@fdEta);
       END IF;
     
     END IF ;
      
	SET insert_values = concat_insert_values(insert_values,', Billing Type',new.billing_type);
     
	SET insert_values = concat_insert_values(insert_values,', Insurance',IF(new.insurance=0,'No','Yes'));
     
     IF new.value_of_goods!='' THEN
	SET insert_values = concat_insert_values(insert_values,', Value of Goods',new.value_of_goods);
     END IF;
    
	SET insert_values = concat_insert_values(insert_values,', Delivery Metro PRSJU',new.delivery_metro);
     
	SET insert_values = concat_insert_values(insert_values,', Documentation',IF(new.documentation=0,'No','Yes'));
      
	SET insert_values = concat_insert_values(insert_values,', Spot Rate',IF(new.spot_rate=0,'No','Yes'));
	
	SET insert_values = concat_insert_values(insert_values,', Unknown Dest',IF(new.non_rated=0,'No','Yes'));
	
     	SET insert_values = concat_insert_values(insert_values,', Default Agent',IF(new.default_agent=0,'No','Yes'));
	
     IF new.agent_acct_no!='' THEN
	SET insert_values = concat_insert_values(insert_values,', Agent Acct No#',new.agent_acct_no);
	SET insert_values = concat_insert_values(insert_values,', Agent Acct Name',(SELECT acct_name FROM trading_partner WHERE acct_no=new.agent_acct_no));
     END IF;
	
	SET insert_values = concat_insert_values(insert_values,', ERT',IF(new.rtd_transaction=0,'No','Yes'));
	
     IF new.rtd_agent_acct_no!='' THEN
	SET insert_values = concat_insert_values(insert_values,', Agent Info',new.rtd_agent_acct_no);
     END IF;
     
	SET insert_values = concat_insert_values(insert_values,', PWK',IF(new.client_pwk_recvd=0,'No','Yes'));
	
	IF new.bill_to_party!='' THEN
	SET insert_values = concat_insert_values(insert_values,', Bill To Party',new.bill_to_party);
      END IF; 
	
      IF new.third_party_acct_no!='' THEN
	SET insert_values = concat_insert_values(insert_values,', Third Party Acct No#',new.third_party_acct_no);
	SET insert_values = concat_insert_values(insert_values,', Third Party Acct',(SELECT acct_name FROM trading_partner WHERE acct_no=new.third_party_acct_no));
      END IF; 
	
      IF (insert_values!='') THEN
        SET insert_values=CONCAT('INSERTED ->',insert_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(new.file_number_id,'auto',insert_values,NOW(),new.entered_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    
    END;
$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER  lcl_booking_piece_insert_trigger$$

CREATE TRIGGER lcl_booking_piece_insert_trigger AFTER INSERT ON lcl_booking_piece 
    FOR EACH ROW BEGIN
    DECLARE insert_values TEXT;
    
    IF new.commodity_type_id!='' THEN
        SET insert_values = concat_insert_values(insert_values,' Tariff',(SELECT desc_en FROM commodity_type WHERE id=new.commodity_type_id));
        SET insert_values = concat_insert_values(insert_values,', Tariff#',(SELECT CODE FROM commodity_type WHERE id=new.commodity_type_id));
     END IF;
        
     IF new.harmonized_code!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Harmonized Code',new.harmonized_code);
     END IF;
     
     IF new.booked_piece_count!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Booked Piece Count',new.booked_piece_count);
     END IF;
     
     IF new.packaging_type_id!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Pkg Type',(SELECT TYPE FROM package_type WHERE id=new.packaging_type_id));
     END IF;
     
     IF new.booked_volume_imperial!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Booked Volume Imperial(CFT)',new.booked_volume_imperial);
     END IF;
     
     IF new.booked_volume_metric!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Booked Volume Metric(CBM)',new.booked_volume_metric);
     END IF;
     
     IF new.booked_weight_imperial!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Booked Weight Imperial(LBS)',new.booked_weight_imperial);
     END IF;
     
     IF new.booked_weight_metric!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Booked Weight Metric(KGS)',new.booked_weight_metric);
     END IF;
     
	SET insert_values = concat_insert_values(insert_values,', HaZmat',IF(new.hazmat=0,'No','Yes'));
	SET insert_values = concat_insert_values(insert_values,', Personal Effects',new.personal_effects);
	SET insert_values = concat_insert_values(insert_values,', Refrigeration',IF(new.refrigeration_required=0,'No','Yes'));
	SET insert_values = concat_insert_values(insert_values,', Weight Verified',IF(new.weight_verified=0,'No','Yes'));
	SET insert_values = concat_insert_values(insert_values,', Barrel Business',IF(new.is_barrel=0,'No','Yes'));
     
    IF new.piece_desc!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Commodity Desc',new.piece_desc);
     END IF;
     
     IF new.mark_no_desc!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Marks & Numbers',new.mark_no_desc);
     END IF;
	
    IF (insert_values!='') THEN
        SET insert_values=CONCAT('INSERTED ->',insert_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(new.file_number_id,'auto',insert_values,NOW(),new.entered_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    
    END;
$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER  lcl_booking_piece_detail_insert_trigger$$

CREATE  TRIGGER lcl_booking_piece_detail_insert_trigger AFTER INSERT ON lcl_booking_piece_detail 
    FOR EACH ROW BEGIN
    DECLARE insert_values TEXT;
       
      IF new.actual_uom!='' THEN
       SET insert_values = concat_insert_values(insert_values,' UOM',new.actual_uom);
      END IF;
      
      IF new.actual_length!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Length(IN)',new.actual_length);
      END IF;
      
      IF new.actual_width!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Width(IN)',new.actual_width);
      END IF;
      
      IF new.actual_height!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Height(IN)',new.actual_height);
      END IF;
      
      IF new.actual_weight!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Weight Per Piece(LBS)',new.actual_weight);
      END IF;
      
      IF new.piece_count!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Piece',new.piece_count);
      END IF;
      
      IF new.stowed_location!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Warehouse Line',new.stowed_location);
      END IF;
      
      SET @fileId=(SELECT file_number_id FROM lcl_booking_piece WHERE id=new.booking_piece_id);
     
    IF (insert_values!='') THEN
        SET insert_values=CONCAT('INSERTED ->',insert_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(@fileId,'auto',insert_values,NOW(),new.entered_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    
    END;
$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER  lcl_booking_piece_whse_insert_trigger$$

CREATE  TRIGGER lcl_booking_piece_whse_insert_trigger AFTER INSERT ON lcl_booking_piece_whse 
    FOR EACH ROW BEGIN
    DECLARE insert_values TEXT;
       
      IF new.warehouse_id!='' THEN
       SET insert_values = concat_insert_values(insert_values,' Warehouse Location',(SELECT warehsname FROM warehouse WHERE id=new.warehouse_id));
       SET insert_values = concat_insert_values(insert_values,', Warehouse Location Code',(SELECT warehsno FROM warehouse WHERE id=new.warehouse_id));
      END IF;
      
      IF new.location!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Line Location',new.location);
      END IF;
      
      SET @fileId=(SELECT file_number_id FROM lcl_booking_piece WHERE id=new.booking_piece_id);
     
    IF (insert_values!='') THEN
        SET insert_values=CONCAT('INSERTED ->',insert_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(@fileId,'auto',insert_values,NOW(),new.entered_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    
    END;
$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER  lcl_booking_hazmat_insert_trigger$$

CREATE TRIGGER lcl_booking_hazmat_insert_trigger AFTER INSERT ON lcl_booking_hazmat 
    FOR EACH ROW BEGIN
    DECLARE insert_values TEXT;
       
      IF new.un_hazmat_no!='' THEN
       SET insert_values = concat_insert_values(insert_values,' UN#',new.un_hazmat_no);
      END IF;
      
      IF new.proper_shipping_name!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Proper Shipping Name',new.proper_shipping_name);
      END IF;
      
      IF new.technical_name!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Tech.Chemical Name',new.technical_name);
      END IF;
      
      IF new.imo_pri_class_code!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Class',new.imo_pri_class_code);
      END IF;
      
      IF new.imo_pri_sub_class_code!='' THEN
       SET insert_values = concat_insert_values(insert_values,', IMO Subsidary Class',new.imo_pri_sub_class_code);
      END IF;
      
      IF new.imo_sec_sub_class_code!='' THEN
       SET insert_values = concat_insert_values(insert_values,', IMO Secondary Class',new.imo_sec_sub_class_code);
      END IF;
      
      IF new.packing_group_code!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Packaging Group Code',new.packing_group_code);
      END IF;
      
      IF new.flash_point!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Flash point',new.flash_point);
      END IF;
      
      IF new.outer_pkg_no_pieces!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Outer Packaging Pieces',new.outer_pkg_no_pieces);
      END IF;
      
      IF new.outer_pkg_type!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Outer Packaging Type',new.outer_pkg_type);
      END IF;
      
      IF new.outer_pkg_composition!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Outer Pkg Composition',new.outer_pkg_composition);
      END IF;
      
       IF new.inner_pkg_no_pieces!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Inner Packaging Pieces',new.inner_pkg_no_pieces);
      END IF;
      
      IF new.inner_pkg_type!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Inner Packaging Type',new.inner_pkg_type);
      END IF;
      
      IF new.inner_pkg_composition!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Inner Pkg Composition',new.inner_pkg_composition);
      END IF;
      
      IF new.inner_pkg_uom!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Inner Pkg UOM',new.inner_pkg_uom);
      END IF;
      
      IF new.inner_pkg_nwt_piece!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Inner Pkg Wt/Vol Per Piece',new.inner_pkg_nwt_piece);
      END IF;
      
      IF new.total_net_weight!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Total Net Weight/Kgs',new.total_net_weight);
      END IF;
      
      IF new.liquid_volume!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Total Volume',new.liquid_volume);
      END IF;
      
      IF new.total_gross_weight!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Total Gross Weight/Kgs',new.total_gross_weight);
      END IF;
      
      IF new.ems_code!='' THEN
       SET insert_values = concat_insert_values(insert_values,', EMS code',new.ems_code);
      END IF;
      
      IF new.emergency_contact_id!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Emergency Contact',(SELECT contact_name FROM lcl_contact WHERE id=new.emergency_contact_id));
       SET insert_values = concat_insert_values(insert_values,', Emergency Phone No',(SELECT phone1 FROM lcl_contact WHERE id=new.emergency_contact_id));
      END IF;
      
       SET insert_values = concat_insert_values(insert_values,', Reportable Quantity',IF(new.reportable_quantity=0,'No','Yes'));
       SET insert_values = concat_insert_values(insert_values,', Marine pollutant',IF(new.marine_pollutant=0,'No','Yes'));
       SET insert_values = concat_insert_values(insert_values,', Excepted Quantity',IF(new.excepted_quantity=0,'No','Yes'));
       SET insert_values = concat_insert_values(insert_values,', Limited Quantity',IF(new.limited_quantity=0,'N0','Yes'));
       SET insert_values = concat_insert_values(insert_values,', Residue',IF(new.residue=0,'N0','Yes'));
       SET insert_values = concat_insert_values(insert_values,', Inhalation Hazard',IF(new.inhalation_hazard=0,'N0','Yes'));
      
    IF (insert_values!='') THEN
        SET insert_values=CONCAT('INSERTED ->',insert_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(new.file_number_id,'auto',insert_values,NOW(),new.entered_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    
    END;
$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER  lcl_booking_pad_insert_trigger$$

CREATE  TRIGGER  lcl_booking_pad_insert_trigger AFTER INSERT ON lcl_booking_pad 
    FOR EACH ROW BEGIN
    DECLARE insert_values TEXT;
    
    IF new.issuing_terminal!='' THEN
       SET insert_values = concat_insert_values(insert_values,' Issued By',(SELECT CONCAT(UPPER(login_name),'/',terminal_id) FROM user_details WHERE user_id=new.entered_by_user_id));
    END IF;
    
    IF new.pickup_to!='' THEN
       SET insert_values = concat_insert_values(insert_values,', To',new.pickup_to);
    END IF;
    
    IF new.pickup_contact_id!='' THEN
    
       SELECT company_name,contact_name,city,address,zip,phone1,fax1,email1 INTO @compName,@contName,@city,@address,@zip,@phone,@fax,@email 
       FROM lcl_contact WHERE id=new.pickup_contact_id;
       
       IF @city!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Door Origin/City/Zip',@city);
       END IF;
       IF @compName!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Ship/Supplier',@compName);
       END IF;
       IF @contName!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Contact Name',@contName);
       END IF;
       IF @address!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Address',@address);
       END IF;
       IF @zip!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Zip',@zip);
       END IF;
       IF @phone!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Phone',@phone);
       END IF;
       IF @fax!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Fax',@fax);
       END IF;
       IF @email!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Email',@email);
       END IF;
       
    END IF;
    
    IF new.pickup_hours!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Shipper Hours',new.pickup_hours);
    END IF;
    
    IF new.pickup_ready_note!='' THEN
       SET insert_values = concat_insert_values(insert_values,', CutOff Note',new.pickup_ready_note);
    END IF;
    
    IF new.pickup_ready_date!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Ready Date',new.pickup_ready_date);
    END IF;
    
    IF new.pickup_cutoff_date!='' THEN
       SET insert_values = concat_insert_values(insert_values,', CutOff Date',new.pickup_cutoff_date);
    END IF;
    
    IF new.scac!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Scac Code',new.scac);
    END IF;
    
    IF new.booking_ac_id!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Pickup Charge/Sell',(SELECT ar_amount FROM lcl_booking_ac WHERE id=new.booking_ac_id));
       SET insert_values = concat_insert_values(insert_values,', Pickup Cost',(SELECT ap_amount FROM lcl_booking_ac WHERE id=new.booking_ac_id));
    END IF;
    
    IF new.pickup_reference_no!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Ref#/PRO#',new.pickup_reference_no);
    END IF;
    
    IF new.delivery_contact_id!='' THEN
       SET insert_values = concat_insert_values(insert_values,', DEL TO WHSE WhseName',(SELECT company_name FROM lcl_contact WHERE id=new.delivery_contact_id));
       SET insert_values = concat_insert_values(insert_values,', Address',(SELECT address FROM lcl_contact WHERE id=new.delivery_contact_id));
       SET insert_values = concat_insert_values(insert_values,', City/State/Zip',(SELECT CONCAT(city,'/',state,'/',zip) FROM lcl_contact WHERE id=new.delivery_contact_id));
       SET insert_values = concat_insert_values(insert_values,', Phone',(SELECT phone1 FROM lcl_contact WHERE id=new.delivery_contact_id));
    END IF;
    
    IF new.pickup_instructions!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Instructions',new.pickup_instructions);
    END IF;
    
    IF new.commodity_desc!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Commodity',new.commodity_desc);
    END IF;
    
    IF new.terms_of_service!='' THEN
       SET insert_values = concat_insert_values(insert_values,', TOS',new.terms_of_service);
    END IF;
    
    IF (insert_values!='') THEN
        SET insert_values=CONCAT('INSERTED ->',insert_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(new.file_number_id,'auto',insert_values,NOW(),new.entered_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    
    END;
$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER  lcl_booking_hs_code_insert_trigger$$

CREATE TRIGGER lcl_booking_hs_code_insert_trigger AFTER INSERT ON lcl_booking_hs_code 
    FOR EACH ROW BEGIN
    DECLARE insert_values TEXT;
        
    IF new.codes!='' THEN
        SET insert_values = concat_insert_values(insert_values,' HS Code# -> Code',new.codes);
     END IF;
    
    IF new.no_pieces!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Pcs',new.no_pieces);
     END IF;
     
     IF new.weight_metric!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Wgt(KGS)',new.weight_metric);
     END IF;
     
     IF new.package_type_id!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Pkg Type',(SELECT description FROM package_type WHERE id=new.package_type_id));
     END IF;
     
    IF (insert_values!='') THEN
        SET insert_values=CONCAT('INSERTED ->',insert_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(new.file_number_id,'auto',insert_values,NOW(),new.entered_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    
    END;
$$

DELIMITER ;

-- BL Module Triggers

DELIMITER $$

DROP TRIGGER lcl_bl_insert_trigger$$

CREATE TRIGGER lcl_bl_insert_trigger AFTER INSERT ON lcl_bl
    FOR EACH ROW BEGIN
    DECLARE insert_values TEXT;
    
     IF new.file_number_id!='' THEN
	SET insert_values = concat_insert_values(insert_values,' Document No',(SELECT file_number FROM lcl_file_number WHERE id=new.file_number_id));
     END IF;
     
     IF new.ship_acct_no!='' THEN
	SELECT tp.acct_no,tp.acct_name,ca.address1,ca.city1,ca.state,ca.zip,ca.phone,ca.fax,ca.email1
	INTO @acctNo,@acctName,@address,@city,@state,@zip,@phone,@fax,@email
	FROM trading_partner tp LEFT JOIN cust_address ca ON tp.acct_no = ca.acct_no WHERE tp.acct_no=new.ship_acct_no;
	
	SET insert_values = concat_insert_values(insert_values,' SHIPPER/EXPORTER -> Number',@acctNo);
	SET insert_values = concat_insert_values(insert_values,', Name',@acctName);
	IF @address!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Address',@address);
	END IF;
	
	IF @city!='' THEN
		SET insert_values = concat_insert_values(insert_values,', City',@city);
	END IF;
	
	IF @state!='' THEN
		SET insert_values = concat_insert_values(insert_values,', State',@state);
	END IF;
	
	IF @zip!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Zip',@zip);
	END IF;
	
	IF @phone!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Phone',@phone);
	END IF;
	
	IF @fax!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Fax',@fax);
	END IF;
	
	IF @email!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Email',@email);
	END IF;
     END IF;
     
     IF new.cons_acct_no!='' THEN
	SELECT tp.acct_no,tp.acct_name,ca.address1,ca.city1,ca.state,ca.zip,ca.phone,ca.fax,ca.email1
	INTO @acctNo,@acctName,@address,@city,@state,@zip,@phone,@fax,@email
	FROM trading_partner tp LEFT JOIN cust_address ca ON tp.acct_no = ca.acct_no WHERE tp.acct_no=new.cons_acct_no;
	
	SET insert_values = concat_insert_values(insert_values,' CONSIGNEE -> Number',@acctNo);
	SET insert_values = concat_insert_values(insert_values,', Name',@acctName);
	IF @address!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Address',@address);
	END IF;
	
	IF @city!='' THEN
		SET insert_values = concat_insert_values(insert_values,', City',@city);
	END IF;
	
	IF @state!='' THEN
		SET insert_values = concat_insert_values(insert_values,', State',@state);
	END IF;
	
	IF @zip!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Zip',@zip);
	END IF;
	
	IF @phone!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Phone',@phone);
	END IF;
	
	IF @fax!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Fax',@fax);
	END IF;
	
	IF @email!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Email',@email);
	END IF;
     END IF;
     
     IF new.noty_acct_no!='' THEN
	SELECT tp.acct_no,tp.acct_name,ca.address1,ca.city1,ca.state,ca.zip,ca.phone,ca.fax,ca.email1
	INTO @acctNo,@acctName,@address,@city,@state,@zip,@phone,@fax,@email
	FROM trading_partner tp LEFT JOIN cust_address ca ON tp.acct_no = ca.acct_no WHERE tp.acct_no=new.noty_acct_no;
	
	SET insert_values = concat_insert_values(insert_values,' NOTIFY PARTY -> Number',@acctNo);
	SET insert_values = concat_insert_values(insert_values,', Name',@acctName);
	IF @address!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Address',@address);
	END IF;
	
	IF @city!='' THEN
		SET insert_values = concat_insert_values(insert_values,', City',@city);
	END IF;
	
	IF @state!='' THEN
		SET insert_values = concat_insert_values(insert_values,', State',@state);
	END IF;
	
	IF @zip!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Zip',@zip);
	END IF;
	
	IF @phone!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Phone',@phone);
	END IF;
	
	IF @fax!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Fax',@fax);
	END IF;
	
	IF @email!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Email',@email);
	END IF;
     END IF;
     
     IF new.fwd_acct_no!='' THEN
	SELECT tp.acct_no,tp.acct_name,ca.address1,ca.city1,ca.state,ca.zip,ca.phone,ca.fax,ca.email1,cg.fw_fmc_no
	INTO @acctNo,@acctName,@address,@city,@state,@zip,@phone,@fax,@email,@fmc
	FROM trading_partner tp LEFT JOIN cust_address ca ON tp.acct_no = ca.acct_no
	LEFT JOIN cust_general_info cg ON ca.acct_no = cg.acct_no WHERE tp.acct_no=new.fwd_acct_no;
	
	SET insert_values = concat_insert_values(insert_values,' FORWARDER -> Number',@acctNo);
	SET insert_values = concat_insert_values(insert_values,', Name',@acctName);
	IF @fmc!='' THEN
		SET insert_values = concat_insert_values(insert_values,', FMC#',@fmc);
	END IF;
	IF @address!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Address',@address);
	END IF;
	
	IF @city!='' THEN
		SET insert_values = concat_insert_values(insert_values,', City',@city);
	END IF;
	
	IF @state!='' THEN
		SET insert_values = concat_insert_values(insert_values,', State',@state);
	END IF;
	
	IF @zip!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Zip',@zip);
	END IF;
	
	IF @phone!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Phone',@phone);
	END IF;
	
	IF @fax!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Fax',@fax);
	END IF;
	
	IF @email!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Email',@email);
	END IF;
     END IF;
     
     IF new.sup_acct_no!='' THEN
	SELECT tp.acct_no,tp.acct_name,ca.address1,ca.city1,ca.state,ca.zip,ca.phone,ca.fax,ca.email1
	INTO @acctNo,@acctName,@address,@city,@state,@zip,@phone,@fax,@email
	FROM trading_partner tp LEFT JOIN cust_address ca ON tp.acct_no = ca.acct_no WHERE tp.acct_no=new.sup_acct_no;
	
	SET insert_values = concat_insert_values(insert_values,'  SUPPLIER -> Number',@acctNo);
	SET insert_values = concat_insert_values(insert_values,', Name',@acctName);
	IF @address!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Address',@address);
	END IF;
	
	IF @city!='' THEN
		SET insert_values = concat_insert_values(insert_values,', City',@city);
	END IF;
	
	IF @state!='' THEN
		SET insert_values = concat_insert_values(insert_values,', State',@state);
	END IF;
	
	IF @zip!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Zip',@zip);
	END IF;
	
	IF @phone!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Phone',@phone);
	END IF;
	
	IF @fax!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Fax',@fax);
	END IF;
	
	IF @email!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Email',@email);
	END IF;
     END IF;
     
     IF new.agent_acct_no!='' THEN
	SELECT tp.acct_no,tp.acct_name,ca.address1,ca.city1,ca.state,ca.zip,ca.phone,ca.fax,ca.email1
	INTO @acctNo,@acctName,@address,@city,@state,@zip,@phone,@fax,@email
	FROM trading_partner tp LEFT JOIN cust_address ca ON tp.acct_no = ca.acct_no WHERE tp.acct_no=new.agent_acct_no;
	
	SET insert_values = concat_insert_values(insert_values,' TO PICK UP FREIGHT PLEASE CONTACT -> Number',@acctNo);
	SET insert_values = concat_insert_values(insert_values,', Name',@acctName);
	IF @address!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Address',@address);
	END IF;
	
	IF @city!='' THEN
		SET insert_values = concat_insert_values(insert_values,', City',@city);
	END IF;
	
	IF @state!='' THEN
		SET insert_values = concat_insert_values(insert_values,', State',@state);
	END IF;
	
	IF @zip!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Zip',@zip);
	END IF;
	
	IF @phone!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Phone',@phone);
	END IF;
	
	IF @fax!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Fax',@fax);
	END IF;
	
	IF @email!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Email',@email);
	END IF;
     END IF;
     
     IF new.master_schedule_id!='' THEN
	
	SELECT ls.schedule_no,tp.acct_name,ls.sp_reference_no,ls.sp_reference_name,ls.pol_pg,ls.pol_etd,ls.poo_lrdt,ls.fd_eta
	INTO @schdNo,@spAcctNo,@spRefNo,@spRefName,@polPg,@polEtd,@pooLrd,@fdEta
	FROM lcl_schedule ls LEFT JOIN trading_partner tp ON ls.sp_acct_no=tp.acct_no WHERE ls.id=new.master_schedule_id;
	
        IF @schdNo!='' THEN
		SET insert_values = concat_insert_values(insert_values,'  TRADE ROUTE -> ECI Voy#',@schdNo);
       END IF;
       IF @spAcctNo!='' THEN
		SET insert_values = concat_insert_values(insert_values,', SS Line',@spAcctNo);
       END IF;
       IF @spRefNo!='' THEN
		SET insert_values = concat_insert_values(insert_values,', SS Voy',@spRefNo);
       END IF;
       IF @spRefName!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Exp Carrier(VSL)(FLG)',@spRefName);
       END IF;
       IF @polPg!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Pier',@polPg);
       END IF;
       IF @polEtd!='' THEN
		SET insert_values = concat_insert_values(insert_values,', Sail Date',@polEtd);
       END IF;
     
     END IF ;
     
     IF new.pol_id!='' THEN
	SET insert_values = concat_insert_values(insert_values,', POL',(SELECT un_loc_name FROM un_location WHERE id=new.pol_id));
     END IF;
     
     IF new.pod_id!='' THEN
	SET insert_values = concat_insert_values(insert_values,', Sea POD',(SELECT un_loc_name FROM un_location WHERE id=new.pod_id));
     END IF;
     
     IF new.point_of_origin!='' THEN
	SET insert_values = concat_insert_values(insert_values,', Point Of Origin',new.point_of_origin);
     END IF;
     
     SET insert_values = concat_insert_values(insert_values,', Billing Type',new.billing_type);
     SET insert_values = concat_insert_values(insert_values,', Spot Rate',IF(new.spot_rate=0,'No','Yes'));
     SET insert_values = concat_insert_values(insert_values,', Free B/L',IF(new.free_bl=0,'No','Yes'));
     
     IF new.rate_type!='' THEN
	SET insert_values = concat_insert_values(insert_values,', Rate Type',new.rate_type);
     END IF;
     
     IF new.bill_to_party!='' THEN
	SET insert_values = concat_insert_values(insert_values,', Bill To Party',new.bill_to_party);
     END IF; 
     
     IF new.billing_terminal!='' THEN
	SET insert_values = concat_insert_values(insert_values,', Rates From Term',(SELECT terminal_location FROM terminal WHERE trmnum=new.billing_terminal));
     END IF; 
     
     IF new.third_party_acct_no!='' THEN
	SET insert_values = concat_insert_values(insert_values,', Third Party Acct No#',new.third_party_acct_no);
	SET insert_values = concat_insert_values(insert_values,', Third Party Acct',(SELECT acct_name FROM trading_partner WHERE acct_no=new.third_party_acct_no));
     END IF; 
     
     IF (insert_values!='') THEN
        SET insert_values=CONCAT('INSERTED ->',insert_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(new.file_number_id,'auto',insert_values,NOW(),new.entered_by_user_id,NOW(),new.modified_by_user_id);
     END IF;
    
    END;
$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER  lcl_bl_piece_insert_trigger$$

CREATE TRIGGER lcl_bl_piece_insert_trigger AFTER INSERT ON lcl_bl_piece 
    FOR EACH ROW BEGIN
    DECLARE insert_values TEXT;
    
    IF new.commodity_type_id!='' THEN
        SET insert_values = concat_insert_values(insert_values,' Tariff',(SELECT desc_en FROM commodity_type WHERE id=new.commodity_type_id));
        SET insert_values = concat_insert_values(insert_values,', Tariff#',(SELECT CODE FROM commodity_type WHERE id=new.commodity_type_id));
     END IF;
        
     IF new.harmonized_code!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Harmonized Code',new.harmonized_code);
     END IF;
     
     IF new.booked_piece_count!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Actual Piece Count',new.booked_piece_count);
     END IF;
     
     IF new.packaging_type_id!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Pkg Type',(SELECT TYPE FROM package_type WHERE id=new.packaging_type_id));
     END IF;
     
     IF new.actual_volume_imperial!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Actual Volume Imperial(CFT)',new.actual_volume_imperial);
     END IF;
     
     IF new.actual_volume_metric!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Actual Volume Metric(CBM)',new.actual_volume_metric);
     END IF;
     
     IF new.actual_weight_imperial!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Actual Weight Imperial(LBS)',new.actual_weight_imperial);
     END IF;
     
     IF new.actual_weight_metric!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Actual Weight Metric(KGS)',new.actual_weight_metric);
     END IF;
     
	SET insert_values = concat_insert_values(insert_values,', HaZmat',IF(new.hazmat=0,'No','Yes'));
	SET insert_values = concat_insert_values(insert_values,', Personal Effects',new.personal_effects);
	SET insert_values = concat_insert_values(insert_values,', Refrigeration',IF(new.refrigeration_required=0,'No','Yes'));
	SET insert_values = concat_insert_values(insert_values,', Barrel Business',IF(new.is_barrel=0,'No','Yes'));
     
    IF new.piece_desc!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Commodity Desc',new.piece_desc);
     END IF;
     
     IF new.mark_no_desc!='' THEN
        SET insert_values = concat_insert_values(insert_values,', Marks & Numbers',new.mark_no_desc);
     END IF;
	
    IF (insert_values!='') THEN
        SET insert_values=CONCAT('INSERTED ->',insert_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(new.file_number_id,'auto',insert_values,NOW(),new.entered_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    
    END;
$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER  lcl_bl_hazmat_insert_trigger$$

CREATE TRIGGER lcl_bl_hazmat_insert_trigger AFTER INSERT ON lcl_bl_hazmat 
    FOR EACH ROW BEGIN
    DECLARE insert_values TEXT;
       
      IF new.un_hazmat_no!='' THEN
       SET insert_values = concat_insert_values(insert_values,' UN#',new.un_hazmat_no);
      END IF;
      
      IF new.proper_shipping_name!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Proper Shipping Name',new.proper_shipping_name);
      END IF;
      
      IF new.technical_name!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Tech.Chemical Name',new.technical_name);
      END IF;
      
      IF new.imo_pri_class_code!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Class',new.imo_pri_class_code);
      END IF;
      
      IF new.imo_pri_sub_class_code!='' THEN
       SET insert_values = concat_insert_values(insert_values,', IMO Subsidary Class',new.imo_pri_sub_class_code);
      END IF;
      
      IF new.imo_sec_sub_class_code!='' THEN
       SET insert_values = concat_insert_values(insert_values,', IMO Secondary Class',new.imo_sec_sub_class_code);
      END IF;
      
      IF new.packing_group_code!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Packaging Group Code',new.packing_group_code);
      END IF;
      
      IF new.flash_point!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Flash point',new.flash_point);
      END IF;
      
      IF new.outer_pkg_no_pieces!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Outer Packaging Pieces',new.outer_pkg_no_pieces);
      END IF;
      
      IF new.outer_pkg_type!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Outer Packaging Type',new.outer_pkg_type);
      END IF;
      
      IF new.outer_pkg_composition!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Outer Pkg Composition',new.outer_pkg_composition);
      END IF;
      
       IF new.inner_pkg_no_pieces!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Inner Packaging Pieces',new.inner_pkg_no_pieces);
      END IF;
      
      IF new.inner_pkg_type!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Inner Packaging Type',new.inner_pkg_type);
      END IF;
      
      IF new.inner_pkg_composition!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Inner Pkg Composition',new.inner_pkg_composition);
      END IF;
      
      IF new.inner_pkg_uom!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Inner Pkg UOM',new.inner_pkg_uom);
      END IF;
      
      IF new.inner_pkg_nwt_piece!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Inner Pkg Wt/Vol Per Piece',new.inner_pkg_nwt_piece);
      END IF;
      
      IF new.total_net_weight!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Total Net Weight/Kgs',new.total_net_weight);
      END IF;
      
      IF new.liquid_volume!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Total Volume',new.liquid_volume);
      END IF;
      
      IF new.total_gross_weight!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Total Gross Weight/Kgs',new.total_gross_weight);
      END IF;
      
      IF new.ems_code!='' THEN
       SET insert_values = concat_insert_values(insert_values,', EMS code',new.ems_code);
      END IF;
      
      IF new.emergency_contact_id!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Emergency Contact',(SELECT contact_name FROM lcl_contact WHERE id=new.emergency_contact_id));
       SET insert_values = concat_insert_values(insert_values,', Emergency Phone No',(SELECT phone1 FROM lcl_contact WHERE id=new.emergency_contact_id));
      END IF;
      
       SET insert_values = concat_insert_values(insert_values,', Reportable Quantity',IF(new.reportable_quantity=0,'No','Yes'));
       SET insert_values = concat_insert_values(insert_values,', Marine pollutant',IF(new.marine_pollutant=0,'No','Yes'));
       SET insert_values = concat_insert_values(insert_values,', Excepted Quantity',IF(new.excepted_quantity=0,'No','Yes'));
       SET insert_values = concat_insert_values(insert_values,', Limited Quantity',IF(new.limited_quantity=0,'N0','Yes'));
       SET insert_values = concat_insert_values(insert_values,', Residue',IF(new.residue=0,'N0','Yes'));
       SET insert_values = concat_insert_values(insert_values,', Inhalation Hazard',IF(new.inhalation_hazard=0,'N0','Yes'));
      
    IF (insert_values!='') THEN
        SET insert_values=CONCAT('INSERTED ->',insert_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(new.file_number_id,'auto',insert_values,NOW(),new.entered_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    
    END;
$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER  lcl_options_insert_trigger$$

CREATE TRIGGER lcl_options_insert_trigger AFTER INSERT ON lcl_options 
    FOR EACH ROW BEGIN
    DECLARE insert_values TEXT;
    
    IF new.value='AES' THEN
	 SET insert_values = concat_insert_values(insert_values,' Print AES on body',IF(new.key='Y','Yes','No'));
    END IF;

    IF new.value='HS' THEN
	 SET insert_values = concat_insert_values(insert_values,' Print HS codes on body',IF(new.key='Y','Yes','No'));
    END IF;
    
    IF new.value='NCM' THEN
	 SET insert_values = concat_insert_values(insert_values,' Print NCM codes on body',IF(new.key='Y','Yes','No'));
    END IF;
    
    IF new.value='FP' THEN
	 SET insert_values = concat_insert_values(insert_values,' Use Alt Freight Pickup account',IF(new.key='Y','Yes','No'));
    END IF;
    
    IF new.value='IMP' THEN
	 SET insert_values = concat_insert_values(insert_values,' Print decimal on Imperial values',IF(new.key='Y','Yes','No'));
    END IF;
    
    IF new.value='ALERT' THEN
	 SET insert_values = concat_insert_values(insert_values,' Pre Alert',IF(new.key='Y','Yes','No'));
    END IF;
    
    IF new.value='PROOF' THEN
	 SET insert_values = concat_insert_values(insert_values,' Proof Copy',IF(new.key='Y','Yes','No'));
    END IF;
    
    IF new.value='MET' THEN
	 SET insert_values = concat_insert_values(insert_values,' Print Imperial values on Metric ports',IF(new.key='Y','Yes','No'));
    END IF;
    
    IF new.value='PORT' THEN
	 SET insert_values = concat_insert_values(insert_values,' Print Alt Port lower right',IF(new.key='Y','Yes','No'));
    END IF;
    
    IF (insert_values!='') THEN
        SET insert_values=CONCAT('INSERTED ->',insert_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(new.file_number_id,'auto',insert_values,NOW(),new.entered_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    
    END;
$$

DELIMITER ;

-- Common Insert Triggers For Quote,Booking And BL

DELIMITER $$

DROP TRIGGER  lcl_inbond_insert_trigger$$

CREATE TRIGGER lcl_inbond_insert_trigger AFTER INSERT ON lcl_inbond
    FOR EACH ROW BEGIN
    DECLARE insert_values TEXT;
        
     IF new.inbond_no!='' THEN
        SET insert_values = concat_insert_values(insert_values,' Inbond#',new.inbond_no);
      END IF;

      IF new.inbond_type!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Type',new.inbond_type);
      END IF;

      IF new.inbond_port!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Port',(SELECT un_loc_name FROM un_location WHERE id=new.inbond_port));
      END IF;

      IF new.inbond_datetime!='' THEN
       SET insert_values = concat_insert_values(insert_values,', Date',new.inbond_datetime);
      END IF;
    
    IF (insert_values!='') THEN
        SET insert_values=CONCAT('INSERTED ->',insert_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(new.file_number_id,'auto',insert_values,NOW(),new.entered_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    
    END;
$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER  lcl_export_insert_trigger$$

CREATE  TRIGGER lcl_export_insert_trigger AFTER INSERT ON lcl_export 
    FOR EACH ROW BEGIN
    DECLARE insert_values TEXT;
        SET insert_values = concat_insert_values(insert_values,' CFCL',IF(new.cfcl=0,'No','Yes'));
    
    IF new.cfcl_acct_no!='' THEN
        SET insert_values = concat_insert_values(insert_values,', CFCL Acct No#',new.cfcl_acct_no);
	SET insert_values = concat_insert_values(insert_values,', CFCL Acct Name',(SELECT acct_name FROM trading_partner WHERE acct_no=new.cfcl_acct_no));
    END IF;
    
       SET insert_values = concat_insert_values(insert_values,', Small Parcel',IF(new.ups=0,'No','Yes'));
       SET insert_values = concat_insert_values(insert_values,', Calc/Heavy',IF(new.calc_heavy=0,'No','Yes'));
       SET insert_values = concat_insert_values(insert_values,', AES By ECI',IF(new.aes=0,'No','Yes'));
       
    IF (insert_values!='') THEN
        SET insert_values=CONCAT('INSERTED ->',insert_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(new.file_number_id,'auto',insert_values,NOW(),new.entered_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    
    END;
$$

DELIMITER ;

-- Quote Module Update Trigger

DELIMITER $$

DROP TRIGGER after_LclQuote_update$$

CREATE TRIGGER after_LclQuote_update AFTER UPDATE ON lcl_quote
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT ;
    
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
    
    IF old.rate_type!=new.rate_type THEN
	SET updated_values=(SELECT concat_string(updated_values,'Rate Type',old.rate_type,new.rate_type));
    END IF;
    
    IF old.poo_door!=new.poo_door THEN
      SET updated_values=(SELECT concat_string(updated_values,'Pickup',IF(old.poo_door=0,'No','Yes'),IF(new.poo_door=0,'No','Yes')));
    END IF;
    
    IF old.relay_override!=new.relay_override THEN
      SET updated_values=(SELECT concat_string(updated_values,'Relay Ovr',IF(old.relay_override=0,'No','Yes'),IF(new.relay_override=0,'No','Yes')));
    END IF;
    
    IF old.poo_whse_contact_id!=new.poo_whse_contact_id THEN
	SET updated_values=(SELECT concat_string(updated_values,'Delivery Cargo to',(SELECT CONCAT(company_name,' / ',address) FROM lcl_contact WHERE id=old.poo_whse_contact_id),(SELECT CONCAT(company_name,' / ',address) FROM lcl_contact WHERE id=new.poo_whse_contact_id)));
    END IF;
    
    IF old.billing_terminal!=new.billing_terminal THEN
       SET updated_values=(SELECT concat_string(updated_values,'Term to do BL',(SELECT terminal_location FROM terminal WHERE trmnum=old.billing_terminal),(SELECT terminal_location FROM terminal WHERE trmnum=new.billing_terminal)));
    END IF;
    
    IF old.billing_type!=new.billing_type THEN
       SET updated_values=(SELECT concat_string(updated_values,'Billing Type',old.billing_type,new.billing_type));
    END IF;
    
    IF old.insurance!=new.insurance THEN
       SET updated_values=(SELECT concat_string(updated_values,'Insurance',IF(old.insurance=0,'No','Yes'),IF(new.insurance=0,'No','Yes')));
    END IF;
    
    IF old.value_of_goods!=new.value_of_goods THEN
       SET updated_values=(SELECT concat_string(updated_values,'Value of Goods',old.value_of_goods,new.value_of_goods));
    END IF;
        
    IF old.delivery_metro!=new.delivery_metro THEN
       SET updated_values=(SELECT concat_string(updated_values,'Delivery Metro',old.delivery_metro,new.delivery_metro));
    END IF;
      
    IF old.documentation!=new.documentation THEN
       SET updated_values=(SELECT concat_string(updated_values,'Documentation',IF(old.documentation=0,'No','Yes'),IF(new.documentation=0,'No','Yes')));
    END IF;
    
    IF old.spot_rate!=new.spot_rate THEN
      SET updated_values=(SELECT concat_string(updated_values,'SpotRate',IF(old.spot_rate=0,'No','Yes'),IF(new.spot_rate=0,'No','Yes')));
    END IF;
    
    IF old.spot_comment!=new.spot_comment THEN
      SET updated_values=(SELECT concat_string(updated_values,'SpotRate Comment',old.spot_comment,new.spot_comment));
    END IF;
    
    IF old.spot_rate_uom!=new.spot_rate_uom THEN
      SET updated_values=(SELECT concat_string(updated_values,'SpotRate W/M',old.spot_rate_uom,new.spot_rate_uom));
    END IF;
    
    IF old.spot_wm_rate!=new.spot_wm_rate THEN
      SET updated_values=(SELECT concat_string(updated_values,'SpotRate',CONCAT('$',old.spot_wm_rate),CONCAT('$',new.spot_wm_rate)));
    END IF;
    
    IF old.spot_rate_bottom!=new.spot_rate_bottom THEN
	SET updated_values = (SELECT concat_string(updated_values,'T&T','','Bottom Line Spot Rate'));
    END IF;
     
    IF old.spot_rate_ofrate!=new.spot_rate_ofrate THEN
	SET updated_values = (SELECT concat_string(updated_values,'T&T','','Ocean Freight Only Spot Rate'));
    END IF;
     
    IF old.default_agent!=new.default_agent THEN
	SET updated_values = (SELECT concat_string(updated_values,'Default Agent',IF(old.default_agent=0,'No','Yes'),IF(new.default_agent=0,'No','Yes')));
    END IF;
     
    IF old.agent_acct_no!=new.agent_acct_no THEN
	SET updated_values = (SELECT concat_string(updated_values,'Agent Acct No#',old.agent_acct_no,new.agent_acct_no));
	SET updated_values = (SELECT concat_string(updated_values,'Agent Acct Name',(SELECT acct_name FROM trading_partner WHERE acct_no=old.agent_acct_no),(SELECT acct_name FROM trading_partner WHERE acct_no=new.agent_acct_no)));
    END IF;
    
    IF old.non_rated!=new.non_rated THEN
	SET updated_values = (SELECT concat_string(updated_values,'ERT',IF(old.rtd_transaction=0,'No','Yes'),IF(new.rtd_transaction=0,'No','Yes')));
     END IF;
     
     IF old.rtd_agent_acct_no!=new.rtd_agent_acct_no THEN
	SET updated_values = (SELECT concat_string(updated_values,'Agent Info',old.rtd_agent_acct_no,new.rtd_agent_acct_no));
     END IF;
     
     IF old.client_pwk_recvd!=new.client_pwk_recvd THEN
	SET updated_values = (SELECT concat_string(updated_values,'PWK',IF(old.client_pwk_recvd=0,'No','Yes'),IF(new.client_pwk_recvd=0,'No','Yes')));
     END IF;
     
     IF old.rtd_agent_acct_no!=new.rtd_agent_acct_no THEN
	SET updated_values = (SELECT concat_string(updated_values,'Bill To Party',old.bill_to_party,new.bill_to_party));
     END IF;
     
     IF old.third_party_acct_no!=new.third_party_acct_no THEN
	SET updated_values = (SELECT concat_string(updated_values,'Third Party Acct No#',old.third_party_acct_no,new.third_party_acct_no));
	SET updated_values = (SELECT concat_string(updated_values,'Third Party Acct',(SELECT acct_name FROM trading_partner WHERE acct_no=old.third_party_acct_no),(SELECT acct_name FROM trading_partner WHERE acct_no=new.third_party_acct_no)));
     END IF;
    
    IF old.over_short_damaged!=new.over_short_damaged THEN
	SET updated_values = (SELECT concat_string(updated_values,'Over/Short/Damage',IF(old.over_short_damaged=0,'No','Yes'),IF(new.over_short_damaged=0,'No','Yes')));
    END IF;
	
    IF old.non_rated!=new.non_rated THEN
	SET updated_values = (SELECT concat_string(updated_values,'Unknown Dest',IF(old.non_rated=0,'No','Yes'),IF(new.non_rated=0,'No','Yes')));
     END IF;
     
    IF old.quote_complete!=new.quote_complete THEN
      SET updated_values=(SELECT concat_string(updated_values,'Quote Complete',IF(old.quote_complete=0,'No','Yes'),IF(new.quote_complete=0,'No','Yes')));
      SET updated_values='Quote is Completed';
    END IF;
   
    IF updated_values IS NOT NULL THEN
    SET updated_values=CONCAT('UPDATED ->',updated_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(old.file_number_id,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
      END;
$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER  after_LclQuotePiece_update$$

CREATE TRIGGER after_LclQuotePiece_update AFTER UPDATE ON lcl_quote_piece
    FOR EACH ROW BEGIN
	DECLARE updated_values TEXT ;
	
	IF new.commodity_type_id!=old.commodity_type_id THEN
		SET updated_values=(SELECT concat_string(updated_values,'Tariff',(SELECT desc_en FROM commodity_type WHERE id=old.commodity_type_id),(SELECT desc_en FROM commodity_type WHERE id=new.commodity_type_id)));
		SET updated_values=(SELECT concat_string(updated_values,'Tariff#',(SELECT CODE FROM commodity_type WHERE id=old.commodity_type_id),(SELECT CODE FROM commodity_type WHERE id=new.commodity_type_id)));
	END IF;

	IF new.harmonized_code!=old.harmonized_code THEN
		SET updated_values=(SELECT concat_string(updated_values,'Harmonized Code',old.harmonized_code,new.harmonized_code));
	END IF;

	IF new.packaging_type_id!=old.packaging_type_id THEN
		SET updated_values=(SELECT concat_string(updated_values,'Package',(SELECT description FROM package_type WHERE id=old.packaging_type_id),(SELECT description FROM package_type WHERE id=new.packaging_type_id)));
	END IF;

	IF new.booked_piece_count!=old.booked_piece_count THEN
		SET updated_values=(SELECT concat_string(updated_values,'Booked Piece Count',old.booked_piece_count,new.booked_piece_count));
	END IF;

	IF new.booked_weight_imperial!=old.booked_weight_imperial THEN
		SET updated_values=(SELECT concat_string(updated_values,'Booked Weight Imperial',old.booked_weight_imperial,new.booked_weight_imperial));
	END IF;

	IF new.booked_weight_metric!=old.booked_weight_metric THEN
		SET updated_values=(SELECT concat_string(updated_values,'Booked Weight Metric',old.booked_weight_metric,new.booked_weight_metric));
	END IF;

	IF new.booked_volume_imperial!=old.booked_volume_imperial THEN
		SET updated_values=(SELECT concat_string(updated_values,'Booked Volume Imperial',old.booked_volume_imperial,new.booked_volume_imperial));
	END IF;

	IF new.booked_volume_metric!=old.booked_volume_metric THEN
		SET updated_values=(SELECT concat_string(updated_values,'Booked Volume Metric',old.booked_volume_metric,new.booked_volume_metric));
	END IF;

	IF new.hazmat!=old.hazmat THEN
		SET updated_values=(SELECT concat_string(updated_values,'Hazmat',IF(old.hazmat=0,'No','Yes'),IF(new.hazmat=0,'No','Yes')));
	END IF;

	IF new.personal_effects!=old.personal_effects THEN
		SET updated_values=(SELECT concat_string(updated_values,'Personal Effects',old.personal_effects,new.personal_effects));
	END IF;

	IF new.refrigeration_required!=old.refrigeration_required THEN
		SET updated_values=(SELECT concat_string(updated_values,'Refrigeration',IF(old.refrigeration_required=0,'No','Yes'),IF(new.refrigeration_required=0,'No','Yes')));
	END IF;

	IF new.weight_verified!=old.weight_verified THEN
		SET updated_values=(SELECT concat_string(updated_values,'Weight Verified',IF(old.weight_verified=0,'No','Yes'),IF(new.weight_verified=0,'No','Yes')));
	END IF;

	IF new.is_barrel!=old.is_barrel THEN
		SET updated_values=(SELECT concat_string(updated_values,'Barrel Business',IF(old.is_barrel=0,'No','Yes'),IF(new.is_barrel=0,'No','Yes')));
	END IF;

	IF new.mark_no_desc!=old.mark_no_desc THEN
		SET updated_values=(SELECT concat_string(updated_values,'Marks And Numbers',old.mark_no_desc,new.mark_no_desc));
	END IF;

	IF new.piece_desc!=old.piece_desc THEN
		SET updated_values=(SELECT concat_string(updated_values,'Commodity Desc',old.piece_desc,new.piece_desc));
	END IF;
	
        IF updated_values IS NOT NULL THEN
        SET updated_values=CONCAT('UPDATED ->',updated_values);
		INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
		VALUES(old.file_number_id,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
	END IF;
        END;
$$
DELIMITER ;

DELIMITER $$

DROP TRIGGER  after_LclQuotePieceDetails_update$$

CREATE TRIGGER after_LclQuotePieceDetails_update AFTER UPDATE ON lcl_quote_piece_detail
    FOR EACH ROW BEGIN
	DECLARE updated_values TEXT ;
	
	IF old.actual_uom!=new.actual_uom THEN 
		SET updated_values=(SELECT concat_string(updated_values,'UOM',old.actual_uom,new.actual_uom));
	END IF;

	IF old.actual_length!=new.actual_length THEN 
		SET updated_values=(SELECT concat_string(updated_values,'Length(IN)',old.actual_length,new.actual_length));
	END IF;

	IF old.actual_width!=new.actual_width THEN 
		SET updated_values=(SELECT concat_string(updated_values,'Width(IN)',old.actual_width,new.actual_width));
	END IF;

	IF old.actual_height!=new.actual_height THEN 
		SET updated_values=(SELECT concat_string(updated_values,'Height(IN)',old.actual_height,new.actual_height));
	END IF;

	IF old.piece_count!=new.piece_count THEN 
		SET updated_values=(SELECT concat_string(updated_values,'Pieces',old.piece_count,new.piece_count));
	END IF;

	IF old.stowed_location!=new.stowed_location THEN 
		SET updated_values=(SELECT concat_string(updated_values,'Warehouse Line',old.stowed_location,new.stowed_location));
	END IF;

	IF old.piece_count!=new.piece_count THEN 
		SET updated_values=(SELECT concat_string(updated_values,'Weight/PC(LBS)',old.piece_count,new.piece_count));
	END IF;
	
	IF updated_values IS NOT NULL THEN
	SET @fileId=(SELECT file_number_id FROM lcl_quote_piece WHERE id=new.quote_piece_id);
        SET updated_values=CONCAT('UPDATED ->',updated_values);
		INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
		VALUES(@fileId,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
	END IF;
        END;
$$
DELIMITER ;

DELIMITER $$

DROP TRIGGER  after_LclQuotePieceWhse_update$$

CREATE TRIGGER after_LclQuotePieceWhse_update AFTER UPDATE ON lcl_quote_piece_whse
    FOR EACH ROW BEGIN
	DECLARE updated_values TEXT ;
	
	IF old.warehouse_id!=new.warehouse_id THEN 
		SET updated_values=(SELECT concat_string(updated_values,'Warehouse Location',(SELECT warehsname FROM warehouse WHERE id=old.warehouse_id),(SELECT warehsname FROM warehouse WHERE id=new.warehouse_id)));
		SET updated_values=(SELECT concat_string(updated_values,'Warehouse Location Code',(SELECT warehsno FROM warehouse WHERE id=old.warehouse_id),(SELECT warehsno FROM warehouse WHERE id=new.warehouse_id)));
	END IF;

	IF old.location!=new.location THEN 
		SET updated_values=(SELECT concat_string(updated_values,'Line Location',old.location,new.location));
	END IF;
	
	IF updated_values IS NOT NULL THEN
		SET @fileId=(SELECT file_number_id FROM lcl_quote_piece WHERE id=new.quote_piece_id);
        SET updated_values=CONCAT('UPDATED ->',updated_values);
	     INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
	     VALUES(@fileId,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
	END IF;
        END;
$$
DELIMITER ;

DELIMITER $$

DROP TRIGGER  after_LclQuoteHazmat_update$$

CREATE TRIGGER after_LclQuoteHazmat_update AFTER UPDATE ON lcl_quote_hazmat
    FOR EACH ROW BEGIN
	DECLARE updated_values TEXT ;
	
      IF old.un_hazmat_no!=new.un_hazmat_no THEN
		SET updated_values = (SELECT concat_string(updated_values,'UN#',old.un_hazmat_no,new.un_hazmat_no));
      END IF;

      IF old.proper_shipping_name!=new.proper_shipping_name THEN
		SET updated_values = (SELECT concat_string(updated_values,'Proper Shipping Name',old.proper_shipping_name,new.proper_shipping_name));
      END IF;

      IF old.technical_name!=new.technical_name THEN
		SET updated_values = (SELECT concat_string(updated_values,'Tech.Chemical Name',old.technical_name,new.technical_name));
      END IF;

      IF old.imo_pri_class_code!=new.imo_pri_class_code THEN
		SET updated_values = (SELECT concat_string(updated_values,'Class',old.imo_pri_class_code,new.imo_pri_class_code));
      END IF;

      IF old.imo_pri_sub_class_code!=new.imo_pri_sub_class_code THEN
		SET updated_values = (SELECT concat_string(updated_values,'IMO Subsidary Class',old.imo_pri_sub_class_code,new.imo_pri_sub_class_code));
      END IF;

      IF old.imo_sec_sub_class_code!=new.imo_sec_sub_class_code THEN
		SET updated_values = (SELECT concat_string(updated_values,'IMO Secondary Class',old.imo_sec_sub_class_code,new.imo_sec_sub_class_code));
      END IF;

      IF old.packing_group_code!=new.packing_group_code THEN
		SET updated_values = (SELECT concat_string(updated_values,'Packaging Group Code',old.packing_group_code,new.packing_group_code));
      END IF;

      IF old.flash_point!=new.flash_point THEN
		SET updated_values = (SELECT concat_string(updated_values,'Flash point',old.flash_point,new.flash_point));
      END IF;

      IF old.outer_pkg_no_pieces!=new.outer_pkg_no_pieces THEN
		SET updated_values = (SELECT concat_string(updated_values,'Outer Packaging Pieces',old.outer_pkg_no_pieces,new.outer_pkg_no_pieces));
      END IF;

      IF old.outer_pkg_type!=new.outer_pkg_type THEN
		SET updated_values = (SELECT concat_string(updated_values,'Outer Packaging Type',old.outer_pkg_type,new.outer_pkg_type));
      END IF;

      IF old.outer_pkg_composition!=new.outer_pkg_composition THEN
		SET updated_values = (SELECT concat_string(updated_values,'Outer Pkg Composition',old.outer_pkg_composition,new.outer_pkg_composition));
      END IF;

      IF old.inner_pkg_no_pieces!=new.inner_pkg_no_pieces THEN
		SET updated_values = (SELECT concat_string(updated_values,'Inner Packaging Pieces',old.inner_pkg_no_pieces,new.inner_pkg_no_pieces));
      END IF;

      IF old.inner_pkg_type!=new.inner_pkg_type THEN
		SET updated_values = (SELECT concat_string(updated_values,'Inner Packaging Type',old.inner_pkg_type,new.inner_pkg_type));
      END IF;

      IF old.inner_pkg_composition!=new.inner_pkg_composition THEN
		SET updated_values = (SELECT concat_string(updated_values,'Inner Pkg Composition',old.inner_pkg_composition,new.inner_pkg_composition));
      END IF;

      IF old.inner_pkg_uom!=new.inner_pkg_uom THEN
		SET updated_values = (SELECT concat_string(updated_values,'Inner Pkg UOM',old.inner_pkg_uom,new.inner_pkg_uom));
      END IF;

      IF old.inner_pkg_nwt_piece!=new.inner_pkg_nwt_piece THEN
		SET updated_values = (SELECT concat_string(updated_values,'Inner Pkg Wt/Vol Per Piece',old.inner_pkg_nwt_piece,new.inner_pkg_nwt_piece));
      END IF;

      IF old.total_net_weight!=new.total_net_weight THEN
		SET updated_values = (SELECT concat_string(updated_values,'Total Net Weight/Kgs',old.total_net_weight,new.total_net_weight));
      END IF;

      IF old.liquid_volume!=new.liquid_volume THEN
		SET updated_values = (SELECT concat_string(updated_values,'Total Volume',old.liquid_volume,new.liquid_volume));
      END IF;

      IF old.total_gross_weight!=new.total_gross_weight THEN
		SET updated_values = (SELECT concat_string(updated_values,'Total Gross Weight/Kgs',old.total_gross_weight,new.total_gross_weight));
      END IF;

      IF old.ems_code!=new.ems_code THEN
		SET updated_values = (SELECT concat_string(updated_values,'EMS code',old.ems_code,new.ems_code));
      END IF;

      IF old.emergency_contact_id!=new.emergency_contact_id THEN
		SET updated_values = (SELECT concat_string(updated_values,'Emergency Contact',(SELECT contact_name FROM lcl_contact WHERE id=old.emergency_contact_id),(SELECT contact_name FROM lcl_contact WHERE id=new.emergency_contact_id)));
		SET updated_values = (SELECT concat_string(updated_values,'Emergency Phone No',(SELECT phone1 FROM lcl_contact WHERE id=old.emergency_contact_id),(SELECT phone1 FROM lcl_contact WHERE id=new.emergency_contact_id)));
      END IF;

      IF old.reportable_quantity!=new.reportable_quantity THEN
		SET updated_values = (SELECT concat_string(updated_values,'Reportable Quantity',IF(old.reportable_quantity=0,'No','Yes'),IF(new.reportable_quantity=0,'No','Yes')));
      END IF;

      IF old.marine_pollutant!=new.marine_pollutant THEN
		SET updated_values = (SELECT concat_string(updated_values,'Marine pollutant',IF(old.marine_pollutant=0,'No','Yes'),IF(new.marine_pollutant=0,'No','Yes')));
      END IF;

      IF old.excepted_quantity!=new.excepted_quantity THEN
		SET updated_values = (SELECT concat_string(updated_values,'Excepted Quantity',IF(old.excepted_quantity=0,'No','Yes'),IF(new.excepted_quantity=0,'No','Yes')));
      END IF;

      IF old.limited_quantity!=new.limited_quantity THEN
		SET updated_values = (SELECT concat_string(updated_values,'Limited Quantity',IF(old.limited_quantity=0,'No','Yes'),IF(new.limited_quantity=0,'No','Yes')));
      END IF;

      IF old.residue!=new.residue THEN
		SET updated_values = (SELECT concat_string(updated_values,'Residue',IF(old.residue=0,'No','Yes'),IF(new.residue=0,'No','Yes')));
      END IF;

      IF old.inhalation_hazard!=new.inhalation_hazard THEN
		SET updated_values = (SELECT concat_string(updated_values,'Inhalation Hazard',IF(old.inhalation_hazard=0,'No','Yes'),IF(new.inhalation_hazard=0,'No','Yes')));
      END IF;
	
	IF updated_values IS NOT NULL THEN
        SET updated_values=CONCAT('UPDATED ->',updated_values);
	     INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
	     VALUES(old.file_number_id,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
	END IF;
        END;
$$
DELIMITER ;

DELIMITER $$

DROP TRIGGER after_LclQuotePad_update$$

CREATE TRIGGER after_LclQuotePad_update AFTER UPDATE ON lcl_quote_pad
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT;
	
    IF old.issuing_terminal!=new.issuing_terminal THEN
       SET updated_values = (SELECT concat_string(updated_values,'Issued By',(SELECT CONCAT(UPPER(login_name),'/',terminal_id) FROM user_details WHERE user_id=old.entered_by_user_id),(SELECT CONCAT(UPPER(login_name),'/',terminal_id) FROM user_details WHERE user_id=new.entered_by_user_id)));
    END IF;
   
    IF old.pickup_to!=new.pickup_to THEN
       SET updated_values = (SELECT concat_string(updated_values,'To',old.pickup_to,new.pickup_to));
    END IF;
    
    IF old.pickup_contact_id!=new.pickup_contact_id THEN
    
       SELECT company_name,contact_name,city,address,zip,phone1,fax1,email1 INTO @oldCompName,@oldContName,@oldCity,@oldAddress,@oldZip,@oldPhone,@oldFax,@oldEmail FROM lcl_contact WHERE id=old.pickup_contact_id;
       SELECT company_name,contact_name,city,address,zip,phone1,fax1,email1 INTO @compName,@contName,@city,@address,@zip,@phone,@fax,@email FROM lcl_contact WHERE id=new.pickup_contact_id;
       
       IF @oldCity!=@city THEN
		SET updated_values = (SELECT concat_string(updated_values,'Door Origin/City/Zip',@oldCity,@city));
       END IF;
       IF @oldCompName!=@compName THEN
		SET updated_values = (SELECT concat_string(updated_values,'Ship/Supplier',@oldCompName,@compName));
       END IF;
       IF @oldContName!=@contName THEN
		SET updated_values = (SELECT concat_string(updated_values,'Contact Name',@oldContName,@contName));
       END IF;
       IF @oldAddress!=@address THEN
		SET updated_values = (SELECT concat_string(updated_values,'Address',@oldAddress,@address));
       END IF;
       IF @oldZip!=@zip THEN
		SET updated_values = (SELECT concat_string(updated_values,'Zip',@oldZip,@zip));
       END IF;
       IF @oldPhone!=@phone THEN
		SET updated_values = (SELECT concat_string(updated_values,'Phone',@oldPhone,@phone));
       END IF;
       IF @oldFax!=@fax THEN
		SET updated_values = (SELECT concat_string(updated_values,'Fax',@oldFax,@fax));
       END IF;
       IF @oldEmail!=@email THEN
		SET updated_values = (SELECT concat_string(updated_values,'Email',@oldEmail,@email));
       END IF;
       
    END IF;
    
    IF old.pickup_hours!=new.pickup_hours THEN
       SET updated_values = (SELECT concat_string(updated_values,'Shipper Hours',old.pickup_hours,new.pickup_hours));
    END IF;
    
    IF old.pickup_ready_note!=new.pickup_ready_note THEN
       SET updated_values = (SELECT concat_string(updated_values,'CutOff Note',old.pickup_ready_note,new.pickup_ready_note));
    END IF;
    
    IF old.pickup_ready_date!=new.pickup_ready_date THEN
       SET updated_values = (SELECT concat_string(updated_values,'Ready Date',old.pickup_ready_date,new.pickup_ready_date));
    END IF;
    
    IF old.pickup_cutoff_date!=new.pickup_cutoff_date THEN
       SET updated_values = (SELECT concat_string(updated_values,'CutOff Date',old.pickup_cutoff_date,new.pickup_cutoff_date));
    END IF;
    
    IF old.scac!=new.scac THEN
       SET updated_values = (SELECT concat_string(updated_values,'Scac Code',old.scac,new.scac));
    END IF;
    
    IF old.quote_ac_id!=new.quote_ac_id THEN
       SET updated_values = (SELECT concat_string(updated_values,'Pickup Charge/Sell',(SELECT ar_amount FROM lcl_quote_ac WHERE id=old.quote_ac_id),(SELECT ar_amount FROM lcl_quote_ac WHERE id=new.quote_ac_id)));
       SET updated_values = (SELECT concat_string(updated_values,'Pickup Cost',(SELECT ap_amount FROM lcl_quote_ac WHERE id=old.quote_ac_id),(SELECT ap_amount FROM lcl_quote_ac WHERE id=new.quote_ac_id)));
    END IF;
    
    IF old.pickup_reference_no!=new.pickup_reference_no THEN
       SET updated_values = (SELECT concat_string(updated_values,'Ref#/PRO#',old.pickup_reference_no,new.pickup_reference_no));
    END IF;
    
    IF old.delivery_contact_id!=new.delivery_contact_id THEN
       SET updated_values = (SELECT concat_string(updated_values,'DEL TO WHSE WhseName',(SELECT company_name FROM lcl_contact WHERE id=old.delivery_contact_id),(SELECT company_name FROM lcl_contact WHERE id=new.delivery_contact_id)));
       SET updated_values = (SELECT concat_string(updated_values,'Address',(SELECT address FROM lcl_contact WHERE id=old.delivery_contact_id),(SELECT address FROM lcl_contact WHERE id=new.delivery_contact_id)));
       SET updated_values = (SELECT concat_string(updated_values,'City/State/Zip',(SELECT CONCAT(city,'/',state,'/',zip) FROM lcl_contact WHERE id=old.delivery_contact_id),(SELECT CONCAT(city,'/',state,'/',zip) FROM lcl_contact WHERE id=new.delivery_contact_id)));
       SET updated_values = (SELECT concat_string(updated_values,'Phone',(SELECT phone1 FROM lcl_contact WHERE id=old.delivery_contact_id),(SELECT phone1 FROM lcl_contact WHERE id=new.delivery_contact_id)));
    END IF;
    
    IF old.pickup_instructions!=new.pickup_instructions THEN
       SET updated_values = (SELECT concat_string(updated_values,'Instructions',old.pickup_instructions,new.pickup_instructions));
    END IF;
    
    IF old.commodity_desc!=new.commodity_desc THEN
       SET updated_values = (SELECT concat_string(updated_values,'Commodity',old.commodity_desc,new.commodity_desc));
    END IF;
    
    IF old.terms_of_service!=new.terms_of_service THEN
       SET updated_values = (SELECT concat_string(updated_values,'TOS',old.terms_of_service,new.terms_of_service));
    END IF;
      	
    IF updated_values IS NOT NULL THEN
        SET updated_values=CONCAT('UPDATED ->',updated_values);
	     INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
	     VALUES(old.file_number_id,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;

-- Booking Module Update Triggers

DELIMITER $$

DROP TRIGGER after_LclBooking_update$$

CREATE TRIGGER after_LclBooking_update AFTER UPDATE ON lcl_booking
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT ;
    
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
    
    IF old.rate_type!=new.rate_type THEN
	SET updated_values=(SELECT concat_string(updated_values,'Rate Type',old.rate_type,new.rate_type));
    END IF;
    
    IF old.poo_pickup!=new.poo_pickup THEN
      SET updated_values=(SELECT concat_string(updated_values,'Pickup',IF(old.poo_pickup=0,'No','Yes'),IF(new.poo_pickup=0,'No','Yes')));
    END IF;
    
    IF old.relay_override!=new.relay_override THEN
      SET updated_values=(SELECT concat_string(updated_values,'Relay Ovr',IF(old.relay_override=0,'No','Yes'),IF(new.relay_override=0,'No','Yes')));
    END IF;
    
    IF old.poo_whse_contact_id!=new.poo_whse_contact_id THEN
	SET updated_values=(SELECT concat_string(updated_values,'Delivery Cargo to',(SELECT CONCAT(company_name,' / ',address) FROM lcl_contact WHERE id=old.poo_whse_contact_id),(SELECT CONCAT(company_name,' / ',address) FROM lcl_contact WHERE id=new.poo_whse_contact_id)));
    END IF;
    
    IF old.billing_terminal!=new.billing_terminal THEN
       SET updated_values=(SELECT concat_string(updated_values,'Term to do BL',(SELECT terminal_location FROM terminal WHERE trmnum=old.billing_terminal),(SELECT terminal_location FROM terminal WHERE trmnum=new.billing_terminal)));
    END IF;
    
    IF old.master_schedule_id!=new.master_schedule_id THEN
    
       SELECT ls.schedule_no,tp.acct_name,ls.sp_reference_no,ls.sp_reference_name,ls.pol_pg,ls.pol_etd,ls.poo_lrdt,ls.fd_eta
       INTO @oldSchdNo,@oldSpAcctNo,@oldSpRefNo,@oldSpRefName,@oldPolPg,@oldPolEtd,@oldPooLrd,@oldFdEta
       FROM lcl_schedule ls LEFT JOIN trading_partner tp ON ls.sp_acct_no=tp.acct_no WHERE ls.id=old.master_schedule_id;
       
       SELECT ls.schedule_no,tp.acct_name,ls.sp_reference_no,ls.sp_reference_name,ls.pol_pg,ls.pol_etd,ls.poo_lrdt,ls.fd_eta
       INTO @schdNo,@spAcctNo,@spRefNo,@spRefName,@polPg,@polEtd,@pooLrd,@fdEta
       FROM lcl_schedule ls LEFT JOIN trading_partner tp ON ls.sp_acct_no=tp.acct_no WHERE ls.id=new.master_schedule_id;
       
       IF @oldSchdNo!=@schdNo THEN
		SET updated_values = (SELECT concat_string(updated_values,'Booked For Voyage ->ECI Voy#',@oldSchdNo,@schdNo));
       END IF;
       IF @oldSpAcctNo!=@spAcctNo THEN
		SET updated_values = (SELECT concat_string(updated_values,'SS Line',@oldSpAcctNo,@spAcctNo));
       END IF;
       IF @oldSpRefNo!=@spRefNo THEN
		SET updated_values = (SELECT concat_string(updated_values,'SS Voy',@oldSpRefNo,@spRefNo));
       END IF;
       IF @oldSpRefName!=@spRefName THEN
		SET updated_values = (SELECT concat_string(updated_values,'Vessel Name',@oldSpRefName,@spRefName));
       END IF;
       IF @oldPolPg!=@polPg THEN
		SET updated_values = (SELECT concat_string(updated_values,'Pier',@oldPolPg,@polPg));
       END IF;
       IF @oldPolEtd!=@polEtd THEN
		SET updated_values = (SELECT concat_string(updated_values,'Sail Date',@oldPolEtd,@polEtd));
       END IF;
       IF @oldPooLrd!=@pooLrd THEN
		SET updated_values = (SELECT concat_string(updated_values,'Origin LRD',@oldPooLrd,@pooLrd));
       END IF;
       IF @oldFdEta!=@fdEta THEN
		SET updated_values = (SELECT concat_string(updated_values,'ETA FD',@oldFdEta,@fdEta));
       END IF;
       
    END IF;
    
    IF old.billing_type!=new.billing_type THEN
       SET updated_values=(SELECT concat_string(updated_values,'Billing Type',old.billing_type,new.billing_type));
    END IF;
    
    IF old.insurance!=new.insurance THEN
       SET updated_values=(SELECT concat_string(updated_values,'Insurance',IF(old.insurance=0,'No','Yes'),IF(new.insurance=0,'No','Yes')));
    END IF;
    
    IF old.value_of_goods!=new.value_of_goods THEN
       SET updated_values=(SELECT concat_string(updated_values,'Value of Goods',old.value_of_goods,new.value_of_goods));
    END IF;
        
    IF old.delivery_metro!=new.delivery_metro THEN
       SET updated_values=(SELECT concat_string(updated_values,'Delivery Metro',old.delivery_metro,new.delivery_metro));
    END IF;
      
    IF old.documentation!=new.documentation THEN
       SET updated_values=(SELECT concat_string(updated_values,'Documentation',IF(old.documentation=0,'No','Yes'),IF(new.documentation=0,'No','Yes')));
    END IF;
    
    IF old.spot_rate!=new.spot_rate THEN
      SET updated_values=(SELECT concat_string(updated_values,'SpotRate',IF(old.spot_rate=0,'No','Yes'),IF(new.spot_rate=0,'No','Yes')));
    END IF;
    
    IF old.spot_comment!=new.spot_comment THEN
      SET updated_values=(SELECT concat_string(updated_values,'SpotRate Comment',old.spot_comment,new.spot_comment));
    END IF;
    
    IF old.spot_rate_uom!=new.spot_rate_uom THEN
      SET updated_values=(SELECT concat_string(updated_values,'SpotRate W/M',old.spot_rate_uom,new.spot_rate_uom));
    END IF;
    
    IF old.spot_wm_rate!=new.spot_wm_rate THEN
      SET updated_values=(SELECT concat_string(updated_values,'SpotRate',CONCAT('$',old.spot_wm_rate),CONCAT('$',new.spot_wm_rate)));
    END IF;
    
    IF old.spot_rate_bottom!=new.spot_rate_bottom THEN
	SET updated_values = (SELECT concat_string(updated_values,'T&T','','Bottom Line Spot Rate'));
    END IF;
     
    IF old.spot_rate_ofrate!=new.spot_rate_ofrate THEN
	SET updated_values = (SELECT concat_string(updated_values,'T&T','','Ocean Freight Only Spot Rate'));
    END IF;
     
    IF old.default_agent!=new.default_agent THEN
	SET updated_values = (SELECT concat_string(updated_values,'Default Agent',IF(old.default_agent=0,'No','Yes'),IF(new.default_agent=0,'No','Yes')));
    END IF;
     
    IF old.agent_acct_no!=new.agent_acct_no THEN
	SET updated_values = (SELECT concat_string(updated_values,'Agent Acct No#',old.agent_acct_no,new.agent_acct_no));
	SET updated_values = (SELECT concat_string(updated_values,'Agent Acct Name',(SELECT acct_name FROM trading_partner WHERE acct_no=old.agent_acct_no),(SELECT acct_name FROM trading_partner WHERE acct_no=new.agent_acct_no)));
    END IF;
    
    IF old.non_rated!=new.non_rated THEN
	SET updated_values = (SELECT concat_string(updated_values,'ERT',IF(old.rtd_transaction=0,'No','Yes'),IF(new.rtd_transaction=0,'No','Yes')));
     END IF;
     
     IF old.rtd_agent_acct_no!=new.rtd_agent_acct_no THEN
	SET updated_values = (SELECT concat_string(updated_values,'Agent Info',old.rtd_agent_acct_no,new.rtd_agent_acct_no));
     END IF;
     
     IF old.client_pwk_recvd!=new.client_pwk_recvd THEN
	SET updated_values = (SELECT concat_string(updated_values,'PWK',IF(old.client_pwk_recvd=0,'No','Yes'),IF(new.client_pwk_recvd=0,'No','Yes')));
     END IF;
     
     IF old.rtd_agent_acct_no!=new.rtd_agent_acct_no THEN
	SET updated_values = (SELECT concat_string(updated_values,'Bill To Party',old.bill_to_party,new.bill_to_party));
     END IF;
     
     IF old.third_party_acct_no!=new.third_party_acct_no THEN
	SET updated_values = (SELECT concat_string(updated_values,'Third Party Acct No#',old.third_party_acct_no,new.third_party_acct_no));
	SET updated_values = (SELECT concat_string(updated_values,'Third Party Acct',(SELECT acct_name FROM trading_partner WHERE acct_no=old.third_party_acct_no),(SELECT acct_name FROM trading_partner WHERE acct_no=new.third_party_acct_no)));
     END IF;
    
    IF old.over_short_damaged!=new.over_short_damaged THEN
	SET updated_values = (SELECT concat_string(updated_values,'Over/Short/Damage',IF(old.over_short_damaged=0,'No','Yes'),IF(new.over_short_damaged=0,'No','Yes')));
    END IF;
	
    IF old.non_rated!=new.non_rated THEN
	SET updated_values = (SELECT concat_string(updated_values,'Unknown Dest',IF(old.non_rated=0,'No','Yes'),IF(new.non_rated=0,'No','Yes')));
     END IF;
   
    IF updated_values IS NOT NULL THEN
    SET updated_values=CONCAT('UPDATED ->',updated_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(old.file_number_id,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
      END;
$$
DELIMITER ;

DELIMITER $$

DROP TRIGGER after_LclBookingPiece_update$$

CREATE TRIGGER after_LclBookingPiece_update AFTER UPDATE ON lcl_booking_piece 
    FOR EACH ROW BEGIN
	DECLARE updated_values TEXT ;
	DECLARE change_value BOOLEAN ;
	
	IF new.commodity_type_id!=old.commodity_type_id THEN
		SET updated_values=(SELECT concat_string(updated_values,'Tariff',(SELECT desc_en FROM commodity_type WHERE id=old.commodity_type_id),(SELECT desc_en FROM commodity_type WHERE id=new.commodity_type_id)));
		SET updated_values=(SELECT concat_string(updated_values,'Tariff#',(SELECT CODE FROM commodity_type WHERE id=old.commodity_type_id),(SELECT CODE FROM commodity_type WHERE id=new.commodity_type_id)));
	END IF;

	IF new.harmonized_code!=old.harmonized_code THEN
		SET updated_values=(SELECT concat_string(updated_values,'Harmonized Code',old.harmonized_code,new.harmonized_code));
	END IF;

	IF new.packaging_type_id!=old.packaging_type_id THEN
		SET updated_values=(SELECT concat_string(updated_values,'Package',(SELECT description FROM package_type WHERE id=old.packaging_type_id),(SELECT description FROM package_type WHERE id=new.packaging_type_id)));
	END IF;

	IF new.booked_piece_count!=old.booked_piece_count THEN
		SET updated_values=(SELECT concat_string(updated_values,'Booked Piece Count',old.booked_piece_count,new.booked_piece_count));
	END IF;

	IF new.booked_weight_imperial!=old.booked_weight_imperial THEN
		SET updated_values=(SELECT concat_string(updated_values,'Booked Weight Imperial',old.booked_weight_imperial,new.booked_weight_imperial));
	END IF;

	IF new.booked_weight_metric!=old.booked_weight_metric THEN
		SET updated_values=(SELECT concat_string(updated_values,'Booked Weight Metric',old.booked_weight_metric,new.booked_weight_metric));
	END IF;

	IF new.booked_volume_imperial!=old.booked_volume_imperial THEN
		SET updated_values=(SELECT concat_string(updated_values,'Booked Volume Imperial',old.booked_volume_imperial,new.booked_volume_imperial));
	END IF;

	IF new.booked_volume_metric!=old.booked_volume_metric THEN
		SET updated_values=(SELECT concat_string(updated_values,'Booked Volume Metric',old.booked_volume_metric,new.booked_volume_metric));
	END IF;
	
	IF new.actual_piece_count!=old.actual_piece_count THEN
		SET updated_values=(SELECT concat_string(updated_values,'Actual Piece Count',old.actual_piece_count,new.actual_piece_count));
	END IF;
	
	IF new.actual_weight_imperial!=old.actual_weight_imperial THEN
		SET updated_values=(SELECT concat_string(updated_values,'Actual Weight Imperial',old.actual_weight_imperial,new.actual_weight_imperial));
	END IF;
	
	IF new.actual_weight_metric!=old.actual_weight_metric THEN
		SET updated_values=(SELECT concat_string(updated_values,'Actual Weight Metric',old.actual_weight_metric,new.actual_weight_metric));
	END IF;
	
	IF new.actual_volume_imperial!=old.actual_volume_imperial THEN
		SET updated_values=(SELECT concat_string(updated_values,'Actual Volume Imperial',old.actual_volume_imperial,new.actual_volume_imperial));
	END IF;
	
	IF new.actual_volume_metric!=old.actual_volume_metric THEN
		SET updated_values=(SELECT concat_string(updated_values,'Actual Volume Metric',old.actual_volume_metric,new.actual_volume_metric));
	END IF;

	IF new.hazmat!=old.hazmat THEN
		SET updated_values=(SELECT concat_string(updated_values,'Hazmat',IF(old.hazmat=0,'No','Yes'),IF(new.hazmat=0,'No','Yes')));
	END IF;

	IF new.personal_effects!=old.personal_effects THEN
		SET updated_values=(SELECT concat_string(updated_values,'Personal Effects',old.personal_effects,new.personal_effects));
	END IF;

	IF new.refrigeration_required!=old.refrigeration_required THEN
		SET updated_values=(SELECT concat_string(updated_values,'Refrigeration',IF(old.refrigeration_required=0,'No','Yes'),IF(new.refrigeration_required=0,'No','Yes')));
	END IF;

	IF new.weight_verified!=old.weight_verified THEN
		SET updated_values=(SELECT concat_string(updated_values,'Weight Verified',IF(old.weight_verified=0,'No','Yes'),IF(new.weight_verified=0,'No','Yes')));
	END IF;

	IF new.is_barrel!=old.is_barrel THEN
		SET updated_values=(SELECT concat_string(updated_values,'Barrel Business',IF(old.is_barrel=0,'No','Yes'),IF(new.is_barrel=0,'No','Yes')));
	END IF;

	IF new.mark_no_desc!=old.mark_no_desc THEN
		SET updated_values=(SELECT concat_string(updated_values,'Marks And Numbers',old.mark_no_desc,new.mark_no_desc));
	END IF;

	IF new.piece_desc!=old.piece_desc THEN
		SET updated_values=(SELECT concat_string(updated_values,'Commodity Desc',old.piece_desc,new.piece_desc));
	END IF;
	
        IF updated_values IS NOT NULL THEN
		IF  change_value=TRUE THEN
			SET @unit_ss_id=(SELECT lcl_unit_ss_id FROM lcl_booking_piece_unit WHERE booking_piece_id=old.id);
			CALL UpdateUnitWMValue(@unit_ss_id);
		END IF;
		INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
		VALUES(old.file_number_id,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
	END IF;
        END;
$$
DELIMITER ;

DELIMITER $$

DROP TRIGGER  after_LclBookingPieceDetails_update$$

CREATE TRIGGER after_LclBookingPieceDetails_update AFTER UPDATE ON lcl_booking_piece_detail
    FOR EACH ROW BEGIN
	DECLARE updated_values TEXT ;
	
	IF old.actual_uom!=new.actual_uom THEN 
		SET updated_values=(SELECT concat_string(updated_values,'UOM',old.actual_uom,new.actual_uom));
	END IF;

	IF old.actual_length!=new.actual_length THEN 
		SET updated_values=(SELECT concat_string(updated_values,'Length(IN)',old.actual_length,new.actual_length));
	END IF;

	IF old.actual_width!=new.actual_width THEN 
		SET updated_values=(SELECT concat_string(updated_values,'Width(IN)',old.actual_width,new.actual_width));
	END IF;

	IF old.actual_height!=new.actual_height THEN 
		SET updated_values=(SELECT concat_string(updated_values,'Height(IN)',old.actual_height,new.actual_height));
	END IF;

	IF old.piece_count!=new.piece_count THEN 
		SET updated_values=(SELECT concat_string(updated_values,'Pieces',old.piece_count,new.piece_count));
	END IF;

	IF old.stowed_location!=new.stowed_location THEN 
		SET updated_values=(SELECT concat_string(updated_values,'Warehouse Line',old.stowed_location,new.stowed_location));
	END IF;

	IF old.piece_count!=new.piece_count THEN 
		SET updated_values=(SELECT concat_string(updated_values,'Weight/PC(LBS)',old.piece_count,new.piece_count));
	END IF;
	
	IF updated_values IS NOT NULL THEN
	SET @fileId=(SELECT file_number_id FROM lcl_booking_piece WHERE id=new.booking_piece_id);
        SET updated_values=CONCAT('UPDATED ->',updated_values);
		INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
		VALUES(@fileId,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
	END IF;
        END;
$$
DELIMITER ;

DELIMITER $$

DROP TRIGGER  after_LclBookingPieceWhse_update$$

CREATE TRIGGER after_LclBookingPieceWhse_update AFTER UPDATE ON lcl_booking_piece_whse
    FOR EACH ROW BEGIN
	DECLARE updated_values TEXT ;
	
	IF old.warehouse_id!=new.warehouse_id THEN 
		SET updated_values=(SELECT concat_string(updated_values,'Warehouse Location',(SELECT warehsname FROM warehouse WHERE id=old.warehouse_id),(SELECT warehsname FROM warehouse WHERE id=new.warehouse_id)));
		SET updated_values=(SELECT concat_string(updated_values,'Warehouse Location Code',(SELECT warehsno FROM warehouse WHERE id=old.warehouse_id),(SELECT warehsno FROM warehouse WHERE id=new.warehouse_id)));
	END IF;

	IF old.location!=new.location THEN 
		SET updated_values=(SELECT concat_string(updated_values,'Line Location',old.location,new.location));
	END IF;
	
	IF updated_values IS NOT NULL THEN
		SET @fileId=(SELECT file_number_id FROM lcl_booking_piece WHERE id=new.booking_piece_id);
		SET updated_values=CONCAT('UPDATED ->',updated_values);
	     INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
	     VALUES(@fileId,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
	END IF;
        END;
$$
DELIMITER ;

DELIMITER $$

DROP TRIGGER  after_LclBookingHazmat_update$$

CREATE TRIGGER after_LclBookingHazmat_update AFTER UPDATE ON lcl_Booking_hazmat
    FOR EACH ROW BEGIN
	DECLARE updated_values TEXT ;
	
      IF old.un_hazmat_no!=new.un_hazmat_no THEN
		SET updated_values = (SELECT concat_string(updated_values,'UN#',old.un_hazmat_no,new.un_hazmat_no));
      END IF;

      IF old.proper_shipping_name!=new.proper_shipping_name THEN
		SET updated_values = (SELECT concat_string(updated_values,'Proper Shipping Name',old.proper_shipping_name,new.proper_shipping_name));
      END IF;

      IF old.technical_name!=new.technical_name THEN
		SET updated_values = (SELECT concat_string(updated_values,'Tech.Chemical Name',old.technical_name,new.technical_name));
      END IF;

      IF old.imo_pri_class_code!=new.imo_pri_class_code THEN
		SET updated_values = (SELECT concat_string(updated_values,'Class',old.imo_pri_class_code,new.imo_pri_class_code));
      END IF;

      IF old.imo_pri_sub_class_code!=new.imo_pri_sub_class_code THEN
		SET updated_values = (SELECT concat_string(updated_values,'IMO Subsidary Class',old.imo_pri_sub_class_code,new.imo_pri_sub_class_code));
      END IF;

      IF old.imo_sec_sub_class_code!=new.imo_sec_sub_class_code THEN
		SET updated_values = (SELECT concat_string(updated_values,'IMO Secondary Class',old.imo_sec_sub_class_code,new.imo_sec_sub_class_code));
      END IF;

      IF old.packing_group_code!=new.packing_group_code THEN
		SET updated_values = (SELECT concat_string(updated_values,'Packaging Group Code',old.packing_group_code,new.packing_group_code));
      END IF;

      IF old.flash_point!=new.flash_point THEN
		SET updated_values = (SELECT concat_string(updated_values,'Flash point',old.flash_point,new.flash_point));
      END IF;

      IF old.outer_pkg_no_pieces!=new.outer_pkg_no_pieces THEN
		SET updated_values = (SELECT concat_string(updated_values,'Outer Packaging Pieces',old.outer_pkg_no_pieces,new.outer_pkg_no_pieces));
      END IF;

      IF old.outer_pkg_type!=new.outer_pkg_type THEN
		SET updated_values = (SELECT concat_string(updated_values,'Outer Packaging Type',old.outer_pkg_type,new.outer_pkg_type));
      END IF;

      IF old.outer_pkg_composition!=new.outer_pkg_composition THEN
		SET updated_values = (SELECT concat_string(updated_values,'Outer Pkg Composition',old.outer_pkg_composition,new.outer_pkg_composition));
      END IF;

      IF old.inner_pkg_no_pieces!=new.inner_pkg_no_pieces THEN
		SET updated_values = (SELECT concat_string(updated_values,'Inner Packaging Pieces',old.inner_pkg_no_pieces,new.inner_pkg_no_pieces));
      END IF;

      IF old.inner_pkg_type!=new.inner_pkg_type THEN
		SET updated_values = (SELECT concat_string(updated_values,'Inner Packaging Type',old.inner_pkg_type,new.inner_pkg_type));
      END IF;

      IF old.inner_pkg_composition!=new.inner_pkg_composition THEN
		SET updated_values = (SELECT concat_string(updated_values,'Inner Pkg Composition',old.inner_pkg_composition,new.inner_pkg_composition));
      END IF;

      IF old.inner_pkg_uom!=new.inner_pkg_uom THEN
		SET updated_values = (SELECT concat_string(updated_values,'Inner Pkg UOM',old.inner_pkg_uom,new.inner_pkg_uom));
      END IF;

      IF old.inner_pkg_nwt_piece!=new.inner_pkg_nwt_piece THEN
		SET updated_values = (SELECT concat_string(updated_values,'Inner Pkg Wt/Vol Per Piece',old.inner_pkg_nwt_piece,new.inner_pkg_nwt_piece));
      END IF;

      IF old.total_net_weight!=new.total_net_weight THEN
		SET updated_values = (SELECT concat_string(updated_values,'Total Net Weight/Kgs',old.total_net_weight,new.total_net_weight));
      END IF;

      IF old.liquid_volume!=new.liquid_volume THEN
		SET updated_values = (SELECT concat_string(updated_values,'Total Volume',old.liquid_volume,new.liquid_volume));
      END IF;

      IF old.total_gross_weight!=new.total_gross_weight THEN
		SET updated_values = (SELECT concat_string(updated_values,'Total Gross Weight/Kgs',old.total_gross_weight,new.total_gross_weight));
      END IF;

      IF old.ems_code!=new.ems_code THEN
		SET updated_values = (SELECT concat_string(updated_values,'EMS code',old.ems_code,new.ems_code));
      END IF;

      IF old.emergency_contact_id!=new.emergency_contact_id THEN
		SET updated_values = (SELECT concat_string(updated_values,'Emergency Contact',(SELECT contact_name FROM lcl_contact WHERE id=old.emergency_contact_id),(SELECT contact_name FROM lcl_contact WHERE id=new.emergency_contact_id)));
		SET updated_values = (SELECT concat_string(updated_values,'Emergency Phone No',(SELECT phone1 FROM lcl_contact WHERE id=old.emergency_contact_id),(SELECT phone1 FROM lcl_contact WHERE id=new.emergency_contact_id)));
      END IF;

      IF old.reportable_quantity!=new.reportable_quantity THEN
		SET updated_values = (SELECT concat_string(updated_values,'Reportable Quantity',IF(old.reportable_quantity=0,'No','Yes'),IF(new.reportable_quantity=0,'No','Yes')));
      END IF;

      IF old.marine_pollutant!=new.marine_pollutant THEN
		SET updated_values = (SELECT concat_string(updated_values,'Marine pollutant',IF(old.marine_pollutant=0,'No','Yes'),IF(new.marine_pollutant=0,'No','Yes')));
      END IF;

      IF old.excepted_quantity!=new.excepted_quantity THEN
		SET updated_values = (SELECT concat_string(updated_values,'Excepted Quantity',IF(old.excepted_quantity=0,'No','Yes'),IF(new.excepted_quantity=0,'No','Yes')));
      END IF;

      IF old.limited_quantity!=new.limited_quantity THEN
		SET updated_values = (SELECT concat_string(updated_values,'Limited Quantity',IF(old.limited_quantity=0,'No','Yes'),IF(new.limited_quantity=0,'No','Yes')));
      END IF;

      IF old.residue!=new.residue THEN
		SET updated_values = (SELECT concat_string(updated_values,'Residue',IF(old.residue=0,'No','Yes'),IF(new.residue=0,'No','Yes')));
      END IF;

      IF old.inhalation_hazard!=new.inhalation_hazard THEN
		SET updated_values = (SELECT concat_string(updated_values,'Inhalation Hazard',IF(old.inhalation_hazard=0,'No','Yes'),IF(new.inhalation_hazard=0,'No','Yes')));
      END IF;
	
	IF updated_values IS NOT NULL THEN
        SET updated_values=CONCAT('UPDATED ->',updated_values);
	     INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
	     VALUES(old.file_number_id,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
	END IF;
        END;
$$
DELIMITER ;

DELIMITER $$

DROP TRIGGER after_LclBookingPad_update$$

CREATE TRIGGER after_LclBookingPad_update AFTER UPDATE ON lcl_Booking_pad
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT;
	
    IF old.issuing_terminal!=new.issuing_terminal THEN
       SET updated_values = (SELECT concat_string(updated_values,'Issued By',(SELECT CONCAT(UPPER(login_name),'/',terminal_id) FROM user_details WHERE user_id=old.entered_by_user_id),(SELECT CONCAT(UPPER(login_name),'/',terminal_id) FROM user_details WHERE user_id=new.entered_by_user_id)));
    END IF;
   
    IF old.pickup_to!=new.pickup_to THEN
       SET updated_values = (SELECT concat_string(updated_values,'To',old.pickup_to,new.pickup_to));
    END IF;
    
    IF old.pickup_contact_id!=new.pickup_contact_id THEN
    
       SELECT company_name,contact_name,city,address,zip,phone1,fax1,email1 INTO @oldCompName,@oldContName,@oldCity,@oldAddress,@oldZip,@oldPhone,@oldFax,@oldEmail FROM lcl_contact WHERE id=old.pickup_contact_id;
       SELECT company_name,contact_name,city,address,zip,phone1,fax1,email1 INTO @compName,@contName,@city,@address,@zip,@phone,@fax,@email FROM lcl_contact WHERE id=new.pickup_contact_id;
       
       IF @oldCity!=@city THEN
		SET updated_values = (SELECT concat_string(updated_values,'Door Origin/City/Zip',@oldCity,@city));
       END IF;
       IF @oldCompName!=@compName THEN
		SET updated_values = (SELECT concat_string(updated_values,'Ship/Supplier',@oldCompName,@compName));
       END IF;
       IF @oldContName!=@contName THEN
		SET updated_values = (SELECT concat_string(updated_values,'Contact Name',@oldContName,@contName));
       END IF;
       IF @oldAddress!=@address THEN
		SET updated_values = (SELECT concat_string(updated_values,'Address',@oldAddress,@address));
       END IF;
       IF @oldZip!=@zip THEN
		SET updated_values = (SELECT concat_string(updated_values,'Zip',@oldZip,@zip));
       END IF;
       IF @oldPhone!=@phone THEN
		SET updated_values = (SELECT concat_string(updated_values,'Phone',@oldPhone,@phone));
       END IF;
       IF @oldFax!=@fax THEN
		SET updated_values = (SELECT concat_string(updated_values,'Fax',@oldFax,@fax));
       END IF;
       IF @oldEmail!=@email THEN
		SET updated_values = (SELECT concat_string(updated_values,'Email',@oldEmail,@email));
       END IF;
       
    END IF;
    
    IF old.pickup_hours!=new.pickup_hours THEN
       SET updated_values = (SELECT concat_string(updated_values,'Shipper Hours',old.pickup_hours,new.pickup_hours));
    END IF;
    
    IF old.pickup_ready_note!=new.pickup_ready_note THEN
       SET updated_values = (SELECT concat_string(updated_values,'CutOff Note',old.pickup_ready_note,new.pickup_ready_note));
    END IF;
    
    IF old.pickup_ready_date!=new.pickup_ready_date THEN
       SET updated_values = (SELECT concat_string(updated_values,'Ready Date',old.pickup_ready_date,new.pickup_ready_date));
    END IF;
    
    IF old.pickup_cutoff_date!=new.pickup_cutoff_date THEN
       SET updated_values = (SELECT concat_string(updated_values,'CutOff Date',old.pickup_cutoff_date,new.pickup_cutoff_date));
    END IF;
    
    IF old.scac!=new.scac THEN
       SET updated_values = (SELECT concat_string(updated_values,'Scac Code',old.scac,new.scac));
    END IF;
    
    IF old.booking_ac_id!=new.booking_ac_id THEN
       SET updated_values = (SELECT concat_string(updated_values,'Pickup Charge/Sell',(SELECT ar_amount FROM lcl_quote_ac WHERE id=old.booking_ac_id),(SELECT ar_amount FROM lcl_booking_ac WHERE id=new.booking_ac_id)));
       SET updated_values = (SELECT concat_string(updated_values,'Pickup Cost',(SELECT ap_amount FROM lcl_quote_ac WHERE id=old.booking_ac_id),(SELECT ap_amount FROM lcl_booking_ac WHERE id=new.booking_ac_id)));
    END IF;
    
    IF old.pickup_reference_no!=new.pickup_reference_no THEN
       SET updated_values = (SELECT concat_string(updated_values,'Ref#/PRO#',old.pickup_reference_no,new.pickup_reference_no));
    END IF;
    
    IF old.delivery_contact_id!=new.delivery_contact_id THEN
       SET updated_values = (SELECT concat_string(updated_values,'DEL TO WHSE WhseName',(SELECT company_name FROM lcl_contact WHERE id=old.delivery_contact_id),(SELECT company_name FROM lcl_contact WHERE id=new.delivery_contact_id)));
       SET updated_values = (SELECT concat_string(updated_values,'Address',(SELECT address FROM lcl_contact WHERE id=old.delivery_contact_id),(SELECT address FROM lcl_contact WHERE id=new.delivery_contact_id)));
       SET updated_values = (SELECT concat_string(updated_values,'City/State/Zip',(SELECT CONCAT(city,'/',state,'/',zip) FROM lcl_contact WHERE id=old.delivery_contact_id),(SELECT CONCAT(city,'/',state,'/',zip) FROM lcl_contact WHERE id=new.delivery_contact_id)));
       SET updated_values = (SELECT concat_string(updated_values,'Phone',(SELECT phone1 FROM lcl_contact WHERE id=old.delivery_contact_id),(SELECT phone1 FROM lcl_contact WHERE id=new.delivery_contact_id)));
    END IF;
    
    IF old.pickup_instructions!=new.pickup_instructions THEN
       SET updated_values = (SELECT concat_string(updated_values,'Instructions',old.pickup_instructions,new.pickup_instructions));
    END IF;
    
    IF old.commodity_desc!=new.commodity_desc THEN
       SET updated_values = (SELECT concat_string(updated_values,'Commodity',old.commodity_desc,new.commodity_desc));
    END IF;
    
    IF old.terms_of_service!=new.terms_of_service THEN
       SET updated_values = (SELECT concat_string(updated_values,'TOS',old.terms_of_service,new.terms_of_service));
    END IF;
      	
    IF updated_values IS NOT NULL THEN
        SET updated_values=CONCAT('UPDATED ->',updated_values);
	     INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
	     VALUES(old.file_number_id,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;

--  BL Module Update Trigger

DELIMITER $$

DROP TRIGGER after_LclBl_update$$

CREATE TRIGGER after_LclBl_update AFTER UPDATE ON lcl_bl
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT;
    
     IF new.file_number_id!=old.file_number_id THEN
	SET updated_values = (SELECT concat_string(updated_values,'Document No',(SELECT file_number FROM lcl_file_number WHERE id=old.file_number_id),(SELECT file_number FROM lcl_file_number WHERE id=new.file_number_id)));
     END IF;
     
     IF new.ship_acct_no!=old.ship_acct_no THEN
	SELECT tp.acct_no,tp.acct_name,ca.address1,ca.city1,ca.state,ca.zip,ca.phone,ca.fax,ca.email1
	INTO @oldAcctNo,@oldAcctName,@oldAddress,@oldCity,@oldState,@oldZip,@oldPhone,@oldFax,@oldEmail
	FROM trading_partner tp LEFT JOIN cust_address ca ON tp.acct_no = ca.acct_no WHERE tp.acct_no=old.ship_acct_no;
	
	SELECT tp.acct_no,tp.acct_name,ca.address1,ca.city1,ca.state,ca.zip,ca.phone,ca.fax,ca.email1
	INTO @acctNo,@acctName,@address,@city,@state,@zip,@phone,@fax,@email
	FROM trading_partner tp LEFT JOIN cust_address ca ON tp.acct_no = ca.acct_no WHERE tp.acct_no=new.ship_acct_no;
	
	SET updated_values = (SELECT concat_string(updated_values,'SHIPPER/EXPORTER ->Number',@oldAcctNo,@acctNo));
	SET updated_values = (SELECT concat_string(updated_values,'Name',@oldAcctName,@acctName));
	IF @address!=@oldAddress THEN
		SET updated_values = (SELECT concat_string(updated_values,'Address',@oldAddress,@address));
	END IF;	
	IF @city!=@oldCity THEN
		SET updated_values = (SELECT concat_string(updated_values,'City',@oldCity,@city));
	END IF;
	IF @state!=@oldState THEN
		SET updated_values = (SELECT concat_string(updated_values,'State',@oldState,@state));
	END IF;
	IF @zip!=@oldZip THEN
		SET updated_values = (SELECT concat_string(updated_values,'Zip',@oldZip,@zip));
	END IF;
	IF @phone!=@oldPhone THEN
		SET updated_values = (SELECT concat_string(updated_values,'Phone',@oldPhone,@phone));
	END IF;
	IF @fax!=@oldFax THEN
		SET updated_values = (SELECT concat_string(updated_values,'Fax',@oldFax,@fax));
	END IF;
	IF @email!=@oldEmail THEN
		SET updated_values = (SELECT concat_string(updated_values,'Email',@oldEmail,@email));
	END IF;
     END IF;
     
     IF new.cons_acct_no!=old.cons_acct_no THEN
	SELECT tp.acct_no,tp.acct_name,ca.address1,ca.city1,ca.state,ca.zip,ca.phone,ca.fax,ca.email1
	INTO @oldAcctNo,@oldAcctName,@oldAddress,@oldCity,@oldState,@oldZip,@oldPhone,@oldFax,@oldEmail
	FROM trading_partner tp LEFT JOIN cust_address ca ON tp.acct_no = ca.acct_no WHERE tp.acct_no=old.cons_acct_no;
	
	SELECT tp.acct_no,tp.acct_name,ca.address1,ca.city1,ca.state,ca.zip,ca.phone,ca.fax,ca.email1
	INTO @acctNo,@acctName,@address,@city,@state,@zip,@phone,@fax,@email
	FROM trading_partner tp LEFT JOIN cust_address ca ON tp.acct_no = ca.acct_no WHERE tp.acct_no=new.cons_acct_no;
	
	SET updated_values = (SELECT concat_string(updated_values,' CONSIGNEE ->Number',@oldAcctNo,@acctNo));
	SET updated_values = (SELECT concat_string(updated_values,'Name',@oldAcctName,@acctName));
	IF @address!=@oldAddress THEN
		SET updated_values = (SELECT concat_string(updated_values,'Address',@oldAddress,@address));
	END IF;
	IF @city!=@oldCity THEN
		SET updated_values = (SELECT concat_string(updated_values,'City',@oldCity,@city));
	END IF;
	IF @state!=@oldState THEN
		SET updated_values = (SELECT concat_string(updated_values,'State',@oldState,@state));
	END IF;
	IF @zip!=@oldZip THEN
		SET updated_values = (SELECT concat_string(updated_values,'Zip',@oldZip,@zip));
	END IF;
	IF @phone!=@oldPhone THEN
		SET updated_values = (SELECT concat_string(updated_values,'Phone',@oldPhone,@phone));
	END IF;
	IF @fax!=@oldFax THEN
		SET updated_values = (SELECT concat_string(updated_values,'Fax',@oldFax,@fax));
	END IF;
	IF @email!=@oldEmail THEN
		SET updated_values = (SELECT concat_string(updated_values,'Email',@oldEmail,@email));
	END IF;
     END IF;
     
     IF new.noty_acct_no!=old.noty_acct_no THEN
	SELECT tp.acct_no,tp.acct_name,ca.address1,ca.city1,ca.state,ca.zip,ca.phone,ca.fax,ca.email1
	INTO @oldAcctNo,@oldAcctName,@oldAddress,@oldCity,@oldState,@oldZip,@oldPhone,@oldFax,@oldEmail
	FROM trading_partner tp LEFT JOIN cust_address ca ON tp.acct_no = ca.acct_no WHERE tp.acct_no=old.noty_acct_no;
	
	SELECT tp.acct_no,tp.acct_name,ca.address1,ca.city1,ca.state,ca.zip,ca.phone,ca.fax,ca.email1
	INTO @acctNo,@acctName,@address,@city,@state,@zip,@phone,@fax,@email
	FROM trading_partner tp LEFT JOIN cust_address ca ON tp.acct_no = ca.acct_no WHERE tp.acct_no=new.noty_acct_no;
	
	SET updated_values = (SELECT concat_string(updated_values,' NOTIFY PARTY ->Number',@oldAcctNo,@acctNo));
	SET updated_values = (SELECT concat_string(updated_values,'Name',@oldAcctName,@acctName));
	IF @address!=@oldAddress THEN
		SET updated_values = (SELECT concat_string(updated_values,'Address',@oldAddress,@address));
	END IF;
	IF @city!=@oldCity THEN
		SET updated_values = (SELECT concat_string(updated_values,'City',@oldCity,@city));
	END IF;
	IF @state!=@oldState THEN
		SET updated_values = (SELECT concat_string(updated_values,'State',@oldState,@state));
	END IF;
	IF @zip!=@oldZip THEN
		SET updated_values = (SELECT concat_string(updated_values,'Zip',@oldZip,@zip));
	END IF;
	IF @phone!=@oldPhone THEN
		SET updated_values = (SELECT concat_string(updated_values,'Phone',@oldPhone,@phone));
	END IF;
	IF @fax!=@oldFax THEN
		SET updated_values = (SELECT concat_string(updated_values,'Fax',@oldFax,@fax));
	END IF;
	IF @email!=@oldEmail THEN
		SET updated_values = (SELECT concat_string(updated_values,'Email',@oldEmail,@email));
	END IF;
     END IF;
     
     IF new.fwd_acct_no!=old.fwd_acct_no THEN
	SELECT tp.acct_no,tp.acct_name,ca.address1,ca.city1,ca.state,ca.zip,ca.phone,ca.fax,ca.email1,cg.fw_fmc_no
	INTO @oldAcctNo,@oldAcctName,@oldAddress,@oldCity,@oldState,@oldZip,@oldPhone,@oldFax,@oldEmail,@oldFmc
	FROM trading_partner tp LEFT JOIN cust_address ca ON tp.acct_no = ca.acct_no WHERE tp.acct_no=old.fwd_acct_no;
	
	SELECT tp.acct_no,tp.acct_name,ca.address1,ca.city1,ca.state,ca.zip,ca.phone,ca.fax,ca.email1,cg.fw_fmc_no
	INTO @acctNo,@acctName,@address,@city,@state,@zip,@phone,@fax,@email,@fmc
	FROM trading_partner tp LEFT JOIN cust_address ca ON tp.acct_no = ca.acct_no
	LEFT JOIN cust_general_info cg ON ca.acct_no = cg.acct_no WHERE tp.acct_no=new.fwd_acct_no;
	
	SET updated_values = (SELECT concat_string(updated_values,' FORWARDER ->Number',@oldAcctNo,@acctNo));
	SET updated_values = (SELECT concat_string(updated_values,'Name',@oldAcctName,@acctName));
	IF @fmc!=@oldFmc THEN
		SET updated_values = (SELECT concat_string(updated_values,'FMC#',@oldFmc,@fmc));
	END IF;
	IF @address!=@oldAddress THEN
		SET updated_values = (SELECT concat_string(updated_values,'Address',@oldAddress,@address));
	END IF;
	IF @city!=@oldCity THEN
		SET updated_values = (SELECT concat_string(updated_values,'City',@oldCity,@city));
	END IF;
	IF @state!=@oldState THEN
		SET updated_values = (SELECT concat_string(updated_values,'State',@oldState,@state));
	END IF;
	IF @zip!=@oldZip THEN
		SET updated_values = (SELECT concat_string(updated_values,'Zip',@oldZip,@zip));
	END IF;
	IF @phone!=@oldPhone THEN
		SET updated_values = (SELECT concat_string(updated_values,'Phone',@oldPhone,@phone));
	END IF;
	IF @fax!=@oldFax THEN
		SET updated_values = (SELECT concat_string(updated_values,'Fax',@oldFax,@fax));
	END IF;
	IF @email!=@oldEmail THEN
		SET updated_values = (SELECT concat_string(updated_values,'Email',@oldEmail,@email));
	END IF;
     END IF;
     
     IF new.sup_acct_no!=old.sup_acct_no THEN
	SELECT tp.acct_no,tp.acct_name,ca.address1,ca.city1,ca.state,ca.zip,ca.phone,ca.fax,ca.email1
	INTO @oldAcctNo,@oldAcctName,@oldAddress,@oldCity,@oldState,@oldZip,@oldPhone,@oldFax,@oldEmail
	FROM trading_partner tp LEFT JOIN cust_address ca ON tp.acct_no = ca.acct_no WHERE tp.acct_no=old.sup_acct_no;
	
	SELECT tp.acct_no,tp.acct_name,ca.address1,ca.city1,ca.state,ca.zip,ca.phone,ca.fax,ca.email1
	INTO @acctNo,@acctName,@address,@city,@state,@zip,@phone,@fax,@email
	FROM trading_partner tp LEFT JOIN cust_address ca ON tp.acct_no = ca.acct_no WHERE tp.acct_no=new.sup_acct_no;
	
	SET updated_values = (SELECT concat_string(updated_values,' SUPPLIER ->Number',@oldAcctNo,@acctNo));
	SET updated_values = (SELECT concat_string(updated_values,'Name',@oldAcctName,@acctName));
	IF @address!=@oldAddress THEN
		SET updated_values = (SELECT concat_string(updated_values,'Address',@oldAddress,@address));
	END IF;
	IF @city!=@oldCity THEN
		SET updated_values = (SELECT concat_string(updated_values,'City',@oldCity,@city));
	END IF;
	IF @state!=@oldState THEN
		SET updated_values = (SELECT concat_string(updated_values,'State',@oldState,@state));
	END IF;
	IF @zip!=@oldZip THEN
		SET updated_values = (SELECT concat_string(updated_values,'Zip',@oldZip,@zip));
	END IF;
	IF @phone!=@oldPhone THEN
		SET updated_values = (SELECT concat_string(updated_values,'Phone',@oldPhone,@phone));
	END IF;
	IF @fax!=@oldFax THEN
		SET updated_values = (SELECT concat_string(updated_values,'Fax',@oldFax,@fax));
	END IF;
	IF @email!=@oldEmail THEN
		SET updated_values = (SELECT concat_string(updated_values,'Email',@oldEmail,@email));
	END IF;
     END IF;
     
     IF new.agent_acct_no!=old.agent_acct_no THEN
	SELECT tp.acct_no,tp.acct_name,ca.address1,ca.city1,ca.state,ca.zip,ca.phone,ca.fax,ca.email1
	INTO @oldAcctNo,@oldAcctName,@oldAddress,@oldCity,@oldState,@oldZip,@oldPhone,@oldFax,@oldEmail
	FROM trading_partner tp LEFT JOIN cust_address ca ON tp.acct_no = ca.acct_no WHERE tp.acct_no=old.agent_acct_no;
	
	SELECT tp.acct_no,tp.acct_name,ca.address1,ca.city1,ca.state,ca.zip,ca.phone,ca.fax,ca.email1
	INTO @acctNo,@acctName,@address,@city,@state,@zip,@phone,@fax,@email
	FROM trading_partner tp LEFT JOIN cust_address ca ON tp.acct_no = ca.acct_no WHERE tp.acct_no=new.agent_acct_no;

	SET updated_values = (SELECT concat_string(updated_values,'TO PICK UP FREIGHT PLEASE CONTACT ->Number',@oldAcctNo,@acctNo));
	SET updated_values = (SELECT concat_string(updated_values,'Name',@oldAcctName,@acctName));
	IF @address!=@oldAddress THEN
		SET updated_values = (SELECT concat_string(updated_values,'Address',@oldAddress,@address));
	END IF;
	IF @city!=@oldCity THEN
		SET updated_values = (SELECT concat_string(updated_values,'City',@oldCity,@city));
	END IF;
	IF @state!=@oldState THEN
		SET updated_values = (SELECT concat_string(updated_values,'State',@oldState,@state));
	END IF;
	IF @zip!=@oldZip THEN
		SET updated_values = (SELECT concat_string(updated_values,'Zip',@oldZip,@zip));
	END IF;
	IF @phone!=@oldPhone THEN
		SET updated_values = (SELECT concat_string(updated_values,'Phone',@oldPhone,@phone));
	END IF;
	IF @fax!=@oldFax THEN
		SET updated_values = (SELECT concat_string(updated_values,'Fax',@oldFax,@fax));
	END IF;
	IF @email!=@oldEmail THEN
		SET updated_values = (SELECT concat_string(updated_values,'Email',@oldEmail,@email));
	END IF;
     END IF;
     
     IF new.master_schedule_id!=old.master_schedule_id THEN
	SELECT ls.schedule_no,tp.acct_name,ls.sp_reference_no,ls.sp_reference_name,ls.pol_pg,ls.pol_etd
	INTO @oldSchdNo,@oldSpAcctNo,@oldSpRefNo,@oldSpRefName,@oldPolPg,@oldPolEtd
	FROM lcl_schedule ls LEFT JOIN trading_partner tp ON ls.sp_acct_no=tp.acct_no WHERE ls.id=old.master_schedule_id;
	
	SELECT ls.schedule_no,tp.acct_name,ls.sp_reference_no,ls.sp_reference_name,ls.pol_pg,ls.pol_etd
	INTO @schdNo,@spAcctNo,@spRefNo,@spRefName,@polPg,@polEtd
	FROM lcl_schedule ls LEFT JOIN trading_partner tp ON ls.sp_acct_no=tp.acct_no WHERE ls.id=new.master_schedule_id;
	
	IF @schdNo!=@oldSchdNo THEN
		SET updated_values = (SELECT concat_string(updated_values,'TRADE ROUTE ->ECI Voy#',@oldSchdNo,@schdNo));
       END IF;
       IF @spAcctNo!=@oldSpAcctNo THEN
		SET updated_values = (SELECT concat_string(updated_values,'SS Line',@oldSpAcctNo,@spAcctNo));
       END IF;
       IF @spRefNo!=@oldSpRefNo THEN
		SET updated_values = (SELECT concat_string(updated_values,'SS Voy',@oldSpRefNo,@spRefNo));
       END IF;
       IF @spRefName!=@oldSpRefName THEN
		SET updated_values = (SELECT concat_string(updated_values,'Exp Carrier(VSL)(FLG)',@oldSpRefName,@spRefName));
       END IF;
       IF @polPg!=@oldPolPg THEN
		SET updated_values = (SELECT concat_string(updated_values,'Pier',@oldPolPg,@polPg));
       END IF;
       IF @polEtd!=@oldPolEtd THEN
		SET updated_values = (SELECT concat_string(updated_values,'Sail Date',@oldPolEtd,@polEtd));
       END IF;
     
     END IF ;
     
     IF new.pol_id!=old.pol_id THEN
	SET updated_values = (SELECT concat_string(updated_values,'POL',(SELECT un_loc_name FROM un_location WHERE id=old.pol_id),(SELECT un_loc_name FROM un_location WHERE id=new.pol_id)));
     END IF;
     
     IF new.pod_id!=old.pod_id THEN
	SET updated_values = (SELECT concat_string(updated_values,'Sea POD',(SELECT un_loc_name FROM un_location WHERE id=old.pod_id),(SELECT un_loc_name FROM un_location WHERE id=new.pod_id)));
     END IF;
     
     IF new.point_of_origin!=old.point_of_origin THEN
	SET updated_values = (SELECT concat_string(updated_values,'Point Of Origin',old.point_of_origin,new.point_of_origin));
     END IF;
     
     IF new.docs_bl!=old.docs_bl THEN
	SET updated_values = (SELECT concat_string(updated_values,'Docs BL',IF(old.docs_bl=0,'No','Yes'),IF(new.docs_bl=0,'No','Yes')));
     END IF;
     
     IF new.docs_aes!=old.docs_aes THEN
	SET updated_values = (SELECT concat_string(updated_values,'Docs AES',IF(old.docs_aes=0,'No','Yes'),IF(new.docs_aes=0,'No','Yes')));
     END IF;
     
     IF new.docs_caricom!=old.docs_caricom THEN
	SET updated_values = (SELECT concat_string(updated_values,'Docs Caricom',IF(old.docs_caricom=0,'No','Yes'),IF(new.docs_caricom=0,'No','Yes')));
     END IF;
     
     IF old.billing_type!=new.billing_type THEN
     SET updated_values = (SELECT concat_string(updated_values,'Billing Type',old.billing_type,new.billing_type));
     END IF;

     IF old.spot_rate!=new.spot_rate THEN
     SET updated_values = (SELECT concat_string(updated_values,'Spot Rate',IF(old.spot_rate=0,'No','Yes'),IF(new.spot_rate=0,'No','Yes')));
     END IF;

     IF old.free_bl!=new.free_bl THEN
     SET updated_values = (SELECT concat_string(updated_values,'Free B/L',IF(old.free_bl=0,'No','Yes'),IF(new.free_bl=0,'No','Yes')));
     END IF;
     
     IF new.rate_type!=old.rate_type THEN
	SET updated_values = (SELECT concat_string(updated_values,'Rate Type',old.rate_type,new.rate_type));
     END IF;
     
     IF new.bill_to_party!=old.bill_to_party THEN
	SET updated_values = (SELECT concat_string(updated_values,'Bill To Party',old.bill_to_party,new.bill_to_party));
     END IF; 
     
     IF new.billing_terminal!=old.billing_terminal THEN
	SET updated_values = (SELECT concat_string(updated_values,'Rates From Term',(SELECT terminal_location FROM terminal WHERE trmnum=old.billing_terminal),(SELECT terminal_location FROM terminal WHERE trmnum=new.billing_terminal)));
     END IF; 
     
     IF new.third_party_acct_no!=old.third_party_acct_no THEN
	SET updated_values = (SELECT concat_string(updated_values,'Third Party Acct No#',old.third_party_acct_no,new.third_party_acct_no));
	SET updated_values = (SELECT concat_string(updated_values,'Third Party Acct',(SELECT acct_name FROM trading_partner WHERE acct_no=old.third_party_acct_no),(SELECT acct_name FROM trading_partner WHERE acct_no=new.third_party_acct_no)));
     END IF; 
     
     IF new.terms_type1!=old.terms_type1 AND new.terms_type1!='S1' THEN
	SET updated_values = (SELECT concat_string(updated_values,'Terms Type 1',
	IF(old.terms_type1='ER','Express Release',IF(old.terms_type1='OR','Originals Required',IF(old.terms_type1='ORD','Originals Released at Destination',''))),
	IF(new.terms_type1='ER','Express Release',IF(new.terms_type1='OR','Originals Required',IF(new.terms_type1='ORD','Originals Released at Destination','')))));
     END IF;
     
     IF new.terms_type2!=old.terms_type2 AND new.terms_type1!='S2' THEN
	SET updated_values = (SELECT concat_string(updated_values,'Terms Type 2',
	IF(old.terms_type2='RS','Receipt for Shipment',IF(old.terms_type2='OB','On Board Bill',IF(old.terms_type2='CB','Clean on Board',''))),
	IF(new.terms_type2='RS','Receipt for Shipment',IF(new.terms_type2='OB','On Board Bill',IF(new.terms_type2='CB','Clean on Board','')))));
     END IF;
     
     IF new.type2_date!=old.type2_date THEN
	SET updated_values = (SELECT concat_string(updated_values,'Type 2 Date',old.type2_date,new.type2_date));
     END IF;
     
    IF updated_values IS NOT NULL THEN
	SET updated_values=CONCAT('UPDATED ->',updated_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(old.file_number_id,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    
    END;
$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER after_LclBlPiece_update$$

CREATE TRIGGER after_LclBlPiece_update AFTER UPDATE ON lcl_bl_piece
    FOR EACH ROW BEGIN
	DECLARE updated_values TEXT ;

	IF new.commodity_type_id!=old.commodity_type_id THEN
		SET updated_values=(SELECT concat_string(updated_values,'Tariff',(SELECT desc_en FROM commodity_type WHERE id=old.commodity_type_id),(SELECT desc_en FROM commodity_type WHERE id=new.commodity_type_id)));
		SET updated_values=(SELECT concat_string(updated_values,'Tariff#',(SELECT CODE FROM commodity_type WHERE id=old.commodity_type_id),(SELECT CODE FROM commodity_type WHERE id=new.commodity_type_id)));
	END IF;

	IF new.harmonized_code!=old.harmonized_code THEN
		SET updated_values=(SELECT concat_string(updated_values,'Harmonized Code',old.harmonized_code,new.harmonized_code));
	END IF;

	IF new.packaging_type_id!=old.packaging_type_id THEN
		SET updated_values=(SELECT concat_string(updated_values,'Package',(SELECT description FROM package_type WHERE id=old.packaging_type_id),(SELECT description FROM package_type WHERE id=new.packaging_type_id)));
	END IF;

	IF new.actual_piece_count!=old.actual_piece_count THEN
		SET updated_values=(SELECT concat_string(updated_values,' Actual Piece Count',old.actual_piece_count,new.actual_piece_count));
	END IF;
	
	IF new.actual_weight_imperial!=old.actual_weight_imperial THEN
		SET updated_values=(SELECT concat_string(updated_values,' Actual Weight Imperial',old.actual_weight_imperial,new.actual_weight_imperial));
	END IF;
	
	IF new.actual_weight_metric!=old.actual_weight_metric THEN
		SET updated_values=(SELECT concat_string(updated_values,' Actual Weight Metric',old.actual_weight_metric,new.actual_weight_metric));
	END IF;
	
	IF new.actual_volume_imperial!=old.actual_volume_imperial THEN
		SET updated_values=(SELECT concat_string(updated_values,' Actual Volume Imperial',old.actual_volume_imperial,new.actual_volume_imperial));
	END IF;
	
	IF new.actual_volume_metric!=old.actual_volume_metric THEN
		SET updated_values=(SELECT concat_string(updated_values,' Actual Volume Metric',old.actual_volume_metric,new.actual_volume_metric));
	END IF;

	IF new.hazmat!=old.hazmat THEN
		SET updated_values=(SELECT concat_string(updated_values,'Hazmat',IF(old.hazmat=0,'No','Yes'),IF(new.hazmat=0,'No','Yes')));
	END IF;

	IF new.personal_effects!=old.personal_effects THEN
		SET updated_values=(SELECT concat_string(updated_values,'Personal Effects',old.personal_effects,new.personal_effects));
	END IF;

	IF new.refrigeration_required!=old.refrigeration_required THEN
		SET updated_values=(SELECT concat_string(updated_values,'Refrigeration',IF(old.refrigeration_required=0,'No','Yes'),IF(new.refrigeration_required=0,'No','Yes')));
	END IF;
	
	IF new.is_barrel!=old.is_barrel THEN
		SET updated_values=(SELECT concat_string(updated_values,'Barrel Business',IF(old.is_barrel=0,'No','Yes'),IF(new.is_barrel=0,'No','Yes')));
	END IF;

	IF new.mark_no_desc!=old.mark_no_desc THEN
		SET updated_values=(SELECT concat_string(updated_values,'Marks And Numbers',old.mark_no_desc,new.mark_no_desc));
	END IF;

	IF new.piece_desc!=old.piece_desc THEN
		SET updated_values=(SELECT concat_string(updated_values,'Commodity Desc',old.piece_desc,new.piece_desc));
	END IF;
	
        IF updated_values IS NOT NULL THEN
        SET updated_values=CONCAT('UPDATED ->',updated_values);
		INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
		VALUES(old.file_number_id,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
	END IF;
        END;
$$
DELIMITER ;

DELIMITER $$

DROP TRIGGER  after_LclBlHazmat_update$$

CREATE TRIGGER after_LclBlHazmat_update AFTER UPDATE ON lcl_bl_hazmat
    FOR EACH ROW BEGIN
	DECLARE updated_values TEXT ;
	
      IF old.un_hazmat_no!=new.un_hazmat_no THEN
		SET updated_values = (SELECT concat_string(updated_values,'UN#',old.un_hazmat_no,new.un_hazmat_no));
      END IF;

      IF old.proper_shipping_name!=new.proper_shipping_name THEN
		SET updated_values = (SELECT concat_string(updated_values,'Proper Shipping Name',old.proper_shipping_name,new.proper_shipping_name));
      END IF;

      IF old.technical_name!=new.technical_name THEN
		SET updated_values = (SELECT concat_string(updated_values,'Tech.Chemical Name',old.technical_name,new.technical_name));
      END IF;

      IF old.imo_pri_class_code!=new.imo_pri_class_code THEN
		SET updated_values = (SELECT concat_string(updated_values,'Class',old.imo_pri_class_code,new.imo_pri_class_code));
      END IF;

      IF old.imo_pri_sub_class_code!=new.imo_pri_sub_class_code THEN
		SET updated_values = (SELECT concat_string(updated_values,'IMO Subsidary Class',old.imo_pri_sub_class_code,new.imo_pri_sub_class_code));
      END IF;

      IF old.imo_sec_sub_class_code!=new.imo_sec_sub_class_code THEN
		SET updated_values = (SELECT concat_string(updated_values,'IMO Secondary Class',old.imo_sec_sub_class_code,new.imo_sec_sub_class_code));
      END IF;

      IF old.packing_group_code!=new.packing_group_code THEN
		SET updated_values = (SELECT concat_string(updated_values,'Packaging Group Code',old.packing_group_code,new.packing_group_code));
      END IF;

      IF old.flash_point!=new.flash_point THEN
		SET updated_values = (SELECT concat_string(updated_values,'Flash point',old.flash_point,new.flash_point));
      END IF;

      IF old.outer_pkg_no_pieces!=new.outer_pkg_no_pieces THEN
		SET updated_values = (SELECT concat_string(updated_values,'Outer Packaging Pieces',old.outer_pkg_no_pieces,new.outer_pkg_no_pieces));
      END IF;

      IF old.outer_pkg_type!=new.outer_pkg_type THEN
		SET updated_values = (SELECT concat_string(updated_values,'Outer Packaging Type',old.outer_pkg_type,new.outer_pkg_type));
      END IF;

      IF old.outer_pkg_composition!=new.outer_pkg_composition THEN
		SET updated_values = (SELECT concat_string(updated_values,'Outer Pkg Composition',old.outer_pkg_composition,new.outer_pkg_composition));
      END IF;

      IF old.inner_pkg_no_pieces!=new.inner_pkg_no_pieces THEN
		SET updated_values = (SELECT concat_string(updated_values,'Inner Packaging Pieces',old.inner_pkg_no_pieces,new.inner_pkg_no_pieces));
      END IF;

      IF old.inner_pkg_type!=new.inner_pkg_type THEN
		SET updated_values = (SELECT concat_string(updated_values,'Inner Packaging Type',old.inner_pkg_type,new.inner_pkg_type));
      END IF;

      IF old.inner_pkg_composition!=new.inner_pkg_composition THEN
		SET updated_values = (SELECT concat_string(updated_values,'Inner Pkg Composition',old.inner_pkg_composition,new.inner_pkg_composition));
      END IF;

      IF old.inner_pkg_uom!=new.inner_pkg_uom THEN
		SET updated_values = (SELECT concat_string(updated_values,'Inner Pkg UOM',old.inner_pkg_uom,new.inner_pkg_uom));
      END IF;

      IF old.inner_pkg_nwt_piece!=new.inner_pkg_nwt_piece THEN
		SET updated_values = (SELECT concat_string(updated_values,'Inner Pkg Wt/Vol Per Piece',old.inner_pkg_nwt_piece,new.inner_pkg_nwt_piece));
      END IF;

      IF old.total_net_weight!=new.total_net_weight THEN
		SET updated_values = (SELECT concat_string(updated_values,'Total Net Weight/Kgs',old.total_net_weight,new.total_net_weight));
      END IF;

      IF old.liquid_volume!=new.liquid_volume THEN
		SET updated_values = (SELECT concat_string(updated_values,'Total Volume',old.liquid_volume,new.liquid_volume));
      END IF;

      IF old.total_gross_weight!=new.total_gross_weight THEN
		SET updated_values = (SELECT concat_string(updated_values,'Total Gross Weight/Kgs',old.total_gross_weight,new.total_gross_weight));
      END IF;

      IF old.ems_code!=new.ems_code THEN
		SET updated_values = (SELECT concat_string(updated_values,'EMS code',old.ems_code,new.ems_code));
      END IF;

      IF old.emergency_contact_id!=new.emergency_contact_id THEN
		SET updated_values = (SELECT concat_string(updated_values,'Emergency Contact',(SELECT contact_name FROM lcl_contact WHERE id=old.emergency_contact_id),(SELECT contact_name FROM lcl_contact WHERE id=new.emergency_contact_id)));
		SET updated_values = (SELECT concat_string(updated_values,'Emergency Phone No',(SELECT phone1 FROM lcl_contact WHERE id=old.emergency_contact_id),(SELECT phone1 FROM lcl_contact WHERE id=new.emergency_contact_id)));
      END IF;

      IF old.reportable_quantity!=new.reportable_quantity THEN
		SET updated_values = (SELECT concat_string(updated_values,'Reportable Quantity',IF(old.reportable_quantity=0,'No','Yes'),IF(new.reportable_quantity=0,'No','Yes')));
      END IF;

      IF old.marine_pollutant!=new.marine_pollutant THEN
		SET updated_values = (SELECT concat_string(updated_values,'Marine pollutant',IF(old.marine_pollutant=0,'No','Yes'),IF(new.marine_pollutant=0,'No','Yes')));
      END IF;

      IF old.excepted_quantity!=new.excepted_quantity THEN
		SET updated_values = (SELECT concat_string(updated_values,'Excepted Quantity',IF(old.excepted_quantity=0,'No','Yes'),IF(new.excepted_quantity=0,'No','Yes')));
      END IF;

      IF old.limited_quantity!=new.limited_quantity THEN
		SET updated_values = (SELECT concat_string(updated_values,'Limited Quantity',IF(old.limited_quantity=0,'No','Yes'),IF(new.limited_quantity=0,'No','Yes')));
      END IF;

      IF old.residue!=new.residue THEN
		SET updated_values = (SELECT concat_string(updated_values,'Residue',IF(old.residue=0,'No','Yes'),IF(new.residue=0,'No','Yes')));
      END IF;

      IF old.inhalation_hazard!=new.inhalation_hazard THEN
		SET updated_values = (SELECT concat_string(updated_values,'Inhalation Hazard',IF(old.inhalation_hazard=0,'No','Yes'),IF(new.inhalation_hazard=0,'No','Yes')));
      END IF;
	
	IF updated_values IS NOT NULL THEN
        SET updated_values=CONCAT('UPDATED ->',updated_values);
	     INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
	     VALUES(old.file_number_id,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
	END IF;
        END;
$$
DELIMITER ;

DELIMITER $$

DROP TRIGGER  after_LclOptions_update$$

CREATE TRIGGER after_LclOptions_update AFTER UPDATE ON lcl_options
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT ;
    
    IF new.value='AES' THEN
	IF old.key!=new.key THEN
		SET updated_values = (SELECT concat_string(updated_values,'Print AES on body',IF(old.key='N','No','Yes'),IF(new.key='N','No','Yes')));
	END IF;
    END IF;
    IF new.value='HS' THEN
	IF old.key!=new.key THEN
		SET updated_values = (SELECT concat_string(updated_values,'Print HS codes on body',IF(old.key='N','No','Yes'),IF(new.key='N','No','Yes')));
	END IF;
    END IF;
    
    IF new.value='NCM' THEN
	IF old.key!=new.key THEN
		SET updated_values = (SELECT concat_string(updated_values,'Print NCM codes on body',IF(old.key='N','No','Yes'),IF(new.key='N','No','Yes')));
	END IF;
    END IF;
    
    IF new.value='FP' THEN
	IF old.key!=new.key THEN
		SET updated_values = (SELECT concat_string(updated_values,'Use Alt Freight Pickup account',IF(old.key='N','No','Yes'),IF(new.key='N','No','Yes')));
	END IF;
    END IF;
    
    IF new.value='IMP' THEN
	IF old.key!=new.key THEN
		SET updated_values = (SELECT concat_string(updated_values,'Print decimal on Imperial values',IF(old.key='N','No','Yes'),IF(new.key='N','No','Yes')));
	END IF;
    END IF;
    
    IF new.value='ALERT' THEN
	IF old.key!=new.key THEN
		SET updated_values = (SELECT concat_string(updated_values,'Pre Alert',IF(old.key='N','No','Yes'),IF(new.key='N','No','Yes')));
	END IF;
    END IF;
    
    IF new.value='PROOF' THEN
	IF old.key!=new.key THEN
		SET updated_values = (SELECT concat_string(updated_values,'Proof Copy',IF(old.key='N','No','Yes'),IF(new.key='N','No','Yes')));
	END IF;
    END IF;
    
    IF new.value='MET' THEN
	IF old.key!=new.key THEN
		SET updated_values = (SELECT concat_string(updated_values,'Print Imperial values on Metric ports',IF(old.key='N','No','Yes'),IF(new.key='N','No','Yes')));
	END IF;
    END IF;
    
    IF new.value='PORT' THEN
	IF old.key!=new.key THEN
		SET updated_values = (SELECT concat_string(updated_values,'Print Alt Port lower right',IF(old.key='N','No','Yes'),IF(new.key='N','No','Yes')));
	END IF;
    END IF;
    
    IF updated_values IS NOT NULL THEN
     SET updated_values=CONCAT('UPDATED ->',updated_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(old.file_number_id,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
      END;
$$

DELIMITER ;

-- Common Update Triggers For Quote,Booking and BL

DELIMITER $$

DROP TRIGGER after_LclInbond_update$$

CREATE TRIGGER after_LclInbond_update AFTER UPDATE ON lcl_inbond
    FOR EACH ROW BEGIN
	DECLARE updated_values TEXT ;
	
	IF old.inbond_no!=new.inbond_no THEN 
		SET updated_values=(SELECT concat_string(updated_values,'Inbond#',old.inbond_no,new.inbond_no));
	END IF;

	IF old.inbond_type!=new.inbond_type THEN 
		SET updated_values=(SELECT concat_string(updated_values,'Type',old.inbond_type,new.inbond_type));
	END IF;

	IF old.inbond_port!=new.inbond_port THEN 
		SET updated_values=(SELECT concat_string(updated_values,'Port',(SELECT un_loc_name FROM un_location WHERE id=old.inbond_port),(SELECT un_loc_name FROM un_location WHERE id=new.inbond_port)));
	END IF;

	IF old.inbond_datetime!=new.inbond_datetime THEN 
		SET updated_values=(SELECT concat_string(updated_values,'Date',old.inbond_datetime,new.inbond_datetime));
	END IF;
	
	IF updated_values IS NOT NULL THEN
	  SET updated_values=CONCAT('UPDATED ->',updated_values);
	     INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
	     VALUES(old.file_number_id,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
	END IF;
        END;
$$

DELIMITER ;

DELIMITER $$

DROP TRIGGER after_LclExport_update$$

CREATE TRIGGER after_LclExport_update AFTER UPDATE ON lcl_export 
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT ;
    
    IF old.cfcl!=new.cfcl THEN
	SET updated_values=(SELECT concat_string(updated_values,'CFCL',IF(old.cfcl=0,'No','Yes'),IF(new.cfcl=0,'No','Yes')));
    END IF;

    IF old.cfcl_acct_no!=new.cfcl_acct_no THEN
	SET updated_values=(SELECT concat_string(updated_values,'CFCL Acct No#',old.cfcl_acct_no,new.cfcl_acct_no));
	SET updated_values=(SELECT concat_string(updated_values,'CFCL Acct Name',(SELECT acct_name FROM trading_partner WHERE acct_no=old.cfcl_acct_no),(SELECT acct_name FROM trading_partner WHERE acct_no=new.cfcl_acct_no)));
    END IF;

    IF old.ups!=new.ups THEN
      SET updated_values=(SELECT concat_string(updated_values,'Small Parcel',IF(old.ups=0,'No','Yes'),IF(new.ups=0,'No','Yes')));
    END IF;

    IF old.calc_heavy!=new.calc_heavy THEN
      SET updated_values=(SELECT concat_string(updated_values,'Calc/Heavy',IF(old.calc_heavy=0,'No','Yes'),IF(new.calc_heavy=0,'No','Yes')));
    END IF;

    IF old.aes!=new.aes THEN
      SET updated_values=(SELECT concat_string(updated_values,'AES By ECI',IF(old.aes=0,'No','Yes'),IF(new.aes=0,'No','Yes')));
    END IF;
    
    IF updated_values IS NOT NULL THEN
     SET updated_values=CONCAT('UPDATED ->',updated_values);
        INSERT INTO lcl_remarks(file_number_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(old.file_number_id,'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
      END;
$$

DELIMITER ;

--4Dec2013@Mei
DELIMITER $$


DROP TRIGGER /*!50032 IF EXISTS */ `after_lclUnitssDispo_insert`$$

CREATE
    /*!50017 DEFINER = 'root'@'%' */
    TRIGGER `after_lclUnitssDispo_insert` AFTER INSERT ON `lcl_unit_ss_dispo` 
    FOR EACH ROW BEGIN
    DECLARE insert_values TEXT ;
	SELECT disposition_id,unit_id INTO @oldDispoId,@oldUnitId  FROM lcl_unit_ss_dispo WHERE id=(SELECT id FROM lcl_unit_ss_dispo WHERE unit_id=new.unit_id ORDER BY id DESC LIMIT 1,1);
	
	SELECT lusd.id INTO @unitDispoId FROM lcl_unit_ss_dispo lusd JOIN disposition d ON lusd.disposition_id=d.id WHERE d.elite_code='AVAL' AND lusd.unit_id=new.unit_id LIMIT 1;
    IF new.disposition_id!='' AND @oldUnitId=new.unit_id THEN
	SET insert_values = concat_string(insert_values,' Disposition',(SELECT elite_code FROM disposition WHERE id=@oldDispoId),(SELECT elite_code FROM disposition WHERE id=new.disposition_id));
     ELSE
	SET insert_values = concat_insert_values(insert_values,' Disposition',(SELECT elite_code FROM disposition WHERE id=new.disposition_id));
    END IF;
    	SET @unit_id=(SELECT id FROM lcl_unit WHERE id=new.unit_id);
	SET @ss_header_id=(SELECT ss_header_id FROM lcl_ss_detail WHERE id=new.ss_detail_id );
	
  IF (@unitDispoId IS NULL) THEN
	SET @unit_ss_id=(SELECT id FROM lcl_unit_ss WHERE unit_id=@unit_id AND ss_header_id=@ss_header_id);
	INSERT INTO lcl_voyage_notification(unit_ss_id,MINUTE,Daily)VALUES(@unit_ss_id,'Pending','Pending');
		
	END IF;
    IF (insert_values!='') THEN       
	INSERT INTO lcl_unit_ss_remarks(unit_id,ss_header_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
         VALUES(@unit_id,@ss_header_id,'auto',insert_values,NOW(),new.entered_by_user_id,NOW(),new.modified_by_user_id);        
    END IF;
      END;
$$

DELIMITER ;

DROP TRIGGER /*!50032 if exists */ `after_lclUnitssDispo_delete`; 
--11DEC2013@MEI
DELIMITER $$

DROP TRIGGER /*!50032 IF EXISTS */ `after_LclUnit_update`$$

CREATE
    /*!50017 DEFINER = 'root'@'%' */
    TRIGGER `after_LclUnit_update` AFTER UPDATE ON `lcl_unit` 
    FOR EACH ROW BEGIN
    DECLARE updated_values TEXT ;
    IF new.unit_no!=old.unit_no THEN
       SET updated_values=(SELECT concat_string(updated_values,'Unit #',old.unit_no,new.unit_no));
    END IF;
    IF new.unit_type_id!=old.unit_type_id THEN
     SET updated_values=(SELECT concat_string(updated_values,'Size',(SELECT description FROM unit_type WHERE id=old.unit_type_id),(SELECT description FROM unit_type WHERE id=new.unit_type_id)));
   END IF;
    IF new.refrigerated!=old.refrigerated THEN
       SET updated_values=(SELECT concat_string(updated_values,'Refrigerated',IF(old.refrigerated=0,'No','Yes'),IF(new.refrigerated=0,'No','Yes')));
    END IF;
    IF new.hazmat_permitted!=old.hazmat_permitted THEN
       SET updated_values=(SELECT concat_string(updated_values,'Hazardous',IF(old.hazmat_permitted=0,'No','Yes'),IF(new.hazmat_permitted=0,'No','Yes')));
    END IF;
    IF ((OLD.su_heading_note IS NULL AND NEW.su_heading_note IS NOT NULL) OR NEW.su_heading_note != OLD.su_heading_note OR (OLD.su_heading_note IS NOT NULL AND NEW.su_heading_note IS NULL)) THEN
    SET updated_values=(SELECT concat_string(updated_values,'SU Heading Note',IFNULL(old.su_heading_note,''),IFNULL(new.su_heading_note,'')));
    END IF;
    IF ((OLD.remarks IS NULL AND NEW.remarks IS NOT NULL) OR NEW.remarks != OLD.remarks OR (OLD.remarks IS NOT NULL AND NEW.remarks IS NULL)) THEN
    SET updated_values=(SELECT concat_string(updated_values,'Trucking Information',IFNULL(old.remarks,''),IFNULL(new.remarks,'')));
    END IF;
    IF updated_values IS NOT NULL THEN
    SET updated_values=CONCAT(updated_values);
        INSERT INTO lcl_unit_ss_remarks(unit_id,ss_header_id,TYPE,remarks,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)
        VALUES(old.id,(SELECT ss_header_id FROM lcl_unit_ss WHERE unit_id=old.id),'auto',updated_values,NOW(),new.modified_by_user_id,NOW(),new.modified_by_user_id);
    END IF;
    END;
$$

DELIMITER ;
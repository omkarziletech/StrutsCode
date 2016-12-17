20/11/2008

ALTER TABLE `logisoft_test`.`fcl_bl` CHANGE `Yoyages` Yoyages VARCHAR(30);
ALTER TABLE `logisoft_test`.`booking_fcl` CHANGE `GoodsDescription` GoodsDescription VARCHAR(500);
ALTER TABLE `logisoft_test`.`booking_fcl` CHANGE `Remarks` Remarks VARCHAR(500);
ALTER TABLE `logisoft_test`.`fcl_bl` CHANGE `agent_name` agent_name VARCHAR(70),
  CHANGE `house_shipper_name` house_shipper_name VARCHAR(60),
  CHANGE `shipper_name` shipper_name VARCHAR(60),
  CHANGE `consignee_name` consignee_name VARCHAR(60),
  CHANGE `house_consignee_name` house_consignee_name VARCHAR(60),
  CHANGE `notify_party_name` notify_party_name VARCHAR(60),
  CHANGE `house_notify_party_name` house_notify_party_name VARCHAR(60),
  CHANGE `forwarding_agent_name` forwarding_agent_name VARCHAR(60);
ALTER TABLE `logisoft_test`.`fcl_bl_costcodes` CHANGE `account_name` account_name VARCHAR(60);
  
  ALTER TABLE `logisoft_test`.`transaction_ledger` CHANGE `Cust_name` Cust_name VARCHAR(60),
  CHANGE `Fwd_name` Fwd_name VARCHAR(60),
  CHANGE `Cons_name` Cons_name VARCHAR(60),
  CHANGE `Third_Pty_name` Third_Pty_name VARCHAR(60),
  CHANGE `Agent_name` Agent_name VARCHAR(60),
  CHANGE `ship_name` ship_name VARCHAR(60);
  ALTER TABLE `logisoft_test`.`transaction_ledger` CHANGE `GL_account_number` GL_account_number VARCHAR(20);
ALTER TABLE `logisoft_test`.`transaction_ledger` CHANGE `correction_flag` correction_flag VARCHAR(4); 
ALTER TABLE `logisoft_test`.`transaction_ledger` CHANGE `cust_no` cust_no VARCHAR(10);
ALTER TABLE `logisoft_test`.`transaction` CHANGE `GL_account_number` GL_account_number VARCHAR(20);
ALTER TABLE `logisoft_test`.`transaction` CHANGE `Transaction_amt` Transaction_amt decimal(10,2);
ALTER TABLE `logisoft_test`.`transaction` CHANGE `cust_no` cust_no VARCHAR(10);
ALTER TABLE `logisoft_test`.`transaction` CHANGE `cust_name` cust_name VARCHAR(60);
ALTER TABLE `logisoft_test`.`transaction_ledger` CHANGE `Destination` Destination VARCHAR(60);
ALTER TABLE `logisoft_test`.`transaction` CHANGE `Destination` Destination VARCHAR(60);

22/11/08

insert into codetype values('45','Hazardous Class');
insert into genericcode_dup (Codetypeid,code,codedesc) values ('45','1','UNNo 1');
insert into genericcode_dup (Codetypeid,code,codedesc) values ('45','2','UNNo 2');

28/11/2008

ALTER TABLE `logisoft_test`.`booking_fcl`ADD  emptypickAddress VARCHAR(200),
  ADD  posLocation VARCHAR(30),
  ADD   shipperFlag CHAR(1) ASCII,
  ADD  forwarderFlag CHAR(1) ASCII,
  ADD consigneeFlag CHAR(1) ASCII;
  
  ALTER TABLE `logisoft_test`.`booking_fcl` ADD  bookEmail VARCHAR(20);

ALTER TABLE `logisoft_test`.`quotation` ADD finalized VARCHAR(4);

insert into system_rules values(1,"AddrL1","514 North ");
insert into system_rules values(2,"AddrL2","Academy St");
insert into system_rules values(3,"City","Lincolnton");
insert into system_rules values(4,"State",",NC 28092");
insert into system_rules values(5,"Phone","PH: US +1  7047326063  Call  ");
insert into system_rules values(6,"Email","FAX: (704)-732-6383"); 

1/12/2008

ALTER TABLE `logisoft_test`.`booking_fcl`ADD  loadContact VARCHAR(20),
  ADD  loadPhone VARCHAR(13);
  
  //To get Comments in the reports
1/12/2008 
insert into genericcode_dup (Codetypeid,code,codedesc) values (39,"BC1","All rates are valid for 30 days. BAF subject to change without notice.");
insert into genericcode_dup (Codetypeid,code,codedesc) values (39,"BC2","Customs is enforcing a no doc, no load policy. If we do not recieve your documents by cutoff you will be");
insert into genericcode_dup (Codetypeid,code,codedesc) values (39,"BC3","charged a roll and demurrage fee, and/or your cargo will not load.");
	
 2/12/2008
 ALTER TABLE `logisoft_test`.`booking_fcl` ADD soc VARCHAR(1);
 //For Quotation Report
 insert
into genericcode_dup(Codetypeid,code,codedesc) values(39,"RC2","Customs is enforcing a no doc, no load policy. If we do not recieve your documents by cutoff you will be"); 
insert
into genericcode_dup(Codetypeid,code,codedesc) values(39,"RC3","charged a roll and demurrage fee, and/or your cargo will not load."); 
insert
into genericcode_dup(Codetypeid,code,codedesc) values(39,"RC4",""); 
insert
into genericcode_dup(Codetypeid,code,codedesc) values(39,"RC5",""); 
insert
into genericcode_dup(Codetypeid,code,codedesc) values(39,"RC6",""); 
insert
into genericcode_dup(Codetypeid,code,codedesc) values(39,"RC7",""); 
insert
into genericcode_dup(Codetypeid,code,codedesc) values(39,"RC8",""); 
// For Booking Reports
insert into genericcode_dup(Codetypeid,code,codedesc) values(39,"BC1","This booking confirmation is subject to receiving all relevant bill of lading information from the shipper in due time. S.E.A. Corp.");
insert into genericcode_dup(Codetypeid,code,codedesc) values(39,"BC2","shall not be responsible for any costs/delays that may occur due to intervention of customs.");
insert into genericcode_dup(Codetypeid,code,codedesc) values(39,"BC3","");
insert into genericcode_dup(Codetypeid,code,codedesc) values(39,"BC4","Shipper is responsible for making sure all auto paperwork/documentation is cleared through customs, and all copies of the");
insert into genericcode_dup(Codetypeid,code,codedesc) values(39,"BC5","stamped-off titles are sent to S.E.A. Corp. before the auto documentation cut off. The shipper will be required to pay all roll fees,");
insert into genericcode_dup(Codetypeid,code,codedesc) values(39,"BC6","port charges, and container per diem if container is in the port and the shipment rolls due to non-compliant customs approval.");
insert into genericcode_dup(Codetypeid,code,codedesc) values(39,"BC7","");
insert into genericcode_dup(Codetypeid,code,codedesc) values(39,"BC8","");

 3/12/2008
 ALTER TABLE `logisoft_test`.`fcl_bl_charges` ADD pcollect VARCHAR(20);
 
  4/12/2008
 ALTER TABLE `logisoft_test`.`quotation` ADD spcl_eqpmt VARCHAR(20);
   ALTER TABLE `logisoft_test`.`quotation` ADD booking_no Integer(20);
   
 ALTER TABLE `logisoft_test`.`booking_fcl` CHANGE `ConsigneeEmail` ConsigneeEmail VARCHAR(50),
  CHANGE `ForwarderEmail` ForwarderEmail VARCHAR(50),
  CHANGE `TruckerEmail` TruckerEmail VARCHAR(50),
  CHANGE `bookEmail` bookEmail VARCHAR(50);
  
 5/12/2008
 insert into system_rules values(1,"AddrL1","514 North ");
insert into system_rules values(2,"AddrL2"," Academy St");
insert into system_rules values(3,"City"," Lincolnton");
insert into system_rules values(4,"State"," NC 28092  ");
insert into system_rules values(5,"Phone"," PH:(704)732-6063");
insert into system_rules values(6,"Email"," FAX: (704)732-6383, OTI: 15564"); 


ALTER TABLE `logisoft_test`.`fcl_bl`
  
  ADD original_printing VARCHAR(4),
  ADD non_negotiable_printing VARCHAR(4),
  ADD express_bl_printing VARCHAR(4),
  ADD no_of_originals INT(5),
  ADD pre_carriage VARCHAR(50),
  ADD pre_carriage_POR VARCHAR(50),
  ADD loading_pier VARCHAR(50);
  
  ALTER TABLE `logisoft_test`.`fcl_bl` CHANGE `no_of_originals` no_of_originals VARCHAR(10);
  
  06/12/08
  ALTER TABLE `logisoft_test`.`fcl_bl` CHANGE `Export_reference` Export_reference VARCHAR(100);
  ALTER TABLE `logisoft_test`.`fcl_bl_costcodes` CHANGE `comments` comments VARCHAR(200),
  CHANGE `cost_code_desc` cost_code_desc VARCHAR(60);
ALTER TABLE `logisoft_test`.`fcl_bl_clause` CHANGE `desciption` desciption VARCHAR(60),
  CHANGE `text` text VARCHAR(100);
ALTER TABLE `logisoft_test`.`fcl_bl` CHANGE `Place_of_Reciept` Place_of_Reciept VARCHAR(50),
  CHANGE `Point_of_Country_and_Origin` Point_of_Country_and_Origin VARCHAR(50),
  CHANGE `Domestic_routing` Domestic_routing VARCHAR(60),
  CHANGE `Transhipment_To` Transhipment_To VARCHAR(30),
  CHANGE `Onward_Inland_Routing` Onward_Inland_Routing VARCHAR(60),
  CHANGE `SSline_Carrier` SSline_Carrier VARCHAR(40),
  CHANGE `streamShipLine` streamShipLine VARCHAR(40);
ALTER TABLE `logisoft_test`.`fcl_bl_charges` CHANGE `charges` charges VARCHAR(60),
  CHANGE `amount` amount DOUBLE(7,2);
ALTER TABLE `logisoft_test`.`fcl_bl_costcodes` CHANGE `amount` amount DOUBLE(10,2);

// this is for FCL BL Report Comments 6/12/2008
    insert into genericcode_dup(Codetypeid,code,codedesc) values(39,"BL1","TO BE RELEASED AGAINST SEA CORP ORIGINAL BL");
  insert into genericcode_dup(Codetypeid,code,codedesc) values(39,"BL2","RECEIVED by the CARRIER from the MERCHANT, in apparent good order and condition unless otherwise indicated, the GOODS mentioned above to be carried by the VESSEL,");
  insert into genericcode_dup(Codetypeid,code,codedesc) values(39,"BL3","subject to this BILL OF LADDING, from the Place of Receipt or the Port of Loading to the port of Discharge or Place of Delivery shown above.");
  insert into genericcode_dup(Codetypeid,code,codedesc) values(39,"BL4","If the GOODS are shipped by the MERCHANT in a CONTAINER or PACKAGE, then this BILL OF LADING is a");
  insert into genericcode_dup(Codetypeid,code,codedesc) values(39,"BL5","receipt only for the condition of the GOODS shall apply only to the number of such CONTAINERS or PACKAGES and their exterior condition.");
  insert into genericcode_dup(Codetypeid,code,codedesc) values(39,"BL6","The CARRIER has the right to use feeder ships, barges , airplanes, motor carriers or railcars for all or any part of its CARRIAGE");
  insert into genericcode_dup(Codetypeid,code,codedesc) values(39,"BL7","of the GOODS when the Place of Receipt is an inland point, notations in this BILL OF LADING such as On Board,");
  insert into genericcode_dup(Codetypeid,code,codedesc) values(39,"BL8","Loaded on Board, Shipped on Board mean on board the conveyance performing the overland or air carriage from the Place of Receipt to the Load Port.");
  insert into genericcode_dup(Codetypeid,code,codedesc) values(39,"BL9","In WITNESS hereof the carrier by its agent has signed");
  insert into genericcode_dup(Codetypeid,code,codedesc) values(39,"BL10","bills of lading, all of the same tenor and date, one of which being accomplished the Others to stand void. ");

  ALTER TABLE `logisoft_test`.`booking_fcl` CHANGE `doc_cut_off` doc_cut_off DATETIME;
  
    09/12/2008
  ALTER TABLE `logisoft_test`.`quotation` ADD origin_check VARCHAR(4),
  ADD pol_check VARCHAR(4),
  ADD pod_check VARCHAR(4),
  ADD destination_check VARCHAR(4),
  ADD poe VARCHAR(20);
  
    11/12/2008
  ALTER TABLE `logisoft_test`.`fcl_bl` ADD shipper_source VARCHAR(50),
  ADD consignee_source VARCHAR(50),
  ADD notifyParty_source VARCHAR(50);
  ALTER TABLE `logisoft_test`.`fcl_bl_marks_no` ADD desc_for_master_bl VARCHAR(500);
  ALTER TABLE `logisoft_test`.`fcl_bl_marks_no` ADD uom INT(30);
  ALTER TABLE `logisoft_test`.`fcl_bl` ADD master_bl_comments VARCHAR(500);
  ALTER TABLE `logisoft_test`.`fcl_bl` ADD master_bl_notes VARCHAR(750);
  
    12/12/2008
  ALTER TABLE `logisoft_test`.`quotation` CHANGE `origin_terminal` origin_terminal VARCHAR(50),
  CHANGE `destination_port` destination_port VARCHAR(50);
  
      16/12/2008
  ALTER TABLE `logisoft_test`.`batch` CHANGE `Total_Credit` Total_Credit DECIMAL(12,2),
  CHANGE `Total_Debit` Total_Debit DECIMAL(12,2);
  ALTER TABLE `logisoft_test`.`journal_entry` CHANGE `Debit` Debit DECIMAL(12,2),
  CHANGE `Credit` Credit DECIMAL(12,2);
  ALTER TABLE `logisoft_test`.`line_item` CHANGE `Debit` Debit DOUBLE(12,2),
  CHANGE `Credit` Credit DECIMAL(12,2);
  
    19/12/2008
  ALTER TABLE `logisoft_test`.`fcl_buy` ADD original_region VARCHAR(50),
  ADD destination_region VARCHAR(50);
  ALTER TABLE `logisoft_test`.`fcl_buy` CHANGE `origin_terminal` origin_terminal INT(20),
  CHANGE `destination_port` destination_port INT(20);
  CREATE TABLE logisoft_test.Fcl_Org_Dest_Misc_Data (
   origin_terminal VARCHAR(20),
   destination_port VARCHAR(20),
   local_drayage VARCHAR(1),
   days_in_transit INT(3),
   remarks VARCHAR(200)
)
ALTER TABLE `logisoft_test`.`Fcl_Org_Dest_Misc_Data` CHANGE `origin_terminal` origin_terminal INT(20),
  CHANGE `destination_port` destination_port INT(20),
  ADD id INT(11) AUTO_INCREMENT,
  ADD PRIMARY KEY (id);
  
    22/12/2008
  ALTER TABLE `logisoft_test`.`line_item` CHANGE `Debit` Debit DECIMAL(16,2),
  CHANGE `Credit` Credit DECIMAL(16,2);
  ALTER TABLE `logisoft_test`.`account_balance` CHANGE `Total_Debit` Total_Debit DECIMAL(15,2),
  CHANGE `Toatl_Credit` Toatl_Credit DECIMAL(15,2),
  CHANGE `Period_Balance` Period_Balance DECIMAL(15,2);
   ALTER TABLE `logisoft_test`.`journal_entry` CHANGE `Debit` Debit DECIMAL(15,2),
  CHANGE `Credit` Credit DECIMAL(15,2);
  ALTER TABLE `logisoft_test`.`batch` CHANGE `Total_Credit` Total_Credit DECIMAL(15,2),
  CHANGE `Total_Debit` Total_Debit DECIMAL(15,2);
  
   05/01/2009
  ALTER TABLE transaction ADD Credit_Terms int(4);
  
  // 10/01/09
//for AR
ALTER TABLE `logisoft_test`.`transaction` 
  ADD ship_no VARCHAR(20),
  ADD ship_name VARCHAR(20);
  
   16/01/2009
   //for Trading partner screen
   delete from item_master where program='view.jsp'
   delete from item_treestructure where item_id=37
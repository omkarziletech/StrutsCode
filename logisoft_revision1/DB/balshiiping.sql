CREATE TABLE `fiscal_year` (
  `ID` int(11) NOT NULL,
  `year` int(11) default NULL,
  `active` varchar(20) default NULL,
  `cls_Period` varchar(20) default NULL,
  `adj_Period` varchar(20) default NULL,
  PRIMARY KEY  (`ID`)
)

CREATE TABLE `system_rules` (
  `id` int(11) NOT NULL DEFAULT '0',
  `Rule_Code` varchar(30) DEFAULT NULL,
  `Rule_Name` varchar(100) DEFAULT NULL,
  PRIMARY KEY  (`id`)
)

insert into system_rules values(1,"AddrL1","965 Piedmont Road"); 
insert

into system_rules values(2,"AddrL2","Suite 200"); 
insert

into system_rules values(3,"City","Marietta"); 
insert

into system_rules values(4,"State","GA"); 
insert

into system_rules values(5,"Phone","US +1  7707928702  Call "); 
insert

into system_rules values(6,"Email","Balship@aol.com");


20/11/2008

ALTER TABLE `logisoft_test`.`subledger` ADD shipment_type VARCHAR(20);
ALTER TABLE `logisoft_test`.`subledger` ADD transcation_type VARCHAR(20);
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
ALTER TABLE `logisoft_test`.`transaction` CHANGE `GL_account_number` GL_account_number VARCHAR(20);
ALTER TABLE `logisoft_test`.`transaction` CHANGE `Transaction_amt` Transaction_amt decimal(10,2);
ALTER TABLE `logisoft_test`.`transaction` CHANGE `cust_no` cust_no VARCHAR(10);
ALTER TABLE `logisoft_test`.`transaction` CHANGE `cust_name` cust_name VARCHAR(60);
ALTER TABLE `logisoft_test`.`transaction_ledger` CHANGE `correction_flag` correction_flag VARCHAR(4); 
ALTER TABLE `logisoft_test`.`transaction_ledger` CHANGE `cust_no` cust_no VARCHAR(10);
ALTER TABLE `logisoft_test`.`transaction_ledger` ADD `Bill_to` varchar(20) default NULL,ADD `Customer_Reference_No` varchar(60) default NULL,
ADD `Originating_Terminal` varchar(20) default NULL,ADD `Destination` varchar(50) default NULL,ADD `Fwd_name` varchar(60) default NULL,ADD `Fwd_no` varchar(20) default NULL,
ADD `Cons_name` varchar(60) default NULL,ADD `Cons_no` varchar(20) default NULL,ADD `Third_Pty_name` varchar(60) default NULL,ADD `Third_Pty_no` varchar(20) default NULL,ADD `Agent_name` varchar(60) default NULL,
ADD `Agent_no` varchar(20) default NULL,ADD `Credit_Hold` varchar(20) default NULL,ADD  `ship_no` varchar(20) default NULL,
ADD  `ship_name` varchar(60) default NULL,ADD  `billing_terminal` varchar(20) default NULL,ADD  `correction_flag` varchar(4) default NULL;
ALTER TABLE `logisoft_test`.`transaction` ADD  `Bill_to` varchar(20) default NULL,ADD `Customer_Reference_No` varchar(100) default NULL,ADD `Originating_Terminal` varchar(20) default NULL,
ADD `Destination` varchar(20) default NULL,ADD  `Saling_date` date default NULL,ADD `Fwd_name` varchar(50) default NULL,ADD `Fwd_no` varchar(10) default NULL,
ADD  `Cons_name` varchar(50) default NULL,ADD `Cons_no` varchar(10) default NULL,ADD `Third_Pty_name` varchar(50) default NULL,ADD  `Third_Pty_no` varchar(10) default NULL,
ADD  `Agent_name` varchar(50) default NULL,ADD `Agent_no` varchar(10) default NULL,ADD `Credit_Hold` varchar(20) default NULL,ADD  `correction_flag` varchar(4) default NULL;
ALTER TABLE `logisoft_test`.`transaction_ledger` CHANGE `Destination` Destination VARCHAR(60);
ALTER TABLE `logisoft_test`.`transaction` CHANGE `Destination` Destination VARCHAR(60);

22/11/08

insert into codetype values('45','Hazardous Class');
insert into genericcode_dup (Codetypeid,code,codedesc) values ('45','1','UNNo 1');
insert into genericcode_dup (Codetypeid,code,codedesc) values ('45','2','UNNo 2');

28/11/2008

ALTER TABLE `logisoft_test`.`quotation` ADD finalized VARCHAR(4);

ALTER TABLE `logisoft_test`.`booking_fcl`ADD  emptypickAddress VARCHAR(200),
  ADD  posLocation VARCHAR(30),
  ADD   shipperFlag CHAR(1) ASCII,
  ADD  forwarderFlag CHAR(1) ASCII,
  ADD consigneeFlag CHAR(1) ASCII;
  
  ALTER TABLE `logisoft_test`.`booking_fcl` ADD  bookEmail VARCHAR(20);
  
 1/12/2008

ALTER TABLE `logisoft_test`.`booking_fcl`ADD  loadContact VARCHAR(20),
  ADD  loadPhone VARCHAR(13);

  2/12/2008
 ALTER TABLE `logisoft_test`.`booking_fcl` ADD soc VARCHAR(1);
 
 3/12/2008
 ALTER TABLE `logisoft_test`.`fcl_bl_charges` ADD pcollect VARCHAR(20);
 
 4/12/2008
 ALTER TABLE `logisoft_test`.`quotation` ADD spcl_eqpmt VARCHAR(20);
  ALTER TABLE `logisoft_test`.`quotation` ADD booking_no Integer(20);
  
  
  ALTER TABLE `logisoft_test`.`booking_fcl` CHANGE `ConsigneeEmail` ConsigneeEmail VARCHAR(50),
  CHANGE `ForwarderEmail` ForwarderEmail VARCHAR(50),
  CHANGE `TruckerEmail` TruckerEmail VARCHAR(50),
  CHANGE `bookEmail` bookEmail VARCHAR(50);
  
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
  
  CREATE TABLE `gl_mapping` (
  `id` int(11) NOT NULL DEFAULT '0',
  `Shipment_Type` varchar(20) DEFAULT NULL,
  `Charge_code` varchar(20) DEFAULT NULL,
	`Rev_Exp` varchar(20) DEFAULT NULL,
	`Transaction_Type` varchar(20) DEFAULT NULL,
	`GL_Acct` varchar(20) DEFAULT NULL,
	`Suffix_Value` varchar(20) DEFAULT NULL,
	`Derive_YN` varchar(20) DEFAULT NULL,
	`subLedger_code` varchar(20) NOT NULL DEFAULT '0',
  PRIMARY KEY  (`id`)
)
  
   05/01/2009
  ALTER TABLE transaction ADD Credit_Terms int(4);
  
  // 10/01/09
//for AR
ALTER TABLE `logisoft_test`.`transaction` 
  ADD ship_no VARCHAR(20),
  ADD ship_name VARCHAR(20);
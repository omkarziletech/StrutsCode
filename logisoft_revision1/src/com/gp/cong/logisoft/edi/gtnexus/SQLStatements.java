package com.gp.cong.logisoft.edi.gtnexus;

public class SQLStatements {
	public static final String FCL_BL = "select * from fcl_bl where file_no=?";
	
	public static final String QUOTATION = "select * from quotation where file_no =?";
	
	public static final String BOOKING = "select * from booking_fcl where file_no =?";
	
	public static final String CONTAINER_DETAILS = "select * from fcl_bl_container_dtls where BolId=?";
	
	public static final String SUBMITTER ="select * from terminal where trmnum=?";
	
	public static final String SSLINE = "select ssline_number from trading_partner where acct_no = ?";
	
	public static final String STATE = "select codedesc from genericcode_dup where id = ?";
	
	public static final String MARKS_NO = "select * from fcl_bl_marks_no where trailer_no_id=?";
	
	public static final String SIZE_LEGEND = "select field1,field2 from genericcode_dup where codetypeid='38' and id=?";
	
	public static final String VESSEL = "select codedesc from genericcode_dup where codetypeid='14' and id=?";
	
	public static final String CARRIER_SCAC = "select scac,contra from tlines where intgt='G' and linnum =?";
	
	public static final String USER_EMAIL = "select email from user_details where terminal_id = ?";

	public static final String SUBMITTER_INFO = "select trmnum,addres,city,state,cuntry,zipcde,phnnum,faxnum from loctn where trmnum = ?";
	
	public static final String FCLRCL = "select codedesc from genericcode_dup where codetypeid=7 and code=?";

	public static final String PACKAGETYPE = "select l009,pkform from packtp where l009 like ?";

	
	public static final String LOCATION_SCHED = "select unctcd,locnam from sched where unctcd = ? and cntnum=1";
	
	public static final String MOVE_TYPE = "select Field1,Field2,Field3,Field4 from genericcode_dup where codetypeid=48 and codedesc =?";

	public static final String QUANTITY_LINE = "select Field3,Field2,Field1 from edi_code_translation where codetypeid=3 and Field2 like ?";

	public static final String DOC_VERSION = "select filename from logfile_edi where status='success' and filename like ?";

	public static final String COUNT_VERSION = "select COUNT(*) from logfile_edi where filename like ?";

	
	public static final String UPDATE_LOG="insert into logfile_edi(filename,processed_date,status,description,edi_company,message_type) values(?,?,?,?,?,?)";
}
//
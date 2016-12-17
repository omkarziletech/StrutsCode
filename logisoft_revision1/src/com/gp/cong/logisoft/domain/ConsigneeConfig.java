package com.gp.cong.logisoft.domain;

import java.io.Serializable;

public class ConsigneeConfig implements Auditable,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String consigNo;
	private String firstName;
	private String lastName;
	private String position;
	private String phone;
	private String fax;
	private String email;
	private String comment;
	private GenericCode codea;
	private GenericCode codeb;
	private GenericCode codec;
	private GenericCode coded;
	private GenericCode codee;
	private GenericCode codef;
	private GenericCode codeg;
	private GenericCode codeh;
	private GenericCode codei;
	private int index;
	
	
	
	/**
	 * @return the consigNo
	 */
	public String getConsigNo() {
		return consigNo;
	}
	/**
	 * @param consigNo the consigNo to set
	 */
	public void setConsigNo(String consigNo) {
		this.consigNo = consigNo;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getFirstName() {
		return firstName;
	}
	public GenericCode getCodeb() {
		return codeb;
	}
	public void setCodeb(GenericCode codeb) {
		this.codeb = codeb;
	}
	public GenericCode getCodec() {
		return codec;
	}
	public void setCodec(GenericCode codec) {
		this.codec = codec;
	}
	public GenericCode getCoded() {
		return coded;
	}
	public void setCoded(GenericCode coded) {
		this.coded = coded;
	}
	public GenericCode getCodee() {
		return codee;
	}
	public void setCodee(GenericCode codee) {
		this.codee = codee;
	}
	public GenericCode getCodef() {
		return codef;
	}
	public void setCodef(GenericCode codef) {
		this.codef = codef;
	}
	public GenericCode getCodeg() {
		return codeg;
	}
	public void setCodeg(GenericCode codeg) {
		this.codeg = codeg;
	}
	public GenericCode getCodeh() {
		return codeh;
	}
	public void setCodeh(GenericCode codeh) {
		this.codeh = codeh;
	}
	public GenericCode getCodei() {
		return codei;
	}
	public void setCodei(GenericCode codei) {
		this.codei = codei;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public GenericCode getCodea() {
		return codea;
	}
	public void setCodea(GenericCode codea) {
		this.codea = codea;
	}
	public AuditInfo getAuditInfo() {
		// TODO Auto-generated method stub
		return null;
	}
	public Object getId1() {
		// TODO Auto-generated method stub
	return this.getConsigNo();
	}
	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	
	
}

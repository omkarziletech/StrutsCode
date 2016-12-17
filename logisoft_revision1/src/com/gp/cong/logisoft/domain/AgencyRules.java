package com.gp.cong.logisoft.domain;

import java.io.Serializable;

public class AgencyRules implements Serializable{
	private Integer id;
	private Integer agencyId;
	private String routeAgtAdminRule;
	private Double routeAgtAdminAmt;
	private Double routeAgtAdminTieramt;
	private String notRouteAgtAdminRule;
	private Double notRouteAgtAdminAmt;
	private Double notRouteAgtAdminTieramt;
	private String routeAgtCommnRule;
	private Double routeAgtCommnAmt;
	private Double routeAgtCommnTieramt;
	private String notRouteAgtCommnRule;
	private Double notRouteAgtCommnAmt;
	private Double notRouteAgtCommnTieramt;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getNotRouteAgtAdminAmt() {
		return notRouteAgtAdminAmt;
	}
	public void setNotRouteAgtAdminAmt(Double notRouteAgtAdminAmt) {
		this.notRouteAgtAdminAmt = notRouteAgtAdminAmt;
	}
	public String getNotRouteAgtAdminRule() {
		return notRouteAgtAdminRule;
	}
	public void setNotRouteAgtAdminRule(String notRouteAgtAdminRule) {
		this.notRouteAgtAdminRule = notRouteAgtAdminRule;
	}
	public Double getNotRouteAgtAdminTieramt() {
		return notRouteAgtAdminTieramt;
	}
	public void setNotRouteAgtAdminTieramt(Double notRouteAgtAdminTieramt) {
		this.notRouteAgtAdminTieramt = notRouteAgtAdminTieramt;
	}
	public Double getNotRouteAgtCommnAmt() {
		return notRouteAgtCommnAmt;
	}
	public void setNotRouteAgtCommnAmt(Double notRouteAgtCommnAmt) {
		this.notRouteAgtCommnAmt = notRouteAgtCommnAmt;
	}
	public String getNotRouteAgtCommnRule() {
		return notRouteAgtCommnRule;
	}
	public void setNotRouteAgtCommnRule(String notRouteAgtCommnRule) {
		this.notRouteAgtCommnRule = notRouteAgtCommnRule;
	}
	public Double getNotRouteAgtCommnTieramt() {
		return notRouteAgtCommnTieramt;
	}
	public void setNotRouteAgtCommnTieramt(Double notRouteAgtCommnTieramt) {
		this.notRouteAgtCommnTieramt = notRouteAgtCommnTieramt;
	}
	public Double getRouteAgtAdminAmt() {
		return routeAgtAdminAmt;
	}
	public void setRouteAgtAdminAmt(Double routeAgtAdminAmt) {
		this.routeAgtAdminAmt = routeAgtAdminAmt;
	}
	public String getRouteAgtAdminRule() {
		return routeAgtAdminRule;
	}
	public void setRouteAgtAdminRule(String routeAgtAdminRule) {
		this.routeAgtAdminRule = routeAgtAdminRule;
	}
	public Double getRouteAgtAdminTieramt() {
		return routeAgtAdminTieramt;
	}
	public void setRouteAgtAdminTieramt(Double routeAgtAdminTieramt) {
		this.routeAgtAdminTieramt = routeAgtAdminTieramt;
	}
	public Double getRouteAgtCommnAmt() {
		return routeAgtCommnAmt;
	}
	public void setRouteAgtCommnAmt(Double routeAgtCommnAmt) {
		this.routeAgtCommnAmt = routeAgtCommnAmt;
	}
	public String getRouteAgtCommnRule() {
		return routeAgtCommnRule;
	}
	public void setRouteAgtCommnRule(String routeAgtCommnRule) {
		this.routeAgtCommnRule = routeAgtCommnRule;
	}
	public Double getRouteAgtCommnTieramt() {
		return routeAgtCommnTieramt;
	}
	public void setRouteAgtCommnTieramt(Double routeAgtCommnTieramt) {
		this.routeAgtCommnTieramt = routeAgtCommnTieramt;
	}
	public Integer getAgencyId() {
		return agencyId;
	}
	public void setAgencyId(Integer agencyId) {
		this.agencyId = agencyId;
	}
	
}

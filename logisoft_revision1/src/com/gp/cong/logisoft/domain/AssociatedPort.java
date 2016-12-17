package com.gp.cong.logisoft.domain;

import java.io.Serializable;

public class AssociatedPort implements Serializable{
private Integer id;
private Ports portId;
private Integer lclId;

public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}
public Integer getLclId() {
	return lclId;
}
public void setLclId(Integer lclId) {
	this.lclId = lclId;
}
public Ports getPortId() {
	return portId;
}
public void setPortId(Ports portId) {
	this.portId = portId;
}
}

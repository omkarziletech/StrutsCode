package com.gp.cong.logisoft.beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.gp.cong.logisoft.domain.UnLocation;
import java.io.Serializable;

public class CitySelectionBean implements Serializable {
private String city;
private String state ;
private String counrty;
private String unloca;
private UnLocation unloaction;
public String getUnloca() {
	return unloca;
}
public void setUnloca(String unloca) {
	this.unloca = unloca;
}
public String getCity() {
	return city;
}
public void setCity(String city) {
	this.city = city;
}
public String getCounrty() {
	return counrty;
}
public void setCounrty(String counrty) {
	this.counrty = counrty;
}
public String getState() {
	return state;
}
public void setState(String state) {
	this.state = state;
}

public List getCityDetails(List city)
{

	List list=new ArrayList();
	CitySelectionBean csb;
	Iterator iterator=city.iterator();
	/*while(iterator.hasNext())
	{
		csb=new CitySelectionBean();
	UnLocation row= (UnLocation)iterator.next();
	
		
	String citys= row.getUnLocationName();
	    String country = "";
	    if(row.getCountryId()!=null)
	    {	
		country = ((GenericCode)row.getCountryId()).getCodedesc() ;
	    }
		String state="";
		if(row.getStateId()!=null)
		{
		state=((GenericCode)row.getStateId()).getCode();
		}
		
		csb.setUnloaction(row);
		csb.setCity(citys);
		csb.setCounrty(country);
		csb.setState(state);
		list.add(csb);

	}*/
	return list;
}
public UnLocation getUnloaction() {
	return unloaction;
}
public void setUnloaction(UnLocation unloaction) {
	this.unloaction = unloaction;
}
}
 

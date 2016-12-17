/**
 * 
 */
package com.gp.cong.logisoft.beans;

import java.io.Serializable;

/**
 * @author Rohith
 *
 */
public class SearchRelayBean implements Serializable {
	private String pol;
	private String polText;
	private String pod;
	private String podText;
	private String match;
	private String buttonValue;
	public String getPod() {
		return pod;
	}
	public void setPod(String pod) {
		this.pod = pod;
	}
	public String getPodText() {
		return podText;
	}
	public void setPodText(String podText) {
		this.podText = podText;
	}
	public String getPol() {
		return pol;
	}
	public void setPol(String pol) {
		this.pol = pol;
	}
	public String getPolText() {
		return polText;
	}
	public void setPolText(String polText) {
		this.polText = polText;
	}
	/**
	 * @return the buttonValue
	 */
	public String getButtonValue() {
		return buttonValue;
	}
	/**
	 * @param buttonValue the buttonValue to set
	 */
	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
	}
	/**
	 * @return the match
	 */
	public String getMatch() {
		return match;
	}
	/**
	 * @param match the match to set
	 */
	public void setMatch(String match) {
		this.match = match;
	}
	
	
}

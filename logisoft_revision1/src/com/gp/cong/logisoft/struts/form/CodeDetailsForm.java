package com.gp.cong.logisoft.struts.form;

import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.Genericcodelabels;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericcodelabelsDAO;
import com.gp.cong.logisoft.util.DBUtil;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class CodeDetailsForm extends ActionForm {

    private static final Logger log = Logger.getLogger(CodeDetailsForm.class);
    private String code;
    private String codevalue;
    private String codeTypeId;
    private String buttonValue;
    private String column1;
    private String column2;
    private String column3;
    private String column4;
    private String column5;
    private String column6;
    private String column7;
    private String column8;
    private String column9;
    private String column10;
    private String column11;
    private String column12;
    private String column13;
    private String country;
    private String city;

    public String getColumn8() {
	return column8;
    }

    public void setColumn8(String column8) {
	this.column8 = column8;
    }

    public String getCity() {
	return city;
    }

    public void setCity(String city) {
	this.city = city;
    }

    public String getCountry() {
	return country;
    }

    public void setCountry(String country) {
	this.country = country;
    }

    public String getCode() {
	return code;
    }

    public void setCode(String code) {

	this.code = code;
    }

    public String getButtonValue() {

	return this.buttonValue;
    }

    public void setButtonValue(String buttonValue) {

	this.buttonValue = buttonValue;
    }

    public ActionErrors validate(ActionMapping mapping,
	    HttpServletRequest request) {

	if (buttonValue.equals("codeselected")) {
	    try {
		ActionErrors ae = new ActionErrors();
		Genericcodelabels gcl = new Genericcodelabels();
		GenericcodelabelsDAO gcldao = new GenericcodelabelsDAO();
		if (code != null && !code.equals("")) {
		    gcl.setCodetypeid(Integer.parseInt(code));
		}
		List list = gcldao.findByExample(gcl);
		if (list.size() == 0) {
		    ae.add("myerror", new ActionError("code.error"));
		    return ae;
		}
	    } catch (Exception ex) {
		log.info("validate() in CodeDetailsForm failed on " + new Date(),ex);
	    }

	}
	if (buttonValue.equals("save")) {
	    try {
		HttpSession session = request.getSession(true);
		GenericCode gc = new GenericCode();
		gc.setCodetypeid(Integer.parseInt(codeTypeId));
		gc.setCode(codevalue);
		GenericCodeDAO gcdao = new GenericCodeDAO();
		List list = gcdao.findByExample(gc);
		if (list.size() > 0 && code != null) {
		    int i = Integer.parseInt(code);
		    DBUtil db = new DBUtil();
		    request.setAttribute("codeDetails", db.getCodesDetails(i));
		    String str = new Integer(i).toString();
		    request.setAttribute("code", str);
		    ActionErrors ae = new ActionErrors();
		    ae.add("myerror", new ActionError("codevalue.error"));
		    return ae;
		}
	    } catch (Exception ex) {
		log.info("validate() in CodeDetailsForm failed on " + new Date(),ex);
	    }

	}
	if (buttonValue.equals("update")) {
	    try {
		HttpSession session = request.getSession(true);
		GenericCode gc = new GenericCode();
		GenericCode gc1 = new GenericCode();
		String s = String.valueOf(session.getAttribute("id"));
		int id = (new Integer(s)).intValue();
		GenericCodeDAO gcdao = new GenericCodeDAO();
		gc.setCodetypeid(gcdao.findById(id).getCodetypeid());
		gc.setCode(gcdao.findById(id).getCode());
		List list = gcdao.findByExample(gc);
		if (list.size() > 0) {
		    gc1 = (GenericCode) list.get(0);
		    if (id != gc1.getId()) {
			ActionErrors ae = new ActionErrors();
			ae.add("myerror", new ActionError("codevalue.error"));
			return ae;
		    }
		}
	    } catch (Exception ex) {
		log.info("validate() in CodeDetailsForm failed on " + new Date(),ex);
	    }
	}

	return null;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {


	codevalue = "";
	column1 = "";
	column2 = "";
	column3 = "";
	column4 = "";
	column5 = "";
	column6 = "";
	column7 = "";
	column8 = "";

    }

    public String getCodevalue() {
	return codevalue;
    }

    public void setCodevalue(String codevalue) {
	this.codevalue = codevalue;
    }

    public String getColumn1() {
	return column1;
    }

    public void setColumn1(String column1) {
	this.column1 = column1;
    }

    public String getColumn2() {
	return column2;
    }

    public void setColumn2(String column2) {
	this.column2 = column2;
    }

    public String getColumn3() {
	return column3;
    }

    public void setColumn3(String column3) {
	this.column3 = column3;
    }

    public String getColumn4() {
	return column4;
    }

    public void setColumn4(String column4) {
	this.column4 = column4;
    }

    public String getColumn5() {
	return column5;
    }

    public void setColumn5(String column5) {
	this.column5 = column5;
    }

    public String getColumn6() {
	return column6;
    }

    public void setColumn6(String column6) {
	this.column6 = column6;
    }

    public String getColumn7() {
	return column7;
    }

    public void setColumn7(String column7) {
	this.column7 = column7;
    }

    public String getCodeTypeId() {
	return codeTypeId;
    }

    public void setCodeTypeId(String codeTypeId) {
	this.codeTypeId = codeTypeId;
    }

    public String getColumn9() {
	return column9;
    }

    public void setColumn9(String column9) {
	this.column9 = column9;
    }

    public String getColumn10() {
	return column10;
    }

    public void setColumn10(String column10) {
	this.column10 = column10;
    }

    public String getColumn11() {
	return column11;
    }

    public void setColumn11(String column11) {
	this.column11 = column11;
    }

    public String getColumn12() {
	return column12;
    }

    public void setColumn12(String column12) {
	this.column12 = column12;
    }

    public String getColumn13() {
	return column13;
    }

    public void setColumn13(String column13) {
	this.column13 = column13;
    }
}
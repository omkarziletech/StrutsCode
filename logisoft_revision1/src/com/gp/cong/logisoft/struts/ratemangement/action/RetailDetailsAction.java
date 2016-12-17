/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.ratemangement.action;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.beans.RetailRatesBean;
import com.gp.cong.logisoft.domain.RetailOceanDetailsRates;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.struts.ratemangement.form.RetailDetailsForm;

/**
 * MyEclipse Struts Creation date: 05-16-2008
 * 
 * XDoclet definition:
 * 
 * @struts.action path="/retailDetails" name="retailDetailsForm"
 *                input="/jsps/ratemanagement/retailDetails.jsp" scope="request"
 *                validate="true"
 */
public class RetailDetailsAction extends Action {
	/*
	 * Generated Methods
	 */

	/**
	 * Method execute
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception{
		RetailDetailsForm retailDetailsForm = (RetailDetailsForm) form;// TODO
		
		HttpSession session = ((HttpServletRequest) request).getSession();
		String forwardName = "";
		RetailOceanDetailsRates retailOceanDetailsRates = new RetailOceanDetailsRates();
		String buttonValue = retailDetailsForm.getButtonValue();
		String metric = retailDetailsForm.getMetric();
		RetailRatesBean retailRatesBean = new RetailRatesBean();
		User user = null;

		if (session.getAttribute("retailDetails") != null) {
			retailOceanDetailsRates = (RetailOceanDetailsRates) session
					.getAttribute("retailDetails");
		} else {
			retailOceanDetailsRates = new RetailOceanDetailsRates();
		}
		if (retailDetailsForm.getRateCbm() != null
				&& !retailDetailsForm.getRateCbm().equals("")) {
			retailOceanDetailsRates.setMetricCmb(new Double(
					retailDetailsForm.getRateCbm()));
		}
		if (retailDetailsForm.getRateCft() != null
				&& !retailDetailsForm.getRateCft().equals("")) {
			retailOceanDetailsRates.setEnglishLbs(new Double(
					retailDetailsForm.getRateCft()));
		}
		if (retailDetailsForm.getRateKgs() != null
				&& !retailDetailsForm.getRateKgs().equals("")) {
			retailOceanDetailsRates.setMetric1000kg(new Double(
					retailDetailsForm.getRateKgs()));
		}

		if (retailDetailsForm.getMetricMinamt() != null
				&& !retailDetailsForm.getMetricMinamt().equals("")) {
			retailOceanDetailsRates.setMetricMinAmt(new Double(
					retailDetailsForm.getMetricMinamt()));
		}
		if (retailDetailsForm.getEnglishMinamt() != null
				&& !retailDetailsForm.getEnglishMinamt().equals("")) {
			retailOceanDetailsRates.setEnglishMinAmt(new Double(
					retailDetailsForm.getEnglishMinamt()));
		}
		if (retailDetailsForm.getRateLb() != null
				&& !retailDetailsForm.getRateLb().equals("")) {

			retailOceanDetailsRates.setEnglish1000lb(new Double(
					retailDetailsForm.getRateLb()));
		}

		if (retailDetailsForm.getFirstOcean() != null
				&& !retailDetailsForm.getFirstOcean().equals("")) {
			retailOceanDetailsRates.setAOcean(new Double(
					retailDetailsForm.getFirstOcean()));
		}
		if (retailDetailsForm.getFirstTt() != null
				&& !retailDetailsForm.getFirstTt().equals("")) {
			retailOceanDetailsRates.setATt(new Double(retailDetailsForm
					.getFirstTt()));
		}

		if (retailDetailsForm.getSecondOcean() != null
				&& !retailDetailsForm.getSecondOcean().equals("")) {
			retailOceanDetailsRates.setBOcean(new Double(
					retailDetailsForm.getSecondOcean()));
		}
		if (retailDetailsForm.getSecondTt() != null
				&& !retailDetailsForm.getSecondTt().equals("")) {
			retailOceanDetailsRates.setBTt(new Double(retailDetailsForm
					.getSecondTt()));
		}

		if (retailDetailsForm.getThirdOcean() != null
				&& !retailDetailsForm.getThirdOcean().equals("")) {
			retailOceanDetailsRates.setCOcean(new Double(
					retailDetailsForm.getThirdOcean()));
		}
		if (retailDetailsForm.getThirdTt() != null
				&& !retailDetailsForm.getThirdTt().equals("")) {
			retailOceanDetailsRates.setCTt(new Double(retailDetailsForm
					.getThirdTt()));
		}

		if (retailDetailsForm.getFourthOcean() != null
				&& !retailDetailsForm.getFourthOcean().equals("")) {
			retailOceanDetailsRates.setDOcean(new Double(
					retailDetailsForm.getFourthOcean()));
		}
		if (retailDetailsForm.getFourthTt() != null
				&& !retailDetailsForm.getFourthTt().equals("")) {
			retailOceanDetailsRates.setDTt(new Double(retailDetailsForm
					.getFourthTt()));
		}

		retailOceanDetailsRates.setMeasureType(metric);

		if (retailDetailsForm.getTxtItemcreatedon() != null
				&& retailDetailsForm.getTxtItemcreatedon() != "") {
			java.util.Date javaDate = null;
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				javaDate = sdf.parse(retailDetailsForm.getTxtItemcreatedon());
			retailOceanDetailsRates.setEffectiveDate(javaDate);
		}
		if (session.getAttribute("loginuser") != null) {
			user = (User) session.getAttribute("loginuser");
		}
		retailOceanDetailsRates.setWhoChanged(user.getLoginName());
		session.setAttribute("retailDetails", retailOceanDetailsRates);

		if (buttonValue.equals("changemetric")) {
			retailRatesBean.setMetric(metric);
			request.setAttribute("retailRatesBean", retailRatesBean);

		}
		forwardName = "airDetailsAdd";
		request.setAttribute("buttonValue", buttonValue);

		return mapping.findForward(forwardName);
	}
}
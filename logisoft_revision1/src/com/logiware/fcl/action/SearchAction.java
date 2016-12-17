package com.logiware.fcl.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.fcl.ImportBc;
import com.gp.cong.logisoft.domain.User;
import com.logiware.fcl.form.SessionForm;
import com.logiware.fcl.dao.SearchDAO;
import com.logiware.fcl.form.SearchForm;
import com.logiware.fcl.model.PortModel;
import com.logiware.utils.ListUtils;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Lakshmi Narayanan
 */
public class SearchAction extends BaseAction {

    private static final String ORIGINS = "origins";
    private static final String DESTINATIONS = "destinations";

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	HttpSession session = request.getSession();
	SearchForm searchForm = (SearchForm) form;
        SessionForm oldSearchForm = new SessionForm();
        PropertyUtils.copyProperties(oldSearchForm, searchForm);
	session.setAttribute("oldSearchForm", oldSearchForm);
	new SearchDAO().search(searchForm);
	return mapping.findForward(SUCCESS);
    }

    public ActionForward doSort(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	HttpSession session = request.getSession();
        SearchForm searchForm = (SearchForm) form;
	SessionForm oldSearchForm = (SessionForm) session.getAttribute("oldSearchForm");
        PropertyUtils.copyProperties(searchForm, oldSearchForm);
	searchForm.setSortBy(request.getParameter("sortBy"));
	searchForm.setOrderBy(request.getParameter("orderBy"));
	new SearchDAO().search(searchForm);
	request.setAttribute("searchForm", searchForm);
	return mapping.findForward(SUCCESS);
    }

    public ActionForward gotoSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	HttpSession session = request.getSession();
        SearchForm searchForm = (SearchForm) form;
	User user = (User) session.getAttribute("loginuser");
	if (user.isSearchScreenReset()) {
	    session.removeAttribute("oldSearchForm");
	}
	SessionForm oldSearchForm = (SessionForm) session.getAttribute("oldSearchForm");
	if (null != oldSearchForm) {
	    session.removeAttribute("oldSearchForm");
            PropertyUtils.copyProperties(searchForm, oldSearchForm);
	    searchForm.setResults(null);
	    searchForm.setAction("reset");
	    request.setAttribute("searchForm", searchForm);
	    return mapping.findForward(SUCCESS);
	} else {
	    return reset(mapping, form, request, response);
	}
    }

    public ActionForward goBack(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	HttpSession session = request.getSession();
        SearchForm searchForm = (SearchForm) form;
	SessionForm oldSearchForm = (SessionForm) session.getAttribute("oldSearchForm");
	if (null != oldSearchForm) {
            PropertyUtils.copyProperties(searchForm, oldSearchForm);
	    searchForm.setAction("search");
	    new SearchDAO().search(searchForm);
	    request.setAttribute("searchForm", searchForm);
	    return mapping.findForward(SUCCESS);
	} else {
	    return reset(mapping, form, request, response);
	}
    }

    public ActionForward reset(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	request.getSession().removeAttribute("oldSearchForm");
	SearchForm searchForm = new SearchForm();
	Calendar cal = Calendar.getInstance();
	if (null != request.getSession().getAttribute(ImportBc.sessionName)) {
	    cal.add(Calendar.MONTH, -6);
	} else {
	    cal.add(Calendar.MONTH, -1);
	}
	searchForm.setFromDate(DateUtils.formatDate(cal.getTime(), "MM/dd/yyyy"));
	searchForm.setToDate(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
	request.setAttribute("searchForm", searchForm);
	return mapping.findForward(SUCCESS);
    }

    public ActionForward checkLocking(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	SearchForm searchForm = (SearchForm) form;
	User user = (User) request.getSession().getAttribute("loginuser");
	String result = new SearchDAO().checkLocking(searchForm.getFileNumber(), user.getUserId());
	PrintWriter out = response.getWriter();
	if (CommonUtils.isNotEmpty(result)) {
	    out.print(result);
	} else {
	    out.print("available");
	}
	out.flush();
	out.close();
	return null;
    }

    public ActionForward getOrigins(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	SearchForm searchForm = (SearchForm) form;
	String destination = searchForm.getDestination();
	String commodity = searchForm.getCommodity();
	String destinationCode = destination.substring(destination.lastIndexOf("(") + 1, destination.lastIndexOf(")"));
	List<PortModel> origins = new SearchDAO().getOrigins(destinationCode, commodity);
	if (CommonUtils.isNotEmpty(origins)) {
	    if (origins.size() == 1) {
		PrintWriter out = response.getWriter();
		out.print("========" + origins.get(0).getId());
		out.flush();
		out.close();
		return null;
	    } else {
		List<List<PortModel>> originsList = ListUtils.split(origins, 4, 12);
		request.setAttribute("originsList", originsList);
		return mapping.findForward(ORIGINS);
	    }
	} else {
	    PrintWriter out = response.getWriter();
	    out.print("No Data");
	    out.flush();
	    out.close();
	    return null;
	}
    }

    public ActionForward getDestinations(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	SearchForm searchForm = (SearchForm) form;
	String origin = searchForm.getOrigin();
	String commodity = searchForm.getCommodity();
	String originCode = origin.substring(origin.lastIndexOf("(") + 1, origin.lastIndexOf(")"));
	List<PortModel> destinations = new SearchDAO().getDestinations(originCode, commodity);
	if (CommonUtils.isNotEmpty(destinations)) {
	    if (destinations.size() == 1) {
		PrintWriter out = response.getWriter();
		out.print("========" + destinations.get(0).getId());
		out.flush();
		out.close();
		return null;
	    } else {
		List<List<PortModel>> destinationsList = ListUtils.split(destinations, 3, 12);
		request.setAttribute("destinationsList", destinationsList);
		return mapping.findForward(DESTINATIONS);
	    }
	} else {
	    PrintWriter out = response.getWriter();
	    out.print("No Data");
	    out.flush();
	    out.close();
	    return null;
	}
    }

    public ActionForward getDestinationsForCountry(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	SearchForm searchForm = (SearchForm) form;
	String destination = searchForm.getDestination();
	String country;
	if (destination.contains("/") && destination.contains("(")) {
	    country = StringUtils.substringAfterLast(StringUtils.substringBeforeLast(destination, "("), "/");
	} else if (destination.contains("(")) {
	    country = StringUtils.substringBeforeLast(destination, "(");
	} else if (destination.contains("/")) {
	    country = StringUtils.substringAfterLast(destination, "/");
	} else {
	    country = destination;
	}
	String destinations = new SearchDAO().getDestinationsForCountry(country);
	PrintWriter out = response.getWriter();
	if (CommonUtils.isNotEmpty(destinations)) {
	    out.print(destinations);
	} else {
	    out.print("");
	}
	out.flush();
	out.close();
	return null;
    }
}

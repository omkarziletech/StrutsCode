package com.logiware.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.lcl.GroupedCity;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.GroupedCityDAO;
import com.logiware.form.SearchUnLocationForm;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author N K Bala
 */
public class UnLocationAction extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    private static final String REMARKS = "remarksLookUp";
    private static final String ADD_UNLOCATION = "addUnLocation";
    private static final String EDIT_UNLOCATION = "editUnLocation";

    /**
     * This is the action called from the Struts framework.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception, SQLException {
        SearchUnLocationForm searchUnLocationForm = (SearchUnLocationForm) form;
        UnLocationDAO unLocationDAO = new UnLocationDAO();
        String buttonValue = searchUnLocationForm.getButtonValue();
        String forward = "success";
        if (CommonUtils.isEqual(buttonValue, "addUnLocations") && null != buttonValue) {
            //clear the form value
            clearFormValues(searchUnLocationForm);
            forward = ADD_UNLOCATION;
        } else if (CommonUtils.isEqual(buttonValue, "saveUnlocations") && null != buttonValue) {
            UnLocation unLocation = new UnLocation();
            unLocation.setUnLocationCode(searchUnLocationForm.getUnLocationCode());
            unLocation.setUnLocationName(searchUnLocationForm.getUnLocationName());
            unLocation.setAddSynonymousCity(searchUnLocationForm.getAddSynonymousCity());
            unLocation.setCyYard(searchUnLocationForm.getCyYard());
            unLocation.setAddSynonymousGroup(searchUnLocationForm.getAddSynonymousGroup());
            unLocation.setCountryId(new GenericCodeDAO().findById(Integer.parseInt(searchUnLocationForm.getCountryId())));
            unLocation.setLclExportService(searchUnLocationForm.isLclExportService());
            unLocation.setPierPass(searchUnLocationForm.getPierPass());
            if (CommonUtils.isNotEmpty(searchUnLocationForm.getTerminalnum())) {
                unLocation.setTerminal(new RefTerminalDAO().findById(searchUnLocationForm.getTerminalnum()));
            }
            if (CommonUtils.isNotEmpty(searchUnLocationForm.getMirrorLclCity())) {
                UnLocation mirrorCity = new UnLocationDAO().getUnlocation(searchUnLocationForm.getMirrorLclCity());
                unLocation.setLclRatedSourceId(mirrorCity);
            }
            if (CommonUtils.isNotEmpty(searchUnLocationForm.getOriginTerminal())) {
                unLocation.setOriginTerminal(new RefTerminalDAO().findById(searchUnLocationForm.getOriginTerminal()));
            }
            if (CommonUtils.isNotEmpty(searchUnLocationForm.getStateId())) {
                unLocation.setStateId(new GenericCodeDAO().findById(Integer.parseInt(searchUnLocationForm.getStateId())));
            }
            unLocation.setAlternatePortName(null != searchUnLocationForm.getAlternatePortName() ? searchUnLocationForm.getAlternatePortName().toUpperCase() : "");
            unLocationDAO.save(unLocation);
            List<UnLocation> unLocationList = unLocationDAO.getUnLocationList(searchUnLocationForm);
            request.setAttribute("UNLocationList", unLocationList);
            forward = SUCCESS;
        } else if (CommonUtils.isEqual(buttonValue, "editUnLocations")) {
            UnLocation unLocation = new UnLocationDAO().findById(searchUnLocationForm.getUserId());
            searchUnLocationForm.setLclExportService(unLocation.isLclExportService());
            if (null != unLocation.getTerminal()) {
                searchUnLocationForm.setTerminalnum(unLocation.getTerminal().getTrmnum());
            }
            if (null != unLocation.getOriginTerminal()) {
                searchUnLocationForm.setOriginTerminal(unLocation.getOriginTerminal().getTrmnum());
            }
            if (null != unLocation.getLclRatedSourceId()) {
                searchUnLocationForm.setMirrorLclCity(unLocation.getLclRatedSourceId().getUnLocationCode());
            } else {
                unLocation.setLclRatedSourceId(null);
            }
            searchUnLocationForm.setPierPass(unLocation.getPierPass());
            request.setAttribute("searchUnLocationForm", searchUnLocationForm);
            searchUnLocationForm.setCityId(unLocation.getId().toString());
            GroupedCityDAO groupCityDAO = new GroupedCityDAO();
            List<GroupedCity> CityList = groupCityDAO.executeQuery("from GroupedCity where city=" + unLocation.getId());
            List<GroupedCity> groupCityList = groupCityDAO.executeQuery("from GroupedCity where groupCity=" + unLocation.getId());
            request.setAttribute("groupCityList", CityList);
            request.setAttribute("CityList", groupCityList);
            searchUnLocationForm.setGroupCity("");
            request.setAttribute("update", "update");
            forward = EDIT_UNLOCATION;
        } else if (CommonUtils.isEqual(buttonValue, "updateUnlocations")) {
            UnLocation unLocation = new UnLocationDAO().findById(searchUnLocationForm.getUserId());
            unLocation.setUnLocationName(searchUnLocationForm.getUnLocationName());
            unLocation.setUnLocationCode(searchUnLocationForm.getUnLocationCode());
            unLocation.setCyYard(searchUnLocationForm.getCyYard());
            unLocation.setAddSynonymousCity(searchUnLocationForm.getAddSynonymousCity());
            unLocation.setAddSynonymousGroup(searchUnLocationForm.getAddSynonymousGroup());
            unLocation.setLclExportService(searchUnLocationForm.isLclExportService());
            unLocation.setPierPass(searchUnLocationForm.getPierPass());
            if (CommonUtils.isNotEmpty(searchUnLocationForm.getTerminalnum())) {
                unLocation.setTerminal(new RefTerminalDAO().findById(searchUnLocationForm.getTerminalnum()));
            } else {
                unLocation.setTerminal(null);
            }
            if (CommonUtils.isNotEmpty(searchUnLocationForm.getOriginTerminal())) {
                unLocation.setOriginTerminal(new RefTerminalDAO().findById(searchUnLocationForm.getOriginTerminal()));
            } else {
                unLocation.setOriginTerminal(null);
            }
            if (CommonUtils.isNotEmpty(searchUnLocationForm.getMirrorLclCity())) {
                UnLocation mirrorCity = new UnLocationDAO().getUnlocation(searchUnLocationForm.getMirrorLclCity());
                unLocation.setLclRatedSourceId(mirrorCity);
            } else {
                unLocation.setLclRatedSourceId(null);
            }
            if (CommonUtils.isNotEmpty(searchUnLocationForm.getCountryId())) {
                unLocation.setCountryId(new GenericCodeDAO().findById(Integer.parseInt(searchUnLocationForm.getCountryId())));
            }
            if (CommonUtils.isNotEmpty(searchUnLocationForm.getStateId())) {
                unLocation.setStateId(new GenericCodeDAO().findById(Integer.parseInt(searchUnLocationForm.getStateId())));
            }
            unLocation.setAlternatePortName(null != searchUnLocationForm.getAlternatePortName() ? searchUnLocationForm.getAlternatePortName().toUpperCase() : "");
            new UnLocationDAO().updatePorts(unLocation);
            request.setAttribute("searchUnLocationForm", searchUnLocationForm);
            List<UnLocation> unLocationList = unLocationDAO.getUnLocationList(searchUnLocationForm);
            request.setAttribute("UNLocationList", unLocationList);
        } else if (CommonUtils.isEqual(buttonValue, "restartUnLocations")) {
            clearFormValues(searchUnLocationForm);
            List<UnLocation> unLocationList = unLocationDAO.getUnLocationList(searchUnLocationForm);
            request.setAttribute("UNLocationList", unLocationList);
            forward = SUCCESS;
        } else if (CommonUtils.isEqual(buttonValue, "add")) {
            request.setAttribute("searchUnLocationForm", searchUnLocationForm);
            if (searchUnLocationForm.getCountryName() != null && !searchUnLocationForm.getCountryName().equals("")) {
                searchUnLocationForm.setCodeDesc(searchUnLocationForm.getCountryName());
            }
            UnLocation unLocation = unLocationDAO.getUnlocation(searchUnLocationForm.getUnLocationCode());
            String unLocationName = searchUnLocationForm.getGroupCity().substring(searchUnLocationForm.getGroupCity().indexOf('(') + 1, searchUnLocationForm.getGroupCity().indexOf(')'));
            UnLocation unLoc = unLocationDAO.getUnlocation(unLocationName);
            GroupedCity groupCity = new GroupedCity();
            GroupedCityDAO groupCityDAO = new GroupedCityDAO();
            groupCity.setCity(unLocation);
            groupCity.setGroupCity(unLoc);
            groupCityDAO.saveOrUpdate(groupCity);
            List<GroupedCity> groupCityList = groupCityDAO.executeQuery("from GroupedCity where city=" + unLocation.getId());
            List<GroupedCity> CityList = groupCityDAO.executeQuery("from GroupedCity where groupCity=" + unLocation.getId());
            request.setAttribute("CityList", CityList);
            request.setAttribute("groupCityList", groupCityList);
            request.setAttribute("update", "update");
            searchUnLocationForm.setGroupCity("");
            forward = EDIT_UNLOCATION;
        } else if (CommonUtils.isEqual(buttonValue, "deleteCity")) {
            request.setAttribute("searchUnLocationForm", searchUnLocationForm);
            if (searchUnLocationForm.getCountryName() != null && !searchUnLocationForm.getCountryName().equals("")) {
                searchUnLocationForm.setCodeDesc(searchUnLocationForm.getCountryName());
            }
            GroupedCityDAO groupCityDAO = new GroupedCityDAO();
            if (searchUnLocationForm.getGroupCityId() != null && !searchUnLocationForm.getGroupCityId().equals("")) {
                groupCityDAO.delete(Long.parseLong(searchUnLocationForm.getGroupCityId()));
            }
            UnLocation unLocation = unLocationDAO.getUnlocation(searchUnLocationForm.getUnLocationCode());
            List<GroupedCity> groupCityList = groupCityDAO.executeQuery("from GroupedCity where city=" + unLocation.getId());
            List<GroupedCity> CityList = groupCityDAO.executeQuery("from GroupedCity where groupCity=" + unLocation.getId());
            request.setAttribute("CityList", CityList);
            request.setAttribute("groupCityList", groupCityList);
            searchUnLocationForm.setGroupCity("");
            request.setAttribute("update", "update");
            forward = EDIT_UNLOCATION;
        } else {
            if (CommonUtils.isEqual(buttonValue, "cancel")) {
                clearFormValues(searchUnLocationForm);
            }
            List<UnLocation> unLocationList = unLocationDAO.getUnLocationList(searchUnLocationForm);
            if (unLocationList != null) {
                for (int j = 0; j < unLocationList.size(); j++) {
                    UnLocation unlocation = (UnLocation) unLocationList.get(0);
                    request.setAttribute("countryId", unlocation.getCountryId().getId());
                }
            }
            request.setAttribute("UNLocationList", unLocationList);
            forward = SUCCESS;
        }
        return mapping.findForward(forward);
    }

    private void clearFormValues(SearchUnLocationForm searchUnLocationForm) {
        searchUnLocationForm.setUnLocationCode("");
        searchUnLocationForm.setCountryId("");
        searchUnLocationForm.setCyYard("");
        searchUnLocationForm.setCountryName("");
        searchUnLocationForm.setUnLocationName("");
        searchUnLocationForm.setGroupCity("");
        searchUnLocationForm.setStateId("");
        searchUnLocationForm.setStateName("");
        searchUnLocationForm.setAddSynonymousCity("");
        searchUnLocationForm.setAddSynonymousGroup("");
        searchUnLocationForm.setAlternatePortName("");
    }
}

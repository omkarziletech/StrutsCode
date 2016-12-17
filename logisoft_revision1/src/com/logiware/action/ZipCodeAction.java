package com.logiware.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cvst.logisoft.hibernate.dao.ZipCodeDAO;
import com.logiware.form.ZipCodeForm;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.gp.cvst.logisoft.domain.Zipcode;

/**
 *
 * @author N K Bala
 */
public class ZipCodeAction extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    private static final String ADD_ZIPCODE = "addZipCode";
    private static final String EDIT_ZIPCODE= "editZipCode";
    private static final String ZIPCODE_LIST= "zipCodeList";

    /**
     * This is the action called from the Struts framework.
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
            throws Exception,SQLException {
        ZipCodeForm zipCodeForm = (ZipCodeForm) form;
        ZipCodeDAO zipCodeDAO = new ZipCodeDAO();
        Zipcode zipcode = new Zipcode();
        String buttonValue = zipCodeForm.getButtonValue();
        String forward = SUCCESS;
        if (CommonUtils.isEqual(buttonValue, "Save")) {
           zipcode.setZip(zipCodeForm.getZip());
           zipcode.setCity(zipCodeForm.getCity());
           zipcode.setState(zipCodeForm.getState());
           zipCodeDAO.save(zipcode);
        } else if (CommonUtils.isEqual(buttonValue, "Update")) {
            if(CommonFunctions.isNotNull(zipCodeForm.getId())){
                zipcode = zipCodeDAO.findById(Integer.parseInt(zipCodeForm.getId()));
                zipcode.setZip(zipCodeForm.getZip());
                zipcode.setCity(zipCodeForm.getCity());
                zipcode.setState(zipCodeForm.getState());
            }
        } else if (CommonUtils.isEqual(buttonValue, "Delete")) {
            if(CommonFunctions.isNotNull(zipCodeForm.getId())){
                zipcode = zipCodeDAO.findById(Integer.parseInt(zipCodeForm.getId()));
                zipCodeDAO.delete(zipcode);
            }
        }
        request.setAttribute(ZIPCODE_LIST, zipCodeDAO.getZipCodeList(zipCodeForm));
        return mapping.findForward(forward);
    }
}

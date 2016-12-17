/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.common.action;

import com.logiware.common.dao.PropertyDAO;
import org.apache.struts.action.ActionForm;
import com.logiware.common.form.PropertyForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.logiware.common.domain.Property;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Balaji.E
 */
public class PropertyAction extends BaseAction {

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PropertyForm propertiesForm = (PropertyForm) form;
        PrintWriter out = null;
        try {
            out = response.getWriter();
            Property properties = new PropertyDAO().findById(propertiesForm.getId());
            properties.setValue(propertiesForm.getValue());
            out.print(properties.getDescription() + " updated successfully");
            out.flush();
            return null;
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != out) {
                out.close();
            }
        }
    }
}

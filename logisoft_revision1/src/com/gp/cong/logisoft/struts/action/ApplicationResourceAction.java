package com.gp.cong.logisoft.struts.action;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Vinay
 */
import com.gp.cong.logisoft.struts.form.ApplicationResourceForm;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ApplicationResourceAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApplicationResourceForm propertyForm = (ApplicationResourceForm) form;
        path = getServlet().getServletContext().getRealPath("/");
        read();
        if ("update".equals(propertyForm.getAction())) {
            Set keySet = p.keySet();
            for (Object key : keySet) {
                setkey(key.toString(), request.getParameter(key.toString()));
            }
            write(path);
            read();
            request.setAttribute("properties", m);
        } else {
            request.setAttribute("properties", m);
        }
        return mapping.findForward("applicationResourceManagement");
    }

    private void setkey(String key, String val) throws Exception {
        p.setProperty(key, val);
    }

    private void read() throws Exception {
        m = new HashMap();
        InputStream is = new FileInputStream(path + "/WEB-INF/classes/com/gp/cong/struts/ApplicationResources.properties");
        p = new Properties();
        p.load(is);
        for (String name : p.stringPropertyNames()) {
            m.put(name, p.getProperty(name));
        }
    }

    private void write(String path) throws Exception {
        PrintWriter pw = new PrintWriter(path + "/WEB-INF/classes/com/gp/cong/struts/ApplicationResources.properties");
        p.store(pw, "");
//            LoadLogisoftProperties.setProp(p);
    }
    private Properties p;
    private Map m;
    private String path;
}

package com.logiware.common.action;

import com.google.gson.Gson;
import com.gp.cong.common.CommonUtils;
import com.logiware.common.filter.JspWrapper;
import com.logiware.common.form.FileUploadForm;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Lakshmi Narayanan
 */
public class FileUploadAction extends BaseAction {

    public ActionForward upload(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            FileUploadForm fileUploadForm = (FileUploadForm) form;
            if (fileUploadForm.isSetRequest()) {
                fileUploadForm.setRequest(request);
            }
            Class<?> cls = Class.forName(fileUploadForm.getClassName());
            Class<?> param = FileUploadForm.class;
            Object ins = cls.newInstance();
            Method method = cls.getDeclaredMethod(fileUploadForm.getMethodName(), param);
            if (CommonUtils.isNotEmpty(fileUploadForm.getForward())) {
                method.invoke(ins, fileUploadForm);
                return mapping.findForward(SUCCESS);
            } else if (CommonUtils.isEqual(fileUploadForm.getDataType(), "json")) {
                response.setContentType("application/json");
                Object result = method.invoke(ins, fileUploadForm);
                Gson gson = new Gson();
                out = response.getWriter();
                out.print(gson.toJson(result));
            } else {
                Object result = method.invoke(ins, fileUploadForm);
                out = response.getWriter();
                out.print(result);
            }
            return null;
        } catch (Exception e) {
            throw new ServletException(e);
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
    }

}

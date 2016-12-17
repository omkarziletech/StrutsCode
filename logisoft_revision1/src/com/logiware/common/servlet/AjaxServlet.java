package com.logiware.common.servlet;

import com.google.gson.Gson;
import com.gp.cong.common.CommonUtils;
import com.logiware.common.filter.JspWrapper;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.TreeMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author Lakshmi Narayanan
 */
public class AjaxServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            String className = request.getParameter("className");
            String methodName = request.getParameter("methodName");
            String formName = request.getParameter("formName");
            String forward = request.getParameter("forward");
            String dataType = request.getParameter("dataType");
            String requestParam = request.getParameter("request");
            Enumeration<String> parameterNames = request.getParameterNames();
            TreeMap<Integer, String> parameters = new TreeMap<Integer, String>();
            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();
                if (paramName.startsWith("param")) {
                    parameters.put(Integer.parseInt(paramName.replace("param", "")), request.getParameter(paramName));
                }
            }
            Class<?> cls = Class.forName(className);
            int size = 0;

            Class<?> formClass = null;
            Object form = null;
            if (CommonUtils.isNotEmpty(formName)) {
                formClass = Class.forName(formName);
                form = formClass.newInstance();
                BeanUtils.populate(form, request.getParameterMap());
                size++;
            }
            size += parameters.size() + (CommonUtils.isNotEmpty(forward) || "true".equals(requestParam) ? 1 : 0);

            Class<?> params[] = new Class[size];
            Object[] obj = new Object[size];

            int index = 0;
            if (CommonUtils.isNotEmpty(formName)) {
                params[index] = formClass;
                obj[index] = form;
                index++;
            }
            for (Integer key : parameters.keySet()) {
                params[index] = String.class;
                obj[index] = parameters.get(key);
                index++;
            }
            if (CommonUtils.isNotEmpty(forward) || "true".equals(requestParam)) {
                params[index] = HttpServletRequest.class;
                obj[index] = request;
            }
            Object ins = cls.newInstance();
            if (CommonUtils.isNotEmpty(forward)) {
                Method method = cls.getDeclaredMethod(methodName, params);
                method.invoke(ins, obj);
                Boolean noData = (Boolean) request.getAttribute("noData");
                if (null != noData && noData) {
                    out.print("");
                } else {
                    JspWrapper jspWrapper = new JspWrapper(response);
                    request.getRequestDispatcher(forward).include(request, jspWrapper);
                    String content = jspWrapper.getOutput();
                    out.print(content);
                }
            } else if (CommonUtils.isEqual(dataType, "json")) {
                response.setContentType("application/json");
                Method method = cls.getDeclaredMethod(methodName, params);
                Object result = method.invoke(ins, obj);
                Gson gson = new Gson();
                out.print(gson.toJson(result));
            } else {
                Method method = cls.getDeclaredMethod(methodName, params);
                Object result = method.invoke(ins, obj);
                out.print(result);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        doGet(request, response);
    }
}

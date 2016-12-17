package com.logiware.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
/**
 *
 * @author omi
 */
public class SearchAction  extends DispatchAction {

    private String newform="newform";
    private String newEmployeePage="newEmployeePage";
    public ActionForward newForm(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {           
            return mapping.findForward(newform);
            
            }   
    public ActionForward newEmployeePage(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {           
            return mapping.findForward(newEmployeePage);
            
            }   
    }
    


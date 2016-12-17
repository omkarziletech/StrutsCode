package com.logiware.action;

import com.logiware.bean.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import com.logiware.form.PersonForm;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author omi
 */
public class UserAction extends DispatchAction {

    public ActionForward add(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)throws Exception {
         

         PersonForm personForm = (PersonForm) form;
          boolean idFound=false;
          for(User user : personForm.getUserList()) {
             if(user.getId()==personForm.getId()){                     
               user.setId(personForm.getId());
               user.setName(personForm.getName());
               user.setLname(personForm.getLname());
                user.setGender(personForm.getGender());
               user.setPhone(personForm.getPhone());
               user.setPost(personForm.getPost()); 
               idFound=true;
               break;
            }
          } 
           if(!idFound){
           personForm.add(personForm.getUser().clone()); 
           }
         
         
        return mapping.findForward("success");
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)throws Exception {
        System.out.println("delete");
        PersonForm personForm = (PersonForm) form;
        User userFount=null;
        for (User user : personForm.getUserList()) {
            if (user.getId() == personForm.getId()) {
               userFount=user;
                 // personForm.getUserList().remove(personForm.getUser());
            }
        }
        if(userFount!=null){
           personForm.getUserList().remove(userFount);
        }
        return mapping.findForward("success");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        PersonForm personForm = (PersonForm) form;
        int id=personForm.getId();
        for(User user : personForm.getUserList()) {
            if (user.getId() == id) {
              //  personForm.setName(user.getId());
                personForm.setName(user.getName());
                personForm.setLname(user.getLname());
                 personForm.setGender(user.getGender());
                personForm.setPost(user.getPost());
                personForm.setPhone(user.getPhone());
               
            } else {
                System.out.println("id is not found");
            }

        }

        return mapping.findForward("success");
    }

    public ActionForward searchName(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        PersonForm personForm = (PersonForm) form;
        System.out.println(personForm.getName());
       // invForm.resetList();
       // String byName = request.getParameter("byName");
       // System.out.println("SearchBy name" + byName);
        
      personForm.setUserSearchList(new ArrayList<>());
        for (User user : personForm.getUserList()) {
            if (user.getName().equals(personForm.getName())) {
                System.out.println("equels" + user.getName());
                personForm.searchAdd(user);
            } else {
                System.out.println("no recout found");

            }
        }

        return mapping.findForward("success");
    }

}

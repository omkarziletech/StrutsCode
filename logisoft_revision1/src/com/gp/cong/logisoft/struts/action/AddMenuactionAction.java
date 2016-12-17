/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.gp.cong.logisoft.domain.Item;
import com.gp.cong.logisoft.domain.ItemTree;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.ItemDAO;
import com.gp.cong.logisoft.struts.form.AddMenuactionForm;

/** 
 * MyEclipse Struts
 * Creation date: 10-31-2007
 * 
 * XDoclet definition:
 * @struts.action path="/addMenuaction" name="addMenuactionForm" input="/jsps/admin/addMenuaction.jsp" scope="request" validate="true"
 */
public class AddMenuactionAction extends Action {
    /*
     * Generated Methods
     */

    /**
     * Method execute
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        AddMenuactionForm addMenuactionForm = (AddMenuactionForm) form;// TODO Auto-generated method stub
        HttpSession session = ((HttpServletRequest) request).getSession();
        String itemname = addMenuactionForm.getItemname();
        String predecessor = addMenuactionForm.getPredecessor();
        String buttonValue = addMenuactionForm.getButtonValue();
        String uniqueCode = addMenuactionForm.getItemCode();
        String forwardName = "";
        String message = "";
        if (buttonValue.equals("save")) {

            ItemDAO itemDAO = new ItemDAO();

            Item item = new Item();
            item.setItemDesc(addMenuactionForm.getItemname());
            item.setProgramName(addMenuactionForm.getProgramname());
            item.setUniqueCode(uniqueCode);
            Set itemTree = new HashSet<ItemTree>();
            ItemTree itemTr = new ItemTree();
            java.util.Date currdate = new java.util.Date();
            item.setItemcreatedon(currdate);
            Item parentItem = itemDAO.findById(Integer.parseInt(addMenuactionForm.getPredecessor()));
            itemTr.setParentId(parentItem);
            itemTree.add(itemTr);
            item.setItemTree(itemTree);
            User userId = null;
            if (session.getAttribute("loginuser") != null) {
                userId = (User) session.getAttribute("loginuser");
            }
            itemDAO.save(item, userId.getLoginName());
            List itemList = new ArrayList();
            itemList.add(item);
            session.setAttribute("itemList", itemList);
            message = "Item details added successfully";
            request.setAttribute("message", message);
            forwardName = "searchmenuaction";
            request.setAttribute("buttonValue", buttonValue);

        }

        if (buttonValue.equals("cancel")) {
            forwardName = "searchmenuaction";

        }

        return mapping.findForward(forwardName);
    }
}
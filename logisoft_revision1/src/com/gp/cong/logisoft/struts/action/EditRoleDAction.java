/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action;

import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.RoleDAO;
import com.gp.cong.logisoft.struts.form.EditRoleDForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Vinay
 */
public class EditRoleDAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        EditRoleDForm erdf = (EditRoleDForm) form;
        String forward = "showRoles";
        RoleDAO rd = new RoleDAO();
        if (null != erdf.getAction() && !erdf.getAction().equals("")) {
            if (erdf.getAction().equals("edit")) {
                forward = "editRole";
                if (!rd.exists(erdf)) {
                    rd.makeRoleDuty(erdf);
                }
                request.setAttribute("tooltips", tooltips);
                request.setAttribute("currRole", rd.getRoleDuty(erdf.getRoleId()));
            } else if (erdf.getAction().equals("update")) {
                rd.editRoleDuty(erdf);
                User loginUser = (User) request.getSession().getAttribute("loginuser");
                request.getSession().setAttribute("roleDuty", new RoleDAO().getRoleDuty(loginUser.getRole().getRoleId()));
            } else if (erdf.getAction().equals("clearProperty")) {
                rd.clearRoleDuty(erdf);
                request.setAttribute("tooltips", tooltips);
                request.setAttribute("currRole", rd.getRoleDuty(erdf.getRoleId()));
                forward = "editRole";
            }
        }
        request.setAttribute("rolesList", rd.getRoles());
        return mapping.findForward(forward);
    }
    private String[] tooltips = {"Allows user to access the FCLBL Corrrection void button",
        "Allows user to "
        + "<br> - Access AR Inquiry (Unable to adjust invoices) "
        + "<br> - Access batch and apply payments "
        + "<br> - Credit Hold - Allow user to put and take off hold"
        + "<br> - View General Ledger: Chart of Accounts"
        + "<br> - View General Ledger: Charge Codes"
        + "<br> - Full access to Accounts Payable module"
        + "<br> - TP Access: new/AP config/Contact config"
        + "<br> Does not allow the user to -"
        + "<br> - Manipulate data in batch opened by another user"
        + "<br> - Post batch opened by another user"
        + "<br> - Post batches against direct General Ledger",
        "AR Inquiry Account Manager",
        "Allows the user to - "
        + "<br> - Have full access to Accounts Recievable module"
        + "<br> - Manipulate data and post batch opened by another user"
        + "<br> - Full access to Accounts Payable"
        + "<br> - Full access to General Ledger module"
        + "<br> - Full access to TP",
        "Allow user to put and take off hold",
        "Allows the user to - "
        + "<br> - Have full access to Accounts Recievable module"
        + "<br> - Manipulate data and post batch opened by another user"
        + "<br> - Full access to Accounts Payable (Excluding approving wire/ach payments)"
        + "<br> - Full access to General Ledger module"
        + "<br> - Full access to TP",
        "Allow user to Unmanifest",
        "Allow user to Reopen BL",
        "Allow user to Show Detailed Charges",
        "Allow user to Approve,Un Approve,Delete corrections",
        "Allow user to Reverse corrections",
        "Allow user to Disable/Enable TradingPartner",
        "Allow user to acknowledge a disputed BL and enter disputed notes",
        "Allow user to Create Vendor Other than FF",
        "Allow user to Edit ECI Account Number",
        "Allow user to Add Predefined Remarks",
        "Allow user to Change Trading Partner",
        "Allow user to change Master",
        "Allow user to Access Correction Print/Fax/Email",
        "Allow user to Take ownership of Disputed BLs",
        "AR Inquiry - Change Customer",
        "AR Inquiry - Make Adjustments",
        "AR Batch - Manage All Users Batch",
        "AR Batch - Enter Direct GL Account",
        "Accruals - Create new Accruals",
        "Bank Account - Create new Bank account",
        "Journal Entry - Select closed period to post",
        "Add TP - Set Vendor as account type",
        "Trading Partner - Show Address",
        "Trading Partner - Show General Info",
        "Trading Partner - Show Ar Config",
        "Trading Partner - Show Ap Config",
        "Trading Partner - Show Contact Config",
        "Trading Partner - Show Consignee Config",
        "Allow user to Resend AES",
        "View Accounting Scan/Attach",
        "Allow user to Close BL",
        "Allow user to Audit",
        "Allow user to Cancel Audit",
        "Allow user to Resend Accruals",
        "Allow user to Void or Reprint Checks",
        "Allow user to Routed Agent",
        "Allow user to delete LCL Commodity",
        "Allow user to see Follow Up Tasks",
        "Trading Partner - Display Defaults",
        "Allow a user to Close/Audit a file without having the Origin Invoice Attached",
        "Allows users to change SSL BL regardless of setup in RDMS",
        "Force Voyage on LCL Exp Booking",
        "Allow user to Terminate Booking Without Invoice",
        "AR Batch - Reversal of Cash Batches",
        "Show All AP Payment",
        "Reverse Posted Invoices",
        "Credit Hold - OPS Level user",
        "Allow user to mass inactivate accruals",
        "LCL - Power to change BL Owner",
        "Allow User To Delete LCL Voyage",
        "Allow User To Open LCL Unit",
        "Allow User To Unmanifest LCL Unit",
        "Update Lcl Current Location",
        "Unpost BL",
        "Allowed to change Voyage in LCL",
        "Allow user to terminate LCL import DR",
        "LCL - Power to change Voyage Owner",
        "Trading Partner - Show CTS Config",
        "Allow user to change ECU Designation",
        "Allow user to delete  Disposition",
        "Allow user to delete Import FCL Containers",
        "Allow user to delete Import Unit",
        "Allow user to Close/Audit LCL Import Voyage",
        "Allow user to Delete Cost and Charges in Fcl Export",
        "Allow user to Reset LCL Unit OSD Details",
        "Lcl OSD",
        "Allow Charge or Cost code Mapping for LCL Eculine Invoice",
        "Allow to Enter Spot Rate in FCL Quotes,Bookings",
        "Verify imports Voyage POD",
        "Delete Attached Documents",
        "Allow User to Edit Deferral Charge",
        "AR Config Tab Read Only",
        "Allow user to Reopen LCL Import Voyage",
        "The ability to mark an account as a Vendor - Import CFS",
        "Delete Manual Notes",
        "Allow User to Change sales code",
        "Link DR after Disposition Port",
        "Allow User to Logo Change",
        "This allows users to disable containers even though costs have been flagged as AP."
            + " It will not update the cost amounts automatically",
        "Show Incomplete Units",
        "Change BL Owner test",
        "Reverse Cob",
        "Void LCL BL after COB",
        "Delete Lcl Exports Notes",
        "AES required for Releasing DRs",
        "AES required for Posting BLs",
        "Allows User to Undo Close/Audit",
        "Delete Lcl Exports Notes",
        "Allow user to select ECI acct number in TP search screen",
        "To add Batch HS Code for Consolidate",
        "No 997 EDI Submission",
        "Force Client on LCL Exports quotes",
        "Allows user to make trashipment file after posted charges"
    };
}

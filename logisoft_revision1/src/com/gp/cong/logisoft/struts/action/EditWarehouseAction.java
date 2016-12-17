 /*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.action;

import com.gp.cong.common.CommonUtils;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.beans.NoteBean;
import com.gp.cong.logisoft.domain.AuditLogRecord;
import com.gp.cong.logisoft.domain.AuditLogRecordWarehouse;
import com.gp.cong.logisoft.domain.Item;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.Warehouse;
import com.gp.cong.logisoft.domain.WarehouseTemp;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.ItemDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.hibernate.dao.WarehouseDAO;
import com.gp.cong.logisoft.struts.form.EditWarehouseForm;
import com.gp.cong.logisoft.util.DBUtil;

/** 
 * MyEclipse Struts
 * Creation date: 12-04-2007
 * @author Rohith
 * XDoclet definition:
 * @struts.action path="/editWarehouse" name="editWarehouseForm" input="jsps/datareference/editWarehouse.jsp" scope="request" validate="true"
 */
public class EditWarehouseAction extends Action {
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
        EditWarehouseForm editWarehouseForm = (EditWarehouseForm) form;// TODO Auto-generated method stub
        HttpSession session = ((HttpServletRequest) request).getSession();

        String warehouseCode = editWarehouseForm.getWarehouseCode();
        String warehouseName = editWarehouseForm.getWarehouseName();
        String managerName = editWarehouseForm.getManagerName();
        String address = editWarehouseForm.getAddress();
        String city = editWarehouseForm.getCity();
        String country = editWarehouseForm.getCountry();
        String acCountry = editWarehouseForm.getAcCountry();
        String zip = editWarehouseForm.getZip();
        String phone = editWarehouseForm.getPhone();
        String fax = editWarehouseForm.getFax();
        String type = editWarehouseForm.getWarehouseType();
        String acWarehouse = editWarehouseForm.getGeneralAirCargo();
        String ipiVendor = CommonUtils.isNotEmpty(editWarehouseForm.getIpiVendor()) ? editWarehouseForm.getIpiVendor().split(":-")[0]:null;
        String ipiCommodity = editWarehouseForm.getIpiCommodity();
        String acAddress = editWarehouseForm.getAcAddress();
        String acCity = editWarehouseForm.getAcCity();
        String acZip = editWarehouseForm.getAcZip();
        String acPhone = editWarehouseForm.getAcPhone();
        String extension = editWarehouseForm.getExtension();
        String acExtension = editWarehouseForm.getAcExtension();
        String acFax = editWarehouseForm.getAcFax();
        String state = editWarehouseForm.getState();
        UnLocationDAO locationDAO = new UnLocationDAO();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        DBUtil dbUtil = new DBUtil();

        String buttonValue = editWarehouseForm.getButtonValue();
        Warehouse warehouse = null;
        String forwardName = "";
        String message = "";
        String phone1 = "";
        String fax1 = "";
        String acphone1 = "";
        String acfax1 = "";

        if (session.getAttribute("warehouse") == null) {
            warehouse = new Warehouse();
        } else {
            warehouse = (Warehouse) session.getAttribute("warehouse");
        }

        if (!buttonValue.equals("cancel") && !buttonValue.equals("cancelview") && !buttonValue.equals("note") && !buttonValue.equals("searchcity") && !buttonValue.equals("searchaccity")) {
            WarehouseDAO warehouseDAO = new WarehouseDAO();
            warehouse.setWarehouseNo(warehouseCode);
            warehouse.setWarehouseName(warehouseName);
            warehouse.setAddress(address);
            warehouse.setZipCode(zip);
            warehouse.setPhone(phone);
            warehouse.setExtension(extension);
            warehouse.setFax(fax);
            warehouse.setCountryCode(genericCodeDAO.findByCodeDescName(country, 11));
            warehouse.setAcCountryCode(genericCodeDAO.findByCodeDescName(acCountry, 11));
            warehouse.setCityCode(warehouse.getCityCode());
            warehouse.setState(state);
            warehouse.setImportsCFSDevanning(editWarehouseForm.getImportCfsDevanning());
            warehouse.setCfsDevanningEmail(editWarehouseForm.getCfsDevanningEmail());
            List list1 = locationDAO.findbyCity(acCity);
            if (list1 != null && list1.size() > 0) {
                UnLocation location1 = (UnLocation) list1.get(0);
                warehouse.setAcCity(location1);
            }
            warehouse.setAcWarehouseName(acWarehouse);
            warehouse.setVendorNo(ipiVendor);
            warehouse.setCommodityNo(ipiCommodity);
            warehouse.setAcAddress(acAddress);
            warehouse.setAcZipCode(acZip);
            warehouse.setAcPhone(acPhone);
            warehouse.setAcFax(acFax);
            warehouse.setPhone(phone);
            warehouse.setAcExtension(acExtension);
            warehouse.setWarehouseType(type);
            warehouse.setCity(city);
            List list = locationDAO.findbyCity(city);
            UnLocation location = null;
            if (list != null && list.size() > 0) {
                location = (UnLocation) list.get(0);
            }
            if (location != null) {
                warehouse.setCityCode(location);
            }
            warehouse.setAirCity(acCity);
            warehouse.setManager(CommonUtils.isNotEmpty(managerName) ? managerName :null);
            if (buttonValue != null && buttonValue.equals("save")) {


                List warehouseList = (List) session.getAttribute("warehouseList");

                for (int i = 0; i < warehouseList.size(); i++) {
                    WarehouseTemp tempWarehouse = (WarehouseTemp) warehouseList.get(i);
                    if (tempWarehouse.getId().equals(warehouse.getId())) {
                        tempWarehouse.setWarehouseName(warehouse.getWarehouseName());
                        tempWarehouse.setWarehouseNo(warehouse.getWarehouseNo());

                        tempWarehouse.setCity(warehouse.getCity());
                        warehouseList = new ArrayList();
                        warehouseList.add(tempWarehouse);
                        session.setAttribute("warehouseList", warehouseList);
                        User userid = null;
                        if (session.getAttribute("loginuser") != null) {
                            userid = (User) session.getAttribute("loginuser");
                        }
                        warehouseDAO.update(warehouse, userid.getLoginName());
                        if (session.getAttribute("view") != null) {
                            session.removeAttribute("view");
                        }
                    }
                }
            }
            if (buttonValue != null && buttonValue.equals("delete")) {
                List warehouseList1 = (List) session.getAttribute("warehouseList");

                String programid = null;
                programid = (String) session.getAttribute("processinfoforwarehouse");

                String recordid = "";
                if (warehouse != null && warehouse.getId() != null) {
                    recordid = warehouse.getId().toString();
                }
                for (int i = 0; i < warehouseList1.size(); i++) {
                    WarehouseTemp tempWarehouse = (WarehouseTemp) warehouseList1.get(i);
                    if (tempWarehouse.getId().equals(warehouse.getId())) {
                        warehouseList1.remove(tempWarehouse);

                        WarehouseDAO warehouse1DAO = new WarehouseDAO();
                        User userId = null;
                        if (session.getAttribute("loginuser") != null) {
                            userId = (User) session.getAttribute("loginuser");
                        }
                        warehouse1DAO.delete(warehouse, userId.getLoginName());
                        dbUtil.getProcessInfo(programid, recordid, "deleted", "edited");
                        message = "Warehouse details deleted successfully";
                        request.setAttribute("message", message);
                        if (session.getAttribute("view") != null) {
                            session.removeAttribute("view");
                        }
                        return mapping.findForward("cancel");
                    }
                }
            }

            if (buttonValue.equals("save")) {
                String programid = "";
                programid = (String) session.getAttribute("processinfoforwarehouse");

                String recordid = "";
                if (warehouse != null && warehouse.getId() != null) {
                    recordid = warehouse.getId().toString();
                }
                dbUtil.getProcessInfo(programid, recordid, "edited", "deleted");
                if (session.getAttribute("warehouse") != null) {
                    session.removeAttribute("warehouse");
                }
                message = "Warehouse details updated successfully";
                request.setAttribute("message", message);
                return mapping.findForward("cancel");
            }
            return mapping.findForward("editsave");
        }

        if (buttonValue != null && buttonValue.equals("cancel")) {
            String programid = null;
            programid = (String) session.getAttribute("processinfoforwarehouse");
            String recordid = "";
            if (warehouse != null && warehouse.getId() != null) {
                recordid = warehouse.getId().toString();
            }
            dbUtil.getProcessInfo(programid, recordid, "editcancelled", null);
            if (session.getAttribute("warehouse") != null) {
                session.removeAttribute("warehouse");
            }
            if (session.getAttribute("view") != null) {
                session.removeAttribute("view");
            }
            return mapping.findForward("cancel");
        }

        if (buttonValue != null && buttonValue.equals("cancelview")) {
            if (session.getAttribute("view") != null) {
                session.removeAttribute("view");
            }
            return mapping.findForward("cancel");
        }
        if (buttonValue != null && buttonValue.equals("note")) {
            ItemDAO itemDAO = new ItemDAO();
            Item item = new Item();
            String itemName = "";
            if (session.getAttribute("processinfoforwarehouse") != null) {
                String itemId = (String) session.getAttribute("processinfoforwarehouse");
                item = itemDAO.findById(Integer.parseInt(itemId));
                itemName = item.getItemDesc();
            }

            forwardName = "note";
            AuditLogRecord auditLogRecord = new AuditLogRecordWarehouse();
            NoteBean noteBean = new NoteBean();
            noteBean.setItemName(itemName);
            noteBean.setAuditLogRecord(auditLogRecord);
            noteBean.setButtonValue(buttonValue);
            //noteBean.setUser(user);
            String noteId = ""+warehouse.getId();
            noteBean.setNoteId(noteId);
            noteBean.setReferenceId(noteId);
            List auditList = null;
            auditList = dbUtil.getNoteInformation(noteId, auditLogRecord);
            noteBean.setAuditList(auditList);
            noteBean.setVoidednote("");
            noteBean.setPageName("cancelwarehouse");
            request.setAttribute("noteBean", noteBean);
            String documentName = "User";
            request.setAttribute("buttonValue", buttonValue);
            return mapping.findForward("note");
        }
        if (buttonValue.equals("searchcity")) {
            if (session.getAttribute("warehouse") != null) {
                warehouse = (Warehouse) session.getAttribute("warehouse");
            } else {
                warehouse = new Warehouse();
            }
            phone1 = dbUtil.stringtokenizer(phone);
            fax1 = dbUtil.stringtokenizer(fax);
            acphone1 = dbUtil.stringtokenizer(acPhone);
            acfax1 = dbUtil.stringtokenizer(acFax);
            warehouse.setWarehouseName(warehouseName);

            warehouse.setAddress(address);

            warehouse.setZipCode(zip);
            warehouse.setPhone(phone);
            warehouse.setExtension(extension);
            warehouse.setAcWarehouseName(acWarehouse);
            warehouse.setVendorNo(ipiVendor);
            warehouse.setCommodityNo(ipiCommodity);
            warehouse.setAcAddress(acAddress);
            warehouse.setAcZipCode(acZip);
            warehouse.setAcPhone(acphone1);
            warehouse.setAcFax(acfax1);
            warehouse.setPhone(phone1);
            warehouse.setAcExtension(acExtension);
            warehouse.setFax(fax1);
            UnLocationDAO unLocationDAO = new UnLocationDAO();
            List searchcity = unLocationDAO.findbyCity(city);
            if (searchcity != null && searchcity.size() > 0) {
                UnLocation unLoc = (UnLocation) searchcity.get(0);
                warehouse.setCityCode(unLoc);
                warehouse.setCity(city);
            } else {
                UnLocation unLoc = null;
                warehouse.setCityCode(unLoc);
                warehouse.setCity(city);
            }
            if (session.getAttribute("warehouse2") != null) {
                session.removeAttribute("warehouse2");
            }
            session.setAttribute("warehouse", warehouse);
            return mapping.findForward("editsave");
        }
        if (buttonValue.equals("searchaccity")) {
            if (session.getAttribute("warehouse") != null) {
                warehouse = (Warehouse) session.getAttribute("warehouse");
            } else {
                warehouse = new Warehouse();
            }
            phone1 = dbUtil.stringtokenizer(phone);
            fax1 = dbUtil.stringtokenizer(fax);
            acphone1 = dbUtil.stringtokenizer(acPhone);
            acfax1 = dbUtil.stringtokenizer(acFax);
            warehouse.setWarehouseName(warehouseName);

            warehouse.setAddress(address);

            warehouse.setZipCode(zip);
            warehouse.setPhone(phone);
            warehouse.setExtension(extension);
            warehouse.setAcWarehouseName(acWarehouse);
            warehouse.setVendorNo(ipiVendor);
            warehouse.setCommodityNo(ipiCommodity);
            warehouse.setAcAddress(acAddress);
            warehouse.setAcZipCode(acZip);
            warehouse.setAcPhone(acphone1);
            warehouse.setAcFax(acfax1);
            warehouse.setPhone(phone1);
            warehouse.setAcExtension(acExtension);
            warehouse.setFax(fax1);
            UnLocationDAO unLocationDAO = new UnLocationDAO();
            List searchaccity = unLocationDAO.findbyCity(acCity);
            if (searchaccity != null && searchaccity.size() > 0) {

                UnLocation unLoc = (UnLocation) searchaccity.get(0);
                warehouse.setAcCity(unLoc);
                warehouse.setAirCity(acCity);
            } else {
                UnLocation unLoc = null;
                warehouse.setCityCode(unLoc);

                warehouse.setAcCity(unLoc);
                warehouse.setAirCity(acCity);
            }
            if (session.getAttribute("warehouse2") != null) {
                session.removeAttribute("warehouse2");
            }
            session.setAttribute("warehouse", warehouse);
            return mapping.findForward("editsave");

        }
        return mapping.findForward(forwardName);
    }
}
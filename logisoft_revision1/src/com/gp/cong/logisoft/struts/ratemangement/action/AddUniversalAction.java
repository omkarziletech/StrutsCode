package com.gp.cong.logisoft.struts.ratemangement.action;

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

import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cong.logisoft.beans.NoteBean;
import com.gp.cong.logisoft.domain.AuditLogRecord;
import com.gp.cong.logisoft.domain.AuditLogRecordLclCoRates;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.HistoryLogInterceptor;
import com.gp.cong.logisoft.domain.Item;
import com.gp.cong.logisoft.domain.UniversalMaster;
import com.gp.cong.logisoft.domain.UniverseAirFreight;
import com.gp.cong.logisoft.domain.UniverseCommodityChrg;
import com.gp.cong.logisoft.domain.UniverseFlatRate;
import com.gp.cong.logisoft.domain.UniverseInsuranceChrg;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.ItemDAO;
import com.gp.cong.logisoft.hibernate.dao.UniversalMasterDAO;
import com.gp.cong.logisoft.struts.ratemangement.form.AddUniversalForm;
import com.gp.cong.logisoft.util.DBUtil;

/** 
 * MyEclipse Struts
 * Creation date: 07-30-2008
 * 
 * XDoclet definition:
 * @struts.action path="/addUniversal" name="addUniversalForm" input="jsps/ratemanagement/addUniversal.jsp" scope="request"
 */
public class AddUniversalAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        AddUniversalForm addLclColoadForm = (AddUniversalForm) form;// TODO
        UniversalMaster universalMaster = new UniversalMaster();
        HttpSession session = ((HttpServletRequest) request).getSession();
        String buttonValue = addLclColoadForm.getButtonValue();
        String FORWARD = null;
        UniversalMasterDAO universalMasterDAO = new UniversalMasterDAO();
        List lclCoList = new ArrayList();
        String programid = null;
        String recordid = "";
        String message = "";
        List comm = new ArrayList();

        String forwardName = "";
        List noncomm = new ArrayList();
        HistoryLogInterceptor historyLogInterceptor = new HistoryLogInterceptor();
        List uniairList = new ArrayList();
        List cossList = new ArrayList();
        List flatRateList = new ArrayList();
        List insuranceList = new ArrayList();
        Set airFrightSet = new HashSet<UniverseAirFreight>();
        Set unicoSet = new HashSet<UniverseCommodityChrg>();
        Set flatRateSet = new HashSet<UniverseFlatRate>();
        Set insuranceSet = new HashSet<UniverseInsuranceChrg>();
        DBUtil dbUtil = new DBUtil();

        if (buttonValue != null && buttonValue.equals("edit")) {
            programid = (String) session.getAttribute("processinfoforuniversal");
            if (session.getAttribute("addUniversalMaster") != null) {
                universalMaster = (UniversalMaster) session.getAttribute("addUniversalMaster");
            }
            if (universalMaster.getId() != null) {
                recordid = universalMaster.getId().toString();
            }
            if (session.getAttribute("unicssList") != null) {
                cossList = (List) session.getAttribute("unicssList");
                for (int j = 0; j < cossList.size(); j++) {
                    UniverseCommodityChrg universeCommodityChrg = (UniverseCommodityChrg) cossList.get(j);
                    historyLogInterceptor.setSessionFactory(HibernateSessionFactory.getSessionFactory());
                    if (universeCommodityChrg.getId() != null) {
                        User userId = null;
                        if (session.getAttribute("loginuser") != null) {
                            userId = (User) session.getAttribute("loginuser");
                        }
                        historyLogInterceptor.setUserName(userId.getLoginName());
                        boolean flag = historyLogInterceptor.onFlushDirty(universeCommodityChrg, universeCommodityChrg.getId(), null, null, null, null);
                    }
                    unicoSet.add(universeCommodityChrg);

                }
                universalMaster.setUniversalCommodity(unicoSet);
            }
            if (session.getAttribute("uniarifrightlist") != null) {
                uniairList = (List) session.getAttribute("uniarifrightlist");
                for (int j = 0; j < uniairList.size(); j++) {
                    UniverseAirFreight universeAirFreight = (UniverseAirFreight) uniairList.get(j);
                    historyLogInterceptor.setSessionFactory(HibernateSessionFactory.getSessionFactory());
                    if (universeAirFreight.getId() != null) {
                        User userId = null;
                        if (session.getAttribute("loginuser") != null) {
                            userId = (User) session.getAttribute("loginuser");
                        }
                        historyLogInterceptor.setUserName(userId.getLoginName());
                        boolean flag = historyLogInterceptor.onFlushDirty(universeAirFreight, universeAirFreight.getId(), null, null, null, null);
                    }
                    airFrightSet.add(universeAirFreight);

                }
                universalMaster.setUniversalImport(airFrightSet);
            }

            //UNIVERSAL IMPORT CHARGES FOR FLATE RATE
            if (session.getAttribute("unifaltratelist") != null) {
                flatRateList = (List) session.getAttribute("unifaltratelist");
                for (int j = 0; j < flatRateList.size(); j++) {
                    UniverseFlatRate universeFlatRate = (UniverseFlatRate) flatRateList.get(j);
                    historyLogInterceptor.setSessionFactory(HibernateSessionFactory.getSessionFactory());
                    if (universeFlatRate.getId() != null) {
                        User userId = null;
                        if (session.getAttribute("loginuser") != null) {
                            userId = (User) session.getAttribute("loginuser");
                        }
                        historyLogInterceptor.setUserName(userId.getLoginName());
                        boolean flag = historyLogInterceptor.onFlushDirty(universeFlatRate, universeFlatRate.getId(), null, null, null, null);
                    }
                    flatRateSet.add(universeFlatRate);

                }
                universalMaster.setUniversalFlat(flatRateSet);
            }
            //UNIVERSAL IMPORT CHARGES FOR INSURANCE CHARGES
            if (session.getAttribute("uniinsurancelist") != null) {
                insuranceList = (List) session.getAttribute("uniinsurancelist");
                for (int j = 0; j < insuranceList.size(); j++) {
                    UniverseInsuranceChrg universeInsuranceChrg = (UniverseInsuranceChrg) insuranceList.get(j);
                    historyLogInterceptor.setSessionFactory(HibernateSessionFactory.getSessionFactory());
                    if (universeInsuranceChrg.getId() != null) {
                        User userId = null;
                        if (session.getAttribute("loginuser") != null) {
                            userId = (User) session.getAttribute("loginuser");
                        }
                        historyLogInterceptor.setUserName(userId.getLoginName());
                        boolean flag = historyLogInterceptor.onFlushDirty(universeInsuranceChrg, universeInsuranceChrg.getId(), null, null, null, null);
                    }
                    insuranceSet.add(universeInsuranceChrg);

                }
                universalMaster.setUniversalInsurance(insuranceSet);
            }
            UniversalMaster universalMasterObj = null;
            if (session.getAttribute("addUniversalMaster") != null) {
                universalMasterObj = (UniversalMaster) session.getAttribute("addUniversalMaster");
                universalMaster.setOriginTerminal(universalMasterObj.getOriginTerminal());
                universalMaster.setDestinationPort(universalMasterObj.getDestinationPort());
                universalMaster.setCommodityCode(universalMasterObj.getCommodityCode());
                User userId = null;
                GenericCode genObj = null;
                GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
                if (session.getAttribute("loginuser") != null) {
                    userId = (User) session.getAttribute("loginuser");
                }
                universalMasterDAO.update(universalMaster, userId.getLoginName());

                if (universalMaster.getId() != null) {

                    recordid = universalMaster.getId().toString();
                }
                if (programid != null) {
                    dbUtil.getProcessInfo(programid, recordid, "deleted", "edited");
                }
            }
            List newList = new ArrayList();
            if (universalMaster.getCommodityCode() != null) {
                newList = (List) session.getAttribute("uninoncommonList");
                for (int i = 0; i < newList.size(); i++) {
                    UniversalMaster aStandardCharges11 = (UniversalMaster) newList.get(i);//parent
                    if (aStandardCharges11.getCommodityCode() != null && universalMaster.getCommodityCode() != null &&
                            aStandardCharges11.getCommodityCode().getCode() != null && universalMaster.getCommodityCode().getCode() != null && aStandardCharges11.getCommodityCode().getCode().equals(universalMaster.getCommodityCode().getCode())) {
                        newList.set(i, universalMaster);
                    }

                }
                session.setAttribute("uninoncommonList", newList);
                session.setAttribute("universalUpdateRecords", universalMaster);

            }

            message = "Universal/Import charges details Edited successfully";

            session.setAttribute("searchuniMaster", universalMasterObj);
            session.setAttribute("unimessage", message);

            if (session.getAttribute("uniinsurancelist") != null) {
                session.removeAttribute("uniinsurancelist");

            }
            if (session.getAttribute("uniarifrightlist") != null) {
                session.removeAttribute("uniarifrightlist");

            }
            if (session.getAttribute("unifaltratelist") != null) {
                session.removeAttribute("unifaltratelist");

            }
            if (session.getAttribute("unicssList") != null) {
                session.removeAttribute("unicssList");

            }
            if (session.getAttribute("view") != null) {
                session.removeAttribute("view");
            }
            forwardName = "adduniversal";
        }

        if (buttonValue != null && buttonValue.equals("save")) {
            if (session.getAttribute("unicssList") != null) {
                cossList = (List) session.getAttribute("unicssList");
                for (int j = 0; j < cossList.size(); j++) {
                    UniverseCommodityChrg LclCssListd = (UniverseCommodityChrg) cossList.get(j);

                    unicoSet.add(LclCssListd);
                }
                universalMaster.setUniversalCommodity(unicoSet);
            }
            //UNIVERSAL IMPORT CHAGRE FOR AIR FRIGHT
            if (session.getAttribute("uniarifrightlist") != null) {
                uniairList = (List) session.getAttribute("uniarifrightlist");
                for (int j = 0; j < uniairList.size(); j++) {
                    UniverseAirFreight LclCssListd = (UniverseAirFreight) uniairList.get(j);
                    airFrightSet.add(LclCssListd);
                }
                universalMaster.setUniversalImport(airFrightSet);
            }

            //UNIVERSAL IMPORT CHARGES FOR FLATE RATE
            if (session.getAttribute("unifaltratelist") != null) {
                flatRateList = (List) session.getAttribute("unifaltratelist");
                for (int j = 0; j < flatRateList.size(); j++) {
                    UniverseFlatRate LclCssListd = (UniverseFlatRate) flatRateList.get(j);
                    historyLogInterceptor.setSessionFactory(HibernateSessionFactory.getSessionFactory());
                    flatRateSet.add(LclCssListd);

                }
                universalMaster.setUniversalFlat(flatRateSet);
            }
            //UNIVER SAL IMPORT CHARGES FOR INSURANCE CHARGES
            if (session.getAttribute("uniinsurancelist") != null) {
                insuranceList = (List) session.getAttribute("uniinsurancelist");
                for (int j = 0; j < insuranceList.size(); j++) {
                    UniverseInsuranceChrg LclCssListd = (UniverseInsuranceChrg) insuranceList.get(j);
                    insuranceSet.add(LclCssListd);

                }
            }
            universalMaster.setUniversalInsurance(insuranceSet);
            if (session.getAttribute("addUniversalMaster") != null) {
                UniversalMaster universalMasterObj = (UniversalMaster) session.getAttribute("addUniversalMaster");
                universalMaster.setOriginTerminal(universalMasterObj.getOriginTerminal());
                universalMaster.setDestinationPort(universalMasterObj.getDestinationPort());
                universalMaster.setCommodityCode(universalMasterObj.getCommodityCode());
                User userId = null;
                if (session.getAttribute("loginuser") != null) {
                    userId = (User) session.getAttribute("loginuser");
                }
                GenericCode genObj = null;
                GenericCodeDAO genericCodeDAO = new GenericCodeDAO();

                //-----SAVE METHOD------
                universalMasterDAO.save(universalMaster, userId.getLoginName());
                message = "Universal/Import charges details Save successfully";
                session.setAttribute("unimessage", message);
                List newList = null;
                if (session.getAttribute("uninoncommonList") != null) {
                    newList = (List) session.getAttribute("uninoncommonList");

                } else {

                    newList = new ArrayList();
                }
                newList.add(universalMaster);
                session.setAttribute("newList", newList);

                if (session.getAttribute("uniinsurancelist") != null) {
                    session.removeAttribute("uniinsurancelist");

                }
                if (session.getAttribute("uniarifrightlist") != null) {
                    session.removeAttribute("uniarifrightlist");

                }
                if (session.getAttribute("unifaltratelist") != null) {
                    session.removeAttribute("unifaltratelist");

                }
                if (session.getAttribute("unicssList") != null) {
                    session.removeAttribute("unicssList");

                }
                if (session.getAttribute("unibverdefaultRate") != null) {
                    session.removeAttribute("unibverdefaultRate");

                }
            }
            forwardName = "adduniversal";
        }// ------------------------------------------------------------------------------------------
        else if (buttonValue != null && buttonValue.equals("delete")) {

            programid = (String) session.getAttribute("processinfoforuniversal");
            User userId = null;

            if (session.getAttribute("loginuser") != null) {
                userId = (User) session.getAttribute("loginuser");
            }
            UniversalMaster universalMasterObj = null;

            if (session.getAttribute("addUniversalMaster") != null) {
                universalMasterObj = (UniversalMaster) session.getAttribute("addUniversalMaster");
                if (universalMasterObj.getId() != null) {

                    recordid = universalMasterObj.getId().toString();
                }
                universalMasterDAO.delete(universalMasterObj, userId.getLoginName());



            }
            List newList = new ArrayList();
            if (universalMasterObj.getCommodityCode() != null) {
                newList = (List) session.getAttribute("uninoncommonList");
                for (int i = 0; i < newList.size(); i++) {
                    UniversalMaster aStandardCharges11 = (UniversalMaster) newList.get(i);//parent
                    if (aStandardCharges11.getCommodityCode() != null && universalMasterObj.getCommodityCode() != null &&
                            aStandardCharges11.getCommodityCode().getCode() != null && universalMasterObj.getCommodityCode().getCode() != null && aStandardCharges11.getCommodityCode().getCode().equals(universalMasterObj.getCommodityCode().getCode())) {
                        newList.remove(i);
                    }

                }
                session.setAttribute("uninoncommonList", newList);


            }


            if (programid != null) {
                dbUtil.getProcessInfo(programid, recordid, "deleted", "edited");
            }

            message = "Universal/Import charges details Delete successfully";
            session.setAttribute("unimessage", message);
            forwardName = "adduniversal";
            if (session.getAttribute("view") != null) {
                session.removeAttribute("view");
            }
            if (session.getAttribute("uniinsurancelist") != null) {
                session.removeAttribute("uniinsurancelist");

            }
            if (session.getAttribute("uniarifrightlist") != null) {
                session.removeAttribute("uniarifrightlist");

            }
            if (session.getAttribute("unifaltratelist") != null) {
                session.removeAttribute("unifaltratelist");

            }
            if (session.getAttribute("unicssList") != null) {
                session.removeAttribute("unicssList");

            }
            if (session.getAttribute("unibverdefaultRate") != null) {
                session.removeAttribute("unibverdefaultRate");

            }

        } // ---------------------------------------------------------------------------------------------------
        else if (buttonValue != null && buttonValue.equals("note")) {
            if (session.getAttribute("addUniversalMaster") != null) {
                universalMaster = (UniversalMaster) session.getAttribute("addUniversalMaster");
            }

            ItemDAO itemDAO = new ItemDAO();
            Item item = new Item();
            String itemName = "";
            if (session.getAttribute("processinfoforuniversal") != null) {
                String itemId = (String) session.getAttribute("processinfoforuniversal");
                item = itemDAO.findById(Integer.parseInt(itemId));
                itemName = item.getItemDesc();

            }


            AuditLogRecord auditLogRecord = new AuditLogRecordLclCoRates();
            NoteBean noteBean = new NoteBean();
            noteBean.setItemName(itemName);
            noteBean.setAuditLogRecord(auditLogRecord);
            noteBean.setButtonValue(buttonValue);
            // noteBean.setUser(user);
            noteBean.setPageName("cancelUniversalcodetails");
            String noteId = "";

            if (universalMaster.getId() != null) {
                noteId = universalMaster.getId().toString();

                noteBean.setNoteId(noteId);
                noteBean.setReferenceId(noteId);
            }

            List auditList = null;

            auditList = dbUtil.getNoteInformation(noteId, auditLogRecord);
            noteBean.setAuditList(auditList);
            noteBean.setVoidednote("");
            request.setAttribute("noteBean", noteBean);
            String documentName = "User";
            request.setAttribute("buttonValue", buttonValue);
            // session.setAttribute("document",documentName);

            forwardName = "note";
        } else if (buttonValue != null && buttonValue.equals("savecancel")) {
            programid = (String) session.getAttribute("processinfoforuniversal");

            if (session.getAttribute("addUniversalMaster") != null) {
                universalMaster = (UniversalMaster) session.getAttribute("addUniversalMaster");

                if (universalMaster.getId() != null) {

                    recordid = universalMaster.getId().toString();
                }
            }
            if (programid != null) {
                dbUtil.getProcessInfo(programid, recordid, "deleted", "edited");
            }

            if (session.getAttribute("uniinsurancelist") != null) {
                session.removeAttribute("uniinsurancelist");

            }
            if (session.getAttribute("unimessage") != null) {
                session.removeAttribute("unimessage");
            }
            if (session.getAttribute("uniarifrightlist") != null) {
                session.removeAttribute("uniarifrightlist");

            }
            if (session.getAttribute("unifaltratelist") != null) {
                session.removeAttribute("unifaltratelist");

            }
            if (session.getAttribute("unicssList") != null) {
                session.removeAttribute("unicssList");

            }
            if (session.getAttribute("unibverdefaultRate") != null) {
                session.removeAttribute("unibverdefaultRate");

            }

            forwardName = "adduniversal";
        } else if (buttonValue != null && buttonValue.equals("cancelview")) {

            programid = (String) session.getAttribute("processinfoforuniversal");

            if (session.getAttribute("addUniversalMaster") != null) {
                universalMaster = (UniversalMaster) session.getAttribute("addUniversalMaster");

                if (universalMaster.getId() != null) {

                    recordid = universalMaster.getId().toString();
                }
            }
            if (programid != null) {
                dbUtil.getProcessInfo(programid, recordid, "deleted", "edited");
            }

            if (session.getAttribute("uniinsurancelist") != null) {
                session.removeAttribute("uniinsurancelist");

            }
            if (session.getAttribute("unimessage") != null) {
                session.removeAttribute("unimessage");
            }
            if (session.getAttribute("uniarifrightlist") != null) {
                session.removeAttribute("uniarifrightlist");

            }
            if (session.getAttribute("unifaltratelist") != null) {
                session.removeAttribute("unifaltratelist");

            }
            if (session.getAttribute("unicssList") != null) {
                session.removeAttribute("unicssList");

            }
            if (session.getAttribute("unibverdefaultRate") != null) {
                session.removeAttribute("unibverdefaultRate");

            }

            forwardName = "adduniversal";

        } else if (buttonValue != null && buttonValue.equals("cancel")) {
            programid = (String) session.getAttribute("processinfoforuniversal");

            if (session.getAttribute("addUniversalMaster") != null) {
                universalMaster = (UniversalMaster) session.getAttribute("addUniversalMaster");

                if (universalMaster.getId() != null) {

                    recordid = universalMaster.getId().toString();
                }
            }
            if (programid != null) {
                dbUtil.getProcessInfo(programid, recordid, "deleted", "edited");
            }

            if (session.getAttribute("uniinsurancelist") != null) {
                session.removeAttribute("uniinsurancelist");

            }
            if (session.getAttribute("unimessage") != null) {
                session.removeAttribute("unimessage");
            }
            if (session.getAttribute("uniarifrightlist") != null) {
                session.removeAttribute("uniarifrightlist");

            }
            if (session.getAttribute("unifaltratelist") != null) {
                session.removeAttribute("unifaltratelist");

            }
            if (session.getAttribute("unicssList") != null) {
                session.removeAttribute("unicssList");

            }
            if (session.getAttribute("unibverdefaultRate") != null) {
                session.removeAttribute("unibverdefaultRate");

            }

            forwardName = "adduniversal";

        }

        if (session.getAttribute("editrecord") != null) {

            session.removeAttribute("editrecord");
        }
        if (session.getAttribute("uniCssAdd") != null) {

            session.removeAttribute("uniCssAdd");
        }
        if (session.getAttribute("universalFalteAdd") != null) {

            session.removeAttribute("universalFalteAdd");
        }
        if (session.getAttribute("uniInsuranceAdd") != null) {

            session.removeAttribute("uniInsuranceAdd");
        }
        if (session.getAttribute("univerAriFreight") != null) {

            session.removeAttribute("univerAriFreight");
        }

        session.setAttribute("trade", "universal");

        return mapping.findForward(forwardName);
    }
}

package com.logiware.utils;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.Notes;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.NotesDAO;
import java.util.Date;

/**
 * @description AuditNotesUtils
 * @author LakshmiNarayanan
 */
public class AuditNotesUtils {

    /**
     * @description Insert Audit Notes
     * @param oldValue
     * @param newValue
     * @param itemName
     * @param moduleId
     * @param moduleRefId
     * @param user
     */
    public static void insertAuditNotes(String oldValue, String newValue, String moduleId, String moduleRefId, String itemName, User user) throws Exception {
	if (CommonUtils.isNotEqualIgnoreCase(newValue, oldValue)) {
	    String desc = setNotesDescription(itemName, oldValue, newValue, user);
	    if (CommonUtils.isNotEmpty(desc)) {
		Notes notes = new Notes();
		notes.setModuleId(moduleId);
		notes.setModuleRefId(moduleRefId);
		notes.setNoteDesc(desc);
		notes.setNoteType(NotesConstants.AUTO);
		notes.setItemName(itemName);
		notes.setUpdateDate(new Date());
		notes.setUpdatedBy(null != user ? user.getLoginName() : "System");
		if (null != user) {
		    new NotesDAO().save(notes);
		} else {
		    new BaseHibernateDAO().getCurrentSession().save(notes);
		}
	    }
	}
    }

    public static void insertAuditNotes(String description, String oldValue, String newValue, String moduleId, String moduleRefId, String itemName, User user) throws Exception {
	if (CommonUtils.isNotEqualIgnoreCase(newValue, oldValue)) {
	    String desc = setNotesDescription(itemName, oldValue, newValue, user);
	    if (CommonUtils.isNotEmpty(desc)) {
		Notes notes = new Notes();
		notes.setModuleId(moduleId);
		notes.setModuleRefId(moduleRefId);
		notes.setNoteDesc((null != description ? (description + " ") : "") + desc);
		notes.setNoteType(NotesConstants.AUTO);
		notes.setItemName(itemName);
		notes.setUpdateDate(new Date());
		notes.setUpdatedBy(null != user ? user.getLoginName() : "System");
		if (null != user) {
		    new NotesDAO().save(notes);
		} else {
		    new BaseHibernateDAO().getCurrentSession().save(notes);
		}
	    }
	}
    }

    public static void insertAuditNotes(String description, String moduleId, String moduleRefId, String itemName, User user) throws Exception {
	NotesDAO notesDAO = new NotesDAO();
	Notes notes = new Notes();
	notes.setModuleId(moduleId);
	notes.setModuleRefId(moduleRefId);
	notes.setNoteDesc(description);
	notes.setNoteType(NotesConstants.AUTO);
	notes.setItemName(itemName);
	notes.setUpdateDate(new Date());
	notes.setUpdatedBy(null != user ? user.getLoginName() : "System");
        notesDAO.save(notes);
    }

    public static void insertAuditNotes(String description, String moduleId, String moduleRefId, String itemName, Date date, User user) throws Exception {
	Notes notes = new Notes();
	notes.setModuleId(moduleId);
	notes.setModuleRefId(moduleRefId);
	notes.setNoteDesc(description);
	notes.setNoteType(NotesConstants.AUTO);
	notes.setItemName(itemName);
	notes.setUpdateDate(date);
	notes.setUpdatedBy(null != user ? user.getLoginName() : "System");
	if (null != user) {
	    new NotesDAO().save(notes);
	} else {
	    new BaseHibernateDAO().getCurrentSession().save(notes);
	}
    }

    private static String setNotesDescription(String itemName, String oldValue, String newValue, User user) throws Exception {
	StringBuilder notesDesc = new StringBuilder(itemName);
	if (CommonUtils.isNotEmpty(oldValue)) {
	    if (CommonUtils.isNotEmpty(newValue)) {
		notesDesc.append(" changed from '").append(oldValue.trim()).append("' to '").append(newValue.trim()).append("' by ");
	    } else {
		notesDesc.append(" '").append(oldValue.trim()).append("' removed by ");
	    }
	} else if (CommonUtils.isNotEmpty(newValue)) {
	    notesDesc.append(" '").append(newValue.trim()).append("' added by ");
	} else {
	    return null;
	}
	notesDesc.append(null != user ? user.getLoginName() : "System");
	notesDesc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
	return notesDesc.toString();
    }
}

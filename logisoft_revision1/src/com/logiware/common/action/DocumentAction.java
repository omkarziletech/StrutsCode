package com.logiware.common.action;

import com.logiware.common.dao.DocumentsDAO;
import com.logiware.common.form.DocumentForm;
import com.logiware.common.model.ResultModel;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Lakshmi Narayanan
 */
public class DocumentAction extends BaseAction {

    public ActionForward showDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	DocumentForm documentForm = (DocumentForm) form;
	List<ResultModel> documents = new DocumentsDAO().getDocuments(documentForm.getDocumentId(), documentForm.getDocumentName(), documentForm.getScreenName());
	documentForm.setDocuments(documents);
	return mapping.findForward(SUCCESS);
    }
}

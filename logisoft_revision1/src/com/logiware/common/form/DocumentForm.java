package com.logiware.common.form;

import com.logiware.common.model.ResultModel;
import java.util.List;

/**
 *
 * @author Lakshmi Narayanan
 */
public class DocumentForm extends BaseForm {

    private String documentId;
    private String documentName;
    private String screenName;
    private List<ResultModel> documents;

    public String getDocumentId() {
	return documentId;
    }

    public void setDocumentId(String documentId) {
	this.documentId = documentId;
    }

    public String getDocumentName() {
	return documentName;
    }

    public void setDocumentName(String documentName) {
	this.documentName = documentName;
    }

    public String getScreenName() {
	return screenName;
    }

    public void setScreenName(String screenName) {
	this.screenName = screenName;
    }

    public List<ResultModel> getDocuments() {
	return documents;
    }

    public void setDocuments(List<ResultModel> documents) {
	this.documents = documents;
    }
}

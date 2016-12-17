package com.logiware.common.form;

import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.ScanConfig;
import com.gp.cong.logisoft.hibernate.dao.CodetypeDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.logiware.common.model.ResultModel;
import java.util.List;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author Lucky
 */
public class ScanForm extends BaseForm {

    private static final long serialVersionUID = 1L;

    private String documentId;
    private String screenName;
    private String documentName;
    private String ignoreDocumentName;
    private String comment;
    private String ssMasterBl;
    private String status;
    private Integer id;
    private ScanConfig scanConfig;
    private FormFile document;
    private List<ResultModel> results;
    private List<ResultModel> results1;
    private List<ResultModel> results2;
    private List<ResultModel> results3;

    private List<ResultModel> importTransResults;
    private List<ResultModel> importTransResults1;
    private List<ResultModel> importTransResults2;
    private List<ResultModel> importTransResults3;
    private String bookingType;
    private String hiddenScreenName;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getIgnoreDocumentName() {
        return ignoreDocumentName;
    }

    public void setIgnoreDocumentName(String ignoreDocumentName) {
        this.ignoreDocumentName = ignoreDocumentName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSsMasterBl() {
        return ssMasterBl;
    }

    public void setSsMasterBl(String ssMasterBl) {
        this.ssMasterBl = ssMasterBl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ScanConfig getScanConfig() {
        if (null == scanConfig) {
            scanConfig = new ScanConfig();
        }
        return scanConfig;
    }

    public void setScanConfig(ScanConfig scanConfig) {
        this.scanConfig = scanConfig;
    }

    public FormFile getDocument() {
        return document;
    }

    public void setDocument(FormFile document) {
        this.document = document;
    }

    public List<ResultModel> getResults() {
        return results;
    }

    public void setResults(List<ResultModel> results) {
        this.results = results;
    }

    public List<ResultModel> getResults1() {
        return results1;
    }

    public void setResults1(List<ResultModel> results1) {
        this.results1 = results1;
    }

    public List<ResultModel> getResults2() {
        return results2;
    }

    public void setResults2(List<ResultModel> results2) {
        this.results2 = results2;
    }

    public List<ResultModel> getResults3() {
        return results3;
    }

    public void setResults3(List<ResultModel> results3) {
        this.results3 = results3;
    }

    public List<GenericCode> getScreenNames() throws Exception {
        Integer codeTypeId = new CodetypeDAO().getCodeTypeId("Screen Name");
        return new GenericCodeDAO().findByCodeTypeid(codeTypeId);
    }

    public List<GenericCode> getDocumentTypes() throws Exception {
        Integer codeTypeId = new CodetypeDAO().getCodeTypeId("Document Type");
        return new GenericCodeDAO().findByCodeTypeid(codeTypeId);
    }

    public List<ResultModel> getImportTransResults() {
        return importTransResults;
    }

    public void setImportTransResults(List<ResultModel> importTransResults) {
        this.importTransResults = importTransResults;
    }

    public List<ResultModel> getImportTransResults1() {
        return importTransResults1;
    }

    public void setImportTransResults1(List<ResultModel> importTransResults1) {
        this.importTransResults1 = importTransResults1;
    }

    public List<ResultModel> getImportTransResults2() {
        return importTransResults2;
    }

    public void setImportTransResults2(List<ResultModel> importTransResults2) {
        this.importTransResults2 = importTransResults2;
    }

    public List<ResultModel> getImportTransResults3() {
        return importTransResults3;
    }

    public void setImportTransResults3(List<ResultModel> importTransResults3) {
        this.importTransResults3 = importTransResults3;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public String getHiddenScreenName() {
        return hiddenScreenName;
    }

    public void setHiddenScreenName(String hiddenScreenName) {
        this.hiddenScreenName = hiddenScreenName;
    }
    
 
}

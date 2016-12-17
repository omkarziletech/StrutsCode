package com.logiware.referencedata.form;

import com.gp.cong.logisoft.domain.Codetype;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.Genericcodelabels;
import com.gp.cong.logisoft.hibernate.dao.CodetypeDAO;
import java.util.List;

/**
 *
 * @author Lakshmi Narayanan
 */
public class GenericCodeForm extends BaseForm {

    private GenericCode genericCode;
    private List<GenericCode> genericCodes;
    private List<String> labels;
    private List<Genericcodelabels> genericCodeLabels;
    private Codetype codeType;

    public GenericCode getGenericCode() {
        if (null == genericCode) {
            genericCode = new GenericCode();
        }
        return genericCode;
    }

    public void setGenericCode(GenericCode genericCode) {
        this.genericCode = genericCode;
    }

    public List<GenericCode> getGenericCodes() {
        return genericCodes;
    }

    public void setGenericCodes(List<GenericCode> genericCodes) {
        this.genericCodes = genericCodes;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<Codetype> getCodeTypes() {
        return new CodetypeDAO().getCodeTypes();
    }

    public List<Genericcodelabels> getGenericCodeLabels() {
        return genericCodeLabels;
    }

    public void setGenericCodeLabels(List<Genericcodelabels> genericCodeLabels) {
        this.genericCodeLabels = genericCodeLabels;
    }

    public Codetype getCodeType() {
        return codeType;
    }

    public void setCodeType(Codetype codeType) {
        this.codeType = codeType;
    }

}

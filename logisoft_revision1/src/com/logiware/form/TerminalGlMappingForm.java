package com.logiware.form;

import com.gp.cong.common.CommonUtils;
import com.logiware.hibernate.domain.TerminalGlMapping;
import com.logiware.utils.ListUtils;
import java.util.List;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author Lakshmi Narayanan
 */
public class TerminalGlMappingForm extends org.apache.struts.action.ActionForm {

    private TerminalGlMapping terminalGlMapping;
    private List<TerminalGlMapping> terminalGlMappings;
    private String action;
    private FormFile terminalGlMappingSheet;
    private String message;
    private Integer terminal;


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public TerminalGlMapping getTerminalGlMapping() {
        if (null == terminalGlMapping) {
            terminalGlMapping = new TerminalGlMapping();
        }
        return terminalGlMapping;
    }

    public void setTerminalGlMapping(TerminalGlMapping terminalGlMapping) {
        this.terminalGlMapping = terminalGlMapping;
    }

    public List<TerminalGlMapping> getTerminalGlMappings()throws Exception {
        if (CommonUtils.isEmpty(terminalGlMappings)) {
            this.setTerminalGlMappings(ListUtils.lazyList(TerminalGlMapping.class));
        }
        return terminalGlMappings;
    }

    public void setTerminalGlMappings(List<TerminalGlMapping> terminalGlMappings) {
        this.terminalGlMappings = terminalGlMappings;
    }

    public FormFile getTerminalGlMappingSheet() {
        return terminalGlMappingSheet;
    }

    public void setTerminalGlMappingSheet(FormFile terminalGlMappingSheet) {
        this.terminalGlMappingSheet = terminalGlMappingSheet;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getTerminal() {
        return terminal;
    }

    public void setTerminal(Integer terminal) {
        this.terminal = terminal;
    }
}

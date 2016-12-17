/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.common.form;

import com.logiware.common.dao.PropertyDAO;
import java.util.List;
import com.logiware.common.domain.Property;
/**
 *
 * @author Balaji.E
 */
public class PropertyForm  extends BaseForm {
    private Long id;
    private String name;
    private String value;
    private String type;
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public List<Property> getCommonProperties() {
	return new PropertyDAO().getCommonProperties();
    }
    public List<Property> getDomesticProperties() {
	return new PropertyDAO().getDomesticProperties();
    }
    public List<Property> getAccountingProperties() {
	return new PropertyDAO().getAccountingProperties();
    }
    public List<Property> getFclProperties() {
	return new PropertyDAO().getFclProperties();
    }
    public List<Property> getLclProperties() {
	return new PropertyDAO().getLclProperties();
    }
}

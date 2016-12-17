/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.UnLocation;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author Wsadmin
 */
@Entity
@Table(name = "template_order")
@DynamicUpdate(true)
@DynamicInsert(true)
public class TemplateOrder extends Domain {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "template_key")
    private String templateKey;
    @Basic(optional = false)
    @Column(name = "sorted_order")
    private int sortedOrder;
    @JoinColumn(name = "template_id", referencedColumnName = "id")
    @ManyToOne
    private LclSearchTemplate lclSearchTemplate;
    
    public TemplateOrder() {
    }

    public TemplateOrder(Integer id) {
        this.id = id;
    }

    public TemplateOrder(Integer id, String templateKey, int sortedOrder) {
        this.id = id;
        this.templateKey = templateKey;
        this.sortedOrder = sortedOrder;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTemplateKey() {
        return templateKey;
    }

    public void setTemplateKey(String templateKey) {
        this.templateKey = templateKey;
    }

    public int getSortedOrder() {
        return sortedOrder;
    }

    public void setSortedOrder(int sortedOrder) {
        this.sortedOrder = sortedOrder;
    }

    public LclSearchTemplate getLclSearchTemplate() {
        return lclSearchTemplate;
    }

    public void setLclSearchTemplate(LclSearchTemplate lclSearchTemplate) {
        this.lclSearchTemplate = lclSearchTemplate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TemplateOrder)) {
            return false;
        }
        TemplateOrder other = (TemplateOrder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.ExportSearchTemplate[ id=" + id + " ]";
    }
    
}

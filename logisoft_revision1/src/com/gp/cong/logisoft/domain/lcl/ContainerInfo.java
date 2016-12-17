/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.Warehouse;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author Rajesh
 */
@Entity
@Table(name = "container_info")
@DynamicInsert(true)
@DynamicUpdate(true)
@NamedQueries({
    @NamedQuery(name = "ContainerInfo.findAll", query = "SELECT c FROM ContainerInfo c"),
    @NamedQuery(name = "ContainerInfo.findById", query = "SELECT c FROM ContainerInfo c WHERE c.id = :id"),
    @NamedQuery(name = "ContainerInfo.findByCntrName", query = "SELECT c FROM ContainerInfo c WHERE c.cntrName = :cntrName"),
    @NamedQuery(name = "ContainerInfo.findBySealNo", query = "SELECT c FROM ContainerInfo c WHERE c.sealNo = :sealNo"),
    @NamedQuery(name = "ContainerInfo.findByCntrType", query = "SELECT c FROM ContainerInfo c WHERE c.cntrType = :cntrType"),
    @NamedQuery(name = "ContainerInfo.findByShipType", query = "SELECT c FROM ContainerInfo c WHERE c.shipType = :shipType"),
    @NamedQuery(name = "ContainerInfo.findByAgentRef", query = "SELECT c FROM ContainerInfo c WHERE c.agentRef = :agentRef"),
    @NamedQuery(name = "ContainerInfo.findByAgentName", query = "SELECT c FROM ContainerInfo c WHERE c.agentName = :agentName"),
    @NamedQuery(name = "ContainerInfo.findByMasterBl", query = "SELECT c FROM ContainerInfo c WHERE c.masterBl = :masterBl"),
    @NamedQuery(name = "ContainerInfo.findByMasterBlBody", query = "SELECT c FROM ContainerInfo c WHERE c.masterBlBody = :masterBlBody")})
public class ContainerInfo extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "cntr_name")
    private String cntrName;
    @Column(name = "seal_no")
    private String sealNo;
    @Column(name = "cntr_type")
    private String cntrType;
    @Column(name = "ship_type")
    private String shipType;
    @Column(name = "agent_ref")
    private String agentRef;
    @Column(name = "agent_name")
    private String agentName;
    @Column(name = "shipping_line")
    private String shippingLine;
    @Lob
    @Column(name = "cntr_remarks")
    private String cntrRemarks;
    @Column(name = "master_bl")
    private String masterBl;
    @Column(name = "master_bl_body")
    private String masterBlBody;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "containerInfo")
    private List<BlInfo> blInfoCollection;
    @JoinColumn(name = "eculine_edi_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EculineEdi eculineEdi;
    @JoinColumn(name = "agent_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner agent;
    @JoinColumn(name = "warehouse_id", referencedColumnName = "id")
    @ManyToOne
    private Warehouse warehouse;
    @Column(name = "warehouse_address")
    private String warehouseAddress;
    @Column(name = "updated_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @JoinColumn(name = "updated_by", referencedColumnName = "user_id")
    @ManyToOne
    private User updatedByUser;
    @Column(name = "created_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @JoinColumn(name = "created_by", referencedColumnName = "user_id")
    @ManyToOne
    private User createdByUser;

    public ContainerInfo() {
    }

    public ContainerInfo(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCntrName() {
        return cntrName;
    }

    public void setCntrName(String cntrName) {
        this.cntrName = cntrName;
    }

    public String getSealNo() {
        return sealNo;
    }

    public void setSealNo(String sealNo) {
        this.sealNo = sealNo;
    }

    public String getCntrType() {
        return cntrType;
    }

    public void setCntrType(String cntrType) {
        this.cntrType = cntrType;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public String getAgentRef() {
        return agentRef;
    }

    public void setAgentRef(String agentRef) {
        this.agentRef = agentRef;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getCntrRemarks() {
        return cntrRemarks;
    }

    public void setCntrRemarks(String cntrRemarks) {
        this.cntrRemarks = cntrRemarks;
    }

    public String getMasterBl() {
        return masterBl;
    }

    public void setMasterBl(String masterBl) {
        this.masterBl = masterBl;
    }

    public String getMasterBlBody() {
        return masterBlBody;
    }

    public void setMasterBlBody(String masterBlBody) {
        this.masterBlBody = masterBlBody;
    }

    public List<BlInfo> getBlInfoCollection() {
        return blInfoCollection;
    }

    public void setBlInfoCollection(List<BlInfo> blInfoCollection) {
        this.blInfoCollection = blInfoCollection;
    }

    public EculineEdi getEculineEdi() {
        return eculineEdi;
    }

    public void setEculineEdi(EculineEdi eculineEdi) {
        this.eculineEdi = eculineEdi;
    }

    public String getShippingLine() {
        return shippingLine;
    }

    public void setShippingLine(String shippingLine) {
        this.shippingLine = shippingLine;
    }

    public TradingPartner getAgent() {
        return agent;
    }

    public void setAgent(TradingPartner agent) {
        this.agent = agent;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public User getUpdatedByUser() {
        return updatedByUser;
    }

    public void setUpdatedByUser(User updatedByUser) {
        this.updatedByUser = updatedByUser;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public User getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(User createdByUser) {
        this.createdByUser = createdByUser;
    }

    public String getWarehouseAddress() {
        return warehouseAddress;
    }

    public void setWarehouseAddress(String warehouseAddress) {
        this.warehouseAddress = warehouseAddress;
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
        if (!(object instanceof ContainerInfo)) {
            return false;
        }
        ContainerInfo other = (ContainerInfo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ecuedi.model.ContainerInfo[id=" + id + "]";
    }
}

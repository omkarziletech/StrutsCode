package com.gp.cong.logisoft.domain;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.struts.form.TradingPartnerForm;
import java.io.Serializable;
import java.util.Date;

public class CustomerContact extends Domain implements Auditable, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String accountNo;
    private String firstName;
    private String lastName;
    private String position;
    private String phone;
    private String extension;
    private String fax;
    private String email;
    private String comment;
    private GenericCode codea;
    private GenericCode codeb;
    private GenericCode codec;
    private GenericCode coded;
    private GenericCode codee;
    private GenericCode codef;
    private GenericCode codeg;
    private GenericCode codeh;
    private GenericCode codei;
    private GenericCode codej;
    private GenericCode codek;
    private Integer index;
    private String updateBy;
    private Date createdDate;
    private Date updatedDate;
    // display purpose
    private String accountName;
    private String accountType;
    private String subType;
    private boolean accountingSelected;
    private boolean applicableToAllShipments;
    private boolean onlyWhenBookingContact =true;
    private boolean nonNegRatedPosting =true;
    private boolean nonNegRatedManifest;
    private boolean nonNegRatedCob =true;
    private boolean nonNegRatedChanges;
    private boolean nonNegUnratedPosting;
    private boolean nonNegUnratedManifest;
    private boolean nonNegUnratedCob;
    private boolean nonNegUnratedChanges;
    private boolean freightInvoiceManifest;
    private boolean freightInvoiceCob =true;
    private boolean confirmOnBoardCob;
    private boolean obkgToAny =true;
    private boolean runvToRcvd =true;
    private boolean any;
    private boolean lclExports;
    private boolean lclImports;
    private boolean fclExports;
    private boolean fclImports;
    private boolean onlyWhenBookingContactCodeK =true;
    private boolean applicableToAllShipmentsCodeK;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public CustomerContact() {
    }

    public CustomerContact(TradingPartnerForm tradingPartnerForm) {

        accountNo = tradingPartnerForm.getAccountNo();
        firstName = tradingPartnerForm.getFirstName();
        lastName = tradingPartnerForm.getLastName();
        position = tradingPartnerForm.getPosition();
        phone = tradingPartnerForm.getPhone();
        extension = tradingPartnerForm.getExtension();
        fax = tradingPartnerForm.getFax();
        email = tradingPartnerForm.getEmail();
        comment = tradingPartnerForm.getComment();

    }

    public TradingPartnerForm setValuesToForm(CustomerContact customerContact, TradingPartnerForm tradingPartnerForm) {
        if (customerContact.getCodea() != null) {
            tradingPartnerForm.setCodea(customerContact.getCodea().getCode());
        }
        if (customerContact.getCodeb() != null) {
            tradingPartnerForm.setCodeb(customerContact.getCodeb().getCode());
        }
        if (customerContact.getCodec() != null) {
            tradingPartnerForm.setCodec(customerContact.getCodec().getCode());
        }
        if (customerContact.getCoded() != null) {
            tradingPartnerForm.setCoded(customerContact.getCoded().getCode());
        }
        if (customerContact.getCodee() != null) {
            tradingPartnerForm.setCodee(customerContact.getCodee().getCode());
        }
        if (customerContact.getCodef() != null) {
            tradingPartnerForm.setCodef(customerContact.getCodef().getCode());
        }
        if (customerContact.getCodeg() != null) {
            tradingPartnerForm.setCodeg(customerContact.getCodeg().getCode());
        }
        if (customerContact.getCodeh() != null) {
            tradingPartnerForm.setCodeh(customerContact.getCodeh().getCode());
        }
        if (customerContact.getCodei() != null) {
            tradingPartnerForm.setCodei(customerContact.getCodei().getCode());
        }
        if (customerContact.getCodej() != null) {
            tradingPartnerForm.setCodej(customerContact.getCodej().getCode());
        }
        if (customerContact.getCodek() != null) {
            tradingPartnerForm.setCodek(customerContact.getCodek().getCode());
        }
        tradingPartnerForm.setCompanyName(customerContact.getFirstName());
        if (null != tradingPartnerForm.getCompanyName() && null != customerContact.getLastName() && !customerContact.getLastName().trim().equals("")) {
            tradingPartnerForm.setCompanyName(tradingPartnerForm.getCompanyName() + " " + customerContact.getLastName());
        }
        tradingPartnerForm.setFirstName(customerContact.getFirstName());
        tradingPartnerForm.setLastName(customerContact.getLastName());
        tradingPartnerForm.setPosition(customerContact.getPosition());
        tradingPartnerForm.setPhone(customerContact.getPhone());
        tradingPartnerForm.setExtension(customerContact.getExtension());
        tradingPartnerForm.setFax(customerContact.getFax());
        tradingPartnerForm.setEmail(customerContact.getEmail());
        tradingPartnerForm.setComment(customerContact.getComment());
        tradingPartnerForm.setIndex(customerContact.getId().toString());
        
        tradingPartnerForm.setApplicableToAllShipments(customerContact.isApplicableToAllShipments());
        tradingPartnerForm.setOnlyWhenBookingContact(customerContact.isOnlyWhenBookingContact());
        // Non Neg Rated
        tradingPartnerForm.setNonNegRatedPosting(customerContact.isNonNegRatedPosting());
        tradingPartnerForm.setNonNegRatedManifest(customerContact.isNonNegRatedManifest());
        tradingPartnerForm.setNonNegRatedCob(customerContact.isNonNegRatedCob());
        tradingPartnerForm.setNonNegRatedChanges(customerContact.isNonNegRatedChanges());
        // Non Neg Unrated
        tradingPartnerForm.setNonNegUnratedPosting(customerContact.isNonNegUnratedPosting());
        tradingPartnerForm.setNonNegUnratedManifest(customerContact.isNonNegUnratedManifest());
        tradingPartnerForm.setNonNegUnratedCob(customerContact.isNonNegUnratedCob());
        tradingPartnerForm.setNonNegUnratedChanges(customerContact.isNonNegUnratedChanges());
        // Freight Invoice
        tradingPartnerForm.setFreightInvoiceManifest(customerContact.isFreightInvoiceManifest());
        tradingPartnerForm.setFreightInvoiceCob(customerContact.isFreightInvoiceCob());
        // Confirm On Board
        tradingPartnerForm.setConfirmOnBoardCob(customerContact.isConfirmOnBoardCob());
        // Dr From Codes 
        tradingPartnerForm.setObkgToAny(customerContact.isObkgToAny());
        tradingPartnerForm.setRunvToRcvd(customerContact.isRunvToRcvd());
        tradingPartnerForm.setAny(customerContact.isAny());
        tradingPartnerForm.setLclExports(customerContact.isLclExports());
        tradingPartnerForm.setLclImports(customerContact.isLclImports());
        tradingPartnerForm.setFclExports(customerContact.isFclExports());
        tradingPartnerForm.setFclImports(customerContact.isFclImports());
        tradingPartnerForm.setApplicableToAllShipmentsCodeK(customerContact.isApplicableToAllShipmentsCodeK());
        tradingPartnerForm.setOnlyWhenBookingContactCodeK(customerContact.isOnlyWhenBookingContactCodeK());
        return tradingPartnerForm;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getFirstName() {
        return firstName;
    }

    public GenericCode getCodeb() {
        return codeb;
    }

    public void setCodeb(GenericCode codeb) {
        this.codeb = codeb;
    }

    public GenericCode getCodec() {
        return codec;
    }

    public void setCodec(GenericCode codec) {
        this.codec = codec;
    }

    public GenericCode getCoded() {
        return coded;
    }

    public void setCoded(GenericCode coded) {
        this.coded = coded;
    }

    public GenericCode getCodee() {
        return codee;
    }

    public void setCodee(GenericCode codee) {
        this.codee = codee;
    }

    public GenericCode getCodef() {
        return codef;
    }

    public void setCodef(GenericCode codef) {
        this.codef = codef;
    }

    public GenericCode getCodeg() {
        return codeg;
    }

    public void setCodeg(GenericCode codeg) {
        this.codeg = codeg;
    }

    public GenericCode getCodeh() {
        return codeh;
    }

    public void setCodeh(GenericCode codeh) {
        this.codeh = codeh;
    }

    public GenericCode getCodei() {
        return codei;
    }

    public void setCodei(GenericCode codei) {
        this.codei = codei;
    }

    public GenericCode getCodej() {
        return codej;
    }

    public void setCodej(GenericCode codej) {
        this.codej = codej;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public GenericCode getCodea() {
        return codea;
    }

    public void setCodea(GenericCode codea) {
        this.codea = codea;
    }

    public AuditInfo getAuditInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getId1() {
        // TODO Auto-generated method stub
        return this.getId();
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public boolean isAccountingSelected() {
        return accountingSelected;
    }

    public void setAccountingSelected(boolean accountingSelected) {
        this.accountingSelected = accountingSelected;
    }

    public boolean isApplicableToAllShipments() {
        return applicableToAllShipments;
    }

    public void setApplicableToAllShipments(boolean applicableToAllShipments) {
        this.applicableToAllShipments = applicableToAllShipments;
    }

    public boolean isOnlyWhenBookingContact() {
        return onlyWhenBookingContact;
    }

    public void setOnlyWhenBookingContact(boolean onlyWhenBookingContact) {
        this.onlyWhenBookingContact = onlyWhenBookingContact;
    }

    public boolean isNonNegRatedPosting() {
        return nonNegRatedPosting;
    }

    public void setNonNegRatedPosting(boolean nonNegRatedPosting) {
        this.nonNegRatedPosting = nonNegRatedPosting;
    }

    public boolean isNonNegRatedManifest() {
        return nonNegRatedManifest;
    }

    public void setNonNegRatedManifest(boolean nonNegRatedManifest) {
        this.nonNegRatedManifest = nonNegRatedManifest;
    }

    public boolean isNonNegRatedCob() {
        return nonNegRatedCob;
    }

    public void setNonNegRatedCob(boolean nonNegRatedCob) {
        this.nonNegRatedCob = nonNegRatedCob;
    }

    public boolean isNonNegRatedChanges() {
        return nonNegRatedChanges;
    }

    public void setNonNegRatedChanges(boolean nonNegRatedChanges) {
        this.nonNegRatedChanges = nonNegRatedChanges;
    }

    public boolean isNonNegUnratedPosting() {
        return nonNegUnratedPosting;
    }

    public void setNonNegUnratedPosting(boolean nonNegUnratedPosting) {
        this.nonNegUnratedPosting = nonNegUnratedPosting;
    }

    public boolean isNonNegUnratedManifest() {
        return nonNegUnratedManifest;
    }

    public void setNonNegUnratedManifest(boolean nonNegUnratedManifest) {
        this.nonNegUnratedManifest = nonNegUnratedManifest;
    }

    public boolean isNonNegUnratedCob() {
        return nonNegUnratedCob;
    }

    public void setNonNegUnratedCob(boolean nonNegUnratedCob) {
        this.nonNegUnratedCob = nonNegUnratedCob;
    }

    public boolean isNonNegUnratedChanges() {
        return nonNegUnratedChanges;
    }

    public void setNonNegUnratedChanges(boolean nonNegUnratedChanges) {
        this.nonNegUnratedChanges = nonNegUnratedChanges;
    }

    public boolean isFreightInvoiceManifest() {
        return freightInvoiceManifest;
    }

    public void setFreightInvoiceManifest(boolean freightInvoiceManifest) {
        this.freightInvoiceManifest = freightInvoiceManifest;
    }

    public boolean isFreightInvoiceCob() {
        return freightInvoiceCob;
    }

    public void setFreightInvoiceCob(boolean freightInvoiceCob) {
        this.freightInvoiceCob = freightInvoiceCob;
    }

    public boolean isConfirmOnBoardCob() {
        return confirmOnBoardCob;
    }

    public void setConfirmOnBoardCob(boolean confirmOnBoardCob) {
        this.confirmOnBoardCob = confirmOnBoardCob;
    }

    public boolean isObkgToAny() {
        return obkgToAny;
    }

    public void setObkgToAny(boolean obkgToAny) {
        this.obkgToAny = obkgToAny;
    }

    public boolean isRunvToRcvd() {
        return runvToRcvd;
    }

    public void setRunvToRcvd(boolean runvToRcvd) {
        this.runvToRcvd = runvToRcvd;
    }

    public boolean isAny() {
        return any;
    }

    public void setAny(boolean any) {
        this.any = any;
    }

    public GenericCode getCodek() {
        return codek;
    }

    public void setCodek(GenericCode codek) {
        this.codek = codek;
    }

    public boolean isLclExports() {
        return lclExports;
    }

    public void setLclExports(boolean lclExports) {
        this.lclExports = lclExports;
    }

    public boolean isLclImports() {
        return lclImports;
    }

    public void setLclImports(boolean lclImports) {
        this.lclImports = lclImports;
    }

    public boolean isFclExports() {
        return fclExports;
    }

    public void setFclExports(boolean fclExports) {
        this.fclExports = fclExports;
    }

    public boolean isFclImports() {
        return fclImports;
    }

    public void setFclImports(boolean fclImports) {
        this.fclImports = fclImports;
    }

    public boolean isOnlyWhenBookingContactCodeK() {
        return onlyWhenBookingContactCodeK;
    }

    public void setOnlyWhenBookingContactCodeK(boolean onlyWhenBookingContactCodeK) {
        this.onlyWhenBookingContactCodeK = onlyWhenBookingContactCodeK;
    }

    public boolean isApplicableToAllShipmentsCodeK() {
        return applicableToAllShipmentsCodeK;
    }

    public void setApplicableToAllShipmentsCodeK(boolean applicableToAllShipmentsCodeK) {
        this.applicableToAllShipmentsCodeK = applicableToAllShipmentsCodeK;
    }

}

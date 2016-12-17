package com.gp.cong.logisoft.domain;

import com.gp.cong.logisoft.domain.lcl.LclSearchTemplate;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import org.apache.commons.codec.binary.Base64;

public class User implements Auditable, Serializable {

    private static final long serialVersionUID = 1L;
    private Integer userId;
    private String firstName;
    private String lastName;
    private String telephone;
    private String address1;
    private String address2;
    private String country;
    private String state;
    private String city;
    private String zipCode;
    private String loginName;
    private String roleName;
    private Role role;
    private String password;
    private String status;
    private Date userCreatedDate;
    private String email;
    private String extension;
    private String fax;
    private RefTerminal terminal;
    private RefTerminal importTerminal;
    private RefTerminal billingTerminal;
    private String userCreatedOn;
    private Set associtedPrinter;
    private GenericCode genericCode;
    private UnLocation unLocation;
    private String match;
    private String officeCityLOcation;
    private Set<CollectorTradingPartner> collectorTradingPartners;
    private Set<APSpecialistTradingPartner> apSpecialistTradingPartners;
    private boolean achApprover;
    private boolean searchScreenReset;
    private Date lastLoginDate;
    private String outsourceEmail;
    private String ctsPackageType;
    private String ctsPalletType;
    private String difflclBookedDimsActual;
    private TradingPartner ctsAccount;
    private Double flatFee;
    private Double lineMarkUp;
    private Double fuelMarkUp;
    private Double minAmount;
    private boolean warehouse;
    private byte[] signatureImage;
    private transient String signatureImg;
    private String warehouseNo;
    private LclSearchTemplate userTemplate;

    public User() {
    }

    public User(Integer userId) {
        this.userId = userId;
    }

    public GenericCode getGenericCode() {
        return genericCode;
    }

    public void setGenericCode(GenericCode genericCode) {
        this.genericCode = genericCode;
    }

    public UnLocation getUnLocation() {
        return unLocation;
    }

    public void setUnLocation(UnLocation unLocation) {
        this.unLocation = unLocation;
    }

    public String getOfficeCityLOcation() {
        return officeCityLOcation;
    }

    public void setOfficeCityLOcation(String officeCityLOcation) {
        this.officeCityLOcation = officeCityLOcation;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * @return the terminal
     */
    public RefTerminal getTerminal() {
        return terminal;
    }

    /**
     * @param terminal the terminal to set
     */
    public void setTerminal(RefTerminal terminal) {
        this.terminal = terminal;
    }

    public RefTerminal getBillingTerminal() {
        return billingTerminal;
    }

    public void setBillingTerminal(RefTerminal billingTerminal) {
        this.billingTerminal = billingTerminal;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getUserCreatedDate() {

        return userCreatedDate;
    }

    public void setUserCreatedDate(Date userCreatedDate) {

        this.userCreatedDate = userCreatedDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUserCreatedOn() {
        if (userCreatedDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss a");
            return sdf.format(userCreatedDate);
        }
        return userCreatedOn;
    }

    public void setUserCreatedOn(String userCreatedOn) {

        this.userCreatedOn = userCreatedOn;
    }

    public Set getAssocitedPrinter() {
        return associtedPrinter;
    }

    public void setAssocitedPrinter(Set associtedPrinter) {
        this.associtedPrinter = associtedPrinter;
    }

    public AuditInfo getAuditInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getId() {
        // TODO Auto-generated method stub
        return this.getUserId();
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<APSpecialistTradingPartner> getApSpecialistTradingPartners() {
        return apSpecialistTradingPartners;
    }

    public void setApSpecialistTradingPartners(Set<APSpecialistTradingPartner> apSpecialistTradingPartners) {
        this.apSpecialistTradingPartners = apSpecialistTradingPartners;
    }

    public Set<CollectorTradingPartner> getCollectorTradingPartners() {
        return collectorTradingPartners;
    }

    public void setCollectorTradingPartners(Set<CollectorTradingPartner> collectorTradingPartners) {
        this.collectorTradingPartners = collectorTradingPartners;
    }

    public boolean isAchApprover() {
        return achApprover;
    }

    public void setAchApprover(boolean achApprover) {
        this.achApprover = achApprover;
    }

    public RefTerminal getImportTerminal() {
        return importTerminal;
    }

    public void setImportTerminal(RefTerminal importTerminal) {
        this.importTerminal = importTerminal;
    }

    public String getOutsourceEmail() {
        return outsourceEmail;
    }

    public void setOutsourceEmail(String outsourceEmail) {
        this.outsourceEmail = outsourceEmail;
    }

    public String getDifflclBookedDimsActual() {
        return difflclBookedDimsActual;
    }

    public void setDifflclBookedDimsActual(String difflclBookedDimsActual) {
        this.difflclBookedDimsActual = difflclBookedDimsActual;
    }

    public boolean isSearchScreenReset() {
        return searchScreenReset;
    }

    public void setSearchScreenReset(boolean searchScreenReset) {
        this.searchScreenReset = searchScreenReset;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getCtsPackageType() {
        return ctsPackageType;
    }

    public void setCtsPackageType(String ctsPackageType) {
        this.ctsPackageType = ctsPackageType;
    }

    public String getCtsPalletType() {
        return ctsPalletType;
    }

    public void setCtsPalletType(String ctsPalletType) {
        this.ctsPalletType = ctsPalletType;
    }

    public TradingPartner getCtsAccount() {
        return ctsAccount;
    }

    public void setCtsAccount(TradingPartner ctsAccount) {
        this.ctsAccount = ctsAccount;
    }

    public Double getFlatFee() {
        return flatFee;
    }

    public void setFlatFee(Double flatFee) {
        this.flatFee = flatFee;
    }

    public Double getFuelMarkUp() {
        return fuelMarkUp;
    }

    public void setFuelMarkUp(Double fuelMarkUp) {
        this.fuelMarkUp = fuelMarkUp;
    }

    public Double getLineMarkUp() {
        return lineMarkUp;
    }

    public void setLineMarkUp(Double lineMarkUp) {
        this.lineMarkUp = lineMarkUp;
    }

    public Double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Double minAmount) {
        this.minAmount = minAmount;
    }

    public boolean isWarehouse() {
        return warehouse;
    }

    public void setWarehouse(boolean warehouse) {
        this.warehouse = warehouse;
    }

    public byte[] getSignatureImage() {
        return signatureImage;
    }

    public void setSignatureImage(byte[] signatureImage) {
        this.signatureImage = signatureImage;
    }

    public String getSignaturImg() {
        if (null != signatureImage) {
            signatureImg = Base64.encodeBase64String(signatureImage);
        }
        return signatureImg;
    }

    public String getWarehouseNo() {
        return warehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo;
    }

    public LclSearchTemplate getUserTemplate() {
        return userTemplate;
    }

    public void setUserTemplate(LclSearchTemplate userTemplate) {
        this.userTemplate = userTemplate;
    }

}

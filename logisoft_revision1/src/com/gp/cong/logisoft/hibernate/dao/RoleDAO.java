package com.gp.cong.logisoft.hibernate.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.gp.cong.logisoft.domain.Role;
import com.gp.cong.logisoft.domain.RoleDuty;
import com.gp.cong.logisoft.struts.form.EditRoleDForm;
import org.apache.log4j.Logger;

public class RoleDAO extends BaseHibernateDAO {

    private static final Logger log = Logger.getLogger(RoleDAO.class);

    public void save(Role transientInstance, String userName) throws Exception {
        getCurrentSession().save(transientInstance);
        getCurrentSession().flush();
    }

    public Iterator getAllRolesForDisplay() throws Exception {
        Iterator results = null;
        results = getCurrentSession().createQuery(
                "select roleId,roleDesc from Role order by roleDesc").list().iterator();
        return results;
    }

    public Role findById(Integer id) throws Exception {
        Role instance = (Role) getCurrentSession().get("com.gp.cong.logisoft.domain.Role", id);
        return instance;
    }

    public List findRoleName(String roleName) throws Exception {
        List list = new ArrayList();
        String queryString = "from Role where roleDesc=?0";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", roleName);
        list = queryObject.list();
        return list;
    }

    public List findAllRoles() throws Exception {
        String queryString = "  from Role order by roleDesc";
        Query queryObject = getCurrentSession().createQuery(queryString);
        return queryObject.list();
    }

    public List findForManagement(String roleDesc, Date roleCreatedDate) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Role.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (roleDesc != null && !roleDesc.equals("")) {
            criteria.add(Restrictions.like("roleDesc", roleDesc + "%"));
        }
        if (roleCreatedDate != null && !roleCreatedDate.equals("")) {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String strSODate = dateFormat.format(roleCreatedDate);
            //criteria.add(Expression.eq("logisticOrderDate", strSODate));
            Date soStartDate = (Date) dateFormat.parse(strSODate);
            Date soEndDate = new Date(soStartDate.getYear(), soStartDate.getMonth(), soStartDate.getDate() + 1);
            criteria.add(Expression.between("roleCreatedOn", soStartDate, soEndDate));
        }
        criteria.addOrder(Order.asc("roleDesc"));
        return criteria.list();
    }

    public List findForSearchRoleAction(String roleDesc, Date roleCreatedDate, String match) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Role.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        if (roleDesc != null && !roleDesc.equals("")) {
            criteria.add(Restrictions.ge("roleDesc", roleDesc));

            criteria.addOrder(Order.asc("roleDesc"));
        }

        if (roleCreatedDate != null && !roleCreatedDate.equals("")) {
            try {
                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                String strSODate = dateFormat.format(roleCreatedDate);
                //criteria.add(Expression.eq("logisticOrderDate", strSODate));
                Date soStartDate = (Date) dateFormat.parse(strSODate);
                Date soEndDate = new Date(soStartDate.getYear(), soStartDate.getMonth(), soStartDate.getDate() + 1);
                criteria.add(Expression.ge("roleCreatedOn", roleCreatedDate));
                criteria.addOrder(Order.asc("roleCreatedOn"));
            } catch (Exception e) {
                log.info("Error while parsing date in findForSearchRoleAction method", e);
            }
        }
        criteria.addOrder(Order.asc("roleDesc"));
        return criteria.list();
    }

    public List findRoleName(Role roleId) {
        //System.out.println("getting login name: "+loginName);
        List list = new ArrayList();
        try {
            String queryString = "from User where role=?0";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", roleId);
            list = queryObject.list();

        } catch (RuntimeException re) {
            log.info("Error in Find by Username in findRoleName method", re);
        }

        return list;
    }

    public void delete(Role persistanceInstance) throws Exception {
        getSession().delete(persistanceInstance);
        getSession().flush();
    }

    public void update(Role persistanceInstance, String userName) throws Exception {
        getSession().saveOrUpdate(persistanceInstance);
        getSession().flush();
    }

    public boolean exists(EditRoleDForm erdf) {
        Criteria crit = getCurrentSession().createCriteria(RoleDuty.class);
        crit.add(Restrictions.eq("roleId", erdf.getRoleId()));
//        crit.add(Restrictions.eq("id", erdf.getId()));
        if (crit.list().isEmpty()) {
            return false;
        }
        return true;
    }

    public void editRoleDuty(EditRoleDForm erdf) throws Exception {
        RoleDuty rd = (RoleDuty) getCurrentSession().createCriteria(RoleDuty.class).add(Restrictions.eq("roleId", erdf.getRoleId())).uniqueResult();
        rd.setAccessVoidButton(erdf.isAccessVoidButton());
        rd.setApSpec(erdf.isApSpec());
        rd.setArInqMngr(erdf.isArInqMngr());
        rd.setBatchAccMngr(erdf.isBatchAccMngr());
        rd.setCreditHolder(erdf.isCreditHolder());
        rd.setSupervisor(erdf.isSupervisor());
        rd.setAccessDisputedBlNotesAndAck(erdf.isAccessDisputedBlNotesAndAck());
        rd.setUnmanifest(erdf.isUnmanifest());
        rd.setReopenBl(erdf.isReopenBl());
        rd.setCloseBl(erdf.isCloseBl());
        rd.setRevCorrections(erdf.isRevCorrections());
        rd.setPostCorrections(erdf.isPostCorrections());
        rd.setDisableOrEnableTp(erdf.isDisableOrEnableTp());
        rd.setShowDetailedCharges(erdf.isShowDetailedCharges());
        rd.setEditEciAcct(erdf.isEditEciAcct());
        rd.setVendorOtherthanFF(erdf.isVendorOtherthanFF());
        rd.setAddPredefinedRemarks(erdf.isAddPredefinedRemarks());
        rd.setAccessCorrectionPrintFax(erdf.isAccessCorrectionPrintFax());
        rd.setChangeMaster(erdf.isChangeMaster());
        rd.setChangeTpType(erdf.isChangeTpType());
        rd.setTakeOwnershipOfDisputedBL(erdf.isTakeOwnershipOfDisputedBL());
        rd.setArInquiryChangeCustomer(erdf.isArInquiryChangeCustomer());
        rd.setArInquiryMakeAdjustments(erdf.isArInquiryMakeAdjustments());
        rd.setArBatchShowallUsersBatch(erdf.isArBatchShowallUsersBatch());
        rd.setArBatchDirectGlAccount(erdf.isArBatchDirectGlAccount());
        rd.setAccrualsCreateNew(erdf.isAccrualsCreateNew());
        rd.setBankAccountCreateNew(erdf.isBankAccountCreateNew());
        rd.setJournalEntryClosedPeriod(erdf.isJournalEntryClosedPeriod());
        rd.setTpSetVendorType(erdf.isTpSetVendorType());
        rd.setTpShowAddress(erdf.isTpShowAddress());
        rd.setTpShowGeneralInfo(erdf.isTpShowGeneralInfo());
        rd.setTpShowArConfig(erdf.isTpShowArConfig());
        rd.setTpShowApConfig(erdf.isTpShowApConfig());
        rd.setTpShowContactConfig(erdf.isTpShowContactConfig());
        rd.setTpShowConsigneeInfo(erdf.isTpShowConsigneeInfo());
        rd.setViewAccountingScanAttach(erdf.isViewAccountingScanAttach());
        rd.setResendAes(erdf.isResendAes());
        rd.setResendAccruals(erdf.isResendAccruals());
        rd.setAudit(erdf.isAudit());
        rd.setCancelAudit(erdf.isCancelAudit());
        rd.setCheckRegisterController(erdf.isCheckRegisterController());
        rd.setAllowRoutedAgent(erdf.isAllowRoutedAgent());
        rd.setDeleteLclCommodity(erdf.isDeleteLclCommodity());
        rd.setByPassVoyage(erdf.isByPassVoyage());
        rd.setEditLclBlOwner(erdf.isEditLclBlOwner());
        rd.setDeleteVoyage(erdf.isDeleteVoyage());
        rd.setOpenLclUnit(erdf.isOpenLclUnit());
        rd.setUnmanifestLclUnit(erdf.isUnmanifestLclUnit());
        rd.setLclCurrentLocation(erdf.isLclCurrentLocation());
        rd.setUnPost(erdf.isUnPost());
        rd.setTerminateWithoutInvoice(erdf.isTerminateWithoutInvoice());
        rd.setShowFollowUpTasks(erdf.isShowFollowUpTasks());
        rd.setDisplayDefaults(erdf.isDisplayDefaults());
        rd.setAuditOverride(erdf.isAuditOverride());
        rd.setSslPrepaidCollect(erdf.isSslPrepaidCollect());
        rd.setArBatchReversal(erdf.isArBatchReversal());
        rd.setApPayment(erdf.isApPayment());
        rd.setReversePostedInvoices(erdf.isReversePostedInvoices());
        rd.setCreditHoldOpsUser(erdf.isCreditHoldOpsUser());
        rd.setInactivateAccruals(erdf.isInactivateAccruals());
        rd.setChangeVoyage(erdf.isChangeVoyage());
        rd.setBookingTerminate(erdf.isBookingTerminate());
        rd.setLclVoyageOwner(erdf.isLclVoyageOwner());
        rd.setLclExpVoyageOwner(erdf.isLclExpVoyageOwner());
        rd.setExpDeleteVoyage(erdf.isExpDeleteVoyage());
        rd.setTpShowCtsConfig(erdf.isTpShowCtsConfig());
        rd.setEcuDesignation(erdf.isEcuDesignation());
        rd.setDeleteDisposition(erdf.isDeleteDisposition());
        rd.setLclEcuInvoiceMapp(erdf.isLclEcuInvoiceMapp());
        rd.setDeleteImportsUnit(erdf.isDeleteImportsUnit());
        rd.setLclUnitOSD(erdf.isLclUnitOSD());
        rd.setDeleteImportContainers(erdf.isDeleteImportContainers());
        rd.setLclImportVoyageClose(erdf.isLclImportVoyageClose());
        rd.setLclImportVoyageReopen(erdf.isLclImportVoyageReopen());
        rd.setLclImportVoyageAudit(erdf.isLclImportVoyageAudit());
        rd.setDeleteCostandCharges(erdf.isDeleteCostandCharges());
        rd.setAllowtoEnterSpotRate(erdf.isAllowtoEnterSpotRate());
        rd.setImportsVoyagePod(erdf.isImportsVoyagePod());
        rd.setDeleteAttachedDocuments(erdf.isDeleteAttachedDocuments());
        rd.setEditDeferralCharge(erdf.isEditDeferralCharge());
        rd.setArConfigTabReadOnly(erdf.isArConfigTabReadOnly());
        rd.setAllowImportCfsVendor(erdf.isAllowImportCfsVendor());
        rd.setDeleteManualNotes(erdf.isDeleteManualNotes());
        rd.setChangeSalesCode(erdf.isChangeSalesCode());
        rd.setLinkDrAfterDispositionPort(erdf.isLinkDrAfterDispositionPort());
        rd.setChangeLogoPreference(erdf.isChangeLogoPreference());
        rd.setDisabledContainerwithAPcosts(erdf.isDisabledContainerwithAPcosts());
        rd.setShowUncompleteUnits(erdf.isShowUncompleteUnits());
        rd.setReverseCob(erdf.isReverseCob());
        rd.setVoyageCloseAuditUndo(erdf.isVoyageCloseAuditUndo());
        rd.setVoidLCLBLafterCOB(erdf.isVoidLCLBLafterCOB());
        rd.setDeleteNotes(erdf.isDeleteNotes());
        rd.setAddTemplates(erdf.isAddTemplates());
        rd.setAesRequiredForReleasingDRs(erdf.isAesRequiredForReleasingDRs());
        rd.setAesRequiredForPostingBLs(erdf.isAesRequiredForPostingBLs());
        rd.setManageECIAccountLink(erdf.isManageECIAccountLink());
        rd.setBatchHsCode(erdf.isBatchHsCode());
        rd.setLclBookingContact(erdf.isLclBookingContact());
        rd.setWarehouseQuickBkg(erdf.isWarehouseQuickBkg());
        rd.setBkgVoyageReleaseDr(erdf.isBkgVoyageReleaseDr());
        rd.setLclManifestPostedBl(erdf.isLclManifestPostedBl());
        rd.setWeightChangeAfterRelease(erdf.isWeightChangeAfterRelease());
        rd.setPreventExpRelease(erdf.isPreventExpRelease());
        rd.setDeleteUnits(erdf.isDeleteUnits());
        rd.setRemoveDrHold(erdf.isRemoveDrHold());
        rd.setNo997EdiSubmission(erdf.isNo997EdiSubmission());
        rd.setBypassRelayCheck(erdf.isBypassRelayCheck());
        rd.setDefaultLoadAllReleased(erdf.isDefaultLoadAllReleased());
        rd.setLclQuoteClient(erdf.isLclQuoteClient());
        rd.setPickDrWarnings(erdf.isPickDrWarnings());
        rd.setChangeBLCommodityAfterCOB(erdf.isChangeBLCommodityAfterCOB());
        rd.setChangeDefaultFF(erdf.isChangeDefaultFF());
        rd.setDefaultDocsRcvd(erdf.isDefaultDocsRcvd());
        rd.setDefaultNoeeiLowVal(erdf.isDefaultNoeeiLowVal());
        rd.setAllowChangeDisposition(erdf.isAllowChangeDisposition());
        rd.setBatchHotCode(erdf.isBatchHotCode());
        rd.setLclBookingDefaultERT(erdf.isLclBookingDefaultERT());
        rd.setLclImportAllowTransshipment(erdf.isLclImportAllowTransshipment());
        getCurrentSession().update(rd);
        getCurrentSession().flush();
    }

    public void clearRoleDuty(EditRoleDForm erdf) throws Exception {
        RoleDuty rd = (RoleDuty) getCurrentSession().createCriteria(RoleDuty.class).add(Restrictions.eq("roleId", erdf.getRoleId())).uniqueResult();
        rd.setAccessVoidButton(false);
        rd.setApSpec(false);
        rd.setArInqMngr(false);
        rd.setBatchAccMngr(false);
        rd.setCreditHolder(false);
        rd.setSupervisor(false);
        rd.setAccessDisputedBlNotesAndAck(false);
        rd.setUnmanifest(false);
        rd.setReopenBl(false);
        rd.setCloseBl(false);
        rd.setAudit(false);
        rd.setCancelAudit(false);
        rd.setRevCorrections(false);
        rd.setPostCorrections(false);
        rd.setDisableOrEnableTp(false);
        rd.setShowDetailedCharges(false);
        rd.setEditEciAcct(false);
        rd.setVendorOtherthanFF(false);
        rd.setAddPredefinedRemarks(false);
        rd.setAccessCorrectionPrintFax(false);
        rd.setChangeMaster(false);
        rd.setChangeTpType(false);
        rd.setTakeOwnershipOfDisputedBL(false);
        rd.setArInquiryChangeCustomer(false);
        rd.setArInquiryMakeAdjustments(false);
        rd.setArBatchShowallUsersBatch(false);
        rd.setArBatchDirectGlAccount(false);
        rd.setAccrualsCreateNew(false);
        rd.setBankAccountCreateNew(false);
        rd.setJournalEntryClosedPeriod(false);
        rd.setTpSetVendorType(false);
        rd.setTpShowAddress(false);
        rd.setTpShowGeneralInfo(false);
        rd.setTpShowArConfig(false);
        rd.setTpShowApConfig(false);
        rd.setTpShowContactConfig(false);
        rd.setTpShowConsigneeInfo(false);
        rd.setResendAes(false);
        rd.setResendAccruals(false);
        rd.setViewAccountingScanAttach(false);
        rd.setAllowRoutedAgent(false);
        rd.setDeleteLclCommodity(false);
        rd.setAuditOverride(false);
        rd.setSslPrepaidCollect(false);
        rd.setByPassVoyage(false);
        rd.setEditLclBlOwner(false);
        rd.setDeleteVoyage(false);
        rd.setOpenLclUnit(false);
        rd.setTerminateWithoutInvoice(false);
        rd.setArBatchReversal(false);
        rd.setApPayment(false);
        rd.setReversePostedInvoices(false);
        rd.setCreditHoldOpsUser(false);
        rd.setInactivateAccruals(false);
        rd.setChangeVoyage(false);
        rd.setBookingTerminate(false);
        rd.setLclVoyageOwner(false);
        rd.setLclExpVoyageOwner(false);
        rd.setExpDeleteVoyage(false);
        rd.setTpShowCtsConfig(false);
        rd.setEcuDesignation(false);
        rd.setDeleteDisposition(false);
        rd.setLclEcuInvoiceMapp(false);
        rd.setDeleteImportsUnit(false);
        rd.setLclUnitOSD(false);
        rd.setDeleteImportContainers(false);
        rd.setLclImportVoyageClose(false);
        rd.setLclImportVoyageReopen(false);
        rd.setLclImportVoyageAudit(false);
        rd.setDeleteCostandCharges(false);
        rd.setAllowtoEnterSpotRate(false);
        rd.setImportsVoyagePod(false);
        rd.setDeleteAttachedDocuments(false);
        rd.setEditDeferralCharge(false);
        rd.setArConfigTabReadOnly(false);
        rd.setAllowImportCfsVendor(false);
        rd.setDeleteManualNotes(false);
        rd.setChangeSalesCode(false);
        rd.setLinkDrAfterDispositionPort(false);
        rd.setChangeLogoPreference(false);
        rd.setDisabledContainerwithAPcosts(false);
        rd.setShowUncompleteUnits(false);
        rd.setReverseCob(false);
        rd.setVoyageCloseAuditUndo(false);
        rd.setVoidLCLBLafterCOB(false);
        rd.setDeleteNotes(false);
        rd.setAddTemplates(false);
        rd.setAesRequiredForReleasingDRs(false);
        rd.setAesRequiredForPostingBLs(false);
        rd.setManageECIAccountLink(false);
        rd.setBatchHsCode(false);
        rd.setLclBookingContact(false);
        rd.setWarehouseQuickBkg(false);
        rd.setBkgVoyageReleaseDr(false);
        rd.setLclManifestPostedBl(false);
        rd.setWeightChangeAfterRelease(false);
        rd.setPreventExpRelease(false);
        rd.setDeleteUnits(false);
        rd.setRemoveDrHold(false);
        rd.setNo997EdiSubmission(false);
        rd.setBypassRelayCheck(false);
        rd.setDefaultLoadAllReleased(false);
        rd.setLclQuoteClient(false);
        rd.setPickDrWarnings(true);
        rd.setChangeBLCommodityAfterCOB(false);
        rd.setChangeDefaultFF(false);
        rd.setDefaultNoeeiLowVal(false);
        rd.setDefaultDocsRcvd(false);
        rd.setAllowChangeDisposition(false);
        rd.setBatchHotCode(false);
        rd.setLclBookingDefaultERT(false);
        rd.setLclImportAllowTransshipment(false);
        getCurrentSession().saveOrUpdate(rd);
        getCurrentSession().flush();
    }

    public RoleDuty getRoleDuty(Integer roleId) throws Exception {
        getCurrentSession().flush();
        return (RoleDuty) getCurrentSession().createCriteria(RoleDuty.class).add(Restrictions.eq("roleId", roleId)).uniqueResult();
    }

    public void makeRoleDuty(EditRoleDForm erdf) throws Exception {
        RoleDuty rd = new RoleDuty(erdf.getRoleId(), erdf.getRoleName());
        getCurrentSession().save(rd);
    }

    public List<Role> getRoles() throws Exception {
        return (List<Role>) getCurrentSession().createCriteria(Role.class).list();
    }

    public List<Integer> getRoleIds() throws Exception {
        List<Integer> results = new ArrayList<Integer>();
        List<Role> roles = this.getRoles();
        for (Role r : roles) {
            results.add(r.getRoleId());
        }
        return (List<Integer>) results;
    }
}

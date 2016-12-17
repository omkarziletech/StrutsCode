package com.gp.cong.logisoft.bc.accounting;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cvst.logisoft.domain.BankDetails;
import com.gp.cvst.logisoft.hibernate.dao.BankDetailsDAO;
import com.gp.cvst.logisoft.struts.form.BankAccountForm;
import com.logiware.utils.AuditNotesUtils;
import java.util.Date;
import org.apache.commons.beanutils.PropertyUtils;

public class APBankAccountBC {

    public List saveBackAccount(BankAccountForm bankAccountForm, User loginUser)throws Exception {
        List<BankDetails> bankDetailsList = new ArrayList<BankDetails>();
        BankDetails bankDetails = new BankDetails(bankAccountForm);
        if (CommonUtils.isNotEmpty(bankAccountForm.getUserName())) {
            Set userSet = new HashSet<User>();
            String[] userNames = StringUtils.split(bankAccountForm.getUserName(), ",");
            for (String loginName : userNames) {
                List user = new UserDAO().findLoginName(loginName);
                if (CommonUtils.isNotEmpty(user)) {
                    userSet.add(user.get(0));
                }

            }
            bankDetails.setUsers(userSet);
        }
        new BankDetailsDAO().save(bankDetails);
        StringBuilder desc = new StringBuilder("Bank Account '").append(bankDetails.getBankAcctNo()).append("'");
        desc.append(" of '").append(bankDetails.getBankName()).append("'");
        desc.append(" added by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
        AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.BANK_ACCOUNT, bankDetails.getBankName() + "-" + bankDetails.getBankAcctNo(),
                NotesConstants.BANK_ACCOUNT, loginUser);
        bankDetailsList.add(bankDetails);
        return bankDetailsList;
    }

    public List getackAccountList(BankAccountForm bankAccountForm)throws Exception {
        return new BankDetailsDAO().findByBankAccountNumberAndBankName(bankAccountForm.getBankName(), bankAccountForm.getBankAccountNumber());
    }

    public BankAccountForm getBankDetail(BankAccountForm bankAccountForm) throws Exception {
        BankDetails bankDetails = null;
        if (CommonUtils.isNotEmpty(bankAccountForm.getBankAccountId())) {
            bankDetails = this.bankDetails(bankAccountForm);
        }
        PropertyUtils.copyProperties(bankAccountForm, new BankAccountForm(bankDetails));
        if (null != bankDetails && bankDetails.getUsers().size() > 0) {
            String userNames = "";
            for (User user : bankDetails.getUsers()) {
                if (userNames.equals("")) {
                    userNames = user.getLoginName();
                } else {
                    userNames = userNames + "," + user.getLoginName();
                }

            }
            bankAccountForm.setUserName(userNames);
        }
        return bankAccountForm;
    }

    public List updateBackAccount(BankAccountForm bankAccountForm, User loginUser)throws Exception {
        List<BankDetails> bankDetailsList = new ArrayList<BankDetails>();
        BankDetails bankDetails = new BankDetailsDAO().findById(Integer.parseInt(bankAccountForm.getBankAccountId()));
        if (CommonUtils.isNotEmpty(bankAccountForm.getUserName())) {
            Set<User> usersFromDomain = bankDetails.getUsers();
            Set<User> usersFromForm = new HashSet<User>();
            String removedUser = "";
            String addedUser = "";
            String[] userNames = StringUtils.split(bankAccountForm.getUserName(), ",");
            for (String loginName : userNames) {
                List users = new UserDAO().findLoginName(loginName);
                if (CommonUtils.isNotEmpty(users)) {
                    User user = (User) users.get(0);
                    usersFromForm.add(user);
                    if (!usersFromDomain.contains(user)) {
                        addedUser = addedUser + user.getLoginName() + ",";
                    }
                }
            }
            for (User user : usersFromDomain) {
                if (!usersFromForm.contains(user)) {
                    removedUser = removedUser + user.getLoginName() + ",";
                }
            }
            if (CommonUtils.isNotEmpty(addedUser)) {
                StringBuilder desc = new StringBuilder("User '").append(StringUtils.removeEnd(addedUser, ",")).append("' assigned with ");
                desc.append(bankDetails.getBankAcctNo()).append(" of ").append(bankDetails.getBankName());
                desc.append(" by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.BANK_ACCOUNT, bankDetails.getBankName() + "-" + bankDetails.getBankAcctNo(), "User", loginUser);
            }
            if (CommonUtils.isNotEmpty(removedUser)) {
                StringBuilder desc = new StringBuilder("User '").append(StringUtils.removeEnd(removedUser, ",")).append("' unassigned from ");
                desc.append(bankDetails.getBankAcctNo()).append(" of ").append(bankDetails.getBankName());
                desc.append(" by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.BANK_ACCOUNT, bankDetails.getBankName() + "-" + bankDetails.getBankAcctNo(), "User", loginUser);
            }
            bankDetails.setUsers(usersFromForm);
        }

        bankDetails.setBankAcctNo(bankAccountForm.getBankAccountNumber());
        bankDetails.setBankName(bankAccountForm.getBankName());
        bankDetails.setBankAddress(bankAccountForm.getBankAddress());
        bankDetails.setGlAccountno(bankAccountForm.getGlAccountNumber());
        bankDetails.setBankRoutingNumber(bankAccountForm.getBankRoutingNumber());
        bankDetails.setLoginName(bankAccountForm.getLoginName());
        bankDetails.setAcctNo(bankAccountForm.getAccountNumber());
        bankDetails.setAcctName(bankAccountForm.getAccountName());
        if (CommonUtils.isNotEmpty(bankAccountForm.getStartingNumber())) {
            bankDetails.setStartingSerialNo(Integer.parseInt(bankAccountForm.getStartingNumber()));
        }

        bankDetails.setBankEmail(bankAccountForm.getBankEmail());
        AuditNotesUtils.insertAuditNotes(bankDetails.getCheckPrinter(), bankAccountForm.getCheckPrinter(),
                NotesConstants.BANK_ACCOUNT, bankDetails.getBankName() + "-" + bankDetails.getBankAcctNo(), "Check Printer", loginUser);
        bankDetails.setCheckPrinter(CommonUtils.isNotEmpty(bankAccountForm.getCheckPrinter()) ? bankAccountForm.getCheckPrinter() : null);
        AuditNotesUtils.insertAuditNotes(bankDetails.getOverflowPrinter(), bankAccountForm.getOverflowPrinter(),
                NotesConstants.BANK_ACCOUNT, bankDetails.getBankName() + "-" + bankDetails.getBankAcctNo(), "Overflow Printer", loginUser);
        bankDetails.setOverflowPrinter(CommonUtils.isNotEmpty(bankAccountForm.getOverflowPrinter()) ? bankAccountForm.getOverflowPrinter() : null);

        bankDetailsList.add(bankDetails);
        return bankDetailsList;
    }

    public BankDetails bankDetails(BankAccountForm bankAccountForm)throws Exception {
        return new BankDetailsDAO().findById(new Integer(bankAccountForm.getBankAccountId()));
    }

    public void deleteBankDetails(BankAccountForm bankAccountForm)throws Exception {
        BankDetails bankDetails = null;
        if (bankAccountForm.getBankAccountId() != null && !bankAccountForm.getBankAccountId().equals("")) {
            bankDetails = bankDetails(bankAccountForm);
            new BankDetailsDAO().delete(bankDetails);
        }

    }
}

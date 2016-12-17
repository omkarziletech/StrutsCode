package com.logiware.utils;

import com.logiware.form.AchSetUpForm;
import com.logiware.hibernate.domain.AchSetUp;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public class AchSetUpUtils {

    static Properties prop = null;

    private static void init() throws Exception {
        prop = new Properties();
        prop.load(AchSetUpUtils.class.getResourceAsStream("/com/logiware/properties/AchSetUp.properties"));
    }

    public static String getProperty(String property) throws Exception {
        if (prop == null) {
            init();
        }
        return prop.getProperty(property);
    }

    public static AchSetUp copyProperties(AchSetUpForm achSetUpForm, AchSetUp achSetUp) throws FileNotFoundException, IOException {
        achSetUp.setFileHeaderRecordTypeCode(achSetUpForm.getFileHeaderRecordTypeCode());
        achSetUp.setPriorityCode(achSetUpForm.getPriorityCode());
        achSetUp.setImmediateOrigin(achSetUpForm.getImmediateOrigin());
        achSetUp.setFileIdModifier(achSetUpForm.getFileIdModifier());
        achSetUp.setRecordSize(achSetUpForm.getRecordSize());
        achSetUp.setBlockingFactor(achSetUpForm.getBlockingFactor());
        achSetUp.setFormatCode(achSetUpForm.getFormatCode());
        achSetUp.setBatchHeaderRecordTypeCode(achSetUpForm.getBatchHeaderRecordTypeCode());
        achSetUp.setServiceClassCode(achSetUpForm.getServiceClassCode());
        achSetUp.setCompanyName(StringUtils.substring(achSetUpForm.getCompanyName(), 0, 16));
        achSetUp.setCompanyIdentification(achSetUpForm.getCompanyIdentification());
        achSetUp.setStandardEntryClass(achSetUpForm.getStandardEntryClass());
        achSetUp.setCompanyEntryDescription(achSetUpForm.getCompanyEntryDescription());
        achSetUp.setOriginatorStatusCode(achSetUpForm.getOriginatorStatusCode());
        achSetUp.setEntryDetailRecordTypeCode(achSetUpForm.getEntryDetailRecordTypeCode());
        achSetUp.setTransactionCode(achSetUpForm.getTransactionCode());
        achSetUp.setAddendaRecordIndicator(achSetUpForm.getAddendaRecordIndicator());
        achSetUp.setBatchSequence(null != achSetUp.getBatchSequence() ? achSetUp.getBatchSequence() : 1);
        achSetUp.setFtpHost(achSetUpForm.getFtpHost());
        achSetUp.setFtpUserName(achSetUpForm.getFtpUserName());
        achSetUp.setFtpPassword(achSetUpForm.getFtpPassword());
        if (!achSetUpForm.getHavePublicKey() && null != achSetUpForm.getPublicKeyFile() && achSetUpForm.getPublicKeyFile().getFileSize() > 0) {
            InputStream is = null;
            try {
                is = achSetUpForm.getPublicKeyFile().getInputStream();
                achSetUp.setPublicKey(IOUtils.toByteArray(is));
            } catch (IOException e) {
                throw e;
            } finally {
                if (null != is) {
                    is.close();
                }
            }
        }
        if (!achSetUpForm.getHasSshPrivateKey() && null != achSetUpForm.getSshPrivateKeyFile() && achSetUpForm.getSshPrivateKeyFile().getFileSize() > 0) {
            InputStream is = null;
            try {
                is = achSetUpForm.getSshPrivateKeyFile().getInputStream();
                achSetUp.setSshPrivateKey(IOUtils.toByteArray(is));
            } catch (IOException e) {
                throw e;
            } finally {
                if (null != is) {
                    is.close();
                }
            }
        }
        achSetUp.setSshPassphrase(achSetUpForm.getSshPassphrase());
        achSetUp.setFtpPort(achSetUpForm.getFtpPort());
        achSetUp.setFtpDirectory(achSetUpForm.getFtpDirectory());
        return achSetUp;
    }
}

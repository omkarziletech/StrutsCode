package com.logiware.security;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.logiware.hibernate.domain.AchProcessHistory;
import com.logiware.hibernate.domain.AchSetUp;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author Lakshmi Narayanan
 */
public class SftpClient {

    public void upload(AchSetUp ach, AchProcessHistory history) throws JSchException, SftpException, SQLException, IOException {
        Session session = null;
        Channel channel = null;
        InputStream is = null;
        try {
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            JSch ssh = new JSch();
            byte[] privateKey = ach.getSshPrivateKey();
            byte[] passphrase = ach.getSshPassphrase().getBytes();
            ssh.addIdentity(ach.getFtpUserName(), privateKey, null, passphrase);
            session = ssh.getSession(ach.getFtpUserName(), ach.getFtpHost(), ach.getFtpPort());
            session.setConfig(config);
            session.setPassword(ach.getFtpPassword());
            session.connect();

            channel = session.openChannel("sftp");
            channel.connect();

            ChannelSftp sftp = (ChannelSftp) channel;
            sftp.cd(ach.getFtpDirectory());

            String fileName = FilenameUtils.getName(history.getAchFileName());
            is = history.getAchFile().getBinaryStream();
            sftp.put(is, fileName, ChannelSftp.OVERWRITE);
        } catch (JSchException e) {
            throw e;
        } catch (SftpException e) {
            throw e;
        } catch (SQLException e) {
            throw e;
        } finally {
            if (null != is) {
                is.close();
            }
            if (null != channel && channel.isConnected()) {
                channel.disconnect();
            }
            if (null != session && session.isConnected()) {
                session.disconnect();
            }
        }
    }
}

package com.logiware.security;

import com.gp.cong.common.CommonUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPEncryptedDataList;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPOnePassSignatureList;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPUtil;

/**
 * A simple utility class that decrypts File Contents based on PGP algorithm
 * and RSA keypair.
 * @author Lakshminarayanan
 */
public class FileDecryptor {

    /**
     * Search a secret key ring collection for a secret key corresponding to
     * keyId if it exists.
     *
     * @param privateKeyFile
     * @param keyId
     * @param pass passphrase to decrypt secret key with.
     * @return PGPPrivateKey
     * @throws IOException
     * @throws PGPException
     */
    private static PGPPrivateKey findSecretKey(String privateKeyFile, long keyId, char[] pass) throws Exception {
            InputStream inputStream = new FileInputStream(privateKeyFile);
            PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(PGPUtil.getDecoderStream(inputStream));
            PGPSecretKey pgpSecKey = pgpSec.getSecretKey(keyId);
            if (pgpSecKey == null) {
                return null;
            }
            return pgpSecKey.extractPrivateKey(pass, new BouncyCastleProvider());
    }

    /**
     * Search a secret key ring collection for a secret key corresponding to
     * keyId if it exists.
     *
     * @param fileToDecrypt
     * @param privateKeyFile
     * @param password
     * @param decryptedFile
     * @return Boolean
     * @throws IOException
     * @throws PGPException
     */
    public static Boolean decrypt(String fileToDecrypt, String privateKeyFile, String password, String decryptedFile) throws Exception {
            if (!new File(fileToDecrypt).exists()) {
                return null;
            }
            if (!new File(privateKeyFile).exists()) {
                return null;
            }
            if (!CommonUtils.isNotEmpty(password)) {
                return null;
            }
            InputStream inputStream = PGPUtil.getDecoderStream(new FileInputStream(fileToDecrypt));
            PGPObjectFactory objectFactory = new PGPObjectFactory(inputStream);
            PGPEncryptedDataList encryptedDataList;
            Object obj = objectFactory.nextObject();

            if (obj instanceof PGPEncryptedDataList) {
                encryptedDataList = (PGPEncryptedDataList) obj;
            } else {
                encryptedDataList = (PGPEncryptedDataList) objectFactory.nextObject();
            }

            Iterator encDataObjIterator = encryptedDataList.getEncryptedDataObjects();
            PGPPrivateKey privateKey = null;
            PGPPublicKeyEncryptedData publicKeyEncryptedData = null;

            while (privateKey == null && encDataObjIterator.hasNext()) {
                publicKeyEncryptedData = (PGPPublicKeyEncryptedData) encDataObjIterator.next();
                privateKey = findSecretKey(privateKeyFile, publicKeyEncryptedData.getKeyID(), password.toCharArray());
            }

            if (privateKey == null) {
                return null;
            }

            InputStream decryptStream = publicKeyEncryptedData.getDataStream(privateKey, new BouncyCastleProvider());

            PGPObjectFactory plainFact = new PGPObjectFactory(decryptStream);
            Object message = plainFact.nextObject();

            if (message instanceof PGPCompressedData) {
                PGPCompressedData cData = (PGPCompressedData) message;
                PGPObjectFactory pgpFact = new PGPObjectFactory(cData.getDataStream());
                message = pgpFact.nextObject();
            }

            if (message instanceof PGPLiteralData) {
                PGPLiteralData ld = (PGPLiteralData) message;
//                String outFileName = ld.getFileName();
//                if (ld.getFileName().length() == 0) {
//                    outFileName = decryptedFile;
//                }
                FileOutputStream fos = new FileOutputStream(decryptedFile);
                InputStream decryptedStream = ld.getInputStream();
                int ch;
                while ((ch = decryptedStream.read()) >= 0) {
                    fos.write(ch);
                }
                decryptedStream.close();
                fos.close();
            } else if (message instanceof PGPOnePassSignatureList) {
                throw new PGPException("encrypted message contains a signed message - not literal data.");
            } else {
                throw new PGPException("message is not a simple encrypted file - type unknown.");
            }

            if (publicKeyEncryptedData.isIntegrityProtected()) {
                if (!publicKeyEncryptedData.verify()) {
                    System.err.println("message failed integrity check");
                }
            } else {
                System.err.println("no message integrity check");
            }
            return true;
    }
}

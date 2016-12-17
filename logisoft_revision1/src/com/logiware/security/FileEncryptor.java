package com.logiware.security;

// Java imports
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Iterator;
// Bouncy castle imports
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPUtil;

/**
 * A simple utility class that encrypts File Contents based on PGP algorithm
 * and RSA keypair.
 * @author Lakshminarayanan
 */
public class FileEncryptor {

    /**
     * A simple routine that opens a key ring file and loads the first available key suitable for
     * encryption.
     *
     * @param publicKeyFile
     * @return PGPPublicKey
     * @throws IOException
     * @throws PGPException
     * @author Lakshminarayanan
     */
    private static PGPPublicKey readPublicKey(InputStream inputStream) throws Exception {
	inputStream = PGPUtil.getDecoderStream(inputStream);
	PGPPublicKeyRingCollection pgpPub = new PGPPublicKeyRingCollection(inputStream);
	Iterator keyRingIterator = pgpPub.getKeyRings();
	while (keyRingIterator.hasNext()) {
	    PGPPublicKeyRing keyRing = (PGPPublicKeyRing) keyRingIterator.next();
	    Iterator keyIterator = keyRing.getPublicKeys();
	    while (keyIterator.hasNext()) {
		PGPPublicKey publicKey = (PGPPublicKey) keyIterator.next();
		if (publicKey.isEncryptionKey()) {
		    return publicKey;
		}
	    }
	}
	return null;
    }

    /**
     * A simple routine that opens a key ring file and loads the first available key suitable for
     * encryption.
     *
     * @param fileToEncrypt
     * @param publicKeyStream
     * @param encryptedFile
     * @param armor
     * @param withIntegrityCheck
     * @throws IOException
     * @throws NoSuchProviderException
     * @throws PGPException
     * @author Lakshminarayanan
     */
    public static void encrypt(String fileToEncrypt, InputStream publicKeyStream,
	    String encryptedFile, boolean armor, boolean withIntegrityCheck) throws Exception {
	OutputStream outputStream = null;
	OutputStream encOutputStream = null;
	ByteArrayOutputStream baos = null;
	try {
	    Security.addProvider(new BouncyCastleProvider());
	    PGPPublicKey publicKey = readPublicKey(publicKeyStream);
	    outputStream = new FileOutputStream(encryptedFile);
	    if (armor) {
		outputStream = new ArmoredOutputStream(outputStream);
	    }
	    baos = new ByteArrayOutputStream();
	    PGPCompressedDataGenerator compressedData = new PGPCompressedDataGenerator(PGPCompressedData.ZIP);
	    PGPUtil.writeFileToLiteralData(compressedData.open(baos), PGPLiteralData.BINARY, new File(fileToEncrypt));
	    compressedData.close();
	    PGPEncryptedDataGenerator encryptedData = new PGPEncryptedDataGenerator(PGPEncryptedData.CAST5, withIntegrityCheck, new SecureRandom(), new BouncyCastleProvider());
	    encryptedData.addMethod(publicKey);
	    byte[] bytes = baos.toByteArray();
	    encOutputStream = encryptedData.open(outputStream, bytes.length);
	    encOutputStream.write(bytes);
	} catch (Exception e) {
	    throw e;
	} finally {
	    if (null != encOutputStream) {
		encOutputStream.close();
	    }
	    if (null != baos) {
		baos.close();
	    }
	    if (null != outputStream) {
		outputStream.close();
	    }
	}
    }
}

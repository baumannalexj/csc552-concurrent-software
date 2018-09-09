package ajeffrey.teaching.test;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import java.security.Security;

import com.sun.crypto.provider.SunJCE;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;

import ajeffrey.teaching.io.Pipe;

public class TestCrypto {

    public static void main (String[] args) throws Exception {
	Security.addProvider(new SunJCE());
	final char[] passPhrase = 
	    args[1].toCharArray ();
	final byte[] salt = 
	{
	    (byte)0xc7, (byte)0x73, (byte)0x21, (byte)0x8c,
	    (byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99
	};
	final int count = 
	    20;
	final PBEParameterSpec pbeParameterSpec = 
	    new PBEParameterSpec(salt, count);
	final InputStream in = 
	    new FileInputStream (args[2]);
	final OutputStream out = 
	    new FileOutputStream (args[3]);
	final PBEKeySpec pbeKeySpec = 
	    new PBEKeySpec(passPhrase);
	final SecretKeyFactory keyFac = 
	    SecretKeyFactory.getInstance("PBEWithMD5AndDES");
	final SecretKey pbeKey = 
	    keyFac.generateSecret(pbeKeySpec);
	final Cipher pbeCipher = 
	    Cipher.getInstance("PBEWithMD5AndDES");
	if (args[0].equals ("-encrypt")) {
	    pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParameterSpec);
	    final CipherOutputStream encryptedOut = 
		new CipherOutputStream (out, pbeCipher);
	    final Pipe pipe = Pipe.factory.build (in, encryptedOut);
	    pipe.connect ();
	    encryptedOut.close ();
	} else if (args[0].equals ("-decrypt")) {
	    pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParameterSpec);
	    final CipherInputStream decryptedIn = 
		new CipherInputStream (in, pbeCipher);
	    final Pipe pipe = Pipe.factory.build (decryptedIn, out);
	    pipe.connect ();
	    decryptedIn.close ();
	} else {
	    System.err.println ("Either -encrypt or -decrypt");
	    return;
	}
	in.close ();
	out.close ();
    }

}

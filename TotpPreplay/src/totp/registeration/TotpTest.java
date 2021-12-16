package totp.registeration;

import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class TotpTest {
	
	 /**
	  * This is an example implementation of the OATH
	  * TOTP algorithm.
	  * Visit www.openauthentication.org for more information.
	  *
	  * @author Johan Rydell, PortWise, Inc.
	  */

	 public class TOTP {

	     private TOTP() {}

	     /**
	      * This method uses the JCE to provide the crypto algorithm.
	      * HMAC computes a Hashed Message Authentication Code with the
	      * crypto hash algorithm as a parameter.
	      *
	      * @param crypto: the crypto algorithm (HmacSHA1, HmacSHA256,
	      *                             HmacSHA512)
	      * @param keyBytes: the bytes to use for the HMAC key
	      * @param text: the message or text to be authenticated
	      */
	     private static byte[] hmac_sha(String crypto, byte[] keyBytes,
	             byte[] text){
	         try {
	             Mac hmac;
	             hmac = Mac.getInstance(crypto);
	             SecretKeySpec macKey =
	                 new SecretKeySpec(keyBytes, "RAW");
	             hmac.init(macKey);
	             return hmac.doFinal(text);
	         } catch (GeneralSecurityException gse) {
	             throw new UndeclaredThrowableException(gse);
	         }
	     }


	     /**
	      * This method converts a HEX string to Byte[]
	      *
	      * @param hex: the HEX string
	      *
	      * @return: a byte array
	      */

	     private static byte[] hexStr2Bytes(String hex){
	         // Adding one byte to get the right conversion
	         // Values starting with "0" can be converted
	         byte[] bArray = new BigInteger("10" + hex,16).toByteArray();

	         // Copy all the REAL bytes, not the "first"
	         byte[] ret = new byte[bArray.length - 1];
	         for (int i = 0; i < ret.length; i++)
	             ret[i] = bArray[i+1];
	         return ret;
	     }

	     private static final int[] DIGITS_POWER
	     // 0 1  2   3    4     5      6       7        8
	     = {1,10,100,1000,10000,100000,1000000,10000000,100000000 };

	     /**
	      * This method generates a TOTP value for the given
	      * set of parameters.
	      *
	      * @param key: the shared secret, HEX encoded
	      * @param time: a value that reflects a time
	      * @param returnDigits: number of digits to return
	      *
	      * @return: a numeric String in base 10 that includes
	      *              {@link truncationDigits} digits
	      */

	     public static String generateTOTP(String key,
	             String time,
	             String returnDigits){
	         return generateTOTP(key, time, returnDigits, "HmacSHA1");
	     }


	     /**
	      * This method generates a TOTP value for the given
	      * set of parameters.
	      *
	      * @param key: the shared secret, HEX encoded
	      * @param time: a value that reflects a time
	      * @param returnDigits: number of digits to return
	      *
	      * @return: a numeric String in base 10 that includes
	      *              {@link truncationDigits} digits
	      */

	     public static String generateTOTP256(String key,
	             String time,
	             String returnDigits){
	         return generateTOTP(key, time, returnDigits, "HmacSHA256");
	     }


}

package totp.registeration;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Scanner;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import totp.registeration.ModularArithmeticV2;

public class GenerateToken {

	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA256";
	
	public static String toHex(byte[] bytes)  {
		Formatter formatter = new Formatter();
		for(byte b : bytes) {
			formatter.format("%02x", b);		
		}
		return formatter.toString();
	}
	
	
	public static String calculateMac(String data, String key) throws Exception {
		
		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "RAW");
		Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
		mac.init(signingKey);
		return toHex(mac.doFinal(data.getBytes()));
	}
	
	public static String calculateOtp(String key, Long time) throws Exception{

		String hexTime = Long.toHexString(time);
		
		key = toHex(key.getBytes());
		
		String mac = calculateMac(hexTime, key);
		
//		1f|86|98|69|0e|02|ca|16|61|85|50|ef|7f|19|da|8e|94|5b|55|5a
//		String mac = "5a555b948eda197fef50856116ca020e6998861f";
		
		String lastByte = mac.substring(0,2);
		String lastFourBits = lastByte.substring(1,2);
		long offset = Long.parseLong(lastFourBits, 16);
		
		List<String> macNumbers = new ArrayList<String>();
		for(int i=mac.length(); i>0; i-=2 ) {
			String bytes = mac.substring(i-2,i);
			macNumbers.add(bytes);
		}
		
		String otp = "";
		for(int i = 0; i<4; i++) {
			otp += macNumbers.get((int)offset+i);
		}
		
		String otpLong = Long.parseLong(otp,16)+"";
	   return ModularArithmeticV2.mod(new BigInteger(otpLong), new BigInteger("1000000")).toString();
	}
	
	
	public static void main(String[] args) throws Exception {
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter Key :: ");
		String key = sc.next();
		
		Long time = System.currentTimeMillis();
		
		for(int i=0; i<=5; i++) {
			String otp = calculateOtp(key, time);
			System.out.println(otp);
			time += 30000;
		}
		
	}
}

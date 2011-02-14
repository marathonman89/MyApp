package sms.index;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.wicket.markup.html.WebPage;


public class Index extends WebPage{
	public Index(){}
	
	public String encPassword(String password){
		MessageDigest mdEnc = null;
		try {
			mdEnc = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		mdEnc.update(password.getBytes(), 0, password.length());
		String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
		return md5;
	}
}


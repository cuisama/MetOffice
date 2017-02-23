package com.weather.framework.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

//import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 编码工具类
 * 实现aes加密、解密
 */
public class EncryptUtils {
	
	/**
	 * 密钥
	 */
	private static final String KEY = Md5("123456");
	
	/**
	 * 算法
	 */
	private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

	public static void main(String[] args) throws Exception {
		String content = "我爱你";
		System.out.println("加密前：" + content);

		System.out.println("加密密钥和解密密钥：" + KEY);

		String encrypt = aesEncrypt(content, KEY);
		System.out.println("加密后：" + encrypt);

		String decrypt = aesDecrypt(encrypt, KEY);
		System.out.println("解密后：" + decrypt);
		
		System.out.println(getRandomString(8));
		
	}
	/**
	 * 生成随机字符串
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length) { //length表示生成字符串的长度  
	    String base = "abcdefghijklmnopqrstuvwxyz0123456789";     
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < length; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    }     
	    return sb.toString();     
	 }  
	
	// md5加密
	public static String Md5(String sourceStr) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sourceStr.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			// result = buf.toString();
			// System.out.println("result: " + result);//32位的加密
			result = buf.toString().substring(8, 24);
			System.out.println("result: " + buf.toString().substring(8,24));//16位的加密
		} catch (NoSuchAlgorithmException e) {

		}                    
		return result;
	}
	
	/**
	 * aes解密
	 * @param encrypt	内容
	 * @return
	 * @throws Exception
	 */
	public static String aesDecrypt(String encrypt) throws Exception {
		return aesDecrypt(encrypt, KEY);
	}
	
	/**
	 * aes加密
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static String aesEncrypt(String content) throws Exception {
		return aesEncrypt(content, KEY);
	}

	/**
	 * 将byte[]转为各种进制的字符串
	 * @param bytes byte[]
	 * @param radix 可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
	 * @return 转换后的字符串
	 */
	public static String binary(byte[] bytes, int radix){
		return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
	}

	/**
	 * base 64 encode
	 * @param bytes 待编码的byte[]
	 * @return 编码后的base 64 code
	 */
	public static String base64Encode(byte[] bytes){
		//return Base64.encodeBase64String(bytes);
		return new BASE64Encoder().encode(bytes);
	}

	/**
	 * base 64 decode
	 * @param base64Code 待解码的base 64 code
	 * @return 解码后的byte[]
	 * @throws Exception
	 */
	public static byte[] base64Decode(String base64Code) throws Exception{
		return StringUtils.isEmpty(base64Code) ? null : new BASE64Decoder().decodeBuffer(base64Code);
	}

	
	/**
	 * AES加密
	 * @param content 待加密的内容
	 * @param encryptKey 加密密钥
	 * @return 加密后的byte[]
	 * @throws Exception
	 */
	public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(256);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));

        return cipher.doFinal(content.getBytes("utf-8"));
    }


	/**
	 * AES加密为base 64 code
	 * @param content 待加密的内容
	 * @param encryptKey 加密密钥
	 * @return 加密后的base 64 code
	 * @throws Exception
	 */
	public static String aesEncrypt(String content, String encryptKey) throws Exception {
		return base64Encode(aesEncryptToBytes(content, encryptKey));
	}

	/**
	 * AES解密
	 * @param encryptBytes 待解密的byte[]
	 * @param decryptKey 解密密钥
	 * @return 解密后的String
	 * @throws Exception
	 */
	 public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
	        KeyGenerator kgen = KeyGenerator.getInstance("AES");
	        kgen.init(128);

	        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
	        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
	        byte[] decryptBytes = cipher.doFinal(encryptBytes);

	        return new String(decryptBytes,"utf-8");
	    }


	/**
	 * 将base 64 code AES解密
	 * @param encryptStr 待解密的base 64 code
	 * @param decryptKey 解密密钥
	 * @return 解密后的string
	 * @throws Exception
	 */
	public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
		return StringUtils.isEmpty(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
	}

}

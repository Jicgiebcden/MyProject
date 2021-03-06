package com.alipay.rsa.sign;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;

import javax.crypto.Cipher;

/**
 * java中RSA加解密的实现（网上找到的）
 * @author huangym3
 * @time 2016年7月26日 上午10:07:26
 */
public class RSAUtils {

	/**
	 * 生成公钥和私钥
	 * 
	 * @throws NoSuchAlgorithmException
	 * 
	 */
	public static HashMap<String, Object> getKeys()
			throws NoSuchAlgorithmException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		map.put("public", publicKey);
		map.put("private", privateKey);
		return map;
	}

	/**
	 * 使用模和指数生成RSA公钥
	 * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
	 * /None/NoPadding】
	 * 
	 * @param modulus
	 *            模
	 * @param exponent
	 *            指数
	 * @return
	 */
	public static RSAPublicKey getPublicKey(String modulus, String exponent) {
		try {
			BigInteger b1 = new BigInteger(modulus);
			BigInteger b2 = new BigInteger(exponent);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 使用模和指数生成RSA私钥
	 * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
	 * /None/NoPadding】
	 * 
	 * @param modulus
	 *            模
	 * @param exponent
	 *            指数
	 * @return
	 */
	public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {
		try {
			BigInteger b1 = new BigInteger(modulus);
			BigInteger b2 = new BigInteger(exponent);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 公钥加密
	 * 不管明文长度是多少，RSA 生成的密文长度总是固定的。但是明文长度不能超过密钥长度。比如 Java 默认的 RSA 加密实现不允许明文长度超过密钥长度减去 11(单位是字节，也就是 byte)。
	 * 也就是说，如果我们定义的密钥(我们可以通过 java.security.KeyPairGenerator.initialize(int keysize) 来定义密钥长度)长度为 1024(单位是位，也就是 bit)，生成的密钥长度就是 1024位 / 8位/字节 = 128字节，
	 * 那么我们需要加密的明文长度不能超过 128字节 - 11 字节 = 117字节。也就是说，我们最大能将 117 字节长度的明文进行加密，否则会出问题(抛诸如 javax.crypto.IllegalBlockSizeException: Data must not be longer than 53 bytes 的异常)。
	 * @param data
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static String encryptByPublicKey(String data, RSAPublicKey publicKey)
			throws Exception {
//		Cipher cipher = Cipher.getInstance("RSA");
//		Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		// 模长
		int key_len = publicKey.getModulus().bitLength() / 8;
		// 加密数据长度 <= 模长-11
		String[] datas = splitString(data, key_len - 11);
		String mi = "";
		// 如果明文长度大于模长-11则要分组加密
		for (String s : datas) {
			mi += bcd2Str(cipher.doFinal(s.getBytes()));
		}
		return mi;
	}

	/**
	 * 私钥解密
	 * 
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static String decryptByPrivateKey(String data,
			RSAPrivateKey privateKey) throws Exception {
//		Cipher cipher = Cipher.getInstance("RSA");
//		Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		// 模长
		int key_len = privateKey.getModulus().bitLength() / 8;
		byte[] bytes = data.getBytes();
		byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
		System.err.println(bcd.length);
		// 如果密文长度大于模长则要分组解密
		String ming = "";
		byte[][] arrays = splitArray(bcd, key_len);
		for (byte[] arr : arrays) {
			ming += new String(cipher.doFinal(arr));
		}
		return ming;
	}

	/**
	 * ASCII码转BCD码
	 * 
	 */
	public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
		byte[] bcd = new byte[asc_len / 2];
		int j = 0;
		for (int i = 0; i < (asc_len + 1) / 2; i++) {
			bcd[i] = asc_to_bcd(ascii[j++]);
			bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
		}
		return bcd;
	}

	public static byte asc_to_bcd(byte asc) {
		byte bcd;

		if ((asc >= '0') && (asc <= '9'))
			bcd = (byte) (asc - '0');
		else if ((asc >= 'A') && (asc <= 'F'))
			bcd = (byte) (asc - 'A' + 10);
		else if ((asc >= 'a') && (asc <= 'f'))
			bcd = (byte) (asc - 'a' + 10);
		else
			bcd = (byte) (asc - 48);
		return bcd;
	}

	/**
	 * BCD转字符串
	 */
	public static String bcd2Str(byte[] bytes) {
		char temp[] = new char[bytes.length * 2], val;

		for (int i = 0; i < bytes.length; i++) {
			val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
			temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

			val = (char) (bytes[i] & 0x0f);
			temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
		}
		return new String(temp);
	}

	/**
	 * 拆分字符串
	 */
	public static String[] splitString(String string, int len) {
		int x = string.length() / len;
		int y = string.length() % len;
		int z = 0;
		if (y != 0) {
			z = 1;
		}
		String[] strings = new String[x + z];
		String str = "";
		for (int i = 0; i < x + z; i++) {
			if (i == x + z - 1 && y != 0) {
				str = string.substring(i * len, i * len + y);
			} else {
				str = string.substring(i * len, i * len + len);
			}
			strings[i] = str;
		}
		return strings;
	}

	/**
	 * 拆分数组
	 */
	public static byte[][] splitArray(byte[] data, int len) {
		int x = data.length / len;
		int y = data.length % len;
		int z = 0;
		if (y != 0) {
			z = 1;
		}
		byte[][] arrays = new byte[x + z][];
		byte[] arr;
		for (int i = 0; i < x + z; i++) {
			arr = new byte[len];
			if (i == x + z - 1 && y != 0) {
				System.arraycopy(data, i * len, arr, 0, y);
			} else {
				System.arraycopy(data, i * len, arr, 0, len);
			}
			arrays[i] = arr;
		}
		return arrays;
	}

	public static void main(String[] args) throws Exception {
		String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALyoPoQe4RxORKZLoM9aNCbbmoZhNINuF/el2LDrmZdQOFD7EKWQH+pdKcC99b8uEpivPkIL/iqK3oquhaK8wZOH4k7SvTBCHBEbNjUedTAYA7DdO76PCpepAaVKR29Vxaq0+OfYmeMn8lXPGnjNV0X6WSdOTIwiGiBaIlAckUItAgMBAAECgYBYMJ3ozTaWMNBOnJPz2kr+zzGRF+egeNqjXdfJ/Jiz70AkOvkxLAp/WEWoYCR81mDDF0iXP1v1Ly2QzryRsoMaUlgCx5Pc6fpcLWR+XFnQiUwJCM55SIQ/L8OmV3pxpHpmU8avwkD5VEvafylU8qX53Av4QzY7o9JfT3eZseNrgQJBAOlzA/vl5h6otfNqGt1FhXpUEixNsJr3yMpsVmm3Kn5lWbgnkHu4GtIMklhl8hkptnfSbwAboEWdIyK3TsX/9+kCQQDO4Y/l7UdqatkipO5LR2yNvNYrTUwqTxvYtdASMVVUJoBmKfzOr+IHTlwOSaqaJitiyngjBNf9ZRB0KOoyIBGlAkBGqjgkgfzcfvlEy3OCU08KLnSp0IKLBkJTc+PccEN+qzMzWVaU/K+xsXiDJajZERVYw+wxvisPp3dBRPelW8wxAkEAgm9ksqrvpADJ9jXWqE/X+WVeAt8xbluCSxgWn+HK0suTmNZQNmBeOg0FrVx028vE/vx40ltzcqgNv68sOX5OoQJAUVZ0gcWQdOcdJFfF+4VhyDus1tsMgTJU2tN+EttIiuLO6P1ozPJCrXK6oio8mq8NYpwB80xy4xwi+a+AD3/D7w==";
		String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8qD6EHuEcTkSmS6DPWjQm25qGYTSDbhf3pdiw65mXUDhQ+xClkB/qXSnAvfW/LhKYrz5CC/4qit6KroWivMGTh+JO0r0wQhwRGzY1HnUwGAOw3Tu+jwqXqQGlSkdvVcWqtPjn2JnjJ/JVzxp4zVdF+lknTkyMIhogWiJQHJFCLQIDAQAB";

		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		byte[] encodedKey = Base64.decode(publicKey);
		RSAPublicKey pubKey = (RSAPublicKey) keyFactory
				.generatePublic(new X509EncodedKeySpec(encodedKey));

		KeyFactory factory = KeyFactory.getInstance("RSA");
		PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
				Base64.decode(privateKey));
		RSAPrivateKey priKey = (RSAPrivateKey) factory
				.generatePrivate(priPKCS8);

		// 明文
		String content = "123456789";
		// 加密后的密文
		String mi = encryptByPublicKey(content, pubKey);
		System.err.println(mi);
		// 解密后的明文
		String ming = decryptByPrivateKey(mi, priKey);
		System.err.println(ming);
		
		// 加密后的密文
		String mi1 = encryptByPublicKey(content, pubKey);
		System.err.println(mi1);
		// 解密后的明文
		String ming1 = decryptByPrivateKey(mi1, priKey);
		System.err.println(ming1);
	}
}
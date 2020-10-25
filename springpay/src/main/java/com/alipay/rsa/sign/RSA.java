
package com.alipay.rsa.sign;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import com.alipay.rsa.config.AlipayConfig;

/*
Java 进行 RSA 加解密时不得不考虑到的那些事儿
来源：http://blog.csdn.net/defonds/article/details/42775183
1. 加密的系统不要具备解密的功能，否则 RSA 可能不太合适
公钥加密，私钥解密。加密的系统和解密的系统分开部署，加密的系统不应该同时具备解密的功能，这样即使黑客攻破了加密系统，他拿到的也只是一堆无法破解的密文数据。否则的话，你就要考虑你的场景是否有必要用 RSA 了。
2. 可以通过修改生成密钥的长度来调整密文长度
生成密文的长度等于密钥长度。密钥长度越大，生成密文的长度也就越大，加密的速度也就越慢，而密文也就越难被破解掉。著名的"安全和效率总是一把双刃剑"定律，在这里展现的淋漓尽致。我们必须通过定义密钥的长度在"安全"和"加解密效率"之间做出一个平衡的选择。
3. 生成密文的长度和明文长度无关，但明文长度不能超过密钥长度
不管明文长度是多少，RSA 生成的密文长度总是固定的。
但是明文长度不能超过密钥长度。比如 Java 默认的 RSA 加密实现不允许明文长度超过密钥长度减去 11(单位是字节，也就是 byte)。也就是说，如果我们定义的密钥(我们可以通过 java.security.KeyPairGenerator.initialize(int keysize) 来定义密钥长度)长度为 1024(单位是位，也就是 bit)，生成的密钥长度就是 1024位 / 8位/字节 = 128字节，那么我们需要加密的明文长度不能超过 128字节 -
11 字节 = 117字节。也就是说，我们最大能将 117 字节长度的明文进行加密，否则会出问题(抛诸如 javax.crypto.IllegalBlockSizeException: Data must not be longer than 53 bytes 的异常)。
而 BC 提供的加密算法能够支持到的 RSA 明文长度最长为密钥长度。
4. byte[].toString() 返回的实际上是内存地址，不是将数组的实际内容转换为 String
警惕 toString 陷阱：Java 中数组的 toString() 方法返回的并非数组内容，它返回的实际上是数组存储元素的类型以及数组在内存的位置的一个标识。
这些输出其实都是字节数组在内存的位置的一个标识，而不是作者所认为的字节数组转换成的字符串内容。如果我们对密钥以 byte[].toString() 进行持久化存储或者和其他一些字符串打 json 传输，那么密钥的解密者得到的将只是一串毫无意义的字符，当他解码的时候很可能会遇到 "javax.crypto.BadPaddingException" 异常。
5. 字符串用以保存文本信息，字节数组用以保存二进制数据
java.lang.String 保存明文，byte 数组保存二进制密文，在 java.lang.String 和 byte[] 之间不应该具备互相转换。如果你确实必须得使用 java.lang.String 来持有这些二进制数据的话，最安全的方式是使用 Base64(推荐 Apache 的 commons-codec 库的 org.apache.commons.codec.binary.Base64)
6. 每次生成的密文都不一致证明你选用的加密算法很安全
一个优秀的加密必须每次生成的密文都不一致，即使每次你的明文一样、使用同一个公钥。因为这样才能把明文信息更安全地隐藏起来。
Java 默认的 RSA 实现是 "RSA/None/PKCS1Padding"(比如 Cipher cipher = Cipher.getInstance("RSA");句，这个 Cipher 生成的密文总是不一致的)，Bouncy Castle 的默认 RSA 实现是 "RSA/None/NoPadding"。
为什么 Java 默认的 RSA 实现每次生成的密文都不一致呢，即使每次使用同一个明文、同一个公钥？这是因为 RSA 的 PKCS #1 padding 方案在加密前对明文信息进行了随机数填充。
你可以使用以下办法让同一个明文、同一个公钥每次生成同一个密文，但是你必须意识到你这么做付出的代价是什么。比如，你可能使用 RSA 来加密传输，但是由于你的同一明文每次生成的同一密文，攻击者能够据此识别到同一个信息都是何时被发送。
Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
final Cipher cipher = Cipher.getInstance("RSA/None/NoPadding", "BC");
7. 可以通过调整算法提供者来减小密文长度
Java 默认的 RSA 实现 "RSA/None/PKCS1Padding" 要求最小密钥长度为 512 位(否则会报 java.security.InvalidParameterException: RSA keys must be at least 512 bits long 异常)，也就是说生成的密钥、密文长度最小为 64 个字节。如果你还嫌大，可以通过调整算法提供者来减小密文长度：
Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
final KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "BC");
keyGen.initialize(128);
8. Cipher 是有状态的，而且是线程不安全的
javax.crypto.Cipher 是有状态的，不要把 Cipher 当做一个静态变量，除非你的程序是单线程的，也就是说你能够保证同一时刻只有一个线程在调用 Cipher。否则你可能会像笔者似的遇到 java.lang.ArrayIndexOutOfBoundsException: too much data for RSA block 异常。遇见这个异常，你需要先确定你给 Cipher 加密的明文(或者需要解密的密文)是否过长；排除掉明文(或者密文)过长的情况，你需要考虑是不是你的 Cipher 线程不安全了。
*/

/**
 * 加解密
 * 使用原则：私钥签名、公钥验签；公钥加密、私钥解密。
 * @author huangym3
 * @time 2016年7月26日 上午11:40:37
 */
public class RSA{
	
	public static final String  SIGN_ALGORITHMS = "SHA1WithRSA";
	
	/**
	* RSA签名
	* @param content 待签名数据
	* @param privateKey 商户私钥
	* @param input_charset 编码格式
	* @return 签名值
	*/
	public static String sign(String content, String privateKey, String input_charset)
	{
        try 
        {
        	PKCS8EncodedKeySpec priPKCS8 	= new PKCS8EncodedKeySpec( Base64.decode(privateKey) ); 
        	KeyFactory keyf 				= KeyFactory.getInstance("RSA");
        	PrivateKey priKey 				= keyf.generatePrivate(priPKCS8);

            java.security.Signature signature = java.security.Signature
                .getInstance(SIGN_ALGORITHMS);

            signature.initSign(priKey);
            signature.update( content.getBytes(input_charset) );

            byte[] signed = signature.sign();
            
            return Base64.encode(signed);
        }
        catch (Exception e) 
        {
        	e.printStackTrace();
        }
        
        return null;
    }
	
	/**
	* RSA验签名检查
	* @param content 待签名数据
	* @param sign 签名值
	* @param ali_public_key 支付宝公钥
	* @param input_charset 编码格式
	* @return 布尔值
	*/
	public static boolean verify(String content, String sign, String ali_public_key, String input_charset)
	{
		try 
		{
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        byte[] encodedKey = Base64.decode(ali_public_key);
	        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

		
			java.security.Signature signature = java.security.Signature
			.getInstance(SIGN_ALGORITHMS);
		
			signature.initVerify(pubKey);
			signature.update( content.getBytes(input_charset) );
		
			boolean bverify = signature.verify( Base64.decode(sign) );
			return bverify;
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	* 私钥解密
	* @param content 经过base64编码的密文
	* @param private_key 商户私钥
	* @param input_charset 编码格式
	* @return 解密后的字符串
	*/
	public static String privateKeyDecrypt(String content, String private_key, String input_charset) throws Exception {
        PrivateKey prikey = getPrivateKey(private_key);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, prikey);

        InputStream ins = new ByteArrayInputStream(Base64.decode(content));
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        //rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
        byte[] buf = new byte[128];
        int bufl;

        while ((bufl = ins.read(buf)) != -1) {
            byte[] block = null;

            if (buf.length == bufl) {
                block = buf;
            } else {
                block = new byte[bufl];
                for (int i = 0; i < bufl; i++) {
                    block[i] = buf[i];
                }
            }

            writer.write(cipher.doFinal(block));
        }

        return new String(writer.toByteArray(), input_charset);
    }

	/**
	 * 公钥加密
	 * @param content 待加密的原文
	 * @param public_key 平台公钥
	 * @param input_charset 编码格式
	 * @return 经过base64编码的密文
	 * @throws Exception
	 */
	public static String publicKeyEncrypt(String content, String public_key,
			String input_charset) throws Exception {
		PublicKey pubkey = getPublicKey(public_key);
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, pubkey);

		InputStream ins = new ByteArrayInputStream(content.getBytes(input_charset));
		ByteArrayOutputStream writer = new ByteArrayOutputStream();
		// 加密的明文长度不能超过 128字节 - 11 字节 = 117字节
		byte[] buf = new byte[117];
		int bufl;

		while ((bufl = ins.read(buf)) != -1) {
			byte[] block = null;

			if (buf.length == bufl) {
				block = buf;
			} else {
				block = new byte[bufl];
				for (int i = 0; i < bufl; i++) {
					block[i] = buf[i];
				}
			}
			writer.write(cipher.doFinal(block));
		}

		return Base64.encode(writer.toByteArray());
	}
	
	/**
	 * 私钥加密
	 * @param content 待加密的原文
	 * @param private_key 商户私钥
	 * @param input_charset 编码格式
	 * @return 经过base64编码的密文
	 * @throws Exception
	 */
	public static String privateKeyEncrypt(String content, String private_key, String input_charset) throws Exception {
		PrivateKey prikey = getPrivateKey(private_key);
		Cipher cipher = Cipher.getInstance("RSA");// RSA/None/PKCS1Padding
//		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");// 效果等同RSA，每次生成的密文都会变
//		Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");// 这种不填充的方式每次生成的密文都不会变，但是解密后得到的明文前头有很多为0的字节
		cipher.init(Cipher.ENCRYPT_MODE, prikey);
		
		InputStream ins = new ByteArrayInputStream(content.getBytes(input_charset));
		ByteArrayOutputStream writer = new ByteArrayOutputStream();
		// 加密的明文长度不能超过 128字节 - 11 字节 = 117字节
		byte[] buf = new byte[117];
		int bufl;
		
		while ((bufl = ins.read(buf)) != -1) {
			byte[] block = null;

			if (buf.length == bufl) {
				block = buf;
			} else {
				block = new byte[bufl];
				for (int i = 0; i < bufl; i++) {
					block[i] = buf[i];
				}
			}
			writer.write(cipher.doFinal(block));
		}
		
		return Base64.encode(writer.toByteArray());
	}
	
	/**
	 * 公钥解密
	 * @param content 经过base64编码的密文
	 * @param public_key 平台公钥
	 * @param input_charset 编码格式
	 * @return 解密后的字符串
	 * @throws Exception
	 */
	public static String publicKeyDecrypt(String content, String public_key, String input_charset) throws Exception {
        PublicKey pubkey = getPublicKey(public_key);
		Cipher cipher = Cipher.getInstance("RSA");// RSA/None/PKCS1Padding
//		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");// 效果等同RSA，每次生成的密文都会变
//		Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");// 这种不填充的方式每次生成的密文都不会变，但是解密后得到的明文前头有很多为0的字节
        cipher.init(Cipher.DECRYPT_MODE, pubkey);

        InputStream ins = new ByteArrayInputStream(Base64.decode(content));
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        //rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
        byte[] buf = new byte[128];
        int bufl;

        while ((bufl = ins.read(buf)) != -1) {
            byte[] block = null;

            if (buf.length == bufl) {
                block = buf;
            } else {
                block = new byte[bufl];
                for (int i = 0; i < bufl; i++) {
                    block[i] = buf[i];
                }
            }

            writer.write(cipher.doFinal(block));
        }

        return new String(writer.toByteArray(), input_charset);
	}
	
	/**
	* 得到私钥
	* @param key 密钥字符串（经过base64编码）
	* @throws Exception
	*/
	public static PrivateKey getPrivateKey(String key) throws Exception {

		byte[] keyBytes;
		
		keyBytes = Base64.decode(key);
		
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		
		return privateKey;
	}
	
	/**
	 * 得到公钥
	 * @param key 密钥字符串（经过base64编码）
	 * @return
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(String key) throws Exception {
		
		byte[] keyBytes;
		
		keyBytes = Base64.decode(key);
		
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		
		PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
		
		return publicKey;
	}
	
	public static void main(String[] args) throws Exception {
		String str = "huangyaoming is abcdefg huangyaoming is abcdefg huangyaoming is abcdefg huangyaoming is abcdefg huangyaoming is abcdefg huangyaoming is abcdefg ";
		String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALyoPoQe4RxORKZLoM9aNCbbmoZhNINuF/el2LDrmZdQOFD7EKWQH+pdKcC99b8uEpivPkIL/iqK3oquhaK8wZOH4k7SvTBCHBEbNjUedTAYA7DdO76PCpepAaVKR29Vxaq0+OfYmeMn8lXPGnjNV0X6WSdOTIwiGiBaIlAckUItAgMBAAECgYBYMJ3ozTaWMNBOnJPz2kr+zzGRF+egeNqjXdfJ/Jiz70AkOvkxLAp/WEWoYCR81mDDF0iXP1v1Ly2QzryRsoMaUlgCx5Pc6fpcLWR+XFnQiUwJCM55SIQ/L8OmV3pxpHpmU8avwkD5VEvafylU8qX53Av4QzY7o9JfT3eZseNrgQJBAOlzA/vl5h6otfNqGt1FhXpUEixNsJr3yMpsVmm3Kn5lWbgnkHu4GtIMklhl8hkptnfSbwAboEWdIyK3TsX/9+kCQQDO4Y/l7UdqatkipO5LR2yNvNYrTUwqTxvYtdASMVVUJoBmKfzOr+IHTlwOSaqaJitiyngjBNf9ZRB0KOoyIBGlAkBGqjgkgfzcfvlEy3OCU08KLnSp0IKLBkJTc+PccEN+qzMzWVaU/K+xsXiDJajZERVYw+wxvisPp3dBRPelW8wxAkEAgm9ksqrvpADJ9jXWqE/X+WVeAt8xbluCSxgWn+HK0suTmNZQNmBeOg0FrVx028vE/vx40ltzcqgNv68sOX5OoQJAUVZ0gcWQdOcdJFfF+4VhyDus1tsMgTJU2tN+EttIiuLO6P1ozPJCrXK6oio8mq8NYpwB80xy4xwi+a+AD3/D7w==";
		String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8qD6EHuEcTkSmS6DPWjQm25qGYTSDbhf3pdiw65mXUDhQ+xClkB/qXSnAvfW/LhKYrz5CC/4qit6KroWivMGTh+JO0r0wQhwRGzY1HnUwGAOw3Tu+jwqXqQGlSkdvVcWqtPjn2JnjJ/JVzxp4zVdF+lknTkyMIhogWiJQHJFCLQIDAQAB";
		
		System.out.println("私钥签名，公钥验签-----------------------------");
		String sign = sign(str, privateKey, AlipayConfig.input_charset);
		System.out.println(sign);
		System.out.println(verify(str, sign, publicKey, AlipayConfig.input_charset));
		
		System.out.println("公钥加密，私钥解密-----------------------------");// 公钥加密、私钥解密时，加密得到的密文每次都会变
		String encryptStr = publicKeyEncrypt(str, publicKey, AlipayConfig.input_charset);
		System.out.println(encryptStr);
		System.out.println(privateKeyDecrypt(encryptStr, privateKey, AlipayConfig.input_charset));
		String encryptStr1 = publicKeyEncrypt(str, publicKey, AlipayConfig.input_charset);
		System.out.println(encryptStr1);
		System.out.println(privateKeyDecrypt(encryptStr1, privateKey, AlipayConfig.input_charset));
		
		System.out.println("私钥加密，公钥解密-----------------------------");// 私钥加密、公钥解密时，加密得到的密文不会变
		String encryptStr2 = privateKeyEncrypt(str, privateKey, AlipayConfig.input_charset);
		System.out.println(encryptStr2);
		System.out.println(publicKeyDecrypt(encryptStr2, publicKey, AlipayConfig.input_charset));
		String encryptStr22 = privateKeyEncrypt(str, privateKey, AlipayConfig.input_charset);
		System.out.println(encryptStr22);
		System.out.println(publicKeyDecrypt(encryptStr22, publicKey, AlipayConfig.input_charset));
		
	}
}

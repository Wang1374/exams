package com.tanmu.util;
 
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
 
/**
 * @ClassName: RsaUtil
 * @Description: RSA加解密工具类
 */
public class RsaUtil {
    private static final int DEFAULT_RSA_KEY_SIZE = 2048;
 
    private static final String KEY_ALGORITHM = "RSA";
 
    public static void main(String [] args){
//        Map<String,String> result = generateRsaKey(DEFAULT_RSA_KEY_SIZE);
//        System.out.println("公钥为：" + result.get("publicKey") );
//        System.out.println("私钥为：" + result.get("privateKey"));
        //解密测试

     String aa=   decrypt(
             "SnM2QBQKmR5mtrb6+95668aWbBxGtV3j34VnUEnDKCVxtswpHXBbWwwWxFFX3XQQOOUUsIW4nG1gTMQVsOXg5Mz8E5RbL6yjq+3WdbO9u1oEhrvfjGb8ueB8sz+k7dlJbbuJYU8/pK7pjB1wQiNdaTMgvbkJmoKwkRYow14KuO5fvpTYj3trRlMYWvWRiIqIsnlKd18NcrEaHV778G0RtlXcUaNTYkiq22aL424ivM7R068SZyjwvS3u7W5cIdFIl6rOfxKToh56NvLeTBxEf7Bb+iaSAiBi7KO3y8kOMaA+SA8Np+R0OyBJTJtk4AdZVMZWBWPHf1ml6BylOu/q7w=="
             ,"MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCgqq00Lz61hY0BQc30MQ2yTNGKrgBjyB5jwmsVdbDQDqUUJsuJ78pygPvxknxAWlBaXHl8fLm24H7cZ90Hzb0JgrwWWOYSWyq8iPmrg1AacGtJT1RRm7NEDzlixDl8GbAllOL2ViD5yEgWRfAoiBbbY/Ja7iwiPAp0s/6rQQkFBFgnhH9hOtXIbweZ7UMS3/iIP7zzmaOBja/vJjPk2aEJXixkLSxkUycE+J0uznBLj0lnj9i4q4zC3BFPav3+jexXpU01Rm1VgHPx+XlRFC0njLEjeG/24kDO6U6dczLxRjAFUbRvSwTgTEX7KVZea5TApu6ln1qyOwck9lMZRTC1AgMBAAECggEAHqHL2goz64nl41p3eNotYVQyEz2vh1atLJnxrqq8pj5UjO4EmmzkbUZYQquKEv7lGQVOiESDZ/GPXlhTFNzN8NVDJQOYAeHt2PpIl8G2cQQEAHsmeVS1LgG1EDaXOyjuo4uqZgrvyI+95rk425Fuwi9hEqMcOP63g56ZrG7+2Dy5d+qA2QNI6auZFCkfJGSmPWzn6ZBLt7aOG535+AW+M76UlyLHeNLX9NzG44A78dFY6CvcnpKfEKxwROMnPdmQ1zEKPszNVJPrcA07FGKOvQu6g2uybsfMM6HcD5tVQBgooTKe8iX/afG6DjumbZXQIMzzYZPobEf54n/bqQi8AQKBgQDMZPzl8+fI8ZDhVgir62eoy5Jk9fyR8Q+iqjpkMVRpgbvuInrnlslP1lPbwlvljQ400RkxkmnIjLO7r+XM1EfC06EuRgzuF3NredGQ3TxHlyyeBk9WEv7XDJ9SCKZ5eMYDL+B2KQCAD9mMQYU+/6t1xB43ZHLncL1jVF9enq3FAQKBgQDJO1lh/vOonKu7sMiY7RU3s+z5HucQ8xizTVHPX5tMcDMg/BximdHcbuZno38nUFqMOmouDiho1AI/gnrXxmsmUxJ+nhxhcdxFVyK+B4JbIiOkva5mfFtUtXEQXQCVQmIK6yJcWyD1DL31SQC+8DD0lXTRbXvSS/O6/2kUIqXntQKBgQClFRu9jpWykoj1tssiL+t1RZZjvNnV9eEu/FZU/hKzdHkZCPhDfGHz/aOVZxFaAedreUbj5uFRfXLdMGBmYz3ieZNBEIr48i/iqm08l7NMDymWKHqDhqwVfLhhRWNP0N/9Svn79XnV9Pc8dN7t1wnhABl3m8XCcChGxaqRfY/UAQKBgFk+ifiNAnDP8tXNpFk2JAfNMxaU3vNEbLrwWoz2oMA4rmQ9izUrdMgnIuXpu7WqzE2OhpKgUsmS5OBXtLVHWTPo+bXP11RcE81oxI60c9qI/HfOa68mDbc2tMsGBAEuLJ8dAuMkwywKl6BV3Q/RJFUEh4dt39E3QAO5zFdSunNVAoGAHUHnNzJT140VZdTY+fjaiWEALXeIghpLYNrf8FGNnkMzzAks9QzPRchsZyMf37mjZQfbzQqg9Q+KUQZ69t59ujPg4K1T73clRjWuetr2boQz4Yrjms0ixhuUNkLiLk4wcPctZDPALh1/aJtK1NzD7wIqrQ0owBe1eWzvgYl5eDw=");
        System.out.println("aa = " + aa);
    }
 
    /**
     * 生成RSA 公私钥,可选长度为1025,2048位.
     */
    public static Map<String,String> generateRsaKey(int keySize) {
        Map<String,String> result = new HashMap<>(2);
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
 
            // 初始化密钥对生成器，密钥大小为1024 2048位
            keyPairGen.initialize(keySize, new SecureRandom());
            // 生成一个密钥对，保存在keyPair中
            KeyPair keyPair = keyPairGen.generateKeyPair();
            // 得到公钥字符串
            result.put("publicKey", new String(Base64.encodeBase64(keyPair.getPublic().getEncoded())));
            // 得到私钥字符串
            result.put("privateKey", new String(Base64.encodeBase64(keyPair.getPrivate().getEncoded())));
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return result;
    }
 
    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) {
        //64位解码加密后的字符串
        byte[] inputByte;
        String outStr = "";
        try {
            inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
            //base64编码的私钥
            byte[] decoded = Base64.decodeBase64(privateKey);
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
            //RSA解密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            outStr = new String(cipher.doFinal(inputByte));
        } catch (UnsupportedEncodingException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return outStr;
    }
 
}
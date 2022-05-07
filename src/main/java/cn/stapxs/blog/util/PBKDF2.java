package cn.stapxs.blog.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 * @Version: 1.0
 * @Date: 2021/9/23 上午 7:51
 * @ClassName: PBKDF2
 * @Author: Stapxs
 * @Description TO DO
 **/
public class PBKDF2 {

    // 加密方式
    private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";
    //盐的长度
    private final int SALT_SIZE;
    //生成密文的长度
    private final int HASH_SIZE;
    // 迭代次数
    private final int PBKDF2_ITERATIONS;

    public PBKDF2(int salt_size, int hash_size, int times) {
        SALT_SIZE = salt_size;
        HASH_SIZE = hash_size;
        PBKDF2_ITERATIONS = times;
    }

    /**
     * @Author Stapxs
     * @Description 生成用于保存的字符串
     * @Date 上午 8:27 2021/9/23
     * @Param [password, salt]
     * @return java.lang.String
     **/
    public String getSaveStr(String password) throws NoSuchAlgorithmException, InvalidKeySpecException, DecoderException {
        // 生成盐水
        String salt = getSalt();
        // 加密
        String out = getPBKDF2(password, salt);
        // 组合字符串
        return "PBKDF2:SHA1:" + PBKDF2_ITERATIONS + ":" + HASH_SIZE + ":" + salt + ":" + out;
    }

    /**
     * @Author Stapxs
     * @Description 验证密码正确性
     * @Date 上午 8:40 2021/9/23
     * @Param [password, dataPassWord]
     * @return boolean
     **/
    public boolean verify(String password, String save)
            throws NoSuchAlgorithmException, InvalidKeySpecException, DecoderException {
        // 拆解字符串
        String[] info = getInfo(save);
        // 用相同的盐值对用户输入的密码进行加密
        String result = getPBKDF2(password, info[4]);
        // 把加密后的密文和原密文进行比较，相同则验证成功，否则失败
        return result.equals(info[5]);
    }

    // --------------------------------------------------------------------------

    /**
     * @Author Stapxs
     * @Description 生成密文
     * @Date 上午 8:28 2021/9/23
     * @Param [password, salt]
     * @return java.lang.String
    **/
    private String getPBKDF2(String password, String salt) throws NoSuchAlgorithmException,
            InvalidKeySpecException, DecoderException {
        //将16进制字符串形式的salt转换成byte数组
        byte[] bytes = Hex.decodeHex(salt.toCharArray());
        KeySpec spec = new PBEKeySpec(password.toCharArray(), bytes, PBKDF2_ITERATIONS, HASH_SIZE * 4);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        byte[] hash = secretKeyFactory.generateSecret(spec).getEncoded();
        //将byte数组转换为16进制的字符串
        return String.valueOf(Hex.encodeHex(hash)).toUpperCase();
    }


    /**
     * @Author Stapxs
     * @Description 生成盐
     * @Date 上午 8:28 2021/9/23
     * @Param []
     * @return java.lang.String
    **/
    private String getSalt() throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] bytes = new byte[SALT_SIZE / 2];
        random.nextBytes(bytes);
        //将byte数组转换为16进制的字符串
        return String.valueOf(Hex.encodeHex(bytes)).toUpperCase();
    }

    /**
     * @Author Stapxs
     * @Description 拆解字符串
     * @Date 上午 8:42 2021/9/23
     * @Param [save]
     * @return java.lang.String[]
    **/
    private String[] getInfo(String save) {
        return save.split(":");
    }
}
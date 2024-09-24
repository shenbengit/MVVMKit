package com.shencoder.mvvmkit.util;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @see org.apache.commons 依赖下的工具类
 *
 * @author ShenBen
 * @date 2019/12/4 17:21
 * @email 714081644@qq.com
 */
@Deprecated
public class AppUtil {
    /**
     * Used to build output as Hex
     */
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f'};

    /**
     * Used to build output as Hex
     */
    private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
            'E', 'F'};

    public static int byteArrayToInt(byte[] byteArray) {
        if (byteArray.length != 4) {
            return 0;
        }
        return byteArray[3] & 0xFF |
                (byteArray[2] & 0xFF) << 8 |
                (byteArray[1] & 0xFF) << 16 |
                (byteArray[0] & 0xFF) << 24;
    }

    public static byte[] intToByteArray(int value) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[4 - i - 1] = (byte) ((value >> 8 * i) & 0xff);
        }
        return b;
    }

    public static String md5EncryptAndBase64(String str) {
        return encodeBase64(md5Encrypt(str));
    }

    /**
     * md5加密
     */
    private static byte[] md5Encrypt(String encryptStr) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(encryptStr.getBytes(StandardCharsets.UTF_8));
            return md5.digest();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * base64加密
     */
    private static String encodeBase64(byte[] b) {
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static byte[] decodeHex(final String data) throws Exception {
        return decodeHex(data.toCharArray());
    }

    public static byte[] decodeHex(final char[] data) throws Exception {

        final int len = data.length;

        if ((len & 0x01) != 0) {
            throw new Exception("Odd number of characters.");
        }

        final byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    private static int toDigit(final char ch, final int index) throws Exception {
        final int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new Exception("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }

    public static char[] encodeHex(final byte[] data) {
        return encodeHex(data, true);
    }

    public static String encodeHexString(final byte[] data, final boolean toLowerCase) {
        return new String(encodeHex(data, toLowerCase));
    }

    public static char[] encodeHex(final byte[] data, final boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    private static char[] encodeHex(final byte[] data, final char[] toDigits) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }
}
